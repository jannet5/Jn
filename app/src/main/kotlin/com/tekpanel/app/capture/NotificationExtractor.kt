package com.tekpanel.app.capture

import android.app.Notification
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat

/**
 * Converts a live `StatusBarNotification` into a [RawNotificationPayload] (spec CAP-04).
 *
 * This is the only class in the capture pipeline that touches Android notification
 * framework APIs directly; everything it produces is plain data so the rest of the
 * pipeline is unit-testable without Robolectric.
 */
object NotificationExtractor {

    fun extract(sbn: StatusBarNotification): RawNotificationPayload {
        val notification = sbn.notification
        val extras = notification.extras
        val messagingStyle = NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(notification)

        val textLines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
            ?.map { it.toString() }
            ?: emptyList()

        val isGroupSummary = (notification.flags and Notification.FLAG_GROUP_SUMMARY) != 0
        val isOngoing = (notification.flags and Notification.FLAG_ONGOING_EVENT) != 0

        return RawNotificationPayload(
            notificationKey = sbn.key,
            packageName = sbn.packageName,
            postTimeEpochMillis = sbn.postTime,
            title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString(),
            text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString(),
            bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString(),
            textLines = textLines,
            conversationTitle = extras.getCharSequence(Notification.EXTRA_CONVERSATION_TITLE)?.toString(),
            messagingStyleMessages = messagingStyle?.messages.orEmpty().map { message ->
                MessagingLine(sender = message.person?.name?.toString(), text = message.text?.toString().orEmpty())
            },
            category = notification.category,
            isGroupSummary = isGroupSummary,
            isOngoing = isOngoing,
            hasContentIntent = notification.contentIntent != null,
        )
    }
}
