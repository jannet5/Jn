# Çözülen sorunlar günlüğü

Her kayıt: ne oldu → asıl neden → ne yaptım → kanıt. En yeni en üstte.
Aynı hata iki kez yaşanmasın diye "yeni kural" satırı var.

## S-14: Play Billing bütün ağ izinlerini getiriyor
- Ne oldu: Play sürümünün birleşmiş manifestinde INTERNET ve ACCESS_NETWORK_STATE çıktı.
- Neden: Billing kütüphanesi Google Data Transport'u (telemetri) bağımlılık olarak çekiyor.
  İlk şüphelim "uygulama içi güncelleme" kütüphanesiydi; onu kaldırınca izin gitmedi, yani asıl
  kaynak Billing. Bağımlılık ağacı (`gradlew :app:dependencies`) ve AAR manifestleri okunarak doğrulandı.
- Çözüm: iki ayrı sürüm. sideload: Billing yok, INTERNET yok. play: Billing var, izinler var.
  Uygulamadaki gizlilik cümlesi kurulu manifesti çalışma anında okuyup doğrusunu yazıyor.
  CI sideload manifestinde INTERNET aramayı yasaklıyor.
- Yeni kural: gizlilik vaadi, ölçülmemiş hiçbir izin iddiası içeremez.

## S-13: Billing 9.1.0 Kotlin 2.0.21 ile derlenmiyor
- Neden: 9.1.0 Kotlin 2.3 metadatası ile derlenmiş. 7.1.1'de `enableAutoServiceReconnection` yok.
- Çözüm: 8.0.0 (ikisini de sağlıyor). Derleme + birleşmiş manifest ile doğrulandı.

## S-12: Ekran görüntüsü testlerinde Koin "zaten başlatıldı"
- Neden: `KoinApplication` ve `startKoin` küresel; testler arası sızıyor.
- Çözüm: ikon ve görsel yükleyicileri Koin yerine `CompositionLocal` ile veriyoruz;
  uygulama köke Koin'den, testler sahte yükleyicilerden veriyor. Daha test edilebilir.

## S-11: Room göç testi şema dosyasını bulamıyor
- Neden: Robolectric birim testinde `src/test/assets` birleştirilmiş varlıklara girmiyor.
- Çözüm: v1 veritabanını gerçek v1 SQL'iyle elle kurup Room'un kendi göç + şema doğrulamasına bıraktık.

## S-10: Lint "media/ is not in an included path"
- Neden: yedek kurallarında hariç tutmak için önce aynı alanı dahil etmek gerekiyor.
- Çözüm: `<include domain="file" path=".">` + `<exclude ... path="media/">`.

## S-09: Aynı bildirim anahtarı ile gelen sonraki mesajlar kayboluyordu (EN ÖNEMLİSİ)
- Ne oldu: WhatsApp gibi uygulamalar her sohbet için tek bildirim kullanır ve her mesajda onu günceller.
- Neden: tekrar koruması bildirim anahtarına dayanıyordu; Room'da anahtar üzerinde benzersiz indeks vardı.
  Her sohbetten yalnızca ilk mesaj kaydediliyordu.
- Çözüm: mesaj başına parmak izi (mesajın kendi zaman damgası), anahtar indeksi kaldırıldı,
  v1→v2 göç.
- Kanıt: `CaptureCoordinatorRobolectricTest` içinde "REGRESSION" testi + uçtan uca emülatör testi
  (`NotificationListenerRealDeviceTest`).
- Yeni kural: tekrar kontrolü hiçbir zaman "bildirim kimliği"ne dayanmaz, içeriğe ve zamana dayanır.

## S-08: "İzin verdim ama hâlâ soruyor"
- Neden olabileceği: tek bir izin sinyaline güvenmek. Bazı ROM'larda `getEnabledListenerPackages`
  gecikiyor.
- Çözüm: üç sinyalden biri yeterli (çerçevenin doğrudan cevabı, etkin dinleyici listesi, dinleyicinin
  şu an bağlı olması) + ekran açıkken 1,5 sn'de bir yeniden kontrol + ayardan dönünce kontrol.
- Not: gerçek cihazda doğrulama kullanıcı elinde (aşağıdaki "açık işler").

## S-07: Rakiplerin 1 numaralı şikâyeti: yakalama sessizce duruyor
- Çözüm: dinleyici kopunca kendini yeniden bağlar (`requestRebind`), ayrıca açılışta kontrol edilir;
  10 saniye bağlı değilse "Dinleme duraklamış" kartı çıkar (Yeniden başlat + pil ayarları).

## S-06: Eski nokta logolar, "?" kutuları, yazım hataları
- Çözüm: yapay nokta logolar tümüyle kaldırıldı; gerçek ikonlar telefondan gelir. Türkçe metinler
  Robolectric ekran görüntüsünde gözle doğrulandı (ğ ş ı İ ö ü ç hepsi doğru çiziliyor).

## S-05: Telefondan APK indirirken yüzde 100'de takılma
- Durum: AÇIK. Sunucu tarafında dosyalar doğrulandı (boyut, SHA-256); telefon tarafı doğrulanamadı.
- Yapılanlar: küçültülmüş ~2,7 MB APK, raw + jsDelivr bağlantıları, aynı imza anahtarı.
- Sıradaki: kullanıcı telefonunda farklı bağlantı yöntemlerini denemeli (bkz. dist/README.md).

## S-04: GitHub Release'e dosya koyma engeli (403)
- Neden: bu oturumun politikası. Çözüm: raw.githubusercontent + jsDelivr üzerinden dağıtım.

## S-03: Smoke betiğinde adb tırnak ve tee sorunu
- Çözüm: cihaz tarafı iç tırnaklar, `tee /dev/stderr` kaldırıldı (commit ac96d7a).

## S-02: Testler arası DataStore sızıntısı
- Çözüm: her testte `clearAll()`.

## S-01: Koin Robolectric'te "AlreadyStarted"
- Çözüm: testlerde `application = android.app.Application::class`.
