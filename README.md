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

<!-- Doldurulacak: son üretilen APK'nın konumu ve SHA-256 değeri. -->

## Bilinen sınırlar / bu ortamda doğrulanamayanlar

<!-- Doldurulacak: bu geliştirme ortamının (Linux konteyner, KVM/emülatör yok, gerçek Windows
     makinesi yok) neyi doğrulayabildiği ve neyi doğrulayamadığı açıkça listelenecek. -->
