package com.tekpanel.app.ui.access

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tekpanel.app.R
import com.tekpanel.app.ui.theme.Tek

/**
 * Shown on top of the (otherwise normal) screen while notification access is missing. It never
 * blocks the app: one short reason, one button that jumps straight to the right Android screen,
 * and a "how" link for the visual guide.
 */
@Composable
fun AccessBanner(onOpenSettings: () -> Unit, onHowTo: () -> Unit, modifier: Modifier = Modifier) {
    val colors = Tek.colors
    Surface(
        shape = RoundedCornerShape(Tek.shapes.card),
        color = colors.surface,
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.5f)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(stringResource(R.string.access_title), style = MaterialTheme.typography.titleSmall, color = colors.text)
            Text(stringResource(R.string.access_body), style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
            Row(Modifier.padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onOpenSettings,
                    shape = RoundedCornerShape(Tek.shapes.button),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.accent, contentColor = colors.onAccent),
                ) { Text(stringResource(R.string.access_open)) }
                TextButton(onClick = onHowTo) { Text(stringResource(R.string.access_howto), color = colors.text) }
            }
        }
    }
}

/** Access is on, but the phone stopped the background listener: restart it, and point to battery settings. */
@Composable
fun StalledBanner(onRestart: () -> Unit, onBatterySettings: () -> Unit, modifier: Modifier = Modifier, oemHint: String? = null) {
    val colors = Tek.colors
    Surface(
        shape = RoundedCornerShape(Tek.shapes.card),
        color = colors.surface,
        border = BorderStroke(1.dp, colors.danger.copy(alpha = 0.6f)),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(stringResource(R.string.stalled_title), style = MaterialTheme.typography.titleSmall, color = colors.text)
            Text(stringResource(R.string.stalled_body), style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
            if (oemHint != null) {
                Text(oemHint, style = MaterialTheme.typography.bodySmall, color = colors.text, modifier = Modifier.padding(top = 4.dp))
            }
            Row(Modifier.padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onRestart,
                    shape = RoundedCornerShape(Tek.shapes.button),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.accent, contentColor = colors.onAccent),
                ) { Text(stringResource(R.string.stalled_restart)) }
                TextButton(onClick = onBatterySettings) { Text(stringResource(R.string.stalled_battery), color = colors.text) }
            }
        }
    }
}
