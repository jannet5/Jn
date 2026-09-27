package com.jn.winremote.ui.pairing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jn.winremote.WinRemoteApplication
import com.jn.winremote.crypto.normalizeFingerprint
import com.jn.winremote.data.PairedDevice
import com.jn.winremote.pairing.parseQrPairingPayload
import com.jn.winremote.repository.PairingOutcome
import com.jn.winremote.util.ReasonText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PairingFormState(
    val host: String = "",
    val port: String = "8787",
    val fingerprint: String = "",
    val pairingCode: String = "",
    val deviceName: String = android.os.Build.MODEL ?: "Android",
)

sealed class PairingUiState {
    object Idle : PairingUiState()
    object Connecting : PairingUiState()
    object Success : PairingUiState()
    data class Failure(val message: String) : PairingUiState()
}

class PairingViewModel(application: Application) : AndroidViewModel(application) {

    private val app get() = getApplication<WinRemoteApplication>()

    private val _form = MutableStateFlow(PairingFormState())
    val form: StateFlow<PairingFormState> = _form.asStateFlow()

    private val _uiState = MutableStateFlow<PairingUiState>(PairingUiState.Idle)
    val uiState: StateFlow<PairingUiState> = _uiState.asStateFlow()

    fun updateForm(transform: (PairingFormState) -> PairingFormState) {
        _form.value = transform(_form.value)
    }

    /** Applies a scanned QR payload onto the manual-entry fields, so both paths converge on one form. */
    fun applyScannedQr(raw: String): Boolean {
        val payload = parseQrPairingPayload(raw) ?: return false
        _form.value = _form.value.copy(
            host = payload.host,
            port = payload.port.toString(),
            fingerprint = payload.fingerprint,
            pairingCode = payload.pairingCode,
        )
        return true
    }

    fun submit() {
        val f = _form.value
        val portInt = f.port.trim().toIntOrNull()
        val fingerprint = normalizeFingerprint(f.fingerprint)
        when {
            f.host.isBlank() -> {
                _uiState.value = PairingUiState.Failure("Sunucu adresi (IP veya ana bilgisayar adı) gerekli.")
                return
            }
            portInt == null || portInt <= 0 || portInt > 65535 -> {
                _uiState.value = PairingUiState.Failure("Port 1-65535 aralığında bir sayı olmalı.")
                return
            }
            fingerprint.length != 64 || !fingerprint.matches(Regex("^[0-9a-f]{64}$")) -> {
                _uiState.value = PairingUiState.Failure(
                    "Sertifika parmak izi 64 karakterlik onaltılık (SHA-256) bir değer olmalı."
                )
                return
            }
            f.pairingCode.isBlank() -> {
                _uiState.value = PairingUiState.Failure("Eşleştirme kodu gerekli.")
                return
            }
            f.deviceName.isBlank() -> {
                _uiState.value = PairingUiState.Failure("Cihaz adı gerekli.")
                return
            }
        }
        if (_uiState.value == PairingUiState.Connecting) return

        _uiState.value = PairingUiState.Connecting
        viewModelScope.launch {
            val outcome = app.pairingClient.pair(
                host = f.host.trim(),
                port = portInt!!,
                pinnedFingerprintHex = fingerprint,
                pairingCode = f.pairingCode.trim(),
                deviceName = f.deviceName.trim(),
            )
            _uiState.value = when (outcome) {
                is PairingOutcome.Success -> {
                    persistAndConnect(outcome.device)
                    PairingUiState.Success
                }
                is PairingOutcome.Rejected -> PairingUiState.Failure(ReasonText.forCode(outcome.reasonCode))
                is PairingOutcome.CertMismatch -> PairingUiState.Failure(
                    "sunucu kimliği doğrulanamadı: bu adresteki sertifika beklenen parmak iziyle eşleşmiyor. " +
                        "Bu bir güvenlik uyarısıdır — devam etmeden önce adresi ve parmak izini Windows " +
                        "bilgisayarından tekrar kontrol edin."
                )
                is PairingOutcome.ProtocolError -> PairingUiState.Failure(ReasonText.forCode(outcome.reasonCode))
                is PairingOutcome.ConnectionError -> PairingUiState.Failure(
                    "Sunucuya bağlanılamadı: ${outcome.detail}"
                )
                PairingOutcome.Timeout -> PairingUiState.Failure("Zaman aşımı: sunucu yanıt vermedi.")
            }
        }
    }

    fun resetToIdle() {
        _uiState.value = PairingUiState.Idle
    }

    private fun persistAndConnect(device: PairedDevice) {
        app.secureStore.upsertDevice(device)
        app.secureStore.setActiveDevice(device.deviceId)
        app.repository.connect(device)
    }
}
