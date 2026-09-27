package com.jn.winremote.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FormattingTest {

    @Test
    fun `bytes formats zero and small values`() {
        assertEquals("0 B", Formatting.bytes(0))
        assertEquals("0 B", Formatting.bytes(-5))
        assertEquals("512 B", Formatting.bytes(512))
    }

    @Test
    fun `bytes steps through units`() {
        assertEquals("1 KB", Formatting.bytes(1024))
        assertTrue(Formatting.bytes(1536).endsWith("KB"))
        assertTrue(Formatting.bytes(1024L * 1024).endsWith("MB"))
        assertTrue(Formatting.bytes(1024L * 1024 * 1024).endsWith("GB"))
        assertTrue(Formatting.bytes(1024L * 1024 * 1024 * 1024).endsWith("TB"))
    }

    @Test
    fun `signedBytes prefixes sign`() {
        assertTrue(Formatting.signedBytes(1024).startsWith("+"))
        assertTrue(Formatting.signedBytes(-1024).startsWith("-"))
    }

    @Test
    fun `percent formats with one decimal by default`() {
        assertEquals("23,4%", Formatting.percent(23.4))
    }

    @Test
    fun `percent supports custom fraction digits`() {
        assertEquals("23%", Formatting.percent(23.4, 0))
    }

    @Test
    fun `timestamp does not throw for epoch zero or large values`() {
        assertTrue(Formatting.timestamp(0).isNotBlank())
        assertTrue(Formatting.timestamp(4_000_000_000L).isNotBlank())
    }
}
