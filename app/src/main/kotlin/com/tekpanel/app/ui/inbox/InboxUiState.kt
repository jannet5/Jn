package com.tekpanel.app.ui.inbox

import com.tekpanel.app.data.repository.ChannelUiEntry
import com.tekpanel.app.domain.model.InboxMessage

sealed interface InboxScreenState {
    data object Loading : InboxScreenState
    data object NotificationAccessMissing : InboxScreenState
    data object NoChannelsInstalled : InboxScreenState
    data class Content(
        val channels: List<ChannelUiEntry>,
        val selectedChannelId: String?,
        val allChannelsSelectedButNoneEnabled: Boolean,
        val messages: List<InboxMessage>,
    ) : InboxScreenState
}

/** A just-hidden batch the user can still bring back (spec CAP-13's short undo window). */
data class PendingClearUndo(val previousStates: Map<String, String>, val label: String)
