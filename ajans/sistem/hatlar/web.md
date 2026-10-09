# HAT: WEB SİTESİ

Hedef: "$10k görünümlü", yerel SEO'lu, tek aksiyona götüren site. 1 iş günü.
Stack varsayılanı: **Astro + Tailwind** (statik, hızlı) → Cloudflare Pages. Etkileşim yoğunsa Next.js + Motion + GSAP/Lenis → Vercel.
Skill'ler: frontend-design (Anthropic) · ui-ux-pro-max · web-design-guidelines (Vercel) · emil animate · gsap-skills · impeccable · hallmark · design-review ajanı.

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
Playwright ile 375 / 768 / 1440 tam sayfa SS → design-review ajanı (`.claude/agents/design-review-agent.md`) 0-4 puanlar → impeccable dedektörleri + hallmark slop-test → <3/4 ise düzelt, en fazla 3 tur.

### W6. Cila (3 AYRI mesaj — tek mesajda istenmez)
```
1) Sadece tipografi: her başlık/gövde boyutunu incele, katı tip ölçeği kur, satır yüksekliği ve harf aralığını düzelt. Başka hiçbir şeye dokunma.
2) Sadece boşluk: dikey ritmi bölüm bölüm denetle, sıkışık bölümlerde boşluğu iki katına çıkar. Başka hiçbir şeye dokunma.
3) Sadece hareket: scroll-reveal ve hover durumları ekle, ince, 200-300ms, hiçbir şey zıplamasın. Bölüm başına en fazla bir özel cursor/mikro etkileşim. Aşırıysa "more subtle, more refined".
4) Mobil turu: 375px'te ne gizlenmeli, ne sıkılaşmalı, ne yeniden boyutlanmalı? Tasarlanmış görünsün, küçültülmüş değil.
5) Toplu: "Lower sections feel generic. Not busier — more expensive."
```

### W7. Kontrol (KAPI-3) ve teslim
- `kapilar/KAPILAR.md` → 20 kontrol (5 alt ajan, salt okuma) + 10K checklist dürüst puanlama + Lighthouse ≥90.
- Yayın: `npm run build` → Cloudflare Pages (output `dist`) ya da Vercel. Domain bağla. Google İşletme Profili'ne site linki.
- Teslim paketi: canlı link + before/after SS (vaka çalışması için izin) + bakım teklifi.
