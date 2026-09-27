package com.tekpanel.app.capture

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService

/**
 * Some OEM battery managers kill the notification listener binding and don't reliably
 * restore it after boot. [NotificationListenerService.requestRebind] is the documented way
 * to ask the system to rebind; it is a no-op (and safe to call) if access was never granted.
 */
class BootRestartReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        runCatching {
            NotificationListenerService.requestRebind(
                ComponentName(context, TekPanelNotificationListenerService::class.java),
            )
        }
    }
}
