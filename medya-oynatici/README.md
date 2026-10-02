# Oynatıcı — sade MP3 & video oynatıcı (Android)

Açınca karşına iki büyük buton çıkar: **Müzik** ve **Video**. Gerisi o kadar basit.

## Kurulum (telefona)

1. `dist/` klasöründen telefonuna uygun APK'yı indir:
   - **`Oynatici-arm64-v8a.apk`** → neredeyse bütün telefonlar (2017 sonrası). Emin değilsen bunu al.
   - `Oynatici-armeabi-v7a.apk` → çok eski / ucuz telefonlar.
2. Dosyaya dokun, "bilinmeyen kaynaklardan yüklemeye izin ver" de, kur.
3. İlk açılışta bildirim izni ister (indirme ilerlemesi için) — izin ver.

Sunucu, hesap, giriş yok. İnternet sadece YouTube'dan indirirken gerekir.

## Neler yapıyor

| Özellik | Nasıl |
|---|---|
| Müzik / Video bölümleri | Ana ekrandaki iki büyük buton |
| Dosya ekleme | **Ekle → Dosya ekle**: telefondan istediğin kadar ses/video seç |
| Klasör ekleme | **Ekle → Klasör ekle**: klasörü seç, içindeki 20 şarkı tek seferde, **klasör olarak** gelir (alt klasörler de korunur) |
| Kendi klasörün | **Ekle → Yeni klasör** (örn. "Gitar videoları", "Filmler") |
| Düzenleme | Satırdaki **⋮** (veya uzun bas): Kırp · Yeniden adlandır · Taşı · Sil |
| Çalma | Dosyaya dokun → klasördeki diğerleri sıraya girer. Müzik ekran kapalıyken de çalar, bildirimde kontroller var |
| YouTube → MP3 | Müzik bölümünde **Ekle → YouTube'dan indir**, linki yapıştır (panoda link varsa kendiliğinden gelir). Kalite sorulmaz: **en yüksek kalitede MP3** iner |
| YouTube → Video | Video bölümünde aynı şekilde; **kalite sorulur** (144p … 1080p, En yüksek) ve MP4 iner |
| Oynatma listesi | Liste linki verirsen hepsi iner ve **liste adıyla bir klasöre** toplanır (müzikte MP3, videoda seçtiğin kalitede) |
| Paylaş ile indirme | YouTube uygulamasında **Paylaş → Oynatıcı** → "Müzik mi Video mu?" |
| Kırpma | **⋮ → Kırp**: iki tutamaçla aralık seç **veya** süreleri yaz (`1.28` – `2.16`, `1:28` de olur). **Orijinal silinmez**, kesit yeni dosya olarak kaydedilir: `Şarkı (1.28-2.16).mp3` |

## Nasıl çalışıyor (teknik)

- **Kotlin + Jetpack Compose**, tek Activity, Material 3 koyu tema.
- **Oynatma:** AndroidX Media3 (ExoPlayer + MediaSessionService → arka planda çalma, bildirim/kilit ekranı kontrolleri).
- **YouTube indirme:** [youtubedl-android](https://github.com/yausername/youtubedl-android) — yt-dlp ve ffmpeg'i **telefonun içinde** çalıştırır (Seal gibi uygulamaların kullandığı kütüphane). YouTube sık değiştiği için uygulama yt-dlp'yi günde bir kendiliğinden günceller; indirme hata verirse önce güncelleyip bir kez daha dener.
  - Müzik: `-f bestaudio -x --audio-format mp3 --audio-quality 0`
  - Video: `-S res:<kalite>,vcodec:h264` + MP4 birleştirme (her telefonda açılsın diye H.264 tercih edilir)
  - Liste: `%(playlist_title)s/001 - başlık.ext`
- **Kırpma:** video → Media3 Transformer (telefonun donanım kodlayıcısı, saniyesi saniyesine; olmazsa ffmpeg ile kopyalayarak). Ses → ffmpeg `-c copy` (yeniden kodlama yok, kalite kaybı yok).
- **Dosyalar:** `Android/data/com.jn.oynatici/files/Muzik` ve `.../Video` altında gerçek klasörler. Not: uygulama silinirse bu dosyalar da silinir.

## Derleme

```bash
# Android SDK (platforms;android-36, build-tools;36.0.0) gerekli
echo "sdk.dir=/android-sdk/yolu" > local.properties
./gradlew assembleRelease
# çıktı: app/build/outputs/apk/release/app-<abi>-release.apk
```
