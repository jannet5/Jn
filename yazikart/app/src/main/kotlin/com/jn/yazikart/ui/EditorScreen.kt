package com.jn.yazikart.ui // arayüz paketi

import android.graphics.Bitmap // arka plan resmi
import androidx.compose.foundation.border // çerçeve
import androidx.compose.foundation.layout.Arrangement // dizilim aralığı
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.foundation.layout.BoxWithConstraints // eldeki alanı ölçen kutu
import androidx.compose.foundation.layout.Column // alt alta yerleşim
import androidx.compose.foundation.layout.Row // yan yana yerleşim
import androidx.compose.foundation.layout.Spacer // boşluk
import androidx.compose.foundation.layout.fillMaxSize // tüm alan
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.imePadding // klavye boşluğu
import androidx.compose.foundation.layout.navigationBarsPadding // alt çubuk boşluğu
import androidx.compose.foundation.layout.padding // dış boşluk
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.layout.statusBarsPadding // durum çubuğu boşluğu
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.foundation.rememberScrollState // kaydırma durumu
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.verticalScroll // dikey kaydırma
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.filled.Download // indir ikonu
import androidx.compose.material.icons.filled.Share // paylaş ikonu
import androidx.compose.material3.Button // birincil buton
import androidx.compose.material3.CircularProgressIndicator // yükleniyor göstergesi
import androidx.compose.material3.Icon // ikon
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.OutlinedButton // ikincil buton
import androidx.compose.material3.OutlinedTextField // yazı kutusu
import androidx.compose.material3.Scaffold // ekran iskeleti
import androidx.compose.material3.SnackbarHost // alt mesaj alanı
import androidx.compose.material3.SnackbarHostState // alt mesaj durumu
import androidx.compose.material3.Surface // yüzey
import androidx.compose.material3.Tab // sekme
import androidx.compose.material3.TabRow // sekme çubuğu
import androidx.compose.material3.Text // yazı
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.LaunchedEffect // durum değişince çalışan iş
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableIntStateOf // sayı durumu
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.saveable.rememberSaveable // ekran dönünce de hatırlama
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas // Android tuvaline erişim
import androidx.compose.ui.graphics.nativeCanvas // Android tuvali
import androidx.compose.ui.platform.LocalConfiguration // ekran ölçüleri
import androidx.compose.ui.platform.LocalContext // uygulama bağlamı
import androidx.compose.ui.unit.dp // ölçü birimi
import androidx.compose.ui.unit.min // iki ölçünün küçüğü
import com.jn.yazikart.data.FontCatalog // yazı tipleri
import com.jn.yazikart.data.PostStyle // görsel ayarları
import com.jn.yazikart.render.PostRenderer // görsel çizici

// Ana ekran: üstte canlı önizleme, ortada yazı kutusu, altta ayar sekmeleri, en altta Kaydet/Paylaş
@Composable
fun EditorScreen(
    style: PostStyle, // güncel stil
    image: Bitmap?, // arka plan resmi
    online: Boolean, // internet var mı
    busy: String?, // süren iş açıklaması
    message: String?, // gösterilecek mesaj
    onMessageShown: () -> Unit, // mesaj gösterildi
    onStyle: ((PostStyle) -> PostStyle) -> Unit, // stil değiştir
    onOpenSearch: () -> Unit, // internetten görsel ara
    onPickGallery: () -> Unit, // galeriden seç
    onClearImage: () -> Unit, // resmi kaldır
    onSave: () -> Unit, // galeriye kaydet
    onShare: () -> Unit, // paylaş
) {
    val snackbar = remember { SnackbarHostState() } // alt mesaj durumu
    LaunchedEffect(message) { // yeni mesaj gelince
        if (message != null) { snackbar.showSnackbar(message); onMessageShown() } // gösterilip temizleniyor
    }
    var tab by rememberSaveable { mutableIntStateOf(0) } // seçili sekme

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) }, // mesaj alanı
        containerColor = MaterialTheme.colorScheme.background, // arka plan
        bottomBar = { ActionBar(onSave, onShare, busy != null) }, // alt eylem çubuğu
    ) { inner ->
        Column(
            Modifier.fillMaxSize().padding(inner).statusBarsPadding() // iskelet boşlukları
                .imePadding().verticalScroll(rememberScrollState()), // klavye açılınca kaydırılabilir
        ) {
            Text( // başlık
                "YazıKart", // uygulama adı
                style = MaterialTheme.typography.titleLarge, // başlık boyutu
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), // kenar boşluğu
            )
            Preview(style, image, busy) // canlı önizleme
            Spacer(Modifier.height(16.dp)) // boşluk
            OutlinedTextField( // yazının yazıldığı kutu
                value = style.text, // güncel yazı
                onValueChange = { t -> onStyle { it.copy(text = t) } }, // yazı değişince stil güncelleniyor
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), // tam genişlik
                placeholder = { Text("Yazını buraya yaz…") }, // ipucu
                minLines = 2, // en az 2 satır yüksekliği
                maxLines = 6, // en çok 6 satır görünür, fazlası kayar
                shape = RoundedCornerShape(12.dp), // yuvarlak köşe
            )
            Spacer(Modifier.height(16.dp)) // boşluk
            TabRow(selectedTabIndex = tab, containerColor = MaterialTheme.colorScheme.background) { // sekme çubuğu
                listOf("Zemin", "Yazı tipi", "Yazı", "Boyut").forEachIndexed { i, t -> // dört sekme
                    Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t) }) // tek sekme
                }
            }
            Spacer(Modifier.height(16.dp)) // boşluk
            when (tab) { // seçili sekmenin paneli
                0 -> BackgroundPanel(style, image != null, online, onStyle, onOpenSearch, onPickGallery, onClearImage) // zemin
                1 -> FontPanel(style, onStyle) // yazı tipi
                2 -> TextPanel(style, onStyle) // yazı ayarları
                else -> SizePanel(style, onStyle) // görsel boyutu
            }
            Spacer(Modifier.height(24.dp)) // alt boşluk
        }
    }
}

// Canlı önizleme: kaydedilecek görselin birebir küçültülmüş hali
@Composable
private fun Preview(style: PostStyle, image: Bitmap?, busy: String?) {
    val context = LocalContext.current // uygulama bağlamı
    val typeface = remember(style.fontIndex, style.bold) { // yazı tipi değişince yeniden yükleniyor
        FontCatalog.typeface(context.assets, style.fontIndex, style.bold) // seçili yazı tipi
    }
    val fakeBold = FontCatalog.needsFakeBold(style.fontIndex, style.bold) // yapay kalınlık gerekir mi
    BoxWithConstraints(Modifier.fillMaxWidth().padding(horizontal = 16.dp), contentAlignment = Alignment.Center) { // ortalı alan
        val ratio = style.aspect.width.toFloat() / style.aspect.height // en/boy oranı
        val maxH = (LocalConfiguration.current.screenHeightDp * 0.42f).dp // önizleme ekran yüksekliğinin en çok %42'si (yazı kutusu görünür kalsın)
        val w = min(maxWidth, maxH * ratio) // genişlik: alan ya da yükseklik sınırı hangisi küçükse
        Box(
            Modifier.width(w).height(w / ratio) // oranlı kutu
                .clip(RoundedCornerShape(12.dp)) // yuvarlak köşe
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)), // ince çerçeve (siyah zeminde sınır görünsün)
            contentAlignment = Alignment.Center, // gösterge ortada
        ) {
            androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) { // çizim alanı
                drawIntoCanvas { c -> // Android tuvaline geçiliyor
                    PostRenderer.draw( // kayıtla aynı çizici
                        c.nativeCanvas, size.width.toInt(), size.height.toInt(), // tuval ve boyut
                        style, typeface, fakeBold, image, // ayarlar
                        placeholder = "Yazın burada görünecek", // boşken ipucu
                    )
                }
            }
            if (busy != null) { // iş sürüyorsa
                Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)) { // yarı saydam kutu
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { // yan yana
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp) // dönen gösterge
                        Spacer(Modifier.width(12.dp)) // boşluk
                        Text(busy) // açıklama
                    }
                }
            }
        }
    }
}

// Alt eylem çubuğu: Kaydet + Paylaş (PNG/JPEG seçimi "Boyut" sekmesinde)
@Composable
private fun ActionBar(
    onSave: () -> Unit, // kaydet
    onShare: () -> Unit, // paylaş
    busy: Boolean, // iş sürüyor mu
) {
    Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) { // alt panel yüzeyi
        Column(Modifier.navigationBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp)) { // kenar boşluklu alan
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { // yan yana iki buton
                OutlinedButton(onClick = onSave, enabled = !busy, modifier = Modifier.weight(1f).height(48.dp)) { // kaydet
                    Icon(Icons.Default.Download, contentDescription = null) // indir ikonu
                    Spacer(Modifier.width(8.dp)) // boşluk
                    Text("Kaydet") // etiket
                }
                Button(onClick = onShare, enabled = !busy, modifier = Modifier.weight(1f).height(48.dp)) { // paylaş (birincil eylem)
                    Icon(Icons.Default.Share, contentDescription = null) // paylaş ikonu
                    Spacer(Modifier.width(8.dp)) // boşluk
                    Text("Paylaş") // etiket
                }
            }
        }
    }
}
