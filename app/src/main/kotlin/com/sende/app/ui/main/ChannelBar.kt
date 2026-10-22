package com.sende.app.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sende.app.R
import com.sende.app.ui.common.AppIcon
import com.sende.app.ui.theme.Tek

/** Side-by-side channel bubbles: "Tümü" first, then the chosen apps with their real icons. */
@Composable
fun ChannelBar(
    items: List<BarItem>,
    selected: String?,
    totalUnread: Int,
    onSelect: (String?) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item(key = "all") {
            Bubble(
                label = stringResource(R.string.filter_all),
                selected = selected == null,
                unread = totalUnread,
                icon = null,
                onClick = { onSelect(null) },
            )
        }
        items(items, key = { it.packageName }) { item ->
            Bubble(
                label = item.label,
                selected = selected == item.packageName,
                unread = item.unread,
                icon = item.packageName,
                onClick = { onSelect(item.packageName) },
            )
        }
        item(key = "add") {
            Surface(
                onClick = onAdd,
                shape = CircleShape,
                color = Tek.colors.surface,
                border = BorderStroke(1.dp, Tek.colors.line),
                modifier = Modifier.minimumInteractiveComponentSize().size(40.dp),
            ) {
                androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.action_add_channel),
                        tint = Tek.colors.textSecondary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun Bubble(label: String, selected: Boolean, unread: Int, icon: String?, onClick: () -> Unit) {
    val colors = Tek.colors
    Surface(
        shape = RoundedCornerShape(Tek.shapes.bubble),
        color = if (selected) colors.accent.copy(alpha = 0.16f) else colors.surface,
        border = BorderStroke(1.dp, if (selected) colors.accent else colors.line),
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .height(40.dp)
            .clip(RoundedCornerShape(Tek.shapes.bubble))
            .clickable(role = Role.Tab, onClick = onClick)
            .semantics { this.selected = selected },
    ) {
        Row(
            modifier = Modifier.padding(start = if (icon != null) 8.dp else 14.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                AppIcon(packageName = icon, size = 24.dp)
                Spacer(Modifier.size(8.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) colors.text else colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 140.dp),
            )
            if (unread > 0) {
                Spacer(Modifier.size(8.dp))
                Surface(shape = CircleShape, color = colors.accent) {
                    Text(
                        text = if (unread > 99) "99+" else unread.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.onAccent,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                    )
                }
            }
        }
    }
}
