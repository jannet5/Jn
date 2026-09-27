package com.jn.winremote.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.winremote.data.PairedDevice
import com.jn.winremote.ui.components.ConfirmDialog
import com.jn.winremote.ui.components.ConnectionStatusChip
import com.jn.winremote.ui.components.EmptyState

@Composable
fun SettingsScreen(
    onAddDevice: () -> Unit,
    viewModel: SettingsViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            Text("Bağlantı durumu", style = MaterialTheme.typography.titleMedium)
            ConnectionStatusChip(state.connectionStatus)
        }
        HorizontalDivider()
        if (state.devices.isEmpty()) {
                EmptyState(
                    title = "Eşleştirilmiş bilgisayar yok",
                    message = "Bir Windows bilgisayarı eşleştirmek için aşağıdaki butonu kullanın.",
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        Text(
                            "Eşleştirilmiş bilgisayarlar",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    items(state.devices, key = { it.deviceId }) { device ->
                        DeviceRow(
                            device = device,
                            isActive = device.deviceId == state.activeDeviceId,
                            onSelect = { viewModel.switchActive(device.deviceId) },
                            onRemove = { viewModel.requestRemove(device.deviceId) },
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onAddDevice, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(start = 8.dp))
                    Text("Yeni cihaz eşleştir")
                }
                OutlinedButton(onClick = viewModel::reconnectActive, modifier = Modifier.fillMaxWidth()) {
                    Text("Yeniden bağlan")
                }
                OutlinedButton(onClick = viewModel::disconnect, modifier = Modifier.fillMaxWidth()) {
                    Text("Bağlantıyı kes")
                }
            }
        }
    }

    val pendingId = state.pendingRemoveDeviceId
    if (pendingId != null) {
        val target = state.devices.firstOrNull { it.deviceId == pendingId }
        ConfirmDialog(
            title = "Cihazı unut",
            message = "\"${target?.deviceName ?: pendingId}\" eşleştirmesi kaldırılsın mı? " +
                "Bu bilgisayara yeniden bağlanmak için tekrar eşleştirme yapmanız gerekecek.",
            confirmLabel = "Kaldır",
            destructive = true,
            onConfirm = viewModel::confirmRemove,
            onDismiss = viewModel::cancelRemove,
        )
    }
}

@Composable
private fun DeviceRow(
    device: PairedDevice,
    isActive: Boolean,
    onSelect: () -> Unit,
    onRemove: () -> Unit,
) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            RadioButton(selected = isActive, onClick = onSelect)
            Column(modifier = Modifier.weight(1f)) {
                Text(device.deviceName, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${device.host}:${device.port}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "Parmak izi: ${device.pinnedFingerprintHex.take(16)}…",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (isActive) {
                    AssistChip(onClick = {}, label = { Text("Etkin", style = MaterialTheme.typography.labelSmall) })
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Cihazı unut", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
