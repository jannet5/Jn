package app.nokta.b.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.nokta.b.R
import app.nokta.b.data.Item

class ItemAdapter(
    private val onToggle: (Long) -> Unit,
    private val onDelete: (Long) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit,
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
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vh = VH(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))
        vh.itemView.setOnClickListener { vh.bindingAdapterPosition.takeIf { it >= 0 }?.let { onToggle(items[it].id) } }
        vh.delete.setOnClickListener { vh.bindingAdapterPosition.takeIf { it >= 0 }?.let { onDelete(items[it].id) } }
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
    }

    override fun onBindViewHolder(h: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return onBindViewHolder(h, position)
        val it = items[position]
        h.text.set(it.done, true)
        h.dot.set(it.done, true)
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
