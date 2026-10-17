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
        val fetched = coroutineScope {
            val tr = variants.map { v -> async { v to client.fetch(Source.WIKT_TR.host, v) } }
            val en = variants.map { v -> async { v to client.fetch(Source.WIKT_EN.host, v) } }
            val wp = async { q to client.fetch(Source.WIKI_TR.host, q) }
            Triple(tr.map { it.await() }, en.map { it.await() }, wp.await())
        }
        val allFetches = (fetched.first + fetched.second + listOf(fetched.third)).map { it.second }
        val failures = allFetches.filterIsInstance<Fetch.Failed>()

        val result = withContext(Dispatchers.Default) {
            val trW = firstOk(fetched.first)?.let { (title, ok) ->
                runCatching { WiktionaryParser.parse(ok.html, Source.WIKT_TR, title, ok.pageUrl) }.getOrNull()
            }
            val enW = firstOk(fetched.second)?.let { (title, ok) ->
                runCatching { WiktionaryParser.parse(ok.html, Source.WIKT_EN, title, ok.pageUrl) }.getOrNull()
            }
            val wp = (fetched.third.second as? Fetch.Ok)?.let { ok ->
                runCatching { WikipediaParser.parse(ok.html, fetched.third.first, ok.pageUrl) }.getOrNull()
            }
            LookupResult(q, trW, enW, wp)
        }

        if (!result.isEmpty) {
            cache[q.lowercase(trLocale)] = result
            return Outcome.Found(result)
        }
        // Hiçbir şey bulunamadı: hata yüzünden mi, gerçekten yok mu?
        return if (failures.isNotEmpty()) Outcome.Failed(q, failures.any { it.code == 429 }) else Outcome.NotFound(q)
    }

    private fun firstOk(list: List<Pair<String, Fetch>>): Pair<String, Fetch.Ok>? =
        list.firstNotNullOfOrNull { (title, f) -> (f as? Fetch.Ok)?.let { title to it } }

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
