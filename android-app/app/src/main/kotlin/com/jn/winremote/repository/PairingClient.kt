package com.jn.winremote.repository

import com.jn.winremote.crypto.CertificatePinningException
import com.jn.winremote.crypto.buildPinnedTls
import com.jn.winremote.data.PairedDevice
import com.jn.winremote.protocol.ClientMessage
import com.jn.winremote.protocol.ServerMessage
import com.jn.winremote.protocol.decodeServerMessage
import com.jn.winremote.protocol.encodeClientMessage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.UUID
import java.util.concurrent.TimeUnit

sealed class PairingOutcome {
    data class Success(val device: PairedDevice) : PairingOutcome()
    data class Rejected(val reasonCode: String) : PairingOutcome()
    data class CertMismatch(val detail: String) : PairingOutcome()
    data class ProtocolError(val reasonCode: String, val message: String?) : PairingOutcome()
    data class ConnectionError(val detail: String) : PairingOutcome()
    object Timeout : PairingOutcome()
}

/**
 * A single-use, ephemeral WebSocket connection that performs exactly the
 * unauthenticated pairing handshake from PROTOCOL.md §3/§4.2
 * (hello -> hello_ack -> pair_request -> pair_success|pair_failed), then
 * closes. Deliberately independent of [WinRemoteRepository]'s persistent,
 * auto-reconnecting session state machine — pairing is a one-shot,
 * human-present operation, not something to retry with backoff.
 */
class PairingClient {

    suspend fun pair(
        host: String,
        port: Int,
        pinnedFingerprintHex: String,
        pairingCode: String,
        deviceName: String,
        timeoutMillis: Long = 15_000,
    ): PairingOutcome {
        val resultDeferred = CompletableDeferred<PairingOutcome>()
        var client: OkHttpClient? = null
        var socket: WebSocket? = null
        val requestId = UUID.randomUUID().toString()

        try {
            val tls = buildPinnedTls(pinnedFingerprintHex)
            client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .sslSocketFactory(tls.socketFactory, tls.trustManager)
                .hostnameVerifier(tls.hostnameVerifier)
                .build()

            val listener = object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                    webSocket.send(encodeClientMessage(ClientMessage.Hello()))
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    val message = decodeServerMessage(text) ?: return
                    when (message) {
                        is ServerMessage.HelloAck -> {
                            webSocket.send(
                                encodeClientMessage(
                                    ClientMessage.PairRequest(
                                        requestId = requestId,
                                        pairingCode = pairingCode,
                                        deviceName = deviceName,
                                    )
                                )
                            )
                        }
                        is ServerMessage.PairSuccess -> {
                            if (message.requestId == requestId) {
                                resultDeferred.complete(
                                    PairingOutcome.Success(
                                        PairedDevice(
                                            deviceId = message.deviceId,
                                            deviceName = deviceName,
                                            host = host,
                                            port = port,
                                            pinnedFingerprintHex = pinnedFingerprintHex,
                                            deviceSecretB64 = message.deviceSecretB64,
                                            pairedAtEpochMs = System.currentTimeMillis(),
                                        )
                                    )
                                )
                            }
                        }
                        is ServerMessage.PairFailed -> {
                            if (message.requestId == requestId) {
                                resultDeferred.complete(PairingOutcome.Rejected(message.reason))
                            }
                        }
                        is ServerMessage.Error -> {
                            resultDeferred.complete(PairingOutcome.ProtocolError(message.reason, message.message))
                        }
                        else -> Unit
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                    val rootCause = rootCauseOf(t)
                    if (rootCause is CertificatePinningException) {
                        resultDeferred.complete(PairingOutcome.CertMismatch(rootCause.message ?: "fingerprint mismatch"))
                    } else {
                        resultDeferred.complete(PairingOutcome.ConnectionError(t.message ?: t.javaClass.simpleName))
                    }
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    if (!resultDeferred.isCompleted) {
                        resultDeferred.complete(PairingOutcome.ConnectionError("connection closed ($code) before pairing finished"))
                    }
                }
            }

            val request = Request.Builder().url("wss://$host:$port/ws").build()
            socket = client.newWebSocket(request, listener)

            return try {
                withTimeout(timeoutMillis) { resultDeferred.await() }
            } catch (_: TimeoutCancellationException) {
                PairingOutcome.Timeout
            }
        } catch (e: Exception) {
            return PairingOutcome.ConnectionError(e.message ?: e.javaClass.simpleName)
        } finally {
            socket?.close(1000, "pairing done")
            client?.dispatcher?.executorService?.shutdown()
        }
    }

    private fun rootCauseOf(t: Throwable): Throwable {
        var cur = t
        while (cur.cause != null && cur.cause !== cur) cur = cur.cause!!
        return cur
    }
}
