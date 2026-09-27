package com.jn.winremote.ui.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.winremote.protocol.AllowedAppItem
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.ui.components.ConfirmDialog
import com.jn.winremote.ui.components.EmptyState
import com.jn.winremote.ui.components.ErrorState
import com.jn.winremote.ui.components.LoadingState
import com.jn.winremote.ui.components.OfflineState
import com.jn.winremote.ui.components.ReconnectingBanner
import com.jn.winremote.ui.components.UnauthorizedState
import com.jn.winremote.util.ReasonText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(
    onRePair: () -> Unit,
    viewModel: AppsViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.collect { message -> scope.launch { snackbarHostState.showSnackbar(message) } }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) { Snackbar(it) } }) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val status = state.connectionStatus) {
                is ConnectionStatus.Offline -> OfflineState(onAction = onRePair)
                is ConnectionStatus.Unauthorized -> UnauthorizedState(
                    reasonMessage = ReasonText.forCode(status.reasonCode),
                    onRePair = onRePair,
                )
                else -> {
                    if (status is ConnectionStatus.Reconnecting) ReconnectingBanner(status.attempt)
                    when (val body = state.body) {
                        is AppsBody.Loading -> LoadingState(label = "Uygulamalar yükleniyor…")
                        is AppsBody.Error -> ErrorState(message = body.message, onRetry = viewModel::refresh)
                        is AppsBody.Content -> {
                            if (body.items.isEmpty()) {
                                EmptyState(
                                    title = "İzin listesi boş",
                                    message = "Windows bilgisayarınızda henüz izin verilen bir uygulama yok. " +
                                        "Bunu PC'deki allowed_apps.json dosyasından ekleyebilirsiniz.",
                                )
                            } else {
                                AppsList(
                                    items = body.items,
                                    launchingAppId = state.launchingAppId,
                                    onLaunchRequested = viewModel::requestLaunch,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    val pendingAppId = state.pendingLaunchAppId
    if (pendingAppId != null) {
        val target = (state.body as? AppsBody.Content)?.items?.firstOrNull { it.appId == pendingAppId }
        ConfirmDialog(
            title = "Uygulamayı başlat",
            message = "\"${target?.label ?: pendingAppId}\" Windows bilgisayarında başlatılsın mı?",
            confirmLabel = "Başlat",
            onConfirm = viewModel::confirmLaunch,
            onDismiss = viewModel::cancelLaunch,
        )
    }
}

@Composable
private fun AppsList(
    items: List<AllowedAppItem>,
    launchingAppId: String?,
    onLaunchRequested: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items, key = { it.appId }) { app ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ) {
                    Text(app.label, style = MaterialTheme.typography.titleMedium)
                    Button(onClick = { onLaunchRequested(app.appId) }, enabled = launchingAppId != app.appId) {
                        if (launchingAppId == app.appId) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        } else {
                            Text("Başlat")
                        }
                    }
                }
            }
        }
    }
}
