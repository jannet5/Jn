package com.cepgozcu.app.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cepgozcu.app.R
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.net.protocol.DiskMetrics
import com.cepgozcu.app.net.protocol.SystemMetrics
import com.cepgozcu.app.ui.common.ConnectionStatusIndicator
import com.cepgozcu.app.ui.common.RadialGauge
import com.cepgozcu.app.ui.common.StateHost
import com.cepgozcu.app.ui.common.UiState
import com.cepgozcu.app.ui.common.UsageBar
import com.cepgozcu.app.ui.common.formatBytes
import com.cepgozcu.app.ui.common.localApp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(connectionSource: ConnectionSource, onOpenApps: () -> Unit, onReauthorize: () -> Unit) {
    val app = localApp()
    val viewModel: DashboardViewModel = viewModel(
        factory = viewModelFactory { initializer { DashboardViewModel(app.connectionRepository) } },
    )
    val state by viewModel.state.collectAsState()
    val connState by connectionSource.connectionState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.dashboard_title)) },
                actions = {
                    ConnectionStatusIndicator(connState)
                    IconButton(onClick = onOpenApps) {
                        Icon(Icons.Filled.Apps, contentDescription = stringResource(R.string.dashboard_go_apps))
                    }
                },
            )
        },
    ) { padding ->
        StateHost(
            state = state,
            modifier = Modifier.padding(padding),
            onRetry = viewModel::refresh,
            onReauthorize = onReauthorize,
        ) { metrics ->
            DashboardContent(metrics, modifier = Modifier.padding(padding))
        }
    }
}

@Composable
private fun DashboardContent(metrics: SystemMetrics, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth(), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(metrics.machineName, style = MaterialTheme.typography.titleLarge)
                    Text(metrics.osVersion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${stringResource(R.string.dashboard_uptime)}: ${metrics.uptime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadialGauge(percent = metrics.cpu.totalPercent.toFloat(), label = stringResource(R.string.dashboard_cpu))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val ramPercent = if (metrics.memory.totalBytes > 0) {
                        (metrics.memory.usedBytes.toFloat() / metrics.memory.totalBytes.toFloat()) * 100f
                    } else 0f
                    RadialGauge(percent = ramPercent, label = stringResource(R.string.dashboard_ram))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "${formatBytes(metrics.memory.usedBytes)} / ${formatBytes(metrics.memory.totalBytes)}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))
        }
        item {
            Text(stringResource(R.string.dashboard_disks), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
        }
        items(metrics.disks) { disk -> DiskRow(disk) }
    }
}

@Composable
private fun DiskRow(disk: DiskMetrics) {
    val percent = if (disk.totalBytes > 0) (disk.usedBytes.toFloat() / disk.totalBytes.toFloat()) * 100f else 0f
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(disk.volumeLabel.ifBlank { disk.name }, style = MaterialTheme.typography.titleSmall)
                Text("${percent.toInt()}%", style = MaterialTheme.typography.titleSmall)
            }
            Spacer(Modifier.height(6.dp))
            UsageBar(percent = percent)
            Spacer(Modifier.height(4.dp))
            Text(
                stringResource(R.string.dashboard_free_of, formatBytes(disk.freeBytes), formatBytes(disk.totalBytes)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
