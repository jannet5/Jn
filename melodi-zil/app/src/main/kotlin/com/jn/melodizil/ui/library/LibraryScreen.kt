package com.jn.melodizil.ui.library // Kütüphane ekranı

import androidx.compose.foundation.layout.Column // Sütun
import androidx.compose.foundation.layout.Spacer // Boşluk
import androidx.compose.foundation.layout.height // Yükseklik
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.shape.RoundedCornerShape // Köşe
import androidx.compose.material.icons.Icons // İkonlar
import androidx.compose.material.icons.rounded.Alarm // Alarm
import androidx.compose.material.icons.rounded.Call // Zil
import androidx.compose.material.icons.rounded.Delete // Sil
import androidx.compose.material.icons.rounded.LibraryMusic // Boş durum
import androidx.compose.material.icons.rounded.Notifications // Bildirim
import androidx.compose.material.icons.rounded.Share // Paylaş
import androidx.compose.material3.AlertDialog // Dialog
import androidx.compose.material3.ExperimentalMaterial3Api // Deneysel
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.material3.ModalBottomSheet // Sheet
import androidx.compose.material3.Text // Metin
import androidx.compose.material3.TextButton // Metin butonu
import androidx.compose.runtime.Composable // Composable
import androidx.compose.runtime.getValue // Delegasyon
import androidx.compose.runtime.mutableStateOf // Durum
import androidx.compose.runtime.remember // Hatırla
import androidx.compose.runtime.setValue // Delegasyon
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.res.stringResource // Metin
import com.jn.melodizil.R // Kaynaklar
import com.jn.melodizil.core.Instrument // Enstrüman
import com.jn.melodizil.data.store.RingtoneKind // Tür
import com.jn.melodizil.data.store.SavedRingtone // Kayıt
import com.jn.melodizil.ui.AppViewModel // Yardımcılar
import com.jn.melodizil.ui.components.AppCard // Kart
import com.jn.melodizil.ui.components.EmptyState // Boş
import com.jn.melodizil.ui.components.ListRow // Satır
import com.jn.melodizil.ui.components.Screen // Ekran
import com.jn.melodizil.ui.components.icon // İkon
import com.jn.melodizil.ui.theme.Radius // Köşe
import com.jn.melodizil.ui.theme.Space // Boşluk
import java.text.DateFormat // Tarih
import java.util.Date // Tarih

/**
 * Zillerim: kayıtlı zillerin listesi. Referans: Zedge "İndirilenler", Google Dosyalar liste görünümü.
 * Durumlar: dolu (liste), boş (EmptyState). Yükleniyor/hata yok: veri yerel ve anlık.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    items: List<SavedRingtone>, // Kayıtlar
    initialSelected: SavedRingtone? = null, // Dışarıdan açılan kayıt
    onGoHome: () -> Unit, // Boş durumda ana sayfaya
    onSetDefault: (SavedRingtone, RingtoneKind) -> Unit, // Varsayılan yap
    onShare: (SavedRingtone) -> Unit, // Paylaş
    onDelete: (SavedRingtone) -> Unit, // Sil
) {
    var selected by remember { mutableStateOf(initialSelected) } // Seçili kayıt (sheet)
    var confirmDelete by remember { mutableStateOf<SavedRingtone?>(null) } // Silme onayı
    Screen { // Ekran
        Spacer(Modifier.height(Space.xl)) // Üst boşluk
        Text(stringResource(R.string.library_title), style = MaterialTheme.typography.displaySmall) // Başlık
        Spacer(Modifier.height(Space.xl)) // Boşluk
        if (items.isEmpty()) EmptyState(Icons.Rounded.LibraryMusic, stringResource(R.string.library_empty_title), stringResource(R.string.library_empty_desc), actionText = stringResource(R.string.library_empty_action), onAction = onGoHome) // Boş
        else AppCard { // Liste
            Column { // Satırlar
                items.forEachIndexed { i, item -> // Her kayıt
                    val inst = Instrument.fromId(item.instrumentId) // Enstrüman
                    val kind = runCatching { RingtoneKind.valueOf(item.kind) }.getOrDefault(RingtoneKind.RINGTONE) // Tür
                    ListRow(item.title, "${AppViewModel.instrumentName(inst)} · ${AppViewModel.kindLabel(kind)} · ${DateFormat.getDateInstance(DateFormat.SHORT).format(Date(item.createdAtMillis))}", inst.icon(), onClick = { selected = item }, showDivider = i < items.size - 1) // Satır
                }
            }
        }
        Spacer(Modifier.height(Space.xxl)) // Alt boşluk
    }

    selected?.let { item -> // Eylem sheet'i
        ModalBottomSheet(onDismissRequest = { selected = null }, shape = RoundedCornerShape(topStart = Radius.lg, topEnd = Radius.lg)) { // Sheet
            Column(Modifier.padding(horizontal = Space.lg).padding(bottom = Space.xxl)) { // İçerik
                Text(item.title, style = MaterialTheme.typography.titleMedium) // Başlık
                Spacer(Modifier.height(Space.md)) // Boşluk
                ListRow(stringResource(R.string.action_set_ringtone), icon = Icons.Rounded.Call, onClick = { onSetDefault(item, RingtoneKind.RINGTONE); selected = null }, showChevron = false) // Zil
                ListRow(stringResource(R.string.action_set_notification), icon = Icons.Rounded.Notifications, onClick = { onSetDefault(item, RingtoneKind.NOTIFICATION); selected = null }, showChevron = false) // Bildirim
                ListRow(stringResource(R.string.action_set_alarm), icon = Icons.Rounded.Alarm, onClick = { onSetDefault(item, RingtoneKind.ALARM); selected = null }, showChevron = false) // Alarm
                ListRow(stringResource(R.string.share), icon = Icons.Rounded.Share, onClick = { onShare(item); selected = null }, showChevron = false) // Paylaş
                ListRow(stringResource(R.string.delete), icon = Icons.Rounded.Delete, onClick = { confirmDelete = item; selected = null }, showChevron = false, showDivider = false) // Sil
            }
        }
    }

    confirmDelete?.let { item -> // Silme onayı (geri alınamaz işlem)
        AlertDialog(onDismissRequest = { confirmDelete = null }, title = { Text(stringResource(R.string.delete_title)) }, text = { Text(stringResource(R.string.delete_text, item.title)) }, // Dialog
            confirmButton = { TextButton(onClick = { onDelete(item); confirmDelete = null }) { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) } }, // Sil
            dismissButton = { TextButton(onClick = { confirmDelete = null }) { Text(stringResource(R.string.cancel)) } }) // Vazgeç
    }
}
