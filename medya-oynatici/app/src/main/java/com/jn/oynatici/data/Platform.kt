package com.jn.oynatici.data

import java.io.File
import java.net.URI

// İndirilebilen siteler
enum class Platform(val ad: String) {
    YOUTUBE("YouTube"), // youtube.com, youtu.be
    TIKTOK("TikTok"), // tiktok.com
    INSTAGRAM("Instagram"), // instagram.com
    X("X"), // x.com, twitter.com
    PINTEREST("Pinterest"), // pinterest.*, pin.it
    DIGER("İnternet"), // yt-dlp'nin tanıdığı diğer siteler
    ;

    companion object {
        // Linkin hangi siteye ait olduğunu bul
        fun bul(url: String): Platform {
            val host = runCatching { URI(url.trim()).host?.lowercase() }.getOrNull() ?: return DIGER // alan adı
            return when {
                host.endsWith("youtube.com") || host == "youtu.be" -> YOUTUBE // YouTube
                host.endsWith("tiktok.com") -> TIKTOK // TikTok
                host.endsWith("instagram.com") -> INSTAGRAM // Instagram
                host == "x.com" || host.endsWith(".x.com") || host.endsWith("twitter.com") -> X // X
                host.contains("pinterest.") || host == "pin.it" -> PINTEREST // Pinterest (tr.pinterest.com dahil)
                else -> DIGER // diğer
            }
        }

        // Klasöre yazılan platform etiketini oku (klasör ikonu için)
        fun klasordenOku(klasor: File): Platform? =
            runCatching { valueOf(File(klasor, ETIKET_DOSYASI).readText().trim()) }.getOrNull() // yoksa null

        // Klasöre platform etiketini yaz
        fun klasoreYaz(klasor: File, platform: Platform) {
            runCatching { File(klasor, ETIKET_DOSYASI).writeText(platform.name) } // gizli dosya
        }

        const val ETIKET_DOSYASI = ".platform" // klasördeki gizli platform dosyası
        const val LINK_DOSYASI = "Linkler.txt" // klasördeki link listesi dosyası
        // TikTok, yt-dlp'nin varsayılan tarayıcı kimliğini engelliyor; masaüstü Firefox kimliğiyle sorunsuz çalışıyor
        const val FIREFOX_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0"
    }
}
