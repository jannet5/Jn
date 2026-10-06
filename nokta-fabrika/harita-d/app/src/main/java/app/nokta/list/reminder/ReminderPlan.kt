package app.nokta.list.reminder

import java.util.Calendar
import java.util.TimeZone

/**
 * Bir ogenin hatirlatma ayari (saf Kotlin, birim testle dogrulanir).
 * [everyMin]: kac dakikada bir bildirim (null = kapali). [atMillis]: tek seferlik alarm ani (null = kapali).
 * Ikisi ayni anda acik olabilir.
 */
data class ReminderPlan(val everyMin: Int? = null, val atMillis: Long? = null) {
    val isEmpty: Boolean get() = everyMin == null && atMillis == null

    /** SharedPreferences icin duz metin: "every=10;at=1700000000000". */
    fun encode(): String = "every=${everyMin ?: ""};at=${atMillis ?: ""}"

    companion object {
        const val MIN_EVERY = 1
        const val MAX_EVERY = 1440
        const val DEFAULT_EVERY = 10

        /** Bozuk ya da bos kayit null doner. */
        fun decode(s: String?): ReminderPlan? {
            if (s.isNullOrBlank()) return null
            val map = s.split(';').mapNotNull { part ->
                val kv = part.split('=', limit = 2)
                if (kv.size == 2) kv[0].trim() to kv[1].trim() else null
            }.toMap()
            val every = map["every"]?.toIntOrNull()?.takeIf { it in MIN_EVERY..MAX_EVERY }
            val at = map["at"]?.toLongOrNull()?.takeIf { it > 0 }
            return ReminderPlan(every, at).takeIf { !it.isEmpty }
        }

        /** Kullanicinin yazdigi dakika: 1..1440 disi ya da sayi olmayan metin null. */
        fun parseMinutes(raw: String): Int? = raw.trim().toIntOrNull()?.takeIf { it in MIN_EVERY..MAX_EVERY }

        /** Secilen saat:dakika icin bir sonraki an: bugun henuz gelmediyse bugun, geldiyse yarin. */
        fun nextAt(now: Long, hour: Int, minute: Int, zone: TimeZone = TimeZone.getDefault()): Long {
            val c = Calendar.getInstance(zone).apply {
                timeInMillis = now
                set(Calendar.HOUR_OF_DAY, hour); set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }
            if (c.timeInMillis <= now) c.add(Calendar.DAY_OF_YEAR, 1)
            return c.timeInMillis
        }

        fun nextEvery(now: Long, everyMin: Int): Long = now + everyMin * 60_000L

        /** Alarm ani bugune mi yarina mi dusuyor (diyalog metni icin). */
        fun isToday(now: Long, at: Long, zone: TimeZone = TimeZone.getDefault()): Boolean {
            val a = Calendar.getInstance(zone).apply { timeInMillis = now }
            val b = Calendar.getInstance(zone).apply { timeInMillis = at }
            return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) && a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)
        }
    }
}
