# DURUM — ajansimiz
Hat: web · Site: `site/` (Astro 7 + Tailwind 4) · Çıktı: `site/dist/index.html` tek dosya (73 KB, harici yalnızca Google Fonts)

| Adım | Durum | Not |
|---|---|---|
| Intake | ✅ | BRIEF.md |
| KAPI-1 DESIGN.md | ✅ | Yön "Atölye Mürekkebi" seçili (DESIGN.md §1) |
| Üretim | ✅ | 11 bölüm, config tek yerden (`src/site.config.ts`) |
| KAPI-2 Görsel QA | ✅ 3 tur | Aşağıda |
| Cila 4 geçiş | ✅ | tipografi → boşluk → hareket → mobil (ayrı commit'ler) |
| KAPI-3 20 kontrol | ✅ (1 kısmi, 3 N/A) | Aşağıda |
| KAPI-4 Teslim | ⬜ | İnsan: gerçek telefonda aç, numara/e-posta gir |

## KAPI-2 — görsel QA (375 / 768 / 1440 + koyu 375 / 1440)
| Tur | Bulgu | Düzeltme |
|---|---|---|
| v1 | Mobil nav'da marka 2 satıra kırılıyor · Değer defteri grafiği SS'te boş · Kafe konsept kartında büyük ölü boşluk · Hero'da 2 kelime vurgulu (DESIGN: tek kelime) | Mobilde WhatsApp ikon-buton + nowrap · Kafe kartına menü listesi · Vurgu yalnız "günler" |
| v2 | Grafik hâlâ boş → sebep: `scroll-behavior: smooth` SS betiğinin kaydırmasını yavaşlatıyor (gerçek kullanıcıda sorun yok) · Koyu modda `ink` bloklar zeminden ayrışmıyor | SS betiği `behavior:'instant'` · koyu modda ink bloklara 1px `ink-border` |
| v3 | Temiz. Taşma 0 px, konsol hatası 0 (5 görünümde) | — |
Cila sonrası: telefon çerçevesindeki boş kahverengi kutu (stok-foto hissi) → menü listesi. Son SS: `ss/son-375.png`, `son-1440.png`, `son-koyu-375.png`.

## KAPI-3 — 20 kontrol
Otomatik kullanıcı testi: `node site/scripts/kullanici-testi.mjs` → **12/12 GEÇTİ** (mobil menü aç/kapa+kaydırma, SSS aç/kapa, kırık çapa yok, 6 WhatsApp linki config'den, görünür yer tutucu yok, dokunma ≥44px, klavye: ilk Tab "İçeriğe geç" + 12/12 odak halkası, reduced-motion, %200 metinde taşma yok).

| # | Kontrol | Sonuç |
|---|---|---|
| 1 | Her değer DESIGN.md'den | ✅ (cila 1-2'de 15px/18px/40px/20px gibi ölçek dışı değerler düzeltildi; konsept önizlemelerinin mini paletleri §8 izinli) |
| 2 | Kısıtlı palet, net hiyerarşi | ✅ |
| 3 | Aynı tür buton/kart aynı | ✅ (.btn / .card tek tanım) |
| 4 | Düz/kalabalık dengesi | ✅ |
| 5 | Açık/koyu zeminde okunur | ✅ Lighthouse kontrast 100 (1 hata bulundu, düzeltildi) |
| 6 | Yatay kaydırma yok | ✅ |
| 7 | Mobil menü var | ✅ details/summary, JS'siz çalışır |
| 8 | Dokunma ≥44 | ✅ |
| 9 | Büyütülmüş metinde düzen | ✅ (grid'lere `grid-cols-1` + e-postaya `break-all` eklendi) |
| 10 | Yükleniyor/boş/hata ekranı | N/A (statik, veri yok) |
| 11 | Buton hover/basılı/odak | ✅ (devre dışı durum: sayfada devre dışı buton yok) |
| 12 | Form durumları | N/A (form yok; tek aksiyon WhatsApp) |
| 13 | Geçişler animasyonlu | ✅ SSS, mobil menü |
| 14 | Ana akış baştan sona | ✅ |
| 15 | Ölü buton/kırık link yok | ⚠️ Kısmi: linkler doğru ama WhatsApp/telefon numarası yer tutucu → gerçek numara girilene kadar çalışmaz |
| 16 | Klavye | ✅ |
| 17 | Tek cümlede ne yaptığımız | ✅ H1 + alt cümle |
| 18 | Tek ana buton | ✅ (tüm birincil butonlar aynı: check-up) |
| 19 | title/description/favicon | ✅ (+ OG, JSON-LD) |
| 20 | Placeholder metin yok | ✅ görünürde yok; config'de `TODO: müşteriye sor` |

Yerel işletme maddeleri (fold üstü form, yorumlar, H1 hizmet+şehir, NAP): bu proje ajansın kendi sitesi, uzaktan hizmet → BRIEF gereği form yok (WhatsApp), yorum yok (henüz müşteri yok). Bilinçli N/A.

## Lighthouse (12.x, headless Chromium, `astro preview`)
| | Performans | Erişilebilirlik | En iyi uyg. | SEO |
|---|---|---|---|---|
| Mobil | 100 (LCP 0.9 s, CLS 0.003) | 100 | 100 | 100 |
| Masaüstü | 100 | 100 | 100 | 100 |
İlk ölçüm mobil 81 / a11y 96 → font CSS'i render bloklamayan yüklemeye alındı, 8px metin kontrastı düzeltildi.

## 10K puanlaması (dürüst)
| Alan | Puan | Neden |
|---|---|---|
| Yön | strong | Tek net yön, slop listesi temiz, mor/krem/Inter yok |
| Tipografi | strong | Archivo geniş 800 başlıklar karakterli; tek aile, katı ölçek |
| Renk | strong | Tek sinyal rengi disiplinli, açık+koyu tam |
| Hiyerarşi | strong | Sol hizalı, her bölüm tek mesaj |
| Görsel | mixed | Fotoğraf yok (bilinçli); CSS önizlemeler temiz ama "wow" anı yok; konsept kartları basit |
| Hareket | mixed | Bölüm başına farklı hareket var ama hepsi küçük; adım çizgisi tam sayfa SS'te boş görünüyor |
| Mobil tasarlanmış | strong | İkon nav, tek sütun ritmi, büyük metin testi geçti |
| Görünmez kalite | strong | Lighthouse 4×100, tek dosya 73 KB, JS ~1.5 KB |

## Müşteriye sorulacaklar (TODO)
WhatsApp numarası · telefon · e-posta alan adı (`vitrinatolyesi.com` sahiplenilmedi) · Instagram kullanıcı adı · kesin marka adı.
