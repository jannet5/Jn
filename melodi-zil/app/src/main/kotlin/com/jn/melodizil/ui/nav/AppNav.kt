package com.jn.melodizil.ui.nav // Gezinme

import android.content.Intent // Intent
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.layout.size // Boyut
import androidx.compose.material.icons.Icons // İkonlar
import androidx.compose.material.icons.rounded.Home // Ana sayfa
import androidx.compose.material.icons.rounded.LibraryMusic // Zillerim
import androidx.compose.material.icons.rounded.Settings // Ayarlar
import androidx.compose.material3.Icon // İkon
import androidx.compose.material3.NavigationBar // Alt sekme
import androidx.compose.material3.NavigationBarItem // Sekme
import androidx.compose.material3.NavigationBarItemDefaults // Sekme renkleri
import androidx.compose.material3.Scaffold // İskelet
import androidx.compose.material3.SnackbarHost // Snackbar
import androidx.compose.material3.SnackbarHostState // Snackbar durumu
import androidx.compose.material3.Text // Metin
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.runtime.Composable // Composable
import androidx.compose.runtime.LaunchedEffect // Etki
import androidx.compose.runtime.getValue // Delegasyon
import androidx.compose.runtime.remember // Hatırla
import androidx.compose.runtime.rememberCoroutineScope // Kapsam
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.platform.LocalContext // Bağlam
import androidx.compose.ui.res.stringResource // Metin
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Akış→durum
import androidx.navigation.NavDestination.Companion.hierarchy // Hiyerarşi
import androidx.navigation.compose.NavHost // Host
import androidx.navigation.compose.composable // Rota
import androidx.navigation.compose.currentBackStackEntryAsState // Mevcut rota
import androidx.navigation.compose.rememberNavController // Controller
import com.jn.melodizil.R // Kaynaklar
import com.jn.melodizil.ui.AppViewModel // VM
import com.jn.melodizil.ui.ProcessState // Durum
import com.jn.melodizil.ui.home.HomeScreen // Ana
import com.jn.melodizil.ui.library.LibraryScreen // Kütüphane
import com.jn.melodizil.ui.onboarding.OnboardingScreen // Tanıtım
import com.jn.melodizil.ui.process.ProcessingScreen // İşlem
import com.jn.melodizil.ui.result.ResultScreen // Sonuç
import com.jn.melodizil.ui.settings.SettingsScreen // Ayarlar
import com.jn.melodizil.ui.theme.Size // Boyut
import kotlinx.coroutines.launch // Başlat

/** Rotalar. */
object Routes { const val ONBOARDING = "onboarding"; const val HOME = "home"; const val PROCESS = "process"; const val RESULT = "result"; const val LIBRARY = "library"; const val SETTINGS = "settings" }

/** Uygulamanın tüm ekranlarını bağlayan kök. Alt sekme yalnızca ana/kütüphane/ayarlar'da görünür. */
@Composable
fun AppNav(vm: AppViewModel, sharedLink: String?, versionName: String, onConsumeSharedLink: () -> Unit) {
    val nav = rememberNavController() // Controller
    val context = LocalContext.current // Bağlam
    val scope = rememberCoroutineScope() // Kapsam
    val snackbar = remember { SnackbarHostState() } // Snackbar
    val onboarded by vm.onboarded.collectAsStateWithLifecycle() // Tanıtım
    val process by vm.process.collectAsStateWithLifecycle() // İşlem
    val result by vm.result.collectAsStateWithLifecycle() // Sonuç
    val library by vm.library.collectAsStateWithLifecycle() // Kütüphane
    val theme by vm.themeMode.collectAsStateWithLifecycle() // Tema
    val isPlaying by vm.player.isPlaying.collectAsStateWithLifecycle() // Oynatma
    val position by vm.player.positionSec.collectAsStateWithLifecycle() // Konum
    val backStack by nav.currentBackStackEntryAsState() // Mevcut rota
    val route = backStack?.destination?.route // Rota adı

    LaunchedEffect(process) { // İşlem bitince sonuç ekranına geç
        if (process is ProcessState.Done && route == Routes.PROCESS) nav.navigate(Routes.RESULT) { popUpTo(Routes.HOME) } // Sonuca git, işlem ekranını yığından çıkar
    }
    LaunchedEffect(result.message) { result.message?.let { snackbar.showSnackbar(it); vm.consumeMessage() } } // Mesajları göster
    LaunchedEffect(sharedLink, onboarded) { // Paylaşımla gelen bağlantı: doğrudan işleme başla
        if (!sharedLink.isNullOrBlank() && onboarded == true) { vm.startFromLink(sharedLink); nav.navigate(Routes.PROCESS); onConsumeSharedLink() } // Başlat
    }
    if (onboarded == null) return // Tercihler yüklenmeden ekran çizilmez (titreme olmasın)

    val showBar = route == Routes.HOME || route == Routes.LIBRARY || route == Routes.SETTINGS // Alt sekme görünür mü
    Scaffold( // İskelet
        containerColor = MaterialTheme.colorScheme.background, // Arka plan
        snackbarHost = { SnackbarHost(snackbar) }, // Snackbar
        bottomBar = { if (showBar) BottomBar(route) { target -> nav.navigate(target) { popUpTo(Routes.HOME) { saveState = true }; launchSingleTop = true; restoreState = true } } }, // Sekmeler
    ) { padding -> // İçerik
        NavHost(nav, startDestination = if (onboarded == true) Routes.HOME else Routes.ONBOARDING, modifier = Modifier.padding(bottom = if (showBar) padding.calculateBottomPadding() else androidx.compose.ui.unit.Dp(0f))) { // Host (üst padding Screen'in kendi insets'iyle çözülür)
            composable(Routes.ONBOARDING) { OnboardingScreen { vm.finishOnboarding(); nav.navigate(Routes.HOME) { popUpTo(Routes.ONBOARDING) { inclusive = true } } } } // Tanıtım
            composable(Routes.HOME) { // Ana
                HomeScreen(initialLink = null, recent = library, // Parametreler
                    onConvert = { vm.startFromLink(it); nav.navigate(Routes.PROCESS) }, // Bağlantı
                    onPickFile = { vm.startFromFile(it); nav.navigate(Routes.PROCESS) }, // Dosya
                    onOpenLibrary = { nav.navigate(Routes.LIBRARY) }, onOpenSaved = { nav.navigate(Routes.LIBRARY) }) // Kütüphane
            }
            composable(Routes.PROCESS) { ProcessingScreen(process, onCancel = { vm.cancel(); nav.popBackStack() }, onRetry = { vm.retry() }, onBack = { vm.resetProcess(); nav.popBackStack() }) } // İşlem
            composable(Routes.RESULT) { // Sonuç
                ResultScreen(result, isPlaying, position, onBack = { vm.player.stop(); nav.popBackStack() }, // Geri
                    onInstrument = vm::selectInstrument, onLength = vm::setLength, onStartChange = vm::setStart, onStartCommit = vm::commitStart, onAuto = vm::autoStart, onTogglePlay = vm::togglePlay, onSave = vm::save, // Eylemler
                    onShare = { scope.launch { vm.shareIntent()?.let { context.startActivity(Intent.createChooser(it, context.getString(R.string.share))) } } }, // Paylaş
                    onOpenWriteSettings = { context.startActivity(vm.writeSettingsIntent()) }, onDismissPermission = vm::dismissPermission) // İzin
            }
            composable(Routes.LIBRARY) { // Kütüphane
                LibraryScreen(library, onGoHome = { nav.navigate(Routes.HOME) { popUpTo(Routes.HOME) } }, // Boş durumda ana sayfa
                    onSetDefault = { item, kind -> if (!vm.setSavedAsDefault(item, kind)) context.startActivity(vm.writeSettingsIntent()) }, // Varsayılan (izin yoksa ayarlar)
                    onShare = { context.startActivity(Intent.createChooser(vm.shareSaved(it), context.getString(R.string.share))) }, onDelete = vm::deleteSaved) // Paylaş / sil
            }
            composable(Routes.SETTINGS) { SettingsScreen(theme, versionName, vm::setTheme) } // Ayarlar
        }
    }
}

@Composable
private fun BottomBar(route: String?, onNavigate: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) { // Alt sekme (kart yüzeyi)
        listOf(Triple(Routes.HOME, Icons.Rounded.Home, R.string.tab_home), Triple(Routes.LIBRARY, Icons.Rounded.LibraryMusic, R.string.tab_library), Triple(Routes.SETTINGS, Icons.Rounded.Settings, R.string.tab_settings)).forEach { (r, icon, label) -> // Üç sekme
            NavigationBarItem(selected = route == r, onClick = { onNavigate(r) }, icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(Size.iconMd)) }, label = { Text(stringResource(label), style = MaterialTheme.typography.labelSmall) }, // Sekme
                colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.secondaryContainer, selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer, selectedTextColor = MaterialTheme.colorScheme.onSurface, unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant, unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant)) // Renkler
        }
    }
}
