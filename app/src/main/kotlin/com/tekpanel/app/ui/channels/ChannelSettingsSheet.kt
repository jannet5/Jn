package com.tekpanel.app.ui.channels

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tekpanel.app.R
import com.tekpanel.app.data.repository.ChannelUiEntry
import com.tekpanel.app.ui.theme.TekPanelColors

@Composable
fun ChannelSettingsSheet(
    channels: List<ChannelUiEntry>,
    diagnosticsUnlocked: Boolean,
    onToggleChannel: (String, Boolean) -> Unit,
    onEnableAll: () -> Unit,
    onDisableAll: () -> Unit,
    onExport: () -> Unit,
    onEraseAllRequested: () -> Unit,
    onOpenDiagnostics: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = TekPanelColors.SurfaceRaised) {
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                text = stringResource(R.string.channel_settings_title),
                style = MaterialTheme.typography.titleLarge,
                color = TekPanelColors.TextPrimary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Text(
                    text = stringResource(R.string.action_enable_all_channels),
                    style = MaterialTheme.typography.labelLarge,
                    color = TekPanelColors.TextPrimary,
                    modifier = Modifier.clickable { onEnableAll() },
                )
                Text(
                    text = stringResource(R.string.action_disable_all_channels),
                    style = MaterialTheme.typography.labelLarge,
                    color = TekPanelColors.TextSecondary,
                    modifier = Modifier.clickable { onDisableAll() },
                )
            }

            if (channels.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_channels_installed_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TekPanelColors.TextSecondary,
                    modifier = Modifier.padding(20.dp),
                )
            } else {
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(channels, key = { it.app.id }) { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Image(
                                    painter = painterResource(entry.app.logoRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                )
                                Text(
                                    text = stringResource(entry.app.displayNameRes),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TekPanelColors.TextPrimary,
                                )
                            }
                            Switch(
                                checked = entry.enabled,
                                onCheckedChange = { onToggleChannel(entry.app.id, it) },
                                colors = SwitchDefaults.colors(checkedTrackColor = entry.app.accentColor),
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = TekPanelColors.Outline, modifier = Modifier.padding(vertical = 8.dp))

            SheetActionRow(label = stringResource(R.string.action_export_data), onClick = onExport)
            SheetActionRow(
                label = stringResource(R.string.action_erase_all_data),
                onClick = onEraseAllRequested,
                tint = TekPanelColors.Danger,
            )
            if (diagnosticsUnlocked) {
                SheetActionRow(label = stringResource(R.string.action_open_diagnostics), onClick = onOpenDiagnostics)
            }
        }
    }
}

@Composable
private fun SheetActionRow(label: String, onClick: () -> Unit, tint: androidx.compose.ui.graphics.Color = TekPanelColors.TextPrimary) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge,
        color = tint,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
    )
}
