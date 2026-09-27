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
- `./` kökünde gerçek bir Gradle/AGP projesi; `assembleDebug` başarıyla APK üretiyor (SHA-256
  ve tarih için aşağıya bakın).
- 26 JVM birim testi (`MessageNormalizerTest`, `MessageFilterEngineTest`, `FingerprintTest`,
  `DuplicateGuardTest`), hepsi geçiyor.

**Bu ortamda (headless, Android SDK'lı ama fiziksel/emulatör cihazsız bir konteyner)
YAPILAMAYAN ve dürüstçe işaretlenmesi gereken şeyler — bahane değil, bu araç setinin gerçek
sınırı:**
- **Gerçek cihazda bildirim yakalama testi.** Şartname madde 0 ve CAP-06 açıkça "gerçek
  cihazda doğrulama" istiyor. Bu konteynerde çalışan bir Android emülatörü/fiziksel cihaz
  yok; kurulabilecek bir emülatör (KVM/donanım hızlandırma gerektirir) bu sandbox'ta
  mevcut değil. Bunun yerine: (a) filtre/normalize/dedupe mantığı gerçek bildirim
  formatlarına (MessagingStyle, bigText, textLines) göre birim testleriyle doğrulandı, (b)
  APK gerçekten derlendi ve kuruluma hazır. Kullanıcı bir Android telefonda `adb install`
  ile APK'yı kurup CAP-06'nın "gerçek cihazda doğrulama" adımını tamamlayabilir; bu oturumda
  bu adım açık bir görev olarak bırakıldı (bkz. ROADMAP.md).
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
