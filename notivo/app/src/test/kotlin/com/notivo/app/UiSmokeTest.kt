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
import org.robolectric.annotation.Config

/** Renders the real screens under Robolectric: catches crash-on-open and wiring mistakes (not pixel/device behaviour). */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp-xxhdpi")   // a normal phone-sized window
class UiSmokeTest {
    @get:Rule val rule = createComposeRule()
    private val app: Application get() = ApplicationProvider.getApplicationContext()

    @Before fun setUp() { resetDbSingleton() }
    @After fun tearDown() { resetDbSingleton() }

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
}
