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
