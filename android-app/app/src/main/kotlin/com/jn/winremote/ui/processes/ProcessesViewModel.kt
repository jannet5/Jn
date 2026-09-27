package com.jn.winremote.ui.processes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.winremote.WinRemoteApplication
import com.jn.winremote.protocol.ProcessItem
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.repository.ServerErrorException
import com.jn.winremote.util.CriticalProcessRules
import com.jn.winremote.util.ReasonText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val POLL_INTERVAL_MILLIS = 5_000L

sealed class ProcessesBody {
    object Loading : ProcessesBody()
    data class Content(val items: List<ProcessItem>) : ProcessesBody()
    data class Error(val message: String) : ProcessesBody()
}

data class ProcessesUiState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.Offline,
    val query: String = "",
    val body: ProcessesBody = ProcessesBody.Loading,
    val pendingKillPid: Int? = null,
)

class ProcessesViewModel(application: Application) : AndroidViewModel(application) {

    private val app get() = getApplication<WinRemoteApplication>()

    private val _state = MutableStateFlow(ProcessesUiState())
    val state: StateFlow<ProcessesUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 4)
    val events: SharedFlow<String> = _events.asSharedFlow()

    private var pollJob: Job? = null

    init {
        viewModelScope.launch {
            app.repository.connectionStatus.collect { status ->
                _state.value = _state.value.copy(connectionStatus = status)
                if (status is ConnectionStatus.Connected) startPolling() else stopPolling()
            }
        }
    }

    private fun startPolling() {
        if (pollJob?.isActive == true) return
        pollJob = viewModelScope.launch {
            while (true) {
                refreshNow()
                delay(POLL_INTERVAL_MILLIS)
            }
        }
    }

    private fun stopPolling() {
        pollJob?.cancel()
        pollJob = null
    }

    fun onQueryChanged(query: String) {
        _state.value = _state.value.copy(query = query)
        viewModelScope.launch { refreshNow() }
    }

    fun refreshNow() {
        viewModelScope.launch {
            if (_state.value.connectionStatus !is ConnectionStatus.Connected) return@launch
            val query = _state.value.query.takeIf { it.isNotBlank() }
            val result = app.repository.listProcesses(query)
            result.fold(
                onSuccess = { reply ->
                    _state.value = _state.value.copy(body = ProcessesBody.Content(reply.items))
                },
                onFailure = { err ->
                    _state.value = _state.value.copy(body = ProcessesBody.Error(errorMessage(err)))
                },
            )
        }
    }

    fun requestKill(pid: Int) {
        _state.value = _state.value.copy(pendingKillPid = pid)
    }

    fun cancelKill() {
        _state.value = _state.value.copy(pendingKillPid = null)
    }

    fun confirmKill() {
        val pid = _state.value.pendingKillPid ?: return
        _state.value = _state.value.copy(pendingKillPid = null)
        viewModelScope.launch {
            val result = app.repository.killProcess(pid)
            result.fold(
                onSuccess = { action ->
                    if (action.success) {
                        _events.tryEmit("Süreç sonlandırıldı (PID $pid).")
                    } else {
                        _events.tryEmit(ReasonText.forCode(action.reason))
                    }
                },
                onFailure = { err -> _events.tryEmit(errorMessage(err)) },
            )
            refreshNow()
        }
    }

    /** UI hint only; the server always re-checks (PROTOCOL.md §5). */
    fun isLikelyProtected(item: ProcessItem): Boolean =
        item.isProtected || CriticalProcessRules.isLikelyProtected(item.pid, item.name, item.exe, item.user)

    private fun errorMessage(err: Throwable): String = when (err) {
        is ServerErrorException -> ReasonText.forCode(err.reasonCode)
        else -> "İşlem gerçekleştirilemedi: ${err.message ?: err.javaClass.simpleName}"
    }
}
