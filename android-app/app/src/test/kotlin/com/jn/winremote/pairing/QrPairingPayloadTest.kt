package com.jn.winremote.pairing

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QrPairingPayloadTest {

    private val fingerprint = "a".repeat(64)

    @Test
    fun `parses the canonical field names`() {
        val json = """{"host":"192.168.1.20","port":8787,"fingerprint":"$fingerprint","pairing_code":"7F3K-9QRT"}"""
        val payload = parseQrPairingPayload(json)
        assertEquals("192.168.1.20", payload?.host)
        assertEquals(8787, payload?.port)
        assertEquals(fingerprint, payload?.fingerprint)
        assertEquals("7F3K-9QRT", payload?.pairingCode)
    }

    @Test
    fun `normalizes an uppercase colon-separated fingerprint`() {
        val decorated = fingerprint.chunked(2).joinToString(":").uppercase()
        val json = """{"host":"h","port":8787,"fingerprint":"$decorated","pairing_code":"CODE"}"""
        val payload = parseQrPairingPayload(json)
        assertEquals(fingerprint, payload?.fingerprint)
    }

    @Test
    fun `defaults port to 8787 when missing`() {
        val json = """{"host":"h","fingerprint":"$fingerprint","pairing_code":"CODE"}"""
        assertEquals(8787, parseQrPairingPayload(json)?.port)
    }

    @Test
    fun `accepts synonym keys for fingerprint and pairing code`() {
        val json = """{"host":"h","port":8787,"cert_fingerprint":"$fingerprint","code":"CODE"}"""
        val payload = parseQrPairingPayload(json)
        assertEquals(fingerprint, payload?.fingerprint)
        assertEquals("CODE", payload?.pairingCode)
    }

    @Test
    fun `parses the actual key the Windows agent emits per PROTOCOL md section 10`() {
        // The windows-agent implementation emits "fingerprint_sha256", not
        // "fingerprint" - this is the real QR payload shape, not a synonym.
        val json = """{"host":"192.168.1.20","port":8787,"fingerprint_sha256":"$fingerprint","pairing_code":"7F3K-9QRT"}"""
        val payload = parseQrPairingPayload(json)
        assertEquals("192.168.1.20", payload?.host)
        assertEquals(fingerprint, payload?.fingerprint)
        assertEquals("7F3K-9QRT", payload?.pairingCode)
    }

    @Test
    fun `returns null for missing required fields`() {
        assertNull(parseQrPairingPayload("""{"host":"h"}"""))
        assertNull(parseQrPairingPayload("""{"port":8787}"""))
    }

    @Test
    fun `returns null for malformed json instead of throwing`() {
        assertNull(parseQrPairingPayload("not json at all"))
        assertNull(parseQrPairingPayload("[]"))
        assertNull(parseQrPairingPayload(""))
    }
}
