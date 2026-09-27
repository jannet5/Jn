package com.jn.winremote.ui.fileactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.winremote.WinRemoteApplication
import com.jn.winremote.protocol.FileEventData
import com.jn.winremote.protocol.TopGrowthItem
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.repository.ServerErrorException
import com.jn.winremote.util.FileEventFilter
import com.jn.winremote.util.ReasonText
import com.jn.winremote.util.applyFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val MAX_LIVE_EVENTS = 500

sealed class LoadState<out T> {
    object Loading : LoadState<Nothing>()
    data class Content<T>(val value: T) : LoadState<T>()
    data class Error(val message: String) : LoadState<Nothing>()
}

data class FileActivityUiState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.Offline,
    val tabIndex: Int = 0,
    val filter: FileEventFilter = FileEventFilter(),
    val events: List<FileEventData> = emptyList(),
    val eventsLoad: LoadState<Unit> = LoadState.Loading,
    val growthWindow: String = "1h",
    val growthLoad: LoadState<List<TopGrowthItem>> = LoadState.Loading,
)

class FileActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val app get() = getApplication<WinRemoteApplication>()

    private val _state = MutableStateFlow(FileActivityUiState())
    val state: StateFlow<FileActivityUiState> = _state.asStateFlow()

    fun currentFilteredEvents(): List<FileEventData> = _state.value.events.applyFilter(_state.value.filter)

    init {
        viewModelScope.launch {
            app.repository.connectionStatus.collect { status ->
                val was = _state.value.connectionStatus
                _state.value = _state.value.copy(connectionStatus = status)
                if (status is ConnectionStatus.Connected && was !is ConnectionStatus.Connected) {
                    loadInitialEvents()
                    loadGrowth()
                }
            }
        }
        viewModelScope.launch {
            app.repository.liveFileEvents.collect { event ->
                val current = _state.value.events
                _state.value = _state.value.copy(events = (listOf(event) + current).take(MAX_LIVE_EVENTS))
            }
        }
    }

    fun selectTab(index: Int) {
        _state.value = _state.value.copy(tabIndex = index)
    }

    fun updateFilter(transform: (FileEventFilter) -> FileEventFilter) {
        _state.value = _state.value.copy(filter = transform(_state.value.filter))
    }

    fun loadInitialEvents() {
        viewModelScope.launch {
            if (_state.value.connectionStatus !is ConnectionStatus.Connected) return@launch
            _state.value = _state.value.copy(eventsLoad = LoadState.Loading)
            val result = app.repository.listFileEvents(limit = 200)
            result.fold(
                onSuccess = { reply ->
                    _state.value = _state.value.copy(
                        events = reply.items.sortedByDescending { it.ts }.take(MAX_LIVE_EVENTS),
                        eventsLoad = LoadState.Content(Unit),
                    )
                },
                onFailure = { err ->
                    _state.value = _state.value.copy(eventsLoad = LoadState.Error(errorMessage(err)))
                },
            )
        }
    }

    fun setGrowthWindow(window: String) {
        _state.value = _state.value.copy(growthWindow = window)
        loadGrowth()
    }

    fun loadGrowth() {
        viewModelScope.launch {
            if (_state.value.connectionStatus !is ConnectionStatus.Connected) return@launch
            _state.value = _state.value.copy(growthLoad = LoadState.Loading)
            val result = app.repository.topGrowth(_state.value.growthWindow)
            result.fold(
                onSuccess = { reply -> _state.value = _state.value.copy(growthLoad = LoadState.Content(reply.items)) },
                onFailure = { err -> _state.value = _state.value.copy(growthLoad = LoadState.Error(errorMessage(err))) },
            )
        }
    }

    private fun errorMessage(err: Throwable): String = when (err) {
        is ServerErrorException -> ReasonText.forCode(err.reasonCode)
        else -> "Veri alınamadı: ${err.message ?: err.javaClass.simpleName}"
    }
}
