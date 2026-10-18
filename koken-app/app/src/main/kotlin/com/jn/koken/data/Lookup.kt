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

        val (trA, enA, wpF) = coroutineScope {
            val a = async { firstExisting(Source.WIKT_TR.host) }
            val b = async { firstExisting(Source.WIKT_EN.host) }
            val c = async { client.fetch(Source.WIKI_TR.host, q) }
            Triple(a.await(), b.await(), c.await())
        }
        val wpFailed = wpF as? Fetch.Failed
        val incomplete = (trA.ok == null && trA.failed) || (enA.ok == null && enA.failed) || wpFailed != null

        val result = withContext(Dispatchers.Default) {
            val trW = trA.ok?.let { ok -> runCatching { WiktionaryParser.parse(ok.html, Source.WIKT_TR, trA.title, ok.pageUrl) }.getOrNull() }
            val enW = enA.ok?.let { ok -> runCatching { WiktionaryParser.parse(ok.html, Source.WIKT_EN, enA.title, ok.pageUrl) }.getOrNull() }
            val wp = (wpF as? Fetch.Ok)?.let { ok -> runCatching { WikipediaParser.parse(ok.html, q, ok.pageUrl) }.getOrNull() }
            LookupResult(q, trW, enW, wp, incomplete)
        }

        if (!result.isEmpty) {
            // Eksik sonucu önbelleğe alma: tekrar denenince tam hali gelsin
            if (!incomplete) cache[q.lowercase(trLocale)] = result
            return Outcome.Found(result)
        }
        // Hiçbir şey bulunamadı: hata yüzünden mi, gerçekten yok mu?
        return if (incomplete) Outcome.Failed(q, trA.rateLimited || enA.rateLimited || wpFailed?.code == 429) else Outcome.NotFound(q)
    }

    /**
     * Zincirin en eski gerçek sözcüğünün (örn. Arapça مُمْتَاز) kendi sayfasını da çekip
     * kaynak dildeki anlamını, atalarını ve kökünü bulur. Başarısız olursa null (anlatım bunsuz da çalışır).
     */
    suspend fun deepen(r: LookupResult): DeepInfo? {
        val target = Narrator.chainOf(r).lastOrNull { !it.langName.startsWith("Proto") && !it.isRoot } ?: return null
        val title = DeepBuilder.title(target) ?: return null
        val ok = client.fetch(Source.WIKT_EN.host, title) as? Fetch.Ok ?: return null
        return withContext(Dispatchers.Default) {
            runCatching {
                WiktionaryParser.parse(ok.html, Source.WIKT_EN, title, ok.pageUrl)?.let { DeepBuilder.build(target, it) }
            }.getOrNull()
        }
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
