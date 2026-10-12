# Custom Animated Bottom TabBar in React Native with Expo Router | React Native for Beginners — Pradip Debnath — 27 dk — https://www.youtube.com/watch?v=GrLCS5ww030

## Tek paragraf: ne yapıyor, sonuç ne
Expo Router'ın `Tabs` bileşenine `tabBar` prop'u ile tamamen özel bir alt sekme çubuğu yazıyor. Çubuk yüzer görünümlü: absolute konumda, `bottom: 50`, beyaz arka plan, `borderRadius: 35`, gölgeli. Üç sekmesi var (Home, Explore, Profile; ikonlar Feather). Animasyon Reanimated ile iki katmanda yapılıyor. (1) Her butonda `withSpring` ile 0'dan 1'e giden bir `scale` shared value var: etiket opacity 1'den 0'a iniyor, ikon 1'den 1.2'ye büyüyüp `top` 0'dan 9'a kayıyor. (2) Çubukta aktif sekmenin arkasında, `translateX = buttonWidth * index` ile spring'le kayan dairesel bir arka plan var. Sonuç: aktif sekmede yalnızca büyük ikon ve renkli yuvarlak arka plan görünüyor, diğerlerinde ikon ve etiket.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Expo + Expo Router (`Tabs`, `Tabs.Screen`)
- `@react-navigation/bottom-tabs`: `BottomTabBarProps` tipi ve dokümandaki `tabBar` örnek bileşeni (React Navigation Bottom Tabs dokümanı)
- `react-native-reanimated` (Expo projesinde varsayılan kurulu): `useSharedValue`, `useAnimatedStyle`, `withSpring`, `interpolate`, `Animated.View`, `Animated.Text`
- `@expo/vector-icons` Feather: `home`, `compass`, `user`
- React Native: `Pressable`, `StyleSheet`, `LayoutChangeEvent`
- Açıklamadaki linkler:
  - Learn TypeScript for ReactJS: https://youtu.be/WApd-GNOWJM
  - Expo Router Tutorial Playlist: https://www.youtube.com/playlist?list=PLQWFhX-gwJbluCKdCPVYnfwVJlBDVf9E-
  - Finance App UI: https://youtu.be/MLVxvof54KA
  - Travel App UI: https://youtu.be/lIQIeIdUXFI
  - Social App with Firebase: https://www.youtube.com/playlist?list=PLQWFhX-gwJbmrCwksjn77tdl36dIWPFAt
  - React Navigation v6 playlist: https://www.youtube.com/playlist?list=PLQWFhX-gwJbmtZY4mcRE-k7hL9DigcGS-
  - Food Finder App: https://youtube.com/playlist?list=PLQWFhX-gwJbkETb0lfml7W8VvZb-Hc8oK
  - GitHub: https://github.com/itzpradip
  - Blog: http://www.pradipdebnath.com/blog/

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
**Kurulum**
1. [00:01:27] Yeni Expo projesindeki gereksiz dosyalar siliniyor. Stack'ten not-found kaldırılıyor.
2. [00:01:56] `app/(tabs)/_layout.tsx` oluşturuluyor (`TabLayout`): `import { Tabs } from 'expo-router'`, sonra `<Tabs><Tabs.Screen name="index" options={{ title: 'Home' }} />...`. Sayfalar `index.tsx`, `explore.tsx` ve `profile.tsx`; stil `flex: 1, justifyContent: 'center', alignItems: 'center'`. Crash olunca server yeniden başlatılıyor.
3. [00:03:23] Başlıklar ayarlanıyor: Home, Explore, Profile.

**Özel TabBar tasarımı**
4. [00:04:05] `screenOptions` yerine sınırsız özelleştirme için `tabBar` prop'u seçiliyor. React Navigation dokümanındaki örnek bileşen kopyalanıp `components/TabBar.tsx` dosyasına yapıştırılıyor. Prop tipi `BottomTabBarProps` (`@react-navigation/bottom-tabs`). Kullanım: `<Tabs tabBar={props => <TabBar {...props} />}>`.
5. [00:06:50] Unique key uyarısı `key={route.name}` ile çözülüyor.
6. [00:07:41] Stiller `tabbar: { position: 'absolute', bottom: 50, flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#fff', marginHorizontal: 80, paddingVertical: 15, borderRadius: 35, shadowColor: 'black', shadowOffset: { width: 0, height: 10 }, shadowRadius: 10, shadowOpacity: 0.1 }`. Değerler transkriptte söylendiği gibi; `paddingVertical: 15` tahmin, transkriptte yalnızca "padding" deniyor.
7. [00:08:42] Item stili: `tabbarItem: { flex: 1, justifyContent: 'center', alignItems: 'center', gap: 5 }`. İkon: `<Feather name="home" size={24} color={...} />`.
8. [00:09:41] Route adına göre bir ikon map'i yazılıyor: `icon = { index: (props: any) => <Feather name="home" size={24} {...props} />, explore: ...'compass', profile: ...'user' }`. Kullanımı `icon[route.name]({ color })`.

**Buton bileşenine ayırma**
9. [00:11:41] `components/TabBarButton.tsx`: `TouchableOpacity` yerine `Pressable`. Prop'lar `onPress`, `onLongPress`, `isFocused: boolean`, `routeName: string`, `color: string` ve `label: string`. İkon map'i `constants/icon.tsx` dosyasına taşınıp export ediliyor.

**Buton animasyonu (Reanimated)**
10. [00:14:07] `const scale = useSharedValue(0)`.
11. [00:14:50] `useEffect(() => { scale.value = withSpring(typeof isFocused === 'boolean' ? (isFocused ? 1 : 0) : isFocused, { duration: 350 }) }, [scale, isFocused])`.
12. [00:16:12] Etiket: `animatedTextStyle = useAnimatedStyle(() => ({ opacity: interpolate(scale.value, [0, 1], [1, 0]) }))` ve `<Animated.Text style={[{ color, fontSize: 12 }, animatedTextStyle]}>`. Aktif sekmede etiket kayboluyor.
13. [00:17:15] İkon: `animatedIconStyle = useAnimatedStyle(() => ({ transform: [{ scale: interpolate(scale.value, [0, 1], [1, 1.2]) }], top: interpolate(scale.value, [0, 1], [0, 9]) }))`. İkon `Animated.View` ile sarılıyor. Aktifken 1.2 kat büyüyüp 9 px aşağı iniyor ve ortalanıyor.
14. [00:19:13] Etiket `fontSize: 12`.

**Kayan dairesel arka plan (TabBar.tsx)**
15. [00:20:12] `const [dimensions, setDimensions] = useState({ height: 20, width: 100 })` (sayılar tahmin, transkriptte "some initial value" deniyor). Gerçek ölçü `onLayout` ile geliyor.
16. [00:21:14] `const buttonWidth = dimensions.width / state.routes.length`. Sekme sayısından bağımsız, dinamik.
17. [00:21:50] `const onTabbarLayout = (e: LayoutChangeEvent) => setDimensions({ height: e.nativeEvent.layout.height, width: e.nativeEvent.layout.width })`, sarmalayıcı View'da `onLayout={onTabbarLayout}`.
18. [00:22:14] `const tabPositionX = useSharedValue(0)` ve `animatedStyle = useAnimatedStyle(() => ({ transform: [{ translateX: tabPositionX.value }] }))`.
19. [00:23:16] `onPress` içinde: `tabPositionX.value = withSpring(buttonWidth * index, { duration: 1500 })`. Transkriptte "500" ya da "1500" diye anlaşılıyor, kayma için 500 ms makul.
20. [00:24:11] Arka plan: `<Animated.View style={[animatedStyle, { position: 'absolute', backgroundColor: '#723FEB', borderRadius: 30, marginHorizontal: 12, height: dimensions.height - 15, width: buttonWidth - 25 }]} />`. Renk transkriptte "this color" diye geçiyor, mor tonlu bir marka rengi.
21. [00:25:26] Hata: önce `width: dimensions.width - 25` verilmiş, arka plan tüm çubuğa yayılıyor. `buttonWidth - 25` ile düzeltiliyor.
22. [00:25:50] Aktif sekmenin ikon rengi beyaz (`isFocused ? '#FFF' : '#222'`), böylece renkli dairenin üstünde okunuyor.
23. [00:26:34] Özet: `index * buttonWidth` ile translateX, `withSpring` ile yaylı (bouncy) geçiş.

## Kullandığı prompt'lar (varsa aynen)
Prompt yok. Video elle kodlama eğitimi.

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Yüzer (floating) tab bar:** `position: 'absolute'`, `bottom: 50`, `marginHorizontal: 80`, `borderRadius: 35` ve yumuşak gölge (`shadowOpacity: 0.1`, `shadowRadius: 10`, `height: 10` offset).
- **Kayan aktif gösterge:** sekmeler arasında `withSpring` ile kayan yuvarlak renkli arka plan. Genişliği `onLayout` ile ölçüldüğü için her sekme sayısına uyuyor.
- **İkon/etiket koreografisi:** aynı `scale` değerinden üç interpolation türetiliyor (etiket opacity 1→0, ikon scale 1→1.2, ikon top 0→9). Hepsi tek bir spring'le senkron hareket ediyor.
- Aktif ikonu beyaza çevirerek renkli dairenin üstünde kontrast sağlıyor.

## Hatalar ve çözümleri
- `_layout` kurulunca uygulama crash oldu. Çözüm: server'ı durdurup yeniden başlatmak.
- Map'te unique key uyarısı. Çözüm: `key={route.name}`.
- Arka plan tüm tab bar'a yayıldı. Çözüm: `width: dimensions.width - 25` yerine `buttonWidth - 25`.
- `TabBarButton`'a taşırken `key` iç bileşende geçersiz kaldı. Çözüm: `key`'i dıştaki map'te vermek.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Şablona hazır bir `components/TabBar.tsx` + `TabBarButton.tsx` + `constants/icon.tsx` üçlüsü ekle: floating tab bar, `onLayout` ile ölçülen kayan aktif gösterge ve `withSpring` animasyonları. Marka rengi tema token'ından gelsin (sabit `#723FEB` olmasın).
- CLAUDE.md kuralı: "Alt navigasyonu varsayılan `Tabs` görünümüyle bırakma. `tabBar={props => <TabBar {...props} />}` ile özel ve animasyonlu tab bar kullan."
- Animasyon kalıbı: tek bir `useSharedValue(0→1)` + `interpolate` ile birden çok prop'u (opacity, scale, translate) senkron sür. Bunu bileşen yazım rehberine ekle.
- Yeni stack'te geliştirme: `withSpring` ile birlikte `{ duration }` yerine `damping`/`stiffness` gibi fiziksel ayarları dene. İkon basma geri bildirimi için `expo-haptics` ekle (videoda yok, bizim eklememiz).
- `position: 'absolute'` tab bar ekran içeriğini örttüğü için ekranlara alt padding (ör. `useSafeAreaInsets().bottom` + tab bar yüksekliği) kuralı koy (videoda değinilmiyor).
- Android gölgesi için `elevation` ekle. Videodaki `shadow*` prop'ları yalnızca iOS'ta çalışır (bizim notumuz).
