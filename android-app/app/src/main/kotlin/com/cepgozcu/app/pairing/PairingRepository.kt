package com.cepgozcu.app.pairing

import com.cepgozcu.app.net.await
import com.cepgozcu.app.net.protocol.PairBeginResponse
import com.cepgozcu.app.net.protocol.PairVerifyRequest
import com.cepgozcu.app.net.protocol.PairVerifyResponse
import com.cepgozcu.app.net.protocol.PairingState
import com.cepgozcu.app.net.protocol.WireJson
import com.cepgozcu.app.security.PinnedTrustManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

sealed class PairingOutcome {
    data class Approved(val host: String, val port: Int, val certSha256: String, val deviceId: String, val sessionToken: String, val refreshToken: String) : PairingOutcome()
    data object Pending : PairingOutcome()
    data object Rejected : PairingOutcome()
    data object Expired : PairingOutcome()
    data object WrongPin : PairingOutcome()
    data class Error(val message: String) : PairingOutcome()
}

/**
 * Drives the pairing REST calls (see the Windows agent's PairingEndpoints/AdminEndpoints):
 * find which of the QR's candidate LAN addresses is actually reachable, submit the PIN,
 * then poll until a human approves or rejects on the PC. Every request here goes over a client
 * built with [PinnedTrustManager] for the exact certificate fingerprint the QR carried — nothing
 * is ever trusted via the system CA store.
 */
class PairingRepository {
    private val jsonMediaType = "application/json".toMediaType()

    fun parseQrPayload(raw: String): PairBeginResponse? = runCatching {
        WireJson.decodeFromString<PairBeginResponse>(raw)
    }.getOrNull()

    private fun clientFor(certSha256: String): OkHttpClient {
        val pinned = PinnedTrustManager(certSha256)
        return OkHttpClient.Builder()
            .sslSocketFactory(pinned.buildSocketFactory(), pinned)
            .hostnameVerifier(pinned.hostnameVerifier)
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    /** Tries each candidate LAN address in turn (they come from the PC's own NIC enumeration; only one is usually reachable from the phone's current Wi-Fi). Returns the first that answers /health. */
    suspend fun findReachableHost(candidates: List<String>, port: Int, certSha256: String): String? = withContext(Dispatchers.IO) {
        val client = clientFor(certSha256)
        for (host in candidates) {
            val request = Request.Builder().url("https://$host:$port/health").get().build()
            val ok = runCatching { client.newCall(request).await().use { it.isSuccessful } }.getOrDefault(false)
            if (ok) return@withContext host
        }
        null
    }

    suspend fun verifyPin(
        host: String,
        port: Int,
        certSha256: String,
        pairingId: String,
        pin: String,
        deviceName: String,
        devicePublicKeyBase64: String,
    ): PairingOutcome = withContext(Dispatchers.IO) {
        runCatching {
            val client = clientFor(certSha256)
            val body = WireJson.encodeToString(PairVerifyRequest(pairingId, pin, deviceName, devicePublicKeyBase64))
            val request = Request.Builder()
                .url("https://$host:$port/pair/verify")
                .post(body.toRequestBody(jsonMediaType))
                .build()
            val response = client.newCall(request).await()
            val text = response.use { it.body?.string().orEmpty() }
            val parsed = WireJson.decodeFromString<PairVerifyResponse>(text)
            toOutcome(parsed, host, port, certSha256)
        }.getOrElse { PairingOutcome.Error(it.message ?: "Bağlantı hatası") }
    }

    suspend fun pollStatus(host: String, port: Int, certSha256: String, pairingId: String): PairingOutcome = withContext(Dispatchers.IO) {
        runCatching {
            val client = clientFor(certSha256)
            val request = Request.Builder().url("https://$host:$port/pair/status/$pairingId").get().build()
            val response = client.newCall(request).await()
            val text = response.use { it.body?.string().orEmpty() }
            val parsed = WireJson.decodeFromString<PairVerifyResponse>(text)
            toOutcome(parsed, host, port, certSha256)
        }.getOrElse { PairingOutcome.Error(it.message ?: "Bağlantı hatası") }
    }

    private fun toOutcome(parsed: PairVerifyResponse, host: String, port: Int, certSha256: String): PairingOutcome = when (parsed.state) {
        PairingState.Approved -> PairingOutcome.Approved(
            host, port, certSha256,
            deviceId = parsed.deviceId.orEmpty(),
            sessionToken = parsed.sessionToken.orEmpty(),
            refreshToken = parsed.refreshToken.orEmpty(),
        )
        PairingState.Rejected -> PairingOutcome.Rejected
        PairingState.Expired -> PairingOutcome.Expired
        PairingState.AwaitingPin -> PairingOutcome.WrongPin
        PairingState.PendingApproval -> PairingOutcome.Pending
    }
}
