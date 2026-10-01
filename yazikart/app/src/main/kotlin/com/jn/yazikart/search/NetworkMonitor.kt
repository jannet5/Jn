package com.jn.yazikart.search // görsel arama paketi

import android.content.Context // uygulama bağlamı
import android.net.ConnectivityManager // bağlantı yöneticisi
import android.net.Network // tek bir ağ (Wi-Fi, mobil veri)
import android.net.NetworkCapabilities // ağın özellikleri (internet var mı)
import kotlinx.coroutines.channels.awaitClose // akış kapanınca temizlik için
import kotlinx.coroutines.flow.Flow // değişen değer akışı
import kotlinx.coroutines.flow.callbackFlow // geri çağrıdan akış üretmek için
import kotlinx.coroutines.flow.distinctUntilChanged // aynı değeri tekrar yaymamak için

// İnternete bağlı olup olmadığımızı canlı olarak takip eder (Wi-Fi, 4.5G, ne olursa)
class NetworkMonitor(context: Context) {
    private val cm = context.getSystemService(ConnectivityManager::class.java) // sistemin bağlantı yöneticisi

    // Şu an internet var mı?
    fun isOnline(): Boolean {
        val caps = cm.getNetworkCapabilities(cm.activeNetwork ?: return false) ?: return false // aktif ağın özellikleri
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) // internet özelliği var mı
    }

    // Bağlantı değiştikçe true/false yayan akış
    val online: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() { // sistemden haber alacak nesne
            override fun onAvailable(network: Network) { trySend(isOnline()) } // ağ geldi
            override fun onLost(network: Network) { trySend(isOnline()) } // ağ gitti
            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) { trySend(isOnline()) } // ağ değişti
        }
        trySend(isOnline()) // ilk durum hemen gönderiliyor
        cm.registerDefaultNetworkCallback(callback) // sistem dinlenmeye başlanıyor
        awaitClose { cm.unregisterNetworkCallback(callback) } // akış kapanınca dinleme bırakılıyor
    }.distinctUntilChanged() // sadece değişince haber ver
}
