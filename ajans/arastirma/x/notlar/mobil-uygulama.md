# Mobil Uygulama Üretimi — X.com bulguları

Kapsam: Claude Code / Codex / Cursor / Rork ile Expo (React Native) app üretimi, yayınlama, para kazanma. Prompt metinleri `prompts.md`'de.

## 1. Standart stack (herkes aynı şeyi söylüyor)

| Katman | Seçim | Kaynak |
|---|---|---|
| Framework | **Expo** (Expo Router, New Architecture) | Carl Vellotti, Evan Bacon, Expo, Rork |
| Styling | **NativeWind** (Tailwind for RN) — Claude Tailwind'i zaten bilir, app güzel çıkar | Vellotti |
| Canlı test | **Expo Go** + QR (iPhone kamerasıyla okut) | Vellotti, Yarchi, codex-phone-lab |
| Backend | **Supabase** (auth/db/storage, free tier) veya **InstantDB** (realtime + auth, `npx create-instant-app -b expo`) | Dikshit Jain, yazins |
| Ödeme | **RevenueCat** (IAP/abonelik; virtual currency ile kredi sistemi) | Dikshit, Rork 1.5, Ege Beşe |
| Build/yayın | **EAS Build** → App Store Connect / TestFlight / Play; `npx testflight`, `eas build -p android -s` | Vellotti, Bacon CLAUDE.md |
| Diğer | PostHog (analytics), Sentry (crash), Resend (email), Cloudflare (domain/hosting), MascoFast (animasyonlu maskot $15) | Dikshit |
| Dokümantasyon | **context7 MCP**: `claude mcp add context7 -- npx -y @upstash/context7-mcp@latest` | yazins |
| Agent bilgisi | **expo/skills** (resmi): `/plugin install expo@claude-plugins-official` veya `npx skills@latest add expo/skills --skill '*'` | Bacon, Expo |

Aylık maliyet gerçekçi: Rork/Claude $20 + Supabase ~$30 + inference + Apple Developer $99/yıl (Rork CEO). "$1.100 ile başladı" (George, Wrestle AI).

## 2. Evan Bacon'ın Expo CLAUDE.md'si (görselden aynen)

Kaynak: https://x.com/Baconbrix/status/1947698636869013671 (1.1k beğeni)
```
## Architecture

- **Website:** Expo Router website with Tailwind.
- **Native app:** Expo Router app with CNG.
- **Backend:** Expo API routes WinterTC-compliant. Routes are in `src/app/api/` directory. API routes use `+api.ts` suffix (`chat+api.ts`).
- **Secrets:** Use .env files and API routes for secret management. Never use `EXPO_PUBLIC_` prefix for sensitive data.

## Code Style

- Use TypeScript whenever possible.
- Use kebab-case for all file names. Avoid capital letters.
- Use `@/` path aliases for imports.
- Use root src directory.

## CLI

- Install packages: npx expo install
- Ensure the rules of React are enforced: npx expo lint
- Create native modules: npx create-expo-module --local
- Deploy iOS: npx testflight
- Deploy Android: eas build -p android -s
- Deploy web and server: npx expo export -p web && eas deploy
```
Bacon'ın 3 Cursor kuralı: ScrollView çoğu durumda yeter, FlatList sadece recycle için · event yakalamak için `{ display: contents }` · RN legacy shadow yerine CSS `boxShadow`.

## 3. Expo'nun resmi skill seti (github.com/expo/skills)

Kurulum: `claude plugin install expo@claude-plugins-official` (Claude Code) · `codex plugin add expo@openai-curated` (Codex) · `npx skills@latest add expo/skills --skill '*'` (Cursor vb.).
Skill'ler: `expo-overview` (giriş), `expo-project-structure`, `expo-router`, **`expo-animation`** (Reanimated + Gesture Handler + haptics), **`expo-native-ui`**, **`expo-design-system`** (token theme + drift audit), `expo-ui` (@expo/ui, SwiftUI/Compose), `expo-data-fetching`, `expo-dom`, `expo-web-to-native` (Next/Vite → native), `expo-module`, `expo-brownfield`, `expo-dev-client`, `expo-examples`, `expo-app-clip`, `expo-upgrade`; ücretli EAS: `eas-app-stores` (build+submit, TestFlight, metadata), `eas-hosting`, EAS Update/Workflows.
Örnek istekler: "Build a native-feeling Expo Router screen with tabs, modals, and animations." · "Create an EAS workflow that builds previews on pull requests." · "Help me upgrade this app to the latest Expo SDK."
Beto (eski Expo DSE): en güvendiği UI skill'i **Build Native UI**: `npx skills add https://github.com/expo/skills --skill building-native-ui` (liquid glass, Expo Symbols, iOS 26 hissi).

## 4. İş akışları

### 4a. Claude Code Web + EAS Workflows (Evan Bacon)
1. `bunx create expo` → GitHub'a push. 2. Expo hesabını/projeyi GitHub'a bağla. 3. `.eas/workflows/preview.yml` → her commit'te EAS Update yayınlar, Expo Go'da açılır link üretir (örnek: github.com/EvanBacon/expo-rsc-movies). 4. Claude app → Code → GitHub bağla → oturumu repoya bağla → prompt'la. Sonuç: telefondan prompt at, commit otomatik preview olur, link müşteriyle paylaşılır. **Bizim için:** müşteri önizleme hattı tam bu.

### 4b. "1 günde app" (yazins)
Claude Code + frontend-design plugin ("use the frontend plugin" de) + Expo + InstantDB; create-instant-app → context7 → plan mode'da spec → iterasyon.

### 4c. Sıfır kodla 2 günde App Store (Yarchi, article)
Fikir: tek mekanik, tek cümle; önce normal Claude'da fikri keskinleştir. İlk prompt (prompts.md #5). Klasör OneDrive/iCloud/Dropbox dışında olsun. 15 dk sonra Expo Go'da iskelet. Tasarım: **readdy** (prompt'u Claude'a yazdır) → ekran görüntülerini klasöre → "Match it". Ses: mixkit. Claude kendiliğinden splash, animasyon, stats, dark theme, çoklu dil önerdi. Apple zorunlulukları: privacy policy URL + iletişim (Netlify), onboarding, settings, loading. Developer Program: Individual, ~3 saat-1 gün onay, $99. App Store Connect: Bundle ID = app.json; subtitle 30, promo 170, açıklama 4000, keywords 100; 6.9" ekran görüntüsü zorunlu. Toplam ~$119 + 2 gün.

### 4d. Codex tarafı
- `npx --yes codex-phone-lab`: Codex skill'i Expo app oluşturur, preview server + QR, canlı refresh (Kappaemmedev).
- Codex "Build iOS apps" plugin: in-app browser'da simülatör, SwiftUI preview, hot reload (OpenAI Devs, dkundel).
- `codex app-server`: Codex'i kendi ürününe gömmek için API (gdb, kagigz).

### 4e. Rork (no-code alternatifi)
Claude Code/Opus tabanlı; React Native ($20 Pro) veya native Swift (Max; Screen Time, Siri, widget, Face ID → iyi "gotcha moment"lar). 3 tıkla App Review, ekran görüntüsü/metadata üretir, Apple kurallarına karşı tarar. Matt Shumer'ın spec prompt'u Rork için yazıldı (prompts.md #1). Hina: mobilde en iyi no-code çıktı Rork.

## 5. Yayın & para kazanma (DamiDefi article, 10 app)

- Apple = ödeme işlemcisi + uyum otoritesi + ev sahibi. %30; **Small Business Program** (<$1M) → %15, **elle başvur** (Step 7, geriye dönük).
- Yayından ÖNCE: Developer Program aktif, 2FA, **Agreements/Tax/Banking** tamamlanmış, Paid Applications Agreement imzalı. Yoksa satış olur ama para tutulur.
- 3 model: paid app (yüksek sürtünme), **IAP/abonelik** (en çok kazandıran, StoreKit 2), harici ödeme (**her zaman red**; "web'de daha ucuz" cümlesi bile red sebebi; fiziksel ürün/hizmet hariç).
- Ödeme: ay kapanışından 45 gün sonra → ilk para 6-10 hafta sonra.
- Trial: 7 gün varsayılan; tam erişim; bitmeden 24 saat önce push (Apple göndermez, sen kur).
- Abonelik state'leri: active, grace period (60 güne kadar billing retry), lapsed, cancelled (dönem sonuna kadar erişim), expired. Claude'a "handle the full subscription lifecycle" de.
- StoreKit 2 prompt yapısı: products array = App Store Connect product ID'leri, purchase fonksiyonu, **app launch'ta transaction listener** (reinstall'da restore; en sık hata), entitlement check, sandbox.
- Kurulum sırası: 1 banking/tax → 2 model kararı → 3 ASC'de ürünler → 4 StoreKit 2 + sandbox → 5 harici ödeme referansı taraması → 6 submit (reviewer notes'a sandbox notu) → 7 Small Business Program.
- Intro offers, promotional offers (win-back), offer codes: ücretsiz, çoğu kullanmıyor.

## 6. Hangi app'i yapmalı (Rork CEO Daniel Dhawan, article)

- Kendi problemin; nişe gir (wrestling app: "kaç güreşçi var ki?" → yeterli).
- **Tarpit fikirler:** AI habit tracker, AI journal, AI meal planner, AI flashcards — klon = sadece pazarlamayla rekabet.
- **Viral videoyu tersine mühendislik:** TikTok'ta her videoda sor: kim izliyor (çok spesifik), problemi ne, bu creator 30 saniyede çözümü nasıl gösterir? → hem fikir hem outreach listesi.
- **Gotcha moment:** 5 saniyede anlaşılan tek özellik (Cal AI: yemeğin fotoğrafı → kalori). Build süresinin yarısı buraya; arayüz billboard gibi.
- Onboarding 10-20 ekran; ilk günden ücret; ladder $100 → $1k → $10k.
- Dağıtım: build in public, UGC/meme sayfaları ($50 meme page), influencer (aynı özelliği 5-10 post, sonra düşüş), paid ads. Aynı ay iki app: önemsenen Wrestle AI 1M view → $17k; klon dating app 1.8M view → $35.
- Prompt sayısı filtredir: herkes 1 prompt atar, 200 atan neredeyse yok.

## 7. App'i büyütme skill'leri (Beto, article)

- `npx skills add eronred/aso-skills --skill aso-audit` — listing 10 faktör skor, keyword gap, rakip tablosu (appeeky verisi).
- `npx skills add eronred/aso-skills --skill apple-search-ads` — keyword research, bid önerisi.
- `npx skills add truongduy2611/app-store-preflight-skills` — red sebebi tarama; `brew install asc` + `asc auth login` ile metadata çeker; 10 app tipi checklist (subscription, health, kids, AI…).
- `npx skills add https://github.com/code-with-beto/skills --skill app-icon` — SnapAI ile ikon (iOS 18/26, Android).
- Claude'u PM yap prompt'u (prompts.md #6): simülatör + RevenueCat + asc → funnel sızıntısı → 3 iterasyon.
- Burak Tahtacı: Cursor'a App Store lokalizasyon scripti → tüm dillere 5 dk, gelir +%200.

## 8. Animasyon kaynakları (mobil)
- Reanimated 4: CSS transitions/animations desteği (Fernando Rojo, Kevin Grajeda); RN 0.85 Shared Animation Backend (Software Mansion).
- Moti (Rojo), React Native Skia, react-native-skottie (Lottie'yi Skia ile), Lottie.
- **text-to-lottie**: `npx skills add diffusionstudio/lottie` → Claude/Codex production Lottie üretir, player'da canlı izlenir; SVG/gerçek veri ver, motion design terimleri kullan.
- HeroUI Native (RN UI lib), @expo/ui (SDK 56, 8 lib yerine 1), nkzw-tech/expo-app-template (Expo 57, Uniwind, bottom-sheet, Legend List, React Compiler).
- Expo blog: sin/cos/exponential decay ile dinamik animasyon.
- Vercel React Native skills (perf + animasyon best practice) — Beto.

## 9. Türkçe örnekler
- Ege Beşe: AI video app; backend yok (AIProxy), RevenueCat virtual currency, fal; Claude Desktop skill'i viral template toplar → JSON → Claude Code skill'i Firebase'e yazar → admin panelde taslak. 50 template/10 dk.
- tonny: 4 saatlik "sıfırdan mobil app" kursu (2M görüntülenme).
- Furkan Kılıç: 19 bölümlük vibe coding app serisi, mağazaya gönderildi.

## Bizim için çıkarımlar
1. Fabrika mobil hattı = Expo + NativeWind + Supabase + RevenueCat + EAS; expo/skills + building-native-ui + frontend-design kurulu CLAUDE.md şablonu (Bacon'ınkini baz al).
2. Müşteri önizleme: GitHub push → EAS Update → Expo Go linki (Bacon 4a).
3. Yayın öncesi kapı: app-store-preflight + DamiDefi'nin 7 adımı + Apple zorunlu ekranları checklist'i.
4. Her app için "gotcha moment" ve 1 cümlelik pitch zorunlu alan olsun (spec prompt'una ekle).
