package com.jn.koken.data

import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

/**
 * İngilizce kısa anlam metinlerini Türkçeye çevirir. Hesap/anahtar gerektirmeyen iki herkese açık
 * uç nokta sırayla denenir; ikisi de olmazsa metin İngilizce kalır (uygulama bunu işaretler).
 * Sadece kısa anlam metinleri gönderilir (örn. "distinguished, selected").
 */
class Translator(private val http: OkHttpClient = WikiClient.defaultHttp) {
    private val cache = ConcurrentHashMap<String, String>()

    /** @return çevrilebilen metinler: İngilizce → Türkçe */
    suspend fun translate(texts: List<String>): Map<String, String> = coroutineScope {
        texts.map { it.trim() }.filter { it.isNotEmpty() }.distinct()
            .map { t -> async { t to one(t) } }
            .awaitAll()
            .mapNotNull { (k, v) -> v?.let { k to it } }
            .toMap()
    }

    private suspend fun one(text: String): String? {
        cache[text]?.let { return it }
        val out = google(text) ?: myMemory(text)
        if (out != null) cache[text] = out
        return out
    }

    private suspend fun google(text: String): String? = withContext(Dispatchers.IO) {
        runCatching {
            val url = HttpUrl.Builder().scheme("https").host("translate.googleapis.com")
                .addPathSegments("translate_a/single")
                .addQueryParameter("client", "gtx").addQueryParameter("sl", "en")
                .addQueryParameter("tl", "tr").addQueryParameter("dt", "t")
                .addQueryParameter("q", text).build()
            http.newCall(Request.Builder().url(url).header("User-Agent", WikiClient.USER_AGENT).build()).execute().use { r ->
                if (!r.isSuccessful) return@use null
                val parts: JSONArray = JSONArray(r.body?.string().orEmpty()).getJSONArray(0)
                buildString { for (i in 0 until parts.length()) append(parts.getJSONArray(i).optString(0)) }
                    .trim().ifEmpty { null }
            }
        }.getOrNull()
    }

    private suspend fun myMemory(text: String): String? = withContext(Dispatchers.IO) {
        runCatching {
            val url = HttpUrl.Builder().scheme("https").host("api.mymemory.translated.net").addPathSegments("get")
                .addQueryParameter("q", text).addQueryParameter("langpair", "en|tr").build()
            http.newCall(Request.Builder().url(url).header("User-Agent", WikiClient.USER_AGENT).build()).execute().use { r ->
                if (!r.isSuccessful) return@use null
                val o = JSONObject(r.body?.string().orEmpty())
                val t = o.getJSONObject("responseData").optString("translatedText").trim()
                // Kota dolunca servis uyarı metni döndürür; bunu çeviri sayma
                if (t.isEmpty() || t.contains("MYMEMORY WARNING", ignoreCase = true)) null else t
            }
        }.getOrNull()
    }
}
