package com.tekpanel.app.capture

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.UserHandle
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.tekpanel.app.data.local.TekPanelDatabase
import com.tekpanel.app.data.prefs.AppPreferences
import com.tekpanel.app.data.repository.InboxRepository
import com.tekpanel.app.diagnostics.DiagnosticsRecorder
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * End-to-end test of the full CAP-04..07 pipeline against a real in-memory Room database and
 * a real DataStore-backed [AppPreferences] (Robolectric gives us a working simulated
 * filesystem), fed with real `android.app.Notification` objects. This is the closest this
 * sandbox can get to "does a WhatsApp-shaped notification really end up in the inbox
 * exactly once" without a physical device or emulator (no KVM available here -- see
 * docs/DECISIONS.md for why an emulator isn't an option in this environment).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class CaptureCoordinatorRobolectricTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    // Robolectric's per-test Context is fresh, but DataStore's underlying file lives under
    // context.filesDir, which is not guaranteed to be wiped between test methods in this
    // class -- an earlier test's setChannelEnabled(false) was observed leaking into a later
    // test's default-enabled assumption. Clearing before each test makes each one hermetic.
    @Before
    fun resetPersistedPreferences() = runBlocking {
        AppPreferences(context).clearAll()
    }

    private fun buildCoordinatorAndRepo(): Pair<CaptureCoordinator, InboxRepository> {
        val db = Room.inMemoryDatabaseBuilder(context, TekPanelDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val repository = InboxRepository(db.inboxMessageDao())
        val coordinator = CaptureCoordinator(
            filterEngine = MessageFilterEngine(),
            duplicateGuard = DuplicateGuard(),
            inboxRepository = repository,
            appPreferences = AppPreferences(context),
            diagnosticsRecorder = DiagnosticsRecorder(),
            liveIntentRegistry = LiveIntentRegistry(),
        )
        return coordinator to repository
    }

    private fun whatsAppNotification(text: String, key: Int): StatusBarNotification {
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Ayşe Yılmaz")
            .setContentText(text)
            .build()
        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(Intent.ACTION_MAIN), PendingIntent.FLAG_IMMUTABLE)
        notification.contentIntent = pendingIntent
        return StatusBarNotification(
            "com.whatsapp",
            "com.whatsapp",
            key,
            "tag$key",
            android.os.Process.myUid(),
            android.os.Process.myPid(),
            0,
            notification,
            UserHandle.getUserHandleForUid(android.os.Process.myUid()),
            System.currentTimeMillis(),
        )
    }

    @Test
    fun `a real WhatsApp-shaped notification lands in the inbox`() = runTest {
        val (coordinator, repository) = buildCoordinatorAndRepo()
        val sbn = whatsAppNotification("Merhaba, siparişim ne zaman gelir?", key = 1)

        val accepted = coordinator.handleNotification(NotificationExtractor.extract(sbn), sbn.notification.contentIntent)

        assertThat(accepted).isNotNull()
        assertThat(repository.countAll()).isEqualTo(1)
    }

    @Test
    fun `re-processing the same notification on listener reconnect does not duplicate it`() = runTest {
        val (coordinator, repository) = buildCoordinatorAndRepo()
        val sbn = whatsAppNotification("Merhaba, siparişim ne zaman gelir?", key = 1)
        val payload = NotificationExtractor.extract(sbn)

        // Simulates onListenerConnected() replaying getActiveNotifications() (CAP-07).
        coordinator.handleNotification(payload, sbn.notification.contentIntent)
        coordinator.handleNotification(payload, sbn.notification.contentIntent)
        coordinator.handleNotification(payload, sbn.notification.contentIntent)

        assertThat(repository.countAll()).isEqualTo(1)
    }

    @Test
    fun `two distinct real notifications both land`() = runTest {
        val (coordinator, repository) = buildCoordinatorAndRepo()

        coordinator.handleNotification(NotificationExtractor.extract(whatsAppNotification("Merhaba", key = 1)), null)
        coordinator.handleNotification(NotificationExtractor.extract(whatsAppNotification("İyi günler, fiyat bilgisi alabilir miyim?", key = 2)), null)

        assertThat(repository.countAll()).isEqualTo(2)
    }

    @Test
    fun `disabling the channel stops new captures without touching existing ones`() = runTest {
        val (coordinator, repository) = buildCoordinatorAndRepo()
        val preferences = AppPreferences(context)

        coordinator.handleNotification(NotificationExtractor.extract(whatsAppNotification("Merhaba", key = 1)), null)
        preferences.setChannelEnabled("whatsapp", enabled = false)
        val secondResult = coordinator.handleNotification(NotificationExtractor.extract(whatsAppNotification("İkinci mesaj", key = 2)), null)

        assertThat(secondResult).isNull()
        assertThat(repository.countAll()).isEqualTo(1)
    }
}
