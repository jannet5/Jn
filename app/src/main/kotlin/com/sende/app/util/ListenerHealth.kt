package com.sende.app.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.service.notification.NotificationListenerService
import com.sende.app.capture.SendeNotificationListenerService

/**
 * The most common complaint about notification apps is "it silently stops capturing after a
 * while": the phone's battery manager kills the listener binding. These helpers let Sende
 * notice that (access granted but not bound) and bring the binding back.
 */
object ListenerHealth {

    /** Asks the system to bind the listener again; on stubborn ROMs the enable/disable toggle forces it. */
    fun restart(context: Context) {
        val component = ComponentName(context, SendeNotificationListenerService::class.java)
        val packageManager = context.packageManager
        runCatching {
            try {
                packageManager.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
            } finally {
                // However the first call ended, the component must be switched back on: a listener left
                // disabled would never capture again.
                runCatching {
                    packageManager.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
                }
            }
        }
        runCatching { NotificationListenerService.requestRebind(component) }
    }

    /** Page where the user lets Sende run in the background (no special permission needed to open it). */
    fun openBatterySettings(context: Context): Boolean {
        val attempts = buildList {
            add(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
            add(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null)))
        }
        return attempts.any { intent ->
            try {
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
