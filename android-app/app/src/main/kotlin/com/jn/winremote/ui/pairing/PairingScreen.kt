package com.jn.winremote.ui.pairing

import android.Manifest
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.winremote.ui.components.ConfirmDialog
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PairingScreen(
    onPaired: () -> Unit,
    onCancel: (() -> Unit)? = null,
    viewModel: PairingViewModel = viewModel(),
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var tabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(uiState) {
        if (uiState is PairingUiState.Success) onPaired()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eşleştirme") },
                navigationIcon = {
                    if (onCancel != null) {
                        androidx.compose.material3.IconButton(onClick = onCancel) {
                            Icon(androidx.compose.material.icons.Icons.Filled.Close, contentDescription = "Kapat")
                        }
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = tabIndex) {
                Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }, text = { Text("QR Tara") })
                Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }, text = { Text("Elle Gir") })
            }

            when (tabIndex) {
                0 -> QrScanTab(
                    onDecoded = { raw ->
                        if (viewModel.applyScannedQr(raw)) {
                            tabIndex = 1
                        }
                    },
                )
                else -> ManualEntryTab(viewModel = viewModel, form = form, uiState = uiState)
            }
        }
    }
}

@Composable
private fun QrScanTab(onDecoded: (String) -> Unit) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!hasPermission) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = null, modifier = Modifier.size(48.dp))
                androidx.compose.foundation.layout.Spacer(Modifier.size(16.dp))
                Text(
                    "QR kodu taramak için kameraya izin verilmesi gerekiyor.",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                androidx.compose.foundation.layout.Spacer(Modifier.size(16.dp))
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("İzin ver")
                }
                androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
                Text(
                    "Kamerayı kullanmak istemiyorsanız \"Elle Gir\" sekmesinden de eşleştirebilirsiniz.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        } else {
            CameraPreviewWithScanner(onDecoded = onDecoded)
        }
    }
}

@Composable
private fun CameraPreviewWithScanner(onDecoded: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { Executors.newSingleThreadExecutor() }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val providerFuture = ProcessCameraProvider.getInstance(ctx)
                providerFuture.addListener({
                    val provider = providerFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val analysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also { it.setAnalyzer(executor, QrAnalyzer(onDecoded)) }
                    try {
                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            analysis,
                        )
                    } catch (_: Exception) {
                        // Camera bind can fail on devices without the requested camera; the
                        // manual-entry tab remains fully usable regardless.
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
        )
        Text(
            "QR kodunu çerçeve içine hizalayın",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            color = androidx.compose.ui.graphics.Color.White,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ManualEntryTab(
    viewModel: PairingViewModel,
    form: PairingFormState,
    uiState: PairingUiState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            "Windows bilgisayarınızda gösterilen eşleştirme bilgilerini girin.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        OutlinedTextField(
            value = form.host,
            onValueChange = { v -> viewModel.updateForm { it.copy(host = v) } },
            label = { Text("Sunucu adresi (IP)") },
            placeholder = { Text("192.168.1.20") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = form.port,
            onValueChange = { v -> viewModel.updateForm { it.copy(port = v.filter { c -> c.isDigit() }) } },
            label = { Text("Port") },
            singleLine = true,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = form.fingerprint,
            onValueChange = { v -> viewModel.updateForm { it.copy(fingerprint = v) } },
            label = { Text("Sertifika parmak izi (SHA-256)") },
            placeholder = { Text("64 karakter, örn. a1b2c3…") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = form.pairingCode,
            onValueChange = { v -> viewModel.updateForm { it.copy(pairingCode = v) } },
            label = { Text("Eşleştirme kodu") },
            placeholder = { Text("7F3K-9QRT") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = form.deviceName,
            onValueChange = { v -> viewModel.updateForm { it.copy(deviceName = v) } },
            label = { Text("Bu cihazın adı") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = { viewModel.submit() },
            enabled = uiState !is PairingUiState.Connecting,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (uiState is PairingUiState.Connecting) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
                Text("Eşleştiriliyor…")
            } else {
                Text("Eşleştir")
            }
        }

        when (uiState) {
            is PairingUiState.Failure -> Text(
                uiState.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
            is PairingUiState.Success -> Text(
                "Eşleştirme başarılı.",
                color = com.jn.winremote.ui.theme.GreenOk,
                style = MaterialTheme.typography.bodyMedium,
            )
            else -> Unit
        }
    }
}
