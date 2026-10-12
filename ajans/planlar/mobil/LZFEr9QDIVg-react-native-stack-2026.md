# Choosing the Right React Native Stack in 2026 — Simon Grimm — 18 dk — https://www.youtube.com/watch?v=LZFEr9QDIVg

## Tek paragraf: ne yapıyor, sonuç ne
Simon Grimm (galaxies.dev), 2026'da sıfırdan bir React Native uygulamasına başlarken kullanılacak tek ve görüş sahibi (opinionated) bir stack öneriyor. Kütüphaneleri karşılaştırmıyor, doğrudan seçim yapıyor. Katmanlar şöyle: çekirdek (Expo + TypeScript + Expo Router + AI'lı IDE), yerel veri (Expo SQLite + Drizzle + MMKV), state (Context → Zustand, server state için TanStack Query), olmazsa olmaz paketler (FlashList, React Hook Form + Zod, Reanimated + Gesture Handler), servisler (EAS, Clerk, Sentry, RevenueCat, CodeRabbit, PostHog), backend (Supabase, InstantDB veya Expo API Routes) ve UI (StyleSheet → Unistyles → Uniwind → Expo UI). Video kod yazmıyor, bir karar haritası veriyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Kurs: React Native Zero to Hero: https://galaxies.dev/missions (ayrıca https://galaxies.dev/missions/zero-to...)
- Çekirdek: Expo, TypeScript, Expo Router, Cursor (alternatifler: VS Code + Copilot, Windsurf, Antigravity)
- Yerel veri: `expo-sqlite`, Drizzle ORM + Drizzle CLI + Drizzle Studio (alternatif: Prisma), `react-native-mmkv`
- State: React Context API, Zustand, TanStack Query (`@tanstack/react-query`)
- Paketler: `@shopify/flash-list` (alternatif: Legend List), `react-hook-form` + `zod`, `react-native-reanimated`, `react-native-gesture-handler`
- Servisler: EAS (Build / Submit / Update / Hosting), Clerk (auth), Sentry (monitoring, session replay), RevenueCat (IAP, paywall editörü, virtual currency), CodeRabbit (AI PR review), PostHog (analytics, feature flag, survey, session replay)
- Backend: Supabase (Postgres), InstantDB, Expo API Routes (EAS Hosting'te çalışır)
- UI: React Native `StyleSheet`, `react-native-unistyles`, Uniwind (Tailwind bağlamaları, Unistyles ile aynı yazardan), `@expo/ui` (SwiftUI ve Jetpack Compose bileşenleri)
(Açıklamada kütüphane linki yok. Yalnızca galaxies.dev linkleri ve sosyal medya hesapları var.)

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:01:25] **Core:** Proje şu komutla açılıyor:
   ```bash
   npx create-expo-app@latest
   ```
   "Expo kısıtlar, eject etmen gerekir" iddiası 2 yıldır geçersiz. İstisna, uygulamaların kabaca %2'si. TypeScript zorunlu. Expo Router ile dosya tabanlı routing, API routes ve RSC geliyor. Bir de AI'lı bir IDE seçiliyor (kendisi Cursor kullanıyor).
2. [00:03:17] **Local data:** Yapılandırılmış veri için Expo SQLite, ham SQL yazmamak için de **Drizzle**: şema bir TS dosyasında tanımlanıyor, SQL Drizzle CLI ile üretiliyor, Drizzle Studio ile cihazdaki DB debug ediliyor. Küçük key-value veriler (intro görüldü mü, seçilen tema) için **react-native-mmkv**: AsyncStorage'dan 30 kat hızlı, hook'lu. Expo Go'da çalışmıyor, dev build gerekiyor.
3. [00:05:21] **State:** Önce Context API (ör. theme context), yeter mi diye bakılıyor. Gerekirse **Zustand**: MMKV ile persist ediliyor. HTTP ve server state için her zaman **TanStack Query**: uygulama `QueryClientProvider` ile sarılıyor, sorgular `queryKey` + `queryFn` ile yazılıyor.
4. [00:07:15] **Must-have (4 paket):** FlashList (2025'te baştan yazıldı, ScrollView ve FlatList yerine), React Hook Form + Zod (tip güvenli form ve hata yönetimi), Reanimated (artık CSS animasyonları da var, UI thread'de koşuyor) ve Gesture Handler (double tap, long press). Bu ikisi worklet'lerle birlikte çalışıyor.
5. [00:09:01] **Services:** EAS Build (bulutta derleme), EAS Submit (mağazaya otomatik gönderim), EAS Update (canlı güncelleme), EAS Hosting. Clerk'in yeni RN bileşenleri var. Sentry en baştan kuruluyor (error tracking, session replay, PR review). RevenueCat: paywall editörü, virtual currency, App Store Connect'e otomatik ürün ekleme, Google Play JSON service account kurulum yardımı. CodeRabbit: her branch/PR'da AI özeti ve zayıf noktaların listesi. PostHog: session replay, product analytics, feature flag, survey.
6. [00:12:16] **Backend:** Supabase (Postgres, Firebase'in ilişkisel karşılığı). SQL dışı ve realtime bir şey gerekirse InstantDB ("Firebase in a better way"). Tam backend gerekmiyorsa **Expo API Routes**: secret'lar (ör. OpenAI key) env dosyasında, sunucu tarafında kalıyor ve EAS Hosting'e deploy ediliyor.
7. [00:14:11] **UI basamakları:** (1) `StyleSheet`: AI araçları bunu çok iyi biliyor, sade iOS/Android uygulamalarına yeter. (2) **Unistyles**: "StyleSheet on steroids", `StyleSheet.create` yerine geçiyor; breakpoints ve variants ekliyor, ek yükü çok küçük. (3) **Uniwind**: en hızlı Tailwind bağlaması, CSS değişkenleri ve temalarla; web/Tailwind geçmişi olanlar için. (4) **Expo UI**: SwiftUI ve Jetpack Compose bileşenlerini sarıyor, tam native his ve bazen performans artışı sağlıyor (bileşen seti henüz eksik).
8. [00:17:29] Kapanış: en yeni parlak pakete koşmak yerine temelden başlayıp ihtiyaç doğdukça bir üst basamağa geçmek.

## Kullandığı prompt'lar (varsa aynen)
Prompt yok. Video yalnızca stack önerisi.

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Animasyon temeli olarak Reanimated + Gesture Handler'ı her projeye başlangıçta kurmayı şart koşuyor. Reanimated'ın yeni **CSS animations/transitions** API'sinin de altını çiziyor.
- "Native his" için **Expo UI** (`@expo/ui`): Android'de Jetpack Compose, iOS'ta SwiftUI bileşenleri. Ham Android pop-up'ı şikâyetimize doğrudan çözüm adayı.
- Tema ve tasarım token'ları için Unistyles (variants, breakpoints) veya Uniwind (CSS variables, themes).
- Liste akıcılığı için FlashList.

## Hatalar ve çözümleri
- "Expo'dan eject etmek gerekir" yanılgısı: güncel değil, Expo'dan native projeye inmek her zaman mümkün.
- MMKV Expo Go'da çalışmıyor. Çözüm: development build (EAS).
- AsyncStorage yavaş. Çözüm: MMKV.
- API anahtarlarının istemciye sızması. Çözüm: Expo API Routes + env.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Fabrikanın **varsayılan stack'i** olarak CLAUDE.md'ye yaz: Expo (`npx create-expo-app@latest`) + TypeScript + Expo Router; `expo-sqlite` + Drizzle; `react-native-mmkv`; Zustand (MMKV persist ile); TanStack Query; `@shopify/flash-list`; `react-hook-form` + `zod`; `react-native-reanimated` + `react-native-gesture-handler`.
- Kural: "FlatList/ScrollView ile uzun liste yazma, FlashList kullan. AsyncStorage kullanma, MMKV kullan."
- Android'deki ham pop-up/alert sorunu için `@expo/ui` (Jetpack Compose / SwiftUI) bileşenlerini değerlendir. Native dialog, picker ve switch için önce Expo UI'yı dene.
- Stil katmanı için tek bir seçim yap ve CLAUDE.md'de sabitle: tasarım token'ları ve varyantlar gerekiyorsa Unistyles, AI'ın Tailwind akıcılığından yararlanmak için Uniwind. Seçimi şablona göm, her projede yeniden tartışma.
- Hizmet şablonu: EAS Build/Submit/Update her projede. Sentry ilk gün. Ödeme alan işletme uygulamalarında RevenueCat (paywall editörü). Analytics için PostHog.
- Backend matrisi: varsayılan Supabase. Sadece gizli anahtar ya da AI çağrısı varsa Expo API Routes + EAS Hosting (sunucusuz).
- Kod kalitesi: her PR'da CodeRabbit (veya Claude) review adımı.
