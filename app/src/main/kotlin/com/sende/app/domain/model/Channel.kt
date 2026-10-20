package com.sende.app.domain.model

/**
 * An installed app the user can listen to. [packageName] is the identity, [label] the only name
 * ever shown. Counters come from the message store, so apps with history rank first.
 */
data class Channel(
    val packageName: String,
    val label: String,
    val enabled: Boolean,
    val totalMessages: Int,
    val unreadMessages: Int,
    val lastMessageAt: Long,
    val isPopular: Boolean,
)
