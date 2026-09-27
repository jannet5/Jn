package com.tekpanel.app.domain.model

import java.time.Instant

/**
 * Common model every accepted capture is normalized into (spec CAP-05).
 *
 * [fingerprint] is a secondary duplicate-detection key derived from normalized
 * package + sender + message + a coarse time bucket, used when [notificationKey]
 * is unavailable or the listener reconnects and re-enumerates active notifications (CAP-07).
 */
data class InboxMessage(
    val id: String,
    val notificationKey: String?,
    val fingerprint: String,
    val sourcePackage: String,
    val channelId: String,
    val senderName: String,
    val messageText: String,
    val receivedAt: Instant,
    val capturedAt: Instant,
    val captureMethod: CaptureMethod,
    val state: MessageState,
    val canOpenLiveConversation: Boolean,
)
