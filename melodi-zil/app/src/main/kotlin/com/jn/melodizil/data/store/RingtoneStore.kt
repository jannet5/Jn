package com.jn.melodizil.data.store // Kalıcı depolama katmanı

import android.content.ContentValues // MediaStore kaydı
import android.content.Context // Bağlam
import android.content.Intent // Ayarlar sayfası
import android.media.RingtoneManager // Varsayılan zil ayarı
import android.net.Uri // Uri
import android.os.Build // Sürüm
import android.os.Environment // Klasörler
import android.provider.MediaStore // Medya veritabanı
import android.provider.Settings // Sistem ayarı izni

/** Zil türleri: ekranlarda kullanıcıya gösterilen üç seçenek. */
enum class RingtoneKind(val managerType: Int) {
    RINGTONE(RingtoneManager.TYPE_RINGTONE), // Arama zili
    NOTIFICATION(RingtoneManager.TYPE_NOTIFICATION), // Bildirim
    ALARM(RingtoneManager.TYPE_ALARM), // Alarm
}

/** WAV baytlarını MediaStore'daki Ringtones klasörüne yazar ve isteğe bağlı varsayılan zil yapar. */
class RingtoneStore(private val context: Context) {

    /** Dosyayı kaydeder, Uri döndürür. Android 10+'da izin gerekmez; 8-9'da WRITE_EXTERNAL_STORAGE gerekir (manifest'te maxSdk 28). */
    fun save(displayName: String, wav: ByteArray, kind: RingtoneKind): Uri {
        val resolver = context.contentResolver // Çözücü
        val safeName = displayName.replace(Regex("[^\\p{L}\\p{N} _-]"), "").trim().ifBlank { "MelodiZil" }.take(60) // Dosya adı temizliği
        val dir = when (kind) { RingtoneKind.RINGTONE -> Environment.DIRECTORY_RINGTONES; RingtoneKind.NOTIFICATION -> Environment.DIRECTORY_NOTIFICATIONS; RingtoneKind.ALARM -> Environment.DIRECTORY_ALARMS } // Türüne göre klasör
        val values = ContentValues().apply { // Kayıt alanları
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$safeName.wav") // Ad
            put(MediaStore.MediaColumns.TITLE, safeName) // Başlık
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav") // Tür
            put(MediaStore.Audio.Media.ARTIST, "Melodi Zil") // Sanatçı
            put(MediaStore.Audio.Media.IS_RINGTONE, kind == RingtoneKind.RINGTONE) // Zil mi
            put(MediaStore.Audio.Media.IS_NOTIFICATION, kind == RingtoneKind.NOTIFICATION) // Bildirim mi
            put(MediaStore.Audio.Media.IS_ALARM, kind == RingtoneKind.ALARM) // Alarm mı
            put(MediaStore.Audio.Media.IS_MUSIC, false) // Müzik kütüphanesinde görünmesin
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10+: kapsamlı depolama, MediaStore dosyayı kendisi yazar
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, "$dir/MelodiZil") // Alt klasör
            values.put(MediaStore.MediaColumns.IS_PENDING, 1) // Yazım bitene kadar gizli
            val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) // Koleksiyon
            val uri = resolver.insert(collection, values) ?: throw IllegalStateException("MediaStore kaydı oluşturulamadı") // Kayıt
            try {
                resolver.openOutputStream(uri)?.use { it.write(wav) } ?: throw IllegalStateException("Dosya yazılamadı") // Yaz
            } catch (e: Exception) { resolver.delete(uri, null, null); throw e } // Hata olursa kaydı sil
            resolver.update(uri, ContentValues().apply { put(MediaStore.MediaColumns.IS_PENDING, 0) }, null, null) // Görünür yap
            return uri // Uri
        }
        // Android 8-9: dosya önce diske yazılır, MediaStore'a tam yol (_data) ile kaydedilir (yol verilmezse MediaProvider NPE atar, D-010)
        @Suppress("DEPRECATION") val folder = java.io.File(Environment.getExternalStoragePublicDirectory(dir), "MelodiZil") // Hedef klasör
        folder.mkdirs() // Klasör oluştur
        var file = java.io.File(folder, "$safeName.wav") // Hedef dosya
        var i = 2 // Aynı ad varsa numara ekle
        while (file.exists()) { file = java.io.File(folder, "$safeName ($i).wav"); i++ } // Benzersiz ad
        file.writeBytes(wav) // Diske yaz
        @Suppress("DEPRECATION") values.put(MediaStore.MediaColumns.DATA, file.absolutePath) // Tam yol
        values.put(MediaStore.MediaColumns.SIZE, file.length()) // Boyut
        val uri = try { resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values) } catch (e: Exception) { file.delete(); throw e } // Kayıt
        if (uri == null) { file.delete(); throw IllegalStateException("MediaStore kaydı oluşturulamadı") } // Başarısız
        return uri // Uri
    }

    /** Sistem ayarlarını değiştirme izni var mı (varsayılan zil için gerekir). */
    fun canWriteSettings(): Boolean = Settings.System.canWrite(context)

    /** İzin ekranını açan Intent. */
    fun writeSettingsIntent(): Intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:${context.packageName}")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    /** Verilen Uri'yi varsayılan zil/bildirim/alarm yapar. İzin yoksa false döner. */
    fun setAsDefault(uri: Uri, kind: RingtoneKind): Boolean {
        if (!canWriteSettings()) return false // İzin yok
        RingtoneManager.setActualDefaultRingtoneUri(context, kind.managerType, uri) // Ayarla
        return true // Başarılı
    }

    /** Kayıtlı dosyayı siler; dosya başkasına aitse ya da yoksa false. */
    fun delete(uri: Uri): Boolean = try { context.contentResolver.delete(uri, null, null) > 0 } catch (e: Exception) { false }

    /** Uri hâlâ geçerli mi (kullanıcı dosyayı dışarıdan silmiş olabilir). */
    fun exists(uri: Uri): Boolean = try { context.contentResolver.openInputStream(uri)?.use { true } ?: false } catch (e: Exception) { false }
}
