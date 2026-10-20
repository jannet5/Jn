package com.jn.koken.data

import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/** Bir aramanın sonucu */
sealed interface Outcome {
    data class Found(val result: LookupResult) : Outcome
    data class NotFound(val query: String) : Outcome
    data class Failed(val query: String, val rateLimited: Boolean) : Outcome
}

/** Üç kaynağı aynı anda sorgular ve sonuçları birleştirir. */
class Lookup(private val client: WikiClient = WikiClient()) {

    // Son 30 sorguyu hatırla: aynı sözcük tekrar aranınca ağa gitmeye gerek kalmaz
    private val cache = object : LinkedHashMap<String, LookupResult>(32, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, LookupResult>?): Boolean = size > 30
    }

    suspend fun search(raw: String): Outcome {
        val q = normalize(raw)
        if (q.isEmpty()) return Outcome.NotFound(raw.trim())
        cache[q.lowercase(trLocale)]?.let { return Outcome.Found(it) }

        val variants = titleVariants(q)

        // Her kaynakta başlık yazılışlarını SIRAYLA dene (ilki varsa diğerlerine gerek yok): az istek, az hata
        class Attempt(val title: String, val ok: Fetch.Ok?, val failed: Boolean, val rateLimited: Boolean)
        suspend fun firstExisting(host: String): Attempt {
            var failed = false
            var rate = false
            for (v in variants) {
                when (val f = client.fetch(host, v)) {
                    is Fetch.Ok -> return Attempt(v, f, failed, rate)
                    is Fetch.Failed -> { failed = true; if (f.code == 429) rate = true }
                    Fetch.Missing -> Unit
                }
            }
            return Attempt(variants.first(), null, failed, rate)
        }

        val (trA, enA) = coroutineScope {
            val a = async { firstExisting(Source.WIKT_TR.host) }
            val b = async { firstExisting(Source.WIKT_EN.host) }
            a.await() to b.await()
        }
        val (trW, enW0) = withContext(Dispatchers.Default) {
            val t = trA.ok?.let { ok -> runCatching { WiktionaryParser.parse(ok.html, Source.WIKT_TR, trA.title, ok.pageUrl) }.getOrNull() }
            val e = enA.ok?.let { ok -> runCatching { WiktionaryParser.parse(ok.html, Source.WIKT_EN, enA.title, ok.pageUrl) }.getOrNull() }
            t to e
        }
        // "電腦'e bak" gibi yönlendirme sayfalarını (örn. sadeleştirilmiş Çince) bir kez takip et
        var enW = enW0
        if (enW == null && enA.ok != null) {
            val tgt = WiktionaryParser.stubTarget(enA.ok.html)
            if (tgt != null && tgt != enA.title) {
                val f = client.fetch(Source.WIKT_EN.host, tgt) as? Fetch.Ok
                if (f != null) enW = withContext(Dispatchers.Default) {
                    runCatching { WiktionaryParser.parse(f.html, Source.WIKT_EN, tgt, f.pageUrl) }.getOrNull()
                }
            }
        }
        // Vikipedi sadece isimlerde ya da Vikisözlük'te hiç kayıt yokken gerekir: sıradan sözcükler için büyük sayfayı çekme
        val wpF: Fetch? = if (Narrator.wantsWikipedia(trW, enW)) client.fetch(Source.WIKI_TR.host, q) else null
        val wpFailed = wpF as? Fetch.Failed
        val incomplete = (trA.ok == null && trA.failed) || (enA.ok == null && enA.failed) || wpFailed != null
        val wp = (wpF as? Fetch.Ok)?.let { ok ->
            withContext(Dispatchers.Default) { runCatching { WikipediaParser.parse(ok.html, q, ok.pageUrl) }.getOrNull() }
        }
        val result = LookupResult(q, trW, enW, wp, incomplete)

        if (!result.isEmpty) {
            // Eksik sonucu önbelleğe alma: tekrar denenince tam hali gelsin
            if (!incomplete) cache[q.lowercase(trLocale)] = result
            return Outcome.Found(result)
        }
        // Hiçbir şey bulunamadı: hata yüzünden mi, gerçekten yok mu?
        return if (incomplete) Outcome.Failed(q, trA.rateLimited || enA.rateLimited || wpFailed?.code == 429) else Outcome.NotFound(q)
    }

    /** Bir atanın kendi sayfasından anlam, ataları ve kökü çıkarır; sayfa/bölüm yoksa null */
    private suspend fun fetchDeep(target: Mention): DeepInfo? {
        val title = DeepBuilder.title(target) ?: return null
        val ok = client.fetch(Source.WIKT_EN.host, title) as? Fetch.Ok ?: return null
        return withContext(Dispatchers.Default) {
            runCatching {
                WiktionaryParser.parse(ok.html, Source.WIKT_EN, title, ok.pageUrl)?.let { DeepBuilder.build(target, it) }
            }.getOrNull()
        }
    }

    /**
     * Zincirin en eski gerçek sözcüğünün kendi sayfasını çekip kaynak dildeki anlamını, atalarını ve kökünü bulur.
     * Arapça ise: fiil sayfası (kalıp/mastar/ortaç) ve kökün kendi bilgisi (anlamı, kökeni) de alınır.
     * Diğer dillerde: daha eski atalar için en çok 2 kat daha geriye gidilir. Başarısız olursa null (anlatım bunsuz da çalışır).
     */
    suspend fun deepen(r: LookupResult, language: String? = null): DeepInfo? {
        val target = Narrator.chainOf(r, language).lastOrNull { !it.langName.startsWith("Proto") && !it.isRoot } ?: return null
        var deep = fetchDeep(target) ?: return null
        val base = deep.chain.firstOrNull()
        if (base != null && base.langCode == "ar") {
            val vt = DeepBuilder.title(base)
            val vf = if (vt != null) client.fetch(Source.WIKT_EN.host, vt) as? Fetch.Ok else null
            val verb = vf?.let { f -> withContext(Dispatchers.Default) { runCatching { ArabicVerbParser.parse(f.html, base) }.getOrNull() } }
            if (verb != null) deep = deep.copy(verb = verb)
        }
        if (deep.root != null) deep = deep.copy(rootInfo = rootInfoFor(deep))
        if (deep.root == null) {
            val seen = HashSet<String>().apply { add(target.langCode + target.text) }
            repeat(2) {
                val last = deep.chain.lastOrNull { !it.langName.startsWith("Proto") && !it.isRoot } ?: return deep
                if (!seen.add(last.langCode + last.text)) return deep
                val d2 = fetchDeep(last) ?: return deep
                val fresh = d2.chain.filter { n -> deep.chain.none { it.text == n.text && it.langCode == n.langCode } }
                if (fresh.isEmpty()) return deep
                deep = deep.copy(chain = deep.chain + fresh)
            }
        }
        return deep
    }

    /** Kökün temel anlamı ve kökeni: I. bâb fiilinden (zaten elimizdeyse onu kullan, yoksa kök harflerinden sayfayı bul) */
    private suspend fun rootInfoFor(deep: DeepInfo): RootInfo? {
        val root = deep.root ?: return null
        val v = deep.verb
        if (v != null && v.form == "I") return RootInfo(v.verb, v.definitions, v.etymology?.chain.orEmpty(), v.etymology?.related.orEmpty())
        val candidates = ArabicVerbParser.formICandidates(root.letters)
        if (candidates.isEmpty()) return null
        val pages = coroutineScope { candidates.map { c -> async { client.fetch(Source.WIKT_EN.host, c) } }.map { it.await() } }
        for (f in pages) {
            val ok = f as? Fetch.Ok ?: continue
            val vi = withContext(Dispatchers.Default) { runCatching { ArabicVerbParser.parseFormI(ok.html, root.letters) }.getOrNull() } ?: continue
            return RootInfo(vi.verb, vi.definitions, vi.etymology?.chain.orEmpty(), vi.etymology?.related.orEmpty())
        }
        return null
    }

    companion object {
        private val trLocale = Locale("tr", "TR")

        fun normalize(raw: String): String =
            raw.filter { !it.isISOControl() }.replace(Regex("\\s+"), " ").trim().take(100)

        /** Vikisözlük başlıkları büyük/küçük harfe duyarlıdır: yazılan, küçük ve baş harfi büyük hallerini dene */
        fun titleVariants(q: String): List<String> {
            fun cap(s: String, l: Locale) = if (s.isEmpty()) s else s.substring(0, 1).uppercase(l) + s.substring(1)
            val out = LinkedHashSet<String>()
            out.add(q)
            out.add(q.lowercase(trLocale))
            out.add(q.lowercase(Locale.ROOT))
            out.add(cap(q.lowercase(trLocale), trLocale))
            out.add(cap(q.lowercase(Locale.ROOT), Locale.ROOT))
            return out.take(4)
        }
    }
}
