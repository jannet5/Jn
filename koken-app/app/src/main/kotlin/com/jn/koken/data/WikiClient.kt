package com.jn.koken.data

import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

/** Bir sayfa isteğinin sonucu */
sealed interface Fetch {
    data class Ok(val html: String, val pageUrl: String) : Fetch
    /** Sayfa yok (404) */
    data object Missing : Fetch
    /** Ağ ya da sunucu hatası; [code] HTTP kodu (ağ hatasında null) */
    data class Failed(val code: Int?) : Fetch
}

/** Vikimedya sayfalarını anahtarsız çeker: index.php?action=render = sadece sayfa gövdesi HTML'i */
class WikiClient(private val http: OkHttpClient = defaultHttp) {
    /** Ağ ya da sunucu hatasında (404 hariç) kısa bir bekleyişten sonra bir kez daha dener */
    suspend fun fetch(host: String, title: String): Fetch {
        val first = fetchOnce(host, title)
        if (first !is Fetch.Failed) return first
        delay(700)
        return fetchOnce(host, title)
    }

    private suspend fun fetchOnce(host: String, title: String): Fetch = withContext(Dispatchers.IO) {
        val url: HttpUrl = HttpUrl.Builder()
            .scheme("https").host(host)
            .addPathSegments("w/index.php")
            .addQueryParameter("title", title)
            .addQueryParameter("action", "render")
            .build()
        val req = Request.Builder().url(url)
            // Vikimedya, uygulamaların kendini tanıtmasını ister
            .header("User-Agent", USER_AGENT)
            .header("Accept-Language", "tr,en;q=0.8")
            .build()
        try {
            http.newCall(req).execute().use { resp ->
                when {
                    resp.code == 404 -> Fetch.Missing
                    !resp.isSuccessful -> Fetch.Failed(resp.code)
                    else -> Fetch.Ok(resp.body?.string().orEmpty(), pageUrl(host, title))
                }
            }
        } catch (e: IOException) {
            Fetch.Failed(null)
        }
    }

    companion object {
        const val USER_AGENT = "Koken/1.0 (Android; https://github.com/jannet5/jn)"

        /** Tüm ağ istekleri aynı istemciyi paylaşır (bağlantı havuzu) */
        val defaultHttp: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .callTimeout(40, TimeUnit.SECONDS)
                .build()
        }

        /** Tarayıcıda açılacak okunur adres */
        fun pageUrl(host: String, title: String): String =
            "https://$host/wiki/" + java.net.URLEncoder.encode(title.replace(' ', '_'), "UTF-8").replace("+", "%20")
    }
}
