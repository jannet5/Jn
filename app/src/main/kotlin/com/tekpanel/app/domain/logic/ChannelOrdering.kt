package com.tekpanel.app.domain.logic

import com.tekpanel.app.domain.model.Channel
import java.text.Collator
import java.util.Locale

/** Pure ordering rules for the two places channels are listed. */
object ChannelOrdering {

    private val collator: Collator = Collator.getInstance(Locale("tr", "TR")).apply { strength = Collator.PRIMARY }

    /**
     * Order of the picker when it opens: chosen apps first, then apps with message history
     * (most-messaged first), then well-known chat apps, then everything else A-Z. This is a
     * *base* order: the caller freezes it while the sheet is open and floats [touched] apps to
     * the top, so an app the user just tapped never jumps away from under the finger.
     */
    fun pickerBaseOrder(channels: List<Channel>): List<Channel> =
        channels.sortedWith(
            compareBy<Channel> { tier(it) }
                .thenByDescending { it.totalMessages }
                .thenComparator { a, b -> collator.compare(a.label, b.label) },
        )

    private fun tier(channel: Channel): Int = when {
        channel.enabled -> 0
        channel.totalMessages > 0 -> 1
        channel.isPopular -> 2
        else -> 3
    }

    /**
     * Applies the "touched stays on top" rule: apps in [touchedNewestFirst] come first in that
     * order, everything else keeps the frozen [baseOrder] positions.
     */
    fun withTouchedOnTop(baseOrder: List<String>, touchedNewestFirst: List<String>): List<String> {
        val touched = touchedNewestFirst.distinct().filter { it in baseOrder }
        return touched + baseOrder.filter { it !in touched }
    }

    /** Order of the bubble row on the main screen: most-messaged first, then A-Z. */
    fun barOrder(enabledChannels: List<Channel>): List<Channel> =
        enabledChannels.sortedWith(
            compareByDescending<Channel> { it.totalMessages }
                .thenComparator { a, b -> collator.compare(a.label, b.label) },
        )
}
