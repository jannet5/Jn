package com.notibox.app.service

import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.notibox.app.MainActivity
import com.notibox.app.R
import com.notibox.app.data.*
import kotlinx.coroutines.*

/** Captures every posted notification into the local DB. Nothing leaves the device. */
class NotiListener : NotificationListenerService() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onListenerConnected() { connected = true; scope.launch { purge() } }
    override fun onListenerDisconnected() { connected = false }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val prefs = Prefs(this)
        if (!prefs.capturing || sbn.packageName == packageName) return
        val n = sbn.notification
        if (prefs.hideOngoing && (n.flags and Notification.FLAG_ONGOING_EVENT != 0)) return
        if (n.flags and Notification.FLAG_GROUP_SUMMARY != 0) return
        if (prefs.hideSensitive && n.visibility == Notification.VISIBILITY_SECRET) return
        val ex = n.extras
        val title = ex.getCharSequence(Notification.EXTRA_TITLE)?.toString().orEmpty()
        val text = (ex.getCharSequence(Notification.EXTRA_BIG_TEXT) ?: ex.getCharSequence(Notification.EXTRA_TEXT))?.toString().orEmpty()
        if (title.isBlank() && text.isBlank()) return
        val pkg = sbn.packageName
        val dao = Db.get(this).dao()
        scope.launch {
            if (pkg in dao.excludedNow()) return@launch
            if (dao.dupCount(pkg, title, text, System.currentTimeMillis() - 10_000) > 0) return@launch
            val hay = "$title $text".lowercase()
            val hit = dao.activeRules().firstOrNull {
                (it.pkg.isEmpty() || it.pkg == pkg) && (it.keyword.isEmpty() || hay.contains(it.keyword.lowercase()))
            }
            if (hit?.action == "BLOCK") { cancelSafe(sbn.key); return@launch }
            val isMsg = n.category == Notification.CATEGORY_MESSAGE || ex.getString(Notification.EXTRA_TEMPLATE)?.contains("Messaging") == true
            val id = dao.insert(Notif(sbnKey = sbn.key, pkg = pkg, appName = label(pkg), title = title, text = text,
                time = sbn.postTime, category = n.category ?: "", isMessage = isMsg))
            if (n.actions != null || n.contentIntent != null) cache[id] = sbn
            if (cache.size > 200) cache.keys.minOrNull()?.let { cache.remove(it) }
            if (hit?.action == "MUTE") cancelSafe(sbn.key)
        }
    }

    private fun cancelSafe(key: String) = try { cancelNotification(key) } catch (_: Exception) {}

    private fun label(pkg: String) = try {
        packageManager.getApplicationLabel(packageManager.getApplicationInfo(pkg, 0)).toString()
    } catch (_: PackageManager.NameNotFoundException) { pkg }

    private suspend fun purge() {
        val d = Prefs(this).retentionDays
        if (d > 0) Db.get(this).dao().purgeOlder(System.currentTimeMillis() - d * 86_400_000L)
    }

    companion object {
        @Volatile var connected = false
        /** Live notification objects (for "open" / quick reply) — valid only while the process lives. */
        val cache = java.util.concurrent.ConcurrentHashMap<Long, StatusBarNotification>()
    }
}

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context, i: Intent) {
        val ch = "snooze"
        val nm = c.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(NotificationChannel(ch, c.getString(R.string.channel_snooze), NotificationManager.IMPORTANCE_HIGH))
        val open = PendingIntent.getActivity(c, 0, Intent(c, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        val n = NotificationCompat.Builder(c, ch).setSmallIcon(R.drawable.ic_stat)
            .setContentTitle(i.getStringExtra("title")).setContentText(i.getStringExtra("text"))
            .setSubText(i.getStringExtra("app")).setContentIntent(open).setAutoCancel(true).build()
        if (Build.VERSION.SDK_INT < 33 || c.checkSelfPermission("android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED)
            nm.notify(i.getLongExtra("id", 0).toInt(), n)
    }
}

fun snooze(c: Context, n: Notif, minutes: Int) {
    val am = c.getSystemService(AlarmManager::class.java)
    val pi = PendingIntent.getBroadcast(c, n.id.toInt(), Intent(c, SnoozeReceiver::class.java)
        .putExtra("id", n.id).putExtra("title", n.title).putExtra("text", n.text).putExtra("app", n.appName),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    val at = System.currentTimeMillis() + minutes * 60_000L
    // inexact-but-doze-friendly alarm: no special permission needed
    am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi)
}
