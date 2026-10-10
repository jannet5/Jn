package com.jn.melodizil.core // Saf Kotlin çekirdek: Android bağımlılığı yok

import kotlin.math.PI // Pi
import kotlin.math.abs // Mutlak değer
import kotlin.math.cos // Kosinüs
import kotlin.math.exp // Üstel
import kotlin.math.ln // Logaritma
import kotlin.math.max // Büyük
import kotlin.math.min // Küçük
import kotlin.math.pow // Üs
import kotlin.math.roundToInt // Yuvarlama
import kotlin.math.sqrt // Karekök

/**
 * Çok sesli bir kayıttan baskın melodiyi (genelde vokal) çıkarır.
 *
 * Yöntem: Salamon & Gómez (2012) "Melodia" — Essentia'daki PredominantPitchMelodia ile aynı adımlar:
 *  1. Spektral tepeler (Hann 2048, parabolik hassaslaştırma), 40 dB eşik
 *  2. 10 cent çözünürlüklü harmonik toplam perde haritası (salience), cos² yayma, 0.8^(h-1) ağırlık
 *  3. Kare başına perde tepeleri → güçlü/zayıf havuzlar → perde konturları (süreklilik + kısa boşluk köprüsü)
 *  4. Sessizlik ayıklama: ortalama gücü düşük konturlar atılır (vibratolu olanlar korunur)
 *  5. Oktav kopyaları ve aykırı konturlar, 5 sn'lik yumuşatılmış melodi ortalamasına göre elenir (3 tur)
 *  6. Her karede en güçlü kontur seçilir → f0 dizisi → nota bölütleme (medyan filtre + histerezis)
 * Değerlendirme: araclar/degerlendirme (mir_eval, Vocadito + Orchset).
 */
class MelodyExtractor(
    private val sampleRate: Int = 22050, // Analiz örnekleme hızı
    private val frameSize: Int = 2048, // Pencere (~93 ms)
    private val hopSize: Int = 256, // Atlama (~11.6 ms)
    private val p: Params = Params(), // Ayarlar (değerlendirme setiyle seçildi)
) {
    /** Ayarlanabilir değerler; varsayılanlar araclar/degerlendirme ile ölçülerek seçildi. */
    data class Params(
        val harmonics: Int = 10, // Harmonik sayısı (20 ile aynı doğruluk, %40 daha hızlı)
        val voicingTolerance: Double = 0.2, // Sessizlik toleransı (σ); büyüdükçe daha çok kontur kalır
        val frameThreshold: Float = 0.9f, // Kare içi güçlü tepe eşiği
        val distThreshold: Double = 0.9, // Dağılım eşiği (σ)
        val hysteresis: Float = 0.5f, // Nota değişimi eşiği (yarım ton)
        val minNoteSec: Float = 0.045f, // En kısa nota
        val legatoGapSec: Float = 0.12f, // Doldurulan boşluk
        val minDurationMs: Double = 100.0, // En kısa kontur
        val outlierBins: Double = 120.0, // Aykırı eşiği
        val absorbSec: Float = 0.12f, // Kayma/vibrato parçası: bu süreden kısa ve komşusundan ±1 yarım ton farklı nota uzun komşuya katılır (0 = kapalı)
        val selectBy: Int = 0, // Karede kontur seçimi: 0 = toplam güç (Melodia), 1 = ortalama güç, 2 = o karedeki güç
        val meanByFrame: Boolean = true, // Melodi ortalaması: true = kare gücüyle ağırlık (uzun süren akor ortalamayı çekmez), false = toplam güç
        val stepCents: Double = 50.0, // Kontur sürekliliği: kareler arası en büyük perde değişimi (cent/kare). Essentia: 27.56 cent/ms × 2.9 ms = 80
        val upperRatio: Double = 0.0, // Oktav çifti: alttaki ortalama gücü üsttekinin bu katından azsa üstteki tutulur (0 = Essentia: ortalamaya yakın olan)
        val gateDb: Float = 0f, // Enerji kapısı: kare RMS'i, %95'lik RMS'in bu kadar dB altındaysa melodi yok (0 = kapalı; ayrılmış vokalde kullanılır)
    ) {
        companion object {
            /** Ayrılmış vokal için ölçülmüş en iyi ayarlar (değerlendirme: doğru perde %70, nota F 0.50). */
            val VOCAL = Params(voicingTolerance = 0.8, gateDb = 20f)
            /** "anahtar=değer,..." metninden Params (değerlendirme aracı için). */
            fun parse(text: String?): Params { var q = Params(); if (text.isNullOrBlank()) return q
                for (kv in text.split(',')) { val (k, v) = kv.split('=').let { it[0].trim() to it.getOrElse(1) { "" }.trim() }
                    q = when (k) { "harmonics" -> q.copy(harmonics = v.toInt()); "voicing" -> q.copy(voicingTolerance = v.toDouble()); "frame" -> q.copy(frameThreshold = v.toFloat())
                        "dist" -> q.copy(distThreshold = v.toDouble()); "hyst" -> q.copy(hysteresis = v.toFloat()); "minnote" -> q.copy(minNoteSec = v.toFloat()); "legato" -> q.copy(legatoGapSec = v.toFloat())
                        "mindur" -> q.copy(minDurationMs = v.toDouble()); "outlier" -> q.copy(outlierBins = v.toDouble()); "gate" -> q.copy(gateDb = v.toFloat()); "absorb" -> q.copy(absorbSec = v.toFloat()); "select" -> q.copy(selectBy = v.toInt()); "meanframe" -> q.copy(meanByFrame = v == "1"); "step" -> q.copy(stepCents = v.toDouble()); "upper" -> q.copy(upperRatio = v.toDouble()); else -> q } }
                return q }
        }
    }
    private val fft = Fft(frameSize) // FFT
    private val window = FloatArray(frameSize) { (0.5 - 0.5 * cos(2.0 * PI * it / frameSize)).toFloat() } // Hann
    private val hopMs = hopSize * 1000.0 / sampleRate // Kare süresi (ms)

    fun interface Progress { fun onProgress(fraction: Float) } // İlerleme

    /** Bir perde konturu: başlangıç karesi, kare başına perde (cent) ve güç. */
    private class Contour(val start: Int, val pitch: FloatArray, val sal: FloatArray) {
        val end get() = start + pitch.size // Bitiş karesi (hariç)
        val meanPitch = pitch.average().toFloat() // Ortalama perde
        val meanSal = sal.average().toFloat() // Ortalama güç
        val totalSal = sal.sum() // Toplam güç
        val pitchStd = sqrt(pitch.map { (it - meanPitch) * (it - meanPitch) }.average()).toFloat() // Perde sapması
        var vibrato = false // Vibrato var mı (vokal ipucu)
    }

    /** Ana giriş: mono PCM (-1..1) → Melody. */
    fun extract(pcm: FloatArray, progress: Progress? = null): Melody {
        val durationSec = pcm.size.toFloat() / sampleRate // Süre
        if (pcm.size < frameSize * 2) return Melody(emptyList(), durationSec) // Çok kısa
        val f0 = extractF0(pcm, progress) // Kare başına f0 (cent, 0 = sessiz)
        progress?.onProgress(0.95f) // İlerleme
        val notes = segment(f0) // Notalar
        progress?.onProgress(1f) // Bitti
        return Melody(notes, durationSec) // Sonuç
    }

    /** Kare başına melodi perdesi (55 Hz'e göre cent; 0 = melodi yok). Değerlendirme için de açık. */
    fun extractF0(pcm: FloatArray, progress: Progress? = null): FloatArray {
        val n = (pcm.size - frameSize) / hopSize + 1 // Kare sayısı
        val peaksBin = arrayOfNulls<FloatArray>(n); val peaksSal = arrayOfNulls<FloatArray>(n) // Kare başına perde tepeleri
        computeSaliencePeaks(pcm, n, peaksBin, peaksSal) { progress?.onProgress(it * 0.7f) } // Adım 1-3a
        val contours = trackContours(n, peaksBin, peaksSal) // Adım 3b
        progress?.onProgress(0.8f) // İlerleme
        val f0 = selectMelody(n, contours) // Adım 4-6
        if (p.gateDb > 0f) applyEnergyGate(pcm, f0) // Ayrılmış vokalde: sessiz karelerde melodi yok
        return f0 // Sonuç
    }

    /** Enerji kapısı: her karenin RMS'i, şarkının %95'lik RMS'inin gateDb altındaysa o karede melodi yoktur. */
    private fun applyEnergyGate(pcm: FloatArray, f0: FloatArray) {
        val rms = FloatArray(f0.size) { f -> var e = 0.0; val off = f * hopSize; for (i in 0 until frameSize) { val x = pcm[off + i]; e += x * x }; sqrt(e / frameSize).toFloat() } // Kare RMS
        val sorted = rms.copyOf().also { it.sort() }; val ref = sorted[(sorted.size * 0.95).toInt().coerceIn(0, sorted.size - 1)] // %95'lik
        val thr = ref * 10f.pow(-p.gateDb / 20f) // Eşik
        for (t in f0.indices) if (rms[t] < thr) f0[t] = 0f // Kapı
    }

    // ---------------------------------------------------------------- 1-2: tepeler ve perde haritası

    private fun computeSaliencePeaks(pcm: FloatArray, n: Int, outBin: Array<FloatArray?>, outSal: Array<FloatArray?>, progress: (Float) -> Unit) {
        val re = FloatArray(frameSize); val im = FloatArray(frameSize) // FFT tamponları
        val half = frameSize / 2 // Yarım
        val mag = FloatArray(half + 1) // Genlik
        val binHz = sampleRate.toFloat() / frameSize // Bin başına Hz
        val pf = FloatArray(MAX_PEAKS); val pm = FloatArray(MAX_PEAKS) // Spektral tepeler
        val sal = FloatArray(BINS) // Perde haritası
        val harmW = FloatArray(p.harmonics) { ALPHA.pow(it) } // Harmonik ağırlıkları
        val spread = FloatArray(2 * SPREAD + 1) { val d = (it - SPREAD) / SPREAD.toFloat(); val c = cos(d * PI / 2).toFloat(); c * c } // cos² yayma (±1 yarım ton)
        val lo = BINS_MIN; val hi = BINS_MAX // Melodi aralığı (bin)
        for (f in 0 until n) { // Her kare
            val off = f * hopSize // Başlangıç
            for (i in 0 until frameSize) { re[i] = pcm[off + i] * window[i]; im[i] = 0f } // Pencere
            fft.transform(re, im) // FFT
            for (k in 0..half) mag[k] = sqrt(re[k] * re[k] + im[k] * im[k]) // Genlik
            val np = findPeaks(mag, binHz, pf, pm) // Tepeler
            java.util.Arrays.fill(sal, 0f) // Haritayı sıfırla
            for (pk in 0 until np) { // Her tepe
                val a = pm[pk] // Genlik (β = 1)
                for (h in 0 until p.harmonics) { // Bu tepe h. harmonik ise f0'ı oyla
                    val f0 = pf[pk] / (h + 1) // Aday f0
                    if (f0 < REF_HZ) break // Haritanın altında
                    val b = (120.0 * ln(f0 / REF_HZ) / LN2).toFloat() // 10 cent'lik bin (kesirli)
                    val bc = b.roundToInt() // En yakın bin
                    if (bc - SPREAD >= BINS) continue // Haritanın üstünde
                    val w = a * harmW[h] // Ağırlık
                    for (j in -SPREAD..SPREAD) { // ±1 yarım ton yay
                        val i = bc + j; if (i < 0 || i >= BINS) continue // Sınır
                        val d = abs(i - b) / SPREAD // Yarım ton cinsinden uzaklık
                        if (d >= 1f) continue // Yarım tondan uzak
                        val c = cos(d * PI / 2).toFloat() // cos² ağırlığı
                        sal[i] += w * c * c // Oy
                    }
                }
            }
            var cnt = 0; val tb = FloatArray(MAX_SAL_PEAKS); val ts = FloatArray(MAX_SAL_PEAKS) // Haritadaki tepeler
            for (i in max(lo, 1) until min(hi, BINS - 1)) { // Melodi aralığında yerel maksimumlar
                val s = sal[i]; if (s <= 0f || s < sal[i - 1] || s <= sal[i + 1]) continue // Tepe değil
                val a0 = sal[i - 1]; val c0 = sal[i + 1]; val den = a0 - 2 * s + c0 // Parabolik hassaslaştırma
                val delta = if (abs(den) > 1e-12f) 0.5f * (a0 - c0) / den else 0f // Kayma
                if (cnt < MAX_SAL_PEAKS) { tb[cnt] = i + delta; ts[cnt] = s; cnt++ } // Ekle
                else { var w = 0; for (k in 1 until cnt) if (ts[k] < ts[w]) w = k; if (s > ts[w]) { tb[w] = i + delta; ts[w] = s } } // En zayıfla değiştir
            }
            outBin[f] = tb.copyOf(cnt); outSal[f] = ts.copyOf(cnt) // Sakla
            if (f and 127 == 0) progress(f.toFloat() / n) // İlerleme
        }
    }

    /** Spektral tepeler: yerel maksimum, 40 dB eşik, parabolik hassaslaştırma; en güçlü MAX_PEAKS tanesi. */
    private fun findPeaks(mag: FloatArray, binHz: Float, outF: FloatArray, outM: FloatArray): Int {
        val minBin = max(1, (MIN_PEAK_HZ / binHz).toInt()); val maxBin = min(mag.size - 2, (MAX_PEAK_HZ / binHz).toInt()) // Aralık
        var mx = 0f; for (k in minBin..maxBin) if (mag[k] > mx) mx = mag[k] // Maksimum
        if (mx <= 1e-7f) return 0 // Sessiz
        val thr = mx * PEAK_THRESHOLD // 40 dB eşik
        var n = 0 // Sayı
        for (k in minBin..maxBin) { // Her bin
            val m = mag[k]; if (m < thr || m <= mag[k - 1] || m < mag[k + 1]) continue // Tepe değil
            val a = ln(mag[k - 1] + 1e-9f); val b = ln(m + 1e-9f); val c = ln(mag[k + 1] + 1e-9f) // Log genlik
            val den = a - 2 * b + c; val d = if (abs(den) > 1e-9f) 0.5f * (a - c) / den else 0f // Kayma
            val freq = (k + d) * binHz; val amp = exp(b - 0.25f * (a - c) * d) // Hassas frekans ve genlik
            if (n < MAX_PEAKS) { outF[n] = freq; outM[n] = amp; n++ } // Ekle
            else { var w = 0; for (i in 1 until MAX_PEAKS) if (outM[i] < outM[w]) w = i; if (amp > outM[w]) { outF[w] = freq; outM[w] = amp } } // Değiştir
        }
        return n // Tepe sayısı
    }

    // ---------------------------------------------------------------- 3: konturlar

    private fun trackContours(n: Int, pb: Array<FloatArray?>, ps: Array<FloatArray?>): List<Contour> {
        // Güçlü/zayıf havuzlar: kare maksimumunun %90'ının altı zayıf; güçlülerden ortalama - 0.9σ altı da zayıf
        val strong = Array(n) { f -> val s = ps[f]!!; val mx = s.maxOrNull() ?: 0f; BooleanArray(s.size) { s[it] >= p.frameThreshold * mx } } // Kare eşiği
        var sum = 0.0; var sq = 0.0; var cnt = 0 // Güçlü tepelerin dağılımı
        for (f in 0 until n) for (i in ps[f]!!.indices) if (strong[f][i]) { val v = ps[f]!![i].toDouble(); sum += v; sq += v * v; cnt++ } // Topla
        if (cnt == 0) return emptyList() // Hiç tepe yok
        val mean = sum / cnt; val std = sqrt(max(0.0, sq / cnt - mean * mean)) // Ortalama ve sapma
        val distThr = (mean - p.distThreshold * std).toFloat() // Dağılım eşiği
        for (f in 0 until n) for (i in ps[f]!!.indices) if (strong[f][i] && ps[f]!![i] < distThr) strong[f][i] = false // Zayıfa taşı
        val used = Array(n) { BooleanArray(pb[it]!!.size) } // Kullanılan tepeler
        val maxStep = p.stepCents / 10.0 // Kareler arası en büyük sıçrama (bin); nota atlaması tek karede olur, süreye göre ölçeklenmez
        val maxGap = (TIME_CONTINUITY_MS / hopMs).roundToInt() // Köprülenebilir boşluk (kare)
        val minLen = (p.minDurationMs / hopMs).roundToInt() // En kısa kontur (kare)
        // Güçlü tepeleri güce göre sırala: her kontur en güçlü kullanılmamış tepeden başlar
        val order = ArrayList<Long>() // (kare, indeks) paketlenmiş
        for (f in 0 until n) for (i in pb[f]!!.indices) if (strong[f][i]) order.add((f.toLong() shl 20) or i.toLong()) // Ekle
        order.sortByDescending { ps[(it shr 20).toInt()]!![(it and 0xFFFFF).toInt()] } // Güce göre azalan
        val result = ArrayList<Contour>() // Konturlar
        val tmpP = ArrayList<Float>(); val tmpS = ArrayList<Float>(); val tmpStrong = ArrayList<Boolean>() // Geçici
        for (key in order) { // Her başlangıç adayı
            val f0 = (key shr 20).toInt(); val i0 = (key and 0xFFFFF).toInt() // Kare ve indeks
            if (used[f0][i0]) continue // Başka kontura girmiş
            used[f0][i0] = true // Kullanıldı
            val fwd = follow(f0, pb[f0]!![i0], +1, n, pb, ps, strong, used, maxStep, maxGap) // İleri
            val bwd = follow(f0, pb[f0]!![i0], -1, n, pb, ps, strong, used, maxStep, maxGap) // Geri
            tmpP.clear(); tmpS.clear() // Birleştir: geri (ters) + başlangıç + ileri
            for (k in bwd.indices.reversed()) { tmpP.add(bwd[k].first); tmpS.add(bwd[k].second) } // Geri
            tmpP.add(pb[f0]!![i0]); tmpS.add(ps[f0]!![i0]) // Başlangıç
            for (e in fwd) { tmpP.add(e.first); tmpS.add(e.second) } // İleri
            if (tmpP.size < minLen) continue // Çok kısa
            val c = Contour(f0 - bwd.size, tmpP.toFloatArray(), tmpS.toFloatArray()) // Kontur
            c.vibrato = hasVibrato(c.pitch) // Vibrato
            result.add(c) // Ekle
        }
        return result // Konturlar
    }

    /** Bir yönde konturu izler: önce güçlü, yoksa zayıf tepelerle; maxGap'ten uzun zayıf koşu olursa sonundaki zayıf kısım atılır. */
    private fun follow(f0: Int, p0: Float, dir: Int, n: Int, pb: Array<FloatArray?>, ps: Array<FloatArray?>, strong: Array<BooleanArray>, used: Array<BooleanArray>, maxStep: Double, maxGap: Int): List<Pair<Float, Float>> {
        val out = ArrayList<Pair<Float, Float>>(); val idx = ArrayList<Pair<Int, Int>>() // Sonuç ve indeksler
        var last = p0; var f = f0 + dir; var weakRun = 0 // Son perde, kare, zayıf koşu
        while (f in 0 until n) { // Kareler
            val bins = pb[f]!!; var best = -1; var bestD = Double.MAX_VALUE; var bestStrong = false // En yakın aday
            for (i in bins.indices) { if (used[f][i]) continue; val d = abs(bins[i] - last).toDouble(); if (d > maxStep) continue // Süreklilik dışı
                val s = strong[f][i]; if ((s && !bestStrong) || (s == bestStrong && d < bestD)) { best = i; bestD = d; bestStrong = s } } // Güçlüye öncelik
            if (best < 0) break // Devam edemiyor
            weakRun = if (bestStrong) 0 else weakRun + 1 // Zayıf koşu
            if (weakRun > maxGap) break // Boşluk çok uzun
            used[f][best] = true; out.add(bins[best] to ps[f]!![best]); idx.add(f to best); last = bins[best] // Ekle
            f += dir // Sonraki kare
        }
        var trim = 0; for (k in out.indices.reversed()) { if (strong[idx[k].first][idx[k].second]) break; trim++ } // Sondaki zayıf kısım
        for (k in 0 until trim) { val (ff, ii) = idx[out.size - 1 - k]; used[ff][ii] = false } // Serbest bırak
        return out.subList(0, out.size - trim) // Kırpılmış
    }

    /** Vibrato: kontur ≥ 300 ms ise trendi çıkarılmış perdede 5-8 Hz bileşen ve 15-150 cent genlik. */
    private fun hasVibrato(p: FloatArray): Boolean {
        val len = p.size; if (len * hopMs < 300) return false // Kısa
        val mean = p.average().toFloat(); var best = 0.0; val fps = 1000.0 / hopMs // Kare/sn
        var hz = 5.0; while (hz <= 8.0) { var re = 0.0; var im = 0.0 // Tek frekans DFT
            for (k in 0 until len) { val a = 2 * PI * hz * k / fps; re += (p[k] - mean) * cos(a); im += (p[k] - mean) * kotlin.math.sin(a) }
            best = max(best, sqrt(re * re + im * im) * 2 / len); hz += 0.5 } // Genlik (bin=10 cent)
        val extentCents = best * 10 // Cent
        return extentCents in 15.0..150.0 // Vibrato aralığı
    }

    // ---------------------------------------------------------------- 4-6: melodi seçimi

    private fun selectMelody(n: Int, all: List<Contour>): FloatArray {
        if (all.isEmpty()) return FloatArray(n) // Boş
        val ms = all.map { it.meanSal.toDouble() }; val mu = ms.average(); val sd = max(sqrt(ms.map { (it - mu) * (it - mu) }.average()), SD_FLOOR * mu) // Güç dağılımı (sapmaya taban: güçler birbirine çok yakınsa eşik melodiyi kesmesin)
        var cs = all.filter { it.vibrato || it.meanSal >= mu - p.voicingTolerance * sd }.toMutableList() // Sessizlik ayıklama
        repeat(FILTER_ITERATIONS) { // Oktav ve aykırı eleme turları
            var mean = melodyMean(n, cs) // Melodi ortalaması
            val remove = HashSet<Contour>() // Silinecekler
            for (a in cs.indices) for (b in a + 1 until cs.size) { // Oktav kopyaları
                val x = cs[a]; val y = cs[b]; val s = max(x.start, y.start); val e = min(x.end, y.end); if (e - s <= 0) continue // Çakışma yok
                if (e - s < 0.5 * min(x.pitch.size, y.pitch.size)) continue // Az çakışma
                var d = 0.0; for (t in s until e) d += abs(x.pitch[t - x.start] - y.pitch[t - y.start]); d /= (e - s) // Ortalama fark (bin)
                if (abs(d - 120) > 5) continue // Bir oktav (±50 cent) değil
                val (lo, hi) = if (x.meanPitch < y.meanPitch) x to y else y to x // Alt ve üst kontur
                if (p.upperRatio > 0 && lo.meanSal < p.upperRatio * hi.meanSal) { remove.add(lo); continue } // Melodi üstte: alt oktav belirgin güçlü değilse at
                val dx = distToMean(x, mean); val dy = distToMean(y, mean) // Ortalamaya uzaklık
                remove.add(if (dx > dy) x else y) // Uzak olanı at
            }
            cs.removeAll(remove) // Sil
            mean = melodyMean(n, cs) // Yeniden hesapla
            cs = cs.filter { distToMean(it, mean) <= p.outlierBins }.toMutableList() // Aykırı (bir oktavdan uzak) konturları at
        }
        val out = FloatArray(n); val bestSal = FloatArray(n) // Sonuç
        for (c in cs) for (k in c.pitch.indices) { val t = c.start + k // Her karede en güçlü kontur (cent)
            val score = when (p.selectBy) { 1 -> c.meanSal; 2 -> c.sal[k]; else -> c.totalSal } // Seçim ölçütü
            if (score > bestSal[t]) { bestSal[t] = score; out[t] = c.pitch[k] * 10f } }
        for (t in 0 until n) if (bestSal[t] == 0f) out[t] = 0f // Melodi yok
        return out // f0 (55 Hz'e göre cent)
    }

    /** Konturların toplam güçle ağırlıklı ortalama perdesi, 5 sn'lik kayan ortalama ile yumuşatılmış (bin). */
    private fun melodyMean(n: Int, cs: List<Contour>): FloatArray {
        val num = DoubleArray(n); val den = DoubleArray(n) // Pay ve payda
        for (c in cs) for (k in c.pitch.indices) { val t = c.start + k; val w = if (p.meanByFrame) c.sal[k].toDouble() else c.totalSal.toDouble(); num[t] += c.pitch[k] * w; den[t] += w } // Ağırlıklı topla
        val raw = DoubleArray(n) { if (den[it] > 0) num[it] / den[it] else Double.NaN } // Ham ortalama
        var last = raw.firstOrNull { !it.isNaN() } ?: 300.0; for (t in 0 until n) { if (raw[t].isNaN()) raw[t] = last else last = raw[t] } // Boşlukları doldur
        val w = (SMOOTH_MS / hopMs).roundToInt().coerceAtLeast(1); val out = FloatArray(n); var acc = 0.0 // Kayan ortalama
        val pre = DoubleArray(n + 1); for (t in 0 until n) pre[t + 1] = pre[t] + raw[t] // Önek toplamı
        for (t in 0 until n) { val a = max(0, t - w / 2); val b = min(n, t + w / 2 + 1); out[t] = ((pre[b] - pre[a]) / (b - a)).toFloat() } // Ortalama
        return out // Yumuşatılmış ortalama
    }

    private fun distToMean(c: Contour, mean: FloatArray): Double { var d = 0.0; for (k in c.pitch.indices) d += abs(c.pitch[k] - mean[c.start + k]); return d / c.pitch.size } // Ortalama uzaklık (bin)

    // ---------------------------------------------------------------- nota bölütleme

    /** f0 (cent) → notalar: medyan filtre, histerezisli perde değişimi, kısa notaları birleştirme. */
    private fun segment(f0: FloatArray): List<Note> {
        val frameSec = hopSize.toFloat() / sampleRate // Kare süresi
        val midi = FloatArray(f0.size) { if (f0[it] > 0) (MIDI_AT_REF + f0[it] / 100f) else 0f } // Kesirli MIDI
        val sm = FloatArray(midi.size) // Medyan filtrelenmiş
        val half = MEDIAN_FRAMES / 2; val buf = FloatArray(MEDIAN_FRAMES) // Pencere
        for (t in midi.indices) { if (midi[t] == 0f) continue; var c = 0
            for (k in -half..half) { val i = t + k; if (i in midi.indices && midi[i] > 0f) buf[c++] = midi[i] } // Sesli komşular
            java.util.Arrays.sort(buf, 0, c); sm[t] = buf[c / 2] } // Medyan
        val notes = ArrayList<Note>() // Notalar
        val minFrames = (p.minNoteSec / frameSec).roundToInt() // En kısa nota
        var t = 0 // Kare
        while (t < sm.size) { // Sesli koşular
            if (sm[t] == 0f) { t++; continue } // Sessiz
            var start = t; var cur = sm[t].roundToInt(); var pend = -1; var pendStart = 0 // Mevcut nota ve bekleyen değişim
            while (t < sm.size && sm[t] > 0f) { // Koşu boyunca
                val r = sm[t].roundToInt() // En yakın yarım ton
                if (abs(sm[t] - cur) > p.hysteresis) { // Mevcut notadan belirgin uzak
                    if (pend != r) { pend = r; pendStart = t } // Yeni aday
                    if (t - pendStart + 1 >= minFrames) { // Aday yeterince sürdü: nota değiştir
                        if (pendStart > start) notes.add(makeNote(start, pendStart, cur, frameSec)) // Önceki nota
                        start = pendStart; cur = pend; pend = -1 // Yeni nota
                    }
                } else pend = -1 // Aday iptal
                t++ // Sonraki kare
            }
            notes.add(makeNote(start, t, cur, frameSec)) // Koşunun son notası
        }
        // Temizlik: aynı perdede çok yakın notaları birleştir, çok kısa notaları komşuya ekle ya da at
        val merged = ArrayList<Note>() // Birleşmiş
        for (nt in notes) { val l = merged.lastOrNull()
            if (l != null && l.midi == nt.midi && nt.startSec - l.endSec <= MERGE_GAP_SEC) merged[merged.size - 1] = l.copy(durationSec = nt.endSec - l.startSec)
            else merged.add(nt) }
        val clean0 = merged.filter { it.durationSec >= p.minNoteSec } // Çok kısaları at
        val clean = if (p.absorbSec > 0f) absorbGlides(clean0) else clean0 // Kayma/vibrato parçalarını birleştir
        val legato = ArrayList<Note>() // Kısa boşlukları doldur (zil sesinde kesik kesik duyulmasın)
        for ((i, nt) in clean.withIndex()) { val next = clean.getOrNull(i + 1)
            legato.add(if (next != null && next.startSec - nt.endSec in 0f..p.legatoGapSec) nt.copy(durationSec = next.startSec - nt.startSec) else nt) }
        return legato.map { it.copy(velocity = 0.85f) } // Sabit şiddet (perde odaklı)
    }

    /** Kısa (< absorbSec) ve bitişik komşusundan ±1 yarım ton farklı notayı, daha uzun bitişik komşuya katar; aynı perdeli komşular birleşir. */
    private fun absorbGlides(input: List<Note>): List<Note> {
        val ns = input.toMutableList(); var changed = true // Çalışma listesi
        while (changed) { changed = false
            for (i in ns.indices) { val n = ns[i]; if (n.durationSec >= p.absorbSec) continue // Uzun nota: dokunma
                val prev = ns.getOrNull(i - 1)?.takeIf { n.startSec - it.endSec <= MERGE_GAP_SEC && abs(it.midi - n.midi) == 1 } // Bitişik önceki
                val next = ns.getOrNull(i + 1)?.takeIf { it.startSec - n.endSec <= MERGE_GAP_SEC && abs(it.midi - n.midi) == 1 } // Bitişik sonraki
                val host = listOfNotNull(prev, next).maxByOrNull { it.durationSec } ?: continue // Uzun olan komşu
                if (host.durationSec <= n.durationSec) continue // Komşu daha kısa
                val hi = ns.indexOf(host); val s0 = minOf(host.startSec, n.startSec); val e0 = maxOf(host.endSec, n.endSec) // Birleşik aralık
                ns[hi] = host.copy(startSec = s0, durationSec = e0 - s0); ns.removeAt(i); changed = true; break } // Kat ve baştan tara
            if (!changed) { for (i in ns.size - 1 downTo 1) { val a = ns[i - 1]; val b = ns[i]
                if (a.midi == b.midi && b.startSec - a.endSec <= MERGE_GAP_SEC) { ns[i - 1] = a.copy(durationSec = b.endSec - a.startSec); ns.removeAt(i); changed = true } } } } // Aynı perdeleri birleştir
        return ns // Sonuç
    }

    private fun makeNote(s: Int, e: Int, midi: Int, frameSec: Float) = Note(midi, s * frameSec + frameSize / 2f / sampleRate - hopSize.toFloat() / sampleRate / 2, (e - s) * frameSec, 0.85f) // Kare merkezine hizalı nota

    companion object {
        const val SD_FLOOR = 0.15 // Sessizlik eşiğinde sapma tabanı (ortalamanın oranı)
        const val REF_HZ = 55.0 // Perde haritasının taban frekansı (A1)
        const val LN2 = 0.6931471805599453 // ln 2
        const val BINS = 600 // 10 cent × 600 = 5 oktav (55–1760 Hz)
        const val MIDI_AT_REF = 33f // 55 Hz = MIDI 33
        val BINS_MIN = (120.0 * ln(100.0 / REF_HZ) / LN2).toInt() // Melodi alt sınırı ~100 Hz
        val BINS_MAX = (120.0 * ln(1300.0 / REF_HZ) / LN2).toInt() // Melodi üst sınırı ~1300 Hz
        const val SPREAD = 10 // ±1 yarım ton (10 bin)
        const val ALPHA = 0.8f // Harmonik zayıflatma
        const val MAX_PEAKS = 40 // Kare başına spektral tepe
        const val MAX_SAL_PEAKS = 12 // Kare başına perde tepesi
        const val PEAK_THRESHOLD = 0.01f // 40 dB
        const val MIN_PEAK_HZ = 55f // Tepe alt sınırı
        const val MAX_PEAK_HZ = 5000f // Tepe üst sınırı
        const val TIME_CONTINUITY_MS = 100.0 // Köprülenebilir boşluk
        const val FILTER_ITERATIONS = 3 // Eleme turu
        const val SMOOTH_MS = 5000.0 // Melodi ortalaması penceresi
        const val MEDIAN_FRAMES = 5 // Nota medyan filtresi (~58 ms)
        const val MERGE_GAP_SEC = 0.06f // Aynı perdede birleştirme boşluğu (ve kayma birleştirmede bitişiklik)
    }
}
