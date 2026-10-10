package com.jn.melodizil.core // Karşılaştırma testi

import org.junit.Assume // Atlama
import org.junit.Test // JUnit
import java.io.File // Dosya
import java.nio.ByteBuffer // PCM
import java.nio.ByteOrder // Bayt sırası

/** Eski (v1) algoritmayla aynı değerlendirmeyi çalıştırır: önce/sonra karşılaştırması için (MELODI_ESKI_DIR). */
class EskiEvalTest {
    @Test
    fun eskiAlgoritma() {
        val dir = System.getenv("MELODI_ESKI_DIR")?.let { File(it) }; Assume.assumeTrue(dir != null && dir.isDirectory) // Klasör
        dir!!.listFiles { f -> f.name.endsWith(".raw") }!!.forEach { f -> val b = f.readBytes(); val pcm = FloatArray(b.size / 4); ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get(pcm)
            val m = EskiMelodyExtractor().extract(pcm); File(dir, f.name.removeSuffix(".raw") + ".notes.csv").writeText(m.notes.joinToString("\n") { "%.4f,%.4f,%d".format(java.util.Locale.US, it.startSec, it.durationSec, it.midi) }) }
    }
}
