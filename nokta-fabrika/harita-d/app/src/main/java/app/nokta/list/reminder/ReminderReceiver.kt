package app.nokta.list.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import app.nokta.list.core.Repo

/** Alarm caldiginda bildirimi gosterir; bildirimdeki "Aldim" / "Durdur" dugmelerini isler. */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext
        val id = intent.getLongExtra(Reminders.EXTRA_ID, -1L)
        if (id < 0) return
        when (intent.action) {
            Reminders.ACTION_STOP -> Reminders.clear(app, id)
            Reminders.ACTION_DONE -> {
                Reminders.clear(app, id)
                val res = goAsync()
                Repo.get(app).markDone(id) { res.finish() }
            }
            Reminders.ACTION_FIRE -> {
                val kind = intent.getIntExtra(Reminders.EXTRA_KIND, Reminders.KIND_EVERY)
                val plan = Reminders.get(app, id) ?: return
                val res = goAsync()
                val repo = Repo.get(app)
                repo.whenLoaded {
                    val item = repo.items.firstOrNull { it.id == id }
                    if (item == null || item.done) {
                        // Oge silinmis ya da ustu cizilmis: hatirlatma biter.
                        Reminders.clear(app, id)
                    } else {
                        Reminders.show(app, item, kind, plan)
                        if (kind == Reminders.KIND_EVERY && plan.everyMin != null) Reminders.scheduleNextEvery(app, id, plan.everyMin)
                        if (kind == Reminders.KIND_AT) Reminders.dropAt(app, id)
                    }
                    res.finish()
                }
            }
        }
    }
}

/** Telefon acilisi, uygulama guncellemesi ya da tam zaman izni degisince alarmlari yeniden kurar. */
class RescheduleReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Reminders.rescheduleAll(context.applicationContext)
    }
}
