package com.sende.app.capture

/**
 * Android-independent view of everything Sende is allowed to read off a
 * `StatusBarNotification`. [NotificationExtractor] builds this from the live framework
 * object; everything downstream ([MessageNormalizer], [MessageFilterEngine], [Fingerprint])
 * is plain Kotlin and runs in a JVM unit test without Robolectric.
 *
 * [media] is a lazy handle: pictures are only decoded for messages that survived filtering
 * and de-duplication, so a busy chat app never makes Sende decode images for nothing.
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
    val isGroupConversation: Boolean,
    val messagingStyleMessages: List<MessagingLine>,
    val category: String?,
    val isGroupSummary: Boolean,
    val isOngoing: Boolean,
    val hasContentIntent: Boolean,
    val hasBigPicture: Boolean = false,
    val media: NotificationMedia? = null,
)

/**
 * One line of a `MessagingStyle` conversation, oldest to newest. [index] addresses its picture.
 * [fromUser] marks the phone owner's own words (their inline reply shown in the same notification):
 * those are never a customer message.
 */
data class MessagingLine(
    val sender: String?,
    val text: String,
    val timestampMillis: Long = 0L,
    val hasPicture: Boolean = false,
    val index: Int = 0,
    val fromUser: Boolean = false,
)

/**
 * Lazy access to the images a notification carries. Implemented against the framework by
 * [AndroidNotificationMedia]; unit tests pass a fake or null. All results are already
 * downscaled and compressed, ready to be stored by [com.sende.app.data.media.MediaStore].
 */
interface NotificationMedia {
    /** Profile photo of the sender (MessagingStyle Person icon, else the large icon). */
    fun avatar(senderName: String?): ByteArray?

    /** Picture attached to the MessagingStyle line at [lineIndex], or the BigPicture. */
    fun picture(lineIndex: Int?): ByteArray?
}
