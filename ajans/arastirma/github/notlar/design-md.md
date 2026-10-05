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

