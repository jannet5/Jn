# Build a React Native app with Claude AI — DesignCode — 38 dk — https://www.youtube.com/watch?v=mMTyFKqrf9U

## Tek paragraf: ne yapıyor, sonuç ne
DesignCode'un ücretli "React Native + AI" kursunun (https://designcode.io/react-native-ai) YouTube'a konan ilk 38 dakikası. Bir araba "Driving Performance" ekranının Figma tasarımını alıp önce Figma'da **responsive** hale getiriyor (auto layout, fill container, constraints, min 360 / max 480 px), sonra **Locofy** Figma eklentisiyle Expo + React Native koduna çeviriyor (bileşenlere bölme: LeftMenu, RightMenu, PowerButton, Speedometer), zip'i VS Code'da `npm install` + `npx expo start` ile iOS simülatöründe çalıştırıyor ve son bölümde yerel **Git** akışını (init/add/commit/diff/reset/restore/switch) öğretiyor. Dikkat: videonun açıklaması "Claude ile Reanimated animasyonları (withSpring, withTiming, menü fade/scale/translate)" vaat ediyor ama bu kısım ücretsiz parçada **yok**; transkriptte tek bir Claude prompt'u geçmiyor. Sonuç: piksel-piksel Figma'ya sadık, bileşenlere ayrılmış, henüz animasyonsuz bir ekran + temiz git geçmişi.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Tam kurs: https://designcode.io/react-native-ai
- Kurs asset'leri (Figma dosyası, ikonlar): https://www.dropbox.com/scl/fo/ox2eo1e61o5sugj2uf4gu/ABxAxe-myvpZxt_I0aW_gDk?rlkey=aqi9a7rztow9ch5adwiivvs6f&st=pjan04nq&dl=0
- Figma (auto layout, constraints, min/max width, "paste to replace")
- Locofy Figma eklentisi + Locofy Builder (design → React Native/Expo kodu, TypeScript seçeneği, bileşen üretimi, zip export)
- Expo (`npx expo start`), Expo Go, Xcode iOS Simulator, Node.js, VS Code
- Git (yerel sürüm kontrolü)
- Kursun devamında (ücretsiz parçada görünmüyor): Claude Sonnet 3.5, react-native-reanimated (withSpring, withTiming), react-native-gesture-handler, AsyncStorage, React Navigation, Cursor AI

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)

### Bölüm 1 — Figma'da responsive tasarım [00:03:37 – 00:14:45]
1. [00:04:26] Frame'in genişliğini sürükleyerek test et; tasarım sabit kalıyor → responsive değil.
2. [00:04:39] Mevcut tasarımı **duplicate** et, referans olarak sakla (ölçüleri buradan alacak).
3. [00:04:51] Status bar'ı sil (sadece sunum amaçlı). Nav bar yukarı kayar çünkü auto layout içinde → referans tasarımdan ölçüp nav bar'a **54 px top padding** ver.
4. [00:05:31] Orta içerik bloğu zaten dikey auto layout, gap = **Auto**.
5. [00:06:04] Shift ile sol menü + orta içerik + sağ menüyü seç → **Auto layout** ikonuna bas; yatay gap 0 olur, gap tutamacını sürükleyip **negatif gap** ile tasarıma oturt.
6. [00:06:39] Home indicator'ı da sil.
7. [00:07:00] Hâlâ uyarlanmıyor → yatay resizing'i **Fill container** yapmak gerekiyor; bunun için parent'ın auto layout olması şart.
8. [00:07:17] Nav bar'da "Controls" yazısı kırılıyor → başlık+içerik frame'ini auto layout'a çevir. Asimetri çıkarsa ölçüleri kontrol et: geri butonu **70 px**, ayarlar ikonu **41 px** → ikisini **aynı genişliğe** getir, aralarındaki gap'i **Auto** yap.
9. [00:08:47] Gereksiz/yinelenen katmanları sil (kod karmaşıklığını azaltır).
10. [00:09:26] Tüm ekrana auto layout uygula; kaydırma yapan görünmez elemanı sil; nav bar ile içerik arasına **10 px gap**.
11. [00:10:05] Nav bar'a ve ekran içeriğine **Fill container**.
12. [00:10:31] Sol menü frame'i (side menu + rectangle): frame olduğu için **constraints** kullan; sağ menüde constraint'leri **right**'a sabitle.
13. [00:11:28] Orta içeriğe de Fill container → artık dış frame boyutlanınca her şey uyarlanıyor.
14. [00:12:01] Küçültünce 360 px altında elemanlar çakışıyor → auto layout'ta **min width 360, max width 480** ayarla.
15. [00:13:06] Katmanları anlamlı isimlerle yeniden adlandır (kod export'unda dosya/bileşen adı olur).
16. [00:13:55] **SF Symbols** Locofy'de desteklenmiyor → SVG/PNG'ye çevir, Figma'ya import et; `Cmd+C` → orijinal ikonu seç → `Cmd+Shift+R` (paste to replace). Nav bar, sağ menü ve "Drive Mode" bölümündeki küçük chevron'lar için tekrarla.

### Bölüm 2 — Locofy ile design → code [00:14:52 – 00:28:58]
17. [00:15:41] Figma Plugins menüsünden **Locofy** kur, frame'i seç → **New project**; proje adı → tip: **Mobile app > React Native** → TypeScript/JS seçimi → ortam: **Expo** (React Native CLI yerine; gerekçe: yeni app için framework önerisi, OTA update, Expo Go).
18. [00:17:48] Plugin adımları: Optimize design → Tag (image/button/header) → Edit styles/layout → Actions. Stil ve etkileşimleri kodda yapacakları için sadece **Sync to Builder** yapıyor.
19. [00:19:39] Builder: sağda canlı prototip önizleme, solda framework dosyaları (app, `screens/DrivingPerformance`, global styles).
20. [00:20:29] **Create components**: AI "Navigation bar" ve "Drive mode" bileşenlerini öneriyor → kabul [00:21:20].
21. [00:21:30] Önizlemede elemana tıkla → ilgili kod satırı vurgulanır.
22. [00:22:16] Layer panelinden sırayla **Left Menu**, **Right Menu**, **Power Button**, **Speedometer** (görsel) için "create component"; her seferinde ekran kodu kısalıyor. Gerekçe: bu bileşenler animasyonun uygulanacağı birimler.
23. [00:25:02] Export: tüm ekran + tüm bileşenler → zip (asset'ler ve klasör yapısıyla).
24. [00:26:03] VS Code'da aç; Locofy'nin yazdığı README'yi oku; Node.js kurulu olsun; `Cmd+\`` terminal → `npm install` (vulnerability uyarısı çıkarsa terminaldeki komutu çalıştır) → `npx expo start`.
25. [00:27:08] Görüntüleme: Expo Go ile QR, web, Android veya iOS simülatör; Xcode kur → terminalde `i` tuşu ile iOS Simulator.
26. [00:28:13] Locofy klasör yapısı: `assets/`, `components/`, `screens/` (şimdilik sadece DrivingPerformance), **global styles** dosyası (renk/font/spacing tek yerden).

### Bölüm 3 — Git ile yerel sürüm kontrolü [00:29:01 – 00:38:46]
27. [00:29:47] Expo'yu `Ctrl+C` ile durdur, `git --version`.
28. [00:30:07] `git init` → `git status` (untracked dosyalar kırmızı).
29. [00:31:10] `git add <dosya>` (örn. LeftMenu bileşeni), `clear`, `git status`; geri almak için `git reset <dosya>` veya `git reset`; hepsini eklemek için `git add .`.
30. [00:32:32] `git commit -am "<mesaj>"` (ilk commit: tüm dosyalar). `git log`, `git log --oneline`.
31. [00:33:55] Değişiklik örneği: Speedometer bileşeninde görseli **100x100** yap; Expo'yu yeniden başlat; `git diff` (kırmızı eski / yeşil yeni, `q` ile çık); `git status`; `git commit -am "..."`.
32. [00:35:49] Eski commit'e `git checkout <hash>` → detached HEAD; görseli **200x200** yap; `git reset <hash>` (değişiklikleri koru, unstage); `git restore <dosya>` (değişikliği at); `git switch main` → 100x100'e döner.
33. [00:37:28] Kalıcı geri dönüş: `git log` ile hash bul → `git reset --hard <hash>` → görsel ilk haline (**300x300**) döner. Öğüt: sık commit at, kayıt noktaları oluştur.

## Kullandığı prompt'lar (varsa aynen)
Ücretsiz parçada **hiç AI prompt'u yok**. Locofy Builder'da AI'ın bileşen önerisi ("Navigation bar", "Drive mode") tıklamayla kabul ediliyor; yazılı prompt girilmiyor. Claude Sonnet 3.5 ile animasyon prompt'ları ücretli kursta.

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Önce tasarımı responsive yap, sonra koda çevir**: tüm ekran auto layout, her blok Fill container; sabit px yok. Kodda bu, flex tabanlı, cihazdan bağımsız düzen demek.
- **Ölçü disiplini**: nav bar top padding 54, nav bar–içerik gap 10, nav bar'da ikon kutuları eşit genişlik (70→41 asimetrisi düzeltildi) + gap Auto → simetrik, dengeli başlık çubuğu.
- **Min 360 / max 480 px** genişlik aralığı: küçük telefonda çakışma yok, tablet/katlanabilirde içerik aşırı yayılmıyor.
- **Katman temizliği ve adlandırma**: gereksiz katman silmek üretilen kodu sadeleştiriyor; isimler bileşen adına dönüşüyor.
- **İkonlar SVG/PNG**: SF Symbols yerine export edilmiş vektör; export sonrası kodda manuel değiştirme serbest.
- **Bileşenlere bölme animasyon için**: LeftMenu / RightMenu / PowerButton / Speedometer ayrı dosyalar; kurs devamında Reanimated `withSpring` / `withTiming` ile menü fade-scale-translate animasyonları bu birimlere uygulanıyor.
- **Global styles dosyası**: renk, font, spacing tek merkezde → tutarlı görünüm.
- Referans tasarım: Dropbox asset'lerindeki Figma dosyası (araba gösterge paneli; sol/sağ menü, speedometer, güç butonu, drive mode satırları).

## Hatalar ve çözümleri
- [00:04:58] Status bar silinince nav bar yukarı kaydı → 54 px top padding.
- [00:06:24] Üç bloğu auto layout'a alınca gap 0 ve üst üste bindi → negatif gap.
- [00:07:17] "Controls" yazısı kırıldı → frame'i auto layout yap, kutuları eşit genişlik, gap Auto.
- [00:07:47] Auto layout sonrası asimetri → eleman ölçülerini kontrol et (70 vs 41 px).
- [00:09:31] Tüm ekrana auto layout verince kayma → görünmez elemanı sil.
- [00:12:01] 360 px altında çakışma → min width 360.
- [00:13:55] SF Symbols Locofy'de yok → SVG/PNG'ye çevirip paste-to-replace.
- [00:26:51] `npm install` vulnerability uyarısı → terminaldeki `npm audit fix` benzeri komutu çalıştır.
- [00:36:05] Detached HEAD → `git switch main`; kalıcı geri dönüş `git reset --hard <hash>`.

## Bizim fabrikaya alınacaklar (somut, maddeli)
1. **Figma → kod öncesi kontrol listesi** (skill veya CLAUDE.md kuralı): tüm frame'ler auto layout, Fill container, min 360 / max 480, status bar + home indicator kaldırılmış, katmanlar adlandırılmış, ikonlar SVG. Müşteri tasarımı gelirse bu listeden geçmeden Claude'a verilmesin.
2. **Nav bar kuralı**: sol/sağ aksiyon kutuları eşit genişlik, aralarında `justifyContent: 'space-between'`; üst boşluk `useSafeAreaInsets().top` (videodaki 54 px'in kod karşılığı). Android'de ham status bar/pop-up görünümünü engellemek için `expo-status-bar` + edge-to-edge ayarı.
3. **Bileşen granülerliği**: her animasyon alacak parça ayrı bileşen (`components/LeftMenu.tsx` gibi). CLAUDE.md'ye: "Animasyon uygulanacak her blok kendi bileşeni olsun; ekran dosyası 150 satırı geçmesin."
4. **Global tema dosyası zorunlu**: `theme/tokens.ts` (renk, spacing 4/8/12/16/24, radius, font) ve hiçbir ekranda hard-coded hex yok. Locofy'nin "global styles" dosyasının bizdeki karşılığı.
5. **İkon politikası**: SF Symbols / Material yerine proje içinde `assets/icons/*.svg` + `react-native-svg`; Claude'a "ikonları SVG bileşeni olarak üret" dedirt.
6. **Git kayıt noktası disiplini**: her fazdan sonra `git commit -am`, beğenilmeyen üretimde `git reset --hard <hash>`; Claude Code'a "her büyük adımdan sonra commit at" kuralı (bkz. Beto videosu).
7. **Locofy'yi opsiyonel "piksel-sadık iskelet" aracı olarak değerlendir**: müşteri Figma'sı varsa Locofy ile Expo iskeleti alıp Claude ile animasyon/etkileşim eklemek, sıfırdan "ekranı tarif et" yaklaşımından daha sadık sonuç verir. Pahalı/kilitli olursa alternatif: Figma MCP + build-native-ui skill.
8. **Reanimated animasyon paketi** (kursun vaat ettiği ama ücretsiz parçada olmayan kısmı biz tamamlayalım): menü aç/kapa `withSpring`, görünürlük `withTiming` opacity, ölçek `scale`, kaydırma `translateX` — bunları hazır hook'lar olarak skill'e koy (`useMenuAnimation`, `useFadeIn`).
