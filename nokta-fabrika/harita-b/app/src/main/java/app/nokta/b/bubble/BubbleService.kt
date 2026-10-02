package app.nokta.b.bubble

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
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.WindowManager.LayoutParams as LP
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import app.nokta.b.R
import app.nokta.b.core.Repo
import app.nokta.b.ui.ListController
import app.nokta.b.ui.MainActivity
import kotlin.math.hypot

/**
 * Sag kenara yapisan, iki eksende surukleneilen balon.
 * Yerlesim mantigi (kenar/clamp) [BubbleMath]'te saf fonksiyon olarak ayrildi ve test edildi.
 */
class BubbleService : Service() {
    private lateinit var wm: WindowManager
    private lateinit var repo: Repo
    private var bubble: BubbleView? = null
    private var bubbleLp: LP? = null
    private var trash: TrashView? = null
    private var panel: PanelRoot? = null
    private var panelController: ListController? = null
    private var springX: SpringAnimation? = null
    private var flingY: FlingAnimation? = null
    private val countListener: () -> Unit = { bubble?.setCount(repo.remaining) }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        running = true
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        repo = Repo.get(this)
        repo.addListener(countListener)
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) { setWanted(this, false); stopSelf(); return START_NOT_STICKY }
        if (!Settings.canDrawOverlays(this)) {
            // Izin yoksa yine de startForeground cagrilmali (startForegroundService sozlesmesi).
            goForeground(false)
            stopSelf(); return START_NOT_STICKY
        }
        setWanted(this, true)
        showBubble()
        goForeground(bubble == null)
        return START_STICKY
    }

    private fun goForeground(hidden: Boolean) {
        val n = notification(hidden)
        if (Build.VERSION.SDK_INT >= 34) {
            ServiceCompat.startForeground(this, NOTIF_ID, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else startForeground(NOTIF_ID, n)
    }

    private fun notification(hidden: Boolean): Notification {
        fun pi(action: String, code: Int) = PendingIntent.getService(
            this, code, Intent(this, BubbleService::class.java).setAction(action),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val open = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        val b = NotificationCompat.Builder(this, CHANNEL)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle(getString(if (hidden) R.string.notif_hidden else R.string.notif_title))
            .setContentIntent(open)
            .setOngoing(true)
            .setSilent(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
        if (hidden) b.addAction(0, getString(R.string.notif_show), pi(ACTION_SHOW, 1))
        b.addAction(0, getString(R.string.notif_stop), pi(ACTION_STOP, 2))
        return b.build()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val ch = NotificationChannel(CHANNEL, getString(R.string.notif_channel), NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        }
    }

    // ---- ekran olculeri ----
    private fun screen(): Pair<Int, Int> =
        if (Build.VERSION.SDK_INT >= 30) wm.maximumWindowMetrics.bounds.let { it.width() to it.height() }
        else resources.displayMetrics.let { it.widthPixels to it.heightPixels }

    private val d get() = resources.displayMetrics.density

    private fun overlayParams(w: Int, h: Int, flags: Int) = LP(
        w, h, LP.TYPE_APPLICATION_OVERLAY, flags or LP.FLAG_LAYOUT_NO_LIMITS, PixelFormat.TRANSLUCENT,
    ).apply { gravity = Gravity.TOP or Gravity.START }

    // ---- balon ----
    private fun showBubble() {
        if (bubble != null) return
        val v = BubbleView(this)
        val (sw, sh) = screen()
        val lp = overlayParams(v.size, v.size, LP.FLAG_NOT_FOCUSABLE or LP.FLAG_NOT_TOUCH_MODAL)
        lp.x = BubbleMath.edgeX(sw, v.size, (4 * d).toInt())
        lp.y = BubbleMath.clampY(loadY(sh), sh, v.size, (24 * d).toInt())
        v.setCount(repo.remaining)
        attachTouch(v, lp)
        bubble = v; bubbleLp = lp
        wm.addView(v, lp)
        visible = true
    }

    private fun hideBubbleTemporarily() {
        springX?.cancel(); flingY?.cancel()
        bubble?.let { runCatching { wm.removeView(it) } }
        bubble = null; bubbleLp = null
        visible = false
        getSystemService(NotificationManager::class.java).notify(NOTIF_ID, notification(true))
    }

    private val xProp = object : FloatPropertyCompat<BubbleView>("x") {
        override fun getValue(v: BubbleView) = bubbleLp?.x?.toFloat() ?: 0f
        override fun setValue(v: BubbleView, value: Float) { bubbleLp?.let { it.x = value.toInt(); update(v, it) } }
    }
    private val yProp = object : FloatPropertyCompat<BubbleView>("y") {
        override fun getValue(v: BubbleView) = bubbleLp?.y?.toFloat() ?: 0f
        override fun setValue(v: BubbleView, value: Float) { bubbleLp?.let { it.y = value.toInt(); update(v, it) } }
    }

    private fun update(v: View, lp: LP) { if (v.isAttachedToWindow) wm.updateViewLayout(v, lp) }

    private fun attachTouch(v: BubbleView, lp: LP) {
        val slop = ViewConfiguration.get(this).scaledTouchSlop
        var downX = 0f; var downY = 0f; var startX = 0; var startY = 0
        var dragging = false
        var vt: VelocityTracker? = null
        v.setOnTouchListener { _, e ->
            val (sw, sh) = screen()
            val margin = (24 * d).toInt()
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    springX?.cancel(); flingY?.cancel()
                    downX = e.rawX; downY = e.rawY; startX = lp.x; startY = lp.y; dragging = false
                    vt?.recycle(); vt = VelocityTracker.obtain().also { it.addMovement(e) }
                    v.animate().scaleX(0.92f).scaleY(0.92f).setDuration(100).start()
                }
                MotionEvent.ACTION_MOVE -> {
                    vt?.addMovement(e)
                    val dx = e.rawX - downX; val dy = e.rawY - downY
                    if (!dragging && hypot(dx, dy) > slop) { dragging = true; showTrash(sw, sh) }
                    if (dragging) {
                        lp.x = BubbleMath.clampX(startX + dx.toInt(), sw, v.size)
                        lp.y = BubbleMath.clampY(startY + dy.toInt(), sh, v.size, margin)
                        update(v, lp)
                        trash?.let { t ->
                            val tcx = sw / 2f; val tcy = sh - 48 * d - t.size / 2f
                            t.active = BubbleMath.overTarget(lp.x + v.size / 2f, lp.y + v.size / 2f, tcx, tcy, 56 * d)
                            v.animate().scaleX(if (t.active) 0.75f else 0.92f).scaleY(if (t.active) 0.75f else 0.92f).setDuration(100).start()
                        }
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
                    val over = trash?.active == true
                    hideTrash()
                    if (!dragging) {
                        if (e.actionMasked == MotionEvent.ACTION_UP) { v.performClick(); openPanel() }
                    } else if (over) {
                        hideBubbleTemporarily()
                    } else {
                        vt?.computeCurrentVelocity(1000)
                        settle(v, lp, sw, sh, vt?.yVelocity ?: 0f, margin)
                    }
                    vt?.recycle(); vt = null
                }
            }
            true
        }
    }

    /** Kenara yapisma: X'te yayli (hafif sekme), Y'de atilma hizi ile surtunmeli kayma. */
    private fun settle(v: BubbleView, lp: LP, sw: Int, sh: Int, vy: Float, margin: Int) {
        springX = SpringAnimation(v, xProp, BubbleMath.edgeX(sw, v.size, (4 * d).toInt()).toFloat()).apply {
            spring = SpringForce(BubbleMath.edgeX(sw, v.size, (4 * d).toInt()).toFloat()).apply {
                stiffness = SpringForce.STIFFNESS_MEDIUM; dampingRatio = 0.6f
            }
            start()
        }
        if (kotlin.math.abs(vy) > 200f) {
            flingY = FlingAnimation(v, yProp).setStartVelocity(vy).setFriction(1.6f)
                .setMinValue(margin.toFloat()).setMaxValue((sh - v.size - margin).toFloat())
                .also { it.addEndListener { _, _, _, _ -> saveY(lp.y) } }
            flingY?.start()
        } else saveY(lp.y)
    }

    // ---- kaldirma hedefi ----
    private fun showTrash(sw: Int, sh: Int) {
        if (trash != null) return
        val t = TrashView(this)
        val lp = overlayParams(t.size, t.size, LP.FLAG_NOT_FOCUSABLE or LP.FLAG_NOT_TOUCHABLE)
        lp.x = (sw - t.size) / 2
        lp.y = sh - (48 * d).toInt() - t.size
        t.alpha = 0f
        wm.addView(t, lp)
        t.animate().alpha(1f).setDuration(150).start()
        trash = t
    }

    private fun hideTrash() {
        val t = trash ?: return
        trash = null
        t.animate().alpha(0f).setDuration(120).withEndAction { runCatching { wm.removeView(t) } }.start()
    }

    // ---- liste paneli ----
    private fun openPanel() {
        if (panel != null) return
        val themed = ContextThemeWrapper(this, R.style.Theme_Nokta)
        val root = LayoutInflater.from(themed).inflate(R.layout.panel_overlay, null) as PanelRoot
        val (sw, sh) = screen()
        val card = root.findViewById<View>(R.id.card)
        val w = minOf(sw - (16 * d).toInt(), (400 * d).toInt())
        card.layoutParams = (card.layoutParams as android.widget.FrameLayout.LayoutParams).apply {
            width = w; height = (sh * 0.52f).toInt(); gravity = Gravity.TOP or Gravity.END
            topMargin = (56 * d).toInt(); marginEnd = (8 * d).toInt()
        }
        val lp = LP(
            LP.MATCH_PARENT, LP.MATCH_PARENT, LP.TYPE_APPLICATION_OVERLAY,
            LP.FLAG_LAYOUT_IN_SCREEN or LP.FLAG_LAYOUT_NO_LIMITS, PixelFormat.TRANSLUCENT,
        ).apply { softInputMode = LP.SOFT_INPUT_ADJUST_RESIZE or LP.SOFT_INPUT_STATE_VISIBLE }
        val ctl = ListController(root.findViewById(R.id.list_root), repo, { closePanel() }, R.drawable.ic_close)
        root.onBack = { closePanel() }
        root.setOnClickListener { closePanel() }
        wm.addView(root, lp)
        root.alpha = 0f; card.translationY = -16 * d
        root.animate().alpha(1f).setDuration(150).start()
        card.animate().translationY(0f).setDuration(200).start()
        ctl.attach(); ctl.focusAdd(true)
        panel = root; panelController = ctl
    }

    private fun closePanel() {
        val p = panel ?: return
        panel = null
        panelController?.detach(); panelController = null
        val im = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        im.hideSoftInputFromWindow(p.windowToken, 0)
        p.animate().alpha(0f).setDuration(120).withEndAction { runCatching { wm.removeView(p) } }.start()
    }

    // ---- kalici y konumu ----
    private fun prefs() = getSharedPreferences(PREFS, MODE_PRIVATE)
    private fun loadY(sh: Int) = (prefs().getFloat("bubble_y", 0.4f) * sh).toInt()
    private fun saveY(y: Int) { prefs().edit().putFloat("bubble_y", y.toFloat() / screen().second).apply() }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        val v = bubble ?: return; val lp = bubbleLp ?: return
        val (sw, sh) = screen()
        lp.x = BubbleMath.edgeX(sw, v.size, (4 * d).toInt())
        lp.y = BubbleMath.clampY(loadY(sh), sh, v.size, (24 * d).toInt())
        update(v, lp)
        closePanel()
    }

    override fun onDestroy() {
        running = false; visible = false
        repo.removeListener(countListener)
        springX?.cancel(); flingY?.cancel()
        closePanel(); hideTrash()
        bubble?.let { runCatching { wm.removeView(it) } }
        bubble = null
        super.onDestroy()
    }

    companion object {
        const val ACTION_SHOW = "app.nokta.b.SHOW"
        const val ACTION_STOP = "app.nokta.b.STOP"
        private const val CHANNEL = "bubble"
        private const val NOTIF_ID = 1
        private const val PREFS = "nokta"
        @Volatile var running = false
        @Volatile var visible = false

        fun wantsBubble(c: Context) = c.getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean("bubble_wanted", true)
        fun setWanted(c: Context, v: Boolean) { c.getSharedPreferences(PREFS, MODE_PRIVATE).edit().putBoolean("bubble_wanted", v).apply() }
        fun start(c: Context) {
            c.startForegroundService(Intent(c, BubbleService::class.java).setAction(ACTION_SHOW))
        }
        fun stop(c: Context) {
            setWanted(c, false)
            c.stopService(Intent(c, BubbleService::class.java))
        }
    }
}
