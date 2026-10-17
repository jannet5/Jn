package com.jn.koken.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Boşluk skalası: 4 · 8 · 12 · 16 · 20 · 24 · 32 */
object Sp {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val screen = 20.dp
    val xl = 24.dp
    val xxl = 32.dp
}

// Açık tema: sıcak kâğıt rengi + toprak kırmızısı (köken/kök hissi), dil etiketleri için petrol mavisi
private val Light = lightColorScheme(
    primary = Color(0xFF8F3F1B), onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF6E3D3), onPrimaryContainer = Color(0xFF5A2410),
    secondary = Color(0xFF1F6F78), onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDCEFF1), onSecondaryContainer = Color(0xFF0E4A52),
    background = Color(0xFFFBF7F0), onBackground = Color(0xFF2B2118),
    surface = Color(0xFFFFFFFF), onSurface = Color(0xFF2B2118),
    surfaceVariant = Color(0xFFF3ECE2), onSurfaceVariant = Color(0xFF6B5E52),
    outline = Color(0xFF8A7B6C), outlineVariant = Color(0xFFE3D6C6),
    error = Color(0xFFB3261E), onError = Color(0xFFFFFFFF),
)

// Koyu tema: saf siyah yerine sıcak koyu kahve yüzeyler
private val Dark = darkColorScheme(
    primary = Color(0xFFF0A67E), onPrimary = Color(0xFF3A1706),
    primaryContainer = Color(0xFF4A2A18), onPrimaryContainer = Color(0xFFFBDCC8),
    secondary = Color(0xFF7FC8CF), onSecondary = Color(0xFF00363B),
    secondaryContainer = Color(0xFF1C4A50), onSecondaryContainer = Color(0xFFCDEEF1),
    background = Color(0xFF17130F), onBackground = Color(0xFFF2E9DF),
    surface = Color(0xFF221C16), onSurface = Color(0xFFF2E9DF),
    surfaceVariant = Color(0xFF2B231B), onSurfaceVariant = Color(0xFFB5A797),
    outline = Color(0xFF9A8B7B), outlineVariant = Color(0xFF3D3226),
    error = Color(0xFFF2B8B5), onError = Color(0xFF601410),
)

@Composable
fun KokenTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (dark) Dark else Light, content = content)
}
