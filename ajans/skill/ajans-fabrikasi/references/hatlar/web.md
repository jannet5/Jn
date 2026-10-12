# HAT: WEB SİTESİ

Hedef: "$10k görünümlü", yerel SEO'lu, tek aksiyona götüren site. 1 iş günü.
Stack varsayılanı: **Astro + Tailwind** (statik, hızlı) → Cloudflare Pages. Etkileşim yoğunsa Next.js + Motion + GSAP/Lenis → Vercel.
Skill'ler: frontend-design (Anthropic) · ui-ux-pro-max · web-design-guidelines (Vercel) · emil animate · gsap-skills · impeccable · hallmark · design-review ajanı.

## Profil ve mod (BRIEF'ten)
- `profil: yerel` (fiziksel dükkân: restoran, berber, klinik) → W3'teki yerel işletme zorunluları ŞART.
- `profil: uzaktan-hizmet` (ajans, danışman, online) → form/harita/şehir sayfası yok; tek aksiyon (WhatsApp/randevu) yeter.
- `mod: otonom` (insan kapıda beklemiyor) → W3+W4 tek adım; "Ask me clarifying questions" yerine kararlar `DURUM.md`'ye yazılır.

## Bilinen tuzaklar (ilk üretimden)
- Fiyat, süre, lansman adedi `src/site.config.ts`'te; metin şablonda. Kullanıcı testi fiyatları sayfada arar.
- Karşılaştırma tablosu mobilde yatay kaydırma yapar → aynı veriden `md:hidden` kartlar, bizim kart `order-first`.
- Grid'de `row-span-2` kartın figcaption'ı komşu satıra taşar → figure `flex flex-col`, iç kart `flex-1`.
- Playwright npm sürümü `/opt/pw-browsers` Chromium'uyla uyuşmayabilir → `executablePath` ver (şablon `scripts/ss.mjs` yapıyor).
- `scroll-behavior: smooth` SS'te reveal tetiklemez → `scrollTo({behavior:'instant'})`; çizim/reveal için ≥1.7 sn bekle.
- Tam sayfa SS'ler Read ile bakılınca 60 px'e küçülür → **1500-1800 px dilimlere kırp, dilimlere bak.**
- Google Fonts render'ı bloklar (mobil Lighthouse 81) → `preload` + `media="print" onload` kalıbı; KVKK için fontsource ile self-host seçeneği.
- Tailwind tek sütun grid: `grid-cols-1` ile başla; uzun e-posta/URL'ye `break-all`.
- Koyu modda koyu bloklar zeminle birleşir → `ink-border` token'ı ile çerçeve.

## Hazır betikler (`sablon/web/scripts/`, yeni-musteri.sh kopyalar)
- `ss.mjs <önek>` → 375/768/1440 + koyu SS · `kullanici-testi.mjs` → tıklama, kırık link, klavye, 200% metin, taşma
- `artifact.py dist/index.html cikti.html "Ad"` → müşteriye telefondan gösterilecek önizleme sayfası
- Lighthouse: `CHROME_PATH=$(ls -d /opt/pw-browsers/chromium-*/chrome-linux/chrome|head -1) npx -y lighthouse@12 <url> --chrome-flags="--headless=new --no-sandbox" --only-categories=performance,accessibility,best-practices,seo`

## Ek kurallar (YouTube web özeti, `planlar/web/OZET.md` §4 tam anti-slop listesi)
- **Marka skill'i:** her müşteri için `.claude/skills/marka-<musteri>/SKILL.md` (hex, font, boşluk, yap/yapma). Web, mobil ve sosyal hat aynı dosyayı okur.
- **Kadranlar:** brief'ten tek satır "tasarım okuması" + değişkenlik/hareket/yoğunluk (restoran-premium 7/6/3, klinik-güven 3-4/2-3/4-5).
- **Düzen çeşitliliği:** 8 bölümde ≥4 farklı düzen ailesi; aynı aile sayfada bir kez. Tek "zirve anı" (en büyük görsel bütçesi orada).
- **Parmak izi kapısı:** yeni site, önceki müşteri sitelerinden 6 boyutun (palet, font, hero tipi, düzen ritmi, imza etkileşimi, görsel dil) en az 4'ünde farklı olmalı → `musteriler/PARMAK-IZI.md`'ye satır ekle.
- **Hazır bileşen > ekran görüntüsü:** 21st.dev "Copy Prompt" / Magic UI / Aceternity; SS'ten kopya kayıplıdır.
- **Görsel zinciri:** Nano Banana (2K, referanslı) → Kling/Veo 3-5 sn döngü → hero ≤300 KB; mobilde statik görsel.
- **Metin:** em dash (—) ve en dash (–) yasak; "John Doe/Acme", "Elevate/Seamless" yasak; aynı eylem her yerde aynı kelime.
- Ek skill'ler: taste-skill (`npx skills add Leonxlnx/taste-skill`), scroll-craft (`/plugin marketplace add nateherkai/scroll-craft`). Aynı anda en fazla bir "taste" skill'i (çakışıyorlar).

## Adımlar

### W1. Kurulum (proje klasörü)
`bash ajans/sistem/yeni-musteri.sh <ad> web` → `CLAUDE.md`, `BRIEF.md`, `DESIGN.md` şablonları gelir. CLAUDE.md `@DESIGN.md` import eder.

### W2. Referans → DESIGN.md (KAPI-1)
1. Sektöre göre 3 referans: Awwwards / Godly / Refero / One Page Love / `npx getdesign@latest add <marka>` / `arastirma/github/kopyalar/VoltAgent-awesome-design-md`. Hero + 1 bölüm + footer SS'i → `ref/ref-1..9.png`.
2. Prompt (Vox):
```
These are screenshots of sites I like. Don't touch any code yet:
1. For each one, tell me what it does well: layout, typography, color, spacing, white space.
2. List what they have in common and propose 3 distinct style directions for BRIEF.md's business.
3. After a direction is chosen, write DESIGN.md with real values: font sizes, weights, color codes, spacing, corner radius. Mark estimates. Don't make anything up. One screen max.
4. Import DESIGN.md into CLAUDE.md with an @ line. Rule: do not introduce new colors — stop and ask.
```
3. `npx @google/design.md lint DESIGN.md` → temiz. 3 yön müşteriye/sahibine gösterilir, biri seçilir.

### W3. Yapı (stil yok)
```
/ui-ux-pro-max
BRIEF.md ve DESIGN.md'yi oku. frontend-design skill'ini uygula.
Kitle: <BRIEF.kitle>. Tek aksiyon: <BRIEF.aksiyon> — her sayfa buna götürür, tek CTA tekrarlanır.
Referanslar: ref/*.png kalite barı. "Tipografi ölçeğini, boşluk ritmini ve hareketi eşleştir. Layout'ları kopyalama."
Stack: Astro + Tailwind, Cloudflare Pages. Statik.
Yerel işletme zorunluları: sağ üstte tıklanabilir telefon/WhatsApp, fold üstünde 3 alanlı form (ad, telefon, hizmet), gerçek fotoğraf, yorumlar, harita, H1 = hizmet + şehir, her hizmet ve şehir için ayrı sayfa, NAP birebir.
Yasak: mor gradyan, emoji ikon, display font olarak Inter, stok foto placeholder, her şeyin ortalandığı layout, 3 eşit kart ızgarası, uydurma metrik, "Elevate/Seamless/Unlock" kopyası, her bölümde aynı fade-up.
Önce sadece yapıyı (bölümler + gerçek metin) kur, stil yok. Ask me clarifying questions.
```

### W4. Stil + görsel
- Yapı onaylanınca: "Şimdi DESIGN.md'ye göre stillendir. Sadece DESIGN.md değerleri."
- Görsel zinciri: Claude görsel prompt'unu yazar → Nano Banana / ChatGPT image → hero için Veo/Kling image-to-video → upscale. Gerçek fotoğraf varsa her zaman o öncelikli.
- Hero/özel bölüm için 21st.dev / Magic UI / Aceternity bileşen prompt'u kopyalanır; mimariye uyarlanır.

### W5. Görsel QA döngüsü (KAPI-2)
`node scripts/ss.mjs v1` → dilimle → design-review ajanı (`.claude/agents/design-review-agent.md`, yeni-musteri.sh kopyalar) 0-4 puanlar → hallmark slop-test (+ kuruluysa impeccable) → <3/4 ise düzelt, en fazla 3 tur.
**Yedek (ajan yoksa):** her dilim için sor: hiyerarşi tek bakışta okunuyor mu · boş/ölü alan var mı · aynı kart ritmi tekrar ediyor mu · yer tutucu kutu kaldı mı · primary renk 3'ten fazla yerde mi · mobilde taşma/kırpılma var mı · koyu modda kaybolan blok var mı.

### W6. Cila (3 AYRI mesaj — tek mesajda istenmez)
```
1) Sadece tipografi: her başlık/gövde boyutunu incele, katı tip ölçeği kur, satır yüksekliği ve harf aralığını düzelt. Başka hiçbir şeye dokunma.
2) Sadece boşluk: dikey ritmi bölüm bölüm denetle, sıkışık bölümlerde boşluğu iki katına çıkar. Başka hiçbir şeye dokunma.
3) Sadece hareket: scroll-reveal ve hover durumları ekle, ince, 200-300ms, hiçbir şey zıplamasın. Bölüm başına en fazla bir özel cursor/mikro etkileşim. Aşırıysa "more subtle, more refined".
4) Mobil turu: 375px'te ne gizlenmeli, ne sıkılaşmalı, ne yeniden boyutlanmalı? Tasarlanmış görünsün, küçültülmüş değil.
5) "Daha pahalı" turu (ölçülebilir): ölü boşluk yok · boş placeholder kutu yok · art arda aynı kart ritmi yok · her bölümde bir "el yapımı" detay (çizgi, doku, mikro etkileşim) · alt bölümler üst bölümler kadar özenli.
```

### W7. Kontrol (KAPI-3) ve teslim
- `kapilar/KAPILAR.md` → 20 kontrol (5 alt ajan, salt okuma) + 10K checklist dürüst puanlama + Lighthouse ≥90.
- Yayın: `npm run build` → Cloudflare Pages (output `dist`) ya da Vercel. Domain bağla. Google İşletme Profili'ne site linki.
- Teslim paketi: canlı link + before/after SS (vaka çalışması için izin) + bakım teklifi.
