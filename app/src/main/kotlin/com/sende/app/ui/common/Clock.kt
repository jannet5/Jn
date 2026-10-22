package com.sende.app.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import kotlinx.coroutines.delay
import java.time.Instant

/** "Now", refreshed every half minute, so "2 dk. önce" keeps counting while the screen stays open. */
val LocalNow = compositionLocalOf { Instant.now() }

@Composable
fun ProvideClock(content: @Composable () -> Unit) {
    val now by produceState(Instant.now()) {
        while (true) {
            delay(CLOCK_TICK_MILLIS)
            value = Instant.now()
        }
    }
    CompositionLocalProvider(LocalNow provides now, content = content)
}

private const val CLOCK_TICK_MILLIS = 30_000L
