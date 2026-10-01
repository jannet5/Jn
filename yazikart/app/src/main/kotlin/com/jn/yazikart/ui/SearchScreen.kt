package com.jn.yazikart.ui // arayüz paketi

import androidx.activity.compose.BackHandler // geri tuşunu yakalamak için
import androidx.compose.foundation.background // arka plan boyama
import androidx.compose.foundation.clickable // tıklanabilirlik
import androidx.compose.foundation.layout.Arrangement // dizilim aralığı
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.foundation.layout.Column // alt alta yerleşim
import androidx.compose.foundation.layout.PaddingValues // iç boşluk değerleri
import androidx.compose.foundation.layout.Row // yan yana yerleşim
import androidx.compose.foundation.layout.Spacer // boşluk
import androidx.compose.foundation.layout.aspectRatio // en/boy oranı
import androidx.compose.foundation.layout.fillMaxSize // tüm alan
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.imePadding // klavye boşluğu
import androidx.compose.foundation.layout.padding // dış boşluk
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.layout.statusBarsPadding // durum çubuğu boşluğu
import androidx.compose.foundation.lazy.grid.GridCells // ızgara sütunları
import androidx.compose.foundation.lazy.grid.GridItemSpan // tam satır kaplayan öğe
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid // kaydırılabilir ızgara
import androidx.compose.foundation.lazy.grid.items // ızgaraya öğe ekleme
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.text.KeyboardActions // klavye eylemleri
import androidx.compose.foundation.text.KeyboardOptions // klavye ayarları
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.automirrored.filled.ArrowBack // geri oku
import androidx.compose.material.icons.filled.Search // büyüteç
import androidx.compose.material.icons.filled.WifiOff // internet yok ikonu
import androidx.compose.material3.Button // buton
import androidx.compose.material3.CircularProgressIndicator // dönen yükleniyor göstergesi
import androidx.compose.material3.Icon // ikon
import androidx.compose.material3.IconButton // ikon buton
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.OutlinedTextField // yazı kutusu
import androidx.compose.material3.Surface // yüzey
import androidx.compose.material3.Text // yazı
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.LaunchedEffect // açılınca bir kez çalışan iş
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.focus.FocusRequester // klavyeyi otomatik açmak için
import androidx.compose.ui.focus.focusRequester // odak bağlayıcı
import androidx.compose.ui.layout.ContentScale // resim ölçekleme
import androidx.compose.ui.text.input.ImeAction // klavyedeki "Ara" tuşu
import androidx.compose.ui.text.style.TextAlign // yazı hizası
import androidx.compose.ui.unit.dp // ölçü birimi
import coil.compose.AsyncImage // internetten resim gösterme

// İnternetten görsel arama ekranı: kelimeyi yaz, sonuçlardan birine dokun, arka plan olsun
@Composable
fun SearchScreen(
    state: SearchState, // arama durumu
    online: Boolean, // internet var mı
    onQuery: (String) -> Unit, // kelime değişti
    onSearch: () -> Unit, // ara
    onPick: (com.jn.yazikart.search.ImageResult) -> Unit, // görsel seçildi
    onClose: () -> Unit, // ekranı kapat
) {
    BackHandler(onBack = onClose) // telefonun geri tuşu ekranı kapatır
    val focus = remember { FocusRequester() } // yazı kutusu odağı
    LaunchedEffect(Unit) { if (state.results.isEmpty()) focus.requestFocus() } // açılınca klavye hazır

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { // tam ekran yüzey
        Column(Modifier.fillMaxSize().statusBarsPadding().imePadding()) { // alt alta; klavye için boşluk
            Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) { // üst çubuk
                IconButton(onClick = onClose) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri") } // geri
                OutlinedTextField( // arama kutusu
                    value = state.query, // yazılan kelime
                    onValueChange = onQuery, // kelime değişince
                    modifier = Modifier.weight(1f).focusRequester(focus), // kalan genişlik
                    placeholder = { Text("Görsel ara (örn. lahmacun)") }, // ipucu
                    singleLine = true, // tek satır
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }, // büyüteç
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // klavyede "Ara" tuşu
                    keyboardActions = KeyboardActions(onSearch = { onSearch() }), // "Ara"ya basınca ara
                    shape = RoundedCornerShape(12.dp), // yuvarlak köşe
                )
            }

            if (!online) { // internet yoksa uyarı şeridi
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp) // kenar boşluğu
                        .clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(12.dp), // kutu
                    verticalAlignment = Alignment.CenterVertically, // dikey ortalı
                ) {
                    Icon(Icons.Default.WifiOff, contentDescription = null, tint = MaterialTheme.colorScheme.error) // ikon
                    Spacer(Modifier.size(8.dp)) // boşluk
                    Text("İnternet yok. Görsel aramak için Wi-Fi ya da mobil veriyi aç.", style = MaterialTheme.typography.bodyMedium) // mesaj
                }
            }

            Box(Modifier.weight(1f).fillMaxWidth()) { // içerik alanı
                when { // duruma göre içerik
                    state.loading -> CenterBox { CircularProgressIndicator() } // yükleniyor
                    state.error != null -> CenterBox { // hata
                        Text(state.error, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) // hata mesajı
                        Spacer(Modifier.height(16.dp)) // boşluk
                        Button(onClick = onSearch, enabled = online) { Text("Tekrar dene") } // tekrar dene
                    }
                    state.results.isEmpty() && state.searched -> CenterBox { // sonuç yok
                        Text("Sonuç bulunamadı. Başka bir kelime dene (İngilizcesi daha çok sonuç verebilir).", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) // mesaj
                    }
                    state.results.isEmpty() -> CenterBox { // henüz aranmadı
                        Text("Ne arıyorsun? Yaz ve klavyeden Ara'ya bas.\nÖrn: lahmacun, deniz, gün batımı, kahve", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant) // ipucu
                    }
                    else -> LazyVerticalGrid( // sonuç ızgarası
                        columns = GridCells.Fixed(3), // 3 sütun
                        contentPadding = PaddingValues(16.dp), // kenar boşlukları
                        horizontalArrangement = Arrangement.spacedBy(8.dp), // yatay aralık
                        verticalArrangement = Arrangement.spacedBy(8.dp), // dikey aralık
                    ) {
                        items(state.results, key = { it.fullUrl }) { r -> // her sonuç
                            AsyncImage( // internetten küçük resim
                                model = r.thumbUrl, // küçük resim adresi
                                contentDescription = r.title, // erişilebilirlik için ad
                                contentScale = ContentScale.Crop, // kareye kırpılarak
                                modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(8.dp)) // kare, yuvarlak köşe
                                    .background(MaterialTheme.colorScheme.surfaceVariant) // yüklenirken gri zemin
                                    .clickable { onPick(r) }, // dokununca seç
                            )
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) { // en altta tam satır not
                            Text(
                                "Görseller Openverse ve Wikimedia Commons'tan gelir. Lisanslar görsele göre değişir; ticari kullanımda kaynağı kontrol et.", // kaynak notu
                                style = MaterialTheme.typography.bodySmall, // küçük yazı
                                color = MaterialTheme.colorScheme.onSurfaceVariant, // soluk renk
                                modifier = Modifier.padding(top = 8.dp), // üst boşluk
                            )
                        }
                    }
                }
            }
        }
    }
}

// İçeriği ortalayan yardımcı kutu
@Composable
private fun CenterBox(content: @Composable () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(32.dp), // tüm alan, geniş kenar boşluğu
        verticalArrangement = Arrangement.Center, // dikey ortalı
        horizontalAlignment = Alignment.CenterHorizontally, // yatay ortalı
    ) { content() } // içerik
}
