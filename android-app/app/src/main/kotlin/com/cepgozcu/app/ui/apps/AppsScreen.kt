package com.cepgozcu.app.ui.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cepgozcu.app.R
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.protocol.AllowedApp
import com.cepgozcu.app.ui.common.ConnectionStatusIndicator
import com.cepgozcu.app.ui.common.StateHost
import com.cepgozcu.app.ui.common.localApp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(connectionSource: ConnectionSource, onBack: () -> Unit, onReauthorize: () -> Unit) {
    val app = localApp()
    val viewModel: AppsViewModel = viewModel(
        factory = viewModelFactory { initializer { AppsViewModel(app.connectionRepository) } },
    )
    val state by viewModel.state.collectAsState()
    val connState by connectionSource.connectionState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.apps_title)) },
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
            emptyMessage = stringResource(R.string.apps_empty),
        ) { apps ->
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(padding),
            ) {
                items(apps, key = { it.id }) { appEntry ->
                    AppTile(appEntry) {
                        viewModel.launchApp(appEntry.id) { result ->
                            val message = when {
                                result == null -> app.getString(R.string.app_launch_failed, appEntry.displayName)
                                result.success -> app.getString(R.string.app_launch_success, appEntry.displayName)
                                else -> app.getString(R.string.app_launch_failed, appEntry.displayName)
                            }
                            scope.launch { snackbarHostState.showSnackbar(message) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppTile(appEntry: AllowedApp, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp), onClick = onClick) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(Icons.Filled.Apps, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
            Text(appEntry.displayName, style = MaterialTheme.typography.titleSmall, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            androidx.compose.foundation.layout.Spacer(Modifier.padding(4.dp))
            Icon(Icons.Filled.PlayArrow, contentDescription = stringResource(R.string.action_launch), tint = MaterialTheme.colorScheme.secondary)
        }
    }
}
