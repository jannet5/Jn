package com.sende.app.util

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.sende.app.capture.SendeNotificationListenerService

/**
 * Opens the exact Android screen where the user flips the notification-access switch for this
 * app, falling back step by step so the button always lands somewhere useful:
 * detail page for this listener (API 30+) -> the list of listeners -> this app's info page.
 */
object AccessSettings {

    fun openNotificationAccess(context: Context): Boolean {
        val component = ComponentName(context, SendeNotificationListenerService::class.java)
        val attempts = buildList {
            if (Build.VERSION.SDK_INT >= 30) {
                add(
                    Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS)
                        .putExtra(Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME, component.flattenToString()),
                )
            }
            add(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            add(appInfoIntent(context))
        }
        return attempts.any { start(context, it) }
    }

    /** App-info page, where Android 13+ hides "Allow restricted settings" for sideloaded installs. */
    fun openAppInfo(context: Context): Boolean = start(context, appInfoIntent(context))

    private fun appInfoIntent(context: Context) =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))

    private fun start(context: Context, intent: Intent): Boolean = try {
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        true
    } catch (_: ActivityNotFoundException) {
        false
    } catch (_: SecurityException) {
        false
    }
}
