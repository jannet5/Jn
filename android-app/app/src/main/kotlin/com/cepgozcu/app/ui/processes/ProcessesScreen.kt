package com.cepgozcu.app.ui.processes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cepgozcu.app.R
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.protocol.ProcessInfo
import com.cepgozcu.app.ui.common.ConnectionStatusIndicator
import com.cepgozcu.app.ui.common.StateHost
import com.cepgozcu.app.ui.common.formatBytes
import com.cepgozcu.app.ui.common.formatPercent
import com.cepgozcu.app.ui.common.localApp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessesScreen(connectionSource: ConnectionSource, onReauthorize: () -> Unit) {
    val app = localApp()
    val viewModel: ProcessesViewModel = viewModel(
        factory = viewModelFactory { initializer { ProcessesViewModel(app.connectionRepository) } },
    )
    val state by viewModel.visibleState.collectAsState()
    val query by viewModel.query.collectAsState()
    val sort by viewModel.sort.collectAsState()
    val connState by connectionSource.connectionState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var pendingKill by remember { mutableStateOf<ProcessInfo?>(null) }
    var sortMenuOpen by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.processes_title)) },
                actions = {
                    ConnectionStatusIndicator(connState)
                    IconButton(onClick = { sortMenuOpen = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(expanded = sortMenuOpen, onDismissRequest = { sortMenuOpen = false }) {
                        DropdownMenuItem(text = { Text(stringResource(R.string.processes_sort_cpu)) }, onClick = { viewModel.setSort(ProcessSort.CPU); sortMenuOpen = false })
                        DropdownMenuItem(text = { Text(stringResource(R.string.processes_sort_ram)) }, onClick = { viewModel.setSort(ProcessSort.RAM); sortMenuOpen = false })
                        DropdownMenuItem(text = { Text(stringResource(R.string.processes_sort_name)) }, onClick = { viewModel.setSort(ProcessSort.NAME); sortMenuOpen = false })
                    }
                },
            )
        },
    ) { padding ->
        Column(Modifier.padding(padding)) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::setQuery,
                placeholder = { Text(stringResource(R.string.processes_search_hint)) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            )
            StateHost(
                state = state,
                onRetry = viewModel::refresh,
                onReauthorize = onReauthorize,
                emptyMessage = if (query.isNotBlank()) stringResource(R.string.processes_search_empty) else stringResource(R.string.state_empty),
            ) { processes ->
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                    items(processes, key = { it.pid }) { process ->
                        ProcessRow(process, onClick = { if (process.canKill) pendingKill = process })
                    }
                }
            }
        }
    }

    pendingKill?.let { process ->
        val displayName = process.displayName ?: process.name
        AlertDialog(
            onDismissRequest = { pendingKill = null },
            title = { Text(stringResource(R.string.process_kill_confirm_title)) },
            text = { Text(stringResource(R.string.process_kill_confirm_message, displayName)) },
            confirmButton = {
                TextButton(onClick = {
                    pendingKill = null
                    viewModel.killProcess(process.pid) { result ->
                        val message = when {
                            result == null -> null
                            result.success -> app.getString(R.string.process_kill_success, displayName)
                            result.reason == "confirmation_required" -> app.getString(R.string.process_kill_reason_confirm)
                            result.reason == "protected_process" -> app.getString(R.string.process_kill_protected)
                            else -> app.getString(R.string.process_kill_reason_failed, result.reason.orEmpty())
                        }
                        if (message != null) scope.launch { snackbarHostState.showSnackbar(message) }
                    }
                }) { Text(stringResource(R.string.action_kill)) }
            },
            dismissButton = {
                TextButton(onClick = { pendingKill = null }) { Text(stringResource(R.string.action_cancel)) }
            },
        )
    }
}

@Composable
private fun ProcessRow(process: ProcessInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(process.displayName ?: process.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    "PID ${process.pid} · ${formatBytes(process.workingSetBytes)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (!process.canKill) {
                    Text(
                        stringResource(R.string.process_kill_protected),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            Text(formatPercent(process.cpuPercent), style = MaterialTheme.typography.titleMedium)
        }
    }
}
