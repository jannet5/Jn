# Build and Publish an App with Claude Code (Complete Beginner Guide) — Code with Beto — 58 dk — https://www.youtube.com/watch?v=M3dO417o7-U

## Tek paragraf: ne yapıyor, sonuç ne
Beto (eski Expo "developer success engineer"), terminalde Claude Code ile sıfırdan "Budget Buddy" adlı basit bir bütçe uygulaması kuruyor ve aynı videoda TestFlight'a kadar götürüyor. Akış: Node + `npx create-expo-app@latest` ile iskeleti **kendi eli ile** kurar (Claude'a kurdurmak token israfı ve eski sürüm riski), telefonda Expo Go ile canlı bağlanır, Expo ekibinin "building native UI" skill'ini projeye kurar, `/plan` + `/effort high` ile tek paragraflık fikri plana çevirir, planda veri modelini (transaction: id, amount, category, note, date, +emoji) düzeltir, sonra "auto accept edits" ile tek seferde yaptırır; hataları terminalden kopyalayıp yapıştırır; %50 bağlamda `/clear` disiplinini anlatır; "Expo deployment" skill'ini kurup Claude'a `eas.json`/`app.json` ürettirir, `npx testflight` ve `eas submit` ile build'i buluttan Apple'a gönderir, App Store Connect → TestFlight'ta kendini test eden olarak ekleyip gerçek uygulamayı telefona yükler. Sonuç: animasyonlu grafik, kategori filtresi, doğrulamalı form içeren, cihazda lokal veri tutan bir V1; TestFlight'ta canlı; mağaza ekran görüntüleri için ücretsiz Figma şablonu gösteriliyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Açıklamadaki linkler: Kitap "From Idea to App Store with Claude Code" https://cwb.sh/book?r=yt · **Beto'nun 5 favori Claude skill'i** https://codewithbeto.dev/blog/my-favorite-claude-skills · **Platano** AI app template (tema değiştirme, paywall/ödeme, skill'ler, jest/zoom hazır; ücretli) https://cwb.sh/platano?r=yt · önceki video https://youtu.be/XFmYkJJxsr8 · skill videosu https://youtu.be/nbqqnl3JdR0 · React Native kursu (publishing bölümü) https://cwb.sh/rn?r=yt · Inkigo (RevenueCat ile ~$480 MRR yapan AI dövme uygulaması, pro üyelere kaynak) https://cwb.sh/inkigo?r=yt · Quick Push (push test aracı) https://apple.co/4tvT4wF · Discord https://cwb.sh/discord?r=yt
- **Expo skills**: https://expo.dev/exposkills — "building native UI" (Expo ekibi) ve "Expo deployment" (build + submit) [00:18:08, 00:38:57]; kurulum `npx ...` komutu, agent: Claude Code, kapsam: proje (`.claude/skills`)
- **Ücretsiz Figma App Store / Play Store ekran görüntüsü şablonu** (Platano sayfasında "free Figma"; ölçüler hazır, SS'yi yapıştır, metni değiştir, export) [00:55:39]
- Claude Code (terminal; `/login`, `/plan`, `/effort high`, `/clear`, `/context`, `/statusline` ile model + bağlam çubuğu) [00:21:23–00:35:17]
- Node.js 24, `npx create-expo-app@latest` (varsayılan şablon; "geliştiriciler için yazılmış, çıplak"), Expo Go (sadece sandbox), `npx expo start` + QR
- EAS: `npm i -g eas-cli`, `eas login`, `eas whoami`, `eas init`, `npx testflight`, `eas submit --platform ios`; expo.dev dashboard (Builds); ücretsiz kuyruk 30 dk–2 saat
- Apple Developer hesabı ($99/yıl), App Store Connect, TestFlight (iç test grubu), alternatif: Xcode (12-13 GB) ile yerel build
- Claude'un seçtiği UI: animasyonlu chart (gün/hafta), FlatList, emoji picker, form doğrulaması (kütüphane adı videoda söylenmiyor; grafik için muhtemelen react-native-gifted-charts tipi "frontColor" alanlı veri)

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:01:05] Ön koşullar: Apple Developer hesabı, Mac (tercihen; Xcode simülatörleri için) veya Windows + Expo hesabı (bulut build), Anthropic hesabı. Yayın seçenek 1: EAS (Mac gereksiz, ücretsiz ama yavaş kuyruk) · seçenek 2: Xcode.
2. [00:05:17] Claude Code kurulumu (sitedeki tek satır komut) → `claude` → `/login` → Claude account. Çıkmak için Ctrl+C ×2.
3. [00:08:36] `cd ~/Desktop`; Node kontrol `node -v`.
4. [00:09:32] `npx create-expo-app@latest` → ad `budget-buddy-video` (tire, boşluk yok). **Kural: iskeleti insan kurar, Claude değil** (token + sürüm riski).
5. [00:14:41] İki terminal: 1) `npx expo start` → kamera ile QR → Expo Go'da canlı; 2) `claude` (klasöre güven).
6. [00:17:31] Üçüncü terminalde blogdaki komutla **building native UI** skill'ini kur (agent: Claude Code, scope: project). Doğrulama prompt'u: `how many skills do I have in this current working directory`.
7. [00:21:19] `/effort` → high (planlama için şart). `/plan` + fikir paragrafı (bkz. Prompt A). ~3 dk planlama.
8. [00:27:18] Planı oku: önemli olan **veri modeli** (Transaction: id, amount, category, note, date; ChartData: value, label, frontColor). Bileşen (component) kavramını bil: daha iyi prompt + daha az bağlam.
9. [00:29:49] Plan düzeltmesi (Prompt B: emoji alanı) → plan yenilenir → "yes, auto accept edits".
10. [00:31:35] Claude bağımlılık kurar ("yes, don't ask again"). Uygulama boş görünürse telefonu salla → Reload; server terminalindeki hatayı kopyala → Claude'a `there is an error` + hata (Prompt C). Claude düzeltir, uygulama canlanır.
11. [00:33:31] Test: light mode uyumu, transaction ekleme, emoji değiştirme, "please enter a valid amount" doğrulaması, gün/hafta geçişi, animasyonlu grafik.
12. [00:34:11] Bağlam yönetimi: `/statusline` ile "folder, git branch, model, context bar" göster; `/context` ile token kullanımı; **%50'yi geçince ve taban memnun ediciyse `/clear`**; ilgisiz yeni ekran = yeni sohbet.
13. [00:36:09] `/clear` sonrası iterasyon (Prompt D: başlık kesik + turuncu primary). Plan gerekmedi, "temel sağlam".
14. [00:37:50] expo.dev hesabı → https://expo.dev/exposkills → **Expo deployment** skill'ini Claude'a kurdur (Prompt E). Doğrula: `list the skills available in this working directory`.
15. [00:40:30] `help me deploy my app to the App Store` → Claude `eas.json`/yapılandırma üretir ve adım 2-3'ü senden ister: `npm install -g eas-cli`, `eas login` (`eas whoami`), `eas init` (org seç) → expo.dev'de proje oluşur. Çıktının son satırlarını Claude'a yapıştır: `Done. Seems to work. Now, please deploy to App Store.`
16. [00:43:55] Claude Apple ID bilgisi isteyince kısa yol: terminalde `npx testflight` → encryption sorusuna Enter → Apple ID girişi, doğrulama: **device** (telefona kod) → hata: bundle ID zaten kullanılıyor → Claude'a `please change that, let's use a similar one` → tekrar `npx testflight` → sertifikalar: hepsine yes → yapılandırma hatası → tüm çıktıyı Claude'a yapıştır → tekrar dene → "could not create developer test…" + API key sorusu → Enter → build kuyruğa girer.
17. [00:47:56] expo.dev → Builds: ilk komut aslında build'i başlatmış ("in progress"); ikinciyi iptal et. Build bitince artifact indirilebilir.
18. [00:49:50] App Store Connect'te uygulama kaydı `npx testflight` tarafından otomatik açılmış. Terminalde `eas submit` → iOS → "select a build from EAS" → en yeni build → Apple girişi → TestFlight'a gönderim.
19. [00:51:39] Telefona TestFlight indir. App Store Connect → TestFlight → build görünür; iç test grubu otomatik oluşmuş ama üye eklenmemiş → "+" ile kendini tester ekle → e-posta → "View in TestFlight" → Install → **gerçek, server'sız standalone uygulama**.
20. [00:55:39] Sonraki adımlar: Distribution formunu doldur; ekran görüntüleri için ücretsiz Figma şablonu (Apple App Store sayfası → SS yapıştır → export → sürükle); inceleme 1-2 gün. Yeni özellik → Claude ile ekle → **versiyonu Claude'a güncellet** → yeniden build + submit.

## Kullandığı prompt'lar (varsa aynen)
Prompt A — plan (`/plan` ile) [00:22:08]:
```
I want to build a budgeting application. It's called Budget Buddy. It needs one screen that shows daily and weekly spending in a chart, a list of recent transactions, and the ability to filter by category. No back-end, data stored locally on device.
```
Prompt B — plan düzeltmesi [00:29:49]:
```
Please add an extra field to the transaction. Include an emoji. And let's display the emoji in the views.
```
Prompt C — hata [00:32:28]:
```
There is an error. <server terminalinden kopyalanan hata metni>
```
Prompt D — iterasyon (`/clear` sonrası) [00:36:09]:
```
Fix the title "Budget Buddy", it's cut. And use primary color orange for now, just to see how it's going to look.
```
Prompt E — skill kurma [00:38:57]:
```
<expo.dev/exposkills'ten kopyalanan Expo deployment kurulum komutu>
Please add this skill for deployment inside the current directory.
```
Prompt F — yayın [00:40:30]:
```
Help me deploy my app to the App Store.
```
Diğer: `how many skills do I have in this current working directory` · `list the skills available in this working directory` · `/statusline show the folder, show the git branch, show the model, show the context bar`.

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Expo "building native UI" skill'i** kurulu olduğundan ilk üretim bile: light/dark uyumlu, animasyonlu grafik, emoji picker, inline form doğrulaması, tek tab'lı sade düzen çıktı; Beto "UI'ı gerçekten beğendim" diyor. Skill = görünüm kalitesinin kaynağı.
- Planda **veri modeline emoji alanı** ekleyip "views'ta göster" demek listeyi görsel olarak zenginleştirdi.
- `/effort high` planlama kalitesini (dolayısıyla UI kararlarını) yükseltiyor.
- Kalan kusur: başlık üstte kesik (safe-area) → iki denemede tam çözülmedi; "zaman kaybetme, V1'i çıkar" yaklaşımı.
- Mağaza görselleri: ücretsiz Figma şablonu ile 1 günlük işi dakikaya indiriyor.
- Platano şablonu: tema rengi değiştirince tüm uygulama güncelleniyor (token tabanlı tema), hazır paywall; "doğru ayakla başlamak için şablon".

## Hatalar ve çözümleri
- Claude bitti dedi ama uygulama boş → Expo Go'yu salla → Reload; server terminalindeki hatayı Claude'a yapıştır [00:31:35].
- Başlık status bar'a giriyor (kesik) → prompt ile kısmen düzeldi; safe-area sorunu [00:37:19].
- Bağlam %36 → öneri: %50'de `/clear`; model kalitesi düşmesin [00:35:17].
- `npx testflight`: bundle ID çakışması → Claude'a değiştirt [00:44:45]; iOS config'te geçersiz alan → çıktıyı Claude'a yapıştır [00:46:55]; "could not create developer test group" → yoksay, Enter [00:47:30]; komut yarıda kalsa da build + App Store Connect kaydı oluşmuş → `eas submit` ile elle gönder [00:49:50].
- Yanlışlıkla ikinci build başlattı → expo.dev'den iptal (ücretsiz kuyruk uzun) [00:48:20].
- TestFlight iç test grubu boş → kendini tester olarak ekle [00:53:20].
- Claude'a iskelet kurdurma hatası (token + eski sürüm) → `create-expo-app` elle [00:13:52].

## Bizim fabrikaya alınacaklar (somut, maddeli)
1. **İskeleti insan/script kurar**: `npx create-expo-app@latest <ad>` (tire'li ad) fabrikanın "Kurulum" adımında deterministik script; Claude'a kurdurma yasak.
2. **Skill seti zorunlu**: Expo ekibinin "building native UI" + "Expo deployment" skill'leri (https://expo.dev/exposkills) proje kapsamında `.claude/skills`'e; Beto'nun 5 favori skill listesini (blog) indirip değerlendir. Kurulum sonrası doğrulama prompt'u ("how many skills…") kalite kapısı.
3. **Plan protokolü**: `/effort high` + `/plan` + tek paragraf fikir; planda **veri modelini** gözden geçir ve zenginleştir (emoji/ikon/renk alanı gibi görsel alanlar burada eklenir); plan onaylanmadan kod yok.
4. **V1 kapsam kuralı**: ilk sürümde auth/DB/ödeme yok, lokal veri; mağazaya çıkar, ücret istersen uygulamayı ücretli sat; ödeme sonraki sürüm.
5. **Bağlam hijyeni**: `/statusline` ile bağlam çubuğu açık; %50'de `/clear`; ilgisiz özellik = yeni sohbet; hata = terminal çıktısını aynen yapıştır.
6. **Yayın runbook'u (EAS)**: `npm i -g eas-cli` → `eas login`/`eas whoami` → `eas init` → Claude'a `eas.json`/`app.json` (benzersiz bundle id!) → `npx testflight` (Apple ID, device doğrulama, sertifikalara yes) → expo.dev Builds'te tek build olduğundan emin ol → `eas submit --platform ios` → App Store Connect → TestFlight'a tester ekle → cihazda doğrula. Ücretsiz kuyruk 30-120 dk; müşteri teslimlerinde ücretli build düşün.
7. **Mağaza görselleri**: Platano'nun ücretsiz Figma App Store/Play Store şablonunu `sistem/` altına al; SS üret → yapıştır → export.
8. **Versiyon disiplini**: her yeni özellikte Claude'a `app.json` version/buildNumber artırt; isim değişikliği de Claude ile.
9. **Şablon kararı**: Platano gibi tema-token + paywall + skill içeren bir **kendi başlangıç şablonumuz** olmalı; `create-expo-app` çıplak şablon her projede aynı safe-area/başlık hatalarını tekrar üretiyor.
10. **Safe-area kuralı** CLAUDE.md'ye: başlık/üst alan `useSafeAreaInsets().top`; "başlık kesik" hatası bu videoda da var.
