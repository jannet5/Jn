package com.jn.yazikart.render // çizim paketi

import android.graphics.Bitmap // arka plan resmi ve kayıt görseli
import android.graphics.Canvas // üzerine çizim yapılan tuval
import android.graphics.Color // renk yardımcıları
import android.graphics.Paint // çizim ayarları
import android.graphics.Rect // resmin kırpılacak bölgesi
import android.graphics.RectF // resmin çizileceği bölge
import android.graphics.Typeface // yazı tipi
import android.text.Layout // satır hizası
import android.text.StaticLayout // çok satırlı yazı yerleşimi
import android.text.TextPaint // yazı çizim ayarları
import com.jn.yazikart.data.PostStyle // görsel ayarları
import com.jn.yazikart.data.TextAlign // yatay hiza
import com.jn.yazikart.data.VerticalPos // dikey konum

// Kırpılacak bölge (Android'den bağımsız; testte de çalışsın diye)
data class CropBox(val left: Int, val top: Int, val right: Int, val bottom: Int)

// Görseli çizen tek yer. Hem ekrandaki önizleme hem de kaydedilen dosya bunu kullanır,
// bu yüzden ekranda ne görüyorsan dosyada da birebir o çıkar.
object PostRenderer {

    const val PADDING = 0.08f // kenar boşluğu, genişliğin oranı
    private const val MIN_SIZE = 0.025f // yazı sığmazsa küçülebileceği en küçük boy (genişlik oranı)

    // Görseli verilen tuvale w x h boyutunda çizer
    fun draw(
        canvas: Canvas, // çizim yapılacak tuval
        w: Int, // genişlik (piksel)
        h: Int, // yükseklik (piksel)
        style: PostStyle, // ayarlar
        typeface: Typeface, // yazı tipi
        fakeBold: Boolean, // kalın dosya yoksa yapay kalınlık
        image: Bitmap?, // arka plan resmi (yoksa sadece renk)
        placeholder: String? = null, // yazı boşken önizlemede gösterilecek ipucu
    ) {
        canvas.drawColor(style.backgroundColor) // önce zemin rengi boyanıyor
        if (image != null) { // arka plan resmi varsa
            drawCenterCrop(canvas, image, w, h) // resim ortadan kırpılarak tüm alana yayılıyor
            if (style.dim > 0f) { // karartma isteniyorsa
                canvas.drawColor(Color.argb((style.dim * 255).toInt(), 0, 0, 0)) // üstüne yarı saydam siyah çekiliyor
            }
        }

        val isPlaceholder = style.text.isBlank() // yazı boş mu
        val text = if (isPlaceholder) placeholder ?: return else style.text // boşsa ipucu, ipucu da yoksa çizim bitti

        val paint = TextPaint(Paint.ANTI_ALIAS_FLAG) // yumuşak kenarlı yazı ayarı
        paint.typeface = typeface // seçili yazı tipi
        paint.isFakeBoldText = fakeBold // gerekiyorsa yapay kalınlık
        paint.color = style.textColor // yazı rengi
        if (isPlaceholder) paint.alpha = 90 // ipucu soluk görünsün
        if (style.shadow) { // gölge açıksa
            paint.setShadowLayer(w * 0.012f, 0f, w * 0.004f, Color.argb(170, 0, 0, 0)) // yumuşak koyu gölge
        }

        val pad = w * PADDING // kenar boşluğu (piksel)
        val boxW = (w - 2 * pad).toInt().coerceAtLeast(1) // yazının sığacağı genişlik
        val boxH = h - 2 * pad // yazının sığacağı yükseklik
        val alignment = when (style.align) { // hiza Android karşılığına çevriliyor
            TextAlign.LEFT -> Layout.Alignment.ALIGN_NORMAL // sola
            TextAlign.CENTER -> Layout.Alignment.ALIGN_CENTER // ortaya
            TextAlign.RIGHT -> Layout.Alignment.ALIGN_OPPOSITE // sağa
        }

        var size = w * style.textSize // istenen yazı boyu (piksel)
        var layout = buildLayout(text, paint, size, boxW, alignment) // ilk yerleşim
        while (layout.height > boxH && size > w * MIN_SIZE) { // yazı sığmıyorsa ve hâlâ küçültülebiliyorsa
            size *= 0.92f // boy biraz küçültülüyor
            layout = buildLayout(text, paint, size, boxW, alignment) // yeniden yerleştiriliyor
        }

        val top = when (style.verticalPos) { // yazının üst kenarının y konumu
            VerticalPos.TOP -> pad // üstte
            VerticalPos.CENTER -> (h - layout.height) / 2f // ortada
            VerticalPos.BOTTOM -> h - pad - layout.height // altta
        }
        canvas.save() // tuvalin durumu saklanıyor
        canvas.translate(pad, top) // yazının başlayacağı noktaya kayılıyor
        layout.draw(canvas) // yazı çiziliyor
        canvas.restore() // tuval eski haline dönüyor
    }

    // Verilen yazı boyuyla çok satırlı yerleşim oluşturur
    private fun buildLayout(text: String, paint: TextPaint, size: Float, width: Int, align: Layout.Alignment): StaticLayout {
        paint.textSize = size // boy ayarlanıyor
        return StaticLayout.Builder.obtain(text, 0, text.length, paint, width) // yerleşim kurucusu
            .setAlignment(align) // hiza
            .setLineSpacing(0f, 1.15f) // satır aralığı biraz açık
            .setIncludePad(false) // gereksiz üst/alt boşluk yok
            .build() // yerleşim hazır
    }

    // Resmi oranını bozmadan tüm alanı kaplayacak şekilde ortadan kırparak çizer
    fun drawCenterCrop(canvas: Canvas, image: Bitmap, w: Int, h: Int) {
        val c = centerCropRect(image.width, image.height, w, h) // resmin kullanılacak bölgesi
        val src = Rect(c.left, c.top, c.right, c.bottom) // Android dikdörtgenine çevriliyor
        val dst = RectF(0f, 0f, w.toFloat(), h.toFloat()) // tüm tuval
        canvas.drawBitmap(image, src, dst, Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)) // yumuşak ölçekleyerek çiziliyor
    }

    // Kaynak resimden hedef orana uyan ortadaki dikdörtgeni hesaplar (test edilebilir saf hesap)
    fun centerCropRect(srcW: Int, srcH: Int, dstW: Int, dstH: Int): CropBox {
        val srcRatio = srcW.toFloat() / srcH // resmin en/boy oranı
        val dstRatio = dstW.toFloat() / dstH // hedefin en/boy oranı
        return if (srcRatio > dstRatio) { // resim hedefe göre daha geniş
            val cropW = (srcH * dstRatio).toInt() // kullanılacak genişlik
            val left = (srcW - cropW) / 2 // yanlardan eşit kırpılıyor
            CropBox(left, 0, left + cropW, srcH) // ortadaki bölge
        } else { // resim hedefe göre daha uzun
            val cropH = (srcW / dstRatio).toInt() // kullanılacak yükseklik
            val topY = (srcH - cropH) / 2 // üstten ve alttan eşit kırpılıyor
            CropBox(0, topY, srcW, topY + cropH) // ortadaki bölge
        }
    }

    // Kaydetmek/paylaşmak için tam boyutlu görsel üretir (örn. 1080x1350)
    fun renderBitmap(style: PostStyle, typeface: Typeface, fakeBold: Boolean, image: Bitmap?): Bitmap {
        val w = style.aspect.width // hedef genişlik
        val h = style.aspect.height // hedef yükseklik
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888) // boş görsel
        draw(Canvas(bmp), w, h, style, typeface, fakeBold, image) // üzerine çiziliyor
        return bmp // hazır görsel
    }
}
