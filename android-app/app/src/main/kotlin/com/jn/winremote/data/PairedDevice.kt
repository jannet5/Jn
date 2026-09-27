package com.jn.winremote.data

import kotlinx.serialization.Serializable

/**
 * One paired Windows PC, exactly what's needed to reconnect and
 * re-authenticate without any user interaction:
 *  - [host]/[port]: where to open the TLS WebSocket.
 *  - [pinnedFingerprintHex]: the SHA-256 SPKI pin captured at pairing time
 *    (PROTOCOL.md §1). Never re-derived from a "trusted" source at runtime.
 *  - [deviceId]/[deviceSecretB64]: identity + secret assigned by the agent
 *    at pairing time (PROTOCOL.md §4.2). The secret is used only to compute
 *    the HMAC challenge response (§4.3); it is never sent over the wire
 *    again and must never be logged.
 *
 * This class is serialized into EncryptedSharedPreferences, never into
 * plaintext storage, and never printed via Log/println.
 */
@Serializable
data class PairedDevice(
    val deviceId: String,
    val deviceName: String,
    val host: String,
    val port: Int,
    val pinnedFingerprintHex: String,
    val deviceSecretB64: String,
    val pairedAtEpochMs: Long,
) {
    override fun toString(): String =
        "PairedDevice(deviceId=$deviceId, deviceName=$deviceName, host=$host, port=$port, " +
            "pinnedFingerprintHex=$pinnedFingerprintHex, deviceSecretB64=<redacted>, " +
            "pairedAtEpochMs=$pairedAtEpochMs)"
}
