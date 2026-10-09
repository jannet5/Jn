# How to Build Real Mobile Apps with Claude - FULL COURSE — Codesistency — 191 dk — https://www.youtube.com/watch?v=tpge9xJ0m5U

## Tek paragraf: ne yapıyor, sonuç ne
Eğitmen, App Store'da yayınladığı kendi uygulamasının (Bulky AI; Cal AI klonu) basitleştirilmiş sürümünü 7 modülde sıfırdan kuruyor: Expo SDK 57 + React Native + NativeWind v4 ile onboarding (10 ekran) → OpenAI ile kişisel kalori/makro planı → Clerk (Google + Apple) ile giriş → Clerk webhook'larını ngrok üzerinden Expo API Route'a alıp Trigger.dev task'leriyle Neon Postgres'e (Drizzle ORM) kullanıcı senkronu → Expo Native Tabs (iOS liquid glass) ile Home / Scan / Profile → expo-camera + expo-image-picker ile yemek fotoğrafı, ImageKit'e yükleme, Trigger.dev arka plan task'inde OpenAI vision analizi ve Realtime hook'larla anlık UI güncellemesi → Sentry (session replay, logs, tracing, Seer otomatik düzeltme, user feedback widget'ı) → profil ekranında "Delete account" (Clerk + DB + ImageKit tam silme) → `legal/` klasöründe HTML/CSS ile landing + Privacy Policy + Terms of Service sayfaları ve Cloudflare Wrangler ile ücretsiz yayın. Tüm iş, VS Code Claude Code eklentisi (Opus 5, bypass permissions, sesli dikte) ile "plan → AI ile UI tasarımı → ekran ekran özellik → simülatör ekran görüntüsü karşılaştırma döngüsü → commit" akışıyla yapılıyor. Sonuç: simülatörde uçtan uca çalışan, mağazaya hazırlanabilir bir MVP + yayınlanmış landing/legal sayfaları (kaynak kodu ücretsiz).

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
**Açıklamadaki linkler (aynen):**
- Kaynak kod: https://dub.sh/code8
- Plan Mode Prompt: https://dub.sh/plan-mode
- Design Prompts (UI üretim prompt'ları, "calorie tracker app UI prompt" en altta): https://dub.sh/design-prompts
- Legal Pages Prompts (Terms of Service + Privacy Policy): https://dub.sh/legal-prompts
- Skool topluluğu: https://dub.sh/codesistency
- Trigger.dev: https://fandf.co/3Tacp8G
- Sentry: https://dub.sh/sentry5
- Neon: https://dub.sh/neon5
- ImageKit: https://tinyurl.com/3vv67dnh
- Clerk: https://go.clerk.com/codesistency

**Araçlar / servisler:**
- Claude Code — VS Code eklentisi ("Claude Code for VS Code"), model Opus 5 (Fable 5 "daha iyi ama çok token yakıyor" diye tercih etmedi), Plan Mode, "auto" izin modu (bypass permissions), mikrofonla sesli prompt
- Node.js, `npx create-expo-app@latest .` (SDK 57; telefonda Expo Go ile test edeceksen SDK 54)
- `expo-dev-client` + `npx expo run:ios` → development build (Expo Go yerine; "gerçek proje için Expo Go yetmez")
- NativeWind v4 (https://www.nativewind.dev — dokümanın tamamı kopyalanıp Claude'a yapıştırıldı)
- Expo Native Tabs (Expo dokümanı markdown olarak kopyalanıp prompt'a eklendi) — iOS liquid glass tab bar
- expo-camera (`useCameraPermissions`, `requestPermission`), expo-image-picker
- Clerk (Google + Apple; e-posta kapalı) + **Clerk AI Skills** sayfası (Claude'a yapıştırılıp proje için gerekli skill'ler kurduruldu; global `~/.claude/skills` ya da proje `.claude/skills`)
- Neon Postgres + Drizzle ORM (`src/db/schema.ts`, `index.ts`, `drizzle.config.ts`, `db:generate` / `db:migrate` script'leri)
- Trigger.dev + **Trigger.dev skills** (sitedeki "get the skills" komutu terminale yapıştırılır; seçilenler: getting-started, realtime, frontend; `trigger.config.ts`, `task()`, `logger`, Realtime hooks, public access token)
- ngrok (ücretsiz statik domain; Clerk webhook'unu localhost'a köprüler)
- OpenAI API (plan üretimi: GPT 5.6 Luna "fast"; yemek analizi: vision; çağrı başına ~$0.01)
- ImageKit (upload + URL transform ile downscale; "copy for LLM" dokümanı)
- Sentry (React Native projesi; Sentry Wizard yerine "copy instructions for LLM" prompt'u; session replay, logs, tracing, Seer, User Feedback)
- Context7 MCP (güncel doküman; "install Context7 in my machine" diye Claude'a kurduruldu)
- CodeRabbit (AI code review — akışta var, videoda atlandı)
- TanStack Query (plan sorularında seçildi), EAS development build (plan sorularında seçildi)
- ChatGPT (UI mockup/ekran/logo/mockup görseli üretimi; alternatif Gemini)
- shots.so (telefon çerçeveli mockup; iPhone 17, transparan arka plan, shadow none)
- GoFullPage Chrome eklentisi (referans sitenin tam sayfa ekran görüntüsü)
- Cloudflare Pages + Wrangler CLI (legal/landing yayını, ücretsiz)
- GitHub (`github.com/new`, VS Code Source Control ile AI commit mesajı + Sync)

**Dosyalar:** `PLAN.md` (büyük harf, to-do'lu), `AGENTS.md` (asıl kurallar) + `CLAUDE.md` (tek satır AGENTS.md'ye referans), `design/` (referans tasarım PNG'leri), `assets/images/logo-dark.png`, `assets/images/welcome-screen-ui-demo-image.png`, `.env` (gitignore'a eklendi), `legal/index.html`, `legal/privacy.html`, `legal/terms.html`, `legal/style.css`, `legal/demo-1.png`.

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)

### Modül 0 — Önizleme ve iş akışı [00:00:00–00:13:00]
1. [00:01:04] Demo: onboarding → "building your plan" (OpenAI) → giriş → Home (takvim, streak, bugünün öğünleri) / Scan (kamera veya galeri → "Analyze the food" → arka plan task) / Profile (Privacy, Terms, Sign out, Delete account, Send feedback [Sentry]).
2. [00:05:12] Mağaza reddi uyarıları: Privacy Policy + Terms sayfası zorunlu; "Delete account" zorunlu; Google Sign-In varsa Apple Sign-In de zorunlu.
3. [00:11:03] İş akışı diyagramı: **Plan & describe** (fikir, hedef, özellikler, ekranlar, kullanıcı akışı, tech stack) → **AI ile UI üret** → **ekran ekran özellik** → simülatörde kendin test et → AI code review → geçerse commit, geçmezse döngüyü tekrarla → özellik kalmayınca bitti.

### Modül 1 — Planlama & araç kurulumu [00:08:41–00:42:27]
4. [00:08:42] Boş klasör VS Code'da; Node.js kurulu; Claude Code VS Code eklentisi, model Opus 5.
5. [00:12:56] https://dub.sh/plan-mode içeriği kopyalanır, Claude'a yapıştırılır (AI'ın tahmin etmeyip 3-6'lık gruplar hâlinde soru sorması). Ardından sesli dikteyle proje anlatılır (prompt aşağıda), **Plan Mode** seçilip gönderilir [00:17:35].
6. [00:17:51] Trigger.dev'e GitHub ile giriş, proje: "cali-clone" (AI agent / Expo / ship a production workflow).
7. [00:20:48] Terminal: `npx create-expo-app@latest .` (mevcut klasöre). SDK seçimi: 57 (telefonda Expo Go ile test → 54).
8. [00:21:44] Claude'un plan soruları (seçimler): backend = **Expo API Routes**; AI workflow = Trigger.dev + realtime; model = OpenAI; "V1 done" = basit, çalışan, güzel UI.
9. [00:23:30] Neon: "Cal AI pro" projesi, varsayılan ayarlar, connection string. ImageKit: public key, private key, URL endpoint (Developer options). Sentry: giriş (proje daha sonra).
10. [00:26:22] Sonraki soru grupları: kalori hedefini **AI hesaplasın** (önerilen formül değil); sıra = **onboarding → auth → home**; fotoğraf **direkt app'ten ImageKit'e**; AI sonrası makrolar **manuel düzenlenebilir**; "building your plan" ekranı **sign-up'tan önce** (serbest metin cevabı aşağıda); AI hedefleri non-deterministic = kabul; home = kalori halkası + makro barları + yatay tarih şeridi + bugünün öğünleri + streak; saat dilimi = cihazdan, kullanıcı başına saklanır; kamera UX = **optimistic card + live** (Trigger realtime); AI hata = "food değil → retake" + **otomatik 1 retry**; V1'de ödeme/paywall/push yok ("skip these for V1. We just want to have a working MVP."); ORM = **Drizzle**, TanStack Query, NativeWind, **EAS development build**; Clerk→Neon senkronu = "Clerk webhooks and trigger.dev background jobs"; meal alanları = name, calories, protein, carbs, fat; test = manuel.
11. [00:36:22] AGENTS.md / CLAUDE.md açıklaması: tüm kurallar `AGENTS.md`'de, `CLAUDE.md` tek satırla ona referans verir; her oturum başında otomatik yüklenir.
12. [00:38:38] Plan onayı: normalde 10-15 dk okuyup "manually approve / keep planning"; burada "yes, auto accept" → `plan.md` to-do'larla üretildi; Clerk kurulumu teklifine "hayır, şimdilik sadece plan". Dosya `PLAN.md` olarak yeniden adlandırıldı.
13. [00:40:43] `github.com/new` → "cali-tutorial" (private); VS Code Source Control: stage all → AI commit mesajı → commit → GitHub'ın "push existing repo" komutları terminale.

### Modül 2 — UI tasarımı & Auth ekranı [00:42:27–01:16:07]
14. [00:42:36] https://dub.sh/design-prompts'tan "calorie tracker app UI prompt" kopyalanır; Claude'a "PLAN.md'yi oku, bu prompt'a benzer ama bizim plana özel bir prompt üret" denir; çıkan prompt ChatGPT'ye (görsel üretim) yapıştırılır.
15. [00:43:55] Kendi geçmiş akışını gösterir: ilk sonuç "çok renkli" → Cal AI ekran görüntüsü referans verilir ("light mode, mavi paleti kaldır, minimalist") → isim "Bulky AI" → ayrı sohbette logo: X'te bulduğu örnekle "grid of 12 logos for a fitness bulking app", sonra "black and white, no purple", "transparent background, dark and light modes" → logo tasarıma entegre edilir → "give me all the screens for onboarding" → "upscale the first five screens of onboarding" / "upscale the next five screens".
16. [00:47:28] `design/welcome-screen-ui-design.png` oluşturulur; welcome ekranındaki telefon görseli ChatGPT'den ("generate the phone image where the camera is open… transparent background") → `assets/images/welcome-screen-ui-demo-image.png`.
17. [00:49:15] **Build & verify loop** açıklaması: Claude simülatörden ekran görüntüsü alır, tasarım PNG'siyle karşılaştırır, aynı olana kadar tekrarlar.
18. [00:50:28] `npx expo start` → `i` (iOS). Sonra `npx expo install expo-dev-client` → `npx expo run:ios` → `ios/` klasörü oluşur, "cali-tutorial" adlı development build açılır.
19. [00:53:58] NativeWind v4 dokümanının tamamı kopyalanıp yapıştırılır + prompt (aşağıda). Claude paketleri kurar, config/global CSS/babel'i düzenler.
20. [00:55:45] Welcome ekranı prompt'u + `@logo-dark.png` eklenir; izin modu "auto" (bypass permissions; nasıl açılacağı Claude'a soruldu: "how to enable bypass permissions in Claude … go ahead and do it for me").
21. [00:58:04] 1. iterasyon: başlık 3 satır (olması gereken 2), font-weight ~700 (olması gereken ~500). Claude döngüyü kendisi sürdürür, 2. iterasyonda neredeyse birebir [00:59:22].
22. [01:00:56] `design/onboarding-1-5.png` ve `design/onboarding-5-10.png` ("skip for now" yazısız varyant seçildi) → onboarding prompt'u (aşağıda). Claude seçenekleri kendisi tıklayarak test eder [01:04:05].
23. [01:05:51] Clerk: "cali-tutorial" uygulaması, Google + Apple, e-posta kapalı. Quickstart → Expo SDK → `.env`'e `EXPO_PUBLIC_CLERK_PUBLISHABLE_KEY`.
24. [01:08:02] Clerk AI Skills sayfası kopyalanıp Claude'a yapıştırılır (prompt aşağıda); skill'ler `.claude/skills` altına (onda global zaten vardı).
25. [01:10:06] Auth ekranı prompt'u → Google/Apple butonlu ekran; Claude gerçek test yapmaya kalkınca durdurulup manuel test; Clerk Users'da kullanıcı görünür [01:12:15]. Takip: "Continue with Google" soluna Google ikonu.
26. [01:13:08] `.gitignore`'a `.env*.local` eklenir; commit + sync. CodeRabbit AI review adımı anlatılır, atlanır [01:14:12].

### Modül 3 — Webhook'lar & Onboarding verisi [01:16:07–01:41:03]
27. [01:17:10] Webhook anlatımı: `user.created` / `user.updated` / `user.deleted` → Clerk Dashboard → Configure → Developers → Webhooks → Add endpoint.
28. [01:19:07] ngrok'a giriş → Domains'ten ücretsiz domain. Kafa karışıklığı için Clerk + ngrok ekran görüntüleri Claude'a yapıştırılıp sorulur (prompt aşağıda). Cevap: endpoint `src/app/api/webhooks/clerk+api.ts`; Clerk'e `https://<ngrok-domain>/api/webhooks/clerk` yazılır [01:23:04].
29. [01:24:05] Signing secret → `.env` `CLERK_WEBHOOK_SIGNING_SECRET`; yeni terminalde ngrok komutu (Expo terminali ile birlikte sürekli açık). Neon → Connect → `.env` `DATABASE_URL` (tırnaksız).
30. [01:26:06] DB init prompt'u (aşağıda) → `schema.ts` (users, meals: imageUrl, userId, status…), `index.ts`, `drizzle.config.ts`, `db:generate`/`db:migrate` script'leri; Neon'da 2 tablo [01:28:29].
31. [01:30:38] Trigger.dev "get the skills" komutu terminalde: agent = Claude, skill'ler = getting-started + realtime + frontend → `.claude/skills`.
32. [01:31:51] Webhook prompt'u (aşağıda). Claude `trigger.config.ts` (project ID, retry/randomize/outOfMemory ayarları) oluşturur, `app.json` `web.output: "static" → "server"` yapar (API route şartı), `clerk+api.ts` imza doğrulaması, 3 task (`clerk-user-created/updated/deleted`, ~60 satır) [01:33:48].
33. [01:36:30] Test: Clerk'te kullanıcı silinip tekrar kayıt → DB'ye düşmedi: **Trigger dev terminali çalışmıyordu**; "what's the command to run trigger from my terminal?" → `npx trigger.dev@latest dev`; ayrıca `.env`'e `TRIGGER_SECRET_KEY` (Dashboard → API keys) → Expo yeniden başlatılır → Runs'ta `clerk-user-created` başarılı, DB'de kullanıcı var [01:39:16]. Commit.

### Modül 4 — Native Tabs, Home, Scan, Profile [01:41:03–02:22:33]
34. [01:41:19] `design/example-home-screen-ui-design.png` (Cal AI ekran görüntüsü; Progress/Groups sekmeleri istenmedi, 3 sekme: Home/Scan/Profile). Expo Native Tabs dokümanı markdown olarak kopyalanır; home/profile prompt'u (aşağıda) + PLAN.md eklenir.
35. [01:45:03] Sonuç benzer; yeni native modül → `Ctrl+C`, `npx expo run:ios` ile rebuild. Beklerken ikinci Claude örneğinde streak pop-up prompt'u (aşağıda). Expo debug butonu: "tools button" ile gizlenir, `Ctrl+D` ile geri gelir.
36. [01:47:30] Sheet alttan kayıyor ama overlay da kayıyor → "overlay should fade in" prompt'u → Claude animasyon tipini değiştirir [01:48:30].
37. [01:48:47] Home'daki değerler hardcoded, DB'de gender/dob/height/weight boş. OpenAI'da "cali-tutorial" secret key → `.env` `OPENAI_API_KEY`. Prompt (aşağıda): onboarding verisini OpenAI'a gönder, DB'ye kaydet, UI'da göster; onboarding'siz giriş yapan da zorunlu onboarding'e düşsün.
38. [01:53:52] `generate-plan` task: OpenAI SDK + Trigger `task`, model GPT 5.6 Luna, sistem prompt "you're a registered dietitian…", şema; Runs'ta payload/output görülür [01:55:46].
39. [01:56:58] Kayıt sonrası hata: `CLERK_SECRET_KEY` eksik (Clerk → Quickstart → Node/Express'ten kopyalandı); Expo yeniden başlatılınca 3740 kcal değeri DB'ye yazıldı, timezone "Asia/Makassar" doğru [02:00:07].
40. [02:01:06] Plan ekranı UI düzeltme prompt'u (boşluk + scroll).
41. [02:02:44] Scan akışı: foto → API route → ImageKit upload → `meals`'a pending satır → Trigger task (OpenAI vision) → DB güncelle → realtime ile UI. `.env`: `IMAGEKIT_PUBLIC_KEY`, `IMAGEKIT_SECRET_KEY`, `IMAGEKIT_URL_ENDPOINT`. Scan prompt'u (aşağıda).
42. [02:07:34] Sonrası: `npx expo prebuild` → `npx expo start --clear`. Dosyalar: `camera.tsx` (expo-camera, `useCameraPermissions`), `app.json` plugins → `expo-camera` `cameraPermission: "This app uses the camera to read the calories and macros in your meal"`, `src/app/api/meals+api.ts` (ImageKit upload, analyzing row, trigger run, public access token), `analyze-meal` task ("you're a nutritionist estimating what is on a plate from a single photo").
43. [02:11:21] Trigger Dashboard → Environment variables (development) → `DATABASE_URL` eklenir; "could not connect to the server" → yeniden başlat. İlk test duvar fotoğrafı: "That doesn't look like food" + `logger` ile Runs'ta görülür [02:12:40].
44. [02:14:01] Galeri butonu prompt'u → expo-image-picker + `app.json` izin metni. Test: "sliced chicken breast ~750 kcal" realtime güncellendi [02:15:43]. Kamera dışı gösterim: ilk denemenin bilerek fail edip Trigger'ın retry etmesi (attempt 1 failed → retry) [02:17:10]; sonra "bring it back to the previous version without failing".
45. [02:18:19] Home öğün listesi: ImageKit transform ile küçük boyut iste; prompt sonunda "use context7" (Context7 kurulumu: "Can you please install Context7 in my machine. So I want to have it for every single project.").
46. [02:20:44] "Not food" kayıtları listede → prompt: gösterme/kaydetme + native tabs'ın üstüne binmemesi için alt padding [02:21:05]. Commit.

### Modül 5 — Sentry [02:22:33–02:51:10]
47. [02:22:45] Neden Sentry: hata yakalama, session replay, performans/trace, structured logs, gerçek zamanlı uyarı (Slack/Gmail). `console.log` vs Sentry logs karşılaştırması.
48. [02:27:20] Sentry → Create project → React Native, slug "cal-ai-tutorial"; "copy instructions for LLM" → Claude'a prompt (aşağıda). Native modül → `npx expo run:ios`.
49. [02:29:47] `Sentry.init`: DSN, `integrations: [Sentry.mobileReplayIntegration({ maskAllImages: false, maskAllText: false, maskAllVectors: false })]` (geliştirmede maske kapatıldı), `enableLogs: true`. Source map için Settings → Organization tokens → "cal-ai-token" → `.env` `SENTRY_AUTH_TOKEN`.
50. [02:32:12] Test butonu prompt'u → Issues'ta hata, device/OS/trace ID/kod/session replay [02:33:00]; Seer tanıtımı; "okay it works remove that test button".
51. [02:34:30] Sentry Logs dokümanı yapıştırılır (prompt aşağıda) → 6 yer: plan üretildi (`api.ts`, formula→warn / ai→info, tag'li), onboarding tamamlandı, profile save failed, meal upload failed, `camera.tsx` sonuç.
52. [02:38:44] Tracing dokümanı (React Native) yapıştırılır → `tracesSampleRate` + `Sentry.wrap(App)`.
53. [02:40:36] `design/settings-screen-design.png` (Cal AI) → profil prompt'u (aşağıda) + 2 takip prompt'u. Delete account: onay + Clerk & DB silme; alt padding; tam silme (meals + ImageKit görselleri) [02:47:23]; test: DB, Clerk, ImageKit media library temiz [02:48:02].
54. [02:48:39] Welcome ekranındaki "scan meals…" alt yazısı elle silinir. Tekrar kayıt → doğrudan onboarding → plan ekranı düzgün boşluklu. Commit.

### Modül 6 — Optimizasyonlar [02:51:10–02:58:58]
55. [02:51:58] Downscale prompt'u + ImageKit dokümanı → `analyze-meal` içinde `visionTransform` (URL transform parametreleriyle gerçek zamanlı küçültme).
56. [02:52:51] Paralel ikinci Claude: takvim 2 hafta geriye scrollable (ileri gitmez). "Scan a meal" butonu elle silindi. `weekMap` uyarısı Claude'ca düzeltildi.
57. [02:54:39] Kamera arkası: Sentry test bench ekranı (hata simülasyon butonları). Seer: "Add repositories to this project" → repo seç → "Start analysis" → kök neden + "make a plan" → adım adım çözüm [02:55:16].
58. [02:56:38] Sentry User Feedback widget'ı profil ekranına (prompt aşağıda); Dashboard → User Feedback (test mesajı spam'e düştü; gerçek rapor inbox'ta, linked error ile).

### Modül 7 — Legal sayfalar & yayın [02:58:58–03:11:50]
59. [02:59:21] `legal/` klasörü. https://dub.sh/legal-prompts'tan Terms prompt'u ("codebase'i analiz et, production-ready ToS üret") + takip prompt'u (aşağıda). Claude'un sorularına (yaş sınırı, uyuşmazlık vb.) cevap verilir.
60. [03:01:48] shots.so: iPhone 17, Frame → background transparent, zoom max, simülatör ekran görüntüsü yapıştır, export → `legal/demo-1.png`; tilt/2-3 ekran varyantları.
61. [03:05:22] Çıktı: `index.html`, `privacy.html`, `terms.html`, `style.css`, logo + phone mockup kopyalanmış. İlk landing "disgusting / looks AI" (koyu tema) → GoFullPage ile Cal AI landing'in tam sayfa ekran görüntüsü referans + prompt → beyaz, temiz; mockup gölgesi shots.so'da "shadow: none" yapılıp yeniden export; "make this look a little bit larger".
62. [03:07:57] Privacy prompt'u → placeholder'lar (tüzel ad, adres) elle doldurulacak.
63. [03:08:53] Cloudflare hesap + Wrangler CLI → Claude'a deploy prompt'u → canlı URL; Claude kendi gerçek domaini (bulkyai.app) ile karıştırdı → "don't mess this up, leave everything as it is, this is just a demo project". Footer link rengi → "make the colors white just like in the navbar".
64. [03:10:56] Test verisi için seed script (Claude'a yazdırıldı). Son commit + sync.

## Kullandığı prompt'lar (varsa aynen)
Not: Prompt'ların çoğu sesli dikte; transkriptteki hâliyle, dolgu sözcükler ("you know") ayıklanarak aktarıldı. Dış şablon prompt'lar (plan mode, design, legal) videoda okunmadı; linkleri yukarıda.

Plan mode (interview prompt'unun altına dikte) [00:14:07]:
```
So, I want to build a project just like Cal AI. If you don't know already, it is a calorie tracking app. Basically, users will take a photo of their meal and then we will send this photo to OpenAI or any kind of AI agent. It's going to take a look at the photo, analyze it, then it's going to give the amount of calories we have in that meal, protein, fats, carbs, and even like macros. So that's the general idea of our application and we would like to make this experience for every single individual. So it's not going to be random. And to be able to achieve this, we can have an onboarding process. So we're going to ask users their age, their height, their weight, what's their goal. Do they want to cut weight or do they want to gain weight? So we would like to have the onboarding process. We would like to have an authentication screen. And then once they have gone through the authentication process and onboarding process we're going to take them to our homepage where they can take a look at their history of the foods and there will be a screen where they can open up their camera and take a photo of their meal. And then there will also be a profile screen where they can edit their information. So we're going to have the authentication screen, onboarding, the camera screen, home screen, profile, so on and so forth. For authentication I would like to use Clerk with Google and Apple authentication options. Then for database we're going to be using Postgres which will be provided by Neon. And for AI agents and background jobs we'll be using trigger.dev. For error tracking and monitoring we'll be using Sentry and for image storage and optimization as well as transformations we'll be using ImageKit. So keep those in mind and go ahead ask me the questions then eventually once I answer every single question you would like to create a plan MD file and we'll be using Expo and React Native to build this project but for now I don't really want you to initialize the Expo project.
```

Plan sorusuna serbest cevap (onboarding sırası) [00:29:19]:
```
First off user will go through the onboarding questions, then we will show the building your plan UI and then they will sign up and we will store that calculated data to database.
```

UI prompt'u ürettirme [00:42:53]:
```
Just go ahead and read the PLAN.MD file that I have and then depending on our application, depending on our plan, I want you to generate me a prompt similar to this one that I just provided.
<altına https://dub.sh/design-prompts'taki "calorie tracker app UI prompt" yapıştırılır>
```

NativeWind kurulumu [00:54:31]:
```
<NativeWind v4 kurulum dokümanının tamamı>
I have just attached the documentation of NativeWind step by step. I want you to follow it and implement it in this codebase. Don't do anything extra or less, just follow the documentation and make sure that NativeWind is working in this project.
```

Welcome ekranı + verify loop [00:55:48] (özet; ekranda okunmadı):
```
In this application we would like to have a welcome screen. Basically, when users open this app, this will be the first thing that they're going to see. For the UI design, I'm attaching this image and I want you to put yourself into a loop: take a screenshot from the simulator and compare it to this UI design, and keep doing this until they are both identical. @logo-dark.png
```

Onboarding ekranları [01:02:40]:
```
So far the welcome screen looks perfect. Do not change anything about it. But when users press the Get Started button we would like to start the onboarding process, just like what we have explained in PLAN.md file. And for the UI design, I'm going to attach you two different screens where we have around 10 different onboarding pages or screens. I want you to implement them. Try to copy them as much as possible and put yourself into this verify loop so that you can take a screenshot from the simulator and then compare it to the UI design.
<onboarding-1-5.png, onboarding-5-10.png>
Now these images are just references for UI design. You do not really need to ask the exact same questions. Just make sure to ask the questions that we have in plan.md.
```

Clerk skill'leri [01:08:34]:
```
<Clerk AI Skills sayfasının tamamı>
I have just pasted the entire Clerk AI skills page. I want you to install the skills that would be needed for this project. So this is an Expo React Native project and we're going to be using webhooks as well. So maybe you need to get the skills for backend as well. Go ahead and figure this out. Then install the skills for this project.
```

Auth ekranı [01:10:06]:
```
Currently I really like the UI, how everything works in the onboarding process. But at the end of this process, we should show some kind of a UI where we have the Google and Apple authentication buttons and when user press them, it should actually work. The authentication will be implemented with Clerk. I already have the environment variables and we also have the Clerk skills. So go ahead and try to implement it with a beautiful UI that matches the theme of our actual application. Keep it simple, minimalistic yet working.
```
Takip [01:12:37]: `Right next to the Continue with Google text on the left hand side we would like to have the Google icon. So go ahead and add it right there.`

Webhook kafa karışıklığı (Clerk + ngrok ekran görüntüleriyle) [01:20:41]:
```
Hey Claude, how are you doing? I have this kind of like confusion and I want you to help me out. So basically when users sign up with Clerk, we are storing them in Clerk only. I want to use trigger.dev with a background job. And with the webhooks I basically want to take the user from Clerk and save it to my Neon database. And for this I know that I need to create an API endpoint. But these screenshots that I have just attached kind of makes me confused. What do I do? What's going to be my endpoint looking like? Just help me out. How do I fill in the Clerk dashboard? So here on the endpoint URL, what do I paste? And for now, just don't really do anything. Just inform me and then I'll follow up with you, just in case if I ask you something, I'll follow up.
```

Veritabanı kurulumu [01:26:06]:
```
So in our case we already have the URL. We paste this into Clerk dashboard. We have our ngrok terminal up and running. We have our Clerk webhook signing secret as the environment variable. And I just got my database URL connection string as well. Now before you do anything else that is related to webhooks, first off, I want you to initialize my database which is Neon Postgres. So we will be using Drizzle as our ORM and for the data modeling you can take a look at our PLAN.MD file and just see all the details. Then you should give me some scripts that I can initialize the database, run migrations, so on and so forth.
@PLAN.md
```

Webhook + Trigger task'leri [01:31:51]:
```
So we have our ngrok set up. We have the ngrok terminal up and running as well. We have the database set up, schema, so on and so forth. Now go ahead and take a look at the trigger.dev skills that we installed under the .claude folder and just try to implement the webhooks. So I want you to generate three different methods. We're going to have one for user creation, one deletion, and for the update.
```

Home / Profile / Native Tabs [01:42:55]:
```
So far in our application, we have everything working from authentication to onboarding to webhooks. Now it's time to get started with the home screen, profile screen, as well as the camera screen. So under the camera screen, user will be able to take the photo of their food. And we will get into it later, but for now, we would like to design the home screen and the profile screen. For the tabs, we would like to use Expo native tabs and I'll just attach the documentation from Expo. So you can take a look at it as an example.
So for the UI design, I want you to keep this style the same as we have at the moment. And here under the design folder I have an example UI image. You can take a look at it as a reference file. So don't try to copy it 100% but make it as close as possible or just get inspired by it.
Now when you implement these screens please take a look at the plan.md file as we have the instructions right there. So at the beginning we have decided all kinds of features under the PLAN.MD. So if you don't know what to do don't guess anything. Just take a look at this plan file.
<design/example-home-screen-ui-design.png, PLAN.md, Expo Native Tabs dokümanı (markdown)>
```

Streak pop-up [01:46:32]:
```
Under the home screen at the very top we have this streak button. When we press that nothing happens but basically implement it in a way that when user press this button we would like to see a pop-up that is fading like sliding in from the bottom of the screen. Keep the UI simple but just make it motivational.
```
Takip [01:47:58]: `Everything is working fine as expected. But there is one thing that I don't really like, which is the overlay. Instead of having the overlay sliding in, it should be kind of like fade in. Go ahead and implement it.`

OpenAI ile plan üretimi + zorunlu onboarding [01:51:00]:
```
So far our application is mostly working as expected. But there is one issue which is about the onboarding and the data that we generate for this user. So how many calories left, protein, carbs and fat — I think these are hardcoded. You need to generate these values using OpenAI. Basically send the information of the user when they sign up or when they went through the onboarding process. You need to take these values, send them to OpenAI and whatever OpenAI returns you need to save it to database and then also display it on the UI.
And in the case of user signing up without going through the onboarding process, when they log in, you should still show them the onboarding process. Like no matter what happens, the user should go through the onboarding process. They have to provide these values like their gender, height, all kinds of questions that we have in the onboarding so that we can generate their macros, calories, so on and so forth. Go ahead and implement this.
```

Plan ekranı UI düzeltme (ekran görüntüsüyle) [02:01:06]:
```
So everything is working as expected, but the attached UI is kind of broken. I want you to add the proper spacing between the continue button and this purple container. And make sure that this page is scrollable, so we see the continue button at the bottom. Just overall go ahead and make this UI look clean.
```

Scan ekranı [02:05:27]:
```
So far in my application everything is working. I'm happy with what you have generated so far. Now I want you to build the scan screen. First off, when user visits this page, we would like to ask them for permission that we can access to their camera. Once they give the permission, we would like to allow them to take a photo of their meal and see some kind of button that says Analyze the food. Once they press that button, we would like to do a couple of different things. So we would like to save the image to ImageKit. We would like to save the meal to our database with the pending state and we would like to run the Trigger task where we will send this image to OpenAI, analyze it and get the result. Now while doing this we can use the real time feature of Trigger and for this I can provide you the documentation or actually you can take a look at the Trigger skills that we have under the .claude folder.
```

Galeri erişimi [02:14:01]:
```
Under the scan screen I want you to add a button for gallery access. And with this gallery access, we should be able to select food images from our gallery and provide it to our application. So it doesn't have to be only taking the photo from camera.
```

Retry testi geri alma [02:17:34]: `Okay, I tested this out. Bring it back to the previous version without failing.`

Context7 kurulumu [02:19:58]: `Can you please install Context7 in my machine. So I want to have it for every single project.`
(Home öğün listesi + ImageKit transform prompt'u ekranda okunmadı; sonunda `use context7` yazıyor.)

Not-food + tab padding [02:21:05]:
```
Under the today's meals section, when we have something that is not food, just don't display it in that section or even maybe don't store it in the database, so we don't really get it under this section. Also, just give some extra spacing or extra padding from the bottom so the native tabs are not really overlapping with these meals.
```

Sentry kurulumu [02:28:59]:
```
This is the entire instructions / documentation I got from Sentry. Go ahead, follow it and set this up in this project.
<Sentry "copy for LLM" kurulum talimatları>
```
Test [02:32:22]: `Now I just want to test out if everything will work for Sentry. I want you to add a test button in the home screen where I can just press and throw an error just to check if Sentry is working and then we'll delete this button.` → `Okay it works, remove that test button.`

Sentry logs [02:35:32]:
```
Now I will paste the documentation of Sentry logs and I want you to implement it in this project. We already enabled this under the root layout file. So all you need to do is finding proper use cases to include the Sentry logs.
<Sentry Logs (React Native) doküman sayfasının tamamı>
```

Sentry tracing [02:39:46]:
```
<Sentry Tracing (React Native) dokümanı>
I have just pasted the documentation for Sentry tracing. Please go ahead and implement this in my codebase in a simple way.
```

Profil ekranı [02:42:45]:
```
<design/settings-screen-design.png>
Now I want you to build me the profile screen. I mean, we already have the profile screen, but I don't really like the current UI that we have. So, take a look at this attached image that I just provided, and I want you to get inspired by it and build the similar style and similar content for this profile screen. I want you to remove the invite friends section, but add the rest of it, like the account, goals, and tracking. And at the very bottom, add a button that says delete your account and also have some links to privacy policy, terms of services.
```
Takip 1 [02:44:03]: `So, overall, I like the vibe of this screen, but I want you to put the sign out button at the very bottom of the screen. Maybe like just above the delete your account button. And also get rid of the goals and tracking section. We don't really want to see these values under the profile screen because we already have them under the home.`
Takip 2 [02:44:44]: `All right. So, overall, I like the profile screen, but just like under the settings-screen-design.png, I want you to have the section with the personal details, preferences, language, and upgrade to family plan buttons. Even though they're not going to do anything, just have them for UI purposes.`
Takip 3 [02:45:22]: `When we press this button, go ahead and make sure that the data of the user is deleted both from Clerk and our database, and it should ask for a confirmation message.`
Takip 4 [02:46:02]: `Go ahead and add some paddings under the profile screen. Just add some paddings from the bottom so the text at the very end is not really overlapping with the tab bar.`
Takip 5 [02:47:23]: `Just to make sure that every single data for this user will be deleted once they delete their account. So this would be the Clerk user, it would be the user from database, the meals that this user generated, the images in ImageKit, literally everything that is related to this user account.`

Downscale [02:52:00]:
```
Downscale the photo before sending it to the vision model when we scan the food. I'll just paste the documentation of ImageKit. You can take a look at it, take it as a reference and then go ahead implement that.
<ImageKit transformation dokümanı (copy for LLM)>
```

Takvim [02:52:55]:
```
Under the homepage we have the calendar but I want to make it in a way that it could be scrollable back to two weeks or maybe just one week. It cannot go to further dates but it can go back in time. Go ahead and implement it so that it can be scrollable.
```

Sentry feedback butonu [02:56:56]:
```
I want to have the Sentry feedback button under the profile screen, but it shouldn't be nested under the Sentry test bench. Maybe it should be just above the sign out button or somewhere in the profile screen. That would make sense.
```

Terms of Service + landing [03:00:46]:
```
<https://dub.sh/legal-prompts'taki Terms of Service prompt'u>
Create the terms of services under the legal folder that we have right here and it should have just some simple HTML and CSS. Make sure UI is looking in the same theme as our mobile app and there will be a landing page and in the navbar and footer we can have terms of services and privacy policy links. For now ignore privacy policy content. I will follow up for it later.
```
Takipler: landing yeniden tasarım prompt'u (Cal AI tam sayfa ekran görüntüsüyle; ekranda okunmadı) → `Make this look a little bit larger.` → Privacy Policy prompt'u (dub.sh/legal-prompts) → Cloudflare/Wrangler deploy prompt'u (ekranda okunmadı) → `Don't mess this up. Leave everything as it is. This is just a demo project.` → `Make the colors white just like in the navbar.`

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Tasarım önce kod sonra:** Önce ChatGPT ile tam bir "sistem tasarımı" görseli (tüm ekranlar tek görselde), sonra "upscale the first five screens" ile 5'erli yüksek çözünürlüklü parçalar; her parça `design/` klasörüne PNG olarak konup Claude'a verildi. Prompt'lar plandan türetildi (rastgele değil).
- **Referans görsel + sadeleştirme talimatı:** İlk AI tasarımı "çok renkli" çıkınca Cal AI ekran görüntüsü referans verilip "light mode, remove the blue palette, keep it minimalistic" dendi. Tüm uygulama beyaz/siyah minimal.
- **Logo:** ayrı sohbette "grid of 12 logos" → "black and white, no purple" → "transparent background, dark and light modes"; `logo-dark.png` olarak projeye alınıp tasarımlara entegre edildi.
- **Build & verify loop:** Claude'a "simülatörden ekran görüntüsü al, tasarımla karşılaştır, aynı olana kadar tekrarla" denmesi; welcome ekranında 2 iterasyonda birebir sonuç. Göz kontrolü: satır sayısı (3→2) ve font-weight (700→500, "daha zarif").
- **Native his:** Expo Native Tabs ile iOS liquid glass tab bar "kutudan çıktığı gibi"; expo-camera/expo-image-picker için `app.json`'da anlamlı izin metinleri.
- **Streak sheet:** alttan kayan sheet + **overlay'in kaymak yerine fade-in olması** (ayrı prompt'la düzeltildi — "it looks very off").
- **Tab bar çakışması:** liste ve profil ekranlarının altına ekstra padding (iki kez istendi).
- **Plan ekranı:** continue butonu ile kart arası boşluk + scrollable sayfa.
- **Küçük cilalar:** Google butonuna Google ikonu; welcome'daki fazla alt yazı silindi; gereksiz "Scan a meal" butonu silindi; profilde çalışmasa da "Personal details / Preferences / Language / Upgrade to family plan" satırları "boş görünmesin diye" eklendi.
- **Realtime geri bildirim:** Trigger Realtime ile "analyzing…" durumu ve sonucun kart içinde canlı dolması (optimistic card).
- **Görsel optimizasyonu:** ImageKit URL transform ile listede küçük görsel, vision modeline downscale edilmiş görsel.
- **Landing/mockup:** shots.so (iPhone 17, transparent background, shadow none, maksimum zoom), Cal AI landing'in GoFullPage tam sayfa ekran görüntüsü referans; koyu "AI görünümlü" ilk sürüm beyaz temaya çevrildi; legal sayfalar uygulama temasıyla aynı.

## Hatalar ve çözümleri
- Sesli dikte yazım hataları ("Kelly"→"Cal AI", "anchor"→"ngrok"): gönderimden önce düzeltildi ya da Claude bağlamdan anladı.
- Welcome ekranı ilk iterasyonda 3 satır başlık ve çok kalın font → verify loop ikinci turda düzeltti.
- Yeni native modül (expo-dev-client, Native Tabs, expo-camera, Sentry) sonrası uygulama çalışmadı → `Ctrl+C`, `npx expo run:ios` (ya da `npx expo prebuild` + `npx expo start --clear`) ile rebuild.
- Webhook geldi ama DB'ye kullanıcı düşmedi → Trigger dev terminali (`npx trigger.dev@latest dev`) çalışmıyordu + `.env`'de `TRIGGER_SECRET_KEY` eksikti; Expo yeniden başlatıldı; test için Clerk'teki kullanıcı silinip yeniden kayıt (aksi hâlde `user.created` tetiklenmez).
- Kayıt sonrası kırmızı hata + onboarding verisi DB'ye yazılmadı → `CLERK_SECRET_KEY` eksik/eşleşmiyor; Clerk Quickstart (Node/Express) sayfasından doğru projenin anahtarı kopyalandı, Expo yeniden başlatıldı.
- Trigger task'i DB'ye erişemedi → Trigger Dashboard → Environment Variables (development) → `DATABASE_URL`; "could not connect to the server" → terminal/uygulama yeniden başlatıldı.
- Simülatörde kamera yok → galeri butonu (expo-image-picker) eklendi; "retake" yavaş/buggy → reload.
- Duvar fotoğrafı: "That doesn't look like food" → istenen davranış; "not food" satırları Home listesinden ve DB'den çıkarıldı.
- Streak overlay'in slide olması → animasyon tipi fade.
- Liste/profil içeriği native tab bar altında kalıyor → alt padding.
- Plan ekranı kırık (buton kartın içine yapışık, scroll yok) → spacing + ScrollView.
- `weekMap` uyarısı (takvim) → Claude düzeltti.
- Landing ilk sürüm koyu ve "AI görünümlü" → referans tam sayfa ekran görüntüsü ile yeniden; mockup'ta gölge → shots.so'da shadow none; footer link rengi görünmüyor → beyaz.
- Cloudflare deploy'da Claude eğitmenin gerçek domainini (bulkyai.app) kullanmaya kalktı → "this is just a demo project, leave everything as it is".
- Expo debug butonu rahatsız edici → "tools button" ile gizle, `Ctrl+D` ile geri getir.
- Mağaza reddi tuzakları (önceden): Privacy/Terms linki yok, Delete account yok, Google var Apple yok.

## Bizim fabrikaya alınacaklar (somut, maddeli)
1. **Plan aşamasına "interview prompt" ekle:** dub.sh/plan-mode tarzı, 3-6 soruluk gruplar hâlinde soran, "V1 done ne demek?" diye soran, sonunda `PLAN.md` (to-do'lu) üreten prompt. Müşteri brifingi + tech stack bu prompt'un altına gider; Plan Mode zorunlu.
2. **AGENTS.md + CLAUDE.md düzeni:** tüm kurallar `AGENTS.md`'de, `CLAUDE.md` tek satır import. Kurallara ekle: "tahmin etme, PLAN.md'ye bak"; "yeni native modül eklersen rebuild gerektiğini söyle"; "liste/scroll ekranlarına tab bar için alt padding"; "overlay'ler fade, sheet'ler alttan slide"; "izin metinlerini app.json'a anlamlı yaz".
3. **Tasarım-önce pipeline:** PLAN.md → Claude'a "bu plana göre UI prompt'u üret" (dub.sh/design-prompts şablonu) → görsel model → referans görsel + "light, minimal, tek vurgu rengi" sadeleştirme → "upscale 5 screens" parçalar → `design/*.png`. Her ekran görevi mutlaka bir PNG ile açılır.
4. **Build & verify loop'u standart prompt yap:** "take a screenshot from the simulator, compare to design/<x>.png, iterate until identical". Kontrol listesi: satır sayısı, font-weight, spacing, alt padding.
5. **Skill paketi:** Clerk AI skills, Trigger.dev skills (getting-started/realtime/frontend), Expo skills; Context7 MCP global kurulu; dokümanı "copy for LLM" ile prompt'a yapıştırma alışkanlığı (NativeWind, Native Tabs, ImageKit, Sentry).
6. **Native his için zorunlu bileşenler:** Expo Native Tabs (liquid glass), expo-dev-client ile development build (Expo Go yasak), expo-camera/expo-image-picker izin metinleri, streak/başarı sheet'i gibi mikro-etkileşimler.
7. **Arka plan işleri:** uzun AI çağrıları Trigger.dev task'ine; `logger` zorunlu; Realtime hook ile UI'da "analyzing…" → sonuç canlı; otomatik retry (1 kez) + "retake" hata durumu; Trigger Dashboard'a env değişkenleri (DATABASE_URL vb.) eklemeyi checklist'e koy.
8. **Auth → DB senkronu:** Clerk webhooks (`user.created/updated/deleted`) → Expo API Route (`app.json` `web.output: "server"`) → Trigger task; geliştirmede ngrok statik domain; `.env` anahtarları: `EXPO_PUBLIC_CLERK_PUBLISHABLE_KEY`, `CLERK_SECRET_KEY`, `CLERK_WEBHOOK_SIGNING_SECRET`, `DATABASE_URL`, `TRIGGER_SECRET_KEY`, `OPENAI_API_KEY`, `IMAGEKIT_PUBLIC_KEY`, `IMAGEKIT_SECRET_KEY`, `IMAGEKIT_URL_ENDPOINT`, `SENTRY_AUTH_TOKEN`; `.env*` gitignore.
9. **Mağaza uyum checklist'i (her projede):** Privacy Policy + Terms canlı URL (profil ekranında link), "Delete account" (onaylı; Clerk + DB + depolama + ilişkili kayıtların tamamı), Google varsa Apple Sign-In, kamera/galeri izin metinleri.
10. **Sentry standart kurulumu:** `Sentry.init` + mobile replay (prod'da maske açık), `enableLogs: true`, tracing + `Sentry.wrap`, organization token ile source map, Seer için repo bağlama, profil ekranında User Feedback butonu; teslimden önce "test button → error → Issues'ta görüldü → butonu sil" ritüeli.
11. **Görsel optimizasyonu:** ImageKit (veya eşdeğeri) URL transform ile listede küçük görsel; vision modeline downscale ederek maliyet düşürme; kullanıcı silinince medya da silinir.
12. **Legal + landing üretimi:** `legal/` klasörü, dub.sh/legal-prompts'taki ToS/Privacy prompt'ları (codebase'i analiz eder, placeholder bırakır), uygulama temasıyla aynı HTML/CSS, shots.so mockup'ları (transparent, shadow none), referans landing'in tam sayfa ekran görüntüsü, Cloudflare Pages/Wrangler ile ücretsiz yayın.
13. **Çalışma disiplini:** her özellik sonrası commit (AI commit mesajı), tek görev tek Claude örneği (rebuild beklerken ikinci örnek açılabilir), kafa karıştıran her dashboard için ekran görüntüsü + bağlam verip "sadece bilgilendir, bir şey yapma" diye sorma, planı 10-15 dk okuyup onaylama, test datası için seed script.
14. **V1 kapsamı:** ödeme/paywall/push V1 dışında (RevenueCat + Expo push sonraki aşama); önce "basit, çalışan, güzel UI".
