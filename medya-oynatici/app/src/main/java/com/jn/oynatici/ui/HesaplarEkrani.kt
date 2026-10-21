package com.jn.oynatici.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.jn.oynatici.data.Platform
import com.jn.oynatici.indir.Cerezler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Hesaplar: Instagram, X, TikTok, Pinterest'e uygulama içinden giriş (profil indirmek için gerekebiliyor)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HesaplarEkrani(model: UygulamaModeli) {
    val ctx = LocalContext.current // bağlam
    val kapsam = rememberCoroutineScope() // çıkış işlemi için
    var yenile by remember { mutableIntStateOf(0) } // çıkıştan sonra durumları yenile
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hesaplar") }, // başlık
                navigationIcon = { IconButton(onClick = { model.geri() }) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Geri") } }, // geri
            )
        },
    ) { bosluk ->
        Column(Modifier.fillMaxSize().padding(bosluk)) {
            Text( // açıklama
                "Instagram ve X profillerinden indirmek için çoğu zaman giriş gerekir. Giriş, sitenin kendi sayfasında yapılır; " +
                    "şifren uygulamaya kaydedilmez, sadece oturum çerezi bu telefonda kalır.",
                color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Cerezler.siteler.forEach { site -> // her site için bir satır
                val girisli = remember(yenile, model.yigin.size) { Cerezler.girisVar(site.platform) } // giriş yapılmış mı
                ListItem(
                    headlineContent = { Text(site.platform.ad) }, // site adı
                    supportingContent = { Text(if (girisli) "Giriş yapıldı" else "Giriş yapılmadı") }, // durum
                    leadingContent = { PlatformIkonu(site.platform) }, // logo
                    trailingContent = {
                        if (girisli) OutlinedButton(onClick = { kapsam.launch { Cerezler.cikisYap(ctx, site.platform); yenile++ } }) { Text("Çıkış") } // çıkış
                        else OutlinedButton(onClick = { model.git(Sayfa.Giris(site.platform)) }) { Text("Giriş yap") } // giriş
                    },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background), // arka plan
                )
            }
        }
    }
}

// Sitenin kendi giriş sayfası (WebView). Giriş olunca kendiliğinden kapanır.
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GirisEkrani(model: UygulamaModeli, platform: Platform) {
    val ctx = LocalContext.current // bağlam
    val site = remember(platform) { Cerezler.siteler.first { it.platform == platform } } // site bilgisi
    var yukleniyor by remember { mutableStateOf(true) } // sayfa yükleniyor mu
    var bitti by remember { mutableStateOf(false) } // giriş tamamlandı mı

    // Sayfa açıldığındaki oturum değeri: zaten girişliyse hemen kapanmasın, ancak yeni bir giriş olunca kapansın
    val ilkDeger = remember(platform) { Cerezler.anahtarDegeri(platform) }

    // Anahtar çerez gelene (ya da değişene) kadar arada bir kontrol et; gelince kaydet ve kapat
    LaunchedEffect(Unit) {
        while (!bitti) {
            delay(1500) // 1,5 saniyede bir bak
            if (Cerezler.girisVar(platform) && Cerezler.anahtarDegeri(platform) != ilkDeger) bitti = true // yeni giriş oldu
        }
        CookieManager.getInstance().flush() // çerezleri diske yaz
        Cerezler.dosyayaYaz(ctx) // indiriciler için dosyaya yaz
        delay(800) // kullanıcı "giriş yapıldı" sayfasını bir an görsün
        model.geri() // hesaplar ekranına dön
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${platform.ad} girişi") }, // başlık
                navigationIcon = { IconButton(onClick = { model.geri() }) { Icon(Icons.Rounded.Close, "Kapat") } }, // kapat
                actions = { TextButton(onClick = { bitti = true }) { Text("Bitti") } }, // elle bitir
            )
        },
    ) { bosluk ->
        Column(Modifier.fillMaxSize().padding(bosluk)) {
            if (yukleniyor) LinearProgressIndicator(Modifier.fillMaxWidth()) // yükleniyor çubuğu
            AndroidView(
                factory = { c ->
                    WebView(c).apply {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) // tam boyut
                        settings.javaScriptEnabled = true // giriş sayfaları JavaScript ister
                        settings.domStorageEnabled = true // giriş sayfaları yerel depolama ister
                        settings.userAgentString = settings.userAgentString.replace("; wv", "") // normal Chrome gibi görün (bazı siteler WebView'i engelliyor)
                        CookieManager.getInstance().setAcceptCookie(true) // çerezleri kabul et
                        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true) // giriş akışındaki üçüncü taraf çerezleri
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) { yukleniyor = false } // yükleme bitti
                        }
                        loadUrl(site.girisAdresi) // giriş sayfasını aç
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
    DisposableEffect(Unit) { onDispose { CookieManager.getInstance().flush() } } // çıkarken çerezleri kaydet
}
