package com.jn.koken.data

/** Veri kaynakları. Üçü de anahtarsız, herkese açık sayfalar. */
enum class Source(val label: String, val host: String) {
    WIKT_TR("Vikisözlük", "tr.wiktionary.org"),
    WIKT_EN("Wiktionary", "en.wiktionary.org"),
    WIKI_TR("Vikipedi", "tr.wikipedia.org"),
}

/** Bir basamağın bir sonrakine (daha yeni olana) nasıl geçtiği */
enum class Relation(val label: String) {
    BORROWED("ödünç alındı"),
    INHERITED("devralındı"),
    DERIVED("türetildi"),
}

/** Köken zincirindeki bir sözcük: dil + yazılış + okunuş + anlam */
data class Mention(
    val langCode: String,
    val langName: String,
    val text: String,
    val translit: String?,
    val gloss: String?,
    val relation: Relation?,
    val isRoot: Boolean = false,   // Sami dillerindeki sözcük kökü (örn. م ي ز)
    val glossEn: Boolean = false,  // anlam metni İngilizce mi (çevrilmesi gerekir)
    /** Bir sonraki (daha yeni) sözcüğün bundan nasıl türediği (Türkçe): "etken ortacı", "VIII. bâb" vb. */
    val how: String? = null,
)

/** Bir "Köken" bölümü: açıklama metni + ayrıştırılmış zincir */
data class EtymologyBlock(
    val text: String,
    val chain: List<Mention>,
    val related: List<Mention>,
    val parts: List<Mention>,
    /** Zincir yoksa, köken bölümünde tek başına yazılmış dil adı (örn. "Arapça") */
    val lonelyLabel: String? = null,
) {
    /** Zincirdeki en eski dil; "Proto-" (yeniden kurulmuş) dilleri atlanır */
    val originLanguage: String?
        get() {
            if (chain.isEmpty()) return lonelyLabel
            val real = chain.lastOrNull { !it.langName.startsWith("Proto") }
            return (real ?: chain.last()).langName
        }
}

/** "İsim: 1) ... 2) ..." gibi bir sözcük türü kaydı */
data class PosEntry(val pos: String, val headword: String?, val definitions: List<String>)

/** Aynı köken başlığı altındaki türler ve köken bilgisi */
data class SenseGroup(
    val heading: String,
    val etymology: EtymologyBlock?,
    val entries: List<PosEntry>,
)

/** Sami dili kökü: harfler ve okunuşu (örn. "م ي ز", "m y z") */
data class Root(val letters: String, val translit: String?)

data class LanguageSection(val language: String, val groups: List<SenseGroup>, val root: Root? = null)

/** Arapça fiil sayfasından: kalıp numarası (I–X), anlam, mastar ve etken ortaç */
data class VerbInfo(
    val verb: Mention,
    val form: String?,
    val definitions: List<String>,
    val verbalNoun: Mention?,
    val activeParticiple: Mention?,
)

/** Zincirin en eski sözcüğünün kendi sayfasından alınan ek bilgi: kaynak dildeki anlam, atalar, kök ve fiil kalıbı */
data class DeepInfo(
    val term: Mention,
    val definitions: List<String>,
    val chain: List<Mention>,
    val root: Root?,
    val formHint: String? = null,
    val verb: VerbInfo? = null,
)

data class WiktionaryResult(
    val source: Source,
    val title: String,
    val url: String,
    val languages: List<LanguageSection>,
)

data class WikipediaResult(
    val title: String,
    val url: String,
    val facts: List<Pair<String, String>>,
    val paragraphs: List<String>,
)

/** Bir aramanın tüm kaynaklardan toplanmış sonucu */
data class LookupResult(
    val query: String,
    val wiktTr: WiktionaryResult?,
    val wiktEn: WiktionaryResult?,
    val wiki: WikipediaResult?,
    /** Bir kaynağa ağ/sunucu hatası yüzünden ulaşılamadı: sonuç eksik olabilir */
    val incomplete: Boolean = false,
) {
    val isEmpty: Boolean get() = wiktTr == null && wiktEn == null && wiki == null

    /** Özet için: (dil bölümü, köken dili) — önce Türkçe bölümler, önce Türkçe kaynak */
    val origin: Pair<String, String>?
        get() {
            for (r in listOfNotNull(wiktTr, wiktEn)) {
                for (lang in r.languages.sortedBy { languageRank(it.language) }) {
                    for (g in lang.groups) {
                        val o = g.etymology?.originLanguage
                        if (o != null) return lang.language to o
                    }
                }
            }
            return null
        }

    /** Özet kartı için ilk anlam cümlesi */
    val firstMeaning: String?
        get() {
            for (r in listOfNotNull(wiktTr, wiktEn)) {
                for (lang in r.languages.sortedBy { languageRank(it.language) }) {
                    for (g in lang.groups) for (e in g.entries) e.definitions.firstOrNull()?.let { return it }
                }
            }
            return wiki?.paragraphs?.firstOrNull()
        }

    companion object {
        /** Türkçe en önce, sonra İngilizce, sonra sayfadaki sıra (sıralama kararlıdır) */
        fun languageRank(name: String): Int = when (name) {
            "Türkçe" -> 0
            "İngilizce" -> 1
            else -> 2
        }
    }
}
