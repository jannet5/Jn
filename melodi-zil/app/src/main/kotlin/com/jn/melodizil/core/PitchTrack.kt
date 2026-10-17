package com.jn.melodizil.core // Saf Kotlin çekirdek

/** Kare kare perde izi: [hopSec] aralıklı; [f0] Hz (0 = sessiz); [salience] 0..1 (RMVPE'nin o karedeki en büyük sınıf olasılığı). */
class PitchTrack(val hopSec: Float, val f0: FloatArray, val salience: FloatArray) {
    init { require(f0.size == salience.size) { "f0 ve salience aynı uzunlukta olmalı" } } // Tutarlılık
    val size: Int get() = f0.size // Kare sayısı
    fun timeOf(i: Int): Float = i * hopSec // Karenin zamanı (sn)
}
