package com.jn.yazikart.ui // arayüz paketi

import android.graphics.Matrix // görüntüyü ölçekleme
import android.view.TextureView // videonun çizildiği yüzey
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.DisposableEffect // açılış/kapanışta bağlama
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.platform.LocalContext // uygulama bağlamı
import androidx.compose.ui.viewinterop.AndroidView // klasik Android görünümü
import androidx.media3.common.Player // oynatıcı
import androidx.media3.common.VideoSize // video boyutu

// Videoyu kutuya ortadan kırparak (kaydedilen videodaki gibi) gösterir
@Composable
fun VideoLayer(player: Player, modifier: Modifier = Modifier) {
    val context = LocalContext.current // uygulama bağlamı
    val view = remember { TextureView(context) } // video yüzeyi
    DisposableEffect(player) { // oynatıcıya bağlan
        fun fit() { // ortadan kırpma hesabı
            val vs = player.videoSize // videonun boyutu
            val vw = vs.width * vs.pixelWidthHeightRatio // gerçek genişlik
            val vh = vs.height.toFloat() // yükseklik
            val w = view.width.toFloat() // kutu genişliği
            val h = view.height.toFloat() // kutu yüksekliği
            if (vw <= 0f || vh <= 0f || w <= 0f || h <= 0f) return // bilgi yoksa bekle
            val scale = maxOf(w / vw, h / vh) // kutuyu tamamen dolduracak ölçek
            view.setTransform(Matrix().apply { setScale(vw * scale / w, vh * scale / h, w / 2, h / 2) }) // ortadan büyüt (taşan kısım kırpılır)
        }
        val listener = object : Player.Listener { // video boyutu değişince
            override fun onVideoSizeChanged(videoSize: VideoSize) = fit() // yeniden hesapla
        }
        val layout = android.view.View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> fit() } // kutu boyutu değişince
        player.setVideoTextureView(view) // oynatıcı bu yüzeye çizsin
        player.addListener(listener) // dinleyici eklendi
        view.addOnLayoutChangeListener(layout) // boyut dinleyicisi eklendi
        onDispose { // ekran kapanınca
            player.removeListener(listener) // dinleyici kaldırıldı
            view.removeOnLayoutChangeListener(layout) // boyut dinleyicisi kaldırıldı
            player.clearVideoTextureView(view) // yüzeyden ayrıldı
        }
    }
    AndroidView(factory = { view }, modifier = modifier) // yüzey ekranda
}
