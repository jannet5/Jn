package com.jn.winremote.ui.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jn.winremote.ui.alerts.AlertsScreen
import com.jn.winremote.ui.apps.AppsScreen
import com.jn.winremote.ui.dashboard.DashboardScreen
import com.jn.winremote.ui.fileactivity.FileActivityScreen
import com.jn.winremote.ui.history.HistoryScreen
import com.jn.winremote.ui.pairing.PairingScreen
import com.jn.winremote.ui.processes.ProcessesScreen
import com.jn.winremote.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

object Routes {
    const val PAIRING = "pairing"
    const val DASHBOARD = "dashboard"
    const val PROCESSES = "processes"
    const val APPS = "apps"
    const val FILE_ACTIVITY = "file_activity"
    const val ALERTS = "alerts"
    const val HISTORY = "history"
    const val SETTINGS = "settings"
}

private data class DrawerDestination(val route: String, val label: String, val icon: ImageVector)

private val drawerDestinations = listOf(
    DrawerDestination(Routes.DASHBOARD, "Panel", Icons.Filled.Dashboard),
    DrawerDestination(Routes.PROCESSES, "Süreçler", Icons.Filled.Terminal),
    DrawerDestination(Routes.APPS, "Uygulamalar", Icons.Filled.Apps),
    DrawerDestination(Routes.FILE_ACTIVITY, "Dosya Etkinliği", Icons.Filled.Folder),
    DrawerDestination(Routes.ALERTS, "Uyarılar", Icons.Filled.NotificationsActive),
    DrawerDestination(Routes.HISTORY, "Geçmiş", Icons.Filled.History),
    DrawerDestination(Routes.PAIRING, "Eşleştirme", Icons.Filled.QrCodeScanner),
    DrawerDestination(Routes.SETTINGS, "Ayarlar", Icons.Filled.Settings),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinRemoteApp(hasPairedDevice: Boolean) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.DASHBOARD

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "WinRemote Monitor",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(24.dp),
                )
                drawerDestinations.forEach { dest ->
                    NavigationDrawerItem(
                        icon = { Icon(dest.icon, contentDescription = null) },
                        label = { Text(dest.label) },
                        selected = currentRoute == dest.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (currentRoute != dest.route) {
                                navController.navigate(dest.route) {
                                    launchSingleTop = true
                                }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(titleFor(currentRoute)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menü")
                        }
                    },
                )
            },
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = if (hasPairedDevice) Routes.DASHBOARD else Routes.PAIRING,
                modifier = Modifier.padding(padding).fillMaxSize(),
            ) {
                composable(Routes.PAIRING) {
                    PairingScreen(onPaired = { navController.navigate(Routes.DASHBOARD) { launchSingleTop = true } })
                }
                composable(Routes.DASHBOARD) {
                    DashboardScreen(onRePair = { navController.navigateToPairing() })
                }
                composable(Routes.PROCESSES) {
                    ProcessesScreen(onRePair = { navController.navigateToPairing() })
                }
                composable(Routes.APPS) {
                    AppsScreen(onRePair = { navController.navigateToPairing() })
                }
                composable(Routes.FILE_ACTIVITY) {
                    FileActivityScreen(onRePair = { navController.navigateToPairing() })
                }
                composable(Routes.ALERTS) {
                    AlertsScreen(onRePair = { navController.navigateToPairing() })
                }
                composable(Routes.HISTORY) {
                    HistoryScreen(onRePair = { navController.navigateToPairing() })
                }
                composable(Routes.SETTINGS) {
                    SettingsScreen(onAddDevice = { navController.navigateToPairing() })
                }
            }
        }
    }
}

private fun NavHostController.navigateToPairing() {
    navigate(Routes.PAIRING) { launchSingleTop = true }
}

private fun titleFor(route: String): String = when (route) {
    Routes.PAIRING -> "Eşleştirme"
    Routes.DASHBOARD -> "Panel"
    Routes.PROCESSES -> "Süreçler"
    Routes.APPS -> "Uygulamalar"
    Routes.FILE_ACTIVITY -> "Dosya Etkinliği"
    Routes.ALERTS -> "Uyarılar"
    Routes.HISTORY -> "Geçmiş"
    Routes.SETTINGS -> "Ayarlar"
    else -> "WinRemote Monitor"
}
