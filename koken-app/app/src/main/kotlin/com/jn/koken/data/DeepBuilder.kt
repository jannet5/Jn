package com.jn.koken.data

/**
 * Zincirin en eski gerçek sözcüğünün (örn. Arapça مُمْتَاز) kendi Wiktionary sayfasından
 * kaynak dildeki anlamını, kendi atalarını, kökünü ve türetme türünü çıkarır.
 */
object DeepBuilder {
    // Arapça harekeler + tatweel, İbranice noktalama/niqqud
    private val marks = Regex("[ً-ٰٟـ֑-ׇ]")
    private val latinMarks = mapOf(
        'ā' to 'a', 'ē' to 'e', 'ī' to 'i', 'ō' to 'o', 'ū' to 'u', 'ȳ' to 'y',
        'ă' to 'a', 'ĕ' to 'e', 'ĭ' to 'i', 'ŏ' to 'o', 'ŭ' to 'u',
    )

    /** Harekesiz, küçük harfli karşılaştırma anahtarı */
    internal fun norm(s: String?): String = marks.replace(s.orEmpty(), "").lowercase()

    /** Kaynak sözcüğün Wiktionary sayfa başlığı; yeniden kurulmuş ("*") sözcükler için null */
    fun title(m: Mention): String? {
        val t = m.text.trim()
        if (t.isEmpty() || t.startsWith("*") || m.isRoot) return null
        val s = when (m.langCode) {
            "la", "ang" -> t.map { latinMarks[it] ?: it }.joinToString("")
            "zh", "cmn", "yue", "ja", "ko" -> t.substringBefore("/")
            "ru", "uk", "be", "bg", "sr", "mk", "orv" -> t.replace("\u0301", "")
            else -> marks.replace(t, "")
        }
        return s.takeIf { it.isNotBlank() }
    }

    // "second-person masculine singular imperative of حَمِدَ" gibi dilbilgisi tanımları anlam sayılmaz
    private val grammatical = Regex(
        "(?i)(^|\\)\\s)(first|second|third)-person|\\b(imperative|participle|verbal noun|masdar|plural|singular|dual|feminine|" +
            "masculine|inflection|genitive|dative|accusative|ablative|nominative|vocative|non-past|past tense|passive|gerund|" +
            "alternative form|obsolete spelling|misspelling|diminutive|elative)\\b.*\\bof\\b",
    )
    // "… of كَتَبَ (kataba) (form I)" içindeki gönderme verilen Arapça sözcük ve okunuşu
    private val refTerm = Regex("\\bof\\s+([\\p{L}\\p{M}'-]+)\\s*(?:\\(([^)]*)\\))?")
    private val formRef = Regex("\\(form ([IVX]+)\\)")

    fun build(target: Mention, page: WiktionaryResult): DeepInfo? {
        val section = page.languages.firstOrNull { it.language == target.langName } ?: return null
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

        // "elative degree of حَمِيد" / "verbal noun of كَتَبَ (form I)" gibi tanımlar türetme türünü ve atayı söyler
        var chain = pick.etymology?.chain.orEmpty().take(5)
        val gramDef = pick.entries.flatMap { it.definitions }.firstOrNull { grammatical.containsMatchIn(it) }
        var formHint: String? = null
        if (gramDef != null) {
            val how = Derivation.howOf(gramDef)
            formHint = formRef.find(gramDef)?.groupValues?.get(1)
            if (chain.isEmpty()) {
                refTerm.find(gramDef)?.let { m ->
                    // "(qadira, “to be able to”)" → okunuş "qadira", anlam "to be able to"
                    val inner = m.groupValues[2]
                    val translit = inner.substringBefore(",").trim().takeIf { it.isNotBlank() && !it.startsWith("form ") && !it.startsWith("“") }
                    val gloss = Regex("“([^”]+)”").find(inner)?.groupValues?.get(1)
                    chain = listOf(
                        Mention(target.langCode, target.langName, m.groupValues[1], translit, gloss, Relation.DERIVED, glossEn = gloss != null, how = how),
                    )
                }
            } else if (chain[0].how == null && how != null) {
                chain = listOf(chain[0].copy(how = how)) + chain.drop(1)
            }
        }
        return DeepInfo(target, defs, chain, section.root, formHint)
    }
}
