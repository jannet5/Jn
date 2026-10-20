package com.sende.app.util

import androidx.annotation.StringRes
import com.sende.app.R

/**
 * Phone makers add their own battery managers on top of Android and several of them kill background
 * listeners (the well-known "dontkillmyapp" problem). This picks the one-sentence, maker-specific
 * instruction to show next to the "Listening paused" card.
 */
object OemHints {

    @StringRes
    fun hintFor(manufacturer: String?): Int? {
        val maker = manufacturer?.trim()?.lowercase() ?: return null
        return when {
            maker in setOf("xiaomi", "redmi", "poco") -> R.string.oem_hint_xiaomi
            maker in setOf("huawei", "honor") -> R.string.oem_hint_huawei
            maker == "samsung" -> R.string.oem_hint_samsung
            maker in setOf("oppo", "realme", "oneplus") -> R.string.oem_hint_oppo
            maker in setOf("vivo", "iqoo") -> R.string.oem_hint_vivo
            else -> null
        }
    }
}
