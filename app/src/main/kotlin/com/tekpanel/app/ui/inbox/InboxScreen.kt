package com.tekpanel.app.ui.inbox

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tekpanel.app.R
import com.tekpanel.app.ui.channels.ChannelSettingsSheet
import com.tekpanel.app.ui.channels.ChannelSettingsViewModel
import com.tekpanel.app.ui.common.ConfirmDialog
import com.tekpanel.app.ui.theme.TekPanelColors
import org.koin.androidx.compose.koinViewModel

private const val DIAGNOSTICS_UNLOCK_TAP_COUNT = 7
private const val DIAGNOSTICS_UNLOCK_WINDOW_MILLIS = 3_000L

@Composable
fun InboxScreen(
    viewModel: InboxViewModel = koinViewModel(),
    channelSettingsViewModel: ChannelSettingsViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsState()
    val pendingUndo by viewModel.pendingUndo.collectAsState()
    val openSourceFailureTick by viewModel.openSourceFailureTick.collectAsState()
    val diagnosticsUnlocked by channelSettingsViewModel.diagnosticsUnlocked.collectAsState()

    var showChannelSheet by rememberSaveable { mutableStateOf(false) }
    var showClearConfirm by rememberSaveable { mutableStateOf(false) }
    var showEraseConfirm by rememberSaveable { mutableStateOf(false) }

    var tapCount by remember { mutableStateOf(0) }
    var firstTapAt by remember { mutableStateOf(0L) }

    val snackbarHostState = remember { SnackbarHostState() }
    val undoActionLabel = stringResource(R.string.action_undo)
    val openFailedMessage = stringResource(R.string.open_source_failed)

    LaunchedEffect(pendingUndo) {
        val undo = pendingUndo ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(message = undo.label, actionLabel = undoActionLabel)
        if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
            viewModel.undoClear()
        } else {
            viewModel.dismissUndo()
        }
    }

    LaunchedEffect(openSourceFailureTick) {
        if (openSourceFailureTick > 0) snackbarHostState.showSnackbar(openFailedMessage)
    }

    Scaffold(
        containerColor = TekPanelColors.Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = TekPanelColors.TextPrimary,
                        modifier = Modifier.clickable {
                            val now = System.currentTimeMillis()
                            if (now - firstTapAt > DIAGNOSTICS_UNLOCK_WINDOW_MILLIS) {
                                firstTapAt = now
                                tapCount = 1
                            } else {
                                tapCount += 1
                                if (tapCount >= DIAGNOSTICS_UNLOCK_TAP_COUNT) {
                                    tapCount = 0
                                    channelSettingsViewModel.unlockDiagnostics()
                                }
                            }
                        },
                    )
                },
                actions = {
                    IconButton(onClick = { showChannelSheet = true }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(R.string.action_open_channel_settings),
                            tint = TekPanelColors.TextPrimary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TekPanelColors.Background),
            )
        },
    ) { padding ->
        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                OutlinedButton(
                    onClick = { showClearConfirm = true },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TekPanelColors.TextPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TekPanelColors.Outline),
                ) {
                    Text(text = stringResource(R.string.action_clear_screen))
                }
            }

            when (val state = screenState) {
                is InboxScreenState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                is InboxScreenState.NotificationAccessMissing -> NotificationAccessMissingState(
                    onOpenSettings = { context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) },
                    modifier = Modifier.fillMaxSize(),
                )
                is InboxScreenState.NoChannelsInstalled -> NoChannelsInstalledState(modifier = Modifier.fillMaxSize())
                is InboxScreenState.Content -> {
                    ChannelFilterRow(
                        channels = state.channels,
                        selectedChannelId = state.selectedChannelId,
                        onSelect = viewModel::selectChannel,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                    when {
                        state.allChannelsSelectedButNoneEnabled -> NoChannelsSelectedState(
                            onOpenChannelSettings = { showChannelSheet = true },
                            modifier = Modifier.fillMaxSize(),
                        )
                        state.messages.isEmpty() && state.selectedChannelId == null -> EmptyInboxState(modifier = Modifier.fillMaxSize())
                        state.messages.isEmpty() -> NoMessagesForFilterState(modifier = Modifier.fillMaxSize())
                        else -> LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(state.messages, key = { it.id }) { message ->
                                MessageCard(
                                    message = message,
                                    onMarkRead = { viewModel.markRead(message) },
                                    onOpenSource = { viewModel.openSource(message) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showChannelSheet) {
        val channels by channelSettingsViewModel.channels.collectAsState()
        val exportedJson by channelSettingsViewModel.exportedJson.collectAsState()
        val exportShareTitle = stringResource(R.string.export_share_title)

        LaunchedEffect(exportedJson) {
            val json = exportedJson ?: return@LaunchedEffect
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_TEXT, json)
            }
            context.startActivity(Intent.createChooser(sendIntent, exportShareTitle))
            channelSettingsViewModel.consumeExport()
        }

        ChannelSettingsSheet(
            channels = channels,
            diagnosticsUnlocked = diagnosticsUnlocked,
            onToggleChannel = channelSettingsViewModel::setChannelEnabled,
            onEnableAll = channelSettingsViewModel::enableAll,
            onDisableAll = channelSettingsViewModel::disableAll,
            onExport = channelSettingsViewModel::requestExport,
            onEraseAllRequested = { showEraseConfirm = true },
            onOpenDiagnostics = {
                context.startActivity(Intent(context, com.tekpanel.app.ui.diagnostics.DiagnosticsActivity::class.java))
            },
            onDismiss = { showChannelSheet = false },
        )
    }

    if (showClearConfirm) {
        ConfirmDialog(
            title = stringResource(R.string.clear_screen_confirm_title),
            message = stringResource(R.string.clear_screen_confirm_message),
            confirmLabel = stringResource(R.string.action_clear_screen),
            dismissLabel = stringResource(R.string.action_cancel),
            onConfirm = {
                showClearConfirm = false
                viewModel.clearActive(context.getString(R.string.clear_screen_undo_label))
            },
            onDismiss = { showClearConfirm = false },
        )
    }

    if (showEraseConfirm) {
        ConfirmDialog(
            title = stringResource(R.string.erase_all_confirm_title),
            message = stringResource(R.string.erase_all_confirm_message),
            confirmLabel = stringResource(R.string.action_erase_all_data),
            dismissLabel = stringResource(R.string.action_cancel),
            onConfirm = {
                showEraseConfirm = false
                showChannelSheet = false
                channelSettingsViewModel.eraseAllData()
            },
            onDismiss = { showEraseConfirm = false },
        )
    }
}
