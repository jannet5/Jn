package com.tekpanel.app.capture

/** Sender + message text chosen from a [RawNotificationPayload], ready for display. */
data class NormalizedContent(val senderName: String, val messageText: String)

/**
 * Picks the most complete, least-duplicated text out of a notification (spec CAP-05).
 *
 * Real chat apps expose the same message through several overlapping fields at once
 * (`text`, `bigText`, `textLines`, `MessagingStyle`). The priority order below was chosen
 * because `MessagingStyle` (when present) is the most structured and least truncated source,
 * followed by `bigText` (the expanded form Android shows on long-press), then the most recent
 * `textLines` entry (older `InboxStyle` notifications), then plain `text` as a last resort.
 */
object MessageNormalizer {

    fun normalize(payload: RawNotificationPayload): NormalizedContent? {
        fromMessagingStyle(payload)?.let { return it }
        fromBigText(payload)?.let { return it }
        fromTextLines(payload)?.let { return it }
        fromPlainText(payload)?.let { return it }
        return null
    }

    private fun fromMessagingStyle(payload: RawNotificationPayload): NormalizedContent? {
        val last = payload.messagingStyleMessages.lastOrNull { it.text.isNotBlank() } ?: return null
        val sender = last.sender?.takeIf { it.isNotBlank() }
            ?: payload.conversationTitle?.takeIf { it.isNotBlank() }
            ?: payload.title?.takeIf { it.isNotBlank() }
            ?: return null
        return NormalizedContent(senderName = sender.trim(), messageText = last.text.trim())
    }

    private fun fromBigText(payload: RawNotificationPayload): NormalizedContent? {
        val bigText = payload.bigText?.takeIf { it.isNotBlank() } ?: return null
        val sender = payload.conversationTitle?.takeIf { it.isNotBlank() }
            ?: payload.title?.takeIf { it.isNotBlank() }
            ?: return null
        return NormalizedContent(senderName = sender.trim(), messageText = stripSenderPrefix(bigText, sender).trim())
    }

    private fun fromTextLines(payload: RawNotificationPayload): NormalizedContent? {
        val lastLine = payload.textLines.lastOrNull { it.isNotBlank() } ?: return null
        val separatorIndex = lastLine.indexOf(": ")
        val (lineSender, lineText) = if (separatorIndex in 1 until lastLine.length - 2) {
            lastLine.substring(0, separatorIndex) to lastLine.substring(separatorIndex + 2)
        } else {
            null to lastLine
        }
        val sender = lineSender?.takeIf { it.isNotBlank() }
            ?: payload.conversationTitle?.takeIf { it.isNotBlank() }
            ?: payload.title?.takeIf { it.isNotBlank() }
            ?: return null
        return NormalizedContent(senderName = sender.trim(), messageText = lineText.trim())
    }

    private fun fromPlainText(payload: RawNotificationPayload): NormalizedContent? {
        val text = payload.text?.takeIf { it.isNotBlank() } ?: return null
        val sender = payload.conversationTitle?.takeIf { it.isNotBlank() }
            ?: payload.title?.takeIf { it.isNotBlank() }
            ?: return null
        return NormalizedContent(senderName = sender.trim(), messageText = stripSenderPrefix(text, sender).trim())
    }

    /** Some apps prefix the expanded text with "Sender: " again; drop the redundant prefix. */
    private fun stripSenderPrefix(text: String, sender: String): String {
        val prefix = "$sender: "
        return if (text.startsWith(prefix)) text.removePrefix(prefix) else text
    }
}
