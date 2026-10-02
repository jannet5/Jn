package app.nokta.b.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import app.nokta.b.R

/** Ustu cizili metin: cizgi soldan saga 200ms'de yurur, metin solar. Cok satirda her satira cizilir. */
class StrikeText @JvmOverloads constructor(c: Context, a: AttributeSet? = null) : AppCompatTextView(c, a) {
    private val line = Paint(Paint.ANTI_ALIAS_FLAG).apply { strokeWidth = resources.displayMetrics.density * 1.5f }
    private val ink = ContextCompat.getColor(c, R.color.ink)
    private val muted = ContextCompat.getColor(c, R.color.muted)
    private var p = 0f
    private var anim: ValueAnimator? = null

    fun set(done: Boolean, animate: Boolean) {
        anim?.cancel()
        val to = if (done) 1f else 0f
        if (!animate) { apply(to); return }
        anim = ValueAnimator.ofFloat(p, to).apply {
            duration = 200; interpolator = DecelerateInterpolator()
            addUpdateListener { apply(it.animatedValue as Float) }
            start()
        }
    }

    private fun apply(v: Float) {
        p = v
        setTextColor(blend(ink, muted, v))
        line.color = muted
        invalidate()
    }

    private fun blend(a: Int, b: Int, t: Float): Int {
        fun ch(s: Int) = (((a shr s) and 0xFF) * (1 - t) + ((b shr s) and 0xFF) * t).toInt()
        return (0xFF shl 24) or (ch(16) shl 16) or (ch(8) shl 8) or ch(0)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val l = layout ?: return
        if (p <= 0f) return
        for (i in 0 until l.lineCount) {
            val left = l.getLineLeft(i) + totalPaddingLeft
            val right = l.getLineRight(i) + totalPaddingLeft
            val y = (l.getLineTop(i) + l.getLineBottom(i)) / 2f + totalPaddingTop - paint.textSize * 0.08f
            canvas.drawLine(left, y, left + (right - left) * p, y, line)
        }
    }
}
