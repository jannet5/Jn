# MOBİL UYGULAMA — YouTube planlarının özeti (YouTube-1 oturumu)

Kaynak: `ajans/planlar/mobil/*.md` (9 plan) + `ajans/arastirma/youtube/transkriptler/` (mobil grubundan 29 transkript, 0 başarısız).
Index'teki 8 mobil bölümden 7'si bu oturumda arandı ve eklendi (`index.md` → "MOBİL UYGULAMA ek sorgular"). Hesap limiti nedeniyle plan yazımı en değerli 9 videoyla sınırlandı; kalan transkriptler (B/C) diskte duruyor, sonraki oturum plan çıkarabilir.

## 1. Herkesin kullandığı ortak stack

| Katman | Herkesin seçimi | Notlar / farklar |
|---|---|---|
| Çerçeve | **Expo + React Native + TypeScript**, Expo Router (dosya tabanlı) | Flutter/Capacitor hep anlatılıp elenir (Nick Saraev). SDK 54-57 arası; Expo Go ile test edilecekse SDK 54 (Codesistency). |
| Stil | **NativeWind** (v4/v5) + `global.css` içinde `@theme` token'ları (JS Mastery, Codesistency) ya da `theme/tokens.ts` + `StyleSheet` (Traversy, DesignCode) | Ortak kural: hiçbir ekranda hard-coded hex yok; tek tema dosyası. |
| Ajan | **Claude Code** (VS Code eklentisi / Antigravity), Plan Mode, bypass permissions ("auto"), sesli dikte (Aqua/mikrofon) | `/init` → CLAUDE.md, `/clear`, `/compact` (200-300k token), Opus (Fable 5 "çok token yakıyor" diye Codesistency tercih etmedi). |
| Doküman besleme | Dokümanı **"Copy for LLM / Copy as Markdown"** ile prompt'a yapıştırma (NativeWind, Native Tabs, Clerk quickstart, Sentry, ImageKit) + **Context7 MCP** | Skill'ler: Clerk AI Skills, Trigger.dev skills, Supabase skill (Claude kendisi yükledi), Expo skills. |
| Auth | **Clerk** (JS Mastery, Codesistency; Google + Apple, e-posta kodu) veya **Supabase Auth** (Nick Saraev) | Mağaza kuralı: Google varsa Apple Sign-In şart. |
| Veritabanı | **Supabase** (Postgres + RLS + Edge Functions) veya **Neon + Drizzle** | Local-first (AsyncStorage / expo-sqlite) → sonra bulut senkronu (Nick). |
| Arka plan / AI | Supabase Edge Function → Claude API (Nick); Trigger.dev task + Realtime hook → OpenAI vision (Codesistency) | API anahtarı asla sohbete, sadece `.env` / secrets. |
| Gözlem | **Sentry** (replay, logs, Seer), **PostHog** (wizard + MCP) | Teslim öncesi "test error butonu → Issues'ta gör → butonu sil" ritüeli. |
| Kalite | **CodeRabbit** PR review → çıktısını Claude'a yapıştır → tekrar push; Security audit prompt'u 2-3 tur (Nick) | Her özellik = `dev` branch + PR + AI commit mesajı. |
| Test | Web (localhost:8081) → iOS simülatör / iPhone Mirroring → **gerçek cihaz** (Expo Go veya `expo-dev-client` development build) | Haptik, push, kamera sadece cihazda doğrulanır. "Gerçek proje için Expo Go yetmez" (Codesistency). |
| Yayın | **EAS CLI**: `eas init` → `eas build --platform ios --profile production` → `eas submit`; App Store Connect (TestFlight, 6.5" + iPad SS, privacy + support URL, inceleme hesabı); Play Console (iş e-postası + Android cihaz doğrulaması) | Privacy/Terms/landing: `legal/` klasörü HTML + Cloudflare Pages/Wrangler (ücretsiz). |

## 2. "Güzel görünmesini" sağlayan ortak teknikler

1. **Tasarım önce, kod sonra.** Görsel model (ChatGPT/GPT Image, Gemini) ile tüm ekranları tek görselde ürettir → "upscale 5 screens" ile parçala → `design/*.png` → her ekran görevi bir PNG ile açılır (Codesistency). Nick: Cal AI / Dribbble ekran görüntüsü ile "önce birebir taklit, sonra farklılaştır".
2. **Build & verify loop.** "Simülatörden ekran görüntüsü al, `design/<x>.png` ile karşılaştır, aynı olana kadar tekrarla." Nick bunu **Chrome DevTools MCP** ile otomatikleştiriyor: Claude her sayfayı gezip spacing/margin/alignment/kontrast listesi çıkarıp sırayla düzeltiyor ("page by page itemize + self loop").
3. **Palet ve tipografi disiplini.** uicolors.app'ten 11 tonluk palet prompt'a yapıştırılır; "monokrom ikon/emoji, palete sadık kal"; premium sans-serif (Plus Jakarta Sans 6 ağırlık, `expo-font` + `SplashScreen.preventAutoHideAsync()` ile FOUT yok); serif denendi, "lame" bulundu; köşe yarıçapı biraz azaltılınca "high-end" hissi; kart outline'ları kaldır, minimal.
4. **Ödül katmanı = core loop.** Her tamamlama/ekleme eyleminde `expo-haptics` + kısa chime (`expo-av`) + Reanimated mikro-animasyon/konfeti; dönüm noktalarında tam ekran kutlama. Nick'in 5 adımlı framework'ü (core function → core loop → accessory → 5-7 ekran limiti → retention hook) animasyonu prompt seviyesinde zorunlu kılıyor.
5. **Native his.** Expo **Native Tabs** (iOS liquid glass) ya da **floating pill tab bar** (`position:absolute`, `useSafeAreaInsets`, label gizli); `Pressable`/`TouchableOpacity` (asla `Button`); alttan kayan sheet + **fade** overlay; `KeyboardAvoidingView`; chip picker'lar; anlamlı izin metinleri `app.json`'da.
6. **Mobil-özel kontrol listesi.** SafeAreaView (`react-native-safe-area-context`, RN'inki yasak), StatusBar stili temaya göre, emoji/ikon `lineHeight` kesilmesi, sabit genişlik yasak, küçük ekranda kart ≤2 satır, `numberOfLines` + `ellipsizeMode`, liste ekranlarında tab bar için alt padding, FlatList'i ScrollView'a sarma yasak.
7. **Ham pop-up yasağı.** Traversy ve Nick'in videolarında bile RN `Alert.alert` kullanılıyor; kullanıcının şikâyet ettiği "Android ham pop-up" tam da bu. Çözüm: inline hata metni + özel toast + kendi `ConfirmSheet` bileşeni (JS Mastery'nin `Modal animationType="slide" transparent` şablonu).
8. **Dev Tools ekranı.** Sahte 30 günlük geçmiş, animasyon durumlarını tetikleme, onboarding reset, push'u toast olarak simüle etme: tasarımı dolu veriyle görmek için (Nick).
9. **Görsel asset pipeline.** GPT Image ile mockup + tek tek arka plansız sprite (grid'den kestirme bozuk çıkar), PNG'lere gradient maske, ImageKit URL transform ile küçük görseller, yayın öncesi sıkıştırma; mockup için shots.so (transparent, shadow none).
10. **Boş durum ve metin kuralları.** Her listede empty state (ikon + metin + CTA); AI metinlerinde em dash yok, emoji sadece başlıkta, kısa başlıklar ("AI gibi" durmasın).

## 3. En iyi 5 video (fabrika için değer sırasıyla)

| # | Video | Neden |
|---|---|---|
| 1 | **BMMcmmnjrM8** Nick Saraev 243 dk → `BMMcmmnjrM8-expo-supabase-full-course.md` | 5 adımlı app design framework + uçtan uca üretim zinciri (ideation → build → design → test → Supabase → security audit ×2 → EAS). Chrome DevTools MCP görsel QA döngüsü, yayın Google Doc'u, security audit prompt'u. |
| 2 | **tpge9xJ0m5U** Codesistency 191 dk → `tpge9xJ0m5U-calai-clone-full-course.md` | Tasarım-önce pipeline (plan → UI prompt → görsel → `design/` → verify loop), Plan Mode interview prompt'u, AGENTS.md düzeni, Clerk/Trigger.dev/Sentry skill'leri, mağaza-uyum checklist'i (Privacy, Delete account, Apple Sign-In), legal sayfa prompt'ları. Kaynak kod ücretsiz. |
| 3 | **4nVoLX2taFg** JS Mastery 223 dk → `4nVoLX2taFg-react-native-expo-recurrly-app-store.md` | Figma → `global.css` design system token'ları, floating tab bar, font pipeline, FlatList mimarisi, Clerk custom auth, PostHog, CodeRabbit çift kontrol, EAS production build + submit adımları en ayrıntılı burada. |
| 4 | **OcqWu_kasck** Codesistency 720 dk → `OcqWu_kasck-*.md` | 3 gerçek uygulama uçtan uca; en geniş hata/çözüm kataloğu (bkz. plan). |
| 5 | **M3dO417o7-U** Code with Beto 58 dk → `M3dO417o7-U-*.md` | Expo ekibinden biri; App Store/Play yayın akışı ve Claude Code'u mobil geliştirici gibi kullanma kuralları. |

Onur listesi: p80OV6kjIO8 (Codesistency 213 dk, güncel Claude Code + Expo akışı), XFmYkJJxsr8 (Beto, 28 dk, en kısa "doğru başlangıç"), XCifkDC0yXA (Traversy, saf RN temelleri: `useFocusEffect`, haptics, storage), mMTyFKqrf9U (DesignCode, Figma auto-layout → responsive kod kuralları).

## 4. Bizim zincir için önerilen mobil üretim adımları

Mevcut `uygulama-fabrikasi` skill'inin 8 aşamasına oturtulmuş hâli; her maddede kaynağı parantezde.

**0. Kurulum skill'i (bir kez):** `npm i -g eas-cli`, `npx create-expo-app@latest`, `npx expo install` (asla `npm install`), NativeWind v5 kurulum adımları + `postcss.config.mjs` + `metro.config.js`, `react-native-safe-area-context`, `expo-haptics`, `expo-av`, `react-native-reanimated`, `expo-font`, `expo-dev-client`; Context7 MCP + Chrome DevTools MCP; Clerk/Supabase/Expo skill'leri `.claude/skills` altına. (JS Mastery, Codesistency, Nick)

**1. Fikir → Brief (kapı 1):** Plan Mode "interview prompt" (3-6 soruluk gruplar, "V1 done ne demek?") → `PLAN.md`. Nick'in 5 alanı zorunlu: core function, core loop (ödül: haptik+ses+animasyon), accessory, 5-7 ekran, retention hook. Müşteri brifingi + stack bu prompt'un altında. (Codesistency, Nick)

**2. Tasarım (kapı 2, kod yok):** DESIGN.md token'ları → uicolors.app paleti + font seçimi → UI prompt'u plandan türet (dub.sh/design-prompts) → görsel model ile tüm ekranlar → referans ekran görüntüsü ile "light, minimal, tek vurgu rengi" sadeleştir → `design/*.png` + logo (transparent, dark/light). Müşteri Figma'sı varsa auto-layout / min 360-max 480 kontrol listesi. (Codesistency, Nick, DesignCode)

**3. İskelet:** `global.css` `@theme` token'ları + semantik sınıflar; `theme/tokens.ts`; `src/app/(tabs)/_layout.tsx` floating tab bar veya Native Tabs; `styled(SafeAreaView)`; font gate; Dev Tools ekranı; özel Toast/ConfirmSheet/BottomSheet bileşenleri (Alert yasağı). CLAUDE.md = tek satır import, kurallar AGENTS.md'de. (JS Mastery, Codesistency, Traversy)

**4. Ekran ekran üretim:** her ekran görevi = `design/<ekran>.png` + "build & verify loop" prompt'u (ekran görüntüsü al, karşılaştır, aynı olana kadar). Her ekranda: empty state, `numberOfLines`, alt padding, haptik, mikro-animasyon (Reanimated `withSpring`/`withTiming` hook paketi). Her ekran sonrası commit. (Codesistency, Nick, JS Mastery, DesignCode)

**5. Veri + Auth:** local-first (AsyncStorage/expo-sqlite) → Supabase (automatic RLS) veya Clerk + Neon/Drizzle; Clerk webhook → API route → Trigger.dev; `.env` listesi şablon; `supabase login` ayrı terminalde. (Nick, Codesistency)

**6. Kalite kapısı:** (a) Chrome DevTools MCP ile Claude'un kendi görsel QA turu; (b) Security audit prompt'u ×2 (temiz oturumda); (c) CodeRabbit PR review → çıktıyı Claude'a; (d) gerçek cihazda test (development build); (e) D-008 kuralımız: en düşük API'de emülatör akışı. (Nick, JS Mastery)

**7. Gözlem + mağaza uyumu:** Sentry (replay, logs, Seer) + PostHog; Privacy + Terms canlı URL (profilde link), Delete account (tam silme), Apple Sign-In, izin metinleri; `legal/` HTML + Cloudflare Pages. (Codesistency, JS Mastery)

**8. Yayın:** Claude'a yayın rehberi Google Doc'unu verip `app.json` (bundle id, 1024 ikon, splash, adaptive icon, izin metinleri) + `eas.json` ürettir → `eas build` → `eas submit` → TestFlight → App Store Connect (6.5" + iPad SS tarayıcıdan Claude'a aldır, inceleme hesabı) → Play Console. (Nick, JS Mastery, Beto)

## 5. Prompt / şablon kaynakları (indirilecekler)

- Yayın rehberi (Nick): https://docs.google.com/document/d/12mIPPoxNnmJnOpDwUXxwmVSFMDG1SVywrQ2ULV1VJKQ
- Security audit prompt'u (Nick): https://docs.google.com/document/d/1m1v59_NLWi_M_9o6pSuayUpz_IRIw1-TjWwbI8qFKzU/edit?tab=t.0
- Plan Mode prompt'u (Codesistency): https://dub.sh/plan-mode · Design prompts: https://dub.sh/design-prompts · Legal prompts: https://dub.sh/legal-prompts · Kaynak kod: https://dub.sh/code8
- JS Mastery kaynakları: https://jsm.dev/nativesub-expo · https://jsm.dev/nativesub-eas · https://jsm.dev/nativesub-clerk · https://jsm.dev/nativesub-posthog · https://jsm.dev/nativesub-coderabbit
- Diğer açıklama linkleri her videonun `transkriptler/<id>.aciklama.txt` dosyasında.
