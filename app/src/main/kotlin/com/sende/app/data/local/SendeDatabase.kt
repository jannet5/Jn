package com.sende.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [InboxMessageEntity::class],
    version = 2,
    exportSchema = true,
)
abstract class SendeDatabase : RoomDatabase() {

    abstract fun inboxMessageDao(): InboxMessageDao

    companion object {
        const val DATABASE_NAME = "sende_inbox.db"

        /**
         * v1 -> v2: channels became "any installed app" (channelId dropped, conversation columns
         * and media paths added) and the notificationKey unique index was removed because chat apps
         * reuse one notification key for every message of a conversation. The table is rebuilt so
         * existing messages survive; their conversation is derived from package + sender.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `inbox_messages_new` (" +
                        "`id` TEXT NOT NULL, `notificationKey` TEXT, `fingerprint` TEXT NOT NULL, " +
                        "`sourcePackage` TEXT NOT NULL, `conversationKey` TEXT NOT NULL, " +
                        "`conversationTitle` TEXT NOT NULL, `isGroupConversation` INTEGER NOT NULL, " +
                        "`senderName` TEXT NOT NULL, `messageText` TEXT NOT NULL, " +
                        "`receivedAtEpochMillis` INTEGER NOT NULL, `capturedAtEpochMillis` INTEGER NOT NULL, " +
                        "`captureMethod` TEXT NOT NULL, `state` TEXT NOT NULL, " +
                        "`avatarPath` TEXT, `picturePath` TEXT, PRIMARY KEY(`id`))",
                )
                db.execSQL(
                    "INSERT INTO inbox_messages_new (id, notificationKey, fingerprint, sourcePackage, " +
                        "conversationKey, conversationTitle, isGroupConversation, senderName, messageText, " +
                        "receivedAtEpochMillis, capturedAtEpochMillis, captureMethod, state, avatarPath, picturePath) " +
                        "SELECT id, notificationKey, fingerprint, sourcePackage, " +
                        "lower(sourcePackage) || '|' || lower(trim(senderName)), senderName, 0, senderName, messageText, " +
                        "receivedAtEpochMillis, capturedAtEpochMillis, captureMethod, state, NULL, NULL " +
                        "FROM inbox_messages",
                )
                db.execSQL("DROP TABLE inbox_messages")
                db.execSQL("ALTER TABLE inbox_messages_new RENAME TO inbox_messages")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_inbox_messages_fingerprint` ON `inbox_messages` (`fingerprint`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_inbox_messages_sourcePackage` ON `inbox_messages` (`sourcePackage`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_inbox_messages_state` ON `inbox_messages` (`state`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_inbox_messages_conversationKey` ON `inbox_messages` (`conversationKey`)")
            }
        }

        fun build(context: Context): SendeDatabase =
            Room.databaseBuilder(context, SendeDatabase::class.java, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}
