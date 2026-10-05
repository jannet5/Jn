# GitHub / açık kaynak araştırması — ana dizin

**Tarih:** 2026-10-05 · **Dal:** `ccr-11072837-dfhtyx` · **Amaç:** Claude Code / Codex ile *güzel* mobil uygulama (Expo/RN) ve web sitesi (Next.js/Astro) üretmek; sosyal medya + Meta reklam hattını otomatikleştirmek. Şu anki sorun: animasyonsuz, Android ham pop-up'lı, jenerik ("AI yapmış") çıktı.

**Dosyalar**
- `OZET.md` — zincire hemen alınacak kaynaklar, neden, nasıl kurulur (buradan başla).
- `notlar/mobil-skiller.md` — Expo/RN skill'leri, UI kitleri, toast/sheet/menü çözümleri (31 kaynak).
- `notlar/web-skiller.md` — frontend-design, anti-slop skill'leri, bileşen kütüphaneleri, şablonlar (50 kaynak).
- `notlar/design-md.md` — DESIGN.md formatları, koleksiyonlar, üreticiler, tasarım inceleme ajanları (27 kaynak).
- `notlar/animasyon.md` — Motion/GSAP/Lenis (web), Reanimated/Moti/Skia/Lottie (mobil) + ajan kuralları.
- `notlar/pipeline-fabrika.md` — superpowers, spec-kit, gstack, BMAD, Ralph, görsel QA kapıları (40 kaynak).
- `notlar/sosyal-medya-reklam.md` — carousel üretimi (HTML→PNG), caption skill'leri, Instagram Graph API, Meta Ads SDK/MCP (45 kaynak).
- `notlar/dizinler.md` — skills.sh, claudskills.com, claudemarketplaces.com, Expo resmi rehber.
- `kopyalar/<repo>/` — en değerli SKILL.md / DESIGN.md / kural dosyalarının birebir kopyası (her klasörde `KAYNAK.md`: URL, yıldız, tarih, lisans).

Yıldız/tarih/lisans bilgileri 2026-10-05 itibarıyla GitHub API'den alındı. Değer sütunu 1-5 (5 = zincire hemen al). Ayrıntılı tablolar ve aynen kopyalanmış metinler kategori notlarında.

---

## 1. Mobil (Expo / React Native) — ayrıntı: `notlar/mobil-skiller.md`

| Repo | Yıldız | Ne işe yarar | Nasıl kullanılır | Değer |
|---|---|---|---|---|
| [expo/skills](https://github.com/expo/skills) | 2.655 | Expo'nun resmi 24 skill'i: `expo-design-system` (+ **native-slop.md**: 20 "AI yapmış" belirtisi), `expo-native-ui`, `expo-ui` (@expo/ui = gerçek SwiftUI/Compose sheet/picker), `expo-animation`, `expo-router` (formSheet) | `claude plugin install expo@claude-plugins-official` + proje köküne `AGENTS.md` | **5** |
| [vercel-labs/agent-skills → react-native-skills](https://github.com/vercel-labs/agent-skills/tree/main/skills/react-native-skills) | 31.956 | 36+ kural: native modal/formSheet, native menü (zeego), Pressable, `boxShadow`, FlashList, Reanimated. skills.sh'de en çok kurulan mobil skill (~147K) | `npx skills add vercel-labs/agent-skills --skill vercel-react-native-skills` | **5** |
| [emilkowalski/skills](https://github.com/emilkowalski/skills) | 43.581 | `animate-expo`, `apple-design`, `review-animations`, `break-ui` (kötü veriyle ekranı kır), `animation-vocabulary` | `npx skills@latest add emilkowalski/skills` | 4 |
| [sonner-native](https://github.com/gunnartorfis/sonner-native-toasts) | 1.357 | Sonner'ın RN portu: toast.success/error/promise, swipe, dark mode; Expo Go'da çalışır → **Android ham Alert/ToastAndroid yerine** | `npx expo install sonner-native` → `_layout.tsx`'e `<Toaster />` | **5** |
| [nandorojo/zeego](https://github.com/nandorojo/zeego) | 2.253 | Native dropdown/context menü (iOS UIMenu, Android Material) | `npx expo install zeego` | 4 |
| [nandorojo/burnt](https://github.com/nandorojo/burnt) | 1.561 | Native toast/alert (iOS SPIndicator/AlertKit); dev build gerekir | `npx expo install burnt` | 4 |
| [kirillzyusko/react-native-keyboard-controller](https://github.com/kirillzyusko/react-native-keyboard-controller) | 3.739 | Klavyeyi kare kare takip eden UI; Expo skill'lerinde KeyboardAvoidingView yerine zorunlu | `npx expo install react-native-keyboard-controller` | 4 |
| [heroui-inc/heroui-native](https://github.com/heroui-inc/heroui-native) | 3.668 | "Güzel, modern" RN UI kit (Uniwind + Reanimated 4), resmi MCP server | `npm i heroui-native` + MCP | 4 |
| [Code-with-Beto/skills](https://github.com/Code-with-Beto/skills) | 140 | `cwb-theming` (iOS semantik renk + Android Material You tek hook), `cwb-app-icon`, `cwb-ship` | `/plugin marketplace add Code-with-Beto/skills` | 3 |
| [callstackincubator/agent-skills](https://github.com/callstackincubator/agent-skills) | 1.664 | `react-native-best-practices` (perf), `react-navigation` (RN7 form-sheet, native tabs) | `npx skills add callstackincubator/agent-skills` | 3 |
| [founded-labs/react-native-reusables](https://github.com/founded-labs/react-native-reusables) | 8.675 | shadcn/ui'nin RN hali (NativeWind, Dialog/Sheet/Select) | `npx @react-native-reusables/cli@latest init` | 3 |
| [gluestack/gluestack-ui](https://github.com/gluestack/gluestack-ui) | 5.320 | v4 copy-paste bileşenler + repo içi `.agents/skills` (7 alt-skill + reanimated) | `npx gluestack-ui init -y` | 3 |
| [nativewind/nativewind](https://github.com/nativewind/nativewind) | 8.107 | Tailwind for RN; Expo `expo-tailwind-setup` skill'i kurar | `npx skills add expo/skills --skill expo-tailwind-setup` | 3 |
| [obytes/react-native-template-obytes](https://github.com/obytes/react-native-template-obytes) | 4.349 | Expo Router + NativeWind + Zustand + React Query starter, `claude.md` dahil | `npx create-obytes-app@latest` | 3 |
| [ehmo/platform-design-skills](https://github.com/ehmo/platform-design-skills) | 604 | Apple HIG + Material 3 + WCAG 450+ kural (ios/android/web SKILL.md) | `npx skills add ehmo/platform-design-skills` | 3 (mobil DESIGN.md için 5) |
| [sleekdotdesign/agent-skills](https://github.com/sleekdotdesign/agent-skills) | 582 | Ajanın mobil ekran *tasarımı* üretip RN'e implement etmesi (ücretli SaaS) | `npx skills add sleekdotdesign/agent-skills` | 3 |
| [gorhom/react-native-bottom-sheet](https://github.com/gorhom/react-native-bottom-sheet) | 9.106 | JS bottom sheet; Expo artık native `formSheet`/@expo/ui öneriyor | `npx expo install @gorhom/bottom-sheet` | 3 |
| [pproenca/dot-skills → expo-react-native-coder](https://github.com/pproenca/dot-skills) | 213 | 50 kural + 7 ekran şablonu (tab, list, form) | `npx skills add pproenca/dot-skills` | 2 |
| [tamagui/tamagui](https://github.com/tamagui/tamagui) · [callstack/react-native-paper](https://github.com/callstack/react-native-paper) · [infinitered/ignite](https://github.com/infinitered/ignite) · [react-native-ui-lib](https://github.com/wix/react-native-ui-lib) | 14.2k · 14.5k · 19.9k · 7.2k | Alternatif UI kit/boilerplate'ler; ajan kural dosyası yok | — | 2 / 2 / 2 / 1 |
| Expo MCP (`mcp.expo.dev`) · [docs.expo.dev/agents/claude](https://docs.expo.dev/agents/claude) | — | Doküman arama, EAS build, simülatör ekran görüntüsü | `claude mcp add --transport http expo https://mcp.expo.dev/mcp` | 4 |

**Almayacaklarımız:** Gentleman-Skills react-native (native-slop örnekleri içeriyor), react-native-ui-lib (New Arch yok).

## 2. Web (Next.js / Astro / Tailwind) — ayrıntı: `notlar/web-skiller.md`

| Repo | Yıldız | Ne işe yarar | Nasıl kullanılır | Değer |
|---|---|---|---|---|
| [anthropics/skills → frontend-design](https://github.com/anthropics/skills) | 179.752 | Resmi anti-slop tasarım yönü: AI default listesi, plan → jeneriklik kritiği → kod. skills.sh'de 956K kurulum | `npx skills add anthropics/skills@frontend-design` | **5** |
| [pbakaus/impeccable](https://github.com/pbakaus/impeccable) | 76.912 | 24 komut (`init`→PRODUCT.md, `document`→DESIGN.md, `craft`, `critique`, `audit`, `polish`, `animate`), 61 deterministik dedektör (hook), craft-floor yasak listesi | `npx impeccable install` → `/impeccable init` | **5** |
| [Nutlope/hallmark](https://github.com/Nutlope/hallmark) | 29.620 | 57 slop-test kapısı, 21 macrostructure (sayfa yapısı çeşitliliği), 21 tema, uydurma metrik yasağı | `skills/hallmark/` → `.claude/skills/` | **5** |
| [nextlevelbuilder/ui-ux-pro-max-skill](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill) | 133.229 | CSV tabanlı stil/palet/font/landing arama; sektör satırları (Restaurant, Medical Clinic, Dental); `--design-system --persist` → MASTER.md | `npx skills add nextlevelbuilder/ui-ux-pro-max-skill@ui-ux-pro-max` | **5** |
| [Leonxlnx/taste-skill](https://github.com/Leonxlnx/taste-skill) | 92.772 | Brief → 3 kadran (variance/motion/density) → gerçek design system; "AI Tells" yasakları; Next.js + Motion kuralları | `npx skills add https://github.com/Leonxlnx/taste-skill --skill design-taste-frontend` | **5** |
| [emilkowalski/skills](https://github.com/emilkowalski/skills) | 43.585 | `animate`, `review-animations`, `pick-ui-library`; easing/duration tabloları | `npx skills@latest add emilkowalski/skills` | **5** |
| [2389-research/landing-page-design](https://github.com/2389-research/landing-page-design) | 21 | Vibe Discovery (4 soru → Vibe Spec), Copy Strategy, %50 hero kuralı | `.claude/skills/landing-page-design/` | **5** |
| [Im-Fran/landing-skills](https://github.com/Im-Fran/landing-skills) | 0 (yeni) | 7 skill pipeline: brief → copy → art-direction (direction.md + tokens.css) → build → motion → review (`detect-tells.mjs`) → launch | `/plugin marketplace add Im-Fran/landing-skills` | **5** (GPL-3.0!) |
| [vercel-labs/agent-skills → web-design-guidelines](https://github.com/vercel-labs/agent-skills) | 31.956 | 100+ UI kuralı denetimi (a11y/form/animation/typography); 702K kurulum | `npx skills add vercel-labs/agent-skills@web-design-guidelines` | 4 |
| [google-labs-code/stitch-skills](https://github.com/google-labs-code/stitch-skills) | 8.426 | `design-md` (DESIGN.md üret), `taste-design` | `npx skills add google-labs-code/stitch-skills@design-md` | 4 |
| [shadcn-ui/ui](https://github.com/shadcn-ui/ui) + shadcn skill | 125.144 | Bileşen temeli; skill `components.json` bağlamı, init/add/search | `npx skills add shadcn/ui` | 4 |
| [magicuidesign/magicui](https://github.com/magicuidesign/magicui) | 22.472 | 150+ animasyonlu registry bileşeni + resmi SKILL.md | `npx shadcn@latest add @magicui/<slug>` | 4 |
| [secondsky/claude-skills → aceternity-ui](https://github.com/secondsky/claude-skills) | 226 | Aceternity UI kurulum/hata skill'i (asıl repo özel) | `npx -y skills add secondsky/claude-skills --skill aceternity-ui` | 4 |
| [21st-dev/magic-mcp](https://github.com/21st-dev/magic-mcp) | 5.969 | 10k+ bileşen arama/üretim MCP | `npx @21st-dev/cli@latest init --client claude` | 4 |
| [tponscr-debug/claude-skill-awwwards](https://github.com/tponscr-debug/claude-skill-awwwards) | 8 | Awwwards kriterleri, fluid type, easing ölçeği, GSAP+Lenis+Three | `~/.claude/skills/awwwards/` | 4 |
| [Vinayak-Shukla-03/anti-ai-slop](https://github.com/Vinayak-Shukla-03/anti-ai-slop) | 9 | 9 sert kural + "ikinci nesil monokültür" (cream+terracotta+Fraunces) yasağı | `~/.claude/skills/anti-ai-slop/` | 4 |
| [DavidHDev/react-bits](https://github.com/DavidHDev/react-bits) · [nolly-studio/cult-ui](https://github.com/nolly-studio/cult-ui) · [ibelick/motion-primitives](https://github.com/ibelick/motion-primitives) | 48.5k · 6.3k · 6.5k | Animasyonlu hazır bileşenler (registry / llms.txt) | shadcn add URL | 3 |
| [jiji262/claude-design-skill](https://github.com/jiji262/claude-design-skill) · [oil-oil/oiloil-ui-ux-guide](https://github.com/oil-oil/oiloil-ui-ux-guide) · [rampstackco/claude-skills](https://github.com/rampstackco/claude-skills) | 200 · 102 · 933 | Design Direction Advisor / 8 stil ailesi / 59 ajans skill'i (brand, landing copy, SEO) | `npx skills add …` | 3 |
| [wshobson/agents → tailwind-design-system](https://github.com/wshobson/agents) · [hairyf/skills](https://github.com/hairyf/skills) | 40.2k · — | Tailwind v4 @theme + OKLCH token şablonu / resmi doküman kaynaklı referans skill'leri | `npx skills add …` | 3 |
| [arthelokyo/astrowind](https://github.com/arthelokyo/astrowind) · [mearashadowfax/ScrewFast](https://github.com/mearashadowfax/ScrewFast) | 6.0k · 1.4k | Astro landing şablonları (KOBİ sitesi için) | `npm create astro -- --template arthelokyo/astrowind` | 3 |
| [Ga14ctic/awwwards-skill](https://github.com/Ga14ctic/awwwards-skill) · [superdesigndev/superdesign-skill](https://github.com/superdesigndev/superdesign-skill) | 1 · 623 | 9 fazlı orkestratör / canvas'ta çoklu taslak | — | 3 / 2 |
| heroui · daisyui · flowbite · preline · hyperui · park-ui · kibo · originui · cruip · ixartz | 30.9k · 42.5k · 9.4k · 6.5k · 12.3k · 2.4k · 4k · 10.6k · 4.7k · 2.1k | Bileşen/blok/şablon kütüphaneleri (kısa not) | — | 1-2 |
| awesome listeleri: [ComposioHQ](https://github.com/ComposioHQ/awesome-claude-skills) · [travisvn](https://github.com/travisvn/awesome-claude-skills) · [BehiSecc](https://github.com/BehiSecc/awesome-claude-skills) · [helloianneo](https://github.com/helloianneo/awesome-claude-code-skills) | 76.5k · 15.3k · 10.2k · 480 | Küratörlük; helloianneo'nun "mutlaka kur" web listesi en iyisi | README | 2-3 |

## 3. DESIGN.md / tasarım sistemi — ayrıntı: `notlar/design-md.md`

| Repo | Yıldız | Ne işe yarar | Nasıl kullanılır | Değer |
|---|---|---|---|---|
| [google-labs-code/design.md](https://github.com/google-labs-code/design.md) | 28.2k | DESIGN.md'nin **resmi spec'i**: YAML token + 8 bölüm; `lint` (WCAG kontrast), `diff`, `export` (Tailwind/DTCG) | `npx @google/design.md lint DESIGN.md`, `export --format css-tailwind` | **5** |
| [VoltAgent/awesome-design-md](https://github.com/VoltAgent/awesome-design-md) (getdesign.md) | 119.6k | 73 gerçek markanın ters mühendislik DESIGN.md'si (Notion, Stripe, Airbnb, Starbucks…) | `npx getdesign@latest add <slug>` | **5** |
| [rohitg00/awesome-claude-design](https://github.com/rohitg00/awesome-claude-design) | 1.1k | Estetik aileye göre DESIGN.md'ler, **Anti-Slop Kit**, `break-default-aesthetic` promptu, `brand-to-design-md` | Prompt'u DESIGN.md başına | **5** |
| [Meliwat/awesome-ios-design-md](https://github.com/Meliwat/awesome-ios-design-md) | 571 | **200 mobil uygulama** DESIGN.md'si; her biri + `DESIGN-expo.md`; HIG uyumlu (44pt, Dynamic Type, haptik, spring) | `DESIGN-expo.md`'yi CLAUDE.md yanına | **5** (mobil) |
| [OneRedOak/claude-code-workflows → design-review](https://github.com/OneRedOak/claude-code-workflows) | 3.9k | En çok kopyalanan design-review ajanı: Playwright MCP ile 7 fazlı inceleme (3 viewport, WCAG, konsol) + CLAUDE.md "Quick Visual Check" | `design-review-agent.md` → `.claude/agents/` | **5** |
| [bergside/awesome-design-skills](https://github.com/bergside/awesome-design-skills) (typeui.sh) | 3.1k | 67 estetik SKILL.md + DESIGN.md çifti (cafe, editorial, premium…) | `npx typeui.sh pull cafe` | 4 |
| [kwakseongjae/oh-my-design](https://github.com/kwakseongjae/oh-my-design) | 530 | Felsefe → karar tablosu → token zinciri; DESIGN.md Core v2; **54 slop gate** (grep'lenebilir) | `npx oh-my-design-cli@latest` | 4 |
| [uxKero/anydesign](https://github.com/uxKero/anydesign) · [yuvrajangadsingh/brandmd](https://github.com/yuvrajangadsingh/brandmd) · [bergside/design-md-chrome](https://github.com/bergside/design-md-chrome) | 214 · 66 · 3.0k | Müşterinin mevcut sitesinden / referans siteden DESIGN.md çıkarma (`npx brandmd <url>`) | Claude skill / CLI / Chrome eklentisi | 4 / 4 / 3 |
| [Dammyjay93/interface-design](https://github.com/Dammyjay93/interface-design) | 5.8k | Dashboard/app craft + `system.md` hafızası | `npx skills add` | 4 |
| [shadcn-ui/lint](https://github.com/shadcn-ui/lint) | 3.1k | Ajan-öncelikli Tailwind DS linter | ESLint/Oxlint | 4 |
| [microsoft/skills → frontend-design-review](https://github.com/microsoft/skills) · [Owl-Listener/designer-skills](https://github.com/Owl-Listener/designer-skills) · [VoltAgent/awesome-claude-design](https://github.com/VoltAgent/awesome-claude-design) | 3.1k · 2.8k · 4.0k | İnceleme skill'leri / visual-critique, color-system / Claude Design için 68 DESIGN.md | — | 3 |
| [SpaceZephyr/brand-design-md](https://github.com/SpaceZephyr/brand-design-md) · [ShriPunta/generate-design-md](https://github.com/ShriPunta/generate-design-md) · [facebook/astryx](https://github.com/facebook/astryx) · designmd.app · tweakcn | — | "X tarzında" tetikleyici / üretici / Meta DS / 759 DESIGN.md kütüphanesi / shadcn tema | — | 2-3 |

## 4. Animasyon — ayrıntı: `notlar/animasyon.md`

| Repo | Yıldız | Ne işe yarar | Nasıl kullanılır | Değer |
|---|---|---|---|---|
| [emilkowalski/skills](https://github.com/emilkowalski/skills) | 43.583 | `animate` (web), `animate-expo` (mobil), `review-animations`, `improve`, `animation-vocabulary`; easing/duration standartları; 1.9M kurulum | `npx skills add emilkowalski/skills` | **5** |
| [greensock/gsap-skills](https://github.com/greensock/gsap-skills) | 15.957 | Resmi 8 GSAP skill'i (core, timeline, **scrolltrigger**, plugins, react, performance) | `npx skills add https://github.com/greensock/gsap-skills` | **5** |
| [greensock/GSAP](https://github.com/greensock/GSAP) + gsap.com/llms.txt | 28.821 | ScrollTrigger pin/scrub, SplitText, Flip — **tüm pluginler artık ücretsiz** | `npm i gsap @gsap/react` | **5** |
| [motiondivision/motion](https://github.com/motiondivision/motion) + motion.dev/llms.txt | 33.839 | React/JS/Vue animasyon (eski Framer Motion; import `motion/react`) | `npm i motion` | **5** |
| Motion AI Kit ([motiondivision/cursor-plugin](https://github.com/motiondivision/cursor-plugin)) | 12 | Resmi `/motion` skill'i, best-practices, performance-audit, hosted MCP (docs ücretsiz) | `npx motion-ai` | **5** |
| [expo/skills → expo-animation](https://github.com/expo/skills) | 2.655 | Expo resmi animasyon skill'i (Emil ile ortak) + RECIPES | `npx skills add expo/skills` | **5** |
| [MengTo/Skills](https://github.com/MengTo/Skills) | 6.618 | `cinematic-gsap-lenis-motion-system`, `build-awwwards-quality-sites`, `animation-systems`, scroll storytelling (17 skill) | `npx skills add MengTo/Skills` | **5** |
| [software-mansion/react-native-reanimated](https://github.com/software-mansion/react-native-reanimated) + llms.txt | 11.020 | RN animasyon motoru (v4 CSS API, layout animations; `runOnJS`→`scheduleOnRN`) | `npx expo install react-native-reanimated react-native-worklets` | **5** |
| [DavidHDev/react-bits](https://github.com/DavidHDev/react-bits) · [magicuidesign/magicui](https://github.com/magicuidesign/magicui) (llms.txt) | 48.534 · 22.472 | Text animasyonu, arka planlar, marquee, bento, blur fade, border beam | shadcn CLI | **5** |
| [darkroomengineering/lenis](https://github.com/darkroomengineering/lenis) + llms.txt | 16.169 | Smooth scroll + ScrollTrigger senkronu | `npm i lenis` | 4 |
| [vercel-labs/agent-skills → react-view-transitions](https://github.com/vercel-labs/agent-skills) | 31.956 | View Transitions + RN animasyon kuralları | `npx skills add vercel-labs/agent-skills` | 4 |
| [LottieFiles/motion-design-skill](https://github.com/LottieFiles/motion-design-skill) | 1.908 | Timing/easing/koreografi ilkeleri (director + patterns) | `npx skills add LottieFiles/motion-design-skill` | 4 |
| Aceternity UI llms.txt · [nolly-studio/cult-ui](https://github.com/nolly-studio/cult-ui) | — · 6.329 | Spotlight, hero highlight, container scroll / 150+ Motion bileşeni | `npx shadcn add @aceternity/<n>` | 4 |
| [software-mansion/react-native-gesture-handler](https://github.com/software-mansion/react-native-gesture-handler) · Expo docs (haptics.md, zoom-transition.md, navigation-transitions.md) · [enzomanuelmangano/demos](https://github.com/enzomanuelmangano/demos) | 6.788 · — · 3.218 | Gesture / haptik + iOS 18 zoom shared element / Reanimated+Skia örnek galerisi | expo install / `.md` ekli URL / ilham | 4 |
| [Shopify/react-native-skia](https://github.com/Shopify/react-native-skia) · [nandorojo/moti](https://github.com/nandorojo/moti) · [lottie-react-native](https://github.com/lottie-react-native/lottie-react-native) · [software-mansion/react-native-screens](https://github.com/software-mansion/react-native-screens) · [react-native-css-animations](https://github.com/software-mansion-labs/react-native-css-animations) | 8.7k · 4.6k · 17.2k · 3.7k · 75 | Shader/canvas / MotiView + skeleton / Lottie / native stack geçişleri / spin-pulse-shimmer presetleri | expo install | 3 |
| [ibelick/motion-primitives](https://github.com/ibelick/motion-primitives) · [shuding/next-view-transitions](https://github.com/shuding/next-view-transitions) · [formkit/auto-animate](https://github.com/formkit/auto-animate) · [romboHQ/tailwindcss-motion](https://github.com/romboHQ/tailwindcss-motion) · [tw-animate-css](https://github.com/Wombosvideo/tw-animate-css) · [DevMartinese/awwwards-animations-skill](https://github.com/DevMartinese/awwwards-animations-skill) · [tristanmanchester/agent-skills](https://github.com/tristanmanchester/agent-skills) | 6.5k · 2.4k · 13.9k · 3.3k · 806 · 15 · 3 | Motion kit / Next.js VT / liste animasyonu / Tailwind utility'leri / GSAP+Motion+Lenis tarifleri / Reanimated4+RNGH3 skill | npm / kopyala | 3 |
| rive-react-native · legend-motion · skeleton-placeholder · react-three-fiber · dylantarre/animation-principles · callstack js-animations · OneWave animate · hairyf motion · motionharvest · catalinmiron | 785 · 450 · 733 · 32.7k · 94 · 1.7k · 321 · — · 2 · ~120 | İkincil / eski / UI dışı | — | 1-2 |

**Önemli bulgular:** GSAP tüm pluginleriyle ücretsiz (ajan eski bilgiyle `.npmrc` token yazmasın) · `framer-motion` → `motion/react` · Reanimated 4'te `.value` → `.get()/.set()` · shared element Reanimated'de deneysel, expo-router iOS 18 zoom geçişi alpha · Expo docs her sayfayı `.md` olarak sunuyor (URL sonuna `.md`).


## 5. Pipeline / fabrika — ayrıntı: `notlar/pipeline-fabrika.md`

| Repo | Yıldız | Ne işe yarar | Nasıl kullanılır | Değer |
|---|---|---|---|---|
| [obra/superpowers](https://github.com/obra/superpowers) | 295.549 | Skill tabanlı SDLC: brainstorm → spec → plan → subagent-driven implement → review → finish; sert HARD-GATE'ler | `/plugin install superpowers@claude-plugins-official` | **5** |
| [github/spec-kit](https://github.com/github/spec-kit) | 140.218 | Spec-Driven Development: constitution → specify → clarify → plan → tasks → implement | `uv tool install specify-cli && specify init --integration claude` | **5** |
| [garrytan/gstack](https://github.com/garrytan/gstack) | 135.325 | "Open source software factory": office-hours → plan-ceo/eng/design-review → autoplan → review → qa (gerçek tarayıcı) → ship → canary; 23 rol | `git clone --depth 1 … ~/.claude/skills/gstack && ./setup` | **5** |
| [snarktank/ralph](https://github.com/snarktank/ralph) | 21.912 | Ralph döngüsü: prd.json'daki her story için taze örnek; UI story'lerinde zorunlu browser doğrulaması | `./scripts/ralph/ralph.sh --tool claude 20` | **5** |
| [OneRedOak/claude-code-workflows](https://github.com/OneRedOak/claude-code-workflows) | 3.896 | design-review ajanı (yukarıda) — görsel QA kapısı | `.claude/agents/` | **5** |
| [anthropics/claude-code plugins/](https://github.com/anthropics/claude-code/tree/main/plugins) | 149.486 | Resmi: `feature-dev`, `ralph-wiggum` (Stop hook döngüsü), `frontend-design`, `code-review` | `/plugin install feature-dev@claude-plugins-official` | 4 |
| [bmad-code-org/BMAD-METHOD](https://github.com/bmad-code-org/BMAD-METHOD) | 53.805 | Rol ajanları (PM, Architect, **UX → DESIGN.md + EXPERIENCE.md**, Dev, QA); review lensleri (edge-case-hunter, verification-gap) | `npx skills add bmad-code-org/BMAD-METHOD` | 4 |
| [EveryInc/compound-engineering-plugin](https://github.com/EveryInc/compound-engineering-plugin) | 25.396 | 36 skill; brainstorm → plan → work → simplify → review → compound; `/lfg` tam otonom (browser test + PR + CI) | `/plugin marketplace add EveryInc/compound-engineering-plugin` | 4 |
| [open-gsd/gsd-core](https://github.com/open-gsd/gsd-core) | 10.197 | discuss → plan → execute (paralel) → verify → ship; `ui-phase`/`ui-review` 6 sütunlu UI kontratı | `npx @opengsd/gsd-core@latest` | 4 |
| [gotalab/cc-sdd](https://github.com/gotalab/cc-sdd) · [tzachbon/smart-ralph](https://github.com/tzachbon/smart-ralph) · [bmad-code-org/bmad-loop](https://github.com/bmad-code-org/bmad-loop) | 3.7k · 554 · 147 | Kiro tarzı SDD / spec-driven Ralph / LLM'siz deterministik orkestratör | `npx cc-sdd@latest` / plugin / `bmad-loop run` | 4 |
| [ChrisBrooksbank/cb-visual-qa](https://github.com/ChrisBrooksbank/cb-visual-qa) · [humbleteam/design-review](https://github.com/humbleteam/design-review) · [nagisanzenin/claude-code-production-grade-plugin](https://github.com/nagisanzenin/claude-code-production-grade-plugin) | 1 · 4 · — | Playwright smoke + `PROMPT_visual-check.md` / 0-4 puanlı UX kritiği (Before/After/Why) / Loop Engine + functional drive | kopyala | 4 |
| [yybd/app-factory](https://github.com/yybd/app-factory) | 1 | 40 skill: App Store/Play yayın zinciri (identity → binary → privacy → media → copy → ship → ASO) | `claude plugin marketplace add yybd/app-factory` | 4 |
| [t3-oss/create-t3-turbo](https://github.com/t3-oss/create-t3-turbo) · [roninoss/create-expo-stack](https://github.com/roninoss/create-expo-stack) | 6.1k · 2.6k | Next.js + Expo monorepo starter / Expo CLI üreteci | `npx create-turbo@latest -e …` / `npx rn-new@latest` | 4 / 3 |
| [Yeachan-Heo/oh-my-claudecode](https://github.com/Yeachan-Heo/oh-my-claudecode) · [michaelshimeles/ralphy](https://github.com/michaelshimeles/ralphy) · [coleam00/context-engineering-intro](https://github.com/coleam00/context-engineering-intro) · [markshust/hcf](https://github.com/markshust/hcf) · [automazeio/ccpm](https://github.com/automazeio/ccpm) | 39.6k · 3.0k · 13.9k · 76 · 8.4k | `/autopilot` / çok motorlu Ralph / PRP / TDD worker'lar / worktree PM | — | 3 |
| [hesreallyhim/awesome-claude-code](https://github.com/hesreallyhim/awesome-claude-code) · [anthropics/claude-cookbooks](https://github.com/anthropics/claude-cookbooks) · [ghuntley.com/ralph](https://ghuntley.com/ralph/) | 55.1k · 53.2k · — | Referans listeleri / agent pattern'ları / Ralph'ın orijinali | — | 3 |
| [wshobson/agents](https://github.com/wshobson/agents) · [VoltAgent/awesome-claude-code-subagents](https://github.com/VoltAgent/awesome-claude-code-subagents) · [davila7/claude-code-templates](https://github.com/davila7/claude-code-templates) · [eyaltoledano/claude-task-master](https://github.com/eyaltoledano/claude-task-master) · [ruvnet/claude-flow](https://github.com/ruvnet/claude-flow) · [buildermethods/agent-os](https://github.com/buildermethods/agent-os) · [Th0rgal/open-ralph-wiggum](https://github.com/Th0rgal/open-ralph-wiggum) · [jezemm/appfactory-plugin](https://github.com/jezemm/appfactory-plugin) | 40.2k · 25.5k · 32.4k · 28.1k · — · 5.5k · 1.9k · 0 | Rol havuzları / şablon kurucu / görev grafiği / meta-harness (ağır) / standart enjeksiyon / tek komut döngü / tescilli fabrika (sadece ilham) | — | 2 |

## 6. Sosyal medya + reklam — ayrıntı: `notlar/sosyal-medya-reklam.md`

| Repo | Yıldız | Ne işe yarar | Nasıl kullanılır | Değer |
|---|---|---|---|---|
| [sergebulaev/instagram-skills](https://github.com/sergebulaev/instagram-skills) | 266 | 9 skill: caption (125 kr hook), carousel planner, 3-5 hashtag, humanizer; Publora ile yayın | plugin (Claude + Codex) | **5** |
| [coreyhaines31/marketingskills](https://github.com/coreyhaines31/marketingskills) | 53.326 | `social` (5 carousel çerçevesi), `copywriting`, `ad-creative`, `ads` (Meta limitleri, TCPL), `image` | `npx skills add coreyhaines31/marketingskills --skill social` | **5** |
| [marcolang/Marketing-Skills → instagram-carousel](https://github.com/marcolang/Marketing-Skills) | 64 | Tek renkten 6-token palet, tipografi tablosu, 7 slayt dizisi, Playwright dsf 2.5714 → 1080×1350 | SKILL.md kopyala | **5** |
| [Ash-Harris/carousel-generator](https://github.com/Ash-Harris/carousel-generator) | 4 | 16 "non-negotiable" tipografi/yerleşim kuralı, hook A/B grid, export.mjs | `npx skills add` | **5** |
| [idrsdev/social-carousel-generator](https://github.com/idrsdev/social-carousel-generator) | 2 | `design-system.json` tek doğruluk kaynağı, base64 font, Playwright 4:5 PNG | plugin | **5** |
| [oliverames/meta-mcp-server](https://github.com/oliverames/meta-mcp-server) | 47 | 199 araç: `meta_publish_instagram_carousel`, insights, ads; Graph API özetleri | `npx -y @oliverames/meta-mcp-server` | **5** |
| [facebook/facebook-python-business-sdk](https://github.com/facebook/facebook-python-business-sdk) | 1.606 | Resmi Meta Marketing API SDK (campaign → adset → ad PAUSED) | `pip install facebook_business` | **5** |
| Instagram Graph API Content Publishing (resmi) | — | container → publish, carousel 2-10, sadece JPEG, public URL, 100 post/24s | curl/SDK | **5** |
| [gitroomhq/postiz-app](https://github.com/gitroomhq/postiz-app) | 36.714 | Self-host zamanlayıcı, Public API, MCP, n8n (AGPL-3.0) | Docker + API key | 4 |
| [publora/skills](https://github.com/publora/skills) | 49 | Hosted MCP, instagram-post skill | `claude mcp add publora` | 4 |
| [pipeboard-co/meta-ads-mcp](https://github.com/pipeboard-co/meta-ads-mcp) · [AgriciDaniel/claude-ads](https://github.com/AgriciDaniel/claude-ads) · [irinabuht12-oss/marketing-skills](https://github.com/irinabuht12-oss/marketing-skills) · [mathiaschu/meta-ads-analyzer](https://github.com/mathiaschu/meta-ads-analyzer) | 1.3k · 9.7k · 3.5k · 439 | Hosted MCP 42 araç / 12 platform ops read-only varsayılan / meta-ads-audit, creative-fatigue, client-report / Breakdown Effect analizi | `claude mcp add` / plugin | 4 |
| [rediumvex/social-media-caption-generator-claude](https://github.com/rediumvex/social-media-caption-generator-claude) · [johnnyang0612/bearcarousel](https://github.com/johnnyang0612/bearcarousel) · [dean9703111/ig-card-generator](https://github.com/dean9703111/ig-card-generator) · [Maartenlouis/remotion-ads](https://github.com/Maartenlouis/remotion-ads) | 129 · 3 · 13 · 60 | 7 platform caption formülleri / JSON→PNG + WCAG audit / Markdown DSL → Puppeteer / Reels 9:16 + TTS | — | 4 |
| [kingbootoshi/nano-banana-2-skill](https://github.com/kingbootoshi/nano-banana-2-skill) · [danielgatis/rembg](https://github.com/danielgatis/rembg) | 414 · 24.964 | Gemini görsel üretimi (4:5, referans, şeffaf PNG) / ürün arka plan silme | `bun link` / `rembg i` | 4 |
| [vercel/satori](https://github.com/vercel/satori) · [remotion-dev/remotion](https://github.com/remotion-dev/remotion) · [remotion-dev/skills](https://github.com/remotion-dev/skills) | 14.0k · 62.0k · 4.8k | Tarayıcısız HTML→SVG→PNG / video+still (şirket lisansı ücretli) | — | 3 |
| [charlie947/social-media-skills](https://github.com/charlie947/social-media-skills) · [zubair-trabzada/ai-marketing-claude](https://github.com/zubair-trabzada/ai-marketing-claude) · [itchernetski/threads-carousel-claude-skill](https://github.com/itchernetski/threads-carousel-claude-skill) · [irinabuht12-oss/google-ads-meta-ads-mcp](https://github.com/irinabuht12-oss/google-ads-meta-ads-mcp) · [ivangfalco/ads-skills](https://github.com/ivangfalco/ads-skills) | 3.8k · 2.7k · 107 · 3.9k · 278 | voice-builder, hook-generator / 30 gün takvim / 12 slayt tipi / hosted Google+Meta MCP / Meta OS referansı (Commons Clause) | — | 3 |
| facebook-nodejs-business-sdk · mikusnuz/meta-ads-mcp · inovector/mixpost · Hao0321/claude-skill-social-post · kostja94/marketing-skills · ScrapeCreators · html2canvas · polotno · n8n şablonları | — | İkincil alternatifler | — | 2 |

**Türkçe'ye özel skill bulunamadı** → `tr-caption` SKILL.md'yi kendimiz yazacağız (bkz. sosyal medya notu §4).
