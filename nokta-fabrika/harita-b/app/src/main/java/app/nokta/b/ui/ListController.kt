package app.nokta.b.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.HapticFeedbackConstants
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.nokta.b.R
import app.nokta.b.core.Repo
import app.nokta.b.core.Undo

/**
 * list_view.xml'i Repo'ya baglar. Ayni sinif hem Activity'de hem balon panelinde kullanilir,
 * boylece iki yuzeyin davranisi ayrismaz.
 */
class ListController(private val root: View, private val repo: Repo, onAction: () -> Unit, actionIcon: Int) {
    private val ctx: Context = root.context
    private val handler = Handler(Looper.getMainLooper())
    private val recycler: RecyclerView = root.findViewById(R.id.recycler)
    private val add: EditText = root.findViewById(R.id.add)
    private val count: TextView = root.findViewById(R.id.count)
    private val empty: View = root.findViewById(R.id.empty)
    private val clear: View = root.findViewById(R.id.clear_done)
    private val undoBar: View = root.findViewById(R.id.undo_bar)
    private val undoText: TextView = root.findViewById(R.id.undo_text)
    private var shownUndo: Undo? = null
    private var dismissTask: Runnable? = null
    val actionButton: ImageButton = root.findViewById(R.id.action)

    private lateinit var helper: ItemTouchHelper
    private val adapter = ItemAdapter(
        onToggle = { repo.toggle(it) },
        onDelete = { repo.delete(it) },
        onStartDrag = { helper.startDrag(it) },
    )
    private val listener: () -> Unit = { render() }

    init {
        recycler.layoutManager = LinearLayoutManager(ctx)
        recycler.adapter = adapter
        recycler.itemAnimator?.apply { moveDuration = 150; removeDuration = 150; addDuration = 150; changeDuration = 0 }
        helper = ItemTouchHelper(DragCallback()).also { it.attachToRecyclerView(recycler) }
        actionButton.setImageResource(actionIcon)
        actionButton.setOnClickListener { onAction() }
        clear.setOnClickListener { repo.clearDone() }
        root.findViewById<View>(R.id.undo_action).setOnClickListener { repo.undoLast() }

        add.setOnEditorActionListener { _, id, e ->
            if (id == EditorInfo.IME_ACTION_DONE || (e?.keyCode == KeyEvent.KEYCODE_ENTER && e.action == KeyEvent.ACTION_DOWN)) {
                submitAdd(); true
            } else false
        }
    }

    fun attach() { repo.addListener(listener); render() }
    fun detach() { repo.removeListener(listener); dismissTask?.let { handler.removeCallbacks(it) } }

    fun focusAdd(showKeyboard: Boolean = true) {
        add.requestFocus()
        if (showKeyboard) add.postDelayed({
            (ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(add, InputMethodManager.SHOW_IMPLICIT)
        }, 150)
    }

    private fun submitAdd() {
        // Yeni oge en uste girer; liste en uste kayar ki eklenen gorunsun.
        if (repo.add(add.text.toString())) { add.setText(""); recycler.scrollToPosition(0) }
    }

    private fun render() {
        adapter.submit(repo.items)
        val rem = repo.remaining
        count.text = when {
            repo.items.isEmpty() -> ""
            rem == 0 -> ctx.getString(R.string.all_done)
            else -> ctx.getString(R.string.remaining_fmt, rem)
        }
        empty.visibility = if (repo.loaded && repo.items.isEmpty()) View.VISIBLE else View.GONE
        clear.visibility = if (repo.doneCount > 0) View.VISIBLE else View.GONE
        renderUndo()
    }

    private fun renderUndo() {
        val u = repo.undo
        if (u === shownUndo) return
        shownUndo = u
        dismissTask?.let { handler.removeCallbacks(it) }
        if (u == null) { undoBar.visibility = View.GONE; return }
        undoText.text = u.label
        undoBar.visibility = View.VISIBLE
        dismissTask = Runnable { if (repo.undo === u) repo.dismissUndo() }.also { handler.postDelayed(it, 5000) }
    }

    private inner class DragCallback : ItemTouchHelper.Callback() {
        private var from = -1
        private var to = -1

        override fun getMovementFlags(rv: RecyclerView, vh: RecyclerView.ViewHolder) =
            makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)

        override fun isLongPressDragEnabled() = true

        override fun onMove(rv: RecyclerView, src: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val a = src.bindingAdapterPosition; val b = target.bindingAdapterPosition
            if (a < 0 || b < 0) return false
            if (from < 0) from = a
            to = b
            adapter.moveLocal(a, b)
            return true
        }

        override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {}

        override fun onSelectedChanged(vh: RecyclerView.ViewHolder?, state: Int) {
            super.onSelectedChanged(vh, state)
            if (state == ItemTouchHelper.ACTION_STATE_DRAG && vh != null) {
                adapter.dragging = true
                from = -1; to = -1
                vh.itemView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                vh.itemView.animate().scaleX(1.02f).scaleY(1.02f).translationZ(8f * ctx.resources.displayMetrics.density).setDuration(150).start()
            }
        }

        override fun clearView(rv: RecyclerView, vh: RecyclerView.ViewHolder) {
            super.clearView(rv, vh)
            vh.itemView.animate().scaleX(1f).scaleY(1f).translationZ(0f).setDuration(150).start()
            if (adapter.dragging) {
                if (from >= 0 && from != to) repo.move(from, to)
                from = -1; to = -1
                adapter.endDrag(repo.items)
            }
        }
    }
}
