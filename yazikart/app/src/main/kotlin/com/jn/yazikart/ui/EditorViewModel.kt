package com.jn.yazikart.ui // arayüz paketi

import android.app.Application // uygulama bağlamı
import android.graphics.Bitmap // arka plan resmi
import android.graphics.drawable.BitmapDrawable // Coil'in döndürdüğü resim türü
import android.net.Uri // galeriden seçilen resmin adresi
import androidx.lifecycle.AndroidViewModel // ekran dönse de yaşayan durum tutucu
import androidx.lifecycle.viewModelScope // ViewModel'e bağlı iş kapsamı
import coil.imageLoader // uygulamanın görsel yükleyicisi
import coil.request.ImageRequest // görsel indirme isteği
import coil.request.SuccessResult // başarılı indirme sonucu
import com.jn.yazikart.data.FontCatalog // yazı tipleri
import com.jn.yazikart.data.ImageExporter // kaydet / paylaş
import com.jn.yazikart.data.PostStyle // görsel ayarları
import com.jn.yazikart.data.StyleStore // ayarları saklama
import com.jn.yazikart.render.PostRenderer // görsel çizici
import com.jn.yazikart.search.ImageResult // arama sonucu
import com.jn.yazikart.search.ImageSearch // görsel arama
import com.jn.yazikart.search.NetworkMonitor // internet takibi
import kotlinx.coroutines.Dispatchers // arka plan iş parçacıkları
import kotlinx.coroutines.Job // iptal edilebilir iş
import kotlinx.coroutines.flow.MutableStateFlow // değiştirilebilir durum
import kotlinx.coroutines.flow.SharingStarted // akış paylaşım ayarı
import kotlinx.coroutines.flow.StateFlow // okunabilir durum
import kotlinx.coroutines.flow.asStateFlow // salt okunur hale çevirme
import kotlinx.coroutines.flow.stateIn // akışı duruma çevirme
import kotlinx.coroutines.flow.update // durumu güvenle güncelleme
import kotlinx.coroutines.launch // iş başlatma
import kotlinx.coroutines.withContext // işi başka iş parçacığına alma

// Arama ekranının durumu
data class SearchState(
    val query: String = "", // yazılan kelime
    val loading: Boolean = false, // aranıyor mu
    val results: List<ImageResult> = emptyList(), // sonuçlar
    val error: String? = null, // hata mesajı
    val searched: Boolean = false, // en az bir kez arandı mı (boş sonucu ayırt etmek için)
)

// Ana ekranın tüm durumunu ve işlerini yöneten sınıf
class EditorViewModel(app: Application) : AndroidViewModel(app) {

    private val store = StyleStore(app) // ayar saklayıcı
    private val searcher = ImageSearch() // görsel arayıcı
    private val network = NetworkMonitor(app) // internet takipçisi

    private val _style = MutableStateFlow(store.load()) // son kullanılan stil ile başlanıyor
    val style: StateFlow<PostStyle> = _style.asStateFlow() // ekranın okuyacağı stil

    private val _image = MutableStateFlow<Bitmap?>(null) // arka plan resmi (başta yok)
    val image: StateFlow<Bitmap?> = _image.asStateFlow() // ekranın okuyacağı resim

    private val _search = MutableStateFlow(SearchState()) // arama durumu
    val search: StateFlow<SearchState> = _search.asStateFlow() // ekranın okuyacağı arama durumu

    private val _busy = MutableStateFlow<String?>(null) // uzun süren iş varsa açıklaması ("Kaydediliyor…")
    val busy: StateFlow<String?> = _busy.asStateFlow() // ekranın okuyacağı meşguliyet

    private val _message = MutableStateFlow<String?>(null) // kullanıcıya gösterilecek kısa mesaj
    val message: StateFlow<String?> = _message.asStateFlow() // ekranın okuyacağı mesaj

    // İnternet var mı (canlı)
    val online: StateFlow<Boolean> = network.online
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), network.isOnline()) // akış durum olarak tutuluyor

    private var searchJob: Job? = null // süren arama (yenisi gelirse iptal için)
    private var imageJob: Job? = null // süren resim indirme

    // Stili değiştirir ve kaydeder
    fun updateStyle(change: (PostStyle) -> PostStyle) {
        _style.update(change) // stil güncelleniyor
        store.save(_style.value) // telefona kaydediliyor
    }

    // Her şeyi sıfırlar: uygulamayı ilk kez açmış gibi (siyah zemin, beyaz yazı, boş metin, resim yok)
    fun resetAll() {
        searchJob?.cancel() // süren arama durduruluyor
        imageJob?.cancel() // süren resim indirme durduruluyor
        _style.value = PostStyle() // stil varsayılana dönüyor
        store.save(_style.value) // varsayılan stil kaydediliyor (sonraki açılışta da temiz)
        _image.value = null // arka plan resmi kaldırılıyor
        _search.value = SearchState() // arama kutusu ve sonuçlar temizleniyor
        _busy.value = null // bekleme göstergesi kapanıyor
        _message.value = "Baştan başlandı" // kısa bilgi mesajı
    }

    // Mesaj gösterildikten sonra temizlenir
    fun messageShown() { _message.value = null }

    // Arama kutusundaki yazı değişti
    fun setQuery(q: String) { _search.update { it.copy(query = q) } }

    // Görsel aramasını başlatır
    fun runSearch() {
        val q = _search.value.query.trim() // kelime boşluklardan temizleniyor
        if (q.isEmpty()) return // boşsa aranmıyor
        if (!network.isOnline()) { // internet yoksa
            _search.update { it.copy(error = "İnternet bağlantısı yok. Wi-Fi ya da mobil veriyi aç.", loading = false) } // hata gösteriliyor
            return // arama yapılmıyor
        }
        searchJob?.cancel() // önceki arama iptal
        searchJob = viewModelScope.launch { // yeni arama başlıyor
            _search.update { it.copy(loading = true, error = null) } // yükleniyor durumu
            try { // arama deneniyor
                val results = searcher.search(q) // iki kaynakta aranıyor
                _search.update { it.copy(loading = false, results = results, searched = true) } // sonuçlar yazılıyor
            } catch (e: Exception) { // hata olursa
                _search.update { it.copy(loading = false, error = "Arama yapılamadı. Bağlantını kontrol edip tekrar dene.", searched = true) } // hata mesajı
            }
        }
    }

    // Aramadan seçilen görseli indirip arka plan yapar
    fun pickResult(r: ImageResult, onDone: () -> Unit) = loadBackground(r.fullUrl, onDone) // tam boy resim indiriliyor

    // Galeriden seçilen resmi arka plan yapar
    fun pickFromGallery(uri: Uri) = loadBackground(uri, {}) // telefondaki resim yükleniyor

    // Arka plan resmini kaldırır (sadece renk kalır)
    fun clearImage() { _image.value = null }

    // Adres ya da Uri'den resmi yükler (en fazla 2048 piksel; bellek taşmasın diye)
    private fun loadBackground(data: Any, onDone: () -> Unit) {
        imageJob?.cancel() // önceki indirme iptal
        imageJob = viewModelScope.launch { // indirme başlıyor
            _busy.value = "Görsel yükleniyor…" // ekranda bekleme göstergesi
            val ctx = getApplication<Application>() // uygulama bağlamı
            val req = ImageRequest.Builder(ctx) // indirme isteği
                .data(data) // adres
                .size(2048) // en fazla 2048 piksel
                .allowHardware(false) // kaydederken üzerine çizebilmek için normal bellek
                .build() // istek hazır
            val result = ctx.imageLoader.execute(req) // indiriliyor
            val bmp = ((result as? SuccessResult)?.drawable as? BitmapDrawable)?.bitmap // resim alınıyor
            _busy.value = null // bekleme bitti
            if (bmp != null) { // başarılıysa
                _image.value = bmp // arka plan oldu
                onDone() // arama ekranı kapatılıyor
            } else { // başarısızsa
                _message.value = "Görsel indirilemedi, başka bir tane dene." // hata mesajı
            }
        }
    }

    // Tam boy görseli üretir
    private fun render(): Bitmap {
        val s = _style.value // güncel stil
        val tf = FontCatalog.typeface(getApplication<Application>().assets, s.fontIndex, s.bold) // yazı tipi
        return PostRenderer.renderBitmap(s, tf, FontCatalog.needsFakeBold(s.fontIndex, s.bold), _image.value) // çizim
    }

    // Galeriye kaydeder
    fun saveToGallery() {
        if (_style.value.text.isBlank()) { _message.value = "Önce bir şey yaz."; return } // yazı yoksa uyarı
        viewModelScope.launch { // iş başlıyor
            _busy.value = "Kaydediliyor…" // bekleme göstergesi
            val msg = withContext(Dispatchers.Default) { // ağır iş arka planda
                runCatching { // hata yakalanıyor
                    ImageExporter.saveToGallery(getApplication(), render(), _style.value.format) // çiz + kaydet
                }.fold({ "Galeriye kaydedildi (Resimler/YaziKart)" }, { "Kaydedilemedi: ${it.message}" }) // sonuç mesajı
            }
            _busy.value = null // bekleme bitti
            _message.value = msg // mesaj gösteriliyor
        }
    }

    // Paylaşma penceresini açar
    fun share() {
        if (_style.value.text.isBlank()) { _message.value = "Önce bir şey yaz."; return } // yazı yoksa uyarı
        viewModelScope.launch { // iş başlıyor
            _busy.value = "Hazırlanıyor…" // bekleme göstergesi
            val result = withContext(Dispatchers.Default) { runCatching { render() } } // görsel arka planda çiziliyor
            _busy.value = null // bekleme bitti
            result.fold( // sonuca göre
                { bmp -> // başarılıysa
                    runCatching { ImageExporter.share(getApplication(), bmp, _style.value.format) } // paylaşma açılıyor
                        .onFailure { _message.value = "Paylaşılamadı: ${it.message}" } // hata olursa mesaj
                },
                { _message.value = "Görsel hazırlanamadı: ${it.message}" }, // çizim hatası
            )
        }
    }
}
