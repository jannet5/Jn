package app.nokta.list.reminder

import android.app.KeyguardManager
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.nokta.list.R
import app.nokta.list.core.Repo

/**
 * Saatli alarm caldiginda ekrani acar (kilitliyken de). Ses bildirimden gelir; bildirim kaldirilinca susar.
 * "Aldim" ogenin ustunu cizer, "Kapat" yalniz alarmi susturur.
 */
class AlarmActivity : AppCompatActivity() {
    private var id = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true); setTurnScreenOn(true)
            getSystemService(KeyguardManager::class.java).requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_alarm)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.alarm_root)) { v, w ->
            val b = w.getInsets(WindowInsetsCompat.Type.systemBars())
            val p = (24 * resources.displayMetrics.density).toInt()
            v.setPadding(p + b.left, p + b.top, p + b.right, p + b.bottom)
            WindowInsetsCompat.CONSUMED
        }
        id = intent.getLongExtra(Reminders.EXTRA_ID, -1L)
        findViewById<TextView>(R.id.alarm_time).text = DateFormat.getTimeFormat(this).format(System.currentTimeMillis())
        findViewById<TextView>(R.id.alarm_text).text = intent.getStringExtra(EXTRA_TEXT).orEmpty()
        findViewById<View>(R.id.alarm_done).setOnClickListener {
            silence()
            if (id >= 0) {
                // Tekrar ayari da biter: madde alindi.
                Reminders.clear(applicationContext, id)
                Repo.get(applicationContext).markDone(id) {}
            }
            finish()
        }
        findViewById<View>(R.id.alarm_dismiss).setOnClickListener { silence(); finish() }
    }

    // Balon alarm ekraninin ustunu kapatmasin.
    override fun onStart() { super.onStart(); app.nokta.list.bubble.BubbleService.setAppForeground(true) }
    override fun onStop() { app.nokta.list.bubble.BubbleService.setAppForeground(false); super.onStop() }

    private fun silence() {
        if (id >= 0) NotificationManagerCompat.from(this).cancel(Reminders.notifId(id))
    }

    companion object { const val EXTRA_TEXT = "text" }
}
