package com.jn.yazikart.video // video paketi

import android.content.Context // uygulama bağlamı
import android.graphics.Bitmap // yazı katmanı
import android.os.Handler // ilerlemeyi düzenli sormak için
import android.os.Looper // ana iş parçacığı
import androidx.annotation.OptIn // gelişmiş araçları kullanma onayı
import androidx.media3.common.MediaItem // tek video
import androidx.media3.common.MimeTypes // çıktı biçimleri (H.264 / AAC)
import androidx.media3.common.util.UnstableApi // media3'ün "gelişmiş" işaretli araçları
import androidx.media3.effect.BitmapOverlay // videonun üstüne resim (yazı) basma
import androidx.media3.effect.OverlayEffect // katman efekti
import androidx.media3.effect.Presentation // çıktı boyutuna ortadan kırparak getirme
import androidx.media3.transformer.Composition // birleştirilecek videoların tamamı
import androidx.media3.transformer.EditedMediaItem // düzenlenmiş tek video
import androidx.media3.transformer.EditedMediaItemSequence // arka arkaya videolar
import androidx.media3.transformer.Effects // efekt listesi
import androidx.media3.transformer.ExportException // dışa aktarma hatası
import androidx.media3.transformer.ExportResult // dışa aktarma sonucu
import androidx.media3.transformer.ProgressHolder // ilerleme yüzdesi
import androidx.media3.transformer.Transformer // video işleyici
import com.google.common.collect.ImmutableList // media3'ün istediği liste türü
import com.jn.yazikart.data.VideoClip // video / fotoğraf parçası
import com.jn.yazikart.data.MusicTrack // müzik
import androidx.media3.common.audio.ChannelMixingAudioProcessor // ses seviyesi
import androidx.media3.common.audio.ChannelMixingMatrix // ses seviyesi çarpanı
import kotlinx.coroutines.suspendCancellableCoroutine // geri çağrıyı bekleme
import java.io.File // çıktı dosyası
import kotlin.coroutines.resume // başarıyla devam
import kotlin.coroutines.resumeWithException // hatayla devam

// Videoları arka arkaya birleştirir, her birini seçilen aralıkta kırpar, istenen boyuta (ör. 1080x1920)
// ortadan kırparak getirir ve yazı katmanını üstüne basar. Telefonun donanım kodlayıcısı kullanılır.
@OptIn(UnstableApi::class) // media3 efekt araçları "gelişmiş" işaretli; bilerek kullanılıyor
object VideoExporter {

    // Dışa aktarır; bitince çıktı dosyasını döndürür. onProgress 0..100 arası yüzde alır.
    suspend fun export(
        context: Context, // uygulama bağlamı
        clips: List<VideoClip>, // arka arkaya videolar ve fotoğraflar
        overlay: Bitmap, // yazı + karartma katmanı (çıktı boyutunda)
        out: File, // çıktı MP4 dosyası
        music: MusicTrack?, // altta çalacak müzik (yoksa null)
        videoSound: Boolean, // videoların kendi sesi kalsın mı
        onProgress: (Int) -> Unit, // ilerleme
    ): File = suspendCancellableCoroutine { cont -> // işlem bitene kadar beklenir
        out.delete() // eski dosya varsa siliniyor
        val effects = Effects( // her videoya uygulanacak efektler
            emptyList(), // ses efekti yok
            listOf( // görüntü efektleri (sırası önemli)
                Presentation.createForWidthAndHeight(overlay.width, overlay.height, Presentation.LAYOUT_SCALE_TO_FIT_WITH_CROP), // ortadan kırparak boyutlandır
                OverlayEffect(ImmutableList.of(BitmapOverlay.createStaticBitmapOverlay(overlay))), // yazıyı üstüne bas
            ),
        )
        val items = clips.map { c -> // her parça
            if (c.isPhoto) { // fotoğraf: belirlenen süre boyunca duran görüntü
                val media = MediaItem.Builder().setUri(c.uri).setImageDurationMs(c.lengthMs).build() // fotoğraf tanımı
                EditedMediaItem.Builder(media) // düzenlenmiş fotoğraf
                    .setDurationUs(c.lengthMs * 1000) // ekranda kalma süresi
                    .setFrameRate(30) // saniyede 30 kare videoya çevrilir
                    .setEffects(effects) // boyutlandırma + yazı
                    .build() // hazır
            } else { // video: seçilen aralık
                val media = MediaItem.Builder() // video tanımı
                    .setUri(c.uri) // dosya
                    .setClippingConfiguration( // kırpma aralığı
                        MediaItem.ClippingConfiguration.Builder()
                            .setStartPositionMs(c.startMs) // başlangıç
                            .setEndPositionMs(c.endMs) // bitiş
                            .build()
                    )
                    .build() // tanım hazır
                EditedMediaItem.Builder(media).setEffects(effects).setRemoveAudio(!videoSound).build() // efektli parça (istenirse sesi kapalı)
            }
        }
        val sequences = mutableListOf(EditedMediaItemSequence(items)) // görüntü dizisi
        if (music != null) { // müzik varsa ikinci dizi olarak altta çalar
            val volume = ChannelMixingAudioProcessor().apply { // müzik sesini ayarlayan işlemci
                for (ch in 1..2) putChannelMixingMatrix(ChannelMixingMatrix.create(ch, ch).scaleBy(music.volume)) // tek ve çift kanallı sesler için
            }
            val track = MediaItem.Builder().setUri(music.uri) // şarkı
                .setClippingConfiguration(MediaItem.ClippingConfiguration.Builder().setStartPositionMs(music.startMs).build()) // seçilen saniyeden başlar
                .build() // hazır
            val musicItem = EditedMediaItem.Builder(track).setRemoveVideo(true).setEffects(Effects(listOf(volume), emptyList())).build() // sadece ses, ayarlı seviyede
            sequences += EditedMediaItemSequence(listOf(musicItem), true) // video bitene kadar tekrar eder
        }
        val composition = Composition.Builder(sequences) // görüntü dizisi + (varsa) müzik
            .experimentalSetForceAudioTrack(true) // sesi olmayan video olsa bile birleştirme çalışsın (sessiz ses eklenir)
            .setHdrMode(Composition.HDR_MODE_TONE_MAP_HDR_TO_SDR_USING_OPEN_GL) // HDR telefon videoları normale çevrilir (renkler bozulmasın)
            .build() // birleştirme hazır

        val handler = Handler(Looper.getMainLooper()) // ilerleme sorgusu ana iş parçacığında
        val holder = ProgressHolder() // ilerleme kutusu
        lateinit var transformer: Transformer // işleyici (aşağıda kuruluyor)
        val poll = object : Runnable { // her yarım saniyede ilerleme sor
            override fun run() {
                if (transformer.getProgress(holder) == Transformer.PROGRESS_STATE_AVAILABLE) onProgress(holder.progress) // yüzde bildiriliyor
                handler.postDelayed(this, 500) // yarım saniye sonra tekrar
            }
        }
        transformer = Transformer.Builder(context) // işleyici kuruluyor
            .setVideoMimeType(MimeTypes.VIDEO_H264) // her telefonda açılan H.264 video
            .setAudioMimeType(MimeTypes.AUDIO_AAC) // her yerde çalan AAC ses
            .addListener(object : Transformer.Listener { // sonuç dinleyicisi
                override fun onCompleted(composition: Composition, exportResult: ExportResult) { // bitti
                    handler.removeCallbacks(poll) // sorgu durduruluyor
                    onProgress(100) // %100
                    if (cont.isActive) cont.resume(out) // dosya döndürülüyor
                }
                override fun onError(composition: Composition, exportResult: ExportResult, exportException: ExportException) { // hata
                    handler.removeCallbacks(poll) // sorgu durduruluyor
                    out.delete() // yarım dosya siliniyor
                    if (cont.isActive) cont.resumeWithException(exportException) // hata iletiliyor
                }
            })
            .build() // işleyici hazır
        transformer.start(composition, out.absolutePath) // işlem başlıyor
        handler.post(poll) // ilerleme sorgusu başlıyor
        cont.invokeOnCancellation { handler.post { handler.removeCallbacks(poll); transformer.cancel(); out.delete() } } // iptal edilirse durdur
    }
}
