package app.koken;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Köken: tek ekranlı kabuk. Web arayüzünü APK içinden sunar, ağ isteklerini CORS'suz yapan köprüyü verir. */
public class MainActivity extends Activity {
    // Arayüzün yaşadığı sahte https adresi (service worker, localStorage ve fetch düzgün çalışsın diye)
    private static final String HOST = "appassets.androidplatform.net";
    private static final String UA = "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36";
    private static final int MAX_BYTES = 2 * 1024 * 1024; // tek yanıt için 2 MB üst sınır

    private WebView web;
    private final ExecutorService pool = Executors.newFixedThreadPool(4); // ağ istekleri arka planda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this); // tam ekran WebView
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true); // arayüz JS ile çalışır
        s.setDomStorageEnabled(true); // son aramalar localStorage'da durur
        s.setAllowFileAccess(false); // dosya erişimi kapalı
        s.setAllowContentAccess(false);
        s.setMediaPlaybackRequiresUserGesture(true);
        web.addJavascriptInterface(new Bridge(), "KokenNative"); // JS'in çağıracağı köprü
        web.setWebViewClient(new WebViewClient() {
            // APK içindeki dosyaları sahte adresten sunar
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest req) {
                Uri u = req.getUrl();
                if (!HOST.equals(u.getHost())) return null; // başka adreslere dokunma
                return serveAsset(u.getPath());
            }

            // Kendi adresimiz dışındaki bağlantıları tarayıcıda açar
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
                Uri u = req.getUrl();
                if (HOST.equals(u.getHost())) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception ignored) { }
                return true;
            }
        });
        if (savedInstanceState != null) web.restoreState(savedInstanceState); // dönüşte sayfayı koru
        else web.loadUrl("https://" + HOST + "/index.html");
    }

    /** assets/ içinden dosyayı okuyup doğru MIME türüyle döndürür. */
    private WebResourceResponse serveAsset(String path) {
        String p = (path == null || path.equals("/")) ? "index.html" : path.substring(1); // baştaki / atılır
        try {
            InputStream in = getAssets().open(p);
            return new WebResourceResponse(mime(p), "UTF-8", in);
        } catch (Exception e) {
            return new WebResourceResponse("text/plain", "UTF-8", 404, "Not Found", null,
                    new java.io.ByteArrayInputStream("404".getBytes()));
        }
    }

    /** Uzantıdan MIME türü bulur; WebView'ın bilmediklerini elle verir. */
    private static String mime(String p) {
        String l = p.toLowerCase();
        if (l.endsWith(".js") || l.endsWith(".mjs")) return "text/javascript";
        if (l.endsWith(".webmanifest")) return "application/manifest+json";
        if (l.endsWith(".woff2")) return "font/woff2";
        if (l.endsWith(".svg")) return "image/svg+xml";
        if (l.endsWith(".html")) return "text/html";
        if (l.endsWith(".css")) return "text/css";
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

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        if (web != null) web.saveState(out);
    }

    @Override
    protected void onDestroy() {
        pool.shutdownNow();
        if (web != null) { web.removeJavascriptInterface("KokenNative"); web.destroy(); }
        super.onDestroy();
    }

    /** JS'in çağırdığı köprü: tarayıcı kısıtı olmadan https isteği atar, sonucu geri bildirir. */
    public class Bridge {
        @JavascriptInterface
        public String version() { return "1.0.0"; }

        /** İstek arka planda yapılır; bitince window.__kokenNativeDone(id, durum, base64) çağrılır. */
        @JavascriptInterface
        public void get(final String id, final String url, final String headersJson, final int timeoutMs) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    int status = 0;
                    byte[] body;
                    try {
                        URL u = new URL(url);
                        if (!"https".equals(u.getProtocol())) throw new Exception("yalnızca https"); // düz http yok
                        HttpURLConnection c = (HttpURLConnection) u.openConnection();
                        int t = timeoutMs > 0 ? Math.min(timeoutMs, 30000) : 12000;
                        c.setConnectTimeout(t);
                        c.setReadTimeout(t);
                        c.setInstanceFollowRedirects(true);
                        c.setRequestProperty("User-Agent", UA);
                        c.setRequestProperty("Accept", "*/*");
                        c.setRequestProperty("Accept-Language", "tr-TR,tr;q=0.9,en;q=0.8");
                        // JS'in verdiği ek başlıkları uygula (User-Agent'ı da ezebilir)
                        if (headersJson != null && headersJson.length() > 2) {
                            JSONObject h = new JSONObject(headersJson);
                            Iterator<String> it = h.keys();
                            while (it.hasNext()) { String k = it.next(); c.setRequestProperty(k, h.getString(k)); }
                        }
                        status = c.getResponseCode();
                        InputStream in = status >= 400 ? c.getErrorStream() : c.getInputStream();
                        body = in == null ? new byte[0] : readLimited(in);
                        c.disconnect();
                    } catch (Exception e) {
                        status = 0; // ağ hatası: 0 + hata metni
                        body = String.valueOf(e).getBytes();
                    }
                    final String js = "window.__kokenNativeDone(" + JSONObject.quote(id) + "," + status + ",'"
                            + Base64.encodeToString(body, Base64.NO_WRAP) + "')";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { if (web != null) web.evaluateJavascript(js, null); }
                    });
                }
            });
        }
    }

    /** Akışı en fazla MAX_BYTES kadar okur. */
    private static byte[] readLimited(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) > 0) {
            out.write(buf, 0, n);
            if (out.size() > MAX_BYTES) break;
        }
        in.close();
        return out.toByteArray();
    }
}
