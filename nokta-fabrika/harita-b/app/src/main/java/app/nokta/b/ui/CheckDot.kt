package app.nokta.b.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import app.nokta.b.R
import androidx.core.content.ContextCompat

/** Yuvarlak isaret: bos halka -> dolan vurgu dairesi (200ms) + onay isareti. */
class CheckDot @JvmOverloads constructor(c: Context, a: AttributeSet? = null) : View(c, a) {
    private val d = resources.displayMetrics.density
    private val ring = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = 1.5f * d; color = ContextCompat.getColor(c, R.color.muted)
    }
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ContextCompat.getColor(c, R.color.accent) }
    private val tick = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = 2f * d; strokeCap = Paint.Cap.ROUND; strokeJoin = Paint.Join.ROUND
        color = ContextCompat.getColor(c, R.color.on_accent)
    }
    private val path = Path()
    private var p = 0f
    private var anim: ValueAnimator? = null

    fun set(done: Boolean, animate: Boolean) {
        anim?.cancel()
        val to = if (done) 1f else 0f
        if (!animate) { p = to; invalidate(); return }
        anim = ValueAnimator.ofFloat(p, to).apply {
            duration = 200; interpolator = DecelerateInterpolator()
            addUpdateListener { p = it.animatedValue as Float; invalidate() }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val cx = width / 2f; val cy = height / 2f; val r = minOf(width, height) / 2f - ring.strokeWidth
        canvas.drawCircle(cx, cy, r, ring)
        if (p > 0f) {
            canvas.drawCircle(cx, cy, r * p, fill)
            path.rewind()
            path.moveTo(cx - r * 0.42f, cy + r * 0.02f)
            path.lineTo(cx - r * 0.08f, cy + r * 0.36f)
            path.lineTo(cx + r * 0.46f, cy - r * 0.28f)
            tick.alpha = (255 * p).toInt()
            canvas.drawPath(path, tick)
        }
    }
}
