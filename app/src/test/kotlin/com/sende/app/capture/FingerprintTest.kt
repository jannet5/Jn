package com.sende.app.capture

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FingerprintTest {

    private val key = Fingerprint.conversationKey("com.whatsapp", "Ayşe Yılmaz")

    @Test
    fun `timed fingerprint is stable for the same message delivered again`() {
        val a = Fingerprint.forTimedMessage("com.whatsapp", key, "Ayşe", "Merhaba", 1_000L)
        val b = Fingerprint.forTimedMessage("com.whatsapp", key, "Ayşe", "Merhaba", 1_000L)
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `two real messages with the same text but different timestamps stay distinct`() {
        val first = Fingerprint.forTimedMessage("com.whatsapp", key, "Ayşe", "Tamam", 1_000L)
        val second = Fingerprint.forTimedMessage("com.whatsapp", key, "Ayşe", "Tamam", 4_000L)
        assertThat(first).isNotEqualTo(second)
    }

    @Test
    fun `untimed fingerprint is stable for the same delivery replayed`() {
        val a = Fingerprint.forUntimedMessage("com.whatsapp", key, "Ayşe", "Merhaba", 60_000L)
        val b = Fingerprint.forUntimedMessage("com.whatsapp", key, "Ayşe", "Merhaba", 60_000L)
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `untimed fingerprint separates the same text delivered at different times`() {
        val a = Fingerprint.forUntimedMessage("com.whatsapp", key, "Ayşe", "Merhaba", 60_000L)
        val b = Fingerprint.forUntimedMessage("com.whatsapp", key, "Ayşe", "Merhaba", 60_001L)
        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `different apps never share a fingerprint`() {
        val a = Fingerprint.forTimedMessage("com.whatsapp", key, "Ayşe", "Merhaba", 1_000L)
        val b = Fingerprint.forTimedMessage("com.whatsapp.w4b", key, "Ayşe", "Merhaba", 1_000L)
        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `fingerprint is a 64 char hex digest and never contains the message text`() {
        val fp = Fingerprint.forTimedMessage("com.whatsapp", key, "Ayşe", "gizli mesaj", 1_000L)
        assertThat(fp).matches("[0-9a-f]{64}")
        assertThat(fp).doesNotContain("gizli")
    }

    @Test
    fun `conversation key ignores case and spacing so the same chat always groups together`() {
        val a = Fingerprint.conversationKey("com.whatsapp", "Ayşe  Yılmaz")
        val b = Fingerprint.conversationKey("COM.WHATSAPP", " ayşe yılmaz ")
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `the same contact in WhatsApp and WhatsApp Business are different conversations`() {
        assertThat(Fingerprint.conversationKey("com.whatsapp", "Ayşe"))
            .isNotEqualTo(Fingerprint.conversationKey("com.whatsapp.w4b", "Ayşe"))
    }
}
