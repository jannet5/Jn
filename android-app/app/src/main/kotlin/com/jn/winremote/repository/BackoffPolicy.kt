package com.jn.winremote.repository

import kotlin.random.Random

/**
 * Exponential reconnect backoff: 1s, 2s, 5s, 10s, then capped at 10s,
 * with jitter so many clients reconnecting at once don't thunder-herd.
 */
object BackoffPolicy {

    private val stepsMillis = longArrayOf(1_000, 2_000, 5_000, 10_000)
    const val MAX_DELAY_MILLIS: Long = 10_000
    private const val JITTER_FRACTION = 0.2 // +/- 20%

    /** attempt is 1-based: attempt 1 is the first retry after the initial drop. */
    fun baseDelayMillis(attempt: Int): Long {
        require(attempt >= 1) { "attempt must be >= 1" }
        val index = (attempt - 1).coerceAtMost(stepsMillis.lastIndex)
        return stepsMillis[index]
    }

    fun jitteredDelayMillis(attempt: Int, random: Random = Random.Default): Long {
        val base = baseDelayMillis(attempt)
        val jitterRange = (base * JITTER_FRACTION).toLong().coerceAtLeast(1)
        val offset = random.nextLong(-jitterRange, jitterRange + 1)
        return (base + offset).coerceAtLeast(0)
    }
}
