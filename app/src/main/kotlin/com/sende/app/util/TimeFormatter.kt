package com.sende.app.util

import android.text.format.DateUtils
import java.time.Instant

/**
 * Relative, locale-correct timestamps ("5 dk önce", "5 min ago", "il y a 5 min", …) via
 * `DateUtils`, which already ships correct pluralization/RTL handling for every language
 * Android supports instead of a hand-rolled string table (spec 5.4 localization + RTL).
 * Anything under a minute reads [justNow] ("Şimdi") instead of "0 dk. önce".
 */
object TimeFormatter {
    fun relative(instant: Instant, now: Instant = Instant.now(), justNow: String): CharSequence {
        if (now.toEpochMilli() - instant.toEpochMilli() < DateUtils.MINUTE_IN_MILLIS) return justNow
        return DateUtils.getRelativeTimeSpanString(
            instant.toEpochMilli(),
            now.toEpochMilli(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE,
        )
    }
}
