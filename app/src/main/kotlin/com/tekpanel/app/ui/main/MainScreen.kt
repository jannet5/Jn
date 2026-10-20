package com.tekpanel.app.ui.main

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.tekpanel.app.R
import com.tekpanel.app.ui.access.AccessBanner
import com.tekpanel.app.ui.access.AccessGuideSheet
import com.tekpanel.app.ui.channels.ChannelPickerSheet
import com.tekpanel.app.ui.common.ConfirmDialog
import com.tekpanel.app.ui.diagnostics.DiagnosticsActivity
import com.tekpanel.app.ui.theme.Tek
import com.tekpanel.app.util.AccessSettings
import com.tekpanel.app.util.ListenerHealth
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

private const val DIAGNOSTICS_TAPS = 7
private const val DIAGNOSTICS_WINDOW_MS = 3_000L
private const val ACCESS_POLL_MS = 1_500L

/**
 * The only screen. Back never leaves the app: it first closes whatever is open (viewer, menu,
 * expanded cards) and only then sends the app to the background.
 */
@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val undo by viewModel.undo.collectAsState()
    val diagnosticsUnlocked by viewModel.diagnosticsUnlocked.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    var menuOpen by rememberSaveable { mutableStateOf(false) }
    var showPicker by rememberSaveable { mutableStateOf(false) }
    var showGuide by rememberSaveable { mutableStateOf(false) }
    var confirmClear by rememberSaveable { mutableStateOf(false) }
    var confirmErase by rememberSaveable { mutableStateOf(false) }
    var showPrivacy by rememberSaveable { mutableStateOf(false) }
    var viewerPath by rememberSaveable { mutableStateOf<String?>(null) }
    var titleTaps by remember { mutableIntStateOf(0) }
    var firstTapAt by remember { mutableLongStateOf(0L) }

    // Re-check the permission whenever the user comes back from Android settings, and keep
    // checking while the banner is visible so it disappears the moment access is granted.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshAccess()
                viewModel.refreshInstalledApps()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            while (true) {
                viewModel.refreshAccess()
                delay(ACCESS_POLL_MS)
            }
        }
    }

    val undoLabel = stringResource(R.string.action_undo)
    LaunchedEffect(undo) {
        val request = undo ?: return@LaunchedEffect
        val result = snackbar.showSnackbar(message = request.label, actionLabel = undoLabel)
        if (result == SnackbarResult.ActionPerformed) viewModel.undo() else viewModel.dismissUndo()
    }

    val copiedText = stringResource(R.string.copied)
    val openFailedText = stringResource(R.string.open_source_failed)
    val replySentText = stringResource(R.string.reply_sent)
    val replyFailedText = stringResource(R.string.reply_failed)
    val exportTitle = stringResource(R.string.export_share_title)
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                UiEvent.Copied -> snackbar.showSnackbar(copiedText)
                UiEvent.OpenFailed -> snackbar.showSnackbar(openFailedText)
                UiEvent.ReplySent -> snackbar.showSnackbar(replySentText)
                UiEvent.ReplyFailed -> snackbar.showSnackbar(replyFailedText)
                is UiEvent.ExportReady -> {
                    val send = Intent(Intent.ACTION_SEND).apply {
                        type = "application/json"
                        putExtra(Intent.EXTRA_TEXT, event.json)
                    }
                    context.startActivity(Intent.createChooser(send, exportTitle))
                }
            }
        }
    }

    BackHandler {
        when {
            menuOpen -> menuOpen = false
            viewModel.collapseAll() -> Unit
            else -> (context as? Activity)?.moveTaskToBack(true)
        }
    }

    val markReadLabel = stringResource(R.string.mark_read_undo_label)
    val clearedLabel = stringResource(R.string.clear_screen_undo_label)

    MainContent(
        state = state,
        menuOpen = menuOpen,
        diagnosticsUnlocked = diagnosticsUnlocked,
        levelOf = { viewModel.levels[it] ?: Level.Collapsed },
        isLong = { id, default -> viewModel.lengthened[id] ?: default },
        canReply = viewModel::canReply,
        snackbarHostState = snackbar,
        callbacks = MainCallbacks(
            onTitleTap = {
                val now = System.currentTimeMillis()
                if (now - firstTapAt > DIAGNOSTICS_WINDOW_MS) {
                    firstTapAt = now
                    titleTaps = 1
                } else if (++titleTaps >= DIAGNOSTICS_TAPS) {
                    titleTaps = 0
                    viewModel.unlockDiagnostics()
                }
            },
            onClear = { confirmClear = true },
            onToggleMenu = { menuOpen = !menuOpen },
            onChannels = { menuOpen = false; showPicker = true },
            onExport = { menuOpen = false; viewModel.requestExport() },
            onPrivacy = { menuOpen = false; showPrivacy = true },
            onErase = { menuOpen = false; confirmErase = true },
            onDiagnostics = { menuOpen = false; context.startActivity(Intent(context, DiagnosticsActivity::class.java)) },
            onSelect = viewModel::select,
            onAdd = { showPicker = true },
            onOpenSettings = { AccessSettings.openNotificationAccess(context) },
            onHowTo = { showGuide = true },
            onRestartListener = viewModel::restartListener,
            onBatterySettings = { ListenerHealth.openBatterySettings(context) },
            onCycle = viewModel::cycle,
            onToggleMessage = viewModel::toggleLengthened,
            onMarkRead = { ids -> viewModel.markRead(ids, markReadLabel) },
            onOpenApp = viewModel::openConversation,
            onReply = viewModel::reply,
            onCopy = { text ->
                copyToClipboard(context, text)
                viewModel.onCopied()
            },
            onPicture = { path -> viewerPath = path },
        ),
    )

    if (showPicker) ChannelPickerSheet(onDismiss = { showPicker = false })
    if (showGuide) {
        AccessGuideSheet(
            onOpenSettings = { AccessSettings.openNotificationAccess(context) },
            onOpenAppInfo = { AccessSettings.openAppInfo(context) },
            onDismiss = { showGuide = false },
        )
    }
    viewerPath?.let { ImageViewer(path = it, onDismiss = { viewerPath = null }) }

    if (confirmClear) {
        ConfirmDialog(
            title = stringResource(R.string.clear_screen_confirm_title),
            message = stringResource(R.string.clear_screen_confirm_message),
            confirmLabel = stringResource(R.string.action_clear_screen),
            dismissLabel = stringResource(R.string.action_cancel),
            destructive = false,
            onConfirm = {
                confirmClear = false
                viewModel.clearScreen(clearedLabel)
            },
            onDismiss = { confirmClear = false },
        )
    }
    if (confirmErase) {
        ConfirmDialog(
            title = stringResource(R.string.erase_all_confirm_title),
            message = stringResource(R.string.erase_all_confirm_message),
            confirmLabel = stringResource(R.string.menu_erase),
            dismissLabel = stringResource(R.string.action_cancel),
            onConfirm = {
                confirmErase = false
                viewModel.eraseEverything()
            },
            onDismiss = { confirmErase = false },
        )
    }
    if (showPrivacy) {
        AlertDialog(
            onDismissRequest = { showPrivacy = false },
            title = { Text(stringResource(R.string.privacy_title)) },
            text = { Text(stringResource(R.string.privacy_body)) },
            confirmButton = { TextButton(onClick = { showPrivacy = false }) { Text(stringResource(R.string.action_ok), color = Tek.colors.accent) } },
            containerColor = Tek.colors.surfaceHigh,
            titleContentColor = Tek.colors.text,
            textContentColor = Tek.colors.textSecondary,
        )
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("message", text))
}
