package com.tekpanel.app.ui.inbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.tekpanel.app.R
import com.tekpanel.app.capture.CaptureCoordinator
import com.tekpanel.app.data.catalog.SourceAppCatalog
import com.tekpanel.app.domain.model.InboxMessage
import com.tekpanel.app.ui.theme.TekPanelColors
import com.tekpanel.app.util.TimeFormatter

/**
 * A single message (spec CAP-10). No package name, no "Bildirim" tag, no capture-method
 * text, and no `maxLines` clamp on the body: the whole point is that the full message is
 * readable without leaving TekPanel.
 */
@Composable
fun MessageCard(
    message: InboxMessage,
    onMarkRead: () -> Unit,
    onOpenSource: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val app = SourceAppCatalog.byId(message.channelId)
    val isShareCapture = message.channelId == CaptureCoordinator.SHARE_FALLBACK_CHANNEL_ID
    val accentColor = app?.accentColor ?: TekPanelColors.OutlineStrong
    val channelLabel = if (app != null) stringResource(app.displayNameRes) else stringResource(R.string.share_channel_label)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(TekPanelColors.Surface)
            .clickable(onClickLabel = stringResource(R.string.action_open_source)) { onOpenSource() },
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .background(accentColor),
        )
        Column(modifier = Modifier.padding(14.dp).weight(1f)) {
            Row(verticalAlignment = CenterVertically) {
                if (app != null) {
                    Image(
                        painter = painterResource(app.logoRes),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                } else if (isShareCapture) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = null,
                        tint = TekPanelColors.TextSecondary,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Text(
                    text = channelLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = TekPanelColors.TextSecondary,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }

            Text(
                text = message.senderName,
                style = MaterialTheme.typography.titleMedium,
                color = TekPanelColors.TextPrimary,
                modifier = Modifier.padding(top = 6.dp),
            )

            Text(
                text = message.messageText,
                style = MaterialTheme.typography.bodyLarge,
                color = TekPanelColors.TextPrimary,
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = TimeFormatter.relative(message.receivedAt).toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = TekPanelColors.TextTertiary,
                )
                MarkReadButton(onClick = onMarkRead)
            }
        }
    }
}

@Composable
private fun MarkReadButton(onClick: () -> Unit) {
    val label = stringResource(R.string.action_mark_read)
    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TekPanelColors.TextSecondary,
        )
    }
}
