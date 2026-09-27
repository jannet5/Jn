package com.cepgozcu.app.ui.disk

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.cepgozcu.app.R
import com.cepgozcu.app.net.protocol.FileEventKind

fun FileEventKind.iconFor(): ImageVector = when (this) {
    FileEventKind.Created -> Icons.Filled.Add
    FileEventKind.Grew -> Icons.Filled.TrendingUp
    FileEventKind.Shrank -> Icons.Filled.TrendingDown
    FileEventKind.Modified -> Icons.Filled.Edit
    FileEventKind.Deleted -> Icons.Filled.Delete
    FileEventKind.Renamed -> Icons.Filled.DriveFileRenameOutline
    FileEventKind.Moved -> Icons.Filled.DriveFileMove
}

@Composable
fun FileEventKind.label(): String = when (this) {
    FileEventKind.Created -> stringResource(R.string.disk_event_created)
    FileEventKind.Grew -> stringResource(R.string.disk_event_grew)
    FileEventKind.Shrank -> stringResource(R.string.disk_event_shrank)
    FileEventKind.Modified -> stringResource(R.string.disk_event_modified)
    FileEventKind.Deleted -> stringResource(R.string.disk_event_deleted)
    FileEventKind.Renamed -> stringResource(R.string.disk_event_renamed)
    FileEventKind.Moved -> stringResource(R.string.disk_event_moved)
}

@Composable
fun growthRangeLabel(range: com.cepgozcu.app.net.protocol.GrowthRange): String = when (range) {
    com.cepgozcu.app.net.protocol.GrowthRange.LastHour -> stringResource(R.string.disk_range_hour)
    com.cepgozcu.app.net.protocol.GrowthRange.LastDay -> stringResource(R.string.disk_range_day)
    com.cepgozcu.app.net.protocol.GrowthRange.LastWeek -> stringResource(R.string.disk_range_week)
    com.cepgozcu.app.net.protocol.GrowthRange.LastMonth -> stringResource(R.string.disk_range_month)
    com.cepgozcu.app.net.protocol.GrowthRange.Custom -> ""
}
