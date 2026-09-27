package com.tekpanel.app.data.repository

import com.tekpanel.app.data.local.InboxMessageDao
import com.tekpanel.app.data.local.toDomain
import com.tekpanel.app.data.local.toEntity
import com.tekpanel.app.domain.model.InboxMessage
import com.tekpanel.app.domain.model.MessageState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Single source of truth for [InboxMessage]s (spec CAP-08). Room is the only persistence
 * layer for message content; nothing here ever touches DataStore or leaves the device.
 */
class InboxRepository(private val dao: InboxMessageDao) {

    fun observeActive(): Flow<List<InboxMessage>> =
        dao.observeActive().map { rows -> rows.map { it.toDomain() } }

    fun observeAll(): Flow<List<InboxMessage>> =
        dao.observeAll().map { rows -> rows.map { it.toDomain() } }

    fun observeUnreadCount(): Flow<Int> = dao.observeUnreadCount()

    /** Returns true if [message] was newly inserted, false if a duplicate index rejected it. */
    suspend fun insertIfNew(message: InboxMessage): Boolean = dao.insertIfNew(message.toEntity()) != -1L

    suspend fun findByFingerprint(fingerprint: String) = dao.findByFingerprint(fingerprint)?.toDomain()

    suspend fun markState(id: String, state: MessageState) = dao.setState(id, state.name)

    /**
     * Hides every currently active message and returns their prior (id -> state) map so the
     * caller can offer an undo (CAP-13).
     */
    suspend fun clearActive(currentActive: List<InboxMessage>): Map<String, String> {
        val previousStates = currentActive.associate { it.id to it.state.name }
        dao.hideAllActive()
        return previousStates
    }

    suspend fun clearActiveForChannel(channelId: String, currentActive: List<InboxMessage>): Map<String, String> {
        val previousStates = currentActive.filter { it.channelId == channelId }.associate { it.id to it.state.name }
        dao.hideAllActiveForChannel(channelId)
        return previousStates
    }

    suspend fun undoClear(previousStates: Map<String, String>) {
        if (previousStates.isNotEmpty()) dao.restoreStates(previousStates)
    }

    suspend fun exportSnapshot(): List<InboxMessage> = dao.getAllOnce().map { it.toDomain() }

    suspend fun countAll(): Int = dao.countAll()

    suspend fun eraseAllMessages() = dao.deleteAll()
}
