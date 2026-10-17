# Tur 3 kontrol noktası (2026-10-17 09:14 – 09:44 UTC)

## Yapıldı (kanıtlı)
- **Yeni melodi hattı uygulamada**: ses → 16 kHz → `OrtRmvpe` (ONNX Runtime, RMVPE int8) → `PitchTrack` → `NoteTracker` (nota_cikar_v2 portu) → mevcut sentez/zil akışı. Eski Spleeter/TFLite/Melodia hattı ve `vokal.tflite` silindi (`MelodyPipeline.kt`, `core/Rmvpe*.kt`, `core/NoteTracker.kt`, `data/audio/OrtRmvpe.kt`).
- **Model indirme** (`data/store/ModelStore.kt`): ilk açılışta 3 parça (~99 MB), parça başına devam (Range), parça + birleşik SHA-256 doğrulama, boş yer kontrolü, iptal. Geliştirme için `adb push` ile `/sdcard/Android/data/<paket>/files/models/` yolu da okunur. `Step.MODEL` adımı + Türkçe/İngilizce metinler (3–4 dk şarkı: 1–3 dk, ilk seferde ~99 MB).
- **Derleme**: `:app:assembleDebug` BAŞARILI (09:33). Debug APK: arm64 48,9 MB, armeabi-v7a 40,6 MB, x86_64 54,9 MB, universal 138,9 MB.
- **NoteTracker parite testi (Gradle/JUnit)**: 9/9 geçti. 5 klipte Kotlin = Python (183/183 nota, aynı MIDI, onset farkı 0 ms). 3 dk parça için 15–18 ms.
- **RMVPE parite (kotlinc + masaüstü ORT ile ayrı koşuldu)**: mel farkı 1,5e-4, ses uyumu %100, cent farkı medyan 0,13 (hedef <5). 10 sn ses 1,9 sn'de işlendi.
- Emülatör (Android 8.0, KVM'siz) 3,5 dk'da açıldı; uçtan uca betik hazır: `araclar/emu_e2e.sh` (kur → izin → model+ses it → dosya seç → melodi → zil yap → `settings get system ringtone`).
- Örnek "önce/sonra" melodiler (aynı 30 sn, piyano): eski yöntem (pYIN) bir kayıtta yalnızca 1 nota bulabildi; yeni yöntem 30–55 nota.

## Yapılamadı / açık
- **Emülatörde uçtan uca test bitmedi**: boot 09:36'da bitti, 55 MB APK'nın kurulumu yüklü makinede çok yavaştı; `/home/user/logs/e2e_kesit.log` arka planda sürüyor (sonuç ve ekran görüntüleri `docs/test/`).
- **Sistem zili gerçekten değişti mi, süreç öldürme, 3–4 dk parça: doğrulanmadı.**
- Release APK denenmedi; **arm64 debug 48,9 MB** (>30 MB). Release (R8) + ABI bölme ile küçülmesi beklenir ama ölçülmedi; ONNX Runtime .so ve NewPipe/Compose en büyük parçalar. Yetmezse: ORT minimal build, material-icons-extended'i çıkarma.
- `ModelStore` için JVM testi yazılmadı; `RmvpeParityTest` Gradle'da koşulmadı (kotlinc ile ayrı doğrulandı). Telefonda RMVPE hızı ölçülmedi.
- Pop şarkı örneği yok (YouTube IP-bağlı 403). Kart, Drive, APK eki henüz yok.

## Dersler
- **İş akışı aynı anda yalnızca CPU−2 = 2 ajan çalıştırıyor.** 4 ajan açınca son ikisi sırada bekledi ve iş yapmadan bitti (tur 2'de de aynı). Kural: en çok 2 paralel ajan; kritik işi ana oturum kendisi yapar.
- Gradle: `--refresh-dependencies` ve metadata silme Maven 429'u artırıyor; tek Gradle (flock), mümkünse önbellek sağlamken `--offline`.

## Tur 4 sırası
1. e2e sonucunu oku (log + ekran görüntüleri); çıkan hataları düzelt. Sonra `emu_kesinti.sh`, 3–4 dk parça.
2. Release arm64 APK <30 MB; SHA-256; Chrome kimliğiyle indirme.
3. Kart (artifact) + Drive + APK dosya eki.
