package com.cepgozcu.app.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * We never trust the system CA store for the agent's TLS certificate — it's self-signed on
 * purpose (see the Windows agent's CertificateProvider). Instead, this trusts exactly one
 * certificate: the one whose SHA-256 fingerprint was shown to the user out of band at pairing
 * time (the QR code / PIN screen on the PC). Anything else — including a perfectly valid CA-signed
 * cert for a different host — is rejected. This is classic TOFU (trust-on-first-use) pinning,
 * the same model SSH host keys use.
 */
class PinnedTrustManager(private val expectedSha256Hex: String) : X509TrustManager {

    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        throw CertificateException("Client certificates are not used by this app")
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        val leaf = chain?.firstOrNull() ?: throw CertificateException("Sunucu sertifikası sunulmadı")
        val actual = sha256Hex(leaf.encoded)
        if (!actual.equals(expectedSha256Hex, ignoreCase = true)) {
            throw CertificateException("Sertifika parmak izi eşleşmiyor — beklenmeyen bir sunucuya bağlanılıyor olabilir")
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()

    /** Accepts any hostname: we connect by LAN IP, and identity is already established by the pinned certificate hash above, not by CN/SAN matching. */
    val hostnameVerifier = HostnameVerifier { _, _ -> true }

    fun buildSocketFactory(): SSLSocketFactory {
        val context = SSLContext.getInstance("TLS")
        context.init(null, arrayOf(this), SecureRandom())
        return context.socketFactory
    }

    companion object {
        fun sha256Hex(bytes: ByteArray): String {
            val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
            return digest.joinToString("") { "%02x".format(it) }
        }
    }
}
