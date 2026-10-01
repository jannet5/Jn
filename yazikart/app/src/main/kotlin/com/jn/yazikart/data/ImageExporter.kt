package com.jn.yazikart.data // veri katmanı paketi

import android.content.ContentValues // galeriye eklenecek dosyanın bilgileri
import android.content.Context // uygulama bağlamı
import android.content.Intent // paylaşma isteği
import android.graphics.Bitmap // kaydedilecek görsel
import android.net.Uri // dosya adresi
import android.os.Build // Android sürümünü kontrol için
import android.os.Environment // Resimler klasörü
import android.provider.MediaStore // telefonun galeri veritabanı
import androidx.core.content.FileProvider // dosyayı diğer uygulamalara güvenle açmak için
import java.io.File // dosya işlemleri
import java.io.FileOutputStream // dosyaya yazma
import java.text.SimpleDateFormat // dosya adına tarih koymak için
import java.util.Date // şu anki zaman
import java.util.Locale // tarih biçimi için dil

// Görseli galeriye kaydeder ya da paylaşır
object ImageExporter {

    private const val FOLDER = "YaziKart" // galeride görünecek klasör adı

    // Dosya adı: YaziKart_20261001_143015.png gibi
    fun fileName(format: ExportFormat, now: Date = Date()): String =
        "YaziKart_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now) + "." + format.ext // tarihli ad

    // Görseli istenen biçimde akışa yazar
    private fun compress(bmp: Bitmap, format: ExportFormat, out: java.io.OutputStream) {
        val cf = if (format == ExportFormat.PNG) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG // biçim seçimi
        bmp.compress(cf, 95, out) // yüksek kalitede yazılıyor (zemin hep dolu olduğu için JPEG'de saydamlık sorunu yok)
    }

    // Galeriye kaydeder; başarılıysa galerideki adresi döndürür
    fun saveToGallery(context: Context, bmp: Bitmap, format: ExportFormat): Uri {
        val name = fileName(format) // dosya adı
        val resolver = context.contentResolver // galeri veritabanına erişim
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10 ve üstü: izin gerekmez
            val values = ContentValues().apply { // dosya bilgileri
                put(MediaStore.Images.Media.DISPLAY_NAME, name) // ad
                put(MediaStore.Images.Media.MIME_TYPE, format.mime) // tür
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/" + FOLDER) // Resimler/YaziKart
                put(MediaStore.Images.Media.IS_PENDING, 1) // yazma bitene kadar gizli
            }
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) // galeride yer açılıyor
                ?: error("Galeride dosya oluşturulamadı") // açılamazsa hata
            try { // yazma deneniyor
                resolver.openOutputStream(uri)?.use { compress(bmp, format, it) } // görsel dosyaya yazılıyor
                    ?: error("Dosya açılamadı") // dosya açılamazsa hata
                values.clear() // bilgiler temizleniyor
                values.put(MediaStore.Images.Media.IS_PENDING, 0) // artık galeride görünsün
                resolver.update(uri, values, null, null) // güncelleme yapılıyor
            } catch (e: Exception) { // hata olursa
                resolver.delete(uri, null, null) // yarım dosya siliniyor
                throw e // hata yukarı iletiliyor
            }
            return uri // galerideki adres
        } else { // Android 9 ve altı: doğrudan Resimler klasörüne yazılır
            @Suppress("DEPRECATION") // eski API, sadece eski telefonlarda kullanılıyor
            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER) // Resimler/YaziKart
            dir.mkdirs() // klasör yoksa oluşturuluyor
            val file = File(dir, name) // hedef dosya
            FileOutputStream(file).use { compress(bmp, format, it) } // görsel yazılıyor
            val values = ContentValues().apply { // galeriye tanıtmak için bilgiler
                put(MediaStore.Images.Media.DISPLAY_NAME, name) // ad
                put(MediaStore.Images.Media.MIME_TYPE, format.mime) // tür
                @Suppress("DEPRECATION") // eski telefonlarda dosya yolu bu alanla bildirilir
                put(MediaStore.Images.Media.DATA, file.absolutePath) // dosya yolu
            }
            return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values) ?: Uri.fromFile(file) // galeriye ekleniyor
        }
    }

    // Görseli geçici dosyaya yazar ve paylaşma penceresini açar (Instagram, WhatsApp vb.)
    fun share(context: Context, bmp: Bitmap, format: ExportFormat) {
        val dir = File(context.cacheDir, "paylas").apply { mkdirs() } // önbellekteki paylaşım klasörü
        dir.listFiles()?.forEach { it.delete() } // eski paylaşım dosyaları temizleniyor
        val file = File(dir, fileName(format)) // yeni dosya
        FileOutputStream(file).use { compress(bmp, format, it) } // görsel yazılıyor
        val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file) // güvenli adres
        val send = Intent(Intent.ACTION_SEND).apply { // paylaşma isteği
            type = format.mime // görsel türü
            putExtra(Intent.EXTRA_STREAM, uri) // paylaşılacak dosya
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // karşı uygulamaya okuma izni
        }
        context.startActivity(Intent.createChooser(send, "Paylaş").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) // uygulama seçme penceresi
    }
}
