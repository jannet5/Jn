# Sosyal Medya + Meta Reklam Otomasyonu — Açık Kaynak Araç/Skill/API Araştırması

Tarih: 2026-10-05 · Kategori: SOSYAL MEDYA + REKLAM OTOMASYONU · Hedef: ajans zincirinin "caption/brief → carousel görseli (GÜZEL) → Instagram'a yayın → Meta Ads" hattının yapay zekâ ajanıyla (Claude Code / Codex) uçtan uca çalışması.

Yöntem: GitHub arama (metadata: yıldız, `pushed_at`, lisans), 25+ repo shallow-clone, README/SKILL.md okundu, değerli `.md` dosyaları `../kopyalar/<repo-adi>/` altına birebir kopyalandı (her klasörde `KAYNAK.md`). Resmi Meta dokümanları ve Postiz/Publora API dokümanları WebFetch ile doğrulandı.

---

## 1. Özet tablo

Değer puanı (1-5): bizim zincire doğrudan katkı. Kopya: `kopyalar/` altında klasör var mı.

| Repo/Kaynak | Yıldız | Son güncelleme | Lisans | Ne işe yarar | Nasıl kullanılır | Değer | Kopya |
|---|---|---|---|---|---|---|---|
| **GÖRSEL ÜRETİMİ (carousel/post → PNG)** | | | | | | | |
| [marcolang/Marketing-Skills](https://github.com/marcolang/Marketing-Skills) `instagram-carousel` | 64 | 2026-10-01 | yok | Tek marka renginden 6 token palet türetir, Google Fonts tipografi tablosu, 7 slayt dizisi (Hero→Problem→Çözüm→…→CTA), HTML swipe önizleme, Playwright `device_scale_factor=2.5714` ile 1080×1350 PNG | SKILL.md'yi `~/.claude/skills/instagram-carousel/` altına koy; Python export scripti SKILL içinde | 5 | ✔ |
| [Ash-Harris/carousel-generator](https://github.com/Ash-Harris/carousel-generator) | 4 | 2026-08-01 | yok | Aynı ailenin daha olgun hali: 16 "non-negotiable" tasarım kuralı (yetim satır yok, başlık slayt genişliğinin %75-90'ı, ölü alan yok), hook A/B grid, `export.mjs`/`export.py`, 1080×1440 (3:4) | `npx skills add Ash-Harris/carousel-generator`; `preview-template.html` ile başla | 5 | ✔ (.md) |
| [idrsdev/social-carousel-generator](https://github.com/idrsdev/social-carousel-generator) | 2 | 2026-04-05 | MIT | `design-system.json` tek doğruluk kaynağı (renk/tipografi/CSS toolkit/ghost numbers), fontlar base64 gömülü, Playwright 1080×1350 PNG; 7 slayt tipi (cover, body_card, body_stat, body_step, strikethrough, cta, grid) | Plugin olarak kur; `design-system.json` + `python3 scripts/download_fonts.py`; `_generate.py` üretir | 5 | ✔ |
| [johnnyang0612/bearcarousel](https://github.com/johnnyang0612/bearcarousel) | 3 | 2026-07-23 | Apache-2.0 | JSON içerik × template × brand → Pillow ile 1080×1350 PNG + **otomatik audit** (gerçek ink-box hizalama, WCAG kontrast, kopya limiti); RED bulguda exit 1 | `pip install bearcarousel`; `draft/render/audit/export/open` CLI; `/plugin marketplace add johnnyang0612/bearcarousel` | 4 | ✔ |
| [dean9703111/ig-card-generator](https://github.com/dean9703111/ig-card-generator) | 13 | 2026-07-26 | MIT | Markdown DSL (`@cover/@point/@cta`, `[stat]`, `[vs]`, `[check]`…) → lint (kriterler) → Puppeteer 4:5/1:1/9:16 PNG; dark/light tema; alt-text.md ve contact-sheet üretir | `node scripts/lint.mjs <deck>` → `node scripts/build.mjs <deck> --theme both` (Çince doküman, DSL evrensel) | 4 | ✔ |
| [itchernetski/threads-carousel-claude-skill](https://github.com/itchernetski/threads-carousel-claude-skill) | 107 | 2026-04-19 | MIT | Next.js önizleme + html-to-image; 12 slayt tipi, 6 format preset (4:5, 1:1, 9:16, 16:9), 3 eksenli stil (font×yüzey×vurgu), PNG/PDF | `/threads-carousel <metin>`; template/ Next.js projesi | 3 | ✔ |
| [Maartenlouis/remotion-ads](https://github.com/Maartenlouis/remotion-ads) | 60 | 2026-05-06 | MIT | Remotion ile Reels (9:16, güvenli alanlar), 4:5 carousel still, ElevenLabs/Gemini TTS voiceover, altyazı, `paid-ads.md` Meta kampanya rehberi | `references/brand-config.md` doldur → scenes.json → `npx remotion render` / still | 4 | ✔ |
| [remotion-dev/skills](https://github.com/remotion-dev/skills) | 4847 | 2026-10-05 | Remotion lisansı (şirketler için ücretli olabilir) | Resmi Remotion Agent Skills: create/markup/render/captions | `npx skills add remotion-dev/skills` | 3 | ✔ |
| [remotion-dev/remotion](https://github.com/remotion-dev/remotion) | 61988 | 2026-10-05 | NOASSERTION (ücretli şirket lisansı) | React ile video/still render | Reels + animasyonlu story için | 3 | – |
| [vercel/satori](https://github.com/vercel/satori) (+ `@vercel/og`, `@resvg/resvg-js`) | 14013 | 2026-10-02 | MPL-2.0 | JSX/HTML+CSS alt kümesi → SVG → PNG; tarayıcısız, hızlı; flexbox sınırlı | `satori(<div/>, {width:1080,height:1350,fonts})` → resvg PNG | 3 | ✔ |
| [Macawls/ogre](https://github.com/Macawls/ogre) | 7 | 2026-09-05 | MIT | Pure Go HTML/CSS → PNG (satori alternatifi), sunucu modu | Docker servis olarak | 2 | – |
| [niklasvh/html2canvas](https://github.com/niklasvh/html2canvas) / [bubkoo/html-to-image](https://github.com/bubkoo/html-to-image) | 31925 / 7250 | 2024-07 / 2026-05 | MIT | Tarayıcı içinde DOM → PNG (önizleme UI'ı için) | Threads-carousel bunu kullanıyor | 2 | – |
| [polotno-project/render-tag](https://github.com/polotno-project/render-tag) (Polotno) | 116 | 2026-09-28 | – (Polotno SDK ticari) | Canva-benzeri editör SDK; HTML→canvas | Müşteriye "düzenle" UI'ı gerekirse | 2 | – |
| [nexu-io/open-design](https://github.com/nexu-io/open-design) | 99521 | 2026-10-05 | – | Açık kaynak "Claude Design" alternatifi; `social-carousel` skill'i (3 panel 1080×1080) | Masaüstü uygulama, ajan BYOK | 2 | – |
| **AI GÖRSEL ÜRETİMİ** | | | | | | | |
| [kingbootoshi/nano-banana-2-skill](https://github.com/kingbootoshi/nano-banana-2-skill) | 414 | 2026-04-02 | MIT | Gemini (Nano Banana 2 / Pro) CLI + Claude Code plugin; `-a 4:5`, `-r` referans görsel (stil/ürün), `-t` yeşil ekran → şeffaf PNG, maliyet takibi | `bun link` → `nano-banana "prompt" -a 4:5 -s 2K -r urun.png` | 4 | ✔ |
| [coreyhaines31/marketingskills](https://github.com/coreyhaines31/marketingskills) `image` | 53326 | 2026-10-03 | MIT | Model seçim tablosu (Gemini/Flux/Ideogram — metinli görsel için Ideogram), prompt kalıpları, optimizasyon | `skills/image/SKILL.md` | 3 | ✔ |
| [danielgatis/rembg](https://github.com/danielgatis/rembg) | 24964 | 2026-09-20 | MIT | Ürün fotoğrafı arka plan silme (ONNX, CPU) | `pip install rembg; rembg i in.jpg out.png` | 4 | – |
| [charlie947/social-media-skills](https://github.com/charlie947/social-media-skills) | 3780 | 2026-10-05 | MIT | `voice-builder`, `gemini-carousel` (slayt başına Gemini prompt), `graphic-designer` (HTML/CSS→PNG), `hook-generator`, `reels-scripting` | `npx skills add charlie947/social-media-skills` | 3 | ✔ |
| **CAPTION / HASHTAG / İÇERİK SKILL'LERİ** | | | | | | | |
| [sergebulaev/instagram-skills](https://github.com/sergebulaev/instagram-skills) | 266 | 2026-10-03 | MIT | 9 skill: `ig-caption-writer` (ilk 125 karakter hook), `ig-carousel-planner` (10 slayt omurgası), `ig-hashtag-strategist` (3-5 boyutlu set), `ig-humanizer` (AI-tell temizliği), `ig-content-planner`; Publora ile yayın | `/plugin marketplace add sergebulaev/instagram-skills`; Codex plugin de var | 5 | ✔ |
| [rediumvex/social-media-caption-generator-claude](https://github.com/rediumvex/social-media-caption-generator-claude) | 129 | 2026-08-11 | MIT | Tek girdiden 7 platform caption (IG Reels/Carousel, TikTok, Threads, FB, FB Group, Shorts); formül + hook kütüphanesi + anti-pattern + FB "trigger word" listesi | `~/.claude/skills/social-captions/SKILL.md` | 4 | ✔ |
| [coreyhaines31/marketingskills](https://github.com/coreyhaines31/marketingskills) `social`, `copywriting` | 53326 | 2026-10-03 | MIT | `social`: 5 carousel çerçevesi (Value-Stack, Problem-Proof, Hack List, Rant, Demo), platform limitleri, post şablonları, kısa video; `copywriting` | `npx skills add coreyhaines31/marketingskills --skill social ad-creative` | 5 | ✔ |
| [zubair-trabzada/ai-marketing-claude](https://github.com/zubair-trabzada/ai-marketing-claude) | 2716 | 2026-03-02 | MIT | `/market social` 30 günlük takvim (pillar oranları, platform içerik karışımı), `/market ads` platform bazlı reklam metni + spec, `/market brand` | Plugin; paralel subagent | 3 | ✔ |
| [Hao0321/claude-skill-social-post](https://github.com/Hao0321/claude-skill-social-post) | 725 | 2026-10-03 | MIT | Kullanıcının FB sesini öğrenir, 14 günlük takvim, FB/IG/Threads/X yayın, Chrome MCP ile yorum yönetimi (Çince, Tayvan) | Fikir kaynağı; dil engeli | 2 | ✔ |
| [kostja94/marketing-skills](https://github.com/kostja94/marketing-skills) | 1013 | 2026-09-25 | MIT | 170+ skill (SEO ağırlıklı); `paid-ads-strategy`, platform skill'leri | `npx skills add kostja94/marketing-skills --skill paid-ads-strategy` | 2 | ✔ |
| [ScrapeCreators/social-media-research-skills](https://github.com/ScrapeCreators/social-media-research-skills) | 3203 | 2026-08-26 | MIT | Rakip/outlier post, yorum madenciliği, Meta Ad Library araştırması (ScrapeCreators API, ücretli) | Rakip analizi adımı için | 2 | – |
| **META ADS (API / MCP / SKILL)** | | | | | | | |
| [facebook/facebook-python-business-sdk](https://github.com/facebook/facebook-python-business-sdk) | 1606 | 2026-09-21 | Facebook Platform License | Resmi Marketing API SDK (campaign/adset/ad/creative/insights) | `pip install facebook_business` | 5 | ✔ |
| [facebook/facebook-nodejs-business-sdk](https://github.com/facebook/facebook-nodejs-business-sdk) | 621 | 2026-09-21 | Facebook Platform License | Node sürümü | `npm i facebook-nodejs-business-sdk` | 4 | – |
| [pipeboard-co/meta-ads-mcp](https://github.com/pipeboard-co/meta-ads-mcp) | 1292 | 2026-09-23 | özel (NOASSERTION) | Hosted Meta Ads MCP (42 araç: kampanya, creative upload, insights, hedefleme arama), yazmalarda onay, yeni kampanya PAUSED başlar; Pipeboard CLI | `claude mcp add` ile `https://meta-ads.mcp.pipeboard.co/`; local için kendi Meta app'i gerekir | 4 | ✔ |
| [oliverames/meta-mcp-server](https://github.com/oliverames/meta-mcp-server) | 47 | 2026-09-24 | MIT | 199 araç: `meta_publish_instagram_carousel`, IG insights, Pages, Threads, Ads Manager, CAPI; tek `META_ACCESS_TOKEN` ile self-host; `docs/` altında Graph API özetleri | `npx -y @oliverames/meta-mcp-server` | 5 | ✔ |
| [mikusnuz/meta-ads-mcp](https://github.com/mikusnuz/meta-ads-mcp) | 82 | 2026-08-28 | MIT | Marketing API v25, 135 araç, self-host | alternatif | 3 | – |
| [EfrainTorres/armavita-meta-ads-mcp](https://github.com/EfrainTorres/armavita-meta-ads-mcp) | 75 | 2026-09-16 | AGPL-3.0 | Marketing API v26, token redaction, cursor pagination | alternatif (AGPL) | 2 | – |
| [irinabuht12-oss/google-ads-meta-ads-mcp](https://github.com/irinabuht12-oss/google-ads-meta-ads-mcp) (Ryze) | 3905 | 2026-09-25 | MIT | Hosted Google+Meta+GA4+GSC MCP (`https://connector.get-ryze.ai/mcp`) | `claude mcp add ryze --transport http …` | 3 | – |
| [irinabuht12-oss/marketing-skills](https://github.com/irinabuht12-oss/marketing-skills) | 3540 | 2026-09-24 | yok | 49 skill: `meta-ads-audit` (yorgunluk eşikleri, overlap, tracking checklist), `ad-copy-variant-generator`, `creative-fatigue-detection`, `client-report-narratives`, `weekly-account-summary` | `claude plugin marketplace add irinabuht12-oss/marketing-skills` | 4 | ✔ |
| [mathiaschu/meta-ads-analyzer](https://github.com/mathiaschu/meta-ads-analyzer) | 439 | 2026-02-26 | MIT | Meta Ads analiz skill'i + MCP: "Breakdown Effect", learning phase, pacing, auction overlap referansları; zorunlu rapor şablonu | `skill/SKILL.md` + references | 4 | ✔ |
| [AgriciDaniel/claude-ads](https://github.com/AgriciDaniel/claude-ads) | 9722 | 2026-10-05 | MIT | 12 platform paid-media ops; `/ads audit|plan|create|launch --draft|monitor|report`; read-only varsayılan, capability-gated yazma; Meta audit kontrol kataloğu, creative spec kontratı | `/plugin marketplace add agricidaniel/claude-ads` | 4 | ✔ |
| [ivangfalco/ads-skills](https://github.com/ivangfalco/ads-skills) | 278 | 2026-09-30 | **Commons Clause** (ticari kısıt) | Meta Ads "Operating System" (TCPL anchor, kill/scale), Marketing API v22 tam referans (campaign/adset/ad şemaları), 39 Python API scripti | Referans olarak oku; lisans nedeniyle kodu ürüne gömme | 3 | ✔ |
| [coreyhaines31/marketingskills](https://github.com/coreyhaines31/marketingskills) `ad-creative`, `ads` | 53326 | 2026-10-03 | MIT | `ad-creative`: grounded inputs (reviews/winning ads), Meta karakter limitleri, hook-system, statik reklam şablonları, creative review page; `ads`: Meta decision system (TCPL), audience targeting, platform setup checklist | aynı repo | 5 | ✔ |
| [zubair-trabzada/ai-ads-claude](https://github.com/zubair-trabzada/ai-ads-claude) | 267 | 2026-10-04 | MIT | 15 skill reklam stratejisi + platform copy + PDF rapor | alternatif | 2 | – |
| **YAYIN (Instagram publishing)** | | | | | | | |
| Meta Instagram Graph API — [Content Publishing](https://developers.facebook.com/docs/instagram-platform/content-publishing) | resmi | – | – | Container → publish akışı; carousel 2-10; **sadece JPEG**; 100 post/24s; `content_publishing_limit` | Doğrudan curl/SDK; izinler aşağıda | 5 | (meta-mcp-server docs) |
| [gitroomhq/postiz-app](https://github.com/gitroomhq/postiz-app) | 36714 | 2026-10-05 | AGPL-3.0 | Self-host zamanlayıcı; Public API (`/public/v1/upload`, `/public/v1/posts`), MCP server, n8n node, Make; 30+ ağ | Docker; API key `Settings > Developers`; rate 90 req/saat (global) | 4 | ✔ |
| [publora/skills](https://github.com/publora/skills) | 49 | 2026-10-02 | MIT | 9 SKILL.md + hosted MCP (`https://mcp.publora.com`), `instagram-post` skill: JPEG/PNG/WebP, 8 MB, 2-10 carousel, `mediaUrls` tek çağrı; ücretsiz Starter plan | `claude mcp add publora --transport http …` | 4 | ✔ |
| [inovector/mixpost](https://github.com/inovector/mixpost) | 3778 | 2026-03-16 | MIT (Lite) | Laravel self-host Buffer alternatifi; API Pro sürümde | Postiz'e göre daha az ajan-dostu | 2 | – |
| n8n şablonları: [#3693](https://n8n.io/workflows/3693-create-and-publish-instagram-carousel-posts-with-gpt-41-mini-imgur-and-graph-api/), [#12413](https://n8n.io/workflows/12413-generate-and-publish-instagram-carousels-with-gemini-and-google-slides/) | – | – | – | GPT/Gemini caption + Imgur/Google Slides görsel + Graph API carousel publish | Referans akış; biz kendi scriptimizle yaparız | 2 | – |
| **TASARIM SİSTEMİ (marka tutarlılığı)** | | | | | | | |
| [VoltAgent/awesome-design-md](https://github.com/VoltAgent/awesome-design-md) / [google-labs-code/design.md](https://github.com/google-labs-code/design.md) | 119643 / 28246 | 2026-10 | MIT / Apache-2.0 | DESIGN.md formatı: marka renk/tipografi/ton tek dosyada → ajan tutarlı üretir | Her müşteri için `DESIGN.md` + `design-system.json` | 4 | (başka kategori) |

---

## 2. Önerilen hat (pipeline)

```
[0] Müşteri marka dosyası              DESIGN.md (VoltAgent formatı) + design-system.json (idrsdev şeması) + voice.md (charlie947 voice-builder)
        │
[1] Brief → caption/hashtag/slayt metni   sergebulaev/instagram-skills (ig-carousel-planner, ig-caption-writer, ig-hashtag-strategist, ig-humanizer)
        │                                  + marketingskills/social carousel-frameworks (yapı seçimi) + rediumvex (çoklu platform caption)
        │
[2] HTML carousel şablonu               marcolang/Ash-Harris kuralları (renk türetme, tipografi, 16 kural) + idrsdev design-system.json token'ları
        │                                  görsel gerekiyorsa: nano-banana (-a 4:5 -r urun.png) / rembg (ürün arka planı)
        │
[3] HTML → PNG                           Playwright: viewport 420×525, device_scale_factor=2.5714 → 1080×1350 (veya 1080×1440 3:4)
        │                                  Kalite kapısı: bearcarousel-benzeri audit (kontrast ≥4.5:1, taşma, yetim satır) + PNG'yi geri oku
        │                                  → JPEG'e çevir (Graph API sadece JPEG; sharp/Pillow quality 92)
        │
[4] Yayın                                a) Doğrudan Instagram Graph API (container→publish; görseller public HTTPS URL'de olmalı: S3/R2)
        │                                  b) Postiz self-host Public API/MCP (zamanlama, çoklu ağ, onay kuyruğu)
        │                                  c) Publora MCP (en hızlı başlangıç, hosted)
        │
[5] Reklam                               facebook-python-business-sdk ile campaign→adset→ad (PAUSED başlat) + creative (aynı PNG/JPEG + ad-creative skill metni)
        │                                  veya pipeboard/oliverames MCP ile sohbetten; analiz: meta-ads-analyzer + irina meta-ads-audit
        │
[6] Raporlama                            IG Insights (/media/insights: reach, saves, shares) + Ads Insights (spend, cpm, ctr, cost_per_action_type) → client-report-narratives skill
```

### Adım adım gereksinimler / izinler

| Adım | Araç | Gereksinim | Not |
|---|---|---|---|
| 1 | Skill'ler (Claude Code) | Sadece dosya; API yok | `voice-profile.md` dolu olmalı; "Never invent the specifics" kuralı |
| 2-3 | Playwright + Chromium | `pip install playwright && playwright install chromium`; fontlar base64 gömülü (headless'ta Google Fonts beklemek yerine) | Üretimde Docker imajı: `mcr.microsoft.com/playwright/python` |
| 2 | nano-banana | `GEMINI_API_KEY`; ~$0.067/1K görsel (Flash), Pro ~$0.134 | Ürün fotoğrafı + marka rengi referansı `-r` ile |
| 4a | Instagram Graph API | **Instagram Professional (Business) hesabı + bağlı Facebook Sayfası**; Meta Developer app; izinler: `instagram_basic` + `instagram_content_publish` + `pages_read_engagement` (Facebook Login) **veya** `instagram_business_basic` + `instagram_business_content_publish` (Business Login for Instagram); Page Publishing Authorization; **App Review** yalnızca app'te rolü olmayan kullanıcılar için gerekli — kendi/müşteri hesapları Business Manager'a eklenirse Standard Access yeterli (2-4 hafta review'u atlar) | Carousel: her görsel `is_carousel_item=true` container → `media_type=CAROUSEL&children=` → `media_publish`. Tüm görseller ilk görselin oranına kırpılır. **Sadece JPEG**. 100 post/24 saat. Görsel public URL'de olmalı (R2/S3 presigned olmaz, düz public). |
| 4b | Postiz | Docker (Postgres+Redis); IG bağlamak için yine Meta app (kendi app'ini Postiz'e tanıtırsın) | `/public/v1/upload` → `/public/v1/posts` (`__type: instagram`); 90 req/saat global limit; n8n node var |
| 4c | Publora | Hosted; IG Business hesabı OAuth; API key | `create_post(mediaUrls=[…10 https url], scheduledTime)`; JPEG/PNG/WebP (PNG'yi kendisi çevirir), 8 MB |
| 5 | Marketing API | Ad account (act_…), Business verification; izinler `ads_management`, `ads_read`, `business_management`; **Standard Access** için app review gerekmez (kendi Business'ındaki hesaplar), Advanced Access için review; Meta Pixel + CAPI (EMQ ≥6) | Yeni kampanya `status=PAUSED`; objective ODAX (`OUTCOME_LEADS`, `OUTCOME_SALES`, `OUTCOME_ENGAGEMENT`); `special_ad_categories=[]`; bütçe kuruş cinsinden |
| 6 | Insights | `/{ig_user_id}/insights`, `/{media_id}/insights`; `/act_X/insights` | `impressions` IG'de v22+ deprecated → `views`/`reach` kullan |

Türkiye bağlamı: Türkçe'ye özel hazır caption skill'i bulunmadı (aramada sadece İngilizce/Portekizce/İspanyolca/Çince skill'ler). Yapılacak: sergebulaev `voice-rules.md` + rediumvex formüllerini Türkçe'ye uyarlayan tek bir `tr-caption` SKILL.md yazmak (ilk 125 karakter hook, 3-5 niş hashtag `#kadıköyrestoran` gibi yerel, FB trigger-word listesinin Türkçe karşılığı: "ücretsiz", "hemen al", "sınırlı süre"). Meta Ads yerel işletme için: hedefleme = konum yarıçapı (restoran 3-8 km, klinik 10-25 km) + `OUTCOME_LEADS` (WhatsApp/Instant Form) veya `OUTCOME_ENGAGEMENT` (mesaj); special_ad_categories sağlık kliniği için boş ama **sağlık reklam politikaları** (öncesi/sonrası görsel yasağı) kontrol edilmeli.

---

## 3. Kaynak bölümleri (değerli SKILL.md / prompt metinleri AYNEN)

### 3.1 sergebulaev/instagram-skills — caption + carousel planlama + hashtag (DEĞER 5)

Link: https://github.com/sergebulaev/instagram-skills · 266★ · MIT · Codex ve Claude Code plugin'i. Kopya: `kopyalar/sergebulaev-instagram-skills/`.

Özet: 9 skill; her taslak "approval card" ile biter, yayın Publora üzerinden (`lib.publish(kind="carousel", media=[...])`). En değerli parçalar: carousel omurgası (slayt 1 = vaat + açık döngü, 2-3 = en güçlü değer, son = kaydedilebilir özet + tek CTA), hashtag "3-5 boyutlu set" kuralı, humanizer (em-dash sınırı, AI kelime listesi), "Never invent the specifics" kuralı.

Kurulum:
```
/plugin marketplace add sergebulaev/instagram-skills
/plugin install instagram-skills@instagram-skills
# veya: npx skills add sergebulaev/instagram-skills
```

**`skills/ig-carousel-planner/SKILL.md` (aynen, kaynak: https://github.com/sergebulaev/instagram-skills/blob/main/skills/ig-carousel-planner/SKILL.md):**

````markdown
---
name: ig-carousel-planner
description: "Plan an Instagram carousel slide by slide, up to 10 slides, with a hook slide that opens a loop, value slides that front-load the payoff, and a payoff slide that earns the save and follow. Picks a 2026 carousel formula (listicle, before/after, myth-buster, framework) by goal (saves, shares, follows), drafts each slide's text plus the caption, then publishes the images you supply via Publora. Use to structure a multi-slide carousel. Not for single-image captions (use ig-caption-writer)."
---

# Instagram Carousel Planner

Plan a carousel that gets swiped to the end and saved. The whole game is slide 1
(the swipe-earning hook) and the last slide (the saveable payoff). This skill
maps every slide, writes the on-image text, and drafts the caption that frames
it.

## When to use

- User wants to turn a topic or notes into a multi-slide carousel
- User has a list, a framework, or a transformation to teach
- User wants a slide-by-slide structure, not just a caption

## Formulas this skill uses (carousel shapes)

| Code | Formula | Primary goal | Best for |
|---|---|---|---|
| IG5 | Listicle Carousel | saves | a numbered teaching list, one item per slide |
| IG6 | Before/After Transformation | saves, follows | proof of a result with the steps between |
| IG7 | Myth-Buster | shares | correcting beliefs your niche holds |
| IG8 | Steal-This Framework | saves | a named, repeatable framework |

Full skeletons in `../../references/hook-formulas.md`.

## Slide architecture (the 10-slide spine)

A carousel is 2-10 slides (API limit). The reliable spine:

| Slide | Role |
|---|---|
| **1 (hook)** | the promise + an open loop ("most miss #4"). Earns the swipe. Big text, one idea. |
| **2-3** | the strongest value, front-loaded (swipe-through decays with depth). |
| **4 to N-1** | one point per slide, each standing alone, each readable in 2 seconds. |
| **N (payoff)** | the one-slide summary (the saveable artifact) + one clear ask (save / follow). |

6-10 slides is the sweet spot for a teaching carousel. Fewer than 4 is usually a
single image. See `references/slide-architecture.md` for per-formula spines.

## Steps

**Voice profile first (all drafts).** If `../../references/voice-profile.md` has `filled: yes`, load it and match the user's voice fingerprint, hard rules, and CTA/link style throughout. If it is not filled, mention once that `ig-humanizer --mode profile` can learn their voice from a few posts, then proceed with the generic voice rules.

**Never invent the specifics.** The rules below ask for a concrete number, a date and a named entity, because that is what separates a real post from a generated one. Take them from what the user actually said in this conversation. **Do not invent a figure, a date, a client name or a result, and do not soften a vague claim into a plausible-looking number.** If the user has nothing concrete for a beat, ask them once, and if they still have nothing, drop the claim rather than decorate it. A published invented number is a retraction; a missing one is only a weaker post.

1. **Gather inputs.** Topic, the list/framework/transformation, target audience,
   and the goal (saves / shares / follows).
2. **Pick the formula.** Use the goal table; suggest 2-3 that fit and let the
   user choose.
3. **Set the slide count.** Match it to the real content (6-10 typical). Never
   pad to hit 10; viewers feel filler and bail.
4. **Write slide 1.** The promise plus the loop. Big, single-idea on-image text.
   Front-load a number if one fits.
5. **Map the value slides.** Put the strongest point on slide 2 or 3. One point
   per slide, each able to stand alone. Draft the on-image text for each (keep it
   short, it has to read in 2 seconds on a 4:5 frame).
6. **Write the payoff slide.** A one-slide recap that is worth saving on its own,
   plus a single ask (save it / follow for more).
7. **Write the caption.** The caption supports the carousel (it can be short),
   restates the hook for the fold, and carries the hashtags. Hand off to
   `ig-hashtag-strategist` or apply the 3-5 sized set.
8. **Humanizer pass** on the on-image text and caption. Scrub 2026 AI vocab
   by density (per slide, per caption paragraph), cap em dashes (at most one
   per slide, about one per 100 words in the caption; never swap one for a
   period), break stacked triads and reveal bridges. Slides are short by
   design: never chop or pad a slide for rhythm. Canonical rules:
   `ig-humanizer` V3.
9. **Approval card.** Show: formula, slide-by-slide outline with each slide's
   text, the caption, hashtag set, primary goal, and a reminder that the user
   supplies 2-10 images (all images, no mixed media) in this order.
10. **On approval, publish with media.** Call `lib.publish(kind="carousel",
    draft_text=<approved caption>, target_url="https://www.instagram.com/",
    media=[<slide image paths in order>], platforms=[<INSTAGRAM_PLATFORM_ID>],
    scheduled_time=<iso_or_None>)`. The wrapper uploads each image in order and
    schedules. With no media or no Publora key it returns the plan as a
    copy-paste block plus the slide order.

## Hard rules

Global voice rules: see root `SKILL.md` Voice rules. Additional skill-specific
rules:

- Slide 1 is a promise with an open loop, never a bare title.
- Front-load value to slides 2-3; never bury the best point at the end.
- One point per slide, readable in 2 seconds on a portrait 4:5 frame.
- 2-10 slides only. Never pad to a round number.
- All images, no mixed media (the API rejects an image+video carousel).
- The last slide must earn the save with a one-slide summary and one ask.

## Anti-patterns (skill will refuse)

- A title-only slide 1 with no promise or loop.
- A 10-slide carousel padded from a 4-point idea.
- The strongest point saved for the final slide.
- Three competing CTAs on the payoff slide.
- Mixing images and a video in one carousel.
- On-image walls of text that cannot be read in a swipe.
- Em dashes above the cap (more than one on a slide), AI vocab clusters, stacked or hollow rule-of-three.
- "The result?" reveals and staccato stacks added for punch.

## Resources

- `../../references/hook-formulas.md` - the carousel formulas (IG5-IG8) with skeletons
- `../../references/algorithm-heuristics.md` - 2026 carousel ranking and swipe-through rules
- `../../references/hashtag-strategy.md` - the 3-5 sized hashtag recipe
- `../../references/media-workflow.md` - uploading 2-10 images in order
- `references/slide-architecture.md` - per-formula slide spines and on-image text rules

## Optional illustration

Offer a generated image when a visual would lift reach. Draft a prompt and call
`lib.illustrate(prompt, kind="carousel")`, pulling brand handle/color from Voice &
Brand Profile section 6 for a pixel-exact overlay. Show the returned `url` + `cost`,
attach it as your Instagram media via `media_urls=[url]` in one shot (Publora fetches the hosted URL server-side): a single image posts as a photo, and the carousel planner can pass 2-10 URLs for a carousel. For a Reel cover instead, set `platformSettings.instagram.coverUrl`. Full workflow (incl. quote-cards):
`../ig-humanizer/sub-skills/illustration.md`. No Pixfaro key -> it drafts the prompt for you to generate manually.
## Related skills

- `ig-caption-writer` - when the idea is a single image, not a carousel
- `ig-hashtag-strategist` - build the sized hashtag set
- `ig-humanizer` - scrub the on-image text and caption
- `ig-hook-extractor` - reverse-engineer a carousel you admire
````

**`references/hashtag-strategy.md` ilk bölüm (aynen, kaynak: https://github.com/sergebulaev/instagram-skills/blob/main/references/hashtag-strategy.md):**

````markdown
# Instagram Hashtag Strategy - 2026

The 30-hashtag wall is dead. Instagram's own guidance and creator testing in
2025-2026 converged on the same finding: **3 to 5 well-chosen, correctly-sized
hashtags outperform 30 random ones.** Hashtags in 2026 act more like topic
labels that help Instagram categorize the post than like a discovery firehose.
Reach now comes mostly from sends, saves, and the recommendation engine, not
from hashtag feeds.

## The core rule: size, not volume

A hashtag's **size** is its total post count. Posting a small account into a
giant hashtag is like dropping a flyer into a stadium: it is gone in seconds.
The strategy is a sized mix so the post can actually rank in the smaller tags
while still touching the bigger ones.

| Tier | Post count | Role | How many to use |
|---|---|---|---|
| **Niche** | under 50k | where a small/mid account can actually rank and stay visible for hours | 2-3 |
| **Mid** | 50k - 500k | the sweet spot: enough audience, beatable competition | 1-2 |
| **Broad** | 500k - 5M+ | a brief touch of a big audience; you rank for minutes, not hours | 0-1 |

**The 2026 default: 3-5 total.** Roughly 2-3 niche + 1-2 mid + at most 1 broad.
Adding more past 5 does not add reach and can read as spam.

## Why niche wins

- A niche tag (under 50k) has slow churn, so your post stays near the top of
  "Recent" and "Top" for hours instead of seconds.
- Ranking in a niche tag is achievable for a small account; ranking in a 5M tag
  is not, so the broad tag is mostly a categorization signal.
- Niche tags also pull a more relevant audience, which means better saves and
  sends, which is what actually drives reach.

## Sizing in practice

You cannot see a tag's exact post count from inside the publishing API, so size
tags by checking the count Instagram shows in the app's search, or by judgment:

- **Niche signals:** specific, long-tail, often combines two words or a
  community name (`#notionforfreelancers`, `#uglyfoodphotography`,
  `#bootstrappedsaas`). If it feels like only people in your exact world would
  type it, it is niche.
- **Mid signals:** a recognizable sub-topic (`#contentstrategy`,
  `#indiehacker`, `#filmphotography`). Known but not universal.
- **Broad signals:** one common word everyone uses (`#marketing`, `#fitness`,
  `#travel`, `#food`). Treat these as labels, use at most one.

## Building the set (the 3-5 recipe)

1. **2-3 niche tags** that describe exactly who the post is for. These are the
   workhorses; they earn the ranked, lasting visibility.
2. **1-2 mid tags** that name the sub-topic. These extend reach to a warm,
   relevant audience.
3. **0-1 broad tag** only if one genuinely fits as a category label. Skip it if
   nothing fits; an irrelevant broad tag dilutes the set.
4. **Match the post.** Every tag must describe the actual content. A mismatched
   tag (popular but off-topic) trains the recommendation engine to show your
   post to the wrong people, which lowers engagement rate and reach.
5. **Avoid banned or flagged tags.** Some generic tags (`#follow4follow`,
   `#like4like`, and various others that cycle through flagged status) can
   suppress a post. When in doubt, skip the generic engagement tags entirely.

## Placement

- **End of the caption** or **first comment** both work; reach is the same. The
  first comment keeps the caption clean. Never put hashtags mid-sentence.
- **Do not** repeat the same 5-tag block on every post forever. Instagram can
  read identical tag sets across many posts as automated behavior. Rotate sets
  per topic.
````

**`skills/ig-caption-writer/SKILL.md` — Hard rules + Anti-patterns (aynen):**

````markdown
## Hard rules

Global voice rules: see root `SKILL.md` Voice rules. Additional skill-specific
rules:

- The first 125 characters must stop the scroll on their own. Rewrite any hook
  that needs the second line to make sense.
- One idea per caption. Two ideas means two posts.
- One specific number where the claim allows it.
- 3-5 sized hashtags, never 30. 0-3 emoji, placed with intent.
- Media is required. Never present a caption as publishable without reminding
  the user to attach the image or video.
- Do not hard-sell. One natural mention of the next step, max.

## Anti-patterns (skill will refuse)

- A hook that only makes sense after the fold.
- Em dashes above the cap (more than about one per 100 words), or an em dash swapped for a period.
- "Let's talk about.." or "Here's the thing.." openers; "The result?" as a reveal.
- Announced candor ("real talk", "not gonna lie", "POV:" on something that is not a POV) with no dated fact behind it.
- Staccato stacks ("No X. No Y. Just Z.") and one-word lines added for drama.
- 20-30 hashtags stuffed at the top.
- Engagement bait ("double tap if you agree", "comment YES").
- Rule-of-three lists without specifics.
- "leverage", "fundamentally", "game-changer", "level up", "dive in".
- Padding a one-line idea to look substantial.

````

Bizim not: Publora yerine kendi Graph API/Postiz katmanımızı `lib.publish` imzasıyla yazarsak skill'ler değişmeden çalışır.

### 3.2 rediumvex/social-media-caption-generator-claude — 7 platform caption (DEĞER 4)

Link: https://github.com/rediumvex/social-media-caption-generator-claude · 129★ · MIT. Kopya: `kopyalar/rediumvex-social-media-caption-generator-claude/SKILL.md`.

Özet: Girdi (metin/görsel/rakip post) → analiz → 7 platform için farklı formülle caption; platform kuralları (IG Reels 80-200 kr, Carousel 300-900 kr + save CTA, FB 40-80 kr + trigger word listesi), sinyal hiyerarşisi (share > save > comment > like). Türkçe'ye uyarlanacak ana şablon budur.

**PLATFORM RULES + CAPTION FORMULAS + ANTI-PATTERNS bölümü (aynen, kaynak: https://github.com/rediumvex/social-media-caption-generator-claude/blob/main/SKILL.md):**

````markdown
## PLATFORM RULES (CRITICAL — follow exactly)

### INSTAGRAM REELS
- **Length:** 80-200 characters. Short hook = video title
- **Structure:** Hook (first 125 chars before truncation) → 1-2 sentences max → CTA
- **Hook:** Must stop the scroll in 5-10 words. Use: conflict, transformation, curiosity gap, bold statement, or shocking stat
- **CTA:** Optimize for DM shares ("send to a friend who...") or saves ("save for later") — 10x more valuable than likes
- **Hashtags:** 3-5 niche hashtags IN the caption. Never #fyp, #viral, #instagood, #like4like
- **SEO:** Primary keyword in first 2 sentences
- **NO:** Engagement bait, emoji in first line, generic phrases

### INSTAGRAM CAROUSEL
- **Length:** 300-900 characters. Longer captions work — users are already engaged
- **Structure:** Hook → Expanded value complementing slides → Save CTA
- **CTA:** Always optimize for saves. Carousels with save CTA get +68% saves
- **Body:** Lists, step-by-step, checklists. Structure with emoji-bullets or numbers

### TIKTOK
- **Length:** 100-300 characters. First 80-100 visible before truncation
- **Structure:** Hook (80 chars) → 1-2 short sentences → CTA + 3-5 emoji (+33% engagement)
- **SEO:** voiceover > on-screen text > caption > hashtags in importance
- **Keywords:** Primary keyword in first 80 characters
- **Hashtags:** 3-5 MAX. Formula: 1 niche + 1 thematic + 1 trending. NEVER #fyp, #foryou, #viral
- **Completion rate:** 70%+ needed for viral. Caption should tease "watch till end"
- **CTA:** Questions get +44% comments

### THREADS
- **Length:** 100-280 characters optimal (max 500)
- **Structure:** One clear idea. Conversational tone, like a group chat
- **Tags:** Only 1 topic tag per post. Can be multi-word. Niche > broad
- **Engagement:** Replies are #1 signal. Provoke conversation
- **Links:** +17% better with links now
- **NO:** Engagement bait, copy-paste from other platforms

### FACEBOOK (Page/Personal)
- **Length:** 40-80 characters for max reach
- **Structure:** Ultra-short hook → value → discussion question
- **Hashtags:** 0-2 max or none
- **AVOID trigger words:** "buy now", "limited time", "FREE" (caps), "click here", "money", "income", "contest", "giveaway"
- **SAFE replacements:** "available today", "complimentary", "no cost", "learn more"
- **Trigger word impact:** 1-2 words = -20-30% reach, 3-5 = -40-60%, 6+ = -70-95%

### FACEBOOK GROUP
- **Length:** 200-500 characters. Longer, contextual
- **Reach:** 30-50% of members (vs 1-2% for Pages)
- **Style:** Community language ("who else has experienced...", "curious what you all think about...")
- **CTA:** Open discussion questions

### YOUTUBE SHORTS
- **Title length:** 40-70 characters optimal (max 100). Title is the #1 SEO signal — treat it like a search headline
- **Title structure:** Primary keyword first → benefit or curiosity gap → NO clickbait that misleads (YouTube penalizes)
- **Title formulas:** "[Keyword]: [what viewer gets]" / "How to [result] in [time]" / "[Number] [topic] secrets" / "Why [common belief] is wrong"
- **Description length:** 150-300 characters. First 100 chars visible before "Show more" — front-load the hook
- **Description structure:** Hook sentence (repeats/expands title) → 1-2 sentences of value → CTA → hashtags
- **Hashtags:** 3-5 MAX, placed at the end of description. 1 broad category + 1-2 niche + 1 topic-specific. NEVER #shorts as the only tag
- **SEO:** YouTube is a search engine. Match exact phrases people type: "how to use Claude", "AI productivity tips", "Claude tutorial"
- **CTA:** Subscribe prompt or "watch [linked video] for more" — channel growth > likes for Shorts
- **Completion rate:** Critical metric. Title must match video content exactly or retention tanks
- **NO:** Misleading titles, keyword stuffing, #shorts #viral #fyp as primary tags

---

## CAPTION FORMULAS (use different ones for each platform)

### For REACH:
1. **Trend-Hook + Benefit:** "[Trend]: [benefit] for [audience]"
2. **Curiosity X vs Y:** "[A] vs [B]: which works better for [situation]?"
3. **Myth Buster:** "Myth: [belief]. Fact: [rebuttal]"
4. **Data Teaser:** "[Number] → [result] in [timeframe]"
5. **What Works Now:** "What works in [niche] right now: [1], [2], [3]"
6. **Watch Time Promise:** "In [time] you'll get [result]. Watch till end for [bonus]"
7. **Beginner's Path:** "New to [field]: [step 1] → [step 2] → [step 3]"

### For ENGAGEMENT:
1. **Micro-Decision:** "Are you [A] or [B]? And why?"
2. **Hot Take:** "Unpopular opinion: [thesis]. Agree or disagree?"
3. **Rate These 3:** "Rank by importance: [1], [2], [3]"
4. **This or That:** "[A] or [B] for [situation]? Your pick?"
5. **Fill in the Blank:** "Complete: 'The best advice about [topic] was...'"

### For SALES:
1. **POOPC:** "Struggling with [pain]? [Result] in [time] with [product]"
2. **Objection Destroyer:** "Think '[objection]'? Actually, [counter]"
3. **Social Proof Lead:** "'[Client quote]'. Here's how:"

### For GROWTH:
1. **Series:** "Day [1/N] of [topic]. Follow for the rest"
2. **Lead Magnet:** "Free [resource] on [topic]. Comment '[word]'"
3. **Cliffhanger:** "10 lessons from [experience]. Today — first 3. Rest for followers"

---

## HOOK LIBRARY

### Provocation:
- "Stop [doing X] — here's why"
- "Nobody talks about this [topic] hack"
- "You're doing [X] wrong"

### Curiosity:
- "Here's what no one tells you about [topic]"
- "Mistake #1 that kills your [metric]"
- "I tested [N] methods. These [N] destroyed the rest"

### Value:
- "Save this for when [situation]"
- "[N] things I wish I knew before [X]"
- "In [time] I [result]. Here's how:"

### Emotion:
- "Honest truth about [topic]"
- "One change flipped everything"
- "Unpopular opinion: [bold claim]"

### TikTok-specific:
- "Stop scrolling if you [situation]"
- "POV: you just discovered [thing]"
- "I spent [money/time] so you don't have to"

---

## ANTI-PATTERNS (NEVER do these)

### Engagement bait (-50-90% reach on ALL platforms):
- "Like if you agree" / "Comment YES" / "Tag 3 friends"
- "Share before they delete this" / "React with [emoji] if..."

### Shadow ban triggers:
- Violence words (even in innocent context)
- Adult content words
- Financial promises: "guaranteed income", "get rich quick"
- Letter substitution does NOT help — algorithms detect it

### Format crimes:
- Same hashtags on every post (spam flag)
- More than 5 hashtags (limit since late 2025)
- Wall of text without line breaks
- #fyp, #foryou, #viral — useless and harmful

---

````

### 3.3 coreyhaines31/marketingskills — `social`, `ad-creative`, `ads`, `image` (DEĞER 5)

Link: https://github.com/coreyhaines31/marketingskills · 53326★ · MIT · 50 skill. Kopya: `kopyalar/coreyhaines31-marketingskills/` (ilgili alt küme).

Kurulum: `npx skills add coreyhaines31/marketingskills --skill social ad-creative ads image copywriting`. Skill'ler önce `.agents/product-marketing.md` dosyasını okur — bizim müşteri brief dosyamızı bu ada koymak yeterli.

**`skills/social/references/carousel-frameworks.md` — Framework A-C (aynen, kaynak: https://github.com/coreyhaines31/marketingskills/blob/main/skills/social/references/carousel-frameworks.md):**

````markdown
# Carousel Frameworks

Five slide-by-slide narrative architectures for carousels — Instagram carousels and LinkedIn document posts. Each framework is a different *reason to keep swiping*; picking the right one for the content matters more than polishing individual slides.

A carousel is not a blog post chopped into squares. It's a swipe-through with two jobs per slide: deliver one idea, and make the next swipe irresistible. These frameworks encode structures that show up repeatedly in high-performing carousels; treat them as starting structures to adapt, not rigid formulas — and expect to validate against your own analytics.

## Picking a Framework

| Your content is... | Use | Why |
|---|---|---|
| A list of resources, tools, or tips | **A: Value-Stack** | Completeness is the promise; the count is the hook |
| A personal result with a system behind it | **B: Problem-Proof** | Proof opens and closes the loop; the system is the meat |
| Several named techniques on one theme | **C: Hack List** | Each hack re-earns the swipe independently |
| A strong opinion about a common practice | **D: Rant Callout** | Conviction is the content; structure keeps it fair |
| A product or workflow you can show | **E: Demo Walkthrough** | Seeing the steps is more persuasive than describing them |

Two cross-framework rules before the specifics:

- **Slide 1 is the thumbnail.** It competes in the feed alone, before anyone knows a carousel follows. Design and write it as a standalone scroll-stopper.
- **One visual template per carousel.** Same layout, type scale, and palette on every interior slide. Variety between slides reads as clutter; consistency lets the *content* change while the frame stays still.

---

## Framework A: Value-Stack (4–14 slides)

The "everything you need" carousel. Works because the cover makes a completeness claim — an exact count and an exact deliverable — and every swipe pays it down.

| Slide | Job | Pattern |
|---|---|---|
| 1 — Cover | State the exact count + exact deliverable. Specificity proves scale upfront. | "[N] [resource type] for [role/outcome]" |
| 2 to N−1 — Value delivery | One item or category per slide, 3–6 concrete sub-items each. Same visual template, zero filler slides. | "[Category/item]: [sub-items]" |
| N — Close | Convert the swipe-through into an action. | "[Action] for [the payoff]" — comment a keyword, follow, link in bio |

**Why the exact count matters:** "27 free tools" outperforms "the best free tools" because a number is a checkable promise — the reader can verify you delivered. Rounding up with padding breaks the trust the count created.

*SaaS example:* "12 ChatGPT prompts for SaaS onboarding emails" → one prompt per slide with the use case → "Comment PROMPTS and I'll send the full doc."

**Failure mode:** filler slides to hit a bigger number. Cut to the real count; a tight 8 beats a padded 14.

---

## Framework B: Problem-Proof (6–10 slides)

The "I did X, here's the system" carousel. The hook is a *result stated as fact*, not advice — and the final slide shows the receipt, closing the loop slide 1 opened.

| Slide | Job | Pattern |
|---|---|---|
| 1 — Hook | A specific personal claim with a number. A result, not a tip. | "[I/we did X]. [Specific result, with number]." |
| 2 — Reframe the problem | Name what's *actually* going wrong so the reader recognizes themselves. | "The real problem: [named issue]" |
| 3–4 — The mechanism | Show the real system: named tools, named steps. Concrete beats vague. | "The system: [named tools/steps]" |
| 5 to N−1 — The detail | The literal prompt text, template, or step-by-step. This is the save-worthy part. | "[The literal prompt or process detail]" |
| N — Proof | The actual output artifact. A screenshot, not a description. | "[Screenshot of output]: the receipt" |

**The open loop is the engine:** slide 1 makes a claim, the last slide proves it, and everything between explains how. Readers swipe to see whether the receipt is real.

*SaaS example:* "We cut churn 22% with one onboarding email." → the real problem (activation, not price) → the sequence structure → the literal email copy → the retention chart.

**Failure mode:** vague mechanism slides ("optimize your workflow") — if the middle slides don't name tools and steps, the proof slide reads as luck, not system.

---

## Framework C: Hack List (6–10 slides)

The "numbered techniques" carousel. A contrarian cover creates status anxiety — most people are doing this wrong — and each numbered hack independently re-earns the swipe.

| Slide | Job | Pattern |
|---|---|---|
| 1 — Contrarian hook | A stat or claim implying most people fail at this. | "[Stat implying most people fail at X]" |
| 2 — The problem | Why the common approach fails, ideally with an analogy. | "The problem: [why it fails]" |
| 3 to N−1 — Numbered hacks | One named technique per slide. Bad-vs-good contrast makes each hack land instantly. | "Hack #[n]: [named technique]" |
| N — Synthesis + close | A one-line thesis tying the hacks together, then a save/share/follow CTA. | "[Thesis tying hacks together]" + [CTA] |

**Name every technique.** "The 3-second rule" is shareable and memorable; "keep it short" is neither. Named techniques travel — people repeat them and credit the source.

*SaaS example:* "90% of trial emails never get opened." → why (they read like receipts, not messages) → Hack #1: The founder-from line, Hack #2: The one-question subject, ... → "Trial emails are conversations, not confirmations. Save this for your next sequence."

**Failure mode:** hacks that are restatements of each other. Each slide must survive alone — if two hacks collapse into one idea, merge them and go shorter.

---

## Framework D: Rant Callout (4–8 slides)

The "someone had to say it" carousel. Personality-led and polarizing by design — the structure exists to keep the heat *fair* so it reads as conviction, not bitterness.

| Slide | Job | Pattern |
|---|---|---|
| 1 — Provocative claim | An unpopular opinion or direct accusation about a common practice. | "Unpopular opinion: [common practice]" |
| 2 to N−2 — Escalate the argument | Sensory, specific detail. Show the offense; don't gesture at it abstractly. Each slide raises the stakes. | "[Escalating, specific complaint]" |
| N−1 — Fairness pivot | "Don't get me wrong…": clarify what you're *not* attacking. Anti-laziness, not anti-tool; anti-practice, not anti-person. | "I use [X] every day. My problem is [Y]." |
| N — Close | Firm, personality-forward sign-off. Signed rants read as owned opinions; anonymous ones read as potshots. | "[Firm sign-off]: signed [name/persona]" |

**The fairness pivot is what makes it work.** Without it you're yelling; with it you're drawing a precise line, and precise lines get quoted. It also pre-empts the top hostile comment.

*SaaS example:* "Your AI-generated LinkedIn posts are costing you customers." → the specifics (same em-dash cadence, same 'game-changer' vocabulary, zero lived detail) → "Don't get me wrong. I use AI daily. My problem is publishing the first draft." → "Write like you talk. [Name]"

**Failure modes:** skipping the pivot (reads unhinged, invites pile-ons), or ranting about something your own product/content visibly does (the comments will find it).

---

## Framework E: Demo Walkthrough (5–11 slides)
````

**`skills/ad-creative/SKILL.md` — Grounded Inputs + Meta specs (aynen):**

````markdown
## Grounded Inputs

Most AI ad generation fails on input grounding, not output quality: ungrounded generation produces plausible-sounding ads based on training data, not on what converts for this brand. For scaled production (Mode 3), maintain a durable inputs corpus:

```
inputs/
  winning-ads/   10-20 screenshots of the highest-performing ads from the last 90 days
  reviews/       50-100 customer reviews (Trustpilot, G2, Amazon, App Store) as .md/.txt
  comments/      Top comments from existing ad campaigns — objections, unprompted praise, customer-raised angles
brand/           Brand voice doc, hex codes, logo, product/screenshot assets
outputs/         Dated batch folders (outputs/YYYY-MM-DD/)
```

**Why each input matters:**
- **Winning ads** carry the hooks, structures, and angles already proven for this brand
- **Reviews** carry the exact language buyers use for pain, transformation, and unexpected benefits — pull copy from them verbatim rather than paraphrasing
- **Ad comments** are the most-skipped and highest-value input: objections ("but does it work for X?") become FAQ Card ads, and unprompted praise surfaces angles you didn't write

**Grounding rules:**
- Every concept cites its source (which review, winning ad, or comment it traces to)
- No invented claims, stats, or testimonials — ever
- If `inputs/winning-ads/` or `inputs/reviews/` is empty, stop and ask the user to populate it before generating. Do not generate ungrounded concepts as a fallback.
- Inputs decay: refresh `inputs/winning-ads/` as new ads scale; refresh `inputs/reviews/` and `inputs/comments/` monthly

---

## Platform Specs

Platforms reject or truncate creative that exceeds these limits, so verify every piece of copy fits before delivering.

### Google Ads (Responsive Search Ads)

| Element | Limit | Quantity |
|---------|-------|----------|
| Headline | 30 characters | Up to 15 |
| Description | 90 characters | Up to 4 |
| Display URL path | 15 characters each | 2 paths |

**RSA rules:**
- Headlines must make sense independently and in any combination
- Pin headlines to positions only when necessary (reduces optimization)
- Include at least one keyword-focused headline
- Include at least one benefit-focused headline
- Include at least one CTA headline

### Meta Ads (Facebook/Instagram)

| Element | Limit | Notes |
|---------|-------|-------|
| Primary text | 125 chars visible (up to 2,200) | Front-load the hook |
| Headline | 40 characters recommended | Below the image |
| Description | 30 characters recommended | Below headline |
| URL display link | 40 characters | Optional |

````

**`skills/ads/references/meta-decision-system.md` — TCPL ve iki-kampanya yapısı (aynen, ilk bölümler):**

````markdown
# Meta Decision System (B2B)

A quantified kill/keep/scale engine for Meta ads. Every threshold derives from one anchor number, so decisions become arithmetic instead of vibes. Pairs with the strategy-level Meta playbook in SKILL.md (creative-as-targeting, creative volume) — this file is the *operating* layer.

## Contents

- TCPL: the anchor variable
- The ad-count ceiling
- Two-campaign structure (Scaling / Testing)
- Destination testing (CBO per persona, one ad set per destination)
- Stage 1: delivery check (day 7)
- Stage 2: quality evaluation (weekly)
- Graduation criteria
- Fatigue detection
- Swap rules
- Creative production math
- Scaling protocol
- Weekly cadence
- Lead forms and social amnesia
- Advantage+ transition
- Partnership ads (the net-new-reach lever)
- Rolling reach as a health signal
- Benchmarks and seasonality

## TCPL: the anchor variable

TCPL = **Target Cost Per Qualified Lead** (qualified = meets your ICP bar, not just a form-fill). Set it one of three ways:

1. **From deal math (best):** TCPL = target cost per demo × qualified-lead-to-demo rate. ($2,000/demo × 0.28 = $560.)
2. **From history:** TCPL = trailing 30-day CPL(qualified) × 0.80 — a 20% improvement is achievable through operational cleanup alone (killing zero-QL ads, graduating winners). Once you have both, use whichever is tighter.
3. **New account:** target CAC × qualified-lead-to-customer rate, or a placeholder from your ACV tier; replace with method 2 after 30 days.

Every rule below is expressed in multiples of TCPL. Review TCPL monthly.

## The ad-count ceiling

More active ads than your budget can feed = every ad starves and nothing gets a fair read.

**Ceiling = (daily budget × 14) / (2 × TCPL)** — i.e., over a 14-day evaluation window, each ad needs at least 2× TCPL of spend to be judged.

$1,000/day at $500 TCPL → ceiling of 14 ads; run **6–10** (winners + 2–3 test slots). At the ceiling, launching a new test requires killing something first.

## Two-campaign structure (Scaling / Testing)

Run two CBO campaigns over the **same audience**:

- **Scaling campaign (~80% of budget)** — holds only graduated, proven ads.
- **Testing campaign (~20%)** — holds new concepts and iterations, with its own protected budget.

Why: inside a single CBO, proven ads always starve new ads — tests never get enough spend to be judged. Why not ABO for testing: equal forced distribution keeps spending on ads Meta has already deprioritized. The separation is *budget protection*, not audience segmentation.

**Image-first validation:** launch new concepts as statics first; only produce the video/carousel/UGC version after the image passes the checks below. Exception: concepts that are inherently video (testimonial, demo, UGC).

## Destination testing (CBO per persona, one ad set per destination)

A complementary structure for when the **lander, not the creative, is the biggest unknown**: one CBO per persona; inside it, one ad set per destination type — PDP, listicle/advertorial, quiz, demo page — with the **same creatives in every ad set**. Holding creative constant makes the read clean: any CPM or performance divergence between ad sets is the destination.

Why it works: the destination is a test axis of the same rank as creative — a losing funnel can hide winning creative, and different personas convert through different funnel shapes. CBO allocates budget across destinations the way it allocates across ads, and practitioners running this report wide CPM/performance spreads between destinations plus meaningful new-reach gains (~30%) from the added variety.

Fit with the two-campaign structure: treat a destination test like a concept test — run it in the Testing campaign with a protected budget, judge each ad set against TCPL at the usual spend gates, then graduate the winning creative × destination pair. *Practitioner-reported pattern (Alexander Pauwelyn, 2026), not a platform-documented mechanic — validate against your own account data.*
````

**`skills/image/SKILL.md` — model karşılaştırma tablosu (aynen):**

````markdown
### Model Comparison

| Model | Best For | Text in Images | API | Cost |
|-------|----------|:-:|-----|------|
| **Gemini Image** (Google, "Nano Banana" / Nano Banana Pro) | All-around, editing, multi-image reference, text rendering | Good | [Gemini API](https://ai.google.dev/gemini-api/docs/image-generation) | Check [pricing](https://ai.google.dev/gemini-api/docs/pricing) |
| **Flux** (Black Forest Labs — Pro 1.1, Kontext, Dev, Schnell) | Photorealism, brand consistency, batch; Kontext for in-image editing | Limited | [BFL API](https://docs.bfl.ai/), Replicate, fal.ai | Check [pricing](https://docs.bfl.ai/quick_start/pricing) |
| **Ideogram 3.0** | Typography, branded graphics, accurate text rendering | Best | [Ideogram API](https://developer.ideogram.ai/) | Check [pricing](https://about.ideogram.ai/api-pricing) |
| **ChatGPT Images 2.0 / GPT Image** (OpenAI) | General purpose, ChatGPT integration, native editing | Good | [OpenAI API](https://platform.openai.com/docs/guides/image-generation) | Check [pricing](https://platform.openai.com/docs/pricing) |
| **Midjourney v7** | Artistic, high-aesthetic, art-directed visuals | Improved | No official API; Discord + Web | Subscription-based |
| **Recraft V3** | Vector + brand-consistent illustrations, design assets | Strong | [Recraft API](https://www.recraft.ai/docs) | Per-credit |
| **Stable Diffusion 3.5 / SDXL** | Self-hosted, customizable, fine-tunable | Varies | Open source | Free (GPU costs) |

**Note:** DALL-E 3 is fully deprecated. OpenAI's current image models are the GPT Image family. `gpt-image-1` retires on 23 Oct 2026 and `gpt-image-1-mini` and `gpt-image-1.5` on 1 Dec 2026; OpenAI's recommended replacements are `gpt-image-2.5-sunburst` and `gpt-image-2.5-flare` (check OpenAI's deprecations page for the current list).

````

### 3.4 marcolang/Marketing-Skills `instagram-carousel` — HTML → Playwright PNG (DEĞER 5)

Link: https://github.com/marcolang/Marketing-Skills · 64★ · lisans belirtilmemiş · PT-BR odaklı ama teknik kısım evrensel. Kopya: `kopyalar/marcolang-Marketing-Skills/instagram-carousel/SKILL.md`.

Özet: Marka bilgisi topla → tek renkten 6-token palet → Google Fonts tipografi çifti → 7 slayt dizisi (Hero/Problem/Solution/Features/Details/How-to/CTA; Listicle, Tutorial, Comparação varyantları) → `.ig-frame` 420px sabit HTML önizleme → onay → Playwright `device_scale_factor=1080/420` ile 1080×1350 PNG. Görseller base64 `data:` URI olarak gömülür; HTML Python `Path.write_text()` ile yazılır (shell heredoc base64'ü bozar).

**Renk türetme + tipografi + Slayt 1 kuralları (aynen, kaynak: https://github.com/marcolang/Marketing-Skills/blob/main/instagram-carousel/SKILL.md):**

````markdown
## Step 2: Derive the Full Color System

From the user's **single primary brand color**, generate the full 6-token palette:

```
BRAND_PRIMARY   = {user's color}                    // Main accent — progress bar, icons, tags
BRAND_LIGHT     = {primary lightened ~20%}           // Secondary accent — tags on dark, pills
BRAND_DARK      = {primary darkened ~30%}            // CTA text, gradient anchor
LIGHT_BG        = {warm or cool off-white}           // Light slide background (never pure #fff)
LIGHT_BORDER    = {slightly darker than LIGHT_BG}    // Dividers on light slides
DARK_BG         = {near-black with brand tint}       // Dark slide background
```

**Rules for deriving colors:**
- LIGHT_BG: tinted off-white complementing the primary (warm → warm cream, cool → cool gray-white)
- DARK_BG: near-black with subtle brand tint (warm → #1A1918, cool → #0F172A)
- LIGHT_BORDER: always ~1 shade darker than LIGHT_BG
- Brand gradient: `linear-gradient(165deg, BRAND_DARK 0%, BRAND_PRIMARY 50%, BRAND_LIGHT 100%)`

---

## Step 3: Set Up Typography

Based on the user's font preference, pick a **heading font** and **body font** from Google Fonts.

| Style | Heading Font | Body Font |
|-------|-------------|-----------|
| Editorial / premium | Playfair Display | DM Sans |
| Modern / clean | Plus Jakarta Sans (700) | Plus Jakarta Sans (400) |
| Warm / approachable | Lora | Nunito Sans |
| Technical / sharp | Space Grotesk | Space Grotesk |
| Bold / expressive | Fraunces | Outfit |
| Classic / trustworthy | Libre Baskerville | Work Sans |
| Rounded / friendly | Bricolage Grotesque | Bricolage Grotesque |

**Font size scale (fixed across all brands):**
- Headings: 28–34px, weight 600, letter-spacing -0.3 to -0.5px, line-height 1.1–1.15
- Body: 14px, weight 400, line-height 1.5–1.55
- Tags/labels: 10px, weight 600, letter-spacing 2px, uppercase
- Step numbers: heading font, 26px, weight 300
- Small text: 11–12px

Apply via CSS classes `.serif` (heading font) and `.sans` (body font) throughout all slides.

---

## Slide 1 — Hook Rules

The first slide must stop the scroll in under 1 second. Prioritize these formats:

| Hook format | Example |
|---|---|
| Afirmação polêmica | "Você está usando IA errado" |
| Número + benefício | "7 ferramentas que substituem seu designer" |
| Pergunta que dói | "Por que seus carrosséis têm 0 salvamentos?" |
| Resultado concreto | "Esse post gerou 4.200 seguidores em 3 dias" |
| Inversão de expectativa | "Mais esforço no design = menos alcance" |

**Rules:**
- Never start with the brand name as headline
- Visual proof on Slide 1 whenever possible (screenshot, result, real number)
- Hook must promise value that the following slides deliver

---

````

**Export scripti (aynen):**

````markdown
## Exporting Slides as Instagram-Ready PNGs

After the user approves the carousel preview, export each slide as an individual **1080×1350px PNG**.

### Critical Export Rules

1. **Use Python for HTML generation** — never use shell scripts with variable interpolation. Always use `Path.write_text()` or `open().write()`.

2. **Embed images as base64** — all user-uploaded images must be base64-encoded as `data:image/jpeg;base64,...` URIs. Check actual file format with the `file` command — a `.png` extension may contain a JPEG.

3. **Keep the 420px layout width** — use Playwright's `device_scale_factor` to scale up to 1080px output WITHOUT changing the layout viewport.

### Install Playwright (only if needed)

Before running the export script, check and install only if missing:

```bash
python3 -c "import playwright" 2>/dev/null || pip3 install playwright
python3 -c "from playwright.sync_api import sync_playwright; sync_playwright().__enter__().chromium" 2>/dev/null || python3 -m playwright install chromium
```

### Export Script

```python
import asyncio
from pathlib import Path
from playwright.async_api import async_playwright

INPUT_HTML = Path("/path/to/carousel.html")
OUTPUT_DIR = Path("/path/to/output/slides")
OUTPUT_DIR.mkdir(exist_ok=True)

TOTAL_SLIDES = 7  # Update to match your carousel

VIEW_W = 420
VIEW_H = 525
SCALE = 1080 / 420  # = 2.5714...

async def export_slides():
    async with async_playwright() as p:
        browser = await p.chromium.launch()
        page = await browser.new_page(
            viewport={"width": VIEW_W, "height": VIEW_H},
            device_scale_factor=SCALE,
        )

        html_content = INPUT_HTML.read_text(encoding="utf-8")
        await page.set_content(html_content, wait_until="networkidle")
        await page.wait_for_timeout(3000)  # Wait for Google Fonts to load

        # Hide IG frame chrome, show only the slide viewport
        await page.evaluate("""() => {
            document.querySelectorAll('.ig-header,.ig-dots,.ig-actions,.ig-caption')
                .forEach(el => el.style.display='none');

            const frame = document.querySelector('.ig-frame');
            frame.style.cssText = 'width:420px;height:525px;max-width:none;border-radius:0;box-shadow:none;overflow:hidden;margin:0;';

            const viewport = document.querySelector('.carousel-viewport');
            viewport.style.cssText = 'width:420px;height:525px;aspect-ratio:unset;overflow:hidden;cursor:default;';

            document.body.style.cssText = 'padding:0;margin:0;display:block;overflow:hidden;';
        }""")
        await page.wait_for_timeout(500)

        for i in range(TOTAL_SLIDES):
            await page.evaluate("""(idx) => {
                const track = document.querySelector('.carousel-track');
                track.style.transition = 'none';
                track.style.transform = 'translateX(' + (-idx * 420) + 'px)';
            }""", i)
            await page.wait_for_timeout(400)

            await page.screenshot(
                path=str(OUTPUT_DIR / f"slide_{i+1}.png"),
                clip={"x": 0, "y": 0, "width": VIEW_W, "height": VIEW_H}
            )
            print(f"Exported slide {i+1}/{TOTAL_SLIDES}")

        await browser.close()

asyncio.run(export_slides())
```

### Why This Works

- **`device_scale_factor=2.5714`** renders at high DPI — a 420px element becomes 1080px in the output. Layout stays at 420px.
- **`clip`** captures only the carousel viewport, not browser chrome.
- **`wait_for_timeout(3000)`** gives Google Fonts time to load.
- **`track.style.transition = 'none'`** disables swipe animation so slides snap instantly.

### Common Export Mistakes to Avoid

| Mistake | What goes wrong | Fix |
|---------|----------------|-----|
| Setting viewport to 1080×1350 | Layout reflows — fonts tiny, spacing breaks | Keep viewport at 420×525, use `device_scale_factor` |
| Using shell scripts to generate HTML | `$` signs and backticks get interpolated | Always use Python for HTML generation |
| Not waiting for fonts | Headings render in fallback system fonts | `wait_for_timeout(3000)` after page load |
| Not hiding IG frame chrome | Export includes header, dots, caption | Hide `.ig-header,.ig-dots,.ig-actions,.ig-caption` |
| Changing `.ig-frame` width | Entire layout shifts | Always keep at exactly 420px |
| Leaving `BRAND_PRIMARY` as variable name in CSS | Color renders as invalid / invisible | Always interpolate actual hex values into HTML |

---

## Design Principles

1. **Every slide is export-ready** — arrow and progress bar are part of the slide image
2. **Light/dark alternation** — creates visual rhythm across swipes
3. **Heading + body font pairing** — display font for impact, body for readability
4. **Brand-derived palette** — all colors stem from one primary, keeping everything cohesive
5. **Progressive disclosure** — progress bar fills and arrow guides forward
6. **Last slide is special** — no arrow, full progress bar, clear CTA
7. **Consistent components** — same tag style, list style, spacing across all slides
8. **Content padding clears UI** — body text never overlaps progress bar or arrow
9. **Hook-first copy** — Slide 1 exists to stop the scroll, not to introduce the brand
10. **Iterate fast** — show preview, fix specific slides, don't rebuild from scratch
````

### 3.5 Ash-Harris/carousel-generator — 16 tasarım kuralı + export (DEĞER 5)

Link: https://github.com/Ash-Harris/carousel-generator · 4★ · lisans yok · `npx skills add Ash-Harris/carousel-generator`. Kopya: `kopyalar/Ash-Harris-carousel-generator/SKILL.md` (49 KB, tam metin).

Özet: marcolang'ın geliştirilmiş hali. Hook aşamasında 3 varyant yan yana statik grid; sonra `preview-template.html` ile swipe önizleme; 3:4 (1080×1440) format (profil grid'i 3:4 kırpar); `export.mjs` (node) / `export.py`. En değerli kısım "NON-NEGOTIABLE RULES": her slaytı screenshot'la doğrula, display font fallback'e düşmesin, başlık en uzun satırı slayt genişliğinin %75-90'ı, yetim satır yok, simetrik satır kırma, ölü alan yok, kenar boşluğu 56px (100px+ asla), bir slayt bir fikir.

**Kural başlıkları + EXPORT RULE (aynen):**

````markdown
## NON-NEGOTIABLE RULES

Apply on the FIRST DRAFT, every time, without being asked.

### 1. ALWAYS screenshot each slide before declaring it done

After ANY structural edit (new slide, copy overhaul, layout change, font swap, padding change), screenshot the affected slide with a headless browser and READ the saved PNG before reporting back. Shipping unverified changes wastes hours of the user's time.

Any headless-browser screenshot tool works (a small Playwright script, a browser CLI, whatever is available). The pattern is fixed:

1. Make the edit
2. Load `carousel.html` in the headless browser (serve it over HTTP and cache-bust the URL with `?cb=<timestamp>` so you never screenshot a stale version)
3. Move the track to the affected slide: `document.getElementById('track').style.transition='none'; document.getElementById('track').style.transform='translateX(-420 * (N-1))px';`
4. Screenshot to a versioned filename (e.g. `screenshots/v3-slide-6.png`)
5. Read the PNG and inspect it
6. ONLY THEN report "done" or iterate

If it looks broken, fix it before claiming done. Don't make the user catch your mistakes.

...
### 9. HEADLINES MUST FILL SLIDE WIDTH (longest line at 75-90% of slide width)

Left-aligned headlines look bad when the longest line only fills 50-60% of the slide; it reads as "stuffed into the corner". The longest line must reach near the right edge of the content area.

How to dial it in:

1. Identify the longest line (by char count) among the lines you want
2. Calculate target font size: at Anton, `font_px = slide_content_width / longest_chars / 0.32`. At Archivo Black, divide by 0.55 instead.
3. If the calculated size causes shorter lines to wrap, drop 2-4px until wrapping stops.
4. Verify via screenshot: the longest line should reach within ~20px of the right edge.

If you can't get within range without breaking other lines, **change the line breaks** (combine two short lines into one, or split one long line into two). Don't ship a 50%-width headline.

### 10. NO ORPHAN LINES: never let a text block wrap to a final line of 1-2 words

A heading, subhead, body paragraph, prompt line, or bullet must NEVER end on a dangling line of one or two words. It reads as messy and unfinished. Applies to EVERY text element.

Fixes, in priority order:

1. **Resize** so it fits on one full line (preferred for short headlines)
2. **Rebalance the break** so both lines are multi-word and similar length
3. **Keep the tail together** with `white-space:nowrap` on the last 3-4 words so the wrap happens earlier and the final line is multi-word
4. Deliberate one-word-per-line display headlines are fine, but only as a clear design choice across ALL lines, never one orphaned tail word

After building any slide, scan every text block's last line. If it's 1-2 words, fix it before showing the user.

...
### 14. Fill the canvas edge-to-edge with massive display typography

Strong carousels use ~95% of the slide width with display typography where the headline alone occupies 50-65% of the slide vertically. Decorative shapes anchor the corners. No timid margins.

Apply on the first draft, every slide (values for a 1080x1440 native frame):

- **Stage padding**: `padding: 56px 56px 160px` or tighter. NEVER 100px+ side padding on a 1080-wide slide.
- **Display typography**: hero headlines at 96-112px, weight 700-800, letter-spacing -2 to -3px, line-height 1.0-1.05. NOT 40-60px.
- **Highlight boxes**: span 90-100% of available width with text centered; don't auto-size to text only.
- **Decorative anchors**: large accent-color shapes in corners or behind text at 0.6-1.0 opacity, with `z-index:0` and positioned so they never overlap headline or CTA text.
- **Border frames**: thin (1-2px) border plus corner notch indicators give the slide a designed feel.

Fail mode to avoid: a 60px heading with 30% of the slide as blank background. That's a draft, not a finished design.

### 15. ONE IDEA PER SLIDE, audience-first copy

Each slide carries exactly one idea: one claim, one step, one proof. If a slide is trying to say two things, split it.

Write for the audience the user is posting to, not for yourself. If their audience is non-technical, reject jargon in headlines and key copy:

- "Write HTML" becomes "Just upload a raw video"
- "Skill / Library / Framework / SDK" becomes "Tool" or "Free tool"
- "Render to MP4" becomes "Get back a finished video"

When in doubt, write the line as if explaining to a busy person in the target audience who has never opened a terminal.

### 16. Hook slide principles

- The first slide must stop the scroll. Lead with a value proposition or bold claim, not a description.
- Use visual proof (screenshots, real numbers, images) to immediately validate the hook.
- Prefer evergreen freshness signals ("JUST RELEASED", "NEW THIS MONTH") over specific dates, which feel stale within weeks. Only use a date when the date itself is the news.
- A prominent centered "SWIPE TO SEE HOW IT WORKS" line in the accent color is a net positive on hero slides, even though the chevron and progress bar also signal it.

---

## EXPORT RULE

**When the user asks to export, download, or get the final PNGs, ALWAYS do this.** Two equivalent exporters ship with this skill; prefer node (it avoids Python environment issues).

First, confirm the exporter can actually run (this should already be true from Step 0A; it is a one-line check, so never skip it):

```bash
node -e "import('playwright').then(()=>console.log('playwright ok')).catch(()=>process.exit(1))"
```

If it is missing, install it right there (`npm install playwright && npx playwright install chromium`) and warn the user it downloads a browser once. Never report an export failure to the user without first checking whether this was the cause.

```bash
# Node (recommended). Run from <carousel_dir> so playwright resolves.
cp .claude/skills/carousel-generator/export.mjs <carousel_dir>/
cd <carousel_dir> && node export.mjs

# Python fallback (needs `pip install playwright` + `python3 -m playwright install chromium`)
cp .claude/skills/carousel-generator/export.py <carousel_dir>/
cd <carousel_dir> && python3 export.py
```

Either runs in ~10 seconds and writes `slide_1.png` ... `slide_N.png` at 1080x1440 into `./slides/`. Both auto-detect the slide count from the HTML.

**DO NOT** reinvent the export with manual browser screenshots, CSS `zoom`, `transform: scale`, or image-crop loops. Browser screenshot tools have unstable `devicePixelRatio` behavior and will waste an hour giving you wrong-sized or black output. The standalone script works because it sets `device_scale_factor = 1080/420` directly on the browser context.

Headless-browser screenshots are ONLY for visual iteration (checking a slide looks right mid-design). They are never the export path.

**Verify every export by reading the PNG files.** A screenshot tool's inline preview is rescaled for display and does NOT reflect what's in the saved file. Read each `slide_N.png` to confirm content fills the frame before telling the user it's done.

---

````

### 3.6 idrsdev/social-carousel-generator — design-system.json → PNG (DEĞER 5)

Link: https://github.com/idrsdev/social-carousel-generator · 2★ · MIT · Claude Code plugin. Kopya: `kopyalar/idrsdev-social-carousel-generator/` (SKILL.md + şema + pipeline + font kurulumu).

Özet: Bizim "marka tutarlılığı" sorununu çözen yaklaşım: tüm görsel kararlar `design-system.json` içinde (colors, typography.scale px, css_toolkit string'leri, svg_patterns, ghost_numbers, canvas 1080×1350 padding 96/88). Ajan `_generate.py` yazar, Playwright `#s1` elementini screenshot'lar. Fontlar `fonts.json` base64 (headless'ta ağ fontu beklenmez). Müşteri başına bir JSON = tutarlı seri.

**SKILL.md — Base Slide HTML + Playwright (aynen, kaynak: https://github.com/idrsdev/social-carousel-generator/blob/main/skills/social-carousel-generator/SKILL.md):**

````markdown
## Font Face CSS

Fonts are embedded as base64 — never use network URLs in headless Playwright.

```python
def build_font_css(DS: dict, F: dict) -> str:
    primary    = DS["typography"].get("font_primary",    "Inter")
    annotation = DS["typography"].get("font_annotation", "Caveat")
    pk = primary.lower().replace(" ", "_")
    ak = annotation.lower().replace(" ", "_")
    return f"""
@font-face{{font-family:'{primary}';font-weight:400;src:url('data:font/woff2;base64,{F[pk+"_regular"]}') format('woff2');}}
@font-face{{font-family:'{primary}';font-weight:600;src:url('data:font/woff2;base64,{F[pk+"_600"]}') format('woff2');}}
@font-face{{font-family:'{primary}';font-weight:700;src:url('data:font/woff2;base64,{F[pk+"_bold"]}') format('woff2');}}
@font-face{{font-family:'{annotation}';font-weight:700;src:url('data:font/woff2;base64,{F[ak+"_bold"]}') format('woff2');}}
"""
```

Font key naming: `{family_lowercase}_{weight_label}` — e.g. `inter_regular`, `inter_600`, `inter_bold`, `caveat_bold`.

---

## Base Slide HTML

Every slide is a full HTML document. Screenshot target: `#s1`.

```python
def slide_html(DS: dict, F: dict, content: str) -> str:
    C   = DS["colors"]
    CSS = DS["css_toolkit"]
    return f"""<!DOCTYPE html><html><head><meta charset="UTF-8"><style>
{build_font_css(DS, F)}
*{{margin:0;padding:0;box-sizing:border-box;}}
body{{background:{C["bg"]};}}
.slide{{ {CSS["slide_base"]}; }}
.ey{{ {CSS["eyebrow"]}; }}
.wm{{ {CSS["wordmark"]}; }}
.sn{{ {CSS["slide_number"]}; }}
.card{{ {CSS["card"]}; }}
.ca{{ {CSS["card_accent"]}; }}
</style></head><body>
<div class="slide" id="s1">{content}</div>
</body></html>"""
```

---

## Playwright Screenshot

```python
import asyncio, os
from playwright.async_api import async_playwright

async def shoot(slides: dict, output_dir: str, canvas: dict) -> list[str]:
    w = int(canvas["width"].replace("px", ""))
    h = int(canvas["height"].replace("px", ""))
    os.makedirs(output_dir, exist_ok=True)
    paths = []
    async with async_playwright() as p:
        browser = await p.chromium.launch()
        for n, html in slides.items():
            page = await browser.new_page(viewport={"width": w, "height": h})
            await page.set_content(html)
            await page.wait_for_timeout(900)
            path = os.path.join(output_dir, f"{str(n).zfill(2)}.png")
            await page.locator("#s1").screenshot(path=path, type="png")
            paths.append(path)
        await browser.close()
    return paths
```

**Troubleshooting blank slides or wrong fonts:**
1. Run `python3 scripts/verify_fonts.py`
2. Confirm font-family names in CSS match `@font-face` declarations exactly
3. Increase `wait_for_timeout` to 1500 if fonts still don't render

---

````

**`references/design-system-schema.md` (aynen, ilk 80 satır):**

````markdown
# Design System Schema

`design-system.json` is the single source of truth for all visual decisions.
The generator reads all values from this file — nothing is hardcoded.

---

## Required Sections

### meta
```json
{
  "meta": {
    "version": "1.0",
    "style": "Brand Name — Style Name",
    "slide_format": "4:5 portrait (1080x1350px)"
  }
}
```

### canvas
```json
{
  "canvas": {
    "width": "1080px",
    "height": "1350px",
    "ratio": "4:5",
    "padding": "96px 88px",
    "viewport_playwright": "1080x1350"
  }
}
```
Confirmed better than 1:1 on Instagram. Horizontal padding 88px, vertical 96px.

### colors
All values are hex or rgba strings. Token names are referenced by CSS toolkit.
```json
{
  "colors": {
    "bg":       "#FAF8F4",
    "white":    "#FFFFFF",
    "ink":      "#1A1A1A",
    "slate":    "#666666",
    "mist":     "#AAAAAA",
    "line":     "#EDEAE4",
    "orange":   "#E85D04",
    "ghost":    "rgba(0,0,0,0.04)",
    "wordmark": "#D0CCC5"
  }
}
```
Replace hex values for a new brand. Keep token names — the CSS toolkit references them by name.

### typography
**All size values must be concrete px values — never ranges.**
`"104-108px"` is malformed. Use `"104px"`. Ranges break CSS — the browser ignores invalid units.

```json
{
  "typography": {
    "font_primary": "Inter",
    "font_annotation": "Caveat",
    "weights": { "regular": 400, "semibold": 600, "bold": 700 },
    "scale": {
      "cover_headline":  { "size": "104px", "weight": 700, "line_height": 0.97, "letter_spacing": "-0.035em" },
      "slide_headline":  { "size": "56px",  "weight": 700, "line_height": 1.05, "letter_spacing": "-0.022em" },
      "stat_hero":       { "size": "200px", "weight": 700, "line_height": 0.88, "letter_spacing": "-0.05em"  },
      "step_number":     { "size": "140px", "weight": 700, "line_height": 0.88, "letter_spacing": "-0.05em"  },
      "body":            { "size": "22px",  "weight": 400, "line_height": 1.65 },
      "eyebrow":         { "size": "13px",  "weight": 600, "letter_spacing": "0.2em", "transform": "uppercase" },
      "wordmark":        { "size": "18px",  "weight": 700, "letter_spacing": "0.05em" },
      "slide_number":    { "size": "13px",  "weight": 600, "letter_spacing": "0.08em" },
      "annotation":      { "font": "Caveat","size": "36px", "weight": 700 },
      "swipe_hint":      { "font": "Caveat","size": "28px", "weight": 700 }
    }
  }
}
```

### spacing
````

### 3.7 johnnyang0612/bearcarousel — render + otomatik kalite denetimi (DEĞER 4)

Link: https://github.com/johnnyang0612/bearcarousel · 3★ · Apache-2.0 · `pip install bearcarousel`. Kopya: `kopyalar/johnnyang0612-bearcarousel/` (README, AGENTS.md, SKILL.md, template rehberi).

Özet: HTML değil Pillow ile çizer; ama asıl değer **audit** fikri: gerçek ink-box ile hizalama, render edilen pikseller üzerinden WCAG kontrast, kopya limiti, logo konumu → RED varsa exit 1. Üç katman: content JSON × template (framework + style) × brand JSON. Framework'ler: aida-teach, pas, listicle, story, contrast, case. Emoji yok (tofu). Biz Playwright hattına aynı audit'i (PNG'yi geri okuyup kontrast/taşma kontrolü) ekleyebiliriz.

**`skills/bearcarousel/SKILL.md` (aynen):**

````markdown
---
name: bearcarousel
description: Turn a topic into finished Instagram carousel slides — aligned, audited 1080x1350 PNGs. Use when the user asks to 做輪播/做 IG 輪播圖/把主題或筆記變成輪播貼文, or to make/draft/restyle an Instagram carousel. Works in ANY directory; bootstraps the BearCarousel engine on first use, then drives its CLI and live-linked browser UI.
---

# BearCarousel — 主題進，成品輪播出 / topic in, finished carousel out

BearCarousel renders aligned 1080x1350 slides from a content JSON, audits what
it drew (alignment, WCAG contrast, copy limits, logo, readability), and exits
non-zero on any RED finding. Your job is to drive it end to end for a user who
never touches a terminal.

## 1. Bootstrap（第一次在這台機器上用）

Check once: `bearcarousel --version`. If missing:

```bash
pip install bearcarousel
```

Fallback if PyPI is unreachable:
`pip install "bearcarousel @ git+https://github.com/johnnyang0612/bearcarousel#subdirectory=pypi"`
— or clone https://github.com/johnnyang0612/bearcarousel and use
`python cli.py` / `python run.py` directly from the checkout.

The first `bearcarousel` command downloads the matching app tree (rulepack,
templates, fonts, UI) into `~/.bearcarousel/app/<version>/`. Everything after
that is offline.

## 2. Read the app's own instruction set, then follow it

The complete playbook ships WITH the app — do not improvise from this skill:

- `~/.bearcarousel/app/<version>/AGENTS.md`（English, canonical）
- `~/.bearcarousel/app/<version>/AGENTS.zh-TW.md`（繁體中文）

Read it and follow **Part B**: read the rulepack in `rules/base/` → choose
高速版 (fast, needs one real anchor) or 進階訪談版 (interview, per-slide
probes from `rules/base/interview.json`) → draft → render → audit → fix every
RED → hand off.

All CLI verbs are proxied by the launcher, from any directory:

```bash
bearcarousel draft  --framework case --slides 8 --out my_post.json
bearcarousel render --content my_post.json --json
bearcarousel audit  --content my_post.json --json     # exit 1 on RED
bearcarousel open   --content my_post.json            # live-linked browser UI
bearcarousel export --content my_post.json --out deck.zip --json
bearcarousel ui                                       # bare UI, no content
```

`open` is the handoff: the user's browser gets the deck live-linked — you
rewrite the content file and the UI hot-reloads; they edit words and the file
updates for you to re-read. Run it in the background; always re-read the file
before rewriting it.

## Hard floor (before you even read AGENTS.md)

- **Never invent a number, case, or quote.** Real material comes from the
  user; missing figures are written as 【待補：…】, never filled in.
- **Never deliver with a RED finding.** Exit code 1 from audit/export means
  not done.
- **No emoji in slide content** — the renderer draws them as tofu and the
  audit hard-blocks.
````

**`brands/_blank.json` (brand token şeması, ilk 60 satır):**

```json
{
  "id": "_blank",
  "name": "Untitled Brand",
  "_readme": "Copy this file, rename it, and edit. Every value here is read by the engine at render time — there are no brand constants in the Python source. Colours accept #RGB, #RRGGBB or #RRGGBBAA.",
  "canvas": {
    "w": 1080,
    "h": 1350
  },
  "colors": {
    "background": "#0A0A0E",
    "text": "#FAFAFA",
    "accent": "#7C3AED",
    "accent_light": "#C084FC",
    "muted": "#C8C8D2",
    "chip": "#FCD34D",
    "stroke": "#000000"
  },
  "_colors_note": "accent_light is what small accent text (stat numerals) is drawn in. It is deliberately lighter than accent: #7C3AED on this background measures 3.2:1, below WCAG AA. If you change these, run the audit — it checks the real rendered background, glow included.",
  "fonts": {
    "cjk": "NotoSansTC",
    "latin": "Lato",
    "cjk_latin_spacing_em": 0.15
  },
  "logo": {
    "path": null,
    "corner": "top-left",
    "margin_x": 50,
    "center_y": 90,
    "height": 72,
    "align": "alpha-mass"
  },
  "wordmark": {
    "enabled": false,
    "line1": "",
    "line1_size": 22,
    "line1_weight": "Bold",
    "line2": "",
    "line2_size": 16,
    "line2_weight": "Medium",
    "gap_x": 18
  },
  "chip": {
    "size": 24
  },
  "background": {
    "type": "glow",
    "glow_strength": 0.5
  },
  "layout": {
    "margin_x": 96,
    "swipe_hint": true
  },
  "footer": {
    "text": "",
    "size": 18,
    "bottom_margin": 36,
    "made_with": false,
    "made_with_text": "made with BearCarousel"
  },
  "language": {
```

### 3.8 dean9703111/ig-card-generator — Markdown DSL → lint → Puppeteer (DEĞER 4)

Link: https://github.com/dean9703111/ig-card-generator · 13★ · MIT · Çince doküman. Kopya: `kopyalar/dean9703111-ig-card-generator/` (SKILL.md, README, hooks.md, content-example.md).

Özet (Türkçe çeviri): Akış `content.md` (DSL) → `node scripts/lint.mjs` (5-10 kart, kapakta hook işareti zorunlu, kart başına tek `##`, karakter limiti, CTA'da eylem fiili + yorum anahtar kelimesi, yasak "AI ağzı" listesi, kaynaksız sayı uyarısı) → `node scripts/build.mjs --theme both` (index.html + alt-text.md + deck.json + taşma kontrolü + 4:5/1:1/9:16 PNG + contact-sheet). İçerik dosyasında renk/stil yazılmaz; görünüm config token'larından gelir (`brand.accent` dark/light ayrı, light mod accent koyu olmalı, kontrast ≥4.5:1 build'de hesaplanır). Kapak A/B: birden çok `@cover` yaz, `--covers` ile yan yana karşılaştır. "İçerik demir kuralları": sayı uydurma, kart başına bir kavram, AI ağzı yasak, CTA tam, kapak önce empati, küçültme ile sığdırma yasak. 6 hook formülü: sayısal kontrast, acı sorusu, bilişsel ters çevirme, liste vaadi, kimlik çağrısı, zaman vaadi.

**DSL söz dizimi tablosu (aynen, Çince):**

````markdown
#### DSL 語法約定

| 語法 | 用途 | 範例 |
|------|------|------|
| `===` | 分卡（獨立一行） | — |
| `@cover` `@point` `@code` `@cta` `@recap` `@section` | 卡片類型（卡片第一行） | 第 1 張必為 cover、最後必為 cta；`@recap` 放 CTA 前，自動彙整各卡 `##` 成回顧清單（儲存率槓桿，7 張以上 lint 會提醒）；`@section` 是章節分隔卡（自動編大號輪廓數字＋標題，計字上限 `chars.section` 預設 40） |
| `@卡型 invert` | 卡級明暗反轉（modifier） | `@section invert`——該卡整組 token 翻到相反主題（深牌出淺卡），做滑動明暗節奏；建議整副 1–3 張，用在章節卡／金句卡／CTA |
| `[badge] 文字` | 頂部膠囊標籤 | `[badge] ⚡ Claude 生態系 · 2026` |
| `[hero] 文字` | 巨型主角字（190px 級），封面唯一視覺主角用 | `[hero] !!90 秒!!` |
| `[stat] 數字 \| 標籤` | 巨型數據卡（大數字＋說明） | `[stat] 3 次 \| 重複超過 3 次就包起來` |
| `[vs] 左（✕）\| 右（✓）` | 左右對比欄，連續多行合併成同一格線 | `[vs] 手動改錯 \| 一條指令跑完` |
| `[chat] 角色: 內容` | 對話氣泡；`我/你/user` 靠右 accent，其他靠左，連續行合併 | `[chat] 我: 幫我排週二的會` |
| `[bar] 標籤 \| 顯示值 \| 寬度%` | 橫向比例條；`*` 開頭＝hot 色，連續行合併 | `[bar] *手動 \| 8 小時 \| 100` |
| `[ring] 數字 \| 標籤` | 圓環百分比（0–100，容忍尾隨 %）；連續行合併成一排，`*` 開頭＝hot 色 | `[ring] *92 \| 包成 Skill 後成功率` |
| `[check] 文字` | 自檢清單（☐）；`*` 開頭＝已勾（☑），連續行合併 | `[check] 每個數字都有來源？` |
| `[shot] 路徑 \| 圖說` | 截圖畫框（窗控 chrome＋嵌入本地圖片，路徑相對 deck 目錄） | `[shot] assets/demo.png \| 後台畫面` |
| `[big] 文字` | 一句話 punchline，整卡視覺主角 | `[big] AI 不是顧問，是==隊友==` |
| `[stamp] 文字` | 品牌章印／系列標籤，多用於 CTA | `[stamp] Dean 的 AI 開發工作流` |
| `# 大標` | H1，可連續多行堆疊 | 封面 hook 用 |
| `## 段標` | H2（自帶強調底線），**一卡限一個** | `## 先講結論` |
| 一般段落 | body 文字，連續行合併換行 | — |
| `1. **標題**：說明` | 編號卡盒 | — |
| `- **標題**：說明` | 條列卡盒 | — |
| `[ok] 文字` / `[no] 文字` | ✅／❌ 對照盒 | 對比型必備 |
| `[warn] 文字` | ⚠️ 警示條 | — |
| `[tip] 文字` | 💡 重點框 | 每卡的記憶點 |
| `[flow] A -> B -> *C` | 自適應流程卡；短流程橫向，長流程自動轉 stepper，`*` 開頭＝強調節點 | `[flow] Define -> Generate -> Eval -> *Fix` |
| `[timeline] 標籤 \| 內容` | 縱向時間軸／演進歷程；連續行合併成一組，`*` 開頭＝強調節點 | `[timeline] *2026 \| MCP 讓 AI 接上真實服務` |
| `> 引言` | 引言框（可多行）；最後一行符合 `— 人名` 格式時，該行獨立成出處，不算引言本體 | `> 先想清楚，再動手`<br>`> — Dean` |
| `<!-- source: 出處 -->` | 證據來源註解；獨立一行，不渲染、不計字，收進該卡 `sources`（`<!-- TODO: ... -->` 同理收進 `todos`，lint 會提醒） | `<!-- source: 官方文件 2026 -->` |
| ` ```lang 檔名 ` | 程式碼視窗（窗控＋檔名列＋語法上色） | ` ```yaml SKILL.md ` |
| `***` | 裝飾分隔線 | — |
| `**重點**` | 強調色粗體（品牌 accent） | — |
| `==標記==` | 螢光標記膠囊 | 封面關鍵詞 |
| `!!衝擊!!` | 衝擊色（hook 數字、警告字） | `!!99%!!` |
| `` `code` `` | 行內程式碼 | — |

frontmatter（選填，覆蓋 config）：`title` / `handle` / `ratio` / `theme` / `background`。

自動注入的元件（content.md 不用寫、也不計字）：封面 creator chip（設定 `brand.avatar.src` 時）、封面「滑 →」提示（`layout.swipe_cue`）、內容卡右下大編號水印（`layout.watermark`）、`@recap` 的回顧清單、CTA 收藏暗示（`layout.save_hint`）與追蹤膠囊、footer 的 `EP.NN`（`brand.series.episode`）。

版型自動切換：`@point` 只有 `[big]`／引言（＋段落）時，自動變成置中大字的**全版金句卡**；`@point`（balanced 以下密度）、`@code`、`@recap` 內容自動垂直置中，避免下方大片留白。

````

### 3.9 itchernetski/threads-carousel-claude-skill — format presetleri (DEĞER 3)

Link: https://github.com/itchernetski/threads-carousel-claude-skill · 107★ · MIT. Kopya: `kopyalar/itchernetski-threads-carousel-claude-skill/SKILL.md`.

Özet: Next.js önizleme + `html-to-image` export; story 9:16 ve 16:9 presetleri hazır; 12 slayt tipi (hook, body/points, list, stats, quote, checklist, process, comparison, cta, image, emoji, number); 5 font × 8 yüzey × 8 vurgu kompozisyonu. Story/Reels kapağı için preset listesi aynen:

````markdown
## Format presets (choose target platform)

| Preset | Size | Platforms |
|---|---|---|
| `threads-4x5` *(default)* | 1080×1350 | Threads, Instagram feed (portrait) |
| `instagram-square` | 1080×1080 | Instagram, Facebook, LinkedIn feed |
| `linkedin-square` | 1080×1080 | LinkedIn document post (PDF) |
| `tiktok-9x16` | 1080×1920 | TikTok Photo Mode, Reels, Shorts |
| `story-9x16` | 1080×1920 | Instagram Stories, Threads Stories |
| `wide-16x9` | 1920×1080 | Presentations, YouTube, desktop decks |

````

