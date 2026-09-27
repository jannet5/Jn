package com.cepgozcu.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionRepository
import com.cepgozcu.app.security.AgentSession
import kotlinx.coroutines.launch

class SettingsViewModel(private val connectionRepository: ConnectionRepository) : ViewModel() {

    val session: AgentSession?
        get() = connectionRepository.currentSession

    fun unpair(onDone: () -> Unit) {
        viewModelScope.launch {
            connectionRepository.unpairAndClear()
            onDone()
        }
    }
}
