package com.cepgozcu.app.connection

import com.cepgozcu.app.net.AgentApi
import com.cepgozcu.app.net.AgentApiContract
import com.cepgozcu.app.net.AgentConnection
import com.cepgozcu.app.net.AgentConnectionContract
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.Envelope
import com.cepgozcu.app.security.AgentSession
import com.cepgozcu.app.security.CredentialStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Wraps the current (nullable — unpaired has none) [AgentConnection]/[AgentApi] pair for the
 * whole app to share. Recreated whenever a new [AgentSession] is stored (fresh pairing, or app
 * cold start with a session already on disk).
 */
class ConnectionRepository(private val credentialStore: CredentialStore) {

    private var connection: AgentConnection? = null
    private var stateMirrorJob: Job? = null
    private val mirrorScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    var api: AgentApiContract? = null
        private set

    val rawConnection: AgentConnectionContract?
        get() = connection

    val pushes: SharedFlow<Envelope>?
        get() = connection?.pushes

    /** Recreates the connection from whatever session is currently stored, if any, and connects. Call once at app start. */
    fun startFromStoredSessionIfAny() {
        val session = credentialStore.loadSession() ?: return
        startFrom(session)
    }

    /** Tears down any previous connection and starts a brand-new one from a freshly-approved session (right after pairing succeeds). */
    fun startFrom(session: AgentSession) {
        connection?.disconnect()
        val fresh = AgentConnection(session)
        connection = fresh
        api = AgentApi(fresh)
        mirrorState(fresh)
        fresh.connect()
    }

    private fun mirrorState(conn: AgentConnection) {
        stateMirrorJob?.cancel()
        stateMirrorJob = mirrorScope.launch {
            conn.state.collect { _connectionState.value = it }
        }
    }

    /** Best-effort tells the agent to forget this device, then always clears local credentials and tears the connection down regardless of whether the network call succeeded. */
    suspend fun unpairAndClear() {
        runCatching { api?.unpair() }
        credentialStore.clear()
        connection?.disconnect()
        stateMirrorJob?.cancel()
        connection = null
        api = null
        _connectionState.value = ConnectionState.Idle
    }

    fun disconnect() {
        connection?.disconnect()
    }

    val currentSessionHost: String?
        get() = credentialStore.loadSession()?.host

    val currentSession: AgentSession?
        get() = credentialStore.loadSession()
}
