# CLAUDE CODE BOOTCAMP: Build 3 Real Mobile Apps — Codesistency — 720 dk (içerik ~11 sa 37 dk) — https://www.youtube.com/watch?v=OcqWu_kasck

> Kaynak notu: Transkript (`transkriptler/OcqWu_kasck.txt`, 772 KB) tamamı okunmadı; giriş [00:00–00:03], bölüm başları ve anahtar kelime (skill, design, screenshot, dashboard, EAS, stream, legal, delete account) geçişleri örneklendi. Bölüm 1 (Triply, 00:03–03:36) = `p80OV6kjIO8` planı, Bölüm 2 (kalori takibi, 03:36–06:48) = `tpge9xJ0m5U` planı ile aynı içerik; bu plan yalnızca **Bölüm 3: Dentify diş kliniği uygulaması [06:48:17–11:36:50]** üzerine yeni bilgileri işler. Okunmayan aralıklar için ayrıntı "(transkriptte örneklenmedi)" diye işaretlidir.

## Tek paragraf: ne yapıyor, sonuç ne
Bootcamp üç uygulamayı tek videoda birleştiriyor; yeni olan 3. uygulama **Dentify**: yerel bir diş kliniği için Expo (SDK 57) mobil uygulama + Next.js web dashboard (monorepo `apps/mobile` + `apps/web`). Akış aynı Codesistency iş akışı: Plan Mode interview prompt'u → `PLAN.md` → GPT ile ekran tasarımları + design system görseli → `design/` → "screenshot al, tasarımla karşılaştır, aynı olana kadar döngüde kal" → branch + PR + CodeRabbit → merge. Uygulama: Clerk (Google + Apple) giriş, kişiselleştirme onboarding'i, 4 native tab (Home, Bookings, Chat, Profile; iOS liquid glass), randevu alma (3 adımlı akış), OpenAI mini modelle "eğitim + triyaj → randevuya yönlendirme" yapan AI asistan (streaming), **Stream** ile klinikle gerçek zamanlı chat (reaksiyon, thread, reply, pin, görsel ek, silme) ve **video görüşme**, Sentry logs, ImageKit görseller, Neon Postgres + Drizzle (Next.js API routes = ayrı backend yok), web tarafında personel/dişhekimi dashboard'u + landing + Privacy/Terms + Delete account. Yeni öğretilenler: **EAS development build ile gerçek iPhone'a kurulum**, OTA güncelleme kavramı, **Higgsfield MCP ile VS Code içinden görsel üretimi**, Clerk ve **Stream agent skills** (CLI ile kurulup `/stream` olarak çağrılıyor), "bypass permissions" kurulumu, `/btw` benzeri yan soru ile oturumu durdurmadan talimat. Sonuç: telefonda çalışan klinik uygulaması + web paneli; "push notification, ödeme" planda var ama yapılmadı (meydan okuma olarak bırakıldı) [11:35:43].

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
**Açıklamadaki linkler (aynen):**
- Kaynak kod: Trip planner https://dub.sh/code6 · Calorie Tracker https://dub.sh/code8 · **Dentist App https://dub.sh/code9** · Diyagramlar https://dub.sh/diagrams8 · Skool https://dub.sh/codesistency
- Sentry https://dub.sh/sentryy ($80 kredi) · Trigger https://fandf.co/3Tacp8G · Inngest https://dub.sh/inngest1 · **Stream https://dub.sh/getstream-free** · Neon https://dub.sh/neon-pg · Expo https://dub.sh/expo-yt · CodeRabbit https://dub.sh/coderabbit1 (kod CODESISTENCYCR) · ImageKit https://tinyurl.com/4wy6sy5p · Clerk https://go.clerk.com/codesistency
- Prompt'lar: Plan Mode https://dub.sh/plan-mode · Design https://dub.sh/design-prompts · Extra https://dub.sh/extra-prompts · Legal https://dub.sh/legal-prompts

**Araçlar (Bölüm 3):**
- Claude Code VS Code eklentisi; Plan Mode; auto mode + **bypass permissions** (boş oturumda "bypass permission'ı kur" prompt'u; sonra Cmd+Shift+P → Reload Window) [07:14:00–07:15:02]
- Expo `create-expo-app` (SDK 57) → Claude'un önerisiyle `apps/mobile`'a taşındı + `reset-project` [07:12:00]; Next.js (API routes + Drizzle) `apps/web`, `npm run web` [07:07:02, 09:17:00]
- Pinterest ("<uygulama> mobile app design") referans görseli → ChatGPT (GPT Image) [07:25:10]; **Higgsfield MCP** (ücretli; "Higgsfield claude MCP" dokümanından komut → Claude'a "set this up for me") [07:59:33–08:02:35]; Nano Banana Pro alternatif [07:58:32]
- **Clerk skills** (CLI komutu; hangi skill'lerin kurulacağına Claude karar verdi; `.claude/skills/` + `.agents/`) [08:13:41–08:16:44]
- **Stream**: chat + video SDK; **Stream agent skills** (CLI kur → `getstream login` → `getstream init` → `stream`, `stream-builder`, `stream-docs` + ayrıca `stream-react-native` skill) [10:31:31–10:37:36]
- Expo EAS: development build, internal distribution, QR ile iPhone'a kurulum, `eas.json`; ücretsiz plan 15 build/ay (düşük öncelik kuyruğu) [09:50:00–09:59:32]
- Sentry (logs), Neon + Drizzle (schema + `seed.ts`), ImageKit, OpenAI mini, CodeRabbit, GitHub [07:22:06, 09:20:02, 11:21:02]
- shots.so benzeri mockup aracı (adı bu kesitte geçmiyor; transkriptte örneklenmedi) [11:23:05]

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)

### Modül 1 — Plan [06:54:26–07:24:08]
1. Boş `dental-app` klasörü VS Code'da; Node kurulu; Expo hesabı aç (dev build için gerekli) [06:54:26].
2. "Önce özellik değil, plan": fikir, hedefler, özellikler, sayfalar, kullanıcı akışı, stack [06:55:27]. Plan Mode + dub.sh/plan-mode prompt'u + proje tarifi: Apple/Google giriş, onboarding, randevu, video görüşme, ağız-diş sağlığı AI asistanı, "MVP için ekstra özellik öner"; stack sesle: Clerk, Neon Postgres, ImageKit, Sentry, "getstream.io"; sonuna "planı `PLAN.md`'ye yaz" [06:59:32–07:01:33].
3. Interview cevapları: kullanıcılar = hasta + personel/dişhekimi (video görüşmeyi göstermek için iki uç); AI = "general education and triage to booking"; dashboard = **ayrı web uygulaması** (önerilen); backend = Next.js API routes + Drizzle; uyum = ABD kliniği; bildirim = push; ödeme = klinikte (uygulamada yok); kayıt = açık; AI = OpenAI en ucuz mini; personel hesapları = Clerk dashboard'dan elle; V1 = "klinik için çalışan demo" [07:05:02–07:10:05].
4. Claude kurulum yapmaya başlayınca yan mesaj: "DO NOT SET UP ANYTHING YET, JUST CREATE THE PLAN.MD FILE" (oturumu durdurmadan) [07:11:07]. Plan → yes, auto accept (gerçek projede okuyup düzelt) [07:12:00].
5. Monorepo: "Expo'yu `apps/mobile`'a taşı" + reset project [07:12:00–07:13:00]. GitHub repo → push komutunu Claude'a yapıştırıp ona çalıştırt [07:20:01–07:21:03]. CodeRabbit'e repo erişimi [07:23:07].

### Modül 2 — Tasarım + servisler [07:24:08–07:35:00]
6. Pinterest'ten referans uygulama görseli → dub.sh/design-prompts örnek prompt'u Claude'a: "PLAN.md'ye göre buna benzer prompt ver" (Prompt A) → çıkan prompt + referans görsel GPT'ye; 16:9 [07:26:10–07:28:13].
7. "Generate me a design system for this design…" (Prompt B) → `apps/mobile/design/ui-reference-design.png` + `design-system.png` [07:29:13–07:31:16].
8. Stream app (dev, region Singapur; sonradan US East'e taşındı), Sentry, Neon, Clerk, ImageKit hesapları [07:32:17–07:34:20].

### Modül 3 — Auth, onboarding, ekranlar [07:35–09:12] (kısmen örneklendi)
9. Expo Go yetmez → development build (video/Sentry native) [07:57:31]. Auth ekranı görseli: screenshot (Mac: Ctrl+Shift+Alt+4) → GPT "bu demo görseli ayrı üret" (Prompt C) **veya** Higgsfield MCP ile VS Code içinden (Prompt D); ikisi `design/`'a, hangisi temizse o [07:58:32–08:02:35].
10. Apple girişi hatası → screenshot + "fix it"; Google çalışıyor [08:12:41]. Clerk skills: CLI komutunu Claude'a ver, "bu proje için gerekenleri seç ve kur" (Prompt E) → webhooks, Next.js, Expo, custom UI skill'leri [08:15:43].
11. Onboarding 1-2 / 3-4 tasarım görselleri → loop prompt'u; varyasyon için GPT "grid of six" → "upscale this" [08:41:00]. Randevu: `book-appointment-1/2/3.png` (seçim → devam → onay) + loop [08:42:01].
12. AI asistan ekranı + Higgsfield kuralı AGENTS.md'ye: "1K kalite, 9:16, GPT Image 2" (Prompt F) [08:52:32]. Profil ekranı tasarımda yoktu → aynı GPT sohbetinde "başta verdiğim prompt'a uygun profil ekranı + alt ekranları üret; aynı yeşil" → `profile-screen-design.png` → Prompt G [08:53:32–08:55:32].
13. Branch → PR → CodeRabbit → merge → master'a dön [09:01:09–09:12:44].

### Modül 4 — Web dashboard + backend [09:12–09:46] (kısmen örneklendi)
14. Prompt H (web + API; "dashboard'larda yapmam gereken varsa söyle; env'ler .env'de") → Claude soruları: önce AI assistant API; dashboard "lean" [09:17:00–09:18:01]. OpenAI key (≈$5; alternatif Gemini) [09:18:01–09:19:02].
15. Üretilen kodu gezdi: `src/db/index.ts` (DATABASE_URL yoksa çökme), `schema.ts` (users, patients, medical_histories, dentists, services…), `seed.ts` [09:20:02–09:21:04].
16. Dashboard UI: mobil tasarım görseli ekli "UI'ı mobil tasarıma benzet" → takip: "sidebar ortada yüzüyor, sola sabitle; butonları çalıştır" → dişhekimi profil fotoğrafı web'de de görünsün [09:36:31–09:37:32].

### Modül 5 — Gerçek cihaz, OTA, AI asistan [09:46–10:31]
17. Expo dashboard → proje → Claude'a EAS komutlarını yaptır → development build (internal distribution) → QR → iPhone'a kurulum; `eas.json` oluşur; build fail olursa logu (genelde "install pods") Claude'a yapıştır [09:51:01–09:59:32].
18. OTA kavramı: yalnız JS/UI değişikliği → yeniden mağaza incelemesi yok ("make an over-the-air update"); native modül eklenirse yeni build [10:00:32–10:01:32].
19. AI asistan yanıtları ChatGPT gibi stream ediliyor [10:09:34–10:14:14]. Sentry dashboard [10:23:01] (transkriptte örneklenmedi: ayrıntı).

### Modül 6 — Stream chat + video [10:31–11:12]
20. Stream agent skills quickstart: CLI kur → `getstream login` → `getstream init` (org + app seç; `stream/` klasörü + `.gitignore`) → Claude seçilir → `stream`, `stream-builder`, `stream-docs`; `/stream do we have a react native skill?` → `stream-react-native` kur → Reload Window [10:32:31–10:36:35].
21. `/stream-react-native` + Prompt I (codebase + PLAN'ı tara, video görüşme + mesajlar ekranını şimdi yap) [10:36:35–10:37:36]. API key/secret `apps/web/.env`'e (Claude söyledi) [10:41:30].
22. Yeni EAS build → telefonda fail → sebep: değişiklikler commit edilmemişti → "commit straight to master and rebuild" → QR → çalıştı. Stream app region sorunu → US East'te yeni app, env'leri değiştir [10:41:30–10:43:30].

### Modül 7 — Legal, mockup, Delete account [11:21–11:36]
23. dub.sh/legal-prompts ToS prompt'u + "scan my entire codebase and generate the terms of services… under a folder called `legal`" (Prompt J); sonra Privacy prompt'u [11:22:02–11:23:05]. Mockup görselleri landing'e; cache yüzünden görseller eski kaldı → Claude'a "cache'i temizle" [11:32:02].
24. Support sayfası (Apple dashboard "support URL" ister; `/support` + e-posta) — ödev bırakıldı [11:32:02].
25. Delete account: Prompt K → onay → sign out → Clerk + DB'den sil; **cascade** ile randevu/medikal geçmiş de silinmeli, Claude'a doğrulat [11:33:04–11:34:05]. Master'a commit; kalan PLAN.md maddeleri (push, ödeme vb.) ödev [11:35:05–11:36:28].

## Kullandığı prompt'lar (varsa aynen)
- **Plan Mode:** dub.sh/plan-mode metni (transkriptte okunmuyor) + sesli tarif + "once we are ready to generate the plan just put it under a file called PLAN.md" [07:01:33].
- **Yan talimat:** "DO NOT SET UP ANYTHING YET, JUST CREATE THE PLAN.MD FILE WITH THE CONTEXT" [07:11:07].
- **Bypass:** "hey Claude I want you to set up the bypass permission so that when I select the auto mode you do everything without asking me for any permissions" [07:15:02].
- **A (tasarım prompt'u üret):** "above I have just attached an example prompt that generates a really really beautiful looking UI design. And I want you to give me a similar prompt to that one, but instead of having the content randomly generated, take a look at my PLAN.md and depending on my mobile application requirements, give me that prompt where I can copy and paste to AI so it can generate me a UI design." [07:27:12]
- **B (design system):** "generate me a design system for this design that I can use and follow consistently. Things like typography, components, font family, etc." [07:29:13]
- **C (asset):** "This is the design that I'm going to be copying and I want you to generate this image on the background so that I can use it as the demo image in my mobile application… actually copy the exact image that we have right here." [07:58:32]
- **D (Higgsfield):** "In this image we have this demo dental image. I want you to generate the exact same image. Generate this using Higgsfield MCP and then save this image to my local folder… under the design." [08:01:35]
- **E (Clerk skills):** "this is how you install Clerk skills. Go ahead and run this command and then decide which skills that we would need for this project at the moment and maybe in the future… set it up." [08:15:43]
- **F (maliyet kuralı):** "use 1K as quality, 9:16 and GPT Image 2… also save this rule to AGENTS.md file so that we don't really spend too much credits" [08:52:32]
- **G (UI-only loop):** "This is the profile screen UI design that I want you to build. For now just don't implement any kind of functionality… use hard-coded values and put yourself into a loop where you take a screenshot from simulator and compare it to this design until it is identical. And if you need to generate any images or assets… feel free to use Higgsfield MCP… take a look at the assets that we currently have locally." [08:54:32]
- **Varyasyon:** "generate me a grid of six with different variations" → "upscale this" [08:41:00]
- **H (web):** "…do not implement mobile video calling yet… if there is anything that I need to do from the dashboards like in the Clerk, Neon, Stream etc. just let me know… I have provided some of the environment variables in the env file. So please check them" [09:17:00]
- **I (Stream):** "I want you to build me the video calling feature using Stream and my application is under the mobile folder. I also have the API routes under the web folder. So before you implement anything, just take a look at my codebase, scan it and take a look at my plan file so that you understand the context and then go ahead use the Stream skills and build me a fully functional video calling feature… under the messages screen go ahead [implement messaging]" [10:36:35–10:37:36]
- **J (legal):** dub.sh/legal-prompts + "just go ahead and scan my entire codebase and generate me the terms of services based off of the commands that I just provided above… create these under a folder called legal" [11:22:02–11:23:05]
- **K (delete):** "Under the profile screen, we have a button that says delete account… implement it in the correct way possible. Once user press that ask for a confirmation and if they confirm… sign them out then delete the account from both Clerk and from the database." [11:33:04]

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Pinterest referansı + PLAN.md'den türetilmiş uzun tasarım prompt'u (sunum, stil, 3D görsel varlıklar, palet) → GPT; tasarımı "aynısı" değil "ilham" olarak kullan [07:25:10–07:28:13].
- Ayrı design system görseli (ikon, spacing, core component'ler) her ekran görevinde referans [07:30:14].
- Eksik ekranı **aynı GPT sohbetinde** ilk prompt'a referansla ürettirme ve renk tutarlılığı ısrarı ("aynı yeşil") [08:53:32].
- Hero/demo görsellerini ekran görüntüsünden ayrı asset olarak yeniden üretme (GPT veya Higgsfield MCP) [07:58:32].
- Native Tabs liquid glass; chat'te Stream'in hazır zengin UI'ı (reaksiyon, thread, pin) [06:49:24–06:50:24].
- Web dashboard'u mobil tasarım görselleriyle aynı dile çekme [09:36:31].

## Hatalar ve çözümleri
- Claude plan aşamasında kurulum yapmaya başladı → yan mesajla "yalnız PLAN.md" [07:11:07].
- İzin soruları → bypass permissions + Reload Window [07:15:02].
- Apple Sign-In hatası → screenshot + fix (Google çalışıyordu) [08:12:41].
- Skill'ler slash menüsünde görünmüyor → Reload Window [10:36:35].
- EAS build kurulumdan sonra telefonda bozuk → commit edilmemiş değişiklik; commit + rebuild [10:42:30].
- Stream app bölgesi (Singapur) kaynaklı hata → US East'te yeni app [10:43:30].
- EAS build fail (genelde install pods) → tam logu Claude'a ver [09:59:32].
- Landing'de eski görseller → cache temizlet [11:32:02].
- Web dashboard sidebar ortada, butonlar çalışmıyor → tek takip prompt'u [09:37:32].

## Bizim fabrikaya alınacaklar (somut, maddeli)
1. **Hizmet işletmesi şablonu (klinik/salon/KOBİ):** mobil (hasta) + Next.js web panel (personel) monorepo `apps/mobile` + `apps/web`; backend = Next.js API routes; personel hesapları Clerk dashboard'dan; ödeme "yerinde" ile V1 sadeleşir. Ajansın web müşterilerine "uygulama + panel" ek paketi olarak satılabilir.
2. **AI asistan kapsamı:** "eğitim + triyaj → randevuya yönlendir" (teşhis yok); mini model; streaming yanıt.
3. **Skill'ler CLI ile:** Clerk skills ve Stream agent skills (`getstream login/init`, `stream-react-native`) — kurulumu Claude'a "bu proje için gerekenleri seç" diye devret; kurulumdan sonra Reload Window.
4. **AGENTS.md'ye maliyet kuralı:** görsel üretim varsayılanı (1K, 9:16, model adı) yazılsın; Higgsfield MCP opsiyonel (pahalı), varsayılan GPT/Nano Banana.
5. **Gerçek cihaz kapısı:** EAS development build + internal distribution + QR; build öncesi **commit zorunlu**; OTA yalnız JS değişikliklerinde.
6. **Plan aşamasında yan talimat:** "DO NOT SET UP ANYTHING YET" kalıbı; auto/bypass mod yalnızca git temizken.
7. **Delete account + cascade** kontrol listesi maddesi (Clerk + DB + ilişkili kayıtlar); Support URL sayfası App Store için zorunlu madde.
8. Tasarım: eksik ekranı aynı GPT sohbetinde ürettir; "grid of six" varyasyon; web panel mobil tasarım diliyle.
