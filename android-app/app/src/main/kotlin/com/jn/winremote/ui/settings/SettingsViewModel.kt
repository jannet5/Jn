package com.jn.winremote.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.winremote.WinRemoteApplication
import com.jn.winremote.data.PairedDevice
import com.jn.winremote.repository.ConnectionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val devices: List<PairedDevice> = emptyList(),
    val activeDeviceId: String? = null,
    val connectionStatus: ConnectionStatus = ConnectionStatus.Offline,
    val pendingRemoveDeviceId: String? = null,
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val app get() = getApplication<WinRemoteApplication>()

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        refreshDevices()
        viewModelScope.launch {
            app.repository.connectionStatus.collect { status ->
                _state.value = _state.value.copy(connectionStatus = status)
            }
        }
    }

    fun refreshDevices() {
        _state.value = _state.value.copy(
            devices = app.secureStore.listDevices(),
            activeDeviceId = app.secureStore.getActiveDevice()?.deviceId,
        )
    }

    fun switchActive(deviceId: String) {
        val device = app.secureStore.getDevice(deviceId) ?: return
        app.secureStore.setActiveDevice(deviceId)
        app.repository.connect(device)
        refreshDevices()
    }

    fun requestRemove(deviceId: String) {
        _state.value = _state.value.copy(pendingRemoveDeviceId = deviceId)
    }

    fun cancelRemove() {
        _state.value = _state.value.copy(pendingRemoveDeviceId = null)
    }

    fun confirmRemove() {
        val deviceId = _state.value.pendingRemoveDeviceId ?: return
        _state.value = _state.value.copy(pendingRemoveDeviceId = null)
        val wasActive = _state.value.activeDeviceId == deviceId
        app.secureStore.removeDevice(deviceId)
        if (wasActive) {
            val next = app.secureStore.getActiveDevice()
            if (next != null) app.repository.connect(next) else app.repository.disconnect()
        }
        refreshDevices()
    }

    fun disconnect() {
        app.repository.disconnect()
    }

    fun reconnectActive() {
        app.repository.connectToActiveDevice()
    }
}
