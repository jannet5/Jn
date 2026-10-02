package app.nokta.c

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeProvider
import android.view.animation.DecelerateInterpolator
import android.widget.OverScroller

/**
 * Tüm liste tek View: Canvas çizimi, dokunma, sürükle-sırala, üstünü çizme, silme, animasyon.
 * Satır: [onay 52dp][metin, çok satır][tutamaç 48dp][sil 48dp].
 */
class NoktaListView(ctx: Context) : View(ctx) {
    private class Row(var item: Item) {
        var layout: StaticLayout? = null
        var layoutW = 0
        var h = 0f
        var fromY = 0f; var toY = 0f
        var fromS = 0f; var toS = 0f   // üstü çizili ilerlemesi
        var fromA = 1f; var toA = 1f   // saydamlık
    }

    private val d = resources.displayMetrics.density
    private fun dp(v: Float) = v * d
    private fun sp(v: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)

    private val cInk = ctx.getColor(R.color.ink)
    private val cAccent = ctx.getColor(R.color.accent)
    private val cOnAccent = ctx.getColor(R.color.on_accent)
    private val cBg = ctx.getColor(R.color.bg)

    private val text = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { textSize = sp(17f); color = cInk }
    private val empty = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { textSize = sp(15f); color = cInk; alpha = 150 }
    private val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeCap = Paint.Cap.ROUND; strokeWidth = dp(1.5f) }
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val check = Path()

    private val rowMin = dp(56f)
    private val padV = dp(14f)
    private val textLeft = dp(56f)
    private val rightZones = dp(96f)
    private val maxLines = 6

    private var rows = ArrayList<Row>()
    private val ghosts = ArrayList<Row>()
    private var t = 1f
    private var suppress = false
    private fun stopAnim() { suppress = true; anim.cancel(); suppress = false }
    private val anim = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 180
        interpolator = DecelerateInterpolator()
        addUpdateListener { t = it.animatedValue as Float; invalidate() }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(a: Animator) { if (!suppress && t >= 1f) finishAnim() }
        })
    }

    private var contentH = 0f
    private val scroller = OverScroller(ctx)
    private var vt: VelocityTracker? = null
    private val slop = ViewConfiguration.get(ctx).scaledTouchSlop
    private val minFling = ViewConfiguration.get(ctx).scaledMinimumFlingVelocity
    private val maxFling = ViewConfiguration.get(ctx).scaledMaximumFlingVelocity

    // dokunma durumu
    private var downX = 0f; private var downY = 0f
    private var lastY = 0f
    private var downRow: Row? = null
    private var downZone = 0
    private var scrolling = false
    private var dragRow: Row? = null
    private var dragOrig = -1
    private var dragY = 0f
    private var grabOff = 0f
    private var fingerY = 0f
    private val longPress = Runnable { downRow?.let { startDrag(it) } }

    private val storeListener: () -> Unit = { sync(true) }

    init {
        isFocusable = true
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        contentDescription = "nokta listesi"
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Store.listen(storeListener)
        sync(false)
    }

    override fun onDetachedFromWindow() {
        Store.unlisten(storeListener)
        removeCallbacks(longPress)
        removeCallbacks(autoScroll)
        stopAnim()
        super.onDetachedFromWindow()
    }

    // ---------- model <-> satırlar ----------

    private fun dispY(r: Row) = r.fromY + (r.toY - r.fromY) * t
    private fun dispS(r: Row) = r.fromS + (r.toS - r.fromS) * t
    private fun dispA(r: Row) = r.fromA + (r.toA - r.fromA) * t

    private fun freeze(r: Row) { r.fromY = dispY(r); r.fromS = dispS(r); r.fromA = dispA(r) }

    private fun sync(animate: Boolean) {
        if (dragRow != null) endDrag(false)
        stopAnim()
        val items = Store.model.items
        val old = HashMap<Long, Row>()
        for (r in rows) old[r.item.id] = r
        val next = ArrayList<Row>(items.size)
        val keep = HashSet<Long>()
        for (it in items) {
            val r = old[it.id]
            if (r != null) {
                freeze(r)
                if (r.item.text != it.text) r.layout = null
                r.item = it
                r.toS = if (it.done) 1f else 0f
                next.add(r); keep.add(it.id)
            } else {
                val n = Row(it)
                n.fromS = if (it.done) 1f else 0f; n.toS = n.fromS
                n.fromA = if (animate) 0f else 1f; n.toA = 1f
                next.add(n)
            }
        }
        for (r in rows) if (r.item.id !in keep) { freeze(r); r.toA = 0f; r.toY = r.fromY; ghosts.add(r) }
        ghosts.removeAll { it.item.id in keep }
        rows = next
        t = 1f
        relayout(animate)
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
        contentDescription = if (rows.isEmpty()) "nokta listesi, boş" else "nokta listesi, ${rows.size} madde"
    }

    private fun ensureLayout(r: Row, w: Int) {
        if (r.layout != null && r.layoutW == w) return
        val tw = (w - textLeft - rightZones - dp(4f)).toInt().coerceAtLeast(dp(40f).toInt())
        val l = StaticLayout.Builder.obtain(r.item.text, 0, r.item.text.length, text, tw)
            .setMaxLines(maxLines).setEllipsize(TextUtils.TruncateAt.END).setIncludePad(false).build()
        r.layout = l; r.layoutW = w
        r.h = maxOf(rowMin, l.height + 2 * padV)
    }

    private fun relayout(animate: Boolean) {
        if (width == 0) return
        var y = 0f
        for (r in rows) {
            ensureLayout(r, width)
            r.fromY = if (animate) (if (r.fromA == 0f && r.toA == 1f) y else r.fromY) else y
            r.toY = y
            y += r.h
        }
        for (g in ghosts) ensureLayout(g, width)
        contentH = y + dp(8f)
        clampScroll()
        if (animate) { t = 0f; anim.start() } else { stopAnim(); for (r in rows) { r.fromY = r.toY; r.fromS = r.toS; r.fromA = r.toA }; ghosts.clear(); t = 1f }
        invalidate()
    }

    private fun finishAnim() {
        for (r in rows) { r.fromY = r.toY; r.fromS = r.toS; r.fromA = r.toA }
        ghosts.clear()
        t = 1f
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        super.onSizeChanged(w, h, ow, oh)
        for (r in rows) r.layout = null
        relayout(false)
    }

    private fun maxScroll() = maxOf(0f, contentH - height).toInt()
    private fun clampScroll() { val s = scrollY.coerceIn(0, maxScroll()); if (s != scrollY) scrollTo(0, s) }
    fun scrollToTop() { scroller.abortAnimation(); scrollTo(0, 0) }

    // ---------- çizim ----------

    override fun onDraw(c: Canvas) {
        if (rows.isEmpty() && ghosts.isEmpty()) {
            val msg = "Henüz nokta yok. Yukarıya yaz."
            val l = StaticLayout.Builder.obtain(msg, 0, msg.length, empty, (width - dp(40f)).toInt().coerceAtLeast(1)).build()
            c.save(); c.translate(dp(20f), dp(24f) + scrollY); l.draw(c); c.restore()
            return
        }
        val top = scrollY.toFloat(); val bot = top + height
        for (g in ghosts) drawRow(c, g, dispY(g), 1f, top, bot)
        for (r in rows) if (r !== dragRow) drawRow(c, r, dispY(r), 1f, top, bot)
        dragRow?.let { drawRow(c, it, dragY, 1.02f, top, bot, true) }
    }

    private fun drawRow(c: Canvas, r: Row, y: Float, scale: Float, top: Float, bot: Float, lifted: Boolean = false) {
        if (y + r.h < top || y > bot) return
        val l = r.layout ?: return
        val a = if (r === dragRow) 1f else dispA(r).coerceIn(0f, 1f)
        val s = dispS(r)
        val w = width.toFloat()
        c.save()
        if (scale != 1f) c.scale(scale, scale, w / 2f, y + r.h / 2f)
        if (lifted) {
            fill.color = cInk; fill.alpha = 18
            c.drawRect(0f, y + dp(2f), w, y + r.h + dp(2f), fill)
            fill.color = cBg; fill.alpha = 255
            c.drawRect(0f, y, w, y + r.h, fill)
        } else {
            stroke.color = cInk; stroke.alpha = (30 * a).toInt(); stroke.strokeWidth = dp(1f)
            c.drawLine(textLeft, y + r.h - dp(0.5f), w, y + r.h - dp(0.5f), stroke)
        }
        val textTop = y + (r.h - l.height) / 2f
        val cy = textTop + (l.getLineBottom(0) - l.getLineTop(0)) / 2f
        val cx = dp(28f)
        // onay dairesi
        stroke.strokeWidth = dp(1.5f)
        stroke.color = cInk; stroke.alpha = (115 * a * (1 - s)).toInt()
        if (s < 1f) c.drawCircle(cx, cy, dp(11f), stroke)
        if (s > 0f) {
            fill.color = cAccent; fill.alpha = (255 * a * s).toInt()
            c.drawCircle(cx, cy, dp(11f), fill)
            check.rewind()
            check.moveTo(cx - dp(4.5f), cy + dp(0.5f)); check.lineTo(cx - dp(1.2f), cy + dp(3.8f)); check.lineTo(cx + dp(4.8f), cy - dp(3f))
            stroke.color = cOnAccent; stroke.alpha = (255 * a * s).toInt()
            c.drawPath(check, stroke)
        }
        // metin
        text.color = cInk; text.alpha = (255 * a * (1f - 0.55f * s)).toInt()
        c.save(); c.translate(textLeft, textTop); l.draw(c)
        if (s > 0f) {
            stroke.color = cInk; stroke.alpha = (170 * a).toInt(); stroke.strokeWidth = dp(1.5f)
            for (i in 0 until l.lineCount) {
                val ly = l.getLineBaseline(i) - text.textSize * 0.3f
                val x0 = l.getLineLeft(i)
                c.drawLine(x0, ly, x0 + (l.getLineRight(i) - x0) * s, ly, stroke)
            }
        }
        c.restore()
        // tutamaç (3 çizgi)
        stroke.color = cInk; stroke.alpha = (115 * a).toInt(); stroke.strokeWidth = dp(1.5f)
        val hx = w - dp(72f)
        for (k in -1..1) c.drawLine(hx - dp(7f), cy + k * dp(4.5f), hx + dp(7f), cy + k * dp(4.5f), stroke)
        // sil (x)
        val dx = w - dp(24f); val q = dp(5.5f)
        c.drawLine(dx - q, cy - q, dx + q, cy + q, stroke)
        c.drawLine(dx - q, cy + q, dx + q, cy - q, stroke)
        c.restore()
    }

    // ---------- dokunma ----------

    private fun rowAt(cy: Float): Row? {
        for (r in rows) { val y = if (r === dragRow) dragY else dispY(r); if (cy >= y && cy < y + r.h) return r }
        return null
    }

    private fun zone(x: Float): Int = when {
        x >= width - dp(48f) -> 3          // sil
        x >= width - rightZones -> 2       // tutamaç
        else -> 1                          // onay + metin
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (vt == null) vt = VelocityTracker.obtain()
        vt!!.addMovement(e)
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scroller.abortAnimation()
                downX = e.x; downY = e.y; lastY = e.y; scrolling = false
                downRow = rowAt(e.y + scrollY); downZone = zone(e.x)
                parent?.requestDisallowInterceptTouchEvent(true)
                val r = downRow
                if (r != null) {
                    if (downZone == 2) startDrag(r)
                    else postDelayed(longPress, ViewConfiguration.getLongPressTimeout().toLong())
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (dragRow != null) {
                    fingerY = e.y; updateDrag()
                } else {
                    if (!scrolling && Math.abs(e.y - downY) > slop) { scrolling = true; removeCallbacks(longPress) }
                    if (scrolling) {
                        val ns = (scrollY + (lastY - e.y)).toInt().coerceIn(0, maxScroll())
                        scrollTo(0, ns)
                    }
                }
                lastY = e.y
            }
            MotionEvent.ACTION_UP -> {
                removeCallbacks(longPress)
                if (dragRow != null) endDrag(true)
                else if (scrolling) {
                    vt!!.computeCurrentVelocity(1000, maxFling.toFloat())
                    val v = vt!!.yVelocity
                    if (Math.abs(v) > minFling) { scroller.fling(0, scrollY, 0, (-v).toInt(), 0, 0, 0, maxScroll()); postInvalidateOnAnimation() }
                } else {
                    val r = downRow
                    if (r != null && rowAt(e.y + scrollY) === r && zone(e.x) == downZone) {
                        if (downZone == 3) Store.delete(r.item.id).also { if (it) onDeleted?.invoke(r.item) }
                        else if (downZone == 1) { performClick(); Store.toggle(r.item.id) }
                    }
                }
                recycle()
            }
            MotionEvent.ACTION_CANCEL -> {
                removeCallbacks(longPress)
                if (dragRow != null) endDrag(true)
                recycle()
            }
        }
        return true
    }

    override fun performClick(): Boolean { super.performClick(); return true }

    private fun recycle() { vt?.recycle(); vt = null; downRow = null; scrolling = false }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) { scrollTo(0, scroller.currY.coerceIn(0, maxScroll())); postInvalidateOnAnimation() }
    }

    /** Silme olayı (geri al şeridi için). */
    var onDeleted: ((Item) -> Unit)? = null

    // ---------- sürükle-sırala ----------

    private fun startDrag(r: Row) {
        if (dragRow != null || rows.size < 1) return
        dragRow = r; dragOrig = rows.indexOf(r)
        freeze(r)
        dragY = dispY(r); grabOff = (downY + scrollY) - dragY
        fingerY = downY
        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        invalidate()
    }

    private fun updateDrag() {
        val r = dragRow ?: return
        dragY = (fingerY + scrollY - grabOff).coerceIn(0f, maxOf(0f, contentH - dp(8f) - r.h))
        // hedef sıra: sürüklenenin merkezi, diğer satırların merkezleriyle karşılaştırılır
        val center = dragY + r.h / 2f
        var y = 0f; var idx = 0
        for (o in rows) { if (o === r) continue; if (center > y + o.h / 2f) idx++; y += o.h }
        val cur = rows.indexOf(r)
        if (idx != cur) {
            rows.removeAt(cur); rows.add(idx, r)
            for (o in rows) if (o !== r) freeze(o)
            var yy = 0f
            for (o in rows) { o.toY = yy; yy += o.h }
            t = 0f; stopAnim(); anim.start()
            performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
        }
        if (!autoRunning) { autoRunning = true; postOnAnimation(autoScroll) }
        invalidate()
    }

    private var autoRunning = false
    private val autoScroll = object : Runnable {
        override fun run() {
            if (dragRow == null) { autoRunning = false; return }
            val edge = dp(64f)
            val dy = when {
                fingerY < edge -> -dp(10f) * (1f - fingerY / edge).coerceIn(0f, 1f)
                fingerY > height - edge -> dp(10f) * (1f - (height - fingerY) / edge).coerceIn(0f, 1f)
                else -> 0f
            }
            if (dy != 0f) {
                val ns = (scrollY + dy).toInt().coerceIn(0, maxScroll())
                if (ns != scrollY) { scrollTo(0, ns); updateDrag(); return }
            }
            postOnAnimation(this)
        }
    }

    private fun endDrag(commit: Boolean) {
        val r = dragRow ?: return
        dragRow = null; autoRunning = false
        val to = rows.indexOf(r)
        // görsel olarak sürüklenenin yerine yumuşak otur
        r.fromY = dragY
        var yy = 0f
        for (o in rows) { o.toY = yy; yy += o.h }
        t = 0f; stopAnim(); anim.start()
        if (commit && to != dragOrig) {
            // model değişimi sync'i tetikler; satırlar zaten doğru sırada
            Store.move(dragOrig, to)
        }
        dragOrig = -1
    }

    // ---------- erişilebilirlik ----------

    private val ID_DELETE = 0x7f0f0001
    private val ID_UP = 0x7f0f0002
    private val ID_DOWN = 0x7f0f0003

    override fun getAccessibilityNodeProvider(): AccessibilityNodeProvider = provider

    private val provider = object : AccessibilityNodeProvider() {
        override fun createAccessibilityNodeInfo(id: Int): AccessibilityNodeInfo? {
            val host = this@NoktaListView
            if (id == HOST_VIEW_ID) {
                val info = AccessibilityNodeInfo.obtain(host)
                host.onInitializeAccessibilityNodeInfo(info)
                for (i in rows.indices) info.addChild(host, i)
                return info
            }
            val r = rows.getOrNull(id) ?: return null
            val info = AccessibilityNodeInfo.obtain(host, id)
            info.setParent(host)
            info.className = "android.widget.CheckBox"
            info.packageName = context.packageName
            info.text = r.item.text
            info.isCheckable = true; info.isChecked = r.item.done
            info.isEnabled = true; info.isClickable = true; info.isFocusable = true
            info.contentDescription = r.item.text + if (r.item.done) ", tamamlandı" else ""
            val y = dispY(r).toInt() - scrollY
            val rect = Rect(0, y, width, y + r.h.toInt())
            info.setBoundsInParent(rect)
            val loc = IntArray(2); getLocationOnScreen(loc)
            info.setBoundsInScreen(Rect(rect).apply { offset(loc[0], loc[1]) })
            info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK)
            info.addAction(AccessibilityNodeInfo.AccessibilityAction(ID_DELETE, "sil"))
            if (id > 0) info.addAction(AccessibilityNodeInfo.AccessibilityAction(ID_UP, "yukarı taşı"))
            if (id < rows.size - 1) info.addAction(AccessibilityNodeInfo.AccessibilityAction(ID_DOWN, "aşağı taşı"))
            return info
        }

        override fun performAction(id: Int, action: Int, args: Bundle?): Boolean {
            if (id == HOST_VIEW_ID) return this@NoktaListView.performAccessibilityAction(action, args)
            val r = rows.getOrNull(id) ?: return false
            return when (action) {
                AccessibilityNodeInfo.ACTION_CLICK -> { Store.toggle(r.item.id); true }
                ID_DELETE -> { if (Store.delete(r.item.id)) onDeleted?.invoke(r.item); true }
                ID_UP -> { Store.move(id, id - 1); true }
                ID_DOWN -> { Store.move(id, id + 1); true }
                else -> false
            }
        }
    }
}
