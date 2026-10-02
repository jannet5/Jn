package com.jn.oynatici

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.jn.oynatici.oynat.CalmaServisi
import com.jn.oynatici.ui.OynaticiTema
import com.jn.oynatici.ui.Uygulama
import com.jn.oynatici.ui.UygulamaModeli

// Uygulamanın tek ekranı; içindeki sayfaları Compose çizer
class MainActivity : ComponentActivity() {
    private val model: UygulamaModeli by viewModels() // ekranlar arası ortak durum
    private var kontrolcuIstegi: ListenableFuture<MediaController>? = null // çalma servisine bağlantı

    // Bildirim izni isteyici (Android 13+)
    private val izinIste = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // üst sınıfı çağır
        enableEdgeToEdge() // ekranın tamamını kullan
        if (savedInstanceState == null) paylasimiAl(intent) // YouTube'dan paylaşılan link varsa al
        if (Build.VERSION.SDK_INT >= 33) izinIste.launch(Manifest.permission.POST_NOTIFICATIONS) // indirme bildirimi için izin
        setContent { OynaticiTema { Uygulama(model) } } // arayüzü çiz
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent) // üst sınıfı çağır
        paylasimiAl(intent) // uygulama açıkken paylaşım gelirse
    }

    // "Paylaş" ile gelen metinden linki çıkar
    private fun paylasimiAl(intent: Intent?) {
        if (intent?.action != Intent.ACTION_SEND) return // paylaşım değilse çık
        val metin = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return // paylaşılan metin
        Regex("""https?://\S+""").find(metin)?.let { model.paylasilanLink = it.value } // içindeki linki al
    }

    override fun onStart() {
        super.onStart() // üst sınıfı çağır
        val jeton = SessionToken(this, ComponentName(this, CalmaServisi::class.java)) // çalma servisinin adresi
        val istek = MediaController.Builder(this, jeton).buildAsync() // servise bağlan
        kontrolcuIstegi = istek // isteği sakla
        istek.addListener({ runCatching { model.oynatici = istek.get() } }, ContextCompat.getMainExecutor(this)) // bağlanınca modele ver
    }

    override fun onStop() {
        model.oynatici?.let { if (model.videoCaliyor()) it.pause() } // video arka planda çalmasın (müzik çalmaya devam eder)
        model.oynatici = null // bağlantıyı bırak
        kontrolcuIstegi?.let { MediaController.releaseFuture(it) } // servisten ayrıl
        kontrolcuIstegi = null // temizle
        super.onStop() // üst sınıfı çağır
    }
}
