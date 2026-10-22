# Google Play yayın rehberi: Türkiye'den dünyaya (2026)
Araştırma kolu: play-yayin

Tarih: 21 Ekim 2026 (ilk tur) ve 22 Ekim 2026 (ikinci tur: doğrulama,
eksik kalanlar, gerçek hayat hikâyeleri). Resmi sayfalar bu iki günde
yeniden okundu; sayıların yanındaki tarih kaynağın kendi tarihidir.

## Kısa özet

- Yeni kişisel hesapta Play'e çıkmanın en uzun kısmı kapalı test:
  en az 12 test kullanıcısı, 14 gün kesintisiz. Sonra "üretim erişimi"
  başvurusu; inceleme genelde 7 gün veya daha az. [resmi]
  https://support.google.com/googleplay/android-developer/answer/14151465
- Resmi sayfa yalnız "13 Kasım 2023'ten sonra açılan kişisel hesapları"
  sayıyor. Kuruluş hesabının muaf olduğu resmi sayfada yazmıyor; iki
  ticari kaynak muaf diyor. [çıkarım]
- Bizim Play sürümümüz para kazanıyor (destek planları). Kişisel hesapta
  para kazanınca TAM ADRESİN mağazada herkese açık görünür. [resmi]
  https://support.google.com/googleplay/android-developer/answer/13628312
- Hedef API: 31 Ağustos 2026'dan beri yeni uygulamalar API 36 hedeflemeli.
  Bizde targetSdk 36. AAB zorunlu. Billing 8.0.0 şimdilik yeterli. [resmi]
- Bildirim dinleyiciye ÖZEL bir Play kuralı yok (politika metninde
  arattım). Ama genel kurallar tam geçerli: belirgin uygulama içi açıklama
  + onay, gizlilik politikası (mağazada VE uygulamada), doğru Veri
  güvenliği formu, marka/meta veri kuralları. [resmi]
- Veri güvenliği formunda "toplamak" = veriyi cihazdan dışarı iletmek.
  Yalnız cihazda işlenen veri beyan edilmez. Yani Sende için doğru cevap
  büyük olasılıkla "veri toplanmıyor". RELEASE.md taslağı ("Evet,
  Messages toplanıyor") bununla çelişiyor; düzeltilmeli. [resmi]
  https://support.google.com/googleplay/android-developer/answer/10787469
- Play Protect, bildirim dinleyiciyi "yüksek riskli" sayıyor ve seçili
  pazarlarda internetten (tarayıcı, mesaj, dosya yöneticisi) sideload
  edilen bu tür uygulamaların kurulumunu engelliyor. Türkiye'nin bu
  pazarlarda olup olmadığı DOĞRULANAMADI. Play ana yol olmalı. [resmi]
  https://developers.google.com/android/play-protect/warning-dev-guidance
- Android "geliştirici doğrulaması": 30 Eylül 2026'da yalnız Brezilya,
  Endonezya, Singapur, Tayland'da başladı; Türkiye ilk dalgada yok;
  dünya geneli 2027. Play'e yayın yapan zaten doğrulanmış sayılıyor. [resmi]
  https://developer.android.com/developer-verification
- Önemli iç risk: sideload APK'nın imzası herkese açık bir test anahtarı
  (sideload-test.jks). Müşterilere dağıtılacak sürüm için uygun değil. [ölçüm]
- Türkiye'de satıcı kaydı ve TRY destekleniyor; KDV'yi Google topluyor.
  Stopaj/şahıs şirketi/istisna konusunda kaynaklar çelişiyor; mali müşavir
  şart. [resmi + ticari]
- İlk sürümde aşamalı yayın (staged rollout) kullanılamıyor; güncellemelerde
  kullanılır. Ülke seçerek (önce Türkiye) başla. [resmi]
- Hesabı "ücretsiz uygulama" olarak oluştur: ücretsiz uygulama sonradan
  ücretli yapılamaz. Bizim plan (ücretsiz + destek planları) buna uyuyor. [resmi]

### İkinci turda (22 Ekim 2026) netleşen / eklenen

- Bildirim dinleyici + WhatsApp geçmişi: Temmuz 2018'de WhatsApp'ın
  avukatları, Android'in standart Notification Listener ve Direct Reply
  API'sini kullanan Can't Talk ve DirectChat'e dava tehdidiyle mektup
  gönderdi; Can't Talk Play'den kalktı. WhatsRemoved+ da Play'den atıldı,
  WhatsApp bildirimi desteğini bırakınca döndü (tek kaynak). Eski olay
  (2018), ama "Meta tarafı" riskini gösteriyor. [haber, 3 bağımsız kaynak]
  https://www.xda-developers.com/whatsapp-sends-cease-desist-apps-native-android-api/
  https://lowyat.net/2018/165604/whatsapp-files-cease-desist-orders-against-apps-that-dont-use-its-apis
  https://www.xatakandroid.com/comunicacion-y-mensajeria/whatsapp-empieza-combatir-apps-que-muestran-mensajes-que-hayan-sido-borrados
- 20/B istisnasının 2026 üst sınırı çelişkisi çözüldü: 5.300.000 TL
  (gelir vergisi tarifesinin 4. dilim üst sınırı; Resmi Gazete'de
  31 Aralık 2025 duyuruldu). "6,5 milyon" tahmindi. [haber + ticari]
- Play paket kaydı: 30 Eylül 2026'dan beri tüm Play paketleri kayıtlı
  olmalı, aksi halde Play'den kaldırılır. Yeni uygulamalar otomatik
  kaydedilmeye çalışılır; kimlik doğrulaması şart. [resmi]
  https://support.google.com/googleplay/android-developer/answer/16984799
- Uygulamayı sonra başka hesaba devretmek mümkün ama TEST GRUPLARI
  taşınmaz; kapalı test kullanıcıları yeniden katılmak zorunda kalabilir.
  Destek devir talebine 2 iş günü içinde dönüyor. [resmi]
  https://support.google.com/googleplay/android-developer/answer/6230247
- Kapalı testte "yetersiz etkileşim" reddi gerçek: bir geliştirici iki kez
  reddedilip üçüncüde (yakın takip ve hızlı değişimle) geçti. [forum,
  tek kaynak, yazar kendi aracını satıyor]
- Play TR aramasında Türkçe ek varyantları (geçmişi / geçmişleri) büyük
  oranda aynı sonucu veriyor (ölçtüm, 30 sonucun 21'i ortak). [ölçüm]
- "What's new" sınırı resmi sayfada: dil başına 500 Unicode karakter. [resmi]

## Nasıl araştırdım (yöntem ve güvenilirlik)

- Resmi Google sayfaları: anonim curl ile tam metin indirip okudum.
  WebFetch bazı Play Console sayfalarını kesik veriyordu (vergi, abonelik).
- Topluluk: Hacker News (Algolia arşiv API'si), dev.to, GitHub issue
  sayfaları, Türkçe forumlar. Reddit ve XDA bu ortamdan AÇILMADI (aşağıda).
- Kullandığım etiketler:
  - [resmi]: Google/Android/Meta kendi sayfası, bugün okundu.
  - [ölçüm]: kendi depomuzda ya da Play sayfalarında ben ölçtüm.
  - [blog]/[ticari]: kişisel blog veya hizmet satan site (çıkar çatışması
    olabilir; özellikle "12 tester" satan siteler).
  - [forum]: R10, Technopat, Hacker News, dev.to, B4X, Unity forumu
    yorum ve yazıları. Tek kişilik deneyim; yazar kendi ürününü satıyor
    olabilir (not edildi).
  - [haber]: teknoloji/ekonomi haberi (XDA, Lowyat, Xataka, Bloomberg HT).
  - [çıkarım]: kaynakları birleştirip ben çıkardım.
  - [doğrulanamadı]: kaynağa ulaşamadım ya da kaynaklar çelişiyor.
- İlk turda WebSearch günlük kotası dolmuştu, son bölümlerde yalnız
  doğrudan sayfa okumuştum. İkinci turda (22 Ekim) WebSearch çalıştı; ilk
  turdaki "arama özetinden okudum" notlarının çoğunu sayfanın kendisini
  indirip kapattım. Hâlâ özetten kalanlar ayrıca işaretli.
- Play TR arama ölçümü: kendi komutumla play.google.com/store/search
  (hl=tr, gl=TR) sayfasını anonim indirip sonuçları saydım. Ham sonuç
  scratch klasöründe (tr_search_2026-10-22.json). Kişiselleştirme ve
  deney farkları olabilir; tek günlük, tek konumlu ölçüm. [ölçüm]
- Kod kopyalanmadı. Başkasının koduna bakılmadı; GitHub issue'lar yalnız
  okundu (lisans notu gerekmiyor).
- Bilmediğim şeyi uydurmadım; "Doğrulanamayanlar" bölümünde toplu liste var.

## 1. Tahmini takvim (kişisel hesap yolu)

- Gün 0-3: hesap aç, $25 öde, kimlik + cihaz doğrulaması. Google kimlik
  incelemesinin "birkaç gün" sürebileceğini yazıyor. [resmi]
  https://support.google.com/googleplay/android-developer/answer/14177239
- Gün 3-7: uygulamayı oluştur, App content formlarını doldur, ilk AAB'yi
  iç teste yükle. Ödeme yöntemi doğrulaması 5 güne kadar sürebilir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/13628312
- Gün 7: kapalı testi başlat (15-20 gerçek kişiyle).
- Gün 21: 14 gün doldu. Başvuru ancak bundan sonra anlamlı. [resmi]
- Gün 21-28: üretim erişimi incelemesi ("genelde 7 gün veya daha az"). [resmi]
- Gün ~28-30: üretim. Bugünden sayınca Kasım 2026 sonu. Bu bir TAHMİN [çıkarım].
- Kuruluş hesabı yolu: D-U-N-S numarası 30 güne kadar sürebilir; ek olarak
  organizasyon doğrulaması. Takvim benzer ya da daha uzun olabilir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/13628312

## 2. Geliştirici hesabı: türler, ücret, doğrulama

### 2.1 Türler

- İki tür var: Kişisel ve Kuruluş. İkisi de aynı işlevi görür, ikisiyle de
  para kazanılır. [resmi]
  https://support.google.com/googleplay/android-developer/answer/13634885
- Kuruluş ZORUNLU olanlar: finans ürünleri, sağlık uygulamaları,
  VpnService onaylı uygulamalar, devlet uygulamaları. Sende bunlardan
  değil. [resmi]
  https://support.google.com/googleplay/android-developer/answer/18258653
- Hesap türü kayıt sırasında seçilir; sonradan değiştirilemez deniyor.
  Çare: yeni hesap açıp uygulamayı resmi yolla aktarmak. [resmi]
  https://support.google.com/googleplay/android-developer/answer/14177239
- Uygulamayı başka geliştirici hesabına devretmek (sayfayı tam okudum): [resmi]
  https://support.google.com/googleplay/android-developer/answer/6230247
  - Kullanıcılar, istatistikler, yorumlar, puanlar, abonelikler, içerik
    derecelendirme, mağaza girdisi, App content cevapları ve geliştirici
    doğrulaması için kayıtlı paket adı + anahtarlar uygulamayla gider.
  - Devirden ÖNCE oluşan siparişler eski hesapta kalır (iade orada yapılır).
  - Test grupları (iç, kapalı, açık) TAŞINMAZ. Kapalı test gruplarını yeni
    hesapta yeniden kurarsın; kullanıcılar yeniden katılmak zorunda kalabilir.
  - Hedef hesap kayıtlı ve aktif olmalı; uygulama içi ürün varsa hedefte
    aktif ödeme profili şart. Hedefte farklı varsayılan para birimi varsa
    uygulama içi ürünlü uygulama devirden sonra "yayından kalkar", fiyatları
    doğrulayıp yeniden yayınlarsın. İkisi de Türkiye/TRY ise bu uyarı bize
    uymuyor gibi görünüyor ama sayfa bunu açıkça söylemiyor. [çıkarım]
  - Yeni hedef hesap açarsan 25 USD ücret; eski hesabı kapatırsan destek
    o hesabın kayıt ücretini iade edebiliyor. Destek, devir talebine
    2 iş günü içinde yanıt veriyor.
  - Play App Signing'de hedef hesap yeni yükleme anahtarı ya da yeni imza
    anahtarı isteyebilir (güvenlik kaygısı varsa).
- Hesap sahibi aktarımı: Nisan 2026 duyurusuyla yeni "Hesap Aktarımları"
  politikası geldi: geliştirici hesabının sahipliği yalnız resmi
  "hesap sahibi aktarımı" süreciyle değişebilir; gayriresmi devir
  yasak. (Politika duyurusu 15 Nisan 2026.) [resmi]
  https://support.google.com/googleplay/android-developer/answer/17517561
  Resmi hesap sahibi aktarımı (sayfayı 22 Ekim 2026'da okudum): [resmi]
  https://support.google.com/googleplay/android-developer/answer/16909862
  - Yalnız mevcut sahip başlatır; yeni sahip hesaba önceden kullanıcı
    olarak eklenmiş olmalı ve kimliğini doğrulamalı. Bireysel hesapta yeni
    sahip kimlik doğrulaması için yeni ödeme profili seçer/oluşturur.
  - 7 günlük bekleme (güvenlik tutuşu); bu sürede hesaptaki herhangi biri
    iptal edebilir. Sonra yeni sahip kurulum sihirbazını tamamlar.
  - Hesap TÜRÜ değişmez (kişisel hesap kişisel kalır; tür seçimi sonradan
    değiştirilemez, 14177239). Kişisel hesabı kuruluşa çevirmenin yolu
    yok; çare yeni kuruluş hesabı + uygulama aktarımı. [resmi]
  - Önceki turda "para kazanan bireysel hesap sahip aktarımına uygun değil"
    diye bir arama özeti okumuştum; sayfada böyle bir kısıt YOK. Geri
    aldım. [düzeltme]

### 2.2 Ücret ve kart

- Tek seferlik 25 USD. Visa, MasterCard, American Express; ABD dışında
  Visa Electron. Ön ödemeli (prepaid) kart KABUL EDİLMEZ. [resmi]
  https://support.google.com/googleplay/android-developer/answer/6112435
- Kayıtta ödeme reddedilirse: ödeme profilinde başka yöntem ya da fatura
  adresi dene; olmazsa destek. 48 saat sonra hâlâ işlenmediyse destek. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9023898
- Türkiye'den raporlar: bazı bankaların sanal kartlarıyla 25 USD ödeme
  hata veriyor (Technopat konusu; sayfa bot korumalı, arama özetinden
  okudum). Kartı yurt dışı/online işleme açtırmak, fatura adresini
  eşleştirmek, farklı bir kart denemek önerilmiş. [forum, tek kaynak]
  https://www.technopat.net/sosyal/konu/google-play-gelistirici-hesap-ucreti-odenemiyor.4166529/

### 2.3 Doğrulamalar (kişisel hesap)

- Ödeme profili geliştirici kimliğini temsil eder. Yasal ad ve adres
  buradan gelir ve yayından önce doğrulanmalı. [resmi]
  https://support.google.com/googleplay/android-developer/answer/10841920
- Kişisel hesapta istenen: resmi kimlik belgesi. D-U-N-S gerekmez. [resmi]
  https://support.google.com/googleplay/android-developer/answer/10841920
- Belge kuralları: geçerli (süresi dolmamış), renkli, net, fotokopi
  değil. "Desteklenmeyen belge, doğrulama başarısızlığının ilk nedeni."
  Sahte/değiştirilmiş belge: hesap ve uygulamalar derhal kaldırılır. [resmi]
  https://support.google.com/googleplay/android-developer/answer/15633622
- Belgedeki ad/adres ödeme profiliyle BİREBİR aynı olmalı. Uyuşmazlık
  varsa doğrulama sırasında profil güncellenebilir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/15633622
- Türkiye için KİŞİSEL hesapta kabul edilen belge listesi dinamik bir
  sayfada; metne gelmedi. [doğrulanamadı] Play Console doğrulama akışı
  kendi listesini gösterir. Önce o akışa bak.
- Türkçe sayfanın statik kısmından (22 Ekim 2026) genel çerçeve: [resmi]
  https://support.google.com/googleplay/android-developer/answer/15633622?hl=tr
  - Gerekli belgeler "ödeme profilindeki ülkeye" göre belirlenir.
  - Kuruluş hesabı iki tür belge ister: (1) tescil belgesi: kuruluş/tescil
    belgesi, ticari sicil kaydı örneği, işletme ruhsatı veya vergi
    sertifikası; (2) yetkili temsilcinin fotoğraflı kimliği: pasaport,
    nüfus cüzdanı, sürücü belgesi veya kalıcı ikamet kartı.
  - Kimlik herhangi bir ülkeye ait olabilir; yetkili temsilci yasal yetkisi
    olan kişi (müdür, yasal temsilci vb.).
  - Türkiye'de vergi levhasının "vergi sertifikası" sayılıp sayılmadığı
    sayfada yazmıyor. [doğrulanamadı]
- Cihaz doğrulaması (yeni kişisel hesap): gerçek, root'suz, en az Android
  10 bir telefon; Play Console mobil uygulamasında hesap sahibi olarak
  "Doğrula"; 1 dakikadan kısa. [resmi]
  https://support.google.com/googleplay/android-developer/answer/14316361
- İletişim e-postası/telefonu tek kullanımlık kodla doğrulanır. Telefon
  doğrulaması, kimlik ve cihaz doğrulamasından SONRA yapılabiliyor. [resmi]
  https://support.google.com/googleplay/android-developer/answer/10841920
- Mağazada herkese açık: yasal ad, ülke, geliştirici e-postası. Para
  kazanmaya karar verirsen TAM adres de gösterilir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/13628312
- Ödeme yöntemi doğrulaması (para kazanacaksan): küçük para yatırma
  testi ya da resmi banka belgesi; 5 güne kadar. [resmi]
  https://support.google.com/googleplay/android-developer/answer/13628312

### 2.4 Kuruluş hesabı ve D-U-N-S

- Kuruluş hesabı için D-U-N-S zorunlu; ücretsiz alınır; 30 güne kadar
  sürebilir. Üçüncü taraf "hızlandırma" ücretleri için Google uyarıyor.
  [resmi]
  https://support.google.com/googleplay/android-developer/answer/13628312
- Kuruluşta Google; yasal ad, yasal adres, geliştirici e-postası ve
  geliştirici TELEFONUNU herkese açık gösterir. Ayrıca organizasyon
  web sitesi doğrulaması isteniyor. [resmi]
  https://support.google.com/googleplay/android-developer/answer/13628312
  https://support.google.com/googleplay/android-developer/answer/10841920
- Kuruluş belgeleri: tescil/kuruluş belgesi, ticaret sicil özeti, işletme
  ruhsatı veya "tax certificate" (Türkiye'de karşılığı büyük olasılıkla
  vergi levhası [çıkarım]); artı yetkilinin kimliği.
  Belgedeki ad D-U-N-S profilindeki adla aynı olmalı. [resmi]
  https://support.google.com/googleplay/android-developer/answer/15633622
  https://support.google.com/googleplay/android-developer/answer/14177239
- Türkiye'de D-U-N-S: CRIF Türkiye, Dun & Bradstreet'in tek yetkili
  temsilcisi olduğunu söylüyor. Standart başvuru ~30 iş günü, hızlandırma
  ücretli ~8 iş günü. Şahıs şirketinin D-U-N-S alıp alamayacağı kaynakta
  net değil. [ticari]
  https://workon.com.tr/blog/duns-numarasi-nedir-nasil-alinir-rehber/
- Forum uyarısı (Mart 2025): kurumsal hesap "satan" kişilerin sahte
  belgeyle aldırdığı D-U-N-S numaraları iptal edilip hesaplar askıya
  alınmış. Yazar kendi çıkarını da belirtiyor. [forum, tek kaynak]
  https://www.r10.net/android-ios-uygulama-gelistirme/4393544-kurumsal-hesap-alirken-ve-uretirken-dikkat.html
- Hesap alım-satımı/kiralama resmen yasak; yalnız resmi devir süreci
  serbest. [resmi]
  https://support.google.com/googleplay/android-developer/answer/18258653

### 2.5 Hesabı sağlıklı tutma

- Uyarı, kaldırma ve askıya alma "ceza puanı" sayılır; çok sayıda ya da
  ağır ihlal hesabın kapanmasına yol açar. Google önceden uyarmak zorunda
  değil. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9023898
- Hesap DDA ihlaliyle kapatılırsa bağlantılı hesaplar kalıcı askıya alınır,
  yeni hesaplar ücret iadesiz kapatılır. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9023898
- "Birden çok ret, uygulamanın askıya alınmasına yol açabilir." [resmi]
  https://support.google.com/googleplay/android-developer/answer/15191715
- Pasif hesap kapatma (sayfayı 22 Ekim 2026'da okudum): kayıt ücreti iade
  edilmeden kapanır. Kriter A (uygulamasız): hesap 1 yıldan eski ve hiç
  uygulama incelemeye göndermemiş. Kriter B (uygulamalı): hesap 1 yıldan
  eski, tüm uygulamaların toplam ömür boyu kurulumu 1.000'den az, telefon ve
  e-posta doğrulanmamış VE son 180 günde Play Console kullanılmamış (sayfa
  maddeleri bağlaçsız listeliyor; ben hepsini birlikte aranan koşullar
  diye okudum). Doğrulanmış, canlı bir hesap B kriterine girmez. [resmi]
  https://support.google.com/googleplay/android-developer/answer/11605267
- Bir Hacker News yorumcusu (Ekim 2026) bitmiş uygulaması olduğu hâlde
  hesabının "hareketsizlikten" kapandığını yazıyor. Tek kişilik anlatım. [forum]
  https://news.ycombinator.com/item?id=49917761
- Para birimini değiştirmek için yeni hesap gerekiyor (hesap ülkesiyle
  birlikte para birimi sabitleniyor). Türkiye profili = TRY. Ödeme
  profilinin ülkesinin de değişmediği Google Payments sayfasında yazıyor
  (arama özeti). Sonradan yurt dışı şirkete geçmek için yeni hesap +
  uygulama aktarımı gerekir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9023898
  https://support.google.com/paymentscenter/answer/9037801
- Paket adı silinemez ve yeniden kullanılamaz. İç teste bile ilk yapı
  yüklenince paket adı kilitlenir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9859152
  https://support.google.com/googleplay/android-developer/answer/9845334
- Bizim paket adı com.sende.app. Play'de bu kimlikle YAYINDA uygulama yok
  (sayfa 404). Bu, adın boş olduğunu KANITLAMAZ (taslak uygulama
  görünmez). [ölçüm, doğrulanamadı]
  https://play.google.com/store/apps/details?id=com.sende.app

## 3. Türkiye'ye özgü: vergi, ödeme profili, ticari durum

### 3.1 Google tarafı (resmi)

- Türkiye hem geliştirici kaydı hem satıcı (merchant) kaydı için
  destekli; varsayılan para birimi TRY. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9306917
- KDV: Google, Play Billing'de "merchant of record" olduğu yerlerde ve
  adı geçen ülkelerde (Türkiye dahil) KDV'yi kendisi belirler, toplar ve
  ilgili makama öder. Geliştirici ayrıca hesaplamaz. [resmi]
  https://support.google.com/googleplay/android-developer/answer/138000
- Kesinti (stopaj) sayfasında Türkiye için özel bölüm göremedim. Tek
  geçtiği yer: Brezilya kazançları için vergi mukimlik belgesi kabul
  edilen ülkeler listesi. Türkiye stopajı için Google kuralı
  [doğrulanamadı]. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9384608
  https://support.google.com/paymentscenter/answer/13401799
- Vergi bilgisi Google Payments Center > Ayarlar'dan girilir; "herkesin
  vermesi gerekmez". Google vergi tavsiyesi vermez. [resmi]
  https://support.google.com/paymentscenter/answer/13401799
- Ödeme takvimi: bir ayın siparişleri sonraki ayın yaklaşık 15'inde
  ödenir; bankaya geçmesi birkaç gün sürebilir. Yerel para birimiyle
  ödeme alan satıcılar için asgari bakiye 1 USD karşılığıdır (wire
  transferde 100 USD). Türkiye satırı: kayıt evet, satıcı evet, para
  birimi TRY. [resmi, 22 Ekim 2026'da okundu]
  https://support.google.com/googleplay/android-developer/answer/137997
  https://support.google.com/googleplay/android-developer/answer/9306917
  "TRY'de 20 TL eşik" diyen bir Ekşi Sözlük girdisi var; tarihsiz, tek
  kişi, resmi sayfa farklı söylüyor (1 USD karşılığı). Kullanma. [forum,
  güvenilmez]
- Hizmet bedeli (Türkiye "diğer pazarlar" grubunda): otomatik yenilenen
  abonelikte %15; diğer satışlarda yıllık ilk 1 milyon USD için %15,
  üstü %30. (ABD/AB/Birleşik Krallık için 30 Haziran 2026'dan beri yeni
  oranlar var; Türkiye onlarda değil.) [resmi]
  https://support.google.com/googleplay/android-developer/answer/112622
  "15% katmanına kayıtlı geliştirici" ifadesi geçiyor; kaydın otomatik
  olup olmadığı sayfada net değil. [doğrulanamadı]

### 3.2 Türkiye vergi rejimi (ticari kaynaklar; çelişki var)

- Uyarı: aşağıdakiler vergi danışmanlığı sitelerinden. Hukuki/mali görüş
  değil; mali müşavir şart. İlk turda iki kaynak 2026 üst sınırını farklı
  veriyordu; ikinci turda 5,3 mn TL olarak netleşti (aşağıda). Diğer
  çelişkiler (GİB özelgesi / Danıştay) hâlâ açık.
- Yol 1, bireysel + özel banka hesabı (GVK Mükerrer 20/B): vergi
  dairesinden "Sosyal İçerik Üreticiliği ve Mobil Uygulama
  Geliştiriciliği İstisna Belgesi" alınır, Türkiye'de özel hesap açılır.
  Banka %15 stopaj keser; beyanname/defter yok iddiası. [ticari]
  https://remotevergi.com/app-store-play-store-vergi/
  https://vergimerkezi.com.tr/mobile-app-developer-vergi-rehberi/
  - 2026 hasılat üst sınırı: 5.300.000 TL. İlk turda iki kaynak çelişiyordu
    (5,3 mn ve "tahmini 6,5 mn"); ikinci turda çözdüm: bu sınır gelir vergisi
    tarifesinin 4. gelir diliminin üst sınırına bağlı, 2026 için
    4.300.000 TL'den 5.300.000 TL'ye çıkarıldı (Bloomberg HT, 31 Aralık
    2025; Resmi Gazete 5. mükerrer). remotevergi sayfası da 5.300.000 TL
    yazıyor (son güncelleme 11 Mayıs 2026). "6,5 mn" rakamı yeniden
    değerleme açıklanmadan önceki bir tahmin; kullanma. [haber + ticari]
    https://www.bloomberght.com/2026-da-uygulanacak-gelir-vergisi-tarifeleri-belli-oldu-3765170
    https://www.fatiharas.com/gvk-mukerrer-madde-20-b-sosyal-icerik-ureticiligi-istisnasi/
  - Sınır aşılırsa kazancın TAMAMI (yalnız aşan kısım değil) yıllık
    beyannameyle beyan edilir; bankanın kestiği %15 mahsup edilir. İstisna
    gerçek kişilere tanınıyor; kurumlar vergisi mükellefi (ör. limited
    şirket) yararlanamaz. Tebliğin kendisini (GİB) okumadım; bu iki
    madde ticari/mali müşavir sitelerinden. [ticari, tek tek]
    https://www.fatiharas.com/gvk-mukerrer-madde-20-b-sosyal-icerik-ureticiligi-istisnasi/
  - Reklam giderleri bu yolda düşülemez. Bağ-Kur primi kişiye ait. [ticari]
- Yol 2, şahıs şirketi + hizmet ihracatı: KVK 10/1-ğ ve GVK 89/13,
  %100 indirim; e-Arşiv faturası yurt dışı muhatabına; KDV %0 (kod 302).
  Yıllık tutarın tamamı Türkiye banka hesabına gelmeli. Aylık mali müşavir
  maliyeti ~3.000-6.000 TL iddiası. [ticari]
  https://remotevergi.com/app-store-play-store-vergi/
- İhtilaf: GİB özelgesi (30 Ocak 2026), uygulama Türk mağazasında
  yayındaysa hizmetin "münhasıran yurt dışında" kullanılmadığını,
  dolayısıyla indirim olmadığını söylemiş. Danıştay 3. Daire (21 Ekim
  2025) hizmetin yabancı platforma verildiğini, kullanıcının yerinin
  önemsiz olduğunu söylemiş. Davalar 2-3 yıl sürebilir iddiası. [ticari]
  https://remotevergi.com/app-store-play-store-vergi/
- Play Console vergi ayarına Türkiye Vergi Kimlik Numarasını girmezsen
  İrlanda yerel vergisi kesilebilir iddiası. [ticari, tek kaynak]
  https://vergimerkezi.com.tr/mobile-app-developer-vergi-rehberi/
- Banka tarafı: Google ödemeleri toplu/net/aylık gelir; banka izleme
  sistemi örüntüyü tanımayıp 5549 sayılı Kanun kapsamında işlem
  askıya alabilir. Önlem: DDA'yı, Play Console finans raporlarını, brüt
  satış - komisyon = net ödeme mutabakatını saklamak; ticaret sicilindeki
  faaliyet tanımını gerçek gelir modeline uydurmak. [ticari]
  https://ozmconsultancy.com/saas-app-store-merchant-of-record-payouts-into-a-turkish-bank-account-avoiding-a-compliance-hold/

### 3.3 E-ticaret, ticari sicil, KVKK (belirsiz alanlar)

- ETBİS: "kendi sitesi/mobil uygulaması üzerinden satış yapan" kayıtlı
  olmalı; yalnız pazaryerinden satanın yükümlülüğü genelde yok deniyor
  (arama özeti; sayfayı açmadım). Google Play üzerinden dijital abonelik
  satışının ETBİS kapsamına girip girmediği HİÇBİR kaynakta yoktu.
  [doğrulanamadı] Mali müşavire/avukata sor. [ticari]
  https://ornekfirma.com.tr/e-ticaret/yasal-yukumlulukler/etbis-kaydi
- KVKK/VERBİS: yalnız cihazda işlenen veride aydınlatma metni ve VERBİS
  yükümlülüğü olup olmadığı bulunamadı. [doğrulanamadı] Güvenli yol:
  Türkçe, sade bir gizlilik/aydınlatma metni yayınla ve avukata göster.
  Ben bunu hukuki görüş olarak vermiyorum. [çıkarım]
- AB (Fransa, Almanya...) için DSA "trader" beyanı: Apple'da Şubat 2025'te
  doğrulanmamış uygulamalar AB mağazasından kalktı. Google Play için
  adımları anlatan RESMİ sayfa bulunamadı; kaynaklar "Play da aynısını
  istiyor" diyor. Gelir getiren (abonelik) uygulama büyük olasılıkla
  "trader" sayılır; adres/telefon/e-posta mağazada görünür. [blog + çıkarım]
  https://neuros.com.tr/en/guides/eu-trader-status-dsa
  Bu yüzden AB'yi ilk dalgaya koyma (bkz. Ülke seçimi).

## 4. Yeni kişisel hesap: kapalı test şartı (en kritik)

### 4.1 Kural (güncel sayıyı doğruladım)

- Kural: 13 Kasım 2023'ten sonra açılan kişisel hesaplar, uygulamalarını
  EN AZ 12 test kullanıcısıyla, onların kesintisiz EN AZ 14 gün kapalı
  teste katılmış olduğu bir test yürütmeli. 21 Ekim 2026'da hâlâ geçerli. [resmi]
  https://support.google.com/googleplay/android-developer/answer/14151465
- Başvuru anında 12 kişi hâlâ katılımcı olmalı ve önceki 14 gün boyunca
  aralıksız katılmış olmalı. [resmi]
- Test kullanıcısı katılıp 14 günden önce çıkarsa SAYILMAZ. Çıkıp
  geri girerse 14 günün ardışık olması gerekir. [resmi]
- Eşik eskiden 20'ydi; 11 Aralık 2024'te 12'ye indi. Resmi sayfa tarihi
  yazmıyor (arama özeti, sayfayı açmadım). [blog]
  https://ontest.app/blog/google-play-12-testers-14-days-requirement-explained
- Üretim (Production) ve ön kayıt (pre-registration) bu şart bitene kadar
  kapalı. Açık test (open testing) de ancak üretim erişiminden sonra
  açılır. [resmi]
  https://support.google.com/googleplay/android-developer/answer/14151465
- İç test sayılmaz: "kapalı test çalıştırmak zorundasın". [resmi]
- Başvuru formu üç bölüm (resmi metin):
  - Kapalı testin hakkında: test kullanıcısı bulmak ne kadar kolaydı;
    kullanıcılar tüm özellikleri kullandı mı; kullanım gerçek üretim
    kullanımına benziyor muydu (farklar); geri bildirimi özetle ve nasıl
    topladığını yaz.
  - Uygulaman hakkında: hedef kitle ("olabildiğince somut"); değer
    önerisi; ilk yıl tahmini kurulum aralığı. Bu bölümün cevapları
    mağazada gösterilmez; görünürlüğü ve program uygunluğunu etkilemez.
  - Üretime hazırlık: testten öğrenip neyi değiştirdin; üretime hazır
    olduğuna nasıl karar verdin.
  [resmi]
  https://support.google.com/googleplay/android-developer/answer/14151465
- Ek test gerekirse: nedenler "12'den az katılımcı" veya "test süresince
  yetersiz katılım/etkileşim". Resmi dilde "etkileşim" geçiyor; nasıl
  ölçüldüğü yok. [resmi]
- Google: başvuru bir "sorun giderme adımı" değildir; politika
  uyumsuzluğu ret, gecikme ve uzun itiraz demek. [resmi]
- Şart "uygulaması için" diye yazılmış. Aynı hesapta ikinci bir uygulama
  (ör. ayrı yapay zekalı sürüm, I-101) için testin yeniden gerekip
  gerekmediği açıkça yazmıyor; ben "gerekir" diye okudum. 14 günü yeniden
  planla. [çıkarım]

### 4.2 Test kullanıcısı toplama

- Resmi öneri: kişisel/mesleki çevre, hedef kitleyi temsil eden kişiler
  ("iş uygulamasıysa farklı sektörlerden profesyoneller"). Bu Sende için
  berber, kuaför, mağaza, sipariş alanlar demek. [resmi]
  https://support.google.com/googleplay/android-developer/answer/14151465
- Talimat ver: "14 gün boyunca testte kal". Geri bildirim kanalı ver
  (e-posta/web/mesaj grubu); Play'in kendi özel geri bildirimi de var. [resmi]
- Tamponlu başla: 15-20 kişi. 12 sınırının hemen üstünde kalırsan tek
  bir kişi çıkınca sayaç bozulur. [blog + çıkarım]
  https://dev.to/playconsolestore1/the-14-day-closed-testing-requirement-for-new-google-play-developer-accounts-explained-4gpc
- Sayım tuzakları (Türkçe bir yazılım firmasının rehberi, ticari):
  - Aynı kişiyi önce iç teste eklemek (iç testten çıkmadan kapalıya
    girmez; bu resmi sayfada da var, aşağıda).
  - Davet edilen hesaptan farklı Google hesabıyla girmek.
  - Kurup hiç açmamak.
  - İki hafta hiç güncelleme/geri bildirim döngüsü olmaması.
  [ticari]
  https://neuros.com.tr/rehberler/google-play-kapali-test
- Test hesabı satın almak / sahte hesap: Play politikası "kurulum
  sayısını meşru olmayan yollarla şişirmeyi" yasaklıyor. Tester satan
  siteler ticari; hesabı riske atmaya değmez. [resmi + çıkarım]
  https://support.google.com/googleplay/android-developer/answer/18258653
- Bir HN yorumcusu (Ekim 2026) tester-takas uygulamalarının Play'de
  durduğunu, Google'ın umursamadığını düşünüyor. Bu kanıt değil, tahmin. [forum]
  https://news.ycombinator.com/item?id=49920086
- Ülke tuzağı: test kullanıcısı Play ülkesi, kapalı kanalın ülke
  listesinde değilse "Yükle" düğmesi çıkmaz. Kanalın ülke listesini
  üretimle eşzamanlı bırakma; test kullanıcılarının ülkesini ekle. [blog]
  https://dev.to/arun_kumars_99869f8d07a3/closed-testers-cant-install-your-app-how-to-fix-country-availability-in-google-play-console-5cfi

### 4.3 Reddedilme ve "ikinci deneme"

- Resmi: ek test gerekirse kapalı test devam eder. Ayrı bir itiraz yolu
  resmi sayfada tarif edilmiyor. [resmi]
- Ticari kaynaklar (tester satan siteler) "ret nedenleri" sıralıyor:
  yetersiz etkileşim, güncelleme olmaması, belirsiz form cevapları,
  erken başvuru, yüksek çökme oranı. Bunlar resmi sayfadaki
  "etkileşim, form cevabı, çökme" ile uyumlu ama sayısal iddialar
  (ör. "%5 çökme kesin ret") ticari. [ticari]
  https://12-testers-for-14-days.github.io/guides/google-play-production-access-requirements/
- Bağımsız, birinci elden "üretim erişimi reddedildim, şöyle çözdüm"
  anlatısı: ilk turda bulamamıştım; ikinci turda iki tane buldum ve
  aşağıda 4.4'te yazdım. İkisi de tek kişilik ve kendi ürününü satan
  yazarlardan. Reddit hâlâ erişilemiyor. [forum]

### 4.4 Gerçek hayat: kapalı test hikâyeleri (22 Ekim 2026'da okundu)

- Hikâye 1, REDDEDİLDİ ve çözdü. Yazar: chakshusoft (dev.to, "7 Haziran";
  sayfada yıl yok, 2026 görünüyor). [forum, tek kaynak]
  https://dev.to/chakshusoft/i-wasted-6-weeks-on-google-plays-closed-testing-heres-what-finally-worked-41h8
  - Tur 1: ~12 arkadaş/aile katıldı. 5. günde ~3 kişi kullanıyordu. 14.
    günde başvurdu, "testers did not show sufficient engagement" ile
    reddedildi.
  - Tur 2: Reddit/Discord'dan ~25 kişi. Çoğu bir kez kurup sildi. 15.
    günde başvurdu, aynı sebeple yine reddedildi. Bir aydan fazla gitti.
  - Tur 3: 22 kişi. Her gün kimin aktif olduğuna baktı, pasif iki kişiye
    yazdı, 10. günde düşen birini hemen yenisiyle değiştirdi. 14. günde
    başvurdu, ONAYLANDI.
  - Çıkarım: yalnız sayı değil, kullanım izi ve pasifi erken fark etmek.
  - Uyarılar: yazar kendi izleme SDK'sını satıyor. "20 test kullanıcısı"
    diyor; resmi sayfa 12 diyor (ben doğruladım), yani bu yazarın yorumu.
- Hikâye 2, reddedilmeden geçti. Yazar: Ingrid (dev.to, 7 Eylül 2025).
  [forum, tek kaynak]
  https://dev.to/ingrid_dev/how-to-find-12-testers-for-your-android-app-2o54
  - Arkadaş/aile: 4-5 kurulum, yarısı birkaç günde sildi ("kibarlıktan
    kurdular"). Reddit paylaşımı saatler içinde moderatörce silindi.
  - Test takas uygulamasıyla 2 günde 14 kişi, 14 gün sonunda ~60 kişi.
    14 günde 6 sürüm yükledi. Başvurudan 2 gün sonra onay.
  - Yazar "12 kişiyle girip reddedilenler, aylarca bekleyenler var" diyor;
    bu kulaktan dolma.
- Tester takas/satış hizmeti yazarları (Can Dizdar/TestFi, dev.to, 15 ve
  17 Ekim, yıl sayfada yok, 2026 verisine atıf yapıyor): [ticari, çıkar var]
  https://dev.to/founder_can/how-to-get-12-testers-for-google-play-closed-testing-with-numbers-from-112-closed-tests-40nl
  https://dev.to/founder_can/google-plays-production-access-form-question-by-question-what-to-write-after-your-14-day-closed-4052
  - Kendi 112 kapalı testinde 12. kuruluma medyan süre 63,6 saat (kendi
    verisi, bağımsız doğrulanmamış). 14 gün SAYAÇ test kullanıcısı
    başınadır: yedek biri gelirse onun 14 günü kendi başlar. (Resmi SSS ile
    uyumlu: "çıkıp tekrar girenin 14 günü ardışık olmalı".)
  - Başvuru formunda "ücretli test sağlayıcı kullandın mı" sorusu olduğunu
    yazıyor; resmi sayfada böyle bir soru YOK. [doğrulanamadı]
  - Form cevap önerisi: sayı ve sürüm numarası ver; "çok ilgiliydiler" gibi
    sıfat verme. Bu öneri mantıklı; resmi form da "hangi özellikleri
    kullandılar, üretim kullanımına benzedi mi, geri bildirimi nasıl
    topladın" diye soruyor. [resmi + blog]
- RevenueCat rehberi (3 Temmuz 2025): sayaç test AKTİF olunca başlar;
  ilk gün "hello world" bile yüklensin; kişi bağlantıdan KATILMAZSA liste
  tek başına yetmez. [blog, bilinen şirket]
  https://www-docs.revenuecat.com/blog/engineering/google-play-14-day
- Doğrulanamayan iddia: "Play Console kullanıcı etkileşim süresi/günlük
  aktif kullanıcıyı ölçüyor; 12 kişiden ~8'i 14 günün 5-7'sinde açmalı"
  (dev.to, "17 Mayıs", yazar test kullanıcısı bulma hizmeti satıyor).
  Yazar eşiğin "yayımlanmadığını, tahmin olduğunu" kendisi yazıyor.
  Resmi sayfada böyle bir sayı yok. KESİN KURAL OLARAK ALMA. [forum]
  https://dev.to/tizoc_araujo_3cd9fb67191f/google-play-closed-testing-rejected-for-insufficient-engagement-what-that-actually-means-in-2026-ion
  Ama resmi sayfa "yetersiz test kullanıcısı etkileşimi"ni ek test
  sebebi olarak SAYIYOR (bölüm 4.1), o yüzden etkileşimi gerçekten üret.
- Sende'ye özel zorluk: analitik/izleme SDK'sı koymayan (sideload'da
  INTERNET yok; Play'de yalnız Billing yüzünden var) bir uygulamada
  kullanım izini bu yazarlar gibi SDK'yla göremeyiz. SDK eklemek "veri
  toplanmıyor" vaadini de bozar. Çare: (1)
  Play Console'un kendi istatistikleri (kurulum, etkin cihaz); (2) bizde
  zaten gizli Tanılama ekranı (7 dokunuş) ve JSON dışa aktarma var
  (ROADMAP Faz 6); ona mesaj İÇERİĞİ olmayan, yalnız sayaç içeren bir
  "kullanım özeti" paylaşımı eklenip test kullanıcısı onu bize yollayabilir;
  (3) haftalık kısa "bu hafta ne yaptın" mesajı. [çıkarım]

## 5. Test kanalları ve davet linki mantığı

- İç test: en fazla 100 test kullanıcısı (e-posta listesi). Uygulama
  kurulumu tamamlanmadan bile açılabilir. Yeni AAB dakikalar içinde
  hazır; ilk yüklemede geçici ad/liste 48 saate kadar görünür. Şart yok. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9845334
- İç test sayılmaz; üstelik iç teste giren kişi, önce çıkmadan kapalı veya
  açık teste ALINMAZ. Kapalı test kullanıcılarını iç test listesine koyma. [resmi]
- Kapalı test: e-posta listesi (200 liste, liste başına 2.000 kişi, kanal
  başına 50 liste) veya Google Groups. "Paylaşılabilir bağlantıyı kopyala"
  ile davet. Kurulum için Google hesabı gerekir. Birden çok kapalı kanal
  açılabilir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9845334
- İlk yayından sonra bağlantının testçilere ulaşması "birkaç saat"
  sürebilir. [resmi]
- Davet (opt-in) bağlantısı yalnız uygulama durumu "Yayınlandı"
  (Published) iken görünür; "Taslak" veya "Yayın bekliyor" durumunda
  görünmez. Her test kullanıcısı bağlantıdan kendisi katılmalı (listeye
  eklenmek tek başına yetmez). Bağlantıda geri bildirim e-postası/URL'si
  gösterilir; onu mutlaka doldur. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9845334
- Açık test: Play'de görünür, herkes katılabilir; ancak üretim erişimi
  sonrasında açılır. Test kullanıcısı geri bildirimi herkese açık puanı
  ETKİLEMEZ. [resmi]
- Test kullanıcısı için Play dışından APK kurmak sayılmaz; davet
  bağlantısından Play üzerinden kurmalılar. [blog]
  https://dev.to/playconsolestore1/the-14-day-closed-testing-requirement-for-new-google-play-developer-accounts-explained-4gpc
- Ücretli uygulamada test kullanıcıları satın almak zorunda; abonelikte
  "lisans test hesapları" listesi ödeme almadan test sağlar. ODEME.md ile
  uyumlu. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9845334
- Bizim A/B planı (iki kapalı kanal, 20+20 kişi): iki kanalın her biri 12
  sınırını tek başına aşıyor. Hangisinin sayılacağı belgelenmemiş;
  güvenli yol: iki kanalı da 15+ kişiyle, 14 gün boyunca aralıksız
  tutmak. [çıkarım]

## 6. Teknik şartlar (2026)

### 6.1 Hedef API

- 31 Ağustos 2026'dan itibaren yeni uygulama ve güncellemeler Android 16
  (API 36) veya üstünü hedeflemeli. Uzatma: 1 Kasım 2026'ya kadar. [resmi]
  https://support.google.com/googleplay/android-developer/answer/11926878
- Bizde: compileSdk 36, targetSdk 36, minSdk 26. Uygun. [ölçüm]
  /home/user/Jn/app/build.gradle.kts
- Uzatma: Console'da bir uzatma formu açılıyor, 1 Kasım 2026'ya kadar.
  Biz zaten 36'yı hedeflediğimiz için gerekmez. [resmi, 22 Ekim 2026]
- Bir sonraki hedef (API 37) için resmi tarih sayfada yok. [doğrulanamadı]
  Android 17 (API 37) 16 Haziran 2026'da kararlı çıktı (Android Authority,
  özet; sayfa açılmadı). Önceki örüntü (API 35: 31 Ağu 2025; API 36:
  31 Ağu 2026) yıllık bir adım gösteriyor; yani API 37 için 31 Ağustos
  2027 beklenebilir. Bu TAHMİN, kaynak değil. [çıkarım]
  https://androidauthority.com/android-17-release-date-3639790

### 6.2 AAB ve imzalama

- Ağustos 2021'den beri yeni uygulamalar AAB (.aab) ile yüklenmeli. [resmi]
  https://developer.android.com/guide/app-bundle
- Üretim için: `./gradlew :app:bundlePlayRelease` (RELEASE.md'deki
  `bundleRelease` artık yetmez; iki "flavor" var). [ölçüm]
- Play App Signing: Google uygulama imza anahtarını tutar; sen yükleme
  (upload) anahtarını tutarsın. Yükleme anahtarı kaybolursa Google
  sıfırlayabilir. Yeni uygulamalar Google'ın ürettiği anahtarla
  "kuantum-hazır hibrit imza"ya otomatik alınıyor. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9842756
- Yükleme anahtarı: RSA 2048+ ve Google'ın imza anahtarından farklı olmalı
  (öneri). Üretim komutu: `keytool -genkeypair ... -keyalg RSA
  -keysize 2048` (RELEASE.md ile uyumlu). [resmi + ölçüm]
- Önemli sonuç: Play'in imzaladığı APK ile bizim sideload APK'sı FARKLI
  anahtarlarla imzalı olur. Aynı paket adında olsalar bile birbirinin
  üstüne kurulmaz ("uygulama yüklenemedi / çakışıyor"). [çıkarım]
  - Seçenek A: sideload için ayrı applicationId (ör. com.sende.app.direct).
  - Seçenek B: aynı id; kullanıcıya "Play'den kurulana geç" de, veri
    taşıma yok.
  Önerim A. Karar kurucuda.
- Depodaki `sideload-test.jks` açıkça "herkese açık, gizli olmayan" anahtar.
  Bu anahtarı ASLA Play'e verme. Müşterilere dağıtılacak sideload
  sürümünü bu anahtarla imzalama: herkes aynı imzayla "güncelleme" üretip
  yükleyebilir. [ölçüm + çıkarım]
  /home/user/Jn/app/build.gradle.kts

### 6.3 Diğer teknik şartlar

- 16 KB sayfa boyutu: API 35+ hedefleyen tüm uygulamalar 64 bit
  cihazlarda 16 KB desteklemeli. Sayfaya göre 1 Şubat 2027'den sonra
  desteklemeyen güncellemeler yayınlanamaz. Yalnız Java/Kotlin uygulamalar
  zaten uyumlu. [resmi]
  https://developer.android.com/guide/practices/page-sizes
  - Bizim APK'da iki AndroidX yerel kütüphanesi var
    (libandroidx.graphics.path.so, libdatastore_shared_counter.so).
    64 bit sürümlerinde `readelf -l` ile LOAD hizası 0x4000 (16 KB)
    ölçtüm: uyumlu görünüyor. [ölçüm]
  - Yine de AAB üretilince `zipalign -c -P 16 -v 4` ve bir 16 KB
    emülatörde `getconf PAGE_SIZE` = 16384 ile doğrula. [resmi]
- Play Billing Library sürümü: 31 Ağustos 2026'dan sonra yeni uygulama ve
  güncellemeler sürüm 8 veya üstünü kullanmalı; 31 Ağustos 2027'den
  sonra sürüm 9. Bizde 8.0.0 (şimdilik yeterli). [resmi]
  https://developer.android.com/google/play/billing/deprecation-faq
  - SORUNLAR_COZUMLER.md S-13: 9.x Kotlin 2.3 gerektiriyor. Ağustos
    2027'den önce Kotlin/AGP yükseltmesi planlanmalı. [ölçüm + çıkarım]
- Teknik kalite eşikleri: Android vitals "kötü davranış" sınırı: kullanıcı
  hissettiği çökme oranı %1,09, ANR %0,47 (telefon modeli başına %8).
  Aşılırsa Play görünürlüğü azaltabilir, mağaza sayfasına uyarı koyabilir. [resmi]
  https://developer.android.com/topic/performance/vitals
- 26 Ağustos 2026'da duyurulan yeni kalite şartları (Şubat 2027'den):
  bellek eşikleri ve DEX optimizasyon (en az %25; yalnız 10 MB'tan büyük
  DEX'i olan uygulamalara). Sende ~2,5-3 MB APK, R8 açık: etkilenmez. [resmi]
  https://support.google.com/googleplay/android-developer/answer/17492799
- Derleme klasörü uyarısı: `app/build` altındaki birleşmiş Play manifestinde
  eski ad kalıntısı (com.tekpanel.app....DYNAMIC_RECEIVER...) var. Üretim
  AAB'sinden önce `./gradlew clean` yapıp birleşmiş manifesti tekrar
  incele. [ölçüm]

## 7. Uygulama içeriği (App content) formları ve Sende için cevaplar

Bu bölümdeki "Önerilen cevap" satırları benim önerim [çıkarım]. Doğru
cevabı verme sorumluluğu geliştiricide: yanlış beyan, kaldırma veya
askıya almaya yol açar. [resmi]
https://support.google.com/googleplay/android-developer/answer/9859655

### 7.1 Gizlilik politikası

- Hassas izin/veri isteyen uygulamada politika hem mağaza sayfasında HEM
  uygulamanın içinde bağlantı olarak bulunmalı. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9859455
- URL şartı: aktif, herkese açık, coğrafi engelsiz, PDF olmayan ve
  düzenlenemez bir sayfa. [resmi]
  https://support.google.com/googleplay/android-developer/answer/10144311
- Politika, uygulama içi açıklamalarla birlikte ne topladığını, nasıl
  kullandığını, kimlerle paylaştığını anlatmalı. [resmi]
- Bir geliştirici, bağlantı adı "Privacy Notice" olduğu için reddedilmiş;
  adı "Privacy Policy" yapınca onaylanmış. Sayfa açılamadı, arama
  özetinden. [blog, tek kaynak]
  https://medium.com/@himanshu_hc/overcoming-google-plays-privacy-policy-pitfall-a-tale-of-updating-a-rejected-build-32c7275085cc
- Bizde: uygulama içinde gizlilik METNİ var (privacy_body) ama bir URL
  bağlantısı yok. [ölçüm] Yayın öncesi eklenmeli.
- Politikada mutlaka geçmesi gerekenler (benim önerim):
  - Seçilen uygulamaların bildirimleri (başlık, metin, gönderen, saat,
    görsel) okunur ve YALNIZ cihazda saklanır; sunucu yok.
  - Kurulu uygulamalar listesi okunur (kanal seçimi için); dışarı
    gönderilmez. Play, "kurulu uygulama envanteri"ni kişisel/hassas veri
    sayıyor; bu veriyi politikada ve açıklamada ele almayan uygulama ihlal
    örneği olarak geçiyor. [resmi]
    https://support.google.com/googleplay/android-developer/answer/10144311
  - Dışa aktarma yalnız kullanıcı paylaşım sayfasından seçerse dışarı çıkar.
  - Silme yolu (uygulama içi "tüm yerel veriyi sil") ve izni kapatma yolu.
  - Play sürümünde Google Play Faturalandırma kütüphanesinin ağ
    kullandığı ve mesaj verisi göndermediği.
  - İletişim e-postası. Türkçe + İngilizce sürüm.
- Kurulu uygulama listesi (ayrıntı, 22 Ekim 2026'da politika metninden):
  [resmi] https://support.google.com/googleplay/android-developer/answer/17517561
  - Politika "Package (App) Visibility" bölümü: cihazdan sorgulanan kurulu
    uygulama envanteri KİŞİSEL ve HASSAS veri sayılır; envantere görünürlük
    uygulamanın temel işleviyle doğrudan ilgili olmalı.
  - QUERY_ALL_PACKAGES yalnız belirli kullanım durumlarında; "daha dar bir
    görünürlük bildirimi" mümkünse kullanılamaz. Ayrıca "geniş görünürlüğe
    yaklaşan alternatif yöntemler de kullanıcıya dönük temel işlevle
    sınırlı". Bizim `<queries>` içindeki MAIN/LAUNCHER sorgusu tam olarak
    bu "alternatif yöntem" sınıfına girebilir; savunma: kullanıcı HERHANGİ
    bir uygulamayı kanal seçebilsin diye liste, ürünün temel işlevi. [çıkarım]
  - Sonuç: bu listenin neden okunduğunu uygulama içi açıklamada, gizlilik
    politikasında ve inceleyici notunda ayrıca yaz. Listenin dışarı
    gitmediğini her yerde aynı cümleyle söyle.
- Gerçek hayat, "kurulu uygulama bilgisi" reddi (Unity forumu, Eylül 2025):
  [forum, birden çok geliştirici ama tek başlık]
  https://discussions.unity.com/t/google-policy-issue-issue-found-apk-requires-prominent-disclosure-upload-of-users-installed-application-information/1683666
  - Birkaç geliştirici "Your app is uploading users' Installed Application
    information without a prominent disclosure" iletisiyle reddedildi;
    süre 6 gün (14 Eylül'de bildirim, 20 Eylül son tarih).
  - Şüphelenilen sebep: reklam aracı SDK'sının (ironSource/LevelPlay)
    getInstalledApplications çağrısı. Geliştiriciler kendi kodlarında
    veri göndermediklerini söylüyordu, yine de ret geldi.
  - Çözüm yolları: SDK'yı kaldırmak (kabul edildi); ya da onay penceresi
    ekleyip Veri güvenliği formunda "Evet" demek (geçti). Dışarıdan kanıt:
    bu kod taraması gerçek ve SDK kaynaklı.
  - Sende için ders: Play sürümünde reklam/analitik SDK'sı OLMAMALI;
    Billing dışında ağ kodu olmamalı. Kurulu uygulama sorgusu bizim kodda
    ve açıklanmış olmalı. [çıkarım]

### 7.2 Uygulama erişimi (App access) ve inceleyici notu

- Politika: uygulamanın kısıtlı bölümleri varsa inceleyiciye çalışan
  demo hesap ve gereken tüm kaynakları vermek zorunlusun. [resmi]
  https://support.google.com/googleplay/android-developer/answer/18258653
- Sende'de giriş yok ama çekirdek işlev "Bildirim erişimi" özel iznine
  bağlı. İnceleyici izni vermezse ekran boş görünür ve "çalışmıyor"
  diye reddedebilir. [çıkarım]
- Çözüm (öneri): uygulamaya "Test bildirimi gönder" düğmesi (Sende kendi
  bildirimini yollar, kendi dinleyicisi yakalar), inceleyici notunda adım
  adım talimat ve 60 sn ekran videosu bağlantısı. Google bunu "sign-in
  details" alanındaki "Any other instructions"a yazmana izin veriyor.
  [resmi + çıkarım]
  https://support.google.com/googleplay/android-developer/answer/9859455
- "Yayından önce uygulamanın beklentiyi karşıladığı kontrol edilir":
  mağazada vaat edilen özellik uygulamada yoksa ret. [resmi]
  https://support.google.com/googleplay/android-developer/answer/15191715

### 7.3 Reklamlar, reklam kimliği, hedef kitle

- Reklam: "reklam içeriyor mu" mutlaka cevaplanır; yanlış beyan askıya
  alma sebebi. Sende reklam yok. Önerilen cevap: HAYIR. [resmi + ölçüm]
  https://support.google.com/googleplay/android-developer/answer/9859455
- Reklam kimliği (AD_ID): Android 13+ hedefleyen uygulama reklam kimliği
  kullanıyorsa manifestte izin tanımlar. Birleşmiş Play manifestinde AD_ID
  YOK (ölçtüm). Önerilen cevap: "reklam kimliği kullanmıyor". [resmi + ölçüm]
  https://support.google.com/googleplay/android-developer/answer/6048248
- Hedef kitle: "çocuklara yönelik değil" seçersen Families politikası
  uygulanmaz. İş aracı olduğu için yalnız 18+ seç. Bu bölüm reklam, uygulama
  erişimi ve gizlilik politikasından SONRA doldurulur. [resmi + çıkarım]
  https://support.google.com/googleplay/android-developer/answer/9867159
- Aşırı geniş yaş grubu seçme: "yalnız herkese açık olsun diye" çok yaş
  grubu seçilmemeli. [resmi]

### 7.4 İçerik derecelendirme

- Zorunlu; IARC anketi. Derecelendirmesiz uygulama kaldırılır. Türkiye,
  PEGI otoritesi kapsamında listeleniyor. Anket cevapları yanlışsa
  kaldırma/askıya alma riski. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9859655
- Beklenti (tahmin): şiddet/cinsellik içeriği yok → en düşük sınıf.
  "Kullanıcı etkileşimi/kullanıcı içeriği" sorusuna dürüst cevap ver:
  Sende kendi sosyal ağı değil; başka uygulamaların bildirimini gösterir.
  Cevabı sorulduğunda birlikte gözden geçiririz. [çıkarım]

### 7.5 Veri güvenliği formu

- Resmi tanım: "Toplamak" = veriyi uygulamadan cihazın DIŞINA iletmek.
  "Yalnız cihazda işlenen, cihazdan çıkmayan veri beyan edilmek zorunda
  DEĞİL." [resmi]
  https://support.google.com/googleplay/android-developer/answer/10787469
- Ödeme servisi (Google Play faturalandırma dahil) kart bilgisini
  doğrudan kullanıcıdan topluyor ve uygulama o bilgiye erişmiyorsa
  beyan gerekmez. [resmi]
- Kullanıcının başlattığı paylaşım (dışa aktarma) "paylaşım" sayılmaz.
  Ama "uygulamadan başka bir uygulamaya cihaz içi aktarım" paylaşım sayılır;
  Sende yanıt/aç eylemleri kullanıcı eylemiyle olduğundan genelde
  "kullanıcı eylemi" istisnasına girer. [resmi + çıkarım]
- Önerilen cevap: veri toplanmıyor, paylaşılmıyor. RELEASE.md §4 taslağı
  ("Evet, Messages toplanıyor") resmi tanımla çelişiyor; güncellenmeli. [çıkarım]
- Doğruluk önemli: Play form yanıltıcıysa güncellemeyi engelleyebilir veya
  uygulamayı kaldırabilir. Formla gizlilik politikası aynı şeyi söylemeli. [resmi]
- İç test kanalında olan uygulamalar Veri güvenliği bölümünün dışında
  tutulur. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9845334
- Bir rakip (Notisave) Play sayfasında "veri toplanmıyor" yazıp aynı
  sayfada cihaz kimliklerinin paylaşıldığını yazıyor: kendi içinde
  tutarsız; bizim açıklığımız avantaj. [ölçüm]
  https://play.google.com/store/apps/datasafety?id=com.tenqube.notisave

### 7.6 Sağlık, finans, devlet, haber, hassas izin beyanı

- Sağlık, finansal özellikler, devlet, haber beyanları: Sende için hepsi
  "yok". Not: finans/sağlık/devlet uygulamaları kuruluş hesabı ister. [resmi]
  https://support.google.com/googleplay/android-developer/answer/18258653
- İzin beyan formu (Permissions Declaration): yüksek riskli izin (SMS,
  arama kaydı vb.) istenince çıkar ve "birkaç hafta" ek inceleme getirir.
  Sende manifestte böyle izin YOK (yalnız RECEIVE_BOOT_COMPLETED + Play
  sürümünde BILLING/INTERNET/ACCESS_NETWORK_STATE). [resmi + ölçüm]
  https://support.google.com/googleplay/android-developer/answer/9214102
- BIND_NOTIFICATION_LISTENER_SERVICE, bu form listesinde ayrı bir
  "talep edilebilir izin" olarak geçmiyor; politika metninde de adı
  yok (arattım). SMS okumak için READ_SMS ASLA ekleme. [resmi + çıkarım]
  https://support.google.com/googleplay/android-developer/answer/9888170
- QUERY_ALL_PACKAGES kullanmıyoruz; MAIN/LAUNCHER sorgusu yeterli.
  İyi karar, ama bu sorgu da "geniş görünürlüğe yaklaşan yöntem" sayılıp
  temel işlevle gerekçelendirilmeli (bkz. 7.1). [ölçüm + çıkarım]
- Pil muafiyeti (REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) ve erişilebilirlik
  servisi ile "canlı tutma" EKLEME: ayrı politika riski (araştırma 05). [resmi]

## 8. Bildirim okuyan uygulamalar: politika ve inceleme riski

### 8.1 Politikada ne var, ne yok

- "NotificationListener" ifadesi Geliştirici Program Politikası, Kullanıcı
  Verileri, Hassas İzinler ve Politika Süreleri sayfalarında GEÇMİYOR. [ölçüm]
  https://support.google.com/googleplay/android-developer/answer/18258653
  https://support.google.com/googleplay/android-developer/table/12921780
- Bu, araştırma 07'deki "bildirim erişimi için Play kuralı bulunamadı"
  notunun cevabı: özel kural yok, genel kurallar var. [çıkarım]
- Genel kural 1, belirgin açıklama + onay: veri erişimi kullanıcının makul
  beklentisi dışındaysa (örnek: arka planda toplama) uygulama içinde,
  normal kullanım sırasında (menüye girmeden), açıkça şunları anlatan bir
  açıklama göstermelisin: hangi veri, nasıl kullanılacak/paylaşılacak.
  Yalnız gizlilik politikasında veya mağaza açıklamasında olamaz; başka
  açıklamalarla karışık olamaz; onay "dokun-kabul et" gibi bilinçli bir
  eylemle olmalı; geri/ana ekran tuşu onay sayılmaz. [resmi]
  https://support.google.com/googleplay/android-developer/answer/10144311
- Önerilen kalıp: "[Bu uygulama] [özellik] için [veri türünü] [şu
  durumda] toplar/saklar." [resmi]
- Bildirim erişimi sistem ekranında verilir (normal izin penceresi değil).
  Bu yüzden açıklama ondan ÖNCE bizim ekranımızda olmalı. [çıkarım]
- Bizde şimdi: erişim kartı "Mesajlar bu ekrana böyle ulaşır. Telefonunda
  kalır." (access_body). Bu kısa; veri türlerini ve kullanımı saymıyor.
  Kurucunun I-47 isteği (kısa tut) ile politika arasında denge gerek.
  Önerim: "Erişimi aç"a basınca ÖNCE iki cümlelik açıklama ve
  "Devam" / "Şimdi değil" düğmeleri çıksın; kartın kendisi kısa kalsın.
  [ölçüm + çıkarım]
  - Taslak (Türkçe): "Sende, seçtiğin uygulamaların bildirimlerini
    (gönderen, mesaj metni, görsel, saat) okur ve yalnız bu telefonda
    saklar. Hiçbir yere göndermez. Bunun için Android'in Bildirim erişimi
    ayarını açman gerekir; istediğin an kapatabilirsin."
- Genel kural 2, Spyware/Stalkerware: "kullanıcı verisini politikaya
  uygun işlevle ilgisiz toplayan/dışarı sızdıran/paylaşan" kod yasak.
  Sende dışarı veri yok; bu iddianın kanıtı mağaza açıklaması, politika
  ve (sideload'da) INTERNET izninin yokluğu. [resmi + ölçüm]
  https://support.google.com/googleplay/android-developer/answer/18258653
- Gelecek (yapay zekalı sürüm): 15 Temmuz 2026 duyurusu, Kullanıcı Verileri
  şartlarının "üçüncü taraf yapay zeka entegrasyonlarına" da uygulandığını
  netleştirdi (sınırlı kullanım, açıklama, onay). Cihaz içi bir model
  kullansak bile bildirim metnini modele vermek için ayrı açıklama ve
  onay planla. [resmi + çıkarım]
  https://support.google.com/googleplay/android-developer/answer/17134731
- Play sürümünde Billing yüzünden INTERNET izni var. "Bildirim dinleyici +
  INTERNET" otomatik tarayıcıların sevdiği bir kombinasyon olabilir. [çıkarım]
  - Seçenek: Play manifestinden INTERNET/ACCESS_NETWORK_STATE'i
    `tools:node="remove"` ile çıkarmak. Billing'in bozulup bozulmadığı
    resmi sayfada yok; iç test + lisans test hesabıyla denenmeden
    yapılmamalı. [doğrulanamadı]
    https://developer.android.com/google/play/billing/integrate

### 8.2 Marka, meta veri ve görsel kuralları (en olası ret sebepleri)

- Başlık en fazla 30 karakter; emoji, TAM BÜYÜK HARF (marka değilse),
  tekrarlı özel karakter, fiyat/sıralama ifadesi yok. "Uygulamaların veya
  markaların veri karşılaştırması" ihlal örneği. [resmi]
  https://support.google.com/googleplay/android-developer/answer/18258653
- Taklit politikası: başka şirketle ilişki ima etme; başka uygulamaya
  benzeyen ad/ikon yok. [resmi]
- Meta (WhatsApp) marka kuralları: WhatsApp adını kendi ürün adının
  parçası yapma; ortaklık/onay ima etme; "WhatsApp'ı malzemenin en
  belirgin öğesi yapma"; resmi dışı logo kullanma. Mağaza açıklaması için
  özel bir hüküm sayfada yok. [resmi, kısmen]
  https://www.meta.com/brand/resources/whatsapp/whatsapp-brand/
- Ekran görüntüsü kuralı: "yetkisiz üçüncü taraf ticari marka/logo"
  kullanma; bildirim çubuğunu temizle; operatör/bildirim gösterme. Bizim
  kanal listemizde telefondaki GERÇEK ikonlar çıkıyor: mağaza görsellerini
  uydurma demo verisiyle ve marka logosu öne çıkmadan üret. [resmi + çıkarım]
  https://support.google.com/googleplay/android-developer/answer/9866151
- Öneri: açıklamada kanal adları (WhatsApp, Instagram...) yalnız düz
  metin, bir kez, alt satırda, yanında "Adlar ilgili sahiplerinin ticari
  markasıdır; Sende bu şirketlerle bağlantılı değildir." notuyla. [çıkarım]
- "Silinen mesajı gör" vaadiyle konumlanma: pazar büyük (WAMR 100M+) ama
  kötüye kullanım yorumları ve politika riski var (araştırma 01). Hem
  Spyware tanımına yakın hem mesaj gizliliği. Mağaza metninde KULLANMA. [çıkarım]
- Mağazadaki vaat uygulamada yoksa ret. Ör. "internet izni yok" iddiası
  Play sürümünde yanlış (INTERNET izni var). Metne ölçülmemiş izin
  iddiası koyma; SORUNLAR S-14 kuralıyla uyumlu. [resmi + ölçüm]

### 8.3 Play Protect ve sideload

- Play Protect "gelişmiş dolandırıcılık koruması": internet kaynaklı
  (tarayıcı, mesajlaşma, dosya yöneticisi) sideload kurulumda uygulama
  RECEIVE_SMS, READ_SMS, NOTIFICATION_LISTENER (BIND_NOTIFICATION_LISTENER_
  SERVICE) veya ERİŞİLEBİLİRLİK'ten birini beyan ediyorsa kurulumu ENGELLER;
  "seçili pazarlarda". Kullanıcı geçersiz kılma yolu belgelenmemiş.
  Geliştirici için "Play Protect itirazı" düğmesi var. [resmi]
  https://developers.google.com/android/play-protect/warning-dev-guidance
  https://blog.google/intl/en-in/products/launching-enhanced-fraud-protection-pilot-in-india/
- Pazar listesi: Hindistan pilotu (Ekim 2024). Bir kurumsal SSS sayfası "2026
  başında 185 pazar, ~2,8 milyar cihaz" diyor ama liste vermiyor (blog,
  Google kaynağı yok). Türkiye'nin içinde olup olmadığı [doğrulanamadı]. [blog]
  https://bayton.org/android/android-enterprise-faq/enhanced-fraud-protection-enterprise/
- 22 Ekim 2026 yeniden okuma: resmi sayfa pazarları yine ADLANDIRMIYOR
  ("seçili pazarlarda etkin"). Bilinen yayılma: Hindistan pilotu (Ekim
  2024), Singapur, Tayland, Brezilya ve Malezya (Lowyat, 28 Nisan 2026:
  "Malezya'daki tüm Android kullanıcıları"). Türkiye hiçbir kaynakta
  GEÇMİYOR; "dahil değil" kanıtı da yok. [resmi + haber]
  https://developers.google.com/android/play-protect/warning-dev-guidance
  https://www.lowyat.net/2026/391257/google-rolls-out-enhanced-fraud-protection-play-protect/
- Aynı resmi sayfadaki "izinli kullanım" listesi: bildirim dinleyici için
  yalnız "giyilebilir donanıma bildirim aktaran sağlık ve fitness
  uygulamaları" sayılıyor. Sende bu kapsamda DEĞİL; yani Sende, Play
  Protect'in seçili pazarlarda internetten (tarayıcı, mesaj, dosya
  yöneticisi) kurulumunu engelleyebileceği sınıfa girer. Kullanıcıya dönük
  geçersiz kılma yolu belgelenmemiş. [resmi]
- Yönetilen Google Play veya Play'den kurulum etkilenmiyor. Bu, sideload'ı
  ikincil yol yapmak için ayrı bir sebep. [blog]
- Test için: Türkiye SIM'li gerçek bir telefonda GitHub bağlantısından
  APK kurmayı DENE ve sonucu yaz. Bu ortamda yapılamaz.
- Android 13+ "kısıtlanmış ayar" (araştırma 03) Play'den kurulan
  uygulamada çıkmıyor; bu da Play'in kullanıcı deneyimi avantajı.
  Google'ın bunu Esper'e doğruladığı bildirilmiş. [blog]
  https://www.esper.io/blog/android-13-sideloading-restriction-harder-malware-abuse-accessibility-apis

### 8.4 Gerçek hayat: reddedilme hikâyeleri ve benzer uygulamalar

- Bildirim dinleyiciye özgü, "Play politikası yüzünden reddetti" anlatısı
  (belirgin açıklama, izin beyanı vb.) hâlâ bulunamadı: Stack Overflow API,
  Hacker News, GitHub issue taramaları ve ikinci turdaki ek aramalar
  (NotificationListenerService + "prominent disclosure"/"declaration"
  birleşimleri). Arama motoru yalnız genel "belirgin açıklama" yazıları
  döndürdü. [doğrulanamadı]
- AMA tarihsel, bildirim dinleyiciye özgü bir "Play'den kaldırıldı" olayı
  var (Temmuz 2018). Sebep Play politikası değil, WhatsApp'ın şartları:
  [haber, 3 kaynak]
  - 3 Temmuz 2018 (XDA): WhatsApp'ın avukatları Can't Talk ve DirectChat
    geliştiricilerine mektup gönderdi: WhatsApp bağlantılı işlevi kaldırın
    ya da dava. İkisi de "standart Android API'si: Notification Listener ve
    Direct Reply" kullanıyor. Can't Talk Play'den kaldırıldı; DirectChat
    kaldı.
    https://www.xda-developers.com/whatsapp-sends-cease-desist-apps-native-android-api/
  - 5 Temmuz 2018 (Lowyat): iddialar belirsiz; tek net talep WhatsApp'a
    bağlanan işlevi kaldırmak. Şirketler yorum yapmadı.
    https://lowyat.net/2018/165604/whatsapp-files-cease-desist-orders-against-apps-that-dont-use-its-apis
  - 7 Temmuz 2018 (Xataka): WhatsRemoved+ "doğrudan Play'den atıldı";
    Google'ın gerekçesi diye "WhatsApp'a müdahale eden/müdahaleye izin
    veren uygulama şartlarını ihlal eder" aktarılıyor (DDA madde 4.9).
    WhatsApp bildirimi desteğini çıkarınca geri dönmüş. Bu detay yalnız
    Xataka'da; bağımsız doğrulayamadım. [tek kaynak]
    https://www.xatakandroid.com/comunicacion-y-mensajeria/whatsapp-empieza-combatir-apps-que-muestran-mensajes-que-hayan-sido-borrados
  - XDA yazısı, WhatsApp adının mağaza açıklamasında geçmesinin marka
    sorunu olabileceğini de söylüyor (görüş, kanıt değil).
  - Bugün (2026) WAMR gibi benzer uygulamalar Play'de yayında; yani olay
    sistematik bir yasak değil. Ama Sende WhatsApp mesajlarını okuyan bir
    uygulama olduğu için aynı yönden risk (Meta ya da Play'e şikâyet)
    sıfır değil. Cevabımız: yalnız OKUR, otomatik cevap/gönderme yok,
    WhatsApp'ı API ile kullanmaz, "silinen mesajı gör" vaadi yok. [çıkarım]
- İki GitHub projesi tam bu konuyu "bilinmeyen" olarak planlıyor: biri
  "bildirim dinleyici politika denetimi" ve "Play'de ekstra inceleme"
  uyarısı koymuş, diğeri "bildirim erişimi için Play ne istiyor" sorusunu
  açık bırakmış. Sonuç yok. [topluluk, planlama]
  https://github.com/theprinceraj/qoody/issues/28
  https://github.com/wkusnierczyk/sleevesight/issues/58
- Varoluş kanıtı: Notisave (10M+), Notification History (svprdga), WAMR gibi
  bildirim okuyan uygulamalar Play'de bugün yayında. Kategori yasaklı
  değil. [ölçüm + araştırma 01]
  https://play.google.com/store/apps/details?id=com.tenqube.notisave
- "Belirgin açıklama eksik" reddi: bir XDA kullanıcısı, izin penceresi
  gösterip "Insufficient prominent disclosure" ile reddedilmiş; daha ayrıntılı
  açıklama ekleyince onaylanmış. Sayfa bot korumalı; arama özetinden.
  [forum, tek kaynak]
  https://xdaforums.com/t/how-to-address-issue-found-missing-prominent-disclosure-when-publish-app-to-playstore.4554619/
- Üst üste üç ret, hepsi düzeltilip 6 günde geçti (B4X forumu, 25-31 Mayıs
  2021; konu eski ama süreç tipik). [forum, tek kaynak]
  https://b4x.com/android/forum/threads/missing-information-in-prominent-disclosure.131063/page-2
  - Ret 1: "belirgin açıklama, konum çalışma zamanı izninden ÖNCE
    görünmeli ve hangi özelliklerin konumu kullandığını söylemeli".
  - Ret 2 (27 Mayıs): izin incelemesi iletisi; forumdaki öneri: yalnız
    versionCode değil versionName da artırılsın (kesin değil).
  - Ret 3 (28 Mayıs, sürüm 83): "gizlilik politikanız şartlara uymuyor":
    aktif URL, PDF olmayan, düzenlenemeyen, konumu anlatan ve hem mağazadan
    hem uygulamadan bağlantılı bir sayfa istendi. 31 Mayıs'ta yayına çıktı.
  - Ders: ret e-postasındaki cümleyi aynen çöz; her turda üç şeyi birlikte
    kontrol et: açıklama metni + izinden önce görünüm, politika URL'si,
    mağaza + uygulama içi bağlantı. [çıkarım]
- 2019 HN yorumu: SMS özelliği çıkarılan yeni sürüm yüklenince bile
  Console "reddedildi" durumundan çıkmamış; eski bir test kanalındaki
  sürüm izni taşıdığı için; geliştirici yeni uygulama açmak zorunda
  kalmış. Ders: kullanılmayan test kanallarını kapat, riskli izni hiç
  ekleme. Resmi sayfa da aynı: kullanılmayan kanalları kapatmayı
  öneriyor. [forum + resmi]
  https://news.ycombinator.com/item?id=19429394
  https://news.ycombinator.com/item?id=19429729
  https://support.google.com/googleplay/android-developer/answer/9214102
- 2026 HN yorumu: "mağaza onayları piyango; ben başka bir uygulamanın
  ekran görüntüsüyle reddedildim, birkaç ret sonra anladım". Tek
  kişilik. [forum]
  https://news.ycombinator.com/item?id=49217612

## 9. Mağaza girdisi: sınırlar ve varlıklar

- Uygulama adı 30, kısa açıklama 80, uzun açıklama 4000 karakter; sınır
  tam ve yarım genişlikli karakterler için aynı. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9859152
- İletişim e-postası ZORUNLU; web sitesi önerilir. [resmi]
- İkon: 512x512 px, 32 bit PNG (alfa), en fazla 1024 KB. Rozet/yazı yok
  (sıra, fiyat). Yanıltıcı simge yasak; "yeni mesaj noktası" gibi
  yanıltıcı sembolü Play özellikle örnek veriyor. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9866151
  https://support.google.com/googleplay/android-developer/answer/18258653
- Öne çıkan görsel (feature graphic): 1024x500, JPEG veya 24 bit PNG
  (ALFA YOK). Kenarlara yakın kesilme alanlarına logo/ana ekran koyma. [resmi]
- Telefon ekran görüntüsü: yayın için cihaz türleri toplamında en az 2
  ekran görüntüsü; her cihaz türü için en fazla 8. JPEG veya 24 bit PNG,
  alfa yok; her kenar 320-3840 px, uzun kenar kısa kenarın 2 katını
  aşamaz. [resmi]
- Öneri kuralı: tavsiye/öne çıkarılma uygunluğu için en az 4 ekran
  görüntüsü, 9:16 ise en az 1080x1920. Gerçek uygulama deneyimi; el/parmak
  yok; sloganlar görüntünün en fazla %20'si; "En iyi, #1, Ücretsiz,
  Şimdi indir" gibi ifadeler yok. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9866151
- Her görsele alt metin (≤140 karakter, "görsel" kelimesi yok). [resmi]
- Kısa açıklamada: emoji, tekrar eden noktalama, ★ gibi semboller, "şimdi
  indir" gibi harekete geçirme çağrısı, "En iyi" gibi ifadeler yok;
  gereksiz anahtar kelime sıralamayı etkilemez ama kötü deneyimdir. [resmi]
- Açıklamada sadece anonim olmayan, kaynaklı kullanıcı yorumu kullanılır.
  Uygulama adını TAM BÜYÜK HARFLE yazma. [resmi]
  https://support.google.com/googleplay/android-developer/answer/18258653
- Google'ın görsellerinizi tanıtımda kullanabilmesi için lisans verirsiniz
  (DDA madde 6). [resmi]
- Sürüm notu (What's new): dil başına en fazla 500 Unicode karakter;
  promosyon amaçlı olamaz, kullanıcıdan eylem istemez ("puan ver" vb.
  yok). Dil etiketleri ayrı satırlarda yazılır. Bir dilde aşım tüm
  sürümü bloke edebilir (blog). [resmi, 22 Ekim 2026]
  https://support.google.com/googleplay/android-developer/answer/9859348
- Mağazada herkese görünen geliştirici bilgisi: kişisel hesapta yasal ad,
  ülke, geliştirici e-postası (bkz. 2.3). Kuruluşta ayrıca telefon.
- Uzun açıklamada kaçınılacak: uzun anahtar kelime listeleri, tekrar,
  "veri karşılaştırması". Kısa tut: uzunluk ve tekrar da ihlal
  sayılabilir. [resmi]

## 10. Türkçe ASO (mağaza arama) stratejisi

### 10.1 Play nasıl okuyor (kaynaklar ticari/blog; Google ağırlık vermiyor)

- Gizli anahtar kelime alanı yok; başlık, kısa ve uzun açıklamanın görünen
  metni okunuyor. Başlık en güçlü, kısa açıklama ikinci. [blog]
  https://appfollow.io/blog/google-play-aso-keywords
  https://muratuysal.com/blog/mobil-uygulama-bulunabilirligi
- Aynı anahtar kelimeyi çok tekrarlamak sıralamayı katlamaz; "yoğunluk
  yüzdesi" iddiaları kaynaksız. [blog]
- Google'ın resmi sayfası: gereksiz anahtar kelime sıralamaya yardım etmez
  ve kötü deneyimdir; tekrar eden/ilgisiz kelime askıya alma sebebi
  olabilir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9866151
  https://support.google.com/googleplay/android-developer/answer/9859152
- Mağaza girdisi çevirisi: her dil için ayrı başlık/açıklama girilir;
  yoksa otomatik çeviri gösterilir. Türkçe `tr-TR`, Arapça `ar`,
  Fransızca `fr-FR`. Play'in ücretsiz makine çevirisi ve ücretli insan
  çevirisi (kelime başı ~0,07 USD'den, 7 gün) var. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9844778
- Dil/ülke yerelleştirme: Türkçe ekler (ör. "bildirimler/bildirim") için
  Play'in nasıl davrandığını gösteren kaynak bulunamadı. [doğrulanamadı]

### 10.2 Play TR'de gözlemim (21 Ekim 2026, hl=tr gl=TR)

- "bildirim geçmişi", "bildirim kaydedici", "bildirim yöneticisi": kalabalık,
  yerelleştirilmiş başlıklı yabancı uygulamalar (Bildirim Geçmişi,
  Bildirim Kaydedici, Notisave, NotiHub...). Baş terimleri kapmak zor. [ölçüm]
- "müşteri mesajları", "işletme mesaj paneli", "tüm mesajlar tek yerde",
  "whatsapp instagram mesajları tek ekranda", "sipariş mesajları": sonuçlar
  toplu SMS, CRM, SMS uygulaması gibi ilgisizler. Doğrudan rakip YOK. [ölçüm]
- "randevu mesajları kaçırma": randevu uygulamaları (Randevumuz, Kolay
  Randevu - Güzellik Cepte...) çıkıyor. Hedef kitle bu aramayı yapıyor;
  ama bizim ürün randevu uygulaması değil. [ölçüm]
- Play arama HACMİ vermiyor; yalnız sıralama gözlemledim. Hangi kelimenin
  kaç kişi tarafından arandığı [doğrulanamadı].
- Ad çakışması/marka: ayrı belge (MARKA_ADI_KONTROL.md). Burada yalnız:
  "sende" aramasında ilk sonuçlar alakasız (ör. com.sende.kibris,
  org.gebze.bel.sende). "Sende" tek başına anlam taşımıyor, ASO'da zayıf;
  başlıkta açıklayıcı ek şart. [ölçüm]

### 10.2b Ek ölçümler (22 Ekim 2026, aynı yöntem)

- Sınırlar: tek gün, tek makine (çıkış ağı Türkiye'de değil; yalnız
  hl=tr ve gl=TR parametresi verdim). Kişiselleştirme ve konum yanlılığı
  olabilir. Mutlak sıralama değil, örüntü okuyun. [ölçüm]
- Ek (çekim eki) testi: "bildirim geçmişi" ve "bildirim geçmişleri"
  aramaları 30'ar sonuç verdi, 21'i ORTAK; ilk 5 aynı uygulamalardan ama
  sırası farklı. Yani Play Türkçe ekleri büyük ölçüde aynı kavram sayıyor.
  Sonuç: "bildirim / bildirimler / bildirimleri" diye yığma yapma; doğal
  cümlede bir kez geçmesi yeterli görünüyor. [ölçüm, tek gün]
- Dil karışması: Türkçe sorguda İngilizce başlıklı uygulamalar da çıkıyor
  (ör. "bildirim geçmişi" için "Notification History" 6. sırada). Play
  anlam/çeviri eşleştirmesi yapıyor gibi; nedeni açıklama ya da etiket de
  olabilir. [ölçüm + çıkarım]
- Başlık etkisi uyumlu: "bildirim geçmişi" aramasında ilk 5 sonucun
  başlığında bu ifade geçiyor. Bu nedensellik kanıtı değil, yalnız uyum. [ölçüm]
- İlk 5 sonuç, seçili sorgular (başlık | paket):
  - "müşteri mesajları": WaMessage Bulk WP Toplu Mesaj; JivoChat –Canlı
    Destek Sistemi; Chatfuel: Auto Reply AI for WA; Shopxare: Müşteri
    Mesajları; Aircall. Hepsi toplu mesaj, canlı destek ya da CRM.
  - "işletme mesajları": WhatsApp Business ilk; sonra toplu mesaj
    araçları.
  - "silinen mesajları gör": ilk 6'nın hepsi "silinen mesaj" uygulaması
    (WAMR: silinen mesajlar, Silinen Mesajları Görüntüle...). Kalabalık
    ve politika açısından riskli konum. KULLANMA.
  - "whatsapp bildirim" ve "whatsapp business bildirim": ilk iki sonuç
    WhatsApp'ın kendisi. Marka adıyla yarışmak anlamsız ve riskli.
  - "bildirim kaydedici": Bildirim Kaydedici (com.tinyfalcon...), Notisave,
    Notifications History - Notix, Bildirimleri kaydet. Kalabalık.
  - "randevu mesajları": 50 sonuç; MHRS, Randevumuz, RandevuApp...
    Randevu uygulamaları. "esnaf mesaj" sonuçları alakasız.
- Başlık notu: "Shopxare: Müşteri Mesajları" (com.shopxare.app) zaten
  "[Ad]: Müşteri Mesajları" kalıbını kullanıyor; ekip için müşteri
  mesajlaşma gelen kutusu (canlı destek) olarak tanıtıyor. Bizim önerilen
  başlık aynı kelime çiftini taşıyor. Karışmayı azaltmak için açıklamada
  "bildirimlerinden" vurgusu şart; başlık alternatifi:
  "Sende: Bildirim ve Mesajlar" (27) ya da "Sende: Mesaj Bildirimleri" (25).
  Başlık mağaza deneyiyle test EDİLEMEZ (yalnız grafik ve açıklama metni
  denenir); başlığı güncellemeyle değiştirebilirsin. [çıkarım]

### 10.3 Önerilen yerleşim (karakter sayıları ölçüldü)

- Başlık (≤30): "Sende: Müşteri Mesajları" (24). Ana kelime "mesaj",
  "müşteri". WhatsApp/Instagram başlıkta olmasın (Meta kuralı, politika).
  [çıkarım]
- Yedek: "Sende: İşletme Mesajları" (24).
- Kısa açıklama (≤80), Play sürümünde "internetsiz" deme:
  - "Müşteri mesajlarını kaçırma: seçtiğin uygulamaların bildirimleri
    tek ekranda." (77)
  - İlk kelimeler önemli: kullanıcı "devamını oku" öncesini görüyor. [blog]
- Uzun açıklama: ilk paragrafta sorun + çözüm ("sipariş, randevu, fiyat
  sorusu"), sonra özellik maddeleri, sonra gizlilik, en sonda kanal
  adları ve marka notu. Anahtar kelimeler doğal cümle içinde 2-3 kez:
  müşteri mesajı, bildirim, tek ekran, sipariş, randevu, işletme, esnaf. [çıkarım]
- "bildirim geçmişi/kaydedici/yöneticisi" terimleri açıklamada bir iki
  kez geçsin (arama niyeti eşleşmesi), ama başlıkta yarışma. [çıkarım]
- Mevcut RELEASE.md taslağı iyi ama: (1) kanal adlarını başa koyuyor,
  (2) "Hiçbir şey bir sunucuya gönderilmez" doğru, "hiçbir şey ağa
  çıkmaz" Play sürümünde yanlış olurdu. İkisini düzeltelim. [ölçüm]
- Mağaza deneyleri (resmi sayfa, 22 Ekim 2026): yalnız YAYINDAKİ
  uygulamalar için. Aynı anda ya bir "varsayılan grafik" deneyi (ikon,
  öne çıkan görsel, ekran görüntüleri) ya da en fazla beş "yerelleştirilmiş"
  deney (grafik + açıklama metni, en fazla beş dilde). En fazla 2 varyant.
  Sonuç "daha fazla veri gerekli" çıkabilir. Yani üretime çıkmadan
  yapılamaz, ilk gün için plan değil. [resmi]
  https://support.google.com/googleplay/android-developer/answer/12053285

## 11. İnceleme süreleri ve sık ret nedenleri

- Resmi: standart yayınlama "mümkün olan en kısa sürede"; "bazı hesaplar
  için daha dikkatli inceleme: 7 güne kadar, istisnai olarak daha uzun".
  Hedef kitle bölümü için de aynı cümle var. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9859751
  https://support.google.com/googleplay/android-developer/answer/9867159
- Üretim erişimi incelemesi: "genelde 7 gün veya daha az". [resmi]
- İzin beyan formu gereken uygulama: "birkaç hafta"; Bizde gerekmiyor. [resmi]
- Kendi gözlemim: yeni hesap + ilk uygulama + özel erişim izni = "dikkatli
  inceleme" ihtimali yüksek. 7 günü planla. [çıkarım]
- Ön inceleme kontrolleri (pre-review checks): değişiklik yapınca Console
  15 dakika içinde bitirir, eksik beyanları ve belirli cihazdaki yüksek
  çökmeyi önceden gösterir. Kritik sorunları düzeltmeden yayınlayamazsın. [resmi]
  https://support.google.com/googleplay/android-developer/answer/14807773
- Google'ın "yayın ipuçları": açıklama net ve yeterli uzunlukta olsun;
  vaat ettiğini yap; giriş gerekiyorsa demo hesap ver; testi tamamla;
  reddedilirsen düzelt ve tekrar gönder; çok sayıda ret askıya alma
  getirebilir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/15191715
- Politika durumu (Policy status) sayfası, e-postadan daha kalıcı ve
  genelde ihlal edilen politikayı adlandırır. [blog]
  https://www.testerscommunity.com/blog/google-play-app-rejected
- Sık ret/uyarı nedenleri (Google'ın kendi metninde geçenler):
  - Meta veri: yanıltıcı/aşırı/ilgisiz açıklama, anahtar kelime yığma.
  - Veri güvenliği formu gerçekle uyuşmuyor.
  - Gizlilik politikası linki yok/açılmıyor/PDF.
  - Belirgin açıklama + onay yok.
  - Çalışmayan işlev (çökme, eksik ekran, giriş yapılamıyor).
  - Mağaza vaadi ile uygulama farkı.
  [resmi]
  https://support.google.com/googleplay/android-developer/answer/15191715
  https://support.google.com/googleplay/android-developer/answer/10787469
- Ret sonrası: düzeltmeden itiraz boşuna; "gerçek değişiklik" olmayan itiraz
  büyük olasılıkla tekrar reddedilir. [blog]
  https://www.testerscommunity.com/blog/google-play-app-rejected

## 12. Android geliştirici doğrulaması (sideload) zaman çizelgesi ve etkisi

### 12.1 Ne oldu, ne zaman

- 19 Mart 2026 (Google duyurusu): doğrulama "gelişmiş akış" (advanced flow)
  ve "sınırlı dağıtım hesapları" ile birlikte; Ağustos 2026'da global
  açılış; kurallar bir ay sonra. [resmi]
  https://android-developers.googleblog.com/2026/03/android-developer-verification.html
- 30 Mart 2026: süreç Play Console ve Android Developer Console'da herkese
  açıldı. Play Console'da doğrulanmış geliştiricinin uygun Play uygulamaları
  OTOMATİK kaydedilir. [resmi]
  https://android-developers.googleblog.com/2026/03/android-developer-verification-rolling-out-to-all-developers.html
- Haziran 2026: Google "Play'deki uygulamaların %99'u otomatik kaydedildi"
  diyor. [resmi]
  https://android-developers.googleblog.com/2026/06/android-developer-verification.html
- 30 Eylül 2026 (geçti): sertifikalı Android 7+ cihazlarda, katılımcı
  mağazalardan (Google Play, Samsung Galaxy Store, Xiaomi GetApps, HONOR,
  OPPO, vivo, Transsion) kurulumlarda kayıt zorunlu — YALNIZ Brezilya,
  Endonezya, Singapur, Tayland. [resmi]
  https://developer.android.com/developer-verification/guides
- AYNI TARİH, Play tarafı (politika takvimi, 15 Temmuz 2026 duyurusu):
  "Play Console Requirements" politikası gereği 30 Eylül 2026'dan itibaren
  tüm Play paketleri kayıtlı olmalı; kayıtsız paketler Play'den kaldırılır.
  Play'deki uygulamaların %99'u otomatik kaydedilmiş. Play'de dağıtıyorsan
  tüm form faktörleri (telefon, TV, saat...) kayıtlı olmalı. Yeni
  uygulamalar için de otomatik kayıt denenir. [resmi]
  https://support.google.com/googleplay/android-developer/table/12921780
  https://developer.android.com/developer-verification/guides/faq
- 2027: kapsam küresel. Kesin tarih yok. Türkiye ilk dalgada değil. [resmi]
  https://developer.android.com/developer-verification
  Bir Türkçe haber başlığı da "doğrulama 2027'de Türkiye'ye geliyor"
  diyor (sayfayı açmadım, yalnız arama özeti; resmi kaynak değil).
  [haber, doğrulanamadı]
  https://pasqualepillitteri.it/tr/news/21034/android-apk-kapiyi-kapatiyor-dogrulama-turkiye-2027
- Sideload (doğrudan indirme, başka mağaza) 30 Eylül 2026'da etkilenmedi:
  katılımcı mağaza değil. 2027'ye kadar kayıt önerilir. [resmi]
  https://developer.android.com/developer-verification/guides/faq
- Kayıtsız uygulama yine kurulabilir: ADB ile veya "gelişmiş akış" ile.
  Gelişmiş akış: geliştirici modu → "zorlanmıyorum" onayı → yeniden
  başlatıp kimlik doğrulama → 1 gün bekleme → biyometri/PIN → "7 gün veya
  süresiz" izin; her uygulamada "Yine de yükle" uyarısı. [resmi]
  https://android-developers.googleblog.com/2026/03/android-developer-verification.html
- Ücret: yalnız Play dışı dağıtanlar için Android Developer Console
  "Tam dağıtım" hesabı 25 USD. Play Console hesabı olan geliştirici,
  Play dışı uygulamalarını da oradan kaydedebilir; ek ücretten söz
  edilmiyor (kesin "ücretsiz" demiyor). [resmi]
  https://developer.android.com/developer-verification/guides/faq
- "Sınırlı dağıtım": kimlik/ücret yok, en fazla 20 cihaz (öğrenci/
  hobi/test). Müşteri satışı için uygun DEĞİL. [resmi]

### 12.2 Sende'ye etkisi

- Play sürümü: ek iş yok denecek kadar az. Play Console hesabını
  doğrulatmak yeterli; paket adı otomatik kaydedilir. Kaydedilemezse
  elle "anahtar ekle + imzalı APK ile talep" yolu var. [resmi]
  https://support.google.com/googleplay/android-developer/answer/16984799
  Kayıt durumu bildirimleri YALNIZ kimlik doğrulaması tamamlandıysa
  gelir; bu yüzden hesabı açar açmaz kimliği tamamla. D-U-N-S süresi
  burada "28 güne kadar" (3 Eylül 2025 SSS), Play yardım sayfasında "30
  güne kadar"; ikisini de planla.
- Paket adı çatışması: otomatik kayıt kuralı: kurulumların %50'sinden
  fazlasını taşıyan anahtar önceliklidir; hiçbir anahtarda 50 kurulum
  yoksa "ilk gelen alır". Yani com.sende.app'i Play'de erken oluşturup
  kaydı almak bu yarışta öne geçirir. [resmi + çıkarım]
- Sideload sürümü (GitHub APK): 2027'de Türkiye'ye gelince doğrulanmamışsa
  kullanıcı 1 günlük "gelişmiş akış" kurmak zorunda kalır. Bu, "ellerinle
  indir" yolunu fiilen kapatır. Çözüm: sideload APK'yı da kaydet. [çıkarım]
- Kayıt için sideload APK'nın imza anahtarı sana ait ve gizli olmalı;
  herkese açık test anahtarı uygun değil. [çıkarım]
- Yol: Play Console'daki "Android developer verification" sayfasından
  Play dışı paketi ekle; APK'ya verilen kod parçacığını koyup imzala ve yükle
  (gerçek APK'yı yüklemek gerekmiyor; yalnız kayıt tetiklenir). [resmi]
  https://support.google.com/googleplay/android-developer/answer/16984799
- Aynı paket birden çok anahtarla kaydedilebilir (Console'da). Yani A/B
  seçeneklerinin ikisi de teknik olarak mümkün. [resmi]
- Programa tepki büyük (Ekim 2026'da Hacker News'te 338 puanlık bir
  tepki başlığı var). Bizim için önemli olan yalnız tarihler ve kayıt
  adımları; siyasi tartışmaya girmiyorum. [forum]
  https://news.ycombinator.com/item?id=49917761
- Önerilen Android Studio ipucu: imzalı AAB/APK üretirken kayıt durumu
  görünür (Android Studio Panda 4+). [resmi]
  https://developer.android.com/developer-verification/guides/faq

## 13. Ülke seçimi, fiyat ve TRY

### 13.1 Ülke

- Kanallar aynı ülke ayarını paylaşır; üretim ülkeleri değişince testler de
  etkilenir (iç test hariç). İlk yayında "Production > Countries/regions". [resmi]
  https://support.google.com/googleplay/android-developer/answer/9845334
- Staged rollout ilk yayında kullanılamaz; güncellemelerde yüzde ve ülke
  seçilebilir. Başlayan rollout'tan ülke ÇIKARILAMAZ. [resmi]
  https://support.google.com/googleplay/android-developer/answer/6346149
- Öneri sırası: 1) Türkiye; 2) Arapça (ar) ve Fransızca (fr) çevirisi
  hazır, AB dışı pazarlar (ör. BAE, ODEME.md/RELEASE.md'de anılanlar);
  3) Fransa ve diğer AB ülkeleri, DSA "trader" kararından sonra. Bu benim
  planım, kaynak değil. [çıkarım]
- remotevergi, tartışmayı önlemek için Türkiye vitrininden çıkmayı bir
  "savunma seçeneği" olarak anlatıyor. Türkiye bizim ilk pazarımız
  olduğundan bu seçenek bize uymaz; karar mali müşavirde. [ticari]
  https://remotevergi.com/app-store-play-store-vergi/
- Türkiye dağıtım ve fiyat aralığı tablosu dinamik sayfada; okunamadı.
  TRY alt fiyat sınırı 2026 için [doğrulanamadı]. Console, geçersiz fiyatı
  reddeder. Tek bulunan rakam 2015'ten: Google, Türkiye dahil 17 ülkede
  asgari uygulama/uygulama içi ürün fiyatını 2,00 TL'den 0,59 TL'ye
  indirmişti (Android Developers Blog, Kasım 2015). 11 yıllık, TRY enflasyonu
  yüzünden bugün geçerli değildir; kullanma.
  https://android-developers.googleblog.com/2015/11/minimum-purchase-price-for-apps-and-in.html
  https://webrazzi.com/2015/11/18/google-playde-taban-fiyat-059-tlye-dustu/
  https://support.google.com/googleplay/android-developer/answer/10532353

### 13.2 Fiyat

- "Ücretsiz" olarak oluşturduğun uygulama sonradan ÜCRETLİ yapılamaz;
  ücretli → ücretsiz mümkün. Bizim plan (ücretsiz + uygulama içi destek)
  uyumlu. [resmi]
  https://support.google.com/googleplay/android-developer/answer/6334373
- 27 Ekim 2025'ten beri fiyat şablonları yok; fiyatlar ürün bazında
  yönetilir. ODEME.md "Fiyat şablonu" kullanmıyor, iyi. [resmi]
- Abonelik temel planı fatura dönemleri: haftalık, 4 hafta, aylık, 2, 3,
  4, 6, 8 aylık, yıllık. Bizim 1/3/6/12 ay planları mümkün. Ürün kimliği
  ve temel plan kimliği bir kez oluşturulunca değişmez/silinmez. [resmi]
  https://support.google.com/googleplay/android-developer/answer/140504
- Abonelik politikası: koşulları ve fiyatı net göster; abonelik yönetme
  /iptal yolunu uygulamada açıkça göster ve kolay bir çevrim içi iptal
  yöntemi sun. Bizde menüye "Aboneliği yönet" bağlantısı (Play abonelikler
  sayfası) eklenmeli. [resmi]
  https://support.google.com/googleplay/android-developer/answer/140504
- Ödeme politikası: uygulama içi özellik/içerik için Play Billing ŞART;
  "vergiden muaf bağışlar" istisna. Geliştiriciye "çay ısmarla" ürünleri
  vergiden muaf bağış değil → Play Billing doğru yol. Uygulamada
  kullanıcıyı başka ödeme yöntemine (ör. harici bağış linki) YÖNLENDİRME;
  Play sürümünde yasak. Sideload sürümünde serbest. [resmi + çıkarım]
  https://support.google.com/googleplay/android-developer/answer/18258653
- Mağaza listesinde uygulama içi özelliklerin ek ücret gerektirdiği
  açıkça belirtilmeli. [resmi]
- Fiyat değişikliği: yeni satın alımlara uygulanır; mevcut aboneler eski
  fiyatta kalır. Mevcut abonelerin fiyatını artırmak "ret etme" (opt-out)
  yalnız bölge/sıklık/miktar ölçütleri sağlanınca mümkün, aksi hâlde
  kullanıcı onayı gerekir. TRY'de kur oynar; kuru yılda birkaç kez gözden
  geçirmeyi planla. [resmi]
  https://support.google.com/googleplay/android-developer/answer/140504
  https://support.google.com/googleplay/android-developer/answer/12124625
- ODEME.md fiyat çıpaları USD (5/10/20/45). Türkiye için TRY'yi elle gir;
  Play, dönüşümde yerel fiyat örüntülerini uygular. [resmi]
  https://support.google.com/googleplay/android-developer/answer/6334373

## 14. Çok dil: per-app language, locale config, values-xx

- Android 13+ kullanıcıya "Uygulama dilleri" ayarını verir. Uygulama
  `android:localeConfig` bildirmezse kullanıcı uygulamanın dilini
  sistemden ayrı seçemez. [resmi]
  https://developer.android.com/guide/topics/resources/app-languages
- Kolay yol (AGP 8.1+): `androidResources { generateLocaleConfig = true }`
  ve `res/resources.properties` içinde `unqualifiedResLocale=en-US`.
  Elle yazılmış LocaleConfig dosyası varsa derleme patlar. [resmi]
- Bizde: values (İngilizce), values-tr, values-ar, values-fr var; locale
  yapılandırması YOK; MainActivity `ComponentActivity`. [ölçüm]
- Kurucunun I-98 isteği "dil otomatik, açılışta soru yok". Sistem dili
  zaten kullanılıyor. Yani uygulama içi dil seçici ŞART DEĞİL; yalnız
  `localeConfig` ekleyip Android 13+ ayarlarında dil görünmesini sağla. [çıkarım]
- Uygulama içi seçici eklenecekse: Compose'da `setApplicationLocales`
  yalnız `AppCompatActivity` ile çalışır; Android 12 ve altı için
  `AppLocalesMetadataHolderService` gerekir. Şimdilik gerekmiyor. [resmi]
- Eksik dil çevirisi eklenmişken `generateLocaleConfig` açılırsa
  yarım çevrilmiş dil de listede görünür; yalnız hazır dilleri
  (tr, en, fr, ar) kaynakta tut. [resmi]
- Mağaza dili ayrı konu: varsayılan dil Console'da seçilir; öneri
  İngilizce (en-US) varsayılan + tr-TR, ar, fr-FR çevirileri. Türkiye
  kullanıcısı Türkçe görür. [çıkarım]
  https://support.google.com/googleplay/android-developer/answer/9844778
- Arapça RTL: `supportsRtl=true` var; mağaza görselleri için de Arapça
  ekran görüntüsü seti düşünülebilir (ikinci dalga). [ölçüm + çıkarım]

## 15. Sürüm numarası stratejisi

- `versionCode` her yüklemede artmalı; daha önce kullanılan değer tekrar
  yüklenemez. Play'in üst sınırı 2.100.000.000. [resmi]
  https://developer.android.com/studio/publish/versioning
  https://support.google.com/googleplay/android-developer/answer/17367361
- `versionName` yalnız kullanıcıya görünür; Semantik Sürümleme öneriliyor. [resmi]
- Tarih tabanlı şema (ör. YYYYMMDDHH = 2026102114, yani 2,03 milyar)
  sınıra çok yakın: dakika eklenemez, aynı saatte iki derleme aynı
  kodu üretir ve aynı kod ikinci kez yüklenemez. Kullanma. [resmi + ölçüm]
- Önerim: `versionCode = major*1_000_000 + minor*10_000 + patch*100 + derleme`
  (ör. 1.0.0 derleme 1 → 1_000_001). Ana sürüm 2000'e gelmeden 2,1
  milyar sınırına ulaşmaz. Play ve sideload aynı formülü kullanır; iki
  paket adı varsa (Bölüm 6.2 Seçenek A) sayaçlar bağımsızdır. [çıkarım]
- Şimdiki durum: versionCode=1, versionName="0.1.0" (elle). İlk Play yüklemesi
  için 1.0.0 (kod 1_000_000) ve her kapalı test sürümü +1. [ölçüm]
- Her güncellemede kapalı testteki 12 kişi yeni sürümü almalı; sürümü
  güncellemek geri bildirim döngüsünü gösterir (form bunu soruyor). [resmi]

## 16. Aşamalı yayın, Pre-launch report, kalite

- Aşamalı yayın: yalnız GÜNCELLEMELERDE; yüzde seçilir, otomatik
  artmaz; durdurulabilir/devam ettirilebilir; kullanıcılar bildirim
  almaz. Ülke seçerek de yapılabilir. [resmi]
  https://support.google.com/googleplay/android-developer/answer/6346149
- İlk üretim sürümü %100 çıkar; riski azaltmak için ÜLKE kısıtla (önce
  Türkiye). Bir sonraki sürümden itibaren %5-20 ile başla. [çıkarım]
- Pre-launch report: AAB yüklenince Google'ın cihaz laboratuvarında
  kurulur ve birkaç dakika "gezilir" (yazı, dokunma, kaydırma);
  kararlılık, uyumluluk, performans, erişilebilirlik raporu. Kapasite
  varsa çalışır. [resmi]
  https://support.google.com/googleplay/android-developer/answer/7002270
- Sende için sınır: tarayıcı robot, sistem "Bildirim erişimi" ayarını
  büyük olasılıkla açamaz; rapor boş durumu görecek. Bu yüzden ilk açılış
  ve boş durum ekranı çökmemeli, anlaşılır olmalı. Bunu Robo betiği ile
  zenginleştirmek mümkün; Sende'de gerekmez. [çıkarım]
- Rapor dil tercihi (5 dile kadar) ve derin bağlantı (3'e kadar)
  verilebilir. [resmi]
- Kalite: vitals sınırlarının üstünde kalırsan görünürlük düşer (bkz. 6.3).
  İlk 28 günlük veri üretim sonrası birikir. [resmi]
- Açık (open) test: üretim erişiminden sonra açılır; ek bir "beta"
  kanalı için faydalı. Geri bildirim herkese açık puanı etkilemez. [resmi]

## 17. Play'e "Sende" için pratik riskler (özet)

- Kapalı test sayısı/engagement yetersiz → "ek test gerekli". Çare:
  15-20 gerçek esnaf, haftalık güncelleme, geri bildirimi kaydet.
- İnceleyici özel erişim iznini veremez → boş ekran → "çalışmıyor". Çare:
  test bildirimi düğmesi + not + video.
- Belirgin açıklama zayıf → ret. Çare: izin öncesi iki cümlelik zorunlu
  metin + onay düğmesi + gizlilik bağlantısı.
- Veri güvenliği "evet, topluyor" yanlış beyanı. Çare: resmî tanıma göre
  "toplanmıyor".
- Marka (WhatsApp...) başlıkta/öne çıkarılırsa Meta ve Play kuralı. Çare:
  ad ve görseli temiz tut.
- Gerçek simge ve konuşmalar içeren ekran görüntüsü. Çare: uydurma veri.
- Sideload engeli (Play Protect, "kısıtlanmış ayar"). Çare: Play ana yol.
- Billing kütüphanesi eskir (Ağustos 2027 → 9). Çare: Kotlin 2.3 planı.
- Test anahtarıyla dağıtım. Çare: ayrı özel anahtar.
- "Yetersiz etkileşim" ile kapalı test reddi (4.4'te gerçek örnek). Çare:
  15-20 kişi, kullanım takibi (Play istatistikleri + Tanılama özeti),
  pasif kişiyi 3-5 günde yenisiyle değiştir, form cevaplarında sayı ve
  sürüm numarası ver.
- Meta/WhatsApp tarafı (2018'de Can't Talk Play'den kalktı). Çare: yalnız
  okuyan, cevap göndermeyen, "silinen mesaj" vaadi vermeyen konum; mağazada
  WhatsApp adı düz metin ve bir kez.
- Kurulu uygulama listesi (Package Visibility) ve SDK taraması (Unity
  forumu, Eylül 2025). Çare: Play sürümünde reklam/analitik SDK yok; liste
  nedeni açıklama + politika + inceleyici notunda yazılı.
- Paket kaydı (30 Eylül 2026'dan beri zorunlu). Çare: kimliği hemen
  doğrulat; ilk yüklemeden sonra Android developer verification
  sayfasında durumu kontrol et.

## Denenen ve çalışmayan yollar

- Reddit (r/androiddev): WebFetch "bu alan adı getirilemez" dedi (old.reddit
  dahil), WebSearch alan adını engelledi, curl JSON uç noktası 403 verdi.
  3 denemede vazgeçtim. Yerine Hacker News Algolia API, dev.to, GitHub
  issue ve Türkçe forumlar kullandım.
- XDA, Medium (himanshu_hc), Technopat, Android Authority: WebFetch 403 ya da
  bot koruması. Bunların içeriği yalnız arama özetinden; "tek kaynak" diye
  işaretlendi.
- support.google.com sayfaları WebFetch'te kesik geldi (vergi, abonelik):
  curl + metin ayıklama ile tam metin okundu.
- Ülke tabloları (dağıtım konumları table/3541286, Android Developer
  Console ülke belge listesi 16501669/15633622): sayfa içeriği JavaScript
  ile yükleniyor, Türkiye satırı okunamadı. Yerine Console akışına
  bakmayı önerdim.
- Google'ın DSA "trader" yardım sayfası: ilk turda iki arama; ikinci turda
  üç arama daha (support.google.com ile sınırlı dahil) ve Play Console
  yardım sayfalarının tam metni (hesap bilgileri, ülke gereklilikleri,
  Geliştirici Program Politikası) tarandı: "trader" sözcüğü bu sayfalarda
  geçmiyor. Üçüncü taraf kaynaklarla yetindim. 5. denemede bıraktım.
- Bildirim dinleyiciye özgü "Play politikası yüzünden reddedildim" hikâyesi:
  Stack Overflow API (3 sorgu), HN (birkaç sorgu), GitHub issue araması ve
  ikinci turda 3 genel arama sonuç vermedi. Bulunan tek bildirim
  dinleyici + Play olayı 2018'deki WhatsApp/Can't Talk olayıdır (8.4).
- "Reddedildim, şöyle çözdüm" (üretim erişimi): ikinci turda dev.to'da
  bir tane (chakshusoft) bulundu; çıkanların çoğu hâlâ tester satan sitelerin
  pazarlaması. Medium'daki (levelup.gitconnected.com) "12 tester tax"
  yazısı WebFetch'te 403 verdi; 1 deneme + 1 arama, vazgeçtim.
- Reddit: ikinci turda WebSearch "reddit.com" alan adını engelledi (API
  400 hatası: alan adına erişilemiyor). Tekrar denemedim.
- Stack Overflow: WebSearch bu alan adını da engelledi.
- Play arama önerileri (market.android.com/suggest/SuggRequest): 404 verdi,
  eski uç nokta kapanmış. Bir daha denemedim. Yerine store/search sayfasını
  doğrudan indirdim; o çalıştı.
- Android Authority makalesi (gelişmiş akış): WebFetch 403.
- Türkiye için kişisel hesapta kabul edilen kimlik belgeleri: Türkçe sayfanın
  statik kısmında yalnız kuruluş belgeleri var; ülke satırı dinamik. 2 deneme
  (İngilizce + Türkçe sayfa), vazgeçtim.
- Play Protect gelişmiş koruma ülke listesi: resmi sayfa "seçili pazarlar"
  diyor; iki arama sonrası liste çıkmadı (Hindistan, Singapur, Tayland,
  Brezilya, Malezya geçiyor, Türkiye geçmiyor).
- Türkçe ek (morfoloji) için Google Play'e özel ASO kaynağı: iki arama,
  yalnız genel blog çıktı. Yerine kendim ölçtüm (10.2b).
- WhatsApp marka sayfasını doğrudan okudum, ama "mağaza açıklaması" özel
  kuralı sayfada yok.
- Play arama hacmi: Play kendi arama hacmini vermiyor. Anahtar kelime
  tercihleri yalnız sıralama gözlemi + ticari ASO yazılarına dayanıyor.
- Başlığı "Sende: ..." ile A/B test etmek: mağaza deneyleri başlığı
  denemiyor (yalnız grafik ve açıklama); bu yol zaten yok.

## Doğrulanamayanlar (toplu liste)

- Türkiye'nin Play Protect "gelişmiş dolandırıcılık koruması" pazarlarında
  olup olmadığı (bilinen: Hindistan, Singapur, Tayland, Brezilya, Malezya;
  Türkiye hiçbir kaynakta geçmiyor ama "değil" kanıtı da yok).
- Kuruluş hesabının kapalı test şartından resmen muaf olduğu: resmi sayfa
  yalnız "13 Kasım 2023'ten sonra açılan kişisel hesapları" sayıyor;
  kuruluşun muaf olduğunu birkaç ticari/blog kaynak söylüyor (Choicely,
  dev.to, Türkçe rehberler).
- Türkiye'de Google'ın kabul ettiği kimlik/kuruluş belgesi listesi.
- Google'ın Türkiye geliştiricilerinden stopaj kesip kesmediği; Türkiye
  için TRY asgari fiyat; "15% katmanı" kaydının otomatik olup olmadığı.
- ETBİS, VERBİS/KVKK yükümlülüğünün Play üzerinden uygulama içi satışa
  uygulanıp uygulanmadığı.
- GİB-Danıştay ihtilafının Ekim 2026 itibarıyla son durumu (özelge 30 Ocak
  2026 ve Danıştay 21 Ekim 2025 iddiaları yalnız ticari sitelerde; ben
  özelge/karar metnini görmedim).
- 20/B tebliğinin resmi (GİB) metni: sınır ve %15 tevkifat bilgileri
  ticari/mali müşavir sitelerinden; 5,3 mn TL için ek olarak Resmi
  Gazete'deki tarife haberi (Bloomberg HT) var.
- Şahıs şirketinin D-U-N-S alıp alamayacağı.
- Google Play'in DSA trader beyan adımları (resmi sayfa bulunamadı).
- Play manifestinden INTERNET'i çıkarmanın Billing'i bozup bozmadığı.
- com.sende.app paket adının başka bir (yayınlanmamış) geliştiricide
  olup olmadığı. Yalnız ilk yüklemede belli olur.
- Uygulama aktarımında hedef hesap yeni açılmış bir KURULUŞ hesabıysa
  (D-U-N-S doğrulaması sürerken) devir talebinin hemen işlenip
  işlenmediği: sayfa "kayıt tamam" şartı koyuyor, başka ayrıntı yok.
- API 37 için hedef API tarihi (31 Ağustos 2027 yalnız örüntüden tahmin).
- Başvuru formunda "ücretli test sağlayıcı kullandın mı" sorusu var mı
  (resmi sayfada yok; bir dev.to yazısı var diyor).
- Play Console'un test kullanıcısı "etkileşim" eşiği (8/12 gibi iddialar
  kaynaksız).
- Android 17'de NotificationListener davranış değişikliği (ayrı kol: 04).
- Play'in Türkçe çekim eklerini nasıl işlediğine dair resmi kaynak (yalnız
  kendi ölçümüm var).

## Sende için çıkarımlar

### Hesap ve süreç

- YAP: Hesap türü kararını mali müşavirle vergi konuşmasından sonra ver.
  İki makul yol var.
  - Yol A, hızlı: kişisel hesap. 12 tester × 14 gün + inceleme ≈ 4 hafta.
    Neden: şirket gerekmez. Risk: para kazanınca tam adres herkese açık;
    abonelik siparişleri sonradan hesap aktarımında eski hesapta kalır.
    Hafifletme: ilk haftalarda Play sürümünde destek menüsünü KAPALI
    tut (ürün yok, satıcı hesabı yok); mağazada yalnız ülken görünür.
    Şirket hazır olunca uygulamayı kuruluş hesabına aktar, sonra
    destek planlarını aç. Bedeli: kapalı testte abonelik A/B ölçümü
    (20+20 kişi) yapılamaz; ikisinden birini seçmek gerekir. [çıkarım]
    Aktarım notları (resmi, 22 Ekim 2026): test grupları taşınmaz (üretime
    çıktıktan sonra zaten gerekmez); kullanıcı, puan, yorum, App content
    cevapları ve paket kaydı uygulamayla gider; destek 2 iş günü içinde
    dönüyor; yeni hedef hesap 25 USD; yalnız resmi "uygulama aktarımı"
    yolu (gayriresmi hesap devri yasak, Nisan 2026 politikası).
  - Karar kuralı (benim önerim): kuruluş/şirket 6-8 hafta içinde hazır
    olacaksa DOĞRUDAN Yol B (aktarım işlemi, ek 25 USD ve kişisel adresin
    mağazada görünmesi riskinden kaçınmak için; D-U-N-S'yi beklerken
    uygulamayı iç testte geliştir). Hazır olmayacaksa Yol A.
    Çünkü Yol A'da test + inceleme zaten ~4 hafta, aktarım ek işlem
    ve ek 25 USD; Yol B'de ise kapalı test şartı muhtemelen yok ama
    resmi olarak doğrulanamadı. [çıkarım]
  - Yol B, temiz: şahıs şirketi/şirket + D-U-N-S + kuruluş hesabı. Neden:
    kurucu çok kişiye satmak istiyor; telefon/adres iş bilgisi olur; kapalı
    test şartı resmi sayfada yok [çıkarım]. Risk: D-U-N-S 30 güne kadar.
  - Her iki yolda D-U-N-S'yi ücretsiz kanaldan (CRIF/D&B) al, "hızlandırma"
    satanlara para verme.
- YAP: Uygulamayı "ücretsiz" oluştur. Neden: ücretliye çevrilemez.
- YAPMA: hesap satın alma/kiralama, sahte belge, tester satın alma. Neden:
  politika ihlali; bağlantılı hesaplar da kapanır; geri dönüş yok.
- YAP: Play Console'a sık gir; hesabı canlı tut (180 gün kuralı; telefon
  ve e-postayı doğrulanmış tut).
- YAP: Vergi sorusunu mali müşavire SAYILARLA götür: (1) 20/B istisnasında
  2026 sınırı 5.300.000 TL (4. gelir dilimi), aşılırsa kazancın tamamı
  beyan edilir; (2) şahıs şirketi hizmet ihracatı iddiası GİB özelgesiyle
  tartışmalı; (3) Google ödemesi aylık, ~ayın 15'inde TRY ile gelir.
  Hukuki/mali görüş benden değil. Neden: ticari kaynaklar çelişkili.
- YAP: Kimlik doğrulamasını (hesap + Android developer verification)
  hesabı açar açmaz bitir. Neden: 30 Eylül 2026'dan beri paket kaydı
  zorunlu, bildirimler kimlik doğrulaması tamamlanınca gelir.
- YAP: Paket adını (com.sende.app) ilk yüklemeden ÖNCE kesinleştir. Neden:
  sonra değişmez, silinemez.

### Kapalı test

- YAP: 15-20 gerçek esnaf (berber, kuaför, mağaza) bul; 14 gün
  boyunca uygulamayı kurulu tutmalarını iste. Neden: resmi öneri
  "hedef kitleyi temsil eden kişiler"; tampon çıkışlara karşı.
- YAP: Kapalı test kullanıcılarını iç test listesine KOYMA. Neden: iç
  testteki kişi kapalıya alınmaz. İç test yalnız sizin ekip (≤5 kişi) için.
- YAP: 14 gün içinde en az 2 güncelleme çıkar (geri bildirimden gelen düzeltme).
  Neden: başvuru formu "testten ne öğrendin, neyi değiştirdin" soruyor.
- YAP: Her test kullanıcısından en az 1 kez "tüm özellikleri kullan" iste
  (yanıt, görsel, filtre, ekranı temizle). Neden: form "tüm özellikleri
  kullandı mı" soruyor.
- YAP: Geri bildirimi bir dosyada topla (tarih, kim, ne dedi, ne
  yaptık). Neden: form "nasıl topladın, özetle" diyor.
- YAP: İki A/B kanalını da ≥15 kişiyle açık tut. Neden: hangisinin
  sayıldığı belirsiz.
- YAP: Bir test takip tablosu tut (kişi, Play hesabı e-postası, katıldığı
  gün, son görüldüğü gün, ne denedi, ne dedi). Her 3-4 günde bir pasif
  olanlara yaz; 5 günden fazla sessizse yenisini ekle. Neden: 4.4'teki
  örnekte (tek kaynak) iki ret "yetersiz etkileşim"di, üçüncüde (aktif
  takip) geçti.
- YAP: Davet metnine "Bağlantıya tıkla, 'Test kullanıcısı ol' de, Play'den
  kur, 14 gün çıkma, haftada en az 2 kez aç" yaz. Neden: yalnız e-posta
  listesine eklemek yetmez, kişi bağlantıdan kendisi katılmalı.
- YAPMA: 14. günden önce başvurma; testi duraklatma.

### Politika ve ret riski

- YAP: İzin öncesi zorunlu iki cümlelik açıklama + "Devam"/"Şimdi değil".
  Neden: Play "belirgin açıklama" şartı; XDA'da bu yüzden ret örneği var.
- YAP: Gizlilik politikası URL'si (PDF değil, düzenlenemez) ve uygulama
  içi bağlantı. Politikada kurulu uygulama listesini de yaz. Neden: resmi
  şart ve ihlal örneği.
- YAP: Veri güvenliği: "toplanmıyor, paylaşılmıyor". RELEASE.md §4'ü
  düzelt. Neden: resmi tanım "cihazdan dışarı iletmek".
- YAP: "Test bildirimi gönder" düğmesi + inceleyici notu + 60 sn video.
  Neden: inceleyici özel izni vermeden boş ekran görür.
- YAP: Ekran görüntülerini uydurma müşteri adlarıyla ve logo öne
  çıkmadan üret. Neden: marka + gizlilik + "bildirim göstermeyin".
- YAPMA: başlıkta WhatsApp/Instagram; "silinen mesajı gör"; "internet
  izni yok" (Play sürümünde yanlış). Neden: Meta ve Play kuralı,
  Spyware yakınlığı, S-14 kuralı.
- YAPMA: pil muafiyeti diyaloğu, erişilebilirlik servisi, READ_SMS,
  QUERY_ALL_PACKAGES. Neden: ek beyan, haftalarca inceleme, ret riski.
- DENE: Play manifestinden INTERNET'i çıkarmayı iç testte dene. Neden:
  "dinleyici + internet" kombinasyonu şüphe çeker. Billing bozulursa geri al.
- YAP: Kurulu uygulama listesini (MAIN/LAUNCHER sorgusu) üç yerde aynı
  cümleyle açıkla: izin öncesi ekran, gizlilik politikası, inceleyici notu.
  Neden: Package Visibility politikası bu envanteri "hassas veri" sayıyor.
- YAPMA: Play sürümüne reklam/analitik/çökme-raporlama SDK'sı koyma
  (Billing dışında ağ kodu yok). Neden: Unity forumundaki gerçek reddin
  sebebi SDK'ydı; "veri toplanmıyor" vaadi de bozulur.
- YAP: Meta tarafında dar dur: yalnız okuma, otomatik cevap yok, WhatsApp
  adı düz metin ve bir kez. Neden: 2018'de Notification Listener
  kullanan uygulamalar WhatsApp'tan hukuki mektup aldı, biri Play'den
  kalktı.

### Teknik

- YAP: `bundlePlayRelease` ile AAB üret; upload key'i ayrı ve yedekli
  tut (3 yerde); şifreleri depoya koyma.
- YAP: Sideload için ayrı applicationId (öneri) VE ayrı, gizli bir
  özel imza anahtarı. Neden: Play imzası farklı; test anahtarı herkese açık.
- YAP: `generateLocaleConfig` + `resources.properties` ekle. Neden:
  Android 13+ dil seçimi; dil otomatik isteği korunur.
- YAP: 16 KB doğrulamasını (zipalign -P 16) CI'ya koy; Billing 9
  geçişi (Ağustos 2027) için Kotlin 2.3 planı yaz.
- YAP: Üretimden önce `clean` ve birleşmiş manifestte eski "tekpanel"
  kalıntısının gittiğini doğrula.
- YAP: versionCode formülü (Bölüm 15); ilk Play yüklemesi 1.0.0.
- YAP: İlk üretimi Türkiye ile sınırla; AB'yi trader kararından sonra aç.

### Dağıtım ve geliştirici doğrulaması

- YAP: Play ana yol; sideload ikinci yol. Neden: Play Protect bildirim
  dinleyiciyi yüksek riskli sayıyor; kısıtlanmış ayar; 2027 doğrulama.
- YAP: 2027 başından önce sideload paketini de kaydet (Play Console
  üzerinden, gizli anahtarla). Neden: yoksa Türkiye'de müşteri 1 günlük
  gelişmiş akış zorunda kalır.
- YAPMA: "sınırlı dağıtım hesabı"nı müşteri dağıtımı için kullanma
  (20 cihaz sınırı).

### Mağaza

- YAP: Başlık "Sende: Müşteri Mesajları" (24), kısa açıklama 77
  karakter; TR öncelikli, EN/AR/FR sonra. Neden: Türkçe "müşteri mesajları"
  aramasında bizim gibi bir ürün yok (21-22 Ekim 2026 gözlemi); baş
  terimlerde (bildirim geçmişi) kalabalık. Not: "Shopxare: Müşteri
  Mesajları" aynı kalıbı kullanıyor (canlı destek gelen kutusu); kısa
  açıklamada "bildirimlerin" vurgusu şart. Yedek: "Sende: Bildirim ve
  Mesajlar" (27).
- YAP: Uzun açıklamada "bildirim geçmişi/kaydedici/yöneticisi" birer kez,
  marka notu en altta. Neden: Play Türkçe ek varyantlarını büyük ölçüde
  aynı sayıyor (10.2b); "bildirimler/bildirimi" diye yığma.
- YAP: Mağaza deneyi (listing experiment) ile ikon/ekran görüntüsü/açıklama
  A/B; ama yalnız YAYINDAKİ uygulamada ve başlık denenmez. Önce yayın.
- YAP: Sürüm notlarını dil başına ≤500 karakterde tut (TR/EN/AR/FR);
  "puan ver" gibi eylem çağrısı yazma.

## Kurucunun yapacağı adımlar (kontrol listesi)

- [ ] 1. Hesap türü kararı: kişisel (hızlı) mı kuruluş mu? (mali müşavire sor)
- [ ] 2. Mali müşavire sor: 20/B istisnası mı (2026 sınırı 5.300.000 TL;
      aşılırsa kazancın tamamı beyan) şahıs şirketi mi; stopaj; ETBİS;
      KVKK/VERBİS; Play ödemesinin (aylık ~15'i, TRY) banka hesabına akışı.
- [ ] 3. Yalnız bu iş için yeni bir Google hesabı aç (2 adımlı doğrulama açık).
- [ ] 4. Play Console'a kaydol: 25 USD (uluslararası/online işleme açık,
      prepaid olmayan kart).
- [ ] 5. Kimlik belgesi hazırla (geçerli, renkli, net, fotokopi değil; ad/adres
      ödeme profiliyle aynı).
- [ ] 6. Cihaz doğrulaması: Android 10+ gerçek telefonda Play Console mobil
      uygulaması. Ardından iletişim e-postası + telefonu tek kullanımlık
      kodla doğrula (Android developer verification bildirimleri için de
      gerekli).
- [ ] 7. (Kuruluş yolu) D-U-N-S başvurusunu ücretsiz kanaldan yap; web sitesi
      doğrulaması.
- [ ] 8. Ödeme profili / satıcı hesabı, banka doğrulaması (5 güne kadar),
      vergi bilgisi.
- [ ] 9. "Uygulama oluştur": ÜCRETSİZ seç; ad "Sende"; varsayılan dil;
      iletişim e-postası.
- [ ] 10. Yükleme anahtarını (upload key) üret; 3 ayrı güvenli yere yedekle;
       şifreleri güvenli saklayıcıda tut.
- [ ] 11. İç test: AAB'yi yükle; kendi telefonunda dene (izin akışı, test
       bildirimi).
- [ ] 12. App content formlarını bizim hazırladığımız cevap kâğıdına göre doldur
       (gizlilik URL'si, erişim notu, reklam, derecelendirme, hedef kitle,
       Veri güvenliği).
- [ ] 13. Gizlilik politikasını herkese açık bir adreste yayınla (PDF değil).
- [ ] 14. Kapalı test kanalı/kanalları aç; 15-20 gerçek esnafı davet et;
       "14 gün boyunca çıkma" talimatını ver. Davet bağlantısı yalnız
       kanal "Yayınlandı" olunca çıkar; bağlantı birkaç saat gecikebilir.
       Her kişinin bağlantıdan KATILDIĞINI tek tek kontrol et.
- [ ] 15. Ülke listesine test kullanıcılarının Play ülkesini ekle (Türkiye).
- [ ] 16. 14 gün boyunca en az 2 güncelleme yükle; geri bildirimi bir dosyada topla.
- [ ] 17. Lisans test hesaplarını ekle; 4 abonelik planı (monthly, quarterly,
       semiannual, yearly) + 3 tüketilebilir ürünü oluştur (ODEME.md); TRY
       fiyatlarını elle gir.
- [ ] 18. 14. gün dolunca Dashboard > "Üretim için başvur" (3 bölümlü form).
- [ ] 19. Mağaza girdisini ve görselleri yükle (bizim dosyalar).
- [ ] 20. Android developer verification: Play Console'da doğrulama durumunu
       ve paket kayıtlarını kontrol et; sideload paketini kaydet.
- [ ] 21. Üretime Türkiye ile çık; ilk 7 gün her gün Android vitals ve
       yorumlara bak.
- [ ] 22. Gerçek telefonda GitHub APK'sını kurup Play Protect davranışını dene
       ve sonucu bana yaz.
- [ ] 23. (Opsiyonel) DSA trader beyanı kararı: AB'ye açmadan önce.
- [ ] 24. Üretim erişimi formunu sayı ve sürüm numarasıyla doldur (kaç kişi
       14 günü tamamladı, hangi sürümde hangi geri bildirimi çözdük).
       Reddedilirse yeniden başvurmadan ÖNCE bana ret metnini ilet.
- [ ] 25. Ret e-postası gelirse: cümleyi aynen kaydet, Policy status sayfasına
       bak, düzelt, yeniden gönder. Google'ın yeniden gönderim yönergesi
       "destek ekibinden yanıt beklemeden" göndermeye izin veriyor (bir
       dev.to yazısının alıntısı; yönergeyi kendim açmadım). Tüm yayın
       kanallarındaki uyumsuz paketleri de devre dışı bırak, yoksa
       yeniden gönderim başarısız olur.
- [ ] 26. Her sürümde Sürüm notları ≤500 karakter/dil; "puan ver" yazma.

## Bizim hazırlayıp vereceğimiz dosyalar (kontrol listesi)

- [ ] 1. Gizlilik politikası sayfası (TR + EN, statik HTML), kurulu uygulama
      listesi ve Play/sideload farkı dahil.
- [ ] 2. Uygulama içi: izin öncesi zorunlu açıklama ekranı (TR/EN/AR/FR),
      gizlilik politikası bağlantısı, "Aboneliği yönet" bağlantısı.
- [ ] 3. Uygulama içi: "Test bildirimi gönder" düğmesi ve boş durum metni.
- [ ] 4. Mağaza metinleri: TR, EN, AR, FR için başlık/kısa/uzun + karakter
      sayacı betiği (30/80/4000).
- [ ] 5. Görseller: 512x512 PNG ikon, 1024x500 öne çıkan görsel, 4-8
      telefon ekran görüntüsü (1080x1920), uydurma veriyle, alt metinlerle.
- [ ] 6. App content cevap kâğıdı: gizlilik, erişim, reklam, AD_ID,
      derecelendirme, hedef kitle, Veri güvenliği, sağlık/finans (hepsi yok).
- [ ] 7. İnceleyici notu (EN) + 60 sn demo videosu (emülatörde kaydedilmiş).
- [ ] 8. Kapalı test paketi: davet metni (TR), WhatsApp mesaj şablonu,
      14 günlük takvim, geri bildirim kayıt şablonu, test kullanıcısı takip dosyası.
- [ ] 9. Üretim erişimi başvuru taslağı (3 bölüm, TR/EN).
- [ ] 10. Gradle: versionCode formülü, `bundlePlayRelease` imza yapılandırması,
      sideload için ayrı applicationId + ayrı imza (karar sonrası),
      `generateLocaleConfig` + `resources.properties`.
- [ ] 11. CI: AAB üretimi, 16 KB hizalama denetimi, birleşmiş manifestte
      yasaklı izin denetimi (Play sürümünde AD_ID/READ_SMS/QUERY_ALL_PACKAGES yok).
- [ ] 12. RELEASE.md §3-§6 güncellemesi: Veri güvenliği cevabı, `bundlePlayRelease`,
      "internet izni yok" cümlesinin Play'de düzeltilmesi.
- [ ] 13. Billing 9 geçiş notu (Kotlin 2.3) ve takvimi.
- [ ] 14. Developer verification kontrol listesi (paket adı, anahtar, kayıt
      adımları).
- [ ] 15. `play-policy-insights` taraması çıktısı (araştırma 07).
- [ ] 16. Sürüm notları (TR/EN/AR/FR, her biri ≤500 karakter, sayaç betiği)
      ve ilk 7 gün izleme listesi (vitals, yorum).
- [ ] 17. Test kullanıcısı takip tablosu şablonu (kişi, Play e-postası, katılım
      günü, son görülme, ne denedi, geri bildirim, ek tarih) + haftalık
      yoklama mesajı şablonu.
- [ ] 18. Tanılama ekranına, mesaj İÇERİĞİ olmayan, yalnız sayaç içeren
      "kullanım özeti" paylaşımı (test kullanıcısı bize gönderir). SDK yok.
- [ ] 19. İnceleyici notuna "kurulu uygulama listesi neden okunur, dışarı
      gitmez" paragrafı; politika ve izin öncesi ekranla birebir aynı cümle.
- [ ] 20. Mağaza metninde Meta/WhatsApp ifadesi denetimi (başlıkta yok; düz
      metin, bir kez; "silinen mesaj" vaadi yok; marka notu).
- [ ] 21. Ret karşılama kitabı: ret e-postasındaki politika adı → hangi form
      bölümü → düzeltme → tekrar gönderim; Policy status ve App content
      denetim listesi.
- [ ] 22. İlk yüklemeden önce paket adı denetimi: com.sende.app kesinleşti mi,
      sideload için ayrı applicationId kararı verildi mi.
