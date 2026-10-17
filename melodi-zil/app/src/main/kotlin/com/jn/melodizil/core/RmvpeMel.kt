package com.jn.melodizil.core // Saf Kotlin çekirdek

import kotlin.math.PI // Pi sabiti
import kotlin.math.cos // Kosinüs
import kotlin.math.ln // Doğal logaritma
import kotlin.math.log10 // 10 tabanlı logaritma
import kotlin.math.max // Büyük olan
import kotlin.math.min // Küçük olan
import kotlin.math.pow // Üs alma
import kotlin.math.sin // Sinüs
import kotlin.math.sqrt // Karekök

/** RMVPE girdisi: 16 kHz mono PCM -> log-mel. rmvpe_model.py'deki MelSpectrogram ile birebir (n_fft=1024, hop=160, 128 mel, 30-8000 Hz, htk, slaney alan normu, log(max(x,1e-5))). */
object RmvpeMel {
    const val SR = 16000 // Örnekleme hızı
    const val N_FFT = 1024 // Pencere = FFT boyutu
    const val HOP = 160 // 10 ms adım
    const val N_MELS = 128 // Mel bandı sayısı
    private const val F_MIN = 30.0 // En düşük frekans
    private const val F_MAX = 8000.0 // En yüksek frekans
    private const val CLAMP = 1e-5 // Log öncesi alt sınır

    /** [data] mel-major: data[m * frames + t]. */
    class Mel(val frames: Int, val data: FloatArray)

    private val window = DoubleArray(N_FFT) { 0.5 - 0.5 * cos(2.0 * PI * it / N_FFT) } // Periyodik Hann (torch.hann_window)
    private val twCos = DoubleArray(N_FFT / 2) { cos(2.0 * PI * it / N_FFT) } // FFT kosinüs tablosu
    private val twSin = DoubleArray(N_FFT / 2) { sin(2.0 * PI * it / N_FFT) } // FFT sinüs tablosu
    private val rev = IntArray(N_FFT) { Integer.reverse(it) ushr (32 - 10) } // Bit tersleme (log2(1024)=10)
    private val bandStart = IntArray(N_MELS) // Her mel süzgecinin ilk sıfırdan farklı FFT kutusu
    private val bandW: Array<DoubleArray> // Süzgeç ağırlıkları (yalnız sıfırdan farklı aralık)

    init { // librosa.filters.mel(htk=True, norm='slaney') ile aynı süzgeç bankası, çift duyarlıkla
        fun hz2mel(f: Double) = 2595.0 * log10(1.0 + f / 700.0) // HTK mel
        fun mel2hz(m: Double) = 700.0 * (10.0.pow(m / 2595.0) - 1.0) // HTK ters
        val lo = hz2mel(F_MIN); val hi = hz2mel(F_MAX) // Mel uçları
        val pts = DoubleArray(N_MELS + 2) { mel2hz(lo + (hi - lo) * it / (N_MELS + 1)) } // Mel eşit aralıklı nokta frekansları
        val bins = N_FFT / 2 + 1 // 513 kutu
        bandW = Array(N_MELS) { i ->
            val w = DoubleArray(bins) { k ->
                val f = k.toDouble() * SR / N_FFT // Kutunun frekansı
                val lower = (f - pts[i]) / (pts[i + 1] - pts[i]) // Yükselen kenar
                val upper = (pts[i + 2] - f) / (pts[i + 2] - pts[i + 1]) // Alçalan kenar
                max(0.0, min(lower, upper)) * 2.0 / (pts[i + 2] - pts[i]) // Alan normu
            }
            var a = 0; while (a < bins && w[a] == 0.0) a++ // İlk sıfırdan farklı
            var b = bins - 1; while (b > a && w[b] == 0.0) b-- // Son sıfırdan farklı
            bandStart[i] = a // Başlangıç kutusu
            w.copyOfRange(a, if (a >= bins) a else b + 1) // Kısaltılmış ağırlıklar
        }
    }

    /** Yansıtmalı (reflect) dolgu indeksi: i, [0, n) dışına taşarsa torch'un reflect kuralıyla içeri katlanır. */
    internal fun reflectIndex(i: Int, n: Int): Int {
        if (i in 0 until n) return i // İçeride
        if (n < 2) return 0 // Tek örnek: aynısı
        val period = 2 * (n - 1) // Yansıma periyodu
        var k = i % period; if (k < 0) k += period // 0..period-1
        return if (k >= n) period - k else k // Geri katlama
    }

    private fun fft(re: DoubleArray, im: DoubleArray) { // Yerinde radix-2 FFT (çift duyarlık)
        for (i in 0 until N_FFT) { val j = rev[i]; if (j > i) { var t = re[i]; re[i] = re[j]; re[j] = t; t = im[i]; im[i] = im[j]; im[j] = t } } // Bit tersleme
        var len = 2 // Alt dönüşüm uzunluğu
        while (len <= N_FFT) {
            val half = len / 2; val step = N_FFT / len // Yarım uzunluk ve tablo adımı
            var i = 0
            while (i < N_FFT) {
                var k = 0
                for (j in i until i + half) { // Kelebek
                    val wr = twCos[k]; val wi = -twSin[k] // e^{-iθ}
                    val xr = re[j + half]; val xi = im[j + half]
                    val tr = xr * wr - xi * wi; val ti = xr * wi + xi * wr
                    re[j + half] = re[j] - tr; im[j + half] = im[j] - ti
                    re[j] += tr; im[j] += ti
                    k += step
                }
                i += len
            }
            len = len shl 1
        }
    }

    /** [pcm16k] -> log-mel. Kare sayısı 1 + n/160 (center=True). */
    fun compute(pcm16k: FloatArray): Mel {
        val n = pcm16k.size // Örnek sayısı
        val frames = 1 + n / HOP // Kare sayısı
        val out = FloatArray(N_MELS * frames) // Mel-major çıktı
        val re = DoubleArray(N_FFT); val im = DoubleArray(N_FFT); val mag = DoubleArray(N_FFT / 2 + 1) // Çalışma dizileri
        for (t in 0 until frames) {
            val start = t * HOP - N_FFT / 2 // Karenin ilk örneği (center=True: iki yanda 512 yansıma)
            if (start >= 0 && start + N_FFT <= n) for (j in 0 until N_FFT) re[j] = pcm16k[start + j] * window[j] // Hızlı yol
            else for (j in 0 until N_FFT) re[j] = (if (n == 0) 0.0 else pcm16k[reflectIndex(start + j, n)].toDouble()) * window[j] // Kenar: yansıtmalı
            java.util.Arrays.fill(im, 0.0) // Sanal kısım sıfır
            fft(re, im)
            for (k in 0..N_FFT / 2) mag[k] = sqrt(re[k] * re[k] + im[k] * im[k]) // Genlik
            for (m in 0 until N_MELS) {
                val w = bandW[m]; val b = bandStart[m]; var s = 0.0
                for (q in w.indices) s += w[q] * mag[b + q] // Mel süzgeci
                out[m * frames + t] = ln(max(s, CLAMP)).toFloat() // log(max(x, 1e-5))
            }
        }
        return Mel(frames, out)
    }
}
