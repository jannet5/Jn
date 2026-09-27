package com.jn.winremote.ui.processes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.winremote.protocol.ProcessItem
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.ui.components.ConfirmDialog
import com.jn.winremote.ui.components.EmptyState
import com.jn.winremote.ui.components.LoadingState
import com.jn.winremote.ui.components.OfflineState
import com.jn.winremote.ui.components.ReconnectingBanner
import com.jn.winremote.ui.components.UnauthorizedState
import com.jn.winremote.util.Formatting
import com.jn.winremote.util.ReasonText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessesScreen(
    onRePair: () -> Unit,
    viewModel: ProcessesViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.events.collect { message ->
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
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
                    OutlinedTextField(
                        value = state.query,
                        onValueChange = viewModel::onQueryChanged,
                        label = { Text("Süreç ara (ad)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                    )
                    when (val body = state.body) {
                        is ProcessesBody.Loading -> LoadingState(label = "Süreçler yükleniyor…")
                        is ProcessesBody.Error -> com.jn.winremote.ui.components.ErrorState(
                            message = body.message,
                            onRetry = viewModel::refreshNow,
                        )
                        is ProcessesBody.Content -> {
                            if (body.items.isEmpty()) {
                                EmptyState(
                                    title = "Süreç bulunamadı",
                                    message = if (state.query.isBlank()) "Şu anda listelenecek süreç yok."
                                    else "\"${state.query}\" ile eşleşen süreç yok.",
                                )
                            } else {
                                ProcessList(
                                    items = body.items,
                                    isLikelyProtected = viewModel::isLikelyProtected,
                                    onKillRequested = viewModel::requestKill,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    val pendingPid = state.pendingKillPid
    if (pendingPid != null) {
        val target = (state.body as? ProcessesBody.Content)?.items?.firstOrNull { it.pid == pendingPid }
        ConfirmDialog(
            title = "Süreci sonlandır",
            message = "\"${target?.name ?: "PID $pendingPid"}\" sonlandırılsın mı? Bu işlem geri alınamaz.",
            confirmLabel = "Sonlandır",
            destructive = true,
            onConfirm = viewModel::confirmKill,
            onDismiss = viewModel::cancelKill,
        )
    }
}

@Composable
private fun ProcessList(
    items: List<ProcessItem>,
    isLikelyProtected: (ProcessItem) -> Boolean,
    onKillRequested: (Int) -> Unit,
) {
    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items, key = { it.pid }) { item ->
            ProcessRow(item, isLikelyProtected(item), onKillRequested)
        }
    }
}

@Composable
private fun ProcessRow(item: ProcessItem, protectedHint: Boolean, onKillRequested: (Int) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                    if (protectedHint) {
                        androidx.compose.foundation.layout.Spacer(Modifier.padding(start = 6.dp))
                        AssistChip(onClick = {}, label = { Text("korumalı", style = MaterialTheme.typography.labelSmall) })
                    }
                }
                Text(
                    "PID ${item.pid} · ${item.user}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "CPU ${Formatting.percent(item.cpuPercent)} · RAM ${Formatting.bytes(item.ramBytes)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = { onKillRequested(item.pid) }, enabled = !protectedHint) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Süreci sonlandır",
                    tint = if (protectedHint) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
