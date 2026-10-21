package com.sende.app.ui.main

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sende.app.R
import com.sende.app.domain.model.Conversation
import com.sende.app.domain.model.InboxMessage
import com.sende.app.ui.common.AppIcon
import com.sende.app.ui.common.Avatar
import com.sende.app.ui.common.rememberStoredImage
import com.sende.app.ui.theme.Tek
import com.sende.app.util.TimeFormatter

/**
 * One chat as a single container box. Tapping the header steps through the levels
 * (closed -> messages -> messages in full -> closed); tapping one message lengthens or shortens
 * just that message in place. Nothing here marks anything read: only the small "Okundu" buttons do.
 */
@Composable
fun ConversationCard(
    conversation: Conversation,
    appLabel: String,
    level: Level,
    isLong: (messageId: String, default: Boolean) -> Boolean,
    canReply: Boolean,
    actionTitles: List<String>,
    onAppAction: (index: Int) -> Unit,
    onCycle: () -> Unit,
    onToggleMessage: (messageId: String, currentlyLong: Boolean) -> Unit,
    onMarkRead: (ids: List<String>) -> Unit,
    onOpenApp: () -> Unit,
    onReply: (String) -> Unit,
    onCopy: (String) -> Unit,
    onPicture: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = Tek.colors
    val motion = Tek.motion
    val shape = RoundedCornerShape(Tek.shapes.card)
    val latest = conversation.latest

    Surface(
        shape = shape,
        color = colors.surface,
        border = BorderStroke(1.dp, colors.line),
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .animateContentSize(spring(dampingRatio = motion.springDamping, stiffness = motion.springStiffness)),
    ) {
        Column {
            Header(
                conversation = conversation,
                appLabel = appLabel,
                level = level,
                onClick = onCycle,
            )

            if (level == Level.Collapsed) {
                Row(
                    modifier = Modifier.padding(start = 74.dp, end = 6.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = previewOf(conversation, stringResource(R.string.message_photo)),
                        style = MaterialTheme.typography.bodyMedium.merge(ContentDirection),
                        color = colors.textSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    ReadButton(onClick = { onMarkRead(conversation.messages.map { it.id }) })
                }
            } else {
                Column(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    conversation.messages.forEach { message ->
                        val long = isLong(message.id, level == Level.Full)
                        MessageRow(
                            message = message,
                            showSender = conversation.isGroup,
                            long = long,
                            onToggle = { onToggleMessage(message.id, long) },
                            onMarkRead = { onMarkRead(listOf(message.id)) },
                            onCopy = { onCopy(message.messageText) },
                            onPicture = onPicture,
                        )
                    }
                    if (level == Level.Full) {
                        Footer(
                            conversation = conversation,
                            canReply = canReply,
                            actionTitles = actionTitles,
                            onAppAction = onAppAction,
                            onOpenApp = onOpenApp,
                            onReply = onReply,
                            onMarkAllRead = { onMarkRead(conversation.messages.map { it.id }) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(conversation: Conversation, appLabel: String, level: Level, onClick: () -> Unit) {
    val colors = Tek.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(48.dp)) {
            Avatar(name = conversation.title, photoPath = conversation.avatarPath, size = 44.dp)
            AppIcon(
                packageName = conversation.sourcePackage,
                size = 20.dp,
                modifier = Modifier.align(Alignment.BottomEnd).background(colors.surface, CircleShape).padding(1.5.dp),
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f).padding(end = 10.dp)) {
            Text(
                text = conversation.title,
                style = MaterialTheme.typography.titleSmall.merge(ContentDirection),
                color = colors.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = appLabel,
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = TimeFormatter.relative(conversation.lastAt).toString(),
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary,
            )
            if (conversation.count > 1) {
                Spacer(Modifier.height(4.dp))
                Surface(shape = CircleShape, color = if (level == Level.Collapsed) colors.accent else colors.surfaceHigh) {
                    Text(
                        text = conversation.count.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (level == Level.Collapsed) colors.onAccent else colors.textSecondary,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 1.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MessageRow(
    message: InboxMessage,
    showSender: Boolean,
    long: Boolean,
    onToggle: () -> Unit,
    onMarkRead: () -> Unit,
    onCopy: () -> Unit,
    onPicture: (String) -> Unit,
) {
    val colors = Tek.colors
    val haptic = LocalHapticFeedback.current
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = colors.surfaceHigh,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .combinedClickable(
                onClick = onToggle,
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCopy()
                },
            ),
    ) {
        Row(
            modifier = Modifier.heightIn(min = 68.dp).padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                if (showSender) {
                    Text(
                        text = message.senderName,
                        style = MaterialTheme.typography.labelMedium.merge(ContentDirection),
                        color = colors.accent,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (message.messageText.isNotBlank()) {
                    Text(
                        text = message.messageText,
                        style = MaterialTheme.typography.bodyMedium.merge(ContentDirection),
                        color = colors.text,
                        maxLines = if (long) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (long && message.messageText.length >= ANDROID_TEXT_LIMIT_HINT) {
                    // Android itself cuts a notification's text at 1024 characters; say so instead of looking broken.
                    Text(
                        text = stringResource(R.string.message_truncated_note),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                if (message.picturePath != null && long) {
                    PictureThumb(message.picturePath, big = true, onClick = { onPicture(message.picturePath) })
                }
                // Lengthened messages carry the Okundu button on their own bottom line so wide
                // content (pictures, long text) keeps the full width of the box.
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                    Text(
                        text = TimeFormatter.relative(message.receivedAt).toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary,
                        modifier = Modifier.weight(1f),
                    )
                    if (long) ReadButton(onClick = onMarkRead)
                }
            }
            if (message.picturePath != null && !long) {
                PictureThumb(message.picturePath, big = false, onClick = { onPicture(message.picturePath) })
            }
            if (!long) ReadButton(onClick = onMarkRead)
        }
    }
}

@Composable
private fun PictureThumb(path: String, big: Boolean, onClick: () -> Unit) {
    val image by rememberStoredImage(path, maxPx = if (big) 900 else 160)
    val bitmap = image
    val shape = RoundedCornerShape(10.dp)
    val sizeModifier = if (big) {
        Modifier.padding(top = 6.dp).fillMaxWidth().heightIn(max = 260.dp)
    } else {
        Modifier.padding(horizontal = 6.dp).size(48.dp)
    }
    Box(sizeModifier.clip(shape).background(Tek.colors.surface).clickable(onClick = onClick)) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = null,
                contentScale = if (big) ContentScale.FillWidth else ContentScale.Crop,
                modifier = if (big) Modifier.fillMaxWidth() else Modifier.size(48.dp),
            )
        }
    }
}

@Composable
private fun ReadButton(onClick: () -> Unit) {
    TextButton(onClick = onClick, contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)) {
        Text(
            text = stringResource(R.string.action_mark_read),
            style = MaterialTheme.typography.labelMedium,
            color = Tek.colors.accent,
        )
    }
}

@Composable
private fun Footer(
    conversation: Conversation,
    canReply: Boolean,
    actionTitles: List<String>,
    onAppAction: (index: Int) -> Unit,
    onOpenApp: () -> Unit,
    onReply: (String) -> Unit,
    onMarkAllRead: () -> Unit,
) {
    val colors = Tek.colors
    var replying by rememberSaveable(conversation.key) { mutableStateOf(false) }
    var draft by rememberSaveable(conversation.key) { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (replying) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    placeholder = { Text(stringResource(R.string.reply_hint), color = colors.textSecondary) },
                    modifier = Modifier.weight(1f),
                    maxLines = 4,
                    shape = RoundedCornerShape(Tek.shapes.button),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.accent,
                        unfocusedBorderColor = colors.line,
                        focusedTextColor = colors.text,
                        unfocusedTextColor = colors.text,
                        cursorColor = colors.accent,
                    ),
                )
                IconButton(
                    onClick = {
                        onReply(draft)
                        draft = ""
                        replying = false
                    },
                    enabled = draft.isNotBlank(),
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.Send,
                        contentDescription = stringResource(R.string.action_send),
                        tint = if (draft.isNotBlank()) colors.accent else colors.textSecondary,
                    )
                }
            }
        }
        if (actionTitles.isNotEmpty()) {
            // The sending app's own buttons, exactly as it labelled them in its notification.
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                actionTitles.forEachIndexed { index, title ->
                    Surface(
                        onClick = { onAppAction(index) },
                        shape = RoundedCornerShape(Tek.shapes.button),
                        color = colors.surfaceHigh,
                        border = BorderStroke(1.dp, colors.line),
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.text,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        )
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onOpenApp) {
                Icon(Icons.AutoMirrored.Rounded.OpenInNew, contentDescription = null, tint = colors.text, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(stringResource(R.string.action_open_in_app), style = MaterialTheme.typography.labelLarge, color = colors.text)
            }
            if (canReply && !replying) {
                TextButton(onClick = { replying = true }) {
                    Text(stringResource(R.string.action_reply), style = MaterialTheme.typography.labelLarge, color = colors.accent)
                }
            }
            Spacer(Modifier.weight(1f))
            if (conversation.count > 1) {
                TextButton(onClick = onMarkAllRead) {
                    Text(stringResource(R.string.action_mark_all_read), style = MaterialTheme.typography.labelLarge, color = colors.accent)
                }
            }
        }
    }
}

private const val ANDROID_TEXT_LIMIT_HINT = 1000

/** Customer text can be in any script, whatever the phone's language: each line picks its own direction. */
private val ContentDirection = androidx.compose.ui.text.TextStyle(textDirection = androidx.compose.ui.text.style.TextDirection.Content)

private fun previewOf(conversation: Conversation, photoLabel: String): String {
    val latest = conversation.latest
    val body = latest.messageText.ifBlank { photoLabel }
    return if (conversation.isGroup) "${latest.senderName}: $body" else body
}
