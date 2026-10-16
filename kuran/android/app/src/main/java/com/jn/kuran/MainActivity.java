package com.jn.kuran;  // uygulama paketi

import android.app.Activity;  // temel ekran sınıfı
import android.content.Intent;  // dış bağlantıları başka uygulamada açmak için
import android.graphics.Color;  // renk dönüşümü
import android.net.Uri;  // adres işlemleri
import android.os.Build;  // sürüm kontrolü
import android.os.Bundle;  // durum paketi
import android.view.View;  // ekran bayrakları
import android.view.Window;  // pencere (durum çubuğu rengi için)
import android.view.WindowManager;  // pencere bayrakları
import android.webkit.JavascriptInterface;  // sayfadan Java'ya çağrı
import android.webkit.WebResourceRequest;  // WebView isteği
import android.webkit.WebResourceResponse;  // WebView cevabı
import android.webkit.WebSettings;  // WebView ayarları
import android.webkit.WebView;  // web görünümü
import android.webkit.WebViewClient;  // istekleri yakalamak için

import androidx.webkit.WebViewAssetLoader;  // yerel dosyaları https adresi altında sunar

/** Kur'an Okuyucu: web uygulamasını (assets) tam ekran WebView içinde çalıştırır. İnternet kullanmaz. */
public class MainActivity extends Activity {
    private static final String HOST = "appassets.androidplatform.net";  // sahte (yalnızca uygulama içi) alan adı
    private static final String HOME = "https://" + HOST + "/assets/index.html";  // açılış sayfası
    private WebView web;  // web görünümü

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);  // üst sınıf
        // assets klasörünü https://appassets.androidplatform.net/assets/ altında sun (localStorage tutarlı çalışsın)
        final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
                .setDomain(HOST)
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();
        web = new WebView(this);  // web görünümünü oluştur
        setContentView(web);  // tüm ekranı kaplasın
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
        web.addJavascriptInterface(new Bridge(), "KuranApp");  // tema değişince durum çubuğu rengi için köprü
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
        if (saved == null) web.loadUrl(HOME); else web.restoreState(saved);  // ilk açılış ya da döndürme sonrası
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);  // üst sınıf
        web.saveState(out);  // sayfa durumunu sakla
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack();  // Git paneli açıksa kapanır (sayfa geçmişine eklenir)
        else super.onBackPressed();  // değilse uygulamadan çık
    }

    /** Web sayfasından çağrılır: durum çubuğu ve gezinme çubuğu rengini temaya uydurur. */
    private class Bridge {
        @JavascriptInterface
        public void setBars(final String color, final boolean dark) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int c = Color.parseColor(color);  // tema zemin rengi
                        Window w = getWindow();  // pencere
                        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  // çubuk rengini çizdir
                        w.setStatusBarColor(c);  // durum çubuğu
                        w.setNavigationBarColor(c);  // gezinme çubuğu
                        int f = w.getDecorView().getSystemUiVisibility();  // mevcut bayraklar
                        int light = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;  // açık zeminde koyu simgeler
                        if (Build.VERSION.SDK_INT >= 26) light |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                        w.getDecorView().setSystemUiVisibility(dark ? (f & ~light) : (f | light));
                        web.setBackgroundColor(c);  // WebView zemini
                    } catch (Exception e) { /* geçersiz renk: yoksay */ }
                }
            });
        }
    }
}
