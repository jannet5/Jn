package com.jn.winremote.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.winremote.protocol.DiskInfo
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.ui.components.OfflineState
import com.jn.winremote.ui.components.ReconnectingBanner
import com.jn.winremote.ui.components.Sparkline
import com.jn.winremote.ui.components.UnauthorizedState
import com.jn.winremote.util.Formatting
import com.jn.winremote.util.ReasonText

@Composable
fun DashboardScreen(
    onRePair: () -> Unit,
    viewModel: DashboardViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val status = state.connectionStatus) {
        is ConnectionStatus.Offline -> OfflineState(onAction = onRePair)
        is ConnectionStatus.Unauthorized -> UnauthorizedState(
            reasonMessage = ReasonText.forCode(status.reasonCode),
            onRePair = onRePair,
        )
        else -> DashboardContent(state = state, status = status, onRetry = viewModel::retryConnect)
    }
}

@Composable
private fun DashboardContent(
    state: DashboardUiState,
    status: ConnectionStatus,
    onRetry: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (status is ConnectionStatus.Reconnecting) {
            ReconnectingBanner(attempt = status.attempt)
        }
        if (status is ConnectionStatus.Error) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                Text(
                    if (status.isCertMismatch) "sunucu kimliği doğrulanamadı" else "Hata: ${status.message}",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f),
                )
                androidx.compose.material3.TextButton(onClick = onRetry) { Text("Yeniden dene") }
            }
        }
        if (status is ConnectionStatus.Connecting && state.latest == null) {
            com.jn.winremote.ui.components.LoadingState(label = "Bağlanıyor…")
            return
        }
        val metrics = state.latest
        if (metrics == null) {
            com.jn.winremote.ui.components.EmptyState(
                title = "Henüz veri yok",
                message = "Bağlantı kurulduğunda anlık metrikler burada görünecek.",
            )
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MetricCard(
                title = "İşlemci (CPU)",
                valueLabel = Formatting.percent(metrics.cpuPercent),
                history = state.cpuHistory,
            )
            MetricCard(
                title = "Bellek (RAM)",
                valueLabel = "${Formatting.bytes(metrics.ram.usedBytes)} / ${Formatting.bytes(metrics.ram.totalBytes)}",
                history = state.ramHistory,
            )
            Text("Diskler", style = MaterialTheme.typography.titleMedium)
            state.disks.forEach { disk -> DiskBar(disk) }
            Text(
                "Son güncelleme: ${Formatting.timestamp(metrics.ts)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MetricCard(title: String, valueLabel: String, history: List<Float>) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(valueLabel, style = MaterialTheme.typography.titleMedium)
            }
            androidx.compose.foundation.layout.Spacer(Modifier.height(8.dp))
            Sparkline(values = history, minValue = 0f, maxValue = 100f)
        }
    }
}

@Composable
private fun DiskBar(disk: DiskInfo) {
    val usedFraction = if (disk.totalBytes > 0) (disk.usedBytes.toFloat() / disk.totalBytes.toFloat()) else 0f
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(disk.volume, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${Formatting.bytes(disk.usedBytes)} / ${Formatting.bytes(disk.totalBytes)}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            androidx.compose.foundation.layout.Spacer(Modifier.height(8.dp))
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(6.dp)),
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxWidth(usedFraction.coerceIn(0f, 1f))
                        .height(10.dp)
                        .background(
                            if (usedFraction > 0.9f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(6.dp),
                        ),
                )
            }
            androidx.compose.foundation.layout.Spacer(Modifier.height(4.dp))
            Text(
                "Boş: ${Formatting.bytes(disk.freeBytes)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
