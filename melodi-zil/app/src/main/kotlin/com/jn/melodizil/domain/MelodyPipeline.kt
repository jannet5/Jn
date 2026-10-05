package com.jn.melodizil.domain // İş mantığı

import android.content.Context // Bağlam
import android.net.Uri // Yerel dosya Uri
import android.provider.OpenableColumns // Dosya adı sorgusu
import com.jn.melodizil.core.Melody // Melodi
import com.jn.melodizil.core.MelodyExtractor // Çıkarıcı
import com.jn.melodizil.data.audio.AudioDecoder // Çözücü
import com.jn.melodizil.data.youtube.SourceException // Hatalar
import com.jn.melodizil.data.youtube.YouTubeSource // YouTube
import kotlinx.coroutines.Dispatchers // İş parçacıkları
import kotlinx.coroutines.ensureActive // İptal kontrolü
import kotlinx.coroutines.withContext // Bağlam değişimi
import java.io.File // Dosya
import kotlin.coroutines.coroutineContext // Mevcut bağlam

/** İşlem adımları: ekranda adım adım gösterilir. */
enum class Step { RESOLVE, DOWNLOAD, DECODE, ANALYZE }

/** İlerleme bildirimi: hangi adım, o adımda yüzde kaç. */
data class PipelineProgress(val step: Step, val fraction: Float)

/** Analiz sonucu: ekranda gösterilen ve sentezde kullanılan her şey. */
data class AnalyzedTrack(
    val title: String, // Başlık
    val subtitle: String, // Kanal ya da "Yerel dosya"
    val thumbnailUrl: String?, // Kapak
    val sourceVideoId: String?, // Video kimliği
    val melody: Melody, // Melodi
)

/** YouTube bağlantısı ya da yerel dosyadan melodi üretir. Her adımda ilerleme yayar; coroutine iptaline saygı duyar. */
class MelodyPipeline(private val context: Context, private val youTube: YouTubeSource) {
    private val extractor = MelodyExtractor(ANALYSIS_RATE) // Çıkarıcı (sabit hız)

    suspend fun fromYouTube(link: String, onProgress: (PipelineProgress) -> Unit): AnalyzedTrack = withContext(Dispatchers.IO) {
        onProgress(PipelineProgress(Step.RESOLVE, 0f)) // Çözümleme başlıyor
        val track = youTube.resolve(link) // Bilgi
        coroutineContext.ensureActive() // İptal edildi mi
        onProgress(PipelineProgress(Step.DOWNLOAD, 0f)) // İndirme
        val ext = if (track.mimeType == "audio/webm") "webm" else "m4a" // Uzantı
        val file = File(context.cacheDir, "indirme_${track.videoId}.$ext") // Önbellek dosyası
        try {
            youTube.download(track, file) { coroutineContext.ensureActive(); onProgress(PipelineProgress(Step.DOWNLOAD, it)) } // İndir
            coroutineContext.ensureActive() // İptal
            val melody = analyze(file, onProgress) // Çöz + analiz
            AnalyzedTrack(track.title, track.uploader, track.thumbnailUrl, track.videoId, melody) // Sonuç
        } finally { file.delete() } // Önbellek temizliği: ses dosyası cihazda tutulmaz
    }

    suspend fun fromLocalFile(uri: Uri, onProgress: (PipelineProgress) -> Unit): AnalyzedTrack = withContext(Dispatchers.IO) {
        onProgress(PipelineProgress(Step.DOWNLOAD, 0f)) // Kopyalama (indirme adımı olarak gösterilir)
        val name = queryName(uri) ?: "Ses dosyası" // Ad
        val file = File(context.cacheDir, "yerel_${System.currentTimeMillis()}.bin") // Önbellek
        try {
            context.contentResolver.openInputStream(uri)?.use { input -> file.outputStream().use { input.copyTo(it) } } ?: throw SourceException.Unavailable("Dosya açılamadı") // Kopyala
            onProgress(PipelineProgress(Step.DOWNLOAD, 1f)) // Tamam
            val melody = analyze(file, onProgress) // Analiz
            if (melody.durationSec > YouTubeSource.MAX_DURATION_SEC) throw SourceException.TooLong(melody.durationSec.toInt()) // Süre sınırı yerel dosyada da geçerli
            AnalyzedTrack(name.substringBeforeLast('.'), "Yerel dosya", null, null, melody) // Sonuç
        } finally { file.delete() } // Temizlik
    }

    private suspend fun analyze(file: File, onProgress: (PipelineProgress) -> Unit): Melody {
        onProgress(PipelineProgress(Step.DECODE, 0f)) // Çözme
        val pcm = AudioDecoder.decodeToMono(file, ANALYSIS_RATE) { onProgress(PipelineProgress(Step.DECODE, it)) } // PCM
        coroutineContext.ensureActive() // İptal
        onProgress(PipelineProgress(Step.ANALYZE, 0f)) // Analiz
        val melody = extractor.extract(pcm) { onProgress(PipelineProgress(Step.ANALYZE, it)) } // Melodi
        if (melody.isEmpty) throw SourceException.Unavailable("Bu kayıtta belirgin bir melodi bulunamadı") // Boş
        return melody // Sonuç
    }

    private fun queryName(uri: Uri): String? = try { // Dosya adını sorgula
        context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c -> if (c.moveToFirst()) c.getString(0) else null } // İlk satır
    } catch (e: Exception) { null } // Sorgu başarısız

    companion object { const val ANALYSIS_RATE = 22050 } // Analiz örnekleme hızı
}
