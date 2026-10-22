package com.sende.app.capture

import java.security.MessageDigest
import java.text.Normalizer
import java.util.Locale

/**
 * Duplicate-detection key: hashed so nothing readable ends up in logs or diagnostics, and
 * stable across everything that can legitimately re-deliver the same message (a listener
 * reconnect re-enumerating the shade, an app re-posting an unchanged notification).
 *
 * - [forTimedMessage]: for `MessagingStyle` lines, which carry their own exact timestamp.
 *   The exact timestamp (not a bucket) is used, so two real messages with the same text sent
 *   seconds apart stay distinct while a re-delivery of one message always collapses.
 * - [forUntimedMessage]: for plain notifications, which only have the post time. The exact post
 *   time identifies one delivery (a listener replay carries the original time); which lines of an
 *   updated notification are new is decided before this, by [UntimedLineTracker].
 */
object Fingerprint {

    fun forTimedMessage(packageName: String, conversationKey: String, senderName: String, messageText: String, timestampMillis: Long): String =
        hash(packageName, conversationKey, senderName, messageText, "t$timestampMillis")

    fun forUntimedMessage(packageName: String, conversationKey: String, senderName: String, messageText: String, postTimeEpochMillis: Long): String =
        hash(packageName, conversationKey, senderName, messageText, "p$postTimeEpochMillis")

    /** Groups messages of one chat: package plus a case/diacritic-insensitive conversation title. */
    fun conversationKey(packageName: String, conversationTitle: String): String =
        packageName.trim().lowercase(Locale.ROOT) + "|" + normalizeKeyPart(conversationTitle)

    private fun normalizeKeyPart(value: String): String =
        Normalizer.normalize(value.trim().lowercase(Locale.ROOT), Normalizer.Form.NFKC)
            .replace(Regex("\\s+"), " ")

    private fun hash(vararg parts: String): String {
        val normalized = parts.joinToString("\u0000") { normalizeKeyPart(it) }
        val digest = MessageDigest.getInstance("SHA-256").digest(normalized.toByteArray(Charsets.UTF_8))
        return digest.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
