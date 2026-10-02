package app.nokta.c

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val d by lazy { resources.displayMetrics.density }
    private fun dp(v: Int) = (v * d).toInt()
    private fun ink(a: Int) = (getColor(R.color.ink) and 0x00FFFFFF) or (a shl 24)

    private lateinit var input: EditText
    private lateinit var count: TextView
    private lateinit var list: NoktaListView
    private lateinit var undoBar: LinearLayout
    private lateinit var undoMsg: TextView
    private lateinit var btnClear: TextView
    private lateinit var btnBalloon: TextView
    private val hideUndo = Runnable { undoBar.animate().alpha(0f).setDuration(150).withEndAction { undoBar.visibility = View.GONE }.start() }
    private val storeListener: () -> Unit = { refresh() }

    private fun label(s: String, size: Float, color: Int = getColor(R.color.ink)) = TextView(this).apply {
        text = s; setTextSize(TypedValue.COMPLEX_UNIT_SP, size); setTextColor(color)
    }

    private fun button(s: String, desc: String, onClick: () -> Unit) = label(s, 14f).apply {
        gravity = Gravity.CENTER; minHeight = dp(48); minWidth = dp(48); maxLines = 1; ellipsize = TextUtils.TruncateAt.END
        contentDescription = desc; isClickable = true; isFocusable = true
        setPadding(dp(8), 0, dp(8), 0)
        setOnClickListener { onClick() }
        background = ripple()
    }

    private fun ripple() = android.graphics.drawable.RippleDrawable(
        android.content.res.ColorStateList.valueOf(ink(40)), null, android.graphics.drawable.ColorDrawable(Color.WHITE))

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        Store.init(this)
        if (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK != android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

        // başlık: serif küçük harf marka + sayaç
        val head = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; setPadding(dp(20), dp(8), dp(20), 0) }
        val title = label("nokta", 30f).apply {
            typeface = Typeface.create("serif", Typeface.NORMAL)
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        }
        count = label("", 13f, ink(170)).apply { gravity = Gravity.END; maxLines = 1; ellipsize = TextUtils.TruncateAt.END }
        head.addView(title, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(48)))
        head.addView(count, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply { leftMargin = dp(12) })
        root.addView(head)

        // ekleme satırı
        val addRow = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL; setPadding(dp(20), 0, dp(8), 0) }
        input = EditText(this).apply {
            hint = "yeni nokta"; setHintTextColor(ink(115)); setTextColor(getColor(R.color.ink))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
            background = null; setPadding(0, 0, 0, 0)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            imeOptions = EditorInfo.IME_ACTION_DONE
            setSingleLine(true)
            contentDescription = "Yeni madde yaz"
            setOnEditorActionListener { _, _, _ -> submit(); true }
        }
        addRow.addView(input, LinearLayout.LayoutParams(0, dp(56), 1f))
        val plus = button("+", "Ekle") { submit() }.apply { setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f); setTextColor(getColor(R.color.accent)) }
        addRow.addView(plus, LinearLayout.LayoutParams(dp(48), dp(48)))
        root.addView(addRow)
        root.addView(View(this).apply { setBackgroundColor(ink(40)) }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1).apply { leftMargin = dp(20); rightMargin = dp(20) })

        list = NoktaListView(this)
        list.onDeleted = { showUndo("silindi") }
        root.addView(list, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))

        // geri al şeridi
        undoBar = LinearLayout(this).apply {
            gravity = Gravity.CENTER_VERTICAL; visibility = View.GONE; setPadding(dp(20), 0, dp(8), 0)
            background = GradientDrawable().apply { setColor(getColor(R.color.ink)) }
        }
        undoMsg = label("", 15f, getColor(R.color.bg)).apply { maxLines = 1; ellipsize = TextUtils.TruncateAt.END }
        undoBar.addView(undoMsg, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        val undoBtn = button("geri al", "Geri al") { if (Store.undo()) dismissUndo() }.apply {
            setTextColor(getColor(R.color.accent)); setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f); typeface = Typeface.DEFAULT_BOLD
        }
        undoBar.addView(undoBtn, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(48)))
        root.addView(undoBar, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(48)))

        // alt eylemler
        val bar = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        btnClear = button("tamamlananları sil", "Tamamlananları temizle") { clearDone() }
        val share = button("paylaş", "Listeyi metin olarak paylaş") { share() }
        btnBalloon = button("balon", "Yüzen balonu aç veya kapat") { toggleBalloon() }
        bar.addView(btnClear, LinearLayout.LayoutParams(0, dp(48), 2f))
        bar.addView(share, LinearLayout.LayoutParams(0, dp(48), 1f))
        bar.addView(btnBalloon, LinearLayout.LayoutParams(0, dp(48), 1f))
        root.addView(View(this).apply { setBackgroundColor(ink(40)) }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1))
        root.addView(bar)

        setContentView(root)
        Store.listen(storeListener)
        refresh()
    }

    private fun submit() {
        val s = input.text.toString()
        if (Store.add(s)) { input.setText(""); list.scrollToTop() }
        input.requestFocus()
    }

    private fun refresh() {
        val m = Store.model
        count.text = when {
            m.items.isEmpty() -> ""
            m.doneCount == 0 -> "${m.openCount} açık"
            else -> "${m.openCount} açık · ${m.doneCount} tamam"
        }
        btnClear.isEnabled = m.doneCount > 0
        btnClear.alpha = if (m.doneCount > 0) 1f else 0.35f
        btnBalloon.setTextColor(if (balloonOn()) getColor(R.color.accent) else getColor(R.color.ink))
        btnBalloon.contentDescription = if (balloonOn()) "Yüzen balon açık, kapatmak için dokun" else "Yüzen balon kapalı, açmak için dokun"
    }

    private fun showUndo(msg: String) {
        undoMsg.text = msg
        undoBar.removeCallbacks(hideUndo)
        undoBar.animate().cancel()
        undoBar.alpha = 0f; undoBar.visibility = View.VISIBLE
        undoBar.animate().alpha(1f).setDuration(150).start()
        undoBar.postDelayed(hideUndo, 6000)
        undoBar.announceForAccessibility("$msg. Geri al düğmesi var.")
    }

    private fun dismissUndo() { undoBar.removeCallbacks(hideUndo); undoBar.animate().cancel(); undoBar.visibility = View.GONE }

    private fun clearDone() {
        val n = Store.clearDone()
        if (n > 0) showUndo("$n tamamlanan silindi")
    }

    private fun share() {
        val text = Store.model.exportText()
        if (text.isEmpty()) { Toast.makeText(this, "Liste boş", Toast.LENGTH_SHORT).show(); return }
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text), "nokta listesini paylaş"))
    }

    private fun balloonOn() = BalloonService.prefs(this).getBoolean(BalloonService.KEY_ON, false) && Settings.canDrawOverlays(this)

    private fun toggleBalloon() {
        val p = BalloonService.prefs(this)
        if (balloonOn()) {
            p.edit().putBoolean(BalloonService.KEY_ON, false).apply()
            stopService(Intent(this, BalloonService::class.java))
        } else {
            p.edit().putBoolean(BalloonService.KEY_ON, true).apply()
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Balon için \"diğer uygulamaların üzerinde göster\" iznini aç", Toast.LENGTH_LONG).show()
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                return
            }
            if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            startBalloon()
        }
        refresh()
    }

    private fun startBalloon() { startForegroundService(Intent(this, BalloonService::class.java)) }

    override fun onResume() {
        super.onResume()
        if (balloonOn()) startBalloon()
        refresh()
        input.postDelayed({
            input.requestFocus()
            getSystemService(InputMethodManager::class.java).showSoftInput(input, InputMethodManager.SHOW_IMPLICIT)
        }, 100)
    }

    override fun onDestroy() { Store.unlisten(storeListener); super.onDestroy() }
}
