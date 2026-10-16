package com.jn.melodizil.core // Saf Kotlin çekirdek: Android bağımlılığı yok

import kotlin.math.PI // Pi
import kotlin.math.abs // Mutlak değer
import kotlin.math.cos // Kosinüs
import kotlin.math.exp // Üstel
import kotlin.math.ln // Logaritma
import kotlin.math.log10 // 10 tabanlı logaritma
import kotlin.math.max // Büyük olan
import kotlin.math.min // Küçük olan
import kotlin.math.pow // Üs
import kotlin.math.roundToInt // Yuvarlama
import kotlin.math.sqrt // Karekök

/**
 * Çok sesli (vokal + enstrüman) bir kayıttan baskın melodiyi çıkarır.
 *
 * Yöntem (Salamon & Gómez "Melodia" yaklaşımının sadeleştirilmiş hali):
 *  1. STFT (Hann, 2048, atlama 512) ve spektral tepe bulma (parabolik hassaslaştırma)
 *  2. Harmonik toplamla perde belirginliği (salience): her tepe, kendisinin 1..H alt harmoniği olan f0 adaylarını oylar
 *  3. Yarım ton çözünürlüğünde Viterbi: süreklilik ödüllendirilir, sıçramalar ve "sessiz" durumu cezalandırılır
 *  4. Nota bölütleme: aynı perdedeki ardışık çerçeveler notaya dönüşür, çok kısa notalar birleştirilir/atılır
 */
class EskiMelodyExtractor(
    private val sampleRate: Int = 22050, // Analiz örnekleme hızı
    private val frameSize: Int = 2048, // Pencere boyutu (~93 ms)
    private val hopSize: Int = 512, // Atlama (~23 ms)
    private val minMidi: Int = 40, // En düşük aday perde (E2 ≈ 82 Hz)
    private val maxMidi: Int = 88, // En yüksek aday perde (E6 ≈ 1319 Hz)
) {
    private val fft = Fft(frameSize) // Yeniden kullanılan FFT
    private val window = FloatArray(frameSize) { (0.5 - 0.5 * cos(2.0 * PI * it / frameSize)).toFloat() } // Hann penceresi
    private val numStates = maxMidi - minMidi + 1 // Perde durumu sayısı
    private val unvoiced = numStates // Ek durum: sessiz / melodi yok

    /** İlerleme geri bildirimi: 0..1 arası. */
    fun interface Progress { fun onProgress(fraction: Float) }

    /** Ana giriş noktası: mono PCM (-1..1) → Melody. */
    fun extract(pcm: FloatArray, progress: Progress? = null): Melody {
        val durationSec = pcm.size.toFloat() / sampleRate // Toplam süre
        if (pcm.size < frameSize * 2) return Melody(emptyList(), durationSec) // Çok kısa giriş
        val numFrames = (pcm.size - frameSize) / hopSize + 1 // Çerçeve sayısı
        val salience = Array(numFrames) { FloatArray(numStates) } // Çerçeve × perde belirginlik matrisi
        val frameEnergy = FloatArray(numFrames) // Çerçeve RMS'i
        computeSalience(pcm, numFrames, salience, frameEnergy) { progress?.onProgress(it * 0.75f) } // Adım 1-2: belirginlik
        val path = viterbi(salience, frameEnergy) // Adım 3: en olası perde yolu
        progress?.onProgress(0.9f) // İlerleme
        val notes = segment(path, salience, numFrames) // Adım 4: notalar
        progress?.onProgress(1f) // Bitti
        return Melody(notes, durationSec) // Sonuç
    }

    // ---------------------------------------------------------------- Adım 1-2: STFT + harmonik toplam

    private fun computeSalience(
        pcm: FloatArray, numFrames: Int, salience: Array<FloatArray>, frameEnergy: FloatArray, progress: (Float) -> Unit,
    ) {
        val re = FloatArray(frameSize); val im = FloatArray(frameSize) // FFT tamponları
        val half = frameSize / 2 // Nyquist'e kadar olan bin sayısı
        val mag = FloatArray(half + 1) // Genlik spektrumu
        val binHz = sampleRate.toFloat() / frameSize // Bin başına Hz
        val peakFreq = FloatArray(MAX_PEAKS); val peakMag = FloatArray(MAX_PEAKS) // Tepe listesi
        val rangeWeight = FloatArray(numStates) { s -> // Melodi aralığı önceliği: insan sesi/baskın melodi bölgesi vurgulanır
            val midi = minMidi + s // Durumun MIDI'si
            val d = (midi - RANGE_CENTER_MIDI) / RANGE_SIGMA // Merkezden uzaklık
            (RANGE_FLOOR + (1 - RANGE_FLOOR) * exp(-0.5 * d * d)).toFloat() // Çan eğrisi, tabanı RANGE_FLOOR
        }
        for (f in 0 until numFrames) { // Her çerçeve
            val off = f * hopSize // Çerçevenin başlangıç örneği
            var energy = 0f // RMS toplamı
            for (i in 0 until frameSize) { // Pencereleme
                val x = pcm[off + i] // Örnek
                energy += x * x // Enerji
                re[i] = x * window[i]; im[i] = 0f // Pencerelenmiş gerçek kısım, sanal sıfır
            }
            frameEnergy[f] = sqrt(energy / frameSize) // RMS
            fft.transform(re, im) // Spektrum
            for (k in 0..half) mag[k] = sqrt(re[k] * re[k] + im[k] * im[k]) // Genlikler
            val nPeaks = findPeaks(mag, binHz, peakFreq, peakMag) // Spektral tepeler
            val sal = salience[f] // Bu çerçevenin belirginlik dizisi
            for (p in 0 until nPeaks) { // Her tepe için
                val fp = peakFreq[p]; val mp = peakMag[p] // Tepe frekansı ve genliği
                for (h in 1..NUM_HARMONICS) { // Tepe, h. harmoniği olduğu f0'ı oylar
                    val f0 = fp / h // Aday temel frekans
                    if (f0 < 20f) break // Çok düşük, daha yüksek h anlamsız
                    val midi = hzToMidi(f0.toDouble()) // Kesirli MIDI
                    val center = midi.roundToInt() // En yakın yarım ton
                    val dev = abs(midi - center) // Yarım tona uzaklık (0..0.5)
                    if (center < minMidi || center > maxMidi || dev > 0.35) continue // Aralık dışı ya da yarım tonlar arasında kararsız
                    val closeness = cos(dev * PI).toFloat() // Yarım tonun merkezine yakınlık ağırlığı (1 → 0)
                    val weight = HARMONIC_ALPHA.pow(h - 1) * closeness // Harmonik zayıflatma × yakınlık
                    sal[center - minMidi] += mp * weight // Oy ekleniyor
                }
            }
            for (s in 0 until numStates) sal[s] *= rangeWeight[s] // Aralık önceliği uygulanıyor
            if (f and 63 == 0) progress(f.toFloat() / numFrames) // Her 64 çerçevede ilerleme
        }
    }

    /** Spektral tepeleri bulur: yerel maksimum, gürültü tabanının üstünde, parabolik hassaslaştırma. */
    private fun findPeaks(mag: FloatArray, binHz: Float, outFreq: FloatArray, outMag: FloatArray): Int {
        var maxMag = 0f // Çerçevedeki en büyük genlik
        for (k in 1 until mag.size - 1) if (mag[k] > maxMag) maxMag = mag[k] // Maksimum aranıyor
        if (maxMag <= 1e-6f) return 0 // Sessiz çerçeve
        val threshold = maxMag * PEAK_REL_THRESHOLD // Zayıf tepeler atılır
        val minBin = max(1, (MIN_PEAK_HZ / binHz).toInt()); val maxBin = min(mag.size - 2, (MAX_PEAK_HZ / binHz).toInt()) // Aranacak bin aralığı
        var n = 0 // Bulunan tepe sayısı
        for (k in minBin..maxBin) { // Her bin
            val m = mag[k] // Genlik
            if (m < threshold || m <= mag[k - 1] || m < mag[k + 1]) continue // Yerel maksimum değil ya da zayıf
            val a = ln(mag[k - 1] + 1e-9f); val b = ln(m + 1e-9f); val c = ln(mag[k + 1] + 1e-9f) // Log genlikler
            val denom = a - 2 * b + c // Parabol eğriliği
            val delta = if (abs(denom) > 1e-9f) 0.5f * (a - c) / denom else 0f // Tepenin bin merkezinden kayması
            val freq = (k + delta) * binHz // Hassas frekans
            val refined = exp(b - 0.25f * (a - c) * delta) // Hassas genlik
            if (n < MAX_PEAKS) { outFreq[n] = freq; outMag[n] = refined; n++ } // Yer varsa ekle
            else { // Yer yoksa en zayıf olanla değiştir
                var weakest = 0 // En zayıf indeks
                for (i in 1 until MAX_PEAKS) if (outMag[i] < outMag[weakest]) weakest = i // Aranıyor
                if (refined > outMag[weakest]) { outFreq[weakest] = freq; outMag[weakest] = refined } // Daha güçlüyse yer değiştir
            }
        }
        return n // Tepe sayısı
    }

    // ---------------------------------------------------------------- Adım 3: Viterbi

    /** Her çerçeve için en olası durumu (perde ya da sessiz) döndürür. */
    private fun viterbi(salience: Array<FloatArray>, frameEnergy: FloatArray): IntArray {
        val n = salience.size // Çerçeve sayısı
        val frameMax = FloatArray(n) { f -> salience[f].maxOrNull() ?: 0f } // Çerçeve başına en güçlü belirginlik
        val sorted = frameMax.copyOf().also { it.sort() } // Referans için sıralı kopya
        val ref = max(sorted[(n * 0.85).toInt().coerceIn(0, n - 1)], 1e-6f) // Şarkının "güçlü" belirginlik referansı (85. yüzdelik)
        val energySorted = frameEnergy.copyOf().also { it.sort() } // Enerji referansı
        val energyRef = max(energySorted[(n * 0.9).toInt().coerceIn(0, n - 1)], 1e-6f) // 90. yüzdelik enerji
        val logEmit = Array(n) { FloatArray(numStates + 1) } // Log emisyon olasılıkları
        for (f in 0 until n) { // Her çerçeve
            val sal = salience[f] // Belirginlikler
            val strength = (frameMax[f] / ref).coerceIn(0f, 1f) // Melodi gücü 0..1
            val quiet = (frameEnergy[f] / energyRef).coerceIn(0f, 1f) // Çerçeve ne kadar sesli
            val pUnvoiced = (1f - strength * VOICING_GAIN).coerceIn(0.02f, 0.98f).let { if (quiet < SILENCE_RATIO) 0.98f else it } // Sessiz olasılığı
            var sum = 0f // Normalizasyon toplamı
            for (s in 0 until numStates) sum += sal[s] // Toplam belirginlik
            val inv = if (sum > 0f) (1f - pUnvoiced) / sum else 0f // Perde durumlarına dağıtılacak olasılık
            val e = logEmit[f] // Bu çerçevenin emisyonları
            for (s in 0 until numStates) e[s] = ln(sal[s] * inv + 1e-7f) // Perde emisyonu
            e[unvoiced] = ln(pUnvoiced + 1e-7f) // Sessiz emisyonu
        }
        val numAll = numStates + 1 // Toplam durum
        val score = FloatArray(numAll) { logEmit[0][it] } // İlk çerçeve skorları
        val back = Array(n) { IntArray(numAll) } // Geri iz
        val next = FloatArray(numAll) // Geçici skorlar
        val jumpCost = FloatArray(numStates) { d -> min(JUMP_COST_PER_SEMITONE * d, MAX_JUMP_COST) } // Aralık cezası tablosu
        for (f in 1 until n) { // Her çerçeve
            val e = logEmit[f] // Emisyonlar
            for (s in 0 until numStates) { // Hedef perde durumu
                var best = score[unvoiced] - UNVOICED_TO_VOICED_COST; var arg = unvoiced // Sessizden gelme
                for (p in 0 until numStates) { // Kaynak perde
                    val v = score[p] - jumpCost[abs(p - s)] // Geçiş skoru
                    if (v > best) { best = v; arg = p } // Daha iyiyse güncelle
                }
                next[s] = best + e[s]; back[f][s] = arg // Skor ve geri iz
            }
            var bestU = score[unvoiced]; var argU = unvoiced // Sessizde kalma
            for (p in 0 until numStates) { // Perdeden sessize geçiş
                val v = score[p] - VOICED_TO_UNVOICED_COST // Geçiş skoru
                if (v > bestU) { bestU = v; argU = p } // Daha iyiyse güncelle
            }
            next[unvoiced] = bestU + e[unvoiced]; back[f][unvoiced] = argU // Sessiz durumu
            System.arraycopy(next, 0, score, 0, numAll) // Skorlar güncelleniyor
        }
        val path = IntArray(n) // Sonuç yolu
        var s = 0; for (i in 1 until numAll) if (score[i] > score[s]) s = i // Son çerçevede en iyi durum
        for (f in n - 1 downTo 0) { path[f] = s; s = back[f][s] } // Geri izleme
        return path // Yol
    }

    // ---------------------------------------------------------------- Adım 4: nota bölütleme

    private fun segment(path: IntArray, salience: Array<FloatArray>, numFrames: Int): List<Note> {
        val frameSec = hopSize.toFloat() / sampleRate // Çerçeve süresi
        val raw = ArrayList<Note>() // Ham notalar
        var start = 0 // Mevcut koşunun başlangıcı
        var salSum = 0f // Koşu boyunca belirginlik toplamı
        for (f in 0..numFrames) { // Son çerçeveden sonra kapanış için bir fazla
            val cur = if (f < numFrames) path[f] else -1 // Mevcut durum (-1 = bitiş)
            val prev = path[start] // Koşunun durumu
            if (f < numFrames && cur == prev) { salSum += if (prev != unvoiced) salience[f][prev] else 0f; continue } // Koşu sürüyor
            if (prev != unvoiced) { // Koşu bir perdeyse nota üret
                val len = f - start // Çerçeve sayısı
                raw.add(Note(minMidi + prev, start * frameSec, len * frameSec, salSum / len)) // Ham nota
            }
            start = f; salSum = if (f < numFrames && cur != unvoiced) salience[f][cur] else 0f // Yeni koşu
        }
        if (raw.isEmpty()) return emptyList() // Hiç nota yok
        val maxVel = raw.maxOf { it.velocity }.coerceAtLeast(1e-6f) // Şiddet normalizasyonu için
        val merged = ArrayList<Note>() // Birleştirilmiş notalar
        for (note in raw) { // Komşu aynı perdeli notaları küçük boşluklarla birleştir
            val last = merged.lastOrNull() // Önceki nota
            if (last != null && last.midi == note.midi && note.startSec - last.endSec <= MERGE_GAP_SEC) { // Aynı perde, küçük boşluk
                merged[merged.size - 1] = last.copy(durationSec = note.endSec - last.startSec, velocity = max(last.velocity, note.velocity)) // Uzat
            } else merged.add(note) // Yeni nota
        }
        val cleaned = ArrayList<Note>() // Kısa notaları süzülmüş liste
        for ((i, note) in merged.withIndex()) { // Her nota
            if (note.durationSec >= MIN_NOTE_SEC) { cleaned.add(note); continue } // Yeterince uzun
            val prev = cleaned.lastOrNull(); val next = merged.getOrNull(i + 1) // Komşular
            if (prev != null && next != null && prev.midi == next.midi && next.startSec - prev.endSec <= MIN_NOTE_SEC * 2) continue // İki aynı nota arasında sıçrama: at (sonraki birleştirme uzatır)
            if (prev != null && note.startSec - prev.endSec <= MERGE_GAP_SEC && abs(prev.midi - note.midi) <= 2) { // Küçük komşu kayması: öncekine ekle
                cleaned[cleaned.size - 1] = prev.copy(durationSec = note.endSec - prev.startSec); continue // Önceki uzatıldı
            }
            // Aksi halde kısa nota atılır (gürültü)
        }
        val result = ArrayList<Note>() // Son geçiş: at işleminden sonra oluşan aynı perdeli boşlukları birleştir
        for (note in cleaned) { // Her nota
            val last = result.lastOrNull() // Önceki
            if (last != null && last.midi == note.midi && note.startSec - last.endSec <= MIN_NOTE_SEC * 2) { // Aynı perde yakın
                result[result.size - 1] = last.copy(durationSec = note.endSec - last.startSec, velocity = max(last.velocity, note.velocity)) // Birleştir
            } else result.add(note) // Ekle
        }
        return result.map { it.copy(velocity = (0.35f + 0.65f * (it.velocity / maxVel)).coerceIn(0f, 1f)) } // Şiddet 0.35..1 aralığına
    }

    companion object {
        const val MAX_PEAKS = 40 // Çerçeve başına en fazla tepe
        const val NUM_HARMONICS = 10 // Harmonik toplamda kullanılan harmonik sayısı
        const val HARMONIC_ALPHA = 0.8f // Yüksek harmoniklerin zayıflatma katsayısı
        const val PEAK_REL_THRESHOLD = 0.01f // Tepe eşiği: çerçeve maksimumunun %1'i (-40 dB)
        const val MIN_PEAK_HZ = 60f // Tepe aramasının alt sınırı
        const val MAX_PEAK_HZ = 5000f // Tepe aramasının üst sınırı
        const val RANGE_CENTER_MIDI = 64.0 // Melodi aralığı önceliğinin merkezi (E4)
        const val RANGE_SIGMA = 12.0 // Önceliğin genişliği (yarım ton)
        const val RANGE_FLOOR = 0.35 // Aralık dışındaki adayların en düşük ağırlığı
        const val VOICING_GAIN = 1.6f // Melodi gücünün sesli olasılığına katkısı
        const val SILENCE_RATIO = 0.08f // Bu enerji oranının altı kesin sessiz
        const val JUMP_COST_PER_SEMITONE = 0.35f // Perde sıçrama cezası (yarım ton başına)
        const val MAX_JUMP_COST = 4.5f // Sıçrama cezası üst sınırı (oktav atlamaları mümkün kalır)
        const val VOICED_TO_UNVOICED_COST = 2.2f // Perdeden sessize geçiş cezası
        const val UNVOICED_TO_VOICED_COST = 2.2f // Sessizden perdeye geçiş cezası
        const val MIN_NOTE_SEC = 0.09f // En kısa nota (~4 çerçeve)
        const val MERGE_GAP_SEC = 0.06f // Aynı perdeli notalar arası birleştirme boşluğu
    }
}
