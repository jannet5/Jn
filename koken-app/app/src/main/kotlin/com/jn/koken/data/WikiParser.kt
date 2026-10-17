package com.jn.koken.data

import java.util.Locale
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

/** Metin temizliği: görünmez yön işaretleri, çift boşluk, noktalama öncesi boşluk */
internal fun cleanText(s: String): String =
    s.replace("‎", "").replace("‏", "").replace(" ", " ")
        .replace(Regex("\\s+"), " ")
        .replace(Regex("\\s+([,.;:!?)\\]”])"), "\$1")
        .replace(Regex("([(\\[“])\\s+"), "\$1")
        .trim()

/** Köken paragrafını (İngilizce ya da Türkçe) dil etiketli zincire çevirir. */
internal object EtymologyExtractor {

    // Paragrafı oluşturan parçalar
    private sealed interface Tok {
        data class Text(val s: String) : Tok
        data class Label(val s: String) : Tok
        data class Term(val code: String, val s: String) : Tok
        data class Tr(val s: String) : Tok
        data class Gloss(val s: String) : Tok
    }

    private class Raw(val code: String, val label: String?, val text: String) {
        var translit: String? = null
        var gloss: String? = null
        var before: String = ""
        var after: String = ""
    }

    private enum class Kind { CHAIN, RELATED, PART, OTHER }

    /** Sayfadaki şablon hatası metinleri köken sayılmasın */
    private fun isErrorParagraph(p: Element): Boolean {
        if (p.select(".scribunto-error, .error").isNotEmpty()) return true
        val t = p.text()
        return t.contains("Lua hatası") || t.contains("Lua error") || t.contains("Script error")
    }

    private val scriptLang = Regex("^[a-z]{2,3}(-[a-z]+)*-[A-Z][a-z]{3}$")

    private val relatedEn = listOf("cognate", "compare", "doublet", "related", "akin", "cf.", "equivalent", "synonym", "displace", "see also", "mirror", "variant of", "alternative")
    private val relatedTr = listOf("eş kökenli", "karşılaştır", "akraba", "çift", "ilgili", "kardeş", "eş anlamlı", "bkz")
    private val chainEn = listOf("from", "borrow", "inherit", "deriv", "via", "calque", "clipping", "short for", "abbreviation", "back-formation", "blend", "contraction", "ellipsis", "learned", "reborrow")
    private val chainTr = listOf("sözcüğünden", "kelimesinden", "devral", "alıntı", "ödünç", "türe", "o da", "gelmekte", "gelir", "köken")

    private val trLocale = Locale("tr", "TR")

    // Terim ile okunuşu/anlamı arasındaki boşluk, virgül, parantez ve tırnaklar "yapıştırıcı" sayılır
    private fun StringBuilder.isGlue(): Boolean = all { it.isWhitespace() || it in ",;:()“”\"'‘’" }

    // Cümle sınırı: nokta + boşluk + büyük harf ("lit. “x”" gibi kısaltmaları cümle sonu saymaz)
    private val sentenceBoundary = Regex("[.!?][\"”)]?\\s+[A-ZÇĞİÖŞÜ]")

    private fun isTranslit(el: Element, lang: String): Boolean =
        el.hasClass("mention-tr") || (scriptLang.matches(lang) && (el.hasClass("tr") || el.hasClass("Latn")))

    private fun tokenize(p: Element): List<Tok> {
        val out = ArrayList<Tok>()
        fun walk(n: Node) {
            when (n) {
                // Görünmez yön işaretleri (LRM/RLM) boşluk sayılsın ki etiket-terim-okunuş bitişikliği bozulmasın
                is TextNode -> n.wholeText.replace(Regex("[‎‏​ ]"), " ").let {
                    if (it.isNotEmpty()) out.add(Tok.Text(it))
                }
                is Element -> {
                    val tag = n.tagName()
                    if (tag == "style" || tag == "link" || tag == "meta" || tag == "script" || tag == "sup") return
                    val lang = n.attr("lang")
                    when {
                        n.hasClass("etyl") -> out.add(Tok.Label(cleanText(n.text())))
                        lang.isNotEmpty() && isTranslit(n, lang) -> out.add(Tok.Tr(cleanText(n.text())))
                        lang.isNotEmpty() -> out.add(Tok.Term(lang, cleanText(n.text())))
                        n.hasClass("mention-gloss") -> out.add(Tok.Gloss(cleanText(n.text())))
                        n.hasClass("mention-gloss-paren") || n.hasClass("mention-gloss-double-quote") ||
                            n.hasClass("mention-gloss-quote") -> Unit
                        tag == "a" && n.hasClass("extiw") -> out.add(Tok.Label(cleanText(n.text())))
                        else -> n.childNodes().forEach { walk(it) }
                    }
                }
            }
        }
        p.childNodes().forEach { walk(it) }
        return out
    }

    private fun assemble(toks: List<Tok>): List<Raw> {
        val out = ArrayList<Raw>()
        var between = StringBuilder()
        var label: String? = null
        var labelAt = 0
        var cur: Raw? = null
        for (t in toks) {
            when (t) {
                is Tok.Text -> between.append(t.s)
                is Tok.Label -> { label = t.s; labelAt = between.length }
                is Tok.Term -> {
                    val text = between.toString()
                    cur?.after = text
                    val attached = label != null && text.substring(minOf(labelAt, text.length)).isBlank()
                    val r = Raw(t.code, if (attached) label else null, t.s)
                    r.before = text
                    out.add(r)
                    cur = r
                    between = StringBuilder()
                    label = null
                }
                is Tok.Tr -> cur?.let { if (it.translit == null && between.isGlue()) it.translit = t.s }
                is Tok.Gloss -> cur?.let { if (it.gloss == null && between.isGlue()) it.gloss = t.s }
            }
        }
        cur?.after = between.toString()
        return out
    }

    private fun relationOf(text: String, tr: Boolean): Relation? {
        val t = text.lowercase(if (tr) trLocale else Locale.ROOT)
        return when {
            tr && ("alıntı" in t || "ödünç" in t || "aktar" in t) -> Relation.BORROWED
            tr && ("devral" in t || "miras" in t) -> Relation.INHERITED
            tr && "türe" in t -> Relation.DERIVED
            !tr && ("borrow" in t || "loan" in t) -> Relation.BORROWED
            !tr && "inherit" in t -> Relation.INHERITED
            !tr && "deriv" in t -> Relation.DERIVED
            else -> null
        }
    }

    /**
     * @param paragraphs köken bölümündeki <p> öğeleri
     * @param tr kaynak Türkçe Vikisözlük mü (bağlaçlar terimden sonra gelir)
     * @param names sayfa boyunca öğrenilen dil kodu → dil adı eşlemesi
     */
    fun extract(allParagraphs: List<Element>, tr: Boolean, names: MutableMap<String, String>): EtymologyBlock? {
        val paragraphs = allParagraphs.filterNot { isErrorParagraph(it) }
        val texts = paragraphs.map { elementText(it) }.filter { it.isNotBlank() }
        if (texts.isEmpty()) return null

        val chain = ArrayList<Mention>()
        val related = ArrayList<Mention>()
        val parts = ArrayList<Mention>()

        // Etiketleri önce topla ki aynı koddaki sonraki terimler adı bilsin
        val allRaw = paragraphs.map { assemble(tokenize(it)) }
        allRaw.flatten().forEach { r -> r.label?.let { names[r.code] = it } }

        // Zincir sadece ilk paragraftan; akrabalar ve parçalar tüm paragraflardan
        allRaw.forEachIndexed { pi, raws ->
            var state = Kind.CHAIN
            for (r in raws) {
                val b = r.before.lowercase(if (tr) trLocale else Locale.ROOT)
                val rel = if (tr) relatedTr else relatedEn
                val ch = if (tr) chainTr else chainEn
                // Yeni cümle başladıysa ve "from/kökenli" gibi bir bağlaç yoksa bu terim zincire ait değildir
                val boundary = sentenceBoundary.findAll(r.before).lastOrNull()
                val tail = if (boundary != null) b.substring(boundary.range.last.coerceAtMost(b.length - 1)).trim() else b
                val kind = when {
                    "+" in b || "+" in r.after -> Kind.PART
                    rel.any { it in b } -> Kind.RELATED
                    boundary == null && ch.any { it in b } -> Kind.CHAIN
                    boundary != null && ch.any { tail.startsWith(it) } -> Kind.CHAIN
                    boundary != null -> Kind.OTHER
                    else -> state
                }
                if (kind != Kind.PART) state = kind
                val rawName = r.label ?: names[r.code] ?: LangNames.codeToEnglish(r.code) ?: r.code
                val langName = LangNames.tr(rawName) // Türkçe ad zaten Türkçedir; İngilizce olanlar çevrilir
                val relation = relationOf(if (tr) r.after else r.before, tr)
                val m = Mention(r.code, langName, r.text, r.translit, r.gloss, relation)
                when (kind) {
                    Kind.CHAIN -> if (pi == 0 && chain.none { it.text == m.text && it.langCode == m.langCode }) chain.add(m)
                    Kind.RELATED -> related.add(m)
                    Kind.PART -> if (pi == 0) parts.add(m)
                    Kind.OTHER -> Unit
                }
            }
        }
        return EtymologyBlock(
            text = texts.take(3).joinToString("\n\n"),
            chain = chain.take(8),
            related = related.take(12),
            parts = parts.take(6),
        )
    }

    /** Köken paragrafının düz metni */
    fun elementText(el: Element): String {
        val c = el.clone()
        c.select("style, link, meta, script, sup, .mw-editsection, .noprint, .etytree, ul.etymonid, .mw-empty-elt").remove()
        return cleanText(c.text())
    }
}

/** Vikisözlük (Türkçe ve İngilizce) sayfa gövdesi → diller, köken, anlamlar */
object WiktionaryParser {
    private val etyHeading = Regex("^(etymology|köken)(\\s*\\d+)?$", RegexOption.IGNORE_CASE)

    private val pos = mapOf(
        "noun" to "İsim", "proper noun" to "Özel ad", "verb" to "Fiil", "adjective" to "Sıfat", "adverb" to "Zarf",
        "pronoun" to "Zamir", "preposition" to "Edat", "postposition" to "Edat", "conjunction" to "Bağlaç",
        "interjection" to "Ünlem", "numeral" to "Sayı", "particle" to "Edat", "determiner" to "Belirteç",
        "article" to "Tanımlık", "prefix" to "Ön ek", "suffix" to "Son ek", "infix" to "Ara ek", "phrase" to "Deyim",
        "proverb" to "Atasözü", "idiom" to "Deyim", "contraction" to "Kısaltma", "abbreviation" to "Kısaltma",
        "initialism" to "Kısaltma", "acronym" to "Kısaltma", "symbol" to "Simge", "letter" to "Harf",
        "participle" to "Ortaç", "name" to "Özel ad", "classifier" to "Sayı sıfatı", "root" to "Kök",
        "ad" to "İsim", "özel ad" to "Özel ad", "fiil" to "Fiil", "sıfat" to "Sıfat", "zarf" to "Zarf",
        "zamir" to "Zamir", "edat" to "Edat", "bağlaç" to "Bağlaç", "ünlem" to "Ünlem", "sayı" to "Sayı",
        "ön ek" to "Ön ek", "son ek" to "Son ek", "deyim" to "Deyim", "atasözü" to "Atasözü",
        "kısaltma" to "Kısaltma", "harf" to "Harf", "simge" to "Simge", "ortaç" to "Ortaç", "sözcük" to "Sözcük",
    )

    private enum class Mode { NONE, ETY, POS, SKIP }

    private class Group(val heading: String) {
        val paragraphs = ArrayList<Element>()
        val entries = ArrayList<PosEntryBuilder>()
    }

    private class PosEntryBuilder(val pos: String) {
        var headword: String? = null
        val defs = ArrayList<String>()
    }

    private class Lang(val name: String) {
        val groups = ArrayList<Group>()
    }

    private val insideSkip = setOf("table", "li", "dl", "ul", "ol", "blockquote")

    fun parse(html: String, source: Source, title: String, url: String): WiktionaryResult? {
        val doc = Jsoup.parse(html)
        val root = doc.selectFirst(".mw-parser-output") ?: doc.body()
        val isTr = source == Source.WIKT_TR
        val langs = ArrayList<Lang>()
        var lang: Lang? = null
        var group: Group? = null
        var entry: PosEntryBuilder? = null
        var mode = Mode.NONE

        for (el in root.select("div.mw-heading, p, ol")) {
            if (el.hasClass("mw-heading")) {
                val h = el.selectFirst("h2, h3, h4, h5, h6") ?: continue
                val level = h.tagName().substring(1).toInt()
                val text = cleanText(h.text())
                if (level == 2) {
                    lang = Lang(if (isTr) text else LangNames.tr(text))
                    langs.add(lang)
                    group = null; entry = null; mode = Mode.NONE
                    continue
                }
                val l = lang ?: continue
                val key = text.lowercase(Locale.ROOT)
                when {
                    etyHeading.matches(text) -> {
                        group = Group(text).also { l.groups.add(it) }
                        entry = null; mode = Mode.ETY
                    }
                    pos.containsKey(key) -> {
                        val g = group ?: Group("").also { l.groups.add(it); group = it }
                        entry = PosEntryBuilder(pos.getValue(key)).also { g.entries.add(it) }
                        mode = Mode.POS
                    }
                    else -> mode = Mode.SKIP
                }
                continue
            }
            if (lang == null) continue
            // tablo, liste, açılır kutu içindekileri atla
            if (el.parents().any { it.tagName() in insideSkip || it.hasClass("NavFrame") || it.hasClass("etytree") }) continue
            when (mode) {
                Mode.ETY -> if (el.tagName() == "p") group?.paragraphs?.add(el)
                Mode.POS -> {
                    val e = entry ?: continue
                    if (el.tagName() == "p") {
                        if (e.headword == null) e.headword = el.selectFirst(".headword")?.let { cleanText(it.text()) }?.takeIf { it.isNotBlank() }
                    } else if (el.tagName() == "ol" && e.defs.isEmpty()) {
                        for (li in el.children().toList().filter { it.tagName() == "li" }.take(5)) {
                            val c = li.clone()
                            c.select("ul, ol, dl, .h-usage-example, .HQToggle, .citation-whole, style, link, sup, .mw-editsection").remove()
                            val t = cleanText(c.text())
                            if (t.isNotBlank()) e.defs.add(t)
                        }
                    }
                }
                else -> Unit
            }
        }

        val names = HashMap<String, String>()
        val sections = langs.mapNotNull { l ->
            val groups = l.groups.mapNotNull { g ->
                val ety = EtymologyExtractor.extract(g.paragraphs, isTr, names)
                val entries = g.entries.filter { it.defs.isNotEmpty() || it.headword != null }
                    .map { PosEntry(it.pos, it.headword, it.defs) }
                if (ety == null && entries.isEmpty()) null else SenseGroup(multiHeading(g.heading), ety, entries)
            }
            if (groups.isEmpty()) null else LanguageSection(l.name, groups)
        }
        if (sections.isEmpty()) return null
        return WiktionaryResult(source, title, url, sections)
    }

    /** "Etymology 2" → "Köken 2"; tek köken başlığı boş bırakılır */
    private fun multiHeading(h: String): String {
        val num = Regex("\\d+").find(h)?.value ?: return ""
        return "Köken $num"
    }
}

/** Türkçe Vikipedi: kısa özet paragrafları ve (isim maddelerindeki) bilgi kutusu */
object WikipediaParser {
    fun parse(html: String, title: String, url: String): WikipediaResult? {
        val doc = Jsoup.parse(html)
        val root = doc.selectFirst(".mw-parser-output") ?: doc.body()

        val facts = ArrayList<Pair<String, String>>()
        for (row in root.select("table.infobox tr")) {
            val k = row.selectFirst("th")?.let { cleanText(it.text()) } ?: continue
            val v = row.selectFirst("td")?.let { cleanText(it.text()) } ?: continue
            if (k.isNotBlank() && v.isNotBlank() && v.length < 160 && facts.none { it.first == k }) facts.add(k to v)
            if (facts.size >= 6) break
        }

        val paragraphs = ArrayList<String>()
        for (p in root.select("p")) {
            if (p.parents().any { it.tagName() in setOf("table", "li", "dl", "blockquote") || it.hasClass("hatnote") || it.hasClass("dablink") }) continue
            val c = p.clone()
            c.select("sup, style, link, .mw-editsection, .noprint").remove()
            val t = cleanText(c.text())
            if (t.length < 25) continue
            if (t.startsWith("Vikisözlük'te") || t.contains("ile ilgili tanım bulabilirsiniz")) continue
            paragraphs.add(if (t.length > 600) t.take(600).trimEnd() + "…" else t)
            if (paragraphs.size >= 2) break
        }
        if (facts.isEmpty() && paragraphs.isEmpty()) return null
        return WikipediaResult(title, url, facts, paragraphs)
    }
}
