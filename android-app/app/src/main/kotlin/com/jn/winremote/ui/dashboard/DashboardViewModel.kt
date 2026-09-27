package com.jn.winremote.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.winremote.WinRemoteApplication
import com.jn.winremote.protocol.DiskInfo
import com.jn.winremote.protocol.ServerMessage
import com.jn.winremote.repository.ConnectionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val HISTORY_SIZE = 60 // ~2 minutes at the spec's 2s metrics push interval

data class DashboardUiState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.Offline,
    val latest: ServerMessage.Metrics? = null,
    val cpuHistory: List<Float> = emptyList(),
    val ramHistory: List<Float> = emptyList(),
    val disks: List<DiskInfo> = emptyList(),
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val app get() = getApplication<WinRemoteApplication>()

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            app.repository.connectionStatus.collect { status ->
                _state.value = _state.value.copy(connectionStatus = status)
            }
        }
        viewModelScope.launch {
            app.repository.metrics.collect { metrics ->
                if (metrics != null) onMetrics(metrics)
            }
        }
    }

    private fun onMetrics(metrics: ServerMessage.Metrics) {
        val ramPercent = if (metrics.ram.totalBytes > 0) {
            (metrics.ram.usedBytes.toDouble() / metrics.ram.totalBytes.toDouble() * 100.0).toFloat()
        } else 0f
        val current = _state.value
        _state.value = current.copy(
            latest = metrics,
            cpuHistory = (current.cpuHistory + metrics.cpuPercent.toFloat()).takeLast(HISTORY_SIZE),
            ramHistory = (current.ramHistory + ramPercent).takeLast(HISTORY_SIZE),
            disks = metrics.disks,
        )
    }

    fun retryConnect() {
        app.repository.connectToActiveDevice()
    }
}
