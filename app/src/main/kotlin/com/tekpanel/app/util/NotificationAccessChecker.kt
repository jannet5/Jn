package com.tekpanel.app.util

import android.content.Context
import androidx.core.app.NotificationManagerCompat

/** CAP-01: whether the user has granted TekPanel notification access, checked on demand. */
class NotificationAccessChecker(private val context: Context) {

    fun isGranted(): Boolean {
        val enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(context)
        return context.packageName in enabledPackages
    }
}
