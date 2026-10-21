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
            fun alan(vararg adlar: String) = adlar.any { host == it || host.endsWith(".$it") } // tam alan adı ya da alt alanı
            return when {
                alan("youtube.com", "youtu.be", "youtube-nocookie.com") -> YOUTUBE // YouTube (m., music. dahil)
                alan("tiktok.com") -> TIKTOK // TikTok
                alan("instagram.com") -> INSTAGRAM // Instagram
                alan("x.com", "twitter.com") -> X // X
                host == "pin.it" || Regex("""(^|\.)pinterest\.(com|[a-z]{2,3}|co\.[a-z]{2}|com\.[a-z]{2})$""").containsMatchIn(host) -> PINTEREST // Pinterest (tr.pinterest.com, pinterest.co.uk dahil)
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
