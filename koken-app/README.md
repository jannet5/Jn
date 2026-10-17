# Köken — sözcüğün ve ismin kökenini bulan Android uygulaması

Bir sözcük ya da isim yaz (örn. **Ahmet**, **kitap**, **salary**). Uygulama Google'da tek tek aramana gerek bırakmadan
şunu gösterir: **hangi dilden geldiği**, **anlamı** ve **zaman içinde nasıl değiştiği** (köken zinciri).

- **Hesap, API anahtarı, sunucu yok.** İnternet yeterli. Uygulama herkese açık sayfaları doğrudan çeker.
- Tek izin: `INTERNET`.
- Açık/koyu tema (telefonun ayarını izler). Android 8.0+.

## Telefona kurmak

`dist/Koken-1.0.0.apk` dosyasını telefona indir ve aç. ("Bilinmeyen kaynaklardan yükleme"ye izin vermen istenebilir.)
GitHub mobilde: dosyaya dokun → **Download raw file**.

## Nasıl çalışıyor?

Aranan sözcük için aynı anda üç kaynaktan sayfa gövdesi alınır (`index.php?action=render`, anahtarsız):

| Sekme | Kaynak | Ne verir |
|---|---|---|
| Vikisözlük | tr.wiktionary.org | **Türkçe** köken açıklaması ve anlamlar |
| Wiktionary EN | en.wiktionary.org | En geniş kapsam; köken zinciri, akrabalar, çok dilli girişler |
| Vikipedi | tr.wikipedia.org | Özellikle **isimler** için köken/anlam bilgi kutusu ve özet |

Köken paragrafı (`Inherited from Ottoman Turkish … from Arabic …` / `… sözcüğünden devralındı, o da Arapça …`)
**dil etiketli zincire** çevrilir: dil, yazılış, okunuş, anlam ve basamağın nasıl geçtiği (devralındı / ödünç alındı).
İngilizce dil adları Türkçeleştirilir. Özetteki **Köken** etiketi, zincirdeki "Proto-" olmayan en eski dildir.
Büyük/küçük harf farkı otomatik denenir (`ahmet` → `Ahmet`). Sonuçlar bellekte önbelleğe alınır, son aramalar kaydedilir.

Hiçbir şey sunucudan "uydurulmaz": her bilgi kaynağın kendi sayfasından gelir ve açıklama metni olduğu gibi gösterilir;
zincir ayrıştırmada bir sapma olursa asıl metin hemen altında durur. Sayfa sonundaki bağlantılar kaynağı, Nişanyan Sözlük'ü
ve Etymonline'ı tarayıcıda açar.

**Lisans / atıf:** Vikisözlük, Wiktionary ve Vikipedi içeriği CC BY-SA 4.0 altındadır; uygulama içeriği canlı çeker ve
kaynağını gösterir. `app/src/test/resources/fixtures/` altındaki sayfalar yalnızca test verisidir.

## Geliştirme

```bash
# Gerekenler: JDK 17+, Android SDK (platforms;android-34, build-tools;34.0.0); local.properties içine sdk.dir yaz
./gradlew :app:testDebugUnitTest      # gerçek Vikisözlük/Vikipedi sayfaları üzerinde ayrıştırıcı testleri
./gradlew :app:assembleDebug          # app/build/outputs/apk/debug/app-debug.apk
./gradlew :app:assembleRelease        # imzalı release APK (keystore/koken-dev.jks)
```

`keystore/koken-dev.jks` yalnızca **yerel/geliştirme imzasıdır** (temiz bir checkout'tan aynı imzayla release üretilebilsin diye
bilerek repoda). Mağaza yayını için kendi anahtarınla değiştirin.

### Emülatörde (proxy arkasında) test

Bu sandbox gibi TLS'i ara proxy'den geçen ortamlarda: `tools/emulator-trust-ca.sh <proxy-ca.crt>` yalnızca **debug** derlemeye
bu CA'yı güvenilir ekler (`app/src/debug/`, `.gitignore`'da; release APK'ya girmez). Emülatör için
`adb shell settings put global http_proxy 10.0.2.2:<port>`.

## Bilinen sınırlar

- Kaynaklarda kaydı olmayan sözcük/isim bulunamaz (örn. çok yeni ya da çok nadir isimler); bu durumda Vikisözlük ve
  Nişanyan Sözlük'te arama bağlantıları sunulur.
- İngilizce Wiktionary açıklamaları İngilizce kalır (Türkçe sekmesi varsa o önce gösterilir).
- Vikimedya aşırı istek gelirse geçici olarak sınır koyar; uygulama bunu "Sunucu şu an yoğun" diye bildirir.
