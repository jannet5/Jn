package com.jn.yazikart.data // veri katmanı paketi

// Arka plana eklenen tek bir video parçası: hangi saniyeden hangi saniyeye oynayacağı ayarlanabilir
data class VideoClip(
    val uri: String, // video dosyasının adresi (galeri content:// ya da indirilen file://)
    val durationMs: Long, // videonun tam uzunluğu (milisaniye)
    val startMs: Long = 0L, // oynamaya başlayacağı yer
    val endMs: Long = durationMs, // oynamayı bitireceği yer
) {
    val lengthMs: Long get() = (endMs - startMs).coerceAtLeast(0L) // kırpılmış uzunluk
}

// Videolarla ilgili saf hesaplar (test edilebilir)
object Clips {
    const val MAX_CLIPS = 5 // en fazla kaç video arka arkaya eklenebilir (uygulama hafif kalsın)
    const val MIN_LENGTH_MS = 500L // bir parça en az yarım saniye olmalı

    // Tüm parçaların toplam süresi
    fun totalMs(clips: List<VideoClip>): Long = clips.sumOf { it.lengthMs } // kırpılmış uzunluklar toplanıyor

    // Kullanıcının seçtiği aralığı geçerli hale getirir (sınır dışı ya da çok kısaysa düzeltir)
    fun clampRange(clip: VideoClip, startMs: Long, endMs: Long): VideoClip {
        val d = clip.durationMs // tam uzunluk
        var s = startMs.coerceIn(0L, d) // başlangıç video içinde
        var e = endMs.coerceIn(0L, d) // bitiş video içinde
        if (e - s < MIN_LENGTH_MS) { // çok kısa kaldıysa
            if (s + MIN_LENGTH_MS <= d) e = s + MIN_LENGTH_MS // bitiş ileri alınıyor
            else { e = d; s = (d - MIN_LENGTH_MS).coerceAtLeast(0L) } // sona dayandıysa başlangıç geri alınıyor
        }
        return clip.copy(startMs = s, endMs = e) // düzeltilmiş parça
    }

    // Listede bir parçayı bir sıra yukarı taşır (sırayı değiştirmek için)
    fun moveUp(clips: List<VideoClip>, index: Int): List<VideoClip> {
        if (index !in 1..clips.lastIndex) return clips // en üstteyse ya da yoksa değişmez
        val m = clips.toMutableList() // değiştirilebilir kopya
        val t = m[index - 1]; m[index - 1] = m[index]; m[index] = t // yer değiştiriliyor
        return m // yeni sıra
    }

    // Süreyi "0:05" ya da "1:23" biçiminde yazar
    fun format(ms: Long): String {
        val totalSec = (ms + 500) / 1000 // en yakın saniyeye yuvarlanıyor
        return "%d:%02d".format(totalSec / 60, totalSec % 60) // dakika:saniye
    }
}
