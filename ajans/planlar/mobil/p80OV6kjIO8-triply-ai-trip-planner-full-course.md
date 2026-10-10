# The Ultimate Claude Code Tutorial for Mobile Apps - FULL COURSE — Codesistency — 213 dk — https://www.youtube.com/watch?v=p80OV6kjIO8

## Tek paragraf: ne yapıyor, sonuç ne
Eğitmen (BulkyAI'yi 14 günde App Store'a çıkaran kişi) "Triply" adlı bir AI gezi planlayıcıyı sıfırdan, "vibe coding değil, yapılandırılmış iş akışı" diyerek kuruyor: Plan Mode + interview prompt'u ile tüm proje `PLAN.md`'ye yazılıyor → GPT Image ile tüm ekranlar tek görselde + design system görseli → her ekran upscale edilip `design/` klasörüne konuyor → her ekran için "simülatörden screenshot al, tasarımla karşılaştır, aynı olana kadar döngüde kal" prompt'u → manuel test → ayrı branch + PR + CodeRabbit AI review → düzeltmeleri Claude'a yapıştır → merge → `PLAN.md`'de işaretle → sıradaki özellik. Stack: Expo SDK 56 + Expo Router API routes (ayrı backend yok) + NativeWind v4 + Expo Native Tabs (iOS liquid glass) + Clerk (Google + Apple) → webhook → Inngest arka plan task'i → Neon Postgres (Drizzle) kullanıcı senkronu; OpenAI (mini model) ile itinerary üretimi Inngest'te, UI DB'yi poll ediyor; Unsplash şehir fotoğrafı; ImageKit ile kullanıcı kapak görseli optimizasyonu; Sentry (logs, tracing, session replay, AI agent monitoring); Inngest experiments ile %90/%10 model A/B; profil ekranında "Delete account" (Clerk + tüm DB verisi); `legal/` klasöründe HTML/CSS landing + Privacy + Terms + Support sayfaları, shots.so mockup'ları, Cloudflare Workers/Wrangler ile ücretsiz yayın. Sonuç: simülatörde uçtan uca çalışan, mağaza kurallarına (Delete account, Apple Sign-In, legal sayfalar, izin metinleri) uyumlu bir MVP + canlı landing/legal sitesi; kaynak kod ve tüm prompt'lar ücretsiz. "Refine trip with AI" ekranı izleyiciye ödev bırakılıyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
**Açıklamadaki linkler (aynen):**
- Kaynak kod: https://dub.sh/code6 · Diyagramlar (iş akışı, webhook, Sentry, experiments): https://dub.sh/diagrams8
- Plan Mode Prompt: https://dub.sh/plan-mode · Design Prompts (recipe/dating/calorie tracker UI prompt'ları + design system prompt'u): https://dub.sh/design-prompts · Extra Prompts (verify loop, webhook/Inngest görevi): https://dub.sh/extra-prompts · Legal Pages Prompts (ToS + Privacy): https://dub.sh/legal-prompts
- Sentry https://dub.sh/sentry4 · Inngest https://dub.sh/inngest1 · CodeRabbit https://dub.sh/yt-coderabbit · Clerk https://go.clerk.com/m9uen7A · Neon https://dub.sh/neon4 · ImageKit https://tinyurl.com/55pbb66z · Skool: https://dub.sh/codesistency

**Araçlar / servisler:**
- Claude Code VS Code eklentisi (Opus 4.8 varsayılan), modlar: Plan Mode (planlama), "edit automatically" (üretim), bypass mode; sesli prompt; birden fazla Claude örneğini paralel çalıştırma (CodeRabbit düzeltmeleri için) [00:17:42, 02:36:30]
- `npx create-expo-app@latest .` (SDK 56) + `npm run reset-project` (N: sil) [00:14:25]
- NativeWind v4 (doküman tamamı prompt'a yapıştırıldı) [01:04:33]; Expo Native Tabs (doküman yapıştırıldı) [01:59:39]; `expo-dev-client` + `npx expo run:ios` (development build; "gerçek uygulama için Expo Go yetmez") [01:00:09]; `expo-image-picker` + `app.json` izin metni [02:27:11]
- **Clerk AI Skills** sayfası "copy as markdown" → Claude'a "install the skills needed for this project" (17 gereksiz skill'i kendi atladı; `.claude/skills/` altına iner) [00:57:32]
- Clerk webhooks (user.created/updated/deleted) → `/api/webhooks/clerk` Expo API Route → **Inngest** dev server (`npm run inngest`), functions: sync/update/delete user [01:41:39–01:50:29]; **Inngest experiments** (`group.experiment`, %90 mini / %10 büyük model) [03:07:22]
- ngrok ücretsiz statik domain (webhook'u localhost'a köprüler) [01:37:33]; **Context7 MCP** ("set up Context7 by Upstash for every project", prompt sonuna "use context7") [01:38:18]
- Neon Postgres + Drizzle ORM; OpenAI (en ucuz "mini" model, ~$5 yeter; alternatif Gemini ücretsiz) [00:25:17]; Unsplash API (şehir kapak görseli + attribution) [02:07:01]; ImageKit (kapak upload + optimize, `triply/covers/`) [02:08:40]
- Sentry: React Native projesi, "copy instructions for LLM" → `Sentry.init`; organization auth token `.env`'e; Sentry logs (`logger.info/warn/error/fatal/debug/trace`), tracing (`Sentry.startSpan`), `mobileReplayIntegration` (maskAll* false dev'de), **AI agent monitoring** (Explorer → LLM calls: token, maliyet, gecikme, input/output) [01:05:24, 02:46:30–03:00:28]
- CodeRabbit (GitHub App + VS Code eklentisi; "prompt for AI agents" kopyala → Claude) [01:27:20]
- Tasarım: GPT Image (tüm ekranlar 16:9 tek görsel; "grid of nine variations"; "upscale the sixth one"; arka plan görseli "no text, no logos"; logo grid'i "transparent background") [00:49:49, 01:56:25]; shots.so (iPhone 17 çerçeve, transparent, export) [03:16:39]; FFmpeg ile görsel sıkıştırma ("use FFmpeg and compress this image") [03:21:47]
- Legal: `legal/` klasörü, sade HTML+CSS, Cloudflare Workers + Wrangler ("deploy the legal folder to Cloudflare, I'm already authenticated with Wrangler") [03:27:12]
- Git: her özellik ayrı branch → PR → CodeRabbit → merge → master'a sync; AI commit mesajı (VS Code Source Control) [00:45:36]

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)

### Modül 1 — Planlama ve araç kurulumu [00:06:20–00:49:38]
1. Boş klasör (Triply) VS Code'da; Node kurulu; `npx create-expo-app@latest .` → `npm run reset-project` (N) → yalnızca `src/app` kalır.
2. Claude Code eklentisini aç → **Plan Mode** → dub.sh/plan-mode prompt'unu yapıştır (Prompt A) + sesli olarak projeyi ve **stack'i kendin belirle** ("stack'i AI değil ben seçerim") [00:20:57].
3. Interview soruları ve verilen cevaplar [00:24:28–00:37:38]: AI model → OpenAI (anahtar var); backend → Expo Router API routes (V1 için önerilen); async üretim bitti sinyali → DB polling; trip içeriği → gün-gün itinerary + bütçe kırılımı + yerler + otel önerisi; yer verisi → "LLM text only, no photos" V1; harita → "Apple Maps, no API key, skip Google Maps for V1" (bu yüzden V1 iOS-only); ORM → Drizzle; limit → ücretsiz, paywall yok; ImageKit rolü → profil fotoğrafı + özel trip kapağı; form alanları → tarih, ilgi alanları (food/history/nightlife/nature), bütçe tier'ları, yolcu sayısı, tempo; home ekranı → "I will provide the UI design later"; Clerk↔Neon → webhooks; kapak → Unsplash; üretim hatası → Inngest auto-retry + graceful fail; trip aksiyonları → view/delete + "AI chatbot button for modifications"; chat → senkron, geçmiş kalıcı, sil butonu; Claude'un takip önerisi → **günde 20 üretim rate limit**; model → mini her yerde.
4. Planı **5-10 dk oku**; R1 riski (EAS Hosting'de uzun Inngest endpoint'i) için: "use Inngest dev server, don't deploy to EAS hosting at the start, keep the rest" → plan güncellendi → "yes, auto accept" → to-do üretimini durdur [00:38:30].
5. Prompt B ile `PLAN.md` (fazlar + to-do, tamamlananlar işaretlenecek, "başka hiçbir şey yapma") [00:40:31].
6. `.env` oluştur, `.gitignore`'a ekle; kaynak koddaki `.env.example`'ı kopyala (Clerk ×3, DATABASE_URL, AI key, Unsplash ×2, Inngest dev, ImageKit ×3, Sentry token) [00:41:29].
7. Sentry, Neon, Clerk, Inngest, ImageKit, CodeRabbit hesaplarını aç. İlk commit doğrudan master'a; GitHub'da repo → "push an existing repository" komutları. Sonraki her özellik: branch → PR → review → merge [00:44:48].

### Modül 2 — UI tasarımı ve auth ekranı [00:49:38–01:37:28]
8. dub.sh/design-prompts'tan bir örnek al → Claude'a: `Generate me a prompt for UI design image generation for my app based on the @PLAN.md file` (Prompt C) → çıkan prompt'u GPT'ye, "aspect ratio 16:9" → tüm ekranlar tek görselde → `design/app-ui-design.png`.
9. Design system görseli: `Generate an image of a design system for this application that includes colors, font family, components, etc.` → `design/design-system.png` [00:53:06].
10. Auth ekranını screenshot → GPT "upscale this screen" → `design/auth-ui-design.png`; arka plan görseli: "Generate me this background image so I can use it, no text, no logos" → `design/auth-screen-background.png`.
11. `npx expo start` → `i` (iOS simülatör, Expo Go kurulur). Clerk skills sayfası "copy as markdown" → Claude: `install the skills that are needed for this project` (izin: allow for all projects) [00:58:27].
12. Claude'a `expo-dev-client` kurdur → `npx expo run:ios` (ilk build uzun). NativeWind v4 dokümanının tamamını yapıştır + Prompt D [01:04:33]. Paralelde Sentry projesi (React Native) → "copy instructions for LLM" → Prompt E; auth token → Settings → Organization tokens → `.env` (`SENTRY_AUTH_TOKEN`) [01:05:24–01:09:45].
13. Build hatası → Prompt F ("what is wrong? let me know and also fix").
14. **AGENTS.md**: Claude'a "depending on my project what should I have in agents.md?" → tech stack (Expo, Router, Sentry, Neon, Drizzle, ImageKit, Clerk, Inngest), konvansiyonlar (className, inline style yok), **"always use native tabs, never JS tabs"**, **"never run the application yourself, I run it in a separate terminal"**; `CLAUDE.md` tek satır referans [01:10:45–01:17:03].
15. Verify-loop prompt'u (dub.sh/extra-prompts; Prompt G) + `@auth-ui-design.png` + `@auth-screen-background.png` → Claude 3-4 turda kendi screenshot'ını alıp düzeltti (buton arka planları eksik → eklendi; başlık 3 satır → 2 satır) [01:17:56–01:19:42]. Takip: e-posta butonunu ve alt metni kaldır.
16. Clerk dashboard → uygulama (Google + Apple, e-posta kapalı) → Expo quickstart'tan `EXPO_PUBLIC_CLERK_PUBLISHABLE_KEY` → `.env` → Prompt H (skill'lere uy) → Google ile giriş test, Clerk Users'ta kullanıcı görünür [01:20:33–01:24:03].
17. Branch `authentication` → commit (AI mesaj) → publish → PR → CodeRabbit kurulumu (repo ekle) → yorumlar: skill dosyalarını atla; Sentry `tracesSampleRate 1.0` prod'da olmasın → "prompt for AI agents" → Claude koşullu yaptı → aynı branch'e commit → sync → merge → master'a geç, pull [01:25:35–01:33:09].
18. Prompt I ile `PLAN.md` güncelle (auth tamam, env'ler var say, sync'i şimdi yapma).

### Modül 3 — Webhook + Inngest ile kullanıcı senkronu [01:37:28–01:56:01]
19. ngrok hesabı (ücretsiz statik domain), Context7 MCP kurulu. dub.sh/extra-prompts'taki görev prompt'u (Prompt J) + Neon → New project → Connect → `.env` DATABASE_URL (tırnaksız).
20. Clerk → Configure → Developers → Webhooks → Add endpoint: `https://<ngrok-domain>/api/webhooks/clerk`, events: user.created/updated/deleted → signing secret → `.env` `CLERK_WEBHOOK_SIGNING_SECRET`. Inngest dashboard: dev'de key gerekmez; prod için `INNGEST_SIGNING_KEY` + `INNGEST_EVENT_KEY` `.env.example`'da [01:41:39–01:44:22].
21. Test: `npm run inngest` (dev server, tarayıcıda functions); hata → `npx expo start --clear`; Clerk'ten kullanıcıyı sil, yeniden kayıt → Inngest run → Neon users tablosunda satır [01:45:11–01:47:53].
22. Prompt K ile update + delete event'leri; test: Clerk'ten sil → Inngest "user.deleted" run → DB'den silindi. Prompt L: "PLAN.md'de yapılmış ama işaretlenmemişleri işaretle" (6 madde çıktı). Branch `auth-db-sync` → PR (12k satır) → CodeRabbit: `.env` boş değer (minor), `isDev` hard-coded (major; tutorial için atlandı) → merge [01:51:19–01:55:39].

### Modül 4 — Native Tabs, Home, trip üretimi [01:56:01–02:40:31]
23. Görsel varyasyon tekniği: beğenilen ekranın screenshot'ı → GPT "Generate me a grid of nine images of different UI for this screen, improve it" → beğenileni "upscale the sixth one" → `design/`; loading ekranı için "generate this background image, make the background transparent"; "world.png"; logo: "grid of nine logos" → "upscale without text, transparent background"; AI buton ikonu aynı yöntem [01:56:25–02:01:09].
24. Native Tabs dokümanını yapıştır + Prompt M (4 tab, home UI önce görünüm, `@home-ui-design.png`, `@world.png`, loop) → 2 takip (Prompt N: world.png büyüt + sağa yasla; scroll sonunda tab bar için ekstra boşluk) → sağa yaslamayı Claude beceremedi → `index.tsx`'te değer elle 58 yapıldı [02:01:54–02:03:38].
25. Get Started (plan formu) ekranı: iki parça tasarım görseli (`design-1/2.png`) → "build the UI, functionality later" → "make the current date selected by default, previous dates disabled" [02:04:34].
26. Unsplash developers → app → access + secret key → `.env`. Prompt O ("implement generate a trip feature, see phase 3 in PLAN.md; if I need to do something tell me, else do everything") → Drizzle schema (trips, generation_usage, chat_messages…) Neon'da oluştu. ImageKit → Developer options → URL endpoint + public + private key → `.env` [02:06:17–02:09:27].
27. Test: "Couldn't start your trip, unauthorized" → screenshot + Prompt P → eksik `CLERK_SECRET_KEY` (Clerk Express quickstart'tan) → `.env` → **Expo'yu `--clear` ile yeniden başlat** (env değişince şart) [02:11:05–02:13:02]. İkinci hata: OpenAI key yok → platform.openai.com → key → `.env` → restart. Inngest dashboard'da failed/completed run'lar ve süreler görülür [02:16:53].
28. Trip detail UI tasarıma uymuyor → `@trip-detail-design.png` + loop prompt'u → "almost identical, map, curve" 5 dakikada; Unsplash attribution otomatik eklendi [02:18:31–02:20:09]. Itinerary tek gün geliyor → Prompt Q (duration kadar gün) [02:21:00].
29. Üç nokta menüsü → Prompt R (dropdown, native confirmation, DB'den sil) → "replace the three dots icon with a trash icon". Kamera butonu + galeri + ImageKit (Prompt S, sonuna "use context7") → `expo-image-picker` native modül → `app.json` izin metni otomatik ("Allow Triply to access your photos so you can set a custom cover image") → ikon boyutu elle 22 → **rebuild `npx expo run:ios`** [02:22:58–02:29:42].
30. Test: Istanbul trip'i, Inngest run canlı, kapak değiştir → ImageKit `triply/covers/` → sil → DB'de yok. Loading ekranı: `@trip-loading-screen-design.png` + `@trip-loading-screen-demo.png` + loop; Claude'un bıraktığı "preview dev hook"u strip et [02:32:12–02:33:50].
31. Branch `create-trip` → PR → CodeRabbit major'ları (data integrity, security/privacy, functional correctness) → her birini **ayrı Claude örneğinde paralel** düzelt → aynı branch'e commit → merge [02:35:43–02:40:16].

### Modül 5 — Sentry derinlik, Assistant, Trips [02:40:31–03:06:33]
32. Sentry gerekçesi: kullanıcı hata görünce e-posta atmaz, siler ve 1 yıldız verir; logs (yapılandırılmış, kalıcı, aranabilir), replay (kullanıcının gördüğü video), tracing, alerts (e-posta/Slack/Discord) [02:41:02–02:45:43].
33. Sentry logs dokümanını yapıştır + Prompt T → API hatalarında warn/error, auth'ta info/error (6 metod: debug, error, fatal, info, trace, warn; `fmt`). Tracing dokümanı + Prompt U → `Sentry.startSpan` (generate-trip). Replay: `mobileReplayIntegration({maskAllImages:false, maskAllText:false, maskAllVectors:false})` dev için [02:46:30–02:52:37].
34. Assistant ekranı: `@assistant-screen-ui-design.png` + loop (Prompt V) → tek seferde; "extra unnecessary spacing, delete" takibi; tasarımdaki örnek sohbetleri kopyalamıştı, kendi düzeltti. Not: loop talimatını AGENTS.md "conventions"a yazıp her seferinde tekrar etme fikri [02:53:26–02:55:14].
35. Streaming (Prompt W: spinner yerine chunk-by-chunk) → ReadableStream + `stream:true`; "reset the input as soon as we send the message" [02:56:12–02:57:06].
36. **Sentry AI agent monitoring** dokümanı + Prompt X (yalnızca assistant ekranı) → Explorer → LLM calls: maliyet, token, model, input/output, AI view; yavaş çağrıları buradan yakala [02:58:06–03:00:28].
37. Mesajlar DB'ye yazılmıyor → Prompt Y (kaydet + navbar'daki sil butonu onaylı tüm mesajları siler) → Claude ayrı `assistant_messages` tablosu açtı (chat_messages trip refine için) [03:01:16–03:02:58].
38. Trips ekranı: `@trips-screen-ui-design.png` + loop → kart → trip detail. Branch `assistant-trip-screens` → PR → merge [03:03:51–03:06:33].

### Modül 6 — Inngest experiments, Profil, Legal, yayın [03:06:33–03:32:30]
39. **Inngest experiments**: eski kod %90, yeni %10 (model A/B: mini vs büyük); düşük risk, Inngest zaten süre/hata/maliyet ölçüyor; iki sayıyı değiştirerek dial-up. Doküman markdown'ı + Prompt Z (ayrı Claude örneği) [03:07:22–03:12:29].
40. Paralelde profil: `@profile-screen-design-1/2.png` + loop (Prompt AA) → "make sure the delete account button works: confirmation → delete from Clerk and DB (webhook) → trips, chat messages, all user data" (Prompt AB) [03:13:21–03:15:01].
41. **Legal + landing**: shots.so → iPhone 17 çerçeve → beyaz/transparan → screenshot yükle → export → `legal/demo1.png, demo2.png, hero.png`; referans sitenin tam ekran görüntüsü `legal/ui-inspiration.png` → Prompt AC (clone et, landing + privacy sayfası, Cloudflare Workers'a deploy edilecek, yalnızca HTML+CSS) → logo.png'yi `legal/`e kopyala → FFmpeg ile sıkıştır (Prompt AD) → dub.sh/legal-prompts: ToS prompt'u, Privacy prompt'u (placeholder'lar: tüzel kişi, adres, minimum yaş) → Prompt AE (support sayfası + home'dan link) [03:16:39–03:26:18].
42. Cloudflare'a GitHub ile giriş → Prompt AF ("deploy the legal folder to Cloudflare, already authenticated with Wrangler") → canlı URL: landing, privacy, terms, support [03:27:12–03:29:01].
43. Ödev: "Refine trip with AI" ekranı (`refine-ai-ui-design.png` verildi). Branch `profile-legal-pages` → PR → merge → master sync. Kaynak kod ücretsiz [03:29:51–03:32:30].

## Kullandığı prompt'lar (varsa aynen)
Prompt A — Plan Mode interview (dub.sh/plan-mode; videoda okunan baş kısmı) [00:19:19]:
```
You are my senior technical co-founder and product architect. I'm about to build a new software project and I want us to be in complete alignment before writing any single line of code or the final plan is written.
<…prompt'un devamı dub.sh/plan-mode'da; AI'nın hiçbir özelliği tahmin etmeden soru sorması, cevaplar bitince spec + plan yazması…>

The project I want to build is an AI trip planner. For the tech stack I want to use Clerk for authentication, Neon for Postgres database, Inngest for background jobs, ImageKit for image optimizations and transformations, and Sentry for error tracking. For authentication I want Google and Apple; skip email for now. Users will use this app to generate trips powered by AI: after sign up they land on the home screen, press "generate a trip", give the location (e.g. Tokyo, Japan), how many days, number of travelers, budget; AI generates the trip, user sees a loading screen and is redirected to the trip detail screen.
```
Plan düzeltmesi [00:38:30]:
```
I like the entire plan, keep everything as it is. But for the open risks and unknowns, for R1: we want to use the Inngest dev server and not deploy anything to EAS hosting at the start. Keep this in mind and keep my plan as it is.
```
Prompt B — PLAN.md [00:40:31]:
```
We have this plan. I want you to put it under a PLAN.md file with every single phase, basically a list of to-dos. As we complete any of these features we will mark them as completed. For now, don't implement anything other than building the PLAN.md file.
```
Prompt C — UI prompt üretimi [00:51:30]:
```
Generate me a prompt for UI design image generation for my app based on the @PLAN.md file. <dub.sh/design-prompts'tan bir örnek prompt yapıştırılır: "depending on my plan, generate me a prompt similar to this">
```
Görsel üretim yardımcıları (GPT Image): `the aspect ratio should be 16:9` · `Generate an image of a design system for this application that includes colors, font family, components, etc.` · `Upscale this screen` · `Generate me this background image so I can use it. No text, no logos, nothing.` · `Generate me a grid of nine images of different UI for this screen. Improve it.` · `Upscale the sixth one` · `Make the background transparent so I can use it in my application` · `Generate a grid of nine logos for this application` · `Upscale this without the text, and it should be transparent background` · `Generate a grid of icons for this AI button at the bottom, it'll be like a chatbot`
Prompt D — NativeWind [01:04:33]:
```
This is the documentation for NativeWind. I want you to read the entire documentation from start until the end step by step and implement it correctly in my codebase, and then double check if it is set up correctly.
<NativeWind v4 "Get started" sayfasının tamamı>
```
Prompt E — Sentry kurulum [01:07:10]:
```
Set up Sentry in my project. This is the documentation I just pasted above.
<Sentry "copy instructions for LLM" çıktısı>
```
Prompt F — hata [01:08:05]:
```
I get this error when running npx expo run:ios. What is wrong? Let me know and also fix the issue.
<hata çıktısı>
```
AGENTS.md üretimi [01:15:06]:
```
On top of this I want you to update the tech stack part. We want to use Postgres database coming from Neon and Drizzle as our ORM. We are using ImageKit for image optimizations, Clerk for authentication, Inngest for background jobs and Sentry for error tracking and monitoring. Other than tech stack, I always want to use native tabs. I don't want to use the JavaScript tabs. This project should always have native tabs. With this in mind, go ahead and build the AGENTS.md.
```
```
Add this instruction to AGENTS.md: you should never ever run the application by yourself because I am already running it in a separate terminal.
```
Prompt G — verify loop (dub.sh/extra-prompts; videoda ek) [01:17:56]:
```
<loop prompt'u: implement the UI from the attached design, then take a screenshot from the simulator, compare it to the design, and keep iterating until it is identical>
Use @auth-screen-background.png as the demo image for the auth screen, just like in the design file @auth-ui-design.png.
```
```
From the authentication screen I want to remove the email option. There should be only two buttons, Google and Apple. Leave them as they are, but remove the email button, and also remove the text that says "let AI build the perfect trip for you".
```
Prompt H — Clerk auth [01:22:24]:
```
I have the environment variables for Clerk. I want you to implement Google and Apple authentication in my codebase. Make sure to read the skills so you don't make any mistakes. You should follow the skills since they are the official documentation.
```
Prompt I — PLAN.md güncelle [01:35:44]:
```
In my PLAN.md file, in phase zero and phase one, there is stuff that we have already implemented but we didn't update in the PLAN.md file. Update those parts. We already implemented authentication for Google and Apple under a single auth screen, which I'm happy with. For the environment variables, assume I have already set them up; all values are in the .env file. I will implement the user synchronization with Inngest in the incoming minutes, so don't update that part.
```
Prompt J — webhook + Inngest görevi (dub.sh/extra-prompts) [01:39:11]:
```
<"This is your task" ile başlayan prompt: Clerk webhook endpoint (/api/webhooks/clerk) → verify → Inngest "sync user" fonksiyonu → Neon'a upsert>
Imagine I have DATABASE_URL (currently undefined; I will add it). For Inngest we use development mode for now, no production keys. For the webhook URL I will use ngrok and provide it when needed.
```
Prompt K — update/delete [01:48:40]:
```
In my project I have already implemented Inngest by creating the user: when a user signs up with Clerk we take that user and save it into our database. In the exact same way, implement user deletion and the user.updated event. Then update the PLAN.md file and mark the things we've done as completed.
```
Prompt L [01:50:29]:
```
Check the PLAN.md file and see if we have anything implemented but forgot to mark as completed. If there are any, mark those as completed.
```
Prompt M — Native Tabs + Home [01:59:39]:
```
I have just pasted the documentation of native tabs below. Implement it in my application. I want four tabs: home, assistant, trips, and profile. Then let's get started with the home screen UI design: first build the UI only, no functionality, later we will make it work. For the UI design look at @home-ui-design.png, and you can use @world.png. Put yourself into that previous loop: take a screenshot from the simulator and compare it to the design until it is identical.
<Expo Native Tabs dokümanı>
```
Prompt N [02:02:42]:
```
Overall I like the home screen UI design, but I will ask for two improvements. Take the world.png, make it a bit larger and put it on the right-hand side; it should be sticky to the right-hand side of the blue box. And at the very end of the home screen when we scroll, there should be some extra spacing so that the native tabs are not overlapping with the popular destination images.
```
Get Started ekranı [02:04:34]: `Take these images (@design-1.png @design-2.png) as a reference and build the UI design. For now make only the UI work; later I'll ask you to implement the functionality.` · `Make the current date selected by default, and the previous dates should be disabled.`
Prompt O — üretim özelliği [02:07:53]:
```
Now I want you to implement the generate a trip feature. We already planned it under the PLAN.md file, take a look at phase three. If there is anything I need to do, like getting an environment variable from a dashboard, let me know; otherwise implement everything by yourself.
```
Prompt P — hata [02:11:05]:
```
When I try to press the "generate my trip" button, this is the first thing I see immediately (screenshot). What is going on? Are we missing any environment variables or any configuration? Take a look at my codebase and let me know.
```
Prompt Q [02:21:00]:
```
I like everything in the trip detail screen, but the only problem is that the duration is 5 days and within the itinerary I can only see day one. Make sure that from the AI model we get the same duration as the user input. Keep this in mind and implement it.
```
Prompt R — sil [02:22:58]:
```
Under the trip detail screen, at the top right corner, we have a button with three dots; when pressed nothing happens. Show a dropdown where the user can delete the trip. Before deleting, ask for a confirmation message (you can use a native component). If the user confirms, delete the trip from the database.
```
Prompt S — kapak görseli [02:24:37]:
```
Right next to the delete button add a camera button. When the user presses it, open the gallery so the user can select a custom image and update the trip cover image. Implement this, and for image optimizations make sure you're using ImageKit; we don't want large files. Use context7.
```
Loading ekranı [02:33:01]: `Make the loading screen look exactly like @trip-loading-screen-design.png; use @trip-loading-screen-demo.png as the background. Put yourself into the loop (screenshot → compare → iterate until identical).` · `Yes, strip out the preview dev hook.`
Prompt T — Sentry logs [02:46:30]:
```
In this project I want to set up Sentry, which we already did in the root layout file (@_layout.tsx). Now implement Sentry logs from scratch. Keep it simple, but add it into the functions where it makes sense; I want this feature to help me in production. Below I attach the official documentation from Sentry.
<Sentry logs dokümanı>
```
Prompt U — tracing [02:49:55]:
```
Perfect. Now implement Sentry tracing just like you did here. Make sure it is implemented in the correct places; think of it as a real-world production-ready application. I paste the docs from Sentry below.
<Sentry tracing dokümanı>
```
Prompt V — assistant [02:53:26]:
```
Build this exact same UI design for the assistant screen (@assistant-screen-ui-design.png). Put yourself into a loop where you take a screenshot from the simulator and compare it to the design until they're identical.
```
Prompt W — streaming [02:56:12]:
```
The assistant screen works as expected. The only add-on feature I want is streaming for chat replies: instead of a loading spinner, get the messages chunk by chunk, it should be streamed. Implement it.
```
Prompt X — AI agent monitoring [02:58:54]:
```
In this project I would like to implement Sentry AI agent monitoring, specifically in the assistant screen. I pasted the documentation from Sentry below; follow along and build it.
```
Prompt Y — mesaj kalıcılığı [03:01:16]:
```
Under the assistant screen everything works as expected. The only thing missing is storing the messages in the database. Implement it so messages are saved in the database, and when we press the delete button at the top of the navbar it should delete the messages from the database for the current user. Before deleting, ask for a confirmation message.
```
Prompt Z — experiments [03:11:35]:
```
Inngest has the experiments feature; I link the documentation below. Use the GPT-4o mini model 90% of the time and the GPT-4o model 10% of the time. Read the documentation and implement it correctly in our application without breaking any other features.
<Inngest experiments markdown>
```
Prompt AA/AB — profil [03:14:07]:
```
Build the profile screen for me. I attached two images (@profile-screen-design-1.png @profile-screen-design-2.png), the entire profile screen that I want you to copy. Put yourself into the loop: screenshot from the simulator, compare, until identical.
```
```
Make sure the delete account button is working. When the user presses it, first ask for a confirmation; if confirmed, delete the user from both Clerk and the database (database deletion will be handled by webhooks). Make sure to delete all user data: trips, chat messages, etc.
```
Prompt AC — landing [03:20:51]:
```
We have built our entire mobile application. Now it is time to get started with a landing page. First I want a beautiful UI design; I attach @ui-inspiration.png, clone it. Then build me a privacy policy screen (content comes later via prompts). For now build the entire landing screen for this legal page. We will deploy this legal folder to Cloudflare Workers for free, keep this in mind. Don't use any external technologies other than HTML and CSS. Keep it simple, yet working.
```
Prompt AD — sıkıştırma [03:21:47]: `Use FFmpeg and compress this image (@demo1.png). Install FFmpeg if I don't have it already.`
Prompt AE — support [03:26:18]: `Under the legal pages build me a pretty simple support screen and link it in the home screen; the support link must take us to that page.`
Prompt AF — deploy [03:27:12]: `Deploy the legal folder to Cloudflare. I have already authenticated using the Wrangler tool.`

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Tasarım görseli her görevin girdisi.** Tek bir "tüm ekranlar" görseli + ayrı **design system görseli** (renk, font, bileşenler) `design/` klasöründe; her ekran upscale edilip `@dosya` olarak prompt'a bağlanıyor; AI "kıyaslayacak referansı" olmadan ekran yapmıyor.
- **"Grid of nine" varyasyon tekniği**: beğenilmeyen ekranı GPT'ye verip 9 varyasyon üret, birini seç, upscale et. Trip detail, loading ekranı, dünya görseli, logo ve AI buton ikonu hep böyle çıktı; arka planlar "no text, no logos, transparent" ile ayrı asset olarak üretildi.
- **Build & verify loop**: Claude kendi ekran görüntüsünü alıp tasarımla kıyaslıyor, kendine geri bildirim yazıyor ("button backgrounds missing", "wraps to 3 lines → 2") ve 3-4 turda %80-90 benzerliğe geliyor; kalan %10 için elle `index.tsx`'te değer değiştirme (world.png offset 58, kamera ikonu 22).
- **Native his**: Expo Native Tabs (liquid glass) AGENTS.md kuralı; native confirmation; `app.json`'da anlamlı izin metni; native modül sonrası rebuild.
- **Fonksiyondan önce görünüm**: "first build the UI only, functionality later" ile ekran tasarıma oturuyor, sonra PLAN.md fazı bağlanıyor.
- **Küçük cilalar**: tab bar altı için ekstra scroll boşluğu, bugünün tarihi varsayılan seçili + geçmiş tarihler pasif, üç nokta → çöp ikonu, streaming cevap (spinner yerine), gönderimde input sıfırlama, Unsplash attribution, kapak görselinde eğri (curve) + harita.
- **Landing/legal**: shots.so iPhone 17 mockup'ları (transparan), referans sitenin tam sayfa ekran görüntüsü klonu, aynı logo + mavi tema, FFmpeg ile sıkıştırılmış görseller.
- Reanimated/özel animasyon kütüphanesi kullanılmıyor; "güzellik" referans görsel + loop + native bileşenlerden geliyor.

## Hatalar ve çözümleri
- `npx expo run:ios` ilk development build başarısız (Sentry yapılandırması eksik) → hata çıktısını "what is wrong + fix" ile Claude'a [01:03:41, 01:08:05].
- Inngest dev server URL'inde hata → `npx expo start --clear` ile yeniden başlat [01:46:04].
- Webhook geldi ama DB'ye yazılmadı senaryosu için test yöntemi: Clerk'ten kullanıcıyı sil, yeniden kayıt ol (user.created yeniden tetiklenir) [01:46:52].
- "Couldn't start your trip, unauthorized" → `CLERK_SECRET_KEY` eksik → ekle → **Expo'yu yeniden başlat** (env değişikliği restart ister) [02:11:05–02:13:02].
- Üretim Inngest'te fail → `OPENAI_API_KEY` eksik → ekle → restart; DB'de yarım kalan trip kaydını elle sil [02:14:04, 02:17:37].
- Itinerary tek gün döndü → prompt ile süre kadar gün zorunlu [02:21:00].
- world.png'yi sağa yaslamayı Claude yapamadı → koddan offset elle [02:03:38].
- `expo-image-picker` native modül → uygulama çöktü → `npx expo run:ios` rebuild [02:28:01].
- Loading ekranında Claude "preview dev hook" bıraktı → "strip it out" [02:33:50].
- Assistant ekranı tasarımdaki örnek sohbet metinlerini kopyaladı → kendi düzeltti; fazla boşluk → tek takip [02:55:14].
- CodeRabbit bulguları: Sentry `tracesSampleRate 1.0` prod'da (koşullu yapıldı), `isDev` hard-coded, `.env` boş değerler, data integrity / security / functional correctness major'ları → her biri ayrı Claude örneğinde "prompt for AI agents" ile [01:30:37, 02:35:43–02:39:16].
- Legal sayfada logo yoktu → logo.png `legal/`e kopyalandı [03:23:35].
- Trip detail'de "Refine AI" butonu demo hatası veriyor → ödev olarak bırakıldı [03:29:51].
- Mağaza reddi tuzakları (önleyici): Delete account yok, Privacy/Terms/Support sayfası yok, izin metni yok, Google var Apple yok [00:02:25, 00:03:10].

## Bizim fabrikaya alınacaklar (somut, maddeli)
1. **Plan Mode interview prompt'u** (dub.sh/plan-mode) fabrikanın kapı-1'i: stack'i biz veriyoruz, AI soruyor; cevaplar `PLAN.md` fazlarına; Claude'a "yapılanı işaretle" alışkanlığı; ilk 10 dk plan okuması zorunlu.
2. **AGENTS.md şablonu** (CLAUDE.md tek satır import): stack listesi, "className, no inline styles", "always Native Tabs", "never run the app yourself", "new native module → tell me to rebuild", "list/scroll screens → bottom padding for tabs", **verify-loop talimatı conventions'a gömülü** (her prompt'ta tekrar etmemek için).
3. **Tasarım asset pipeline'ı**: PLAN.md → UI prompt'u ürettir (dub.sh/design-prompts) → 16:9 tüm ekranlar → design system görseli → ekran başına upscale → `design/<ekran>.png`; beğenilmeyen ekran için "grid of nine" varyasyon; arka plan/logo/ikonlar "no text, transparent" ayrı asset; her ekran görevi `@design` referansı ile açılır.
4. **Verify-loop prompt'u** standart (dub.sh/extra-prompts): "screenshot from simulator, compare to design, iterate until identical"; %80-90'da dur, kalan ince ayarı koddan elle yap (değerler: offset, ikon boyutu).
5. **"UI önce, fonksiyon sonra"** iki aşamalı ekran görevi; fonksiyon fazı PLAN.md'ye referansla ("implement phase 3; tell me only what you need from dashboards").
6. **Skill + doküman besleme**: Clerk AI Skills (copy as markdown → "install the skills needed"), Expo Native Tabs/NativeWind dokümanı tam yapıştırma, Sentry/Inngest "copy for LLM", Context7 MCP global + prompt sonuna "use context7".
7. **Development build zorunlu** (`expo-dev-client`, `npx expo run:ios`); Expo Go yalnızca ilk dakikalar. Env değişikliği → `npx expo start --clear`; native modül → rebuild. Bu iki kuralı CLAUDE.md'ye yaz.
8. **Auth→DB senkron kalıbı**: Clerk webhooks (created/updated/deleted) → `/api/webhooks/clerk` Expo API route → arka plan işçisi (Inngest veya Trigger.dev) → Neon/Drizzle upsert; geliştirmede ngrok statik domain; `.env.example` şablonu projeye.
9. **Uzun AI işleri arka planda + UI polling/realtime**; otomatik retry + graceful fail; günlük rate limit tablosu (20/gün) ücretsiz V1'de bile; **Inngest experiments** ile model/prompt A/B (%90/%10) ürün sonrası iyileştirme aracı.
10. **Gözlem standardı**: Sentry init + logs (API hata/auth) + tracing (üretim fonksiyonu) + replay (prod'da maske açık) + **AI agent monitoring** (assistant/LLM çağrıları: token, maliyet, gecikme) → müşteriye "kullanıcılar ne soruyor" raporu.
11. **CodeRabbit kapısı**: her özellik ayrı branch → PR → bulguları "prompt for AI agents" ile **paralel Claude örneklerinde** düzelt → aynı branch'e commit → merge; skill dosyalarına gelen yorumları atla.
12. **Mağaza-uyum checklist'i**: Delete account (Clerk + DB + medya, onaylı), Google varsa Apple Sign-In, Privacy + Terms + Support canlı URL (profilde link), izin metinleri `app.json`, Unsplash attribution, rate app butonu.
13. **Legal + landing üretimi**: `legal/` HTML+CSS, dub.sh/legal-prompts (ToS, Privacy; placeholder'ları doldur), shots.so mockup'ları, referans site klonu, FFmpeg sıkıştırma, Cloudflare Workers/Wrangler ücretsiz deploy — her müşteri uygulamasına standart teslimat.
14. **Görsel kaynak**: Unsplash API ile konuma göre gerçek fotoğraf (ücretsiz, attribution'lı); kullanıcı yüklemeleri ImageKit üzerinden optimize; 5 MB görsel yasak.
15. **Kapsam disiplini**: V1 iOS-only (Apple Maps, API key yok), ödeme yok, mini model; "random vibe prompt" yok ("build me an app and don't make mistakes" yasak). Tüm diyagramları (iş akışı, webhook, Sentry, experiments) `sistem/` dokümanına koy (dub.sh/diagrams8).
