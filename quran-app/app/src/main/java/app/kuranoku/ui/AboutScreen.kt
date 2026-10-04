package app.kuranoku.ui // hakkında ekranı

import androidx.compose.foundation.background // zemin
import androidx.compose.foundation.layout.Arrangement // dizilim
import androidx.compose.foundation.layout.Column // dikey dizi
import androidx.compose.foundation.layout.fillMaxSize // tam boyut
import androidx.compose.foundation.layout.navigationBarsPadding // gezinme çubuğu
import androidx.compose.foundation.layout.padding // iç boşluk
import androidx.compose.foundation.rememberScrollState // kaydırma
import androidx.compose.foundation.verticalScroll // dikey kaydırma
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.Text // metin
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.ui.Modifier // değiştirici
import app.kuranoku.ui.components.SectionLabel // etiket
import app.kuranoku.ui.components.TopBar // üst çubuk
import app.kuranoku.ui.theme.Space // boşluklar
import app.kuranoku.ui.theme.appColors // ek renkler

/** Hakkında: sürüm, metin kaynağı, yazı tipi lisansları, gizlilik. */
@Composable
fun AboutScreen(version: String, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) { // tam ekran
        TopBar("Hakkında", onBack) // üst çubuk
        Column(
            Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Space.l, vertical = Space.s).navigationBarsPadding(), // kaydırılabilir
            verticalArrangement = Arrangement.spacedBy(Space.xl), // bölümler arası 24
        ) {
            Block("Kur'an Oku $version", "Sadece Kur'an okumak için. Reklam yok, hesap yok, internet izni yok; hiçbir veri cihazından çıkmaz. Kaldığın sayfa ve yer imlerin yalnızca telefonunda saklanır.") // tanıtım
            Block("Metin", "Kur'an metni: Tanzil Projesi, Uthmani metni (tanzil.net). Metin değiştirilmeden kullanılmıştır; sadece her surenin başındaki besmele başlık olarak ayrı satırda gösterilir. Sayfa ve cüz düzeni Medine mushafına göredir (604 sayfa).") // kaynak
            Block("Yazı tipleri", "Amiri Quran ve Amiri (Khaled Hosny), Scheherazade New (SIL International), Noto Naskh Arabic (Google). Hepsi SIL Open Font License 1.1 ile dağıtılır.") // fontlar
            Block("İkonlar", "Lucide ikon seti (ISC lisansı).") // ikonlar
        }
    }
}

@Composable
private fun Block(title: String, body: String) { // başlık + paragraf
    Column(verticalArrangement = Arrangement.spacedBy(Space.xs)) {
        SectionLabel(title) // başlık
        Text(body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface) // metin
    }
}
