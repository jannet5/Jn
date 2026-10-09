# React Native Full Course 2026 | Build, Publish and Monetize a Full Stack Mobile App to App Store — JavaScript Mastery — 223 dk — https://www.youtube.com/watch?v=4nVoLX2taFg

## Tek paragraf: ne yapıyor, sonuç ne
Adrian (JS Mastery), sıfırdan "Recurrly" adlı bir abonelik takip uygulaması kuruyor: Expo SDK 54 + Expo Router (dosya tabanlı routing, route group'lar, tab ve dynamic route), NativeWind v5 (Tailwind sınıfları + Figma'dan çıkarılmış `global.css` tema/design-system), Plus Jakarta Sans özel fontları, floating (yüzen) özel tab bar, FlatList tabanlı home ekranı (yatay "upcoming" carousel + dikey genişleyebilen abonelik kartları), Clerk ile e-posta + doğrulama kodlu custom auth (sign-in/sign-up/protected tabs), PostHog analytics (wizard ile otomatik kurulum + manuel `posthog.capture`), arama yapılabilen Subscriptions ekranı, alttan kayan "Create Subscription" modal'ı ve Zustand store; her özellik `dev` branch → PR → CodeRabbit AI review → merge akışıyla ilerliyor. Sonunda EAS CLI ile `eas build --platform ios --profile production` + `eas submit` ile IPA'yı App Store Connect/TestFlight'a yüklüyor. Not: Başlıktaki "monetize" kısmı (Clerk Billing ile abonelik planı/ödeme) ve Node/Express/MongoDB backend bu videoda **uygulanmıyor**; yalnızca intro'da anlatılıyor, backend ayrı kursa/2. bölüme bırakılıyor ([03:41:47]). Uygulama videonun sonunda hâlâ local state/dummy data ile çalışıyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Expo (SDK 54, `create-expo-app`, Expo Go, Expo Router, expo-font, expo-splash-screen, expo-secure-store): https://jsm.dev/nativesub-expo
- Expo EAS (EAS CLI, EAS Build, EAS Submit, EAS Update, EAS Workflows, Expo Observe): https://jsm.dev/nativesub-eas
- Clerk (auth + billing; `@clerk/clerk-expo`, tokenCache, Expo quickstart "Copy as Markdown"): https://jsm.dev/nativesub-clerk
- PostHog (React Native SDK, `npx -y @posthog/wizard@latest`, PostHog MCP server, session replay, funnels, feature flags): https://jsm.dev/nativesub-posthog
- CodeRabbit (PR üzerinde AI code review, "prompt for AI agent" özelliği): https://jsm.dev/nativesub-coderabbit
- Junie AI (JetBrains coding agent; "brave mode", plan mode, Claude modeli seçimi): https://jsm.dev/nativesub-junie
- WebStorm (IDE, kişisel kullanım için ücretsiz): https://jsm.dev/nativesub-webstorm
- Video Kit (kod, assets zip, Figma tasarımı, final `global.css`, `theme.ts`, `icons.ts`, `data.ts`, `type.d.ts`, `image.d.ts`, `utils.ts`, prompt'lar, React Native roadmap/guide): https://jsm.dev/nativesub-kit
- JSM Skills: jsmastery.com/skills · Workflow guides: jsmastery.com/workflow-guides
- Agentic Engineering Course: https://jsm.dev/ai ve https://jsm.dev/nativesub-agentic · React Native Course waitlist: https://jsm.dev/nativesub-native · JSM Pro: https://jsm.dev/nativesub-jsm
- Kütüphaneler: `nativewind@5` (pre-release), `tailwindcss`, `react-native-css`, `react-native-reanimated`, `react-native-safe-area-context`, `lightningcss` (override 1.30.1), `prettier-plugin-tailwindcss`, `clsx`, `dayjs`, `zustand`, `posthog-react-native`, `react-native-svg`, `@clerk/clerk-expo`, `expo-secure-store`, `eas-cli` (global)
- Dış araçlar: Figma (tasarım), Google Fonts (Plus Jakarta Sans TTF), GitHub, ImgBB (ekran görüntüsünü URL'ye çevirmek için), Whisper Flow (sesle prompt yazma), Apple Developer Program ($99/yıl), Google Play Console ($25 tek sefer), App Store Connect, TestFlight

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)

### Modül 0 — Giriş ve stack (00:00:00 – 00:13:14)
1. [00:00:34] Hedef: Recurrly abonelik yöneticisi; stack: Expo, NativeWind, Clerk (auth+billing), CodeRabbit, PostHog. Backend (Node/Express/MongoDB) ve e-posta hatırlatıcıları ayrı kursta.
2. [00:04:27] React Native yeni mimari (JSI, bridge yok) → performans tartışması bitti. [00:05:23] Expo = framework katmanı; React Native docs resmi olarak Expo öneriyor.
3. [00:06:57] EAS tanımı: EAS Build (bulutta derleme, Mac gerekmez), EAS Submit (tek komutla mağazaya), EAS Update (store review'suz OTA düzeltme), EAS Workflows (GitHub'dan otomatik build/sign/submit), Expo Observe (launch time vb. metrikler).
4. [00:08:47] Clerk: sign up/in, session, custom flow; Clerk'in yeni **native component'leri** (SwiftUI/Jetpack Compose ile render) dev build gerektirdiği için bu kursta kullanılmıyor. [00:09:48] Clerk Billing ile plan/ödeme/limit anlatılıyor (uygulanmadı).
5. [00:10:10] PostHog: event tracking, funnel, feature flag, error tracking, mobile session replay, in-app survey. [00:11:21] CodeRabbit: her PR'ı satır satır inceler, tek tıkla fix önerir.

### Modül 1 — Crash course: RN bileşenleri (00:13:14 – 00:23:44)
6. [00:14:06] `View`/`Text` (div/p yerine), `StyleSheet`, NativeWind. [00:16:19] `TouchableOpacity` (`onPress`), `TouchableHighlight`, `TouchableWithoutFeedback`, `ActivityIndicator`, `Button`.
7. [00:17:46] `FlatList` (`data`, `renderItem`) büyük listeler için, küçükler için `map`; `ScrollView`; [00:19:21] `SafeAreaView` ve neden `react-native-safe-area-context` tercih ettiği.
8. [00:20:22] `Image`, `ImageBackground` (SVG desteklemez → `react-native-svg`), [00:21:24] `Modal` (`visible`, `animationType`), `Alert.alert`, `Switch`, Expo `StatusBar`.

### Modül 2 — Proje kurulumu (00:23:44 – 00:35:19)
9. [00:26:09] expo.dev'de ücretsiz hesap. WebStorm'da boş klasör açılıyor; Junie kuruluyor.
10. [00:27:30] `npx create-expo-app@latest --template default@sdk-54 ./` (SDK 55 çıkmış olsa da sürüm sorunlarından kaçınmak için 54 sabitleniyor).
11. [00:28:52] Telefona Expo Go indir; `npx expo start` → QR tara. Terminal kısayolları: `a` Android, `i` iOS sim, `r` reload, `j` debugger. [00:30:14] Dev menü: cihazı salla veya 3 parmakla uzun bas. iOS bağlanmıyorsa: aynı Wi-Fi + VPN kapalı.
12. [00:31:22] Üretilen dosyalar: `package.json` (`main: expo-router/entry`), `app.json`, `tsconfig.json` (strict + `@/` alias), `assets/`, `app/_layout.tsx`, `app/index.tsx`, `components/`.
13. [00:34:10] İki terminal: "Expo" ve "terminal". `npm run reset-project` → eski kodu `app-example`'a taşı (Y). Expo'yu Ctrl+C ile durdur, `npx expo start` ile yeniden başlat.

### Modül 3 — NativeWind v5 ile styling (00:35:19 – 00:45:17)
14. [00:35:40] nativewind.dev → v5 installation (existing project adımları).
15. [00:36:38] `npx expo install nativewind tailwindcss react-native-css react-native-reanimated react-native-safe-area-context` (+ opsiyonel `prettier-plugin-tailwindcss` dev dep).
16. [00:37:20] Kökte `postcss.config.mjs` oluştur (docs'tan kopya). [00:37:56] `global.css` oluştur, 4 satır Tailwind directive yapıştır.
17. [00:38:21] `npx expo customize metro.config.js` → üretilen dosyayı NativeWind docs'taki `withNativeWind(...)` içeriğiyle değiştir.
18. [00:38:47] `app/_layout.tsx` içinde `import "./global.css"` (yol `app/global.css`).
19. [00:39:02] `package.json` → `"overrides": { "lightningcss": "1.30.1" }` (docs'taki değer).
20. [00:39:32] Kökte `nativewind-env.d.ts` oluştur, docs'taki `/// <reference types="nativewind/types" />` satırını ekle (düzenlenmez, commit edilir).
21. [00:39:58] `app/index.tsx`'e docs'taki "Welcome to NativeWind" test kodu. Stil gelmedi → bkz. Hatalar #1.
22. [00:43:03] Video kit'teki Figma tasarımından renkler: primary `#E87A53` (turuncu), kağıt rengi arka plan, accent'ler. Final `global.css` (Tailwind `@theme` ile `--color-background`, `--color-foreground`, `--color-primary`, `--color-accent`, success/destructive/subscription renkleri, spacing token'ları, `tabs-*` component sınıfları) kopyalanıyor = Recurrly design system. Test: `bg-background`, `text-success`.
23. [00:44:54] Pro tip: yeni projede tek komutla NativeWind v5 + Expo 54 + Tailwind kurulabilir.

### Modül 4 — Routing & navigation (00:45:17 – 01:03:10)
24. [00:46:02] `app/onboarding.tsx` oluştur, `rnfe` snippet'i; home'da `<Link href="/onboarding" className="mt-4 rounded bg-primary text-white p-4">`.
25. [00:48:01] Route group: `app/(auth)/sign-in.tsx` ve `sign-up.tsx` (karşılıklı `Link`: `/(auth)/sign-up`). Parantez URL'de görünmez.
26. [00:50:27] "Unmatched route" → `r` ile reload düzeltir (Metro cache ipucu).
27. [00:52:03] `app/(auth)/_layout.tsx`: `<Stack screenOptions={{ headerShown: false }} />`. Root `_layout.tsx`'te de `headerShown: false` (yoksa çift header).
28. [00:53:48] `app/(tabs)/` içine `subscriptions.tsx`, `insights.tsx`, `settings.tsx`; `index.tsx` de buraya taşınıyor.
29. [00:55:48] Dynamic route: `subscriptions/[id].tsx` → `const { id } = useLocalSearchParams<{ id: string }>()`. Test linkleri: `href="/subscriptions/spotify"` ve `href={{ pathname: "/subscriptions/[id]", params: { id } }}`. [00:58:10] Metin status bar'a yapışıyor → SafeAreaView ihtiyacı.
30. [00:59:50] github.com/new → repo "recurrly-native"; push komutları Junie'ye yapıştırılıyor (auto model + brave mode, komutları onaysız çalıştırır, commit mesajlarını yazar).
31. [01:02:17] CodeRabbit → GitHub ile bağla → "Add repositories" → repo'yu seç; ayarlar varsayılan bırakılıyor.

### Modül 5 — Tab navigation + floating tab bar (01:03:10 – 01:27:55)
32. [01:03:41] `app/(tabs)/_layout.tsx`: `<Tabs screenOptions={{ headerShown: false }}>` + `<Tabs.Screen name="index" options={{ title: "Home" }} />` (subscriptions, insights, settings).
33. [01:06:11] `[id]` beşinci tab olarak görünüyor → `subscriptions/[id].tsx` klasörü `(tabs)` dışına, `app/` köküne taşınıyor; gerekirse `<Tabs.Screen name="subscriptions/[id]" options={{ href: null }} />` ile gizle.
34. [01:07:24] Video kit'ten `assets.zip` (Google Drive) indir, mevcut `assets/` klasörünü sil, yenisini koy.
35. [01:08:01] `constants/icons.ts` (tüm PNG ikonları import/export). TS şikâyeti için kökte `image.d.ts` (png/jpg module declaration) ve `type.d.ts` (AppTab, TabIconProps, Subscription, UpcomingSubscription, ListHeadingProps, SubscriptionCardProps...).
36. [01:09:28] `constants/data.ts`: `export const tabs = [{ name: "index", title: "Home", icon: icons.home }, { name: "subscriptions", ..., icon: icons.wallet }, { name: "insights", icon: icons.activity }, { name: "settings", icon: icons.setting }]`.
37. [01:10:30] `npm i clsx`. Tab layout `tabs.map(tab => <Tabs.Screen key={tab.name} name={tab.name} options={{ title: tab.title, tabBarIcon: ({ focused }) => <TabIcon focused={focused} icon={tab.icon} /> }} />)`.
38. [01:11:47] `TabIcon` bileşeni: `<View className="tabs-icon"><View className={clsx("tabs-pill", focused && "tabs-active")}><Image source={icon} className="tabs-glyph" resizeMode="contain" /></View></View>`. `Image` **react-native**'den (expo-image değil) — bkz. Hatalar #3.
39. [01:14:46] `screenOptions`: `tabBarShowLabel: false`, `tabBarStyle: { position: "absolute", ... }`.
40. [01:15:27] `npm i react-native-safe-area-context`; `const insets = useSafeAreaInsets()`. `constants/theme.ts` (video kit; `global.css`'in JS karşılığı: `colors`, `spacing`, `components.tabBar`). `const tabBar = components.tabBar`.
41. [01:16:14] `tabBarStyle`: `{ position: "absolute", bottom: Math.max(insets.bottom, tabBar.horizontalInset), height: tabBar.height, marginHorizontal: tabBar.horizontalInset, borderRadius: tabBar.radius, backgroundColor: colors.primary, borderTopWidth: 0, elevation: 0 }`.
42. [01:18:37] `tabBarItemStyle: { paddingVertical: tabBar.height / 2 - tabBar.iconFrame / 1.6 }`; `tabBarIconStyle: { width: tabBar.iconFrame, height: tabBar.iconFrame, alignSelf: "center" }`.
43. [01:20:36] Her tab ekranında `import { SafeAreaView as RNSafeAreaView } from "react-native-safe-area-context"; const SafeAreaView = styled(RNSafeAreaView)` (styled NativeWind'den) → `className="flex-1 bg-background p-5"`. 4 ekrana uygulanıyor.
44. [01:24:01] `git checkout -b dev` → `git add .` → `git commit -m "feat: project setup, nativewind, routing and tab navigation"` → `git push -u origin dev` → PR aç. CodeRabbit: Spotify dynamic route yolu bozuk (gerçekten bozuk) + kullanılmayan `View` import'ları → düzelt, `git commit -m "implement fixes"`, merge.

### Modül 6 — Özel fontlar (01:27:55 – 01:36:11)
45. [01:28:15] Plus Jakarta Sans (Light, Regular, Medium, SemiBold, Bold, ExtraBold TTF) `assets/fonts/` içinde (video kit).
46. [01:29:21] `app.json` → `plugins`'e `["expo-font", { "fonts": ["./assets/fonts/PlusJakartaSans-Regular.ttf", ...6 dosya] }]`.
47. [01:31:15] `app/_layout.tsx`: `const [fontsLoaded] = useFonts({ "Sans-Regular": require("../assets/fonts/PlusJakartaSans-Regular.ttf"), "Sans-Bold": ..., "Sans-Medium", "Sans-SemiBold", "Sans-ExtraBold", "Sans-Light" })`; `useEffect(() => { if (fontsLoaded) SplashScreen.hideAsync() }, [fontsLoaded])`; `if (!fontsLoaded) return null`.
48. [01:33:21] `global.css` `@theme` içine `--font-sans: "Sans-Regular"; --font-sans-bold: "Sans-Bold"; --font-sans-extra-bold: ...` → JSX'te `className="font-sans-bold"`. Kural: body `font-sans`, başlık `font-sans-bold`, vurgu `font-sans-extra-bold`.
49. [01:35:45] `git commit -m "implement fonts"`, push.

### Modül 7 — Home UI (01:36:11 – 02:24:13)
50. [01:37:21] `npm install dayjs`. Video kit'ten güncel `constants/data.ts` (`HOME_SUBSCRIPTIONS`, `UPCOMING_SUBSCRIPTIONS`, `HOME_BALANCE`, `HOME_USER` dummy verisi) ve `type.d.ts`.
51. [01:38:47] `constants/images.ts`: `splashPattern`, `avatar` import/export default.
52. [01:40:12] Junie prompt'u ile `lib/utils.ts` → `formatCurrency(value, currency = "USD")` (`Intl.NumberFormat`, try/catch fallback).
53. [01:41:34] `app/(tabs)/index.tsx`: `<SafeAreaView className="flex-1 bg-background p-5">` → `<View className="home-header">` → `<View className="home-user"><Image source={images.avatar} className="home-avatar" /><Text className="home-user-name">{HOME_USER.name}</Text></View><Image source={icons.add} className="home-add-icon" />`. Sınıflar final `global.css`'ten (video kit).
54. [01:45:15] Balance card: `home-balance-card` > `home-balance-label` "Balance", `home-balance-row` > `home-balance-amount` `{formatCurrency(HOME_BALANCE.amount)}`, `home-balance-date` `{dayjs(HOME_BALANCE.nextRenewalDate).format("MM/DD")}`.
55. [01:48:36] `components/ListHeading.tsx` (`list-head`, `list-title`, `TouchableOpacity.list-action` > `list-action-text` "View all"; props `ListHeadingProps`).
56. [01:51:47] `components/UpcomingSubscriptionCard.tsx`: `upcoming-card` > `upcoming-row` > `Image.upcoming-icon`, `upcoming-price` `{formatCurrency(price, currency)}`, `upcoming-meta` `{daysLeft > 1 ? `${daysLeft} days left` : "Last day"}`, `upcoming-name` `numberOfLines={1}`.
57. [01:55:20] Yatay carousel: `<FlatList data={UPCOMING_SUBSCRIPTIONS} keyExtractor={i => i.id} renderItem={({ item }) => <UpcomingSubscriptionCard {...item} />} horizontal showsHorizontalScrollIndicator={false} ListEmptyComponent={<Text className="home-empty-state">No upcoming renewals yet</Text>} />`. Kart genişliği bir sonraki kartın ucu görünecek şekilde ayarlı (swipe ipucu).
58. [01:58:38] `lib/utils.ts`'e `formatStatusLabel`, `formatSubscriptionDateTime` (video kit). `components/SubscriptionCard.tsx`: `sub-card bg-card` > `sub-head` > `sub-main` (`Image.sub-icon`, `sub-copy` > `Text.sub-title numberOfLines=1`) + `sub-price-box` (`sub-price`, `sub-billing`).
59. [02:02:11] Kart rengi: `style={color ? { backgroundColor: color } : undefined}` + `className={clsx("sub-card", ...)}`. [02:03:21] `sub-meta` alt satırı: `category?.trim() || plan?.trim() || (renewalDate ? formatSubscriptionDateTime(renewalDate) : "")`, `ellipsizeMode="tail"`.
60. [02:04:35] Dış `View` → `Pressable onPress={onPress}`; `className={clsx("sub-card", expanded ? "sub-card-expanded" : "bg-card")}`, renk yalnızca `!expanded` iken.
61. [02:06:14] Home'da `const [expandedSubscriptionId, setExpandedSubscriptionId] = useState<string | null>(null)`; `onPress={() => setExpandedSubscriptionId(cur => cur === item.id ? null : item.id)}`.
62. [02:07:38] Genişleyen gövde: `{expanded && <View className="sub-body"><View className="sub-details">` + 5 adet `sub-row` > `sub-row-copy` > `sub-label`/`sub-value` (Payment, Category|Plan, Started, Renewal, Status).
63. [02:11:44] Dikey liste: `<FlatList data={HOME_SUBSCRIPTIONS} keyExtractor={i => i.id} renderItem={({ item }) => <SubscriptionCard {...item} expanded={expandedSubscriptionId === item.id} onPress={...} />} extraData={expandedSubscriptionId} ItemSeparatorComponent={() => <View className="h-4" />} showsVerticalScrollIndicator={false} ListEmptyComponent={<Text className="home-empty-state">No subscriptions yet</Text>} />`.
64. [02:14:12] **Scroll sorunu**: header/balance/upcoming sabit kalıyor; ScrollView içine FlatList koymak uyarı + performans sorunu. Çözüm: hepsini `ListHeaderComponent={<><View className="home-header"/>…<View className="mb-5"><FlatList horizontal …/></View><ListHeading title="All subscriptions" /></>}` içine taşı. Dikey parent + yatay child = çakışma yok.
65. [02:16:45] Floating tab bar kartları örtüyor → `contentContainerClassName="pb-30"`.
66. [02:17:32] `git commit -m "implement the home page"`, push, PR. CodeRabbit bulguları (bkz. Hatalar #5-#8), `git commit -m "implement CodeRabbit suggested fixes"`, merge.

### Modül 8 — Clerk kurulumu (02:24:13 – 02:28:37)
67. [02:25:50] Clerk dashboard → "Create application" → ad `jsm_recurrly`, Email + istenen SSO sağlayıcılar. Overview'da framework'ü Expo'ya çevir.
68. [02:26:29] `EXPO_PUBLIC_CLERK_PUBLISHABLE_KEY` kopyala → kökte `.env` oluştur. Configure → Email: sign-up varsayılan olarak şifre + e-posta doğrulama kodu ister (kod gönderimi Clerk'te hazır).
69. [02:27:23] Clerk Native Components (beta; SwiftUI/Jetpack Compose) dev build ister → şimdilik atlanıyor; EAS dev build aşamasında geçiş önerisi.

### Modül 9 — Authentication (AI ile) (02:28:37 – 02:53:40)
70. [02:30:23] Clerk Expo quickstart → "Copy as Markdown". Junie'de yeni chat, model Claude, **plan mode** açık.
71. [02:31:10] Doküman token limitini aşıyor → 3 mesaja bölme hilesi (aşağıdaki prompt). 1. yarı → "I've read the first part… provide the next part" → 2. yarı → asıl prompt.
72. [02:32:26] Asıl auth prompt'u (Kullandığı prompt'lar bölümünde). Tasarım görüntüsü: Figma login ekranı → JPEG export → JetBrains chat görsel desteklemiyor → ImgBB'ye yükle → URL'yi prompt'a ekle.
73. [02:34:12] Plan: root layout'ta `ClerkProvider`, `(auth)/_layout`, sign-up, sign-in, protected tabs, home ekranında kullanıcı bilgisi, tip tanımları. "Accept plan" → 7 todo → 6 dosya diff → "Keep all changes". `npx expo start` + `r`.
74. [02:36:12] Telefonda gerçek hesap: Clerk şifreyi data-breach veritabanına karşı kontrol ediyor ("please use a different password"); e-postaya 6 haneli kod geliyor; klavye otomatik numerik.
75. [02:38:00] Clerk dashboard → Users → profil fotoğrafı/isim güncelle → uygulamada anında yansıyor (`user.imageUrl`, `user.fullName`).
76. [02:38:58] Üretilen kodu okuma: `_layout.tsx`'te `ClerkProvider publishableKey={process.env.EXPO_PUBLIC_CLERK_PUBLISHABLE_KEY} tokenCache={tokenCache}` (tokenCache = `expo-secure-store` ile şifreli session → app kapanıp açılınca oturum kalıyor). `(tabs)/_layout.tsx`'te `const { isSignedIn, isLoaded } = useAuth()` → `<Redirect href="/(auth)/sign-in" />`. Koruma ekranlarda değil layout'ta.
77. [02:40:31] sign-in: email/password `TextInput`, validation (`emailTouched && !isEmailValid`), inline hata, `signIn.create({ identifier, password })`, `setActive`. sign-up: `signUp.create` → `prepareEmailAddressVerification` → kod ekranı → `attemptEmailAddressVerification`.
78. [02:42:14] Junie prompt'u: `add a logout button on our settings screen so that I can retest the entire authentication flow` → tam Settings/profil ekranı + `signOut()` butonu geldi.
79. [02:44:58] `git commit -m "feat: clerk auth implementation"`, push, PR (34 dosya). CodeRabbit sequence diagram + bulgular (bkz. Hatalar #9-#12); her bulgunun "prompt for AI agent" metni Junie'ye yapıştırılıyor. `git commit -m "implement CodeRabbit suggested fixes"`; README conflict'i çözülüp merge.

### Modül 10 — PostHog (02:53:40 – 03:05:44)
80. [02:54:19] posthog.com → Get started → bölge seç → organizasyon → "Product analytics" → "React Native" → otomatik kurulum.
81. [02:55:32] `npx -y @posthog/wizard@latest` (öncesinde terminalde açık Claude Code vb. AI oturumundan çıkış yap, çakışıyor). Wizard: RN algılar → tarayıcıda yetkilendirme → proje seç → ~8 dk codebase analizi → "PostHog event plan" → `posthog-react-native` + `react-native-svg` kurar, `app.config.js` oluşturur, `src/config/posthog.ts` yazar, `_layout`'u sarar, auth dosyalarına event ekler.
82. [02:58:31] Wizard sonunda **PostHog MCP server** kurulumu (editör seç, `a` ile tüm veri izinleri) → AI aracı "hangi ekranda drop-off en yüksek" gibi soruları gerçek veriden yanıtlar.
83. [02:59:14] Otomatik eklenen eventler: `user_signed_in`, `user_sign_in_failed`, `user_signed_up`, `user_sign_up_failed`, `user_signed_out`, `subscription_expanded`, subscription detail opened.
84. [03:00:20] Hata "Unable to resolve posthog-react-native" → `npx expo install posthog-react-native` → `npx expo start -c`.
85. [03:01:21] Logout/login → PostHog'da `identify` eventi görünüyor. Onboarding: autocapture (click/submit), heatmaps, **session replay** açık. Plan: ücretsiz (1M event, 5K replay) veya pay-as-you-go.
86. [03:02:55] Dashboard dark theme; Quick start: proxy (opsiyonel), ilk insight (pageviews). Activity sekmesinde kullanıcı e-postası, dokunulan view/button, ziyaret edilen ekran, `subscription_expanded` görünüyor.
87. [03:05:01] `git commit -m "implement PostHog integration"`, push.

### Modül 11 — Subscriptions tab (03:05:44 – 03:10:16)
88. [03:06:27] Junie prompt'u: `implement a searchable subscription list using the existing SubscriptionCard component and the dummy data from constants on the subscriptions screen`.
89. [03:07:36] Sorunlar: klavye içeriği örtüyor; başlık ve input metni beyaz. İkinci prompt (bkz. prompt'lar). Üçüncü: `the cards are still being hidden by the keyboard. Fix it`.
90. [03:08:56] Çözüm: `KeyboardAvoidingView` kaldırıldı; FlatList `data={filtered}` (name/category/plan'a göre filtre) + `keyboardDismissMode="on-drag"` (+ `keyboardShouldPersistTaps`), liste klavye açılınca kendisi kaydırıyor.

### Modül 12 — Create Subscription modal + Zustand (03:10:16 – 03:13:57)
91. [03:10:54] Video kit'teki detaylı prompt (bkz. prompt'lar) → `components/CreateSubscriptionModal.tsx` üretildi + home header'daki `icons.add` `Pressable` ile açılıyor; AI kendiliğinden PostHog tracking de ekledi.
92. [03:12:39] Test: Spotify 5.99 monthly entertainment → listenin başına geliyor. Subscriptions ekranında görünmüyor → prompt: `make sure the newly created subscription also shows on the all subscriptions screen` → AI mevcut **Zustand** ile `useSubscriptionStore` kuruyor; home ve subscriptions aynı store'u okuyor.
93. [03:14:13] Modal kodu: `Category`/`Frequency` tipleri, `CATEGORY_COLORS`; props `visible`, `onClose`, `onSubmit`; `handleSubmit`: `parseFloat(price)`, `dayjs().add(1, frequency === "monthly" ? "month" : "year")` renewal, yeni obje `{ id, name, price, currency, frequency, category, status: "active", startDate, renewalDate, icon: icons.wallet (sonra icons.add), billing, color }` → `onSubmit` → form reset → kapat.
94. [03:16:02] `<Modal visible animationType="slide" transparent onRequestClose>` > `KeyboardAvoidingView behavior="padding"` (iOS) > dışa dokununca kapatan `Pressable` overlay + sağ üst X `Pressable` > `ScrollView` > `TextInput` (name, price) + frequency/category için `Pressable` chip picker + submit butonu.
95. [03:17:49] `git commit -m "implement the subscriptions screen and the subscription create modal"`, push, PR.

### Modül 13 — PostHog custom event + CodeRabbit toplu fix (03:13:57 – 03:26:04)
96. [03:18:38] `CreateSubscriptionModal.tsx` → `handleSubmit` içinde `import posthog from "@/src/config/posthog"` ve:
    `posthog.capture("subscription_created", { subscription_name: name, subscription_price: price, subscription_frequency: frequency, subscription_category: category })`.
97. [03:19:58] Test: Netflix 15.99 → PostHog Activity'de event + property'ler. [03:20:30] New insight → Funnel: `user_signed_in` → `subscription_created` → ikinci `subscription_created` (retention göstergesi). Trends, Retention, User paths da gösteriliyor.
98. [03:22:36] `git commit -m "implement PostHog tracking for subscription created"`, push.
99. [03:22:54] CodeRabbit bulguları: upcoming carousel hâlâ statik veri okuyor (store'a bağlanmalı); Settings'te `posthog.reset()` `signOut()`'tan önce çağrılıyor (önce signOut, sonra reset). [03:23:32] **"Prompt for all review comments with AI agents"** → tek mesaj kopyalanıp Junie'ye yapıştırılıyor → params sanitization (`_layout`'ta hassas veri sızıntısı önleme), sign-in `handleVerified` yoluna PostHog, upcoming'in store'dan okunması, reset sıralaması + error handling. `git commit -m "implement CodeRabbit suggested fixes"`, merge.
100. [03:25:37] Ödev olarak bırakılanlar: abonelik adına göre otomatik ikon eşleme (büyük ikon kütüphanesi), Insights ekranı.

### Modül 14 — EAS Build & Submit → App Store (03:26:04 – 03:39:39)
101. [03:26:49] expo.dev/eas → free plan. `npm install -g eas-cli`.
102. [03:27:11] Mağaza hesapları: **Apple Developer Program** $99/yıl (2FA'lı Apple hesabı; Individual = kişisel ad satıcı adı olur; Organization = D-U-N-S numarası gerekir) → App Store Connect, TestFlight. **Google Play** $25 tek sefer (Google hesabı → developer account → personal/organization → kimlik doğrulama).
103. [03:27:57] `eas login` (e-posta/şifre; Google ile girildiyse Expo settings'ten şifre belirle) → `eas whoami`.
104. [03:28:37] `eas build:configure` → EAS projesi oluşturulsun (Y). Hata: proje `app.config.js` kullanıyor, projectId otomatik eklenemez → `app.config.js` → `expo.extra.eas.projectId = "<id>"` (JS dosyası, tırnaksız key). Komutu tekrar çalıştır → platform: **All** → `eas.json` üretildi (development/preview/production profilleri).
105. [03:30:07] `app.json` düzenle: `name: "Recurrly"`, `slug: "recurrly"`, `version: "1.0.0"`, `icon: "./assets/icons/logo.png"`, `expo-splash-screen` plugin `image: "./assets/images/splash-pattern.png"`, `ios.bundleIdentifier: "com.adrian.recurrly"`, `android.package: "com.adrian.recurrly"` (ilk gönderimden sonra değiştirilemez).
106. [03:32:08] `eas build --platform ios --profile production` (Android için `--platform android`, ikisi için `all`). Soru: standart/exempt encryption kullanıyor mu → Y → verilen `ios.infoPlist.ITSAppUsesNonExemptEncryption: false` satırını `app.config.js`'e ekle. Apple hesabı ile credential üretimine izin (Y) → Apple ID → cihaz doğrulama 6 haneli kod → proje sıkıştırılıp yükleniyor.
107. [03:33:16] Build FAILED: "npm ci can only install packages when your package.json and package-lock.json are in sync" → `rm -rf node_modules package-lock.json && npm install` → `git add package-lock.json && git commit -m "fix: sync package-lock" && git push` → build'i tekrar çalıştır (5–15 dk). Expo dashboard → Builds: spin up → config → expo doctor → credentials → prebuild → pod install → fastlane → archive. EAS iOS'ta provisioning profile/distribution certificate, Android'de release keystore'u kendisi yönetir.
108. [03:36:36] Build bitti → IPA indirilebilir. `eas submit --platform ios` → "Select a build from EAS" (2 dk önceki build) → Apple ile tekrar giriş → Expo dashboard → Deploy → Submissions: build indirildi, App Store Connect'e yükleniyor → "binary successfully uploaded", Apple işlemesi 5–10 dk, e-posta gelir.
109. [03:38:03] App Store Connect → Recurrly: "Ready for submission" (promotional text, screenshots/preview, keywords gir → Apple review'a gönder). **TestFlight**: "Missing Compliance" → Manage → encryption: standard; France dağıtımı: hayır → Save. Internal testing grubu; Builds 1.0.0 → telefona TestFlight indir, kendini tester ekle → gerçek kurulum.
110. [03:39:25] Store review/release Apple ve Google dashboard'larında yapılır; EAS yalnızca upload'u otomatikleştirir.

### Kapanış (03:39:39)
111. [03:41:47] Uygulama hâlâ in-memory; kalıcılık/backend için ayrı Node.js+Express+MongoDB kursu ve hazır GitHub backend repo'su; 20K like gelirse 2. bölümde bağlanacak.

## Kullandığı prompt'lar (varsa aynen)

Para birimi formatlama (Junie, sesle dikte — [01:40:17]):
```
Create a currency formatting function in lib/utils.ts that takes in a value and currency as params and formats a number as standard US money (dollar sign) with exactly two decimal places, defaulting to USD. Use a try/catch and handle the fallback.
```

Uzun dokümanı bölme hilesi (Junie/Claude, plan mode — [02:31:17]):
```
I'll provide you with three different messages. The first one is going to be the first part of the Expo documentation, the second one is going to be the second part of the Expo documentation, and the third one is going to be my prompt that you should take into account when implementing all of these features. After every single message, ask me to provide the next one.
```

Clerk auth implementasyonu (video kit prompt'u — [02:32:26]; Clerk Expo quickstart markdown'u ve login ekranı görüntüsünün ImgBB URL'si eklenerek):
```
Study the whole codebase and attached Clerk documentation to implement a complete custom Clerk auth flow for this Expo app with production-grade sign-up and sign-in screens with logic, validation, and navigation. The UI must strictly match the existing app design system and NativeWind patterns. Use the attached design only as layout inspiration, but keep the auth polished, brand-native, and focused on conversion, clarity, and trust.
```

Logout butonu ([02:42:16]):
```
Add a logout button on our settings screen so that I can retest the entire authentication flow once again.
```

CodeRabbit bulgusunu doğrulatma ([02:51:13]) — CodeRabbit'in "prompt for AI agent" metninin sonuna eklenen cümle:
```
Check whether this is actually a bug or not, because at one point I was able to see a password related error.
```

Subscriptions ekranı ([03:06:29]):
```
Implement a searchable subscription list using the existing SubscriptionCard component and the dummy data from constants. This can happen on the subscriptions screen.
```

Subscriptions düzeltmeleri ([03:08:02] ve [03:08:47]):
```
Great, but a couple of problems. The subscriptions heading at the top is white, it needs to be dark. Same thing goes for the text when we type into the input where we search subscriptions. And third thing is to bump the content up above the keyboard once the keyboard pops up when we start searching.
```
```
The cards are still being hidden by the keyboard. Fix it.
```

Create Subscription modal (video kit prompt'u — [03:11:00]):
```
Study the entire codebase, paying close attention to the existing design system, the constants, the icons, and the home screen. Then, create a new CreateSubscriptionModal component that is a React Native Modal that slides up from the bottom with a transparent overlay. It has a header with "New Subscription" and it has four different fields: name, price, frequency, and category, as well as a submit button. On submit, it creates a subscription object with all of the different things that a subscription needs (id, name, price, currency, frequency, category, status, start date, renewal date, icon, billing, color), and uses KeyboardAvoidingView for iOS so we can type into it properly, and it resets the form after the submission. Then, the plus icon in the home header should open this modal when tapped. When a subscription is created, add it to the beginning of the subscriptions list on the home screen. The new subscription should immediately appear in both the all subscriptions FlatList and the home screen.
```

Ortak state ([03:13:30]):
```
Make sure the newly created subscription also shows on the all subscriptions screen.
```

Ayrıca: CodeRabbit'in her bulgu için ürettiği "Prompt for AI agent" metinleri ve PR seviyesinde "Prompt for all review comments with AI agents" tek mesajı olduğu gibi Junie'ye yapıştırılıyor ([02:47:51], [03:23:32]).

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Figma → design system → `global.css`**: Renkler Tailwind `@theme` token'ı olarak (`--color-primary: #E87A53`, background/foreground/accent/success/destructive/subscription), spacing token'ları ve **semantik component sınıfları** (`home-header`, `home-balance-card`, `sub-card`, `upcoming-card`, `tabs-pill`, `list-action`…) tek dosyada. Ekranlarda utility yığını yerine bu sınıflar kullanılıyor; aynı değerler `constants/theme.ts`'te JS olarak var (tab bar gibi `style` prop'u gereken yerler için). [00:43:03], [01:16:29]
- **Floating pill tab bar**: `position: absolute`, `bottom: Math.max(insets.bottom, horizontalInset)`, yuvarlatılmış köşe, `marginHorizontal`, `backgroundColor: colors.primary`, `borderTopWidth: 0`, `elevation: 0`, label gizli (`tabBarShowLabel: false`), aktif tabda `tabs-pill`+`tabs-active` arka plan "hap" efekti; ikon kutusu `iconFrame` ile ortalanıyor. [01:14:46]–[01:20:19]
- **Safe area disiplini**: `react-native-safe-area-context` + `styled()` sarmalı SafeAreaView her ekranda; `useSafeAreaInsets` ile cihaza göre alt boşluk (iPhone 15 ≈ 34px, home-button'lu cihaz 0). [01:15:27]
- **Tipografi**: Plus Jakarta Sans 6 ağırlık, `expo-font` plugin + `useFonts`, `SplashScreen.preventAutoHideAsync()` ile font yüklenmeden ekran gösterilmez (FOUT yok); `font-sans` / `font-sans-bold` / `font-sans-extra-bold` sözlüğü. [01:27:57]
- **Splash**: `splash-pattern.png` (tasarımdaki desen) splash görseli, `logo.png` app ikonu. [03:31:01]
- **Liste hissi**: Yatay carousel kart genişliği bir sonraki kartın kenarı görünecek şekilde ("daha var" ipucu); `showsHorizontalScrollIndicator={false}`; `ItemSeparatorComponent` ile `h-4` boşluk; `ListEmptyComponent` ile boş durum metni; tüm ekran tek FlatList (`ListHeaderComponent`) ile tek parça kayıyor; `contentContainerClassName="pb-30"` ile floating bar altında içerik kalmıyor. [01:56:56], [02:14:37]
- **Kart renkleri**: Her aboneliğin kendi `color`'ı kapalı durumda arka plan; genişleyince nötr `sub-card-expanded` stiline geçerek detay metni okunur kalıyor; `Pressable` ile tap → expand/collapse, aynı anda tek kart açık (`expandedSubscriptionId`). [02:02:11]
- **Modal**: `Modal animationType="slide" transparent` + yarı saydam overlay, dışa dokununca kapanma, `KeyboardAvoidingView`, frequency/category için input yerine `Pressable` chip seçiciler. [03:16:02]
- **Metin güvenliği**: `numberOfLines={1}` + `ellipsizeMode="tail"` her başlık/değerde; "1 days left" yerine "Last day"; boş alanlar için "Not provided" fallback. [01:54:02], [02:21:28]
- Animasyon kütüphanesi (Reanimated) kuruluyor ama kursta özel animasyon yazılmıyor; tab/modal/stack geçişleri platform varsayılanı.

## Hatalar ve çözümleri
1. [00:40:54] NativeWind stilleri uygulanmıyor → `package.json`'daki `lightningcss` override'ı yalnızca temiz kurulumda etkili. `rm -rf node_modules package-lock.json && npm install` → `npx expo start --clear` → `r`.
2. [00:40:39] `Cannot find global.css` → import yolunu `./global.css` (app/ içinde) olarak düzelt.
3. [01:19:55] Tab ikonları görünmüyor / `resizeMode deprecated` uyarısı → `Image` yanlışlıkla `expo-image`'dan import edilmiş; `react-native`'den import et.
4. [01:13:49] `activity.png` bulunamıyor (dosya var) → Metro yeni asset'leri görmemiş; `npx expo start` ile sunucuyu yeniden başlat. Genel kural: kod doğruysa önce `r`, sonra 3 parmak reload, sonra `--clear` ile restart.
5. [01:05:40]/[01:25:59] CodeRabbit: `subscriptions/[id]` taşındıktan sonra Spotify linki kırık → yol düzeltildi. Kullanılmayan `View` import'ları temizlendi.
6. [02:19:32] CodeRabbit (major): `SplashScreen.preventAutoHideAsync()` modül seviyesinde çağrılmamış → font yüklenmeden splash kapanıp flash olabilir → `app/_layout.tsx` en üstüne eklendi.
7. [02:20:19]–[02:23:22] CodeRabbit: "All subscription" → "All subscriptions"; `View all` butonunda `onPress` yok (sonraya bırakıldı); boş opsiyonel alanlar boş satır bırakıyor → "Not provided" fallback (Junie'ye yaptırıldı); `images.ts`'te `splathPattern` typo; `formatCurrency` fallback'i currency'yi yok sayıyor → `value.toFixed(2)`; dummy `nextRenewalDate` geçmiş tarih (dummy, görmezden gelindi).
8. [01:46:20] `formatCurrency((...))` çift parantez syntax hatası → düzeltildi.
9. [02:47:37] CodeRabbit: `EXPO_PUBLIC_CLERK_PUBLISHABLE_KEY` repoya commit edilmiş → Junie `.env.example` ("replace me") oluşturdu, `.env`'i `.gitignore`'a ekledi, git geçmişinden kaldırdı; önerisi: key'i Clerk dashboard'dan yeniden üret (production'a geçerken zaten değişecek). Not: `.env.local` kullanılmalıydı.
10. [02:49:16] CodeRabbit: `ClerkProvider` font gate'in içinde → auth ve font seri yükleniyor, splash sonrası boş kare riski → `ClerkProvider` en dışa alındı, `RootLayoutContent` içinde fontlar + `useAuth` paralel yükleniyor (başlangıç süresi iyileşmesi).
11. [02:50:53] CodeRabbit (critical): `passwordValid` her zaman true (`length === 0 || length > 1`) → doğrulatıldı, gerçek bug, düzeltildi.
12. [02:51:53] CodeRabbit (critical): `window.location.href` React Native'de crash → `Platform.OS === "web"` kontrolü, native'de `Linking.openURL`; sign-in ve sign-up'ta 3 noktada düzeltildi.
13. [02:53:12] PR'da README conflict (Junie env notu eklemiş) → değişiklik atılıp resolved, merge.
14. [03:00:20] PostHog wizard sonrası `Unable to resolve posthog-react-native` → paket kurulmamış; `npx expo install posthog-react-native` + `npx expo start -c`.
15. [02:55:43] PostHog wizard aktif AI CLI oturumlarıyla çakışabilir → wizard'dan önce Claude Code vb.'den logout.
16. [03:07:39] AI'ın ürettiği Subscriptions ekranında klavye içeriği örtüyor, başlık/input rengi beyaz → iki ek prompt; nihai çözüm `KeyboardAvoidingView` yerine FlatList `keyboardDismissMode="on-drag"`.
17. [03:13:22] Yeni abonelik yalnızca home'da görünüyor (ekranlar ayrı local state) → Zustand store ile ortak state.
18. [03:22:54] CodeRabbit: `posthog.reset()` `signOut()` öncesinde → analytics identity ile session desync; sıralama değiştirildi + try/catch. Upcoming carousel statik veri okuyordu → store'a bağlandı.
19. [03:28:52] `eas build:configure` projectId'yi `app.config.js`'e yazamıyor → `extra.eas.projectId` elle eklendi.
20. [03:33:16] EAS build FAILED (dependency installation: `npm ci` lock uyumsuz) → lock dosyası yeniden üretilip commit edildi, build tekrar.
21. [03:32:32] iOS encryption sorusu → `ITSAppUsesNonExemptEncryption: false` `app.config.js`'e; App Store Connect/TestFlight'ta ayrıca "Missing Compliance" → standard encryption, France hayır.
22. [03:28:12] `eas login` şifresi bilinmiyor (Google ile kayıt) → Expo settings → change password.

## Bizim fabrikaya alınacaklar (somut, maddeli)
1. **Design system dosyası zorunlu**: Her projede `global.css` içinde Tailwind `@theme` token'ları (`--color-primary/background/foreground/accent/success/destructive`, spacing, `--font-sans*`) + semantik component sınıfları (`home-header`, `card`, `list-head`, `tabs-pill`…) ve aynı değerlerin JS kopyası `constants/theme.ts`. CLAUDE.md kuralı: "ekranlarda ham hex/`#fff`/`blue-600` kullanma; yalnızca token sınıfları".
2. **NativeWind v5 kurulum skill'i** (adım listesi + hata-önleyici): `npx expo install nativewind tailwindcss react-native-css react-native-reanimated react-native-safe-area-context`, `postcss.config.mjs`, `global.css`, `npx expo customize metro.config.js` + `withNativeWind`, `nativewind-env.d.ts`, `overrides.lightningcss`, ardından **mutlaka** `rm -rf node_modules package-lock.json && npm install && npx expo start --clear`.
3. **Floating tab bar şablonu**: `constants/data.ts` (tabs dizisi) + `(tabs)/_layout.tsx` (`tabs.map` → `Tabs.Screen`, `TabIcon` with `clsx`, `tabBarShowLabel:false`, absolute + `useSafeAreaInsets` + `theme.components.tabBar`), `href: null` ile tab dışı route gizleme. Android'in ham tab bar görünümünü bu şablon çözer.
4. **Safe area kuralı**: `react-native-safe-area-context`'ten `styled(SafeAreaView)`; RN'in kendi SafeAreaView'ı yasak; her ekran `flex-1 bg-background p-5` ile başlar; FlatList ekranlarda `contentContainerClassName="pb-30"`.
5. **Font pipeline**: `assets/fonts/*.ttf` + `app.json` `expo-font` plugin + `useFonts` + `SplashScreen.preventAutoHideAsync()` (modül seviyesi) + `hideAsync` in `useEffect` + `@theme --font-*` eşlemesi. CLAUDE.md: "varsayılan sistem fontu ile ekran teslim etme".
6. **Scroll mimarisi kuralı**: Ekranda liste varsa tüm sayfa tek `FlatList`; üst bloklar `ListHeaderComponent`; FlatList'i ScrollView'a sarma yasak; yatay carousel yalnızca dikey parent içinde; `ItemSeparatorComponent`, `ListEmptyComponent`, `showsVerticalScrollIndicator={false}` zorunlu.
7. **Ham pop-up yerine**: `Alert.alert` değil, alttan kayan `Modal animationType="slide" transparent` + overlay + `KeyboardAvoidingView` + chip picker şablonu (CreateSubscriptionModal) bileşen kütüphanesine alınır. Arama ekranlarında `keyboardDismissMode="on-drag"`.
8. **Metin güvenliği lint'i**: Tüm başlık/değer `Text`'lerinde `numberOfLines` + `ellipsizeMode="tail"`; boş değer için "Not provided"; çoğul/tekil ("Last day") kontrolü.
9. **Auth prompt şablonu**: Clerk Expo quickstart'ı "Copy as Markdown" ile prompt'a göm, plan mode'da çalıştır, tasarım görselini URL olarak ver; prompt metni yukarıdaki Clerk prompt'u. `ClerkProvider` en dışta (font gate dışında), `tokenCache` expo-secure-store, koruma `(tabs)/_layout`'ta `useAuth` + `Redirect`. Secrets: `.env.local` + `.env.example` + `.gitignore` ilk commit'ten önce.
10. **Çift AI kontrolü**: Üretim → PR → CodeRabbit → "Prompt for all review comments with AI agents" çıktısını Claude Code'a yapıştır → tekrar push. Fabrikada her modül `dev` branch + PR olarak çıkar; CodeRabbit ücretsiz katman repo'ya bağlanır. Özellikle `window`/web API kullanımı, validation mantığı, provider sıralaması, splash gate CodeRabbit'in yakaladığı tipik hatalar → CLAUDE.md'ye "RN'de `window`/`document` yok; `Platform.OS` kontrolü" kuralı.
11. **Analytics standardı**: `npx -y @posthog/wizard@latest` (AI CLI oturumları kapalı) + PostHog MCP; sonrasında her yeni özellikte `posthog.capture("<feature>_created", {...})`; `signOut()` sonra `posthog.reset()`; müşteriye funnel/retention/session replay panosu teslim edilebilir.
12. **Yayın checklist'i (EAS)**: `npm i -g eas-cli` → `eas login`/`eas whoami` → `eas build:configure` (platform All; `app.config.js` varsa `extra.eas.projectId` elle) → `app.json`: `name`, `slug`, `version`, `icon` (logo.png), splash görseli, `ios.bundleIdentifier`/`android.package` (`com.<musteri>.<app>`, sonradan değişmez), `ios.infoPlist.ITSAppUsesNonExemptEncryption:false` → lock dosyasını senkronla (`rm -rf node_modules package-lock.json && npm install`, commit) → `eas build --platform all --profile production` → `eas submit --platform ios|android` → TestFlight compliance + internal tester → App Store Connect metadata (screenshots, keywords) → review. Müşteri hesapları: Apple Developer $99/yıl (Organization için D-U-N-S), Google Play $25.
13. **Kısayol/hata kuralı CLAUDE.md'ye**: "Hata mantıksızsa önce `r`, sonra `npx expo start --clear`; asset eklendiyse Metro'yu yeniden başlat; `Image` her zaman `react-native`'den".
14. **State**: Ekranlar arası paylaşılan veri için Zustand store (`useSubscriptionStore`); local `useState` yalnızca ekran-içi UI durumu için.
15. **Monetize notu**: Videoda Clerk Billing uygulanmadı; fabrikada ödeme için Clerk Billing (plan/limit) veya RevenueCat ayrıca araştırılmalı. Clerk Native Components (SwiftUI/Compose) dev build ile gerçek native auth ekranı sağlar — EAS dev build'e geçildiğinde kullanılabilir.
