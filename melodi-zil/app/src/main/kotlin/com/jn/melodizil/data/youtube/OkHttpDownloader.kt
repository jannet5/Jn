package com.jn.melodizil.data.youtube // YouTube veri katmanı

import okhttp3.OkHttpClient // HTTP istemcisi
import okhttp3.RequestBody.Companion.toRequestBody // Gövde dönüştürme
import org.schabi.newpipe.extractor.downloader.Downloader // NewPipe indirici soyutlaması
import org.schabi.newpipe.extractor.downloader.Request // NewPipe isteği
import org.schabi.newpipe.extractor.downloader.Response // NewPipe yanıtı
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException // Bot doğrulaması hatası
import java.util.concurrent.TimeUnit // Zaman aşımı birimi

/** NewPipeExtractor'ın HTTP isteklerini OkHttp ile yerine getiren köprü. */
class OkHttpDownloader(private val client: OkHttpClient = defaultClient()) : Downloader() {

    override fun execute(request: Request): Response {
        val builder = okhttp3.Request.Builder().url(request.url()) // OkHttp isteği
        val body = request.dataToSend()?.toRequestBody() // Gönderilecek veri (POST için)
        builder.method(request.httpMethod(), body) // HTTP yöntemi
        builder.header("User-Agent", USER_AGENT) // Tarayıcı kimliği
        for ((name, values) in request.headers()) { // Ekstraktörün istediği başlıklar
            builder.removeHeader(name) // Varsayılan silinir
            for (v in values) builder.addHeader(name, v) // Her değer eklenir
        }
        client.newCall(builder.build()).execute().use { resp -> // İstek yürütülüyor, yanıt otomatik kapanır
            if (resp.code == 429) throw ReCaptchaException("YouTube bot doğrulaması istedi", request.url()) // Çok fazla istek
            val text = resp.body?.string() ?: "" // Yanıt gövdesi
            return Response(resp.code, resp.message, resp.headers.toMultimap(), text, resp.request.url.toString()) // NewPipe yanıtı
        }
    }

    companion object {
        /** Masaüstü Firefox kimliği: NewPipe'ın da kullandığı, YouTube'un web istemcisiyle uyumlu dize. */
        const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0"

        fun defaultClient(): OkHttpClient = OkHttpClient.Builder() // Varsayılan istemci
            .connectTimeout(20, TimeUnit.SECONDS) // Bağlanma zaman aşımı
            .readTimeout(30, TimeUnit.SECONDS) // Okuma zaman aşımı
            .build() // Oluştur
    }
}
