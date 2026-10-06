package app.nokta.list.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.nokta.list.R
import app.nokta.list.data.Item

class ItemAdapter(
    private val onToggle: (Long) -> Unit,
    private val onDelete: (Long) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit,
    private val onMove: (Int, Int) -> Unit,
    private val onBell: (Item) -> Unit,
    private val hasReminder: (Long) -> Boolean,
) : RecyclerView.Adapter<ItemAdapter.VH>() {

    var items: List<Item> = emptyList()
        private set
    /** Surukleme suresince gelen guncellemeler bekletilir (satir elinden kaymasin). */
    var dragging = false
    private var pending: List<Item>? = null

    init { setHasStableIds(true) }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val dot: CheckDot = v.findViewById(R.id.dot)
        val text: StrikeText = v.findViewById(R.id.text)
        val handle: ImageView = v.findViewById(R.id.handle)
        val delete: View = v.findViewById(R.id.delete)
        val bell: ImageView = v.findViewById(R.id.bell)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vh = VH(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))
        vh.itemView.setOnClickListener { vh.bindingAdapterPosition.takeIf { it >= 0 }?.let { onToggle(items[it].id) } }
        vh.delete.setOnClickListener { vh.bindingAdapterPosition.takeIf { it >= 0 }?.let { onDelete(items[it].id) } }
        vh.bell.setOnClickListener { vh.bindingAdapterPosition.takeIf { it >= 0 }?.let { onBell(items[it]) } }
        vh.handle.setOnTouchListener { _, e ->
            if (e.actionMasked == MotionEvent.ACTION_DOWN) onStartDrag(vh)
            false
        }
        return vh
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val it = items[position]
        h.text.text = it.text
        h.text.set(it.done, false)
        h.dot.set(it.done, false)
        describe(h, it, position)
    }

    /** TalkBack: durum soylenir, tek dokunus ve tasima eylemleri adlandirilir. */
    private fun describe(h: VH, it: Item, position: Int) {
        val ctx = h.itemView.context
        val state = ctx.getString(if (it.done) R.string.state_done else R.string.state_open)
        h.itemView.contentDescription = ctx.getString(R.string.row_desc, it.text, state)
        ViewCompat.replaceAccessibilityAction(
            h.itemView, AccessibilityActionCompat.ACTION_CLICK,
            ctx.getString(if (it.done) R.string.act_undone else R.string.act_done), null,
        )
        h.delete.contentDescription = ctx.getString(R.string.delete_item, it.text)
        val on = hasReminder(it.id)
        h.bell.setImageResource(if (on) R.drawable.ic_bell_on else R.drawable.ic_bell)
        h.bell.contentDescription = ctx.getString(if (on) R.string.remind_on_item else R.string.remind_item, it.text)
        h.handle.contentDescription = ctx.getString(R.string.drag_item, it.text)
        h.dot.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        ViewCompat.addAccessibilityAction(h.itemView, ctx.getString(R.string.act_up)) { _, _ ->
            val p = h.bindingAdapterPosition; if (p > 0) { onMove(p, p - 1); true } else false
        }
        ViewCompat.addAccessibilityAction(h.itemView, ctx.getString(R.string.act_down)) { _, _ ->
            val p = h.bindingAdapterPosition; if (p in 0 until items.size - 1) { onMove(p, p + 1); true } else false
        }
    }

    override fun onBindViewHolder(h: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return onBindViewHolder(h, position)
        val it = items[position]
        h.text.set(it.done, true)
        h.dot.set(it.done, true)
        describe(h, it, position)
    }

    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = items[position].id

    fun submit(next: List<Item>) {
        if (dragging) { pending = next; return }
        pending = null
        val old = items
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = old.size
            override fun getNewListSize() = next.size
            override fun areItemsTheSame(o: Int, n: Int) = old[o].id == next[n].id
            override fun areContentsTheSame(o: Int, n: Int) = old[o].text == next[n].text && old[o].done == next[n].done
            override fun getChangePayload(o: Int, n: Int): Any? = if (old[o].text == next[n].text) "done" else null
        })
        items = next
        diff.dispatchUpdatesTo(this)
    }

    /** Surukleme sirasinda yalniz gorunum guncellenir; model birakinca bir kez degisir. */
    fun moveLocal(from: Int, to: Int) {
        val m = items.toMutableList()
        m.add(to, m.removeAt(from))
        items = m
        notifyItemMoved(from, to)
    }

    fun endDrag(latest: List<Item>) {
        dragging = false
        submit(pending ?: latest)
    }
}
