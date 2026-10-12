# React Native Tutorial for Beginners: Build App with AI — Mikey No Code — 15 dk — https://www.youtube.com/watch?v=oaYIiJvtJ0g

## Tek paragraf: ne yapıyor, sonuç ne
Listonic (aylık ~10 bin $) klonu alışveriş listesi uygulaması; Claude Code değil **Windsurf IDE + Cascade agent + Claude Sonnet 4.5** ile React Native + **Expo SDK 54 + EAS**. Aynı Mikey kalıbı: önce 3 sekmeli placeholder iskelet, sonra **ayrı bir "modern tasarım" prompt'u**, ardından özellikler tek tek: listeler + dashboard widget'ı, filtre/arama, akıllı öneri widget'ı, bütçe widget'ı, katalog bölümü, dark mode. Yayın (EAS ile App Store/Play) videoda yok, Skool'a yönlendiriyor. Bir önceki Mikey videosundan farkı: tasarımın ayrı adım olması ve "dashboard widget" deseni.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Windsurf IDE + Cascade agent, model Claude Sonnet 4.5.
- React Native + Expo SDK 54 + EAS (kurulumda baştan doğru yapılandırma).
- Skill / MCP / backend yok (veri yerel).
- Açıklama linkleri: https://mikeyappchecklist.netlify.app/ (uygulama checklist'i), https://mikeyno-code.com/Skool-react , https://mikeyno-code.com/Skool-base44 , https://mikeyno-code.com/video84

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:01:38] Windsurf'te boş proje; "Expo SDK 54 and EAS" içeren iskelet prompt'u → dashboard, cart/list, settings sekmeleri, navigasyon çalışıyor.
2. [00:03:21] [00:04:14] Özelliklerden önce sadece tasarım: modern alışveriş uygulamalarından ilham alarak UI/UX güncelle.
3. [00:05:28] Liste/sepet işlevi: placeholder temizle, dashboard'da son liste widget'ı → tıklayınca liste sekmesinde ilgili modal; özel listeler, mağaza/miktar/fiyat/kategori filtreleri. Test: "Hardware Essentials" listesi, "hardwood" (5, flooring), "varnish" (essentials).
4. [00:06:47] Filtre + arama genişletme.
5. [00:09:19] Akıllı öneri widget'ı (sık alınanlar, tek dokunuşla ekle).
6. [00:10:10] Bütçe widget'ı; tüm listeleri silip yeniden oluşturarak gerçek zamanlı güncellemeyi doğruladı.
7. [00:12:31] Katalog bölümü (geniş ürün kütüphanesi, list builder'a bağlı, tek dokunuşla ekle).
8. [00:13:13] Dark mode.
9. [00:14:10] Uçtan uca kullanım testi. [00:14:44] EAS ile mağazalara gönderim → Skool.

## Kullandığı prompt'lar (varsa aynen)
```
Please create a React Native Plus Expo application that utilizes Expo SDK 54 and EAS. Create a mobile app with three sections, a dashboard, cart, and settings. add placeholder data for a grocery list shopping app, but don't build the actual functionality yet.
```
```
Please update the app's design and give it a more modern look. Get inspiration from modern shopping cart apps similar to ours and then apply the same design and UX.
```
```
Build the shopping cart/list functionality. Clear out all placeholder data and create a widget in the dashboard that shows the recent shopping list created. And whenever the user clicks it, it should go to the shopping list and cart section. Then open the modal of that specific list. Users should be able to create custom lists as well and add filters to categorize which stores quantity, the price and other proper filters for the list.
```
```
Now please expand the list by adding filters and search options. Make it comprehensive yet easy to understand for the user.
```
```
Please build the smart suggestion feature. Create a widget in the dashboard that shows suggestions of items that you frequently buy so you can easily add them in the cart.
```
```
Now create a widget in the dashboard that shows budgeting stats for the user based on their recent lists. Make sure this updates extensively.
```
```
Please create a new section containing a wide variety of common foods and general items. Link this to the list builder whereas users should be able to easily add these items to the list. Make this as comprehensive as possible.
```
```
Please create a dark mode theme or button for the application.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **İskeletten hemen sonra, özelliklerden önce** ayrı bir tasarım prompt'u: "modern shopping cart apps"tan ilham → sonraki özellikler aynı görsel dile oturuyor.
- Dashboard'u **widget kartları** (son liste, akıllı öneri, bütçe) ile canlı tutma; widget → ilgili sekme + modal derin bağlantısı.
- Tek dokunuşla ekleme, onay ekranı yok (sürtünmesiz akış).
- Animasyon / bileşen kütüphanesi / skill yok; tasarım tamamen modelin yorumuna bırakılmış (bizim şikâyetimizin kaynağı tam da bu).

## Hatalar ve çözümleri
- Videoda hata gösterilmiyor. Bütçe widget'ını "listeleri sil → yeniden oluştur → miktar değiştir" testiyle doğruladı (veri bağlantısı statik değil mi kontrolü).

## Bizim fabrikaya alınacaklar (somut, maddeli)
- İskelet prompt'una "Expo SDK <sürüm> and EAS" koy: EAS (`eas.json`, bundle id) en baştan yapılandırılsın.
- Akış sırası: iskelet (placeholder) → **tasarım geçişi** → özellikler. Ama tasarım geçişini "modern görünsün"e bırakma; bizim design system/skill'lerimizle somut yap (bkz. misjUj4Q_ho, sneNR5KnRJU planları).
- "Dashboard widget" deseni işletme uygulamaları için hazır şablon: son sipariş/randevu, öneriler, özet istatistik kartları; her widget derin bağlantılı.
- Her yeni özellikte "Clear out all placeholder data" ve "Make it comprehensive yet easy to understand" ifadeleri.
- Gerçek zamanlı doğrulama testi (sil/yeniden oluştur/değiştir) QA checklist'ine.
- Hazır içerik katalogları ("Make this as comprehensive as possible") müşteri uygulamalarında demo verisi için.
