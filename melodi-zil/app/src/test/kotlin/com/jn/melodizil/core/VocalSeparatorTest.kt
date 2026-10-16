package com.jn.melodizil.core // Çekirdek testleri

import org.junit.Assert.assertTrue // Doğruluk
import org.junit.Test // JUnit
import kotlin.math.PI // Pi
import kotlin.math.abs // Mutlak
import kotlin.math.sin // Sinüs

/** VocalSeparator DSP'si: maske=1 → sinyal geri kurulur, maske=0 → sessizlik, parça sınırlarında süreklilik. */
class VocalSeparatorTest {
    private fun signal(n: Int) = FloatArray(n) { (0.5 * sin(2 * PI * 440 * it / 22050) + 0.3 * sin(2 * PI * 1250 * it / 22050)).toFloat() } // 0-11 kHz içi sinyal

    @Test
    fun tamMaskeSinyaliGeriKurar() {
        val x = signal(22050 * 15) // 15 sn: birden fazla 512 karelik parça (parça sınırı test edilir)
        val y = VocalSeparator { inp, out -> System.arraycopy(inp, 0, out, 0, inp.size) }.separate(x) // Çıktı = girdi → maske 1
        var maxErr = 0f; for (i in 2048 until x.size - 2048) maxErr = maxOf(maxErr, abs(x[i] - y[i])) // Kenarlar hariç
        assertTrue("geri kurma hatası $maxErr", maxErr < 1e-3f) // Mükemmele yakın
    }

    @Test
    fun sifirMaskeSessizlikVerir() {
        val y = VocalSeparator { _, out -> java.util.Arrays.fill(out, 0f) }.separate(signal(22050 * 3)) // Maske 0
        assertTrue(y.maxOf { abs(it) } < 1e-6f) // Sessiz
    }

    @Test
    fun yarimMaskeGenligiYarilar() {
        val x = signal(22050 * 5)
        val y = VocalSeparator { inp, out -> for (i in inp.indices) out[i] = inp[i] * 0.5f }.separate(x) // Maske 0.5
        var e = 0f; for (i in 4096 until x.size - 4096) e = maxOf(e, abs(y[i] - 0.5f * x[i])) // Beklenen: yarı genlik
        assertTrue("hata $e", e < 1e-3f)
    }
}
