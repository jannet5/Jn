package com.notibox.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.notibox.app.data.*
import com.notibox.app.service.NotiListener
import com.notibox.app.service.snooze
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
    fun snooze(n: Notif, min: Int) { snooze(getApplication(), n, min); delete(n) }

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
}
