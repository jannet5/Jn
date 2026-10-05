# Mobil (Expo / React Native) — Ajan Skill'leri, Kurallar, Şablonlar ve UI Kütüphaneleri

> Araştırma tarihi: 2026-10-05. Amaç: Claude Code / Codex ile üretilen Expo uygulamalarının "çirkin" çıkmasını (animasyonsuz, Android'in ham `Alert.alert` pop-up'ları, jenerik "AI-slop" görünüm) düzeltecek skill, kural dosyası, şablon ve kütüphaneleri bulmak.
> Birebir kopyalar: `ajans/arastirma/github/kopyalar/<repo-adi>/` (her klasörde `KAYNAK.md` var).

## 0. Özet tablo

| Repo/Kaynak | Yıldız | Son güncelleme | Lisans | Ne işe yarar | Nasıl kullanılır | Bizim için değeri (1-5) |
|---|---|---|---|---|---|---|
| [expo/skills](https://github.com/expo/skills) (resmi, 24 skill) | 2.655 | 2026-10-05 | MIT | Expo ekibinin resmi ajan skill'leri. UI için kritik olanlar: `expo-native-ui`, `expo-design-system` (+ **native-slop.md**: 20 "AI-generated görünüm" belirtisi), `expo-ui` (@expo/ui = gerçek SwiftUI/Compose sheet, picker, switch), `expo-animation` (Emil Kowalski ile), `expo-router` (formSheet, NativeTabs), `expo-project-structure`, `expo-data-fetching` (4 durum kuralı) | `claude plugin install expo@claude-plugins-official` veya `npx skills@latest add expo/skills --skill '*'`; Codex: `codex plugin add expo@openai-curated` | **5** |
| [docs.expo.dev/agents/claude](https://docs.expo.dev/agents/claude) + [docs.expo.dev/skills](https://docs.expo.dev/skills/) | — | 2026 | — | Expo'nun resmi Claude Code rehberi: plugin kur, `AGENTS.md` + `.claude/settings.json` commit et, `/mcp` ile Expo hesabına bağlan | Rehberdeki adımlar; skill listesi aynı repo | 4 |
| [Expo MCP](https://docs.expo.dev/eas/ai/mcp) (`mcp.expo.dev`) | — | 2026 | — | Uzak MCP: Expo dokümanı arama (ücretli EAS planı gerekir), `npx expo install`, EAS build, simülatör ekran görüntüsü/otomasyon (yerel `expo-mcp`) | `claude mcp add --transport http expo https://mcp.expo.dev/mcp` → `/mcp`; yerel: `npx expo install expo-mcp --dev` + `EXPO_UNSTABLE_MCP_SERVER=1 npx expo start` | 3 |
| [vercel-labs/agent-skills → react-native-skills](https://github.com/vercel-labs/agent-skills/tree/main/skills/react-native-skills) | 31.956 (monorepo) | 2026-10-05 | MIT | 36+ kural (AGENTS.md tek dosya): native modal/formSheet, native menü (zeego), Pressable, `boxShadow`, `borderCurve`, FlashList, Reanimated. skills.sh'de en çok kurulan mobil skill (~147K) | `npx skills add vercel-labs/agent-skills --skill vercel-react-native-skills` | **5** |
| [emilkowalski/skills](https://github.com/emilkowalski/skills) | 43.581 | 2026-10-05 | MIT | Tasarım-mühendisliği skill'leri: `animate-expo` (expo-animation'ın orijinali), `apple-design` (WWDC akıcı arayüz ilkeleri), `review-animations`, `break-ui` (kötü veriyle UI kırma), `animation-vocabulary`, `pick-ui-library` (web) | `npx skills@latest add emilkowalski/skills` | 4 |
| [callstackincubator/agent-skills](https://github.com/callstackincubator/agent-skills) | 1.664 | 2026-10-05 | MIT | `react-native-best-practices` (performans, FPS, TTI), `react-navigation` (RN7: form-sheet, native tabs, header, safe area referansları) | `npx skills@latest add callstackincubator/agent-skills --skill react-navigation` | 3 |
| [Code-with-Beto/skills](https://github.com/Code-with-Beto/skills) (Beto Moedano) | 140 | 2026-09-19 | MIT | 4 plugin: `cwb-theming` (iOS semantik renk + Android Material You tek `useColors()` hook'u, SDK 56+), `cwb-app-icon` (SnapAI ile iOS 26 Liquid Glass ikon), `cwb-ship` (Platano template, RevenueCat), `cwb-local-build` | `/plugin marketplace add Code-with-Beto/skills` → `/plugin install cwb-theming@cwb-plugins`; Codex: `codex plugin marketplace add Code-with-Beto/skills` | 4 |
| [betomoedano/expo-ui-playground](https://github.com/betomoedano/expo-ui-playground) | 251 | 2026-09-21 | — | @expo/ui (SwiftUI/Compose) bileşen örnekleri; "nasıl native görünür" referans projesi | Klonla, örnek kodu ajana referans ver | 2 |
| [pproenca/dot-skills → expo-react-native-coder](https://github.com/pproenca/dot-skills/tree/master/skills/.experimental/expo-react-native-coder) | 213 | 2026-10-04 | MIT | 50 kural, 10 kategori (route, screen, auth, deep link, ux-haptic, form…) + 7 hazır şablon (tab-layout, list-screen, form-screen). Daha "klasik" (SafeAreaView, KeyboardAvoidingView) | `npx skills add pproenca/dot-skills --skill expo-react-native-coder` | 3 |
| [gluestack/gluestack-ui](https://github.com/gluestack/gluestack-ui) (.agents/skills) | 5.320 | 2026-10-05 | MIT | Repo içi `gluestack-ui-v4` skill'i (7 alt-skill: setup, components, styling, variants, validation) + `creating-reanimated-animations`; v4 CLI copy-paste bileşenler (NativeWind) | `npx gluestack-ui init -y` + `.agents/skills` klasörünü projeye kopyala | 3 |
| [founded-labs/react-native-reusables](https://github.com/founded-labs/react-native-reusables) | 8.675 | 2026-10-05 | MIT | shadcn/ui'nin RN hali (NativeWind/Uniwind + RN Primitives, PortalHost, Dialog/Sheet/Select). Repo içi `.claude/` sadece kendi "Argent" ajanı için; skill yok | `npx @react-native-reusables/cli@latest init` ; ajan skill'i: [hairyf/skills](https://github.com/hairyf/skills) `npx -y skills add hairyf/skills --skill react-native-reusables` | 4 |
| [heroui-inc/heroui-native](https://github.com/heroui-inc/heroui-native) | 3.668 | 2026-10-05 | Apache-2.0 | "Güzel, hızlı, modern" RN UI kit (Uniwind+Tailwind, Reanimated 4). Figma tabanlı sıkı tasarım sistemi; resmi **MCP server** `@heroui/native-mcp` var | `npm i heroui-native` + peer deps; MCP: heroui.com/docs/native/getting-started/mcp-server | 4 |
| [nativewind/nativewind](https://github.com/nativewind/nativewind) | 8.107 | 2026-10-05 | MIT | Tailwind for RN. Repo'da `skills/nativewind-v4-to-v5` migrasyon skill'i; Expo `expo-tailwind-setup` skill'i bunu kurar | `npx skills add expo/skills --skill expo-tailwind-setup` | 3 |
| [tamagui/tamagui](https://github.com/tamagui/tamagui) | 14.212 | 2026-10-05 | MIT | Stil + UI kit + derleyici. AGENTS.md repo-içi bakım sözleşmesi, bizim için kural değil | `npm create tamagui@latest` | 2 |
| [callstack/react-native-paper](https://github.com/callstack/react-native-paper) | 14.465 | 2026-10-05 | MIT | Material Design 3 bileşenleri (Android'de doğru idiyom). AGENTS.md repo-içi | `npx expo install react-native-paper` | 2 |
| [wix/react-native-ui-lib](https://github.com/wix/react-native-ui-lib) | 7.157 | 2026-10-05 | MIT | Kapsamlı UI toolset; New Arch desteği henüz tam değil (RN 0.73) | — | 1 |
| [obytes/react-native-template-obytes](https://github.com/obytes/react-native-template-obytes) | 4.349 | 2026-10-05 | MIT | Expo Router + NativeWind + Zustand + React Query + TanStack Form starter; şablon içinde `claude.md` geliyor | `npx create-obytes-app@latest MyApp` | 3 |
| [infinitered/ignite](https://github.com/infinitered/ignite) | 19.938 | 2026-10-05 | MIT | En eski RN boilerplate; theme/bileşen kütüphanesi, keyboard-controller, edge-to-edge, Reanimated 4. Ajan kural dosyası yok | `npx ignite-cli@latest new MyApp` | 2 |
| [sleekdotdesign/agent-skills](https://github.com/sleekdotdesign/agent-skills) | 582 | 2026-10-05 | MIT | Sleek.design REST API ile ajanın mobil ekran **tasarımı** üretmesi, screenshot, sonra RN'e implement etmesi (ücretli SaaS, 1 run deneme) | `npx skills add sleekdotdesign/agent-skills` + `SLEEK_API_KEY` | 3 |
| [ehmo/platform-design-skills](https://github.com/ehmo/platform-design-skills) | 604 | 2026-10-05 | MIT | Apple HIG + Material 3 + WCAG 300+ kural (ios/android SKILL.md). Kod Swift/Kotlin ama idiyom kuralları RN'e taşınır | `npx skills add ehmo/platform-design-skills` | 3 |
| [gorhom/react-native-bottom-sheet](https://github.com/gorhom/react-native-bottom-sheet) | 9.106 | 2026-10-05 | MIT | JS bottom sheet (Reanimated). Expo ve Vercel artık **native** `formSheet` / `@expo/ui BottomSheet` öneriyor; @expo/ui drop-in replacement sağlıyor | `npx expo install @gorhom/bottom-sheet` | 3 |
| [sonner-native](https://github.com/gunnartorfis/sonner-native-toasts) | 1.357 | 2026-10-04 | MIT | Sonner'ın RN portu: `toast.success/error/promise`, swipe-dismiss, dark mode, Expo Go'da çalışır | `npx expo install sonner-native` → `_layout.tsx`'e `<Toaster />` | **5** |
| [nandorojo/burnt](https://github.com/nandorojo/burnt) | 1.561 | 2026-10-04 | MIT | **Native** toast/alert (iOS: SPIndicator/AlertKit, Android: ToastAndroid, Web: sonner); native modal üstünde görünür. Dev build gerekir | `npx expo install burnt` + prebuild | 4 |
| [calintamas/react-native-toast-message](https://github.com/calintamas/react-native-toast-message) | 2.150 | 2026-09-28 | MIT | Klasik toast; keyboard-aware, custom layout | `npx expo install react-native-toast-message` | 3 |
| [nandorojo/zeego](https://github.com/nandorojo/zeego) | 2.253 | 2026-10-05 | MIT | Native dropdown/context menü (iOS UIMenu, Android Material) — JS "dropdown" yerine | `npx expo install zeego` | 4 |
| [kirillzyusko/react-native-keyboard-controller](https://github.com/kirillzyusko/react-native-keyboard-controller) | 3.739 | 2026-10-05 | MIT | Klavyeyi kare kare takip eden UI; Expo skill'lerinde `KeyboardAvoidingView` yerine zorunlu | `npx expo install react-native-keyboard-controller` | 4 |
| [Gentleman-Programming/Gentleman-Skills → react-native](https://github.com/Gentleman-Programming/Gentleman-Skills/tree/main/community/react-native) | 657 | 2026-10-05 | MIT | Topluluk RN skill'i; jenerik (emoji ikon, elevation gölge, `headerShown:false` örnekleri = tam da native-slop) | — (ALMA) | 1 |
| [BjornMelin/dev-skills → expo-motion](https://github.com/BjornMelin/dev-skills/tree/main/skills/expo-motion) | 8 | 2026-09-24 | MIT | Reanimated 4 / Skia / Lottie / Rive karar matrisi, manifest kontrolü | `~/.claude/skills/expo-motion/SKILL.md` | 2 |
| [tristanmanchester/agent-skills → animating-react-native-expo](https://github.com/tristanmanchester/agent-skills/tree/main/animating-react-native-expo) | 3 | 2026-09-14 | — | Reanimated 4 + RNGH 3 (onDeactivate `canceled`) motion kuralları | kopyala | 2 |
| [hairyf/skills → react-native-expo / react-native-reusables](https://github.com/hairyf/skills) | ~24 | 2026-06 | MIT | expo/expo dokümanından otomatik üretilmiş skill'ler (config, EAS, RNR bileşen referansı) | `npx -y skills add hairyf/skills --skill react-native-reusables --agent claude-code` | 2 |
| [skills.sh/topic/mobile](https://skills.sh/topic/mobile) | — | canlı | — | Dizin: vercel-react-native-skills (~147K), expo/skills (937K toplam; building-native-ui ~53K, expo-tailwind-setup ~37K), sleek-design-mobile-apps | `npx skills add <owner/repo>` | 3 |
| claudskills.com / claudemarketplaces.com | — | canlı | — | Aynı skill'lerin aynaları (`rn-mobile-dev` = vercel kurallarının kopyası, `expo-motion` = BjornMelin, `react-native-expo` = hairyf). Yeni şey yok | — | 1 |

---

## 1. expo/skills — Expo resmi ajan skill'leri (DEĞER 5)

- Link: https://github.com/expo/skills — docs: https://docs.expo.dev/skills/ — Claude rehberi: https://docs.expo.dev/agents/claude
- 2.655 yıldız, MIT, günlük güncelleniyor; skills.sh'de 937K toplam kurulum.
- Kurulum:
  ```bash
  # Claude Code (plugin, otomatik güncellenir)
  claude plugin install expo@claude-plugins-official
  # Codex
  codex plugin add expo@openai-curated
  # Cursor / OpenCode / diğerleri
  npx skills@latest add expo/skills --skill '*'
  ```
- Expo'nun Claude rehberi: `npx create-expo-app@latest` projesi → plugin kur → repo'ya **`AGENTS.md`** (proje bağlamı + SDK doküman linkleri) ve **`.claude/settings.json`** (plugin enable) commit et → oturumda `/mcp` ile Expo hesabına bağlan.
- Bizim için kritik skill'ler (hepsi `kopyalar/expo-skills/` altında):
  - `expo-overview` → yönlendirici. "It looks AI-generated / too generic" → `expo-design-system` + `expo-native-ui`.
  - `expo-native-ui` → kütüphane tercihleri, stil kuralları, semantik renk.
  - `expo-design-system` → token teması + **`references/native-slop.md`**: 20 adlandırılmış "AI yapmış" belirtisi ve grep kontrolleri. **Jenerik görünüm sorununun doğrudan ilacı.**
  - `expo-ui` → `@expo/ui` ile gerçek SwiftUI/Compose BottomSheet/Picker/Switch/Menu (SDK 56+, Expo Go'da çalışır). **Ham Android pop-up sorununun doğrudan ilacı.**
  - `expo-animation` (+ `RECIPES.md`) → Reanimated 4 + haptics, "animate mi etmeyeyim mi" kapısı, press feedback, sheet, toast tarifleri.
  - `expo-router` → `presentation: 'formSheet'`, `NativeTabs`, `Link.Preview`/`Link.Menu`.
  - `expo-data-fetching` → "her ekranın 4 durumu var" (loading ≠ empty).

### 1.1 `expo-native-ui/SKILL.md` — kritik bölümler (AYNEN)
Kaynak: https://github.com/expo/skills/blob/main/plugins/expo/skills/expo-native-ui/SKILL.md

```markdown
## Library Preferences

- **For any sheet, picker, slider, toggle, menu, or grouped-form section: use `@expo/ui` (see `expo-ui` skill) before reaching for a React Native built-in or community library** — it renders native SwiftUI/Compose and works in Expo Go on SDK 56+. For grouped/settings-style rows (short, fixed-length), use `@expo/ui`'s `List` + `ListItem`. For large or unknown-length scrolling lists (feeds, search results, catalogs), use `FlatList` or `FlashList` — `@expo/ui`'s `List` is not virtualized.
- Never use modules removed from React Native such as Picker, WebView, SafeAreaView, or AsyncStorage
- Never use legacy expo-permissions
- `expo-audio` not `expo-av`
- `expo-video` not `expo-av`
- `expo-symbols` (`SymbolView`) for SF Symbols on iOS, not `@expo/vector-icons` — see `references/icons.md`. SF Symbols are Apple-only: on Android every icon needs a Material source (`md` prop on NativeTabs triggers; in-screen options under "Android: Material Icons" in icons.md), never SF-only iconography
- `react-native-safe-area-context` not react-native SafeAreaView
- `process.env.EXPO_OS` not `Platform.OS`
- `React.use` not `React.useContext`
- `expo-image` Image component instead of intrinsic element `img`
- `expo-glass-effect` for liquid glass backdrops
- `Color` from `expo-router` for native semantic colors, not raw `PlatformColor` (type-safe, auto-adapts to light/dark)
- In SDK 56+, never import from `@react-navigation/*` directly — use `expo-router/react-navigation` instead (covers `@react-navigation/native`, `/core`, `/elements`, `/routers`)

## Responsiveness

- Wrap screens with scrollable content in a ScrollView. Screens whose root is a FlatList/FlashList must not add an outer ScrollView (the list is the scroll container), and full-bleed screens (camera, map, canvas) need neither
- Use `<ScrollView contentInsetAdjustmentBehavior="automatic" />` instead of `<SafeAreaView>` for smarter safe area insets
- `contentInsetAdjustmentBehavior="automatic"` should be applied to FlatList and SectionList as well
- Use flexbox instead of Dimensions API
- ALWAYS prefer `useWindowDimensions` over `Dimensions.get()` to measure screen size

## Behavior

- Use expo-haptics conditionally on iOS to make more delightful experiences
- Use views with built-in haptics like `<Switch />` from React Native and `@react-native-community/datetimepicker`
- When a Stack route has scrollable content, make the ScrollView (or FlatList) the first component inside the route, with `contentInsetAdjustmentBehavior="automatic"` set
- Use the `<Text selectable />` prop on text containing data that could be copied
- Consider formatting large numbers like 1.4M or 38k
- Never use intrinsic elements like 'img' or 'div' unless in a webview or Expo DOM component
- Every screen that loads data has four states (loading, error, empty, content) - never show the empty state while the first load is still resolving; the rules live in the `expo-data-fetching` skill
- On scrollable forms and search results, use `keyboardShouldPersistTaps="handled"` so controls receive the first tap and unhandled taps can dismiss the keyboard. Use `"always"` only when unhandled taps should also keep it open
- A form's primary action must never sit under the keyboard. For UI that tracks the keyboard's real frame, load the `expo-animation` skill's keyboard recipe (`react-native-keyboard-controller`) - never `Keyboard.addListener` plus a timing animation
- Every enabled control must perform its advertised action: search filters results, Save commits edits, and settings affect behavior. Empty handlers and success alerts are not implementations; local state is enough when the user requested a prototype
- For async saves, preserve drafts and handle pending/failure states per `expo-data-fetching`; do not dismiss a form before its save succeeds

Before calling a screen complete, walk through its primary task, including one failure and recovery when it loads or saves data. Check keyboard access and back/dismiss behavior. Try long titles, missing images, no search results, and large system text; required actions must remain reachable. Report what you exercised and what you could not run.

# Styling

Follow each platform's own design language: Apple Human Interface Guidelines on iOS, Material Design 3 on Android. Never dress one platform in the other's uniform - no FAB or ripple in iOS layouts; no hand-built iOS chrome (back-chevrons, large-title text, iOS-styled switches) on Android.

## General Styling Rules

- Prefer flex gap over margin and padding styles
- Prefer padding over margin where possible
- Always account for safe area, either with stack headers, tabs, or ScrollView/FlatList `contentInsetAdjustmentBehavior="automatic"`
- Ensure both top and bottom safe area insets are accounted for
- Inline styles not StyleSheet.create unless reusing styles is faster
- For any motion or animation work, load the `expo-animation` skill — it owns the animate-or-not decision, timing values, and interruption rules
- Use `{ borderCurve: 'continuous' }` for rounded corners unless creating a capsule shape
- ALWAYS use a navigation stack title instead of a custom text element on the page
- When padding a ScrollView, use `contentContainerStyle` padding and gap instead of padding on the ScrollView itself (reduces clipping)
- CSS and Tailwind are not supported - use inline styles

## Colors

Use the `Color` API from `expo-router` for native semantic colors. It is a type-safe wrapper over `PlatformColor` that exposes iOS UIKit colors through `Color.ios.*` and Android Material 3 colors through `Color.android.material.*` (static) or `Color.android.dynamic.*` (adapts to the user's wallpaper on Android 12+). These resolve on-device and automatically adapt to light/dark mode and accessibility settings, so you no longer maintain separate light/dark hex tables or a `colors.web.ts` file.

## Text Styling

- Add the `selectable` prop to every `<Text/>` element displaying important data or error messages
- Counters should use `{ fontVariant: 'tabular-nums' }` for alignment

## Shadows

Use CSS `boxShadow` style prop. NEVER use legacy React Native shadow or elevation styles.
```

### 1.2 `expo-design-system/references/native-slop.md` — 20 "AI yapmış" belirtisi (AYNEN)
Kaynak: https://github.com/expo/skills/blob/main/plugins/expo/skills/expo-design-system/references/native-slop.md

```markdown
| # | Name | The tell (observable) | Native instead |
|---|---|---|---|
| 1 | **The Web Modal** | A custom centered dialog for picking or composing that ignores keyboard space and platform dismissal | Native sheet (`presentation: 'formSheet'`, `@expo/ui` BottomSheet) or anchored menu; use native alerts for consequential confirmations |
| 2 | **The X-Button Sheet** | A sheet closed only by an "X" in the top corner - no grab handle, no swipe-to-dismiss | Native sheet with detents; drag down to dismiss; Cancel/Done in the header where the platform puts them |
| 3 | **Emoji Iconography** | 🔥 ⚙️ ✨ ❤️ as tab icons, buttons, or empty-state art | SF Symbols on iOS, Material icons on Android - one icon family per platform (see `expo-native-ui`) |
| 4 | **The Purple-Gradient Hero** | A decorative gradient intro pushes useful content and the primary task below the fold | Lead task screens with useful content under a navigation title; keep a hero when it serves the requested experience |
| 5 | **The Floating Pill Tab Bar** | A custom rounded, inset, drop-shadowed tab bar hovering above the home indicator | The platform tab bar (`NativeTabs`) with system-managed placement, materials, and behaviors |
| 6 | **Inter Everywhere** | An arbitrary downloaded font replaces the app's typography, with missing weights or poor readability | System type (SF / Roboto) by default; preserve brand typography when requested and verify weights, readability, and scaling |
| 7 | **Everything's a Card** | Every list row and section wrapped in its own white rounded shadowed card; cards nested inside cards | Grouped lists (`@expo/ui` List, iOS inset-grouped, Material sections); grouping via background + hairlines, not boxes |
| 8 | **Shadowboxing** | Heavy drop shadows (opacity ≥ 0.15, radius ≥ 10) doing hierarchy's job on white-on-white surfaces | 2-3 tokened elevation levels; hierarchy from the type ramp and grouping. iOS is a low-shadow platform |
| 9 | **Wireframe Borders** | A 1px gray `borderWidth` outlining every container - usually Tailwind's `#E5E7EB` from web muscle memory | Spacing and surface contrast; hairlines only as list separators (`StyleSheet.hairlineWidth`, semantic separator color) |
| 10 | **alert() Confirmation** | Alerts interrupt routine undoable actions, report success, or replace field validation | Native confirmation alert for uncommon irreversible actions; undo for routine reversible ones. Field errors stay inline; success updates the UI |
| 11 | **The Hand-Rolled Header** | `headerShown: false` plus a `<Text>` title and custom back button - losing large-title collapse, back-swipe, and scroll-to-top | Stack header options. The navigation bar is configured, never rebuilt |
| 12 | **16-Everything** | The same 16px padding on every axis at every level; section gaps equal row gaps, so proximity carries no meaning | The spacing scale with distinct steps: row gap < group gap < section gap |
| 13 | **The Squish Reflex** | `scale: 0.96` press feedback on *every* touchable - including full-width list rows - or `TouchableOpacity`'s washed-out flash | Rows highlight (background change); buttons scale slightly or dim; `Pressable` with per-role feedback. Never `TouchableOpacity` |
| 14 | **The Grand Entrance** | Staggered `FadeInDown.delay(i * 100)` on every list and screen, replaying on every visit | Entrance animation only for rare/first-time moments (`expo-animation`'s frequency gate); routine screens just appear |
| 15 | **The Onboarding Carousel** | Generic promotional slides delay the first useful screen without collecting required setup or teaching necessary concepts | Start with useful content and contextual guidance; retain onboarding that serves required setup or the user's brief |
| 16 | **Cross-Platform Costume** | One platform wearing the other's uniform: a FAB or ripple in an iOS-idiom app; iOS back-chevrons, large titles, or iOS-styled switches on Android | Each platform gets its own HIG's idiom - or a deliberate, documented platform-neutral treatment |
| 17 | **Safe-Area Collision** | Content under the notch/Dynamic Island or home indicator - or hand-patched with `marginTop: 50` | Headers/tab bars handle it; otherwise `contentInsetAdjustmentBehavior="automatic"` or safe-area-context insets |
| 18 | **Dark-Mode Amnesia** | Hardcoded `#fff` / `#000` / gray hexes; the app breaks - or half-breaks - the moment the OS theme flips | Semantic colors (`Color.ios.*` / `Color.android.dynamic.*`) through the theme; brand colors as declared light/dark pairs |
| 19 | **The Spinner Blink** | A full-screen centered `ActivityIndicator` between every state, or "No items yet" flashing while the first fetch resolves | Four-state screens (see `expo-data-fetching`): loading ≠ empty, keep stale content while revalidating, `RefreshControl`, skeletons for slow initial loads with a known layout |
| 20 | **Keyboard Blindness** | The focused input or the submit button disappears behind the keyboard; buttons above a keyboard need two taps | `react-native-keyboard-controller` tracks the real keyboard frame (`expo-animation` keyboard recipe); `keyboardShouldPersistTaps="handled"` for forms/search, `"always"` when unhandled taps must also keep the keyboard open |
```

Grep kontrolleri (aynı dosyadan):
```bash
grep -rn 'TouchableOpacity\|TouchableHighlight\|TouchableWithoutFeedback' $SRC --include='*.tsx'   # #13
grep -rn 'fontFamily:' $SRC --include='*.tsx' | grep -v "^$THEME/"                                  # #6
grep -rn '<Modal' $SRC --include='*.tsx'                                                            # #1
grep -rn 'Alert\.alert' $SRC --include='*.tsx'                                                      # #10
grep -rn 'LinearGradient\|experimental_backgroundImage' $SRC --include='*.tsx'                      # #4
grep -rn 'headerShown:\s*false' $SRC --include='*.tsx'                                              # #11
grep -rn 'tabBarStyle' $SRC --include='*.tsx'                                                       # #5
rg -n '[\p{Emoji_Presentation}\x{FE0F}]' $SRC -g '*.tsx'                                            # #3
```

`expo-design-system/SKILL.md`'nin öz-eleştiri geçişi (AYNEN):
```markdown
## Self-Critique Pass

After building or changing a screen, screenshot it and check it against these principles (from Expo's design-principles guide). Each one maps to a system fix, not a local tweak:

- **Hierarchy / contrast** - is the most important element obviously first? Fix with `type` ramp steps, not ad-hoc font sizes.
- **Proximity / white space** - do related items sit closer than unrelated ones? Fix with `gap` + spacing tokens.
- **Repetition / unity** - do all corners, shadows, and accents match? If not, a value escaped the theme - move it in.
- **Alignment** - do edges share axes? Fix with consistent screen edge padding.
```

### 1.3 `expo-ui/SKILL.md` — Android ham pop-up'ın yerine native sheet (AYNEN)
Kaynak: https://github.com/expo/skills/blob/main/plugins/expo/skills/expo-ui/SKILL.md

```markdown
## Use @expo/ui by default — don't reach for RN alternatives first

**Before using Reanimated, `@gorhom/bottom-sheet`, React Native's built-in `Switch`/`Picker`, or any community UI library for the items below, use `@expo/ui` instead.** Only fall back to RN built-ins when `@expo/ui` is missing the component.

| Need | Use |
|------|-----|
| Slide-up sheet / bottom sheet | `BottomSheet` from `@expo/ui` — **not** Reanimated or `@gorhom/bottom-sheet` |
| Grouped native list rows (settings/form-style) | `List` + `ListItem` from `@expo/ui` — **not** `FlatList` (see note below) |
| Toggle | `Switch` from `@expo/ui` |
| Slider | `Slider` from `@expo/ui` |
| Date/time picker | `@expo/ui/community/datetimepicker` |
| Menu | `Menu` from `@expo/ui` |
| Form section with label | `FieldGroup` from `@expo/ui` |
| Collapsible section | `Collapsible` from `@expo/ui` |
```
```tsx
import { Host, BottomSheet, Column, Text } from '@expo/ui';
<Host>
  <BottomSheet isPresented={isOpen} onDismiss={() => setIsOpen(false)} snapPoints={['half', 'full']}>
    <Column><Text>Café name</Text><Text>Address</Text></Column>
  </BottomSheet>
</Host>
```
Drop-in replacement'lar (`@expo/ui/community/<name>`): `@gorhom/bottom-sheet`, `datetimepicker`, `masked-view`, `@react-native-menu/menu`, `pager-view`, `picker`, `segmented-control`, `slider`. Kurulum: `npx expo install @expo/ui` (universal katman SDK 56+, Expo Go'da çalışır).

### 1.4 `expo-router/SKILL.md` — modal / sheet (AYNEN)
```tsx
// Modal
<Stack.Screen name="modal" options={{ presentation: "modal" }} />   // Prefer this to building a custom modal component.

// Dynamic form sheet
<Stack.Screen
  name="sheet"
  options={{
    presentation: "formSheet",
    sheetGrabberVisible: true,
    sheetAllowedDetents: [0.5, 1.0],
    contentStyle: { backgroundColor: "transparent" },   // liquid glass on iOS 26+
  }}
/>
```
Ayrıca: `ALWAYS use _layout.tsx files to define stacks`, `Prefer Stack.SearchBar`, `Whenever possible, include a <Link.Preview>`; tab bar için `NativeTabs` (`expo-router/unstable-native-tabs`) + `sf`/`md` ikon çifti.

### 1.5 `expo-animation/SKILL.md` — kritik bölümler (AYNEN; animasyon detayı başka ajanda)
Kaynak: https://github.com/expo/skills/blob/main/plugins/expo/skills/expo-animation/SKILL.md (Emil Kowalski ile ortak; orijinali `emilkowalski/skills/animate-expo`)

```markdown
### 1. Should this animate at all?

| Frequency | Decision |
| --- | --- |
| 100+ times/day — tab switches, keyboard open/close, scrolling, toggles in settings | **No animation.** Platform default or nothing. Stop here. |
| Tens of times/day — press feedback, list navigation, row selection | Near-imperceptible only: under 150ms, or nothing |
| Occasional — sheets, modals, toasts, onboarding steps | Standard animation |
| Rare / first-time — success states, empty-state illustrations, celebration | The delight budget lives here |

**Tab switches never slide.** ... `animation: 'none'`.

### 7. Press, not hover
- **Feedback on press-in, commit on press-out.**
- **`scale: 0.97` in 100–150ms** on any button-like pressable, `Pressable` + a CSS transition. ... Full-width list rows are the exception: they highlight their background instead
- **44×44pt minimum touch target** (48dp Android). If the visual is smaller, add `hitSlop`
- **Android ripple only in a Material-styled app.**

### 8. Haptics
| Moment | Call |
| --- | --- |
| A value ticks past a step — picker, slider detent, segmented control | `Haptics.selectionAsync()` |
| Something snaps home, a sheet detent catches, a drag commits | `Haptics.impactAsync(ImpactFeedbackStyle.Light)` |
| A heavy object lands, a destructive action fires | `Haptics.impactAsync(ImpactFeedbackStyle.Medium)` |
| Operation succeeded or failed | `Haptics.notificationAsync(NotificationFeedbackType.Success / Error)` |
- **Same frame as the visual.** - **One per user action.** - **Never the only feedback.**

## Never Ship
| Never | Instead |
| --- | --- |
| `PanResponder` | `Gesture.Pan()` from gesture-handler |
| `setState` in a gesture or scroll handler | shared value + `useAnimatedStyle` |
| `runOnJS` (deprecated in Reanimated 4) | `scheduleOnRN` from `react-native-worklets` |
| Core `Animated` for anything a finger touches | Reanimated |
| Animating `height` / `width` / `margin` / `flex` / `top` | `transform` + `opacity` |
| Animating `BlurView` intensity or Android `elevation` | crossfade a static layer |
| A screen transition rebuilt in JS | native stack `animation` |
| Sliding between tabs | `animation: 'none'` |
| `Easing.in(...)` on a UI element | `Easing.bezier(0.23, 1, 0.32, 1)` |
| `scale(0)` entrance | `scale(0.95)` + `opacity: 0` |
| Judging feel in Expo Go or the simulator | release build, slowest supported device |
```
`RECIPES.md` tarifleri: Press feedback (Reanimated CSS transition, 120ms, scale 0.97), drag-to-dismiss sheet, swipe-to-delete, collapsing header, list entrances, keyboard-synced UI, tab indicator, screen transitions, **Toast** (FadeInDown 300ms / FadeOutDown 250ms, `bottom: insets.bottom + 16`), threshold haptic.

### 1.6 `expo-data-fetching` — 4 durum kuralı (AYNEN)
```markdown
## Every Screen Has Four States
Design **loading**, **error**, **empty**, and **content** for screens that load data.
- **Loading ≠ empty.** Empty means *resolved with zero items*, not missing data.
- **Empty is a designed state, not a blank list.** Use `ListEmptyComponent`...
- **Refetches keep stale content.** ... `isLoading` for first-fetch spinners and `isFetching` for background activity; prefer a skeleton for a slow initial load with a known layout, and `RefreshControl` for user-initiated refresh.
- **Gate on hydration.**
**Saves preserve work.** While a mutation is pending, disable repeat submission. On failure, retain the draft, show an inline error...
```

---

## 2. Vercel `react-native-skills` (DEĞER 5)

- Link: https://github.com/vercel-labs/agent-skills/tree/main/skills/react-native-skills — 31.956 yıldız (monorepo), MIT, skills.sh mobilde #1 (~147K kurulum).
- Kurulum: `npx skills add vercel-labs/agent-skills --skill vercel-react-native-skills` (Claude Code'da `.claude/skills/` altına iner). Tek dosya versiyonu: `AGENTS.md` (35+ kural, 13 kategori) → doğrudan proje `AGENTS.md`'sine eklenebilir.
- Kopya: `kopyalar/vercel-react-native-skills/` (SKILL.md, AGENTS.md, `rules/*.md`).
- Bizim sorunlarımıza doğrudan dokunan kurallar (AYNEN):

`rules/ui-native-modals.md`:
```markdown
## Use Native Modals Over JS-Based Bottom Sheets

Use native `<Modal>` with `presentationStyle="formSheet"` or React Navigation v7's native form sheet instead of JS-based bottom sheet libraries. Native modals have built-in gestures, accessibility, and better performance. Rely on native UI for low-level primitives.
```
```tsx
<Modal visible={visible} presentationStyle='formSheet' animationType='slide' onRequestClose={() => setVisible(false)}>…</Modal>
// React Navigation v7
<Stack.Screen name='Details' component={DetailsScreen} options={{ presentation: 'formSheet', sheetAllowedDetents: 'fitToContents' }} />
```

`rules/ui-menus.md`:
```markdown
## Use Native Menus for Dropdowns and Context Menus
Use native platform menus instead of custom JS implementations. Native menus provide built-in accessibility, consistent platform UX, and better performance. Use zeego (https://zeego.dev) for cross-platform native menus.
```

`rules/ui-styling.md`:
```markdown
**Always use `borderCurve: 'continuous'` with `borderRadius`**
**Use `gap` instead of margin for spacing between elements**
**Use `padding` for space within, `gap` for space between**
**Use `experimental_backgroundImage` for linear gradients** (instead of third-party gradient library)
**Use CSS `boxShadow` string syntax for shadows** — `{ boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)' }`; NOT shadowColor/shadowOffset or `elevation`
**Avoid multiple font sizes – use weight and color for emphasis**
```

`rules/ui-pressable.md`: `Never use TouchableOpacity or TouchableHighlight. Use Pressable from react-native or react-native-gesture-handler instead.`

`rules/navigation-native-navigators.md`: native stack (`expo-router` default) + `NativeTabs` / `react-native-bottom-tabs`; `header: () => <CustomHeader/>` yerine `title`, `headerLargeTitleEnabled`, `headerSearchBarOptions`.

`rules/ui-safe-area-scroll.md`: `contentInsetAdjustmentBehavior='automatic'` on root ScrollView instead of `SafeAreaView` / manual inset padding.

`rules/design-system-compound-components.md`: `Button` + `ButtonText` + `ButtonIcon` compound pattern; string children kabul eden polimorfik bileşen yok.

---

## 3. emilkowalski/skills (DEĞER 4)

- Link: https://github.com/emilkowalski/skills — 43.581 yıldız, MIT. Emil Kowalski (Vercel/Linear; sonner & vaul yazarı).
- Kurulum: `npx skills@latest add emilkowalski/skills`
- Kopya: `kopyalar/emilkowalski-skills/` (animate-expo, apple-design, review-animations, break-ui, animation-vocabulary, find-animation-opportunities, pick-ui-library, mobile-native).
- Mobil için değerli olanlar:
  - **`animate-expo`** — `expo-animation`'ın birebir kaynağı (Expo'dakinde `cubicBezier` düzeltmesi + feedback bölümü ek). İkisinden biri yeter; Expo plugin'i kurduysan bunu ekleme.
  - **`apple-design`** — Apple WWDC "Designing Fluid Interfaces" ilkeleri: press-down'da anında tepki, 1:1 takip, kesintiye uğrayabilir animasyon, spring (damping 1.0 varsayılan; momentum varsa 0.8), velocity handoff, rubber-band, translucent material hiyerarşisi. Web'e çevrilmiş ama ilkeler RN'de aynen geçerli; `animate-expo` zaten bunları RN'e uygular.
  - **`review-animations`** — mevcut animasyonları sert kriterle gözden geçirme (iyi bir "QA" adımı).
  - **`break-ui`** — "Demo data / Worst case" toggle'ı ile uzun isim, boş liste, 1 öğe, 1.284 üye gibi gerçekçi kötü veriyle ekranı kırma. Jenerik görünümün gizlediği taşma/kırpılma hatalarını yakalar.
  - `pick-ui-library` web listesi (Sonner, base-ui, motion…); mobil karşılığı: `sonner-native`, `zeego`, `@expo/ui`.
- README'den (AYNEN): *"Agents don't have great taste... An `ease-in` easing for an enter animation when it's supposed to be `ease-out`. Or they choose a solid border instead of a semi-transparent shadow... This is your shortcut to great interfaces. A shortcut to stand out in a sea of slop."*

---

## 4. Code-with-Beto/skills — Beto Moedano (DEĞER 4)

- Link: https://github.com/Code-with-Beto/skills — 140 yıldız, 2026-09-19. (Ayrı bir `betomoedano/expo-skills` repo'su YOK; Beto'nun skill'leri bu org altında. Beto ayrıca Expo'nun `expo-native-ui`'sini "RN+Expo için merkez skill" olarak öneriyor.)
- Plugin'ler: `cwb-theming`, `cwb-app-icon` (SnapAI, iOS 26 Liquid Glass `.icon` + Android adaptive), `cwb-ship` (`bunx @codewithbeto/ship`, Platano template + RevenueCat), `cwb-local-build`.
- Kurulum:
  ```text
  /plugin marketplace add Code-with-Beto/skills
  /plugin install cwb-theming@cwb-plugins
  /plugin install cwb-app-icon@cwb-plugins
  ```
  Codex: `codex plugin marketplace add Code-with-Beto/skills`
- Kopya: `kopyalar/code-with-beto-skills/` (README + cwb-theming SKILL.md). `.ts` asset'leri (config.ts / colors.ts / ThemeContext.tsx) https://github.com/Code-with-Beto/skills/tree/main/plugins/cwb-theming/skills/theming/assets
- `cwb-theming/SKILL.md`'nin çekirdek fikri (AYNEN):
```markdown
## The two ideas that make this work

1. **Re-render on theme change.** Native and Material You colors are platform color objects, not strings React can diff. The signal that the theme changed is `useColorScheme()`. So the resolvers take the color scheme as an argument even when they do not read it: that makes dark/light an explicit dependency, and the value recomputes when the user toggles. The `ColorsProvider` reads `useColorScheme()` once near the root, so a toggle re-renders every consumer.

2. **Two ThemeProviders, different jobs.** `ColorsProvider` (this skill) feeds `useColors()` to your components. The navigation `ThemeProvider` themes the navigation chrome (headers, tab bars, default background). Both live at the root. Wire navigation's with `getNavigationTheme(dark)` so the chrome matches your tokens. Do not confuse them or drop one. On **Expo SDK 56+** the navigation theme bits import from `expo-router/react-navigation`; on **older SDKs** they come from `@react-navigation/native` instead.
```
```tsx
const { background, text, secondaryText, separator } = useColors();
const { primary } = useBrand();
```
- Jenerik görünüm ve dark-mode amnezisi (#18) için hazır çözüm: semantik token → iOS `Color.ios.*` / Android Material You.

---

## 5. callstackincubator/agent-skills (DEĞER 3)

- Link: https://github.com/callstackincubator/agent-skills — 1.664 yıldız, MIT.
- Kurulum: `npx skills@latest add callstackincubator/agent-skills --skill '*'` (veya tek tek `react-navigation`, `react-native-best-practices`). Codex: Plugins → "react native".
- Kopya: `kopyalar/callstack-agent-skills/` (README, react-native-best-practices + references, react-navigation + references: `form-sheet.md`, `native-bottom-tabs.md`, `header.md`, `safe-areas.md`).
- Değer: bare React Navigation 7 kullanan projeler için form-sheet/native-tabs/header rehberi; performans skill'i "ölç → optimize et → tekrar ölç" disipliniyle jank'i çözer (agent-device ile React DevTools profil komutları). Görsel kalite kuralı içermiyor.

---

## 6. pproenca/dot-skills → `expo-react-native-coder` (DEĞER 3)

- Link: https://github.com/pproenca/dot-skills/tree/master/skills/.experimental/expo-react-native-coder — 213 yıldız, MIT, experimental.
- Kurulum: `npx skills add pproenca/dot-skills --skill expo-react-native-coder`
- Kopya: `kopyalar/pproenca-expo-react-native-coder/` (SKILL.md, AGENTS.md, 50 `references/*.md`).
- 50 kural: `setup-*`, `route-*` (modal presentation, typed routes, stack-in-tabs), `screen-*` (FlashList, loading state, pull-to-refresh, settings SectionList), `auth-*` (Stack.Protected), `link-*`, `ux-*` (haptic, gesture, keyboard, safe area, status bar), `form-*`, `asset-*`, `test-*`. `assets/templates/` altında tab-layout, auth-layout, list/detail/form screen şablonları.
- Uyarı: Expo skill'lerinin "yapma" dediği bazı şeyleri öneriyor (`SafeAreaView` sarmalama, `KeyboardAvoidingView`, `@expo/vector-icons`). İskelet/akış için iyi; görsel kurallarda Expo + Vercel öncelikli.

---

## 7. UI kütüphaneleri ve ajan dosyaları

### 7.1 react-native-reusables (shadcn → RN) — DEĞER 4
- https://github.com/founded-labs/react-native-reusables — 8.675 yıldız, MIT. NativeWind/Uniwind + RN Primitives; Dialog, Sheet, Select, Popover (root `PortalHost` şart), `Text` kalıtımı, Lucide `Icon`.
- `npx @react-native-reusables/cli@latest init` / `add button dialog`. Templates: founded-labs/react-native-reusables-templates.
- Repo içindeki `.claude/` ve `.cursor/rules/argent.md` kendi "Argent" ajan ortam-inspektörü için; bize kural vermiyor. Ajan skill'i için hairyf/skills (`kopyalar/hairyf-react-native-reusables-skill/`).
- Neden değerli: web'de shadcn'e alışık ajanlar tutarlı, tema token'lı (CSS variables) bileşen üretir; "Everything's a Card" ve "Wireframe Borders" riskini azaltmak için yine native-slop kontrolü gerekir.

### 7.2 heroui-native — DEĞER 4
- https://github.com/heroui-inc/heroui-native — 3.668 yıldız, Apache-2.0, v1.0.x. Uniwind+Tailwind, Reanimated 4, Gesture Handler; opsiyonel `@gorhom/bottom-sheet`.
- Kurulum: `npm i heroui-native` + peer deps; `<GestureHandlerRootView><HeroUINativeProvider>…`; örnek app: heroui-inc/heroui-native-example.
- Resmi **MCP server** `@heroui/native-mcp` (dokümanlarda ajan için hazır kurulum prompt'u var): https://heroui.com/docs/native/getting-started/mcp-server
- Kopya: `kopyalar/heroui-native/` (README + docs).

### 7.3 gluestack-ui v4 — DEĞER 3
- https://github.com/gluestack/gluestack-ui — 5.320 yıldız, MIT. Copy-paste bileşenler (NativeWind), `npx gluestack-ui init -y` / `add --all -y`.
- Repo içinde `.agents/skills/gluestack-ui-v4/SKILL.md` (+7 alt skill) ve `.agents/skills/creating-reanimated-animations/SKILL.md`. Kopya: `kopyalar/gluestack-ui-agent-skills/`.
- Çekirdek ilkeler (AYNEN):
```markdown
1. **Gluestack components over React Native primitives**
2. **Component props over className utilities**
3. **Semantic tokens ONLY - NO EXCEPTIONS** - NEVER use generic tokens (`typography-*`, `neutral-*`, `gray-*`) or numbered colors (`red-500`, `blue-600`). ONLY use semantic tokens (`text-foreground`, `bg-primary`, `border-border`, etc.)
4. **className over inline styles**
5. **Spacing scale over pixel values**
```

### 7.4 NativeWind / Tamagui / Paper / ui-lib
- **NativeWind** 8.107★ — Expo `expo-tailwind-setup` skill'i ile kurulur; repo'da `skills/nativewind-v4-to-v5` migrasyon skill'i. Not: `expo-native-ui` "CSS and Tailwind are not supported - use inline styles" diyor; Tailwind seçersen `expo-design-system` "tokens in global.css as CSS variables" diyor.
- **Tamagui** 14.212★ — AGENTS.md/CLAUDE.md repo bakım sözleşmesi; UI kuralı yok.
- **react-native-paper** 14.465★ — Android'de Material 3 idiyomu için doğru; iOS'ta "Cross-Platform Costume" riski (#16).
- **react-native-ui-lib** 7.157★ — New Architecture desteği eksik (RN 0.73); Reanimated 4 projelerinde kaçın.

### 7.5 Şablonlar / starter'lar
- **obytes/react-native-template-obytes** 4.349★ — `npx create-obytes-app@latest MyApp`; şablon içindeki `claude.md` (kopya: `kopyalar/react-native-template-obytes/claude.md`): stack (Expo 54, Router 6, Uniwind/NativeWind, Zustand, React Query, TanStack Form+Zod, MMKV), `src/features/[name]/` yapısı, `@/` import, "DO NOT modify android/ or ios/ directly". Görsel kural yok.
- **infinitered/ignite** 19.938★ — `npx ignite-cli@latest new`; theme sistemi + bileşen kütüphanesi, keyboard-controller, edge-to-edge, MMKV. Ajan dosyası yok.
- **Expo resmi**: `npx create-expo-app@latest` + `expo-project-structure` skill'i (`src/app` routes-only, `screens/`, `components/`, kebab-case, `AGENTS.md`/`CLAUDE.md` kökte).
- **Beto Platano** (`bunx @codewithbeto/ship`) — ücretli template + skill.

---

## 8. "Android ham alert/pop-up" ve geri bildirim UI'si — kütüphane reçetesi

Sorun: ajan `Alert.alert()` ve RN `<Modal>` ile ham, platform-dışı pop-up üretiyor. Kurallar (yukarıdaki skill'lerden) + kütüphaneler:

| İhtiyaç | Tercih sırası | Kurulum |
|---|---|---|
| Bottom sheet / seçim / form | 1) `presentation: 'formSheet'` (expo-router, gerçek UISheetPresentationController) 2) `@expo/ui` `BottomSheet` (SDK 56+, Expo Go'da çalışır) 3) `@gorhom/bottom-sheet` (JS; 9.106★) | `npx expo install @expo/ui` / `npx expo install @gorhom/bottom-sheet` |
| Toast / bildirim | **sonner-native** (Expo Go OK, Reanimated, swipe-dismiss, `toast.promise`) veya **burnt** (tam native, iOS SPIndicator; dev build ister) veya react-native-toast-message | `npx expo install sonner-native` / `npx expo install burnt` |
| Dropdown / context menü | **zeego** (native UIMenu / Material) veya `Link.Menu` (expo-router, iOS) veya `@expo/ui` `Menu` | `npx expo install zeego` |
| Onay (geri alınamaz, nadir) | Native `Alert.alert` **uygun** (HIG: uncommon irreversible); rutin aksiyonda "Undo" toast | — |
| Başarı mesajı | Alert DEĞİL → UI'nin kendisi güncellenir + hafif toast + `Haptics.notificationAsync(Success)` | `expo-haptics` |
| Form hatası | Inline hata; alert değil | — |
| Klavye | `react-native-keyboard-controller` (`KeyboardProvider` kökte) | `npx expo install react-native-keyboard-controller` |
| Blur / cam | `expo-blur` (`tint="systemMaterial"`), iOS 26 `expo-glass-effect` | `npx expo install expo-blur expo-glass-effect` |

sonner-native kullanım (README'den):
```tsx
// app/_layout.tsx
import { Toaster } from 'sonner-native';
<GestureHandlerRootView style={{ flex: 1 }}>
  <Stack />
  <Toaster />
</GestureHandlerRootView>
// herhangi bir yerde
import { toast } from 'sonner-native';
toast.success('Kaydedildi'); toast.promise(save(), { loading: 'Kaydediliyor…', success: 'Tamam', error: 'Hata' });
```
burnt (README'den):
```tsx
import * as Burnt from "burnt";
Burnt.toast({ title: "Burnt installed.", preset: "done", message: "See your downloads." });
Burnt.alert({ title: "Done", preset: "done" });   // Android'de toast'a düşer
```
Kopyalar: `kopyalar/sonner-native/`, `kopyalar/burnt/`, `kopyalar/react-native-bottom-sheet/`, `kopyalar/react-native-toast-message/` (README + docs).

---

## 9. Animasyon / etkileşim skill'leri (kısa not — detay animasyon ajanında)

- **expo/skills `expo-animation`** = **emilkowalski `animate-expo`** (aynı metin; Expo'daki daha güncel). Reanimated 4 + worklets + Gesture Handler + expo-haptics; `RECIPES.md` hazır tarifler. Reanimated/moti/skia/lottie/gesture-handler için ayrı resmi skill yok; bu ikisi onların "ajan sözleşmesi".
- **gluestack `creating-reanimated-animations`** — v3/v4 tespiti, CSS transitions/keyframes, layout animations, Jest; kod üreticisi olarak iyi.
- **BjornMelin `expo-motion`** — Reanimated vs Skia vs Lottie/Rive karar matrisi; manifest (package.json/app.json) kapısı.
- **tristanmanchester `animating-react-native-expo`** — RNGH 3 `onDeactivate({canceled})` gibi güncel API notları.
- **Vercel** `animation-gpu-properties`, `animation-gesture-detector-press`, `react-compiler-reanimated-shared-values` (`.get()/.set()`).
- Moti / Lottie / Skia için ajan skill'i bulunamadı; `expo-animation` Lottie'yi "illustration only", Skia'yı "canvas when view hierarchy is the bottleneck" diye konumluyor.

---

## 10. Dizin/katalog bulguları

- **skills.sh** — `topic/mobile`: vercel-react-native-skills, sleek-design-mobile-apps, expo/skills (building-native-ui, native-data-fetching, expo-tailwind-setup, upgrading-expo). expo/skills sayfası: 937K toplam; `expo-ui` 26.4K, `expo-native-ui` 25.3K, `expo-design-system` 11.7K, `expo-animation` 10.5K.
- **claudskills.com** — `rn-mobile-dev` (Vercel kurallarının 36'sını paketlemiş kopya), `expo-motion` (BjornMelin), `expo-brownfield` (Expo). Ücretli masaüstü app; manuel `curl` ile SKILL.md indirilebilir.
- **claudemarketplaces.com** — `vercel-labs/agent-skills/react-native-skills`, `hairyf/skills/react-native-expo` (268 kurulum), `jezweb/claude-skills/react-native-expo`, `pproenca/dot-skills/expo-react-native-coder` ve `expo-react-native-performance` (42 kural), `tristanmanchester/.../animating-react-native-expo`. Hepsi GitHub'daki orijinallerin aynası.
- Expo blog: *How to apply professional design principles in AI app development* — 8 ilke (contrast, hierarchy, alignment, proximity, repetition, balance, white space, unity); yöntem: ekran görüntüsünü ajana ver, checklist'e göre öz-eleştiri yaptır (expo-design-system "Self-Critique Pass" bundan türemiş).
- Evan Bacon (Expo) benchmark (Beto'nun aktarımı): `building-native-ui` skill'i aktifken native header/toolbar/bottom-sheet kullanımı **+%46**.

---

## 11. Sonuç: zincire hemen alınacak kaynaklar

1. **expo/skills (plugin)** — `claude plugin install expo@claude-plugins-official` + proje köküne `AGENTS.md`. **[JENERİK GÖRÜNÜM ✔] [ANDROID POP-UP ✔]** `expo-design-system/native-slop.md`'nin 20 belirtisi + grep'leri doğrudan "AI yapmış görünüyor" sorununun teşhis/tedavisi; `expo-ui` + `expo-router formSheet` ham `Alert`/`Modal` yerine native sheet; `expo-native-ui` stil kuralları (boxShadow, borderCurve, semantik renk, SF Symbols/Material); `expo-animation` press feedback + haptics. Bizim zincirde "ekran bitti" demeden önce **Self-Critique Pass + native-slop grep** zorunlu adım olmalı.
2. **Vercel react-native-skills** — `npx skills add vercel-labs/agent-skills --skill vercel-react-native-skills`. **[ANDROID POP-UP ✔]** `ui-native-modals` (formSheet), `ui-menus` (zeego), `ui-pressable` (TouchableOpacity yasak), `ui-styling`; Expo skill'leriyle çelişmez, bare React Navigation projelerini de kapsar. AGENTS.md tek dosya olduğu için CLAUDE.md'ye gömülebilir.
3. **sonner-native + zeego + react-native-keyboard-controller + @expo/ui kurulumu proje şablonuna varsayılan** — **[ANDROID POP-UP ✔]** Ajana "toast = sonner-native, menü = zeego, sheet = formSheet/@expo/ui, onay = sadece geri alınamaz aksiyonda Alert" kuralı CLAUDE.md'ye yazılmalı; kütüphane yoksa ajan `Alert.alert`'e düşüyor.
4. **Code-with-Beto `cwb-theming`** — **[JENERİK GÖRÜNÜM ✔]** Dark-mode amnezisi ve platform rengi için tek `useColors()` hook'u; SDK 56+ `Color` API'sini Android Material You ile birleştiriyor. `cwb-app-icon` ile ikon da jenerik kalmaz.
5. **emilkowalski/skills (`review-animations`, `break-ui`, `apple-design`)** — **[JENERİK GÖRÜNÜM ✔]** QA katmanı: animasyonları sert kriterle gözden geçir, kötü veriyle ekranı kır, Apple akıcılık ilkeleri. `animate-expo`'yu Expo plugin'i varken ekleme (çift).

Yedek/ikincil: heroui-native (hazır "güzel" bileşen seti + MCP), react-native-reusables (shadcn alışkanlığı), callstack `react-navigation` (bare RN7), platform-design-skills (HIG/M3 kural referansı), sleek.design (tasarım üretimi; ücretli).

Almayacaklarımız: Gentleman-Skills react-native (native-slop örnekleri içeriyor), react-native-ui-lib (New Arch yok), claudskills/claudemarketplaces kopyaları (orijinal repo'lardan al).
