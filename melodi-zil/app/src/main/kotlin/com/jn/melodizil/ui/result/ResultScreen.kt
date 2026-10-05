package com.jn.melodizil.ui.result // Sonuç ekranı

import androidx.compose.foundation.layout.Arrangement // Dizilim
import androidx.compose.foundation.layout.Column // Sütun
import androidx.compose.foundation.layout.ExperimentalLayoutApi // FlowRow
import androidx.compose.foundation.layout.FlowRow // Akan satır
import androidx.compose.foundation.layout.Row // Satır
import androidx.compose.foundation.layout.Spacer // Boşluk
import androidx.compose.foundation.layout.fillMaxWidth // Genişlik
import androidx.compose.foundation.layout.height // Yükseklik
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.layout.size // Boyut
import androidx.compose.foundation.layout.width // Genişlik
import androidx.compose.foundation.shape.RoundedCornerShape // Köşe
import androidx.compose.material.icons.Icons // İkonlar
import androidx.compose.material.icons.automirrored.rounded.ArrowBack // Geri
import androidx.compose.material.icons.rounded.Alarm // Alarm
import androidx.compose.material.icons.rounded.AutoAwesome // Otomatik
import androidx.compose.material.icons.rounded.Call // Zil
import androidx.compose.material.icons.rounded.MusicNote // Kapak yoksa
import androidx.compose.material.icons.rounded.Notifications // Bildirim
import androidx.compose.material.icons.rounded.Pause // Durdur
import androidx.compose.material.icons.rounded.PlayArrow // Oynat
import androidx.compose.material.icons.rounded.Share // Paylaş
import androidx.compose.material3.AlertDialog // Dialog
import androidx.compose.material3.ExperimentalMaterial3Api // Deneysel
import androidx.compose.material3.FilterChip // Chip
import androidx.compose.material3.FilterChipDefaults // Chip renkleri
import androidx.compose.material3.Icon // İkon
import androidx.compose.material3.IconButton // İkon butonu
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.material3.ModalBottomSheet // Sheet
import androidx.compose.material3.rememberModalBottomSheetState // Sheet durumu
import androidx.compose.foundation.rememberScrollState // Kaydırma
import androidx.compose.foundation.verticalScroll // Kaydırma
import androidx.compose.foundation.layout.navigationBarsPadding // Gezinme çubuğu payı
import androidx.compose.material3.SegmentedButton // Bölümlü buton
import androidx.compose.material3.SegmentedButtonDefaults // Varsayılan
import androidx.compose.material3.SingleChoiceSegmentedButtonRow // Satır
import androidx.compose.material3.Slider // Kaydırıcı
import androidx.compose.material3.Switch // Anahtar
import androidx.compose.material3.Text // Metin
import androidx.compose.material3.TextButton // Metin butonu
import androidx.compose.runtime.Composable // Composable
import androidx.compose.runtime.getValue // Delegasyon
import androidx.compose.runtime.mutableStateOf // Durum
import androidx.compose.runtime.remember // Hatırla
import androidx.compose.runtime.setValue // Delegasyon
import androidx.compose.ui.Alignment // Hizalama
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.draw.clip // Kırp
import androidx.compose.ui.res.pluralStringResource // Çoğul metin
import androidx.compose.ui.res.stringResource // Metin
import androidx.compose.ui.text.style.TextOverflow // Taşma
import androidx.compose.ui.unit.dp // dp
import coil.compose.AsyncImage // Görsel
import com.jn.melodizil.R // Kaynaklar
import com.jn.melodizil.core.Instrument // Enstrüman
import com.jn.melodizil.data.store.RingtoneKind // Tür
import com.jn.melodizil.ui.AppViewModel // Yardımcılar
import com.jn.melodizil.ui.ResultState // Durum
import com.jn.melodizil.ui.components.AppCard // Kart
import com.jn.melodizil.ui.components.Caption // Açıklama
import com.jn.melodizil.ui.components.ListRow // Satır
import com.jn.melodizil.ui.components.PianoRoll // Piyano rulosu
import com.jn.melodizil.ui.components.PrimaryButton // Birincil
import com.jn.melodizil.ui.components.Screen // Ekran
import com.jn.melodizil.ui.components.SecondaryButton // İkincil
import com.jn.melodizil.ui.components.SectionTitle // Başlık
import com.jn.melodizil.ui.components.SkeletonBox // Skeleton
import com.jn.melodizil.ui.components.description // Açıklama
import com.jn.melodizil.ui.components.icon // İkon
import com.jn.melodizil.ui.theme.Radius // Köşe
import com.jn.melodizil.ui.theme.Size // Boyut
import com.jn.melodizil.ui.theme.Space // Boşluk

/**
 * Sonuç ekranı: parça başlığı, piyano rulosu + oynat, 8 tını chip'i, uzunluk, başlangıç kaydırıcısı, "Zil sesi yap".
 * Referans: Ringtone Maker (InShot) kesme ekranı (dalga formu + aralık), GarageBand enstrüman seçimi (chip'ler).
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    state: ResultState, // Durum
    isPlaying: Boolean, // Oynatılıyor mu
    positionSec: Float, // Oynatma konumu
    onBack: () -> Unit, // Geri
    onInstrument: (Instrument) -> Unit, // Tını seçimi
    onLength: (Int) -> Unit, // Uzunluk
    onStartChange: (Float) -> Unit, // Kaydırma
    onStartCommit: () -> Unit, // Kaydırma bitti
    onAuto: () -> Unit, // Otomatik bölüm
    onTogglePlay: () -> Unit, // Oynat/durdur
    onSave: (RingtoneKind, Boolean) -> Unit, // Kaydet
    onShare: () -> Unit, // Paylaş
    onOpenWriteSettings: () -> Unit, // İzin ekranı
    onDismissPermission: () -> Unit, // İzin vazgeç
) {
    val track = state.track ?: return // Parça yoksa çizilmez
    var sheet by remember { mutableStateOf(false) } // Kayıt sheet'i
    val maxStart = (track.melody.durationSec - state.lengthSec).coerceAtLeast(0f) // Kaydırıcı üst sınırı

    Screen { // Ekran
        Row(Modifier.fillMaxWidth().padding(vertical = Space.sm), verticalAlignment = Alignment.CenterVertically) { // Header
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back), modifier = Modifier.size(Size.iconMd)) } // Geri
            Text(stringResource(R.string.result_title), style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f)) // Başlık
            IconButton(onClick = onShare, enabled = state.pcm != null) { Icon(Icons.Rounded.Share, contentDescription = stringResource(R.string.share), modifier = Modifier.size(Size.iconMd)) } // Paylaş
        }
        AppCard { // Parça + rulo kartı
            Row(verticalAlignment = Alignment.CenterVertically) { // Başlık satırı
                if (track.thumbnailUrl != null) AsyncImage(model = track.thumbnailUrl, contentDescription = null, modifier = Modifier.size(56.dp).clip(RoundedCornerShape(Radius.sm))) // Kapak
                else Icon(Icons.Rounded.MusicNote, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(Size.iconMd)) // Yerel dosya ikonu
                Spacer(Modifier.width(Space.md)) // Boşluk
                Column(Modifier.weight(1f)) { // Metinler
                    Text(track.title, style = MaterialTheme.typography.bodyLarge, maxLines = 2, overflow = TextOverflow.Ellipsis) // Başlık
                    Caption(pluralStringResource(R.plurals.result_notes, track.melody.notes.size, track.subtitle, track.melody.notes.size)) // Kanal · nota sayısı
                }
            }
            Spacer(Modifier.height(Space.lg)) // Boşluk
            if (state.rendering || state.pcm == null) SkeletonBox(Modifier.fillMaxWidth().height(140.dp)) // Yükleniyor
            else PianoRoll(track.melody, state.startSec, state.lengthSec.toFloat(), if (isPlaying) positionSec else null) // Rulo
            Spacer(Modifier.height(Space.lg)) // Boşluk
            SecondaryButton(stringResource(if (isPlaying) R.string.stop else R.string.preview), onTogglePlay, enabled = state.pcm != null, icon = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow) // Önizleme
        }
        Spacer(Modifier.height(Space.xl)) // Bölüm boşluğu
        SectionTitle(stringResource(R.string.result_form)) // Form başlığı
        FlowRow(horizontalArrangement = Arrangement.spacedBy(Space.sm), verticalArrangement = Arrangement.spacedBy(Space.sm)) { // Chip'ler
            Instrument.entries.forEach { inst -> // Her tını
                FilterChip( // Chip
                    selected = state.instrument == inst, onClick = { onInstrument(inst) }, // Seçim
                    label = { Text(AppViewModel.instrumentName(inst), style = MaterialTheme.typography.labelMedium) }, // Ad
                    leadingIcon = { Icon(inst.icon(), contentDescription = null, modifier = Modifier.size(Size.iconSm)) }, // İkon
                    shape = RoundedCornerShape(Radius.sm), // Köşe
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer, selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer, selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer), // Renkler
                )
            }
        }
        Spacer(Modifier.height(Space.sm)) // Boşluk
        Caption(state.instrument.description()) // Tını açıklaması
        Spacer(Modifier.height(Space.xl)) // Bölüm boşluğu
        SectionTitle(stringResource(R.string.result_length)) // Uzunluk
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) { // Bölümlü
            listOf(15, 20, 30, 40).forEachIndexed { i, sec -> // Seçenekler
                SegmentedButton(selected = state.lengthSec == sec, onClick = { onLength(sec) }, shape = SegmentedButtonDefaults.itemShape(i, 4)) { Text("$sec sn", style = MaterialTheme.typography.labelMedium) } // Seçenek
            }
        }
        Spacer(Modifier.height(Space.xl)) // Bölüm boşluğu
        Row(verticalAlignment = Alignment.CenterVertically) { // Başlangıç başlığı + otomatik
            SectionTitle(stringResource(R.string.result_start, formatTime(state.startSec), formatTime(state.startSec + state.lengthSec)), Modifier.weight(1f)) // Aralık
            TextButton(onClick = onAuto) { Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(Size.iconSm)); Spacer(Modifier.width(Space.xs)); Text(stringResource(R.string.result_auto)) } // Otomatik
        }
        Slider(value = state.startSec.coerceIn(0f, maxStart), onValueChange = onStartChange, onValueChangeFinished = onStartCommit, valueRange = 0f..maxStart.coerceAtLeast(0.01f), enabled = maxStart > 0f) // Kaydırıcı
        Caption(stringResource(R.string.result_start_hint)) // İpucu
        Spacer(Modifier.height(Space.xl)) // Boşluk
        PrimaryButton(stringResource(R.string.result_make), onClick = { sheet = true }, enabled = state.pcm != null && !state.saving, loading = state.saving) // Tek birincil eylem
        Spacer(Modifier.height(Space.xxl)) // Alt boşluk
    }

    if (sheet) SaveSheet(onDismiss = { sheet = false }, onSave = { k, d -> sheet = false; onSave(k, d) }) // Kayıt sheet'i

    if (state.needsWriteSettings) AlertDialog( // İzin dialog'u
        onDismissRequest = onDismissPermission, // Kapat
        title = { Text(stringResource(R.string.perm_title)) }, // Başlık
        text = { Text(stringResource(R.string.perm_text)) }, // Açıklama
        confirmButton = { TextButton(onClick = onOpenWriteSettings) { Text(stringResource(R.string.perm_open)) } }, // Aç
        dismissButton = { TextButton(onClick = onDismissPermission) { Text(stringResource(R.string.perm_later)) } }, // Sonra
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SaveSheet(onDismiss: () -> Unit, onSave: (RingtoneKind, Boolean) -> Unit) {
    var kind by remember { mutableStateOf(RingtoneKind.RINGTONE) } // Seçili tür
    var setDefault by remember { mutableStateOf(true) } // Varsayılan yap
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true), shape = RoundedCornerShape(topStart = Radius.lg, topEnd = Radius.lg)) { // Sheet: tam açılır (yarım durumda Kaydet butonu ekran dışında kalıyordu, D-009)
        Column(Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Space.lg).padding(bottom = Space.xxl).navigationBarsPadding()) { // İçerik: kaydırılabilir + gezinme çubuğu payı
            Text(stringResource(R.string.save_title), style = MaterialTheme.typography.titleMedium) // Başlık
            Spacer(Modifier.height(Space.md)) // Boşluk
            KindRow(RingtoneKind.RINGTONE, Icons.Rounded.Call, stringResource(R.string.kind_ringtone), kind) { kind = it } // Zil
            KindRow(RingtoneKind.NOTIFICATION, Icons.Rounded.Notifications, stringResource(R.string.kind_notification), kind) { kind = it } // Bildirim
            KindRow(RingtoneKind.ALARM, Icons.Rounded.Alarm, stringResource(R.string.kind_alarm), kind) { kind = it } // Alarm
            Spacer(Modifier.height(Space.md)) // Boşluk
            ListRow(stringResource(R.string.save_set_default), stringResource(R.string.save_set_default_desc), showDivider = false, trailing = { Switch(checked = setDefault, onCheckedChange = { setDefault = it }) }) // Varsayılan anahtarı
            Spacer(Modifier.height(Space.lg)) // Boşluk
            PrimaryButton(stringResource(R.string.save), onClick = { onSave(kind, setDefault) }) // Kaydet
        }
    }
}

@Composable
private fun KindRow(kind: RingtoneKind, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, selected: RingtoneKind, onSelect: (RingtoneKind) -> Unit) {
    ListRow(label, icon = icon, onClick = { onSelect(kind) }, showChevron = false, trailing = { androidx.compose.material3.RadioButton(selected = selected == kind, onClick = { onSelect(kind) }) }) // Radyo satırı
}

/** Saniyeyi m:ss biçimine çevirir. */
fun formatTime(sec: Float): String { val s = sec.toInt().coerceAtLeast(0); return "%d:%02d".format(s / 60, s % 60) }
