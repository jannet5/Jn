# nokta — ortak spesifikasyon (A, B, C, D haritalarının hepsi)

## Zorunlu (TXT'den)
1. Sağ kenarda yuvarlak yüzen balon; dokununca liste doğrudan açılır.
2. Satırın sağında sil. Satıra dokununca üstü çizilir (iki ayrı davranış).
3. Tutup sürükleyerek sıralama (tutamaç + uzun basma).
4. Çok hızlı, basit, kasmayan; arayüz "AI slop" değil.
5. Marka adı, logo, renk, tipografi: yapay zekanın kararı (varsayılan ad "nokta"; harita değiştirebilir, gerekçeli).
6. Çıktı: imzalı release APK + AAB; yalnız bulutta derlenir.

## Yorum analizinden eklenenler (NOTLAR/yorum_analizi.md, 5940 gerçek yorum)
- Kazara silmeye karşı **geri al (undo)** şeridi.
- Üstü çizili öğe silinmeden kalır; **tamamlananları toplu temizle** + geri al.
- **Hızlı ekleme**: açılışta imleç hazır.
- **Koyu tema** + sistem temasına uyum; balon kenara yapışır, kolay gizlenir.
- **Yedek/dışa aktar** (metin paylaş), hesapsız, reklamsız, çevrimdışı.

## Kabul ölçütleri (her harita bunları kanıtlamak zorunda)
- Birim testleri geçer; `./gradlew testDebugUnitTest assembleRelease bundleRelease` başarılı.
- APK v2 imzalı (apksigner verify), release (minify açık).
- Kanıt dosyası: `RAPOR_<harita>.md` içinde: kullanılan araç/kaynak (gerçekten kullanılanlar), APK/AAB boyutu, yöntem sayısı, karşılaşılan sorunlar, neyin test edilip neyin edilmediği.
- Emülatörde cihaz testini ana oturum yapar (tek emülatör); agent kendi APK yolunu ve test yöntemini raporlar.

## Ortak ortam
- Android SDK: /root/android-sdk (ANDROID_HOME). JDK 21. Gradle wrapper şablonu: /home/user/Jn/nokta-app/gradle + gradlew kopyalanabilir.
- Maven 429 verirse bekleyip tekrar dene (arka planda döngü). Aynı yerde sonsuz dönme; farklı yönteme geç.
- Keystore: kendi `keystore/` klasöründe üret (git'e koyma). Şifreyi dosyalara düz yazma, rapora yazma.
- Mevcut `nokta-app/`, `android-app/`, `windows-agent/` klasörlerini DEĞİŞTİRME.
