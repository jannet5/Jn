package com.tekpanel.app.capture

import com.tekpanel.app.domain.model.FilterReason

/**
 * Layered filter, in this order:
 * 1. is the package one TekPanel must never read (itself, the system UI)?
 * 2. did the user pick this app as a channel?
 * 3. is there meaningful text or a picture?
 * 4. is this a group summary / bulk counter?
 * 5. is this a live/ongoing/service/promotional notification?
 * (Duplicate detection needs the store and is applied by [CaptureCoordinator] afterwards.)
 *
 * Hard rules (1, 2, 4 and the ongoing/category check in 5) never depend on message text, so they
 * can never misfire on a genuine customer message. [PromotionalHeuristics] is the only part that
 * looks at content and is kept deliberately small.
 */
class MessageFilterEngine(
    private val ownPackage: String,
    private val heuristics: PromotionalHeuristics = PromotionalHeuristics(),
) {

    private val neverRead = setOf("android", "com.android.systemui")

    private val rejectCategories = setOf(
        "call", "promo", "service", "sys", "progress", "transport",
        "status", "recommendation", "navigation", "alarm", "reminder", "workout", "err",
    )

    fun decide(
        payload: RawNotificationPayload,
        normalized: NormalizedNotification?,
        channelEnabled: Boolean,
    ): FilterReason {
        if (payload.packageName == ownPackage || payload.packageName in neverRead) {
            return FilterReason.UNSUPPORTED_PACKAGE
        }
        if (!channelEnabled) {
            return FilterReason.CHANNEL_DISABLED
        }
        if (normalized == null || normalized.messages.none { it.messageText.isNotBlank() || it.hasPicture }) {
            return FilterReason.EMPTY_CONTENT
        }
        if (payload.isGroupSummary) {
            return FilterReason.GROUP_SUMMARY
        }
        if (payload.isOngoing || (payload.category != null && payload.category in rejectCategories)) {
            return FilterReason.ONGOING_SERVICE_CATEGORY
        }
        if (heuristics.looksNonCustomer(payload, normalized)) {
            return FilterReason.LIVE_OR_PROMOTIONAL_HEURISTIC
        }
        return FilterReason.ACCEPTED
    }
}
