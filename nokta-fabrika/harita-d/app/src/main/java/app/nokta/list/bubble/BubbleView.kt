package app.nokta.list.bubble

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import app.nokta.list.R

/** Yuvarlak balon: vurgu rengi daire, icinde kalan oge sayisi; hepsi tamamsa yalniz nokta. */
class BubbleView(c: Context) : View(c) {
    private val d = resources.displayMetrics.density
    val size = (52 * d).toInt()
    private var count = 0
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ContextCompat.getColor(c, R.color.accent) }
    private val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = 2 * d; color = 0x33000000
    }
    private val label = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(c, R.color.on_accent); textAlign = Paint.Align.CENTER; textSize = 19 * d
        typeface = ResourcesCompat.getFont(c, R.font.plex_medium)
    }

    init { contentDescription = c.getString(R.string.bubble_desc) }

    fun setCount(n: Int) { if (n != count) {
        count = n; invalidate()
        contentDescription = if (n > 0) context.getString(R.string.bubble_desc_fmt, n) else context.getString(R.string.bubble_desc)
    } }

    override fun onMeasure(w: Int, h: Int) = setMeasuredDimension(size, size)

    override fun onDraw(canvas: Canvas) {
        val cx = width / 2f; val cy = height / 2f
        canvas.drawCircle(cx, cy, cx - d, fill)
        canvas.drawCircle(cx, cy, cx - d, rim)
        if (count > 0) {
            val t = if (count > 99) "99+" else count.toString()
            canvas.drawText(t, cx, cy - (label.descent() + label.ascent()) / 2f, label)
        } else {
            canvas.drawCircle(cx, cy, 5 * d, label)
        }
    }

    override fun performClick(): Boolean = super.performClick()
}
