package com.jn.koken.data

/** İngilizce dil adlarını (Wiktionary) Türkçeye çevirir; bilinmeyen ad olduğu gibi kalır. */
object LangNames {
    private val base = mapOf(
        "Arabic" to "Arapça", "Persian" to "Farsça", "Turkish" to "Türkçe", "English" to "İngilizce",
        "French" to "Fransızca", "German" to "Almanca", "Italian" to "İtalyanca", "Spanish" to "İspanyolca",
        "Portuguese" to "Portekizce", "Russian" to "Rusça", "Greek" to "Yunanca", "Latin" to "Latince",
        "Hebrew" to "İbranice", "Aramaic" to "Aramice", "Akkadian" to "Akadca", "Sumerian" to "Sümerce",
        "Sanskrit" to "Sanskritçe", "Hindi" to "Hintçe", "Urdu" to "Urduca", "Chinese" to "Çince",
        "Mandarin" to "Mandarin Çincesi", "Cantonese" to "Kantonca", "Japanese" to "Japonca", "Korean" to "Korece",
        "Dutch" to "Felemenkçe", "Swedish" to "İsveççe", "Danish" to "Danca", "Norwegian" to "Norveççe",
        "Finnish" to "Fince", "Hungarian" to "Macarca", "Polish" to "Lehçe", "Czech" to "Çekçe",
        "Bulgarian" to "Bulgarca", "Serbo-Croatian" to "Sırp-Hırvatça", "Bosnian" to "Boşnakça",
        "Serbian" to "Sırpça", "Croatian" to "Hırvatça", "Albanian" to "Arnavutça", "Romanian" to "Rumence",
        "Armenian" to "Ermenice", "Georgian" to "Gürcüce", "Azerbaijani" to "Azerbaycan Türkçesi",
        "Uzbek" to "Özbekçe", "Kazakh" to "Kazakça", "Kyrgyz" to "Kırgızca", "Tatar" to "Tatarca",
        "Crimean Tatar" to "Kırım Tatarcası", "Turkmen" to "Türkmence", "Uyghur" to "Uygurca",
        "Mongolian" to "Moğolca", "Kurdish" to "Kürtçe", "Central Kurdish" to "Orta Kürtçe",
        "Northern Kurdish" to "Kurmançça", "Gothic" to "Gotça", "Swahili" to "Svahili", "Indonesian" to "Endonezce",
        "Malay" to "Malayca", "Vietnamese" to "Vietnamca", "Thai" to "Tayca", "Tamil" to "Tamilce",
        "Bengali" to "Bengalce", "Welsh" to "Galce", "Irish" to "İrlandaca", "Scottish Gaelic" to "İskoç Gaelcesi",
        "Icelandic" to "İzlandaca", "Latvian" to "Letonca", "Lithuanian" to "Litvanca", "Estonian" to "Estonca",
        "Ukrainian" to "Ukraynaca", "Belarusian" to "Belarusça", "Macedonian" to "Makedonca", "Slovak" to "Slovakça",
        "Slovene" to "Slovence", "Catalan" to "Katalanca", "Occitan" to "Oksitanca", "Galician" to "Galiçyaca",
        "Maltese" to "Maltaca", "Egyptian" to "Eski Mısır dili", "Coptic" to "Kıpti dili", "Phoenician" to "Fenikece",
        "Ugaritic" to "Ugaritçe", "Hittite" to "Hititçe", "Ge'ez" to "Geez", "Amharic" to "Amharca",
        "Berber" to "Berberice", "Hausa" to "Hausaca", "Yoruba" to "Yorubaca", "Zulu" to "Zuluca",
        "Nahuatl" to "Nahuatl", "Quechua" to "Keçuaca", "Tupi" to "Tupice", "Hawaiian" to "Hawaiice",
        "Maori" to "Maoriçe", "Tagalog" to "Tagalogca", "Esperanto" to "Esperanto", "Yiddish" to "Yidiş",
        "Romani" to "Romanca", "Chagatai" to "Çağatayca", "Karakhanid" to "Karahanlı Türkçesi",
        "Ottoman Turkish" to "Osmanlı Türkçesi", "Old Anatolian Turkish" to "Eski Anadolu Türkçesi",
        "Old Turkic" to "Eski Türkçe", "Anglo-Norman" to "Anglo-Norman", "Norman" to "Norman Fransızcası",
        "Ancient Greek" to "Eski Yunanca", "Koine Greek" to "Koine Yunancası", "Byzantine Greek" to "Bizans Yunancası",
        "Medieval Greek" to "Bizans Yunancası", "Medieval Latin" to "Ortaçağ Latincesi", "Vulgar Latin" to "Halk Latincesi",
        "Late Latin" to "Geç Latince", "New Latin" to "Yeni Latince", "Classical Latin" to "Klasik Latince",
        "Ecclesiastical Latin" to "Kilise Latincesi", "Old Norse" to "Eski İskandinav dili",
        "Proto-Indo-European" to "Proto-Hint-Avrupa dili", "Proto-Germanic" to "Proto-Cermen dili",
        "Proto-West Germanic" to "Proto-Batı Cermen dili", "Proto-Semitic" to "Proto-Sami dili",
        "Proto-Turkic" to "Proto-Türk dili", "Proto-Italic" to "Proto-İtalik dil", "Proto-Slavic" to "Proto-Slav dili",
        "Proto-Celtic" to "Proto-Kelt dili", "Proto-Indo-Iranian" to "Proto-Hint-İran dili",
        "Proto-Iranian" to "Proto-İran dili", "Proto-Hellenic" to "Proto-Yunan dili",
        "Proto-Afroasiatic" to "Proto-Afro-Asya dili", "Proto-Mongolic" to "Proto-Moğol dili",
        "Proto-Uralic" to "Proto-Ural dili", "Proto-Balto-Slavic" to "Proto-Balto-Slav dili",
        "Proto-Romance" to "Proto-Roman dili", "Proto-Armenian" to "Proto-Ermeni dili",
    )

    private val prefixes = listOf(
        "Old " to "Eski ", "Middle " to "Orta ", "Late " to "Geç ", "Early " to "Erken ",
        "Classical " to "Klasik ", "Medieval " to "Ortaçağ ", "Ancient " to "Eski ", "Modern " to "Modern ",
    )

    /** Dil kodu → İngilizce ad (sayfada ad bulunamazsa yedek) */
    private val codes = mapOf(
        "ar" to "Arabic", "fa" to "Persian", "tr" to "Turkish", "en" to "English", "fr" to "French",
        "de" to "German", "it" to "Italian", "es" to "Spanish", "pt" to "Portuguese", "ru" to "Russian",
        "el" to "Greek", "la" to "Latin", "he" to "Hebrew", "arc" to "Aramaic", "akk" to "Akkadian",
        "sa" to "Sanskrit", "hi" to "Hindi", "ur" to "Urdu", "zh" to "Chinese", "ja" to "Japanese",
        "ko" to "Korean", "nl" to "Dutch", "sv" to "Swedish", "da" to "Danish", "fi" to "Finnish",
        "hu" to "Hungarian", "pl" to "Polish", "bg" to "Bulgarian", "sh" to "Serbo-Croatian",
        "sq" to "Albanian", "ro" to "Romanian", "hy" to "Armenian", "ka" to "Georgian", "az" to "Azerbaijani",
        "ota" to "Ottoman Turkish", "grc" to "Ancient Greek", "enm" to "Middle English", "ang" to "Old English",
        "fro" to "Old French", "frm" to "Middle French", "xno" to "Anglo-Norman", "non" to "Old Norse",
        "goh" to "Old High German", "gmh" to "Middle High German", "gem-pro" to "Proto-Germanic",
        "ine-pro" to "Proto-Indo-European", "sem-pro" to "Proto-Semitic", "trk-pro" to "Proto-Turkic",
        "itc-pro" to "Proto-Italic", "sla-pro" to "Proto-Slavic", "gmw-pro" to "Proto-West Germanic",
        "peo" to "Old Persian", "pal" to "Middle Persian", "got" to "Gothic", "cu" to "Old Church Slavonic",
        "ky" to "Kyrgyz", "kk" to "Kazakh", "uz" to "Uzbek", "tt" to "Tatar", "tk" to "Turkmen",
        "ug" to "Uyghur", "mn" to "Mongolian", "ku" to "Kurdish", "sw" to "Swahili", "id" to "Indonesian",
        "ms" to "Malay", "cmn" to "Mandarin", "yue" to "Cantonese", "egy" to "Egyptian", "sux" to "Sumerian",
        "ota-Arab" to "Ottoman Turkish", "chg" to "Chagatai", "otk" to "Old Turkic", "bs" to "Bosnian",
        "sr" to "Serbian", "hr" to "Croatian", "mk" to "Macedonian", "uk" to "Ukrainian", "cs" to "Czech",
    )

    fun codeToEnglish(code: String): String? = codes[code]

    /** İngilizce dil adını Türkçeye çevirir */
    fun tr(name: String): String {
        val n = name.trim()
        base[n]?.let { return it }
        for ((en, trPrefix) in prefixes) {
            if (n.startsWith(en)) {
                val rest = base[n.removePrefix(en)] ?: return n
                return trPrefix + rest
            }
        }
        return n
    }
}
