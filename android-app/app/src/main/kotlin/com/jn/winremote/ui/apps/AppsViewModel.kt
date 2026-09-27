package com.jn.winremote.ui.apps

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.winremote.WinRemoteApplication
import com.jn.winremote.protocol.AllowedAppItem
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.repository.ServerErrorException
import com.jn.winremote.util.ReasonText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AppsBody {
    object Loading : AppsBody()
    data class Content(val items: List<AllowedAppItem>) : AppsBody()
    data class Error(val message: String) : AppsBody()
}

data class AppsUiState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.Offline,
    val body: AppsBody = AppsBody.Loading,
    val pendingLaunchAppId: String? = null,
    val launchingAppId: String? = null,
)

class AppsViewModel(application: Application) : AndroidViewModel(application) {

    private val app get() = getApplication<WinRemoteApplication>()

    private val _state = MutableStateFlow(AppsUiState())
    val state: StateFlow<AppsUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 4)
    val events: SharedFlow<String> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            app.repository.connectionStatus.collect { status ->
                _state.value = _state.value.copy(connectionStatus = status)
                if (status is ConnectionStatus.Connected) refresh()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            if (_state.value.connectionStatus !is ConnectionStatus.Connected) return@launch
            _state.value = _state.value.copy(body = AppsBody.Loading)
            val result = app.repository.listAllowedApps()
            result.fold(
                onSuccess = { reply -> _state.value = _state.value.copy(body = AppsBody.Content(reply.items)) },
                onFailure = { err -> _state.value = _state.value.copy(body = AppsBody.Error(errorMessage(err))) },
            )
        }
    }

    fun requestLaunch(appId: String) {
        _state.value = _state.value.copy(pendingLaunchAppId = appId)
    }

    fun cancelLaunch() {
        _state.value = _state.value.copy(pendingLaunchAppId = null)
    }

    fun confirmLaunch() {
        val appId = _state.value.pendingLaunchAppId ?: return
        _state.value = _state.value.copy(pendingLaunchAppId = null, launchingAppId = appId)
        viewModelScope.launch {
            val result = app.repository.launchApp(appId)
            result.fold(
                onSuccess = { action ->
                    _events.tryEmit(
                        if (action.success) "Uygulama başlatıldı" + (action.pid?.let { " (PID $it)" } ?: "") + "."
                        else ReasonText.forCode(action.reason)
                    )
                },
                onFailure = { err -> _events.tryEmit(errorMessage(err)) },
            )
            _state.value = _state.value.copy(launchingAppId = null)
        }
    }

    private fun errorMessage(err: Throwable): String = when (err) {
        is ServerErrorException -> ReasonText.forCode(err.reasonCode)
        else -> "İşlem gerçekleştirilemedi: ${err.message ?: err.javaClass.simpleName}"
    }
}
