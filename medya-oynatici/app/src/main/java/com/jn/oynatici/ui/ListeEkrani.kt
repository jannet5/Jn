package com.jn.oynatici.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCut
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DriveFileMove
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jn.oynatici.data.Kutuphane
import com.jn.oynatici.data.Tur
import com.jn.oynatici.indir.Durum
import com.jn.oynatici.indir.IndirmeMerkezi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

// Klasör içeriği: alt klasörler (kaç dosya içerdiğiyle) ve dosyalar
private data class Icerik(val klasorler: List<Pair<File, Int>>, val dosyalar: List<File>)

// Bir öğe üzerinde yapılabilecek işlemler
private enum class Islem { AD, TASI, SIL }

// Müzik ya da Video bölümündeki bir klasörün ekranı
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListeEkrani(model: UygulamaModeli, tur: Tur, klasor: File) {
    val ctx = LocalContext.current // dosya işlemleri için
    val kapsam = rememberCoroutineScope() // arka plan işleri için
    val kok = remember(tur) { Kutuphane.kok(ctx, tur) } // bölümün ana klasörü
    var yenile by remember { mutableIntStateOf(0) } // artınca liste yeniden okunur
    val biten by IndirmeMerkezi.biten.collectAsState() // indirme bitince de yenile
    val icerik by produceState<Icerik?>(null, klasor, yenile, biten) { // klasörü arka planda oku
        value = withContext(Dispatchers.IO) {
            val (k, d) = Kutuphane.listele(tur, klasor) // klasörler ve dosyalar
            Icerik(k.map { it to Kutuphane.dosyaSayisi(tur, it) }, d) // klasörlerin dosya sayısıyla
        }
    }
    val durum by calmaDurumu(model.oynatici) // mini oynatıcı için

    var mesgul by remember { mutableStateOf<String?>(null) } // "Ekleniyor 3/20" gibi bekleme yazısı
    var ekleMenusu by remember { mutableStateOf(false) } // ekleme menüsü açık mı
    var yeniKlasorPenceresi by remember { mutableStateOf(false) } // yeni klasör penceresi
    var indirPenceresi by remember { mutableStateOf<String?>(null) } // indirme penceresi (içindeki hazır link)
    var secili by remember { mutableStateOf<Pair<File, Islem>?>(null) } // işlem yapılacak öğe

    // Başka yerden (paylaşım) link geldiyse indirme penceresini hazır aç
    LaunchedEffect(model.bekleyenLink) {
        model.bekleyenLink?.let { indirPenceresi = it; model.bekleyenLink = null } // linki pencereye aktar
    }

    // Ekleme işlerini ortak şekilde yürüt: bekleme yazısı göster, bitince sonucu söyle
    fun ekle(is_: (ilerleme: (Int, Int) -> Unit) -> Int) {
        kapsam.launch {
            mesgul = "Ekleniyor…" // bekleme yazısı
            val sayi = withContext(Dispatchers.IO) { is_ { i, n -> mesgul = "Ekleniyor ${i + 1}/$n" } } // kopyala
            mesgul = null // bekleme bitti
            yenile++ // listeyi yenile
            Toast.makeText(ctx, if (sayi > 0) "$sayi dosya eklendi" else "Uygun dosya bulunamadı", Toast.LENGTH_SHORT).show() // sonuç
        }
    }
    // Telefondan tek tek dosya seçici
    val dosyaSec = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uriler ->
        if (uriler.isNotEmpty()) ekle { Kutuphane.dosyalariEkle(ctx, tur, uriler, klasor, it) } // seçilenleri kopyala
    }
    // Telefondan klasör seçici
    val klasorSec = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) ekle { Kutuphane.klasoruEkle(ctx, tur, uri, klasor, it) } // klasörü olduğu gibi kopyala
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (klasor == kok) tur.baslik else klasor.name, maxLines = 1, overflow = TextOverflow.Ellipsis) }, // başlık
                navigationIcon = { IconButton(onClick = { model.geri() }) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Geri") } }, // geri
            )
        },
        floatingActionButton = { // sağ altta büyük "Ekle" butonu
            ExtendedFloatingActionButton(onClick = { ekleMenusu = true }, icon = { Icon(Icons.Rounded.Add, null) }, text = { Text("Ekle") })
        },
        bottomBar = { if (durum.varMi && !durum.video) MiniOynatici(model, durum) }, // müzik çalıyorsa mini oynatıcı
    ) { bosluk ->
        Column(Modifier.fillMaxSize().padding(bosluk)) {
            IndirmeKartlari(tur) // devam eden indirmeler
            val ic = icerik // okunan içerik
            when {
                ic == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() } // yükleniyor
                ic.klasorler.isEmpty() && ic.dosyalar.isEmpty() -> BosDurum(tur) // boş klasör
                else -> LazyColumn(contentPadding = PaddingValues(bottom = 96.dp)) { // liste (alttaki buton üstünü kapatmasın)
                    items(ic.klasorler, key = { it.first.path }) { (k, sayi) -> // klasör satırları
                        OgeSatiri(
                            ad = k.name, alt = "$sayi dosya", ikon = { OgeIkonu(Icons.Rounded.Folder, true) }, // klasör görünümü
                            tikla = { model.git(Sayfa.Liste(tur, k)) }, // klasörü aç
                            kirp = null, // klasör kırpılamaz
                            islem = { secili = k to it }, // işlem seçildi
                        )
                    }
                    items(ic.dosyalar, key = { it.path }) { d -> // dosya satırları
                        OgeSatiri(
                            ad = d.nameWithoutExtension, alt = "${d.extension.uppercase()} · ${Kutuphane.boyutYazi(d.length())}", // ad, tür ve boyut
                            ikon = { OgeIkonu(if (tur == Tur.MUZIK) Icons.Rounded.MusicNote else Icons.Rounded.Movie, false) }, // dosya ikonu
                            tikla = { model.cal(tur, ic.dosyalar, ic.dosyalar.indexOf(d)) }, // çal (klasördeki diğerleri sırada)
                            kirp = { model.git(Sayfa.Kirp(tur, d)) }, // kırpma ekranı
                            islem = { secili = d to it }, // işlem seçildi
                        )
                    }
                }
            }
        }
    }

    // Ekleme menüsü
    if (ekleMenusu) EkleMenusu(
        tur = tur,
        kapat = { ekleMenusu = false }, // menüyü kapat
        dosya = { dosyaSec.launch(arrayOf(if (tur == Tur.MUZIK) "audio/*" else "video/*")) }, // dosya seç
        klasor = { klasorSec.launch(null) }, // klasör seç
        yeniKlasor = { yeniKlasorPenceresi = true }, // yeni klasör
        youtube = { indirPenceresi = "" }, // YouTube penceresi
    )
    // Yeni klasör adı penceresi
    if (yeniKlasorPenceresi) AdPenceresi("Yeni klasör", "", { yeniKlasorPenceresi = false }) { ad ->
        Kutuphane.yeniKlasor(klasor, ad); yenile++ // klasörü oluştur, listeyi yenile
    }
    // YouTube indirme penceresi
    indirPenceresi?.let { hazirLink ->
        IndirPenceresi(tur, hazirLink, { indirPenceresi = null }) { link, kalite ->
            IndirmeMerkezi.ekle(ctx, link, tur, klasor, kalite) // indirmeyi başlat
            Toast.makeText(ctx, "İndirme başladı", Toast.LENGTH_SHORT).show() // bilgi ver
        }
    }
    // Seçili öğe üzerindeki işlem pencereleri
    secili?.let { (oge, islem) ->
        val kapat = { secili = null } // pencereyi kapat
        when (islem) {
            Islem.AD -> AdPenceresi("Yeniden adlandır", if (oge.isFile) oge.nameWithoutExtension else oge.name, kapat) { ad ->
                if (!Kutuphane.yenidenAdlandir(oge, ad)) Toast.makeText(ctx, "Ad değiştirilemedi", Toast.LENGTH_SHORT).show() // hata
                yenile++ // listeyi yenile
            }
            Islem.SIL -> SilPenceresi(oge, kapat) { Kutuphane.sil(oge); yenile++ } // sil
            Islem.TASI -> TasiPenceresi(tur, kok, oge, kapat) { hedef ->
                if (!Kutuphane.tasi(oge, hedef)) Toast.makeText(ctx, "Taşınamadı", Toast.LENGTH_SHORT).show() // hata
                yenile++ // listeyi yenile
            }
        }
    }
    // Kopyalama sürerken bekleme penceresi
    mesgul?.let { yazi ->
        AlertDialog(onDismissRequest = {}, confirmButton = {}, text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(Modifier.size(28.dp)) // dönen çember
                Spacer(Modifier.width(16.dp)) // boşluk
                Text(yazi) // "Ekleniyor 3/20"
            }
        })
    }
}

// Liste satırındaki yuvarlak ikon kutusu
@Composable
private fun OgeIkonu(ikon: androidx.compose.ui.graphics.vector.ImageVector, klasor: Boolean) {
    Surface(
        shape = RoundedCornerShape(12.dp), // yuvarlak köşe
        color = if (klasor) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant, // klasörler vurgulu
    ) {
        Icon(ikon, null, Modifier.padding(10.dp).size(24.dp), tint = if (klasor) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// Tek bir klasör/dosya satırı: dokun = aç/çal, uzun bas veya üç nokta = işlemler
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OgeSatiri(ad: String, alt: String, ikon: @Composable () -> Unit, tikla: () -> Unit, kirp: (() -> Unit)?, islem: (Islem) -> Unit) {
    var menu by remember { mutableStateOf(false) } // işlem menüsü açık mı
    ListItem(
        headlineContent = { Text(ad, maxLines = 2, overflow = TextOverflow.Ellipsis) }, // ad
        supportingContent = { Text(alt) }, // alt bilgi
        leadingContent = ikon, // sol ikon
        trailingContent = {
            Box {
                IconButton(onClick = { menu = true }) { Icon(Icons.Rounded.MoreVert, "İşlemler") } // üç nokta
                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) { // işlem menüsü
                    kirp?.let { k -> DropdownMenuItem(text = { Text("Kırp") }, leadingIcon = { Icon(Icons.Rounded.ContentCut, null) }, onClick = { menu = false; k() }) } // kırp
                    DropdownMenuItem(text = { Text("Yeniden adlandır") }, leadingIcon = { Icon(Icons.Rounded.Edit, null) }, onClick = { menu = false; islem(Islem.AD) }) // ad değiştir
                    DropdownMenuItem(text = { Text("Taşı") }, leadingIcon = { Icon(Icons.Rounded.DriveFileMove, null) }, onClick = { menu = false; islem(Islem.TASI) }) // taşı
                    DropdownMenuItem(text = { Text("Sil") }, leadingIcon = { Icon(Icons.Rounded.Delete, null) }, onClick = { menu = false; islem(Islem.SIL) }) // sil
                }
            }
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background), // arka planla aynı
        modifier = Modifier.combinedClickable(onClick = tikla, onLongClick = { menu = true }), // dokun / uzun bas
    )
}

// Klasör boşken gösterilen yönlendirme
@Composable
private fun BosDurum(tur: Tur) {
    Column(
        Modifier.fillMaxSize().padding(32.dp), // ortada, kenarlardan boşluklu
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(if (tur == Tur.MUZIK) Icons.Rounded.MusicNote else Icons.Rounded.Movie, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant) // büyük ikon
        Spacer(Modifier.height(16.dp)) // boşluk
        Text("Burası boş", style = MaterialTheme.typography.titleLarge) // başlık
        Spacer(Modifier.height(8.dp)) // boşluk
        Text( // açıklama
            "Sağ alttaki \"Ekle\" ile telefonundan dosya ya da klasör ekleyebilir, YouTube'dan indirebilir veya yeni klasör açabilirsin.",
            textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// Bu bölümün devam eden / biten indirmeleri
@Composable
private fun IndirmeKartlari(tur: Tur) {
    val isler by IndirmeMerkezi.isler.collectAsState() // tüm işler
    isler.filter { it.tur == tur }.forEach { isi -> // sadece bu bölümünkiler
        Surface(
            shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant, // kart görünümü
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        ) {
            Row(Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text( // başlık: video adı ya da tür
                        isi.baslik.ifEmpty { if (isi.liste) "Oynatma listesi" else "YouTube" },
                        maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleSmall,
                    )
                    Text( // durum yazısı (hatalıysa kırmızı)
                        isi.bilgi, style = MaterialTheme.typography.bodySmall, maxLines = 2,
                        color = if (isi.durum == Durum.HATA) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (isi.durum == Durum.INIYOR || isi.durum == Durum.BEKLIYOR) { // devam ediyorsa ilerleme çubuğu
                        Spacer(Modifier.height(8.dp)) // boşluk
                        if (isi.yuzde > 0f) LinearProgressIndicator(progress = { isi.yuzde / 100f }, Modifier.fillMaxWidth()) // yüzdeli
                        else LinearProgressIndicator(Modifier.fillMaxWidth()) // belirsiz
                    }
                }
                IconButton(onClick = { // devam ediyorsa iptal, bittiyse kartı kapat
                    if (isi.durum == Durum.INIYOR || isi.durum == Durum.BEKLIYOR) IndirmeMerkezi.iptal(isi.id) else IndirmeMerkezi.kaldir(isi.id)
                }) { Icon(Icons.Rounded.Close, "Kapat") }
            }
        }
    }
}
