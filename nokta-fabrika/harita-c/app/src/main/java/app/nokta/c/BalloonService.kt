package app.nokta.c

import android.animation.ValueAnimator
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator

/** En basit balon: WindowManager overlay, kenara yapışır, dokununca uygulamayı (listeyi) açar. */
class BalloonService : Service() {
    private var wm: WindowManager? = null
    private var view: BalloonView? = null
    private lateinit var lp: WindowManager.LayoutParams

    override fun onBind(i: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Store.init(this)
        if (intent?.action == ACTION_STOP) {
            prefs(this).edit().putBoolean(KEY_ON, false).apply()
            stopSelf()
            return START_NOT_STICKY
        }
        startFg()
        if (!Settings.canDrawOverlays(this) || !prefs(this).getBoolean(KEY_ON, false)) {
            stopSelf(); return START_NOT_STICKY
        }
        if (view == null) addBalloon()
        return START_STICKY
    }

    private fun startFg() {
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(NotificationChannel(CH, "Balon", NotificationManager.IMPORTANCE_MIN))
        val open = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        val stop = PendingIntent.getService(this, 1, Intent(this, BalloonService::class.java).setAction(ACTION_STOP), PendingIntent.FLAG_IMMUTABLE)
        val n = Notification.Builder(this, CH)
            .setSmallIcon(R.drawable.ic_dot)
            .setContentTitle("nokta")
            .setContentText("Balon açık")
            .setContentIntent(open)
            .addAction(Notification.Action.Builder(Icon.createWithResource(this, R.drawable.ic_dot), "balonu kapat", stop).build())
            .setOngoing(true)
            .build()
        if (Build.VERSION.SDK_INT >= 34) startForeground(1, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        else startForeground(1, n)
    }

    private fun addBalloon() {
        val m = getSystemService(WindowManager::class.java); wm = m
        val v = BalloonView(this); view = v
        val size = v.sizePx
        val dm = resources.displayMetrics
        lp = WindowManager.LayoutParams(
            size, size, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )
        lp.gravity = Gravity.TOP or Gravity.START
        lp.x = dm.widthPixels - size - v.margin
        lp.y = prefs(this).getInt(KEY_Y, dm.heightPixels * 2 / 5).coerceIn(0, dm.heightPixels - size)
        v.setOnClickListener { open() }
        v.onMove = { x, y -> lp.x = x; lp.y = y; m.updateViewLayout(v, lp) }
        v.pos = { lp.x to lp.y }
        v.onRelease = { prefs(this).edit().putInt(KEY_Y, lp.y).apply() }
        try { m.addView(v, lp) } catch (e: Exception) { view = null; stopSelf() }
    }

    private fun open() {
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun onDestroy() {
        view?.let { it.detach(); try { wm?.removeView(it) } catch (_: Exception) {} }
        view = null
        super.onDestroy()
    }

    class BalloonView(ctx: Context) : View(ctx) {
        private val d = resources.displayMetrics.density
        val sizePx = (56 * d).toInt()
        val margin = (6 * d).toInt()
        private val bg = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ctx.getColor(R.color.accent) }
        private val tx = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ctx.getColor(R.color.on_accent); textAlign = Paint.Align.CENTER
            textSize = 20 * d; typeface = Typeface.create("serif", Typeface.NORMAL)
        }
        private val slop = ViewConfiguration.get(ctx).scaledTouchSlop
        var onMove: ((Int, Int) -> Unit)? = null
        var onRelease: (() -> Unit)? = null
        var pos: (() -> Pair<Int, Int>)? = null
        private var dx = 0f; private var dy = 0f; private var sx = 0; private var sy = 0
        private var dragging = false
        private val listener: () -> Unit = { updateDesc(); invalidate() }

        init { updateDesc(); Store.listen(listener) }
        fun detach() { Store.unlisten(listener) }

        private fun updateDesc() { contentDescription = "nokta, ${Store.model.openCount} açık madde. Listeyi aç" }

        override fun onDraw(c: Canvas) {
            val r = width / 2f
            c.drawCircle(r, r, r - 2 * d, bg)
            val n = Store.model.openCount
            c.drawText(if (n > 99) "99+" else n.toString(), r, r - (tx.ascent() + tx.descent()) / 2f, tx)
        }

        override fun onTouchEvent(e: MotionEvent): Boolean {
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> { dx = e.rawX; dy = e.rawY; val p = pos?.invoke() ?: (0 to 0); sx = p.first; sy = p.second; dragging = false }
                MotionEvent.ACTION_MOVE -> {
                    if (!dragging && (Math.abs(e.rawX - dx) > slop || Math.abs(e.rawY - dy) > slop)) dragging = true
                    if (dragging) {
                        val dm = resources.displayMetrics
                        onMove?.invoke((sx + e.rawX - dx).toInt().coerceIn(0, dm.widthPixels - sizePx), (sy + e.rawY - dy).toInt().coerceIn(0, dm.heightPixels - sizePx))
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (!dragging) performClick() else snap()
                    dragging = false
                }
                MotionEvent.ACTION_CANCEL -> if (dragging) snap()
            }
            return true
        }

        private fun snap() {
            val p = pos?.invoke() ?: return
            val dm = resources.displayMetrics
            val toRight = p.first + sizePx / 2 > dm.widthPixels / 2
            val target = if (toRight) dm.widthPixels - sizePx - margin else margin
            ValueAnimator.ofInt(p.first, target).apply {
                duration = 200; interpolator = DecelerateInterpolator()
                addUpdateListener { onMove?.invoke(it.animatedValue as Int, pos?.invoke()?.second ?: p.second) }
                start()
            }
            onRelease?.invoke()
        }

        override fun performClick(): Boolean { return super.performClick() }
    }

    companion object {
        const val CH = "balon"
        const val ACTION_STOP = "app.nokta.c.STOP"
        const val KEY_ON = "balon"
        const val KEY_Y = "balon_y"
        fun prefs(c: Context) = c.getSharedPreferences("nokta", MODE_PRIVATE)
    }
}
