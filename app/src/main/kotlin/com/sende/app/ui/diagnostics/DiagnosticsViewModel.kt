package com.sende.app.ui.diagnostics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sende.app.data.repository.InboxRepository
import com.sende.app.diagnostics.DiagnosticsRecorder
import com.sende.app.diagnostics.DiagnosticsSnapshot
import com.sende.app.util.NotificationAccessChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DiagnosticsUiState(val snapshot: DiagnosticsSnapshot, val roomRowCount: Int)

/**
 * CAP-16: read-only view of the counters in [DiagnosticsRecorder] plus a live Room row
 * count. Deliberately exposes no message title/text field anywhere in this model.
 */
class DiagnosticsViewModel(
    private val diagnosticsRecorder: DiagnosticsRecorder,
    private val inboxRepository: InboxRepository,
    private val notificationAccessChecker: NotificationAccessChecker,
) : ViewModel() {

    private val roomRowCount = MutableStateFlow(0)

    val uiState: StateFlow<DiagnosticsUiState> = combine(diagnosticsRecorder.snapshot, roomRowCount) { snapshot, count ->
        DiagnosticsUiState(snapshot.copy(notificationAccessGranted = notificationAccessChecker.isGranted()), count)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DiagnosticsUiState(DiagnosticsSnapshot(), 0))

    init {
        refreshRoomCount()
    }

    fun refreshRoomCount() {
        viewModelScope.launch { roomRowCount.value = inboxRepository.countAll() }
    }
}
