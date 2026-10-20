package com.sende.app.domain

import com.google.common.truth.Truth.assertThat
import com.sende.app.domain.logic.ChannelOrdering
import com.sende.app.domain.model.Channel
import org.junit.Test

class ChannelOrderingTest {

    private fun channel(pkg: String, label: String = pkg, enabled: Boolean = false, total: Int = 0, popular: Boolean = false) =
        Channel(pkg, label, enabled, total, unreadMessages = 0, lastMessageAt = 0, isPopular = popular)

    @Test
    fun `picker puts chosen apps first, then apps with history, then popular, then the rest A to Z`() {
        val order = ChannelOrdering.pickerBaseOrder(
            listOf(
                channel("z", "Zebra"),
                channel("a", "Arı"),
                channel("pop", "Telegram", popular = true),
                channel("hist", "Eski", total = 3),
                channel("on", "Seçili", enabled = true, total = 1),
            ),
        ).map { it.packageName }

        assertThat(order).containsExactly("on", "hist", "pop", "a", "z").inOrder()
    }

    @Test
    fun `among chosen apps the most-messaged comes first`() {
        val order = ChannelOrdering.pickerBaseOrder(
            listOf(
                channel("few", "A", enabled = true, total = 2),
                channel("many", "B", enabled = true, total = 50),
            ),
        ).map { it.packageName }

        assertThat(order).containsExactly("many", "few").inOrder()
    }

    @Test
    fun `A to Z uses Turkish collation so dotted and dotless i sort correctly`() {
        val order = ChannelOrdering.pickerBaseOrder(
            listOf(channel("i1", "İstanbul"), channel("c", "Çay"), channel("a", "Ada"), channel("s", "Şeker")),
        ).map { it.label }

        assertThat(order).containsExactly("Ada", "Çay", "İstanbul", "Şeker").inOrder()
    }

    @Test
    fun `touched apps float to the top newest first and stay there`() {
        val base = listOf("a", "b", "c", "d")

        assertThat(ChannelOrdering.withTouchedOnTop(base, listOf("d", "b"))).containsExactly("d", "b", "a", "c").inOrder()
    }

    @Test
    fun `touching an app that is no longer listed changes nothing`() {
        assertThat(ChannelOrdering.withTouchedOnTop(listOf("a", "b"), listOf("zzz"))).containsExactly("a", "b").inOrder()
    }

    @Test
    fun `bubble row orders by message count then name`() {
        val order = ChannelOrdering.barOrder(
            listOf(channel("x", "Beta", total = 1), channel("y", "Alfa", total = 1), channel("z", "Gama", total = 9)),
        ).map { it.packageName }

        assertThat(order).containsExactly("z", "y", "x").inOrder()
    }
}
