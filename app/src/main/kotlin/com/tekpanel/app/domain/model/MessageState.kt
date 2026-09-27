package com.tekpanel.app.domain.model

/** Lifecycle state of an [InboxMessage] inside TekPanel's local store (CAP-08, CAP-12). */
enum class MessageState {
    UNREAD,
    READ,
    HIDDEN,
}
