package com.tekpanel.app.capture

/**
 * Android-independent view of everything TekPanel is allowed to read off a
 * `StatusBarNotification` (spec CAP-04). [NotificationExtractor] builds this from the live
 * framework object; everything downstream ([MessageNormalizer], [MessageFilterEngine],
 * [Fingerprint]) is plain Kotlin and runs in a JVM unit test without Robolectric.
 */
data class RawNotificationPayload(
    val notificationKey: String,
    val packageName: String,
    val postTimeEpochMillis: Long,
    val title: String?,
    val text: String?,
    val bigText: String?,
    val textLines: List<String>,
    val conversationTitle: String?,
    val messagingStyleMessages: List<MessagingLine>,
    val category: String?,
    val isGroupSummary: Boolean,
    val isOngoing: Boolean,
    val hasContentIntent: Boolean,
)

/** One line of a `NotificationCompat.MessagingStyle` conversation, oldest to newest. */
data class MessagingLine(val sender: String?, val text: String)
