package com.jn.yazikart.ui // arayüz paketi

import android.graphics.Bitmap // arka plan resmi
import androidx.compose.foundation.Canvas // çizim alanı
import androidx.compose.foundation.Image // fotoğraf karesi
import androidx.compose.foundation.background // arka plan boyama
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.asImageBitmap // Bitmap → ImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas // Android tuvaline erişim
import androidx.compose.ui.graphics.nativeCanvas // Android tuvali
import androidx.compose.ui.layout.ContentScale // resim ölçekleme
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
                videoMode = videoMode, // video/fotoğraf varsa sadece yazı katmanı
            )
        }
    }
}

// Önizleme ve tam ekranın ortak yüzeyi: sırada parça varsa (video ya da fotoğraf) onu göster, yoksa normal görsel.
// Üstüne her zaman yazı katmanı gelir; kaydedilen dosyada da aynı sıra ve yazı olur.
@Composable
fun PostSurface(
    style: PostStyle, // ayarlar
    image: Bitmap?, // arka plan resmi (sırada parça yoksa)
    video: VideoUi?, // sıra ve oynatıcı (yoksa null)
    modifier: Modifier = Modifier, // dış boyut
    canvasModifier: Modifier = Modifier, // yazı katmanına eklenecek dokunma değiştiricileri
) {
    val v = video // yerel kopya (akıllı dönüşüm için)
    val show = v != null && v.hasVideo // sıra gösterilecek mi
    Box(modifier) { // üst üste katmanlar
        if (v != null && show) { // sıra modu
            Box(Modifier.matchParentSize().background(Color(style.backgroundColor))) // sıra yüklenirken zemin rengi
            val photo = v.photo // şu an gösterilen fotoğraf (video oynuyorsa null)
            if (photo != null) { // fotoğraf
                Image(bitmap = photo.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.matchParentSize())
            } else { // video
                VideoLayer(v.player, Modifier.matchParentSize())
            }
        }
        PostCanvas(style, image, Modifier.matchParentSize().then(canvasModifier), videoMode = show) // yazı (sıra yoksa zemin de çizilir)
    }
}
