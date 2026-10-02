package com.notivo.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.notivo.app.data.*
import com.notivo.app.service.NotiListener
import com.notivo.app.service.snooze as scheduleSnooze
import com.notivo.app.service.Digest
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class Vm(app: Application) : AndroidViewModel(app) {
    private val dao = Db.get(app).dao()
    val prefs = Prefs(app)
    val all = dao.all().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val apps = dao.appCounts().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val rules = dao.rules().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val excluded = dao.excluded().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val perDay = dao.perDay(System.currentTimeMillis() - 14 * 86_400_000L).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun read(n: Notif) = viewModelScope.launch { dao.markRead(n.id) }
    fun readAll() = viewModelScope.launch { dao.markAllRead() }
    fun star(n: Notif) = viewModelScope.launch { dao.toggleStar(n.id) }
    fun delete(n: Notif) = viewModelScope.launch { dao.delete(n.id) }
    fun clear() = viewModelScope.launch { dao.clearUnstarred() }
    fun deleteApp(pkg: String) = viewModelScope.launch { dao.deleteApp(pkg) }
    fun addRule(pkg: String, kw: String, action: String) = viewModelScope.launch { dao.addRule(Rule(pkg = pkg, keyword = kw.trim(), action = action)) }
    fun delRule(r: Rule) = viewModelScope.launch { dao.delRule(r.id) }
    fun toggleRule(r: Rule) = viewModelScope.launch { dao.toggleRule(r.id) }
    fun setExcluded(pkg: String, on: Boolean) = viewModelScope.launch { if (on) dao.exclude(Excluded(pkg)) else dao.include(pkg) }
    fun snooze(n: Notif, min: Int) = viewModelScope.launch { scheduleSnooze(getApplication(), n, min); dao.delete(n.id) }

    /** Opens the original notification's target (only possible while the process holds the live notification). */
    fun open(n: Notif): Boolean {
        read(n)
        val sbn = NotiListener.cache[n.id]
        try { sbn?.notification?.contentIntent?.send(); if (sbn != null) return true } catch (_: Exception) {}
        val i = getApplication<Application>().packageManager.getLaunchIntentForPackage(n.pkg) ?: return false
        i.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK); getApplication<Application>().startActivity(i); return true
    }

    fun exportCsv(): String = buildString {
        appendLine("time,app,title,text")
        fun q(s: String) = "\"" + s.replace("\"", "\"\"") + "\""
        all.value.forEach { appendLine("${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US).format(it.time)},${q(it.appName)},${q(it.title)},${q(it.text)}") }
    }

    fun setDigest(h: Int) { prefs.digestHour = h; Digest.schedule(getApplication()) }

    /** Latest live notification of a conversation that exposes a reply (RemoteInput) action, if the process still holds it. */
    private fun replyAction(pkg: String, title: String) =
        NotiListener.cache.values.filter { it.packageName == pkg && it.notification.extras.getCharSequence(android.app.Notification.EXTRA_TITLE)?.toString() == title }
            .sortedByDescending { it.postTime }
            .firstNotNullOfOrNull { sbn -> sbn.notification.actions?.firstOrNull { a -> a.remoteInputs?.isNotEmpty() == true } }

    fun canReply(pkg: String, title: String) = replyAction(pkg, title) != null

    fun reply(pkg: String, title: String, text: String): Boolean {
        val a = replyAction(pkg, title) ?: return false
        val ri = a.remoteInputs.first()
        val intent = android.content.Intent()
        val b = android.os.Bundle().apply { putCharSequence(ri.resultKey, text) }
        android.app.RemoteInput.addResultsToIntent(a.remoteInputs, intent, b)
        return try { a.actionIntent.send(getApplication(), 0, intent); true } catch (_: Exception) { false }
    }

    fun backupJson(): String {
        val arr = JSONArray()
        all.value.forEach { n -> arr.put(JSONObject().put("pkg", n.pkg).put("app", n.appName).put("title", n.title).put("text", n.text)
            .put("time", n.time).put("cat", n.category).put("msg", n.isMessage).put("read", n.read).put("star", n.starred).put("del", n.deletedBySender)) }
        return JSONObject().put("app", "notivo").put("version", 1).put("items", arr).toString()
    }

    /** Returns number of imported items, or -1 for a file that is not a Notivo backup. */
    suspend fun restore(json: String): Int = try {
        val o = JSONObject(json)
        require(o.optString("app") == "notivo")
        val arr = o.getJSONArray("items"); var n = 0
        for (i in 0 until arr.length()) {
            val j = arr.getJSONObject(i)
            val pkg = j.getString("pkg"); val t = j.getString("title"); val x = j.getString("text"); val time = j.getLong("time")
            if (dao.sameCount(pkg, t, x, time) == 0) {
                dao.insert(Notif(sbnKey = "", pkg = pkg, appName = j.optString("app", pkg), title = t, text = x, time = time,
                    category = j.optString("cat"), isMessage = j.optBoolean("msg"), read = j.optBoolean("read"),
                    starred = j.optBoolean("star"), deletedBySender = j.optBoolean("del"))); n++
            }
        }
        n
    } catch (_: Exception) { -1 }
}
