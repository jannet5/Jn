# Build your own Mobile App with Codex GPT-6 Astra - Full iOS and Android Course — freeCodeCamp.org (kurs: @codesistency) — 155 dk — https://www.youtube.com/watch?v=bGALxRWI10A

## Tek paragraf: ne yapıyor, sonuç ne
Eğitmen VS Code içindeki Codex eklentisiyle (model: GPT-6 Astra, effort "medium", izin "full access") sıfırdan "CodexGram" adlı Instagram tarzı bir sosyal medya uygulaması yapıyor. Teknoloji yığını: Expo (SDK 57, expo-router), NativeWind, Expo Router **Native Tabs** (iOS liquid glass), Clerk (Google + Apple girişi), Convex (backend, veritabanı, dosya depolama, gerçek zamanlı veri), Sentry (hata takibi, loglar, kullanıcı geri bildirimi) ve Cloudflare Pages (yasal sayfalar ile landing page). İş akışı sırasıyla şöyle: (1) Plan Mode'da "beni sorgula" prompt'uyla ilerleyip `plan.md` üretmek, (2) ChatGPT görsel modeliyle (image 2.5) tüm uygulamanın UI tasarımını, design system görselini ve her ekranın büyütülmüş (upscale) referans görselini üretip `design/` klasörüne koymak, (3) her ekranı "simülatörden ekran görüntüsü al, referansla karşılaştır, aynı olana kadar devam et" döngüsüyle kurmak (bu döngü daha sonra bir skill haline getiriliyor), (4) önce sahte veriyle tüm UI'ı bitirip sonra Convex'e bağlamak, (5) PR açıp CodeRabbit'e AI code review yaptırmak, (6) EAS ile development build alıp gerçek telefona kurmak, (7) App Store reddini önleyen zorunlu parçaları eklemek: Sign in with Apple, hesap silme, privacy policy / terms / support URL, (8) Sentry ile izleme. Sonuçta hikâyeler (stories), gönderi, beğeni, yorum, takip, gerçek zamanlı mesajlaşma, explore, profil düzenleme, ayarlar, hesap silme, geri bildirim formu ve uygulama içi web sheet ile açılan yasal sayfalar çalışır durumda. Eğitmen kendi uygulamasının bu akışla App Store'a ilk denemede reddedilmeden girdiğini söylüyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
**Açıklamadaki linkler (aynen):**
- Kaynak kod: https://dub.sh/code10
- Plan Mode Prompt: https://dub.sh/plan-mode
- Design Prompts: https://dub.sh/design-prompts
- Legal Pages Prompts: https://dub.sh/legal-prompts
- Sentry: https://dub.sh/sentryy
- Expo: https://dub.sh/expo-yt
- Clerk: https://go.clerk.com/codesistency
- Skool topluluğu: https://dub.sh/codesistency

**Araçlar ve kütüphaneler:**
- VS Code ve Codex eklentisi (GPT-6 Astra). Claude Code for VS Code de alternatif olarak gösteriliyor (doğrulanmış/verified eklenti).
- Node.js (nodejs.org).
- Expo: `create-expo-app` (SDK 57), `npm run reset-project`, `npx expo start`, `expo-dev-client`, `npx expo run:ios` (EAS, `eas.json`).
- Expo Router **Native Tabs**: iOS liquid glass sekme çubuğu. Doküman sayfası "Copy page as markdown" ile kopyalanıp prompt'a yapıştırılıyor.
- NativeWind (React Native için Tailwind).
- Clerk: Expo SDK; `.env` içine publishable key (`EXPO_PUBLIC_CLERK_PUBLISHABLE_KEY`); hesap silme için Convex ortam değişkenlerine `CLERK_SECRET_KEY`; Clerk Dashboard > Configure > Integrations > Convex açık.
- Convex: `npm install convex`, `npx convex dev`. Proje kurulurken "convex AI files" (agents.md ve skill'ler) seçiliyor. `convex/` klasöründe schema, queries, mutations ve file storage var.
- ChatGPT (image 2.5 modeli) veya Gemini: UI tasarımı, design system, ekran büyütme, ikon ızgarası.
- CodeRabbit: GitHub repo senkronu, PR review, "Prompt for all review comments for AI agents"; VS Code eklentisi de var.
- shots.so: cihaz mockup'ı (iPhone 17, arka plan yok, gölge yok).
- VS Code Live Server eklentisi (landing page önizleme).
- Cloudflare Pages ve **Wrangler** CLI (deploy).
- Sentry: React Native SDK, Sentry Wizard, DSN, Sentry Logs, Sentry Tracing, User Feedback widget.
- App Store Connect, Apple Developer Program (yıllık 99$, bölgesel fiyat farkı var).
- Skill: `.agents/skills/<isim>/SKILL.md` ve `.claude/skills/<isim>/SKILL.md`. İçinde "screenshot ve referansla karşılaştırma döngüsü" var.
- `AGENTS.md` (asıl kurallar) ve `CLAUDE.md` (AGENTS.md'yi import ediyor).

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)

### Modül 0: Önizleme [00:00:00]
1. [00:00:50] Demo: Google/Apple girişi, 4 Native Tab (Home, Messages, Explore, Profile), liquid glass görünümü.
2. [00:01:36] Stories: kütüphaneden veya kameradan seçim, arka planda yükleme, basılı tutunca durma, dokununca sonraki hikâye, sahibi silebiliyor.
3. [00:02:26] Gönderi oluşturma (caption ile), beğeni, yorum, yorum beğenisi ve sıralama, bookmark.
4. [00:03:12] Gerçek zamanlı DM, explore ve arama, profil (posts, videos, bookmarks, tagged), edit profile.
5. [00:04:04] Settings: privacy policy ve terms gerçek URL'ye bağlı (yoksa Apple reddediyor), delete account (yoksa red), feedback butonu.
6. [00:04:50] Landing page: demo mockup görselleri, footer'da yasal linkler.

### Modül 1: Planlama [00:09:59]
7. [00:10:05] VS Code ve Node.js kuruldu. Masaüstünde `CodexGram` klasörü açıldı.
8. [00:11:41] expo.dev/docs sayfasındaki komut sonuna `.` eklenerek çalıştırıldı: `npx create-expo-app@latest .` (SDK 57).
9. [00:14:13] `npx expo start`, ardından `i` ile iOS simülatöre açıldı. Hata çıktığında Codex'e sorulması öneriliyor.
10. [00:15:16] Codex eklentisi kuruldu. Model GPT-6 Astra, effort **medium** (kritik özelliklerde extra high/ultra; bunlar yavaş), permissions **full access**.
11. [00:20:15] `npm run reset-project` çalıştırıldı; `app-example` klasörü silindi.
12. [00:21:52] Plan Mode'a geçildi (Codex'te "+" ve "Plan mode"). https://dub.sh/plan-mode prompt'u yapıştırıldı. Bu prompt AI'ın kullanıcıyı her konuda sorguya çekmesini sağlıyor. Ardından proje tarifi yazıldı (aşağıdaki "Prompt'lar" bölümüne bakın).
13. [00:24:21] Expo Native Tabs dokümanı incelendi; tarife "we will use native tabs" ve "NativeWind" eklendi.
14. [00:26:13] AI 3-4 soruluk partiler halinde sorular sordu. Verilen cevaplar şöyle:
    - Hedef çalışan bir demo. Platform önce yalnız iOS. Giriş yapmış herkes kullanabiliyor.
    - [00:27:51] Tek görsel yükleme. Kayıttan sonra username seçimi zorunlu. Düz (flat) yorumlar, kullanıcı yalnızca kendi yorumunu siliyor.
    - Medya limitleri: görsel en fazla 10 MB; video en fazla 30 sn ve 50 MB.
    - [00:28:50] V1'de push notification ve ödeme yok ("trusted demo", önce MVP).
    - [00:29:39] Clerk'in hazır UI'ı yerine kendi native ekranlar. Her yerde Convex. Online-first ve retry.
    - Tema yalnızca light mode. Renkler sonradan verilecek tasarım referansından alınacak (AI'ın kendi mavi paletini seçmesi engelleniyor).
    - Video "tap to play". Expo Go yerine **development build** (Expo Go yalnızca deneme için, native modüllerin hepsi çalışmıyor).
    - [00:32:14] Takip ettiği kimse olmayan yeni kullanıcıya seed demo içerik gösteriliyor. Gerçek projede "honest empty state" tercih edilirmiş.
    - Mesajlarda unread göstergesi yok (bare chat list). Clerk publishable key ile development.
15. [00:33:53] "create a plan.md file with all the details" dendi. Plan Mode dosya yazamadığı için mod kapatılıp "do it now" denildi ve `plan.md` oluştu.
16. [00:36:32] GitHub'da private repo açıldı. VS Code'da "stage all", AI ile commit mesajı üretildi, GitHub'ın verdiği `git remote add ... && git push` komutu çalıştırıldı.

### Modül 2: UI tasarımı üretme [00:38:55]
17. [00:39:01] https://dub.sh/design-prompts sayfasında örnek prompt'lar var (trip planner, recipe, dating, kalori uygulaması, dental). Trip planner prompt'u kopyalandı.
18. [00:39:48] ChatGPT'de **meta-prompt** kullanıldı: örnek prompt ve `plan.md` yapıştırılıp "depending on the plan that I have, I want you to generate me a prompt like this that will generate a UI design" dendi. Çıkan prompt **yeni bir sohbette** "Create image" ile (image 2.5) çalıştırıldı.
19. [00:41:42] Takip mesajıyla logo kamera ikonuna çevrildi ve design system ayrı görsel olarak büyütüldü.
20. [00:43:21] Auth ekranı beğenilmedi; "give me a different variation and upscale that image" dendi. "Tek seferde mükemmel bekleme, takip mesajı yaz" vurgusu yapılıyor.
21. [00:44:08] `design/` klasörü açıldı: `design-system-reference.png`, `auth-screen-reference.png`, `app-design-reference.png` (tüm ekranların bir arada olduğu görsel).
22. [00:45:53] Auth ekranındaki demo görseli ayrıca üretildi. Ekran görüntüsü (Cmd+Ctrl+Shift+4) ile hangi görselin istendiği gösterildi, "make the background transparent" dendi ve sonuç `assets/images/auth-demo-image.png` olarak kaydedildi.
23. [00:48:29] Commit ve sync yapıldı.

### Modül 3: Authentication [00:49:09]
24. [00:49:22] **Screenshot döngüsü** tanıtıldı: ekranı kur, simülatörden ekran görüntüsü al, referansla karşılaştır; aynıysa dur, değilse devam et.
25. [00:50:11] Yeni sohbette auth ekranı bu döngüyle istendi ve `npx expo start` ile `i` çalıştırıldı.
26. [00:51:05] Clerk'te uygulama oluşturuldu: email kapalı, Google ve Apple açık. **Kural:** Google gibi bir sosyal giriş varsa Apple girişi zorunlu, yoksa red.
27. [00:52:37] Clerk Expo SDK sayfasından publishable key `.env` dosyasına yazıldı.
28. [00:53:35] Yaklaşık 4 dk sonra ekran referansla neredeyse aynı çıktı. Gereksiz "A social app for trusted testers" metni elle silindi.
29. [00:53:35] Clerk'in hazır "AI prompt"u ve doküman yapıştırılıp "implement authentication with clerk" dendi. [00:54:35] Codex bilgisayarı kullanıp akışı kendisi test etti.
30. [00:55:26] Development build: `npx expo install expo-dev-client`, ardından `npx expo run:ios` (Android için `run:android`). `ios/` klasörü oluşuyor. Bundan sonra Expo Go kullanılmıyor.
31. [00:57:10] Simülatörde Ctrl+D ile developer menüsü (fast refresh, reload). Google girişi test edildi ve Clerk Users'ta kullanıcı göründü.
32. [00:58:16] ChatGPT'de aynı sohbette "upscale the home screen, profile, and chat screens. generate each of them as a separate image" dendi. Ardından explore, edit profile sheet ve comment sheet istendi. Dosyalar `design/home-screen-ref.png`, `chat-screen-ref.png`, `profile-screen-ref.png`, `explore-screen-ref.png`, `edit-profile-ref.png`, `comment-sheet-ref.png`.
33. [01:00:21] Home ekranı döngüyle kuruldu: "create some fake data", referans `@home-screen-ref.png` ile eklendi.
34. [01:02:01] Kalan hatalar ekran görüntüsü alınarak düzeltildi; **her prompt'ta tek düzeltme** isteniyor ("fix this overflow").
35. [01:02:58] Native Tabs dokümanı "Copy page as markdown" ile kopyalanıp "convert tabs to native tabs. Here is the entire documentation" dendi. Yaklaşık 5 dk sürdü.
36. [01:03:57] "native tabs looks as I wanted, but remove the gray background so the rest of the application is visible" dendi.
37. [01:04:45] Profile ekranı ("now build the profile screen. use that loop"), ardından edit profile kuruldu.
38. [01:05:35] **İlke:** önce tüm UI sahte veriyle bitiyor, backend sonra bağlanıyor.
39. [01:06:23] Edit profile'dan sign out kaldırıldı; üç nokta ikonu settings ikonu yapıldı.
40. [01:07:21] Explore ve chat ekranları referanslarla kuruldu.

### Modül 3b: AI code review ve branch akışı [01:09:04]
41. [01:09:51] CodeRabbit'e GitHub ile giriş yapıldı ve repolar senkronlandı.
42. [01:10:34] Branch, commit, PR ve merge mantığı anlatıldı.
43. [01:12:53] Cmd+Shift+P ile "Create new branch" seçildi (`auth-ui-design`), stage, AI commit mesajı, commit, "Publish branch" ve GitHub'da "Create pull request".
44. [01:14:39] Yaklaşık 10 dk sonra CodeRabbit review'ı geldi (2 minor öneri). "Prompt for all review comments for AI agents" kopyalanıp Codex'e verildi.
45. [01:16:21] Düzeltmeler commit ve sync edildi, PR merge edildi. Master'a geçilip sync yapıldı.

### Modül 4: Backend (Convex) ve telefonda çalıştırma [01:18:21]
46. [01:18:58] İki terminal açık tutuluyor: `expo-mobile` (`npx expo start`) ve `convex` (`npx convex dev`). Kurulum için önce `npm install convex`.
47. [01:20:00] Logo: ChatGPT'de "generate a grid of nine of icons for this app that I can choose from". Beğenilmeyince "completely different style and vibe, again a grid of nine" dendi, "upscale option six and make it in the blue theme" ve "remove the background" ile bitirildi. `assets/images/logo.png`.
48. [01:21:37] `npx convex dev`: yeni proje `codexgram`, cloud deployment, **"set up Convex AI files (agents.md, skills)" evet**.
49. [01:23:26] "use this logo.png as my brand logo in this app, replace the current one" dendi (yaklaşık 50 sn).
50. [01:24:24] Prompt: `plan.md` okunup post creation, likes, comments ve follows implement edilecek; "let me know if I need to set up anything from the clerk or convex dashboard" ekleniyor.
51. [01:25:14] Codex'in sorusu üzerine Clerk > Configure > Developers > Integrations > Convex açıldı. Toplam yaklaşık 20 dk.
52. [01:26:04] Username onboarding çalıştı. Gönderi oluşturuldu ve Convex Dashboard > Development > Files ile Data (uploads, profiles) üzerinden doğrulandı.
53. [01:27:52] Yorum UI hatası (alttan boşluk) ekran görüntüsüyle düzeltildi. Post detail için ChatGPT'den 3 varyasyon istendi.
54. [01:29:56] "add some seeded data" ile 24 sahte profil, gönderiler, beğeniler ve yorumlar eklendi (yaklaşık 10 dk).
55. [01:32:34] Mesajlaşma: chat ekranının ekran görüntüsü ChatGPT'ye verilip "give me a grid of three for this UI ... matching with the rest of the design system" dendi, 3. varyasyon büyütüldü: `design/messages-tab-ref.png`. "implement the chat messaging" isteği `@chat-screen-ref.png`, `@messages-tab-ref.png` ve "put yourself into a loop" ile verildi (yaklaşık 17 dk).
56. [01:34:19] DB'de mesaj görünmedi. Sorulunca demo hesapların sahte veri olduğu, gerçek test için ikinci hesap gerektiği anlaşıldı.
57. [01:35:09] Post detail: ilk 3 varyasyon birbirinin aynısı çıktı. Design system görseli eklenince farklılaştı, 2. varyasyon büyütüldü: `design/post-detail-ref.png`. Yalnızca alt kısım uygulandı ("only update this part" ve ekran görüntüsü), yaklaşık 10 dk.
58. [01:36:57] Gerçek telefon için yol 1: USB ile `npx expo run:ios --device`.
59. [01:37:49] Yol 2: EAS. Apple Developer Program yıllık 99$ (Türkiye'de daha ucuz).
60. [01:40:17] Codex'e "I'd like to get a development build and install it in my phone ... I want to use EAS ... give me everything step by step" dendi. `eas.json` oluştu. İlk kez yapılacaksa `eas login`, `eas init` ve cihaz kaydı gerekiyor.
61. [01:41:53] "give me the command to get a development build on the cloud" dendi. Komut `eas build --profile development --platform ios` (Android için platform değişiyor).
62. [01:42:40] "invalid version" hatası alındı: Codex cache'i düzeltti, komut yeniden çalıştırıldı ve sorulara Enter ile geçildi.
63. [01:43:33] expo.dev'deki build başarısız oldu. Başarısız adımın logları kopyalanıp "my dev build failed and here are the logs" dendi ve düzeltildi.
64. [01:44:24] Uncommitted değişiklikler EAS'i engellemedi (Codex "commit gerekmez" dedi).
65. [01:45:23] Build bitince verilen QR okutuldu ve uygulama kuruldu. Gerekirse iPhone'da Settings > Privacy & Security > Developer Mode açılıyor.
66. [01:46:10] Mac'te `npx expo start` çalıştırıldı, olmayınca `--tunnel` ile. URL elle girildi (http… kısmı kopyalanıp dev client'a yapıştırıldı).
67. [01:48:12] Telefonda Google girişi ve username yapıldı. [01:49:18] Takip, mesaj, beğeni ve yorumun telefon ile simülatör arasında gerçek zamanlı aktığı gösterildi (Convex).
68. [01:51:07] Commit yapıldı. CodeRabbit VS Code eklentisi tanıtıldı.

### Modül 5: Uygulamayı tamamlama [01:52:14]
69. [01:52:14] Explore ekranı yeni referans ve döngüyle tek seferde kuruldu (yaklaşık 13 dk).
70. [01:52:49] Home ekranı aynı yöntemle yenilendi ("do not change the icon here") ve stories eklendi. Story caption'ı kaldırıldı, kamera seçeneği eklendi; post creation'da caption kaldı, kamera eklendi.
71. [01:54:27] **Döngü skill'e çevrildi.** "I don't want to type this every single time. Can you please create me a skill" dendi. Codex `.agents/skills/<isim>/SKILL.md` ve `.claude/skills/<isim>/SKILL.md` oluşturdu. Artık "make sure profile screen looks like this image" demek yetiyor; skill otomatik yükleniyor.
72. [01:57:45] Profilden search ikonu kaldırıldı, settings ikonu eklendi.
73. [01:58:35] Settings tasarımı: ChatGPT'deki tasarım sohbetinde "grid of three for setting screen" istendi (privacy policy, terms, delete account, sign out ve sosyal uygulamada olması gereken diğer satırlar). 1. varyasyon büyütüldü: `design/settings-screen-ref.png`, ardından "build me this exact UI" (yaklaşık 4 dk). Header kaldırıldı.
74. [02:01:08] Native tabs kaybolup JS tabs'a dönmüştü. "also bring back the native tabs ... remember we always want to use native tabs" dendi ve Codex kuralı `AGENTS.md`'ye yazdı: "always use expo-router native tabs".
75. [02:02:09] `AGENTS.md` / `CLAUDE.md` açıklaması: oturum başında otomatik yükleniyor. Kurallar AGENTS.md'de tutuluyor, CLAUDE.md onu import ediyor.
76. [02:03:57] Hesap silme: Codex `CLAUDE_SECRET_KEY` değil **`CLERK_SECRET_KEY`** istedi. Değer Clerk > API keys'ten alınıp Convex Dashboard > Settings > Environment Variables'a eklendi. [02:05:39] Silme onay diyaloğu çalıştı ve kullanıcı Clerk'ten silindi.

### Modül 6: Yasal sayfalar ve landing page [02:06:32]
77. [02:06:36] Zorunlu kontrol listesi: delete account, Sign in with Apple, privacy policy, terms of service, **support URL** (App Store Connect istiyor).
78. [02:07:23] https://dub.sh/legal-prompts sayfasındaki terms prompt'u yeni sohbete yapıştırıldı (kod tabanını okuyup terms sayfası yazdırıyor). Ek: "put it all under the legal folder and actually build me a landing page as well, make sure it looks pretty good".
79. [02:09:15] shots.so: iPhone 17 mockup, Frame > background yok, Mockup > **shadow yok**, ekran görüntüsü yapıştırılıp boşluklar gitsin diye maksimum zoom, export. 3 görsel `legal/demo-1.png`, `demo-2.png`, `demo-3.png`.
80. [02:12:55] "in landing page use these images which are demo one, two, and three" dendi. Yaklaşık 17 dk sürdü, Codex tarayıcıda kendisi test etti. `legal/index.html` Live Server ile açıldı. Landing page mobil uygulamanın mavi temasını izledi. Terms `/terms` altında.
81. [02:14:42] Placeholder alanlar (şirket yasal adı, URL'ler) elle doldurulacak.
82. [02:15:28] Privacy prompt'u yapıştırıldı ve footer linki oluştu.
83. [02:16:12] "also add a support page. Please keep it simple and super quick to build ... don't test, only build" dendi. Support email güncellenecek.
84. [02:17:56] "deploy the legal folder to Cloudflare pages ... use Wrangler, I already have the setup and login" dendi. 1 dk 20 sn sürdü; `/privacy`, `/terms` ve `/support` URL'leri App Store Connect'e giriliyor.

### Modül 7: Hata takibi ve izleme (Sentry) [02:20:02]
85. [02:20:37] Sorun şu: kullanıcı hata alınca e-posta atmıyor, 1 yıldız verip uygulamayı siliyor. Sentry hata yakalama, yapılandırılmış log, session replay, tracing ve e-posta/Slack/Discord/Teams alarmları sunuyor.
86. [02:22:54] Sentry'de proje oluşturuldu: GitHub repo seçilince React Native otomatik algılandı, isim `codexgram`, alarm e-posta.
87. [02:24:25] Sentry Wizard talimatları (DSN dahil) kopyalanıp Codex'e yapıştırıldı (yaklaşık 6 dk).
88. [02:25:24] "Sentry Logs React Native" dokümanı "copy page" ile verilip "setup sentry logs for my project" dendi (tek satırlık değişiklik). Log seviyeleri: debug, info, warn, error, fatal (ör. DB bağlantısının kopması).
89. [02:27:08] **Sentry native modül olduğu için rebuild gerekiyor:** `npx expo run:ios`.
90. [02:28:56] "create me a sentry test page under the settings and create some errors and logs when we press those buttons" dendi. Issues ekranında cihaz, OS ve kullanıcı bilgisiyle hatalar göründü; Explore > Logs da kontrol edildi.
91. [02:31:17] Sentry Tracing (performans) ödev olarak bırakıldı.
92. [02:31:17] "Sentry React Native feedback" dokümanı verilip "under the settings screen, add a nice feedback button with sentry" dendi (yaklaşık 2 dk). Gönderilen geri bildirim Issues > User Feedback'te göründü.
93. [02:33:18] Settings'teki privacy ve terms satırları deploy edilen URL'lere bağlandı. Safari yerine "a sheet would come from the bottom" ile uygulama içi tarayıcı sheet'i kullanıldı.
94. [02:35:01] Commit yapıldı. Kalan iş: yeni tasarım referanslarıyla UI'ı cilalamak.

## Kullandığı prompt'lar (varsa aynen)
Not: Plan Mode, Design ve Legal prompt'larının tam metni videoda okunmuyor; açıklamadaki linklerde (https://dub.sh/plan-mode, https://dub.sh/design-prompts, https://dub.sh/legal-prompts) ve kaynak kodda (https://dub.sh/code10) bulunuyor. Aşağıdakiler transkriptte söylendiği haliyle aktarıldı.

Plan Mode prompt'undan sonra yazılan proje tarifi (eğitmenin özeti) [00:23:31]:
```
I want to build a simple Instagram style social media application using clerk for authentication and convex for the backend and database. Users should be able to sign up and log in with Google or Apple. They should be able to create images or videos as posts, follow and unfollow, like and comment ... sending direct messages ... view and edit their profile. The app should have four main tabs which are going to be home, messages, explore, and profile. We will use native tabs and for styling we want to use NativeWind.
```

Plan dosyası [00:34:50]:
```
For now create a plan MD file with all the details so that later we can implement this step by step.
```

Meta-prompt (ChatGPT) [00:40:46]:
```
Depending on the plan that I have, I want you to generate me a prompt like this that will generate a UI design.
```

Tasarım takip prompt'u [00:41:42]:
```
This is a good start, but I want you to update the logo from the C letter to a camera icon. Similar to Instagram but a little bit different. So do not copy it exactly. Also upscale the design system as a separate image.
```

```
Give me a different variation and upscale that image. Also upscale the design system.
```

```
I want you to generate me the demo image that you're using in the authentication screen.
```
```
I meant this one. Make the background to be transparent so that it matches the white background of the application.
```

Screenshot döngüsü (skill'in özü) [00:49:22]:
```
Build the authentication screen. Here is the workflow: take a screenshot from the simulator and compare it to the actual design. If it is identical, just stop. If it is not, then keep going.
```
```
Put yourself into a loop where you take a screenshot from simulator and compare it to the reference image and keep building until it's identical.
```

Ekran büyütme (ChatGPT) [00:58:16]:
```
Upscale the home screen, profile, and chat screens. Generate each of them as a separate image.
```
```
Upscale the explore screen, the edit profile sheet, comment sheet. Generate every single one of them as a separate image.
```

Native Tabs [01:02:58] ve [01:03:57]:
```
Convert tabs to native tabs. Here is the entire documentation: <Expo Native Tabs sayfasının markdown kopyası>
```
```
Native tabs looks as I wanted, but remove the gray background. So the rest of the application is visible.
```

Tek düzeltme prompt'ları:
```
Fix this overflow.
```
```
Delete the sign out button from edit profile screen.
```
```
Replace the three dots with a settings icon. Later we will build that page.
```

Logo ızgarası [01:20:00]:
```
Generate a grid of nine of icons for this app that I can choose from.
```
```
Give me a completely different style and vibe. Again a grid of nine.
```
```
Upscale option six and make it in the blue theme.
```
```
Remove the background.
```
```
Use this logo.png as my brand logo in this app. Replace the current one with this one.
```

Backend [01:24:24]:
```
Now that we have authentication and most of the UI design ready, read my plan.md so that you know what kind of features we want to build. Start implementing the post creation, likes, comments, and follows. Let me know if I need to set up anything from the clerk or convex dashboard.
```

Varyasyon üretme (ChatGPT) [01:28:00]:
```
This is the current UI that I have for the post detail screen. I want you to give me three different variations of this UI. Try to keep it simple and make sure it is matching with the blue color palette that we have already. Just give me three different variations.
```
```
Give me a grid of three for this UI. Make sure that it is matching with the rest of the design system.
```
```
Upscale the variation three.
```

Seed veri [01:29:56]:
```
Add some seeded data. That would be helpful for me to see the rest of the app with a lot of data.
```

Doğrulama sorusu [01:34:19]:
```
When we send a message does it get saved to the database or all of this data are still hardcoded?
```

EAS [01:40:17]:
```
I'd like to get a development build and install it in my phone. I want to use EAS (Expo Application Services). What I need to do? Give me everything step by step.
```
```
Give me the command to get a development build on the cloud.
```
```
My dev build failed and here are the logs: <log>
```
```
I have uncommitted changes. Do I have to commit them beforehand?
```
```
Now in my terminal I have a QR code. How do I run this?
```

Skill üretme [01:56:09]:
```
I don't want to type this every single time. Can you please create me a skill? Put it under the agents folder as well as the claude folder.
```

Settings tasarımı (ChatGPT) [01:58:35]:
```
I want you to give me a grid of three for setting screen. (privacy policy link, terms of services, delete account, sign out and some of the other things you would need in a social media application)
```
```
Build me this exact UI.
```
```
Also bring back the native tabs. Remember we always want to use native tabs.
```

Yasal sayfalar [02:08:16]:
```
<legal prompt: read my entire codebase and depending on my project create me a terms of services page>
Put it all under the legal folder and actually build me a landing page as well. Make sure it looks pretty good.
```
```
In landing page use these images which are demo one, two, and three.
```
```
Also add a support page. Please keep it simple and super quick to build. Don't test, only build.
```
```
Deploy the legal folder to Cloudflare pages. Use Wrangler. I already have the setup and login.
```

Sentry [02:28:56]:
```
Setup sentry logs for my project. <Sentry Logs React Native doküman sayfası>
```
```
Create me a sentry test page under the settings and create some errors and logs when we press those buttons.
```
```
Under the settings screen, add a nice feedback button with sentry. <Sentry RN feedback doküman sayfası>
```
```
When we press privacy policy and terms of services link it to the actual deployed URLs.
```
```
Can you open this up within the app? Like a sheet would come from the bottom and we would see the website.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Önce görsel tasarım, sonra kod:** Tam uygulama görseli, ayrı **design system görseli** (renk, font, bileşen) ve her ekranın **ayrı büyütülmüş referans PNG'si** `design/` klasöründe tutuluyor. Agent renkleri ve bileşenleri bu görsellerden okuyor.
- **Meta-prompt:** Kanıtlanmış bir tasarım prompt'u ve `plan.md` ile "bana bunun gibi bir prompt yaz" deniyor; çıkan prompt temiz bir sohbette çalıştırılıyor.
- **Renk paletini AI'a bırakmamak:** Planlamada "renkler sonra gelecek tasarım referansından" dendi; böylece varsayılan jenerik mavi kullanılmadı.
- **Varyasyon ızgarası:** "grid of three/nine" ile tek görselde birden çok seçenek alınıp istenen varyasyon büyütülüyor. Varyasyonlar birbirine benziyorsa **design system görseli ekleniyor**.
- **Screenshot ve karşılaştırma döngüsü** (skill olarak): ekran referansla "identical" olana kadar agent kendini düzeltiyor.
- **Native Tabs (liquid glass):** JS tab yerine Expo Router Native Tabs, ayrıca sekme çubuğunun gri arka planı kaldırılarak içeriğin cam efektinin altından görünmesi sağlanıyor. Kural AGENTS.md'ye yazılıyor ki geri dönmesin.
- **Şeffaf arka planlı görseller:** Auth demo görseli ve logo "remove/transparent background" ile beyaz zeminle kaynaşıyor.
- **Seed data ile tasarımı doldurmak:** 24 profil ve gönderiyle explore ve feed gerçekçi görünüyor (boş ekran çirkin durmuyor).
- **Bottom sheet'ler:** edit profile sheet, comment sheet, yasal sayfalar için uygulama içi web sheet (Safari'ye atmıyor).
- **Kısmi uygulama:** Referansın yalnızca beğenilen bölümü ekran görüntüsüyle işaretlenip "only update this part" deniyor; iyi olan header korunuyor.
- **Landing page mockup'ları:** shots.so, iPhone 17, arka plan yok, **gölge yok**, maksimum zoom. Landing page uygulamanın temasını izliyor.
- Uygulamada belirgin bir animasyon kütüphanesi (Reanimated vb.) anılmıyor; görsel kalite büyük ölçüde referans görsel ve native bileşenlerden geliyor. Stories'te basılı tutunca durma ve dokunarak ilerleme etkileşimi var.

## Hatalar ve çözümleri
- [00:14:13] `npx expo start` sonrası `i` ile simülatör açılmadı. Bir süre sonra kendiliğinden düzeldi; öneri hatayı Codex'e sormak.
- [00:35:40] Plan Mode'da `plan.md` yazılamadı. Plan Mode kapatılıp "do it now" denildi.
- [00:43:21] Auth ekranı tasarımı kötüydü. "different variation and upscale" takip mesajıyla çözüldü.
- [00:45:53] Yanlış demo görseli üretildi. Ekran görüntüsüyle doğru görsel işaretlendi ve şeffaf arka plan istendi.
- [00:53:35] Gereksiz "trusted testers" metni elle silindi.
- [01:02:01] Döngüye rağmen home ekranında overflow kaldı. Ekran görüntüsü ve tek düzeltme prompt'uyla giderildi.
- [01:02:58] Görselin siyah arka planı vardı (sahte veri, önemsiz bırakıldı).
- [01:03:57] Native tabs altında gri arka plan vardı. "remove the gray background" ile düzeldi.
- [01:06:23] Edit profile'da sign out vardı (tasarımdan geliyordu). Kaldırıldı, settings'e taşındı.
- [01:17:13] Merge sonrası beklenmeyen `.env` değişikliği vardı (Codex secret key eklemişti). Sync edildi; secret'ların repoya girmemesine dikkat edilmeli.
- [01:27:52] Yorum alanı alta yapışıktı. Ekran görüntüsüyle "more spaced out from the bottom" dendi ve 30 sn'de düzeldi.
- [01:31:40] Follow ve unfollow yavaştı (optimistic update yok). "Hızlandırılabilir" diye not düşüldü.
- [01:34:19] Mesajlar DB'de yoktu çünkü demo hesaplar sahte veri. İkinci gerçek hesapla test edildi.
- [01:35:09] ChatGPT'nin 3 varyasyonu birbirinin aynısıydı. Design system görseli eklenince farklılaştı.
- [01:42:40] EAS "invalid version" hatası verdi. Hata Codex'e yapıştırıldı, cache temizlendi ve komut yeniden çalıştırıldı.
- [01:43:33] EAS cloud build başarısız oldu. expo.dev'de ilk başarısız adımın logları kopyalanıp Codex'e verildi, düzeltilip yeniden build alındı.
- [01:46:10] Dev client, development server'ı bulamadı. `npx expo start --tunnel` ve URL'nin elle girilmesiyle çözüldü.
- [01:52:49] Explore'a basınca ikon değişiyordu ve ikon yeni logoyla uyumsuzdu (sonraya bırakıldı).
- [02:01:08] Settings kurulurken Native Tabs JS tabs'a geri döndü. Kural AGENTS.md'ye yazıldı ("always use expo-router native tabs").
- [02:03:57] Hesap silme için Clerk secret key gerekti. Clerk API keys'ten alınıp Convex env'e `CLERK_SECRET_KEY` olarak eklendi.
- [02:27:08] Sentry native modül olduğu için kurulumdan sonra `npx expo run:ios` ile rebuild gerekti.

## Bizim fabrikaya alınacaklar (somut, maddeli)
1. **"Screenshot ve referans karşılaştırma döngüsü" skill'i:** `.claude/skills/screen-match/SKILL.md` gibi bir skill. Adımlar: `design/<ekran>-ref.png` dosyasını oku, ekranı kur, simülatör veya emülatör ekran görüntüsü al (`xcrun simctl io booted screenshot` / `adb exec-out screencap -p`), farkları listele, "identical" olana kadar tekrarla. Her ekran üretiminde zorunlu olsun.
2. **Tasarım aşaması zorunlu çıktıları:** `design/app-design-reference.png`, `design/design-system-reference.png` ve her ekran için `design/<ekran>-ref.png`. Kod aşaması bu dosyalar olmadan başlamasın.
3. **Meta-prompt şablonu:** Hazır bir tasarım prompt'u (https://dub.sh/design-prompts örnekleri) ve müşteri `plan.md`'si verilip "generate me a prompt like this that will generate a UI design" deniyor; çıkan prompt temiz sohbette görsel modele veriliyor. Sonra "upscale X screen as a separate image" ile ekran ekran büyütülüyor.
4. **Varyasyon kuralı:** Beğenilmeyen her ekran için "grid of three ... matching with the design system" isteniyor ve seçilen varyasyon büyütülüyor. Logo ve ikon için "grid of nine". Varyasyonlar benzer çıkarsa design system görseli eklensin.
5. **Planlama prompt'u:** Plan Mode'da "interview me, batch'ler halinde sor" yapısı (https://dub.sh/plan-mode) kullanılsın. Müşteri formundaki sorulara medya limitleri, V1 kapsam dışı (push, ödeme), boş durum mu seed mi, light/dark, yorum davranışı, offline/online-first eklensin. Çıktı `plan.md`.
6. **Renk/palet kuralı:** Planlamada AI'ın palet seçmesi yasak; renkler design system görselinden gelecek.
7. **CLAUDE.md / AGENTS.md kuralları** (geri kaymayı önlemek için):
   - "Tabs için daima `expo-router` Native Tabs (`NativeTabs`) kullan; JS tabs'a dönme. Tab bar arka planı şeffaf kalsın."
   - "Android'in ham `Alert` / pop-up'larını kullanma; onay ve form için bottom sheet / özel modal bileşeni kullan." (Videoda delete onayı için sistem alert'i kullanıldı; bizim şikâyetimiz tam olarak bu, o yüzden kural olarak eklenmeli.)
   - "Harici linkleri Safari/Chrome'a atma; uygulama içi sheet (`expo-web-browser` `openBrowserAsync` page sheet) ile aç."
   - "Görsel asset'ler şeffaf arka planlı PNG olsun."
   - Kurallar AGENTS.md'de, CLAUDE.md'de `@AGENTS.md` import'u.
8. **Prompt disiplini:** Her prompt'ta tek düzeltme, ekran görüntüsü ekli. Kısmi uygulama için "only update this part" ve işaretli ekran görüntüsü.
9. **Doküman besleme:** Expo/Clerk/Sentry doküman sayfaları "Copy page as markdown" ile prompt'a eklensin (Native Tabs, Clerk Expo, Sentry Logs, Feedback, Tracing).
10. **Önce UI sonra backend:** Tüm ekranlar sahte veriyle onaylanır, sonra backend bağlanır. Backend prompt'u "read plan.md ... let me know if I need to set up anything in dashboards" ile biter.
11. **Seed data adımı:** Demo ve mağaza ekran görüntüleri için 20+ sahte kayıt ekleyen seed script'i standart olsun.
12. **App Store red-önleme kontrol listesi** (yayın aşamasına):
    - Google ya da başka bir sosyal giriş varsa **Sign in with Apple** zorunlu.
    - Hesap oluşturma varsa **uygulama içi hesap silme** zorunlu (Clerk için backend'de `CLERK_SECRET_KEY`).
    - Privacy policy, terms of service ve **support URL** canlı bir adreste olmalı ve uygulama içinde settings'ten linklenmeli.
    - Placeholder alanlar (yasal ad, e-posta) doldurulmalı.
13. **Yasal sayfa ve landing page üretimi:** https://dub.sh/legal-prompts ile `legal/` klasörü (index, terms, privacy, support) üretilip Cloudflare Pages'e Wrangler ile deploy edilir (`wrangler pages deploy legal`). Mockup'lar shots.so ile hazırlanır (arka plan yok, gölge yok, maksimum zoom).
14. **Development build standardı:** Expo Go kullanılmaz. `npx expo install expo-dev-client`, `npx expo run:ios|android`, gerçek cihaz için `eas build --profile development --platform ios|android`. Dev server bulunamazsa `--tunnel`. EAS hatasında ilk başarısız adımın logu agent'a verilir.
15. **Sentry standardı:** Wizard, Logs, Feedback widget (settings'te), test sayfası (yalnızca dev build'de). Native modül olduğu için kurulumdan sonra rebuild hatırlatması. Alarm e-postası müşteriye veya ajansa yönlendirilir.
16. **AI code review:** Her feature branch'te PR açılır, CodeRabbit "Prompt for all review comments for AI agents" çıktısı agent'a verilir, ardından merge edilir.
17. **Convex AI files:** Convex kullanılan projelerde `npx convex dev` kurulumunda "AI files / skills" evet işaretlensin. Supabase gibi başka bir backend kullanılıyorsa eşdeğer agent skill'i eklensin.
18. **Eksik kalan, bizim eklememiz gereken:** Videoda Reanimated/animasyon işi yok. Bizim skill'e geçişler, liste animasyonları, haptics ve skeleton loading ile optimistic update kuralları eklenmeli (videodaki "follow yavaş" sorunu bunun örneği).
