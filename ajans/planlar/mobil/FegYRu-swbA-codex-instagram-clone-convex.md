# How to Build Real Mobile Apps with Codex - FULL COURSE 2026 — Codesistency — 155 dk — https://www.youtube.com/watch?v=FegYRu-swbA

## Tek paragraf: ne yapıyor, sonuç ne
Aynı eğitmenin Claude Code kurslarındaki (bkz. `tpge9xJ0m5U-calai-clone-full-course.md`, `p80OV6kjIO8-triply-ai-trip-planner-full-course.md`) "plan → AI ile UI tasarımı → ekran ekran inşa + simülatör ekran görüntüsü döngüsü → commit" iş akışını bu kez **OpenAI Codex VS Code eklentisi** (model "GPT-6 Astra", effort medium, Full access) ile tekrarlıyor ve "CodexGram" adlı basit bir **Instagram klonu** kuruyor: Expo SDK 57 + Native Tabs (iOS liquid glass) + NativeWind, Clerk (Google + Apple, custom native auth ekranı), backend olarak bu kez **Convex** (gerçek zamanlı DB + file storage; Clerk–Convex entegrasyonu), post/like/yorum/bookmark/follow, story (basılı tut-duraklat, dokun-sonraki, sil), gerçek zamanlı DM, explore + arama, profil/edit profile, settings (delete account, privacy/terms in-app sheet, Sentry feedback butonu). Claude kurslarından farklı olarak: Codex'in kendi bilgisayarı/simülatörü kullanarak kendini test etmesi, verify-loop'un bir **skill**'e (`.agents/skills/` + `.claude/skills/`) dönüştürülmesi, AGENTS.md'nin asıl kural dosyası olması (CLAUDE.md onu import ediyor), CodeRabbit PR akışının uçtan uca gösterilmesi, **EAS ile bulutta development build alıp gerçek iPhone'a kurma** (`--tunnel` ile bağlanma), Convex seed data ve "grid of nine / grid of three" ile görsel varyasyon üretimi. Son modüller Claude kursuyla neredeyse aynı: `legal/` klasöründe landing + ToS + Privacy + Support sayfası, shots.so mockup'ları, Cloudflare Pages + Wrangler deploy, Sentry (errors, logs, test sayfası, user feedback). Sonuç: iki cihaz arasında gerçek zamanlı çalışan, App Store kurallarına (Apple Sign-In, delete account, legal URL'ler) uygun bir MVP; kaynak kod ücretsiz.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
**Açıklamadaki linkler (aynen):**
- Kaynak kod: https://dub.sh/code10
- Plan Mode Prompt: https://dub.sh/plan-mode
- Design Prompts: https://dub.sh/design-prompts
- Legal Pages Prompts: https://dub.sh/legal-prompts
- Skool topluluğu: https://dub.sh/codesistency
- Sentry ($80 ücretsiz kredi): https://dub.sh/sentryy
- Expo: https://dub.sh/expo-yt
- Clerk: https://go.clerk.com/codesistency

**Claude kurslarına göre YENİ / farklı olanlar:**
- **Codex VS Code eklentisi** (OpenAI). Model seçici: "GPT-6 Astra" (transkriptte "GPD6 Astra"); effort: medium / high / extra high / ultra — "önemsiz özellikte medium/high, tam istediğim gibi olması gerekiyorsa extra high/ultra ama çok daha yavaş"; izin: **Full access** ("whatever you would like to do just do it, don't ask me"). Plan mode chat kutusundaki **"+"** menüsünden açılıyor. Dosya referansı `@dosya-adı` (ör. `@homescreen-ref.png`). Eğitmene göre Codex, Claude/Cursor'dan ucuz; 20$/ay plan öneriyor.
- **Codex'in computer-use / simülatör kontrolü**: Codex auth'u uyguladıktan sonra simülatörde kendisi tıklayıp test ediyor [00:54:00]; landing page'i tarayıcıda kendisi test ediyor [02:12:50].
- **Convex** (https://convex.dev) — backend + DB + file storage, gerçek zamanlı varsayılan. `npm install convex`, `npx convex dev` (yeni proje, cloud deploy, **"set up Convex AI files such as agents.md and some skills" = Evet**). Kod `convex/` klasöründe: schema, queries (okuma), mutations (yazma).
- **Clerk → Convex integration** (Clerk dashboard → Configure → Developers → Integrations → Convex aç).
- **CodeRabbit** (https://coderabbit.ai) — GitHub ile giriş, tüm repoları senkronla, PR'da otomatik review (~10 dk), "Prompt for all review comments with AI agents" kopyala → Codex'e yapıştır. Ayrıca **CodeRabbit VS Code eklentisi** (ücretsiz, commit'te popup review).
- **EAS (Expo Application Services)** — bulutta development build, `eas.json`, QR ile iPhone'a kurulum. Apple Developer Program 99$/yıl (Türkiye'de yerel fiyat ~20-25$ dedi).
- **expo-dev-client** — `npx expo install expo-dev-client`, `npx expo run:ios` (USB ile cihaz: `npx expo run:ios --device`).
- **ChatGPT "image 2.5"** (yeni görsel modeli; UI mockup, design system, logo, ekran upscale).
- **Live Server** VS Code eklentisi (landing `index.html` önizleme).
- **Skill dosyası**: `.agents/skills/<isim>/SKILL.md` (Codex/Cursor/diğerleri) + `.claude/skills/<isim>/SKILL.md` (Claude) — verify-loop skill'i, Codex'e ürettirildi.

**Claude kurslarıyla ortak olanlar (kısa):** VS Code, Node.js, `npx create-expo-app@latest .` (SDK 57), `npm run reset-project`, Expo Router **Native Tabs**, **NativeWind**, Clerk Expo SDK (publishable key `.env`, sonra secret key), shots.so (iPhone 17, no background, no shadow), Cloudflare Pages + **Wrangler** CLI, Sentry React Native (wizard yerine "LLM instructions", Sentry Logs, Tracing, User Feedback), GitHub + VS Code Source Control AI commit mesajı.

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)

### Modül 0 — Önizleme [00:00:00]
1. Demo: Google ile giriş, 4 native tab (Home, Messages, Explore, Profile), liquid glass; story yükleme (arka planda upload), tap-hold pause, tap next, sahibi silebilir; post + caption, like, yorum (like, sıralama), bookmark; gerçek zamanlı DM; explore + people/posts/topics arama; profil (posts, videos, bookmarks, tagged), edit profile (bio anında güncelleniyor), settings (privacy/terms gerçek URL, delete account, feedback formu), landing page [00:00:43–00:04:45].
2. Mesaj: Apple'ın reddettiği şeyler (privacy, terms, delete account) baştan planlanıyor; "kendi uygulamam ilk submit'te onaylandı" [00:06:56].

### Modül 1 — Kurulum ve planlama [00:09:32]
3. VS Code + Node.js; masaüstünde `CodexGram` klasörü, VS Code'a sürükle [00:10:44].
4. expo.dev/docs'tan komut: `npx create-expo-app@latest .` (nokta = mevcut klasör), SDK 57 sorusuna Enter [00:12:25].
5. `npx expo start`, `i` ile iOS simülatör (ilk açılış hatası kendi kendine düzeldi) [00:13:17].
6. Codex eklentisini kur; model "GPT-6 Astra", effort **medium**, permission **Full access** [00:17:33–00:19:15].
7. `Ctrl+J` terminal → `npm run reset-project` → oluşan `app-example` klasörünü sil → `npx expo` ile tek ekranlı boş uygulama [00:19:15–00:20:55].
8. Plan mode: "+" → Plan mode; dub.sh/plan-mode prompt'unu yapıştır + "we are about to describe you the project" + proje tanımı (Clerk, Convex, Google/Apple, post/follow/like/comment/DM, 4 tab, native tabs, NativeWind) [00:22:33–00:25:13].
9. Codex soru batch'leri (3-4'erli) ve verilen cevaplar [00:26:03–00:33:41]:
   - V1 hedefi: "just a working demo" (recommended).
   - Platform: **yalnızca iOS** önce.
   - Erişim: giriş yapan herkes.
   - Görsel: tekli upload.
   - Kayıt sonrası: **username seçimi zorunlu**; create butonu home header'da.
   - Yorumlar: düz (flat) + sadece kendi yorumunu silme.
   - Medya limiti: görsel ≤10 MB; video ≤30 sn ve ≤50 MB.
   - Push notification / ödeme yok ("trusted demo").
   - Auth UI: Clerk hazır UI değil, **custom native ekran** (AI tasarımından üretmek için).
   - Veri: Convex her yerde, online-first + retry.
   - Tema: "for now only light mode; for colors I'll provide a design reference later" (AI'nin mavi palet önerisini reddetti).
   - Video: tap-to-play.
   - Development build: evet (Expo Go = sadece öğrenme/test; native modüller çalışmaz) [00:30:27].
   - Takip yokken feed: **seeded demo content** (gerçek projede "honest empty state" seçerdi).
   - Mesajlar: unread göstergesi yok, sade liste.
   - Clerk: geliştirmede publishable key.
10. "Create a plan.md file with all the details" → **plan mode'da dosya yazamadı**; plan mode'u kapat → "do it now" → `plan.md` ~2 dk'da oluştu [00:33:41–00:35:30].
11. GitHub'da private repo → Source Control: Stage All → AI commit mesajı (✨ buton) → commit → GitHub'ın verdiği `git remote add origin ... && git push -u origin master` komutunu terminale yapıştır [00:35:30–00:37:12].

### Modül 2 — UI tasarımı üretimi [00:38:28]
12. dub.sh/design-prompts sayfasından örnek (trip planner) prompt'unu kopyala → ChatGPT'ye yapıştır + `plan.md` içeriği + "depending on the plan that I have I want you to generate me a prompt like this that will generate a UI design" → çıkan prompt'u **yeni chat'te** "Create image" (image 2.5) ile çalıştır [00:38:50–00:40:39].
13. Takip: logo C harfinden kamera ikonuna + design system'i ayrı upscale [00:41:32]; auth ekranını beğenmedi → "give me a different variation and upscale that image" [00:43:10].
14. Proje kökünde `design/` klasörü: `design-system-ref.png`, `auth-screen-ref.png`, `app-design-ref.png` (tüm ekranlar) [00:43:59–00:45:43]. Kural: her ekran ayrı upscale edilir, ama yeterli referans birikince geri kalan ekranları AI tasarım dilinden çıkarabiliyor [00:47:35].
15. Auth ekranındaki demo görseli: "generate me the demo image that you're using in the authentication screen" → yanlış görsel gelince ekran görüntüsü (`Cmd+Ctrl+Shift+4`) ile "I meant this one" + "make the background transparent so that it matches the white background" → `assets/images/auth-demo-image.png` [00:45:43–00:46:40].
16. Commit + Sync [00:47:35].

### Modül 3 — Authentication [00:48:42]
17. **Verify-loop** anlatımı: "build the screen, take a screenshot from the simulator, compare it to the design, if identical stop, if not keep going" → yeni chat'te auth ekranı için çalıştır [00:48:42–00:50:03].
18. Paralelde Clerk: yeni uygulama "CodexGram", e-posta kapalı, **Google + Apple** (Google varsa Apple zorunlu — yoksa ret) [00:50:51–00:51:37]. Expo SDK sayfası → publishable key → `.env` [00:51:37].
19. ~4 dk'da ekran neredeyse birebir; gereksiz "A social app trusted testers" metnini **kendisi koddan arayıp sildi** [00:52:30].
20. Clerk'in Expo "copy prompt"u + "implement authentication with Clerk" + doküman → Codex uyguladı ve **simülatörde kendisi tıklayarak test etmeye başladı**; eğitmen durdurup elle test etti [00:53:29–00:54:20].
21. Codex arka planda `ios/` klasörü + development build üretmiş. Manuel yol: `npx expo install expo-dev-client` → `npx expo run:ios` (Android için `run:android`) [00:55:10]. Bundan sonra Expo Go kullanılmıyor. `Ctrl+D` = dev menüsü (tools butonu, fast refresh, reload) [00:56:58].
22. Google ile giriş/çıkış çalıştı; Clerk'te Codex'in test sırasında açtığı kullanıcı da görünüyor [00:58:02].
23. ChatGPT'de aynı chat: "upscale the home screen, profile and chat screens. Generate each of them as a separate image" → sonra explore, edit profile sheet, comment sheet → `design/home-screen-ref.png`, `chat-screen-ref.png`, `profile-screen-ref.png`, `explore-screen-ref.png`, `edit-profile-ref.png`, `comment-sheet-ref.png` [00:58:02–00:59:03].
24. Home: "put yourself into this loop once again and build the home screen… create some fake data" + `@homescreen-ref.png` [01:00:12]. Döngü sürerken kalan overflow için ekran görüntüsü + "fix this overflow" — **her prompt'ta tek düzeltme** [01:01:46].
25. Native Tabs: Expo dokümanında "Copy page as markdown" → "convert tabs to native tabs. Here is the entire documentation" + yapıştır (~5 dk) [01:02:49]. Sonra ekran görüntüsü + "native tabs looks as I wanted, but remove the gray background so the rest of the application is visible" (<1 dk) [01:03:42].
26. Profile (loop + ref, ~6 dk) → Edit profile (ref) → "delete the sign out button from edit profile screen" + "replace the three dots with a settings icon" → Explore (ref) → Chat listesi + detay (ref) [01:04:32–01:08:01]. **Gerekçe: önce tüm UI fake data ile, sonra backend'e bağlamak kolay** [01:05:21].
27. **CodeRabbit akışı** [01:08:49–01:17:01]: coderabbit.ai'de GitHub ile giriş, repoları senkronla → VS Code `Cmd+Shift+P` → "Create new branch" `auth-ui-design` → stage + AI commit + Publish Branch → GitHub'da "Create pull request" → ~10 dk review (2 minor öneri) → en üstteki "prompt for all review comments for AI agents" → Codex'e yapıştır (~1 dk düzeltme) → aynı branch'e commit + sync (yeni PR açma) → Merge → master'a dön, sync.
28. Merge sonrası `.env` değişikliği: Codex arka planda secret key eklemiş (dikkat noktası) [01:17:01].

### Modül 4 — Backend (Convex) ve telefonda çalıştırma [01:17:54]
29. İki terminal kuralı: biri `npx expo` (adı "expo"), diğeri `npx convex dev` (adı "convex") — terminalleri sağ tık → Rename [01:18:46, 01:22:16].
30. `npm install convex` → `npx convex dev` → yeni proje "codexgram" (Tab+Enter) → cloud → **Convex AI files (agents.md + skills) = Yes** [01:21:22].
31. Logo arası [01:19:44]: ChatGPT "generate a grid of nine icons for this app that I can choose from" → beğenmedi → "give me a completely different style and vibe. Again, a grid of nine" → "upscale option six and make it in the blue theme" → "remove the background" → `assets/images/logo.png` → Codex: "use this logo.png as my brand logo in this app, replace the current one" (50 sn) [01:23:11].
32. Backend prompt'u: plan.md'yi oku, post creation, likes, comments, follows'u uygula, "let me know if I need to set up anything from the Clerk or Convex dashboard" [01:23:11–01:24:10]. Codex sordu: Clerk'te Convex integration açık mı? → Clerk dashboard → Configure → Developers → Integrations → Convex ON → "it is enabled" [01:24:10–01:25:02]. ~20 dk.
33. Test: username ekranı → boş state → post (galeriden görsel, caption "this weekend trip…") → Convex dashboard → Development → Files'da dosya, Data'da `uploads`, `profiles` tabloları [01:25:51–01:26:48].
34. Yorum sheet'i alttan sıkışık → ekran görüntüsü + "fix the comment section. It should be a bit more spaced out from the bottom" [01:27:56].
35. Post detail ekranı için ChatGPT'ye ekran görüntüsü + "give me three different variations… matching with the blue color palette" [01:27:56]. Varyasyonlar benzer çıkınca **design system görselini de ekleyip** tekrar istedi → "upscale the second version" → `design/post-detail-ref.png` [01:34:07–01:34:58].
36. Seed data: "add some seeded data that would be helpful for me to see the rest of the app with a lot of data" → 24 kurgusal profil, foto, like, yorum (~10 dk) [01:29:44–01:30:35]. Follow/unfollow yavaş → optimistic update gerekli notu [01:31:29].
37. Chat: "implement the chat messaging" + `@chat-screen-ref.png` + ChatGPT'den "grid of three… matching with the rest of the design system" → "upscale the variation third" → `design/messages-tab-ref.png` + "put yourself into a loop" (~17 dk, test ağırlıklı) [01:32:21–01:33:18]. DB'de mesaj göremeyince sorgu: "when we send a message does it get saved to the database or all of this data are still hardcoded?" → demo hesaplar fake, gerçek hesapla test et [01:33:18–01:34:07].
38. Post detail: tüm ekranı değil **sadece alt kısmı** referanstan al (header mevcut hali daha iyi); referansın alt bölümünün ekran görüntüsü + "only update this part" (~10 dk) [01:34:58–01:36:01].
39. Telefona kurulum — iki yol [01:36:47]: (a) USB + `npx expo run:ios --device`; (b) **EAS**. Codex'e: "I'd like to get a development build and install it in my phone… I want to use EAS… give me everything step by step" → `eas.json` oluştu [01:39:22–01:40:10]. Sonra "give me the command to get a development build on the cloud" (≈ `eas build --profile development --platform ios`; Android için platform değişir) [01:41:46].
40. Hata 1: "invalid version" → çıktıyı Codex'e yapıştır → cache düzeltmesi → tekrar çalıştır, sorulara Enter [01:42:40]. Hata 2: expo.dev dashboard'da build failed → ilk başarısız adımın logları → "my dev build failed and here are the logs" → düzeltme → tekrar [01:43:30–01:44:19]. Commit edilmemiş değişiklik sorusu: Codex "commit gerekmiyor" dedi [01:44:19].
41. Build bitince QR → iPhone'a kurulum → uygulama dev server arıyor → Codex'e "in my terminal I have a QR code. How do I run this?" → iPhone Settings → Privacy & Security → **Developer Mode** (istenirse) → Mac'te `npx expo start` (görünmezse `--tunnel`) → terminaldeki URL'nin `http…` kısmını telefondaki alana yapıştır [01:45:16–01:47:04].
42. Telefonda yeni hesapla giriş, username "my phone"; simülatör ↔ telefon arasında follow, like, yorum, DM **gerçek zamanlı** [01:48:19–01:50:13]. Commit (gerçek projede PR + CodeRabbit derdi) [01:51:06].

### Modül 5 — Uygulamayı tamamlama [01:51:47]
43. Off-camera: Explore yeni ref + loop (13 dk, tek seferde); Home: "do the exact same thing but do not change the icon" + **stories** özelliği; sonra "delete caption from stories", "allow camera, not only library", "in post creation keep the caption but also add the camera option" [01:51:58–01:53:33].
44. **Verify-loop skill'i** [01:53:33–01:56:57]: Codex'e "I don't want to type this every single time. Can you please create me a skill?" + "put it under the agents folder as well as the Claude folder" → `.agents/skills/<loop-skill>/SKILL.md` ve `.claude/skills/<loop-skill>/SKILL.md`. Artık prompt sadece "make sure profile screen looks like this image" + ref; skill oturum başında yükleniyor.
45. Profil: arama ikonunu sil, settings ikonu; edit profile/share profile butonlarını kaldır; **delete account + sign out** gerekli [01:56:57–01:57:45].
46. Settings tasarımı: ChatGPT tasarım chat'inde "grid of three for setting screen" (privacy policy, terms, delete account, sign out, sosyal medya uygulamasına uygun diğerleri) → "upscale the variation one" → `design/settings-screen-ref.png` → "build me this exact UI" (~4 dk) → "delete this header part" [01:58:30–02:00:18].
47. Regresyon: Settings eklerken **Native Tabs → JS tabs'e döndü**. Prompt: "also bring back the native tabs… remember we always want to use native tabs" → Codex kuralı **AGENTS.md**'ye yazdı ("always use expo-router native tabs") [02:00:18–02:03:03].
48. AGENTS.md vs CLAUDE.md: ikisi de oturum başında otomatik yüklenen "AI için README"; kural AGENTS.md'de, CLAUDE.md onu import eder [02:01:12–02:03:53].
49. Delete account: Codex "Clerk secret key'i Convex dashboard'a ekle" dedi → Clerk → API Keys → Secret key → Convex dashboard → Project settings → Environment variables → Add → "that's done, go ahead and implement it" → test: Clerk'te kullanıcı 2→1 [02:03:53–02:05:38].

### Modül 6 — Legal sayfalar + landing [02:06:05]
50. Zorunlu liste: delete account ✓, Apple Sign-In ✓, privacy policy, terms, **support URL** (App Store Connect'te istenir) [02:06:28].
51. dub.sh/legal-prompts'tan ToS prompt'u + "put it all under the legal folder and actually build me a landing page as well. Make sure it looks pretty good" [02:07:18–02:08:16].
52. shots.so: iPhone 17 mockup, Frame → no background, Mockup → **no shadow**, simülatör ekran görüntüsünü (copy to clipboard) yapıştır, üst/alt boşluğu yok edecek kadar zoom, export ×3 → `legal/demo1.png`, `demo2.png`, `demo3.png` → "in landing page use these images" (~17 dk, tarayıcıda kendini test etti) [02:08:16–02:12:50].
53. `legal/index.html` → sağ tık → Open with Live Server; mavi tema mobil uygulamayla uyumlu, referans vermeden; placeholder'lar (şirket adı, URL'ler) elle doldurulacak [02:13:02–02:14:36].
54. Privacy prompt'u → footer'a link [02:14:36]. Support: "also add a support page. Please keep it simple and super quick to build… don't test, only build" [02:16:12].
55. Deploy: "deploy the legal folder to Cloudflare Pages, use Wrangler" (kurulum yoksa "set this up for me") → 1 dk 20 sn'de canlı; `/privacy`, `/terms`, `/support` URL'leri App Store Connect'e [02:17:06–02:18:51].

### Modül 7 — Sentry [02:19:35]
56. Problem: hata alan kullanıcı e-posta atmaz, 1 yıldız verip siler [02:20:34]. Sentry özellikleri: errors, structured logs, session replay, tracing, real-time alert (e-posta/Slack/Discord/Teams) [02:21:19].
57. Sentry → Create project → repo seç (React Native otomatik algılandı) → isim → alert varsayılan (e-posta) → wizard yerine talimatları + DSN'i Codex'e yapıştır (~6 dk) [02:22:54–02:27:13]. **Native modül → rebuild**: `npx expo run:ios` [02:27:13].
58. Sentry Logs dokümanı "copy page" → "set up sentry logs for me" (1 satır, <1 dk). console.log vs Sentry logs: cihazda kaybolur / bulutta, aranabilir, trace-error-replay'e bağlı; seviyeler debug/info/warn/error/fatal [02:25:29–02:28:06].
59. "create me a Sentry test page under the settings and create some errors and logs when we press those buttons" (~2 dk) → Issues'ta hatalar + e-posta; Explore → Logs [02:28:06–02:30:35]. Tracing'i ödev bıraktı.
60. Feedback: Sentry React Native feedback dokümanı + "under the settings screen, add a nice feedback button with Sentry" (~2 dk) → Issues → User Feedback [02:31:20–02:33:22].
61. Legal linkler: "when we press privacy policy and terms of services link it to the actual deployed URLs" → sonra "can you open this up within the app? Like a sheet would come from the bottom and we would see the website" [02:33:22–02:34:16]. Commit; sonrası = yeni design ref + takip prompt'larıyla UI cilası [02:34:16].

## Kullandığı prompt'lar (varsa aynen)
Not: Plan/design/legal prompt'larının tam metni dub.sh linklerinde; video bunları okumuyor. Aşağıdakiler eğitmenin sesli okuduğu/ekranda gösterdiği kısımlar (transkriptten aynen).

**Proje tanımı (plan mode prompt'unun arkasına) [00:22:33]:**
```
we are about to describe you the project
I want to build a simple Instagram style social media application using Clerk for authentication and Convex for the back end and database. Users should be able to sign up and log in with Google or Apple. They should be able to create images or videos as posts, follow and unfollow, like and comment. [...] sending direct messages [...] view and edit their profile. The app should have four main tabs which are going to be home, messages, explore, and profile. We will use native tabs. For styling we want to use NativeWind.
```

**Plan dosyası [00:33:41]:**
```
for now create a plan MD file with all the details so that later we can implement this step by step
```
```
for now only create the file. Later I'll ask for implementation.
```
(plan mode kapatıldıktan sonra) `do it now`

**ChatGPT — UI prompt'u ürettirme [00:39:41]:**
```
depending on the plan that I have I want you to generate me a prompt like this that will generate a UI design
```

**ChatGPT — logo + design system [00:41:32]:**
```
This is a good start, but I want you to update the logo from the C letter to a camera icon. similar to Instagram but a little bit different. So do not copy it exactly. Also upscale the design system as a separate image.
```
```
give me a different variation and upscale that image. also upscale the design system.
```

**ChatGPT — auth demo görseli [00:45:43]:**
```
I want you to generate me the demo image that you're using in the authentication screen.
```
```
I meant this one [ekran görüntüsü] ... make the background to be transparent so that it matches the white background of the application.
```

**ChatGPT — ekran upscale [00:58:02]:**
```
upscale the home screen profile and chat screens. Generate each of them as a separate image.
```
```
upscale the explore screen [...] the edit profile sheet, comment sheet [...] generate every single one of them as a separate image.
```

**Verify-loop (skill olmadan önce) [00:49:14, 01:00:12]:**
```
Go ahead and build the authentication screen. Take a screenshot from the simulator and compare it to the actual design. If it is identical, just stop. If it is not, then keep going.
```
```
put yourself into this loop once again and build the home screen. For now we don't have a database, so create some fake data and build the UI. @homescreen-ref.png
```
```
now build the profile screen. use that loop and here is the reference image
```

**Tek düzeltme prompt'ları (ekran görüntüsüyle):**
```
fix this overflow
```
```
convert tabs to native tabs. And here is the entire documentation. [Expo Native Tabs sayfası "Copy page as markdown"]
```
```
native tabs looks as I wanted, but remove the gray background. So the rest of the application is visible.
```
```
delete the sign out button from edit profile screen
```
```
replace the three dots with a settings icon. Later we will build that page.
```
```
fix the comment section. It should be a bit more spaced out from the bottom
```

**Logo varyasyonu — grid of nine [01:19:44]:**
```
generate a grid of nine of icons for this app that I can choose from
```
```
give me a completely different style and vibe. Again, a grid of nine.
```
```
upscale this and make it in the blue theme
```
```
remove the background
```
```
use this logo.png as my brand logo in this app. replace the current one with this one.
```

**Backend implementasyonu [01:23:11]:**
```
now that we have authentication and most of the UI design ready, go ahead and read my plan.md so that you know what kind of features we want to build, then start implementing the post creation, likes, comments and follows. Let me know if I need to set up anything from the Clerk or Convex dashboard.
```

**Seed data [01:29:44]:**
```
add some seeded data that would be helpful for me to see the rest of the app with a lot of data
```

**ChatGPT — ekran varyasyonu (grid of three) [01:27:56, 01:32:21, 01:58:30]:**
```
this is the current UI that I have for the post detail screen. I want you to give me three different variations of this UI. try to keep it simple and make sure it is matching with the blue color palette that we have already. just give me three different variations.
```
```
give me a grid of three for this UI. make sure that it is matching with the rest of the design system.
```
```
upscale the variation third
```
```
I want you to give me a grid of three for setting screen [privacy policy link, terms of services, delete account, sign out, and some of the other things that you would need in a social media application]
```

**Chat + doğrulama sorusu [01:32:21, 01:33:18]:**
```
implement the chat messaging @chat-screen-ref.png @messages-tab-ref.png put yourself into a loop
```
```
when we send a message does it get saved to the database or all of this data are still hardcoded?
```

**EAS / telefon [01:39:22–01:47:04]:**
```
I'd like to get a development build and install it in my phone and I want to use EAS (expo application services). What I need to do, give me everything step by step.
```
```
give me the command to get a development build on the cloud
```
```
my dev build failed and here are the logs [log yapıştır]
```
```
I have uncommitted changes. Do I have to commit them beforehand?
```
```
now in my terminal I have a QR code. How do I run this? Like what do I do?
```
(İlk kez yapan için önerisi: `I am a complete beginner. I have never done this before. So tell me all the steps that I need to do.`)

**Home + stories [01:52:43]:**
```
do the exact same thing for the home screen, but do not change the icon. [...] implement the stories feature
```
```
delete the caption feature from the stories. allow us to take the photo from camera, not only from library. in the post creation, keep the caption, but also add the camera option.
```

**Skill üretimi [01:55:13]:**
```
I don't want to type this every single time. Can you please create me a skill? Put it under the agents folder as well as the claude folder.
```
Skill sonrası ekran prompt'u: `make sure profile screen looks like this image` + ref.

**Settings / native tabs kuralı [01:59:19, 02:00:18]:**
```
build me this exact UI @settings-screen-ref.png
```
```
delete this header part
```
```
also bring back the native tabs. remember we always want to use native tabs.
```

**Delete account [02:04:47]:**
```
now go ahead and implement the account deletion
```
```
that's done. go ahead and implement it.
```

**Legal + landing [02:08:16–02:17:57]:**
```
[dub.sh/legal-prompts — Terms of Service prompt'u] put it all under the legal folder and actually build me a landing page as well. make sure it looks pretty good.
```
```
in landing page use these images which are demo1, 2 and 3.
```
```
also add a support page. Please keep it simple and super quick to build. don't test, only build.
```
```
deploy the legal folder to Cloudflare pages. use Wrangler.
```

**Sentry [02:23:38–02:33:22]:**
```
[Sentry "copy instructions" + DSN yapıştır]
```
```
set up sentry logs for my project [Sentry Logs React Native sayfası]
```
```
create me a sentry test page under the settings and create some errors and logs when we press those buttons
```
```
under the settings screen, add a nice feedback button with sentry [Sentry React Native feedback sayfası]
```
```
when we press privacy policy and terms of services link it to the actual deployed URLs
```
```
can you open this up within the app? Like a sheet would come from the bottom and we would see the website.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Plan → ChatGPT'ye UI prompt'u ürettir → yeni chat'te görsel üret** iki aşaması (prompt'u kendisi yazmıyor; design-prompts'taki örnek kalıbı plana uyarlatıyor).
- **Design system görseli ayrı upscale** ve her ekran ayrı upscale; `design/*-ref.png` adlandırma tutarlılığı ("settings screen reference just to be consistent with the rest of the names").
- Varyasyon tekniği: **"grid of nine"** (logo; 9 kez ayrı istemek yerine) ve **"grid of three"** (ekran); varyasyonlar birbirine benzer çıkarsa **design system görselini ekle**.
- Referansın **sadece bir bölümünü** uygulatma (post detail'de header mevcut kalsın, alt aksiyon/yorum bölümü refden) — kısmi ekran görüntüsü + "only update this part".
- Native Tabs (liquid glass) + **gri arka planı kaldırma** (içerik tab bar'ın arkasından görünsün); kural AGENTS.md'de kalıcı.
- Görsel asset'ler transparan arka planla (auth demo görseli, logo) — uygulamanın beyaz zemine oturması için.
- Light-mode only V1; renkler AI'nin tahmin ettiği paletten değil, tasarım referansından.
- Seed data (24 profil) ile explore/feed'in dolu hâliyle tasarım değerlendirmesi.
- Legal linkleri Safari yerine **uygulama içi alttan açılan sheet**.
- shots.so: no background + **no shadow** + maksimum zoom → landing'de "havada duran telefon" mockup'ları.
- Animasyon özelinde bir şey yok (Reanimated vb. kullanılmadı); story etkileşimi (tap-hold pause, tap next) Instagram davranışı olarak istenmiş.

## Hatalar ve çözümleri
- **İlk `i` ile simülatör açılmadı** → bekledi, kendiliğinden düzeldi [00:14:14].
- **Plan mode'da `plan.md` yazılamıyor** → plan mode'u kapat, "do it now" [00:35:30].
- **ChatGPT görsel yerine prompt döndürdü / yanlış demo görseli** → çıkan prompt'u yeni chat'e yapıştır; yanlışta ekran görüntüsüyle "I meant this one" [00:40:39, 00:45:43].
- **Codex kendi kendine test ederken uzuyor** → işlevin bittiğine eminsen durdur, elle test et [00:54:20]; zaman kritikse "don't test, only build" [02:16:12].
- **Loop bittiği hâlde overflow kaldı** → ekran görüntüsü + tek fix'li prompt [01:01:46].
- **Tab bar gri arka plan** → "remove the gray background" [01:03:42].
- **Native Tabs → JS tabs regresyonu** (settings eklenirken) → geri getir + kuralı AGENTS.md'ye yazdır [02:00:18].
- **Fake veri ile gerçek veri karışıklığı** (chat'te DB'de mesaj yok) → Codex'e doğrudan sor; demo hesaplar hardcoded, ikinci gerçek hesapla test et [01:33:18].
- **Follow/unfollow gecikmesi** → not edildi (optimistic update ile hızlandırılabilir), çözülmedi [01:31:29].
- **Codex `.env`'e secret key eklemiş** (merge sonrası değişiklik) → fark et, commit'leme dikkat [01:17:01].
- **EAS "invalid version"** → hata çıktısını Codex'e ver, cache düzeltmesi, tekrar [01:42:40].
- **EAS build failed** → expo.dev'de ilk başarısız adımın loglarını kopyala → "my dev build failed and here are the logs" [01:43:30].
- **Dev build telefonda sunucuyu bulamıyor** → Developer Mode, `npx expo start --tunnel`, URL'yi elle gir [01:46:04].
- **Delete account için secret key** → Clerk secret key'i Convex env variables'a [02:03:53].
- **Sentry sonrası crash riski** → native modül = `npx expo run:ios` ile rebuild [02:27:13].

## Bizim fabrikaya alınacaklar (somut, maddeli)
Claude kurslarından gelen maddeler (plan-mode interview, design pipeline, verify-loop, UI-önce, dev build, Sentry, legal+Cloudflare, mağaza checklist'i) aynen geçerli; aşağıdakiler bu videonun **ek/yeni** katkıları:
1. **Verify-loop'u skill yap, iki yere koy**: `.agents/skills/design-verify-loop/SKILL.md` + `.claude/skills/design-verify-loop/SKILL.md` (aynı içerik). Ekran görevi prompt'u tek satıra iner: "make sure <ekran> looks like @design/<ekran>-ref.png". Fabrika şablon reposuna hazır skill olarak gömülsün.
2. **AGENTS.md = tek gerçek kaynak, CLAUDE.md = `@AGENTS.md` import**: böylece aynı repo Codex/Cursor/Claude ile çalışır. Kural listesine ekle: "always use expo-router Native Tabs — never replace with JS tabs", "tab bar has no background (transparent)", "light mode only unless told", "never write secrets into .env without telling me".
3. **Regresyon kuralı**: bir AI'nin geri aldığı her tasarım kararı (Native Tabs gibi) anında AGENTS.md'ye kural olarak yazdırılır ("remember we always want to …" kalıbı).
4. **Convex alternatif backend profili**: gerçek zamanlı ihtiyaç (chat, sosyal, canlı sipariş durumu) olan işletme uygulamalarında Neon+Drizzle+Trigger yerine Convex — `npx convex dev` sırasında **Convex AI files (agents.md + skills) = Yes**, Clerk → Integrations → Convex ON, Clerk secret key Convex env'e. İki terminal (expo / convex) adlandırılmış.
5. **Seed script standardı**: her müşteri demosunda `convex/seed` (veya eşdeğeri) ile 20+ gerçekçi profil/kayıt — boş ekranla demo yok; prod'da "honest empty state" tasarımı ayrıca.
6. **Görsel varyasyon kalıpları** fabrika design aşamasına: logo için "grid of nine → upscale option N + brand color → remove background"; beğenilmeyen ekran için "grid of three + design system görseli ekli → upscale variation N"; kısmi güncelleme için referansın parçasının ekran görüntüsü + "only update this part".
7. **Tek prompt = tek düzeltme** kuralı + her düzeltmede simülatör ekran görüntüsü (QA turlarında checklist).
8. **CodeRabbit kapısı somut akış**: özellik branch'i → PR → "Prompt for all review comments with AI agents" → AI'ya yapıştır → aynı branch'e commit (yeni PR yok) → ikinci review'u beklemeden merge. Ek olarak CodeRabbit VS Code eklentisi lokal ön-kontrol.
9. **EAS cloud dev build reçetesi** (müşteriye cihazda demo için): `eas.json` development profili → `eas build --profile development --platform ios` → QR ile kurulum → iPhone Developer Mode → `npx expo start --tunnel` → URL girişi. Hata olursa expo.dev'deki ilk failed step logu AI'ya. Bu adımları fabrikanın "cihazda demo" runbook'una yaz.
10. **İki cihazla gerçek zamanlı kabul testi** (simülatör + telefon, farklı hesaplar): follow, like, yorum, DM anlık yansımalı — sosyal/sohbet özellikli her teslimatta zorunlu test.
11. **Settings ekranı standart paketi**: privacy + terms + support (uygulama içi bottom sheet WebView), delete account (Clerk + DB), sign out, **Sentry User Feedback butonu**; dev build'de gizli **Sentry test sayfası** (hata/log üreten butonlar) — prod'a çıkmadan kaldır/feature-flag.
12. **Ajan test süresini yönet**: Codex/Claude'un otomatik simülatör/tarayıcı testi değerli ama uzun; basit sayfalarda "don't test, only build", kritik akışlarda test serbest. Effort politikası: rutin ekran = medium, ödeme/auth/veri silme = high/extra high.
13. **Sorgulama alışkanlığı**: AI'nın gerçekten DB'ye yazıp yazmadığını sor ("does it get saved to the database or is this still hardcoded?") — fake→gerçek veri geçişinde zorunlu kontrol maddesi.
14. **V1 kapsam kararları şablonu** (plan interview cevap ön-ayarları): iOS-first, push/ödeme yok, medya limitleri (görsel 10 MB, video 30 sn/50 MB), flat yorum + sadece kendi yorumunu silme, kayıt sonrası zorunlu username, custom auth ekranı (hazır Clerk UI değil).
