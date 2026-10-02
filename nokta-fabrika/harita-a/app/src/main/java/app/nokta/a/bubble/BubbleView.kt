package app.nokta.a.bubble

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.content.res.Configuration

/** Klasik View balon (DESIGN.md §4, §8): 52dp daire, iç nokta (+5,-5)dp ofset, kenara yapışır. */
class BubbleView(
    context: Context,
    private val wm: WindowManager,
    private val lp: WindowManager.LayoutParams,
    private val onTap: () -> Unit,
    private val onLongPress: () -> Unit,
    private val onMoved: (side: Int, yFraction: Float) -> Unit,
) : View(context) {

    private val d = resources.displayMetrics.density
    val sizePx = (52 * d).toInt()
    private val margin = (8 * d).toInt()
    private val slop = ViewConfiguration.get(context).scaledTouchSlop
    private val dark = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = if (dark) 0xFFFF6A3A.toInt() else 0xFFE5481C.toInt() }
    private val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = if (dark) 0xFF111312.toInt() else 0xFF181B19.toInt() }
    private var downX = 0f; private var downY = 0f
    private var startX = 0; private var startY = 0
    private var dragging = false
    private var longFired = false
    private val longRun = Runnable {
        if (!dragging) { longFired = true; performHapticFeedback(HapticFeedbackConstants.LONG_PRESS); onLongPress() }
    }

    init {
        elevation = 4 * d
        contentDescription = "nokta"
    }

    override fun onMeasure(w: Int, h: Int) = setMeasuredDimension(sizePx, sizePx)

    override fun onDraw(canvas: Canvas) {
        val c = sizePx / 2f
        canvas.drawCircle(c, c, c, dotPaint)
        canvas.drawCircle(c + 5 * d * 0.6f, c - 5 * d * 0.6f, 7 * d, innerPaint)
    }

    private fun bounds() = wm.currentWindowMetrics.bounds

    /** side: 0 sol, 1 sağ */
    fun placeAt(side: Int, yFraction: Float) {
        val b = bounds()
        lp.x = if (side == 0) margin else b.width() - sizePx - margin
        lp.y = (yFraction * (b.height() - sizePx)).toInt().coerceIn(0, b.height() - sizePx)
        if (isAttachedToWindow) wm.updateViewLayout(this, lp)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = e.rawX; downY = e.rawY; startX = lp.x; startY = lp.y
                dragging = false; longFired = false
                postDelayed(longRun, ViewConfiguration.getLongPressTimeout().toLong() + 200)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = e.rawX - downX; val dy = e.rawY - downY
                if (!dragging && (kotlin.math.abs(dx) > slop || kotlin.math.abs(dy) > slop)) {
                    dragging = true; removeCallbacks(longRun)
                }
                if (dragging) {
                    val b = bounds()
                    lp.x = (startX + dx).toInt().coerceIn(0, b.width() - sizePx)
                    lp.y = (startY + dy).toInt().coerceIn(0, b.height() - sizePx)
                    wm.updateViewLayout(this, lp)
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                removeCallbacks(longRun)
                if (longFired) return true
                if (dragging) snap()
                else if (e.actionMasked == MotionEvent.ACTION_UP) { performClick(); onTap() }
                return true
            }
        }
        return super.onTouchEvent(e)
    }

    private fun snap() {
        val b = bounds()
        val center = lp.x + sizePx / 2
        val side = if (center < b.width() / 2) 0 else 1
        val target = if (side == 0) margin else b.width() - sizePx - margin
        ValueAnimator.ofInt(lp.x, target).apply {
            duration = 200; interpolator = DecelerateInterpolator()
            addUpdateListener { lp.x = it.animatedValue as Int; if (isAttachedToWindow) wm.updateViewLayout(this@BubbleView, lp) }
            start()
        }
        onMoved(side, lp.y.toFloat() / (b.height() - sizePx).coerceAtLeast(1))
    }

    override fun performClick(): Boolean { super.performClick(); return true }
}
