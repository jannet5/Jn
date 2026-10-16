# Tur 1 kontrol noktası (2026-10-16, 23:17–23:47 UTC)

## Yapıldı (kanıtlı)
- Önceki kod bu dala taşındı (commit de3aa4d) → istenirse `git checkout de3aa4d -- melodi-zil` ile o hale dönülür.
- Android SDK 35 + build-tools + emülatör + AVD (api26, api34) kuruldu: `/home/user/android-env.sh`. (Bu oturumluk; yeni oturumda yeniden kurulur, betik komutları içinde.)
- ML ortamı (`/home/user/venv-ml`: torch CPU, demucs, torchcrepe, basic-pitch, mir_eval…), RMVPE ağırlıkları, ORTAM.md.
- Veri: MIR-1K (100 klip karışım+vokal, perde etiketli), Vocadito (40 solo, insan notalı). Ölçüm aleti: `araclar/degerlendirme/olc.py` (RPA, RCA, OA, oktavsız RPA, nota F, RTF).
- Ölçüm sonuçları (`arastirma/sonuc/*.json`): RMVPE ayırmasız karışımda doğru perde **%92–95** (hedef ≥%80 ✅). pYIN karışımda %29,7.
- Nota temizleme: `arastirma/ortak/nota_cikar.py` — nota F Vocadito **0,46→0,64**, MIR-1K karışım **0,665** (hedef 0,65: ham perdede ✅ sınırda; yarım tona yuvarlanmış çıktıda 0,61–0,62 ⚠️).
- Araştırma: `docs/ARASTIRMA_MELODI.md`.

- Model küçültme: RMVPE ONNX int8 (dinamik nicemleme) **361 MB → 98,7 MB**, doğruluk aynı (MIR-1K karışım 10 klip: doğru perde %91,26 vs %91,28; nota F 0,634 vs 0,633). Hız (bu x86 CPU, 2 iş parçacığı): gerçek zamanın ~0,09 katı (3–4 dk şarkı ≈ 20 sn); arm64 telefonda ölçülecek. Dosya: `/home/user/models/rmvpe_q/rmvpe_int8.onnx` (depoda yok; betik: nicemleme `onnxruntime.quantization.quantize_dynamic`).

## Yapılamadı / açık
- YouTube’dan gerçek pop şarkı indirilemedi (bot kontrolü) → Ç-015. Bu yüzden **3 şarkılık dinleme örneği henüz yok**.
- Model 98,7 MB (int8) hâlâ APK sınırına (30 MB) sığmaz → ilk açılışta indirilecek (SHA-256 + ilerleme çubuğu) ya da FCPE (≈10 M parametre) aday olarak ölçülecek.
- Nota F yarım tona yuvarlanmış halde hedefin biraz altında; enerji/onset ile aynı perdeli tekrar notaları ayırma denenecek.
- Android uygulama: yeni hat (RMVPE ONNX + nota_cikar) Kotlin’e taşınmadı; emülatör testleri (API 26/34, süreç öldürme, tam uzunluk) bu turda yapılmadı.
- Demucs/CREPE/basic-pitch/FCPE adayları: ajanlar hâlâ ölçüyor olabilir; biten sonuçlar `arastirma/sonuc/` içinde.

## Sıradaki tur (tur 2) sırası
1. NewPipeExtractor ile (JVM testi) 3–5 bilinen şarkıyı indir → RMVPE+nota_cikar → 30 sn melodi MP3 (kullanıcı dinleyecek).
2. RMVPE’yi fp16/int8 ONNX → boyut + arm64’e yakın hız ölçümü; ORT-mobile.
3. Kotlin’e port + `ProcessingService` içine bağla + gerçek süre metni.
4. Emülatör uçtan uca (API 26 + 34), süreç öldürme, 3–4 dk şarkı.
5. APK <30 MB, SHA-256, kart, Drive.
