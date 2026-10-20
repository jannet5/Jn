# Bilgisayarda (veya telefonsuz) arayüzü görmek

Amaç: telefonun elinde olmadan ekranı görmek, hatayı bulmak, düzeltmek.

## 1. En hızlısı: ekran görüntüsü hattı (bu projede hazır, çalışıyor)

- Komut: `./gradlew :app:testSideloadDebugUnitTest -Proborazzi.test.record=true`
- Çıktı: `app/build/shots/` altında PNG'ler (ana ekran kapalı/açık/uzun, resimli, menü,
  izin kartı, rehber, seçici, destek planı, boş durum, dinleme duraklamış).
- Marka konseptleri: `app/build/shots/marka/konsept-N-uygulama.png` (gerçek uygulama, her konseptle).
- Nasıl çalışır: Robolectric, Android'i JVM'de simüle eder; Roborazzi gerçek Compose arayüzünü
  çizer. Emülatör, KVM, telefon gerekmez. Gerçek yazı tipleri ve Türkçe karakterler çizilir.
- GitHub'da: her push'ta CI aynı PNG'leri "ui-screenshots" adıyla artifact olarak yükler;
  bilgisayarsız, telefon tarayıcısından da indirilebilir.
- Sınır: dokunuş akıcılığı ve gerçek WhatsApp bildirimi burada ölçülmez (emülatör/telefon gerekir).

## 2. Android Studio (bilgisayarın varsa)

- Projeyi aç, Compose `@Preview` ile bileşenleri anlık gör, Layout Inspector ile canlı uygulamayı incele.
- Emülatör (AVD) için donanım hızlandırma (Windows: HAXM/WHPX, Mac: Apple Silicon yerleşik) gerekir.

## 3. Gerçek telefonu bilgisayara yansıtmak

- scrcpy (açık kaynak): USB veya Wi-Fi ile telefon ekranını bilgisayarda gösterir ve kontrol eder.
  - `scrcpy` komutu yeter; telefonda "USB hata ayıklama" açık olmalı.
- Bu, "telefonda deneyip bilgisayardan düzeltmek" için en rahat yoldur.

## 4. Bulutta emülatör (hesap/sunucu gerekir)

- GitHub Actions emülatör matrisi (API 26/30/34/36): `.github/workflows/android-ci.yml`.
  Testleri gerçek Android sisteminde çalıştırır; log ve sonuçları artifact olarak verir.
- Tarayıcıda çalışan çevrimiçi emülatörler (ör. Appetize, BrowserStack) APK'yı kendi sunucusuna
  yükler. Gizlilik nedeniyle gerçek müşteri verisiyle ASLA kullanılmamalı; yalnızca sahte veriyle.

## 5. Waydroid / Android-x86

- Linux masaüstünde Android'i kapsayıcıda çalıştırır; KVM gerektirmez ama çekirdek modülleri gerekir.
  Bu geliştirme ortamında (bulut kapsayıcı) kurulamıyor; kendi bilgisayarında denenebilir.

## Önerilen düzen

1. Her değişiklikte ekran görüntüsü hattını çalıştır, PNG'lere bak.
2. Her push'ta CI emülatör matrisi gerçek sistemde doğrular.
3. Çıkmadan önce telefonda bir kez elle dene (scrcpy ile).
