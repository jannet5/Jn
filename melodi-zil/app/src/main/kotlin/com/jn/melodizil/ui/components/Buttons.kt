package com.jn.melodizil.ui.components // Ortak bileşenler

import androidx.compose.foundation.layout.Row // Satır
import androidx.compose.foundation.layout.Spacer // Boşluk
import androidx.compose.foundation.layout.fillMaxWidth // Tam genişlik
import androidx.compose.foundation.layout.height // Yükseklik
import androidx.compose.foundation.layout.size // Boyut
import androidx.compose.foundation.layout.width // Genişlik
import androidx.compose.foundation.shape.RoundedCornerShape // Köşe
import androidx.compose.material3.Button // Birincil buton
import androidx.compose.material3.ButtonDefaults // Varsayılanlar
import androidx.compose.material3.CircularProgressIndicator // Yükleniyor
import androidx.compose.material3.Icon // İkon
import androidx.compose.material3.MaterialTheme // Tema
import androidx.compose.material3.OutlinedButton // İkincil buton
import androidx.compose.material3.Text // Metin
import androidx.compose.runtime.Composable // Composable
import androidx.compose.ui.Alignment // Hizalama
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.graphics.vector.ImageVector // Vektör ikon
import androidx.compose.ui.unit.dp // dp
import com.jn.melodizil.ui.theme.Radius // Köşe token'ı
import com.jn.melodizil.ui.theme.Size // Boyut token'ı
import com.jn.melodizil.ui.theme.Space // Boşluk token'ı

/** Birincil buton: ekranda yalnızca bir tane olur. Tam genişlik, 52 yüksek, radius-md. */
@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, loading: Boolean = false, icon: ImageVector? = null) {
    Button(onClick = onClick, enabled = enabled && !loading, shape = RoundedCornerShape(Radius.md), modifier = modifier.fillMaxWidth().height(Size.buttonHeight)) { // Buton
        if (loading) CircularProgressIndicator(modifier = Modifier.size(Size.iconSm), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary) // Yükleniyor
        else { // Normal
            if (icon != null) { Icon(icon, contentDescription = null, modifier = Modifier.size(Size.iconSm)); Spacer(Modifier.width(Space.sm)) } // İkon
            Text(text, style = MaterialTheme.typography.labelLarge) // Etiket
        }
    }
}

/** İkincil buton: çerçeveli, aynı ölçüler. */
@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, icon: ImageVector? = null) {
    OutlinedButton(onClick = onClick, enabled = enabled, shape = RoundedCornerShape(Radius.md), modifier = modifier.fillMaxWidth().height(Size.buttonHeight), colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)) { // Buton
        Row(verticalAlignment = Alignment.CenterVertically) { // İçerik
            if (icon != null) { Icon(icon, contentDescription = null, modifier = Modifier.size(Size.iconSm)); Spacer(Modifier.width(Space.sm)) } // İkon
            Text(text, style = MaterialTheme.typography.labelLarge) // Etiket
        }
    }
}
