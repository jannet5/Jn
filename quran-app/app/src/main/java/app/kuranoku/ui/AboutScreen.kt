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
            Block("Metin ve sayfa düzeni", "Kur'an metni ve yazı tipi Medine'deki Kral Fahd Kur'an-ı Kerim Matbaası'nındır (KFGQPC Hafs, Uthmanic Script). Sayfa ve satır düzeni Medine mushafıyla birebir aynıdır: 604 sayfa, her sayfada 15 satır, her kelime basılı mushaftaki satırında. Kelime/satır verisi quran.com (Quran Foundation) üzerinden alınmıştır.") // kaynak
            Block("Yazı tipi", "KFGQPC HAFS Uthmanic Script, Kral Fahd Kur'an-ı Kerim Matbaası. Lisansı gereği değiştirilmeden, ücretsiz olarak kullanılmaktadır.") // font
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
