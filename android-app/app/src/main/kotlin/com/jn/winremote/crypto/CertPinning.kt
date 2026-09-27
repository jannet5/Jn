package com.jn.winremote.crypto

import java.security.MessageDigest
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * Thrown when the live server certificate's pinned fingerprint does not
 * match the one recorded at pairing time. Surfaced to the user as
 * "sunucu kimliği doğrulanamadı" — never silently swallowed or downgraded
 * to a generic connection error, per PROTOCOL.md §1.
 */
class CertificatePinningException(message: String) : CertificateException(message)

/**
 * Computes the pin value PROTOCOL.md §1 defines: the SHA-256 of the
 * DER-encoded (X.509 SubjectPublicKeyInfo) public key, hex-encoded
 * lowercase, no separators.
 *
 * This is a public-key pin (SPKI pin), not a whole-certificate hash — the
 * protocol text is explicit about this ("fingerprint of the DER-encoded
 * public key"), even though the surrounding prose also loosely says
 * "certificate's fingerprint". We follow the precise wire definition.
 */
fun publicKeyFingerprintHex(cert: X509Certificate): String {
    val spkiDer = cert.publicKey.encoded
    val digest = MessageDigest.getInstance("SHA-256").digest(spkiDer)
    return digest.joinToString(separator = "") { byte -> "%02x".format(byte) }
}

/** Normalizes a user/QR-supplied fingerprint string for comparison/storage. */
fun normalizeFingerprint(raw: String): String =
    raw.trim().lowercase().replace(":", "").replace(" ", "")

/**
 * A [X509TrustManager] that ignores the system/CA trust store entirely and
 * trusts the connection if and only if the leaf certificate's public-key
 * fingerprint equals [pinnedFingerprintHex]. This implements the
 * "pinned-fingerprint model, not a CA-trust model" described in
 * PROTOCOL.md §1: the agent's cert is self-signed, so ordinary chain
 * validation would either fail outright or (worse) be meaningless.
 */
class PinnedFingerprintTrustManager(private val pinnedFingerprintHex: String) : X509TrustManager {

    private val expected = normalizeFingerprint(pinnedFingerprintHex)

    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        throw CertificateException("Client certificate authentication is not part of this protocol")
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        val leaf = chain?.firstOrNull()
            ?: throw CertificateException("Sunucu sertifikası sunulmadı")
        val actual = publicKeyFingerprintHex(leaf)
        if (!actual.equals(expected, ignoreCase = true)) {
            throw CertificatePinningException(
                "sunucu kimliği doğrulanamadı: beklenen=$expected görülen=$actual"
            )
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
}

/**
 * Hostname verification is intentionally bypassed: trust is established by
 * the pinned public-key fingerprint above, not by CN/SAN-vs-host matching.
 * The user typically pairs by raw IP address to a self-signed cert that
 * carries no meaningful hostname, so classic hostname verification would
 * reject every legitimate connection while adding no security beyond what
 * the fingerprint pin already guarantees.
 */
object TrustAllHostnames : HostnameVerifier {
    override fun verify(hostname: String?, session: javax.net.ssl.SSLSession?): Boolean = true
}

data class PinnedTls(
    val socketFactory: SSLSocketFactory,
    val trustManager: X509TrustManager,
    val hostnameVerifier: HostnameVerifier,
)

/** Builds the TLS material OkHttp needs to enforce a single pinned fingerprint. */
fun buildPinnedTls(pinnedFingerprintHex: String): PinnedTls {
    val trustManager = PinnedFingerprintTrustManager(pinnedFingerprintHex)
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, arrayOf(trustManager), SecureRandom())
    return PinnedTls(
        socketFactory = sslContext.socketFactory,
        trustManager = trustManager,
        hostnameVerifier = TrustAllHostnames,
    )
}
