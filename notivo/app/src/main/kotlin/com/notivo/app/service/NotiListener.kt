package com.notivo.app.service

import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.notivo.app.MainActivity
import com.notivo.app.R
import com.notivo.app.data.*
import com.notivo.app.data.Logic
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
            val hit = Logic.matchRule(dao.activeRules(), pkg, title, text)
            if (hit?.action == "BLOCK") { cancelSafe(sbn.key); return@launch }
            if (Logic.isDeletedNotice(text)) {
                dao.markDeleted(pkg, title, System.currentTimeMillis() - 24 * 3_600_000L)
                return@launch
            }
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

const val SNOOZE_CH = "snooze"

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context, i: Intent) {
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val id = i.getLongExtra("id", 0)
                Db.get(c).dao().delSnooze(id)
                postSnoozed(c, id, i.getStringExtra("title").orEmpty(), i.getStringExtra("text").orEmpty(), i.getStringExtra("app").orEmpty())
            } finally { pending.finish() }
        }
    }
}

fun postSnoozed(c: Context, id: Long, title: String, text: String, app: String) {
    val nm = c.getSystemService(NotificationManager::class.java)
    nm.createNotificationChannel(NotificationChannel(SNOOZE_CH, c.getString(R.string.channel_snooze), NotificationManager.IMPORTANCE_HIGH))
    val open = PendingIntent.getActivity(c, 0, Intent(c, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
    val n = NotificationCompat.Builder(c, SNOOZE_CH).setSmallIcon(R.drawable.ic_stat)
        .setContentTitle(title).setContentText(text).setSubText(app).setContentIntent(open).setAutoCancel(true).build()
    if (Build.VERSION.SDK_INT < 33 || c.checkSelfPermission("android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED)
        nm.notify(id.toInt(), n)
}

/** Persists the snooze (survives reboot) and arms an alarm for it. */
suspend fun snooze(c: Context, n: Notif, minutes: Int) {
    val at = System.currentTimeMillis() + minutes * 60_000L
    val s = Snooze(at = at, title = n.title, text = n.text, app = n.appName)
    val id = Db.get(c).dao().addSnooze(s)
    arm(c, s.copy(id = id))
}

fun arm(c: Context, s: Snooze) {
    val pi = PendingIntent.getBroadcast(c, s.id.toInt(), Intent(c, SnoozeReceiver::class.java)
        .putExtra("id", s.id).putExtra("title", s.title).putExtra("text", s.text).putExtra("app", s.app),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    c.getSystemService(AlarmManager::class.java).setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, s.at, pi)
}

/** Re-arms persisted snoozes after a reboot; ones that already elapsed fire immediately. */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context, i: Intent) {
        if (i.action != Intent.ACTION_BOOT_COMPLETED) return
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = Db.get(c).dao()
                dao.snoozes().forEach { s ->
                    if (s.at <= System.currentTimeMillis()) { dao.delSnooze(s.id); postSnoozed(c, s.id, s.title, s.text, s.app) } else arm(c, s)
                }
                Digest.schedule(c)
            } finally { pending.finish() }
        }
    }
}
