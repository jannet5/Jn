package com.jn.winremote.ui.alerts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.winremote.protocol.AlertData
import com.jn.winremote.protocol.AlertKind
import com.jn.winremote.protocol.AlertSeverity
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.ui.components.EmptyState
import com.jn.winremote.ui.components.ErrorState
import com.jn.winremote.ui.components.LoadingState
import com.jn.winremote.ui.components.OfflineState
import com.jn.winremote.ui.components.ReconnectingBanner
import com.jn.winremote.ui.components.UnauthorizedState
import com.jn.winremote.ui.theme.AmberWarning
import com.jn.winremote.ui.theme.RedCritical
import com.jn.winremote.util.AlertText
import com.jn.winremote.util.Formatting
import com.jn.winremote.util.ReasonText

private val kindLabels = mapOf(
    AlertKind.LARGE_FILE to "Büyük dosya",
    AlertKind.FAST_GROWTH to "Hızlı büyüme",
    AlertKind.DISK_FILL_RATE to "Disk dolma hızı",
    AlertKind.LOW_FREE_SPACE to "Az boş alan",
)

@Composable
fun AlertsScreen(
    onRePair: () -> Unit,
    viewModel: AlertsViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            AnimatedVisibility(visible = state.banner != null) {
                state.banner?.let { alert -> AlertBanner(alert, onDismiss = viewModel::dismissBanner) }
            }
            when (val status = state.connectionStatus) {
                is ConnectionStatus.Offline -> OfflineState(onAction = onRePair)
                is ConnectionStatus.Unauthorized -> UnauthorizedState(
                    reasonMessage = ReasonText.forCode(status.reasonCode),
                    onRePair = onRePair,
                )
                else -> {
                    if (status is ConnectionStatus.Reconnecting) ReconnectingBanner(status.attempt)
                    when (val body = state.body) {
                        is AlertsBody.Loading -> LoadingState(label = "Uyarılar yükleniyor…")
                        is AlertsBody.Error -> ErrorState(message = body.message, onRetry = viewModel::refresh)
                        is AlertsBody.Content -> {
                            if (body.items.isEmpty()) {
                                EmptyState(title = "Uyarı yok", message = "Şu anda aktif bir uyarı bulunmuyor.")
                            } else {
                                LazyColumn(
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    items(body.items, key = { it.id }) { alert -> AlertRow(alert) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun severityColor(severity: String) =
    if (severity == AlertSeverity.CRITICAL) RedCritical else AmberWarning

@Composable
private fun AlertBanner(alert: AlertData, onDismiss: () -> Unit) {
    val color = severityColor(alert.severity)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Yeni uyarı: ${kindLabels[alert.kind] ?: alert.kind}",
                style = MaterialTheme.typography.titleMedium,
                color = color,
            )
            Text(AlertText.describe(alert), style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = onDismiss) {
            androidx.compose.material3.Icon(Icons.Filled.Close, contentDescription = "Kapat")
        }
    }
}

@Composable
private fun AlertRow(alert: AlertData) {
    val color = severityColor(alert.severity)
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(color, CircleShape),
            )
            androidx.compose.foundation.layout.Spacer(Modifier.size(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(kindLabels[alert.kind] ?: alert.kind, style = MaterialTheme.typography.titleMedium)
                    Text(Formatting.timestamp(alert.ts), style = MaterialTheme.typography.labelSmall)
                }
                Text(AlertText.describe(alert), style = MaterialTheme.typography.bodyMedium)
                Text(
                    if (alert.severity == AlertSeverity.CRITICAL) "Kritik" else "Uyarı",
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                )
            }
        }
    }
}
