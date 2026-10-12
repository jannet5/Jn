# HAT: MOBİL UYGULAMA (Expo)

Hedef: native hissi veren, animasyonlu, Android'in ham pop-up'ları olmayan uygulama. Müşteri her commit'i telefonunda görür.
Stack (X + GitHub + YouTube'da herkesin ortak seçimi): **Expo (Router, New Arch) + NativeWind/React Native Reusables + Reanimated + Gesture Handler + expo-haptics + @expo/ui / formSheet + zeego menü + sonner-native toast + FlashList** · backend gerekirse Supabase (ya da InstantDB) · ödeme RevenueCat · EAS Build/Update.
Skill'ler: **expo/skills (resmi: expo-native-ui, expo-design-system, expo-animation, expo-ui, expo-router, expo-deployment)** · vercel-react-native-skills · emil animate-expo · frontend-design · context7 MCP · Expo MCP.
Ayrıntılı ekran-ekran süreç: `uygulama-fabrikasi` skill'i (8 aşama) — bu hat onun **üretim ve kalite katmanını** değiştirir.

## Native-slop yasakları (expo-design-system/native-slop.md'den, CLAUDE.md'ye girer)
`Alert.alert` ile onay/uyarı yok → native sheet/menü/toast · JS modal yok → `presentation: 'formSheet'` · Android `elevation` gölge yok → `boxShadow` · `headerShown:false` + elle header yok → native header / large title · KeyboardAvoidingView yamaları yok → `react-native-keyboard-controller` · TouchableOpacity yok → Pressable + basılı durum + haptic · emoji ikon yok → SF Symbols (expo-symbols) / lucide tek set · animasyonsuz geçiş yok → Reanimated layout animation, ease-in yasak (Emil).

## Ek kurallar (YouTube mobil özeti, `planlar/mobil/OZET.md`)
- **Nick Saraev'in 5 alanı spec'te zorunlu:** çekirdek işlev · çekirdek döngü (her tamamlamada haptik + kısa ses + mikro animasyon) · yardımcı özellik · 5-7 ekran sınırı · geri getiren kanca (bildirim, seri).
- **Önce tasarım görseli:** görsel modelle tüm ekranlar → `design/<ekran>.png`; her ekran görevi bir PNG ile açılır ve "SS al, design/ ile karşılaştır, aynı olana kadar düzelt" döngüsüyle kapanır.
- **İlk günden skill:** `npx skills add expo/skills --skill '*' -y -a claude-code` → expo-native-ui + expo-design-system (native-slop.md) + expo-animation + expo-ui + expo-deployment. (`building-native-ui` adı artık yok.) Skill olmadan çıktı ham `Alert.alert`'li çıkıyor (Traversy örneği).
- **Kendi bileşenlerin:** Toast, ConfirmSheet, BottomSheet. `Alert.alert` yok, RN `Button` yok, RN'in SafeAreaView'ı yok (`react-native-safe-area-context`).
- **Dev Tools ekranı:** sahte 30 günlük veri, animasyon tetikleme, onboarding sıfırlama; tasarım dolu veriyle görülür.
- **Mağaza uyumu:** Privacy + Terms canlı URL, hesabı silme (tam), Google girişi varsa Apple girişi, izin metinleri `app.json`'da, `legal/` HTML → Cloudflare Pages.
- **Kalite:** Chrome DevTools MCP ile sayfa sayfa görsel denetim · temiz oturumda güvenlik denetimi ×2 · gerçek cihazda development build · Sentry + PostHog.
- Prompt kaynakları: Codesistency plan/design/legal prompt'ları (dub.sh/plan-mode, dub.sh/design-prompts, dub.sh/legal-prompts), Nick'in yayın rehberi ve güvenlik denetimi dokümanları (linkler OZET §5).

## İlk üretimden kurallar (ajansimiz-app DERSLER)
- **Stil kararı:** NativeWind yalnız düzen/tipografi/boşluk; **renkler `src/theme/tokens.ts`'ten inline** (expo-native-ui Tailwind'i önermiyor, konsept temaları JS'te gerekiyor).
- **Kurulum tuzağı:** kökte bir kez `cssInterop(Animated.View, { className: 'style' })`; yoksa Animated üstündeki className sessizce yok sayılır.
- **React Compiler açık gelir:** Reanimated değerleri `sv.set()/sv.get()` ile; `sv.value = x` lint hatası.
- **Sheet:** web SS'i alınacaksa kendi `AltSayfa` (Portal ile kökte, 260ms ease-out açılış, birincil buton açılışta görünür); sadece cihaz QA'sı varsa `@expo/ui`.
- **Haptik** `lib/haptik.ts` üzerinden (web'de no-op).
- **Web DESIGN → mobil çeviri:** kart çerçevesi → yüzey rengi farkı · font-stretch → ağırlık + negatif harf aralığı · hover → basılı durum.
- **QA:** `qa/ss.mjs` (yeni-musteri.sh kopyalar) dist'i sunar, ana ekrandan tıklayarak ilerler (doğrudan rota açılırsa geri butonu yok), ilk *etkin* öğeyi seçer, 390×844 açık+koyu, konsol + yatay taşma raporlar.
- **Emülatör yoksa** (bulutta /dev/kvm yok): KAPI-4 = insan Expo Go testi ya da EAS Build. Mobilde tek HTML artifact yok; müşteri teslimi = SS + EAS Update/Expo Go linki.

## Adımlar
M1. `npx create-expo-app@latest mobil --template default --no-install` → şablonun örnek dosyalarını sil (components/constants/hooks/explore/scripts) → `npx expo install` ile bağımlılıklar → `claude plugin install expo@claude-plugins-official` → `npx skills add vercel-labs/agent-skills --skill vercel-react-native-skills` → `npx skills add emilkowalski/skills`.
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
