package com.jn.winremote.ui.fileactivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.winremote.protocol.FileEventData
import com.jn.winremote.protocol.FileOp
import com.jn.winremote.protocol.TopGrowthItem
import com.jn.winremote.repository.ConnectionStatus
import com.jn.winremote.ui.components.EmptyState
import com.jn.winremote.ui.components.ErrorState
import com.jn.winremote.ui.components.LoadingState
import com.jn.winremote.ui.components.OfflineState
import com.jn.winremote.ui.components.ReconnectingBanner
import com.jn.winremote.ui.components.UnauthorizedState
import com.jn.winremote.util.Formatting
import com.jn.winremote.util.ReasonText
import com.jn.winremote.util.applyFilter

private val operationLabels = mapOf(
    FileOp.CREATED to "Oluşturuldu",
    FileOp.MODIFIED to "Değiştirildi",
    FileOp.DELETED to "Silindi",
    FileOp.RENAMED to "Yeniden adlandırıldı",
    FileOp.MOVED to "Taşındı",
)

@Composable
fun FileActivityScreen(
    onRePair: () -> Unit,
    viewModel: FileActivityViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val status = state.connectionStatus) {
                is ConnectionStatus.Offline -> OfflineState(onAction = onRePair)
                is ConnectionStatus.Unauthorized -> UnauthorizedState(
                    reasonMessage = ReasonText.forCode(status.reasonCode),
                    onRePair = onRePair,
                )
                else -> {
                    if (status is ConnectionStatus.Reconnecting) ReconnectingBanner(status.attempt)
                    TabRow(selectedTabIndex = state.tabIndex) {
                        Tab(
                            selected = state.tabIndex == 0,
                            onClick = { viewModel.selectTab(0) },
                            text = { Text("Zaman Akışı") },
                        )
                        Tab(
                            selected = state.tabIndex == 1,
                            onClick = { viewModel.selectTab(1) },
                            text = { Text("Diski Dolduran") },
                        )
                    }
                    if (state.tabIndex == 0) TimelineTab(state, viewModel) else GrowthTab(state, viewModel)
                }
            }
        }
    }
}

@Composable
private fun TimelineTab(state: FileActivityUiState, viewModel: FileActivityViewModel) {
    var showFilters by remember { mutableStateOf(false) }
    val filtered = remember(state.events, state.filter) { state.events.applyFilter(state.filter) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("${filtered.size} olay", style = MaterialTheme.typography.labelLarge)
            FilterChip(
                selected = showFilters,
                onClick = { showFilters = !showFilters },
                label = { Text("Filtreler") },
            )
        }
        if (showFilters) {
            FilterPanel(state, viewModel)
        }
        when (state.eventsLoad) {
            is LoadState.Loading -> LoadingState(label = "Dosya etkinliği yükleniyor…")
            is LoadState.Error -> ErrorState(
                message = (state.eventsLoad as LoadState.Error).message,
                onRetry = viewModel::loadInitialEvents,
            )
            is LoadState.Content -> {
                if (filtered.isEmpty()) {
                    EmptyState(
                        title = "Olay bulunamadı",
                        message = "Seçili filtrelerle eşleşen bir dosya etkinliği yok.",
                    )
                } else {
                    LazyColumn(
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(filtered, key = { "${it.ts}-${it.path}-${it.op}" }) { event -> FileEventRow(event) }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterPanel(state: FileActivityUiState, viewModel: FileActivityViewModel) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedTextField(
            value = state.filter.pathPrefix ?: "",
            onValueChange = { v -> viewModel.updateFilter { it.copy(pathPrefix = v.ifBlank { null }) } },
            label = { Text("Yol öneki (örn. C:\\Users\\jake\\Downloads)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = state.filter.op == null,
                onClick = { viewModel.updateFilter { it.copy(op = null) } },
                label = { Text("Tümü") },
            )
            operationLabels.forEach { (opValue, label) ->
                FilterChip(
                    selected = state.filter.op == opValue,
                    onClick = { viewModel.updateFilter { it.copy(op = opValue) } },
                    label = { Text(label) },
                )
            }
        }
        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 8.dp))
        OutlinedTextField(
            value = if (state.filter.minSizeBytes > 0) (state.filter.minSizeBytes / (1024 * 1024)).toString() else "",
            onValueChange = { v ->
                val mb = v.filter { it.isDigit() }.toLongOrNull() ?: 0L
                viewModel.updateFilter { it.copy(minSizeBytes = mb * 1024 * 1024) }
            },
            label = { Text("Minimum boyut (MB)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun FileEventRow(event: FileEventData) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    operationLabels[event.op] ?: event.op,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(Formatting.timestamp(event.ts), style = MaterialTheme.typography.labelSmall)
            }
            Text(event.path, style = MaterialTheme.typography.bodyMedium)
            if (event.oldPath != null) {
                Text("Eski: ${event.oldPath}", style = MaterialTheme.typography.bodyMedium)
            }
            if (!event.isDir) {
                Text(
                    Formatting.bytes(event.sizeBytes),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private val growthWindows = listOf("1h" to "1 saat", "24h" to "24 saat", "7d" to "7 gün")

@Composable
private fun GrowthTab(state: FileActivityUiState, viewModel: FileActivityViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            growthWindows.forEach { (value, label) ->
                FilterChip(
                    selected = state.growthWindow == value,
                    onClick = { viewModel.setGrowthWindow(value) },
                    label = { Text(label) },
                )
            }
        }
        when (val load = state.growthLoad) {
            is LoadState.Loading -> LoadingState(label = "Hesaplanıyor…")
            is LoadState.Error -> ErrorState(message = load.message, onRetry = viewModel::loadGrowth)
            is LoadState.Content -> {
                if (load.value.isEmpty()) {
                    EmptyState(
                        title = "Büyüme verisi yok",
                        message = "Bu zaman aralığında dikkat çekici bir disk büyümesi görülmedi.",
                    )
                } else {
                    LazyColumn(
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(load.value, key = { it.path }) { item -> GrowthRow(item) }
                    }
                }
            }
        }
    }
}

@Composable
private fun GrowthRow(item: TopGrowthItem) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(item.path, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Text(
                    Formatting.signedBytes(item.deltaBytes),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Text(
                "${if (item.isDir) "Klasör" else "Dosya"} · ${Formatting.bytes(item.sizeBefore)} → ${Formatting.bytes(item.sizeAfter)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
