package com.sende.app.capture

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Payload shapes mirror the documented structure of real chat notifications (MessagingStyle for
 * modern chat apps, bigText/textLines for InboxStyle notifications that older versions and some
 * OEM SMS apps still post). They are representative fixtures, not a device capture.
 */
class MessageNormalizerTest {

    @Test
    fun `returns every MessagingStyle line, oldest first, each with its own timestamp`() {
        val payload = payloadOf(
            title = "Ayşe Yılmaz",
            text = "old preview",
            lines = listOf(
                MessagingLine(sender = "Ayşe Yılmaz", text = "Merhaba, randevu almak istiyorum", timestampMillis = 100, index = 0),
                MessagingLine(sender = "Ayşe Yılmaz", text = "Yarın saat 14:00 uygun mu?", timestampMillis = 200, index = 1),
            ),
        )

        val result = MessageNormalizer.normalize(payload)!!

        assertThat(result.conversationTitle).isEqualTo("Ayşe Yılmaz")
        assertThat(result.messages.map { it.messageText }).containsExactly(
            "Merhaba, randevu almak istiyorum",
            "Yarın saat 14:00 uygun mu?",
        ).inOrder()
        assertThat(result.messages.map { it.timestampMillis }).containsExactly(100L, 200L).inOrder()
    }

    @Test
    fun `group chat keeps the group name as conversation and each person as sender`() {
        val payload = payloadOf(
            title = "Kuaför Müşterileri",
            conversationTitle = "Kuaför Müşterileri",
            isGroup = true,
            lines = listOf(
                MessagingLine("Ali", "merhaba", 10, index = 0),
                MessagingLine("Zeynep", "fiyat bilgisi alabilir miyim?", 20, index = 1),
            ),
        )

        val result = MessageNormalizer.normalize(payload)!!

        assertThat(result.isGroupConversation).isTrue()
        assertThat(result.conversationTitle).isEqualTo("Kuaför Müşterileri")
        assertThat(result.messages.map { it.senderName }).containsExactly("Ali", "Zeynep").inOrder()
    }

    @Test
    fun `plain notification uses the longest of bigText and text so nothing is cut short`() {
        val full = "Saç kesimi için ne zaman gelebilirim, yarın uygun musunuz? Cumartesi de olabilir, esnek davranabilirim."
        val payload = payloadOf(title = "Mehmet Demir", text = full.take(40) + "…", bigText = "Mehmet Demir: $full")

        val result = MessageNormalizer.normalize(payload)!!

        assertThat(result.messages.single().messageText).isEqualTo(full)
        assertThat(result.messages.single().senderName).isEqualTo("Mehmet Demir")
    }

    @Test
    fun `plain text wins over a shorter bigText`() {
        val payload = payloadOf(title = "Can Öz", text = "Merhaba, ürün stokta var mı? Rengi lacivert olanı arıyorum.", bigText = "Merhaba")

        val result = MessageNormalizer.normalize(payload)!!

        assertThat(result.messages.single().messageText).startsWith("Merhaba, ürün stokta var mı?")
    }

    @Test
    fun `text lines become one message each with the sender split off`() {
        val payload = payloadOf(
            title = "Grup: Kuaför Müşterileri",
            textLines = listOf("Ali: merhaba", "Zeynep: fiyat bilgisi alabilir miyim?"),
        )

        val result = MessageNormalizer.normalize(payload)!!

        assertThat(result.messages.map { it.senderName }).containsExactly("Ali", "Zeynep").inOrder()
        assertThat(result.messages.last().messageText).isEqualTo("fiyat bilgisi alabilir miyim?")
    }

    @Test
    fun `strips a redundant sender colon prefix from plain text`() {
        val payload = payloadOf(title = "Selin Aksoy", text = "Selin Aksoy: siparişim ne zaman gelir?")

        val result = MessageNormalizer.normalize(payload)!!

        assertThat(result.messages.single().messageText).isEqualTo("siparişim ne zaman gelir?")
    }

    @Test
    fun `a picture-only line is kept even though it has no text`() {
        val payload = payloadOf(
            title = "Selin",
            lines = listOf(MessagingLine("Selin", "", 5, hasPicture = true, index = 0)),
        )

        val result = MessageNormalizer.normalize(payload)!!

        assertThat(result.messages.single().hasPicture).isTrue()
    }

    @Test
    fun `big picture on a plain notification marks its message as having a picture`() {
        val payload = payloadOf(title = "Selin", text = "foto gönderdi", hasBigPicture = true)

        assertThat(MessageNormalizer.normalize(payload)!!.messages.single().hasPicture).isTrue()
    }

    @Test
    fun `returns null when there is no usable content at all`() {
        assertThat(MessageNormalizer.normalize(payloadOf(title = "Instagram", text = null))).isNull()
    }
}
