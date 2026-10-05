# Melodi Zil — Android

YouTube bağlantısı (<10 dk) ya da telefondaki bir ses dosyasından **baskın melodiyi** çıkarıp 8 farklı tınıda (piyano, müzik kutusu, 8-bit, marimba, flüt, çan, gitar, synth) **zil sesi / bildirim / alarm** üreten Android uygulaması. Tüm işlem cihazda; sunucu, hesap, reklam yok.

Fabrika belgeleri (fikir → yayın) `fabrika/` klasöründe; mağaza metni ve yükleme adımları `docs/PLAY_STORE.md`; gizlilik politikası `docs/gizlilik.html`.

## Mimari
```
app/src/main/kotlin/com/jn/melodizil/
  core/        Saf Kotlin (Android bağımsız, JVM'de test edilir)
    Fft.kt              radix-2 FFT
    MelodyExtractor.kt  STFT → harmonik toplam (salience) → Viterbi → nota bölütleme
    Synth.kt            8 tını: toplamsal sentez + Karplus-Strong; fade, limiter, normalizasyon
    SegmentChooser.kt   En yoğun N saniyeyi (nakarat) seçer
    WavWriter.kt        16-bit mono WAV
    YouTubeUrl.kt       Bağlantı → video kimliği
    Resampler.kt        Mono + yeniden örnekleme
  data/
    youtube/   NewPipeExtractor + OkHttp: akış çözümleme (3 deneme) ve Range'li indirme
    audio/     MediaExtractor+MediaCodec → float PCM; AudioTrack önizleme
    store/     MediaStore kaydı + RingtoneManager varsayılan ayarı; DataStore tercihler/kütüphane
  domain/      MelodyPipeline: çözümle → indir → çöz → analiz (ilerleme + iptal)
  ui/          Compose: theme (token'lar = fabrika/DESIGN.md), components, 6 ekran, nav, AppViewModel
```

## Melodi çıkarma nasıl çalışır (özet)
1. 22.05 kHz mono; Hann 2048, atlama 512.
2. Her çerçevede spektral tepeler (parabolik hassaslaştırma); her tepe 1..10 alt harmoniği olan f0 adaylarını 0.8^(h-1) ağırlıkla oylar (Salamon & Gómez "Melodia" sadeleştirmesi). İnsan sesi/baskın melodi aralığı (E4 merkezli çan) hafif öncelikli.
3. 49 yarım ton + "sessiz" durumunda Viterbi: sıçrama cezası 0.35/yarım ton (üst sınır 4.5), sesli↔sessiz 2.2.
4. Aynı perdedeki ardışık çerçeveler nota olur; <90 ms notalar birleştirilir/atılır.

Gerçek bir pop şarkısında (3:33) JVM'de 1.0 s sürdü, 337 nota, tümü şarkının tonalitesinde (A♭ majör). Sentetik çok sesli testte %92 çerçeve doğruluğu (`CoreTest`).

## Derleme
Gerekenler: JDK 17+, Android SDK (platform 35, build-tools 35). `local.properties` içine `sdk.dir=/yol/android-sdk`.
```bash
./gradlew testDebugUnitTest          # birim testleri (core + canlı YouTube testi MELODI_LIVE=1 ile)
./gradlew assembleDebug              # app/build/outputs/apk/debug/app-debug.apk
./gradlew bundleRelease              # app/build/outputs/bundle/release/app-release.aab  (Play'e yüklenecek)
./gradlew assembleRelease            # app/build/outputs/apk/release/app-release.apk (doğrudan kurulum)
./gradlew lintDebug                  # Android Lint (0 hata)
```
İsteğe bağlı gerçek şarkı testi: `MELODI_TEST_PCM=<f32le mono 22050 ham dosya> MELODI_TEST_OUT=<klasör> ./gradlew testDebugUnitTest --tests '*RealSongHarnessTest*'` her tını için 30 sn WAV üretir.

## İmza
`keystore/upload.jks` + `keystore.properties` bu oturumda üretilen **yükleme anahtarı**dır (Play App Signing ile kullanılır). Üretim için kendi anahtarınla değiştirmek istersen `keystore.properties` değerlerini güncelle. Şifreler dosyada; depo özel kalmalı ya da anahtar değiştirilmeli.

## Lisans
Uygulama kodu GPL-3.0 (NewPipeExtractor GPL-3.0 bağımlılığı gereği). Üçüncü taraf lisansları Ayarlar → Açık kaynak lisansları.
