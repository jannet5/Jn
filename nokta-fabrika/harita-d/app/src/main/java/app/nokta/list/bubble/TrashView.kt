package app.nokta.list.bubble

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import app.nokta.list.R

/** Kaldirma hedefi: surukleme baslayinca altta belirir, balon uzerine gelince buyur ve vurgu rengine doner. */
class TrashView(c: Context) : View(c) {
    private val d = resources.displayMetrics.density
    val size = (72 * d).toInt()
    private val bg = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ink = ContextCompat.getColor(c, R.color.ink)
    private val accent = ContextCompat.getColor(c, R.color.accent)
    private val x = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(c, R.color.bg); style = Paint.Style.STROKE; strokeWidth = 2 * d; strokeCap = Paint.Cap.ROUND
    }
    var active = false
        set(v) {
            if (field == v) return
            field = v
            animate().scaleX(if (v) 1.25f else 1f).scaleY(if (v) 1.25f else 1f).setDuration(150).start()
            invalidate()
        }

    init { contentDescription = c.getString(R.string.trash_desc) }

    override fun onMeasure(w: Int, h: Int) = setMeasuredDimension(size, size)

    override fun onDraw(canvas: Canvas) {
        val c = width / 2f
        bg.color = if (active) accent else ink
        canvas.drawCircle(c, c, c - 4 * d, bg)
        val r = 8 * d
        canvas.drawLine(c - r, c - r, c + r, c + r, x)
        canvas.drawLine(c - r, c + r, c + r, c - r, x)
    }
}
