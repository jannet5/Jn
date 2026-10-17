package com.jn.melodizil.core // Saf Kotlin çekirdek: Android bağımlılığı yok, JVM'de test edilir

import kotlin.math.exp // Üstel
import kotlin.math.ln // Logaritma
import kotlin.math.log10 // Logaritma 10
import kotlin.math.max // En büyük
import kotlin.math.pow // Üs
import kotlin.math.sqrt // Karekök

/**
 * Ayrılmış vokalden melodi: CREPE "tiny" perde modeli (Kim ve ark. 2018; ağırlıklar torchcrepe, MIT) + Viterbi yolu + nota bölme.
 * Neden: değerlendirme setinde doğru perde Melodia ile %67, bu yolla %96; nota doğruluğu 0.53 → 0.60. Model 2 MB.
 * Akış: 16 kHz vokal → 20 ms aralıkla 1024 örneklik kareler → sessiz kareler atlanır (enerji kapısı) → model [N,1024]→[N,360]
 * → softmax gözlem + bantlı geçişle Viterbi (torchcrepe ile aynı) → güven = seçilen kutunun olasılığı → NoteSegmenter.
 * Birebir Python karşılığı: araclar/model/crepe_ref.py (CrepeMelodyTest bununla karşılaştırır).
 */
class CrepeMelody(private val model: FrameModel, private val p: Params = Params()) {

    /** Kareler [count][1024] → olasılıklar [count][360] (satır öncelikli). */
    fun interface FrameModel { fun predict(frames: FloatArray, count: Int, out: FloatArray) }

    fun interface Progress { fun onProgress(fraction: Float) } // İlerleme

    data class Params(
        val hop: Int = 320, // Kare aralığı (örnek, 16 kHz'de 20 ms): 10 ms'ye göre 2 kat hızlı, nota doğruluğu −0.015
        val gateDb: Float = 15f, // Enerji kapısı: %95'lik kare enerjisinin bu kadar dB altı sessiz (model çalışmaz)
        val voiceDb: Float = 12f, // Melodi sayılması için en az enerji (dB, %95'liğe göre)
        val conf: Float = 0.5f, // Melodi sayılması için en az model güveni
        val segment: NoteSegmenter.Params = SEGMENT, // Nota bölme ayarları
    )

    /** Kare başına sonuç (değerlendirme ve test için açık). */
    class Track(val hz: FloatArray, val conf: FloatArray, val energyDb: FloatArray, val frameSec: Float)

    /** 16 kHz mono vokal → melodi. */
    fun extract(pcm: FloatArray, progress: Progress? = null): Melody {
        val tr = track(pcm, progress) // Perde izi
        val midi = FloatArray(tr.hz.size) { i -> if (tr.conf[i] > p.conf && tr.energyDb[i] > -p.voiceDb) hzToMidi(tr.hz[i]) else 0f } // Melodi kareleri
        val notes = NoteSegmenter(p.segment).segment(midi, tr.frameSec, 0f) // Notalar
        return Melody(notes, pcm.size / SR.toFloat()) // Sonuç
    }

    /** 16 kHz mono → kare başına perde, güven ve enerji. */
    fun track(pcm: FloatArray, progress: Progress? = null): Track {
        val hop = p.hop; val n = 1 + pcm.size / hop // Kare sayısı (merkezli)
        val padded = FloatArray(pcm.size + W); System.arraycopy(pcm, 0, padded, W / 2, pcm.size) // İki yandan W/2 sıfır
        val rms = FloatArray(n) { i -> var e = 0.0; val off = i * hop; for (k in 0 until W) { val x = padded[off + k]; e += x * x }; sqrt(e / W).toFloat() } // Kare enerjisi
        val ref = max(percentile95(rms), 1e-9f) // %95'lik enerji
        val edb = FloatArray(n) { (20 * log10(max(rms[it], 1e-9f) / ref)) } // dB
        val run = BooleanArray(n) { p.gateDb <= 0f || edb[it] > -p.gateDb } // Model çalışacak kareler
        val idx = (0 until n).filter { run[it] } // İndeksler
        val prob = ByteArray(n * R) // Aralık içi olasılıklar (0..255; bellek: 4 dk ≈ 3 MB)
        val logObs = FloatArray(n * R) // Viterbi gözlemleri (log softmax)
        val uniform = -ln(R.toFloat()) // Model çalışmayan karede tekdüze gözlem
        java.util.Arrays.fill(logObs, uniform) // Varsayılan
        val frames = FloatArray(BATCH * W); val out = FloatArray(BATCH * NB) // Toplu tamponlar
        var s = 0 // Toplu başlangıcı
        while (s < idx.size) {
            val c = minOf(BATCH, idx.size - s) // Bu toplu
            for (j in 0 until c) normalizeFrame(padded, idx[s + j] * hop, frames, j * W) // Kare + normalizasyon
            model.predict(frames, c, out) // Model
            for (j in 0 until c) { val i = idx[s + j]; val o = j * NB // Her kare
                var mx = Float.NEGATIVE_INFINITY; for (b in 0 until R) mx = max(mx, out[o + LO + b]) // Softmax için en büyük
                var sum = 0.0; for (b in 0 until R) sum += exp((out[o + LO + b] - mx).toDouble()) // Payda
                val lse = mx + ln(sum).toFloat() // log-toplam-üstel
                for (b in 0 until R) { val v = out[o + LO + b]; logObs[i * R + b] = v - lse; prob[i * R + b] = (v.coerceIn(0f, 1f) * 255f + 0.5f).toInt().toByte() } // Gözlem ve olasılık
            }
            s += c; progress?.onProgress(0.9f * s / max(1, idx.size)) // İlerleme
        }
        val path = viterbi(logObs, n) // En olası kutu yolu
        val hz = FloatArray(n); val conf = FloatArray(n) // Sonuçlar
        for (i in 0 until n) { val b = path[i]; hz[i] = (10.0 * 2.0.pow((20.0 * (b + LO) + 1997.3794084376191) / 1200.0)).toFloat(); conf[i] = if (run[i]) (prob[i * R + b].toInt() and 0xFF) / 255f else 0f } // Hz ve güven
        progress?.onProgress(1f) // Bitti
        return Track(hz, conf, edb, hop / SR.toFloat()) // Sonuç
    }

    /** Bantlı (±11 kutu) geçişle Viterbi; librosa.sequence.viterbi ile aynı (başlangıç tekdüze). Geri izler bayt olarak tutulur. */
    private fun viterbi(logObs: FloatArray, n: Int): IntArray {
        var v = FloatArray(R) { logObs[it] - ln(NB.toFloat()) } // İlk kare
        var nv = FloatArray(R) // Sonraki
        val bp = ByteArray(n * R) // Geri izler (−11..11)
        for (t in 1 until n) {
            for (j in 0 until R) { var best = Float.NEGATIVE_INFINITY; var arg = 0 // Her durum
                for (d in -11..11) { val i = j + d; if (i < 0 || i >= R) continue; val c = v[i] + LOGT[d + 11]; if (c > best) { best = c; arg = d } } // En iyi önceki
                nv[j] = best + logObs[t * R + j]; bp[t * R + j] = arg.toByte() }
            val tmp = v; v = nv; nv = tmp // Değiş
        }
        val path = IntArray(n); var b = 0; for (j in 1 until R) if (v[j] > v[b]) b = j; path[n - 1] = b // Son kare
        for (t in n - 1 downTo 1) { path[t - 1] = path[t] + bp[t * R + path[t]]; } // Geri iz
        return path // Sonuç
    }

    /** Kareyi kopyalar, ortalamasını çıkarır, standart sapmasına (n−1) böler (torchcrepe ile aynı). */
    private fun normalizeFrame(src: FloatArray, off: Int, dst: FloatArray, dOff: Int) {
        var m = 0.0; for (k in 0 until W) m += src[off + k]; m /= W // Ortalama
        var v = 0.0; for (k in 0 until W) { val d = src[off + k] - m; v += d * d } // Kareler toplamı
        val sd = max(sqrt(v / (W - 1)), 1e-10) // Standart sapma
        for (k in 0 until W) dst[dOff + k] = ((src[off + k] - m) / sd).toFloat() // Yaz
    }

    private fun percentile95(a: FloatArray): Float { // numpy.percentile (doğrusal ara değer)
        if (a.isEmpty()) return 0f; val s = a.copyOf(); s.sort(); val pos = 0.95 * (s.size - 1); val lo = pos.toInt(); val hi = minOf(lo + 1, s.size - 1)
        return (s[lo] + (s[hi] - s[lo]) * (pos - lo)).toFloat()
    }

    companion object {
        const val SR = 16000 // Model örnekleme hızı
        const val W = 1024 // Kare uzunluğu
        const val NB = 360 // Kutu sayısı (20 cent)
        const val LO = 39 // 50 Hz kutusu
        const val HI = 308 // 1100 Hz kutusu (hariç)
        const val R = HI - LO // Aralıktaki kutu sayısı
        const val BATCH = 256 // Toplu boyutu
        private val LOGT = FloatArray(23) { ln((12 - kotlin.math.abs(it - 11)) / 144.0).toFloat() } // Bantlı geçiş (torchcrepe)
        /** Ölçülmüş nota bölme ayarları (akort kayması düzeltmesi açık). */
        val SEGMENT = NoteSegmenter.Params(tune = true)
        /** Hz → kesirli MIDI. */
        fun hzToMidi(hz: Float): Float = (69.0 + 12.0 * ln(hz / 440.0) / ln(2.0)).toFloat()
    }
}
