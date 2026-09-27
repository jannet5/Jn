package com.tekpanel.app.capture

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.tekpanel.app.diagnostics.DiagnosticsRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Reads notifications the OS has already decided to show (spec CAP-04). Only runs once the
 * user has explicitly granted notification access in Android settings (CAP-01); Android
 * refuses to bind this service otherwise, so there is no code-path where TekPanel captures
 * anything without that grant.
 */
class TekPanelNotificationListenerService : NotificationListenerService() {

    private val captureCoordinator: CaptureCoordinator by inject()
    private val diagnosticsRecorder: DiagnosticsRecorder by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onListenerConnected() {
        super.onListenerConnected()
        diagnosticsRecorder.onListenerConnected()
        diagnosticsRecorder.onNotificationAccessState(true)

        // CAP-07: re-processing already-seen notifications on reconnect must not duplicate.
        // The dedupe guard + Room unique indices make this safe to just replay every time.
        serviceScope.launch {
            activeNotifications?.forEach { sbn -> processSafely(sbn) }
        }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        diagnosticsRecorder.onListenerDisconnected()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        serviceScope.launch { processSafely(sbn) }
    }

    private suspend fun processSafely(sbn: StatusBarNotification) {
        runCatching {
            val payload = NotificationExtractor.extract(sbn)
            captureCoordinator.handleNotification(payload, sbn.notification.contentIntent)
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}
