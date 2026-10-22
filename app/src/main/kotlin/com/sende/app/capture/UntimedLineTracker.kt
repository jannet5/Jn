package com.sende.app.capture

/**
 * Tells apart "this notification was updated with a new line" from "the same lines were delivered
 * again" for lines that carry no timestamp of their own (plain text, InboxStyle, MessagingStyle
 * without times).
 *
 * Chat apps keep one notification per conversation and rewrite it for every message, so an update
 * lists the old lines again followed by the new one. Remembering the previous list per
 * notification key lets only the lines after the overlap count as new: an old line is never
 * stored twice, and a customer who really sends the same word twice (after the notification was
 * dismissed, or with a line in between) is not mistaken for a repost.
 *
 * Kept in memory only; [forget] is called when the notification disappears from the shade.
 */
class UntimedLineTracker(private val capacity: Int = 500) {

    /** Result of comparing a delivery with the previous one for the same notification. */
    data class Diff(val newIndices: List<Int>, val firstSighting: Boolean)

    private val seen = object : LinkedHashMap<String, List<String>>(16, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, List<String>>?): Boolean = size > capacity
    }

    @Synchronized
    fun diff(notificationKey: String, lines: List<String>): Diff {
        val previous = seen[notificationKey]
        seen[notificationKey] = lines
        if (previous == null) return Diff(lines.indices.toList(), firstSighting = true)
        // Longest k such that the last k old lines are the first k lines of the new list.
        var overlap = minOf(previous.size, lines.size)
        while (overlap > 0 && previous.subList(previous.size - overlap, previous.size) != lines.subList(0, overlap)) overlap--
        return Diff((overlap until lines.size).toList(), firstSighting = false)
    }

    @Synchronized
    fun forget(notificationKey: String) {
        seen.remove(notificationKey)
    }
}
