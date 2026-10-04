package app.kuranoku.ui.reader // okuma ekranı

import android.graphics.Color as AColor // Android renk dönüşümleri (HSV)
import androidx.compose.foundation.background // zemin
import androidx.compose.foundation.border // çerçeve
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Box // kutu
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.material3.AlertDialog // pencere
import androidx.compose.material3.Slider // kaydırıcı
import androidx.compose.material3.Text // metin
import androidx.compose.material3.TextButton // metin buton
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableFloatStateOf // ondalık durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.graphics.Brush // fırça
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.luminance // parlaklık
import androidx.compose.ui.unit.dp // dp
import app.kuranoku.ui.components.SectionLabel // etiket
import app.kuranoku.ui.theme.Radius // köşeler
import app.kuranoku.ui.theme.Space // boşluklar

/** Özel renk seçici: renk tonu, doygunluk ve parlaklık kaydırıcıları + canlı önizleme. */
@Composable
fun ColorPickerDialog(title: String, initial: Int, onPick: (Int) -> Unit, onDismiss: () -> Unit) {
    val hsv = remember { FloatArray(3).also { AColor.colorToHSV(initial, it) } } // başlangıç rengi HSV'ye çevriliyor
    var ton by remember { mutableFloatStateOf(hsv[0]) } // renk tonu (0-360)
    var doy by remember { mutableFloatStateOf(hsv[1]) } // doygunluk (0-1)
    var parlak by remember { mutableFloatStateOf(hsv[2]) } // parlaklık (0-1)
    val renk = AColor.HSVToColor(floatArrayOf(ton, doy, parlak)) // seçilen renk
    AlertDialog(
        onDismissRequest = onDismiss, // dışarı dokununca kapan
        title = { Text(title) }, // başlık
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Space.m)) {
                Box( // önizleme
                    Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(Radius.md)).background(Color(renk)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(Radius.md)), // renk kutusu
                    contentAlignment = Alignment.Center, // ortalı
                ) { Text("بِسْمِ ٱللَّهِ", color = if (Color(renk).luminance() > 0.45f) Color(0xFF1D1B18) else Color(0xFFF5F0E6)) } // örnek yazı
                Track("Renk tonu", Brush.horizontalGradient((0..6).map { Color.hsv(it * 60f % 360f, 0.8f, 0.9f) })) // gökkuşağı şeridi
                Slider(ton, { ton = it }, valueRange = 0f..359f) // ton kaydırıcı
                Track("Canlılık", Brush.horizontalGradient(listOf(Color.hsv(ton, 0f, parlak), Color.hsv(ton, 1f, parlak)))) // gri → canlı şeridi
                Slider(doy, { doy = it }, valueRange = 0f..1f) // doygunluk kaydırıcı
                Track("Açıklık", Brush.horizontalGradient(listOf(Color.hsv(ton, doy, 0f), Color.hsv(ton, doy, 1f)))) // koyu → açık şeridi
                Slider(parlak, { parlak = it }, valueRange = 0f..1f) // parlaklık kaydırıcı
            }
        },
        confirmButton = { TextButton(onClick = { onPick(renk) }) { Text("Seç") } }, // onay
        dismissButton = { TextButton(onClick = onDismiss) { Text("Vazgeç") } }, // vazgeç
    )
}

/** Kaydırıcının üstündeki etiket ve renk şeridi. */
@Composable
private fun Track(label: String, brush: Brush) {
    Column(verticalArrangement = Arrangement.spacedBy(Space.xs)) {
        SectionLabel(label) // etiket
        Box(Modifier.fillMaxWidth().height(8.dp).padding(horizontal = Space.s).clip(RoundedCornerShape(Radius.sm)).background(brush)) // şerit
    }
}
