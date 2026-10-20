package com.sende.app.monetization

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.roundToInt

/**
 * Support plans through Google Play Billing. One subscription product with four base plans
 * (monthly, quarterly, semiannual, yearly) is configured in Play Console; prices, currency and
 * the Turkey price list live there, the app only displays what Play returns.
 *
 * Entitlement is whatever Play says right now (`queryPurchasesAsync` reads the Play Store app's
 * own cache), so this build needs no INTERNET permission of its own.
 */
class PlayPurchaseGateway(private val context: Context) : PurchaseGateway, PurchasesUpdatedListener {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _state = MutableStateFlow(SupportState(available = true))
    override val state: StateFlow<SupportState> = _state.asStateFlow()

    private var details: ProductDetails? = null
    private val offerTokens = HashMap<String, String>()
    private val tipDetails = HashMap<String, ProductDetails>()

    private val client: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .enableAutoServiceReconnection()
        .build()

    override suspend fun refresh() {
        if (!connect()) return
        loadPlans()
        loadEntitlement()
    }

    private suspend fun connect(): Boolean {
        if (client.isReady) return true
        return suspendCancellableCoroutine { cont ->
            client.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(result: BillingResult) {
                    if (cont.isActive) cont.resume(result.responseCode == BillingClient.BillingResponseCode.OK)
                }

                override fun onBillingServiceDisconnected() = Unit
            })
        }
    }

    private suspend fun loadPlans() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                ),
            )
            .build()
        val product = client.queryProductDetails(params).productDetailsList?.firstOrNull() ?: return
        details = product

        val offers = product.subscriptionOfferDetails.orEmpty()
            .filter { it.offerId == null } // plain base plans, not promotional offers
            .mapNotNull { offer ->
                val months = MONTHS_BY_BASE_PLAN[offer.basePlanId] ?: return@mapNotNull null
                val phase = offer.pricingPhases.pricingPhaseList.lastOrNull() ?: return@mapNotNull null
                offerTokens[offer.basePlanId] = offer.offerToken
                Triple(offer.basePlanId, months, phase)
            }
            .sortedBy { it.second }

        val monthlyMicros = offers.firstOrNull { it.second == 1 }?.third?.priceAmountMicros
        val plans = offers.map { (id, months, phase) ->
            val savings = if (monthlyMicros != null && months > 1) {
                ((1.0 - (phase.priceAmountMicros.toDouble() / months) / monthlyMicros) * 100).roundToInt().takeIf { it > 0 }
            } else {
                null
            }
            SupportPlan(id = id, months = months, priceText = phase.formattedPrice, savingsPercent = savings)
        }
        _state.update { it.copy(plans = plans + loadTips()) }
    }

    /** One-time tips (consumable, can be given repeatedly): small / medium / large. */
    private suspend fun loadTips(): List<SupportPlan> {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                TIP_IDS.map {
                    QueryProductDetailsParams.Product.newBuilder().setProductId(it).setProductType(BillingClient.ProductType.INAPP).build()
                },
            )
            .build()
        val found = client.queryProductDetails(params).productDetailsList.orEmpty()
        found.forEach { tipDetails[it.productId] = it }
        return TIP_IDS.mapNotNull { id ->
            val price = tipDetails[id]?.oneTimePurchaseOfferDetails?.formattedPrice ?: return@mapNotNull null
            SupportPlan(id = id, months = 0, priceText = price, savingsPercent = null)
        }
    }

    private suspend fun loadEntitlement() {
        val result = client.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
        )
        if (result.billingResult.responseCode != BillingClient.BillingResponseCode.OK) return
        handle(result.purchasesList)
    }

    override fun buy(activity: Activity, planId: String) {
        val params = if (planId in TIP_IDS) {
            val tip = tipDetails[planId] ?: return
            BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(tip).build()
        } else {
            val product = details ?: return
            val token = offerTokens[planId] ?: return
            BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(product).setOfferToken(token).build()
        }
        client.launchBillingFlow(activity, BillingFlowParams.newBuilder().setProductDetailsParamsList(listOf(params)).build())
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            scope.launch { handle(purchases) }
        }
    }

    private suspend fun handle(purchases: List<Purchase>) {
        val owned = purchases.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
        val (tips, subs) = owned.partition { purchase -> purchase.products.any { it in TIP_IDS } }
        // Play refunds a subscription that is not acknowledged within 3 days.
        subs.filter { !it.isAcknowledged }.forEach { purchase ->
            client.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build())
        }
        // A tip is consumed right away so the same amount can be given again.
        tips.forEach { purchase ->
            client.consumePurchase(ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build())
        }
        _state.update { it.copy(supporter = subs.isNotEmpty(), justThanked = it.justThanked || tips.isNotEmpty()) }
    }

    companion object {
        const val PRODUCT_ID = "sende_support"
        val TIP_IDS = listOf("tip_small", "tip_medium", "tip_large")
        private val MONTHS_BY_BASE_PLAN = mapOf("monthly" to 1, "quarterly" to 3, "semiannual" to 6, "yearly" to 12)
    }
}
