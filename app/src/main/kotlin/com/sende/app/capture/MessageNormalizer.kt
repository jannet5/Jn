package com.sende.app.capture

/** One message pulled out of a notification, ready to be fingerprinted and stored. */
data class NormalizedMessage(
    val senderName: String,
    val messageText: String,
    val timestampMillis: Long?,
    val lineIndex: Int?,
    val hasPicture: Boolean,
    /**
     * Set only for plain-text notifications: the text split into its non-blank lines. Some apps keep one
     * notification per chat and rewrite its text as "first\nsecond\nthird" for every new message, so the
     * lines tell [CaptureCoordinator] which of them are new.
     */
    val blockLines: List<String>? = null,
)

/** Everything a notification says: which chat it belongs to and the messages inside it. */
data class NormalizedNotification(
    val conversationTitle: String,
    val isGroupConversation: Boolean,
    val messages: List<NormalizedMessage>,
)

/**
 * Picks the most complete, least-duplicated text out of a notification.
 *
 * Chat apps expose the same message through several overlapping fields at once (`text`,
 * `bigText`, `textLines`, `MessagingStyle`). `MessagingStyle` is the most structured and the
 * only one that carries *every* recent message with its own timestamp, so every line is
 * returned (an app keeps re-using one notification per chat, so reading only the newest line
 * would silently lose messages that arrived in a burst). Without it, the longest of
 * `bigText` / `text` is used so a long message is never cut at the collapsed length.
 */
object MessageNormalizer {

    fun normalize(payload: RawNotificationPayload): NormalizedNotification? {
        // A conversation holding only the phone owner's own words says nothing a customer wrote.
        if (payload.messagingStyleMessages.isNotEmpty() && payload.messagingStyleMessages.all { it.fromUser }) return null
        fromMessagingStyle(payload)?.let { return it }
        fromTextLines(payload)?.let { return it }
        return fromPlainText(payload)
    }

    private fun fromMessagingStyle(payload: RawNotificationPayload): NormalizedNotification? {
        val lines = payload.messagingStyleMessages.filter { !it.fromUser && (it.text.isNotBlank() || it.hasPicture) }
        if (lines.isEmpty()) return null
        val title = conversationTitleOf(payload) ?: lines.last().sender?.takeIf { it.isNotBlank() } ?: return null
        val fallbackSender = payload.title?.takeIf { it.isNotBlank() } ?: title
        val messages = lines.map { line ->
            NormalizedMessage(
                senderName = (line.sender?.takeIf { it.isNotBlank() } ?: fallbackSender).trim(),
                messageText = line.text.trim(),
                timestampMillis = line.timestampMillis.takeIf { it > 0L },
                lineIndex = line.index,
                hasPicture = line.hasPicture,
            )
        }
        return NormalizedNotification(title.trim(), payload.isGroupConversation, messages)
    }

    private fun fromTextLines(payload: RawNotificationPayload): NormalizedNotification? {
        val title = conversationTitleOf(payload) ?: return null
        val lines = payload.textLines.filter { it.isNotBlank() }
        if (lines.isEmpty()) return null
        val messages = lines.map { line ->
            val separator = line.indexOf(": ")
            val hasSender = separator in 1 until line.length - 2
            NormalizedMessage(
                senderName = (if (hasSender) line.substring(0, separator) else title).trim(),
                messageText = (if (hasSender) line.substring(separator + 2) else line).trim(),
                timestampMillis = null,
                lineIndex = null,
                hasPicture = false,
            )
        }
        return NormalizedNotification(title.trim(), payload.isGroupConversation, messages)
    }

    private fun fromPlainText(payload: RawNotificationPayload): NormalizedNotification? {
        val title = conversationTitleOf(payload) ?: return null
        val raw = listOfNotNull(payload.bigText, payload.text)
            .filter { it.isNotBlank() }
            .maxByOrNull { it.length }
            ?: return null
        val body = stripSenderPrefix(raw.trim(), title)
        if (body.isBlank()) return null
        return NormalizedNotification(
            conversationTitle = title.trim(),
            isGroupConversation = payload.isGroupConversation,
            messages = listOf(
                NormalizedMessage(
                    senderName = title.trim(),
                    messageText = body,
                    timestampMillis = null,
                    lineIndex = null,
                    hasPicture = payload.hasBigPicture,
                    blockLines = body.lines().map { it.trim() }.filter { it.isNotEmpty() },
                ),
            ),
        )
    }

    private fun conversationTitleOf(payload: RawNotificationPayload): String? =
        payload.conversationTitle?.takeIf { it.isNotBlank() } ?: payload.title?.takeIf { it.isNotBlank() }

    /** Some apps prefix the expanded text with "Sender: " again; drop the redundant prefix. */
    private fun stripSenderPrefix(text: String, sender: String): String {
        val prefix = "${sender.trim()}: "
        return if (text.startsWith(prefix)) text.removePrefix(prefix) else text
    }
}
