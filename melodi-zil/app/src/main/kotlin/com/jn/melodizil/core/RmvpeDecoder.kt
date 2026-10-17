package com.jn.melodizil.core // Saf Kotlin çekirdek

import kotlin.math.max // Büyük olan
import kotlin.math.pow // Üs alma

/** RMVPE çıktısı (salience [T,360]) -> Hz. rmvpe_model.py to_local_average_cents ile aynı: argmax çevresinde 9 kutuluk ağırlıklı ortalama. */
object RmvpeDecoder {
    const val BINS = 360 // Perde sınıfı sayısı (20 cent aralıklı)
    const val THRESHOLD = 0.2f // Bu değer ve altında kare sessiz sayılır
    private const val BASE_CENTS = 1997.3794084376191 // 0. sınıfın cent değeri (10 Hz'e göre)
    private val cents = DoubleArray(BINS) { 20.0 * it + BASE_CENTS } // Sınıf -> cent

    /**
     * [sal]: [frames x 360] düz dizi (salienceOffset'ten başlar). Sonuç f0Out/maxOut dizilerine outOffset'ten yazılır.
     * f0Out: Hz (0 = sessiz). maxOut: karedeki en büyük sınıf olasılığı.
     */
    fun decode(sal: FloatArray, salOffset: Int, frames: Int, f0Out: FloatArray, maxOut: FloatArray, outOffset: Int, threshold: Float = THRESHOLD) {
        for (t in 0 until frames) {
            val base = salOffset + t * BINS // Karenin ilk sınıfı
            var c = 0; var best = sal[base] // En büyük sınıf (ilk eşit olan, numpy argmax gibi)
            for (k in 1 until BINS) if (sal[base + k] > best) { best = sal[base + k]; c = k }
            maxOut[outOffset + t] = best // Salience = karedeki en büyük değer
            if (best <= threshold) { f0Out[outOffset + t] = 0f; continue } // Sessiz kare
            var num = 0.0; var den = 0.0 // Ağırlıklı ortalama toplamları
            for (k in max(0, c - 4)..minOf(BINS - 1, c + 4)) { val s = sal[base + k].toDouble(); num += s * cents[k]; den += s } // 9 kutu
            val cent = num / max(den, 1e-9) // Yerel ortalama cent
            f0Out[outOffset + t] = (10.0 * 2.0.pow(cent / 1200.0)).toFloat() // Cent -> Hz
        }
    }

    /** Tek seferde tüm salience'ı PitchTrack'e çevirir (test/küçük girdi için). */
    fun toTrack(sal: FloatArray, frames: Int, threshold: Float = THRESHOLD): PitchTrack {
        val f0 = FloatArray(frames); val mx = FloatArray(frames) // Çıktılar
        decode(sal, 0, frames, f0, mx, 0, threshold)
        return PitchTrack(0.01f, f0, mx)
    }
}
