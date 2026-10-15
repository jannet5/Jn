package com.notivo.app

import android.app.AlarmManager
import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.notivo.app.data.Db
import com.notivo.app.service.NotivoWidget
import com.notivo.app.service.postSnoozed
import com.notivo.app.ui.Vm
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/** Rakip uygulamalarda bulunan özellikler: yeniden gösterme, kendi hatırlatıcın, ana ekran widget'ı. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class FeaturesTest {
    private val app: Application get() = ApplicationProvider.getApplicationContext()
    private val postNotifications = "android.permission.POST_NOTIFICATIONS"

    @Before fun setUp() {
        resetDbSingleton()
        app.getSharedPreferences("notivo", Context.MODE_PRIVATE).edit().clear().commit()
        shadowOf(app).grantPermissions(postNotifications)
    }
    @After fun tearDown() { resetDbSingleton() }

    @Test fun restoreRepostsWithOriginalTimeTitleAndText() {
        val n = notif(title = "Ayşe", text = "Akşam görüşelim mi?", time = 1_700_000_000_000L, appName = "WhatsApp").copy(id = 7)
        assertTrue(Vm(app).restore(n))
        val posted = shadowOf(app.getSystemService(NotificationManager::class.java)).allNotifications.single()
        assertEquals("Ayşe", posted.extras.getString(Notification.EXTRA_TITLE))
        assertEquals("Akşam görüşelim mi?", posted.extras.getString(Notification.EXTRA_TEXT))
        assertEquals(1_700_000_000_000L, posted.`when`)            // özgün saat korunur
        assertEquals("WhatsApp", posted.extras.getString(Notification.EXTRA_SUB_TEXT))
    }

    @Test fun restoreWithoutNotificationPermissionDoesNothing() {
        shadowOf(app).denyPermissions(postNotifications)
        assertFalse(Vm(app).restore(notif().copy(id = 1)))
        assertTrue(shadowOf(app.getSystemService(NotificationManager::class.java)).allNotifications.isEmpty())
    }

    // Robolectric'te test ana iş parçacığında koşar: join() ile bloke etmek Room'dan dönüşü kilitler, bu yüzden döngüyü boşaltarak bekleriz.
    private fun snoozes() = runBlocking { Db.get(app).dao().snoozes() }

    @Test fun reminderIsStoredAndAlarmArmed() {
        val before = System.currentTimeMillis()
        val vm = Vm(app)
        vm.addReminder("Su iç", "bir bardak", 15)
        awaitUntil { snoozes().size == 1 }
        val rows = snoozes()
        assertEquals("Su iç", rows[0].title); assertEquals("bir bardak", rows[0].text); assertEquals("Hatırlatıcı", rows[0].app)
        assertTrue(rows[0].at >= before + 15 * 60_000L)
        val alarms = shadowOf(app.getSystemService(AlarmManager::class.java))
        awaitUntil { alarms.nextScheduledAlarm != null }              // alarm, kayıttan sonra kurulur
        vm.addReminder("", "başlıksız not", 60)
        awaitUntil { snoozes().size == 2 }
        assertTrue(snoozes().any { it.title == "Hatırlatıcı" })    // boş başlık varsayılana düşer
    }

    @Test fun reminderFiresAsNotification() {
        postSnoozed(app, 5, "Su iç", "bir bardak", "Hatırlatıcı")
        val posted = shadowOf(app.getSystemService(NotificationManager::class.java)).allNotifications.single()
        assertEquals("Su iç", posted.extras.getString(Notification.EXTRA_TITLE))
    }

    @Test fun widgetShowsUnreadCountAndNewestThree() = runBlocking {
        val awm = AppWidgetManager.getInstance(app)
        val id = shadowOf(awm).createWidget(NotivoWidget::class.java, R.layout.widget)
        val dao = Db.get(app).dao()
        dao.insert(notif(pkg = "com.mail", appName = "Gmail", title = "Fatura", text = "x", time = 1_000, read = true))
        dao.insert(notif(pkg = "com.chat", appName = "WhatsApp", title = "Ayşe", text = "y", time = 2_000))
        dao.insert(notif(pkg = "com.chat", appName = "WhatsApp", title = "Mehmet", text = "z", time = 3_000))
        dao.insert(notif(pkg = "com.news", appName = "Haber", title = "", text = "son dakika", time = 4_000))
        NotivoWidget.refreshNow(app)
        val v = shadowOf(awm).getViewFor(id)
        assertEquals("Notivo · 3 okunmamış", v.findViewById<TextView>(R.id.w_header).text.toString())
        assertEquals("Haber: son dakika", v.findViewById<TextView>(R.id.w_l1).text.toString())     // başlık boşsa metin
        assertEquals("WhatsApp: Mehmet", v.findViewById<TextView>(R.id.w_l2).text.toString())
        assertEquals("WhatsApp: Ayşe", v.findViewById<TextView>(R.id.w_l3).text.toString())
    }

    @Test fun emptyWidgetSaysNoNotificationsYet() = runBlocking {
        val awm = AppWidgetManager.getInstance(app)
        val id = shadowOf(awm).createWidget(NotivoWidget::class.java, R.layout.widget)
        NotivoWidget.refreshNow(app)
        assertEquals("Henüz bildirim yok", shadowOf(awm).getViewFor(id).findViewById<TextView>(R.id.w_l1).text.toString())
    }

    @Test fun refreshWithoutPlacedWidgetIsHarmless() = runBlocking {
        Db.get(app).dao().insert(notif())
        NotivoWidget.refreshNow(app)   // widget yok: hata vermemeli
    }
}
