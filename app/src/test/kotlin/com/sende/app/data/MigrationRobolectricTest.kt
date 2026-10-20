package com.sende.app.data

import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.sende.app.data.local.SendeDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Builds a real version-1 database file from the exact SQL of the shipped v1 schema
 * (app/schemas/.../1.json), then lets Room open it with the v1->v2 migration. Room itself
 * validates the migrated table against the v2 entity and throws if they differ.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class MigrationRobolectricTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    private fun createV1(name: String, vararg inserts: String) {
        val file = context.getDatabasePath(name).apply { parentFile?.mkdirs(); delete() }
        SQLiteDatabase.openOrCreateDatabase(file, null).use { db ->
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS `inbox_messages` (`id` TEXT NOT NULL, `notificationKey` TEXT, `fingerprint` TEXT NOT NULL, " +
                    "`sourcePackage` TEXT NOT NULL, `channelId` TEXT NOT NULL, `senderName` TEXT NOT NULL, `messageText` TEXT NOT NULL, " +
                    "`receivedAtEpochMillis` INTEGER NOT NULL, `capturedAtEpochMillis` INTEGER NOT NULL, `captureMethod` TEXT NOT NULL, " +
                    "`state` TEXT NOT NULL, `canOpenLiveConversation` INTEGER NOT NULL, PRIMARY KEY(`id`))",
            )
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_inbox_messages_notificationKey` ON `inbox_messages` (`notificationKey`)")
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_inbox_messages_fingerprint` ON `inbox_messages` (`fingerprint`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_inbox_messages_channelId` ON `inbox_messages` (`channelId`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_inbox_messages_state` ON `inbox_messages` (`state`)")
            inserts.forEach { db.execSQL(it) }
            db.version = 1
        }
    }

    private fun open(name: String) = Room.databaseBuilder(context, SendeDatabase::class.java, name)
        .addMigrations(SendeDatabase.MIGRATION_1_2)
        .allowMainThreadQueries()
        .build()

    @Test
    fun `v1 to v2 keeps existing messages and derives their conversation`() = runBlocking {
        createV1(
            "mig-a",
            "INSERT INTO inbox_messages VALUES ('m1','k1','fp1','com.whatsapp','whatsapp','Ayşe','Merhaba',1000,1100,'NOTIFICATION','UNREAD',1)",
        )

        val db = open("mig-a")
        val rows = db.inboxMessageDao().getAllOnce()

        assertThat(rows).hasSize(1)
        val row = rows.single()
        assertThat(row.sourcePackage).isEqualTo("com.whatsapp")
        assertThat(row.conversationKey).isEqualTo("com.whatsapp|ayşe")
        assertThat(row.conversationTitle).isEqualTo("Ayşe")
        assertThat(row.messageText).isEqualTo("Merhaba")
        assertThat(row.state).isEqualTo("UNREAD")
        assertThat(row.avatarPath).isNull()
        db.close()
    }

    @Test
    fun `after v2 two rows may share a notification key`() = runBlocking {
        createV1("mig-b")
        val db = open("mig-b")
        val dao = db.inboxMessageDao()

        fun entity(id: String, fp: String) = com.sende.app.data.local.InboxMessageEntity(
            id, "same-key", fp, "com.whatsapp", "c", "Ayşe", false, "Ayşe", "t", 1, 1, "NOTIFICATION", "UNREAD", null, null,
        )
        assertThat(dao.insertIfNew(entity("a", "f1"))).isNotEqualTo(-1L)
        assertThat(dao.insertIfNew(entity("b", "f2"))).isNotEqualTo(-1L)
        assertThat(dao.insertIfNew(entity("c", "f2"))).isEqualTo(-1L) // fingerprint still unique
        assertThat(dao.countAll()).isEqualTo(2)
        db.close()
    }
}
