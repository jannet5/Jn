package com.cepgozcu.app.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cepgozcu.app.R

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.state_loading), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun EmptyState(message: String = stringResource(R.string.state_empty), modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Filled.Inbox, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ErrorState(message: String = stringResource(R.string.state_error), onRetry: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Filled.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(12.dp))
        Text(message, color = MaterialTheme.colorScheme.onSurface)
        if (onRetry != null) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) { Text(stringResource(R.string.state_retry)) }
        }
    }
}

@Composable
fun OfflineState(onRetry: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Filled.CloudOff, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.status_offline), color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(R.string.state_offline_message),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
        if (onRetry != null) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) { Text(stringResource(R.string.state_retry)) }
        }
    }
}

@Composable
fun UnauthorizedState(onReauthorize: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Filled.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.status_unauthorized), color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(R.string.state_unauthorized_message),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onReauthorize) { Text(stringResource(R.string.unauthorized_banner_action)) }
    }
}

/** Renders the right explicit state for every screen so nothing ever falls through to a blank/crashing view. */
@Composable
fun <T> StateHost(
    state: UiState<T>,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    onReauthorize: (() -> Unit)? = null,
    emptyMessage: String = stringResource(R.string.state_empty),
    content: @Composable (T) -> Unit,
) {
    when (state) {
        is UiState.Loading -> LoadingState(modifier)
        is UiState.Content -> content(state.data)
        is UiState.Empty -> EmptyState(emptyMessage, modifier)
        is UiState.Error -> ErrorState(state.message, onRetry, modifier)
        is UiState.Offline -> OfflineState(onRetry, modifier)
        is UiState.Unauthorized -> UnauthorizedState(onReauthorize ?: {}, modifier)
    }
}
