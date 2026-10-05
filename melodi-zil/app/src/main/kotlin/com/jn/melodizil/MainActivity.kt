package com.jn.melodizil // Uygulama kökü

import android.content.Intent // Intent
import android.os.Bundle // Durum
import android.view.WindowManager // Ekran açık tutma
import androidx.activity.ComponentActivity // Activity
import androidx.activity.compose.setContent // Compose
import androidx.activity.enableEdgeToEdge // Kenardan kenara
import androidx.activity.viewModels // VM
import androidx.compose.runtime.LaunchedEffect // Etki
import androidx.compose.runtime.getValue // Delegasyon
import androidx.compose.runtime.mutableStateOf // Durum
import androidx.compose.runtime.setValue // Delegasyon
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // Splash
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Akış
import com.jn.melodizil.core.YouTubeUrl // Bağlantı
import com.jn.melodizil.ui.AppViewModel // VM
import com.jn.melodizil.ui.ProcessState // Durum
import com.jn.melodizil.ui.nav.AppNav // Gezinme
import com.jn.melodizil.ui.theme.MelodiZilTheme // Tema

/** Tek Activity. YouTube uygulamasından "Paylaş" ile gelen bağlantıları da alır. */
class MainActivity : ComponentActivity() {
    private val vm: AppViewModel by viewModels() // ViewModel
    private var sharedLink by mutableStateOf<String?>(null) // Paylaşımla gelen bağlantı

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // Açılış ekranı
        super.onCreate(savedInstanceState) // Üst sınıf
        enableEdgeToEdge() // Kenardan kenara
        sharedLink = extractLink(intent) // İlk intent
        val version = runCatching { packageManager.getPackageInfo(packageName, 0).versionName }.getOrNull() ?: "1.0" // Sürüm
        setContent { // Compose kökü
            val theme by vm.themeMode.collectAsStateWithLifecycle() // Tema
            val process by vm.process.collectAsStateWithLifecycle() // İşlem
            LaunchedEffect(process) { // İşlem sürerken ekran kapanmasın (süreç ölmesin)
                if (process is ProcessState.Running) window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) else window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // Bayrak
            }
            MelodiZilTheme(theme) { AppNav(vm, sharedLink, version) { sharedLink = null } } // Uygulama
        }
    }

    override fun onNewIntent(intent: Intent) { super.onNewIntent(intent); extractLink(intent)?.let { sharedLink = it } } // Açıkken paylaşım

    override fun onResume() { super.onResume(); vm.applyPendingDefault() } // İzin ekranından dönüş: bekleyen varsayılanı uygula

    /** ACTION_SEND (metin) ve ACTION_VIEW (youtube bağlantısı) intent'lerinden bağlantı çıkarır. */
    private fun extractLink(intent: Intent?): String? {
        val text = when (intent?.action) { Intent.ACTION_SEND -> intent.getStringExtra(Intent.EXTRA_TEXT); Intent.ACTION_VIEW -> intent.dataString; else -> null } ?: return null // Metin
        return YouTubeUrl.extractVideoId(text)?.let { YouTubeUrl.canonical(it) } // Geçerliyse standart bağlantı
    }
}
