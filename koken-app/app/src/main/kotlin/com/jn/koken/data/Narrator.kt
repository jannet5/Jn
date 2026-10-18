package com.jn.koken.data

import java.util.Locale

/** Anlatımın bir paragrafı: isteğe bağlı küçük başlık + düz Türkçe metin */
data class Para(val label: String?, val text: String)

/** Sonuç ekranının ana içeriği: sanki biri anlatıyormuş gibi okunan metin + yolculuk */
data class Story(
    val word: String,
    val badge: String?,
    val paras: List<Para>,
    /** en yakın → en eski (kök dahil) */
    val timeline: List<Mention>,
    val endLanguage: String,
    val related: List<Mention>,
    /** Çeviri/ayrıntı hâlâ bekleniyor mu */
    val pending: Boolean,
)

/**
 * Toplanan verilerden (anlam, köken zinciri, kök) düz Türkçe bir anlatım üretir.
 * Kural tabanlıdır: yapay zekâ ya da anahtar yok; her cümle kaynak veriden gelir, uydurma yok.
 */
object Narrator {
    private val trLocale = Locale("tr", "TR")

    private class Txt(val s: String, val en: Boolean)

    private class G(
        val word: String, val language: String, val pos: String?, val meanings: List<Txt>,
        val block: EtymologyBlock?, val chain: List<Mention>, val hasWikt: Boolean,
    )

    /** Anlatılacak sözcüğün köken zinciri (en yakın → en eski) */
    fun chainOf(r: LookupResult): List<Mention> = gather(r).chain

    private fun gather(r: LookupResult): G {
        val tr = r.wiktTr?.languages.orEmpty()
        val en = r.wiktEn?.languages.orEmpty()
        val word = r.wiktTr?.title ?: r.wiktEn?.title ?: r.wiki?.title ?: r.query
        val langs = (tr.map { it.language } + en.map { it.language }).distinct().sortedBy { LookupResult.languageRank(it) }
        fun etyOf(s: LanguageSection?): EtymologyBlock? = s?.groups?.firstNotNullOfOrNull { it.etymology }
        fun hasEty(l: String) = etyOf(tr.find { it.language == l }) != null || etyOf(en.find { it.language == l }) != null
        // Önce Türkçe/İngilizce bölümü (köken bilgisi olan), yoksa köken bilgisi olan herhangi biri, yoksa ilk dil
        val lang = langs.firstOrNull { LookupResult.languageRank(it) < 2 && hasEty(it) }
            ?: langs.firstOrNull { hasEty(it) } ?: langs.firstOrNull() ?: "Türkçe"
        val trSec = tr.find { it.language == lang }
        val enSec = en.find { it.language == lang }
        // Zinciri en uzun olan kaynak (eşitlikte Türkçe kaynak)
        val trBlock = etyOf(trSec)
        val enBlock = etyOf(enSec)
        val block = listOfNotNull(trBlock, enBlock).maxByOrNull { it.chain.size }
        var chain = block?.chain.orEmpty()
        // İki kaynak aynı zinciri veriyorsa: Türkçe adları koru, eksik "devralındı/alıntı" bilgisini İngilizce kaynaktan tamamla
        if (trBlock != null && enBlock != null && trBlock.chain.isNotEmpty() &&
            trBlock.chain.map { it.langCode } == enBlock.chain.map { it.langCode }
        ) {
            chain = trBlock.chain.mapIndexed { i, m ->
                val e = enBlock.chain[i]
                m.copy(
                    relation = m.relation ?: e.relation,
                    translit = m.translit ?: e.translit,
                    gloss = m.gloss ?: e.gloss,
                    glossEn = if (m.gloss == null && e.gloss != null) true else m.glossEn,
                )
            }
        }
        fun firstEntry(s: LanguageSection?) = s?.groups?.flatMap { it.entries }?.firstOrNull { it.definitions.isNotEmpty() }
        val trEntry = firstEntry(trSec)
        val entry = trEntry ?: firstEntry(enSec)
        val meanings = entry?.definitions.orEmpty().take(3).map { Txt(it, trEntry == null) }
        return G(word, lang, entry?.pos, meanings, block, chain, r.wiktTr != null || r.wiktEn != null)
    }

    /** Çevrilmesi gereken İngilizce metinler */
    fun englishStrings(r: LookupResult, deep: DeepInfo?): List<String> {
        val g = gather(r)
        val out = LinkedHashSet<String>()
        g.meanings.filter { it.en }.forEach { out += it.s }
        (g.chain + g.block?.parts.orEmpty()).forEach { m -> if (m.glossEn) m.gloss?.let { out += it } }
        deep?.let { d ->
            d.definitions.forEach { out += it }
            d.chain.forEach { m -> if (m.glossEn) m.gloss?.let { out += it } }
            if (d.term.glossEn) d.term.gloss?.let { out += it }
        }
        return out.toList()
    }

    private val copula = mapOf(
        "sözcük" to "sözcüktür", "sıfat" to "sıfattır", "fiil" to "fiildir", "zarf" to "zarftır",
        "isim" to "isimdir", "erkek ismi" to "erkek ismidir", "kadın ismi" to "kadın ismidir",
        "zamir" to "zamirdir", "edat" to "edattır", "bağlaç" to "bağlaçtır", "ünlem" to "ünlemdir",
        "deyim" to "deyimdir", "atasözü" to "atasözüdür", "kısaltma" to "kısaltmadır", "ek" to "ektir",
    )

    private fun genderOf(meanings: List<Txt>): String? {
        val t = meanings.joinToString(" ") { it.s }.lowercase(trLocale)
        return when {
            "erkek" in t || ("male" in t && "female" !in t) -> "erkek ismi"
            "kadın" in t || "female" in t -> "kadın ismi"
            else -> null
        }
    }

    private fun kindOf(pos: String?, gender: String?): String = when (pos) {
        "Özel ad" -> gender ?: "isim"
        "Sıfat" -> "sıfat"
        "Fiil" -> "fiil"
        "Zarf" -> "zarf"
        "Zamir" -> "zamir"
        "Edat" -> "edat"
        "Bağlaç" -> "bağlaç"
        "Ünlem" -> "ünlem"
        "Deyim" -> "deyim"
        "Atasözü" -> "atasözü"
        "Kısaltma" -> "kısaltma"
        else -> "sözcük"
    }

    private fun verb(rel: Relation?): String? = when (rel) {
        Relation.INHERITED -> "devralınmıştır"
        Relation.BORROWED -> "alıntıdır"
        Relation.DERIVED -> "türetilmiştir"
        null -> null
    }

    /**
     * @param translations İngilizce → Türkçe çeviriler
     * @param translating çeviri sürüyor mu (sürüyorsa çevrilmemiş metinler gösterilmez)
     */
    fun compose(r: LookupResult, deep: DeepInfo?, translations: Map<String, String>, translating: Boolean): Story {
        val g = gather(r)
        var pending = false

        // İngilizce metni Türkçesiyle değiştirir; çeviri beklenirken null, başarısızsa İngilizce kalır
        fun tx(s: String, en: Boolean): String? {
            if (!en) return s
            translations[s]?.let { return it }
            if (translating) { pending = true; return null }
            return s
        }
        fun gloss(m: Mention): String? = m.gloss?.let { tx(it, m.glossEn) }
        fun term(m: Mention, omitLang: Boolean = false): String {
            val inside = listOfNotNull(m.translit, gloss(m)?.let { "“$it”" }).joinToString(", ")
            return buildString {
                if (!omitLang) append(m.langName).append(' ')
                append(m.text)
                if (inside.isNotEmpty()) append(" (").append(inside).append(')')
            }
        }
        // "A, B sözcüğünden devralınmıştır, o da C sözcüğünden gelmektedir."
        fun chainSentence(subject: String, subjectLang: String, steps: List<Mention>): String {
            var prev = subjectLang // aynı dil art arda geliyorsa dil adını tekrarlama
            val pieces = steps.mapIndexed { i, m ->
                val v = verb(m.relation) ?: if (i == steps.lastIndex) "gelmektedir" else null
                val t = term(m, omitLang = m.langName == prev)
                prev = m.langName
                "$t sözcüğünden" + (v?.let { " $it" } ?: "")
            }
            return "$subject, " + pieces.joinToString(", o da ") + "."
        }
        fun joinMeanings(list: List<Txt>): String? {
            val parts = list.map { tx(it.s, it.en)?.trim()?.trimEnd('.', ':', ' ') ?: return null }.filter { it.isNotEmpty() }
            if (parts.isEmpty()) return null
            return if (parts.size == 1) parts[0] else parts.mapIndexed { i, s -> "${i + 1}) $s" }.joinToString("\n")
        }

        val paras = ArrayList<Para>()
        val isName = g.pos == "Özel ad"
        val origin = g.block?.originLanguage
        val wikiOrigin = r.wiki?.facts?.firstOrNull { it.first.contains("Köken") }?.second
        val kind = kindOf(g.pos, genderOf(g.meanings))
        val c = copula[kind] ?: "sözcüktür"

        // 1) Kısaca
        val headline = when {
            !g.hasWikt -> null
            g.language == "Türkçe" && origin != null && origin != "Türkçe" -> "$origin kökenli bir $c."
            g.language == "Türkçe" && origin == "Türkçe" -> "Türkçe asıllı bir $c."
            g.language == "Türkçe" -> "Kaynaklarda bu sözcük için köken bilgisi bulunamadı."
            origin != null && origin != g.language -> "${g.language} bir $c; $origin kökenlidir."
            else -> "${g.language} bir $c."
        }
        headline?.let { paras += Para(null, it) }

        // 2) Anlamı (isimlerde: kaynak sözcüğün anlamı)
        if (isName) {
            val deepDefs = deep?.definitions.orEmpty().map { Txt(it, true) }
            val m = if (deepDefs.isNotEmpty()) joinMeanings(deepDefs) else r.wiki?.facts?.firstOrNull { it.first == "Anlam" }?.second
            m?.let { paras += Para("Adın anlamı", it) }
        } else if (g.meanings.isNotEmpty()) {
            joinMeanings(g.meanings)?.let { paras += Para("Anlamı", it) }
        }

        // 3) Nereden geldi
        val chain = g.chain
        if (chain.isNotEmpty()) paras += Para("Nereden geldi?", chainSentence(g.word, g.language, chain))
        g.block?.parts?.takeIf { it.isNotEmpty() }?.let { parts ->
            paras += Para("Yapısı", parts.joinToString(" + ") { term(it) } + " sözcüklerinden oluşur.")
        }

        // 4) Kaynak sözcüğün anlamı ve kökü
        if (deep != null) {
            if (!isName) {
                val dd = deep.definitions.map { Txt(it, true) }
                if (dd.isNotEmpty()) joinMeanings(dd)?.let { paras += Para("Kaynak sözcüğün anlamı", it) }
            }
            val deepSentence = if (deep.chain.isNotEmpty()) chainSentence(term(deep.term), deep.term.langName, deep.chain) else null
            val rootText = deep.root?.let {
                // Sami kökleri genelde üç harflidir: "Üç harfli Arapça kök: ك ت ب (k t b)."
                val count = mapOf(2 to "İki", 3 to "Üç", 4 to "Dört")[it.letters.trim().split(Regex("\\s+")).size]
                (count?.let { n -> "$n harfli " } ?: "") + "${deep.term.langName} kök: ${it.letters}" + (it.translit?.let { t -> " ($t)" } ?: "") + "."
            }
            val text = listOfNotNull(deepSentence, rootText).joinToString(" ")
            if (text.isNotBlank()) paras += Para("Kökü", text)
        }

        // 5) Vikipedi'nin Türkçe anlatımı: isimlerde ya da Vikisözlük'te hiç kayıt yoksa
        val wikiLead = r.wiki?.paragraphs?.firstOrNull()
        if (wikiLead != null && (isName || !g.hasWikt)) {
            paras += Para(if (g.hasWikt) "Vikipedi'ye göre" else null, wikiLead)
        }

        val timeline = buildList {
            addAll(chain)
            deep?.let { d ->
                addAll(d.chain)
                d.root?.let { add(Mention(d.term.langCode, d.term.langName, it.letters, it.translit, null, null, isRoot = true)) }
            }
        }
        val badgeLang = origin ?: wikiOrigin?.takeIf { it.isNotBlank() }
        return Story(
            word = g.word,
            badge = badgeLang?.let { "$it kökenli" },
            paras = paras,
            timeline = timeline,
            endLanguage = g.language,
            related = g.block?.related.orEmpty(),
            pending = pending,
        )
    }
}
