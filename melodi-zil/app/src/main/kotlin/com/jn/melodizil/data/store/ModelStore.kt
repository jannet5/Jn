package com.jn.melodizil.data.store // Yerel depolama

import android.content.Context // Bağlam
import com.jn.melodizil.data.youtube.SourceException // Hatalar
import kotlinx.coroutines.currentCoroutineContext // İptal kontrolü
import kotlinx.coroutines.ensureActive // İptal kontrolü
import okhttp3.OkHttpClient // HTTP istemcisi
import okhttp3.Request // İstek
import java.io.File // Dosya
import java.io.FileOutputStream // Ekleyerek yazma
import java.io.IOException // Ağ/disk hatası
import java.security.MessageDigest // SHA-256
import java.util.concurrent.TimeUnit // Zaman aşımı

/** Ezgi modelini (RMVPE int8, ~99 MB) 3 parça halinde indirir: parça başına devam eder, SHA-256 ile doğrular, birleştirir. */
class ModelDownloader(
    private val baseUrl: String = MODEL_BASE_URL, // Parçaların bulunduğu adres
    private val parts: List<Part> = PARTS, // Parça listesi
    private val totalBytes: Long = MODEL_BYTES, // Birleşik boyut
    private val totalSha256: String = MODEL_SHA256, // Birleşik SHA-256
    private val minFreeBytes: Long = MIN_FREE_BYTES, // Gereken boş yer
    private val client: OkHttpClient = defaultClient(), // İstemci
) {
    data class Part(val name: String, val bytes: Long, val sha256: String) // Tek parça

    /** [dir] içine parçaları indirir; bitince [target] dosyasını yazar. [onProgress] 0..1. İptalde coroutine iptal istisnası fırlar, yarım parçalar devam için kalır. */
    suspend fun download(dir: File, target: File, onProgress: (Float) -> Unit) {
        dir.mkdirs() // Klasör
        val partFiles = parts.map { File(dir, it.name + ".part") } // Parça dosyaları
        val needed = parts.indices.sumOf { parts[it].bytes - (if (partFiles[it].length() <= parts[it].bytes) partFiles[it].length() else 0L) } + totalBytes // Parçalar + birleşik dosya
        if (dir.usableSpace in 1 until minOf(needed, minFreeBytes)) throw SourceException.Unavailable("Telefonda yeterli yer yok: model için en az ${minFreeBytes / 1_000_000} MB boş yer gerekir")
        var done = 0L // Biten bayt
        for ((i, p) in parts.withIndex()) {
            val f = partFiles[i] // Bu parça
            if (f.exists() && f.length() == p.bytes && sha256(f) == p.sha256) { done += p.bytes; onProgress(done.toFloat() / totalBytes); continue } // Zaten tam
            if (f.exists() && f.length() >= p.bytes) f.delete() // Bozuk/fazla: baştan
            var have = if (f.exists()) f.length() else 0L // Devam noktası
            val req = Request.Builder().url(baseUrl + p.name).apply { if (have > 0) header("Range", "bytes=$have-") }.build() // İstek
            try {
                client.newCall(req).execute().use { resp ->
                    if (!resp.isSuccessful) throw SourceException.Network("Model indirilemedi (HTTP ${resp.code})") // Sunucu hatası
                    val append = resp.code == 206 && have > 0 // Devam yanıtı mı
                    if (!append) have = 0 // Sunucu baştan verdi
                    val body = resp.body ?: throw IOException("Boş yanıt") // Gövde
                    body.byteStream().use { input ->
                        FileOutputStream(f, append).use { out ->
                            val buf = ByteArray(64 * 1024) // Tampon
                            var got = have // Bu parçada gelen
                            while (true) {
                                val n = input.read(buf) // Oku
                                if (n < 0) break // Bitti
                                currentCoroutineContext().ensureActive() // İptal
                                out.write(buf, 0, n); got += n // Yaz
                                onProgress(((done + got).toFloat() / totalBytes).coerceAtMost(0.99f)) // İlerleme
                            }
                        }
                    }
                }
            } catch (e: IOException) { throw SourceException.Network("Model indirilemedi: ${e.message ?: "bağlantı hatası"}") } // Ağ kesildi: parça kalır, tekrar denemede devam eder
            if (f.length() != p.bytes || sha256(f) != p.sha256) { f.delete(); throw SourceException.Network("Model parçası bozuk geldi, tekrar dene") } // Doğrulama
            done += p.bytes // Parça tamam
        }
        val tmp = File(dir, target.name + ".tmp") // Birleşik geçici dosya
        val md = MessageDigest.getInstance("SHA-256") // Birleşik özet
        tmp.outputStream().use { out -> for (f in partFiles) f.inputStream().use { input ->
            val buf = ByteArray(64 * 1024); while (true) { val n = input.read(buf); if (n < 0) break; md.update(buf, 0, n); out.write(buf, 0, n) } // Birleştir
        } }
        if (hex(md.digest()) != totalSha256) { tmp.delete(); partFiles.forEach { it.delete() }; throw SourceException.Network("Model doğrulanamadı, baştan indirmen gerekiyor") } // Son doğrulama
        target.parentFile?.mkdirs() // Hedef klasör
        if (target.exists()) target.delete() // Eskisi
        if (!tmp.renameTo(target)) throw SourceException.Unavailable("Model kaydedilemedi") // Yerine koy
        partFiles.forEach { it.delete() } // Parçaları sil
        onProgress(1f) // Bitti
    }

    companion object {
        const val MODEL_NAME = "rmvpe_int8.onnx" // Model dosya adı
        const val MODEL_BYTES = 98_719_246L // Birleşik boyut
        const val MODEL_SHA256 = "4faf262ad642424a112013609f0bdf75f3986409ee71ddc4fc870431a964ba7f" // Birleşik SHA-256
        const val MIN_FREE_BYTES = 250_000_000L // Parçalar + birleşik dosya için gereken boş yer
        const val MODEL_BASE_URL = "https://raw.githubusercontent.com/jannet5/Jn/3faf117f2b86cfc1da2396f3ded38d1c959d2e7a/melodi-zil/modeller/" // jsDelivr 20 MB sınırı yüzünden raw.githubusercontent
        val PARTS = listOf( // 3 parça
            Part("rmvpe_int8.part1", 34_000_000L, "9656d1fac7f64ba771640d5df1c409bc970832ed9f68a994bdd1348ee3b22d11"),
            Part("rmvpe_int8.part2", 34_000_000L, "4ec3b42f295f7a92a34447a5e054c778e8a4025405d2a46dddfd928a546a73a9"),
            Part("rmvpe_int8.part3", 30_719_246L, "a293acaf2576892f53f6f43d815855eb53dc12cb9643e560d94a1950c1f09268"),
        )
        fun defaultClient(): OkHttpClient = OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).followRedirects(true).build() // İstemci
        fun sha256(f: File): String { // Dosya özeti
            val md = MessageDigest.getInstance("SHA-256")
            f.inputStream().use { input -> val buf = ByteArray(64 * 1024); while (true) { val n = input.read(buf); if (n < 0) break; md.update(buf, 0, n) } }
            return hex(md.digest())
        }
        private fun hex(b: ByteArray): String = b.joinToString("") { "%02x".format(it) } // Onaltılık
    }
}

/** Ezgi modelinin cihazdaki yeri. Geliştirme: `adb push` ile harici dosya klasörüne konan model de kullanılır. */
object ModelStore {
    fun candidates(context: Context): List<File> = listOfNotNull( // Aranan yerler
        context.getExternalFilesDir(null)?.let { File(it, "models/${ModelDownloader.MODEL_NAME}") }, // Geliştirme (adb push)
        File(context.filesDir, "models/${ModelDownloader.MODEL_NAME}"), // Normal yer
    )

    /** Hazır model dosyası (boyutu doğruysa) ya da null. Tam SHA-256 yalnızca indirme sonunda bakılır. */
    fun existing(context: Context): File? = candidates(context).firstOrNull { it.isFile && it.length() == ModelDownloader.MODEL_BYTES }

    fun isReady(context: Context): Boolean = existing(context) != null // Hazır mı

    /** Model yoksa indirir. [onProgress] 0..1. */
    suspend fun ensure(context: Context, onProgress: (Float) -> Unit): File {
        existing(context)?.let { onProgress(1f); return it } // Hazır
        val dir = File(context.filesDir, "models") // İndirme klasörü
        val target = File(dir, ModelDownloader.MODEL_NAME) // Hedef
        ModelDownloader().download(dir, target, onProgress) // İndir
        return target
    }
}
