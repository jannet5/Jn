# HAT: MOBİL UYGULAMA (Expo)

Hedef: native hissi veren, animasyonlu, Android'in ham pop-up'ları olmayan uygulama. Müşteri her commit'i telefonunda görür.
Stack (X + GitHub + YouTube'da herkesin ortak seçimi): **Expo (Router, New Arch) + NativeWind/React Native Reusables + Reanimated + Gesture Handler + expo-haptics + @expo/ui / formSheet + zeego menü + sonner-native toast + FlashList** · backend gerekirse Supabase (ya da InstantDB) · ödeme RevenueCat · EAS Build/Update.
Skill'ler: **expo/skills (resmi)** · vercel-react-native-skills · emil animate-expo · frontend-design · context7 MCP · Expo MCP.
Ayrıntılı ekran-ekran süreç: `uygulama-fabrikasi` skill'i (8 aşama) — bu hat onun **üretim ve kalite katmanını** değiştirir.

## Native-slop yasakları (expo-design-system/native-slop.md'den, CLAUDE.md'ye girer)
`Alert.alert` ile onay/uyarı yok → native sheet/menü/toast · JS modal yok → `presentation: 'formSheet'` · Android `elevation` gölge yok → `boxShadow` · `headerShown:false` + elle header yok → native header / large title · KeyboardAvoidingView yamaları yok → `react-native-keyboard-controller` · TouchableOpacity yok → Pressable + basılı durum + haptic · emoji ikon yok → SF Symbols (expo-symbols) / lucide tek set · animasyonsuz geçiş yok → Reanimated layout animation, ease-in yasak (Emil).

## Adımlar
M1. `bunx create expo` (ya da nkzw-tech/expo-app-template) → `claude plugin install expo@claude-plugins-official` → `npx skills add vercel-labs/agent-skills --skill vercel-react-native-skills` → `npx skills add emilkowalski/skills`.
M2. Spec: Matt Shumer spec prompt'u (`arastirma/x/notlar/prompts.md` #1) + BRIEF.md → `SPEC.md`: ürün özeti, 3-5 sahnelik deneyim hikâyesi, ekranlar/navigasyon, akışlar (hata mesajlarıyla), veri. Bir **"gotcha moment"** (uygulamayı sevdiren tek an) zorunlu.
M3. DESIGN.md (KAPI-1): `arastirma/github/kopyalar/Meliwat-awesome-ios-design-md` içinden sektöre en yakın `DESIGN-expo.md` (OpenTable, Starbucks, Things 3 …) başlangıç → markaya uyarla → açık/koyu token'lar sayıyla.
M4. Vitrin ekranı: tüm bileşenler tek ekranda (buton halleri, input halleri, kart, liste satırı, sheet, toast, boş/hata/yükleniyor) → SS → onay. Vitrin güzel değilse hiçbir ekran güzel olmaz.
M5. Ekranlar story story (Ralph döngüsü: her story taze bağlam + progress.txt). Her ekranda 4 durum (dolu/boş/yükleniyor/hata), tek birincil aksiyon, en az 2 gerçek uygulama referansı.
M6. Görsel QA (KAPI-2): web export'u Playwright ile 390×844 açık/koyu SS + mümkünse Android emülatör (API 26 x86, `-no-accel`) / iOS simülatör SS → design-review → düzelt.
M7. Önizleme: `.eas/workflows/preview.yml` → her push'ta EAS Update → Expo Go linki müşteriye (Evan Bacon yöntemi).
M8. Yayın (KAPI-4): app-store-preflight skill'i + Apple zorunluları (privacy URL, onboarding, ayarlar, loading) + Small Business Program başvurusu + `npx testflight` / `eas build -p android` → mağaza metni "farkımız" maddelerinden.

## Dersler (geçmiş uygulamalardan)
- JVM/web testi yetmez: en düşük desteklenen Android'de ana akış koşulmadan "bitti" denmez (melodi-zil D-008).
- Bottom sheet açılır açılmaz birincil buton görünür olmalı (D-009).
- Ağır cihaz-üstü medya işi varsa native Kotlin/Swift istisnası, nedeni DERSLER.md'ye.
