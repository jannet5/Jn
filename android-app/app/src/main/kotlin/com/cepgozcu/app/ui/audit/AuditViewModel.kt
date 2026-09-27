package com.cepgozcu.app.ui.audit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.data.local.AuditDao
import com.cepgozcu.app.data.local.AuditEntity
import com.cepgozcu.app.net.AgentApiException
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.AuditEntry
import com.cepgozcu.app.net.protocol.AuditQuery
import com.cepgozcu.app.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuditViewModel(
    private val connection: ConnectionSource,
    private val auditDao: AuditDao,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<AuditEntry>>>(UiState.Loading)
    val state: StateFlow<UiState<List<AuditEntry>>> = _state.asStateFlow()

    init {
        viewModelScope.launch { loadFromCache() }
        viewModelScope.launch {
            connection.connectionState.collect { connState ->
                when (connState) {
                    is ConnectionState.Connected -> loadFromNetwork()
                    is ConnectionState.Unauthorized -> _state.value = UiState.Unauthorized
                    is ConnectionState.Disconnected -> if (_state.value !is UiState.Content) loadFromCache()
                    ConnectionState.Connecting, ConnectionState.Idle -> Unit
                }
            }
        }
    }

    fun refresh() { viewModelScope.launch { loadFromNetwork() } }

    private suspend fun loadFromCache() {
        val cached = auditDao.getRecent().map { it.toDto() }
        _state.value = if (cached.isEmpty()) UiState.Offline else UiState.Content(cached)
    }

    private suspend fun loadFromNetwork() {
        val api = connection.api
        if (api == null) {
            loadFromCache()
            return
        }
        if (_state.value !is UiState.Content) _state.value = UiState.Loading
        runCatching { api.audit(AuditQuery(limit = 100)) }
            .onSuccess { list ->
                if (list.isNotEmpty()) auditDao.upsertAndTrim(list.map { AuditEntity.fromDto(it) })
                _state.value = if (list.isEmpty()) UiState.Empty else UiState.Content(list)
            }
            .onFailure { e ->
                if (e is AgentApiException) _state.value = UiState.Error(e.code) else loadFromCache()
            }
    }
}
