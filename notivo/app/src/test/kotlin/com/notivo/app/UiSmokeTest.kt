package com.notivo.app

import android.app.Application
import android.provider.Settings
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.notivo.app.data.Db
import com.notivo.app.ui.App
import com.notivo.app.ui.NotiTheme
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/** Renders the real screens under Robolectric: catches crash-on-open and wiring mistakes (not pixel/device behaviour). */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp-xxhdpi")   // a normal phone-sized window
class UiSmokeTest {
    @get:Rule val rule = createComposeRule()
    private val app: Application get() = ApplicationProvider.getApplicationContext()

    @Before fun setUp() {
        resetDbSingleton()
        Settings.Secure.putString(app.contentResolver, "enabled_notification_listeners", "")   // testler arasında erişim izni sızmasın
        app.getSharedPreferences("notivo", android.content.Context.MODE_PRIVATE).edit().clear().commit()   // view choice must not leak between tests
    }
    @After fun tearDown() { resetDbSingleton() }

    /** Robolectric tercihleri testler arasında taşıyabilir: sıralama çipi "eski → yeni" ise geri çevir. */
    private fun ensureNewestFirst() {
        if (rule.onAllNodesWithText("Sıra: eski → yeni").fetchSemanticsNodes().isNotEmpty()) rule.onNodeWithText("Sıra: eski → yeni").performClick()
    }

    private fun enableListener() {
        Settings.Secure.putString(app.contentResolver, "enabled_notification_listeners",
            "com.notivo.app/com.notivo.app.service.NotiListener")
    }

    @Test fun withoutNotificationAccessTheOnboardingIsShown() {
        rule.setContent { NotiTheme { App() } }
        rule.onNodeWithText("Bildirim erişimini aç").assertIsDisplayed()
    }

    @Test fun inboxShowsStoredNotificationsGroupsByDayAndSwitchesTabs() {
        enableListener()
        runBlocking {
            val dao = Db.get(app).dao()
            dao.insert(notif(title = "Ayşe", text = "Akşam görüşelim mi?"))
            dao.insert(notif(title = "Ayşe", text = "Geliyorum", time = System.currentTimeMillis() - 1000))
            dao.insert(notif(pkg = "com.mail", title = "Fatura", text = "Ekstreniz hazır", time = System.currentTimeMillis() - 3 * 86_400_000L))
        }
        rule.setContent { NotiTheme { App() } }
        rule.waitUntil(5000) { rule.onAllNodesWithText("Akşam görüşelim mi?").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("Zamana göre").performClick()
        rule.onNodeWithText("Bugün").assertIsDisplayed()
        rule.onNodeWithText("Bu hafta").assertIsDisplayed()
        rule.onNodeWithText("Ekstreniz hazır").assertIsDisplayed()
        rule.onNodeWithText("Bildirimlerde ara").performTextInput("fatura")
        rule.waitUntil(5000) { rule.onAllNodesWithText("Akşam görüşelim mi?").fetchSemanticsNodes().isEmpty() }
        rule.onNodeWithText("Ekstreniz hazır").assertIsDisplayed()
        for (tab in listOf("Uygulamalar", "Kurallar", "İstatistik", "Ayarlar", "Gelen")) {
            rule.onAllNodesWithText(tab).onFirst().performClick()
            rule.waitForIdle()
        }
    }

    @Test fun appGroupViewShowsOneHeaderPerAppAndExpands() {
        enableListener()
        val now = System.currentTimeMillis()
        runBlocking {
            val dao = Db.get(app).dao()
            (1..5).forEach { dao.insert(notif(pkg = "com.whatsapp", appName = "WhatsApp", title = "Ayşe", text = "wa mesaj $it", time = now - it * 1000L)) }
            dao.insert(notif(pkg = "com.mail", appName = "Gmail", title = "Fatura", text = "mail içerik", time = now - 60_000))
        }
        rule.setContent { NotiTheme { App() } }
        // Robolectric leaks the stored view choice between tests, so pick the grouped view the way a user does.
        rule.waitUntil(5000) { rule.onAllNodesWithText("Uygulamaya göre").fetchSemanticsNodes().isNotEmpty() }
        ensureNewestFirst()
        rule.onNodeWithText("Uygulamaya göre").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithText("5 bildirim", substring = true).fetchSemanticsNodes().isNotEmpty() }
        // one header per app with counts
        rule.onNodeWithText("WhatsApp").assertIsDisplayed()           // exactly one header: rows inside the group do not repeat the app name
        rule.onNodeWithText("Gmail").assertIsDisplayed()
        rule.onNodeWithText("5 bildirim", substring = true).assertIsDisplayed()
        // collapsed: newest three of five shown, the rest behind "2 daha göster"
        rule.onNodeWithText("wa mesaj 1").assertIsDisplayed()
        rule.onNodeWithText("wa mesaj 3").assertIsDisplayed()
        rule.onAllNodesWithText("wa mesaj 4").assertCountEquals(0)
        rule.onNodeWithText("2 daha göster").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithText("wa mesaj 5").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("wa mesaj 5").assertIsDisplayed()
        // switching to conversations leaves the app groups
        rule.onNodeWithText("Konuşmalar").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithText("5 bildirim", substring = true).fetchSemanticsNodes().isEmpty() }
    }

    @Test fun menuOffersCopyReadAloudAndRestore() {
        enableListener()
        runBlocking { Db.get(app).dao().insert(notif(title = "Ayşe", text = "Akşam görüşelim mi?", appName = "WhatsApp")) }
        shadowOf(app).grantPermissions("android.permission.POST_NOTIFICATIONS")
        rule.setContent { NotiTheme { App() } }
        rule.waitUntil(5000) { rule.onAllNodesWithText("Zamana göre").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("Zamana göre").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithText("Akşam görüşelim mi?").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("Akşam görüşelim mi?").performTouchInput { longClick() }
        rule.waitUntil(5000) { rule.onAllNodesWithText("Metni kopyala").fetchSemanticsNodes().isNotEmpty() }
        rule.mainClock.advanceTimeBy(1500)           // alt sayfanın açılış animasyonu bitsin; yoksa tıklama ekran dışına düşer
        rule.onNodeWithText("Sesli oku").assertExists()
        rule.onNodeWithText("Bildirim çubuğunda yeniden göster").assertExists()
        rule.onNodeWithText("Metni kopyala").performSemanticsAction(androidx.compose.ui.semantics.SemanticsActions.OnClick)
        try { awaitUntil { org.robolectric.shadows.ShadowToast.getTextOfLatestToast() == "Kopyalandı" } } catch (e: IllegalStateException) { throw AssertionError("toast=" + org.robolectric.shadows.ShadowToast.getTextOfLatestToast() + " clip=" + app.getSystemService(android.content.ClipboardManager::class.java).primaryClip) }
        rule.waitUntil(5000) { rule.onAllNodesWithText("Metni kopyala").fetchSemanticsNodes().isEmpty() }
        rule.onNodeWithText("Akşam görüşelim mi?").performTouchInput { longClick() }
        rule.waitUntil(5000) { rule.onAllNodesWithText("Bildirim çubuğunda yeniden göster").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("Bildirim çubuğunda yeniden göster").performSemanticsAction(androidx.compose.ui.semantics.SemanticsActions.OnClick)
        awaitUntil { shadowOf(app.getSystemService(android.app.NotificationManager::class.java)).allNotifications.isNotEmpty() }
        val posted = shadowOf(app.getSystemService(android.app.NotificationManager::class.java)).allNotifications
        org.junit.Assert.assertEquals(1, posted.size)
        org.junit.Assert.assertEquals("Ayşe", posted[0].extras.getString(android.app.Notification.EXTRA_TITLE))
    }

    @Test fun dateRangeChipsFilterAndSortChipFlipsOrder() {
        enableListener()
        val now = System.currentTimeMillis()
        runBlocking {
            val dao = Db.get(app).dao()
            dao.insert(notif(title = "Eski", text = "eski haber", time = now - 3 * 86_400_000L))
            dao.insert(notif(title = "Yeni", text = "yeni haber", time = now - 1_000))
        }
        rule.setContent { NotiTheme { App() } }
        rule.waitUntil(5000) { rule.onAllNodesWithText("Zamana göre").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("Zamana göre").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithText("yeni haber").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("eski haber").assertExists()
        rule.onAllNodesWithText("haber", substring = true)[0].assertTextContains("yeni haber", substring = true)   // yeni → eski
        rule.onNodeWithText("Sıra: yeni → eski").performClick()
        rule.onNodeWithText("Sıra: eski → yeni").assertExists()
        rule.waitUntil(5000) { rule.onAllNodesWithText("haber", substring = true)[0].fetchSemanticsNode().config.toString().contains("eski haber") }
        rule.onNodeWithText("Bugünkü").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithText("eski haber").fetchSemanticsNodes().isEmpty() }
        rule.onNodeWithText("yeni haber").assertExists()
        rule.onNodeWithText("Tüm zaman").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithText("eski haber").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithText("7 gün").performClick()
        rule.onNodeWithText("eski haber").assertExists()
        rule.onNodeWithText("Dünkü").performClick()
        rule.waitUntil(5000) { rule.onAllNodesWithText("yeni haber").fetchSemanticsNodes().isEmpty() }
        rule.onNodeWithText("Tüm zaman").performClick()
        ensureNewestFirst()   // sonraki testlere tercih bırakma
    }

    @Test fun reminderDialogSavesAReminder() {
        enableListener()
        rule.setContent { NotiTheme { App() } }
        rule.waitUntil(5000) { rule.onAllNodesWithContentDescription("Hatırlatıcı ekle").fetchSemanticsNodes().isNotEmpty() }
        rule.onNodeWithContentDescription("Hatırlatıcı ekle").performClick()
        rule.mainClock.advanceTimeBy(1500)
        rule.onNodeWithText("Kaydet").assertIsNotEnabled()               // boşken kaydedilemez
        rule.onNodeWithText("Başlık").performTextInput("Su iç")
        rule.onNodeWithText("3 saat").performSemanticsAction(androidx.compose.ui.semantics.SemanticsActions.OnClick)
        rule.onNodeWithText("Kaydet").performSemanticsAction(androidx.compose.ui.semantics.SemanticsActions.OnClick)
        awaitUntil { runBlocking { Db.get(app).dao().snoozes() }.isNotEmpty() }
        val s = runBlocking { Db.get(app).dao().snoozes() }.single()
        org.junit.Assert.assertEquals("Su iç", s.title)
        org.junit.Assert.assertTrue(s.at - System.currentTimeMillis() in (179 * 60_000L)..(181 * 60_000L))
    }
}
