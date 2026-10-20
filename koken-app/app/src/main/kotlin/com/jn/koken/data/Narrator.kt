package com.jn.koken.data

import java.util.Locale

/** Kısa giriş paragrafı: isteğe bağlı küçük başlık + düz Türkçe metin (⟦kalın⟧ işaretli) */
data class Para(val label: String?, val text: String)

/** Oluşumun bir basamağı: yukarıdan aşağı, kökten sözcüğe. Metinlerde ⟦kalın⟧ işareti olabilir. */
data class Step(
    val label: String,                 // KÖK, FİİL, ATA, SÖZCÜK, BUGÜN
    val language: String,
    val form: String,
    val tiles: List<String> = emptyList(), // kök harfleri (م · ي · ز)
    val translit: String? = null,
    val meaning: String? = null,       // Türkçe anlam
    val how: String? = null,           // bu basamağın öncekinden nasıl oluştuğu
    val note: String? = null,          // ek bilgi (kökün kökeni, akrabalar)
    val parts: String? = null,         // "Bileşenleri: A + B"
    val isWord: Boolean = false,       // sözcüğün bugünkü hâli (vurgulanır)
)

/** Sonuç ekranının içeriği */
data class Story(
    val word: String,
    val badge: String?,
    val paras: List<Para>,             // kısa giriş: anlam (+ isimlerde Vikipedi anlatımı)
    val narration: String?,            // yazı olarak tam anlatım (isteğe bağlı açılır)
    val steps: List<Step>,             // yukarıdan aşağı oluşum
    /** en yakın → en eski (kök dahil) */
    val timeline: List<Mention>,
    val endLanguage: String,
    val related: List<Mention>,
    val languages: List<String>,       // bu yazılışın bulunduğu diller (seçici için)
    val language: String,              // anlatılan dil
    val pending: Boolean,
)

/**
 * Toplanan verilerden (anlam, köken zinciri, kök, fiil kalıbı) hem adım adım oluşumu (yukarıdan aşağı) hem de
 * düz Türkçe bir anlatım üretir. Kural tabanlıdır: yapay zekâ ya da anahtar yok; her cümle kaynak veriden gelir.
 */
object Narrator {
    private val trLocale = Locale("tr", "TR")

    private class Txt(val s: String, val en: Boolean)

    private class G(
        val word: String, val language: String, val pos: String?, val meanings: List<Txt>,
        val block: EtymologyBlock?, val chain: List<Mention>, val hasWikt: Boolean,
    )

    /** "م ي ز" → "م + ي + ز" */
    private fun plus(letters: String) = letters.trim().split(Regex("\\s+")).joinToString(" + ")

    /**
     * Uzun İngilizce tanımı kısaltır: parantez/köşeli parantezleri at; "to mark on a surface, to write, to inscribe" gibi
     * uzun açıklama + kısa eş anlamlılar varsa sadece kısa eş anlamlıları ("to write, to inscribe") tut.
     */
    internal fun shorten(d: String): String {
        var t = d
        repeat(2) { t = t.replace(Regex("\\s*\\([^()]*\\)"), "") }
        t = t.replace(Regex("\\s*\\[[^\\]]*\\]"), "").replace(Regex("\\s+"), " ").trim()
        val parts = t.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        if (parts.size > 1) {
            val short = parts.filter { it.split(" ").size <= 3 }
            if (short.isNotEmpty() && short.size < parts.size) return short.take(3).joinToString(", ")
        }
        return if (t.length <= 55) t else parts.firstOrNull().orEmpty().take(80)
    }

    private fun deepTxts(list: List<String>): List<Txt> = list.map { Txt(shorten(it), true) }.filter { it.s.isNotBlank() }.distinctBy { it.s }

    /** Bu yazılışın bulunduğu diller (köken ya da anlamı olanlar), öncelik sırasıyla */
    fun languagesOf(r: LookupResult): List<String> =
        (r.wiktTr?.languages.orEmpty() + r.wiktEn?.languages.orEmpty())
            .filter { l -> l.groups.any { it.etymology != null || it.entries.isNotEmpty() } }
            .map { it.language }.distinct().sortedBy { LookupResult.languageRank(it) }

    /** Anlatılacak sözcüğün köken zinciri (en yakın → en eski) */
    fun chainOf(r: LookupResult, language: String? = null): List<Mention> = gather(r, language).chain

    private fun gather(r: LookupResult, language: String? = null): G {
        val tr = r.wiktTr?.languages.orEmpty()
        val en = r.wiktEn?.languages.orEmpty()
        val word = r.wiktTr?.title ?: r.wiktEn?.title ?: r.wiki?.title ?: r.query
        val langs = (tr.map { it.language } + en.map { it.language }).distinct().sortedBy { LookupResult.languageRank(it) }
        fun etyOf(s: LanguageSection?): EtymologyBlock? = s?.groups?.firstNotNullOfOrNull { it.etymology }
        fun hasEty(l: String) = etyOf(tr.find { it.language == l }) != null || etyOf(en.find { it.language == l }) != null
        // Seçilen dil; yoksa köken bilgisi olan en öncelikli dil (Türkçe, İngilizce, yaygın diller…); yoksa ilk dil
        val lang = language?.takeIf { it in langs } ?: langs.firstOrNull { hasEty(it) } ?: langs.firstOrNull() ?: "Türkçe"
        val trSec = tr.find { it.language == lang }
        val enSec = en.find { it.language == lang }
        val trBlock = etyOf(trSec)
        val enBlock = etyOf(enSec)
        // Zinciri en uzun olan kaynak (eşitlikte Türkçe kaynak)
        val block = listOfNotNull(trBlock, enBlock).maxByOrNull { it.chain.size }
        var chain = block?.chain.orEmpty()
        // İki kaynak aynı zinciri veriyorsa: Türkçe adları koru, eksik bilgiyi (devralındı/alıntı, harekeli yazım) İngilizce kaynaktan tamamla
        if (trBlock != null && enBlock != null && trBlock.chain.isNotEmpty() &&
            trBlock.chain.map { it.langCode } == enBlock.chain.map { it.langCode }
        ) {
            chain = trBlock.chain.mapIndexed { i, m ->
                val e = enBlock.chain[i]
                m.copy(
                    text = if (DeepBuilder.norm(e.text) == DeepBuilder.norm(m.text) && e.text.length > m.text.length) e.text else m.text,
                    relation = m.relation ?: e.relation,
                    translit = m.translit ?: e.translit,
                    gloss = m.gloss ?: e.gloss,
                    glossEn = if (m.gloss == null && e.gloss != null) true else m.glossEn,
                    how = m.how ?: e.how,
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
    fun englishStrings(r: LookupResult, deep: DeepInfo?, language: String? = null): List<String> {
        val g = gather(r, language)
        val out = LinkedHashSet<String>()
        g.meanings.filter { it.en }.forEach { out += it.s }
        (g.chain + g.block?.parts.orEmpty()).forEach { m -> if (m.glossEn) m.gloss?.let { out += it } }
        deep?.let { d ->
            deepTxts(d.definitions).forEach { out += it.s }
            d.chain.forEach { m -> if (m.glossEn) m.gloss?.let { out += it } }
            if (d.term.glossEn) d.term.gloss?.let { out += it }
            d.verb?.let { v ->
                deepTxts(v.definitions).forEach { out += it.s }
                if (v.verb.glossEn) v.verb.gloss?.let { out += it }
            }
            d.rootInfo?.let { ri ->
                deepTxts(ri.definitions).forEach { out += it.s }
                (ri.origin + ri.cognates).forEach { m -> if (m.glossEn) m.gloss?.let { out += it } }
            }
        }
        return out.toList()
    }

    /** Vikipedi sadece isimler (Özel ad) ya da Vikisözlük'te hiç kayıt yokken gerekir */
    fun wantsWikipedia(tr: WiktionaryResult?, en: WiktionaryResult?): Boolean {
        if (tr == null && en == null) return true
        return listOfNotNull(tr, en).any { r ->
            r.languages.any { l ->
                LookupResult.languageRank(l.language) < 2 && l.groups.any { g -> g.entries.any { it.pos == "Özel ad" } }
            }
        }
    }

    private fun genderOf(meanings: List<Txt>): String? {
        val t = meanings.joinToString(" ") { it.s }.lowercase(trLocale)
        return when {
            "erkek" in t || ("male" in t && "female" !in t) -> "erkek ismi"
            "kadın" in t || "female" in t -> "kadın ismi"
            else -> null
        }
    }

    /**
     * @param translations İngilizce → Türkçe çeviriler
     * @param translating çeviri sürüyor mu (sürüyorsa çevrilmemiş metinler gösterilmez)
     * @param language anlatılacak dil (aynı yazılış birden çok dilde varsa); null = en öncelikli dil
     */
    fun compose(
        r: LookupResult, deep: DeepInfo?, translations: Map<String, String>, translating: Boolean, language: String? = null,
    ): Story {
        val g = gather(r, language)
        var pending = false

        // İngilizce metni Türkçesiyle değiştirir; çeviri beklenirken null, başarısızsa İngilizce kalır
        fun tx(s: String, en: Boolean): String? {
            if (!en) return s
            translations[s]?.let { return it }
            if (translating) { pending = true; return null }
            return s
        }
        fun gloss(m: Mention): String? = m.gloss?.let { tx(it, m.glossEn) }
        fun b(s: String) = "⟦$s⟧"
        // "⟦كِتَاب⟧ (kitāb, “kitap”)"
        fun term(m: Mention, withGloss: Boolean = true): String {
            val inside = listOfNotNull(m.translit, if (withGloss) gloss(m)?.let { "“$it”" } else null).joinToString(", ")
            return b(m.text) + if (inside.isNotEmpty()) " ($inside)" else ""
        }
        fun clean(s: String) = s.trim().trimEnd('.', ':', ' ')
        fun soft(s: String): String =
            if (s.length <= 40 && s.drop(1).none { it.isUpperCase() }) s.replaceFirstChar { it.lowercase(trLocale) } else s
        /** Çevrilmiş anlam listesi; çeviri beklenirken null */
        fun meanings(list: List<Txt>): List<String>? {
            val out = ArrayList<String>()
            for (t in list) {
                val x = tx(t.s, t.en) ?: return null
                // Çeviri aynı parçayı iki kez verebilir ("üstün olmak, üstün olmak"): tekrarları at
                val c = clean(x).split(Regex("\\s*[,;]\\s*")).filter { it.isNotBlank() }
                    .distinctBy { it.lowercase(trLocale) }.joinToString(", ")
                if (c.isNotEmpty()) out += soft(c)
            }
            return out.distinctBy { it.lowercase(trLocale) }
        }
        fun quoted(list: List<Txt>, max: Int): String? =
            meanings(list)?.take(max)?.takeIf { it.isNotEmpty() }?.joinToString("; ")?.let { "“$it”" }
        fun plain(list: List<Txt>, max: Int): String? =
            meanings(list)?.take(max)?.takeIf { it.isNotEmpty() }?.joinToString("; ")

        val isName = g.pos == "Özel ad"
        val nameKind = if (isName) (genderOf(g.meanings) ?: "isim") else null
        val origin = g.block?.originLanguage
        val paras = ArrayList<Para>()

        // ---- 1) Ne demek?
        val deepDefs = deepTxts(deep?.definitions.orEmpty())
        // Sözcüğün kendi anlamı çevrilince sözcüğün kendisi çıkıyorsa ("book" → "kitap") bilgi vermez: at
        val ownMs: List<String>? =
            if (isName || g.meanings.isEmpty()) emptyList()
            else meanings(g.meanings)?.filter { !it.equals(g.word, ignoreCase = true) }
        val ms: List<String>? = when {
            // isim: adın anlamı kaynak sözcüğün anlamıdır; yoksa Vikipedi'nin "Anlam" alanı
            isName -> if (deepDefs.isNotEmpty()) meanings(deepDefs)
            else r.wiki?.facts?.firstOrNull { it.first == "Anlam" }?.second?.let { listOf(it) } ?: emptyList()
            ownMs == null -> null                // çeviri bekleniyor
            ownMs.isNotEmpty() -> ownMs
            else -> meanings(deepDefs)           // kendi anlamı yok ya da anlamsız: kaynak sözcüğün anlamı
        }?.let { l -> if (isName) l else l.filter { !it.equals(g.word, ignoreCase = true) } }
        val head = nameKind?.let { "${b(g.word)}, bir $it." }
        val p1: String? = if (ms == null || ms.isEmpty()) head else {
            val m1 = ms[0]
            val sentence = when {
                m1.length <= 70 -> (if (head == null) "${b(g.word)}, " else "") + b(if (head != null) m1.replaceFirstChar { it.uppercase(trLocale) } else m1) + " demek."
                else -> (if (head == null) "${b(g.word)} şu" else "Şu") + " anlama geliyor: $m1."
            }
            val extra = ms.drop(1).take(2).takeIf { it.isNotEmpty() }?.let { " Başka anlamları da var: ${it.joinToString("; ")}." }.orEmpty()
            listOfNotNull(head, sentence).joinToString(" ") + extra
        }
        p1?.let { paras += Para(null, it) }

        // ---- 2) Köken zinciri (en yakın → en eski) ve hedef (derin bilginin sözcüğü)
        val chain = g.chain
        val targetIdx = if (deep != null) chain.indexOfLast { !it.langName.startsWith("Proto") && !it.isRoot } else -1
        val target = if (targetIdx >= 0) chain[targetIdx] else null
        val used = if (target != null) chain.take(targetIdx + 1) else chain
        val newer = if (target != null) chain.take(targetIdx).reversed() else emptyList() // hedeften sözcüğe doğru
        val finalRel = chain.firstOrNull()?.relation
        val root = deep?.root
        val base = deep?.chain?.firstOrNull()

        // Aynı dilin eski evresi (Orta İngilizce → İngilizce, Osmanlı Türkçesi → Türkçe): devralma sayılır
        fun lineage(prev: String?): Boolean = prev != null && prev != g.language && prev.contains(g.language)

        // ================= Yazı olarak anlatım (eskiden yeniye) =================
        val sentences = ArrayList<String>()

        fun passages(items: List<Mention>, startLang: String?, startIndex: Int): List<String> {
            val out = ArrayList<String>()
            val connectors = listOf("Sonra", "Ardından", "Daha sonra", "Derken")
            var prev = startLang
            var ci = startIndex
            var i = 0
            while (i < items.size) {
                val lang = items[i].langName
                var j = i
                while (j + 1 < items.size && items[j + 1].langName == lang) j++
                val group = items.subList(i, j + 1)
                var s = when {
                    lang == prev -> "Orada ${term(group[0])} olmuş"
                    lang.startsWith("Proto") -> "${connectors[ci++ % connectors.size]} ${TrSuffix.locative(lang)} ${term(group[0])} biçimine dönüşmüş"
                    else -> "${connectors[ci++ % connectors.size]} ${TrSuffix.dative(lang)} ${term(group[0])} olarak geçmiş"
                }
                if (group.size > 1) s += ", ondan " + group.drop(1).joinToString(", ondan ") { term(it) } + " olmuş"
                out += "$s."
                prev = lang
                i = j + 1
            }
            return out
        }

        fun finalClause(prevLang: String?): String = when {
            g.language == prevLang -> "${TrSuffix.locative(g.language)} de ${b(g.word)} olmuş."
            finalRel == Relation.INHERITED || (finalRel == null && lineage(prevLang)) ->
                "${TrSuffix.locative(g.language)} ise ${b(g.word)} biçimini almış."
            finalRel == Relation.BORROWED -> "Sonunda ${TrSuffix.dative(g.language)} ${b(g.word)} olarak girmiş."
            else -> "Sonunda ${TrSuffix.dative(g.language)} ${b(g.word)} olarak geçmiş."
        }

        if (chain.isEmpty() && origin != null) {
            sentences += "${TrSuffix.ablative(origin)} gelmiş."
        } else if (deep != null && target != null && root != null) {
            // Kök harfli (Sami) dil: harfler → kalıp → fiil → sözcük
            val lang = deep.term.langName
            sentences += "${TrSuffix.ablative(lang)} gelmiş."
            sentences += (if (lang == "Arapça") "Arapçada sözcükler üç harfli köklerden türetilir; bu sözcüğün kök harfleri " else "Kök harfleri ") +
                b(plus(root.letters)) + (root.translit?.let { " (${plus(it)})" } ?: "") + "."
            val tDefs = quoted(deepDefs, 3)
            val v = deep.verb
            if (base != null && v != null) {
                val vDefs = quoted(deepTxts(v.definitions), 2)
                val formText = v.form?.let { Derivation.formName(it) }
                sentences += (if (formText != null) "Bu köke $formText kalıbı uygulanınca" else "Bu kökten") +
                    " ${term(base.copy(translit = base.translit ?: v.verb.translit), withGloss = false)} fiili " +
                    (if (formText != null) "oluşmuş" else "türemiş") + (vDefs?.let { ": $it" } ?: "") + "."
                val how = base.how
                sentences += if (how != null) "Bu fiilin $how olan ${term(target, false)}" + (tDefs?.let { " ise $it demek" } ?: "") + "."
                else "Bu fiilden ${term(target, false)} türemiş" + (tDefs?.let { ": $it" } ?: "") + "."
                val vn = v.verbalNoun
                if (vn != null && base.how != "mastarı (fiil adı)" && vn.text != target.text) sentences += "Aynı fiilin mastarı ise ${term(vn, false)}."
            } else if (base != null && base.langName == lang) {
                // Aynı dilden bir ata (örn. حَمِيد): köke bağlı türev
                sentences += "Bu kökten ${term(base)} türemiş."
                sentences += "${term(target, false)} ise onun ${base.how ?: "türevi"}" + (tDefs?.let { ": $it" } ?: "") + "."
            } else if (base != null) {
                // Başka bir dilden gelen ata (örn. Proto-Sinaitik 𐩱𐩡𐩽): köke değil, o dildeki sözcüğe bağlanır
                sentences += "${term(target, false)} ise ${base.langName} ${term(base)} sözcüğünden gelmiş" + (tDefs?.let { ": $it" } ?: "") + "."
            } else {
                sentences += "Bu harflerden ${term(target, false)} olmuş" + (tDefs?.let { ": $it" } ?: "") + "."
            }
            deep.rootInfo?.let { ri ->
                val rm = plain(deepTxts(ri.definitions), 2)
                if (rm != null) sentences += "Kökün temel anlamı: $rm."
            }
            sentences += passages(newer, target.langName, 0)
            sentences += finalClause(newer.lastOrNull()?.langName ?: target.langName)
        } else if (chain.isNotEmpty()) {
            // Kök harfi yoksa: zinciri eskiden yeniye anlat (aynı dildeki basamaklar birlikte)
            val items = (if (deep != null && target != null) used else chain).reversed() // eski → yeni
            val deepOlder = if (deep != null && target != null) deep.chain.reversed() else emptyList()
            val all = deepOlder + items
            val groups = ArrayList<MutableList<Mention>>()
            for (m in all) {
                if (groups.isNotEmpty() && groups.last().first().langName == m.langName) groups.last().add(m) else groups.add(mutableListOf(m))
            }
            val first = groups.first()
            val l0 = first.first().langName
            sentences += if (first.size == 1) "${TrSuffix.locative(l0)} ${term(first[0])} diye bir sözcük varmış."
            else "${TrSuffix.locative(l0)} ${term(first[0])} sözcüğünden " +
                first.drop(1).mapIndexed { i, m -> (if (i == 0) "" else "ondan da ") + term(m) }.joinToString(", ") + " türemiş."
            sentences += passages(groups.drop(1).flatten(), l0, 0)
            sentences += finalClause(all.last().langName)
        }
        val partsOwner = g.block?.partsOwner ?: -1
        val partsMentions = g.block?.parts.orEmpty()
        if (partsMentions.isNotEmpty()) {
            val ownerName = if (partsOwner in chain.indices) "${chain[partsOwner].langName} ${b(chain[partsOwner].text)}" else b(g.word)
            val names = partsMentions.map { term(it) }
            val joined = if (names.size == 1) names[0] else names.dropLast(1).joinToString(", ") + " ile " + names.last()
            sentences += "$ownerName sözcüğü ise $joined sözcüklerinin birleşmesinden oluşmuş."
        }
        val narration = sentences.joinToString(" ").ifBlank { null }

        // ================= Adım adım oluşum: yukarıdan aşağı, kökten sözcüğe =================
        val steps = ArrayList<Step>()
        val fullChain = (if (target != null) used else chain) + deep?.chain.orEmpty() // en yakın → en eski
        val ordered = fullChain.reversed()                                               // en eski → en yakın
        val partsText = partsMentions.takeIf { it.isNotEmpty() }?.joinToString(" + ") { term(it) }?.let { "Bu sözcük şunların birleşimi: $it" }
        val ownerItem = if (partsOwner in chain.indices) chain[partsOwner] else null
        val deepMeaning = plain(deepDefs, 3)
        val verbMeaning = deep?.verb?.let { plain(deepTxts(it.definitions), 2) }

        if (root != null && deep != null && target != null) {
            val ri = deep.rootInfo
            val rm = ri?.let { plain(deepTxts(it.definitions), 2) }
            val originNote = ri?.origin?.firstOrNull()?.let { "Kökün kökeni: ${it.langName} ${term(it)}." }
            val cogNote = ri?.cognates?.take(3)?.takeIf { it.isNotEmpty() }
                ?.joinToString(", ") { "${it.langName} ${b(it.text)}" }?.let { "Aynı kök başka dillerde de var: $it." }
            steps += Step(
                "KÖK", deep.term.langName, plus(root.letters), root.letters.trim().split(Regex("\\s+")), root.translit?.let { plus(it) },
                rm, "${TrSuffix.locative(deep.term.langName)} sözcükler, anlamı taşıyan bu harflerden türetilir.",
                listOfNotNull(originNote, cogNote).joinToString(" ").ifBlank { null },
            )
        }
        var prev: Mention? = null
        ordered.forEachIndexed { idx, m ->
            val p = prev
            val isBase = root != null && base != null && m === base
            val how: String? = when {
                p == null && root != null ->
                    if (isBase && deep?.verb?.form != null) "Köke ${Derivation.formName(deep.verb.form)} kalıbı uygulanınca oluştu." else "Bu kökten türedi."
                p == null -> null
                p.how != null -> (if (deep?.verb != null && p === base) "Bu fiilin " else "Bunun ") + p.how + " olarak oluştu."
                p.langName != m.langName -> when (p.relation) {
                    Relation.BORROWED -> "${TrSuffix.dative(m.langName)} alıntı olarak geçti."
                    Relation.INHERITED -> "${TrSuffix.locative(m.langName)} şu biçime dönüştü."
                    else -> if (m.langName.startsWith("Proto")) "${TrSuffix.locative(m.langName)} şu biçime dönüştü." else "${TrSuffix.dative(m.langName)} geçti."
                }
                else -> "Ondan türetildi."
            }
            val meaning = when {
                m === target -> deepMeaning ?: gloss(m)
                isBase && verbMeaning != null -> verbMeaning
                else -> gloss(m)
            }
            val label = when {
                idx == 0 && root == null -> if (m.langName.startsWith("Proto")) "KÖK" else "EN ESKİ ATA"
                isBase && deep?.verb != null -> "FİİL"
                m === target && root != null -> "SÖZCÜK"
                else -> "ATA"
            }
            steps += Step(label, m.langName, m.text, emptyList(), m.translit, meaning, how, null, if (m === ownerItem) partsText else null)
            prev = m
        }
        val last = ordered.lastOrNull()
        val finalHow = when {
            last == null -> null
            last.how != null -> "Bunun ${last.how} olarak oluştu."
            last.langName == g.language -> "Ondan türetildi."
            last.relation == Relation.BORROWED -> "${TrSuffix.dative(g.language)} alıntı olarak girdi."
            last.relation == Relation.INHERITED || lineage(last.langName) -> "${TrSuffix.locative(g.language)} bugünkü biçimini aldı."
            else -> "${TrSuffix.dative(g.language)} geçti."
        }
        if (ordered.isNotEmpty() || root != null) {
            steps += Step(
                "BUGÜN", g.language, g.word, emptyList(), null, ms?.firstOrNull(), finalHow, null,
                if (partsOwner == -1) partsText else null, isWord = true,
            )
        }

        // ---- 3) Vikipedi'nin Türkçe anlatımı: isimlerde ya da Vikisözlük'te hiç kayıt yoksa
        val wikiLead = r.wiki?.paragraphs?.firstOrNull()
        if (wikiLead != null && (isName || !g.hasWikt)) paras += Para(if (g.hasWikt) "Vikipedi'ye göre" else null, wikiLead)

        // ---- Eski çizgi (en yakın → en eski, kök dahil)
        val timeline = buildList {
            addAll(if (target != null) used else chain)
            deep?.let { d ->
                addAll(d.chain)
                d.root?.let {
                    val form = d.verb?.form?.let { f -> Derivation.formName(f) }
                    add(Mention(d.term.langCode, d.term.langName, it.letters, it.translit, null, null, isRoot = true, how = form))
                }
            }
        }
        val badgeLang = origin ?: r.wiki?.facts?.firstOrNull { it.first.contains("Köken") }?.second?.takeIf { it.isNotBlank() }
        return Story(
            word = g.word,
            badge = badgeLang?.let { "$it kökenli" },
            paras = paras,
            narration = narration,
            steps = steps,
            timeline = timeline,
            endLanguage = g.language,
            related = g.block?.related.orEmpty(),
            languages = languagesOf(r),
            language = g.language,
            pending = pending,
        )
    }
}
