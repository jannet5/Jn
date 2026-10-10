# WEB SİTESİ + TASARIM — YouTube araştırması ÖZETİ

Kapsam: index.md'deki 3 bölüm ("landing page animations scroll", "framer motion gsap", "DESIGN.md skill") + 5 ek arama (claude design tutorial, awwwards style, website skill steal, restaurant website, nano banana animated). Toplam 98 benzersiz video kuyruğa alındı; YouTube bu bulut IP'sine altyazı indirmede **HTTP 429 (rate limit)** uyguladığı için 8 transkript çekilebildi (Invidious/Piped yedekleri de kapalı ya da boş döndü). 99 videonun açıklaması çekildi; açıklamalardaki 7 kaynak (skill/şablon/repo) açılıp `ajans/arastirma/youtube/kaynaklar/` altına aynen kaydedildi. 9 plan yazıldı (`ajans/planlar/web/`).

Durum: `transkriptler/<id>.txt` var = transkript çekildi; `.aciklama.txt` = açıklama. Altyazısı hiç olmayan: QUI6Ug4cHnE. 429 nedeniyle çekilemeyenler index'te A/B olarak duruyor; başka IP'den `python3 ajans/araclar/yt_transcript.py <id>` ile tamamlanmalı (kuyruk: `kuyruk.txt` mantığı A→B→C).

## 1. Ortak kalıplar (9 planın kesişimi)

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
