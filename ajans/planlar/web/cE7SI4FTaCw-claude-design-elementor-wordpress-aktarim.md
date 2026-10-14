# 2026'da Claude Design ve Elementor Kullanarak Gerçekten Bir WordPress Web Sitesi Oluşturdum — Darrel Wilson Türkçe'de — 45 dk — https://www.youtube.com/watch?v=cE7SI4FTaCw

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).

## Tek paragraf: ne yapıyor, sonuç ne
Claude Design'da (sadece Pro/Max, tarayıcı sürümü) önce hazır marka tasarım sistemlerini (designmd.ai ve getdesign.md'den kopyala-yapıştır) tasarım sistemi olarak kaydediyor, sonra kendi Evernote prompt'uyla (işletme adı + hizmetler + konum + SEO anahtar kelimeleri + tasarım yönü + sayfa listesi, "önce HTML") 5-6 sayfalık bir emlak sitesi (Wilson Estates, Las Vegas) üretiyor; alternatif olarak ThemeForest temalarının tam sayfa ekran görüntüsünü referans veriyor. Ardından Hostinger'da WordPress kuruyor, Astra + Elementor + WPForms + XPro (ücretsiz header/footer builder) + NovaMira (WordPress'i MCP ile Claude'a bağlayan eklenti) kuruyor, Elementor ayarlarını (Atomic Editor kapalı, Container aktif) düzeltiyor, Node.js kuruyor, Claude Desktop'ın config dosyasına NovaMira JSON'unu yapıştırıp bağlıyor. Claude Design projesini ZIP indirip Claude Desktop'a yükleyerek "WordPress'e dönüştür, sayfaları oluştur, header/footer'ı XPro ile yap" prompt'unu çalıştırıyor; ~15 dakikada site **native Elementor öğeleriyle** (buton, metin modülleri — HTML bloğu değil) canlı alan adına aktarılıyor. Sonuç demoya çok yakın; header/footer, global renk/font atamaları ve AI'ın eklediği CSS sınıfları yüzünden birkaç bozukluk var, bunlar Elementor'da elle hızlıca düzeltiliyor. Ana mesaj: "Claude temeli kurar, ince ayarı sayfa oluşturucu yapar" — müşteriye teslim edilebilir, sürükle-bırak ile düzenlenebilir site.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude Design** — https://claude.ai/design (sadece Pro veya Max planında; tarayıcı sürümü, "Ürünler > Tasarım"; Design Systems sekmesi)
- **designmd.ai** (renk paleti / tasarım sistemi kütüphanesi, "Explore") — https://designmd.ai/explore?page=1
- **getdesign.md** (Binance, BMW, Bugatti, Coinbase, Ferrari, Lamborghini gibi markaların tasarım sistemleri; "copy" butonu) — https://getdesign.md/
- **ThemeForest** (ilham: Ohio teması demosu; kendi sitesinde kullandığı "Essentials" teması, "AI Agency" demosu) — ekran görüntüsü referansı için
- **GoFullPage** Chrome eklentisi (tam sayfa ekran görüntüsü, ~11 milyon kullanıcı)
- **Evernote prompt'ları** (artık Tiberium'a taşındı): https://tiberium.app/p/2FwUcExQQqN2 — (1) site oluşturma prompt'u (ekran görüntülü + ekran görüntüsüz sürüm), (2) WordPress'e aktarma prompt'u
- **Hostinger** (hosting + ücretsiz alan adı + WordPress kurulumu): https://darrelwilson.com/hostinger-turkish — kupon kodu **Darrel10** (+%10)
- **WordPress eklentileri/teması:** Astra (tema), Elementor (ücretsiz), WPForms (iletişim formu), **XPro** ("Elementor için ücretsiz XPro" header/footer builder + istediği "XPro Elementor Addons"), **NovaMira** (WP dizininde yok, siteden ZIP): https://www.darrelwilson.com/novamira
- **Node.js** (nodejs.org; Claude bazen siteyi React ile ürettiği için dışa aktarmada hata olmasın diye)
- **Claude Desktop**: https://claude.com/download (MCP bağlantısı için şart; tarayıcı sürümü WordPress ile düzgün senkronize olmuyor)

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
**A. Tasarım sistemi (Claude Design)**
1. claude.ai → Tasarım (Design) → üstte **Design Systems** → Oluştur.
2. designmd.ai/explore'da bir palet seç (örn. "Genesis") veya getdesign.md'de bir marka (örn. Lamborghini) → "panoya kopyala".
3. Claude Design'da sisteme anlamlı isim ver ("Mavimsi Genesis tasarım sistemi", "lüks siyah-sarı") → **Diğer notlar (Other notes)** alanına kopyalananı yapıştır → sağ üst "Oluşturmaya devam et" → Oluştur. Birkaç dakikada UI kit çıkıyor (marka renkleri, birincil/hover/ikincil, butonların boyut/varyantları, tipografi) → **Yayınla**.
4. Müşteri tipine göre birden fazla tasarım sistemi tutulabilir.

**B. Siteyi üretme (3 yol)**
5. Yeni prototip → isim ("Wilson emlak") → tasarım sistemi olarak Genesis'i seç → Oluştur.
6. Yol 1: basit prompt — "bu tasarım ve stillerle 5 sayfalık bir emlak web sitesi oluştur" → iyi ama sade.
7. Yol 2: kendi Evernote prompt'u (ekran görüntüsüz sürüm) → daha fazla özgün stil/animasyon.
8. Yol 3: prompt + ekran görüntüsü → ThemeForest demosunu GoFullPage ile tam sayfa çek, Claude'a yükle; birebir kopyalamıyor, ilham alıyor.
9. Prompt'u önce Word/Evernote'ta doldur (Claude'un kutusuna yapıştırınca içi görünmüyor/düzenlenemiyor): şirket adı, hizmetler, **konum**, **SEO anahtar kelimeleri**, tasarım yönü (örn. "Kurumsal modern"), sayfalar (1–6: ana sayfa, hakkımızda, hizmetler, portföy…), gereksinimler (önce HTML, mobil uyumlu, SEO optimize, görseller kullan, grid tabanlı, benzersiz, footer'da telif yılı + hızlı linkler + sosyal linkler).
10. Tasarım sistemi seçili mi kontrol et (kapanırsa "Design system" tıklayıp yeniden ekle) → prompt'u yapıştır → çalıştır. ~10 dk'da site hazır (emlak: hero sağda metin+görsel, ilanlar, referanslar, CTA, footer, hizmetler sayfasında anahtar kelimeler, blog/günlük sayfası).

**C. Hosting + WordPress (Hostinger)**
11. darrelwilson.com/hostinger-turkish → Başla → **Business ("kurumsal") planı** önerisi (50 siteye kadar, NVMe; Premium = 3 site + SSD; Cloud Startup = çok siteli olanlar için) → süre (48 ay ≈ 200 $ / 4 yıl en ucuz; yeni başlayana 12 ay ≈ 50 $/yıl + ücretsiz alan adı) → kupon **Darrel10** → hesap → fatura → ödeme (kart / PayPal / Google Pay).
12. Kurulum sihirbazı: "Kendim veya işim için" → "Web sitesi oluştur" → WordPress giriş bilgileri (not al) → ücretsiz alan adı → alan adı sahip bilgileri → hedef kitle ülkesi → WordPress kuruluyor → "WordPress'e git".
13. E-postadan **iki doğrulama**: Hostinger hesabı + alan adı (30 gün içinde doğrulanmazsa iade edip hesabı kapatıyorlar). Doğrulanana kadar geçici alan adı kullanılır.
14. Giriş: `alanadi.com/wp-admin` veya Hostinger → Web siteleri → **WP Admin** (şifresiz).

**D. WordPress hazırlığı (eklentiler + ayarlar)**
15. Görünüm > Temalar > **Astra** kur + etkinleştir (5 temayı A/B test etmiş; tema özelleştiricisiyle içe aktarma çok hata veriyor).
16. Eklentiler > Yeni ekle: **Elementor** (kurulum sihirbazını "atla"), **WPForms**, **XPro** (+ istediği XPro Elementor Addons). XPro'nun nedeni: header/footer'ı tema değil Elementor yapsın, Elementor Pro gerekmesin.
17. **NovaMira**: siteden ZIP indir → Eklenti ekle > Eklenti yükle > dosya seç > Şimdi kur > Etkinleştir.
18. Ayarlar > Elementor: **Atomic Editor'ı devre dışı bırak** ("okudum ve anladım"); **Özellikler (Features) > Container = Aktif** (Claude eski "inner section" yerine flexbox kullansın) → Değişiklikleri kaydet.
19. **Node.js** kur (nodejs.org, "evet-evet" kurulumu). Gerekçe: Claude bazen bölümleri React ile kuruyor, Node yoksa dışa aktarmada hata; Claude Code kullanıyorsan zaten lazım.

**E. Claude Desktop ↔ WordPress (MCP) bağlantısı**
20. WP panel > NovaMira > **Yapılandırma** → "bu site için AI yeteneklerini aç" kutusunu işaretle → Ayarları kaydet.
21. **Uygulama şifresi oluştur**. Aşağıda "AI istemci bağlantı komutu" çıkıyor; onu kullanmıyor (Claude ile iyi çalışmıyor dediği için), en alttaki **"İstemci için JSON yapılandırması"** → **Claude Desktop** → kodu kopyala.
22. Claude Desktop'ı indir/kur (claude.com/download). Ayarlar → **Geliştirici (Developer)** → **Yapılandırmayı düzenle (Edit Config)** → config dosyasını çift tıkla aç → içindekini sil → kopyalanan JSON'u yapıştır (URL, kullanıcı adı, şifre içeriyor) → **Kaydet** ("Farklı kaydet" değil, yoksa kopya oluşur).
23. Claude'u tamamen kapat (sağ tık > çıkış) ve yeniden aç → Ayarlar > Geliştirici'de NovaMira bağlı görünür → sohbette "bağlı mısın?" diye sor, doğrular.

**F. Aktarım**
24. Claude Design → **Paylaş (Share)** → **"Projeyi ZIP olarak indir"**.
25. Açıklamadaki ikinci Evernote prompt'unu al, içindeki **site URL'sini kendi sitenle değiştir**.
26. Claude Desktop'ta ZIP'i yükle + prompt'u yapıştır → çalıştır → izin sorularına "Her zaman izin ver". ~15 dk sürdü. Claude "bitti ama XPro header/footer'da sorun var" dedi.

**G. Elementor'da ince ayar**
27. Sayfada "Elementor ile düzenle" → bölümler/öğeler sürükle-bırak, çoğalt, sil, stil kopyala-yapıştır.
28. Yan yana dizilmeyen butonlar: "kaydırma (wrap)" kapalı mı bak, genişliği artır; **Gelişmiş > CSS sınıfları**'nda AI'ın eklediği sınıfı sil → düzelir.
29. **Site Ayarları > Global renkler**: Claude birincil/ikincil rengi yanlış atamış (buton rengi metne gitmiş) → buradan düzelt. **Global yazı tipleri > Birincil** → ana fontu ata.
30. Header/footer: XPro → kalem → "Tüm web sitesinde göster" → yayınla/güncelle (footer için de aynı). Header'ı XPro'nun Elementor düzenleyicisinde aç → menü WP panelindeki menüden gelir; buton arka planını global ana renge çek, rastgele seçilmiş fontu değiştir, buton metni "Görüşme ayarlayın" + link alanına sayfa adı yazınca ("iletişim") otomatik bağlanır, menü genişliği %70, logo için Görsel veya "Site Logo" öğesi. XPro > Kayıtlı şablonlar'dan da backend'den düzenlenebilir.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Yol 1 basit prompt (Claude Design'a): "Bu tasarım ve stillerle 5 sayfalık bir emlak web sitesi oluştur."
- Site oluşturma prompt'unun tam metni videoda okunmuyor; Tiberium/Evernote'ta (https://tiberium.app/p/2FwUcExQQqN2). Videoda gösterilen alanlar: işletme adı (Wilson Estates), hizmetler, konum, **hedeflenecek SEO anahtar kelimeleri** ("Las Vegas emlak", "Las Vegas'ta emlakçı", "Darrel Wilson emlakçı", "Las Vegas mülk hizmetleri"), **tasarım yönü** (seçeneklerden biri bırakılıyor; örnek: "Kurumsal modern — temiz, yapılandırılmış, güvenilir; finans, hukuk ve B2B hizmetleri için iyi"), **oluşturulacak sayfalar** (1–6: ana sayfa, hakkımızda, hizmetler, portföy…), **gereksinimler** (önce HTML; mobil uyumlu; SEO optimize; görseller kullan), **tasarım içermeli** (grid tabanlı düzen; benzersiz; altta telif yılı, hızlı bağlantılar ve sosyal medya linkleri olan footer). Ekran görüntülü ve ekran görüntüsüz iki sürümü var.
- Aktarım prompt'u (Tiberium/Evernote'ta; videodaki özeti): (1) Claude'a web sitesinin bir kopyasını (ZIP) verdiğimizi bildir, (2) bu siteyi WordPress için dönüştür, (3) sadece site için sayfaları oluştur, (4) header ve footer için XPro eklentisini kullan, (5) site URL'si = [kendi alan adın].
- Bağlantı testi: "Bağlı mısın? WordPress'e bağlı mısın?"

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- **Hazır profesyonel tasarım sistemi** (getdesign.md'de büyük markaların; designmd.ai'de paletler): renkler, yüzey/metin/semantik renkler, tipografi, butonlar (birincil/ikincil), kartlar, inputlar, çipler, kutu gölgeleri — hepsi tek kopyala-yapıştırla Claude Design'a. Müşteri tipine göre birden fazla sistem.
- **Ekran görüntüsü yöntemi**: ThemeForest'taki en iyi tasarımcıların temalarından (Ohio, Essentials) GoFullPage ile tam sayfa SS → Claude ilham alıyor, kopyalamıyor.
- Prompt'a **tasarım yönü** ve grid tabanlı/benzersiz düzen isteği; kendi prompt'u "özgün stil ve animasyonlar" getiriyor.
- Claude bazen tasarım sistemini ezip kendi seçimini yapıyor → "tasarım sistemini kullan" diye hatırlat.
- **Görseller**: Claude ~%50 ihtimalle doğru görsel buluyor; bulamazsa kendi görsellerini ekle.
- Aktarımdan sonra **global renk + global font** atamasını kontrol et (en büyük hata kaynağı).

## Hatalar ve çözümleri
- Claude Design'da prompt yapıştırınca kutunun içi açılıp düzenlenemiyor → prompt'u önce Word/Evernote'ta doldur.
- Claude tasarım sistemini ezebiliyor → tasarım sistemini kullanmasını hatırlat.
- Görseller tutarsız (~%50) → kendi görsellerini ekle.
- Tema özelleştiricisiyle içe aktarma → 5 temada da çok hata → Astra + Elementor + XPro eklentisi yolu.
- Claude eski Elementor "inner section" kullanıyor → Elementor Features'ta Container'ı aktif et (flexbox'a zorla). Atomic Editor kapalı olsun.
- Claude bazı bölümleri React ile üretebiliyor → dışa aktarma hatası → Node.js kur.
- NovaMira'nın "AI istemci bağlantı komutu" Claude ile iyi çalışmıyor → JSON config + Claude Desktop config dosyası. Tarayıcı Claude'u WP ile senkronize olmuyor → Desktop şart. Config'i "Farklı kaydet" ile kaydetme. Kaydettikten sonra Claude'u tamamen kapatıp aç.
- Aktarımda izin soruları → "Her zaman izin ver".
- XPro header/footer'da sorun (Claude kendisi bildirdi), header yanlış, butonlar yan yana değil, metin biraz büyük → Elementor'da elle düzelt.
- Öğeler beklendiği gibi davranmıyor → Gelişmiş > CSS sınıfları'ndaki AI eklemesi sınıfı sil.
- Buton rengi metinlere uygulanmış / genel stil yanlış → Site Ayarları > Global renkler (birincil/ikincil yer değiştirmiş).
- Header'da rastgele font → değiştir; logo metin düzenleyiciyle yapılmış → Görsel veya Site Logo öğesi.
- Hostinger doğrulama e-postaları 30 gün içinde tıklanmazsa hesap kapanıyor.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Müşteriye satış fiyatı verilmiyor. Sadece: canlı alan adında, sürükle-bırak ile düzenlenebilir olduğu için "artık müşterilere gönderebiliriz", çalışma süresini takip eden eklenti eklenebilir, "gerçek ödeme yapan müşterilere teslim etmeyi daha ideal hale getiriyor"; Claude içinde sürekli revizyon prompt'u kredi ve zaman yiyor, küçük düzeltmeler sayfa oluşturucuda çok daha verimli.
- Maliyetler: Claude Pro/Max (Claude Design için şart); Hostinger Business — 48 ay ≈ 200 $, 12 ay ≈ 50 $/yıl (ücretsiz alan adı dahil), Darrel10 ile +%10; 30 gün para iade. NovaMira ücretsiz (yükseltilebilir). Elementor, Astra, WPForms, XPro ücretsiz.

## Bizim fabrikaya alınacaklar (somut)
1. **İki teslim hattı kararı**: (a) "Vitrin/premium görsel" müşteri → statik HTML (bizim mevcut hat); (b) "Kendim düzenlemek istiyorum" müşteri → bu video zinciri: Claude Design/Claude Code HTML → NovaMira MCP → native Elementor. Teklif aşamasında müşteriye "metin/foto/buton değiştirmeyi kendin mi yapacaksın?" sorusu ekle; cevap evetse WP hattı.
2. **WP hazırlık kontrol listesi (script gibi uygula)**: Astra etkin; Elementor + WPForms + XPro (+Addons) + NovaMira; Elementor > Atomic Editor KAPALI, Features > Container AKTİF; Node.js kurulu; NovaMira > AI yetenekleri açık + uygulama şifresi; Claude Desktop config'e JSON (veya video 2'deki custom connector) ; "bağlı mısın?" testi.
3. **Aktarım prompt şablonumuz** (kendi metnimizi yaz): "Ekteki ZIP sitenin kopyası. WordPress/Elementor için dönüştür; YALNIZCA native Elementor öğeleri kullan (HTML widget yok); Container/flexbox kullan; sadece bu sayfaları oluştur: […]; header/footer'ı XPro ile oluştur ve tüm sitede göster; global renkleri [birincil=…, ikincil=…, metin=…] ve global fontları [başlık=…, gövde=…] Site Ayarları'na ata; öğelere özel CSS sınıfı ekleme; site URL'si: […]".
4. **Aktarım sonrası QA kapısı**: (i) Navigator'da HTML widget var mı, (ii) Site Ayarları global renk/font doğru mu, (iii) Gelişmiş > CSS sınıfları boş mu, (iv) flex wrap/yan yana butonlar, (v) header/footer "tüm sitede" ve yayında mı, (vi) logo Site Logo öğesi mi, (vii) mobil.
5. **Tasarım sistemi kütüphanesi**: getdesign.md / designmd.ai'den sektör başına 3-4 sistem seçip (lüks, kurumsal, sıcak/yerel esnaf…) Claude Design'da kalıcı tasarım sistemi olarak kaydet; brief'te müşteriye seçtir.
6. **Brief alanları** (prompt'a girmeden doldurulacak form): işletme adı, hizmetler, konum (şehir/ilçe), 4-6 yerel SEO anahtar kelime ("Kadıköy diş kliniği" tarzı), tasarım yönü, sayfa listesi, footer içeriği. Prompt'u Claude kutusunda değil, dosyada doldur.
7. **Referans SS yöntemi**: ThemeForest/Awwwards demosu → GoFullPage tam sayfa → Claude Design'a ek (kopyalamaz, ilham alır).
8. **Hosting**: Çok müşteri için tek Hostinger Business/Unlimited hesabında (50+ site) WP'leri topla; alan adı + hesap doğrulama e-postalarını teslim checklist'ine koy.
9. **Güvenlik notu**: NovaMira geliştirme/staging için tasarlanmış ve kuruluma güçlü erişim veriyor (açıklamada) → canlı müşteri sitesinde bağlıyken yedek al, teslimden sonra uygulama şifresini iptal et / AI yeteneklerini kapat.
