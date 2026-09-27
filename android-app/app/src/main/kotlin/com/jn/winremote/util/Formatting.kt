package com.jn.winremote.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

/** Byte / percentage / timestamp formatting shared by every screen. */
object Formatting {

    private val turkishLocale = Locale("tr", "TR")
    private val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")

    /** e.g. 1536 -> "1,5 KB", 0 -> "0 B". Uses binary (1024) steps, IEC-ish labels kept short for UI. */
    fun bytes(value: Long): String {
        if (value <= 0) return "0 B"
        val digitGroups = (log10(value.toDouble()) / log10(1024.0)).toInt().coerceIn(0, units.lastIndex)
        val scaled = value / 1024.0.pow(digitGroups.toDouble())
        val pattern = if (digitGroups == 0) "%.0f %s" else "%.1f %s"
        return String.format(turkishLocale, pattern, scaled, units[digitGroups])
    }

    /** Signed byte delta for growth views, e.g. +512 MB / -12 KB. */
    fun signedBytes(value: Long): String {
        val sign = if (value < 0) "-" else "+"
        return sign + bytes(abs(value))
    }

    fun percent(value: Double, fractionDigits: Int = 1): String =
        String.format(turkishLocale, "%.${fractionDigits}f%%", value)

    /** epoch seconds (as the protocol sends `ts`) -> "27 Eyl 14:03:05". */
    fun timestamp(epochSeconds: Long): String {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val formatter = DateTimeFormatter.ofPattern("d MMM HH:mm:ss", turkishLocale).withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    fun timestampFull(epochSeconds: Long): String {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm:ss", turkishLocale).withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    fun nowEpochSeconds(): Long = Instant.now().epochSecond
}
