package com.cepgozcu.app.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cepgozcu.app.pairing.PairingViewModel
import com.cepgozcu.app.ui.alerts.AlertsScreen
import com.cepgozcu.app.ui.apps.AppsScreen
import com.cepgozcu.app.ui.audit.AuditScreen
import com.cepgozcu.app.ui.common.localApp
import com.cepgozcu.app.ui.main.MainScaffold
import com.cepgozcu.app.ui.onboarding.OnboardingScreen
import com.cepgozcu.app.ui.pairing.PairingHostScreen
import kotlinx.coroutines.launch

private const val ROUTE_ONBOARDING = "onboarding"
private const val ROUTE_PAIRING = "pairing"
private const val ROUTE_MAIN = "main"
private const val ROUTE_APPS = "apps"
private const val ROUTE_AUDIT = "audit"

/**
 * The whole app's navigation graph. Root start destination depends on whether a session is
 * already stored: present -> straight into the main shell (which renders its own
 * Connecting/Offline states — we never block navigation on the socket actually being open);
 * absent -> onboarding.
 */
@Composable
fun CepGozcuNavHost(onPairingApproved: () -> Unit) {
    val app = localApp()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val hasSession = remember { app.credentialStore.loadSession() != null }
    val startDestination = if (hasSession) ROUTE_MAIN else ROUTE_ONBOARDING

    fun goToOnboardingRoot() {
        navController.navigate(ROUTE_ONBOARDING) {
            popUpTo(0) { inclusive = true }
        }
    }

    /** Used when the connection itself reports Unauthorized: clears the (now-invalid) session, then returns to onboarding. */
    fun reauthorize() {
        scope.launch { app.connectionRepository.unpairAndClear() }
        goToOnboardingRoot()
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(ROUTE_ONBOARDING) {
            OnboardingScreen(onAddComputer = { navController.navigate(ROUTE_PAIRING) })
        }
        composable(ROUTE_PAIRING) {
            val pairingViewModel: PairingViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { PairingViewModel(app.pairingRepository, app.credentialStore, app.connectionRepository) }
                },
            )
            PairingHostScreen(
                viewModel = pairingViewModel,
                onApproved = {
                    onPairingApproved()
                    navController.navigate(ROUTE_MAIN) { popUpTo(0) { inclusive = true } }
                },
            )
        }
        composable(ROUTE_MAIN) {
            MainScaffold(
                connectionSource = app.connectionRepository,
                onOpenApps = { navController.navigate(ROUTE_APPS) },
                onOpenAudit = { navController.navigate(ROUTE_AUDIT) },
                onReauthorize = ::reauthorize,
                onUnpairedNavigateToOnboarding = ::goToOnboardingRoot,
            )
        }
        composable(ROUTE_APPS) {
            AppsScreen(
                connectionSource = app.connectionRepository,
                onBack = { navController.popBackStack() },
                onReauthorize = ::reauthorize,
            )
        }
        composable(ROUTE_AUDIT) {
            AuditScreen(
                connectionSource = app.connectionRepository,
                onBack = { navController.popBackStack() },
                onReauthorize = ::reauthorize,
            )
        }
    }
}
