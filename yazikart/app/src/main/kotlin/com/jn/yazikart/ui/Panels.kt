@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class) // parçalı butonlar ve alt pencere deneysel API
package com.jn.yazikart.ui // arayüz paketi

import androidx.compose.foundation.background // arka plan boyama
import androidx.compose.foundation.border // çerçeve
import androidx.compose.foundation.clickable // tıklanabilirlik
import androidx.compose.foundation.layout.Arrangement // dizilim aralığı
import androidx.compose.foundation.layout.Column // alt alta yerleşim
import androidx.compose.foundation.layout.PaddingValues // iç boşluk değerleri
import androidx.compose.foundation.layout.Row // yan yana yerleşim
import androidx.compose.foundation.layout.Spacer // boşluk
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.padding // dış boşluk
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.foundation.lazy.LazyColumn // dikey kaydırılabilir liste
import androidx.compose.foundation.lazy.LazyRow // yatay kaydırılabilir liste
import androidx.compose.foundation.lazy.itemsIndexed // sıralı öğe ekleme
import androidx.compose.foundation.lazy.rememberLazyListState // liste kaydırma durumu
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.filled.Close // kapat ikonu
import androidx.compose.material.icons.filled.Image // galeri ikonu
import androidx.compose.material.icons.filled.Search // büyüteç
import androidx.compose.material.icons.filled.WifiOff // internet yok ikonu
import androidx.compose.material3.Button // birincil buton
import androidx.compose.material3.ExperimentalMaterial3Api // deneysel bileşenler
import androidx.compose.material3.FilterChip // seçilebilir çip
import androidx.compose.material3.Icon // ikon
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.ModalBottomSheet // alttan açılan pencere
import androidx.compose.material3.OutlinedButton // ikincil buton
import androidx.compose.material3.SegmentedButton // parçalı buton
import androidx.compose.material3.SegmentedButtonDefaults // parçalı buton şekilleri
import androidx.compose.material3.SingleChoiceSegmentedButtonRow // tek seçimli parçalı buton satırı
import androidx.compose.material3.Slider // kaydırıcı
import androidx.compose.material3.Switch // aç/kapa düğmesi
import androidx.compose.material3.Text // yazı
import androidx.compose.material3.TextButton // yazı buton
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.LaunchedEffect // açılınca çalışan iş
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.platform.LocalContext // uygulama bağlamı
import androidx.compose.ui.text.font.FontFamily // Compose yazı tipi ailesi
import androidx.compose.ui.text.font.FontWeight // yazı kalınlığı
import androidx.compose.ui.text.style.TextOverflow // taşan yazı
import androidx.compose.ui.unit.dp // ölçü birimi
import androidx.compose.ui.unit.sp // yazı ölçü birimi
import com.jn.yazikart.data.AspectRatio // görsel oranı
import com.jn.yazikart.data.ExportFormat // kayıt biçimi
import com.jn.yazikart.data.FontCatalog // yazı tipleri
import com.jn.yazikart.data.PostStyle // görsel ayarları
import com.jn.yazikart.data.TextAlign // yatay hiza
import com.jn.yazikart.data.VerticalPos // dikey konum

private val SIDE = 16.dp // tüm uygulamada tek kenar boşluğu

// Bölüm başlığı (küçük, soluk)
@Composable
private fun Label(text: String) {
    Text(
        text, // başlık metni
        style = MaterialTheme.typography.labelLarge, // küçük başlık
        color = MaterialTheme.colorScheme.onSurfaceVariant, // soluk renk
        modifier = Modifier.padding(horizontal = SIDE), // kenar boşluğu
    )
}

// ZEMİN sekmesi: renk + internetten/galeriden resim
@Composable
fun BackgroundPanel(
    style: PostStyle, // güncel stil
    hasImage: Boolean, // arka plan resmi var mı
    online: Boolean, // internet var mı
    onStyle: ((PostStyle) -> PostStyle) -> Unit, // stil değiştir
    onOpenSearch: () -> Unit, // internetten ara
    onPickGallery: () -> Unit, // galeriden seç
    onClearImage: () -> Unit, // resmi kaldır
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { // alt alta
        Label("Zemin rengi") // başlık
        ColorRow(style.backgroundColor) { c -> onStyle { it.copy(backgroundColor = c) } } // renk şeridi

        Spacer(Modifier.height(12.dp)) // bölümler arası boşluk
        Label("Arka plan görseli") // başlık
        Row(Modifier.padding(horizontal = SIDE), horizontalArrangement = Arrangement.spacedBy(12.dp)) { // yan yana iki buton
            Button(onClick = onOpenSearch, enabled = online, modifier = Modifier.weight(1f).height(48.dp)) { // internetten ara
                Icon(if (online) Icons.Default.Search else Icons.Default.WifiOff, contentDescription = null) // ikon
                Spacer(Modifier.width(8.dp)) // boşluk
                Text(if (online) "İnternetten ara" else "İnternet yok") // etiket: bağlantıya göre
            }
            OutlinedButton(onClick = onPickGallery, modifier = Modifier.weight(1f).height(48.dp)) { // galeriden seç
                Icon(Icons.Default.Image, contentDescription = null) // ikon
                Spacer(Modifier.width(8.dp)) // boşluk
                Text("Galeriden") // etiket
            }
        }
        if (hasImage) { // resim varsa ek ayarlar
            Label("Karartma (yazı daha net okunsun)") // başlık
            Slider( // karartma kaydırıcısı
                value = style.dim, // güncel değer
                onValueChange = { v -> onStyle { it.copy(dim = v) } }, // değişince
                valueRange = 0f..0.8f, // en fazla %80
                modifier = Modifier.padding(horizontal = SIDE), // kenar boşluğu
            )
            TextButton(onClick = onClearImage, modifier = Modifier.padding(horizontal = 8.dp)) { // resmi kaldır
                Icon(Icons.Default.Close, contentDescription = null) // ikon
                Spacer(Modifier.width(8.dp)) // boşluk
                Text("Görseli kaldır, sadece renk kalsın") // etiket
            }
        }
    }
}

// YAZI TİPİ sekmesi: yatay hızlı seçim + 50'sinin tam listesi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontPanel(style: PostStyle, onStyle: ((PostStyle) -> PostStyle) -> Unit) {
    val context = LocalContext.current // uygulama bağlamı
    val families = remember { // 50 yazı tipinin Compose karşılıkları bir kez hazırlanıyor
        FontCatalog.fonts.indices.map { FontFamily(FontCatalog.typeface(context.assets, it, false)) } // her biri dosyasından
    }
    var showAll by remember { mutableStateOf(false) } // tam liste açık mı
    val rowState = rememberLazyListState() // yatay liste durumu
    LaunchedEffect(Unit) { rowState.scrollToItem(style.fontIndex) } // seçili yazı tipi görünür olsun

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { // alt alta
        Label("Yazı tipi (${FontCatalog.fonts.size} adet)") // başlık
        LazyRow( // yatay yazı tipi şeridi
            state = rowState, // kaydırma durumu
            contentPadding = PaddingValues(horizontal = SIDE), // kenar boşlukları
            horizontalArrangement = Arrangement.spacedBy(8.dp), // aralık
        ) {
            itemsIndexed(FontCatalog.fonts) { i, f -> // her yazı tipi
                FilterChip( // seçilebilir çip
                    selected = i == style.fontIndex, // seçili mi
                    onClick = { onStyle { it.copy(fontIndex = i) } }, // dokununca seç
                    label = { Text(f.name.substringBefore(" ("), fontFamily = families[i], fontSize = 16.sp) }, // adı kendi yazı tipiyle
                )
            }
        }
        OutlinedButton(onClick = { showAll = true }, modifier = Modifier.padding(horizontal = SIDE).fillMaxWidth().height(48.dp)) { // tam liste
            Text("Tümünü yazınla önizle") // etiket
        }
    }

    if (showAll) { // tam liste penceresi
        ModalBottomSheet(onDismissRequest = { showAll = false }) { // alttan açılan pencere
            val sample = style.text.ifBlank { "Merhaba dünya! Çğıöşü" }.lines().first().take(40) // önizleme metni: kullanıcının yazısı
            LazyColumn(contentPadding = PaddingValues(bottom = 32.dp)) { // dikey liste
                itemsIndexed(FontCatalog.fonts) { i, f -> // her yazı tipi
                    val selected = i == style.fontIndex // seçili mi
                    Column(
                        Modifier.fillMaxWidth() // tam genişlik
                            .clickable { onStyle { it.copy(fontIndex = i) }; showAll = false } // dokununca seç ve kapat
                            .background(if (selected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface) // seçiliyse vurgulu zemin
                            .padding(horizontal = SIDE, vertical = 12.dp), // iç boşluk
                    ) {
                        Text(f.name, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) // yazı tipi adı
                        Text(sample, fontFamily = families[i], fontSize = 22.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) // önizleme
                    }
                }
            }
        }
    }
}

// YAZI sekmesi: boyut, renk, kalın, gölge, hiza, konum
@Composable
fun TextPanel(style: PostStyle, onStyle: ((PostStyle) -> PostStyle) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { // alt alta
        Label("Yazı boyutu") // başlık
        Slider( // boyut kaydırıcısı
            value = style.textSize, // güncel değer
            onValueChange = { v -> onStyle { it.copy(textSize = v) } }, // değişince
            valueRange = 0.03f..0.2f, // küçükten büyüğe aralık
            modifier = Modifier.padding(horizontal = SIDE), // kenar boşluğu
        )
        Label("Yazı rengi") // başlık
        ColorRow(style.textColor) { c -> onStyle { it.copy(textColor = c) } } // renk şeridi

        ToggleRow("Kalın", style.bold) { b -> onStyle { it.copy(bold = b) } } // kalın aç/kapa
        ToggleRow("Gölge (resim üstünde okunurluk)", style.shadow) { b -> onStyle { it.copy(shadow = b) } } // gölge aç/kapa

        Label("Hiza") // başlık
        Segmented( // sol / orta / sağ
            options = listOf(TextAlign.LEFT to "Sol", TextAlign.CENTER to "Orta", TextAlign.RIGHT to "Sağ"), // seçenekler
            selected = style.align, // seçili
        ) { a -> onStyle { it.copy(align = a) } } // değişince

        Label("Konum") // başlık
        Segmented( // üst / orta / alt
            options = listOf(VerticalPos.TOP to "Üst", VerticalPos.CENTER to "Orta", VerticalPos.BOTTOM to "Alt"), // seçenekler
            selected = style.verticalPos, // seçili
        ) { p -> onStyle { it.copy(verticalPos = p) } } // değişince
    }
}

// BOYUT sekmesi: kare / dikey / hikaye
@Composable
fun SizePanel(style: PostStyle, onStyle: ((PostStyle) -> PostStyle) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { // alt alta
        Label("Görsel boyutu") // başlık
        Segmented( // oran seçimi
            options = AspectRatio.entries.map { it to it.label.substringBefore(" ") }, // Kare / Dikey / Hikaye
            selected = style.aspect, // seçili
        ) { a -> onStyle { it.copy(aspect = a) } } // değişince
        Text( // seçilen oranın piksel ölçüsü
            "${style.aspect.label} · ${style.aspect.width}×${style.aspect.height} piksel", // örn. Kare 1:1 · 1080×1080
            style = MaterialTheme.typography.bodyMedium, // gövde yazısı
            color = MaterialTheme.colorScheme.onSurfaceVariant, // soluk
            modifier = Modifier.padding(horizontal = SIDE), // kenar boşluğu
        )
    }
}

// PNG / JPEG seçimi
@Composable
fun FormatToggle(selected: ExportFormat, onSelect: (ExportFormat) -> Unit) {
    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) { // tek seçimli satır
        ExportFormat.entries.forEachIndexed { i, f -> // her biçim
            SegmentedButton( // tek parça
                selected = f == selected, // seçili mi
                onClick = { onSelect(f) }, // dokununca seç
                shape = SegmentedButtonDefaults.itemShape(i, ExportFormat.entries.size), // kenar parçalarında yuvarlak köşe
            ) { Text(f.label) } // etiket
        }
    }
}

// Yazı + aç/kapa düğmesi satırı
@Composable
private fun ToggleRow(text: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable { onChange(!checked) }.padding(horizontal = SIDE, vertical = 4.dp), // tüm satır tıklanabilir
        verticalAlignment = Alignment.CenterVertically, // dikey ortalı
    ) {
        Text(text, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge) // etiket
        Switch(checked = checked, onCheckedChange = onChange) // düğme
    }
}

// Genel parçalı seçim satırı
@Composable
private fun <T> Segmented(options: List<Pair<T, String>>, selected: T, onSelect: (T) -> Unit) {
    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth().padding(horizontal = SIDE)) { // tek seçimli satır
        options.forEachIndexed { i, (value, label) -> // her seçenek
            SegmentedButton( // tek parça
                selected = value == selected, // seçili mi
                onClick = { onSelect(value) }, // dokununca seç
                shape = SegmentedButtonDefaults.itemShape(i, options.size), // kenar parçalarında yuvarlak köşe
            ) { Text(label, fontWeight = if (value == selected) FontWeight.Bold else FontWeight.Normal) } // etiket
        }
    }
}
