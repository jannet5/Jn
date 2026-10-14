# 2026 İçin En Kapsamlı Claude AI + WordPress Eğitimi — Darrel Wilson Türkçe'de — 76 dk — https://www.youtube.com/watch?v=jebtS94HLa0

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).

## Tek paragraf: ne yapıyor, sonuç ne
İki aşamalı bir WordPress + Claude eğitimi. **1. kısım (yeni başlayan yolu):** Hostinger'da WordPress kuruyor, Solace temasıyla "Flexy Web" başlangıç şablonunu içe aktarıyor, 10 dakikalık Elementor kursu veriyor (container/flexbox, wrap, maske, çoğaltma, geçmiş), ChatGPT ile arka plan/kişi görselleri ve ElevenLabs ile animasyonlu video arka plan üretip şablonu kişiselleştiriyor. Sonra **NovaMira** eklentisiyle Claude Desktop'ı WordPress'e **özel bağlayıcı (custom connector, uzak MCP sunucu URL'si + OAuth yetkilendirme)** olarak bağlıyor, iki skill yüklüyor (**HTML'den Elementor'a** ve **Solace Header & Footer**) ve sohbetten bölüm, mega menü, "Ödüller" sayfası, tam emlak sitesi, RankMath SEO + blog yazıları + anahtar kelime sayfaları ürettiriyor — hepsi native Elementor öğeleriyle. **2. kısım (asıl önerdiği yol):** siteyi Claude'a sildirip sıfırdan; ChatGPT'ye "ödül kazanan, lüks bir emlak sitesi ekran görüntüsü" çizdiriyor, aynı görselleri tek tek yüksek kaliteli + sıkıştırılmış ürettirip ZIP alıyor, Claude Design'a SS + görselleri verip (tasarım sistemi kapalı) Evernote prompt'uyla HTML kopyasını kuruyor, HTML'yi **görselsiz** indiriyor (24 KB), görselleri WP Medya'ya elle yüklüyor, Claude Desktop'ta dönüştürme prompt'uyla ~12 dakikada siteyi native Elementor'a aktarıyor. Kalan hataları (flex wrap, yanlış renk, header arka planı) elle veya "bozuk SS + orijinal SS" verip Claude'a düzelttirerek gideriyor; ek sayfaları Claude'a WordPress üzerinden ürettiriyor. Son olarak Elementor Pro + NovaMira Pro ile aynı siteyi mega menü ve popup'la yeniden aktarıyor ("piksel piksel uyumlu"); popup tetikleyicilerini elle ayarlamak gerekiyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Hostinger** — %10 indirim linki: https://darrelwilson.com/hostinger-turkish (kupon: Darrel10; VPS, Horizons ve temel hosting dahil tüm planlarda). Önerilen plan: **Unlimited** (sınırsız site, ücretsiz alan adı, NVMe).
- **Solace** teması (WP dizini) + **Solace Extra** eklentisi + başlangıç şablonları (166+ ücretsiz düzen; şablon: **Flexy Web**); Solace Site Builder (ücretsiz header/footer/mega menü). Solace PRO: https://pro.solacewp.com/
- **Elementor** (ücretsiz; şablon içe aktarırken onay kutusuyla kuruluyor) ve son bölümde **Elementor Pro** (mega menü, popup, tema oluşturucu)
- **NovaMira** (WordPress MCP eklentisi; ücretsiz ZIP): https://www.darrelwilson.com/novamira — NovaMira Pro için Darrel10 ile %10 indirim
- **Claude Desktop** (tarayıcı sürümü değil) — Ayarlar > Bağlayıcılar (Connectors), Ayarlar > Yetenekler (Skills)
- **Claude için skill'ler (Google Drive)**: https://drive.google.com/file/d/1gbo-LP3W1Ar2ttFJ4QPWtkz8Bt9ygogo/view?usp=sharing — "HTML to Elementor" (dönüştürme + Claude'a Elementor öğretme) ve "Solace Header and Footer" (Elementor Pro olmadan özel header/footer)
- **Claude Design** (ücretli; aynı adımlar ücretsiz Claude ile de yapılabilir ama Claude Design "çok daha iyi ve hızlı")
- **Prompt'lar (Evernote)**: https://share.evernote.com/note/6d86932b-d409-7dd7-b87d-c31eec6863bb — HTML kopya prompt'u; HTML→Elementor dönüştürme prompt'u (ücretsiz sürüm + Pro sürüm bölümleri)
- **ChatGPT** (site ekran görüntüsü ve görsel üretimi; altyazıda "ChatGPT SEO güncellemesi" diye geçen yeni görsel modeli)
- **ElevenLabs** (görselden animasyonlu video; modeller: Seedance — "en iyisi", Gemini/Veo da iyi): https://darrelwilson.com/11labs
- **GoFullPage** (tam sayfa ekran görüntüsü)
- **Rank Math** SEO eklentisi (Claude'a kurduruyor); görseller için Claude **unsplash.com**'dan çekiyor
- Hostinger'ın önbellek (cache) eklentisi bırakılıyor, diğer Hostinger eklentileri siliniyor

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
**A. Hosting + WordPress (Hostinger)**
1. Hostinger → Başla → **Unlimited** plan → süre (48 ay "4 yıl 200 $ altı"; en az 12 ay → ücretsiz alan adı) → kupon Darrel10 → hesap → fatura → kart/PayPal → ödeme.
2. Sihirbaz: "Yeni web sitesi oluştur" → **WordPress** → ücretsiz alan adı → alan adı sahip bilgileri → ülke → WP giriş bilgileri (not al) → **"Sıfırdan oluştur"**.
3. E-postadan alan adını doğrula (30 gün içinde yoksa iade + alan adı iptal).
4. Eklentiler: tüm Hostinger eklentilerini devre dışı bırak + sil; **önbellek eklentisi kalsın**.
5. Giriş: `alanadi.com/wp-admin` ya da Hostinger > Web siteleri > **WP Admin** (şifresiz). Kullanıcılar > Profil'den panel renk şeması / yeni şifre.
6. Demo yazı ve sayfayı çöpe at.

**B. Tema + şablon (yeni başlayan yolu)**
7. Görünüm > Temalar > Yeni ekle > **Solace** kur + etkinleştir → "başlangıç şablonlarını yükle" → Solace paneli > Başlangıç şablonları > Başla → **Flexy Web** → önizlemede renk paleti/font seçilebilir → **sayfa oluşturucuyu yükle onay kutusu** → Devam → bülten "atla" → ~30 sn – 1 dk'da içe aktarma (referanslar, iletişim, kariyer, hakkımızda sayfaları + medya kütüphanesine ücretsiz görseller dahil).
8. Elementor > Ayarlar > Editör: **Atomic Editor'ı devre dışı bırak** (geliştiricilere yönelik).

**C. Elementor hızlı kurs (özet)**
9. Yeni sayfa (pratik için "demo") → Elementor ile düzenle. Container = satır; öğeler sürükle-bırak; İçerik / Stil / Gelişmiş sekmeleri (Gelişmiş: padding, hareket efektleri, giriş animasyonu "soldan kayarak").
10. Yükseklik **VH** (örn. 80), arka plan Stil > Klasik > renk/görsel/video.
11. **Flexbox**: ortalama, boşluk (gap), yön dikey/yatay; iki buton yan yana → iç container + yatay yön + gap + padding 0. **Wrap / No wrap**: wrap kartları alt satıra geçirir.
12. Görsele **Maske** (Gelişmiş > Maske). Sağ tık: çoğalt / stili kopyala / sil; bölüm ve sütun çoğaltma. **Geçmiş (History)** ile geri dönüş. Buton link alanına sayfa adı yazınca otomatik bağlanıyor.
13. Görünüm > Menüler > birincil menü → sayfayı ekle → sürükle → Menüyü kaydet.

**D. AI görsellerle şablonu kişiselleştirme**
14. Sitenin SS'i → ChatGPT: "web sitem için bir arka plan oluştur"; "bu web sitesi ile uyumlu oldukça yaratıcı bir görsel verebilir misin?" (kişi görseli) → iki görselle his tamamen değişti.
15. GoFullPage ile tam sayfa SS → ChatGPT: "web sitesinin geri kalanı için mevcut tasarıma en uygun arka planları verebilir misin?" → "her arka plan görselini yüksek kalitede ver ama sitem hızlı çalışsın diye sıkıştır" → ZIP halinde indir → sitede uygula.
16. Video: ChatGPT video için kötü → **ElevenLabs**: görsel referans yükle → model (Seedance en iyi, Gemini de iyi) → "Bu arka planla animasyonlu bir video oluşturmanı istiyorum. Çok detaylı ve dekoratif olduğundan emin ol." → Medya > Yeni ekle → videoyu yükle → **Dosya URL'sini kopyala** → Elementor'da hero container > Stil > Arka plan > Video → URL'yi yapıştır.

**E. Claude ↔ WordPress (MCP) — NovaMira, custom connector yöntemi**
17. novamira sitesinden "Ücretsiz indir" → ZIP → Eklentiler > Eklenti yükle > Şimdi kur > Etkinleştir.
18. NovaMira > **Yapılandırma** → AI yeteneklerini etkinleştir kutusu → Ayarları kaydet → Tamam.
19. İki yöntem sunuyor: **Kimlik doğrulama (authentication)** veya uygulama şifresi → "Kimlik doğrulama" (basit) → istemci olarak **Claude Desktop** → bir **ad** ve **sunucu URL'si** veriyor.
20. Claude Desktop (tarayıcı olmaz) → Ayarlar → **Bağlayıcılar (Connectors)** → Ekle → **Özel bağlayıcı (Custom connector)** → adı yapıştır → **Uzak MCP sunucu URL'si**'ne NovaMira sunucu URL'sini yapıştır → Ekle → **Bağlan**.
21. Açılan pencerede **WordPress'e zaten giriş yapmış olmalısın** (çıkış yaptıysan kimlik doğrulama sorunu) → **Yetkilendir** → Claude Desktop'a dönüş, bağlı.
22. Araç izinlerini **"Her zaman izin ver"** yap (yoksa her adımda onay soruyor).
23. Doğrulama: sohbet kutusunda "+" → Connectors → site adı (orientutorial.com) listede.

**F. Skill kurulumu**
24. Google Drive'dan iki skill'i indir → Claude Desktop Ayarlar → **Yetenekler (Skills)** → Ekle → **Beceri yükle (Upload a skill)** → "HTML to Elementor" yükle; aynı şekilde "Solace Header and Footer".
25. Kendi skill'ini yapmak için: Claude'dan skill'i oluşturacak prompt'u iste → oluştur → test et → hataları SS + açıklamayla Claude'a öğret → yinele. Kendi skill'ini mükemmelleştirmesi ~5 saat sürmüş; ilk denemede çalışmaz.

**G. Claude ile mevcut WordPress'i düzenleme (MCP üzerinden)**
26. Bölüm: kısa prompt (aşağıda) → ~3 dk → sayfayı yenile → animasyonlu, aynı renk/stilde bölüm; özel CSS ile geliştirmiş; Elementor'da açınca **hepsi native öğe, HTML bloğu değil**.
27. Mega menü + "Ödüller" sayfası (Solace Site Builder ile) → ~7 dk → küçük hizalama hataları (yatay/dikey), unutulan ikonlar → elle düzeltme. Mega menü: Solace > Site Builder > header düzenle; içinde nereden geldiği belli olmayan bir video bile koymuş; link sekmesinden sayfa bağlanıyor.
28. Tam site: Claude'dan önce prompt ürettir, sonra o prompt'u çalıştır → ana sayfa + hakkımızda + iletişim (form, ofis, harita, telefon CTA); görseller unsplash'tan; bazı bölümler bozuk.
29. SEO: Rank Math'i kurdur, tüm sayfa/gönderileri optimize, site haritası, iç linkler, 2 blog yazısı, her anahtar kelime için ek sayfa → ~11 dk → 3 sayfa + blog + optimizasyon. Rank Math snippet'inde ajans adını yanlış ("Orion") koymuş → elle düzelt. Claude arka planda çalışırken sen sitenin başka yerinde çalışabilirsin.

**H. Asıl yöntem: AI ile özgün tasarım → HTML → native Elementor**
30. Claude'a "tüm sayfaları, gönderileri, header ve footer şablonlarını sil; sıfırdan başlamak istiyorum" → site 404.
31. **ChatGPT**'de site görseli: "Nashville, Tennessee'de oldukça yaratıcı bir emlak web sitesinin ekran görüntüsünü oluştur. Tasarımı yaratıcı, ödül kazanan tarzda ve lüks tarzda yap." (kısa genel prompt yeterli, yeni modeller iyi).
32. Görseller: "Ekran görüntüsündeki her bir görseli benim için oluştur. Ekran görüntüsündekiyle birebir aynı görseli istiyorum; yüksek çözünürlüklü ve kaliteli olsunlar ama çok büyük olmamaları için sıkıştır." → "Bu görselleri indirebilmem için bir ZIP dosyasına koy." → SS'yi ve ZIP'i indir.
33. **Claude Design**: SS'yi + görselleri sürükle-bırak; **tasarım sistemi kullanıyorsan kapat** (tasarımı etkiler) → Evernote'taki "tüm siteyi HTML olarak kopyala" prompt'unu yapıştır → 3-4 dk → site hazır (görselleri doğru yerlere koymuş; footer büyük).
34. HTML'yi **görselsiz** indir: "HTML dosyasını indirmek istiyorum ama HTML indirmesine görselleri dahil etme." → dosya 20–100 KB (onunki 24 KB; görselli ~7–10 MB olurdu ve WP'ye aktarım uzun sürerdi).
35. **WP hazırlığı**: NovaMira yüklü/etkin (MCP), **Solace teması + Solace Extra** yüklü (prompt header/footer'ı Solace ile kuruyor → Elementor Pro gereksiz), Elementor > Ayarlar: Atomic Editor kapalı, **Features > Container = Active** (eski "inner sections" yerine flexbox/grid) → Kaydet; **Medya > Kütüphane**: ZIP'i masaüstüne çıkar (ZIP'i doğrudan sürükleme), içindeki görselleri sürükleyip yükle.
36. Claude Desktop'ta iki skill yüklü olsun (HTML to Elementor + Solace).
37. Evernote'taki **dönüştürme prompt'unu (ücretsiz sürüm bölümü)** kopyala → not defterinde doldur: site URL'si, işletme adı (Wilson Estates), hizmetler, ne iş yaptığın, sıralanmak istediğin anahtar kelimeler (HTML şablondaki içeriği bunlarla tüm sitede yeniden yazıyor; sonradan tek tek değiştirmek çok uzun sürer). Claude kutusunda düzenleme yapılamadığı için dışarıda doldur.
38. Claude Desktop: "Dosya ve fotoğraf ekle" → HTML dosyası + doldurulmuş prompt → çalıştır → ~12 dk → site canlı.
39. Kontrol: Elementor'da açınca tüm öğeler native; bir görseli arka plan/kapak olarak koymuş (sorun değil); içerik "Wilson Estates"e uyarlanmış.
40. Elle düzeltmeler: sağda olması gereken içerik altta → container'da **No wrap**; nereden geldiği belli olmayan mavi buton → Stil > renk > global/varsayılan renk; Solace > Site Builder > Header > bölümü düzenle → arka plan sadece hover'da eklenmiş → Stil > arka plan rengi ekle → yayınla; Footer → **No wrap** (görsel tam genişliğe zorlandığı için alt satıra geçiyordu).
41. **SS ile düzeltme**: bozuk sayfanın SS'i + orijinal tasarımın SS'i → Claude'a fix prompt'u → ~5 dk → "%100 kusursuz" düzeltme (Claude site yapısını önbellekte tuttuğu için hızlı).
42. Ek sayfa: "aynı stil ve tasarımı taşıyan hakkımızda sayfası oluştur + menüye ekle" → ~7 dk. 10-20-30 sayfa da istenebilir (hizmetler, portföy, iletişim, ekibim, ödüller…). Görüşü: ilk sayfayı Claude Design'dan aktarıp **gerisini Claude'a doğrudan WordPress'te ürettirmek**, her şeyi Claude Design'da yapıp aktarmaktan daha hızlı/verimli.

**I. Pro sürüm hattı (Elementor Pro + NovaMira Pro)**
43. Elementor Pro + NovaMira Pro yükle (NovaMira Pro zorunlu değil; hızlandırıyor, doğruluğu artırıyor) → siteyi sil → Evernote'taki **Pro sürüm prompt'unu** not defterinde doldur (sitenin gerçek adı/URL — "çok önemli", işletme bilgileri) → HTML'yi ekle + prompt (görseller zaten yüklü) → ~15 dk.
44. Sonuç: görselli **mega menü**, kısa tanıtım yazıları; önceki "wrap" ve footer sorunları NovaMira Pro ile çözülmüş; "orijinal tasarımla %100 piksel piksel uyumlu"; bir "danışmanlık randevusu" **popup**'ı oluşturmuş.
45. Popup **görüntüleme koşulları/tetikleyicileri elle**: tüm site, sayfa kaydırma tetikleyicisi / 3 sn, gelişmiş: en fazla ~2 kez göster. Claude bunu yapmaya çalışıyor ama başarısız (özel prompt'larla bile).

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
(Videoda söylendiği gibi; altyazı hataları düzeltildi.)
- Kendi skill'ini üretmek: "Claude'un Elementor'u nasıl kullanacağını öğreneceği bir beceri (skill) oluşturmak istiyorum. Yerel (native) Elementor öğelerini kullanmasını istiyorum. Bana bu beceriyi oluşturacak bir prompt üret."
- Bölüm ekleme (kısa olan daha iyi çalıştı): "Açılış sayfamın altına bir hizmetler bölümü oluşturmak istiyorum. Web sitesinin tasarım stiliyle aynı olmasını ve oldukça yaratıcı olmasını istiyorum." (Alternatif "bunun için bana bir prompt oluştur" — küçük bölümlerde Claude'un ürettiği prompt "çoğu zaman aşırıya kaçıyor".)
- Mega menü + sayfa: "Solace Site Builder'ı kullanarak oldukça yaratıcı bir mega menü oluştur. İçine görseller ve video ekle. Bunun için Solace Site Builder'ı kullan. Ayrıca 'Ödüller' adında bir sayfa oluştur. Şirketimizin ödüllerini sergileyen oldukça yaratıcı ve dekoratif bir sayfa olmasını istiyorum. Yerel Elementor öğelerini kullandığından emin ol. Yardıma ihtiyacın olursa becerileri (skills) referans al."
- Tam site (prompt ürettirme): "orientutorial.com için üst düzey bir emlak web sitesi yapmak istiyorum. Bu web sitesini oluşturmam için bana bir prompt oluştur. Bir ana sayfa, hakkımızda sayfası ve iletişim sayfası istiyorum. Oldukça dekoratif, özel bir üst bilgi ve alt bilgi oluştur ve Solace Site Builder'ı kullandığından emin ol." (çıkan prompt kopyalanıp yeniden çalıştırılıyor; "Claude prompt yazmada insanlardan çok daha iyi")
- SEO: "Rank Math eklentisini kur. Web sitemizin tüm sayfalarını ve gönderilerini optimize et. Site haritasını oluştur. İç bağlantılar ekle. İşletmemiz için iki blog yazısı oluştur. Ayrıca bu anahtar kelimelerin her biri için ek sayfalar oluştur, içerik ekle ve bunları çok bilgilendirici yap. Google'ın faydalı içerik (helpful content) güncellemeleriyle uyumlu olduğundan emin ol. İçeriği bilgilendirici hale getir."
- Sıfırlama: "Web sitemizdeki tüm sayfaları, gönderileri, üst bilgi ve alt bilgi şablonlarını sil; her şeye sıfırdan başlamak istiyorum." (özet)
- ChatGPT site görseli: "Bana Nashville, Tennessee'de oldukça yaratıcı bir emlak web sitesinin ekran görüntüsünü oluştur. Tasarımı yaratıcı yap, ödül kazanan tarzda ve lüks tarzda olsun."
- ChatGPT görseller: "Mükemmel. Şimdi ekran görüntüsündeki her bir görseli benim için oluştur. Bu ekran görüntüsündekiyle birebir aynı görseli istiyorum ve yüksek çözünürlüklü ve kaliteli olduklarından emin ol. Ancak çok büyük olmamaları için görselleri sıkıştır." → "Bu görselleri indirebilmem için bir ZIP dosyasına koy."
- ChatGPT arka planlar (D bölümü): "Web sitem için bir arka plan oluştur." / "Bana bu web sitesi ile uyumlu oldukça yaratıcı bir görsel verebilir misin?" / "Web sitesinin geri kalanı için mevcut tasarımına en uygun arka planları verebilir misin?" / "Her bir arka plan görselini yüksek kalitede ver ama web sitemin hızlı çalışması için görselleri sıkıştır."
- ElevenLabs video: "Bu arka planla animasyonlu bir video oluşturmanı istiyorum. Çok detaylı ve dekoratif olduğundan emin ol."
- Claude Design HTML indirme: "HTML dosyasını indirmek istiyorum ama HTML indirmesine görselleri dahil etme."
- HTML kopya prompt'u ve HTML→Elementor dönüştürme prompt'ları (ücretsiz + Pro): metinleri videoda okunmuyor; Evernote linkinde. Doldurulan alanlar: web sitesi adı/URL, işletme adı, hizmetler/ne iş yaptığın, hedef anahtar kelimeler. Bilerek "çok spesifik" tutulmamış: "isteminizi çok spesifik tutarsanız düzeltmekten çok daha fazla hata yaratır, biraz esneklik bırakıyorum" (örn. flex wrap'ı prompt'a eklememiş).
- SS ile düzeltme: "Web sitesi bozuk ve hatalı. Bir ekran görüntüsü ekledim. Ayrıca web sitesinin orijinal ekran görüntüsünü de ekledim. Web sitesini düzelt ve orijinal tasarıma göre ayarla. Olabildiğince benzet. Web sitesindeki tüm hataları gider." ("birebir aynı" dersen her şeyi tutturmak için özel CSS ekleyebiliyor — istemiyor.)
- Ek sayfa: "Bana web sitesinin aynı stilini ve tasarımını taşıyan bir hakkımızda sayfası oluştur. Ayrıca bunu menüye ekle."
- Skill metinleri: videoda içerikleri gösterilmiyor (Google Drive'da ZIP/dosya).

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- **Önce görsel olarak tasarla, sonra koda çevir**: ChatGPT'ye "yaratıcı, ödül kazanan, lüks" site ekran görüntüsü çizdirip onu referans almak; sonra aynı sahnelerin görsellerini tek tek ürettirmek (tutarlı, gerçekçi fotoğraflar — "yapay zeka olduğunu anlayamıyorum").
- Claude Design'a SS + görseller verirken **tasarım sistemini kapatmak** (referansı bozmasın).
- Görselleri **yüksek kalite ama sıkıştırılmış** istemek (hız).
- Şablon kişiselleştirmede sadece 2 AI görseli (arka plan + kahraman kişi) bile sitenin hissini kökten değiştiriyor; tüm bölümlere uyumlu arka planlar.
- **Animasyonlu video hero**: görsel → ElevenLabs (Seedance/Gemini) → "çok detaylı ve dekoratif" → Elementor arka plan videosu.
- Claude bölüm üretirken **özel CSS ile animasyon** ekliyor, aynı renk/stili koruyor.
- Elementor tarafında: Maske, giriş animasyonları, flexbox gap, VH yüksekliği, kutu gölgesi + kenarlık (Claude'un Ödüller sayfasında beğendiği detaylar), hover animasyonları.
- Prompt'ta "oldukça yaratıcı", "dekoratif", "aynı tasarım stilinde" kalıpları.
- Hataları sözle değil **"bozuk SS + orijinal SS"** ile düzelttirmek; "olabildiğince benzet" (birebir değil).

## Hatalar ve çözümleri
- Hostinger eklentileri can sıkıcı → hepsini sil, önbellek kalsın.
- Atomic Editor kafa karıştırıcı → kapat. Claude eski inner section kullanmasın → Container aktif.
- Claude tarayıcı sürümü MCP'de çalışmıyor → Claude Desktop.
- Yetkilendirmede WP'den çıkış yapmışsan kimlik doğrulama sorunu → önce WP'ye giriş yap.
- Her araçta onay sorması zaman kaybı → "Her zaman izin ver".
- Kendi skill'in ilk denemede çalışmaz → test et, SS + açıklama ile düzelttir (≈5 saat).
- Claude'dan küçük bölüm için prompt ürettirmek aşırıya kaçıyor → kısa prompt yaz.
- Claude bölümü "açılış sayfasının altına" değil bir sonraki bölüme koydu → önemsiz.
- Kazara HTML widget kullanma riski → prompt'ta "yerel/native Elementor öğelerini kullan" de.
- Ödüller sayfasında hizalama (sayı metnin üstüne gelmemiş), unutulan ikonlar → Elementor'da yön yatay yap, ikonu ekle.
- Mega menüye kaynağı belirsiz video koymuş → kontrol et.
- Tam site üretiminde bazı bölümler bozuk; iletişim butonu rengi → elle.
- Rank Math snippet'inde ajans adı yanlış → elle düzelt.
- Görselli HTML 7–10 MB → aktarım çok uzun → HTML'yi görselsiz indir, görselleri Medya'ya elle yükle; ZIP'i doğrudan sürükleme, önce çıkar.
- Prompt'u Claude kutusunda düzenleyememe → not defterinde doldur.
- Aktarım sonrası en yaygın hata: öğeler flexbox içinde doğru sarmalanmamış → container'da **No wrap** (ya da wrap) ayarı. Bilerek prompt'a eklenmiyor (fazla spesifik prompt daha çok hata).
- Nereden çıktığı belli olmayan mavi buton / yanlış renk → Stil'den global renk.
- Header arka planı sadece hover'da → Stil > arka plan rengi.
- Footer'da görsel tam genişliğe zorlanınca alt satıra kayma → No wrap.
- Sözle tek tek düzeltme istemek yeni hatalar doğuruyor → SS karşılaştırmalı tek seferlik düzeltme prompt'u.
- "Birebir aynı" dersen ekstra CSS ekliyor → "olabildiğince benzet".
- Popup tetikleyicilerini Claude ayarlayamıyor → Elementor Pro'da Görüntüleme koşulları + Tetikleyiciler (kaydırma, 3 sn) + Gelişmiş (gösterim sayısı) elle.
- Ücretsiz sürümde kalan wrap/footer sorunları NovaMira Pro ile çözülmüş.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Müşteriye satış fiyatı yok. Sadece: "komut vererek harika siteler oluştururken siz müşterilerle ilgilenebilir veya daha üretken işlere odaklanabilirsiniz"; bu yöntemle yapılan sayfaların Google'da sıralamaya girdiğini söylüyor.
- Maliyetler: Hostinger Unlimited (48 ay < 200 $; 12 ay seçince ücretsiz alan adı; Darrel10 +%10; 30 gün iade). NovaMira ücretsiz, NovaMira Pro opsiyonel (%10 indirim kodu). Solace + Solace Extra + Elementor ücretsiz; Elementor Pro opsiyonel (mega menü, popup, tema oluşturucu — "ciddi kullanacaksanız mantıklı"). Claude Design ücretli (ücretsiz Claude ile de yapılabilir). Kredi tasarrufu için tek sayfa üretiyor ("tüm kredilerimi tüketmek istemiyorum").

## Bizim fabrikaya alınacaklar (somut)
1. **"Düzenlenebilir site" paketi = WP hattı**: bizim premium HTML'imizi (Claude Code/Claude Design) üret → NovaMira MCP + "HTML to Elementor" skill + dönüştürme prompt'u → native Elementor. Müşteri metin/foto/buton/sayfa çoğaltmayı kendisi yapar. Statik HTML'i "sadece vitrin, bakımı bizde" paketi olarak tut. Karar kriteri: müşteri sık içerik değiştirecek mi, blog/SEO sayfası istiyor mu, form/popup/mega menü lazım mı → evetse WP.
2. **Bağlantı yöntemi olarak video 2'yi standartlaştır**: NovaMira > Yapılandırma > AI yetenekleri → "Kimlik doğrulama" → Claude Desktop > Ayarlar > Connectors > Custom connector (ad + uzak MCP URL) → WP'de oturum açıkken Yetkilendir → "Her zaman izin ver". (Video 1'deki claude_desktop_config.json + uygulama şifresi yöntemi yedek.) Teslimden sonra bağlayıcıyı kaldır / yetkiyi iptal et.
3. **Kendi "HTML→Elementor" skill'imizi yaz** (Darrel'in skill'i Drive'da; biz kendi kurallarımızla): native öğeler, Container/flexbox, global renk + font'u Site Ayarları'na yaz, özel CSS sınıfı ekleme, görselleri Medya'dan eşle, header/footer ücretsiz builder (Solace Site Builder veya XPro), wrap kuralları. Hataları SS ile besleyerek iteratif geliştir (≈5 saat bütçe).
4. **Görselsiz HTML + Medya'ya ayrı yükleme** kuralı: HTML'de görsel dosya adları sabit; görseller önce sıkıştırılıp (WebP) WP Medya'ya; sonra dönüştürme.
5. **WP hazırlık checklist'i**: Hostinger eklentilerini sil (cache kalsın) → Solace + Solace Extra (veya Astra + XPro) → Elementor (Atomic kapalı, Container aktif) → NovaMira → görseller Medya'da → 2 skill yüklü.
6. **Aktarım sonrası QA**: wrap/no-wrap taraması, global renkler, header arka planı (hover-only tuzağı), footer, ikon eksikleri, Rank Math başlık/açıklamalarında işletme adı, mobil. Düzeltmeyi **"bozuk SS + orijinal SS + 'olabildiğince benzet'"** prompt'uyla tek seferde yaptır.
7. **Ek sayfalar ve SEO'yu MCP üzerinden üret**: ilk sayfa/tasarım aktarıldıktan sonra Claude'a "aynı stil ve tasarımda X sayfası + menüye ekle"; Rank Math + site haritası + iç linkler + yerel anahtar kelime sayfaları + blog (helpful content vurgusu) → yerel işletme SEO paketi olarak satılabilir ek hizmet.
8. **Görsel üretim zinciri**: (a) ChatGPT'ye "ödül kazanan, lüks [sektör] [şehir] sitesi SS'i" → (b) aynı görselleri tek tek, yüksek kalite + sıkıştırılmış, ZIP → (c) Claude Design'a SS + görseller (tasarım sistemi kapalı) → HTML. Hero için ElevenLabs görsel→video (Seedance), Elementor arka plan videosu.
9. **Elementor Pro gerektiren işler** (popup, mega menü, tema oluşturucu) için popup tetikleyicilerini her zaman elle kur; Pro lisans maliyetini teklife ekle. Ücretsiz hatta mega menü/header-footer için Solace Site Builder yeterli.
10. Prompt'ları Claude kutusunda değil, dosyada doldur (müşteri brief formundan otomatik doldurulabilir şablon: site URL, işletme adı, hizmetler, konum, anahtar kelimeler).
