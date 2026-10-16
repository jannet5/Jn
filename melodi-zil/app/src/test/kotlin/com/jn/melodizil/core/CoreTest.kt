package com.jn.melodizil.core // Çekirdek testleri

import org.junit.Assert.assertEquals // Eşitlik
import org.junit.Assert.assertNull // Null
import org.junit.Assert.assertTrue // Doğruluk
import org.junit.Test // JUnit
import kotlin.math.PI // Pi
import kotlin.math.abs // Mutlak
import kotlin.math.sin // Sinüs

/** Saf Kotlin çekirdeğin birim testleri: FFT, melodi çıkarımı (sentetik çok sesli), sentez, WAV, bölüm seçici, bağlantı ayrıştırma. */
class CoreTest {

    @Test
    fun fftSafSinusunTepesiniDogruBindeBulur() {
        val n = 2048; val sr = 22050; val f = 440f // Parametreler
        val re = FloatArray(n) { sin(2 * PI * f * it / sr).toFloat() }; val im = FloatArray(n) // Sinüs
        Fft(n).transform(re, im) // Dönüşüm
        var best = 0; var bestMag = 0f // Tepe arama
        for (k in 1 until n / 2) { val m = re[k] * re[k] + im[k] * im[k]; if (m > bestMag) { bestMag = m; best = k } } // Maksimum
        assertTrue("Tepe bin $best", abs(best - f * n / sr) <= 1f) // 440 Hz ≈ bin 40.9
    }

    @Test
    fun midiDonusumleriTutarli() {
        assertEquals(440f, midiToHz(69), 0.001f) // A4
        assertEquals(69.0, hzToMidi(440.0), 1e-6) // Tersi
        assertEquals("C4", midiToName(60)); assertEquals("A#3", midiToName(58)) // Adlar
    }

    /** Sentetik "şarkı": melodi (harmonik zengin) + bas + akor pedleri + gürültü. Melodi geri bulunmalı. */
    @Test
    fun cokSesliKaritisimdanMelodiGeriBulunur() {
        val sr = 22050 // Hız
        val melody = intArrayOf(67, 67, 69, 67, 72, 71, 67, 67, 69, 67, 74, 72) // "Happy Birthday" başı (G4…)
        val noteDur = 0.4f // Nota süresi
        val total = (melody.size * noteDur * sr).toInt() // Örnek
        val pcm = FloatArray(total) // Sinyal
        var seed = 7 // Gürültü
        for (i in 0 until total) { // Her örnek
            val t = i.toFloat() / sr // Zaman
            val idx = (t / noteDur).toInt().coerceAtMost(melody.size - 1) // Nota indeksi
            val f = midiToHz(melody[idx]) // Melodi frekansı
            val tn = t - idx * noteDur // Nota içi zaman
            val env = if (tn < 0.01f) tn / 0.01f else 1f // Zarf
            var s = 0f // Melodi: 6 harmonik
            for (h in 1..6) s += sin(2 * PI * f * h * t).toFloat() / h // Harmonikler
            s *= 0.5f * env // Genlik
            val bass = midiToHz(43) // Bas G2
            s += 0.4f * sin(2 * PI * bass * t).toFloat() + 0.2f * sin(2 * PI * bass * 2 * t).toFloat() // Bas
            for (c in intArrayOf(55, 59, 62)) s += 0.15f * sin(2 * PI * midiToHz(c) * t).toFloat() // Akor (G3 B3 D4)
            seed = seed * 1103515245 + 12345; s += 0.02f * (((seed ushr 8) and 0xFFFF) / 32768f - 1f) // Gürültü
            pcm[i] = s * 0.5f // Yaz
        }
        val result = MelodyExtractor(sr, p = MelodyExtractor.Params.parse(System.getenv("MELODI_PARAMS"))).extract(pcm) // Çıkarım (varsayılan ya da test ayarı)
        assertTrue("Nota yok", result.notes.isNotEmpty()) // Boş olmasın
        var correct = 0f // Doğru süre
        for (n in result.notes) { // Her çıkan nota
            var t = n.startSec // Zaman
            while (t < n.endSec) { // Çerçeve çerçeve
                val idx = (t / noteDur).toInt().coerceAtMost(melody.size - 1) // Beklenen
                if (melody[idx] == n.midi) correct += 0.01f // Doğru
                t += 0.01f // İlerle
            }
        }
        val accuracy = correct / (melody.size * noteDur) // Doğru oran
        println("Sentetik melodi doğruluğu: %.2f, notalar: %s".format(accuracy, result.notes.map { midiToName(it.midi) })) // Bilgi
        assertTrue("Doğruluk düşük: $accuracy", accuracy > 0.8f) // En az %80
    }

    @Test
    fun sessizGirdiBosMelodiVerir() {
        val m = MelodyExtractor().extract(FloatArray(22050 * 3)) // 3 sn sessizlik
        assertTrue(m.isEmpty) // Nota yok
        assertEquals(3f, m.durationSec, 0.01f) // Süre doğru
    }

    @Test
    fun herEnstrumanSesUretirVeSinirlarIcindeKalir() {
        val melody = Melody(listOf(Note(60, 0f, 0.5f, 0.8f), Note(64, 0.5f, 0.5f, 0.6f), Note(67, 1f, 1f, 1f)), 3f) // Üç nota
        val synth = Synth(44100) // Sentez
        for (inst in Instrument.entries) { // Her tını
            val pcm = synth.render(melody, inst, 0f, 3f) // 3 sn
            assertEquals(3 * 44100, pcm.size) // Uzunluk
            val peak = pcm.maxOf { abs(it) } // Tepe
            assertTrue("$inst sessiz", peak > 0.5f) // Ses var
            assertTrue("$inst kırpılıyor", peak <= 1f) // Sınır içi
            val rms = kotlin.math.sqrt(pcm.map { it * it }.average()) // RMS
            assertTrue("$inst RMS anormal: $rms", rms > 0.01 && rms < 0.7) // Makul seviye
            assertTrue("$inst sonu fade değil", abs(pcm.last()) < 0.01f) // Fade-out
        }
    }

    @Test
    fun pencereDisiNotalarCalinmaz() {
        val melody = Melody(listOf(Note(60, 0f, 1f, 1f), Note(72, 10f, 1f, 1f)), 20f) // Biri 0'da, biri 10'da
        val pcm = Synth(44100).render(melody, Instrument.SEKIZ_BIT, 9.5f, 3f) // 9.5..12.5 penceresi
        val firstHalf = pcm.copyOfRange(0, 44100 / 4).maxOf { abs(it) } // İlk 0.25 sn (nota 10'da başlar → sessiz)
        val later = pcm.copyOfRange(44100, 44100 * 2).maxOf { abs(it) } // 10.5..11.5 (nota çalıyor)
        assertTrue(firstHalf < 0.05f); assertTrue(later > 0.3f) // Doğru yerleşim
    }

    @Test
    fun wavBasligiDogru() {
        val wav = WavWriter.toWav(FloatArray(100) { 0.5f }, 44100) // 100 örnek
        assertEquals(44 + 200, wav.size) // Başlık + veri
        assertEquals("RIFF", String(wav, 0, 4, Charsets.US_ASCII)); assertEquals("WAVE", String(wav, 8, 4, Charsets.US_ASCII)) // İmzalar
        val rate = (wav[24].toInt() and 0xff) or ((wav[25].toInt() and 0xff) shl 8) or ((wav[26].toInt() and 0xff) shl 16) // Örnekleme hızı
        assertEquals(44100, rate) // Doğru
        val sample = ((wav[44].toInt() and 0xff) or ((wav[45].toInt() and 0xff) shl 8)).toShort() // İlk örnek
        assertEquals(16383, sample.toInt()) // 0.5 → 16383
    }

    @Test
    fun bolumSeciciEnYogunPencereyiBulur() {
        val notes = (0 until 20).map { Note(60 + it % 5, 60f + it * 0.5f, 0.45f, 0.9f) } + listOf(Note(60, 5f, 0.3f, 0.3f)) // Yoğun bölüm 60-70 sn, tek nota 5 sn'de
        val start = SegmentChooser.bestStart(Melody(notes, 120f), 15f) // 15 sn pencere
        assertTrue("Başlangıç yoğun bölüme yakın değil: $start", start in 50f..61f) // Pencere yoğun bölümü kapsamalı
    }

    @Test
    fun bolumSeciciKisaSarkidaSifirDoner() {
        val m = Melody(listOf(Note(60, 1f, 1f, 1f)), 10f) // 10 sn şarkı
        assertEquals(0f, SegmentChooser.bestStart(m, 30f), 0.001f) // 30 sn pencere sığmaz → 0
    }

    @Test
    fun youtubeBaglantilariAyristirilir() {
        val id = "dQw4w9WgXcQ" // Beklenen
        listOf( // Biçimler
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ", "https://youtu.be/dQw4w9WgXcQ?si=xyz", "https://m.youtube.com/watch?feature=share&v=dQw4w9WgXcQ",
            "https://www.youtube.com/shorts/dQw4w9WgXcQ", "https://music.youtube.com/watch?v=dQw4w9WgXcQ&list=RD", "youtube.com/embed/dQw4w9WgXcQ", "dQw4w9WgXcQ",
            "Şuna bak: https://youtu.be/dQw4w9WgXcQ harika",
        ).forEach { assertEquals(it, id, YouTubeUrl.extractVideoId(it)) } // Hepsi aynı kimlik
        assertNull(YouTubeUrl.extractVideoId("https://vimeo.com/12345")); assertNull(YouTubeUrl.extractVideoId("")); assertNull(YouTubeUrl.extractVideoId("merhaba")) // Geçersizler
        assertEquals("https://www.youtube.com/watch?v=$id", YouTubeUrl.canonical(id)) // Standart
    }

    @Test
    fun resamplerUzunluguVeMonoDogru() {
        val stereo = FloatArray(200) { if (it % 2 == 0) 1f else 0f } // Sol 1, sağ 0
        val mono = Resampler.toMono(stereo, 2) // Mono
        assertEquals(100, mono.size); assertEquals(0.5f, mono[0], 1e-6f) // Ortalama
        val down = Resampler.resample(FloatArray(44100) { sin(2 * PI * 440 * it / 44100).toFloat() }, 44100, 22050) // Düşür
        assertEquals(22050, down.size) // Yarı uzunluk
    }
}
