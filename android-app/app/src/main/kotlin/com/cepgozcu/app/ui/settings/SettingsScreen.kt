package com.cepgozcu.app.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cepgozcu.app.BuildConfig
import com.cepgozcu.app.R
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.ui.common.ConnectionStatusIndicator
import com.cepgozcu.app.ui.common.localApp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(connectionSource: ConnectionSource, onUnpaired: () -> Unit, onOpenAudit: () -> Unit) {
    val app = localApp()
    val context = LocalContext.current
    val viewModel: SettingsViewModel = viewModel(
        factory = viewModelFactory { initializer { SettingsViewModel(app.connectionRepository) } },
    )
    val connState by connectionSource.connectionState.collectAsState()
    val session = viewModel.session
    var showUnpairConfirm by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                actions = { ConnectionStatusIndicator(connState) },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(stringResource(R.string.settings_connection_section), style = MaterialTheme.typography.titleMedium)
            Card(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column {
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.settings_paired_host)) },
                        supportingContent = { Text(session?.host?.let { "$it:${session.port}" } ?: "—") },
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.settings_paired_device_id)) },
                        supportingContent = { Text(session?.deviceId ?: "—") },
                    )
                    Divider()
                    ListItem(
                        headlineContent = { Text(stringResource(R.string.audit_title)) },
                        modifier = Modifier.clickableListItem(onOpenAudit),
                    )
                }
            }

            OutlinedButton(onClick = { showUnpairConfirm = true }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text(stringResource(R.string.settings_unpair))
            }

            Text(stringResource(R.string.settings_about), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 24.dp))
            Card(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleSmall)
                    Text(
                        stringResource(R.string.settings_app_version, appVersionName(context)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        stringResource(R.string.settings_about_body),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }
    }

    if (showUnpairConfirm) {
        AlertDialog(
            onDismissRequest = { showUnpairConfirm = false },
            title = { Text(stringResource(R.string.settings_unpair)) },
            text = { Text(stringResource(R.string.settings_unpair_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    showUnpairConfirm = false
                    viewModel.unpair { onUnpaired() }
                }) { Text(stringResource(R.string.settings_unpair)) }
            },
            dismissButton = {
                TextButton(onClick = { showUnpairConfirm = false }) { Text(stringResource(R.string.action_cancel)) }
            },
        )
    }
}

private fun appVersionName(context: android.content.Context): String = runCatching {
    context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: BuildConfig.VERSION_NAME
}.getOrDefault(BuildConfig.VERSION_NAME)

@Composable
private fun Modifier.clickableListItem(onClick: () -> Unit): Modifier =
    this.clickable(onClick = onClick)
