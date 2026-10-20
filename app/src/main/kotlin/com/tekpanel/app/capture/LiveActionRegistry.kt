package com.tekpanel.app.capture

import android.app.Notification
import android.app.PendingIntent
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.os.Bundle

/** What can still be done with a chat while the sending app's notification is alive. */
data class LiveConversation(
    val contentIntent: PendingIntent?,
    val replyAction: Notification.Action?,
    val replyInput: RemoteInput?,
) {
    val canReply: Boolean get() = replyAction?.actionIntent != null && replyInput != null
}

/**
 * Keeps, for the lifetime of the process only, the `contentIntent` (jump to the exact chat) and
 * the inline-reply action of each conversation's most recent notification. PendingIntents cannot
 * be serialized meaningfully, so after a process restart a conversation falls back to
 * "open the app"; the UI never promises more than this registry can deliver.
 */
class LiveActionRegistry(private val maxEntries: Int = 200) {

    private val byConversation = LinkedHashMap<String, LiveConversation>()

    @Synchronized
    fun put(conversationKey: String, live: LiveConversation) {
        byConversation.remove(conversationKey)
        byConversation[conversationKey] = live
        while (byConversation.size > maxEntries) {
            val oldest = byConversation.keys.iterator()
            if (oldest.hasNext()) {
                oldest.next()
                oldest.remove()
            }
        }
    }

    @Synchronized
    fun get(conversationKey: String): LiveConversation? = byConversation[conversationKey]

    @Synchronized
    fun clear() = byConversation.clear()

    /** Sends [text] through the sending app's own reply action. @return false if it cannot be sent. */
    fun sendReply(context: Context, conversationKey: String, text: String): Boolean {
        val live = get(conversationKey) ?: return false
        val input = live.replyInput ?: return false
        val pending = live.replyAction?.actionIntent ?: return false
        return try {
            val fillIn = Intent()
            val results = Bundle().apply { putCharSequence(input.resultKey, text) }
            RemoteInput.addResultsToIntent(arrayOf(input), fillIn, results)
            pending.send(context, 0, fillIn)
            true
        } catch (_: PendingIntent.CanceledException) {
            false
        } catch (_: RuntimeException) {
            false
        }
    }

    companion object {
        /** Picks the notification's free-text reply action, if the sending app offers one. */
        fun fromNotification(notification: Notification): LiveConversation {
            val reply = notification.actions.orEmpty().firstOrNull { action ->
                action.actionIntent != null && action.remoteInputs.orEmpty().any { it.allowFreeFormInput }
            }
            val input = reply?.remoteInputs.orEmpty().firstOrNull { it.allowFreeFormInput }
            return LiveConversation(notification.contentIntent, reply, input)
        }
    }
}
