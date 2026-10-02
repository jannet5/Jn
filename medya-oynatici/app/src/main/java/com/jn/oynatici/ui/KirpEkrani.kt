package com.jn.oynatici.ui

import android.net.Uri
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ContentCut
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.jn.oynatici.data.Tur
import com.jn.oynatici.data.Zaman
import com.jn.oynatici.kirp.Kirpici
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

// Kırpma ekranı: iki tutamaçla aralık seç ya da süreleri elle yaz (örn. 1.28 - 2.16)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KirpEkrani(model: UygulamaModeli, tur: Tur, dosya: File) {
    val ctx = LocalContext.current // bağlam
    val kapsam = rememberCoroutineScope() // kırpma işi için
    // Önizleme için ayrı küçük oynatıcı (çalan müziği bozmaz)
    val onizleme = remember(dosya) {
        ExoPlayer.Builder(ctx).build().apply { setMediaItem(MediaItem.fromUri(Uri.fromFile(dosya))); prepare() } // dosyayı yükle
    }
    DisposableEffect(onizleme) {
        model.oynatici?.pause() // arka planda çalan müziği duraklat
        onDispose { onizleme.release() } // sayfadan çıkınca oynatıcıyı kapat
    }

    var sure by remember { mutableLongStateOf(0L) } // dosyanın süresi (ms)
    var bas by remember { mutableFloatStateOf(0f) } // başlangıç (ms)
    var bit by remember { mutableFloatStateOf(0f) } // bitiş (ms)
    var basYazi by remember { mutableStateOf("0:00") } // başlangıç kutusundaki yazı
    var bitYazi by remember { mutableStateOf("0:00") } // bitiş kutusundaki yazı
    var konum by remember { mutableLongStateOf(0L) } // önizlemenin şu anki yeri
    var caliyor by remember { mutableStateOf(false) } // önizleme çalıyor mu
    var ilerleme by remember { mutableStateOf<Int?>(null) } // kırpma sürüyorsa yüzde

    // Süre öğrenilince aralığı tüm dosya yap; önizlemeyi takip et
    LaunchedEffect(onizleme) {
        while (true) {
            val d = onizleme.duration // süre
            if (sure == 0L && d > 0) { sure = d; bit = d.toFloat(); bitYazi = Zaman.yaz(d) } // ilk kez öğrenildi
            konum = onizleme.currentPosition // şu anki yer
            caliyor = onizleme.isPlaying // çalıyor mu
            if (caliyor && konum >= bit.toLong()) onizleme.pause() // seçilen aralığın sonunda dur
            delay(100) // sık kontrol
        }
    }

    // İki kutudaki yazıyı birlikte oku; ikisi de geçerliyse (baş < bit <= süre) çubuğa uygula
    fun yazilariUygula(): Pair<Long, Long>? {
        val b = Zaman.oku(basYazi) ?: return null // başlangıç okunamadı
        val e = (Zaman.oku(bitYazi) ?: return null).coerceAtMost(sure) // bitiş (süreyi geçemez; yuvarlama payı)
        if (b >= e || Zaman.oku(bitYazi)!! > sure + 999) return null // sıra ya da süre hatalı
        bas = b.toFloat(); bit = e.toFloat() // çubuğu güncelle
        return b to e // uygulanan değerler
    }
    val basOku = Zaman.oku(basYazi) // yazılan başlangıç
    val bitOku = Zaman.oku(bitYazi) // yazılan bitiş
    val basHata = basOku == null || (bitOku != null && basOku >= bitOku) // başlangıç geçersiz ya da bitişten sonra
    val bitHata = bitOku == null || bitOku > sure + 999 || (basOku != null && bitOku <= basOku) // bitiş geçersiz, süreyi aşıyor ya da baştan önce
    val gecerli = sure > 0 && bit - bas >= 1000 && !basHata && !bitHata // en az 1 saniyelik geçerli aralık

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kırp") }, // başlık
                navigationIcon = { IconButton(onClick = { model.geri() }) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Geri") } }, // geri
            )
        },
    ) { bosluk ->
        Column(
            Modifier.fillMaxSize().padding(bosluk).imePadding().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp), // kaydırılabilir sayfa
            verticalArrangement = Arrangement.spacedBy(16.dp), // bölümler arası boşluk
        ) {
            Text(dosya.nameWithoutExtension, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis) // dosya adı
            // Önizleme alanı: videoysa görüntü, sesse büyük nota
            Surface(shape = RoundedCornerShape(16.dp), color = Color.Black, modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)) {
                if (tur == Tur.VIDEO) AndroidView(
                    factory = { c ->
                        PlayerView(c).apply {
                            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) // tam boyut
                            useController = false // kendi butonlarımızı kullanıyoruz
                            player = onizleme // önizleme oynatıcısı
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                ) else Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.MusicNote, null, Modifier.size(72.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer) // nota
                }
            }
            // Önizleme çal/duraklat + şu anki yer
            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledTonalButton(onClick = {
                    if (caliyor) onizleme.pause() // çalıyorsa duraklat
                    else { if (konum < bas.toLong() || konum >= bit.toLong() - 200) onizleme.seekTo(bas.toLong()); onizleme.play() } // aralığın başından çal
                }) {
                    Icon(if (caliyor) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, null) // ikon
                    Spacer(Modifier.size(8.dp)) // boşluk
                    Text(if (caliyor) "Duraklat" else "Seçimi dinle") // yazı
                }
                Spacer(Modifier.weight(1f)) // boşluk
                Text("Şu an: ${Zaman.yaz(konum)}", color = MaterialTheme.colorScheme.onSurfaceVariant) // şu anki yer
            }
            // İki tutamaçlı aralık çubuğu
            if (sure > 0) RangeSlider(
                value = bas..bit, // seçili aralık
                onValueChange = { r -> // tutamaç sürüklenince
                    if (r.start != bas) onizleme.seekTo(r.start.toLong()) else if (r.endInclusive != bit) onizleme.seekTo(r.endInclusive.toLong()) // o kareyi göster
                    bas = r.start; bit = r.endInclusive // değerleri güncelle
                    basYazi = Zaman.yaz(bas.toLong()); bitYazi = Zaman.yaz(bit.toLong()) // kutulara yaz
                },
                valueRange = 0f..sure.toFloat(), // 0 - süre
            ) else LinearProgressIndicator(Modifier.fillMaxWidth()) // süre okunurken
            // Süreleri elle yazma kutuları
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ZamanKutusu("Başlangıç", basYazi, basHata, Modifier.weight(1f), { y ->
                    basYazi = y // yazıyı güncelle
                    yazilariUygula()?.let { onizleme.seekTo(it.first) } // ikisi de geçerliyse çubuğu taşı, o kareyi göster
                }) { bas = konum.toFloat().coerceAtMost(bit - 1000).coerceAtLeast(0f); basYazi = Zaman.yaz(bas.toLong()) } // "şu an"
                ZamanKutusu("Bitiş", bitYazi, bitHata, Modifier.weight(1f), { y ->
                    bitYazi = y // yazıyı güncelle
                    yazilariUygula()?.let { onizleme.seekTo(it.second) } // ikisi de geçerliyse çubuğu taşı, o kareyi göster
                }) { bit = konum.toFloat().coerceAtLeast(bas + 1000).coerceAtMost(sure.toFloat()); bitYazi = Zaman.yaz(bit.toLong()) } // "şu an"
            }
            Text( // bilgi
                "Seçilen: ${Zaman.yaz((bit - bas).toLong())}  ·  Orijinal dosya silinmez, kırpılan kısım yeni dosya olarak kaydedilir.",
                color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium,
            )
            Button( // tek ana buton
                enabled = gecerli && ilerleme == null,
                onClick = {
                    onizleme.pause() // önizlemeyi durdur
                    ilerleme = 0 // kırpma başladı
                    kapsam.launch {
                        try {
                            val yeni = Kirpici.kirp(ctx, tur, dosya, bas.toLong(), bit.toLong()) { ilerleme = it } // kırp
                            Toast.makeText(ctx, "Kaydedildi: ${yeni.name}", Toast.LENGTH_LONG).show() // başarı
                            model.geri() // listeye dön
                        } catch (e: Exception) {
                            Toast.makeText(ctx, "Kırpılamadı: ${e.message?.take(120)}", Toast.LENGTH_LONG).show() // hata
                        } finally {
                            ilerleme = null // bitti
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {
                Icon(Icons.Rounded.ContentCut, null) // makas ikonu
                Spacer(Modifier.size(8.dp)) // boşluk
                Text("Kırp ve yeni dosya olarak kaydet", fontWeight = FontWeight.SemiBold) // yazı
            }
            Spacer(Modifier.height(16.dp)) // alt boşluk
        }
    }

    // Kırpma sürerken bekleme penceresi
    ilerleme?.let { yuzde ->
        AlertDialog(onDismissRequest = {}, confirmButton = {}, title = { Text("Kırpılıyor…") }, text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (yuzde > 0) LinearProgressIndicator(progress = { yuzde / 100f }, Modifier.fillMaxWidth()) // yüzdeli
                else CircularProgressIndicator() // belirsiz
                if (yuzde > 0) Text("%$yuzde") // yüzde yazısı
            }
        })
    }
}

// Süre yazma kutusu + "Şu anki yer" butonu
@Composable
private fun ZamanKutusu(etiket: String, yazi: String, hata: Boolean, modifier: Modifier, degisti: (String) -> Unit, simdi: () -> Unit) {
    Column(modifier) {
        OutlinedTextField(
            value = yazi, onValueChange = degisti, singleLine = true, isError = hata, // yazı kutusu
            label = { Text(etiket) }, // "Başlangıç" / "Bitiş"
            supportingText = { Text(if (hata) "Örn. 1.28 ya da 1:28" else "dk.sn") }, // ipucu
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // rakam klavyesi
            modifier = Modifier.fillMaxWidth(),
        )
        TextButton(onClick = simdi) { Text("Şu anki yer") } // önizlemenin olduğu yeri al
    }
}
