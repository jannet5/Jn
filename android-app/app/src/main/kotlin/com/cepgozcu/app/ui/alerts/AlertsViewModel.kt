package com.cepgozcu.app.ui.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.data.local.AlertDao
import com.cepgozcu.app.data.local.AlertEntity
import com.cepgozcu.app.net.AgentApiException
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.AlertDto
import com.cepgozcu.app.net.protocol.AlertsQuery
import com.cepgozcu.app.net.protocol.MessageType
import com.cepgozcu.app.net.protocol.WireJson
import com.cepgozcu.app.ui.common.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement

class AlertsViewModel(
    private val connection: ConnectionSource,
    private val alertDao: AlertDao,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<AlertDto>>>(UiState.Loading)
    val state: StateFlow<UiState<List<AlertDto>>> = _state.asStateFlow()

    private var pushJob: Job? = null

    init {
        viewModelScope.launch { loadFromCache() }
        viewModelScope.launch {
            connection.connectionState.collect { connState ->
                when (connState) {
                    is ConnectionState.Connected -> {
                        loadFromNetwork()
                        startPushCollection()
                    }
                    is ConnectionState.Unauthorized -> {
                        pushJob?.cancel()
                        _state.value = UiState.Unauthorized
                    }
                    is ConnectionState.Disconnected -> {
                        pushJob?.cancel()
                        if (_state.value !is UiState.Content) loadFromCache()
                    }
                    ConnectionState.Connecting, ConnectionState.Idle -> Unit
                }
            }
        }
    }

    fun refresh() { viewModelScope.launch { loadFromNetwork() } }

    private suspend fun loadFromCache() {
        val cached = alertDao.getRecent().map { it.toDto() }
        _state.value = if (cached.isEmpty()) UiState.Offline else UiState.Content(cached)
    }

    private suspend fun loadFromNetwork() {
        val api = connection.api
        if (api == null) {
            loadFromCache()
            return
        }
        if (_state.value !is UiState.Content) _state.value = UiState.Loading
        runCatching { api.alerts(AlertsQuery(limit = 100)) }
            .onSuccess { list ->
                if (list.isNotEmpty()) alertDao.upsertAndTrim(list.map { AlertEntity.fromDto(it) })
                _state.value = if (list.isEmpty()) UiState.Empty else UiState.Content(list)
            }
            .onFailure { e ->
                if (e is AgentApiException) _state.value = UiState.Error(e.code) else loadFromCache()
            }
    }

    private fun startPushCollection() {
        pushJob?.cancel()
        val pushes = connection.pushes ?: return
        pushJob = viewModelScope.launch {
            pushes.collect { envelope ->
                if (envelope.type == MessageType.ALERT_PUSH) {
                    val payload = envelope.payload ?: return@collect
                    val alert = runCatching { WireJson.decodeFromJsonElement<AlertDto>(payload) }.getOrNull() ?: return@collect
                    alertDao.upsertAndTrim(listOf(AlertEntity.fromDto(alert)))
                    val current = (_state.value as? UiState.Content)?.data.orEmpty()
                    val merged = (listOf(alert) + current).distinctBy { it.id }
                    _state.value = UiState.Content(merged)
                }
            }
        }
    }

    override fun onCleared() {
        pushJob?.cancel()
        super.onCleared()
    }
}
