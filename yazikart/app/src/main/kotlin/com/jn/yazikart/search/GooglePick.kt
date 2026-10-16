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
