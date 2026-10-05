package com.jn.melodizil.core // Saf Kotlin çekirdek

/** Zil sesi için şarkının en "dolu" bölümünü (genelde nakarat) otomatik seçer. */
object SegmentChooser {
    /** Verilen uzunlukta, nota yoğunluğu × şiddeti en yüksek pencerenin başlangıcını döndürür. */
    fun bestStart(melody: Melody, lengthSec: Float): Float {
        if (melody.isEmpty) return 0f // Nota yoksa baştan
        val maxStart = (melody.durationSec - lengthSec).coerceAtLeast(0f) // Son geçerli başlangıç
        val step = 0.5f // Tarama adımı
        var best = 0f; var bestScore = -1f // En iyi aday
        var start = 0f // Tarama başlangıcı
        val onsets = melody.notes.map { it.startSec } // Nota başlangıçları (hizalama için)
        while (start <= maxStart + 1e-3f) { // Her aday pencere
            val end = start + lengthSec // Pencere sonu
            var score = 0f // Pencere skoru
            for (n in melody.notes) { // Her nota
                if (n.endSec <= start || n.startSec >= end) continue // Dışarıda
                val overlap = minOf(n.endSec, end) - maxOf(n.startSec, start) // Pencere içindeki kısmı
                score += overlap * (0.5f + n.velocity) // Süre × şiddet
            }
            if (score > bestScore) { bestScore = score; best = start } // Daha iyi
            start += step // Sonraki aday
        }
        val snapped = onsets.filter { it >= best - 0.6f && it <= best + 0.6f }.minByOrNull { kotlin.math.abs(it - best) } // Yakın nota başlangıcına hizala
        val aligned = (snapped ?: best) - 0.05f // Notanın hemen öncesinden başla
        return aligned.coerceIn(0f, maxStart) // Sınırlar içinde
    }
}
