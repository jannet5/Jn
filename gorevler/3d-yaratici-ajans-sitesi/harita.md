# Harita — 3D yaratıcı ajans sitesi (Sarmal Stüdyo)

Bu harita hedeften kalıcı teslime kadar her adımı birbirine bağlar. Her düğüm, bir öncekinin çıktısını girdi olarak kullanır; bir yol kırılırsa “Kırılma → yedek” sütunundaki alternatife geçilir.

```
[H] Hedef
 │  Fare + kaydırmaya tepki veren 3D spiral görsel galerili, özgün ajans sitesi;
 │  son kullanıcı düzeltmeleri: büyük başlık küçültülür, yazılar kaydırmayla yavaş gelir,
 │  galeri bozulmaz, hover ve akıcı geçişler korunur.
 ▼
[B] Bağımlılıklar
 │  B1 WebGL 3D motoru (three.js)      B2 yumuşak kaydırma + hız (Lenis)
 │  B3 görsel içerik (lisanslı/özgün)  B4 derleme (esbuild)  B5 gerçek tarayıcı (Chromium)
 ▼
[Y] Yol seçimi  ── A (seçildi) / B / C  ── aşağıdaki tablo
 ▼
[U] Uygulama  → site/src/{config,artwork,gallery,reveal,main}.js + styles.css + index.html
 ▼
[T] Test/Build → npm run build (dist/) → npm test (19 kontrol; Chromium, http + file://, mobil, reduced-motion)
 ▼
[K] Kalıcı teslim → görev dalına push + geri okuma; özel ZIP (kaynak metni yok) + SHA-256 + geri okuma
```

## A/B/C yolları

| | A — Seçilen | B | C |
|---|---|---|---|
| Yaklaşım | Vanilla JS modülleri + three.js + Lenis, **esbuild ile tek IIFE** dosyası; görseller **prosedürel canvas** | Referans repo gibi Vite + three + Lenis + GSAP ScrollTrigger, JPG görseller | WebGL’siz CSS 3D (`transform-style: preserve-3d`) spiral |
| Artı | `dist/index.html` çift tıklamayla (file://) açılır; sunucu gerektirmez; lisans belirsiz görsel yok; GSAP gerekmez (IntersectionObserver + CSS) | Ekosistemde en yaygın kurulum, hızlı geliştirme sunucusu | Bağımlılık yok, en hafif |
| Eksi | Gerçek fotoğraflar yerine üretilmiş görseller (gerçek portföy görselleri sonra eklenmeli) | ES modül + `fetch`’li JPG’ler file:// altında CORS’a takılır (three.js forum); referans repoda lisans dosyası yok → görselleri/kodu kopyalayamayız | Kavisli karo, derinlik sisi, 48 karoda akıcılık ve raycast hover zor; kaynakta “3GS/three.js” ürün tercihi var |
| Kırılma → yedek | WebGL başlatılamazsa sayfa `no-webgl` sınıfıyla statik degrade arka plana düşer, içerik çalışır. Swiftshader’da kare hızı düşük → hareket kare hızından bağımsız (dt) yapıldı | A’nın derlemesi bozulursa Vite’a geçilebilir (src aynı kalır) | A ve B olmazsa son çare |

**Neden A:** Kaynak, galerinin three.js ile yapılmasını ve fare/kaydırma hızına bağlı dönmesini istiyor; bu ürün tercihi korunur. Teslim “indirilebilir ve kalıcı” olmalı; tek dosyalık IIFE derleme + prosedürel doku, ZIP’i açan herkesin sunucu kurmadan siteyi görmesini sağlar. GSAP yerine yerel IntersectionObserver + CSS geçişi aynı “yavaş gelen yazı” etkisini bağımlılıksız verir.

## Kaynak düzeltmeleri → uygulama → kanıt bağlantısı

| Kaynak isteği | Uygulama | Kabul kontrolü |
|---|---|---|
| Fare hareketiyle galeri döner | `gallery.js` `mouseRotate` + eğim; dt’ye bağlı yumuşatma | K1a (rotasyon işareti/genliği), K1b (ekran görüntüsünde piksel farkı) |
| Ne kadar hızlı kaydırırsam o kadar hızlı döner | Lenis `velocity` → `addScrollVelocity` → `spinVelocity` (sönümlü, tavanlı) | K2 (boşta < yavaş < hızlı), K2b (kamera spiral boyunca iner) |
| Büyük başlık küçültülsün | `--hero-title: clamp(30px, 4.4vw, 68px)` (ilk sürüm `clamp(54px, 9.6vw, 160px)`) | K3 (1440 px’de 63 px ≈ ilk sürümün %46’sı) |
| Yazılar kaydırmayla yavaşça gelsin | `reveal.js`: kelime maskeli yükselme + blok fade-up, 1.3–1.6 sn | K5, K5b |
| Galeri bozulmasın | Metin katmanı `pointer-events: none`; galeri ayrı modül; başa dönüşte tekrar doğrulama | K7 |
| Hover efektleri / görsel “yavaşça geriye gider” | 3D karo hover’da eksene doğru çekilir + etiket; büyük görseller 1.12 → 1.00 ölçek | K4, K6, K6b |
| Erişilebilirlik / mobil | `prefers-reduced-motion`, mobil düzen, file:// açılış | K9, K10, K11 |

## Referanslar (kontrol edildi, 2026-10-03)

- Kaynak video: https://www.youtube.com/watch?v=JfmAm3sxCSc
- Videodaki sonuç sitesi: https://stately-naiad-8f0d7f.netlify.app/ (bölüm yapısı incelendi, içerik kopyalanmadı)
- Videodaki repo: https://github.com/YildizDikme/3D-threejs-spiral-gallery (Three.js + Lenis + GSAP + Vite; **LICENSE dosyası yok** → kod/görsel kopyalanmadı; yalnızca ilk sürüm başlık boyutu `clamp(54px, 9.6vw, 160px)` karşılaştırma ölçütü olarak okundu)
- İlham sitesi: https://studiodialect.com/ (minimal ızgara, imleç X/Y göstergesi — atmosfer olarak alındı)
- Studio Dialect, Awwwards: https://www.awwwards.com/inspiration/dialect-labs-studio-dialect
- three.js renk yönetimi: https://threejs.org/docs/#manual/en/introduction/Color-management
- three.js forumu, file:// ve yerel doku CORS: https://discourse.threejs.org/t/using-local-images-for-texture/1414
- three.js forumu, mobil pixel ratio performansı: https://discourse.threejs.org/t/low-fps-on-ios-mobile-with-pixel-ration-set-as-window-devicepixelratio/4963
- three.js forumu, doku performansı: https://discourse.threejs.org/t/texture-performance/24297
- Codrops, verimli three.js sahneleri: https://tympanus.net/codrops/?p=86572
- Lenis: https://github.com/darkroomengineering/lenis
- Benzer topluluk örneği (kaydırma hızına bağlı 3D galeri): https://freefrontend.com/code/smooth-3d-scroll-driven-reveal-2026-02-26/
- Framer “media spiral gallery” bileşeni (hazır ürün karşılaştırması): https://www.framer.com/marketplace/components/media-spiral-gallery/
- Reduced-motion + IntersectionObserver deseni: https://bquery.js.org/cookbook/scroll-reveal
