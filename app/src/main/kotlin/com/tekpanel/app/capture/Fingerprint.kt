package com.tekpanel.app.capture

import java.security.MessageDigest

/**
 * Secondary duplicate-detection key (spec CAP-07): normalized package + sender + message +
 * a coarse time bucket, hashed so nothing readable ends up in logs or diagnostics exports.
 *
 * The 60-second bucket absorbs the few-hundred-ms jitter Android introduces when a
 * notification is updated in place (same content re-posted with a slightly later postTime)
 * without merging genuinely distinct messages sent a minute or more apart.
 */
object Fingerprint {

    private const val TIME_BUCKET_MILLIS = 60_000L

    fun of(packageName: String, senderName: String, messageText: String, postTimeEpochMillis: Long): String {
        val bucket = postTimeEpochMillis / TIME_BUCKET_MILLIS
        val normalized = listOf(
            packageName.trim().lowercase(),
            senderName.trim().lowercase(),
            messageText.trim().lowercase(),
            bucket.toString(),
        ).joinToString("\u0000")
        return sha256Hex(normalized)
    }

    private fun sha256Hex(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
        return digest.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
