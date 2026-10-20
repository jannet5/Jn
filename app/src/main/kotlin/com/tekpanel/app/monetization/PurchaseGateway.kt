package com.tekpanel.app.monetization

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow

/**
 * One support option as shown to the user; [priceText] is already localized by Google Play.
 * [months] is the renewal period for a subscription plan and 0 for a one-time tip (a tip has no
 * period and can be given again and again).
 */
data class SupportPlan(val id: String, val months: Int, val priceText: String, val savingsPercent: Int?) {
    val isOneTime: Boolean get() = months == 0
}

data class SupportState(
    val available: Boolean = false,
    val supporter: Boolean = false,
    val plans: List<SupportPlan> = emptyList(),
    /** A one-time tip just went through: show a thank-you even though nothing stays "subscribed". */
    val justThanked: Boolean = false,
)

/**
 * The seam between the app and Google Play Billing. The sideload build binds a no-op, the Play
 * build binds the real thing; nothing in `main` knows about Billing classes.
 */
interface PurchaseGateway {
    val state: StateFlow<SupportState>
    suspend fun refresh()
    fun buy(activity: Activity, planId: String)
}
