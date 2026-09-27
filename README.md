# CepGözcü

Android telefondan Windows bilgisayarını izlemek ve güvenli biçimde yönetmek için yazılmış iki
parçalı bir sistem: bilgisayarda çalışan bir **ajan** (Windows bileşeni, .NET 8) ve telefonda
çalışan bir **Android uygulaması** (Kotlin, Jetpack Compose). İkisi arasındaki tek bağlantı yolu,
kendinden imzalı ve eşleştirme anında sabitlenen bir TLS sertifikası üzerinden yerel ağda kurulan
bir WebSocket bağlantısıdır — internet üzerinden erişilebilen bir röle sunucusu yoktur.

```
windows-agent/   .NET 8 ajanı: gerçek CPU/RAM/disk metrikleri, süreç yönetimi, izinli uygulama
                 başlatıcı, disk değişim/büyüme izleme motoru, eşleştirme + oturum güvenliği.
                 Bkz. windows-agent/README.md.
android-app/     Kotlin/Compose uygulaması: eşleştirme, canlı panel, süreçler, disk geçmişi ve
                 büyüme analitiği, uyarılar, denetim günlüğü. Bkz. android-app/README.md.
```

## Neden bu mimari?

Birkaç bağlantı/protokol yaklaşımı değerlendirildi:

- **gRPC** — güçlü tip güvenliği sunar ama Android tarafında sertifika sabitleme + özel
  el sıkışma akışları (PIN/onay) ile bir arada kullanmak, ham WebSocket'e göre belirgin bir
  karmaşıklık artışı getiriyordu; kazanımı bu ölçekte haklı çıkmadı.
- **REST + ayrı bir push mekanizması (FCM vb.)** — FCM, Google sunucularına bağımlılık ve
  internet erişimi gerektirir; "yerel ağı önceliklendir" gereksinimiyle çelişir.
- **Ham TCP + özel bir protokol** — TLS/sertifika/çerçeveleme işini sıfırdan yazmak gereksiz risk
  taşır.

Seçilen yaklaşım: **HTTPS + WebSocket (ASP.NET Core Kestrel / OkHttp)**, kendinden imzalı sertifika
+ eşleştirme anında parmak izi sabitleme, JSON mesaj zarfı. Hem .NET hem Android tarafında birinci
sınıf, iyi test edilmiş kütüphane desteği var; sertifika sabitleme klasik SSH host-key modeliyle
aynı, anlaşılır bir güven modeli sağlıyor; ve tamamen yerel ağda çalışıyor.

Veritabanı olarak SQLite (ajan tarafında) ve Room/SQLite (telefon tarafında, çevrimdışı önbellek
için) seçildi — gömülü, dosya tabanlı, ekstra bir servis kurulumu gerektirmiyor, bu ölçekteki bir
disk geçmişi/olay akışı için fazlasıyla yeterli.

## Güvenlik modeli (özet)

- Sadece yerel ağ: uygulama katmanında IP aralığı kontrolü (loopback/özel aralık dışını reddeder)
  + güvenlik duvarı kuralı (yalnızca Özel/Etki Alanı profilleri).
- Eşleştirme iki faktörlü: PC ekranında gösterilen tek kullanımlık PIN + bilgisayarın başındaki
  kişinin yönetim panelinden verdiği açık onay. Hiçbiri tek başına yeterli değildir.
- Sertifika sabitleme (TOFU): telefon, eşleştirme anında gösterilen SHA-256 parmak izini saklar;
  sistem CA deposuna hiç güvenilmez.
- Genel amaçlı komut çalıştırma yok: protokolde yalnızca sabit bir mesaj kümesi var (metrik oku,
  süreç listele/sonlandır, izinli uygulama başlat, disk geçmişini sorgula). Kritik sistem
  süreçlerinin sonlandırılması sunucu tarafında kayıtsız şartsız engellenir. Uygulama başlatma
  yalnızca bilgisayarda (yönetim panelinden, yalnızca o bilgisayardan erişilebilir) elle eklenmiş
  uygulamalarla sınırlıdır.
- Her kontrol işlemi (süreç sonlandırma, uygulama başlatma, cihaz eşleşmesini kaldırma) bir denetim
  günlüğüne yazılır ve hem yönetim panelinde hem telefon uygulamasında görülebilir.
- **v1 sınırı, açıkça belirtilmiştir:** eşleştirmede gönderilen "cihaz genel anahtarı" alanı şu an
  yalnızca bilgi amaçlıdır; imzalı bir challenge/response ile cihaz kimliği doğrulanmıyor. Gerçek
  yetkilendirme sabitlenmiş TLS + PIN + yerel onay + opak oturum anahtarına dayanıyor. Bu, gelecekte
  eklenebilecek makul bir sertleştirme adımı olarak README'lerde ayrıca not edilmiştir.

Ayrıntılar için `windows-agent/README.md` ve `android-app/README.md`.

## Hızlı başlangıç

1. **Windows bilgisayarda:** `windows-agent/README.md`'deki adımlarla ajanı çalıştırın veya
   `scripts/publish.ps1` + `scripts/install.ps1` ile kalıcı servis olarak kurun.
2. **Telefonda:** `android-app/README.md`'deki adımlarla APK'yı kurun (veya aşağıdaki hazır APK'yı
   kullanın).
3. Bilgisayardaki yönetim panelinden ("Eşleştirme kodu oluştur") QR kodu telefonla okutun, ekranda
   gösterilen PIN'i girin, bilgisayarda "Onayla"ya basın.

## Test durumu

- **Windows ajanı:** `cd windows-agent && dotnet test` → 51 test, hepsi geçiyor (birim testleri +
  gerçek dosya sistemi/süreç entegrasyon testleri + gerçek ASP.NET Core hattından uçtan uca
  eşleştirme akışı testi).
- **Android uygulaması:** `cd android-app && ./gradlew testDebugUnitTest` — bkz. `android-app/README.md`
  için güncel sonuç ve kapsam notları.

## APK

Bu depoda üretilen son sürüm: `android-app/app/build/outputs/apk/release/app-release.apk`
(APK dosyasının kendisi, imzalama anahtarıyla birlikte, git'e değil size doğrudan teslim edilir —
bkz. sohbetteki dosya ekleri).

```
SHA-256: 84487a183365c4651440b559e74416d67e189a8114cc38319ce45a5bfe889397
```

Doğrulama:

```bash
sha256sum app-release.apk
apksigner verify --verbose --print-certs app-release.apk   # Android SDK build-tools içinde gelir
```

`apksigner verify` bu depoda gerçekten çalıştırılıp doğrulandı: v2 imza şeması geçerli, tek imzalayan,
paket adı `com.cepgozcu.app`, minSdk 26 / targetSdk 34, dört ana CPU mimarisi (arm64-v8a,
armeabi-v7a, x86, x86_64) pakete dahil.

## Bilinen sınırlar / bu ortamda doğrulanamayanlar

Bu proje, gerçek bir Windows bilgisayarı ve gerçek bir Android cihazı/emülatörü *olmayan* bir Linux
konteynerde geliştirildi. Aşağıdaki ayrım, neyin gerçekten çalıştırılıp doğrulandığını ve neyin
yalnızca kod incelemesi + kısmi doğrulamayla bırakıldığını nettir:

**Bu ortamda gerçekten çalıştırılıp doğrulananlar:**
- Windows ajanının çekirdek mantığı: gerçek CPU/RAM/disk metrikleri (bu Linux konteynerinin kendi
  değerleriyle, `/proc` üzerinden — Windows'ta aynı sınıf `GetSystemTimes`/`GlobalMemoryStatusEx`
  P/Invoke çağırılarını kullanır), gerçek süreç listeleme/sonlandırma (gerçek alt süreçler
  başlatılıp gerçekten sonlandırıldı), kritik süreç koruması (gerçek bir "korunan" PID'i
  sonlandırma denemesi gerçekten reddedildi), gerçek bir klasörde dosya
  oluşturma/büyütme/yeniden adlandırma/taşıma/silme olaylarının hem canlı `FileSystemWatcher` hem
  periyodik tarayıcı tarafından doğru şekilde yakalanması, gürültü filtresi, klasör büyüme
  analitiği (gerçek iç içe klasör boyutları üzerinden), uyarı motoru (gerçek düşük disk alanı
  koşuluyla tetiklendi).
- Tüm eşleştirme akışı: PIN doğrulama → yerel onay → oturum anahtarı üretimi → WebSocket üzerinden
  kimlik doğrulama — gerçek bir çalışan sunucuya karşı gerçek bir WebSocket istemcisiyle (Node.js
  `ws` kütüphanesiyle) uçtan uca test edildi (ayrıca xUnit'te ASP.NET Core hattından otomatik
  olarak da test ediliyor).
- Kimliği doğrulanmamış bir istemcinin hiçbir işlem yapamadığı (401, bağlantı reddi).
- Yayımlanan APK'nın gerçekten kurulabilir olduğu (imza doğrulaması, paket bilgileri, mimari
  kapsamı) ve Android JVM birim testlerinin (protokol serileştirme, eşleştirme durum makinesi,
  ViewModel durum geçişleri) gerçekten geçtiği.
- .NET tarafının `dotnet publish -r win-x64` ile gerçek bir win-x64 PE32+ çalıştırılabilir dosyası
  ürettiği (çapraz derleme; bu makine Windows değil, ama üretilen dosya gerçek bir Windows
  yürütülebilir dosyasıdır).

**Bu ortamda doğrulanamayan (dürüstçe belirtilmesi gereken) şeyler:**
- Ajanın gerçek bir Windows bilgisayarında Windows servisi olarak çalıştırılması (P/Invoke
  çağrılarının gerçek Windows API'lerine karşı davranışı, güvenlik duvarı kuralının gerçekten
  engellediği, Windows Hizmetleri altında kurulum/kaldırma betiklerinin uçtan uca çalışması).
  Kod incelemesiyle ve platform API'lerinin belgelenmiş davranışına güvenerek yazıldı, ama bu
  konteynerde *çalıştırılamadı* — bunu olmuş gibi göstermiyoruz.
- Android tarafında gerçek cihaz/emülatör akışı (QR kod okutma, gerçek kamera, gerçek bir ajana
  gerçek Wi-Fi üzerinden bağlanma, ekranlar arası gerçek dokunmatik gezinme). Bu konteynerde
  KVM/donanım sanallaştırma desteği yok, bu yüzden bir Android emülatörü çalıştırılamadı ve
  fiziksel bir cihaz da yok. Bunun yerine derleme/imza/test doğrulamasıyla ve dikkatli kod
  incelemesiyle güvence sağlandı (bkz. yukarıdaki madde ve `android-app/README.md`).
- Uygulama başlatma/süreç sonlandırma gibi kontrol işlemlerinin *gerçek Windows uygulamalarıyla*
  (örn. Not Defteri) uçtan uca telefon → ajan → Windows akışı — mantık gerçek süreçlerle test
  edildi (bu Linux ortamında `sleep`, `/bin/echo` gibi gerçek alt süreçler kullanılarak), ama
  Windows'a özgü hedefler değil.

Kısacası: **mimari, protokol, güvenlik mantığı ve her iki tarafın iş mantığı gerçek ve test
edilmiştir; yalnızca "gerçek Windows makinesi + gerçek Android cihazı" kombinasyonuyla ikisini
aynı anda çalıştırma adımı bu ortamın fiziksel sınırları nedeniyle yapılamamıştır.** Bir Windows
bilgisayarında ajanı `dotnet run` ile veya kurulum betikleriyle çalıştırıp APK'yı gerçek bir
telefona kurarak bu son adımı doğrulamak, bu depodaki adımları izleyen herkes için birkaç dakika
sürer.
