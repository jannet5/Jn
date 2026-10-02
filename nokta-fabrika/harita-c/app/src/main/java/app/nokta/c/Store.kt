package app.nokta.c

import android.content.Context
import java.io.File
import java.util.concurrent.Executors

/** Süreç içi tek örnek: model + dosya + dinleyiciler. Aktivite ve balon aynı modeli görür. */
object Store {
    private var file: File? = null
    private val io by lazy { Executors.newSingleThreadExecutor() }
    var model = NoktaList()
        private set
    private val listeners = ArrayList<() -> Unit>()

    @Synchronized
    fun init(ctx: Context) {
        if (file != null) return
        val f = File(ctx.applicationContext.filesDir, "nokta.json")
        file = f
        val text = try { if (f.exists()) f.readText() else null } catch (e: Exception) { null }
        val items = NoktaJson.decode(text)
        if (items == null && text != null && text.isNotBlank()) {
            // bozuk dosyayı silme, yedeğini bırak
            try { f.copyTo(File(f.parentFile, "nokta.json.bozuk"), overwrite = true) } catch (_: Exception) {}
        }
        model = NoktaList(items ?: emptyList())
    }

    fun listen(l: () -> Unit) { listeners.add(l) }
    fun unlisten(l: () -> Unit) { listeners.remove(l) }

    /** Model değiştikten sonra çağrılır: dinleyicilere haber ver, dosyaya yaz. */
    fun changed() {
        val json = NoktaJson.encode(model.items)
        val f = file
        if (f != null) io.execute {
            try {
                val tmp = File(f.parentFile, "nokta.json.tmp")
                tmp.writeText(json)
                if (!tmp.renameTo(f)) { f.writeText(json); tmp.delete() }
            } catch (_: Exception) {}
        }
        for (l in ArrayList(listeners)) l()
    }

    fun add(text: String): Boolean { val ok = model.add(text) != null; if (ok) changed(); return ok }
    fun toggle(id: Long) { if (model.toggle(id)) changed() }
    fun delete(id: Long): Boolean { val ok = model.delete(id); if (ok) changed(); return ok }
    fun clearDone(): Int { val n = model.clearDone(); if (n > 0) changed(); return n }
    fun move(from: Int, to: Int) { if (model.move(from, to)) changed() }
    fun undo(): Boolean { val ok = model.undo(); if (ok) changed(); return ok }
}
