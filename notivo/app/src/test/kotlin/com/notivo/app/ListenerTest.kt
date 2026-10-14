package com.notivo.app

import android.app.Application
import android.app.Notification
import android.os.Process
import android.os.UserHandle
import android.service.notification.StatusBarNotification
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.notivo.app.data.*
import com.notivo.app.service.NotiListener
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

/** Drives the real NotiListener with synthetic StatusBarNotifications (JVM simulation, not a device). */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class ListenerTest {
    private val ctx: Application get() = ApplicationProvider.getApplicationContext()
    private lateinit var svc: NotiListener
    private lateinit var dao: NotiDao
    private var seq = 0

    @Before fun setUp() {
        resetDbSingleton()
        svc = Robolectric.setupService(NotiListener::class.java)
        dao = Db.get(ctx).dao()
    }
    @After fun tearDown() { svc.onDestroy(); resetDbSingleton() }

    private fun sbn(pkg: String, title: String, text: String, ongoing: Boolean = false, summary: Boolean = false): StatusBarNotification {
        val n = Notification.Builder(ctx, "ch").setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title).setContentText(text).setCategory(Notification.CATEGORY_MESSAGE).build()
        if (ongoing) n.flags = n.flags or Notification.FLAG_ONGOING_EVENT
        if (summary) n.flags = n.flags or Notification.FLAG_GROUP_SUMMARY
        // hidden constructor; pick it by exact signature because reflection order is not stable
        val c = StatusBarNotification::class.java.getDeclaredConstructor(
            String::class.java, String::class.java, Int::class.javaPrimitiveType, String::class.java, Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType, Notification::class.java, UserHandle::class.java, String::class.java, Long::class.javaPrimitiveType)
        c.isAccessible = true
        return c.newInstance(pkg, pkg, ++seq, null, 1000, 1, n, UserHandle.getUserHandleForUid(Process.myUid()), null, System.currentTimeMillis())
    }

    /** Events run FIFO on one worker, so once the sentinel is stored everything posted before it has been processed. */
    private fun flush() {
        svc.onNotificationPosted(sbn("com.sentinel", "s", "s$seq"))
        awaitUntil { runBlocking { dao.allNow().any { it.pkg == "com.sentinel" } } }
    }
    private fun rows() = runBlocking { dao.allNow().filter { it.pkg != "com.sentinel" } }

    @Test fun capturesMessageWithAppTitleAndText() {
        svc.onNotificationPosted(sbn("com.chat", "Ayşe", "Akşam görüşelim mi?"))
        flush()
        val r = rows().single()
        assertEquals("Ayşe", r.title); assertEquals("Akşam görüşelim mi?", r.text); assertTrue(r.isMessage); assertFalse(r.read)
    }

    @Test fun identicalNotificationWithinTenSecondsIsStoredOnce() {
        repeat(3) { svc.onNotificationPosted(sbn("com.chat", "Banka", "Kodunuz 4821")) }
        flush()
        assertEquals(1, rows().size)
    }

    @Test fun blockRuleDropsAndMuteRuleKeeps() {
        runBlocking { dao.addRule(Rule(pkg = "", keyword = "İNDİRİM", action = "BLOCK")); dao.addRule(Rule(pkg = "", keyword = "sessiz", action = "MUTE")) }
        svc.onNotificationPosted(sbn("com.shop", "Büyük indirim", "%50"))
        svc.onNotificationPosted(sbn("com.shop", "Sessiz bildirim", "saklanmalı"))
        flush()
        assertEquals(listOf("Sessiz bildirim"), rows().map { it.title })
    }

    @Test fun excludedAppOngoingSummaryAndPausedCaptureAreSkipped() {
        runBlocking { dao.exclude(Excluded("com.private")) }
        svc.onNotificationPosted(sbn("com.private", "gizli", "x"))
        svc.onNotificationPosted(sbn("com.music", "Çalıyor", "şarkı", ongoing = true))
        svc.onNotificationPosted(sbn("com.chat", "3 yeni mesaj", "özet", summary = true))
        flush()
        assertTrue(rows().isEmpty())
        Prefs(ctx).capturing = false
        svc.onNotificationPosted(sbn("com.chat", "Ayşe", "duraklatıldı"))
        Prefs(ctx).capturing = true
        flush()
        assertTrue(rows().isEmpty())
    }

    @Test fun deletedNoticeFlagsPreviousMessageAndIsNotStored() {
        svc.onNotificationPosted(sbn("com.chat", "Ayşe", "yanlış mesaj"))
        svc.onNotificationPosted(sbn("com.chat", "Ayşe", "This message was deleted"))
        flush()
        val r = rows().single()
        assertEquals("yanlış mesaj", r.text); assertTrue(r.deletedBySender)
    }
}
