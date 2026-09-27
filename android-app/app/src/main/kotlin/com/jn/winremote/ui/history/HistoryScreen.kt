package com.jn.winremote.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.winremote.protocol.HistoryAction
import com.jn.winremote.protocol.HistoryItem
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.ui.components.EmptyState
import com.jn.winremote.ui.components.ErrorState
import com.jn.winremote.ui.components.LoadingState
import com.jn.winremote.ui.components.OfflineState
import com.jn.winremote.ui.components.ReconnectingBanner
import com.jn.winremote.ui.components.UnauthorizedState
import com.jn.winremote.ui.theme.GreenOk
import com.jn.winremote.ui.theme.RedCritical
import com.jn.winremote.util.Formatting
import com.jn.winremote.util.ReasonText

private val actionLabels = mapOf(
    HistoryAction.KILL_PROCESS to "Süreç sonlandırma",
    HistoryAction.LAUNCH_APP to "Uygulama başlatma",
    HistoryAction.PAIR to "Eşleştirme",
    HistoryAction.UNPAIR to "Eşleştirmeyi kaldırma",
)

@Composable
fun HistoryScreen(
    onRePair: () -> Unit,
    viewModel: HistoryViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold { padding ->
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
                        is HistoryBody.Loading -> LoadingState(label = "Geçmiş yükleniyor…")
                        is HistoryBody.Error -> ErrorState(message = body.message, onRetry = viewModel::refresh)
                        is HistoryBody.Content -> {
                            if (body.items.isEmpty()) {
                                EmptyState(title = "Geçmiş boş", message = "Henüz kayıtlı bir işlem yok.")
                            } else {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    LazyColumn(
                                        modifier = Modifier.weight(1f),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        items(body.items, key = { "${it.ts}-${it.deviceId}-${it.action}" }) { item ->
                                            HistoryRow(item)
                                        }
                                        if (body.items.size < body.total) {
                                            item {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.Center,
                                                ) {
                                                    TextButton(onClick = viewModel::loadMore, enabled = !state.isLoadingMore) {
                                                        Text(if (state.isLoadingMore) "Yükleniyor…" else "Daha fazla yükle")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryRow(item: HistoryItem) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Icon(
                if (item.success) Icons.Filled.CheckCircle else Icons.Filled.Error,
                contentDescription = null,
                tint = if (item.success) GreenOk else RedCritical,
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(start = 10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(actionLabels[item.action] ?: item.action, style = MaterialTheme.typography.titleMedium)
                    Text(Formatting.timestamp(item.ts), style = MaterialTheme.typography.labelSmall)
                }
                Text(item.deviceName, style = MaterialTheme.typography.bodyMedium)
                if (item.detail != null) {
                    Text(
                        item.detail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (!item.success) {
                    Text(
                        ReasonText.forCode(item.reason),
                        style = MaterialTheme.typography.labelSmall,
                        color = RedCritical,
                    )
                }
            }
        }
    }
}
