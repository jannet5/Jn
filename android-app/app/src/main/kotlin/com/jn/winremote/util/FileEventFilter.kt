package com.jn.winremote.util

import com.jn.winremote.protocol.FileEventData

/**
 * Client-side filter state for the "Dosya Etkinliği" screen: time range,
 * event type, minimum size, path prefix. This filters what's already been
 * received/queried; the equivalent fields also map 1:1 onto
 * `ClientMessage.ListFileEvents` when querying the server directly.
 */
data class FileEventFilter(
    val fromTsSeconds: Long? = null,
    val toTsSeconds: Long? = null,
    val op: String? = null,
    val minSizeBytes: Long = 0,
    val pathPrefix: String? = null,
) {
    fun matches(event: FileEventData): Boolean {
        if (fromTsSeconds != null && event.ts < fromTsSeconds) return false
        if (toTsSeconds != null && event.ts > toTsSeconds) return false
        if (op != null && !event.op.equals(op, ignoreCase = true)) return false
        if (event.sizeBytes < minSizeBytes) return false
        val prefix = pathPrefix
        if (!prefix.isNullOrBlank() && !event.path.startsWith(prefix, ignoreCase = true)) return false
        return true
    }
}

fun List<FileEventData>.applyFilter(filter: FileEventFilter): List<FileEventData> =
    filter { filter.matches(it) }
