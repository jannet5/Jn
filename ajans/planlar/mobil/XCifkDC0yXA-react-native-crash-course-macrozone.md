# React Native Crash Course 2026 - Build a Complete Mobile App — Traversy Media — 98 dk — https://www.youtube.com/watch?v=XCifkDC0yXA

## Tek paragraf: ne yapıyor, sonuç ne
Brad Traversy, hiç yapay zekâ kullanmadan, Expo SDK 55 + Expo Router ile sıfırdan "Macro Zone" adlı bir beslenme/makro takip uygulaması yapıyor: 3 sekmeli (Home / Add Meal / All Meals) tab bar, StyleSheet tabanlı global renk ve stil sistemi, AsyncStorage ile kalıcı veri (öğün ekle/sil/tümünü temizle), `expo-haptics` ile titreşim, React Native `Share` API ile paylaşım, `expo-clipboard` ile özet kopyalama, `expo-notifications` ile günlük öğün hatırlatıcısı (12:00 ve 18:00) ve en sonda EAS Build ile Android APK üretip emülatörde çalıştırma. Sonuç: tüm kodun bir `steps.md` dosyasında numaralı adımlar (1–52+) halinde verildiği, Expo'nun temel kavramlarının (View/Text/ScrollView/Pressable/TouchableOpacity, Stack/Tabs, useFocusEffect, StyleSheet, platform kontrolü) tek projede öğretildiği, Play Store'a gönderilebilir AAB + yüklenebilir APK üreten çalışan bir uygulama. Video Claude Code kullanmıyor; değeri bizim için "AI'ye verilecek doğru RN/Expo iskeleti ve komut zinciri" referansı olması.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Açıklama dosyasında link yok ("NA"); videoda sözü geçen kaynaklar: GitHub repo adı **macrozone** (final kod + `steps.md` adım dosyası + diyagram PDF'i), açıklamada olacağı söyleniyor ama transkript dışı.
- expo.dev hesabı (ücretsiz) → "New project" → 4 komut (EAS CLI kurulumu, create-expo-app, `eas init --id`).
- `eas-cli` (global): `sudo npm install -g eas-cli`
- `npx create-expo-app@latest --template default@sdk-55` (SDK 55: native tabs API, yenilenmiş tasarım, `src/` klasör yapısı)
- Expo Router (`Stack`, `Tabs`, `Link`, `router.push`, `useFocusEffect`)
- `@expo/vector-icons` (tab ikonları, share/copy ikonları)
- `expo-device` (cihaz bilgisi: `Device.modelName`, `Device.brand`, `Device.osVersion`)
- `@react-native-async-storage/async-storage`
- `expo-haptics`
- React Native `Share` API (core)
- `expo-clipboard`
- `expo-notifications`
- `Switch` (RN core) – hatırlatıcı toggle
- iOS Simulator (Xcode, sadece macOS), Android Studio → Virtual Device Manager → Pixel 9 Pro emülatörü
- Expo Go (SDK 55'te Android'de Expo Go, iOS'ta TestFlight sürümü üzerinden)
- EAS Build (cloud), `eas.json` profilleri (development / preview / production), EAS Submit, EAS Workflows (CI/CD) sözle anlatıldı.
- VS Code; Cmd+D ile dev menu; `?` ile Expo CLI kısayolları (a / i / w / r)

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)

### Bölüm 0 — Teori (00:01:04–00:11:25)
1. [00:01:42] React Native nedir: tek kod tabanıyla iOS+Android; React core aynı, `react-dom` yerine `react-native` renderer.
2. [00:05:33] Yeni mimari: JS tarafı Hermes'te çalışır, JSI (C++ arayüzü) ile native'e geçer; Fabric UI'yi, Turbo Modules native API'leri (kamera, storage) yönetir. Eski bridge/JSON yok.
3. [00:08:04] Expo = "React Native'in Next.js'i": Expo Router (dosya tabanlı), Expo Go, EAS (cloud build, OTA update, submit).
4. [00:10:08] Kurallar: iOS simülatör sadece Mac+Xcode; Android emülatör her platformda; web (react-native-web) native API'lerde çalışmaz.

### Bölüm 1 — Proje kurulumu (00:11:30–00:23:58)
5. [00:11:48] Repo `macrozone` ve `steps.md`: tüm komutlar/kod örnekleri numaralı; video boyunca bu numaralar kopyalanıyor.
6. [00:12:19] Hedef ekranlar: Dashboard (4 makro kartı + hedef + son öğünler + Share / Copy Summary / Meal Reminders), Add Meal formu, All Meals listesi.
7. [00:13:18] expo.dev'de hesap aç → New project → ad `macrozone` → verilen 4 komut.
8. [00:14:56] `sudo npm install -g eas-cli`
9. [00:15:16] `npx create-expo-app@latest --template default@sdk-55` → proje adı `macrozone`.
10. [00:16:07] `cd macrozone` → `eas init --id <expo.dev'den gelen id>` → "project successfully linked".
11. [00:16:50] `package.json` incelemesi: React 19, `react-dom`, `react-native-web`, expo-router, splash screen, status bar paketleri; `src/app/` içinde `index.tsx`, `explore.tsx`, `_layout.tsx`.
12. [00:18:48] `npm start` → QR kod (Expo Go), `w` web (localhost:8081), `i` iOS simülatör, `a` Android, `r` reload, `?` tüm kısayollar.
13. [00:21:00] Cmd+D → dev tools (reload, element inspector, performance monitor, fast refresh). Köşedeki dişli ikonunu kapatıyor.
14. [00:21:53] Android Studio → More actions → Virtual Device Manager → "+" → Pixel 9 Pro → Play → terminalde `a`.
15. [00:22:52] `npm run reset-project` → "move existing files to /example?" sorusuna **No** → örnek dosyalar silinir; `npm start` yeniden.

### Bölüm 2 — Temel bileşenler ve stil (00:23:58–00:36:00)
16. [00:24:20] reactnative.dev/docs/components: `View` (container, flexbox), `ScrollView` (taşan içerik için), `Text`, `Image`, `Modal`, `Button`, `Pressable` (onPressIn/Out/LongPress), `TouchableOpacity` (basınca solma efekti).
17. [00:26:56] steps #9: `Platform.OS` ile "Running on iOS/Android" metni.
18. [00:27:43] steps #10: `expo-device` ile `Device.modelName`, `Device.brand`, `Device.osVersion`. Bileşen adı `IndexScreen` (`<Ad>Screen` kuralı).
19. [00:28:30] Inline style örneği (`style={{ flex:1, justifyContent, alignItems, backgroundColor:'red' }}`) → camelCase CSS.
20. [00:29:32] `StyleSheet.create({ container, title, date })` ile yerel stiller.
21. [00:30:40] `src/app/_layout.tsx`: `<Stack screenOptions={{ headerTitle: 'Macro Zone' }} />` sonra `headerShown: false`.
22. [00:32:00] **Global stil sistemi**: `src/styles/global.ts` → `export const colors = { background, text, textSecondary, primary, border … }` + `export const globalStyles = StyleSheet.create({ container, title, sectionTitle, empty, header })` (steps #15).
23. [00:33:07] `index.tsx`'te `globalStyles.container`, `globalStyles.title` kullan; sayfaya özel `styles.date` yerel kalır.
24. [00:34:12] `src/components/HomeHeader.tsx` (steps #17): `new Date().toLocaleDateString()` ile tarih; `globalStyles.header` + yerel `date` stili.

### Bölüm 3 — Routing: Stack, Link, Tabs (00:36:03–00:46:40)
25. [00:36:26] `src/app/meals.tsx` (steps #19): `ScrollView` + `globalStyles.container/title`, "All Meals".
26. [00:37:20] `_layout.tsx`: `<Stack><Stack.Screen name="index"/><Stack.Screen name="meals"/></Stack>` (name = dosya adı).
27. [00:38:14] steps #21: `import { Link } from 'expo-router'` → `<Link href="/meals">Go to meals</Link>` (geçici, sonra silindi).
28. [00:39:07] steps #22: `screenOptions={{ headerStyle:{ backgroundColor: colors.background }, headerTintColor: colors.text }}`; `index` için `options={{ headerShown:false, title:'Home' }}` (geri butonu "index" yerine "Home" yazsın), `meals` için `options={{ title:'Meals' }}`.
29. [00:41:08] `src/app/add-meal.tsx` (steps #23) + `<Stack.Screen name="add-meal" options={{ title:'Add Meal' }} />`.
30. [00:42:30] **Tabs grubu**: `src/app/(tabs)/` klasörü oluştur; `index.tsx`, `meals.tsx`, `add-meal.tsx` içine taşı (root `_layout.tsx` dışarıda kalır).
31. [00:43:36] `src/app/(tabs)/_layout.tsx` (steps #26): `import { Tabs } from 'expo-router'`, `Ionicons` from `@expo/vector-icons`; `TabLayout` fonksiyonu; `<Tabs screenOptions={{ headerShown:false, tabBarStyle:{ backgroundColor: colors.background, borderTopColor: colors.border }, tabBarActiveTintColor: colors.primary, tabBarInactiveTintColor: colors.textSecondary }}>`; her `Tabs.Screen` için `name`, `title`, `tabBarIcon: ({color,size}) => <Ionicons name="..." color={color} size={size}/>`.
32. [00:45:52] Root `_layout.tsx` sadeleşir: `<Stack screenOptions={{ headerShown:false }}><Stack.Screen name="(tabs)" /></Stack>`.

### Bölüm 4 — UI bileşenleri (00:46:43–00:56:10)
33. [00:46:56] `src/components/MacroCard.tsx` (steps #28): `type MacroCardProps = { label; value; goal; color }`; her kart farklı kenarlık rengi; yerel StyleSheet.
34. [00:48:46] `src/components/MacroGrid.tsx` (steps #29): 4 adet `MacroCard` (Calories/Protein/Carbs/Fat) sabit değerlerle.
35. [00:49:51] `(tabs)/index.tsx`'e `MacroGrid` eklenir; import yolu iki seviye yukarı `../../components/...`; kök `View` → `ScrollView`.
36. [00:51:02] `src/components/MealItem.tsx` (steps #31): isim + kalori/protein/karb/yağ satırı; kendi `container` stili (globalden farklı).
37. [00:52:26] `src/components/RecentMeals.tsx` (steps #32): `globalStyles.sectionTitle` + 3 sabit `MealItem`; `index.tsx`'e eklenir (#33).
38. [00:53:36] `(tabs)/add-meal.tsx` formu (steps #34): `TextInput` + `TouchableOpacity`; `useState` ile name/calories/protein/carbs/fat (controlled); `handleAddMeal` şimdilik `console.log`.

### Bölüm 5 — AsyncStorage ile veri katmanı (00:56:14–01:17:36)
39. [00:56:26] `npx expo install @react-native-async-storage/async-storage` (npm install yerine **`npx expo install`**: SDK ile uyumlu sürümü seçer).
40. [00:57:19] Mantık: `AsyncStorage.setItem(key, JSON.stringify(value))` / `JSON.parse(await AsyncStorage.getItem(key))`; web'de localStorage'a düşer.
41. [00:58:35] `src/storage/meals.ts` (steps #36): `type Meal`, `const MEALS_KEY = 'meals'`, `getMeals(): Promise<Meal[]>` (yoksa `[]`), `addMeal(meal)` → `id: Date.now().toString()`, `createdAt: new Date().toISOString()`, tüm diziyi yeniden yazar, yeni öğünü döner.
42. [01:02:23] Form bağlama (steps #38): `Alert` (RN) ile doğrulama ("name ve calories zorunlu") ve başarı mesajı; `addMeal({...Number(...)})`; state temizle; `router.push('/')`.
43. [01:05:06] `(tabs)/index.tsx` (steps #39): `const [meals, setMeals] = useState<Meal[]>([])`, `loadMeals()` → `getMeals()`; **`useFocusEffect(useCallback(() => { loadMeals() }, []))`** — `useEffect` yerine; böylece sekmeye dönünce liste yenilenir.
44. [01:07:29] `RecentMeals` prop `meals` alır; `meals.length === 0` → "No meals logged" (`globalStyles.empty`); `meals.slice(0,5).map(m => <MealItem .../>)` (steps #40).
45. [01:08:56] `MacroGrid` prop `meals` alır; `reduce` ile `totals = { calories, protein, carbs, fat }`; kartlara `value={totals.x}` (steps #41).
46. [01:11:08] Silme (steps #42–45): `storage/meals.ts`'e `deleteMeal(id)` (filter + setItem). `MealItem`'da `onLongPress → handleLongPress` → `Alert.alert('Delete meal?', 'Are you sure…', [{text:'Cancel', style:'cancel'}, {text:'Delete', style:'destructive', onPress: async () => { await deleteMeal(id); onDelete?.() }}])`. `RecentMeals` → `onDelete` prop'u geçer; `index.tsx`'te `<RecentMeals meals={meals} onDelete={loadMeals} />`.
47. [01:15:06] `(tabs)/meals.tsx` (steps #46): aynı `getMeals`+`useFocusEffect` deseni, tüm öğünleri `MealItem` ile listeler, `onDelete={loadMeals}`.
48. [01:16:14] `clearAllMeals()` → `AsyncStorage.removeItem(MEALS_KEY)` (steps #47); `meals.tsx`'te "Clear All" `TouchableOpacity` (`clearButton: { color:'red', fontSize:16 }`).

### Bölüm 6 — Expo API'leri: haptics, share, clipboard, notifications (01:17:53–01:31:45)
49. [01:18:09] `npx expo install expo-haptics` → `import * as Haptics from 'expo-haptics'`; `add-meal.tsx`'te başarı Alert'inden sonra, `router.push` öncesi `Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success)`; `MealItem`'da `deleteMeal` sonrası, `onDelete` öncesi aynı çağrı. (Simülatörde titreşim test edilemez; Expo Go/fiziksel cihazda çalışır.)
50. [01:20:37] `src/components/ShareButton.tsx` (steps #50): `import { Share } from 'react-native'`; `meals` prop; `reduce` ile toplamlar; `Share.share({ message: 'Macro Zone Daily Summary\nCalories: …\nProtein: …\n… Meals logged: N' })`; ikon + `TouchableOpacity`. `index.tsx`'te başlık `Text`'i bir `View` içine alınıp altına `<ShareButton meals={meals}/>`. Test: iOS Reminders'a paylaşıldı.
51. [01:23:30] `npx expo install expo-clipboard` → `src/components/CopyButton.tsx` (steps #51): `Clipboard.setStringAsync(summary)` + haptics + `Alert('Macro summary copied to clipboard')`; `index.tsx`'te `MacroGrid` altına `<CopyButton meals={meals}/>`.
52. [01:25:47] `npx expo install expo-notifications` (SDK 53+ Expo Go'da çalışmaz; simülatör ve build'de çalışır).
53. [01:26:32] `src/utils/notifications.ts` (steps #52): `Notifications.setNotificationHandler({ handleNotification: async () => ({ shouldShowAlert:true, shouldShowBanner:true, shouldShowList:true, shouldPlaySound:true, shouldSetBadge:false }) })`; `requestPermissions()` → `Notifications.requestPermissionsAsync()`; `scheduleMealReminders()` → iki `scheduleNotificationAsync({ content:{ title:'Macro Zone', body:"Don't forget to log your lunch" }, trigger:{ type: Notifications.SchedulableTriggerInputTypes.DAILY, hour:12, minute:0 } })` ve `hour:18` ("Time to log your dinner"); `cancelMealReminders()` → `cancelAllScheduledNotificationsAsync()`.
54. [01:28:27] `src/components/ReminderToggle.tsx`: `REMINDERS_KEY = 'reminders_enabled'` AsyncStorage'da saklanır; `useState(false)`; `toggle(value)` → açılırsa `requestPermissions()` + `scheduleMealReminders()`, kapanırsa `cancelMealReminders()`; UI: `<Switch value={enabled} onValueChange={toggle} trackColor={{ true: colors.primary, false: colors.border }} />`. `index.tsx`'te `CopyButton` altına.
55. [01:30:52] Telefonda gerçek bildirimler gösterildi (öğle/akşam hatırlatıcıları günlük geliyor).

### Bölüm 7 — EAS Build / yayın (01:32:05–01:38:13)
56. [01:32:38] `eas build --platform all|ios|android` (global kurulduysa `npx` gerekmez). iOS için **ücretli Apple Developer hesabı ($99/yıl)** şart; Brad'in hesabı "enrolled" görünmediği için iOS build alamadı, Xcode ile telefona yükledi.
57. [01:33:17] Android: ücretsiz; varsayılan çıktı **AAB** (Play Store'a gönderilecek format). Cihaza doğrudan yüklenebilir **APK** için `eas.json`:
    ```json
    {
      "build": {
        "preview": { "distribution": "internal", "android": { "buildType": "apk" } },
        "production": { "autoIncrement": true }
      }
    }
    ```
58. [01:34:36] `eas build --platform android --profile preview` → "Uploaded to EAS", build kuyruğa alınır; expo.dev → proje → Builds/Recent activity'de loglar; ~10 dk sürdü.
59. [01:35:44] Bitince CLI "Install and run the Android build on the emulator?" → Yes → Pixel 9 emülatöründe gerçek APK (dev server yok) çalışır; splash screen görünür; öğün ekleme, share sheet, kopyalama, bildirim izni ("Allow notifications") test edildi.
60. [01:37:27] Daha önce `production` profiliyle AAB da üretilmişti; Play Store'a **`eas submit`** ile gönderilebilir. **EAS Workflows** ile GitHub'a push → otomatik build + store'a submit (CI/CD) kurulabilir (sözle).

## Kullandığı prompt'lar (varsa aynen)
Video yapay zekâ/prompt kullanmıyor (bilerek "AI'den önce temelleri öğren" yaklaşımı). Tek istisna: [01:18:56] editör AI autocomplete'in `Haptics.notificationAsync` satırını önermesi, Tab ile kabul edildi.

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Tek renk kaynağı**: `src/styles/global.ts` içinde `colors` objesi (background, text, textSecondary, primary, border) + `globalStyles` (container, title, sectionTitle, empty, header). Tema değişikliği tek yerden. Tab bar, header, Switch trackColor hep bu objeden besleniyor.
- **Yerel vs global stil ayrımı**: Yalnızca tekrar eden stiller global; `date`, `MacroCard` stilleri bileşen içinde kalıyor.
- **Kart tabanlı dashboard**: 4 `MacroCard` kartı, her biri farklı kenarlık rengiyle (`color` prop) → görsel ayrım.
- **Native his**: Buton olarak `Button` yerine `TouchableOpacity` ("basınca solar, daha native"); `Pressable`'ın onPressIn/Out/LongPress durumları anlatıldı.
- **Haptics**: Başarıyla ekleme, silme ve kopyalamada `NotificationFeedbackType.Success` titreşimi.
- **Header stratejisi**: Ana ekranda header gizli (`headerShown:false`), iç ekranlarda renkli header + geri butonu etiketi (`title:'Home'`), tab'lara geçince header tamamen kapatıldı.
- **Tab bar**: `tabBarStyle` background + `borderTopColor`, `tabBarActiveTintColor`/`tabBarInactiveTintColor`, Ionicons ikonlar.
- **Silme için destructive Alert**: `style:'destructive'` ile kırmızı buton (iOS native), long-press ile tetikleniyor.
- **Boş durum**: "No meals logged" metni (`globalStyles.empty`).
- Not: Animasyon kütüphanesi (Reanimated vb.) **yok**; Alert'ler RN'in ham `Alert.alert`'i — yani bizim şikâyet ettiğimiz "ham pop-up" görünümü bu videoda da var.

## Hatalar ve çözümleri
- [00:22:52] `npm run reset-project` sonrası simülatör güncellenmedi → dev server durmuştu; `npm start` + `i` yeniden.
- [00:31:27] `screenOptions={ headerTitle }` tek süslü parantezle yazılınca hata → `screenOptions={{ headerTitle: 'Macro Zone' }}` (çift süslü).
- [00:40:06] Geri butonu "index" yazıyor → `index` ekranına `options={{ title:'Home' }}`.
- [00:50:13] `(tabs)/` içine taşıyınca `HomeHeader` bulunamadı → import yolu `../components` → `../../components`.
- [00:50:30] Kök `View` ile içerik kesiliyor → `ScrollView`.
- [01:01:57] Form submit log'u görünmedi → uygulama reload edildi, sonra log geldi.
- [01:06:31] `useEffect` ile yüklenince sekmeye dönüşte liste güncellenmiyor → `useFocusEffect(useCallback(loadMeals, []))`.
- [01:19:07] Haptics satırı yapıştırma ilk seferde "take etmedi" → tekrar yazıldı.
- [01:25:47] `expo-notifications` SDK 53+ Expo Go'da çalışmıyor → simülatör ya da EAS build ile test.
- [01:32:55] Apple Developer hesabı "enrolled" görünmediği için iOS EAS build alınamadı → Xcode ile cihaza kurdu; iOS yayın için ücretli hesap şart.
- [01:34:18] `eas.json`'a yapıştırırken fazladan `}` → düzeltildi.
- [01:35:44] EAS Android build ~10 dk sürdü (beklenen).

## Bizim fabrikaya alınacaklar (somut, maddeli)
1. **Kurulum zinciri standardı** (CLAUDE.md "Kurulum" bölümüne): `npm i -g eas-cli` → `npx create-expo-app@latest --template default@sdk-55` → `eas init --id <id>` → `npm run reset-project` (No) → `npx expo install <paket>` (asla `npm install`). Fabrikada proje açılır açılmaz expo.dev projesine bağlanmış olsun.
2. **Dosya mimarisi şablonu**: `src/app/(tabs)/_layout.tsx` + `src/app/_layout.tsx` (Stack → `(tabs)`), `src/components/`, `src/styles/global.ts` (`colors` + `globalStyles`), `src/storage/*.ts` (AsyncStorage handler: get/add/delete/clearAll), `src/utils/notifications.ts`. Claude Code'a bu iskeleti CLAUDE.md'de zorunlu kıl.
3. **Tema token kuralı**: Hiçbir bileşende hard-coded renk olmayacak; `colors.*` zorunlu. Tab bar, header, Switch, kenarlıklar token'dan beslenecek (video bunu tutarlı yapıyor).
4. **`useFocusEffect` kuralı**: Tab/stack arası dönüşlerde veri yenilenmesi için `useEffect` değil `useFocusEffect(useCallback(...))`. "Geri dönünce liste güncellenmiyor" hatasını baştan engeller.
5. **Haptics zorunlu**: Her başarılı ekleme/silme/kopyalama/önemli aksiyonda `expo-haptics` (`NotificationFeedbackType.Success`, hatalarda `Error`, seçimlerde `selectionAsync`). Ucuz ama "native his" veriyor.
6. **Alert yasağı → özel bileşen**: Video RN `Alert.alert` kullanıyor ve bu tam olarak kullanıcının şikâyet ettiği "Android ham pop-up". Fabrikada: doğrulama hataları için inline hata metni + toast; onay için kendi `ConfirmSheet`/bottom sheet bileşeni; `Alert.alert` sadece destructive onaylarda ve iOS'ta kabul edilebilir — Android'de kesinlikle özel modal.
7. **Buton standardı**: `Button` bileşeni yasak; `Pressable` (pressed state ile scale/opacity) ya da en az `TouchableOpacity`. Long-press = silme onayı deseni iyi, alınabilir.
8. **Boş durum (empty state) her listede zorunlu**: ikon + kısa metin + CTA (video sadece metin veriyor; biz ikon ve CTA ekleyelim).
9. **Expo API seti varsayılan "ekstra değer" paketi**: `expo-clipboard` (özet kopyala), RN `Share` (paylaş), `expo-notifications` (günlük hatırlatıcı, `SchedulableTriggerInputTypes.DAILY`), `expo-device`/`Platform.OS` (platforma özel UI). İşletme uygulamalarında "randevu hatırlatıcı", "kampanya paylaş" gibi özelliklere direkt eşleniyor.
10. **Bildirim tercihinin kalıcılığı**: toggle durumu AsyncStorage'da (`reminders_enabled`) saklanacak; açılışta okunacak; açılırken izin iste, kapanırken `cancelAllScheduledNotificationsAsync`.
11. **EAS yayın adımları (fabrika "Yayın" aşamasına)**:
    - `eas.json`: `preview` (internal, Android `buildType: apk` — müşteriye hızlı demo için APK) + `production` (`autoIncrement: true`, AAB).
    - `eas build --platform android --profile preview` → müşteri demosu; `--profile production` → `eas submit -p android` ile Play'e.
    - iOS build/submit için önceden ücretli Apple Developer hesabının **enrolled** olduğunu doğrula (video burada takıldı).
    - Build ~10 dk; CLI'nin "install on emulator" teklifini kabul edip son kontrol yap.
    - İleri aşama: EAS Workflows ile push → build → submit CI/CD.
12. **Test matrisi notu**: Haptics ve push bildirimleri simülatörde test edilemez/ Expo Go'da (SDK 53+) notifications çalışmaz → fabrikada bu özellikler için `preview` APK ya da dev build üzerinden test adımı planlansın.
13. **"steps.md" fikri**: Her üretilen projeye numaralı adım dosyası (komutlar + dosya içerikleri) eklensin; hem müşteriye teslim dokümanı hem Claude Code için tekrar üretilebilir reçete olur.
