package app.nokta.list.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tek liste satiri. Sira, listedeki konumdan turetilir ([position] yalniz saklama icin). */
@Entity(tableName = "items")
data class Item(
    @PrimaryKey val id: Long,
    val text: String,
    val done: Boolean,
    val position: Int,
)
