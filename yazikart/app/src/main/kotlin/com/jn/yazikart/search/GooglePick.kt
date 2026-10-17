package com.jn.yazikart.search // görsel arama paketi

import java.net.URLDecoder // adres içindeki %xx kodlarını çözmek için
import java.net.URLEncoder // aranan kelimeyi adrese koymak için

// Google Görseller sayfasında basılı tutulan görselden en iyi (en büyük) adresi seçen saf hesaplar
object GooglePick {

    // Google Görseller arama adresi (Türkçe arayüz)
    fun searchUrl(query: String): String =
        "https://www.google.com/search?tbm=isch&hl=tr&q=" + URLEncoder.encode(query.trim(), "UTF-8") // kelime adrese kodlanıyor

    // Basılı tutulan öğeden gelen adreslerden en iyisini seçer:
    // 1) bağlantıdaki "imgurl=" (görselin orijinal adresi), 2) http(s) resim adresi, 3) sayfaya gömülü küçük resim (data:)
    fun best(href: String?, src: String?, extra: String?): String? {
        for (h in listOf(href, src, extra)) { // önce bağlantılarda orijinal adres aranıyor
            val orig = h?.let { imgUrlParam(it) } // imgurl parametresi
            if (orig != null) return orig // bulunduysa en iyisi bu
        }
        listOf(src, extra).firstOrNull { it != null && (it.startsWith("https://") || it.startsWith("http://")) }?.let { return it } // normal resim adresi
        return listOf(src, extra).firstOrNull { it != null && it.startsWith("data:image") } // son çare: gömülü küçük resim
    }

    // Adresteki imgurl=... değerini çözer (yoksa null)
    fun imgUrlParam(url: String): String? {
        val q = url.substringAfter('?', "") // soru işaretinden sonrası
        val raw = q.split('&').firstOrNull { it.startsWith("imgurl=") }?.removePrefix("imgurl=") ?: return null // imgurl değeri
        val decoded = runCatching { URLDecoder.decode(raw, "UTF-8") }.getOrNull() ?: return null // %xx çözülüyor
        return decoded.takeIf { it.startsWith("http://") || it.startsWith("https://") } // sadece internet adresi kabul
    }
}

// Ücretsiz video siteleri: videoları her yerde kullanmaya izin verirler. Uygulama sitedeki OYNAYAN videoyu doğrudan alır,
// bu yüzden sitenin "İndir" düğmesine ve girişe (login) gerek kalmaz.
enum class VideoSite(val label: String, val home: String) {
    PEXELS("Pexels", "https://www.pexels.com/tr-tr/videolar/"), // en büyük arşiv
    PIXABAY("Pixabay", "https://pixabay.com/tr/videos/"), // büyük arşiv
    COVERR("Coverr", "https://coverr.co/"), // dikey/kısa videolar
    MIXKIT("Mixkit", "https://mixkit.co/free-stock-video/"); // seçkin klipler

    // Sitede arama adresi
    fun searchUrl(query: String): String {
        val q = java.net.URLEncoder.encode(query.trim(), "UTF-8").replace("+", "%20") // boşluk %20
        return when (this) {
            PEXELS -> "https://www.pexels.com/tr-tr/arama/videos/$q/" // Pexels arama
            PIXABAY -> "https://pixabay.com/tr/videos/search/$q/" // Pixabay arama
            COVERR -> "https://coverr.co/s?q=$q" // Coverr arama
            MIXKIT -> "https://mixkit.co/free-stock-video/${q.replace("%20", "-")}/" // Mixkit konu sayfası
        }
    }
}

// Sayfadaki video öğelerinden eklenecek olanı seçen saf hesap
object VideoPick {
    // Sayfadaki tek bir <video> öğesi
    data class Candidate(val src: String, val playing: Boolean, val area: Float)

    // Önce oynayan, sonra ekranda en büyük görünen video; o da yoksa sayfanın en son yüklediği .mp4 dosyası.
    // "blob:" adresleri (YouTube/Instagram gibi korumalı oynatıcılar) alınamaz.
    fun choose(candidates: List<Candidate>, lastSeenMp4: String?): String? {
        val ok = candidates.filter { it.src.startsWith("https://") || it.src.startsWith("http://") } // sadece doğrudan dosya adresleri
        ok.filter { it.playing }.maxByOrNull { it.area }?.let { return it.src } // oynayan en büyük video
        ok.filter { it.area > 0f }.maxByOrNull { it.area }?.let { return it.src } // ekranda görünen en büyük video
        return lastSeenMp4 // son çare: sayfanın yüklediği son mp4
    }

    // Sayfadaki videolar korumalı mı (blob:) — kullanıcıya doğru mesaj vermek için
    fun onlyProtected(candidates: List<Candidate>): Boolean =
        candidates.isNotEmpty() && candidates.all { it.src.startsWith("blob:") } // hepsi korumalıysa

    // Adres bir video dosyası mı (sayfanın arka planda yüklediklerini yakalamak için)
    fun looksLikeVideoFile(url: String): Boolean {
        val path = url.substringBefore('?').lowercase() // soru işaretinden önceki kısım
        return path.endsWith(".mp4") || path.endsWith(".webm") || path.endsWith(".mov") // video uzantıları
    }
}
