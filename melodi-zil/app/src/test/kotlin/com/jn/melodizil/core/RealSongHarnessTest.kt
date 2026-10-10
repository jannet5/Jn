package com.jn.melodizil.core // Çekirdek testleri

import org.junit.Assume // Ortam değişkeni yoksa testi atla
import org.junit.Test // JUnit
import java.io.File // Dosya
import java.nio.ByteBuffer // Ham PCM okuma
import java.nio.ByteOrder // Bayt sırası

/**
 * Gerçek bir şarkı üzerinde uçtan uca deneme: MELODI_TEST_PCM ortam değişkeni ham f32le mono 22050 Hz dosyayı gösterir.
 * CI'da değişken yoksa test atlanır. Çıktı WAV'ları MELODI_TEST_OUT klasörüne yazılır.
 */
class RealSongHarnessTest {
    @Test
    fun gercekSarkidanMelodiCikar() {
        val path = System.getenv("MELODI_TEST_PCM") // Ham PCM yolu
        Assume.assumeTrue("MELODI_TEST_PCM yok, atlanıyor", path != null && File(path).exists()) // Yoksa atla
        val bytes = File(path!!).readBytes() // Dosya okunuyor
        val pcm = FloatArray(bytes.size / 4) // Float dizisi
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get(pcm) // Çözülüyor
        val t0 = System.currentTimeMillis() // Süre ölçümü
        val params = if (System.getenv("MELODI_VOCAL") == "1") MelodyExtractor.Params.VOCAL else MelodyExtractor.Params() // Ayrılmış vokal mi karışım mı
        val melody = MelodyExtractor(p = params).extract(pcm) // Melodi çıkarımı
        val t1 = System.currentTimeMillis() // Bitiş
        val voiced = melody.notes.sumOf { it.durationSec.toDouble() } / melody.durationSec // Sesli oran
        println("SÜRE: ${melody.durationSec}s, NOTA: ${melody.notes.size}, ÇIKARIM ${t1 - t0} ms, sesli oran %.2f".format(voiced)) // Özet
        val hist = melody.notes.groupBy { it.midi }.mapValues { it.value.sumOf { n -> n.durationSec.toDouble() } }.toSortedMap() // Perde histogramı
        println("PERDE HİSTOGRAMI: " + hist.entries.joinToString { "${midiToName(it.key)}=%.1f".format(it.value) }) // Yazdır
        println("ORT. NOTA SÜRESİ: %.3f".format(melody.notes.map { it.durationSec }.average())) // Ortalama süre
        val start = SegmentChooser.bestStart(melody, 30f) // En iyi bölüm
        println("EN İYİ BAŞLANGIÇ: $start") // Yazdır
        println("İLK 40 NOTA (seçilen pencere): " + melody.notes.filter { it.startSec >= start }.take(40).joinToString(" ") { "${midiToName(it.midi)}(%.2f)".format(it.durationSec) }) // Nota dizisi
        val outDir = System.getenv("MELODI_TEST_OUT") // Çıktı klasörü
        if (outDir != null) { // Çıktı isteniyorsa
            File(outDir).mkdirs() // Klasör
            val synth = Synth(44100) // Sentezleyici
            for (inst in Instrument.entries) { // Her enstrüman
                val s0 = System.currentTimeMillis() // Süre
                val pcmOut = synth.render(melody, inst, start, 30f) // 30 sn çal
                File(outDir, "zil_${inst.id}.wav").writeBytes(WavWriter.toWav(pcmOut, 44100)) // Yaz
                println("SENTEZ ${inst.id}: ${System.currentTimeMillis() - s0} ms") // Süre
            }
        }
        assert(melody.notes.size > 50) { "Gerçek şarkıdan çok az nota çıktı" } // Makul nota sayısı
    }
}
