package com.cepgozcu.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.AgentApiException
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.MessageType
import com.cepgozcu.app.net.protocol.SystemMetrics
import com.cepgozcu.app.net.protocol.WireJson
import com.cepgozcu.app.ui.common.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement

class DashboardViewModel(private val connection: ConnectionSource) : ViewModel() {

    private val _state = MutableStateFlow<UiState<SystemMetrics>>(UiState.Loading)
    val state: StateFlow<UiState<SystemMetrics>> = _state.asStateFlow()

    private var pushJob: Job? = null

    init {
        viewModelScope.launch {
            connection.connectionState.collect { connState ->
                when (connState) {
                    is ConnectionState.Connected -> {
                        loadMetrics()
                        startPushCollection()
                    }
                    is ConnectionState.Unauthorized -> {
                        pushJob?.cancel()
                        _state.value = UiState.Unauthorized
                    }
                    is ConnectionState.Disconnected -> {
                        pushJob?.cancel()
                        if (_state.value !is UiState.Content) _state.value = UiState.Offline
                    }
                    ConnectionState.Connecting, ConnectionState.Idle -> {
                        if (_state.value !is UiState.Content) _state.value = UiState.Loading
                    }
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch { loadMetrics() }
    }

    private suspend fun loadMetrics() {
        val api = connection.api
        if (api == null) {
            _state.value = UiState.Offline
            return
        }
        if (_state.value !is UiState.Content) _state.value = UiState.Loading
        runCatching { api.getMetrics() }
            .onSuccess { _state.value = UiState.Content(it) }
            .onFailure { e -> _state.value = mapError(e) }
    }

    private fun mapError(e: Throwable): UiState<SystemMetrics> = when (e) {
        is AgentApiException -> UiState.Error(e.code)
        else -> UiState.Offline
    }

    private fun startPushCollection() {
        pushJob?.cancel()
        val pushes = connection.pushes ?: return
        pushJob = viewModelScope.launch {
            pushes.collect { envelope ->
                if (envelope.type == MessageType.METRICS_UPDATE) {
                    val payload = envelope.payload ?: return@collect
                    val metrics = runCatching { WireJson.decodeFromJsonElement<SystemMetrics>(payload) }.getOrNull()
                    if (metrics != null) _state.value = UiState.Content(metrics)
                }
            }
        }
    }

    override fun onCleared() {
        pushJob?.cancel()
        super.onCleared()
    }
}
