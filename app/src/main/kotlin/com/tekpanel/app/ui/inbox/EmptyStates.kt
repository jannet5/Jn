package com.tekpanel.app.ui.inbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tekpanel.app.R
import com.tekpanel.app.ui.theme.TekPanelColors

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = TekPanelColors.TextSecondary)
    }
}

/** CAP-01: the whole notification-access ask lives here, as this screen's empty state — not a separate onboarding flow. */
@Composable
fun NotificationAccessMissingState(onOpenSettings: () -> Unit, modifier: Modifier = Modifier) {
    EmptyStateScaffold(
        modifier = modifier,
        title = stringResource(R.string.onboarding_title),
        description = stringResource(R.string.onboarding_description),
        actionLabel = stringResource(R.string.onboarding_action),
        onAction = onOpenSettings,
    )
}

@Composable
fun NoChannelsInstalledState(modifier: Modifier = Modifier) {
    EmptyStateScaffold(
        modifier = modifier,
        title = stringResource(R.string.no_channels_installed_title),
        description = stringResource(R.string.no_channels_installed_description),
    )
}

@Composable
fun NoChannelsSelectedState(onOpenChannelSettings: () -> Unit, modifier: Modifier = Modifier) {
    EmptyStateScaffold(
        modifier = modifier,
        title = stringResource(R.string.no_channels_selected_title),
        description = stringResource(R.string.no_channels_selected_description),
        actionLabel = stringResource(R.string.action_open_channel_settings),
        onAction = onOpenChannelSettings,
    )
}

@Composable
fun EmptyInboxState(modifier: Modifier = Modifier) {
    EmptyStateScaffold(
        modifier = modifier,
        title = stringResource(R.string.empty_inbox_title),
        description = stringResource(R.string.empty_inbox_description),
    )
}

@Composable
fun NoMessagesForFilterState(modifier: Modifier = Modifier) {
    EmptyStateScaffold(
        modifier = modifier,
        title = stringResource(R.string.no_messages_for_filter_title),
        description = null,
    )
}

@Composable
private fun EmptyStateScaffold(
    title: String,
    description: String?,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = TekPanelColors.TextPrimary,
            textAlign = TextAlign.Center,
        )
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TekPanelColors.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
        if (actionLabel != null && onAction != null) {
            Button(
                onClick = onAction,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TekPanelColors.TextPrimary,
                    contentColor = TekPanelColors.Background,
                ),
                modifier = Modifier.padding(top = 22.dp),
            ) {
                Text(text = actionLabel)
            }
        }
    }
}
