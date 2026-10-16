package com.jn.melodizil.ui.theme // Tema

import androidx.compose.foundation.isSystemInDarkTheme // Sistem teması
import androidx.compose.material3.MaterialTheme // M3 tema
import androidx.compose.material3.Typography // Tipografi
import androidx.compose.material3.darkColorScheme // Koyu şema
import androidx.compose.material3.lightColorScheme // Açık şema
import androidx.compose.runtime.Composable // Composable
import androidx.compose.runtime.CompositionLocalProvider // Yerel sağlayıcı
import androidx.compose.runtime.staticCompositionLocalOf // Yerel tanım
import androidx.compose.ui.graphics.Color // Renk
import androidx.compose.ui.text.TextStyle // Metin stili
import androidx.compose.ui.text.font.FontWeight // Ağırlık
import androidx.compose.ui.unit.sp // sp
import com.jn.melodizil.data.store.ThemeMode // Tema modu

/** M3 şemasında karşılığı olmayan ek token'lar (success) için yerel. */
data class ExtraColors(val success: Color, val border: Color, val mutedSurface: Color)
val LocalExtraColors = staticCompositionLocalOf { ExtraColors(LightColors.success, LightColors.border, LightColors.muted) } // Varsayılan açık

private val LightScheme = lightColorScheme( // Açık şema: token → M3 rolü
    primary = LightColors.primary, onPrimary = LightColors.primaryForeground, // Birincil
    secondaryContainer = LightColors.secondary, onSecondaryContainer = LightColors.secondaryForeground, // Chip seçili
    background = LightColors.background, onBackground = LightColors.foreground, // Arka plan
    surface = LightColors.background, onSurface = LightColors.foreground, // Yüzey = arka plan (kartlar surfaceVariant kullanır)
    surfaceVariant = LightColors.card, onSurfaceVariant = LightColors.mutedForeground, // Kart ve ikincil metin
    surfaceContainer = LightColors.card, surfaceContainerHigh = LightColors.card, surfaceContainerLow = LightColors.card, surfaceContainerHighest = LightColors.muted, // Dialog/sheet yüzeyleri
    outline = LightColors.border, outlineVariant = LightColors.border, // Çerçeve
    error = LightColors.destructive, onError = Color.White, // Hata
)
private val DarkScheme = darkColorScheme( // Koyu şema
    primary = DarkColors.primary, onPrimary = DarkColors.primaryForeground, // Birincil
    secondaryContainer = DarkColors.secondary, onSecondaryContainer = DarkColors.secondaryForeground, // Chip seçili
    background = DarkColors.background, onBackground = DarkColors.foreground, // Arka plan
    surface = DarkColors.background, onSurface = DarkColors.foreground, // Yüzey
    surfaceVariant = DarkColors.card, onSurfaceVariant = DarkColors.mutedForeground, // Kart
    surfaceContainer = DarkColors.card, surfaceContainerHigh = DarkColors.card, surfaceContainerLow = DarkColors.card, surfaceContainerHighest = DarkColors.muted, // Dialog/sheet
    outline = DarkColors.border, outlineVariant = DarkColors.border, // Çerçeve
    error = DarkColors.destructive, onError = Color(0xFF3B0A0A), // Hata
)

/** Tipografi skalası: 6 boyut, 3 ağırlık (400/500/700). Sistem fontu (Roboto). */
val AppTypography = Typography(
    displaySmall = TextStyle(fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.Bold), // display
    headlineSmall = TextStyle(fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold), // heading
    titleMedium = TextStyle(fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium), // title
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal), // body
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal), // body-sm
    labelLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium), // buton etiketi
    labelMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium), // chip etiketi
    labelSmall = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium), // caption
)

@Composable
fun MelodiZilTheme(mode: ThemeMode, content: @Composable () -> Unit) {
    val dark = when (mode) { ThemeMode.SYSTEM -> isSystemInDarkTheme(); ThemeMode.DARK -> true; ThemeMode.LIGHT -> false } // Koyu mu
    val extra = if (dark) ExtraColors(DarkColors.success, DarkColors.border, DarkColors.muted) else ExtraColors(LightColors.success, LightColors.border, LightColors.muted) // Ek renkler
    CompositionLocalProvider(LocalExtraColors provides extra) { // Ek renkleri sağla
        MaterialTheme(colorScheme = if (dark) DarkScheme else LightScheme, typography = AppTypography, content = content) // Tema uygula
    }
}
