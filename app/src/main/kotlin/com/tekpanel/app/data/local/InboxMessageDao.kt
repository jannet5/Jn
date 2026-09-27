package com.tekpanel.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InboxMessageDao {

    /**
     * Returns the generated rowid, or -1 if the insert was ignored because [entity]'s
     * notificationKey or fingerprint already exists (CAP-07 persistence-layer backstop).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNew(entity: InboxMessageEntity): Long

    @Update
    suspend fun update(entity: InboxMessageEntity)

    @Query("SELECT * FROM inbox_messages WHERE state != 'HIDDEN' ORDER BY receivedAtEpochMillis DESC")
    fun observeActive(): Flow<List<InboxMessageEntity>>

    @Query("SELECT * FROM inbox_messages ORDER BY receivedAtEpochMillis DESC")
    fun observeAll(): Flow<List<InboxMessageEntity>>

    @Query("SELECT * FROM inbox_messages ORDER BY receivedAtEpochMillis DESC")
    suspend fun getAllOnce(): List<InboxMessageEntity>

    @Query("SELECT * FROM inbox_messages WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): InboxMessageEntity?

    @Query("SELECT * FROM inbox_messages WHERE fingerprint = :fingerprint LIMIT 1")
    suspend fun findByFingerprint(fingerprint: String): InboxMessageEntity?

    @Query("SELECT COUNT(*) FROM inbox_messages")
    suspend fun countAll(): Int

    @Query("SELECT COUNT(*) FROM inbox_messages WHERE state = 'UNREAD'")
    fun observeUnreadCount(): Flow<Int>

    @Query("UPDATE inbox_messages SET state = :state WHERE id = :id")
    suspend fun setState(id: String, state: String)

    @Query("UPDATE inbox_messages SET state = 'HIDDEN' WHERE state != 'HIDDEN'")
    suspend fun hideAllActive(): Int

    @Query("UPDATE inbox_messages SET state = 'HIDDEN' WHERE channelId = :channelId AND state != 'HIDDEN'")
    suspend fun hideAllActiveForChannel(channelId: String): Int

    @Transaction
    suspend fun restoreStates(idsToStates: Map<String, String>) {
        idsToStates.forEach { (id, state) -> setState(id, state) }
    }

    @Query("DELETE FROM inbox_messages")
    suspend fun deleteAll()
}
