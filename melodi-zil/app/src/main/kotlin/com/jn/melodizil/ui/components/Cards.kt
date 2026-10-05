package com.jn.melodizil.ui.components // Ortak bileşenler

import androidx.compose.foundation.BorderStroke // Çerçeve
import androidx.compose.foundation.clickable // Tıklama
import androidx.compose.foundation.layout.Column // Sütun
import androidx.compose.foundation.layout.ColumnScope // Kapsam
import androidx.compose.foundation.layout.Row // Satır
import androidx.compose.foundation.layout.Spacer // Boşluk
import androidx.compose.foundation.layout.fillMaxWidth // Tam genişlik
import androidx.compose.foundation.layout.heightIn // En az yükseklik
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.layout.size // Boyut
import androidx.compose.foundation.layout.width // Genişlik
import androidx.compose.foundation.shape.RoundedCornerShape // Köşe
import androidx.compose.material.icons.Icons // İkon seti
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight // Liste oku
import androidx.compose.material3.Card // Kart
import androidx.compose.material3.CardDefaults // Kart varsayılanları
import androidx.compose.material3.HorizontalDivider // Ayraç
import androidx.compose.material3.Icon // İkon
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.material3.Text // Metin
import androidx.compose.runtime.Composable // Composable
import androidx.compose.ui.Alignment // Hizalama
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.graphics.vector.ImageVector // Vektör ikon
import androidx.compose.ui.text.style.TextOverflow // Taşma
import androidx.compose.ui.unit.dp // dp
import com.jn.melodizil.ui.theme.LocalExtraColors // Ek renkler
import com.jn.melodizil.ui.theme.Radius // Köşe
import com.jn.melodizil.ui.theme.Size // Boyut
import com.jn.melodizil.ui.theme.Space // Boşluk

/** Kart: kart yüzeyi + 1dp çerçeve, gölge YOK (DESIGN.md §7). Padding 16, radius-md. İç içe kart yok. */
@Composable
fun AppCard(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(Radius.md) // Köşe
    val colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Kart yüzeyi
    val border = BorderStroke(Size.borderWidth, LocalExtraColors.current.border) // Çerçeve
    val m = modifier.fillMaxWidth() // Tam genişlik
    if (onClick != null) Card(onClick = onClick, shape = shape, colors = colors, border = border, elevation = CardDefaults.cardElevation(0.dp), modifier = m) { Column(Modifier.padding(Space.lg), content = content) } // Tıklanabilir
    else Card(shape = shape, colors = colors, border = border, elevation = CardDefaults.cardElevation(0.dp), modifier = m) { Column(Modifier.padding(Space.lg), content = content) } // Statik
}


/** Liste satırı: ikon (20, muted) + başlık/alt başlık + sağda ok ya da özel içerik. Yükseklik ≥64. */
@Composable
fun ListRow(
    title: String, // Başlık
    subtitle: String? = null, // Alt başlık
    icon: ImageVector? = null, // Sol ikon
    onClick: (() -> Unit)? = null, // Tıklama
    showChevron: Boolean = onClick != null, // Sağ ok
    trailing: (@Composable () -> Unit)? = null, // Sağ özel içerik
    showDivider: Boolean = true, // Alt ayraç
) {
    Column { // Satır + ayraç
        Row( // Satır
            modifier = Modifier.fillMaxWidth().heightIn(min = Size.rowHeight).let { if (onClick != null) it.clickable(onClick = onClick) else it }.padding(vertical = Space.md), // Dokunma ve dikey padding
            verticalAlignment = Alignment.CenterVertically, // Ortala
        ) {
            if (icon != null) { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(Size.iconSm)); Spacer(Modifier.width(Space.md)) } // İkon
            Column(Modifier.weight(1f)) { // Metinler
                Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis) // Başlık
                if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis) // Alt başlık
            }
            if (trailing != null) trailing() // Özel sağ içerik
            else if (showChevron) Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(Size.iconSm)) // Ok (20, muted)
        }
        if (showDivider) HorizontalDivider(color = LocalExtraColors.current.border) // Ayraç
    }
}

/** Bölüm başlığı: title boyutunda, altında 12 boşluk. */
@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, modifier = modifier.padding(bottom = Space.md)) // Başlık
}

/** Boş kutu: başlık altı açıklama. */
@Composable
fun Caption(text: String, modifier: Modifier = Modifier) {
    Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = modifier) // Açıklama
}
