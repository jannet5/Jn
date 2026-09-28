# TekPanel — Kod Kararları Günlüğü

Bu dosya, şartnamenin (bkz. görev tanımı) kullanıcı kararı gerektirdiğini işaretlediği
noktalarda hangi kararın alındığını ve nedenini kaydeder. Amaç: sonraki bir ajan veya
geliştirici "bu neden böyle?" sorusuna kod içinde değil burada cevap bulsun.

## 1. Uygulama adı, application ID, launcher adı

**Soru kullanıcıya soruldu**, cevap: "Sende" (karar ajana bırakıldı).

**Karar:**
- Launcher/mağaza adı: **TekPanel**
- Application ID: **com.tekpanel.app**
- Geliştirme sırasında konuşulan "kaka" adı hiçbir yerde kullanılmadı; nihai marka her yerde TekPanel.

**Neden:** Şartname boyunca en ağırlıklı kullanılan isim TekPanel; "Sende" yanıtı açık bir
tercih belirtmediği için şartnamenin kendi ağırlıklı ismine sadık kalındı. Bu, mağazaya
çıkmadan önce marka/hukuk incelemesiyle değiştirilebilir; `applicationId` değişikliği o an
yeni bir Play Console kaydı gerektireceği için mümkünse ilk yayından ÖNCE netleşmelidir.

## 2. Varsayılan dil ve yerelleştirme

**Soru kullanıcıya soruldu**, cevap: "Globale satacağız öyle düşün" (global satış hedefi).

**Karar:** Varsayılan (`values/`) dil **İngilizce**. Türkçe (`values-tr`), Fransızca
(`values-fr`) ve Arapça (`values-ar`, RTL) tam çeviriyle eklendi — şartnamenin TR/FR/BAE
mağaza yerelleştirme talebiyle uyumlu. Türkçe, birincil kullanıcı kitlesi olan Türkiye
işletmecisi göz önünde bulundurularak eksiksiz çevrildi (şartnamenin örnek metinleri
Türkçe'ydi, bu metinler `values-tr/strings.xml`'e taşındı).

**Not:** Daha fazla pazar eklemek yalnızca yeni bir `values-xx/strings.xml` dosyası demektir;
kod hiçbir yerde dile bağlı `if` içermez (bkz. `TimeFormatter`, sistem `DateUtils` kullanır).

## 3. Kapsam: bu oturumda ne kadarı bitirildi

**Soru kullanıcıya soruldu**, cevap özetle: "tam harita çıkar, baştan sona uygula, Google
Play'e kadar götür, bahane üretme, araştırıp çöz."

**Bu oturumda tamamlanan (gerçek, derlenen, test edilen kod):**
- CAP-01..17'nin tümü için çalışan Kotlin/Compose kaynak kodu (bkz. RELEASE.md'deki CAP
  eşleme tablosu).
- `./` kökünde gerçek bir Gradle/AGP projesi; `assembleDebug` ve `assembleRelease` başarıyla
  APK üretiyor (SHA-256 ve tarih için RELEASE.md §1'e bakın).
- **34 test, hepsi geçiyor**: 26 saf JVM birim testi + 8 Robolectric testi. Robolectric
  testleri gerçek `android.app.Notification`/`StatusBarNotification` nesneleri (gerçek
  `NotificationCompat.MessagingStyle`/`BigTextStyle` ile inşa edilmiş) ve gerçek bellek-içi
  bir Room veritabanına karşı çalışıyor; CAP-07'nin "listener yeniden bağlanınca aynı
  bildirim tekrar işlense bile mükerrer kayıt oluşmaz" ve CAP-03'ün "kanalı kapatınca yeni
  kayıt girmez" davranışları gerçek nesnelerle uçtan uca doğrulandı.

**Bu ortamda (headless, Android SDK'lı ama KVM/donanım sanallaştırması olmayan bir
konteyner) YAPILAMAYAN ve dürüstçe işaretlenmesi gereken tek şey — kontrol edildi, varsayım
değil:**
- **Gerçek cihaz/emülatörde bildirim yakalama testi.** `/dev/kvm` yok, CPU'da `vmx`/`svm`
  bayrağı görünmüyor (kontrol edildi) — bu konteynerde hızlandırılmış bir Android emülatörü
  çalıştırılamaz; hızlandırma olmadan yazılımsal emülasyon bu ortamda pratik/güvenilir
  değildir. Bunun yerine, gidilebilecek en yakın nokta yapıldı: Robolectric ile gerçek
  Android çerçeve sınıfları JVM üzerinde simüle edilerek uçtan uca pipeline test edildi
  (yukarıya bakın) — bu, sentetik `RawNotificationPayload` fixture'larından çok daha güçlü
  bir kanıt, ama gerçek WhatsApp/Instagram uygulamasının o an postaladığı gerçek bildirimin
  yerini tutmaz. Kullanıcı, gönderilen APK'yı bir Android telefonda `adb install -r` ile
  kurup gerçek mesaj göndererek son doğrulamayı tamamlayabilir; bu oturumda bu adım açık bir
  görev olarak bırakıldı (bkz. ROADMAP.md).
- **Google Play'de gerçek yayın.** Ayrıntı için RELEASE.md → "Yalnızca kullanıcının
  yapabileceği adımlar" bölümüne bakın: Play Console hesabı açmak, 25$ kayıt ücretini ödemek,
  Geliştirici Dağıtım Sözleşmesi'ni kabul etmek ve kimlik doğrulamasını tamamlamak; bunların
  hepsi Google'ın kimlik/ödeme sahibi bir insan hesabı gerektirdiği işlemler ve bu ajan
  bunları kullanıcı adına yapamaz (banka/kimlik bilgisi yok, gerçek bir Google hesabına
  giriş yapılamıyor). Bunun dışındaki HER ŞEY (mağaza metinleri, gizlilik politikası taslağı,
  data safety eşlemesi, imzalama talimatları, AAB üretimi) bu oturumda hazırlandı.

## 4. CAP-03 ile CAP-11 arasındaki çelişkinin çözümü

Şartname iki yerde birbiriyle gerilim içinde:
- CAP-03: "Kanal kapatıldığında geçmiş kayıtlar otomatik silinmez... geçmişi
  gizleme/silme ayrı kullanıcı eylemidir."
- CAP-11: "Tümü tüm açık kanalların kayıtlarını gösterir."

**Karar:** "Tümü" filtresi, kanalın o an açık/kapalı olmasından bağımsız olarak **tüm aktif
mesajları** gösterir (CAP-03'e sadık kalındı — kapatma hiçbir zaman otomatik gizleme
yapmaz). Kanal ayarları sayfasındaki filtre pilleri ise yalnızca şu an açık+kurulu kanalları
listeler (CAP-02/03). Yani bir kanalı kapatırsan onun pili kaybolur ama o kanaldan önceden
gelen mesajlar "Tümü" görünümünde durmaya devam eder; silmek istersen "Ekranı tertemiz yap"
veya "Tüm yerel veriyi sil" ayrı, açık eylemlerdir. Kod: `InboxViewModel.toScreenState`.

## 5. Kanal logoları ve uygulama ikonu — yer tutucu, hukuki not

Şartname "her kanalın gerçek/orijinal marka logosu kullanılmalı" diyor. Bu oturumda:
- Her kanal için **marka rengiyle boyanmış düz bir daire** kullanıldı (bkz.
  `app/src/main/res/drawable/logo_*.xml`), gerçek marka logosu KOPYALANMADI.
- **Neden:** Gerçek marka logolarının (WhatsApp, Instagram, TikTok, X, Meta ürünleri, vb.)
  izinsiz kullanımı marka kullanım kurallarını ihlal edebilir ve Play Store incelemesinde
  reddedilme/kaldırılma riski taşır. Bu, atlanabilir bir "kolay" adım değil, gerçek bir
  hukuki/politika sınırı.
- **Yapılması gereken (mağaza yayınından önce):** Her marka için resmi marka kullanım
  kılavuzunu okuyup (çoğu marka "third-party app" kullanımına izin vermez veya sıkı kurallar
  koyar), izin veren markalar için resmi paketten SVG/PNG alıp `drawable/logo_<id>.xml`
  dosyasının yerine koymak; izin vermeyenler için renk+isim kombinasyonuyla devam etmek.
  Kod tarafında değişiklik gerekmez — sadece drawable dosyaları değişir.
- Uygulama ikonu (`ic_launcher_foreground.xml`) tamamen özgün, basit bir "gelen kutusu"
  motifidir; marka çakışması yoktur ama bir tasarımcı geçişi önerilir.

## 6. Bildirim erişimi / kanal seçimi UI'sinin tam ekran değil "geçici yüzey" olması

Şartname madde 3.3 açıkça izin/kanal/tanılama akışlarının ayrı büyük sayfa olmamasını
istiyor. Karar: bildirim erişimi isteği, ana ekranın kendisinin bir "boş durumu" (empty
state) olarak gösterildi (ayrı Activity/route yok); kanal ayarları bir `ModalBottomSheet`;
tanılama ayrı ama gizli bir Activity (ana ekrandan link yok, yalnızca başlığa 3 saniye içinde
7 kez dokunma jestiyle açılır — Android'in "geliştirici seçenekleri" jestine benzer).

## 7. Paylaşım (CAP-15) kaynak tespiti

`Activity.getReferrer()` kullanıldı (Android'in ACTION_SEND çağrısını yapan uygulamayı
öğrenmek için resmi desteklenen yöntemi), çağıran uygulamanın kendi beyan ettiği extra'lara
güvenilmedi (sahtecilik riski).

## 8. Gerçek WhatsApp/Instagram bildirim şekline karşı doğrulama — ne yapıldı, ne yapılamadı

Kullanıcı bu oturumda ısrarla "gerçek uygulamalardan gerçek bildirim gelmeden bunu nasıl
doğruladın" sorusuna somut bir cevap istedi. Gerçek bir telefonda gerçek WhatsApp/Instagram
hesabı olmadan bunu %100 kapatmak mümkün değil (bkz. ROADMAP.md'deki açık adım), ama bahane
üretmek yerine şu araştırma yapıldı ve koda işlendi:

**Araştırma:** WhatsApp/Instagram/Telegram bildirimlerini `NotificationListenerService` ile
okuyan birden fazla açık kaynak proje incelendi (web araması ile):
- [jimale/WhatsDeleted PR #8](https://github.com/jimale/WhatsDeleted/pull/8) — "bundled
  (kilitliyken gelen) WhatsApp bildirimlerinde `android.text` sadece son satırı taşır; tüm
  mesajlar için `MessagingStyle.messages` veya ham `EXTRA_MESSAGES` bundle dizisi okunmalı"
  ve "bazı OEM bildirimleri bozuk bir mesaj bundle dizisi bildiriyor" notu.
- [Pitch-code/NextMove PR #7](https://github.com/Pitch-code/NextMove/pull/7) — WhatsApp için
  önce `NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification`, yoksa
  `android.text`'e düşme sırası; sohbet/gönderen adı için `android.title`.
- Genel arama: MessagingStyle içeren bir bildirim, gerçek bir doğrudan mesajın (DM) güçlü bir
  işareti olarak kabul ediliyor; bazı MessagingStyle bildirimlerinde `EXTRA_TEXT` tamamen boş
  bırakılabiliyor.

**Bunun koda etkisi — doğrulanan, değiştirilmeyen kararlar:**
- `MessageNormalizer`'ın MessagingStyle'ı bigText/text'in ÖNÜNE koyması (CAP-05), bağımsız
  üç kaynakla da örtüşüyor — değiştirilmedi, doğrulandı.
- `NotificationExtractor`'ın `EXTRA_TITLE`'ı `EXTRA_TEXT`'ten bağımsız okuması zaten doğru
  davranıyordu (Instagram gibi `EXTRA_TEXT`'i boş bırakan bildirimlerde sorun yok).

**Bunun koda etkisi — gerçek bir sertleştirme yapıldı, ama iddiası dürüstçe sınırlandı:**
`NotificationExtractor.extract()`, `extractMessagingStyleFromNotification` çağrısını artık
`try/catch` ile sarıyor (bkz. `NotificationExtractorRobolectricTest.falls back to bigText
when MessagingStyle parsing yields nothing usable`). Bunu eklerken önce gerçekten kırmaya
çalıştım: Robolectric altında bozuk bir `EXTRA_MESSAGES` dizisi (Bundle olmayan Parcelable
öğeleri) verip önce DÜZELTME OLMADAN test çalıştırıldı — **çökmedi**. Nedenini projenin
gerçek `androidx.core:core:1.13.1` bağımlılığının .jar'ını `javap` ile decompile ederek
kontrol ettim: `getMessagesFromBundleArray` her öğeyi `instanceof Bundle` ile kontrol ediyor,
ve `getMessageFromBundle`'ın TÜM gövdesi bytecode seviyesinde bir `ClassCastException`
exception-table girişiyle sarılı (satırlar 0–188, hedef 189, `null` döndürüyor). Yani bu
bağımlılık sürümü, GitHub'daki geliştiricilerin bahsettiği tam senaryoya karşı zaten
sertleştirilmiş.

Bu yüzden `try/catch`'i "kanıtlanmış bir çökmeyi düzeltiyorum" diye değil, "gerçek üretimde
bu çağrı bir Binder/IPC sınırını geçiyor (bu sandbox'ın tekrar edemediği tek şey) ve gelecekte
farklı bir androidx sürümü veya farklı bir OEM hatası bu korumanın kapsamadığı bir yerde
çökebilir, bunun maliyeti sıfır" diye ekledim. Test de buna göre adlandırıldı — bir çökmeyi
"reproduce ettim" demiyor, geri dönüş davranışının doğru çalıştığını doğruluyor.
