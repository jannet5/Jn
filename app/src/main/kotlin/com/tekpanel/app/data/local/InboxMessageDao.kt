package com.tekpanel.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface InboxMessageDao {

    /** Returns the new rowid, or -1 if the insert was ignored because the fingerprint exists. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNew(entity: InboxMessageEntity): Long

    @Query("SELECT EXISTS(SELECT 1 FROM inbox_messages WHERE fingerprint = :fingerprint)")
    suspend fun fingerprintExists(fingerprint: String): Boolean

    /** Everything the main screen shows: unread messages, newest last inside a conversation. */
    @Query("SELECT * FROM inbox_messages WHERE state = 'UNREAD' ORDER BY receivedAtEpochMillis ASC, capturedAtEpochMillis ASC LIMIT 3000")
    fun observeUnread(): Flow<List<InboxMessageEntity>>

    @Query(
        "SELECT sourcePackage, COUNT(*) AS total, " +
            "SUM(CASE WHEN state = 'UNREAD' THEN 1 ELSE 0 END) AS unread, " +
            "MAX(receivedAtEpochMillis) AS lastAt FROM inbox_messages GROUP BY sourcePackage",
    )
    fun observePackageCounts(): Flow<List<PackageCount>>

    @Query("SELECT * FROM inbox_messages ORDER BY receivedAtEpochMillis DESC")
    suspend fun getAllOnce(): List<InboxMessageEntity>

    @Query("SELECT COUNT(*) FROM inbox_messages")
    suspend fun countAll(): Int

    @Query("SELECT COUNT(*) FROM inbox_messages WHERE state = 'UNREAD'")
    fun observeUnreadCount(): Flow<Int>

    @Query("SELECT id, state FROM inbox_messages WHERE id IN (:ids)")
    suspend fun statesOf(ids: List<String>): List<IdState>

    @Query("UPDATE inbox_messages SET state = :state WHERE id IN (:ids)")
    suspend fun setStateForIds(ids: List<String>, state: String)

    @Query("UPDATE inbox_messages SET state = :state WHERE id = :id")
    suspend fun setState(id: String, state: String)

    @Query("SELECT id FROM inbox_messages WHERE state = 'UNREAD' AND (:allPackages = 1 OR sourcePackage IN (:packages))")
    suspend fun unreadIds(allPackages: Boolean, packages: List<String>): List<String>

    @Transaction
    suspend fun restoreStates(idsToStates: Map<String, String>) {
        idsToStates.forEach { (id, state) -> setState(id, state) }
    }

    @Query("SELECT avatarPath FROM inbox_messages WHERE avatarPath IS NOT NULL UNION SELECT picturePath FROM inbox_messages WHERE picturePath IS NOT NULL")
    suspend fun referencedMediaPaths(): List<String>

    /** Deletes handled (read/hidden) rows older than [cutoffMillis]; unread rows are never pruned. */
    @Query("DELETE FROM inbox_messages WHERE state != 'UNREAD' AND receivedAtEpochMillis < :cutoffMillis")
    suspend fun pruneHandledBefore(cutoffMillis: Long): Int

    @Query("DELETE FROM inbox_messages")
    suspend fun deleteAll()
}

data class IdState(val id: String, val state: String)
