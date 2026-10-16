package com.jn.melodizil.core // Saf Kotlin çekirdek

import java.io.ByteArrayOutputStream // Bellek içi çıkış
import java.nio.ByteBuffer // Küçük-endian başlık yazımı
import java.nio.ByteOrder // Bayt sırası

/** 16-bit PCM mono WAV dosyası üretir. Android zil sesi olarak doğrudan kabul edilir. */
object WavWriter {
    fun toWav(pcm: FloatArray, sampleRate: Int): ByteArray {
        val dataSize = pcm.size * 2 // 16 bit = 2 bayt/örnek
        val buf = ByteBuffer.allocate(44 + dataSize).order(ByteOrder.LITTLE_ENDIAN) // Başlık + veri
        buf.put("RIFF".toByteArray(Charsets.US_ASCII)) // RIFF imzası
        buf.putInt(36 + dataSize) // Dosya boyutu - 8
        buf.put("WAVE".toByteArray(Charsets.US_ASCII)) // WAVE biçimi
        buf.put("fmt ".toByteArray(Charsets.US_ASCII)) // fmt alt parçası
        buf.putInt(16) // fmt uzunluğu
        buf.putShort(1) // PCM
        buf.putShort(1) // Mono
        buf.putInt(sampleRate) // Örnekleme hızı
        buf.putInt(sampleRate * 2) // Bayt/saniye
        buf.putShort(2) // Blok hizası
        buf.putShort(16) // Bit derinliği
        buf.put("data".toByteArray(Charsets.US_ASCII)) // data alt parçası
        buf.putInt(dataSize) // Veri uzunluğu
        for (v in pcm) { // Her örnek
            val c = v.coerceIn(-1f, 1f) // Kırpma
            buf.putShort((c * 32767f).toInt().toShort()) // 16-bit'e çevir
        }
        return buf.array() // Bayt dizisi
    }
}
