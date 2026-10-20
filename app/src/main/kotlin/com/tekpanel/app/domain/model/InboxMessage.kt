package com.tekpanel.app.domain.model

import java.time.Instant

/**
 * A single captured message. [conversationKey] groups messages of the same chat/feed
 * (package + normalized conversation title); [sourcePackage] is also the channel id, since
 * a channel is simply an installed app the user chose to listen to.
 */
data class InboxMessage(
    val id: String,
    val notificationKey: String?,
    val fingerprint: String,
    val sourcePackage: String,
    val conversationKey: String,
    val conversationTitle: String,
    val isGroupConversation: Boolean,
    val senderName: String,
    val messageText: String,
    val receivedAt: Instant,
    val capturedAt: Instant,
    val captureMethod: CaptureMethod,
    val state: MessageState,
    val avatarPath: String?,
    val picturePath: String?,
)
