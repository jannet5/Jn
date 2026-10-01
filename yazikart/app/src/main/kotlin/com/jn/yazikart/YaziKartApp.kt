package com.jn.yazikart // ana paket

import android.app.Application // uygulamanın kendisi
import coil.ImageLoader // görsel yükleyici
import coil.ImageLoaderFactory // Coil'e kendi yükleyicimizi vermek için
import com.jn.yazikart.search.ImageSearch // ortak internet istemcisi

// Uygulama açılınca bir kez oluşur; tüm görsel indirmeleri aynı ayarlı istemciyi kullanır
class YaziKartApp : Application(), ImageLoaderFactory {
    // Coil görsel yükleyicisi: Wikimedia kimlik başlığı istediği için bizim istemcimizi kullanıyor
    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this) // yükleyici kuruluyor
        .okHttpClient { ImageSearch.defaultClient() } // kimlik başlıklı internet istemcisi
        .crossfade(true) // resimler yumuşak geçişle görünsün
        .build() // yükleyici hazır
}
