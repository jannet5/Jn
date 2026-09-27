package com.cepgozcu.app.ui.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.AgentApiException
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.AllowedApp
import com.cepgozcu.app.net.protocol.AppLaunchResult
import com.cepgozcu.app.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppsViewModel(private val connection: ConnectionSource) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<AllowedApp>>>(UiState.Loading)
    val state: StateFlow<UiState<List<AllowedApp>>> = _state.asStateFlow()

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
        runCatching { api.listApps() }
            .onSuccess { list -> _state.value = if (list.isEmpty()) UiState.Empty else UiState.Content(list) }
            .onFailure { e -> _state.value = if (e is AgentApiException) UiState.Error(e.code) else UiState.Offline }
    }

    fun launchApp(appId: String, onResult: (AppLaunchResult?) -> Unit) {
        val api = connection.api ?: run { onResult(null); return }
        viewModelScope.launch {
            val result = runCatching { api.launchApp(appId) }.getOrNull()
            onResult(result)
        }
    }
}
