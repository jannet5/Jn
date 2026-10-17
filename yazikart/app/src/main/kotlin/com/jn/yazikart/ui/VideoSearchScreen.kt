package com.jn.yazikart.ui // arayüz paketi

import android.annotation.SuppressLint // JavaScript uyarısını bilerek kapatmak için
import android.webkit.CookieManager // sitenin çerezleri
import android.webkit.WebResourceRequest // sayfanın yaptığı istekler
import android.webkit.WebResourceResponse // (dokunmadan geçiriyoruz)
import android.webkit.WebView // uygulama içi tarayıcı
import android.webkit.WebViewClient // bağlantılar uygulamanın içinde açılsın diye
import androidx.activity.compose.BackHandler // geri tuşu
import androidx.compose.foundation.horizontalScroll // yatay kaydırma
import androidx.compose.foundation.layout.Arrangement // dizilim aralığı
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.foundation.layout.Column // alt alta yerleşim
import androidx.compose.foundation.layout.Row // yan yana yerleşim
import androidx.compose.foundation.layout.Spacer // boşluk
import androidx.compose.foundation.layout.fillMaxSize // tüm alan
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.height // yükseklik
import androidx.compose.foundation.layout.imePadding // klavye boşluğu
import androidx.compose.foundation.layout.navigationBarsPadding // alt çubuk boşluğu
import androidx.compose.foundation.layout.padding // dış boşluk
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.layout.statusBarsPadding // üst çubuk boşluğu
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.foundation.rememberScrollState // kaydırma durumu
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.text.KeyboardActions // klavye eylemleri
import androidx.compose.foundation.text.KeyboardOptions // klavye ayarları
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.automirrored.filled.ArrowBack // geri oku
import androidx.compose.material.icons.filled.Add // artı ikonu
import androidx.compose.material.icons.filled.Search // büyüteç
import androidx.compose.material3.Button // birincil buton
import androidx.compose.material3.CircularProgressIndicator // yükleniyor göstergesi
import androidx.compose.material3.FilterChip // site seçimi
import androidx.compose.material3.Icon // ikon
import androidx.compose.material3.IconButton // ikon buton
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.OutlinedTextField // yazı kutusu
import androidx.compose.material3.Surface // yüzey
import androidx.compose.material3.Text // yazı
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.DisposableEffect // kapanışta temizlik
import androidx.compose.runtime.LaunchedEffect // mesajı bir süre sonra silmek için
import androidx.compose.runtime.getValue // durum okuma
import androidx.compose.runtime.mutableStateOf // durum
import androidx.compose.runtime.remember // hatırlama
import androidx.compose.runtime.rememberUpdatedState // güncel değeri izleme
import androidx.compose.runtime.saveable.rememberSaveable // ekran dönse de hatırlama
import androidx.compose.runtime.setValue // durum yazma
import androidx.compose.ui.Alignment // hizalama
import androidx.compose.ui.Modifier // değiştiriciler
import androidx.compose.ui.platform.LocalContext // uygulama bağlamı
import androidx.compose.ui.platform.LocalFocusManager // klavyeyi kapatmak için
import androidx.compose.ui.text.input.ImeAction // klavyedeki "Ara" tuşu
import androidx.compose.ui.unit.dp // ölçü birimi
import androidx.compose.ui.viewinterop.AndroidView // klasik Android görünümü
import com.jn.yazikart.search.VideoPick // eklenecek videoyu seçme
import com.jn.yazikart.search.VideoSite // video siteleri
import kotlinx.coroutines.delay // bekleme
import org.json.JSONArray // sayfadan gelen video listesi
import org.json.JSONTokener // JavaScript cevabını çözmek için
import java.util.concurrent.atomic.AtomicReference // arka plandan güvenle yazılan değer

// Sayfadaki tüm <video> öğelerinin adresini, oynayıp oynamadığını ve ekrandaki boyutunu toplayan küçük betik
private const val FIND_VIDEOS_JS = """(function(){var out=[];var vs=document.querySelectorAll('video');for(var i=0;i<vs.length;i++){var v=vs[i];var s=v.currentSrc||v.src;if(!s){var so=v.querySelector('source');if(so)s=so.src;}var r=v.getBoundingClientRect();var vis=Math.max(0,Math.min(r.right,innerWidth)-Math.max(r.left,0))*Math.max(0,Math.min(r.bottom,innerHeight)-Math.max(r.top,0));if(s)out.push({src:s,playing:!v.paused&&!v.ended,area:vis});}return JSON.stringify(out);})()"""

// İnternetten video: ücretsiz video sitesi uygulamanın içinde açılır (telefon doğrudan siteye bağlanır).
// Videoyu açıp oynat → "Bu videoyu ekle"ye bas → oynayan video doğrudan telefona iner. Giriş (login) gerekmez.
@SuppressLint("SetJavaScriptEnabled") // siteler JavaScript olmadan çalışmaz
@Composable
fun VideoSearchScreen(
    busy: String?, // video iniyorsa açıklaması
    onDownload: (url: String, userAgent: String, cookies: String?, referer: String?) -> Unit, // indirilecek video
    onClose: () -> Unit, // ekranı kapat
) {
    val context = LocalContext.current // uygulama bağlamı
    val focus = LocalFocusManager.current // klavyeyi kapatmak için
    var query by rememberSaveable { mutableStateOf("") } // arama kutusu
    var site by rememberSaveable { mutableStateOf(VideoSite.PEXELS) } // seçili site
    var hint by remember { mutableStateOf<String?>(null) } // kısa uyarı mesajı
    val download by rememberUpdatedState(onDownload) // en güncel indirme işlevi
    val lastMp4 = remember { AtomicReference<String?>(null) } // sayfanın arka planda yüklediği son video dosyası
    val webView = remember { // uygulama içi tarayıcı
        WebView(context).apply {
            settings.javaScriptEnabled = true // siteler için gerekli
            settings.domStorageEnabled = true // siteler için gerekli
            settings.mediaPlaybackRequiresUserGesture = false // video önizlemeleri dokununca hemen oynasın
            webViewClient = object : WebViewClient() { // bağlantılar uygulamanın içinde
                override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                    val u = request.url.toString() // sayfanın istediği adres
                    if (VideoPick.looksLikeVideoFile(u)) lastMp4.set(u) // video dosyasıysa not alınıyor (değiştirilmeden)
                    return null // istek olduğu gibi devam eder
                }
                override fun onPageStarted(view: WebView, url: String?, favicon: android.graphics.Bitmap?) { lastMp4.set(null) } // yeni sayfa: eski not silinir
            }
            setDownloadListener { url, userAgent, _, mimetype, _ -> // sitenin kendi "İndir" düğmesi de çalışsın
                if (mimetype?.startsWith("image/") == true) return@setDownloadListener // resim değil, video bekleniyor
                download(url, userAgent, CookieManager.getInstance().getCookie(url), this.url) // indir
            }
            loadUrl(VideoSite.PEXELS.home) // ilk site
        }
    }
    DisposableEffect(Unit) { onDispose { webView.destroy() } } // ekran kapanınca tarayıcı kapatılıyor
    BackHandler { if (webView.canGoBack()) webView.goBack() else onClose() } // geri: önce sayfada, sonra çık
    LaunchedEffect(hint) { if (hint != null) { delay(4000); hint = null } } // uyarı 4 sn sonra kaybolur

    // Sayfadaki oynayan videoyu bulup ekler
    fun addPlayingVideo() {
        webView.evaluateJavascript(FIND_VIDEOS_JS) { raw -> // sayfadaki videolar soruluyor
            val list = runCatching { // cevap çözülüyor
                val arr = JSONArray(JSONTokener(raw).nextValue() as String) // "[...]" metni diziye
                (0 until arr.length()).map { i -> arr.getJSONObject(i).let { VideoPick.Candidate(it.optString("src"), it.optBoolean("playing"), it.optDouble("area").toFloat()) } } // adaylar
            }.getOrDefault(emptyList()) // çözülemezse boş
            val url = VideoPick.choose(list, lastMp4.get()) // en uygun video
            when {
                url != null -> download(url, webView.settings.userAgentString, CookieManager.getInstance().getCookie(url), webView.url) // indiriliyor
                VideoPick.onlyProtected(list) -> hint = "Bu sitedeki video korumalı, alınamıyor. Pexels, Pixabay, Coverr ya da Mixkit'ten seç." // YouTube gibi
                else -> hint = "Önce bir videoyu aç ve oynat, sonra bu düğmeye bas." // video bulunamadı
            }
        }
    }

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { // tam ekran yüzey
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) { // alt alta
            Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) { // üst çubuk
                IconButton(onClick = onClose) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri") } // çık
                OutlinedTextField( // arama kutusu
                    value = query, // yazılan kelime
                    onValueChange = { query = it }, // değişince
                    modifier = Modifier.weight(1f), // kalan genişlik
                    placeholder = { Text("Video ara (örn. elma)") }, // ipucu
                    singleLine = true, // tek satır
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }, // büyüteç
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // "Ara" tuşu
                    keyboardActions = KeyboardActions(onSearch = { // "Ara"ya basınca
                        if (query.isNotBlank()) { focus.clearFocus(); webView.loadUrl(site.searchUrl(query)) } // seçili sitede ara
                    }),
                    shape = RoundedCornerShape(12.dp), // yuvarlak köşe
                )
            }
            Row( // site seçimi
                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp), // yatay kaydırılabilir
                horizontalArrangement = Arrangement.spacedBy(8.dp), // aralık
            ) {
                VideoSite.entries.forEach { s -> // her site
                    FilterChip( // seçilebilir çip
                        selected = s == site, // seçili mi
                        onClick = { site = s; webView.loadUrl(if (query.isNotBlank()) s.searchUrl(query) else s.home) }, // siteyi değiştir (kelime varsa orada da ara)
                        label = { Text(s.label) }, // site adı
                    )
                }
            }
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) { // sayfa alanı
                AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize()) // tarayıcı
                val message = busy ?: hint // gösterilecek durum
                if (message != null) { // iniyorsa ya da uyarı varsa
                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 4.dp, modifier = Modifier.padding(24.dp)) { // kutu
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { // yan yana
                            if (busy != null) { CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp); Spacer(Modifier.width(12.dp)) } // dönen gösterge
                            Text(message) // açıklama
                        }
                    }
                }
            }
            Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) { // alt eylem alanı
                Text( // nasıl eklenir
                    "Videoyu aç ve oynat, sonra bu düğmeye bas. Giriş yapman gerekmez.", // açıklama
                    style = MaterialTheme.typography.bodySmall, // küçük yazı
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // soluk
                )
                Spacer(Modifier.height(8.dp)) // boşluk
                Button(onClick = { addPlayingVideo() }, enabled = busy == null, modifier = Modifier.fillMaxWidth().height(52.dp)) { // birincil eylem
                    Icon(Icons.Default.Add, contentDescription = null) // artı
                    Spacer(Modifier.width(8.dp)) // boşluk
                    Text("Bu videoyu ekle") // etiket
                }
            }
        }
    }
}
