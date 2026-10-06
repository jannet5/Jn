package com.jn.yazikart.render // çizim paketi

// Canva'daki gibi mıknatıslı ortalama: yazının merkezi görselin ortasına yaklaşınca tam ortaya yapışır
object Snap {
    const val THRESHOLD = 0.025f // yapışma mesafesi: görsel boyutunun %2,5'i

    // raw: parmağın getirdiği kayma (oran), centerPx: yazının şu anki merkezi, targetPx: görselin ortası,
    // sizePx: görselin o yöndeki boyu. Döner: (yeni kayma oranı, yapıştı mı)
    fun snap(raw: Float, centerPx: Float, targetPx: Float, sizePx: Float): Pair<Float, Boolean> {
        val diff = targetPx - centerPx // ortaya olan uzaklık (piksel)
        return if (kotlin.math.abs(diff) <= sizePx * THRESHOLD) { // yeterince yakınsa
            (raw + diff / sizePx) to true // tam ortaya kaydırılıyor
        } else raw to false // uzaksa parmak neredeyse orada
    }
}
