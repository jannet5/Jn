package com.tekpanel.app.capture

import com.tekpanel.app.data.catalog.SourceAppCatalog
import com.tekpanel.app.domain.model.FilterReason

/**
 * Layered filter from spec CAP-06, in the exact order the spec lists:
 * 1. package in the supported catalog?
 * 2. channel enabled by the user?
 * 3. is there meaningful text?
 * 4. is this a group summary / bulk counter?
 * 5. is this a live/ongoing/promotional/service/system notification?
 * (6, duplicate detection, is deliberately NOT here: it needs the DAO/cache and is applied by
 * [com.tekpanel.app.capture.CaptureCoordinator] after this content-only decision.)
 *
 * Hard rules (1, 2, 4, and the ongoing/category check in 5) never depend on message text, so
 * they can never misfire on a genuine customer message. The heuristic rules inside
 * [PromotionalHeuristics] are the only ones that look at text/sender content and are kept
 * deliberately small and conservative (spec CAP-06: "a heuristic rule must not silently lose
 * a genuine customer message").
 */
class MessageFilterEngine(
    private val heuristics: PromotionalHeuristics = PromotionalHeuristics(),
) {

    private val rejectCategories = setOf(
        "call", "promo", "service", "sys", "progress", "transport",
        "status", "recommendation", "navigation", "alarm", "reminder", "workout", "err",
    )

    fun decide(
        payload: RawNotificationPayload,
        normalized: NormalizedContent?,
        channelEnabled: Boolean,
    ): FilterReason {
        if (!SourceAppCatalog.isSupportedPackage(payload.packageName)) {
            return FilterReason.UNSUPPORTED_PACKAGE
        }
        if (!channelEnabled) {
            return FilterReason.CHANNEL_DISABLED
        }
        if (normalized == null || normalized.messageText.isBlank()) {
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
