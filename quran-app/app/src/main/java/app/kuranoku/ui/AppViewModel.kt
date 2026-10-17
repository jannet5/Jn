package app.kuranoku.ui // uygulama durumu

import android.app.Application // uygulama bağlamı
import androidx.lifecycle.AndroidViewModel // bağlamlı ViewModel
import androidx.lifecycle.viewModelScope // ViewModel eşzamanlılık alanı
import app.kuranoku.data.Prefs // kalıcı ayarlar
import app.kuranoku.data.Quran // Kur'an
import app.kuranoku.data.ReaderSettings // ayarlar
import app.kuranoku.data.SearchEngine // arama
import app.kuranoku.ui.reader.AyahRef // ayet referansı
import kotlinx.coroutines.Dispatchers // iş parçacıkları
import kotlinx.coroutines.Job // iş
import kotlinx.coroutines.delay // bekleme
import kotlinx.coroutines.flow.MutableStateFlow // değişebilir akış
import kotlinx.coroutines.flow.StateFlow // salt okunur akış
import kotlinx.coroutines.launch // başlatma
import kotlinx.coroutines.withContext // bağlam değiştirme

/** Kur'an verisinin yüklenme durumu. */
sealed interface LoadState {
    data object Loading : LoadState // yükleniyor
    data class Ready(val quran: Quran, val search: SearchEngine) : LoadState // hazır
    data class Error(val message: String) : LoadState // hata
}

/** Sayfaya gitme isteği (aramadan ya da içindekilerden). */
data class NavRequest(val page: Int, val highlight: AyahRef?, val id: Long = System.nanoTime()) // her istek benzersiz

/** Uygulamanın tek ViewModel'i. */
class AppViewModel(app: Application) : AndroidViewModel(app) {
    val prefs = Prefs(app) // kalıcı depo
    val state = MutableStateFlow<LoadState>(LoadState.Loading) // yükleme durumu
    val settings: StateFlow<ReaderSettings> = prefs.settings // ayarlar
    val bookmarks = prefs.bookmarks // yer imleri
    val nav = MutableStateFlow<NavRequest?>(null) // bekleyen sayfa isteği
    val currentPage = MutableStateFlow(prefs.lastPage) // şu an açık sayfa
    private var kayit: Job? = null // gecikmeli kayıt işi

    init { load() } // açılışta yükle

    /** Kur'an'ı varlık dosyalarından arka planda yükler. */
    fun load() {
        state.value = LoadState.Loading // yükleniyor
        viewModelScope.launch {
            state.value = try {
                val q = withContext(Dispatchers.IO) { // disk işi arka planda
                    val a = getApplication<Application>().assets // varlıklar
                    a.open("surahs.tsv").use { s -> a.open("quran.tsv").use { k -> a.open("mushaf.tsv").use { m -> Quran.parse(s, k, m) } } } // ayrıştırma (metin + mushaf düzeni)
                }
                LoadState.Ready(q, SearchEngine(q)) // hazır
            } catch (e: Exception) {
                LoadState.Error("Kur'an metni okunamadı. Uygulamayı yeniden açmayı dene; sorun sürerse uygulamayı yeniden yükle.") // hata mesajı
            }
        }
    }

    /** Okunan sayfa değişti: kaldığı yer olarak kaydedilir. */
    fun onPageChanged(page: Int) {
        currentPage.value = page // bellekte
        prefs.lastPage = page // diskte
    }

    /** Sayfaya git. */
    fun goTo(page: Int, highlight: AyahRef? = null) { nav.value = NavRequest(page, highlight) }

    /** İstek işlendi. */
    fun consumeNav() { nav.value = null }

    /** Ayar değişikliği: anında uygulanır, diske 400 ms sonra yazılır (kaydırıcı çekilirken diski yormamak için). */
    fun updateSettings(s: ReaderSettings) {
        prefs.update(s, persist = false) // anında
        kayit?.cancel() // önceki bekleyen kayıt iptal
        kayit = viewModelScope.launch { delay(400); prefs.flush() } // gecikmeli kayıt
    }

    /** Yer imi ekle/kaldır. */
    fun toggleBookmark(page: Int) = prefs.toggleBookmark(page)
}
