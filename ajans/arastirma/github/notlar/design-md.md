# DESIGN.md / Tasarım Sistemi → Ajan Araştırma Notları

**Kategori:** DESIGN.md, tasarım sistemi kaynakları, "design system → agent" araçları
**Tarih:** 2026-10-05
**Amaç:** Ürettiğimiz web sitesi ve mobil uygulamaların jenerik / "AI yapmış" görünmemesi; her müşteri (restoran, klinik, saatçi…) için tutarlı ve güzel bir tasarım dili üreten, ajana (Claude Code / Codex) okutulabilir bir DESIGN.md şablonu ve çevresindeki araç zincirini kurmak.
**Birebir kopyalar:** `/home/user/Jn/ajans/arastirma/github/kopyalar/<repo-adi>/` (her klasörde `KAYNAK.md`)

> Kısa özet: 2026 itibarıyla "DESIGN.md" fiilen bir standart. Google Labs resmi spec'i yayınladı (`google-labs-code/design.md`, YAML token + 8 bölüm prose + `npx @google/design.md lint/export`). Topluluk tarafında VoltAgent'ın getdesign.md koleksiyonu (9 bölümlü "Stitch formatı"), typeui.sh'in SKILL.md+DESIGN.md çiftleri, Meliwat'ın mobil (SwiftUI/Expo/Compose) paketleri ve oh-my-design'ın "felsefe → karar tablosu → token" zinciri var. Anti-jenerik tarafta ise Anthropic'in kendi `frontend-design` skill'i, `taste-skill`, `impeccable` ve `anti-ai-slop` aynı tespiti yapıyor: *Inter + mor gradyan + 3 eşit kart + emoji ikon + "Elevate/Seamless" kopyası* bir tell'dir; çözüm "her seçimi brief'ten türet, varsayılanı yasakla, çıktıyı ekran görüntüsüyle denetle".

---

## 1. Kaynak tablosu

| Repo/Kaynak | Yıldız | Son güncelleme | Lisans | Ne işe yarar | Nasıl kullanılır | Bizim için değeri (1-5) |
|---|---|---|---|---|---|---|
| [google-labs-code/design.md](https://github.com/google-labs-code/design.md) | 28.2k | 2026-10-01 | Apache-2.0 | DESIGN.md'nin **resmi spesifikasyonu**: YAML front matter token şeması (colors/typography/rounded/spacing/components) + 8 bölüm prose; lint (WCAG kontrast, broken ref), diff, export (Tailwind v3/v4, DTCG) | `npx @google/design.md lint DESIGN.md`, `export --format css-tailwind`, `spec` komutu ile spec'i ajan promptuna enjekte et | **5** |
| [VoltAgent/awesome-design-md](https://github.com/VoltAgent/awesome-design-md) (getdesign.md) | 119.6k | 2026-10-05 | MIT | 73 gerçek markanın (Notion, Stripe, Airbnb, Starbucks, Apple…) ters mühendislik DESIGN.md'si; 25-35KB/dosya, YAML token + 9 bölüm | `npx getdesign@latest add <slug>` ya da repo'dan `design-md/<marka>/DESIGN.md` kopyala; "bu dosyaya göre sayfa yap" de | **5** |
| [VoltAgent/awesome-claude-design](https://github.com/VoltAgent/awesome-claude-design) | 4.0k | 2026-06-20 | MIT | Aynı koleksiyonun Claude Design (claude.ai/design) için paketlenmişi; 9 bölüm tablosu ve Claude Design'ın DESIGN.md'den ürettiği paket (README+colors_and_type.css+preview/+UI kit+SKILL.md) | Claude Design → "Create new design system" → DESIGN.md yükle; çıkan SKILL.md'yi skills klasörüne al | 4 |
| [rohitg00/awesome-claude-design](https://github.com/rohitg00/awesome-claude-design) | 1.1k | 2026-04-23 | MIT | Estetik aileye göre (editorial/terminal/warm/data-dense/cinematic/playful/glass/brutalist/indie) kısa DESIGN.md'ler; **Anti-Slop Kit**, `break-default-aesthetic` promptu, `brand-to-design-md` promptu, Claude Design parmak izleri tablosu | Prompt'ları DESIGN.md'nin başına/sistem promptuna yapıştır | **5** |
| [bergside/awesome-design-skills](https://github.com/bergside/awesome-design-skills) (typeui.sh) | 3.1k | 2026-06-28 | MIT | 67 estetik "design skill" (cafe, editorial, premium, material, stitch…): her biri SKILL.md (ajan talimatı + kalite kapıları) + DESIGN.md (token) | `npx typeui.sh pull cafe` → `.claude/skills/` | 4 |
| [bergside/design-md-chrome](https://github.com/bergside/design-md-chrome) | 3.0k | 2026-05-25 | MIT | Chrome eklentisi: açık sekmeden tipografi/renk/spacing/radius/shadow/motion çıkarıp DESIGN.md veya SKILL.md üretir; blueprint şablonu | Müşterinin mevcut sitesinden (veya beğendiği referans siteden) 30 sn'de taslak çıkar | 3 |
| [Meliwat/awesome-ios-design-md](https://github.com/Meliwat/awesome-ios-design-md) (spectr.to) | 571 | 2026-05-21 | MIT | **200 mobil uygulama** DESIGN.md'si; her biri nötr + `DESIGN-swiftui.md` + `DESIGN-expo.md` + `DESIGN-android.md`; HIG uyumlu (44pt hit, Dynamic Type, safe area, haptik, spring eğrileri); food kategorisinde OpenTable/Resy/Starbucks | Dosyayı `CLAUDE.md` yanına koy, "Expo ile X ekranını DESIGN-expo.md'ye göre yap" | **5** (mobil) |
| [kwakseongjae/oh-my-design](https://github.com/kwakseongjae/oh-my-design) | 530 | 2026-10-01 | MIT | "Felsefe → karar tablosu (D-id) → token → bileşen sözleşmesi → layout grammar → build → render critique → DESIGN.md" zinciri; **DESIGN.md Core v2** spec (7 anchor); 54 slop gate + 8 sistem-sadakat gate; 45 bileşen craft normu; 500+ referans; `omd book` | `npx oh-my-design-cli@latest` (Claude Code/Codex/Cursor); ya da sadece `slop-gates.md` ve `derivation-chain.md`'yi kendi skill'imize al | 4 |
| [uxKero/anydesign](https://github.com/uxKero/anydesign) | 214 | 2026-09-15 | MIT | Görsel / URL / Figma → `design.md` + DTCG `design-tokens.json` + WCAG raporu; her çıkarım ✅⚠️❓ güven işaretli; Playwright ile computed style yakalar | Claude skill; "bu sitenin tasarım sistemini çıkar" | 4 |
| [SpaceZephyr/brand-design-md](https://github.com/SpaceZephyr/brand-design-md) | 110 | 2026-04-10 | — | "Notion tarzında yap" deyince slug'ı bulup `npx getdesign add` ile DESIGN.md'yi çekip UI üreten tetikleyici skill; marka mix kuralları | Skill olarak kur; müşteri "Apple gibi olsun" dediğinde | 3 |
| [yuvrajangadsingh/brandmd](https://github.com/yuvrajangadsingh/brandmd) | 66 | 2026-09-30 | MIT | `npx brandmd <url> -o DESIGN.md` → Google spec'ine uyan (lint 0 hata) DESIGN.md; `--tailwind`, `--css`, `--agent` (SKILL.md + Cursor rule yazar), `--vision` (Gemini ile illüstrasyon/ses tonu) | Müşterinin eski sitesi varsa ilk taslak; referans site analizi | 4 |
| [ShriPunta/generate-design-md](https://github.com/ShriPunta/generate-design-md) | 2 | 2026-05-08 | Other | Açıklama ya da kod tabanı taramasıyla DESIGN.md üretip lint'leyen minimal Claude skill'i | `/generate-design-md` | 3 |
| [ehmo/platform-design-skills](https://github.com/ehmo/platform-design-skills) | 604 | 2026-03-19 | MIT | Apple HIG + Material 3 + WCAG 2.2'den damıtılmış 450+ kural; ios/ipados/macos/watchos/visionos/tvos/android/web SKILL.md (doğru/yanlış kod örnekli) | `npx skills add ehmo/platform-design-skills`; mobil DESIGN.md'nin "platform" bölümü için | **5** (mobil) |
| [Leonxlnx/taste-skill](https://github.com/Leonxlnx/taste-skill) | 92.8k | 2026-09-26 | MIT | Anti-slop frontend skill (87KB): brief inference, **3 dial** (DESIGN_VARIANCE / MOTION_INTENSITY / VISUAL_DENSITY), tipografi/renk kalibrasyonu, "AI Tells" yasak listesi, premium-consumer palet yasağı, redesign protokolü; brandkit (logo/kimlik görsel) ve stitch-skill | `.claude/skills/taste-skill`; landing/portfolio/redesign için | **5** |
| [pbakaus/impeccable](https://github.com/pbakaus/impeccable) | 76.9k | 2026-10-05 | Apache-2.0 | 24 komutlu tasarım skill'i (`init`→PRODUCT.md, `document`→DESIGN.md, `craft`, `critique`, `audit`, `polish`, `typeset`, `colorize`, `adapt`…), 61 deterministik dedektör (CLI, LLM'siz), canlı tarayıcı varyant modu; iOS/Android native referansları | `npx impeccable install` → `/impeccable init` | **5** |
| [Vinayak-Shukla-03/anti-ai-slop](https://github.com/Vinayak-Shukla-03/anti-ai-slop) | 9 | 2026-07-02 | MIT | Küçük ama en net anti-slop skill'i: 9 sert kural, pre-ship self-audit, **2. derece monokültür** uyarısı (cream + terracotta + Fraunces da artık tell), toolkit (font çiftleri, paletler, ölçekler) | SKILL.md'yi aynen kendi skill'imize gömülebilir | 4 |
| [anthropics/skills — frontend-design](https://github.com/anthropics/skills/tree/main/skills/frontend-design) | 179.8k | 2026-10-05 | Anthropic LICENSE | Anthropic'in resmi tasarım felsefesi skill'i: "design lead" rolü, konuya bağlı tasarım, 5 AI tell kümesi (cream+terracotta, black+acid green, broadsheet, SaaS-card kit, template chrome), iki geçişli süreç (plan→brief'e karşı gözden geçir→kod→öz-eleştiri), yazı/kopya kuralları; theme-factory (10 hazır font+renk teması); brand-guidelines (marka skill şablonu) | Claude Code'da otomatik yüklenir; biz kendi DESIGN.md'mizi bu felsefeyle yazmalıyız | **5** |
| [microsoft/skills — frontend-design-review](https://github.com/microsoft/skills/tree/main/.github/skills/frontend-design-review) | 3.1k | 2026-10-05 | MIT | İki mod: tasarım incelemesi (3 kalite sütunu: frictionless / craft / trustworthy; blocking/major/minor) ve yaratıcı tasarım; checklist + çıktı formatı | PR/ekran inceleme skill'i | 3 |
| [OneRedOak/claude-code-workflows — design-review](https://github.com/OneRedOak/claude-code-workflows/tree/main/design-review) | 3.9k | 2026-09-29 | MIT | **Playwright MCP ile canlı ortamda 7 fazlı design-review subagent'ı** (1440/768/375 ekran görüntüsü, klavye, kontrast, konsol), `/design-review` slash komutu, CLAUDE.md "Quick Visual Check" snippet'i, S-tier dashboard checklist'i | Agent dosyasını `.claude/agents/`, snippet'i CLAUDE.md'ye | **5** |
| [Dammyjay93/interface-design](https://github.com/Dammyjay93/interface-design) | 5.8k | 2026-06-20 | MIT | Ürün arayüzü (dashboard/admin/app) için craft skill'i: "Where defaults hide", domain exploration, her bileşen öncesi Intent/Hierarchy/Palette/Depth/Surfaces/Typography/Spacing beyanı, `.interface-design/system.md` hafızası | `npx skills add …/interface-design`; panel/uygulama işlerinde | 4 |
| [nextlevelbuilder/ui-ux-pro-max-skill](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill) | 133.2k | 2026-10-03 | MIT | Ürün tipine göre stil/palet/font çifti/landing pattern öneren arama motoru (CSV: 192 palet, 74 Google Fonts çifti, 79 stil, 192 ürün tipi; **Restaurant, Medical Clinic, Dental, Veterinary** satırları var) | `python3 search.py "restaurant warm" --design-system` | 4 |
| [shadcn-ui/lint](https://github.com/shadcn-ui/lint) | 3.1k | 2026-10-05 | MIT | Ajan-öncelikli Tailwind tasarım sistemi linter'ı (ESLint/Oxlint): `no-restyle`, `no-raw-colors`, `no-arbitrary-values`; hata mesajı ajana ne yapması gerektiğini söyler | shadcn/Tailwind v4 projelerinde token dışı değerleri CI'da yakalamak | 4 |
| [Owl-Listener/designer-skills](https://github.com/Owl-Listener/designer-skills) | 2.8k | 2026-09-05 | MIT | 273 tasarım skill'i; `visual-critique` (color/typography/hierarchy/composition/brand-consistency, pass/minor/major), `color-system`, `typography-scale`, `design-token`, `theming-system` | `/plugin marketplace add Owl-Listener/designer-skills` | 3 |
| [facebook/astryx](https://github.com/facebook/astryx) | 13.5k | 2026-10-05 | MIT | Meta'nın "agent ready" tasarım sistemi (React 19 + StyleX, 150+ bileşen, 7 tema, CLI); AGENTS.md + vibe-test ile ajan uyumunu ölçüyor | Web uygulaması tarafında shadcn alternatifi; AGENTS.md kurgusu örnek | 2 |
| [VoltAgent/awesome-claude-code-subagents — design-bridge](https://github.com/VoltAgent/awesome-claude-code-subagents/blob/main/categories/01-core-development/design-bridge.md) | 25.5k | 2026-10-05 | MIT | DESIGN.md'yi okuyup diğer subagent'lara (ui-designer, frontend-developer) talimata çeviren köprü ajan; ui-designer subagent'ı | `.claude/agents/design-bridge.md` | 3 |
| [designmd.app](https://designmd.app/) | — | — | açık kaynak | 759 DESIGN.md kütüphanesi (YAML token + 8 bölüm kanonik sıra); ücretsiz; Claude Code/Cursor/Kiro/Windsurf/Stitch | Siteden stil seç, DESIGN.md'yi köke kopyala | 3 |
| [Google Stitch DESIGN.md docs](https://stitch.withgoogle.com/docs/design-md/overview/) | — | — | — | Formatın çıktığı yer: 9 bölüm (Visual Theme & Atmosphere … Agent Prompt Guide); Stitch'ten export/import | Stitch'te tasarla → DESIGN.md export → Claude Code | 3 |
| [tweakcn.com](https://tweakcn.com) / [orchestkit design-system-tokens](https://skills.cat/skills/yonatangross/orchestkit/design-system-tokens) / Style Dictionary | — | — | — | shadcn tema üretici (CSS değişkeni export, light/dark önizleme); DTCG 3 katmanlı token + OKLCH + Style Dictionary skill'i | DESIGN.md'den çıkan token'ları shadcn/Tailwind temasına çevirirken | 3 |

Not: Yıldız/tarih/lisans değerleri `mcp__github__search_repositories` (2026-10-05) çıktısından. `git clone --depth 1` ile 25 repo klonlanıp DESIGN.md/SKILL.md dosyaları doğrudan okundu.

---

## 2. DESIGN.md anatomisi (incelenen örneklerden çıkan ortak yapı)

Piyasada dört format ailesi var; hepsi aynı 8-9 çekirdeği taşıyor:

| Aile | Kaynak | Bölümler | Token katmanı |
|---|---|---|---|
| **Google resmi spec (alpha)** | google-labs-code/design.md, designmd.app, brandmd, generate-design-md, impeccable `document` | `## Overview` (Brand & Style) → `## Colors` → `## Typography` → `## Layout` (& Spacing) → `## Elevation & Depth` → `## Shapes` → `## Components` → `## Do's and Don'ts` (sıra zorunlu, bölüm atlanabilir) | YAML front matter: `name, description, colors{}, typography{fontFamily,fontSize,fontWeight,lineHeight,letterSpacing}, rounded{}, spacing{}, components{button-primary{backgroundColor,textColor,typography,rounded,padding,size,height,width}}`; `{colors.primary}` referansı; lint kontrast/broken-ref |
| **Stitch / getdesign.md "9 bölüm"** | VoltAgent awesome-design-md & awesome-claude-design, Meliwat iOS, rohitg00, design-bridge subagent | 1 Visual Theme & Atmosphere · 2 Color Palette & Roles · 3 Typography Rules · 4 Component Stylings · 5 Layout Principles · 6 Depth & Elevation · 7 Do's and Don'ts · 8 Responsive Behavior · **9 Agent Prompt Guide** (hızlı renk referansı + örnek bileşen promptları + iterasyon rehberi) | Yeni sürümlerde Google'ın YAML front matter'ı + bu 9 bölüm (Notion dosyası 300 satır YAML + 500 satır prose) |
| **typeui.sh SKILL.md + DESIGN.md çifti** | bergside/awesome-design-skills, design-md-chrome | SKILL.md: Mission · Brand · Style Foundations · Accessibility · Writing Tone · Rules Do / Don't · Guideline Authoring Workflow · Required Output Structure · Component Rule Expectations · Quality Gates; DESIGN.md: Google uyumlu kısa token dosyası | "must/should" dili, her kural bir token/eşik/örneğe bağlı |
| **oh-my-design Core v2** | kwakseongjae/oh-my-design | 7 sabit anchor: `experience` · `foundations` · `typography-assets` · `components-states` · `layout-platforms` · `content-locales` · `governance`; YAML front matter YOK, üstte araç metadata YOK; 600-1800 kelime; `<!-- design-md:section experience -->` yorum anchor'ları | Token'lar karar id'sine (`D-P2-4`) geri bağlanır; bilinmeyen değer uydurulmaz ("unknown means absent") |

### 2.1 Ortak bölümler (hepsinde tekrar eden içerik)

1. **Kimlik / Atmosfer** — tek cümlelik "stance", ruh hali kelimeleri, hedef kitle, *spesifik referans* ("1970'ler üniversite ders notu", "Golden Retriever turuncusu"). Google PHILOSOPHY: "Adjectives describe a region. A specific reference describes a point."
2. **Renk & roller** — semantik isim + hex (+ oklch/rgba) + **nerede kullanılır / nerede kullanılMAZ** + hover/pressed türevleri + dark mode; aksan bütçesi (OmD: aksan viewport'un ~%5'ini geçmez; Things 3: "mavi yalnızca seçim, birincil aksiyon, checkbox").
3. **Tipografi** — aile(ler) + rol tablosu (display/headline/title/body/label/caption: boyut, ağırlık, line-height, tracking), ölçek oranı (1.2/1.25/1.333), measure (45-75ch), Google Fonts fallback, "en fazla 2 aile" kuralı.
4. **Layout & spacing** — taban birim (4/8px), ölçek, grid (12 kolon / mobil 4 kolon), max-width, whitespace felsefesi, safe area (mobil).
5. **Depth & elevation** — shadow token tablosu (level 0-3) ya da "flat: tonal katman + hairline"; scrim.
6. **Shapes / radius** — ölçek (4/8/12/16/full) ve hangi bileşene hangisi; "concentric radius" (dış = iç + padding).
7. **Bileşenler** — buton (primary/secondary/ghost + default/hover/active/focus-visible/disabled/loading), input (label görünür, focus ring), card, nav/tab bar, badge/chip, sheet/modal; mobilde **imza bileşen** (Things'in Magic-Plus'ı, OpenTable'ın saat chip'leri).
8. **Motion** — süre (feedback 100-160ms, content 200-300ms), easing (ease-out; bounce yasak), spring parametreleri (mobil), `prefers-reduced-motion` zorunlu.
9. **Do's & Don'ts / Yasaklar** — en değerli bölüm; spesifik referans "negatif kısıtları bedavaya getirir" ama açık liste şart.
10. **Responsive / platform** — breakpoint'ler, touch target 44pt, Dynamic Type, iPad/landscape, bottom tab bar vs sidebar.
11. **Ses / ton (copy)** — Anthropic skill: aktif ses, cümle düzeni, buton = yapılacak eylem ("Save changes"), hata = ne oldu + nasıl düzelir; typeui "Writing Tone"; OmD `content-locales` (TR için sayı/tarih/para formatı buraya).
12. **Agent Prompt Guide** — hızlı renk referansı, 3-5 hazır bileşen promptu, iterasyon kuralları, **red clause** ("Would this look at home on …? If not, start over").
13. **Governance / kaynak** — hangi değerler doğrulanmış (✅⚠️❓), bilinmeyenler, değişiklik süreci (OmD, anydesign, brandmd "confidence tags").

### 2.2 Bizim şablon için taslak başlıklar (öneri)

```markdown
---
# Google spec uyumlu YAML (lint edilebilir): name, description, colors, typography, rounded, spacing, components
# + bizim uzantılar (spec izin veriyor): motion, breakpoints, platform, voice
---
# <Müşteri> Design System

## 1. Overview / Kimlik              (stance cümlesi, 3-5 trait, spesifik referans dünyası, hedef kitle, ne DEĞİL)
## 2. Colors                          (semantik token + rol + yasak kullanım + dark mode + aksan bütçesi)
## 3. Typography                      (display+body aile, rol tablosu, ölçek oranı, measure, Google Fonts fallback, TR karakter kontrolü)
## 4. Layout & Spacing                (taban birim, ölçek, grid, max-width, whitespace felsefesi, safe area)
## 5. Elevation & Depth               (shadow seviyeleri veya flat stratejisi, scrim)
## 6. Shapes                          (radius ölçeği, concentric kuralı, köşe dili)
## 7. Components                      (buton/input/card/nav/badge/sheet + state matrisi + imza bileşen)
## 8. Motion                          (süre/easing/spring, reduced-motion, "tek orkestre an")
## 9. Content & Voice                 (ton, CTA fiilleri, hata/boş durum dili, TR yerelleştirme formatları)
## 10. Platform & Responsive          (web breakpoint'leri; iOS HIG / M3 farkları; 44pt; Dynamic Type)
## 11. Do's and Don'ts                (marka-özel + evrensel anti-slop listesi; AI tell yasakları)
## 12. Agent Prompt Guide             (hızlı referans, 3-5 örnek prompt, öz-denetim checklist'i, red clause)
## 13. Governance                     (kaynak/doğrulama seviyesi, bilinmeyenler, değişiklik kuralı)
```

Sektör çeşitlemesi (restoran / klinik / saatçi) için ui-ux-pro-max'ın `products.csv` satırları iyi tohum: *Restaurant → warm (orange/red/brown) + appetizing imagery + Hero-Centric + menü/rezervasyon/konum öncelikli; Medical Clinic → Accessible & Ethical + Minimalism/Swiss, Medical Blue #0077B6 + calm green, Trust & Authority, doktor profilleri + online randevu; Dental → Fresh Blue + Smile Yellow, before/after, testimonials.* Ama taste-skill'in uyarısı geçerli: sektör klişesi de bir tell; "ilk aklına gelen paleti değil, markaya özgü olanı seç ve neden'ini yaz".

---

## 3. Kaynaklar (ayrıntı + aynen metinler)

### 3.1 google-labs-code/design.md — resmi spec, linter, felsefe

- **Link:** https://github.com/google-labs-code/design.md · spec: `docs/spec.md` · npm: `@google/design.md` · Kopya: `kopyalar/google-labs-code-design.md/`
- **Özet:** DESIGN.md = opsiyonel YAML front matter (makine okur token'lar, DTCG'den esinli) + markdown gövde (insan/ajan okur rationale). "Tokens are the normative values; the prose provides context." 8 bölüm sabit sırada. Linter 11 kural çalıştırır (`broken-ref`, `missing-primary`, `contrast-ratio` WCAG AA 4.5:1, `orphaned-tokens`, `section-order`, `unknown-key`…). `export` ile Tailwind v3 JSON, Tailwind v4 `@theme` CSS ve DTCG `tokens.json` üretir. `spec --rules` çıktısını ajan promptuna enjekte etmek resmi öneri.
- **Nasıl kullanırız:** Şablonumuzun YAML kısmını bu şemaya uydurup CI'da `npx @google/design.md lint DESIGN.md` koşarız; `export --format css-tailwind` ile Tailwind v4 temasını otomatik üretiriz. Motion/breakpoint gibi ek anahtarlar serbest ("The format grows through its users").
- **PHILOSOPHY.md'nin en önemli parçası (aynen):**

> **The quality of a generated design is determined less by the precision of its values than by how clearly the intent is described.**
>
> A design that references "A 1970s graduate lecture handout in the tradition of an old and established university" evokes a complete world: the one color of ink, the generous margins, the serif set at a reading size, and the absence of decoration. That single sentence carries more useful information than a dozen metric values. It carries the reasoning behind the values.
>
> "Modern, clean, trustworthy, premium" evokes nothing specific. A model creates something in the center of what those words describe, creating an output that is typically generic. Adjectives describe a region. A specific reference describes a point.
>
> The negative constraints arrive for free when the reference is specific enough. An intentional list of "don'ts" is useful. A long rambling list is often a sign the description was too vague to carry them.

Örnek Do's & Don'ts bloğu (PHILOSOPHY.md, "Technical Handout" tasarımı için, aynen):

```markdown
## Do's and Don'ts

- **Don't** add a hero moment to the title page. A real handout title
  page is the first page of content, not a magazine cover.
- **Don't** reach for an italic standfirst beneath a large title.
  That is the Substack register.
- **Don't** add corner ornaments, chapter marks, or abstract glyphs
  in the margins.
- **Don't** color the page numeral or any other piece of metadata.
  Vermilion lives in diagrams only.
- **Don't** use a display-class serif. One family at four modest sizes.
- **Don't** use Bold. Anywhere.
- **Don't** use sans-serif for any role other than monospace metadata.
- **Don't** introduce dark mode, gradients, glows, glass surfaces,
  drop shadows, or rounded corners.
- **Do** treat the handout as a printed object. The screen is the
  substrate; the design is the page.
- **Do** keep vermilion inside diagrams. Its scarcity outside is what
  makes its presence inside meaningful.
- **Do** trust modest size differences. The section title is only
  ~1.9× body, not 5× body.
- **Do** let pages have visible white space. A page that ends
  two-thirds of the way down is correct, not under-filled.
```

Motion bölümü örneği (spec'te olmayan ama kabul edilen uzantı, aynen):

````markdown
## Motion

```yaml
motion:
  feedback: 120ms
  content: 250ms
  easing: 'cubic-bezier(0.2, 0, 0, 1)'
```

Transitions are quick and mechanical. Nothing bounces, nothing overshoots, nothing lingers. State changes should feel like a light switch, not a door closing.

- Interactive feedback (hover, press, toggle): {motion.feedback}, always {motion.easing}.
- Content transitions (page, panel, modal): {motion.content}, same curve.
- Nothing in the UI animates longer than 300ms. If something takes longer, cut it.
- Respect `prefers-reduced-motion`: all durations collapse to 0ms.
````

#### TAM DESIGN.md ÖRNEĞİ #1 — "Paws & Paths" (köpek gezdirme servisi; Google resmi örneği)
Kaynak: https://github.com/google-labs-code/design.md/blob/main/examples/paws-and-paths/DESIGN.md (Apache-2.0). Küçük yerel hizmet işletmesi için bizim müşteri profiline en yakın resmi örnek: Material-3 tarzı tam renk rolleri + tek font ailesi + bileşen token'ları + kısa prose.

```markdown
---
name: Paws & Paths
colors:
  surface: "#f9f9ff"
  surface-dim: "#d3daea"
  surface-bright: "#f9f9ff"
  surface-container-lowest: "#ffffff"
  surface-container-low: "#f0f3ff"
  surface-container: "#e7eefe"
  surface-container-high: "#e2e8f8"
  surface-container-highest: "#dce2f3"
  on-surface: "#151c27"
  on-surface-variant: "#534434"
  inverse-surface: "#2a313d"
  inverse-on-surface: "#ebf1ff"
  outline: "#867461"
  outline-variant: "#d8c3ad"
  surface-tint: "#855300"
  primary: "#855300"
  on-primary: "#ffffff"
  primary-container: "#f59e0b"
  on-primary-container: "#613b00"
  inverse-primary: "#ffb95f"
  secondary: "#0058be"
  on-secondary: "#ffffff"
  secondary-container: "#2170e4"
  on-secondary-container: "#fefcff"
  tertiary: "#00658b"
  on-tertiary: "#ffffff"
  tertiary-container: "#1abdff"
  on-tertiary-container: "#004966"
  error: "#ba1a1a"
  on-error: "#ffffff"
  error-container: "#ffdad6"
  on-error-container: "#93000a"
  primary-fixed: "#ffddb8"
  primary-fixed-dim: "#ffb95f"
  on-primary-fixed: "#2a1700"
  on-primary-fixed-variant: "#653e00"
  secondary-fixed: "#d8e2ff"
  secondary-fixed-dim: "#adc6ff"
  on-secondary-fixed: "#001a42"
  on-secondary-fixed-variant: "#004395"
  tertiary-fixed: "#c5e7ff"
  tertiary-fixed-dim: "#7fd0ff"
  on-tertiary-fixed: "#001e2d"
  on-tertiary-fixed-variant: "#004c6a"
  background: "#f9f9ff"
  on-background: "#151c27"
  surface-variant: "#dce2f3"
typography:
  display:
    fontFamily: Plus Jakarta Sans
    fontSize: 44px
    fontWeight: "800"
    lineHeight: 52px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: "700"
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: "700"
    lineHeight: 32px
  title-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: "600"
    lineHeight: 28px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: "400"
    lineHeight: 28px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: "400"
    lineHeight: 24px
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: "600"
    lineHeight: 20px
    letterSpacing: 0.01em
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: "500"
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  xs: 4px
  sm: 12px
  md: 24px
  lg: 40px
  xl: 64px
  gutter: 16px
  margin: 24px
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    typography: "{typography.label-md}"
    rounded: "{rounded.lg}"
    padding: "{spacing.md}"
  button-primary-hover:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.on-primary-container}"
  button-secondary:
    backgroundColor: "{colors.secondary}"
    textColor: "{colors.on-secondary}"
    typography: "{typography.label-md}"
    rounded: "{rounded.lg}"
    padding: "{spacing.md}"
  button-secondary-hover:
    backgroundColor: "{colors.secondary-container}"
    textColor: "{colors.on-secondary-container}"
  card-profile:
    backgroundColor: "{colors.surface-container-lowest}"
    rounded: "{rounded.xl}"
    padding: "{spacing.md}"
  card-walk-stat:
    backgroundColor: "{colors.secondary-container}"
    textColor: "{colors.on-secondary-container}"
    rounded: "{rounded.md}"
    padding: "{spacing.sm}"
  input-field:
    backgroundColor: "{colors.surface-container-low}"
    textColor: "{colors.on-surface}"
    typography: "{typography.body-md}"
    rounded: "{rounded.DEFAULT}"
    padding: "{spacing.sm}"
  list-item-walker:
    backgroundColor: transparent
    padding: "{spacing.sm}"
    rounded: "{rounded.md}"
  list-item-walker-hover:
    backgroundColor: "{colors.surface-container-high}"
  badge-status:
    backgroundColor: "{colors.tertiary-container}"
    textColor: "{colors.on-tertiary-container}"
    typography: "{typography.label-sm}"
    rounded: "{rounded.full}"
    padding: "{spacing.xs}"
---

## Brand & Style

The design system is built to evoke the joyful energy of a walk in the park balanced with the reliability of a premium professional service. The brand personality is optimistic, trustworthy, and active.

The chosen style is **Modern Corporate** with a friendly, human-centric twist. It utilizes clean layouts and significant whitespace to reduce cognitive load for busy pet owners. The interface feels light and airy, avoiding heavy borders in favor of soft shadows and tonal shifts to create a welcoming, "best-in-class" digital environment.

## Colors

The palette centers on "Golden Retriever" orange to drive action and signal energy. This is balanced by "Sky Walk" blue, which provides a calming counterpoint for administrative tasks and scheduling.

- **Primary:** Use for main actions, active states, and highlights.
- **Secondary:** Use for secondary information, trust indicators, and navigation accents.
- **Neutral:** A range of soft grays used for backgrounds and borders to keep the UI feeling "premium."
- **Deep Charcoal:** Used for all primary text to ensure high legibility and a grounded, professional feel.

## Typography

This design system utilizes **Plus Jakarta Sans** for its friendly, rounded terminals and exceptional legibility. It maintains a contemporary look while feeling more approachable than standard geometric sans-serifs.

- **Headlines:** Bold weights are used to create a clear hierarchy and guide the eye quickly to key information.
- **Body:** Generous line heights are applied to the body text to maintain the "premium and clean" feel.
- **Labels:** Used for buttons and small metadata, utilizing a medium or semi-bold weight to remain distinct even at small scales.

## Layout & Spacing

The layout follows a **Fixed Grid** model for mobile-first consistency, utilizing a 4-column system for handheld devices.

- **Whitespace:** A "generous" philosophy is applied. Never crowd elements; use `lg` and `xl` spacing for section vertical separation to maintain a high-end aesthetic.
- **Rhythm:** Spacing is strictly based on an 8px scale.
- **Containers:** Content should be centered with a maximum width on larger screens, ensuring the "Paths" (user journeys) feel focused and intentional.

## Elevation & Depth

This design system uses **Ambient Shadows** and **Tonal Layers** to define the interface's verticality.

- **Surfaces:** Main backgrounds use the lightest neutral tint. Interactive cards sit one level above on a pure white surface.
- **Shadows:** Shadows are highly diffused and soft (Blur: 20px-40px, Opacity: 4-8%) with a subtle hint of the primary orange or secondary blue mixed into the shadow color to prevent a "dirty" gray look.
- **Interactions:** Elements should subtly lift on hover or tap, increasing shadow spread to provide tactile feedback.

## Shapes

The shape language is defined by **Rounded** corners, mirroring the soft features of a pet and making the app feel safe and friendly.

- **Buttons:** Main CTA buttons use a `12px` (rounded-lg) radius to feel substantial and clickable.
- **Cards:** Dog profiles and walker cards use a `1.5rem` (rounded-xl) radius to create a soft, containerized look.
- **Inputs:** Form fields use a `0.5rem` radius to maintain a professional yet modern appearance.
- **Icons:** Icons should feature rounded caps and corners to harmonize with the UI's structural elements.

## Components

### Buttons & Inputs

Buttons use `rounded-lg` (12px) to feel substantial and friendly, while form fields use a smaller `DEFAULT` radius to maintain structural alignment. All interactive states should utilize a subtle 150ms ease-in-out transition for background color shifts.

### Cards & Elevation

The `card-profile` is the hero container, utilizing `rounded-xl` and a tinted ambient shadow to create a "lifted" appearance against the `surface` background. Use `card-walk-stat` for high-contrast data visualization within the blue secondary palette.

### Lists & Navigation

List items should maintain a wide touch target and use `surface-container-high` for hover states to provide clear feedback without visual clutter. Use the `badge-status` for pet availability or walk progress indicators, ensuring the typography remains legible at the smaller scale.

```

### 3.2 VoltAgent/awesome-design-md + awesome-claude-design — marka DESIGN.md koleksiyonu (getdesign.md)

- **Link:** https://github.com/VoltAgent/awesome-design-md (119.6k⭐) · https://github.com/VoltAgent/awesome-claude-design · https://getdesign.md · Kopya: `kopyalar/VoltAgent-awesome-design-md/` (notion, claude, airbnb, cal, starbucks, stripe, apple) ve `kopyalar/VoltAgent-awesome-claude-design/README.md`
- **Özet:** 73 marka; her DESIGN.md 25-35KB: Google uyumlu YAML (Notion'da ~300 satır token: `primary`, `brand-navy`, `card-tint-peach`… + typography rolleri + components: `button-primary`, `pill-tab-active`, `badge-purple`) + 9 bölüm prose (Overview, Colors, Typography, Layout, Elevation & Depth, Shapes, Components, Do's and Don'ts, Responsive Behavior, Iteration Guide, Known Gaps). `AGENTS.md` = nasıl inşa edilir, `DESIGN.md` = nasıl görünür ayrımı buradan yayıldı.
- **Claude Design'ın bir DESIGN.md'den ürettiği paket (README'den):** `README.md` (brand context, voice, visual foundations) · `colors_and_type.css` (CSS değişkenleri, type scale, utility class) · Google Fonts substitutes · `preview/` kartları · çalışan UI kit (`index.html` + components) · **`SKILL.md`** (taşınabilir skill). Yani DESIGN.md → SKILL.md dönüşümü Claude Design'ın yerleşik çıktısı.
- **9 bölüm tablosu (awesome-claude-design README, aynen):**

| # | Section | What Claude reads it for |
|---|---------|--------------------------|
| 1 | Visual Theme & Atmosphere | Setting tone, density, and mood of the scaffold |
| 2 | Color Palette & Roles | Emitting CSS variables with semantic names + hex |
| 3 | Typography Rules | Building the type scale and picking Google Fonts fallbacks |
| 4 | Component Stylings | Generating buttons, inputs, cards, nav with states |
| 5 | Layout Principles | Spacing scale, grid, whitespace rhythm |
| 6 | Depth & Elevation | Shadow tokens and surface hierarchy |
| 7 | Do's and Don'ts | Guardrails Claude respects when generating new screens |
| 8 | Responsive Behavior | Breakpoints, touch targets, collapse behavior |
| 9 | Agent Prompt Guide | Reusable prompts Claude embeds into the generated `SKILL.md` |

- **Nasıl kullanırız:** (a) Müşteri "X gibi olsun" derse ilgili DESIGN.md'yi *ilham* olarak oku, değerleri kopyalama (README'deki marka uyarısı); (b) şablonumuzun YAML derinliği için Notion/Claude dosyaları referans; (c) `npx getdesign@latest add <slug>` CLI'ı var.
- **Notion DESIGN.md'den YAML başı (aynen, ilk satırlar):**

```yaml
---
version: alpha
name: Notion-design-analysis
description: Notion presents itself as the all-in-one workspace through a confident, illustration-rich brand voice — anchored by a deep navy hero band ({colors.brand-navy}) decorated with brand-colored sticky-note dots and mesh wire illustrations, a signature purple pill primary CTA ({colors.primary}), and a rich palette of pastel-tinted feature cards ...
colors:
  primary: "#5645d4"
  primary-pressed: "#4534b3"
  primary-deep: "#3a2a99"
  on-primary: "#ffffff"
  brand-navy: "#0a1530"
  ...
  canvas: "#ffffff"
  surface: "#f6f5f4"
  hairline: "#e5e3df"
  ink: "#1a1a1a"
  charcoal: "#37352f"
```

```yaml
  pill-tab-active:
    backgroundColor: "{colors.ink-deep}"
    textColor: "{colors.on-dark}"
    rounded: "{rounded.full}"
    border: "1px solid {colors.ink-deep}"
  text-input-focused:
    backgroundColor: "{colors.canvas}"
    textColor: "{colors.ink}"
    border: "2px solid {colors.primary}"
```

### 3.3 rohitg00/awesome-claude-design — estetik aileler + Anti-Slop Kit

- **Link:** https://github.com/rohitg00/awesome-claude-design (1.1k⭐, MIT) · Kopya: `kopyalar/rohitg00-awesome-claude-design/` (README, prompts/*, design-md/warm/claude.md, editorial/linear.md, recipes)
- **Özet:** DESIGN.md'leri 9 estetik aileye ayırıyor (editorial minimalism, terminal-core, warm editorial, data-dense pro, cinematic dark, playful color, glass/soft-futurism, neon brutalist, indie). Kısa (3-4KB) ve CSS-değişkeni odaklı dosyalar; her biri "Agent Prompt Guide" ve **red clause** ile bitiyor. Ayrıca Claude Design'ın varsayılan parmak izleri tablosu ve bunları nötrleyen `break-default-aesthetic` promptu.
- **Claude Design'ın varsayılan parmak izleri (README, aynen):**

| Fingerprint | What it looks like | Counter-rule |
|---|---|---|
| **Teal accent everywhere** | The default `#16d5e6`-adjacent action color appears on CTA, headline accent, focus rings, and chart fill | Pick a brand-specific accent in your DESIGN.md before the first generation |
| **Blinking status dot** | Animated green/lime dot top-right of nav, signals "live"/"AI" by reflex | Reject in your prompt: "no animated status indicators" |
| **Container soup** | Pills wrapping cards wrapping cards wrapping content; padding stacking 24/24/24 | Cap nesting depth: "containers nest at most 2 levels" |
| **Default serif headline** | Tiempos- or Source-Serif-adjacent serif paired with sans body — reads like the Anthropic brand's leftovers | Specify font stack with explicit weight + tracking, not a vibe |
| **Accent bar left of every card** | 4px coloured rule on every card, regardless of semantic meaning | Reserve left-rule for one role (e.g. severity) — never as decoration |
| **Three-column feature grid in hero** | Almost every landing the model produces has the same section-2 layout | Brief: "no three-column feature grid; choose marquee, alternating-row, or single-column instead" |
| **Lucide icon stack** | Default icon set across nav, buttons, empty states | Either commit to a single icon family (Phosphor / Heroicons / custom) or ship type-only |
| **Generative hero in product palette ignored** | Image generator picks colors that "look right" but ignore the DESIGN.md tokens | Constrain the image: "regenerate hero using only `--bg`, `--accent`, `--text`" |

- **Anthropic frontend-aesthetics cookbook'tan alıntılanan Anti-Slop fragmanı (aynen):**

```
NEVER use generic AI-generated aesthetics:
- Overused font families (Inter, Roboto, Arial, system fonts)
- Cliched color schemes (purple gradients on white or dark backgrounds)
- Predictable layouts and component patterns
- Cookie-cutter design that lacks context-specific character

DO use:
- Unique fonts chosen for the brand, not defaults
- Cohesive colors and themes grounded in the product's story
- Animations for effects and micro-interactions
- Context-specific character in every component
```

#### TAM DESIGN.md ÖRNEĞİ #2 — "Claude (Anthropic) — Warm Editorial" (kısa, CSS-değişkenli format)
Kaynak: https://github.com/rohitg00/awesome-claude-design/blob/main/design-md/warm/claude.md (MIT). Kısa formatın en iyi örneği: 9 bölüm, CSS değişkenleri, net yasaklar, red clause. Bizim "hızlı DESIGN.md" seviyemiz bu olmalı.

```markdown
# Claude (Anthropic) — Warm Editorial

Reference DESIGN.md for a terracotta-on-cream editorial aesthetic. Drop into Claude Design → scaffolds the full system.

## 1. Visual Theme & Atmosphere

Warm editorial. Human, considered, slightly literary. Not tech-startup sterile, not luxury-brand cold. Feels like a well-designed magazine spread with a craft espresso on the side. Generous whitespace, slow rhythm, long-form reading is first-class.

Mood words: calm, earnest, tactile, deliberate, patient.

## 2. Color Palette & Roles

```
--bg-primary:      #f4f3ee   /* cream, page */
--bg-secondary:    #eeede6   /* subtle surface lift */
--bg-inverse:      #191817   /* near-black, inverse sections */
--text-primary:    #191817   /* ink */
--text-secondary:  #5a554e   /* warm gray */
--text-muted:      #8a847a
--accent:          #c96442   /* terracotta */
--accent-hover:    #b55738
--accent-soft:     #e89268   /* callouts, illustrations */
--border:          #d8d3c8
--success:         #6b7a3d
--warning:         #c98a42
--danger:          #a53e2a
```

Rules: one accent per viewport. Terracotta never competes with itself. Never tint body text with accent.

## 3. Typography Rules

- **Display / headlines:** serif. `Tiempos Headline`, `Iowan Old Style`, fallback `Georgia`. Weight 500. Letter-spacing −1.5% at large sizes.
- **Body:** humanist sans. `Styrene A`, fallback `Inter`. Weight 400. Line-height 1.6. Measure 65–72 characters.
- **UI / labels:** same humanist sans, weight 500, letter-spacing +2%, uppercase only for eyebrow labels.
- **Code / mono:** `GT America Mono`, fallback `JetBrains Mono`, `SF Mono`.

Type scale (modular 1.25): 13 / 15 / 17 / 21 / 26 / 32 / 40 / 50 / 62.

Headlines balance (`text-wrap: balance`), body reflow (`text-wrap: pretty`).

## 4. Component Stylings

**Buttons**
- Primary: terracotta fill, cream text, radius 6px, padding 10/18, weight 500. Hover: darker terracotta, no lift.
- Secondary: 1px `--border`, ink text, transparent fill. Hover: `--bg-secondary`.
- Ghost: ink text, underline on hover.

**Cards**
- Background `--bg-secondary`, no border, no shadow. Radius 8px. Padding 24.
- Hover: subtle `--border` appears. No transform, no scale.

**Inputs**
- 1px `--border`, radius 6px, padding 10/14. Focus: 2px `--accent` outline with 2px offset. No box-shadow glows.

**Navigation**
- Horizontal, underline-on-hover, accent underline on active. No pill backgrounds.

**Tables**
- Zebra rows on `--bg-secondary`. Header in mono, uppercase, tracked wide.

## 5. Layout Principles

- Max content width 680px for long-form, 1180px for app shells.
- 12-column grid, 24px gutters.
- Vertical rhythm: baseline 8px, heading top-margin 2× line-height, section break = 96px.
- Asymmetry is allowed and encouraged for editorial pages (pull-quotes float, sidenotes in margin).

## 6. Depth & Elevation

Flat by default. No drop shadows. Depth comes from:
- Surface color shifts (`--bg-secondary` over `--bg-primary`)
- 1px `--border` lines
- Type weight contrast

Exception: modals use a single `0 8px 24px rgba(25, 24, 23, 0.08)` shadow.

## 7. Do's and Don'ts

**Do**
- Let whitespace do the heavy lifting.
- Use serif for long-form headlines, sans for UI.
- Lean on typographic hierarchy before color.
- Write microcopy in full sentences with periods.

**Don't**
- Use purple gradients. Ever.
- Apply Inter to every surface by default.
- Animate hover states with scale or lift.
- Mix more than two fonts per page.
- Use emojis in UI chrome.

## 8. Responsive Behavior

- Single-column mobile, no hamburger-only nav — show 2–3 primary links.
- Serif headline size scales 62 → 36 on mobile.
- Tables become stacked cards below 640px.
- Long-form retains 18px body on mobile (not 16).

## 9. Agent Prompt Guide

Bias toward:
- Serif headlines, humanist sans body.
- One terracotta moment per viewport.
- Flat surfaces, typographic depth.
- Long lines broken by pull-quotes and sidenotes.

Reject:
- Purple-to-pink gradients, glassmorphism, neon glows.
- Card-heavy layouts where every element is a rounded box.
- Generic hero + 3-column features + CTA template.

When generating, ask: "Would this look at home on the Anthropic blog or in a New Yorker feature?" If not, start over.

```

#### `break-default-aesthetic` promptu (aynen) — DESIGN.md'nin başına / sistem promptuna yapıştırılacak
Kaynak: https://github.com/rohitg00/awesome-claude-design/blob/main/prompts/break-default-aesthetic.md

```
Before you generate anything, internalize the following constraints.
These exist because Claude Design has observable default patterns
that I do not want in my output.

EXPLICIT REJECTIONS — do not produce any of these unless I ask:

1. ACCENT COLOR
   - Do not default to teal (#16d5e6 or near). My accent is defined
     in DESIGN.md and must be the only chromatic action color.
   - Do not introduce a second accent for variety. One accent only.

2. STATUS INDICATORS
   - No animated dots, blinking lights, "live" badges, or pulsing
     orbs in nav, header, or hero.
   - Status surfaces use static glyphs and explicit text.

3. CONTAINER NESTING
   - Maximum container depth: 2 (e.g., section > card, not
     section > card > pill > tag > content).
   - No card-on-card decoration. If something needs visual separation,
     use border or tonal shift, not another wrapper.

4. TYPOGRAPHY
   - No serif headline + sans body unless DESIGN.md specifies it.
   - No Inter, Roboto, Arial, system-ui, "system stack" fallback as
     the chosen primary face.
   - Headline weight, tracking, and optical-size all stated in tokens.

5. LAYOUT CLICHES
   - No three-column feature grid in hero / section 2.
   - No identical-aspect card grids when the data is heterogeneous.
   - No "Get Started" / "Learn More" CTA pair as the default duo.

6. DECORATIVE LEFT RULES
   - The 4px coloured left-rule on every card is reserved for ONE
     semantic role (e.g., severity, status). Never decorative.

7. ICON FAMILY
   - Pick exactly one icon family per project: Phosphor, Heroicons,
     Tabler, custom. Do not mix.
   - If no family is provided, prefer type-only solutions over
     defaulting to Lucide.

8. GENERATIVE IMAGES
   - Hero illustrations and generative stills must use only colors
     declared in DESIGN.md (--bg, --surface, --accent, --text).
   - No purple-pink gradient on dark backgrounds.
   - No glass / frosted card stack as hero composition.

9. MOTION
   - Motion serves a purpose: communicate state, hierarchy, or
     spatial relationship. Decorative motion (floating particles,
     bobbing icons, scroll-tied parallax) requires explicit request.
   - Respect prefers-reduced-motion at all times.

10. COPY
    - Microcopy is product-specific. No "Welcome to {Product}" hero,
      no "Built for teams" subtitle, no "Get Started" CTA without a
      verb that names the action.

POSITIVE BIAS — when in doubt, prefer:

- Distinctive type choices that suit the brand voice
- One bold aesthetic direction over hedged "modern minimal"
- Border-based depth over drop shadows
- Editorial rhythm (max-width, vertical breathing) over uniform grids
- Content-first hero (type, table, demo) over decorative hero
- Real product surfaces (settings, empty states, edge cases) over
  marketing hero alone

VERIFICATION — after generating, audit your own output:

- Did I use teal as accent? (FAIL)
- Is there an animated status dot? (FAIL)
- Are there 3+ levels of nested containers anywhere? (FAIL)
- Did I use Inter / Roboto / Arial as primary face? (FAIL)
- Is there a three-column feature grid in section 2? (FAIL)
- Are there decorative coloured left-rules on more than one role? (FAIL)
- Is the hero illustration outside the DESIGN.md palette? (FAIL)

If any FAIL, regenerate the offending region before showing it to me.
```

`brand-to-design-md` promptu (URL → 9 bölümlü DESIGN.md; aynen, çekirdek kısım):

```
You are a senior design systems architect. Analyze the brand at {URL}
and produce a DESIGN.md file that captures its visual language for
coding agents to replicate.

Inspect:
1. Computed CSS of body, h1, primary button, primary link
2. Hero section tokens (color, type, spacing)
3. Font stack and licensing notes
4. Distinctive visual signatures (radii, shadows, motion, grid)

Output MUST include all 9 canonical sections:
- Visual Theme & Atmosphere
- Color Palette & Roles (CSS variables)
- Typography Rules (scale + stack)
- Component Stylings (button, card, input, nav)
- Layout Principles (grid, max-width, spacing scale)
- Depth & Elevation
- Do's and Don'ts
- Responsive Behavior
- Agent Prompt Guide

Rules:
- Only describe what you can verify from rendered output.
- Never hallucinate typography names — mark "unknown" if unclear.
- Color values as hex with role names, not brand names.
- Keep each section under 150 words.
```

### 3.4 Meliwat/awesome-ios-design-md — mobil DESIGN.md (nötr + SwiftUI + Expo + Compose)

- **Link:** https://github.com/Meliwat/awesome-ios-design-md (571⭐, MIT) · galeri: https://www.spectr.to/gallery · Kopya: `kopyalar/Meliwat-awesome-ios-design-md/` (opentable 4 lezzet, starbucks, things-3, oura)
- **Özet:** 200 mobil uygulama, 11 kategori (food: OpenTable, Resy, Starbucks, Deliveroo…; fitness: Oura; productivity: Things 3…). Her paket: `DESIGN.md` (framework-nötr, Stitch 9 bölümü iOS'a uyarlanmış: SF Pro Dynamic Type hiyerarşisi, 4/8pt grid, safe area, `.regularMaterial`, haptik, spring eğrileri, cihaz tablosu) + `DESIGN-swiftui.md` (Color/Font extension, ViewModifier) + `DESIGN-expo.md` (theme/colors.ts, typography.ts, Reanimated + expo-haptics) + `DESIGN-android.md` (Material 3 Typography, Compose). README her paketi tek paragrafta özetliyor ("OpenTable Red `#DA3743`… 10pt time-slot chips… sticky red Reserve bar").
- **Nasıl kullanırız:** Expo projelerinde `DESIGN-expo.md` formatını (token modülü + bileşen snippet'leri) kendi mobil şablonumuzun "implementation companion" dosyası olarak kopyalarız. Restoran müşterisi için OpenTable/Resy/Starbucks paketleri hazır ilham; klinik için Oura'nın (sağlık verisi, sakin koyu tema) yapısı referans.
- **DESIGN-expo.md'nin token modülü örneği (OpenTable, aynen başı):**

```ts
// theme/colors.ts
export const colors = {
  // Surfaces (light)
  canvas:         '#FFFFFF',
  surfaceGray:    '#F6F6F7',
  surfaceRaised:  '#FBFBFC',
  surfacePressed: '#ECECEE',
  divider:        '#E4E4E7',
  // Surfaces (dark)
  darkCanvas:     '#121212',
  darkSurface1:   '#1C1C1E',
  // Text
  textPrimary:    '#1A1A1C',
  textSecondary:  '#6B6B70',
  textTertiary:   '#9A9A9F',
  // Brand
  red:        '#DA3743',
  redBright:  '#F2545B',
  redPressed: '#B92C37',
  redTint:    '#FCEBEC',
  // Functional accents
  goldStar:   '#E8A33D',
  pointsTeal: '#1F8A8A',
  dinerGreen: '#2FA86A',
} as const;
```

#### TAM DESIGN.md ÖRNEĞİ #3 — Things 3 (iOS), framework-nötr mobil format
Kaynak: https://github.com/Meliwat/awesome-ios-design-md/blob/main/design-md/productivity/things-3/DESIGN.md (MIT). Mobil şablonumuzun iskeleti: 9 bölüm + imza bileşenler + motion (spring response/damping) + Dynamic Type + cihaz tablosu + Agent Prompt Guide. Not: tüm metin bir "ilham/analiz" dosyasıdır, resmi Cultured Code belgesi değildir.

```markdown
# Design System Inspiration of Things 3 (iOS)

## 1. Visual Theme & Atmosphere

Things 3's iOS app is a study in calm. Where most task managers are dense, colorful dashboards, Things is a quiet white room: a pure white canvas (`#FFFFFF`), a single restrained blue accent, and acres of generous whitespace that let a to-do list feel like a calm sheet of paper rather than an anxiety machine. The design philosophy is "serene productivity" — the interface should disappear so that the user's own intentions are the only thing on screen. Surfaces lift only barely (`#F5F6F8`), dividers are whisper-thin (`#ECECEC`), and there is essentially no chrome: no toolbars crowding the top, no badges screaming for attention. The app feels expensive precisely because it withholds.

The accent is Things Blue `#4F97FF` — a soft, friendly blue used with monastic discipline for exactly three things: the active selection, the primary action, and the checkbox fill on completion. The one other color the app allows is the Today-yellow `#FFD60A` star, the single warm note in an otherwise cool, neutral palette — it marks the "Today" list, the heart of the app, and the star a task earns when scheduled for today. Everything else is ink-black text, soft gray metadata, and white. The restraint is the brand: when something turns blue, it is the one thing you should look at; when the yellow star appears, it means *today*.

Typography is San Francisco (SF Pro) — Apple's system face — used at 400 / 600 / 700 across a 13–28pt range, closely approximated by Inter where SF is unavailable. The hierarchy is built for reading a list like prose: list titles at 28pt bold (the "Today" headline feels like a chapter heading), task titles at 17pt regular, notes at 15pt regular gray. Headings *within* a project are a signature device — a bold 17pt heading with a hairline divider that visually chunks a long project into calm sections. The most expressive typographic moment is simply the generous line height and the air around each task; type here is about breathing room, not density.

**Key Characteristics:**
- Pure white canvas (`#FFFFFF`) with serene, generous whitespace — the calm is the product
- Things Blue (`#4F97FF`) as the single accent — selection, primary action, checkbox fill
- Today-yellow (`#FFD60A`) star — the one warm note, marking the "Today" list and scheduled tasks
- Circular checkbox with a satisfying fill-and-check spring animation on completion
- Magic-Plus button — a floating blue "+" that can be dragged to insert a to-do exactly where you want
- Project pie-progress — a small circular ring that fills as a project's to-dos are completed
- Headings within projects — bold section labels with hairline dividers that chunk long lists calmly
- Sidebar / list navigation (no bottom tab bar) — Inbox, Today, Upcoming, Anytime, Someday, Projects

## 2. Color Palette & Roles

### Primary
- **Things Blue** (`#4F97FF`): Active selection, primary action, the Magic-Plus button, checkbox fill on completion, links.
- **Blue Pressed** (`#3D7FE0`): Active/pressed state for blue controls.
- **Blue Tint** (`rgba(79,151,255,0.10)`): Selected-row wash, subtle highlight behind the active item.

### Today
- **Today Yellow** (`#FFD60A`): The "Today" list star, the star badge on a task scheduled for today — the single warm accent.
- **Yellow Pressed** (`#E6BE00`): Pressed state for the Today star control.

### Canvas & Surface (Light — primary)
- **Canvas** (`#FFFFFF`): Pure white app background — the serene room.
- **Surface 1** (`#F5F6F8`): Grouped section backgrounds, search field, the barely-there lift.
- **Surface 2** (`#ECECEC`): Pressed rows, segmented controls.
- **Divider** (`#ECECEC`): Whisper-thin hairline dividers between groups and under headings.
- **Text Primary** (`#1D1D1F`): Task titles, list headlines, primary text — near-black ink, not pure black.
- **Text Secondary** (`#8A8A8E`): Notes, dates, metadata, project subtitles.
- **Text Tertiary** (`#C7C7CC`): Disabled, placeholder, completed-task strike text, very low emphasis.

### Semantic
- **Checkbox Border** (`#C7C7CC`): The unchecked circular checkbox outline.
- **Completed Check** (`#4F97FF`): The filled checkbox + white checkmark on completion.
- **Deadline Red** (`#FF3B30`): A task past its deadline; the deadline flag when overdue.
- **Today Star** (`#FFD60A`): Scheduled-for-today indicator (shared with Today yellow).
- **Tag Pill** (`#8A8A8E` text on `#F5F6F8`): Neutral tag chips — tags are not color-coded by default.
- **Area Accent**: Areas use a subtle neutral; color is structure, not decoration.

### Dark Mode
Things ships a true dark mode for users who prefer it; the primary identity is the white room.
- **Dark Canvas** (`#1C1C1E`)
- **Dark Surface** (`#2C2C2E`)
- **Dark Divider** (`#38383A`)
- **Dark Text Primary** (`#F2F2F7`)
- **Dark Text Secondary** (`#98989F`)
- Things Blue and Today yellow stay identical — they read well on both.

## 3. Typography Rules

### Font Family
- **Primary**: `SF Pro` (Apple system) — Text and Display optical variants, used at 400 / 600 / 700
- **Fallback Stack**: `-apple-system, BlinkMacSystemFont, 'Inter', 'SF Pro Text', 'Segoe UI', Roboto, Helvetica, Arial, sans-serif`
- **Optical sizing**: SF Pro Display at 20pt+ (titles), SF Pro Text below 20pt (rows, notes)

### Hierarchy

| Role | Font | Size | Weight | Line Height | Letter Spacing | Notes |
|------|------|------|--------|-------------|----------------|-------|
| List Title (Large) | SF Pro Display | 28pt | 700 | 1.2 | 0.3pt | "Today", "Upcoming" — feels like a chapter title |
| Project Title | SF Pro Display | 24pt | 700 | 1.2 | 0.3pt | Project detail screen hero title |
| Section Heading | SF Pro Text | 17pt | 700 | 1.3 | -0.2pt | A heading inside a project (with hairline divider) |
| Task Title | SF Pro Text | 17pt | 400 | 1.35 | -0.2pt | A to-do row's primary text |
| Task Title (done) | SF Pro Text | 17pt | 400 | 1.35 | -0.2pt | Completed — color tertiary, strikethrough |
| Body / Notes | SF Pro Text | 15pt | 400 | 1.45 | -0.1pt | To-do notes, descriptions |
| List Row Subtitle | SF Pro Text | 14pt | 400 | 1.35 | -0.1pt | Project name under a to-do, area subtitles |
| Metadata | SF Pro Text | 13pt | 400 | 1.3 | 0pt | Date, "3 to-dos", tag text |
| Button (Primary) | SF Pro Text | 17pt | 600 | 1.0 | -0.2pt | "Add to-do", confirm actions |
| Sidebar Item | SF Pro Text | 16pt | 400 | 1.3 | -0.1pt | "Inbox", "Today" sidebar rows (active = 600) |
| Date Pill | SF Pro Text | 13pt | 600 | 1.0 | 0pt | "Today", "Tomorrow" scheduled-date chip |
| Tag Pill | SF Pro Text | 13pt | 400 | 1.0 | 0pt | Tag chip text |
| Count / Meta | SF Pro Text | 15pt | 400 | 1.0 | 0pt | Sidebar count badge, tabular |
| Tiny Label (UPPER) | SF Pro Text | 12pt | 700 | 1.2 | 0.5pt | "THIS EVENING", small caps section labels |

### Principles
- **Weights concentrated at 400 / 600 / 700**: Regular for tasks and notes, semibold for buttons and active sidebar, bold for titles and headings — no light, no black
- **Titles breathe**: List titles at 28pt with positive 0.3pt tracking feel like calm chapter headings, not UI labels
- **Tasks read like prose**: 17pt regular with 1.35 line height and lots of vertical air — this is a reading surface
- **Headings chunk, dividers separate**: A bold 17pt heading + a hairline divider is how a long project stays serene
- **Completed = quiet, not gone**: Done tasks dim to tertiary with a strikethrough — present but receded

## 4. Component Stylings

### Buttons

**Magic-Plus Button (the signature control)**
- Shape: Circle, 56pt diameter, floating bottom-right (or bottom-center) above content
- Background: `#4F97FF`
- Icon: a `plus` glyph, 24pt, `#FFFFFF`
- Shadow: `rgba(79,151,255,0.35) 0 8px 20px` — a soft blue glow
- Pressed: scale 0.94, background `#3D7FE0`, medium haptic
- Drag behavior: press-and-drag lifts the button; a blue insertion line follows the finger so a new to-do drops exactly where released

**Primary Text Button ("Add", confirm)**
- Background: transparent (text-only) or `#4F97FF` filled for modal confirms
- Text: `#4F97FF` (text) / `#FFFFFF` (filled), SF Pro Text 17pt weight 600
- Filled padding: 12pt vertical, 24pt horizontal; corner radius 10pt
- Pressed: text `#3D7FE0` / filled `#3D7FE0`, no scale on text buttons

**Icon Button (toolbar — search, more, calendar)**
- Size: 22pt glyph, 44pt hit target
- Default: `#8A8A8E`
- Active/pressed: `#4F97FF`
- No background; the generous hit area is the affordance

**Today Star Toggle**
- A `star` glyph, 18pt
- Off: outline `#C7C7CC`; On: filled `#FFD60A`
- Tapping animates a quick fill + 1.0 → 1.15 → 1.0 bounce

### Cards & Containers

**To-Do Row (the unit)**
- Min height: 44pt (expands with notes/subtitle)
- Layout: 22pt circular checkbox (leading) → task title (17pt w400) → optional subtitle (14pt) / tag pills / date pill (trailing)
- Background: transparent (canvas `#FFFFFF`)
- Selected: background `rgba(79,151,255,0.10)`, corner radius 8pt, title color unchanged
- Swipe actions: swipe-right to schedule (blue), swipe-left for "when"/move
- Completed: checkbox fills `#4F97FF` with white check; title goes `#C7C7CC` strikethrough; row fades and drops out of the list after a beat

**Circular Checkbox**
- Diameter: 22pt; 1.5pt border `#C7C7CC` when unchecked
- Checked: fills `#4F97FF`, a white `checkmark` scales in with a spring (the signature micro-interaction)
- Tapping anywhere on the checkbox or its 44pt hit area toggles completion with a soft haptic

**Section Heading (inside a project)**
- A bold 17pt `#1D1D1F` label, 24pt top padding, followed by a hairline `#ECECEC` divider
- Tap to collapse/expand the heading's to-dos; a small chevron rotates
- The signature device that keeps long projects calm and scannable

**Project Row (in a list)**
- 56pt row: a pie-progress ring (leading) → project name (17pt w400) → area / count subtitle (14pt secondary)
- The ring fills proportionally to completed to-dos; a fully-complete project shows a solid blue ring with a check

**List Title Header**
- The big 28pt w700 list name ("Today", "Upcoming") with generous 24pt top / 8pt bottom padding
- For "Today", a `#FFD60A` star sits beside the title; for "Upcoming", a small calendar glyph
- No background, no divider — the whitespace is the separation

**Quick-Find / Search**
- A `#F5F6F8` rounded field, 10pt radius, 40pt tall
- Leading `magnifyingglass` 16pt `#8A8A8E`; placeholder "Quick Find" 16pt w400 `#8A8A8E`
- Focus: subtle, no heavy border; caret `#4F97FF`

### Navigation

**Sidebar (no bottom tab bar)**
- A slide-over / left panel listing: Inbox, Today (★), Upcoming, Anytime, Someday, Logbook, then Areas & Projects
- Item height: 40pt; icon 18pt + label 16pt w400
- Active item: background `rgba(79,151,255,0.10)`, label 16pt w600, icon `#4F97FF`; trailing count in 15pt `#8A8A8E`
- Inactive: label `#1D1D1F`, icon `#8A8A8E`
- Opened via the top-left back/menu affordance or an edge swipe; canvas dims `rgba(0,0,0,0.2)` behind

**Top Bar**
- Height: 44pt + safe area
- Leading: back / sidebar affordance ; Trailing: search + more (22pt `#8A8A8E`)
- No center title (the big 28pt list title lives in the scroll content, not the bar)
- Fully transparent over white; on scroll the list title shrinks toward the bar gently

### Input Fields

**To-Do Title Field (composer)**
- Appears inline as a new row or in a card sheet
- Text: 17pt w400 `#1D1D1F`; placeholder "New To-Do" 17pt w400 `#C7C7CC`
- No border, no box — it is just text on white; the checkbox sits to its left
- Below: a notes line (15pt), then a row of quick controls (When, tag, deadline, checklist)

**When / Schedule Picker**
- A calm sheet: "Today" (★ yellow), "This Evening", "Tomorrow", "Upcoming" (calendar), "Someday", "Clear"
- Each row a 44pt tappable item with a leading glyph; the chosen option gets a `#4F97FF` check

**Tag Field**
- Tags render as neutral pills (`#F5F6F8` background, 13pt `#8A8A8E` text, full-pill radius)
- Add-tag opens a calm list of existing tags with a search field

### Distinctive Components

**Magic-Plus**
- The signature creation gesture: a floating 56pt blue "+" that you can tap to add a to-do at the top, or press-and-drag to drop a new to-do precisely between existing rows (a blue insertion line tracks the finger)
- On release it morphs into an inline editable to-do row with the keyboard rising

**Circular Checkbox Fill Animation**
- On completion the 22pt circle fills with `#4F97FF` from center outward over ~180ms, and a white checkmark draws/scales in with a spring (response 0.3, damping 0.6)
- A soft haptic fires on toggle; the completed row then gently fades + collapses out of the list

**Project Pie-Progress**
- A small (18–22pt) circular ring, `#ECECEC` track + `#4F97FF` fill arc proportional to completed to-dos
- It animates the arc forward whenever a to-do in the project is completed — a quiet sense of momentum

**Headings + Hairline Dividers**
- Bold 17pt headings with a thin `#ECECEC` rule that segment a long project into serene chunks
- Collapsible: tapping a heading folds its to-dos with a 200ms height ease

**This Evening Section**
- Within "Today", a calm "This Evening" sub-section (12pt w700 UPPER label + divider) separates evening tasks — a uniquely Things organizing device for the daily rhythm

## 5. Layout Principles

### Spacing System
- Base unit: 4pt
- Scale: 4, 8, 12, 16, 20, 24, 32, 44, 56
- Standard margin: 20pt horizontal (more generous than typical 16pt — part of the calm)
- To-do rows: 12pt vertical breathing inside the 44pt min height; 0pt between rows (whitespace + dividers carry it)

### Grid & Container
- Content width: full device width with generous 20pt horizontal margins
- Single column lists; iPad uses sidebar + list (+ optional project detail) panes
- The Magic-Plus floats above the list, bottom-trailing, clearing the safe area

### Whitespace Philosophy
- **Whitespace IS the product**: Things deliberately uses more air than competitors — generous top padding on list titles, roomy rows, wide margins. The calm is the feature.
- **Restraint over information density**: It would be easy to cram more metadata into a row; Things chooses not to. A to-do is mostly its title and a lot of quiet space.
- **Headings create rhythm**: Long projects stay serene because bold headings + hairline dividers chunk them into digestible sections rather than one dense wall.

### Border Radius Scale
- Sharp (0pt): Hairline dividers
- Soft (8pt): Selected-row highlight, segmented controls
- Standard (10pt): Search field, filled buttons, sheets' inner cards
- Comfortable (14pt): Bottom sheets (When picker, tag picker)
- Circle (50%): Checkbox, Magic-Plus button, pie-progress ring

## 6. Depth & Elevation

| Level | Treatment | Use |
|-------|-----------|-----|
| Flat (Level 0) | No shadow | To-do rows, list, canvas — the white room is flat |
| Selection (Level 1) | Background tint only (`rgba(79,151,255,0.10)`), no shadow | The selected to-do row |
| Magic-Plus (Level 2) | `rgba(79,151,255,0.35) 0 8px 20px` (soft blue glow) | The floating + button |
| Sheet (Level 3) | `rgba(0,0,0,0.12) 0 -8px 32px` | When picker, tag picker, modal cards |
| Scrim | `rgba(0,0,0,0.2)` (gentle — Things keeps even dimming soft) | Behind the sidebar and sheets |

**Shadow Philosophy**: Things is almost entirely flat — the white room has no card stacks, no drop shadows on rows, no skeuomorphic depth. The single intentional shadow is the soft blue glow under the Magic-Plus button, which signals "this is the live creation tool." Sheets get a gentle, low-opacity shadow as they rise. Even the modal scrim is unusually light (0.2) because Things never wants to feel heavy or alarming. Elevation is communicated by whitespace and a barely-there tint, not by shadow.

### Motion
- **Checkbox completion**: circle fills center-out ~180ms + white check spring (response 0.3, damping 0.6) + soft haptic; the row then fades + collapses out over 250ms
- **Magic-Plus tap**: scale 0.94 → 1.0 spring (response 0.25, damping 0.7) + `.impactOccurred(.medium)`
- **Magic-Plus drag**: the button lifts (scale 1.05) and a blue insertion line eases to the nearest gap as the finger moves; release drops the new row in with a 200ms settle
- **Heading collapse/expand**: child to-dos animate height over 200ms ease-in-out; chevron rotates
- **Pie-progress**: the fill arc animates forward over 300ms when a project to-do completes
- **Sidebar slide-over**: translateX spring 240ms (response 0.35, damping 0.9) with a soft scrim fade
- **Schedule swipe**: the row reveals a blue action with a rubber-band spring; commit triggers a light haptic

## 7. Do's and Don'ts

### Do
- Use a pure white canvas (`#FFFFFF`) and protect the generous whitespace — the calm is the product
- Reserve Things Blue (`#4F97FF`) for selection, primary action, and the checkbox fill
- Use the Today-yellow (`#FFD60A`) star only for the Today list and scheduled-today markers
- Animate the circular checkbox fill + check spring on completion — it is the signature delight
- Provide the Magic-Plus button with press-and-drag insertion (blue line tracks the finger)
- Chunk long projects with bold 17pt headings and hairline dividers
- Keep list titles big (28pt w700) with roomy top padding so they read like chapter headings
- Show project progress as a quiet circular pie ring that animates forward on completion
- Keep the app flat — the only shadow is the soft blue glow under Magic-Plus

### Don't
- Don't crowd the canvas — resist adding metadata, badges, or chrome that breaks the calm
- Don't introduce extra accent colors — blue for action, the single yellow star for "today"
- Don't color-code tags or projects by default — color is structure here, not decoration
- Don't make completion abrupt — the fill + check spring (and gentle fade-out) is the moment
- Don't add card shadows or skeuomorphic depth — the white room is flat
- Don't shrink the whitespace to fit more rows — density is explicitly not the goal
- Don't use a bottom tab bar — navigation is the sidebar (Inbox / Today / Upcoming / …)
- Don't use heavy modal scrims — even dimming stays soft (≈0.2)
- Don't use light font weights — SF Pro here is 400/600/700 only

## 8. Responsive Behavior

### Device Sizes
| Device | Width | Key Changes |
|--------|-------|-------------|
| iPhone SE (3rd gen) | 375pt | 20pt margins kept; subtitle may truncate before tags |
| iPhone 13/14/15 | 390pt | Standard roomy rows, full 28pt title |
| iPhone 15/16 Pro | 393pt | Dynamic Island clears the transparent top bar |
| iPhone 15/16 Pro Max | 430pt | Slightly larger title (30pt), more notes preview |
| iPad | 768pt+ | Sidebar + list (+ project detail) panes; Magic-Plus per pane |

### Dynamic Type
- List titles, task titles, notes: scale fully with Dynamic Type (this is a reading app — honor it)
- Checkbox: fixed 22pt (interaction target, not text)
- Sidebar count badges: tabular, scale modestly
- Tiny section labels ("THIS EVENING"): scale but clamp so the divider rhythm holds

### Orientation
- All lists: **portrait and landscape**
- iPad: landscape unlocks the persistent multi-pane (sidebar + list + detail)
- The Magic-Plus stays bottom-trailing in both orientations

### Touch Targets
- Checkbox: 22pt visual, 44pt hit area
- To-do row: full row tappable (min 44pt)
- Magic-Plus: 56pt — generous and unmissable
- Toolbar icons: 22pt glyph, 44pt hit area
- Sidebar rows: full 40pt height

### Safe Area Handling
- Top: transparent bar respects Dynamic Island / notch; the big title scrolls under it
- Bottom: the Magic-Plus floats above the home indicator with a comfortable inset
- Sides: 20pt content insets (the generous margin is intentional)

## 9. Agent Prompt Guide

### Quick Color Reference
- Canvas: `#FFFFFF`
- Surface 1: `#F5F6F8`
- Surface 2 / Divider: `#ECECEC`
- Text primary: `#1D1D1F`
- Text secondary: `#8A8A8E`
- Text tertiary: `#C7C7CC`
- Things Blue (action/selection): `#4F97FF`
- Blue pressed: `#3D7FE0`
- Today yellow (star): `#FFD60A`
- Deadline red: `#FF3B30`

### Example Component Prompts
- "Create a SwiftUI Things to-do row: a 22pt circular checkbox with a 1.5pt #C7C7CC border on the leading side, then the task title 'Email the design feedback' in SF Pro Text 17pt weight 400 #1D1D1F, with a trailing #F5F6F8 'work' tag pill (13pt #8A8A8E, full radius) and a blue 'Today' date pill. The whole row is ≥44pt with 12pt vertical breathing on a pure-white canvas. When checked: fill the circle #4F97FF center-out over 180ms, spring a white checkmark in, dim the title to #C7C7CC with a strikethrough, then fade + collapse the row."
- "Build the Things Magic-Plus button: a 56pt circular button, background #4F97FF, a 24pt white plus glyph, soft blue shadow rgba(79,151,255,0.35) 0 8px 20px, floating bottom-trailing above the list. Tap scales 0.94 with a medium haptic; press-and-drag lifts it (scale 1.05) while a blue insertion line eases to the nearest row gap, and release drops a new editable to-do there."
- "Design the Things 'Today' list header: the word 'Today' in SF Pro Display 28pt weight 700 #1D1D1F with a #FFD60A star beside it, 24pt top padding, 8pt bottom, no divider — surrounded by generous whitespace on white."
- "Create a Things project section heading: a bold SF Pro Text 17pt #1D1D1F label 'Research' with 24pt top padding followed by a 1pt #ECECEC hairline divider; tapping collapses its to-dos with a 200ms height ease and rotates a small chevron."
- "Build the Things project pie-progress ring: a 20pt circle with a #ECECEC track and a #4F97FF arc filled to 60%; when a to-do in the project completes, animate the arc forward over 300ms."

### Iteration Guide
1. Canvas is pure white `#FFFFFF` — protect the generous whitespace, the calm IS the product
2. Things Blue (`#4F97FF`) is the only action color — selection, primary action, checkbox fill
3. The Today-yellow (`#FFD60A`) star is the single warm note — only Today and scheduled-today
4. The circular checkbox fill + spring check (then gentle fade-out) is the signature delight — never make completion abrupt
5. The Magic-Plus is the creation gesture — tap to add, press-drag to insert exactly where you want
6. Chunk long projects with bold 17pt headings + hairline dividers; keep list titles big (28pt w700)
7. The app is flat — the only shadow is the soft blue glow under Magic-Plus; even scrims stay light (≈0.2)
8. Navigation is the sidebar (Inbox / Today / Upcoming / Anytime / Someday) — no bottom tab bar

```

### 3.5 bergside/awesome-design-skills + design-md-chrome — SKILL.md + DESIGN.md çifti (typeui.sh)

- **Link:** https://github.com/bergside/awesome-design-skills (3.1k⭐) · https://github.com/bergside/design-md-chrome (3.0k⭐) · https://typeui.sh/design-skills · Kopya: `kopyalar/bergside-awesome-design-skills/` (cafe, editorial, premium, material, stitch) ve `kopyalar/bergside-design-md-chrome/`
- **Özet:** 67 estetik; her biri klasör: `SKILL.md` ajan talimatı (Mission, Brand, Style Foundations, Accessibility WCAG 2.2 AA, Writing Tone, Rules Do/Don't, Guideline Authoring Workflow, Required Output Structure, Component Rule Expectations, Quality Gates) + `DESIGN.md` Google uyumlu token. `npx typeui.sh pull cafe`. Chrome eklentisi aynı şablonu canlı siteden doldurur. DESIGN.md'ler ince (1.5KB) — asıl değer SKILL.md'nin "guideline authoring" iskeletinde ve "must/should" dilinde.
- **Nasıl kullanırız:** Kendi şablonumuzda DESIGN.md (token + rationale) ile SKILL.md (ajan davranış kuralları, QA checklist) ayrımını bu çiftten alırız; `design-md-chrome/DESIGN.md` "Design System Skill Blueprint" bizim SKILL.md taslağımız için birebir iskelet.

#### TAM ÖRNEK #4 — "Cafe" SKILL.md + DESIGN.md çifti (restoran/kafe müşterisine en yakın hazır skill)
Kaynak: https://github.com/bergside/awesome-design-skills/tree/main/skills/cafe (MIT)

```markdown
---
name: cafe
description: Cozy cafe-inspired interface with warm tones, soft typography, and clean layouts for a relaxed browsing experience.
license: MIT
metadata:
  author: typeui.sh
---

<!-- TYPEUI_SH_MANAGED_START -->
# Cafe Design System Skill (Universal)

## Mission
You are an expert design-system guideline author for Cafe.
Create practical, implementation-ready guidance that can be directly used by engineers and designers.

## Brand
A cozy café-inspired interface that blends warm tones, soft typography, and clean layouts to create a relaxed browsing experience

## Style Foundations
- Visual style: minimal, clean
- Typography scale: desktop-first expressive scale | Fonts: primary=Poppins, display=Poppins, mono=JetBrains Mono | weights=100, 200, 300, 400, 500, 600, 700, 800, 900
- Color palette: primary, neutral, success, warning, danger | Tokens: primary=#5D4432, secondary=#E9E3DD, success=#16A34A, warning=#D97706, danger=#DC2626, surface=#F9F7F5, text=#3E2B1E
- Spacing scale: 2/4/8/12/16/24/32/48


## Accessibility
WCAG 2.2 AA, keyboard-first interactions, visible focus states

## Writing Tone
concise, confident, helpful

## Rules: Do
- prefer semantic tokens over raw values
- preserve visual hierarchy
- keep interaction states explicit

## Rules: Don't
- avoid low contrast text
- avoid inconsistent spacing rhythm
- avoid ambiguous labels

## Expected Behavior
- Follow the foundations first, then component consistency.
- When uncertain, prioritize accessibility and clarity over novelty.
- Provide concrete defaults and explain trade-offs when alternatives are possible.
- Keep guidance opinionated, concise, and implementation-focused.

## Guideline Authoring Workflow
1. Restate the design intent in one sentence before proposing rules.
2. Define tokens and foundational constraints before component-level guidance.
3. Specify component anatomy, states, variants, and interaction behavior.
4. Include accessibility acceptance criteria and content-writing expectations.
5. Add anti-patterns and migration notes for existing inconsistent UI.
6. End with a QA checklist that can be executed in code review.

## Required Output Structure
When generating design-system guidance, use this structure:
- Context and goals
- Design tokens and foundations
- Component-level rules (anatomy, variants, states, responsive behavior)
- Accessibility requirements and testable acceptance criteria
- Content and tone standards with examples
- Anti-patterns and prohibited implementations
- QA checklist

## Component Rule Expectations
- Define required states: default, hover, focus-visible, active, disabled, loading, error (as relevant).
- Describe interaction behavior for keyboard, pointer, and touch.
- State spacing, typography, and color-token usage explicitly.
- Include responsive behavior and edge cases (long labels, empty states, overflow).

## Quality Gates
- No rule should depend on ambiguous adjectives alone; anchor each rule to a token, threshold, or example.
- Every accessibility statement must be testable in implementation.
- Prefer system consistency over one-off local optimizations.
- Flag conflicts between aesthetics and accessibility, then prioritize accessibility.

## Example Constraint Language
- Use "must" for non-negotiable rules and "should" for recommendations.
- Pair every do-rule with at least one concrete don't-example.
- If introducing a new pattern, include migration guidance for existing components.

<!-- TYPEUI_SH_MANAGED_END -->

```

DESIGN.md:

```markdown
---
name: Cafe
colors:
  primary: "#5D4432"
  secondary: "#E9E3DD"
  success: "#16A34A"
  warning: "#D97706"
  danger: "#DC2626"
  surface: "#F9F7F5"
  text: "#3E2B1E"
  neutral: "#F9F7F5"
typography:
  h1:
    fontFamily: "Poppins"
    fontSize: 3rem
  body-md:
    fontFamily: "Poppins"
    fontSize: 1rem
  label-caps:
    fontFamily: "JetBrains Mono"
    fontSize: 0.75rem
  sourceScale: "desktop-first expressive scale"
  weights: "100, 200, 300, 400, 500, 600, 700, 800, 900"
rounded:
  sm: 4px
  md: 8px
spacing:
  sm: 2px
  md: 4px
  sourceScale: "2/4/8/12/16/24/32/48"
---

## Overview

Cozy cafe-inspired interface with warm tones, soft typography, and clean layouts for a relaxed browsing experience.

## Style Foundations

- **Visual style:** minimal, clean
- **Typography scale:** desktop-first expressive scale
- **Typography fonts:** primary=Poppins, display=Poppins, mono=JetBrains Mono
- **Typography weights:** 100, 200, 300, 400, 500, 600, 700, 800, 900
- **Color palette:** primary, neutral, success, warning, danger
- **Spacing scale:** 2/4/8/12/16/24/32/48

## Colors

- **Primary (#5D4432):** Token from style foundations.
- **Secondary (#E9E3DD):** Token from style foundations.
- **Success (#16A34A):** Token from style foundations.
- **Warning (#D97706):** Token from style foundations.
- **Danger (#DC2626):** Token from style foundations.
- **Surface (#F9F7F5):** Token from style foundations.
- **Text (#3E2B1E):** Token from style foundations.
- **Neutral (#F9F7F5):** Derived from the surface token for official format compatibility.

```

### 3.6 anthropics/skills — frontend-design (Anthropic'in resmi tasarım felsefesi)

- **Link:** https://github.com/anthropics/skills/tree/main/skills/frontend-design (179.8k⭐ repo) · Kopya: `kopyalar/anthropics-skills/frontend-design/SKILL.md`, `theme-factory/`, `brand-guidelines/`
- **Özet:** Claude Code'un UI işinde otomatik yüklediği skill. Çekirdek fikirler: (1) "design lead at a studio" rolü, müşteri klişeyi zaten reddetti; (2) tasarımı **konunun dünyasından** türet (8-11 yaş kız oyuncağı ≠ finans dashboard'u); (3) tipografi kişiliktir, 1-2 aile, Elements of Typographic Style ölçeği, <80 karakter; (4) jenerik tipografi tell'leri: tek kelime vurgusu, ALL CAPS etiket, gereksiz eyebrow; (5) yapısal cihazlar bilgi taşımalı (01/02/03 sadece gerçek sıra varsa); (6) motion: tek orkestre an, her section'da fade-up yasak; (7) **5 AI-slop kümesi** (cream #F4F1EA + serif + terracotta #D97757; near-black + acid green; broadsheet hairline; SaaS-card kit; template chrome: eyebrow, middot, em-dash, #0B0B0B, mono etiket, →); (8) iki geçiş: plan (4-6 hex, type rolleri, ASCII wireframe, prensipler) → brief'e karşı "jenerik mi?" testi → kod → ekran görüntüsüyle öz-eleştiri, "Chanel: bir aksesuar çıkar"; (9) kopya kuralları (aktif ses, CTA = eylem, hata = ne oldu + çözüm, boş ekran = davet).
- **Nasıl kullanırız:** Şablonumuzun "Agent Prompt Guide" ve "Do's and Don'ts" bölümleri bu skill'in diliyle yazılmalı; "plan → brief'e karşı test → kod → screenshot critique" döngüsünü zincire sabitleriz. `theme-factory/themes/*.md` 10 hazır font+renk teması (ör. Ocean Depths, Golden Hour, Desert Rose) hızlı tohum.
- **SKILL.md (aynen, tamamı):**

```markdown
---
name: frontend-design
description: Guidance for distinctive, intentional visual design when building new UI or reshaping an existing one. Helps with aesthetic direction, typography, and making choices that don't read as templated defaults.
license: Complete terms in LICENSE.txt
---

# Frontend Design

Approach this as the design lead at a design studio known for giving every client a distinct visual identity that is not mistaken for anyone else's. This client has already rejected proposals that felt cliché or templated, and is paying for a distinctive point of view: make deliberate, opinionated choices about palette, typography, and layout that are specific to this brief, and take aesthetic risk if justified.

## Ground your designs in the subject matter

If the brief does not identify what the product or subject matter is, identify it yourself before designing, and confirm with the client. You can come up with one concrete subject, the design's audience, and the design's primary job, as a proposal. If there's any information in your memory about the client's preferences or context about what they're building, use that as a hint. The subject's industry, subject matter, materials, and vernacular are where distinctive visual choices come from — a design for a toy for girls aged 8–11 will be very aesthetically different from a dashboard for financial analysts. Build with the brief's real content and subject matter throughout.

## Design principles

For web designs, the hero is the first thing viewers will see. Open with the most characteristic thing in the subject's world, in the form that is most appropriate: a headline, an image, an animation, a live demo, an interactive moment, or other treatments. Be deliberate with your choice: a big number with a small label, supporting stats, and a gradient accent is the default treatment, so only use it if that's truly the best option.

Typography carries the personality of the page. You don't need a different typeface for display or headline text and body content: use one family or two, and if two, make them clearly distinct.

Choose your typefaces deliberately, not the default families you would reach for on any other project, and set a clear type scale following the default guidance of The Elements of Typographic Style with intentional weights, widths, and spacing. When type is used as a headline or visual element, use the type treatment itself as an active part of the design, not a neutral delivery vehicle for the content.

Default to line lengths of less than 80 characters. Serif typefaces can have slightly longer line lengths; give serif body text slightly more line-height than a sans-serif.

Avoid these default typographic treatments; they are the commonest tells of a generated page:
- Accenting just a single word or phrase in a headline, like putting one word in italic/bold or a different color.
- Using all caps for labels.
- Adding unnecessary typographic labels above content.

Visual structure is information. Structural devices like outlines, borders, numbering, eyebrows, dividers, labels, etc., encode useful information about the content rather than decorate it. Many generic designs use numbered markers (01 / 02 / 03), but that's only appropriate if the content actually is a sequence — like a stepped process or a timeline. Before adding numbered markers, check the content really is a sequence.

Use non-user-triggered motion sparingly and deliberately, only to draw attention. A single orchestrated moment — one page-load sequence or one reveal — lands better than scattered effects; fade-and-slide-up entrances on each section and hover transitions on every card are the generic default and read as AI-generated. Motion that answers a person's action (opening, expanding, confirming) is welcome when it shows what changed.

Consider written content carefully. Often a design brief may not contain real content, and it's up to you to come up with copy and placeholder content. Copy can make a design feel as templated as the design itself. See the below section on writing for more guidance.

## Process: plan, review against the brief, build, critique

For calibration, AI-generated design right now clusters around some traits:
1. a warm cream background (near #F4F1EA) with a high-contrast serif display and a terracotta or warm-clay accent (often near #D97757 — Anthropic's own Claude-interaction accent, so on a user's brief it reads as a tell);
2. a near-black background with a single bright acid-green or vermilion accent;
3. a broadsheet-style layout with hairline rules, zero border-radius, and dense newspaper-like columns;
4. the SaaS-card kit: content chopped into identical rounded cards, one border-radius on everything regardless of hierarchy, the same soft grey shadow (rgba(0,0,0,.1)) under each, and gradient washes as decoration;
5. template chrome that appears whatever the subject: a tracked-out ALL-CAPS eyebrow label above every heading; meta strings joined with middle dots ('A · B · C'); labels built as 'WORD — fragment' with a spaced em dash; tinted near-black (#0B0B0B, #111) standing in for black; a monospace face for small data labels; a '→' appended to link and button text.

All traits are legitimate for some briefs, but they are defaults rather than choices, and they appear regardless of subject. Where the brief pins down a visual direction, follow it exactly — the brief's own words always win, including when it asks for one of these looks. Where it leaves an axis free, don't spend that freedom on one of these defaults. As with a hired human designer, there's often a careful balance between doing what you're good at and taking each project as a chance to experiment and learn.

Work in two passes. First, brainstorm a short design plan based on the client's design brief: create a compact token system with color, type, layout, and principles.
- Color: describe the core base palette as 4–6 named hex values.
- Type: the typefaces and their roles.
- Layout: a layout concept, using one-sentence prose descriptions and ASCII wireframes to ideate and compare. Include alignment guidance; should the content be left aligned, center aligned, justified?
- Principles: the high-level guidance for what makes this page unique.

Then review that plan against the brief before building: if any part of it reads like the generic default you would produce for any similar page (work through a similar prompt to see if you arrive somewhere similar) rather than a choice made for this specific brief — revise that part, say what you changed and why. Only after you've confirmed the relative uniqueness of your design plan should you start to write the code, following the revised plan.

When writing the code, be careful of structuring your CSS selector specificities. It's easy to generate CSS classes that cancel each other out (especially with a type-based selector like .section and an element-based selector like .cta). This can happen often with padding/margin between sections.

## Restraint and self-critique

Spend your boldness in one place. Let one element be the memorable thing, keep everything around it quiet and disciplined, and cut any decoration that does not serve the brief. Build to a quality floor without announcing it: responsive down to mobile, visible keyboard focus, reduced motion respected, visually accessible, harmonious color palettes. Critique your own work as you build, taking screenshots to review if your environment supports it — a picture is worth 1000 tokens. Consider Chanel's advice: before leaving the house, take a look in the mirror and remove one accessory. Human creatives have memory and always try to do something new, so if you have a space to quickly jot down notes about what you've tried, it can help you in future passes.

## More on writing in design

Words appear in a design for one reason: to make it easier to understand and use. They are design content, not decoration. Bring the same intentionality and minimalism to copywriting that you would bring to spacing and color. Before writing anything, ask what the design needs to say, and how it can best be said to help the person navigate the experience.

Write from the end user's perspective. Name things by what users will understand in simple language, not by how the system is built. A user manages notifications, not webhook config. Describe what something is or does in plain terms rather than selling it. Being specific and legible to new users is always better than being clever.

Use active voice as default. A CTA says exactly what happens when it is used: "Save changes," not "Submit." An action keeps the same name through the whole flow, so the button that says "Publish" produces a toast that says "Published." The vocabulary of an interface is the signposting for someone navigating the product. Cohesion and consistency are how people learn their way around.

Treat failure and emptiness as moments for direction, not mood. Explain what went wrong and how to fix it, in the interface's voice rather than a person's. Errors don't apologize, and they are never vague about what happened. An empty screen is an invitation to act.

Keep the tone conversational: plain verbs, sentence case, no filler, with tone matched to the brand and the audience. Let each written element do exactly one job.

```

### 3.7 Leonxlnx/taste-skill — anti-slop frontend skill (3 dial + AI tells)

- **Link:** https://github.com/Leonxlnx/taste-skill (92.8k⭐, MIT) · https://tasteskill.dev · Kopya: `kopyalar/Leonxlnx-taste-skill/` (taste-skill, brandkit, stitch-skill, redesign-skill SKILL.md'leri)
- **Özet:** 87KB'lık, landing/portfolio/redesign odaklı skill. Akış: 0) brief inference ("Design Read" tek satır) → 1) üç dial → 2) brief→gerçek design system haritası (Fluent/Material/Carbon/shadcn/Bootstrap; "resmi paket varsa CSS'i elle yazma") → 3) stack/ikon/emoji politikası → 4) bias correction (tipografi, renk, layout, shadow, state, görsel, yoğunluk, tema kilidi) → 5) scroll animasyon iskeletleri → 6) perf/a11y → 9) **AI Tells** yasak listesi → 11) redesign protokolü. `brandkit` skill'i logo/kimlik board'u üretimi için (kategori → sembol mantığı tablosu). `stitch-skill` Google Stitch DESIGN.md üretimi için.
- **Üç dial ve preset'ler (aynen):**

```
* `DESIGN_VARIANCE: 8` - 1 = Perfect Symmetry, 10 = Artsy Chaos
* `MOTION_INTENSITY: 6` - 1 = Static, 10 = Cinematic / Physics
* `VISUAL_DENSITY: 4` - 1 = Art Gallery / Airy, 10 = Cockpit / Packed Data
```

| Signal | VARIANCE | MOTION | DENSITY |
|---|---|---|---|
| "minimalist / clean / calm / editorial / Linear-style" | 5-6 | 3-4 | 2-3 |
| "premium consumer / Apple-y / luxury / brand" | 7-8 | 5-7 | 3-4 |
| "playful / wild / Dribbble / Awwwards / experimental / agency" | 9-10 | 8-10 | 3-4 |
| "landing page / portfolio / marketing site (default)" | 7-9 | 6-8 | 3-5 |
| "trust-first / public-sector / regulated / accessibility-critical" | 3-4 | 2-3 | 4-5 |
| "redesign - preserve" | match existing | +1 | match existing |
| "redesign - overhaul" | +2 | +2 | match existing |

- **Tipografi ve renk kuralları (4.1 / 4.2, aynen seçmeler):**

```
* Sans font choice:
  * Discouraged as default: `Inter`. Pick `Geist`, `Outfit`, `Cabinet Grotesk`, `Satoshi`, or a brand-appropriate serif first.
  * Pairings to know: `Geist` + `Geist Mono`, `Satoshi` + `JetBrains Mono`, `Cabinet Grotesk` + `Inter Tight`, `GT America` + `IBM Plex Mono`.
* SERIF DISCIPLINE (VERY DISCOURAGED AS DEFAULT): ... Specifically BANNED as defaults: `Fraunces` and `Instrument_Serif` (the two LLM-favorite display serifs).
  If a serif is justified, rotate from this pool: PP Editorial New, GT Sectra Display, Cardinal Grotesque, Reckless Neue, Tiempos Headline, Recoleta, Cormorant Garamond, Playfair Display, EB Garamond, IvyPresto, Migra, Editorial Old, Saol Display, Söhne Breit Kursiv, Domaine Display, Canela, Schnyder, Tobias, NB Architekt, ITC Galliard.
* EMPHASIS RULE: use italic or bold of the SAME font. Do NOT inject a random serif word into a sans headline.

* Max 1 accent color. Saturation < 80% by default.
* THE LILA RULE: The "AI Purple / Blue glow" aesthetic is discouraged as a default. Use neutral bases (Zinc / Slate / Stone) with high-contrast singular accents (Emerald, Electric Blue, Deep Rose, Burnt Orange, etc.).
* COLOR CONSISTENCY LOCK: Once an accent color is chosen for a page, it is used on the WHOLE page.
* PREMIUM-CONSUMER PALETTE BAN: warm beige/cream (#f5f1ea, #f7f5f1, #fbf8f1, #efeae0 …) + brass/clay/oxblood/ochre (#b08947, #b6553a, #9a2436 …) + espresso text (#1a1714 …) is BANNED as the default reach.
  Default alternatives (rotate): Cold Luxury (silver-grey + chrome + smoke) · Forest (deep green + bone + amber) · Black and Tan · Cobalt + Cream · Terracotta + Slate · Olive + Brick + Paper · Pure monochrome + single saturated pop.
```

- **AI Tells (9.A-9.G) özet:** neon/dış glow yok, saf #000 yok, aşırı doygun aksan yok; Inter default değil, dev H1 yok, dashboard'da serif yok; 3 eşit feature kartı yasak; "John Doe/Acme/99.99%" yok; el yapımı SVG ikon yok (Phosphor/HugeIcons/Radix/Tabler), div'den sahte screenshot yok, Unsplash kırık link yok (picsum seed); hero'da `v0.6/BETA` etiketi yok, `00 / INDEX` section numarası yok, middot satırda en fazla 1, dekoratif renkli durum noktası yok, `<br>`+italik kırılmış başlık yok, dikey döndürülmüş metin yok, "Quietly trusted by / Field notes / From the field" yok, "Step 1/2/3" yok, foto üstüne pill etiket yok, sahte foto kredisi yok, footer'da versiyon yok, hero altında "BRAND. MOTION. SPATIAL." şeridi yok, scroll cue yok, lokasyon/hava şeridi yok; **em-dash (—) tamamen yasak** (en çok ihlal edilen tell).

### 3.8 Vinayak-Shukla-03/anti-ai-slop — en kısa ve net kural seti (+ 2. derece monokültür uyarısı)

- **Link:** https://github.com/Vinayak-Shukla-03/anti-ai-slop (9⭐, MIT; küçük ama metni çok iyi) · Kopya: `kopyalar/Vinayak-Shukla-03-anti-ai-slop/` (SKILL.md, references/toolkit.md, web-ui.md, presentations.md)
- **Özet:** "Distributional convergence" teşhisi; 9 sert kural; pre-ship self-audit; **set check** (birden çok ekran/slide aynı tasarımcıdan çıkmış gibi görünürse yeniden yakınsadın); `references/toolkit.md`'de font çiftleri, paletler, radius/spacing/type ölçekleri, ikon ve motion kiti, 5 estetik duruş.
- **9 sert kural (SKILL.md, aynen):**

```
1. No unbranded purple/indigo/violet, and no reflexive gradient. Not `#6366f1`, `#8b5cf6`, `#818cf8`, `sky-400 → indigo-400`, no 135° gradient on a logo chip, no glowing aurora "orbs" behind a hero. Pick one ownable, slightly-unexpected accent.
2. Never ship the system font stack or Inter/Geist/Poppins flat. Choose a real display face + a distinct body face. Typography is the #1 fastest way out of slop. (Self-host or a deliberate, characterful Google Font. Sentence case, not Title Case.)
3. No uniform-everything. Vary border-radius (a 4/8/16 hierarchy), vary padding, so size and space signal priority. Not every surface is a bordered rounded card. No cards-inside-cards. No single 0.1-opacity shadow on everything.
4. No emoji as icons. Use a real, consistent (ideally customized) icon set with a chosen weight. Never 🔥/🧘/🛒 as UI glyphs, never rotated-square CSS "diamond" bullets.
5. No `transition: all` and no blanket fade-in. Animate specific properties with tuned easing, and only to signal state, direct attention, or express brand.
6. No benefit-speak copy. Ban: Elevate, Unlock, Supercharge, Seamless(ly), Empower, Streamline, Leverage, "all-in-one", "Build the future of", tapestry/landscape/beacon/journey. No "not just X — it's Y". No forced rule-of-three. No em-dash pile-ups or decorative "·" middots.
7. No fabricated precision. Don't invent exact stats ("38% MoM", "3.2 days") with no source, no anonymous pull quotes attributed to nobody, no fake "updated 2 minutes ago".
8. Break the template skeleton. Don't emit Hero → 3-icon grid → social proof → CTA → footer by reflex.
9. No second-order monoculture — and if you make a set, force divergence. ... warm cream/dark paper + a high-contrast serif display (Fraunces/Playfair) + an earthy accent (terracotta/clay/ember/radish) + a corner page-counter + a "colored last word" headline + mono micro-labels on everything + a cute "sample data" disclaimer. Treat that whole kit as banned-by-default, exactly like purple/Inter.
```

- **Toolkit: font çiftleri ve paletler (references/toolkit.md, aynen):**

| Mood | Display / headings | Body / UI | Reads as |
|---|---|---|---|
| Editorial, trustworthy | Fraunces or Playfair Display | Inter Tight / Source Serif | magazine, considered |
| Modern, sharp product | Bricolage Grotesque or Clash Display | IBM Plex Sans | opinionated startup |
| Warm, human | Instrument Serif or Gambetta | Work Sans / Public Sans | approachable, editorial |
| Technical, precise | Space Grotesk (headings only) | JetBrains Mono / Geist Mono | engineering, exact |
| Bold, confident | Archivo / Anton (tight) | Inter / Satoshi | poster-like, punchy |
| Classic, premium | GT-Sectra-like serif / Libre Caslon | Söhne-like grotesk / Karla | luxury, established |

```
- Warm editorial: ink #1a1a17, paper #f7f3ec, accent terracotta #c4552f, secondary olive #6b6f3f.
- Deep & confident: near-black #0c0d0f, bone #e8e6e1, accent electric-lime #c3f53c used sparingly, muted steel #5b6470.
- Calm clinical: off-white #fbfbfa, ink #20242a, accent muted teal-not-cyan #2f7e78, sand #d8cbb4. (trustworthy, healthcare/finance)
- Vivid brand: cream #fff8ef, ink #161311, accent cobalt #2b4cff OR persimmon #ff5a36 (pick one), charcoal support #3a3733.
- Monochrome + one pop: true grayscale ramp, plus a single saturated accent used on <5% of the surface.
Radius hierarchy: small controls 4px, cards 8–10px, large containers 16px.
Spacing: 8px base (4, 8, 12, 16, 24, 32, 48, 64).
Type scale: 1.25 major-third: 14 / 16 / 20 / 25 / 31 / 39 / 49.
Elevation: 2–3 defined shadow levels; not a hairline border AND a shadow on the same card.
Motion: transition specific properties with cubic-bezier(0.2, 0, 0, 1) for entrances; never transition: all.
```

Divergence matrix (bir müşteri için birden çok sayfa/ekran ya da birden çok müşteri üretirken, aynen):

| Artifact | Ground | Display family | Accent | Stance |
|---|---|---|---|---|
| A | cool near-white | geometric grotesk | cobalt | Swiss |
| B | dark graphite | neutral grotesk + mono | amber | ops/technical |
| C | saturated field | bold condensed/display | cream-on-color | poster |
| D | warm newsprint | quirky display grotesque | duotone | riso/print |
| E | white | humanist sans | one signal color | data/editorial |

