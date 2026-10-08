package app.kuranoku.ui.reader // okuma ekranı

import androidx.compose.animation.core.Animatable // elle animasyon
import androidx.compose.animation.core.tween // süreli animasyon
import androidx.compose.foundation.BorderStroke // çerçeve
import androidx.compose.foundation.background // zemin
import androidx.compose.foundation.border // çerçeve
import androidx.compose.foundation.clickable // tıklanabilir
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Box // kutu
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.PaddingValues // boşluk değerleri
import androidx.compose.foundation.layout.Row // yatay dizi
import androidx.compose.foundation.layout.Spacer // boşluk
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.heightIn // en az yükseklik
import androidx.compose.foundation.layout.navigationBarsPadding // gezinme çubuğu boşluğu
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.foundation.lazy.LazyRow // yatay liste
import androidx.compose.foundation.lazy.items // liste öğeleri
import androidx.compose.foundation.rememberScrollState // kaydırma durumu
import androidx.compose.foundation.selection.selectable // seçilebilir
import androidx.compose.foundation.shape.CircleShape // daire
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.verticalScroll // dikey kaydırma
import androidx.compose.material3.ExperimentalMaterial3Api // deneysel Material API
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.ModalBottomSheet // alt panel
import androidx.compose.material3.OutlinedButton // ikincil buton
import androidx.compose.material3.Slider // kaydırıcı
import androidx.compose.material3.Switch // anahtar
import androidx.compose.material3.Text // metin
import androidx.compose.material3.rememberModalBottomSheetState // panel durumu
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.rememberCoroutineScope // eşzamanlılık alanı
import androidx.compose.runtime.rememberUpdatedState // güncel değer
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.luminance // parlaklık
import androidx.compose.ui.semantics.Role // erişilebilirlik rolü
import androidx.compose.ui.text.TextStyle // yazı stili
import androidx.compose.ui.text.style.TextAlign // hiza
import androidx.compose.ui.text.style.TextDirection // yön
import androidx.compose.ui.text.style.TextOverflow // taşma
import androidx.compose.ui.unit.dp // dp
import androidx.compose.ui.unit.sp // sp
import app.kuranoku.data.QuranFont // yazı tipleri
import app.kuranoku.data.ReaderSettings // ayarlar
import app.kuranoku.ui.components.Ico // ikon
import app.kuranoku.ui.components.IconBtn // ikon buton
import app.kuranoku.ui.components.Lucide // ikonlar
import app.kuranoku.ui.components.SectionLabel // bölüm etiketi
import app.kuranoku.ui.theme.IconSize // ikon boyutu
import app.kuranoku.ui.theme.InkColors // yazı renkleri
import app.kuranoku.ui.theme.PageColors // sayfa renkleri
import app.kuranoku.ui.theme.PagePresets // hazır görünümler
import app.kuranoku.ui.theme.Radius // köşeler
import app.kuranoku.ui.theme.Space // boşluklar
import app.kuranoku.ui.theme.appColors // ek renkler
import app.kuranoku.ui.theme.contrast // kontrast
import app.kuranoku.ui.theme.family // yazı tipi ailesi
import app.kuranoku.ui.theme.withInkColor // yazı rengi uygula
import app.kuranoku.ui.theme.withPageColor // sayfa rengi uygula
import app.kuranoku.ui.theme.withPreset // hazır görünüm uygula
import app.kuranoku.ui.theme.onSwatch // kutu üstü renk
import kotlinx.coroutines.launch // başlatma
import kotlin.math.roundToInt // yuvarlama

/** Görünüm paneli: yazı boyutu, sayfa görünümü, renkler, yazı tipi. Değişiklikler arkadaki sayfada anında görünür. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSheet(s: ReaderSettings, onChange: (ReaderSettings) -> Unit, onDismiss: () -> Unit) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = false) // yarım açılabilir
    var renkSecici by remember { mutableStateOf<Boolean?>(null) } // özel renk penceresi (true: sayfa, false: yazı)
    ModalBottomSheet(
        onDismissRequest = onDismiss, // kapatma
        sheetState = state, // durum
        scrimColor = Color.Transparent, // arkadaki sayfa kararmasın, değişiklik canlı görünsün
        shape = RoundedCornerShape(topStart = Radius.lg, topEnd = Radius.lg), // büyük köşe
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow, // zemin
    ) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(bottom = Space.xl), // kaydırılabilir
            verticalArrangement = Arrangement.spacedBy(Space.xl), // bölümler arası 24
        ) {
            LayoutSection(s, onChange) // sayfa düzeni (en üstte)
            SizeSection(s, onChange) // yazı boyutu
            PresetSection(s, onChange) // hazır görünümler
            ColorSection("Sayfa rengi", PageColors, s.pageColor, { onChange(s.withPageColor(it)) }, { renkSecici = true }) // sayfa rengi
            ColorSection("Yazı rengi", InkColors, s.inkColor, { onChange(s.withInkColor(it)) }, { renkSecici = false }) // yazı rengi
            if (contrast(s.inkColor, s.pageColor) < 3f) ContrastWarning() // okunurluk uyarısı
            Column(Modifier.padding(horizontal = Space.l)) { // anahtarlar
                SwitchRow("Kağıt dokusu", "Lifli kağıt ve kabartma yazı", s.texture) { onChange(s.copy(texture = it, presetId = "custom")) } // doku
                SwitchRow("Süslü çerçeve", "Mushaf gibi altın çizgili kenar", s.frame) { onChange(s.copy(frame = it, presetId = "custom")) } // çerçeve
                SwitchRow("Ekran açık kalsın", "Okurken ekran kararmaz", s.keepScreenOn) { onChange(s.copy(keepScreenOn = it)) } // ekran açık
            }
            OutlinedButton( // varsayılana dön
                onClick = { onChange(ReaderSettings(keepScreenOn = s.keepScreenOn)) }, // varsayılan ayarlar
                modifier = Modifier.padding(horizontal = Space.l).fillMaxWidth().heightIn(min = 48.dp), // tam genişlik
                shape = RoundedCornerShape(Radius.md), // orta köşe
            ) {
                Ico(Lucide.RotateCcw, null, size = IconSize.m) // ikon
                Spacer(Modifier.width(Space.s)) // aralık
                Text("Varsayılana dön") // metin
            }
        }
    }
    renkSecici?.let { sayfaMi -> // özel renk penceresi
        ColorPickerDialog(
            title = if (sayfaMi) "Sayfa rengi" else "Yazı rengi", // başlık
            initial = if (sayfaMi) s.pageColor else s.inkColor, // başlangıç
            onPick = { c -> onChange(if (sayfaMi) s.withPageColor(c) else s.withInkColor(c)); renkSecici = null }, // seçildi
            onDismiss = { renkSecici = null }, // vazgeçildi
        )
    }
}

/** Yazı boyutu: %1 adımlı kaydırıcı ve yumuşak animasyonlu −/+ butonları. */
@Composable
private fun SizeSection(s: ReaderSettings, onChange: (ReaderSettings) -> Unit) {
    val scope = rememberCoroutineScope() // eşzamanlılık alanı
    val anim = remember { Animatable(s.scale) } // buton animasyonu
    val guncel by rememberUpdatedState(s) // güncel ayar
    fun adim(fark: Float) = scope.launch { // butonla 5'er yüzde, yumuşak geçişle
        anim.snapTo(guncel.scale) // şu anki değerden başla
        val hedef = ((guncel.scale + fark) * 100).roundToInt().div(100f).coerceIn(ReaderSettings.MIN_SCALE, ReaderSettings.MAX_SCALE) // hedef
        anim.animateTo(hedef, tween(260)) { onChange(guncel.copy(scale = value)) } // her karede uygula
    }
    Column(Modifier.padding(horizontal = Space.l), verticalArrangement = Arrangement.spacedBy(Space.xs)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { // başlık satırı
            SectionLabel(if (s.mushafLayout) "Sayfayı büyüt" else "Yazı boyutu") // etiket (mushafta sayfa net büyür)
            Text("%${(s.scale * 100).roundToInt()}", style = MaterialTheme.typography.titleMedium) // yüzde
        }
        Row(verticalAlignment = Alignment.CenterVertically) { // kaydırıcı satırı
            IconBtn(Lucide.Minus, "Küçült", { adim(-0.05f) }) // küçült
            Slider(
                value = s.scale, // değer
                onValueChange = { onChange(s.copy(scale = (it * 100).roundToInt() / 100f)) }, // %1 hassasiyet, anında
                valueRange = (if (s.mushafLayout) 1f else ReaderSettings.MIN_SCALE)..ReaderSettings.MAX_SCALE, // mushafta %100 (tam sayfa) – %255, akan yazıda %65 – %255
                modifier = Modifier.weight(1f).height(48.dp), // dokunma alanı
            )
            IconBtn(Lucide.Plus, "Büyüt", { adim(0.05f) }) // büyüt
        }
    }
}

/** Hazır görünüm kartları (küçük sayfa önizlemesi). */
@Composable
private fun PresetSection(s: ReaderSettings, onChange: (ReaderSettings) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Space.s)) {
        SectionLabel("Sayfa görünümü", Modifier.padding(horizontal = Space.l)) // etiket
        LazyRow(contentPadding = PaddingValues(horizontal = Space.l), horizontalArrangement = Arrangement.spacedBy(Space.m)) { // yatay liste
            items(PagePresets, key = { it.id }) { p -> // her görünüm
                val secili = s.presetId == p.id // seçili mi
                Column(
                    Modifier.width(88.dp).clip(RoundedCornerShape(Radius.md)).selectable(secili, role = Role.RadioButton) { onChange(s.withPreset(p)) }, // seçilebilir
                    horizontalAlignment = Alignment.CenterHorizontally, // ortalı
                    verticalArrangement = Arrangement.spacedBy(Space.xs), // aralık
                ) {
                    Box(
                        Modifier.size(88.dp, 112.dp).clip(RoundedCornerShape(Radius.md)) // mini sayfa
                            .paper(s.copy(pageColor = p.page, texture = p.texture)) // aynı kağıt çizimi
                            .border(if (secili) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline), RoundedCornerShape(Radius.md)), // seçili çerçeve
                        contentAlignment = Alignment.Center, // ortalı
                    ) {
                        Text("بِسْمِ ٱللَّهِ", style = TextStyle(fontFamily = s.font.family(), fontSize = 20.sp, color = Color(p.ink), textDirection = TextDirection.Rtl)) // örnek yazı
                        if (p.frame) Box(Modifier.matchParentSize().padding(Space.s).border(1.dp, Color(p.ornament)).padding(3.dp).border(0.5.dp, Color(p.ornament).copy(alpha = 0.7f))) // çerçeve önizlemesi (çift çizgi)
                    }
                    Text(p.label, style = MaterialTheme.typography.labelLarge, color = if (secili) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface) // ad
                }
            }
        }
    }
}

/** Renk kutuları + özel renk butonu. */
@Composable
private fun ColorSection(title: String, colors: List<Pair<Int, String>>, selected: Int, onPick: (Int) -> Unit, onCustom: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Space.s)) {
        SectionLabel(title, Modifier.padding(horizontal = Space.l)) // etiket
        LazyRow(contentPadding = PaddingValues(horizontal = Space.l), horizontalArrangement = Arrangement.spacedBy(Space.s)) { // yatay liste
            item { // özel renk
                Box(
                    Modifier.size(44.dp).clip(CircleShape).border(1.dp, MaterialTheme.colorScheme.outline, CircleShape).clickable(onClickLabel = "Özel renk seç", onClick = onCustom), // daire buton
                    contentAlignment = Alignment.Center, // ortalı
                ) { Ico(Lucide.Palette, "Özel renk", size = IconSize.m) } // ikon
            }
            items(colors, key = { it.first }) { (c, ad) -> // her renk
                val secili = c == selected // seçili mi
                Box(
                    Modifier.size(44.dp).clip(CircleShape).background(Color(c)) // renk dairesi
                        .border(if (secili) 2.5.dp else 1.dp, if (secili) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, CircleShape) // seçili çerçeve
                        .selectable(secili, role = Role.RadioButton) { onPick(c) }, // seçilebilir
                    contentAlignment = Alignment.Center, // ortalı
                ) {
                    if (secili) Ico(Lucide.Check, ad, size = IconSize.m, tint = onSwatch(c)) // seçili işareti (zemine göre koyu/açık)
                }
            }
        }
    }
}

/** Düşük kontrast uyarısı. */
@Composable
private fun ContrastWarning() {
    Row(Modifier.padding(horizontal = Space.l), horizontalArrangement = Arrangement.spacedBy(Space.s), verticalAlignment = Alignment.CenterVertically) {
        Ico(Lucide.TriangleAlert, null, size = IconSize.m, tint = MaterialTheme.colorScheme.error) // uyarı ikonu
        Text("Yazı ile sayfa rengi birbirine çok yakın, okumak zor olabilir.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error) // uyarı
    }
}

/** Başlık + açıklama + anahtar satırı. */
@Composable
private fun SwitchRow(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 56.dp).clickable { onChange(!checked) }, // satırın tamamı tıklanır
        verticalAlignment = Alignment.CenterVertically, // dikey ortalı
    ) {
        Column(Modifier.weight(1f)) { // metinler
            Text(title, style = MaterialTheme.typography.titleMedium) // başlık
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = appColors.muted) // açıklama
        }
        Switch(checked = checked, onCheckedChange = onChange) // anahtar
    }
}

/** Sayfa düzeni: Medine mushafı sayfası ya da ekrana akan yazı. */
@Composable
private fun LayoutSection(s: ReaderSettings, onChange: (ReaderSettings) -> Unit) {
    Column(Modifier.padding(horizontal = Space.l), verticalArrangement = Arrangement.spacedBy(Space.s)) {
        SectionLabel("Sayfa düzeni") // etiket
        listOf(
            Triple(true, "Mushaf sayfası", "Medine mushafının birebir aynısı: 15 satır, aynı kelimeler aynı yerde"), // mushaf
            Triple(false, "Akan yazı", "Satırlar ekrana göre akar; çok büyük yazıyla okumak için"), // akan
        ).forEach { (deger, ad, not) ->
            val secili = s.mushafLayout == deger // seçili mi
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(Radius.md)) // kart
                    .border(if (secili) 2.dp else 1.dp, if (secili) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, RoundedCornerShape(Radius.md)) // çerçeve
                    .selectable(secili, role = Role.RadioButton) { onChange(s.copy(mushafLayout = deger, scale = if (deger) 1f else s.scale)) } // seçilebilir (mushafa dönünce sayfa tam sığar)
                    .heightIn(min = 56.dp).padding(horizontal = Space.l, vertical = Space.s), // iç boşluk
                verticalAlignment = Alignment.CenterVertically, // dikey ortalı
                horizontalArrangement = Arrangement.spacedBy(Space.m), // aralık
            ) {
                Column(Modifier.weight(1f)) { // ad ve not
                    Text(ad, style = MaterialTheme.typography.titleMedium) // ad
                    Text(not, style = MaterialTheme.typography.bodySmall, color = appColors.muted) // açıklama
                }
                if (secili) Ico(Lucide.Check, null, size = IconSize.m, tint = MaterialTheme.colorScheme.primary) // seçili işareti
            }
        }
    }
}
