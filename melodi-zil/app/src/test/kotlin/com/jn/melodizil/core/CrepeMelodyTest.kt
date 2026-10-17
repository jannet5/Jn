package com.jn.melodizil.core // Çekirdek testleri

import org.junit.Assert.assertEquals // Eşitlik
import org.junit.Assert.assertTrue // Doğruluk
import org.junit.Test // JUnit
import java.nio.ByteBuffer // Ham veri
import java.nio.ByteOrder // Bayt sırası
import kotlin.math.abs // Mutlak değer
import kotlin.math.sqrt // Karekök

/**
 * CrepeMelody'nin Python karşılığıyla (araclar/model/crepe_ref.py) birebir aynı çalıştığını doğrular.
 * Test verisi (src/test/resources): 6 sn gerçek vokal (16 kHz), Python'daki TFLite modelinin çıktıları (çalışan kareler sırasıyla) ve beklenen iz.
 * Model yerine bu kayıtlı çıktıları sırayla veren sahte model kullanılır; enerji kapısı, normalizasyon, Viterbi ve güven Kotlin'de hesaplanır.
 */
class CrepeMelodyTest {
    private fun floats(name: String): FloatArray { val b = javaClass.classLoader!!.getResourceAsStream(name)!!.readBytes(); val f = FloatArray(b.size / 4); ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().get(f); return f } // Ham float32

    @Test
    fun pythonIleAyniIz() {
        val audio = floats("crepe_ses16k.raw") // Ses
        val probs = floats("crepe_olasilik.raw") // Kayıtlı model çıktıları
        val expected = javaClass.classLoader!!.getResourceAsStream("crepe_beklenen.csv")!!.bufferedReader().readLines().filter { it.isNotBlank() }.map { l -> l.split(',').map(String::toFloat) } // Beklenen
        var cursor = 0; var badNorm = 0 // Sıradaki satır, normalizasyon hatası
        val fake = CrepeMelody.FrameModel { frames, count, out -> // Sahte model
            for (j in 0 until count) { var m = 0.0; var v = 0.0; for (k in 0 until 1024) m += frames[j * 1024 + k]; m /= 1024; for (k in 0 until 1024) { val d = frames[j * 1024 + k] - m; v += d * d }
                if (abs(m) > 1e-3 || abs(sqrt(v / 1023) - 1) > 1e-3) badNorm++ } // Her kare: ortalama 0, sapma 1 olmalı
            System.arraycopy(probs, cursor * 360, out, 0, count * 360); cursor += count } // Sırayla ver
        val tr = CrepeMelody(fake).track(audio) // İz
        assertEquals("kare sayısı", expected.size, tr.hz.size) // Aynı sayıda kare
        assertEquals("tüm kayıtlı çıktılar kullanılmalı (aynı kareler çalıştı)", probs.size / 360, cursor) // Enerji kapısı aynı
        assertEquals("normalizasyon", 0, badNorm) // Normalizasyon doğru
        var same = 0; var voiced = 0 // Karşılaştırma
        for (i in expected.indices) { val e = expected[i]
            assertEquals("enerji dB, kare $i", e[3], tr.energyDb[i], 0.02f) // Enerji
            assertEquals("güven, kare $i", e[2], tr.conf[i], 0.006f) // Güven (bayt nicemleme payı)
            if (e[2] > 0.5f) { voiced++; if (abs(e[1] - tr.hz[i]) / e[1] < 0.001f) same++ } } // Melodi karelerinde perde
        assertTrue("melodi karelerinde perde aynı olmalı: $same/$voiced", voiced > 100 && same == voiced) // Hepsi aynı
        val mel = CrepeMelody(CrepeMelody.FrameModel { _, count, out -> java.util.Arrays.fill(out, 0, count * 360, 0f) }).extract(audio) // Uç durum: model hiçbir şey bulmazsa
        assertTrue("sessizlikte nota olmamalı", mel.notes.isEmpty()) // Boş melodi
    }
}

class ResampleSincTest {
    @Test
    fun sinusPerdesiVeGenligiKorunur() { // 22050 → 16000: 440 Hz sinüs aynı frekans ve genlikte kalmalı, 9 kHz (16k'nın Nyquist'i üstü) bastırılmalı
        val n = 22050; val x = FloatArray(n) { (0.5 * kotlin.math.sin(2 * Math.PI * 440 * it / 22050.0)).toFloat() } // 1 sn sinüs
        val y = Resampler.resampleSinc(x, 22050, 16000) // Dönüştür
        assertEquals(16000, y.size) // Uzunluk
        var maxErr = 0.0; for (i in 200 until y.size - 200) maxErr = maxOf(maxErr, abs(y[i] - 0.5 * kotlin.math.sin(2 * Math.PI * 440 * i / 16000.0))) // İdeal ile fark
        assertTrue("440 Hz hatası $maxErr", maxErr < 0.01) // Çok küçük
        val hi = FloatArray(n) { (0.5 * kotlin.math.sin(2 * Math.PI * 9000 * it / 22050.0)).toFloat() } // 9 kHz
        val yh = Resampler.resampleSinc(hi, 22050, 16000); var e = 0.0; for (i in 200 until yh.size - 200) e += yh[i] * yh[i] // Kalan enerji
        assertTrue("9 kHz bastırılmalı (rms ${sqrt(e / (yh.size - 400))})", sqrt(e / (yh.size - 400)) < 0.03) // −24 dB altı
    }
}
