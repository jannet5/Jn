package com.tekpanel.app.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.tekpanel.app.ui.theme.Tek

/** A short, plain confirm/cancel dialog: destructive actions always get one. */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    dismissLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    destructive: Boolean = true,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmLabel, color = if (destructive) Tek.colors.danger else Tek.colors.accent)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(text = dismissLabel, color = Tek.colors.textSecondary) } },
        containerColor = Tek.colors.surfaceHigh,
        titleContentColor = Tek.colors.text,
        textContentColor = Tek.colors.textSecondary,
    )
}
