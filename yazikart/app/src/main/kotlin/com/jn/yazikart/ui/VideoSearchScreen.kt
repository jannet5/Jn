package com.jn.yazikart.ui // arayüz paketi

import android.annotation.SuppressLint // JavaScript uyarısını bilerek kapatmak için
import android.webkit.CookieManager // sitenin çerezleri (indirme izni için)
import android.webkit.WebView // uygulama içi tarayıcı
import android.webkit.WebViewClient // bağlantılar uygulamanın içinde açılsın diye
import androidx.activity.compose.BackHandler // geri tuşu
import androidx.compose.foundation.layout.Box // üst üste yerleşim
import androidx.compose.foundation.layout.Column // alt alta yerleşim
import androidx.compose.foundation.layout.Row // yan yana yerleşim
import androidx.compose.foundation.layout.Spacer // boşluk
import androidx.compose.foundation.layout.fillMaxSize // tüm alan
import androidx.compose.foundation.layout.fillMaxWidth // tam genişlik
import androidx.compose.foundation.layout.imePadding // klavye boşluğu
import androidx.compose.foundation.layout.navigationBarsPadding // alt çubuk boşluğu
import androidx.compose.foundation.layout.padding // dış boşluk
import androidx.compose.foundation.layout.size // boyut
import androidx.compose.foundation.layout.statusBarsPadding // üst çubuk boşluğu
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.text.KeyboardActions // klavye eylemleri
import androidx.compose.foundation.text.KeyboardOptions // klavye ayarları
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.automirrored.filled.ArrowBack // geri oku
import androidx.compose.material.icons.filled.Search // büyüteç
import androidx.compose.material3.CircularProgressIndicator // yükleniyor göstergesi
import androidx.compose.material3.Icon // ikon
import androidx.compose.material3.IconButton // ikon buton
import androidx.compose.material3.MaterialTheme // tema
import androidx.compose.material3.OutlinedTextField // yazı kutusu
import androidx.compose.material3.Surface // yüzey
import androidx.compose.material3.Text // yazı
import androidx.compose.runtime.Composable // Compose bileşeni işareti
import androidx.compose.runtime.DisposableEffect // kapanışta temizlik
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
import com.jn.yazikart.search.VideoSite // video sitesi adresleri

// İnternetten video: uygulamanın içinde ücretsiz video sitesi Pexels açılır (telefon doğrudan siteye bağlanır).
// Videoyu açıp "Ücretsiz indir"e basınca video telefona iner ve arka plana eklenir.
@SuppressLint("SetJavaScriptEnabled") // site JavaScript olmadan çalışmaz
@Composable
fun VideoSearchScreen(
    busy: String?, // video iniyorsa açıklaması
    onDownload: (url: String, userAgent: String, cookies: String?) -> Unit, // indirilecek video
    onClose: () -> Unit, // ekranı kapat
) {
    val context = LocalContext.current // uygulama bağlamı
    val focus = LocalFocusManager.current // klavyeyi kapatmak için
    var query by rememberSaveable { mutableStateOf("") } // arama kutusu
    val download by rememberUpdatedState(onDownload) // en güncel indirme işlevi
    val webView = remember { // uygulama içi tarayıcı
        WebView(context).apply {
            settings.javaScriptEnabled = true // site için gerekli
            settings.domStorageEnabled = true // site için gerekli
            settings.mediaPlaybackRequiresUserGesture = true // videolar kendiliğinden sesli oynamasın
            webViewClient = WebViewClient() // bağlantılar uygulamanın içinde
            setDownloadListener { url, userAgent, _, mimetype, _ -> // sitede "İndir"e basıldı
                if (mimetype?.startsWith("image/") == true) return@setDownloadListener // resim indirmesi değil, video bekleniyor
                download(url, userAgent, CookieManager.getInstance().getCookie(url)) // video indiriliyor (çerezlerle)
            }
            loadUrl(VideoSite.HOME) // video ana sayfası
        }
    }
    DisposableEffect(Unit) { onDispose { webView.destroy() } } // ekran kapanınca tarayıcı kapatılıyor
    BackHandler { if (webView.canGoBack()) webView.goBack() else onClose() } // geri: önce sayfada, sonra çık

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
                        if (query.isNotBlank()) { focus.clearFocus(); webView.loadUrl(VideoSite.searchUrl(query)) } // sitede aranıyor
                    }),
                    shape = RoundedCornerShape(12.dp), // yuvarlak köşe
                )
            }
            Text( // nasıl eklenir ipucu
                "Videoya dokun, açılınca \"Ücretsiz indir\"e bas → video eklenir. (Pexels: ücretsiz, her yerde kullanılabilir videolar)", // açıklama
                style = MaterialTheme.typography.bodyMedium, // gövde yazısı
                color = MaterialTheme.colorScheme.onSurfaceVariant, // soluk
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp), // kenar boşluğu
            )
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) { // sayfa alanı
                AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize()) // tarayıcı
                if (busy != null) { // video iniyorsa
                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 4.dp) { // kutu
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { // yan yana
                            CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp) // dönen gösterge
                            Spacer(Modifier.width(12.dp)) // boşluk
                            Text(busy) // açıklama
                        }
                    }
                }
            }
        }
    }
}
