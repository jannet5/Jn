# This New React Native Animation Library Is Insane — Code with Beto — 9 dk — https://www.youtube.com/watch?v=oAIqn44Y63c

## Tek paragraf: ne yapıyor, sonuç ne
Beto, App&Flow ekibinin (React Native Safe Area Context'in yapımcıları; yazar Yannick) çıkardığı **React Native Ease** (`react-native-ease`) kütüphanesini tanıtıyor. Ease, CSS transition'larından esinlenen bildirimsel bir API sunuyor: JS animasyon döngüsü, worklet ya da shared value yok. Animasyonu iOS'ta Core Animation, Android'de Animator çalıştırıyor, yani UI thread takılsa bile animasyon akıcı kalıyor. Video, kendi Platano şablonunda iki gerçek Reanimated animasyonunu (check/circle ikonu cross-fade ve onboarding "congratulations" ekranındaki kademeli giriş) `EaseView`'e taşıyarak koddaki sadeleşmeyi gösteriyor. Vardığı sonuç şu: basit giriş/çıkış ve state değişimi animasyonlarında Ease, gesture, layout ve karmaşık interpolation gerektiren işlerde Reanimated.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- React Native Ease: https://github.com/appandflow/react-native-ease
- App&Flow (ajans, kütüphanenin yapımcısı): https://appandflow.com?utm_source=beto&utm_medium=youtube
- Platano (Beto'nun satışa hazır AI image-wrapper şablonu, demo kod tabanı): https://cwb.sh/platano?r=yt
- React Native Reanimated (karşılaştırma için; `useSharedValue`, `useAnimatedStyle`, `entering={FadeDown}`)
- Native API'ler: iOS Core Animation, Android Animator
- Paket yöneticisi: `bun`, ayrıca `npx expo install`
- Claude (stagger koduna Claude yazdırıldı)
- Beto'nun RN kursu (Reanimated + Gesture Handler bölümü): https://cwb.sh/rn

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:38] Ease'i tanıtıyor: "zero JavaScript overhead", CSS transition'a benzeyen API. İşi animasyonu bildirmek, gerisini işletim sistemi hallediyor.
2. [00:01:19] Duyuru blog yazısını inceliyor: App&Flow ve yazar Yannick.
3. [00:02:13] Nasıl çalıştığını anlatıyor: iOS'ta Core Animation, Android'de Animator. Demo: UI thread lag'liyken Reanimated animasyonu takılıyor, Ease akıcı kalıyor.
4. [00:03:08] "Neden Reanimated değil?" sorusu: Reanimated her frame'de UI thread'de JS çalıştırıyor, basit animasyon için bu fazla (overkill). Ease Reanimated'ın yerine geçmiyor.
5. [00:03:59] Desteklenenler: **opacity, transform, backgroundColor, borderRadius** (layout dışı prop'lar). Non-goal'lar: gesture-driven animasyon, layout animasyonu (width/height), shared element transition, eski mimari (old architecture).
6. [00:04:31] Kurulum:
   ```bash
   bun install react-native-ease
   # veya
   npx expo install react-native-ease
   ```
   Ardından iOS ve Android için prebuild ve yeniden derleme gerekiyor (native modül, Expo Go'da çalışmaz): `npx expo prebuild`, sonra `npx expo run:ios` ve `npx expo run:android`. Bundan sonra dev server başlatılıyor.
7. [00:05:42] Örnek 1, cross-fade: seçim listesinde check ikonu ile circle ikonu arasında geçiş.
   - Önceki hâl: `Animated.View` + `useAnimatedStyle` + her liste öğesi için ayrı `progress` shared value (bu ekranda 4 tane).
   - Yeni hâl: `import { EaseView } from 'react-native-ease'`, sonra `<EaseView animate={{...}} transition={{ type: 'timing', ... }}>`. Hook'lar ve shared value siliniyor.
   - Görmek kolay olsun diye süreyi **1 saniyeye** çıkarıyor [00:06:58].
   - **Border color** animasyonu Ease'te henüz desteklenmediği için o kısım Reanimated'da kalıyor.
8. [00:07:14] Örnek 2, kademeli giriş (staggered reveal, onboarding "congratulations" adımı):
   - Önceki hâl: `Animated.View` + `entering={FadeDown}` (Reanimated'dan import) + `style`.
   - Yeni hâl: bir **delay dizisi** tanımlanıyor, bu dizi üzerinde dönülerek bir `revealStep` state'i artırılıyor ve her `EaseView` kendi adımına göre `animate` ile `transition` alıyor. Bu kodu Claude'a yazdırmış.
9. [00:08:21] README'deki karar tablosu: **Ease** = enter/exit, fade, slide, scale, state değişimi. **Reanimated** = pan, pinch, layout animasyonu, karmaşık interpolation. Platano'daki pinch-to-zoom Reanimated'da kalıyor.
10. [00:09:04] Hızlı geçiş: mevcut bir `Animated.View`'i `EaseView` ile sarıp aynı `style`'ı koruyor, `Animated.View` ve animasyon import'unu siliyor.

## Kullandığı prompt'lar (varsa aynen)
Transkriptte prompt'un tam metni yok. Stagger kodu için yalnızca şunu söylüyor: "basically, I just ask cloth [Claude] to do this for me". Önerilen karşılık (bizim üretimimiz, videodan alıntı değil):
```
Replace the Reanimated entering={FadeDown} animations on this screen with EaseView from react-native-ease. Use an array of delays and a revealStep state so each item fades/slides in one after another with a timing transition.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Seçim ikonunda pat diye değişim yerine **cross-fade** (check ve circle arasında opacity geçişi).
- Onboarding sonunda ögelerin **kademeli (staggered) girişi**: delay dizisiyle tek tek fade/slide.
- Animasyonlar native thread'de koştuğu için JS/UI thread meşgulken bile jank olmuyor.
- Platano Android'de "native colors" kullanıyor (Material renkleri), tek bir primary color ile tema veriliyor.
- Hibrit kullanım: basit geçişler Ease ile, border color, gesture ve zoom Reanimated ile.

## Hatalar ve çözümleri
- Border color animasyonu Ease'te desteklenmiyor. Çözüm: o prop için Reanimated'da kalmak.
- Native modül olduğu için kurulumdan sonra prebuild ve yeniden derleme şart, aksi hâlde modül bulunamıyor.
- Layout (width/height) ve gesture animasyonları Ease ile yapılamıyor. Çözüm: Reanimated + Gesture Handler.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- CLAUDE.md'ye **animasyon karar kuralı**: "Fade/slide/scale enter-exit ve state-change animasyonlarında `react-native-ease` `EaseView` kullan. Gesture (pan/pinch), layout (width/height), shared element ve karmaşık interpolation için Reanimated 4 + Gesture Handler kullan."
- Ease'in sınırlarını CLAUDE.md'ye yaz: opacity, transform, backgroundColor ve borderRadius destekleniyor. borderColor ve layout prop'ları desteklenmiyor, bunlar için Reanimated.
- Bileşen kütüphanemize hazır kalıplar ekle: `SelectableRow` (check↔circle cross-fade, `transition: { type: 'timing' }`) ve `StaggerReveal` (delay dizisi + `revealStep`, onboarding/başarı ekranları için).
- Onboarding'in son adımına standart bir "congratulations" ekranı koy: ögeler kademeli fade-down ile girsin.
- Kurulum zincirine ekle: `npx expo install react-native-ease`, sonra `npx expo prebuild`. Dev build şart (Expo Go yetmez). Bunu EAS development profile'ına bağla.
- Liste öğesi başına shared value açmaktan kaçın (performans). Basit durumlarda Ease ile bildirimsel çöz.
- Kalite kontrol: animasyonları UI thread'i bilerek yükleyerek (lag simülasyonu) test et. Native çalışan animasyonlar takılmamalı.
