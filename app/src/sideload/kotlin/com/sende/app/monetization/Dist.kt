package com.sende.app.monetization

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Sideload build: no billing and no update library are even compiled in. */
object Dist {
    const val NAME = "sideload"

    fun purchaseGateway(@Suppress("UNUSED_PARAMETER") context: Context): PurchaseGateway = object : PurchaseGateway {
        override val state: StateFlow<SupportState> = MutableStateFlow(SupportState(available = false))
        override suspend fun refresh() = Unit
        override fun buy(activity: Activity, planId: String) = Unit
    }
}
