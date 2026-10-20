package com.sende.app.monetization

import android.content.Context

/** Google Play build: real Play Billing. */
object Dist {
    const val NAME = "play"

    fun purchaseGateway(context: Context): PurchaseGateway = PlayPurchaseGateway(context.applicationContext)
}
