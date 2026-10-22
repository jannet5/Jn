package com.jn.yazikart.data // veri katmanı paketi

// Sıradaki tek bir parça: video ya da fotoğraf. Video için hangi saniyeden hangi saniyeye oynayacağı,
// fotoğraf için ekranda kaç saniye kalacağı (0'dan endMs'e) ayarlanır.
data class VideoClip(
    val uri: String, // dosyanın adresi (galeri content:// ya da uygulamanın kopyası file://)
    val durationMs: Long, // video: tam uzunluk · fotoğraf: en fazla kalabileceği süre
    val startMs: Long = 0L, // oynamaya başlayacağı yer (fotoğrafta hep 0)
    val endMs: Long = durationMs, // oynamayı bitireceği yer (fotoğrafta ekranda kalma süresi)
    val isPhoto: Boolean = false, // fotoğraf mı
) {
    val lengthMs: Long get() = (endMs - startMs).coerceAtLeast(0L) // ekranda kalacağı süre
}

// Müzik: telefondaki bir ses dosyası; videonun altında çalar
data class MusicTrack(
    val uri: String, // ses dosyasının adresi
    val name: String, // ekranda görünen adı
    val durationMs: Long, // şarkının uzunluğu
    val startMs: Long = 0L, // şarkının hangi saniyesinden başlayacağı
    val volume: Float = 0.8f, // müzik sesi (0..1)
)

// Sıra (video + fotoğraf) ile ilgili saf hesaplar (test edilebilir)
object Clips {
    const val MAX_CLIPS = 10 // en fazla kaç parça arka arkaya eklenebilir (uygulama hafif kalsın)
    const val MIN_LENGTH_MS = 500L // bir parça en az yarım saniye olmalı
    const val PHOTO_DEFAULT_MS = 3_000L // fotoğraf varsayılan olarak 3 saniye görünür
    const val PHOTO_MAX_MS = 10_000L // bir fotoğraf en fazla 10 saniye

    // Yeni fotoğraf parçası
    fun photo(uri: String): VideoClip = VideoClip(uri, PHOTO_MAX_MS, 0L, PHOTO_DEFAULT_MS, isPhoto = true) // 3 sn, en fazla 10 sn

    // Tüm parçaların toplam süresi
    fun totalMs(clips: List<VideoClip>): Long = clips.sumOf { it.lengthMs } // kırpılmış uzunluklar toplanıyor

    // Kullanıcının seçtiği aralığı geçerli hale getirir (sınır dışı ya da çok kısaysa düzeltir)
    fun clampRange(clip: VideoClip, startMs: Long, endMs: Long): VideoClip {
        val d = clip.durationMs // tam uzunluk
        var s = if (clip.isPhoto) 0L else startMs.coerceIn(0L, d) // fotoğraf hep baştan başlar
        var e = endMs.coerceIn(0L, d) // bitiş sınır içinde
        if (e - s < MIN_LENGTH_MS) { // çok kısa kaldıysa
            if (s + MIN_LENGTH_MS <= d) e = s + MIN_LENGTH_MS // bitiş ileri alınıyor
            else { e = d; s = (d - MIN_LENGTH_MS).coerceAtLeast(0L) } // sona dayandıysa başlangıç geri alınıyor
        }
        return clip.copy(startMs = s, endMs = e) // düzeltilmiş parça
    }

    // Parçayı listede başka bir sıraya taşır (sola/sağa almak için)
    fun move(clips: List<VideoClip>, from: Int, to: Int): List<VideoClip> {
        if (from !in clips.indices || to !in clips.indices || from == to) return clips // geçersizse değişmez
        val m = clips.toMutableList() // değiştirilebilir kopya
        val item = m.removeAt(from) // yerinden alınıyor
        m.add(to, item) // yeni yerine konuyor
        return m // yeni sıra
    }

    // Listede bir parçayı bir sıra yukarı (sola) taşır
    fun moveUp(clips: List<VideoClip>, index: Int): List<VideoClip> = move(clips, index, index - 1) // bir önceki yere

    // Süreyi "0:05" ya da "1:23" biçiminde yazar
    fun format(ms: Long): String {
        val totalSec = (ms + 500) / 1000 // en yakın saniyeye yuvarlanıyor
        return "%d:%02d".format(totalSec / 60, totalSec % 60) // dakika:saniye
    }

    // Süreyi ondalıklı saniye olarak yazar: "2,5 sn"
    fun seconds(ms: Long): String = "%.1f sn".format(java.util.Locale("tr"), ms / 1000.0) // Türkçe virgüllü
}
