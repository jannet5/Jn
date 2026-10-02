package com.jn.oynatici.data

// Süre yazma/okuma yardımcıları
object Zaman {

    // Milisaniyeyi "1:28" ya da "1:02:03" biçimine çevir
    fun yaz(ms: Long): String {
        val toplam = (ms.coerceAtLeast(0) + 500) / 1000 // en yakın saniyeye yuvarla
        val saat = toplam / 3600 // saat
        val dakika = (toplam % 3600) / 60 // dakika
        val saniye = toplam % 60 // saniye
        return if (saat > 0) "%d:%02d:%02d".format(saat, dakika, saniye) // saatliyse 1:02:03
        else "%d:%02d".format(dakika, saniye) // değilse 1:28
    }

    // Kullanıcının yazdığı süreyi milisaniyeye çevir.
    // "1.28", "1:28", "1,28" -> 1 dk 28 sn ; "1:02:03" -> 1 sa 2 dk 3 sn ; "95" -> 95 sn
    fun oku(yazi: String): Long? {
        val parcalar = yazi.trim().split(':', '.', ',').map { it.trim() } // ayırıcılara göre böl
        if (parcalar.isEmpty() || parcalar.size > 3 || parcalar.any { it.isEmpty() || !it.all(Char::isDigit) }) return null // geçersiz
        val sayilar = parcalar.map { it.toLong() } // sayıya çevir
        if (sayilar.size > 1 && sayilar.drop(1).any { it >= 60 }) return null // dakika/saniye 60'tan küçük olmalı
        var saniye = 0L // toplam saniye
        sayilar.forEach { saniye = saniye * 60 + it } // sa:dk:sn şeklinde topla
        return saniye * 1000 // milisaniye olarak döndür
    }

    // Dosya adında kullanmak için "1.28" biçimi (iki nokta dosya adında olmaz)
    fun dosyaAdi(ms: Long): String = yaz(ms).replace(':', '.')
}
