package com.jn.melodizil.data.audio // Ses çözme katmanı

import android.media.AudioFormat // PCM kodlama sabitleri
import android.media.MediaCodec // Donanım/yazılım çözücü
import android.media.MediaExtractor // Kapsayıcı okuyucu
import android.media.MediaFormat // Biçim bilgisi
import com.jn.melodizil.core.Resampler // Mono + yeniden örnekleme
import java.io.File // Dosya
import java.nio.ByteOrder // Bayt sırası

/** Sıkıştırılmış ses dosyasını (m4a, webm/opus, mp3, ogg, wav…) mono float PCM'e çözer. */
object AudioDecoder {
    class DecodeException(message: String) : Exception(message) // Çözme hatası

    /** Dosyayı hedef örnekleme hızında mono PCM (-1..1) olarak döndürür. */
    fun decodeToMono(file: File, targetRate: Int, onProgress: (Float) -> Unit = {}): FloatArray {
        val extractor = MediaExtractor() // Okuyucu
        try {
            extractor.setDataSource(file.absolutePath) // Kaynak
            val trackIndex = (0 until extractor.trackCount).firstOrNull { extractor.getTrackFormat(it).getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true } ?: throw DecodeException("Dosyada ses parçası yok") // Ses parçası
            extractor.selectTrack(trackIndex) // Parça seçildi
            val format = extractor.getTrackFormat(trackIndex) // Biçim
            val mime = format.getString(MediaFormat.KEY_MIME)!! // MIME
            val durationUs = if (format.containsKey(MediaFormat.KEY_DURATION)) format.getLong(MediaFormat.KEY_DURATION) else 0L // Süre
            val codec = MediaCodec.createDecoderByType(mime) // Çözücü
            try {
                codec.configure(format, null, null, 0) // Yapılandır
                codec.start() // Başlat
                return pump(extractor, codec, durationUs, targetRate, onProgress) // Çöz
            } finally { codec.stop(); codec.release() } // Çözücü serbest
        } finally { extractor.release() } // Okuyucu serbest
    }

    /** Giriş/çıkış tamponlarını döndürür; çıkan PCM'i float olarak toplar. */
    private fun pump(extractor: MediaExtractor, codec: MediaCodec, durationUs: Long, targetRate: Int, onProgress: (Float) -> Unit): FloatArray {
        val chunks = ArrayList<FloatArray>() // Çözülmüş parçalar (kaynak hızda, mono)
        var sampleRate = 0; var channels = 0; var encoding = AudioFormat.ENCODING_PCM_16BIT // Çıkış biçimi
        val info = MediaCodec.BufferInfo() // Tampon bilgisi
        var inputDone = false; var outputDone = false // Durumlar
        var totalSamples = 0L // Toplam mono örnek
        while (!outputDone) { // Çıkış bitene kadar
            if (!inputDone) { // Giriş beslemesi
                val inIdx = codec.dequeueInputBuffer(10_000) // Boş giriş tamponu
                if (inIdx >= 0) { // Varsa
                    val buf = codec.getInputBuffer(inIdx)!! // Tampon
                    val n = extractor.readSampleData(buf, 0) // Veri oku
                    if (n < 0) { codec.queueInputBuffer(inIdx, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM); inputDone = true } // Akış sonu
                    else { codec.queueInputBuffer(inIdx, 0, n, extractor.sampleTime, 0); extractor.advance() } // Çözücüye ver
                }
            }
            val outIdx = codec.dequeueOutputBuffer(info, 10_000) // Çıkış tamponu
            when {
                outIdx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> { // Biçim öğrenildi
                    val f = codec.outputFormat // Çıkış biçimi
                    sampleRate = f.getInteger(MediaFormat.KEY_SAMPLE_RATE) // Hız
                    channels = f.getInteger(MediaFormat.KEY_CHANNEL_COUNT) // Kanal
                    if (f.containsKey(MediaFormat.KEY_PCM_ENCODING)) encoding = f.getInteger(MediaFormat.KEY_PCM_ENCODING) // Kodlama
                }
                outIdx >= 0 -> { // Veri geldi
                    if (sampleRate == 0) { val f = codec.outputFormat; sampleRate = f.getInteger(MediaFormat.KEY_SAMPLE_RATE); channels = f.getInteger(MediaFormat.KEY_CHANNEL_COUNT) } // Biçim gelmemişse al
                    val buf = codec.getOutputBuffer(outIdx)!! // Tampon
                    buf.position(info.offset); buf.limit(info.offset + info.size) // Geçerli aralık
                    val interleaved = when (encoding) { // Float'a çevir
                        AudioFormat.ENCODING_PCM_FLOAT -> FloatArray(info.size / 4).also { buf.order(ByteOrder.nativeOrder()).asFloatBuffer().get(it) } // Float PCM
                        else -> { val sb = buf.order(ByteOrder.nativeOrder()).asShortBuffer(); FloatArray(sb.remaining()) { sb.get() / 32768f } } // 16-bit PCM
                    }
                    val mono = Resampler.toMono(interleaved, maxOf(channels, 1)) // Mono
                    chunks.add(mono); totalSamples += mono.size // Topla
                    codec.releaseOutputBuffer(outIdx, false) // Tamponu geri ver
                    if (durationUs > 0) onProgress((info.presentationTimeUs.toFloat() / durationUs).coerceIn(0f, 1f)) // İlerleme
                    if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) outputDone = true // Son
                }
            }
        }
        if (sampleRate == 0 || totalSamples == 0L) throw DecodeException("Ses çözülemedi") // Boş
        val all = FloatArray(totalSamples.toInt()) // Birleştir
        var pos = 0 // Konum
        for (c in chunks) { System.arraycopy(c, 0, all, pos, c.size); pos += c.size } // Kopyala
        return Resampler.resample(all, sampleRate, targetRate) // Hedef hıza getir
    }
}
