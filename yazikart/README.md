# YazıKart

Instagram'ın hikâye/gönderi editörüyle uğraşmadan, **düz bir zemin ya da bir fotoğraf üstüne yazı yazıp**
görsel olarak kaydetmeye/paylaşmaya yarayan basit Android uygulaması.

## Ne yapıyor?

| Özellik | Açıklama |
|---|---|
| Zemin rengi | Hazır 20 renk (ilki siyah) + ton/doygunluk/parlaklık ya da HEX kodu ile özel renk |
| Yazı | Çok satırlı yazı; boyut, renk, kalın, gölge, hiza (sol/orta/sağ), konum (üst/orta/alt). Sığmayan yazı otomatik küçülür |
| 50 yazı tipi | Dünyada en çok kullanılan Google Fonts yazı tipleri (Roboto, Open Sans, Montserrat, Poppins, Bebas Neue, Pacifico…). Hepsi uygulamanın içinde, **internetsiz çalışır**, hepsi Türkçe karakterleri (ç ğ ı İ ö ş ü) destekler. Arial yerine metrik olarak birebir aynı **Arimo**, Times New Roman yerine **Tinos** var |
| Google Görseller | "Google'da ara"ya bas, uygulama içinde gerçek Google Görseller açılır; görsele dokun, büyüyünce basılı tut → arka plan olur. İnternet yoksa düğme "İnternet yok" der |
| Galeriden görsel | Telefondaki bir fotoğrafı arka plan yap |
| Karartma | Arka plan fotoğrafını koyulaştırıp yazıyı okunur yapar |
| Boyut | Kare 1:1 (1080×1080), Dikey 4:5 (1080×1350), Hikâye 9:16 (1080×1920) |
| Kaydet | PNG ya da JPEG olarak galeriye, `Resimler/YaziKart` klasörüne |
| Paylaş | Telefonun paylaşma penceresi (Instagram, WhatsApp, X…) |

Ekrandaki önizleme ile kaydedilen dosya **aynı kodla** çizilir; ekranda ne görüyorsan dosyada o çıkar.
Son kullandığın stil (yazı tipi, renkler, boyut…) hatırlanır.

## Görsel arama (Google Görseller) ve gizlilik

"Google'da ara"ya basınca uygulamanın içinde **gerçek Google Görseller sayfası** açılır. Görsele dokun,
büyüyünce üstüne **basılı tut**: görsel telefona iner ve arka plan olur.

- Uygulamanın **kendi sunucusu yok**. Yazdıkların, renklerin, kaydettiğin görseller hiçbir yere gönderilmez,
  her şey telefonda kalır.
- Sadece Google'da arama yaptığında telefon **doğrudan Google'a** bağlanır (Chrome'da arama yapmakla aynı);
  seçtiğin görsel de doğrudan bulunduğu siteden iner. Arada başka kimse yok.
- İnternet yoksa bu ekran açılmaz; uygulamanın geri kalanı internetsiz çalışır.
- Görsellerin telif hakkı sahiplerine aittir; kişisel kullanım dışında kaynağı kontrol et.

## Kurulum (telefona)

`dist/YaziKart-1.0.0.apk` dosyasını telefona at, aç, "bilinmeyen kaynaklardan yüklemeye izin ver" de, kur.
Android 8.0 ve üstü.

> İmza: APK, depodaki `keystore/dev-release.jks` geliştirme anahtarıyla imzalı (kasıtlı olarak depoda;
> böylece sonraki sürümler aynı imzayla üstüne güncellenebilir). Play Store'a koyacaksan kendi gizli anahtarını üret.

## Derleme

```bash
# Android SDK yolu (platforms;android-34 ve build-tools;34.0.0 kurulu olmalı)
echo "sdk.dir=/android-sdk/yolu" > local.properties
./gradlew testDebugUnitTest assembleRelease
# çıktı: app/build/outputs/apk/release/app-release.apk
```

## Kod haritası

```
app/src/main/kotlin/com/jn/yazikart/
  MainActivity.kt          tek ekran; ana ekran <-> arama ekranı geçişi, galeri seçici, izin
  YaziKartApp.kt           görsel indirici (Coil) ayarı
  data/FontCatalog.kt      50 yazı tipinin listesi ve yüklenmesi
  data/PostStyle.kt        görsel ayarları + son stili saklama
  data/ImageExporter.kt    galeriye kaydet (PNG/JPEG) + paylaş
  render/PostRenderer.kt   önizleme ve kaydın ortak çizicisi
  search/GooglePick.kt     Google Görseller'de basılı tutulan görselden en iyi adresi seçer
  search/NetworkMonitor.kt internet var mı (canlı)
  ui/                      ekranlar ve paneller (Jetpack Compose); GoogleImagesScreen = uygulama içi Google Görseller
app/src/main/assets/fonts/ 50 yazı tipinin dosyaları (Latin + Türkçe karakterlere küçültülmüş)
```

Yazı tipleri SIL Open Font License / Apache 2.0 lisanslıdır; uygulama içinde dağıtılmaları serbesttir.
