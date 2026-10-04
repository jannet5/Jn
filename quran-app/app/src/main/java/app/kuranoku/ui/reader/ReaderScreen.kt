package app.kuranoku.ui.reader // okuma ekranı

import android.app.Activity // pencereye erişim
import androidx.compose.animation.AnimatedVisibility // görün/kaybol animasyonu
import androidx.compose.animation.core.Animatable // elle animasyon
import androidx.compose.animation.core.tween // süreli animasyon
import androidx.compose.animation.fadeIn // belirme
import androidx.compose.animation.fadeOut // sönme
import androidx.compose.animation.slideInVertically // aşağıdan/yukarıdan gelme
import androidx.compose.animation.slideOutVertically // aşağı/yukarı gitme
import androidx.compose.foundation.ExperimentalFoundationApi // deneysel temel API
import androidx.compose.foundation.background // zemin
import androidx.compose.foundation.gestures.awaitEachGesture // jest döngüsü
import androidx.compose.foundation.gestures.awaitFirstDown // ilk dokunuş
import androidx.compose.foundation.gestures.calculateZoom // iki parmak yakınlaşma oranı
import androidx.compose.foundation.gestures.detectTapGestures // dokunma algılama
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Box // kutu
import androidx.compose.foundation.layout.BoxWithConstraints // ölçülü kutu
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.ExperimentalLayoutApi // deneysel düzen API
import androidx.compose.foundation.layout.Row // yatay dizi
import androidx.compose.foundation.layout.WindowInsets // sistem çubuğu boşlukları
import androidx.compose.foundation.layout.displayCutout // çentik
import androidx.compose.foundation.layout.fillMaxSize // tam boyut
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.heightIn // en az yükseklik
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility // gezinme çubuğu (gizliyken de)
import androidx.compose.foundation.layout.navigationBarsPadding // gezinme çubuğu boşluğu
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility // durum çubuğu (gizliyken de)
import androidx.compose.foundation.layout.statusBarsPadding // durum çubuğu boşluğu
import androidx.compose.foundation.layout.union // birleştirme
import androidx.compose.foundation.layout.windowInsetsPadding // boşluk uygulama
import androidx.compose.foundation.pager.HorizontalPager // yatay sayfa çevirici
import androidx.compose.foundation.pager.rememberPagerState // çevirici durumu
import androidx.compose.foundation.rememberScrollState // kaydırma durumu
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.verticalScroll // dikey kaydırma
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.Slider // kaydırıcı
import androidx.compose.material3.Text // metin
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.LaunchedEffect // yan etki
import androidx.compose.runtime.SideEffect // her çizimde yan etki
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableFloatStateOf // ondalık durum
import androidx.compose.runtime.mutableLongStateOf // uzun tamsayı durum
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.rememberCoroutineScope // eşzamanlılık alanı
import androidx.compose.runtime.rememberUpdatedState // güncel değer
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.runtime.snapshotFlow // durumdan akış
import androidx.compose.runtime.CompositionLocalProvider // değer sağlayıcı
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.input.pointer.PointerEventPass // olay geçişi
import androidx.compose.ui.input.pointer.pointerInput // dokunma girişi
import androidx.compose.ui.platform.LocalLayoutDirection // yön
import androidx.compose.ui.platform.LocalView // görünüm
import androidx.compose.ui.platform.LocalDensity // yoğunluk
import androidx.compose.ui.layout.onGloballyPositioned // ekrandaki konum
import androidx.compose.ui.layout.positionInRoot // köke göre konum
import androidx.compose.ui.text.style.TextOverflow // taşma
import androidx.compose.ui.unit.LayoutDirection // yön tipi
import androidx.compose.ui.unit.dp // dp
import androidx.core.view.WindowCompat // pencere yardımcıları
import androidx.core.view.WindowInsetsCompat // sistem çubukları
import androidx.core.view.WindowInsetsControllerCompat // sistem çubuğu denetimi
import app.kuranoku.data.Quran // Kur'an
import app.kuranoku.data.ReaderSettings // ayarlar
import app.kuranoku.ui.NavRequest // sayfa isteği
import app.kuranoku.ui.components.IconBtn // ikon buton
import app.kuranoku.ui.components.Lucide // ikonlar
import app.kuranoku.ui.theme.Radius // köşeler
import app.kuranoku.ui.theme.Space // boşluklar
import app.kuranoku.ui.theme.appColors // ek renkler
import app.kuranoku.ui.theme.isDarkPage // koyu sayfa mı
import kotlinx.coroutines.delay // bekleme
import kotlinx.coroutines.launch // başlatma

/** Okuma ekranı: sağdan sola sayfa çevirme, dokununca çubuklar, iki parmakla yumuşak büyütme. */
@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun ReaderScreen(
    quran: Quran, // Kur'an
    settings: ReaderSettings, // görünüm
    initialPage: Int, // açılış sayfası
    bookmarkedPages: Set<Int>, // yer imli sayfalar
    active: Boolean, // üstte başka ekran yokken true
    nav: NavRequest?, // bekleyen sayfa isteği
    showHint: Boolean, // ilk açılış ipucu
    onNavConsumed: () -> Unit, // istek işlendi
    onPageChanged: (Int) -> Unit, // sayfa değişti
    onToggleBookmark: (Int) -> Unit, // yer imi
    onOpenSearch: () -> Unit, // arama
    onOpenIndex: () -> Unit, // içindekiler
    onSettingsChange: (ReaderSettings) -> Unit, // ayar değişti
    onHintShown: () -> Unit, // ipucu gösterildi
) {
    val pager = rememberPagerState(initialPage = (initialPage - 1).coerceIn(0, quran.pageCount - 1)) { quran.pageCount } // 604 sayfa
    val scope = rememberCoroutineScope() // eşzamanlılık alanı
    var chrome by remember { mutableStateOf(false) } // çubuklar görünüyor mu
    var sheet by remember { mutableStateOf(false) } // görünüm paneli açık mı
    var highlight by remember { mutableStateOf<AyahRef?>(null) } // vurgulu ayet
    var highlightKey by remember { mutableLongStateOf(0L) } // vurgu isteği kimliği
    val highlightAlpha = remember { Animatable(0f) } // vurgu görünürlüğü
    var hint by remember { mutableStateOf(showHint) } // ipucu görünüyor mu
    val ayar by rememberUpdatedState(settings) // jest içinde güncel ayar
    val ayarDegisti by rememberUpdatedState(onSettingsChange) // jest içinde güncel geri çağırma

    LaunchedEffect(nav?.id) { // sayfa isteği geldiğinde
        val r = nav ?: return@LaunchedEffect // istek yoksa çık
        pager.scrollToPage(r.page - 1) // sayfaya atla
        highlight = r.highlight // vurgulanacak ayet
        highlightKey = r.id // vurgu animasyonunu başlat
        chrome = false // çubukları kapat
        onNavConsumed() // istek tüketildi
    }
    LaunchedEffect(highlightKey) { // vurgu animasyonu
        if (highlight == null) return@LaunchedEffect // vurgu yoksa çık
        highlightAlpha.snapTo(1f) // tam görünür
        delay(3000) // 3 saniye görünür kalsın
        highlightAlpha.animateTo(0f, tween(1500)) // yavaşça sön
    }
    LaunchedEffect(pager) { snapshotFlow { pager.settledPage }.collect { onPageChanged(it + 1) } } // sayfa yerleşince kaydet
    LaunchedEffect(hint) { if (hint) { delay(5000); hint = false; onHintShown() } } // ipucu 5 sn sonra kaybolur

    SystemBars(active && !chrome && !sheet, settings.keepScreenOn && active) // tam ekran ve ekran açık

    Box(
        Modifier.fillMaxSize().paper(settings) // kağıt zemin
            .pointerInput(Unit) { // iki parmakla yumuşak büyütme
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial) // ilk parmak
                    var buyutuldu = false // bu jestte büyütme oldu mu
                    do {
                        val olay = awaitPointerEvent(PointerEventPass.Initial) // sonraki olay (sayfa çeviriciden önce)
                        if (olay.changes.count { it.pressed } >= 2) { // iki parmak varsa
                            val oran = olay.calculateZoom() // yakınlaşma oranı
                            if (oran != 1f) { // değiştiyse
                                val yeni = (ayar.scale * oran).coerceIn(ReaderSettings.MIN_SCALE, ReaderSettings.MAX_SCALE) // sınır içinde
                                ayarDegisti(ayar.copy(scale = yeni)) // anında uygula (her karede azar azar)
                            }
                            olay.changes.forEach { it.consume() } // sayfa çevirme tetiklenmesin
                            buyutuldu = true // büyütme oldu
                        }
                    } while (olay.changes.any { it.pressed }) // parmaklar kalkana kadar
                    if (buyutuldu) hint = false // ipucunu kapat
                }
            },
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) { // sayfa yönünü elle belirliyoruz
            HorizontalPager(
                state = pager, // durum
                reverseLayout = true, // mushaf gibi: sonraki sayfa soldan gelir
                beyondViewportPageCount = 1, // komşu sayfa önceden hazır
                key = { it }, // sayfa kimliği
                modifier = Modifier.fillMaxSize(), // tam ekran
            ) { i ->
                PageScroller(quran, i + 1, settings, highlight, highlightAlpha.value, onTap = { chrome = !chrome; hint = false }) // tek sayfa
            }
        }

        val sayfa = pager.currentPage + 1 // görünen sayfa
        AnimatedVisibility(chrome, Modifier.align(Alignment.TopCenter), enter = fadeIn() + slideInVertically { -it }, exit = fadeOut() + slideOutVertically { -it }) { // üst çubuk
            TopChrome(quran, sayfa, sayfa in bookmarkedPages, onOpenIndex, { onToggleBookmark(sayfa) }, onOpenSearch, { sheet = true }) // içerik
        }
        AnimatedVisibility(chrome, Modifier.align(Alignment.BottomCenter), enter = fadeIn() + slideInVertically { it }, exit = fadeOut() + slideOutVertically { it }) { // alt çubuk
            BottomChrome(quran, sayfa) { hedef -> scope.launch { pager.scrollToPage(hedef - 1) } } // sayfa kaydırıcısı
        }
        AnimatedVisibility(hint && !chrome, Modifier.align(Alignment.BottomCenter), enter = fadeIn(), exit = fadeOut()) { // ilk açılış ipucu
            Text(
                "Sayfa çevirmek için yana kaydır · Menü için dokun · İki parmakla büyüt", // ipucu metni
                Modifier.navigationBarsPadding().padding(Space.xl).clip(RoundedCornerShape(Radius.md)).background(MaterialTheme.colorScheme.inverseSurface).padding(horizontal = Space.l, vertical = Space.m), // hap kutu
                color = MaterialTheme.colorScheme.inverseOnSurface, style = MaterialTheme.typography.bodyMedium, // renk ve yazı
            )
        }
    }

    if (sheet) AppearanceSheet(settings, onSettingsChange, onDismiss = { sheet = false }) // görünüm paneli
}

/** Tek sayfa: büyütülünce dikey kayar, en altta sayfa numarası sabit durur. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PageScroller(quran: Quran, page: Int, s: ReaderSettings, highlight: AyahRef?, alpha: Float, onTap: () -> Unit) {
    val scope = rememberCoroutineScope() // kaydırma animasyonu için
    BoxWithConstraints(
        Modifier.fillMaxSize() // tam ekran
            .windowInsetsPadding(WindowInsets.statusBarsIgnoringVisibility.union(WindowInsets.navigationBarsIgnoringVisibility).union(WindowInsets.displayCutout)) // sistem çubukları gizlense de yerleşim kaymaz
            .pointerInput(Unit) { detectTapGestures(onTap = { onTap() }) }, // dokununca çubuklar
    ) {
        val yukseklik = maxHeight // görünür yükseklik
        val kaydirma = rememberScrollState() // sayfa içi kaydırma
        var ustY by remember { mutableFloatStateOf(0f) } // görünür alanın ekrandaki üst kenarı
        val pay = with(LocalDensity.current) { 96.dp.toPx() } // vurgulu ayetin üstünde bırakılacak boşluk
        QuranPage(
            quran, page, s, highlight, alpha, // sayfa verisi
            Modifier.fillMaxWidth().onGloballyPositioned { ustY = it.positionInRoot().y - kaydirma.value } // kaydırmasız üst kenar
                .verticalScroll(kaydirma).heightIn(min = yukseklik) // kısa sayfada sayfa no altta, uzunda kayar
                .padding(horizontal = Space.l, vertical = Space.s), // kenar boşluğu 16
            onHighlightAt = { y -> // vurgulu ayetin ekrandaki yeri bildirildi
                val hedef = (kaydirma.value + y - (ustY + kaydirma.value) - pay).toInt().coerceIn(0, kaydirma.maxValue) // ayet üstte görünecek şekilde
                if (kotlin.math.abs(hedef - kaydirma.value) > 4) scope.launch { kaydirma.animateScrollTo(hedef) } // gerekiyorsa kaydır
            },
        )
    }
}

/** Üst çubuk: içindekiler, sure/cüz, yer imi, arama, görünüm. */
@Composable
private fun TopChrome(quran: Quran, page: Int, bookmarked: Boolean, onIndex: () -> Unit, onBookmark: () -> Unit, onSearch: () -> Unit, onAppearance: () -> Unit) {
    val sure = quran.surahOfPage(page) // sayfanın suresi
    Row(
        Modifier.fillMaxWidth().background(appColors.chrome).statusBarsPadding().height(56.dp).padding(horizontal = Space.xs), // yarı saydam zemin
        verticalAlignment = Alignment.CenterVertically, // dikey ortalı
    ) {
        IconBtn(Lucide.List, "İçindekiler", onIndex) // içindekiler
        Column(Modifier.weight(1f).padding(horizontal = Space.xs)) { // başlık
            Text(sure.turkishName, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis) // sure adı
            Text("${quran.juzOfPage(page)}. Cüz · Sayfa $page", style = MaterialTheme.typography.bodySmall, color = appColors.muted, maxLines = 1) // cüz ve sayfa
        }
        IconBtn(if (bookmarked) Lucide.BookmarkCheck else Lucide.Bookmark, if (bookmarked) "Yer imini kaldır" else "Yer imi ekle", onBookmark, selected = bookmarked) // yer imi
        IconBtn(Lucide.Search, "Ara", onSearch) // arama
        IconBtn(Lucide.Type, "Görünüm", onAppearance) // görünüm
    }
}

/** Alt çubuk: sağdan sola sayfa kaydırıcısı. */
@Composable
private fun BottomChrome(quran: Quran, page: Int, onJump: (Int) -> Unit) {
    var surukle by remember(page) { mutableFloatStateOf(page.toFloat()) } // sürüklenen değer
    val hedef = surukle.toInt().coerceIn(1, quran.pageCount) // hedef sayfa
    Column(
        Modifier.fillMaxWidth().background(appColors.chrome).navigationBarsPadding().padding(horizontal = Space.l, vertical = Space.s), // yarı saydam zemin
        verticalArrangement = Arrangement.spacedBy(Space.xs), // aralık
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { // bilgi satırı
            Text("Sayfa $hedef / ${quran.pageCount}", style = MaterialTheme.typography.labelLarge) // sayfa
            Text("${quran.surahOfPage(hedef).turkishName} · ${quran.juzOfPage(hedef)}. Cüz", style = MaterialTheme.typography.labelLarge, color = appColors.muted) // sure ve cüz
        }
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) { // mushaf yönü: 1. sayfa sağda
            Slider(
                value = surukle, // değer
                onValueChange = { surukle = it }, // sürüklenirken
                onValueChangeFinished = { onJump(hedef) }, // bırakınca git
                valueRange = 1f..quran.pageCount.toFloat(), // 1-604
                modifier = Modifier.fillMaxWidth().height(48.dp), // dokunma alanı
            )
        }
    }
}

/** Sistem çubuklarını gizler/gösterir ve ekranın açık kalmasını ayarlar (ikon rengi MainActivity'de). */
@Composable
private fun SystemBars(immersive: Boolean, keepOn: Boolean) {
    val view = LocalView.current // kök görünüm
    SideEffect {
        val window = (view.context as? Activity)?.window ?: return@SideEffect // pencere
        val c = WindowCompat.getInsetsController(window, view) // denetleyici
        c.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE // kenardan kaydırınca geçici görünür
        if (immersive) c.hide(WindowInsetsCompat.Type.systemBars()) else c.show(WindowInsetsCompat.Type.systemBars()) // gizle/göster
        view.keepScreenOn = keepOn // ekran açık kalsın
    }
}
