# Build Your First App with Codex (No Experience Needed) — Code with Beto — 19 dk — https://www.youtube.com/watch?v=Ji2Lmh1kolA

## Tek paragraf: ne yapıyor, sonuç ne
Eski Expo ekibinden Beto, Codex masaüstü uygulamasıyla sıfırdan bir habit tracker (Expo SDK 55) kurup iOS ve Android'de Expo Go ile çalıştırıyor. Asıl öğrettiği 5 adımlık iş akışı: Codex kur → Expo Go (SDK 55 beta: iOS TestFlight, Android expo.dev/go APK) → bun kur → projeyi AI'a değil terminal komutuyla oluştur (token israfı olmasın) → Expo plugin'inden **"Building native UI" skill**'ini aç → plan mode ile planla, veri modelini kodlamadan önce sabitle → küçük, odaklı prompt'larla ilerle. Sonuç: SQLite ile lokal kayıt, emoji+renk seçimli habit, iki tab (home/settings), light/dark mode uyumlu, Android'e özel padding düzeltmeli bir uygulama. Kısa ama "native UI skill + veri modeli önce" vurgusu bizim için değerli.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Codex desktop (model GPT 5.5, reasoning High), plan mode `/plan`
- Expo SDK 55 (Expo Go beta: TestFlight; Android: https://expo.dev/go → SDK 55 APK). App Store'daki Expo Go o an 54.
- bun (paket yöneticisi), `bun create expo` şablonu (SDK sürümü komutta: 55, gerekirse 54/56)
- **Expo agent skills** (Codex Plugins → "Expo" → yalnızca "Building native UI" açık, diğerleri kapalı)
- `expo-sqlite` (lokal DB)
- Beto'nun favori Claude skill'leri: https://codewithbeto.dev/blog/my-favorite-claude-skills
- Companion Guide PDF: https://cwb.lemonsqueezy.com/checkout/buy/fef9b539-1e69-46a6-83fd-3f78ae90f11e
- Kitap "From Idea to App Store" (App Store'a yayın, ikon, screenshot, izinler, ret mailleri, agent skills bölümü): https://cwb.sh/book?r=yt-codex
- AquaVoice (speech-to-text): https://cwb.sh/aqua
- İlgili video (zaten planı var: `sneNR5KnRJU-expo-instantdb-native-ui-skill.md`): https://youtu.be/sneNR5KnRJU
- Platano — AI app template: https://cwb.sh/platano?r=yt ; React Native Course: https://cwb.sh/rn?r=yt ; Quick Push (push test, macOS): https://apple.co/4tvT4wF ; GitHub: https://github.com/sponsors/betomoedano

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:01:25] Codex desktop kur.
2. [00:01:42] Expo Go SDK 55: iPhone'da TestFlight → beta linki → Expo Go (SDK 55) install. Android: telefonda expo.dev/go → SDK 55 → APK indir/kur → Settings'te client version 55 doğrula.
3. [00:04:15] bun kur (Mac: curl komutu, Windows: PowerShell komutu) — Codex'in sağ üst panelinden Terminal açıp yapıştır; `bun --version` ile doğrula.
4. [00:05:39] Codex → Add new project → Use an existing folder (`~/Desktop/apps`) → terminal → dokümandaki `bun create expo` (template SDK 55) komutu → app name `habit-tracker-codex`. Gerekçe: "prompting Codex to create this project ... is just a waste of expensive tokens."
5. [00:07:57] Çalışma klasörünü yeni projeye değiştir → yeni terminal → `npx expo start` → QR'ı kamera ile tara → Expo Go. Expo Go artık Expo hesabıyla giriş istiyor → Profile'dan sign in → tekrar tara. Light/dark mode cihazda çalışıyor. Yenileme: Expo Go ayar butonu → Reload (ya da salla).
6. [00:10:08] Codex sol menü → Plugins → "Expo" → sadece **Building native UI** açık → Add to Codex → Install.
7. [00:10:53] Model GPT 5.5, reasoning High → `/plan` → plan prompt'u (aşağıda).
8. [00:12:45] Codex'in soruları: habit progress V1 = daily check-in (recommended), yönetim = CRUD, platform = mobile first.
9. [00:13:25] Planı incele: özellikle **data model** (habit: title, description, emoji, color, createdAt, updatedAt; habit entries; settings key/value — haptics, hide completed). "Değişirse DB + UI + refactor gerekir, ilk prompt'ta netleştir."
10. [00:14:44] Onay → komut izinleri "Yes, don't ask again" → kodlama. Kırmızı hata ekranı → dismiss → reload.
11. [00:15:21] Habit eklenemiyor ("Codex is starting to trip") → **yeni chat**, plansız doğrudan prompt: sağ üst + butonu, input, emoji seçici.
12. [00:16:14] Android'de Expo Go'nun QR tarama butonu ile test; toggle'lar platforma göre farklı görünüyor (native).
13. [00:16:52] Habit kaydet → görünmüyor → Settings'te "Hide completed" kapalıyken görünüyor (hata değil ayar).
14. [00:17:50] Android'de header ikonu eksik + padding → platforma özel prompt; iOS aynen kaldı. Dark mode tam uyumlu.

## Kullandığı prompt'lar (varsa aynen)
Plan mode [00:11:18]:
```
Let's create a plan to build a habit tracker application. I care about a minimalistic UI at the moment. Use the built-native UI export skill when building the UI. For each habit tracker, I care about having a title, description, and an emoji, and a color for each habits that we want to track. I want to have two tabs, one for my home screen, one for my settings, where I can toggle settings uh for the application. I'm not exactly sure at the time which settings I want to have, but definitely we need the settings screen.
All the habits and the progress in the application, I want it to save locally using an SQLite database, so that we don't lose any progress.
```
Yeni chat, doğrudan [00:15:21]:
```
add a way to create new habits. Add a button at the top right plus icon with an input and a way to select an emoji and all the entries for a habits.
```
Platforma özel [00:18:13]:
```
make sure that Android has a nice padding top above the header. Leave iOS as it is.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Tek gerçek kaldıraç: Expo'nun resmi **Building native UI** skill'i, plan prompt'unda adıyla çağrılıyor ("Use the building native UI expo skill"). Sonuç: native toggle'lar (iOS/Android'de farklı görünen gerçek native kontroller), native header + sağ üst + butonu, otomatik light/dark uyumu.
- "Minimalistic UI" + her habit'e emoji ve renk → kişilik veren ucuz detay.
- Platforma özel ince ayar: Android padding/ikon, iOS'a dokunmadan.
- Settings'te haptics toggle (data modelde) — haptic geri bildirim fikri.

## Hatalar ve çözümleri
- App Store'daki Expo Go SDK 54, proje 55 → iOS TestFlight beta / Android APK; ya da komutta 54 kullan [00:02:00].
- Expo Go "giriş yap" ekranı → Expo hesabıyla sign in [00:09:26].
- Değişiklik görünmüyor → Expo Go Reload / salla [00:10:01].
- Kırmızı hata ekranı (kod değişirken) → dismiss + reload [00:15:21].
- Ajan "trip" ediyor, özellik çalışmıyor → yeni chat açıp temiz, tek odaklı prompt [00:15:45].
- Android'de header ikonu eksik / padding yok → platform-specific prompt [00:18:13].

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Fabrika şablonuna Expo resmi agent skill'leri kurulu gelsin; özellikle **building-native-ui** her UI prompt'unda adıyla çağrılsın (CLAUDE.md: "UI işlerinde building-native-ui skill'ini kullan"). Claude Code için Expo skills: `sneNR5KnRJU` planındaki kurulumla aynı.
- Proje oluşturmayı ajana yaptırma: sabit komut (`bun create expo` / `npx create-expo-app` + SDK sürümü sabit) script'te.
- Aşama 1 çıktısı = **veri modeli onayı**: entity/alan listesi müşteriye/operatöre gösterilip kilitlenmeden kod yok.
- Plan mode → sorulara cevap → onay → sonra "her özellik yeni chat" kuralı (bağlam şişince ajan bozuluyor).
- Cihaz testi checklist'i: iOS + Android ikisinde, light + dark ikisinde; Android'de header/ikon/padding ayrı kontrol.
- SDK sürüm uyumu: Expo Go'nun mağaza sürümünü kontrol et; uyumsuzsa development build (EAS) veya TestFlight Expo Go.
- Settings ekranı varsayılanı: haptics toggle (`expo-haptics`), tema; lokal kalıcılık için `expo-sqlite`.
- Yayın tarafı için Beto'nun kitabı (ikon, screenshot, izin metinleri, Apple ret mailleri) referans olarak değerlendirilsin.
