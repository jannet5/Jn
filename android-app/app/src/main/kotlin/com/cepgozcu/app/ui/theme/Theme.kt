package com.cepgozcu.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val ColorWhite = androidx.compose.ui.graphics.Color.White

private val DarkColors = darkColorScheme(
    primary = AccentBlueLight,
    onPrimary = NavyBackground,
    secondary = AccentBlue,
    background = NavyBackground,
    onBackground = OnNavy,
    surface = NavySurface,
    onSurface = OnNavy,
    surfaceVariant = NavySurfaceVariant,
    onSurfaceVariant = OnNavyMuted,
    error = SeverityCritical,
)

private val LightColors = lightColorScheme(
    primary = AccentBlueOnLight,
    onPrimary = ColorWhite,
    secondary = AccentBlue,
    background = LightBackground,
    onBackground = OnLight,
    surface = LightSurface,
    onSurface = OnLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = OnLightMuted,
    error = SeverityCritical,
)

@Composable
fun CepGozcuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = CepGozcuTypography,
        content = content,
    )
}
