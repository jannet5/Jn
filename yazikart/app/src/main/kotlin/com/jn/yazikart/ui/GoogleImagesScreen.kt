package com.jn.yazikart.ui // arayüz paketi

import android.annotation.SuppressLint // JavaScript uyarısını bilerek kapatmak için
import android.os.Handler // WebView'den bağlantı bilgisini almak için
import android.os.Looper // ana iş parçacığı
import android.webkit.WebView // uygulama içi tarayıcı
import android.webkit.WebViewClient // bağlantılar uygulamanın içinde açılsın diye
import androidx.activity.compose.BackHandler // geri tuşunu yakalamak için
import androidx.compose.foundation.background // arka plan boyama
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
import androidx.compose.foundation.layout.statusBarsPadding // durum çubuğu boşluğu
import androidx.compose.foundation.layout.width // genişlik
import androidx.compose.foundation.shape.RoundedCornerShape // yuvarlak köşe
import androidx.compose.foundation.text.KeyboardActions // klavye eylemleri
import androidx.compose.foundation.text.KeyboardOptions // klavye ayarları
import androidx.compose.material.icons.Icons // ikon seti
import androidx.compose.material.icons.automirrored.filled.ArrowBack // geri oku
import androidx.compose.material.icons.filled.Search // büyüteç
import androidx.compose.material.icons.filled.WifiOff // internet yok ikonu
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
import androidx.compose.ui.text.style.TextAlign // yazı hizası
import androidx.compose.ui.unit.dp // ölçü birimi
import androidx.compose.ui.viewinterop.AndroidView // klasik Android görünümünü Compose'a koymak için
import com.jn.yazikart.search.GooglePick // en iyi görsel adresini seçme

// Google Görseller: uygulamanın içinde gerçek Google sayfası açılır.
// Telefon doğrudan Google'a bağlanır (arada başka sunucu yok). Görsele basılı tutunca arka plan olur.
@SuppressLint("SetJavaScriptEnabled") // Google sayfası JavaScript olmadan çalışmaz
@Composable
fun GoogleImagesScreen(
    online: Boolean, // internet var mı
    busy: String?, // görsel iniyorsa açıklaması
    onPick: (String) -> Unit, // basılı tutulan görselin adresi
    onClose: () -> Unit, // ekranı kapat
) {
    val context = LocalContext.current // uygulama bağlamı
    val focus = LocalFocusManager.current // klavyeyi kapatmak için
    var query by rememberSaveable { mutableStateOf("") } // arama kutusundaki yazı
    val pick by rememberUpdatedState(onPick) // en güncel seçme işlevi
    val webView = remember { // uygulama içi tarayıcı (bir kez oluşturulur)
        WebView(context).apply { // tarayıcı
            settings.javaScriptEnabled = true // Google sayfası için gerekli
            settings.domStorageEnabled = true // Google sayfası için gerekli
            webViewClient = WebViewClient() // tıklanan bağlantılar uygulamanın içinde açılır
            var pendingExtra: String? = null // basılı tutulan resmin kendi adresi
            val handler = Handler(Looper.getMainLooper()) { msg -> // bağlantı bilgisi gelince
                val url = GooglePick.best(msg.data.getString("url"), msg.data.getString("src"), pendingExtra) // en iyi adres
                if (url != null) pick(url) // arka plan yapılmak üzere gönderiliyor
                true // mesaj işlendi
            }
            setOnLongClickListener { // basılı tutunca
                val hit = hitTestResult // parmağın altındaki öğe
                if (hit.type == WebView.HitTestResult.IMAGE_TYPE || hit.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) { // bir görselse
                    pendingExtra = hit.extra // resmin adresi
                    requestFocusNodeHref(handler.obtainMessage()) // varsa bağlantısı (orijinal görsel adresi) isteniyor
                    true // basılı tutma kullanıldı
                } else false // görsel değilse normal davranış
            }
            loadUrl("https://www.google.com/imghp?hl=tr") // Google Görseller ana sayfası
        }
    }
    DisposableEffect(Unit) { onDispose { webView.destroy() } } // ekran kapanınca tarayıcı kapatılıyor
    BackHandler { if (webView.canGoBack()) webView.goBack() else onClose() } // geri: önce sayfada geri, sonra çık

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { // tam ekran yüzey
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) { // alt alta
            Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) { // üst çubuk
                IconButton(onClick = onClose) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri") } // çık
                OutlinedTextField( // arama kutusu
                    value = query, // yazılan kelime
                    onValueChange = { query = it }, // kelime değişince
                    modifier = Modifier.weight(1f), // kalan genişlik
                    placeholder = { Text("Google Görsellerde ara") }, // ipucu
                    singleLine = true, // tek satır
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }, // büyüteç
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // klavyede "Ara" tuşu
                    keyboardActions = KeyboardActions(onSearch = { // "Ara"ya basınca
                        if (query.isNotBlank()) { focus.clearFocus(); webView.loadUrl(GooglePick.searchUrl(query)) } // Google'da aranıyor
                    }),
                    shape = RoundedCornerShape(12.dp), // yuvarlak köşe
                )
            }
            Text( // nasıl seçilir ipucu
                "Görsele dokun, büyüyünce üstüne basılı tut → arka plan olur.", // açıklama
                style = MaterialTheme.typography.bodyMedium, // gövde yazısı
                color = MaterialTheme.colorScheme.onSurfaceVariant, // soluk
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp), // kenar boşluğu
            )
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) { // sayfa alanı
                if (online) { // internet varsa Google sayfası
                    AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize()) // tarayıcı
                } else { // internet yoksa uyarı
                    Column(Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) { // ortalı
                        Icon(Icons.Default.WifiOff, contentDescription = null, tint = MaterialTheme.colorScheme.error) // ikon
                        Spacer(Modifier.size(12.dp)) // boşluk
                        Text("İnternet yok. Google Görseller için Wi-Fi ya da mobil veriyi aç.", textAlign = TextAlign.Center) // mesaj
                    }
                }
                if (busy != null) { // görsel iniyorsa
                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)) { // kutu
                        Row(Modifier.background(MaterialTheme.colorScheme.surface).padding(16.dp), verticalAlignment = Alignment.CenterVertically) { // yan yana
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
