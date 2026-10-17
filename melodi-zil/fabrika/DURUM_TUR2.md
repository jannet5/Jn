# Tur 2 kontrol noktası (2026-10-16 23:49 – 2026-10-17 00:19 UTC)

## Yapıldı (kanıtlı)
- **APK derleme zinciri çalışıyor**: Maven 429/404 sorunu `araclar/build_retry.sh` ile aşıldı (Ç-016). Eski kodun debug APK'sı derlendi (arm64 43 MB, targetSdk 35). Yeni melodi hattı henüz içinde YOK.
- **Android 8.0 emülatör** (KVM'siz) açıldı (~3 dk) ve `adb` çalışıyor; eski APK kuruldu. Yük altında kurulum çok yavaş (CPU'yu ölçüm işleri yiyordu).
- **Model seçimi kesinleşti**: karışık şarkıda doğru perde (MIR-1K): RMVPE %92,4 · torchcrepe-tiny %82,8 (yanlış alarm %70, nota F 0,41) · FCPE %69,6 (eşlik perdesini seçiyor). Hedefe ancak RMVPE yetiyor → **RMVPE int8 (99 MB)**, ilk açılışta indirilecek.
- **Model barındırma**: `modeller/rmvpe_int8.part1..3` + `SHA256SUMS` depoda (commit 3faf117). jsDelivr 20 MB sınırında 403 verdi; `raw.githubusercontent.com` tam iniyor, SHA-256 doğrulandı. URL: `modeller/URL.txt`.
- **Nota temizleme v2** (`arastirma/ortak/nota_cikar_v2.py`, ayrıntı `arastirma/NOTA_V2.md`): yuvarlanmış nota F — Vocadito (insan notalı) **0,651 → 0,722** (hedef 0,65 ✅; ayarda kullanılmayan son 20 klipte 0,737), MIR-1K karışım **0,528 → 0,628** (hedefin 0,02 altı; referans tam sayı yarım ton etiketinden türetildiği için tavanı düşük, yalnız onset'e bakınca 0,80).
- **Düzeltme**: Tur 1'de yazdığım "MIR-1K karışım 0,665" doğru değildi (referansı kendi segmenterimle türettiğim için dairesel ölçüm). Aynı koşulda tutarlı olan sayı v1 için 0,525–0,528, v2 için 0,628.
- **Örnek melodiler (yedek kayıtlar)**: 3 eski, kamu malı/tanıdık kayıttan 30 sn'lik melodi MP3'leri (piyano, müzik kutusu, 8-bit) üretildi. Demucs-vokal ile doğrudan-karışım uyumu: 0,955 / 0,944 / 0,694 (ikisi ≥0,94). **Pop değil, yedek.** MP3'ler telif ve depo nedeniyle repoda değil (`/home/user/ornekler`).
- RMVPE int8 hızı tam parçada (yüklü makine, tek iş parçacığı): gerçek zamanın ~0,34 katı; yüksüz 2 iş parçacığıyla ~0,09. Telefonda ölçülecek.

## Yapılamadı / açık
- **Pop şarkı indirilemedi**: NewPipe arama+çözme çalışıyor (Tarkan – Şımarık, 192 sn bulundu) ama googlevideo indirme adresi 403 veriyor. Adres IPv6 `ip=` parametresine bağlı; proxy her bağlantıda farklı IPv4 çıkış IP'si veriyor → IP eşleşmiyor. Telefonun kendi IP'sinde bu sorun beklenmez (gerçek test telefonda). Bu sandbox'ta çözmek için denenmeyenler: bgutil PO-token, farklı NewPipe client'ı, pytubefix, forum çözümleri.
- **Kotlin portu başlamadı** (RMVPE ONNX + NoteTracker + ModelStore + ProcessingService bağlantısı + süre metinleri). Birkaç saatlik iş; tur 3'ün ilk işi.
- Emülatör uçtan uca test (API 26 + 34), süreç öldürme, 3–4 dk şarkı, APK <30 MB / SHA-256, kart, Drive: yapılmadı.
- Güvenlik: `keystore/upload.jks` + `keystore.properties` hâlâ depoda ve depo herkese açık (jsDelivr/raw erişiyor). Çıkarılması ve anahtarın yenilenmesi öneriliyor (kullanıcı onayı bekleniyor).

## Tur 3 sırası
1. Kotlin portu (ORT-android, mel+decode, NoteTracker v2, ModelStore, servis) → JVM testi Python çıktısıyla karşılaştır → APK (<30 MB).
2. Pop şarkı örneği için: kullanıcıdan 3 mp3 (Drive'a atarsa okurum) ya da forum çözümleriyle IP-bağlı 403'ü aş; olmazsa telefonda uygulama içinden.
3. Emülatör e2e (API 26): link/dosya → melodi → çalgı → zil → `settings get system ringtone`; süreç öldürme; 3–4 dk parça.
4. Kart (artifact), Drive, SHA-256, Chrome kimliğiyle indirme doğrulaması.
