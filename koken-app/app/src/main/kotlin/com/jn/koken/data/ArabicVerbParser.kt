package com.jn.koken.data

import org.jsoup.Jsoup

/**
 * Arapça fiil sayfasından kalıp numarası (I–X), anlamlar, mastar (fiil adı), etken ortaç ve fiilin kendi kökeni.
 * Başlık satırında kalıp bağlantısı olmayan (fiil olmayan) kayıtlar için null döner.
 */
object ArabicVerbParser {
    fun parse(html: String, verb: Mention): VerbInfo? {
        val doc = Jsoup.parse(html)
        val want = DeepBuilder.norm(verb.text)
        val heads = doc.select(".headword-line strong[lang=ar]")
        // Aynı ünsüzlerle yazılan birden çok fiil olabilir (kataba / kattaba): önce harekeleriyle birebir eşleşen
        val hw = heads.firstOrNull { cleanText(it.text()) == verb.text }
            ?: heads.firstOrNull { DeepBuilder.norm(it.text()) == want }
            ?: return null
        val line = hw.closest(".headword-line") ?: return null
        val form = line.selectFirst("a[href*=Form_]")?.text()?.trim()?.takeIf { Regex("^[IVX]+$").matches(it) } ?: return null
        val hwTranslit = line.selectFirst("span[lang=ar-Latn]")?.let { cleanText(it.text()) }

        // Mastar: başlık satırındaki "verbal noun" etiketinden sonraki Arapça yazı
        var verbalNoun: Mention? = null
        val label = line.select("i").firstOrNull { it.text().trim().equals("verbal noun", ignoreCase = true) }
        val vn = label?.nextElementSibling()
        if (vn != null && vn.attr("lang") == "ar") {
            val tl = generateSequence(vn.nextElementSibling()) { it.nextElementSibling() }.take(3)
                .firstOrNull { it.attr("lang") == "ar-Latn" }?.let { cleanText(it.text()) }
            verbalNoun = Mention("ar", verb.langName, cleanText(vn.text()), tl, null, null)
        }

        // Anlamlar: başlık paragrafının hemen ardındaki numaralı liste
        val p = line.closest("p")
        val ol = p?.nextElementSibling()?.takeIf { it.tagName() == "ol" }
        val defs = ol?.children()?.toList()?.filter { it.tagName() == "li" }?.take(3)?.mapNotNull { li ->
            val c = li.clone()
            c.select("ul, ol, dl, style, link, sup, .h-usage-example").remove()
            cleanText(c.text()).takeIf { it.isNotBlank() }
        }.orEmpty()

        // Etken ortaç: çekim tablosundaki "active participle" satırı
        val row = generateSequence(p?.nextElementSibling()) { it.nextElementSibling() }.take(6)
            .flatMap { it.select("tr").asSequence() }
            .firstOrNull { tr -> tr.selectFirst("th")?.ownText()?.trim()?.lowercase()?.startsWith("active participle") == true }
        val apText = row?.selectFirst("td span[lang=ar]")?.let { cleanText(it.text()) }
        val apTr = row?.selectFirst("td span[lang=ar-Latn]")?.let { cleanText(it.text()) }
        val ap = apText?.takeIf { it.isNotBlank() }?.let { Mention("ar", verb.langName, it, apTr, null, null) }

        // Fiilin kendi kökeni (Proto-Sami…) ve akrabaları: sayfanın genel ayrıştırmasından, başlık sözcüğü eşleşen grup
        val etymology = runCatching {
            WiktionaryParser.parse(html, Source.WIKT_EN, verb.text, "")
                ?.languages?.firstOrNull { it.language == verb.langName }
                ?.groups?.firstOrNull { g -> g.entries.any { it.headword == cleanText(hw.text()) } }?.etymology
        }.getOrNull()

        return VerbInfo(verb.copy(translit = verb.translit ?: hwTranslit), form, defs, verbalNoun, ap, etymology)
    }

    /** Kök harfleri verilen sayfada ilk I. bâb fiilini bulur (kökün temel anlamı için); kök kutusu uyuşmazsa null */
    fun parseFormI(html: String, rootLetters: String): VerbInfo? {
        val doc = Jsoup.parse(html)
        val rootOk = doc.select("table").any { t ->
            t.selectFirst("th")?.text()?.startsWith("Root") == true &&
                cleanText(t.selectFirst("span[lang=ar]")?.text().orEmpty()) == rootLetters
        }
        if (!rootOk) return null
        val hw = doc.select(".headword-line strong[lang=ar]").firstOrNull {
            it.closest(".headword-line")?.selectFirst("a[href*=Form_]")?.text()?.trim() == "I"
        } ?: return null
        return parse(html, Mention("ar", "Arapça", cleanText(hw.text()), null, null, null))
    }

    /** Kök harflerinden I. bâb fiilinin olası sayfa başlıkları (sağlam, ortası/sonu zayıf, hemzeli kökler) */
    fun formICandidates(rootLetters: String): List<String> {
        val l = rootLetters.trim().split(Regex("\\s+"))
        if (l.size != 3) return emptyList()
        fun h(c: String) = if (c == "ء") "أ" else c
        val (a, b, c) = l
        val out = LinkedHashSet<String>()
        out += h(a) + b + c
        if (b == "و" || b == "ي") out += h(a) + "ا" + c
        if (c == "و" || c == "ي") { out += h(a) + b + "ى"; out += h(a) + b + "ا" }
        return out.take(3).toList()
    }
}
