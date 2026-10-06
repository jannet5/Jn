# Tasarım, DESIGN.md, anti-slop ve animasyon — X.com bulguları

## 1. Problem: "AI slop" tell'leri (herkesin listesi aynı)
Inter/Roboto/Arial · mor→mavi gradient (beyaz üstünde) · 3-4 özdeş rounded kart grid · tek zayıf hover state · emoji ikon · left-border kart · SVG illüstrasyon · her başlığın üstünde ALL-CAPS eyebrow · "A · B · C" meta · "→" buton sonu · tinted near-black (#0B0B0B) · glassmorphism+soft shadow · "Welcome to our platform" boş copy · her section'da fade-slide-up giriş. Neden: model eğitim verisinin istatistiksel ortalamasına ("güvenli" seçime) gider — "distribution convergence" (Anthropic terimi; sitin, nett0, edinsoncode).
Çözüm katmanları: (a) referans göster (ekran görüntüsü/URL), (b) DESIGN.md / design system dosyası, (c) taste skill'i (üretim), (d) denetim skill'i (audit), (e) **göz ver**: Playwright screenshot döngüsü.

## 2. Anthropic frontend-design SKILL.md (ham tam metin, github.com/anthropics/skills)
277k+ kurulum; `/plugin marketplace add anthropics/skills && /plugin install frontend-design`. Yerel yol: `~/.claude/plugins/marketplaces/claude-code-plugins/plugins/frontend-design/skills/frontend-design/SKILL.md` — markdown, düzenle (ör. "sadece Geist/DM Sans kullan").
Özü (metinden):
- "Design lead at a studio known for giving every client a distinct visual identity… client has already rejected proposals that felt cliché."
- **Konuyu temellendir**: brief ürünü söylemiyorsa sen belirle ve onayla; sektörün malzemesi/dili ayırt edici seçimlerin kaynağı.
- Hero: konunun dünyasındaki en karakteristik şeyle aç (headline/görsel/animasyon/canlı demo). "Big number + small label + gradient accent" default'tur.
- Tipografi kişiliği taşır: 1 veya 2 aile (ikiyse net farklı); Elements of Typographic Style ölçeği; satır <80 karakter; serif'e daha fazla line-height. **Yasak tell'ler**: başlıkta tek kelimeyi italik/renkli yapmak, etiketlerde all-caps, içerik üstünde gereksiz etiket.
- Yapı = bilgi: numaralı 01/02/03 sadece gerçekten sıra varsa.
- Hareket: kullanıcı tetiklemeli olmayan motion nadir ve tek orkestre an; her section'da fade-slide-up ve her kartta hover = AI tell. Aksiyona cevap veren motion hoş.
- Kalibrasyon — şu anki AI tasarım kümeleri (default'tur, seçim değil): (1) krem #F4F1EA + yüksek kontrast serif + terracotta #D97757 (Anthropic'in kendi vurgusu → müşteri işinde tell), (2) near-black + tek asit yeşil/vermilion vurgu, (3) broadsheet: hairline, 0 radius, gazete kolonları, (4) SaaS kart kiti: aynı radius/aynı gri gölge/gradient wash, (5) şablon kromu: ALL-CAPS eyebrow, orta nokta meta, "WORD — fragment", tinted black, mono data etiketi, "→". Brief bir yönü sabitliyorsa ona uy; serbest bıraktığı eksende default'a harcama.
- **İki geçiş**: önce kompakt token sistemi (4-6 renk hex, tipografi rolleri, ASCII wireframe ile layout + hizalama, ilkeler) → **brief'e karşı review**: "benzer bir prompt'ta aynı yere mi varırdım?" → generic kısmı revize et, ne değiştiğini söyle → sonra kod. CSS specificity çakışmalarına dikkat.
- **Cesareti tek yere harca**; kalite tabanı: mobil responsive, görünür klavye focus, reduced motion, erişilebilir kontrast. Screenshot alıp kendini eleştir ("a picture is worth 1000 tokens"). Chanel: çıkmadan bir aksesuar çıkar.
- Copy: kullanıcı perspektifi, aktif ses, CTA ne yapıyorsa onu söyler ("Save changes" > "Submit"), hata/boş state yön verir, sentence case, filler yok.

## 3. Alternatif/ek taste skill'leri
| Skill | Kurulum | Ne yapar | Not |
|---|---|---|---|
| **Impeccable** (Paul Bakaus, jQuery UI yaratıcısı; 45-70k★) | `npx impeccable install` → `/impeccable init` (PRODUCT.md yazar) | 1 skill, **24 komut**, 61 deterministik detector (LLM'siz CLI + Chrome overlay). Modlar: brand (pazarlama) vs product (dashboard). 4/8px grid, OKLCH, clamp() type, WCAG AA. | Komutlar: craft, shape, critique, audit, **polish**, bolder, quieter, distill, harden, onboard, animate, colorize, typeset, layout, delight, overdrive, clarify, adapt, optimize, live, generate, **document** (DESIGN.md üret), extract. `/impeccable pin audit` → `/audit`. frontend-design ile birlikte kurma. |
| **taste-skill** (Leonxlnx; 100k+ kurulum) | `npx skills add https://github.com/Leonxlnx/taste-skill --skill design-taste-frontend` | v2: brief'ten dil çıkarır, 3 dial: VARIANCE / MOTION / DENSITY (default 8/6/4, artsy-kinetik). Default stack Next+Tailwind+Framer Motion+Radix; asimetrik layout, GSAP skeleton, em-dash yasağı. | Diğer skill'ler: gpt-taste, image-to-code, redesign-existing-projects, high-end-visual-design, minimalist-ui (Notion/Linear), industrial-brutalist-ui, stitch-design-taste (DESIGN.md export); imagegen-frontend-web/mobile, **brandkit** (logo yönü, palet, tipografi board'u). |
| **ui-ux-pro-max** (nextlevelbuilder; 29-105k★) | repo README | Tasarım veritabanı: 67-79 UI stili, 161 palet, 57 font çifti, 99 UX kuralı, 192 reasoning kuralı, 16 stack; Python CLI ile sorgu; v2 **Design System Generator** (ör. "Serenity Spa": pattern Hero-Centric + Social Proof, stil Soft UI Evolution, bölümler Hero/Services/Testimonials/Booking/Contact). | Boş sayfadan başlarken; design system varsa atla. web-design-guidelines ile çakışır. Ne kadar spesifik brief, o kadar iyi. |
| **web-design-guidelines** (vercel-labs/agent-skills) | `/plugin marketplace add vercel-labs/agent-skills && /plugin install web-design-guidelines` | 100+ kural denetimi (focus, zoom, paste, loading, heading, ARIA), ihlalleri preview'da turuncu işaretletebilirsin. | Proje sonunda QA. |
| emil-design-eng / animate (Emil Kowalski) | — | Motion kuralları: micro 150-250ms, standart 200-350ms, çıkış girişten hızlı (300 in/200 out), stagger 30-60ms, asla >1s, girişte ease-out, sadece transform+opacity, prefers-reduced-motion. | |
| theme-factory (Anthropic/Composio) | anthropics/skills | Token sistemi (renk/spacing/type ramp CSS değişkenleri), tek seferlik hex icadını önler. | |
| canvas-design, algorithmic-art, brand-guidelines (Anthropic) | anthropics/skills | Sosyal grafik PNG/PDF; generatif arka plan; markayı skill'e kodla. | Sosyal medya hattı için. |
| Astryx (Meta) | GitHub | 150+ komponent, 7 tema, agent-ready design system, CLAUDE.md dahil; React + StyleX. | Sistem tutarlılığı. |
| Önerilen kombinasyon (West Lord) | | 1 base taste (frontend-design **veya** impeccable **veya** taste) + emil (motion) + theme-factory (token) + **playwright** (göz). | |

## 4. DESIGN.md hareketi
- Google Stitch'in kavramı: AGENTS.md "nasıl build edilir", **DESIGN.md "nasıl görünür"**. Stitch MCP Claude Code/Cursor/Gemini CLI'a bağlanır; PRD→design→code tek döngü (PawelHuryn 3k beğeni; Prajwal Tomar 1.8k).
- **awesome-design-md** (VoltAgent, 73 dosya): Stripe, Vercel, Linear, Notion, Supabase, Apple, Claude, Cursor, Expo, Lovable, Raycast, Superhuman, Warp, PostHog… proje köküne at, "build me a page that looks like this". getdesign.md'den istek. Mobile starter kit + website launch kit var.
- **Refero Styles** (styles.refero.design): 2000+ gerçek ürün sitesinden AI-okunur design system + DESIGN.md; minimal/clean SaaS/brutalist/monochrome/playful filtreleri. Örn. Linear: near-black, paper-white type, tek asit-lime aksiyon rengi, hairline border, kompakt spacing.
- Vox yöntemi (prompts.md #2): Godly/Refero/One Page Love ekran görüntüleri → Claude analiz → DESIGN.md (gerçek değerler; tahminleri işaretle; tek ekran) → CLAUDE.md'ye `@DESIGN.md`.
- Stitch Skills (Google): code-to-design, generate-design, manage-design-system, extract-design-md, react-components, remotion walkthrough, shadcn-ui, enhance-prompt, stitch-loop (tek prompt'tan çok sayfalı site), taste-design.
- Design DNA: referans URL/ekran görüntüsü → JSON (design system + style + visual effects katmanları), versiyonlanır. Hue: bir markayı öğrenip skill üretir (cursor.com gibi). design-extract.
- Moonchild (Mnimiy): design system paketi (tokens + components + guidelines) → export → Claude Code'a input; HTML/Figma SVG import PNG'den iyi; "Add DS" ile her üretime iliştir. Sonuç 2/10→9/10 on-brand.
- Claude Design (claude.ai/design, beta): `claude mcp add --scope user --transport http claude-design https://api.anthropic.com/v1/design/mcp` → `/design-consent` → `/design-sync` (GitHub/Figma/codebase'den design system). Export: zip/PDF/PPTX/HTML/Canva/Lovable/Vercel/**Claude Code handoff**. Sızan sistem prompt'u: "Mocking from scratch is a LAST RESORT" (önce UI kit/Figma/codebase/screenshot ara), anti-slop blocklist, doğrulamayı **fork edilmiş subagent** yapar.
- Figma → Claude Code (Figma MCP): happy path'i Figma'da, 15 dk'da temel, sonra 50 saat iterasyon (Tommy Geoco). "Send this to Figma" ile tersi (Codevolution).
- Spec-driven dosya düzeni: constitution.md, product.md, **design.md** (kimlik, tipografi, renk, spacing, yasaklı pattern'ler), architecture.md.
- listudio'nun sentezi: tasarımcı iki dosya tutacak — Figma (insanlar için) ve DESIGN.md + DNA JSON (agent'lar için). "Designers will write skills."

## 5. Görsel-önce iş akışları (image → code)
- GPT-Image-2 / Nano Banana ile yüksek sadakatli UI mockup üret → coding agent'a "pixel-perfect recreate" → tarayıcıda doğrula. **Aynı oturumda üretme+kodlama drift yapar; görseli üret, YENİ oturumda referans olarak ver** (Alex Volkov bulgusu). Elle: screenshot'u referansla yan yana koydurup farkları tek tek düzelttir.
- readdy → ekran görüntüsü → Claude "match it" (Yarchi, mobil).
- Stitch → Lovable: ekranları Claude'da haritala → Stitch'te tasarla → kodu export → Lovable (Prajwal).
- Huashu Design (Çin): tek satırdan prototip/deck/PPTX/MP4/infografik; Core Asset Protocol (ask→search→download→verify→brand-spec.md); "junior designer workflow": varsayımları listele, erken göster.
- Open Design: lokal, açık kaynak Claude Design alternatifi; PATH'teki tüm agent CLI'ları algılar.
- Avid'in ilkeleri: önce akış, sonra ekran, sonra state, en son piksel; **prompt = brief, 500 kelime** (kitle, kısıt, his, hayran olduğun ve olmak istemediğin referanslar); design system repo'da `principles.md`.
- Meng To: "vibe design de yap" — ilk UI blueprint olur; 21st.dev, reactbits, uiverse, tailwind şablonları ver.

## 6. UI kütüphaneleri & komponent kaynakları (shadcn üstüne 17 — Om Patel)
Origin UI (tutarlı dil, navbar/input/calendar) · MVP Blocks (framer motion animated sections, pricing/waitlist/bento) · Kobalte · Skipper UI (slide button, carousel) · **Magic UI** (50+ animated, Vercel/Stripe kullanıyor) · **Aceternity UI** (3D, spotlight, parallax) · Cult UI (form/dashboard) · Luxe UI (premium testimonial/CTA) · Animata (copy-paste animasyon) · **Phosphor Icons** (lucide yerine, 9000+/6 ağırlık) · PatternCraft (arka plan doku) · Tailark · Smooth UI · **Motion Primitives** (scroll trigger/reveal) · Kokonut · Bundui (hero/features/pricing section) · Prism UI (glass). Ayrıca 21st.dev **Magic MCP** (`github.com/21st-dev/magic-mcp`: "give me a clean login page" → komponent IDE'de; SVG logo finder; /ui komutu), reactbits, uiverse, HeroUI Native (RN), shadcn/lint (agent-first Tailwind design-rule linter).

## 7. Web animasyon
- Framer Motion (React komponent/layout) vs GSAP (scroll storytelling/timeline; Awwwards %90). Vague "add scroll animation" yerine: "GSAP ScrollTrigger, scrub true, rotateX 8, scale 0.92, y -60, ease power2.out".
- Osmo piksel dalgası sayfa geçişi: iki sayfa stack, clip-path + `steps(12)` stop-motion, [data-transition-col]/[data-transition-pixel] helper, stagger per column; `pixelHorizontalAmount=12, transitionDuration=1, pixelFadeDuration=0.2, pixelOverlap=0.3`; portre ekranda yön otomatik değişir. 8 saat iş. Osmo Supply boilerplate + Barba.js.
- Viktor Oddy (motionsites.ai) hero/landing prompt'ları: başlangıçta tam design system (CSS değişkenleri), gerçek HLS/MP4 video URL'leri, framer-motion + anti-slop kuralı ("Awwwards-level motion only"), **BlurText** word-by-word blur reveal, liquid-glass CSS sınıfları (tam prompt prompts.md #16). 20 prompt kataloğu: cinematic hero, full landing, animated deck, liquid glass utility, one-shot 7 adım, Claude+Nano Banana, Gemini.
- Remotion skills (117k haftalık kurulum): React ile MP4; remotion-superpowers (13 komut, voiceover, caption, render→review loop). Ürün demo/sosyal video.
- Mobil: Reanimated 4 (CSS animations), Moti, Skia, skottie, text-to-lottie (`npx skills add diffusionstudio/lottie`), expo-animation skill.

## 8. Tipografi (AmirMushich, 10 yıl)
Google Fonts seçimleri: Manrope (SaaS/dijital, SemiBold başlık + Regular gövde), Fraunces ("old money" serif, yemek/heritage/fintech), Syne (sanat/stüdyo), Albert Sans (startup/lifestyle), Merriweather (uzun okuma), Tenor Sans (editoryal), Space Grotesk (dev/web3), Google Sans, IBM Plex Sans (kurumsal). Başlıkta tracking -3..-7%, leading 0.8-0.9. Nano Banana + LTX Elements ile font tutarlı görsel üretimi. Ballaz: Plus Jakarta Sans, SF Pro, Satoshi. tonny: "display font olarak Inter yasak".

## 9. Doğrulama döngüsü (göz)
- Playwright MCP: `claude mcp add playwright -s user -- npx @playwright/mcp@latest`; Task → Claude ⇄ Playwright ↻. Her section'ı iki viewport'ta screenshot'la, hata kalmayana kadar.
- Claude Chrome extension (Boris: her değişiklik tarayıcıda test).
- OneRedOak Design Review workflow (PR'da).
- Vox'un 5 subagent / 20 kontrol listesi (prompts.md #2b); Tom'un onay döngüsü checklist'i (#9).
- Impeccable `/audit` + `/critique` + Chrome overlay; web-design-guidelines; UI-UX-Pro-Max'in a11y kuralları.

## Bizim için çıkarımlar
1. Web hattı tasarım kapısı: (a) müşteri sektörüne göre 3 referans (Refero/Godly/awesome-design-md) → DESIGN.md; (b) frontend-design + theme-factory + emil motion kuralları CLAUDE.md'de; (c) build; (d) 3 ayrı cila geçişi; (e) Playwright 375/1440 screenshot + /impeccable audit (veya web-design-guidelines) → (f) 20 kontrol.
2. Kendi vitrinimiz için Viktor Oddy'nin "AI web design agency" prompt'u (#16) birebir başlangıç.
3. Yasaklı liste CLAUDE.md'ye: Inter display, mor gradient, emoji ikon, left-border kart, her section fade-up, "→" buton, ALL-CAPS eyebrow, krem+terracotta default.
4. Sosyal içerik için canvas-design + brand-guidelines skill'leri + Nano Banana 9'lu feed prompt'u.
