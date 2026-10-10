# DERSLER — ajansimiz-app (mobil hattının ilk gerçek üretim testi)

## Mobil hattına geri bildirim (eksik / yanlış / belirsiz)
1. **YANLIŞ — skill adı:** `expo/skills` içinde `building-native-ui` yok; artık `expo-native-ui` + `expo-design-system` (native-slop.md burada) + `expo-animation` + `expo-ui`. mobil.md ve görev şablonları güncellenmeli.
2. **ÇELİŞKİ — NativeWind vs Expo skill:** `expo-native-ui` "CSS ve Tailwind desteklenmez, inline stil kullan" diyor; hat NativeWind istiyor. Karar gerekli. Bu projede: NativeWind = düzen/tipografi, renk = token dosyasından inline (K3). Hatta tek cümle olarak yazılmalı.
3. **EKSİK — NativeWind + Reanimated tuzağı:** `Animated.View` üstündeki `className` sessizce yok sayılıyor (damgalar kare, toast/sheet konumu bozuk çıktı). Çözüm: `cssInterop(Animated.View, { className: 'style' })` kökte bir kez. Hat kurulum adımına (M1) eklenmeli.
4. **EKSİK — React Compiler lint:** SDK 57 şablonu React Compiler açık geliyor; `sv.value = x` lint hatası veriyor → Reanimated'de `sv.set()/sv.get()` kullan. CLAUDE.md'ye kural olarak girmeli.
5. **EKSİK — M1 komutları güncel değil:** `bunx create expo` yerine `npx create-expo-app@latest <ad> --template default --no-install`; varsayılan şablon `src/app` + NativeTabs + örnek dosyalarla geliyor, temizleme adımı yazılmalı (components/constants/hooks/explore/scripts silinir).
6. **EKSİK — `yeni-musteri.sh mobil`:** web için `scripts/` ve Stop kancası kopyalanıyor ama mobil için hiçbir şey yok. Mobil için `qa/ss.mjs` (bu projedeki: dist'i sunar, 390×844 açık+koyu, akışları tıklar, konsol hatası + yatay taşma raporlar) şablona taşınmalı.
7. **BELİRSİZ — Expo Router + web SS:** `page.goto('/berber')` ile doğrudan açılan ekranda geri butonu yok (yığın boş). SS akışı ana ekrandan tıklayarak ilerlemeli ya da bu kabul edilmeli.
8. **BELİRSİZ — `@expo/ui` önceliği:** skill "sheet için önce @expo/ui" diyor; hat "kendi BottomSheet'in" diyor. @expo/ui web'de render etmediği için Playwright QA'sı ile çakışıyor. Kural: web SS gerekiyorsa kendi bileşen, sadece cihaz QA'sı varsa @expo/ui.
9. **EKSİK — Android emülatör:** bulut konteynerde /dev/kvm ve SDK yok; M6'daki "API 26 x86 -no-accel" talimatı burada uygulanamaz. Alternatif yazılmalı: EAS Build + eas-simulator skill'i ya da insan Expo Go testi (KAPI-4).
10. **EKSİK — tek HTML artifact (web hattı artifact.py):** Expo web export çok rotalı + 4.6 MB bundle; mobil için "artifact yok, SS + Expo Go linki" kuralı netleşmeli. EAS Update önizleme linki (M7) asıl müşteri teslimidir.
11. **YANLIŞ VARSAYIM — sabit saat SS'i:** demo veride "dolu" saatler deterministik; QA script'i sabit bir saat seçince dolu çıktı. Script'ler devre dışı öğeyi değil ilk etkin öğeyi seçmeli.
12. **EKSİK — ana DESIGN'dan mobil sapmaları:** ana marka "kart = 1px çerçeve" diyor, native-slop #9 bunu yasaklıyor. Mobil hattı "web DESIGN'ını mobile çevirirken: çerçeve→yüzey rengi, font-stretch→ağırlık, hover→basılı durum" çeviri tablosu içermeli.

## CLAUDE.md'ye eklenecekler
- Reanimated değerleri `.set()/.get()` ile; `Animated.View` className için cssInterop kurulu olmalı.
- Sheet'ler `Portal` ile kökte; birincil buton açılışta görünür (spring değil 260ms ease-out — spring yerleşmeden SS kesik çıktı).
- Haptik `lib/haptik.ts` üzerinden (web'de no-op).
