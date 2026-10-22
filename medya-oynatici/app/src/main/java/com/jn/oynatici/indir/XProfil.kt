package com.jn.oynatici.indir

import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

// X'ten girişsiz video bulan yardımcılar.
// X, girişsiz ziyaretçiye çoğu kişisel hesabın en yeni gönderilerini değil popüler gönderilerini gösteriyor ve
// profil akışlarını (Medya, Yanıtlar, Arama) hesapsız hiç açmıyor; hesapsız açık olan tek şey tek gönderi okumak.
// Bu işi yapan açık kaynak projeler (FxEmbed/FxTwitter, BetterTwitFix/vxtwitter, Nitter) kendi sunucularındaki X hesaplarıyla bakıyor.
// Kullanıcı hiç giriş yapmasın diye bu ücretsiz servislerin birkaçı sırayla denenir; biri kapanırsa diğeri devreye girer:
// 1) FxTwitter: profilin "Medya" sekmesi, en yeniden eskiye, sayfa sayfa.
// 2) X'in kendi misafir erişimi (gallery-dl, "Gönderiler") — Listeleyici'de; kurumsal hesaplarda en yeniden eskiye gelir.
// 3) vxtwitter: profilin son ~20 gönderisi (misafir erişim popülerleri verdiyse ya da açılmadıysa).
// 4) X'in yerleştirme (embed) akışı.
// Tek gönderide yt-dlp açamazsa FxTwitter / vxtwitter'dan videonun doğrudan MP4 adresi alınır.
object XProfil {

    private const val FX_KOK = "https://api.fxtwitter.com/2/" // FxTwitter API kökü
    private const val FX = FX_KOK + "profile/" // FxTwitter profil adresi
    private const val VX = "https://api.vxtwitter.com/" // vxtwitter API kökü (tarayıcı kimliğiyle gelinirse Cloudflare engeli çıkıyor)
    private const val FX_KIMLIK = "OynaticiApp/1.0" // FxTwitter tarayıcı kimliği istemiyor, uygulama adı yeterli
    private const val TARAYICI = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0" // X sayfaları için

    // FxTwitter ile profilin Medya sekmesindeki son [n] videolu gönderi. Servise ulaşılamazsa / kullanıcı bulunamazsa null.
    suspend fun fxListe(kullanici: String, n: Int, iptalMi: () -> Boolean): ListeSonucu? = withContext(Dispatchers.IO) {
        // Kullanıcı adıyla arama arada bir 404 veriyor (servisin bilinen huyu): birkaç kez dene, sonra sayısal kimlikle devam et
        val kisi = json(FX + URLEncoder.encode(kullanici, "UTF-8"), FX_KIMLIK)?.optJSONObject("user") ?: return@withContext null // profil
        val kimlik = kisi.optString("id").takeIf { it.isNotBlank() } ?: return@withContext null // sayısal kimlik
        if (kisi.optBoolean("protected")) return@withContext null // gizli hesap: girişsiz görünmez (aşağıdaki yol anlaşılır hata verir)
        val ekranAdi = kisi.optString("screen_name").ifBlank { kullanici } // @kullanici (büyük/küçük harf doğru haliyle)
        val bulunan = LinkedHashMap<String, Oge>() // gönderi kimliği → öge (sırayı korur)
        var imlec: String? = null // sonraki sayfanın imleci
        var bosSayfa = 0 // art arda boş gelen sayfa sayısı
        for (sayfa in 1..40) { // en fazla 40 sayfa (~2000 medya gönderisi)
            if (iptalMi()) throw YoutubeDL.CanceledException() // kullanıcı iptal etti
            val adres = FX + "id:$kimlik/media?count=100" + (imlec?.let { "&cursor=" + URLEncoder.encode(it, "UTF-8") } ?: "") // sayfa adresi
            val cevap = json(adres, FX_KIMLIK) ?: break // sayfa gelmediyse o ana kadar bulunanlarla yetin
            val sonuclar = cevap.optJSONArray("results") // gönderiler (en yeniden eskiye)
            for (i in 0 until (sonuclar?.length() ?: 0)) {
                val g = sonuclar!!.optJSONObject(i) ?: continue // gönderi
                if (g.optJSONObject("author")?.optString("id") != kimlik) continue // başkasının gönderisi (yeniden paylaşım) değil
                val videolar = g.optJSONObject("media")?.optJSONArray("videos") ?: continue // videosu yoksa atla
                val videoVar = (0 until videolar.length()).any { videolar.optJSONObject(it)?.optString("type") in setOf("video", "gif") } // video ya da GIF
                val id = g.optString("id").takeIf { it.all(Char::isDigit) && it.isNotEmpty() } ?: continue // gönderi kimliği
                if (videoVar) bulunan.putIfAbsent(id, Oge("https://x.com/$ekranAdi/status/$id", "")) // gönderi linki
                if (bulunan.size >= n) break // yeterli
            }
            if (bulunan.size >= n) break // yeterli
            bosSayfa = if ((sonuclar?.length() ?: 0) == 0) bosSayfa + 1 else 0 // boş sayfa sayacı
            val sonraki = cevap.optJSONObject("cursor")?.optString("bottom")?.takeIf { it.isNotBlank() && it != "null" } // sonraki sayfa
            if (sonraki == null || sonraki == imlec || bosSayfa >= 2) break // sayfalar bitti
            imlec = sonraki // devam
        }
        ListeSonucu(kisi.optString("name").ifBlank { ekranAdi }, bulunan.values.toList()) // sonuç
    }

    // vxtwitter ile profilin son ~20 gönderisinden videolu olanlar (en yeniden eskiye, video adresleriyle). Olmazsa null.
    suspend fun vxListe(kullanici: String, n: Int): ListeSonucu? = withContext(Dispatchers.IO) {
        val kok = json(VX + URLEncoder.encode(kullanici, "UTF-8") + "?with_tweets=true", FX_KIMLIK) ?: return@withContext null // profil + son gönderiler
        if (kok.optBoolean("protected")) return@withContext null // gizli hesap
        val gonderiler = kok.optJSONArray("latest_tweets") ?: return@withContext null // son gönderiler
        val ekranAdi = kok.optString("screen_name").ifBlank { kullanici } // @kullanici
        val sira = mutableListOf<Long>() // geldiği sıra (kontrol için)
        val bulunan = mutableListOf<Pair<Long, Oge>>() // (kimlik, öge)
        for (i in 0 until gonderiler.length()) {
            val t = gonderiler.optJSONObject(i) ?: continue // gönderi
            if (!t.optString("user_screen_name").equals(ekranAdi, ignoreCase = true) || !t.isNull("retweetURL")) continue // kendi gönderisi, yeniden paylaşım değil
            val id = t.optString("tweetID").toLongOrNull() ?: continue // kimlik
            sira += id // sırayı kaydet
            val medya = t.optJSONArray("media_extended") ?: continue // medya
            if ((0 until medya.length()).none { medya.optJSONObject(it)?.optString("type") in setOf("video", "gif") }) continue // video yok
            bulunan += id to Oge("https://x.com/$ekranAdi/status/$id", "") // link
        }
        if (siraBozuk(sira)) return@withContext null // en yeniden eskiye değilse işe yaramaz
        ListeSonucu(kok.optString("name").ifBlank { ekranAdi }, bulunan.sortedByDescending { it.first }.map { it.second }.take(n)) // sonuç
    }

    // Bir gönderinin videolarının doğrudan MP4 adresleri (kaliteye en yakın) ve gönderenin adı. Bulunamazsa boş liste.
    data class Videolar(val ad: String, val adresler: List<String>)
    suspend fun dogrudanVideolar(gonderiNo: String, kalite: Int?): Videolar = withContext(Dispatchers.IO) {
        json(FX_KOK + "status/" + gonderiNo, FX_KIMLIK)?.optJSONObject("status")?.let { s -> // önce FxTwitter (kalite basamaklarıyla)
            val videolar = s.optJSONObject("media")?.optJSONArray("videos") // videolar
            val adresler = (0 until (videolar?.length() ?: 0)).mapNotNull { i ->
                videolar!!.optJSONObject(i)?.takeIf { it.optString("type") in setOf("video", "gif") }?.let { mp4Sec(it, kalite) } // en uygun MP4
            }
            if (adresler.isNotEmpty()) return@withContext Videolar(s.optJSONObject("author")?.optString("name").orEmpty(), adresler)
        }
        json(VX + "i/status/" + gonderiNo, FX_KIMLIK)?.let { v -> // sonra vxtwitter (sadece en yüksek kalite)
            val medya = v.optJSONArray("media_extended") // medya
            val adresler = (0 until (medya?.length() ?: 0)).mapNotNull { i ->
                medya!!.optJSONObject(i)?.takeIf { it.optString("type") in setOf("video", "gif") }?.optString("url")?.takeIf { it.startsWith("https://") } // video adresi
            }
            if (adresler.isNotEmpty()) return@withContext Videolar(v.optString("user_name"), adresler)
        }
        Videolar("", emptyList()) // bulunamadı
    }

    // FxTwitter video bilgisinden seçilen kaliteye (kısa kenar, örn. 720) en yakın MP4: altındaki en iyisi, yoksa en küçüğü
    private fun mp4Sec(video: JSONObject, kalite: Int?): String? {
        val formatlar = video.optJSONArray("formats") // kalite basamakları
        val mp4ler = (0 until (formatlar?.length() ?: 0)).mapNotNull { formatlar!!.optJSONObject(it) }
            .filter { it.optString("container") == "mp4" && it.optString("url").startsWith("https://") } // sadece MP4
            .map { f ->
                val boyut = Regex("""/(\d+)x(\d+)/""").find(f.optString("url"))?.groupValues // "1280x720" adresin içinde
                Triple(f.optString("url"), boyut?.let { minOf(it[1].toInt(), it[2].toInt()) } ?: 0, f.optString("bitrate").toLongOrNull() ?: 0L) // (adres, kısa kenar, bit hızı)
            }
        if (mp4ler.isEmpty()) return video.optString("url").takeIf { it.startsWith("https://") } // basamak yoksa verilen adres
        val uygun = if (kalite == null) mp4ler else mp4ler.filter { it.second <= kalite } // kalite sınırı
        return (uygun.maxWithOrNull(compareBy({ it.second }, { it.third })) ?: mp4ler.minByOrNull { it.second })?.first // en iyisi
    }

    // X'in yerleştirme (embed) akışı: profilin son ~20 gönderisinden videolu olanlar (en yeniden eskiye). Olmazsa null.
    suspend fun yerlestirmeListe(kullanici: String, n: Int): ListeSonucu? = withContext(Dispatchers.IO) {
        val sayfa = metin("https://syndication.twitter.com/srv/timeline-profile/screen-name/" + URLEncoder.encode(kullanici, "UTF-8"), TARAYICI, deneme = 1) ?: return@withContext null // sayfa (sınır 15 dakikada 30 istek: tekrar denemenin anlamı yok)
        val veri = Regex("""<script id="__NEXT_DATA__" type="application/json">(.*?)</script>""", RegexOption.DOT_MATCHES_ALL).find(sayfa)?.groupValues?.get(1) ?: return@withContext null // gömülü JSON
        val girdiler = runCatching { JSONObject(veri).getJSONObject("props").getJSONObject("pageProps").getJSONObject("timeline").getJSONArray("entries") }.getOrNull() ?: return@withContext null // akış
        var ad = "" // görünen ad
        val bulunan = mutableListOf<Pair<Long, Oge>>() // (kimlik, öge)
        val sira = mutableListOf<Long>() // profil sahibinin tüm gönderileri, geldiği sırayla (sıra kontrolü için)
        for (i in 0 until girdiler.length()) {
            val t = girdiler.optJSONObject(i)?.optJSONObject("content")?.optJSONObject("tweet") ?: continue // gönderi
            if (t.has("retweeted_status")) continue // yeniden paylaşım değil
            val yazar = t.optJSONObject("user") // gönderen
            if (!yazar?.optString("screen_name").equals(kullanici, ignoreCase = true)) continue // profilin sahibi
            t.optString("id_str").toLongOrNull()?.let { sira += it } // sırayı kaydet
            val medya = t.optJSONObject("extended_entities")?.optJSONArray("media") ?: continue // medyası yoksa atla
            if ((0 until medya.length()).none { medya.optJSONObject(it)?.optString("type") in setOf("video", "animated_gif") }) continue // video yok
            val id = t.optString("id_str").toLongOrNull() ?: continue // kimlik
            if (ad.isEmpty()) ad = yazar?.optString("name").orEmpty() // ad
            bulunan += id to Oge("https://x.com/${yazar?.optString("screen_name") ?: kullanici}/status/$id", "") // link
        }
        // Bazı hesaplarda bu akış da girişsiz ziyaretçiye en yenileri değil popüler gönderileri (karışık sırayla) veriyor: o zaman işe yaramaz
        if (siraBozuk(sira)) return@withContext null
        ListeSonucu(ad.ifBlank { kullanici }, bulunan.sortedByDescending { it.first }.map { it.second }.distinctBy { it.url }.take(n)) // en yeniden eskiye
    }

    // gallery-dl'in misafir listesinde sıra bozuk mu (X en yenileri değil "popüler" gönderileri verdiyse sıra karışık gelir).
    // Gönderi kimliği zamanı içerir; bir gönderiden sonra 1 günden daha YENİ bir gönderi geliyorsa sıra bozuktur
    // (aynı konudaki zincir gönderileri dakikalar içinde ters sırada gelebiliyor, onları sayma).
    fun siraBozukMu(linkler: List<String>): Boolean = siraBozuk(linkler.mapNotNull { it.substringAfterLast('/').toLongOrNull() })

    // Gönderi kimlikleri en yeniden eskiye mi (bkz. siraBozukMu)
    private fun siraBozuk(kimlikler: List<Long>): Boolean {
        val zamanlar = kimlikler.map { it shr 22 } // milisaniye (X'in başlangıcından beri)
        if (zamanlar.size < 2) return false // karşılaştıracak yok
        val bozuk = zamanlar.zipWithNext().count { (once, sonra) -> sonra - once > 24L * 60 * 60 * 1000 } // geriye değil ileriye giden çiftler
        return bozuk * 4 > zamanlar.size - 1 // çiftlerin dörtte birinden fazlası
    }

    // Sırasını düzelt: en yeniden eskiye
    fun yenidenEskiye(ogeler: List<Oge>) = ogeler.sortedByDescending { it.url.substringAfterLast('/').toLongOrNull() ?: 0L }

    // JSON cevabı (200 değilse birkaç kez dener, olmazsa null)
    private suspend fun json(adres: String, kimlik: String): JSONObject? = metin(adres, kimlik)?.let { runCatching { JSONObject(it) }.getOrNull() }

    // Sayfanın metni (404/429/5xx'te [deneme] kez dener, olmazsa null)
    private suspend fun metin(adres: String, kimlik: String, deneme: Int = 4): String? {
        repeat(deneme) { kacinci ->
            val sonuc = runCatching {
                val b = (URL(adres).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 15000; readTimeout = 30000 // zaman aşımı
                    setRequestProperty("User-Agent", kimlik) // kimlik
                    setRequestProperty("Accept", "application/json, text/html") // ne beklediğimiz
                }
                try {
                    val kod = b.responseCode // cevap kodu
                    if (kod == 200) b.inputStream.bufferedReader().use { it.readText() } else if (kod == 404 || kod == 429 || kod >= 500) null else return null // tekrar denenebilir / denenemez
                } finally {
                    b.disconnect() // kapat
                }
            }.getOrNull() // bağlantı hatası: tekrar dene
            if (sonuc != null) return sonuc // geldi
            if (kacinci < deneme - 1) delay(1500L * (kacinci + 1)) // biraz bekle
        }
        return null // olmadı
    }
}
