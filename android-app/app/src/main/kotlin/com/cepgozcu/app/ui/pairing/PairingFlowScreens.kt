package com.cepgozcu.app.ui.pairing

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cepgozcu.app.R
import com.cepgozcu.app.pairing.PinSubmitError

@Composable
fun ManualEntryScreen(onSubmit: (host: String, port: Int, pairingId: String) -> Unit, onCancel: () -> Unit) {
    var host by remember { mutableStateOf("") }
    var portText by remember { mutableStateOf("8443") }
    var pairingId by remember { mutableStateOf("") }
    val portValue = portText.toIntOrNull()
    val canSubmit = host.isNotBlank() && pairingId.isNotBlank() && portValue != null

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(stringResource(R.string.pairing_manual_title), style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = host,
                onValueChange = { host = it },
                label = { Text(stringResource(R.string.pairing_manual_host_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = portText,
                onValueChange = { portText = it.filter(Char::isDigit) },
                label = { Text(stringResource(R.string.pairing_manual_port_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = pairingId,
                onValueChange = { pairingId = it },
                label = { Text(stringResource(R.string.pairing_manual_pairing_id_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { onSubmit(host.trim(), portValue ?: 0, pairingId.trim()) },
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth(),
            ) { Text(stringResource(R.string.pairing_manual_continue)) }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    }
}

@Composable
fun CheckingReachabilityScreen() {
    CenteredMessage {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.state_loading))
    }
}

@Composable
fun QrParseErrorScreen(onRetryScan: () -> Unit, onUseManualEntry: () -> Unit) {
    CenteredMessage {
        Text(stringResource(R.string.pairing_qr_parse_error), textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onRetryScan, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.state_retry)) }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onUseManualEntry, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.pairing_qr_try_manual)) }
    }
}

@Composable
fun UnreachableScreen(fromManualEntry: Boolean, onRetry: () -> Unit, onUseManualEntry: (() -> Unit)?) {
    CenteredMessage {
        Text(
            if (fromManualEntry) stringResource(R.string.pairing_manual_not_reachable) else stringResource(R.string.state_offline_message),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.state_retry)) }
        if (onUseManualEntry != null) {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onUseManualEntry, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.pairing_manual_entry)) }
        }
    }
}

@Composable
fun FingerprintConfirmScreen(formattedFingerprint: String, onConfirm: (matches: Boolean) -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(Icons.Filled.Fingerprint, contentDescription = null, modifier = Modifier.height(48.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.pairing_fingerprint_title), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.pairing_fingerprint_question), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(20.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    formattedFingerprint,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(R.string.pairing_fingerprint_warning),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = { onConfirm(true) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.pairing_fingerprint_match_yes))
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { onConfirm(false) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.pairing_fingerprint_match_no))
            }
        }
    }
}

@Composable
fun PinEntryScreen(
    submitting: Boolean,
    error: PinSubmitError?,
    onSubmit: (pin: String, deviceName: String) -> Unit,
) {
    var pin by remember { mutableStateOf("") }
    var deviceName by remember { mutableStateOf(Build.MODEL ?: "Android") }

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(stringResource(R.string.pairing_pin_title), style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.pairing_pin_hint), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = { if (it.length <= 6) pin = it.filter(Char::isDigit) },
                label = { Text(stringResource(R.string.pairing_pin_title)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = deviceName,
                onValueChange = { deviceName = it },
                label = { Text(stringResource(R.string.pairing_device_name_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            if (error != null) {
                Spacer(Modifier.height(12.dp))
                val message = when (error) {
                    PinSubmitError.WrongPin -> stringResource(R.string.pairing_pin_wrong)
                    is PinSubmitError.Generic -> stringResource(R.string.pairing_error_generic, error.message)
                }
                Text(message, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { onSubmit(pin, deviceName.ifBlank { "Android" }) },
                enabled = pin.length == 6 && !submitting,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (submitting) {
                    CircularProgressIndicator(modifier = Modifier.height(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(R.string.pairing_pin_submit))
                }
            }
        }
    }
}

@Composable
fun PendingApprovalScreen() {
    CenteredMessage {
        Icon(Icons.Filled.HourglassEmpty, contentDescription = null, modifier = Modifier.height(48.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.pairing_waiting_approval), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.pairing_waiting_approval_hint), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))
        CircularProgressIndicator()
    }
}

@Composable
fun RejectedScreen(onRetry: () -> Unit) {
    CenteredMessage {
        Text(stringResource(R.string.pairing_rejected), textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.pairing_retry)) }
    }
}

@Composable
fun ExpiredScreen(onRetry: () -> Unit) {
    CenteredMessage {
        Text(stringResource(R.string.pairing_expired), textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.pairing_retry)) }
    }
}

@Composable
fun PairingFatalErrorScreen(message: String, onRetry: () -> Unit) {
    CenteredMessage {
        Text(stringResource(R.string.pairing_error_generic, message), textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.pairing_retry)) }
    }
}

@Composable
private fun CenteredMessage(content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content,
        )
    }
}
