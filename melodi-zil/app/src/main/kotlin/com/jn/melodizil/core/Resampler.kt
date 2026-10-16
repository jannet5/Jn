package com.jn.melodizil.core // Saf Kotlin çekirdek

/** Basit ses hazırlama yardımcıları: kanal birleştirme ve doğrusal yeniden örnekleme. */
object Resampler {
    /** Çok kanallı, kanalları iç içe (interleaved) PCM'i tek kanala indirger. */
    fun toMono(interleaved: FloatArray, channels: Int): FloatArray {
        if (channels <= 1) return interleaved // Zaten mono
        val frames = interleaved.size / channels // Çerçeve sayısı
        val out = FloatArray(frames) // Çıkış dizisi
        val inv = 1f / channels // Ortalama için çarpan
        for (i in 0 until frames) { // Her çerçeve
            var s = 0f // Kanal toplamı
            val base = i * channels // Çerçevenin ilk örneği
            for (c in 0 until channels) s += interleaved[base + c] // Kanallar toplanıyor
            out[i] = s * inv // Ortalama yazılıyor
        }
        return out // Mono sinyal
    }

    /**
     * Doğrusal enterpolasyonla yeniden örnekler. Melodi analizi için yeterli;
     * düşürme (downsample) öncesi basit bir ortalama filtresiyle alias azaltılır.
     */
    fun resample(input: FloatArray, fromRate: Int, toRate: Int): FloatArray {
        if (fromRate == toRate || input.isEmpty()) return input // Dönüşüm gerekmiyor
        val src = if (fromRate > toRate * 3 / 2) boxFilter(input, fromRate / toRate) else input // Büyük düşürmede önce yumuşat
        val ratio = fromRate.toDouble() / toRate // Giriş/çıkış oranı
        val outLen = (input.size / ratio).toInt() // Çıkış uzunluğu
        val out = FloatArray(outLen) // Çıkış dizisi
        for (i in 0 until outLen) { // Her çıkış örneği
            val pos = i * ratio // Girişteki kesirli konum
            val idx = pos.toInt() // Tam kısım
            val frac = (pos - idx).toFloat() // Kesir
            val a = src[minOf(idx, src.size - 1)] // Sol örnek
            val b = src[minOf(idx + 1, src.size - 1)] // Sağ örnek
            out[i] = a + (b - a) * frac // Doğrusal karışım
        }
        return out // Yeniden örneklenmiş sinyal
    }

    /** Kayan ortalama (kutu) filtresi: alias azaltmak için düşürme öncesi uygulanır. */
    private fun boxFilter(x: FloatArray, width: Int): FloatArray {
        val w = maxOf(2, width) // En az 2 genişlik
        val out = FloatArray(x.size) // Çıkış
        var sum = 0f // Pencere toplamı
        for (i in x.indices) { // Her örnek
            sum += x[i] // Yeni örnek eklenir
            if (i >= w) sum -= x[i - w] // Pencereden çıkan örnek düşülür
            out[i] = sum / minOf(i + 1, w) // Ortalama
        }
        return out // Filtrelenmiş sinyal
    }
}
