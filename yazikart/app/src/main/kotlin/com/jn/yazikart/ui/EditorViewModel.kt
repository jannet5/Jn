package com.jn.yazikart.ui // arayüz paketi

import android.app.Application // uygulama bağlamı
import android.graphics.Bitmap // arka plan resmi
import android.graphics.drawable.BitmapDrawable // Coil'in döndürdüğü resim türü
import android.graphics.BitmapFactory // gömülü (data:) resmi çözmek için
import android.net.Uri // galeriden seçilen resmin adresi
import android.util.Base64 // gömülü resmin şifrelemesini çözmek için
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
import com.jn.yazikart.search.NetworkMonitor // internet takibi
import android.media.MediaMetadataRetriever // videonun süresini okumak için
import androidx.media3.common.MediaItem // oynatıcıya verilecek video
import androidx.media3.common.Player // oynatıcı ayarları
import androidx.media3.exoplayer.ExoPlayer // video oynatıcı (önizleme)
import com.jn.yazikart.data.Clips // video hesapları
import com.jn.yazikart.data.VideoClip // video parçası
import com.jn.yazikart.video.VideoDownloader // video indirme
import com.jn.yazikart.video.VideoExporter // video dışa aktarma
import java.io.File // dosyalar
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

// Ana ekranın tüm durumunu ve işlerini yöneten sınıf
class EditorViewModel(app: Application) : AndroidViewModel(app) {

    private val store = StyleStore(app) // ayar saklayıcı
    private val network = NetworkMonitor(app) // internet takipçisi

    private val _style = MutableStateFlow(store.load()) // son kullanılan stil ile başlanıyor
    val style: StateFlow<PostStyle> = _style.asStateFlow() // ekranın okuyacağı stil

    private val _image = MutableStateFlow<Bitmap?>(null) // arka plan resmi (başta yok)
    val image: StateFlow<Bitmap?> = _image.asStateFlow() // ekranın okuyacağı resim

    private val _busy = MutableStateFlow<String?>(null) // uzun süren iş varsa açıklaması ("Kaydediliyor…")
    val busy: StateFlow<String?> = _busy.asStateFlow() // ekranın okuyacağı meşguliyet

    private val _message = MutableStateFlow<String?>(null) // kullanıcıya gösterilecek kısa mesaj
    val message: StateFlow<String?> = _message.asStateFlow() // ekranın okuyacağı mesaj

    // İnternet var mı (canlı)
    val online: StateFlow<Boolean> = network.online
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), network.isOnline()) // akış durum olarak tutuluyor

    private var imageJob: Job? = null // süren resim indirme
    private var videoJob: Job? = null // süren video indirme / dışa aktarma

    private val _clips = MutableStateFlow<List<VideoClip>>(emptyList()) // arka plandaki videolar (sırayla)
    val clips: StateFlow<List<VideoClip>> = _clips.asStateFlow() // ekranın okuyacağı videolar

    // Önizleme oynatıcısı: videolar arka arkaya, seçilen aralıklarla, sürekli tekrar oynar
    val player: ExoPlayer = ExoPlayer.Builder(app).build().apply { repeatMode = Player.REPEAT_MODE_ALL } // tekrar modunda

    private val videoDir = File(app.cacheDir, "videolar") // internetten inen videoların klasörü

    // Oynatıcıyı güncel video listesine göre yeniden kurar
    private fun syncPlayer(seekToIndex: Int = 0) {
        val c = _clips.value // güncel liste
        if (c.isEmpty()) { player.stop(); player.clearMediaItems(); return } // video yoksa oynatıcı boş
        player.setMediaItems( // her parça kırpma aralığıyla
            c.map { clip ->
                MediaItem.Builder().setUri(clip.uri) // dosya
                    .setClippingConfiguration(MediaItem.ClippingConfiguration.Builder().setStartPositionMs(clip.startMs).setEndPositionMs(clip.endMs).build()) // aralık
                    .build() // hazır
            },
            seekToIndex.coerceIn(0, c.lastIndex), 0L, // değişen parçanın başından oynat
        )
        player.prepare() // hazırla
        player.play() // oynat
    }

    // Galeriden ya da internetten gelen videoyu listenin sonuna ekler
    fun addVideo(uri: Uri) {
        if (_clips.value.size >= Clips.MAX_CLIPS) { _message.value = "En fazla ${Clips.MAX_CLIPS} video eklenebilir."; return } // sınır
        viewModelScope.launch { // iş başlıyor
            val duration = withContext(Dispatchers.IO) { // süre okuma arka planda
                runCatching { // hata olabilir
                    MediaMetadataRetriever().run { // video bilgisi okuyucu
                        try { setDataSource(getApplication(), uri); extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() } // süre (ms)
                        finally { release() } // okuyucu kapatılıyor
                    }
                }.getOrNull() // okunamazsa null
            }
            if (duration == null || duration < Clips.MIN_LENGTH_MS) { _message.value = "Bu video açılamadı, başka birini dene."; return@launch } // geçersiz
            _image.value = null // video varken resim kullanılmaz
            _clips.update { it + VideoClip(uri.toString(), duration) } // listeye eklendi
            syncPlayer(_clips.value.lastIndex) // yeni videodan oynamaya başla
            _message.value = "Video eklendi (${Clips.format(duration)})" // bilgi
        }
    }

    // Bir videonun oynama aralığını değiştirir (kaydırırken çağrılır; oynatıcı bırakınca güncellenir)
    fun setClipRange(index: Int, startMs: Long, endMs: Long) {
        _clips.update { list -> list.mapIndexed { i, c -> if (i == index) Clips.clampRange(c, startMs, endMs) else c } } // aralık düzeltilip yazılıyor
    }

    // Aralık ayarı bitince oynatıcıyı o videodan başlatır
    fun commitClipRange(index: Int) = syncPlayer(index) // değişen parçadan oynat

    // Videoyu bir sıra yukarı alır
    fun moveClipUp(index: Int) { _clips.update { Clips.moveUp(it, index) }; syncPlayer(index - 1) } // sıra değişti

    // Videoyu listeden çıkarır (internetten indiyse dosyasını da siler)
    fun removeClip(index: Int) {
        val c = _clips.value.getOrNull(index) ?: return // yoksa bir şey yapma
        _clips.update { it.filterIndexed { i, _ -> i != index } } // listeden çıkarıldı
        Uri.parse(c.uri).path?.let { p -> if (p.startsWith(videoDir.absolutePath)) File(p).delete() } // indirilen dosya siliniyor
        syncPlayer() // oynatıcı güncelleniyor
    }

    // Tüm videoları kaldırır
    private fun clearVideos() {
        _clips.value = emptyList() // liste boş
        syncPlayer() // oynatıcı durdu
        videoDir.listFiles()?.forEach { it.delete() } // indirilen dosyalar silindi
    }

    // İnternetteki videoyu indirip ekler (Pexels gibi sitelerin "İndir" düğmesinden gelen adres)
    fun downloadVideo(url: String, userAgent: String, cookies: String?, onDone: () -> Unit) {
        if (videoJob?.isActive == true) return // zaten bir iş sürüyorsa bekle
        videoJob = viewModelScope.launch { // iş başlıyor
            _busy.value = "Video iniyor…" // gösterge
            val dest = File(videoDir, "video_${System.currentTimeMillis()}.mp4") // hedef dosya
            val result = runCatching { // hata yakalanıyor
                VideoDownloader.download(url, userAgent, cookies, dest) { done, total -> // indiriliyor
                    _busy.value = if (total > 0) "Video iniyor ${done / 1_048_576}/${total / 1_048_576} MB" else "Video iniyor ${done / 1_048_576} MB" // ilerleme
                }
            }
            _busy.value = null // gösterge kapandı
            result.fold( // sonuca göre
                { f -> addVideo(Uri.fromFile(f)); onDone() }, // eklendi, ekran kapanıyor
                { e -> dest.delete(); _message.value = "Video indirilemedi: ${e.message}" }, // hata
            )
        }
    }

    // Videoyu (yazısıyla birlikte) MP4 olarak hazırlar
    private suspend fun exportVideo(out: File): File {
        player.pause() // dışa aktarırken önizleme durur (telefon yorulmasın)
        val s = _style.value // güncel stil
        val tf = FontCatalog.typeface(getApplication<Application>().assets, s.fontIndex, s.bold) // yazı tipi
        val overlay = withContext(Dispatchers.Default) { PostRenderer.renderOverlay(s, tf, FontCatalog.needsFakeBold(s.fontIndex, s.bold)) } // yazı katmanı
        out.parentFile?.mkdirs() // klasör hazırlanıyor
        return VideoExporter.export(getApplication(), _clips.value, overlay, out) { p -> _busy.value = "Video hazırlanıyor %$p" } // birleştir + kırp + yazıyı bas
    }

    // Videoyu galeriye kaydeder
    private fun saveVideo() {
        if (videoJob?.isActive == true) return // iş sürüyorsa bekle
        videoJob = viewModelScope.launch { // ana iş parçacığında (video işleyici bunu ister)
            _busy.value = "Video hazırlanıyor…" // gösterge
            val tmp = File(getApplication<Application>().cacheDir, "disari/YaziKart.mp4") // geçici çıktı
            val msg = runCatching { // hata yakalanıyor
                exportVideo(tmp) // hazırla
                withContext(Dispatchers.IO) { ImageExporter.saveVideoToGallery(getApplication(), tmp) } // galeriye kopyala
            }.fold({ "Video galeriye kaydedildi (Filmler/YaziKart)" }, { "Video kaydedilemedi: ${it.message}" }) // sonuç
            tmp.delete() // geçici dosya silindi
            _busy.value = null // gösterge kapandı
            _message.value = msg // mesaj
            player.play() // önizleme devam
        }
    }

    // Videoyu paylaşır
    private fun shareVideo() {
        if (videoJob?.isActive == true) return // iş sürüyorsa bekle
        videoJob = viewModelScope.launch { // ana iş parçacığında
            _busy.value = "Video hazırlanıyor…" // gösterge
            val dir = File(getApplication<Application>().cacheDir, "paylas") // paylaşım klasörü
            withContext(Dispatchers.IO) { dir.listFiles()?.forEach { it.delete() } } // eski paylaşımlar temizleniyor
            val out = File(dir, ImageExporter.fileName(com.jn.yazikart.data.ExportFormat.PNG).replace(".png", ".mp4")) // tarihli ad
            val result = runCatching { exportVideo(out) } // hazırla
            _busy.value = null // gösterge kapandı
            result.fold( // sonuca göre
                { f -> runCatching { ImageExporter.shareVideo(getApplication(), f) }.onFailure { _message.value = "Paylaşılamadı: ${it.message}" } }, // paylaş
                { _message.value = "Video hazırlanamadı: ${it.message}" }, // hata
            )
        }
    }

    // Uygulama arka plana gidince / dönünce önizlemeyi durdur / sürdür
    fun pausePreview() = player.pause() // durdur
    fun resumePreview() { if (_clips.value.isNotEmpty() && videoJob?.isActive != true) player.play() } // sürdür

    override fun onCleared() { player.release(); super.onCleared() } // oynatıcı kapatılıyor

    // Stili değiştirir ve kaydeder
    fun updateStyle(change: (PostStyle) -> PostStyle) {
        _style.update(change) // stil güncelleniyor
        store.save(_style.value) // telefona kaydediliyor
    }

    // Her şeyi sıfırlar: uygulamayı ilk kez açmış gibi (siyah zemin, beyaz yazı, boş metin, resim yok)
    fun resetAll() {
        imageJob?.cancel() // süren resim indirme durduruluyor
        videoJob?.cancel() // süren video işi durduruluyor
        clearVideos() // videolar kaldırılıyor
        _style.value = PostStyle() // stil varsayılana dönüyor
        store.save(_style.value) // varsayılan stil kaydediliyor (sonraki açılışta da temiz)
        _image.value = null // arka plan resmi kaldırılıyor
        _busy.value = null // bekleme göstergesi kapanıyor
        _message.value = "Baştan başlandı" // kısa bilgi mesajı
    }

    // Mesaj gösterildikten sonra temizlenir
    fun messageShown() { _message.value = null }

    // Google Görseller'de basılı tutulan görseli arka plan yapar.
    // Adres "data:image/..." ise (Google'ın küçük önizlemesi) görsel zaten sayfanın içindedir, doğrudan çözülür.
    fun pickFromWeb(url: String, onDone: () -> Unit) {
        if (url.startsWith("data:image")) { // gömülü küçük görsel
            val bmp = runCatching { // çözmeyi dene
                val bytes = Base64.decode(url.substringAfter(","), Base64.DEFAULT) // virgülden sonrası base64 veri
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size) // resme çevriliyor
            }.getOrNull() // hata olursa null
            if (bmp != null) { // başarılıysa
                clearVideos() // resim seçildi: videolar kaldırılır
                _image.value = bmp // arka plan oldu
                _message.value = "Küçük önizleme alındı. Daha net olması için görsele dokunup büyüyünce basılı tut." // ipucu
                onDone() // Google ekranı kapanıyor
            } else _message.value = "Bu görsel alınamadı, başka birini dene." // hata
            return // iş bitti
        }
        loadBackground(url, onDone) // normal adres: indiriliyor
    }

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
                clearVideos() // resim seçildi: videolar kaldırılır
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
        if (_clips.value.isNotEmpty()) { saveVideo(); return } // video varsa MP4 kaydedilir
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
        if (_clips.value.isNotEmpty()) { shareVideo(); return } // video varsa MP4 paylaşılır
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
