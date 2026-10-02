package com.notivo.app.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "notifs", indices = [Index("pkg"), Index("time")])
data class Notif(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sbnKey: String,
    val pkg: String,
    val appName: String,
    val title: String,
    val text: String,
    val time: Long,
    val category: String,
    val isMessage: Boolean,
    val read: Boolean = false,
    val starred: Boolean = false,
    val deletedBySender: Boolean = false,
)

@Entity(tableName = "snoozes")
data class Snooze(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val at: Long, val title: String, val text: String, val app: String,
)

@Entity(tableName = "rules")
data class Rule(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pkg: String,          // "" = all apps
    val keyword: String,      // "" = any text
    val action: String,       // BLOCK (hide from tray + don't store) | MUTE (store silently, dismiss from tray)
    val enabled: Boolean = true,
)

@Entity(tableName = "excluded") data class Excluded(@PrimaryKey val pkg: String)

data class AppCount(val pkg: String, val appName: String, val n: Int, val unread: Int)
data class DayCount(val day: String, val n: Int)

@Dao
interface NotiDao {
    @Insert suspend fun insert(n: Notif): Long
    @Query("SELECT * FROM notifs ORDER BY time DESC LIMIT 2000") fun all(): Flow<List<Notif>>
    @Query("SELECT pkg, appName, COUNT(*) n, SUM(CASE WHEN read=0 THEN 1 ELSE 0 END) unread FROM notifs GROUP BY pkg ORDER BY n DESC")
    fun appCounts(): Flow<List<AppCount>>
    @Query("SELECT strftime('%Y-%m-%d', time/1000, 'unixepoch', 'localtime') day, COUNT(*) n FROM notifs WHERE time > :since GROUP BY day ORDER BY day")
    fun perDay(since: Long): Flow<List<DayCount>>
    @Query("UPDATE notifs SET read=1 WHERE id=:id") suspend fun markRead(id: Long)
    @Query("UPDATE notifs SET read=1") suspend fun markAllRead()
    @Query("UPDATE notifs SET starred = NOT starred WHERE id=:id") suspend fun toggleStar(id: Long)
    @Query("DELETE FROM notifs WHERE id=:id") suspend fun delete(id: Long)
    @Query("DELETE FROM notifs WHERE starred=0") suspend fun clearUnstarred()
    @Query("DELETE FROM notifs WHERE pkg=:pkg") suspend fun deleteApp(pkg: String)
    @Query("DELETE FROM notifs WHERE time < :before AND starred=0") suspend fun purgeOlder(before: Long)
    @Query("SELECT * FROM notifs WHERE id=:id") suspend fun get(id: Long): Notif?
    @Query("SELECT * FROM notifs ORDER BY time DESC") suspend fun allNow(): List<Notif>
    @Query("SELECT COUNT(*) FROM notifs WHERE pkg=:pkg AND title=:t AND text=:x AND time=:time")
    suspend fun sameCount(pkg: String, t: String, x: String, time: Long): Int
    @Query("SELECT COUNT(*) FROM notifs WHERE read=0") suspend fun unreadNow(): Int
    /** Marks the newest recent message of this conversation as (probably) deleted by its sender. */
    @Query("""UPDATE notifs SET deletedBySender=1 WHERE id = (SELECT id FROM notifs
        WHERE pkg=:pkg AND title=:title AND deletedBySender=0 AND time > :since ORDER BY time DESC LIMIT 1)""")
    suspend fun markDeleted(pkg: String, title: String, since: Long)

    @Insert suspend fun addSnooze(s: Snooze): Long
    @Query("DELETE FROM snoozes WHERE id=:id") suspend fun delSnooze(id: Long)
    @Query("SELECT * FROM snoozes") suspend fun snoozes(): List<Snooze>
    // dedupe: same app+title+text in last 10s
    @Query("SELECT COUNT(*) FROM notifs WHERE pkg=:pkg AND title=:t AND text=:x AND time > :since")
    suspend fun dupCount(pkg: String, t: String, x: String, since: Long): Int

    @Query("SELECT * FROM rules ORDER BY id DESC") fun rules(): Flow<List<Rule>>
    @Query("SELECT * FROM rules WHERE enabled=1") suspend fun activeRules(): List<Rule>
    @Insert suspend fun addRule(r: Rule)
    @Query("DELETE FROM rules WHERE id=:id") suspend fun delRule(id: Long)
    @Query("UPDATE rules SET enabled = NOT enabled WHERE id=:id") suspend fun toggleRule(id: Long)

    @Query("SELECT pkg FROM excluded") fun excluded(): Flow<List<String>>
    @Query("SELECT pkg FROM excluded") suspend fun excludedNow(): List<String>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun exclude(e: Excluded)
    @Query("DELETE FROM excluded WHERE pkg=:pkg") suspend fun include(pkg: String)
}

@Database(entities = [Notif::class, Rule::class, Excluded::class, Snooze::class], version = 1, exportSchema = false)
abstract class Db : RoomDatabase() {
    abstract fun dao(): NotiDao
    companion object {
        @Volatile private var inst: Db? = null
        fun get(c: Context): Db = inst ?: synchronized(this) {
            inst ?: Room.databaseBuilder(c.applicationContext, Db::class.java, "notivo.db").build().also { inst = it }
        }
    }
}
