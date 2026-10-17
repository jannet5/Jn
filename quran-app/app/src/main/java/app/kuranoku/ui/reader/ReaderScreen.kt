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
import androidx.compose.foundation.border // çerçeve
import androidx.compose.foundation.clickable // tıklanabilir
import androidx.compose.foundation.text.BasicTextField // yazı kutusu
import androidx.compose.foundation.text.KeyboardActions // klavye eylemleri
import androidx.compose.foundation.text.KeyboardOptions // klavye seçenekleri
import androidx.compose.foundation.gestures.awaitEachGesture // jest döngüsü
import androidx.compose.foundation.gestures.awaitFirstDown // ilk dokunuş
import androidx.compose.foundation.gestures.calculateZoom // iki parmak yakınlaşma oranı
import androidx.compose.foundation.gestures.detectTapGestures // dokunma algılama
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Box // kutu
import androidx.compose.foundation.layout.BoxWithConstraints // ölçülü kutu
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.ExperimentalLayoutApi // deneysel düzen API
import androidx.compose.foundation.layout.PaddingValues // iç boşluk değerleri
import androidx.compose.foundation.layout.Row // yatay dizi
import androidx.compose.foundation.layout.ime // klavye boşluğu
import androidx.compose.foundation.layout.navigationBars // gezinme çubuğu
import androidx.compose.foundation.layout.width // genişlik
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
import androidx.compose.foundation.ScrollState // kaydırma durumu tipi
import kotlin.math.roundToInt // yuvarlama
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.verticalScroll // dikey kaydırma
import androidx.compose.foundation.horizontalScroll // yatay kaydırma
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.Button // birincil buton
import androidx.compose.material3.Text // metin
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.DisposableEffect // bırakılırken yan etki
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
import androidx.compose.ui.draw.alpha // saydamlık
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.focus.onFocusChanged // odak değişimi
import androidx.compose.ui.graphics.SolidColor // düz boya
import androidx.compose.ui.platform.LocalFocusManager // odak yöneticisi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController // klavye denetimi
import androidx.compose.ui.semantics.LiveRegionMode // canlı bölge
import androidx.compose.ui.semantics.Role // erişilebilirlik rolü
import androidx.compose.ui.semantics.contentDescription // erişilebilirlik adı
import androidx.compose.ui.semantics.liveRegion // sesli duyuru
import androidx.compose.ui.semantics.semantics // erişilebilirlik bilgisi
import androidx.compose.ui.text.TextRange // seçim aralığı
import androidx.compose.ui.text.input.ImeAction // klavye eylemi
import androidx.compose.ui.text.input.KeyboardType // klavye türü
import androidx.compose.ui.text.input.TextFieldValue // yazı ve seçim
import androidx.compose.ui.text.style.TextAlign // metin hizası
import androidx.compose.ui.input.pointer.PointerEventPass // olay geçişi
import androidx.compose.ui.input.pointer.pointerInput // dokunma girişi
import androidx.compose.ui.platform.LocalLayoutDirection // yön
import androidx.compose.ui.platform.LocalView // görünüm
import androidx.compose.ui.platform.LocalDensity // yoğunluk
import androidx.compose.ui.layout.onGloballyPositioned // ekrandaki konum
import androidx.compose.ui.layout.positionInRoot // köke göre konum
import androidx.compose.ui.text.style.TextOverflow // taşma
import androidx.compose.ui.unit.LayoutDirection // yön tipi
import androidx.compose.ui.graphics.vector.ImageVector // ikon tipi
import androidx.compose.ui.unit.Density // yoğunluk tipi
import androidx.compose.ui.unit.dp // dp
import androidx.core.view.WindowCompat // pencere yardımcıları
import androidx.core.view.WindowInsetsCompat // sistem çubukları
import androidx.core.view.WindowInsetsControllerCompat // sistem çubuğu denetimi
import app.kuranoku.data.Quran // Kur'an
import app.kuranoku.data.ReaderSettings // ayarlar
import app.kuranoku.ui.NavRequest // sayfa isteği
import app.kuranoku.ui.components.Ico // ikon
import app.kuranoku.ui.components.IconBtn // ikon buton
import app.kuranoku.ui.components.Lucide // ikonlar
import app.kuranoku.ui.theme.IconSize // ikon boyutları
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
                                val yeni = (ayar.scale * oran).coerceIn(if (ayar.mushafLayout) 1f else ReaderSettings.MIN_SCALE, ReaderSettings.MAX_SCALE) // sınır içinde (mushafta en küçük hal: sayfa ekrana tam sığar)
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
            BottomChrome(
                quran, sayfa, // sayfa bilgisi
                onStep = { adim -> scope.launch { pager.animateScrollToPage((pager.targetPage + adim).coerceIn(0, quran.pageCount - 1)) } }, // bir sayfa ileri/geri (peş peşe basışta hedeften devam eder)
                onJump = { hedef -> scope.launch { pager.scrollToPage(hedef - 1) } }, // yazılan sayfaya git
            ) // alt çubuk
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
        val genislik = maxWidth // görünür genişlik
        if (s.mushafLayout && quran.mushafLines(page).isNotEmpty()) { // Medine mushafı sayfa düzeni
            val zoom = s.scale.coerceAtLeast(1f) // mushafta en küçük hali sayfanın ekrana tam sığması
            val dikey = rememberScrollState() // dikey kaydırma
            val yatay = rememberScrollState() // yatay kaydırma
            KeepReadingSpot(dikey) // büyütüp küçültünce okunan yer kaymasın
            KeepReadingSpot(yatay) // yatayda da
            Box(
                Modifier.fillMaxSize().verticalScroll(dikey) // büyütünce aşağı/yukarı gezinme
                    .horizontalScroll(yatay, reverseScrolling = true), // sağdan başlar; sola kaydırınca kenarda sonraki sayfaya geçer
            ) {
                MushafPage(quran, page, s, highlight, alpha, genislik * zoom, yukseklik * zoom, zoom) // sayfa
            }
            return@BoxWithConstraints // akan yazı çizilmez
        }
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

/** Sayfa büyüyüp küçülünce kaydırma konumunu aynı oranda tutar (okunan yer ekranda kalır). */
@Composable
private fun KeepReadingSpot(k: ScrollState) {
    LaunchedEffect(k) {
        var oncekiMax = k.maxValue // önceki kaydırma sınırı
        var oran = 0f // okunan yerin oranı
        snapshotFlow { k.maxValue to k.value }.collect { (mx, v) -> // sınır ya da konum değişti
            if (mx != oncekiMax) { // büyütme/küçültme oldu
                oncekiMax = mx // yeni sınır
                val hedef = (oran * mx).roundToInt().coerceIn(0, mx) // aynı oran
                if (hedef != v) k.scrollTo(hedef) // konumu düzelt
            } else if (mx > 0) oran = v.toFloat() / mx // kullanıcı kaydırdı: oranı sakla
        }
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

/** Alt çubuk: sayfa bilgisi, bir sayfa ileri/geri okları ve sayfa numarası yazıp gitme. */
@Composable
private fun BottomChrome(quran: Quran, page: Int, onStep: (Int) -> Unit, onJump: (Int) -> Unit) {
    var yazi by remember(page) { mutableStateOf(TextFieldValue(page.toString())) } // kutudaki yazı (sayfa değişince güncellenir)
    var hata by remember(page) { mutableStateOf(false) } // yazılan numara geçersiz mi
    val odak = LocalFocusManager.current // odak yöneticisi
    val klavye = LocalSoftwareKeyboardController.current // klavye denetimi
    DisposableEffect(Unit) { onDispose { klavye?.hide() } } // çubuk kapanırken klavye de kapansın
    LaunchedEffect(page) { odak.clearFocus(); klavye?.hide() } // sayfa değişince (ok, kaydırma, Git) kutu odağı bırakır, klavye kapanır
    val git: () -> Unit = { // "Git" düğmesi ya da klavyedeki Git
        val n = yazi.text.toIntOrNull() // yazılan numara
        when {
            yazi.text.isEmpty() -> { yazi = TextFieldValue(page.toString()); odak.clearFocus() } // boşsa sayfa değişmez
            n == null || n !in 1..quran.pageCount -> hata = true // geçersiz: uyar, gitme
            else -> { hata = false; odak.clearFocus(); onJump(n) } // geçerli: klavyeyi kapat, sayfayı aç
        }
    }
    Column(
        Modifier.fillMaxWidth().background(appColors.chrome).windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime)).padding(horizontal = Space.l, vertical = Space.s), // yarı saydam zemin; klavye açılınca üstüne çıkar
        verticalArrangement = Arrangement.spacedBy(Space.xs), // aralık
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { // bilgi satırı
            Text("Sayfa $page / ${quran.pageCount}", style = MaterialTheme.typography.labelLarge) // sayfa
            Text("${quran.surahOfPage(page).turkishName} · ${quran.juzOfPage(page)}. Cüz", style = MaterialTheme.typography.labelLarge, color = appColors.muted) // sure ve cüz
        }
        val yog = LocalDensity.current // ekran yoğunluğu
        CompositionLocalProvider(
            LocalLayoutDirection provides LayoutDirection.Ltr, // mushaf yönü: "Sonraki" solda, "Önceki" sağda (telefon dili ne olursa olsun)
            LocalDensity provides Density(yog.density, yog.fontScale.coerceAtMost(1.3f)), // en büyük sistem yazısında da düğme yazıları kırpılmasın
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Space.s)) { // oklar ve kutu
                StepButton(Lucide.ChevronLeft, "Sonraki", "Sonraki sayfa", page < quran.pageCount) { onStep(1) } // sola: sonraki sayfa
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Space.s)) { // ortada numara kutusu ve Git
                    val sekil = RoundedCornerShape(Radius.md) // orta köşe
                    BasicTextField(
                        value = yazi, // kutudaki yazı
                        onValueChange = { v -> // yazarken
                            val temiz = v.text.filter { it.isDigit() }.take(3) // sadece rakam, en çok 3 hane
                            yazi = if (temiz == v.text) v else TextFieldValue(temiz, TextRange(temiz.length)) // rakam olmayanı at
                            hata = false // uyarıyı kaldır
                        },
                        singleLine = true, // tek satır
                        textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center), // ortalı numara
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary), // imleç rengi
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Go), // rakam klavyesi, "Git" tuşu
                        keyboardActions = KeyboardActions(onGo = { git() }), // klavyedeki Git
                        modifier = Modifier.weight(1f).height(STEP_HEIGHT).semantics { contentDescription = "Sayfa numarası" }.onFocusChanged { f -> // dokununca eski numara silinir (yeni numara üstüne eklenmesin)
                            if (f.isFocused) yazi = TextFieldValue("") else if (yazi.text.isEmpty()) yazi = TextFieldValue(page.toString()) // odak gidince boşsa eski numara geri gelir
                        },
                        decorationBox = { kutu -> // görünüm
                            Box(
                                Modifier.fillMaxSize().clip(sekil).background(MaterialTheme.colorScheme.surfaceContainer)
                                    .border(if (hata) 2.dp else 1.dp, if (hata) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline, sekil), // hatada kırmızı çerçeve
                                contentAlignment = Alignment.Center, // ortalı
                            ) {
                                if (yazi.text.isEmpty()) Text(page.toString(), style = MaterialTheme.typography.titleMedium, color = appColors.muted) // yazarken eski numara soluk görünür
                                kutu() // yazı alanı
                            }
                        },
                    )
                    Button(
                        onClick = git, // git
                        shape = sekil, // orta köşe
                        contentPadding = PaddingValues(horizontal = Space.m), // dar iç boşluk
                        modifier = Modifier.height(STEP_HEIGHT), // yükseklik
                    ) { Text("Git", style = MaterialTheme.typography.labelLarge) } // birincil düğme
                }
                StepButton(Lucide.ChevronRight, "Önceki", "Önceki sayfa", page > 1) { onStep(-1) } // sağa: önceki sayfa
            }
        }
        if (hata) Text("1 ile ${quran.pageCount} arasında bir sayfa yaz", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite }, textAlign = TextAlign.Center) // uyarı (TalkBack okur)
    }
}

private val STEP_HEIGHT = 52.dp // alt çubuk düğmeleri ve kutu yüksekliği

/** Alt çubuktaki ok düğmesi: üstte ok, altında yazı. */
@Composable
private fun StepButton(icon: ImageVector, label: String, description: String, enabled: Boolean, onClick: () -> Unit) {
    val sekil = RoundedCornerShape(Radius.md) // orta köşe
    Column(
        Modifier.width(72.dp).height(STEP_HEIGHT).alpha(if (enabled) 1f else 0.38f).clip(sekil).border(1.dp, MaterialTheme.colorScheme.outline, sekil)
            .clickable(enabled = enabled, onClickLabel = description, role = Role.Button, onClick = onClick), // dokunma alanı
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, // ortalı
    ) {
        Ico(icon, null, size = IconSize.m) // ok
        Text(label, style = MaterialTheme.typography.labelMedium, maxLines = 1) // yazı
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
