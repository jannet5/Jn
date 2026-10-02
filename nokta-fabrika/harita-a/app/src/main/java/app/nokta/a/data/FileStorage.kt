package app.nokta.a.data

import app.nokta.a.model.Storage
import java.io.File
import java.util.concurrent.Executors

/** filesDir/list.json; yazma arka plan tek iş parçacığında, geçici dosya + rename (yarım yazı yok). */
class FileStorage(private val dir: File) : Storage {
    private val file = File(dir, "list.json")
    private val io = Executors.newSingleThreadExecutor { r -> Thread(r, "nokta-io").apply { isDaemon = false } }

    override fun load(): String? = try { if (file.exists()) file.readText() else null } catch (e: Exception) { null }

    override fun save(json: String) {
        io.execute {
            try {
                val tmp = File(dir, "list.json.tmp")
                tmp.writeText(json)
                if (!tmp.renameTo(file)) { file.writeText(json); tmp.delete() }
            } catch (_: Exception) { }
        }
    }

    override fun backupCorrupt(json: String) {
        try { File(dir, "list.corrupt.${System.currentTimeMillis()}.json").writeText(json) } catch (_: Exception) { }
    }
}
