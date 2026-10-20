package com.tekpanel.app.monetization

/**
 * How a build asks for money. DONATE is the default and never locks anything. The two trial modes
 * exist only so two groups of ~20 testers can try "say nothing during the 3 free days" against
 * "show a small reminder" (each variant is uploaded to its own Play closed-testing track).
 */
enum class MonetizationMode {
    DONATE,
    TRIAL_SILENT,
    TRIAL_WARN;

    companion object {
        fun parse(value: String): MonetizationMode = when (value.trim().lowercase()) {
            "trial_silent" -> TRIAL_SILENT
            "trial_warn" -> TRIAL_WARN
            else -> DONATE
        }
    }
}

/** What the screen should do about payment right now. */
sealed interface Gate {
    /** Nothing to show. */
    data object Open : Gate

    /** Free days left, shown as a small quiet chip (TRIAL_WARN only). */
    data class Notice(val daysLeft: Int) : Gate

    /** Trial over and not a supporter: show the plans, dismissible, once a day. */
    data object Paywall : Gate
}

/** Pure time rules for the trial; no Android, no billing, fully unit-tested. */
object TrialPolicy {

    const val TRIAL_DAYS = 3
    private const val DAY_MS = 24L * 60 * 60 * 1000

    fun gate(
        mode: MonetizationMode,
        firstInstallMillis: Long,
        nowMillis: Long,
        supporter: Boolean,
        paywallDismissedOnDay: Long?,
    ): Gate {
        if (supporter || mode == MonetizationMode.DONATE) return Gate.Open
        val elapsed = (nowMillis - firstInstallMillis).coerceAtLeast(0)
        if (elapsed < TRIAL_DAYS * DAY_MS) {
            val daysLeft = (TRIAL_DAYS - (elapsed / DAY_MS)).toInt()
            return if (mode == MonetizationMode.TRIAL_WARN) Gate.Notice(daysLeft) else Gate.Open
        }
        return if (paywallDismissedOnDay == epochDay(nowMillis)) Gate.Open else Gate.Paywall
    }

    fun epochDay(millis: Long): Long = millis / DAY_MS
}
