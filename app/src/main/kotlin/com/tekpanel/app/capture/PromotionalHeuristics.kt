package com.tekpanel.app.capture

import com.tekpanel.app.data.catalog.SourceAppCatalog

/**
 * The "sezgisel kurallar" (soft/heuristic) bucket from spec CAP-06.
 *
 * Kept intentionally small: every rule here is a pattern that describes a notification
 * *about* the app itself (an engagement alert, a live-broadcast alert, a system message)
 * rather than a direct message *from a person to the business*. Both rules are opt-in
 * per notification (they must all match a real, characteristic pattern) rather than
 * broad keyword bans, specifically so a customer message that happens to mention e.g.
 * "kargo" ("could you ship this today?") is never dropped.
 */
class PromotionalHeuristics {

    fun looksNonCustomer(payload: RawNotificationPayload, normalized: NormalizedContent): Boolean {
        if (isGenericBrandSender(payload.packageName, normalized.senderName)) return true
        if (matchesLiveBroadcastPhrase(normalized.messageText) || matchesLiveBroadcastPhrase(payload.title)) return true
        return false
    }

    /**
     * Engagement/system notifications from social apps often use the app's own name as the
     * "sender" (e.g. title = "Instagram", text = "yourhandle beğenilerinizi görüntüleyin")
     * instead of a real username, because they're not a direct message at all.
     */
    private fun isGenericBrandSender(packageName: String, senderName: String): Boolean {
        val app = SourceAppCatalog.byPackageName(packageName) ?: return false
        val genericNames = genericNamesByChannel[app.id] ?: return false
        return genericNames.any { it.equals(senderName.trim(), ignoreCase = true) }
    }

    private fun matchesLiveBroadcastPhrase(text: String?): Boolean {
        val haystack = text?.lowercase() ?: return false
        return liveBroadcastPhrases.any { haystack.contains(it) }
    }

    companion object {
        private val genericNamesByChannel: Map<String, Set<String>> = mapOf(
            "instagram" to setOf("instagram"),
            "tiktok" to setOf("tiktok"),
            "x_twitter" to setOf("x", "twitter"),
            "facebook" to setOf("facebook"),
        )

        private val liveBroadcastPhrases = listOf(
            "canlı yayına başladı",
            "canlı yayında",
            "started a live video",
            "is live now",
            "went live",
        )
    }
}
