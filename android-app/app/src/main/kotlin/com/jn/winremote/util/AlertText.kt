package com.jn.winremote.util

import com.jn.winremote.protocol.AlertData
import com.jn.winremote.protocol.AlertKind
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

/**
 * Turkish description of an alert built from its structured `context`
 * (volume / path / percentages / sizes, PROTOCOL.md §8). The agent's own
 * `message` is English log text, so it is only shown when the context lacks
 * the fields a rule needs (e.g. an older agent).
 */
object AlertText {

    fun describe(alert: AlertData): String {
        val ctx = alert.context as? JsonObject ?: return alert.message
        fun str(key: String) = ctx[key]?.jsonPrimitive?.contentOrNull
        fun num(key: String) = ctx[key]?.jsonPrimitive?.doubleOrNull
        fun long(key: String) = ctx[key]?.jsonPrimitive?.longOrNull ?: num(key)?.toLong()

        return when (alert.kind) {
            AlertKind.LOW_FREE_SPACE -> {
                val volume = str("volume") ?: return alert.message
                val pct = num("free_percent")
                if (pct != null) "$volume sürücüsünde boş alan azaldı: %${Formatting.percent(pct).removeSuffix("%")} boş."
                else "$volume sürücüsünde boş alan azaldı."
            }
            AlertKind.DISK_FILL_RATE -> {
                val volume = str("volume") ?: return alert.message
                val pct = num("loss_percent")
                if (pct != null) "$volume sürücüsü hızla doluyor: boş alanın %${Formatting.percent(pct).removeSuffix("%")} kadarı kısa sürede tükendi."
                else "$volume sürücüsü hızla doluyor."
            }
            AlertKind.LARGE_FILE -> {
                val path = str("path") ?: return alert.message
                val size = long("size_bytes")
                if (size != null) "Büyük dosya: $path (${Formatting.bytes(size)})" else "Büyük dosya: $path"
            }
            AlertKind.FAST_GROWTH -> {
                val path = str("path") ?: return alert.message
                val delta = long("delta_bytes")
                if (delta != null) "$path kısa sürede ${Formatting.bytes(delta)} büyüdü." else "$path hızla büyüyor."
            }
            else -> alert.message
        }
    }
}
