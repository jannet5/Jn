package com.sende.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room row backing [com.sende.app.domain.model.InboxMessage].
 *
 * [fingerprint] is unique and is the persistence-layer duplicate guard. The notification key is
 * deliberately NOT unique: chat apps update one notification per conversation for every new
 * message, so the key repeats across genuinely different messages.
 */
@Entity(
    tableName = "inbox_messages",
    indices = [
        Index(value = ["fingerprint"], unique = true),
        Index(value = ["sourcePackage"]),
        Index(value = ["state"]),
        Index(value = ["conversationKey"]),
    ],
)
data class InboxMessageEntity(
    @PrimaryKey val id: String,
    val notificationKey: String?,
    val fingerprint: String,
    val sourcePackage: String,
    val conversationKey: String,
    val conversationTitle: String,
    val isGroupConversation: Boolean,
    val senderName: String,
    val messageText: String,
    val receivedAtEpochMillis: Long,
    val capturedAtEpochMillis: Long,
    val captureMethod: String,
    val state: String,
    val avatarPath: String?,
    val picturePath: String?,
)

/** Row of the per-app message counters used to rank and badge channels. */
data class PackageCount(val sourcePackage: String, val total: Int, val unread: Int, val lastAt: Long)
