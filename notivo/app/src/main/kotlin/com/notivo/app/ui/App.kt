package com.notivo.app.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel

fun listenerEnabled(c: android.content.Context) =
    NotificationManagerCompat.getEnabledListenerPackages(c).contains(c.packageName)

@Composable
fun LockScreen(onUnlock: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(32.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Icon(Icons.Default.Lock, null, Modifier.size(64.dp))
        Spacer(Modifier.height(16.dp))
        Text("Notivo kilitli", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onUnlock) { Text("Kilidi aç") }
    }
}

@Composable
fun App(vm: Vm = viewModel()) {
    val ctx = LocalContext.current
    var enabled by remember { mutableStateOf(listenerEnabled(ctx)) }
    val owner = LocalLifecycleOwner.current
    DisposableEffect(owner) {
        val o = LifecycleEventObserver { _, e -> if (e == Lifecycle.Event.ON_RESUME) enabled = listenerEnabled(ctx) }
        owner.lifecycle.addObserver(o); onDispose { owner.lifecycle.removeObserver(o) }
    }
    if (!enabled) { Onboarding(); return }

    val postPerm = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    LaunchedEffect(Unit) { if (Build.VERSION.SDK_INT >= 33) postPerm.launch(Manifest.permission.POST_NOTIFICATIONS) }

    var tab by remember { mutableIntStateOf(0) }
    var filterPkg by remember { mutableStateOf<String?>(null) }
    val items = listOf("Gelen" to Icons.Default.Inbox, "Uygulamalar" to Icons.Default.Apps, "Kurallar" to Icons.Default.FilterAlt,
        "İstatistik" to Icons.Default.BarChart, "Ayarlar" to Icons.Default.Settings)
    Scaffold(bottomBar = {
        NavigationBar {
            items.forEachIndexed { i, (l, ic) ->
                NavigationBarItem(selected = tab == i, onClick = { tab = i; if (i != 0) filterPkg = null },
                    icon = { Icon(ic, l) }, label = { Text(l, maxLines = 1) })
            }
        }
    }) { pad ->
        Box(Modifier.padding(pad).fillMaxSize()) {
            when (tab) {
                0 -> InboxScreen(vm, filterPkg) { filterPkg = null }
                1 -> AppsScreen(vm) { filterPkg = it; tab = 0 }
                2 -> RulesScreen(vm)
                3 -> StatsScreen(vm)
                else -> SettingsScreen(vm)
            }
        }
    }
}

@Composable
fun Onboarding() {
    val ctx = LocalContext.current
    Column(Modifier.fillMaxSize().padding(28.dp), Arrangement.Center) {
        Icon(Icons.Default.NotificationsActive, null, Modifier.size(72.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text("Notivo'a hoş geldin", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        Text("Tüm uygulamalarının bildirimlerini tek bir kutuda topla, ara, filtrele, ertele. " +
            "Bunun için \"Bildirim erişimi\" iznini vermen gerekir.")
        Spacer(Modifier.height(12.dp))
        Text("Gizlilik: Bildirimler yalnızca bu telefonda saklanır. İnternete hiçbir veri gönderilmez; uygulamanın internet izni bile yoktur.",
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        Button(onClick = { ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }, Modifier.fillMaxWidth()) {
            Text("Bildirim erişimini aç")
        }
        Spacer(Modifier.height(8.dp))
        Text("Açılan listede Notivo'ı etkinleştir. Android 13+ cihazlarda \"kısıtlı ayar\" uyarısı çıkarsa: Ayarlar → Uygulamalar → Notivo → ⋮ → Kısıtlı ayarlara izin ver.",
            style = MaterialTheme.typography.bodySmall)
    }
}
