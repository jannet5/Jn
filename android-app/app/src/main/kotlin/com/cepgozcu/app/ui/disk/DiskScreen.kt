package com.cepgozcu.app.ui.disk

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cepgozcu.app.R
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.protocol.BiggestFileEntry
import com.cepgozcu.app.net.protocol.DiskMetrics
import com.cepgozcu.app.net.protocol.FileEventDto
import com.cepgozcu.app.net.protocol.FileEventKind
import com.cepgozcu.app.net.protocol.FolderGrowthEntry
import com.cepgozcu.app.net.protocol.GrowthRange
import com.cepgozcu.app.ui.common.ConnectionStatusIndicator
import com.cepgozcu.app.ui.common.SizeTimelineChart
import com.cepgozcu.app.ui.common.StateHost
import com.cepgozcu.app.ui.common.UsageBar
import com.cepgozcu.app.ui.common.formatBytes
import com.cepgozcu.app.ui.common.formatBytesDelta
import com.cepgozcu.app.ui.common.formatBytesPerHour
import com.cepgozcu.app.ui.common.formatRelativeTime
import com.cepgozcu.app.ui.common.localApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiskScreen(connectionSource: ConnectionSource, onReauthorize: () -> Unit) {
    val connState by connectionSource.connectionState.collectAsState()
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.disk_tab_overview),
        stringResource(R.string.disk_tab_history),
        stringResource(R.string.disk_tab_growth),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.disk_title)) },
                actions = { ConnectionStatusIndicator(connState) },
            )
        },
    ) { padding ->
        Column(Modifier.padding(padding)) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index, onClick = { tabIndex = index }, text = { Text(title) })
                }
            }
            when (tabIndex) {
                0 -> DiskOverviewTab(connectionSource, onReauthorize)
                1 -> DiskHistoryTab(connectionSource, onReauthorize)
                else -> DiskGrowthTab(connectionSource, onReauthorize)
            }
        }
    }
}

@Composable
private fun DiskOverviewTab(connectionSource: ConnectionSource, onReauthorize: () -> Unit) {
    val app = localApp()
    val viewModel: DiskOverviewViewModel = viewModel(
        factory = viewModelFactory { initializer { DiskOverviewViewModel(app.connectionRepository) } },
    )
    val state by viewModel.state.collectAsState()

    StateHost(state = state, onRetry = viewModel::refresh, onReauthorize = onReauthorize) { overview ->
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item { Text(stringResource(R.string.disk_overview_title), style = MaterialTheme.typography.titleMedium) }
            item { Spacer(Modifier.height(8.dp)) }
            items(overview.disks) { disk -> OverviewDiskRow(disk) }
            item { Spacer(Modifier.height(16.dp)) }
            item { Text(stringResource(R.string.disk_watched_roots), style = MaterialTheme.typography.titleMedium) }
            item { Spacer(Modifier.height(8.dp)) }
            if (overview.watchedRoots.isEmpty()) {
                item { Text(stringResource(R.string.disk_no_watched_roots), color = MaterialTheme.colorScheme.onSurfaceVariant) }
            } else {
                items(overview.watchedRoots) { root ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(root.path, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun OverviewDiskRow(disk: DiskMetrics) {
    val percent = if (disk.totalBytes > 0) (disk.usedBytes.toFloat() / disk.totalBytes.toFloat()) * 100f else 0f
    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(disk.volumeLabel.ifBlank { disk.name }, style = MaterialTheme.typography.titleSmall)
                Text("${percent.toInt()}%")
            }
            Spacer(Modifier.height(6.dp))
            UsageBar(percent = percent)
            Spacer(Modifier.height(4.dp))
            Text(
                stringResource(R.string.dashboard_free_of, formatBytes(disk.freeBytes), formatBytes(disk.totalBytes)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DiskHistoryTab(connectionSource: ConnectionSource, onReauthorize: () -> Unit) {
    val app = localApp()
    val viewModel: DiskHistoryViewModel = viewModel(
        factory = viewModelFactory { initializer { DiskHistoryViewModel(app.connectionRepository, app.database.fileEventDao()) } },
    )
    val state by viewModel.state.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val canLoadMore by viewModel.canLoadMore.collectAsState()

    Column {
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            item {
                FilterChip(
                    selected = filter == null,
                    onClick = { viewModel.setFilter(null) },
                    label = { Text(stringResource(R.string.disk_event_filter_all)) },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
            items(FileEventKind.entries.toList()) { kind ->
                FilterChip(
                    selected = filter == kind,
                    onClick = { viewModel.setFilter(kind) },
                    label = { Text(kind.label()) },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }
        StateHost(
            state = state,
            onRetry = viewModel::refresh,
            onReauthorize = onReauthorize,
            emptyMessage = stringResource(R.string.disk_history_empty),
        ) { events ->
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                items(events, key = { it.id }) { event -> FileEventRow(event) }
                if (canLoadMore) {
                    item {
                        OutlinedButton(onClick = viewModel::loadMore, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Text(stringResource(R.string.disk_history_load_more))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FileEventRow(event: FileEventDto) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Icon(event.kind.iconFor(), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(event.path, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                Row {
                    Text(event.kind.label(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (event.sizeDeltaBytes != 0L) {
                        Text(" · ${formatBytesDelta(event.sizeDeltaBytes)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Text(formatRelativeTime(event.occurredAt), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiskGrowthTab(connectionSource: ConnectionSource, onReauthorize: () -> Unit) {
    val app = localApp()
    val overviewViewModel: DiskOverviewViewModel = viewModel(
        factory = viewModelFactory { initializer { DiskOverviewViewModel(app.connectionRepository) } },
    )
    val growthViewModel: DiskGrowthViewModel = viewModel(
        factory = viewModelFactory { initializer { DiskGrowthViewModel(app.connectionRepository) } },
    )
    val overviewState by overviewViewModel.state.collectAsState()
    val firstDiskName = (overviewState as? com.cepgozcu.app.ui.common.UiState.Content)?.data?.disks?.firstOrNull()?.name
    androidx.compose.runtime.LaunchedEffect(firstDiskName) {
        firstDiskName?.let { growthViewModel.setDefaultRootPathIfUnset(it) }
    }

    val state by growthViewModel.state.collectAsState()
    val range by growthViewModel.range.collectAsState()
    val ranges = listOf(GrowthRange.LastHour, GrowthRange.LastDay, GrowthRange.LastWeek, GrowthRange.LastMonth)

    Column {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            ranges.forEachIndexed { index, r ->
                SegmentedButton(
                    selected = range == r,
                    onClick = { growthViewModel.setRange(r) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = ranges.size),
                ) { Text(growthRangeLabel(r)) }
            }
        }
        StateHost(state = state, onRetry = growthViewModel::refresh, onReauthorize = onReauthorize, emptyMessage = stringResource(R.string.disk_growth_no_data)) { growth ->
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                item {
                    Text(
                        stringResource(R.string.disk_growth_question, growthRangeLabel(range)),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(Modifier.height(12.dp))
                    SizeTimelineChart(points = growth.timeline)
                    Spacer(Modifier.height(16.dp))
                }
                item { Text(stringResource(R.string.disk_top_growing), style = MaterialTheme.typography.titleSmall) }
                items(growth.topGrowingFolders) { FolderGrowthRow(it) }
                item { Spacer(Modifier.height(16.dp)) }
                item { Text(stringResource(R.string.disk_top_shrinking), style = MaterialTheme.typography.titleSmall) }
                items(growth.topShrinkingFolders) { FolderGrowthRow(it) }
                item { Spacer(Modifier.height(16.dp)) }
                item { Text(stringResource(R.string.disk_biggest_files), style = MaterialTheme.typography.titleSmall) }
                items(growth.biggestNewFiles) { BiggestFileRow(it) }
            }
        }
    }
}

@Composable
private fun FolderGrowthRow(entry: FolderGrowthEntry) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(entry.path, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(formatBytesDelta(entry.deltaBytes), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(stringResource(R.string.disk_growth_rate_per_hour, formatBytesPerHour(entry.bytesPerHour)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun BiggestFileRow(entry: BiggestFileEntry) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(entry.path, style = MaterialTheme.typography.bodyMedium, maxLines = 1, modifier = Modifier.weight(1f))
            Text(formatBytes(entry.sizeBytes), style = MaterialTheme.typography.bodySmall)
        }
    }
}
