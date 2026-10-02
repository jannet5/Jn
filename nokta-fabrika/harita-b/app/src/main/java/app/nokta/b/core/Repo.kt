package app.nokta.b.core

import android.content.Context
import android.os.Handler
import android.os.Looper
import app.nokta.b.data.Item
import app.nokta.b.data.ItemStore
import app.nokta.b.data.RoomItemStore
import java.util.concurrent.Executors

/**
 * Tek gercek kaynak. Hem Activity hem balon paneli bunu dinler; kayit tek is parcaciginda sirayla yapilir.
 * Model ana is parcaciginda degisir; kayit icin her degisiklikte degismez goruntu gonderilir.
 */
class Repo(private val store: ItemStore, private val main: Handler = Handler(Looper.getMainLooper())) {
    private val io = Executors.newSingleThreadExecutor { Thread(it, "nokta-io") }
    private val listeners = LinkedHashSet<() -> Unit>()
    private var model = ListModel()
    var loaded = false
        private set

    val items: List<Item> get() = model.items
    val remaining: Int get() = model.remaining
    val doneCount: Int get() = model.doneCount
    val undo: Undo? get() = model.undo

    fun addListener(l: () -> Unit) { listeners += l }
    fun removeListener(l: () -> Unit) { listeners -= l }

    fun load() {
        if (loaded) return
        io.execute {
            val items = store.load()
            main.post {
                if (!loaded) { model = ListModel(items); loaded = true; fire() }
            }
        }
    }

    private fun fire() { listeners.toList().forEach { it() } }
    private fun persist() { val snap = model.items; io.execute { store.save(snap) } }
    private inline fun <T> change(block: ListModel.() -> T): T {
        val r = model.block(); persist(); fire(); return r
    }

    fun add(text: String): Boolean = change { add(text) } != null
    fun toggle(id: Long) = change { toggle(id) }
    fun delete(id: Long) = change { delete(id) }
    fun clearDone() = change { clearDone() }
    fun undoLast() = change { undoLast() }
    fun dismissUndo() { model.dismissUndo(); fire() }
    fun importText(text: String) = change { importText(text) }
    fun export() = model.export()

    /** Surukleme sirasinda goruntu adaptorde; birakinca bir kez islenir. */
    fun move(from: Int, to: Int) = change { move(from, to) }

    companion object {
        @Volatile private var inst: Repo? = null
        fun get(context: Context): Repo = inst ?: synchronized(this) {
            inst ?: Repo(RoomItemStore(context.applicationContext)).also { inst = it; it.load() }
        }
    }
}
