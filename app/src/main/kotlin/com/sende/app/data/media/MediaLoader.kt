package com.sende.app.data.media

import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

/** Decodes stored pictures off the main thread, downsampled to what the screen needs, with a small cache. */
class MediaLoader {

    private val cache = object : LruCache<String, ImageBitmap>(CACHE_BYTES) {
        override fun sizeOf(key: String, value: ImageBitmap): Int = value.width * value.height * 4
    }

    fun cached(path: String, maxPx: Int): ImageBitmap? = cache.get("$path@$maxPx")

    suspend fun load(path: String, maxPx: Int): ImageBitmap? {
        val key = "$path@$maxPx"
        cache.get(key)?.let { return it }
        return withContext(Dispatchers.IO) {
            runCatching {
                val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(path, bounds)
                var sample = 1
                while (max(bounds.outWidth, bounds.outHeight) / (sample * 2) >= maxPx) sample *= 2
                val options = BitmapFactory.Options().apply { inSampleSize = sample }
                BitmapFactory.decodeFile(path, options)?.asImageBitmap()
            }.getOrNull()?.also { cache.put(key, it) }
        }
    }

    private companion object {
        const val CACHE_BYTES = 24 * 1024 * 1024
    }
}
