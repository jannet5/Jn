package com.jn.oynatici.kirp

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.ProgressHolder
import androidx.media3.transformer.Transformer
import com.jn.oynatici.data.Kutuphane
import com.jn.oynatici.data.Tur
import com.jn.oynatici.data.Zaman
import com.jn.oynatici.indir.Motor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Ses ve video kırpma. Orijinal dosyaya DOKUNULMAZ, kırpılan kısım yeni dosya olarak kaydedilir.
object Kirpici {

    // Kırp ve yeni dosyayı döndür. ilerleme: 0-100
    suspend fun kirp(ctx: Context, tur: Tur, kaynak: File, baslangicMs: Long, bitisMs: Long, ilerleme: (Int) -> Unit): File {
        val uzanti = if (tur == Tur.VIDEO) "mp4" else kaynak.extension.ifEmpty { "mp3" } // video MP4, ses kendi biçiminde
        val ad = "${kaynak.nameWithoutExtension} (${Zaman.dosyaAdi(baslangicMs)}-${Zaman.dosyaAdi(bitisMs)}).$uzanti" // "Şarkı (1.28-2.16).mp3"
        val hedef = Kutuphane.bosAd(kaynak.parentFile!!, Kutuphane.guvenliAd(ad)) // aynı klasöre, çakışmayan isimle
        try {
            if (tur == Tur.VIDEO) {
                try {
                    videoKirp(ctx, kaynak, baslangicMs, bitisMs, hedef, ilerleme) // telefonun donanım kodlayıcısıyla, saniyesi saniyesine
                } catch (e: Exception) {
                    hedef.delete() // yarım dosyayı sil
                    ffmpegKirp(ctx, kaynak, baslangicMs, bitisMs, hedef) // olmazsa ffmpeg ile (yeniden kodlamadan) dene
                }
            } else {
                ffmpegKirp(ctx, kaynak, baslangicMs, bitisMs, hedef) // ses: kalite kaybı olmadan kes
            }
        } catch (e: Exception) {
            hedef.delete() // hata olursa yarım dosya kalmasın
            throw e // hatayı ekrana ilet
        }
        ilerleme(100) // bitti
        return hedef // yeni dosya
    }

    // Media3 Transformer ile video kırpma (sadece gereken kısım yeniden kodlanır, hızlı ve tam isabetli)
    @OptIn(UnstableApi::class)
    private suspend fun videoKirp(ctx: Context, kaynak: File, bas: Long, bit: Long, hedef: File, ilerleme: (Int) -> Unit) =
        withContext(Dispatchers.Main) { // Transformer ana iş parçacığında çalışmalı
            suspendCancellableCoroutine { devam ->
                val handler = Handler(Looper.getMainLooper()) // ilerleme sorgusu için
                val oge = MediaItem.Builder()
                    .setUri(Uri.fromFile(kaynak)) // kaynak video
                    .setClippingConfiguration( // kesilecek aralık
                        MediaItem.ClippingConfiguration.Builder().setStartPositionMs(bas).setEndPositionMs(bit).build(),
                    ).build()
                lateinit var sorgu: Runnable // ilerleme sorgusu
                val donusturucu = Transformer.Builder(ctx)
                    .experimentalSetTrimOptimizationEnabled(true) // sadece kesim noktası yeniden kodlansın
                    .addListener(object : Transformer.Listener {
                        override fun onCompleted(composition: Composition, exportResult: ExportResult) {
                            handler.removeCallbacks(sorgu) // sorguyu durdur
                            if (devam.isActive) devam.resume(Unit) // başarılı
                        }

                        override fun onError(composition: Composition, exportResult: ExportResult, exportException: ExportException) {
                            handler.removeCallbacks(sorgu) // sorguyu durdur
                            if (devam.isActive) devam.resumeWithException(exportException) // hata
                        }
                    }).build()
                val tutucu = ProgressHolder() // ilerleme değeri buraya yazılır
                sorgu = Runnable {
                    if (donusturucu.getProgress(tutucu) == Transformer.PROGRESS_STATE_AVAILABLE) ilerleme(tutucu.progress) // yüzdeyi bildir
                    handler.postDelayed(sorgu, 300) // 300 ms sonra tekrar sor
                }
                donusturucu.start(EditedMediaItem.Builder(oge).build(), hedef.absolutePath) // kırpmayı başlat
                handler.post(sorgu) // ilerleme sorgusunu başlat
                devam.invokeOnCancellation { handler.post { handler.removeCallbacks(sorgu); donusturucu.cancel() } } // iptal edilirse durdur
            }
        }

    // ffmpeg ile kesme: yeniden kodlama yok, kalite kaybı yok
    private suspend fun ffmpegKirp(ctx: Context, kaynak: File, bas: Long, bit: Long, hedef: File) {
        val (kod, cikti) = Motor.ffmpeg(
            ctx,
            listOf(
                "-y", "-hide_banner", // sormadan yaz, gereksiz yazı yok
                "-i", kaynak.absolutePath, // kaynak dosya
                "-ss", saniye(bas), "-to", saniye(bit), // başlangıç ve bitiş
                "-map", "0", "-c", "copy", // tüm akışları olduğu gibi kopyala
                hedef.absolutePath, // yeni dosya
            ),
        )
        if (kod != 0 || !hedef.exists() || hedef.length() == 0L) throw IllegalStateException("Kırpılamadı: ${cikti.takeLast(200)}") // başarısız
    }

    // ms -> ffmpeg'in anladığı "88.000" saniye biçimi
    private fun saniye(ms: Long) = "%d.%03d".format(ms / 1000, ms % 1000)
}
