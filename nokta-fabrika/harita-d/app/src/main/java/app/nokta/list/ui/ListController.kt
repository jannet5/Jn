package app.nokta.list.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.nokta.list.R
import app.nokta.list.core.AddFlow
import app.nokta.list.core.Repo
import app.nokta.list.core.Undo
import app.nokta.list.data.Item
import app.nokta.list.reminder.Reminders

/**
 * list_view.xml'i Repo'ya baglar. Ayni sinif hem Activity'de hem balon panelinde kullanilir,
 * boylece iki yuzeyin davranisi ayrismaz.
 */
class ListController(
    private val root: View,
    private val repo: Repo,
    onAction: () -> Unit,
    actionIcon: Int,
    actionDesc: Int,
    onBell: (Item) -> Unit,
) {
    private val ctx: Context = root.context
    private val handler = Handler(Looper.getMainLooper())
    private val recycler: RecyclerView = root.findViewById(R.id.recycler)
    private val add: EditText = root.findViewById(R.id.add)
    private val addBtn: TextView = root.findViewById(R.id.add_btn)
    private val kbAdd: TextView = root.findViewById(R.id.kb_add)
    private val editBar: View = root.findViewById(R.id.edit_bar)
    /** Duzenlenen ogenin kimligi; null ise giris alani yeni oge ekler. */
    private var editingId: Long? = null
    /** Surukleme tutamactan basladiysa bas-birak duzenleme acmaz. */
    private var dragFromHandle = false
    private val count: TextView = root.findViewById(R.id.count)
    private val empty: View = root.findViewById(R.id.empty)
    private val clear: View = root.findViewById(R.id.clear_done)
    private val undoBar: View = root.findViewById(R.id.undo_bar)
    private val undoText: TextView = root.findViewById(R.id.undo_text)
    private var shownUndo: Undo? = null
    private var dismissTask: Runnable? = null
    val actionButton: ImageButton = root.findViewById(R.id.action)

    private var helper: ItemTouchHelper
    private val adapter = ItemAdapter(
        onToggle = { id ->
            repo.toggle(id)
            // Ustu cizilen ogenin hatirlatmasi durur.
            if (repo.items.firstOrNull { it.id == id }?.done == true && Reminders.has(ctx, id)) Reminders.clear(ctx, id)
        },
        onDelete = { repo.delete(it) },
        onStartDrag = { dragFromHandle = true; helper.startDrag(it) },
        onMove = { from, to -> repo.move(from, to) },
        onBell = onBell,
        hasReminder = { Reminders.has(ctx, it) },
    )
    @SuppressLint("NotifyDataSetChanged") // hatirlatma degisimi seyrek; tum zil ikonlari yenilenir
    private val reminderListener: () -> Unit = { adapter.notifyDataSetChanged() }
    private val listener: () -> Unit = { render() }

    init {
        recycler.layoutManager = LinearLayoutManager(ctx)
        recycler.adapter = adapter
        recycler.itemAnimator?.apply { moveDuration = 150; removeDuration = 150; addDuration = 150; changeDuration = 0 }
        helper = ItemTouchHelper(DragCallback()).also { it.attachToRecyclerView(recycler) }
        // Liste dokunmayla odak calmasin: odak yalniz giris alaninda kalir (hizli art arda ekleme).
        recycler.isFocusable = false
        recycler.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
        actionButton.setImageResource(actionIcon)
        actionButton.contentDescription = ctx.getString(actionDesc)
        actionButton.setOnClickListener { onAction() }
        clear.setOnClickListener { repo.clearDone() }
        root.findViewById<View>(R.id.undo_action).setOnClickListener { repo.undoLast() }

        // Enter yeni satir acar (paragraf); ekleme/kaydetme yalniz dugmeyle.
        addBtn.setOnClickListener { submitAdd() }
        kbAdd.setOnClickListener { submitAdd() }
        add.setOnFocusChangeListener { _, _ -> syncKbAdd() }
        root.findViewById<View>(R.id.edit_cancel).setOnClickListener { stopEdit(clear = true) }
    }

    /** Ogeyi giris alanina alir; dugme "Kaydet" olur. */
    private fun startEdit(item: Item) {
        editingId = item.id
        editBar.visibility = View.VISIBLE
        addBtn.setText(R.string.save); kbAdd.setText(R.string.save)
        add.setText(item.text)
        add.setSelection(add.text.length)
        refocus()
    }

    private fun stopEdit(clear: Boolean) {
        if (editingId == null) return
        editingId = null
        editBar.visibility = View.GONE
        addBtn.setText(R.string.add); kbAdd.setText(R.string.add)
        if (clear) add.setText("")
    }

    private val layoutListener = ViewTreeObserver.OnGlobalLayoutListener { syncKbAdd() }

    /** Klavye acik ve giris alani odaktaysa klavye ustundeki Ekle gorunur; geri al cubugu onun ustune kayar. */
    private fun syncKbAdd() {
        val ime = ViewCompat.getRootWindowInsets(root)?.isVisible(WindowInsetsCompat.Type.ime()) == true
        val show = ime && add.hasFocus()
        val vis = if (show) View.VISIBLE else View.GONE
        if (kbAdd.visibility != vis) kbAdd.visibility = vis
        val lift = if (show) -52f * ctx.resources.displayMetrics.density else 0f
        if (undoBar.translationY != lift) undoBar.translationY = lift
    }

    fun attach() {
        root.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        repo.addListener(listener); Reminders.addListener(reminderListener); render()
        // Arka plandayken (bildirimden "Durdur"/"Aldim") degisen zil durumlari geri donuste yenilenir.
        reminderListener()
    }
    fun detach() {
        root.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
        repo.removeListener(listener); Reminders.removeListener(reminderListener)
        dismissTask?.let { handler.removeCallbacks(it) }
    }

    fun focusAdd(showKeyboard: Boolean = true) {
        add.requestFocus()
        if (showKeyboard) add.postDelayed({
            (ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(add, InputMethodManager.SHOW_IMPLICIT)
        }, 150)
    }

    private fun submitAdd() {
        editingId?.let { id ->
            // Bos birakilan metin kaydedilmez (silmek icin carpi var); oge silindiyse duzenleme sessizce biter.
            if (add.text.isBlank() && repo.items.any { it.id == id }) return
            repo.edit(id, add.text.toString())
            stopEdit(clear = true)
            refocus()
            return
        }
        // Yeni oge en uste girer; liste en uste kayar ki eklenen gorunsun. Odak ve klavye korunur.
        val added = repo.add(add.text.toString())
        if (added) showEditTipOnce()
        val flow = AddFlow.after(added)
        if (flow.clearField) add.setText("")
        if (flow.scrollToTop) recycler.scrollToPosition(0)
        if (flow.keepFocus) refocus()
    }

    /** Ilk eklemede bir kez: duzeltmenin yolu soylenir. */
    private fun showEditTipOnce() {
        val prefs = ctx.getSharedPreferences("ui", Context.MODE_PRIVATE)
        if (prefs.getBoolean("edit_tip", false)) return
        prefs.edit().putBoolean("edit_tip", true).apply()
        Toast.makeText(ctx, R.string.edit_tip, Toast.LENGTH_LONG).show()
    }

    private fun refocus() {
        if (!add.hasFocus()) add.requestFocus()
        val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // Liste degisikligi/animasyonu odagi ya da klavyeyi dusurduyse bir sonraki karede geri getir.
        add.post {
            if (!add.hasFocus()) add.requestFocus()
            imm.showSoftInput(add, InputMethodManager.SHOW_IMPLICIT)
        }
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
        undoText.text = when (u) {
            is Undo.Deleted -> ctx.getString(R.string.undo_deleted)
            is Undo.ClearedDone -> ctx.getString(R.string.undo_cleared, u.removed.size)
        }
        undoBar.visibility = View.VISIBLE
        dismissTask = Runnable { if (repo.undo === u) repo.dismissUndo() }.also { handler.postDelayed(it, UNDO_MS) }
    }

    companion object { const val UNDO_MS = 6000L }

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
            // Animasyonla degil kesin olarak yerine oturt: liste guncellemesinin animasyonu bunu yarida kesebiliyordu
            // ve satir buyutulmus/golgeli kaliyordu (yavas dokunusta uzun basma -> birakma).
            vh.itemView.animate().cancel()
            vh.itemView.scaleX = 1f; vh.itemView.scaleY = 1f; vh.itemView.translationZ = 0f
            if (adapter.dragging) {
                if (from >= 0 && from != to) repo.move(from, to)
                // Basili tutup kipirdatmadan birakmak = duzenle (tutamactan baslayan dokunus haric).
                val pressOnly = from < 0 && !dragFromHandle
                from = -1; to = -1; dragFromHandle = false
                adapter.endDrag(repo.items)
                if (pressOnly) vh.itemId.let { id -> repo.items.firstOrNull { it.id == id } }?.let { startEdit(it) }
            }
        }
    }
}
