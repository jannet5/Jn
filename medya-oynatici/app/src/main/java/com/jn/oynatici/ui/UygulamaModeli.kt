package com.jn.oynatici.ui

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.jn.oynatici.data.Kutuphane
import com.jn.oynatici.data.Tur
import kotlinx.coroutines.delay
import java.io.File

// Uygulamadaki sayfalar
sealed interface Sayfa {
    data object Ana : Sayfa // iki büyük butonlu açılış
    data class Liste(val tur: Tur, val klasor: File) : Sayfa // klasör içeriği
    data object Calan : Sayfa // çalan müzik
    data object VideoIzle : Sayfa // tam ekran video
    data class Kirp(val tur: Tur, val dosya: File) : Sayfa // kırpma
}

// Sayfalar arası ortak durum
class UygulamaModeli(app: Application) : AndroidViewModel(app) {
    val yigin = mutableStateListOf<Sayfa>(Sayfa.Ana) // açık sayfalar (sonuncusu görünür)
    var oynatici by mutableStateOf<Player?>(null) // çalma servisine bağlı oynatıcı
    var paylasilanLink by mutableStateOf<String?>(null) // YouTube'dan paylaşılan link
    var bekleyenLink by mutableStateOf<String?>(null) // indirme penceresinde hazır gelecek link

    // Yeni sayfa aç
    fun git(sayfa: Sayfa) { yigin.add(sayfa) }

    // Bir önceki sayfaya dön
    fun geri() { if (yigin.size > 1) yigin.removeAt(yigin.lastIndex) }

    // Şu an çalan şey video mu
    fun videoCaliyor(): Boolean =
        oynatici?.currentMediaItem?.mediaId?.let { Kutuphane.uygunMu(Tur.VIDEO, it) } == true

    // Bir dosyayı, bulunduğu klasördeki diğerleriyle sıraya koyarak çal
    fun cal(tur: Tur, dosyalar: List<File>, sira: Int) {
        val p = oynatici ?: return // servis bağlı değilse çık
        val ogeler = dosyalar.map { dosya -> // her dosyayı çalınabilir öğeye çevir
            MediaItem.Builder()
                .setMediaId(dosya.absolutePath) // kimlik = dosya yolu
                .setUri(Uri.fromFile(dosya)) // dosya adresi
                .setMediaMetadata(MediaMetadata.Builder().setTitle(dosya.nameWithoutExtension).build()) // başlık
                .build() // öğeyi oluştur
        }
        p.setMediaItems(ogeler, sira, 0) // sırayı ver, seçilenden başla
        p.prepare() // hazırla
        p.play() // çal
        git(if (tur == Tur.VIDEO) Sayfa.VideoIzle else Sayfa.Calan) // ilgili sayfayı aç
    }
}

// Oynatıcının ekranda gösterilecek anlık durumu
data class CalmaDurumu(
    val varMi: Boolean = false, // sırada bir şey var mı
    val caliyor: Boolean = false, // şu an çalıyor mu
    val baslik: String = "", // çalan dosyanın adı
    val konum: Long = 0, // şu anki saniye (ms)
    val sure: Long = 0, // toplam süre (ms)
    val video: Boolean = false, // çalan şey video mu
)

// Oynatıcıyı yarım saniyede bir okuyup ekrana durum veren yardımcı
@Composable
fun calmaDurumu(p: Player?): State<CalmaDurumu> = produceState(CalmaDurumu(), p) {
    if (p == null) { value = CalmaDurumu(); return@produceState } // bağlı değilse boş durum
    fun oku() = CalmaDurumu( // oynatıcıdan değerleri oku
        varMi = p.mediaItemCount > 0, // sıra dolu mu
        caliyor = p.isPlaying || (p.playWhenReady && p.playbackState == Player.STATE_BUFFERING), // çalıyor mu
        baslik = p.mediaMetadata.title?.toString() ?: "", // başlık
        konum = p.currentPosition, // konum
        sure = p.duration.coerceAtLeast(0), // süre (bilinmiyorsa 0)
        video = p.currentMediaItem?.mediaId?.let { Kutuphane.uygunMu(Tur.VIDEO, it) } == true, // video mu
    )
    val dinleyici = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) { value = oku() } // değişiklik olunca hemen güncelle
    }
    p.addListener(dinleyici) // dinlemeye başla
    try {
        while (true) { value = oku(); delay(500) } // konumu düzenli güncelle
    } finally {
        p.removeListener(dinleyici) // sayfa kapanınca dinlemeyi bırak
    }
}
