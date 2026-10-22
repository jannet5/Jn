package com.sende.app.capture

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
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
 *    SendeNotificationListenerService once access is granted -- a manifest mistake here
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

    /** A previous run's messages must never satisfy (or break) this run's assertions. */
    @org.junit.Before
    fun cleanInbox() = runBlocking<Unit> {
        org.koin.core.context.GlobalContext.get().get<com.sende.app.data.repository.InboxRepository>().eraseAllMessages()
    }

    @Test
    fun notificationListenerServiceBindsOnARealSystemOnceAccessIsGranted() {
        val componentName = android.content.ComponentName(context, SendeNotificationListenerService::class.java)
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
        // The setting alone proves nothing: the real system must actually have bound the service.
        assertWithMessage("the system never bound SendeNotificationListenerService")
            .that(pollUntilTrue(timeoutMillis = 20_000) { SendeNotificationListenerService.connected }).isTrue()
    }

    @Test
    fun aRealOsDeliveredNotificationExtractsCorrectly() {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            instrumentation.uiAutomation.grantRuntimePermission(context.packageName, "android.permission.POST_NOTIFICATIONS")
        }
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

    /** Runs a shell command and waits for it to finish (closing the pipe early can kill the command). */
    private fun shell(command: String): String =
        android.os.ParcelFileDescriptor.AutoCloseInputStream(instrumentation.uiAutomation.executeShellCommand(command))
            .use { it.readBytes().toString(Charsets.UTF_8) }

    /** Gradle prints only the first line of a failure message, so everything is squeezed onto one. */
    private fun oneLine(text: String): String = text.replace(Regex("\\s+"), " ").trim()

    /** What the app itself decided about the last notification, so a failure explains itself. */
    private fun whatTheAppSaw(): String {
        val koin = org.koin.core.context.GlobalContext.get()
        val snapshot = koin.get<com.sende.app.diagnostics.DiagnosticsRecorder>().snapshot.value
        val posted = oneLine(
            shell("dumpsys notification --noredact").lineSequence()
                .filter { "NotificationRecord" in it && "pkg=$foreignPackage" in it }
                .take(4).joinToString(" // "),
        ).take(600)
        return "app saw: package=${snapshot.lastSeenPackage} decision=${snapshot.lastDecisionReason} " +
            "accepted=${snapshot.totalAccepted} rejected=${snapshot.rejectedByReason} error=${snapshot.lastError} " +
            "connected=${SendeNotificationListenerService.connected} | system records: $posted"
    }

    /**
     * The "other app" that posts customer messages is the shell (`cmd notification post`), which only
     * exists from Android 11. Older systems refuse a notification claiming another package's name
     * (the test runs under the app's own uid), so the cross-app delivery tests are skipped there; the
     * listener-binding and real-OS-notification tests above still run on every API level.
     */
    private val foreignPackage = "com.android.shell"

    private fun requireShellPost() =
        org.junit.Assume.assumeTrue("cmd notification post needs Android 11+", android.os.Build.VERSION.SDK_INT >= 30)

    private fun postFromAnotherApp(tag: String, title: String, text: String): String =
        // UiAutomation.executeShellCommand does not interpret quotes: every argument must be one word.
        shell("cmd notification post -S bigtext -t ${title.replace(' ', '_')} $tag ${text.replace(' ', '-')}")

    private fun cancelFromAnotherApp(tag: String) {
        shell("cmd notification cancel $tag")
    }

    private suspend fun bindListenerAndPick(preferences: com.sende.app.data.prefs.AppPreferences) {
        val componentName = android.content.ComponentName(context, SendeNotificationListenerService::class.java)
        shell("cmd notification allow_listener ${componentName.flattenToString()}")
        android.service.notification.NotificationListenerService.requestRebind(componentName)
        assertThat(pollUntilTrue(20_000) { SendeNotificationListenerService.connected }).isTrue()
        preferences.setChannelEnabled(foreignPackage, true)
    }

    /**
     * The real thing, end to end, on a real system: the OS binds our listener, a notification posted
     * by another package (the shell, standing in for WhatsApp) is delivered to it, and the message
     * ends up in the real Room database exactly once. The SAME notification key then carries a new
     * message (what chat apps do for every new message) and that must be stored too.
     */
    @Test
    fun aSameKeyNotificationUpdateFlowsThroughTheRealListenerIntoRoom() = runBlocking<Unit> {
        requireShellPost()
        val koin = org.koin.core.context.GlobalContext.get()
        val repository = koin.get<com.sende.app.data.repository.InboxRepository>()
        val preferences = koin.get<com.sende.app.data.prefs.AppPreferences>()
        bindListenerAndPick(preferences)

        val tag = "e2e-" + System.currentTimeMillis()
        val log = StringBuilder()
        fun post(text: String) {
            log.append(postFromAnotherApp(tag, "Ayse Yilmaz", text)).append('\n')
        }

        post("randevu almak istiyorum")
        val first = pollUntilTrue(20_000) { runBlocking { messagesOf(repository).any { "randevu" in it } } }
        assertWithMessage("first message never reached Room. ${whatTheAppSaw()} | shell said: ${oneLine(log.toString())}").that(first).isTrue()

        val diagnostics = koin.get<com.sende.app.diagnostics.DiagnosticsRecorder>()
        val duplicatesBefore = diagnostics.snapshot.value.totalDuplicatesSuppressed
        post("randevu almak istiyorum") // the very same notification delivered again
        // Wait for the pipeline to SAY it recognised the repeat, instead of sleeping and hoping.
        assertWithMessage("the repeat was never processed. ${whatTheAppSaw()}")
            .that(pollUntilTrue(20_000) { diagnostics.snapshot.value.totalDuplicatesSuppressed > duplicatesBefore }).isTrue()
        assertThat(messagesOf(repository).count { "randevu" in it }).isEqualTo(1)

        post("cumartesi 14:00 uygun mu") // SAME tag/key, different text
        val second = pollUntilTrue(20_000) { runBlocking { messagesOf(repository).any { "cumartesi" in it } } }
        assertWithMessage("second message with the same key was lost. ${whatTheAppSaw()} | shell said: ${oneLine(log.toString())}").that(second).isTrue()
        assertThat(messagesOf(repository).count { "randevu" in it }).isEqualTo(1)

        preferences.setChannelEnabled(foreignPackage, false)
        cancelFromAnotherApp(tag)
    }

    /** Same flow with a real MessagingStyle notification (needs `cmd notification post --conversation`, Android 11+). */
    @Test
    fun aMessagingStyleNotificationFlowsThroughTheRealListenerIntoRoom() = runBlocking<Unit> {
        requireShellPost()
        val koin = org.koin.core.context.GlobalContext.get()
        val repository = koin.get<com.sende.app.data.repository.InboxRepository>()
        val preferences = koin.get<com.sende.app.data.prefs.AppPreferences>()
        bindListenerAndPick(preferences)

        val tag = "e2m-" + System.currentTimeMillis()
        val out = shell("cmd notification post -S messaging --conversation Mehmet_Demir --message Mehmet:lacivert-var-mi $tag x")
        val arrived = pollUntilTrue(20_000) { runBlocking { messagesOf(repository).any { "lacivert" in it } } }
        val usage = if (arrived) "" else shell("cmd notification post")
        assertWithMessage("messaging notification never reached Room. ${whatTheAppSaw()} | shell said: ${oneLine(out)} | usage: ${oneLine(usage)}").that(arrived).isTrue()

        preferences.setChannelEnabled("com.android.shell", false)
        shell("cmd notification cancel $tag")
    }

    private suspend fun messagesOf(repository: com.sende.app.data.repository.InboxRepository): List<String> =
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
