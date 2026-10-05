package com.jn.melodizil.core // Saf Kotlin çekirdek: Android bağımlılığı yok, JVM'de test edilir

import kotlin.math.PI // Pi sabiti
import kotlin.math.cos // Kosinüs
import kotlin.math.sin // Sinüs

/**
 * Yerinde (in-place) radix-2 karmaşık FFT. Boyut 2'nin kuvveti olmalı.
 * Sin/cos tabloları ve bit tersleme indeksleri bir kez hesaplanır, her çerçevede tekrar kullanılır.
 */
class Fft(val size: Int) {
    private val cosTable = FloatArray(size / 2) // Twiddle kosinüsleri
    private val sinTable = FloatArray(size / 2) // Twiddle sinüsleri
    private val reverse = IntArray(size) // Bit tersleme permütasyonu

    init {
        require(size >= 2 && size and (size - 1) == 0) { "FFT boyutu 2'nin kuvveti olmalı: $size" } // Boyut kontrolü
        for (i in 0 until size / 2) { // Twiddle tabloları dolduruluyor
            cosTable[i] = cos(2.0 * PI * i / size).toFloat() // cos(2πi/N)
            sinTable[i] = sin(2.0 * PI * i / size).toFloat() // sin(2πi/N)
        }
        val bits = Integer.numberOfTrailingZeros(size) // log2(N)
        for (i in 0 until size) reverse[i] = Integer.reverse(i) ushr (32 - bits) // i'nin bit tersi
    }

    /** re/im dizilerini yerinde dönüştürür. */
    fun transform(re: FloatArray, im: FloatArray) {
        for (i in 0 until size) { // Bit tersleme sıralaması
            val j = reverse[i] // Hedef indeks
            if (j > i) { // Her çift yalnızca bir kez değiştirilir
                var t = re[i]; re[i] = re[j]; re[j] = t // Gerçek kısım takası
                t = im[i]; im[i] = im[j]; im[j] = t // Sanal kısım takası
            }
        }
        var len = 2 // Alt dönüşüm uzunluğu
        while (len <= size) { // Her aşamada uzunluk ikiye katlanır
            val half = len / 2 // Yarım uzunluk
            val step = size / len // Twiddle tablosundaki adım
            var i = 0 // Blok başlangıcı
            while (i < size) { // Tüm bloklar
                var k = 0 // Twiddle indeksi
                for (j in i until i + half) { // Kelebek işlemleri
                    val wr = cosTable[k]; val wi = -sinTable[k] // İleri dönüşüm için e^{-iθ}
                    val xr = re[j + half]; val xi = im[j + half] // İkinci yarı örneği
                    val tr = xr * wr - xi * wi // Karmaşık çarpım gerçek kısım
                    val ti = xr * wi + xi * wr // Karmaşık çarpım sanal kısım
                    re[j + half] = re[j] - tr; im[j + half] = im[j] - ti // Alt kelebek
                    re[j] += tr; im[j] += ti // Üst kelebek
                    k += step // Sonraki twiddle
                }
                i += len // Sonraki blok
            }
            len = len shl 1 // Uzunluk ikiye katlanır
        }
    }
}
