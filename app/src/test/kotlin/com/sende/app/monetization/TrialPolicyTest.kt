package com.sende.app.monetization

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TrialPolicyTest {

    private val day = 24L * 60 * 60 * 1000
    private val installed = 1_000 * day

    private fun gate(mode: MonetizationMode, daysSinceInstall: Double, supporter: Boolean = false, dismissedOnDay: Long? = null) =
        TrialPolicy.gate(mode, installed, installed + (daysSinceInstall * day).toLong(), supporter, dismissedOnDay)

    @Test
    fun `donate mode never asks for anything, even years later`() {
        assertThat(gate(MonetizationMode.DONATE, 0.0)).isEqualTo(Gate.Open)
        assertThat(gate(MonetizationMode.DONATE, 400.0)).isEqualTo(Gate.Open)
    }

    @Test
    fun `silent trial shows nothing during the three free days`() {
        assertThat(gate(MonetizationMode.TRIAL_SILENT, 0.0)).isEqualTo(Gate.Open)
        assertThat(gate(MonetizationMode.TRIAL_SILENT, 2.9)).isEqualTo(Gate.Open)
    }

    @Test
    fun `silent trial shows the plans once the third day is over`() {
        assertThat(gate(MonetizationMode.TRIAL_SILENT, 3.0)).isEqualTo(Gate.Paywall)
        assertThat(gate(MonetizationMode.TRIAL_SILENT, 30.0)).isEqualTo(Gate.Paywall)
    }

    @Test
    fun `warning trial counts the free days down`() {
        assertThat(gate(MonetizationMode.TRIAL_WARN, 0.1)).isEqualTo(Gate.Notice(3))
        assertThat(gate(MonetizationMode.TRIAL_WARN, 1.5)).isEqualTo(Gate.Notice(2))
        assertThat(gate(MonetizationMode.TRIAL_WARN, 2.9)).isEqualTo(Gate.Notice(1))
        assertThat(gate(MonetizationMode.TRIAL_WARN, 3.1)).isEqualTo(Gate.Paywall)
    }

    @Test
    fun `a supporter is never asked`() {
        assertThat(gate(MonetizationMode.TRIAL_SILENT, 90.0, supporter = true)).isEqualTo(Gate.Open)
        assertThat(gate(MonetizationMode.TRIAL_WARN, 1.0, supporter = true)).isEqualTo(Gate.Open)
    }

    @Test
    fun `not now keeps it quiet for the rest of that day only`() {
        val today = TrialPolicy.epochDay(installed + (10 * day))
        assertThat(gate(MonetizationMode.TRIAL_SILENT, 10.0, dismissedOnDay = today)).isEqualTo(Gate.Open)
        assertThat(gate(MonetizationMode.TRIAL_SILENT, 11.0, dismissedOnDay = today)).isEqualTo(Gate.Paywall)
    }

    @Test
    fun `a clock set before the install time does not crash or lock`() {
        assertThat(gate(MonetizationMode.TRIAL_SILENT, -5.0)).isEqualTo(Gate.Open)
    }

    @Test
    fun `mode parsing falls back to donate for anything unknown`() {
        assertThat(MonetizationMode.parse("trial_warn")).isEqualTo(MonetizationMode.TRIAL_WARN)
        assertThat(MonetizationMode.parse(" TRIAL_SILENT ")).isEqualTo(MonetizationMode.TRIAL_SILENT)
        assertThat(MonetizationMode.parse("")).isEqualTo(MonetizationMode.DONATE)
        assertThat(MonetizationMode.parse("hard_lock")).isEqualTo(MonetizationMode.DONATE)
    }
}
