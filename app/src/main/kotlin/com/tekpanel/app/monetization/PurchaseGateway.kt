package com.tekpanel.app.monetization

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow

/** One support plan as shown to the user; [priceText] is already localized by Google Play. */
data class SupportPlan(val id: String, val months: Int, val priceText: String, val savingsPercent: Int?)

data class SupportState(
    val available: Boolean = false,
    val supporter: Boolean = false,
    val plans: List<SupportPlan> = emptyList(),
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
