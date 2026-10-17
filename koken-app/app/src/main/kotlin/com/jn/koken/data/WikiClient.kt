package com.jn.koken.data

import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
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
class WikiClient(
    private val http: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .build(),
) {
    suspend fun fetch(host: String, title: String): Fetch = withContext(Dispatchers.IO) {
        val url: HttpUrl = HttpUrl.Builder()
            .scheme("https").host(host)
            .addPathSegments("w/index.php")
            .addQueryParameter("title", title)
            .addQueryParameter("action", "render")
            .build()
        val req = Request.Builder().url(url)
            // Vikimedya, uygulamaların kendini tanıtmasını ister
            .header("User-Agent", "Koken/1.0 (Android; https://github.com/jannet5/jn)")
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
        /** Tarayıcıda açılacak okunur adres */
        fun pageUrl(host: String, title: String): String =
            "https://$host/wiki/" + java.net.URLEncoder.encode(title.replace(' ', '_'), "UTF-8").replace("+", "%20")
    }
}
