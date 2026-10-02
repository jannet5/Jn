package app.nokta.list.core

import app.nokta.list.data.Item

/** Geri alinabilir tek islem. Tek yuvali: yeni silme eskisinin geri alinmasini kapatir. */
sealed class Undo {
    data class Deleted(val item: Item, val index: Int) : Undo()
    data class ClearedDone(val removed: List<Pair<Int, Item>>) : Undo()
}

/** Ekleme sonrasi giris alani davranisi (saf): odak ve klavye korunur, alan temizlenir, liste basa kayar. */
data class AddFlow(val clearField: Boolean, val keepFocus: Boolean, val scrollToTop: Boolean) {
    companion object {
        fun after(added: Boolean) =
            // Bos metin eklenmedi: alan oldugu gibi, ama odak yine de kaybolmaz.
            if (added) AddFlow(clearField = true, keepFocus = true, scrollToTop = true)
            else AddFlow(clearField = false, keepFocus = true, scrollToTop = false)
    }
}

/**
 * Listenin saf mantigi: Android bagimliligi yok, birim testle dogrulanir.
 * Siralama listedeki konumdur; her degisiklikte position yeniden yazilir.
 */
class ListModel(initial: List<Item> = emptyList()) {
    private var list: List<Item> = initial.sortedBy { it.position }.reindexed()
    var undo: Undo? = null
        private set

    val items: List<Item> get() = list
    val remaining: Int get() = list.count { !it.done }
    val doneCount: Int get() = list.count { it.done }

    private fun List<Item>.reindexed() = mapIndexed { i, it -> if (it.position == i) it else it.copy(position = i) }
    private fun nextId() = (list.maxOfOrNull { it.id } ?: 0L) + 1

    /** Bos/bosluk metni eklenmez. Yeni oge en uste girer (hizli ekleme sonucu hemen gorunur). */
    fun add(raw: String): Item? {
        val text = raw.trim().replace(Regex("\\s+"), " ")
        if (text.isEmpty()) return null
        val item = Item(nextId(), text.take(MAX_TEXT), done = false, position = 0)
        list = (listOf(item) + list).reindexed()
        return list.first()
    }

    fun toggle(id: Long): Boolean {
        val i = list.indexOfFirst { it.id == id }
        if (i < 0) return false
        list = list.toMutableList().also { it[i] = it[i].copy(done = !it[i].done) }
        return true
    }

    fun delete(id: Long): Boolean {
        val i = list.indexOfFirst { it.id == id }
        if (i < 0) return false
        undo = Undo.Deleted(list[i], i)
        list = list.filterIndexed { idx, _ -> idx != i }.reindexed()
        return true
    }

    /** Alinan (ustu cizili) tum ogeleri kaldirir; geri alinabilir. */
    fun clearDone(): Int {
        val removed = list.mapIndexedNotNull { i, it -> if (it.done) i to it else null }
        if (removed.isEmpty()) return 0
        undo = Undo.ClearedDone(removed)
        list = list.filter { !it.done }.reindexed()
        return removed.size
    }

    fun move(from: Int, to: Int): Boolean {
        if (from == to || from !in list.indices || to !in list.indices) return false
        val m = list.toMutableList()
        m.add(to, m.removeAt(from))
        list = m.reindexed()
        return true
    }

    /** @return geri alindiysa true. */
    fun undoLast(): Boolean {
        val u = undo ?: return false
        val m = list.toMutableList()
        when (u) {
            is Undo.Deleted -> m.add(u.index.coerceIn(0, m.size), u.item)
            // Artan indeksle ekleme: her ogenin eski konumu kendinden onceki eklemelerle tutarli kalir.
            is Undo.ClearedDone -> u.removed.sortedBy { it.first }.forEach { (idx, it) -> m.add(idx.coerceIn(0, m.size), it) }
        }
        list = m.reindexed()
        undo = null
        return true
    }

    fun dismissUndo() { undo = null }

    /** Yedek metni: "[ ] oge" / "[x] oge". Paylasilabilir, insan okur. */
    fun export(): String = list.joinToString("\n") { (if (it.done) "[x] " else "[ ] ") + it.text }

    /** [export] ciktisini (ya da duz satirlari) sonuna ekler. @return eklenen oge sayisi. */
    fun importText(text: String): Int {
        var n = 0
        val parsed = text.lines().mapNotNull { line ->
            val t = line.trim()
            if (t.isEmpty()) return@mapNotNull null
            val m = BOX.matchEntire(t)
            if (m != null) (m.groupValues[1].isNotBlank()) to m.groupValues[2] else false to t
        }
        var id = nextId()
        val add = parsed.mapNotNull { (done, raw) ->
            val s = raw.trim()
            if (s.isEmpty()) null else Item(id++, s.take(MAX_TEXT), done, 0).also { n++ }
        }
        list = (list + add).reindexed()
        return n
    }

    companion object {
        const val MAX_TEXT = 200
        private val BOX = Regex("^\\[([ xX])](?:\\s+(.*))?$")
    }
}
