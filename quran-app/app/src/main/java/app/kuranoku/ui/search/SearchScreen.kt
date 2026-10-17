package app.kuranoku.ui.search // arama ekranı

import androidx.compose.foundation.background // zemin
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.ExperimentalLayoutApi // deneysel düzen
import androidx.compose.foundation.layout.FlowRow // akan satır
import androidx.compose.foundation.layout.Row // yatay dizi
import androidx.compose.foundation.layout.fillMaxSize // tam boyut
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.heightIn // en az yükseklik
import androidx.compose.foundation.layout.imePadding // klavye boşluğu
import androidx.compose.foundation.layout.navigationBarsPadding // gezinme çubuğu
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.layout.statusBarsPadding // durum çubuğu
import androidx.compose.foundation.lazy.LazyColumn // dikey liste
import androidx.compose.foundation.lazy.itemsIndexed // indeksli öğeler
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.text.KeyboardActions // klavye eylemleri
import androidx.compose.foundation.text.KeyboardOptions // klavye seçenekleri
import androidx.compose.material3.AssistChip // çip
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.OutlinedTextField // metin kutusu
import androidx.compose.material3.OutlinedTextFieldDefaults // metin kutusu varsayılanları
import androidx.compose.material3.Text // metin
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.LaunchedEffect // yan etki
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.saveable.rememberSaveable // döndürmede korunan durum
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.focus.FocusRequester // odak isteği
import androidx.compose.ui.focus.focusRequester // odak bağlama
import androidx.compose.ui.text.TextStyle // yazı stili
import androidx.compose.ui.text.input.ImeAction // klavye eylemi
import androidx.compose.ui.text.style.TextDirection // yön
import androidx.compose.ui.unit.dp // dp
import androidx.compose.ui.unit.sp // sp
import app.kuranoku.data.QuranFont // yazı tipi
import app.kuranoku.data.SearchEngine // arama motoru
import app.kuranoku.data.Suggestion // öneri
import app.kuranoku.ui.components.EmptyState // boş durum
import app.kuranoku.ui.components.Ico // ikon
import app.kuranoku.ui.components.IconBtn // ikon buton
import app.kuranoku.ui.components.ListRow // liste satırı
import app.kuranoku.ui.components.Lucide // ikonlar
import app.kuranoku.ui.components.RowDivider // ayraç
import app.kuranoku.ui.components.SectionLabel // etiket
import app.kuranoku.ui.theme.IconSize // ikon boyutu
import app.kuranoku.ui.theme.Radius // köşeler
import app.kuranoku.ui.theme.Space // boşluklar
import app.kuranoku.ui.theme.appColors // ek renkler
import app.kuranoku.ui.theme.family // aile

/** Arama: yazdıkça sure/cüz/sayfa/ayet önerir; Enter ilk öneriye gider. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(engine: SearchEngine, font: QuranFont, lastPage: Int, onBack: () -> Unit, onPick: (Suggestion) -> Unit) {
    var q by rememberSaveable { mutableStateOf("") } // sorgu
    val sonuc = remember(q) { engine.search(q) } // canlı öneriler
    val odak = remember { FocusRequester() } // odak
    LaunchedEffect(Unit) { odak.requestFocus() } // açılınca klavye açılır

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).statusBarsPadding().imePadding()) { // tam ekran, klavyeye göre küçülür
        Row(Modifier.fillMaxWidth().padding(end = Space.l, top = Space.xs, bottom = Space.s), verticalAlignment = Alignment.CenterVertically) { // arama satırı
            IconBtn(Lucide.ChevronLeft, "Geri", onBack) // geri
            OutlinedTextField(
                value = q, onValueChange = { q = it }, // değer
                modifier = Modifier.weight(1f).focusRequester(odak), // genişlik ve odak
                placeholder = { Text("Sure, cüz, sayfa ya da 2:255") }, // ipucu
                leadingIcon = { Ico(Lucide.Search, null, size = IconSize.m, tint = appColors.muted) }, // büyüteç
                trailingIcon = { if (q.isNotEmpty()) IconBtn(Lucide.X, "Temizle", { q = "" }) }, // temizle
                singleLine = true, // tek satır
                shape = RoundedCornerShape(Radius.md), // orta köşe
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go), // klavyede "Git"
                keyboardActions = KeyboardActions(onGo = { sonuc.firstOrNull()?.let(onPick) }), // ilk öneriye git
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer, focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer), // zemin
            )
        }
        when {
            q.isBlank() -> Hints(lastPage) { q = it } // boş: ipuçları
            sonuc.isEmpty() -> EmptyState(Lucide.Search, "Sonuç yok", "“$q” ile eşleşen bir şey bulamadım. Sure adı (Mâide), cüz (20), sayfa (s 302) ya da ayet (2:255) yazabilirsin.") // sonuç yok
            else -> LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = Space.xl)) { // öneriler
                itemsIndexed(sonuc, key = { _, s -> "${s.kind}-${s.page}-${s.title}" }) { i, s -> // her öneri
                    if (i > 0) RowDivider() // ayraç
                    ListRow(
                        title = s.title, subtitle = s.subtitle, onClick = { onPick(s) }, badge = s.badge, highlighted = i == 0, // ilk öneri vurgulu (Enter ona gider)
                        trailing = if (s.arabic.isNotEmpty()) { { Text(s.arabic.removePrefix("سُورَةُ ").trim(), style = TextStyle(fontFamily = font.family(), fontSize = 18.sp, textDirection = TextDirection.Rtl)) } } else null, // Arapça ad
                    )
                }
            }
        }
    }
}

/** Boş sorguda hızlı geçiş çipleri ve örnekler. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Hints(lastPage: Int, onFill: (String) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(horizontal = Space.l, vertical = Space.s).navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(Space.xl)) {
        Column(verticalArrangement = Arrangement.spacedBy(Space.s)) {
            SectionLabel("Sık açılanlar") // etiket
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Space.s), verticalArrangement = Arrangement.spacedBy(Space.s)) { // akan çipler
                listOf("Yâsîn", "Kehf", "Mülk", "Rahmân", "Vâkıa", "Cüz 30", "Sayfa $lastPage").forEach { t -> // örnekler
                    AssistChip(onClick = { onFill(t) }, label = { Text(t) }, modifier = Modifier.heightIn(min = 48.dp), shape = RoundedCornerShape(Radius.sm)) // çip
                }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(Space.s)) {
            SectionLabel("Neler yazabilirsin") // etiket
            listOf("ma → Mâide, Mâûn" to "Sure adının başını yaz", "20 → 20. Cüz" to "Sadece sayı yazarsan önce cüz çıkar", "s 302 → 302. sayfa" to "Sayfaya gitmek için başına s koy", "2:255 → Bakara 255" to "Sure ve ayet numarası").forEach { (a, b) -> // örnek satırları
                Column {
                    Text(a, style = MaterialTheme.typography.titleMedium) // örnek
                    Text(b, style = MaterialTheme.typography.bodySmall, color = appColors.muted) // açıklama
                }
            }
        }
    }
}
