package com.cepgozcu.app.ui.audit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cepgozcu.app.R
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.protocol.AuditEntry
import com.cepgozcu.app.ui.common.ConnectionStatusIndicator
import com.cepgozcu.app.ui.common.StateHost
import com.cepgozcu.app.ui.common.formatRelativeTime
import com.cepgozcu.app.ui.common.localApp
import com.cepgozcu.app.ui.theme.SeverityCritical
import com.cepgozcu.app.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditScreen(connectionSource: ConnectionSource, onBack: () -> Unit, onReauthorize: () -> Unit) {
    val app = localApp()
    val viewModel: AuditViewModel = viewModel(
        factory = viewModelFactory { initializer { AuditViewModel(app.connectionRepository, app.database.auditDao()) } },
    )
    val state by viewModel.state.collectAsState()
    val connState by connectionSource.connectionState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.audit_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back)) }
                },
                actions = { ConnectionStatusIndicator(connState) },
            )
        },
    ) { padding ->
        StateHost(
            state = state,
            modifier = Modifier.padding(padding),
            onRetry = viewModel::refresh,
            onReauthorize = onReauthorize,
            emptyMessage = stringResource(R.string.audit_empty),
        ) { entries ->
            LazyColumn(contentPadding = PaddingValues(16.dp), modifier = Modifier.padding(padding)) {
                items(entries, key = { it.id }) { entry -> AuditRow(entry) }
            }
        }
    }
}

@Composable
private fun AuditRow(entry: AuditEntry) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(entry.action, style = MaterialTheme.typography.titleSmall)
                Text(
                    if (entry.success) stringResource(R.string.audit_result_success) else stringResource(R.string.audit_result_failed),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (entry.success) SuccessGreen else SeverityCritical,
                )
            }
            entry.target?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            Text(entry.deviceName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            entry.reason?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Text(formatRelativeTime(entry.occurredAt), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
