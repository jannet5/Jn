# Araçlar, repo'lar, MCP'ler — X.com'da geçen ve linklenen kaynaklar

Format: ad — ne — kurulum/komut — kim bahsetti — bizim için.

## A. Mobil (Expo / RN)
- **expo/skills** (resmi) — Expo/EAS için 20+ agent skill (router, animation, native-ui, design-system, data-fetching, upgrade, eas-app-stores…) — `claude plugin install expo@claude-plugins-official` · `npx skills@latest add expo/skills --skill '*'` · tekil: `npx skills add https://github.com/expo/skills --skill building-native-ui` — Evan Bacon, Expo, Beto — **zorunlu**.
- **EAS Workflows** `.eas/workflows/preview.yml` — commit'te Expo Go preview linki — github.com/EvanBacon/expo-rsc-movies — müşteri önizleme hattı.
- **Expo Go** — QR ile telefonda çalıştırma; **`npx testflight`**, `eas build -p android -s`, `npx expo export -p web && eas deploy`.
- **NativeWind** — Tailwind for RN. **Uniwind** (nkzw template'te).
- **nkzw-tech/expo-app-template** — Expo 57/RN 0.86 New Arch, Expo Router, Uniwind, fbtee i18n, React Compiler, bottom-sheet, Legend List, Vite+ — Christoph Nakazawa — başlangıç şablonu adayı.
- **InstantDB** — realtime db + auth, `npx create-instant-app -b expo` — yazins. **Supabase** (free tier, MCP var) — herkes. **Firebase** — Ege Beşe.
- **RevenueCat** — IAP/abonelik, Claude Code plugin'i var, virtual currency (kredi) — Dikshit, Rork, Ege Beşe, HHaandr.
- **asc CLI** (`brew install asc`, `asc auth login`) — App Store Connect metadata/analytics terminalden — HHaandr, Beto/Preflight.
- **eronred/aso-skills** (appeeky) — `npx skills add eronred/aso-skills` — /aso-router, /aso-audit (10 faktör 0-100), /keyword-research, /metadata-optimization, /apple-search-ads, /competitor-tracking, /android-aso, /in-app-events, /seasonal-aso; ua-skills (TikTok/Meta/ASA) ayrı repo — Beto.
- **truongduy2611/app-store-preflight-skills** — `npx skills add truongduy2611/app-store-preflight-skills` — 100+ Apple guideline indeksi, 10 app tipi checklist (subscription_iap, health_fitness, ai_apps, kids…), asc ile metadata çeker — Beto.
- **code-with-beto/skills app-icon** (SnapAI CLI) — `npx skills add https://github.com/code-with-beto/skills --skill app-icon` — OpenAI/nano banana ile ikon, iOS 18/26 + Android.
- **Vercel React Native skills** (vercel-labs/agent-skills) — perf/animasyon best practice — Beto.
- **codex-phone-lab** — `npx --yes codex-phone-lab` — Codex + Expo Go + QR — Kappaemmedev.
- **Rork** (rork.com) — Claude Code tabanlı no-code mobil; RN ($20) / Swift (Max); App Review'a 3 tık, ekran görüntüsü + metadata üretir. **a0.dev**, **vibecodeapp** (iOS + web tek platform) alternatifler.
- **Happy** (github.com/slopus/happy) — telefondan Claude/Codex oturumu; Expo Router ile yazılmış.
- **MascoFast** — animasyonlu app maskotu ($15). **mixkit** — ücretsiz ses efektleri. **readdy** — AI app tasarımı (prompt'u Claude'a yazdır).
- Animasyon: **Reanimated 4**, **Moti**, **React Native Skia**, **react-native-skottie** (Margelo), **text-to-lottie** `npx skills add diffusionstudio/lottie`, **@expo/ui**, **HeroUI Native** (github.com/heroui-inc/heroui-native).
- **PostHog / Sentry / Resend / Cloudflare** — analytics / crash / email / domain (Dikshit stack'i).
- Burak Tahtacı'nın açık kaynak App Store lokalizasyon aracı (Cursor ile yazıldı).

## B. Tasarım
- **anthropics/skills** — frontend-design (tam metin tasarim-animasyon.md), skill-creator, canvas-design, algorithmic-art, theme-factory, brand-guidelines, web-artifacts-builder, pdf/docx/pptx/xlsx — `/plugin marketplace add anthropics/skills && /plugin install frontend-design`.
- **pbakaus/impeccable** — `npx impeccable install` → `/impeccable init`; 24 komut, 61 detector, Chrome overlay, PRODUCT.md + DESIGN.md — impeccable.style.
- **Leonxlnx/taste-skill** — `npx skills add https://github.com/Leonxlnx/taste-skill --skill design-taste-frontend`; brandkit, image-to-code, minimalist-ui, brutalist-ui, stitch-design-taste — tasteskill.dev.
- **nextlevelbuilder/ui-ux-pro-max-skill** — design DB + Design System Generator; npm `ui-ux-pro-max-cli` — uupm.cc.
- **vercel-labs/agent-skills web-design-guidelines** — 100+ kural audit.
- **VoltAgent/awesome-design-md** — 73 DESIGN.md (Stripe, Linear, Vercel, Notion, Apple, Claude, Expo…) — getdesign.md; mobile/website starter kit.
- **styles.refero.design** — 2000+ AI-okunur design system + DESIGN.md (Tom Crawshaw). **Godly**, **One Page Love**, **Awwwards**, **Dribbble** — referans kaynakları.
- **Google Stitch** (stitch.withgoogle.com) + MCP + Stitch Skills (generate-design, extract-design-md, react-components, stitch-loop, taste-design).
- **Claude Design** — `claude mcp add --scope user --transport http claude-design https://api.anthropic.com/v1/design/mcp` → `/design-consent`, `/design-sync`; export → Claude Code handoff. Skill'ler: jiji262/claude-design-skill, Dammyjay93/interface-design.
- **Figma MCP** (Figma-Context-MCP, figma-console-mcp) — Figma → Claude Code; "Send this to Figma" tersi.
- **Moonchild** — design system builder, export → Claude Code input (Mnimiy, Avid).
- **Design DNA** (referans → JSON), **Hue** (markayı öğrenip skill üretir), **design-extract**, **Huashu Design**, **Open Design** (lokal Claude Design alternatifi), **Astryx** (Meta design system, agent-ready), **shadcn/lint**.
- **21st.dev Magic MCP** — github.com/21st-dev/magic-mcp — doğal dil → komponent; /ui komutu; SVG logo finder.
- UI kütüphaneleri: Origin UI, MVP Blocks, Magic UI, Aceternity, Cult UI, Luxe UI, Animata, **Phosphor Icons**, PatternCraft, Tailark, Smooth UI, Motion Primitives, Kokonut, Bundui, Prism UI, Skipper UI, Kobalte; reactbits, uiverse.
- **motionsites.ai** (Viktor Oddy) — hazır cinematic hero/landing prompt'ları. **Osmo Supply** — GSAP boilerplate/page transition; Barba.js.
- Tipografi: Google Fonts (Manrope, Fraunces, Syne, Albert Sans, Tenor Sans, Space Grotesk, IBM Plex, Plus Jakarta Sans, Satoshi, Instrument Serif, Barlow, Kanit); **LTX Studio Elements** + Nano Banana ile font-tutarlı görsel.
- Görsel üretim: **Nano Banana Pro**, **GPT-Image-2**, Higgsfield, **Remotion** (+ remotion-superpowers), Blender MCP, After Effects MCP, Kling 3.0 (Kai AI), Seedance 2.5 (Dreamina/CapCut), Arcads (karakter swap), Pomelli (Google), Veo Automation.

## C. Claude Code altyapısı
- **claude-code-setup** — `/plugin install claude-code-setup@claude-plugins-official` — projeyi tarar, hook/skill/MCP/subagent önerir.
- **superpowers** (obra) — `/plugin install superpowers@claude-plugins-official` — brainstorm→spec→plan→TDD→subagent→review.
- **gstack** (garrytan) — /office-hours, /plan-eng-review, /review, /qa (gerçek tarayıcı), /ship.
- **ECC** (affaan-m/ECC) — disiplin + hafıza; /ecc:plan, agentshield.
- **code-simplifier**, **skill-creator**, **hookify**, **ralph-wiggum** (uzun döngü), **/fewer-permission-prompts** — Anthropic.
- **Playwright MCP** — `claude mcp add playwright -s user -- npx @playwright/mcp@latest`. **Claude Chrome extension**. **agent-browser** skill.
- **Context7** — `claude mcp add context7 -- npx -y @upstash/context7-mcp@latest`. **Task Master AI** (PRD → görevler). **Supabase MCP**. **Meta Ads MCP** (resmi). **Composio MCP** (1000+ app).
- **Spec Kit** (github/spec-kit), **OpenSpec**, **Kiro**, **gotalab/cc-sdd** — spec-driven dosya düzeni.
- **OneRedOak/claude-code-workflows** — Design Review workflow (Playwright + agents).
- **Vibe Kanban** — `npx vibe-kanban` — ajanlara kart, worktree ile paralel.
- **komunite/kalfa-os** — `npx @komunite/kalfa init` — Türkçe operasyon katmanı (994 skill, /start /sync /wrap-up /audit /review /release /handoff).
- **coreyhaines31/marketingskills** — resmi plugin marketplace'te; 50 pazarlama skill'i.
- **AgriciDaniel/claude-seo** — 12 alt skill, DataForSEO, schema, GEO.
- **remotion-dev/skills**, **kepano/obsidian-skills**, **mattpocock/skills**, **travisvn/awesome-claude-skills**, **skillsmp.com**, **skills.sh**, **Type UI**, **UI Skills**, **MCP Market** — dizinler.
- Ruben Hassid skill kütüphanesi (how-to-ai.guide): /grill-me, /humanizer, /fact-checker, /prompt-master.

## D. Otomasyon / satış (ajans operasyonu)
- **Make.com** (önerilen denge), **Zapier** (en hızlı), **n8n** (en güçlü, self-host) — speed-to-lead agent.
- **Twilio** (SMS $0.0079, voice), **Vapi** (AI voice), **ElevenLabs** (voice agent), **Calendly / Cal.com**.
- **Mission Control HQ** (MCHQ_AI) — yerel işletme operasyon merkezi; kurulum satılıyor.
- **Telegram grubu** (müşteri+agent+operatör) — Phil Goodwin fulfillment modeli.
- **Discolike** (benzer müşteri bulma), **Whop** (rev-share dağıtım), **Google Business Profile**.
- **Cloudflare Pages** (ücretsiz hosting; `npm run build`, `dist`), **Netlify** (privacy policy sayfası), **Astro** (statik site; Cloudflare'a katıldı; Emdash CMS `npm create emdash`).

## E. Veri çekme yöntemi (bu araştırmada işe yarayan)
- X araması: WebSearch `allowed_domains: ["x.com"]` + extended mod (site: operatörü çalışmadı).
- Tweet metni: `https://api.fxtwitter.com/i/status/<id>` (JSON; article içeriği draft-js blokları), `cdn.syndication.twimg.com/tweet-result?id=<id>&token=x`.
- Thread: `https://threadreaderapp.com/thread/<id>.html` → `id="tweet_N" class="content-tweet"` div'leri.
- xcancel/nitter kapalı (451/403/502); r.jina.ai x.com'u engelliyor.
- GitHub: `raw.githubusercontent.com/<owner>/<repo>/main/README.md`.
