package app.kuranoku.ui.index // içindekiler ekranı

import androidx.compose.foundation.background // zemin
import androidx.compose.foundation.layout.Box // kutu
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.PaddingValues // boşluk değerleri
import androidx.compose.foundation.layout.fillMaxSize // tam boyut
import androidx.compose.foundation.layout.navigationBarsPadding // gezinme çubuğu
import androidx.compose.foundation.lazy.LazyColumn // dikey liste
import androidx.compose.foundation.lazy.itemsIndexed // indeksli öğeler
import androidx.compose.foundation.lazy.rememberLazyListState // liste durumu
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.SnackbarDuration // bildirim süresi
import androidx.compose.material3.SnackbarHost // bildirim alanı
import androidx.compose.material3.SnackbarHostState // bildirim durumu
import androidx.compose.material3.SnackbarResult // bildirim sonucu
import androidx.compose.material3.Tab // sekme
import androidx.compose.material3.TabRow // sekme satırı
import androidx.compose.material3.Text // metin
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableIntStateOf // tamsayı durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.rememberCoroutineScope // eşzamanlılık alanı
import androidx.compose.runtime.saveable.rememberSaveable // korunan durum
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.text.TextStyle // yazı stili
import androidx.compose.ui.text.style.TextDirection // yön
import androidx.compose.ui.unit.dp // dp
import androidx.compose.ui.unit.sp // sp
import app.kuranoku.data.Bookmark // yer imi
import app.kuranoku.data.Quran // Kur'an
import app.kuranoku.data.QuranFont // yazı tipi
import app.kuranoku.ui.components.EmptyState // boş durum
import app.kuranoku.ui.components.IconBtn // ikon buton
import app.kuranoku.ui.components.ListRow // liste satırı
import app.kuranoku.ui.components.Lucide // ikonlar
import app.kuranoku.ui.components.RowDivider // ayraç
import app.kuranoku.ui.components.TopBar // üst çubuk
import app.kuranoku.ui.theme.Space // boşluklar
import app.kuranoku.ui.theme.family // aile
import kotlinx.coroutines.launch // başlatma
import java.text.SimpleDateFormat // tarih biçimi
import java.util.Date // tarih
import java.util.Locale // dil

/** İçindekiler: Sureler · Cüzler · Yer imleri. */
@Composable
fun IndexScreen(
    quran: Quran, // Kur'an
    font: QuranFont, // yazı tipi
    currentPage: Int, // şu anki sayfa
    bookmarks: List<Bookmark>, // yer imleri
    onBack: () -> Unit, // geri
    onOpenPage: (Int) -> Unit, // sayfaya git
    onRemoveBookmark: (Bookmark) -> Unit, // yer imini sil
    onRestoreBookmark: (Bookmark) -> Unit, // geri al
    onAbout: () -> Unit, // hakkında
) {
    var sekme by rememberSaveable { mutableIntStateOf(0) } // seçili sekme
    val bildirim = remember { SnackbarHostState() } // geri al bildirimi
    val scope = rememberCoroutineScope() // eşzamanlılık alanı
    val suankiSure = quran.surahOfPage(currentPage).number // okunan sure
    val suankiCuz = quran.juzOfPage(currentPage) // okunan cüz

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { // tam ekran
        Column(Modifier.fillMaxSize()) {
            TopBar("İçindekiler", onBack) { IconBtn(Lucide.Info, "Hakkında", onAbout) } // üst çubuk
            TabRow(selectedTabIndex = sekme, containerColor = MaterialTheme.colorScheme.background) { // sekmeler
                listOf("Sureler", "Cüzler", "Yer imleri").forEachIndexed { i, t -> Tab(selected = sekme == i, onClick = { sekme = i }, text = { Text(t) }) } // her sekme
            }
            when (sekme) {
                0 -> { // sureler
                    val liste = rememberLazyListState(initialFirstVisibleItemIndex = (suankiSure - 3).coerceAtLeast(0)) // okunan sureye yakın açılır
                    LazyColumn(Modifier.fillMaxSize(), liste, contentPadding = PaddingValues(bottom = Space.xl), ) {
                        itemsIndexed(quran.surahs, key = { _, s -> s.number }) { i, s -> // her sure
                            if (i > 0) RowDivider() // ayraç
                            ListRow(
                                title = s.turkishName, subtitle = "${s.ayahCount} ayet · ${if (s.meccan) "Mekkî" else "Medenî"} · sayfa ${s.startPage}", // ad ve bilgi
                                onClick = { onOpenPage(s.startPage) }, badge = s.number.toString(), highlighted = s.number == suankiSure, // dokununca git
                                trailing = { Text(s.arabicName.removePrefix("سُورَةُ ").trim(), style = TextStyle(fontFamily = font.family(), fontSize = 18.sp, textDirection = TextDirection.Rtl, color = MaterialTheme.colorScheme.onSurface)) }, // Arapça ad
                            )
                        }
                        item { Box(Modifier.navigationBarsPadding()) } // alt boşluk
                    }
                }
                1 -> { // cüzler
                    val liste = rememberLazyListState(initialFirstVisibleItemIndex = (suankiCuz - 3).coerceAtLeast(0)) // okunan cüze yakın
                    LazyColumn(Modifier.fillMaxSize(), liste, contentPadding = PaddingValues(bottom = Space.xl)) {
                        itemsIndexed(quran.juzs, key = { _, j -> j.number }) { i, j -> // her cüz
                            val s = quran.surahs[j.startSura - 1] // başladığı sure
                            if (i > 0) RowDivider() // ayraç
                            ListRow(
                                title = "${j.number}. Cüz", subtitle = "${s.turkishName} ${j.startAyah}. ayet · sayfa ${j.startPage}", // başlık ve başlangıç
                                onClick = { onOpenPage(j.startPage) }, badge = j.number.toString(), highlighted = j.number == suankiCuz, // dokununca git
                            )
                        }
                        item { Box(Modifier.navigationBarsPadding()) } // alt boşluk
                    }
                }
                else -> { // yer imleri
                    if (bookmarks.isEmpty()) EmptyState(Lucide.Bookmark, "Henüz yer imi yok", "Okurken sayfaya bir kez dokun, üstteki yer imi simgesine bas. İşaretlediğin sayfalar burada durur.", "Okumaya dön", onBack) // boş durum
                    else LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = Space.xl)) {
                        itemsIndexed(bookmarks, key = { _, b -> b.page }) { i, b -> // her yer imi
                            val s = quran.surahOfPage(b.page) // sayfanın suresi
                            if (i > 0) RowDivider() // ayraç
                            ListRow(
                                title = "Sayfa ${b.page} · ${s.turkishName}", subtitle = "${quran.juzOfPage(b.page)}. Cüz · ${tarih(b.createdAt)}", // başlık ve tarih
                                onClick = { onOpenPage(b.page) }, showChevron = false, // dokununca git
                                trailing = {
                                    IconBtn(Lucide.Trash, "Yer imini sil", { // sil (geri alınabilir)
                                        onRemoveBookmark(b) // siliniyor
                                        scope.launch { // geri al bildirimi
                                            bildirim.currentSnackbarData?.dismiss() // önceki bildirim kapanır
                                            val r = bildirim.showSnackbar("Yer imi silindi", "Geri al", duration = SnackbarDuration.Short) // bildirim
                                            if (r == SnackbarResult.ActionPerformed) onRestoreBookmark(b) // geri alındı
                                        }
                                    })
                                },
                            )
                        }
                        item { Box(Modifier.navigationBarsPadding()) } // alt boşluk
                    }
                }
            }
        }
        SnackbarHost(bildirim, Modifier.align(Alignment.BottomCenter).navigationBarsPadding()) // bildirim alanı
    }
}

private fun tarih(ms: Long): String = SimpleDateFormat("d MMMM yyyy", Locale.forLanguageTag("tr")).format(Date(ms)) // "4 Ekim 2026"
