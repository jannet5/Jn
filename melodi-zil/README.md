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
1. **Vokal ayırma (cihazda):** Spleeter 2stems vokal U-Net'i (Deezer Research, MIT), TFLite fp16, `assets/vokal.tflite` (19.7 MB). Enstrümantal parçada bu adım karışımdan devam eder.
2. **Perde (cihazda):** CREPE "tiny" (Kim ve ark. 2018; ağırlıklar torchcrepe, MIT), TFLite, `assets/crepe_tiny.tflite` (2 MB). 16 kHz vokal, 20 ms kare, sessiz kareler atlanır; Viterbi yolu (`core/CrepeMelody.kt`).
3. **Notalar:** güven ve enerji eşiği, akort kayması düzeltmesi, medyan filtre, histerezis, kayma birleştirme (`core/NoteSegmenter.kt`).
4. Yedek: model açılamazsa ya da şarkı enstrümantalsa Melodia (Salamon & Gómez 2012, `core/MelodyExtractor.kt`).

## Ölçülen doğruluk (`araclar/degerlendirme/`)
| Ölçüm | v1.0 | v1.1 (Melodia + ayırma) | v1.2 (ayırma + CREPE) |
|---|---|---|---|
| Vocadito + pop eşlik (40), ham doğru perde | — | %67 | %97 |
| Aynı set, nota F (insan işaretli notalar, ±50 ms, ±50 cent) | 0.27 | 0.53 | 0.60 |
| Gerçek pop şarkısında nakarat notaları | yok | yok | doğru sırayla |
Karşılaştırılan diğer modeller: RMVPE (karışımda %96, 181 MB), FCPE (ayrılmış vokalde %97.5, 43 MB).

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
Uygulama kodu GPL-3.0 (NewPipeExtractor GPL-3.0 bağımlılığı gereği). Vokal modeli: Spleeter (Deezer Research, MIT). Perde modeli: CREPE tiny (Kim ve ark. 2018; torchcrepe ağırlıkları, MIT). TensorFlow Lite: Apache-2.0. Üçüncü taraf lisansları Ayarlar → Açık kaynak lisansları.
