package com.notivo.app.service

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.notivo.app.MainActivity
import com.notivo.app.R
import com.notivo.app.data.Db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Ana ekran widget'ı: okunmamış sayısı ve en yeni 3 bildirim. Dokununca Notivo açılır. */
class NotivoWidget : AppWidgetProvider() {
    override fun onUpdate(c: Context, m: AppWidgetManager, ids: IntArray) {
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try { render(c, m, ids) } finally { pending.finish() }
        }
    }

    companion object {
        /** Yerleştirilmiş tüm widget'ları günceller; hiç yoksa bir şey yapmaz. */
        suspend fun refreshNow(c: Context) {
            val m = AppWidgetManager.getInstance(c)
            val ids = m.getAppWidgetIds(ComponentName(c, NotivoWidget::class.java))
            if (ids.isNotEmpty()) render(c, m, ids)
        }

        private suspend fun render(c: Context, m: AppWidgetManager, ids: IntArray) {
            val dao = Db.get(c).dao()
            val unread = dao.unreadNow()
            val latest = dao.latest(3)
            val v = RemoteViews(c.packageName, R.layout.widget)
            v.setTextViewText(R.id.w_header, if (unread > 0) "Notivo · $unread okunmamış" else "Notivo")
            listOf(R.id.w_l1, R.id.w_l2, R.id.w_l3).forEachIndexed { i, rid ->
                val n = latest.getOrNull(i)
                v.setTextViewText(rid, if (n != null) "${n.appName}: ${n.title.ifBlank { n.text }}" else if (i == 0) "Henüz bildirim yok" else "")
            }
            v.setOnClickPendingIntent(R.id.w_root, PendingIntent.getActivity(c, 0, Intent(c, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE))
            m.updateAppWidget(ids, v)
        }
    }
}
