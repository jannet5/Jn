# How to Build ACTUALLY Beautiful UI With This Claude Code Skill — Build Great Products — 12 dk — https://www.youtube.com/watch?v=95_NJ-a-CMQ

## Tek paragraf: ne yapıyor, sonuç ne
15 yıllık ürün/tasarım danışmanı Chris, Anthropic'in resmi `frontend-design` skill'inin (Claude Code plugin) "jenerik AI estetiği"ni nasıl kırdığını üç turda gösteriyor: (1) tasarım talimatı olmadan Claude Code'a bir AI ajansı landing page'i yaptırıyor — sonuç mavi-mor, sıradan; (2) skill'i kurup yalnızca "use the front-end design skill to improve the design" diyor — siyah/beyaz/tek vurgu rengi, özenli tipografi, scroll'da glow, fade-in animasyonlar, hover efektleri, arka planda ince grid deseni gelir; (3) Dribbble'dan bir referans ekran görüntüsü ekleyip aynı skill'le tekrar tasarlatıyor — "brutalist"ten "soft modern"e geçiş: pastel gradient'ler, blur'lu yumuşak gölgeler, yuvarlak köşeler, Outfit fontu, smooth scroll, underline animasyonları. Sonuç "AI araçlarının çok büyük kısmından iyi" ama bazı bölümler (kart olmayan metin blokları, emoji ikonlar, kırık hizalama, yine mora kayma) düzeltme istiyor. Önerisi: bir sonraki seviye için kod tabanında bir design system kurup skill'i ona göre çalıştırmak. Web landing page örneği; mobil değil, ama skill + referans görsel yöntemi doğrudan fabrikaya taşınabilir.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code (Cursor içindeki terminalde `claude` ile çalıştırılıyor)
- Cursor (editör/terminal)
- **frontend-design skill** (Anthropic resmi plugin; açıklaması: "Create distinctive production grade front-end interfaces with high design quality, generates creative polished code that avoids generic AI aesthetics")
  - Kurulum (açıklamadan aynen):
    ```
    /plugin marketplace add anthropics/claude-code
    /plugin install frontend-design@claude-code-plugins
    ```
  - Kaynak repo: https://github.com/anthropics/claude-code (marketplace)
- Dribbble — UI ilham/referans kaynağı (arama: "AI website design")
- Viral X gönderisi (235.000+ görüntülenme) — skill'in "music player app" örneği (teenage engineering tarzı)
- Açıklamadaki linkler:
  - Ücretsiz AI playbook'lar: https://skool.com/claudebuilders/about
  - AI ürün tasarım sistemi: https://skool.com/aiapps/about
  - 1-1 çalışma: https://www.buildgreatproducts.com/product-studio
  - 6 saatlik Claude Code kursu: https://youtu.be/_0E-dzhjCoY?si=P8wW_byqWR1ykTP-

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:43] X gönderisini gösteriyor: tek prompt'la ("use this front-end design skill in claude code. Create a music player app") gelen yüksek kaliteli tasarım.
2. [00:01:21] Plan: önce baz site → skill ile redesign → referans görselle redesign.
3. [00:01:41] Cursor'da boş "claude design skill" projesi, terminalde `claude` çalıştırıyor; tasarım bilgisi vermeden landing page prompt'u (aşağıda #1).
4. [00:02:30] Baz sonuç: mor gradient yok ama "bluey purple"a varsayılan, basit, jenerik.
5. [00:03:00] Claude Code sohbetine `/plugin marketplace add anthropics/claude-code` yapıştırıyor → marketplace eklendi.
6. [00:03:23] `/plugin install frontend-design@claude-code-plugins` → "Install now".
7. [00:04:00] Bağlamsız tetik: "use the front end design skill to improve the design of this landing page" (prompt #2).
8. [00:04:34] Sonuç incelemesi: tipografi, scroll'da glow, fade-in, hover, düzgün padding, menü butonlarında glow, tutarlı stil, siyah-beyaz-tek renk palet.
9. [00:05:44] Eleştiri: ana CTA siyah zeminde öne çıkmıyor (mavi olmalı), social proof eklenebilir, sağ altta anlamsız kare animasyon (hata), bazı bölümler kart olmalı, başlık hizası bozuk.
10. [00:07:11] Dribbble'da "AI website design" arıyor; mor gradient'li "AI'ish" olanları eliyor, temiz, birkaç renkli, farklı tipografili bir tasarımı seçip ekran görüntüsünü panoya kopyalıyor.
11. [00:07:53] Ekran görüntüsünü yapıştırıp prompt #3'ü gönderiyor.
12. [00:08:30] Claude'un kendi değişiklik listesi: brutalist → soft modern (aşağıda ayrıntı).
13. [00:09:23] Final: smooth scroll, soft shadow'lu hover, underline animasyonu, pastel gradient arka plan; kalan sorunlar: emoji ikonlar, kırık biçimlendirme/ikonlar, metin-üstü çakışmalar, bazı bölümler hâlâ dağınık, yer yer mora dönüş.
14. [00:10:42] Sonraki seviye: kod tabanında design system oluşturup skill'e "bu framework'ü takip et" dedirtmek.

## Kullandığı prompt'lar (varsa aynen)
1. Baz landing page [00:01:41]:
```
let's create a simple landing page for an AI agency that builds and sells custom AI apps to businesses to save them time, automate processes and make them more money. Research other AI agencies to create highquality copy that converts potential clients to book a call. follows best practice for agency landing pages to maximize the number of call bookings that we would get.
```
2. Skill ile bağlamsız redesign [00:04:00]:
```
use the front end design skill to improve the design of this landing page
```
3. Referans görselle redesign [00:07:53] (+ Dribbble ekran görüntüsü yapıştırılarak):
```
let's use the front end design skill to improve the design of this landing page following the attached screenshot
```
4. X gönderisindeki örnek kullanım:
```
use this front-end design skill in claude code. Create a music player app
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Skill'in kendisi**: frontend-design, "generic AI aesthetics"ten (mor gradient, Inter, emoji ikon) kaçınmaya ve cesur bir estetik yön seçmeye zorlayan talimat seti. Tek cümlelik tetik bile ciddi fark yaratıyor.
- **Renk kuralı (Chris'in tavsiyesi)**: "Black, white, and one color" — siyah, beyaz ve tek vurgu rengiyle başla, gerekirse üstüne ikinci palet ekle.
- Skill'in 1. turda eklediği detaylar: güçlü tipografi, scroll'la beliren glow, bölümlerde fade-in animasyonları, kartlarda hover efekti, menü butonlarında glow, kart arkasında çok ince gri grid deseni, tutarlı padding.
- Referans görsel turundaki dönüşüm (Claude'un listesi):
  - black/white/cyan → soft gradients & pastels
  - harsh shadows → soft shadows with blur
  - sharp edges → rounded everything
  - monospace fonts → modern sans-serif (**Outfit**)
  - geometric icons → emoji icons (olumsuz; skill emojiye geri düşüyor)
  - terminal aesthetic → premium SaaS feel
  - data streams & grids → floating gradient blobs
  - hard transitions → smooth animations (smooth scroll, link underline animasyonu)
- **Referans görsel kaynağı seçimi**: Dribbble'da mor gradient'li "AI'ish" sonuçları bilinçli eliyor; temiz, az renkli bir referans seçiyor.
- Tasarımcı gözüyle eleştiri kriterleri: above-the-fold CTA kontrastı, social proof, kart vs düz metin, hizalama, ikon tutarlılığı.

## Hatalar ve çözümleri
- Jenerik mavi-mor AI tasarımı → frontend-design skill'i kurup açıkça "use the front end design skill" demek.
- Sağ altta anlamsız kare animasyon, bazı bölümlerde kart olmayan dağınık metin, bozuk başlık hizası → videoda çözülmedi; ek iterasyon gerekiyor.
- Primary CTA siyah zeminde görünmüyor → vurgu rengine (mavi) çekilmeli (önerildi).
- Emoji ikonlar, kırık ikon/formatlar, yer yer mora dönüş → skill tek başına yetmiyor; çözüm önerisi: kod tabanında design system + referans görsel.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- `frontend-design` plugin'ini fabrikanın standart kurulumuna ekle (`/plugin marketplace add anthropics/claude-code` + `/plugin install frontend-design@claude-code-plugins`); her UI turunda prompt'a "use the frontend-design skill" ekle. Web odaklı olduğu için, SKILL.md'yi okuyup Expo/React Native'e uyarlanmış bir `mobile-design` skill'i türet (Reanimated, StyleSheet, native font yükleme vb.).
- İki aşamalı tasarım akışı: (1) işlev çalışsın, (2) skill + **referans ekran görüntüsü** ile redesign. Mobil için referans kaynağı Dribbble/Mobbin; prompt kalıbı: "use the frontend-design skill to improve the design of this screen following the attached screenshot".
- CLAUDE.md'ye tasarım yasakları: emoji ikon yok (ikon kütüphanesi kullan, ör. lucide-react-native / @expo/vector-icons), mor/mavi-mor varsayılan gradient yok, Inter/Roboto varsayılanı yok — markaya özgü font (`expo-font`, ör. Outfit).
- Palet kuralı: "siyah + beyaz + 1 marka rengi" ile başla; tokenları `theme.ts`'de tanımla.
- Zorunlu mikro-etkileşim listesi: ekran/öğe girişinde fade-in, basılabilir öğede press/hover geri bildirimi, blur'lu yumuşak gölge, yuvarlatılmış köşe, ince arka plan dokusu (grid/gradient blob) — Reanimated `FadeIn`/`withSpring` ile.
- Kalite kontrol checklist'i (tasarımcı gözü): ana CTA kontrastı, above-the-fold mesaj, kart tutarlılığı, hizalama, ikonların render olup olmadığı, renk sapması (mora dönüş) — her redesign sonrası ekran görüntüsüyle kontrol.
- Uzun vadede: her müşteri için kod tabanında design system (token + bileşen) oluştur ve skill'e "bu design system'i takip et" dedirt — videodaki "next level" önerisi.
