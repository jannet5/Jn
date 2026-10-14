# Claude İle Web Site Yapıyoruz (Herkes İçin Kolaylaştırılmış) - Adım Adım Rehber — Burak Çakır • Filtresiz Yapay Zeka — 16 dk — https://www.youtube.com/watch?v=wJyLGopw304

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).
## Tek paragraf: ne yapıyor, sonuç ne
Terminal/Claude Code kullanmadan, sadece **claude.ai sohbetinde** (chat modu) bir makarna işletmesi için blog/SSS içeren site yapıyor. Hile: **UI UX Pro Max** skill'inin GitHub reposunu "Download ZIP" ile indirip zip'i sohbete sürüklüyor (Claude zip'i açıp skill kurallarına göre tasarım yapıyor). Renk paletini **Coolors**'ta üretip görsel olarak export edip sohbete ekliyor, prompt'a SEO + GEO (yapay zeka aramaları için SEO) maddesi ve "şimdilik HTML/CSS olarak üret" ekliyor; model Fable 5, efor yüksek. Bu sırada Hostinger'da domain (~10 TL'lik kampanya fiyatı gösteriliyor) ve Premium hosting (~100 TL/ay) alıyor, Dosya Yöneticisi → `public_html` içine yeni dosya açıp Claude'un kodunu yapıştırıyor. Sonuç: makarnaları 3D gösteren, sos bilgisi, blog ve SSS içeren, mobilde de uyumlu tek HTML site. Revizyonlar (abone ol kaldır, blog "oku" kaldır) ve ikinci sayfa (`hikayemiz.html`) üretip butondaki `#hikaye` linkini yeni sayfaya bağlıyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude** (https://claude.ai) — chat modu (Cowork da seçenek olarak anılıyor); model seçenekleri: Opus 4.8, Sonnet 5, **Fable 5** (seçilen), **efor: yüksek**.
- **UI UX Pro Max** skill'i — Google'da "UI UX Pro Max GitHub" → repo → yeşil **Code → Download ZIP**. (Claude Code'da normalde README'deki "using Claude marketplace" komutu terminale yapıştırılır.) Link videoda verilmiyor; bizde bilinen repo: https://github.com/nextlevelbuilder/ui-ux-pro-max-skill
- **Coolors** (https://coolors.co) — "Start the generator", boşluk tuşu = yeni palet, **Export → Image** (PDF de olur).
- **Hostinger** (https://hostinger.com) — domain + Premium Web Hosting (~100 TL/ay), hPanel → Web siteleri → Dosya Yöneticisi → `public_html`.
- Google Chrome DevTools (İncele → cihaz: iPhone 14 Pro Max) — mobil kontrol.
- Sonraki adım olarak anılan: **Replit** (tasarımı panelli hale getirmek için, ayrı videoda).
- Açıklamadaki topluluk linkleri: https://www.skool.com/ucretsiz-grup-2003/about , https://www.skool.com/sfrdan-yapay-zeka-3691/about , Instagram https://instagram.com/burakcakir.ai

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. claude.ai → chat modu (terminal kullanmadan).
2. UI UX Pro Max GitHub sayfası → Code → Download ZIP → zip'i sohbete sürükle ("bu skill'in bilgi ve şartlarına göre tasarım istiyorum" anlamına geliyor).
3. Prompt'u yaz (makarna işletmesi, teknik makarna bilgisi → blog sayfası da olsun; uzun detay). Prompt metni ekranda/açıklamada bırakılacağı söyleniyor ama transkriptte ve açıklamada metin yok.
4. Coolors → Start the generator → boşlukla palet bul → Export → Image (adı "makarna") → indir → sohbete "renk teması" olarak ekle. Logo varsa onu da ekle.
5. Prompt'a SEO ve GEO maddesi ekle.
6. Model: Fable 5, efor yüksek → gönder. Unuttuğu için durdur → kalemle prompt'u düzenle: "Bu tasarımı şu anlık HTML CSS olarak üretmen yeterli." → Save → devam.
7. Beklerken Hostinger: Google ile giriş → dil Türkçe → Domainler → Yeni alan adı satın al → "makarnasitesiburada" ara (.com yıllık ~10 TL gösteriliyor; .online vb. alternatifler) → sepete ekle → satın al.
8. Diğer hizmetler → Hosting ve VPS → Premium Web Hosting (~100 TL/ay) → Planı seç → öde (aylık/yıllık bütçeye göre).
9. Web siteleri → yeni domain'i hostinge bağla (sistem soruyor) → "HTML/CSS olarak devam edilsin mi?" → Evet.
10. Web siteleri → Kontrol paneli → **Dosya Yöneticisi** (engellenirse tekrar tıkla) → `public_html` → New File `makarna.html` (index.html'in yanına).
11. Claude çıktısı ("paleti uydurdum", SVG olarak) → kodu kopyala (veya HTML indir) → `makarna.html` içine yapıştır → Kaydet → `domain.com/makarna.html` ile aç.
12. Sonuç: penne/burgu/spagetti 3D ve etkileşimli, "3D incele" butonu, sos bilgileri, "Yazılar" (blog), SSS.
13. Mobil kontrol: Chrome → İncele → cihaz iPhone 14 Pro Max → yenile → uyumlu, 3D de çalışıyor.
14. Revizyonlar (admin paneline bağlamadan statik kullanılacaksa boş kalan şeyleri kaldır): "Revizeler: Abone ol kısmını kaldır. Bloklarda oku kısmını kaldır, kısaca özet bırak."
15. Diğer revizyon örnekleri: logo linkini vererek logo değiştirme; Coolors'tan yeni palet SS'i atıp "renk temasını bu şekilde değiştir"; font değiştirme.
16. İkinci sayfa: "Tasarım ve her şey çok güzel. Şimdi ikinci bir sayfa yapacağız. Temaya bağlı kal; hikayemiz kısmını farklı bir linkte farklı bir sayfa olarak göstereceğim. Buna göre temaya bağlı kalarak sayfanın tasarımını yap." → Hostinger'da `hikayemiz.html` oluştur → kodu yapıştır → kaydet → `domain.com/hikayemiz.html` kontrol.
17. `makarna.html`'de Cmd+F "hikaye" → `href="#hikaye"` yerine hikayemiz sayfasının linkini yapıştır → kaydet → buton yeni sayfaya gidiyor.
18. Diğer menü linklerini ana sayfaya yönlendir veya boş bırak; **logo mutlaka ana sayfaya link versin** (insanlar geri dönmek için logoya basar).

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Ana prompt metni videoda okunmuyor (sadece içerik: makarna işletmesi web sitesi, makarna ile ilgili teknik bilgiler, blog sayfası; + SEO ve GEO; + ekli skill zip'i ve Coolors paleti).
- Ek: `Bu tasarımı şu anlık HTML CSS olarak üretmen yeterli.`
- Revize: `Revizeler: Abone ol kısmını kaldır. Bloklarda oku kısmını kaldır, kısaca özet bırak.`
- Logo: `Tasarımdaki logo kısmını benim sana ilettiğim logo linkiyle değiştir.`
- Renk: (yeni palet SS'i ile) `Renk temasını bu şekilde değiştir.`
- İkinci sayfa: `Tasarım ve her şey çok güzel. Şimdi ikinci bir sayfa yapacağız. Temaya bağlı kal ve hikayemiz kısmını farklı bir linkte, farklı bir sayfa olarak göstereceğim. Buna göre temaya bağlı kalarak sayfanın tasarımını yap.`

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- UI UX Pro Max skill'inin zip olarak sohbete verilmesi ("yapay zekanın kendi yaptığı tasarımlardan uzak, modern tasarım dili").
- Coolors'tan seçilmiş hazır palet görsel olarak verilmesi.
- Güçlü model + yüksek efor (Fable 5, high).
- Ürünün 3D/etkileşimli gösterimi (makarna çeşitleri) — Claude'un kendi kreatif kararı.
- Tema tutarlılığı için ek sayfalarda "temaya bağlı kal" ifadesi.
- Font/animasyon kuralı videoda ayrıca yok.

## Hatalar ve çözümleri
- Prompt'a "HTML/CSS" yazmayı unuttu → üretimi durdur, kalemle prompt'u düzenle, Save → kaldığı yerden devam.
- Hostinger Dosya Yöneticisi bazen engelliyor → tekrar tıklayınca açılıyor.
- Statik sitede boş kalan "Abone ol" ve blog "Oku" butonları → revizyonla kaldır / özet bırak.
- Tek sayfa yerine ayrı sayfa isteniyorsa: yeni HTML dosyası + `#hikaye` anchor'ını sayfa linkiyle değiştir.
- "Hatalarla birlikte yaptım, hiçbir yeri kırpmadım."

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
Müşteriye satış yok. Maliyet: Hostinger Premium Web Hosting **aylık ~100 TL**, örnek .com domain **yıllık ~10 TL** (kampanya fiyatı olarak ekranda). Claude aboneliği gerekli.

## Bizim fabrikaya alınacaklar (somut)
1. **Claude Code'suz yedek yol**: claude.ai chat'e skill repo zip'i (UI UX Pro Max vb.) sürükleyerek skill kurallarını uygulatma — ekip içi teknik olmayan kişiler veya hızlı satış demoları için.
2. **Coolors paleti export (image) → Claude'a ver** adımını brief'e ekle; müşteri paletini kendisi Coolors'ta seçebilir (boşluk tuşu), biz export'u alırız.
3. Brief şablonuna **SEO + GEO** maddesini standart koy (yerel işletme için Google + AI arama görünürlüğü).
4. Statik teslimde **ölü buton denetimi**: abone ol, blog "oku", boş menü linkleri kaldırılsın veya gerçek sayfaya bağlansın; **logo → ana sayfa** zorunlu.
5. Çok sayfalı statik site kalıbı: her ek sayfa "temaya bağlı kal" prompt'uyla ayrı HTML; anchor (`#bolum`) linkleri sayfa linkine çevrilir.
6. Mobil kontrol adımı: Chrome DevTools cihaz modu (iPhone 14 Pro Max) ile teslim öncesi tur.
7. Hostinger hPanel Dosya Yöneticisi → `public_html` ile yükleme, müşteri hosting'i Hostinger'daysa en basit teslim yolu; maliyet referansı ~100 TL/ay hosting.
