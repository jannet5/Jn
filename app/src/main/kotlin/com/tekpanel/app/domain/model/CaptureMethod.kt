package com.tekpanel.app.domain.model

/**
 * How a given [InboxMessage] entered TekPanel.
 *
 * MANUAL and PASTE are kept for forward compatibility (spec section CAP-05) but are not
 * reachable from the V1 main screen, which has no manual entry form.
 */
enum class CaptureMethod {
    NOTIFICATION,
    SHARE,
    PASTE,
    MANUAL,
}
