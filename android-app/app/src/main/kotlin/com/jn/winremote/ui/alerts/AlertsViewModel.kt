package com.jn.winremote.ui.alerts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.winremote.WinRemoteApplication
import com.jn.winremote.protocol.AlertData
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.repository.ServerErrorException
import com.jn.winremote.util.ReasonText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val MAX_ALERTS = 200
private const val BANNER_DURATION_MILLIS = 6_000L

sealed class AlertsBody {
    object Loading : AlertsBody()
    data class Content(val items: List<AlertData>) : AlertsBody()
    data class Error(val message: String) : AlertsBody()
}

data class AlertsUiState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.Offline,
    val body: AlertsBody = AlertsBody.Loading,
    val banner: AlertData? = null,
)

class AlertsViewModel(application: Application) : AndroidViewModel(application) {

    private val app get() = getApplication<WinRemoteApplication>()

    private val _state = MutableStateFlow(AlertsUiState())
    val state: StateFlow<AlertsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            app.repository.connectionStatus.collect { status ->
                val was = _state.value.connectionStatus
                _state.value = _state.value.copy(connectionStatus = status)
                if (status is ConnectionStatus.Connected && was !is ConnectionStatus.Connected) refresh()
            }
        }
        viewModelScope.launch {
            app.repository.liveAlerts.collect { alert ->
                val existing = (state.value.body as? AlertsBody.Content)?.items ?: emptyList()
                _state.value = _state.value.copy(
                    body = AlertsBody.Content((listOf(alert) + existing).take(MAX_ALERTS)),
                    banner = alert,
                )
                // A fresh, independent timer per alert so the banner always reflects the most
                // recent one and this collector never blocks on `delay` (which would otherwise
                // stall processing of alerts that arrive while one is already showing).
                bannerJob?.cancel()
                bannerJob = viewModelScope.launch {
                    delay(BANNER_DURATION_MILLIS)
                    _state.value = _state.value.copy(banner = null)
                }
            }
        }
    }

    private var bannerJob: Job? = null

    fun dismissBanner() {
        _state.value = _state.value.copy(banner = null)
    }

    fun refresh() {
        viewModelScope.launch {
            if (_state.value.connectionStatus !is ConnectionStatus.Connected) return@launch
            _state.value = _state.value.copy(body = AlertsBody.Loading)
            val result = app.repository.listAlerts()
            result.fold(
                onSuccess = { reply -> _state.value = _state.value.copy(body = AlertsBody.Content(reply.items)) },
                onFailure = { err -> _state.value = _state.value.copy(body = AlertsBody.Error(errorMessage(err))) },
            )
        }
    }

    private fun errorMessage(err: Throwable): String = when (err) {
        is ServerErrorException -> ReasonText.forCode(err.reasonCode)
        else -> "Uyarılar alınamadı: ${err.message ?: err.javaClass.simpleName}"
    }
}
