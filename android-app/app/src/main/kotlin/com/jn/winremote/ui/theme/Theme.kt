package com.jn.winremote.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val BluePrimary = Color(0xFF3B82F6)
val BluePrimaryDark = Color(0xFF60A5FA)
val TealAccent = Color(0xFF14B8A6)
val AmberWarning = Color(0xFFF59E0B)
val RedCritical = Color(0xFFEF4444)
val GreenOk = Color(0xFF22C55E)
val SlateBackground = Color(0xFF0F172A)
val SlateSurface = Color(0xFF1E293B)

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    secondary = TealAccent,
    onSecondary = Color.White,
    error = RedCritical,
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    surfaceVariant = Color(0xFFE2E8F0),
)

private val DarkColors = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = Color(0xFF0B1220),
    secondary = TealAccent,
    onSecondary = Color(0xFF0B1220),
    error = Color(0xFFFCA5A5),
    background = SlateBackground,
    surface = SlateSurface,
    surfaceVariant = Color(0xFF334155),
)

@Composable
fun WinRemoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    @Suppress("DEPRECATION")
                    window.statusBarColor = colorScheme.background.toArgb()
                }
            }
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
