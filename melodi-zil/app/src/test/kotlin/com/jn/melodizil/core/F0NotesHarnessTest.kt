package com.jn.melodizil.core // Çekirdek testleri

import org.junit.Assume // Atlama
import org.junit.Test // JUnit
import java.io.File // Dosya

/**
 * Değerlendirme: hazır bir perde modelinin (RMVPE) 10 ms f0 çıktısını (<ad>.f0.csv: zaman, f0_hz, güven) NoteSegmenter ile notaya çevirir.
 * MELODI_F0_DIR: f0 dosyaları; MELODI_F0_OUT: çıktı kökü; MELODI_F0_RUNS: "ad|conf=0.1,hyst=0.5;ad2|..." (her ayar seti ayrı klasöre).
 * Puanlama: araclar/degerlendirme/puanla.py <cikti>/<ad> (ref dosyaları oraya bağlanır).
 */
class F0NotesHarnessTest {
    @Test
    fun f0DosyalariniNotayaCevir() {
        val dir = System.getenv("MELODI_F0_DIR")?.let { File(it) } // f0 klasörü
        Assume.assumeTrue("MELODI_F0_DIR yok", dir != null && dir.isDirectory) // Yoksa atla
        val outRoot = File(System.getenv("MELODI_F0_OUT")!!) // Çıktı kökü
        val runs = System.getenv("MELODI_F0_RUNS")!!.split(';').filter { it.isNotBlank() } // Ayar setleri
        val files = dir!!.listFiles { f -> f.name.endsWith(".f0.csv") }!!.sorted() // f0 dosyaları
        val data = files.associate { f -> f.name.removeSuffix(".f0.csv") to f.readLines().filter { it.isNotBlank() }.map { l -> l.split(',').map(String::toFloat) } } // Oku
        for (run in runs) {
            val (name, params) = run.split('|').let { it[0] to it.getOrElse(1) { "" } } // Ad ve ayarlar
            val conf = params.split(',').firstOrNull { it.startsWith("conf=") }?.substringAfter('=')?.toFloat() ?: 0.03f // Güven eşiği
            val gate = params.split(',').firstOrNull { it.startsWith("edb=") }?.substringAfter('=')?.toFloat() // Enerji kapısı (dB, %95'liğin altı)
            val seg = NoteSegmenter(NoteSegmenter.parse(params, CrepeMelody.SEGMENT)) // Bölütleyici
            val out = File(outRoot, name).apply { mkdirs() } // Çıktı klasörü
            for ((id, rows) in data) {
                val midi = FloatArray(rows.size) { i -> val r = rows[i]; if (r[1] > 0f && r[2] > conf && (gate == null || r.size < 4 || r[3] > -gate)) CrepeMelody.hzToMidi(r[1]) else 0f } // Kesirli MIDI
                val fs = if (rows.size > 1) rows[1][0] - rows[0][0] else 0.01f // Kare süresi (dosyadan)
                val notes = seg.segment(midi, fs, rows.firstOrNull()?.get(0) ?: 0f) // Notalar
                File(out, "$id.notes.csv").writeText(notes.joinToString("\n") { "%.4f,%.4f,%.3f".format(java.util.Locale.US, it.startSec, it.durationSec, it.midi + seg.lastTuning) }) // Yaz (puanlamada akort kayması geri eklenir: perde doğruluğu ızgaradan bağımsız ölçülür)
            }
        }
    }
}
