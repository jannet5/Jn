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
)

/** Bir "Köken" bölümü: açıklama metni + ayrıştırılmış zincir */
data class EtymologyBlock(
    val text: String,
    val chain: List<Mention>,
    val related: List<Mention>,
    val parts: List<Mention>,
) {
    /** Zincirdeki en eski dil; "Proto-" (yeniden kurulmuş) dilleri atlanır */
    val originLanguage: String?
        get() {
            if (chain.isEmpty()) return null
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

data class LanguageSection(val language: String, val groups: List<SenseGroup>)

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
