# CepGözcü Android Uygulaması

Kotlin + Jetpack Compose (Material 3) ile yazılmış, `windows-agent/`'daki .NET ajanına yerel ağ
üzerinden bağlanan istemci. Minimum SDK 26 (Android 8.0+), hedef/derleme SDK 34.

## Mimari

```
net/protocol/     Ajanın wire protokolünün Kotlin karşılığı (Envelope, DTO'lar) — C# tarafındaki
                  CepGozcu.Agent.Core.Protocol.* ile alan alan birebir eşleşir.
net/              WebSocket bağlantısı (sertifika sabitleme + otomatik yeniden bağlanma) ve
                  tipli istek/yanıt sarmalayıcıları (AgentApi).
pairing/          Eşleştirme REST akışı: QR/PIN/yerel onay, elle bağlanma için TOFU parmak izi
                  onayı (UnpinnedFingerprintFetcher — yalnızca bu tek amaç için, dar kapsamlı).
security/         Sertifika sabitleme, şifreli kimlik bilgisi deposu (EncryptedSharedPreferences).
connection/       Uygulama genelinde paylaşılan bağlantı deposu + bağlantıyı ayakta tutan
                  önplan (foreground) servisi.
data/local/       Room önbelleği (yalnızca disk geçmişi/uyarılar/denetim günlüğü için —
                  süreçler/uygulamalar/metrikler her zaman canlı, önbelleklenmez).
notifications/    Bildirim kanalları ve uyarı bildirimleri.
ui/               Compose ekranları + ViewModel'ler (onboarding/eşleştirme, panel, süreçler,
                  uygulamalar, disk, uyarılar, ayarlar, denetim günlüğü) + ortak durum bileşenleri
                  (yükleniyor/boş/hata/çevrimdışı/yetkisiz).
```

Bağımlılık enjeksiyonu kasıtlı olarak elle yapılır (Hilt/Dagger yok) — `CepGozcuApp` tek seferlik
singleton'ları kurar, ViewModel'ler `(application as CepGozcuApp)` üzerinden erişir. Bu ölçekteki
bir uygulama için daha basit ve daha az "sihir" içeriyor.

## Derleme

Gereksinimler: JDK 17+, Android SDK (platform 34, build-tools 34.0.0), Gradle (depoda `gradlew`
mevcut).

```bash
cd android-app
./gradlew testDebugUnitTest   # birim testleri
./gradlew assembleDebug       # imzasız debug APK
./gradlew assembleRelease     # imzalı, küçültülmüş (R8) release APK — bkz. aşağıdaki imzalama notu
```

Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
Release APK: `app/build/outputs/apk/release/app-release.apk`

## Release imzalama

`app/build.gradle.kts`, `keystore.properties` (repo kökünde, **git'e dahil değil**) varsa
release derlemesini otomatik imzalar. Kendi anahtarınızı üretmek için:

```bash
keytool -genkeypair -v -keystore android-app/keystore/release.keystore \
  -alias cepgozcu -keyalg RSA -keysize 2048 -validity 10950
```

sonra `android-app/keystore.properties` dosyasını oluşturun:

```properties
storeFile=keystore/release.keystore
storePassword=...
keyAlias=cepgozcu
keyPassword=...
```

Bu depoda teslim edilen APK, bu şekilde üretilmiş gerçek bir imzalı derlemedir (bkz. depo kökündeki
README'de SHA-256 değeri). Gelecekte aynı imzayla güncelleme yayınlamak isterseniz, size ayrıca
verilen `.keystore` dosyasını saklayın — kaybederse yeni bir anahtar üretip uygulamayı telefonlara
yeniden (üzerine yazarak değil, önce eskisini kaldırarak) kurmanız gerekir.

## Testler

```bash
./gradlew testDebugUnitTest
```

JVM birim testleri (gerçek ağ veya emülatör gerektirmez): protokol serileştirme round-trip'i (ve
kritik alanlarda tam camelCase JSON string temsilinin — örn. `"kind":"created"` — .NET tarafıyla
birebir eşleştiğinin doğrulanması), eşleştirme durum makinesi/QR ayrıştırma, ve sahte
(`AgentApiContract`/`ConnectionSource`) bağımlılıklarla bir ViewModel'in durum geçişleri
(Loading→Content, çevrimdışı/hata/yetkisiz durumlara geçiş).

**Bu geliştirme ortamında doğrulanamayan şey:** gerçek bir Android cihaz veya emülatörde uçtan uca
kullanıcı akışı (QR okutma, gerçek bir CepGözcü Agent'a bağlanma, ekranlarda gezinme). Bu konteyner
KVM/donanım sanallaştırma desteklemediği için emülatör çalıştırılamadı. Bunun yerine: (a) yayımlanan
APK'nın gerçekten kurulabilir olduğu `apksigner verify` ve `aapt dump badging` ile doğrulandı
(imza geçerli, paket/izin/SDK bilgileri doğru, dört ana CPU mimarisi de (arm64-v8a, armeabi-v7a,
x86, x86_64) pakete dahil), (b) Windows ajanı tarafı gerçek bir çalışan sunucuya karşı gerçek
WebSocket istemcileriyle (bu depoda değil, geliştirme sırasında) uçtan uca test edildi — bkz. depo
kökü README'sinin "Bilinen sınırlar" bölümü.

## Tasarım dili

Material 3, açık/koyu tema + Android 12+ üzerinde dinamik renk desteği ile; koyu lacivert/mavi
vurgu rengi Windows tarafındaki yönetim paneliyle görsel tutarlılık için bir başlangıç noktası
olarak kullanıldı, ancak Compose/Material 3 deyimlerine göre uyarlandı (gösterge/gauge'lar, disk
büyüme zaman çizelgesi gibi grafikler için harici bir kütüphane yerine Compose Canvas kullanıldı).
