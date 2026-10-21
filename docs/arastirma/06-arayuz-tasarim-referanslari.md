# Arayüz ve marka tasarım referansları
Araştırma kolu: tasarim-referans

Tarih: 21 Ekim 2026
Kapsam: Sende (Android, Kotlin + Compose) için arayüz, marka, hareket ve ergonomi
referansları. 100'den fazla kaynak incelendi. Her iddianın yanında https bağlantısı
var. Okunamayan veya doğrulanamayan şey açıkça "doğrulanamadı" yazıldı. Önceki
araştırmalarla (01-04) çakışan konu tekrar edilmedi; Android bildirim çubuğu
davranışı için 04 numaralı dosyaya bak.

## Kısa özet (önce bunu oku)

- Premium his, tek bir "ayırt edici şey" ile disiplinli sessizliğin karışımı. Cash
  App tek bir yeşili (#00E013) "her yerde ama düşünerek" kullanıyor. Monzo "hot
  coral" rengine daha çok yaslanırken paleti genişletti. Duolingo yeşil ve bir
  maskotla tanınıyor.
  - https://design.cash.app/color
  - https://monzo.com/blog/weve-had-a-little-makeover
  - https://www.canny-creative.com/brand-breakdown/brand/duolingo/
- Linear'ın 2026 yenilemesinin iki ilkesi Sende'ye doğrudan uyuyor: "Kazanmadığın
  dikkat için yarışma" ve "Yapı hissedilsin, görülmesin" (daha az ve daha yumuşak
  çizgi, daha az ikon, sönük gezinme).
  - https://linear.app/now/behind-the-latest-design-refresh
- "AI slop" görünümü kataloglarda şöyle: mor-mavi gradyan, Inter, ortalanmış hero,
  üç eş kart, her şeye aynı yuvarlak köşe ve aynı gri gölge, cam efekti, koyu zemin
  üstünde neon vurgu, kartın içinde kart. Anthropic'in kendi tasarım yönergesi artık
  "krem zemin + kiremit vurgu", "yakın-siyah + tek parlak vurgu" ve "ince çizgili
  gazete düzeni"ni de varsayılan (yani ayırt edici olmayan) sayıyor.
  - https://raw.githubusercontent.com/anthropics/skills/main/skills/frontend-design/SKILL.md
  - https://cdn.jsdelivr.net/npm/pixelslop@0.3.8/dist/skill/resources/ai-slop-patterns.md
- Önemli çıkarım: Sende'nin 5 marka konseptinin çoğu o ikinci kalıp listesine denk
  düşüyor (Bölüm 2). Karar kurucunun. Ama seçmeden önce "bu konsept neden bu ürünün
  konsepti" sorusunun tek cümlelik, ürüne özgü bir cevabı olmalı.
- Başparmak ergonomisi: Hoober'ın gözlemi %49 tek el, %36 "beşik" tutuş, %15 iki el;
  tutuş birkaç saniyede bir değişiyor. Hoober 2017'de eski "başparmak süpürme
  şeması"nın yanlış olduğunu yazdı: insanlar ekranın ORTASINDA daha isabetli. Hedef
  boyutu merkezde en az 7 mm, köşede en az 12 mm (yaklaşık 44 dp ve 76 dp).
  - https://www.uxmatters.com/mt/archives/2013/02/how-do-users-really-hold-mobile-devices.php
  - https://www.uxmatters.com/mt/archives/2017/03/design-for-fingers-touch-and-people-part-1.php
- Hareket: Material 3 Expressive, süre/easing yerine yay (spring) jetonlarına geçti.
  `standard` şema "sık tekrar eden işlevsel öğeler", `expressive` şema "öne çıkan
  etkileşimler" için. Sende'nin mevcut genişleme yayı (sönüm 0.75, sertlik 400) sık
  tekrar eden bir satır için fazla zıplak.
  - https://m3.material.io/styles/motion/overview/how-it-works
  - https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/MotionScheme.kt
- İzin ekranı: tek birincil düğme, sonucu gösteren küçük bir önizleme, sessiz bir
  "Şimdi değil" ve reddedilince alternatif yol. Play ayrıca uygulama içinde
  "belirgin açıklama ve açık onay" istiyor.
  - https://developer.apple.com/tutorials/data/design/human-interface-guidelines/privacy.json
  - https://support.google.com/googleplay/android-developer/answer/11150561
- Boş durum üç iş yapmalı: sistem durumunu söyle, öğret, doğrudan bir yol ver.
  Sende'nin mevcut boş durumları ("tek kısa cümle, en fazla bir düğme") bu ölçüte
  uyuyor.
  - https://www.nngroup.com/articles/empty-state-interface-design/
- Marka çıktısının düzeni: Cash App "Foundations / Expressions / Resources" diye üç
  katman kuruyor. Duolingo, Monzo ve Slack süreçlerinde ortak adım: önce denetim ve
  anlatı, logo en sonda.
  - https://design.cash.app/typography
  - https://www.johnsonbanks.co.uk/templates/work/duolingo
  - https://itsnicethat.com/news/michael-bierut-pentagram-slack-rebrand-graphic-design-170119
- Kaynak dürüstlüğü: Behance ve Dribbble doğrudan açılmıyor (bot engeli). Liste ve
  başlıklar bir okuyucu proxy ile okundu; küçük resimler indirilip gerçekten
  incelendi (Bölüm "Nasıl araştırdım"). PageFlows, Screenlane, Lapa Ninja açılamadı.

## Bu belgede ne var (telefonda gezinmek için)

- 1. Premium desenler: renk, boşluk, yazı, köşe, gölge, ikon, hareket, karakter.
- 2. "AI slop" işaretleri, kaçınma listesi, Sende'nin 5 konseptiyle karşılaştırma.
- 3. Mesaj / gelen kutusu / liste ekranı için en iyi örnekler.
- 4. Tek elle kullanım ve başparmak ergonomisi.
- 5. Boş durum, onboarding ve izin açıklaması.
- 6. Animasyon ilkeleri (süre, yay, liste hareketleri, hareketi azaltma, haptik).
- 7. Marka sistemi çıktısı nasıl düzenlenir (süreç + klasör önerisi + mağaza
  ölçüleri).
- 8. Türkçe ve Türkiye'ye özgü notlar.
- Sende için çıkarımlar, Denenen ve çalışmayan yollar, Doğrulanamayanlar.
- Referans listesi (271 madde, bağlantı + tek cümle neden) ve temizlik notu.

## Nasıl araştırdım

- Araçlar
  - curl, yerel Chromium (Playwright; "playwright install" çalıştırılmadı).
  - Okuyucu proxy: r.jina.ai (üçüncü taraf hizmet). Yalnızca herkese açık sayfa
    adresleri gönderildi. Behance, Dribbble, Mobbin ve Material 3 sitesi bu yolla
    okundu.
  - Hacker News Algolia API'si, Reddit arşivi (arctic-shift), YouTube oEmbed (video
    başlığı doğrulama).
  - AndroidX kaynak kodu (anonim raw.githubusercontent.com): yay sabitleri doğrudan
    koddan okundu.
- Kapsam
  - Yaklaşık 350 adres denendi (bir kısmı doğrudan, bir kısmı okuyucu proxy ile);
    açılan ve içeriği kullanılabilen kaynaklar aşağıdaki listede.
  - İçeriği gerçekten okunan: Material 3, Android, Apple HIG, WWDC, Linear, Cash
    App, Slack, Telegram, NN/g, Hoober, Smashing, W3C, Anthropic cookbook ve
    SKILL.md, HN ve Reddit yorumları.
  - Yalnız liste/başlık görülen: galeri siteleri (Mobbin, Refero, Dribbble etiket
    sayfaları, Behance arama).
- Görsel inceleme (hepsi notlandıktan sonra silindi)
  - Dribbble: 10 küçük resim (mesajlaşma, gelen kutusu, boş durum, izin, bildirim).
  - Behance: 11 küçük resim ("mobile app branding case study" araması).
  - Mobbin izin ekranları: 4 ekran. Google Messages Material 3 Expressive: 2 ekran
    görüntüsü.
  - Sende'nin kendi 5 marka panosu (docs/marka/konsept-N.png): yalnız karşılaştırma
    için.
- Okuma işaretleri
  - [resmi] platform veya şirketin kendi belgesi.
  - [kaynak kod] AndroidX/AOSP kodu okundu.
  - [araştırma] ölçüm veya yayımlanmış çalışma.
  - [blog] bağımsız yazar, ajans veya haber yazısı.
  - [topluluk] Reddit, HN, forum yorumu.
  - [görsel] görseli indirip gerçekten açtım.
  - [liste] galeri veya liste sayfası; yalnız öğeler görüldü.
  - [göz gezdirildi] sayfa açıldı, yalnız başlık ve ilk bölümü okundu.
  - [tek kaynak] tek bağımsız kaynak; dikkatli oku.
  - [çıkarım] kaynaklardan benim çıkardığım sonuç.
  - [doğrulanamadı] kanıt bulunamadı veya okunamadı.
- Çeviri notu: Tırnak içindeki yabancı dil alıntıları serbest çevirimdir; birebir
  değildir. Kaynak bağlantısı hemen altında.
- Lisans: Material/AndroidX kodu Apache-2.0 (sabitler ve akışlar not alındı, kod
  kopyalanmadı). Apple HIG metinleri yalnız okundu. Başkasının tasarımı
  kopyalanmadı; yalnız ilke çıkarıldı.

## 1. Premium hissi veren ortak desenler

### Renk
- Tek "kahraman" renk, geri kalan sakin nötrler.
  - Cash App: tek renk "Cash Green" (#00E013); "her marka temas noktasında
    bulunmalı"; "yeşil sel" güçlü olabilir ama yeşilin tek görünme biçimi olmamalı,
    "inceliklice ve bilinçle" kullanılmalı deniyor. [resmi]
    - https://design.cash.app/color
  - Monzo: "hot coral"a daha çok yaslanıp paleti genişletti; ikincil renkler kısmen
    erişilebilirlik için seçildi. [resmi]
    - https://monzo.com/blog/weve-had-a-little-makeover
  - Duolingo: Feather Green (#58CC02), yazıda Eel (#4B4B4B), zeminde beyaz; ikincil
    renkler "yeşili sulandırmadan" çeşitlilik katıyor. [blog; marka kılavuzunu
    aktarıyor]
    - https://www.canny-creative.com/brand-breakdown/brand/duolingo/
  - Headspace: turuncu korundu, palet genişletildi; kontrast ve renk kombinasyonları
    erişilebilirliğe göre düzenlendi. [blog]
    - https://itsnicethat.com/articles/italic-studio-headspace-graphic-design-project-250424
  - Slack 2019: 11 tonluk palet 4 ana renge indi; patlıcan moru zemin olarak kaldı.
    [blog]
    - https://www.dezeen.com/2019/01/17/slack-logo-rebrand-pentagram/
- Sakin nötr: gri tonunda ince ayar önemli. Linear'ın 2026 yenilemesi soğuk-mavimsi
  griden "daha sıcak ama hâlâ net, daha az doygun" griye geçti; fazla sıcaksa
  "çamurlaşıyor" diye tonu canlı bir renk aracıyla (ton, doygunluk, parlaklık
  sürgüleri) denediler. [resmi]
  - https://linear.app/now/behind-the-latest-design-refresh
- Marka rengini ölçülü kullan: Apple "marka rengini cömertçe yayma; kontrollerde en
  aza indir, birincil eylem ve durum göstergeleri (okunmamış rozeti, seçili sekme
  ikonu) için kullan" diyor. Başlıkta özel yazı, gövdede sistem yazısı da
  öneriliyor. [resmi]
  - https://developer.apple.com/design/human-interface-guidelines/branding
- Tema üretimini az girdiye indir: Linear (2024) her tema için 98 değişken
  tanımlamak yerine 3 girdi tanımladı: taban renk, vurgu rengi, kontrast. LCH renk
  uzayı kullanıyorlar ("açıklığı 50 olan kırmızı ve sarı insan gözüne yaklaşık eşit
  açık görünür"); kontrast girdisi sayesinde çok yüksek kontrastlı temalar otomatik
  çıkıyor. Başlıkta Inter Display, metinde Inter. Not: Inter slop listelerinde
  geçiyor ama Linear'ın seçimi bilinçli; sorun yazı tipi değil, seçilmemiş
  varsayılan. [resmi]
  - https://linear.app/now/how-we-redesigned-the-linear-ui
- Koyu tema kuralları (Material): önerilen yüzey #121212, vurgu renkleri doygunluğu
  azaltılarak her yükseklikte 4.5:1 geçmeli, büyük yüzeyler koyu, vurgu sınırlı.
  Beyaz metin #121212 üstünde 15.8:1. [resmi]
  - https://m2.material.io/design/color/dark-theme.html
- Kontrast ölçütleri: metin 4.5:1, yüzeye karşı simge/kenarlık gibi metin dışı öğe
  3:1. Android gövde metnini 12 sp'den küçük yapma diyor. [resmi]
  - https://developer.android.com/design/ui/mobile/guides/foundations/accessibility
  - https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast.html
- Palet üretirken algısal renk uzayı kullanan ekipler (Stripe: CIELAB; Evil
  Martians: OKLCH; Radix: 12 basamaklı ölçek) aynı sorunu çözüyor: sarı ve yeşil
  aynı "parlaklık" numarasında bile çok daha açık görünür.
  - https://stripe.com/blog/accessible-color-systems
  - https://evilmartians.com/chronicles/oklch-in-css-why-quit-rgb-hsl
  - https://www.radix-ui.com/colors/docs/palette-composition/understanding-the-scale
  - Radix basamak işlevleri: 1-2 arka plan, 3-5 bileşen zemini, 6-8 kenarlık, 9-10
    dolu zemin, 11-12 metin. 11 ve 12, 2. basamak üstünde APCA Lc 60 ve Lc 90
    garantili.
- Renkli zeminde gri metin yok: "Refactoring UI" içindeki başlıklardan biri ("Don't
  use grey text on colored backgrounds"); AI-slop kataloğunda 24 numaralı işaret.
  [blog/kitap içeriği]
  - https://refactoringui.com/
  - https://cdn.jsdelivr.net/npm/pixelslop@0.3.8/dist/skill/resources/ai-slop-patterns.md
- [çıkarım] Sende'ye özgü: içerikte zaten renk gürültüsü var (WhatsApp yeşili,
  Instagram gradyanı, Telegram mavisi gerçek uygulama ikonları). Bu yüzden arayüzün
  kendi rengi (vurgu) çok az ve anlamlı kullanılmalı: "yeni mesaj" dışında hiçbir
  şeye vurgu rengi verme. Linear'ın "kazanmadığın dikkat" ilkesinin uygulaması bu.

### Boşluk ve yoğunluk
- Aşırı boşluk eleştiri konusu. HN'de Material 3 Expressive başlığında (360 puan,
  557 yorum) yorumlardan birkaçı "çok boşluk, az bilgi" ve "giderek azalan kontrast"
  diyor (bu bir örneklem, sayım değil). Bir yorumcu Gmail'in 3 yoğunluk ayarını
  "zirve tasarım" diye anıyor. [topluluk]
  - https://news.ycombinator.com/item?id=43975352
  - https://hn.algolia.com/api/v1/items/43975352
- Aynı başlıkta olumlu yorum da var: "düğmeleri başparmağa yaklaştırmak harika".
  Yani yorumlar boşluğun kendisini değil, içeriğin boşluk uğruna küçülmesini
  eleştiriyor [çıkarım].
- Google'ın kendi araştırması "dokunma hedefi, kontrast gibi konularda mevcut
  standartların ÜSTÜNE çıktık" diyor ve e-posta örneğinde Gönder düğmesini büyütüp
  klavyenin hemen üstüne aldı. [resmi araştırma]
  - https://design.google/library/expressive-material-design-google-research
- Linear 2026: "Yoğun bilgiyi bunaltmadan koru." Kenarlıklar çoğalmıştı; "kenarları
  yuvarlayıp kontrastı yumuşatarak" yapı korundu, gürültü azaldı. [resmi]
  - https://linear.app/now/behind-the-latest-design-refresh
- Android: benzer öğeler arasında tutarlı boşluk; görünüm başına çok fazla eylem
  koyma. [resmi]
  - https://developer.android.com/design/ui/mobile/guides/layout-and-content/layout-basics
- Her yerde aynı boşluk (hep 24 dp) bir slop işareti; ritim için "grup içi dar,
  gruplar arası geniş" kullan (yakınlık ilkesi). [blog + çıkarım]
  - https://cdn.jsdelivr.net/npm/pixelslop@0.3.8/dist/skill/resources/ai-slop-patterns.md
- [çıkarım] Sende bir "ayıklama" (triage) aracı; bir işletmeci günde yüzlerce
  bildirim görebilir. Rahat/sıkı iki yoğunluk ayarı (Gmail örneği) ve 200% yazı
  ölçeğinde bozulmayan satır düzeni, M3 Expressive'in boşluk eleştirisine karşı
  sigorta.

### Tipografi
- İki rol, net iş bölümü.
  - Monzo: "kahraman" yazı tipi Oldschool Grotesk, işlev için özel kesim Monzo Sans.
    [resmi]
    - https://monzo.com/blog/weve-had-a-little-makeover
  - Duolingo: başlıkta özel Feather Bold (yalnız küçük harf), gövdede DIN Next
    Rounded; ikisi aynı cümlede karışmıyor. [blog]
    - https://www.canny-creative.com/brand-breakdown/brand/duolingo/
  - Headspace: Colophon Foundry'nin Aperçu'dan türettiği özel yazı; "oyuncul sesten
    klinik sese geçebildiği için" seçildi. [blog]
    - https://www.printmag.com/?p=767405
- Cash App tipografi sayfasının bölümleri: "Our typeface, Everyday usage,
  Experimental usage, Typesetting, Typeface features, Incorrect usage, Secondary
  typeface, Languages". "Languages" bölümü Türkçe için de önemli: yazı tipinin dil
  kapsamı kılavuzun parçası. [resmi]
  - https://design.cash.app/typography
- Material: tek ölçek, 15 temel + 15 vurgulu (emphasized) stil; "hiçbir ürün hepsini
  kullanmaz". [resmi]
  - https://m3.material.io/styles/typography/overview
- Apple: en küçük 11 pt; yazıyı en az %200 büyütmeye izin ver; ince ağırlıklarda
  daha büyük boyut kullan. [resmi]
  - https://developer.apple.com/tutorials/data/design/human-interface-guidelines/accessibility.json
- Anthropic yönergesi: satır uzunluğu 80 karakterin altı; "BÜYÜK HARF etiket",
  "başlıkta tek kelimeyi vurgulama", "her başlığın üstüne küçük etiket" üretilmiş
  sayfaların en sık işaretleri. [resmi]
  - https://raw.githubusercontent.com/anthropics/skills/main/skills/frontend-design/SKILL.md
- Türkçe için sınama: İ ı Ğ ğ Ş ş Ç ç Ö ö Ü ü her ağırlıkta çizilmeli. Eski bir
  WordPress forum başlığında "yazı tipi Latin Extended içermiyorsa Ş Ç Ğ Ö Ü
  kayboluyor" sorunu anlatılıyor. [topluluk, eski, tek kaynak]
  - https://www.cryoutcreations.eu/forums/t/missing-turkish-characters-s-c-g-o-u-etc
  - https://haacked.com/archive/2012/07/05/turkish-i-problem-and-why-you-should-care.aspx/
  - https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/uppercase.html
  - Kotlin'de `uppercase()` "invariant locale" kullanır: "i" -> "I" (Türkçede "İ"
    olmalı). Uygulamada `app/src/main/kotlin/com/sende/app/ui/common/Images.kt`
    93-94. satırlar avatar baş harfini `uppercase()` ile alıyor. [çıkarım; kod
    yalnız okundu, değiştirilmedi]
  - Çözüm yönü: `uppercase(Locale("tr"))` veya Compose `Locale.current`; daha iyisi
    BÜYÜK HARF etiketlerinden vazgeçmek (aynı zamanda slop işareti).

### Köşe yarıçapı
- Tek köşe değeri herkese uygulanmıyor, bir ölçek var. Material Expressive ölçeğe
  20, 32, 48 dp ve "full" ekledi; "şekil semantik değil çok yönlüdür"; "küçük
  şekiller temel eylemi daha az önemli gösterebilir". [resmi]
  - https://m3.material.io/styles/shape/overview-principles
  - https://m3.material.io/blog/building-with-m3-expressive
- İç içe köşe: Apple'ın Liquid Glass anlatımında kontroller pencerenin yuvarlak
  köşelerine "eş merkezli (concentric)" oturuyor. Kural: iç yarıçap = dış yarıçap -
  aradaki boşluk. [resmi + çıkarım]
  - https://developer.apple.com/videos/play/wwdc2025/219/
- Kaçınılacak: "her şeye aynı yarıçap, aynı gri gölge" (Anthropic'in "SaaS-card kit"
  maddesi) ve "kartın içinde kart" (slop kataloğu 9 ve 10). [resmi + blog]
  - https://raw.githubusercontent.com/anthropics/skills/main/skills/frontend-design/SKILL.md
  - https://cdn.jsdelivr.net/npm/pixelslop@0.3.8/dist/skill/resources/ai-slop-patterns.md
- İyi örnek: Google Messages'ın Material 3 Expressive sürümünde konuşma listesi
  satır başına ayrı kart yerine TEK kapsayıcı içinde; uygulama çubuğu ve durum
  çubuğu birleşik; içerik alanının üst köşeleri yuvarlak; uygulama çubuğu "kendi
  (daha koyu) katmanı". Ekleme menüsünde "renkli ikonlar kaldırıldı", ikonlar hap
  şekilli kapsayıcılarda, ızgara yoğunluğu düşürüldü. [haber + görsel]
  - https://9to5google.com/2025/08/20/google-messages-homepage-redesign/
  - https://9to5google.com/2025/06/02/google-messages-material-3-expressive-chat/
- Material'ın liste kuralı: kapsayıcı listede ayırıcı çizgi yerine "boşluk (gap)";
  ayırıcı yalnız kapsayıcısız veya karmaşık listede. [resmi]
  - https://m3.material.io/components/lists/guidelines
- [çıkarım] Sende'de `ConversationCard` bugün: dış kart (20 dp, 1 dp çizgi) + içinde
  satırlar (14 dp) + iç parçalar (10 dp). Bu "kartın içinde kart". Kurucunun "grup
  tek kapsayıcı kutu" isteği (I-29) ile uyumlu çözüm: tek kapsayıcı, içinde
  çizgisiz/gölgesiz satırlar, aralarda boşluk.

### Gölge ve yükseklik
- Koyu temada yükseklik gölgeyle değil açık yüzey tonuyla anlatılır; Material koyu
  tema belgesi: "yükseklik arttıkça yüzey açılır". [resmi]
  - https://m2.material.io/design/color/dark-theme.html
- Açık temada tek ışık kaynağı, birkaç katmanlı hafif gölge, gölge rengini zemine
  uydurmak. [blog]
  - https://www.joshwcomeau.com/css/designing-shadows/
- Önce gölge değil boşluk ve zemin farkı: "Refactoring UI" kısa özeti: "Daha az
  kenarlık kullan; yerine gölge, zıt zemin veya daha çok boşluk dene". [blog/kitap]
  - https://refactoringui.com/
- Linear: "Yapı hissedilsin, görülmesin." Ayırıcıları kaldırmadı, yumuşattı. [resmi]
  - https://linear.app/now/behind-the-latest-design-refresh

### İkon dili
- İkon ancak anlam taşıyorsa değerlidir; çoğu ikon için standart yok, metin etiketi
  gerekir. Aksi halde "göz tatlısı ve görsel gürültü". [araştırma/blog]
  - https://www.nngroup.com/articles/icon-usability/
- Linear 2026: ikon kullanımı azaltıldı, boyutu küçüldü, renkli takım ikon arka
  planları kaldırıldı. [resmi]
  - https://linear.app/now/behind-the-latest-design-refresh
- Google Messages'ın Material 3 Expressive sürümü ekleme menüsünde "renkli ikonları
  kaldırdı"; tek tonlu ikon + hap kapsayıcı. [haber + görsel]
  - https://9to5google.com/2025/06/02/google-messages-material-3-expressive-chat/
- Tek aile ve değişken eksenler: Material Symbols (Apache-2.0) ağırlık 100-700,
  doluluk (fill) 0/1, derece (grade), optik boyut. Doluluk ekseni durum geçişi için
  önerilir (okunmamış dolu, okunmuş çizgili). Metin derecesi ile ikon derecesi
  eşleştirilebilir. [resmi]
  - https://developers.google.com/fonts/docs/material_symbols
  - https://fonts.google.com/icons
- Alternatifler: Lucide (ISC lisansı), Phosphor (lisans bu araştırmada
  doğrulanamadı). [resmi]
  - https://lucide.dev/
  - https://phosphoricons.com/
- [çıkarım] Sende'de renkli içerik ikonları (gerçek uygulama ikonları) var. Arayüz
  ikonları tek renk, tek kalınlık, çizgi tipinde olmalı ki rekabet etmesin.

### Hareket (özet; ayrıntı Bölüm 6)
- Premium his hızlı geri bildirimden gelir: Apple "çok kısa ve kesin geri bildirim"
  ister, sık tekrar eden etkileşimlere süs hareketi eklememeyi söyler. [resmi]
  - https://developer.apple.com/tutorials/data/design/human-interface-guidelines/motion.json
- Yay tabanlı hareket yarıda kesilebilir ve hızı korur (kesilmiş animasyon
  sıçramaz). [resmi]
  - https://developer.apple.com/videos/play/wwdc2023/10158/
  - https://developer.android.com/develop/ui/compose/animation/quick-guide

### Karakter: tek somut şey
- Ayırt edici markalar bir nesne, kelime veya fikir üzerine kurulu: Monzo'nun mercan
  kartı, Duolingo'nun baykuşu ve tüylü yazı tipi, Headspace'in turuncu gülen kafası
  ve nefes animasyonu, Things'in "Magic Plus" düğmesi (sürüklenip bırakıldığı yere
  göre görev oluşturuyor) ve görevi karta çeviren genişleme. [blog + resmi]
  - https://www.johnsonbanks.co.uk/templates/work/duolingo
  - https://itsnicethat.com/articles/italic-studio-headspace-graphic-design-project-250424
  - https://www.macstories.net/reviews/things-3-beauty-and-delight-in-a-task-manager/
- Monzo'nun kendi yazısında anti-jenerik tutum açık: illüstrasyonları "düz geometri
  ve jenerik insanlar kullanan popüler kurumsal stilden" uzaklaştırdılar; fotoğrafta
  "bankacılıkta jenerik insan fotoğrafı bol" diyerek sıcak tonlu, güneş ışığı
  parlamalı, dramatik gölgeli bir stil seçtiler. [resmi]
  - https://monzo.com/blog/weve-had-a-little-makeover
- Behance'te "mobile app branding case study" aramasındaki işlere bakınca [görsel]:
  gerçekten ayırt edilenler tek somut bir nesneye yaslanıyor (illüstrasyonlu bir
  fennec maskotu, kart üstünde limon + koyu yeşil ikili, Arapça "bulut" sözcüğünden
  çıkan bir marka hikâyesi); jenerik görünenler mor-mavi gradyan, cam panel ve elde
  telefon fotoğrafı üstüne kurulu (öznel okuma; bu işler kötü değil, ayırt edici
  değil).
  - https://www.behance.net/search/projects/mobile%20app%20branding%20case%20study
  - https://www.behance.net/gallery/256309773/Tritter-Crypto-Piggy-Bank-iOS-App
  - https://www.behance.net/gallery/245508601/Rillo
  - https://www.behance.net/gallery/255784383/Miznaa-Fintech-App
  - https://www.behance.net/gallery/251945531/Hiff-Brand-Identity-Mobile-App-Design
- Not: "premium his" üzerine pazarlama blogları (VP0, Glance vb.) "%40 daha çok
  etkileşim" gibi rakamlar veriyor; bu rakamların kaynağı gösterilmiyor. Güvenme.
  [doğrulanamadı]
  - https://vp0.com/blogs/what-makes-an-app-look-professional
  - https://thisisglance.com/blog/the-psychology-of-luxury-app-design-and-why-it-matters

## 2. "AI slop" işaretleri ve kaçınma listesi

### Kaynaklar ne diyor
- Terimin kaynağı: Anthropic'in "frontend estetiği" cookbook'u (21 Ekim 2025). Model
  "dağılıma yakın (on distribution) çıktılara yakınsıyor"; bunun adı kullanıcıların
  "AI slop estetiği" dediği şey. Kaçınılacaklar: aşırı kullanılan yazı aileleri
  (Inter, Roboto, Arial, sistem yazıları), beyaz zemin üstünde mor gradyan,
  öngörülebilir düzen. Cookbook "Space Grotesk"u bile ortak seçim diye anıyor.
  [resmi]
  - https://platform.claude.com/cookbook/coding-prompting-for-frontend-aesthetics
- Anthropic'in tasarım yönergesi (SKILL.md) üretilmiş tasarımın kümelendiği beş
  kalıbı sayıyor [resmi]:
  - Sıcak krem zemin (#F4F1EA civarı) + yüksek kontrastlı serif başlık + kiremit/kil
    vurgu (#D97757 civarı).
  - Yakın-siyah zemin + tek parlak "asit yeşil" veya "vermilyon" vurgu.
  - İnce çizgili, sıfır köşe yuvarlaklıklı, yoğun kolonlu gazete düzeni.
  - "SaaS kart seti": aynı yuvarlak kartlar, her şeye tek köşe yarıçapı, aynı gri
    gölge, süs olarak gradyan.
  - Konudan bağımsız "şablon kabuğu": her başlığın üstünde aralıklı BÜYÜK HARF
    etiket, noktalı meta satırı, "KELİME — parça" etiketi, saf siyah yerine tonlu
    yakın-siyah (#0B0B0B, #111), küçük etiketlerde mono yazı, düğme metnine eklenen
    "→".
  - Hareket: her bölümde "kayarak gelen" giriş ve her kartta hover, varsayılan ve
    "yapay zekâ yapmış" gibi okunuyor; kullanıcı eylemine cevap veren hareket (aç,
    genişlet, onayla) iyi.
  - https://raw.githubusercontent.com/anthropics/skills/main/skills/frontend-design/SKILL.md
- Aynı yönergenin dengeleyici cümlesi: "Bu özelliklerin hepsi bazı briflerde
  meşrudur; ama seçim değil varsayılan olduklarında sorun." Ve: "Cesaretini tek
  yerde harca; çevresi sakin kalsın." [resmi]
- Topluluk tarafı:
  - Reddit r/webdev "AI has a Purple Problem" (581 puan, 172 yorum). Yorumlarda:
    Tailwind UI bileşenlerinin varsayılanı indigo; bir yorumcu "slop'u belirli bir
    mor tonundan anlarsınız; mesele yapay zekânın başka renk üretememesi değil"
    diyor. [topluluk]
    - https://www.reddit.com/r/webdev/comments/1nx0y4q/ai_has_a_purple_problem/
  - Aynı konuda YouTube'da Syntax podcast'i "The AI Purple Problem" (video başlığı
    ve kanal oEmbed ile doğrulandı; içerik okunamadı). [doğrulanamadı (içerik)]
    - https://youtu.be/AG_791Y-vs4
  - Tailwind'in yaratıcısı Adam Wathan'ın "her Tailwind UI düğmesini bg-indigo-500
    yaptığım için özür dilerim" esprisi üç ikincil kaynakta aktarılıyor; X'teki
    birincil gönderi okunamadı. [tek kaynak değil ama birincil doğrulanamadı]
    - https://prg.sh/ramblings/Why-Your-AI-Keeps-Building-the-Same-Purple-Gradient-Website
    - https://www.925studios.co/blog/ai-slop-design-tells
    - https://robotsonpayroll.substack.com/p/how-to-avoid-that-vibe-coded-look
  - Bir yazıda "renkli SOL KENARLIK taşıyan kartlar" ve "emoji özellik ikonları",
    metindeki uzun tire kadar güvenilir "yapay zekâ" işareti sayılıyor (alıntılanan
    denetim: Hacker News'te açılan 1.590 üründen yarısından azı işaretsiz; bu
    denetimin birincil kaynağı okunamadı). [blog, doğrulanamadı]
    - https://robotsonpayroll.substack.com/p/how-to-avoid-that-vibe-coded-look
  - 925Studios: Inter + indigo gradyan + üç yuvarlak kart + "Build the future" tipi
    muğlak başlık + her yerde eşit 16 px köşe. Ajans pazarlaması; rakamlara temkinli
    ol. [blog]
    - https://www.925studios.co/blog/ai-slop-web-design-guide
- Topluluk yapımı bir "slop kataloğu" (26 görsel örüntü, şiddet puanıyla): gradyan
  metin, her yerde cam, koyu zemin + parlayan vurgu, siyan-koyu palet, mor-mavi
  gradyan, "kahraman metrik" (büyük sayı + küçük etiket), eş kart ızgarası, jenerik
  yazı, kart içinde kart, 8-16 px köşe + jenerik gölge, süs sparkline, "teknik hava"
  için mono yazı, zıplama/elastik animasyon, modal çoğaltma, tek taraflı renkli
  kenarlık, saf siyah/beyaz zemin, her şey ortalı, ikon-başlık-üstte, her düğme
  birincil, her yerde aynı boşluk, neon vurgu, her şeyi saran kart, renkli zeminde
  gri metin. [blog; lisansı bilinmiyor, yalnız okundu]
  - https://cdn.jsdelivr.net/npm/pixelslop@0.3.8/dist/skill/resources/ai-slop-patterns.md

### Mobilde gördüğüm (Dribbble ve Behance küçük resimleri)
- Dribbble "messaging app" ve "chat app" etiketleri [görsel + liste]:
  - B2C destek platformu gelen kutusu (koyu ve açık sürüm): eğik duran telefon 3D
    yeşil-mavi bir sahnede; her kişi için farklı renkli avatar halkası. İki temanın
    birlikte gösterilmesi iyi; ama avatar renkleri satırı gürültülü yapıyor ve sunum
    kullanımdan fazla göze çarpıyor.
    - https://dribbble.com/shots/26008875-Mobile-Dark-Mode-Design-for-B2C-Support-Platform
  - Chatia (AI sohbet): pembe-mor gradyan zemin, pembe vurgulu ikon ve kartlar, "AI"
    etiketli jenerik öneri kartları; slop kataloğuyla neredeyse birebir.
    - https://dribbble.com/shots/25438032-Chatia-AI-Chatbot-Mobile-App-UI-Design-Template
  - Talk Sense (müşteri mesajlaşma): pastel pembe gradyan zemin + cam görünümlü
    telefon maketleri.
    - https://dribbble.com/shots/26589117-Talk-Sense-Mobile-App-Design-for-Customer-Messaging-Platform
  - Mesaj kartları örneği: her mesaj kendi gölgeli kartında, avatar + ad + rol,
    sağda kırmızı okunmamış noktası. Nokta fikri iyi; kart içinde kart yığını zayıf.
    - https://dribbble.com/shots/25311273-Messages
  - Zendenta (hasta uygulaması gelen kutusu): sade beyaz liste, sabit satır yapısı,
    alt gezinme; metin boyutu telefonda çok küçük kalır.
    - https://dribbble.com/shots/24595764-Zendenta-Inbox-Feature-in-the-Patient-Mobile-App
- Behance arama sonuçları [görsel]: ayırt edilenler tek somut nesne taşıyor (bkz.
  Bölüm 1, Karakter). Jenerik görünenler: mor gradyan + cam panel ("UI/UX Portfolio
  2026") ve mor-siyah zemin üstünde elde telefon fotoğrafı (Hiff). Bu işler kötü
  değil; ayırt edici değil.
  - https://www.behance.net/search/projects/mobile%20app%20branding%20case%20study
  - https://www.behance.net/gallery/251945531/Hiff-Brand-Identity-Mobile-App-Design

### Kaçınma listesi (Sende için)
- Renk
  - Mor/indigo/menekşe vurgu ve mor-mavi gradyan yok.
  - Gradyan metin yok; gradyan zemin yok (marka gerektirmedikçe, gerekçesi
    yazılarak).
  - Koyu zemin + parlayan/neon vurgu ve "glow" gölge yok. Vurgu rengi parlamaz.
  - Renkli zeminde gri metin yok; metin rengi yüzey tonundan türetilir.
  - Her kişiye farklı renkli avatar halkası yok (içerik ikonları zaten renkli).
  - Saf #000/#FFF'yi varsayılan yapma. Not: gerçek siyaha yakın zemin OLED'de pil
    kazandırabilir; bu bilinçli bir seçimse gerekçesi yazılsın. [çıkarım]
- Biçim
  - Kartın içinde kart yok; tek kapsayıcı + çizgisiz satırlar.
  - Her şeye aynı köşe, aynı gölge yok; 3 kademeli köşe ölçeği.
  - Okunmamış göstergesi için "sol renkli kenarlık" yok; nokta + yazı ağırlığı +
    saat rengi kullan. Tek başına renge güvenme: Android "birden fazla görsel ipucu
    kullan" diyor. [resmi]
    - https://developer.android.com/design/ui/mobile/guides/foundations/accessibility
  - Cam/bulanık yüzey yok. Telegram Android'de arayüz efektlerini "Güç Tasarrufu"
    altında kısma seçeneği sunuyor; yani efektin bir performans ve pil maliyeti var.
    [resmi + çıkarım]
    - https://telegram.org/blog/crafting-android-design-and-more
  - Süs sparkline ve "kahraman metrik" yok; büyük sayı yalnız gerçekten karar
    verdiriyorsa.
- Yazı ve içerik
  - BÜYÜK HARF aralıklı etiket, "01/02/03" numaralama (sıra yoksa), düğme metnine
    "→", "A · B · C" noktalı meta satırı yok.
  - Mono yazı yalnız gerçek kod/sipariş numarası için.
  - Emoji'yi ikon veya madde işareti yapma.
  - Jenerik metin yok ("Hoş geldin", "Gücünü keşfet"). Düğme tam olarak ne olacağını
    söylesin: "Kaydet", "Okundu". Boş durum ve hata bir açıklama ve yön versin; hata
    özür dilemesin. [resmi ilke]
    - https://raw.githubusercontent.com/anthropics/skills/main/skills/frontend-design/SKILL.md
- Hareket
  - Her şey zıplamasın: zıplama yalnız "kahraman" anlarda (ilk yakalanan mesaj,
    "Hepsi okundu" onayı).
  - Kullanıcı tetiklemeyen süs hareketi (her bölüme kayarak giriş) yok.
  - Modal çoğaltma yok: onay yerine geri alınabilir eylem ve "Geri al" snackbar'ı.
- Sunum
  - Mağaza ekran görüntülerinde 3D cam/eğik telefon maketi yok; gerçek arayüz + kısa
    fayda cümlesi. [tek kaynak]
    - https://www.apptweak.com/en/aso-blog/how-to-optimize-your-app-screenshots
  - Stok/3D illüstrasyon ve "jenerik insan" yok (Monzo bunu kendi yazısında açıkça
    reddediyor). Maskot ya özgün ya hiç.
  - Hiç özelleştirilmemiş varsayılan bileşen kitaplığı yok (sadece bir pazarlama
    yazısı söylüyor; mantıklı ama kanıt zayıf). [çıkarım]
    - https://vp0.com/blogs/why-does-my-ai-app-look-generic.md

### Dikkat: "anti-slop" da bir kalıp olabilir
- Anthropic'in listesindeki ilk iki kalıp (krem + kiremit + serif; koyu + tek parlak
  vurgu) eski "mor gradyan" kalıbından sonra yeni varsayılan olarak sayılıyor. Yani
  "koyu zemin + amber" veya "krem + terracotta" seçmek tek başına güvenli değil.
  [çıkarım: listenin nedenini yönerge söylemiyor]
  - https://raw.githubusercontent.com/anthropics/skills/main/skills/frontend-design/SKILL.md
- Bunu söyleyen bir GitHub aracı da var ("avoid-ai-design": "tasteful default'lar:
  krem + terracotta, mono chrome"). Sayfa bu oturumda 403 verdi; yalnız arama özeti
  görüldü. [doğrulanamadı]
  - https://github.com/funboy322/avoid-ai-design
- Ölçüt: kararı "güzel mi" ile değil, "bu ürünün işine bağlı mı" ile ver. Cash App
  yeşili parayı "yeniden tanımlamak" fikrine bağlı; Monzo mercanı bankacılık
  sektörünün bayat rengine karşı çıkıyor; Duolingo yeşili baykuşun renginden
  geliyor. Her biri bir gerekçeye bağlı. [blog + resmi]
  - https://design.cash.app/color
  - https://www.dezeen.com/2019/01/17/slack-logo-rebrand-pentagram/

### Sende'nin 5 konseptiyle karşılaştırma [çıkarım; docs/marka/konsept-N-belirtecler.json okundu]
- Bu liste kesin hüküm değil; kataloglar da görüş. Kurucu karar verir. Amaç, "risk
  nerede" sorusunu görünür yapmak.
- Konsept 1, "Gece Mürekkebi" (yakın-siyah #0B0C10 + kehribar #F0B240, Manrope +
  Inter)
  - Anthropic'in 2. kalıbı (yakın-siyah + tek parlak vurgu) ve 5. kalıptaki "tonlu
    yakın-siyah" ile örtüşüyor. Inter listelerde var.
  - Lehine: Material koyu tema yönergesi "büyük yüzeyler koyu, sınırlı vurgu" diyor;
    OLED. Yay 0.8/380 Material Expressive varsayılanı.
  - Önlem: açık tema da eşit kalitede olmalı; kehribar yalnız "yeni mesaj" için.
- Konsept 2, "Sıcak Esnaf" (krem #F6EEE1 + kiremit #B03F1B, Bricolage Grotesque + DM
  Sans + JetBrains Mono)
  - 1. kalıpla (krem + kiremit) güçlü örtüşme. Mono etiket de 5. kalıpta. Yay sönümü
    0.6: Expressive'in "fast spatial" sönümüyle aynı; satır genişlemesi için fazla
    zıplak.
  - Lehine: "esnaf dükkânı" sıcaklığı hedef kitleye özgü bir fikir.
- Konsept 3, "Keskin Sinyal" (grafit #0D0F11 + limon #C8F23A, Space Grotesk + Inter
  + mono etiket)
  - En yüksek risk: 2. kalıp (yakın-siyah + asit yeşil), 5. kalıp (mono etiket),
    cookbook'un adını andığı Space Grotesk, katalogdaki "koyu + neon" ve "mono
    teknik hava".
- Konsept 4, "Editoryal Masa" (kâğıt #F4EFE4 + zinober #C4361B, Fraunces +
  Instrument Sans + mono)
  - 1. ve 3. kalıpla örtüşüyor (krem + serif + vermilyon, ince çizgi). Lehine:
    "okunmamış mesaj = katlanmış sayfa köşesi" ürüne özgü bir fikir.
- Konsept 5, "Cesur Parça" (kırık beyaz #F6F2EA + kobalt #1E35C8 + mercan #FF6B4A,
  Sora + DM Sans)
  - Beş kalıbın hiçbirine doğrudan oturmuyor; gradyan/cam/mor yok. "Tek kahraman
    renk + somut şekil fikri" (iki balon parçası = tek S) Cash/Monzo/Duolingo
    desenine en yakın. Risk: mavi, genel SaaS rengi olabilir; ayırt edicilik testi
    şart.
- Ortak nokta: beşi de "S" harfi monogramı. Test: 48 px'te WhatsApp, Telegram ve
  Instagram ikonlarının yanında ayırt ediliyor mu; Android ana ekranında temalı (tek
  renk) ikon halinde okunuyor mu (bkz. Bölüm 7).

## 3. Mesaj / gelen kutusu / liste ekranı için en iyi örnekler

### İyi örnekler ve nedenleri
- Google Messages, Material 3 Expressive sürümü [haber + görsel]
  - Ana liste TEK kapsayıcıda; satır başına ayrı kart yok; avatarlar büyük, yoğunluk
    düşük; uygulama çubuğu ve durum çubuğu birleşik. Sohbet ekranında yazı alanı
    alta (başparmak bölgesi) hap şeklinde; ekleme menüsünde renkli ikonlar
    kaldırılıp tek tonlu ikon + hap kapsayıcı kullanılmış. Ekran ekran yayılıyor,
    her şey bir anda değişmiyor.
  - Neden iyi: tutarlı hizalama, tek renk dili, az çizgi. Sende'ye çeviri: grup =
    tek kapsayıcı, satırlar çizgisiz.
  - https://9to5google.com/2025/08/20/google-messages-homepage-redesign/
  - https://9to5google.com/2025/06/02/google-messages-material-3-expressive-chat/
- Material 3 liste kuralları [resmi]
  - Destek görseli ve ana metin her satırda aynı yerde; öğe konumu satırdan satıra
    değişmesin; kapsayıcı listede çizgi yerine boşluk; Android'de kaydırınca eylem:
    birincil eylem SONDA ve sağda-hizalı, tam kaydırma onu tetikler, gizli eylem
    için alternatif (örneğin "daha fazla" ikonu) şart.
  - https://m3.material.io/components/lists/guidelines
- Telegram sohbet klasörleri [resmi]
  - Klasörler "sohbet listesi dağınıklaşacak kadar uzayınca" arayüzde belirir;
    sekmeler arası kaydır; her klasörde sınırsız sabitleme. Şubat 2026 Android
    yenilemesi: alt çubuk (sohbetler, ayarlar, profil tek dokunuşla) ve "Güç
    Tasarrufu" altında efekt kontrolü.
  - Neden iyi: filtre, ihtiyaç doğunca görünüyor (aşamalı açıklama). Sende'ye
    çeviri: kanal balonları yalnız birden fazla kanalda mesaj varken görünsün.
  - https://telegram.org/blog/folders
  - https://telegram.org/blog/crafting-android-design-and-more
- Linear Inbox [resmi]
  - "Priority" sekmesi "dikkat isteyen" bildirimleri varsayılan olarak ayırır
    (kullanıcı filtreyi değiştirebilir); ertele (snooze), sil, okunmamışları başa
    al, hızlı arama; klavye ve sağ tık kısayolları.
  - Neden iyi: önce "dikkat isteyen" gelir, gerisi bir sekme arkada.
  - https://linear.app/docs/inbox
- Slack Activity 2.0 tasarım yazısı [resmi]
  - Temel içgörü: "hatırlama (recall) ile ayıklama (triage) farklıdır." İnsanlar
    dört davranış arasında gidip geliyor: Tut (keep), Taşı (move), Oku (read), Göz
    at (skim). Aynı kişi aceleyken göz atıyor, odaktayken tutuyor. "Dışarıdan basit,
    içeride güçlü." Etkileşim oranı %80; geçişte onboarding "isteğe bağlı değil".
  - Sende'ye çeviri: Sende bir ayıklama aracı. "Okundu", "Yanıtla", "Uygulamada aç",
    "Hepsi okundu" bu dört davranışı destekliyor mu diye test et.
  - https://slack.design/articles/no-small-task-evolving-the-way-millions-catch-up-in-slack/
- HEY "Screener" [resmi]
  - İlk kez yazan kişi için karar ekranı; karar özel, geri alınabilir (geçmiş ve 90
    gün öncesi mesajlar).
  - Sende'ye çeviri: kayıtsız numaradan ilk mesaj için hafif bir "tanımıyorum"
    grubu. (Android 15'te telefon numarası içeren sohbet adları "hassas" sayılıp
    gizlenebiliyor; bkz. 04 numaralı dosya.)
  - https://www.hey.com/features/the-screener/
- GitHub Mobile [resmi]: mobilin sayılan ilk işi "bildirimleri yönet, ayıkla ve
  temizle".
  - https://docs.github.com/en/get-started/using-github/github-mobile
- WhatsApp Business uygulaması [resmi]: küçük işletmenin zaten kullandığı araç;
  karşılama ve "uzaktayım" mesajları, ürün vitrini. Sende bunun yerine değil yanında
  duracak; aynı işi tekrar etme.
  - https://business.whatsapp.com/products/business-app
- Mobbin "Emails & Messages" ve "Empty State" desen sayfaları [liste]: WhatsApp,
  Spark Mail, Apple Mail, Gmail, Discord, OpenPhone, TextNow, Microsoft Teams gibi
  gerçek uygulamaların ekranları etiketli duruyor; üyelik olmadan yalnız adlar
  görülebildi.
  - https://mobbin.com/explore/mobile/screens/emails-messages
- Bird "yenilenmiş gelen kutusu" duyurusu [pazarlama; kanıtsız]: "mobile için
  sadeleştirilmiş arayüz, odak, arıtılmış renk paleti" diyor ama ölçüm vermiyor.
  Yalnız yön olarak not.
  - https://bird.com/en-us/changelog/streamlined-inbox-boosting-productivity-with-sleek-design

### Ortak anatomi [çıkarım; yukarıdaki kaynaklardan]
- Satırda sabit sütunlar: avatar (sol), ad (ağırlıklı) + saat (ikincil) aynı
  satırda, altında 1-2 satır önizleme, sağda okunmamış nokta veya sayı.
- Okunmamış = nokta/sayı + yazı ağırlığı + saat rengi. Tek başına renk değil.
- Grup = tek kapsayıcı, satırlar arasında boşluk. İç içe kart yok.
- Filtreler (kanal balonları) kalabalık olunca görünür; varsayılan görünüm "dikkat
  isteyen".
- Alt yarıda sık eylem (yazma/yanıt, filtre), üstte seyrek eylem (menü).
- Satır eylemi: kaydır (birincil eylem sonda) + görünür bir alternatif.

## 4. Tek elle kullanım ve başparmak ergonomisi

### Ölçülmüş veri
- Steven Hoober, 2 ay sonunda 1.333 gözlem (780'inde ekrana dokunuluyor), sokak,
  havalimanı, kafe, otobüs [araştırma, 2013]:
  - Tek el %49, "beşik" (bir elle tut, diğer elle dokun) %36, iki el %15.
  - Tek elde sağ başparmak %67, sol %33.
  - Tutuş statik değil: kişi birkaç saniyede bir tutuşunu değiştiriyor; kaydırırken
    tek el, yazarken iki el, sonra yine tek el.
  - https://www.uxmatters.com/mt/archives/2013/02/how-do-users-really-hold-mobile-devices.php
  - Aynı sayılar Hurff, Addy Osmani ve Smart Insights yazılarında tekrarlanıyor
    (ikincil).
    - https://www.scotthurff.com/posts/how-to-design-for-thumbs-in-the-era-of-huge-screens/
    - https://addyosmani.com/blog/touch-friendly-design/
- Hoober 2017 düzeltmesi [araştırma]:
  - Meşhur "başparmak süpürme" şeması için "iyi bilinen ama yanlış" diyor. Sahada
    Geri düğmesi, sağ üstte olsa bile ekrandaki en çok kullanılan düğme.
  - İnsanlar ekranın ORTASINA bakıp ortasına dokunmayı tercih ediyor; en isabetli
    yer orası. Hedef boyutu: ortada en az ~7 mm, köşede ~12 mm.
  - "İnsanlar tam hedefe dokunmaz"; boyutlar sapma payı için konur. Piksel temelli
    standartlar işe yaramaz çünkü fiziksel boyuta bağlı değil.
  - https://www.uxmatters.com/mt/archives/2017/03/design-for-fingers-touch-and-people-part-1.php
  - Güncel bir özet (2026) aynı sayılara kenarda ~9 mm ekliyor ve "birincil eylem
    merkezde, ikincil eylem üst/altta, köşede hassas dokunma gerektiren önemli şey
    olmasın" diyor. Tek ikincil kaynak; 9 mm doğrulanamadı. [tek kaynak]
    - https://www.monterail.com/blog/thumb-friendly-navigation-word-oriented-design
- Josh Clark: ekranın yalnızca yaklaşık üçte biri "zahmetsiz" bölge: altta, tutan
  elin karşı tarafında (sağ elle tutana sol-alt yay). Başparmak çoğu ekranı
  süpürebiliyor; doğruluk ve rahatlık aynı şey değil. [blog/kitap bölümü]
  - https://alistapart.com/article/how-we-hold-our-gadgets/
- NN/g: etkileşimli öğe en az 1 cm × 1 cm; MIT Touch Lab: parmak ucu 1,6-2 cm,
  başparmak temas alanı ortalama 2,5 cm. "Görme-dokunma asimetrisi": öğe okunacak
  kadar büyük ama rahat dokunulamayacak kadar küçük/sıkışık. Karşıt eylemler (Takip
  et / Kaldır) yan yana ve küçükse yanlış dokunma artıyor. [araştırma]
  - https://www.nngroup.com/articles/touch-target-size/
- Android: en az 48 dp (yaklaşık 7,6 mm); WCAG 2.2: AA için 24 × 24 CSS px veya
  yeterli boşluk; AAA için 44 × 44. [resmi]
  - https://support.google.com/accessibility/android/answer/7101858
  - https://www.w3.org/WAI/WCAG22/Understanding/target-size-minimum.html
  - https://www.w3.org/WAI/WCAG22/Understanding/target-size-enhanced.html
- Çevirme (1 dp = 1/160 inç) [hesap]: 7 mm ≈ 44 dp, 9 mm ≈ 57 dp, 12 mm ≈ 76 dp, 1
  cm ≈ 63 dp.
- Samsung One UI: ekranı üstte "görüntüleme alanı", altta "etkileşim alanı" diye
  ikiye bölüyor; büyük ekranlarda kontrolleri erişilebilir tutmak için bazı
  kontroller yana (kamera örneği) alınıyor. [resmi]
  - https://developer.samsung.com/one-ui/index.html
  - https://developer.samsung.com/one-ui/largescreen-and-foldable/large_screen_layout.html
- Google'ın Material 3 Expressive araştırması: e-posta örneğinde Gönder düğmesi
  büyütülüp klavyenin hemen üstüne alındı, ikincil renkle vurgulandı; eski tasarımda
  küçük düğme üst çubukta başka kontrollerin yanındaydı. Gezinme çubuğu "uygun
  erişim için" altta, 3-5 hedef. [resmi araştırma + resmi]
  - https://design.google/library/expressive-material-design-google-research
  - https://m3.material.io/components/navigation-bar/guidelines
- Küçük bir topluluk sesi: HN'de bir yorumcu Expressive'in düğmeleri başparmağa
  yaklaştırmasını "harika" buluyor; aynı yorum diğer eleştirileri de haklı sayıyor.
  [topluluk, tek yorum]
  - https://news.ycombinator.com/item?id=43975352
- Material 3 Expressive'de alt uygulama çubuğu (bottom app bar) artık önerilmiyor.
  Yerine "docked toolbar" (daha kısa, daha esnek) ve "floating toolbar" (yatay veya
  dikey, çok eylem alabilir, FAB ile eşlenebilir) geldi; gezinme çubuğuyla aynı anda
  gösterilmez. Eylemler alt yarıya iniyor. [resmi]
  - https://m3.material.io/components/toolbars/overview
- Öngörülü geri jesti (predictive back): kenar jest alanlarının altına dokunma jesti
  veya sürükleme hedefi koyma. Satırdan kaydırma eylemlerinin kenara yapışık
  başlamaması gerekir. [resmi]
  - https://developer.android.com/design/ui/mobile/guides/patterns/predictive-back

### Kurallar (Sende için) [kaynaklardan çıkarım]
- Tek bir "sıcak bölge" haritasına güvenme. Tutuş sürekli değişiyor ve %33 sol
  başparmak var. Sık eylem alt yarıda ve merkeze yakın; seyrek eylem (menü, dışa
  aktar, sil) üstte.
- Hedef boyutu konuma göre: merkezde en az 48 dp; kenar/köşede 56-76 dp veya geniş
  görünmez dokunma alanı.
- Karşıt veya yakın eylemleri (Okundu / Yanıtla / Uygulamada aç) alt alta değil yan
  yana koy; araya en az 8 dp boşluk ve her birine en az 48 dp yükseklik. Yanlış
  dokunmanın bedeli "okundu" gibi geri alınamaz eylemse "Geri al" ver.
- Sürekli kullanılan filtre (kanal balonları) üstte duruyorsa en zor erişilen yerde
  sık kullanılan bir kontrol var demektir. Seçenekler: alt yarıya almak, alt çubuk
  üstünde ince bir şerit yapmak veya en azından yüksekliği 48 dp'ye çıkarıp ekran
  başına yapışık bırakmak. Karar için 5 işletmeciyle dokunma kaydı al (Hoober'ın
  yöntemi: kaçırılan dokunuşları da kaydet).
- Yanıt/yazma alanı (varsa) ekranın altında; klavye açılınca üstünde kalsın.
- Kaydırma eylemi: M3 kuralı: birincil eylem SONDA, tam kaydırma onu tetikler, gizli
  eylemin görünür bir alternatifi olsun. Arapça (RTL) için "son" tarafı otomatik
  döner; Compose'da başlangıç/bitiş (start/end) kullan. [resmi + çıkarım]
  - https://m3.material.io/components/lists/guidelines
- Büyük ekran ve katlanabilir: pencere boyutu sınıfına göre liste-ayrıntı iki
  bölmeli düzen; kontroller yine alt/yan erişilebilir bölgede. [resmi]
  - https://developer.android.com/develop/ui/compose/layouts/adaptive/window-size-classes
  - https://m3.material.io/foundations/adaptive-design/overview
- Ölçüm: Roborazzi/UiAutomator ile 48 dp altı tıklanabilir öğeleri tara. Mevcut koda
  bakınca kanal balonu yüksekliği 40 dp (`ChannelBar.kt`); Material bileşeni mi
  yoksa düz yüzey mi olduğuna göre dokunma alanı 48 dp'ye çıkıyor olabilir,
  doğrulanmadı. [çıkarım]

## 5. Boş durum, onboarding ve izin açıklaması

### Boş durum
- NN/g'nin üç işlevi: sistem durumunu bildir, öğrenme ipucu ver, anahtar görev için
  doğrudan yol sun. Boş bırakmak güveni düşürür. Özellikle: veri YÜKLENİRKEN "kayıt
  yok" yazan ekran yanlış durum bildirir ve en zararlısıdır. [araştırma]
  - https://www.nngroup.com/articles/empty-state-interface-design/
  - https://www.nngroup.com/videos/empty-states-in-application-design-guidelines/
- SAP Fiori: boş durum ne olduğunu, neden görüldüğünü ve sonra ne yapılacağını
  söylemeli; "asla boş ya da olumsuz hissettirmemeli". [resmi]
  - https://www.sap.com/design-system/fiori-design-web/v1-96/foundations/best-practices/global-patterns/designing-for-empty-states
- Canva uygulama kılavuzu: başlık (ne boş), 1-2 cümlelik gövde, TEK birincil düğme;
  başlık ile gövde arası 8 px, gövde ile düğme arası 16 px. [resmi]
  - https://www.canva.dev/docs/apps/design-guidelines/empty-states
- Material 2 (arşiv): boş durum belgesi; bugün "Material 2 artık bakılmıyor" notu
  var. [resmi, eski]
  - https://m2.material.io/design/communication/empty-states.html
- Dribbble "empty state" etiketi ve Untitled UI örnekleri [görsel]: sakin, tek nötr
  ikon, başlık, açıklama, ikincil + birincil düğme ("Aramayı temizle" + "Yeni
  belge"). Çoğu etiket sonucu illüstrasyon seti satan paylaşım; uygulama ekranı
  değil.
  - https://dribbble.com/tags/empty_state
  - https://dribbble.com/shots/25639475-Empty-state-Untitled-UI
- Mobbin "Empty State" sayfası: 5.071 ekran sayıldığı yazıyor (listeden: Truecaller,
  Pinterest, Apple Wallet, Uber Eats, eBay vb.). [liste]
  - https://mobbin.com/explore/mobile/screens/empty-state
- İskelet ekran (skeleton): yalnız tam sayfa yüklemeleri için; algılanan bekleme
  süresini kısaltır. [araştırma]
  - https://www.nngroup.com/articles/skeleton-screens/
- [çıkarım] Sende için 6 durum:
  - 1) İlk açılış, kanal seçilmemiş: başlık + 1 cümle + tek düğme "Kanalları seç".
    (Bugün `NoChannelsChosen` böyle.)
  - 2) Kanal var, mesaj yok: "Dinleme açık. Yeni mesaj gelince burada görünür." +
    mesajın nasıl görüneceğinin küçük önizlemesi.
  - 3) Dinleme duraklamış veya izin kapalı: durum + tek eylem ("Bildirim erişimini
    aç"). Rakiplerin 1 numaralı şikâyeti bu (docs/MIMARI.md).
  - 4) Filtre sonucu boş: "Bu kanalda mesaj yok" + "Tümü'ne dön".
  - 5) Hepsi okundu: kısa, tek satır, süssüz.
  - 6) Yükleniyor: veri tabanı ilk değeri gelene kadar boş durum GÖSTERME; hiçbir
    şey ya da hafif iskelet.

### Onboarding
- Apple HIG: onboarding hızlı, eğlenceli ve isteğe bağlı olsun; etkileşimle öğret;
  tek akış yerine bağlama özgü ipuçları; gereksiz kurulumu ertele ve makul
  varsayılanlar ver; izin işin çalışması için şartsa onboarding içinde iste. [resmi]
  - https://developer.apple.com/tutorials/data/design/human-interface-guidelines/onboarding.json
- Apple HIG, açılış: "anında aç" ve "uygulama yeniden başlayınca önceki durumu geri
  yükle (listeyi kullanıcının son konumuna kaydır)". Sende için: açılışta kaldığın
  yere dön. [resmi]
  - https://developer.apple.com/design/human-interface-guidelines/launching
- NN/g: eğitim turları kullanıcıyı böler, görev başarısını artırmayabilir ve çabuk
  unutulur; bağlamsal yardım ("pull revelation") daha iyi. Mobilde kullanıcılar
  ipucu katmanlarını (coach mark) hızla kapatıyor ve dikkatle okumuyor. [araştırma]
  - https://www.nngroup.com/articles/onboarding-tutorials/
  - https://www.nngroup.com/articles/mobile-instructional-overlay/
- Aşamalı açıklama: nadir özellikleri ikinci katmana at. [araştırma]
  - https://www.nngroup.com/articles/progressive-disclosure/
- Behance onboarding analizi: ipuçları (tooltip) ve metin üzerinden; izin
  isteklerinde bile açıklayıcı metin. [blog]
  - https://goodux.appcues.com/blog/behance-user-onboarding
- Mobbin onboarding akışları: Airbnb, Revolut, Spotify, Headspace, Wise, Uber
  akışları etiketli; ortak: karşılama + hesap + izin + varsayılan tercih. [liste]
  - https://mobbin.com/explore/mobile/flows/onboarding
- Toplu örnek siteleri: UserOnboard teardown'ları (web uygulamaları, ClickUp,
  Zapier, Canva), Checklist Design (mobil için Onboarding, Chat, Push Notification
  Opt-in listeleri; içerik okunamadı), Refero (iOS/web ekran arama). [liste]
  - https://www.useronboard.com/
  - https://www.checklist.design/
  - https://refero.design/
- [çıkarım] Sende için en fazla 3 adım:
  - Adım 1: tek cümle ("Mesajların Sende.") + "Veri telefondan çıkmaz" + tek düğme.
    Slayt turu yok.
  - Adım 2: kanal seçimi; telefonda yüklü uygulamalardan önerilenler önceden
    işaretli (WhatsApp, WhatsApp Business, Instagram, Telegram...). Varsayılan makul
    olsun.
  - Adım 3: izin (aşağıda). İzin verilince mevcut bildirimlerin hemen düşmesi
    "anında fayda" olarak gösterilsin (kod zaten mevcut bildirimleri yeniden
    işliyor).

### İzin açıklaması
- Apple HIG, izin için özel ekran kuralları: TEK düğme ve sistem iletişim kutusunu
  açtığı net olsun; "İzin ver" gibi sistem düğmesini taklit eden ifade, teşvik,
  sahte istem ekranı ve sistem kutusunun üstüne işaret çizmek yasak/reddedilme
  nedeni. İstek cümlesi kısa, tam, etken ve özgül olsun. [resmi]
  - https://developer.apple.com/tutorials/data/design/human-interface-guidelines/privacy.json
- Material (Android izinleri): kritik ve açık izni ilk açılışta iste (mesajlaşma
  uygulaması için SMS örneği); ikincil izni bağlamda iste; belirsiz izinde önce
  eğit; "anında fayda" göster; yalnız gerekli izni iste; reddedilince geri bildirim
  ver. [resmi, eski ama ilke geçerli]
  - https://m2.material.io/design/platform-guidance/android-permissions.html
- Android belgeleri: gereksiz izin isteme, bağlamı açıkla, hassas erişimde gerekçe
  ekranı göster; açıklayıcı akışta İPTAL seçeneği HER ZAMAN olsun, kullanıcıyı
  engelleme; reddedilirse özelliği zarifçe kapat, uygulama kullanılabilir kalsın.
  Çalışma zamanı izninde kullanıcı bir izni ikinci kez reddederse sistem kutusu bir
  daha çıkmaz (kalıcı red). Bildirim erişimi özel bir ayar olduğu için bu kutu yok;
  ilke yine geçerli. [resmi + çıkarım]
  - https://developer.android.com/training/permissions/usage-notes
  - https://developer.android.com/privacy-and-security/minimize-permission-requests
  - https://developer.android.com/training/permissions/explaining-access
  - https://developer.android.com/training/permissions/requesting
- Google Play: kullanıcının beklemediği kişisel/hassas veri erişiminde UYGULAMA
  İÇİNDE "belirgin açıklama + açık onay" şart. Açıklama: uygulamanın içinde, normal
  kullanımda gösterilmeli (menü/ayara gitmeyi gerektirmemeli), hangi verinin
  alındığını ve nasıl kullanıldığını söylemeli, yalnız gizlilik politikasında
  olamaz, izin istenmeden ÖNCE gelmeli. Onay: "dokun-kabul et" gibi olumlu bir
  eylem; geri/ana ekran tuşuna basmak veya kendiliğinden kapanan ifade onay
  sayılmaz. Önerilen biçim: "[Uygulama] [veri türünü] [özellik] için [durumda]
  toplar/iletir/eşler/saklar." [resmi]
  - https://support.google.com/googleplay/android-developer/answer/11150561
  - https://support.google.com/googleplay/android-developer/answer/10144311
  - [çıkarım] Bildirim içeriği hassas veri sayılabilir; politika örnekleri başka
    izinleri adlandırıyor. Yazılı açıklamayı koymak ucuz sigorta.
- Gerçek uygulamalardan örnekler [görsel; Mobbin "Permission" sayfasından 4 ekran]:
  - Nextdoor: başlık ("Mahalle güncellemelerini kaçırmamak için bildirimleri aç"),
    altında gelecek bildirimlerin iskelet önizlemesi, tek koyu birincil düğme
    "Enable notifications", altında düz yazı "Not now".
  - Citizen: "Allow Notifications" + gerçek bir örnek bildirim kartı haritanın
    üstünde; tek beyaz hap düğme.
  - Walmart: kamera izni, canlı tarayıcı üstünde alt sayfa, tek illüstrasyon, 2
    cümle, tek "Continue".
  - Kart tarama ekranı olan bir finans uygulaması (adı görülmedi): kamera izni
    kapalıyken küçük bir iletişim kutusu: "Ayarlar" (birincil) ve "Manuel giriş"
    (alternatif yol).
  - https://mobbin.com/explore/mobile/screens/permission
- Dribbble "Allow camera access — Untitled UI" [görsel; macOS ayarları için]:
  açıklama + sistem isteminin önizlemesi + numaralı adım adım rehber (menü, Ayarlar,
  Gizlilik, Kamera, listede bul ve aç) + "Bildirim gelmedi mi?" yedek yol + tek mavi
  "Continue". Bu yapı, Sende'nin kısıtlanmış ayar rehberi için doğrudan model.
  - https://dribbble.com/shots/26512097-Allow-camera-access-Untitled-UI
  - https://dribbble.com/tags/notification
- [çıkarım] Sende izin akışı:
  - Başlık: ne yapacağı ("Bildirimlerini buraya toplayabilmem için Bildirim erişimi
    gerekir").
  - Altında Sende'nin gerçek bir mesaj kartının küçük önizlemesi (sonucu göster).
  - Güven cümlesi: "İnternet izni yok; mesajlar telefondan çıkmaz." (Sideload
    sürümünde manifestte doğrulanabilir; Play sürümü için yalnızca "kendi kodum ağa
    bir şey göndermez" denebilir. docs/MIMARI.md.)
  - Tek birincil düğme: "Bildirim erişimini aç" (sistem ayarlarına gittiğini
    söylesin). Altında sessiz "Şimdi değil".
  - Sistem ekranına gitmeden 1 cümle: "Listede Sende'yi bul ve aç."
  - Sideload kurulumda "kısıtlanmış ayar" adımları numaralı rehber olarak (bkz. 04
    numaralı dosya; `AccessGuideSheet` zaten var).
  - Dönüşte izin otomatik algılansın; "Hazır" + hemen düşen mevcut bildirimler
    (anında fayda).
  - Reddedilirse: neyin çalışmayacağını söyle ve alternatif yol ver ("Paylaş" ile
    mesaj ekleme; `Share intake` zaten var).

## 6. Animasyon ilkeleri

### Süre
- NN/g: çoğu animasyon 100-500 ms arasında; "sarsıcı olmayan en kısa süreyi bul";
  "animasyonların çok uzun olması, çok kısa olmasından çok daha yaygın". Basit geri
  bildirim (onay kutusu, anahtar) yaklaşık 100 ms; modal gibi büyük ekran değişimi
  200-300 ms; 500 ms'de "külfet" başlıyor; çoğu durumda 100-400 ms uygun.
  [araştırma]
  - https://www.nngroup.com/articles/animation-duration/
- Material'ın süre jetonları (eski sistem ama sayılar kullanışlı): kısa 50-200 ms
  (seçim kontrolleri 200 ms, standard easing), orta 250-400 ms (FAB'ın sayfaya
  açılması 400 ms), uzun 450-600 ms (kartın tam ekrana açılması 500 ms), ekstra uzun
  700-1000 ms (kullanıcı girdisi olmayan ortam geçişleri). [resmi]
  - https://m3.material.io/styles/motion/easing-and-duration/tokens-specs
- Sistem geri bildirimi 400 ms altında (Doherty eşiği); eylemden sonra 100 ms içinde
  cevap "anlık" hissettirir. [araştırma + resmi]
  - https://lawsofux.com/doherty-threshold/
  - https://web.dev/articles/rail
- Material'ın eski süre tablosu: giriş 400 ms, çıkış 200 ms, standart 250-300 ms.
  "Çıkış girişten kısa" ve "keskin kalkış, yumuşak iniş" ilkeleri hâlâ geçerli.
  [resmi]
  - https://m3.material.io/styles/motion/easing-and-duration
- Emil Kowalski: UI animasyonu çoğunlukla 300 ms altında; giriş/çıkışta ease-out
  (ease-in aynı sürede daha yavaş hissettirir); düğmeye basınca scale 0,97;
  scale(0)'dan başlama (0,9+ ile başla); klavyeden başlatılan eylemi animasyonlama.
  [blog, kişisel görüş]
  - https://emilkowal.ski/ui/great-animations
  - https://emilkowal.ski/ui/7-practical-animation-tips
  - https://animations.dev/
- Apple: geri bildirim animasyonu kısa ve kesin olsun; sık tekrar eden
  etkileşimlerde hareket ekleme; kullanıcı animasyonu beklemek zorunda kalmasın,
  iptal edebilsin; hareket isteğe bağlı olsun. [resmi]
  - https://developer.apple.com/tutorials/data/design/human-interface-guidelines/motion.json

### Yay (spring) tabanlı hareket
- Neden yay: yarıda kesilebilir, hızı korur, hareketler birbirine bağlanır. Compose
  varsayılan olarak yay kullanıyor ve "nesnenin mevcut hızını hesaba kattığı için
  kesilebilir". SwiftUI de varsayılanı yay yaptı; "yay = zıplama demek değil, sıfır
  zıplamalı yaylar iOS'ta her yerde". [resmi]
  - https://developer.android.com/develop/ui/compose/animation/quick-guide
  - https://developer.apple.com/videos/play/wwdc2023/10158/
  - https://developer.apple.com/videos/play/wwdc2018/803/
- Material 3 Expressive hareket fiziği: iki şema ve altı yay. [resmi + kaynak kod]
  - Şema: `standard` (işlevsel, minimal zıplama, "sık tekrar eden öğeler") ve
    `expressive` (zıplamalı, "öne çıkan/kahraman etkileşimler").
  - Tür: "spatial" (konum, boyut, dönme, köşe: aşmaya izin verir) ve "effects"
    (renk, opaklık: aşma YOK).
  - Hız: küçük bileşen (anahtar, düğme) "fast"; kısmen kaplayan (alt sayfa)
    "default"; tam ekran "slow".
  - https://m3.material.io/styles/motion/overview/how-it-works
  - https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/MotionScheme.kt
- Sayılar (sönüm oranı / sertlik) AndroidX kaynak kodundan okundu (Apache-2.0):
  - Standard: default spatial 0.9 / 700; fast spatial 0.9 / 1400; slow spatial 0.9 /
    300; effects hepsi sönüm 1.0 (default 1600, fast 3800, slow 800).
  - Expressive: default spatial 0.8 / 380; fast spatial 0.6 / 800; slow spatial 0.8
    / 200; effects aynı (1.0).
  - https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/StandardMotionTokens.kt
  - https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/ExpressiveMotionTokens.kt
  - https://raw.githubusercontent.com/material-components/material-components-android/master/docs/theming/Motion.md
- Compose sabitleri (aynı kaynakta okundu): `DampingRatioLowBouncy` = 0.75,
  `DampingRatioMediumBouncy` = 0.5, `DampingRatioNoBouncy` = 1.0;
  `StiffnessMediumLow` = 400, `StiffnessMedium` = 1500.
  - https://developer.android.com/reference/kotlin/androidx/compose/animation/core/Spring
- [çıkarım] Mevcut `Brand.kt`: genişleme yayı `LowBouncy (0.75)` + `MediumLow
  (400)`, süreler 260/220 ms. Konsept JSON'larında sönüm 0.6 ile 0.9 arası; konsept
  1'de süreler 420/320 ms. Öneri:
  - Sık tekrar eden satır hareketi (genişle/daralt, yerleşim): sönüm 0.9 / sertlik
    700 (standard default spatial). Konsept 2 (0.6) ve 5 (0.62) bunun için fazla
    zıplak.
  - Renk, opaklık: sönüm 1.0 / sertlik 1600.
  - Küçük öğeler (rozet, anahtar, düğme basışı): 0.9 / 1400.
  - Yalnız "kahraman an" (ilk yakalanan mesaj, "Hepsi okundu" onayı): expressive 0.8
    / 380.
  - Doğrudan koddan: `MotionScheme.standard().defaultSpatialSpec<Dp>()` gibi;
    `MotionScheme.standard()` ve `.expressive()` herkese açık (kaynak kodda okundu).
    Tema parametresi olarak bağlanışı kütüphane sürümüne göre değişir;
    [doğrulanamadı].
  - Süre: 400 ms üstü yok; satır genişlemesi görsel olarak ~250-350 ms'de oturmalı.

### Liste: ekleme, silme, sıra değişimi, genişleme
- Ekleme/silme/yeniden sıralama: `LazyColumn` + her öğeye sabit `key` +
  `Modifier.animateItem()`. Belgedeki özel örnek: giriş solma 250 ms, çıkış solma
  100 ms, yerleşim için yay. (Örnekte `MediumBouncy` var; üretim için 0.9 daha
  uygun, [çıkarım].) Her öğeye anahtar (key) vermek şart; yoksa animasyon doğru
  çalışmaz. [resmi]
  - https://developer.android.com/develop/ui/compose/lists
- Boyut değişimi (genişleme): `Modifier.animateContentSize()`, göster/gizle:
  `AnimatedVisibility`. [resmi]
  - https://developer.android.com/develop/ui/compose/animation/quick-guide
  - https://developer.android.com/develop/ui/compose/animation/customize
  - https://developer.android.com/develop/ui/compose/animation/value-based
- Kaydırarak okundu/sil: Compose belgesi, "e-postayı sil, görevi arşivle" gibi işler
  için hazır Material `SwipeToDismissBox`'ı öneriyor; özel yakalamalar için
  `anchoredDraggable`. [resmi]
  - https://developer.android.com/develop/ui/compose/touch-input/pointer-input/drag-swipe-fling
- Geri alınabilirlik: eylemli snackbar kullanıcı eylemine kadar ekranda kalmalı;
  eylemsiz olan platforma göre 4-10 sn'de kapanabilir; art arda snackbar tek tek
  gösterilir. [resmi]
  - https://m3.material.io/components/snackbar/guidelines
- Dokununca açılma: Things'te bir göreve dokununca görev "kart" olarak öne çıkıyor,
  liste arka plana soluyor; Android bildirimleri genişletmek için ok kullanıyor (04
  numaralı dosya). Dokunulan öğe yerine "atlayan" liste sorunu (I-22): sabit anahtar
  + kaydırma konumunu sabitleme. [blog + çıkarım]
  - https://www.macstories.net/reviews/things-3-beauty-and-delight-in-a-task-manager/
- Süre ve sıra önerisi [çıkarım]:
  - Okundu: satır 120-200 ms solar + kayar, boşluk yayla kapanır (placement spring);
    "Geri al" snackbar'ı hemen çıkar.
  - Yeni mesaj en üste gelince: yeni satır solarak girer (250 ms), diğerleri yayla
    aşağı kayar; kullanıcı kaydırmışsa konum sabit kalır ("yeni mesaj" çipi
    gösterilir).
  - Genişleme: yükseklik yayla, içerik opaklığı effects yayıyla; birden çok satırın
    aynı anda hareket etmesinden kaçın.

### Hareketi azaltma, haptik, performans
- WCAG 2.2 (AAA): "etkileşimden doğan hareket kapatılabilmeli"; vestibüler
  rahatsızlık (baş dönmesi, mide bulantısı) gerçek. Apple da "hareket isteğe bağlı
  olsun" diyor. Telegram Android'de efektleri "Güç Tasarrufu" altında kontrol
  ettiriyor. [resmi]
  - https://www.w3.org/WAI/WCAG22/Understanding/animation-from-interactions.html
  - https://web.dev/articles/prefers-reduced-motion
  - https://telegram.org/blog/crafting-android-design-and-more
  - [doğrulanamadı] Compose yayları sistem "animasyonları kaldır" ayarına otomatik
    uyuyor mu? Cihazda sınanmalı. Güvenli yol: uygulama içi "Hareketi azalt"
    seçeneği + sistem ayarını okuyup animasyonu kapatma.
- Haptik: Android ilkeleri: kısa "tık" geri bildirimi 10-20 ms; sık olaylar
  (kaydırma) çok hafif, önemli olaylar (gönder, yenile) anahtar değiştirmeden güçlü;
  uygulama genelinde tutarlı; eski tek atımlık titreşimlerden kaçın; mümkünse
  `HapticFeedbackConstants` kullan (işlev odaklı). [resmi]
  - https://developer.android.com/develop/ui/views/haptics/haptics-principles
  - https://developer.android.com/develop/ui/views/haptics/haptic-feedback
- Performans: 60 kare/sn = kare başına 16 ms (uygulamaya ~10 ms kalır); animasyonu
  düzen değişikliği yerine dönüşüm ve opaklıkla yap (web kuralı; Compose karşılığı
  `graphicsLayer`, [çıkarım]). [resmi]
  - https://web.dev/articles/rail
  - https://web.dev/articles/animations-guide
- Önceden sınanmış hareket dilleri (ilham): Cash App: üç davranış (Keşfet:
  gir/ölçekle/aç; Deneyim: karıştır/kaydır; Değiş tokuş: döndür/çevir) ve üç ilke
  (Amaçlı, Zahmetsiz, Organik); tipografide kayarak giriş/kesme/sıralı/vurgu.
  Airbnb: dört ilkeden biri "Konuşkan: hareket ürüne can verir ve kullanıcıyla
  anlaşılır biçimde iletişim kurar". [resmi + blog]
  - https://design.cash.app/motion
  - https://www.designprinciplesftw.com/collections/airbnbs-design-principles
- Hareket arşivleri: 60fps.design (2.110 ekran kaydı, 494 uygulama), Rive, Lottie.
  Sende için fazla: ek bağımlılık + APK boyutu; gerçek ihtiyaç doğarsa düşün.
  [liste]
  - https://60fps.design/
  - https://rive.app/
  - https://airbnb.io/lottie/
- NN/g mikro etkileşim: tetikleyici + geri bildirim çifti; sistem durumunu, hata
  önlemeyi ve markayı taşır. Her mikro etkileşim tek amaçlı olmalı. [araştırma]
  - https://www.nngroup.com/articles/microinteractions/

## 7. Marka sistemi çıktısı nasıl düzenlenir

### Profesyonellerin süreç adımları (kaynaklı)
- 0) Marka gerilimini bul, logoyla başlama. Bir ajansın "bağımsız Revolut konsepti"
  yazısında ilk soru "markanın neyi iletmesi gerektiği". Revolut'un kendi çalışması
  değil; bağımsız bir ajans konsepti. [tek kaynak]
  - https://news.designrush.com/revolut-brand-refresh-lessons-it-monks
- 1) Denetim + çalıştay + 3-5 yön + tek seçim + tüm marka kodlarına yayma. Monzo'nun
  anlatımı: "görsel marka kodlarını denetledik; şirketten insanlarla yaratıcı
  çalıştaylar ve görüşmeler; birkaç yön geliştirip değerlendirdik; mercan ağırlıklı
  yola karar verdik; sonra tüm marka kodlarına yaydık". Sıra: renk, ikon, tipografi,
  sanat yönetimi (fotoğraf), illüstrasyon, "abartılmış UI". [resmi]
  - https://monzo.com/blog/weve-had-a-little-makeover
- 2) Anlatı (çatı fikir) önce, görsel sonra. Duolingo (Johnson Banks, 2019): önce
  "Duolingo neyi mümkün kılıyor" anlatısı ve mesajı; çalıştaylar ve personel
  "expo"su ile "Everyone can Duolingo" çatısı; mevcut görsel öğeler (baykuş,
  çekirdek renk, illüstrasyon stili) "genelde çalışıyordu", eksik olan uygulama
  DIŞINDA nasıl kullanılacağının kılavuzuydu. Sonuç: özel yazı tipi (Feather Bold;
  "her teknoloji şirketi gibi nötr yazı kullanmak yerine" logotipi yeniden çizip tüm
  yazı ailesine genişlettiler), logo yerleşimleri, gözden geçirilmiş renkler,
  illüstrasyon kılavuzları ve daha net bir ses tonu. [blog]
  - https://www.johnsonbanks.co.uk/templates/work/duolingo
  - https://creativebloq.com/news/feather-bold
- 3) "Sistem yok, doğaçlama var" tespiti. Slack (Pentagram, 2019): Bierut ekibi
  mevcut varlıklara bakınca marka "tamamen tutarsız" çıktı; "nasıl evrileceği
  düşünülmemişti". 11 tonluk renk 4'e indi; logo yeniden kuruldu. [blog]
  - https://itsnicethat.com/news/michael-bierut-pentagram-slack-rebrand-graphic-design-170119
  - https://www.dezeen.com/2019/01/17/slack-logo-rebrand-pentagram/
- 4) Genişleyince yeniden dengeleme. Headspace (2024): ürün meditasyondan ruh
  sağlığına genişleyince iç ekip + Italic Studio + Colophon (özel yazı tipi) ile
  marka güncellendi; turuncu korundu, destekleyici palet, çeşitli yüz ifadeleriyle
  yeni illüstrasyon, daha çok fotoğraf. [blog]
  - https://itsnicethat.com/articles/italic-studio-headspace-graphic-design-project-250424
  - https://www.printmag.com/?p=767405
  - https://designcompass.org/en/2024/04/30/headspace/
- 4b) Fikir aşamasına zaman ayır, kavramları gerçek kanallarda sına. Cash App
  "Evergreen" (Buck, 2023-24): "zamanın büyük bölümünü fikir, kavram ve kompozisyona
  ayırdık; hemen tasarıma dalmak çoğu zaman modaya bağlı, eskiyen iş üretir"; amaç
  "anında tanınır ama sonsuz uyarlanabilir" sistem ve "rekabette erimeyen,
  minimalist bir marka denizinde kaybolmayan" bir iş. Teslimler: tasarım stratejisi
  ve amaç belgeleri, kaynak dosyalarıyla 3D illüstrasyon kitaplığı, 3D marka
  kılavuzu; paralelde pazar araştırma kolu: kavramlar TikTok, Instagram, Google ve
  YouTube'da denendi, pazarlama varlıkları kendi görsel dilini aldı. [blog, ajans
  anlatımı]
  - https://www.buck.co/work/cash-app-visual-identity
- 4c) "Yeniden marka" değil "enerji katma" da bir seçenek. Monzo'nun topluluk
  yazısı: "bu bir yeniden marka veya yeniden konumlandırma değil"; "çok kapsamlı
  keşif ve konsept aşamaları"; ikincil renkler "erişilebilirliği en üst düzeye
  çıkaracak şekilde" seçildi. [resmi]
  - https://community.monzo.com/t/we-ve-given-our-visual-brand-a-refresh-q-a-w-jesse-senior-brand-designer/139681
- 5) Marka kılavuzunun iskeleti: Cash App'in kılavuzu üç katman:
  - Foundations: Logo, Color, Typography, Motion, Iconography, Voice & Tone.
  - Expressions: Card, Graphic UI.
  - Resources: Tokens, Components, Documentation, Logo download, Type download,
    Press kit.
  - Tipografi sayfası ayrıca "Everyday usage / Experimental usage / Typesetting /
    Incorrect usage / Languages" bölümlerine ayrılıyor: "yanlış kullanım" ve
    "diller" kılavuzun parçası. [resmi]
  - https://design.cash.app/
  - https://design.cash.app/typography
  - https://design.cash.app/color
- 6) Küçük ve orta şirket kılavuzları da kısa tutuyor: Linear'ın marka sayfası: adın
  yazımı ("Linear" tek kelime, büyük L), "varlıkların etrafında bol boşluk bırak",
  kelime işareti için tek renk tercihi, işaret (logomark) ve indirilebilir
  varlıklar, izinsiz değiştirme yasağı. Mailchimp'in içerik kılavuzu: Yazım
  hedefleri ve ilkeleri, Ses ve ton, Erişilebilirlik için yazma, Çeviri için yazma,
  Kelime listesi. Discord, Wise ve Slack Design siteleri de indirilebilir varlık ve
  tasarım günlüğü sunuyor (Discord ve Wise sayfalarının içeriği ayrıntılı
  okunamadı). [resmi]
  - https://linear.app/brand
  - https://discord.com/branding
  - https://styleguide.mailchimp.com/
  - https://wise.design/
  - https://slack.design/
- 7) Süreç çerçevesi: Alina Wheeler'ın "Designing Brand Identity" kitabı beş faz
  tanımlıyor (araştırma, strateji, kimlik tasarımı, temas noktaları, varlıkları
  yönetme). Beş fazın adları arama özetinden; yayıncı sayfaları yalnız "beş fazlı
  süreç" diyor. [tek kaynak; faz adları doğrulanamadı]
  - https://campusstore.miamioh.edu/designing-brand-identity-essential-guide/bk/9781118099209
- 8) Yaşayan sistem: Linear 2026: tasarım sistemi sürekli "budanıyor"; bileşen
  kitaplığı değişirken ekipler çalışmaya devam ediyor (özellik bayrağı arkasında
  kademeli geçiş, tek tıkla eski/yeni karşılaştırma, geliştirici çubuğunda renk
  aracı). [resmi]
  - https://linear.app/now/behind-the-latest-design-refresh

### Sende için çıktı klasörü (öneri) [çıkarım]
- docs/marka/ altında numaralı klasörler (kılavuz okunur sırada):
  - 00-strateji: çatı fikir, hedef kitle, 3 marka ilkesi, "ne değiliz" listesi.
  - 01-logo: ana işaret, yatay kilit (işaret + yazı), tek renk, ters renk, koruma
    alanı, en küçük boyut, yanlış kullanım.
  - 02-renk: kahraman renk, nötr ölçek (12 basamak), durum renkleri, kontrast
    tablosu (metin 4.5:1, metin dışı 3:1), koyu ve açık tema eşlemesi.
  - 03-tipografi: iki rol (başlık, işlev), M3 ölçeğine eşleme, Türkçe harf testi (İ
    ı Ğ ğ Ş ş Ç ç Ö ö Ü ü), 200% ölçek.
  - 04-ikon: arayüz ikon ailesi (tek ağırlık/boyut), uygulama ikonu kuralları.
  - 05-illüstrasyon ve görsel dil: boş durum, onboarding, mağaza; "jenerik
    insan/stok yok" kuralı.
  - 06-hareket: yay jetonları (standard/expressive eşleme), süre sınırları,
    "hareketi azalt" davranışı, haptik tablosu.
  - 07-ses-ve-haptik: ses için kanıt bulunamadı (aşağıya bak); haptik için Android
    ilkeleri.
  - 08-ses-tonu: yazım ilkeleri, düğme/hata/boş durum kalıpları (Türkçe +
    İngilizce), yasaklı kalıplar.
  - 09-mağaza-ve-reklam: Play simgesi, öne çıkan grafik, ekran görüntüsü şablonları,
    açıklama metinleri.
  - 10-jetonlar: tek JSON (renk, köşe, hareket, boşluk) -> `Brand.kt`; Sende'de
    `konsept-N-belirtecler.json` zaten bu fikre bağlı.
  - 11-kullanım: yapılacak/yapılmayacak sayfası, sürüm günlüğü (ne zaman neyi
    budadık).
- Tema girdisini azalt: Linear 3 girdiden (taban, vurgu, kontrast) tüm temayı
  üretiyor (Bölüm 1). Sende için karşılığı: koyu/açık/yüksek kontrast üç temayı tek
  kaynaktan üretmek. [çıkarım]
- Jeton aktarımı: W3C Design Tokens Community Group biçimi + Style Dictionary
  (JSON'dan platform çıktısı üretir). Renk şeması üretimi için Material Theme
  Builder ve Material Color Utilities (Apache-2.0). [resmi]
  - https://www.designtokens.org/
  - https://styledictionary.com/
  - https://material-foundation.github.io/material-theme-builder/
  - https://raw.githubusercontent.com/material-foundation/material-color-utilities/main/README.md
- Uygulama ikonu (Android): tüm katmanlar 108 × 108 dp; logo en az 48 × 48 dp ve en
  çok 66 × 66 dp (iç 66 dp güvenli bölge, maskeden kırpılmaz); dış 18 dp maske ve
  paralaks efekti için ayrılmış; kenarlar temiz, maske veya gölge yok; tek katmanlı
  "monochrome" sürüm tema ikonları için. Android 16 QPR 2'den itibaren sistem, kendi
  tek renkli katmanını sağlamayan uygulamalar için ikonu otomatik temalıyor. [resmi]
  - https://developer.android.com/develop/ui/views/launch/icon_design_adaptive
  - https://proandroiddev.com/android-adaptive-themed-icons-guide-8e690263f7aa
- Google Play görselleri [resmi]:
  - Simge: 512 × 512 px, 32 bit PNG (alfa kanallı), sRGB, en çok 1024 KB; tam kare
    ver, maskeyi (köşe yarıçapı simge boyutunun %30'u) ve gölgeyi Play uygular,
    kendin gölge koyma; logoyu "keyline" ızgarasına yerleştir, çizim (illüstrasyon)
    ise tam kadraj olabilir.
  - https://developer.android.com/distribute/google-play/resources/icon-design-specifications
  - Öne çıkan grafik: 1024 × 500 px, JPEG veya 24 bit PNG (alfasız); simgeye
    benzeyen belirgin marka kullanma (çakışma olur); ince ayrıntılardan kaçın; odak
    merkezde, kırpılma bölgelerinde önemli öğe yok.
  - Ekran görüntüsü: yayın için toplam en az 2; öneri ve ana sayfa biçimlerinde yer
    almak için en az 4 adet, en az 1080 px, dikeyde 9:16 (en az 1080 × 1920); JPEG
    veya 24 bit PNG (alfasız), kenar 320-3840 px.
  - https://support.google.com/googleplay/android-developer/answer/9866151
- Mağaza ekran görüntüsü pratiği [blog; tek kaynak]: ilk 3 ekran en çok etkiler,
  kısa fayda cümleleri (3-5 kelime), gerçek arayüz, küçük boyutta okunabilirlik; A/B
  test. Google Play'de arama sonuçlarında yalnız simge göründüğünden ilk sayfa
  içeriği önem kazanıyor.
  - https://www.apptweak.com/en/aso-blog/how-to-optimize-your-app-screenshots
- Ses (sonic) kimliği: bu araştırmada güvenilir bir uygulama-UI ses tasarımı kaynağı
  BULUNAMADI (aşağıda "Denenen yollar"). Öneri: ilk sürümde marka sesi yok; yalnız
  haptik. [doğrulanamadı]
- Facebook/Instagram reklam görselleri (I-05): bu kolun kapsamı dışında kaldı;
  araştırılmadı.

## 8. Türkçe ve Türkiye'ye özgü notlar

- Türkçe büyük/küçük harf: `i` ile `ı` ve büyük `İ` ile `I` ayrı harf. .NET'te
  klasik hata anlatılıyor; Kotlin'de `String.uppercase()` / `Char.uppercase()`
  "invariant locale" kullanır ("i" -> "I"). Türkçe için `uppercase(Locale("tr"))`
  veya Compose `Locale.current`. Avatar baş harfi, BÜYÜK HARF etiketler ve arama
  normalizasyonu için geçerli. [resmi + blog]
  - https://haacked.com/archive/2012/07/05/turkish-i-problem-and-why-you-should-care.aspx/
  - https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/uppercase.html
- Yazı tipi seçerken Latin Extended (latin-ext) alt kümesini iste ve gerçek ekranda
  çiz: Ş ş Ğ ğ İ ı Ç ç Ö ö Ü ü. [topluluk, eski; çıkarım]
  - https://www.cryoutcreations.eu/forums/t/missing-turkish-characters-s-c-g-o-u-etc
  - https://fonts.google.com/knowledge
- Yerli örnek: BiP (Turkcell). Teknoblog'un 29 Eylül 2026 haberi: BiP iPhone
  menülerine "Liquid Glass" görünümünü getirdi. Haber, bunu iPhone uygulamasındaki
  tasarım çalışmasının ilk adımı diye aktarıyor. Android için bilgi yok. [haber]
  - https://www.teknoblog.com/bip-iphone-menulerini-liquid-glass-ile-yeniledi/
  - https://www.teknoblog.com/turkcell-bip-mobil-uygulama-incelemesi/
- Getir iOS uygulaması için bir okul eleştirisi (Pratt, 2022): net düzen, ürünleri
  öne çıkarma; adres ekleme "+" ikonu yönlendirici. Eski ve öğrenci eleştirisi.
  [blog, tek kaynak]
  - https://ixd.prattsi.org/2022/02/design-critique-getir-ios-app/
- Bulunamayanlar: Getir, Trendyol, Papara, Hepsiburada gibi Türk uygulamaların resmi
  marka/tasarım sistemi yazıları bulunamadı. Trendyol'un açık kaynak tasarım sistemi
  "Baklava" olduğu arama özetinde geçti; GitHub sayfası 403 verdi, doğrulanamadı.
  [doğrulanamadı]
- Sektör bağlamı: küçük işletme zaten WhatsApp Business uygulamasını kullanıyor
  (karşılama, "uzaktayım" mesajı, ürün vitrini). Sende'nin tasarım dili "yanında
  duran, sakin, hızlı ayıklama" olmalı; WhatsApp Business'ı taklit etmemeli.
  [çıkarım]
  - https://business.whatsapp.com/products/business-app

## Sende için çıkarımlar

Bu bölüm öneridir; karar kurucunun. Her madde: ne yapalım / ne yapmayalım, neden.
Ayrıntı ve kaynaklar ilgili bölümde.

### Hemen yapılabilecekler (düşük maliyet, yüksek getiri)
- Satır hareketini "standard" yaya çek. Neden: sık tekrar eden işlevsel hareket; M3
  `standard` şeması bunun için; `Brand.kt` bugün 0.75 / 400 kullanıyor. (Bölüm 6)
  - Genişleme ve yerleşim: sönüm 0.9, sertlik 700. Renk/opaklık: 1.0 / 1600. Küçük
    öğe: 0.9 / 1400.
  - Expressive (0.8 / 380) yalnız iki "kahraman an" için: ilk yakalanan mesaj ve
    "Hepsi okundu".
  - 5 konseptin hareket değerlerinden sönüm 0.6 olan (konsept 2) ve 0.62 olan
    (konsept 5) satır hareketi için fazla zıplak.
- "Kartın içinde kart"ı kaldır: grup tek kapsayıcı, içinde çizgisiz satırlar,
  aralarda boşluk. Neden: slop işareti; Material liste kuralı ("kapsayıcı listede
  boşluk"); Google Messages bunu yaptı; kurucunun "grup tek kutu" isteğiyle (I-29)
  uyumlu. (Bölüm 1, 2, 3)
- Okunmamış göstergesi: nokta/sayı + yazı ağırlığı + saat rengi. Sol renkli kenarlık
  yok. Neden: Android "birden fazla görsel ipucu" diyor; tek taraflı renkli kenarlık
  yaygın slop işareti. (Bölüm 2)
- İzin ekranını yeniden kur: sonuç önizlemesi, tek birincil düğme, sessiz "Şimdi
  değil", 1 cümlelik yönlendirme, reddedilince alternatif ("Paylaş" ile ekleme).
  Play için uygulama içinde "hangi veri, ne için" açıklaması ve olumlu bir onay
  eylemi. Neden: Play politikası, Apple HIG, Mobbin örnekleri. (Bölüm 5)
- Boş durumları 6 duruma çıkar ve veri yüklenirken "mesaj yok" GÖSTERME. Neden:
  NN/g, yanlış sistem durumu güveni en çok zedeleyen şey. (Bölüm 5)
- Türkçe harf güvenliği: avatar baş harfi ve BÜYÜK HARF etiketlerde
  `uppercase(Locale("tr"))` veya `Locale.current`; Roborazzi'ye "İ ı Ğ ğ Ş ş Ç ç Ö ö
  Ü ü" ekran görüntüsü testi. Neden: Kotlin `uppercase()` invariant;
  `ui/common/Images.kt` 93-94. satır `uppercase()` çağırıyor. (Bölüm 8; kod okundu,
  değiştirilmedi)
- Dokunma hedefi taraması: 48 dp altındaki tıklanabilirleri bul (kanal balonu 40 dp
  görünüyor); köşe/kenarda 56-76 dp. Neden: Android 48 dp, Hoober köşede ~12 mm.
  (Bölüm 4)
- Kenar jesti çakışması: satır kaydırma eylemlerini ekran kenarına yapıştırma;
  öngörülü geri jesti (predictive back) kenar alanını kullanıyor. Neden: Android
  belgesi. (Bölüm 4)
- Test matrisine ekle: yazı ölçeği %200, dar ekran, Arapça (RTL), koyu ve açık tema.
  Neden: Apple en az %200 büyütmeyi, Android gövdede en az 12 sp'yi, M3 "son" tarafı
  RTL'de ters çevirmeyi söylüyor.

### Arayüz kararları
- Kanal balonlarının yerini ölç: üstte mi altta mı? 5 işletmeciyle dokunma kaydı al
  (kaçırılan dokunuşlar dahil). Neden: Hoober "sahada üst köşedeki düğmeler de çok
  kullanılıyor" diyor, ama Google'ın araştırması ve Samsung kontrolleri alta
  çekiyor; kesin hüküm yok. (Bölüm 4)
- Balonlar yalnız ≥2 kanalda mesaj varken görünsün (Telegram klasörleri: liste
  dağınıklaşınca belirir). Neden: aşamalı açıklama. (Bölüm 3)
- Varsayılan görünüm "dikkat isteyen" (okunmamış/yanıt bekleyen), gerisi bir dokunuş
  arkada (Linear Priority, Slack "recall değil triage"). (Bölüm 3)
- Açılışta son kaydırma konumuna dön (Apple HIG: önceki durumu geri yükle). (Bölüm
  5)
- Yoğunluk ayarı ekle (Rahat / Sıkı). Neden: Material 3 Expressive'in boşluk
  eleştirisi HN'de yoğun; günde yüzlerce bildirimi olan işletmeci için sıkı mod.
  [topluluk, örneklem]
- Satır eylemi: kaydır (birincil eylem SONDA, tam kaydırma tetikler) + görünür
  alternatif + "Geri al" snackbar'ı. Neden: M3 liste ve snackbar kuralları. (Bölüm
  3, 6)
- Hareketi azalt: sistem ayarını oku + uygulama içi seçenek. Neden: WCAG 2.3.3, HIG;
  Compose yaylarının sistem ayarına uyumu doğrulanamadı. (Bölüm 6)
- Haptik: kısa "tık" (10-20 ms), yalnız anlamlı eylemde (okundu, hepsi okundu);
  işlev odaklı `HapticFeedbackConstants`. (Bölüm 6)
- Arayüz ikonları tek aile, tek ağırlık, çizgi tipi; durum geçişi için doluluk
  ekseni (okunmamış dolu, okunmuş çizgili). Neden: içerik ikonları zaten renkli,
  arayüz rekabet etmesin. (Bölüm 1)

### Marka kararı için
- Konsept seçimini "ürüne bağlı fikir" kriteriyle yap: seçilen konseptin neden bu
  ürün olduğunu tek cümleyle kanıtla. Katalog risk haritası (Bölüm 2): konsept 3 en
  riskli; 2 ve 4 "krem + kiremit/serif" kalıbına yakın; 1 "koyu + tek parlak vurgu"
  kalıbına yakın; 5 en az örtüşen, ama mavi için ayırt edicilik testi şart. Bu bir
  hüküm değil, görünür kılınmış risk.
- Seçilen konseptin "kabuğunu" (renk + yazı) özgünleştir: yerel/sektörel bir
  referans ara (esnaf tabelası boyası, veresiye defteri, kasa fişi vb.). Neden:
  ayırt edici örnekler bir nesneye veya kültürel köke yaslanıyor (Monzo mercan kart;
  Miznaa'nın Arapça "bulut" hikâyesi). [öneri]
- Vurgu rengi yalnız "yeni mesaj" ve birincil eylem anlamında. Neden: Linear
  "kazanmadığın dikkat" ilkesi; Apple "marka rengini kontrollerde en aza indir,
  okunmamış rozeti ve birincil eylem için kullan" diyor; Cash App tek rengi bilinçli
  kullanıyor; içerikte zaten renk gürültüsü var.
- Temayı 3 girdiden üret: taban renk, vurgu, kontrast (Linear 98 değişkeni 3'e
  indirdi, LCH). Koyu, açık ve yüksek kontrast tema tek kaynaktan çıkar;
  erişilebilirlik bedava gelir. (Bölüm 1, 7) [çıkarım]
- Yazı: iki rol (başlık + işlev), Türkçe harf testi, 200% ölçek. Inter, Space
  Grotesk, Fraunces, Bricolage Grotesque gibi seçimleri "tell listesi" ışığında
  bilinçli gerekçelendir. Yazı tiplerinin lisansı (genelde OFL) bu araştırmada
  doğrulanmadı; seçimden sonra kontrol et. (Bölüm 1, 2)
- Ayırt edicilik testi: 48 px ve tek renkli (temalı) ikon halinde WhatsApp,
  Telegram, Instagram ikonlarının yanında. Beş konseptin beşi de "S" monogramı.
  (Bölüm 2, 7)
- Marka çıktısını numaralı klasörlerle ve tek JSON'dan `Brand.kt`'ye düzenle;
  "yanlış kullanım" sayfasını unutma (Cash App'te var). (Bölüm 7)
- Uygulama ikonu: 108 dp katman, 66 dp güvenli bölge, tek katmanlı monochrome sürüm
  (temalı ikon). Play: 512 × 512 simge, 1024 × 500 öne çıkan grafik, en az 4 adet
  1080 × 1920 dikey ekran görüntüsü. (Bölüm 7)
- Mağaza ekran görüntüleri gerçek arayüz + kısa fayda cümlesi (3-5 kelime), ilk üçü
  en önemli; 3D cam/eğik telefon maketi yok. [tek kaynak: AppTweak]
- Kavramları gerçek kanallarda sına (Cash App/Buck: TikTok, Instagram, Google,
  YouTube). Kurucunun Facebook/Instagram reklamları aynı zamanda ucuz bir marka
  testi olabilir. [çıkarım]

### Yapma listesi (kısa)
- Mor/indigo, gradyan metin ve gradyan zemin, cam/bulanık yüzey, neon parlama,
  ikon-üstte üç eş kart, kartın içinde kart, her şeye aynı köşe ve gölge, sol renkli
  kenarlık, emoji ikon, BÜYÜK HARF aralıklı etiket, "→" ekli düğme, her yerde
  zıplama, modal çoğaltma. (Bölüm 2'deki liste)
- Material 3 Expressive'in her oyuncağını alma. Neden: tepkiler karışık (Android
  Authority'de okuyucu anketi çoğunlukla olumlu ama itirazlar sert; HN'de eleştiri
  yoğun; Google'ın "Achieve" örnek uygulamasına tasarım camiasından sert tepki).
  Google, kullanıcı geri bildirimiyle bulanıklığı kapatma seçeneği getirdiğini
  söylüyor (podcast açıklaması). Ürünün işi ayıklama; seçici al: tonal yüzey, büyük
  hedef, alt eylem, tek kapsayıcı. [topluluk + haber]
- "Her şey altta olsun" takıntısı yok. Neden: Hoober 2017 sahada merkezin en
  isabetli olduğunu söylüyor; alt yarı + merkez, üstte seyrek eylem.
- Slayt turu şeklinde onboarding yok; en çok 3 adım.
- iOS Liquid Glass taklidi yok. Neden: Android'in kendi dili var; okunabilirlik
  riski (NN/g bulgusunu aktaran tek kaynak: Monterail). [tek kaynak]
- Cash/Monzo/Duolingo'nun renk veya yazısını kopyalama; yalnız yöntemi al: tek
  kahraman renk, iki yazı rolü, tek somut fikir, "yanlış kullanım" sayfası.

### Ölç ve doğrula
- Cihazda doğrula (bu ortamda cihaz yok): Compose yaylarının "animasyonları kaldır"
  uyumu, kenar jest çakışması, kanal balonu gerçek dokunma alanı, düşük donanımda
  satır animasyonu akıcılığı.
- 5 işletmeciyle 20 dakikalık test: (a) ilk 3 dakikada izin akışını tamamlıyor mu,
  (b) "Okundu" yanlış dokunma sayısı, (c) üst/alt filtre dokunma kaydı, (d) "Hangi
  satır yeni?" sorusuna cevap süresi.
- Seçilen marka sonrası slop kontrol listesini (Bölüm 2) Roborazzi ekran görüntüsü
  incelemesinin parçası yap.

## Denenen ve çalışmayan yollar

- Behance
  - curl 403; Playwright (Chromium) 3 denemede "Loading..." /
    `ERR_TOO_MANY_RETRIES`; WebFetch 403.
  - Çözüm: okuyucu proxy (r.jina.ai) ile arama sonuçları ve proje sayfaları okundu;
    küçük resimler mir-s3-cdn-cf.behance.net adresinden indirilip incelendi. Tam
    vaka çalışması metinleri çoğunlukla görsel; yalnız birkaçı okundu.
- Dribbble
  - curl 202 (AWS WAF "challenge"); Playwright `ERR_TOO_MANY_RETRIES`; WebFetch boş
    sayfa.
  - Çözüm: aynı okuyucu proxy; etiket sayfaları ve paylaşım sayfaları okundu, 10
    küçük resim indirilip incelendi.
- Awwwards: doğrudan bağlantı sıfırlandı; okuyucu proxy "Mobile & Apps" listesini
  verdi (web sitesi odaklı, uygulama içi arayüz değil).
- Page Flows, Screenlane, Siteinspire, Lapa Ninja: Cloudflare/Vercel doğrulaması
  ("Just a moment"), doğrudan ve okuyucu ile açılamadı. UX Archive: DNS engeli.
  Pttrns: yalnız ana sayfa (giriş istiyor). Bu siteler için üçüncü yol aranmadı.
- Mobbin: ayrıntılı ekranlar üyelik istiyor. Desen sayfalarının metni okundu; küçük
  resimler tembel yüklendiği için Playwright'ta 2 denemede 4 izin ekranı görüldü.
- Cash App marka alt sayfaları: Playwright'ta "Something went wrong" ve
  `ERR_TOO_MANY_RETRIES`; okuyucu proxy ile okundu.
- design.duolingo.com alt sayfaları: doğrudan ve okuyucuyla yalnız site altbilgisi
  döndü (tek sayfa uygulaması). Duolingo için ajans ve haber yazıları kullanıldı.
- Airbnb "Building a Visual Language" (airbnb.design): 404. Airbnb için tasarım
  ilkelerini derleyen bağımsız sayfa kullanıldı.
- Headspace design.headspace.com: bağlantı hatası. Headspace için haber yazıları
  kullanıldı.
- Revolut: resmi marka/tasarım yazısı bulunamadı (design.revolut.com bir Google
  girişine yönlendiriyor; topluluk sayfası bot kontrolü verdi). Yalnız bağımsız bir
  ajans konsepti okundu; resmî çalışma gibi sunulmadı.
- Superhuman bölünmüş gelen kutusu yazısı (404) ve Intercom gelen kutusu tasarım
  yazısı (genel pazarlama sayfası): bırakıldı. NN/g "bottom navigation" ve "emoji
  icons": 404.
- Bazı Android tasarım sayfaları (renk, tipografi, bildirim desenleri,
  swipe-to-dismiss, haptics-compose, material3-expressive): 404; yerine çalışan
  sayfalar kullanıldı.
- xylight.dev "Everything Wrong with Material 3 Expressive": alan adı çözülemedi
  (DNS). HN yorum başlığı ve Android Authority yazıları kullanıldı.
- GitHub sayfaları (avoid-ai-design, telegram-book, Trendyol/baklava,
  material-color-utilities): 403. Kural gereği kimlik kullanılmadı. Yalnız
  raw.githubusercontent.com'dan gerekli dosyalar okundu.
- Reddit: doğrudan 403. Arşiv API'siyle (arctic-shift) tek başlık okundu;
  r/androiddev, r/userexperience ve r/Android aramaları hata veya boş sonuç verdi.
- X.com: Adam Wathan'ın özür gönderisine doğrudan gidilmedi (oturum gerektiriyor);
  ikincil kaynaklarla yetinildi.
- Marka sesi (sonic branding): tek arama yapıldı, uygulama-UI ses tasarımı için
  güvenilir bir kaynak çıkmadı; ikinci yol denenmedi.
- Checklist Design alt sayfaları: okuyucu yalnız menüyü döndürdü, madde içeriği (JS)
  okunamadı.
- WebSearch: sonuçların bir kısmı SEO/pazarlama içeriği; yalnız adresleri açılıp
  okunanlar kaynak olarak kullanıldı.

## Doğrulanamayanlar ve güvenilirlik notları

- Adam Wathan'ın "bg-indigo-500" özrü: üç ikincil kaynakta var, birincil X gönderisi
  okunmadı. Reddit başlığı ve YouTube video başlığı (Syntax) bu hikâyenin
  dolaştığını gösteriyor, içeriğini kanıtlamıyor. [doğrulanamadı (birincil)]
- "Hacker News'te 1.590 üründen yarısından azı slop işaretsiz" ve "renkli sol
  kenarlık = uzun tire kadar güvenilir işaret": bir blogdan aktarma; denetimin
  kendisi bulunamadı. [doğrulanamadı]
- Google'ın "4 kat daha hızlı" ve "%87 tercih" iddiaları kendi araştırmasında;
  bağımsız tekrar yok. HN yorumcuları yöntemi eleştiriyor. [tek kaynak, ilgili
  taraf]
- Monterail'in "kenarda 9 mm" ve "Apple iOS 27'de okunabilirlik düzeltmesi yaptı"
  cümleleri tek ikincil kaynakta. ("Google alt uygulama çubuğunu önermiyor" cümlesi
  Material toolbar sayfasında doğrulandı.) [tek kaynak]
- Hoober'ın 2013 verisi 1.333 gözlem; yaş ve ekran boyutu bugünden farklı (büyük
  ekran dönemi öncesi). 2017 düzeltmesi bu eksiği kısmen kapatıyor.
- Dribbble/Behance incelemem yalnız küçük resimlerden; proje metinleri çoğunlukla
  okunmadı. Öznel yorumlar ([görsel]) benim okumam.
- "Premium his" pazarlama bloglarındaki yüzdeler ("%40 daha çok etkileşim")
  kaynaksız. [doğrulanamadı]
- Wheeler'ın beş faz adları (araştırma, strateji, kimlik tasarımı, temas noktaları,
  varlık yönetimi) arama özetinden; yayıncı sayfaları yalnız "beş fazlı süreç"
  diyor. [doğrulanamadı]
- Lisanslar: Material Symbols (Apache-2.0) ve Lucide (ISC) sayfalarında yazıyor.
  Phosphor, Radix Colors ve önerilen yazı tiplerinin (Manrope, Inter, Bricolage
  Grotesque, Space Grotesk, Fraunces, Sora, DM Sans, Instrument Sans, JetBrains
  Mono) lisansı bu araştırmada doğrulanmadı.
- BiP'in ana menüsünü sadeleştirip "Keşfet"i "Daha Fazla"ya taşıdığı bilgisi yalnız
  bir arama özetinde (haber sayfası açılamadı). [doğrulanamadı]
- Compose: `MaterialTheme`'e `motionScheme` parametresi olarak bağlama ve
  "animasyonları kaldır" uyumu doğrulanamadı; `MotionScheme.standard()/expressive()`
  ve spec fonksiyonları kaynak kodda okundu.
- Cihaz testi yok: bu ortamda telefon veya emülatör yok; ergonomi ve hareket
  önerileri kâğıt üstünde.
- Slop katalogları (pixelslop, 925Studios vb.) görüş ve pazarlama içeriği; "tell"
  listeleri bir kural değil, risk göstergesi. Anthropic'in kendi yönergesi bile
  "hepsi bazı briflerde meşrudur" diyor.
- Türk uygulamaların (Getir, Trendyol, Papara, Hepsiburada) resmi tasarım yazıları
  bulunamadı.
- Facebook/Instagram reklam görselleri bu kolun kapsamı dışında kaldı.

## Referans listesi (100'den fazla, bağlantı + tek cümle neden)

Her madde: bağlantı, altında "[işaret] neden" cümlesi (telefonda yana kaymasın diye
ikinci satıra kırıldı). İşaretlerin anlamı "Nasıl araştırdım" bölümünde.

### Material 3 ve Android (resmi, kaynak kod)

- https://m3.material.io/blog/building-with-m3-expressive
  [resmi] Expressive'in kapsamı: 14 bileşen, yay tabanlı hareket, 35 şekil ve "M4
  değil, M3'ün evrimi" vurgusu.
- https://design.google/library/expressive-material-design-google-research
  [araştırma] 46 çalışma ve 18.000 katılımcı iddiasının kaynağı; Gönder düğmesinin
  büyütülüp klavye üstüne alındığı örnek.
- https://m3.material.io/blog/testing-material-3
  [resmi] Yeni tasarımın kullanıcıda ve marka sesinde nasıl algılandığını ölçme
  yöntemi.
- https://m3.material.io/styles/motion/overview/how-it-works
  [resmi] Standard ve expressive şemalar, spatial ve effects yayları, üç hız;
  standard "işlevsel ürünler" için.
- https://m3.material.io/styles/motion/overview/specs
  [resmi] Yayların web eğrisine çevirisi ve eski easing sisteminin geride kaldığı
  notu.
- https://m3.material.io/styles/motion/easing-and-duration
  [resmi] Eski sürenin tablosu: giriş 400, çıkış 200 ms; "keskin kalkış, yumuşak
  iniş".
- https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/MotionScheme.kt
  [kaynak kod] standard() sık tekrar eden işlevsel öğeler, expressive() kahraman
  etkileşimler için; altı spec fonksiyonu.
- https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/ExpressiveMotionTokens.kt
  [kaynak kod] Expressive yay sayıları (default spatial 0.8 / 380, fast 0.6 / 800).
- https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/StandardMotionTokens.kt
  [kaynak kod] Standard yay sayıları (default spatial 0.9 / 700, fast 0.9 / 1400).
- https://raw.githubusercontent.com/material-components/material-components-android/master/docs/theming/Motion.md
  [resmi] Views tarafında aynı altı yay ve "şekil için spatial, renk için effects"
  kuralı.
- https://developer.android.com/reference/kotlin/androidx/compose/animation/core/Spring
  [resmi] DampingRatioLowBouncy 0.75 ve StiffnessMediumLow 400 gibi sabitlerin
  sayısal değerleri.
- https://m3.material.io/styles/shape/overview-principles
  [resmi] Köşe ölçeğine 20/32/48 dp ve "full" eklendi; şekil "semantik değil, çok
  yönlü".
- https://m3.material.io/styles/color/overview
  [göz gezdirildi] Renk sisteminin girişi: roller ve yüzey tonlarıyla hiyerarşi.
- https://m3.material.io/styles/color/roles
  [göz gezdirildi] Renk rolleri; Brand.kt jetonlarını bu rollere eşlemek için.
- https://m3.material.io/styles/color/dynamic-color/overview
  [resmi] Dinamik renk kaynağı duvar kâğıdı veya içerik; tek vurgulu bir marka için
  seçmeli kullanım gerekir.
- https://m3.material.io/styles/typography/overview
  [resmi] Tek ölçek: 15 temel + 15 vurgulu stil, "hiçbir ürün hepsini kullanmaz".
- https://m3.material.io/styles/typography/type-scale-tokens
  [göz gezdirildi] 30 yazı stilinin jeton adları.
- https://m3.material.io/styles/icons/overview
  [resmi] Material Symbols: ağırlık, doluluk, optik boyut ve derece eksenli değişken
  font.
- https://m3.material.io/components/lists/guidelines
  [resmi] Kapsayıcı listede boşluk, aynı konumda ana metin, kaydırma eyleminde
  birincil eylem sonda.
- https://m3.material.io/components/lists/overview
  [göz gezdirildi] Liste bileşeninin türleri ve kullanım özeti.
- https://m3.material.io/components/navigation-bar/guidelines
  [resmi] Gezinme çubuğu: 3-5 hedef, altta, ikon + etiket.
- https://m3.material.io/components/snackbar/guidelines
  [resmi] Eylemli snackbar kalıcı, eylemsiz 4-10 sn; art arda olanlar tek tek.
- https://m3.material.io/components/chips/overview
  [resmi] Çip türleri (yardım, filtre, girdi, öneri); kanal filtresi için filtre
  çipi.
- https://m3.material.io/components/floating-action-button/overview
  [resmi] FAB "ekrandaki en yaygın veya önemli eylem" için; üç boyut.
- https://m3.material.io/components/toolbars/overview
  [resmi] Expressive'de alt uygulama çubuğu önerilmiyor; docked ve floating toolbar,
  FAB ile eşleme.
- https://m3.material.io/styles/motion/easing-and-duration/tokens-specs
  [resmi] Eski sistemin süre jetonları (50-1000 ms) ve easing eğrileri (cubic-bezier
  sayıları).
- https://m3.material.io/components/buttons/overview
  [resmi] Beş renk seçeneği ve beş boyut: düğme hiyerarşisi kurmak için.
- https://m3.material.io/components/search/overview
  [göz gezdirildi] Arama çubuğu ve öneri listesi bileşeni.
- https://m3.material.io/components/badges/overview
  [göz gezdirildi] Okunmamış sayı rozetinin bileşen belgesi.
- https://m3.material.io/components/bottom-sheets/overview
  [göz gezdirildi] Alt sayfa: ikincil içerik altta (kanal seçici için
  karşılaştırma).
- https://m3.material.io/components/dialogs/overview
  [göz gezdirildi] Diyalog kullanımı; modal çoğalmasına karşı kıyas.
- https://m3.material.io/components/progress-indicators/overview
  [göz gezdirildi] Aynı süreç için aynı yapılandırma kuralı.
- https://m3.material.io/foundations/adaptive-design/overview
  [resmi] Pencere sınıflarına göre düzen uyarlama (compact'tan extra-large'a).
- https://m3.material.io/foundations/content-design/overview
  [göz gezdirildi] İçerik tasarımı ilkelerinin girişi.
- https://m3.material.io/foundations/accessible-design/overview
  [resmi] "Varsayılan olarak erişilebilirlik" çekirdek değeri.
- https://m3.material.io/foundations/interaction/states/overview
  [göz gezdirildi] Etkileşim durumlarının (basılı, odak vb.) özeti.
- https://m2.material.io/design/color/dark-theme.html
  [resmi] Koyu tema: #121212 yüzey, doygunluğu azaltılmış vurgu, beyaz metinde
  15.8:1.
- https://m2.material.io/design/platform-guidance/android-permissions.html
  [resmi] İzin kalıpları: kritik izin başta, ikincil izin bağlamda, reddedilince
  geri bildirim.
- https://m2.material.io/design/communication/empty-states.html
  [resmi] Material 2 boş durum belgesi (artık bakılmıyor notuyla).
- https://developer.android.com/develop/ui/compose/designsystems/material3
  [resmi] Compose'da Material 3 ve M3 Expressive uygulaması.
- https://developer.android.com/design/ui/mobile/guides/foundations/accessibility
  [resmi] Gövde en az 12 sp, metin 4.5:1, metin dışı 3:1, birden fazla görsel ipucu.
- https://developer.android.com/design/ui/mobile/guides/layout-and-content/layout-basics
  [resmi] Tutarlı boşluk ve görünüm başına çok eylem koymama.
- https://developer.android.com/design/ui/mobile/guides/patterns/predictive-back
  [resmi] Geri jesti animasyonları ve kenar jest alanlarının altına dokunma hedefi
  koymama uyarısı.
- https://developer.android.com/design/ui/mobile/guides/foundations/system-bars
  [göz gezdirildi] Sistem çubukları ve kenardan kenara düzen.
- https://developer.android.com/develop/ui/compose/system/system-bars
  [göz gezdirildi] Sistem çubuğu koruması (kenardan kenara düzende).
- https://developer.android.com/develop/ui/compose/animation/introduction
  [göz gezdirildi] Compose animasyon API'lerine giriş.
- https://developer.android.com/develop/ui/compose/animation/customize
  [resmi] spring(dampingRatio, stiffness) varsayılanları ve animationSpec
  özelleştirme.
- https://developer.android.com/develop/ui/compose/animation/quick-guide
  [resmi] animateContentSize, AnimatedVisibility ve "yaylar kesilebilir" notu.
- https://developer.android.com/develop/ui/compose/animation/value-based
  [göz gezdirildi] Değer tabanlı animasyonlar (animate*AsState).
- https://developer.android.com/develop/ui/compose/lists
  [resmi] animateItem + sabit key; özel fade ve yerleşim yayı örneği.
- https://developer.android.com/develop/ui/compose/touch-input/pointer-input/drag-swipe-fling
  [resmi] SwipeToDismissBox ne zaman, anchoredDraggable ne zaman.
- https://developer.android.com/develop/ui/views/animations/spring-animation
  [göz gezdirildi] View sistemi yay animasyonu sabitleri.
- https://developer.android.com/develop/ui/views/haptics/haptics-principles
  [resmi] Haptik ilkeleri: 10-20 ms tık, sık olayda hafif, tutarlılık.
- https://developer.android.com/develop/ui/views/haptics/haptic-feedback
  [resmi] İşlev odaklı HapticFeedbackConstants kullanımı.
- https://developer.android.com/distribute/google-play/resources/icon-design-specifications
  [resmi] Play simgesi: 512 px tam kare, %30 köşe yarıçapı Play tarafından, gölge
  yok, keyline ızgarası.
- https://developer.android.com/develop/ui/compose/animation/choose-api
  [göz gezdirildi] Hangi Compose animasyon API'sinin ne zaman seçileceği.
- https://developer.android.com/guide/topics/ui/accessibility/apps
  [göz gezdirildi] Uygulamayı daha erişilebilir yapma rehberi (Android).
- https://developer.android.com/develop/ui/views/launch/icon_design_adaptive
  [resmi] Adaptive icon: 108 dp katman, 66 dp güvenli bölge, monochrome katman,
  Android 16 QPR 2 notu.
- https://developer.android.com/about/versions/13/features
  [göz gezdirildi] Android 13 özellikleri: temalı ikonlar ve bildirim izni.
- https://developer.android.com/develop/ui/views/notifications/notification-permission
  [göz gezdirildi] Android 13 bildirim çalışma zamanı izni.
- https://developer.android.com/develop/ui/views/notifications/conversations
  [resmi] Konuşma bildirimlerinin sistem düzeni (Sende satırının sistemdeki
  karşılığı).
- https://developer.android.com/training/permissions/requesting
  [resmi] Çalışma zamanı izni isteme akışı.
- https://developer.android.com/training/permissions/usage-notes
  [resmi] İzin en iyi uygulamaları: bağlam, açıklama, gereksiz izinden kaçınma.
- https://developer.android.com/privacy-and-security/minimize-permission-requests
  [resmi] İzin isteklerini en aza indirme ve gizlilik dostu alternatifler.
- https://developer.android.com/training/permissions/explaining-access
  [resmi] Hassas erişim için gerekçe ekranı gösterme.
- https://developer.android.com/develop/ui/compose/layouts/adaptive/window-size-classes
  [resmi] Pencere boyutu sınıflarıyla uyarlanabilir düzen.
- https://developer.android.com/develop/ui/compose/accessibility/key-steps
  [göz gezdirildi] Compose'da erişilebilirlik adımları.
- https://developer.android.com/develop/ui/views/theming/darktheme
  [göz gezdirildi] Android koyu tema uygulaması.
- https://developer.android.com/develop/ui/views/launch/splash-screen
  [göz gezdirildi] Açılış ekranı API'si (marka ilk izlenimi).
- https://support.google.com/accessibility/android/answer/7101858
  [resmi] Dokunma hedefi en az 48 dp.
- https://support.google.com/googleplay/android-developer/answer/9866151
  [resmi] Play simge, öne çıkan grafik ve ekran görüntüsü ölçüleri ve kuralları.
- https://support.google.com/googleplay/android-developer/answer/11150561
  [resmi] Belirgin açıklama ve onay için en iyi uygulamalar.
- https://support.google.com/googleplay/android-developer/answer/10144311
  [resmi] Kullanıcı verisi politikası: uygulama içi açıklama, olumlu onay, örnek
  cümle biçimi.
- https://proandroiddev.com/android-adaptive-themed-icons-guide-8e690263f7aa
  [blog] Temalı (tek renkli) adaptive ikon hazırlama rehberi.

### Apple ve diğer platform belgeleri

- https://developer.apple.com/design/human-interface-guidelines/
  [resmi] Apple tasarım yönergelerinin ana sayfası (konu listesi).
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/layout.json
  [resmi] İçeriği önem sırasına diz; kontrolleri içerikten ayır; güvenli alanlara
  uy.
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/typography.json
  [göz gezdirildi] Yazı boyutu, ağırlık ve hiyerarşi yönergeleri (ayrıntılı
  okunmadı).
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/motion.json
  [resmi] Hareket amaçlı, kısa, iptal edilebilir ve isteğe bağlı olmalı.
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/onboarding.json
  [resmi] Onboarding hızlı, eğlenceli, isteğe bağlı; etkileşimle öğret; makul
  varsayılanlar.
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/accessibility.json
  [resmi] En az %200 büyütme, 11 pt alt sınır, kontrast ve hedef boyutu.
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/color.json
  [resmi] Aynı rengi farklı anlamlarda kullanma; renk ve yarı saydamlığın komşu
  renklere etkisi.
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/lists-and-tables.json
  [resmi] Metni listede göster, seçimde uygun geri bildirim ver, öğe metnini kısa
  tut.
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/notifications.json
  [resmi] Doğrudan iletişim bildirimleri uygulama ikonu yerine avatar ve grup adı
  gösterir; aynı şey için çoklu bildirimden kaçın.
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/privacy.json
  [resmi] İzin ekranında tek düğme, yasak taklit istemler, kısa ve özgül istek
  cümlesi.
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/loading.json
  [resmi] "Hemen bir şey göster": içerik yüklenirken yer tutucu; bekleme boş ekranla
  geçmesin.
- https://developer.apple.com/tutorials/data/design/human-interface-guidelines/feedback.json
  [resmi] Geri bildirimi renk, metin, ses ve haptikle çoklu ver; durumu ilgili
  öğenin yanında göster.
- https://developer.apple.com/design/human-interface-guidelines/branding
  [resmi] Marka rengini ölçülü kullan (birincil eylem, okunmamış rozeti); başlıkta
  özel yazı, gövdede sistem yazısı.
- https://developer.apple.com/design/human-interface-guidelines/launching
  [resmi] Anında aç, önceki durumu geri yükle, açılış ekranını onboarding başına
  koy.
- https://developer.apple.com/design/human-interface-guidelines/app-icons
  [göz gezdirildi] Uygulama ikonu yönergeleri.
- https://developer.apple.com/design/human-interface-guidelines/materials
  [göz gezdirildi] Malzeme (cam, bulanıklık) yönergeleri.
- https://developer.apple.com/design/human-interface-guidelines/sf-symbols
  [göz gezdirildi] SF Symbols sistem ikon dili.
- https://www.apple.com/newsroom/2025/06/apple-introduces-a-delightful-and-elegant-new-software-design/
  [haber] Apple'ın 2025 yazılım tasarımı (Liquid Glass) tanıtım duyurusu.
- https://developer.apple.com/videos/play/wwdc2025/219/
  [resmi] Liquid Glass; kontrollerin köşelere eş merkezli oturması.
- https://developer.apple.com/videos/play/wwdc2018/803/
  [resmi] Akıcı arayüz: tepki, yönlendirme ve kesinti, lastik bant etkisi.
- https://developer.apple.com/videos/play/wwdc2023/10158/
  [resmi] Yaylar: hız sürekliliği, kesintide yumuşaklık, zıplamasız yaylar.

### Marka sistemi ve şirket tasarım yazıları

- https://linear.app/now/how-we-redesigned-the-linear-ui
  [resmi] 2024 yenilemesi: kenar çubuğu, sekmeler, başlıklar; az kişiyle haftalar
  içinde tam yenileme.
- https://linear.app/now/behind-the-latest-design-refresh
  [resmi] 2026 yenilemesi: "kazanmadığın dikkat için yarışma", "yapı hissedilsin
  görülmesin", daha sıcak gri.
- https://linear.app/now/invisible-details
  [resmi] Bağlamsal menülerde alt menüye çapraz hareketi destekleyen "görünmez
  ayrıntı" örneği.
- https://linear.app/brand
  [resmi] Kısa marka kılavuzu: ad yazımı, bol boşluk, tek renk tercihi,
  indirilebilir varlıklar.
- https://linear.app/docs/inbox
  [resmi] Gelen kutusunda Priority sekmesi, ertele, klavye kısayolları ve
  görüntüleme seçenekleri.
- https://design.cash.app/
  [resmi] Cash App kılavuzunun üç katmanı: Foundations, Expressions, Resources.
- https://design.cash.app/color
  [resmi] Tek kahraman renk (Cash Green #00E013) ve "sel olarak değil, bilinçle"
  kullanım ilkesi.
- https://design.cash.app/motion
  [resmi] Üç hareket davranışı (Keşfet, Deneyim, Değiş tokuş) ve üç ilke (Amaçlı,
  Zahmetsiz, Organik).
- https://design.cash.app/typography
  [resmi] Tipografi bölümleri: günlük kullanım, deneysel kullanım, yanlış kullanım,
  diller.
- https://www.buck.co/work/cash-app-visual-identity
  [blog] Cash App Evergreen: fikir aşamasına zaman, 3D illüstrasyon kitaplığı,
  kanallarda kavram testi.
- https://www.johnsonbanks.co.uk/templates/work/duolingo
  [blog] Duolingo yeniden markalama süreci: anlatı, çalıştay, uygulama dışı kullanım
  kılavuzu, özel yazı.
- https://creativebloq.com/news/feather-bold
  [blog] Duolingo'nun özel yazısı Feather Bold: nötr yazı yerine logotipi yeniden
  çizmek.
- https://www.canny-creative.com/brand-breakdown/brand/duolingo/
  [blog] Duolingo renk ve yazı iş bölümü: Feather Green, Eel, Feather Bold (küçük
  harf) ve DIN Next Rounded.
- https://itsnicethat.com/articles/italic-studio-headspace-graphic-design-project-250424
  [blog] Headspace 2024 markalaması: turuncu korundu, destekleyici palet, yeni
  illüstrasyon ve özel yazı.
- https://www.printmag.com/?p=767405
  [blog] Headspace'in özel yazısı oyuncul sesten klinik sese geçebildiği için
  seçildi.
- https://designcompass.org/en/2024/04/30/headspace/
  [blog] Headspace illüstrasyon ve fotoğraf ayrımı: soyut duygular çizimle, gerçek
  hizmet fotoğrafla.
- https://monzo.com/blog/weve-had-a-little-makeover
  [resmi] Monzo: denetim, çalıştay, yönler, mercan ağırlıklı yol; iki yazı rolü;
  jenerik illüstrasyondan kaçış.
- https://community.monzo.com/t/we-ve-given-our-visual-brand-a-refresh-q-a-w-jesse-senior-brand-designer/139681
  [resmi] Monzo markasının "yeniden marka değil, enerji katma" olduğunu anlatan
  topluluk yazısı.
- https://itsnicethat.com/news/michael-bierut-pentagram-slack-rebrand-graphic-design-170119
  [blog] Slack markası "doğaçlama ve tutarsız" bulundu; Bierut'un yeniden kurma
  gerekçesi.
- https://www.dezeen.com/2019/01/17/slack-logo-rebrand-pentagram/
  [blog] Slack: 11 tonluk palet 4 ana renge indi; patlıcan moru zemin olarak kaldı.
- https://slack.design/
  [resmi] Slack Design günlüğü: tasarım kararlarının yazıya dökülmüş hali.
- https://slack.design/articles/no-small-task-evolving-the-way-millions-catch-up-in-slack/
  [resmi] Activity 2.0: "hatırlama değil ayıklama", Keep/Move/Read/Skim
  davranışları.
- https://slack.design/articles/should-we-make-it-a-preference-on-customization-defaults-and-accessibility/
  [göz gezdirildi] Slack'te "tercih mi olsun" sorusu: özelleştirme, varsayılan ve
  erişilebilirlik.
- https://www.designprinciplesftw.com/collections/airbnbs-design-principles
  [blog] Airbnb'nin dört ilkesi: Birleşik, Evrensel, İkonik, Konuşkan (hareket).
- https://stanforddaily.com/2022/10/23/the-browser-company-looks-to-reimagine-the-browser/
  [haber] Arc'ın çıkış sorusu: "Tarayıcınız sizi en son ne zaman güldürdü?"
- https://www.macstories.net/reviews/things-3-beauty-and-delight-in-a-task-manager/
  [blog] Things 3: Magic Plus düğmesi ve görevin "kart" olarak öne çıkması.
- https://www.notion.com/blog/introducing-notion-calendar
  [resmi] Cron'un Notion Calendar olması; ürün duyurusu, tasarım ayrıntısı
  içermiyor.
- https://stripe.com/blog/accessible-color-systems
  [resmi] Stripe'ın algısal renk uzayıyla erişilebilir renk sistemi kurması.
- https://styleguide.mailchimp.com/
  [resmi] Mailchimp içerik kılavuzu: ses ve ton, erişilebilirlik ve çeviri için
  yazma.
- https://discord.com/branding
  [göz gezdirildi] Discord marka kılavuzu ve indirilebilir varlıklar sayfası.
- https://wise.design/
  [liste] Wise tasarım sitesi: marka uygulamalarının görsel arşivi (metin yok).
- https://atlassian.design/foundations/color
  [resmi] Atlassian'da renk "markayı ayırt eder, deneyimi tutarlı kılar"; doygun
  renk anlam taşır.
- https://primer.style/design/foundations/color/overview
  [göz gezdirildi] GitHub Primer renk kullanım ilkeleri.
- https://carbondesignsystem.com/patterns/empty-states-pattern/
  [resmi] IBM Carbon: boş durum "veri yok" durumlarının deseni.
- https://polaris.shopify.com/foundations/design/empty-state
  [göz gezdirildi] Shopify Polaris boş durum sayfası (içerik ayrıntısı okunamadı).
- https://design-system.service.gov.uk/
  [göz gezdirildi] GOV.UK tasarım sistemi ana sayfası.
- https://news.designrush.com/revolut-brand-refresh-lessons-it-monks
  [blog] Revolut için BAĞIMSIZ ajans konsepti: "logoyla değil marka gerilimiyle
  başla".
- https://telegram.org/blog/folders
  [resmi] Sohbet klasörleri liste dağınıklaşınca belirir; sekmeler arası kaydırma.
- https://telegram.org/blog/crafting-android-design-and-more
  [resmi] Şubat 2026 Android yenilemesi: yeni alt çubuk ve Güç Tasarrufu'nda efekt
  kontrolü.
- https://telegram.org/blog/new-design-ai-summaries
  [göz gezdirildi] Telegram'ın 2026 başı iOS "Liquid Glass" güncellemesi.
- https://www.hey.com/features/the-screener/
  [resmi] HEY Screener: ilk kez yazanı kabul/ret kararı, özel ve geri alınabilir.
- https://docs.github.com/en/get-started/using-github/github-mobile
  [resmi] GitHub Mobile'ın ilk işi "bildirimleri yönet, ayıkla ve temizle".
- https://business.whatsapp.com/products/business-app
  [resmi] WhatsApp Business uygulaması: karşılama/uzaktayım mesajı ve ürün vitrini
  (hedef kitlenin mevcut aracı).
- https://bird.com/en-us/changelog/streamlined-inbox-boosting-productivity-with-sleek-design
  [blog] Bir gelen kutusu yenilemesi duyurusu; yön olarak sadeleşme, ölçüm yok.

### Galeri ve desen kütüphaneleri (liste)

- https://www.behance.net/search/projects/mobile%20app%20branding%20case%20study
  [liste] "mobile app branding case study" araması: ilk 25 proje, küçük resimler
  incelendi.
- https://www.behance.net/gallery/245508601/Rillo
  [görsel] Rillo: limon + koyu yeşil kart markası; tek vurgulu, malzemeye yaslı
  sunum.
- https://www.behance.net/gallery/256309773/Tritter-Crypto-Piggy-Bank-iOS-App
  [görsel] Tritter: illüstrasyonlu fennec maskotu; ayırt edici tek nesne örneği.
- https://www.behance.net/gallery/255784383/Miznaa-Fintech-App
  [blog] Miznaa: Arapça "yağmur yüklü bulut" kelimesinden doğan marka hikâyesi;
  kültürel kök örneği.
- https://www.behance.net/gallery/251945531/Hiff-Brand-Identity-Mobile-App-Design
  [görsel] Hiff: mor-siyah gradyan zemin ve elde telefon sunumu; jenerik görünümün
  örneği.
- https://www.behance.net/gallery/224067175/Mobile-App-Design-UXUI-Case-Study-for-a-Cooking-App
  [görsel] Yemek uygulaması vaka çalışması kapağı: krem zemin, tek mercan düğme,
  fotoğraf ağırlıklı karşılama.
- https://dribbble.com/tags/messaging_app
  [liste] Mesajlaşma uygulaması paylaşımları; incelenen örneklerde pastel gradyan ve
  cam maket eğilimi var.
- https://dribbble.com/tags/chat_app
  [liste] Sohbet uygulaması paylaşımları: AI sohbet şablonlarının yoğunluğu.
- https://dribbble.com/tags/inbox
  [liste] Gelen kutusu paylaşımları (çoğu web paneli; mobil az).
- https://dribbble.com/tags/empty_state
  [liste] Boş durum paylaşımları: çoğu illüstrasyon seti, az uygulama ekranı.
- https://dribbble.com/tags/onboarding_screens
  [liste] Onboarding ekranı paylaşımlarının listesi; başlıklara bakıldı, tek tek
  açılmadı.
- https://dribbble.com/tags/notification
  [liste] Bildirim paylaşımları: toast, bildirim merkezi ve izin ekranları.
- https://dribbble.com/tags/android_app
  [liste] "Android app" etiketli paylaşımlar (finans, sağlık, kripto ağırlıklı
  başlıklar); tek tek açılmadı.
- https://dribbble.com/shots/26008875-Mobile-Dark-Mode-Design-for-B2C-Support-Platform
  [görsel] B2C destek gelen kutusu: 3D sahnede eğik telefon, renkli avatar
  halkaları.
- https://dribbble.com/shots/26589117-Talk-Sense-Mobile-App-Design-for-Customer-Messaging-Platform
  [görsel] Müşteri mesajlaşma: pastel gradyan zemin ve cam maket sunumu.
- https://dribbble.com/shots/25438032-Chatia-AI-Chatbot-Mobile-App-UI-Design-Template
  [görsel] AI sohbet şablonu: pembe gradyan ve jenerik kartlar.
- https://dribbble.com/shots/25311273-Messages
  [görsel] Mesaj kartları: kırmızı okunmamış nokta iyi, her mesaj ayrı gölgeli kart
  zayıf.
- https://dribbble.com/shots/24595764-Zendenta-Inbox-Feature-in-the-Patient-Mobile-App
  [görsel] Hasta uygulaması gelen kutusu: sabit satır yapısı iyi, metin çok küçük.
- https://dribbble.com/shots/25639475-Empty-state-Untitled-UI
  [görsel] Sakin boş durum: tek nötr ikon, başlık, açıklama, ikincil + birincil
  düğme.
- https://dribbble.com/shots/26512097-Allow-camera-access-Untitled-UI
  [görsel] İzin modalı: açıklama, sistem istemi önizlemesi, numaralı adımlar, yedek
  yol.
- https://dribbble.com/shots/25882999-Expandable-Notification-Toast-with-timer-for-Saas-App-Dashboard
  [liste] Genişleyen bildirim toast'ı fikri (web paneli).
- https://mobbin.com/explore/mobile/screens
  [liste] Mobbin ekran desenleri dizini: Boş Durum 5.071, İzin 1.878, E-posta ve
  Mesajlar vb.
- https://mobbin.com/explore/mobile/screens/emails-messages
  [liste] WhatsApp, Spark Mail, Apple Mail, Gmail, Discord, OpenPhone gibi gerçek
  mesajlaşma ekranları.
- https://mobbin.com/explore/mobile/screens/empty-state
  [liste] Truecaller, Pinterest, Apple Wallet, Uber Eats vb. boş durum ekranları.
- https://mobbin.com/explore/mobile/screens/permission
  [görsel] İzin ekranları: Nextdoor, Citizen, Walmart ve kart tarama örneği
  incelendi.
- https://mobbin.com/explore/mobile/flows/onboarding
  [liste] Airbnb, Revolut, Spotify, Headspace, Wise, Uber onboarding akışı listesi.
- https://mobbin.com/explore/mobile/ui-elements/badge
  [liste] Rozet bileşeni örnekleri sayfası.
- https://refero.design/
  [liste] Gerçek ürün ekranı ve akış arama kütüphanesi (iOS ve web).
- https://refero.design/s/empty-state
  [liste] Boş durum ekranı arama sayfası (görseller yüklenmedi).
- https://60fps.design/
  [liste] 2.110 hareket örneği ve 494 uygulama; hareket ilham arşivi.
- https://pttrns.com/
  [liste] Pttrns: ücretsiz hesapla mobil desen kütüphanesi (ana sayfa okundu).
- https://www.uisources.com/
  [liste] ScreensDesign: 2.838 uygulamanın ekran kayıtları ve MCP bağlantısı (Pro
  plan sunuyor).
- https://land-book.com/
  [liste] Land-book: web sitesi tasarım galerisi (uygulama pazarlama sayfaları
  için).
- https://godly.website/
  [liste] Godly: web tasarım ilham galerisi (içerik kısıtlı okundu).
- https://collectui.com/
  [liste] Collect UI: günlük arayüz ilhamı koleksiyonu.
- https://www.awwwards.com/websites/mobile-apps/
  [liste] Awwwards "Mobile & Apps" listesi: uygulama pazarlama siteleri (arayüz içi
  değil).
- https://www.checklist.design/
  [liste] Mobil için Onboarding, Chat, Push Notification Opt-in gibi kontrol
  listeleri (içerik okunamadı).
- https://www.useronboard.com/
  [liste] UserOnboard: web uygulamalarının ilk kullanım analizleri (ClickUp, Zapier,
  Canva).
- https://goodux.appcues.com/blog/behance-user-onboarding
  [blog] Behance onboarding analizi: tooltip'ler ve izin isteklerinde açıklayıcı
  metin.

### AI slop ve topluluk tepkileri

- https://platform.claude.com/cookbook/coding-prompting-for-frontend-aesthetics
  [resmi] "AI slop" terimi, Inter/mor gradyan ve Space Grotesk yakınsaması uyarısı.
- https://raw.githubusercontent.com/anthropics/skills/main/skills/frontend-design/SKILL.md
  [resmi] Üretilmiş tasarımın beş kümesi ve "cesaretini tek yerde harca" ilkesi.
- https://cdn.jsdelivr.net/npm/pixelslop@0.3.8/dist/skill/resources/ai-slop-patterns.md
  [blog] 26 görsel slop örüntüsünün şiddet puanlı topluluk kataloğu.
- https://www.925studios.co/blog/ai-slop-design-tells
  [blog] Inter, indigo gradyan, üç kart; Wathan "özrü" hikâyesinin ikincil aktarımı.
- https://www.925studios.co/blog/ai-slop-web-design-guide
  [blog] Slop'un nedeni (dağılıma yakınsama) ve sık işaretler; ajans pazarlaması
  içerir.
- https://prg.sh/ramblings/Why-Your-AI-Keeps-Building-the-Same-Purple-Gradient-Website
  [blog] Mor gradyanın Tailwind varsayılanından geldiği hikâyesinin ayrıntılı
  anlatımı.
- https://dev.to/jaainil/ai-purple-problem-make-your-ui-unmistakable-3ono
  [göz gezdirildi] "AI morunu" aşma üzerine tek yazarlı görüş yazısı.
- https://robotsonpayroll.substack.com/p/how-to-avoid-that-vibe-coded-look
  [blog] Sol renkli kenarlık ve emoji ikonlarını slop işareti sayan yazı; denetim
  iddiası doğrulanamadı.
- https://ui-ux-pro-max-skill.com/blog/avoiding-ai-slop/
  [blog] Slop'u önlemek için "yasak listesi" ve somut referans verme teknikleri.
- https://vp0.com/blogs/why-does-my-ai-app-look-generic.md
  [blog] Mobilde jenerik görünüm: varsayılan kitaplıklar sameness'i artırır (satıcı
  yazısı).
- https://medium.com/design-bootcamp/the-most-installed-design-document-of-2026-is-30-lines-long-6b9a89834bd8
  [göz gezdirildi] Anthropic'in kısa tasarım yönergesinin yaygınlaşması üzerine
  ikincil yazı.
- https://www.reddit.com/r/webdev/comments/1nx0y4q/ai_has_a_purple_problem/
  [topluluk] 581 puanlı r/webdev başlığı: "AI'nın mor sorunu", Tailwind UI indigo
  yorumları.
- https://youtu.be/AG_791Y-vs4
  [topluluk] Syntax podcast videosu "The AI Purple Problem" (başlık ve kanal
  doğrulandı).
- https://news.ycombinator.com/item?id=43975352
  [topluluk] HN'de Material 3 Expressive (360 puan, 557 yorum): boşluk, kontrast,
  yoğunluk eleştirileri.
- https://hn.algolia.com/api/v1/items/43975352
  [topluluk] Aynı HN başlığının yorum ağacı (API); yorumlar buradan okundu.
- https://www.androidauthority.com/material-3-expressive-dislike-3608927/
  [haber] Okuyucuların Expressive'e itirazları; anketin çoğunlukla olumlu çıktığı
  notu.
- https://www.androidauthority.com/month-with-material-3-expressive-admit-i-was-wrong-3609382/
  [haber] Bir ay sonra "yanılmışım" yazısı: oyunbaz şekil ve animasyonlara alışma.
- https://designcompass.org/en/2026/08/19/google-achieve-design-backlash/
  [haber] Google'ın "Achieve" örnek uygulamasına yönelik sert tasarım eleştirisi.
- https://www.androidfaithful.com/material-3-expressive-explained-a-conversation-with-googles-mindy-brooks-android-faithful-episode-109/
  [haber] Google yöneticisiyle söyleşi: erişilebilirlik dengesi ve bulanıklık
  kapatma düğmesi.
- https://phandroid.com/2025/05/13/material-3-expressive-makes-google-apps-feel-less-robotic/
  [haber] Google araştırma rakamlarının ikincil özeti (4 kat hızlı bulma, %87
  tercih).
- https://1000logos.net/news/what-makes-an-app-feel-expensive-the-psychology-of-premium-digital-design/
  [göz gezdirildi] "Pahalı hissettiren uygulama" psikolojisi üzerine genel yazı.
- https://thisisglance.com/blog/the-psychology-of-luxury-app-design-and-why-it-matters
  [göz gezdirildi] Lüks uygulama tasarımı üzerine ajans yazısı.
- https://vp0.com/blogs/what-makes-an-app-look-professional
  [blog] "Profesyonel görünüm" nitelikleri ve kaynaksız yüzdeler; satıcı yazısı.
- https://wisdmlabs.com/blog/mobile-website-design-that-feels-premium/
  [göz gezdirildi] Mobilde "ucuz" veya "premium" hissettiren kararlar üzerine ajans
  yazısı.

### Ergonomi ve erişilebilirlik

- https://www.uxmatters.com/mt/archives/2013/02/how-do-users-really-hold-mobile-devices.php
  [araştırma] Hoober: 1.333 gözlem; tek el %49, beşik %36, iki el %15; tutuş sürekli
  değişiyor.
- https://www.uxmatters.com/mt/archives/2017/03/design-for-fingers-touch-and-people-part-1.php
  [araştırma] Hoober 2017: başparmak süpürme şeması yanlış; merkezde 7 mm, köşede 12
  mm hedef.
- https://smashingmagazine.com/2016/09/the-thumb-zone-designing-for-mobile-users
  [blog] Başparmak bölgesi testleri: gezinme, kart ve kaydırma jestleri.
- https://alistapart.com/article/how-we-hold-our-gadgets/
  [blog] Josh Clark: zahmetsiz bölge altta, tutan elin karşı tarafında.
- https://www.scotthurff.com/posts/how-to-design-for-thumbs-in-the-era-of-huge-screens/
  [blog] Büyük ekranda başparmak tasarımı; Hoober sayılarının tekrarı.
- https://adactio.com/journal/6053
  [blog] Josh Clark "Designing for Touch" konuşmasının özeti.
- https://addyosmani.com/blog/touch-friendly-design/
  [blog] Dokunma dostu tasarım özeti; Hoober sayıları ve %75 rakamına dair uyarı.
- https://www.monterail.com/blog/thumb-friendly-navigation-word-oriented-design
  [blog] 2026 özeti: 7/9/12 mm hedefler ve başparmak bölgesinin "çok katı" olduğu
  (tek kaynak).
- https://mobiforge.com/design-development/designing-touch-thumb-and-finger-sized-design
  [blog] Parmak genişliği 16-20 mm; 44 px önerisi (2013, eski).
- https://www.smartinsights.com/mobile-marketing/mobile-design/research-on-mobile-interaction-behaviour-and-design/
  [blog] Hoober çalışmasının pazarlamacılar için özeti.
- https://bootcamp.uxdesign.cc/understanding-mobile-device-holding-habits-implications-for-ux-design-1cc11a122b96
  [göz gezdirildi] Tutuş alışkanlıklarının UX'e etkisi üzerine özet yazı.
- https://www.nngroup.com/articles/touch-target-size/
  [araştırma] Hedef en az 1 cm × 1 cm; görme-dokunma asimetrisi; karşıt eylemlerde
  yanlış dokunma.
- https://www.w3.org/WAI/WCAG22/Understanding/target-size-minimum.html
  [resmi] WCAG 2.5.8: en az 24 × 24 CSS px veya yeterli boşluk (AA).
- https://www.w3.org/WAI/WCAG22/Understanding/target-size-enhanced.html
  [resmi] WCAG 2.5.5: 44 × 44 CSS px (AAA).
- https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast.html
  [göz gezdirildi] WCAG 1.4.11: arayüz bileşenleri ve grafikler için metin dışı
  kontrast.
- https://www.w3.org/WAI/WCAG22/Understanding/contrast-minimum.html
  [göz gezdirildi] WCAG 1.4.3: metin kontrastı asgari ölçütü.
- https://developer.samsung.com/one-ui/index.html
  [resmi] One UI: üstte görüntüleme alanı, altta etkileşim alanı.
- https://developer.samsung.com/one-ui/largescreen-and-foldable/large_screen_layout.html
  [resmi] Büyük ekranda erişilebilir kontroller: kamera kontrolleri yana alınıyor.
- https://www.nngroup.com/articles/aesthetic-usability-effect/
  [araştırma] Güzel görünen arayüz küçük kullanılabilirlik sorunlarına tolerans
  doğurur; testte maskeleyebilir.

### Animasyon ve hareket

- https://www.nngroup.com/articles/animation-duration/
  [araştırma] Animasyon 100-500 ms; basit geri bildirim ~100 ms; "çok uzun olması
  çok kısa olmasından yaygın".
- https://www.nngroup.com/articles/microinteractions/
  [araştırma] Mikro etkileşim: tetikleyici + tek amaçlı geri bildirim; durum, hata
  önleme, marka.
- https://lawsofux.com/doherty-threshold/
  [blog] Sistem geri bildirimi 400 ms altında olmalı (Doherty eşiği).
- https://web.dev/articles/rail
  [resmi] Eylemden sonra 100 ms içinde yanıt; kare başına 16 ms bütçesi.
- https://web.dev/articles/prefers-reduced-motion
  [resmi] Hareket azaltma tercihi ve vestibüler rahatsızlık gerekçesi.
- https://web.dev/articles/animations-guide
  [göz gezdirildi] Yüksek performanslı animasyon rehberi (dönüşüm ve opaklık).
- https://www.w3.org/WAI/WCAG22/Understanding/animation-from-interactions.html
  [resmi] WCAG 2.3.3: etkileşimden doğan hareket kapatılabilmeli (AAA).
- https://emilkowal.ski/ui/great-animations
  [blog] İyi animasyon: doğal, hızlı, amaçlı, performanslı; ease-out ve 300 ms altı.
- https://emilkowal.ski/ui/7-practical-animation-tips
  [blog] Basışta 0,97 scale, scale(0)'dan başlamama, doğru easing.
- https://animations.dev/
  [blog] Emil Kowalski'nin animasyon kursu ve kaynakları.
- https://sonner.emilkowal.ski/
  [liste] Emil Kowalski'nin toast bileşeni Sonner'ın tanıtım sayfası (içerik kısıtlı
  okundu).
- https://www.joshwcomeau.com/animation/
  [liste] Josh Comeau'nun animasyon yazıları dizini.
- https://www.joshwcomeau.com/animation/css-transitions/
  [blog] Geçişler: ease-out, süre, hareket tercihine saygı.
- https://www.joshwcomeau.com/animation/a-friendly-introduction-to-spring-physics/
  [göz gezdirildi] Yay fiziği animasyonuna sade giriş (JavaScript örnekleriyle).
- https://rive.app/
  [liste] Rive: etkileşimli animasyon motoru (ek bağımlılık maliyeti).
- https://airbnb.io/lottie/
  [liste] Lottie belgeleri (animasyon dosyası oynatıcı).

### Boş durum, onboarding ve izin

- https://www.nngroup.com/articles/empty-state-interface-design/
  [araştırma] Boş durum üç iş yapar: durum bildir, öğret, yol göster; "kayıt yok"
  yanlış durum uyarısı.
- https://www.nngroup.com/videos/empty-states-in-application-design-guidelines/
  [araştırma] Aynı üç yönergenin video özeti.
- https://www.sap.com/design-system/fiori-design-web/v1-96/foundations/best-practices/global-patterns/designing-for-empty-states
  [resmi] Boş durum ne olduğunu, neden görüldüğünü, sonraki adımı söylemeli.
- https://www.canva.dev/docs/apps/design-guidelines/empty-states
  [resmi] Başlık + 1-2 cümle + tek birincil düğme; ölçüler.
- https://www.setproduct.com/blog/empty-state-ui-design
  [blog] Boş durum ilkeleri ve kontrol listesi (satıcı blogu).
- https://vp0.com/blogs/designing-ios-empty-states-that-feel-intentional
  [blog] İlk açılış, temizlenmiş ve sonuçsuz durumların ayrı tasarlanması (satıcı
  yazısı).
- https://www.nngroup.com/articles/skeleton-screens/
  [araştırma] İskelet ekran yalnız tam sayfa yüklemeleri için.
- https://www.nngroup.com/articles/onboarding-tutorials/
  [araştırma] Eğitim turları unutuluyor; bağlamsal yardım ("pull revelation") daha
  iyi.
- https://www.nngroup.com/articles/mobile-instructional-overlay/
  [araştırma] Mobil ipucu katmanları hızla kapatılıyor; taranabilir olmalı.
- https://www.nngroup.com/articles/progressive-disclosure/
  [araştırma] Nadir özellikleri ikinci katmana at.
- https://www.nngroup.com/articles/progress-indicators/
  [araştırma] Bekleme animasyonları belirsizliği azaltıp beklemeyi tolere edilir
  kılar.
- https://www.nngroup.com/articles/icon-usability/
  [araştırma] İkonlar etiketsiz belirsiz; anlamsız ikon "göz tatlısı" ve gürültü.

### Zanaat: renk, yazı, ikon, gölge

- https://www.radix-ui.com/colors/docs/palette-composition/understanding-the-scale
  [resmi] 12 basamaklı renk ölçeği ve her basamağın işlevi (arka plan, kenarlık,
  metin).
- https://evilmartians.com/chronicles/oklch-in-css-why-quit-rgb-hsl
  [blog] OKLCH: algısal parlaklık, daha öngörülebilir palet ve erişilebilirlik.
- https://refactoringui.com/
  [blog] Refactoring UI: daha az kenarlık, hiyerarşi, boşluk sistemi başlıkları.
- https://practicaltypography.com/
  [göz gezdirildi] Butterick'in çevrimiçi tipografi kitabı (ana sayfa okundu).
- https://fonts.google.com/knowledge
  [göz gezdirildi] Google Fonts bilgi merkezi (yazı tipi konuları).
- https://fonts.google.com/icons
  [resmi] Material Symbols kütüphanesi ve Apache lisansı notu.
- https://developers.google.com/fonts/docs/material_symbols
  [resmi] Material Symbols eksenleri: doluluk, ağırlık, derece, optik boyut.
- https://lucide.dev/
  [resmi] Lucide ikon seti (ISC lisansı).
- https://phosphoricons.com/
  [liste] Phosphor ikon ailesi (lisans bu araştırmada doğrulanmadı).
- https://www.joshwcomeau.com/css/designing-shadows/
  [blog] Tek ışık kaynağı, katmanlı ve renkli gölge ilkeleri.

### Marka süreci, mağaza ve sistem çıktıları

- https://www.designtokens.org/
  [resmi] W3C Design Tokens topluluk grubu: jeton biçimi standardı.
- https://styledictionary.com/
  [göz gezdirildi] Style Dictionary: tasarım jetonlarını platform çıktısına çeviren
  araç (ana sayfa).
- https://material-foundation.github.io/material-theme-builder/
  [göz gezdirildi] Material Theme Builder aracı (sayfa betikle çiziliyor; yalnız
  başlık okundu).
- https://raw.githubusercontent.com/material-foundation/material-color-utilities/main/README.md
  [resmi] Material Color Utilities: erişilebilir kontrastla renk şeması üretme
  kitaplığı.
- https://campusstore.miamioh.edu/designing-brand-identity-essential-guide/bk/9781118099209
  [blog] Wheeler'ın "Designing Brand Identity" kitabı: beş fazlı süreç (yayıncı
  sayfası).
- https://www.apptweak.com/en/aso-blog/how-to-optimize-your-app-screenshots
  [blog] Mağaza ekran görüntüleri: ilk üç ekran, kısa fayda başlığı, gerçek arayüz
  (tek kaynak).

### Türkiye ve yerelleştirme

- https://haacked.com/archive/2012/07/05/turkish-i-problem-and-why-you-should-care.aspx/
  [blog] "Türkçe İ sorunu": locale'e duyarsız büyük/küçük harf dönüşümü hata verir.
- https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/uppercase.html
  [resmi] Kotlin uppercase() invariant locale kullanır; locale'li sürüm ayrı.
- https://www.cryoutcreations.eu/forums/t/missing-turkish-characters-s-c-g-o-u-etc
  [topluluk] Latin Extended içermeyen yazıda Türkçe harfler kayboluyor (eski forum,
  tek kaynak).
- https://www.teknoblog.com/bip-iphone-menulerini-liquid-glass-ile-yeniledi/
  [haber] BiP'in iPhone menülerini yenilemesi; yerli uygulama arayüz haberi.
- https://www.teknoblog.com/turkcell-bip-mobil-uygulama-incelemesi/
  [göz gezdirildi] Turkcell BiP uygulama incelemesi (eski, bağlam için).
- https://ixd.prattsi.org/2022/02/design-critique-getir-ios-app/
  [blog] Getir iOS uygulamasına okul eleştirisi: net düzen, ürün odağı.


## Temizlik ve lisans notu

- İndirilen küçük resimler (Dribbble 10, Behance 11, Mobbin 2 ekran görüntüsü,
  Google Messages 2, kendi konsept panolarımızdan 1 karşılaştırma sayfası) not
  alındıktan hemen sonra silindi. Scratch klasöründe başkasının görseli kalmadı
  (`shots` klasörü silindi; klasörde başka .png/.jpg yok).
- Aynı scratch klasöründe önceki, yarım kalmış bir denemeden dosyalar vardı (20 Ekim
  22:08-22:12 tarihli: notes.md, pw.js, behance.txt, m3.txt, txt/ altında HIG metin
  dökümleri, iki ekran görüntüsü). İki ekran görüntüsü (behance.png, m3.png)
  silindi; notes.md içindeki 7 notun (Linear, M3 süre ve easing değerleri, Hoober,
  HIG hareket, Android izin) tamamı bu çalışmada kaynağından yeniden okunup
  doğrulandı (N1'deki "bildirimler tipe göre + takım arkadaşlarının yüzleri"
  ayrıntısı hariç, o bakılmadı); çelişen bir şey çıkmadı.
- Scratch klasöründe yalnız sayfa metni notları (ham metin) ve küçük araç betikleri
  duruyor; repoda yalnız bu çıktı dosyası yazıldı, başka dosya değiştirilmedi,
  commit/push yapılmadı.
- Başkasının kodu kopyalanmadı. AndroidX ve Material bileşenleri Apache-2.0: yay
  sabitleri sayı olarak not alındı. Apple HIG ve WWDC içerikleri yalnız okundu.
  Pixelslop kataloğu, Anthropic cookbook ve SKILL.md yalnız okundu; ilke çıkarıldı.
- Okuyucu proxy (r.jina.ai) üçüncü taraf bir hizmettir; yalnız herkese açık sayfa
  adresleri gönderildi, hiçbir kimlik bilgisi veya özel veri gönderilmedi.
