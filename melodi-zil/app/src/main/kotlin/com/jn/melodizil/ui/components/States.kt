package com.jn.melodizil.ui.components // Ortak bileşenler

import androidx.compose.foundation.background // Arka plan
import androidx.compose.foundation.layout.Box // Kutu
import androidx.compose.foundation.layout.Column // Sütun
import androidx.compose.foundation.layout.Spacer // Boşluk
import androidx.compose.foundation.layout.fillMaxWidth // Genişlik
import androidx.compose.foundation.layout.height // Yükseklik
import androidx.compose.foundation.layout.padding // Padding
import androidx.compose.foundation.layout.size // Boyut
import androidx.compose.foundation.shape.RoundedCornerShape // Köşe
import androidx.compose.material.icons.Icons // İkonlar
import androidx.compose.material.icons.rounded.ErrorOutline // Hata ikonu
import androidx.compose.material3.Icon // İkon
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.material3.Text // Metin
import androidx.compose.runtime.Composable // Composable
import androidx.compose.ui.Alignment // Hizalama
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.graphics.vector.ImageVector // İkon
import androidx.compose.ui.text.style.TextAlign // Hizalama
import androidx.compose.ui.unit.dp // dp
import com.jn.melodizil.ui.theme.LocalExtraColors // Ek renkler
import com.jn.melodizil.ui.theme.Radius // Köşe
import com.jn.melodizil.ui.theme.Space // Boşluk

/** Boş durum: 48 ikon + başlık + açıklama + isteğe bağlı birincil buton. */
@Composable
fun EmptyState(icon: ImageVector, title: String, description: String, modifier: Modifier = Modifier, actionText: String? = null, onAction: (() -> Unit)? = null) {
    Column(modifier.fillMaxWidth().padding(vertical = Space.xxxl), horizontalAlignment = Alignment.CenterHorizontally) { // Ortalanmış sütun
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp)) // Büyük ikon (yalnızca boş durumlarda 48)
        Spacer(Modifier.height(Space.lg)) // Boşluk
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center) // Başlık
        Spacer(Modifier.height(Space.sm)) // Boşluk
        Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center) // Açıklama
        if (actionText != null && onAction != null) { Spacer(Modifier.height(Space.xl)); PrimaryButton(actionText, onAction) } // Eylem
    }
}

/** Hata durumu: kırmızı ikon + mesaj + "Tekrar dene". */
@Composable
fun ErrorState(message: String, onRetry: (() -> Unit)?, retryText: String, modifier: Modifier = Modifier, secondaryText: String? = null, onSecondary: (() -> Unit)? = null) {
    Column(modifier.fillMaxWidth().padding(vertical = Space.xxl), horizontalAlignment = Alignment.CenterHorizontally) { // Sütun
        Icon(Icons.Rounded.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp)) // Hata ikonu
        Spacer(Modifier.height(Space.lg)) // Boşluk
        Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center) // Mesaj
        if (onRetry != null) { Spacer(Modifier.height(Space.xl)); PrimaryButton(retryText, onRetry) } // Tekrar
        if (secondaryText != null && onSecondary != null) { Spacer(Modifier.height(Space.md)); SecondaryButton(secondaryText, onSecondary) } // İkincil
    }
}

/** Skeleton kutusu: yükleniyor durumunda içerik yerine. */
@Composable
fun SkeletonBox(modifier: Modifier = Modifier) {
    Box(modifier.background(LocalExtraColors.current.mutedSurface, RoundedCornerShape(Radius.sm))) // Sönük yüzey
}
