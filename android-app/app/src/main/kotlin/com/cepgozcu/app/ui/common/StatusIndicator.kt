package com.cepgozcu.app.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cepgozcu.app.R
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.ui.theme.SeverityCritical
import com.cepgozcu.app.ui.theme.SeverityWarning
import com.cepgozcu.app.ui.theme.SuccessGreen

/** Small colored dot + short label, used in every screen's top bar so the user always knows at a glance whether they're looking at live data. */
@Composable
fun ConnectionStatusIndicator(state: ConnectionState, modifier: Modifier = Modifier) {
    val (color, label) = when (state) {
        is ConnectionState.Connected -> SuccessGreen to stringResource(R.string.status_online)
        is ConnectionState.Connecting -> SeverityWarning to stringResource(R.string.status_connecting)
        is ConnectionState.Disconnected -> SeverityCritical to stringResource(R.string.status_offline)
        is ConnectionState.Unauthorized -> SeverityCritical to stringResource(R.string.status_unauthorized)
        ConnectionState.Idle -> MaterialTheme.colorScheme.onSurfaceVariant to stringResource(R.string.status_offline)
    }
    Row(modifier = modifier.padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Dot(color)
        androidx.compose.foundation.layout.Spacer(Modifier.size(6.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Dot(color: Color) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(8.dp)
            .background(color, CircleShape),
    )
}
