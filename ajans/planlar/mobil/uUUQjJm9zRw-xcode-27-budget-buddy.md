# Build Your First iPhone App with Claude & Xcode 27 — Code with Beto — 19 dk — https://www.youtube.com/watch?v=uUUQjJm9zRw

## Tek paragraf: ne yapıyor, sonuç ne
Beto, Xcode 27'nin yerleşik "Intelligence" sekmesine bağlanan Claude Agent (model: Fable 5.1; $20 planda Opus 5) ile native SwiftUI bir harcama takip uygulaması "Budget Buddy" kuruyor. Kullanıcı yazmak yerine konuşuyor ("I just spent $20 in McDonald's"); speech-to-text metni cihaz üstü Foundation Models (Apple Intelligence yerel LLM) ile yapılandırılmış harcama kaydına çevriliyor ve SwiftData ile yerelde saklanıyor. Dribbble'dan siyah-beyaz bir UI referansı alınıyor, VoiceOS (sponsor) sesle uzun bir prompt + ekran görüntüleri üretiyor, Xcode'da `/plan` ile plan çıkarılıyor. Sonuç: 3 adımlı animasyonlu onboarding, günlük harcama grafiği, sürükle-bırak sıralanabilir liste, swipe-to-delete, haptics, light/dark mode, manuel giriş fallback'i ve bir geliştirici ekranı (izinler, Foundation Models durumu, dummy data) olan, fiziksel iPhone'da çalışan uygulama. Expo değil native Swift; ama iş akışı (referans → sesli prompt → plan modu → soru-cevap → dev ekranı) fabrikaya doğrudan taşınabilir.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Xcode 27 (Eylül öncesi: https://developer.apple.com/download ; sonrası Mac App Store) — yeni **Intelligence** sekmesi (Settings `Cmd+,`), Claude Agent + Codex bağlanabiliyor; "Model Context Protocol available to external agents" → **Always** (Claude Code desktop'tan da projeye bağlanmak için).
- Claude Agent / Fable 5.1 (usage credits), alternatif Opus 5.
- SwiftUI, SwiftData (yerel depolama), Speech (speech-to-text), Foundation Models (on-device LLM, iOS 27 hedefi).
- Device Hub (Xcode → Open Developer Tool → Device Hub): fiziksel telefonu bilgisayardan görüntüleme/kontrol. QuickTime ekran yansıtma.
- Dribbble (UI ilhamı, "expense tracker" araması).
- VoiceOS (sponsor, speech-to-text + aksiyon, ekran görüntüsüyle prompt üretimi): https://cwb.sh/voiceos
- Kitap — From Idea to App Store with Claude Code (Git/GitHub, deploy, versioning, skill paketi): https://cwb.sh/book?r=yt
- React Native Course: https://cwb.sh/rn?r=yt — https://youtu.be/WTRNddbopGQ
- Platano — AI app template: https://cwb.sh/platano?r=yt
- Pro Access: https://cwb.sh/pro?r=yt ; Inkigo: https://cwb.sh/inkigo?r=yt ; Quick Push (push bildirim test, macOS): https://apple.co/4tvT4wF
- GitHub Sponsors: https://github.com/sponsors/betomoedano ; Discord: https://cwb.sh/discord?r=yt ; Newsletter: https://cwb.sh/newsletter?r=yt

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:30] Xcode 27 indir; VoiceOS kur (Ctrl+Option = konuşarak aksiyon, Fn = dikte, Fn iki kez = uzun dikte).
2. [00:01:28] Yeni proje: "App" hızlı başlangıcı yerine **template** seç → Multiplatform → App. Ad: `Budget Buddy`; Team: Apple hesabı (geliştirme için $100 ödemeye gerek yok); Organization identifier ters domain: `dev.codewithbeto.budgetbuddy-video`; diğerleri None.
3. [00:02:57] Simulator olarak iPhone 17 seç; `ContentView` önizlemesinde "resizable" ile büyük ekran/iPad görünümünü kontrol et.
4. [00:03:38] `Cmd+,` → Intelligence → Claude Agent'ı bağla, oturum aç, model olarak Fable seç. MCP'yi harici ajanlara "Always" aç.
5. [00:04:17] Sol üst Coding Assistant → New Conversation → Claude Agent.
6. [00:04:47] Dribbble'da "expense tracker" ara, siyah-beyaz sade bir referans seç (grafik, orta liste, manuel giriş klavyesi için ayrı referanslar).
7. [00:06:29] VoiceOS'a sesle ihtiyacı anlat → ekran görüntülü uzun prompt üretiyor, panoya kopyalıyor.
8. [00:08:10] Xcode'da `/plan` + yapıştırılan prompt + ekler (onboarding, izinler, sortable liste, "don't make assumptions ask me questions").
9. [00:08:54] Ajanın soruları: depolama → **SwiftData** (recommended); LLM otomatik kategori → **evet**; zaman aralıkları → recommended; para birimi → **device locale currency** (Avrupa €, Meksika peso).
10. [00:09:37] Kodu ayrı sekmede aç (sağ tık → Open in New Tab) ki ajan ile kod/preview arasında hızlı geçilsin.
11. [00:11:02] Plan: hedef **iOS 27 only**, API doğrulaması, izinler, dosya yapısı, modeller, uygulama sırası, edge case'ler → "yes implement it".
12. [00:11:45] Ajan derleyip iPhone 17 simülatöründe açtı: animasyonlu onboarding + Apple Intelligence adımı.
13. [00:12:00] Cihaz dropdown'dan fiziksel iPhone 17 Pro seç → Play. Device Hub'da telefonu görüntüle/kontrol et.
14. [00:12:50] Mikrofon testi: bottom sheet "listening" diyor ama Done disabled. Klavye fallback çalışıyor (tutar animasyonu).
15. [00:14:03] Fable simülatörü kendisi test ediyor; mikrofon izni yoksa Settings'e yönlendiren fallback ekledi.
16. [00:14:45] Takip prompt'u: mikrofon bug'ı + geliştirici ekranı.
17. [00:15:35] Dev ekranı: mikrofon authorized, Foundation Models available. Asıl sorun: Device Hub ile yansıtırken Apple mikrofon/kamera girişini kapatıyor → QuickTime'a geçince sesli giriş çalıştı.
18. [00:16:09] "$20 in McDonald's" → Foundation Models → McDonald's / Food & Drink / tarih / notlar formu → kaydet. Gelir girişi ("$100 from a course sale") kategori "Other" ve gider olarak kaydedildi (eksik). Swipe-left delete, long-press drag reorder, ay görünümü.
19. [00:17:11] VoiceOS ile "dispatch Claude Code" → grafiğe mavi/daha güzel renk; sonuç gradient.
20. [00:18:00] Yeni konuşma: dev ekranına dummy data seçeneği → zengin veriyle grafik kontrolü. VoiceOS ekran işaretlemesi prompt'a ekran görüntüsü olarak giriyor.
21. [00:18:49] Sonuç: haptics, light/dark toggle, reorder. Devamı (Git, GitHub, deploy) kitapta.

## Kullandığı prompt'lar (varsa aynen)
VoiceOS'a söylenen (prompt üretme isteği) [00:06:29]:
```
help me create a plan I want to build a an expense tracker application it's going it's going to be called Budget Buddy but I want it to be modern I want to use modern APIs like speech to text the goal is that the user doesn't have to type it only says something like oh I just spent $20 in McDonald's then using Foundation Models which is the local LLMs for iOS we can get a structured data for the expense item and then save it locally okay and for the UI you can check this here so I want something like this black and white and I want something like this for the for the chart the daily chart where you can see how much you're spending I also want the list of items like these one right here in the middle and for the input of course we want speech to input but also you know some users might not be able to speak to enter a record so we want something like this for a fallback when you are entering an item manually so help me create a prompt of course using best practices with Xcode and SwiftUI
```
`/plan` sonuna eklenen [00:08:11]:
```
please add a simple onboarding with three steps modern looking just to onboard the user to the application make sure that we ask for permissions in the onboarding like for Apple Intelligence make sure that everything is available let's save the data locally make sure that the items on the list are sortable so that the user can move them around don't make assumptions ask me questions
```
Bug + dev ekranı [00:14:45]:
```
when I press the microphone button it says that it's listening but it's not doing anything and I'm not able to press done and I can actually see like a circle moving up and down in a loop animation that I don't understand why uh could you please fix that
```
```
create a screen for development where I can toggle things like permissions verify that Foundation Models and the Siri AI capabilities are enabled also uh double viewing the onboarding screen
```
Renk [00:17:11] ve dummy data [00:18:00]:
```
can you dispatch Claude Code to change the graph to use different colors maybe like a blue but make it more beautiful
```
```
can you add in the developer screen an option to add dummy data so that we can see the app with rich data past events and see the actual graph with more data
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Kod yazdırmadan önce Dribbble'dan **somut ekran referansı** (siyah-beyaz, sade); her bölüm (grafik, liste, manuel giriş) için ayrı görsel prompt'a ek olarak verildi.
- Onboarding "modern looking, three steps" diye açıkça istendi → animasyonlu geçişler.
- Manuel girişte tutar yazılırken sayı animasyonu; giriş için bottom sheet.
- Liste: swipe-to-delete, long-press drag reorder, haptics.
- Grafik için "make it more beautiful" → gradient.
- Dark mode baştan destekli; dev ekranında light/dark toggle.
- Dummy data ile "dolu hâl" görsel kontrolü (boş state'e bakıp karar vermemek).

## Hatalar ve çözümleri
- Mikrofon "listening" ama Done disabled / sonsuz loop animasyonu → asıl neden: **Device Hub ile ekran yansıtırken Apple mikrofon/kamera girişini kapatıyor**. QuickTime ile yansıtınca çözüldü. Ajan ayrıca izin reddinde Settings'e yönlendiren fallback ekledi.
- İlk "$35 in tacos" denemesi tanınmadı → tekrar deneyince çalıştı (konuşma tanıma kararsızlığı; retry UX'i gerekli).
- Gelir ("course sale") gider olarak + "Other" kategorisiyle kaydedildi → veri modeli baştan düşünülmemiş; Beto: "ID, name, description, emoji, notes gibi veri şeklini Claude'a önceden söyleyin, yoksa varsayım yapar."
- Device Hub açılmıyorsa: Xcode menüsü → Open Developer Tool → Device Hub.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Plan aşamasında zorunlu kural: prompt sonu `don't make assumptions ask me questions` + plan modu (Claude Code `/plan` / plan mode); depolama, para birimi (device locale), kategori kararları soru-cevapla netleşsin.
- Her uygulamaya **Developer/Debug ekranı** şablonu: izin durumları, özellik kullanılabilirliği, onboarding'i tekrar göster, dummy/seed data yükle, light/dark toggle. (`__DEV__` ile gizli; Expo'da `expo-dev-client` ile.)
- Veri modeli (id, ad, açıklama, emoji, notlar, gelir/gider tipi) prompt'tan önce yazılı spec olarak verilsin.
- Tasarım girdisi: Dribbble/Mobbin'den bölüm bazında (grafik, liste, giriş sheet'i) referans ekran görüntüsü — "çirkin çıktı" şikâyetine karşı en ucuz önlem.
- Onboarding standardı: 3 adım, animasyonlu, izinleri (mikrofon, bildirim vb.) onboarding içinde iste; izin reddinde Settings'e yönlendiren fallback (`Linking.openSettings()`).
- Liste etkileşimleri standardı: swipe-to-delete, drag-reorder, haptics (Expo: `react-native-gesture-handler`, `react-native-reanimated`, `expo-haptics`, `react-native-draggable-flatlist`).
- Ham Alert yerine bottom sheet (Expo: `@gorhom/bottom-sheet` veya native sheet) ile giriş akışı.
- Sesli giriş + LLM ile yapılandırılmış veri fikri (Expo: `expo-speech-recognition` + Claude API structured output) işletme uygulamalarında (sipariş, not, kayıt) özellik olarak sunulabilir.
- Test notu: fiziksel cihazda ses/kamera testinde ekran yansıtma aracının girişi kapatabileceğini kontrol listesine ekle.
