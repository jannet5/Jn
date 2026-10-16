package com.jn.melodizil.ui.home // Ana ekran

import android.net.Uri // Uri
import androidx.activity.compose.rememberLauncherForActivityResult // Dosya seçici
import androidx.activity.result.contract.ActivityResultContracts // Sözleşmeler
import androidx.compose.foundation.layout.Column // Sütun
import androidx.compose.foundation.layout.Spacer // Boşluk
import androidx.compose.foundation.layout.fillMaxWidth // Genişlik
import androidx.compose.foundation.layout.height // Yükseklik
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.layout.size // Boyut
import androidx.compose.foundation.shape.RoundedCornerShape // Köşe
import androidx.compose.foundation.text.KeyboardActions // Klavye eylemi
import androidx.compose.foundation.text.KeyboardOptions // Klavye seçenekleri
import androidx.compose.material.icons.Icons // İkonlar
import androidx.compose.material.icons.rounded.ContentPaste // Yapıştır
import androidx.compose.material.icons.rounded.FolderOpen // Dosya
import androidx.compose.material.icons.rounded.Link // Bağlantı
import androidx.compose.material3.Icon // İkon
import androidx.compose.material3.IconButton // İkon butonu
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.material3.OutlinedTextField // Input
import androidx.compose.material3.OutlinedTextFieldDefaults // Input renkleri
import androidx.compose.material3.Text // Metin
import androidx.compose.runtime.Composable // Composable
import androidx.compose.runtime.LaunchedEffect // Etki
import androidx.compose.runtime.getValue // Delegasyon
import androidx.compose.runtime.mutableStateOf // Durum
import androidx.compose.runtime.remember // Hatırla
import androidx.compose.runtime.setValue // Delegasyon
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.platform.LocalClipboardManager // Pano
import androidx.compose.ui.res.stringResource // Metin
import androidx.compose.ui.text.input.ImeAction // IME
import androidx.compose.ui.text.input.KeyboardType // Klavye türü
import com.jn.melodizil.R // Kaynaklar
import com.jn.melodizil.core.Instrument // Enstrüman
import com.jn.melodizil.core.YouTubeUrl // Bağlantı kontrolü
import com.jn.melodizil.data.store.SavedRingtone // Kayıt
import com.jn.melodizil.ui.AppViewModel // VM yardımcıları
import com.jn.melodizil.ui.components.AppCard // Kart
import com.jn.melodizil.ui.components.ListRow // Satır
import com.jn.melodizil.ui.components.PrimaryButton // Birincil
import com.jn.melodizil.ui.components.Screen // Ekran
import com.jn.melodizil.ui.components.SecondaryButton // İkincil
import com.jn.melodizil.ui.components.SectionTitle // Başlık
import com.jn.melodizil.ui.components.icon // Enstrüman ikonu
import com.jn.melodizil.ui.theme.Radius // Köşe
import com.jn.melodizil.ui.theme.Size // Boyut
import com.jn.melodizil.ui.theme.Space // Boşluk

/**
 * Ana ekran. Referans: Shazam ana ekranı (tek büyük eylem), Ringtone Maker (InShot) ana ekranı (kaynak seçimi).
 * Tek birincil eylem: "Melodiye çevir".
 */
@Composable
fun HomeScreen(
    initialLink: String?, // Paylaşımla gelen bağlantı
    recent: List<SavedRingtone>, // Son ziller
    onConvert: (String) -> Unit, // Bağlantı gönder
    onPickFile: (Uri) -> Unit, // Dosya seçildi
    onOpenLibrary: () -> Unit, // Tümünü gör
    onOpenSaved: (SavedRingtone) -> Unit, // Kayıt aç
    ensureStorage: (then: () -> Unit) -> Unit = { it() }, // Android 8-9: işlem başlamadan depolama izni iste (D-011)
) {
    var link by remember { mutableStateOf(initialLink ?: "") } // Input metni
    var touched by remember { mutableStateOf(false) } // Hata göstermek için
    val clipboard = LocalClipboardManager.current // Pano
    val valid = YouTubeUrl.isYouTube(link) // Geçerli mi
    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> if (uri != null) onPickFile(uri) } // Dosya seçici
    LaunchedEffect(initialLink) { if (!initialLink.isNullOrBlank()) link = initialLink } // Paylaşım güncellenince

    Screen { // Ekran
        Spacer(Modifier.height(Space.xl)) // Üst boşluk
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onSurface) // Büyük başlık
        Text(stringResource(R.string.home_subtitle), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) // Alt başlık
        Spacer(Modifier.height(Space.xl)) // Bölüm boşluğu
        OutlinedTextField( // Bağlantı girişi
            value = link, onValueChange = { link = it; touched = true }, // Değer
            modifier = Modifier.fillMaxWidth(), singleLine = true, // Tek satır
            shape = RoundedCornerShape(Radius.sm), // Köşe
            label = { Text(stringResource(R.string.home_link_label)) }, // Etiket
            placeholder = { Text(stringResource(R.string.home_link_placeholder)) }, // Yer tutucu
            leadingIcon = { Icon(Icons.Rounded.Link, contentDescription = null, modifier = Modifier.size(Size.iconSm)) }, // Sol ikon
            trailingIcon = { IconButton(onClick = { clipboard.getText()?.text?.let { link = it; touched = true } }) { Icon(Icons.Rounded.ContentPaste, contentDescription = stringResource(R.string.home_paste), modifier = Modifier.size(Size.iconSm)) } }, // Yapıştır
            isError = touched && link.isNotBlank() && !valid, // Hata
            supportingText = { Text(if (touched && link.isNotBlank() && !valid) stringResource(R.string.home_link_invalid) else stringResource(R.string.home_link_hint)) }, // Yardım/hata metni
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Go), // Klavye
            keyboardActions = KeyboardActions(onGo = { if (valid) ensureStorage { onConvert(link) } }), // Enter
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline), // Renkler
        )
        Spacer(Modifier.height(Space.lg)) // Boşluk
        PrimaryButton(stringResource(R.string.home_convert), onClick = { ensureStorage { onConvert(link) } }, enabled = valid) // Tek birincil eylem
        Spacer(Modifier.height(Space.md)) // Boşluk
        SecondaryButton(stringResource(R.string.home_pick_file), onClick = { ensureStorage { filePicker.launch(arrayOf("audio/*")) } }, icon = Icons.Rounded.FolderOpen) // Dosyadan (önce izin, sonra seçici)
        Spacer(Modifier.height(Space.xxl)) // Bölüm boşluğu
        SectionTitle(stringResource(R.string.home_recent)) // Son ziller
        if (recent.isEmpty()) { // Boş durum (kompakt)
            AppCard { Text(stringResource(R.string.home_recent_empty), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) } // Açıklama kartı
        } else {
            AppCard { // Liste kartı
                Column { // Satırlar
                    recent.take(3).forEachIndexed { i, item -> // En fazla 3
                        val inst = Instrument.fromId(item.instrumentId) // Enstrüman
                        ListRow(item.title, AppViewModel.instrumentName(inst), inst.icon(), onClick = { onOpenSaved(item) }, showDivider = i < minOf(recent.size, 3) - 1) // Satır
                    }
                }
                if (recent.size > 3) { Spacer(Modifier.height(Space.sm)); SecondaryButton(stringResource(R.string.home_see_all), onOpenLibrary) } // Tümü
            }
        }
        Spacer(Modifier.height(Space.xl).padding(bottom = Space.lg)) // Alt boşluk
    }
}
