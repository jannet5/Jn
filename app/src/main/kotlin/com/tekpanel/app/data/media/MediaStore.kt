package com.tekpanel.app.data.media

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

/**
 * App-private storage for the profile photos and pictures that arrive inside notifications.
 * Files are content-addressed (SHA-256 of the bytes), so a contact's avatar that rides along
 * with a hundred messages is stored once. Nothing here is world-readable or backed up.
 */
class MediaStore(context: Context) {

    private val dir = File(context.filesDir, "media").apply { mkdirs() }

    /** @return absolute path of the stored file; same bytes always map to the same path. */
    suspend fun save(bytes: ByteArray, extension: String = "webp"): String = withContext(Dispatchers.IO) {
        val name = sha256(bytes).take(40) + "." + extension
        val target = File(dir, name)
        if (!target.exists()) {
            val tmp = File(dir, "$name.tmp")
            tmp.writeBytes(bytes)
            if (!tmp.renameTo(target)) tmp.delete()
        }
        target.absolutePath
    }

    /** Removes every stored file that is not in [referencedPaths]; returns how many were removed. */
    suspend fun deleteUnreferenced(referencedPaths: Set<String>): Int = withContext(Dispatchers.IO) {
        dir.listFiles().orEmpty()
            .filter { it.absolutePath !in referencedPaths }
            .count { it.delete() }
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO) { dir.listFiles().orEmpty().forEach { it.delete() } }

    private fun sha256(bytes: ByteArray): String =
        MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
}
