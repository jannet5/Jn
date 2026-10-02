package com.jn.oynatici.ui

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.material.icons.rounded.CreateNewFolder
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.jn.oynatici.data.Tur
import com.jn.oynatici.indir.IndirmeMerkezi
import java.io.File

// "Ekle" butonuna basınca alttan açılan menü
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EkleMenusu(tur: Tur, kapat: () -> Unit, dosya: () -> Unit, klasor: () -> Unit, yeniKlasor: () -> Unit, youtube: () -> Unit) {
    val ad = if (tur == Tur.MUZIK) "ses" else "video" // yazılarda kullanılacak kelime
    ModalBottomSheet(onDismissRequest = kapat) { // alttan açılan pencere
        Column(Modifier.navigationBarsPadding().padding(bottom = 16.dp)) {
            MenuSatiri(Icons.Rounded.UploadFile, "Dosya ekle", "Telefonundan $ad dosyaları seç") { kapat(); dosya() } // dosya
            MenuSatiri(Icons.Rounded.FolderOpen, "Klasör ekle", "Bütün klasörü olduğu gibi ekle") { kapat(); klasor() } // klasör
            MenuSatiri(Icons.Rounded.CreateNewFolder, "Yeni klasör", "Kendi klasörünü oluştur") { kapat(); yeniKlasor() } // yeni klasör
            MenuSatiri( // YouTube
                Icons.Rounded.Download, "YouTube'dan indir",
                if (tur == Tur.MUZIK) "Linki yapıştır, en kaliteli MP3 insin" else "Linki yapıştır, kaliteyi seç",
            ) { kapat(); youtube() }
        }
    }
}

// Menüdeki tek satır
@Composable
private fun MenuSatiri(ikon: ImageVector, baslik: String, alt: String, tikla: () -> Unit) {
    ListItem(
        headlineContent = { Text(baslik) }, // başlık
        supportingContent = { Text(alt) }, // açıklama
        leadingContent = { Icon(ikon, null, tint = MaterialTheme.colorScheme.primary) }, // ikon
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), // menü rengiyle aynı
        modifier = Modifier.dokun(tikla), // dokununca
    )
}

// Ad yazma penceresi (yeni klasör / yeniden adlandır)
@Composable
fun AdPenceresi(baslik: String, ilkAd: String, kapat: () -> Unit, tamam: (String) -> Unit) {
    var metin by remember { mutableStateOf(TextFieldValue(ilkAd, TextRange(0, ilkAd.length))) } // yazı (tamamı seçili gelir)
    val odak = remember { FocusRequester() } // klavye direkt açılsın
    LaunchedEffect(Unit) { odak.requestFocus() } // pencere açılınca odaklan
    AlertDialog(
        onDismissRequest = kapat, // dışına dokununca kapat
        title = { Text(baslik) }, // başlık
        text = {
            OutlinedTextField(
                value = metin, onValueChange = { metin = it }, singleLine = true, // tek satır
                label = { Text("Ad") }, modifier = Modifier.fillMaxWidth().focusRequester(odak),
            )
        },
        confirmButton = {
            TextButton(enabled = metin.text.isNotBlank(), onClick = { tamam(metin.text.trim()); kapat() }) { Text("Kaydet") } // kaydet
        },
        dismissButton = { TextButton(onClick = kapat) { Text("Vazgeç") } }, // vazgeç
    )
}

// Silme onayı
@Composable
fun SilPenceresi(oge: File, kapat: () -> Unit, sil: () -> Unit) {
    AlertDialog(
        onDismissRequest = kapat, // dışına dokununca kapat
        title = { Text("Silinsin mi?") }, // soru
        text = { Text(if (oge.isDirectory) "\"${oge.name}\" klasörü içindekilerle birlikte silinecek." else "\"${oge.name}\" silinecek.") }, // açıklama
        confirmButton = { TextButton(onClick = { sil(); kapat() }) { Text("Sil", color = MaterialTheme.colorScheme.error) } }, // sil
        dismissButton = { TextButton(onClick = kapat) { Text("Vazgeç") } }, // vazgeç
    )
}

// Taşıma: hedef klasörü seç
@Composable
fun TasiPenceresi(tur: Tur, kok: File, oge: File, kapat: () -> Unit, tasi: (File) -> Unit) {
    val hedefler = remember(oge) { // taşınabilecek klasörler (kendisi ve kendi içi hariç)
        com.jn.oynatici.data.Kutuphane.tumKlasorler(tur, kok).filter { k ->
            k != oge.parentFile && !(oge.isDirectory && k.canonicalPath.startsWith(oge.canonicalPath)) // anlamsız hedefleri çıkar
        }
    }
    AlertDialog(
        onDismissRequest = kapat, // dışına dokununca kapat
        title = { Text("Nereye taşınsın?") }, // soru
        text = {
            if (hedefler.isEmpty()) Text("Taşınacak başka klasör yok. Önce \"Yeni klasör\" ile bir klasör oluştur.") // klasör yoksa
            else LazyColumn(Modifier.heightIn(max = 360.dp)) { // klasör listesi
                items(hedefler) { k ->
                    val yol = if (k == kok) tur.baslik else tur.baslik + " / " + k.relativeTo(kok).path.replace("/", " / ") // "Müzik / Gitar"
                    ListItem(
                        headlineContent = { Text(yol) }, // klasör yolu
                        leadingContent = { Icon(Icons.Rounded.Folder, null) }, // klasör ikonu
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh), // pencere rengi
                        modifier = Modifier.dokun { tasi(k); kapat() }, // dokununca taşı
                    )
                }
            }
        },
        confirmButton = {}, // onay butonu yok, listeden seçilir
        dismissButton = { TextButton(onClick = kapat) { Text("Vazgeç") } }, // vazgeç
    )
}

// Video kalite seçenekleri (null = en yüksek)
private val kaliteler = listOf<Pair<String, Int?>>("144p" to 144, "240p" to 240, "360p" to 360, "480p" to 480, "720p" to 720, "1080p" to 1080, "En yüksek" to null)

// YouTube indirme penceresi
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IndirPenceresi(tur: Tur, hazirLink: String, kapat: () -> Unit, indir: (String, Int?) -> Unit) {
    val ctx = LocalContext.current // pano erişimi için
    var link by remember { mutableStateOf(hazirLink) } // yazılan link
    var kalite by remember { mutableStateOf<Int?>(720) } // seçilen kalite (varsayılan 720p)
    // Panoda YouTube linki varsa kendiliğinden yapıştır (kullanıcı uğraşmasın)
    LaunchedEffect(Unit) { if (link.isBlank()) panoLinki(ctx)?.let { link = it } }
    val gecerli = link.trim().startsWith("http") // link gibi görünüyor mu
    val liste = gecerli && IndirmeMerkezi.listeMi(link) // oynatma listesi mi

    AlertDialog(
        onDismissRequest = kapat, // dışına dokununca kapat
        title = { Text("YouTube'dan indir") }, // başlık
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField( // link kutusu
                    value = link, onValueChange = { link = it }, singleLine = true,
                    label = { Text("Video ya da oynatma listesi linki") },
                    trailingIcon = { IconButton(onClick = { panoLinki(ctx, herhangi = true)?.let { link = it } }) { Icon(Icons.Rounded.ContentPaste, "Yapıştır") } }, // yapıştır
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                )
                if (liste) Text( // oynatma listesi bilgisi
                    "Oynatma listesi: hepsi, listenin adıyla açılan bir klasöre inecek.",
                    color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium,
                )
                if (tur == Tur.MUZIK) { // müzikte kalite sorulmaz
                    Text("En yüksek ses kalitesinde MP3 olarak iner.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                } else { // videoda kalite seçilir
                    Text("Kalite", style = MaterialTheme.typography.titleSmall) // başlık
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) { // kalite çipleri
                        kaliteler.forEach { (ad, deger) ->
                            FilterChip(selected = kalite == deger, onClick = { kalite = deger }, label = { Text(ad) }) // tek çip
                        }
                    }
                    Spacer(Modifier.height(0.dp)) // düzen
                }
            }
        },
        confirmButton = {
            TextButton(enabled = gecerli, onClick = { indir(link.trim(), if (tur == Tur.VIDEO) kalite else null); kapat() }) { Text("İndir") } // indir
        },
        dismissButton = { TextButton(onClick = kapat) { Text("Vazgeç") } }, // vazgeç
    )
}

// Panodaki linki oku. herhangi=false ise sadece YouTube linklerini alır
private fun panoLinki(ctx: Context, herhangi: Boolean = false): String? {
    val pano = ctx.getSystemService(ClipboardManager::class.java) // pano servisi
    val metin = pano.primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(ctx)?.toString() ?: return null // panodaki yazı
    val link = Regex("""https?://\S+""").find(metin)?.value ?: return if (herhangi) metin.trim() else null // içindeki link
    return if (herhangi || link.contains("youtu")) link else null // YouTube linki mi
}
