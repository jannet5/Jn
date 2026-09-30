package app.nokta.list

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val list: TaskList,
    private val onChanged: () -> Unit,
) : RecyclerView.Adapter<TaskAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val text: TextView = v.findViewById(R.id.text)
        val delete: View = v.findViewById(R.id.delete)
    }

    override fun getItemCount() = list.all.size
    override fun getItemId(position: Int) = list.all[position].id
    init { setHasStableIds(true) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.row_task, parent, false))

    override fun onBindViewHolder(h: VH, position: Int) {
        val t = list.all[position]
        h.text.text = t.text
        h.text.paintFlags = if (t.done) h.text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else h.text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        h.text.alpha = if (t.done) 0.45f else 1f
        h.itemView.setOnClickListener {
            val p = h.bindingAdapterPosition
            if (p != RecyclerView.NO_POSITION) { list.toggle(p); notifyItemChanged(p); onChanged() }
        }
        h.delete.setOnClickListener {
            val p = h.bindingAdapterPosition
            if (p != RecyclerView.NO_POSITION) { list.remove(p); notifyItemRemoved(p); onChanged() }
        }
    }

    fun move(from: Int, to: Int) { list.move(from, to); notifyItemMoved(from, to) }
}
