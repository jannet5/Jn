# MOBİL UYGULAMA — YouTube planlarının özeti (YouTube-1 oturumu)

## Kapsam ve durum (denetim: 2026-10-14)

**Durum: kısmen tamamlandı.** Erişilebilen her transkript planlandı ve sentezlendi; 33 videonun transkripti YouTube IP engeli (429 / IpBlocked) nedeniyle çekilemedi.

| Kalem | Sayı | Not |
|---|---|---|
| Mobil grup (8 index bölümü, benzersiz video) | 77 | A 28 · B 27 · C 22 |
| Transkript (`transkriptler/<id>.txt`) | 44 | A/B 43 · C 1 |
| Açıklama (`<id>.aciklama.txt`) | 49 | transkripti olmayan bazı videoların açıklaması var; açıklama tam metin değildir |
| Plan, bu klasörde (`planlar/mobil/<id>-*.md`) | 41 | |
| Plan, başka klasörde (web grubu oturumu) | 2 | `Iup1WlUyj9M`, `VeWf0l4ci6Y` |
| A/B video / planlı | 55 / 43 | transkripti olan her A/B videonun planı var |
| Transkript alınamayan | 33 | A/B 12, C 21; liste `arastirma/youtube/index.md` sonunda |

- **Eksik A/B (planı yok, transkript yok):** `qa5jMoJRQL8`, `UMjeSU6C4qU`, `E6Bj5Wxh-50`, `cTiP900ghk0`, `AAmdB1bvmYw`, `Em9EzurouOU`, `xno-O4vAx7Q`, `X7YMMyd2Qnk`, `Cpi--ZrpMns`, `g5d3ok5TbiI`, `KKWS0bSiCT4`, `CwHD6Fg-Mjs`. `qa5jMoJRQL8` için hiçbir yolda altyazı bulunamadı; diğerleri IP engeli.
- **Alternatif tam metin:** repoda bu 33 video için başka bir transkript veya tam metin yok (denetimde tüm `ajans/` tarandı). Bazılarının yalnızca YouTube açıklaması var. `WTRNddbopGQ` (C) aynı kanalın aynı başlıklı videosu `uUUQjJm9zRw` ile benzer görünüyor ve onun planı var; içeriklerin aynı olduğu kanıtlanmadı.
- **Sentezin dayanağı:** bu özet yalnızca yukarıdaki 43 plana dayanır. Transkripti alınamayan videolardan çıkarım yapılmadı.
- Aşağıdaki "En iyi 5 video" ve ilk bölümler ilk 8 plandan yazıldı; sonraki planların kararları en alttaki "Ek" bölümlerinde.

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
10. **Expo'nun resmî skill'leri.** Beto'nun iki videosunda da ilk üretimin "güzel" çıkmasının tek nedeni `npx skills add expo/skills` ile kurulan **build-native-ui** skill'i (haptics, bottom sheet, native his hazır geliyor); yayında **expo-deployment** skill'i. Codesistency'de Clerk AI Skills aynı rolü oynuyor. Skill yoksa çıktı Traversy videosundaki gibi ham `Alert.alert`'li çıkıyor.
11. **Boş durum ve metin kuralları.** Her listede empty state (ikon + metin + CTA); AI metinlerinde em dash yok, emoji sadece başlıkta, kısa başlıklar ("AI gibi" durmasın).

## 3. En iyi 5 video (fabrika için değer sırasıyla)

| # | Video | Neden |
|---|---|---|
| 1 | **BMMcmmnjrM8** Nick Saraev 243 dk → `BMMcmmnjrM8-expo-supabase-full-course.md` | 5 adımlı app design framework + uçtan uca üretim zinciri (ideation → build → design → test → Supabase → security audit ×2 → EAS). Chrome DevTools MCP görsel QA döngüsü, yayın Google Doc'u, security audit prompt'u. |
| 2 | **tpge9xJ0m5U** Codesistency 191 dk → `tpge9xJ0m5U-calai-clone-full-course.md` | Tasarım-önce pipeline (plan → UI prompt → görsel → `design/` → verify loop), Plan Mode interview prompt'u, AGENTS.md düzeni, Clerk/Trigger.dev/Sentry skill'leri, mağaza-uyum checklist'i (Privacy, Delete account, Apple Sign-In), legal sayfa prompt'ları. Kaynak kod ücretsiz. |
| 3 | **4nVoLX2taFg** JS Mastery 223 dk → `4nVoLX2taFg-react-native-expo-recurrly-app-store.md` | Figma → `global.css` design system token'ları, floating tab bar, font pipeline, FlatList mimarisi, Clerk custom auth, PostHog, CodeRabbit çift kontrol, EAS production build + submit adımları en ayrıntılı burada. |
| 4 | **p80OV6kjIO8** Codesistency 213 dk → `p80OV6kjIO8-triply-ai-trip-planner-full-course.md` | Aynı iş akışının en güncel ve en "prompt'u aynen verilmiş" sürümü: 30+ prompt, AGENTS.md kuralları (Native Tabs, "never run the app", verify loop), "grid of nine" tasarım varyasyonu, Inngest webhook/experiments, Sentry AI agent monitoring, legal + landing + Cloudflare. |
| 5 | **M3dO417o7-U** Code with Beto 58 dk → `M3dO417o7-U-expo-testflight-yayin-rehberi.md` | Eski Expo mühendisi; Expo'nun resmî `building native UI` + `Expo deployment` skill'leri, `/plan` + `/effort high`, `npx testflight` → `eas submit` → TestFlight'a kadar gerçek yayın akışı, ücretsiz Figma mağaza SS şablonu. |

Onur listesi: XFmYkJJxsr8 (Beto, 28 dk, `build-native-ui` skill ile tek seferde haptic'li, bottom sheet'li, heat map'li habit tracker: "çirkin çıktı" sorununun en kısa cevabı), XCifkDC0yXA (Traversy, saf RN temelleri: `useFocusEffect`, haptics, storage), mMTyFKqrf9U (DesignCode, Figma auto-layout → responsive kod kuralları). OcqWu_kasck (12 saat) transkripti var, planı sonraki oturuma kaldı.

## 4. Bizim zincir için önerilen mobil üretim adımları

Mevcut `uygulama-fabrikasi` skill'inin 8 aşamasına oturtulmuş hâli; her maddede kaynağı parantezde.

**0. Kurulum skill'i (bir kez):** `npx skills add expo/skills` (build-native-ui + expo-deployment, proje kapsamı), `npm i -g eas-cli`, `npx create-expo-app@latest`, `npx expo install` (asla `npm install`), NativeWind v5 kurulum adımları + `postcss.config.mjs` + `metro.config.js`, `react-native-safe-area-context`, `expo-haptics`, `expo-av`, `react-native-reanimated`, `expo-font`, `expo-dev-client`; Context7 MCP + Chrome DevTools MCP; Clerk/Supabase/Expo skill'leri `.claude/skills` altına. (JS Mastery, Codesistency, Nick)

**1. Fikir → Brief (kapı 1):** Plan Mode "interview prompt" (3-6 soruluk gruplar, "V1 done ne demek?") → `PLAN.md`. Nick'in 5 alanı zorunlu: core function, core loop (ödül: haptik+ses+animasyon), accessory, 5-7 ekran, retention hook. Müşteri brifingi + stack bu prompt'un altında. (Codesistency, Nick)

**2. Tasarım (kapı 2, kod yok):** DESIGN.md token'ları → uicolors.app paleti + font seçimi → UI prompt'u plandan türet (dub.sh/design-prompts) → görsel model ile tüm ekranlar → referans ekran görüntüsü ile "light, minimal, tek vurgu rengi" sadeleştir → `design/*.png` + logo (transparent, dark/light). Müşteri Figma'sı varsa auto-layout / min 360-max 480 kontrol listesi. (Codesistency, Nick, DesignCode)

**3. İskelet:** `global.css` `@theme` token'ları + semantik sınıflar; `theme/tokens.ts`; `src/app/(tabs)/_layout.tsx` floating tab bar veya Native Tabs; `styled(SafeAreaView)`; font gate; Dev Tools ekranı; özel Toast/ConfirmSheet/BottomSheet bileşenleri (Alert yasağı). CLAUDE.md = tek satır import, kurallar AGENTS.md'de. (JS Mastery, Codesistency, Traversy)

**4. Ekran ekran üretim:** her ekran görevi = `design/<ekran>.png` + "build & verify loop" prompt'u (ekran görüntüsü al, karşılaştır, aynı olana kadar). Her ekranda: empty state, `numberOfLines`, alt padding, haptik, mikro-animasyon (Reanimated `withSpring`/`withTiming` hook paketi). Her ekran sonrası commit. (Codesistency, Nick, JS Mastery, DesignCode)

**5. Veri + Auth:** local-first (AsyncStorage/expo-sqlite) → Supabase (automatic RLS) veya Clerk + Neon/Drizzle; Clerk webhook → API route → Trigger.dev; `.env` listesi şablon; `supabase login` ayrı terminalde. (Nick, Codesistency)

**6. Kalite kapısı:** (a) Chrome DevTools MCP ile Claude'un kendi görsel QA turu; (b) Security audit prompt'u ×2 (temiz oturumda); (c) CodeRabbit PR review → çıktıyı Claude'a; (d) gerçek cihazda test (development build); (e) D-008 kuralımız: en düşük API'de emülatör akışı. (Nick, JS Mastery)

**7. Gözlem + mağaza uyumu:** Sentry (replay, logs, Seer) + PostHog; Privacy + Terms canlı URL (profilde link), Delete account (tam silme), Apple Sign-In, izin metinleri; `legal/` HTML + Cloudflare Pages. (Codesistency, JS Mastery)

**8. Yayın:** Expo deployment skill'i + Claude'a yayın rehberi Google Doc'unu verip `app.json` (bundle id, 1024 ikon, splash, adaptive icon, izin metinleri) + `eas.json` ürettir → `eas init` → `npx testflight` / `eas build` → `eas submit` → TestFlight (iç test grubuna kendini ekle) → App Store Connect (6.5" + iPad SS tarayıcıdan Claude'a aldır, inceleme hesabı) → Play Console. (Nick, JS Mastery, Beto)

## 5. Prompt / şablon kaynakları (indirilecekler)

- Yayın rehberi (Nick): https://docs.google.com/document/d/12mIPPoxNnmJnOpDwUXxwmVSFMDG1SVywrQ2ULV1VJKQ
- Security audit prompt'u (Nick): https://docs.google.com/document/d/1m1v59_NLWi_M_9o6pSuayUpz_IRIw1-TjWwbI8qFKzU/edit?tab=t.0
- Plan Mode prompt'u (Codesistency): https://dub.sh/plan-mode · Design prompts: https://dub.sh/design-prompts · Legal prompts: https://dub.sh/legal-prompts · Kaynak kod: https://dub.sh/code8
- JS Mastery kaynakları: https://jsm.dev/nativesub-expo · https://jsm.dev/nativesub-eas · https://jsm.dev/nativesub-clerk · https://jsm.dev/nativesub-posthog · https://jsm.dev/nativesub-coderabbit
- Codesistency ek: https://dub.sh/extra-prompts (verify loop + webhook görevi) · https://dub.sh/diagrams8 · kaynak kod https://dub.sh/code6
- Beto: Expo skills https://expo.dev/exposkills · 5 favori skill https://codewithbeto.dev/blog/my-favorite-claude-skills · Platano şablonu + ücretsiz Figma mağaza SS şablonu https://cwb.sh/platano?r=yt
- Diğer açıklama linkleri her videonun `transkriptler/<id>.aciklama.txt` dosyasında.

## Ek (2026-10-10): yeni planlar
- `OcqWu_kasck-claude-code-bootcamp-3-uygulama-dentify.md` — 12 sa bootcamp; uygulama 1-2 = p80OV6kjIO8 + tpge9xJ0m5U, yeni olan Dentify (klinik: mobil + Next.js panel, Stream chat/video).
- `G9o8eoHzpxc-claude-design-spec-html-6-adim.md` — Peter Yang: design.md → Claude Design prototip → tek `spec.html` (PRD/Design/Tech) → tüm ekranlar → build.
- Zincire eklenecekler:
  - Plan aşamasında yan talimat "DO NOT SET UP ANYTHING YET, JUST CREATE PLAN.MD"; build başlangıcı "spec + design'ı incele, önce belirsizlikleri sor".
  - Spec'te **component library + veri şeması** zorunlu; kod değişince spec/design güncellenir (CLAUDE.md kuralı); plan+tasarım ≥ süre %50.
  - Ekran tasarımında boş/edge durumlar + onboarding baştan; eksik ekranı aynı GPT sohbetinde ilk prompt'a referansla ürettir.
  - Hizmet işletmesi şablonu: `apps/mobile` + `apps/web` monorepo, Next.js API routes backend, personel Clerk dashboard'dan; AI asistan = eğitim + triyaj → randevu.
  - Skill'leri CLI ile kur (Clerk skills, Stream agent skills `getstream init` + `stream-react-native`), sonra Reload Window.
  - Gerçek cihaz: EAS dev build + QR; build öncesi commit şart; OTA yalnız JS değişikliğinde.
  - AGENTS.md'ye görsel üretim maliyet kuralı (1K, 9:16, model); Delete account'ta cascade doğrulaması; Support URL sayfası.

## Ek (2026-10-12): sonradan eklenen planların sentezi — fabrikaya girecek kararlar

**A. Tasarım kalitesi = skill + referans + döngü (en çok tekrar eden bulgu)**
1. **Zorunlu skill seti (project scope, kurulum script'inde):** Expo `building-native-ui` + `expo-deployment` (`npx skills add expo/skills`), Anthropic `frontend-design` (`/plugin marketplace add anthropics/claude-code` → `/plugin install frontend-design@claude-code-plugins`), backend'in kendi skill'i (Clerk / Supabase / InstantDB / Convex), Android için Google'ın resmi Android skill'leri, yayın öncesi App Store Preflight, ikon için App Icon + SnapAI. Kaynak: Beto (sneNR5KnRJU, mvfQGjQurAQ, Ji2Lmh1kolA), Build Great Products (95_NJ-a-CMQ), Tech With Tim (misjUj4Q_ho).
2. **Referans kütüphanesi skill'i (`tasarim-ilham`):** Mobbin'den sektör + 2 farklı dikeyden 6-10 ekran; `references/onboarding|paywall|home|empty-state|bottom-sheet|modal-dialog/`; kural "mimic, not copy". Mobbin MCP ile moodboard → PDF stil rehberi → **müşteri başına brand skill** (ljJuOxTsrtY, nbk0PMS0tos).
3. **Extract → Expand → Merge → Design zinciri** slash command olarak: `design/competitor-analysis.md` → `styles.md` → `design-system.md` (color, typography, spacing, radius, shadow, motion) → `theme.ts` (wiQMsQwWJQE).
4. **Screen-match / verify-loop'u skill yap:** `design/<ekran>-ref.png` oku → kur → `xcrun simctl io booted screenshot` → karşılaştır → tekrarla; ekran prompt'u tek satıra iner: "make sure <ekran> looks like @design/<ekran>-ref.png" (bGALxRWI10A, FegYRu-swbA). **Uyarı:** simülatör screenshot döngüsü context ve krediyi çok yakıyor; ekran başına tur sınırı koy (9V2ZFhLsh9w).
5. **Bileşen varyantı önce, sayfa sonra:** tek bileşenin aynı veriyle 4 varyantı → seç → tüm ekranlara uygula; dev-only `app/design.tsx` ekranında token + Button/Card/Input/Sheet/Toast vitrin, müşteri onayı sonrası "apply to all screens" (ljJuOxTsrtY, misjUj4Q_ho).
6. **"AI işi" görünümünü temizleme:** teslim öncesi Impeccable skill (`quieter`, `distill`) bir tur; CLAUDE.md'de emoji ikon ve mor/mavi gradient yasağı (ljJuOxTsrtY, 95_NJ-a-CMQ). Impeccable linki (github.com/pbakaus/impeccable) doğrulanmadı.
7. **Tasarım kilidi:** görsel prototip (Claude Design / `/design` / Paper) onaylanmadan build yok; her özellik prompt'una sabit ek "Do not change the existing visual design; new screens must reuse the existing design tokens and components" (s4E8iQBUrJA, eoEsVQJruow, L_tpLe8pIn8, 76_h94U4ztQ).
8. **Illüstrasyon hattı:** her ekran için stil+palet+konu prompt'u `design/illustration-prompts.md` → Higgsfield MCP / GPT Image → `assets/illustrations/` → `expo-image` (76_h94U4ztQ).

**B. "Ham Android pop-up" şikâyetinin kesin çözümü**
- `@expo/ui` (SwiftUI / Jetpack Compose) native dialog, picker, switch; Beto'nun planı "Expo UI native components" diye açıkça istiyor (LZFEr9QDIVg, 9V2ZFhLsh9w).
- **Production polish prompt'u** (xfptZcCf484'ten uyarlanacak): loading/skeleton, başarı-hata toast'ı, geçişler, dokunmaya uygun kontroller, haptics; `Alert.alert` yasak.
- Animasyon karar kuralı: basit enter/exit/state için `react-native-ease` `EaseView`; gesture/layout/shared element için Reanimated 4 (oAIqn44Y63c). Hazır kalıplar: `SelectableRow`, `StaggerReveal`, onboarding sonu "congratulations" ekranı.
- Şablona özel animasyonlu tab bar: `TabBar.tsx` + `TabBarButton.tsx`, `onLayout` ile kayan gösterge, `withSpring`, tek `useSharedValue` + `interpolate` (GrLCS5ww030). Codesistency ise Native Tabs (liquid glass) kuralı koyuyor; **karar: iOS'ta Native Tabs, özel marka hissi gereken projede animasyonlu TabBar**.
- Sohbet ekranı kalıpları: "yazıyor" üç noktası, boşken mikrofon / konuşurken dalga animasyonu, linkleri uygulama içinde açma (eoEsVQJruow).

**C. Varsayılan stack'e eklenecekler**
- Simon Grimm 2026 stack'i (LZFEr9QDIVg): `expo-sqlite` + Drizzle, `react-native-mmkv` (AsyncStorage yerine), Zustand (MMKV persist), TanStack Query, `@shopify/flash-list` (uzun listede FlatList yasak), `react-hook-form` + zod; stil için tek seçim sabitlenir (Uniwind / NativeWind veya Unistyles).
- Backend alternatifleri: Supabase (RLS), Convex (Codex kurslarında Instagram klonu), InstantDB (Beto), Sanity Studio = işletmenin kendi verisini girdiği hazır panel (lpeP6BcUlFU).
- **Çok kullanıcılı test zorunlu:** iki test kullanıcısı ile RLS/izolasyon ve paylaşılan durum (koltuk/randevu slotu) kontrolü; tek kullanıcıyla "boş görünen dolu slot" hatası yakalanmaz (lpeP6BcUlFU, s4E8iQBUrJA).

**D. Süreç / ajan kuralları**
- **AGENTS.md tek gerçek kaynak, CLAUDE.md = `@AGENTS.md`**; aynı repo Codex/Claude/Cursor ile çalışır. Bir AI'nın geri aldığı her tasarım kararı anında kurala yazılır ("remember we always want to …") (FegYRu-swbA, ZXkeWiWB4xg).
- Prompt'ta framework + sürüm açık ("React Native + Expo SDK 54"); aşama 1 "only front end with placeholder data", sonra her özellikte "remove placeholder data" (6q8joS_592k, oaYIiJvtJ0g).
- `plans/phase-N.md` ritmi, her büyük değişiklikte commit, bozulunca temiz commit'ten yeni sohbet (nlMyplRL8V8, Ji2Lmh1kolA).
- Kod yazmadan önce ayrı ürün turu: "What are other cool high retention and engagement features we can add?" → gamification modülü (XP, level, streak, fireworks) (-ldzZ6D5cwY).
- Brief formu eksenleri: 6 soru (müşteri, pain, outcome, mechanism, pricing, proof) + **magic moment** (76_h94U4ztQ); Claude Design'ın soru formu (palet vibe, kişilik, grain, varyasyon stratejisi) (L_tpLe8pIn8).
- Codex araçları: `/goal` (bitiş kriterine kadar döngü), teslim öncesi `/review`, `/compact` %70-90'da, worktree ile paralel iş (ZXkeWiWB4xg).
- Her uygulamaya **Developer ekranı**: izin durumu, onboarding reset, dummy data, light/dark geçişi (uUUQjJm9zRw, Nick Saraev ile aynı).
- **Referans proje MCP'si:** Beto kaliteyi "Inkigo'nun onboarding'ini referans al" gibi üretimde kanıtlanmış kendi projelerinden çekiyor → bizim de kendi referans kütüphanemiz (skill veya MCP) olmalı (REEt7nX6x-0).
- Maliyet gözlemi: aynı habit tracker + widget Fable 5 ile ~$50 / 49 dk, Fable 5.1 ile ~$33 / 40 dk (9V2ZFhLsh9w, REEt7nX6x-0); Claude Design'ın ayrı haftalık kotası var, 30 dk'da ~%21-26 bitti (L_tpLe8pIn8).

**E. Yayın ve pazarlama**
- **Mağaza ekran görüntüsü hattı:** metin HTML'de gerçek fontla, ortada gerçek screenshot, AI sadece arka plan → PNG; yazım hatası sıfır (J17j1wiisCs). Alternatif: Beto'nun ücretsiz Figma şablonu.
- Her projede `business-context.md` + koddan üretilen `brand-guideline.html` (renk rolleri, tipografi, bileşenler, voice & tone) (J17j1wiisCs).
- Claude in Chrome ile RevenueCat, Resend DNS, App Store Connect kurulumunu ajan yapabiliyor; **legal sayfaları okumadan yayınlama** (J17j1wiisCs uyarısı).
- iOS widget: Expo SDK 57 dev build + `containerBackground` API (REEt7nX6x-0).

**F. Dikkat: başlığı yanıltıcı / düşük değerli videolar**
- xfptZcCf484 (Mikey) Claude Code değil, sponsorlu Base44; yalnız polish prompt'u değerli. GUgxx6fMiR8 (Tim) web + Hostinger deploy, mobil değil. ZXkeWiWB4xg Codex özellik turu, mobil uygulama yok. uUUQjJm9zRw, eoEsVQJruow, L_tpLe8pIn8 native Swift; kod değil iş akışı alınır.

**G. Son eklenen plan (2026-10-14): u2QqWkMv3Lg, JS Mastery Claude Code kursu (web, mobil değil)**
- Tasarım kalitesi süreçten geliyor: referans site analizi (tipografi, spacing, renk, buton, responsive) → tek global tasarım sistemi (token + temel bileşenler) → ancak sonra ekranlar. Expo karşılığı: `theme/tokens.ts` + primitive bileşenler aşaması.
- CLAUDE.md'de somut, yasaklayıcı estetik kurallar ("avoid dashboard-style UI", "no purple gradients"); "make it beautiful" işe yaramıyor. `build-ui` skill'i: önce mevcut bileşeni ara, yeni renk/stil icat etme, sonunda doğrula. Mobil uyarlaması: `Alert.alert` yasak, kendi Sheet/Dialog, her basılabilir öğede Reanimated basma geri bildirimi.
- Süreç: izin şablonunda `.env*`, keystore, `.p8` için deny, `eas build/submit` için ask; yayın skill'lerine `disable-model-invocation: true`; kodu yazan modelden bağımsız güvenlik taraması (videoda placeholder formun e-posta sızdırması ve boş auth secret yakalandı).
