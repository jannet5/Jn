package com.tekpanel.app.domain.model

/** Why the filter engine accepted or rejected a captured notification (CAP-06, CAP-16). */
enum class FilterReason {
    ACCEPTED,
    UNSUPPORTED_PACKAGE,
    CHANNEL_DISABLED,
    EMPTY_CONTENT,
    GROUP_SUMMARY,
    ONGOING_SERVICE_CATEGORY,
    LIVE_OR_PROMOTIONAL_HEURISTIC,
    DUPLICATE_BY_KEY,
    DUPLICATE_BY_FINGERPRINT,
}

/** Result of running a captured notification through [com.tekpanel.app.capture.MessageFilterEngine]. */
sealed interface FilterDecision {
    data class Accept(val message: InboxMessage) : FilterDecision
    data class Reject(val reason: FilterReason, val sourcePackage: String) : FilterDecision
}
