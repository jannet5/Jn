package com.sende.app.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sende.app.R
import com.sende.app.ui.theme.Tek

/** Brand name, the one-tap "Ekranı tertemiz yap" action, and the chevron that opens the menu. */
@Composable
fun TopBar(
    menuOpen: Boolean,
    canClear: Boolean,
    onTitleTap: () -> Unit,
    onClear: () -> Unit,
    onToggleMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = Tek.colors
    val rotation by animateFloatAsState(if (menuOpen) 180f else 0f, label = "chevron")
    Row(
        modifier = modifier.fillMaxWidth().statusBarsPadding().padding(start = 16.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            color = colors.text,
            // Natural width, measured first: at 200% text size the clear button wraps instead of squeezing the name.
            modifier = Modifier.clickable(onClick = onTitleTap),
            maxLines = 1,
            softWrap = false,
        )
        Spacer(Modifier.weight(1f))
        Surface(
            onClick = onClear,
            enabled = canClear,
            shape = RoundedCornerShape(Tek.shapes.button),
            color = colors.surface,
            border = BorderStroke(1.dp, if (canClear) colors.line else colors.line.copy(alpha = 0.5f)),
        ) {
            Row(
                modifier = Modifier.heightIn(min = 40.dp).padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Rounded.CleaningServices,
                    contentDescription = null,
                    tint = if (canClear) colors.accent else colors.textSecondary,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.action_clear_screen),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (canClear) colors.text else colors.textSecondary,
                    maxLines = 2,
                )
            }
        }
        IconButton(onClick = onToggleMenu) {
            Icon(
                Icons.Rounded.KeyboardArrowDown,
                contentDescription = stringResource(if (menuOpen) R.string.menu_close else R.string.menu_open),
                tint = colors.text,
                modifier = Modifier.rotate(rotation),
            )
        }
    }
}

/** The menu hides behind the chevron and slides open in place; nothing else lives on the main screen. */
@Composable
fun MenuPanel(
    open: Boolean,
    diagnosticsUnlocked: Boolean,
    onChannels: () -> Unit,
    onExport: () -> Unit,
    onPrivacy: () -> Unit,
    onErase: () -> Unit,
    onDiagnostics: () -> Unit,
    modifier: Modifier = Modifier,
    showSupport: Boolean = false,
    onSupport: () -> Unit = {},
) {
    AnimatedVisibility(
        visible = open,
        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
        modifier = modifier,
    ) {
        Surface(
            shape = RoundedCornerShape(Tek.shapes.card),
            color = Tek.colors.surface,
            border = BorderStroke(1.dp, Tek.colors.line),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        ) {
            Column(Modifier.padding(vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
                MenuItem(Icons.Rounded.Tune, stringResource(R.string.menu_channels), onChannels)
                MenuItem(Icons.Rounded.Lock, stringResource(R.string.menu_privacy), onPrivacy)
                if (showSupport) MenuItem(Icons.Rounded.Favorite, stringResource(R.string.menu_support), onSupport)
                MenuItem(Icons.Rounded.IosShare, stringResource(R.string.menu_export), onExport)
                MenuItem(Icons.Rounded.DeleteForever, stringResource(R.string.menu_erase), onErase, danger = true)
                if (diagnosticsUnlocked) MenuItem(Icons.Rounded.BugReport, stringResource(R.string.menu_diagnostics), onDiagnostics)
            }
        }
    }
}

@Composable
private fun MenuItem(icon: ImageVector, label: String, onClick: () -> Unit, danger: Boolean = false) {
    val tint = if (danger) Tek.colors.danger else Tek.colors.text
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .heightIn(min = 48.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = if (danger) tint else Tek.colors.textSecondary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium, color = tint, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}
