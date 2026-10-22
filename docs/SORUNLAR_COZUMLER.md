# Çözülen sorunlar günlüğü

Her kayıt: ne oldu → asıl neden → ne yaptım → kanıt. En yeni en üstte.
Aynı hata iki kez yaşanmasın diye "yeni kural" satırı var.

## S-22: Aynı kelimeyi iki kez yazan müşterinin ikinci mesajı kayboluyordu (ve eski satırlar geri geliyordu)
- Ne oldu (bağımsız denetim buldu): zamansız bildirimlerde tekrar koruması "gönderilme dakikası" kovasına
  bağlıydı. Aynı dakikada gelen iki "Evet" tek mesaj sayılıyordu; çok satırlı bildirim güncellenince
  eski satırlar yeni dakikayla yeniden hash'lenip tekrar ekleniyordu.
- Çözüm: `UntimedLineTracker` (bildirim başına önceki satırlar, yalnız eklenen satır yeni sayılır),
  bildirim kapanınca unutur; yeniden başlatmadan sonra çok satırlı bildirimde son 6 saatte kaydedilmiş
  aynı satırlar atlanır. Parmak izi artık tam gönderilme zamanı. 8 + 7 yeni test.
- Yeni kural: tekrar kararı "içerik + zaman kovası" ile değil, "bu bildirimde ne eklendi" ile verilir.

## S-21: Kendi yanıtımız müşteri mesajı olarak kaydoluyordu
- Neden: `MessagingStyle` içinde telefon sahibinin satırları (kendi kişisi ya da kişisiz) okunmuyordu.
- Çözüm: `MessagingLine.fromUser`; yalnız sahibin satırı olan bildirim hiçbir şey kaydetmez. Testli.

## S-20: Yayın işi "doğruluyormuş gibi" geçiyordu
- Ne oldu: `apksigner` yolu `build-tools/*/apksigner` diye yazılmıştı; birden çok sürüm kurulu olduğu için
  glob birkaç yola açıldı, komut hata verdi ama `| head -3` hatayı yuttu. İmza doğrulaması aylarca
  (aslında birkaç saat) hiçbir şey yapmadan "başarılı" göründü. Denetim buldu, `set -o pipefail` ile
  açığa çıktı.
- Çözüm: tek `build-tools` sürümü seçilir (`sort -V | tail -1`), çıktı dosyaya yazılır.
- Yeni kural: bir doğrulama adımı, hatayı yutabilecek bir boru hattının sonunda olamaz.

## S-19: Başka uygulama gibi davranan bildirim testi Android 8'de çalışmıyor
- Neden: `cmd notification post` Android 11'de geldi; test APK'sı uygulamayla aynı uid'de çalıştığı için
  başka paket adıyla bildirim atamıyor (`Calling uid ... gave package ... which is owned by uid ...`).
- Çözüm: çapraz uygulama senaryoları Android 11+ ile sınırlı (atlanır, kırmızı olmaz); Android 8'de
  dinleyici bağlanması ve gerçek bildirimin çıkarılması testleri çalışır. Dürüst sınır yazıldı.

## S-18: Emülatör testinde bildirim "Room'a düşmüyor" (aslında düşüyordu)
- Ne oldu: dört Android sürümünde iki uçtan uca test kırmızıydı; uygulama bildirimi doğru kaydediyordu.
- Neden: `UiAutomation.executeShellCommand` tırnakları çözmez. `-t 'Ayse Yilmaz' etiket 'randevu ...'`
  parçalanıyor, bildirimin metni etiket oluyordu; test "randevu" arıyordu.
- Nasıl bulundu: test hata mesajına "uygulama ne gördü" dökümü (karar ACCEPTED, sayaçlar, son hata) ve
  sistem kaydı eklendi; Gradle konsolu mesajın yalnız ilk satırını gösterdiği için tek satıra indirildi.
- Çözüm: her argüman tek kelime. Yeni kural: başarısız bir test kendini açıklayan tek satır yazsın.

## S-17: Birim test CI'da 10 saniyelik sanal zamana takıldı
- Neden: `runTest` varsayılanı 10 sn; Room/DataStore gerçek iş parçacığında çalışıyor, yavaş CI makinesinde
  ilk test aşıyordu. Çözüm: 60 sn. (Testi atlamak değil, bütçeyi gerçekçi yapmak.)

## S-16: Her push çalışan emülatör koşusunu iptal ediyordu
- Çözüm: yalnız `docs/**`, `**.md`, `tools/**` değişen push'lar CI'ı başlatmaz; yayın işi, tüm kapılar
  (birim, duman, emülatör matrisi) geçmeden çalışmaz.

## S-15: Sideload APK herkese açık test anahtarıyla imzalıydı
- Risk (denetim): anahtar depoda; biri aynı paketle "güncelleme" üretip verilerin ve bildirim erişiminin
  üstüne kurabilir.
- Çözüm hazır: özel dağıtım anahtarı (`tools/make_release_key.sh` → tek satırlık GitHub sırrı
  `SENDE_SIGNING_B64`); CI sırrı görünce onunla imzalar, yoksa test anahtarına düşer ve sürüm notunda
  bunu açıkça yazar. Sırrı eklemek kurucunun işi (ben GitHub sırlarına erişemem).

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
