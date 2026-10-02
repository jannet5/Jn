package com.jn.oynatici.indir

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.jn.oynatici.data.Tur
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.UUID

// Bir indirme işinin durumu
enum class Durum { BEKLIYOR, INIYOR, BITTI, HATA, IPTAL }

// Tek bir indirme işi (bir video, bir ses ya da bütün bir oynatma listesi)
data class IndirmeIsi(
    val id: String = UUID.randomUUID().toString(), // işin kimliği
    val url: String, // YouTube linki
    val tur: Tur, // Müzik mi Video mu
    val hedef: File, // hangi klasöre inecek
    val kalite: Int?, // video için en fazla yükseklik (720 gibi), null = en yüksek
    val liste: Boolean, // oynatma listesi mi
    val durum: Durum = Durum.BEKLIYOR, // şu anki durum
    val yuzde: Float = 0f, // 0-100 arası ilerleme
    val bilgi: String = "Sırada", // ekranda gösterilecek kısa bilgi
    val baslik: String = "", // inen videonun adı (öğrenilince dolar)
)

// Tüm indirmelerin listesi; ekran ve servis buradan okur/yazar
object IndirmeMerkezi {
    private val _isler = MutableStateFlow<List<IndirmeIsi>>(emptyList()) // iş listesi
    val isler: StateFlow<List<IndirmeIsi>> = _isler // ekranın okuduğu liste
    private val _biten = MutableStateFlow(0) // her biten işte artar, ekran listeyi yeniler
    val biten: StateFlow<Int> = _biten // ekranın dinlediği sayaç

    // Linkin oynatma listesi olup olmadığını anla
    fun listeMi(url: String): Boolean = Regex("""[?&]list=""").containsMatchIn(url) || url.contains("/playlist")

    // Yeni indirme ekle ve servisi başlat
    fun ekle(ctx: Context, url: String, tur: Tur, hedef: File, kalite: Int?) {
        val isi = IndirmeIsi(url = url.trim(), tur = tur, hedef = hedef, kalite = kalite, liste = listeMi(url)) // işi oluştur
        _isler.update { it + isi } // listeye ekle
        ContextCompat.startForegroundService(ctx, Intent(ctx, IndirmeServisi::class.java)) // servisi başlat
    }

    // İndirmeyi iptal et
    fun iptal(id: String) {
        val isi = _isler.value.find { it.id == id } ?: return // işi bul
        if (isi.durum == Durum.INIYOR) YoutubeDL.getInstance().destroyProcessById(id) // iniyorsa işlemi durdur
        guncelle(id) { it.copy(durum = Durum.IPTAL, bilgi = "İptal edildi") } // durumu iptal yap
    }

    // Bitmiş/hatalı işi listeden kaldır
    fun kaldir(id: String) = _isler.update { liste -> liste.filterNot { it.id == id } }

    // Bir işin bilgilerini değiştir
    internal fun guncelle(id: String, degistir: (IndirmeIsi) -> IndirmeIsi) =
        _isler.update { liste -> liste.map { if (it.id == id) degistir(it) else it } }

    // Sıradaki bekleyen işi getir
    internal fun siradaki(): IndirmeIsi? = _isler.value.firstOrNull { it.durum == Durum.BEKLIYOR }

    // İş bittiğinde ekrana haber ver
    internal fun bitti() = _biten.update { it + 1 }
}
