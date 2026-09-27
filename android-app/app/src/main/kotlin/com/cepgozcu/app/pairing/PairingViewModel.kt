package com.cepgozcu.app.pairing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cepgozcu.app.connection.ConnectionRepository
import com.cepgozcu.app.net.protocol.PairBeginResponse
import com.cepgozcu.app.security.AgentSession
import com.cepgozcu.app.security.CredentialStore
import com.cepgozcu.app.security.UnpinnedFingerprintFetcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** A resolved wire error shown by the PIN screen; kept as data (not a raw string) so the Composable can pick the right localized string resource. */
sealed class PinSubmitError {
    data object WrongPin : PinSubmitError()
    data class Generic(val message: String) : PinSubmitError()
}

sealed class PairingUiState {
    data object Start : PairingUiState()
    data object ManualEntry : PairingUiState()
    data object CheckingReachability : PairingUiState()
    data class QrParseError(val raw: String) : PairingUiState()
    data class Unreachable(val fromManualEntry: Boolean) : PairingUiState()
    data class FingerprintConfirm(val host: String, val port: Int, val pairingId: String, val certSha256: String, val formattedFingerprint: String) : PairingUiState()
    data class PinEntry(val host: String, val port: Int, val pairingId: String, val certSha256: String, val submitting: Boolean = false, val error: PinSubmitError? = null) : PairingUiState()
    data class PendingApproval(val host: String, val port: Int, val pairingId: String, val certSha256: String) : PairingUiState()
    data object Approved : PairingUiState()
    data object Rejected : PairingUiState()
    data object Expired : PairingUiState()
    data class FatalError(val message: String) : PairingUiState()
}

/**
 * Drives the whole pairing flow end to end: QR parse -> reachability check -> (manual entry only)
 * TOFU fingerprint confirmation -> PIN entry -> approval polling -> saved session. A single
 * instance is shared across every pairing screen (see the nested "pairing" nav graph in NavGraph.kt).
 */
class PairingViewModel(
    private val pairingRepository: PairingRepository,
    private val credentialStore: CredentialStore,
    private val connectionRepository: ConnectionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<PairingUiState>(PairingUiState.Start)
    val state: StateFlow<PairingUiState> = _state.asStateFlow()

    private var pollJob: Job? = null

    fun resetToStart() {
        pollJob?.cancel()
        _state.value = PairingUiState.Start
    }

    fun goToManualEntry() {
        pollJob?.cancel()
        _state.value = PairingUiState.ManualEntry
    }

    /** raw == null means the user cancelled the scan (back button) — just return to Start, not an error. */
    fun onQrScanned(raw: String?) {
        if (raw == null) {
            _state.value = PairingUiState.Start
            return
        }
        val parsed: PairBeginResponse? = pairingRepository.parseQrPayload(raw)
        if (parsed == null) {
            _state.value = PairingUiState.QrParseError(raw)
            return
        }
        _state.value = PairingUiState.CheckingReachability
        viewModelScope.launch {
            val host = pairingRepository.findReachableHost(parsed.hostCandidates, parsed.port, parsed.certSha256)
            _state.value = if (host != null) {
                PairingUiState.PinEntry(host, parsed.port, parsed.pairingId, parsed.certSha256)
            } else {
                PairingUiState.Unreachable(fromManualEntry = false)
            }
        }
    }

    fun onManualEntrySubmitted(host: String, port: Int, pairingId: String) {
        _state.value = PairingUiState.CheckingReachability
        viewModelScope.launch {
            when (val result = UnpinnedFingerprintFetcher.fetchFingerprint(host, port)) {
                is UnpinnedFingerprintFetcher.Result.Fetched -> {
                    _state.value = PairingUiState.FingerprintConfirm(
                        host, port, pairingId, result.sha256Hex,
                        UnpinnedFingerprintFetcher.formatForDisplay(result.sha256Hex),
                    )
                }
                UnpinnedFingerprintFetcher.Result.Unreachable -> {
                    _state.value = PairingUiState.Unreachable(fromManualEntry = true)
                }
            }
        }
    }

    fun onFingerprintConfirmed(matches: Boolean) {
        val current = _state.value as? PairingUiState.FingerprintConfirm ?: return
        _state.value = if (matches) {
            PairingUiState.PinEntry(current.host, current.port, current.pairingId, current.certSha256)
        } else {
            PairingUiState.Start
        }
    }

    fun submitPin(pin: String, deviceName: String) {
        val current = _state.value as? PairingUiState.PinEntry ?: return
        _state.value = current.copy(submitting = true, error = null)
        viewModelScope.launch {
            val outcome = pairingRepository.verifyPin(
                host = current.host,
                port = current.port,
                certSha256 = current.certSha256,
                pairingId = current.pairingId,
                pin = pin,
                deviceName = deviceName,
                devicePublicKeyBase64 = credentialStore.getOrCreateInstallIdentifier(),
            )
            handleOutcome(outcome, current.host, current.port, current.pairingId, current.certSha256)
        }
    }

    private fun startPolling(host: String, port: Int, pairingId: String, certSha256: String) {
        _state.value = PairingUiState.PendingApproval(host, port, pairingId, certSha256)
        pollJob?.cancel()
        pollJob = viewModelScope.launch {
            var consecutiveErrors = 0
            while (true) {
                delay(2_000)
                val outcome = pairingRepository.pollStatus(host, port, certSha256, pairingId)
                if (outcome is PairingOutcome.Error) {
                    consecutiveErrors++
                    if (consecutiveErrors >= 5) {
                        _state.value = PairingUiState.FatalError(outcome.message)
                        return@launch
                    }
                    continue
                }
                consecutiveErrors = 0
                if (outcome is PairingOutcome.Pending) continue
                handleOutcome(outcome, host, port, pairingId, certSha256)
                return@launch
            }
        }
    }

    private fun handleOutcome(outcome: PairingOutcome, host: String, port: Int, pairingId: String, certSha256: String) {
        when (outcome) {
            is PairingOutcome.Approved -> {
                pollJob?.cancel()
                val session = AgentSession(
                    host = outcome.host,
                    port = outcome.port,
                    certSha256 = outcome.certSha256,
                    deviceId = outcome.deviceId,
                    sessionToken = outcome.sessionToken,
                    refreshToken = outcome.refreshToken,
                )
                credentialStore.saveSession(session)
                connectionRepository.startFrom(session)
                _state.value = PairingUiState.Approved
            }
            PairingOutcome.Pending -> startPolling(host, port, pairingId, certSha256)
            PairingOutcome.Rejected -> _state.value = PairingUiState.Rejected
            PairingOutcome.Expired -> _state.value = PairingUiState.Expired
            PairingOutcome.WrongPin -> _state.value = PairingUiState.PinEntry(host, port, pairingId, certSha256, error = PinSubmitError.WrongPin)
            is PairingOutcome.Error -> _state.value = PairingUiState.PinEntry(host, port, pairingId, certSha256, error = PinSubmitError.Generic(outcome.message))
        }
    }

    override fun onCleared() {
        pollJob?.cancel()
        super.onCleared()
    }
}
