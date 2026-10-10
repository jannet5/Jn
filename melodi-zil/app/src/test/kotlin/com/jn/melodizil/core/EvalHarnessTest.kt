package com.jn.melodizil.core // Çekirdek testleri

import org.junit.Assume // Atlama
import org.junit.Test // JUnit
import java.io.File // Dosya
import java.nio.ByteBuffer // Ham PCM
import java.nio.ByteOrder // Bayt sırası

/** Değerlendirme: MELODI_EVAL_DIR içindeki her *.raw için çıkarılan notaları <ad>.notes.csv olarak yazar (başlangıç, süre, midi). Puanlama araclar/degerlendirme/puanla.py ile. */
class EvalHarnessTest {
    @Test
    fun degerlendirmeSetiniCalistir() {
        val dir = System.getenv("MELODI_EVAL_DIR")?.let { File(it) } // Klasör
        Assume.assumeTrue("MELODI_EVAL_DIR yok", dir != null && dir.isDirectory) // Yoksa atla
        val ex = MelodyExtractor(p = MelodyExtractor.Params.parse(System.getenv("MELODI_PARAMS"))) // Çıkarıcı (ayarlar ortam değişkeninden)
        var totalMs = 0L // Toplam süre
        dir!!.listFiles { f -> f.name.endsWith(".raw") }!!.sorted().forEach { f -> // Her parça
            val b = f.readBytes(); val pcm = FloatArray(b.size / 4) // PCM
            ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get(pcm) // Çöz
            val t0 = System.currentTimeMillis(); val m = ex.extract(pcm); totalMs += System.currentTimeMillis() - t0 // Çıkar
            File(dir, f.name.removeSuffix(".raw") + ".notes.csv").writeText(m.notes.joinToString("\n") { "%.4f,%.4f,%d".format(java.util.Locale.US, it.startSec, it.durationSec, it.midi) }) // Yaz
        }
        println("EVAL_SURE_MS $totalMs") // Süre
    }
}
