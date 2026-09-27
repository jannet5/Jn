package com.jn.winremote.crypto

import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Pure challenge-response math for PROTOCOL.md §4.3:
 *
 *   hmac = base64(HMAC_SHA256(device_secret, nonce_bytes || ":" || device_id))
 *
 * Kept dependency-free (no Android framework classes) so it is fully
 * covered by JVM unit tests.
 */
object HmacAuth {

    private const val ALGO = "HmacSHA256"
    private val COLON = ":".toByteArray(Charsets.US_ASCII)

    /**
     * @param deviceSecret raw (already base64-decoded) 256-bit device secret
     * @param nonceB64 the base64 nonce as sent by the server in `auth_challenge`
     * @param deviceId the paired device's device_id string
     * @return base64-encoded HMAC-SHA256, to place in `auth_response.hmac_b64`
     */
    fun computeAuthResponse(deviceSecret: ByteArray, nonceB64: String, deviceId: String): String {
        val nonceBytes = Base64.getDecoder().decode(nonceB64)
        val message = nonceBytes + COLON + deviceId.toByteArray(Charsets.UTF_8)
        return Base64.getEncoder().encodeToString(hmacSha256(deviceSecret, message))
    }

    fun hmacSha256(key: ByteArray, message: ByteArray): ByteArray {
        val mac = Mac.getInstance(ALGO)
        mac.init(SecretKeySpec(key, ALGO))
        return mac.doFinal(message)
    }
}
