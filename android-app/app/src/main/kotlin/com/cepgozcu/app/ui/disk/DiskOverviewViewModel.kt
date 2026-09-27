package com.cepgozcu.app.ui.disk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.AgentApiException
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.DiskOverview
import com.cepgozcu.app.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiskOverviewViewModel(private val connection: ConnectionSource) : ViewModel() {

    private val _state = MutableStateFlow<UiState<DiskOverview>>(UiState.Loading)
    val state: StateFlow<UiState<DiskOverview>> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            connection.connectionState.collect { connState ->
                when (connState) {
                    is ConnectionState.Connected -> loadOnce()
                    is ConnectionState.Unauthorized -> _state.value = UiState.Unauthorized
                    is ConnectionState.Disconnected -> if (_state.value !is UiState.Content) _state.value = UiState.Offline
                    ConnectionState.Connecting, ConnectionState.Idle -> if (_state.value !is UiState.Content) _state.value = UiState.Loading
                }
            }
        }
    }

    fun refresh() { viewModelScope.launch { loadOnce() } }

    private suspend fun loadOnce() {
        val api = connection.api
        if (api == null) {
            _state.value = UiState.Offline
            return
        }
        if (_state.value !is UiState.Content) _state.value = UiState.Loading
        runCatching { api.diskOverview() }
            .onSuccess { _state.value = UiState.Content(it) }
            .onFailure { e -> _state.value = if (e is AgentApiException) UiState.Error(e.code) else UiState.Offline }
    }
}
