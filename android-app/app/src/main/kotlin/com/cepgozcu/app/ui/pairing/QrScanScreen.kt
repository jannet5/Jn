package com.cepgozcu.app.ui.pairing

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cepgozcu.app.R
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

/**
 * Compose-idiomatic wrapper over zxing-android-embedded: launches its CaptureActivity via
 * [ScanContract]/[rememberLauncherForActivityResult] rather than starting a raw Activity by hand.
 */
@Composable
fun QrScanScreen(
    onScanned: (raw: String?) -> Unit,
    onUseManualEntry: () -> Unit,
) {
    val context = LocalContext.current
    var permissionDenied by remember { mutableStateOf(false) }

    val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        onScanned(result.contents)
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            scanLauncher.launch(
                ScanOptions()
                    .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    .setBeepEnabled(false)
                    .setOrientationLocked(false),
            )
        } else {
            permissionDenied = true
        }
    }

    fun startScan() {
        val hasPermission = androidx.core.content.ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            scanLauncher.launch(
                ScanOptions()
                    .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    .setBeepEnabled(false)
                    .setOrientationLocked(false),
            )
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(Unit) { startScan() }

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(Icons.Filled.QrCodeScanner, contentDescription = null, modifier = Modifier.height(56.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.pairing_scan_title), style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.pairing_scan_hint),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (permissionDenied) {
                Spacer(Modifier.height(12.dp))
                Text(
                    stringResource(R.string.pairing_camera_permission_denied),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(24.dp))
            Button(onClick = { startScan() }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.pairing_scan_button))
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onUseManualEntry, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.pairing_manual_entry))
            }
        }
    }
}
