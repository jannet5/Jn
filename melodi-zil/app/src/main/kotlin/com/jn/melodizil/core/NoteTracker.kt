package com.jn.melodizil.core // Saf Kotlin çekirdek

import kotlin.math.abs // Mutlak değer
import kotlin.math.atan2 // Akort kayması için açı
import kotlin.math.ceil // Yukarı yuvarlama
import kotlin.math.cos // Kosinüs
import kotlin.math.floor // Aşağı yuvarlama
import kotlin.math.log10 // dB hesabı
import kotlin.math.max // Büyük olan
import kotlin.math.min // Küçük olan
import kotlin.math.sin // Sinüs

/**
 * Perde izini temiz notalara çevirir (arastirma/ortak/nota_cikar_v2.py'nin Kotlin portu; ayrıntı arastirma/NOTA_V2.md).
 * Adımlar: kısa boşluk doldur -> akort kayması -> Viterbi (parçalı sabit yarım ton) -> kısa notaları katla -> nota perdesi medyan
 * -> enerji çukurundan böl -> kısa/güvensiz notaları at -> 10 ms kaydır. Ayni perdeli yan yana notalar BİRLEŞTİRİLMEZ (tekrar notaları ayrı kalır).
 */
object NoteTracker {
    private const val LAM = 12.0 // Viterbi'de perde değiştirme cezası
    private const val CAP = 5.0 // Kare maliyeti üst sınırı (yarım ton²)
    private const val MIN_DUR = 0.06 // sn: bundan kısa nota komşuya katılır ya da atılır
    private const val GAP_FILL = 0.02 // sn: bundan kısa sessiz boşluk doldurulur
    private const val GAP_TOL = 1.5 // Boşluğun iki yanı en çok bu kadar yarım ton farklıysa doldurulur
    private const val KAT_TOL = 1.0 // Katılacak komşu en çok 1 yarım ton farklı olabilir
    private const val AKORT_GUC = 0.5 // Akort kaymasının yarısı uygulanır
    private const val BOL_ENJ = 6.0 // dB: enerji çukuru bu kadar derinse nota bölünür
    private const val BOL_PEN = 0.08 // sn: çukurun aranacağı yarım pencere
    private const val BOL_MIN = 0.08 // sn: kesim noktaları arası ve nota kenarına en az uzaklık
    private const val GUVEN_ESIK = 0.5 // Notanın ortalama salience'i bunun altındaysa atılır
    private const val KAYMA = 0.01 // sn: tüm notalar 10 ms ileri alınır
    private const val MIDI_ALT = 30 // Viterbi durum aralığı alt sınır
    private const val MIDI_UST = 100 // Viterbi durum aralığı üst sınır
    private const val SAL_TABAN = 1e-3 // Salience'tan sahte enerji üretirken alt sınır

    private class Nt(var s: Int, var e: Int, var p: Double) // Not: başlangıç kare, bitiş kare (dahil değil), yarım ton durumu

    /** Perde izinden melodi; enerji yoksa salience çukurları (dB gibi) enerji yerine kullanılır. */
    fun track(p: PitchTrack, durationSec: Float): Melody = track(p, durationSec, null) // Sözleşme imzası

    /** [rms] verilirse (kare başına RMS, [p] ile aynı uzunlukta) onset bölme gerçek enerjiyle, yoksa salience ile yapılır. */
    fun track(p: PitchTrack, durationSec: Float, rms: FloatArray?): Melody {
        val n = p.size // Kare sayısı
        if (n < 3) return Melody(emptyList(), durationSec) // Çok kısa izde nota yok
        val hop = Math.round(p.hopSec * 1_000_000.0) / 1_000_000.0 // Kare adımı (0.01 sn) tam değer
        val m = DoubleArray(n) // Karenin MIDI perdesi (sessizde 0)
        val v = BooleanArray(n) // Sesli mi
        for (i in 0 until n) { val f = p.f0[i]; if (f > 0f && f.isFinite()) { v[i] = true; m[i] = hzToMidi(f.toDouble()) } } // Hz -> MIDI
        fillGaps(m, v, Math.round(GAP_FILL / hop).toInt()) // Kısa sessiz boşlukları doldur
        val off = tuningOffset(m, v) * AKORT_GUC // Akort kayması (Viterbi'den ÖNCE, yarım güçle)
        val notes = viterbi(m, v, off) // Parçalı sabit yarım ton notaları
        val md = max(1, Math.round(MIN_DUR / hop).toInt()) // En kısa nota (kare)
        foldShort(notes, md) // Kısa notaları komşuya kat
        for (z in notes) { // Nota perdesi = gerçek karelerin medyanı (akort düzeltilmiş)
            val med = medianVoiced(m, v, z.s, z.e)
            if (!med.isNaN()) z.p = Math.rint(med - off)
        }
        val split = splitByDips(notes, dipSeries(p, rms, n), BOL_ENJ, Math.round(BOL_PEN / hop).toInt(), max(1, Math.round(BOL_MIN / hop).toInt())) // Enerji çukurundan böl
        split.sortBy { it.s } // Zaman sırası
        val out = ArrayList<Note>() // Çıktı
        for (z in split) {
            if (z.e - z.s < md) continue // Çok kısa: at
            var sum = 0.0 // Salience toplamı
            for (i in z.s until z.e) sum += p.salience[i]
            val meanSal = sum / (z.e - z.s) // Ortalama salience
            if (meanSal < GUVEN_ESIK) continue // Güvensiz: at
            val start = max(0.0, z.s * hop - hop / 2) + KAYMA // Başlangıç (sn)
            val end = (min(z.e, n) - 1) * hop + hop / 2 + KAYMA // Bitiş (sn)
            val midi = Math.rint(z.p + off).toInt().coerceIn(0, 127) // Yuvarlanmış MIDI
            val vel = (0.3 + 0.7 * ((meanSal - GUVEN_ESIK) / 0.45).coerceIn(0.0, 1.0)).toFloat() // Şiddet 0.3..1 (salience güveni)
            out.add(Note(midi, start.toFloat(), (end - start).toFloat(), vel)) // Nota
        }
        return Melody(out, durationSec) // Sonuç
    }

    /** Hop'tan kısa (en çok [maxLen] kare) sessiz boşlukları, iki yanı yakın perdeliyse doğrusal ara değerle doldurur. */
    private fun fillGaps(m: DoubleArray, v: BooleanArray, maxLen: Int) {
        val n = v.size; var i = 0
        while (i < n) {
            if (v[i]) { i++; continue }
            var j = i; while (j < n && !v[j]) j++ // Sessiz koşu [i, j)
            if (i > 0 && j < n && (j - i) <= maxLen && abs(m[i - 1] - m[j]) < GAP_TOL) { // Doldurulabilir
                val steps = (j - i + 1).toDouble()
                for (k in i until j) { m[k] = m[i - 1] + (m[j] - m[i - 1]) * (k - i + 1) / steps; v[k] = true } // Doğrusal ara değer
            }
            i = j
        }
    }

    /** Şarkının akort kayması (yarım ton, -0.5..0.5): yuvarlama artıklarının dairesel ortalaması. Az sesli kare varsa 0. */
    private fun tuningOffset(m: DoubleArray, v: BooleanArray): Double {
        var cnt = 0; var ss = 0.0; var cc = 0.0
        for (i in m.indices) if (v[i]) { cnt++; val fr = 2 * Math.PI * (m[i] - Math.rint(m[i])); ss += sin(fr); cc += cos(fr) }
        if (cnt <= 10) return 0.0
        return atan2(ss, cc) / (2 * Math.PI)
    }

    /** Her sesli koşuda Viterbi: durumlar tam yarım tonlar, durum değişimi LAM, kare maliyeti kırpılmış kare fark. */
    private fun viterbi(m: DoubleArray, v: BooleanArray, off: Double): ArrayList<Nt> {
        val n = v.size; val notes = ArrayList<Nt>(); var i = 0
        while (i < n) {
            if (!v[i]) { i++; continue }
            var j = i; while (j < n && v[j]) j++ // Sesli koşu [i, j)
            runViterbi(m, i, j, off, notes)
            i = j
        }
        return notes
    }

    private fun runViterbi(m: DoubleArray, i: Int, j: Int, off: Double, out: MutableList<Nt>) {
        val len = j - i
        if (len == 1) { out.add(Nt(i, j, Math.rint(m[i] - off))); return } // Tek kare
        var mn = Double.MAX_VALUE; var mx = -Double.MAX_VALUE
        for (k in i until j) { val x = m[k] - off; if (x < mn) mn = x; if (x > mx) mx = x }
        val lo = max(MIDI_ALT, floor(mn).toInt() - 1); val hi = min(MIDI_UST, ceil(mx).toInt() + 1) // Durum aralığı
        if (lo > hi) return // Aralık dışı (çok pes/tiz): nota yok
        val ns = hi - lo + 1 // Durum sayısı
        var prev = DoubleArray(ns); var cur = DoubleArray(ns)
        val bp = ByteArray(len * ns) // Geri izleme (durum < 127)
        val x0 = m[i] - off
        for (s in 0 until ns) { val d = x0 - (lo + s); prev[s] = min(d * d, CAP) } // İlk kare maliyeti
        for (k in 1 until len) {
            var bprev = 0; var best = prev[0]
            for (s in 1 until ns) if (prev[s] < best) { best = prev[s]; bprev = s } // En iyi önceki durum (ilk en küçük)
            val bmin = best + LAM // Herhangi bir durumdan sıçrama maliyeti
            val x = m[i + k] - off; val base = k * ns
            for (s in 0 until ns) {
                val d = x - (lo + s); val c = min(d * d, CAP); val kal = prev[s]
                if (kal <= bmin) { cur[s] = c + kal; bp[base + s] = s.toByte() } else { cur[s] = c + bmin; bp[base + s] = bprev.toByte() } // Kal ya da sıçra
            }
            val t = prev; prev = cur; cur = t // Takas
        }
        val path = IntArray(len)
        var bl = 0; var bc = prev[0]
        for (s in 1 until ns) if (prev[s] < bc) { bc = prev[s]; bl = s } // Son karenin en iyi durumu
        path[len - 1] = bl
        for (k in len - 1 downTo 1) path[k - 1] = bp[k * ns + path[k]].toInt() // Geri izleme
        var a = 0
        for (k in 1..len) if (k == len || path[k] != path[a]) { out.add(Nt(i + a, i + k, (lo + path[a]).toDouble())); a = k } // Durum değişince nota
    }

    /** [md] kareden kısa notayı, bitişik ve en çok KAT_TOL yarım ton farklı komşuya katar; komşu yoksa notayı siler. */
    private fun foldShort(notes: ArrayList<Nt>, md: Int) {
        var k = 0
        while (k < notes.size) {
            val z = notes[k]
            if (z.e - z.s >= md) { k++; continue } // Yeterince uzun
            val prev = if (k > 0 && notes[k - 1].e == z.s) notes[k - 1] else null // Bitişik önceki
            val nxt = if (k + 1 < notes.size && notes[k + 1].s == z.e) notes[k + 1] else null // Bitişik sonraki
            var tgt: Nt? = null; var tgtPrev = false
            if (prev != null && abs(prev.p - z.p) <= KAT_TOL) { tgt = prev; tgtPrev = true } // Önceki aday
            if (nxt != null && abs(nxt.p - z.p) <= KAT_TOL && (tgt == null || abs(nxt.p - z.p) < abs(tgt.p - z.p))) { tgt = nxt; tgtPrev = false } // Sonraki daha yakınsa
            if (tgt != null) { if (tgtPrev) tgt.e = z.e else tgt.s = z.s } // Komşuyu uzat
            notes.removeAt(k) // Kısa notayı sil (aynı indekste devam)
        }
    }

    /** [s, e) aralığındaki sesli karelerin MIDI medyanı; sesli kare yoksa NaN. */
    private fun medianVoiced(m: DoubleArray, v: BooleanArray, s: Int, e: Int): Double {
        var c = 0; for (i in s until e) if (v[i]) c++
        if (c == 0) return Double.NaN
        val a = DoubleArray(c); var k = 0
        for (i in s until e) if (v[i]) a[k++] = m[i]
        a.sort()
        return if (c % 2 == 1) a[c / 2] else (a[c / 2 - 1] + a[c / 2]) / 2.0
    }

    /** Onset bölme için dB serisi: RMS varsa onun, yoksa salience'ın 20*log10'u; 3 noktalı hareketli ortalama (uçlar kenar değeriyle). */
    private fun dipSeries(p: PitchTrack, rms: FloatArray?, n: Int): DoubleArray {
        val raw = DoubleArray(n)
        if (rms != null && rms.size == n) for (i in 0 until n) raw[i] = 20 * log10(rms[i].toDouble() + 1e-5)
        else for (i in 0 until n) raw[i] = 20 * log10(max(p.salience[i].toDouble(), SAL_TABAN) + 1e-5)
        val x = DoubleArray(n)
        for (i in 0 until n) x[i] = (raw[max(i - 1, 0)] + raw[i] + raw[min(i + 1, n - 1)]) / 3.0
        return x
    }

    /** Notanın içindeki belirgin (>= [esik] dB) yerel enerji çukurlarında notayı böler (aynı perde, ayrı notalar). */
    private fun splitByDips(notes: List<Nt>, x: DoubleArray, esik: Double, pen: Int, minp: Int): ArrayList<Nt> {
        val out = ArrayList<Nt>(notes.size + 16)
        val cand = ArrayList<Pair<Double, Int>>()
        for (z in notes) {
            cand.clear()
            for (i in z.s + minp..z.e - minp) {
                val lo = max(z.s, i - pen); val hi = min(z.e, i + pen + 1)
                var wmin = Double.MAX_VALUE; for (k in lo until hi) if (x[k] < wmin) wmin = x[k]
                if (x[i] > wmin + 1e-12) continue // Yerel en küçük değil
                var sol = -Double.MAX_VALUE; for (k in lo..i) if (x[k] > sol) sol = x[k]
                var sag = -Double.MAX_VALUE; for (k in i until hi) if (x[k] > sag) sag = x[k]
                val pr = min(sol, sag) - x[i] // Çukur derinliği
                if (pr >= esik) cand.add(Pair(pr, i))
            }
            if (cand.isEmpty()) { out.add(Nt(z.s, z.e, z.p)); continue }
            cand.sortWith(compareByDescending<Pair<Double, Int>> { it.first }.thenByDescending { it.second }) // En derin önce
            val cuts = ArrayList<Int>()
            for ((_, i) in cand) if (cuts.all { abs(i - it) >= minp }) cuts.add(i) // Birbirine yeterince uzak kesimler
            cuts.sort()
            var a = z.s
            for (c in cuts) { out.add(Nt(a, c, z.p)); a = c }
            out.add(Nt(a, z.e, z.p))
        }
        return out
    }
}
