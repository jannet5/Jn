package app.koken;

import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

/** JS köprüsü: web arayüzü CORS'a takılmadan https isteği atabilsin diye ağı Java tarafında yapar. */
public class KokenNative {
    // Sürüm bilgisi (JS tarafı köprünün varlığını ve sürümünü buradan öğrenir)
    static final String VERSION = "1.0.0";
    // Tarayıcı gibi görünmek için Chrome Mobile User-Agent
    static final String UA = "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36";
    // Tek yanıt için 2 MB üst sınır
    static final int MAX_BYTES = 2 * 1024 * 1024;
    // En fazla izlenecek yönlendirme sayısı
    static final int MAX_REDIRECTS = 6;

    private final MainActivity activity;
    private final WebView web;
    // Ağ istekleri arka plan iş parçacığı havuzunda yapılır (UI donmasın)
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    KokenNative(MainActivity activity, WebView web) {
        this.activity = activity;
        this.web = web;
    }

    /** Köprü sürümünü döndürür. */
    @JavascriptInterface
    public String version() { return VERSION; }

    /** İsteği havuza atar; bitince window.__kokenNativeDone(id, durum, base64) UI iş parçacığında çağrılır. */
    @JavascriptInterface
    public void get(final String id, final String url, final String headersJson, final int timeoutMs) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                int status;
                byte[] body;
                try {
                    // Asıl ağ çağrısı: durum kodu + gövde
                    Object[] r = fetch(url, headersJson, timeoutMs);
                    status = (Integer) r[0];
                    body = (byte[]) r[1];
                } catch (Throwable e) {
                    // Hata: durum 0, gövde hata metni
                    status = 0;
                    body = String.valueOf(e).getBytes(java.nio.charset.StandardCharsets.UTF_8);
                }
                // Gövde UTF-8 baytlarıdır; base64 tırnak içermediği için JS'e güvenle gömülür
                final String js = "window.__kokenNativeDone(" + JSONObject.quote(id) + "," + status + ",'"
                        + Base64.encodeToString(body, Base64.NO_WRAP) + "')";
                // WebView'a yalnızca UI iş parçacığından dokunulur
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!activity.isDestroyed()) web.evaluateJavascript(js, null);
                    }
                });
            }
        });
    }

    /** Havuzu kapatır (Activity yok olurken). */
    void shutdown() { pool.shutdownNow(); }

    /** Yönlendirmeleri elle izleyerek (yalnız https) GET yapar; {durum, bayt[]} döndürür. */
    static Object[] fetch(String url, String headersJson, int timeoutMs) throws Exception {
        // Zaman aşımı: verilmezse 12 sn, en çok 30 sn
        int t = timeoutMs > 0 ? Math.min(timeoutMs, 30000) : 12000;
        String cur = url;
        for (int hop = 0; hop <= MAX_REDIRECTS; hop++) {
            URL u = new URL(cur);
            // Düz http (ya da başka şema) kesinlikle yok
            if (!"https".equals(u.getProtocol())) throw new Exception("yalnızca https desteklenir: " + u.getProtocol());
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            try {
                c.setConnectTimeout(t);
                c.setReadTimeout(t);
                // Yönlendirmeyi kendimiz izliyoruz ki her adımda https denetlensin
                c.setInstanceFollowRedirects(false);
                c.setRequestProperty("User-Agent", UA);
                c.setRequestProperty("Accept", "*/*");
                c.setRequestProperty("Accept-Language", "tr-TR,tr;q=0.9,en;q=0.8");
                // JS'in verdiği ek başlıkları uygula (User-Agent'ı ezebilir; Accept-Encoding'i biz yönetiriz)
                if (headersJson != null && headersJson.length() > 2) {
                    JSONObject h = new JSONObject(headersJson);
                    Iterator<String> it = h.keys();
                    while (it.hasNext()) {
                        String k = it.next();
                        if ("accept-encoding".equalsIgnoreCase(k) || "host".equalsIgnoreCase(k)) continue;
                        c.setRequestProperty(k, String.valueOf(h.get(k)));
                    }
                }
                // gzip'i açıkça isteyince çözmeyi de biz yaparız
                c.setRequestProperty("Accept-Encoding", "gzip");
                int status = c.getResponseCode();
                // 3xx ise Location'a git
                if (status >= 300 && status < 400 && status != 304) {
                    String loc = c.getHeaderField("Location");
                    if (loc != null && hop < MAX_REDIRECTS) {
                        cur = new URL(u, loc).toString(); // göreli Location'ı da çöz
                        continue;
                    }
                }
                // Hata durumlarında gövde errorStream'dedir
                InputStream in = status >= 400 ? c.getErrorStream() : c.getInputStream();
                byte[] body = new byte[0];
                if (in != null) {
                    // gzip ile geldiyse aç
                    String enc = c.getContentEncoding();
                    if (enc != null && enc.toLowerCase().contains("gzip")) in = new GZIPInputStream(in);
                    body = readLimited(in);
                }
                return new Object[]{status, body};
            } finally {
                c.disconnect();
            }
        }
        throw new Exception("çok fazla yönlendirme");
    }

    /** Akışı tam MAX_BYTES'a kadar okur, fazlasını atar. */
    static byte[] readLimited(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        try {
            while (out.size() < MAX_BYTES) {
                // Kalan hakkı aşmayacak kadar oku
                int n = in.read(buf, 0, Math.min(buf.length, MAX_BYTES - out.size()));
                if (n < 0) break;
                out.write(buf, 0, n);
            }
        } finally {
            in.close();
        }
        return out.toByteArray();
    }
}
