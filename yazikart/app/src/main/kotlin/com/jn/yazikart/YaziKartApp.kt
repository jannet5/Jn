package com.jn.yazikart // ana paket

import android.app.Application // uygulamanın kendisi
import coil.ImageLoader // görsel yükleyici
import coil.ImageLoaderFactory // Coil'e kendi yükleyicimizi vermek için
import okhttp3.OkHttpClient // internet istemcisi
import java.util.concurrent.TimeUnit // zaman aşımı birimi

// Uygulama açılınca bir kez oluşur; görsel indirmeleri aynı ayarlı istemciyi kullanır.
// Uygulamanın kendi sunucusu yoktur: görseller doğrudan bulundukları siteden telefona iner.
class YaziKartApp : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this) // yükleyici kuruluyor
        .okHttpClient { // internet istemcisi
            OkHttpClient.Builder() // kurucu
                .connectTimeout(15, TimeUnit.SECONDS) // bağlanma için en çok 15 sn
                .readTimeout(30, TimeUnit.SECONDS) // indirme için en çok 30 sn
                .addInterceptor { chain -> // bazı siteler tarayıcı olmayan isteklere resim vermez
                    chain.proceed(chain.request().newBuilder().header("User-Agent", BROWSER_UA).build()) // tarayıcı kimliğiyle istek
                }
                .build() // istemci hazır
        }
        .crossfade(true) // resimler yumuşak geçişle görünsün
        .build() // yükleyici hazır

    companion object {
        // Sıradan bir Android Chrome tarayıcısının kimliği (görsel siteleri bunu bekler)
        const val BROWSER_UA = "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0 Mobile Safari/537.36"
    }
}
