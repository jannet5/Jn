package com.jn.melodizil.core // Saf Kotlin çekirdek testi

import com.jn.melodizil.data.audio.OrtRmvpe // ORT RMVPE
import java.io.File // Dosya
import java.nio.ByteBuffer // Bayt tamponu
import java.nio.ByteOrder // Bayt sırası
import kotlin.math.abs // Mutlak değer
import kotlin.math.ln // Doğal logaritma
import kotlin.math.pow // Üs alma
import kotlin.math.sign // İşaret
import org.junit.Assert.assertEquals // Eşitlik
import org.junit.Assert.assertTrue // Doğruluk
import org.junit.Assert.fail // Başarısız
import org.junit.Assume.assumeTrue // Koşullu atla
import org.junit.Test // Test

/** Kotlin RMVPE hattı (mel + ORT + çözücü) Python (rmvpe_model.py, aynı int8 model) çıktısıyla parite testi. Referans: src/test/resources/rmvpe/ (arastirma/ortak/rmvpe_model.py ile üretildi). */
class RmvpeParityTest {
    private val modelPath = "/home/user/models/rmvpe_q/rmvpe_int8.onnx" // Yerel model kopyası

    private fun res(name: String): ByteArray = javaClass.classLoader!!.getResourceAsStream("rmvpe/$name")!!.use { it.readBytes() } // Kaynak oku

    /** 8 bit mu-law -> float (üretici Python ile aynı formül). */
    private fun ulaw(name: String): FloatArray = res(name).let { b -> FloatArray(b.size) { i -> val y = (b[i].toInt() and 0xFF) / 255.0 * 2.0 - 1.0; (sign(y) * ((256.0).pow(abs(y)) - 1.0) / 255.0).toFloat() } }

    private fun refF32(name: String): Pair<FloatArray, FloatArray> { // [f0..., salMax...]
        val fb = ByteBuffer.wrap(res(name)).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer()
        val n = fb.remaining() / 2; val a = FloatArray(2 * n); fb.get(a)
        return a.copyOfRange(0, n) to a.copyOfRange(n, 2 * n)
    }

    private fun cents(a: Float, b: Float) = 1200.0 * ln(a.toDouble() / b.toDouble()) / ln(2.0) // İki Hz arası cent

    /** (sesli kare uyumu, ortak sesli karelerde cent farkı medyanı, sesli kare sayısı) */
    private fun compare(f0: FloatArray, ref: FloatArray): Triple<Double, Double, Int> {
        assertEquals("kare sayısı", ref.size, f0.size)
        var same = 0; val diffs = ArrayList<Double>(); var voicedRef = 0
        for (i in ref.indices) {
            if ((f0[i] > 0f) == (ref[i] > 0f)) same++
            if (ref[i] > 0f) voicedRef++
            if (f0[i] > 0f && ref[i] > 0f) diffs += abs(cents(f0[i], ref[i]))
        }
        diffs.sort()
        return Triple(same.toDouble() / ref.size, if (diffs.isEmpty()) 0.0 else diffs[diffs.size / 2], voicedRef)
    }

    @Test fun melParite() { // (a) Mel farkı
        val pcm = ulaw("klip1.ulaw")
        val mel = RmvpeMel.compute(pcm)
        assertEquals("kare sayısı = 1 + n/160", 1 + pcm.size / 160, mel.frames)
        val ref = ByteBuffer.wrap(res("klip1_mel200.u16")).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
        var maxDiff = 0.0
        for (m in 0 until 128) for (t in 0 until 200) {
            val r = (ref.get(m * 200 + t).toInt() and 0xFFFF) / 3500.0 - 11.6 // Python değeri (uint16 nicemli)
            maxDiff = maxOf(maxDiff, abs(r - mel.data[m * mel.frames + t]))
        }
        println("RMVPE_PARITE mel_maks_fark=$maxDiff (nicemleme hatasi <=1.5e-4 dahil)")
        assertTrue("mel farkı $maxDiff", maxDiff < 1e-3)
    }

    private fun f0Parite(klip: String, ref: String, windowSec: Float, overlapSec: Float, minUyum: Double, maxCent: Double): String {
        val pcm = ulaw(klip); val (rf0, _) = refF32(ref)
        return OrtRmvpe(modelPath, windowSec, overlapSec).use { m ->
            val t0 = System.nanoTime()
            val tr = m.estimate(pcm) {}
            val sn = (System.nanoTime() - t0) / 1e9
            val (uyum, cent, voiced) = compare(tr.f0, rf0)
            val msg = "RMVPE_PARITE $klip pencere=${windowSec}s bindirme=${overlapSec}s kare=${tr.size} sesli_ref=$voiced sesli_uyum=${"%.4f".format(uyum)} cent_medyan=${"%.3f".format(cent)} sure=${"%.2f".format(sn)}s"
            println(msg)
            assertTrue("$msg: sesli kare uyumu", uyum > minUyum)
            assertTrue("$msg: cent medyanı", cent < maxCent)
            msg
        }
    }

    @Test fun f0PariteTamKlip() { // (b) Tek pencere (klipler 8 sn'den kısa): Python ile aynı hesap
        assumeTrue("model yok", File(modelPath).exists())
        f0Parite("klip1.ulaw", "klip1_ref.f32", 8f, 1f, 0.97, 5.0)
        f0Parite("klip2.ulaw", "klip2_ref.f32", 8f, 1f, 0.97, 5.0)
    }

    @Test fun f0ParitePencereli() { // (b) Pencereleme/birleştirme mantığı: 5 sn'lik klip 2 sn pencere + 0.5 sn bindirmeyle (3 pencere)
        assumeTrue("model yok", File(modelPath).exists())
        f0Parite("klip1.ulaw", "klip1_ref.f32", 2f, 0.5f, 0.95, 10.0)
    }

    @Test fun onSaniyeSureBellekIlerlemeIptal() { // (c) 10 sn: süre/bellek, ilerleme tekdüze, iptal
        assumeTrue("model yok", File(modelPath).exists())
        val a = ulaw("klip1.ulaw"); val pcm = a + a // 5+5 = 10 sn
        val rt = Runtime.getRuntime(); System.gc()
        val before = rt.totalMemory() - rt.freeMemory()
        var last = -1f; var mono = true; var peak = before
        OrtRmvpe(modelPath).use { m -> // Varsayılan: 8 sn pencere, 1 sn bindirme (2 pencere)
            val t0 = System.nanoTime()
            val tr = m.estimate(pcm) { p -> if (p < last) mono = false; last = p; peak = maxOf(peak, rt.totalMemory() - rt.freeMemory()) }
            val sn = (System.nanoTime() - t0) / 1e9
            val usedMb = (peak - before) / 1048576.0
            val (rf0, _) = refF32("klip1_ref.f32")
            // Birinci yarı ~ tek klip referansı; ikinci yarı aynı sinyal -> aynı perde (kare i ile i+500)
            val first = tr.f0.copyOfRange(0, rf0.size)
            val (uyum, cent, _) = compare(first, rf0)
            var same = 0; for (i in 0 until 500) if ((tr.f0[i] > 0f) == (tr.f0[i + 500] > 0f)) same++
            println("RMVPE_PARITE 10sn kare=${tr.size} sure=${"%.2f".format(sn)}s (gercek zamanin ${"%.3f".format(sn / 10.0)} kati) JVM_heap_artis~${"%.0f".format(usedMb)}MB ilk_yari_uyum=${"%.4f".format(uyum)} cent=${"%.2f".format(cent)} yarilar_arasi_uyum=${same / 500.0}")
            assertEquals(1 + pcm.size / 160, tr.size)
            assertTrue("ilerleme tekdüze", mono); assertEquals(1f, last, 1e-6f)
            assertTrue("süre makul: ${sn}s", sn < 60.0)
            assertTrue("bellek makul: ${usedMb}MB", usedMb < 400.0)
            assertTrue("ilk yarı uyumu", uyum > 0.95)
            assertTrue("yarılar arası tutarlılık", same / 500.0 > 0.9)
        }
        // İptal: kesme bayrağı açıksa InterruptedException
        OrtRmvpe(modelPath).use { m ->
            Thread.currentThread().interrupt()
            try { m.estimate(pcm) {}; fail("InterruptedException beklenirdi") } catch (e: InterruptedException) { /* beklenen */ }
            assertTrue("bayrak temizlenmiş olmalı", !Thread.currentThread().isInterrupted)
        }
    }
}
