# The EASIEST way to build iOS apps with Claude Code (Opus 4.5) — Alex Finn — 18 dk — https://www.youtube.com/watch?v=-ldzZ6D5cwY

## Tek paragraf: ne yapıyor, sonuç ne
Alex Finn, Xcode'da boş bir SwiftUI iOS projesi açıp Claude Desktop içindeki Claude Code'u (Opus 4.5) o klasöre yönlendiriyor ve üç turda bir habit tracker kuruyor: V1 alışkanlık ekle + checkbox + streak; V2 XP/level sistemi (her tik +10 XP, "fireworks" animasyonu, 100 XP = 1 level, üstte XP bar); V3 Liquid Glass sheet içinde emoji + not ile mood tracker. Teknik derinlik düşük, başlangıç seviyesinde; özgün değeri iş akışında: Claude Desktop'un **chat** sekmesini "co-pilot CEO" gibi kullanıp "high retention and engagement" özellik fikri almak, uygulamadan önce "what are a few different ideas" diye seçenek istemek. Sonunda Sign in with Apple, Supabase, Apple IAP ve App Store gönderimini (Product → Archive → Distribute) sadece özet geçiyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Xcode (Mac App Store, ücretsiz; kurulumda iOS platformunu seç).
- Claude Desktop app (içinde Claude Code + chat sekmesi), model Opus 4.5.
- SwiftUI, Liquid Glass (iOS 26 görsel dili — sheet'te otomatik geldi).
- Önerilen ama gösterilmeyen: Sign in with Apple, Supabase (DB + auth), Apple in-app purchase/subscription, Google Nano Banana (app icon + launch screen üretimi).
- Vibe Coding Academy: https://www.skool.com/vibe-coding-academy
- Newsletter: https://www.alexfinn.ai/subscribe
- X: https://x.com/AlexFinnX
- Creator Buddy: https://www.creatorbuddy.io/

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:33] Xcode + Claude Desktop kur.
2. [00:01:00] Xcode → Create New Project → iOS → App. Ad `Habit Tracker iOS`, organization identifier `Alex`, Documents altında yeni `habit tracker` klasörüne kaydet.
3. [00:02:36] Claude Desktop → Code sekmesi → Xcode'un oluşturduğu proje klasörünü (içteki ilk klasör) aç; Opus 4.5 seç.
4. [00:03:12] V1 prompt'u gönder ("specific but simple" — tek seferde her şeyi isteme). Claude Code to-do listesi çıkarıp sırayla işaretliyor.
5. [00:05:00] Xcode'a dön: dosyalar aynı klasörde olduğu için otomatik görünüyor; sağdaki canvas preview'da test: "+" → "meditate" → checkbox → streak.
6. [00:06:38] Claude Desktop sidebar → chat balonu: özellik fikri iste (prompt aşağıda). Öneriler: accountability partners, anonymous habit buddies, gamification, weekly/monthly/perfect-week badges, XP sistemi (temalar/ikonlar açar).
7. [00:07:57] Code sekmesine dön → XP prompt'u. Claude: user progress model oluştur, app entry point güncelle (5 adımlık to-do).
8. [00:10:35] Test: level + XP bar arayüzü, "exercise" ekle, tik → "+10 XP" fireworks, bar doluyor.
9. [00:11:55] Mood tracker için önce fikir iste; seçenekler: (1) simple emoji picker, (2) mood + notes journal. Claude 1'i önerdi, Alex 2'yi seçti.
10. [00:13:40] Test: alt "Mood" bölümü → Liquid Glass sheet, 5 emoji, not alanı, geçmiş kayıtlar. Ardından eski özellikleri (habit + XP) regresyon için yeniden test etti.
11. [00:15:01] Gösterilmeyenler: "implement authorization using Apple sign-in", önce Supabase DB ("this is my first time... walk me through"), ödemeler için Apple'ın sistemi (subscription).
12. [00:15:50] Yayın hazırlığı: proje (parent) → General/Identity: Category `Lifestyle`, Display Name `Habit Tracker Plus`, Bundle Identifier, Version, Build. App icon + launch screen (Nano Banana ile). Signing → Team → Apple Developer (ücretli) hesabı ekle.
13. [00:16:50] Product → Archive → Organizer → Distribute App → formu doldur → Submit (review).

## Kullandığı prompt'lar (varsa aynen)
V1 (açıklamadaki hâli):
```
you are in an iOS app we just created. I want to build a habit tracking app. Users should be able to enter a new habit, then click a checkbox to say they completed it. Next to that will be their current streak for days in a row.
```
Chat'te özellik fikri [00:06:38]:
```
I'm building an iOS app for habit tracking. Right now, you have the ability to add a habit, check it off, and see your daily streak. What are other cool high retention and engagement features we can add?
```
XP [00:07:57]:
```
I want to add XP features. For every habit a user does, they should earn XP points. When they check off a habit, an XP fireworks animation happens and it shows their XP at the top. Every 100 XP gains them a level. They start off at level one.
```
Mood tracker (fikir isteme) [00:11:55]:
```
I want to add a mood tracker where every day we can enter our mood in what are a few different ideas you have for implementing this system.
```
Önerilen yardımcı prompt'lar [00:15:37, 00:16:13]:
```
Hey, I'm ready to ship my app. Walk me through step by step how to do it.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Ödül anına özel animasyon açıkça istendi: "XP fireworks animation" + "+10 XP" + dolan XP progress bar → uygulamaya oyun hissi.
- Native SwiftUI + iOS 26 Liquid Glass sheet'i hazır "profesyonel" görünüm verdi (ek çaba yok).
- Mood girişinde 5 emoji seçici + opsiyonel not — tek dokunuşla giriş.
- Tasarım referansı/ design system verilmedi; görsel kalite tamamen platform varsayılanlarından geliyor (bizim için uyarı: Expo'da bu "bedava güzellik" yok, açıkça kurmak gerekiyor).

## Hatalar ve çözümleri
- Videoda hata yaşanmadı. Tek gözlem: mood kaydı XP vermedi (özellik entegrasyonu eksik; isteğe bağlı eklenebilir dedi).
- Önerisi: her yeni özellikten sonra eski akışları tekrar test et (regresyon).

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Fikir/strateji turu** fabrikaya aşama olarak: kodlamadan önce ayrı bir sohbette "What are other cool high retention and engagement features we can add?" ve her özellik için "what are a few different ideas you have for implementing this" → seçenekler müşteriye sunulabilir.
- **Gamification paketi** (XP, level, streak, badge, perfect week) yeniden kullanılabilir modül olarak: ödül anında konfeti/fireworks (Expo: `react-native-reanimated` + `lottie-react-native` veya `react-native-confetti-cannon`), `expo-haptics` success titreşimi, animasyonlu progress bar.
- Prompt kuralı: "specific but simple" — her turda tek özellik; büyük one-shot yerine artımlı turlar.
- Her özellik turundan sonra regresyon test checklist'i (önceki ekranlar/akışlar).
- Yayın checklist'ine: kategori, display name, bundle id, version/build, app icon + splash (görsel üretim aracıyla), signing team; Expo karşılığı `app.json` (`name`, `ios.bundleIdentifier`, `version`, `ios.buildNumber`, `icon`, `splash`) + `eas build` / `eas submit`.
- Auth/DB/ödeme sırası: önce DB (Supabase), sonra Sign in with Apple, sonra IAP (Expo'da `expo-apple-authentication`, RevenueCat/`react-native-purchases`).
- Liquid Glass benzeri görünüm için Expo'da `expo-blur` / `expo-glass-effect` ve native sheet kullanımı — ham Alert/pop-up yerine.
