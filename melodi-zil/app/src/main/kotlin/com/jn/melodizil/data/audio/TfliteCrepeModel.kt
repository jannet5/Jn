package com.jn.melodizil.data.audio // Ses katmanı

import android.content.Context // Bağlam
import com.jn.melodizil.core.CrepeMelody // Perde izleyici
import org.tensorflow.lite.Interpreter // TFLite yorumlayıcı
import java.io.FileInputStream // Model dosyası
import java.nio.ByteBuffer // Tampon
import java.nio.ByteOrder // Bayt sırası
import java.nio.channels.FileChannel // Kanal

/** assets/crepe_tiny.tflite (CREPE tiny perde modeli, 2 MB) için TFLite sarmalayıcı. Toplu boyutu değişince girdi yeniden boyutlanır. */
class TfliteCrepeModel(context: Context, threads: Int = 4) : CrepeMelody.FrameModel, AutoCloseable {
    private val interpreter: Interpreter // Yorumlayıcı
    private val inBuf = ByteBuffer.allocateDirect(CrepeMelody.BATCH * CrepeMelody.W * 4).order(ByteOrder.nativeOrder()) // Girdi (1 MB)
    private val outBuf = ByteBuffer.allocateDirect(CrepeMelody.BATCH * CrepeMelody.NB * 4).order(ByteOrder.nativeOrder()) // Çıktı (360 KB)
    private var batch = -1 // Şu anki toplu boyutu

    init {
        val fd = context.assets.openFd(MODEL) // Sıkıştırılmamış varlık (build.gradle: noCompress tflite)
        val mapped = FileInputStream(fd.fileDescriptor).channel.map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength) // Eşle
        interpreter = Interpreter(mapped, Interpreter.Options().setNumThreads(threads)) // XNNPACK varsayılan açık
    }

    override fun predict(frames: FloatArray, count: Int, out: FloatArray) {
        if (count != batch) { interpreter.resizeInput(0, intArrayOf(count, CrepeMelody.W)); interpreter.allocateTensors(); batch = count } // Boyut değişti
        inBuf.clear(); inBuf.asFloatBuffer().put(frames, 0, count * CrepeMelody.W); inBuf.limit(count * CrepeMelody.W * 4) // Girdi
        outBuf.clear(); outBuf.limit(count * CrepeMelody.NB * 4) // Çıktı alanı
        interpreter.run(inBuf, outBuf) // Çalıştır
        outBuf.rewind(); outBuf.asFloatBuffer().get(out, 0, count * CrepeMelody.NB) // Oku
    }

    override fun close() = interpreter.close() // Serbest bırak

    companion object { const val MODEL = "crepe_tiny.tflite" } // Varlık adı
}
