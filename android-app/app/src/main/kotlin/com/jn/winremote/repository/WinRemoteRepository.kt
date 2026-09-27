package com.jn.winremote.repository

import android.util.Log
import com.jn.winremote.crypto.CertificatePinningException
import com.jn.winremote.crypto.HmacAuth
import com.jn.winremote.crypto.buildPinnedTls
import com.jn.winremote.data.PairedDevice
import com.jn.winremote.data.ActiveDeviceProvider
import com.jn.winremote.protocol.AlertData
import com.jn.winremote.protocol.ClientMessage
import com.jn.winremote.protocol.FileEventData
import com.jn.winremote.protocol.ServerMessage
import com.jn.winremote.protocol.decodeServerMessage
import com.jn.winremote.protocol.encodeClientMessage
import com.jn.winremote.protocol.requestIdOrNull
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.Base64
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

private const val TAG = "WinRemoteRepository"
private const val REQUEST_TIMEOUT_MILLIS = 10_000L
private const val LIVENESS_TIMEOUT_MILLIS = 30_000L // ~2 missed 2s metrics pushes + missed 20s ping, generously
private const val WATCHDOG_INTERVAL_MILLIS = 5_000L

/**
 * Owns the single persistent, authenticated WebSocket connection to the
 * *active* paired PC: connect -> hello -> auth challenge/response ->
 * subscribe -> live push topics + request/response calls, with automatic
 * exponential-backoff reconnect (§3, §4.3) driven entirely from here (no
 * user interaction needed after a transient drop).
 */
class WinRemoteRepository(
    private val secureStore: ActiveDeviceProvider,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile private var webSocket: WebSocket? = null
    @Volatile private var okHttpClient: OkHttpClient? = null
    @Volatile private var currentDevice: PairedDevice? = null
    @Volatile private var manualDisconnect = false
    @Volatile private var attempt = 0
    @Volatile private var currentAuthRequestId: String? = null
    @Volatile private var lastMessageAtMs: Long = 0L

    private var reconnectJob: Job? = null
    private var watchdogJob: Job? = null

    private val pendingRequests = ConcurrentHashMap<String, CompletableDeferred<ServerMessage>>()

    private val _connectionStatus = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Offline)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private val _metrics = MutableStateFlow<ServerMessage.Metrics?>(null)
    val metrics: StateFlow<ServerMessage.Metrics?> = _metrics.asStateFlow()

    private val _liveFileEvents = MutableSharedFlow<FileEventData>(extraBufferCapacity = 128)
    val liveFileEvents: SharedFlow<FileEventData> = _liveFileEvents.asSharedFlow()

    private val _liveAlerts = MutableSharedFlow<AlertData>(extraBufferCapacity = 32)
    val liveAlerts: SharedFlow<AlertData> = _liveAlerts.asSharedFlow()

    // -------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------

    fun connectToActiveDevice() {
        val device = secureStore.getActiveDevice() ?: run {
            _connectionStatus.value = ConnectionStatus.Offline
            return
        }
        connect(device)
    }

    fun connect(device: PairedDevice) {
        manualDisconnect = false
        attempt = 0
        currentDevice = device
        reconnectJob?.cancel()
        _connectionStatus.value = ConnectionStatus.Connecting
        openSocket(device)
    }

    fun disconnect() {
        manualDisconnect = true
        reconnectJob?.cancel()
        watchdogJob?.cancel()
        failAllPending("disconnected")
        webSocket?.close(1000, "user disconnect")
        webSocket = null
        _connectionStatus.value = ConnectionStatus.Offline
    }

    private fun openSocket(device: PairedDevice) {
        try {
            val tls = buildPinnedTls(device.pinnedFingerprintHex)
            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS) // long-lived stream; liveness handled by our own watchdog
                .sslSocketFactory(tls.socketFactory, tls.trustManager)
                .hostnameVerifier(tls.hostnameVerifier)
                .build()
            okHttpClient = client
            val request = Request.Builder().url("wss://${device.host}:${device.port}/ws").build()
            webSocket = client.newWebSocket(request, Listener(device))
        } catch (e: Exception) {
            Log.w(TAG, "openSocket failed: ${e.javaClass.simpleName}")
            _connectionStatus.value = ConnectionStatus.Error(e.message ?: "bağlantı hatası")
            scheduleReconnect()
        }
    }

    private inner class Listener(private val device: PairedDevice) : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            lastMessageAtMs = System.currentTimeMillis()
            webSocket.send(encodeClientMessage(ClientMessage.Hello()))
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            lastMessageAtMs = System.currentTimeMillis()
            handleIncoming(text, device)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.w(TAG, "onFailure: ${rootCauseOf(t).javaClass.simpleName}")
            val rootCause = rootCauseOf(t)
            failAllPending("connection failed")
            watchdogJob?.cancel()
            if (rootCause is CertificatePinningException) {
                // Never silently proceed on a fingerprint mismatch: hard-fail, no auto-retry,
                // since retrying against the same (possibly hostile) endpoint teaches nothing.
                _connectionStatus.value = ConnectionStatus.Error(
                    message = "sunucu kimliği doğrulanamadı",
                    isCertMismatch = true,
                )
                return
            }
            _connectionStatus.value = ConnectionStatus.Error(t.message ?: "bağlantı hatası")
            scheduleReconnect()
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            watchdogJob?.cancel()
            if (!manualDisconnect) {
                scheduleReconnect()
            }
        }
    }

    private fun scheduleReconnect() {
        if (manualDisconnect) return
        val device = currentDevice ?: return
        attempt += 1
        val delayMs = BackoffPolicy.jitteredDelayMillis(attempt)
        _connectionStatus.value = ConnectionStatus.Reconnecting(attempt, delayMs)
        reconnectJob?.cancel()
        reconnectJob = scope.launch {
            delay(delayMs)
            if (isActive && !manualDisconnect) openSocket(device)
        }
    }

    private fun startWatchdog() {
        watchdogJob?.cancel()
        watchdogJob = scope.launch {
            while (isActive) {
                delay(WATCHDOG_INTERVAL_MILLIS)
                val elapsed = System.currentTimeMillis() - lastMessageAtMs
                if (elapsed > LIVENESS_TIMEOUT_MILLIS && _connectionStatus.value is ConnectionStatus.Connected) {
                    Log.w(TAG, "liveness watchdog: no server traffic for ${elapsed}ms, forcing reconnect")
                    webSocket?.cancel() // triggers onFailure/onClosed -> scheduleReconnect
                    return@launch
                }
            }
        }
    }

    // -------------------------------------------------------------------
    // Handshake
    // -------------------------------------------------------------------

    private fun handleIncoming(text: String, device: PairedDevice) {
        val message = decodeServerMessage(text) ?: return // unknown/forward-incompatible type: ignore, don't crash

        when (message) {
            is ServerMessage.HelloAck -> {
                lastKnownHostname = message.hostname
                val requestId = UUID.randomUUID().toString()
                currentAuthRequestId = requestId
                webSocket?.send(
                    encodeClientMessage(ClientMessage.AuthRequest(requestId = requestId, deviceId = device.deviceId))
                )
            }

            is ServerMessage.AuthChallenge -> {
                if (message.requestId != currentAuthRequestId) return
                val secretBytes = try {
                    Base64.getDecoder().decode(device.deviceSecretB64)
                } catch (_: IllegalArgumentException) {
                    _connectionStatus.value = ConnectionStatus.Error("cihaz anahtarı okunamadı")
                    return
                }
                val hmac = HmacAuth.computeAuthResponse(secretBytes, message.nonce, device.deviceId)
                webSocket?.send(
                    encodeClientMessage(ClientMessage.AuthResponse(requestId = message.requestId, hmacB64 = hmac))
                )
            }

            is ServerMessage.AuthSuccess -> {
                if (message.requestId != currentAuthRequestId) return
                attempt = 0
                _connectionStatus.value = ConnectionStatus.Connected(hostname = lastKnownHostname ?: device.host)
                webSocket?.send(
                    encodeClientMessage(ClientMessage.Subscribe(topics = listOf("metrics", "file_events", "alerts")))
                )
                startWatchdog()
            }

            is ServerMessage.AuthFailed -> {
                if (message.requestId != currentAuthRequestId) return
                _connectionStatus.value = ConnectionStatus.Unauthorized(message.reason)
                when (message.reason) {
                    "locked_out" -> scheduleReconnect() // transient rate limit: worth retrying later
                    else -> {
                        // bad_hmac / unknown_device / revoked: retrying won't fix a wrong or
                        // revoked secret, and would just add lockout pressure server-side.
                        // Requires an explicit user action (re-pair) instead.
                        manualDisconnect = true
                        webSocket?.close(1000, "auth failed: ${message.reason}")
                    }
                }
            }

            is ServerMessage.Metrics -> _metrics.value = message

            is ServerMessage.FileEvent -> _liveFileEvents.tryEmit(
                FileEventData(
                    ts = message.ts,
                    op = message.op,
                    path = message.path,
                    oldPath = message.oldPath,
                    sizeBytes = message.sizeBytes,
                    isDir = message.isDir,
                )
            )

            is ServerMessage.Alert -> _liveAlerts.tryEmit(
                AlertData(
                    ts = message.ts,
                    id = message.id,
                    kind = message.kind,
                    severity = message.severity,
                    message = message.message,
                    context = message.context,
                )
            )

            is ServerMessage.Error -> {
                val requestId = message.requestId
                if (requestId != null && pendingRequests.containsKey(requestId)) {
                    pendingRequests.remove(requestId)?.complete(message)
                } else {
                    // A connection-level error with no matching pending request (e.g. a
                    // bad_request from an unsupported proto_version, §9) is fatal to this
                    // handshake attempt: retrying the same hello would just fail again.
                    Log.w(TAG, "connection-level server error: reason=${message.reason}")
                    manualDisconnect = true
                    _connectionStatus.value = ConnectionStatus.Error(message.message ?: message.reason)
                    webSocket?.close(1000, "server error: ${message.reason}")
                }
            }

            else -> {
                val requestId = message.requestIdOrNull()
                if (requestId != null) {
                    pendingRequests.remove(requestId)?.complete(message)
                }
            }
        }
    }

    @Volatile private var lastKnownHostname: String? = null

    // -------------------------------------------------------------------
    // Request/response calls
    // -------------------------------------------------------------------

    private suspend fun sendRequest(message: ClientMessage, requestId: String): Result<ServerMessage> {
        if (_connectionStatus.value !is ConnectionStatus.Connected) {
            return Result.failure(IllegalStateException("not_connected"))
        }
        val ws = webSocket ?: return Result.failure(IllegalStateException("not_connected"))
        val deferred = CompletableDeferred<ServerMessage>()
        pendingRequests[requestId] = deferred
        val sent = ws.send(encodeClientMessage(message))
        if (!sent) {
            pendingRequests.remove(requestId)
            return Result.failure(IllegalStateException("send_failed"))
        }
        return try {
            Result.success(withTimeout(REQUEST_TIMEOUT_MILLIS) { deferred.await() })
        } catch (e: TimeoutCancellationException) {
            Result.failure(e)
        } finally {
            pendingRequests.remove(requestId)
        }
    }

    suspend fun listProcesses(query: String? = null): Result<ServerMessage.ProcessList> {
        val requestId = UUID.randomUUID().toString()
        return sendRequest(ClientMessage.ListProcesses(requestId, query), requestId)
            .mapCatchingType()
    }

    suspend fun killProcess(pid: Int): Result<ServerMessage.ActionResult> {
        val requestId = UUID.randomUUID().toString()
        return sendRequest(ClientMessage.KillProcess(requestId, pid), requestId).mapCatchingType()
    }

    suspend fun listAllowedApps(): Result<ServerMessage.AllowedApps> {
        val requestId = UUID.randomUUID().toString()
        return sendRequest(ClientMessage.ListAllowedApps(requestId), requestId).mapCatchingType()
    }

    suspend fun launchApp(appId: String): Result<ServerMessage.ActionResult> {
        val requestId = UUID.randomUUID().toString()
        return sendRequest(ClientMessage.LaunchApp(requestId, appId), requestId).mapCatchingType()
    }

    suspend fun listFileEvents(
        fromTs: Long? = null,
        toTs: Long? = null,
        op: String? = null,
        minSizeBytes: Long = 0,
        pathPrefix: String? = null,
        limit: Int = 100,
        offset: Int = 0,
    ): Result<ServerMessage.FileEventsResult> {
        val requestId = UUID.randomUUID().toString()
        return sendRequest(
            ClientMessage.ListFileEvents(requestId, fromTs, toTs, op, minSizeBytes, pathPrefix, limit, offset),
            requestId,
        ).mapCatchingType()
    }

    suspend fun topGrowth(window: String, limit: Int = 20): Result<ServerMessage.TopGrowthResult> {
        val requestId = UUID.randomUUID().toString()
        return sendRequest(ClientMessage.TopGrowth(requestId, window, limit), requestId).mapCatchingType()
    }

    suspend fun listAlerts(limit: Int = 50): Result<ServerMessage.AlertsResult> {
        val requestId = UUID.randomUUID().toString()
        return sendRequest(ClientMessage.ListAlerts(requestId, limit), requestId).mapCatchingType()
    }

    suspend fun listHistory(
        fromTs: Long? = null,
        toTs: Long? = null,
        limit: Int = 50,
        offset: Int = 0,
    ): Result<ServerMessage.HistoryResult> {
        val requestId = UUID.randomUUID().toString()
        return sendRequest(ClientMessage.ListHistory(requestId, fromTs, toTs, limit, offset), requestId)
            .mapCatchingType()
    }

    private fun failAllPending(reason: String) {
        val ex = IllegalStateException(reason)
        pendingRequests.keys.toList().forEach { key ->
            pendingRequests.remove(key)?.completeExceptionally(ex)
        }
    }

    private fun rootCauseOf(t: Throwable): Throwable {
        var cur = t
        while (cur.cause != null && cur.cause !== cur) cur = cur.cause!!
        return cur
    }
}

@Suppress("UNCHECKED_CAST")
private inline fun <reified T : ServerMessage> Result<ServerMessage>.mapCatchingType(): Result<T> =
    mapCatching { msg ->
        when (msg) {
            is T -> msg
            is ServerMessage.Error -> throw ServerErrorException(msg.reason, msg.message)
            else -> throw IllegalStateException("unexpected_reply:${msg::class.simpleName}")
        }
    }

class ServerErrorException(val reasonCode: String, val detail: String?) :
    Exception(detail ?: reasonCode)
