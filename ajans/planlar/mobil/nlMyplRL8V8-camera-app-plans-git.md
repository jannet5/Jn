# Build Apps Faster with Claude Code + React Native — Code with Beto — 27 dk — https://www.youtube.com/watch?v=nlMyplRL8V8

## Tek paragraf: ne yapıyor, sonuç ne
Beto, Dribbble'daki bir kamera konseptini (Jess Barrett) hedef alarak **önce kendisi** projeyi hazırlıyor: `bunx create-expo-app`, `expo-camera` + `expo-media-library` kurulumu ve `app.json` plugin izin metinleri, tab şablonunu silip tek ekran Stack'e indirme, zorunlu dark tema. Sonra `/init` ile `CLAUDE.md`, plan mode, **Anthropic Console prompt generator** ile yapılandırılmış "implementation plan" prompt'u, doküman linkleri ve Reanimated/Gesture Handler sürümleri; `plans/` klasöründe faz başına dosya ve GitHub CLI ile her büyük değişiklikte commit. Ekran görüntüsünü `Ctrl+V` ile verip bileşenleri `components/`'a soyutlattı. Sonuç: onboarding + izin ekranları, kamera, flip, timer, saat-kadranı zoom (yarım çalışıyor). Ders: iş mantığında iyi, UI çeviride zayıf, temel bilgi şart.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code (`npm install -g` ile global, `claude -v`), Cursor + Claude Code eklentisi, `/init`, plan mode / auto-accept (`Shift+Tab`).
- Bun (`bunx create-expo-app camera-app`, `bun install`).
- `expo-camera`, `expo-media-library` (app.json plugin'leri), `react-native-reanimated`, `react-native-gesture-handler` (Expo şablonunda hazır), `react-native-svg` (Claude unuttu, elle eklendi), `expo-image-manipulator`, AsyncStorage (ayarlar).
- Anthropic Console → Generate prompt (console.anthropic.com).
- GitHub CLI (`gh`), Super Whisper (ücretsiz dikte), Dribbble tasarımı (Jess Barrett).
- Açıklama linkleri: https://codewithbeto.dev , https://codewithbeto.dev/learn , https://codewithbeto.dev/learnReact , https://codewithbeto.dev/learnGit , https://codewithbeto.dev/projects , https://codewithbeto.dev/pricing , https://www.figma.com/@codewithbeto , https://www.prisma.io/?via=beto , https://www.ecostudios.dev , https://youtu.be/EVsJ-qlEQp8?si=tl5juYIvKXKYbjra , https://github.com/betomoedano , https://github.com/sponsors/betomoedano , https://discord.gg/dbYfWFw862 , https://medium.com/@betomoedano01 , https://x.com/betomoedano , https://bsky.app/profile/codewithbeto.dev , https://www.linkedin.com/in/betomoedano , https://www.youtube.com/channel/UCh247h68vszOMA_OWpGEa5g/join

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:01:17] `npm install -g @anthropic-ai/claude-code`, `claude -v`.
2. [00:01:59] `bunx create-expo-app camera-app` → `cd` → Cursor'da aç; Claude Code eklentisi (dosya seçince ikon → terminalde `claude`).
3. [00:02:49] AI'dan önce bağımlılıklar: `npx expo install expo-camera` → `app.json` `plugins`'e expo-camera (kamera+mikrofon izin metinleri, Android dahil); `expo-media-library` + plugin'i.
4. [00:03:39] Hedef tasarım: Dribbble kamera konsepti; sağ alttaki döndürmeli zoom kadranı için Reanimated + Gesture Handler (package.json'da var olduğunu kontrol).
5. [00:04:08] Şablonu sadeleştir: `(tabs)` klasörünü sil, `index` dosyasını kök `app/`'e taşı, `+not-found` sil, root layout'ta temayı sabit `DarkTheme`, font yüklemesini kaldır, index'te boş `View`.
6. [00:05:46] `/init` → `CLAUDE.md` (README, package.json, app.json incelendi) → kabul. `Shift+Tab` ile plan mode.
7. [00:07:18] Plan npm kullanıyordu → "we are using bun instead of npm" → scriptler `bun start`, `bun ios` olarak güncellendi.
8. [00:08:45] Anthropic Console → "Generate prompt" → gereksinimleri yaz → üretilen şablona madde madde gereksinimler + `expo-camera` ve `expo-media-library` doküman linkleri + Reanimated/Gesture Handler **sürümleri** ekle → Claude'a yapıştır.
9. [00:13:32] Faz planı geldi (Phase 1: permissions, basic camera + photo, navigation, camera switch; Phase 2: video, pinch+slider zoom 5x, settings/AsyncStorage, gallery preview). Ekran oluşturmaya geçmeden "No" deyip yönlendirme: `plans/` klasörü + faz başına dosya, `gh` ile her büyük değişiklikte commit.
10. [00:15:57] Dribbble screenshot (`Shift+Ctrl+Cmd+4`) → Claude input'unda **`Ctrl+V`** (Cmd+V değil) + UI prompt'u; ardından bileşen soyutlama + saat-kadranı + plan güncelleme + sadece dark mode talimatı.
11. [00:18:13] `npx expo start` → gerçek cihazda Expo Go: `react-native-svg` eksik hatası → `bun install react-native-svg`, sunucuyu yeniden başlat.
12. [00:19:53] Geri bildirim: onboarding + izin ekranları, header kaldır, kadran rakamları yerinde değil → onboarding 3 sayfa ("Advanced camera modes", "Professional controls", "Customizable settings", "Grant permissions and start"), kamera çalıştı.
13. [00:23:27] "Move on to phase two" → video, flash, timer, grid, settings; `components/`, `ui/`, `icons/`, custom hook'lar düzenli; commit.
14. [00:24:21] Timer hatası (sonsuz çekim) → düzeltildi. Galeri ve settings navigasyonu TODO kaldı; zoom kadranı yarım.

## Kullandığı prompt'lar (varsa aynen)
```
Hey, uh we are using bun instead of npm.
```
Console'da üretilen meta-prompt (başı aynen, gerisi özet):
```
You are tasked with creating a detailed implementation plan for a camera application based on a given requirements and documentation. Your goal is to analyze the requirements, create a comprehensive plan and provide insight on how to implement each feature.
First review the camera app requirements: [maddeler: permissions; camera & gallery access; open the gallery, take photos, videos; quick actions like flash, focus, rotate camera, motion blur, low light, light trail; bottom-right control to swipe and rotate to increase and decrease the zoom; settings screen]
[Docs: expo-camera, expo-media-library links]
Analyze the requirements and break down that into main categories like permissions, core functionality, UI elements, advanced features, settings. For each requirement, include specific considerations such as how to handle permissions, camera... If anything is unclear or seems challenging to implement with expo-camera, mention it and suggest alternative approaches.
```
Ek: "Here are the requirements." + doküman linkleri + "We are using [react-native-reanimated x.y, react-native-gesture-handler x.y]".
```
Can you please create a folder for the plans and a file for each phase and after that also I want you to use version control and use GitHub CLI. I have the GitHub CLI so you can use it. It is installed in my machine. Commit after every major change.
```
```
This is the UI that we want to achieve. Please develop the UI very similar to this image as much as possible.
```
```
Please try to abstract as many components as you can like things that you're going to be utilizing icons. Keep them in the components folder and pay close attention to the bottom right of the screenshot that I shared with you. We want to achieve this kind of clock like interface where users can swap with their finger to increase and decrease the zoom. Oh, and I forgot to say one more thing. Please update the markdown and plans accordingly to the screenshot that I share with you and develop the components as well accordingly so that we can match the UI that we want and keep in mind that we are only using dark mode in this case. So don't worry about compatibility with light mode and stuff like that. Just keep everything dark and stick to the design.
```
```
It's looking great, but first we need an onboarding screen that shows the features of the application as well as permissions for the library and for the camera because right now we don't have any permissions and we cannot see anything using the camera. And then also get rid of the header and make sure that the clock animation works nice because right now the numbers are out of place. If that means moving to page two, let's go ahead and do that.
```
```
When I use a timer for 3 seconds, after it takes a picture, it keeps taking pictures forever and I cannot get it to stop. Please fix that.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Somut görsel hedef: Dribbble'dan profesyonel konsept → "very similar to this image as much as possible", "stick to the design".
- **Reanimated + Gesture Handler** ile jest tabanlı saat-kadranı zoom kontrolü; sürümleri prompt'ta vererek eski API kullanımını engelleme.
- Sadece dark mode: tema sabitlendi, light uyumluluk masrafı yok.
- Bileşen soyutlama (`components/`, `ui/`, `icons/`, custom hooks) → tutarlı görünüm.
- Çok sayfalı onboarding + izin istemeden önce özellik tanıtımı (izin pop-up'ları bağlamsız çıkmıyor).
- `react-native-svg` ile özel ikon/kadran çizimi.
- Not: Beto'ya göre Claude "UI çeviride pek iyi değil"; karmaşık jestli bileşen (kadran) yarım kaldı.

## Hatalar ve çözümleri
- Plan npm varsayıyordu → bun olduğunu söyle → scriptler düzeldi [00:07:18].
- Claude plan dosyası yazmadan ekran üretmeye başladı → "No" ile durdur, `plans/` + commit talimatı [00:14:20].
- Expo Go'da `react-native-svg` bulunamadı → token harcamamak için elle `bun install react-native-svg` + Metro yeniden başlat [00:19:00].
- İzin yok → kamera siyah → onboarding + izin ekranı ekletildi.
- Saat kadranı rakamları kayık / kırılıyor → bir tur düzeltildi, hâlâ kusurlu; ileride elle iyileştirme.
- Timer sonsuz fotoğraf çekiyor → net tarif + düzeltme [00:25:07].
- Faz 2 güncellemelerinden sonra uygulama kapandı → yeniden başlatma.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Ön hazırlık AI'dan önce**: bilinen native bağımlılıkları (`npx expo install ...`) ve `app.json` plugin izin metinlerini fabrika script'i kursun; şablondaki gereksiz tab/explore/not-found ekranlarını sil → Claude temiz zeminde başlasın.
- `/init` → `CLAUDE.md`; içine paket yöneticisi (bun/npm), tema kararı, kütüphane sürümleri yazılsın.
- `plans/` klasörü + faz başına `.md` (phase-1.md …) ve "faz bitince test, sonra 'move on to phase N'" ritmi.
- Git: `gh` ile "commit after every major change" — geri dönüş noktası, yeni Claude instance'ı temiz commit'ten başlatma.
- Meta-prompt: Anthropic Console prompt generator ile gereksinim → yapılandırılmış plan prompt'u; fabrikada müşteri brief'inden aynı şablonu otomatik üret ("If anything is unclear or challenging … suggest alternatives").
- Doküman linkleri + **tam sürüm numaraları** (Reanimated, Gesture Handler, Expo SDK) her plan prompt'unda.
- Görsel referans (Dribbble/Figma/müşteri marka) + "abstract components into components/" + "stick to the design".
- Eksik paket hatalarını elle çöz (token tasarrufu); QA: Expo Go'da gerçek cihaz.
- İzin isteyen her uygulamada onboarding → açıklama → izin akışı standardı (ham sistem izin pop-up'ını bağlamsız göstermemek).
- Karmaşık jest/animasyon bileşenleri için hazır kütüphane veya insan müdahalesi bütçesi ayır.
