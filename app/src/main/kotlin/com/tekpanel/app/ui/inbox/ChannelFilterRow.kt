package com.tekpanel.app.ui.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.tekpanel.app.R
import com.tekpanel.app.data.repository.ChannelUiEntry
import com.tekpanel.app.ui.theme.TekPanelColors

/**
 * The pill row from spec CAP-11: "Tümü" always first, then every installed+enabled channel.
 * Selection is shown with an outline + a filled dot in the channel's accent color, never a
 * solid fill block (spec 3.4: the earlier solid-fill "Tümü" pill read as visually too heavy).
 */
@Composable
fun ChannelFilterRow(
    channels: List<ChannelUiEntry>,
    selectedChannelId: String?,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val enabledChannels = channels.filter { it.enabled }
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        item {
            FilterPill(
                label = stringResource(R.string.filter_all),
                isSelected = selectedChannelId == null,
                accentColor = TekPanelColors.TextPrimary,
                onClick = { onSelect(null) },
            )
        }
        items(enabledChannels, key = { it.app.id }) { entry ->
            FilterPill(
                label = stringResource(entry.app.displayNameRes),
                isSelected = selectedChannelId == entry.app.id,
                accentColor = entry.app.accentColor,
                logoRes = entry.app.logoRes,
                onClick = { onSelect(entry.app.id) },
            )
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    logoRes: Int? = null,
) {
    val borderColor = if (isSelected) TekPanelColors.TextPrimary else TekPanelColors.Outline
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(width = if (isSelected) 1.5.dp else 1.dp, color = borderColor, shape = RoundedCornerShape(20.dp))
            .background(if (isSelected) TekPanelColors.SurfaceRaised else TekPanelColors.Background)
            .clickable { onClick() }
            .semantics { this.selected = isSelected; contentDescription = label }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (logoRes != null) {
            Image(painter = painterResource(logoRes), contentDescription = null, modifier = Modifier.size(16.dp))
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(accentColor),
            )
        }
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = TekPanelColors.TextPrimary)
    }
}
