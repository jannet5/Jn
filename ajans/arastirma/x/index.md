# X.com Araştırması — Kaynak İndeksi

Tarih: 2026-10-05 · Yöntem: WebSearch (`allowed_domains: x.com`, 40+ sorgu, EN+TR) → tweet metinleri fxtwitter API + threadreaderapp ile çekildi → thread'lerin linklediği GitHub repo'ları ham README olarak indirildi.
Konu notları: `notlar/` klasörü. Özet + aksiyon listesi: `OZET.md`.

Değer sütunu: **Y** = yüksek (doğrudan sisteme girer), **O** = orta (referans/ilham), **D** = düşük (bağlam).

## 1. Mobil uygulama üretimi (Claude Code / Codex / Expo / Rork)

| Link | Hesap | Konu | Özet | Linklediği kaynaklar | Değer |
|---|---|---|---|---|---|
| https://x.com/carlvellotti/status/2026804023756730426 | @carlvellotti | Claude Code ile iOS app 4 adım | Expo + Expo Go (QR) + NativeWind + EAS Build; Xcode'a hiç dokunmadan App Store'a. Duolingo PM mülakatı için app yapıp teslim etmiş. | — | Y |
| https://x.com/yazinsai/status/1998422553682162097 | @yazins | "1 günde app" playbook | Claude Code + frontend-design plugin + Expo + InstantDB; `npx create-instant-app -b expo`, context7 MCP, plan mode ile spec. | context7 MCP, instantdb | Y |
| https://x.com/undefinedKi/article/2055641692401754135 | @undefinedKi | Sıfır kodla 2 günde App Store (article) | Adım adım: ilk prompt, Expo Go, readdy ile tasarım → ekran görüntüsünü Claude'a verip "match it", mixkit sesler, Apple'ın zorunlu kıldığı ekranlar (privacy policy, onboarding, settings, loading), Developer Program, App Store Connect listesi. | readdy, mixkit, netlify, EAS | Y |
| https://x.com/Baconbrix/status/1947698636869013671 | @Baconbrix (Expo) | Expo için CLAUDE.md | Evan Bacon'ın yeni Expo app'leri için kullandığı CLAUDE.md (görsel; metni skills.md'de). | — | Y |
| https://x.com/Baconbrix/status/1982180394297393257 | @Baconbrix | Swift modül için CLAUDE.md | `npx create-expo-module --local` + `modules/CLAUDE.md` "Native iOS" kuralları. | — | O |
| https://x.com/Baconbrix/status/2005763720405025009 | @Baconbrix | Claude Code Web + EAS Workflows | GitHub'a push → EAS workflow her commit'te Expo Go'da açılabilir preview yayınlar; Claude Code web oturumunu repoya bağla. | github.com/EvanBacon/expo-rsc-movies (.eas/workflows/preview.yml) | Y |
| https://x.com/Baconbrix/status/2011862532320084329 | @Baconbrix | Expo Claude Code skills duyurusu | `/plugin marketplace add expo/skills`, expo-app-design / expo-deployment / upgrading-expo; `bunx add-skill expo/skills`. | github.com/expo/skills | Y |
| https://x.com/kevinma_dev_zh/status/2011918715764326713 | @kevinma_dev_zh | Expo skills içerik özeti | 3 skill'in kapsamı: UI/Expo Router/NativeWind/API routes; App Store/TestFlight/Play/EAS/CI; SDK upgrade. | expo/skills README'leri | O |
| https://x.com/expo/status/2054969915682263333 | @expo | React web dev 1 haftada native iOS | Expo + Claude Code + Expo Skills ile; blog'da hangi beceriler transfer oldu. | expo blog | O |
| https://x.com/expo/status/2066220125645115562 | @expo | SDK upgrade'i Claude'a devret | expo/skills ile SDK 56 upgrade; rehber linki. | try.expo.dev/sdk-56-upgrade-twitter | O |
| https://x.com/betomoedano/status/2040597359638315142 | @betomoedano | 5 favori Claude skill (mobil) | Building Native UI (Expo), explain-code, Vercel React Native skills, skill-creator, app-icon (SnapAI). | code-with-beto/skills, SnapAI | Y |
| https://x.com/betomoedano/article/2050715197065572413 | @betomoedano | Mobil için en iyi Claude skill'leri (article) | ASO Audit, Apple Search Ads (appeeky), App Store Preflight, App Icon, Build Native UI; install komutları. | eronred/aso-skills, truongduy2611/app-store-preflight-skills, expo/skills building-native-ui | Y |
| https://x.com/suna_gaku/status/2058895482878058676 | @suna_gaku → @Kappaemmedev | Codex + Expo Go + QR skill | `npx --yes codex-phone-lab`: Codex'in telefonda canlı app üretip iterasyon yapması. | codex-phone-lab | O |
| https://x.com/dkundel/status/2062599892347039813 | @dkundel (OpenAI) | Codex "Build iOS apps" plugin | Codex içinde simülatör, SwiftUI preview, hot reload. | serve-sim (Baconbrix) | O |
| https://x.com/Baconbrix/status/2045207510039322668 | @Baconbrix | Codex desktop + iOS simülatör | 1.2M görüntülenme; Codex'te doğrudan iPhone app. | — | D |
| https://x.com/mahanot_dikshit/status/2099855578151764042 | @mahanot_dikshit | Solo app founder bütçe stack'i | Expo, Supabase, RevenueCat, Claude $20, MascoFast, PostHog, Sentry, Resend, Cloudflare. | — | Y |
| https://x.com/Baconbrix/status/1930919837926109250 | @Baconbrix | 3 Cursor kuralı (Expo) | ScrollView > FlatList (recycle hariç), `display: contents`, CSS boxShadow. | — | O |
| https://x.com/devnamipress/status/1916467660159869250 | @devnamipress | Cursor rules for Expo/RN (gist) | "Cursor Adherence" bölümlü kural dosyası; mandatory compliance, self-check, error prevention. | gist (expo-rules.md) | O |
| https://x.com/wilterrero/status/1893650416237482133 | @wilterrero | RN + Cursor 5 app/yıl ipuçları | Scaffold, component üretimi, stack trace debug, native modüller, perf. | — | D |
| https://x.com/cpojer/status/1917797055562670546 | @cnakazawa | Expo app template | nkzw-tech/expo-app-template: Expo Router, Uniwind/Tailwind, i18n, React Compiler, bottom-sheet, Legend List. | github.com/nkzw-tech/expo-app-template | Y |
| https://x.com/mattshumer_/status/1957839186653761546 | @mattshumer_ | Tek seferde mobil app spec prompt'u | "Mobile PM + Designer + RN Tech Lead" discovery → Rork-Ready App Spec (tam metin prompts.md). | Rork | Y |
| https://x.com/rork_app/status/2008648481557409882 | @rork | Rork 1.5 | Claude Code/Opus tabanlı, RevenueCat, analytics, 3 tıkla App Store. | — | O |
| https://x.com/daniel_dhawan/article/2086873090391511406 | @daniel_dhawan (Rork CEO) | $10k/ay mobil app playbook (article) | Fikir bulma (viral videoyu tersine mühendislik), "gotcha moment", tarpit fikirler, onboarding 10-20 ekran, ilk günden ücret, UGC/meme/influencer dağıtım. | — | Y |
| https://x.com/DamiDefi/article/2050197937578193153 | @DamiDefi | Vibe coder'lar neden para kazanamıyor (article) | App Store ödeme sistemi: banking/tax, Small Business Program %15, StoreKit 2, 45 gün ödeme, trial, harici ödeme yasağı, kurulum sırası. | — | Y |
| https://x.com/cyrilXBT/status/2050634267173253409 | @cyrilXBT | Monetizasyon asıl iş | DamiDefi makalesinin özeti: kod değil iş modeli darboğaz. | — | D |
| https://x.com/a0_dev/status/1920686674691940683 | @a0_dev | a0.dev | Gerçek RN koduyla dakikalar içinde mobil app, Lovable alternatifi. | a0.dev | D |
| https://x.com/HHaandr/status/2103852436041441310 | @HHaandr | Claude'u app'inin PM'i yap | `brew install asc`, RevenueCat plugin; simülatörde ilk kullanıcı gibi gez, funnel sızıntısını bul, 3 iterasyon öner (tam prompt prompts.md). | asc CLI, RevenueCat plugin | Y |
| https://x.com/alexchristou_/status/1949870632524533939 | @alexchristou_ | Claude Code 15 ipucu | Supabase MCP, 2 pencere, plan mode, mesaj kuyruğu, CLAUDE.md, docs .md'leri, görsel yapıştır. | — | O |
| https://x.com/noisyb0y1/status/2040805910700761550 | @noisyb0y1 | $25k dev yerine $20 Claude | Open SaaS + Supabase + Claude Code; 4. ayda $47k. | — | D |
| https://x.com/learn2vibecode/status/2011582135572828488 | @learn2vibecode | vibecodeapp | Tek platformdan iOS + web app, 1 tıkla deploy. | vibecodeapp | D |
| https://x.com/slmhina/status/1929823221320503369 | @slmhina | No-code araç sıralaması | Web: Lovable; Mobil: Rork. | — | D |
| https://x.com/cj_zZZz/status/1885369723367661848 | @cjzafir | 8 AI aracı dürüst sıralama | Cursor 8/10, v0 UI için 8/10, Bolt 7, Lovable 6.5; 13 müşteri projesi dahil 33 proje. | — | O |
| https://x.com/MengTo/status/1943717847236325519 | @MengTo | Aura 15k MRR vibe coding dersleri | "Vibe design de yap": şablon/url/görsel ver; ilk tasarım blueprint olur; 21st.dev, reactbits, uiverse. | — | O |
| https://x.com/Baconbrix/status/2008350447765987688 | @Baconbrix → @housecor | Happy: telefondan Claude/Codex | Expo Router ile yazılmış açık kaynak client. | github.com/slopus/happy | D |
| https://x.com/luke_pighetti/status/1998698206008394168 | @luke_pighetti | HeroUI Native | RN UI kütüphanesi. | github.com/heroui-inc/heroui-native | O |
| https://x.com/konstipaulus/status/2064011863889788972 | @konstipaulus | text-to-lottie skill | `npx skills add diffusionstudio/lottie`; Claude/Codex ile production Lottie. | github.com/diffusionstudio/lottie | Y |
| https://x.com/motylinskiai/status/2058838465945407718 | @motylinskiai | @expo/ui | SDK 56: 8 kütüphane yerine tek paket (bottom-sheet, picker). | — | D |
| https://x.com/btahtaci/status/1895195315625484563 | @btahtaci | App Store çeviri scripti | Cursor'a yazdırdığı lokalizasyon aracı: gelir %200, impression %120. | açık kaynak tool | O |

## 2. Tasarım, DESIGN.md, anti-slop skill'leri

| Link | Hesap | Konu | Özet | Linklediği kaynaklar | Değer |
|---|---|---|---|---|---|
| https://x.com/donvito/status/1997558102099644889 | @donvito | frontend-design SKILL.md yeri | `~/.claude/plugins/marketplaces/claude-code-plugins/plugins/frontend-design/skills/frontend-design/SKILL.md`; markdown, düzenlenebilir. | anthropics/skills | O |
| https://x.com/aakashgupta/status/2041251109147423016 | @aakashgupta | Skill = sadece iyi prompt | frontend-design'ın MCP/API'si yok; en kaldıraçlı skill'ler zamanla rafine edilen prompt'lar. | — | O |
| https://x.com/Voxyz_ai/status/2106474370860548341 | @Voxyz_ai | Önce iyi tasarım göster → DESIGN.md | Godly/Refero/One Page Love'dan 3-5 ekran görüntüsü → Claude analiz → DESIGN.md → CLAUDE.md'ye @ ile import → taste-skill + /impeccable polish; 20 maddelik 5 subagent kontrol listesi (tam metin prompts.md). | taste-skill, impeccable | Y |
| https://x.com/101babich/status/2037561579714032116 | @101babich | 6 UX/UI skill | ui-ux-pro-max, frontend-design, taste-skill, shadcn-ui skill… | — | O |
| https://x.com/sitinme/status/2060319742829990339 | @sitinme | frontend-design neden işe yarıyor | "Dağılım yakınsaması": model en güvenli ortalamaya gider, kısıt onu kenara iter. | — | D |
| https://x.com/edinsoncode/status/2097606595454673067 | @edinsoncode | Vibe coding problemi + spec-driven (article) | Herkes aynı görünüyor; Spec Kit / Kiro / OpenSpec; constitution.md, product.md, design.md, architecture.md dosya düzeni. | github/spec-kit, Fission-AI/OpenSpec, gotalab/cc-sdd | O |
| https://x.com/eng_khairallah1/status/2037816689665147355 | @eng_khairallah1 | Top 60 skill/repo (article) | Superpowers, Spec Kit, Context7, Task Master, Playwright MCP, frontend-design, marketing skills, claude-seo… | çok sayıda GitHub linki | O |
| https://x.com/zodchiii/article/2034924354337714642 | @zodchiii | Top 50 skill & repo (article) | Resmi Anthropic skill'leri (pdf/docx/pptx/canvas-design/theme-factory/brand-guidelines), Remotion, Marketing Skills, claude-seo, MCP'ler. | github.com/anthropics/skills vd. | O |
| https://x.com/jasonzhou1993/status/2046778555787575583 | @jasonzhou1993 | Claude Design sistem prompt'u sızıntısı | "Mocking from scratch is a LAST RESORT"; anti-slop blocklist (gradient, emoji, left-border card, SVG illüstrasyon, Inter/Roboto/Arial); doğrulama için forked subagent. | — | Y |
| https://x.com/MrBallaz/status/2024071716763242781 | @MrBallaz | Landing page anti-slop 10 adım | frontend-design oku, Inter yasak (Plus Jakarta Sans), Framer Motion zorunlu, Phosphor icons, gradient yok, shadcn'i customize et, glassmorphism prompt'u, micro-interaction, hex ver, prompt zinciri. | — | Y |
| https://x.com/alexalbert__/status/1988707509973184516 | @alexalbert__ (Anthropic) | Mor slop'u skill ile yok etme | Anthropic blog yazısı duyurusu. | anthropic blog | D |
| https://x.com/ihteshamali/status/2040710213100322828 | @ihteshamali | awesome-design-md | Stripe, Vercel, Linear, Notion vb. 31+ (şimdi 73) siteden çıkarılmış DESIGN.md dosyaları; proje köküne at, "bunun gibi yap". | github.com/VoltAgent/awesome-design-md | Y |
| https://x.com/om_patel5/status/2039939578694438979 | @om_patel5 | Claude'a kendi tasarım aracı MCP | Tasarım sistemine bağlamlı MCP, tasarımı doğrudan koda düşürür. | — | D |
| https://x.com/om_patel5/status/2017102986892169280 | @om_patel5 | shadcn üstüne 17 UI kütüphanesi | Origin UI, MVP Blocks, Magic UI, Aceternity, Luxe, Animata, Phosphor, PatternCraft, Tailark, Motion Primitives, Kokonut, Bundui, Prism… | — | Y |
| https://x.com/PrajwalTomar_/status/2038292355095335406 | @PrajwalTomar_ | Stitch 2.0 + Claude Code MCP | Tasarımı Stitch'e, mantığı Claude'a; design.md köprü. | stitch.withgoogle.com | Y |
| https://x.com/PrajwalTomar_/status/2037204941945921743 | @PrajwalTomar_ | Stitch + Claude Code rehberi | Mühendisler için profesyonel UI dakikalar içinde. | — | O |
| https://x.com/PrajwalTomar_/status/2051640826095337734 | @PrajwalTomar_ | Stitch → Lovable | Ekranları Claude'da haritala → Stitch'te tasarla → kodu export → Lovable'a yapıştır. | — | O |
| https://x.com/PawelHuryn/status/2034583837351526763 | @PawelHuryn | DESIGN.md asıl haber | Stitch MCP Claude Code/Cursor/Gemini CLI'a bağlanıyor; PRD→design→code tek döngü. | — | O |
| https://x.com/designertom/status/1976479398711992572 | @designertom | Figma → Claude Code | Happy path'i Figma'da tasarla, Figma MCP ile Claude'a ver, 15 dk'da temel, 50 saat iterasyon. | — | O |
| https://x.com/viktoroddy/status/2035366371530617238 | @viktoroddy | AI web ajansı landing page tam prompt | Liquid glass, Instrument Serif + Barlow, HLS video arka plan, 9 bölüm; tam prompt prompts.md'de. Bizim vitrin için birebir referans. | motionsites.ai | Y |
| https://x.com/viktoroddy/status/2044391383394353285 | @viktoroddy | 3D creator portföy prompt'u | Kanit font, gradient text, marquee; detaylı spec. | motionsites.ai | O |
| https://x.com/viktoroddy/status/2033471317513777417 | @viktoroddy | 20 UI prompt kataloğu (article) | Cinematic hero, full landing, animated deck, liquid glass, one-shot workflow, Claude+Nano Banana. Polish ipuçları. | motionsites.ai | Y |
| https://x.com/osmosupply/status/2026956548086300825 | @osmosupply | GSAP piksel dalgası sayfa geçişi | clip-path + steps(12), stagger, 4 ayar değişkeni; Osmo boilerplate, Barba.js. | osmo.supply | O |
| https://x.com/listudio/status/2097728192668991514 | @listudio | Design skill'ler "zevk işletim sistemi" (article) | Impeccable, Taste Skill, Huashu Design, Open Design, Stitch Skills, Design DNA, Hue, design-extract, Figma MCP; GPT-Image → Codex workflow. | çok sayıda repo | Y |
| https://x.com/MyWestLord/article/2068064010600108267 | @MyWestLord | 45 Claude design skill (article) | 6 katman: frontend/UI, görsel/video, Claude Design, AI ürün tasarımı… Emil Kowalski motion kuralları (150-250ms vb.). | — | Y |
| https://x.com/Mnilax/status/2051701429987897712 | @Mnilax | 247 skill test, 23 kaldı (article) | Tier S: frontend-design, superpowers, simplify, skill-creator, web-design-guidelines; çakışan skill'leri birlikte kurma. | — | Y |
| https://x.com/Mnilax/article/2065161044587426265 | @Mnilax | Design system'i Claude'a input ver (article) | Moonchild ile token/komponent/kılavuz paketi; 10 ekranda on-brand 2→9, yeniden stil 6 saat→40 dk; "yeni renk ekleme, dur ve sor". | moonchild | Y |
| https://x.com/nett0eth/article/2076763781057663040 | @nett0eth | 5 web design skill + kendi skill'ini yaz (article, PT) | frontend-design, Impeccable, UIUX Pro Max, Web Design Guidelines (Vercel), Astryx (Meta design system); Type UI, UI Skills dizinleri. | — | O |
| https://x.com/majidmanzarpour/status/2013228422156144884 | @majidmanzarpour | Elit frontend UX skill denemesi | Claude'un kendi sitesini yeniden tasarlatma. | — | D |
| https://x.com/tomcrawshaw01/article/2093323226797744557 | @tomcrawshaw01 | Claude Design'ı Claude Code içinde kullanma (article) | `claude mcp add ... claude-design`, /design-consent, /design-sync; Refero Styles 2000+ design system; onay döngüsü prompt'u; 4 şeyli ilk prompt. | styles.refero.design, jiji262/claude-design-skill, Dammyjay93/interface-design | Y |
| https://x.com/gauravmc/status/1957850259549487393 | @gmchande | Playwright MCP döngüsü | "Claude Code UI'da kötü" değil, kurulumun kötü: Task → Claude ⇄ Playwright. | YouTube | O |
| https://x.com/AnandChowdhary/status/1958306807899934738 | @AnandChowdhary | Design Review workflow | OneRedOak/claude-code-workflows: PR'larda Playwright ile UI/UX/a11y review. | github.com/OneRedOak/claude-code-workflows | O |
| https://x.com/Av1dlive/article/2060035425574764708 | @Av1dlive | AI ile tasarım (builder's guide, article) | 3 ilke: önce akış sonra piksel; prompt = brief (500 kelime); design system repo'da markdown. 8 fazlı workflow, principles.md. | github.com/codejunkie99/cove, Moonchild | O |
| https://x.com/AmirMushich/article/2026693633794076892 | @AmirMushich | 10 tipografi preset (Nano Banana) | Manrope, Fraunces, Syne, Albert Sans, Tenor Sans, Space Grotesk, IBM Plex; tracking -3..-7%, leading 0.8-0.9. | amirmushich.link | O |
| https://x.com/MVP_Builder/status/1942958405972549657 | @MVP_Builder | 21st.dev Magic MCP | Doğal dil → modern UI komponenti IDE içinde. | github.com/21st-dev/magic-mcp | O |
| https://x.com/korablev/status/1891836064345751718 | @serafimcloud | Magic MCP özellikleri | Instant UI, component enhance, SVG logo finder, /ui komutu. | 21st.dev/magic | D |

## 3. Claude Code iş akışı, skill yazımı, CLAUDE.md

| Link | Hesap | Konu | Özet | Linklediği kaynaklar | Değer |
|---|---|---|---|---|---|
| https://x.com/bcherny/status/2007179832300581177 | @bcherny (Claude Code yaratıcısı) | 13 ipuçlu setup thread'i | 5 paralel Claude, web+local, Opus+thinking, ortak CLAUDE.md, plan mode, slash komutlar (/commit-push-pr), subagent'lar (code-simplifier, verify-app), PostToolUse format hook, /permissions, MCP'ler, Stop hook/ralph-wiggum, en önemlisi doğrulama döngüsü (Chrome extension). Tam metin skills.md. | code.claude.com docs, ralph-wiggum plugin | Y |
| https://x.com/trq212/article/2033949937936085378 | @trq212 (Anthropic) | Skill yazma dersleri (article) | 9 skill tipi (library ref, verification, data, process automation, scaffolding, review, CI/CD, runbook, infra); ipuçları: bariz olanı söyleme, Gotchas bölümü, klasör = progressive disclosure, description model içindir, config.json, on-demand hook'lar. | docs | Y |
| https://x.com/akshay_pachaar/article/2035341800739877091 | @akshay_pachaar | .claude/ klasörünün anatomisi (article) | CLAUDE.md (<200 satır), CLAUDE.local.md, rules/ (path-scoped), hooks (exit code 2 bloklar), commands, skills, agents. | — | Y |
| https://x.com/charliejhills/status/2043214357337911589 | @charliejhills | .claude/ karar kuralı | Proje talimatı → CLAUDE.md; tekrarlayan workflow → commands/; bağlam tetikli → skills/; uzman review → agents/. | — | O |
| https://x.com/socialwithaayan/article/2089647610122662164 | @socialwithaayan | CLAUDE.md nasıl kurulur (article) | 412 satırlık dosyadan derse: 4 konum, birleştirme (override değil), /init, `CLAUDE_CODE_NEW_INIT=1`, "kaldırınca hata yapar mı?" testi, hook vs CLAUDE.md. | — | Y |
| https://x.com/affaan/article/2012378465664745795 | @affaan | Everything Claude Code kısa rehber (article) | Skills vs commands, hook tipleri, subagent, rules örnekleri ("mor ton kullanma"), MCP bağlam yönetimi (<10 MCP / <80 tool), kısayollar, /fork, worktree. | affaan-m/ECC | Y |
| https://x.com/undefinedKi/article/2070852381164630023 | @undefinedKi | Claude Code'u 10x yapan 10 repo (article) | ECC (210k★), GStack (Garry Tan: /office-hours, /plan-eng-review, /review, /qa, /ship)… | github.com/affaan-m/ECC, garrytan/gstack | Y |
| https://x.com/nykdotdev/article/2051882598851547544 | @nykdotdev | 5 katmanlı operatör stack'i (article) | CLAUDE.md → hooks → skills → MCP → subagents; kurulum checklist'i. | — | O |
| https://x.com/hasantoxr/status/2048004868292678143 | @hasantoxr | claude-code-setup plugin | `/plugin install claude-code-setup@claude-plugins-official`: projeyi tarar, hook/skill/MCP/subagent önerir. | — | Y |
| https://x.com/santtiagom_/status/2031760340485030070 | @santtiagom_ | Subagent nasıl kurulur (ES) | /agents veya .claude/agents/*.md; "use proactively"; Haiku ile ucuz subagent. | — | O |
| https://x.com/rubenhassid/status/2081333711698587794 | @rubenhassid | Günlük skill kütüphanesi | /grill-me, /humanizer, /fact-checker, /prompt-master, /linkedin-hook; Notion'dan indir. | how-to-ai.guide | O |
| https://x.com/DanKornas/status/2100075692671037735 | @DanKornas | "Agents" repo: ortak AGENTS.md + skills | Claude Code/Codex için paylaşılan standartlar, kullanıcı tercihlerini ayır, skill ZIP paketleme. | GitHub (yanıtta) | D |
| https://x.com/JJEnglert/status/2042322280035074265 | @JJEnglert | Skill'leri ekiple paylaşma | 3 yöntem: shared drive, Team plan org dizini, GitHub repo (önerilen; .gitignore ile kişisel dosyalar). | — | O |
| https://x.com/PrajwalTomar_/status/1947272871967174720 | @PrajwalTomar_ | Vibe coding döngüsü (11 adım) | PRD + implementation plan yükle → feature seç → önce yaklaşımlar → plan → docs ekle → build → test talimatı → commit → sıradaki → yeni chat. | — | Y |
| https://x.com/Hartdrawss/status/2051957694454444370 | @Hartdrawss | PRD prompt'u | Her projede yapıştırılan PRD prompt'u (tam metin prompts.md). | — | Y |
| https://x.com/nateherk/article/2070237706953576871 | @nateherk | Claude'u para kazandıran ortak yap (article) | 4 upgrade: "roast council" ile yes-man'i öldür, kendi işini doğrulat, bağlam yönetimi, paralel subagent + /goal. | — | O |
| https://x.com/VK_ROXy/article/2080586827241595163 | @VK_ROXy | 150+ Claude Code aracı (article) | Kategori kataloğu: status line, design/UI, memory, orchestration, security… | çok sayıda | O |
| https://x.com/PrajwalTomar_/article/2059612250047209957 | @PrajwalTomar_ | Vibe coder güvenlik checklist'i (article) | 30 dk pre-launch: legal, OWASP, data leak, API key, Supabase RLS; 60+ MVP tecrübesi. | — | O |
| https://x.com/AIMevzulari/status/2008158502485672153 | @AIMevzulari | Claude Code + Vibe Kanban (TR) | `npx vibe-kanban`: ajanlara kart ata, git worktree ile paralel, diff review. | vibe-kanban | O |
| https://x.com/felixleezd/status/2013654252263219406 | @felixleezd | Tasarımcılar için vibe coding rehberi (article) | Cursor + Claude Code kurulumu, 48 saatte AI chat'li portföy, 5 günde internal tool. | — | O |

## 4. Ajans iş modeli, müşteri bulma, fiyatlandırma, reklam

| Link | Hesap | Konu | Özet | Linklediği kaynaklar | Değer |
|---|---|---|---|---|---|
| https://x.com/bettercalltonny/article/2085766984088649924 | @bettercalltonny (TR) | Claude Code ile premium web sitesi (TR article) | 5 bölüm: skill yükle (frontend-design + ui-ux-pro-max), 3 referans sitenin 9 ekran görüntüsü, 5 bloklu build prompt'u, 3 ayrı cila geçişi (tipografi/boşluk/hareket) + 375px mobil, Cloudflare Pages yayın. Ajans $1-2k alıyor; 3. sitede 2 saatlik pipeline. | — | Y |
| https://x.com/theseoguy_/status/2080670105931792408 | @theseoguy_ | Yerel işletme sitesinde olmazsa olmazlar | Sağ üst tıklanabilir telefon, 3 alanlı form, gerçek fotoğraf, yorumlar, tek CTA; H1'de ana keyword + şehir, 500+ kelime, her hizmet/şehir için sayfa, NAP tutarlılığı; anında bildirim, cevapsız arama SMS. | — | Y |
| https://x.com/gippp69/status/2062499588838228166 | @gippp69 | $1.800'lık landing page → Claude ile $70 | 5 referans ekran görüntüsü, önce 7 soru sordur, tek görsel yöne it; 2. geçişte tipografi, koyu palet, mobil, cursor efekti, 6 micro-interaction. | — | Y |
| https://x.com/DeRonin_/article/2062301065312407891 | @DeRonin_ | Tek kişilik ajans $40k MRR (article) | 14 müşteri (4×$5k, 6×$2.5k, 4×$1.25k); 4 aşamalı teslimat (intake 10dk → model üretir → QA 15dk → handoff); model roster (ucuz workhorse + premium %10); opex ~$750; 90 günlük plan; "skill olarak kaydet, tekrar kullan". | — | Y |
| https://x.com/lukepierceops/status/2101026188844302617 | @lukepierceops | 180 günlük $500k ajans planı (article) | Teklif: $3k audit → $30k build → $3k/ay retainer; huni matematiği (haftada 1 discovery call); 5 faz; hook offer ile outreach (audit'i önden satma); 6 ölüm nedeni. | Google Doc checklist | Y |
| https://x.com/lukepierceops/status/2074198287780110555 | @lukepierceops | AI ajans en iyi iş modeli | Esneklik: $3k audit, $30k sistem, retainer; tek hissedilen problemi çözen teklif + profesyonel süreç. | — | O |
| https://x.com/coreyganim/status/2041104385619480989 | @coreyganim | Takipçisiz müşteri bulmanın 7 yolu | Yerel AI meetup, 10 kapı çalma ("ücretsiz 15 dk audit"), LinkedIn DM soruları, çevreye ücretsiz audit, ajanslarla referans anlaşması, coworking office hours, 90 gün paylaşım. | — | Y |
| https://x.com/coreyganim/status/2039831742467715206 | @coreyganim | Küçük ölçekte zengin olma modelleri | Tek dikey AI pazarlama ajansı (diş hekimi/med spa/HVAC) $2-5k/ay; skill paketleri $97-297; programatik SEO lead-gen. | — | Y |
| https://x.com/coreyganim/status/2029215703178334469 | @coreyganim | Speed-to-lead agent (article) | 2 saatte kur, $1.5-5k setup + $300-1k/ay; Zapier/Make/n8n; Twilio; müşteri bulma: yerel servis işletmeleri (formu test et, 1 saatte dönmüyorsa müşteri), emlakçılar, web ajanslarına white-label. | — | Y |
| https://x.com/coreyganim/article/2084974468347121940 | @coreyganim | KOBİ'ye "yönetilen AI çalışan" satmak (article) | Rol sat, agent değil; Telegram grubu (müşteri+agent+operatör); haftalık değer defteri; fiyat evrimi $500 setup → $2k setup + $1k/ay; 12 adımlı pilot. | — | Y |
| https://x.com/GanimCorey/status/2023066652858847590 | @coreyganim | $2-5k/ay satılabilecek 10 agent (article) | Teklif üretimi (müteahhit), vendor yönetimi (etkinlik), onboarding (SaaS), içerik yeniden kullanım, garanti talepleri (oto galeri)… | — | O |
| https://x.com/Zephyr_hg/status/2048064238317638062 | @Zephyr_hg | Sıkıcı yerel dikeylerde implementasyon | Emlak ofisi, diş kliniği, muhasebe; tek operatör $40-50k/ay; sabır moat. | — | O |
| https://x.com/tibo_maker/status/2082760636593828181 | @tibo_maker | Mission Control HQ'yu yerel işletmeye kurmak | Kuaför, tadilat firması; teklif/randevu/takip/admin'i taşı; müşteri başı $200-500/ay, 20 müşteri = $10k. | MCHQ_AI | O |
| https://x.com/dimitarangg/status/2040822212534423679 | @dimitarangg | Cold outreach ilkeleri (article) | Hacim önce; teklif > copy; 3 paragraf, 4-6 cümle; assumptive dil; 24-48 saat follow-up; 3 touch; %70-80 toplantı follow-up'tan; ICP netliği. | — | Y |
| https://x.com/umzrs/status/1907101763389325664 | @umzrs | Cold outreach sadece başta | Sonra referans + kişisel marka inbound. | — | D |
| https://x.com/mikefutia/status/2064446547505602605 | @mikefutia | Meta ads Claude Code plugin'i | 5 skill: /spy, /competitors-extractor, /bulk-creative, /ad-score, /ad-matter (Meta MCP ile hesap audit). | playbook (DM) | Y |
| https://x.com/zackpaid/status/2086497571313537299 | @zackpaid | Meta ad creative blueprint | Claude instructions file, ICP Reddit/Quora prompt'u, rakip kreatif analizi, hook/UGC script, statik görsel sistemi, Seedance video, Meta API izleme skill'i. | DM | O |
| https://x.com/zackpaid/status/2061794284253188247 | @zackpaid | 18 yaşındakiler $100k/ay (article) | Claude + Meta Ads MCP + Higgsfield MCP; 40+ kreatif/ay test. | — | O |
| https://x.com/AnthonyEclipse/article/2096615724101861498 | @AnthonyEclipse | AI UGC reklam workflow'u (article) | Claude script (10 açı, AIDA) → Pinterest yüz → Kling 3.0 klipler → TikTok b-roll → CapCut; günde 5-10 reklam. | ecommafia.io, YouTube | O |
| https://x.com/kritarthmittal/status/2029505868266619030 | @kritarthmittal | AI içerik fabrikası (article) | Nano Banana karakter (kusurlar=gerçekçilik) → viral klip bul → Arcads ile karakter swap. | — | O |
| https://x.com/junwatu/status/1993887904117223684 | @junwatu → @gizakdag | Nano Banana Pro 9'lu Instagram feed | Tek prompt ile tutarlı 9 görsel (prompt prompts.md). | — | Y |
| https://x.com/recap_david/status/1971642101302780371 | @recap_david | Diş kliniği AI sesli asistan | n8n + ElevenLabs; mesai dışı randevu; $24k/yıl satıldı. | n8n template (DM) | O |
| https://x.com/DeRonin_/article/2091620376514277560 | @DeRonin_ | Growth operator + Whop (article) | Başkasının uzmanlığına dağıtım kur, rev-share. | — | D |
| https://x.com/yigitech/status/1864289599046004753 | @yigitech (TR) | Ajans sitesini Cursor+Claude ile yeniledi | JarvisAgency UK; "web tasarım ölür mü?" | jarvisdigitalagency.com | D |

## 5. Türkçe kaynaklar

| Link | Hesap | Konu | Özet | Linklediği kaynaklar | Değer |
|---|---|---|---|---|---|
| https://x.com/bettercalltonny/status/2078886860064248034 | @bettercalltonny | 4 saatlik sıfırdan mobil app kursu (TR altyazı) | Claude ile adım adım gerçek uygulama, yayınlayacak seviye. 2M görüntülenme. | video | O |
| https://x.com/bettercalltonny/status/2079974507251482715 | @bettercalltonny | 1 saatlik Claude kursu | Uygulama geliştir, rutinleri otomatikleştir. | video | D |
| https://x.com/fatihguner/status/2035353139650376143 | @fatihguner (Komünite) | Kalfa OS | Claude Code için Türkçe işletim sistemi: 994 skill/16 kategori, 10 agent, 22 komut, 9 hook, 6 katman hafıza; `npx @komunite/kalfa init`. | github.com/komunite/kalfa-os | Y |
| https://x.com/ozgenurkorlu/status/2035433931168678015 | @ozgenurkorlu | Kalfa OS incelemesi | 989 skill; "skill nasıl yazılır" rehberi eksik eleştirisi. | — | D |
| https://x.com/surucudev/status/2053024709621514620 | @surucudev | Claude Design workshop (İstanbul) | Design system temeli → landing page → Claude Code ile canlıya alma → sunum. | luma.com/vlvofrqv | D |
| https://x.com/yigitakinkaya/status/2057708924300669156 | @yigitakinkaya | Sıfır kodla Claude Code (TR) | Code sekmesi, /init, Plan modu (Shift+Tab×2), bypassPermissions ayarı, ekran görüntüsüyle prompt şablonu. | claudecode.free | O |
| https://x.com/egebese/status/2020558145517466066 | @egebese | AI video app içerik pipeline'ı | Claude Desktop skill'i viral template'leri toplar → JSON → Claude Code skill'i Firebase'e yazar, thumbnail üretir, admin'de taslak. 50 template 10 dk. AIProxy, RevenueCat virtual currency, fal. | — | Y |
| https://x.com/benfurkankilic/status/1985728714144198966 | @benfurkankilic | Vibe coding ile app, 19 bölümlük seri | Logo ChatGPT, mağazaya gönderildi. | YouTube serisi | O |
| https://x.com/AIMevzulari/status/2008158502485672153 | @AIMevzulari | Vibe Kanban (TR) | Yukarıda. | — | O |
| https://x.com/alitekintr/status/1803164308693565645 | @alitekintr | GPT-4o ile web sitesi (2024) | Eski, bağlam için. | — | D |

## Thread'lerin linklediği ve indirilen GitHub kaynakları (özetler `notlar/araclar.md`)

anthropics/skills (frontend-design SKILL.md tam metni) · expo/skills · VoltAgent/awesome-design-md · komunite/kalfa-os · Leonxlnx/taste-skill · pbakaus/impeccable · nextlevelbuilder/ui-ux-pro-max-skill · coreyhaines31/marketingskills · eronred/aso-skills · truongduy2611/app-store-preflight-skills · diffusionstudio/lottie · nkzw-tech/expo-app-template · 21st-dev/magic-mcp · obra/superpowers · garrytan/gstack

Toplam: 118 X kaynağı + 15 GitHub reposu.
