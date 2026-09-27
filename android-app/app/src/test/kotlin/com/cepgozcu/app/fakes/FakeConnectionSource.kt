package com.cepgozcu.app.fakes

import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.AgentApiContract
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.Envelope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/** Test double for [ConnectionSource]: lets a test drive connection-state transitions and push events without any real socket. */
class FakeConnectionSource(initialApi: AgentApiContract? = null) : ConnectionSource {
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    override val connectionState: StateFlow<ConnectionState> = _connectionState

    override var api: AgentApiContract? = initialApi

    private val _pushes = MutableSharedFlow<Envelope>(extraBufferCapacity = 16)
    override val pushes: SharedFlow<Envelope> = _pushes

    fun setState(state: ConnectionState) { _connectionState.value = state }

    suspend fun emitPush(envelope: Envelope) { _pushes.emit(envelope) }
}
