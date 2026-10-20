package com.tekpanel.app.domain.model

import java.time.Instant

/** UI-facing group of messages that belong to one chat (or one app feed). */
data class Conversation(
    val key: String,
    val sourcePackage: String,
    val title: String,
    val isGroup: Boolean,
    val avatarPath: String?,
    val messages: List<InboxMessage>,
) {
    val latest: InboxMessage get() = messages.last()
    val lastAt: Instant get() = latest.receivedAt
    val count: Int get() = messages.size
}
