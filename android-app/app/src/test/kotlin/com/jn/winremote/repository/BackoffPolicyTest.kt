package com.jn.winremote.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class BackoffPolicyTest {

    @Test
    fun `base sequence is 1s, 2s, 5s, 10s then capped at 10s`() {
        assertEquals(1_000L, BackoffPolicy.baseDelayMillis(1))
        assertEquals(2_000L, BackoffPolicy.baseDelayMillis(2))
        assertEquals(5_000L, BackoffPolicy.baseDelayMillis(3))
        assertEquals(10_000L, BackoffPolicy.baseDelayMillis(4))
        assertEquals(10_000L, BackoffPolicy.baseDelayMillis(5))
        assertEquals(10_000L, BackoffPolicy.baseDelayMillis(100))
    }

    @Test
    fun `attempt must be at least 1`() {
        org.junit.Assert.assertThrows(IllegalArgumentException::class.java) {
            BackoffPolicy.baseDelayMillis(0)
        }
    }

    @Test
    fun `jitter stays within plus or minus 20 percent of the base delay`() {
        val seededRandom = Random(42)
        for (attempt in 1..6) {
            val base = BackoffPolicy.baseDelayMillis(attempt)
            repeat(200) {
                val jittered = BackoffPolicy.jitteredDelayMillis(attempt, seededRandom)
                val lowerBound = (base * 0.8).toLong()
                val upperBound = (base * 1.2).toLong() + 1 // +1 for integer rounding slack
                assertTrue(
                    "attempt=$attempt base=$base jittered=$jittered out of bounds [$lowerBound,$upperBound]",
                    jittered in lowerBound..upperBound,
                )
            }
        }
    }

    @Test
    fun `jitter is deterministic for a given seed (regression safety)`() {
        val a = BackoffPolicy.jitteredDelayMillis(1, Random(7))
        val b = BackoffPolicy.jitteredDelayMillis(1, Random(7))
        assertEquals(a, b)
    }

    @Test
    fun `jitter never goes negative`() {
        val random = Random(1)
        repeat(1000) {
            assertTrue(BackoffPolicy.jitteredDelayMillis(1, random) >= 0)
        }
    }
}
