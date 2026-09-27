package com.jn.winremote.util

import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Every reason code PROTOCOL.md defines must have specific Turkish copy, not a generic fallback. */
class ReasonTextTest {

    private val allKnownReasonCodes = listOf(
        "invalid_code", "expired_code", "locked_out",
        "bad_hmac", "unknown_device", "revoked",
        "critical_process_protected", "not_found", "access_denied",
        "not_allow_listed", "launch_failed",
        "unauthenticated", "bad_request", "internal_error",
    )

    @Test
    fun `every protocol reason code has specific, non-generic Turkish text`() {
        for (code in allKnownReasonCodes) {
            val text = ReasonText.forCode(code)
            assertTrue("code=$code text=$text", text.isNotBlank())
            assertNotEquals("code=$code should not fall back to the generic message", "Bilinmeyen hata.", text)
        }
    }

    @Test
    fun `unknown reason code falls back gracefully instead of throwing`() {
        val text = ReasonText.forCode("some_future_reason_code")
        assertTrue(text.contains("some_future_reason_code"))
    }

    @Test
    fun `null reason code has a generic message`() {
        assertTrue(ReasonText.forCode(null).isNotBlank())
    }
}
