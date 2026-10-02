package app.nokta.c

data class Item(val id: Long, val text: String, val done: Boolean = false)

/** Saf Kotlin liste mantığı: Android'e bağımlı değil, JVM'de test edilir. */
class NoktaList(initial: List<Item> = emptyList()) {
    private var list: List<Item> = initial
    private var nextId: Long = (initial.maxOfOrNull { it.id } ?: 0L) + 1L
    private val undoStack = ArrayList<List<Item>>()

    val items: List<Item> get() = list
    val doneCount: Int get() = list.count { it.done }
    val openCount: Int get() = list.size - doneCount
    val canUndo: Boolean get() = undoStack.isNotEmpty()

    private fun commit(next: List<Item>) {
        undoStack.add(list)
        if (undoStack.size > MAX_UNDO) undoStack.removeAt(0)
        list = next
    }

    /** Yeni öğe en üste eklenir. Boş/boşluk metin reddedilir. */
    fun add(raw: String): Item? {
        val t = raw.trim().take(MAX_TEXT)
        if (t.isEmpty()) return null
        val it = Item(nextId++, t)
        commit(listOf(it) + list)
        return it
    }

    fun toggle(id: Long): Boolean {
        val i = list.indexOfFirst { it.id == id }
        if (i < 0) return false
        commit(list.toMutableList().also { l -> l[i] = l[i].copy(done = !l[i].done) })
        return true
    }

    fun delete(id: Long): Boolean {
        if (list.none { it.id == id }) return false
        commit(list.filter { it.id != id })
        return true
    }

    /** Tamamlananların hepsini siler; kaç öğe silindiğini döner. */
    fun clearDone(): Int {
        val n = doneCount
        if (n == 0) return 0
        commit(list.filter { !it.done })
        return n
    }

    fun move(from: Int, to: Int): Boolean {
        if (from == to || from !in list.indices || to !in list.indices) return false
        val l = list.toMutableList()
        l.add(to, l.removeAt(from))
        commit(l)
        return true
    }

    fun undo(): Boolean {
        if (undoStack.isEmpty()) return false
        list = undoStack.removeAt(undoStack.size - 1)
        return true
    }

    /** Dışa aktarma metni: "[ ] açık" / "[x] tamam", satır başına bir öğe. */
    fun exportText(): String = list.joinToString("\n") { (if (it.done) "[x] " else "[ ] ") + it.text.replace('\n', ' ') }

    companion object {
        const val MAX_UNDO = 50
        const val MAX_TEXT = 2000
    }
}
