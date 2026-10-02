package com.notivo.app.service

import android.content.Context
import androidx.work.*
import com.notivo.app.data.Db
import com.notivo.app.data.Prefs
import java.util.Calendar
import java.util.concurrent.TimeUnit

/** Optional once-a-day "you have N unread" summary. */
object Digest {
    private const val NAME = "digest"
    fun schedule(c: Context) {
        val wm = WorkManager.getInstance(c)
        val h = Prefs(c).digestHour
        if (h < 0) { wm.cancelUniqueWork(NAME); return }
        val now = Calendar.getInstance()
        val next = (now.clone() as Calendar).apply { set(Calendar.HOUR_OF_DAY, h); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1) }
        wm.enqueueUniquePeriodicWork(NAME, ExistingPeriodicWorkPolicy.UPDATE,
            PeriodicWorkRequestBuilder<Worker>(24, TimeUnit.HOURS).setInitialDelay(next.timeInMillis - now.timeInMillis, TimeUnit.MILLISECONDS).build())
    }

    class Worker(private val c: Context, p: WorkerParameters) : CoroutineWorker(c, p) {
        override suspend fun doWork(): Result {
            val n = Db.get(c).dao().unreadNow()
            if (n > 0) postSnoozed(c, 9001, "Notivo özeti", "$n okunmamış bildirimin var", "Günlük özet")
            return Result.success()
        }
    }
}
