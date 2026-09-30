package app.nokta.list

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
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.NotificationCompat
import kotlin.math.abs

/** Round floating bubble docked to the right edge. Tap opens the list; drag moves it vertically. */
class BubbleService : Service() {
    private lateinit var wm: WindowManager
    private var view: View? = null
    private lateinit var params: WindowManager.LayoutParams

    override fun onBind(i: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundCompat()
        if (!Settings.canDrawOverlays(this)) { stopSelf(); return START_NOT_STICKY }
        if (view == null) addBubble()
        updateBadge()
        return START_STICKY
    }

    private fun startForegroundCompat() {
        val nm = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26)
            nm.createNotificationChannel(NotificationChannel(CH, getString(R.string.bubble_channel), NotificationManager.IMPORTANCE_MIN))
        val open = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        val n: Notification = NotificationCompat.Builder(this, CH)
            .setSmallIcon(R.drawable.ic_notif).setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.bubble_notif)).setContentIntent(open)
            .setPriority(NotificationCompat.PRIORITY_MIN).setOngoing(true).build()
        if (Build.VERSION.SDK_INT >= 29)
            startForeground(1, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        else startForeground(1, n)
    }

    private fun addBubble() {
        wm = getSystemService(WindowManager::class.java)
        val v = LayoutInflater.from(this).inflate(R.layout.bubble, null)
        val size = (56 * resources.displayMetrics.density).toInt()
        params = WindowManager.LayoutParams(
            size, size, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 0
            y = Store.prefs(this@BubbleService).getInt("bubble_y", resources.displayMetrics.heightPixels / 3)
        }
        var startY = 0; var touchY = 0f; var moved = false
        v.setOnTouchListener { _, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> { startY = params.y; touchY = e.rawY; moved = false; true }
                MotionEvent.ACTION_MOVE -> {
                    val dy = e.rawY - touchY
                    if (abs(dy) > 12) moved = true
                    if (moved) {
                        val max = resources.displayMetrics.heightPixels - size
                        params.y = (startY + dy).toInt().coerceIn(0, max)
                        wm.updateViewLayout(v, params)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (moved) Store.prefs(this).edit().putInt("bubble_y", params.y).apply()
                    else startActivity(Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    true
                }
                else -> false
            }
        }
        wm.addView(v, params)
        view = v
    }

    private fun updateBadge() {
        val left = Store.get(this).remaining
        view?.findViewById<TextView>(R.id.badge)?.text = if (left > 0) left.coerceAtMost(99).toString() else "✓"
    }

    override fun onDestroy() {
        view?.let { runCatching { wm.removeView(it) } }
        view = null
        super.onDestroy()
    }

    companion object {
        private const val CH = "bubble"
        fun start(c: Context) { androidx.core.content.ContextCompat.startForegroundService(c, Intent(c, BubbleService::class.java)) }
        fun stop(c: Context) { c.stopService(Intent(c, BubbleService::class.java)) }
        fun update(c: Context) {
            if (Store.prefs(c).getBoolean(Store.KEY_BUBBLE, false) && Settings.canDrawOverlays(c)) start(c)
        }
    }
}
