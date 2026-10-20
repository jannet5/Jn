package com.sende.app.capture

/**
 * Short-lived in-memory half of the duplicate guard.
 *
 * It is a fast pre-check so a burst of near-simultaneous updates to the same notification never
 * even reaches Room; it is *not* the source of truth. The unique fingerprint index on
 * [com.sende.app.data.local.InboxMessageEntity] is, since this cache is empty again after a
 * process restart and a listener reconnect re-enumerates currently active notifications.
 *
 * It deliberately keys on the message fingerprint only, never on the notification key: chat apps
 * keep one notification per conversation and update it for every new message, so the key stays
 * the same across different messages.
 */
class DuplicateGuard(private val maxEntries: Int = 1000) {

    private val seen = LinkedHashSet<String>()

    @Synchronized
    fun isKnown(fingerprint: String): Boolean = seen.contains(fingerprint)

    @Synchronized
    fun remember(fingerprint: String) {
        seen.add(fingerprint)
        while (seen.size > maxEntries) {
            val oldest = seen.iterator()
            if (oldest.hasNext()) {
                oldest.next()
                oldest.remove()
            }
        }
    }

    @Synchronized
    fun clear() = seen.clear()
}
