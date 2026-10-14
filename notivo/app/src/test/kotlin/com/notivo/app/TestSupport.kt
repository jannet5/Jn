package com.notivo.app

import android.app.Application
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.notivo.app.data.Db
import com.notivo.app.data.Notif
import org.robolectric.Shadows.shadowOf

/** Db.get() is a process-wide singleton; Robolectric reuses the JVM across tests, so drop it between tests. */
fun resetDbSingleton() {
    val f = Db::class.java.getDeclaredField("inst").apply { isAccessible = true }
    (f.get(null) as? Db)?.close()
    f.set(null, null)
    ApplicationProvider.getApplicationContext<Application>().deleteDatabase("notivo.db")
}

fun awaitUntil(timeoutMs: Long = 5000, cond: () -> Boolean) {
    val end = System.currentTimeMillis() + timeoutMs
    while (System.currentTimeMillis() < end) {
        shadowOf(Looper.getMainLooper()).idle()
        if (cond()) return
        Thread.sleep(25)
    }
    shadowOf(Looper.getMainLooper()).idle()
    check(cond()) { "condition not met within ${timeoutMs}ms" }
}

fun notif(pkg: String = "com.chat", title: String = "Ayşe", text: String = "Merhaba", time: Long = System.currentTimeMillis(),
          starred: Boolean = false, read: Boolean = false) =
    Notif(sbnKey = "k", pkg = pkg, appName = pkg, title = title, text = text, time = time, category = "msg", isMessage = true,
        read = read, starred = starred)
