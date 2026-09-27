package com.cepgozcu.app.security

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * NARROW, SINGLE-PURPOSE helper used ONLY during manual pairing entry, when the phone has no QR
 * code (and therefore no `certSha256` from [com.cepgozcu.app.net.protocol.PairBeginResponse]) to
 * pin against yet. Its one job is to open a TLS connection that accepts *any* certificate, read
 * the SHA-256 fingerprint of whatever certificate the server presents, and hand that back to the
 * caller so a human can visually confirm it matches the fingerprint shown on the PC's own admin
 * page — classic TOFU (trust-on-first-use), the same model SSH uses for "the authenticity of host
 * X can't be established, are you sure you want to continue connecting?".
 *
 * This is intentionally NOT a general-purpose permissive client. It must never be reused to send
 * any real request (PIN verification, pairing status, or anything on [com.cepgozcu.app.net.AgentConnection]).
 * Once the fingerprint is confirmed by the user, every subsequent call uses [PinnedTrustManager]
 * pinned to that exact confirmed value, same as the QR flow.
 *
 * Honest limitation: because the "device public key" in this app's v1 pairing model is just an
 * opaque per-install random string (see [CredentialStore.getOrCreateInstallIdentifier]) rather
 * than a real asymmetric key challenge, this fingerprint check is the ONLY thing standing between
 * the user and a MITM/wrong-host during manual entry — there is no cryptographic device identity
 * behind it. Treat the "does this match the PC screen" confirmation as security-critical, not a
 * formality.
 */
object UnpinnedFingerprintFetcher {

    /** Trusts any certificate, for the sole purpose of inspecting it. Never used to send data other than the bare TLS handshake + this one GET. */
    private class AcceptAnyTrustManager : X509TrustManager {
        var lastChain: Array<out X509Certificate>? = null
            private set

        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            lastChain = chain
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
    }

    sealed class Result {
        data class Fetched(val sha256Hex: String) : Result()
        data object Unreachable : Result()
    }

    /** Connects to `https://host:port/health`, accepting any certificate, purely to read its fingerprint. Discards the response body. */
    suspend fun fetchFingerprint(host: String, port: Int): Result = withContext(Dispatchers.IO) {
        val trustManager = AcceptAnyTrustManager()
        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), SecureRandom())
        }
        val socketFactory: SSLSocketFactory = sslContext.socketFactory
        val client = OkHttpClient.Builder()
            .sslSocketFactory(socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
        runCatching {
            val request = Request.Builder().url("https://$host:$port/health").get().build()
            client.newCall(request).execute().use { /* body intentionally ignored */ }
            val leaf = trustManager.lastChain?.firstOrNull() ?: return@withContext Result.Unreachable
            Result.Fetched(PinnedTrustManager.sha256Hex(leaf.encoded))
        }.getOrElse { Result.Unreachable }
    }

    /** Formats a lowercase hex SHA-256 string into readable "AB12 CD34 …" groups for on-screen comparison. */
    fun formatForDisplay(sha256Hex: String): String =
        sha256Hex.uppercase().chunked(4).joinToString(" ")
}
