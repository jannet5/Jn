package app.nokta.a.bubble

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import app.nokta.a.MainActivity
import app.nokta.a.R

/** Foreground service (specialUse) + WindowManager overlay balonu. */
class BubbleService : Service() {
    private var wm: WindowManager? = null
    private var bubble: BubbleView? = null

    override fun onBind(i: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            setEnabled(this, false)
            stopSelf()
            return START_NOT_STICKY
        }
        startInForeground()
        if (!Settings.canDrawOverlays(this)) { stopSelf(); return START_NOT_STICKY }
        if (bubble == null) addBubble()
        instance = this
        applyVisibility()
        return START_STICKY
    }

    private fun startInForeground() {
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(
            NotificationChannel(CHANNEL, getString(R.string.channel_name), NotificationManager.IMPORTANCE_MIN)
        )
        val open = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val stop = PendingIntent.getService(
            this, 1, Intent(this, BubbleService::class.java).setAction(ACTION_STOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val n = Notification.Builder(this, CHANNEL)
            .setSmallIcon(R.drawable.ic_stat_dot)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notif_text))
            .setContentIntent(open)
            .setOngoing(true)
            .addAction(Notification.Action.Builder(null, getString(R.string.notif_stop), stop).build())
            .build()
        if (Build.VERSION.SDK_INT >= 34) startForeground(NOTIF_ID, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        else startForeground(NOTIF_ID, n)
    }

    private fun addBubble() {
        val w = getSystemService(WindowManager::class.java)
        val lp = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT,
        ).apply { gravity = Gravity.TOP or Gravity.START }
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val v = BubbleView(
            this, w, lp,
            onTap = {
                startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP))
            },
            onLongPress = {
                Toast.makeText(this, R.string.bubble_hidden, Toast.LENGTH_SHORT).show()
                setEnabled(this, false)
                stopSelf()
            },
            onMoved = { side, y -> prefs.edit().putInt(KEY_SIDE, side).putFloat(KEY_Y, y).apply() },
        )
        v.placeAt(prefs.getInt(KEY_SIDE, 1), prefs.getFloat(KEY_Y, 0.35f))
        w.addView(v, lp)
        wm = w; bubble = v
    }

    fun applyVisibility() {
        bubble?.visibility = if (appVisible) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        bubble?.let { runCatching { wm?.removeView(it) } }
        bubble = null
        if (instance === this) instance = null
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL = "bubble"
        private const val NOTIF_ID = 1
        private const val ACTION_STOP = "app.nokta.a.STOP"
        private const val PREFS = "bubble"
        private const val KEY_ENABLED = "enabled"
        private const val KEY_SIDE = "side"
        private const val KEY_Y = "y"
        @Volatile private var appVisible = false
        @Volatile private var instance: BubbleService? = null

        fun isEnabled(c: Context) = c.getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean(KEY_ENABLED, true)
        fun setEnabled(c: Context, on: Boolean) = c.getSharedPreferences(PREFS, MODE_PRIVATE).edit().putBoolean(KEY_ENABLED, on).apply()
        fun start(c: Context) = c.startForegroundService(Intent(c, BubbleService::class.java))
        fun stop(c: Context) { c.stopService(Intent(c, BubbleService::class.java)) }

        /** Uygulama önde iken balon gizlenir (üstünü örtmesin), arka plana geçince döner. */
        fun setAppVisible(v: Boolean) { appVisible = v; instance?.applyVisibility() }
    }
}
