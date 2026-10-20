package com.tekpanel.app.data.apps

import android.content.Context
import android.util.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Loads the *real* launcher icon of an installed app (so WhatsApp looks like WhatsApp and
 * WhatsApp Business like WhatsApp Business) once, off the main thread, and keeps it in a
 * small memory cache so scrolling never re-decodes anything.
 */
class AppIconLoader(private val context: Context) {

    private val cache = object : LruCache<String, ImageBitmap>(CACHE_BYTES) {
        override fun sizeOf(key: String, value: ImageBitmap): Int = value.width * value.height * 4
    }

    fun cached(packageName: String): ImageBitmap? = cache.get(packageName)

    /** Seeds the cache (screenshot tests use it so the previews show icons without installing apps). */
    fun prime(packageName: String, icon: ImageBitmap) {
        cache.put(packageName, icon)
    }

    suspend fun load(packageName: String): ImageBitmap? {
        cache.get(packageName)?.let { return it }
        return withContext(Dispatchers.IO) {
            runCatching {
                context.packageManager.getApplicationIcon(packageName)
                    .toBitmap(ICON_PX, ICON_PX)
                    .asImageBitmap()
            }.getOrNull()?.also { cache.put(packageName, it) }
        }
    }

    private companion object {
        const val ICON_PX = 144
        const val CACHE_BYTES = 6 * 1024 * 1024
    }
}
