package com.jn.winremote.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.winremote.WinRemoteApplication
import com.jn.winremote.protocol.HistoryItem
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.repository.ServerErrorException
import com.jn.winremote.util.ReasonText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 50

sealed class HistoryBody {
    object Loading : HistoryBody()
    data class Content(val items: List<HistoryItem>, val total: Int) : HistoryBody()
    data class Error(val message: String) : HistoryBody()
}

data class HistoryUiState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.Offline,
    val body: HistoryBody = HistoryBody.Loading,
    val isLoadingMore: Boolean = false,
)

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val app get() = getApplication<WinRemoteApplication>()

    private val _state = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            app.repository.connectionStatus.collect { status ->
                val was = _state.value.connectionStatus
                _state.value = _state.value.copy(connectionStatus = status)
                if (status is ConnectionStatus.Connected && was !is ConnectionStatus.Connected) refresh()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            if (_state.value.connectionStatus !is ConnectionStatus.Connected) return@launch
            _state.value = _state.value.copy(body = HistoryBody.Loading)
            val result = app.repository.listHistory(limit = PAGE_SIZE, offset = 0)
            result.fold(
                onSuccess = { reply ->
                    _state.value = _state.value.copy(body = HistoryBody.Content(reply.items, reply.total))
                },
                onFailure = { err -> _state.value = _state.value.copy(body = HistoryBody.Error(errorMessage(err))) },
            )
        }
    }

    fun loadMore() {
        val current = _state.value.body as? HistoryBody.Content ?: return
        if (_state.value.isLoadingMore || current.items.size >= current.total) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingMore = true)
            val result = app.repository.listHistory(limit = PAGE_SIZE, offset = current.items.size)
            result.fold(
                onSuccess = { reply ->
                    _state.value = _state.value.copy(
                        body = HistoryBody.Content(current.items + reply.items, reply.total),
                        isLoadingMore = false,
                    )
                },
                onFailure = { _state.value = _state.value.copy(isLoadingMore = false) },
            )
        }
    }

    private fun errorMessage(err: Throwable): String = when (err) {
        is ServerErrorException -> ReasonText.forCode(err.reasonCode)
        else -> "Geçmiş alınamadı: ${err.message ?: err.javaClass.simpleName}"
    }
}
