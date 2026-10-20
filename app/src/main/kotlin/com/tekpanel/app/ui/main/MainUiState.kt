package com.tekpanel.app.ui.main

import androidx.compose.runtime.Immutable
import com.tekpanel.app.domain.model.Conversation

/** One bubble in the channel row on the main screen. */
@Immutable
data class BarItem(val packageName: String, val label: String, val unread: Int)

@Immutable
data class MainUiState(
    val loaded: Boolean = false,
    val accessGranted: Boolean = true,
    val bar: List<BarItem> = emptyList(),
    val selectedPackage: String? = null,
    val totalUnread: Int = 0,
    val conversations: List<Conversation> = emptyList(),
    val labels: Map<String, String> = emptyMap(),
    val hasEnabledChannels: Boolean = false,
    /** Access is granted but the system is not delivering notifications (listener unbound for a while). */
    val listenerStalled: Boolean = false,
)

/** How much of a conversation card is open. */
enum class Level { Collapsed, Children, Full }

/** A just-handled batch the user can still bring back. */
data class UndoRequest(val previousStates: Map<String, String>, val label: String)

sealed interface UiEvent {
    data object Copied : UiEvent
    data object OpenFailed : UiEvent
    data object ReplySent : UiEvent
    data object ReplyFailed : UiEvent
    data class ExportReady(val json: String) : UiEvent
}
