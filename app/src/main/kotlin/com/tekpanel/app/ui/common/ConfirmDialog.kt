package com.tekpanel.app.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.tekpanel.app.ui.theme.TekPanelColors

/**
 * A short, plain confirm/cancel dialog (spec 3.4: destructive actions get a normal dialog,
 * never a search-bar-shaped surface).
 */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmLabel: String,
    dismissLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmLabel, color = TekPanelColors.Danger)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissLabel)
            }
        },
        containerColor = TekPanelColors.SurfaceRaised,
        titleContentColor = TekPanelColors.TextPrimary,
        textContentColor = TekPanelColors.TextSecondary,
    )
}
