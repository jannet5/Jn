package com.sende.app.capture

import android.app.Notification
import android.content.Context
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat

/**
 * Converts a live `StatusBarNotification` into a [RawNotificationPayload].
 *
 * This is the only class in the capture pipeline (besides [AndroidNotificationMedia]) that
 * touches the notification framework directly; everything it produces is plain data so the
 * rest of the pipeline is unit-testable without Robolectric.
 */
object NotificationExtractor {

    fun extract(context: Context, sbn: StatusBarNotification): RawNotificationPayload {
        val notification = sbn.notification
        val extras = notification.extras

        // Some OEM ROMs throw while parsing EXTRA_MESSAGES. If it ever does, the title/text
        // below must still survive instead of losing the whole notification.
        val messagingStyle = try {
            NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(notification)
        } catch (_: Exception) {
            null
        }

        val textLines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
            ?.map { it.toString() }
            ?: emptyList()

        val styleUser = messagingStyle?.user
        // A message without a Person is the phone owner's only when the app names everyone else.
        val othersAreNamed = messagingStyle?.messages.orEmpty().any { it.person != null }
        val lines = messagingStyle?.messages.orEmpty().mapIndexed { index, message ->
            val person = message.person
            val fromUser = if (person == null) {
                othersAreNamed
            } else {
                (person.key != null && person.key == styleUser?.key) ||
                    (!person.name.isNullOrBlank() && person.name.toString() == styleUser?.name?.toString())
            }
            MessagingLine(
                sender = person?.name?.toString(),
                text = message.text?.toString().orEmpty(),
                timestampMillis = message.timestamp,
                hasPicture = message.dataUri != null && message.dataMimeType?.startsWith("image/") == true,
                index = index,
                fromUser = fromUser,
            )
        }

        val hasBigPicture = extras.containsKey(Notification.EXTRA_PICTURE) || extras.containsKey("android.pictureIcon")

        return RawNotificationPayload(
            notificationKey = sbn.key,
            packageName = sbn.packageName,
            postTimeEpochMillis = sbn.postTime,
            title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString(),
            text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString(),
            bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString(),
            textLines = textLines,
            conversationTitle = (messagingStyle?.conversationTitle
                ?: extras.getCharSequence(Notification.EXTRA_CONVERSATION_TITLE))?.toString(),
            isGroupConversation = messagingStyle?.isGroupConversation
                ?: extras.getBoolean(Notification.EXTRA_IS_GROUP_CONVERSATION, false),
            messagingStyleMessages = lines,
            category = notification.category,
            isGroupSummary = (notification.flags and Notification.FLAG_GROUP_SUMMARY) != 0,
            isOngoing = (notification.flags and Notification.FLAG_ONGOING_EVENT) != 0,
            hasContentIntent = notification.contentIntent != null,
            hasBigPicture = hasBigPicture,
            media = AndroidNotificationMedia(context.applicationContext, sbn),
        )
    }
}
