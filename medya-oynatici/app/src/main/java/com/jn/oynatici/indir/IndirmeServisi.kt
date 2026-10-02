package com.jn.oynatici.indir

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.jn.oynatici.MainActivity
import com.jn.oynatici.data.Tur
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File

// YouTube indirmelerini sırayla yapan servis. Uygulama arka plandayken de indirme sürer.
class IndirmeServisi : Service() {

    private val kapsam = CoroutineScope(SupervisorJob() + Dispatchers.IO) // arka plan iş kapsamı
    @Volatile private var calisiyor = false // döngü çalışıyor mu
    private var sonBildirim = 0L // bildirimi çok sık güncellememek için

    override fun onBind(intent: Intent?): IBinder? = null // bağlanma yok

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        kanalOlustur() // bildirim kanalını hazırla
        ServiceCompat.startForeground( // ön plan servisi olarak başla (Android öldürmesin)
            this, BILDIRIM_ID, bildirim("İndirme hazırlanıyor…", 0f, true),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
        )
        synchronized(this) { // döngünün bitişiyle çakışmasın
            if (!calisiyor) { // döngü yoksa başlat
                calisiyor = true // çalışıyor işaretle
                kapsam.launch { dongu() } // arka planda işleri yap
            }
        }
        return START_NOT_STICKY // öldürülürse kendiliğinden yeniden başlama
    }

    // Sıradaki işleri bitene kadar sırayla indir
    private suspend fun dongu() {
        try {
            Motor.hazirla(this) // yt-dlp ve ffmpeg hazır olsun
            Motor.guncelle(this) // yt-dlp'yi güncel tut (günde bir)
            while (true) { // iş kaldıkça
                val isi = IndirmeMerkezi.siradaki() ?: break // sıradaki iş yoksa çık
                if (!indir(isi)) { // başarısız olduysa
                    Motor.guncelle(this, zorla = true) // YouTube değişmiş olabilir: yt-dlp'yi güncelle
                    IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.BEKLIYOR, bilgi = "Tekrar deneniyor…") } // tekrar sıraya al
                    indir(isi.copy(durum = Durum.BEKLIYOR), sonDeneme = true) // bir kez daha dene
                }
            }
        } catch (e: Exception) {
            IndirmeMerkezi.isler.value.filter { it.durum == Durum.BEKLIYOR || it.durum == Durum.INIYOR } // kalan işler
                .forEach { isi -> IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.HATA, bilgi = "Hazırlanamadı: ${e.message}") } } // hata yaz
        } finally {
            synchronized(this) { // yeni iş eklenmesiyle çakışmasın
                if (IndirmeMerkezi.siradaki() != null) { // tam bu arada yeni iş geldiyse
                    kapsam.launch { dongu() } // döngüye devam et
                } else {
                    calisiyor = false // döngü bitti
                    ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE) // bildirimi kaldır
                    stopSelf() // servisi kapat
                }
            }
        }
    }

    // Tek bir işi indir. Başarılıysa (ya da tekrar denemeye gerek yoksa) true döner.
    private fun indir(isi: IndirmeIsi, sonDeneme: Boolean = false): Boolean {
        if (IndirmeMerkezi.isler.value.find { it.id == isi.id }?.durum == Durum.IPTAL) return true // iptal edildiyse atla
        IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.INIYOR, bilgi = "Bilgiler alınıyor…", yuzde = 0f) } // iniyor yap
        val oncekiler = isi.hedef.walkTopDown().map { it.path }.toSet() // indirme öncesi dosyalar
        val gecici = File(cacheDir, "ytdl/${isi.id}").apply { mkdirs() } // ara dosyalar için geçici klasör
        var listeBilgi = "" // "3/20" gibi
        try {
            YoutubeDL.getInstance().execute(istekOlustur(isi, gecici), isi.id) { yuzde, _, satir -> // indirmeyi başlat
                Regex("""Downloading item (\d+) of (\d+)""").find(satir)?.let { listeBilgi = "${it.groupValues[1]}/${it.groupValues[2]} · " } // liste sırası
                Regex("""Destination: (.+)$""").find(satir)?.let { m -> // inen dosyanın adı
                    IndirmeMerkezi.guncelle(isi.id) { it.copy(baslik = File(m.groupValues[1]).nameWithoutExtension) } // başlık olarak göster
                }
                val bilgi = when {
                    satir.contains("[ExtractAudio]") -> "MP3'e dönüştürülüyor…" // mp3 dönüştürme aşaması
                    satir.contains("[Merger]") -> "Ses ve görüntü birleştiriliyor…" // birleştirme aşaması
                    yuzde > 0 -> "%${yuzde.toInt()} indirildi" // indirme aşaması
                    else -> "Bilgiler alınıyor…" // başlangıç
                }
                IndirmeMerkezi.guncelle(isi.id) { it.copy(yuzde = yuzde.coerceIn(0f, 100f), bilgi = listeBilgi + bilgi) } // ekranı güncelle
                bildirimGuncelle(listeBilgi + bilgi, yuzde) // bildirimi güncelle
            }
            IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.BITTI, yuzde = 100f, bilgi = "İndirildi") } // başarılı
            return true // tamam
        } catch (e: YoutubeDL.CanceledException) {
            IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.IPTAL, bilgi = "İptal edildi") } // iptal
            return true // tekrar deneme yok
        } catch (e: Exception) {
            if (IndirmeMerkezi.isler.value.find { it.id == isi.id }?.durum == Durum.IPTAL) return true // iptal yüzünden hata
            val yeniVar = isi.hedef.walkTopDown().any { it.isFile && it.path !in oncekiler } // bir şeyler indi mi
            if (isi.liste && yeniVar) { // listede bazı videolar inmediyse bile çoğu indi
                IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.BITTI, yuzde = 100f, bilgi = "İndirildi (bazıları açılamadı)") } // kısmi başarı
                return true // tekrar deneme yok
            }
            if (!sonDeneme) return false // bir kez daha denensin
            IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.HATA, bilgi = hataMetni(e.message ?: "")) } // hata göster
            return true // vazgeç
        } finally {
            gecici.deleteRecursively() // geçici dosyaları sil
            IndirmeMerkezi.bitti() // ekran listeyi yenilesin
        }
    }

    // yt-dlp komutunu hazırla
    private fun istekOlustur(isi: IndirmeIsi, gecici: File) = YoutubeDLRequest(isi.url).apply {
        addOption("-P", isi.hedef.absolutePath) // dosyaların ineceği klasör
        addOption("-P", "temp:${gecici.absolutePath}") // ara dosyalar buraya, klasör temiz kalsın
        addOption("--no-mtime") // dosya tarihi indirme tarihi olsun
        addOption("--windows-filenames") // telefonda sorun çıkaran karakterleri temizle
        addOption("--trim-filenames", "120") // çok uzun adları kısalt
        addOption("--newline") // ilerleme satır satır gelsin
        if (isi.liste) { // oynatma listesiyse
            addOption("--yes-playlist") // tüm listeyi indir
            addOption("--ignore-errors") // açılmayan video olursa diğerlerine devam et
            addOption("-o", "%(playlist_title,playlist_id|Oynatma Listesi)s/%(playlist_index)03d - %(title)s.%(ext)s") // liste adıyla klasör
        } else { // tek video
            addOption("--no-playlist") // linkte liste olsa da sadece bu video
            addOption("-o", "%(title)s.%(ext)s") // dosya adı = video başlığı
        }
        if (isi.tur == Tur.MUZIK) { // müzik bölümü: en kaliteli ses, MP3
            addOption("-f", "bestaudio/best") // en iyi ses
            addOption("-x") // sadece sesi çıkar
            addOption("--audio-format", "mp3") // MP3'e çevir
            addOption("--audio-quality", "0") // en yüksek MP3 kalitesi
            addOption("--embed-metadata") // başlık/sanatçı bilgisini dosyaya yaz
        } else { // video bölümü: seçilen kalitede MP4
            addOption("-f", "bv*+ba/b") // en iyi görüntü + en iyi ses
            val sirala = if (isi.kalite != null) "res:${isi.kalite},vcodec:h264,acodec:aac" else "res,vcodec:h264,acodec:aac" // kalite sınırı, telefon dostu kodek
            addOption("-S", sirala) // format seçim sırası
            addOption("--merge-output-format", "mp4") // her telefonda açılan MP4
        }
    }

    // Teknik hatayı anlaşılır Türkçe metne çevir
    private fun hataMetni(ham: String): String = when {
        ham.contains("Unsupported URL", true) || ham.contains("is not a valid URL", true) -> "Bu link desteklenmiyor" // geçersiz link
        ham.contains("Private video", true) -> "Bu video gizli" // gizli video
        ham.contains("unavailable", true) -> "Video kullanılamıyor" // kaldırılmış video
        ham.contains("Unable to download", true) || ham.contains("Failed to resolve", true) || ham.contains("timed out", true) -> "İnternet bağlantısını kontrol et" // internet sorunu
        ham.contains("Sign in to confirm", true) -> "YouTube giriş istiyor, biraz sonra tekrar dene" // bot kontrolü
        else -> ham.lineSequence().firstOrNull { it.contains("ERROR") }?.substringAfter("ERROR:")?.trim()?.take(140) ?: "İndirilemedi" // diğer
    }

    // Bildirimi en fazla saniyede bir güncelle
    private fun bildirimGuncelle(metin: String, yuzde: Float) {
        val simdi = System.currentTimeMillis() // şimdiki zaman
        if (simdi - sonBildirim < 1000) return // çok sık olmasın
        sonBildirim = simdi // zamanı kaydet
        runCatching { NotificationManagerCompat.from(this).notify(BILDIRIM_ID, bildirim(metin, yuzde, yuzde <= 0f)) } // izin yoksa sessizce geç
    }

    // İndirme bildirimi
    private fun bildirim(metin: String, yuzde: Float, belirsiz: Boolean) =
        NotificationCompat.Builder(this, KANAL) // kanal
            .setSmallIcon(android.R.drawable.stat_sys_download) // indirme ikonu
            .setContentTitle("İndiriliyor") // başlık
            .setContentText(metin) // açıklama
            .setProgress(100, yuzde.toInt(), belirsiz) // ilerleme çubuğu
            .setOngoing(true) // kaydırarak kapatılmasın
            .setOnlyAlertOnce(true) // her güncellemede ses çıkarmasın
            .setContentIntent( // bildirime dokununca uygulamayı aç
                PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE),
            )
            .build() // bildirimi oluştur

    // Bildirim kanalını oluştur (Android 8+)
    private fun kanalOlustur() {
        val kanal = NotificationChannel(KANAL, "İndirmeler", NotificationManager.IMPORTANCE_LOW) // sessiz kanal
        getSystemService(NotificationManager::class.java).createNotificationChannel(kanal) // kaydet
    }

    override fun onDestroy() {
        kapsam.cancel() // arka plan işlerini durdur
        super.onDestroy() // üst sınıfı çağır
    }

    companion object {
        private const val KANAL = "indirme" // kanal kimliği
        private const val BILDIRIM_ID = 42 // bildirim kimliği
    }
}
