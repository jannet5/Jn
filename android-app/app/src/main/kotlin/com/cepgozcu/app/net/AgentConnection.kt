package com.cepgozcu.app.net

import com.cepgozcu.app.net.protocol.Envelope
import com.cepgozcu.app.net.protocol.MessageType
import com.cepgozcu.app.net.protocol.WireJson
import com.cepgozcu.app.security.AgentSession
import com.cepgozcu.app.security.PinnedTrustManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.math.min

sealed class ConnectionState {
    data object Idle : ConnectionState()
    data object Connecting : ConnectionState()
    data object Connected : ConnectionState()
    data class Disconnected(val willRetry: Boolean) : ConnectionState()
    data object Unauthorized : ConnectionState()
}

/**
 * Narrow contract for [AgentConnection] so ViewModels/repositories can depend on an interface
 * instead of the concrete OkHttp-backed class, letting unit tests substitute a fake connection
 * (fake state/pushes flows) without touching a real socket.
 */
interface AgentConnectionContract {
    val state: StateFlow<ConnectionState>
    val pushes: SharedFlow<Envelope>
    fun connect()
    fun disconnect()
    suspend fun call(type: String, payload: JsonElement? = null, timeoutMs: Long = 8_000): Envelope
}

/**
 * Owns the single WebSocket connection to a paired PC: authenticates with the stored bearer
 * token, reconnects with capped exponential backoff on any drop (Wi-Fi hiccup, PC sleeping,
 * agent restart), and exposes both a request/response call() for on-demand queries and a
 * SharedFlow of server-pushed events (metrics.update, disk.fileEvent, alert.push, device.revoked).
 */
class AgentConnection(private val session: AgentSession) : AgentConnectionContract {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val pending = ConcurrentHashMap<String, CompletableDeferred<Envelope>>()
    private var webSocket: WebSocket? = null
    private var intentionallyClosed = false
    private var retryAttempt = 0

    private val _state = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val state: StateFlow<ConnectionState> = _state.asStateFlow()

    private val _pushes = MutableSharedFlow<Envelope>(extraBufferCapacity = 64)
    val pushes: SharedFlow<Envelope> = _pushes.asSharedFlow()

    private val client: OkHttpClient by lazy {
        val pinned = PinnedTrustManager(session.certSha256)
        OkHttpClient.Builder()
            .sslSocketFactory(pinned.buildSocketFactory(), pinned)
            .hostnameVerifier(pinned.hostnameVerifier)
            .pingInterval(15, TimeUnit.SECONDS)
            .build()
    }

    override fun connect() {
        intentionallyClosed = false
        openSocket()
    }

    override fun disconnect() {
        intentionallyClosed = true
        webSocket?.close(1000, "client_disconnect")
        webSocket = null
        _state.value = ConnectionState.Idle
    }

    private fun openSocket() {
        _state.value = ConnectionState.Connecting
        val request = Request.Builder()
            .url("wss://${session.host}:${session.port}/ws")
            .addHeader("Authorization", "Bearer ${session.sessionToken}")
            .build()
        webSocket = client.newWebSocket(request, listener)
    }

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            retryAttempt = 0
            _state.value = ConnectionState.Connected
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val envelope = runCatching { WireJson.decodeFromString<Envelope>(text) }.getOrNull() ?: return
            val id = envelope.id
            if (id != null && pending.containsKey(id)) {
                pending.remove(id)?.complete(envelope)
            } else if (envelope.type == MessageType.DEVICE_REVOKED) {
                _state.value = ConnectionState.Unauthorized
                disconnect()
            } else {
                _pushes.tryEmit(envelope)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            failAllPending()
            scheduleReconnectIfNeeded(code)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            failAllPending()
            if (response?.code == 401) {
                _state.value = ConnectionState.Unauthorized
                intentionallyClosed = true
                return
            }
            scheduleReconnectIfNeeded(null)
        }
    }

    private fun failAllPending() {
        pending.values.forEach { it.cancel() }
        pending.clear()
    }

    private fun scheduleReconnectIfNeeded(closeCode: Int?) {
        if (intentionallyClosed) {
            _state.value = ConnectionState.Idle
            return
        }
        _state.value = ConnectionState.Disconnected(willRetry = true)
        val delayMs = min(30_000L, 1000L * (1L shl min(retryAttempt, 5)))
        retryAttempt++
        scope.launch {
            delay(delayMs)
            if (!intentionallyClosed) openSocket()
        }
    }

    /** Sends a request and suspends for the matching response (by envelope id), or throws on timeout/disconnect. */
    override suspend fun call(type: String, payload: JsonElement?, timeoutMs: Long): Envelope {
        val id = UUID.randomUUID().toString()
        val deferred = CompletableDeferred<Envelope>()
        pending[id] = deferred
        val envelope = Envelope(id = id, type = type, ts = System.currentTimeMillis(), payload = payload)
        val sent = webSocket?.send(WireJson.encodeToString(envelope)) ?: false
        if (!sent) {
            pending.remove(id)
            throw IllegalStateException("not_connected")
        }
        return withTimeout(timeoutMs) { deferred.await() }
    }
}
