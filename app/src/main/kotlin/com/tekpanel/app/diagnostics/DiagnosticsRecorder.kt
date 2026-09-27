package com.tekpanel.app.diagnostics

import com.tekpanel.app.domain.model.FilterReason
import java.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Backing store for the hidden diagnostics surface (spec CAP-16).
 *
 * Deliberately never stores message title/text: every field here is metadata (package,
 * channel id, counters, timestamps) that is safe to show on screen or include in a support
 * export without leaking a customer's conversation.
 */
data class DiagnosticsSnapshot(
    val notificationAccessGranted: Boolean = false,
    val listenerConnected: Boolean = false,
    val lastConnectedAt: Instant? = null,
    val lastSeenPackage: String? = null,
    val lastSeenChannelId: String? = null,
    val lastDecisionReason: FilterReason? = null,
    val lastDecisionAt: Instant? = null,
    val lastAcceptedAt: Instant? = null,
    val totalAccepted: Int = 0,
    val totalDuplicatesSuppressed: Int = 0,
    val rejectedByReason: Map<FilterReason, Int> = emptyMap(),
)

class DiagnosticsRecorder {

    private val _snapshot = MutableStateFlow(DiagnosticsSnapshot())
    val snapshot: StateFlow<DiagnosticsSnapshot> = _snapshot

    fun onListenerConnected() {
        _snapshot.update { it.copy(listenerConnected = true, lastConnectedAt = Instant.now()) }
    }

    fun onListenerDisconnected() {
        _snapshot.update { it.copy(listenerConnected = false) }
    }

    fun onNotificationAccessState(granted: Boolean) {
        _snapshot.update { it.copy(notificationAccessGranted = granted) }
    }

    fun onDecision(packageName: String, channelId: String?, reason: FilterReason) {
        _snapshot.update { current ->
            val now = Instant.now()
            when (reason) {
                FilterReason.ACCEPTED -> current.copy(
                    lastSeenPackage = packageName,
                    lastSeenChannelId = channelId,
                    lastDecisionReason = reason,
                    lastDecisionAt = now,
                    lastAcceptedAt = now,
                    totalAccepted = current.totalAccepted + 1,
                )
                FilterReason.DUPLICATE_BY_KEY, FilterReason.DUPLICATE_BY_FINGERPRINT -> current.copy(
                    lastSeenPackage = packageName,
                    lastSeenChannelId = channelId,
                    lastDecisionReason = reason,
                    lastDecisionAt = now,
                    totalDuplicatesSuppressed = current.totalDuplicatesSuppressed + 1,
                )
                else -> current.copy(
                    lastSeenPackage = packageName,
                    lastSeenChannelId = channelId,
                    lastDecisionReason = reason,
                    lastDecisionAt = now,
                    rejectedByReason = current.rejectedByReason + (reason to (current.rejectedByReason[reason] ?: 0) + 1),
                )
            }
        }
    }

    fun reset() {
        _snapshot.value = DiagnosticsSnapshot()
    }
}
