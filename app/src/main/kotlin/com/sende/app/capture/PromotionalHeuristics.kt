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
        if (matchesLiveBroadcastPhrase(latest)) return true
        return false
    }

    private fun isGenericBrandSender(packageName: String, normalized: NormalizedNotification): Boolean {
        val generic = KnownApps.GENERIC_SENDER_NAMES[packageName] ?: return false
        val sender = normalized.messages.lastOrNull()?.senderName?.trim() ?: return false
        return generic.any { it.equals(sender, ignoreCase = true) }
    }

    /**
     * Live-broadcast alerts are one short sentence, optionally led by the broadcaster's name
     * ("ali_veli canlı yayında."). A customer message that merely contains the words, or a chat whose
     * title contains them, is a real message and must never match.
     */
    private fun matchesLiveBroadcastPhrase(text: String?): Boolean {
        val sentence = text?.trim()?.lowercase() ?: return false
        return liveBroadcastSentence.matches(sentence)
    }

    companion object {
        private val liveBroadcastSentence = Regex(
            "^(\\S+( \\S+){0,3}\\s+)?(canlı yayına başladı|canlı yayında|started a live video|is live now|is live|went live)[.!]?$",
        )
    }
}
