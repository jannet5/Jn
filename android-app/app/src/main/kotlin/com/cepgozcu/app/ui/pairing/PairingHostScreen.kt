package com.cepgozcu.app.ui.pairing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cepgozcu.app.pairing.PairingUiState
import com.cepgozcu.app.pairing.PairingViewModel

/** Single route that renders the right screen for whatever step [PairingViewModel.state] is currently at — the whole pairing flow lives in one place instead of many nav destinations. */
@Composable
fun PairingHostScreen(viewModel: PairingViewModel, onApproved: () -> Unit) {
    val state by viewModel.state.collectAsState()

    when (val s = state) {
        PairingUiState.Start -> QrScanScreen(
            onScanned = viewModel::onQrScanned,
            onUseManualEntry = viewModel::goToManualEntry,
        )
        PairingUiState.ManualEntry -> ManualEntryScreen(
            onSubmit = viewModel::onManualEntrySubmitted,
            onCancel = viewModel::resetToStart,
        )
        PairingUiState.CheckingReachability -> CheckingReachabilityScreen()
        is PairingUiState.QrParseError -> QrParseErrorScreen(
            onRetryScan = viewModel::resetToStart,
            onUseManualEntry = viewModel::goToManualEntry,
        )
        is PairingUiState.Unreachable -> UnreachableScreen(
            fromManualEntry = s.fromManualEntry,
            onRetry = { if (s.fromManualEntry) viewModel.goToManualEntry() else viewModel.resetToStart() },
            onUseManualEntry = if (!s.fromManualEntry) viewModel::goToManualEntry else null,
        )
        is PairingUiState.FingerprintConfirm -> FingerprintConfirmScreen(
            formattedFingerprint = s.formattedFingerprint,
            onConfirm = viewModel::onFingerprintConfirmed,
        )
        is PairingUiState.PinEntry -> PinEntryScreen(
            submitting = s.submitting,
            error = s.error,
            onSubmit = viewModel::submitPin,
        )
        is PairingUiState.PendingApproval -> PendingApprovalScreen()
        PairingUiState.Approved -> LaunchedEffect(Unit) { onApproved() }
        PairingUiState.Rejected -> RejectedScreen(onRetry = viewModel::resetToStart)
        PairingUiState.Expired -> ExpiredScreen(onRetry = viewModel::resetToStart)
        is PairingUiState.FatalError -> PairingFatalErrorScreen(message = s.message, onRetry = viewModel::resetToStart)
    }
}
