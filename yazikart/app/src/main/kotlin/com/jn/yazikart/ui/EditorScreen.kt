package com.jn.yazikart.ui // arayüz paketi

import android.graphics.Bitmap // arka plan resmi
import androidx.compose.foundation.background // arka plan boyama
import androidx.compose.foundation.border // çerçeve
import androidx.compose.foundation.clickable // dokunma
import androidx.compose.foundation.layout.Arrangement // dizilim aralığı
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.foundation.layout.BoxWithConstraints // eldeki alanı ölçen kutu
import androidx.compose.foundation.layout.Column // alt alta yerleşim
import androidx.compose.foundation.layout.Row // yan yana yerleşim
import androidx.compose.foundation.layout.Spacer // boşluk
import androidx.compose.foundation.layout.fillMaxSize // tüm alan
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.navigationBarsPadding // alt çubuk boşluğu
import androidx.compose.foundation.layout.padding // dış boşluk
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.layout.statusBarsPadding // durum çubuğu boşluğu
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.foundation.rememberScrollState // kaydırma durumu
import androidx.compose.foundation.shape.CircleShape // daire
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.verticalScroll // dikey kaydırma
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.filled.Download // indir ikonu
import androidx.compose.material.icons.filled.Edit // düzenle ikonu
import androidx.compose.material.icons.filled.Fullscreen // tam ekran ikonu
import androidx.compose.material.icons.filled.Refresh // yenile (baştan başla)
import androidx.compose.material.icons.filled.Share // paylaş ikonu
import androidx.compose.material3.AlertDialog // onay penceresi
import androidx.compose.material3.Button // birincil buton
import androidx.compose.material3.CircularProgressIndicator // yükleniyor göstergesi
import androidx.compose.material3.Icon // ikon
import androidx.compose.material3.IconButton // ikon buton
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.OutlinedButton // ikincil buton
import androidx.compose.material3.Scaffold // ekran iskeleti
import androidx.compose.material3.ScrollableTabRow // kaydırılabilir sekmeler
import androidx.compose.material3.SnackbarHost // alt mesaj alanı
import androidx.compose.material3.SnackbarHostState // alt mesaj durumu
import androidx.compose.material3.Surface // yüzey
import androidx.compose.material3.Tab // sekme
import androidx.compose.material3.Text // yazı
import androidx.compose.material3.TextButton // yazı buton
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.LaunchedEffect // değişince çalışan iş
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableIntStateOf // sayı durumu
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.rememberCoroutineScope // iş kapsamı
import androidx.compose.runtime.saveable.rememberSaveable // ekran dönünce de hatırlama
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.platform.LocalConfiguration // ekran ölçüleri
import androidx.compose.ui.platform.LocalFocusManager // odak yöneticisi
import androidx.compose.ui.unit.dp // ölçü birimi
import androidx.compose.ui.unit.min // iki ölçünün küçüğü
import kotlinx.coroutines.launch // iş başlatma
import com.jn.yazikart.data.PostStyle // görsel ayarları

// Ana ekran: üstte SABİT canlı önizleme (ayarları değiştirirken hep görünür), altta kaydırılabilir ayarlar, en altta Kaydet/Paylaş.
// Yazı için önizlemeye dokunulur; klavye hemen açılır.
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
    onReset: () -> Unit, // baştan başla
    onFullscreen: () -> Unit, // tam ekran
    video: VideoUi, // sıra (video/fotoğraf), müzik ve oynatıcı
) {
    val snackbar = remember { SnackbarHostState() } // alt mesaj durumu
    LaunchedEffect(message) { // yeni mesaj gelince
        if (message != null) { snackbar.showSnackbar(message); onMessageShown() } // gösterilip temizleniyor
    }
    var tab by rememberSaveable { mutableIntStateOf(0) } // seçili sekme
    val scroll = rememberScrollState() // ayar alanının kaydırma durumu
    val scope = rememberCoroutineScope() // baştan başlayınca en üste kaydırmak için
    val focus = LocalFocusManager.current // klavyeyi kapatmak için
    var confirmReset by remember { mutableStateOf(false) } // "baştan başla" onay penceresi açık mı
    var editingText by rememberSaveable { mutableStateOf(false) } // yazı düzenleme penceresi açık mı

    if (confirmReset) { // onay penceresi
        AlertDialog(
            onDismissRequest = { confirmReset = false }, // dışına basınca kapanır
            title = { Text("Baştan başla?") }, // başlık
            text = { Text("Yazı, arka plan, sıradaki fotoğraf ve videolar, müzik ve tüm ayarlar silinip uygulama ilk açıldığı haline döner. Kaydettiğin dosyalar galeride kalır.") }, // açıklama
            confirmButton = {
                TextButton(onClick = { // onaylanınca
                    confirmReset = false // pencere kapanıyor
                    focus.clearFocus() // klavye kapanıyor
                    onReset() // her şey sıfırlanıyor
                    tab = 0 // ilk sekmeye dönülüyor
                    scope.launch { scroll.animateScrollTo(0) } // ayarlar en üste kayıyor
                }) { Text("Baştan başla") } // onay butonu
            },
            dismissButton = { TextButton(onClick = { confirmReset = false }) { Text("Vazgeç") } }, // iptal
        )
    }

    if (editingText) { // yazı düzenleme: klavye hemen açılır, önizleme arkada anında değişir
        TextEditSheet(
            text = style.text, // güncel yazı
            onText = { t -> onStyle { it.copy(text = t) } }, // her harfte stil güncellenir
            onDismiss = { editingText = false }, // kapanınca
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) }, // mesaj alanı
        containerColor = MaterialTheme.colorScheme.background, // arka plan
        bottomBar = { ActionBar(onSave, onShare, busy != null) }, // alt eylem çubuğu
    ) { inner ->
        Column(Modifier.fillMaxSize().padding(inner).statusBarsPadding()) { // sabit üst kısım + kaydırılabilir ayarlar
            Row( // üst çubuk: solda ad, sağda "baştan başla"
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp), // kenar boşlukları
                verticalAlignment = Alignment.CenterVertically, // dikey ortalı
            ) {
                Text("YazıKart", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f)) // başlık
                IconButton(onClick = { confirmReset = true }) { Icon(Icons.Default.Refresh, contentDescription = "Baştan başla") } // yenile
            }
            Preview( // SABİT önizleme: dokunulunca yazı düzenlenir, sağ üstte tam ekran
                style = style,
                image = image,
                busy = busy,
                onFullscreen = onFullscreen,
                onEditText = { editingText = true },
                video = video,
            )
            TextButton(onClick = { editingText = true }, modifier = Modifier.align(Alignment.CenterHorizontally)) { // yazıyı düzenleme düğmesi
                Icon(Icons.Default.Edit, contentDescription = null) // kalem
                Spacer(Modifier.width(6.dp)) // boşluk
                Text("Yazıyı düzenle") // etiket
            }
            ScrollableTabRow( // sekmeler (sabit)
                selectedTabIndex = tab,
                containerColor = MaterialTheme.colorScheme.background,
                edgePadding = 16.dp,
            ) {
                listOf("Arka plan", "Sıra ve müzik", "Yazı tipi", "Yazı", "Boyut").forEachIndexed { i, t -> // beş sekme
                    Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t) }) // tek sekme
                }
            }
            Column( // yalnızca bu alan kayar; önizleme yerinde kalır
                Modifier.weight(1f).fillMaxWidth().verticalScroll(scroll).padding(top = 12.dp, bottom = 24.dp),
            ) {
                when (tab) { // seçili sekmenin paneli
                    0 -> BackgroundPanel(style, image != null, online, onStyle, onOpenSearch, onPickGallery, onClearImage, video.hasVideo) // arka plan
                    1 -> SequencePanel(video, online) // sıra ve müzik
                    2 -> FontPanel(style, onStyle) // yazı tipi
                    3 -> TextPanel(style, onStyle) // yazı ayarları
                    else -> SizePanel(style, onStyle, video.hasVideo) // görsel boyutu
                }
            }
        }
    }
}

// Canlı önizleme: kaydedilecek görselin birebir küçültülmüş hali. Dokununca yazı düzenlenir, sağ üstte tam ekran.
@Composable
private fun Preview(
    style: PostStyle,
    image: Bitmap?,
    busy: String?,
    onFullscreen: () -> Unit,
    onEditText: () -> Unit,
    video: VideoUi,
) {
    BoxWithConstraints(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), contentAlignment = Alignment.Center) { // ortalı alan
        val ratio = style.aspect.width.toFloat() / style.aspect.height // en/boy oranı
        val maxH = (LocalConfiguration.current.screenHeightDp * 0.36f).dp // önizleme ekran yüksekliğinin en çok %36'sı (ayarlar altta kalsın)
        val w = min(maxWidth, maxH * ratio) // genişlik: alan ya da yükseklik sınırı hangisi küçükse
        Box(
            Modifier.width(w).height(w / ratio) // oranlı kutu
                .clip(RoundedCornerShape(12.dp)) // yuvarlak köşe
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)) // ince çerçeve
                .clickable(onClick = onEditText), // dokununca yazı düzenlenir (klavye açılır)
            contentAlignment = Alignment.Center, // gösterge ortada
        ) {
            PostSurface(style, image, video, Modifier.fillMaxSize()) // görsel ya da sıra + yazı
            FullscreenButton(onFullscreen, Modifier.align(Alignment.TopEnd).padding(4.dp)) // sağ üst köşede tam ekran düğmesi
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

// Önizlemenin köşesindeki tam ekran düğmesi: her zeminde görünsün diye yarı saydam koyu daire içinde
@Composable
private fun FullscreenButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) { // 48dp dokunma alanı
        Box(
            Modifier.size(36.dp).clip(CircleShape).background(Color.Black.copy(alpha = 0.55f)), // koyu yarı saydam daire
            contentAlignment = Alignment.Center, // ikon ortada
        ) {
            Icon(Icons.Default.Fullscreen, contentDescription = "Tam ekran", tint = Color.White) // tam ekran ikonu
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
