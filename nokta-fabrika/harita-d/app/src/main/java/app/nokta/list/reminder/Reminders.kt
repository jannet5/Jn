package app.nokta.list.reminder

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.nokta.list.R
import app.nokta.list.data.Item
import app.nokta.list.ui.MainActivity

/**
 * Zil isaretiyle kurulan hatirlatmalar: kayit (SharedPreferences), AlarmManager zamanlamasi ve
 * ekranin ustunde acilan (heads-up) bildirim. Her oge icin iki bagimsiz alarm vardir:
 * KIND_EVERY (her N dakikada bir, her calista bir sonrakini kurar) ve KIND_AT (tek seferlik saat).
 */
object Reminders {
    const val ACTION_FIRE = "app.nokta.list.REMIND"
    const val ACTION_DONE = "app.nokta.list.REMIND_DONE"
    const val ACTION_STOP = "app.nokta.list.REMIND_STOP"
    /** Alarmi susturur; tekrar ayari (varsa) surer. */
    const val ACTION_SILENCE = "app.nokta.list.REMIND_SILENCE"
    const val EXTRA_ID = "id"
    const val EXTRA_KIND = "kind"
    const val KIND_EVERY = 0
    const val KIND_AT = 1
    const val CHANNEL = "reminders"
    /** Saatli alarm: alarm sesi, susturulana kadar tekrar eder, ekrani acar. */
    const val CHANNEL_ALARM = "alarm"
    private const val ALARM_RING_MS = 10 * 60_000L
    private const val PREFS = "reminders"
    private const val KEY = "r_"

    private val listeners = LinkedHashSet<() -> Unit>()
    fun addListener(l: () -> Unit) { listeners += l }
    fun removeListener(l: () -> Unit) { listeners -= l }
    private fun changed() { listeners.toList().forEach { it() } }

    private fun prefs(c: Context) = c.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun get(c: Context, id: Long): ReminderPlan? = ReminderPlan.decode(prefs(c).getString(KEY + id, null))
    fun has(c: Context, id: Long): Boolean = prefs(c).contains(KEY + id)

    fun all(c: Context): Map<Long, ReminderPlan> = prefs(c).all.mapNotNull { (k, v) ->
        val id = k.removePrefix(KEY).toLongOrNull() ?: return@mapNotNull null
        ReminderPlan.decode(v as? String)?.let { id to it }
    }.toMap()

    /** Kaydeder ve alarmlari bastan kurar (tekrar sayaci simdiden baslar). Bos plan = kaldir. */
    fun set(c: Context, id: Long, plan: ReminderPlan) {
        if (plan.isEmpty) return clear(c, id)
        prefs(c).edit().putString(KEY + id, plan.encode()).apply()
        schedule(c, id, plan)
        changed()
    }

    fun clear(c: Context, id: Long) {
        cancelAlarm(c, id, KIND_EVERY)
        cancelAlarm(c, id, KIND_AT)
        prefs(c).edit().remove(KEY + id).apply()
        NotificationManagerCompat.from(c).cancel(notifId(id))
        changed()
    }

    /** Tek seferlik alarm caldiktan sonra yalniz onu dusurur; tekrar ayari varsa surer. */
    fun dropAt(c: Context, id: Long) {
        val p = get(c, id) ?: return
        val next = p.copy(atMillis = null)
        if (next.isEmpty) { cancelAlarm(c, id, KIND_EVERY); prefs(c).edit().remove(KEY + id).apply() }
        else prefs(c).edit().putString(KEY + id, next.encode()).apply()
        changed()
    }

    fun scheduleNextEvery(c: Context, id: Long, everyMin: Int) =
        setAlarm(c, id, KIND_EVERY, ReminderPlan.nextEvery(System.currentTimeMillis(), everyMin))

    private fun schedule(c: Context, id: Long, plan: ReminderPlan, now: Long = System.currentTimeMillis()) {
        plan.everyMin?.let { setAlarm(c, id, KIND_EVERY, ReminderPlan.nextEvery(now, it)) } ?: cancelAlarm(c, id, KIND_EVERY)
        // Telefon kapaliyken kacan alarm acilista kisa sure sonra calar.
        plan.atMillis?.let { setAlarm(c, id, KIND_AT, maxOf(it, now + 5_000)) } ?: cancelAlarm(c, id, KIND_AT)
    }

    /**
     * "Zorla durdur" ya da telefon ureticisinin temizleyicisi (Xiaomi vb.) uygulamanin tum alarmlarini siler;
     * Android bunu bildirmez ve PendingIntent kaydi kaldigi icin sorgulanamaz. Bu yuzden uygulama/balon her
     * acildiginda alarmlar kayitli zamanlarina yeniden kurulur (ayni PendingIntent eskisinin yerine gecer).
     * Tekrar sayaci sifirlanmaz; kacirilan zaman birkac saniye sonra calar.
     */
    fun ensureScheduled(c: Context, now: Long = System.currentTimeMillis()) = all(c).forEach { (id, p) ->
        p.everyMin?.let { setAlarm(c, id, KIND_EVERY, maxOf(nextAt(c, id, KIND_EVERY) ?: ReminderPlan.nextEvery(now, it), now + 5_000)) }
        p.atMillis?.let { setAlarm(c, id, KIND_AT, maxOf(it, now + 5_000)) }
    }

    private fun timeKey(id: Long, kind: Int) = "t_${id}_$kind"
    private fun nextAt(c: Context, id: Long, kind: Int): Long? =
        prefs(c).getLong(timeKey(id, kind), 0L).takeIf { it > 0L }

    /** Acilis / guncelleme / izin degisikligi sonrasi tum alarmlari yeniden kurar. */
    fun rescheduleAll(c: Context) = all(c).forEach { (id, p) -> schedule(c, id, p) }

    fun canExact(c: Context): Boolean =
        Build.VERSION.SDK_INT < 31 || c.getSystemService(AlarmManager::class.java).canScheduleExactAlarms()

    /** Izin verilmis olsa da kullanici ayarlardan bildirimleri ya da hatirlatma kanalini kapatmis olabilir. */
    fun canNotify(c: Context): Boolean {
        if (Build.VERSION.SDK_INT >= 33 &&
            c.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return false
        val nm = NotificationManagerCompat.from(c)
        if (!nm.areNotificationsEnabled()) return false
        return listOf(CHANNEL, CHANNEL_ALARM).none { nm.getNotificationChannel(it)?.importance == NotificationManager.IMPORTANCE_NONE }
    }

    /** Izin hic istenmediyse istenebilir; reddedildiyse ya da kapatildiysa yalniz ayarlardan acilir. */
    fun hasNotifPermission(c: Context): Boolean = Build.VERSION.SDK_INT < 33 ||
        c.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    private fun fireIntent(c: Context, id: Long, kind: Int) =
        Intent(c, ReminderReceiver::class.java).setAction(ACTION_FIRE).putExtra(EXTRA_ID, id).putExtra(EXTRA_KIND, kind)
            .setData(android.net.Uri.parse("nokta://remind/$id/$kind"))

    private fun setAlarm(c: Context, id: Long, kind: Int, at: Long) {
        prefs(c).edit().putLong(timeKey(id, kind), at).apply()
        val am = c.getSystemService(AlarmManager::class.java)
        val pi = PendingIntent.getBroadcast(
            c, 0, fireIntent(c, id, kind), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        // Tam zaman izni yoksa (Android 12+) sistem birkac dakika kaydirabilir; uygulama yine calisir.
        if (canExact(c)) am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi)
        else am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi)
    }

    private fun cancelAlarm(c: Context, id: Long, kind: Int) {
        prefs(c).edit().remove(timeKey(id, kind)).apply()
        val pi = PendingIntent.getBroadcast(
            c, 0, fireIntent(c, id, kind), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE,
        ) ?: return
        c.getSystemService(AlarmManager::class.java).cancel(pi)
        pi.cancel()
    }

    fun notifId(id: Long): Int = 1000 + (id % 1_000_000).toInt()

    fun ensureChannels(c: Context) {
        if (Build.VERSION.SDK_INT < 26) return
        val nm = c.getSystemService(NotificationManager::class.java)
        // Yuksek onem = bildirim ekranin ustunde acilir (heads-up), ses ve titresimle.
        if (nm.getNotificationChannel(CHANNEL) == null) nm.createNotificationChannel(
            NotificationChannel(CHANNEL, c.getString(R.string.remind_channel), NotificationManager.IMPORTANCE_HIGH).apply {
                description = c.getString(R.string.remind_channel_desc)
                enableVibration(true)
            },
        )
        // Alarm kanali alarm sesiyle ve alarm ses akisinda calar (sessiz moddan etkilenmez, alarm sesi duzeyini kullanir).
        if (nm.getNotificationChannel(CHANNEL_ALARM) == null) nm.createNotificationChannel(
            NotificationChannel(CHANNEL_ALARM, c.getString(R.string.alarm_channel), NotificationManager.IMPORTANCE_HIGH).apply {
                description = c.getString(R.string.alarm_channel_desc)
                val tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                setSound(tone, AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 800, 600, 800, 600)
                setBypassDnd(true)
            },
        )
    }

    @SuppressLint("MissingPermission") // canNotify ile denetleniyor
    fun show(c: Context, item: Item, kind: Int, plan: ReminderPlan) {
        ensureChannels(c)
        if (!canNotify(c)) return
        val nid = notifId(item.id)
        fun action(action: String) = PendingIntent.getBroadcast(
            c, 0,
            Intent(c, ReminderReceiver::class.java).setAction(action).putExtra(EXTRA_ID, item.id)
                .setData(android.net.Uri.parse("nokta://remind/${item.id}/$action")),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val open = PendingIntent.getActivity(
            c, nid, Intent(c, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val alarm = kind == KIND_AT
        val text = if (!alarm && plan.everyMin != null) c.getString(R.string.remind_every_fmt, plan.everyMin)
        else c.getString(R.string.remind_alarm_now)
        val b = NotificationCompat.Builder(c, if (alarm) CHANNEL_ALARM else CHANNEL)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle(item.text)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(item.text))
            .setPriority(if (alarm) NotificationCompat.PRIORITY_MAX else NotificationCompat.PRIORITY_HIGH)
            .setCategory(if (alarm) NotificationCompat.CATEGORY_ALARM else NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(open)
            .addAction(0, c.getString(R.string.remind_done), action(ACTION_DONE))
            .addAction(0, c.getString(if (alarm) R.string.alarm_dismiss else R.string.remind_stop), action(if (alarm) ACTION_SILENCE else ACTION_STOP))
        if (alarm) {
            // Ekran kapali/kilitliyken alarm ekrani acilir; acikken ust bildirim olarak duser.
            val full = PendingIntent.getActivity(
                c, nid, Intent(c, AlarmActivity::class.java).putExtra(EXTRA_ID, item.id).putExtra(AlarmActivity.EXTRA_TEXT, item.text)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
            b.setFullScreenIntent(full, true).setContentIntent(full).setOngoing(true).setTimeoutAfter(ALARM_RING_MS)
        }
        val n = b.build()
        // Alarm sesi kullanici dokunana kadar tekrar eder.
        if (alarm) n.flags = n.flags or android.app.Notification.FLAG_INSISTENT
        NotificationManagerCompat.from(c).notify(nid, n)
    }
}
