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

---
# v2 — fiyatlı sürüm (2026-10-10)
Kaynak: `ajans/KARARLAR.md` + `arastirma/rakipler/OZET.md` §5-7. Fiyat/teslim/sahiplik/lansman **tek yerden**: `site/src/site.config.ts` (`teklif`, `paketler`, `tl()`).

| Değişiklik | Durum |
|---|---|
| Hero: "İşletmenizin dijital vitrini, 7 günde yayında." + "Aylık 2.990 ₺'den. Büyük peşinat yok." + "İlk 5 işletmeye kurulum ~~4.990 ₺~~ 0 ₺" | ✅ |
| Hero altı sektör çipleri (Kafe-Restoran / Berber-Kuaför / Klinik / Butik) → `#ornek-*` | ✅ (Butik konsepti yeni eklendi) |
| Karşılaştırma bölümü: masaüstü tablo, mobil kart (bizim kart önde) | ✅ |
| Paketler: 3 kademe gerçek fiyat + kurulum, her kartta Dahil / Hariç / Sizden istenen; altta "Fiyatlar KDV hariç · 12 ay sonra site ve uygulama sizin · reklam bütçesi hariç" | ✅ |
| SSS: "12 ay sonra ne olur?", "İptal edebilir miyim?", "Reklam bütçesi kimin?" (+ "7 günde nasıl", eski "Fiyatlar neden yok" kaldırıldı) | ✅ |
| Nav: + "Karşılaştır", "Paketler" → "Fiyatlar" | ✅ |

**Bilinçli sapmalar (ana beyin onaylasın):**
- "En popüler" yerine **"En çok önerdiğimiz"**: henüz müşteri yok, "popüler" doğrulanamaz iddia (sahte sayı/yorum yok kuralı).
- "Peşinatsız" yerine **"Büyük peşinat yok"**: kurulum ücreti (4.990 ₺'den) var; ilk 5 işletmede 0 ₺.
- **İptal koşulu KARARLAR'da yok** → SSS'de "uzun taahhüt yok, bıraktığınız ay hizmet durur; kod 12. ayda devredilir" yazıldı. Karar değişirse yalnız SSS metni.
- Klasik ajans bandı "4-16 hafta, 25-75 bin ₺ peşin" `01-teklif-ve-paketler.md` çapa cümlesinden; Wix/Canva için sayı yazılmadı.

## v2 KAPI-2 (375 / 768 / 1440 + koyu 375 / 768 / 1440, dilimlere bakıldı)
| Tur | Bulgu | Düzeltme |
|---|---|---|
| v2 (`ss/v2-*`) | Hero'da "7 günde yayında" H1 ile fiyat satırında tekrar · Butik konseptinde 3 ürüne aynı tişört ikonu (etek/hırka yanlış, büyük boş kutu hissi) · Mobil karşılaştırmada bizim kart en sonda (6 satır × 3 kart kaydırması) · Paketlerde "sizden istenen" 3 kez aynı | Fiyat satırı "Aylık 2.990 ₺'den. Büyük peşinat yok." · Butik: ürün listesi + renk seçenekleri · Bizim kart `order-first` · Üst paketlerde "Vitrin'de istenenler +" |
| son2 (`ss/son2-*`) | 1440'ta kafe kartının alt yazısı butik kartına biniyordu (row-span-2 + h-full) | figure `flex-col`, kart `flex-1` → temiz |
Puan: hiyerarşi 4 · tutarlılık 4 · boşluk 3 (değer defteri bölümünde masaüstü sol sütun boşluğu, v1'den) · tipografi 4 · durumlar 4 · erişilebilirlik 4 · konsol 0 hata → **≥3/4 ✅**. Taşma 0 px (6 görünüm).

## v2 KAPI-3
`node site/scripts/kullanici-testi.mjs` → **20/20 GEÇTİ** (v1'in 12'si + 8 yeni: 4 sektör çipi doğru örneğe kayıyor, 6 fiyat + lansman + KDV + sahiplik notu görünür, mobilde kart/masaüstünde tablo, 3 yeni SSS).

| # | Kontrol | v2 sonucu |
|---|---|---|
| 1 | Her değer DESIGN.md'den | ✅ yeni bileşenler (çip, fiyat, tablo) DESIGN §6'ya eklendi; butik mini paleti §8'e |
| 2 | Kısıtlı palet, net hiyerarşi | ✅ primary yalnız CTA, vurgu kelime, "En çok önerdiğimiz", lansman noktası |
| 3 | Aynı tür buton/kart aynı | ✅ .btn/.card/.chip tek tanım |
| 4 | Düz/kalabalık dengesi | ✅ paket kartları uzun ama 3 alt başlıkla bölündü |
| 5 | Açık/koyu okunur | ✅ Lighthouse kontrast 100 |
| 6 | Yatay kaydırma yok | ✅ 0 px (375/768/1440 açık+koyu) |
| 7 | Mobil menü | ✅ (+ Karşılaştır, Fiyatlar) |
| 8 | Dokunma ≥44 | ✅ çipler 44 |
| 9 | Büyütülmüş metin | ✅ |
| 10 | Yükleniyor/boş/hata | N/A (statik) |
| 11 | Hover/basılı/odak | ✅ (çip hover eklendi) |
| 12 | Form durumları | N/A (form yok) |
| 13 | Geçişler animasyonlu | ✅ |
| 14 | Ana akış | ✅ hero → sektör çipi → örnek → karşılaştırma → fiyat → SSS → WhatsApp |
| 15 | Ölü buton/kırık link | ⚠️ Kısmi (değişmedi): WhatsApp/telefon numarası yer tutucu |
| 16 | Klavye | ✅ |
| 17 | Tek cümlede ne yaptığımız | ✅ |
| 18 | Tek ana buton | ✅ birincil buton yalnız check-up; paket altı ikincil |
| 19 | title/description/favicon | ✅ description'a fiyat+süre, JSON-LD'ye priceRange |
| 20 | Placeholder yok | ✅ |

## v2 Lighthouse (12.x, `astro preview`)
| | Perf | A11y | BP | SEO |
|---|---|---|---|---|
| Mobil | 100 (LCP 1.0 s, CLS 0.002) | 100 | 100 | 100 |
| Masaüstü | 100 (LCP 0.2 s) | 100 | 100 | 100 |

Son SS: `ss/son2-375.png`, `son2-768.png`, `son2-1440.png`, `son2-koyu-375.png`, `son2-koyu-1440.png`. Yayın dosyası: `yayin/vitrin-atolyesi.html` (güncellendi, yayınlanmadı).
