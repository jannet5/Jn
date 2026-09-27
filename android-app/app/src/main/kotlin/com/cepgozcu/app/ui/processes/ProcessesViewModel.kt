package com.cepgozcu.app.ui.processes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.AgentApiException
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.ProcessInfo
import com.cepgozcu.app.net.protocol.ProcessKillResult
import com.cepgozcu.app.ui.common.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ProcessSort { CPU, RAM, NAME }

class ProcessesViewModel(private val connection: ConnectionSource) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<ProcessInfo>>>(UiState.Loading)
    private val _query = MutableStateFlow("")
    private val _sort = MutableStateFlow(ProcessSort.CPU)

    val query: StateFlow<String> = _query.asStateFlow()
    val sort: StateFlow<ProcessSort> = _sort.asStateFlow()

    /** Search+sort applied, but state envelope (Loading/Error/etc) preserved from the underlying fetch. */
    val visibleState: StateFlow<UiState<List<ProcessInfo>>> = combine(_state, _query, _sort) { state, query, sort ->
        when (state) {
            is UiState.Content -> {
                val filtered = state.data.filter { it.displayName.orEmpty().contains(query, true) || it.name.contains(query, true) }
                val sorted = when (sort) {
                    ProcessSort.CPU -> filtered.sortedByDescending { it.cpuPercent }
                    ProcessSort.RAM -> filtered.sortedByDescending { it.workingSetBytes }
                    ProcessSort.NAME -> filtered.sortedBy { (it.displayName ?: it.name).lowercase() }
                }
                if (sorted.isEmpty() && query.isNotBlank()) UiState.Empty else UiState.Content(sorted)
            }
            else -> state
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    private var refreshLoopJob: Job? = null

    init {
        viewModelScope.launch {
            connection.connectionState.collect { connState ->
                when (connState) {
                    is ConnectionState.Connected -> startAutoRefresh()
                    is ConnectionState.Unauthorized -> {
                        refreshLoopJob?.cancel()
                        _state.value = UiState.Unauthorized
                    }
                    is ConnectionState.Disconnected -> {
                        refreshLoopJob?.cancel()
                        if (_state.value !is UiState.Content) _state.value = UiState.Offline
                    }
                    ConnectionState.Connecting, ConnectionState.Idle -> {
                        if (_state.value !is UiState.Content) _state.value = UiState.Loading
                    }
                }
            }
        }
    }

    fun setQuery(value: String) { _query.value = value }
    fun setSort(value: ProcessSort) { _sort.value = value }

    fun refresh() { viewModelScope.launch { loadOnce() } }

    private fun startAutoRefresh() {
        refreshLoopJob?.cancel()
        refreshLoopJob = viewModelScope.launch {
            while (true) {
                loadOnce()
                delay(5_000)
            }
        }
    }

    private suspend fun loadOnce() {
        val api = connection.api
        if (api == null) {
            _state.value = UiState.Offline
            return
        }
        if (_state.value !is UiState.Content) _state.value = UiState.Loading
        runCatching { api.listProcesses() }
            .onSuccess { list -> _state.value = if (list.isEmpty()) UiState.Empty else UiState.Content(list) }
            .onFailure { e -> _state.value = if (e is AgentApiException) UiState.Error(e.code) else UiState.Offline }
    }

    fun killProcess(pid: Int, onResult: (ProcessKillResult?) -> Unit) {
        val api = connection.api ?: run { onResult(null); return }
        viewModelScope.launch {
            val result = runCatching { api.killProcess(pid, confirm = true) }.getOrNull()
            onResult(result)
            loadOnce()
        }
    }

    override fun onCleared() {
        refreshLoopJob?.cancel()
        super.onCleared()
    }
}
