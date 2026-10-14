# WEB SİTESİ + TASARIM — YouTube araştırması ÖZETİ

## 0. Gerçek kapsam (denetim, 2026-10-14)

İstenen: index'teki 3 bölüm + 5 ek arama = **98 benzersiz video** için transkript + açıklama + A/B planı, sonra sentez. ID bazlı tam liste: `KAPSAM.md`.

| grup | video | transkript | açıklama | plan |
|---|---|---|---|---|
| asıl 3 bölüm | 39 | 7 | 39 | 8 |
| 5 ek arama | 59 | 1 | 59 | 1 |
| **istenen toplam** | **98** | **8** | **98** | **9** |
| Türkçe web bölümü (bitişik, istenmemişti) | 15 | 13 | 13 | 13 |

- **İstenen kapsam tamamlanmadı.** 98 videonun 8'inde transkript var; 65 A/B videosunun 56'sında transkript ve plan yok. Sebep: YouTube bu bulut IP'sinde altyazı indirmeyi HTTP 429 ile kesti. Yeniden toplu istek atılmadı.
- Diğer oturumların sonradan eklediği transkriptler ID ile kontrol edildi: **hiçbiri bu 98'lik kuyruktan değil** (mobil, iş akışı, satış ve Türkçe gruplarına ait).
- Bu sebeple sentez, istenen gruptan 9 plan + Türkçe web bölümünden 13 plan (transkriptleri başka oturumlar çekti) + mobil klasördeki 7 tasarım planına çapraz referansla güncellendi. Toplam web planı: 22 (+ başka oturumun `kdev1F8o5y8` satış planı).
- QUI6Ug4cHnE'de altyazı hiç yok; planı skill reposundan yazıldı. 7 skill/şablon metni `ajans/arastirma/youtube/kaynaklar/` altında.
- Kalan iş: 90 videonun transkripti başka IP'den `python3 ajans/araclar/yt_transcript.py <id>` ile çekilip A (19) ve B (37) planları yazılmalı; C (33) isteğe bağlı.

## 1. Ortak kalıplar (ilk 9 planın kesişimi; Türkçe grup bulguları en altta)

1. **Düz prompt = AI slop.** Herkes aynı sonucu gösteriyor: mor/indigo gradient (Tailwind'in eski varsayılanı), 3 eşit kart, Inter, ortalanmış hero, fazla metin. Fark araçta değil kurulumda.
2. **Skill katmanı şart ama yetmez.** Anthropic frontend-design + UI UX Pro Max (ya da taste-skill / scroll-craft) ilk üretimi "tasarlanmış şablon" seviyesine çıkarıyor; premium his için sonraki katmanlar gerekiyor.
3. **Brief'in kalitesi = sitenin kalitesi.** 4 öğe (hedef, layout/bölüm listesi, içerik+ses, kitle) + "Ask me clarifying questions / ask me anything before you build". Claude'un sunduğu 3 stil yönünden seçim en kritik an.
4. **Göster, anlatma.** Referans SS'leri (Awwwards, godly.website, Dribbble, Pinterest, refero) → ama SS→kod kayıplı; **kaynak kodu teardown** (HTML+CSS+JS) ya da **hazır bileşen** (21st.dev "Copy Prompt") çok daha yakın sonuç veriyor.
5. **Gerçek görsel/hareketli varlık.** Nano Banana (2K, beyaz fon, referanslı tutarlılık) → Kling/Veo (start+end frame, 3-5 sn ya da 15 sn subtle loop) → hero video + inward mask gradient; scroll'a bağlı kare animasyonu (video → JPEG kareler + preload, ya da three.js scrub). Mobilde statik görsel.
6. **Önce büyük, sonra küçük.** Yapı/yön (chat) → eleman (inline comment/SS) → bölüm başına **tek** restrained mikro-etkileşim → "more subtle, more refined" törpüsü → Inter→Geist → mobil için ayrı tasarım turu → "daha hızlı yükle" x2.
7. **Kontrol listesine karşı dürüst puanlama.** 10K checklist (8 madde) ya da skill'lerin pre-flight listeleri → "Where does this land? Be honest" → niyet odaklı toplu düzeltme ("busier değil, more expensive").
8. **İçerik de tasarımdır.** Jenerik copy slop'tur: research + fill, kısa "chef yazmış gibi" cümleler, John Doe/Acme yasağı, em dash yasağı.
9. **Öğrenmeler biriksin.** CLAUDE.md'ye geri bildirim yazdırma (Jeff Su), fingerprint registry (scroll-craft) → her teslimat öncekinden iyi ve öncekine benzemiyor.
10. **Teslimat basit.** Tek dosya statik HTML (çoğu yerel işletme için yeterli) → Netlify/Vercel/Hostinger. Maliyet 40-60 $/ay; satış 3-10K $.

## 2. En iyi 5 video

| # | Video | Neden |
|---|---|---|
| 1 | **Metics Media — Build $10,000 Websites (VMvZuhcDdnw)** | Uçtan uca en eksiksiz akış: 2 skill + brief + checklist + polish protokolü + görsel zinciri + deploy. 8 maddelik 10K checklist PDF'i elimizde. |
| 2 | **Nate Herk — Scrollcraft skill (QUI6Ug4cHnE)** + repo | Ajans için en güçlü sistem: 8 soruluk röportaj, his eğrisi + tek zirve, 8 sayfa grameri, fingerprint kapısı (siteler birbirine benzemesin), kendi işini doğrulama. Transkript yok ama repo tamamen bizde. |
| 3 | **Nick Saraev — taste-skill + Kling (ZfYvv-0l9NA)** | En kısa yol: tek prompt + taste-skill + 5 sn video + Netlify; 3-5 $. taste-skill'in "AI tells" listesi anti-slop kurallarımızın çekirdeği. |
| 4 | **Chase AI — 7 seviye (1PXFAFMgdns)** | Neden/nasıl haritası: teardown ile öğrenme, görsel hikâye anlatımı, Stitch ile ideasyon, mikro dokunuş listesi. |
| 5 | **AI Chris Lee — 3D site + 10K satış (mhIAd5lVMag)** | Tek satış anlatan video: demo-önce outreach, DM/e-posta şablonları, fiyat çapası, segment ve sınırlar. |

Onur: Ed Hill ANF (bileşen montajı + normalize + fill), Jeff Su (DESIGN.md → design system → şablon → CLAUDE.md), Mikey (Claude Design 4 adım + alternatif hero), Griffin (Brand Guidelines skill'i müşteri başına).

## 3. Bizim web sitesi üretim zinciri (fabrika adımları)

**Kurulum (bir kez):** Claude Code + global skill'ler: frontend-design, UI UX Pro Max, taste-skill (`npx skills add https://github.com/Leonxlnx/taste-skill`), scroll-craft plugin (`/plugin marketplace add nateherkai/scroll-craft`); `doctor.mjs` kontrolü; sektör başına 21st.dev bileşen setleri (`components/`); sektör başına jenerikleştirilmiş DESIGN.md (awesome-design-md'den "tescilli içeriği temizle"); anti-slop kontrol listesi (bölüm 4); DM/e-posta şablonları.

1. **Keşif / satış**: Instagram + Google Maps tarama → eski siteli hizmet işletmeleri → 20 dk'lık kişiselleştirilmiş ana sayfa demosu (logo/renk/foto) → Loom yan yana → "ana sayfa bizden, yorum istiyoruz" → tam site teklifi (tek sayfa / çok sayfa / bakım; ajans fiyatına çapa; statik sınırları yaz).
2. **Brief**: scroll-craft'ın 8 sorusu + Mikey'nin 4 öğesi Türkçe onboarding formu → `BRIEF.md` (müşterinin kelimeleriyle) + voice principles (ses tonu, em dash yok, jargon yok) + eldeki varlıklar (logo, foto, menü).
3. **Marka skill'i**: Brand Guidelines şablonundan `skills/marka-<musteri>/SKILL.md` (hex, font, spacing, do/don't). Web/app/sosyal hattı aynı dosyayı okur.
4. **Yön seçimi**: `/ui-ux-pro-max` + brief + "Ask me clarifying questions" → Claude'un 3 stil yönü → müşteriye (Claude Design share link ile) seçtir. Kadranlar (taste-skill): restoran/premium 7/6/3, klinik/trust-first 3-4/2-3/4-5.
5. **Varlık üretimi**: Claude görsel prompt'larını yazar → Nano Banana (2K, beyaz fon, tek stil ön-metni, referanslı tutarlılık) → Kling/Veo (start+end frame 3-5 sn ya da 15 sn subtle loop) → sıkıştır (hero ≤300 KB), mobil statik varyant.
6. **Montaj (ANF)**: bileşenleri `components/` → "assemble in order" → **normalize** (tek font çifti, 4-5 hex, tek radius ölçeği, light/dark) → **research + fill** (sektör araştırması + müşteri bilgisiyle gerçek içerik).
7. **Hareket**: hero video + inward mask gradient; isteğe göre scroll-scrub (JPEG kareler + preload / three.js); bölüm başına tek mikro-etkileşim (sayaç, ışık süpürmesi, ticker, progress bar, glass ağırlık); `window.addEventListener("scroll")` yasak, reduced-motion zorunlu.
8. **Kalite kapısı**: 10K checklist + anti-slop listesi → "Be honest" puanlama → toplu düzeltme → SS ile geri besleme → "more subtle" → Inter→Geist → mobil ayrı tur → "daha hızlı yükle" x2 → headless SS/kontrast doğrulaması (scroll-craft `shoot.mjs`).
9. **Onay**: Claude Design/preview linki, müşteri yorumu → geri bildirimler proje `CLAUDE.md`'sine (birikim) → fingerprint kaydı (önceki sitelerden 4/6 boyutta farklı).
10. **Yayın + bakım**: tek dosya HTML → Netlify/Vercel (müşteri domaini) → meta/SEO/hız kontrolü → aylık bakım paketi.

## 4. Anti-slop tasarım kuralları (kontrol listesi)

**Yasaklar (varsayılan olarak)**
- Mor/indigo/"AI purple" gradient, neon outer glow, aşırı doygun vurgu, saf siyah #000, büyük başlıklarda gradient metin.
- Inter/Roboto/Arial varsayılanı; Fraunces ve Instrument Serif; başlıkta tek kelimeyi başka fontla vurgulama (aynı ailenin italik/bold'u olur).
- Krem (#f5f1ea…) + pirinç/kil/oxblood + espresso "artisan" paleti refleksi (premium-consumer brief'lerde rotasyon zorunlu).
- 3 eşit özellik kartı; beyaz üstü beyaz bento; 3. ardışık görsel+metin zigzag; her bölüm başına eyebrow (max 1 eyebrow / 3 bölüm); "left big headline + right small paragraph" split header.
- Hero'da 4'ten fazla metin öğesi; hero taşması; nav 2 satır; nav >80 px; hero top padding >6rem; CTA metni 2 satıra sarması; aynı niyette 2 farklı CTA etiketi.
- `01 / 06` sayaçları, "Scroll to explore", bölüm numarası eyebrow'ları, orta nokta ayırıcı zinciri, dekoratif renkli status noktaları, dikey döndürülmüş metin, dekoratif hairline grid, hero altı "BRAND. MOTION. SPATIAL." şeridi, lokasyon/saat/hava şeritleri, sürüm etiketleri, fotoğraf üstü pill/etiket, sahte foto kredisi, marketing sayfasında versiyon footer'ı.
- Div'den sahte ekran görüntüsü/dashboard/terminal; elle çizilmiş SVG ikon/illüstrasyon (Phosphor/Tabler/Radix kullan); kırık Unsplash linki; metin logosu "logo duvarı".
- "John Doe", "Acme/Nexus", 99,99 %, "Elevate/Seamless/Unleash"; "Quietly trusted by"; "Field notes" tarzı şairane etiketler; adım etiketleri "Step 1/2/3"; **em dash (—) ve en dash (–) tamamen yasak**.
- `window.addEventListener("scroll")`, scrollY ile React state, rAF içinde state; her bölümde fade-up; her kartta hover; özel mouse cursor; AOS-fade-up slop; tek kesintisiz kamera uçuşu (gerekmedikçe); low-poly clay diorama dünyası.
- Placeholder-as-label; form kontrastı düşük; buton metni/arka planı kontrast testi geçmeyen.

**Zorunlular**
- Tek satırlık "Design Read" + kadranlar (variance/motion/density) brief'ten çıkarılır; tek palet (4-5 hex, max 1 vurgu, doygunluk <%80), tek radius ölçeği, tek font çifti (2 aile max), 45-75ch ölçü, hero başlık ≤2 satır, alt metin ≤20 kelime.
- Gerçek görsel (üretilmiş ya da müşterinin) — minimalist sitede bile ≥2-3 görsel; hero'da gerçek görsel/hareket.
- Bölüm düzeni çeşitliliği: 8 bölümde ≥4 farklı layout ailesi; bir aile sayfada en fazla 1 kez; scroll sitelerinde ≥4 cihaz ailesi, aynı cihaz art arda yok, max 2 scrub.
- Tek zirve anı (en büyük varlık bütçesi ve scroll alanı), his eğrisi; tek imza etkileşimi.
- Loading/empty/error durumları, `:active` dokunsal geri bildirim, WCAG AA kontrast (butonlar, formlar, kompozit video üstü metin), klavye odağı, `prefers-reduced-motion`, dark mode tutarlılığı, LCP <2 s, hero medya ≤300 KB, semantik HTML + gerçek meta.
- Mobil ayrı tasarlanır (gizle/sıkılaştır/yeniden boyutlandır; videoda statik görsel), her çok sütunlu bölüm için <768 px kuralı yazılı.
- Copy: aktif fiil, kısa, somut; CTA eylemi söyler ("Rezervasyon yap"), aynı eylem aynı adla; Türkçe'de em dash yok.
- Teslim öncesi: 10K checklist 8 madde + bu listeyle "be honest" puanlama; headless SS doğrulaması; önceki sitelerle fingerprint farkı (4/6).

## 5. Kaynak dosyaları (aynen kaydedilen metinler)
- `kaynaklar/ZfYvv-0l9NA-taste-skill.md` — README + ana SKILL.md (87 KB) + redesign + stitch DESIGN.md
- `kaynaklar/QUI6Ug4cHnE-scroll-craft.md` — README + SKILL.md + taste/feel/uniqueness/verify/assets/hero-depth
- `kaynaklar/VMvZuhcDdnw-frontend-design-ve-ui-ux-pro-max.md` — Anthropic frontend-design SKILL.md + UI UX Pro Max README/skill-content/quick-reference
- `kaynaklar/VMvZuhcDdnw-10k-checklist.md` — Metics Media 10K checklist PDF metni
- `kaynaklar/VeWf0l4ci6Y-awesome-design-md.md` — README + Apple ve Nike DESIGN.md örnekleri (73 marka repoda)
- `kaynaklar/Iup1WlUyj9M-anthropic-tasarim-skilleri.md` — theme-factory, brand-guidelines, canvas-design SKILL.md
- Kapalı/erişilemeyen: Ed Hill normalize/fill prompt'ları (e-posta kapısı), Chase AI site-teardown skill'i (Skool), Jack Roberts design-loop skill'i (Skool), Figma implement-design (403).

## Ek (2026-10-10): yeni planlar
- `kdev1F8o5y8-ai-web-sitesi-yap-ve-sat-baslangic.md` — Max Max: pazar → Base44 ile restoran sitesi (Bella Noce) → yayın → fiyat/retainer/satış. Üretim adımları Claude Code'a çevrilerek kullanılır.
- (Süreç için ayrıca: `../mobil/G9o8eoHzpxc-...` design.md → Claude Design → spec.html akışı web projelerine de uyar.)
- Zincire eklenecekler:
  - Restoran brief'i: HTML menü (PDF yasak), Rezervasyon + Sipariş CTA, gömülü harita, tıklanabilir tel/adres, LocalBusiness + Restaurant schema.
  - Teslim checklist'i: sticky mobil alt bar (Ara / Yol tarifi / Rezervasyon), Google Rich Results testi, işlevsiz CTA yok, slider ≤1, font ≤2.
  - Galeri/görsel üretiminde sayı sabitle (örn. 6); AI placeholder'ları teslimden önce değişir.
  - Fiyat: saat değil sonuç; KOBİ tatlı nokta $2–8K; ilk işlerde düşük, her projede artır; bakım retainer'ı 3 kademe ($50–500/ay), 50 × $99 ≈ $5K MRR.
  - Satış kiti: domainli e-posta, sözleşme, Loom, booking linki; yüz yüze ziyaret açılış metni + önce/sonra tek sayfa; "ücretsiz denetim günü" paylaşımı.

## Ek (2026-10-14): Türkçe web bölümü (13 plan) ve mobil tasarım planlarından aktarılanlar

Planlar: `PJ4JAim5-jQ`, `DidvfbtO--c`, `iCyGIF5uIzo`, `wJyLGopw304`, `ydO2_a97J6g`, `tDHWgGpZE2M`, `at7rogD2lMY`, `Lk3rSo1cXBs`, `cE7SI4FTaCw`, `jebtS94HLa0`, `9DYDT_jQwt8`, `tqmZqSdXhEw`, `DFAkocd1nBU` (dosya adları `ajans/planlar/web/<id>-…`).

**Sentezi değiştiren bulgular**
1. **İkinci teslim hattı: WordPress + Elementor (Darrel Wilson, cE7SI4FTaCw + jebtS94HLa0).** Claude Design'da tasarla → ZIP/HTML indir → NovaMira eklentisinin MCP bağlantısıyla Claude Desktop'tan WordPress'e aktar → sonuç native Elementor öğeleri, müşteri kendi düzenler. Aktarım sonrası global renk/font, eklenen CSS sınıfları ve header/footer elle düzeltilir; hatalar "bozuk SS + orijinal SS + olabildiğince benzet" tek prompt'uyla giderilir. NovaMira geliştirme/staging için tasarlanmış: yedek al, teslimden sonra MCP yetkisini kaldır.
2. **Karar kuralı:** vitrin ve premium hareket isteyen müşteri → statik HTML hattı (mevcut zincir). "Kendim düzenleyeceğim / blog-SEO / form-popup" diyen müşteri → WordPress hattı.
3. **Tasarım önce dışarıda kilitlenir.** Stitch (Redesign + URL, varyantlar, DESIGN.md export, kodu kopyala) ya da Claude Design'da tasarla → Claude Code'a "@DESIGN.md + kod, sadık kal, responsive" ver (PJ4JAim5-jQ). Ek sayfalar DESIGN.md sayesinde aynı tipografide çıkar ve token maliyeti düşer. Mobil tarafta aynı kural: "tasarım kilidi" ve her prompt'ta "mevcut görsel tasarımı değiştirme" (`../mobil/s4E8iQBUrJA-…`).
4. **Mimar, Malzeme, Usta (iCyGIF5uIzo):** `/design` skill'i yalnız iskelet çıkarır; 21st.dev MCP her blok için 2-3 insan yapımı bileşen adayı getirir, seçimi insan yapar; Claude birleştirir. ANF (Ed Hill) ile aynı fikir, MCP ile hızlandırılmış. Maliyet uyarısı: tek videoda Max 5x 5 saatlik limitin %94'ü doldu.
5. **Proje MD'si ile sınır koyma (ydO2_a97J6g):** "Nano Banana + Fal kullan, frontend-design skill'i kullan, en fazla 30 görsel / 10 video, bitince Vercel'e deploy et." Model adı açıkça yazılmazsa Claude ucuz modele düşer (Flux Dev); API anahtarı `.env`'e konacak diye ayrıca söylenmeli (tDHWgGpZE2M). Claude "deploy ettim" dedi ama etmemişti; Vercel'de deployment tarihine bakarak doğrula.
6. **Kılavuz MD dosyası soru sordurur (at7rogD2lMY, DidvfbtO--c):** rol + başlangıç soruları (marka adı, estetik yön seçenekleri, 3 değer önerisi, ziyaretçi aksiyonu, fiyatlar) + "asla değişmez" görsel kurallar. Bizim BRIEF şablonuyla aynı yön; Antigravity'nin ücretsiz kredileriyle satış öncesi demo maliyeti sıfıra yakın.
7. **Kare kare scroll hero, Türkçe reçete (DidvfbtO--c, tDHWgGpZE2M):** Nano Banana Pro ile ilk kare + "parçalanmış" son kare → fal üzerinde Kling 3.0 start/end frame (12 sn, sessiz, slow motion) → MP4'ü JPG karelere böl (001-289) → `image/` klasörü → "her görsel bir frame, hero'ya scroll animasyonu ekle". Aynı akış iç mimarlık sitesinde çok iyi, otomasyon ajansı sitesinde sönük kaldı: konu fiziksel ürün ya da mekân değilse bu efekti seçme.
8. **Statik teslim kuralları (wJyLGopw304, Lk3rSo1cXBs, tqmZqSdXhEw):** ölü buton bırakma (abone ol, "oku"), ek sayfalar "temaya bağlı kal" ile ayrı HTML, `#anchor` linkleri sayfa linkine çevir, logo ana sayfaya bağlı, Tally form embed, Google Analytics etiketi, domainli e-posta, SEO + GEO maddesi. cPanel'de zip açılınca dosyalar alt klasörde kalır, köke taşı. Admin paneli girişsiz bırakılmaz.
9. **Terminalsiz yol (wJyLGopw304):** UI UX Pro Max reposu ZIP olarak claude.ai sohbetine sürüklenir, yanına Coolors paleti eklenir. Ekipte Claude Code kullanmayan biri için yedek akış.
10. **Mobil klasördeki tasarım planlarından web'e alınacaklar:** Impeccable skill'i teslim öncesi bir kez "anti-slop" geçişi (`../mobil/ljJuOxTsrtY-…`); Mobbin referanslarını skill klasörüne koyan `tasarim-ilham` skill'i (`../mobil/nbk0PMS0tos-…`); farklı iki dikeyden extract → expand → merge ile kendi tasarım sistemi (`../mobil/wiQMsQwWJQE-…`); önce kilit bileşenin 3-4 varyantı; illüstrasyonu Claude'a SVG çizdirmek yerine görsel modelle üretmek (`../mobil/76_h94U4ztQ-…`).

**Türkiye fiyat ve maliyet verisi (yeni)**
| kalem | değer | kaynak |
|---|---|---|
| Reklamlardaki site fiyatı | 10.000-15.000 TL | 9DYDT_jQwt8 |
| Ajansa yaptırma | 30.000-40.000 TL | 9DYDT_jQwt8 |
| Önerilen giriş fiyatı | 5.000-7.000 TL | 9DYDT_jQwt8 |
| Yurt dışı çapa | 2.000-3.000 $ | ydO2_a97J6g |
| Hostinger Premium, 12 ay | 1.439 TL (hosting + domain + e-posta) | Lk3rSo1cXBs |
| 21st.dev ücretli | ~1.200 TL / 3 ay | iCyGIF5uIzo |
| Fal limitsiz bırakılırsa | ~100 $ / 100 istek | ydO2_a97J6g |

**Satış zinciri (ydO2_a97J6g):** Apify Google Maps scraper ile kategori + semt taranır (örn. Kadıköy restoranları), sitesi olmayana yeni site, olana yenileme teklif edilir; kısa soğuk e-posta (Instantly). Dönüş %7-12, yarısı olumlu; %7-8 dönüşte 100 e-postadan ~4 toplantı.

**Değeri düşük çıkanlar:** tqmZqSdXhEw ve DFAkocd1nBU yeni teknik getirmiyor; 9DYDT_jQwt8'in değeri yalnız açıklamadaki e-ticaret prompt'unda (15 tablo, RLS, pending_payment, çerez onayı sonrası Pixel/GA4).

**Açılmamış kaynak linkleri (YouTube dışı, sonraki adım):** İsa Nurdoğdu GEMINI.md, Emrullah Yaprak prompt paketi, Darrel Wilson Evernote/Tiberium prompt'ları ve iki WordPress skill'i (Drive). Burhan Kocabıyık'ın söylediği kılavuz MD ve repo açıklamalarda yok.
