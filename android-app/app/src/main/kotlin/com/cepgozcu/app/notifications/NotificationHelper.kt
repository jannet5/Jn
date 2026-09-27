package com.cepgozcu.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cepgozcu.app.MainActivity
import com.cepgozcu.app.R
import com.cepgozcu.app.net.protocol.AlertDto
import com.cepgozcu.app.net.protocol.AlertSeverity

/**
 * Two notification channels: a low-importance ongoing "connection status" one for the foreground
 * service, and a default/high-importance one for pushed alerts. Posting an alert notification
 * always respects the runtime POST_NOTIFICATIONS permission (Android 13+) — [NotificationManagerCompat]
 * silently no-ops the post if it was denied, which is the graceful behavior we want here.
 */
object NotificationHelper {
    const val CHANNEL_CONNECTION = "connection_status"
    const val CHANNEL_ALERTS = "alerts"
    const val ONGOING_NOTIFICATION_ID = 1001
    private const val ALERT_NOTIFICATION_ID_BASE = 2000

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        val connectionChannel = NotificationChannel(
            CHANNEL_CONNECTION,
            context.getString(R.string.notif_channel_connection_name),
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = context.getString(R.string.notif_channel_connection_desc)
            setShowBadge(false)
        }
        val alertsChannel = NotificationChannel(
            CHANNEL_ALERTS,
            context.getString(R.string.notif_channel_alerts_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = context.getString(R.string.notif_channel_alerts_desc)
        }
        manager.createNotificationChannels(listOf(connectionChannel, alertsChannel))
    }

    fun ongoingNotification(context: Context, contentText: String) =
        NotificationCompat.Builder(context, CHANNEL_CONNECTION)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(contentText)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(MainActivity.pendingIntent(context))
            .build()

    fun updateOngoing(context: Context, contentText: String) {
        val notification = ongoingNotification(context, contentText)
        runCatching { NotificationManagerCompat.from(context).notify(ONGOING_NOTIFICATION_ID, notification) }
    }

    fun postAlert(context: Context, alert: AlertDto) {
        val importance = when (alert.severity) {
            AlertSeverity.Critical -> NotificationCompat.PRIORITY_HIGH
            AlertSeverity.Warning -> NotificationCompat.PRIORITY_DEFAULT
            AlertSeverity.Info -> NotificationCompat.PRIORITY_LOW
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.alerts_title))
            .setContentText(alert.message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(alert.message))
            .setPriority(importance)
            .setAutoCancel(true)
            .setContentIntent(MainActivity.pendingIntent(context))
            .build()
        runCatching {
            NotificationManagerCompat.from(context).notify(ALERT_NOTIFICATION_ID_BASE + (alert.id % 10_000).toInt(), notification)
        }
    }
}
