package com.tekpanel.app.capture

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Payload shapes below mirror the documented structure of real WhatsApp/Instagram/TikTok
 * chat notifications (MessagingStyle for modern chat notifications, bigText/textLines for
 * the InboxStyle notifications older app versions and some OEM SMS apps still post). They
 * are representative fixtures, not a device capture -- see docs/DECISIONS.md for the
 * still-open item to validate against notifications from real installs of each app.
 */
class MessageNormalizerTest {

    private fun basePayload(
        title: String? = null,
        text: String? = null,
        bigText: String? = null,
        textLines: List<String> = emptyList(),
        conversationTitle: String? = null,
        messagingStyleMessages: List<MessagingLine> = emptyList(),
    ) = RawNotificationPayload(
        notificationKey = "key",
        packageName = "com.whatsapp",
        postTimeEpochMillis = 1_000_000L,
        title = title,
        text = text,
        bigText = bigText,
        textLines = textLines,
        conversationTitle = conversationTitle,
        messagingStyleMessages = messagingStyleMessages,
        category = null,
        isGroupSummary = false,
        isOngoing = false,
        hasContentIntent = true,
    )

    @Test
    fun `prefers the last MessagingStyle message over everything else`() {
        val payload = basePayload(
            title = "Ayşe Yılmaz",
            text = "old preview",
            messagingStyleMessages = listOf(
                MessagingLine(sender = "Ayşe Yılmaz", text = "Merhaba, randevu almak istiyorum"),
                MessagingLine(sender = "Ayşe Yılmaz", text = "Yarın saat 14:00 uygun mu?"),
            ),
        )

        val result = MessageNormalizer.normalize(payload)

        assertThat(result?.senderName).isEqualTo("Ayşe Yılmaz")
        assertThat(result?.messageText).isEqualTo("Yarın saat 14:00 uygun mu?")
    }

    @Test
    fun `falls back to bigText when there is no MessagingStyle`() {
        val payload = basePayload(
            title = "Mehmet Demir",
            text = "Kısa önizleme",
            bigText = "Mehmet Demir: Saç kesimi için ne zaman gelebilirim, yarın uygun musunuz?",
        )

        val result = MessageNormalizer.normalize(payload)

        assertThat(result?.senderName).isEqualTo("Mehmet Demir")
        assertThat(result?.messageText).isEqualTo("Saç kesimi için ne zaman gelebilirim, yarın uygun musunuz?")
    }

    @Test
    fun `falls back to the last text line when there is no bigText`() {
        val payload = basePayload(
            title = "Grup: Kuaför Müşterileri",
            textLines = listOf(
                "Ali: merhaba",
                "Zeynep: fiyat bilgisi alabilir miyim?",
            ),
        )

        val result = MessageNormalizer.normalize(payload)

        assertThat(result?.senderName).isEqualTo("Zeynep")
        assertThat(result?.messageText).isEqualTo("fiyat bilgisi alabilir miyim?")
    }

    @Test
    fun `falls back to plain text as a last resort`() {
        val payload = basePayload(title = "Can Öz", text = "Merhaba, ürün stokta var mı?")

        val result = MessageNormalizer.normalize(payload)

        assertThat(result?.senderName).isEqualTo("Can Öz")
        assertThat(result?.messageText).isEqualTo("Merhaba, ürün stokta var mı?")
    }

    @Test
    fun `returns null when there is no usable content at all`() {
        val payload = basePayload(title = "Instagram", text = null)

        val result = MessageNormalizer.normalize(payload)

        assertThat(result).isNull()
    }

    @Test
    fun `strips a redundant sender colon prefix from plain text`() {
        val payload = basePayload(title = "Selin Aksoy", text = "Selin Aksoy: siparişim ne zaman gelir?")

        val result = MessageNormalizer.normalize(payload)

        assertThat(result?.messageText).isEqualTo("siparişim ne zaman gelir?")
    }
}
