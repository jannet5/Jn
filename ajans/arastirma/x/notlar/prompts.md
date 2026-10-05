# Prompt'lar — X.com'dan toplanan, aynen kopyalanmış prompt metinleri

Her başlıkta: kim paylaştı, ne için, prompt metni (İngilizce orijinal aynen; Türkçe olanlar aynen), link.
Bizim zincirde nerede kullanılır notu en altta.

---

## 1. Mobil app için tek seferlik spec üretici (Matt Shumer → Rork)

Kaynak: https://x.com/mattshumer_/status/1957839186653761546 — "Here's my powerful prompt for building a mobile (iOS + Android) app in one shot. Use this to build a spec, then paste it into @rork."
Kullanım: Müşteri brief'ini bu prompt'a sok, çıkan "App Spec"i Claude Code / Rork / Expo'ya ver.

```
You are my expert Mobile PM + Product Designer + React Native Tech Lead.

Goal: Turn my idea into a crystal‑clear spec I can paste into Rork to build a great v1 — with NO custom servers unless needed. 
Assume:
- Rork generates React Native + Expo apps with instant previews.
- The app can call external APIs.
- For secrets/secure logic, use Rork Backend Functions (serverless) and ask me for any keys you need during a guided setup.
- There is no built‑in database or auth by default; if I ask for those, run a guided setup to connect external services (e.g., Supabase) and/or create Rork Backend Functions. 
- Analytics is optional (default OFF).

Your approach
1) Ask concise "Discovery" questions first, then output the final **Rork‑Ready App Spec (Front‑End + Guided Backend)** exactly as defined below. 
2) Experience first: what the app should feel like, key screens, flows, and copy.
3) No jargon. Use plain language. If I leave blanks, choose sensible defaults and mark them [ASSUMPTION].
4) When a feature needs secrets or provisioning, include a **Guided Setup** section that Rork can follow: the exact questions to ask me, the keys/IDs to collect, the checks to run, and which calls must use a Rork Backend Function.

--------------------------------
DISCOVERY (ask these, grouped & concise, then WAIT)
--------------------------------

A) Vision & Users
1) App name + one‑line pitch?
2) Who is it for (one sentence)? What problem do they solve with the app?
3) In <2 minutes, what should a new user be able to do?

B) Core Experience
4) Pick an app shape: [Home feed] [Task/Checklist] [Tracker/Logger] [Social/Community] [Booking/Scheduling] [Notes/Journal] [Learn/Practice] [Shop] [Other: ___]
5) Top 3 things users do every session (short verbs).
6) Top 3 frustrations to avoid.

C) Screens & Navigation
7) Layout: [Tabs (≤5)] [Single flow (Stack)] [Home + Details] [Drawer]
8) Up to 6 screen names (or say "You decide").
9) For each screen, what should a user be able to do? (bullets)

D) Content (plain words — no schemas)
10) What "things" does the app handle? (e.g., workouts, notes, events, photos)
11) For each thing, what details matter? (e.g., for "workout": title, duration, notes)
12) Give 2–3 realistic examples per thing.

E) Live Data & Integrations
13) Do you need live/shared data? [No (local‑only)] [Yes: from APIs] [Yes: Supabase (guided)] [Other service: ___]
14) If APIs: name the service(s) + what data you want (or say "Suggest APIs/endpoints").
15) Any private keys involved? [Yes/No]. If Yes, we'll route those calls via Rork Backend Functions.

F) Sign‑in / Paywall / Notifications (optional)
16) Sign‑in: [No login] [Email/Password] [Magic link] [Apple] [Google]
17) Subscriptions or one‑time unlocks? If yes: what does paid unlock?
18) Push notifications? [No] [Yes]. If Yes: 3 triggers + example copy.

G) Visual Vibe & Tone
19) 6–8 vibe words (e.g., calm, bold, playful, premium).
20) Reference apps/sites (and what you like).
21) Copy tone: [Friendly] [Direct] [Playful] [Professional] [Coach‑like]

H) Access & Inclusion
22) Languages/regions? Accessibility must‑haves (font size, high contrast, VoiceOver)?
23) Any sensitive content or age limits?

I) Analytics (optional)
24) Analytics: [Off (default)] [On]. If ON: the 3 events that matter most.

--------------------------------
DELIVERABLES & OUTPUT FORMAT
--------------------------------

# Rork‑Ready App Spec (Front‑End + Guided Backend)

## 1) Product Snapshot
- One‑liner:
- Who it's for:
- Day‑1 promise (<2 minutes):
- v1 success (simple outcomes/metrics):

## 2) Experience Storyboard (3–5 tiny scenes)
Scene = Trigger → What user sees → What they do → What they feel → Success

## 3) Screens & Navigation
- App layout:
- Tabs (if any): names/icons/order
- Screen inventory:

| ID | Screen | Why it exists | Key actions | Empty state |
|----|--------|---------------|-------------|-------------|

- Navigation rules (what opens from where; back behavior)

## 4) Core Flows (step lists with friendly errors)
- Flow A: Trigger → Step 1 → Step 2 → … → Success / Failure (+ message)
- Flow B: …
- Flow C: …
Include quick "what might go wrong?" and the exact user‑facing message.

## 5) Content & Data (simple, auto‑inferred)
Describe in plain language (no schemas):
- The "things" the app handles and the details we store for each
- How users add/edit them (forms/lists)
- Example items (cleaned from my samples)
- Storage notes: [On device], [Fetched from <API>], or [Saved via Rork Backend Function]
```
(Devamı — bölüm 6+: Integrations & Guided Setup, Sign‑in/Paywall/Notifications, Visual System, Copy, Accessibility, Analytics, Build Plan — orijinal tweet'te. Tam ham kopya: `../_ham/1957839186653761546.md` yoksa linke bak.)

---

## 2. Referans ekran görüntülerinden DESIGN.md çıkarma (Vox)

Kaynak: https://x.com/Voxyz_ai/status/2106474370860548341
Adımlar: (1) Godly / Refero / One Page Love'dan 3-5 beğendiğin site, ekran görüntülerini Claude Code'a sürükle. (2) Seçtiklerini DESIGN.md yap, CLAUDE.md'ye `@DESIGN.md` ile import et. (3) `npx skills add Leonxlnx/taste-skill --skill design-taste-frontend` ve `npx impeccable install` → `/impeccable init`.

Ekran görüntüleriyle birlikte gönderilecek prompt:
```
These are screenshots of sites I like. Don't touch any code yet:
1. For each one, tell me what it does well: layout, typography, color, spacing, white space.
2. List what they have in common, ask me which parts I want, and wait for my answer before going on.
3. Write a DESIGN.md in the project root based on what I pick, with real values: font sizes, weights, color codes, spacing, corner radius. Mark anything you can't tell from the screenshots as an estimate. Don't make it up. Keep it short, one screen at most.
4. Import DESIGN.md into the root CLAUDE.md with an @ line, not inside backticks. Then add a rule: after every page you finish, run /impeccable polish.
```

### 2b. Yayın öncesi 5 subagent / 20 kontrol (aynı hesap, alıntılanan tweet)
Tüm metni Claude Code'a yapıştır:
```
Claude Code tip: before you ship a vibe-coded site, have Opus send out 5 subagents to check these 20 things, then fix them all in one pass:

Consistent design
→ Write a DESIGN.md first, and make every color, font size, spacing and corner radius follow it
→ Keep the palette restrained and the type hierarchy clear
→ Make every button, card and input of the same kind look alike
→ Too flat? Add emphasis. Too crowded? Cut
→ Keep text readable on both dark and light backgrounds

On mobile
→ No sideways scrolling, and nothing spilling off the screen
→ Add a mobile menu
→ Make buttons big enough to tap
→ Keep the layout intact when the text is enlarged

Every state
→ Give loading, empty and error states a proper screen
→ Give every button feedback, with its own hover, pressed and disabled look
→ Show form errors right where they happen, plus submitting, success and failure messages
→ Add transitions to modals, dropdowns and tab switches

Use it like a real user
→ Click through the core flows, like sign-up and checkout, from start to finish
→ Find buttons that do nothing and links that are broken
→ Make sure the whole thing works with just a keyboard

Before launch
→ Say what you do in one line on the home page
→ Give each page one main button you want people to click
→ Give every page a title, a description and a favicon
→ Delete all placeholder text

Send one subagent per group to check it without editing anything. Then fix everything yourself from their lists, so no two agents edit the same file. If a group is more complex, raise that subagent's effort. Show me the list of issues first, don't change anything until I confirm, and when you're done, give me before-and-after screenshots.
```

---

## 3. Premium web sitesi: 5 bloklu build prompt'u + 3 cila geçişi (tonny, Türkçe)

Kaynak: https://x.com/bettercalltonny/article/2085766984088649924 — "Claude Code ile Premium Web Siteleri Oluşturun"

Ön hazırlık: `.claude/skills/` içine frontend-design + ui-ux-pro-max; Awwwards/Dribbble'dan 3 site × (hero + 1 bölüm + footer) = 9 ekran görüntüsü `ref-1.png, ref-2.png, ref-3.png`.
Referans verirken birebir şu cümle: **"Bu referanslardaki tipografi ölçeğini, boşluk ritmini ve hareketi eşleştir. Layout'ları kopyalama."**

Build prompt'u (tek mesaj, 5 blok):
```
Kitle: "Bu site, seans başına 2.000$+ ücret alan serbest fotoğrafçılar için."
Tek aksiyon: "Her sayfa, bir görüşme rezervasyonuna yönlendiriyor. Tek CTA, tekrarlanarak."
Referanslar: "ref-1.png, ref-2.png, ref-3.png'i kalite barı olarak kullan."
Stack: "Astro, Tailwind, Cloudflare Pages'e deploy. Statik, hızlı, CMS yok."
Yasaklı liste: "Yasak: mor gradyanlar, ikon olarak emoji, display font olarak Inter, jenerik stok fotoğraf placeholder'ları, her şeyin ortalandığı layout'lar."
```

Cila geçişleri (3 AYRI mesaj — aynı anda istersen biri iyi ikisi kötü olur):
```
Geçiş 1 - sadece tipografi: "Her başlık ve gövde metni boyutunu incele. Katı bir tip ölçeği kur. Satır yüksekliğini ve harf aralığını düzelt. Başka hiçbir şeye dokunma."
Geçiş 2 - sadece boşluk: "Dikey ritmi bölüm bölüm denetle. Sıkışık hissettiren bölümlerde boşluğu iki katına çıkar. Başka hiçbir şeye dokunma."
Geçiş 3 - sadece hareket: "Scroll-reveal ve hover durumları ekle. İnce olsun. 200-300ms. Hiçbir şey zıplamasın."
Mobil: "Her sayfayı 375px genişlikte göster ve bozulan yerleri düzelt."
```
Yayın: git push → Cloudflare Pages → build `npm run build`, output `dist`.

---

## 4. Landing page anti-slop prompt zinciri (Ballaz)

Kaynak: https://x.com/MrBallaz/status/2024071716763242781
```
Prompt 1: "Read frontend-design skill, generate structure only, no styling"
Prompt 2: "Now style it: Plus Jakarta Sans, Phosphor icons, framer-motion, [color palette]"
Prompt 3: "Center the hero text"
Prompt 4: "Enlarge the CTA a bit"
```
Kural cümleleri: `"Plus Jakarta Sans for everything, no exceptions"` · `"Use phosphor-icons only"` · `"Flat colors only. If I see a gradient, regenerate"` · `"Use shadcn/ui components, but customize the hell out of them"` · `"Buttons: scale on press, lift shadow on hover"` · `"Cards: subtle border glow on hover, smooth transform"` · `"Nav: underline slide animation, not fade"` · Renk verirken: `"#3B82F6 primary, #1E40AF hover state, #DBEAFE backgrounds"`.
Glassmorphism: `"Add glassmorphism effects: backdrop-filter blur, subtle borders, and floating liquid bubble backgrounds with CSS gradients. Use border-radius: 20px minimum."`

---

## 5. Sıfır kod ilk prompt (Yarchi, iPhone oyunu 2 günde App Store)

Kaynak: https://x.com/undefinedKi/article/2055641692401754135
```
I'm building an iPhone app, and I want it on the App Store. The idea: [your one-sentence idea here]. I've never built a mobile app and I've never used Claude Code before. I'll be working from Windows, with React Native. Please create all the files and folders I'll need for this.
```
Üç şart: hedef (App Store), seviyen, OS'in. Tasarım için: readdy'ye prompt'u Claude'a yazdır → readdy ekranlarının ekran görüntülerini proje klasörüne at →
```
The design I want is in this folder. Match it in our app.
```
Apple'ın zorunlu kıldıkları (yoksa red): privacy policy + iletişim URL'si (Netlify'da ücretsiz), onboarding (2-3 ekran), settings ekranı, loading state. Developer Program: Individual seç (Organization DUNS ister). Bundle ID app.json ile birebir aynı.

---

## 6. Claude'u app'inin ürün müdürü yap (Hendrik Haandrikman)

Kaynak: https://x.com/HHaandr/status/2103852436041441310
Hazırlık: `brew install asc` → App Store Connect API key (Admin) → `asc auth login`; Claude Code'da RevenueCat plugin'i bağla.
```
Act as a world-class mobile product manager reviewing {app name}, my app that helps {who} {do what}

Build and run it in the iOS Simulator. Go through it like a first-time user: onboarding, core feature, paywall. Screenshot every bit of friction

Pull the last 90 days from RevenueCat (trials, trial conversion, churn, revenue) and App Store Connect via asc (impressions, product page views, downloads), plus recent App Store reviews

Find the biggest leak in the funnel: from store page to download to trial to paid to renewal

Propose 3 iterations: one that makes it easier to use, one that brings people back, one that converts more to paid. For each: the evidence, the change, the metric it should move, the effort

Put it all in one dashboard. Don't touch the code yet.
```

---

## 7. PRD prompt'u (Harshil Tomar)

Kaynak: https://x.com/Hartdrawss/status/2051957694454444370
```
Create a Product Requirements Document for [project name].

Include:
- Problem statement: the exact user pain this solves in one sentence
- Target user: one specific persona (not 'everyone')
- Core user journey: what the user does from signup to first value, step by step
- MVP feature list: only features required for that core journey. Nothing else.
- Out of scope: explicitly list what we are NOT building in v1
- Success metric: one number that tells us if this worked in 30 days
- Tech constraints: stack, integrations, third-party APIs required
- Open questions: things we need to decide before dev starts

For each feature in the MVP list, add: priority (P0/P1/P2), estimated complexity (S/M/L), and which user journey step it serves.

Flag anything that sounds like a phase 2 feature disguised as a phase 1 requirement.
```

---

## 8. Vibe coding döngüsü (Prajwal Tomar, 11 adım)

Kaynak: https://x.com/PrajwalTomar_/status/1947272871967174720
```
1/ Load the full project context (PRD, Implementation Plan, etc.)
2/ Pick up a feature from the implementation plan
3/ Ask for different approaches first, not the code
4/ Pick the best approach and ask for a detailed action plan
5/ Review the plan carefully
6/ Pull up API docs if needed, review them, and attach them inside Cursor
7/ Ask Cursor to stick to the plan and build the feature
8/ Ask for testing instructions and test the feature properly
9/ Commit the changes
10/ Ask Cursor what makes sense to build next
11/ Start a new chat and repeat this flow until you ship.
```

---

## 9. Tasarım onay döngüsü + Refero referans prompt'u (Tom Crawshaw, Claude Design rehberi)

Kaynak: https://x.com/tomcrawshaw01/article/2093323226797744557
Referans design system'den öğren (styles.refero.design):
```
Study this design system and use it as a reference for my dashboard. Keep the underlying ideas about type, spacing, colour and hierarchy, but create an original interface for my product. Explain which choices you are carrying across and which ones you are changing.
```
Onay döngüsü:
```
Before you build, propose the design direction and wait for my approval. After I approve it, create the first version. Then review it against the checklist below, show me what failed and wait for approval before making the final changes.
```
Checklist: hiyerarşi ana aksiyonu belli ediyor mu · yön ürüne/kitleye uygun mu · tip/renk/boşluk tutarlı mı · hedef ekran boyutları · klavye ile kullanım · focus/kontrast/empty/error state'ler · gerçek içerik (filler yok) · "jenerik AI default" gibi duran yer var mı.
İyi ilk prompt 4 şey söyler: amaç, layout, içerik, kitle. Örnek:
```
Create a desktop dashboard for a finance team showing monthly revenue, new customers and churn. Put the three headline metrics across the top, a revenue chart below them, and filters for region and product line in the upper right. Use our existing dashboard components and make the hierarchy easy to scan during a weekly review.
```

---

## 10. Design system'i input olarak verirken kilit cümle (Mnimiy / Moonchild)

Kaynak: https://x.com/Mnilax/article/2065161044587426265
Proje seviyesinde kalıcı talimat: **"do not introduce new colors, stop and ask"** — modelin boşluk doldurma refleksini "bayrak kaldır"a çevirir. Sonuç: 10 ekranda on-brand 2→9, icat edilen tek seferlik hex 19→0.

---

## 11. Instagram 9'lu feed tek prompt (Nano Banana Pro)

Kaynak: https://x.com/junwatu/status/1993887904117223684 (alıntı @gizakdag)
```
Create a 9-image Instagram feed for this product in the same aesthetic. Use different locations, angles, and compositions, incorporating people, animals, nature, and various environments while maintaining a cohesive visual style.
```

---

## 12. Sıfır kodla site (Yiğit Akın Kaya, Türkçe)

Kaynak: https://x.com/yigitakinkaya/status/2057708924300669156
```
"[İSİM] adında bir GitHub repo oluştur. Ben kod bilmiyorum. Her şeyi sen yaz. [HEDEF] için [BAŞARI KRİTERİ] istiyorum. İşte bir örnek [ekran görüntüsü ekle]."
```
Ayarlar: Opus seç; Shift+Tab ×2 Plan modu; `~/.claude/settings.json` → `"permissions": {"defaultMode": "bypassPermissions"}` (dikkat). Dosya referansı için `(Header.jsx)` gibi yaz.

---

## 13. Rork CEO'nun tasarım prompt ipucu

Kaynak: https://x.com/daniel_dhawan/article/2086873090391511406
Paleti değil kişiyi tarif et: `"Design this for someone who wants to become more productive and successful, make it feel sleek and futuristic"`. Önce görünüm, sonra mantık. Beğenmediğinde yama yapma: önceki sürüme dön, orijinal prompt'u yeniden yaz.

---

## 14. $1.800'lık landing page'i $70'a (Gipp'in anlattığı yöntem)

Kaynak: https://x.com/gippp69/status/2062499588838228166
5 referans ekran görüntüsü ver → **önce 7 soru sormasını iste** → tek net görsel yöne it → 2. geçiş: daha iyi tipografi, koyu palet, mobil temizlik, cursor efektleri, 6 micro-interaction, özel hero görseli.

---

## 15. Expo + Swift için CLAUDE.md "Native iOS" bölümü (Evan Bacon)

Kaynak: https://x.com/Baconbrix/status/1982180394297393257 (görselden)
```
# Native iOS

- ALWAYS use only Expo modules API.
- Prefer Swift and Kotlin.
- New Architecture only. NEVER support legacy React Native architecture.
- Design native APIs as if you contributing W3C specs for the browser, take inspiration from modern web modules. eg `std:kv-storage`, `clipboard`.
- Prefer string union types for API options instead of boolean flags, enums, or multiple parameters. eg instead of `capture(options: { isHighQuality: boolean })`, use `capture(options: { quality: 'high' | 'medium' | 'low' })`.
- Use optionality for availability checks as opposed to extraneous `isAvailable` functions or constants. eg `snapshot.capture?.()` instead of `snapshot.isAvailable && snapshot.capture()`.

## Views

Prefer functions on views instead of `useImperativeHandle` + `findNodeHandle`.
```

---

## 16. Viktor Oddy — AI web ajansı landing page tam yeniden üretim prompt'u

Kaynak: https://x.com/viktoroddy/status/2035366371530617238 — bizim ajans vitrinine en yakın şablon. Tam metin aşağıda (tweet'ten aynen).

```
Build a single-page landing page for an AI-powered web design agency using React + Vite + TypeScript + Tailwind CSS + shadcn/ui. The aesthetic is dark, premium, Apple-inspired with a custom "liquid glass" morphism effect. Pure black background throughout.

FONTS & DESIGN SYSTEM
Google Fonts import:

Instrument Serif (italic) — headings
Barlow (300, 400, 500, 600) — body text
Tailwind config — extend fontFamily:

heading: ["'Instrument Serif'", "serif"]
body: ["'Barlow'", "sans-serif"]
CSS Variables (:root in index.css):

--background: 213 45% 67%;
--foreground: 0 0% 100%;
--primary: 0 0% 100%;
--primary-foreground: 213 45% 67%;
--border: 0 0% 100% / 0.2;
--radius: 9999px;
--font-heading: 'Instrument Serif', serif;
--font-body: 'Barlow', sans-serif;
All headings use: font-heading italic text-white tracking-tight leading-[0.9] All body text uses: font-body font-light text-white/60 text-sm All buttons use: font-body with rounded-full

LIQUID GLASS CSS (in @layer components)
Two variants — .liquid-glass (subtle) and .liquid-glass-strong (more visible):

.liquid-glass:

background: rgba(255, 255, 255, 0.01);
background-blend-mode: luminosity;
backdrop-filter: blur(4px);
border: none;
box-shadow: inset 0 1px 1px rgba(255, 255, 255, 0.1);
position: relative;
overflow: hidden;
::before pseudo-element — a gradient border mask:

content: '';
position: absolute; inset: 0;
border-radius: inherit;
padding: 1.4px;
background: linear-gradient(180deg,
  rgba(255,255,255,0.45) 0%, rgba(255,255,255,0.15) 20%,
  rgba(255,255,255,0) 40%, rgba(255,255,255,0) 60%,
  rgba(255,255,255,0.15) 80%, rgba(255,255,255,0.45) 100%);
-webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
-webkit-mask-composite: xor;
mask-composite: exclude;
pointer-events: none;
.liquid-glass-strong: Same but backdrop-filter: blur(50px), stronger box-shadow: 4px 4px 4px rgba(0,0,0,0.05), inset 0 1px 1px rgba(255,255,255,0.15), and slightly higher gradient opacity (0.5 / 0.2).

SECTION 1 — NAVBAR (fixed)
Fixed at top-4, full-width, z-50. Left: logo image (48×48). Center: a liquid-glass rounded-full pill containing nav links ("Home", "Services", "Work", "Process", "Pricing") as text-sm font-medium text-foreground/90 + a solid white bg-white text-black rounded-full "Get Started" button with ArrowUpRight icon.

SECTION 2 — HERO (1000px height)
Container: relative overflow-visible, height 1000px, black background.

Background video:

src: https://d8j0ntlcm91z4.cloudfront.net/user_38xzZboKViGWJOttwIXH07lWA1P/hf_20260307_083826_e938b29f-a43a-41ec-a153-3d4730578ab8.mp4
Position: absolute, top: 20%, w-full h-auto object-contain z-0
Autoplay, loop, muted, playsInline
Poster fallback image at /images/hero_bg.jpeg
Overlays:

absolute inset-0 bg-black/5 z-0 (light darkening)
Bottom gradient: absolute bottom-0 left-0 right-0 z-[1], height 300px, linear-gradient(to bottom, transparent, black)
Content (z-10, centered, paddingTop 150px):

Badge pill: liquid-glass rounded-full containing a white bg-white text-black rounded-full "New" tag + "Introducing AI‑powered web design." text
Heading: Use a BlurText animation component — text: "The Website Your Brand Deserves" — text-6xl md:text-7xl lg:text-[5.5rem] font-heading italic text-foreground leading-[0.8] tracking-[-4px] — animates word-by-word from bottom with blur-to-clear effect (delay 100ms per word)
Subtext (motion.p): "Stunning design. Blazing performance. Built by AI, refined by experts. This is web design, wildly reimagined." — fades in with blur at 0.8s delay
CTA buttons (motion.div, 1.1s delay): liquid-glass-strong rounded-full "Get Started" + ArrowUpRight icon, and a text-only "Watch the Film" + Play icon
Partners bar at bottom (mt-auto pb-8 pt-16)
BlurText component: Uses motion/react (framer-motion). Splits text by words, each word animates via IntersectionObserver with filter: blur(10px) → blur(5px) → blur(0px), opacity: 0 → 0.5 → 1, y: 50 → -5 → 0. Step duration 0.35s.

SECTION 3 — PARTNERS BAR
Centered column. Top: liquid-glass rounded-full badge "Trusted by the teams behind". Below: horizontal row of partner names ("Stripe", "Vercel", "Linear", "Notion", "Figma") rendered as text-2xl md:text-3xl font-heading italic text-white, gap-12.

SECTION 4 — START SECTION ("How It Works")
Full-width section, min-height: 700px, py-32 px-6 md:px-16 lg:px-24.

Background HLS video:

src: https://stream.mux.com/9JXDljEVWYwWu01PUkAemafDugK89o01BR6zqJ3aS9u00A.m3u8
Use hls.js library. Absolute, full cover, z-0. Top + bottom fade gradients (200px each, black ↔ transparent).

Content (z-10, centered, min-height 500px):

Badge: liquid-glass rounded-full — "How It Works"
Heading: "You dream it. We ship it." — text-4xl md:text-5xl lg:text-6xl font-heading italic
Subtext: "Share your vision. Our AI handles the rest—wireframes, design, code, launch. All in days, not quarters."
Button: liquid-glass-strong rounded-full "Get Started" + ArrowUpRight
SECTION 5 — FEATURES CHESS (alternating rows)
py-24 px-6 md:px-16 lg:px-24. Header: badge "Capabilities", heading "Pro features. Zero complexity."

Row 1 (text left, image right):

H3: "Designed to convert. Built to perform."
P: "Every pixel is intentional. Our AI studies what works across thousands of top sites—then builds yours to outperform them all."
Button: liquid-glass-strong rounded-full "Learn more"
Image: GIF in liquid-glass rounded-2xl overflow-hidden container
Row 2 (image left, text right — using lg:flex-row-reverse):

H3: "It gets smarter. Automatically."
P: "Your site evolves on its own. AI monitors every click, scroll, and conversion—then optimizes in real time. No manual updates. Ever."
Button: "See how it works"
SECTION 6 — FEATURES GRID (4 columns)
Badge "Why Us", heading "The difference is everything."

4 cards in grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6. Each card: liquid-glass rounded-2xl p-6. Contains:

Icon in liquid-glass-strong rounded-full w-10 h-10 circle
Title: text-lg font-heading italic text-white
Description: text-white/60 font-body font-light text-sm
Cards:

⚡ Zap — "Days, Not Months" — "Concept to launch at a pace that redefines fast."
🎨 Palette — "Obsessively Crafted" — "Every detail considered. Every element refined."
📊 BarChart3 — "Built to Convert" — "Layouts informed by data. Decisions backed by performance."
🛡️ Shield — "Secure by Default" — "Enterprise-grade protection comes standard."
SECTION 7 — STATS
Background HLS video:

src: https://stream.mux.com/NcU3HlHeF7CUL86azTTzpy3Tlb00d6iF3BmCdFslMJYM.m3u8
Desaturated: style={{ filter: 'saturate(0)' }}. Top + bottom black fades (200px).

Content (z-10): liquid-glass rounded-3xl p-12 md:p-16, grid grid-cols-2 lg:grid-cols-4 gap-8 text-center:

"200+" — "Sites launched"
"98%" — "Client satisfaction"
"3.2x" — "More conversions"
"5 days" — "Average delivery"
Values: text-4xl md:text-5xl lg:text-6xl font-heading italic text-white Labels: text-white/60 font-body font-light text-sm

SECTION 8 — TESTIMONIALS
Badge "What They Say", heading "Don't take our word for it."

3-column grid. Each: liquid-glass rounded-2xl p-8. Quote in text-white/80 font-body font-light text-sm italic. Name: text-white font-body font-medium text-sm. Role: text-white/50 font-body font-light text-xs.

Testimonials:

Sarah Chen, CEO Luminary — "A complete rebuild in five days..."
Marcus Webb, Head of Growth Arcline — "Conversions up 4x..."
Elena Voss, Brand Director Helix — "They didn't just design our site..."
SECTION 9 — CTA FOOTER
Background HLS video:

src: https://stream.mux.com/8wrHPCX2dC3msyYU9ObwqNdm00u3ViXvOSHUMRYSEe5Q.m3u8
Top + bottom black fades (200px).

Content (z-10, centered):

Heading: "Your next website starts here." — text-5xl md:text-6xl lg:text-7xl
Subtext: "Book a free strategy call. See what AI‑powered design can do."
Two buttons: liquid-glass-strong "Book a Call" + solid bg-white text-black "View Pricing"
Footer: mt-32 pt-8 border-t border-white/10, copyright "© 2026 Studio" + links (Privacy, Terms, Contact) in text-white/40 text-xs
DEPENDENCIES
hls.js, motion (framer-motion), lucide-react, tailwindcss-animate
KEY PATTERNS
All section badges use: liquid-glass rounded-full px-3.5 py-1 text-xs font-medium text-white font-body inline-block mb-4
All section headings use: text-4xl md:text-5xl lg:text-6xl font-heading italic text-white tracking-tight leading-[0.9]
All video sections use HLS via hls.js with Safari fallback (canPlayType)
All video fades: 200px height, linear-gradient(to bottom/top, black, transparent)
Outer page wrapper: bg-black overflow-visible
Here's the complete, detailed prompt to recreate this landing page:

PROMPT TO RECREATE LANDING PAGE
Build a single-page landing page for an AI-powered web design agency using React + Vite + TypeScript + Tailwind CSS + shadcn/ui. The aesthetic is dark, premium, Apple-inspired with a custom "liquid glass" morphism effect. Pure black background throughout.

FONTS & DESIGN SYSTEM
Google Fonts import:

Instrument Serif (italic) — headings
Barlow (300, 400, 500, 600) — body text
Tailwind config — extend fontFamily:

heading: ["'Instrument Serif'", "serif"]
body: ["'Barlow'", "sans-serif"]
CSS Variables (:root in index.css):

--background: 213 45% 67%;
--foreground: 0 0% 100%;
--primary: 0 0% 100%;
--primary-foreground: 213 45% 67%;
--border: 0 0% 100% / 0.2;
--radius: 9999px;
--font-heading: 'Instrument Serif', serif;
--font-body: 'Barlow', sans-serif;
All headings use: font-heading italic text-white tracking-tight leading-[0.9] All body text uses: font-body font-light text-white/60 text-sm All buttons use: font-body with rounded-full

LIQUID GLASS CSS (in @layer components)
Two variants — .liquid-glass (subtle) and .liquid-glass-strong (more visible):

.liquid-glass:

background: rgba(255, 255, 255, 0.01);
background-blend-mode: luminosity;
backdrop-filter: blur(4px);
border: none;
box-shadow: inset 0 1px 1px rgba(255, 255, 255, 0.1);
position: relative;
overflow: hidden;
::before pseudo-element — a gradient border mask:

content: '';
position: absolute; inset: 0;
border-radius: inherit;
padding: 1.4px;
background: linear-gradient(180deg,
  rgba(255,255,255,0.45) 0%, rgba(255,255,255,0.15) 20%,
  rgba(255,255,255,0) 40%, rgba(255,255,255,0) 60%,
  rgba(255,255,255,0.15) 80%, rgba(255,255,255,0.45) 100%);
-webkit-mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
-webkit-mask-composite: xor;
mask-composite: exclude;
pointer-events: none;
.liquid-glass-strong: Same but backdrop-filter: blur(50px), stronger box-shadow: 4px 4px 4px rgba(0,0,0,0.05), inset 0 1px 1px rgba(255,255,255,0.15), and slightly higher gradient opacity (0.5 / 0.2).

SECTION 1 — NAVBAR (fixed)
Fixed at top-4, full-width, z-50. Left: logo image (48×48). Center: a liquid-glass rounded-full pill containing nav links ("Home", "Services", "Work", "Process", "Pricing") as text-sm font-medium text-foreground/90 + a solid white bg-white text-black rounded-full "Get Started" button with ArrowUpRight icon.

SECTION 2 — HERO (1000px height)
Container: relative overflow-visible, height 1000px, black background.

Background video:

src: https://d8j0ntlcm91z4.cloudfront.net/user_38xzZboKViGWJOttwIXH07lWA1P/hf_20260307_083826_e938b29f-a43a-41ec-a153-3d4730578ab8.mp4
Position: absolute, top: 20%, w-full h-auto object-contain z-0
Autoplay, loop, muted, playsInline
Poster fallback image at /images/hero_bg.jpeg
Overlays:

absolute inset-0 bg-black/5 z-0 (light darkening)
Bottom gradient: absolute bottom-0 left-0 right-0 z-[1], height 300px, linear-gradient(to bottom, transparent, black)
Content (z-10, centered, paddingTop 150px):

Badge pill: liquid-glass rounded-full containing a white bg-white text-black rounded-full "New" tag + "Introducing AI‑powered web design." text
Heading: Use a BlurText animation component — text: "The Website Your Brand Deserves" — text-6xl md:text-7xl lg:text-[5.5rem] font-heading italic text-foreground leading-[0.8] tracking-[-4px] — animates word-by-word from bottom with blur-to-clear effect (delay 100ms per word)
Subtext (motion.p): "Stunning design. Blazing performance. Built by AI, refined by experts. This is web design, wildly reimagined." — fades in with blur at 0.8s delay
CTA buttons (motion.div, 1.1s delay): liquid-glass-strong rounded-full "Get Started" + ArrowUpRight icon, and a text-only "Watch the Film" + Play icon
Partners bar at bottom (mt-auto pb-8 pt-16)
BlurText component: Uses motion/react (framer-motion). Splits text by words, each word animates via IntersectionObserver with filter: blur(10px) → blur(5px) → blur(0px), opacity: 0 → 0.5 → 1, y: 50 → -5 → 0. Step duration 0.35s.

SECTION 3 — PARTNERS BAR
Centered column. Top: liquid-glass rounded-full badge "Trusted by the teams behind". Below: horizontal row of partner names ("Stripe", "Vercel", "Linear", "Notion", "Figma") rendered as text-2xl md:text-3xl font-heading italic text-white, gap-12.

SECTION 4 — START SECTION ("How It Works")
Full-width section, min-height: 700px, py-32 px-6 md:px-16 lg:px-24.

Background HLS video:

src: https://stream.mux.com/9JXDljEVWYwWu01PUkAemafDugK89o01BR6zqJ3aS9u00A.m3u8
Use hls.js library. Absolute, full cover, z-0. Top + bottom fade gradients (200px each, black ↔ transparent).

Content (z-10, centered, min-height 500px):

Badge: liquid-glass rounded-full — "How It Works"
Heading: "You dream it. We ship it." — text-4xl md:text-5xl lg:text-6xl font-heading italic
Subtext: "Share your vision. Our AI handles the rest—wireframes, design, code, launch. All in days, not quarters."
Button: liquid-glass-strong rounded-full "Get Started" + ArrowUpRight
SECTION 5 — FEATURES CHESS (alternating rows)
py-24 px-6 md:px-16 lg:px-24. Header: badge "Capabilities", heading "Pro features. Zero complexity."

Row 1 (text left, image right):

H3: "Designed to convert. Built to perform."
P: "Every pixel is intentional. Our AI studies what works across thousands of top sites—then builds yours to outperform them all."
Button: liquid-glass-strong rounded-full "Learn more"
Image: GIF in liquid-glass rounded-2xl overflow-hidden container
Row 2 (image left, text right — using lg:flex-row-reverse):

H3: "It gets smarter. Automatically."
P: "Your site evolves on its own. AI monitors every click, scroll, and conversion—then optimizes in real time. No manual updates. Ever."
Button: "See how it works"
SECTION 6 — FEATURES GRID (4 columns)
Badge "Why Us", heading "The difference is everything."

4 cards in grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6. Each card: liquid-glass rounded-2xl p-6. Contains:

Icon in liquid-glass-strong rounded-full w-10 h-10 circle
Title: text-lg font-heading italic text-white
Description: text-white/60 font-body font-light text-sm
Cards:

⚡ Zap — "Days, Not Months" — "Concept to launch at a pace that redefines fast."
🎨 Palette — "Obsessively Crafted" — "Every detail considered. Every element refined."
📊 BarChart3 — "Built to Convert" — "Layouts informed by data. Decisions backed by performance."
🛡️ Shield — "Secure by Default" — "Enterprise-grade protection comes standard."
SECTION 7 — STATS
Background HLS video:

src: https://stream.mux.com/NcU3HlHeF7CUL86azTTzpy3Tlb00d6iF3BmCdFslMJYM.m3u8
Desaturated: style={{ filter: 'saturate(0)' }}. Top + bottom black fades (200px).

Content (z-10): liquid-glass rounded-3xl p-12 md:p-16, grid grid-cols-2 lg:grid-cols-4 gap-8 text-center:

"200+" — "Sites launched"
"98%" — "Client satisfaction"
"3.2x" — "More conversions"
"5 days" — "Average delivery"
Values: text-4xl md:text-5xl lg:text-6xl font-heading italic text-white Labels: text-white/60 font-body font-light text-sm

SECTION 8 — TESTIMONIALS
Badge "What They Say", heading "Don't take our word for it."

3-column grid. Each: liquid-glass rounded-2xl p-8. Quote in text-white/80 font-body font-light text-sm italic. Name: text-white font-body font-medium text-sm. Role: text-white/50 font-body font-light text-xs.

Testimonials:

Sarah Chen, CEO Luminary — "A complete rebuild in five days..."
Marcus Webb, Head of Growth Arcline — "Conversions up 4x..."
Elena Voss, Brand Director Helix — "They didn't just design our site..."
SECTION 9 — CTA FOOTER
Background HLS video:

src: https://stream.mux.com/8wrHPCX2dC3msyYU9ObwqNdm00u3ViXvOSHUMRYSEe5Q.m3u8
Top + bottom black fades (200px).

Content (z-10, centered):

Heading: "Your next website starts here." — text-5xl md:text-6xl lg:text-7xl
Subtext: "Book a free strategy call. See what AI‑powered design can do."
Two buttons: liquid-glass-strong "Book a Call" + solid bg-white text-black "View Pricing"
Footer: mt-32 pt-8 border-t border-white/10, copyright "© 2026 Studio" + links (Privacy, Terms, Contact) in text-white/40 text-xs
DEPENDENCIES
hls.js, motion (framer-motion), lucide-react, tailwindcss-animate
KEY PATTERNS
All section badges use: liquid-glass rounded-full px-3.5 py-1 text-xs font-medium text-white font-body inline-block mb-4
All section headings use: text-4xl md:text-5xl lg:text-6xl font-heading italic text-white tracking-tight leading-[0.9]
All video sections use HLS via hls.js with Safari fallback (canPlayType)
All video fades: 200px height, linear-gradient(to bottom/top, black, transparent)
Outer page wrapper: bg-black overflow-visible```

## 17. Viktor Oddy — 3D creator portföy prompt'u (tam)

Kaynak: https://x.com/viktoroddy/status/2044391383394353285
```
Build a 3D Creator portfolio landing page for "Jack" using React, TypeScript, Tailwind CSS, Framer Motion, and Lucide React. The page has a dark theme (#0C0C0C background) with the font Kanit (Google Fonts, weights 300-900). The page title is "Jack -- 3D Creator".

GLOBAL STYLES
Background: #0C0C0C on html, body, #root, and the main wrapper
Font family: 'Kanit', sans-serif
Global reset: box-sizing border-box, margin 0, padding 0
CSS class .hero-heading: gradient text using background: linear-gradient(180deg, #646973 0%, #BBCCD7 100%) with -webkit-background-clip: text and -webkit-text-fill-color: transparent
Main wrapper has overflowX: 'clip'
SECTION ORDER
HeroSection
MarqueeSection
AboutSection
ServicesSection
ProjectsSection
1. HERO SECTION
Full viewport height (h-screen), flex column layout with overflowX: clip.

Navbar: Horizontal nav bar with 4 links -- "About", "Price", "Projects", "Contact" -- evenly spaced with justify-between. Text color #D7E2EA, font-medium, uppercase, tracking-wider. Sizes: text-sm md:text-lg lg:text-[1.4rem]. Padding: px-6 md:px-10 pt-6 md:pt-8. Hover: opacity 70% with 200ms transition.

Hero Heading: Massive h1 with text "Hi, i'm jack" (lowercase "i", curly apostrophe via &apos;). Uses the .hero-heading gradient text class. Font-black, uppercase, tracking-tight, leading-none, whitespace-nowrap, w-full. Font sizes: text-[14vw] sm:text-[15vw] md:text-[16vw] lg:text-[17.5vw]. Margin top: mt-6 sm:mt-4 md:-mt-5. Wrapped in overflow-hidden container.

Bottom bar: Flexbox justify-between items-end with pb-7 sm:pb-8 md:pb-10:

Left: paragraph text "a 3d creator driven by crafting striking and unforgettable projects", color #D7E2EA, font-light, uppercase, tracking-wide, leading-snug. Font size: clamp(0.75rem, 1.4vw, 1.5rem). Max-width: max-w-[160px] sm:max-w-[220px] md:max-w-[260px].
Right: ContactButton component (see below)
Hero Portrait: Centered absolutely. Uses a Magnet component (mouse-following magnetic effect) wrapping an image. Image URL: https://shrug-person-78902957.figma.site/_components/v2/d24c01ad3a56fc65e942a1f501eb73db42d7cf9a/Rectangle_40443.81459862.png. Magnet settings: padding 150, strength 3, activeTransition "transform 0.3s ease-out", inactiveTransition "transform 0.6s ease-in-out". Positioning: absolute left-1/2 -translate-x-1/2 z-10. Width: w-[280px] sm:w-[360px] md:w-[440px] lg:w-[520px]. On mobile: top-1/2 -translate-y-1/2. On sm+: sm:top-auto sm:translate-y-0 sm:bottom-0.

FadeIn animations: Navbar fades in with delay 0, y -20. Heading: delay 0.15, y 40. Left text: delay 0.35, y 20. Contact button: delay 0.5, y 20. Portrait: delay 0.6, y 30.

2. MARQUEE SECTION
Two rows of images that scroll horizontally based on page scroll position. Background #0C0C0C. Padding: pt-24 sm:pt-32 md:pt-40 pb-10.

21 GIF images from http://motionsites.ai (exact URLs):
https://motionsites.ai/assets/hero-space-voyage-preview-eECLH3Yc.gif
https://motionsites.ai/assets/hero-codenest-preview-Cgppc2qV.gif
https://motionsites.ai/assets/hero-vex-ventures-preview-BczMFIiw.gif
https://motionsites.ai/assets/hero-stellar-ai-v2-preview-DjvxjG3C.gif
https://motionsites.ai/assets/hero-asme-preview-B_nGDnTP.gif
https://motionsites.ai/assets/hero-transform-data-preview-Cx5OU29N.gif
https://motionsites.ai/assets/hero-vitara-preview-Cjz2QYyU.gif
https://motionsites.ai/assets/hero-terra-preview-BFjrCr7T.gif
https://motionsites.ai/assets/hero-skyelite-preview-DHaZIgUv.gif
https://motionsites.ai/assets/hero-aethera-preview-DknSlcTa.gif
https://motionsites.ai/assets/hero-designpro-preview-D8c5_een.gif
https://motionsites.ai/assets/hero-stellar-ai-preview-D3HL6bw1.gif
https://motionsites.ai/assets/hero-xportfolio-preview-D4A8maiC.gif
https://motionsites.ai/assets/hero-orbit-web3-preview-BXt4OttD.gif
https://motionsites.ai/assets/hero-nexora-preview-cx5HmUgo.gif
https://motionsites.ai/assets/hero-evr-ventures-preview-DZxeVFEX.gif
https://motionsites.ai/assets/hero-planet-orbit-preview-DWAP8Z1P.gif
https://motionsites.ai/assets/hero-new-era-preview-CocuDUm9.gif
https://motionsites.ai/assets/hero-wealth-preview-B70idl_u.gif
https://motionsites.ai/assets/hero-luminex-preview-CxOP7ce6.gif
https://motionsites.ai/assets/hero-celestia-preview-0yO3jXO8.gif
Row 1: first 11 images, tripled for seamless scrolling. Moves RIGHT on scroll (translateX(offset - 200)).
Row 2: remaining 10 images, tripled. Moves LEFT on scroll (translateX(-(offset - 200))).
Scroll offset calculated as: (window.scrollY - sectionTop + window.innerHeight) * 0.3
Each image tile: 420px x 270px, rounded-2xl, object-cover, lazy loaded.
Gap between tiles: gap-3. Gap between rows: gap-3.
Uses willChange: 'transform' for performance. Scroll listener is passive.
3. ABOUT SECTION
Full-height centered section with min-h-screen, padding px-5 sm:px-8 md:px-10 py-20.

Four decorative 3D images positioned absolutely in corners:

Top-left: Moon icon -- https://shrug-person-78902957.figma.site/_components/v2/ebb2b8f25d8e24d5f0a5ca8af4c950de81aa2fd7/moon_icon.11395d36.png -- w-[120px] sm:w-[160px] md:w-[210px], positioned top-[4%] left-[1%] sm:left-[2%] md:left-[4%]. FadeIn: delay 0.1, x -80, y 0, duration 0.9.
Bottom-left: 3D object -- https://shrug-person-78902957.figma.site/_components/v2/ebb2b8f25d8e24d5f0a5ca8af4c950de81aa2fd7/p59_1.4659672e.png -- w-[100px] sm:w-[140px] md:w-[180px], positioned bottom-[8%] left-[3%] sm:left-[6%] md:left-[10%]. FadeIn: delay 0.25, x -80, y 0, duration 0.9.
Top-right: Lego icon -- https://shrug-person-78902957.figma.site/_components/v2/ebb2b8f25d8e24d5f0a5ca8af4c950de81aa2fd7/lego_icon-1.703bb594.png -- w-[120px] sm:w-[160px] md:w-[210px], positioned top-[4%] right-[1%] sm:right-[2%] md:right-[4%]. FadeIn: delay 0.15, x 80, y 0, duration 0.9.
Bottom-right: 3D group -- https://shrug-person-78902957.figma.site/_components/v2/ebb2b8f25d8e24d5f0a5ca8af4c950de81aa2fd7/Group_134-1.2e04f3ce.png -- w-[130px] sm:w-[170px] md:w-[220px], positioned bottom-[8%] right-[3%] sm:right-[6%] md:right-[10%]. FadeIn: delay 0.3, x 80, y 0, duration 0.9.
Heading: "About me" using .hero-heading gradient text, font-black, uppercase, leading-none, tracking-tight, centered. Font size: clamp(3rem, 12vw, 160px). FadeIn: delay 0, y 40.

Animated paragraph: Uses a character-by-character scroll-driven opacity animation. Text: "With more than five years of experience in design, i focus on branding, web design, and user experience, i truly enjoy working with businesses that aim to stand out and present their best image. Let's build something incredible together!" -- color #D7E2EA, font-medium, centered, leading-relaxed, max-w-[560px], font size clamp(1rem, 2vw, 1.35rem). Each character animates from opacity 0.2 to 1 based on scroll progress, with scroll offset ['start 0.8', 'end 0.2'].

Contact button below the text block. Gap between heading/text: gap-10 sm:gap-14 md:gap-16. Gap between text block and button: gap-16 sm:gap-20 md:gap-24.

4. SERVICES SECTION
White background (#FFFFFF), with rounded-t-[40px] sm:rounded-t-[50px] md:rounded-t-[60px] top corners. Padding: px-5 sm:px-8 md:px-10 py-20 sm:py-24 md:py-32.

Heading: "Services" in #0C0C0C, font-black, uppercase, centered, font size clamp(3rem, 12vw, 160px). Margin bottom: mb-16 sm:mb-20 md:mb-28.

5 service items in a vertical list, max-w-5xl, centered:

01 - 3D Modeling: "Creation of detailed objects, characters, or environments tailored to specific client needs, ideal for games, products, and visualizations."
02 - Rendering: "High-quality, photorealistic renders that showcase designs with custom lighting, textures, and materials to bring concepts to life."
03 - Motion Design: "Dynamic animations and motion graphics that add energy and storytelling to brands, products, and digital experiences."
04 - Branding: "Crafting cohesive visual identities -- from logos to full brand systems -- that communicate a clear and memorable presence."
05 - Web Design: "Designing clean, modern, and conversion-focused websites with attention to layout, typography, and user experience."
Each item: horizontal layout with number (font-black, font size clamp(3rem, 10vw, 140px), color #0C0C0C) on the left and name + description stacked vertically on the right. Name: font-medium, uppercase, font size clamp(1rem, 2.2vw, 2.1rem). Description: font-light, leading-relaxed, max-w-2xl, font size clamp(0.85rem, 1.6vw, 1.25rem), opacity 0.6. Items separated by 1px borders (rgba(12, 12, 12, 0.15)). Padding: py-8 sm:py-10 md:py-12. Staggered FadeIn: each item delays by i * 0.1.

5. PROJECTS SECTION
Dark background (#0C0C0C), rounded top corners rounded-t-[40px] sm:rounded-t-[50px] md:rounded-t-[60px], pulled up with -mt-10 sm:-mt-12 md:-mt-14, z-10.

Heading: "Project" (singular) using .hero-heading gradient, same styling as other headings.

3 sticky-stacking project cards that scale down as you scroll past them (card stacking effect using Framer Motion useScroll and useTransform). Each card is sticky top-24 md:top-32 inside an h-[85vh] container.

Scale calculation: targetScale = 1 - (totalCards - 1 - index) * 0.03. Each card offset by top: ${index * 28}px.

Each card has: rounded-[40px] sm:rounded-[50px] md:rounded-[60px], border-2 border-[#D7E2EA], background #0C0C0C, padding p-4 sm:p-6 md:p-8.

Card layout:

Top row: Number (huge, same style as services), category label, project name, and a "Live Project" ghost button (rounded-full, border-2 #D7E2EA, uppercase, tracking-widest).
Bottom row: Two-column image grid -- left column (40% width) has 2 stacked images, right column (60%) has 1 tall image. All images have heavy border radius rounded-[40px] sm:rounded-[50px] md:rounded-[60px]. Left top image height: clamp(130px, 16vw, 230px). Left bottom image height: clamp(160px, 22vw, 340px).
Project data with CloudFront image URLs:

Project 01 - "Nextlevel Studio" (Client):

Col1 image 1: https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2Fhf_20260412_055344_5eff02e0-87a5-41ce-b64f-eb08da8f33db.png&w=1280&q=85
Col1 image 2: https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2Fhf_20260412_055431_11d841fd-8b41-46a5-82e4-b04f2407a7d8.png&w=1280&q=85
Col2 image: https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2Fhf_20260412_055451_e317bf2d-28d4-48cc-86b0-6f72f25b6327.png&w=1280&q=85
Project 02 - "Aura Brand Identity" (Personal):

Col1 image 1: https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2Fhf_20260412_055654_911201c5-36d9-4bc6-bac7-331adfce159f.png&w=1280&q=85
Col1 image 2: https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2Fhf_20260412_055723_5ceda0b8-d9c2-4665-b2e3-83ba19ba76d1.png&w=1280&q=85
Col2 image: https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2Fhf_20260412_055753_adc5dcbd-a8e6-49c0-b43a-9b030d835cea.png&w=1280&q=85
Project 03 - "Solaris Digital" (Client):

Col1 image 1: https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2Fhf_20260412_055759_963cfb0b-4bd1-4b0f-9d0a-09bd6cf95b2f.png&w=1280&q=85
Col1 image 2: https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2Fhf_20260412_060108_438f781a-9846-4dcc-89ab-c4e6cb830f5b.png&w=1280&q=85
Col2 image: https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2Fhf_20260412_055818_9d062121-ad7e-46b9-999a-1a6a692ef1ee.png&w=1280&q=85
REUSABLE COMPONENTS
ContactButton: Rounded-full pill button with gradient background linear-gradient(123deg, #18011F 7%, #B600A8 37%, #7621B0 72%, #BE4C00 100%), inner box-shadow 0px 4px 4px rgba(181, 1, 167, 0.25), 4px 4px 12px #7721B1 inset, white 2px outline with -3px offset. Text: white, font-medium, uppercase, tracking-widest. Sizes: px-8 py-3 sm:px-10 sm:py-3.5 md:px-12 md:py-4, text text-xs sm:text-sm md:text-base. Label: "Contact Me".

LiveProjectButton: Ghost/outline pill button. Rounded-full, border-2 border-[#D7E2EA], text color #D7E2EA, font-medium, uppercase, tracking-widest. Sizes: px-8 py-3 sm:px-10 sm:py-3.5, text text-sm sm:text-base. Hover: bg-[#D7E2EA]/10. Label: "Live Project".

FadeIn: Framer Motion wrapper using whileInView with viewport={{ once: true, margin: "50px", amount: 0 }}. Accepts delay, duration (default 0.7), x (default 0), y (default 30). Easing: [0.25, 0.1, 0.25, 1]. Uses motion.create() for dynamic element types.

Magnet: Mouse-following magnetic hover effect. Tracks mouse position relative to element center, applies translate3d transform divided by strength factor. Activates when cursor is within padding distance of element edge. Smooth transition in (0.3s ease-out) and out (0.6s ease-in-out). Uses willChange: 'transform'.

AnimatedText: Character-by-character scroll-reveal text animation. Each character goes from opacity 0.2 to 1 based on its position in the text relative to scroll progress. Uses Framer Motion useScroll targeting the paragraph element with offset ['start 0.8', 'end 0.2']. Each character uses invisible placeholder + absolute positioned animated span.

KEY DEPENDENCIES
react, react-dom (^18.3.1)
framer-motion (^12.38.0)
lucide-react (^0.344.0)
tailwindcss (^3.4.1)
vite, typescript
RESPONSIVE BREAKPOINTS
All sections use Tailwind's default breakpoints (sm: 640px, md: 768px, lg: 1024px) with mobile-first approach. Heavy use of clamp() for fluid typography. The entire design scales gracefully from mobile to ultra-wide screens.```

## Bizim zincirde nereye girer

- #1 (spec) + #7 (PRD) → müşteri brief'inden spec/PRD üreten ilk kapı.
- #2 (DESIGN.md) + #3 (5 blok + 3 cila) + #14 → web hattının tasarım ve build adımları.
- #2b (20 kontrol) + #9 (onay döngüsü) → kalite kapıları.
- #16/#17 → kendi vitrin sitemiz için başlangıç prompt'u (ajans landing page).
- #6 → teslim sonrası app büyütme hizmeti (retainer).
- #11 → sosyal medya içerik hattı.
