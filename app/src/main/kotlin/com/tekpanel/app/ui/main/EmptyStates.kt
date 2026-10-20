package com.tekpanel.app.ui.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tekpanel.app.R
import com.tekpanel.app.ui.theme.Tek

/** Calm empty states: a drawn panel, one short sentence, at most one button. */
@Composable
fun EmptyInbox(modifier: Modifier = Modifier) {
    EmptyState(
        title = stringResource(R.string.empty_inbox_title),
        body = stringResource(R.string.empty_inbox_body),
        modifier = modifier,
    )
}

@Composable
fun EmptyForFilter(modifier: Modifier = Modifier) {
    EmptyState(title = stringResource(R.string.empty_filter_title), body = null, modifier = modifier)
}

@Composable
fun NoChannelsChosen(onPick: () -> Unit, modifier: Modifier = Modifier) {
    EmptyState(
        title = stringResource(R.string.no_channels_title),
        body = stringResource(R.string.no_channels_body),
        actionLabel = stringResource(R.string.menu_channels),
        onAction = onPick,
        modifier = modifier,
    )
}

@Composable
private fun EmptyState(
    title: String,
    body: String?,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val colors = Tek.colors
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val line = colors.line
        val accent = colors.accent
        Canvas(Modifier.size(width = 96.dp, height = 72.dp)) {
            val stroke = Stroke(width = 2.dp.toPx())
            drawRoundRect(line, Offset.Zero, Size(size.width, size.height), CornerRadius(18.dp.toPx()), stroke)
            drawRoundRect(line, Offset(16.dp.toPx(), 18.dp.toPx()), Size(size.width - 32.dp.toPx(), 8.dp.toPx()), CornerRadius(4.dp.toPx()))
            drawRoundRect(line, Offset(16.dp.toPx(), 34.dp.toPx()), Size(size.width * 0.5f, 8.dp.toPx()), CornerRadius(4.dp.toPx()))
            drawCircle(accent, radius = 5.dp.toPx(), center = Offset(size.width - 6.dp.toPx(), 6.dp.toPx()))
        }
        Spacer(Modifier.height(20.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = colors.text, textAlign = TextAlign.Center)
        if (body != null) {
            Spacer(Modifier.height(8.dp))
            Text(body, style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary, textAlign = TextAlign.Center)
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onAction,
                shape = RoundedCornerShape(Tek.shapes.button),
                colors = ButtonDefaults.buttonColors(containerColor = colors.accent, contentColor = colors.onAccent),
            ) { Text(actionLabel) }
        }
    }
}
