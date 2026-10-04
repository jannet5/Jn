package app.kuranoku.ui.components // ortak bileşenler

import androidx.compose.foundation.background // zemin
import androidx.compose.foundation.clickable // tıklanabilir
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Box // kutu
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.Row // yatay dizi
import androidx.compose.foundation.layout.RowScope // satır kapsamı
import androidx.compose.foundation.layout.Spacer // boşluk
import androidx.compose.foundation.layout.fillMaxSize // tam boyut
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.heightIn // en az yükseklik
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.layout.sizeIn // en az boyut
import androidx.compose.foundation.layout.statusBarsPadding // durum çubuğu boşluğu
import androidx.compose.foundation.layout.widthIn // genişlik sınırı
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.material3.Button // birincil buton
import androidx.compose.material3.HorizontalDivider // ayraç
import androidx.compose.material3.IconButton // ikon buton
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.Text // metin
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştirici
import androidx.compose.ui.draw.clip // kırpma
import androidx.compose.ui.graphics.vector.ImageVector // ikon tipi
import androidx.compose.ui.text.style.TextAlign // metin hizası
import androidx.compose.ui.text.style.TextOverflow // taşma
import androidx.compose.ui.unit.dp // dp
import app.kuranoku.ui.theme.IconSize // ikon boyutları
import app.kuranoku.ui.theme.Radius // köşeler
import app.kuranoku.ui.theme.Space // boşluklar
import app.kuranoku.ui.theme.appColors // ek renkler

/** 48dp dokunma alanlı ikon buton. */
@Composable
fun IconBtn(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier, selected: Boolean = false) {
    IconButton(onClick = onClick, modifier = modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)) { // dokunma alanı 48dp
        Ico(icon, label, size = IconSize.l, tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface) // seçiliyse marka rengi
    }
}

/** Standart üst çubuk: geri + başlık + sağ eylemler. */
@Composable
fun TopBar(title: String, onBack: () -> Unit, actions: @Composable RowScope.() -> Unit = {}) {
    Row(
        Modifier.fillMaxWidth().statusBarsPadding().height(56.dp).padding(horizontal = Space.xs), // durum çubuğunun altından başlar
        verticalAlignment = Alignment.CenterVertically, // dikey ortalı
    ) {
        IconBtn(Lucide.ChevronLeft, "Geri", onBack) // geri butonu
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f).padding(start = Space.xs), maxLines = 1, overflow = TextOverflow.Ellipsis) // başlık
        actions() // sağdaki eylemler
    }
}

/** Liste satırı: rozet + başlık/alt başlık + sağ içerik + ok. */
@Composable
fun ListRow(
    title: String, // başlık
    subtitle: String, // alt başlık
    onClick: () -> Unit, // dokunma
    badge: String? = null, // soldaki rozet
    highlighted: Boolean = false, // şu an okunan mı
    trailing: @Composable (() -> Unit)? = null, // sağ içerik
    showChevron: Boolean = true, // ok gösterilsin mi
) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 64.dp).clickable(onClick = onClick) // en az 64dp, tıklanabilir
            .background(if (highlighted) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface) // vurgulu satır
            .padding(horizontal = Space.l, vertical = Space.s), // kenar boşlukları
        verticalAlignment = Alignment.CenterVertically, // dikey ortalı
        horizontalArrangement = Arrangement.spacedBy(Space.m), // öğeler arası 12
    ) {
        if (badge != null) Badge(badge, highlighted) // rozet
        Column(Modifier.weight(1f)) { // metinler
            Text(title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis) // başlık
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = appColors.muted, maxLines = 1, overflow = TextOverflow.Ellipsis) // alt başlık
        }
        trailing?.invoke() // sağ içerik
        if (showChevron) Ico(Lucide.ChevronRight, null, size = IconSize.m, tint = appColors.muted) // liste oku: 20, muted
    }
}

/** Kare rozet (sure no, cüz no). */
@Composable
fun Badge(text: String, active: Boolean = false) {
    Box(
        Modifier.widthIn(min = 36.dp).height(36.dp).clip(RoundedCornerShape(Radius.sm)) // 36dp, küçük köşe
            .background(if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant) // zemin
            .padding(horizontal = Space.xs), // iç boşluk
        contentAlignment = Alignment.Center, // ortalı
    ) {
        Text(text, style = MaterialTheme.typography.labelMedium, color = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface, maxLines = 1) // metin
    }
}

/** Bölüm etiketi. */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(text, style = MaterialTheme.typography.labelLarge, color = appColors.muted, modifier = modifier) // sönük etiket
}

/** Liste ayracı. */
@Composable
fun RowDivider() = HorizontalDivider(Modifier.padding(start = Space.l), thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant) // ince ayraç

/** Boş durum: ikon + başlık + açıklama + isteğe bağlı buton. */
@Composable
fun EmptyState(icon: ImageVector, title: String, description: String, action: String? = null, onAction: () -> Unit = {}) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = Space.xxl, vertical = Space.xxxl), // geniş boşluk
        horizontalAlignment = Alignment.CenterHorizontally, // ortalı
        verticalArrangement = Arrangement.spacedBy(Space.m), // 12 aralık
    ) {
        Box(Modifier.size(56.dp).clip(RoundedCornerShape(Radius.md)).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) { // ikon kutusu
            Ico(icon, null, size = IconSize.l, tint = appColors.muted) // ikon
        }
        Text(title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center) // başlık
        Text(description, style = MaterialTheme.typography.bodyMedium, color = appColors.muted, textAlign = TextAlign.Center) // açıklama
        if (action != null) { Spacer(Modifier.height(Space.xs)); Button(onClick = onAction, shape = RoundedCornerShape(Radius.md), modifier = Modifier.heightIn(min = 48.dp)) { Text(action) } } // buton
    }
}

/** Hata durumu: neyin ters gittiği + tekrar dene. */
@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) { // tam ekran
        EmptyState(Lucide.TriangleAlert, "Kur'an açılamadı", message, "Tekrar dene", onRetry) // boş durum düzeniyle
    }
}
