package app.nokta.a.model

const val MAX_TEXT = 500

data class Item(val id: Long, val text: String, val done: Boolean = false)

data class IndexedItem(val index: Int, val item: Item)

/** Silinen veya temizlenen öğeler; geri alma için konumlarıyla birlikte tutulur. */
data class Removal(val entries: List<IndexedItem>, val kind: Kind) {
    enum class Kind { DELETE, CLEAR_DONE }
}

/** Değişmez liste durumu. Her işlem yeni bir ListState döndürür; Android'e bağımlı değildir. */
data class ListState(val items: List<Item> = emptyList(), val nextId: Long = 1L) {

    val doneCount: Int get() = items.count { it.done }
    val pendingCount: Int get() = items.size - doneCount

    fun add(raw: String): ListState {
        val text = normalize(raw)
        if (text.isEmpty()) return this
        return copy(items = items + Item(nextId, text), nextId = nextId + 1)
    }

    fun toggle(id: Long): ListState =
        copy(items = items.map { if (it.id == id) it.copy(done = !it.done) else it })

    fun edit(id: Long, raw: String): ListState {
        val text = normalize(raw)
        if (text.isEmpty()) return this
        return copy(items = items.map { if (it.id == id) it.copy(text = text) else it })
    }

    fun remove(id: Long): Pair<ListState, Removal>? {
        val idx = items.indexOfFirst { it.id == id }
        if (idx < 0) return null
        val removal = Removal(listOf(IndexedItem(idx, items[idx])), Removal.Kind.DELETE)
        return copy(items = items.filterIndexed { i, _ -> i != idx }) to removal
    }

    /** Alınmış (üstü çizili) tüm öğeleri kaldırır. Hiç yoksa null. */
    fun clearDone(): Pair<ListState, Removal>? {
        val entries = items.mapIndexedNotNull { i, it -> if (it.done) IndexedItem(i, it) else null }
        if (entries.isEmpty()) return null
        return copy(items = items.filter { !it.done }) to Removal(entries, Removal.Kind.CLEAR_DONE)
    }

    /** Öğeleri özgün konumlarına (sınıra sıkıştırarak) geri koyar; aynı id zaten varsa atlar. */
    fun restore(removal: Removal): ListState {
        val result = items.toMutableList()
        for (e in removal.entries.sortedBy { it.index }) {
            if (result.any { it.id == e.item.id }) continue
            result.add(e.index.coerceIn(0, result.size), e.item)
        }
        return copy(items = result)
    }

    fun move(from: Int, to: Int): ListState {
        if (from == to || from !in items.indices || to !in items.indices) return this
        val m = items.toMutableList()
        m.add(to, m.removeAt(from))
        return copy(items = m)
    }

    companion object {
        fun normalize(raw: String): String =
            raw.replace(Regex("\\s+"), " ").trim().take(MAX_TEXT)
    }
}
