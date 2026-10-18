package com.jn.koken.data

/**
 * Zincirin en eski gerçek sözcüğünün (örn. Arapça مُمْتَاز) kendi Wiktionary sayfasından
 * kaynak dildeki anlamını, kendi atalarını ve (varsa) kökünü çıkarır.
 */
object DeepBuilder {
    // Arapça harekeler + tatweel, İbranice noktalama/niqqud
    private val marks = Regex("[ً-ٰٟـ֑-ׇ]")
    private val latinMarks = mapOf(
        'ā' to 'a', 'ē' to 'e', 'ī' to 'i', 'ō' to 'o', 'ū' to 'u', 'ȳ' to 'y',
        'ă' to 'a', 'ĕ' to 'e', 'ĭ' to 'i', 'ŏ' to 'o', 'ŭ' to 'u',
    )

    /** Kaynak sözcüğün Wiktionary sayfa başlığı; yeniden kurulmuş ("*") sözcükler için null */
    fun title(m: Mention): String? {
        val t = m.text.trim()
        if (t.isEmpty() || t.startsWith("*") || m.isRoot) return null
        val s = when (m.langCode) {
            "la", "ang" -> t.map { latinMarks[it] ?: it }.joinToString("")
            else -> marks.replace(t, "")
        }
        return s.takeIf { it.isNotBlank() }
    }

    // "second-person masculine singular imperative of حَمِدَ" gibi dilbilgisi tanımları anlam sayılmaz
    private val grammatical = Regex(
        "(?i)(^|\\)\\s)(first|second|third)-person|\\b(imperative|participle|verbal noun|masdar|plural|singular|dual|feminine|" +
            "masculine|inflection|genitive|dative|accusative|ablative|nominative|vocative|non-past|past tense|passive|gerund|" +
            "alternative form|obsolete spelling|misspelling|diminutive)\\b.*\\bof\\b",
    )

    fun build(target: Mention, page: WiktionaryResult): DeepInfo? {
        val section = page.languages.firstOrNull { it.language == target.langName } ?: return null
        fun norm(s: String?) = marks.replace(s.orEmpty(), "").lowercase()
        // Aynı yazılışlı birden çok sözcük olabilir: başlık sözcüğü eşleşen köken gruplarını sırayla dene
        var groups = section.groups.filter { g -> g.entries.any { it.headword == target.text } }
        if (groups.isEmpty()) groups = section.groups.filter { g -> g.entries.any { norm(it.headword) == norm(target.text) } }
        if (groups.isEmpty()) groups = section.groups.take(1)
        if (groups.isEmpty()) return null

        // Önce gerçek anlam taşıyan (dilbilgisi göndermesi olmayan, "özel ad" olmayan) ilk grup
        fun realDefs(g: SenseGroup, allowName: Boolean): List<String> =
            g.entries.filter { allowName || it.pos != "Özel ad" }
                .flatMap { it.definitions }.filter { !grammatical.containsMatchIn(it) }.take(3)
        val pick = groups.firstOrNull { realDefs(it, false).isNotEmpty() }
            ?: groups.firstOrNull { realDefs(it, true).isNotEmpty() }
            ?: groups.first()
        val defs = realDefs(pick, false).ifEmpty { realDefs(pick, true) }
            .ifEmpty { pick.entries.flatMap { it.definitions }.take(3) }
        return DeepInfo(
            term = target,
            definitions = defs,
            chain = pick.etymology?.chain.orEmpty().take(5),
            root = section.root,
        )
    }
}
