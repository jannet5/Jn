package app.nokta.list.ui

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import app.nokta.list.R
import app.nokta.list.data.Item
import app.nokta.list.reminder.ReminderPlan
import app.nokta.list.reminder.Reminders
import java.util.Calendar

/**
 * Zil isaretine basinca acilan pencere: "her N dakikada bir" ve/veya "alarm (saat)".
 * Ikisi birlikte kullanilabilir. [onSaved] bildirim izni istemek icin cagrilir.
 */
class ReminderDialog(private val act: AppCompatActivity, private val item: Item, private val onSaved: () -> Unit) {

    fun show() {
        val v = LayoutInflater.from(act).inflate(R.layout.dialog_reminder, null)
        val everySw = v.findViewById<SwitchCompat>(R.id.every_switch)
        val everyMin = v.findViewById<EditText>(R.id.every_min)
        val atSw = v.findViewById<SwitchCompat>(R.id.at_switch)
        val atBtn = v.findViewById<Button>(R.id.at_time)
        val exactHint = v.findViewById<TextView>(R.id.exact_hint)
        v.findViewById<TextView>(R.id.item_text).text = item.text
        v.findViewById<View>(R.id.notif_hint).visibility = if (Reminders.canNotify(act)) View.GONE else View.VISIBLE

        val plan = Reminders.get(act, item.id)
        everySw.isChecked = plan?.everyMin != null
        everyMin.setText((plan?.everyMin ?: ReminderPlan.DEFAULT_EVERY).toString())
        // Dakika yazilinca tekrar kendiliginden acilir.
        everyMin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) { if (!s.isNullOrBlank() && everyMin.hasFocus()) everySw.isChecked = true }
        })

        val cal = Calendar.getInstance().apply {
            if (plan?.atMillis != null) timeInMillis = plan.atMillis
            else { add(Calendar.HOUR_OF_DAY, 1); set(Calendar.MINUTE, 0) }
        }
        var hour = cal.get(Calendar.HOUR_OF_DAY)
        var minute = cal.get(Calendar.MINUTE)
        fun renderAt() {
            val at = ReminderPlan.nextAt(System.currentTimeMillis(), hour, minute)
            val time = DateFormat.getTimeFormat(act).format(at)
            atBtn.text = act.getString(
                if (ReminderPlan.isToday(System.currentTimeMillis(), at)) R.string.remind_today_fmt else R.string.remind_tomorrow_fmt, time,
            )
            exactHint.visibility = if (atSw.isChecked && !Reminders.canExact(act)) View.VISIBLE else View.GONE
        }
        atSw.isChecked = plan?.atMillis != null
        atSw.setOnCheckedChangeListener { _, _ -> renderAt() }
        atBtn.setOnClickListener {
            TimePickerDialog(act, { _, h, m -> hour = h; minute = m; atSw.isChecked = true; renderAt() },
                hour, minute, DateFormat.is24HourFormat(act)).show()
        }
        if (Build.VERSION.SDK_INT >= 31) exactHint.setOnClickListener {
            act.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:${act.packageName}")))
        }
        renderAt()

        val b = AlertDialog.Builder(act)
            .setTitle(R.string.remind_title)
            .setView(v)
            .setPositiveButton(R.string.save, null)
            .setNegativeButton(R.string.cancel, null)
        if (plan != null) b.setNeutralButton(R.string.remind_remove) { _, _ ->
            Reminders.clear(act, item.id)
            Toast.makeText(act, R.string.remind_removed, Toast.LENGTH_SHORT).show()
        }
        val d = b.create()
        d.setOnShowListener {
            d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val every = if (everySw.isChecked) {
                    ReminderPlan.parseMinutes(everyMin.text.toString()) ?: run {
                        everyMin.error = act.getString(R.string.remind_bad_min); return@setOnClickListener
                    }
                } else null
                val at = if (atSw.isChecked) ReminderPlan.nextAt(System.currentTimeMillis(), hour, minute) else null
                val next = ReminderPlan(every, at)
                Reminders.set(act, item.id, next)
                Toast.makeText(act, summary(next), Toast.LENGTH_LONG).show()
                if (!next.isEmpty) onSaved()
                d.dismiss()
            }
        }
        d.show()
    }

    private fun summary(p: ReminderPlan): String {
        if (p.isEmpty) return act.getString(R.string.remind_removed)
        val time = p.atMillis?.let { DateFormat.getTimeFormat(act).format(it) }
        return when {
            p.everyMin != null && time != null -> act.getString(R.string.remind_saved_both, p.everyMin, time)
            p.everyMin != null -> act.getString(R.string.remind_saved_every, p.everyMin)
            else -> act.getString(R.string.remind_saved_at, time)
        }
    }
}
