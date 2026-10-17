import org.schabi.newpipe.extractor.*;
import org.schabi.newpipe.extractor.downloader.*;
import org.schabi.newpipe.extractor.exceptions.*;
import org.schabi.newpipe.extractor.localization.*;
import org.schabi.newpipe.extractor.search.*;
import org.schabi.newpipe.extractor.stream.*;
import java.io.*;
import java.net.*;
import java.util.*;

/** Komut satiri: NewPipeExtractor ile arama + en iyi ses akisini indirme. Kullanim: Indir "sorgu" cikti_dosyasi_oneki */
public class Indir {
    static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0";

    static class Dl extends Downloader {
        public Response execute(Request r) throws IOException, ReCaptchaException {
            HttpURLConnection c = (HttpURLConnection) new URL(r.url()).openConnection();
            c.setConnectTimeout(20000); c.setReadTimeout(30000);
            c.setRequestMethod(r.httpMethod());
            c.setRequestProperty("User-Agent", UA);
            for (Map.Entry<String, List<String>> e : r.headers().entrySet()) {
                c.setRequestProperty(e.getKey(), e.getValue().get(0));
                for (int i = 1; i < e.getValue().size(); i++) c.addRequestProperty(e.getKey(), e.getValue().get(i));
            }
            byte[] data = r.dataToSend();
            if (data != null) { c.setDoOutput(true); try (OutputStream o = c.getOutputStream()) { o.write(data); } }
            int code = c.getResponseCode();
            if (code == 429) throw new ReCaptchaException("429", r.url());
            InputStream in = code >= 400 ? c.getErrorStream() : c.getInputStream();
            String body = in == null ? "" : new String(in.readAllBytes(), "UTF-8");
            Map<String, List<String>> h = new HashMap<>();
            for (Map.Entry<String, List<String>> e : c.getHeaderFields().entrySet()) if (e.getKey() != null) h.put(e.getKey(), e.getValue());
            return new Response(code, c.getResponseMessage(), h, body, c.getURL().toString());
        }
    }

    public static void main(String[] a) throws Exception {
        String query = a[0]; String out = a[1];
        NewPipe.init(new Dl(), new Localization("tr", "TR"), new ContentCountry("TR"));
        SearchExtractor se = ServiceList.YouTube.getSearchExtractor(query);
        se.fetchPage();
        String chosen = null;
        for (InfoItem it : se.getInitialPage().getItems()) {
            if (!(it instanceof StreamInfoItem)) continue;
            StreamInfoItem s = (StreamInfoItem) it;
            System.err.println("ADAY: " + s.getName() + " | " + s.getUploaderName() + " | " + s.getDuration() + " sn | " + s.getUrl());
            if (chosen == null && s.getDuration() >= 120 && s.getDuration() <= 600) chosen = s.getUrl();
        }
        if (chosen == null) throw new RuntimeException("uygun sonuc yok");
        StreamInfo info = StreamInfo.getInfo(ServiceList.YouTube, chosen);
        System.err.println("SECILEN: " + info.getName() + " | " + info.getUploaderName() + " | " + info.getDuration() + " sn");
        AudioStream best = null;
        for (AudioStream s : info.getAudioStreams()) {
            if (s.getDeliveryMethod() != DeliveryMethod.PROGRESSIVE_HTTP || !s.isUrl()) continue;
            if (best == null || (s.getFormat() == MediaFormat.M4A && best.getFormat() != MediaFormat.M4A) || (s.getFormat() == best.getFormat() && s.getAverageBitrate() > best.getAverageBitrate())) best = s;
        }
        if (best == null) throw new RuntimeException("ses akisi yok");
        System.err.println("URL_PARAMS: " + best.getContent().replaceAll("^[^?]*\\?","").replaceAll("(sig|lsig|n)=[^&]*","$1=..") + " | " + best.getFormat() + " " + best.getAverageBitrate());
        try (PrintWriter pw = new PrintWriter("/home/user/data/sarkilar/son_url.txt")) { pw.print(best.getContent()); }
        String ext = best.getFormat() == MediaFormat.M4A ? ".m4a" : ".webm";
        File f = new File(out + ext);
        long off = 0; final long CH = 2L * 1024 * 1024; boolean fin = false;
        try (OutputStream os = new FileOutputStream(f)) {
            while (!fin) {
                HttpURLConnection c = (HttpURLConnection) new URL(best.getContent()).openConnection();
                c.setRequestProperty("User-Agent", UA);
                c.setRequestProperty("Range", "bytes=" + off + "-" + (off + CH - 1));
                c.setConnectTimeout(20000); c.setReadTimeout(30000);
                int code = c.getResponseCode();
                if (code == 416) break;
                if (code >= 400) throw new IOException("HTTP " + code);
                long got = 0; try (InputStream in = c.getInputStream()) { byte[] buf = new byte[65536]; int n; while ((n = in.read(buf)) > 0) { os.write(buf, 0, n); got += n; } }
                off += got;
                if (code == 200 || got < CH) fin = true;
            }
        }
        System.out.println("TAMAM " + f.getAbsolutePath() + " " + off + " bayt | " + info.getName() + " | " + info.getUploaderName());
    }
}
