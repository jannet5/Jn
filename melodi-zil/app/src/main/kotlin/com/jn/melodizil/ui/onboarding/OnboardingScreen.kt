package com.jn.melodizil.ui.onboarding // Tanıtım ekranı

import androidx.compose.foundation.layout.Column // Sütun
import androidx.compose.foundation.layout.Row // Satır
import androidx.compose.foundation.layout.Spacer // Boşluk
import androidx.compose.foundation.layout.fillMaxWidth // Genişlik
import androidx.compose.foundation.layout.height // Yükseklik
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.layout.size // Boyut
import androidx.compose.foundation.layout.width // Genişlik
import androidx.compose.material.icons.Icons // İkonlar
import androidx.compose.material.icons.rounded.Link // Bağlantı
import androidx.compose.material.icons.rounded.MusicNote // Nota
import androidx.compose.material.icons.rounded.PhoneAndroid // Telefon
import androidx.compose.material3.Icon // İkon
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.material3.Text // Metin
import androidx.compose.runtime.Composable // Composable
import androidx.compose.ui.Alignment // Hizalama
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.graphics.vector.ImageVector // İkon
import androidx.compose.ui.res.stringResource // Metin kaynağı
import com.jn.melodizil.R // Kaynaklar
import com.jn.melodizil.ui.components.PrimaryButton // Buton
import com.jn.melodizil.ui.components.Screen // Ekran
import com.jn.melodizil.ui.theme.Size // Boyut
import com.jn.melodizil.ui.theme.Space // Boşluk

/** Tek sayfalık tanıtım: 3 adım + Başla. Referans: Shazam ilk açılış, Google Zil Sesi onboarding. */
@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    Screen(scrollable = false) { // Kaydırmasız: içerik sığar
        Spacer(Modifier.weight(1f)) // Üst boşluk
        Icon(Icons.Rounded.MusicNote, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(Size.touchMin)) // Logo ikonu
        Spacer(Modifier.height(Space.xl)) // Boşluk
        Text(stringResource(R.string.onboarding_title), style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onSurface) // Başlık
        Spacer(Modifier.height(Space.sm)) // Boşluk
        Text(stringResource(R.string.onboarding_subtitle), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant) // Alt başlık
        Spacer(Modifier.height(Space.xxl)) // Bölüm boşluğu
        StepRow(Icons.Rounded.Link, stringResource(R.string.onboarding_step1_title), stringResource(R.string.onboarding_step1_desc)) // Adım 1
        StepRow(Icons.Rounded.MusicNote, stringResource(R.string.onboarding_step2_title), stringResource(R.string.onboarding_step2_desc)) // Adım 2
        StepRow(Icons.Rounded.PhoneAndroid, stringResource(R.string.onboarding_step3_title), stringResource(R.string.onboarding_step3_desc)) // Adım 3
        Spacer(Modifier.weight(1f)) // Alt boşluk
        Text(stringResource(R.string.onboarding_note), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) // Telif notu
        Spacer(Modifier.height(Space.lg)) // Boşluk
        PrimaryButton(stringResource(R.string.onboarding_start), onDone, modifier = Modifier.padding(bottom = Space.lg)) // Başla
    }
}

@Composable
private fun StepRow(icon: ImageVector, title: String, desc: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = Space.md), verticalAlignment = Alignment.CenterVertically) { // Satır
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(Size.iconMd)) // İkon 24
        Spacer(Modifier.width(Space.lg)) // Boşluk
        Column { // Metinler
            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface) // Başlık
            Text(desc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) // Açıklama
        }
    }
}
