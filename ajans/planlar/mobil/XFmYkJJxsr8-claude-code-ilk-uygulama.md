# Build Your First App with Claude Code (No Experience Needed) — Code with Beto — 28 dk — https://www.youtube.com/watch?v=XFmYkJJxsr8

## Tek paragraf: ne yapıyor, sonuç ne
Beto (eski Walmart ve Expo "developer success engineer") sıfırdan bir Expo + React Native projesi açıp, Expo'nun resmî `build-native-ui` agent skill'ini kurduktan sonra Claude Code'u **plan mode**'da, referans ekran görüntüsü yapıştırarak ve sesle (Aqua Voice) prompt'layarak siyah-beyaz, yalnızca light mode, SQLite tabanlı, ısı haritalı (heat map) bir **habit tracker** yaptırıyor. Projeyi Claude'a değil kendisi `create-expo-app` ile açıyor (token israfı ve eski sürüm riski), skill sayesinde 3 tab'lı, haptic feedback'li, bottom sheet ile habit ekleyen bir UI tek seferde çıkıyor (~8-10 dk). Ardından Git ile commit alıyor, context'i `/clear` ile sıfırlayıp ikinci bir özellik (ayarlardan 5 renk arasından birincil renk seçimi) ekletiyor. Sonuç: 20 dakikada Expo Go'da çalışan, "pretty nice" görünen bir uygulama; kapanışta sponsor Readdy ile landing page üretiyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code (kurulum `curl` komutu landing page'den) — https://claude.ai/download
- Node.js LTS (v24) — nodejs.org
- `npx create-expo-app` (Expo default template; Expo Router + tabs)
- Expo Go (iOS App Store / Play Store) — fiziksel cihazda geliştirme, Xcode/Android Studio gerekmez
- **Expo Skills** — `npx skills add expo/skills` → `build-native-ui` skill'i — https://github.com/expo/skills
- Cursor (IDE; alternatif VS Code) — dosya ağacı `Shift+Cmd+E`, terminal `Cmd+J`
- Warp terminal — https://cwb.sh/warp
- Aqua Voice (Mac speech-to-text, `fn` iki kez) — https://cwb.sh/aqua
- Git (Cursor'ın Source Control paneli üzerinden stage/commit/discard)
- Claude Code komutları: `/login`, `/plan`, `Shift+Tab` (mod değiştirme), `/context`, `/clear`, `@dosya` referansı, görsel yapıştırma (`Cmd+V`)
- Model: Claude Sonnet 4.6 (videoda görünen)
- Readdy (sponsor, landing page) — https://bit.ly/Readdy3_Beto
- Platano (Beto'nun ücretli hazır şablonu) — https://cwb.sh/platano?r=yt
- Kitap "From Idea to App Store with Claude Code" — https://cwb.sh/book?r=yt-claude-first-app
- React Native kursu — https://cwb.sh/rn?r=yt-claude-first-app
- Bağlantılı videolar: agent skills açıklaması https://youtu.be/nbqqnl3JdR0 , deploy videosu https://youtu.be/qzTZt6mYFF4 , https://youtu.be/i5wd1-QGE1A
- Topluluk: Newsletter https://cwb.sh/newsletter?r=yt , Discord https://cwb.sh/discord

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:01:37] Claude Code kurulumu: claude.ai/download sayfasındaki `curl` komutunu terminale yapıştır. Doğrulama: `claude help` ve `claude -v`.
2. [00:02:32] Proje klasörüne geç: Finder'dan klasörü terminale sürükle, başına `cd` ekle (`cd ~/Desktop/apps`).
3. [00:03:18] Node.js LTS kur (nodejs.org → "Get Node", macOS installer); `node -v` ile doğrula (v24).
4. [00:04:22] `npx create-expo-app` → proje adı `habit-tracker-2026`. **Claude'a yaptırmıyor**: "waste of tokens", eski sürüm kullanabilir; önce sağlam temel, sonra agent. `cd habit-tracker-2026`.
5. [00:05:40] IDE: `cursor .` ile Cursor'da aç. Dosya ağacına bak, hiçbir şeyi değiştirme.
6. [00:07:01] Telefona Expo Go kur.
7. [00:07:30] `npx expo start` → QR kodu telefon kamerasıyla okut → Expo Go'da default template açılır. Server'ı başlatıp durdurmayı kendin öğren, Claude'a yaptırma.
8. [00:08:38] Skill kurulumu (server çalışırken ikinci terminal sekmesi): `npx skills add expo/skills` → listeden boşlukla `build-native-ui` seç → agent olarak Claude Code (birden çok seçilebilir) → kapsam **project** (global da olabilir) → **symlink (recommended)** → proceed. Sonuç: `.claude/skills/...` ve `.agents/skills/...` klasörleri (Cursor da algıladığı için çift; `SKILL.md` içinde kurallar).
9. [00:11:34] Projede `claude` başlat → `/login` → Claude aboneliği ile tarayıcıdan yetkilendir (alternatif: Anthropic Console API ödemesi veya 3. parti).
10. [00:12:11] Kurulumu doğrula: prompt `List my skills in this project` → skill algılanıyor.
11. [00:12:25] **Plan mode**: `/plan` (veya `Shift+Tab` ile mod döngüsü: normal / accept edits / plan). Tek seferde "one-shot" yapmama, her zaman plan.
12. [00:13:02] Referans UI: bir habit tracker dashboard ekran görüntüsünü alıp terminale `Cmd+V` ile yapıştır (görsel prompt'a eklenir).
13. [00:13:47] Aqua Voice ile sesli prompt (aşağıda aynen). Önemli ekler: "ask me questions", "composable", "reutilize components".
14. [00:16:03] Claude kod tabanını keşfediyor, sonra soruyor: ekran/navigasyon yapısı → **Tabs** (önerilen); frekans → **Daily**; ısı haritası aralığı → **son 3 ay**; ekran görüntüsünde gördüğü mood tracking → **atla**. (4. seçenekle serbest metin/sesle açıklama yapılabilir.)
15. [00:17:00] İzin istekleri için "Yes, and don't ask me again" veya `Shift+Tab` → accept edits on.
16. [00:17:26] Plan hazır; planı oku. İçerik: local-first, light mode only, SQLite, ekran görüntüsünden alınan grayscale palet, hook'lar/abstraction'lar.
17. [00:18:37] Plan onaylandıktan sonra context %21 → **önce context'i temizle** (%50 üstü "agent dumb olur"), sonra uygulat. ~10 dk sürüyor.
18. [00:19:05] Uygulama görünmüyorsa telefonu salla → Expo Go dev menüsü → **Reload**. Sonuç: 3 tab, her tab'da haptic, `+` ile bottom sheet, emoji seçimi, habit düzenleme/toggle. Eksik: stats ekranı tamamlama sonrası güncellenmiyor; manuel reload gerekti.
19. [00:20:46] **Git ile kaydet**: Cursor → Source Control → "Stage All Changes" → commit mesajı (AI'a yazdır) → Commit. Agent oluşturdukları: `components/`, `hooks/`, `database/`, `types/`.
20. [00:22:29] Değişiklik testi: `app/(tabs)/index.tsx`'te "Today" → "Hello", `Cmd+S` ile live reload; ikinci commit; istenmeyen değişiklik için Source Control'de **Discard** ikonu.
21. [00:23:20] `/context` → %31. Uzun session'ı sürdürme; `/clear`.
22. [00:24:40] Yeni özellik (bu kez plan mode'suz, doğrudan): ayarlar ekranına hardcoded 5 renkten birincil renk seçimi. `@` ile dosya referansı gösteriliyor (`@index` → `app/(tabs)/index.tsx`). Sonuç: mor seçilince ana ekranda habit rengi değişiyor.
23. [00:26:05] Readdy ile landing page: prompt yaz, Tab ile önerilen genişletmeyi kabul et, yapı seç, ikon üretimi (regenerate/upload), publish → link. İkinci prompt ile hero animasyonu + telefon mockup eklendi.

## Kullandığı prompt'lar (varsa aynen)
Doğrulama [00:12:11]:
```
List my skills in this project
```
Plan mode'daki ana prompt (ekran görüntüsü ekli, sesle dikte) [00:14:00]:
```
I want to build a minimalistic habit tracker application with light mode enabled. Just light. Don't worry about dark mode. And I want to use just white and black colors. For storing the habits, I want everything to be local. Keep it simple for now. No database, no nothing complex. Maybe we can use an SQLite database for saving the data locally. Focus on a beautiful UI. Something important for this application is showing a heat map of the consistency of the user following the habits.

Please ask me questions and any clarification that you need before proceeding. Make sure that the code is going to be composable, we're able to reutilize components and follow those best practices.
```
İkinci özellik (plan mode'suz) [00:24:40]:
```
Can you add an option on the settings screen to allow the user to select the primary color to use for the habits. Right now, we are using black for everything. But let's let the user to choose. For now, let's hardcode five colors that they can select.
```
Readdy landing page [00:26:22]:
```
Build a website for my habit tracker application
```
Readdy iterasyon [00:27:39]:
```
Add animations to the hero and make it pop. Maybe add a phone with a frame.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **`build-native-ui` skill'i** tek başına en büyük fark: native UI pattern'leri, Expo Router, Apple SF Symbols, animasyonlar, visual effects — Beto "this is the result of using skills" diyor; tab'larda haptic feedback, bottom sheet, emoji picker skill sayesinde kendiliğinden geldi.
- **Referans ekran görüntüsü** plan mode'a yapıştırıldı; Claude paleti (grayscale) ve layout'u görselden çıkardı, hatta görseldeki mood tracking'i sorarak teklif etti.
- Prompt'ta net kısıt: "light mode only, white and black only, minimalistic, focus on a beautiful UI" — kapsamı daraltmak tutarlı bir görünüm verdi.
- "Composable / reusable components" talebi: tek dosyalık dev ekran yerine `components/` + `hooks/` yapısı → sonraki iterasyonlar (renk seçimi) kolay ve ucuz.
- Heat map gibi tek bir "imza" özellik belirleyip onu prompt'ta vurgulamak.
- Plan mode'da Claude'un sorduğu seçeneklerde "recommended" olanı (Tabs) seçmek.

## Hatalar ve çözümleri
- Build bitince telefonda değişiklik görünmüyor → telefonu salla → Expo Go dev menüsü → Reload. [00:19:05]
- Stats ekranı habit tamamlayınca güncellenmiyor (ilk turda bırakıldı; "first step is never perfect"). [00:20:18]
- Agent her dosya için izin soruyor → "Yes, don't ask again" ya da `Shift+Tab` accept edits. [00:17:00]
- Context %50 üstünde kalite düşüyor → plan sonrası ve büyük iş sonrası `/clear`; `/context` ile takip. [00:18:37, 00:23:20]
- Agent'ın bir sonraki değişiklikte bir şeyi bozma riski → her iyi noktada Git commit; bozulursa Discard. [00:21:00]
- Projeyi Claude'a kurdurma → eski sürüm/garip yapı riski; `create-expo-app` elle. [00:04:47]
- Skill kurulumunda `bunx` yerine `npx` kullan (bun yoksa). [00:10:02]

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Fabrika zincirine zorunlu adım: `npx skills add expo/skills` ile **`build-native-ui`** (ve ileride `expo-deployment`) skill'ini **project** kapsamında, symlink ile kur; `.claude/skills/` deposuna commit et. "Çirkin, animasyonsuz" şikâyetinin birinci çözümü bu.
- Proje iskeletini her zaman `npx create-expo-app@latest` ile **biz** oluştur, agent'a kurdurma; agent'a sadece var olan iskelet üzerinde çalıştır.
- Her yeni uygulama için **plan mode** zorunlu: `/plan` + referans ekran görüntüsü/moodboard + "ask me questions before proceeding" + "composable, reusable components" cümleleri prompt şablonuna gir.
- Prompt şablonuna tasarım kısıtları: tema (light/dark), palet (2-3 renk), "minimalistic", imza özellik (ör. heat map), local-first depolama (expo-sqlite).
- Plan onayından sonra ve her büyük iş sonrası `/clear`; `/context` %50 eşiğini CLAUDE.md'ye kural olarak yaz.
- Her milestone'da otomatik Git commit (fabrika script'i: `git add -A && git commit -m "..."`); bozulursa `git checkout -- .`.
- Expo Go + `npx expo start` ile fiziksel cihazda doğrulama adımı; değişiklik görünmezse "shake → Reload" talimatı dokümana.
- Müşteri teslimi için landing page adımı (Readdy veya kendi şablonumuz) zincirin sonuna eklenebilir.
- İkinci tur özellikler (renk seçimi vb.) için `@dosya` referansıyla hedefli prompt; küçük işlerde plan mode gerekmez.
