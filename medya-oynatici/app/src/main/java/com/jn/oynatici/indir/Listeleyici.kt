package com.jn.oynatici.indir

import android.content.Context
import com.jn.oynatici.data.CozulmusLink
import com.jn.oynatici.data.Platform
import com.jn.oynatici.data.Sekme
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

// Bir listenin (oynatma listesi, pano, profil) adı ve içindeki içerikler
data class ListeSonucu(
    val ad: String, // liste / kanal / kullanıcı adı
    val ogeler: List<Oge>, // indirilecek içerikler
    val fotoSayisi: Int = 0, // video olmadığı için listeden çıkarılan fotoğraf gönderisi sayısı
)

// Oynatma listelerini ve profillerin son içeriklerini, indirmeden önce listeleyen yardımcı
object Listeleyici {

    // Seçim ekranı için oynatma listesi / panoyu getir (en fazla [sinir] öge)
    suspend fun listeGetir(ctx: Context, link: CozulmusLink, sinir: Int = 300): ListeSonucu =
        if (link.platform == Platform.PINTEREST) pinterestListe(ctx, link.url, sinir, null, link.url.trimEnd('/').substringAfterLast('/')) // pano: gallery-dl
        else ytdlpListe(ctx, link.url, sinir, null, link.platform, profil = false) // yt-dlp ile düz liste

    // Bir profilin / kanalın son [sayi] içeriğini getir
    suspend fun profilGetir(ctx: Context, isi: IndirmeIsi): ListeSonucu {
        val kullanici = isi.url.trimEnd('/').substringAfterLast('/').removePrefix("@") // kullanıcı adı (adresin son parçası)
        return when (isi.platform) {
            Platform.YOUTUBE -> { // kanalın Videolar ya da Shorts bölümü (en yeniden eskiye)
                val bolum = if (isi.sekme == Sekme.SHORTS) "shorts" else "videos" // bölüm
                ytdlpListe(ctx, isi.url.trimEnd('/') + "/" + bolum, isi.sayi, isi.id, isi.platform) // yt-dlp
            }
            Platform.TIKTOK -> ytdlpListe(ctx, isi.url, isi.sayi, isi.id, isi.platform) // TikTok profili (yt-dlp)
            Platform.PINTEREST -> pinterestListe(ctx, isi.url.trimEnd('/') + "/_created/", isi.sayi, isi.id, kullanici) // Pinterest: kullanıcının oluşturduğu pinler
            Platform.INSTAGRAM -> ytdlpListe( // Instagram: Reels ya da Gönderiler bölümü (uygulamanın yt-dlp eklentisiyle, girişsiz)
                ctx, "https://www.instagram.com/$kullanici/" + (if (isi.sekme == Sekme.GONDERILER) "posts/" else "reels/"), isi.sayi, isi.id, isi.platform,
            )
            Platform.X -> galeriListe(ctx, isi, "https://x.com/$kullanici/media", kullanici) // X: medya bölümü (giriş gerekir)
            Platform.DIGER -> ytdlpListe(ctx, isi.url, isi.sayi, isi.id, isi.platform) // diğer: yt-dlp ne bulursa
        }
    }

    // yt-dlp ile "düz liste" (indirmeden, sadece başlık ve linkler)
    private suspend fun ytdlpListe(ctx: Context, url: String, sinir: Int, islemId: String?, platform: Platform, profil: Boolean = true): ListeSonucu = withContext(Dispatchers.IO) {
        Motor.hazirla(ctx) // motor hazır olsun
        var sonHata: Exception? = null // son deneme hatası
        repeat(if (platform == Platform.TIKTOK) 3 else 1) { // TikTok bazen boş cevap veriyor: 3 kez dene
            try {
                val sonuc = ytdlpListeBirKez(ctx, url, sinir, islemId, platform, profil) // listele
                if (sonuc.ogeler.isNotEmpty() || sonuc.fotoSayisi > 0 || platform != Platform.TIKTOK) return@withContext sonuc // doluysa bitti
            } catch (e: YoutubeDL.CanceledException) {
                throw e // iptal edildiyse tekrar deneme
            } catch (e: Exception) {
                sonHata = e // hatayı sakla, tekrar dene
            }
        }
        throw sonHata ?: IllegalStateException("İçerik bulunamadı") // olmadı
    }

    // yt-dlp ile tek bir listeleme denemesi
    private fun ytdlpListeBirKez(ctx: Context, url: String, sinir: Int, islemId: String?, platform: Platform, profil: Boolean): ListeSonucu {
        val istek = YoutubeDLRequest(url).apply {
            addOption("--flat-playlist") // içeriklere tek tek girmeden listele (hızlı)
            addOption("-J") // sonucu tek bir JSON olarak ver
            addOption("-I", "1:$sinir") // ilk N öge (en yeniler)
            addOption("--extractor-args", "youtube:lang=tr") // YouTube başlıkları Türkçe gelsin (varsa)
            Cerezler.dosyaYolu(ctx)?.let { addOption("--cookies", it) } // giriş yapıldıysa çerezler
            if (platform == Platform.TIKTOK) addOption("--user-agent", Platform.FIREFOX_UA) // TikTok engeline takılmamak için
            if (platform == Platform.INSTAGRAM) { // Instagram: eklenti ile girişsiz liste, sadece videolu gönderiler
                addOption("--plugin-dirs", Motor.eklentiKlasoru(ctx)) // eklenti klasörü
                addOption("--extractor-args", "instagramprofile:videos_only=true") // fotoğraf gönderilerini sayma
            }
        }
        val cevap = try {
            YoutubeDL.getInstance().execute(istek, islemId, null) // çalıştır
        } catch (e: com.yausername.youtubedl_android.YoutubeDLException) {
            throw e // hata çıktısı mesajda
        }
        return jsonCoz(JSONObject(cevap.out), platform, sinir, profil) // çöz
    }

    // yt-dlp JSON çıktısını ad + öge listesine çevir
    private fun jsonCoz(kok: JSONObject, platform: Platform, sinir: Int, profil: Boolean): ListeSonucu {
        val ogeler = mutableListOf<Oge>() // öge listesi
        var foto = 0 // atlanan fotoğraf gönderileri
        var ilkKanal = "" // ilk öğenin kanal adı (TikTok'ta asıl görünen ad burada)
        fun ekle(o: JSONObject) { // tek öge ekle
            if (ilkKanal.isEmpty()) ilkKanal = o.optString("channel").takeUnless { it == "null" } ?: "" // görünen adı sakla
            val kapak = o.optJSONArray("thumbnails")?.optJSONObject(0)?.optString("url") ?: "" // ilk kapak resmi
            if (platform == Platform.TIKTOK && kapak.contains("photomode")) { foto++; return } // TikTok fotoğraf gönderisi: video değil
            val baslik = o.optString("title") // başlık
            if (baslik == "[Private video]" || baslik == "[Deleted video]") return // gizli/silinmiş videolar indirilemez
            if (o.optString("availability") in setOf("subscriber_only", "premium_only", "needs_auth")) return // üyelere özel
            if (o.optString("live_status") in setOf("is_upcoming", "is_live")) return // henüz yayınlanmamış / canlı yayın
            val link = o.optString("webpage_url").takeIf { it.startsWith("http") } // önce asıl sayfa linki
                ?: o.optString("url").takeIf { it.startsWith("http") } // yoksa url alanı
                ?: return // link yoksa atla
            ogeler += Oge(link, baslik.takeUnless { it == "null" } ?: "", o.optDouble("duration").takeUnless { it.isNaN() }) // ekle
        }
        val girdiler: JSONArray? = kok.optJSONArray("entries") // liste öğeleri
        if (girdiler == null) ekle(kok) // tek içerikse kendisi
        else for (i in 0 until girdiler.length()) { // her öge
            val o = girdiler.optJSONObject(i) ?: continue // boş öğeyi atla
            val ic = o.optJSONArray("entries") // iç içe liste (bölümler) olabilir
            if (ic != null) for (j in 0 until ic.length()) ic.optJSONObject(j)?.let(::ekle) else ekle(o) // içini ya da kendisini ekle
            if (ogeler.size >= sinir) break // yeterince toplandı
        }
        // Ad: liste için listenin başlığı, profil için kanal/kullanıcı adı
        val alanlar = if (profil) listOf("channel", "uploader", "playlist_uploader") else listOf("title", "playlist_title", "channel", "uploader")
        val adaylar = (if (profil && platform == Platform.INSTAGRAM) listOf(kok.optString("uploader")) else emptyList()) + // Instagram: tam ad
            (if (profil) listOf(kok.optString("channel"), ilkKanal) else emptyList()) + alanlar.map { kok.optString(it) } +
            (if (profil) listOf(kok.optString("uploader_id"), kok.optString("title")) else emptyList()) // olası adlar (görünen ad önce)
        val ad = adaylar.firstOrNull { it.isNotBlank() && it != "null" } // ilk dolu alan
            ?.removeSuffix(" - Videos")?.removeSuffix(" - Shorts")?.removeSuffix(" - Live")?.removeSuffix(" - Videolar") // "Kanal - Videos" → "Kanal"
            ?: platform.ad // bulunamazsa site adı
        return ListeSonucu(ad, ogeler.take(sinir), foto) // sonuç
    }

    // gallery-dl ile profilin son içeriklerinin linklerini al (Instagram, X, Pinterest)
    private suspend fun galeriListe(ctx: Context, isi: IndirmeIsi, url: String, kullanici: String): ListeSonucu {
        val n = isi.sayi // istenen sayı
        val ortak = mutableListOf( // her sitede ortak seçenekler
            "-o", "ratelimit=abort", // sınır aşılırsa 15 dakika bekleme, hemen hata ver
            "--cache-file", java.io.File(ctx.cacheDir, "gdl-onbellek.sqlite3").absolutePath, // oturum önbelleği
        )
        Cerezler.dosyaYolu(ctx)?.let { ortak += listOf("--cookies", it) } // giriş yapıldıysa çerezler
        return when (isi.platform) {
            Platform.X -> {
                // Medya bölümündeki videolar (en yeniden eskiye). Bir gönderide birden çok video olabileceği için 2 katı dosya istenir.
                val cikti = Motor.galeriCalistir(ctx, ortak + listOf(
                    "--filter", "type in ('video','animated_gif')", // sadece videolar
                    "--range", "1-${n * 2}", // en fazla 2N dosya (sonra gönderiye göre tekilleştirilir)
                    "-N", "{tweet_id}\t{user[nick]}\thttps://x.com/{author[name]}/status/{tweet_id}", // her dosya için tek satır
                    url,
                ), isi.id)
                satirlardan(cikti, n, kullanici) // çöz
            }
            else -> throw UnsupportedOperationException("${isi.platform.ad} profili henüz desteklenmiyor") // diğerleri aşağıda
        }
    }

    // Pinterest pano/profil: gallery-dl ile sadece video pinleri listele (girişsiz çalışır)
    private suspend fun pinterestListe(ctx: Context, url: String, n: Int, islemId: String?, yedekAd: String): ListeSonucu {
        val arg = mutableListOf("-o", "ratelimit=abort") // sınır aşılırsa bekleme
        Cerezler.dosyaYolu(ctx)?.let { arg += listOf("--cookies", it) } // giriş yapıldıysa çerezler
        arg += listOf(
            "--filter", "extension in ('mp4', 'm3u8')", // sadece videolar
            "--range", "1-$n", // ilk N video (en yeniden eskiye)
            "-N", "{id}\t{pinner[full_name]}\t{board[name]}\t{title|grid_title}\t{url}", // her pin için tek satır
            url,
        )
        val cikti = Motor.galeriCalistir(ctx, arg, islemId) // çalıştır
        val ogeler = mutableListOf<Oge>() // pinler
        var kisi = "" // pinleyen kişinin adı
        var pano = "" // pano adı
        cikti.lineSequence().map { it.split('\t') }.filter { it.size >= 5 && it[4].startsWith("http") }.forEach { p -> // her geçerli satır
            if (kisi.isBlank() && p[1].isNotBlank() && p[1] != "None") kisi = p[1] // kişi
            if (pano.isBlank() && p[2].isNotBlank() && p[2] != "None") pano = p[2] // pano
            ogeler += Oge("https://www.pinterest.com/pin/${p[0]}/", p[3].takeUnless { it == "None" } ?: "", medya = p[4]) // pin sayfası + video dosyası
        }
        val panoMu = !url.contains("/_created") // pano mu profil mi
        return ListeSonucu((if (panoMu) pano else kisi).ifBlank { yedekAd }, ogeler.take(n)) // ad: pano adı ya da kişi adı
    }

    // gallery-dl'in "kimlik<TAB>ad<TAB>link" satırlarını çöz: aynı gönderiyi bir kez al, ilk N tanesini döndür
    private fun satirlardan(cikti: String, n: Int, yedekAd: String): ListeSonucu {
        val gorulen = LinkedHashMap<String, String>() // kimlik → link (sırayı korur)
        var ad = "" // görünen ad
        cikti.lineSequence().map { it.split('\t') }.filter { it.size >= 3 && it[2].startsWith("http") }.forEach { p -> // her geçerli satır
            if (ad.isBlank() && p[1].isNotBlank() && p[1] != "None") ad = p[1] // görünen ad
            if (gorulen.size < n) gorulen.putIfAbsent(p[0], p[2]) // gönderi başına bir link
        }
        return ListeSonucu(ad.ifBlank { yedekAd }, gorulen.values.map { Oge(it, "") }) // sonuç
    }

    // Teknik hatayı anlaşılır Türkçe metne çevir
    fun anlasilirHata(ham: String): String = when {
        ham.contains("login", true) || ham.contains("log in", true) || ham.contains("cookies", true) || ham.contains("authorization", true) ||
            ham.contains("authenticat", true) || ham.contains("rate-limit", true) -> "Bu site giriş istiyor. Hesaplar'dan giriş yapıp tekrar dene." // giriş gerekli
        ham.contains("Unsupported URL", true) || ham.contains("is not a valid URL", true) || ham.contains("No suitable extractor", true) -> "Bu link desteklenmiyor" // geçersiz link
        ham.contains("does not have a", true) && ham.contains("tab", true) -> "Bu kanalda o bölüm yok" // örn. Shorts yok
        ham.contains("not found", true) || ham.contains("404", true) || ham.contains("does not exist", true) -> "Bulunamadı (link ya da kullanıcı adı yanlış olabilir)" // yok
        ham.contains("Private", true) -> "Bu içerik/profil gizli" // gizli
        ham.contains("Unable to download", true) || ham.contains("Failed to resolve", true) || ham.contains("timed out", true) || ham.contains("Network", true) -> "İnternet bağlantısını kontrol et" // internet
        else -> ham.lineSequence().firstOrNull { it.contains("ERROR") || it.contains("error", true) }?.substringAfter("ERROR:")?.trim()?.take(160)
            ?: ham.lineSequence().lastOrNull { it.isNotBlank() }?.take(160) ?: "Liste alınamadı" // diğer
    }

    // Hata giriş yapılmadığı için mi
    fun girisHatasiMi(ham: String) = listOf("login", "log in", "cookies", "authorization", "authenticat", "rate-limit", "Sign in").any { ham.contains(it, true) }
}
