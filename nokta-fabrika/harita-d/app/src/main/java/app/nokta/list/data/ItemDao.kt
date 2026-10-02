package app.nokta.list.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction

@Dao
abstract class ItemDao {
    @Query("SELECT * FROM items ORDER BY position ASC")
    abstract fun all(): List<Item>

    @Query("DELETE FROM items")
    abstract fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(items: List<Item>)

    /** Liste kucuk (yuzler); tum anlik goruntuyu tek islemde yazmak tutarlilik icin yeterli. */
    @Transaction
    open fun replaceAll(items: List<Item>) {
        clear()
        insertAll(items)
    }
}

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class NoktaDb : RoomDatabase() {
    abstract fun items(): ItemDao
}
