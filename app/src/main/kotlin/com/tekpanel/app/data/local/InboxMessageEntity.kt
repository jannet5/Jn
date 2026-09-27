package com.tekpanel.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room row backing [com.tekpanel.app.domain.model.InboxMessage] (spec CAP-08).
 *
 * Two unique indices implement CAP-07's duplicate guard at the persistence layer, as a
 * backstop behind the in-memory cache in [com.tekpanel.app.capture.DuplicateGuard]:
 * - [notificationKey] is unique when present (SQLite allows multiple NULLs through a unique
 *   index, so SHARE-captured rows without a notification key are unaffected).
 * - [fingerprint] is always unique, covering listener reconnect re-enumeration and any case
 *   where the OS reuses/omits a notification key.
 */
@Entity(
    tableName = "inbox_messages",
    indices = [
        Index(value = ["notificationKey"], unique = true),
        Index(value = ["fingerprint"], unique = true),
        Index(value = ["channelId"]),
        Index(value = ["state"]),
    ],
)
data class InboxMessageEntity(
    @PrimaryKey val id: String,
    val notificationKey: String?,
    val fingerprint: String,
    val sourcePackage: String,
    val channelId: String,
    val senderName: String,
    val messageText: String,
    val receivedAtEpochMillis: Long,
    val capturedAtEpochMillis: Long,
    val captureMethod: String,
    val state: String,
    val canOpenLiveConversation: Boolean,
)
