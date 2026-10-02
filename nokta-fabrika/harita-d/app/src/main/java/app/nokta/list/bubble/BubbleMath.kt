package app.nokta.list.bubble

import kotlin.math.hypot

/** Balon yerlesiminin saf hesaplari (WindowManager'siz), birim testle dogrulanir. */
object BubbleMath {
    /** Sag kenara yapisik x: ekran genisligi - balon - kenar bosluğu. */
    fun edgeX(screenW: Int, size: Int, gap: Int) = screenW - size - gap

    fun clampX(x: Int, screenW: Int, size: Int) = x.coerceIn(0, maxOf(0, screenW - size))

    /** Y, ust/alt kenardan [margin] iceride kalir. */
    fun clampY(y: Int, screenH: Int, size: Int, margin: Int): Int {
        val max = maxOf(margin, screenH - size - margin)
        return y.coerceIn(margin, max)
    }

    /** Balon merkezi hedef merkezine [radius] icinde mi? */
    fun overTarget(bx: Float, by: Float, tx: Float, ty: Float, radius: Float) = hypot(bx - tx, by - ty) <= radius
}
