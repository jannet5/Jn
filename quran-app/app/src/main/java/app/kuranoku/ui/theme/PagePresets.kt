package app.kuranoku.ui.theme // sayfa görünümleri

import androidx.compose.ui.graphics.Color // renk
import androidx.compose.ui.graphics.luminance // parlaklık
import app.kuranoku.data.ReaderSettings // ayarlar

/** Hazır sayfa görünümü. */
data class PagePreset(
    val id: String, // kimlik
    val label: String, // görünen ad
    val page: Int, // sayfa rengi
    val ink: Int, // yazı rengi
    val ornament: Int, // süs rengi
    val texture: Boolean, // doku
    val frame: Boolean, // çerçeve
)

/** Kullanıcının seçebileceği hazır görünümler. */
val PagePresets = listOf(
    PagePreset("mushaf", "Mushaf", 0xFFEFE4CC.toInt(), 0xFF2B2118.toInt(), 0xFFA88234.toInt(), texture = true, frame = true), // fildişi kağıt, altın çerçeve
    PagePreset("sade", "Sade", 0xFFFFFFFF.toInt(), 0xFF111111.toInt(), 0xFF0F6E66.toInt(), texture = false, frame = false), // beyaz sayfa siyah yazı
    PagePreset("lacivert", "Lacivert", 0xFF13203A.toInt(), 0xFFECDFC2.toInt(), 0xFFD6B56A.toInt(), texture = true, frame = true), // koyu mavi, krem yazı
    PagePreset("gece", "Gece", 0xFF151515.toInt(), 0xFFD2CBBD.toInt(), 0xFF8FA89A.toInt(), texture = false, frame = false), // gece okuması
    PagePreset("mavi", "Mavi", 0xFFE6EEF7.toInt(), 0xFF16365F.toInt(), 0xFF3D6AA1.toInt(), texture = true, frame = true), // açık mavi kağıt
    PagePreset("sepya", "Sepya", 0xFFF3E7D3.toInt(), 0xFF4A3420.toInt(), 0xFF8C5A2B.toInt(), texture = true, frame = false), // eski kitap
)

/** Sayfa rengi seçenekleri. */
val PageColors = listOf(
    0xFFEFE4CC to "Fildişi", 0xFFF7F1E1 to "Krem", 0xFFFFFFFF to "Beyaz", 0xFFECECEA to "Açık gri",
    0xFFF3E7D3 to "Sepya", 0xFFE6EEF7 to "Açık mavi", 0xFFCFE0F1 to "Gök mavisi", 0xFFE5EFE4 to "Açık yeşil",
    0xFFD7EBDD to "Nane", 0xFFF4E4E1 to "Gül", 0xFF13203A to "Lacivert", 0xFF14281F to "Koyu yeşil",
    0xFF2A1F17 to "Koyu kahve", 0xFF151515 to "Gece",
).map { it.first.toInt() to it.second } // ARGB tamsayıya çevriliyor

/** Yazı rengi seçenekleri. */
val InkColors = listOf(
    0xFF2B2118 to "Mürekkep", 0xFF111111 to "Siyah", 0xFF3A3A3A to "Koyu gri", 0xFF16365F to "Lacivert",
    0xFF1F4E8C to "Mavi", 0xFF1F5A46 to "Yeşil", 0xFF0E6B4F to "Zümrüt", 0xFF6B1E25 to "Bordo",
    0xFF5A3A1E to "Kahve", 0xFFECDFC2 to "Krem", 0xFFD6B56A to "Altın", 0xFFF2F2F2 to "Beyaz",
).map { it.first.toInt() to it.second } // ARGB tamsayıya çevriliyor

/** Hazır görünümü ayarlara uygular. */
fun ReaderSettings.withPreset(p: PagePreset) = copy(presetId = p.id, pageColor = p.page, inkColor = p.ink, ornamentColor = p.ornament, texture = p.texture, frame = p.frame)

/** Sayfa rengi değişince: süs rengi okunmaz hale gelirse yazı rengine yaklaştırılır. */
fun ReaderSettings.withPageColor(c: Int): ReaderSettings {
    val y = copy(presetId = "custom", pageColor = c) // yeni ayar
    return if (contrast(y.ornamentColor, c) < 2.2f) y.copy(ornamentColor = y.inkColor) else y // süs okunuyor mu
}

/** Yazı rengi değişince süs rengi korunur; okunmuyorsa yazı rengini alır. */
fun ReaderSettings.withInkColor(c: Int): ReaderSettings {
    val y = copy(presetId = "custom", inkColor = c) // yeni ayar
    return if (contrast(y.ornamentColor, y.pageColor) < 2.2f) y.copy(ornamentColor = c) else y // süs okunuyor mu
}

/** WCAG kontrast oranı (1 – 21). */
fun contrast(a: Int, b: Int): Float {
    val la = Color(a).luminance() // a'nın parlaklığı
    val lb = Color(b).luminance() // b'nin parlaklığı
    return (maxOf(la, lb) + 0.05f) / (minOf(la, lb) + 0.05f) // oran
}
