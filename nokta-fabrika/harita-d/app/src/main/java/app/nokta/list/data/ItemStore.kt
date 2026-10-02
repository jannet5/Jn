package app.nokta.list.data

import android.content.Context
import androidx.room.Room

/** Depolama sozlesmesi: testlerde bellek icindeki sahte ile, uygulamada Room ile calisir. */
interface ItemStore {
    fun load(): List<Item>
    fun save(items: List<Item>)
}

class RoomItemStore(context: Context) : ItemStore {
    private val dao = Room.databaseBuilder(context.applicationContext, NoktaDb::class.java, "nokta.db")
        .build()
        .items()

    override fun load(): List<Item> = dao.all()
    override fun save(items: List<Item>) = dao.replaceAll(items)
}
