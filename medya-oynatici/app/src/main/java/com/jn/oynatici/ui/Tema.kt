package com.jn.oynatici.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Sade koyu tema: tek vurgu rengi (mavi), gerisi koyu gri tonları
private val renkler = darkColorScheme(
    primary = Color(0xFF6EA2FF), // vurgu rengi (butonlar, ilerleme)
    onPrimary = Color(0xFF002E6E), // vurgu üstündeki yazı
    primaryContainer = Color(0xFF1E3A66), // vurgulu kutular
    onPrimaryContainer = Color(0xFFD8E2FF), // vurgulu kutu yazısı
    secondaryContainer = Color(0xFF2A2F38), // ikincil kutular
    onSecondaryContainer = Color(0xFFDDE2EC), // ikincil kutu yazısı
    background = Color(0xFF121316), // ekran arka planı
    onBackground = Color(0xFFE3E2E6), // ana yazı
    surface = Color(0xFF121316), // yüzey
    onSurface = Color(0xFFE3E2E6), // yüzey yazısı
    surfaceVariant = Color(0xFF23252B), // kart/satır yüzeyi
    onSurfaceVariant = Color(0xFFA9ADB7), // ikincil yazı
    surfaceContainer = Color(0xFF1B1D22), // alt çubuk, menü yüzeyi
    surfaceContainerHigh = Color(0xFF22252B), // diyalog yüzeyi
    surfaceContainerHighest = Color(0xFF2A2D33), // en üst yüzey
    error = Color(0xFFFFB4AB), // hata rengi
)

// Uygulamanın teması
@Composable
fun OynaticiTema(icerik: @Composable () -> Unit) = MaterialTheme(colorScheme = renkler, content = icerik)
