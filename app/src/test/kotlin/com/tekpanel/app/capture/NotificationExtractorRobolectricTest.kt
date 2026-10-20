package com.tekpanel.app.capture

import android.app.Notification
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Drives [NotificationExtractor] against *real* `android.app.Notification` objects built the way
 * chat apps build them (`NotificationCompat.Builder` + `MessagingStyle`, or plain `bigText`),
 * through Robolectric's simulation of the framework. Not a substitute for a real device, but far
 * stronger than hand-written payloads because the extras layout and MessagingStyle parsing run for real.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class NotificationExtractorRobolectricTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    @Test
    fun `extracts every message of a real MessagingStyle notification with its timestamp`() {
        val sbn = FakeSbn.chat(context, "com.whatsapp", "Ayşe Yılmaz", listOf("Randevu almak istiyorum" to 1_000L, "Yarın 14:00 uygun mu?" to 2_000L))

        val payload = NotificationExtractor.extract(context, sbn)
        val normalized = MessageNormalizer.normalize(payload)!!

        assertThat(normalized.conversationTitle).isEqualTo("Ayşe Yılmaz")
        assertThat(normalized.messages.map { it.messageText }).containsExactly("Randevu almak istiyorum", "Yarın 14:00 uygun mu?").inOrder()
        assertThat(normalized.messages.map { it.timestampMillis }).containsExactly(1_000L, 2_000L).inOrder()
        assertThat(payload.hasContentIntent).isTrue()
        assertThat(payload.notificationKey).isNotEmpty()
    }

    @Test
    fun `extracts sender and text from a real bigText notification`() {
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Mehmet Demir")
            .setContentText("kısa önizleme")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Saç kesimi için yarın uygun musunuz?"))
            .build()

        val normalized = MessageNormalizer.normalize(NotificationExtractor.extract(context, FakeSbn.wrap(context, "com.whatsapp", notification, id = 2)))!!

        assertThat(normalized.messages.single().senderName).isEqualTo("Mehmet Demir")
        assertThat(normalized.messages.single().messageText).isEqualTo("Saç kesimi için yarın uygun musunuz?")
    }

    @Test
    fun `flags a real group-summary notification so the filter engine rejects it`() {
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setGroup("thread-1")
            .setGroupSummary(true)
            .setContentTitle("3 new messages")
            .build()

        assertThat(NotificationExtractor.extract(context, FakeSbn.wrap(context, "com.whatsapp", notification, id = 3)).isGroupSummary).isTrue()
    }

    @Test
    fun `a group conversation is flagged as a group`() {
        val style = NotificationCompat.MessagingStyle(androidx.core.app.Person.Builder().setName("Me").build())
            .setConversationTitle("Kuaför Müşterileri")
            .setGroupConversation(true)
            .addMessage("selam", 1_000L, androidx.core.app.Person.Builder().setName("Ali").build())
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setStyle(style)
            .build()

        val payload = NotificationExtractor.extract(context, FakeSbn.wrap(context, "com.whatsapp", notification, id = 6))
        val normalized = MessageNormalizer.normalize(payload)!!

        assertThat(payload.isGroupConversation).isTrue()
        assertThat(normalized.conversationTitle).isEqualTo("Kuaför Müşterileri")
        assertThat(normalized.messages.single().senderName).isEqualTo("Ali")
    }

    /**
     * Some OEM ROMs ship malformed `EXTRA_MESSAGES`. androidx already guards the per-message parsing,
     * and [NotificationExtractor] wraps the call too; either way the notification must fall back to
     * the title/bigText instead of being lost.
     */
    @Test
    fun `falls back to bigText when MessagingStyle parsing yields nothing usable`() {
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Zeynep Aksu")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Ürün ne zaman kargoya verilir?"))
            .build()
        notification.extras.putParcelableArray(Notification.EXTRA_MESSAGES, arrayOf(Intent()))

        val payload = NotificationExtractor.extract(context, FakeSbn.wrap(context, "com.whatsapp", notification, id = 5))
        val normalized = MessageNormalizer.normalize(payload)!!

        assertThat(payload.messagingStyleMessages).isEmpty()
        assertThat(normalized.messages.single().senderName).isEqualTo("Zeynep Aksu")
        assertThat(normalized.messages.single().messageText).isEqualTo("Ürün ne zaman kargoya verilir?")
    }

    @Test
    fun `finds the inline reply action and the content intent of a live notification`() {
        val remoteInput = androidx.core.app.RemoteInput.Builder("reply_key").setLabel("Yanıtla").build()
        val replyPending = android.app.PendingIntent.getBroadcast(context, 0, Intent("reply"), android.app.PendingIntent.FLAG_MUTABLE)
        val action = NotificationCompat.Action.Builder(android.R.drawable.ic_menu_send, "Yanıtla", replyPending)
            .addRemoteInput(remoteInput)
            .build()
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Can")
            .setContentText("selam")
            .addAction(action)
            .build()
        val sbn = FakeSbn.wrap(context, "com.whatsapp", notification, id = 7)

        val live = LiveActionRegistry.fromNotification(sbn.notification)

        assertThat(live.canReply).isTrue()
        assertThat(live.contentIntent).isNotNull()
    }

    @Test
    fun `keeps the source app's own buttons but not its reply action`() {
        fun pending(name: String) = android.app.PendingIntent.getBroadcast(context, name.hashCode(), Intent(name), android.app.PendingIntent.FLAG_IMMUTABLE)
        val reply = NotificationCompat.Action.Builder(android.R.drawable.ic_menu_send, "Yanıtla", pending("reply"))
            .addRemoteInput(androidx.core.app.RemoteInput.Builder("k").build()).build()
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Can").setContentText("selam")
            .addAction(NotificationCompat.Action.Builder(android.R.drawable.ic_menu_view, "Okundu işaretle", pending("read")).build())
            .addAction(reply)
            .addAction(NotificationCompat.Action.Builder(android.R.drawable.ic_menu_add, "Beğen", pending("like")).build())
            .build()

        val live = LiveActionRegistry.fromNotification(FakeSbn.wrap(context, "com.whatsapp", notification, id = 8).notification)

        assertThat(live.actions.map { it.title }).containsExactly("Okundu işaretle", "Beğen").inOrder()
        assertThat(live.canReply).isTrue()
    }
}
