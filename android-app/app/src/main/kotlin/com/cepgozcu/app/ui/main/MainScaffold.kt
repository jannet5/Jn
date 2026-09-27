package com.cepgozcu.app.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cepgozcu.app.R
import com.cepgozcu.app.connection.ConnectionSource
import com.cepgozcu.app.net.ConnectionState
import com.cepgozcu.app.ui.alerts.AlertsScreen
import com.cepgozcu.app.ui.dashboard.DashboardScreen
import com.cepgozcu.app.ui.disk.DiskScreen
import com.cepgozcu.app.ui.processes.ProcessesScreen
import com.cepgozcu.app.ui.settings.SettingsScreen
import com.cepgozcu.app.ui.theme.SeverityCritical

private enum class MainDestination(val route: String, val icon: ImageVector, val labelRes: Int) {
    Dashboard("dashboard", Icons.Filled.Dashboard, R.string.nav_dashboard),
    Processes("processes", Icons.Filled.ListAlt, R.string.nav_processes),
    Disk("disk", Icons.Filled.Storage, R.string.nav_disk),
    Alerts("alerts", Icons.Filled.Notifications, R.string.nav_alerts),
    Settings("settings", Icons.Filled.Settings, R.string.nav_settings),
}

/**
 * The paired-in-app shell: bottom nav across the five main destinations, plus a persistent
 * non-dismissable banner whenever the connection turns Unauthorized (the paired session was
 * revoked by the PC) — the user must re-pair, there is no "dismiss and ignore" option.
 */
@Composable
fun MainScaffold(
    connectionSource: ConnectionSource,
    onOpenApps: () -> Unit,
    onOpenAudit: () -> Unit,
    onReauthorize: () -> Unit,
    onUnpairedNavigateToOnboarding: () -> Unit,
) {
    val innerNav = rememberNavController()
    val connState by connectionSource.connectionState.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by innerNav.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                MainDestination.entries.forEach { dest ->
                    NavigationBarItem(
                        selected = currentRoute == dest.route,
                        onClick = {
                            innerNav.navigate(dest.route) {
                                popUpTo(innerNav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(dest.icon, contentDescription = null) },
                        label = { Text(stringResource(dest.labelRes)) },
                    )
                }
            }
        },
    ) { padding ->
        Box(Modifier.padding(padding)) {
            Column {
                if (connState is ConnectionState.Unauthorized) {
                    UnauthorizedBanner(onReauthorize)
                }
                NavHost(innerNav, startDestination = MainDestination.Dashboard.route, modifier = Modifier.weight(1f)) {
                    composable(MainDestination.Dashboard.route) {
                        DashboardScreen(connectionSource, onOpenApps = onOpenApps, onReauthorize = onReauthorize)
                    }
                    composable(MainDestination.Processes.route) {
                        ProcessesScreen(connectionSource, onReauthorize = onReauthorize)
                    }
                    composable(MainDestination.Disk.route) {
                        DiskScreen(connectionSource, onReauthorize = onReauthorize)
                    }
                    composable(MainDestination.Alerts.route) {
                        AlertsScreen(connectionSource, onReauthorize = onReauthorize)
                    }
                    composable(MainDestination.Settings.route) {
                        SettingsScreen(connectionSource, onUnpaired = onUnpairedNavigateToOnboarding, onOpenAudit = onOpenAudit)
                    }
                }
            }
        }
    }
}

@Composable
private fun UnauthorizedBanner(onReauthorize: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SeverityCritical.copy(alpha = 0.15f))
            .padding(16.dp),
    ) {
        Text(stringResource(R.string.unauthorized_banner_title), style = MaterialTheme.typography.titleSmall, color = SeverityCritical)
        Text(stringResource(R.string.unauthorized_banner_message), style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onReauthorize) { Text(stringResource(R.string.unauthorized_banner_action)) }
    }
}
