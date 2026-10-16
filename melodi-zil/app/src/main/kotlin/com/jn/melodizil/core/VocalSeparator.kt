package com.jn.melodizil.core // Saf Kotlin çekirdek: Android bağımlılığı yok, JVM'de test edilir

import kotlin.math.PI // Pi
import kotlin.math.cos // Kosinüs
import kotlin.math.sqrt // Karekök

/**
 * Vokal ayırıcı (Spleeter 2stems vokal U-Net'i, Deezer Research, MIT lisansı).
 * İşlem: 22050 Hz mono → STFT (2048/512, periyodik Hann; 44.1k/4096/1024 ile aynı bin aralığı ve süre) →
 * 512 karelik parçalar halinde model (girdi genliği ×2 ölçeklenir) → maske = çıktı / girdi → vokal STFT → iSTFT.
 * Bellek: yalnızca bir parçanın spektrumu tutulur (~8 MB), şarkı uzunluğundan bağımsız.
 * Doğrulama: araclar/model/vokal_ayir22.py ile birebir aynı; değerlendirme setinde doğru perde %31 → %70.
 */
class VocalSeparator(private val model: MaskModel) {

    /** Spektrogram → tahmini vokal genliği. Girdi/çıktı düzeni [T=512][F=1024][kanal=2] satır öncelikli. */
    fun interface MaskModel { fun predict(input: FloatArray, output: FloatArray) }

    fun interface Progress { fun onProgress(fraction: Float) } // İlerleme

    private val fft = Fft(N) // FFT
    private val win = FloatArray(N) { (0.5 - 0.5 * cos(2.0 * PI * it / N)).toFloat() } // Periyodik Hann

    /** Mono PCM (22050 Hz) → yalnızca vokal içeren PCM (aynı uzunluk). */
    fun separate(pcm: FloatArray, progress: Progress? = null): FloatArray {
        val padded = FloatArray(pcm.size + N) // Merkezli STFT için iki yandan N/2 sıfır
        System.arraycopy(pcm, 0, padded, N / 2, pcm.size) // Kopyala
        val frames = 1 + (padded.size - N) / H // Kare sayısı
        val out = FloatArray(padded.size); val wsum = FloatArray(padded.size) // Üst üste ekleme tamponları
        val re = Array(T) { FloatArray(N) }; val im = Array(T) { FloatArray(N) } // Bir parçanın spektrumu
        val input = FloatArray(T * F * 2); val pred = FloatArray(T * F * 2) // Model tamponları
        val fr = FloatArray(N); val fi = FloatArray(N) // Ters FFT tamponu
        var s = 0 // Parça başlangıç karesi
        while (s < frames) { // Her parça
            val count = minOf(T, frames - s) // Bu parçadaki kare
            java.util.Arrays.fill(input, 0f) // Boş kareler sıfır
            for (j in 0 until count) { // İleri STFT
                val off = (s + j) * H; val r = re[j]; val m = im[j] // Kare
                for (k in 0 until N) { r[k] = padded[off + k] * win[k]; m[k] = 0f } // Pencere
                fft.transform(r, m) // FFT
                for (b in 0 until F) { val mag = sqrt(r[b] * r[b] + m[b] * m[b]) * 2f; val idx = (j * F + b) * 2; input[idx] = mag; input[idx + 1] = mag } // Genlik ×2, mono → 2 kanal
            }
            model.predict(input, pred) // Model
            for (j in 0 until count) { // Maske + ters STFT
                val r = re[j]; val m = im[j] // Spektrum
                for (k in 0 until N) { fr[k] = 0f; fi[k] = 0f } // Sıfırla (1024 ve üstü bin = 0: Spleeter "mask_extension: zeros")
                for (b in 0 until F) { // Maske uygula
                    val idx = (j * F + b) * 2; val inMag = input[idx]; val v = (pred[idx] + pred[idx + 1]) * 0.5f // Ortalama vokal genliği
                    val mask = if (inMag > 1e-10f) (v / inMag).coerceIn(0f, 1f) else 0f // Maske
                    fr[b] = r[b] * mask; fi[b] = m[b] * mask // Vokal spektrumu
                    if (b > 0) { fr[N - b] = fr[b]; fi[N - b] = -fi[b] } // Hermit simetri (gerçel sinyal)
                }
                for (k in 0 until N) fi[k] = -fi[k] // Ters FFT = eşlenik(FFT(eşlenik(X))) / N
                fft.transform(fr, fi) // FFT
                val off = (s + j) * H // Kare konumu
                for (k in 0 until N) { val w = win[k]; out[off + k] += fr[k] / N * w; wsum[off + k] += w * w } // Pencereli üst üste ekleme
            }
            s += count // Sonraki parça
            progress?.onProgress(s.toFloat() / frames) // İlerleme
        }
        val result = FloatArray(pcm.size) // Merkez kırpması + pencere normalizasyonu
        for (i in pcm.indices) { val k = i + N / 2; result[i] = if (wsum[k] > 1e-8f) out[k] / wsum[k] else 0f } // Normalize
        return result // Vokal PCM
    }

    companion object {
        const val N = 2048 // Pencere (22050 Hz'de 92.9 ms = 44.1k'da 4096)
        const val H = 512 // Atlama (23.2 ms)
        const val T = 512 // Model parça uzunluğu (kare)
        const val F = 1024 // Model frekans bin sayısı (0-11 kHz)
    }
}
