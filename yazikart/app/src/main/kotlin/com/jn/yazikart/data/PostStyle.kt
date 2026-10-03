package com.jn.yazikart.data // veri katmanı paketi

import android.content.Context // ayarları kaydetmek için uygulama bağlamı

const val MIN_TEXT_SIZE = 0.03f // en küçük yazı boyu (genişliğin oranı)
const val MAX_TEXT_SIZE = 0.3f // en büyük yazı boyu (genişliğin oranı)

// Görselin oranı: Instagram gönderi ve hikaye boyutları
enum class AspectRatio(val label: String, val width: Int, val height: Int) {
    SQUARE("Kare 1:1", 1080, 1080), // kare gönderi
    PORTRAIT("Dikey 4:5", 1080, 1350), // dikey gönderi
    STORY("Hikaye 9:16", 1080, 1920), // hikaye / reels
}

// Yazının yatay hizası
enum class TextAlign { LEFT, CENTER, RIGHT }

// Yazının dikey konumu
enum class VerticalPos { TOP, CENTER, BOTTOM }

// Kayıt biçimi
enum class ExportFormat(val label: String, val mime: String, val ext: String) {
    PNG("PNG", "image/png", "png"), // kayıpsız, net
    JPEG("JPEG", "image/jpeg", "jpg"), // daha küçük dosya
}

// Görseli oluşturan tüm ayarlar (resim hariç; resim ayrıca tutuluyor)
data class PostStyle(
    val text: String = "", // kullanıcının yazdığı yazı
    val fontIndex: Int = 0, // seçili yazı tipinin sırası
    val bold: Boolean = false, // kalın mı
    val textSize: Float = 0.08f, // yazı boyu, görsel genişliğinin oranı olarak (önizleme = kayıt)
    val textColor: Int = 0xFFFFFFFF.toInt(), // yazı rengi (varsayılan beyaz)
    val backgroundColor: Int = 0xFF000000.toInt(), // zemin rengi (varsayılan siyah)
    val align: TextAlign = TextAlign.CENTER, // yatay hiza
    val verticalPos: VerticalPos = VerticalPos.CENTER, // dikey konum
    val shadow: Boolean = false, // yazı gölgesi (resim üstünde okunurluk için)
    val offsetX: Float = 0f, // parmakla sürüklenen yatay kayma (genişliğin oranı; 0 = hizadaki yer)
    val offsetY: Float = 0f, // parmakla sürüklenen dikey kayma (yüksekliğin oranı; 0 = konumdaki yer)
    val dim: Float = 0.35f, // arka plan resmini karartma oranı (0 = hiç)
    val aspect: AspectRatio = AspectRatio.SQUARE, // görsel oranı
    val format: ExportFormat = ExportFormat.PNG, // kayıt biçimi
)

// Son kullanılan stili telefonda saklar; uygulama açılınca aynı ayarlarla başlarsın
class StyleStore(context: Context) {
    private val prefs = context.getSharedPreferences("stil", Context.MODE_PRIVATE) // küçük ayar dosyası

    // Kayıtlı stili okur (yazının kendisi saklanmaz, sadece görünüm)
    fun load(): PostStyle {
        val d = PostStyle() // varsayılan değerler
        return PostStyle(
            fontIndex = prefs.getInt("font", d.fontIndex).coerceIn(0, FontCatalog.fonts.lastIndex), // yazı tipi
            bold = prefs.getBoolean("bold", d.bold), // kalınlık
            textSize = prefs.getFloat("size", d.textSize), // boyut
            textColor = prefs.getInt("textColor", d.textColor), // yazı rengi
            backgroundColor = prefs.getInt("bgColor", d.backgroundColor), // zemin rengi
            align = enumOr(prefs.getString("align", null), d.align), // hiza
            verticalPos = enumOr(prefs.getString("vpos", null), d.verticalPos), // dikey konum
            shadow = prefs.getBoolean("shadow", d.shadow), // gölge
            offsetX = prefs.getFloat("offX", d.offsetX), // yatay kayma
            offsetY = prefs.getFloat("offY", d.offsetY), // dikey kayma
            dim = prefs.getFloat("dim", d.dim), // karartma
            aspect = enumOr(prefs.getString("aspect", null), d.aspect), // oran
            format = enumOr(prefs.getString("format", null), d.format), // biçim
        )
    }

    // Stili kaydeder
    fun save(s: PostStyle) {
        prefs.edit() // düzenleme başlıyor
            .putInt("font", s.fontIndex) // yazı tipi
            .putBoolean("bold", s.bold) // kalınlık
            .putFloat("size", s.textSize) // boyut
            .putInt("textColor", s.textColor) // yazı rengi
            .putInt("bgColor", s.backgroundColor) // zemin rengi
            .putString("align", s.align.name) // hiza
            .putString("vpos", s.verticalPos.name) // dikey konum
            .putBoolean("shadow", s.shadow) // gölge
            .putFloat("offX", s.offsetX) // yatay kayma
            .putFloat("offY", s.offsetY) // dikey kayma
            .putFloat("dim", s.dim) // karartma
            .putString("aspect", s.aspect.name) // oran
            .putString("format", s.format.name) // biçim
            .apply() // arka planda diske yazılıyor
    }

    // Metinden enum değerine güvenli çeviri; bulunamazsa varsayılan döner
    private inline fun <reified T : Enum<T>> enumOr(name: String?, def: T): T =
        enumValues<T>().firstOrNull { it.name == name } ?: def // eşleşen yoksa varsayılan
}
