package com.tekpanel.app.ui.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tekpanel.app.data.prefs.AppPreferences
import com.tekpanel.app.data.repository.ChannelRepository
import com.tekpanel.app.data.repository.ChannelUiEntry
import com.tekpanel.app.domain.usecase.EraseAllDataUseCase
import com.tekpanel.app.export.DataExporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChannelSettingsViewModel(
    private val channelRepository: ChannelRepository,
    private val dataExporter: DataExporter,
    private val appPreferences: AppPreferences,
    private val eraseAllDataUseCase: EraseAllDataUseCase,
) : ViewModel() {

    val channels: StateFlow<List<ChannelUiEntry>> = channelRepository.observeInstalledChannels()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val diagnosticsUnlocked: StateFlow<Boolean> = appPreferences.diagnosticsUnlocked
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private val _exportedJson = MutableStateFlow<String?>(null)
    val exportedJson: StateFlow<String?> = _exportedJson

    fun setChannelEnabled(channelId: String, enabled: Boolean) {
        viewModelScope.launch { channelRepository.setChannelEnabled(channelId, enabled) }
    }

    fun enableAll() {
        viewModelScope.launch { channelRepository.setAllInstalledEnabled(true) }
    }

    fun disableAll() {
        viewModelScope.launch { channelRepository.setAllInstalledEnabled(false) }
    }

    fun requestExport() {
        viewModelScope.launch { _exportedJson.value = dataExporter.exportAsJson() }
    }

    fun consumeExport() {
        _exportedJson.value = null
    }

    fun unlockDiagnostics() {
        viewModelScope.launch { appPreferences.setDiagnosticsUnlocked(true) }
    }

    fun eraseAllData() {
        viewModelScope.launch { eraseAllDataUseCase() }
    }
}
