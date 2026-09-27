# TekPanel

Android işletmecisinin telefonuna WhatsApp, Instagram, TikTok, Messenger, Telegram, X ve
SMS gibi kanallardan gelen **yeni müşteri mesajlarını**, kullanıcının verdiği bildirim
erişimi izniyle, tek bir yerel ekranda toplayan uygulama.

Bu proje **sıfırdan** (greenfield) yazılmıştır; şartname `docs/` altındaki belgelerde,
kararlar `docs/DECISIONS.md`'de, kalan işler `docs/ROADMAP.md`'de, yayın hazırlığı
`docs/RELEASE.md`'de kayıtlıdır.

## Gereksinimler

- JDK 17+ (proje JDK 21 ile derlendi ve test edildi)
- Android SDK: `platform-34`, `build-tools;34.0.0`, `platform-tools`
- Gradle 8.9+ (bu ortamda 8.14.3 ile derlendi)

`local.properties` dosyasında `sdk.dir=<android-sdk-yolu>` tanımlı olmalıdır (git'e girmez).

## Derleme ve test

```
./gradlew :app:testDebugUnitTest   # 26 birim testi
./gradlew :app:assembleDebug        # kurulum yapılabilir debug APK
./gradlew :app:assembleRelease      # minified/shrink release APK (imzasız, bkz. docs/RELEASE.md)
./gradlew :app:bundleRelease        # Play Console'a yüklenecek .aab
```

## Mimari (kısaca)

- **Kotlin + Jetpack Compose (Material 3)**, tek Activity, tek gerçek ekran.
- **Room** mesaj deposu, **DataStore** kanal/tercih deposu, **Koin** için hafif DI.
- Bildirim yakalama zinciri saf Kotlin sınıflarına ayrıldı (`capture/`), bu yüzden
  `NotificationListenerService`'e veya Robolectric'e ihtiyaç duymadan birim testleriyle
  doğrulanabilir: `NotificationExtractor` (Android'e bağlı) → `MessageNormalizer` → 
  `MessageFilterEngine` → `Fingerprint`/`DuplicateGuard` (tümü saf Kotlin).

Ayrıntılı CAP-01..17 → dosya eşlemesi: `docs/RELEASE.md` §2.

## Bu depoya commit edilmeyenler

`.gitignore`: `build/`, `.gradle/`, `local.properties`, imzalama anahtarları
(`*.jks`, `*.keystore`, `release/keystore.properties`), üretilen `.apk`/`.aab` dosyaları.
Derlenmiş artifact'lerin SHA-256 kayıtları `docs/RELEASE.md`'dedir.
