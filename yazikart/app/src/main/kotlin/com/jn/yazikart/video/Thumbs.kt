package com.jn.yazikart.video // video paketi

import android.content.Context // uygulama bağlamı
import android.graphics.Bitmap // kare görüntüsü
import android.media.MediaMetadataRetriever // videodan kare çeken Android aracı
import android.net.Uri // video adresi
import android.os.Build // Android sürümü

// Videodan küçük kareler çıkarır: sıradaki küçük resimler ve kırparken gösterilen büyük kare için.
// Android'in kendi aracını kullanır, ek kütüphane yok.
object Thumbs {

    // Videonun istenen saniyesindeki kareyi en fazla maxPx boyunda döndürür (hata olursa null).
    // exact=true: tam o kare (kırparken); false: en yakın ana kare (hızlı, küçük resimler için).
    fun frame(context: Context, uri: String, timeMs: Long, maxPx: Int, exact: Boolean = false): Bitmap? =
        frames(context, uri, listOf(timeMs), maxPx, exact).firstOrNull() // tek kare

    // Videonun başından sonuna eşit aralıklı `count` kare (kırpma şeridi için)
    fun strip(context: Context, uri: String, durationMs: Long, count: Int, maxPx: Int): List<Bitmap?> =
        frames(context, uri, (0 until count).map { i -> durationMs * (2 * i + 1) / (2 * count) }, maxPx, false) // her dilimin ortası

    // Videoyu bir kez açıp istenen zamanlardaki kareleri çeker
    private fun frames(context: Context, uri: String, timesMs: List<Long>, maxPx: Int, exact: Boolean): List<Bitmap?> = runCatching {
        val r = MediaMetadataRetriever() // kare çekici
        try {
            r.setDataSource(context, Uri.parse(uri)) // video açılıyor
            val w = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: maxPx // video genişliği
            val h = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: maxPx // video yüksekliği
            val rot = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)?.toIntOrNull() ?: 0 // döndürme (dikey çekim)
            val (vw, vh) = if (rot == 90 || rot == 270) h to w else w to h // ekranda görünen en/boy
            val scale = minOf(1f, maxPx.toFloat() / maxOf(vw, vh)) // küçültme oranı (büyütme yok)
            val tw = (vw * scale).toInt().coerceAtLeast(1) // hedef genişlik
            val th = (vh * scale).toInt().coerceAtLeast(1) // hedef yükseklik
            val option = if (exact) MediaMetadataRetriever.OPTION_CLOSEST else MediaMetadataRetriever.OPTION_CLOSEST_SYNC // hassasiyet
            timesMs.map { t -> // her zaman için
                runCatching {
                    if (Build.VERSION.SDK_INT >= 27) r.getScaledFrameAtTime(t * 1000, option, tw, th) // doğrudan küçük kare (Android 8.1+)
                    else r.getFrameAtTime(t * 1000, option)?.let { b -> Bitmap.createScaledBitmap(b, tw, th, true) } // eski telefonlarda çekilip küçültülür
                }.getOrNull() // o kare alınamazsa null
            }
        } finally { r.release() } // çekici kapatılıyor
    }.getOrDefault(timesMs.map { null }) // video açılamazsa hepsi boş
}
