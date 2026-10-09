package app.kuranoku.ui.theme // tema: DESIGN.md'deki token'ların tek kod karşılığı

import androidx.compose.material3.ColorScheme // Material renk şeması
import androidx.compose.material3.LocalContentColor // varsayılan içerik rengi
import androidx.compose.material3.MaterialTheme // tema sağlayıcı
import androidx.compose.material3.Typography // yazı ölçeği
import androidx.compose.material3.darkColorScheme // koyu şema oluşturucu
import androidx.compose.material3.lightColorScheme // açık şema oluşturucu
import androidx.compose.runtime.Composable // Compose işlevi
import androidx.compose.runtime.Immutable // değişmez sınıf işareti
import androidx.compose.runtime.staticCompositionLocalOf // ağaç boyunca değer taşıma
import androidx.compose.runtime.CompositionLocalProvider // değer sağlayıcı
import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.luminance // parlaklık hesabı
import androidx.compose.ui.text.TextStyle // yazı stili
import androidx.compose.ui.text.font.Font // yazı tipi dosyası
import androidx.compose.ui.text.font.FontFamily // yazı tipi ailesi
import androidx.compose.ui.text.font.FontWeight // kalınlık
import androidx.compose.ui.unit.dp // dp birimi
import androidx.compose.ui.unit.sp // sp birimi
import app.kuranoku.R // kaynaklar
import app.kuranoku.data.QuranFont // yazı tipi seçenekleri
import app.kuranoku.data.ReaderSettings // ayarlar

/** Boşluk skalası (DESIGN.md §5): 4 · 8 · 12 · 16 · 24 · 32 · 48. */
object Space {
    val xs = 4.dp // en küçük
    val s = 8.dp // küçük
    val m = 12.dp // orta
    val l = 16.dp // ekran kenarı, kart içi
    val xl = 24.dp // bölümler arası
    val xxl = 32.dp // büyük bölüm arası
    val xxxl = 48.dp // en büyük
}

/** Köşe yarıçapları (DESIGN.md §6): küçük 8, orta 14, büyük 24. */
object Radius {
    val sm = 8.dp // rozet, renk kutusu
    val md = 14.dp // kart, buton, input
    val lg = 24.dp // alt sayfa (sheet)
}

/** İkon boyutları (DESIGN.md §8). */
object IconSize {
    val s = 16.dp // metin içi
    val m = 20.dp // liste, buton
    val l = 24.dp // başlık çubuğu
}

/** Arayüz rengi token'ları (Material şemasında olmayanlar). */
@Immutable
data class AppColors(
    val muted: Color, // ikincil metin ve ikon
    val border: Color, // ince çerçeve, ayraç
    val chrome: Color, // okuma ekranındaki üst/alt çubuk zemini
    val success: Color, // başarı
    val dark: Boolean, // koyu mod mu
)

val LocalAppColors = staticCompositionLocalOf { lightAppColors } // ağaçtan erişim

private val lightScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF1F5A46), // marka: mushaf yeşili
    onPrimary = Color(0xFFFFFFFF), // yeşil üstü yazı
    secondaryContainer = Color(0xFFEDE5D3), // seçili çip zemini
    onSecondaryContainer = Color(0xFF1D1B18), // seçili çip yazısı
    background = Color(0xFFFBF8F1), // ekran zemini
    onBackground = Color(0xFF1D1B18), // ana metin
    surface = Color(0xFFFBF8F1), // yüzey
    onSurface = Color(0xFF1D1B18), // yüzey metni
    surfaceVariant = Color(0xFFF2ECDF), // sönük yüzey
    onSurfaceVariant = Color(0xFF625B50), // sönük yüzey metni
    surfaceContainerLow = Color(0xFFF6F1E6), // sheet zemini
    surfaceContainer = Color(0xFFF4EEE2), // kap zemini
    surfaceContainerHigh = Color(0xFFF1EADC), // yüksek kap
    surfaceContainerHighest = Color(0xFFEDE5D3), // en yüksek kap (slider izi)
    outline = Color(0xFFD8D0BF), // çerçeve
    outlineVariant = Color(0xFFE6DFD0), // ayraç
    error = Color(0xFFB3261E), // hata
)

private val darkScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFD6B56A), // marka (koyu): altın
    onPrimary = Color(0xFF1E1A10), // altın üstü yazı
    secondaryContainer = Color(0xFF2B3040), // seçili çip zemini
    onSecondaryContainer = Color(0xFFECE7DC), // seçili çip yazısı
    background = Color(0xFF14171D), // ekran zemini
    onBackground = Color(0xFFECE7DC), // ana metin
    surface = Color(0xFF14171D), // yüzey
    onSurface = Color(0xFFECE7DC), // yüzey metni
    surfaceVariant = Color(0xFF232733), // sönük yüzey
    onSurfaceVariant = Color(0xFFB0A898), // sönük yüzey metni
    surfaceContainerLow = Color(0xFF1A1E26), // sheet zemini
    surfaceContainer = Color(0xFF1D212A), // kap zemini
    surfaceContainerHigh = Color(0xFF22262F), // yüksek kap
    surfaceContainerHighest = Color(0xFF2B3040), // en yüksek kap
    outline = Color(0xFF3A3F4C), // çerçeve
    outlineVariant = Color(0xFF2C303A), // ayraç
    error = Color(0xFFF2B8B5), // hata
)

private val lightAppColors = AppColors(Color(0xFF6A645A), Color(0xFFE3DCCD), Color(0xF5FBF8F1), Color(0xFF2E7D4F), false) // açık ek token'lar
private val darkAppColors = AppColors(Color(0xFFA49C8D), Color(0xFF2E323B), Color(0xF514171D), Color(0xFF7CC3A6), true) // koyu ek token'lar

/** Yazı ölçeği (DESIGN.md §4): 12 · 14 · 16 · 20 · 24; ağırlıklar 400 · 500 · 700. */
private val typography = Typography(
    headlineSmall = TextStyle(fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold), // ekran başlığı
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold), // bölüm başlığı
    titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium), // liste başlığı
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal), // gövde
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal), // küçük gövde
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium), // buton
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium), // etiket
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal), // dipnot
)

/** Arayüzün açık mı koyu mu olacağı sayfa renginden çıkar: koyu sayfada koyu arayüz. */
fun ReaderSettings.isDarkPage(): Boolean = Color(pageColor).luminance() < 0.25f

/** Uygulama teması. */
@Composable
fun KuranTheme(dark: Boolean, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppColors provides if (dark) darkAppColors else lightAppColors) { // ek token'lar
        val sema = if (dark) darkScheme else lightScheme // renk şeması
        MaterialTheme(colorScheme = sema, typography = typography) { // Material tema
            CompositionLocalProvider(LocalContentColor provides sema.onBackground, content = content) // varsayılan yazı rengi temadan (koyu temada siyah yazı kalmasın)
        }
    }
}

/** Ek renk token'larına kısa erişim. */
val appColors: AppColors @Composable get() = LocalAppColors.current

/** Yazı tipi ailesi ve satır yüksekliği çarpanı. */
fun QuranFont.family(): FontFamily = when (this) {
    QuranFont.HAFS -> Hafs // KFGQPC Hafs
    QuranFont.SCHEHERAZADE -> Scheherazade // Scheherazade New
    QuranFont.NOTO_NASKH -> NotoNaskh // Noto Naskh Arabic
    QuranFont.AMIRI -> Amiri // Amiri
}

/** Her yazı tipinin rahat okunması için satır yüksekliği (yazı boyutunun katı). */
fun QuranFont.lineHeightFactor(): Float = 2.0f // harekeler ve ayet gülleri için rahat aralık

private val Hafs = FontFamily(Font(R.font.uthmanic_hafs)) // KFGQPC HAFS Uthmanic Script (değiştirilmemiş özgün dosya)
private val Scheherazade = FontFamily(Font(R.font.scheherazade_new)) // SIL Scheherazade New
private val NotoNaskh = FontFamily(Font(R.font.noto_naskh_arabic)) // Noto Naskh Arabic
private val Amiri = FontFamily(Font(R.font.amiri)) // Amiri
