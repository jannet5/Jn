package com.jn.yazikart.search // görsel arama paketi

import kotlinx.coroutines.Dispatchers // arka plan iş parçacıkları
import kotlinx.coroutines.async // iki kaynağı aynı anda sorgulamak için
import kotlinx.coroutines.coroutineScope // eşzamanlı işler için kapsam
import kotlinx.coroutines.withContext // işi arka plana almak için
import okhttp3.HttpUrl.Companion.toHttpUrl // adres oluşturucu
import okhttp3.OkHttpClient // internet istemcisi
import okhttp3.Request // internet isteği
import org.json.JSONObject // gelen cevabı çözmek için
import java.util.concurrent.TimeUnit // zaman aşımı birimi

// Arama sonucundaki tek bir görsel
data class ImageResult(
    val thumbUrl: String, // listede gösterilecek küçük resim
    val fullUrl: String, // seçilince indirilecek büyük resim
    val title: String, // görselin adı
    val source: String, // nereden geldiği (Openverse / Wikimedia)
)

// İnternetten görsel arar.
// Not: Google Görseller'in ücretsiz ve anahtarsız resmi bir arayüzü yok (Google'ın arama API'si
// anahtar ister ve yeni kullanıcılara kapatıldı). Bu yüzden anahtar gerektirmeyen, yasal olarak
// kullanılabilen iki büyük görsel arşivinden aynı anda aranıyor:
//  - Openverse: Flickr, Wikimedia ve daha birçok siteden 800 milyondan fazla görsel
//  - Wikimedia Commons: Vikipedi'nin görsel arşivi
class ImageSearch(
    private val client: OkHttpClient = defaultClient(), // internet istemcisi
) {
    companion object {
        const val USER_AGENT = "YaziKart/1.0 (Android; https://github.com/jannet5/Jn)" // Wikimedia kimlik ister

        // Zaman aşımları ve kimlik başlığı ayarlı istemci
        fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // bağlanma için en çok 10 sn
            .readTimeout(15, TimeUnit.SECONDS) // cevap için en çok 15 sn
            .addInterceptor { chain -> // her isteğe kimlik başlığı ekleniyor
                chain.proceed(chain.request().newBuilder().header("User-Agent", USER_AGENT).build()) // başlık eklenmiş istek
            }
            .build() // istemci hazır

        // Openverse cevabını sonuç listesine çevirir
        fun parseOpenverse(json: String): List<ImageResult> {
            val results = JSONObject(json).optJSONArray("results") ?: return emptyList() // sonuç dizisi
            return (0 until results.length()).mapNotNull { i -> // her sonuç dolaşılıyor
                val o = results.getJSONObject(i) // tek sonuç
                val full = o.optString("url") // büyük resim adresi
                if (full.isBlank()) return@mapNotNull null // adres yoksa atlanıyor
                val thumb = o.optString("thumbnail").ifBlank { full } // küçük resim yoksa büyüğü kullanılıyor
                ImageResult(thumb, full, o.optString("title"), "Openverse") // sonuç oluşturuluyor
            }
        }

        // Wikimedia Commons cevabını sonuç listesine çevirir
        fun parseWikimedia(json: String): List<ImageResult> {
            val pages = JSONObject(json).optJSONObject("query")?.optJSONArray("pages") ?: return emptyList() // sayfa dizisi
            val list = (0 until pages.length()).map { pages.getJSONObject(it) } // sayfalar listeye alınıyor
            return list.sortedBy { it.optInt("index", Int.MAX_VALUE) } // arama sırasına diziliyor
                .mapNotNull { p -> // her sayfa dolaşılıyor
                    val info = p.optJSONArray("imageinfo")?.optJSONObject(0) ?: return@mapNotNull null // görsel bilgisi
                    val mime = info.optString("mime") // dosya türü
                    if (mime !in setOf("image/jpeg", "image/png", "image/webp")) return@mapNotNull null // sadece fotoğraf türleri
                    val original = info.optString("url") // orijinal dosya
                    val thumb = info.optString("thumburl").ifBlank { original } // küçük resim
                    val width = info.optInt("width") // orijinal genişlik
                    val full = if (width > 1600 && thumb.contains("/330px-")) { // çok büyükse
                        thumb.replace("/330px-", "/1280px-") // 1280 piksellik hali kullanılıyor (hızlı iner)
                    } else original // değilse orijinal
                    if (original.isBlank()) return@mapNotNull null // adres yoksa atlanıyor
                    val title = p.optString("title").removePrefix("File:").substringBeforeLast('.') // "File:Lahmacun.jpg" -> "Lahmacun"
                    ImageResult(thumb, full, title, "Wikimedia") // sonuç oluşturuluyor
                }
        }

        // İki listeyi sırayla karıştırır (bir ondan bir bundan) ki iki kaynak da üstte görünsün
        fun interleave(a: List<ImageResult>, b: List<ImageResult>): List<ImageResult> {
            val out = ArrayList<ImageResult>(a.size + b.size) // birleşik liste
            for (i in 0 until maxOf(a.size, b.size)) { // en uzun liste kadar dönülüyor
                if (i < a.size) out += a[i] // A'dan bir tane
                if (i < b.size) out += b[i] // B'den bir tane
            }
            return out.distinctBy { it.fullUrl } // aynı görsel iki kez çıkmasın
        }
    }

    // Verilen adrese istek atıp cevabı metin olarak döndürür
    private fun get(url: String): String {
        client.newCall(Request.Builder().url(url).build()).execute().use { resp -> // istek atılıyor
            if (!resp.isSuccessful) error("HTTP ${resp.code}") // başarısızsa hata
            return resp.body?.string() ?: "" // cevap metni
        }
    }

    // Openverse'de arar
    private fun searchOpenverse(query: String): List<ImageResult> {
        val url = "https://api.openverse.org/v1/images/".toHttpUrl().newBuilder() // adres kuruluyor
            .addQueryParameter("q", query) // aranan kelime
            .addQueryParameter("page_size", "30") // 30 sonuç
            .addQueryParameter("mature", "false") // uygunsuz içerik yok
            .build().toString() // adres metni
        return parseOpenverse(get(url)) // istek atılıp sonuçlar çözülüyor
    }

    // Wikimedia Commons'ta arar
    private fun searchWikimedia(query: String): List<ImageResult> {
        val url = "https://commons.wikimedia.org/w/api.php".toHttpUrl().newBuilder() // adres kuruluyor
            .addQueryParameter("action", "query") // sorgu
            .addQueryParameter("generator", "search") // arama ile sayfa üret
            .addQueryParameter("gsrsearch", "$query filetype:bitmap") // aranan kelime, sadece resim dosyaları
            .addQueryParameter("gsrnamespace", "6") // dosya alanı
            .addQueryParameter("gsrlimit", "30") // 30 sonuç
            .addQueryParameter("prop", "imageinfo") // görsel bilgisi
            .addQueryParameter("iiprop", "url|mime|size") // adres, tür, boyut
            .addQueryParameter("iiurlwidth", "330") // küçük resim genişliği
            .addQueryParameter("format", "json") // JSON cevap
            .addQueryParameter("formatversion", "2") // sade JSON biçimi
            .build().toString() // adres metni
        return parseWikimedia(get(url)) // istek atılıp sonuçlar çözülüyor
    }

    // İki kaynakta aynı anda arar; biri çökse bile diğerinin sonuçları gelir
    suspend fun search(query: String): List<ImageResult> = withContext(Dispatchers.IO) { // internet işi arka planda
        coroutineScope { // eşzamanlı işler kapsamı
            val ov = async { runCatching { searchOpenverse(query) } } // Openverse araması başlıyor
            val wm = async { runCatching { searchWikimedia(query) } } // Wikimedia araması başlıyor
            val a = ov.await() // Openverse sonucu bekleniyor
            val b = wm.await() // Wikimedia sonucu bekleniyor
            if (a.isFailure && b.isFailure) throw a.exceptionOrNull()!! // ikisi de başarısızsa hata veriliyor
            interleave(a.getOrDefault(emptyList()), b.getOrDefault(emptyList())) // sonuçlar karıştırılıyor
        }
    }
}
