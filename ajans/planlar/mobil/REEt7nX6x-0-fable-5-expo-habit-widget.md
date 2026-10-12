# Can Claude Fable 5 Build a Real iPhone App? — Code with Beto — 22 dk — https://www.youtube.com/watch?v=REEt7nX6x-0

## Tek paragraf: ne yapıyor, sonuç ne
Beto, Claude Code desktop'ta Fable 5 (effort: extra) ile boş bir Expo SDK 57 projesinden native hissiyatlı bir iOS habit tracker ("Ember") üretiyor: oluştur/düzenle/sil, streak, tutarlılık heat graph'ı, yerel bildirim hatırlatıcıları, yerel veri, onboarding, sortable liste, ana ekranda canlı heat graph gösteren native iOS widget, `app-icon` skill'iyle üretilmiş (dark mode uyumlu) ikon. Kalitenin anahtarı: "build me a habit tracker" yerine çok spesifik bir plan prompt'u + Code with Beto MCP üzerinden üretimde kanıtlanmış referans projeler (Platano template, Inkigo onboarding'i). Widget Expo Go'da çalışmadığı için development build (Xcode) kullanıyor. İlk sürüm 41 dk / ~$40, düzeltme turu 8 dk / $6 — toplam ≈$50. Kalan hatalar (widget, long-press, taşan ikonlar, unmatched route) ikinci turda düzeltildi.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code desktop, model Fable 5 ($10/M input, $50/M output; Pro planda dahil), effort **extra**, mod **bypass permissions**.
- Expo SDK 57 (Expo Go mağazada SDK 54'te; SDK 57 + widget için **development build**), Expo Go, Xcode (simulator / fiziksel cihaz — fiziksel cihaz için developer hesabı).
- Bun (`bun --version`), `bun create expo-app` (SDK 57 dev build vs SDK 54 Expo Go seçimi soruyor), `npx expo start`.
- `app-icon` skill (Code with Beto ekibinin; ikon üretir, dark mode uyumlu).
- Code with Beto MCP server (Pro üyelik; Platano, Inkigo, Ember gibi referans projeler: haptics, onboarding, animasyon kalıpları). Kurulum: codewithbeto.dev profil → MCP setup'ı kopyala → Claude'a ver.
- iOS widget (containerBackground API), local notifications, toast, form sheet, native context menu, sortable list.
- Ücretsiz rehber (kurulum + tam plan prompt'u): https://codewithbeto.dev/free/build-your-first-app-with-fable-5
- Best Claude Skills for Mobile Development: https://codewithbeto.dev/blog/best-claude-skills-for-mobile-development
- Kitap: https://cwb.sh/book?r=yt ; React Native course: https://cwb.sh/rn?r=yt ; Platano: https://cwb.sh/platano?r=yt ; Pro: https://cwb.sh/pro?r=yt ; Inkigo: https://cwb.sh/inkigo?r=yt ; Kujun App: https://apple.co/4f4fRKJ ; Quick Push: https://apple.co/4tvT4wF
- İlgili videolar (Fable 5 vs Opus 4.8 karşılaştırması dahil): https://youtu.be/VN4IfPpj-JU , https://youtu.be/XCK55zyHv0c
- GitHub Sponsors: https://github.com/sponsors/betomoedano ; Discord: https://cwb.sh/discord?r=yt

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:01:19] Claude Code desktop → Open Folder → `~/Desktop/apps`. Model Fable 5, effort extra.
2. [00:02:20] Ön koşullar (rehber dokümanda): Expo Go (telefona), Xcode (dev build için), Bun (Mac/Windows kurulum komutu, `bun --version`).
3. [00:05:22] Proje oluşturmayı **elle** yap (ajan yaparsa pahalı token israfı): "hello" yazınca sağ üstte terminal butonu çıkıyor → `bun create expo-app` → ad `Fable` → SDK 57 + development build seç.
4. [00:06:33] `Cmd+N` ile yeni session, yeni proje klasörünü aç. Terminalde `npx expo start` → QR'ı Expo Go ile tara (Expo hesabına giriş). Boş template light/dark uyumlu.
5. [00:08:51] Skill kurulumu: `app-icon` skill install komutunu terminale yapıştır, enter'larla tamamla.
6. [00:10:00] MCP: codewithbeto.dev profilden MCP setup'ı Claude'a ver; doğrulama prompt'u "list the resources" → popüler örnek projeler listelendi.
7. [00:11:08] Plan prompt'unun içeriği (tam metin rehber dokümanda; videoda özetlendi):
   - 3-8 harfli kısa bir isim bul, her yerde kullan (branding).
   - Günlük alışkanlık takibi (ör. "walk outside").
   - SwiftUI ile yapılmış gibi **native** hissettirsin; Platano template ve Code with Beto MCP projelerini referans al.
   - Habit create/edit/delete; yerel bildirim hatırlatıcıları (ilaç, yürüyüş); veriler yerelde.
   - Temiz onboarding — MCP üzerinden **Inkigo'nun onboarding'ini** referans al (para kazanan uygulama → çalışan onboarding).
   - iOS home screen widget — dokümantasyonu araştırıp uygula.
   - `app-icon` skill'iyle ikon üret.
   - **Sadece iOS**'a odaklan (V1'de token tasarrufu + platforma özel daha iyi sonuç).
   - Sample data yükleme seçeneği.
8. [00:13:54] Modu **bypass permissions** yap, planı yapıştır. Fable araçları kontrol edip MCP'yi çağırdı.
9. [00:15:04] ~30 dk sonra: oturum kullanımı %67, ~4000 satır eklendi/1000 silindi; `app-icon` skill ikon üretti; Fable kendisi build alıp bağlı iPhone 17 Pro'ya kurdu.
10. [00:15:56] Cihazda: local network izni → Reload JS. Onboarding'de bildirim izni isteniyor. "Load sample data" → toast. Tamamlama animasyonları, dark/light uyumu, test bildirimi 3 sn'de geliyor ve uygulamayı açıyor, sortable liste çalışıyor.
11. [00:16:57] Sorunlar: long-press context menu yok, widget çalışmıyor, widget'tan açınca "unmatched route", gün seçicide ikonlar container dışına taşıyor.
12. [00:17:46] `Cmd+N` yeni pencere → madde madde düzeltme prompt'u (aşağıda).
13. [00:19:28] Düzeltmeler: her değişiklikte üstte mavi refresh barı; drag & drop + haptics; container düzeldi; toolbar buton geçişleri ve form sheet native.
14. [00:20:21] ~7 dk sonra widget çalışıyor: boyutlar, uygulama adı, sıralama değişince widget güncelleniyor, dokununca doğru ekrana gidiyor, dark mode uyumlu.
15. [00:21:01] Maliyet: `/usage` → düzeltme oturumu $6 (8 dk; bazı subagent'lar daha ucuz modelle çalışıyor), ilk oturum 41 dk ≈ $40. Karar: Fable 5'i plan + ilk sürüm gibi karmaşık işlere ayır.

## Kullandığı prompt'lar (varsa aynen)
MCP doğrulama [00:10:00]:
```
hey can you use the Code with Beto MCP server to list the resources
```
Ana plan prompt'u videoda okunmuyor; tam metin: https://codewithbeto.dev/free/build-your-first-app-with-fable-5 (içeriği Adım 7'de özetlendi).
Düzeltme turu [00:17:46] (madde madde verildi):
```
Please adopt the containerBackground API.
items in the habits are not responding to press events
in the new screen colors are okay
```
(+ gün seçicide container dışına taşan ikonlar ve widget/unmatched route sorunu da bildirildi.)
Referansla istek kalıbı [00:22:04]:
```
I just want a sortable list like Ember from the Code with Beto MCP
a widget like the Ember application
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Referans projeyle prompt**: "native SwiftUI gibi hissettir" + Platano/Inkigo/Ember gibi üretimde test edilmiş projelerin kodunu MCP ile okut → haptics, onboarding, animasyon kalıpları hazır geliyor ("makes an app feel expensive").
- Onboarding'i rastgele değil, para kazanan bir uygulamanınkinden türetme.
- Tamamlama animasyonu, tümü bitince ekstra kutlama animasyonu, toast bildirimleri.
- Native form sheet sunumu, toolbar buton geçişleri, native drag & drop + haptic feedback.
- Kısa marka adı her yerde (widget'ta dahil) + skill ile üretilmiş, dark mode uyumlu ikon.
- Heat graph (tutarlılık ısı haritası) hem uygulamada hem widget'ta.
- Light/dark her ekranda test.

## Hatalar ve çözümleri
- Expo Go mağaza sürümü SDK 54, SDK 57 beta TestFlight grubu dolu → development build (Xcode) veya SDK 54 + Expo Go. `bun create expo-app` bu seçimi soruyor.
- Widget Expo Go'da çalışmaz → development build şart.
- Cihazda ilk açılış network hatası → local network izni ver → Reload JS.
- Widget boş / widget'tan açınca "unmatched route" → `containerBackground` API'sini benimset + deep link route düzeltmesi.
- Liste öğeleri press event'e yanıt vermiyor, long-press context menu yok → düzeltme turunda giderildi.
- Gün seçicide ikonlar container dışına taşıyor → layout düzeltildi.
- Maliyet/kota: tek oturum %67 kota → projeyi ajan yerine elle oluştur, tek platform hedefle, spesifik plan ver.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Kendi referans kütüphanemiz (MCP/skill)**: üretimde iyi görünen kendi Expo projelerimizden (onboarding, sortable list, widget, paywall, settings) kod örnekleri; plan prompt'unda "X'in onboarding'ini referans al" diye çağrılsın. Beto'nun MCP'sinin yerelde muadili: `ajans/referanslar/` + bir skill.
- Plan şablonu maddeleri: kısa marka adı (3-8 harf, her yerde), "SwiftUI ile yapılmış gibi native hisset", CRUD, yerel bildirim, yerel veri, onboarding (izinler içinde), sample data yükleme, ikon skill'i, V1'de tek platform.
- `app-icon` benzeri ikon skill'i fabrikaya eklensin (light/dark/tinted varyantlı).
- Proje iskeletini (`bun create expo-app` / `npx create-expo-app`) ajan değil script oluştursun — token tasarrufu.
- Widget/Live Activity gibi native özellikler için development build zorunluluğunu planlamaya yaz (`expo-dev-client`, `@bacons/apple-targets` / `expo-widgets` benzeri hedef; `containerBackground` iOS 17+ gereksinimi).
- Widget deep link'i için Expo Router'da route'u baştan tanımla (unmatched route hatası).
- Native hissiyat standardı: form sheet (`presentation: 'formSheet'`), native context menu (`zeego` / `@react-native-menu/menu`), native drag & drop + `expo-haptics`, toast (`sonner-native` / `burnt`) — ham `Alert` yasak.
- Düzeltme turları için yeni session + madde madde bug listesi pratiği.
- Model maliyet politikası: pahalı modeli plan + V1'de, ucuz modeli düzeltme/iterasyonda kullan; `/usage` ile maliyet kaydı tut (müşteri fiyatlandırması için ~$50/V1 referansı).
