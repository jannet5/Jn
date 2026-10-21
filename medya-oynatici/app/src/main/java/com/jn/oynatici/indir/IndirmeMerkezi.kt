package com.jn.oynatici.indir

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.jn.oynatici.data.LinkTuru
import com.jn.oynatici.data.Platform
import com.jn.oynatici.data.Sekme
import com.jn.oynatici.data.Tur
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.UUID

// Bir indirme işinin durumu
enum class Durum { BEKLIYOR, INIYOR, BITTI, HATA, IPTAL }

// Listedeki tek bir içerik (oynatma listesi / profil öğesi)
data class Oge(
    val url: String, // içeriğin https linki
    val baslik: String, // başlığı (yoksa boş)
    val sureSn: Double? = null, // süresi (biliniyorsa, saniye)
    val medya: String? = null, // doğrudan video dosyası linki (Pinterest'te bazı pinler sadece böyle iner)
)

// Bir indirme işi: tek içerik, seçilmiş liste öğeleri ya da bir profilin son N içeriği
data class IndirmeIsi(
    val id: String = UUID.randomUUID().toString(), // işin kimliği
    val url: String, // kullanıcının verdiği link
    val tur: Tur, // Müzik mi Video mu
    val hedef: File, // hangi klasörün içine inecek
    val kalite: Int?, // video için en fazla yükseklik (720 gibi), null = en yüksek
    val platform: Platform = Platform.bul(url), // site
    val linkTuru: LinkTuru = LinkTuru.TEK, // tek / liste / profil
    val ogeler: List<Oge> = emptyList(), // LISTE: kullanıcının seçtiği öğeler
    val listeAdi: String = "", // LISTE: listenin adı (klasör adı için)
    val sayi: Int = 0, // PROFIL: kaç içerik
    val sekme: Sekme? = null, // PROFIL: hangi bölüm (Reels, Shorts...)
    val durum: Durum = Durum.BEKLIYOR, // şu anki durum
    val yuzde: Float = 0f, // 0-100 arası ilerleme
    val bilgi: String = "Sırada", // ekranda gösterilecek kısa bilgi
    val baslik: String = "", // inen içeriğin adı (öğrenilince dolar)
    val girisGerekli: Boolean = false, // hata giriş yapılmadığı içinse true (ekranda "Giriş yap" çıkar)
) {
    // Birden çok içerik tek klasöre mi inecek
    val toplu: Boolean get() = linkTuru != LinkTuru.TEK
}

// Tüm indirmelerin listesi; ekran ve servis buradan okur/yazar
object IndirmeMerkezi {
    private val _isler = MutableStateFlow<List<IndirmeIsi>>(emptyList()) // iş listesi
    val isler: StateFlow<List<IndirmeIsi>> = _isler // ekranın okuduğu liste
    private val _biten = MutableStateFlow(0) // her biten işte artar, ekran listeyi yeniler
    val biten: StateFlow<Int> = _biten // ekranın dinlediği sayaç

    // Yeni indirme ekle ve servisi başlat
    fun ekle(ctx: Context, isi: IndirmeIsi) {
        _isler.update { it + isi } // listeye ekle
        ContextCompat.startForegroundService(ctx, Intent(ctx, IndirmeServisi::class.java)) // servisi başlat
    }

    // İndirmeyi iptal et
    fun iptal(id: String) {
        val isi = _isler.value.find { it.id == id } ?: return // işi bul
        if (isi.durum == Durum.INIYOR) { // iniyorsa çalışan işlemleri durdur
            YoutubeDL.getInstance().destroyProcessById(id) // yt-dlp
            Motor.galeriDurdur(id) // gallery-dl (liste alınıyorsa)
        }
        guncelle(id) { it.copy(durum = Durum.IPTAL, bilgi = "İptal edildi") } // durumu iptal yap
    }

    // Bitmiş/hatalı işi listeden kaldır
    fun kaldir(id: String) = _isler.update { liste -> liste.filterNot { it.id == id } }

    // Bir işin bilgilerini değiştir
    internal fun guncelle(id: String, degistir: (IndirmeIsi) -> IndirmeIsi) =
        _isler.update { liste -> liste.map { if (it.id == id) degistir(it) else it } }

    // Sıradaki bekleyen işi getir
    internal fun siradaki(): IndirmeIsi? = _isler.value.firstOrNull { it.durum == Durum.BEKLIYOR }

    // İş iptal edildi mi
    internal fun iptalMi(id: String) = _isler.value.find { it.id == id }?.durum == Durum.IPTAL

    // İş bittiğinde ekrana haber ver
    internal fun bitti() = _biten.update { it + 1 }
}
