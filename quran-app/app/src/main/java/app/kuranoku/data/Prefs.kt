package app.kuranoku.data // veri katmanı

import android.content.Context // SharedPreferences için
import kotlinx.coroutines.flow.MutableStateFlow // değişebilir akış
import kotlinx.coroutines.flow.StateFlow // dışarıya salt okunur akış
import kotlinx.coroutines.flow.asStateFlow // dönüştürme

/** Kur'an yazı tipleri. */
enum class QuranFont(val id: String, val label: String, val note: String) {
    HAFS("hafs", "KFGQPC Hafs", "Medine mushafının kendi hattı"); // Kral Fahd Kur'an Matbaası yazı tipi (tek ve doğru)

    companion object { fun of(@Suppress("UNUSED_PARAMETER") id: String?) = HAFS } // eski ayarlar da Hafs'a döner
}

/** Okuma görünümü ayarları. Renkler ARGB tamsayıdır. */
data class ReaderSettings(
    val scale: Float = 1f, // yazı boyutu çarpanı (0.65 – 2.55)
    val presetId: String = "mushaf", // seçili hazır görünüm ya da "custom"
    val pageColor: Int = 0xFFEFE4CC.toInt(), // sayfa rengi
    val inkColor: Int = 0xFF2B2118.toInt(), // yazı rengi
    val ornamentColor: Int = 0xFFA88234.toInt(), // süs (ayet gülü, çerçeve) rengi
    val texture: Boolean = true, // kağıt dokusu
    val frame: Boolean = true, // süslü çerçeve
    val font: QuranFont = QuranFont.HAFS, // yazı tipi
    val mushafLayout: Boolean = true, // true: Medine mushafı sayfa düzeni, false: akan yazı
    val keepScreenOn: Boolean = true, // okurken ekran kapanmasın
) {
    companion object {
        const val MIN_SCALE = 0.65f // en küçük
        const val MAX_SCALE = 2.55f // en büyük
    }
}

/** Yer imi. */
data class Bookmark(val page: Int, val createdAt: Long) // sayfa ve eklenme zamanı

/** Ayarları, kaldığı sayfayı ve yer imlerini cihazda saklar. Uygulama güncellense de silinmez. */
class Prefs(context: Context) {
    private val sp = context.getSharedPreferences("kuran_oku", Context.MODE_PRIVATE) // kalıcı depo

    private val _settings = MutableStateFlow(readSettings()) // ayarlar akışı
    val settings: StateFlow<ReaderSettings> = _settings.asStateFlow() // dışarıya

    private val _bookmarks = MutableStateFlow(readBookmarks()) // yer imleri akışı
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks.asStateFlow() // dışarıya

    var lastPage: Int // kaldığı sayfa
        get() = sp.getInt("last_page", 1) // okunuyor (ilk açılışta 1)
        set(v) = sp.edit().putInt("last_page", v).apply() // yazılıyor

    var hintShown: Boolean // ilk açılış ipucu gösterildi mi
        get() = sp.getBoolean("hint_shown", false) // okunuyor
        set(v) = sp.edit().putBoolean("hint_shown", v).apply() // yazılıyor

    /** Ayarları bellekte günceller; [persist] true ise diske de yazar. */
    fun update(s: ReaderSettings, persist: Boolean = true) {
        _settings.value = s // anında arayüze yansır
        if (persist) writeSettings(s) // diske yazılıyor
    }

    /** Bellekteki ayarları diske yazar (iki parmakla büyütme bitince çağrılır). */
    fun flush() = writeSettings(_settings.value)

    /** Sayfa için yer imi ekler ya da kaldırır. */
    fun toggleBookmark(page: Int) {
        val liste = _bookmarks.value // mevcut liste
        _bookmarks.value = if (liste.any { it.page == page }) liste.filterNot { it.page == page } // varsa kaldır
        else (liste + Bookmark(page, System.currentTimeMillis())).sortedByDescending { it.createdAt } // yoksa ekle
        writeBookmarks() // kaydet
    }

    /** Silinen yer imini geri koyar ("Geri al"). */
    fun restoreBookmark(b: Bookmark) {
        if (_bookmarks.value.any { it.page == b.page }) return // zaten varsa
        _bookmarks.value = (_bookmarks.value + b).sortedByDescending { it.createdAt } // geri ekleniyor
        writeBookmarks() // kaydet
    }

    private fun readSettings(): ReaderSettings { // diskten ayar okuma
        val d = ReaderSettings() // varsayılanlar
        return ReaderSettings(
            scale = sp.getFloat("scale", d.scale).coerceIn(ReaderSettings.MIN_SCALE, ReaderSettings.MAX_SCALE), // boyut
            presetId = sp.getString("preset", d.presetId) ?: d.presetId, // hazır görünüm
            pageColor = sp.getInt("page_color", d.pageColor), // sayfa rengi
            inkColor = sp.getInt("ink_color", d.inkColor), // yazı rengi
            ornamentColor = sp.getInt("ornament_color", d.ornamentColor), // süs rengi
            texture = sp.getBoolean("texture", d.texture), // doku
            frame = sp.getBoolean("frame", d.frame), // çerçeve
            font = QuranFont.of(sp.getString("font", null)), // yazı tipi
            mushafLayout = sp.getBoolean("mushaf_layout", d.mushafLayout), // sayfa düzeni
            keepScreenOn = sp.getBoolean("keep_on", d.keepScreenOn), // ekran açık
        )
    }

    private fun writeSettings(s: ReaderSettings) { // diske ayar yazma
        sp.edit()
            .putFloat("scale", s.scale) // boyut
            .putString("preset", s.presetId) // hazır görünüm
            .putInt("page_color", s.pageColor) // sayfa rengi
            .putInt("ink_color", s.inkColor) // yazı rengi
            .putInt("ornament_color", s.ornamentColor) // süs rengi
            .putBoolean("texture", s.texture) // doku
            .putBoolean("frame", s.frame) // çerçeve
            .putString("font", s.font.id) // yazı tipi
            .putBoolean("mushaf_layout", s.mushafLayout) // sayfa düzeni
            .putBoolean("keep_on", s.keepScreenOn) // ekran açık
            .apply() // arka planda yazılıyor
    }

    private fun readBookmarks(): List<Bookmark> = // diskten yer imi okuma ("sayfa:zaman" biçiminde)
        (sp.getStringSet("bookmarks", emptySet()) ?: emptySet()).mapNotNull { k ->
            val p = k.split(':') // parçalara ayrılıyor
            val sayfa = p.getOrNull(0)?.toIntOrNull() ?: return@mapNotNull null // sayfa
            Bookmark(sayfa, p.getOrNull(1)?.toLongOrNull() ?: 0L) // yer imi
        }.sortedByDescending { it.createdAt } // en yeni üstte

    private fun writeBookmarks() = // diske yer imi yazma
        sp.edit().putStringSet("bookmarks", _bookmarks.value.map { "${it.page}:${it.createdAt}" }.toSet()).apply()
}
