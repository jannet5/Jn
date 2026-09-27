package com.tekpanel.app.capture

/**
 * Short-lived in-memory half of CAP-07's duplicate guard.
 *
 * This is a fast pre-check so a burst of near-simultaneous updates to the same notification
 * never even reaches Room; it is *not* the source of truth. The unique indices on
 * [com.tekpanel.app.data.local.InboxMessageEntity] are, since this cache is empty again after
 * a process restart and a listener reconnect re-enumerates currently active notifications.
 */
class DuplicateGuard(private val maxEntries: Int = 500) {

    private val seenKeys = LinkedHashSet<String>()
    private val seenFingerprints = LinkedHashSet<String>()

    @Synchronized
    fun isLikelyDuplicate(notificationKey: String?, fingerprint: String): Boolean {
        if (notificationKey != null && seenKeys.contains(notificationKey)) return true
        return seenFingerprints.contains(fingerprint)
    }

    @Synchronized
    fun remember(notificationKey: String?, fingerprint: String) {
        if (notificationKey != null) rememberInto(seenKeys, notificationKey)
        rememberInto(seenFingerprints, fingerprint)
    }

    private fun rememberInto(set: LinkedHashSet<String>, value: String) {
        set.add(value)
        while (set.size > maxEntries) {
            val oldest = set.iterator()
            if (oldest.hasNext()) {
                oldest.next()
                oldest.remove()
            }
        }
    }

    @Synchronized
    fun clear() {
        seenKeys.clear()
        seenFingerprints.clear()
    }
}
