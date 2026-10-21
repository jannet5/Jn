package com.jn.oynatici.data

import java.net.URI

// Link türü: tek içerik mi, seçilebilir liste mi (oynatma listesi / pano), yoksa kanal/profil mi
enum class LinkTuru { TEK, LISTE, PROFIL }

// Profilde hangi bölümün indirileceği (YouTube: Videolar/Shorts, Instagram: Reels/Gönderiler)
enum class Sekme(val ad: String) { VIDEOLAR("Videolar"), SHORTS("Shorts"), REELS("Reels"), GONDERILER("Gönderiler") }

// Linki çözümlenmiş hali
data class CozulmusLink(
    val url: String, // temizlenmiş link
    val platform: Platform, // site
    val turu: LinkTuru, // tek / liste / profil
    val kullanici: String? = null, // profil linkiyse kullanıcı adı (@ olmadan)
    val sekmeler: List<Sekme> = emptyList(), // seçilebilecek bölümler (boşsa sorulmaz)
)

// Linke bakıp ne olduğunu anlayan yardımcı (internete çıkmadan, sadece adres kalıbından)
object LinkCozucu {

    // X ve Instagram'da kullanıcı adı olamayan, sitenin kendi sayfaları
    private val xAyrilmis = setOf("home", "explore", "search", "i", "settings", "notifications", "messages", "compose", "intent", "hashtag", "share", "login", "signup", "tos", "privacy")
    private val igAyrilmis = setOf("p", "reel", "reels", "tv", "stories", "explore", "accounts", "direct", "about", "developer", "legal", "web", "share", "s", "highlights", "api", "graphql")
    private val pinAyrilmis = setOf("pin", "search", "ideas", "today", "settings", "business", "login", "_", "explore")

    fun coz(hamLink: String): CozulmusLink {
        var url = hamLink.trim() // boşlukları at
        if (Platform.bul(url) == Platform.TIKTOK) url = url.replace(Regex("""://m\.tiktok\.com/(?=@)"""), "://www.tiktok.com/").replace("/photo/", "/video/") // yt-dlp'nin tanıdığı biçime çevir (m.tiktok.com/v/... olduğu gibi kalır, kendisi yönlendirir)
        val platform = Platform.bul(url) // hangi site
        val uri = runCatching { URI(url) }.getOrNull() ?: return CozulmusLink(url, platform, LinkTuru.TEK) // çözülemezse tek say
        val parcalar = (uri.path ?: "").split('/').filter { it.isNotBlank() } // adres yolu parçaları
        val sorgu = uri.rawQuery ?: "" // ?list=... gibi kısım
        return when (platform) {
            Platform.YOUTUBE -> youtube(url, parcalar, sorgu) // YouTube
            Platform.TIKTOK -> tiktok(url, uri.host ?: "", parcalar) // TikTok
            Platform.INSTAGRAM -> instagram(url, parcalar) // Instagram
            Platform.X -> x(url, parcalar) // X
            Platform.PINTEREST -> pinterest(url, uri.host ?: "", parcalar) // Pinterest
            Platform.DIGER -> CozulmusLink(url, platform, LinkTuru.TEK) // diğer siteler: tek içerik
        }
    }

    private fun youtube(url: String, p: List<String>, sorgu: String): CozulmusLink {
        if (Regex("""(^|&)list=""").containsMatchIn(sorgu) || p.firstOrNull() == "playlist") // oynatma listesi
            return CozulmusLink(url, Platform.YOUTUBE, LinkTuru.LISTE)
        val ilk = p.firstOrNull() ?: return CozulmusLink(url, Platform.YOUTUBE, LinkTuru.TEK) // ana sayfa vb.
        val kanalKoku = when { // kanal adresinin kök kısmı
            ilk.startsWith("@") -> "https://www.youtube.com/$ilk" // /@isim
            ilk in setOf("channel", "c", "user") && p.size >= 2 -> "https://www.youtube.com/$ilk/${p[1]}" // /channel/UC.., /c/isim, /user/isim
            else -> null // değil
        } ?: return CozulmusLink(url, Platform.YOUTUBE, LinkTuru.TEK) // watch, shorts/ID, youtu.be/ID → tek
        val kullanici = if (ilk.startsWith("@")) ilk.removePrefix("@") else p[1] // görünen kullanıcı adı (/channel/ değil)
        val sekmeIndeks = if (ilk.startsWith("@")) 1 else 2 // sekme hangi parçada
        val sekmeler = if (p.getOrNull(sekmeIndeks) == "shorts") listOf(Sekme.SHORTS, Sekme.VIDEOLAR) else listOf(Sekme.VIDEOLAR, Sekme.SHORTS) // linkteki sekme önce
        return CozulmusLink(kanalKoku, Platform.YOUTUBE, LinkTuru.PROFIL, kullanici, sekmeler) // kanal
    }

    private fun tiktok(url: String, host: String, p: List<String>): CozulmusLink {
        if (host.startsWith("vm.") || host.startsWith("vt.")) return CozulmusLink(url, Platform.TIKTOK, LinkTuru.TEK) // kısa link → tek video
        val ilk = p.firstOrNull() ?: return CozulmusLink(url, Platform.TIKTOK, LinkTuru.TEK) // ana sayfa
        if (ilk.startsWith("@") && p.size == 1) // sadece /@kullanici → profil
            return CozulmusLink("https://www.tiktok.com/$ilk", Platform.TIKTOK, LinkTuru.PROFIL, ilk.removePrefix("@"))
        return CozulmusLink(url, Platform.TIKTOK, LinkTuru.TEK) // /@kullanici/video/ID vb.
    }

    private fun instagram(url: String, parcalar: List<String>): CozulmusLink {
        val p = if (parcalar.firstOrNull() == "_u") parcalar.drop(1) else parcalar // instagram.com/_u/kullanici biçimi
        val ilk = p.firstOrNull() ?: return CozulmusLink(url, Platform.INSTAGRAM, LinkTuru.TEK) // ana sayfa
        if (ilk in igAyrilmis) return CozulmusLink(url, Platform.INSTAGRAM, LinkTuru.TEK) // /p/, /reel/ ... → tek gönderi
        val iki = p.getOrNull(1) // ikinci parça
        if (iki in setOf("p", "reel", "tv") || (iki == "reels" && p.size >= 3)) return CozulmusLink(url, Platform.INSTAGRAM, LinkTuru.TEK) // /kullanici/reel/ID → yine tek gönderi
        val sekmeler = if (iki == "posts") listOf(Sekme.GONDERILER, Sekme.REELS) else listOf(Sekme.REELS, Sekme.GONDERILER) // linkteki bölüm önce seçili gelsin
        // /kullanici, /kullanici/reels, /kullanici/tagged → profil
        return CozulmusLink("https://www.instagram.com/$ilk/", Platform.INSTAGRAM, LinkTuru.PROFIL, ilk, sekmeler)
    }

    private fun x(url: String, p: List<String>): CozulmusLink {
        val ilk = p.firstOrNull() ?: return CozulmusLink(url, Platform.X, LinkTuru.TEK) // ana sayfa
        if (ilk.lowercase() in xAyrilmis || p.getOrNull(1) == "status") return CozulmusLink(url, Platform.X, LinkTuru.TEK) // gönderi linki → tek
        return CozulmusLink("https://x.com/$ilk", Platform.X, LinkTuru.PROFIL, ilk) // /kullanici, /kullanici/media → profil
    }

    private fun pinterest(url: String, host: String, p: List<String>): CozulmusLink {
        if (host == "pin.it") return CozulmusLink(url, Platform.PINTEREST, LinkTuru.TEK) // kısa link (indirme sayfası önce asıl adresi çözer)
        val ilk = p.firstOrNull() ?: return CozulmusLink(url, Platform.PINTEREST, LinkTuru.TEK) // ana sayfa
        if (ilk in pinAyrilmis) return CozulmusLink(url, Platform.PINTEREST, LinkTuru.TEK) // /pin/ID → tek pin
        val ikinci = p.getOrNull(1) // ikinci parça
        if (ikinci == null || ikinci.startsWith("_") || ikinci == "pins") // /kullanici/, /_created/, /_saved/, /pins/ → profil (linkteki bölüm korunur)
            return CozulmusLink("https://www.pinterest.com/$ilk/" + (ikinci?.let { "$it/" } ?: ""), Platform.PINTEREST, LinkTuru.PROFIL, ilk)
        val ucuncu = p.getOrNull(2)?.takeUnless { it.startsWith("_") || it in setOf("more_ideas", "organize", "organise") } // pano bölümü (section) varsa
        return CozulmusLink("https://www.pinterest.com/$ilk/$ikinci/" + (ucuncu?.let { "$it/" } ?: ""), Platform.PINTEREST, LinkTuru.LISTE) // /kullanici/pano/[bölüm]/ → pano (seçilebilir liste)
    }
}
