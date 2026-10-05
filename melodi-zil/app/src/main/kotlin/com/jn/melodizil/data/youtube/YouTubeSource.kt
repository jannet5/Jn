package com.jn.melodizil.data.youtube // YouTube veri katmanı

import com.jn.melodizil.core.YouTubeUrl // Bağlantı çözümleme
import okhttp3.OkHttpClient // HTTP istemcisi
import org.schabi.newpipe.extractor.MediaFormat // Ses biçimleri
import org.schabi.newpipe.extractor.NewPipe // Ekstraktör girişi
import org.schabi.newpipe.extractor.ServiceList // Servisler (YouTube)
import org.schabi.newpipe.extractor.localization.ContentCountry // İçerik ülkesi
import org.schabi.newpipe.extractor.localization.Localization // Dil
import org.schabi.newpipe.extractor.stream.DeliveryMethod // Teslim yöntemi
import org.schabi.newpipe.extractor.stream.StreamInfo // Video bilgisi
import java.io.File // Dosya
import java.io.IOException // G/Ç hatası

/** Çözümlenmiş video bilgisi: başlık, süre, kapak ve seçilen ses akışı. */
data class ResolvedTrack(
    val videoId: String, // Video kimliği
    val title: String, // Başlık
    val uploader: String, // Kanal
    val durationSec: Int, // Süre (saniye)
    val thumbnailUrl: String?, // Kapak görseli
    val audioUrl: String, // Ses akışı adresi
    val mimeType: String, // Ses biçimi (audio/mp4 ya da audio/webm)
    val approxBytes: Long, // Yaklaşık boyut (ilerleme için, bilinmiyorsa -1)
)

/** Hata türleri: kullanıcıya Türkçe açıklama göstermek için ayrıştırılır. */
sealed class SourceException(message: String) : Exception(message) {
    class InvalidLink : SourceException("Geçerli bir YouTube bağlantısı değil") // Bağlantı tanınmadı
    class TooLong(val durationSec: Int) : SourceException("Video 10 dakikadan uzun") // Süre sınırı
    class Unavailable(detail: String) : SourceException(detail) // Erişilemez/özel/kaldırılmış
    class NoAudio : SourceException("Bu video için ses akışı bulunamadı") // Ses yok
    class Network(detail: String) : SourceException(detail) // Ağ
}

/** YouTube bağlantısını çözer ve ses akışını indirir. Ekstraktör olarak açık kaynak NewPipeExtractor kullanılır. */
class YouTubeSource(private val client: OkHttpClient = OkHttpDownloader.defaultClient()) {

    init { ensureInit(client) } // NewPipe bir kez başlatılır

    /** Bağlantıdan video bilgisi ve en uygun ses akışını bulur. */
    fun resolve(linkOrId: String): ResolvedTrack {
        val id = YouTubeUrl.extractVideoId(linkOrId) ?: throw SourceException.InvalidLink() // Kimlik
        var info: StreamInfo? = null // Çözümlenen bilgi
        var candidates: List<org.schabi.newpipe.extractor.stream.AudioStream> = emptyList() // Uygun akışlar
        for (attempt in 1..MAX_ATTEMPTS) { // YouTube bazen akış listesi boş yanıt döndürür; birkaç kez denenir
            info = try { StreamInfo.getInfo(ServiceList.YouTube, YouTubeUrl.canonical(id)) } // Bilgi çekiliyor
            catch (e: IOException) { throw SourceException.Network(e.message ?: "Ağ hatası") } // Ağ
            catch (e: Exception) { throw SourceException.Unavailable(friendly(e)) } // Diğer
            val duration = info.duration.toInt() // Süre
            if (duration <= 0) throw SourceException.Unavailable("Canlı yayın ya da süresi bilinmeyen video desteklenmiyor") // Canlı
            if (duration > MAX_DURATION_SEC) throw SourceException.TooLong(duration) // Sınır
            candidates = info.audioStreams.filter { it.deliveryMethod == DeliveryMethod.PROGRESSIVE_HTTP && it.isUrl && !it.content.isNullOrBlank() } // Doğrudan indirilebilir akışlar
            if (candidates.isNotEmpty()) break // Bulundu
            Thread.sleep(400L * attempt) // Kısa bekleme
        }
        info ?: throw SourceException.Unavailable("Video açılamadı") // Olmaması gereken durum
        if (candidates.isEmpty()) throw SourceException.NoAudio() // Ses yok
        val best = candidates.sortedWith( // Önce M4A (cihaz çözücüsü en güvenilir), sonra yüksek bit hızı
            compareByDescending<org.schabi.newpipe.extractor.stream.AudioStream> { it.format == MediaFormat.M4A }.thenByDescending { it.averageBitrate },
        ).first() // En iyi aday
        val mime = when (best.format) { MediaFormat.M4A -> "audio/mp4"; MediaFormat.WEBMA, MediaFormat.WEBMA_OPUS, MediaFormat.OPUS -> "audio/webm"; else -> best.format?.mimeType ?: "audio/mp4" } // MIME
        val thumb = info.thumbnails.maxByOrNull { it.width }?.url // En büyük kapak
        return ResolvedTrack(id, info.name, info.uploaderName ?: "", info.duration.toInt(), thumb, best.content, mime, best.itagItem?.contentLength ?: -1L) // Sonuç
    }

    /** Ses akışını parça parça (Range) indirir; YouTube büyük tek isteklerde hız kısıtlar. */
    fun download(track: ResolvedTrack, target: File, onProgress: (Float) -> Unit) {
        target.parentFile?.mkdirs() // Klasör
        var total = track.approxBytes // Toplam boyut (itag tahmini; gerçek dosyadan büyük olabilir)
        var offset = 0L // İndirilen bayt
        var finished = false // Bitti bayrağı (416 ya da kısa parça)
        target.outputStream().use { out -> // Çıkış akışı
            while (!finished) { // Parçalar
                val end = offset + CHUNK_BYTES - 1 // Parça sonu
                val req = okhttp3.Request.Builder().url(track.audioUrl).header("Range", "bytes=$offset-$end").header("User-Agent", OkHttpDownloader.USER_AGENT).build() // Range isteği
                val resp = try { client.newCall(req).execute() } catch (e: IOException) { throw SourceException.Network(e.message ?: "İndirme kesildi") } // İstek
                resp.use { r -> // Yanıt
                    if (r.code == 416) { finished = true; return@use } // Aralık dosya sonunu aştı: indirme tamam (D-012: eskiden sonsuz döngüydü)
                    if (!r.isSuccessful) throw SourceException.Network("İndirme başarısız (HTTP ${r.code})") // Hata
                    val rangeTotal = parseTotal(r.header("Content-Range")) // Sunucunun bildirdiği gerçek toplam
                    if (rangeTotal > 0) total = rangeTotal // Gerçek boyut her zaman tahmine tercih edilir
                    val body = r.body ?: throw SourceException.Network("Boş yanıt") // Gövde
                    val buf = ByteArray(64 * 1024) // Tampon
                    var got = 0L // Bu parçada okunan
                    body.byteStream().use { input -> // Giriş akışı
                        while (true) { // Okuma döngüsü
                            val n = input.read(buf) // Oku
                            if (n <= 0) break // Bitti
                            out.write(buf, 0, n); offset += n; got += n // Yaz ve say
                            if (total > 0) onProgress((offset.toFloat() / total).coerceIn(0f, 1f)) // İlerleme
                        }
                    }
                    if (r.code == 200 || got == 0L || got < CHUNK_BYTES || (total > 0 && offset >= total)) finished = true // Tamamı geldi ya da son parça
                }
            }
        }
        if (offset == 0L) throw SourceException.Network("İndirilen dosya boş") // Hiç veri gelmedi
        onProgress(1f) // Tamam
    }

    private fun parseTotal(contentRange: String?): Long = contentRange?.substringAfter('/', "")?.toLongOrNull() ?: -1L // "bytes 0-999/12345" → 12345

    companion object {
        const val MAX_DURATION_SEC = 600 // 10 dakika sınırı
        const val MAX_ATTEMPTS = 3 // Boş akış listesine karşı deneme sayısı

        /** Ekstraktör hatalarını kullanıcıya gösterilebilir Türkçe metne çevirir. */
        fun friendly(e: Exception): String {
            val m = e.message ?: "" // Mesaj
            return when {
                m.contains("private", true) -> "Bu video özel (private), erişilemiyor"
                m.contains("age", true) -> "Yaş sınırlı videolar desteklenmiyor"
                m.contains("not available", true) || m.contains("unavailable", true) -> "Video kullanılamıyor (kaldırılmış ya da bölgede kapalı)"
                m.contains("live", true) -> "Canlı yayınlar desteklenmiyor"
                else -> "Video açılamadı: ${m.take(120)}"
            }
        }
        const val CHUNK_BYTES = 2L * 1024 * 1024 // 2 MB parçalar
        private var initialized = false // NewPipe başlatıldı mı

        @Synchronized
        fun ensureInit(client: OkHttpClient) {
            if (initialized) return // Zaten hazır
            NewPipe.init(OkHttpDownloader(client), Localization("tr", "TR"), ContentCountry("TR")) // Türkçe yerelleştirme ile başlat
            initialized = true // İşaretle
        }
    }
}
