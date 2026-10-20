package com.tekpanel.app.capture

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Runs only on a real, running Android system (a CI emulator with KVM, or a physical
 * device) -- see .github/workflows/android-ci.yml. Unlike the Robolectric tests in
 * src/test, this proves two things Robolectric structurally cannot:
 *
 * 1. `BIND_NOTIFICATION_LISTENER_SERVICE` + the manifest <service> declaration are wired
 *    correctly enough that the real system NotificationManagerService actually binds
 *    TekPanelNotificationListenerService once access is granted -- a manifest mistake here
 *    would compile fine and pass every Robolectric test, and only ever surface on a real
 *    device (see docs/DECISIONS.md).
 * 2. A `StatusBarNotification` the OS itself constructed and delivered (not one we built by
 *    hand) extracts correctly through [NotificationExtractor].
 *
 * What this still cannot prove: that WhatsApp/Instagram's *actual current* notification
 * shape matches what MessageNormalizer expects -- only installing those real apps on a real
 * device and receiving a real message can do that (see docs/ROADMAP.md "Sıradaki somut
 * adımlar").
 */
@RunWith(AndroidJUnit4::class)
class NotificationListenerRealDeviceTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext

    @Test
    fun notificationListenerServiceBindsOnARealSystemOnceAccessIsGranted() {
        val componentName = android.content.ComponentName(context, TekPanelNotificationListenerService::class.java)
        val flattened = componentName.flattenToString()

        // Grants notification access the way an instrumented test can (the settings key
        // NotificationListenerService reads); requestRebind then nudges the system to bind
        // immediately instead of waiting for its own poll interval.
        instrumentation.uiAutomation.executeShellCommand(
            "settings put secure enabled_notification_listeners $flattened",
        ).close()
        android.service.notification.NotificationListenerService.requestRebind(componentName)

        val granted = pollUntilTrue(timeoutMillis = 15_000) {
            context.packageName in NotificationManagerCompat.getEnabledListenerPackages(context)
        }
        assertThat(granted).isTrue()
    }

    @Test
    fun aRealOsDeliveredNotificationExtractsCorrectly() {
        val channelId = "test-channel"
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            android.app.NotificationChannel(channelId, "Test", NotificationManager.IMPORTANCE_DEFAULT),
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Gerçek Müşteri")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText("Bu bildirim gerçek Android sistemi tarafından oluşturuldu"),
            )
            .build()

        NotificationManagerCompat.from(context).notify(42, notification)

        try {
            val activeSbn = pollForActiveNotification(id = 42, timeoutMillis = 5_000)
            assertThat(activeSbn).isNotNull()

            val payload = NotificationExtractor.extract(context, activeSbn!!)
            val normalized = MessageNormalizer.normalize(payload)

            assertThat(payload.packageName).isEqualTo(context.packageName)
            assertThat(normalized?.conversationTitle).isEqualTo("Gerçek Müşteri")
            assertThat(normalized?.messages?.single()?.messageText).isEqualTo("Bu bildirim gerçek Android sistemi tarafından oluşturuldu")
        } finally {
            NotificationManagerCompat.from(context).cancel(42)
        }
    }

    /**
     * The real thing, end to end, on a real system: the OS binds our listener, a notification posted
     * by another package (the shell, standing in for WhatsApp) is delivered to it, and the message
     * ends up in the real Room database exactly once, grouped under its conversation.
     */
    @Test
    fun aMessageFromAnotherAppFlowsThroughTheRealListenerIntoRoomExactlyOnce() = runBlocking {
        // `cmd notification post --conversation/--message` only exists from Android 11 (API 30).
        org.junit.Assume.assumeTrue(android.os.Build.VERSION.SDK_INT >= 30)
        val koin = org.koin.core.context.GlobalContext.get()
        val repository = koin.get<com.tekpanel.app.data.repository.InboxRepository>()
        val preferences = koin.get<com.tekpanel.app.data.prefs.AppPreferences>()
        val componentName = android.content.ComponentName(context, TekPanelNotificationListenerService::class.java)

        instrumentation.uiAutomation.executeShellCommand("cmd notification allow_listener ${componentName.flattenToString()}").close()
        android.service.notification.NotificationListenerService.requestRebind(componentName)
        assertThat(pollUntilTrue(15_000) { TekPanelNotificationListenerService.connected }).isTrue()

        preferences.setChannelEnabled("com.android.shell", true)
        val tag = "e2e-" + System.currentTimeMillis()
        fun post(vararg messages: String) {
            val args = messages.joinToString(" ") { "--message '$it'" }
            instrumentation.uiAutomation.executeShellCommand(
                "cmd notification post -S messaging --conversation 'Ayse Yilmaz' $args $tag 'x'",
            ).close()
        }

        post("Ayse:randevu almak istiyorum")
        assertThat(pollUntilTrue(15_000) { runBlocking { messagesOf(repository).any { "randevu" in it } } }).isTrue()

        post("Ayse:randevu almak istiyorum") // the very same notification delivered again
        Thread.sleep(2_000)
        assertThat(messagesOf(repository).count { "randevu" in it }).isEqualTo(1)

        // The chat app updates the SAME notification with a second message: it must be stored too.
        post("Ayse:randevu almak istiyorum", "Ayse:cumartesi 14:00 uygun mu")
        assertThat(pollUntilTrue(15_000) { runBlocking { messagesOf(repository).any { "cumartesi" in it } } }).isTrue()
        assertThat(messagesOf(repository).count { "randevu" in it }).isEqualTo(1)

        preferences.setChannelEnabled("com.android.shell", false)
        instrumentation.uiAutomation.executeShellCommand("cmd notification cancel $tag").close()
    }

    private suspend fun messagesOf(repository: com.tekpanel.app.data.repository.InboxRepository): List<String> =
        repository.observeConversations().first().flatMap { it.messages }.map { it.messageText }

    private fun pollUntilTrue(timeoutMillis: Long, intervalMillis: Long = 500, condition: () -> Boolean): Boolean {
        val deadline = System.currentTimeMillis() + timeoutMillis
        while (System.currentTimeMillis() < deadline) {
            if (condition()) return true
            Thread.sleep(intervalMillis)
        }
        return condition()
    }

    private fun pollForActiveNotification(id: Int, timeoutMillis: Long): android.service.notification.StatusBarNotification? {
        val manager = context.getSystemService(NotificationManager::class.java)
        val deadline = System.currentTimeMillis() + timeoutMillis
        while (System.currentTimeMillis() < deadline) {
            manager.activeNotifications.find { it.id == id }?.let { return it }
            Thread.sleep(200)
        }
        return manager.activeNotifications.find { it.id == id }
    }
}
