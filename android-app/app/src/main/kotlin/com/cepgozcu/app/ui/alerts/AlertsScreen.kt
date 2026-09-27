package com.cepgozcu.app.ui.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cepgozcu.app.R
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.protocol.AlertDto
import com.cepgozcu.app.net.protocol.AlertKind
import com.cepgozcu.app.net.protocol.AlertSeverity
import com.cepgozcu.app.ui.common.ConnectionStatusIndicator
import com.cepgozcu.app.ui.common.StateHost
import com.cepgozcu.app.ui.common.formatRelativeTime
import com.cepgozcu.app.ui.common.localApp
import com.cepgozcu.app.ui.theme.SeverityCritical
import com.cepgozcu.app.ui.theme.SeverityInfo
import com.cepgozcu.app.ui.theme.SeverityWarning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(connectionSource: ConnectionSource, onReauthorize: () -> Unit) {
    val app = localApp()
    val viewModel: AlertsViewModel = viewModel(
        factory = viewModelFactory { initializer { AlertsViewModel(app.connectionRepository, app.database.alertDao()) } },
    )
    val state by viewModel.state.collectAsState()
    val connState by connectionSource.connectionState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.alerts_title)) },
                actions = { ConnectionStatusIndicator(connState) },
            )
        },
    ) { padding ->
        StateHost(
            state = state,
            modifier = Modifier.padding(padding),
            onRetry = viewModel::refresh,
            onReauthorize = onReauthorize,
            emptyMessage = stringResource(R.string.alerts_empty),
        ) { alerts ->
            LazyColumn(contentPadding = PaddingValues(16.dp), modifier = Modifier.padding(padding)) {
                items(alerts, key = { it.id }) { alert -> AlertRow(alert) }
            }
        }
    }
}

@Composable
private fun severityColor(severity: AlertSeverity): Color = when (severity) {
    AlertSeverity.Info -> SeverityInfo
    AlertSeverity.Warning -> SeverityWarning
    AlertSeverity.Critical -> SeverityCritical
}

@Composable
private fun severityLabel(severity: AlertSeverity): String = when (severity) {
    AlertSeverity.Info -> stringResource(R.string.alert_severity_info)
    AlertSeverity.Warning -> stringResource(R.string.alert_severity_warning)
    AlertSeverity.Critical -> stringResource(R.string.alert_severity_critical)
}

@Composable
private fun kindLabel(kind: AlertKind): String = when (kind) {
    AlertKind.BigFile -> stringResource(R.string.alert_kind_big_file)
    AlertKind.FastFolderGrowth -> stringResource(R.string.alert_kind_fast_folder_growth)
    AlertKind.LowFreeSpace -> stringResource(R.string.alert_kind_low_free_space)
    AlertKind.SuddenDiskDrop -> stringResource(R.string.alert_kind_sudden_disk_drop)
}

@Composable
private fun AlertRow(alert: AlertDto) {
    val color = severityColor(alert.severity)
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(Modifier.fillMaxWidth().padding(12.dp)) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
                    .align(androidx.compose.ui.Alignment.CenterVertically),
            )
            Column(Modifier.padding(start = 12.dp).weight(1f)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(severityLabel(alert.severity), style = MaterialTheme.typography.labelMedium, color = color)
                    Text(formatRelativeTime(alert.occurredAt), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(kindLabel(alert.kind), style = MaterialTheme.typography.titleSmall)
                Text(alert.message, style = MaterialTheme.typography.bodyMedium)
                alert.path?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                }
            }
        }
    }
}
