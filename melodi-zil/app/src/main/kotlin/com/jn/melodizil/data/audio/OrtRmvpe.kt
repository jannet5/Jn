package com.jn.melodizil.data.audio // Ses verisi katmanı

import ai.onnxruntime.OnnxTensor // ORT tensörü
import ai.onnxruntime.OrtEnvironment // ORT ortamı
import ai.onnxruntime.OrtSession // ORT oturumu
import com.jn.melodizil.core.PitchTrack // Perde izi
import com.jn.melodizil.core.RmvpeDecoder // Salience -> Hz
import com.jn.melodizil.core.RmvpeMel // Log-mel
import java.nio.FloatBuffer // Düz float tampon
import kotlin.math.max // Büyük olan
import kotlin.math.min // Küçük olan
import kotlin.math.roundToInt // Yuvarlama

/**
 * RMVPE int8 ONNX modeli (ONNX Runtime) ile karışık sesten ana ezgi perdesini çıkarır.
 * Ses, bellek yumuşak olsun diye [windowSec] saniyelik pencerelerle ([overlapSec] bindirmeli) işlenip birleştirilir.
 * İptal: her pencere başında Thread.interrupted() bakılır; kesilmişse InterruptedException fırlar.
 */
class OrtRmvpe(private val modelPath: String, private val windowSec: Float = 8f, private val overlapSec: Float = 1f, private val threads: Int = 0) : AutoCloseable {
    private var options: OrtSession.SessionOptions? = null // Oturum ayarları
    private var session: OrtSession? = null // Model oturumu (ilk kullanımda açılır)

    private fun openSession(): OrtSession {
        session?.let { return it } // Açıksa tekrar kullan
        val th = if (threads > 0) threads else Runtime.getRuntime().availableProcessors().coerceIn(2, 4) // Çekirdek sayısı (2..4)
        val o = OrtSession.SessionOptions()
        o.setIntraOpNumThreads(th) // İşlem içi iş parçacığı
        o.setInterOpNumThreads(1) // İşlemler arası tek
        o.setMemoryPatternOptimization(false) // Son pencere farklı boyutta: bellek deseni tutma
        options = o
        val s = OrtEnvironment.getEnvironment().createSession(modelPath, o)
        session = s
        return s
    }

    /** [pcm16k]: 16 kHz mono float [-1,1]. [onProgress]: 0..1. */
    fun estimate(pcm16k: FloatArray, onProgress: (Float) -> Unit): PitchTrack {
        onProgress(0f)
        if (Thread.interrupted()) throw InterruptedException("RMVPE iptal edildi") // İptal
        val env = OrtEnvironment.getEnvironment()
        val sess = openSession()
        val inName = sess.inputNames.first() // 'input'
        val mel = RmvpeMel.compute(pcm16k) // Tüm sesin log-mel'i (kare başına yerel, pencereden bağımsız)
        val total = mel.frames
        val f0 = FloatArray(total); val sal = FloatArray(total) // Birleşik sonuç
        onProgress(0.03f)

        val win = max(32, (windowSec * 100f).roundToInt()) // Pencere kare sayısı
        val ov = (overlapSec * 100f).roundToInt().coerceIn(0, win / 2) // Bindirme kare sayısı
        val stride = win - ov // Pencere adımı
        val starts = ArrayList<Int>() // Pencere başlangıçları
        var s0 = 0
        while (true) {
            if (s0 + win >= total) { starts += max(0, total - win); break } // Son pencere sona hizalı
            starts += s0; s0 += stride
        }
        var written = 0 // Sonuca yazılmış kare sayısı
        for ((i, ws) in starts.withIndex()) {
            if (Thread.interrupted()) throw InterruptedException("RMVPE iptal edildi") // İptal
            val n = min(total, ws + win) - ws // Penceredeki gerçek kare
            val tp = 32 * ((n - 1) / 32 + 1) // 32'nin katına dolgu (modelin U-Net'i için)
            val inp = FloatArray(RmvpeMel.N_MELS * tp)
            for (m in 0 until RmvpeMel.N_MELS) { // Mel-major kopya + yansıtmalı dolgu
                val src = m * total + ws; val dst = m * tp
                for (j in 0 until tp) inp[dst + j] = mel.data[src + (if (j < n) j else RmvpeMel.reflectIndex(j, n))]
            }
            val outSal: FloatArray = OnnxTensor.createTensor(env, FloatBuffer.wrap(inp), longArrayOf(1, RmvpeMel.N_MELS.toLong(), tp.toLong())).use { t ->
                sess.run(mapOf(inName to t)).use { r ->
                    val fb = (r.get(0) as OnnxTensor).floatBuffer // [1, tp, 360]
                    val arr = FloatArray(fb.remaining()); fb.get(arr); arr // Düz kopya
                }
            }
            val last = i == starts.size - 1
            val to = if (last) total else { val ns = starts[i + 1]; ns + (ws + win - ns) / 2 } // Bindirmenin ortası: iki pencerenin sınırı
            if (to > written) { // Bu pencerenin sahip olduğu kareler [written, to)
                RmvpeDecoder.decode(outSal, (written - ws) * RmvpeDecoder.BINS, to - written, f0, sal, written)
                written = to
            }
            onProgress(0.03f + 0.97f * (i + 1) / starts.size)
        }
        return PitchTrack(0.01f, f0, sal)
    }

    override fun close() { // Kaynakları bırak
        try { session?.close() } catch (_: Exception) {}
        try { options?.close() } catch (_: Exception) {}
        session = null; options = null
    }
}
