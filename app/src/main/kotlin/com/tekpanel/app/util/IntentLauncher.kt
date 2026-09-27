package com.tekpanel.app.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tekpanel.app.capture.LiveIntentRegistry

/** Outcome of a CAP-14 "open source" attempt, used to decide what feedback to show. */
enum class OpenSourceResult {
    OPENED_LIVE_CONVERSATION,
    OPENED_APP,
    FAILED,
}

/**
 * Implements the CAP-14 fallback order exactly as specified:
 * 1. the live notification `contentIntent`, if the process still holds it;
 * 2. otherwise a plain launch of the source app;
 * 3. otherwise report failure so the UI can show "Uygulama açılamadı".
 */
class IntentLauncher(
    private val context: Context,
    private val liveIntentRegistry: LiveIntentRegistry,
) {

    fun openSource(messageId: String, packageName: String): OpenSourceResult {
        liveIntentRegistry.get(messageId)?.let { pendingIntent ->
            if (trySendPendingIntent(pendingIntent)) return OpenSourceResult.OPENED_LIVE_CONVERSATION
        }
        return if (tryLaunchPackage(packageName)) OpenSourceResult.OPENED_APP else OpenSourceResult.FAILED
    }

    private fun trySendPendingIntent(pendingIntent: PendingIntent): Boolean =
        try {
            pendingIntent.send()
            true
        } catch (_: PendingIntent.CanceledException) {
            false
        }

    private fun tryLaunchPackage(packageName: String): Boolean {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName) ?: return false
        return try {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
            true
        } catch (_: Exception) {
            false
        }
    }
}
