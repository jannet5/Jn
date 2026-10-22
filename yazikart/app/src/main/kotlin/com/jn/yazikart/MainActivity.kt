package com.jn.yazikart // ana paket

import android.Manifest // izin adları
import android.content.Intent // başka uygulamadan gelen paylaşım
import android.content.pm.PackageManager // izin durumu
import android.net.Uri // dosya adresi
import android.os.Build // Android sürümü
import android.os.Bundle // açılış verisi
import androidx.activity.ComponentActivity // Compose destekli ekran
import androidx.activity.compose.rememberLauncherForActivityResult // sonuç bekleyen işlem başlatıcı
import androidx.activity.compose.setContent // Compose içeriğini yerleştirme
import androidx.activity.enableEdgeToEdge // tam ekran çizim
import androidx.activity.result.PickVisualMediaRequest // galeri seçici isteği
import androidx.activity.result.contract.ActivityResultContracts // hazır sonuç sözleşmeleri
import androidx.activity.viewModels // ViewModel oluşturma
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.saveable.rememberSaveable // ekran dönse de hatırlama
import androidx.compose.runtime.setValue // durum yazma
import androidx.core.content.ContextCompat // izin kontrolü
import androidx.core.content.IntentCompat // paylaşılan dosyaları güvenle okumak için
import androidx.lifecycle.compose.collectAsStateWithLifecycle // akışları güvenle dinleme
import com.jn.yazikart.data.Clips // sıra sınırı
import com.jn.yazikart.ui.EditorScreen // ana ekran
import com.jn.yazikart.ui.EditorViewModel // ana ekranın beyni
import com.jn.yazikart.ui.FullscreenScreen // tam ekran
import com.jn.yazikart.ui.GoogleImagesScreen // Google Görseller ekranı
import com.jn.yazikart.ui.VideoSearchScreen // internetten video ekranı
import com.jn.yazikart.ui.VideoUi // sıra ve müzik paketi
import com.jn.yazikart.ui.YaziKartTheme // tema

// Uygulamanın tek ekranı (Activity). Ana ekran ile arama/tam ekran arasında geçiş burada yapılır.
class MainActivity : ComponentActivity() {

    private val vm: EditorViewModel by viewModels() // ekran döndüğünde de yaşayan durum

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // standart açılış
        enableEdgeToEdge() // içerik durum/gezinme çubuklarının altına kadar uzanır
        if (savedInstanceState == null) handleShared(intent) // başka uygulamadan paylaşılan dosya var mı
        setContent { // arayüz kuruluyor
            YaziKartTheme { // tema uygulanıyor
                val style by vm.style.collectAsStateWithLifecycle() // stil
                val image by vm.image.collectAsStateWithLifecycle() // arka plan resmi
                val busy by vm.busy.collectAsStateWithLifecycle() // meşguliyet
                val message by vm.message.collectAsStateWithLifecycle() // mesaj
                val online by vm.online.collectAsStateWithLifecycle() // internet durumu
                val clips by vm.clips.collectAsStateWithLifecycle() // sıradaki parçalar
                val thumbs by vm.thumbs.collectAsStateWithLifecycle() // küçük resimler
                val music by vm.music.collectAsStateWithLifecycle() // müzik
                val videoSound by vm.videoSound.collectAsStateWithLifecycle() // video sesi
                val photoFrame by vm.photoFrame.collectAsStateWithLifecycle() // önizlemedeki fotoğraf karesi
                val showingPhoto by vm.showingPhoto.collectAsStateWithLifecycle() // şu an fotoğraf mı oynuyor
                var showSearch by rememberSaveable { mutableStateOf(false) } // Google Görseller açık mı
                var showFullscreen by rememberSaveable { mutableStateOf(false) } // tam ekran açık mı
                var showVideoSearch by rememberSaveable { mutableStateOf(false) } // video arama açık mı

                val mediaPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(Clips.MAX_CLIPS)) { uris -> // galeriden çoklu seçim
                    if (uris.isNotEmpty()) vm.addMedia(uris, copyVideos = false) // seçilenler sıraya eklenir
                }
                val musicPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> // telefondan ses dosyası
                    if (uri != null) vm.setMusic(uri) // müzik seçildi
                }
                val galleryPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> // galeri seçici (tek arka plan resmi)
                    if (uri != null) vm.pickFromGallery(uri) // seçildiyse arka plan yapılıyor
                }
                val storagePermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> // eski telefonlarda depolama izni
                    if (granted) vm.saveToGallery() // izin verildiyse kaydediliyor
                }

                val video = VideoUi( // sıra, müzik ve oynatıcı tek pakette
                    clips = clips,
                    thumbs = thumbs,
                    music = music,
                    videoSound = videoSound,
                    player = vm.player,
                    photo = if (showingPhoto) photoFrame else null,
                    onAddMedia = { mediaPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)) }, // fotoğraf ve video
                    onSearchVideo = { showVideoSearch = true }, // internetten video
                    onPickMusic = { musicPicker.launch(arrayOf("audio/*")) }, // ses dosyası
                    onRange = vm::setClipRange, // aralık değişti (kaydırırken)
                    onRangeDone = vm::commitClipRange, // aralık bitti
                    onMove = vm::moveClip, // sırayı değiştir
                    onRemove = vm::removeClip, // kaldır
                    onMusicStart = vm::setMusicStart, // müzik başlangıcı (kaydırırken)
                    onMusicStartDone = vm::commitMusicStart, // müzik başlangıcı bitti
                    onMusicVolume = vm::setMusicVolume, // müzik sesi
                    onMusicRemove = vm::removeMusic, // müziği kaldır
                    onVideoSound = vm::setVideoSound, // videoların sesi
                    strip = { c, n -> vm.stripFor(c, n) }, // kırpma şeridi kareleri
                    frameAt = { c, ms -> vm.frameAt(c, ms) }, // tek kare
                )

                if (showVideoSearch) { // internetten video ekranı
                    VideoSearchScreen(
                        busy = busy, // indirme durumu
                        onDownload = { url, ua, cookies, referer -> vm.downloadVideo(url, ua, cookies, referer) { showVideoSearch = false } }, // indirip ekle, ekranı kapat
                        onClose = { showVideoSearch = false }, // kapat
                    )
                } else if (showFullscreen) { // tam ekran: görsel Reels'teki gibi ekranı kaplar
                    FullscreenScreen(
                        style = style, // stil
                        image = image, // resim
                        onStyle = vm::updateStyle, // yazı değiştir
                        onClose = { showFullscreen = false }, // kapat
                        video = video, // sıra, oynatıcı ve müzik
                    )
                } else if (showSearch) { // Google Görseller ekranı
                    GoogleImagesScreen(
                        online = online, // internet var mı
                        busy = busy, // görsel iniyor mu
                        onPick = { url -> vm.pickFromWeb(url) { showSearch = false } }, // basılı tutulan görsel arka plan olur, ekran kapanır
                        onClose = { showSearch = false }, // kapat
                    )
                } else { // ana ekran
                    EditorScreen(
                        style = style, // stil
                        image = image, // resim
                        online = online, // internet
                        busy = busy, // meşguliyet
                        message = message, // mesaj
                        onMessageShown = vm::messageShown, // mesaj gösterildi
                        onStyle = vm::updateStyle, // stil değiştir
                        onOpenSearch = { showSearch = true }, // arama ekranını aç
                        onPickGallery = { // galeriyi aç (tek arka plan resmi)
                            galleryPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) // sadece resimler
                        },
                        onClearImage = vm::clearImage, // resmi kaldır
                        onSave = { // kaydet
                            val needsPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && // Android 9 ve altı
                                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED // izin yoksa
                            if (needsPermission) storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE) // izin isteniyor
                            else vm.saveToGallery() // doğrudan kaydediliyor
                        },
                        onShare = vm::share, // paylaş
                        onReset = vm::resetAll, // baştan başla
                        onFullscreen = { showFullscreen = true }, // tam ekranı aç
                        video = video, // sıra ve müzik
                    )
                }
            }
        }
    }

    // Uygulama açıkken başka bir uygulamadan paylaşılan dosya gelirse sıraya ekler
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent) // standart işlem
        handleShared(intent) // paylaşılanı işle
    }

    // Galeriden/dosyalardan paylaşılan fotoğraf ya da videoyu sıraya ekler (metin ya da bağlantı gelirse bir şey yapmaz)
    private fun handleShared(intent: Intent?) {
        if (intent == null) return // yoksa çık
        val uris: List<Uri> = when (intent.action) { // paylaşım türüne göre
            Intent.ACTION_SEND -> listOfNotNull(IntentCompat.getParcelableExtra(intent, Intent.EXTRA_STREAM, Uri::class.java)) // tek dosya
            Intent.ACTION_SEND_MULTIPLE -> IntentCompat.getParcelableArrayListExtra(intent, Intent.EXTRA_STREAM, Uri::class.java).orEmpty() // birden çok dosya
            else -> emptyList() // başka bir şey
        }
        if (uris.isNotEmpty()) vm.addMedia(uris, copyVideos = true) // kopyalanarak sıraya eklenir (paylaşım izni kalkınca da çalışsın)
    }

    override fun onStop() { super.onStop(); vm.pausePreview() } // uygulama arka plana gidince video durur
    override fun onStart() { super.onStart(); vm.resumePreview() } // geri gelince devam eder
}
