package com.notivo.app.data

/** All notifications of one app, newest first. */
data class AppGroup(val pkg: String, val appName: String, val items: List<Notif>) {
    val unread: Int get() = items.count { !it.read }
    val latest: Long get() = items.maxOf { it.time }
}

/** Pure decision logic, kept free of Android types so it can be unit-tested on the JVM. */
object Logic {
    private val deletedPatterns = listOf(
        "this message was deleted", "bu mesaj silindi", "mesaj silindi",
        "se eliminó este mensaje", "diese nachricht wurde gelöscht", "ce message a été supprimé",
    )

    /** True for the placeholder text messengers show when the sender deletes a message. */
    fun isDeletedNotice(text: String): Boolean {
        val t = text.trim().lowercase()
        return deletedPatterns.any { t == it || t.endsWith(it) }
    }

    /** Case/diacritic-insensitive form that treats Turkish İ/I/ı/i as the same letter (Kotlin's lowercase() turns İ into i + combining dot). */
    fun norm(s: String): String = s.replace('İ', 'i').replace('I', 'i').replace('ı', 'i').lowercase()

    /** First enabled rule that matches, or null. Empty pkg/keyword in a rule are wildcards. */
    fun matchRule(rules: List<Rule>, pkg: String, title: String, text: String): Rule? {
        val hay = norm("$title $text")
        return rules.firstOrNull {
            it.enabled && (it.pkg.isEmpty() || it.pkg == pkg) && (it.keyword.isEmpty() || hay.contains(norm(it.keyword)))
        }
    }

    fun dayBucket(now: Long, t: Long, zoneOffsetMs: Long): Int {
        val d = Math.floorDiv(now + zoneOffsetMs, 86_400_000L) - Math.floorDiv(t + zoneOffsetMs, 86_400_000L)
        return when { d <= 0 -> 0; d == 1L -> 1; d < 7 -> 2; else -> 3 }
    }

    const val GROUP_PREVIEW = 3

    const val RANGE_ALL = 0
    const val RANGE_TODAY = 1
    const val RANGE_YESTERDAY = 2
    const val RANGE_7_DAYS = 3
    const val RANGE_30_DAYS = 4

    /** Takvim günü bazlı tarih süzgeci (yerel saat dilimiyle). Gelecek tarihli kayıtlar bugün sayılır. */
    fun inRange(range: Int, now: Long, t: Long, zoneOffsetMs: Long): Boolean {
        val d = maxOf(0L, Math.floorDiv(now + zoneOffsetMs, 86_400_000L) - Math.floorDiv(t + zoneOffsetMs, 86_400_000L))
        return when (range) {
            RANGE_TODAY -> d == 0L
            RANGE_YESTERDAY -> d == 1L
            RANGE_7_DAYS -> d <= 6
            RANGE_30_DAYS -> d <= 29
            else -> true
        }
    }

    fun sorted(items: List<Notif>, newestFirst: Boolean): List<Notif> =
        if (newestFirst) items.sortedByDescending { it.time } else items.sortedBy { it.time }

    /** One group per app, groups ordered by their newest notification, items newest first. */
    fun groupByApp(items: List<Notif>, newestFirst: Boolean = true): List<AppGroup> =
        items.groupBy { it.pkg }
            .map { (pkg, l) -> AppGroup(pkg, l.first().appName, sorted(l, newestFirst)) }
            .let { g -> if (newestFirst) g.sortedByDescending { it.latest } else g.sortedBy { it.latest } }

    /** Collapsed groups show only the NEWEST few (whatever the sort direction); expanded groups show everything. Order is kept. */
    fun visible(g: AppGroup, expanded: Boolean): List<Notif> {
        if (expanded) return g.items
        val keep = g.items.sortedByDescending { it.time }.take(GROUP_PREVIEW).toSet()
        return g.items.filter { it in keep }
    }
}
