package app.nokta.a

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.nokta.a.bubble.BubbleService
import app.nokta.a.ui.NoktaViewModel
import app.nokta.a.ui.NoktaScreen
import app.nokta.a.ui.ScreenActions
import app.nokta.a.ui.theme.NoktaTheme

class MainActivity : ComponentActivity() {
    private val vm: NoktaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoktaTheme {
                val ui by vm.ui.collectAsStateWithLifecycle()
                val ctx = LocalContext.current
                var bubbleOn by remember { mutableStateOf(BubbleService.isEnabled(ctx)) }
                var overlayOk by remember { mutableStateOf(Settings.canDrawOverlays(ctx)) }

                val notifLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                    BubbleService.start(ctx)
                }
                fun startBubble() {
                    if (Build.VERSION.SDK_INT >= 33 &&
                        ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                    ) notifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) else BubbleService.start(ctx)
                }

                // Ayar dönüşünde izin durumunu yenile; izin varsa ve açıksa balonu başlat.
                val owner = LocalLifecycleOwner.current
                DisposableEffect(owner) {
                    val obs = LifecycleEventObserver { _, e ->
                        if (e == Lifecycle.Event.ON_RESUME) {
                            overlayOk = Settings.canDrawOverlays(ctx)
                            bubbleOn = BubbleService.isEnabled(ctx)
                            if (overlayOk && bubbleOn) startBubble()
                        }
                    }
                    owner.lifecycle.addObserver(obs)
                    onDispose { owner.lifecycle.removeObserver(obs) }
                }

                NoktaScreen(
                    ui = ui, bubbleOn = bubbleOn, overlayGranted = overlayOk,
                    actions = ScreenActions(
                        onAdd = vm::add, onToggle = vm::toggle, onDelete = vm::remove, onMove = vm::move,
                        onUndo = vm::undo, onDismissUndo = vm::dismissUndo, onClearDone = vm::clearDone,
                        onExport = { share(vm.exportText()) },
                        onToggleBubble = {
                            if (bubbleOn) { BubbleService.setEnabled(ctx, false); BubbleService.stop(ctx); bubbleOn = false }
                            else { BubbleService.setEnabled(ctx, true); bubbleOn = true; if (overlayOk) startBubble() }
                        },
                        onGrantOverlay = {
                            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                        },
                    ),
                )
            }
        }
    }

    override fun onStart() { super.onStart(); BubbleService.setAppVisible(true) }
    override fun onStop() { super.onStop(); BubbleService.setAppVisible(false) }

    private fun share(text: String) {
        val send = Intent(Intent.ACTION_SEND).setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, text).putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        startActivity(Intent.createChooser(send, getString(R.string.export_list)))
    }
}
