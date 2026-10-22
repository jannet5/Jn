package com.sende.app.capture

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.sende.app.data.prefs.AppPreferences
import com.sende.app.diagnostics.DiagnosticsRecorder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Reads notifications the OS has already decided to show. Only runs once the user has explicitly
 * granted notification access in Android settings; Android refuses to bind this service
 * otherwise, so there is no code-path where Sende captures anything without that grant.
 */
class SendeNotificationListenerService : NotificationListenerService() {

    private val captureCoordinator: CaptureCoordinator by inject()
    private val diagnosticsRecorder: DiagnosticsRecorder by inject()
    private val appPreferences: AppPreferences by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var selectionJob: Job? = null

    override fun onListenerConnected() {
        super.onListenerConnected()
        diagnosticsRecorder.onListenerConnected()
        diagnosticsRecorder.onNotificationAccessState(true)
        connected = true

        // Replay what is already in the shade, and again whenever the user ticks a new app, so
        // a channel picked after a message arrived still shows that message. The duplicate guard
        // and the unique fingerprint index make replaying always safe.
        selectionJob?.cancel()
        selectionJob = serviceScope.launch {
            var previous: Set<String>? = null
            appPreferences.enabledPackages.collect { enabled ->
                val added = if (previous == null) enabled else enabled - previous!!
                previous = enabled
                if (added.isNotEmpty()) {
                    runCatching { activeNotifications }.getOrNull()
                        ?.filter { it.packageName in added }
                        ?.sortedBy { it.postTime }
                        ?.forEach { processSafely(it) }
                }
            }
        }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        connected = false
        selectionJob?.cancel()
        diagnosticsRecorder.onListenerDisconnected()
        // The system unbound us (battery manager, ROM quirk, process restart): ask to be bound again.
        runCatching { requestRebind(android.content.ComponentName(this, SendeNotificationListenerService::class.java)) }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        serviceScope.launch { processSafely(sbn) }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        captureCoordinator.forget(sbn.key)
    }

    private suspend fun processSafely(sbn: StatusBarNotification) {
        try {
            val payload = NotificationExtractor.extract(applicationContext, sbn)
            val live = LiveActionRegistry.fromNotification(sbn.notification)
            captureCoordinator.handleNotification(payload, live)
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            // One broken notification must never stop the listener; the cause stays visible in diagnostics.
            diagnosticsRecorder.onError(sbn.packageName, error)
        }
    }

    override fun onDestroy() {
        connected = false
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object {
        /** True while the system has this listener bound; the strongest proof that access works. */
        @Volatile var connected: Boolean = false
            private set
    }
}
