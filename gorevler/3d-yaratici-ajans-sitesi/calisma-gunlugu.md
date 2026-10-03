# Çalışma günlüğü — 3D yaratıcı ajans sitesi

Tarih: 2026-10-03 · Ortam: Claude Code Cloud (Linux konteyner), depo `jannet5/Jn`, dal `claude/sweet-mendel-7qrogq`, çalışma klasörü `gorevler/3d-yaratici-ajans-sitesi/`.

## 1. Ne istendi

- Videodaki fikirden ilhamla, kopya olmayan, **fare ve kaydırmaya tepki veren 3D spiral görsel galerili** bir yaratıcı ajans sitesi.
- Son kullanıcı düzeltmeleri: **büyük başlık küçültülsün**, sayfadaki **yazılar kaydırmayla yavaşça gelsin**, bunlar yapılırken **galeri bozulmasın**; hover ve akıcı geçişler korunsun.
- Süreç: web/topluluk araştırması → bağlı harita + A/B/C → uygulama → gerçek kabul testi → indirilebilir kalıcı teslim.
- Sınırlar: mevcut projeler değiştirilmeyecek; özel kaynak metni (görev metni ve video dökümü) public depoya konmayacak, yalnızca bulut çalışma alanında (`scratchpad/ozel/`) tutuldu.

## 2. Başlangıç durumu ve engel

- Kaynakta adı geçen yerel `youtube-project` klasörü (Windows yolu) bulut ortamında yok; kod, görsel ve “son görünüm” pakette gelmedi. Bu yüzden mevcut bir ürünü güncellemek yerine istekleri karşılayan **yeni ve özgün** bir site sıfırdan yazıldı. Kullanıcının kendi eski kodu gelirse, bu klasördeki galeri modülü oraya taşınabilir.

## 3. Araştırma (araç: WebSearch, WebFetch, git clone)

| Ne | Bulgu | Etkisi |
|---|---|---|
| Videodaki repo (`git clone --depth 1 https://github.com/YildizDikme/3D-threejs-spiral-gallery`) | Vite + three + Lenis + GSAP; `CONFIG` nesnesi; ilk başlık `clamp(54px, 9.6vw, 160px)`; **LICENSE yok** | Kod/görsel kopyalanmadı; başlık değeri yalnızca “küçültme” ölçütü olarak kullanıldı |
| Netlify’daki sonuç sitesi | 5 bölüm (manifesto, pratik, işler, yaklaşım, iletişim) | Bölüm akışı fikri alındı, metinler özgün yazıldı |
| studiodialect.com | Minimal ızgara, imleç X/Y koordinat göstergesi | Atmosfer + koordinat göstergesi özgün biçimde eklendi |
| three.js forumu (file:// CORS) | Yerel resimler file:// altında WebGL dokusu olamaz | Görseller canvas ile **prosedürel** üretildi → çift tıklamayla açılır |
| three.js forumu (mobil DPR) | `devicePixelRatio` sınırsızken FPS düşer | `maxPixelRatio: 1.75` |
| Lenis / reduced-motion desenleri | IO + CSS geçişi, reduced-motion’da her şey hemen görünür | GSAP gerekmedi; `reveal.js` |
| Hazır ürünler (Framer spiral bileşeni, WordPress Shader Spiral Carousel) | Platforma bağlı / ücretli | Bağımsız statik çözüm seçildi |

Tam bağlantılar: `harita.md` → “Referanslar”.

## 4. Kararlar

1. **Yol A** (vanilla + three.js + Lenis + esbuild IIFE + prosedürel doku). Gerekçe `harita.md`’de.
2. Sürümler `npm view` ile güncel alındı ve sabitlendi: three 0.186.1, lenis 1.3.26, esbuild 0.28.2, playwright-core 1.63.0.
3. Marka: kurgusal “Sarmal Stüdyo” (sarmal = spiral). Gerçek kişi/kurum taklidi yok; e-posta `.example` ayrılmış alan adı.
4. Başlık: `clamp(30px, 4.4vw, 68px)` — ilk sürümün yaklaşık %46’sı.
5. Galeri hover’ı videodaki “görsel yavaşça geriye gidiyor” etkisinin 3D karşılığı: karo eksene doğru çekilir, hafif aydınlanır, imleçte eser adı çıkar. Büyük görsellerde 1.12 → 1.00 ölçek.

## 5. Uygulama (dosyalar)

- `site/src/config.js` — tüm galeri ayarları
- `site/src/artwork.js` — 12 eserin prosedürel çizimi (6 kompozisyon, tohumlu rastgelelik)
- `site/src/gallery.js` — kavisli karo geometrisi, özel GLSL (arka yüz karartma, kenar vinyeti, derinlik sisi, hover), fare/kaydırma/kamera/raycast
- `site/src/reveal.js` — kelime maskeli + blok yavaş girişler
- `site/src/main.js` — Lenis, kaydırma hızı köprüsü, imleç etiketi, X/Y göstergesi, WebGL yedeği
- `site/src/styles.css`, `site/index.html`
- `site/scripts/build.mjs` (esbuild), `site/scripts/serve.mjs` (bağımlılıksız statik sunucu)
- `site/tests/acceptance.mjs` — Chromium kabul testi

## 6. Sorunlar ve çözümler

| Sorun | Çözüm |
|---|---|
| İlk test: canvas pikselleri 0 okundu (WebGL çizim tamponu kompozitten sonra temizleniyor) | Piksel kontrolü gerçek ekran görüntüsü (PNG) üzerinden yapıldı |
| İlk test: fare dönüşü hedefe ulaşmadı (SwiftShader ≈12 FPS; yumuşatma kare başınaydı) | Tüm yumuşatma/sönüm **kare hızından bağımsız** (dt) yapıldı — gerçek kullanıcıda da 30/60/120 Hz ekranlarda aynı his |
| İlk test: manifesto daha önceki kaydırma testinde açılmıştı | “Yavaş giriş” testi temiz sayfada ayrı çalıştırıldı |
| Lenis lisans yorumu derlemeye girmiyor | Build, three ve Lenis LICENSE dosyalarını `dist/assets/` içine kopyalıyor |

## 7. Doğrulama

Komutlar:

```bash
cd gorevler/3d-yaratici-ajans-sitesi/site
npm install
npm run build
npm test
```

Son çalıştırma: **19/19 geçti** (`kanit/sonuc.json`). Ana değerler: fare ile rot −0.49 ↔ +0.49 rad, piksel değişimi %44; dönüş boşta 0.05 / yavaş 10.2 / hızlı 28.8; başlık 63 px (ilk sürüm 138 px); yavaş giriş 0 → 1 (~1.3 sn), 27/27; mobil 390 px taşma 0; reduced-motion ve file:// açılışı hatasız.

Görsel kontrol: `kanit/*.png` ekran görüntüleri tek tek açılıp incelendi (başlık okunur, spiral kadrajda, hover etiketi görünür, mobil düzen düzgün).

**Yapılmayanlar:** gerçek Windows tarayıcısı, gerçek telefon, Safari/Firefox, gerçek GPU FPS ölçümü, canlı yayın (hesap yok).

## 8. Kalıcı teslim

- Görev dalına commit + push; ardından uzak daldan geri okuma (aşağıdaki SHA’lar sohbet yanıtında raporlanır).
- Özel ZIP: `scratchpad/teslim/sarmal-studyo-teslim.zip` — yalnızca ürün dosyaları, harita, günlük, kanıtlar; kaynak metni ve görev dosyası **içermez**. SHA-256 hesaplandı, ZIP açılıp yeniden derlenerek/test edilerek geri okundu.

## 9. Gerekli gerçek girdiler

Gerçek portföy görselleri (lisanslı), gerçek stüdyo adı/metinleri/iletişim adresi, yayın için barındırma erişimi. Bunlar gelmeden uydurulmadı.
