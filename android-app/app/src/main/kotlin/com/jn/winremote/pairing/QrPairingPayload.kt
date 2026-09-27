package com.jn.winremote.pairing

import com.jn.winremote.crypto.normalizeFingerprint
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json

/**
 * JSON payload the Windows agent's pairing QR code encodes.
 *
 * NOTE ON SPEC GAP: PROTOCOL.md §2/§3 requires that the pairing code AND
 * the cert fingerprint both be obtained out-of-band (QR or manual entry),
 * and §1 fixes the fingerprint's own format, but the document does not
 * define the QR's exact JSON schema/field names. This shape (host, port,
 * fingerprint, pairing_code, device label) is this client's best-effort
 * interpretation, kept intentionally close to the field names already used
 * in `pair_request`/§1 so both implementations are likely to converge. The
 * manual-entry form is a fully equivalent, independently functional path
 * that does not depend on guessing this schema right.
 */
@Serializable
data class QrPairingPayload(
    @SerialName("host") val host: String,
    @SerialName("port") val port: Int = 8787,
    // Accept "fingerprint" as the canonical key; a couple of synonyms are
    // tolerated below in [parse] in case the agent names it differently.
    @SerialName("fingerprint") val fingerprint: String,
    @SerialName("pairing_code") val pairingCode: String,
    @SerialName("agent_name") val agentName: String? = null,
)

private val lenientJson = Json { ignoreUnknownKeys = true; isLenient = true }

/**
 * Parses a scanned QR string into a payload, tolerating a couple of
 * plausible key-name variants for the fingerprint field. Returns null for
 * anything that isn't a recognizable pairing payload (caller falls back to
 * telling the user to use manual entry).
 */
fun parseQrPairingPayload(raw: String): QrPairingPayload? = try {
    parseQrPairingPayloadOrThrow(raw)
} catch (_: Exception) {
    null
}

private fun parseQrPairingPayloadOrThrow(raw: String): QrPairingPayload? {
    val element = lenientJson.parseToJsonElement(raw)
    val obj = element as? kotlinx.serialization.json.JsonObject ?: return null
    val host = obj["host"]?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content } ?: return null
    val port = obj["port"]?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content?.toIntOrNull() } ?: 8787
    val fingerprintRaw = listOf("fingerprint", "cert_fingerprint", "fp")
        .firstNotNullOfOrNull { key -> obj[key]?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content } }
        ?: return null
    val pairingCode = listOf("pairing_code", "code")
        .firstNotNullOfOrNull { key -> obj[key]?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content } }
        ?: return null
    val agentName = obj["agent_name"]?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content }
    return QrPairingPayload(
        host = host,
        port = port,
        fingerprint = normalizeFingerprint(fingerprintRaw),
        pairingCode = pairingCode,
        agentName = agentName,
    )
}
