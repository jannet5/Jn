package com.jn.melodizil.core // Saf Kotlin çekirdek

/** Melodinin çalınabileceği "formlar". Kullanıcı sonuç ekranında bunlardan birini seçer. */
enum class Instrument(
    val id: String, // Kalıcı kimlik (kayıtlarda kullanılır)
    val transpose: Int, // Yarım ton kaydırma (müzik kutusu gibi tınılar bir oktav yukarıda daha doğal)
) {
    PIYANO("piyano", 0), // Klasik piyano: sıcak, dengeli
    MUZIK_KUTUSU("muzik_kutusu", 12), // Müzik kutusu: tiz, çınlayan
    SEKIZ_BIT("8bit", 0), // Retro oyun sesi: kare dalga
    MARIMBA("marimba", 0), // Ahşap vurmalı: kısa, yuvarlak
    FLUT("flut", 0), // Flüt: yumuşak, sürekli
    CAN("can", 12), // Çan: uzun çınlama, armonik
    GITAR("gitar", 0), // Akustik gitar: tele vurma
    SYNTH("synth", 0); // Synth pad: rüya gibi, geniş

    companion object {
        fun fromId(id: String): Instrument = entries.firstOrNull { it.id == id } ?: PIYANO // Kimlikten enum, bilinmiyorsa piyano
    }
}
