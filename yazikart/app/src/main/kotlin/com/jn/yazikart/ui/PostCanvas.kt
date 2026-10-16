package com.jn.yazikart.ui // arayüz paketi

import android.graphics.Bitmap // arka plan resmi
import androidx.compose.foundation.Canvas // çizim alanı
import androidx.compose.foundation.background // arka plan boyama
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.ui.graphics.Color // renk
import androidx.media3.common.Player // video oynatıcısı
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas // Android tuvaline erişim
import androidx.compose.ui.graphics.nativeCanvas // Android tuvali
import androidx.compose.ui.platform.LocalContext // uygulama bağlamı
import com.jn.yazikart.data.FontCatalog // yazı tipleri
import com.jn.yazikart.data.PostStyle // görsel ayarları
import com.jn.yazikart.render.PostRenderer // görsel çizici

const val PLACEHOLDER = "Yazın burada görünecek" // yazı boşken görünen ipucu

// Görseli verilen alana çizen ortak bileşen (küçük önizleme ve tam ekran aynı çiziciyi kullanır)
@Composable
fun PostCanvas(style: PostStyle, image: Bitmap?, modifier: Modifier = Modifier, videoMode: Boolean = false) {
    val context = LocalContext.current // uygulama bağlamı
    val typeface = remember(style.fontIndex, style.bold) { // yazı tipi değişince yeniden yükleniyor
        FontCatalog.typeface(context.assets, style.fontIndex, style.bold) // seçili yazı tipi
    }
    val fakeBold = FontCatalog.needsFakeBold(style.fontIndex, style.bold) // yapay kalınlık gerekir mi
    Canvas(modifier) { // çizim alanı
        drawIntoCanvas { c -> // Android tuvaline geçiliyor
            PostRenderer.draw( // kayıtla aynı çizici
                c.nativeCanvas, size.width.toInt(), size.height.toInt(), // tuval ve boyut
                style, typeface, fakeBold, image, // ayarlar
                placeholder = PLACEHOLDER, // boşken ipucu
                videoMode = videoMode, // video varsa sadece yazı katmanı
            )
        }
    }
}

// Önizleme ve tam ekranın ortak yüzeyi: video varsa video + üstünde yazı katmanı, yoksa normal görsel
@Composable
fun PostSurface(
    style: PostStyle, // ayarlar
    image: Bitmap?, // arka plan resmi
    hasVideo: Boolean, // arka planda video var mı
    player: Player?, // video oynatıcısı
    modifier: Modifier = Modifier, // dış boyut
    canvasModifier: Modifier = Modifier, // yazı katmanına eklenecek (dokunma) değiştiriciler
) {
    val video = hasVideo && player != null // video modu mu
    Box(modifier) { // üst üste katmanlar
        if (video) { // video modu
            Box(Modifier.matchParentSize().background(Color(style.backgroundColor))) // video yüklenirken zemin rengi görünür
            VideoLayer(player!!, Modifier.matchParentSize()) // video
        }
        PostCanvas(style, image, Modifier.matchParentSize().then(canvasModifier), videoMode = video) // yazı (video yoksa zemin de)
    }
}
