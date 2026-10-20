package com.sende.app.monetization

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sende.app.BuildConfig
import com.sende.app.data.prefs.AppPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SupportUi(
    val support: SupportState = SupportState(),
    val gate: Gate = Gate.Open,
)

/**
 * Joins what Google Play says (plans, supporter or not) with the trial rules. In the default
 * DONATE mode the gate is always open: the sheet only appears when the user opens it from the menu.
 */
class SupportViewModel(
    private val gateway: PurchaseGateway,
    private val preferences: AppPreferences,
    context: Context,
    private val mode: MonetizationMode = MonetizationMode.parse(BuildConfig.MONETIZATION),
    private val clock: () -> Long = System::currentTimeMillis,
) : ViewModel() {

    private val firstInstall: Long = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
    }.getOrDefault(clock())

    val state: StateFlow<SupportUi> = combine(gateway.state, preferences.paywallDismissedDay) { support, dismissedDay ->
        SupportUi(
            support = support,
            gate = if (support.available) {
                TrialPolicy.gate(mode, firstInstall, clock(), support.supporter, dismissedDay)
            } else {
                Gate.Open
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SupportUi())

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch { gateway.refresh() }
    }

    fun buy(activity: android.app.Activity, planId: String) = gateway.buy(activity, planId)

    /** "Not now": quiet for the rest of the day. */
    fun dismissPaywall() {
        viewModelScope.launch { preferences.setPaywallDismissedDay(TrialPolicy.epochDay(clock())) }
    }
}
