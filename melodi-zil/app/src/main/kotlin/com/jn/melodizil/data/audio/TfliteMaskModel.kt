package com.jn.melodizil.data.audio // Ses katmanı

import android.content.Context // Bağlam
import com.jn.melodizil.core.VocalSeparator // Ayırıcı
import org.tensorflow.lite.Interpreter // TFLite yorumlayıcı
import java.io.FileInputStream // Model dosyası
import java.nio.ByteBuffer // Tampon
import java.nio.ByteOrder // Bayt sırası
import java.nio.MappedByteBuffer // Bellek eşleme
import java.nio.channels.FileChannel // Kanal

/** assets/vokal.tflite (Spleeter vokal U-Net, fp16) için TFLite sarmalayıcı. Model belleğe eşlenir (kopyalanmaz). */
class TfliteMaskModel(context: Context, threads: Int = 4) : VocalSeparator.MaskModel, AutoCloseable {
    private val interpreter: Interpreter // Yorumlayıcı
    private val inBuf = ByteBuffer.allocateDirect(SIZE * 4).order(ByteOrder.nativeOrder()) // Girdi (4 MB)
    private val outBuf = ByteBuffer.allocateDirect(SIZE * 4).order(ByteOrder.nativeOrder()) // Çıktı (4 MB)

    init {
        val fd = context.assets.openFd(MODEL) // Sıkıştırılmamış varlık (build.gradle: noCompress tflite)
        val mapped: MappedByteBuffer = FileInputStream(fd.fileDescriptor).channel.map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength) // Eşle
        interpreter = Interpreter(mapped, Interpreter.Options().setNumThreads(threads)) // XNNPACK varsayılan açık
    }

    override fun predict(input: FloatArray, output: FloatArray) {
        inBuf.rewind(); inBuf.asFloatBuffer().put(input) // Girdiyi yaz
        outBuf.rewind(); interpreter.run(inBuf, outBuf) // Çalıştır
        outBuf.rewind(); outBuf.asFloatBuffer().get(output) // Çıktıyı oku
    }

    override fun close() = interpreter.close() // Serbest bırak

    companion object {
        const val MODEL = "vokal.tflite" // Varlık adı
        const val SIZE = VocalSeparator.T * VocalSeparator.F * 2 // [1,512,1024,2]
    }
}
