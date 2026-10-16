package com.jn.kuran;  // uygulama paketi

import android.content.Intent;  // dış bağlantıları başka uygulamada açmak için
import android.graphics.Color;  // renk dönüşümü
import android.net.Uri;  // adres işlemleri
import android.os.Bundle;  // durum paketi
import android.view.View;  // ekran görünümü
import android.view.ViewGroup;  // yerleşim parametreleri
import android.webkit.JavascriptInterface;  // sayfadan Java'ya çağrı
import android.webkit.WebResourceRequest;  // WebView isteği
import android.webkit.WebResourceResponse;  // WebView cevabı
import android.webkit.WebSettings;  // WebView ayarları
import android.webkit.WebView;  // web görünümü
import android.webkit.WebViewClient;  // istekleri yakalamak için
import android.widget.FrameLayout;  // kök yerleşim

import androidx.activity.ComponentActivity;  // modern Activity tabanı
import androidx.activity.EdgeToEdge;  // kenardan kenara ekran (Android 15+ zorunlu)
import androidx.activity.OnBackPressedCallback;  // geri hareketi (predictive back uyumlu)
import androidx.activity.SystemBarStyle;  // sistem çubuğu stili
import androidx.core.graphics.Insets;  // sistem çubuğu boyutları
import androidx.core.view.ViewCompat;  // inset dinleyici
import androidx.core.view.WindowCompat;  // pencere yardımcıları
import androidx.core.view.WindowInsetsCompat;  // inset türleri
import androidx.core.view.WindowInsetsControllerCompat;  // çubuk simge rengi
import androidx.webkit.WebViewAssetLoader;  // yerel dosyaları https adresi altında sunar

/** Kur'an Okuyucu: web uygulamasını (assets) tam ekran WebView içinde çalıştırır. İnternet kullanmaz. */
public class MainActivity extends ComponentActivity {
    private static final String HOST = "appassets.androidplatform.net";  // sahte (yalnızca uygulama içi) alan adı
    private static final String HOME = "https://" + HOST + "/assets/index.html";  // açılış sayfası
    private FrameLayout root;  // kök yerleşim (sistem çubuklarının arkasındaki zemin rengi burada)
    private WebView web;  // web görünümü

    @Override
    protected void onCreate(Bundle saved) {
        // Çubuklar şeffaf, uygulama kenardan kenara çizilir (hedef Android 15+ için zorunlu)
        EdgeToEdge.enable(this,
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT));
        super.onCreate(saved);  // üst sınıf
        // assets klasörünü https://appassets.androidplatform.net/assets/ altında sun (localStorage tutarlı çalışsın)
        final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
                .setDomain(HOST)
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();
        root = new FrameLayout(this);  // kök
        root.setBackgroundColor(Color.parseColor("#fbf8f0"));  // kâğıt rengi
        web = new WebView(this);  // web görünümü
        root.addView(web, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(root);  // tüm ekranı kaplasın
        // Durum çubuğu, gezinme çubuğu ve klavye kadar boşluk bırak: içerik çubukların altına girmesin
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets i = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.ime());
            v.setPadding(i.left, i.top, i.right, i.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        WebSettings s = web.getSettings();  // ayarlar
        s.setJavaScriptEnabled(true);  // uygulama JavaScript ile çalışır
        s.setDomStorageEnabled(true);  // localStorage: kaldığın yer, yazı boyutu, tema
        s.setAllowFileAccess(false);  // dosya erişimi kapalı (güvenlik)
        s.setAllowContentAccess(false);  // content:// erişimi kapalı
        s.setTextZoom(100);  // telefonun yazı boyutu ayarı Kur'an düzenini bozmasın (kendi yakınlaştırmamız var)
        s.setSupportZoom(false);  // tarayıcı yakınlaştırması kapalı
        s.setBuiltInZoomControls(false);  // zoom düğmeleri kapalı
        web.setOverScrollMode(View.OVER_SCROLL_NEVER);  // kenarda parlama efekti yok
        web.setBackgroundColor(Color.parseColor("#fbf8f0"));  // yüklenirken kâğıt rengi
        web.setKeepScreenOn(true);  // okurken ekran kararmasın
        web.addJavascriptInterface(new Bridge(), "KuranApp");  // tema değişince çubuk rengi için köprü
        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView v, WebResourceRequest r) {
                return loader.shouldInterceptRequest(r.getUrl());  // yerel dosyaları ver
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                Uri u = r.getUrl();  // gidilmek istenen adres
                if (HOST.equals(u.getHost())) return false;  // uygulamanın kendi sayfası: WebView açsın
                try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception e) { /* tarayıcı yoksa yoksay */ }
                return true;  // dış bağlantı tarayıcıda açılır
            }
        });
        // Geri hareketi: Git paneli açıksa kapanır (sayfa geçmişine eklenir), değilse uygulamadan çıkılır
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (web.canGoBack()) {
                    web.goBack();  // paneli kapat
                } else {
                    setEnabled(false);  // bu callback'i devre dışı bırak
                    getOnBackPressedDispatcher().onBackPressed();  // sistemin varsayılanı: uygulamadan çık
                }
            }
        });
        if (saved == null) web.loadUrl(HOME); else web.restoreState(saved);  // ilk açılış ya da döndürme sonrası
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);  // üst sınıf
        web.saveState(out);  // sayfa durumunu sakla
    }

    /** Web sayfasından çağrılır: çubukların arkasındaki zemin rengini ve simge rengini temaya uydurur. */
    private class Bridge {
        @JavascriptInterface
        public void setBars(final String color, final boolean dark) {
            runOnUiThread(() -> {
                try {
                    int c = Color.parseColor(color);  // tema zemin rengi
                    root.setBackgroundColor(c);  // çubukların arkası
                    web.setBackgroundColor(c);  // WebView zemini
                    WindowInsetsControllerCompat ctl = WindowCompat.getInsetsController(getWindow(), root);
                    ctl.setAppearanceLightStatusBars(!dark);  // açık zeminde koyu simgeler
                    ctl.setAppearanceLightNavigationBars(!dark);
                } catch (Exception e) { /* geçersiz renk: yoksay */ }
            });
        }
    }
}
