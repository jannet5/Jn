package com.tekpanel.app.capture

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.UserHandle
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unlike [MessageNormalizerTest], this drives [NotificationExtractor] against *real*
 * `android.app.Notification`/`StatusBarNotification` objects built the same way WhatsApp/
 * Messenger/SMS apps build theirs (`NotificationCompat.Builder` + `MessagingStyle`, or
 * plain `bigText`), via Robolectric's simulation of the real Android framework classes.
 * This is not a substitute for a real device (no KVM/hardware virtualization is available
 * in this sandbox to run an emulator -- see docs/DECISIONS.md), but it is a materially
 * stronger check than hand-written `RawNotificationPayload` fixtures because the extraction
 * code path (extras bundle layout, MessagingStyle parsing) is exercised for real.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class NotificationExtractorRobolectricTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    private fun wrap(notification: Notification, key: Int = 1): StatusBarNotification {
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(Intent.ACTION_MAIN),
            PendingIntent.FLAG_IMMUTABLE,
        )
        notification.contentIntent = pendingIntent
        return StatusBarNotification(
            context.packageName,
            context.packageName,
            key,
            "tag$key",
            android.os.Process.myUid(),
            android.os.Process.myPid(),
            0,
            notification,
            UserHandle.getUserHandleForUid(android.os.Process.myUid()),
            System.currentTimeMillis(),
        )
    }

    @Test
    fun `extracts sender and text from a real MessagingStyle notification`() {
        val person = Person.Builder().setName("Ayşe Yılmaz").build()
        val style = NotificationCompat.MessagingStyle(Person.Builder().setName("Me").build())
            .addMessage("Randevu almak istiyorum", System.currentTimeMillis() - 60_000, person)
            .addMessage("Yarın 14:00 uygun mu?", System.currentTimeMillis(), person)

        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setStyle(style)
            .setContentTitle("Ayşe Yılmaz")
            .build()

        val payload = NotificationExtractor.extract(wrap(notification))

        assertThat(payload.messagingStyleMessages).isNotEmpty()
        val normalized = MessageNormalizer.normalize(payload)
        assertThat(normalized?.senderName).isEqualTo("Ayşe Yılmaz")
        assertThat(normalized?.messageText).isEqualTo("Yarın 14:00 uygun mu?")
        assertThat(payload.hasContentIntent).isTrue()
        assertThat(payload.notificationKey).isNotEmpty()
    }

    @Test
    fun `extracts sender and text from a real bigText notification`() {
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Mehmet Demir")
            .setContentText("kısa önizleme")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Saç kesimi için yarın uygun musunuz?"),
            )
            .build()

        val payload = NotificationExtractor.extract(wrap(notification, key = 2))
        val normalized = MessageNormalizer.normalize(payload)

        assertThat(normalized?.senderName).isEqualTo("Mehmet Demir")
        assertThat(normalized?.messageText).isEqualTo("Saç kesimi için yarın uygun musunuz?")
    }

    @Test
    fun `flags a real group-summary notification so the filter engine rejects it`() {
        val notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setGroup("thread-1")
            .setGroupSummary(true)
            .setContentTitle("3 new messages")
            .build()

        val payload = NotificationExtractor.extract(wrap(notification, key = 3))

        assertThat(payload.isGroupSummary).isTrue()
    }

    @Test
    fun `two independently built notifications with the same content produce the same fingerprint`() {
        fun build(): Notification = NotificationCompat.Builder(context, "channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Can Öz")
            .setContentText("Merhaba, ürün stokta var mı?")
            .build()

        val payloadA = NotificationExtractor.extract(wrap(build(), key = 4))
        val payloadB = NotificationExtractor.extract(wrap(build(), key = 4))

        val a = MessageNormalizer.normalize(payloadA)!!
        val b = MessageNormalizer.normalize(payloadB)!!
        val fingerprintA = Fingerprint.of(payloadA.packageName, a.senderName, a.messageText, payloadA.postTimeEpochMillis)
        val fingerprintB = Fingerprint.of(payloadB.packageName, b.senderName, b.messageText, payloadB.postTimeEpochMillis)

        assertThat(fingerprintA).isEqualTo(fingerprintB)
    }
}
