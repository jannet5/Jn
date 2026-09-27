package com.tekpanel.app.capture

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DuplicateGuardTest {

    @Test
    fun `a fresh key and fingerprint are not duplicates`() {
        val guard = DuplicateGuard()
        assertThat(guard.isLikelyDuplicate("key1", "fp1")).isFalse()
    }

    @Test
    fun `remembers by notification key`() {
        val guard = DuplicateGuard()
        guard.remember("key1", "fp1")
        assertThat(guard.isLikelyDuplicate("key1", "fp-different")).isTrue()
    }

    @Test
    fun `remembers by fingerprint even with a different or missing key`() {
        val guard = DuplicateGuard()
        guard.remember("key1", "fp1")
        assertThat(guard.isLikelyDuplicate(null, "fp1")).isTrue()
        assertThat(guard.isLikelyDuplicate("key-different", "fp1")).isTrue()
    }

    @Test
    fun `evicts the oldest entries past the configured capacity`() {
        val guard = DuplicateGuard(maxEntries = 2)
        guard.remember("key1", "fp1")
        guard.remember("key2", "fp2")
        guard.remember("key3", "fp3")

        assertThat(guard.isLikelyDuplicate("key1", "fp1")).isFalse()
        assertThat(guard.isLikelyDuplicate("key3", "fp3")).isTrue()
    }

    @Test
    fun `clear forgets everything`() {
        val guard = DuplicateGuard()
        guard.remember("key1", "fp1")
        guard.clear()
        assertThat(guard.isLikelyDuplicate("key1", "fp1")).isFalse()
    }
}
