# Tur 4 kontrol noktası (2026-10-18 12:36 – 13:06 UTC)

## Yapıldı (kanıtlı)
- **Release APK küçüldü**: yerel kütüphaneler APK içinde sıkıştırılınca (`jniLibs.useLegacyPackaging = true`) **arm64 31,6 MB → 13,4 MB**. Diğerleri: armeabi-v7a 12,5 MB, x86_64 15,3 MB, x86 15,4 MB, universal 48,2 MB. `aapt2`: targetSdk 35, sürüm 1.1.1; `apksigner`: "CN=Melodi Zil" yükleme anahtarıyla imzalı. Hedef <30 MB sağlandı. (Çıktı: `app/build/outputs/apk/release/`, depoya girmez.)
- **Uygulama Android 8.0 emülatörde çalışıyor**: kur → açılış (karşılama) → ana ekran → "Dosyadan seç" → dosya seçildi → işlem ekranı adımları görünüyor ("Opening file, Copying file, Preparing the melody model, Decoding audio, Finding the melody", Cancel). Model `adb push` ile uygulamanın harici dosya klasörüne konuldu (98,7 MB, 20 sn). Ekran görüntüleri: `docs/test/01_ana.png … 03_hala_isleniyor.png`.

## Yapılamadı / açık
- **Melodi sonucu ekranı henüz görülmedi.** 30 sn'lik kesitte işlem ekranı ~6 dk sonra hâlâ "Finding the melody" idi (KVM'siz emülatör, ORT x86_64 yazılım emülasyonunda çok yavaş olabilir; uygulama çökmedi — logcat'te görülen FATAL satırları uiautomator/sistem sürecine ait, uygulamaya değil). Sonuç ekranı, çalgı seçimi, "Zil sesi yap", `settings get system ringtone` doğrulaması, süreç öldürme, 3–4 dk parça: yapılmadı.
- Gerçek hız bilgisi için **gerçek telefon** (ya da daha hızlı x86 emülasyon) gerekiyor. Masaüstünde 5 sn'lik ses 1,9 sn'de (JVM, ORT).
- Pop şarkı örneği, kart (artifact), Drive, APK dosya eki: yok.

## Emülatör dersleri (belirti → neden → çözüm)
- `adb install` debug APK (55 MB): **9,5 dk** → TCG'de dexopt çok yavaş → test için küçük **release x86_64 APK (15 MB)** kullan.
- "System UI isn't responding" penceresi arayüz otomasyonunu bozuyor → betik "Wait" düğmesine bassın; emülatör belleği 3 GB yapılmalı (`-memory 3072`, şimdi 2 GB ve dolu).
- Emülatör dili İngilizce → Türkçe düğme adları bulunamıyor → betik İngilizce/Türkçe iki adı birden denesin ya da dil `tr-TR` yapılsın.
- Makine oturumlar arasında yeniden başlıyor → arka plan işleri ve emülatör ölüyor; uzun testleri tek tur içinde bitirilecek şekilde böl, kanıtı hemen commit'le.

## Tur 5 sırası
1. Emülatör: release x86_64 APK, 3 GB RAM, "Wait" işleme; 30 sn kesitle sonuç ekranına kadar → çalgı → zil → ringtone doğrula. Çok yavaşsa ORT iş parçacığı/pencere süresi ayarını gözden geçir; asıl doğrulama telefonda.
2. Süreç öldürme (`am kill`, `kill -9`) → "yarıda kaldı" ekranı.
3. APK dosya eki (arm64 release 13,4 MB) + SHA-256 + kart + Drive.
