package com.jn.yazikart.ui // arayüz paketi

import android.graphics.Bitmap // arka plan resmi
import androidx.compose.foundation.Canvas // çizim alanı
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas // Android tuvaline erişim
import androidx.compose.ui.graphics.nativeCanvas // Android tuvali
import androidx.compose.ui.platform.LocalContext // uygulama bağlamı
import com.jn.yazikart.data.FontCatalog // yazı tipleri
import com.jn.yazikart.data.PostStyle // görsel ayarları
import com.jn.yazikart.render.PostRenderer // görsel çizici

// Görseli verilen alana çizen ortak bileşen (küçük önizleme ve tam ekran aynı çiziciyi kullanır)
@Composable
fun PostCanvas(style: PostStyle, image: Bitmap?, modifier: Modifier = Modifier) {
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
                placeholder = "Yazın burada görünecek", // boşken ipucu
            )
        }
    }
}
