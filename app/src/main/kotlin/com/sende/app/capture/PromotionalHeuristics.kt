package com.sende.app.capture

import com.sende.app.data.catalog.KnownApps

/**
 * The soft/heuristic filter bucket.
 *
 * Kept intentionally small: every rule is a pattern that describes a notification *about* the
 * app itself (an engagement alert, a live-broadcast alert) rather than a direct message from a
 * person. Rules are exact patterns, not broad keyword bans, so a customer message that merely
 * mentions e.g. "kargo" is never dropped.
 */
class PromotionalHeuristics {

    fun looksNonCustomer(payload: RawNotificationPayload, normalized: NormalizedNotification): Boolean {
        if (isGenericBrandSender(payload.packageName, normalized)) return true
        val latest = normalized.messages.lastOrNull()?.messageText
        if (matchesLiveBroadcastPhrase(latest) || matchesLiveBroadcastPhrase(payload.title)) return true
        return false
    }

    private fun isGenericBrandSender(packageName: String, normalized: NormalizedNotification): Boolean {
        val generic = KnownApps.GENERIC_SENDER_NAMES[packageName] ?: return false
        val sender = normalized.messages.lastOrNull()?.senderName?.trim() ?: return false
        return generic.any { it.equals(sender, ignoreCase = true) }
    }

    private fun matchesLiveBroadcastPhrase(text: String?): Boolean {
        val haystack = text?.lowercase() ?: return false
        return liveBroadcastPhrases.any { haystack.contains(it) }
    }

    companion object {
        private val liveBroadcastPhrases = listOf(
            "canlı yayına başladı",
            "canlı yayında",
            "started a live video",
            "is live now",
            "went live",
        )
    }
}
