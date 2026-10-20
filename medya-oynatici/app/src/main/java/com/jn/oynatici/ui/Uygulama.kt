package com.jn.oynatici.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.oynatici.data.Kutuphane
import com.jn.oynatici.data.Tur

// Kök arayüz: yığının en üstündeki sayfayı gösterir
@Composable
fun Uygulama(model: UygulamaModeli) {
    val ctx = LocalContext.current // dosya klasörlerine erişim için
    BackHandler(enabled = model.yigin.size > 1) { model.geri() } // geri tuşu bir önceki sayfaya döner

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { // arka plan + varsayılan yazı rengi (açık)
        when (val sayfa = model.yigin.last()) { // görünür sayfa
            Sayfa.Ana -> AnaEkran(model) // açılış
            is Sayfa.Liste -> ListeEkrani(model, sayfa.tur, sayfa.klasor) // klasör
            Sayfa.Calan -> CalanEkrani(model) // çalan müzik
            Sayfa.VideoIzle -> VideoEkrani(model) // video
            is Sayfa.Kirp -> KirpEkrani(model, sayfa.tur, sayfa.dosya) // kırpma
            is Sayfa.Indir -> IndirEkrani(model, sayfa.tur, sayfa.klasor, sayfa.link) // internetten indirme
            is Sayfa.Linkler -> LinklerEkrani(model, sayfa.klasor) // linkler
            Sayfa.Hesaplar -> HesaplarEkrani(model) // hesaplar
            is Sayfa.Giris -> GirisEkrani(model, sayfa.platform) // giriş sayfası
        }
    }

    // YouTube, TikTok, Instagram... uygulamasından "Paylaş" ile link geldiyse: ses mi video mu diye sor
    model.paylasilanLink?.let { link ->
        AlertDialog(
            onDismissRequest = { model.paylasilanLink = null }, // dışına dokununca kapat
            title = { Text("Bu linki nasıl indireyim?") }, // soru
            text = { Text(link, maxLines = 2) }, // linkin kendisi
            confirmButton = {
                TextButton(onClick = { // video olarak
                    model.paylasilanLink = null // soruyu kapat
                    val kok = Kutuphane.kok(ctx, Tur.VIDEO) // video bölümü
                    model.git(Sayfa.Liste(Tur.VIDEO, kok)) // video bölümüne git (geri basınca orada kalsın)
                    model.git(Sayfa.Indir(Tur.VIDEO, kok, link)) // indirme sayfasını linkle aç
                }) { Text("Video") }
            },
            dismissButton = {
                TextButton(onClick = { // müzik olarak
                    model.paylasilanLink = null // soruyu kapat
                    val kok = Kutuphane.kok(ctx, Tur.MUZIK) // müzik bölümü
                    model.git(Sayfa.Liste(Tur.MUZIK, kok)) // müzik bölümüne git
                    model.git(Sayfa.Indir(Tur.MUZIK, kok, link)) // indirme sayfasını linkle aç
                }) { Text("Müzik (MP3)") }
            },
        )
    }
}

// Açılış ekranı: sadece iki büyük buton
@Composable
fun AnaEkran(model: UygulamaModeli) {
    val ctx = LocalContext.current // klasör yolları için
    val durum by calmaDurumu(model.oynatici) // mini oynatıcı için
    Column(Modifier.fillMaxSize().safeDrawingPadding()) { // sistem çubuklarının altına girmesin
        Column(
            Modifier.weight(1f).fillMaxWidth().padding(20.dp), // ekranın çoğunu kapla
            verticalArrangement = Arrangement.spacedBy(16.dp), // butonlar arası boşluk
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) { // başlık satırı
                Text("Oynatıcı", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f)) // başlık
                IconButton(onClick = { model.git(Sayfa.Hesaplar) }) { Icon(Icons.Rounded.AccountCircle, "Hesaplar") } // giriş yapılan hesaplar
            }
            BuyukButon("Müzik", "MP3 ve ses dosyaların", Icons.Rounded.MusicNote, Modifier.weight(1f)) { // müzik butonu
                model.git(Sayfa.Liste(Tur.MUZIK, Kutuphane.kok(ctx, Tur.MUZIK))) // müzik bölümünü aç
            }
            BuyukButon("Video", "Videoların ve filmlerin", Icons.Rounded.Movie, Modifier.weight(1f)) { // video butonu
                model.git(Sayfa.Liste(Tur.VIDEO, Kutuphane.kok(ctx, Tur.VIDEO))) // video bölümünü aç
            }
        }
        if (durum.varMi && !durum.video) MiniOynatici(model, durum) // müzik çalıyorsa altta küçük oynatıcı
    }
}

// Ana ekrandaki büyük dokunmatik kart
@Composable
private fun BuyukButon(baslik: String, alt: String, ikon: ImageVector, modifier: Modifier, tikla: () -> Unit) {
    Surface(
        onClick = tikla, // dokununca
        shape = RoundedCornerShape(28.dp), // yuvarlak köşeler
        color = MaterialTheme.colorScheme.surfaceVariant, // kart rengi
        modifier = modifier.fillMaxWidth(), // tam genişlik
    ) {
        Column(
            Modifier.fillMaxSize().padding(24.dp), // iç boşluk
            verticalArrangement = Arrangement.Center, // ortala
            horizontalAlignment = Alignment.CenterHorizontally, // ortala
        ) {
            Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.primaryContainer) { // ikon kutusu
                Icon(ikon, null, Modifier.padding(20.dp).size(48.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer) // büyük ikon
            }
            Spacer(Modifier.height(16.dp)) // boşluk
            Text(baslik, fontSize = 26.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface) // "Müzik"
            Text(alt, color = MaterialTheme.colorScheme.onSurfaceVariant) // açıklama
        }
    }
}

// Tıklanabilir satır yardımcı (boş tıklama efekti olmadan)
fun Modifier.dokun(tikla: () -> Unit) = this.clickable(onClick = tikla)
