package com.jn.oynatici.indir

import android.content.Context
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File

// Telefonun içinde çalışan yt-dlp (python) ve ffmpeg'i hazırlayan motor. Sunucu yok.
object Motor {
    private val kilit = Mutex() // aynı anda iki kez kurulmasın
    @Volatile private var hazir = false // kurulum bitti mi
    @Volatile private var guncellemeDenendi = false // yt-dlp bu açılışta güncellendi mi
    private const val GUNCELLEME_ARALIGI = 24L * 60 * 60 * 1000 // günde en fazla bir kez güncelle

    // İlk açılışta python ve ffmpeg dosyalarını açar (birkaç saniye sürer, sonra anında)
    suspend fun hazirla(ctx: Context) = withContext(Dispatchers.IO) {
        kilit.withLock { // kilitle
            if (hazir) return@withLock // zaten hazırsa çık
            YoutubeDL.getInstance().init(ctx.applicationContext) // python + yt-dlp hazırla
            FFmpeg.getInstance().init(ctx.applicationContext) // ffmpeg hazırla
            hazir = true // hazır işaretle
        }
    }

    // YouTube sık değiştiği için yt-dlp'yi kendini güncel tutar (internet varsa, günde bir)
    suspend fun guncelle(ctx: Context, zorla: Boolean = false) = withContext(Dispatchers.IO) {
        if (guncellemeDenendi && !zorla) return@withContext // bu açılışta denendiyse tekrar deneme
        val tercih = ctx.getSharedPreferences("motor", Context.MODE_PRIVATE) // son güncelleme zamanı burada
        val son = tercih.getLong("son_guncelleme", 0) // en son ne zaman güncellendi
        if (!zorla && System.currentTimeMillis() - son < GUNCELLEME_ARALIGI) return@withContext // yakın zamanda güncellendiyse geç
        guncellemeDenendi = true // denendi işaretle
        runCatching { // hata olursa (internet yoksa) sessizce geç
            YoutubeDL.getInstance().updateYoutubeDL(ctx.applicationContext, YoutubeDL.UpdateChannel.STABLE) // en son kararlı sürüme güncelle
            tercih.edit().putLong("son_guncelleme", System.currentTimeMillis()).apply() // zamanı kaydet
        }
    }

    // yt-dlp eklentileri klasörü (Instagram eklentisi: girişsiz profil listeleme ve reels indirme)
    fun eklentiKlasoru(ctx: Context): String {
        val klasor = File(ctx.filesDir, "ytdlp-eklentileri").apply { mkdirs() } // hedef klasör
        val hedef = File(klasor, "ig_plugins.zip") // eklenti paketi
        val boyut = runCatching { ctx.assets.openFd("ytdlp-eklentileri/ig_plugins.zip").length }.getOrDefault(-1L) // paketli dosyanın boyutu
        if (!hedef.isFile || hedef.length() != boyut) { // yoksa ya da uygulama güncellendiyse
            ctx.assets.open("ytdlp-eklentileri/ig_plugins.zip").use { giris -> hedef.outputStream().use { giris.copyTo(it) } } // kopyala
        }
        return klasor.absolutePath // yt-dlp'ye --plugin-dirs ile verilir
    }

    // Kısa/paylaşım linkinin gittiği asıl adresi bul (örn. instagram.com/share/... → /reel/...)
    suspend fun yonlendirmeyiCoz(url: String): String = withContext(Dispatchers.IO) {
        var adres = url // şu anki adres
        repeat(5) { // en fazla 5 yönlendirme
            val baglanti = (java.net.URL(adres).openConnection() as java.net.HttpURLConnection).apply {
                instanceFollowRedirects = false // yönlendirmeyi kendimiz takip edelim
                requestMethod = "GET" // bazı siteler HEAD'i reddediyor
                connectTimeout = 15000; readTimeout = 15000 // zaman aşımı
                setRequestProperty("User-Agent", com.jn.oynatici.data.Platform.FIREFOX_UA) // tarayıcı gibi
            }
            val kod = runCatching { baglanti.responseCode }.getOrDefault(0) // cevap kodu
            val yeni = baglanti.getHeaderField("Location") // yönlendirilen adres
            baglanti.disconnect() // bağlantıyı kapat
            if (kod !in 300..399 || yeni == null) return@withContext adres // yönlendirme yoksa bitti
            adres = java.net.URL(java.net.URL(adres), yeni).toString() // göreli adresleri tamamla
        }
        adres // sonuç
    }

    // ---------------- gallery-dl (X ve Pinterest profillerini listelemek için) ----------------
    // gallery-dl saf Python; telefondaki (yt-dlp ile gelen) Python'la çalışır. Paket assets/gdl içinde gelir,
    // haftada bir PyPI'den yeni sürümü kendiliğinden indirilir (siteler değiştikçe güncel kalsın).

    private val galeriIslemleri = java.util.concurrent.ConcurrentHashMap<String, Process>() // iptal için çalışan işlemler
    private const val GALERI_GUNCELLEME_ARALIGI = 7L * 24 * 60 * 60 * 1000 // haftada bir

    // gallery-dl dosyalarının klasörü
    private fun galeriKlasoru(ctx: Context) = File(ctx.filesDir, "gdl").apply { mkdirs() }

    // Paketli gallery-dl'i ilk kullanımda (ya da uygulama güncellenince) assets'ten kopyala
    private fun galeriKur(ctx: Context) {
        val klasor = galeriKlasoru(ctx) // hedef klasör
        val paketSurum = ctx.assets.open("gdl/surum.txt").bufferedReader().readText().trim() // uygulamayla gelen sürüm
        val isaret = File(klasor, "paket-surum.txt") // en son hangi paket kopyalandı
        if (isaret.isFile && isaret.readText().trim() == paketSurum && File(klasor, "gdl-bagimliliklar.zip").isFile) return // zaten kurulu
        listOf("gallery_dl.whl", "gdl-bagimliliklar.zip").forEach { ad -> // iki dosyayı kopyala
            ctx.assets.open("gdl/$ad").use { giris -> File(klasor, ad).outputStream().use { giris.copyTo(it) } }
        }
        isaret.writeText(paketSurum) // kopyalandı işareti
    }

    // Kullanılacak gallery-dl: güncellenmiş sürüm varsa o, yoksa paketli olan
    private fun galeriTekeri(ctx: Context): File {
        val klasor = galeriKlasoru(ctx) // klasör
        return File(klasor, "gallery_dl-guncel.whl").takeIf { it.isFile && it.length() > 100_000 } ?: File(klasor, "gallery_dl.whl") // güncel ya da paketli
    }

    // gallery-dl'i PyPI'den güncelle (haftada bir; internet yoksa sessizce geç)
    suspend fun galeriGuncelle(ctx: Context) = withContext(Dispatchers.IO) {
        val tercih = ctx.getSharedPreferences("motor", Context.MODE_PRIVATE) // son güncelleme zamanı
        if (System.currentTimeMillis() - tercih.getLong("galeri_guncelleme", 0) < GALERI_GUNCELLEME_ARALIGI) return@withContext // yakın zamanda bakıldı
        runCatching {
            val bilgi = org.json.JSONObject(java.net.URL("https://pypi.org/pypi/gallery-dl/json").readText()) // PyPI bilgisi
            val surum = bilgi.getJSONObject("info").getString("version") // en son sürüm
            val mevcut = tercih.getString("galeri_surum", null) ?: ctx.assets.open("gdl/surum.txt").bufferedReader().readText().trim() // elimizdeki
            if (surumBuyuk(surum, mevcut)) { // daha yeniyse
                val dosyalar = bilgi.getJSONArray("urls") // indirilebilir dosyalar
                val teker = (0 until dosyalar.length()).map { dosyalar.getJSONObject(it) }.first { it.getString("packagetype") == "bdist_wheel" } // saf Python paketi
                val gecici = File(galeriKlasoru(ctx), "indiriliyor.whl") // önce geçici dosyaya
                java.net.URL(teker.getString("url")).openStream().use { giris -> gecici.outputStream().use { giris.copyTo(it) } } // indir
                if (gecici.length() > 100_000) { gecici.renameTo(File(galeriKlasoru(ctx), "gallery_dl-guncel.whl")); tercih.edit().putString("galeri_surum", surum).apply() } // yerine koy
            }
            tercih.edit().putLong("galeri_guncelleme", System.currentTimeMillis()).apply() // bakıldı
        }
    }

    // "1.32.17" > "1.32.9" gibi sürüm karşılaştırması
    private fun surumBuyuk(a: String, b: String): Boolean {
        val x = a.split('.').map { it.filter(Char::isDigit).toIntOrNull() ?: 0 } // parçalar
        val y = b.split('.').map { it.filter(Char::isDigit).toIntOrNull() ?: 0 } // parçalar
        for (i in 0 until maxOf(x.size, y.size)) { val f = (x.getOrElse(i) { 0 }) - (y.getOrElse(i) { 0 }); if (f != 0) return f > 0 } // ilk farklı parça
        return false // eşit
    }

    // gallery-dl'i çalıştır, standart çıktısını döndür. Hata olursa hata metniyle istisna fırlatır.
    suspend fun galeriCalistir(ctx: Context, argumanlar: List<String>, islemId: String?): String = withContext(Dispatchers.IO) {
        hazirla(ctx) // Python hazır olsun
        galeriKur(ctx) // gallery-dl dosyaları hazır olsun
        val binDizini = File(ctx.applicationInfo.nativeLibraryDir) // native dosyalar
        val python = File(ctx.noBackupFilesDir, "youtubedl-android/packages/python/usr") // açılmış Python
        val sertifika = File(python, "etc/tls/cert.pem").absolutePath // güvenli bağlantı sertifikaları
        val komut = listOf(File(binDizini, "libpython.so").absolutePath, "-m", "gallery_dl") + argumanlar // python -m gallery_dl ...
        val islem = ProcessBuilder(komut).apply {
            environment().apply {
                this["LD_LIBRARY_PATH"] = File(python, "lib").absolutePath // Python kütüphaneleri
                this["PYTHONHOME"] = python.absolutePath // Python kökü
                this["HOME"] = python.absolutePath // ev klasörü (yt-dlp ile aynı)
                this["SSL_CERT_FILE"] = sertifika // sertifikalar
                this["REQUESTS_CA_BUNDLE"] = sertifika // requests kütüphanesi için sertifikalar
                this["TMPDIR"] = ctx.cacheDir.absolutePath // geçici klasör
                this["PYTHONPATH"] = galeriTekeri(ctx).absolutePath + ":" + File(galeriKlasoru(ctx), "gdl-bagimliliklar.zip").absolutePath // gallery-dl + requests vb.
                this["PYTHONIOENCODING"] = "utf-8" // Türkçe karakterler bozulmasın
            }
        }.start() // başlat
        islemId?.let { galeriIslemleri[it] = islem } // iptal edilebilsin
        try {
            val hata = StringBuilder() // hata çıktısı
            val hataOkuyucu = Thread { runCatching { islem.errorStream.bufferedReader().forEachLine { hata.appendLine(it) } } }.apply { start() } // ayrı iş parçacığında oku
            val cikti = islem.inputStream.bufferedReader().readText() // standart çıktı
            val kod = islem.waitFor() // bitmesini bekle
            hataOkuyucu.join(2000) // hata okumayı bitir
            if (islemId != null && galeriIslemleri[islemId] == null) throw YoutubeDL.CanceledException() // iptal edildi
            if (kod != 0 && cikti.isBlank()) throw IllegalStateException(hata.toString().ifBlank { "gallery-dl hata kodu $kod" }) // hata
            cikti // sonuç
        } finally {
            islemId?.let { galeriIslemleri.remove(it) } // listeden çıkar
        }
    }

    // Çalışan gallery-dl işlemini durdur
    fun galeriDurdur(islemId: String) { galeriIslemleri.remove(islemId)?.destroy() }

    // ffmpeg'i doğrudan çalıştırır (ses kırpma için). Dönüş: (çıkış kodu, çıktı metni)
    suspend fun ffmpeg(ctx: Context, argumanlar: List<String>): Pair<Int, String> = withContext(Dispatchers.IO) {
        hazirla(ctx) // önce motor hazır olsun
        val binDizini = File(ctx.applicationInfo.nativeLibraryDir) // native dosyaların klasörü
        val paketler = File(ctx.noBackupFilesDir, "youtubedl-android/packages") // açılmış kütüphaneler
        val komut = listOf(File(binDizini, "libffmpeg.so").absolutePath) + argumanlar // ffmpeg + parametreler
        val islem = ProcessBuilder(komut).redirectErrorStream(true).apply { // işlemi hazırla
            environment()["LD_LIBRARY_PATH"] = listOf("python", "ffmpeg", "aria2c")
                .joinToString(":") { File(paketler, "$it/usr/lib").absolutePath } // ffmpeg'in ihtiyaç duyduğu kütüphaneler
            environment()["TMPDIR"] = ctx.cacheDir.absolutePath // geçici dosya klasörü
        }.start() // başlat
        val cikti = islem.inputStream.bufferedReader().readText() // tüm çıktıyı oku
        islem.waitFor() to cikti // bitmesini bekle, sonucu döndür
    }
}
