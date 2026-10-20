package com.jn.oynatici.indir

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.jn.oynatici.MainActivity
import com.jn.oynatici.data.Kutuphane
import com.jn.oynatici.data.LinkTuru
import com.jn.oynatici.data.Platform
import com.jn.oynatici.data.Tur
import com.jn.oynatici.data.Zaman
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

// İnternet indirmelerini (YouTube, TikTok, Instagram, X, Pinterest...) sırayla yapan servis. Uygulama arka plandayken de indirme sürer.
class IndirmeServisi : Service() {

    private val kapsam = CoroutineScope(SupervisorJob() + Dispatchers.IO) // arka plan iş kapsamı
    @Volatile private var calisiyor = false // döngü çalışıyor mu
    private var sonBildirim = 0L // bildirimi çok sık güncellememek için
    private var uyanikKilit: PowerManager.WakeLock? = null // ekran kapanınca işlemci uyumasın (dönüştürme durmasın)
    private var wifiKilit: WifiManager.WifiLock? = null // ekran kapanınca Wi-Fi yavaşlamasın

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
        uyanikTut(true) // indirme/dönüştürme bitene kadar telefon uyumasın
        try {
            Motor.hazirla(this) // yt-dlp ve ffmpeg hazır olsun
            Motor.guncelle(this) // yt-dlp'yi güncel tut (günde bir)
            Motor.galeriGuncelle(this) // gallery-dl'i güncel tut (haftada bir)
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
                    uyanikTut(false) // artık uyanık tutmaya gerek yok
                    calisiyor = false // döngü bitti
                    ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE) // bildirimi kaldır
                    stopSelf() // servisi kapat
                }
            }
        }
    }

    // Tek bir işi indir. Başarılıysa (ya da tekrar denemeye gerek yoksa) true döner.
    private suspend fun indir(isi: IndirmeIsi, sonDeneme: Boolean = false): Boolean {
        if (IndirmeMerkezi.iptalMi(isi.id)) return true // iptal edildiyse atla
        IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.INIYOR, bilgi = "Bilgiler alınıyor…", yuzde = 0f, girisGerekli = false) } // iniyor yap
        val cerez = runCatching { Cerezler.dosyayaYaz(this)?.absolutePath }.getOrNull() // giriş yapılan hesapların güncel çerezleri
        val gecici = File(cacheDir, "ytdl/${isi.id}").apply { mkdirs() } // ara dosyalar için geçici klasör
        val ilerleme = Ilerleme() // ekranda gösterilen ilerleme bilgisi
        val sayac = kapsam.launch { // her saniye ekranı güncelle (dönüştürme sırasında da süre aksın, donmuş sanılmasın)
            while (isActive) {
                val bilgi = ilerleme.yazi() // "3/20 · %45 indirildi" gibi
                IndirmeMerkezi.guncelle(isi.id) { if (it.durum == Durum.INIYOR) it.copy(yuzde = ilerleme.yuzde, bilgi = bilgi) else it } // ekran
                bildirimGuncelle(bilgi, ilerleme.yuzde) // bildirim
                delay(1000) // bir saniye bekle
            }
        }
        try {
            if (!isi.toplu) { // tek içerik: bulunduğun klasöre
                calistir(isi, YoutubeDLRequest(isi.url), isi.hedef, gecici, cerez, ilerleme, toplu = false) // indir
                IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.BITTI, yuzde = 100f, bilgi = "İndirildi") } // başarılı
                return true // tamam
            }
            // Toplu: önce içerikleri belirle (profilse son N içeriği bul)
            val liste = if (isi.linkTuru == LinkTuru.PROFIL) {
                ilerleme.asamaBaslat("Son ${isi.sayi} içerik bulunuyor") // bilgi
                Listeleyici.profilGetir(this, isi) // listele
            } else ListeSonucu(isi.listeAdi, isi.ogeler) // liste: kullanıcının seçtikleri
            if (IndirmeMerkezi.iptalMi(isi.id)) return true // bu arada iptal edildiyse
            if (liste.ogeler.isEmpty()) { // hiç içerik yok
                val neden = if (liste.fotoSayisi > 0) "Son ${liste.fotoSayisi} gönderinin hepsi fotoğraf, video yok" else "İçerik bulunamadı" // sebep
                IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.HATA, bilgi = neden) } // hata
                return true // tekrar deneme yok
            }
            val ad = liste.ad.ifBlank { isi.platform.ad } // kanal / liste adı
            val klasor = Kutuphane.bosAd(isi.hedef, Kutuphane.guvenliAd(klasorAdi(ad, liste.ogeler.size, isi.platform))).apply { mkdirs() } // "A, 20 içerik, Instagram"
            Platform.klasoreYaz(klasor, isi.platform) // klasör ikonu için site etiketi
            runCatching { File(klasor, Platform.LINK_DOSYASI).writeText(liste.ogeler.joinToString("\n", postfix = "\n") { it.url }) } // içeriklerin linkleri alt alta
            IndirmeMerkezi.guncelle(isi.id) { it.copy(baslik = klasor.name) } // kartta klasör adı görünsün
            ilerleme.toplam = liste.ogeler.size // "3/20" için toplam
            ilerleme.asamaBitir() // indirme aşamasına geç
            val hata = runCatching { calistir(isi, YoutubeDLRequest(liste.ogeler.map { it.url }), klasor, gecici, cerez, ilerleme, toplu = true) }.exceptionOrNull() // hepsini indir
            if (hata is YoutubeDL.CanceledException) throw hata // iptal edildiyse aşağıda yakala
            // Sonuç: kaç içerik indi, klasör adını gerçek sayıyla güncelle
            val inen = Kutuphane.listele(isi.tur, klasor).second.size // inen medya dosyası sayısı
            if (inen == 0) { // hiçbiri inmedi
                klasor.deleteRecursively() // boş klasör kalmasın
                throw hata ?: IllegalStateException("Hiçbir içerik indirilemedi") // hatayı aşağıda anlaşılır göster
            }
            val sonAd = Kutuphane.guvenliAd(klasorAdi(ad, inen, isi.platform)) // gerçek sayıyla ad
            val sonKlasor = if (sonAd != klasor.name) Kutuphane.bosAd(isi.hedef, sonAd).takeIf { klasor.renameTo(it) } ?: klasor else klasor // yeniden adlandır
            val atlanan = ilerleme.videosuzSayisi + liste.fotoSayisi // video olmayan (fotoğraf) gönderi sayısı
            val ozet = buildString { // sonuç yazısı
                append("İndirildi: $inen/${liste.ogeler.size + liste.fotoSayisi}") // kaç tane
                if (atlanan > 0) append(" · $atlanan fotoğraf gönderisi atlandı") // fotoğraflar
            }
            IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.BITTI, yuzde = 100f, bilgi = ozet, baslik = sonKlasor.name) } // başarılı
            return true // tamam
        } catch (e: YoutubeDL.CanceledException) {
            IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.IPTAL, bilgi = "İptal edildi") } // iptal
            return true // tekrar deneme yok
        } catch (e: Exception) {
            if (IndirmeMerkezi.iptalMi(isi.id)) return true // iptal yüzünden hata
            val mesaj = e.message ?: "" // ham hata
            val giris = Listeleyici.girisHatasiMi(mesaj) && isi.platform in Cerezler.siteler.map { it.platform } // giriş gerekli mi
            if (!sonDeneme && !giris) return false // bir kez daha denensin (yt-dlp güncellenerek)
            IndirmeMerkezi.guncelle(isi.id) { it.copy(durum = Durum.HATA, bilgi = hataMetni(mesaj, isi.platform), girisGerekli = giris) } // hata göster
            return true // vazgeç
        } finally {
            sayac.cancel() // saniye sayacını durdur
            gecici.deleteRecursively() // geçici dosyaları sil
            IndirmeMerkezi.bitti() // ekran listeyi yenilesin
        }
    }

    // Klasör adı: "Kanal adı, 20 içerik, Instagram"
    private fun klasorAdi(ad: String, sayi: Int, platform: Platform) = "$ad, $sayi içerik, ${platform.ad}"

    // yt-dlp'yi çalıştır ve çıktısından ilerlemeyi takip et
    private fun calistir(isi: IndirmeIsi, istek: YoutubeDLRequest, klasor: File, gecici: File, cerez: String?, ilerleme: Ilerleme, toplu: Boolean) {
        istek.apply {
            addOption("-P", klasor.absolutePath) // dosyaların ineceği klasör
            addOption("-P", "temp:${gecici.absolutePath}") // ara dosyalar buraya, klasör temiz kalsın
            addOption("--no-mtime") // dosya tarihi indirme tarihi olsun
            addOption("--windows-filenames") // telefonda sorun çıkaran karakterleri temizle
            addOption("--trim-filenames", "120") // çok uzun adları kısalt
            addOption("--newline") // ilerleme satır satır gelsin
            addOption("--no-playlist") // her link tek içerik olarak insin
            if (isi.platform != Platform.X) cerez?.let { addOption("--cookies", it) } // giriş yapılan hesapların çerezleri (X'in herkese açık gönderileri çerezsiz daha sağlam iniyor)
            if (isi.platform == Platform.TIKTOK) addOption("--user-agent", Platform.FIREFOX_UA) // TikTok engeline takılmamak için
            if (toplu) { // tek klasöre çok içerik
                addOption("--ignore-errors") // açılmayan (ya da fotoğraf olan) içerik olursa diğerlerine devam et
                addOption("-o", "%(autonumber)03d - %(title).90B.%(ext)s") // "001 - başlık.mp4" (sıra = en yeniden eskiye / seçim sırası)
            } else if (isi.platform == Platform.X) {
                addOption("-o", "%(uploader).40B - %(display_id)s.%(ext)s") // "Kişi - gönderi numarası.mp4" (X başlıkları uzun ve tekrarlı)
            } else if (isi.platform == Platform.YOUTUBE || isi.platform == Platform.DIGER) {
                addOption("-o", "%(title).120B.%(ext)s") // dosya adı = video başlığı
            } else {
                addOption("-o", "%(title).90B [%(id)s].%(ext)s") // TikTok/Instagram başlıkları birbirine benzeyebilir: sonuna kimlik eklenir
            }
            if (isi.tur == Tur.MUZIK) { // müzik bölümü: en kaliteli ses, MP3
                addOption("-f", "bestaudio/best") // en iyi ses
                addOption("-x") // sadece sesi çıkar
                addOption("--audio-format", "mp3") // MP3'e çevir
                addOption("--audio-quality", "0") // en yüksek MP3 kalitesi
                addOption("--embed-metadata") // başlık/sanatçı bilgisini dosyaya yaz
            } else { // video bölümü: seçilen kalitede MP4
                addOption("-f", if (isi.platform == Platform.X) "best[protocol=https][ext=mp4]/bv*+ba/b" else "bv*+ba/b") // en iyi görüntü + ses (X'te tek parça MP4 tercih edilir)
                val sirala = if (isi.kalite != null) "res:${isi.kalite},vcodec:h264,acodec:aac" else "res,vcodec:h264,acodec:aac" // kalite sınırı, telefon dostu kodek
                addOption("-S", sirala) // format seçim sırası
                addOption("--merge-output-format", "mp4") // her telefonda açılan MP4
            }
        }
        YoutubeDL.getInstance().execute(istek, isi.id) { yuzde, _, satir -> ilerleme.satirIsle(isi.id, yuzde, satir) } // çalıştır
    }

    // İndirme ilerlemesini yt-dlp çıktısından çıkaran küçük yardımcı
    private class Ilerleme {
        @Volatile var toplam = 0 // toplu indirmede içerik sayısı
        @Volatile var sira = 0 // şu an kaçıncı içerik
        @Volatile var yuzde = 0f // şu anki dosyanın yüzdesi
        @Volatile var asama = "" // indirme dışı aşama ("MP3'e dönüştürülüyor" gibi)
        @Volatile var asamaBas = System.currentTimeMillis() // aşamanın başladığı an
        @Volatile var videosuzSayisi = 0 // video olmayan (fotoğraf) gönderiler

        fun asamaBaslat(ad: String) { asama = ad; asamaBas = System.currentTimeMillis() } // yeni aşama
        fun asamaBitir() { asama = ""; yuzde = 0f; asamaBas = System.currentTimeMillis() } // indirme aşamasına dön

        // Ekranda gösterilecek yazı
        fun yazi(): String {
            val on = if (toplam > 1 && sira > 0) "${minOf(sira, toplam)}/$toplam · " else "" // "3/20 · "
            val gecen = Zaman.yaz(System.currentTimeMillis() - asamaBas) // aşamada geçen süre
            return on + when {
                asama.isNotEmpty() -> "$asama… $gecen" // "MP3'e dönüştürülüyor… 0:12"
                yuzde >= 99.9f -> "İndirme bitti, hazırlanıyor… $gecen" // %100 oldu, sıradaki aşama başlıyor
                yuzde > 0f -> "%${yuzde.toInt()} indirildi" // indirme sürüyor
                else -> "Bilgiler alınıyor… $gecen" // başlangıç
            }
        }

        // yt-dlp'nin bir çıktı satırını işle
        fun satirIsle(id: String, y: Float, satir: String) {
            if (Regex("""^\[[\w:.-]+] Extracting URL: """).containsMatchIn(satir)) { sira++; asamaBitir() } // sıradaki içeriğe geçti
            Regex("""Downloading item (\d+) of (\d+)""").find(satir)?.let { toplam = it.groupValues[2].toInt(); sira = it.groupValues[1].toInt() } // liste sırası
            if (satir.contains("No video formats found", true) || satir.contains("There is no video in this", true)) videosuzSayisi++ // fotoğraf gönderisi
            Regex("""\[download] Destination: (.+)$""").find(satir)?.let { m -> // yeni bir dosyanın indirmesi başladı
                if (toplam <= 1) IndirmeMerkezi.guncelle(id) { it.copy(baslik = File(m.groupValues[1]).nameWithoutExtension) } // tek indirmede başlık olarak göster
                asama = ""; yuzde = 0f; asamaBas = System.currentTimeMillis() // indirme aşamasına dön
            }
            when {
                satir.startsWith("[ExtractAudio]") -> asamaBaslat("MP3'e dönüştürülüyor") // mp3 dönüştürme
                satir.startsWith("[Merger]") -> asamaBaslat("Ses ve görüntü birleştiriliyor") // birleştirme
                asama.isEmpty() && Regex("""^\[download]\s+[\d.]+%""").containsMatchIn(satir) -> { // indirme ilerleme satırı ("[download]  42.0% of ...")
                    if (y >= 99.9f && yuzde < 99.9f) asamaBas = System.currentTimeMillis() // %100 olduğu an
                    yuzde = y.coerceIn(0f, 100f) // yüzdeyi kaydet
                }
            }
        }
    }

    // İndirme sürerken işlemciyi ve Wi-Fi'ı uyanık tut; bitince bırak
    @Suppress("DEPRECATION")
    private fun uyanikTut(ac: Boolean) {
        if (ac) {
            if (uyanikKilit == null) uyanikKilit = getSystemService(PowerManager::class.java)
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Oynatici:indirme").apply { setReferenceCounted(false) } // işlemci kilidi
            if (wifiKilit == null) wifiKilit = applicationContext.getSystemService(WifiManager::class.java).createWifiLock(
                if (Build.VERSION.SDK_INT >= 29) WifiManager.WIFI_MODE_FULL_LOW_LATENCY else WifiManager.WIFI_MODE_FULL_HIGH_PERF, "Oynatici:indirme",
            ).apply { setReferenceCounted(false) } // Wi-Fi kilidi
            uyanikKilit?.acquire(3 * 60 * 60 * 1000L) // en fazla 3 saat (unutulursa pil bitmesin)
            runCatching { wifiKilit?.acquire() } // Wi-Fi kilidini al
        } else {
            runCatching { if (uyanikKilit?.isHeld == true) uyanikKilit?.release() } // işlemci kilidini bırak
            runCatching { if (wifiKilit?.isHeld == true) wifiKilit?.release() } // Wi-Fi kilidini bırak
        }
    }

    // Teknik hatayı anlaşılır Türkçe metne çevir
    private fun hataMetni(ham: String, platform: Platform): String = when {
        ham.contains("looks truncated", true) || ham.contains("Incomplete YouTube ID", true) -> "Link eksik görünüyor, tamamını yapıştır" // yarım link
        ham.contains("Sign in to confirm", true) -> "YouTube giriş istiyor, biraz sonra tekrar dene" // YouTube bot kontrolü
        Listeleyici.girisHatasiMi(ham) && platform in Cerezler.siteler.map { it.platform } -> "${platform.ad} giriş istiyor. Giriş yapıp tekrar dene." // giriş gerekli
        ham.contains("No video formats found", true) || ham.contains("There is no video in this", true) -> "Bu gönderide video yok (fotoğraf)" // fotoğraf
        ham.contains("Private video", true) -> "Bu video gizli" // gizli video
        ham.contains("unavailable", true) -> "İçerik kullanılamıyor" // kaldırılmış
        else -> Listeleyici.anlasilirHata(ham).ifBlank { "İndirilemedi" } // diğer
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
        uyanikTut(false) // kilitler kalmasın
        kapsam.cancel() // arka plan işlerini durdur
        super.onDestroy() // üst sınıfı çağır
    }

    companion object {
        private const val KANAL = "indirme" // kanal kimliği
        private const val BILDIRIM_ID = 42 // bildirim kimliği
    }
}
