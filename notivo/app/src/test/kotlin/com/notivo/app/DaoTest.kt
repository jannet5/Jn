package com.notivo.app

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.notivo.app.data.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class DaoTest {
    private lateinit var db: Db
    private lateinit var dao: NotiDao
    private val now = 1_000_000_000_000L

    @Before fun setUp() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Application>(), Db::class.java)
            .allowMainThreadQueries().build()
        dao = db.dao()
    }
    @After fun tearDown() = db.close()

    @Test fun markDeletedFlagsOnlyNewestMatchingMessageInWindow() = runBlocking {
        dao.insert(notif(title = "Ayşe", text = "eski", time = now - 3_000_000))
        val newest = dao.insert(notif(title = "Ayşe", text = "yeni", time = now - 1_000))
        dao.insert(notif(title = "Mehmet", text = "başka kişi", time = now - 500))
        dao.insert(notif(pkg = "com.other", title = "Ayşe", text = "başka uygulama", time = now - 400))
        dao.markDeleted("com.chat", "Ayşe", now - 24 * 3_600_000L)
        val rows = dao.allNow()
        assertEquals(listOf(newest), rows.filter { it.deletedBySender }.map { it.id })
        // a second notice flags the next-newest, never the same row twice
        dao.markDeleted("com.chat", "Ayşe", now - 24 * 3_600_000L)
        assertEquals(2, dao.allNow().count { it.deletedBySender })
        // nothing left inside a tiny window
        dao.markDeleted("com.chat", "Ayşe", now)
        assertEquals(2, dao.allNow().count { it.deletedBySender })
    }

    @Test fun duplicateWindowCountsOnlyRecentIdenticalNotifications() = runBlocking {
        dao.insert(notif(text = "kod 1234", time = now - 5_000))
        dao.insert(notif(text = "kod 1234", time = now - 60_000))
        assertEquals(1, dao.dupCount("com.chat", "Ayşe", "kod 1234", now - 10_000))
        assertEquals(0, dao.dupCount("com.chat", "Ayşe", "başka", now - 10_000))
    }

    @Test fun purgeAndClearKeepStarredOnly() = runBlocking {
        dao.insert(notif(text = "eski", time = now - 40 * 86_400_000L))
        dao.insert(notif(text = "eski yıldızlı", time = now - 40 * 86_400_000L, starred = true))
        dao.insert(notif(text = "yeni", time = now))
        dao.purgeOlder(now - 30 * 86_400_000L)
        assertEquals(setOf("eski yıldızlı", "yeni"), dao.allNow().map { it.text }.toSet())
        dao.clearUnstarred()
        assertEquals(listOf("eski yıldızlı"), dao.allNow().map { it.text })
    }

    @Test fun readStarAndCounts() = runBlocking {
        val a = dao.insert(notif(text = "1")); dao.insert(notif(text = "2")); dao.insert(notif(pkg = "com.mail", text = "3"))
        dao.markRead(a); dao.toggleStar(a)
        assertEquals(2, dao.unreadNow())
        val counts = dao.appCounts().first().associateBy { it.pkg }
        assertEquals(2, counts.getValue("com.chat").n); assertEquals(1, counts.getValue("com.chat").unread)
        assertTrue(dao.get(a)!!.starred)
        dao.markAllRead(); assertEquals(0, dao.unreadNow())
        dao.deleteApp("com.chat"); assertEquals(listOf("com.mail"), dao.allNow().map { it.pkg })
    }

    @Test fun rulesExcludedAndSnoozesRoundTrip() = runBlocking {
        dao.addRule(Rule(pkg = "", keyword = "indirim", action = "BLOCK"))
        dao.addRule(Rule(pkg = "com.chat", keyword = "", action = "MUTE", enabled = false))
        assertEquals(1, dao.activeRules().size)
        val off = dao.rules().first().first { it.keyword.isEmpty() }
        dao.toggleRule(off.id); assertEquals(2, dao.activeRules().size)
        dao.exclude(Excluded("com.spam")); assertEquals(listOf("com.spam"), dao.excludedNow())
        dao.include("com.spam"); assertTrue(dao.excludedNow().isEmpty())
        val id = dao.addSnooze(Snooze(at = now, title = "t", text = "x", app = "a"))
        assertEquals(1, dao.snoozes().size); dao.delSnooze(id); assertTrue(dao.snoozes().isEmpty())
    }
}
