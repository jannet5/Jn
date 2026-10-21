package com.jn.oynatici.indir

import android.content.Context
import android.webkit.CookieManager
import com.jn.oynatici.data.Platform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URI

// Uygulama içinden (WebView ile) giriş yapılan hesapların çerezleri.
// İndirici (yt-dlp / gallery-dl) bu çerezlerle senin hesabınmış gibi istek atar; şifren hiçbir yere kaydedilmez.
object Cerezler {

    // Giriş yapılabilen siteler: giriş sayfası, çerezlerinin okunacağı adresler ve "giriş yapıldı" demek olan çerez
    // anahtarDeger: çerezin "giriş yapıldı" sayılması için alması gereken değer (null = boş olmaması yeter)
    data class Site(val platform: Platform, val girisAdresi: String, val adresler: List<String>, val anahtarCerez: String, val anahtarDeger: String? = null)

    val siteler = listOf(
        Site(Platform.INSTAGRAM, "https://www.instagram.com/accounts/login/", listOf("https://www.instagram.com"), "sessionid"), // Instagram
        Site(Platform.X, "https://x.com/i/flow/login", listOf("https://x.com", "https://twitter.com"), "auth_token"), // X
        Site(Platform.TIKTOK, "https://www.tiktok.com/login", listOf("https://www.tiktok.com"), "sessionid"), // TikTok
        Site(Platform.PINTEREST, "https://www.pinterest.com/login/", listOf("https://www.pinterest.com"), "_auth", "1"), // Pinterest (_pinterest_sess girişsiz de geliyor; _auth=1 sadece girişte)
    )

    // Netscape biçimindeki çerez dosyası (indiricilere --cookies ile verilir)
    fun dosya(ctx: Context) = File(ctx.filesDir, "cerezler.txt")

    // Çerez dosyası varsa yolu, yoksa null (sadece okumak için; indiricilere kopyası verilir)
    fun dosyaYolu(ctx: Context): String? = dosya(ctx).takeIf { it.isFile && it.length() > 40 }?.absolutePath

    // İndiricilere verilecek kopya: yt-dlp ve gallery-dl iş bitince çerezleri dosyaya geri yazıyor;
    // asıl dosyayı verirsek "Çıkış" yaptıktan sonra eski oturum geri gelebilirdi. Bu yüzden her seferinde ayrı kopya.
    fun kopyaYolu(ctx: Context): String? {
        val asil = dosya(ctx).takeIf { it.isFile && it.length() > 40 } ?: return null // çerez yoksa null
        val klasor = File(ctx.cacheDir, "cerez-kopya").apply { mkdirs() } // kopyalar klasörü
        klasor.listFiles()?.filter { System.currentTimeMillis() - it.lastModified() > 3 * 60 * 60 * 1000 }?.forEach { it.delete() } // 3 saatten eski kopyaları temizle
        val kopya = File(klasor, "cerez-${java.util.UUID.randomUUID()}.txt") // yeni kopya
        asil.copyTo(kopya, overwrite = true) // kopyala
        return kopya.absolutePath // yol
    }

    // Bu sitede giriş yapılmış mı (anahtar çerez var mı)
    fun girisVar(platform: Platform): Boolean {
        val site = siteler.find { it.platform == platform } ?: return false // giriş desteklenmeyen site
        val deger = anahtarDegeri(platform) ?: return false // anahtar çerez yok
        return if (site.anahtarDeger != null) deger == site.anahtarDeger else deger.isNotEmpty() // beklenen değer mi / dolu mu
    }

    // Anahtar çerezin şu anki değeri (yoksa null) — giriş sayfası, değer değişince "giriş oldu" anlar
    fun anahtarDegeri(platform: Platform): String? {
        val site = siteler.find { it.platform == platform } ?: return null // site
        return site.adresler.asSequence().flatMap { cerezler(it).asSequence() }.firstOrNull { it.first == site.anahtarCerez }?.second // ilk eşleşen
    }

    // Tüm sitelerin çerezlerini dosyaya yaz. Hiç çerez yoksa dosyayı siler ve null döner.
    suspend fun dosyayaYaz(ctx: Context): File? = withContext(Dispatchers.Main) { // CookieManager ana iş parçacığında güvenli
        val satirlar = StringBuilder("# Netscape HTTP Cookie File\n") // dosya başlığı
        var sayi = 0 // yazılan çerez sayısı
        val sonKullanma = System.currentTimeMillis() / 1000 + 365L * 24 * 3600 // bir yıl geçerli say
        siteler.flatMap { it.adresler }.forEach { adres -> // her adres
            val alan = "." + (URI(adres).host ?: return@forEach).removePrefix("www.") // ".instagram.com" gibi
            cerezler(adres).forEach { (ad, deger) -> // her çerez
                satirlar.append(alan).append("\tTRUE\t/\tTRUE\t").append(sonKullanma).append('\t').append(ad).append('\t').append(deger).append('\n') // tek satır
                sayi++ // say
            }
        }
        val f = dosya(ctx) // hedef dosya
        withContext(Dispatchers.IO) { if (sayi == 0) f.delete() else f.writeText(satirlar.toString()) } // yaz ya da sil
        if (sayi == 0) null else f // sonuç
    }

    // Bir sitedeki oturumu kapat (çerezlerini sil)
    suspend fun cikisYap(ctx: Context, platform: Platform) = withContext(Dispatchers.Main) {
        val site = siteler.find { it.platform == platform } ?: return@withContext // site
        val yonetici = CookieManager.getInstance() // çerez yöneticisi
        site.adresler.forEach { adres -> // her adres
            val host = URI(adres).host ?: return@forEach // alan adı
            cerezler(adres).forEach { (ad, _) -> // her çerezi
                listOf(host, "." + host.removePrefix("www."), host.removePrefix("www.")).forEach { alan -> // olası alan adlarında
                    yonetici.setCookie(adres, "$ad=; Max-Age=0; Path=/; Domain=$alan") // süresi geçmiş yap
                }
                yonetici.setCookie(adres, "$ad=; Max-Age=0; Path=/") // alan adsız hali
            }
        }
        yonetici.flush() // diske yaz
        dosyayaYaz(ctx) // dosyayı güncelle
    }

    // Bir adresin çerezlerini (ad, değer) listesi olarak al
    private fun cerezler(adres: String): List<Pair<String, String>> {
        val ham = runCatching { CookieManager.getInstance().getCookie(adres) }.getOrNull() ?: return emptyList() // "a=b; c=d"
        return ham.split(';').mapNotNull { parca -> // her parça
            val ad = parca.substringBefore('=').trim() // çerez adı
            if (ad.isEmpty()) null else ad to parca.substringAfter('=', "").trim() // (ad, değer)
        }
    }
}
