package com.tekpanel.app.capture

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FingerprintTest {

    @Test
    fun `is stable for identical inputs`() {
        val a = Fingerprint.of("com.whatsapp", "Ali Veli", "merhaba", 1_000_000L)
        val b = Fingerprint.of("com.whatsapp", "Ali Veli", "merhaba", 1_000_000L)
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `is case and whitespace insensitive`() {
        val a = Fingerprint.of("com.whatsapp", "Ali Veli", "merhaba", 1_000_000L)
        val b = Fingerprint.of("com.whatsapp", "  ALI VELI  ", "MERHABA", 1_000_000L)
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `absorbs small jitter within the same time bucket`() {
        val a = Fingerprint.of("com.whatsapp", "Ali Veli", "merhaba", 1_000_000L)
        val b = Fingerprint.of("com.whatsapp", "Ali Veli", "merhaba", 1_000_400L)
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `differs across a real gap in time`() {
        val a = Fingerprint.of("com.whatsapp", "Ali Veli", "merhaba", 0L)
        val b = Fingerprint.of("com.whatsapp", "Ali Veli", "merhaba", 5 * 60_000L)
        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `differs when the message text differs`() {
        val a = Fingerprint.of("com.whatsapp", "Ali Veli", "merhaba", 0L)
        val b = Fingerprint.of("com.whatsapp", "Ali Veli", "iyi günler", 0L)
        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `never contains the raw message text`() {
        val fingerprint = Fingerprint.of("com.whatsapp", "Ali Veli", "gizli müşteri mesajı", 0L)
        assertThat(fingerprint).doesNotContain("gizli")
        assertThat(fingerprint).doesNotContain("müşteri")
    }
}
