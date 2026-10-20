package com.tekpanel.app.capture

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DuplicateGuardTest {

    @Test
    fun `unknown fingerprint is not a duplicate`() {
        assertThat(DuplicateGuard().isKnown("a")).isFalse()
    }

    @Test
    fun `remembered fingerprint is a duplicate`() {
        val guard = DuplicateGuard()
        guard.remember("a")
        assertThat(guard.isKnown("a")).isTrue()
    }

    @Test
    fun `oldest entries are evicted past the cap`() {
        val guard = DuplicateGuard(maxEntries = 2)
        guard.remember("a")
        guard.remember("b")
        guard.remember("c")
        assertThat(guard.isKnown("a")).isFalse()
        assertThat(guard.isKnown("c")).isTrue()
    }

    @Test
    fun `clear forgets everything`() {
        val guard = DuplicateGuard()
        guard.remember("a")
        guard.clear()
        assertThat(guard.isKnown("a")).isFalse()
    }
}
