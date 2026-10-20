package com.jn.oynatici.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.oynatici.data.Platform

// Platformun kendi rengiyle küçük logo kutusu (klasör ikonu, hesaplar listesi, indirme penceresi için)
@Composable
fun PlatformIkonu(platform: Platform, boyut: Dp = 44.dp) {
    val sekil = RoundedCornerShape(boyut * 0.27f) // köşe yuvarlaklığı boyuta göre
    val arka: Brush = when (platform) { // markanın rengi
        Platform.YOUTUBE -> Brush.linearGradient(listOf(Color(0xFFFF0033), Color(0xFFE00000))) // YouTube kırmızısı
        Platform.INSTAGRAM -> Brush.linearGradient(listOf(Color(0xFFFEDA75), Color(0xFFFA7E1E), Color(0xFFD62976), Color(0xFF962FBF), Color(0xFF4F5BD5))) // Instagram geçişi
        Platform.TIKTOK -> Brush.linearGradient(listOf(Color(0xFF010101), Color(0xFF111111))) // TikTok siyahı
        Platform.X -> Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF14171A))) // X siyahı
        Platform.PINTEREST -> Brush.linearGradient(listOf(Color(0xFFE60023), Color(0xFFBD081C))) // Pinterest kırmızısı
        Platform.DIGER -> Brush.linearGradient(listOf(Color(0xFF3A4252), Color(0xFF2A303C))) // diğer: gri
    }
    Box(Modifier.size(boyut).background(arka, sekil), contentAlignment = Alignment.Center) { // logo kutusu
        val ikonBoyut = boyut * 0.55f // iç işaretin boyutu
        when (platform) {
            Platform.YOUTUBE -> Icon(Icons.Rounded.PlayArrow, "YouTube", Modifier.size(ikonBoyut), tint = Color.White) // oynat üçgeni
            Platform.INSTAGRAM -> Icon(Icons.Rounded.PhotoCamera, "Instagram", Modifier.size(ikonBoyut), tint = Color.White) // kamera
            Platform.TIKTOK -> Icon(Icons.Rounded.MusicNote, "TikTok", Modifier.size(ikonBoyut), tint = Color(0xFF25F4EE)) // nota (TikTok turkuazı)
            Platform.X -> Text("𝕏", color = Color.White, fontSize = (boyut.value * 0.5f).sp, fontWeight = FontWeight.Bold) // X harfi
            Platform.PINTEREST -> Text("P", color = Color.White, fontSize = (boyut.value * 0.55f).sp, fontWeight = FontWeight.Black) // P harfi
            Platform.DIGER -> Icon(Icons.Rounded.Language, "İnternet", Modifier.size(ikonBoyut), tint = Color.White) // dünya
        }
    }
}
