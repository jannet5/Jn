package com.sende.app.util

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.sende.app.capture.SendeNotificationListenerService

/**
 * Whether the user has granted Sende notification access, checked on demand. Three signals,
 * any of which counts: the framework's direct answer (API 27+), the enabled-listener list, and
 * the listener actually being bound right now. Relying on only one of them is what makes an app
 * keep asking for a permission it already has on some ROMs.
 */
class NotificationAccessChecker(private val context: Context) {

    /** True while the system actually has the listener bound (notifications are being delivered). */
    fun isListenerBound(): Boolean = SendeNotificationListenerService.connected

    fun isGranted(): Boolean {
        if (SendeNotificationListenerService.connected) return true
        if (Build.VERSION.SDK_INT >= 27) {
            val manager = context.getSystemService(NotificationManager::class.java)
            val component = ComponentName(context, SendeNotificationListenerService::class.java)
            if (runCatching { manager.isNotificationListenerAccessGranted(component) }.getOrDefault(false)) return true
        }
        return context.packageName in NotificationManagerCompat.getEnabledListenerPackages(context)
    }
}
