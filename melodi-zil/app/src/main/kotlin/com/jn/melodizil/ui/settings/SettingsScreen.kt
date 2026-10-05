package com.jn.melodizil.ui.settings // Ayarlar ekranı

import androidx.compose.foundation.layout.Column // Sütun
import androidx.compose.foundation.layout.Spacer // Boşluk
import androidx.compose.foundation.layout.fillMaxWidth // Genişlik
import androidx.compose.foundation.layout.height // Yükseklik
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.rememberScrollState // Kaydırma
import androidx.compose.foundation.verticalScroll // Kaydırma
import androidx.compose.material.icons.Icons // İkonlar
import androidx.compose.material.icons.rounded.Code // Kaynak
import androidx.compose.material.icons.rounded.Info // Bilgi
import androidx.compose.material.icons.rounded.Lightbulb // Nasıl çalışır
import androidx.compose.material.icons.rounded.PrivacyTip // Gizlilik
import androidx.compose.material3.AlertDialog // Dialog
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.material3.SegmentedButton // Bölümlü
import androidx.compose.material3.SegmentedButtonDefaults // Varsayılan
import androidx.compose.material3.SingleChoiceSegmentedButtonRow // Satır
import androidx.compose.material3.Text // Metin
import androidx.compose.material3.TextButton // Buton
import androidx.compose.runtime.Composable // Composable
import androidx.compose.runtime.getValue // Delegasyon
import androidx.compose.runtime.mutableStateOf // Durum
import androidx.compose.runtime.remember // Hatırla
import androidx.compose.runtime.setValue // Delegasyon
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.res.stringResource // Metin
import com.jn.melodizil.R // Kaynaklar
import com.jn.melodizil.data.store.ThemeMode // Tema
import com.jn.melodizil.ui.components.AppCard // Kart
import com.jn.melodizil.ui.components.ListRow // Satır
import com.jn.melodizil.ui.components.Screen // Ekran
import com.jn.melodizil.ui.components.SectionTitle // Başlık
import com.jn.melodizil.ui.theme.Space // Boşluk

/** Ayarlar: tema, gizlilik politikası, nasıl çalışır, lisanslar, sürüm. Referans: Google Saat ayarları, Signal ayarları (gruplanmış kartlar). */
@Composable
fun SettingsScreen(theme: ThemeMode, versionName: String, onTheme: (ThemeMode) -> Unit) {
    var dialog by remember { mutableStateOf<Int?>(null) } // Açık bilgi dialog'u (string kaynağı)
    Screen { // Ekran
        Spacer(Modifier.height(Space.xl)) // Üst boşluk
        Text(stringResource(R.string.settings_title), style = MaterialTheme.typography.displaySmall) // Başlık
        Spacer(Modifier.height(Space.xl)) // Boşluk
        SectionTitle(stringResource(R.string.settings_theme)) // Tema
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) { // Seçim
            ThemeMode.entries.forEachIndexed { i, m -> // Üç seçenek
                SegmentedButton(selected = theme == m, onClick = { onTheme(m) }, shape = SegmentedButtonDefaults.itemShape(i, 3)) { Text(stringResource(when (m) { ThemeMode.SYSTEM -> R.string.theme_system; ThemeMode.LIGHT -> R.string.theme_light; ThemeMode.DARK -> R.string.theme_dark }), style = MaterialTheme.typography.labelMedium) } // Seçenek
            }
        }
        Spacer(Modifier.height(Space.xl)) // Bölüm boşluğu
        SectionTitle(stringResource(R.string.settings_about)) // Hakkında
        AppCard { // Liste kartı
            Column { // Satırlar
                ListRow(stringResource(R.string.settings_how), icon = Icons.Rounded.Lightbulb, onClick = { dialog = R.string.how_text }) // Nasıl çalışır
                ListRow(stringResource(R.string.settings_privacy), icon = Icons.Rounded.PrivacyTip, onClick = { dialog = R.string.privacy_text }) // Gizlilik
                ListRow(stringResource(R.string.settings_licenses), icon = Icons.Rounded.Code, onClick = { dialog = R.string.licenses_text }) // Lisanslar
                ListRow(stringResource(R.string.settings_version), versionName, Icons.Rounded.Info, showChevron = false, showDivider = false) // Sürüm
            }
        }
        Spacer(Modifier.height(Space.xxl)) // Alt boşluk
    }
    dialog?.let { res -> // Bilgi dialog'u
        AlertDialog(onDismissRequest = { dialog = null }, confirmButton = { TextButton(onClick = { dialog = null }) { Text(stringResource(R.string.ok)) } }, // Kapat
            title = { Text(stringResource(when (res) { R.string.how_text -> R.string.settings_how; R.string.privacy_text -> R.string.settings_privacy; else -> R.string.settings_licenses })) }, // Başlık
            text = { Text(stringResource(res), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.verticalScroll(rememberScrollState()).padding(end = Space.xs)) }) // Kaydırılabilir metin
    }
}
