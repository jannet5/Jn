package com.sende.app.data.repository

import com.sende.app.data.local.InboxMessageDao
import com.sende.app.data.local.PackageCount
import com.sende.app.data.local.toDomain
import com.sende.app.data.local.toEntity
import com.sende.app.data.media.MediaStore
import com.sende.app.domain.model.Conversation
import com.sende.app.domain.model.InboxMessage
import com.sende.app.domain.model.MessageState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.Instant

/**
 * Single source of truth for [InboxMessage]s. Room is the only persistence layer for message
 * content; nothing here ever leaves the device.
 */
class InboxRepository(
    private val dao: InboxMessageDao,
    private val mediaStore: MediaStore,
) {

    /** Unread messages grouped by conversation, conversations ordered by their newest message. */
    fun observeConversations(): Flow<List<Conversation>> = dao.observeUnread().map { rows ->
        rows.map { it.toDomain() }
            .groupBy { it.conversationKey }
            .map { (key, messages) ->
                val newest = messages.last()
                Conversation(
                    key = key,
                    sourcePackage = newest.sourcePackage,
                    title = newest.conversationTitle,
                    isGroup = newest.isGroupConversation,
                    avatarPath = messages.lastOrNull { it.avatarPath != null }?.avatarPath,
                    messages = messages,
                )
            }
            .sortedByDescending { it.lastAt }
    }

    fun observePackageCounts(): Flow<List<PackageCount>> = dao.observePackageCounts()

    fun observeUnreadCount(): Flow<Int> = dao.observeUnreadCount()

    suspend fun exists(fingerprint: String): Boolean = dao.fingerprintExists(fingerprint)

    /** True if the same words from the same sender in the same chat were stored since [sinceMillis]. */
    suspend fun hasSameContentSince(conversationKey: String, senderName: String, messageText: String, sinceMillis: Long): Boolean =
        dao.contentExistsSince(conversationKey, senderName, messageText, sinceMillis)

    /** Returns true if [message] was newly inserted, false if the fingerprint index rejected it. */
    suspend fun insertIfNew(message: InboxMessage): Boolean = dao.insertIfNew(message.toEntity()) != -1L

    suspend fun markRead(ids: List<String>) {
        if (ids.isNotEmpty()) dao.setStateForIds(ids, MessageState.READ.name)
    }

    /**
     * Hides every unread message of [packages] (null = all) and returns their prior (id -> state)
     * map so the caller can offer an undo.
     */
    suspend fun clearUnread(packages: Collection<String>?): Map<String, String> {
        val ids = dao.unreadIds(allPackages = packages == null, packages = packages?.toList().orEmpty())
        if (ids.isEmpty()) return emptyMap()
        val previous = ids.chunked(SQLITE_VARIABLE_LIMIT).flatMap { dao.statesOf(it) }.associate { it.id to it.state }
        ids.chunked(SQLITE_VARIABLE_LIMIT).forEach { dao.setStateForIds(it, MessageState.HIDDEN.name) }
        return previous
    }

    suspend fun restoreStates(previousStates: Map<String, String>) {
        if (previousStates.isNotEmpty()) dao.restoreStates(previousStates)
    }

    /** Drops handled messages older than [keepFor] and the picture files nobody references any more. */
    suspend fun prune(keepFor: Duration = Duration.ofDays(RETENTION_DAYS)) {
        dao.pruneHandledBefore(Instant.now().minus(keepFor).toEpochMilli())
        mediaStore.deleteUnreferenced(dao.referencedMediaPaths().toSet())
    }

    suspend fun exportSnapshot(): List<InboxMessage> = dao.getAllOnce().map { it.toDomain() }

    suspend fun countAll(): Int = dao.countAll()

    suspend fun eraseAllMessages() {
        dao.deleteAll()
        mediaStore.deleteAll()
    }

    private companion object {
        const val RETENTION_DAYS = 14L
        const val SQLITE_VARIABLE_LIMIT = 500
    }
}
