package com.jn.melodizil.ui.process // İşlem ekranı

import androidx.compose.foundation.layout.Column // Sütun
import androidx.compose.foundation.layout.Row // Satır
import androidx.compose.foundation.layout.Spacer // Boşluk
import androidx.compose.foundation.layout.fillMaxWidth // Genişlik
import androidx.compose.foundation.layout.height // Yükseklik
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.layout.size // Boyut
import androidx.compose.foundation.layout.width // Genişlik
import androidx.compose.material.icons.Icons // İkonlar
import androidx.compose.material.icons.rounded.CheckCircle // Tamam
import androidx.compose.material.icons.rounded.RadioButtonUnchecked // Bekliyor
import androidx.compose.material3.CircularProgressIndicator // Dönen
import androidx.compose.material3.Icon // İkon
import androidx.compose.material3.LinearProgressIndicator // Çubuk
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.material3.Text // Metin
import androidx.compose.runtime.Composable // Composable
import androidx.compose.ui.Alignment // Hizalama
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.res.stringResource // Metin
import androidx.compose.ui.unit.dp // dp
import com.jn.melodizil.R // Kaynaklar
import com.jn.melodizil.domain.Step // Adımlar
import com.jn.melodizil.ui.ProcessState // Durum
import com.jn.melodizil.ui.components.AppCard // Kart
import com.jn.melodizil.ui.components.ErrorState // Hata
import com.jn.melodizil.ui.components.Screen // Ekran
import com.jn.melodizil.ui.components.SecondaryButton // İkincil
import com.jn.melodizil.ui.components.SkeletonBox // Skeleton
import com.jn.melodizil.ui.theme.LocalExtraColors // Ek renkler
import com.jn.melodizil.ui.theme.Size // Boyut
import com.jn.melodizil.ui.theme.Space // Boşluk

/**
 * İşlem ekranı: 4 adım, adım adım ilerleme. Referans: Shazam dinleme ekranı (tek odak), Google Files temizleme ilerleme listesi.
 * Durumlar: yükleniyor (adım listesi), hata (tekrar dene / geri), dolu→otomatik sonuç ekranına geçer.
 */
@Composable
fun ProcessingScreen(state: ProcessState, onCancel: () -> Unit, onRetry: () -> Unit, onBack: () -> Unit) {
    Screen(scrollable = false) { // Ekran
        Spacer(Modifier.height(Space.xl)) // Üst boşluk
        Text(stringResource(R.string.process_title), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface) // Başlık
        Spacer(Modifier.height(Space.xs)) // Boşluk
        Text(stringResource(R.string.process_subtitle), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) // Uyarı metni
        Spacer(Modifier.height(Space.xl)) // Bölüm boşluğu
        when (state) { // Duruma göre
            is ProcessState.Failed -> ErrorState(state.message, if (state.canRetry) onRetry else null, stringResource(R.string.retry), secondaryText = stringResource(R.string.back), onSecondary = onBack) // Hata
            is ProcessState.Running -> { // Çalışıyor
                AppCard { // Adım kartı
                    Row(verticalAlignment = Alignment.CenterVertically) { SkeletonBox(Modifier.size(56.dp)); Spacer(Modifier.width(Space.md)); Column { SkeletonBox(Modifier.fillMaxWidth(0.7f).height(16.dp)); Spacer(Modifier.height(Space.sm)); SkeletonBox(Modifier.fillMaxWidth(0.4f).height(12.dp)) } } // Başlık skeleton'u
                    Spacer(Modifier.height(Space.xl)) // Boşluk
                    Step.entries.forEach { step -> StepRow(step, state.progress.step, state.progress.fraction, state.sourceLabel != "YouTube") } // Adımlar
                }
                Spacer(Modifier.height(Space.xl)) // Boşluk
                SecondaryButton(stringResource(R.string.cancel), onCancel) // İptal (birincil değil: işlem birincil eylem)
            }
            else -> Unit // Idle/Done: gezinme yönetir
        }
    }
}

@Composable
private fun StepRow(step: Step, current: Step, fraction: Float, localFile: Boolean) {
    val label = when (step) { // Adım adı
        Step.RESOLVE -> stringResource(if (localFile) R.string.step_open else R.string.step_resolve)
        Step.DOWNLOAD -> stringResource(if (localFile) R.string.step_copy else R.string.step_download)
        Step.DECODE -> stringResource(R.string.step_decode)
        Step.SEPARATE -> stringResource(R.string.step_separate)
        Step.ANALYZE -> stringResource(R.string.step_analyze)
    }
    val done = step.ordinal < current.ordinal // Tamamlandı mı
    val active = step == current // Şu anki adım
    Column(Modifier.padding(vertical = Space.sm)) { // Satır
        Row(verticalAlignment = Alignment.CenterVertically) { // İkon + metin
            when {
                done -> Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = LocalExtraColors.current.success, modifier = Modifier.size(Size.iconSm)) // Tamam
                active -> CircularProgressIndicator(modifier = Modifier.size(Size.iconSm), strokeWidth = 2.dp) // Dönüyor
                else -> Icon(Icons.Rounded.RadioButtonUnchecked, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(Size.iconSm)) // Bekliyor
            }
            Spacer(Modifier.width(Space.md)) // Boşluk
            Text(label, style = MaterialTheme.typography.bodyLarge, color = if (active || done) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant) // Etiket
        }
        if (active) { Spacer(Modifier.height(Space.sm)); LinearProgressIndicator(progress = { fraction }, modifier = Modifier.fillMaxWidth().padding(start = Size.iconSm + Space.md)) } // İlerleme çubuğu
    }
}
