# Can Claude Fable 5.1 Build a Real iPhone App? — Code with Beto — 12 dk — https://www.youtube.com/watch?v=9V2ZFhLsh9w

## Tek paragraf: ne yapıyor, sonuç ne
REEt7nX6x-0 videosunun tekrarı: aynı Expo başlangıç projesi, aynı plan prompt'u, aynı Code with Beto MCP ve `app-icon` skill'i — bu kez Fable 5.1 + "Ultracode" ile **tek prompt**. Sonuç: ilk denemede çalışan uygulama (onboarding geçiş animasyonları, haptics, bildirim izni, swipe ile hızlı aksiyonlar, zoom transition, düzenleme) ve **ilk seferde çalışan** iki boyutlu widget (değişiklikte güncelleniyor, dark mode uyumlu), dark mode'da değişen ikon. Maliyet $33 / ~40 dk (Fable 5: ≈$50 / ~41+8 dk ve widget bozuktu). Özgün ders: Claude Desktop'taki iOS simulator entegrasyonu (attach simulator) ajanın kendi kendini test etmesini sağlıyor ama screenshot döngüsü context/kredi yakıyor — Beto durdurup testi kendisi yaptı. Context harcamasının çoğu iOS simulator MCP'ye, %16'sı Code with Beto MCP'ye gitti. Küçük kusurlar: header animasyonu çalışmıyor, zoom transition header yüzünden garip, bir öğe sola hizalı değil.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code desktop, Fable 5.1 ($10/M in, $50/M out; cache read'ler %75 ucuzladı → tipik oturumda ~1/4, uzun agentic oturumda ~1/2 maliyet iddiası), mod: Ultracode.
- Expo (React Native), **Expo UI native components** (plan prompt'unda açıkça istendi), native widget, yerel veri.
- Code with Beto MCP (Platano template, settings screen örneği, widget örnekleri, Ember benzeri örnek).
- App Icon skill: https://codewithbeto.dev/blog/my-favorite-claude-skills#5-app-icon
- Claude Desktop iOS simulator entegrasyonu ("attach simulator" — ajan simülatörü kontrol edip screenshot alıyor; iOS simulator MCP).
- Plan/prompt/rehber: https://codewithbeto.dev/free/build-your-first-app-with-fable-5
- Orijinal Fable 5 videosu: https://youtu.be/REEt7nX6x-0
- Kitap (promo BUILD19, $19): https://cwb.sh/book?r=yt-fable ; Hire: https://codewithbeto.dev/enterprise ; Platano: https://cwb.sh/platano?r=yt ; Pro: https://cwb.sh/pro?r=yt ; React Native Course: https://cwb.sh/rn?r=yt ; Inkigo: https://cwb.sh/inkigo?r=yt ; Quick Push: https://apple.co/4tvT4wF ; GitHub Sponsors: https://github.com/sponsors/betomoedano

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
(Kurulum, proje oluşturma, skill/MCP kurulumu için bkz. `REEt7nX6x-0-fable-5-expo-habit-widget.md`.)
1. [00:00:39] Yeni Expo projesi hazır; model Fable 5.1, Ultracode açık.
2. [00:00:51] Rehberdeki planın **tamamını** tek prompt olarak yapıştır. Plan vurguları: yalnız iOS, çok native his, **Expo UI native components**, native widget, yerel veri, Code with Beto MCP ile UI güzelleştirme, Platano template + settings screen referansı, isim + `app-icon` skill'i ile ikon, tamamen standalone uygulama.
3. [00:04:57] ~20 dk: MCP'den kaliteli örnekler (çok sayıda widget örneği) çekiyor; ikon + iOS **dark icon** varyantı üretildi.
4. [00:05:25] Kredi bitti → $50 kredi alındı → "please continue".
5. [00:06:31] Fable simülatörde test etmek istedi → Claude Desktop'ta "attach simulator" → ajan simülatörü kontrol ediyor, screenshot alıp iterasyon yapıyor, light/dark değiştiriyor. Context çok büyüdüğü için Beto durdurdu.
6. [00:07:30] Fiziksel cihazda çalıştırmada sorun çıktı → Claude'a "fiziksel cihazımda çalıştır" dedi, Claude düzeltti.
7. [00:07:51] Test: onboarding geçiş animasyonları + haptics, nazik bildirim izni sorusu, light/dark; habit oluşturma; sola/sağa sürükleyerek hızlı aksiyonlar (undo/done); dummy data yükleme; habits ekranında zoom transition; düzenleme/güncelleme.
8. [00:09:49] Widget: iki boyut (büyük + küçük) ilk seferde çalıştı, uygulamada değişiklik → widget güncellendi, dark mode uyumlu; ikon dark mode'da değişiyor.
9. [00:10:39] `/usage`: toplam $33, ~40 dk. Context dağılımı: çoğu iOS simulator MCP, %16 Code with Beto MCP.

## Kullandığı prompt'lar (varsa aynen)
Ana prompt: rehberdeki plan, videoda okunmuyor — https://codewithbeto.dev/free/build-your-first-app-with-fable-5
Kredi bitince:
```
please continue
```
Fiziksel cihaz (anlatım): Claude'a uygulamayı fiziksel cihazda çalıştırmasını söyledi.

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Plan prompt'unda **Expo UI native components** (SwiftUI bileşenlerini Expo'dan kullanma) açıkça istendi → native kontroller, native his.
- MCP ile üretim kalitesinde örnekler (Platano, settings screen, widget örnekleri) — Beto: "MCP'm bu uygulamaya çok benzer bir örnek sunuyor"; kalite büyük ölçüde referanstan geliyor.
- Onboarding ekranları arası geçiş animasyonları + haptics; bildirim iznini onboarding içinde nazikçe sorma.
- Liste satırında sola/sağa swipe ile hızlı aksiyonlar (done/undo) — "so satisfying".
- Zoom transition (liste → detay).
- iOS dark/tinted ikon varyantı, widget dark mode.
- Ajanın simülatörde screenshot alıp kendi UI'ını eleştirmesi (görsel QA döngüsü) — kaliteyi artırıyor ama pahalı.

## Hatalar ve çözümleri
- Kredi bitti ortada → kredi alıp "please continue" (oturum kaldığı yerden devam etti).
- Simulator screenshot döngüsü context'i şişirdi → ajanı durdurup manuel test; Beto pahalı modelde bunu önermiyor.
- Fiziksel cihazda elle çalıştırma sorunları → Claude'a devretti, çözdü.
- Header animasyonu çalışmıyor; zoom transition header yüzünden garip; bir öğe sola hizalı değil → videoda düzeltilmedi (ikinci tur gerekli).

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Expo UI (`@expo/ui`) native components**'i plan şablonunun varsayılanı yap (iOS'ta SwiftUI, Android'de Jetpack Compose bileşenleri) — "ham Android pop-up" şikâyetine doğrudan çözüm.
- Aynı plan + farklı model karşılaştırması: plan şablonu sabit kaldıkça model güncellemeleri ölçülebilir; fabrikada sabit bir "benchmark uygulaması" (habit tracker + widget) tutup yeni modelleri onunla deneyin.
- Görsel QA döngüsü (simulator screenshot → eleştir → düzelt) değerli ama pahalı: ucuz modelle veya sınırlı sayıda (ör. ekran başına 1 screenshot) çalıştır; CLAUDE.md'ye "simulator screenshot'ı yalnızca ekran tamamlandığında al" kuralı.
- Kendi referans MCP/skill kütüphanemiz context'in ~%16'sını yiyebilir — örnekleri kısa, odaklı tut.
- Swipe aksiyonları (`react-native-gesture-handler` Swipeable / `ReanimatedSwipeable`), zoom transition (Expo Router iOS zoom transition / `react-native-screens`), onboarding geçişleri + `expo-haptics` standart bileşen paketine.
- İkon: light + dark + tinted varyantları (`app.json` → `ios.icon` dark/tinted) zorunlu.
- Bütçe referansı: tek prompt'la tam V1 ≈ $33 / 40 dk; kota bitimine karşı oturum ortası kredi planı.
- Teslim öncesi QA listesine: header animasyonları, transition'larda header davranışı, hizalama kontrolü.
