package com.cepgozcu.app.ui.disk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.AgentApiException
import com.cepgozcu.app.net.protocol.DiskGrowthQuery
import com.cepgozcu.app.net.protocol.DiskGrowthResult
import com.cepgozcu.app.net.protocol.GrowthRange
import com.cepgozcu.app.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiskGrowthViewModel(private val connection: ConnectionSource) : ViewModel() {

    private val _state = MutableStateFlow<UiState<DiskGrowthResult>>(UiState.Loading)
    val state: StateFlow<UiState<DiskGrowthResult>> = _state.asStateFlow()

    private val _range = MutableStateFlow(GrowthRange.LastDay)
    val range: StateFlow<GrowthRange> = _range.asStateFlow()

    private val _rootPath = MutableStateFlow<String?>(null)

    /** Called once the Overview tab knows the watched roots, so Growth has something sensible to query by default. */
    fun setDefaultRootPathIfUnset(path: String) {
        if (_rootPath.value == null) {
            _rootPath.value = path
            load()
        }
    }

    fun setRange(range: GrowthRange) {
        _range.value = range
        load()
    }

    fun refresh() = load()

    private fun load() {
        val root = _rootPath.value ?: return
        val api = connection.api
        if (api == null) {
            _state.value = UiState.Offline
            return
        }
        viewModelScope.launch {
            if (_state.value !is UiState.Content) _state.value = UiState.Loading
            runCatching { api.diskGrowth(DiskGrowthQuery(rootPath = root, range = _range.value)) }
                .onSuccess { _state.value = UiState.Content(it) }
                .onFailure { e -> _state.value = if (e is AgentApiException) UiState.Error(e.code) else UiState.Offline }
        }
    }
}
