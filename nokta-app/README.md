# nokta

Alışveriş / plan listesi için küçük ve hızlı Android uygulaması. `applicationId`: `app.nokta.list`, minSdk 26, targetSdk 34.

- Sağ kenarda yuvarlak yüzen balon (rozet = kalan sayı); dokununca liste açılır, dikey sürüklenir.
- Satıra dokun → üstü çizilir (silmez). Sağdaki × → siler.
- Soldaki tutamaçla (veya satıra basılı tutarak) sürükle-sırala.
- Veri yerelde (SharedPreferences JSON), bağımlılık: appcompat, recyclerview, core-ktx. Release APK ≈ 0,7 MB.

Marka, logo ve renkler (kâğıt #F5F1E8, mürekkep #191815, vermilyon #E8502A) bilerek sade tutuldu: kart/gradyan/emoji yok, ince ayraç çizgileri.

## Derleme
```
echo "sdk.dir=/path/to/android-sdk" > local.properties
./gradlew testDebugUnitTest assembleRelease
```
İmzalı release için `keystore.properties` gerekir (repoda **yok**, gizli tutulur). Anahtar yoksa imzasız APK çıkar.

## Çıktı
`dist/nokta-1.0.0-release.apk` (v2 imzalı, SHA256: `dist/SHA256SUMS.txt`).

## Doğrulama (gerçekten yapılan)
- 6 birim testi (ekle/boş reddi, üstünü çiz, sil, sırala, JSON gidiş-dönüş, bozuk JSON) — geçti.
- KVM'siz yazılım emülatörü, Android 8.0 (API 26): APK kuruldu; adb ile ekle, üstünü çiz, sil, tutamaçla sürükleyip sırala, uygulamayı kapatıp açınca kalıcılık, balonun ana ekranda görünmesi ve dokununca uygulamanın açılması denendi. Ekran görüntüleri `docs/`.
- Test edilmeyen: gerçek telefon, Android 13+ bildirim/overlay izin akışı, Play Store yükleme, uzun basarak sürükleme (adb ile tetiklenemedi; tutamaç yolu test edildi).
