package com.jn.yazikart.ui // arayüz paketi

import androidx.compose.material3.MaterialTheme // Material tema
import androidx.compose.material3.darkColorScheme // koyu renk şeması
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.ui.graphics.Color // renk

// Uygulamanın renkleri: siyah ağırlıklı, tek vurgu rengi (mercan)
private val Colors = darkColorScheme(
    primary = Color(0xFFFF5A36), // vurgu rengi: birincil buton, seçili öğe
    onPrimary = Color(0xFF1A0600), // vurgu üstündeki yazı
    secondary = Color(0xFF2C2C2E), // ikincil yüzey
    onSecondary = Color(0xFFF2F2F7), // ikincil yüzey üstündeki yazı
    background = Color(0xFF0B0B0C), // ekran arka planı
    onBackground = Color(0xFFF2F2F7), // ana yazı rengi
    surface = Color(0xFF151517), // kart/panel yüzeyi
    onSurface = Color(0xFFF2F2F7), // panel üstündeki yazı
    surfaceVariant = Color(0xFF232326), // giriş kutusu yüzeyi
    onSurfaceVariant = Color(0xFFA1A1AA), // ikincil yazı
    outline = Color(0xFF3A3A3F), // çerçeve
    error = Color(0xFFFF6B6B), // hata rengi
)

// Tüm ekranları saran tema
@Composable
fun YaziKartTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colors, content = content) // renkler uygulanıyor
}
