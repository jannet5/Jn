package com.tekpanel.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [InboxMessageEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class TekPanelDatabase : RoomDatabase() {

    abstract fun inboxMessageDao(): InboxMessageDao

    companion object {
        const val DATABASE_NAME = "tekpanel_inbox.db"

        fun build(context: Context): TekPanelDatabase =
            Room.databaseBuilder(context, TekPanelDatabase::class.java, DATABASE_NAME).build()
    }
}
