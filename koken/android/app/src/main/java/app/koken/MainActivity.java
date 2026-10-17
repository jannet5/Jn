package app.koken;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.webkit.ServiceWorkerClient;
import android.webkit.ServiceWorkerController;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/** Köken: tek ekranlı kabuk. Web arayüzünü APK içinden https sahte adresiyle sunar, ağ köprüsünü ekler. */
public class MainActivity extends Activity {
    // Arayüzün yaşadığı sahte https adresi (service worker, localStorage ve fetch düzgün çalışsın diye)
    private static final String HOST = "appassets.androidplatform.net";
    private static final String START = "https://" + HOST + "/index.html";

    private WebView web;
    private KokenNative bridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hata ayıklama (chrome://inspect) yalnızca debug derlemede açılır
        boolean debuggable = (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        if (debuggable) WebView.setWebContentsDebuggingEnabled(true);

        // Tam ekran WebView; arka planı tema rengi yapıp açılışta beyaz parlamayı önle
        web = new WebView(this);
        web.setBackgroundColor(getResources().getColor(R.color.bg));
        setContentView(web);
        applyBars();

        // Ayarlar: JS ve DOM depolama açık, dosya erişimi kapalı
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);
        s.setMediaPlaybackRequiresUserGesture(true);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        // JS köprüsü: window.KokenNative
        bridge = new KokenNative(this, web);
        web.addJavascriptInterface(bridge, "KokenNative");

        // Sayfa istekleri: APK içindeki dosyaları sahte adresten sunar, dış bağlantıları tarayıcıya verir
        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest req) {
                Uri u = req.getUrl();
                if (!HOST.equals(u.getHost())) return null; // başka adreslere dokunma
                return serveAsset(u.getPath());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
                Uri u = req.getUrl();
                // Kendi adresimiz içinde gezinti WebView'da kalır
                if ("https".equals(u.getScheme()) && HOST.equals(u.getHost())) return false;
                // Geri kalan her şey (http, https, mailto, tel...) sistem tarayıcısında açılır
                try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception ignored) { }
                return true;
            }
        });

        // Service worker kendi isteklerini (sw.js ve önbellek dosyaları) ayrı istemciyle yapar; onu da APK'ya bağla
        if (Build.VERSION.SDK_INT >= 24) {
            ServiceWorkerController.getInstance().setServiceWorkerClient(new ServiceWorkerClient() {
                @Override
                public WebResourceResponse shouldInterceptRequest(WebResourceRequest req) {
                    Uri u = req.getUrl();
                    if (!HOST.equals(u.getHost())) return null;
                    return serveAsset(u.getPath());
                }
            });
        }

        // Döndürme/tema değişiminde sayfa geçmişini koru, ilk açılışta ana sayfayı yükle
        if (savedInstanceState != null && web.restoreState(savedInstanceState) != null) return;
        web.loadUrl(START);
    }

    /** Durum çubuğu ve gezinti çubuğu renklerini temaya uydurur (açık temada koyu ikonlar). */
    private void applyBars() {
        // Koyu temada mı olduğumuzu kaynak yapılandırmasından anla
        boolean dark = (getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        Window w = getWindow();
        int bg = getResources().getColor(R.color.bg);
        w.setStatusBarColor(bg);
        w.setNavigationBarColor(bg);
        // Açık zeminde sistem ikonlarını koyu çiz (API 23+ durum çubuğu, API 26+ gezinti çubuğu)
        View d = w.getDecorView();
        int f = d.getSystemUiVisibility();
        if (!dark) {
            f |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            if (Build.VERSION.SDK_INT >= 26) f |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
            f &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            if (Build.VERSION.SDK_INT >= 26) f &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        d.setSystemUiVisibility(f);
    }

    /** assets/ içinden dosyayı okuyup doğru MIME türüyle döndürür; yoksa 404. */
    private WebResourceResponse serveAsset(String path) {
        // Baştaki / atılır, boş yol index.html olur
        String p = (path == null || path.equals("/") || path.isEmpty()) ? "index.html" : path.substring(1);
        try {
            InputStream in = getAssets().open(p);
            WebResourceResponse r = new WebResourceResponse(mime(p), isText(p) ? "UTF-8" : null, in);
            // Aynı kökenli ama service worker / font istekleri için güvenli başlıklar
            Map<String, String> h = new HashMap<>();
            h.put("Access-Control-Allow-Origin", "*");
            h.put("Cache-Control", "no-cache");
            r.setResponseHeaders(h);
            return r;
        } catch (Exception e) {
            return new WebResourceResponse("text/plain", "UTF-8", 404, "Not Found", null,
                    new ByteArrayInputStream("404".getBytes()));
        }
    }

    /** Metin türü mü? (karakter kodlaması yalnız metne verilir) */
    private static boolean isText(String p) {
        String l = p.toLowerCase();
        return l.endsWith(".html") || l.endsWith(".js") || l.endsWith(".css") || l.endsWith(".svg")
                || l.endsWith(".webmanifest") || l.endsWith(".json") || l.endsWith(".txt");
    }

    /** Uzantıdan MIME türü bulur; WebView'ın yanlış/boş verebildiklerini elle sabitler. */
    private static String mime(String p) {
        String l = p.toLowerCase();
        if (l.endsWith(".js") || l.endsWith(".mjs")) return "text/javascript";
        if (l.endsWith(".webmanifest")) return "application/manifest+json";
        if (l.endsWith(".woff2")) return "font/woff2";
        if (l.endsWith(".woff")) return "font/woff";
        if (l.endsWith(".svg")) return "image/svg+xml";
        if (l.endsWith(".html")) return "text/html";
        if (l.endsWith(".css")) return "text/css";
        if (l.endsWith(".json")) return "application/json";
        if (l.endsWith(".png")) return "image/png";
        // Bilinmeyenler için sistem tablosuna bak
        String ext = MimeTypeMap.getFileExtensionFromUrl(p);
        String m = ext == null ? null : MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        return m != null ? m : "application/octet-stream";
    }

    /** Geri tuşu: sayfada geri gidilebiliyorsa git, yoksa uygulamadan çık. */
    @Override
    public void onBackPressed() {
        if (web != null && web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }

    /** Sayfa geçmişini kaydet (tema/dil değişimi sonrası yeniden yaratmada geri yüklenir). */
    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        if (web != null) web.saveState(out);
    }

    /** Temizlik: iş parçacığı havuzunu kapat, WebView'ı yok et. */
    @Override
    protected void onDestroy() {
        if (bridge != null) bridge.shutdown();
        if (web != null) { web.removeJavascriptInterface("KokenNative"); web.destroy(); web = null; }
        super.onDestroy();
    }
}
