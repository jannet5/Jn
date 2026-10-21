package com.sende.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SendeTheme(
    colors: BrandColors = if (isSystemInDarkTheme()) Brand.dark else Brand.light,
    shapes: BrandShapes = Brand.shapes,
    motion: BrandMotion = Brand.motion,
    typography: androidx.compose.material3.Typography = remember { brandTypography(BrandFontFamilies.heading, BrandFontFamilies.body) },
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            val controller = WindowCompat.getInsetsController(it, view)
            controller.isAppearanceLightStatusBars = !colors.isDark
            controller.isAppearanceLightNavigationBars = !colors.isDark
        }
    }
    val scheme = if (colors.isDark) {
        darkColorScheme(
            background = colors.background, surface = colors.surface, surfaceVariant = colors.surfaceHigh,
            onBackground = colors.text, onSurface = colors.text, onSurfaceVariant = colors.textSecondary,
            outline = colors.line, primary = colors.accent, onPrimary = colors.onAccent,
            error = colors.danger, onError = colors.text,
        )
    } else {
        lightColorScheme(
            background = colors.background, surface = colors.surface, surfaceVariant = colors.surfaceHigh,
            onBackground = colors.text, onSurface = colors.text, onSurfaceVariant = colors.textSecondary,
            outline = colors.line, primary = colors.accent, onPrimary = colors.onAccent,
            error = colors.danger, onError = colors.background,
        )
    }
    CompositionLocalProvider(
        LocalBrandColors provides colors,
        LocalBrandShapes provides shapes,
        LocalBrandMotion provides motion,
    ) {
        MaterialTheme(colorScheme = scheme, typography = typography, content = content)
    }
}
