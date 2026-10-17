package com.jn.yazikart.video // video paketi

import kotlinx.coroutines.Dispatchers // arka plan iş parçacığı
import kotlinx.coroutines.ensureActive // iptal edildiyse durmak için
import kotlinx.coroutines.withContext // işi arka plana almak için
import okhttp3.OkHttpClient // internet istemcisi
import okhttp3.Request // internet isteği
import java.io.File // hedef dosya
import java.util.concurrent.TimeUnit // zaman aşımı birimi

// İnternetteki video dosyasını doğrudan telefona indirir (arada başka sunucu yok)
object VideoDownloader {
    const val MAX_BYTES = 300L * 1024 * 1024 // en fazla 300 MB (telefon dolmasın)

    private val client = OkHttpClient.Builder() // istemci
        .connectTimeout(20, TimeUnit.SECONDS) // bağlanma için en çok 20 sn
        .readTimeout(60, TimeUnit.SECONDS) // veri gelmezse en çok 60 sn bekle
        .build() // hazır

    // İndirir; onProgress indirilen ve toplam MB'ı bildirir. Video değilse ya da çok büyükse hata verir.
    suspend fun download(url: String, userAgent: String, cookies: String?, referer: String?, dest: File, onProgress: (Long, Long) -> Unit): File =
        withContext(Dispatchers.IO) { // ağ işi arka planda
            val req = Request.Builder().url(url).header("User-Agent", userAgent) // tarayıcıyla aynı kimlik
                .apply { if (!cookies.isNullOrBlank()) header("Cookie", cookies) } // sitenin çerezleri (indirme izni için)
                .apply { if (!referer.isNullOrBlank() && referer.startsWith("http")) header("Referer", referer) } // hangi sayfadan gelindiği (bazı siteler ister)
                .build() // istek hazır
            client.newCall(req).execute().use { resp -> // istek atılıyor
                if (!resp.isSuccessful) error("Sunucu ${resp.code} döndü") // başarısızsa hata
                val body = resp.body ?: error("Boş cevap") // içerik
                val type = body.contentType()?.toString().orEmpty() // dosya türü
                if (!type.startsWith("video/") && !type.startsWith("application/octet-stream") && !url.substringBefore('?').endsWith(".mp4")) {
                    error("Bu bir video dosyası değil") // video değilse
                }
                val total = body.contentLength() // toplam boyut (bilinmiyorsa -1)
                if (total > MAX_BYTES) error("Video çok büyük (300 MB üstü). Daha düşük kalite seç.") // çok büyükse
                dest.parentFile?.mkdirs() // klasör hazırlanıyor
                dest.outputStream().use { out -> // dosyaya yazılıyor
                    val input = body.byteStream() // gelen veri
                    val buf = ByteArray(64 * 1024) // 64 KB'lık parçalar
                    var done = 0L // indirilen bayt
                    while (true) { // veri bitene kadar
                        ensureActive() // iptal edildiyse dur
                        val n = input.read(buf) // parça oku
                        if (n < 0) break // bitti
                        out.write(buf, 0, n) // dosyaya yaz
                        done += n // sayaç
                        if (done > MAX_BYTES) error("Video çok büyük (300 MB üstü). Daha düşük kalite seç.") // sınır aşıldı
                        onProgress(done, total) // ilerleme
                    }
                }
            }
            dest // indirilen dosya
        }
}
