package com.tekpanel.app.capture

import android.app.PendingIntent

/**
 * Holds each captured notification's `contentIntent` for the lifetime of the process only
 * (spec CAP-14). Never written to Room or DataStore: `PendingIntent` cannot be serialized
 * meaningfully, and the spec explicitly says TekPanel must not promise "always opens the
 * exact conversation" once the process/service has been killed and restarted.
 */
class LiveIntentRegistry(private val maxEntries: Int = 300) {

    private val intentsByMessageId = LinkedHashMap<String, PendingIntent>()

    @Synchronized
    fun put(messageId: String, pendingIntent: PendingIntent) {
        intentsByMessageId[messageId] = pendingIntent
        while (intentsByMessageId.size > maxEntries) {
            val oldest = intentsByMessageId.keys.iterator()
            if (oldest.hasNext()) {
                oldest.next()
                oldest.remove()
            }
        }
    }

    @Synchronized
    fun get(messageId: String): PendingIntent? = intentsByMessageId[messageId]

    @Synchronized
    fun clear() {
        intentsByMessageId.clear()
    }
}
