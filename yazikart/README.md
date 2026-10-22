# YazıKart

Instagram'ın hikâye/gönderi editörüyle uğraşmadan, **düz bir zemin ya da bir fotoğraf üstüne yazı yazıp**
görsel olarak kaydetmeye/paylaşmaya yarayan basit Android uygulaması.

## Ne yapıyor?

| Özellik | Açıklama |
|---|---|
| Zemin rengi | Hazır 20 renk (ilki siyah) + ton/doygunluk/parlaklık ya da HEX kodu ile özel renk |
| Yazı | Çok satırlı yazı; boyut, renk, kalın, gölge, hiza (sol/orta/sağ), konum (üst/orta/alt). Sığmayan yazı otomatik küçülür. Önizlemeye dokununca ya da "Yazıyı düzenle"ye basınca klavye hemen açılır, yazdıkça önizleme canlı değişir |
| 50 yazı tipi | Dünyada en çok kullanılan Google Fonts yazı tipleri (Roboto, Open Sans, Montserrat, Poppins, Bebas Neue, Pacifico…). Hepsi uygulamanın içinde, **internetsiz çalışır**, hepsi Türkçe karakterleri (ç ğ ı İ ö ş ü) destekler. Arial yerine metrik olarak birebir aynı **Arimo**, Times New Roman yerine **Tinos** var |
| Google Görseller | "Google'da ara"ya bas, uygulama içinde gerçek Google Görseller açılır; görsele dokun, büyüyünce basılı tut → arka plan olur. İnternet yoksa düğme "İnternet yok" der |
| Galeriden görsel | Telefondaki bir fotoğrafı arka plan yap |
| Sıra (fotoğraf + video) | Galeriden ya da paylaşımla en fazla 10 parça arka arkaya eklenir. Fotoğraf 3 sn görünür (en çok 10 sn). Video, kırpma şeridinde iki tutamacı sürükleyerek başlangıç/bitiş seçilerek kırpılır. Parçalar basılı tutup sürüklenerek ya da "Öne al / Arkaya al" ile sıralanır. Önizleme tüm sırayı canlı oynatır, yazı hepsinin üstünde kalır |
| Video ara | Uygulama içinde ücretsiz video siteleri: Pexels, Pixabay, Coverr, Mixkit. Videoyu aç, oynat, "Bu videoyu ekle"ye bas — giriş gerekmez. TikTok, Pinterest, X gibi sitelerden indirme yapılmaz; oradaki "kaydet" özelliğiyle kaydettiğin videoyu galeriden ekle ya da paylaş ile YazıKart'a gönder |
| Müzik | Telefondaki bir ses dosyası, videonun altında çalar. Şarkının hangi saniyeden başlayacağı ve ses seviyesi ayarlanır. Videoların kendi sesi açılıp kapatılabilir |
| Paylaşımla ekle | Galeride ya da başka bir uygulamada bir fotoğraf/video için "Paylaş → YazıKart" dersen doğrudan sıraya eklenir; birden fazla seçince hepsi sırayla gelir |
| Video kaydet/paylaş | Sıra doluyken Kaydet/Paylaş **MP4** üretir: fotoğraf ve videolar birleştirilir, kırpılır, seçilen boyuta (ör. 1080×1920) ortadan kırpılarak getirilir, yazı üstüne basılır, müzik eklenir. Telefonun donanım kodlayıcısı kullanılır; galeride `Filmler/YaziKart` |
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

## Hafiflik

- Video için Android'in kendi kütüphanesi (Media3) kullanılır; FFmpeg gibi ağır bir motor yok.
- Kullanılmayan kod derlemede atılır (R8): video eklenmesine rağmen APK **16,7 MB → 7,8 MB** oldu.
- Uygulamanın sunucusu yok; video ve görseller doğrudan bulundukları siteden telefona iner.

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
  MainActivity.kt          tek ekran; galeri/müzik seçiciler, paylaşımla gelen dosyalar, ekran geçişleri
  YaziKartApp.kt           görsel indirici (Coil) ayarı
  data/FontCatalog.kt      50 yazı tipinin listesi ve yüklenmesi
  data/PostStyle.kt        görsel ayarları + son stili saklama
  data/ImageExporter.kt    galeriye kaydet (PNG/JPEG/MP4) + paylaş
  render/PostRenderer.kt   önizleme ve kaydın ortak çizicisi
  search/GooglePick.kt     Google Görseller'de en iyi görsel adresi + video sitesi adresleri
  data/VideoClip.kt        sıra parçası (video/fotoğraf, aralık, süre), müzik ve sıra hesapları
  video/VideoExporter.kt   parçaları birleştir + kırp + yazıyı bas + müziği ekle → MP4 (Media3 Transformer)
  video/Thumbs.kt          videodan küçük kareler (sıra ve kırpma şeridi için)
  video/VideoDownloader.kt internetten video indirme (300 MB sınır)
  search/NetworkMonitor.kt internet var mı (canlı)
  ui/EditorViewModel.kt    tüm durum: stil, sıra, önizleme oynatıcıları, indirme ve kaydetme
  ui/EditorScreen.kt       ana ekran: sabit canlı önizleme + kaydırılabilir ayarlar + Kaydet/Paylaş
  ui/SequencePanel.kt      "Sıra ve müzik" sekmesi: parça şeridi, kırpma şeridi, müzik
  ui/VideoLayer.kt         önizlemede videonun çizimi
  ui/GoogleImagesScreen.kt uygulama içi Google Görseller
  ui/                      diğer ekranlar ve paneller (Jetpack Compose)
app/src/main/assets/fonts/ 50 yazı tipinin dosyaları (Latin + Türkçe karakterlere küçültülmüş)
```

Yazı tipleri SIL Open Font License / Apache 2.0 lisanslıdır; uygulama içinde dağıtılmaları serbesttir.
