package com.cepgozcu.app.ui.disk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.data.local.FileEventDao
import com.cepgozcu.app.data.local.FileEventEntity
import com.cepgozcu.app.net.AgentApiException
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.DiskEventsQuery
import com.cepgozcu.app.net.protocol.FileEventDto
import com.cepgozcu.app.net.protocol.FileEventKind
import com.cepgozcu.app.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** null means "no filter" (show every kind). */
class DiskHistoryViewModel(
    private val connection: ConnectionSource,
    private val fileEventDao: FileEventDao,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<FileEventDto>>>(UiState.Loading)
    val state: StateFlow<UiState<List<FileEventDto>>> = _state.asStateFlow()

    private val _filter = MutableStateFlow<FileEventKind?>(null)
    val filter: StateFlow<FileEventKind?> = _filter.asStateFlow()

    private val _canLoadMore = MutableStateFlow(false)
    val canLoadMore: StateFlow<Boolean> = _canLoadMore.asStateFlow()

    private var nextCursor: Long? = null
    private var isOnline = false

    init {
        viewModelScope.launch {
            loadFromCache()
        }
        viewModelScope.launch {
            connection.connectionState.collect { connState ->
                when (connState) {
                    is ConnectionState.Connected -> {
                        isOnline = true
                        loadInitialFromNetwork()
                    }
                    is ConnectionState.Unauthorized -> {
                        isOnline = false
                        _state.value = UiState.Unauthorized
                    }
                    is ConnectionState.Disconnected -> {
                        isOnline = false
                        if (_state.value !is UiState.Content) loadFromCache()
                    }
                    ConnectionState.Connecting, ConnectionState.Idle -> {
                        isOnline = false
                    }
                }
            }
        }
    }

    fun setFilter(kind: FileEventKind?) {
        _filter.value = kind
        viewModelScope.launch {
            if (isOnline) loadInitialFromNetwork() else loadFromCache()
        }
    }

    fun refresh() { viewModelScope.launch { if (isOnline) loadInitialFromNetwork() else loadFromCache() } }

    fun loadMore() {
        val cursor = nextCursor ?: return
        val api = connection.api ?: return
        viewModelScope.launch {
            runCatching {
                api.diskEvents(DiskEventsQuery(kinds = _filter.value?.let { listOf(it) }, cursor = cursor, limit = 100))
            }.onSuccess { page ->
                cacheEvents(page.items)
                nextCursor = page.nextCursor
                _canLoadMore.value = page.nextCursor != null
                val current = (_state.value as? UiState.Content)?.data.orEmpty()
                _state.value = UiState.Content(current + page.items)
            }
        }
    }

    private suspend fun loadFromCache() {
        val cached = fileEventDao.getRecent()
            .filter { _filter.value == null || it.kind == _filter.value }
            .map { it.toDto() }
        _state.value = if (cached.isEmpty()) UiState.Offline else UiState.Content(cached)
        _canLoadMore.value = false
    }

    private suspend fun loadInitialFromNetwork() {
        val api = connection.api
        if (api == null) {
            loadFromCache()
            return
        }
        if (_state.value !is UiState.Content) _state.value = UiState.Loading
        runCatching {
            api.diskEvents(DiskEventsQuery(kinds = _filter.value?.let { listOf(it) }, limit = 100))
        }.onSuccess { page ->
            cacheEvents(page.items)
            nextCursor = page.nextCursor
            _canLoadMore.value = page.nextCursor != null
            _state.value = if (page.items.isEmpty()) UiState.Empty else UiState.Content(page.items)
        }.onFailure { e ->
            if (e is AgentApiException) {
                _state.value = UiState.Error(e.code)
            } else {
                loadFromCache()
            }
        }
    }

    private suspend fun cacheEvents(items: List<FileEventDto>) {
        if (items.isEmpty()) return
        fileEventDao.upsertAndTrim(items.map { FileEventEntity.fromDto(it) })
    }
}
