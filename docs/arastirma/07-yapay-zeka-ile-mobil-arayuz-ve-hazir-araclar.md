# Yapay zeka ile mobil arayüz, marka ve hazır araçlar
Araştırma kolu: yz-arayuz

Tarih: 21 Ekim 2026
Kapsam: Profesyoneller yapay zekayla bir mobil uygulamanın arayüzünü, markasını ve
animasyonlarını nasıl üretiyor ve koda bağlıyor; Sende (Kotlin + Compose) için hangi
araç hangi adımda kullanılmalı. aitmpl.com tek tek incelendi. Codex eşdeğerleri,
Android'e özel hazır paketler, ikon / font / animasyon seçenekleri ve "görselden
koda" yönteminin tuzakları var.

Bu dosya 04 (bildirim çubuğu) ve 06 (arayüz-tasarım referansları) ile çakışmasın diye
şunlar TEKRAR edilmedi: "AI slop" işaret kataloğu, 5 konseptin slop karşılaştırması,
başparmak ergonomisi, M3 Expressive yay değerleri. Onlar için 06'ya bak.

Okuma işaretleri
- [resmi] platformun veya şirketin kendi belgesi
- [kaynak kod] repo klonlanıp dosyalar okundu (anonim git clone)
- [ölçtüm] bu araştırmada kendi komutumla ölçtüm; komut ve sonuç yazılı
- [topluluk] Reddit, HN, dev.to gibi insan deneyimi
- [satıcı] aracı satan veya rakip şirketin blogu; taraflı say
- [tek kaynak] yalnız bir yerde geçiyor
- [çıkarım] benim yorumum
- [doğrulanamadı] açıklaması aşağıda

## Kısa özet (önce bunu oku)

- Stitch, Figma Make, v0, Magic Patterns, Claude Design: baktığım resmi ve satıcı
  sayfalarında hiçbiri Jetpack Compose çıktısı vermiyor. Çıktıları HTML/CSS, React,
  Next.js, Tailwind, Figma. Compose çıktısı gördüğüm iki yol: Android Studio
  içindeki Gemini (resmi) ve Builder.io Visual Copilot (satıcı iddiası).
  [resmi + satıcı]
  - https://developer.android.com/studio/gemini/generate-ui-with-images
  - https://builder.io/blog/convert-figma-to-jetpack-compose
  - https://github.com/google-labs-code/stitch-skills
  - https://www.anthropic.com/news/claude-design-anthropic-labs
- Bu yüzden bu araçlar Sende'de "referans görsel / keşif" içindir; kod kaynağı
  değil. Kod, Brand.kt jetonlarına bağlı yazılmalı. [çıkarım]
- Topluluk deneyimi aynı yöne gidiyor. r/androiddev'de bir kullanıcı: UI'da
  skill yardım etmiyor, çünkü "model ekranı göremiyor". HN'de "skill'ler hep aynı
  yere yakınsıyor" yorumu var. [topluluk]
  - https://reddit.com/r/androiddev/comments/1wc1ns5/best_claude_skills_you_found_as_a_android_dev/
  - https://news.ycombinator.com/item?id=49058547
- Çözüm modele ekranı göstermek: sahte veriyle @Preview + ekran görüntüsü testi.
  Sende'de Roborazzi var; kal. Google'ın resmi "Compose Preview Screenshot Testing"
  eklentisi deprecated oldu, yerine AGP test suites geliyor (hâlâ alpha). [resmi]
  - https://developer.android.com/studio/preview/compose-screenshot-testing
  - https://developer.android.com/studio/preview/compose-screenshot-testing-with-testsuites
- "Görselden doğrudan koda" tuzakları: tek ekran boyutu, mutlak konum, tahmini
  hex ve boşluk, bileşen yapısı yok, mantık yok. Akademik çalışma üç hata türü
  sayıyor: eksik öğe, bozuk öğe, yanlış yerleşim. [akademik + blog]
  - https://arxiv.org/abs/2406.16386
- Claude'a verilen görsel standart katmanda uzun kenar 1568 px'e küçülür. Telefon
  ekran görüntüsü (2400 px) küçülüp yazı okunmaz olur; kırp ve küçült. [resmi]
  - https://platform.claude.com/docs/en/build-with-claude/vision
- Google'ın resmi Android CLI + android/skills (Apache-2.0) var. Bilerek "LLM'in
  zaten iyi bildiği" temel Compose'u değil, LLM'in zayıf kaldığı işleri
  (edge-to-edge, test kurulumu, Play politikası) kapsıyor. [resmi + kaynak kod]
  - https://github.com/android/skills
  - https://developer.android.com/tools/agents/android-cli
- aitmpl.com: 1.910+ bileşen ama Android/Compose'a özel neredeyse hiçbiri yok.
  Bize yarayanlar az: frontend-design (süreç), android-cicd (yayın, sonra),
  app-store-optimization (mağaza metni), birkaç yardımcı. CLI'ı varsayılan
  telemetri gönderiyor; kapatma değişkeni var. [kaynak kod]
  - https://aitmpl.com
  - https://github.com/davila7/claude-code-templates
- [ölçtüm] material-icons-extended-android 1.7.8 AAR dosyası 35,7 MB. Google artık
  bu kütüphaneyi önermiyor; Material 3 1.4.0'dan kaldırıldı. Sende 13 ikon
  kullanıyor. Yeni Compose BOM'a geçmeden önce ikonları vektör dosyasına çevir.
  - https://developer.android.com/jetpack/androidx/releases/compose-material3
- [ölçtüm] 20 Google Fonts dosyasında Türkçe harflerin (ğ ş ı İ ö ü ç) hepsi var.
  Ama ₺ (Türk lirası) işareti 9 dosyada YOK: Sora, Instrument Sans, JetBrains
  Mono, Figtree, Geist, Hanken Grotesk, Rubik, Urbanist, Outfit. Konsept 4 ve 5
  ve konsept 2-4'ün mono yazısı etkileniyor. Konsept 1 (Manrope + Inter) temiz.
- [ölçtüm] Kotlin `lowercase()` "İNDİRİM" yazısını "i̇ndi̇ri̇m" (noktalı i + birleşik
  nokta) yapıyor; "indirim" araması bulamıyor. Sende'de Fingerprint,
  PromotionalHeuristics ve Images.kt etkileniyor (Bölüm 8).
- Lottie ve Rive ilk sürümde gerekmiyor. Rive'in Compose API'si beta. Lottie
  Apache-2.0 ve işe yarar ama APK 2,7 MB iken bir bağımlılık ve lisans takibi
  ekler. Compose'un kendi yay animasyonu ve graphics-shapes (kararlı) yeter.
- Reddit doğrudan 403 veriyor; arşiv API'siyle r/androiddev okundu. X.com 402,
  YouTube açıklamaları engelli. Ne bulunamadı: Stitch'ten Compose'a gerçek bir
  üretim deneyimi yazısı. Ayrıntı "Denenen ve çalışmayan yollar"da.

## Nasıl araştırdım

- Araçlar
  - WebSearch + WebFetch (genel web), Bash ile anonim git clone ve curl.
  - Hacker News Algolia API'si (yorumlar dahil).
  - Reddit arşiv API'si (arctic-shift), yalnız başlık araması ve yorumlar.
  - Stack Exchange API'si (az işe yaradı).
  - Python fontTools: font dosyalarının glif haritası ve alt küme ölçümü.
  - jshell/Java: Türkçe büyük-küçük harf davranışı.
- Klonlanan repolar (hepsi anonim, derinlik 1; iş bitince silindi)
  - davila7/claude-code-templates (aitmpl'nin kaynağı, MIT)
  - android/skills, google-labs-code/design.md, google-labs-code/stitch-skills
  - chrisbanes/skills, aldefy/compose-skill, Meet-Miyani/compose-skill
  - krutikJain/android-agent-skills, new-silvermoon/awesome-android-agent-skills
  - skydoves/compose-performance-skills, SangkuOh/build-androidos-apps
  - openai/plugins, hesreallyhim/awesome-claude-code, wasabeef/awesome-android-ui
  - mobile-next/mobile-mcp, CursorTouch/Android-MCP, zainkhalid91/spyglass
  - airbnb/lottie-android
- Ölçümlerim (komutlar dosyanın sonundaki "Ölçüm notları"nda)
  - 20 font için glif kontrolü, 3 font için alt küme boyutu.
  - Maven'den AAR boyutu (material-icons-extended-android, lottie-compose).
  - 5 konseptin WCAG kontrastı (hepsi AA geçiyor).
  - Java ile Türkçe harf dönüşümü.
- Görsel inceleme: Başkasının ekran görüntüsü veya tasarımı İNDİRMEDİM. Yalnız
  kod/metin okudum. İndirilen font dosyaları ve klonlar iş bitince silindi.
- Taraflılık uyarısı: Stitch, Figma Make, v0 karşılaştırmalarının çoğu rakip
  araç satan şirketlerin blogu (Banani, Mowgli, Builder.io, MiroMiro). Bunlar
  [satıcı] diye işaretlendi; yalnız iki satıcı aynı şeyi söylüyorsa güvendim.

## 1. AI tasarım araçları: ne üretiyor, Android'e uyuyor mu

### Google Stitch
- Ne: Google Labs'in ücretsiz yapay zeka arayüz tasarım aracı. Galileo AI'ın
  Google tarafından alınmış ve adı değişmiş hâli (Mayıs 2025). [satıcı + haber]
  - https://uxmagic.ai/blog/galileo-ai-is-now-google-stitch
- Çıktı (iki satıcı rakip aynı şeyi söylüyor): Figma'ya aktarma, HTML/CSS (Tailwind),
  zip, kod ajanlarına MCP. Compose veya XML yok. [satıcı, iki bağımsız rakip]
  - https://www.banani.co/blog/google-stitch-ai-review
  - https://mowgli.ai/blog/figma-make-vs-stitch-vs-claude-design-vs-v0-vs-lovable
  - Bir arama özeti Flutter/SwiftUI/Vue de sayıyor; resmi sayfadan doğrulayamadım.
    [doğrulanamadı] https://www.codecademy.com/article/google-stitch-tutorial-ai-powered-ui-design-tool
- Sınırlar (Banani, rakip): günlük 400 tasarım kredisi + 15 yeniden tasarım;
  kredi satın alınamaz; "kullanılabilir ama premium değil, renkli tel kafes gibi";
  3'ten fazla bağlantılı ekranda tutarlılık zor; bazı ülkelerde erişim yok.
  [satıcı, tek kaynak] https://www.banani.co/blog/google-stitch-ai-review
- DESIGN.md: Stitch'in tasarım kuralı dosyası. Google 21 Nisan 2026'da taslak
  şartnameyi açık kaynak yaptı (Apache-2.0). Jeton (YAML) + gerekçe (metin).
  Komutlar: `lint` (WCAG kontrastı dahil), `diff`, `export` (Tailwind, DTCG).
  Kotlin/Compose dışa aktarımı YOK; sürüm "alpha". npm paketi `@google/design.md`
  0.4.0 (kayıt defterinde son değişiklik 27 Temmuz 2026). [resmi + kaynak kod + ölçtüm]
  - https://blog.google/innovation-and-ai/models-and-research/google-labs/stitch-design-md/
  - https://github.com/google-labs-code/design.md
  - https://registry.npmjs.org/@google%2fdesign.md
- Resmi "stitch-skills" (Apache-2.0, 15 skill): React, React Native, shadcn, Remotion
  video, DESIGN.md üretimi. Android/Compose yok. "Resmi olarak desteklenen Google
  ürünü değildir" notu var. Claude Code, Cursor ve Codex için kurulum komutu var.
  [kaynak kod] https://github.com/google-labs-code/stitch-skills
- Gerçek deneyim
  - r/androiddev (Temmuz 2026, 5+ yorum): bir kullanıcı Stitch'i tel kafes için
    kullanıp Figma + Material bileşenleriyle cilalıyor; bir diğeri "Stitch ile
    tasarladım, son şekle Claude Design ile getirdim; Stitch'te her mesaj yeni
    bir konuşma gibi, Claude Design'da konuşma sürüyor" diyor; bir diğeri kendi
    tasarım sistemini verince Claude Design'dan "oldukça iyi" sonuç alıyor.
    [topluluk, 3 ayrı kişi]
    - https://reddit.com/r/androiddev/comments/1ut6fz6/ai_tools_for_android_ui_design_whats_worth_using/
    - https://reddit.com/r/androiddev/comments/1u0t6fz/app_ai_design/
    - Dikkat: bu konunun açılış yazısı bir araç derlemesi; bir kullanıcı yazarın
      aylardır hep Sleek'i anıp anmadığını (bağlı mı) sormuş. Reklam şüphesi var;
      açılış yazısındaki sıralamaya güvenme, yorumlardaki kişisel deneyimleri say.
  - HN: bir geliştirici Stitch ile ilk ekranları üretip Codex/Claude Code'a
    verip düzeltiyor; "birkaç ekrandan sonra kalıplar oturuyor ve model sonraki
    ekranlarda onları örnek alıyor". [topluluk, tek yorum]
    - https://news.ycombinator.com/item?id=47074767
  - YouTube: "Cursor AI ve Google Stitch ile Jetpack Compose arayüzü" başlıklı bir
    anlatım var (UiLover kanalı). Açıklaması engelli olduğu için içeriği okunamadı.
    [doğrulanamadı] https://www.youtube.com/watch?v=rUMdODftxQE
- Android için değerlendirme [çıkarım]: Tel kafes ve renk/yerleşim keşfi için
  bedava ve yeterli. Çıktısını Compose'a çevirtme; PNG referans olarak al.

### Claude Design (Anthropic)
- Ne: 17 Nisan 2026'da araştırma önizlemesi. Komutla prototip, slayt, tek sayfa.
  Girdi: metin, görsel, belge, kod tabanı, web yakalama. Çıktı: URL, klasör, Canva,
  PDF, PPTX, HTML ve "Claude Code'a teslim paketi". Kod tabanını okuyup tasarım
  sistemi kurar. Pro/Max/Team/Enterprise abonelerine, kullanım limitine sayılır.
  [resmi] https://www.anthropic.com/news/claude-design-anthropic-labs
- Android: yerel mobil çıktı belgelenmemiş; çıktılar HTML tabanlı. [çıkarım]
- Deneyim: Reddit'te "iyi ama çok token yiyor" (iki ayrı yorum), "Stitch'ten
  sonra cilayı Claude Design'la yaptım". [topluluk]
  - https://reddit.com/r/androiddev/comments/1ut6fz6/ai_tools_for_android_ui_design_whats_worth_using/
  - HN'de bir yorum: Claude Design çıktısının "kokusu belli" (LLM tutarlılık için
    eğitildiği için tasarımda yaratıcılık zayıf). Yorumcu rakip bir şirketin
    kurucusu (taraflı). https://news.ycombinator.com/item?id=50107557

### Figma Make
- Ne: Figma içinde istemle çalışan, kodlu prototip/web uygulaması üretici
  (ana dosya App.tsx, yani React). 28 Mayıs 2026'da "yerel kodunda Make" beta:
  tasarımcı mevcut koda Figma'dan dokunuyor; yalnız Mac beta uygulaması,
  bekleme listesi. Hangi yığınları desteklediği belirtilmemiş. [resmi + satıcı]
  - https://www.figma.com/blog/figma-make-now-on-your-local-code/
  - https://mowgli.ai/blog/figma-make-vs-stitch-vs-claude-design-vs-v0-vs-lovable
- Android: Compose çıktısı yok. Figma'da zaten çalışan bir tasarımcı varsa anlamlı;
  tek kişilik Sende için gereksiz maliyet. [çıkarım]
- Ücret (satıcı): ücretsiz katmanda günlük 150 kredi, aylık en fazla 500.

### v0 (Vercel)
- Ne: Next.js + Tailwind + shadcn/ui üretir. Expo/React Native belgede geçmiyor.
  Ekran görüntüsü veya Figma'dan "klonlama" var. [resmi]
  - https://v0.app/docs
- Topluluk: "modeller tasarımda, özellikle tüm uygulamada tutarlı kenar boşluğu ve
  kontrast konusunda zorlanır; v0 varsayılan olarak shadcn'e gider" (2025). [topluluk]
  - https://news.ycombinator.com/item?id=43346628
- Android: uygun değil. Karar: kullanma.

### Magic Patterns, Galileo, Uizard ve diğerleri
- Magic Patterns: istemle yüksek doğruluklu arayüz, sonsuz tuval, mevcut tasarım
  sistemini içe aktarma; "kazananı kodlama ajanına ver". Çerçeve adı sayfada yok.
  Mobil uygulama şablonu var. Ücret: Starter 20 $/koltuk/ay. [resmi]
  - https://www.magicpatterns.com/
  - Mart 2026'da kullanım tabanlı krediye geçtiği bir arama özetinde geçiyor
    (karmaşık istek daha pahalı); sayfayı açmadım. [doğrulanamadı]
    https://alloy.app/library/magic-patterns-reviews-pricing-and-alternatives
- Galileo AI: bağımsız ürün olarak yok; Stitch oldu. galileo.ai alan adı başka bir
  ürüne geçmiş (arama özeti). [satıcı + topluluk]
  - https://uxmagic.ai/blog/galileo-ai-is-now-google-stitch
- Uizard: Haziran 2024'te Miro aldı; ürün aynı kaldı. "Autodesigner" adlı ürün
  hakkında bilgi bulunamadı. [haber + doğrulanamadı]
  - https://sy.dealroom.co/news/feed/miro-acquires-uizard-expands-design-tools
- Open Design, Sleek, UX Pilot, PictorX: Reddit yorumlarında anılıyor; Sleek
  reklam şüphesi taşıyor. Ben denemedim. [topluluk, güvenilirliği düşük]
- Penpot (açık kaynak tasarım aracı) resmi MCP sunucusu var: dosyayı okur,
  jeton oluşturur, HTML/CSS üretir. Lisansı bu araştırmada doğrulanamadı.
  Tek aktif sekme, tek anahtar, sekme uyursa bağlantı düşer. [resmi]
  - https://help.penpot.app/mcp/

### El çizimi ve "görüntü modeli mimar olsun" yaklaşımı
- HN'de bir kullanıcı: skill'ler, shuffle.dev, ekran görüntüsü örnekleri,
  Figma eklentileri denedi; "en hızlı ve daha az yapay görünen yol kağıda ayrıntılı
  çizip Figma/Penpot'a aktarıp görüntü alan modele vermek". [topluluk, tek yorum]
  - https://news.ycombinator.com/item?id=49058547
- Başka bir yorumcu (Figma'dan ayrılıp bu işe girişen biri, taraflı): görüntü
  üreten (difüzyon) modeller LLM'lerden daha iyi "yaratıcı tasarımcı", görselden
  HTML'e yaklaşım daha iyi sonuç veriyor. [topluluk, tek kaynak, taraflı]
  - https://news.ycombinator.com/item?id=50107557
- [çıkarım] İki yorum da aynı şeyi söylüyor: araç ürüne özgü fikri bulmuyor, o
  fikri insan koyuyor (06'daki "bu konsept neden bu ürünün konsepti" sorusu).

## 2. Claude / Codex / Cursor ile Compose arayüzü: gerçek deneyimler

### Erişilebilirlik deneyi (en sağlam veri)
- Eevis (Eevajonna Panula), 2025: aynı istemi Gemini, Junie, Cursor ve Claude'a
  verip dört Android uygulamasını TalkBack, Switch Access, büyük yazı ve
  klavyeyle denedi. Sonuç: "insan yapımı uygulamalardakine benzer sorunlar".
  [topluluk, ayrıntılı deney]
  - https://dev.to/eevajonnapanula/does-ai-generate-accessible-android-apps-2gb5
  - https://dev.to/eevajonnapanula/does-claude-generate-accessible-apps-5759
- Bulunan sorunlar
  - Dördü de görünen yazıyı tekrarlayan gereksiz `contentDescription` yazdı
    (Claude ayrıca "Tap for details" ekledi).
  - Gemini, Junie, Cursor ekranı kaydırılabilir yapmadı; büyük yazıda içerik kesildi.
    Claude bu konuda iyiydi.
  - Claude, var olmayan `Role.TextField` yazdı; bu, yapay zeka çıktısının
    uygulamayı DERLEMEYE ENGEL olduğu ilk durumdu (yazar öyle diyor).
  - Claude'da düğmeli gezinme açıkken sistem çubuğu "Add needles" düğmesini örttü.
    Bu, kurucunun I-62 isteğiyle (alt düğmeler gezinme tuşlarına binmesin) birebir.
  - Claude'un yazdığı `AccessibilityExtensions.kt` hiç kullanılmadı.
- Okur yorumu (yazarın katıldığı): "modeller kalıp-tamam kod üretir, bağlam-tamam
  değil". [topluluk, yorum]
- Akademik tez (UFC, 2025): LLM'lerin ürettiği yerel Android arayüzlerinde 702
  erişilebilirlik sorunu; Compose diğerlerinden iyi; "erişilebilir yap" diye
  açıkça isteyen istemler bazen DAHA ÇOK hata üretmiş. Yalnız arama özetinden
  okudum, sayfa 503 verdi. [tek kaynak, doğrulanamadı]
  - https://repositorio.ufc.br/ri/handle/riufc/81252?mode=full
- [çıkarım] "Erişilebilir yap" istemine güvenme. Kural: otomatik test (Bölüm 15)
  ve elle TalkBack kontrolü.

### Reddit r/androiddev: Claude Code ile Android (Eylül 2026)
- Başlık: "Android geliştiricisi olarak bulduğun en iyi Claude skill'leri?" Açan kişi
  mimari/hata sorunlarının çoğunun gittiğini, ama "UI/UX'te zorlandığını ve
  skill'lerin yardım etmediğini" yazıyor. [topluluk]
  - https://reddit.com/r/androiddev/comments/1wc1ns5/best_claude_skills_you_found_as_a_android_dev/
- En çok oy alan yanıt (13): hiçbir skill işe yaramadı; kuralları teste çevirmek
  işe yaradı. "UI'da skill'in yardım edebileceğini sanmıyorum; model ekranı göremez;
  ona geri bildirim veren bir şey ver." Flutter'da yazı ölçeğini 1.0-2.0 arası
  deneyen widget testleri ve taşmada başarısız olan testlerle çalışıyor; Android için
  Paparazzi/Roborazzi'yi anıyor (arşivdeki yorum metni burada kesik). [topluluk]
- Bir başka yanıt (3): "Google'ın skill'leri testing-setup dışında neredeyse
  işe yaramaz; en iyi skill kendi projen için yazdığındır." [topluluk]
  - Bu, android/skills README'sindeki bilinçli tercihle uyumlu (aşağıda).
- Bir başka yanıt: Android CLI'nin kurduğu skill'ler "iyi verim veriyor; ama ajan
  her değişiklikten sonra derleme ve test yapmaya takıntılı hâle geliyor, ucuz
  planda token israfı olur." [topluluk]
- Başka yanıtlar: "Android Studio IDE MCP eklentisini kullan", "Figma kullanıyorsan
  Figma MCP'yi bağla", "test için Android/Mobile MCP". [topluluk]
- Sende'ye çıkarım: AGENTS.md'ye "ekranı değiştirdiysen yalnız şu Gradle görevini
  çalıştır; her değişiklikte tam derleme yok" yazılmalı. [çıkarım]

### Diğer tek kişilik deneyimler
- "İki katmanlı AI protokolü": Claude.ai tasarım ve gereksinim yazıyor, Claude Code
  yalnız uyguluyor, insan gerçek cihazda test edip karar veriyor. Kütüphane seçiminden
  önce uygulayan yapay zekanın o kütüphaneyi iyi bildiğini kontrol et. Emülatörde
  görünen yazı tipi/glif hatası "küçük ama utanç verici"ydi; yazı tipini uygulamaya
  koyarak çözdü. Bu, Türkçe karakterler ve ₺ için de aynı ders. [topluluk, tek kişi]
  - https://dev.to/raio/i-built-an-android-app-in-4-days-with-zero-android-experience-using-claude-code-and-a-two-layer-2p44
- Vmobify yazısı (genel, ajansın kendi blogu): "derlenir ama yanlış çalışır" kalıpları;
  erişilebilirlik yardımcıları üretilir ama kullanılmaz; uzun oturumda ajan önceki
  kararlarla çelişir; çözüm: ajanın tekrar okuduğu bir spec dosyası ve ~200 satırlık
  CLAUDE.md. Rakamları ölçümsüz. [satıcı]
  - https://vmobify.com/blog/claude-code-android-development
- HN (2024): bir geliştirici SwiftUI kodunu ChatGPT/Claude ile Kotlin + Compose +
  Material3'e çevirmiş; "çok iyi çalıştı, yalnız stil/tema düzeltmesi gerekti".
  Kod-koda çeviri, görsel-koda çevirmeden çok daha güvenilir görünüyor. [topluluk,
  tek yorum] https://news.ycombinator.com/item?id=41387764

### Resmi Gemini (Android Studio): görselden Compose
- Wireframe veya el çizimi PNG eklenir; "Jetpack Compose kodu üret" denir; ajan
  kodu ve önizlemeyi verir. Önizlemede öğe seçip değiştirtebilirsin;
  "AI Actions > Match UI to Target Image" ve "Fix all UI check issues" (erişilebilirlik
  gibi sorunlar) var. Yalnız ücretsiz Gemini katmanında. Belge 31 Ağustos 2026. [resmi]
  - https://developer.android.com/studio/gemini/generate-ui-with-images
- Mart 2025 duyurusunda Google çıktıyı "ilk taslak" olarak tanımlıyor; içe aktarma ve
  ikon düzeltmesi gerekir. [resmi]
  - https://android-developers.googleblog.com/2025/03/multimodal-image-attachment-now-available-gemini-android-studio.html
- Sende'de CI ve ajan komut satırından çalışıyor; Android Studio bir kişilik
  elle yardımcı, hattın parçası olamaz. [çıkarım]

### Hazır Compose skill'leri ne kadar işe yarıyor
- Google'ın android/skills README'si: "LLM'lerin zayıf kaldığı iş akışlarına
  odaklanıyoruz; temel Jetpack Compose gibi LLM'lerin zaten iyi olduğu yerlere
  öncelik vermiyoruz." [resmi]
  - https://github.com/android/skills
- aldefy/compose-skill (MIT) sorunu şöyle tarif ediyor: AI "derlenen ama ayrıntıyı
  yanlış yapan" Compose üretir: yanlış `remember`, kararsız yeniden kompozisyon,
  bozuk modifier sırası, uydurma API. Çözüm: 24 başvuru rehberi + androidx kaynak
  kodundan "makbuz". [kaynak kod, kendi iddiası]
  - https://github.com/aldefy/compose-skill
- Meet-Miyani/compose-skill (MIT) kendi ölçümünü yayımlamış: 7 model, 90+36 ajan
  çalıştırması, iki bağımsız puanlayıcı. Skill ile puan artmış (örnek: Sonnet 5.5
  %79,5'ten %87-90'a). Aynı yazıda bir kusur da var: "bizim kuralları uygula"
  görevinde modeller fazla yeniden yapılandırmış. Ölçümü yapanın kendisi;
  bağımsız doğrulama yok. [tek kaynak, kendi ölçümü]
  - https://github.com/Meet-Miyani/compose-skill
- [çıkarım] Skill'ler "tarz" için değil "API doğruluğu" için; marka tarzını
  jetonlar ve ekran görüntüsü testleri taşıyor.

### Cursor ve Codex tarafı (kısa)
- Cursor: `.cursor/rules/*.mdc`; AGENTS.md'yi de okuyor (agents.md sayfası listeliyor).
  PatrickJS'in "Android Jetpack Compose" kuralı eski `.cursorrules` biçiminde, CC0
  lisanslı; içeriği genel (Material 3, Hilt, StateFlow). [topluluk + resmi]
  - https://agents.md
  - https://www.mdskills.ai/rules/cursor-android-jetpack-compose?tab=source
- Codex: AGENTS.md okur; sınırı 32 KiB (`project_doc_max_bytes`), aşılırsa
  keser. Kök, alt klasör ve `AGENTS.override.md` zinciri var. [resmi]
  - https://learn.chatgpt.com/docs/agent-configuration/agents-md

## 3. "Görselden doğrudan koda" yönteminin tuzakları

- Tek ekran boyutu. Ekran görüntüsü tek bir viewport; kartlar küçük ekranda
  alt alta mı iner, yazı büyüyünce ne olur belli değil. [blog]
  - https://dev.to/arsalanmeee/screenshot-to-code-sounds-easy-here-are-5-things-ai-still-gets-wrong-12j0
  - Android karşılığı: 360/411 dp genişlik, yazı ölçeği 2.0, katlanabilir, Arapça
    (RTL). Bunların hiçbiri PNG'de yok. [çıkarım]
- Mutlak konum. Model `position:absolute; top:142px` gibi tek ekranda tutan değerler
  üretir. Çözüm: ekranı üretebilecek yerleşim sistemini kurmak. [blog]
  - Compose karşılığı: sabit `offset`/`size(…dp)` yığınları. [çıkarım]
- Bileşen mimarisi yok. Tek dev fonksiyon, tekrarlanan işaretleme. Hiyerarşiyi
  sen belirle. [blog] (aynı kaynak). aldefy rehberi (771 satır) ayrıştırma
  algoritması veriyor: kök yerleşim -> bölümler -> yerleşim türü -> özellik çıkarma
  -> M3 bileşen eşleme. [kaynak kod]
  - https://github.com/aldefy/compose-skill
- Mantık ve durum yok. Düğmenin ne yaptığı, boş/yükleniyor/hata durumları
  resimde görünmez. [blog] Sende'de bu durumlar zaten sahte veri (fixture) ile
  tanımlı; onlardan başla. [çıkarım]
- Küçük ayrıntılar: yazı ağırlığı, satır yüksekliği, harf aralığı, köşe, gölge,
  ikon boyu, düğme yüksekliği. Düzen tutsa bile "kötü hissettiren" şey budur. [blog]
- Hex ve boşluk tahmini. PNG'de gerçek jeton yok; model piksel renginden hex,
  sürekli değerden boşluk uydurur. Önerilen çözüm: izin verilen boşluk/köşe
  kümesini istemde sınırla; Figma varsa MCP ile gerçek değişkenleri ver. [blog]
  - https://dev.to/juholee/figma-mcp-claude-code-give-it-your-real-layout-variables-and-components-497m
  - https://www.digitalapplied.com/blog/screenshot-driven-ui-development-vision-models-2026
- Üç hata türü (GPT-4o'yla, web sayfalarında): öğe eksik kalır, öğe bozulur,
  öğe yanlış yere gider. DCGen yöntemi (ekranı parçalara bölüp parça parça
  üretmek) büyük görsellerde görsel benzerliği %15'e kadar artırdı. FSE 2025.
  [akademik; web sayfası, mobil değil] https://arxiv.org/abs/2406.16386
- Bağlam maliyeti ve görüntü sınırı
  - Claude standart katmanda uzun kenarı 1568 px'e küçültüyor; 1920x1080 -> 1456x819.
    "Yazı küçükse veya net değilse okunmayabilir"; sayma ve yer belirleme
    "yaklaşık". 4.7 ve sonrası modeller 2576 px'e kadar tam çözünürlük. [resmi]
    - https://platform.claude.com/docs/en/build-with-claude/vision
  - Telefon ekran görüntüsü 1080x2400 gibi: uzun kenar 2400 px; standart katmanda
    küçülür. [çıkarım] Çözüm: bölüm bölüm kırp; Roborazzi'yi küçük ölçekte al.
  - JSON açıklama ile ekran görüntüsünü karşılaştıran bir yorum tek ekranda JSON'un
    ucuz, uzun sayfada pahalı olduğunu bildirdi; yorumcu rakip araç yapıyor. [çelişkili]
    https://dev.to/albertvilella/your-ai-doesnt-think-in-pixels-why-raw-screenshot-to-code-workflows-waste-your-context-window-2nfe
- "Model ölçüm sorunlarını yakalar, yargı sorunlarını insan yakalar." Tek ekran
  görüntüsü hover, hareket, aşağıdaki içerik, uç durum veri, marka hissini göstermez.
  [satıcı, ama kendi sınırlarını da yazıyor]
  - https://www.digitalapplied.com/blog/screenshot-driven-ui-development-vision-models-2026
- Gerçek kaynaktan çıkar. Canlı bir site varsa gerçek HTML/CSS'i çıkarmak
  resimden çıkarmaktan güvenilirdir (MiroMiro; ürün satıyor, taraflı ama mantık
  doğru). [satıcı]
  - https://miromiro.app/blog/screenshot-to-code-what-actually-works
- Telif ve kopya riski [çıkarım]: başkasının uygulamasının ekran görüntüsünü
  "aynen kodla" demek tasarımı kopyalamaktır. aitmpl'deki `design-mirror` bunu
  amaçlıyor; kurucunun "kopyalama" kuralıyla çakışır, kullanma.
- Gizlilik [çıkarım]: Sende'nin sözü "veriler cihazdan çıkmaz". Gerçek müşteri
  mesajı içeren ekran görüntüsünü bulut yapay zekaya veya MCP'ye verme.
  Yalnız sahte veri (fixture) kullan.

Daha iyi çalışan yollar (kaynaklarla)
- Yapısal girdi: Figma MCP (gerçek değişken, bileşen, yerleşim) veya DESIGN.md.
- Kod-koda çeviri (SwiftUI -> Compose örneği).
- Parçalara bölme ve bileşen bileşen ilerleme.
- Sınırlı jeton kümesi: boşluk 4/8/12/16/24, köşe 8/14/20 gibi; model aralarında
  uydurmasın. [çıkarım]
- Ölçülebilir karşılaştırma: ekran görüntüsü farkı (Roborazzi `compare`) + kısa
  insan bakışı; yalnız modelin gözüne güvenme.

## 4. Döngü: Compose Preview + ekran görüntüsü testleri

### Resmi durum (Eylül 2026)
- "Compose Preview Screenshot Testing" bağımsız eklentisi (`com.android.compose.screenshot`)
  DEPRECATED; hâlâ deneysel/alpha. Yerine "AGP test suites" öneriliyor. [resmi]
  - https://developer.android.com/studio/preview/compose-screenshot-testing
- AGP test suites: AGP 9.5.0-alpha03+, motor 0.0.1-alpha16+, deneysel bayraklar.
  Sende AGP 8.11.1. Yani ŞİMDİ kullanılamaz. Karar: Roborazzi'de kal. [resmi + çıkarım]
  - https://developer.android.com/studio/preview/compose-screenshot-testing-with-testsuites

### Roborazzi (Sende zaten var, 1.32.2)
- Apache-2.0, ~1,1 bin yıldız. Görevler: `recordRoborazziDebug`,
  `compareRoborazziDebug`, `verifyRoborazziDebug`, `verifyAndRecordRoborazziDebug`.
  [kaynak kod / resmi README] https://github.com/takahirom/roborazzi
- Deneysel: `generateComposePreviewRobolectricTests` ile `@Preview` işlevlerinden
  otomatik test üretimi. Deneysel: `roborazzi-ai-gemini` / `roborazzi-ai-openai`
  ile "ekran şunu içeriyor mu" yapay zeka doğrulaması; yalnız görüntüler farklıysa
  çalışır; ağ gerektirir. Bizde gizlilik kuralı gereği yalnız CI'da, sahte veriyle
  düşünülebilir; şimdilik gerek yok. [çıkarım]
- Paparazzi, Robolectric'le birlikte çalışmıyor; etkileşim (tıklama) testine
  izin vermiyor; bu yüzden Roborazzi doğuyor. [blog; arama özetinden, sayfaları açmadım]
  - https://hub.telefonica.com/en/engineering/testing-screenshot-roborazzi-systems-design
  - https://blog.kinto-technologies.com/posts/2024-12-13-Introducing-Screenshot-Testing-in-UnlimitedApp/
- Yeni arkadaşlar (hepsi çok yeni, denemedim)
  - Mugshot: `@Preview`'in üstüne tek ek açıklama; layoutlib ile, Android çalışma
    zamanı olmadan. Paparazzi'den doğmuş. 12 oy. [topluluk, tek kaynak]
    https://reddit.com/r/androiddev/comments/1wi56w0/i_built_mugshot_a_screenshot_testing_library/
  - Spyglass (MIT, sürüm 0.1.0, "erken geliştirme"): `@Preview`'i IDE'siz PNG'ye çevirir,
    tasarım görseliyle karşılaştırıp fark bölgelerini ve ısı haritasını verir, MCP
    sunucusu var. Bugün yalnız Compose Multiplatform/masaüstü önizlemelerini
    destekliyor; yalnız-Android önizleme "yol haritasında". Sende kullanılamaz.
    [kaynak kod] https://github.com/zainkhalid91/spyglass
- Android CLI: `android studio render-compose-preview` çalışan bir Android Studio
  ister; `android screen capture` ve `android layout` (JSON: metin, content-desc,
  sınırlar) cihaz/emülatörde çalışır. Skill'i "PNG'yi önce GÖZLE incele" diyor.
  [resmi + kaynak kod]
  - https://developer.android.com/tools/agents/android-cli/commands
- Topluluk: "XML önizlemesi dünyanın en büyük yalancısı; birden çok durumu
  göstermez; ekran görüntüsü testine bak." (39 oy). "ViewModel'i önizlemeye sokma;
  stateless alt bileşenleri sahte veriyle önizle." [topluluk]
  - https://reddit.com/r/androiddev/comments/1ty3n9i/am_i_missing_something_or_is_preview_in_compose/
  - https://reddit.com/r/androiddev/comments/1s7dlfb/jetpack_compose_preview_is_hell/

### Sende için döngü kuralları [çıkarım]
- Bileşen stateless olsun; ViewModel'i kök sarmalar. Önizleme/test sahte veriyle.
- Her bileşen için durum matrisi: boş, kısa, çok uzun metin, çok satır, Türkçe
  İ/ı/ş/ğ, Arapça (RTL), okunmuş/okunmamış.
- Her ekran için ortam matrisi: açık/koyu, yazı ölçeği 1,0 / 1,3 / 2,0, 360 ve 411 dp,
  düğmeli ve hareketli gezinme (alt düğme gezinme çubuğuna binmesin).
- Bir değişiklik -> bir kayıt -> ajan PNG'yi okur -> fark listesi -> düzelt.
  En fazla 3 tur; sonra insana sor.
- Test, animasyon ortasında görüntü almasın (bekle/boşta olana kadar). [blog; arama
  özetinden, sayfayı açmadım]
  - https://medium.com/@prnksingh829/snapshot-testing-jetpack-compose-with-roborazzi-a-quickstart-guide-911358662d9c
- Ajan her değişiklikte tam Gradle derlemesi yapmasın; token ve süre israfı. [topluluk]

## 5. Tasarım jetonları (token)

Mevcut durum [kaynak kod, bu repo]
- `Brand.kt` kendi `BrandColors/BrandShapes/BrandMotion` sınıflarını kullanıyor
  (Material ColorScheme değil). `docs/marka/konsept-N-belirtecler.json` ayrı duruyor.
- İkisi birebir aynı değil: Brand.kt arka plan 0C0D10, vurgu F2B544; JSON (işlenmiş
  commit) 0B0C10 ve F0B240. Yani "tek kaynak" yok; elle kopyalanmış. [ölçtüm]
- Not: araştırma sürerken `konsept-1-belirtecler.json` başka bir kolda yeniden
  yazılıyor (git durumunda değişmiş görünüyor; arka plan 0A0E1B, yüzey 111628 oldu).
  Yani kayma sürüyor; bu, tek kaynak + üretim betiği önerisini güçlendiriyor.
  Aşağıdaki konsept 1 kontrast değerleri ilk okuduğum sürümden; yeni sürümde de AA
  geçiyor (metin/arka plan 16,8; ikincil/yüzey 7,8; vurgu/arka plan 10,2; tehlike/yüzey
  7,3). Diğer 4 konseptin değerleri değişmedi.

DESIGN.md hattı (Google, Apache-2.0, "alpha")
- Biçim: YAML jeton + metin gerekçe. `lint` kontrast ve kırık referans bulur,
  `diff` sürümleri karşılaştırır, `export` Tailwind veya DTCG (W3C jeton biçimi)
  üretir. Kotlin/Compose dışa aktarımı yok. [kaynak kod]
  - https://github.com/google-labs-code/design.md
- [çıkarım] JSON -> DESIGN.md -> `lint` -> küçük bir betikle `Brand.kt` üretmek
  mantıklı; ama DESIGN.md "alpha". Alternatif: JSON'u tek kaynak bırak, 40 satırlık
  bir betik hem Brand.kt'yi hem kontrast testini üretsin. DESIGN.md ancak Stitch
  veya Claude Design'a girdi olarak lazım.

Beş konseptin kontrastı [ölçtüm: WCAG 2.x formülü, JSON'daki hex]
- Hepsi AA (4,5:1) geçiyor. Birkaç değer
  - Konsept 1: metin/arka plan 17,0; ikincil metin/yüzey 7,1 (yüzeyYüksek'te 6,4);
    vurgu/arka plan 10,4; vurguÜstü/vurgu 9,8; tehlike/yüzey 6,7.
  - Konsept 2: vurgu/arka plan 5,1 (en zayıf halka); ikincil 5,5-6,7.
  - Konsept 3: vurgu/arka plan 14,8.
  - Konsept 4: vurgu/arka plan 4,7 (AA sınırında).
  - Konsept 5: tehlike/yüzey 5,6; başarı/yüzey 5,3.
- Çizgi/arka plan oranı 1,3-1,5; süs çizgisi için yeter, ama bir alanı tek başına
  tanımlayan sınır ise 3:1 gerekir (WCAG 1.4.11) [çıkarım].

Diğer yollar
- Material Theme Builder: Figma eklentisi + web; tek anahtar renkten tam şema,
  Compose/Views/DSP dışa aktarımı; "yeniden içe aktarma yok" uyarısı. Sende kendi
  Brand.kt'sini kullandığı için gerekmez. [resmi codelab + üçüncü taraf]
  - https://codelabs.developers.google.com/customizing-material-color
- Compose Styles API (`Style`, `Modifier.styleable`): DENEYSEL; compileSdk 37 ve
  foundation 1.12.0-alpha veya BOM 2026.04.01 ister; Material bileşenlerini
  desteklemez. Bekle. [kaynak kod: android/skills styles]
  - https://github.com/android/skills
- Relay (Google, Figma -> Compose): resmi belgeler duruyor; bir Perficient yazısı
  "30 Nisan 2025'te kapatıldı" diyor ama o yazı şu an blog dizinine yönleniyor,
  resmi duyuru bulunamadı. [tek kaynak, doğrulanamadı] Alfa, bakım belirsiz: kullanma.
  - https://developer.android.com/jetpack/compose/tooling/relay/install-relay
- Tokens Studio + Style Dictionary: Kotlin/Compose için resmi çıktı biçimi yok
  (arama sonucu); topluluk MCP'si `design-token-bridge-mcp` Figma değişkenlerinden
  Material 3 Kotlin teması üretiyor; olgunluğu bilinmiyor. [doğrulanamadı]
  - https://glama.ai/mcp/servers/kenneives/design-token-bridge-mcp

## 6. Animasyon: Lottie, Rive, Compose'un kendisi

Seçenekler ve kanıt
- Lottie (airbnb/lottie-android): Apache-2.0, 35,8 bin yıldız, sürüm 6.7.1; depodaki
  son commit 15 Şubat 2026. Tasarımcı notlarında: ifadeler (expressions) ve metin
  DESTEKLENMİYOR. Sistem "hareketi azalt" ayarına uyan seçenek sınıfı var
  (`SystemReducedMotionOption`). lottie-compose AAR'ı 133.598 bayt [ölçtüm];
  çekirdek `lottie` AAR'ı Maven Central'dan 429 aldığı için ölçülemedi.
  [kaynak kod + ölçtüm]
  - https://github.com/airbnb/lottie-android
- LottieFiles ücretsiz animasyon lisansı: ticari uygulamada kullanım serbest;
  atıf zorunlu değil ama teşvik ediliyor; değiştirirsen türev iş sayılıp aynı
  şartlarla dağıtılır; tek başına dosya olarak yeniden dağıtılamaz; bazı
  animasyonların ek kısıtı olabilir; premium ayrı lisans. [resmi yardım sayfası]
  - https://help.lottiefiles.com/animation-licensing-basics-
- Rive: Compose API'si "beta" ama belge "özellik tamam, üretime hazır, yeni Compose
  projeleri için önerilir" diyor; ayrı iş parçacığı (worker) kullanıyor, başlatma
  zorunlu. Belgedeki sürüm 11.9.2, Maven'deki en son 11.14.3 (14 Ekim 2026): belge
  geride. Çalışma zamanı lisansı resmi sayfada adıyla yok; üçüncü taraf dizin MIT
  diyor. [resmi + ölçtüm; lisans doğrulanamadı]
  - https://rive.app/docs/runtimes/android/android
- Lottie ile Rive karşılaştırması: Callstack ölçümü React Native'de, tek eski
  telefonda, tek animasyonla: Rive ~60 FPS, Lottie ~17 FPS (2023). Compose
  değil, tek örnek. Bellek toplamları kendi içinde tutmuyor. [blog, zayıf]
  - https://www.callstack.com/blog/lottie-vs-rive-optimizing-mobile-app-animation
  - HN'de "fark çoğu durumda küçük, asıl fark sıkıştırılmamış dosya boyu"
    diyenler ve "Rive pazarlaması performansıyla uyuşmuyor" diyenler var (arama
    özetinden; tartışmaları açmadım).
    https://news.ycombinator.com/item?id=37140191
    https://news.ycombinator.com/item?id=43577809
- Pil ölçümü: bulunamadı. [yok]

Compose'un kendi araçları
- Yay (spring) animasyonları ve `AnimatedVisibility`/öğe yer değişimi: 06 ve 04'te
  değerler var. Ek bağımlılık yok.
- `androidx.graphics:graphics-shapes` 1.1.0 (22 Ekim 2025, kararlı): `RoundedPolygon`
  ve `Morph`; şekil morf animasyonu `progress` 0..1 ile sürülür. Yalnız çokgen
  benzeri şekiller için; ikon A->B dönüşümü için `AnimatedVectorDrawable` öneriliyor.
  [resmi]
  - https://developer.android.com/develop/ui/compose/graphics/draw/shapes
  - https://developer.android.com/jetpack/androidx/releases/graphics

Yapay zeka ile animasyon üretimi
- Lottie/Rive dosyasını yapay zekanın güvenilir üretebildiğine dair kaynak
  BULUNAMADI. Bulunanlar: Remotion/Manim/CapCut gibi VİDEO araçları için skill'ler
  (motion-skills, ~50 skill; Compose değil). Codex'in Figma eklentisinde
  `figma-implement-motion` ve `figma-use-motion` adlı skill'ler var; içeriğini
  okumadım. [doğrulanamadı]
  - https://github.com/iart-ai/motion-skills
  - https://github.com/openai/plugins
- Sende için [çıkarım]: ilk sürümde 3 an yeter: satır açılma/kapanma (yay),
  yeni mesaj gelişi (öğe yerleşimi + vurgu noktası), "ekranı temizle" onayı.
  Hepsi Compose-yerel. Lottie'yi ancak tasarımcı hazır dosya verirse (ör. ilk
  açılış) ve yalnız o ekranda düşün.
- M3 Expressive tartışmalı: HN'de hem "çok seviyorum" hem "beceriksiz ve ham" hem
  "Fisher-Price gibi çocuksu" yorumları var; tarz ölçütü olarak güvenme,
  hareket FİZİĞİ için al. [topluluk]
  - https://news.ycombinator.com/item?id=49667036
  - https://news.ycombinator.com/item?id=46413510
  - https://news.ycombinator.com/item?id=50004971

## 7. İkon setleri

- Google artık `androidx.compose.material:material-icons-*` kütüphanesini ÖNERMİYOR:
  "Material Symbols yeni yol; bu kütüphaneye güncelleme yayımlamayı bıraktık; en son
  Material 3 sürümünden kaldırıldı." Gerekçe: derleme süresini ciddi artırıyor.
  Önerilen: Google Fonts'un "Android" sekmesinden Vector Drawable XML indirmek.
  [resmi, iki sayfa]
  - https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
  - https://developer.android.com/jetpack/androidx/releases/compose-material3
- [ölçtüm] material-icons-extended-android 1.7.8 AAR: 35.720.998 bayt (35,7 MB).
  Komut: `curl -I <aşağıdaki adres>` ve `Content-Length` satırı.
  - https://dl.google.com/dl/android/maven2/androidx/compose/material/material-icons-extended-android/1.7.8/material-icons-extended-android-1.7.8.aar
  R8 release'te kullanılmayanı atar, ama derleme süresi ve geliştirme ortamı
  yükü kalır. Resmî sayfa da "çok büyük, R8 kullan" diyor.
- Material 3 1.4.0 notu: bu sürüm artık `material-icons-core` bağımlılığını
  getirmiyor. Sende BOM 2024.09.03 + `Icons.Rounded.*` var; BOM'u yükseltirken
  ikonlar derleme hatası verebilir. Önce ikonları vektöre çevir. [resmi; notun
  devamını okuyamadım, çıkarım]
- Sende 13 ikon kullanıyor: Tune, Search, Lock, KeyboardArrowDown, IosShare,
  Favorite, DeleteForever, CleaningServices, BugReport, Add, Send, OpenInNew,
  Close. [kaynak kod, bu repo]
- Seçenekler
  - Material Symbols: Apache-2.0; değişken eksenler (ağırlık, doluluk, derece,
    optik boyut). [resmi] https://github.com/google/material-design-icons
  - Phosphor: MIT (core repo LICENSE okundu). Compose paketleri:
    `com.adamglin:phosphor-icon` (MIT diye listeleniyor), `composeicons` (MIT,
    LICENSE okundu). CortenaUI'nin Phosphor paketi GPLv3: kullanma.
    [kaynak kod + arama özeti]
    - https://github.com/phosphor-icons/core
    - https://github.com/composablehorizons/composeicons
  - Lucide: ISC (LICENSE okundu; MIT'e benzer, atıf notu ister). [kaynak kod]
    https://github.com/lucide-icons/lucide
- [çıkarım] Sende için: tek aile (çizgi, tek kalınlık), 12-15 ikon, SVG -> Vector
  Drawable (Android Studio "Import vector asset" veya Google Fonts Android sekmesi)
  `res/drawable`'a; `AppIcons` tek dosyada. Lisans metni "açık kaynak lisansları"
  ekranına. Uygulamanın ikon (launcher) çiziminden ayrı düşün.
- Yapay zeka uyarısı: ikon setini model "çizmesin"; set içinden seç. Uydurma
  ikon adı (`Icons.Rounded.Foo`) derleme hatasıdır; uydurma API'ler konusunda
  Eevis'in `Role.TextField` örneği gibi. [çıkarım]

## 8. Yazı tipi seçimi ve Türkçe

### Ölçtüm: 20 yazı tipinin glif haritası
- Yöntem: google/fonts deposundan (ofl/) değişken TTF'ler indirildi; Python
  fontTools ile `cmap`'te şu karakterler arandı: ğ Ğ ş Ş ı İ ö Ö ü Ü ç Ç â î û ₺ ’ … →.
- Sonuç
  - Hepsinde ğ Ğ ş Ş ı İ ö Ö ü Ü ç Ç â î û var. Yani "ğ ş ı İ eksik" riski bu
    20 yazı tipinde yok.
  - ₺ (U+20BA, Türk lirası) EKSİK: Figtree, Geist, Hanken Grotesk, Instrument Sans,
    Rubik, Sora, Urbanist, Outfit, JetBrains Mono.
  - ₺ VAR: Inter, Manrope, DM Sans, Space Grotesk, Bricolage Grotesque, Plus
    Jakarta Sans, Onest, Lexend, Nunito, Fraunces, Source Serif 4.
  - → eksik: Fraunces, Lexend, Nunito, Rubik, Sora. (Ok işareti çoğu arayüzde
    ikonla çizilir; önemsiz.)
- Konseptlerin yazıları [ölçtüm + JSON]
  - Konsept 1: Manrope + Inter: ikisi de tam. En temiz.
  - Konsept 2: Bricolage + DM Sans tam; mono JetBrains Mono ₺ yok.
  - Konsept 3: Space Grotesk + Inter tam; mono ₺ yok.
  - Konsept 4: Fraunces tam (₺ var); Instrument Sans ₺ YOK; mono ₺ yok.
  - Konsept 5: Sora ₺ YOK; DM Sans tam.
  - Etki: fiyat yazan her yerde (ör. sipariş mesajında "250 ₺") eksik glif sistem
    yazı tipinden yedeklenir; boşluk ve ağırlık uyumsuz görünür. Müşteri mesajı
    Sende'de kullanıcı içeriği: ₺ mutlaka çıkacak. Çözüm: ₺ olan yazı tipini
    seç veya yalnız ₺ için tam bir yedek yazı tipi tanımla. [çıkarım]
- Lisans: bu dosyaların hepsi SIL OFL 1.1 (name tablosu okundu). Telif
  dizelerinde "Reserved Font Name" beyanı yok (nameID 0 okundu). Uygulamayla
  birlikte lisans metni verilmeli. [ölçtüm + çıkarım]

### Boyut [ölçtüm]
- Komut: `pyftsubset` (fontTools) ile Latin + Latin-1 + Latin Ext-A + ₺ + temel
  noktalama; tüm GSUB/GPOS özellikleri korundu; değişken eksenler korundu.
  - Inter değişken: 876.576 -> 258.052 bayt (gzip-9 ile ~128 KB)
  - Manrope değişken: 164.700 -> 86.632 bayt (gzip-9 ile ~36 KB)
  - DM Sans değişken: 240.164 -> 206.872 bayt
- APK şimdi ~2,7 MB; Inter'i alt kümeleyip koymak ~0,13 MB (sıkıştırılmış) ekler;
  tamamını koymak ~0,46 MB. Alt küme almak anlamlı. [ölçtüm + çıkarım]
- Alt kümede Türkçe harfler ve ₺ yine doğrulandı (aynı betikle). [ölçtüm]

### Compose'ta yazı tipi
- `res/font` içine koy; değişken yazı tipi yalnız Android 8 (API 26) ve üstünde
  çalışır; alt sürümde statik yedek gerek. Sende minSdk 26 (app/build.gradle.kts
  satır 49 okundu). `FontVariation` ile ağırlık ayarlanır. Dosya adı küçük harf,
  özel karaktersiz olmalı. [resmi]
  - https://developer.android.com/develop/ui/compose/text/fonts
- İndirilebilir yazı tipi (Google Fonts sağlayıcısı) değişken yazı tipini
  desteklemiyor ve `com.google.android.gms` sağlayıcısına dayanıyor (ağ ve Google
  Play hizmetleri gerekir; belge bunu açıkça yazmıyor, çıkarım). Sideload
  sürümünde INTERNET izni yok. Bu yüzden: GÖMÜLÜ yazı tipi. [resmi + çıkarım]
- Yazı tipini "gözle" doğrula: Roborazzi görüntüsünde İ ı ş ğ ₺ pangram satırı.
  Sende dil ekran görüntüsü testi var (LocaleScreenshotTest). [çıkarım]

### Türkçe büyük-küçük harf hataları (kendi kodumuzda ölçüldü)
- Kotlin `uppercase()/lowercase()` "invariant locale" kullanır: `"i".uppercase()` -> "I",
  Türkçe için `uppercase(Locale("tr"))` gerekir. [resmi]
  - https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/uppercase.html
- [ölçtüm] Java 21 ile (Kotlin aynı JVM çağrısını kullanır)
  - "İNDİRİM".toLowerCase(Locale.ROOT) -> 10 karakter: i + U+0307 (birleşik nokta)
    ... "indirim" içermiyor; NFKC de düzeltmiyor.
  - "ilker".toUpperCase(ROOT) -> "ILKER"; Türkçe doğrusu "İLKER".
  - "IŞIK".toLowerCase(ROOT) -> "işik" (noktalı i); Türkçe doğrusu "ışık".
  - "İbrahim" ile "ibrahim" ROOT küçük harf + NFKC sonrası EŞİT DEĞİL.
- Sende etkilenen yerler [kaynak kod, bu repo; değiştirmedim]
  - `ui/common/Images.kt` satır 93-94: avatar baş harfi `uppercase()` ile alınıyor;
    "İlker" -> "I". (06 da aynısını buldu.)
  - `capture/Fingerprint.kt` satır 31-34: konuşma anahtarı `lowercase(Locale.ROOT)` +
    NFKC. Aynı kişi "İBRAHİM" ve "İbrahim" diye gelirse iki farklı konuşma olabilir.
  - `capture/PromotionalHeuristics.kt` satır 29: `text?.lowercase()` ile
    "canlı yayında" gibi kalıp aranıyor; BÜYÜK HARFLİ "CANLI YAYINDA" -> "canli
    yayinda" olur ve eşleşmez. Gerçek bildirimlerde ne sıklıkta olduğu bilinmiyor.
  - Çözüm yönü: arama ve anahtar için tek bir "katla" işlevi: ı->i, İ->i, sonra
    ROOT küçük harf; ekranda gösterilecek büyük harf için `Locale.current`. [çıkarım]
- Not: ASCII yazan Türk kullanıcı "isik" ile "Işık"ı bulamaz. Kanal seçicideki
  aramada diyakritik duyarsız arama bir kazanç olur. [çıkarım]

### Yapay zeka görsel modelleri ve Türkçe metin
- "Dotting Test" (Hugging Face Space): Türkçe noktalı/noktasız i'yi görsel
  modellerin doğru çizip çizmediğini ölçüyor. 8.400 üretim; insan etiketi yalnız
  1.055 satırda; geri kalan yapay zeka tahmini. Tahmini doğruluk: GPT Image 2
  %97,1; Nano Banana 2 %93,3; Nano Banana Pro %86,7; GPT Image 1.5 %83,8; Ideogram
  4.0 %75,2; FLUX.2 [flex] %65,7. Sayfa "kesin iddia için insan etiketli alt kümeyi
  kullan" diyor. Tarih yok. [topluluk, geçici]
  - https://fge-auto-dotting-benchmark.static.hf.space/index.html
- Çıkarım: reklam ve mağaza görselindeki YAZIYI görsel modele çizdirme; yazıyı gerçek
  yazı tipiyle sonradan koy. [çıkarım]

## 9. Figma -> Compose araçları

- Figma MCP (resmi). Uzak sunucu `https://mcp.figma.com/mcp` önerilen; Claude Code ve
  Codex destekli. Yerel masaüstü sunucusu Dev/Full koltuk ister. Figma'nın
  Claude Code eklentisi MCP ayarı + skill'leri birlikte kuruyor (arama özetinden). [resmi]
  - https://help.figma.com/hc/en-us/articles/32132100833559-Guide-to-the-Figma-MCP-server
  - https://help.figma.com/hc/en-us/articles/39888612464151-Claude-Code-and-Figma-Set-up-the-MCP-server
- Limitler (resmi sayfa bugün okundu): View/Collab koltuğunda "ayda en fazla 20" çağrı;
  Full/Dev koltukta Pro "günde 200, dakikada 10", Organization "200 / 15", Enterprise
  "600 / 20". Canvas'a yazan araçlar muaf. Eski yazılarda "ayda 6" geçiyor; limitler
  değişiyor, kayda geçmeden önce yeniden bak. [resmi]
  - https://developers.figma.com/docs/figma-mcp-server/rate-limits-access/
- Varsayılan çıktı React + Tailwind. `get_design_context` yapıyı verir; bunu
  "tasarımın açıklaması" say, kod değil; çerçeveyi ve bileşenleri istemde adlandır.
  Büyük çerçeve yavaşlatır; bir çerçeve bir bileşen. Katman adları anlamlı olmalı.
  [blog, tek kaynak] https://dev.to/juholee/figma-mcp-claude-code-give-it-your-real-layout-variables-and-components-497m
- Code Connect: Figma bileşenini koddaki gerçek bileşene bağlar. Faydası yazarın
  iddiası, ölçüm yok. [blog]
- Çoklu çerçeveli akışlar (onboarding) her çerçeve ayrı dönüştürülür; "sonraki
  küçük görsel düzeltme kodu elle düzenlemek"tir. [satıcı, Builder.io]
  - https://www.builder.io/blog/claude-code-figma-mcp-server
- Builder.io Visual Copilot: Figma'dan Jetpack Compose üretir (Mitosis derleyicisi +
  ince ayarlı model; Figma eklentisi + CLI beta). "Başlangıç noktası" der; bağımsız
  ölçüm yok. [satıcı] https://builder.io/blog/convert-figma-to-jetpack-compose
- Locofy, Anima: Compose çıktısı bulunamadı (React/Vue/HTML). Kombai: Compose
  desteği bulunamadı. [yok / doğrulanamadı]
- FigmaToCompose (topluluk eklentisi + Kotlin arka uç): README Compose rc-02 ile
  denendiğini yazıyor; eski. [kaynak yazısı, eski]
- Penpot MCP: açık kaynak alternatif (Bölüm 1).
- Sende için [çıkarım]: Figma gerekli değil. Tek ekran, tek geliştirici, jeton
  JSON'da. Figma ancak dışarıdan tasarımcı gelirse veya reklam şablonları için
  devreye girer; o zaman uzak MCP + Code Connect + "Compose'a çevir" istemi.

## 10. aitmpl.com incelemesi

### Nedir
- Daniel Ávila'nın açık kaynak "Claude Code Templates" kataloğu; kaynak kod MIT
  lisanslı. Site kendini "1.910+ bileşen" diye tanıtıyor; GitHub ~30,8 bin yıldız,
  1,34 milyon CLI indirmesi iddiası (site kendi sayısı). [resmi + kaynak kod]
  - https://aitmpl.com
  - https://github.com/davila7/claude-code-templates
  - https://aitmpl.com/llms.txt
- Türlere göre (llms.txt, 21 Ekim 2026): skill 891, agent 422, slash komutu 288, MCP
  107, ayar 77, hook 63, mod 44, loop 18. İndirdiğim arama dizini 1.935 kayıt
  (14 şablon ve 11 sandbox dahil). Sayılar birbirini tutmuyor (trending-data
  2.184 diyor); önemli değil. [ölçtüm]
  - https://www.aitmpl.com/search-index.json
- Makine okunur arayüz: `search-index.json`, `components/{tür}.json`,
  `component-content/{tür}/{kategori}/{ad}.json`, `openapi.json`.
- Bileşen sayfası adresi: `https://www.aitmpl.com/component/{tür}/{kategori}/{ad}`
  (sitemap'ten doğrulandı). Dosyalar GitHub'da:
  `cli-tool/components/{tür}/{kategori}/{ad}`.
- "Plugins" sekmesi: 34 üçüncü taraf pazar yeri (plugins.json). İçlerinde Everything
  Claude Code, Buildwithclaude, Playwright Skill gibi genel olanlar var. Android,
  Compose, Figma veya tasarım odaklı bir pazar yeri YOK. [kaynak kod]
  - https://github.com/davila7/claude-code-templates/blob/main/dashboard/public/plugins.json

### Kurulum ve güvenlik
- Kurulum: `npx claude-code-templates@latest --skill <kategori>/<ad>` (diğer
  bayraklar: `--agent`, `--command`, `--mcp`, `--hook`, `--setting`, `--mod`,
  `--loop`). Birden çok bayrak birleştirilebilir. [resmi]
  - https://docs.aitmpl.com/installation (arama sonucunda gördüm, açmadım)
- Telemetri: CLI varsayılan olarak anonim "bileşen indirildi" olayını Supabase'e
  yollar (bileşen adı, Node sürümü, platform, mimari, CLI sürümü, oturum kimliği).
  Kapatma: `CCT_NO_TRACKING=true` veya `CCT_NO_ANALYTICS=true`; `CI=true` iken kapalı.
  [kaynak kod: cli-tool/src/tracking-service.js]
- Hook, MCP ve mod yerel izinlerle KOD ÇALIŞTIRIR; llms.txt kendisi "yüklemeden önce
  içeriği kullanıcıya göster" diyor. Bileşenler topluluk katkısı; bazı dosyalarda
  `risk: safe` etiketi var ama kim doğruluyor belli değil. [resmi + çıkarım]
- [çıkarım] Öneri: CLI'yi çalıştırma. Bileşen dosyasını oku, MIT lisansını koru,
  gerekirse `.claude/skills/<ad>/` altına elle kopyala. Bir skill klasörü düz
  dosyadır.

### Arama: Android / Compose'a özel ne var
- "android|kotlin|compose|jetpack" için dizinin tamamı tarandı. Android'e özel
  yalnız iki kayıt: `android-cicd` (skill) ve `android-mcp` (MCP). Kotlin:
  `kotlin-specialist` (agent), `kotlin-coroutines-expert` (skill),
  `kotlin-mcp-expert` (MCP sunucusu YAZMAK için, alakasız). Compose adını taşıyan
  bileşen YOK. [ölçtüm]
- `mobile-design` skill'inin `platform-android.md` dosyasında Compose bölümü var
  (satır 587 ve 627). `ui-ux-pro-max` yığınları: html-tailwind, react, nextjs,
  nuxt, vue, svelte, shadcn, react-native, flutter, swiftui. Compose yok. [kaynak kod]

### Tek tek liste: bize yarayabilecekler
Her madde: ad, tür, bağlantı, ne yapar, kurulum, Sende'ye yarar mı.

1. frontend-design (skill, Anthropic'in, Apache-2.0)
   - https://www.aitmpl.com/component/skills/creative-design/frontend-design
   - Ne yapar: "ayırt edici, şablonsuz arayüz" süreci. Önce marka jetonu (4-6
     adlandırılmış hex, yazı tipi rolleri, yerleşim fikri, ilkeler), sonra planı
     "bunu her benzer sayfada üretir miydim" diye gözden geçir, sonra kod, sonra
     ekran görüntüsüyle kendini eleştir. Yapay zekanın yakınsadığı 5 kalıbı sayıyor
     (krem+kiremit; yakın-siyah+tek parlak vurgu; gazete düzeni; SaaS kart seti;
     şablon süsler).
   - Kurulum: `npx claude-code-templates@latest --skill creative-design/frontend-design`
     (veya dosyayı elle kopyala).
   - Sende'ye: KISMEN EVET. Web/CSS odaklı; ama süreç (plan -> gözden geçir -> inşa
     -> eleştir) ve "tek yerde cesur ol" ilkesi AGENTS.md'ye 15 satır olarak
     taşınabilir. Not: konsept 1 (yakın-siyah + tek vurgu) bu kalıplardan
     ikincisine denk (06 da aynısını söylüyor).
2. mobile-design (skill)
   - https://www.aitmpl.com/component/skills/creative-design/mobile-design
   - Ne yapar: 13 dosya (SKILL.md dahil), 6.078 satır: dokunma psikolojisi (Fitts, başparmak
     bölgesi), mobil performans, gezinme, yazı tipi, renk (OLED), `mobile-design-thinking`
     ("yapay zeka varsayılanlarını sorgula" listesi), `platform-android.md` (Material 3,
     Compose bölümü). `scripts/mobile_audit.py` yalnız React Native / Flutter kodunu
     denetliyor, Kotlin'i değil.
   - Kurulum: `... --skill creative-design/mobile-design`
   - Sende'ye: KISMEN. Yalnız `platform-android.md`, `touch-psychology.md`,
     `mobile-design-thinking.md` okunabilir. Hepsini kurma (bağlam maliyeti;
     "ZORUNLU: önce hepsini oku" diyor). Python denetimi Compose'a uymaz.
3. figma ve figma-implement-design (skill, OpenAI yazarlı)
   - https://www.aitmpl.com/component/skills/creative-design/figma
   - https://www.aitmpl.com/component/skills/creative-design/figma-implement-design
   - Ne yapar: Figma MCP'yi doğru sırayla kullandırır: `get_design_context` ->
     `get_screenshot` -> varlık indirme -> projenin jetonlarıyla çevir -> doğrula.
     "Çıktıyı (genelde React+Tailwind) tasarım olarak gör, kod olarak değil" kuralı.
   - Kurulum: `... --skill creative-design/figma` (Figma MCP ayrıca kurulur).
   - Sende'ye: Figma kullanılırsa EVET, şimdilik HAYIR.
4. figma-dev-mode (MCP)
   - https://www.aitmpl.com/component/mcps/devtools/figma-dev-mode
   - Ne yapar: yerel `http://127.0.0.1:3845/mcp` adresindeki Figma masaüstü Dev Mode
     sunucusu. Kurulum: `... --mcp devtools/figma-dev-mode`.
   - Sende'ye: Figma kullanılırsa uzak sunucu (`https://mcp.figma.com/mcp`) tercih
     edilmeli; yerel sunucu Dev/Full koltuk ister. [resmi]
5. android-cicd (skill, MIT, yazar ezescholz, Mayıs 2026)
   - https://www.aitmpl.com/component/skills/development/android-cicd
   - Ne yapar: `npx android-cicd` ile keystore üretir, GitHub Secrets'a yükler
     (`gh secret set`), çok aşamalı iş akışı kurar: main -> internal, `v*-alpha` ->
     alpha, `v*-beta` -> beta, `v*` -> production; her push'ta `versionCode`'u
     artırıp `[skip ci]` ile main'e commit eder. Gerekenler: Play Console'da en az bir
     elle yüklenmiş sürüm, Play Uygulama İmzalama, servis hesabı JSON'u.
   - Kurulum: `... --skill development/android-cicd`
   - Sende'ye: SONRA (yayın aşaması). Dikkat: bot'un main'e otomatik commit atması
     ve anahtar üretimi mevcut "gerçek anahtar repoya girmez" kuralıyla birlikte
     elle gözden geçirilmeli. Play sürümü ayrı flavor.
6. app-store-optimization (skill)
   - https://www.aitmpl.com/component/skills/business-marketing/app-store-optimization
   - Ne yapar: Python betikleri (anahtar kelime, metadata, rakip, yorum, başlatma
     kontrol listesi, A/B planı, yerelleştirme). Betiklerde ağ çağrısı bulamadım:
     girdiyi sen veriyorsun, canlı mağaza verisi çekmiyor. Kalite ölçümsüz.
   - Kurulum: `... --skill business-marketing/app-store-optimization`
   - Sende'ye: KISMEN, mağaza metni aşamasında (TR + EN karakter sınırı, anahtar
     kelime). Sonuçları körü körüne alma.
7. screenshot-feature-extractor (skill) + 5 ajan + screenshot-analyzer (komut)
   - https://www.aitmpl.com/component/skills/development/screenshot-feature-extractor
   - https://www.aitmpl.com/component/commands/utilities/screenshot-analyzer
   - Ne yapar: ürün ekran görüntülerinden özellik listesi ve görev listesi çıkaran
     çok ajanlı hat (ui-analyzer, interaction-analyzer, business-analyzer,
     synthesizer, reviewer). "NE yapılacağını yaz, NASIL yapılacağını yazma" ilkesi.
   - Sende'ye: rakip analizi için opsiyonel. Kurucunun "kopyalama" kuralına dikkat:
     özellik çıkar, tasarım kopyalama.
8. accessibility-tester (agent) ve accessibility-auditor (skill)
   - https://www.aitmpl.com/component/agents/accessibility/accessibility-tester
   - https://www.aitmpl.com/component/skills/creative-design/accessibility-auditor
   - Ne yapar: WCAG 2.2 AA denetimi; kaynak dosyayı değiştirmez; bulguları kriter
     numarası ve ciddiyetle raporlar; araçları web araçları (axe, Lighthouse, pa11y).
   - Sende'ye: KISMEN. Rapor biçimi ve WCAG 2.2 eşlemesi (ör. hedef boyutu) işe yarar;
     araçlar Android'de çalışmaz. Android için Bölüm 15.
9. android-mcp (MCP, CursorTouch/Android-MCP, MIT)
   - https://www.aitmpl.com/component/mcps/devtools/android-mcp
   - Ne yapar: ADB üzerinden cihaz/emülatörü kontrol (dokun, kaydır, ekran
     görüntüsü, arayüz ağacı). aitmpl yapılandırması: `uvx --python 3.13 android-mcp`.
   - Sende'ye: GEREKMEZ. Bizde `cmd notification post` uçtan uca testi var; Google'ın
     resmi `android` CLI'si aynı işi yapıyor (Bölüm 11). Kullanılacaksa gerçek
     müşteri verisi olmayan emülatörde.
10. playwright-skill ve playwright-e2e-builder (skill)
    - https://www.aitmpl.com/component/skills/utilities/playwright-skill
    - Ne yapar: tarayıcı otomasyonu. Android'de işe yaramaz; reklam ve mağaza görseli
      şablonlarını (HTML) PNG'ye çevirmek için kullanılabilir.
    - Sende'ye: reklam görseli aşamasında EVET (Bölüm 14).
11. generate-image (skill)
    - https://www.aitmpl.com/component/skills/scientific/generate-image
    - Ne yapar: OpenRouter üzerinden FLUX ve Gemini ile görsel üretir/düzenler;
      API anahtarı ve internet ister.
    - Sende'ye: yalnız mood board / arka plan dokusu. Yazı içeren görselde kullanma
      (Bölüm 8). Üretilen görselin telif durumu için Bölüm 14.
12. canvas-design (skill, Anthropic) ve remotion (skill)
    - https://www.aitmpl.com/component/skills/creative-design/canvas-design
    - https://www.aitmpl.com/component/skills/video/remotion
    - Ne yapar: PNG/PDF "sanat" üretimi; React ile programatik video.
    - Sende'ye: opsiyonel, reklam aşamasında. Remotion lisans koşulunu
      doğrulamadım. [doğrulanamadı]
13. ascii-ui-mockup-generator (agent) ve se-ux-ui-designer (agent)
    - https://www.aitmpl.com/component/agents/development-tools/ascii-ui-mockup-generator
    - https://www.aitmpl.com/component/agents/expert-advisors/se-ux-ui-designer
    - Ne yapar: ASCII yerleşim taslağı (birden çok seçenek); iş-yapılacak (JTBD) ve
      kullanıcı yolculuğu çıkarımı.
    - Sende'ye: opsiyonel, çok ucuz keşif adımı.
14. mobile-app-developer, mobile-developer, kotlin-specialist, ui-designer,
    ui-ux-designer (agent)
    - https://www.aitmpl.com/component/agents/development-team/mobile-app-developer
    - https://www.aitmpl.com/component/agents/programming-languages/kotlin-specialist
    - Ne yapar: genel rol istemleri ("kontrol listesi: uygulama < 50 MB, açılış < 2 sn
      ..."). "context-manager" adlı bir protokol bekliyor; bizde yok.
    - Sende'ye: HAYIR. Model zaten bunu biliyor; değer katmıyor.

### Bize yaramayanlar (kısa)
- design-to-code (coderio, Figma -> React), ui-design-system (jeton üretici CSS/SCSS),
  design-system-starter (web), tailwind-*, shadcn, web-design-reviewer (komut, web),
  setup-visual-testing (komut, Cypress/Playwright), mobile_audit.py (RN/Flutter),
  i18n-localization (genel liste; projede 4 dil hazır), webapp-testing,
  ui-ux-pro-max (web yığınları; renk veritabanında "SaaS = #2563EB mavi" gibi
  tam da yakınsama kaynağı olan varsayılanlar var).
- design-mirror (web-data/design-mirror): başka sitenin görsel stilini kopyalar. Kurucunun
  kopyalama kuralı ve marka riski nedeniyle KULLANMA.
- Hook ve ayarlardan bize uyan yok (tdd-gate gibi genel kapılar; statusline'lar).

## 11. Android'e özel hazır paketler (aitmpl dışı)

### Google'ın resmisi
- android/skills (Apache-2.0, 33 skill, son commit 21 Ekim 2026). Agent Skills açık
  standardında. Kurulum
  - Android CLI ile: `android skills add <ad> --project=.` veya `android skills add --all`
  - Claude Code: `/plugin marketplace add android/skills` ve
    `/plugin install android-skills@android-skills`
  - Codex: `codex plugin marketplace add android/skills` ve
    `codex plugin add android-skills@android-skills`
  - README: "Herkese açık katkı kabul edilmiyor."
  [kaynak kod] https://github.com/android/skills
- Bize yarayanlar
  - `system/edge-to-edge`: alt düğmelerin gezinme çubuğuna binmesi, IME ve sistem
    çubuğu okunabilirliği. Kurucunun I-62 isteği. Kapsam ve adımlar okundu. EVET.
  - `testing/testing-setup`: birim, arayüz, ekran görüntüsü ve uçtan uca test
    stratejisi. Reddit'te "işe yarayan tek Google skill'i" diye anıldı. EVET.
  - `devtools/android-cli`: `android` komutunu kurdurur/öğretir. EVET.
  - `play/play-policy-insights`: manifest ve kodu Play politikalarına karşı tarar,
    bildirimsiz veri toplama / eksik açıklama arar; kendi betikleri var. Yayın öncesi
    EVET. Ama `NotificationListener` / `BIND_NOTIFICATION_LISTENER_SERVICE` için
    kural veya tarayıcı YOK (dosyalarda arattım, bulunamadı); erişilebilirlik API'si
    için "belirgin açıklama + açık onay" kuralı var (bizim dinleyiciye birebir
    uymaz, ama aynı ruh). Bildirim erişimi için Play kuralı ayrıca araştırılmalı.
  - `play/play-billing-library-version-upgrade`: Play sürümünde Billing 8.0.0 var; sonra.
  - `jetpack-compose/jetpack-compose-performance`: kayma akıcılığı (I-44); sonra.
  - `security/android-permissions-security`, `performance/r8-analyzer`: sonra.
  - `jetpack-compose/adaptive`: katlanabilir/tablet; düşük öncelik.
  - `jetpack-compose/theming/styles`: DENEYSEL, kullanma (Bölüm 5).
- Bize yaramayanlar: wear, tv, xr, cars, camera, media3, device-ai, identity, navigation-3
  (tek ekran), glance (widget).
- Android CLI (Google, 2026): `create`, `run`, `install`, `emulator`, `screen
  capture`, `layout` (JSON arayüz ağacı), `input`, `docs`, `skills`, `studio`
  (çalışan Android Studio ister), `sdk`, `init`, `update`. Kurulum betiği
  `curl -fsSL https://dl.google.com/android/cli/latest/linux_x86_64/install.sh | bash`
  (önce betiği oku). Telemetri var; `--no-metrics` ile kapanır. Genel sayfa
  25 Eylül, komut sayfası 16 Ekim 2026 tarihli. [resmi]
  - https://developer.android.com/tools/agents/android-cli
  - https://developer.android.com/tools/agents/android-cli/commands

### Topluluk paketleri (hepsi açık kaynak; lisans LICENSE dosyasından okundu)
- chrisbanes/skills (Apache-2.0, 19 skill, son commit 21 Ekim 2026): compose-animations,
  compose-component-design, compose-state-and-effects, compose-ui-testing-patterns,
  compose-performance, gradle-run, implement-with-subagents, to-plan, deliver-spec ...
  Kurulum: `npx skills add chrisbanes/skills`; Claude plugin; Codex plugin; OpenCode.
  Vmobify yazısı da bu paketin bir skill'inden söz ediyor. Bize: compose-animations,
  compose-component-design, compose-ui-testing-patterns EVET (okunup uyarlanmalı).
  https://github.com/chrisbanes/skills
- aldefy/compose-skill (MIT, 1 skill + 24 rehber, son commit 8 Temmuz 2026):
  `design-to-compose.md`, `material3-motion.md`, `accessibility.md`,
  `theming-material3.md`, `deprecated-patterns.md`, `atomic-design.md`. Kurulum:
  `/plugin marketplace add aldefy/compose-skill` sonra `/plugin install compose-expert`;
  Codex için elle. Bize: design-to-compose ve deprecated-patterns EVET.
  https://github.com/aldefy/compose-skill
- Meet-Miyani/compose-skill (MIT, 7 skill, son commit 19 Ekim 2026): Compose +
  Compose Multiplatform, MVI/Koin/Navigation 3/Ktor/Room'a bağlı "ev tarzı". Kendi
  ölçümü var (Bölüm 2). Bize: SONRA; tek ekranlı uygulamada fazla mimari dayatabilir.
  https://github.com/Meet-Miyani/compose-skill
- krutikJain/android-agent-skills (MIT, 34 skill, son commit 25 Mart 2026): tasarım
  için `android-mobile-frontend-design` (oluştur/iyileştir/düzelt kipleri; uzun
  metin, çeviri, RTL, dar genişlik, klavye, sistem çubuğu stres listesi),
  `android-material3-design-system`, `android-compose-accessibility`,
  `android-testing-ui` (Roborazzi anıyor), `android-ci-cd-release-playstore`,
  `android-emulator-automation`. 7 aydır güncellenmemiş; eskime riski. Bize:
  frontend-design skill'inin stres listesini oku, kurma. https://github.com/krutikJain/android-agent-skills
- new-silvermoon/awesome-android-agent-skills (Apache-2.0, 17 SKILL.md, son commit
  27 Temmuz 2026): compose-ui, coil-compose, android-accessibility, compose-navigation,
  android-testing, android-emulator-skill, compose-performance-audit. Bize: kısmen.
  https://github.com/new-silvermoon/awesome-android-agent-skills
- skydoves/compose-performance-skills (Apache-2.0, Haziran 2026): yeniden
  kompozisyon, liste, kararlılık, baseline profile. Bize: kayma sorunu çıkarsa.
  https://github.com/skydoves/compose-performance-skills
- SangkuOh/build-androidos-apps (MIT, Haziran 2026): Codex eklentisi; Kotlin,
  Compose, Material 3, Gradle, adb ve profil iş akışları. Okumadım.
  https://github.com/SangkuOh/build-androidos-apps
- Adı geçen ama açmadığım: Drjacky/claude-android-ninja, gecko23/android-agent-skills,
  JosephSanjaya/skills, prasad-vennam/Awesome-Android-AI-Agent-Skills,
  revanthkumarJ/android-skills, mmiani/kotlin-kmp-claude-agent-skills. [doğrulanamadı]

### Cihaz/emülatör sürücüleri (MCP)
- mobile-next/mobile-mcp (Apache-2.0): iOS/Android cihaz, emülatör; erişilebilirlik
  anlık görüntüsü veya ekran görüntüsüne dayalı dokunma. Claude: `claude mcp add
  mobile-mcp -- npx -y @mobilenext/mobile-mcp@latest`; Codex: `codex mcp add
  mobile-mcp npx "@mobilenext/mobile-mcp@latest"`. PostHog ve Scarf ile anonim
  telemetri toplar; kapatma: `MOBILEMCP_DISABLE_TELEMETRY=1`. [kaynak kod]
  https://github.com/mobile-next/mobile-mcp
- CursorTouch/Android-MCP (MIT): yalnız ADB. Bir "android-mcp-beta" temiz-oda yeniden
  yazımı da var. [kaynak kod] https://github.com/CursorTouch/Android-MCP
- Karar [çıkarım]: Google'ın resmi `android` CLI'si + Bölüm 4'teki Roborazzi yeter.
  Fazladan MCP, fazladan telemetri ve fazladan saldırı yüzeyi.

## 12. Codex eşdeğerleri

- AGENTS.md: açık biçim (Linux Foundation altında Agentic AI Foundation yönetiyor);
  Codex, Cursor, Gemini CLI, Jules, Copilot coding agent, Zed vb. okuyor. Claude Code
  agents.md sayfasında anılmıyor; Claude Code `CLAUDE.md` okur. Aynı içeriği iki
  ada koymak (CLAUDE.md içinde "AGENTS.md'ye bak" satırı) yaygın. [resmi + çıkarım]
  - https://agents.md
- Codex AGENTS.md keşfi: `~/.codex` genel dosya, proje kökünden çalışma dizinine
  inerek her dizinde en fazla bir dosya, kökten yaprağa birleştirilir; yaprak sonra
  geldiği için önceliklidir; `AGENTS.override.md` ve `project_doc_fallback_filenames`
  var; toplam sınır 32 KiB (`project_doc_max_bytes`), aşılınca kesilir. [resmi]
  - https://learn.chatgpt.com/docs/agent-configuration/agents-md
  - Çıkarım: AGENTS.md'yi kısa tut (~200 satır), uzun şeyleri docs/ altına koy.
- Codex skill/eklenti: `openai/skills` deprecated görünüyor; yerine `openai/plugins`
  (62 eklenti örneği). Bize ilgili olanlar [kaynak kod]
  - `test-android-apps` (MIT): emülatörde adb ile başlatma, dokunma, arayüz ağacı,
    ekran görüntüsü, logcat; `android-emulator-qa` ve `android-performance`
    (Simpleperf, Perfetto, gfxinfo, bellek). Bize EVET (uçtan uca QA).
  - `figma` (Figma geliştirici koşulları): design-to-code, Code Connect,
    `figma-implement-motion`, `figma-use-motion` (adlarını gördüm, içeriği
    okumadım).
  - `product-design` (Tescilli lisans): görselden koda, URL'den koda, tasarım QA,
    denetim; web odaklı (web sitesi/uygulaması), Compose değil. Hayır.
  - `creative-production` (Tescilli): kampanya/mood board/reklam yönü. Reklam
    aşamasında bakılabilir.
  - https://github.com/openai/plugins
- Google'ın android/skills'i ve Stitch skills'i Codex kurulum komutunu da veriyor
  (Bölüm 1 ve 11). [resmi]
- Skill kayıt defterleri: `npx skills add <sahip>/<depo>` (skills.sh) ve açık standart
  agentskills.io. İkisini de ayrıca incelemedim. [doğrulanamadı]
- Cursor: Bölüm 2. PatrickJS/awesome-cursorrules'taki Android Compose kuralı CC0.

## 13. Hazır listeler, galeriler, örnek uygulamalar

- hesreallyhim/awesome-claude-code: ~261 kayıtlık düzenli liste (CSV). Lisans: CC BY-NC-ND
  4.0 (LICENSE okundu): TİCARİ KULLANIM VE TÜREV YOK. Listeyi kopyalama/uyarlama;
  bakıp kendi seçimini yap. "Design & UI/UX" bölümünden bize ilginç olanlar
  - atelier: `brand.json` ile jeton (CSS, Tailwind, Figma), favicon/uygulama ikon seti,
    WCAG denetimi. https://github.com/EthanY33/atelier
  - Logo Designer Skill: SVG logo kavramları, favicon okunurluğu, PNG dışa aktarım.
    https://github.com/neonwatty/logo-designer-skill
  - StyleSeed (~74 tasarım yargı kuralı), UI Craft (puanlanabilir eleştiri),
    Dev Browser, Snip (ajana görsel geri bildirim), Pinpoint, web-asset-generator.
    Hiçbirini denemedim.
  - naming: marka adı üretimi (Sende adı kesin, gerek yok).
  - pwa2play, claude-code-android: bizimle ilgisiz (PWA -> Play; telefonda Claude Code).
  - https://github.com/hesreallyhim/awesome-claude-code
- wasabeef/awesome-android-ui (MIT, son commit 5 Haziran 2026): Android UI/UX kütüphane
  listesi; çoğu View-çağı. Compose başlığı var (Landscapist, Orchestra ...). Bize:
  düşük değer; bağımlılık eklemeden önce lisans ve bakım durumuna bak.
  https://github.com/wasabeef/awesome-android-ui
- android/compose-samples (Apache-2.0, ~23,5 bin yıldız): Jetsnack (özel tasarım
  sistemi, animasyon), Jetcaster (kapaktan tema), Reply (uyarlanabilir), JetLagged
  (özel çizim), Jetchat, JetNews, Material Catalog (bileşen kataloğu). Crane/Owl/Jetsurvey/
  Rally 2024'te kaldırıldı. Bize: Jetsnack ve Material Catalog ilham/başvuru (kod
  kopyalama değil; Apache-2.0 olsa da yapıyı anlamak için).
  https://github.com/android/compose-samples
- Galeri siteleri (Mobbin, Dribbble, Behance, Refero): 06 numaralı dosyada ele alındı.
- "Blueprint Compose Preview" (Reddit, 33 oy, Haziran 2026) ve "Mugshot": yeni
  araçlar, denemedim. https://reddit.com/r/androiddev/comments/1u3shbb/introducing_blueprint_compose_preview/
  [doğrulanamadı]

## 14. Marka, ikon, mağaza ve reklam görseli: üretim ve riskler

### Play'in kuralları [resmi]
- Uygulama ikonu: 512x512, 32-bit PNG + alfa, en çok 1024 KB; "sıralama/fiyat/kategori"
  rozeti ve yanıltıcı yazı yok.
- Öne çıkan görsel: 1024x500, JPEG veya 24-bit PNG (alfa yok); cihaz görseli yok,
  üçüncü taraf marka izinsiz yok, fiyat/performans/sıralama ifadesi yok, uygulama
  ikonunu tekrar eden marka yok, saf beyaz/siyah/koyu gri zeminden kaçın.
- Ekran görüntüleri: her cihaz türü için en çok 8; yayın için en az 2; JPEG/PNG
  (alfa yok); kenar 320-3840 px, uzun kenar kısanın 2 katını geçmesin; 16:9 veya 9:16;
  gerçek uygulama içi deneyim; parmak/insan yok; slogan görselin en çok %20'si; "şimdi
  indir" gibi çağrı yok; bildirim çubuğunu temizle.
  - https://support.google.com/googleplay/android-developer/answer/9866151
- Uyarlanabilir ikon: katmanlar 108x108 dp; kenarlardan 18 dp maske/efekt payı; 66x66 dp
  güvenli bölge; logo 48-66 dp; tek renkli `monochrome` katmanı (Android 13+ temalı
  ikon). Android 16 QPR 2'den itibaren monochrome yoksa sistem otomatik temalıyor.
  [resmi] https://developer.android.com/develop/ui/views/launch/icon_design_adaptive
  - Sende `docs/marka/konsept-N-ikon-on.svg` ve `-mono.svg` dosyaları var. İçlerini
    incelemedim; arka plan / ön plan / monochrome katman ayrımı ve 66 dp güvenli
    bölge ayrıca kontrol edilmeli. [doğrulanamadı]

### Yapay zeka ile üretirken riskler
- Yazı: görsel modeller Türkçe noktalı/noktasız i'yi hâlâ yanlış çizebiliyor
  (Bölüm 8). Reklam ve mağaza görselinde yazıyı sonradan gerçek yazı tipiyle koy.
- Vektör ve kaynak: görsel modeller raster üretir; logo ve ikon el/SVG olarak
  üretilmeli. [çıkarım]
- Telif: ABD Telif Bürosu "insan yazarlık" arıyor; yalnız istemle üretilmiş görsel
  genelde tescil edilemiyor (ikincil kaynaklar). Ticari marka hukuku farklı işliyor
  (kullanımla). Türkiye için ayrı hukuk görüşü gerek; bu araştırmada
  Türkiye hukuku BULUNAMADI. [ikincil, doğrulanamadı]
  - https://www.clarkhill.com/news-events/news/u-s-copyright-office-ponders-copyright-protection-for-ai-generated-works-following-d-c-circuits-thaler-decision/
  - https://www.jukeboxprint.com/blog/what-small-businesses-own-ai-generated-art
- Marka adı taraması zaten `docs/MARKA_ADI_KONTROL.md`'de; logo benzerliği taraması
  ayrıca gerekir. [çıkarım]
- Üçüncü taraf logoları (WhatsApp vb.) reklamda: Meta kuralları WhatsApp'ı "en
  belirgin öğe" yapmayı, onay/ortaklık ima etmeyi, logoyu başka logo/adla
  birleştirmeyi yasaklıyor. Ekran görüntüsü için açık kural bulamadım. Uygulamanın
  kendi arayüzü ön planda, 3. taraf logolar küçük ve doğal kalmalı; emin değilsen
  Meta'dan yazılı onay iste. [resmi + çıkarım]
  - https://www.meta.com/brand/resources/whatsapp/whatsapp-brand/
- Kitle tepkisi: r/androiddev'de "bu garip ücretsiz ekran görüntüsü aracı
  gönderilerini bir şeye bağlayabilir miyiz" başlığı (23 oy) var; topluluk bu tür
  tanıtımlardan bıkmış. Araç seçerken reklam gönderilerine güvenme. [topluluk]
  - https://reddit.com/r/androiddev/comments/1vv74lp/can_we_do_something_about_these_weird_free/

## 15. Erişilebilirlik ve test araçları

- Compose'ta otomatik erişilebilirlik denetimi (Accessibility Test Framework):
  `ui-test-junit4-accessibility` bağımlılığı, `enableAccessibilityChecks()`,
  `tryPerformAccessibilityChecks()`. Kapsam: etiket, kontrast, dokunma hedefi boyutu,
  gezinme sırası. Compose 1.8.0 ve sonrası; yalnız `AndroidComposeTestRule`.
  API başvurusu API 34 şartı koyuyor (arama özetinden, sayfayı açmadım).
  "TalkBack ve Switch Access ile elle test yerini tutmaz." Sende Compose BOM
  2024.09.03 (Compose ~1.7): önce yükseltme gerek.
  Robolectric ile çalışıp çalışmadığı BULUNAMADI: emülatör işinde dene. [resmi]
  - https://developer.android.com/develop/ui/compose/accessibility/testing
- Dokunma hedefi: en az 48x48 dp; özel bileşende `minimumInteractiveComponentSize()`
  veya `sizeIn`. [resmi] https://support.google.com/accessibility/android/answer/7101858
- Android Studio'da "AI Actions > Fix all UI check issues" önizleme denetimi var;
  elle IDE gerekir. [resmi]
- Eevis deneyi (Bölüm 2): kaydırılabilirlik, büyük yazı, düğmeli gezinme, gereksiz
  `contentDescription`, uydurma `Role`. Test listesi buradan çıkar.
- Gözle denetim listesi [çıkarım]: yazı ölçeği 2,0'da kaydırılabilir mi ve düğmeler
  görünür mü; düğmeli gezinmede alt düğme örtülüyor mu; TalkBack'te kart "başlık +
  mesaj + Okundu düğmesi" diye mantıklı sırayla okunuyor mu; Okundu düğmesinin
  `contentDescription`'ı kişiyi söylüyor mu; hareketi azalt ayarında animasyon
  sadeleşiyor mu.

## Sende için önerilen tasarım üretim hattı

Genel ilke [çıkarım; Bölüm 1-15'teki kanıtlara dayanıyor]
- Kod, tasarım aracından değil JETONLARDAN ve EKRAN GÖRÜNTÜSÜ TESTLERİNDEN doğar.
- Yapay zeka üç iş yapar: keşif (referans PNG), çeviri (ajan Compose yazar),
  kontrol (ekran görüntüsü + otomatik test). Yön ve zevk kararı insanın.
- "İNSAN ONAYI" yazan her yerde hat durur; onaysız sonraki adıma geçilmez.
- Her adımın bir çıktı dosyası var; kurucu telefonda tek bakışta görebilsin.
- Gerçek müşteri mesajı hiçbir adımda bulut yapay zekaya girmez; yalnız sahte veri.

Adım 0. Hazırlık (bir kez)
- Araç: Claude Code ve/veya Codex.
- Yap: kökte kısa bir `AGENTS.md` (Codex 32 KiB'ta keser; ~200 satır hedef);
  `CLAUDE.md` içinde "AGENTS.md'ye bak" satırı.
- İçerik: yığın ve Gradle görevleri (hangisi ne zaman; her değişiklikte tam
  derleme YOK), `Brand.kt` tek kaynak ve ham hex yasağı, sahte veri kuralı,
  Türkçe harf kuralı (`uppercase()/lowercase()` kullanma), 06'daki slop kaçınma
  listesi, frontend-design'ın 15 satırlık süreci (Bölüm 10).
- Skill (Android CLI ile): `edge-to-edge`, `testing-setup`, `android-cli`; yayın
  öncesi `play-policy-insights`. Telemetri kapalı (`--no-metrics`).
- İNSAN ONAYI: kurucu AGENTS.md'yi ve kurulan skill listesini okur.

Adım 1. Marka yönü (insan karar verir)
- Girdi: 5 konsept + 06'daki slop karşılaştırması + Bölüm 8'deki ₺ ölçümü.
- Araç: yapay zeka yalnız "bu konsept neden bu ürüne özgü" ve "itiraz" metni yazar.
  İsteğe bağlı: kâğıda tek sayfa gelen kutusu çizimi (HN deneyimi).
- İNSAN ONAYI: tek konsept + tek cümle gerekçe.

Adım 2. Jetonları dondur
- Araç: `docs/marka/konsept-N-belirtecler.json` -> (isteğe bağlı) DESIGN.md ->
  `npx @google/design.md lint` (kontrast) -> küçük betik -> `Brand.kt` ve `res/font`.
- Yazı tipi: ₺ ve Türkçe harf kontrolü (Bölüm 8 betiği), alt kümeleme
  (`pyftsubset`), OFL lisans metni, `res/font`'a gömme.
- Otomatik: `Brand.kt` değerleri JSON ile aynı mı (birim test), kontrast testi (AA),
  İ ı ş ğ ₺ pangram ekran görüntüsü.
- İNSAN ONAYI: kurucu renk ve yazı örneğini telefonda görüp onaylar.

Adım 3. Referans ekranlar (isteğe bağlı keşif)
- Araç: Stitch (bedava) veya Claude Design. Girdi: DESIGN.md + fixture'dan gerçek
  Türkçe örnek mesajlar. Üç ekran: çok mesajlı grup, boş durum, izin kartı.
- Çıktı: yalnız PNG. HTML/React kodunu ALMA.
- Kural: ürüne özgü fikir yoksa ya da slop listesine uyuyorsa at.
- İNSAN ONAYI: kurucu bir referans seçer ya da "hiçbiri" der.

Adım 4. Compose'a çeviri (ajan)
- Araç: Claude Code / Codex; AGENTS.md; Bölüm 11'den seçili skill'ler (aldefy
  design-to-compose, chrisbanes compose-component-design ve compose-animations;
  önce oku, özetini AGENTS.md'ye al).
- Girdi: seçilen PNG'nin bölüm bölüm kırpıntıları (uzun kenar en çok 1568 px),
  jetonlar, mevcut fixture'lar.
- Biçim: bir bileşen = bir tur; stateless; önce `@Preview` + fixture durumları;
  renk/köşe/hareket yalnız `Tek.colors/shapes/motion`'dan.

Adım 5. Görsel doğrulama döngüsü (otomatik, bileşen başına en çok 3 tur)
- Araç: Roborazzi `record` -> ajan PNG'yi referansla yan yana okur -> fark listesi ->
  düzelt -> tekrar.
- Matris: açık/koyu; yazı ölçeği 1,0 / 1,3 / 2,0; 360 ve 411 dp; tr / ar (RTL);
  düğmeli ve hareketli gezinme.
- Sistem düzeyi kontrol: emülatörde `android screen capture` ve `android layout`
  (sahte veriyle).
- Çıkış: 3 turda bitmediyse insana sor, döngüyü uzatma.

Adım 6. Otomatik kapılar (her PR, CI)
- Roborazzi `verify`; kontrast testi; erişilebilirlik denetimi (Compose 1.8+ gerek;
  emülatör işinde); İ/ı birim testleri; APK boyut bütçesi (değeri kurucu belirler);
  INTERNET izni denetimi (zaten var).

Adım 7. Gerçek cihaz (insan)
- Kurucu önizleme APK'sını kurar; 10 dakika gerçek kullanım; büyük yazı; TalkBack
  2 dakika; düğmeli gezinme; kaydırma akıcılığı (I-44); ISTEKLER.md listesi.
- Kanıt: `adb shell screenrecord` videosu.
- İNSAN ONAYI: "bu ürün gibi hissettiriyor" evet/hayır + sorun listesi.

Adım 8. Animasyon
- Araç: Compose yayı + graphics-shapes; ajan yazar. Lottie/Rive yok (ilk sürüm).
- Üç an: satır açılma/kapanma, yeni mesaj gelişi, "ekranı temizle" onayı.
- Test: hareketi azalt açıkken sadeleşiyor mu.
- İNSAN ONAYI: kurucu ekran videosunda üç anı izler.

Adım 9. Marka varlıkları ve mağaza
- İkon: elle/SVG (`konsept-N-ikon-*.svg`) -> uyarlanabilir ikon (108 dp, 66 dp güvenli
  bölge, monochrome) -> 512 PNG betikle.
- Ekran görüntüleri: Roborazzi veya emülatörden GERÇEK arayüz, gerçek Türkçe metin,
  sahte müşteri adları; cihaz çerçevesi yok; slogan en çok %20; çağrı metni yok.
- HTML/SVG şablon + Playwright -> PNG (gerçek yazı tipiyle). 1024x500 öne çıkan görsel
  aynı şablon sisteminden.
- Görsel model (varsa) yalnız arka plan dokusu için; yazı ve logo için DEĞİL.
- İNSAN ONAYI: kurucu Play kuralları kontrol listesiyle tüm varlıkları onaylar.

Adım 10. Reklam görselleri (Meta)
- Aynı şablon sistemi ve Playwright. Metni kurucu yazar veya onaylar.
- Gizlilik iddiaları kurulu manifestten ölçülerek yazılır (mevcut kural).
- WhatsApp vb. logolar: Bölüm 14'teki kurallar. Meta'nın güncel boyut/yazı oranı
  kuralları bu araştırmada ARAŞTIRILMADI. [doğrulanamadı]
- İNSAN ONAYI: metin, iddia ve logo kullanımı; marka tescil/benzerlik taraması.

Adım 11. Yayın öncesi
- Araç: `play-policy-insights` skill; isteğe bağlı `android-cicd`; iç test kanalı.
- İNSAN ONAYI: Play Console işlemleri (zaten yalnız kullanıcı yapabilir).

Hattın özeti (kim ne yapıyor)
- Kurucu: adım 1, 2 (onay), 3 (seçim), 7, 8 (onay), 9, 10, 11.
- Ajan: adım 0, 2 (betik), 4, 5, 6, 8 (kod), 9-10 (şablon).
- Otomatik: adım 5-6 (CI).

## Sende için çıkarımlar

### Ne yapalım (ve neden)
1. AI tasarım aracından yalnız referans PNG al; kod alma. Neden: Stitch, Figma Make,
   v0, Claude Design Compose vermiyor; çıktı HTML/React. Görselden koda tuzakları
   Bölüm 3'te.
2. Jetonları tek kaynakta tut ve kodu oradan üret/kontrol et. Neden: Brand.kt ile
   `konsept-1-belirtecler.json` şimdiden ayrışmış (0C0D10 / 0B0C10, F2B544 / F0B240;
   JSON başka bir kolda yeniden yazılıyor).
   Üretim betiği + kontrast testi bunu kapatır.
3. Roborazzi'de kal; ekran görüntüsünü ajan döngüsünün gözü yap. Neden: Reddit'te
   en çok oy alan görüş "model ekranı göremez, ona geri bildirim ver"; resmî eklenti
   deprecated, AGP test suites alpha ve AGP 9.5 ister (Sende 8.11.1).
4. Bileşenleri stateless yaz, fixture durum matrisiyle önizle. Neden: topluluk
   "ViewModel'i önizlemeye sokma" diyor; matris (boş, uzun, Türkçe, RTL, 2,0 yazı)
   ajanın göremediği hataları görünür yapar.
5. Android'in resmi `android` CLI'sini ve `android/skills`'ten dört skill'i (edge-to-edge,
   testing-setup, android-cli, play-policy-insights) kur. Neden: LLM'in zayıf
   kaldığı işleri kapsıyorlar; edge-to-edge kurucunun I-62 isteği; hepsi Apache-2.0.
   Telemetriyi kapat.
6. Yazı tipini seçmeden önce ₺ ve Türkçe harf kontrolü yap, alt kümele, `res/font`'a
   göm. Neden: ₺ 20 dosyadan 9'unda yok (konsept 4 ve 5 etkileniyor); Inter alt
   kümesi 876 KB'tan 258 KB'a iniyor; indirilebilir yazı tipi Play hizmetleri ve ağ
   ister, sideload sürümde INTERNET izni yok.
7. `material-icons-extended`'ı bırak; 13 ikonu Vector Drawable olarak al, tek aile.
   Neden: Google önermiyor, AAR 35,7 MB, Material 3 1.4.0'dan kaldırıldı; BOM yükseltmeyi
   kolaylaştırır.
8. Animasyonu Compose yayıyla yap; Lottie/Rive'ı ilk sürümde alma. Neden: APK 2,7 MB,
   Lottie'de metin/ifade desteği yok ve lisans takibi ister, Rive Compose API'si beta;
   üç an Compose ile yeter.
9. Türkçe büyük-küçük harf için tek bir "katla" işlevi yaz (ı->i, İ->i sonra ROOT);
   ekranda büyük harf gerekiyorsa `Locale.current`. Neden: `Fingerprint`,
   `PromotionalHeuristics` ve `Images.kt` bugün "İNDİRİM", "İlker" gibi yazıları
   yanlış işleyebiliyor (ölçtüm, Bölüm 8). Bu koda bu kolda dokunmadım; ilgili
   kola/oturuma devret.
10. Erişilebilirliği "istemle" değil testle iste: otomatik denetim (Compose 1.8+),
    yazı ölçeği 2,0 ekran görüntüleri, elle TalkBack. Neden: Eevis deneyinde dört
    araç da aynı sınıf hataları yaptı; akademik tezde açık istem bazen daha çok hata
    üretmiş (tek kaynak).
11. AGENTS.md'ye "her değişiklikte tam Gradle derlemesi yapma" yaz. Neden: ajanın
    derleme/test takıntısı token israfı (Reddit).
12. Frontend-design'ın plan -> gözden geçir -> inşa -> eleştir sürecini AGENTS.md'ye
    15 satır olarak al. Neden: Anthropic'in kendi kılavuzu; yapay zekanın yakınsadığı
    kalıpları adıyla sayıyor ve konsept 1 bunlardan birine denk (06 ile aynı uyarı).
13. Reklam ve mağaza görselini şablon + gerçek yazı tipiyle üret; görsel modeli
    yalnız arka plan için kullan. Neden: modeller Türkçe i/ı'yı hâlâ yanlış çizebiliyor
    (GPT Image 2 bile tahminen %97, diğerleri daha düşük; insan etiketi az).
14. Android CLI ve mobile-mcp gibi araçlarda telemetriyi kapat; aitmpl CLI'yi
    çalıştırma, dosyaları oku ve elle kopyala. Neden: Sende'nin vaadi gizlilik;
    geliştirme araçları da ağa veri yollamasın.
15. Yalnız sahte veriyle çalış; müşteri mesajı içeren ekran görüntüsünü bulut
    yapay zekaya verme. Neden: ürünün sözü "veri cihazdan çıkmaz".

### Ne yapmayalım (ve neden)
1. Stitch/Figma Make/v0 çıktısını Compose'a çevirtme. Neden: tuzaklar (Bölüm 3),
   kod yapısı yok, jeton yok.
2. aitmpl'den `ui-ux-pro-max`, `design-mirror`, `mobile_audit.py`, web odaklı
   denetçileri kurma. Neden: Compose yok; renk veritabanı yakınsama kaynağı;
   design-mirror kopyalama kuralını çiğner.
3. Compose Styles API'si, Relay, AGP test suites, Material Theme Builder'ı şimdi
   hatta alma. Neden: deneysel/alpha/bakımı belirsiz veya Sende zaten kendi
   Brand.kt'sini kullanıyor.
4. Skill sayısını artırma. Neden: Reddit'te "en iyi skill kendi projen için
   yazdığındır"; her skill bağlam yer; ajan tam derleme takıntısına girer.
5. awesome-claude-code listesini kopyalama/uyarlama. Neden: CC BY-NC-ND 4.0
   (ticari kullanım ve türev yasak). Bakıp kendi seçimini yap.
6. GPL'li Phosphor Compose paketini (CortenaUI) ve lisansı belirsiz ikon
   paketlerini alma. Neden: copyleft; Sende ticari olarak satılacak.
7. "Erişilebilir yap, premium yap" gibi genel istemlere güvenme. Neden: ölçülebilir
   ölçüt yok; model başarıyı kendi ilan eder. Ölçüt ver: kontrast, hedef boyutu,
   yazı ölçeği, ekran görüntüsü farkı.
8. Tüm ekranı tek seferde resimden kodlatma. Neden: eksik/bozuk/yanlış yerleşim
   (DCGen bulgusu); bileşen bileşen ilerle.
9. Referans resmi aynen kopyalatma, başka uygulamanın ekranını "bire bir" isteme.
   Neden: telif ve kopya riski; kurucunun kuralı.
10. AI görsel çıktısını logo olarak olduğu gibi kullanma. Neden: raster, yazı hatası,
    telif/tescil belirsizliği (ikincil kaynak, hukuk görüşü gerek).
11. Rive/Lottie'yi "performans daha iyi" diye seçme. Neden: tek eski telefon ve
    React Native ölçümü var (Compose değil); pil ölçümü bulunamadı.

### Kurucuya sorulacaklar (karar gerektiren)
- Hangi konsept? (Türkçe ₺ ve kontrast açısından konsept 1 en temiz; konsept 4 ve 5
  için ₺ yedek yazı tipi gerekir; slop riski için 06'ya bak.)
- Referans üretimi için Stitch (bedava) mı, Claude Design (abonelik kullanımı,
  token yer) mı, yoksa kâğıt çizim mi?
- Figma hesabı/tasarımcı gelecek mi? Yoksa Figma hattı kurulmayacak.
- APK boyut bütçesi ne olsun? (Şu an ~2,7 MB; yazı tipi ~0,1-0,2 MB ekler.)
- Reklam görsellerinde üçüncü taraf uygulama logoları gösterilecek mi?

### Başka kollara devredilecek bulgular (bu kolda kod değiştirilmedi)
- `Fingerprint.kt`, `PromotionalHeuristics.kt`, `Images.kt`: Türkçe harf katlama.
- `Brand.kt` ile `konsept-1-belirtecler.json` eşitleme.
- `app/build.gradle.kts`: `material-icons-extended` bağımlılığı ve BOM 2024.09.03
  yükseltmesi sırası (önce ikonlar).
- Play'de bildirim erişimi için politika/açıklama gereksinimi: bu kolda BULUNAMADI,
  ayrıca araştırılmalı.

## Denenen ve çalışmayan yollar

- Reddit (arama ve okuma)
  - WebSearch `allowed_domains=reddit.com`: API 400, "bu alan adları kullanıcı
    aracımızca erişilebilir değil".
  - `curl www.reddit.com/.../search.json` ve `old.reddit.com`: 403 "Blocked".
  - WebFetch reddit.com: "unable to fetch". pullpush.io: 429 hız sınırı.
  - ÇÖZÜM: arctic-shift arşiv API'si. `title=` ile başlık araması ve `link_id=` ile
    yorumlar çalıştı. Sınırlar: `body=` parametresi yok (400); sık "Timeout, slow
    down" (422) ve 429; bu yüzden istekler 6-8 sn arayla yapıldı. r/ClaudeCode,
    r/ClaudeAI, r/FigmaDesign, r/UI_Design aramaları boş/zaman aşımı döndü;
    r/androiddev'de "stitch", "figma", "lottie" başlık aramaları boş. Sonuç:
    Reddit verisi yalnız r/androiddev'den ve sınırlı.
- X.com: WebFetch arama sayfası HTTP 402; nitter.net "offline". Başka yol aranmadı
  (2 deneme). X'ten doğrudan bulgu YOK.
- YouTube: WebFetch Google "sorry" doğrulamasına yönlendirdi; curl 302. oEmbed
  çalıştı (başlık ve kanal) ama açıklama alınamadı. Video içerikleri okunamadı.
- Stack Overflow: `curl stackoverflow.com` 302. Stack Exchange API çalıştı ama uzun
  sorgularda boş döndü; kısa sorgular alakasız (2020-2023 Lottie/Compose) sonuç
  verdi. Bu konuda işe yarar bulgu çıkmadı.
- Stitch resmî sayfaları (docs, MCP kurulumu) ve Material Theme Builder blog yazısı:
  WebFetch yalnız başlık döndürdü (sayfa JS ile yükleniyor). Stitch'in sınırları ve
  çıktı biçimleri yalnız satıcı/rakip yazılarından alındı; resmî doğrulama YOK.
- Lottie resmî Compose belgesi (airbnb.io -> lottie.airbnb.tech): içerik yok (JS).
  ÇÖZÜM: depoyu klonlayıp CHANGELOG, DESIGNER_NOTES ve kaynak okundu.
- Maven Central: `lottie-6.7.1.aar` HEAD isteği 429. Çekirdek Lottie boyutu ÖLÇÜLEMEDİ.
- aitmpl.com/plugins: WebFetch boş (istemci taraflı). ÇÖZÜM: GitHub deposundaki
  `dashboard/public/plugins.json` ve `search-index.json`.
- developer.android.com icons sayfası: WebFetch ilk 100 bin karakterde ifadeyi göstermedi
  (sayfa 362 bin karakter). ÇÖZÜM: curl + metin ayıklama; ifade bulundu.
- Perficient "Figma to Compose yöntemlerini karşılaştırma" makalesi: URL blog
  dizinine yönleniyor, makale yok. Relay "kapatıldı" iddiası doğrulanamadı.
- UFC tez deposu: WebFetch 503; yalnız arama özeti.
- Claude görüntü belgesi: eski adres 301 ile taşınmıştı; yeni adres çalıştı.
- fontTools `pyftsubset --flavor=woff2`: brotli modülü yok. ÇÖZÜM: TTF alt küme
  ölçüldü (Android zaten TTF kullanır).
- GitHub: yalnız anonim `git clone` ve herkese açık sayfalar. `api.github.com` ve
  oturumun GitHub araçları başka depolar için KULLANILMADI (kural).
- aitmpl CLI (`npx claude-code-templates`) ÇALIŞTIRILMADI (telemetri); bileşenler
  klon ve JSON dizinden okundu.
- Stitch'ten Compose'a gerçek bir üretim deneyimi yazısı aradım (YouTube başlığı,
  HN, Reddit, web): BULUNAMADI. Yalnız Stitch -> React/HTML ve "Stitch + Cursor ile
  Compose" başlıklı bir video var (içeriği okunamadı).
- Uizard "Autodesigner": hakkında bilgi BULUNAMADI.
- Playwright/Chromium kullanılmadı (gerek kalmadı).

## Doğrulanamayanlar ve güvenilirlik notları

- Stitch'in Compose/Flutter/SwiftUI çıktısı verdiği: tek arama özetinde var, resmî
  sayfada doğrulanamadı. Stitch'in kredi sınırları yalnız bir rakipten (Banani).
- Magic Patterns'in fiyat modeli değişikliği: arama özeti, sayfa açılmadı.
- Relay'in kapatıldığı: tek ikincil kaynak; makale açılmıyor.
- Remotion lisans koşulu: bakılmadı.
- `design-token-bridge-mcp`, Penpot lisansı, Kombai, Locofy/Anima'nın Compose çıktısı:
  doğrulanamadı / bulunamadı.
- Figma MCP monthly limit: resmi sayfa bugün "View/Collab ayda 20" diyor; arama
  özetleri ve bir blog "6" diyor. Limitler değişiyor.
- ATF denetiminin Robolectric'te çalışıp çalışmadığı: bulunamadı.
- UFC tezinin bulguları: tek kaynak, yalnız özet.
- Meet-Miyani'nin ölçümleri: yazarın kendisi; bağımsız doğrulama yok.
- "Dotting Test": çoğu yapay zeka tahmini; insan etiketi 1.055 satır; tarih yok.
- r/androiddev araç derlemesi yazısı: reklam şüphesi var; yalnız yorumlardaki kişisel
  deneyimleri saydım.
- HN yorumları: tek kişilik deneyimler; bazıları rakip girişimcilerden (belirtildi).
- Callstack Lottie/Rive ölçümü: 2023, React Native, tek cihaz.
- Telif/tescil: ikincil kaynaklar, ABD merkezli; Türkiye hukuku araştırılmadı.
- Meta reklam boyutları ve WhatsApp logosunun ekran görüntüsünde kullanımı: açık kural
  bulunamadı.
- Material 3 Expressive'in resmî Compose belgeleri bu kolda okunmadı (06 okudu).
- İndirilen sürüm numaraları (Lottie 6.7.1, Rive 11.14.3, graphics-shapes 1.1.0,
  Material3 1.4.0 / 1.5.0-beta01) 21 Ekim 2026 itibarıyla; hızla eskir.
- Bildirim erişimi (NotificationListener) için Play politikası: bu kolun konusu
  değildi ve bulunamadı.

## Ölçüm notları (komutlar ve sonuçlar)

- Yazı tipi glif kontrolü: `google/fonts` GitHub deposundan `ofl/<ad>/<Ad>[wght].ttf`
  indirildi; Python fontTools `getBestCmap()` ile U+011F, U+011E, U+015F, U+015E,
  U+0131, U+0130, U+00F6, U+00D6, U+00FC, U+00DC, U+00E7, U+00C7, U+00E2, U+00EE,
  U+00FB, U+20BA, U+2019, U+2026, U+2192, U+2713 arandı. 20 dosya.
- Alt küme: `python -m fontTools.subset X.ttf --unicodes=U+0020-007E,U+00A0-00FF,
  U+0100-017F,U+0218-021B,U+02C6,U+02DA,U+02DC,U+2013-2014,U+2018-201E,U+2022,
  U+2026,U+20AC,U+20BA,U+2122,U+2190-2193,U+2212 --layout-features='*'`.
  Boyutlar: Inter 876.576 -> 258.052; Manrope 164.700 -> 86.632; DM Sans 240.164 ->
  206.872 bayt. `gzip -9`: Inter ~127.8 KB, Manrope ~35.8 KB.
- AAR boyutu: `curl -I` `Content-Length`: material-icons-extended-android-1.7.8.aar =
  35.720.998; lottie-compose-6.7.1.aar = 133.598.
- Türkçe harf davranışı: Java 21 (`jshell` değil, tek dosyalı program):
  `"İNDİRİM".toLowerCase(Locale.ROOT)` -> U+0069 U+0307 ... (10 karakter),
  `contains("indirim")` = false, NFKC sonrası da false;
  `"ilker".toUpperCase(Locale.ROOT)` = "ILKER"; `"IŞIK".toLowerCase(Locale.ROOT)` =
  "işik"; "İbrahim" ve "ibrahim" ROOT+NFKC sonrası eşit değil. Kotlin'in
  `lowercase()/uppercase()` işlevleri Locale.ROOT kullanır (kotlinlang belgesi).
- Kontrast: WCAG 2.x göreli parlaklık formülü, `konsept-N-belirtecler.json`
  değerleriyle.
- aitmpl dizini: `search-index.json` indirildi (1.935 kayıt); anahtar sözcük süzmesi
  (android, kotlin, compose, jetpack, mobile, figma, design, brand, screenshot,
  accessib, play store, lottie, icon, font ...), sonra ilgili dosyalar klonda okundu.
- Reddit: arctic-shift `posts/search?subreddit=...&title=...` ve
  `comments/search?link_id=...`.
- npm kayıt defteri: `@google/design.md` son sürüm 0.4.0; `claude-code-templates` 1.30.0
  (lisans alanı MIT). `curl https://registry.npmjs.org/<paket>`.
- Brand.kt / JSON karşılaştırması: `docs/marka/konsept-1-belirtecler.json` ile
  `app/src/main/kotlin/com/sende/app/ui/theme/Brand.kt` değerleri elle okundu.

## Temizlik ve lisans notu

- Başkasının ekran görüntüsü veya tasarımı indirilmedi. Klonlar, indirilen font
  dosyaları ve geçici çıktılar iş bitince kendi scratch klasöründen silindi.
- Kod kopyalanmadı; okunan her repo yalnız anlamak içindi. Lisanslar: Apache-2.0
  (android/skills, design.md, stitch-skills, chrisbanes, roborazzi, lottie, mobile-mcp,
  compose-samples, new-silvermoon, skydoves), MIT (aitmpl, aldefy, Meet-Miyani,
  krutikJain, SangkuOh, Android-MCP, awesome-android-ui, spyglass), CC BY-NC-ND 4.0
  (awesome-claude-code; kopyalama/uyarlama yok), karma (openai/plugins: eklenti başına).
- Font lisansı: SIL OFL 1.1 (kullanılacak yazı tipleri için lisans metni uygulamayla
  birlikte verilmeli).

## Kaynak dizini

Bu dosyada geçen farklı https bağlantıları (geçiş sırasıyla).

### Resmi belgeler ve şirket sayfaları (63)
- https://developer.android.com/studio/gemini/generate-ui-with-images
- https://www.anthropic.com/news/claude-design-anthropic-labs
- https://developer.android.com/studio/preview/compose-screenshot-testing
- https://developer.android.com/studio/preview/compose-screenshot-testing-with-testsuites
- https://arxiv.org/abs/2406.16386
- https://platform.claude.com/docs/en/build-with-claude/vision
- https://developer.android.com/tools/agents/android-cli
- https://aitmpl.com
- https://developer.android.com/jetpack/androidx/releases/compose-material3
- https://blog.google/innovation-and-ai/models-and-research/google-labs/stitch-design-md/
- https://registry.npmjs.org/@google%2fdesign.md
- https://www.figma.com/blog/figma-make-now-on-your-local-code/
- https://v0.app/docs
- https://www.magicpatterns.com/
- https://help.penpot.app/mcp/
- https://android-developers.googleblog.com/2025/03/multimodal-image-attachment-now-available-gemini-android-studio.html
- https://agents.md
- https://learn.chatgpt.com/docs/agent-configuration/agents-md
- https://developer.android.com/tools/agents/android-cli/commands
- https://codelabs.developers.google.com/customizing-material-color
- https://developer.android.com/jetpack/compose/tooling/relay/install-relay
- https://help.lottiefiles.com/animation-licensing-basics-
- https://rive.app/docs/runtimes/android/android
- https://developer.android.com/develop/ui/compose/graphics/draw/shapes
- https://developer.android.com/jetpack/androidx/releases/graphics
- https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
- https://dl.google.com/dl/android/maven2/androidx/compose/material/material-icons-extended-android/1.7.8/material-icons-extended-android-1.7.8.aar
- https://developer.android.com/develop/ui/compose/text/fonts
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/uppercase.html
- https://help.figma.com/hc/en-us/articles/32132100833559-Guide-to-the-Figma-MCP-server
- https://help.figma.com/hc/en-us/articles/39888612464151-Claude-Code-and-Figma-Set-up-the-MCP-server
- https://developers.figma.com/docs/figma-mcp-server/rate-limits-access/
- https://aitmpl.com/llms.txt
- https://www.aitmpl.com/search-index.json
- https://www.aitmpl.com/component/{tür}/{kategori}/{ad}
- https://docs.aitmpl.com/installation
- https://www.aitmpl.com/component/skills/creative-design/frontend-design
- https://www.aitmpl.com/component/skills/creative-design/mobile-design
- https://www.aitmpl.com/component/skills/creative-design/figma
- https://www.aitmpl.com/component/skills/creative-design/figma-implement-design
- https://www.aitmpl.com/component/mcps/devtools/figma-dev-mode
- https://www.aitmpl.com/component/skills/development/android-cicd
- https://www.aitmpl.com/component/skills/business-marketing/app-store-optimization
- https://www.aitmpl.com/component/skills/development/screenshot-feature-extractor
- https://www.aitmpl.com/component/commands/utilities/screenshot-analyzer
- https://www.aitmpl.com/component/agents/accessibility/accessibility-tester
- https://www.aitmpl.com/component/skills/creative-design/accessibility-auditor
- https://www.aitmpl.com/component/mcps/devtools/android-mcp
- https://www.aitmpl.com/component/skills/utilities/playwright-skill
- https://www.aitmpl.com/component/skills/scientific/generate-image
- https://www.aitmpl.com/component/skills/creative-design/canvas-design
- https://www.aitmpl.com/component/skills/video/remotion
- https://www.aitmpl.com/component/agents/development-tools/ascii-ui-mockup-generator
- https://www.aitmpl.com/component/agents/expert-advisors/se-ux-ui-designer
- https://www.aitmpl.com/component/agents/development-team/mobile-app-developer
- https://www.aitmpl.com/component/agents/programming-languages/kotlin-specialist
- https://dl.google.com/android/cli/latest/linux_x86_64/install.sh
- https://support.google.com/googleplay/android-developer/answer/9866151
- https://developer.android.com/develop/ui/views/launch/icon_design_adaptive
- https://www.meta.com/brand/resources/whatsapp/whatsapp-brand/
- https://developer.android.com/develop/ui/compose/accessibility/testing
- https://support.google.com/accessibility/android/answer/7101858
- https://registry.npmjs.org/<paket

### Kaynak kod ve depolar (GitHub) (28)
- https://github.com/google-labs-code/stitch-skills
- https://github.com/android/skills
- https://github.com/davila7/claude-code-templates
- https://github.com/google-labs-code/design.md
- https://github.com/aldefy/compose-skill
- https://github.com/Meet-Miyani/compose-skill
- https://github.com/takahirom/roborazzi
- https://github.com/zainkhalid91/spyglass
- https://github.com/airbnb/lottie-android
- https://github.com/iart-ai/motion-skills
- https://github.com/openai/plugins
- https://github.com/google/material-design-icons
- https://github.com/phosphor-icons/core
- https://github.com/composablehorizons/composeicons
- https://github.com/lucide-icons/lucide
- https://github.com/davila7/claude-code-templates/blob/main/dashboard/public/plugins.json
- https://github.com/chrisbanes/skills
- https://github.com/krutikJain/android-agent-skills
- https://github.com/new-silvermoon/awesome-android-agent-skills
- https://github.com/skydoves/compose-performance-skills
- https://github.com/SangkuOh/build-androidos-apps
- https://github.com/mobile-next/mobile-mcp
- https://github.com/CursorTouch/Android-MCP
- https://github.com/EthanY33/atelier
- https://github.com/neonwatty/logo-designer-skill
- https://github.com/hesreallyhim/awesome-claude-code
- https://github.com/wasabeef/awesome-android-ui
- https://github.com/android/compose-samples

### Topluluk (Reddit, HN, dev.to, Medium, YouTube) (26)
- https://reddit.com/r/androiddev/comments/1wc1ns5/best_claude_skills_you_found_as_a_android_dev/
- https://news.ycombinator.com/item?id=49058547
- https://reddit.com/r/androiddev/comments/1ut6fz6/ai_tools_for_android_ui_design_whats_worth_using/
- https://reddit.com/r/androiddev/comments/1u0t6fz/app_ai_design/
- https://news.ycombinator.com/item?id=47074767
- https://www.youtube.com/watch?v=rUMdODftxQE
- https://news.ycombinator.com/item?id=50107557
- https://news.ycombinator.com/item?id=43346628
- https://dev.to/eevajonnapanula/does-ai-generate-accessible-android-apps-2gb5
- https://dev.to/eevajonnapanula/does-claude-generate-accessible-apps-5759
- https://dev.to/raio/i-built-an-android-app-in-4-days-with-zero-android-experience-using-claude-code-and-a-two-layer-2p44
- https://news.ycombinator.com/item?id=41387764
- https://dev.to/arsalanmeee/screenshot-to-code-sounds-easy-here-are-5-things-ai-still-gets-wrong-12j0
- https://dev.to/juholee/figma-mcp-claude-code-give-it-your-real-layout-variables-and-components-497m
- https://dev.to/albertvilella/your-ai-doesnt-think-in-pixels-why-raw-screenshot-to-code-workflows-waste-your-context-window-2nfe
- https://reddit.com/r/androiddev/comments/1wi56w0/i_built_mugshot_a_screenshot_testing_library/
- https://reddit.com/r/androiddev/comments/1ty3n9i/am_i_missing_something_or_is_preview_in_compose/
- https://reddit.com/r/androiddev/comments/1s7dlfb/jetpack_compose_preview_is_hell/
- https://medium.com/@prnksingh829/snapshot-testing-jetpack-compose-with-roborazzi-a-quickstart-guide-911358662d9c
- https://news.ycombinator.com/item?id=37140191
- https://news.ycombinator.com/item?id=43577809
- https://news.ycombinator.com/item?id=49667036
- https://news.ycombinator.com/item?id=46413510
- https://news.ycombinator.com/item?id=50004971
- https://reddit.com/r/androiddev/comments/1u3shbb/introducing_blueprint_compose_preview/
- https://reddit.com/r/androiddev/comments/1vv74lp/can_we_do_something_about_these_weird_free/

### Blog, satıcı ve diğer (21)
- https://builder.io/blog/convert-figma-to-jetpack-compose
- https://uxmagic.ai/blog/galileo-ai-is-now-google-stitch
- https://www.banani.co/blog/google-stitch-ai-review
- https://mowgli.ai/blog/figma-make-vs-stitch-vs-claude-design-vs-v0-vs-lovable
- https://www.codecademy.com/article/google-stitch-tutorial-ai-powered-ui-design-tool
- https://alloy.app/library/magic-patterns-reviews-pricing-and-alternatives
- https://sy.dealroom.co/news/feed/miro-acquires-uizard-expands-design-tools
- https://repositorio.ufc.br/ri/handle/riufc/81252?mode=full
- https://vmobify.com/blog/claude-code-android-development
- https://www.mdskills.ai/rules/cursor-android-jetpack-compose?tab=source
- https://www.digitalapplied.com/blog/screenshot-driven-ui-development-vision-models-2026
- https://miromiro.app/blog/screenshot-to-code-what-actually-works
- https://hub.telefonica.com/en/engineering/testing-screenshot-roborazzi-systems-design
- https://blog.kinto-technologies.com/posts/2024-12-13-Introducing-Screenshot-Testing-in-UnlimitedApp/
- https://glama.ai/mcp/servers/kenneives/design-token-bridge-mcp
- https://www.callstack.com/blog/lottie-vs-rive-optimizing-mobile-app-animation
- https://fge-auto-dotting-benchmark.static.hf.space/index.html
- https://mcp.figma.com/mcp
- https://www.builder.io/blog/claude-code-figma-mcp-server
- https://www.clarkhill.com/news-events/news/u-s-copyright-office-ponders-copyright-protection-for-ai-generated-works-following-d-c-circuits-thaler-decision/
- https://www.jukeboxprint.com/blog/what-small-businesses-own-ai-generated-art
