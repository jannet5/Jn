package com.cepgozcu.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FileEventDao {
    @Query("SELECT * FROM file_events ORDER BY occurredAt DESC LIMIT :limit")
    fun observeRecent(limit: Int = MAX_ROWS_PER_TABLE): Flow<List<FileEventEntity>>

    @Query("SELECT * FROM file_events ORDER BY occurredAt DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = MAX_ROWS_PER_TABLE): List<FileEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<FileEventEntity>)

    @Query("DELETE FROM file_events WHERE id NOT IN (SELECT id FROM file_events ORDER BY occurredAt DESC LIMIT :keep)")
    suspend fun trim(keep: Int = MAX_ROWS_PER_TABLE)

    @Transaction
    suspend fun upsertAndTrim(items: List<FileEventEntity>) {
        upsertAll(items)
        trim()
    }
}

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts ORDER BY occurredAt DESC LIMIT :limit")
    fun observeRecent(limit: Int = MAX_ROWS_PER_TABLE): Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts ORDER BY occurredAt DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = MAX_ROWS_PER_TABLE): List<AlertEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<AlertEntity>)

    @Query("DELETE FROM alerts WHERE id NOT IN (SELECT id FROM alerts ORDER BY occurredAt DESC LIMIT :keep)")
    suspend fun trim(keep: Int = MAX_ROWS_PER_TABLE)

    @Transaction
    suspend fun upsertAndTrim(items: List<AlertEntity>) {
        upsertAll(items)
        trim()
    }
}

@Dao
interface AuditDao {
    @Query("SELECT * FROM audit_entries ORDER BY occurredAt DESC LIMIT :limit")
    fun observeRecent(limit: Int = MAX_ROWS_PER_TABLE): Flow<List<AuditEntity>>

    @Query("SELECT * FROM audit_entries ORDER BY occurredAt DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = MAX_ROWS_PER_TABLE): List<AuditEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<AuditEntity>)

    @Query("DELETE FROM audit_entries WHERE id NOT IN (SELECT id FROM audit_entries ORDER BY occurredAt DESC LIMIT :keep)")
    suspend fun trim(keep: Int = MAX_ROWS_PER_TABLE)

    @Transaction
    suspend fun upsertAndTrim(items: List<AuditEntity>) {
        upsertAll(items)
        trim()
    }
}
