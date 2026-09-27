package com.cepgozcu.app.pairing

import com.cepgozcu.app.net.protocol.PairVerifyResponse
import com.cepgozcu.app.net.protocol.PairingState
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Exercises [PairingRepository.toOutcome] — the mapping from the wire-level [PairingState] enum
 * to the app's [PairingOutcome] state machine — directly, with no network involved (that mapping
 * is what every pairing screen branches on).
 */
class PairingOutcomeTest {

    private val repository = PairingRepository()
    private val host = "192.168.1.50"
    private val port = 8443
    private val certSha256 = "ff".repeat(32)

    @Test
    fun `approved maps to Approved with session fields`() {
        val response = PairVerifyResponse(
            state = PairingState.Approved,
            sessionToken = "session-token",
            refreshToken = "refresh-token",
            deviceId = "device-1",
            expiresAt = "2026-10-01T00:00:00Z",
        )

        val outcome = repository.toOutcome(response, host, port, certSha256)

        assertThat(outcome).isInstanceOf(PairingOutcome.Approved::class.java)
        outcome as PairingOutcome.Approved
        assertThat(outcome.host).isEqualTo(host)
        assertThat(outcome.port).isEqualTo(port)
        assertThat(outcome.certSha256).isEqualTo(certSha256)
        assertThat(outcome.deviceId).isEqualTo("device-1")
        assertThat(outcome.sessionToken).isEqualTo("session-token")
        assertThat(outcome.refreshToken).isEqualTo("refresh-token")
    }

    @Test
    fun `rejected maps to Rejected`() {
        val response = PairVerifyResponse(state = PairingState.Rejected)
        assertThat(repository.toOutcome(response, host, port, certSha256)).isEqualTo(PairingOutcome.Rejected)
    }

    @Test
    fun `expired maps to Expired`() {
        val response = PairVerifyResponse(state = PairingState.Expired)
        assertThat(repository.toOutcome(response, host, port, certSha256)).isEqualTo(PairingOutcome.Expired)
    }

    @Test
    fun `pendingApproval maps to Pending`() {
        val response = PairVerifyResponse(state = PairingState.PendingApproval)
        assertThat(repository.toOutcome(response, host, port, certSha256)).isEqualTo(PairingOutcome.Pending)
    }

    @Test
    fun `awaitingPin maps to WrongPin`() {
        val response = PairVerifyResponse(state = PairingState.AwaitingPin)
        assertThat(repository.toOutcome(response, host, port, certSha256)).isEqualTo(PairingOutcome.WrongPin)
    }

    @Test
    fun `parseQrPayload returns null for garbage input instead of throwing`() {
        assertThat(repository.parseQrPayload("not json at all")).isNull()
    }

    @Test
    fun `parseQrPayload parses a well-formed PairBeginResponse`() {
        val raw = """{"pairingId":"abc","hostCandidates":["10.0.0.5"],"port":8443,"certSha256":"${"aa".repeat(32)}","pinExpiresInSeconds":300}"""
        val parsed = repository.parseQrPayload(raw)
        assertThat(parsed).isNotNull()
        assertThat(parsed!!.pairingId).isEqualTo("abc")
        assertThat(parsed.hostCandidates).containsExactly("10.0.0.5")
    }
}
