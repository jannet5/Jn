package com.tekpanel.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val TekPanelColorScheme = darkColorScheme(
    background = TekPanelColors.Background,
    surface = TekPanelColors.Surface,
    surfaceVariant = TekPanelColors.SurfaceRaised,
    onBackground = TekPanelColors.TextPrimary,
    onSurface = TekPanelColors.TextPrimary,
    onSurfaceVariant = TekPanelColors.TextSecondary,
    outline = TekPanelColors.Outline,
    primary = TekPanelColors.Accent,
    onPrimary = TekPanelColors.Background,
    error = TekPanelColors.Danger,
    onError = TekPanelColors.TextPrimary,
)

@Composable
fun TekPanelTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = false
        }
    }
    MaterialTheme(
        colorScheme = TekPanelColorScheme,
        typography = TekPanelTypography,
        content = content,
    )
}
