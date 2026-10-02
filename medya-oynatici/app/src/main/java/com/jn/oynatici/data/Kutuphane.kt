package com.jn.oynatici.data

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File

// Uygulamadaki iki bölüm: Müzik ve Video
enum class Tur(val klasorAdi: String, val baslik: String) {
    MUZIK("Muzik", "Müzik"), // ses dosyaları bölümü
    VIDEO("Video", "Video"), // video dosyaları bölümü
}

// Dosya ve klasör işlemlerinin hepsi burada (kopyalama, silme, ad değiştirme...)
object Kutuphane {

    // Ses dosyası sayılan uzantılar
    private val sesUzantilari = setOf("mp3", "m4a", "aac", "wav", "ogg", "oga", "opus", "flac", "wma", "amr", "mka")
    // Video dosyası sayılan uzantılar
    private val videoUzantilari = setOf("mp4", "mkv", "webm", "3gp", "mov", "avi", "m4v", "ts", "flv", "wmv")
    // Dosya adında kullanılamayan karakterler
    private val yasakKarakter = Regex("""[\\/:*?"<>|\u0000-\u001F]""")

    // Bölümün ana klasörü (telefonda Android/data/com.jn.oynatici/files/Muzik gibi)
    fun kok(ctx: Context, tur: Tur): File {
        val temel = ctx.getExternalFilesDir(null) ?: ctx.filesDir // harici alan yoksa iç alanı kullan
        return File(temel, tur.klasorAdi).apply { mkdirs() } // klasör yoksa oluştur
    }

    // Dosya uzantısı bu bölüme uygun mu?
    fun uygunMu(tur: Tur, ad: String): Boolean {
        val uzanti = ad.substringAfterLast('.', "").lowercase() // uzantıyı küçük harfle al
        return if (tur == Tur.MUZIK) uzanti in sesUzantilari else uzanti in videoUzantilari // bölüme göre kontrol
    }

    // Klasör içeriğini getir: önce klasörler, sonra dosyalar (ikisi de alfabetik)
    fun listele(tur: Tur, klasor: File): Pair<List<File>, List<File>> {
        val hepsi = klasor.listFiles()?.filter { !it.name.startsWith(".") } ?: emptyList() // gizli dosyaları atla
        val klasorler = hepsi.filter { it.isDirectory }.sortedBy { it.name.lowercase() } // alt klasörler
        val dosyalar = hepsi.filter { it.isFile && uygunMu(tur, it.name) }.sortedBy { it.name.lowercase() } // medya dosyaları
        return klasorler to dosyalar // ikisini birlikte döndür
    }

    // Klasör (ve alt klasörleri) içinde kaç medya dosyası var
    fun dosyaSayisi(tur: Tur, klasor: File): Int =
        klasor.walkTopDown().count { it.isFile && uygunMu(tur, it.name) } // tüm ağacı say

    // Bölümdeki tüm klasörler (taşıma ekranında hedef seçmek için)
    fun tumKlasorler(tur: Tur, kok: File): List<File> =
        kok.walkTopDown().filter { it.isDirectory && !it.name.startsWith(".") }.toList() // kök dahil hepsi

    // Dosya adını güvenli hale getir
    fun guvenliAd(ad: String): String {
        val temiz = ad.replace(yasakKarakter, "_").trim().trim('.') // yasak karakterleri değiştir
        return temiz.take(150).ifBlank { "adsiz" } // çok uzunsa kısalt, boşsa "adsiz"
    }

    // Aynı isimde dosya varsa "ad (2).mp3" gibi boş bir isim bul
    fun bosAd(klasor: File, ad: String): File {
        val ilk = File(klasor, ad) // önce adın kendisini dene
        if (!ilk.exists()) return ilk // boşsa direkt kullan
        val govde = ad.substringBeforeLast('.') // uzantısız kısım
        val uzanti = ad.substringAfterLast('.', "").let { if (it.isEmpty() || it == ad) "" else ".$it" } // uzantı
        var i = 2 // sayaç 2'den başlar
        while (true) { // boş isim bulana kadar dene
            val aday = File(klasor, "$govde ($i)$uzanti") // "ad (2).mp3"
            if (!aday.exists()) return aday // boşsa bunu kullan
            i++ // değilse bir sonrakine geç
        }
    }

    // Yeni klasör oluştur
    fun yeniKlasor(ust: File, ad: String): File {
        val hedef = bosAd(ust, guvenliAd(ad)) // çakışmayan isim
        hedef.mkdirs() // klasörü oluştur
        return hedef // oluşturulan klasörü döndür
    }

    // Dosya veya klasörün adını değiştir (uzantı korunur)
    fun yenidenAdlandir(dosya: File, yeniAd: String): Boolean {
        val uzanti = if (dosya.isFile) dosya.extension.let { if (it.isEmpty()) "" else ".$it" } else "" // dosyaysa uzantıyı sakla
        val temiz = guvenliAd(yeniAd.removeSuffix(uzanti)) // yeni adı temizle
        if (temiz + uzanti == dosya.name) return true // ad aynıysa bir şey yapma
        val hedef = bosAd(dosya.parentFile!!, temiz + uzanti) // çakışmayan hedef
        return dosya.renameTo(hedef) // adı değiştir
    }

    // Dosya ya da klasörü (içiyle birlikte) sil
    fun sil(dosya: File): Boolean = dosya.deleteRecursively()

    // Dosya/klasörü başka klasöre taşı
    fun tasi(dosya: File, hedefKlasor: File): Boolean {
        if (dosya.parentFile == hedefKlasor) return true // zaten oradaysa bir şey yapma
        if (dosya.isDirectory && hedefKlasor.canonicalPath.startsWith(dosya.canonicalPath)) return false // klasör kendi içine taşınamaz
        return dosya.renameTo(bosAd(hedefKlasor, dosya.name)) // aynı disk içinde taşı
    }

    // Telefondan seçilen tek tek dosyaları kopyala
    fun dosyalariEkle(ctx: Context, tur: Tur, uriler: List<Uri>, hedef: File, ilerleme: (Int, Int) -> Unit): Int {
        var eklenen = 0 // başarıyla eklenen sayısı
        uriler.forEachIndexed { i, uri -> // her seçilen dosya için
            ilerleme(i, uriler.size) // ekrana "3/20" bilgisini ver
            val belge = DocumentFile.fromSingleUri(ctx, uri) // dosya bilgisini al
            val ad = guvenliAd(belge?.name ?: "dosya_${System.currentTimeMillis()}") // dosya adı
            if (kopyala(ctx, uri, bosAd(hedef, ad))) eklenen++ // kopyala, başarılıysa say
        }
        return eklenen // kaç dosya eklendiğini döndür
    }

    // Telefondan seçilen bir klasörü (alt klasörleriyle) olduğu gibi kopyala
    fun klasoruEkle(ctx: Context, tur: Tur, agacUri: Uri, hedef: File, ilerleme: (Int, Int) -> Unit): Int {
        val kaynak = DocumentFile.fromTreeUri(ctx, agacUri) ?: return 0 // seçilen klasörü aç
        val dosyalar = mutableListOf<Pair<DocumentFile, String>>() // (dosya, göreli yol) listesi
        topla(tur, kaynak, "", dosyalar) // klasördeki uygun dosyaları topla
        if (dosyalar.isEmpty()) return 0 // uygun dosya yoksa bir şey oluşturma
        val yeni = yeniKlasor(hedef, kaynak.name ?: "Klasör") // uygulamada aynı isimde klasör aç
        var eklenen = 0 // eklenen sayısı
        dosyalar.forEachIndexed { i, (belge, yol) -> // her dosya için
            ilerleme(i, dosyalar.size) // ilerlemeyi bildir
            val altKlasor = if (yol.isEmpty()) yeni else File(yeni, yol).apply { mkdirs() } // alt klasörü oluştur
            if (kopyala(ctx, belge.uri, bosAd(altKlasor, guvenliAd(belge.name ?: "dosya")))) eklenen++ // kopyala
        }
        return eklenen // eklenen dosya sayısı
    }

    // Klasörü gez, uygun dosyaları göreli yollarıyla listeye ekle
    private fun topla(tur: Tur, klasor: DocumentFile, yol: String, liste: MutableList<Pair<DocumentFile, String>>) {
        klasor.listFiles().forEach { oge -> // klasördeki her öğe
            val ad = oge.name ?: return@forEach // adı yoksa atla
            if (oge.isDirectory) topla(tur, oge, if (yol.isEmpty()) guvenliAd(ad) else "$yol/${guvenliAd(ad)}", liste) // alt klasöre in
            else if (uygunMu(tur, ad) || (oge.type ?: "").startsWith(if (tur == Tur.MUZIK) "audio/" else "video/")) liste += oge to yol // uygunsa ekle
        }
    }

    // Bir Uri'deki dosyayı uygulama klasörüne kopyala
    private fun kopyala(ctx: Context, uri: Uri, hedef: File): Boolean = try {
        ctx.contentResolver.openInputStream(uri)!!.use { giris -> // kaynağı aç
            hedef.outputStream().use { cikis -> giris.copyTo(cikis, 256 * 1024) } // hedefe yaz
        }
        true // başarılı
    } catch (e: Exception) {
        hedef.delete() // yarım kalan dosyayı sil
        false // başarısız
    }

    // Dosya boyutunu okunur hale getir (örn. 4,2 MB)
    fun boyutYazi(bayt: Long): String = when {
        bayt >= 1L shl 30 -> String.format("%.1f GB", bayt / (1L shl 30).toDouble()) // gigabayt
        bayt >= 1L shl 20 -> String.format("%.1f MB", bayt / (1L shl 20).toDouble()) // megabayt
        else -> "${bayt / 1024} KB" // kilobayt
    }
}
