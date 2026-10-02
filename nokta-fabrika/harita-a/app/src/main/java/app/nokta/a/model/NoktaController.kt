package app.nokta.a.model

interface Storage {
    fun load(): String?
    fun save(json: String)
    /** Okunamayan dosyanın kopyasını sakla (veri kaybı korkusu). */
    fun backupCorrupt(json: String)
}

/** Durum + tek seviyeli geri al + kalıcılık. Android'e bağımlı değil, JVM'de test edilir. */
class NoktaController(private val storage: Storage) {
    var state: ListState = ListState()
        private set
    var undo: Removal? = null
        private set
    var recoveredFromCorrupt = false
        private set

    init {
        val raw = storage.load()
        val decoded = ListCodec.decode(raw)
        if (decoded != null) state = decoded
        else if (!raw.isNullOrBlank()) {
            storage.backupCorrupt(raw)
            recoveredFromCorrupt = true
        }
    }

    private fun commit(s: ListState) {
        state = s
        storage.save(ListCodec.encode(s))
    }

    fun add(text: String) { if (ListState.normalize(text).isNotEmpty()) commit(state.add(text)) }
    fun toggle(id: Long) = commit(state.toggle(id))
    fun move(from: Int, to: Int) { val n = state.move(from, to); if (n !== state) commit(n) }

    fun remove(id: Long) {
        val r = state.remove(id) ?: return
        undo = r.second
        commit(r.first)
    }

    fun clearDone() {
        val r = state.clearDone() ?: return
        undo = r.second
        commit(r.first)
    }

    fun undoLast() {
        val u = undo ?: return
        undo = null
        commit(state.restore(u))
    }

    fun dismissUndo() { undo = null }

    fun importLines(lines: List<Pair<String, Boolean>>) {
        var s = state
        for ((t, d) in lines) {
            s = s.add(t)
            if (d) s = s.toggle(s.nextId - 1)
        }
        if (s !== state) commit(s)
    }
}
