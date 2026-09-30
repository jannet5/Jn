package app.nokta.list

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: TaskAdapter
    private lateinit var list: TaskList
    private lateinit var count: TextView
    private lateinit var empty: View
    private lateinit var bubbleSwitch: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list = Store.get(this)
        count = findViewById(R.id.count)
        empty = findViewById(R.id.empty)
        bubbleSwitch = findViewById(R.id.bubble_switch)

        lateinit var touch: ItemTouchHelper
        adapter = TaskAdapter(list, { Store.save(this); refresh() }, { touch.startDrag(it) })
        val rv = findViewById<RecyclerView>(R.id.recycler)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        rv.itemAnimator?.changeDuration = 120

        // Long-press anywhere on a row to drag and reorder.
        touch = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean {
                adapter.move(v.bindingAdapterPosition, t.bindingAdapterPosition); return true
            }
            override fun onSwiped(v: RecyclerView.ViewHolder, d: Int) {}
            override fun isLongPressDragEnabled() = true
            override fun onSelectedChanged(vh: RecyclerView.ViewHolder?, state: Int) {
                super.onSelectedChanged(vh, state)
                if (state == ItemTouchHelper.ACTION_STATE_DRAG) {
                    vh?.itemView?.apply { performHapticFeedback(HapticFeedback_LONG); animate().scaleX(1.02f).scaleY(1.02f).translationZ(12f).setDuration(100).start() }
                }
            }
            override fun clearView(r: RecyclerView, vh: RecyclerView.ViewHolder) {
                super.clearView(r, vh)
                vh.itemView.animate().scaleX(1f).scaleY(1f).translationZ(0f).setDuration(100).start()
                Store.save(this@MainActivity)
            }
        })
        touch.attachToRecyclerView(rv)

        val input = findViewById<EditText>(R.id.input)
        fun submit() {
            val t = list.add(input.text.toString()) ?: return
            input.text.clear()
            adapter.notifyItemInserted(list.all.size - 1)
            rv.scrollToPosition(list.all.size - 1)
            Store.save(this); refresh()
        }
        input.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_ACTION_SEND) { submit(); true } else false
        }
        input.setOnKeyListener { _, code, ev ->
            if (code == android.view.KeyEvent.KEYCODE_ENTER && ev.action == android.view.KeyEvent.ACTION_UP) { submit(); true } else code == android.view.KeyEvent.KEYCODE_ENTER
        }
        findViewById<View>(R.id.add).setOnClickListener { submit() }

        bubbleSwitch.setOnCheckedChangeListener { b, on -> if (b.isPressed) setBubble(on) }
        refresh()
    }

    override fun onResume() {
        super.onResume()
        val want = Store.prefs(this).getBoolean(Store.KEY_BUBBLE, false)
        val ok = want && canOverlay()
        bubbleSwitch.isChecked = ok
        if (ok) BubbleService.start(this)
        BubbleService.update(this)
    }

    private fun canOverlay() = Settings.canDrawOverlays(this)

    private fun setBubble(on: Boolean) {
        Store.prefs(this).edit().putBoolean(Store.KEY_BUBBLE, on).apply()
        if (!on) { BubbleService.stop(this); return }
        if (!canOverlay()) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
            return // onResume starts the bubble once permission is granted
        }
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        BubbleService.start(this)
    }

    private fun refresh() {
        val left = list.remaining
        count.text = when {
            list.all.isEmpty() -> getString(R.string.count_none)
            left == 0 -> getString(R.string.count_all_done)
            else -> getString(R.string.count_left, left)
        }
        empty.visibility = if (list.all.isEmpty()) View.VISIBLE else View.GONE
        BubbleService.update(this)
    }

    private companion object { const val HapticFeedback_LONG = android.view.HapticFeedbackConstants.LONG_PRESS }
}
