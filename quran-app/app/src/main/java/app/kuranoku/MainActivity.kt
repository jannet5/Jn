package app.kuranoku // uygulama girişi

import android.os.Bundle // yaşam döngüsü verisi
import androidx.activity.ComponentActivity // Compose etkinliği
import androidx.activity.compose.BackHandler // geri tuşu
import androidx.activity.compose.setContent // içerik
import androidx.activity.enableEdgeToEdge // kenardan kenara çizim
import androidx.activity.viewModels // ViewModel
import androidx.compose.animation.AnimatedVisibility // görün/kaybol
import androidx.compose.animation.core.RepeatMode // tekrar modu
import androidx.compose.animation.core.animateFloat // durum animasyonu
import androidx.compose.animation.core.infiniteRepeatable // sonsuz tekrar
import androidx.compose.animation.core.rememberInfiniteTransition // sonsuz geçiş
import androidx.compose.animation.core.tween // süreli
import androidx.compose.animation.fadeIn // belirme
import androidx.compose.animation.fadeOut // sönme
import androidx.compose.animation.slideInHorizontally // yandan gelme
import androidx.compose.animation.slideOutHorizontally // yana gitme
import androidx.compose.foundation.background // zemin
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Box // kutu
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.fillMaxSize // tam boyut
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.layout.statusBarsPadding // durum çubuğu
import androidx.compose.foundation.shape.RoundedCornerShape // köşe
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.collectAsState // akıştan durum
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.saveable.rememberSaveable // korunan durum
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.draw.alpha // saydamlık
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.graphics.Color // renk
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // açılış ekranı
import app.kuranoku.data.ReaderSettings // ayarlar
import app.kuranoku.data.SuggestionKind // öneri türü
import app.kuranoku.ui.AboutScreen // hakkında
import app.kuranoku.ui.AppViewModel // ViewModel
import app.kuranoku.ui.LoadState // yükleme durumu
import app.kuranoku.ui.components.ErrorState // hata durumu
import app.kuranoku.ui.index.IndexScreen // içindekiler
import app.kuranoku.ui.reader.AyahRef // ayet referansı
import app.kuranoku.ui.reader.ReaderScreen // okuma
import app.kuranoku.ui.reader.paper // kağıt zemin
import app.kuranoku.ui.search.SearchScreen // arama
import app.kuranoku.ui.theme.KuranTheme // tema
import app.kuranoku.ui.theme.Radius // köşeler
import app.kuranoku.ui.theme.Space // boşluklar
import app.kuranoku.ui.theme.isDarkPage // koyu sayfa mı

/** Ekranlar: okuma her zaman altta durur, diğerleri üstüne açılır. */
private enum class Ekran { OKU, ARA, ICINDEKILER, HAKKINDA }

class MainActivity : ComponentActivity() {
    private val vm: AppViewModel by viewModels() // tek ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { vm.state.value is LoadState.Loading } // metin yüklenene kadar açılış ekranı (genelde < 0,3 sn)
        super.onCreate(savedInstanceState) // üst sınıf
        enableEdgeToEdge() // sistem çubuklarının altına çiz
        setContent { App(vm) } // arayüz
    }
}

@Composable
private fun App(vm: AppViewModel) {
    val state by vm.state.collectAsState() // yükleme durumu
    val settings by vm.settings.collectAsState() // görünüm
    val bookmarks by vm.bookmarks.collectAsState() // yer imleri
    val nav by vm.nav.collectAsState() // sayfa isteği
    val page by vm.currentPage.collectAsState() // okunan sayfa
    var ekran by rememberSaveable { mutableStateOf(Ekran.OKU) } // açık ekran
    var ipucu by remember { mutableStateOf(!vm.prefs.hintShown) } // ilk açılış ipucu

    KuranTheme(dark = settings.isDarkPage()) { // sayfa koyuysa arayüz de koyu
        when (val s = state) {
            LoadState.Loading -> LoadingPage(settings) // yükleniyor
            is LoadState.Error -> ErrorState(s.message) { vm.load() } // hata
            is LoadState.Ready -> Box(Modifier.fillMaxSize()) {
                ReaderScreen(
                    quran = s.quran, settings = settings, initialPage = remember { vm.prefs.lastPage }, // kaldığı sayfadan açılır
                    bookmarkedPages = remember(bookmarks) { bookmarks.map { it.page }.toSet() }, // yer imli sayfalar
                    active = ekran == Ekran.OKU, nav = nav, showHint = ipucu, // durum
                    onNavConsumed = vm::consumeNav, onPageChanged = vm::onPageChanged, onToggleBookmark = vm::toggleBookmark, // olaylar
                    onOpenSearch = { ekran = Ekran.ARA }, onOpenIndex = { ekran = Ekran.ICINDEKILER }, // ekranlar
                    onSettingsChange = vm::updateSettings, onHintShown = { ipucu = false; vm.prefs.hintShown = true }, // ayar ve ipucu
                )
                AnimatedVisibility(ekran == Ekran.ARA, enter = fadeIn() + slideInHorizontally { it / 6 }, exit = fadeOut() + slideOutHorizontally { it / 6 }) { // arama
                    SearchScreen(s.search, settings.font, page, onBack = { ekran = Ekran.OKU }) { o -> // öneri seçildi
                        vm.goTo(o.page, if (o.kind == SuggestionKind.AYAH) AyahRef(o.sura, o.ayah) else null) // sayfaya git (ayetse vurgula)
                        ekran = Ekran.OKU // okumaya dön
                    }
                }
                AnimatedVisibility(ekran == Ekran.ICINDEKILER || ekran == Ekran.HAKKINDA, enter = fadeIn() + slideInHorizontally { -it / 6 }, exit = fadeOut() + slideOutHorizontally { -it / 6 }) { // içindekiler
                    IndexScreen(
                        s.quran, settings.font, page, bookmarks, // veri
                        onBack = { ekran = Ekran.OKU }, onOpenPage = { vm.goTo(it); ekran = Ekran.OKU }, // gezinme
                        onRemoveBookmark = { vm.toggleBookmark(it.page) }, onRestoreBookmark = { vm.prefs.restoreBookmark(it) }, // yer imi
                        onAbout = { ekran = Ekran.HAKKINDA }, // hakkında
                    )
                }
                AnimatedVisibility(ekran == Ekran.HAKKINDA, enter = fadeIn(), exit = fadeOut()) { AboutScreen(BuildConfigVersion) { ekran = Ekran.ICINDEKILER } } // hakkında
                BackHandler(ekran != Ekran.OKU) { ekran = if (ekran == Ekran.HAKKINDA) Ekran.ICINDEKILER else Ekran.OKU } // geri tuşu
            }
        }
    }
}

/** Yüklenirken sayfa iskeleti (açılış ekranından sonra nadiren görünür). */
@Composable
private fun LoadingPage(s: ReaderSettings) {
    val t = rememberInfiniteTransition(label = "iskelet") // nabız
    val a by t.animateFloat(0.10f, 0.22f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "saydamlık") // saydamlık
    Column(Modifier.fillMaxSize().paper(s).statusBarsPadding().padding(Space.xl), verticalArrangement = Arrangement.spacedBy(Space.l)) { // kağıt
        repeat(12) { i -> // 12 satır
            Box(Modifier.fillMaxWidth(if (i % 4 == 3) 0.6f else 1f).height(Space.l).clip(RoundedCornerShape(Radius.sm)).alpha(a).background(Color(s.inkColor))) // satır iskeleti
        }
    }
}

private const val BuildConfigVersion = "1.0.0" // görünen sürüm
