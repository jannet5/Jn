package com.jn.melodizil.data.audio // Ses katmanı

import android.media.AudioAttributes // Ses öznitelikleri
import android.media.AudioFormat // Biçim
import android.media.AudioTrack // Ham PCM oynatıcı
import kotlinx.coroutines.CoroutineScope // Kapsam
import kotlinx.coroutines.Dispatchers // İş parçacığı
import kotlinx.coroutines.Job // İş
import kotlinx.coroutines.flow.MutableStateFlow // Durum akışı
import kotlinx.coroutines.flow.StateFlow // Salt okunur akış
import kotlinx.coroutines.isActive // Aktiflik
import kotlinx.coroutines.launch // Başlat

/** Sentezlenen PCM'i önizleme için çalar. Tek seferde tek ses; yeniden çağrılınca önceki durur. */
class PcmPlayer(private val scope: CoroutineScope) {
    private var track: AudioTrack? = null // Aktif oynatıcı
    private var job: Job? = null // Yazma işi
    private val _isPlaying = MutableStateFlow(false) // Oynatma durumu
    val isPlaying: StateFlow<Boolean> = _isPlaying // Dışa salt okunur
    private val _positionSec = MutableStateFlow(0f) // Konum (saniye)
    val positionSec: StateFlow<Float> = _positionSec // Dışa salt okunur

    fun play(pcm: FloatArray, sampleRate: Int) {
        stop() // Önceki durur
        val minBuf = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_FLOAT) // En küçük tampon
        val t = AudioTrack.Builder() // Oynatıcı
            .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()) // Medya sesi
            .setAudioFormat(AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_FLOAT).setSampleRate(sampleRate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build()) // Float mono
            .setBufferSizeInBytes(maxOf(minBuf, 16384)) // Tampon
            .setTransferMode(AudioTrack.MODE_STREAM) // Akış modu
            .build() // Oluştur
        track = t; _isPlaying.value = true; _positionSec.value = 0f // Durum
        t.play() // Başlat
        job = scope.launch(Dispatchers.IO) { // Arka planda yaz
            var off = 0 // Konum
            val chunk = 4096 // Parça
            while (isActive && off < pcm.size) { // Tüm veri
                val n = minOf(chunk, pcm.size - off) // Yazılacak
                val w = t.write(pcm, off, n, AudioTrack.WRITE_BLOCKING) // Yaz
                if (w < 0) break // Hata
                off += w; _positionSec.value = off.toFloat() / sampleRate // İlerle
            }
            if (isActive) { try { t.stop() } catch (_: Exception) {}; _isPlaying.value = false } // Doğal bitiş
        }
    }

    fun stop() {
        job?.cancel(); job = null // İş iptal
        track?.let { try { it.pause(); it.flush(); it.stop() } catch (_: Exception) {}; it.release() } // Serbest
        track = null; _isPlaying.value = false; _positionSec.value = 0f // Sıfırla
    }
}
