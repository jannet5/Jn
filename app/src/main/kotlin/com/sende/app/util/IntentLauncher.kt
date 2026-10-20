package com.sende.app.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.sende.app.capture.LiveActionRegistry

/** Outcome of an "open the source" attempt, used to decide what feedback to show. */
enum class OpenSourceResult {
    OPENED_LIVE_CONVERSATION,
    OPENED_APP,
    FAILED,
}

/**
 * Fallback order for "open this conversation":
 * 1. the live notification `contentIntent`, if the process still holds it (jumps to the exact chat);
 * 2. otherwise a plain launch of the source app;
 * 3. otherwise report failure so the UI can say so.
 */
class IntentLauncher(
    private val context: Context,
    private val liveActionRegistry: LiveActionRegistry,
) {

    fun openConversation(conversationKey: String, packageName: String): OpenSourceResult {
        liveActionRegistry.get(conversationKey)?.contentIntent?.let { pendingIntent ->
            if (trySendPendingIntent(pendingIntent)) return OpenSourceResult.OPENED_LIVE_CONVERSATION
        }
        return if (tryLaunchPackage(packageName)) OpenSourceResult.OPENED_APP else OpenSourceResult.FAILED
    }

    fun actionTitles(conversationKey: String): List<String> = liveActionRegistry.get(conversationKey)?.actions.orEmpty().map { it.title }

    fun fireAction(conversationKey: String, index: Int): Boolean = liveActionRegistry.fireAction(conversationKey, index)

    fun canReply(conversationKey: String): Boolean = liveActionRegistry.get(conversationKey)?.canReply == true

    fun reply(conversationKey: String, text: String): Boolean = liveActionRegistry.sendReply(context, conversationKey, text)

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
