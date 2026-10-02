package com.jn.yazikart // ana paket

import android.Manifest // izin adları
import android.content.pm.PackageManager // izin durumu
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle // akışları güvenle dinleme
import com.jn.yazikart.ui.EditorScreen // ana ekran
import com.jn.yazikart.ui.EditorViewModel // ana ekranın beyni
import com.jn.yazikart.ui.FullscreenScreen // tam ekran
import com.jn.yazikart.ui.SearchScreen // arama ekranı
import com.jn.yazikart.ui.YaziKartTheme // tema

// Uygulamanın tek ekranı (Activity). Ana ekran ile arama ekranı arasında geçiş burada yapılır.
class MainActivity : ComponentActivity() {

    private val vm: EditorViewModel by viewModels() // ekran döndüğünde de yaşayan durum

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // standart açılış
        enableEdgeToEdge() // içerik durum/gezinme çubuklarının altına kadar uzanır
        setContent { // arayüz kuruluyor
            YaziKartTheme { // tema uygulanıyor
                val style by vm.style.collectAsStateWithLifecycle() // stil
                val image by vm.image.collectAsStateWithLifecycle() // arka plan resmi
                val search by vm.search.collectAsStateWithLifecycle() // arama durumu
                val busy by vm.busy.collectAsStateWithLifecycle() // meşguliyet
                val message by vm.message.collectAsStateWithLifecycle() // mesaj
                val online by vm.online.collectAsStateWithLifecycle() // internet durumu
                var showSearch by rememberSaveable { mutableStateOf(false) } // arama ekranı açık mı
                var showFullscreen by rememberSaveable { mutableStateOf(false) } // tam ekran açık mı

                val galleryPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> // galeri seçici
                    if (uri != null) vm.pickFromGallery(uri) // seçildiyse arka plan yapılıyor
                }
                val storagePermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> // eski telefonlarda depolama izni
                    if (granted) vm.saveToGallery() // izin verildiyse kaydediliyor
                }

                if (showFullscreen) { // tam ekran: görsel Reels'teki gibi ekranı kaplar
                    FullscreenScreen(
                        style = style, // stil
                        image = image, // resim
                        onStyle = vm::updateStyle, // yazı değiştir
                        onClose = { showFullscreen = false }, // kapat
                    )
                } else if (showSearch) { // arama ekranı
                    SearchScreen(
                        state = search, // arama durumu
                        online = online, // internet var mı
                        onQuery = vm::setQuery, // kelime değişti
                        onSearch = vm::runSearch, // ara
                        onPick = { r -> vm.pickResult(r) { showSearch = false } }, // seçilince indirilip ekran kapanıyor
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
                        onPickGallery = { // galeriyi aç
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
                    )
                }
            }
        }
    }
}
