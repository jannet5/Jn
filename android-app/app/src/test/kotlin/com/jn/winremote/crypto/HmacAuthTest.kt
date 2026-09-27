package com.jn.winremote.crypto

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Base64

class HmacAuthTest {

    /** RFC 4231 test case 1: raw HMAC-SHA256 math, independent of our message framing. */
    @Test
    fun `hmacSha256 matches RFC 4231 test case 1`() {
        val key = ByteArray(20) { 0x0b }
        val data = "Hi There".toByteArray(Charsets.US_ASCII)
        val expected = hexToBytes("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7")

        val actual = HmacAuth.hmacSha256(key, data)

        assertEquals(32, actual.size)
        assertArrayEquals(expected, actual)
    }

    /**
     * Independently computed with Python's hmac/hashlib (see command in the task notes):
     *   key = bytes(range(32)), nonce = bytes(range(16)), device_id = "abc"
     *   hmac_b64 = base64(HMAC_SHA256(key, nonce || ":" || "abc"))
     */
    @Test
    fun `computeAuthResponse matches independently computed vector`() {
        val key = ByteArray(32) { it.toByte() }
        val nonce = ByteArray(16) { it.toByte() }
        val nonceB64 = Base64.getEncoder().encodeToString(nonce)
        val expected = "ltggF5+c1uXIEo719ExQ6Qfb3yJQoS5k83sZjvzyLU4="

        val actual = HmacAuth.computeAuthResponse(key, nonceB64, "abc")

        assertEquals(expected, actual)
    }

    @Test
    fun `different device_id changes the hmac (no key confusion across devices)`() {
        val key = ByteArray(32) { it.toByte() }
        val nonce = ByteArray(16) { it.toByte() }
        val nonceB64 = Base64.getEncoder().encodeToString(nonce)

        val a = HmacAuth.computeAuthResponse(key, nonceB64, "device-a")
        val b = HmacAuth.computeAuthResponse(key, nonceB64, "device-b")

        org.junit.Assert.assertNotEquals(a, b)
    }

    @Test
    fun `different nonce changes the hmac (replay resistance)`() {
        val key = ByteArray(32) { it.toByte() }
        val nonce1 = Base64.getEncoder().encodeToString(ByteArray(16) { it.toByte() })
        val nonce2 = Base64.getEncoder().encodeToString(ByteArray(16) { (it + 1).toByte() })

        val a = HmacAuth.computeAuthResponse(key, nonce1, "device-a")
        val b = HmacAuth.computeAuthResponse(key, nonce2, "device-a")

        org.junit.Assert.assertNotEquals(a, b)
    }

    private fun hexToBytes(hex: String): ByteArray =
        ByteArray(hex.length / 2) { i -> ((Character.digit(hex[i * 2], 16) shl 4) + Character.digit(hex[i * 2 + 1], 16)).toByte() }
}
