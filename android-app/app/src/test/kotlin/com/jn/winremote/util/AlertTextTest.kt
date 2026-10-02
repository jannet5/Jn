package com.jn.winremote.util

import com.jn.winremote.protocol.AlertData
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AlertTextTest {

    private fun alert(kind: String, context: String?, message: String = "English agent text") = AlertData(
        ts = 0, id = "a", kind = kind, severity = "warning", message = message,
        context = context?.let { Json.parseToJsonElement(it) },
    )

    @Test
    fun `low free space uses volume and percent from context`() {
        // Exactly what the agent sends (runtime.go raiseAlert context).
        val text = AlertText.describe(alert("low_free_space", """{"volume":"C:\\","free_percent":7.3}"""))
        assertEquals("C:\\ sürücüsünde boş alan azaldı: %7,3 boş.", text)
    }

    @Test
    fun `large file shows path and human size`() {
        val text = AlertText.describe(alert("large_file", """{"path":"C:\\Users\\a\\big.iso","size_bytes":1073741824}"""))
        assertTrue(text, text.startsWith("Büyük dosya: C:\\Users\\a\\big.iso ("))
        assertTrue(text, text.endsWith("GB)"))
    }

    @Test
    fun `fast growth and disk fill rate are Turkish`() {
        assertTrue(AlertText.describe(alert("fast_growth", """{"path":"C:\\Videos","delta_bytes":209715200}""")).contains("büyüdü"))
        assertTrue(AlertText.describe(alert("disk_fill_rate", """{"volume":"D:\\","loss_percent":6.0}""")).contains("hızla doluyor"))
    }

    @Test
    fun `falls back to agent message when context is missing`() {
        assertEquals("English agent text", AlertText.describe(alert("low_free_space", null)))
        assertEquals("English agent text", AlertText.describe(alert("low_free_space", """{"unexpected":1}""")))
        assertEquals("English agent text", AlertText.describe(alert("some_future_kind", """{"volume":"C:\\"}""")))
    }
}
