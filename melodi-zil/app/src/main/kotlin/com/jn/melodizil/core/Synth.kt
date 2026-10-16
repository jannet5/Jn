package com.jn.melodizil.core // Saf Kotlin çekirdek

import kotlin.math.PI // Pi
import kotlin.math.abs // Mutlak değer
import kotlin.math.exp // Üstel
import kotlin.math.max // Büyük
import kotlin.math.min // Küçük
import kotlin.math.pow // Üs
import kotlin.math.sin // Sinüs
import kotlin.math.tanh // Yumuşak sınırlayıcı
import kotlin.random.Random // Gürültü üretimi

/**
 * Nota listesini seçilen enstrüman tınısıyla PCM'e dönüştürür.
 * Tüm tınılar toplamsal/basit fiziksel modelle üretilir; dış ses dosyası gerekmez.
 */
class Synth(private val sampleRate: Int = 44100) {

    /** Bir pencereyi (startSec..startSec+lengthSec) seçilen enstrümanla çalar; -1..1 arası mono PCM döner. */
    fun render(melody: Melody, instrument: Instrument, startSec: Float, lengthSec: Float): FloatArray {
        val total = (lengthSec * sampleRate).toInt() // Çıkış örnek sayısı
        val out = FloatArray(total) // Çıkış tamponu
        val endSec = startSec + lengthSec // Pencere bitişi
        val tail = RELEASE_TAIL_SEC // Pencere bitiminden sonra çınlayan kuyruk payı
        for (note in melody.notes) { // Her nota
            if (note.endSec < startSec || note.startSec > endSec) continue // Pencere dışı
            val relStart = note.startSec - startSec // Pencereye göre başlangıç
            val playDur = min(note.durationSec, endSec + tail - note.startSec) // Pencereyi çok aşmasın
            renderNote(out, instrument, note.midi + instrument.transpose, relStart, playDur, note.velocity) // Nota ekleniyor
        }
        finalize(out) // Fade, sınırlama ve normalizasyon
        return out // PCM
    }

    /** Tek bir notayı tampona ekler (toplar). */
    private fun renderNote(out: FloatArray, inst: Instrument, midi: Int, startSec: Float, durSec: Float, vel: Float) {
        val f0 = midiToHz(midi) // Temel frekans
        val startIdx = (startSec * sampleRate).toInt() // Başlangıç örneği
        val ringSec = ringTime(inst, midi) // Nota bırakıldıktan sonra çınlama süresi
        val totalSec = durSec + ringSec // Notanın toplam ses süresi
        val n = (totalSec * sampleRate).toInt() // Örnek sayısı
        val amp = 0.25f * (0.5f + 0.5f * vel) // Genlik: şiddete göre
        when (inst) { // Enstrümana göre üretim
            Instrument.GITAR -> karplusStrong(out, startIdx, n, f0, durSec, amp) // Fiziksel model
            else -> additive(out, startIdx, n, f0, durSec, amp, inst) // Toplamsal sentez
        }
    }

    /** Toplamsal sentez: her enstrüman için kısmi (partial) tablosu, zarf ve efektler. */
    private fun additive(out: FloatArray, startIdx: Int, n: Int, f0: Float, heldSec: Float, amp: Float, inst: Instrument) {
        val partials = partials(inst) // (frekans oranı, genlik, bozunma hız çarpanı) listesi
        val env = envelope(inst) // Zarf parametreleri
        val nyquist = sampleRate / 2f // Nyquist frekansı
        val dt = 1f / sampleRate // Örnek süresi
        val vibrato = inst == Instrument.FLUT || inst == Instrument.SYNTH // Vibrato uygulanan tınılar
        val detune = if (inst == Instrument.SYNTH) 3 else 1 // Synth için 3 hafif akortsuz ses
        for (i in 0 until n) { // Her örnek
            val idx = startIdx + i // Çıkış indeksi
            if (idx >= out.size) break // Tampon bitti
            if (idx < 0) continue // Negatif başlangıç
            val t = i * dt // Nota içi zaman
            var e = if (t < env.attack) t / env.attack else { // Atak
                val d = exp(-(t - env.attack) / env.decayTau) // Bozunma eğrisi
                env.sustain + (1 - env.sustain) * d // Sürdürme seviyesine iner
            }
            if (t > heldSec) e *= exp(-(t - heldSec) / env.releaseTau) // Bırakma (release)
            if (e < 1e-4f) continue // Duyulmaz, hesaplama
            var s = 0f // Örnek değeri
            val vib = if (vibrato && t > 0.15f) 1f + 0.004f * sin((2 * PI * 5.2 * t).toFloat()) * min(1f, (t - 0.15f) * 4) else 1f // Vibrato oranı
            for (v in 0 until detune) { // Akortsuz katmanlar
                val det = if (detune == 1) 1f else 1f + (v - 1) * 0.004f // ±0.4% akortsuzluk
                for (p in partials) { // Her kısmi
                    val freq = f0 * p.ratio * vib * det // Kısmi frekansı
                    if (freq >= nyquist) continue // Alias önleme
                    val pd = if (p.decayMul > 0f) exp(-t * p.decayMul) else 1f // Kısminin kendi bozunması (yüksekler daha hızlı söner)
                    s += p.amp * pd * sin((2 * PI * freq * t).toFloat()) // Sinüs toplanıyor
                }
            }
            if (inst == Instrument.FLUT) s += 0.015f * noise() * e // Nefes gürültüsü
            out[idx] += s * e * amp / detune // Çıkışa ekleniyor
        }
    }

    /** Karplus-Strong tele vurma modeli (gitar). */
    private fun karplusStrong(out: FloatArray, startIdx: Int, n: Int, f0: Float, heldSec: Float, amp: Float) {
        val period = max(2, (sampleRate / f0).toInt()) // Gecikme hattı uzunluğu
        val buf = FloatArray(period) { noise() } // Başlangıç: beyaz gürültü (vurma)
        var pos = 0 // Halka tampon konumu
        val heldSamples = (heldSec * sampleRate).toInt() // Tutma süresi
        for (i in 0 until n) { // Her örnek
            val idx = startIdx + i // Çıkış indeksi
            if (idx >= out.size) break // Tampon bitti
            val cur = buf[pos] // Mevcut örnek
            val nxt = buf[(pos + 1) % period] // Sonraki örnek
            val damping = if (i > heldSamples) 0.985f else 0.996f // Bırakıldıktan sonra daha hızlı sönüm
            buf[pos] = damping * 0.5f * (cur + nxt) // Alçak geçiren filtre + sönüm
            pos = (pos + 1) % period // İlerle
            if (idx >= 0) out[idx] += cur * amp * 1.4f // Çıkışa ekle
        }
    }

    /** Enstrümanın notadan sonra çınlama süresi (saniye). */
    private fun ringTime(inst: Instrument, midi: Int): Float = when (inst) {
        Instrument.PIYANO -> 0.25f // Pedal yok, kısa damper
        Instrument.MUZIK_KUTUSU -> 1.2f // Çınlar
        Instrument.CAN -> 2.0f // Uzun çınlama
        Instrument.MARIMBA -> 0.4f // Kısa
        Instrument.GITAR -> 0.8f // Doğal sönüm
        Instrument.SEKIZ_BIT -> 0.05f // Anlık kesilir
        Instrument.FLUT -> 0.12f // Kısa release
        Instrument.SYNTH -> 0.35f // Yumuşak release
    }.let { if (midi > 84) it * 0.6f else it } // Çok tiz notalar daha hızlı söner

    private data class Partial(val ratio: Float, val amp: Float, val decayMul: Float) // Kısmi tanımı
    private data class Env(val attack: Float, val decayTau: Float, val sustain: Float, val releaseTau: Float) // ADSR benzeri zarf

    private fun partials(inst: Instrument): List<Partial> = when (inst) {
        Instrument.PIYANO -> listOf(Partial(1f, 1f, 0.6f), Partial(2f, 0.55f, 1.2f), Partial(3f, 0.3f, 1.8f), Partial(4f, 0.18f, 2.5f), Partial(5f, 0.1f, 3f), Partial(6f, 0.06f, 3.5f)) // Harmonik, üstler hızlı söner
        Instrument.MUZIK_KUTUSU -> listOf(Partial(1f, 1f, 0.8f), Partial(2.0f, 0.25f, 1.5f), Partial(3.93f, 0.2f, 2.2f), Partial(6.1f, 0.08f, 3f)) // İnharmonik çınlama
        Instrument.SEKIZ_BIT -> (1..15 step 2).map { Partial(it.toFloat(), 1f / it, 0f) } // Kare dalga: tek harmonikler 1/n
        Instrument.MARIMBA -> listOf(Partial(1f, 1f, 2.5f), Partial(4f, 0.55f, 6f), Partial(10f, 0.12f, 10f)) // Marimba: 4. kısmi akortlu
        Instrument.FLUT -> listOf(Partial(1f, 1f, 0f), Partial(2f, 0.22f, 0f), Partial(3f, 0.08f, 0f)) // Neredeyse saf sinüs
        Instrument.CAN -> listOf(Partial(0.5f, 0.35f, 0.3f), Partial(1f, 1f, 0.5f), Partial(1.2f, 0.5f, 0.9f), Partial(1.5f, 0.45f, 1.0f), Partial(2.0f, 0.5f, 1.3f), Partial(2.5f, 0.25f, 1.8f), Partial(3.0f, 0.2f, 2.2f)) // Çan kısmileri (hum, prime, tierce, quint, nominal)
        Instrument.GITAR -> emptyList() // Fiziksel model kullanır
        Instrument.SYNTH -> (1..12).map { Partial(it.toFloat(), (1f / it) * exp(-it * 0.18f), 0f) } // Alçak geçirenli testere
    }

    private fun envelope(inst: Instrument): Env = when (inst) {
        Instrument.PIYANO -> Env(0.004f, 1.1f, 0f, 0.07f) // Hızlı atak, uzun bozunma, kısa release
        Instrument.MUZIK_KUTUSU -> Env(0.002f, 0.9f, 0f, 0.6f) // Çınlayan
        Instrument.SEKIZ_BIT -> Env(0.003f, 0.08f, 0.75f, 0.02f) // Sürdürülen kare dalga
        Instrument.MARIMBA -> Env(0.001f, 0.3f, 0f, 0.15f) // Çok kısa
        Instrument.FLUT -> Env(0.07f, 0.4f, 0.85f, 0.08f) // Yumuşak atak, sürdürme
        Instrument.CAN -> Env(0.002f, 1.8f, 0f, 1.2f) // Uzun
        Instrument.GITAR -> Env(0.001f, 0.5f, 0f, 0.1f) // Kullanılmaz
        Instrument.SYNTH -> Env(0.12f, 0.6f, 0.8f, 0.25f) // Pad
    }

    private var seed = 12345 // Deterministik gürültü tohumu (testler tekrarlanabilir olsun)
    private fun noise(): Float { seed = seed * 1103515245 + 12345; return ((seed ushr 8) and 0xFFFF) / 32768f - 1f } // Hızlı LCG beyaz gürültü

    /** Baş/son fade, yumuşak sınırlama ve -1 dBFS'e normalizasyon. */
    private fun finalize(out: FloatArray) {
        var peak = 0f // Tepe değer
        for (v in out) peak = max(peak, abs(v)) // Tepe aranıyor
        if (peak < 1e-6f) return // Sessiz
        val pre = 0.9f / peak // Ön kazanç
        val fadeIn = (0.01f * sampleRate).toInt(); val fadeOut = (FADE_OUT_SEC * sampleRate).toInt() // Fade uzunlukları
        for (i in out.indices) { // Her örnek
            var v = tanh(out[i] * pre * 1.2f) / tanh(1.2f) // Yumuşak sınırlama (hafif ısıtma)
            if (i < fadeIn) v *= i.toFloat() / fadeIn // Giriş fade
            val fromEnd = out.size - 1 - i // Sona uzaklık
            if (fromEnd < fadeOut) v *= fromEnd.toFloat() / fadeOut // Çıkış fade
            out[i] = v // Yazılıyor
        }
        var p2 = 0f; for (v in out) p2 = max(p2, abs(v)) // Yeni tepe
        val gain = 0.891f / max(p2, 1e-6f) // -1 dBFS hedef
        for (i in out.indices) out[i] *= gain // Normalizasyon
    }

    companion object {
        const val RELEASE_TAIL_SEC = 0.5f // Pencere sonundan sonra çınlama payı
        const val FADE_OUT_SEC = 0.6f // Zil sonu fade
    }
}
