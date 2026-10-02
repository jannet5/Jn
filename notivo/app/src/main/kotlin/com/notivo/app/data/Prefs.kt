package com.notivo.app.data

import android.content.Context

class Prefs(c: Context) {
    private val sp = c.getSharedPreferences("notivo", Context.MODE_PRIVATE)
    var retentionDays: Int get() = sp.getInt("ret", 30); set(v) = sp.edit().putInt("ret", v).apply()
    var capturing: Boolean get() = sp.getBoolean("cap", true); set(v) = sp.edit().putBoolean("cap", v).apply()
    var hideOngoing: Boolean get() = sp.getBoolean("ongoing", true); set(v) = sp.edit().putBoolean("ongoing", v).apply()
    var hideSensitive: Boolean get() = sp.getBoolean("sens", false); set(v) = sp.edit().putBoolean("sens", v).apply()
    var appLock: Boolean get() = sp.getBoolean("lock", false); set(v) = sp.edit().putBoolean("lock", v).apply()
    var onboarded: Boolean get() = sp.getBoolean("onb", false); set(v) = sp.edit().putBoolean("onb", v).apply()
    var digestHour: Int get() = sp.getInt("digest", -1); set(v) = sp.edit().putInt("digest", v).apply()
}
