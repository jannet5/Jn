package com.jn.koken.data

/** Arapça türetme türlerinin ve fiil kalıplarının (bâb) Türkçe adları */
internal object Derivation {
    /** İngilizce açıklamadaki türetme türü → Türkçe ("Derived from the active participle of ..." → etken ortacı) */
    fun howOf(text: String): String? {
        val t = text.lowercase()
        return when {
            "active participle" in t -> "etken ortacı (ism-i fâil)"
            "passive participle" in t -> "edilgen ortacı (ism-i mefʿûl)"
            "verbal noun" in t -> "mastarı (fiil adı)"
            "elative" in t -> "üstünlük sıfatı (ism-i tafdîl)"
            "diminutive" in t -> "küçültme hâli"
            "plural" in t -> "çoğul hâli"
            "feminine" in t -> "dişil hâli"
            "nisba" in t || "relational adjective" in t -> "nispet sıfatı"
            "noun of place" in t -> "yer adı (ism-i mekân)"
            "noun of instrument" in t -> "alet adı (ism-i âlet)"
            else -> null
        }
    }

    /** Fiil kalıbı: "VIII" → "VIII. bâb (iftiʿâl)" */
    fun formName(form: String): String? = when (form.uppercase()) {
        "I" -> "I. bâb (temel kalıp, faʿale)"
        "II" -> "II. bâb (tefʿîl)"
        "III" -> "III. bâb (müfâale)"
        "IV" -> "IV. bâb (ifʿâl)"
        "V" -> "V. bâb (tefeʿʿül)"
        "VI" -> "VI. bâb (tefâʿul)"
        "VII" -> "VII. bâb (infiʿâl)"
        "VIII" -> "VIII. bâb (iftiʿâl)"
        "IX" -> "IX. bâb (ifʿilâl)"
        "X" -> "X. bâb (istifʿâl)"
        else -> null
    }
}
