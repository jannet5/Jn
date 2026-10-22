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
import com.jn.yazikart.data.VideoClip // video / fotoğraf parçası
import com.jn.yazikart.data.MusicTrack // müzik
import com.jn.yazikart.video.Thumbs // videodan küçük kareler
import androidx.media3.exoplayer.image.ImageOutput // önizlemede fotoğraf görüntüsü
import android.provider.OpenableColumns // dosya adı okumak için
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

    private val _clips = MutableStateFlow<List<VideoClip>>(emptyList()) // sıradaki videolar ve fotoğraflar
    val clips: StateFlow<List<VideoClip>> = _clips.asStateFlow() // ekranın okuyacağı sıra

    private val _thumbs = MutableStateFlow<Map<String, Bitmap>>(emptyMap()) // her parçanın küçük resmi (adres → resim)
    val thumbs: StateFlow<Map<String, Bitmap>> = _thumbs.asStateFlow() // ekranın okuyacağı küçük resimler

    private val _music = MutableStateFlow<MusicTrack?>(null) // altta çalan müzik
    val music: StateFlow<MusicTrack?> = _music.asStateFlow() // ekranın okuyacağı müzik

    private val _videoSound = MutableStateFlow(true) // videoların kendi sesi açık mı
    val videoSound: StateFlow<Boolean> = _videoSound.asStateFlow() // ekranın okuyacağı ses ayarı

    private val _photoFrame = MutableStateFlow<Bitmap?>(null) // önizlemede o an gösterilen fotoğraf
    val photoFrame: StateFlow<Bitmap?> = _photoFrame.asStateFlow() // ekranın okuyacağı fotoğraf karesi
    private val _showingPhoto = MutableStateFlow(false) // önizlemede şu an fotoğraf mı oynuyor
    val showingPhoto: StateFlow<Boolean> = _showingPhoto.asStateFlow() // ekranın okuyacağı durum

    // Müzik oynatıcısı: önizlemede sıranın altında çalar
    val musicPlayer: ExoPlayer = ExoPlayer.Builder(app).build().apply { repeatMode = Player.REPEAT_MODE_ONE } // şarkı bitince başa döner

    // Önizleme oynatıcısı: videolar ve fotoğraflar arka arkaya, seçilen sürelerle, sürekli tekrar oynar
    val player: ExoPlayer = ExoPlayer.Builder(app).build().apply {
        repeatMode = Player.REPEAT_MODE_ALL // sürekli tekrar
        setImageOutput(object : ImageOutput { // fotoğraf sırası gelince görüntüsü buraya gelir
            override fun onImageAvailable(presentationTimeUs: Long, bitmap: Bitmap) { _photoFrame.value = bitmap } // ekranda gösterilecek fotoğraf
            override fun onDisabled() {} // bir şey yapma
        })
        addListener(object : Player.Listener {
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                _message.value = "Önizleme oynatılamadı. Kaydetmeyi yine de deneyebilirsin." // kullanıcıya söyleniyor (siyah ekranda kalmasın)
            }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _showingPhoto.value = mediaItem?.mediaId?.startsWith("foto") == true // sıradaki parça fotoğraf mı
                if (currentMediaItemIndex == 0 && reason != Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED) restartMusic() // sıra başa dönünce müzik de başa döner
            }
        })
    }

    private val mediaDir = File(app.cacheDir, "videolar") // uygulamanın kopyaladığı/indirdiği videolar ve fotoğraflar

    // Oynatıcıyı güncel sıraya göre yeniden kurar
    private fun syncPlayer(seekToIndex: Int = 0) {
        val c = _clips.value // güncel liste
        if (c.isEmpty()) { player.stop(); player.clearMediaItems(); musicPlayer.pause(); _showingPhoto.value = false; return } // boşsa oynatıcı boş
        player.setMediaItems( // her parça
            c.mapIndexed { i, clip ->
                if (clip.isPhoto) MediaItem.Builder().setUri(clip.uri).setMediaId("foto$i").setImageDurationMs(clip.lengthMs).build() // fotoğraf: belirlenen süre
                else MediaItem.Builder().setUri(clip.uri).setMediaId("video$i") // video
                    .setClippingConfiguration(MediaItem.ClippingConfiguration.Builder().setStartPositionMs(clip.startMs).setEndPositionMs(clip.endMs).build()) // aralık
                    .build() // hazır
            },
            seekToIndex.coerceIn(0, c.lastIndex), 0L, // değişen parçanın başından oynat
        )
        player.volume = if (_videoSound.value) 1f else 0f // video sesi ayarı
        player.prepare() // hazırla
        player.play() // oynat
        restartMusic() // müzik de baştan
    }

    // Müziği seçilen saniyeden yeniden başlatır (sıra başa dönünce)
    private fun restartMusic() {
        val m = _music.value ?: return // müzik yoksa bir şey yapma
        if (_clips.value.isEmpty()) return // sıra yoksa müzik çalmaz
        musicPlayer.seekTo(m.startMs) // seçilen saniye
        musicPlayer.volume = m.volume // ses seviyesi
        if (player.playWhenReady) musicPlayer.play() // önizleme oynuyorsa müzik de çalar
    }

    // Galeriden seçilen (ya da paylaşılan) fotoğrafları ve videoları sıranın sonuna ekler
    fun addMedia(uris: List<Uri>, copyVideos: Boolean = false) {
        viewModelScope.launch { // iş başlıyor
            var added = 0 // eklenen sayısı
            for (uri in uris) { // her dosya
                if (_clips.value.size >= Clips.MAX_CLIPS) { _message.value = "En fazla ${Clips.MAX_CLIPS} parça eklenebilir."; break } // sınır
                val type = getApplication<Application>().contentResolver.getType(uri).orEmpty() // dosya türü
                val isImage = type.startsWith("image/") || (type.isEmpty() && uri.toString().substringBefore('?').lowercase().let { it.endsWith(".jpg") || it.endsWith(".jpeg") || it.endsWith(".png") || it.endsWith(".webp") }) // fotoğraf mı
                val ok = if (isImage) addPhotoInternal(uri) else addVideoInternal(uri, copyVideos) // türüne göre ekleniyor
                if (ok) added++ // sayaç
            }
            if (added > 0) { // en az biri eklendiyse
                _image.value = null // sıra varken tek arka plan resmi kullanılmaz
                syncPlayer(_clips.value.lastIndex - added + 1) // yeni eklenenlerin ilkinden oynat
                _message.value = if (added == 1) "Eklendi" else "$added parça eklendi" // bilgi
            } else if (uris.isNotEmpty()) _message.value = "Bu dosya açılamadı, başka birini dene." // hiçbiri eklenemedi
        }
    }

    // Tek video ekler (internetten inen)
    fun addVideo(uri: Uri) = addMedia(listOf(uri)) // ortak yol

    // Fotoğrafı 1920 piksele küçültüp uygulamanın klasörüne kopyalar ve sıraya ekler (telefon yorulmasın, dosya hafif olsun)
    private suspend fun addPhotoInternal(data: Any): Boolean {
        val ctx = getApplication<Application>() // uygulama bağlamı
        val req = ImageRequest.Builder(ctx).data(data).size(1920).allowHardware(false).build() // en fazla 1920 piksel, döndürme (EXIF) düzeltilmiş
        val bmp = ((ctx.imageLoader.execute(req) as? SuccessResult)?.drawable as? BitmapDrawable)?.bitmap ?: return false // fotoğraf açılıyor
        val file = File(mediaDir, "foto_${System.nanoTime()}.jpg") // kopya dosyası
        withContext(Dispatchers.IO) { mediaDir.mkdirs(); file.outputStream().use { bmp.compress(Bitmap.CompressFormat.JPEG, 90, it) } } // kaydediliyor
        val uri = Uri.fromFile(file).toString() // adres
        _clips.update { it + Clips.photo(uri) } // sıraya eklendi (3 sn)
        _thumbs.update { it + (uri to scaled(bmp, 240)) } // küçük resmi
        return true // eklendi
    }

    // Videonun süresini okuyup sıraya ekler (paylaşılan videolar kaybolmasın diye kopyalanabilir)
    private suspend fun addVideoInternal(source: Uri, copy: Boolean): Boolean {
        val ctx = getApplication<Application>() // uygulama bağlamı
        val uri = if (!copy) source else withContext(Dispatchers.IO) { // gerekirse kopya
            runCatching {
                mediaDir.mkdirs() // klasör
                val f = File(mediaDir, "video_${System.nanoTime()}.mp4") // kopya dosyası
                ctx.contentResolver.openInputStream(source)!!.use { input -> f.outputStream().use { input.copyTo(it) } } // kopyalanıyor
                Uri.fromFile(f) // kopyanın adresi
            }.getOrNull()
        } ?: return false // kopyalanamadıysa
        val duration = withContext(Dispatchers.IO) { // süre okuma arka planda
            runCatching {
                MediaMetadataRetriever().run { // video bilgisi okuyucu
                    try { setDataSource(ctx, uri); extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() } // süre (ms)
                    finally { release() } // okuyucu kapatılıyor
                }
            }.getOrNull() // okunamazsa null
        }
        if (duration == null || duration < Clips.MIN_LENGTH_MS) return false // geçersiz
        val key = uri.toString() // adres
        _clips.update { it + VideoClip(key, duration) } // sıraya eklendi
        viewModelScope.launch(Dispatchers.IO) { Thumbs.frame(ctx, key, 0L, 240)?.let { b -> _thumbs.update { it + (key to b) } } } // küçük resim arka planda
        return true // eklendi
    }

    // Google Görseller'den seçilen görseli sıraya fotoğraf olarak ekler
    fun addPhotoFromWeb(url: String, onDone: () -> Unit) {
        if (_clips.value.size >= Clips.MAX_CLIPS) { _message.value = "En fazla ${Clips.MAX_CLIPS} parça eklenebilir."; return } // sınır
        viewModelScope.launch { // iş başlıyor
            _busy.value = "Görsel yükleniyor…" // gösterge
            val data: Any = if (url.startsWith("data:image")) runCatching { Base64.decode(url.substringAfter(","), Base64.DEFAULT) }.getOrDefault(ByteArray(0)) else url // gömülü ya da adres
            val ok = runCatching { addPhotoInternal(data) }.getOrDefault(false) // ekleniyor
            _busy.value = null // gösterge kapandı
            if (ok) { _image.value = null; syncPlayer(_clips.value.lastIndex); _message.value = "Fotoğraf sıraya eklendi"; onDone() } // başarılı
            else _message.value = "Görsel indirilemedi, başka bir tane dene." // hata
        }
    }

    // Küçük resim için ölçekler
    private fun scaled(b: Bitmap, maxPx: Int): Bitmap {
        val s = minOf(1f, maxPx.toFloat() / maxOf(b.width, b.height)) // oran
        return if (s >= 1f) b else Bitmap.createScaledBitmap(b, (b.width * s).toInt().coerceAtLeast(1), (b.height * s).toInt().coerceAtLeast(1), true) // küçültülmüş
    }

    // Kırpma penceresi için videodan kareler (arka planda)
    suspend fun stripFor(clip: VideoClip, count: Int): List<Bitmap?> = withContext(Dispatchers.IO) { Thumbs.strip(getApplication(), clip.uri, clip.durationMs, count, 200) } // şerit
    suspend fun frameAt(clip: VideoClip, ms: Long): Bitmap? = withContext(Dispatchers.IO) { Thumbs.frame(getApplication(), clip.uri, ms, 720, exact = true) } // tam kare

    // Bir parçanın aralığını / süresini değiştirir (kaydırırken çağrılır; bırakınca oynatıcı güncellenir)
    fun setClipRange(index: Int, startMs: Long, endMs: Long) {
        _clips.update { list -> list.mapIndexed { i, c -> if (i == index) Clips.clampRange(c, startMs, endMs) else c } } // aralık düzeltilip yazılıyor
    }

    // Aralık ayarı bitince oynatıcıyı o parçadan başlatır
    fun commitClipRange(index: Int) = syncPlayer(index) // değişen parçadan oynat

    // Parçayı sola (öne) ya da sağa (arkaya) taşır
    fun moveClip(from: Int, to: Int) { _clips.update { Clips.move(it, from, to) }; syncPlayer(to.coerceIn(0, _clips.value.lastIndex.coerceAtLeast(0))) } // sıra değişti
    fun moveClipUp(index: Int) = moveClip(index, index - 1) // bir öne

    // Parçayı listeden çıkarır (uygulamanın kopyasıysa dosyasını da siler)
    fun removeClip(index: Int) {
        val c = _clips.value.getOrNull(index) ?: return // yoksa bir şey yapma
        _clips.update { it.filterIndexed { i, _ -> i != index } } // listeden çıkarıldı
        if (_clips.value.none { it.uri == c.uri }) { // başka yerde kullanılmıyorsa
            _thumbs.update { it - c.uri } // küçük resmi silindi
            Uri.parse(c.uri).path?.let { p -> if (p.startsWith(mediaDir.absolutePath)) File(p).delete() } // kopya dosya siliniyor
        }
        syncPlayer() // oynatıcı güncelleniyor
    }

    // Tüm sırayı kaldırır
    private fun clearVideos() {
        _clips.value = emptyList() // liste boş
        _thumbs.value = emptyMap() // küçük resimler boş
        syncPlayer() // oynatıcı durdu
        mediaDir.listFiles()?.forEach { it.delete() } // kopyalar silindi
    }

    // Müzik seçer (telefondaki ses dosyası)
    fun setMusic(uri: Uri) {
        viewModelScope.launch { // iş başlıyor
            val ctx = getApplication<Application>() // uygulama bağlamı
            val info = withContext(Dispatchers.IO) { // bilgiler arka planda
                runCatching {
                    val name = ctx.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c -> if (c.moveToFirst()) c.getString(0) else null } ?: "Müzik" // dosya adı
                    val dur = MediaMetadataRetriever().run { try { setDataSource(ctx, uri); extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() } finally { release() } } // süre
                    name.substringBeforeLast('.') to dur // ad ve süre
                }.getOrNull()
            }
            val dur = info?.second // süre
            if (info == null || dur == null || dur < 1000) { _message.value = "Bu ses dosyası açılamadı."; return@launch } // geçersiz
            val m = MusicTrack(uri.toString(), info.first, dur) // müzik
            _music.value = m // kaydedildi
            musicPlayer.setMediaItem(MediaItem.fromUri(uri)) // oynatıcıya verildi
            musicPlayer.prepare() // hazırla
            restartMusic() // sıra varsa çalmaya başlar
            _message.value = "Müzik eklendi: ${m.name}" // bilgi
        }
    }

    // Müziğin başlangıç saniyesini değiştirir (kaydırırken)
    fun setMusicStart(ms: Long) { _music.update { it?.copy(startMs = ms.coerceIn(0L, (it.durationMs - 1000).coerceAtLeast(0L))) } } // sınır içinde
    // Başlangıç ayarı bitince müziği oradan çal
    fun commitMusicStart() = restartMusic() // yeniden başlat
    // Müzik sesi
    fun setMusicVolume(v: Float) { _music.update { it?.copy(volume = v.coerceIn(0f, 1f)) }; musicPlayer.volume = _music.value?.volume ?: 0f } // anında duyulur
    // Müziği kaldır
    fun removeMusic() { _music.value = null; musicPlayer.stop(); musicPlayer.clearMediaItems() } // sessiz
    // Videoların kendi sesi açık/kapalı
    fun setVideoSound(on: Boolean) { _videoSound.value = on; player.volume = if (on) 1f else 0f } // anında duyulur

    // İnternetteki videoyu indirip ekler (sitede oynayan videonun ya da sitenin "İndir" düğmesinin adresi)
    fun downloadVideo(url: String, userAgent: String, cookies: String?, referer: String?, onDone: () -> Unit) {
        if (videoJob?.isActive == true) return // zaten bir iş sürüyorsa bekle
        if (_clips.value.size >= Clips.MAX_CLIPS) { _message.value = "En fazla ${Clips.MAX_CLIPS} parça eklenebilir."; return } // sınır
        videoJob = viewModelScope.launch { // iş başlıyor
            _busy.value = "Video iniyor…" // gösterge
            val dest = File(mediaDir, "video_${System.currentTimeMillis()}.mp4") // hedef dosya
            val result = runCatching { // hata yakalanıyor
                VideoDownloader.download(url, userAgent, cookies, referer, dest) { done, total -> // indiriliyor
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
        player.pause(); musicPlayer.pause() // dışa aktarırken önizleme durur (telefon yorulmasın)
        val s = _style.value // güncel stil
        val tf = FontCatalog.typeface(getApplication<Application>().assets, s.fontIndex, s.bold) // yazı tipi
        val overlay = withContext(Dispatchers.Default) { PostRenderer.renderOverlay(s, tf, FontCatalog.needsFakeBold(s.fontIndex, s.bold)) } // yazı katmanı
        out.parentFile?.mkdirs() // klasör hazırlanıyor
        return VideoExporter.export(getApplication(), _clips.value, overlay, out, _music.value, _videoSound.value) { p -> _busy.value = "Video hazırlanıyor %$p" } // birleştir + kırp + yazıyı bas + müzik
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
            resumePreview() // önizleme devam
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
            resumePreview() // önizleme devam
        }
    }

    // Uygulama arka plana gidince / dönünce önizlemeyi durdur / sürdür
    fun pausePreview() { player.pause(); musicPlayer.pause() } // durdur
    fun resumePreview() { if (_clips.value.isNotEmpty() && videoJob?.isActive != true) { player.play(); if (_music.value != null) musicPlayer.play() } } // sürdür

    override fun onCleared() { player.release(); musicPlayer.release(); super.onCleared() } // oynatıcılar kapatılıyor

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
        removeMusic() // müzik kaldırılıyor
        setVideoSound(true) // video sesi açık
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
