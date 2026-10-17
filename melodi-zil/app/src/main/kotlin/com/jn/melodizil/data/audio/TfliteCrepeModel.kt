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
    private var inBuf: ByteBuffer = ByteBuffer.allocateDirect(4) // Girdi (toplu boyutuna tam eşit olmalı: TFLite tamponun kapasitesini tensörle karşılaştırır)
    private var outBuf: ByteBuffer = ByteBuffer.allocateDirect(4) // Çıktı
    private var batch = -1 // Şu anki toplu boyutu

    init {
        val fd = context.assets.openFd(MODEL) // Sıkıştırılmamış varlık (build.gradle: noCompress tflite)
        val mapped = FileInputStream(fd.fileDescriptor).channel.map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength) // Eşle
        interpreter = Interpreter(mapped, Interpreter.Options().setNumThreads(threads)) // XNNPACK varsayılan açık
    }

    override fun predict(frames: FloatArray, count: Int, out: FloatArray) {
        if (count != batch) { // Boyut değişti (yalnızca son, eksik toplu için)
            interpreter.resizeInput(0, intArrayOf(count, CrepeMelody.W)); interpreter.allocateTensors(); batch = count // Tensörü yeniden boyutla
            inBuf = ByteBuffer.allocateDirect(count * CrepeMelody.W * 4).order(ByteOrder.nativeOrder()) // Tam boy girdi tamponu
            outBuf = ByteBuffer.allocateDirect(count * CrepeMelody.NB * 4).order(ByteOrder.nativeOrder()) // Tam boy çıktı tamponu
        }
        inBuf.rewind(); inBuf.asFloatBuffer().put(frames, 0, count * CrepeMelody.W) // Girdi
        outBuf.rewind(); interpreter.run(inBuf, outBuf) // Çalıştır
        outBuf.rewind(); outBuf.asFloatBuffer().get(out, 0, count * CrepeMelody.NB) // Oku
    }

    override fun close() = interpreter.close() // Serbest bırak

    companion object { const val MODEL = "crepe_tiny.tflite" } // Varlık adı
}
