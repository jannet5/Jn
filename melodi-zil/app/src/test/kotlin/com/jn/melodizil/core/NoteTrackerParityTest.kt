package com.jn.melodizil.core // NoteTracker testleri: Python (nota_cikar_v2.py) ile parite + birim senaryolar

import org.junit.Assert.assertEquals // Eşitlik
import org.junit.Assert.assertTrue // Doğruluk
import org.junit.Test // JUnit
import java.util.Random // Tekrarlanabilir rastgele sayı
import kotlin.math.PI // Pi
import kotlin.math.abs // Mutlak değer
import kotlin.math.ceil // Yukarı yuvarlama
import kotlin.math.pow // Üs
import kotlin.math.sin // Sinüs

/** Kotlin NoteTracker'ın Python v2 ile aynı notaları üretmesi (5 klip, src/test/resources/nota/) ve temel senaryolar. */
class NoteTrackerParityTest {

    private val klipler = listOf("vocadito_1", "vocadito_13", "vocadito_10", "Ani_1_01", "amy_10_05") // 3 Vocadito + 2 MIR-1K karışım
    private class Bek(val bas: Double, val midi: Int) // Python'un beklenen notası: başlangıç sn, yuvarlanmış MIDI
    private class Sonuc(val ad: String, val py: Int, val kt: Int, val esles: Int, val midiAyni: Int, val enBuyukOnset: Double) // Klip sonucu

    private fun satirlar(dosya: String): List<String> { // CSV satırları (başlık atılır)
        val ins = javaClass.getResourceAsStream("/nota/$dosya") ?: error("test kaynağı yok: $dosya")
        return ins.bufferedReader().use { it.readLines() }.drop(1).filter { it.isNotBlank() }
    }

    private fun karsilastir(ad: String, rmsKullan: Boolean): Sonuc { // Bir klibi Kotlin ile çalıştır, Python beklentisiyle kıyasla
        val g = satirlar("${ad}_girdi.csv").map { it.split(',') }
        val f0 = FloatArray(g.size) { g[it][1].toFloat() }; val sal = FloatArray(g.size) { g[it][2].toFloat() }; val rms = FloatArray(g.size) { g[it][3].toFloat() }
        val pt = PitchTrack(0.01f, f0, sal)
        val mel = if (rmsKullan) NoteTracker.track(pt, g.size * 0.01f, rms) else NoteTracker.track(pt, g.size * 0.01f)
        val bek = satirlar(if (rmsKullan) "${ad}_beklenen.csv" else "${ad}_beklenen_sal.csv").map { it.split(',') }.map { Bek(it[0].toDouble(), Math.rint(it[2].toDouble()).toInt()) }
        var esles = 0; var midiAyni = 0; var enBuyuk = 0.0
        for (b in bek) { // Her Python notasına en yakın başlangıçlı Kotlin notası
            val en = mel.notes.minByOrNull { abs(it.startSec - b.bas) } ?: continue
            val fark = abs(en.startSec - b.bas)
            if (fark < 0.020) { esles++; if (fark > enBuyuk) enBuyuk = fark; if (en.midi == b.midi) midiAyni++ }
        }
        return Sonuc(ad, bek.size, mel.notes.size, esles, midiAyni, enBuyuk)
    }

    private fun paritiDene(rmsKullan: Boolean, etiket: String) { // 5 klibin toplamı üzerinden ölçüt
        val r = klipler.map { karsilastir(it, rmsKullan) }
        for (s in r) println("PARITE[$etiket] ${s.ad}: python=${s.py} kotlin=${s.kt} onset<20ms eslesen=${s.esles} midi_ayni=${s.midiAyni} en_buyuk_onset_farki_ms=${"%.1f".format(s.enBuyukOnset * 1000)}")
        val py = r.sumOf { it.py }; val kt = r.sumOf { it.kt }; val es = r.sumOf { it.esles }; val ma = r.sumOf { it.midiAyni }
        println("PARITE[$etiket] TOPLAM: python=$py kotlin=$kt sayi_farki=${abs(py - kt)} (%${"%.2f".format(100.0 * abs(py - kt) / py)}) onset_eslesen=$es midi_ayni=$ma")
        assertTrue("Nota sayısı farkı > %3: python=$py kotlin=$kt", abs(py - kt) <= 0.03 * py) // Sayı farkı <= %3
        assertTrue("Onset(<20ms) eşleşen oranı < %97: $es/$py", es >= ceil(0.97 * py)) // Eşleşme
        assertTrue("Eşleşenlerde midi farklı: $ma/$es", ma >= es - 1) // Eşleşen notalarda perde aynı (en çok 1 istisna)
    }

    @Test fun pariteGercekRmsIle() = paritiDene(true, "rms") // Gerçek enerjiyle: Python ile birebir yol

    @Test fun pariteVarsayilanSalienceIle() = paritiDene(false, "salience") // Sözleşme imzası (rms yok): salience'tan sahte enerji

    // ---------------- Sentetik birim testleri ----------------
    private fun iz(f0: FloatArray, sal: FloatArray? = null) = PitchTrack(0.01f, f0, sal ?: FloatArray(f0.size) { if (f0[it] > 0f) 0.9f else 0.1f }) // Kısa kurucu
    private fun hz(midi: Double): Float = (440.0 * 2.0.pow((midi - 69.0) / 12.0)).toFloat() // MIDI -> Hz

    @Test fun titremeTekNotaKalir() {
        val f0 = FloatArray(100) { hz(69.0 + 0.4 * sin(2 * PI * 5.5 * it * 0.01)) } // 1 sn A4, ±40 cent titreme
        val m = NoteTracker.track(iz(f0), 1f)
        assertEquals("Titreme tek nota kalmalı: ${m.notes}", 1, m.notes.size)
        assertEquals(69, m.notes[0].midi)
        assertTrue("Süre ~1 sn: ${m.notes[0].durationSec}", abs(m.notes[0].durationSec - 1f) < 0.05f)
    }

    @Test fun elliMilisaniyeKirikNotaAtilir() {
        val tek = FloatArray(65) { if (it in 30 until 35) hz(69.0) else 0f } // Sessizlik içinde 5 kare (50 ms)
        assertTrue("Yalnız 50 ms'lik nota atılmalı", NoteTracker.track(iz(tek), 0.65f).isEmpty)
        val ara = FloatArray(85) { if (it < 40) hz(69.0) else if (it < 45) hz(76.0) else hz(72.0) } // İki nota arasında 50 ms'lik kırık parça
        val m = NoteTracker.track(iz(ara), 0.85f)
        assertEquals("Kırık parça atılmalı: ${m.notes}", listOf(69, 72), m.notes.map { it.midi })
    }

    @Test fun ayniPerdeEnerjiCukuruylaBolunur() {
        val f0 = FloatArray(100) { hz(69.0) } // 1 sn aynı perde
        val rms = FloatArray(100) { if (it in 48..52) 0.005f else 0.1f } // Ortada 26 dB enerji çukuru
        val m = NoteTracker.track(iz(f0, FloatArray(100) { 0.95f }), 1f, rms)
        assertEquals("Enerji çukuru notayı ikiye bölmeli: ${m.notes}", listOf(69, 69), m.notes.map { it.midi })
        val duz = NoteTracker.track(iz(f0, FloatArray(100) { 0.95f }), 1f, FloatArray(100) { 0.1f })
        assertEquals("Çukursuz tek nota", 1, duz.notes.size)
    }

    @Test fun ayniPerdeSalienceCukuruylaBolunur() {
        val f0 = FloatArray(100) { hz(69.0) }
        val sal = FloatArray(100) { if (it in 48..52) 0.45f else 0.95f } // rms verilmediğinde salience çukuru (6,5 dB)
        val m = NoteTracker.track(iz(f0, sal), 1f)
        assertEquals("Salience çukuru notayı ikiye bölmeli: ${m.notes}", listOf(69, 69), m.notes.map { it.midi })
    }

    @Test fun sessizGirisBosMelodi() {
        assertTrue(NoteTracker.track(iz(FloatArray(300)), 3f).isEmpty) // Hepsi sessiz
        assertTrue(NoteTracker.track(PitchTrack(0.01f, FloatArray(0), FloatArray(0)), 0f).isEmpty) // Boş iz
        assertTrue("Düşük güven atılmalı", NoteTracker.track(iz(FloatArray(100) { hz(69.0) }, FloatArray(100) { 0.3f }), 1f).isEmpty) // Salience < 0.5
    }

    @Test fun kisaBosluklarDolarAkortKaymasiDuzelir() {
        val bosluk = FloatArray(42) { if (it in 20..21) 0f else hz(69.0) } // 20 ms boşluk doldurulmalı
        assertEquals(1, NoteTracker.track(iz(bosluk, FloatArray(42) { 0.9f }), 0.42f).notes.size) // Boşlukta salience düşmezse tek nota
        val flat = FloatArray(80) { if (it < 40) hz(68.7) else hz(71.7) } // 30 cent bemol söyleyen: yine 69 ve 72 çıkmalı
        assertEquals(listOf(69, 72), NoteTracker.track(iz(flat), 0.8f).notes.map { it.midi })
    }

    @Test fun notalarSiraliCakismasizSiddetAraliktaVeHizli() {
        val rnd = Random(7); val n = 18000 // 3 dk
        val f0 = FloatArray(n); val sal = FloatArray(n); var i = 0; var midi = 66.0
        while (i < n) {
            val len = 20 + rnd.nextInt(60); midi = (midi + rnd.nextInt(9) - 4).coerceIn(52.0, 82.0)
            val vurgu = rnd.nextInt(20) == 0 // Ara sıra kısa sessizlik
            for (k in 0 until len) { if (i + k >= n) break
                val vib = 0.35 * sin(2 * PI * 5.5 * k * 0.01) + 0.15 * (rnd.nextDouble() - 0.5)
                f0[i + k] = if (vurgu && k < 8) 0f else hz(midi + vib); sal[i + k] = 0.6f + 0.35f * rnd.nextFloat() }
            i += len
        }
        val pt = PitchTrack(0.01f, f0, sal); NoteTracker.track(pt, 180f) // Isınma
        val sure = (0 until 3).map { val t0 = System.nanoTime(); NoteTracker.track(pt, 180f); (System.nanoTime() - t0) / 1e6 }.sorted()[1] // Ortanca ms
        val m = NoteTracker.track(pt, 180f)
        println("HIZ: 18000 kare -> ${"%.0f".format(sure)} ms, ${m.notes.size} nota")
        assertTrue("3 dk iz < 1 sn olmalı: $sure ms", sure < 1000.0)
        assertTrue(m.notes.size > 100)
        for (k in 1 until m.notes.size) assertTrue("Sıralı/çakışmasız", m.notes[k].startSec >= m.notes[k - 1].startSec + 0.0f && m.notes[k].startSec >= m.notes[k - 1].endSec - 0.011f)
        assertTrue(m.notes.all { it.velocity in 0.3f..1f && it.durationSec >= 0.05f })
    }
}
