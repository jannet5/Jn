package com.tekpanel.app.capture

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.UserHandle
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.core.app.Person

/** Builds real framework notification objects the way chat apps do (Robolectric simulates the framework). */
object FakeSbn {

    fun wrap(context: Context, packageName: String, notification: Notification, id: Int = 1, postTime: Long = System.currentTimeMillis()): StatusBarNotification {
        notification.contentIntent = PendingIntent.getActivity(context, 0, Intent(Intent.ACTION_MAIN), PendingIntent.FLAG_IMMUTABLE)
        return StatusBarNotification(
            packageName, packageName, id, "tag$id",
            android.os.Process.myUid(), android.os.Process.myPid(), 0, notification,
            UserHandle.getUserHandleForUid(android.os.Process.myUid()), postTime,
        )
    }

    fun plain(context: Context, packageName: String, title: String, text: String, id: Int = 1, postTime: Long = System.currentTimeMillis()): StatusBarNotification {
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .build()
        return wrap(context, packageName, notification, id, postTime)
    }

    /** A chat notification with several messages, like WhatsApp's one-notification-per-chat. */
    fun chat(
        context: Context,
        packageName: String,
        chatTitle: String,
        messages: List<Pair<String, Long>>,
        sender: String = chatTitle,
        id: Int = 1,
    ): StatusBarNotification {
        val person = Person.Builder().setName(sender).build()
        val style = NotificationCompat.MessagingStyle(Person.Builder().setName("Me").build())
        messages.forEach { (text, at) -> style.addMessage(text, at, person) }
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(chatTitle)
            .setStyle(style)
            .build()
        return wrap(context, packageName, notification, id, messages.maxOf { it.second })
    }
}
