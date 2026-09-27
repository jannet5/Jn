package com.cepgozcu.app.ui.common

import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.Locale
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

/** Human-friendly byte sizes ("1,2 GB"), used everywhere disk/memory sizes are shown. */
fun formatBytes(bytes: Long): String {
    if (bytes == 0L) return "0 B"
    val absBytes = abs(bytes).toDouble()
    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")
    val digitGroups = (log10(absBytes) / log10(1024.0)).toInt().coerceIn(0, units.size - 1)
    val value = bytes / 1024.0.pow(digitGroups)
    return String.format(Locale.getDefault(), "%.1f %s", value, units[digitGroups])
}

/** Signed variant for deltas ("+1,2 GB" / "-340 MB"). */
fun formatBytesDelta(bytes: Long): String {
    val sign = if (bytes > 0) "+" else if (bytes < 0) "-" else ""
    return sign + formatBytes(abs(bytes))
}

fun formatBytesPerHour(bytesPerHour: Double): String = formatBytes(bytesPerHour.roundToInt().toLong())

fun formatPercent(value: Double): String = "${value.roundToInt()}%"

/** Parses an ISO-8601 instant (as the .NET agent sends via DateTimeOffset) into a short relative-time string. Falls back to the raw text if parsing fails. */
fun formatRelativeTime(isoInstant: String, now: Instant = Instant.now()): String {
    val instant = runCatching { Instant.parse(isoInstant) }.getOrElse { return isoInstant }
    val duration = Duration.between(instant, now)
    val seconds = duration.seconds
    return when {
        seconds < 5 -> "az önce"
        seconds < 60 -> "$seconds sn önce"
        seconds < 3600 -> "${seconds / 60} dk önce"
        seconds < 86_400 -> "${seconds / 3600} sa önce"
        seconds < 30 * 86_400 -> "${seconds / 86_400} gün önce"
        else -> "${seconds / (30 * 86_400)} ay önce"
    }
}

fun isoToEpochMillisOrNull(isoInstant: String): Long? =
    try {
        Instant.parse(isoInstant).toEpochMilli()
    } catch (e: DateTimeParseException) {
        null
    }
