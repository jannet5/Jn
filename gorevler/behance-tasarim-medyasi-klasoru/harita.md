# Harita — Behance ilk proje medya klasörü

Hedef → bağımlılıklar → A/B/C yolları → uygulama → doğrulama → kalıcı teslim. Her düğüm bir sonrakini besler;
kırılan yol işaretli ve hangi alternatife geçildiği yazılı.

```
[H] Hedef: "mobile app" aramasındaki ilk gerçek (sponsorsuz) projenin tüm görsel/GIF/video
     varlıklarını tek klasörde, kaynak + hak bilgisi + eksik listesiyle topla
  │
  ├─[B1] Arama sayfası erişimi ── düz HTTP: 403 + JS challenge ──> gerçek tarayıcı gerekir
  ├─[B2] Proje verisi (modül listesi) ── sayfa içi `beconfig-store_state` JSON
  ├─[B3] Görsel CDN (mir-s3-cdn-cf.behance.net) ── doğrudan erişilebilir (200)
  ├─[B4] Gömülü video (player.vimeo.com) ── Cloudflare doğrulaması: 401
  └─[B5] Hak bilgisi ── proje `license` alanı + Behance telif rehberi
        │
        ▼
 A) gallery-dl (hazır, Behance çıkarıcısı var)          → KIRILDI: 403 (bot koruması, çerez taşınsa da)
 B) Playwright+Chromium ile sayfa durumu JSON + kendi    → SEÇİLDİ: arama + proje açıldı, 20 modül okundu
    indiricimiz (gallery-dl'nin boyut tercih sırası)
 C) Tarayıcı eklentisi / userscript (Behance Image       → yedek; bulutta kullanıcı etkileşimi gerektirir,
    Downloader, MBBR userscript)                            Windows'ta kullanıcı için seçenek olarak not edildi
 Video alt yolu:
   V1) yt-dlp                     → 401
   V2) yt-dlp --impersonate chrome → 401
   V3) Chromium içinde iframe     → "We couldn't verify the security of your connection" (Cloudflare)
   V4) Vimeo resmi oEmbed API     → başlık/süre/kapak karesi alındı; video dosyası EKSİK olarak işaretlendi
       + kullanıcı makinesi için hazır `videolari-indir.ps1`
   (Bot korumasını daha ileri atlatmaya çalışılmadı — bilinçli sınır.)
        │
        ▼
[U] Uygulama: arac/arama.mjs → arac/proje.mjs → arac/vimeo.mjs → arac/indir.py
        │
        ▼
[D] Doğrulama: `file` ile gerçek tür/çözünürlük, GIF kare sayıları (ffprobe), numaralı temas sayfasıyla
    gözle görünüm kontrolü, manifestte SHA-256, ZIP geri açma + karşılaştırma
        │
        ▼
[T] Teslim: özel ZIP (medya + manifest + BENI_OKU + önizleme) + SHA-256 dosyası.
    Public depoya yalnız araçlar, harita ve günlük gider — medya (lisans `no-use`) ve özel bağlam gitmez.
```

## Neden B?
- A, Behance'in JS-challenge/Varnish korumasına takıldı; gallery-dl'nin `requests` istemcisi tarayıcı
  doğrulamasını geçemiyor.
- B, sitenin kendi sunduğu proje verisini okur (modül türleri, `source` URL'leri, sahipler, lisans) —
  DOM kazımaktan daha eksiksiz: tembel yüklenen modüller ve medya koleksiyonlarının alt öğeleri dahil.
- Boyut seçimi gallery-dl çıkarıcısıyla aynı sıradadır (`source` → `max_3840` → `fs` → …), yani A'nın
  sonucuyla eşdeğer kalite hedeflenir.

## Referanslar (okundu)
- gallery-dl Behance çıkarıcısı: https://github.com/mikf/gallery-dl/blob/master/gallery_dl/extractor/behance.py
- gallery-dl PyPI: https://pypi.org/project/gallery-dl/
- yt-dlp ve taklit (impersonation) notu: https://github.com/yt-dlp/yt-dlp#impersonation
- Vimeo oEmbed: https://developer.vimeo.com/api/oembed/videos
- Behance telif rehberi: https://help.behance.net/hc/en-us/articles/204485044-Guide-Copyright-And-Posting-Content-On-Behance
- Behance ek şartları: https://wwwimages2.adobe.com/content/dam/cc/en/legal/servicetou/Behance-Additional-Terms-en_US-20221001.pdf
- Topluluk araçları (C yolu): https://openuserjs.org/scripts/MBBR/Behance_Project_Downloader_(High_Resolution) ,
  https://chromewebstore.google.com/detail/ghhedamclfpgcnpfclmefepnacnkngio
- Playwright Chromium: https://playwright.dev/docs/browsers
