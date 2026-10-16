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
        val text = clean(raw)
        if (text.isEmpty()) return null
        val item = Item(nextId(), text, done = false, position = 0)
        list = (listOf(item) + list).reindexed()
        return list.first()
    }

    /** Metni degistirir; yeri ve durumu korunur. Bos metin kabul edilmez. */
    fun edit(id: Long, raw: String): Boolean {
        val text = clean(raw)
        val i = list.indexOfFirst { it.id == id }
        if (i < 0 || text.isEmpty()) return false
        list = list.toMutableList().also { it[i] = it[i].copy(text = text) }
        return true
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

    /** Yedek metni: "[ ] oge" / "[x] oge"; cok satirli ogenin devam satirlari 4 bosluk iceride. */
    fun export(): String = list.joinToString("\n") { (if (it.done) "[x] " else "[ ] ") + it.text.replace("\n", "\n" + CONT) }

    /** [export] ciktisini (ya da duz satirlari) sonuna ekler. @return eklenen oge sayisi. */
    fun importText(text: String): Int {
        val parsed = ArrayList<Pair<Boolean, StringBuilder>>()
        var boxed = false
        for (line in text.lines()) {
            // Kutulu bir ogenin ardindaki 4 bosluklu satir o ogenin devamidir (paragraf dahil).
            if (boxed && line.startsWith(CONT)) { parsed.last().second.append('\n').append(line.substring(CONT.length)); continue }
            val t = line.trim()
            if (t.isEmpty()) continue
            val m = BOX.matchEntire(t)
            boxed = m != null
            parsed += if (m != null) (m.groupValues[1].isNotBlank()) to StringBuilder(m.groupValues[2]) else false to StringBuilder(t)
        }
        var id = nextId()
        val add = parsed.mapNotNull { (done, raw) ->
            val s = clean(raw.toString())
            if (s.isEmpty()) null else Item(id++, s, done, 0)
        }
        list = (list + add).reindexed()
        return add.size
    }

    companion object {
        const val MAX_TEXT = 1000
        private const val CONT = "    "
        private val BOX = Regex("^\\[([ xX])](?:\\s+(.*))?$")
        private val SPACES = Regex("[ \\t\\u00A0]+")

        /** Satir ici bosluklar teke iner; satir sonlari kalir, art arda bos satirlar tek paragraf boslugu olur. */
        fun clean(raw: String): String {
            val out = ArrayList<String>()
            for (l in raw.replace("\r", "").split('\n')) {
                val t = l.trim().replace(SPACES, " ")
                if (t.isEmpty() && (out.isEmpty() || out.last().isEmpty())) continue
                out += t
            }
            while (out.isNotEmpty() && out.last().isEmpty()) out.removeAt(out.lastIndex)
            return out.joinToString("\n").take(MAX_TEXT).trimEnd()
        }
    }
}
