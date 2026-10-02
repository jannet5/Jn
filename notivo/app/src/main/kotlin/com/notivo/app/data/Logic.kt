package com.notivo.app.data

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
}
