# Adımlar (M0 → M12) ve kabul ölçütleri

Her adımda: ne yapılır, hangi dosyalar, ve BİTTİ demek için çalıştırılacak komut.

## M0 İskelet
- Yeni Gradle projesi: AGP 8.11.1, Kotlin 2.0.21, Compose, KSP, Room, Koin, DataStore.
- İki sürüm (flavor): `sideload` ve `play`.
- Bitti: `./gradlew :app:assembleSideloadDebug` yeşil.

## M1 Veri
- Room tablosu `inbox_messages`: benzersiz parmak izi, konuşma anahtarı, durum (UNREAD/READ/HIDDEN),
  avatar/resim yolları. Bildirim anahtarında BENZERSİZ indeks YOK.
- DataStore: seçili paket listesi.
- Bitti: birim testi "aynı anahtarla gelen ikinci mesaj kaydedilir".

## M2 Saf yakalama mantığı (Android'siz)
- `MessageNormalizer` (çok mesajlı), `MessageFilterEngine`, `Fingerprint`, `DuplicateGuard`.
- Bitti: 40+ saf JVM testi yeşil (zaman damgalı parmak izi, konuşma anahtarı, filtre sırası).

## M3 Bildirim servisi
- `NotificationListenerService`, çıkarıcı, medya (avatar + resim), koordinatör.
- Bağlantı kopunca `requestRebind`; yeni uygulama seçilince kabuktaki bildirimler yeniden işlenir.
- Bitti: Robolectric uçtan uca testi (gerçek `Notification` nesneleri + gerçek Room).

## M4 Kanallar
- Telefondaki tüm uygulamalar (launcher intent), gerçek ikonlar, seçici sayfa, arama.
- Sıra: seçili → geçmişi olan → bilinen sohbet uygulamaları → A-Z (Türkçe sıralama); dokunulan
  öğe seçimi kaldırılsa bile üstte kalır.
- Bitti: `ChannelOrderingTest`.

## M5 Ana ekran
- Üstte marka + "Ekranı tertemiz yap" + ok (menü). Altında yan yana kanal balonları ("Tümü" ilk).
- Konuşma kartları: başlık tıklanınca kapalı → mesajlar → tam uzun → kapalı. Mesaja dokununca
  yalnız o mesaj uzar/kısalır (yeni sayfa yok). Eşit yükseklikte kutular. Okundu düğmesi her
  mesajın yanında. Basılı tutunca kopyala. Resme dokununca büyür.
- Bitti: Roborazzi görüntüleri gözle incelendi; `MainViewModelRobolectricTest` yeşil.

## M6 İzin ve ilk kullanım
- Zorunlu ilk adım YOK. İzin yoksa üstte tek kart: kısa neden + doğrudan doğru ayar sayfasına
  giden düğme + görsel rehber (Android 13+ "kısıtlanmış ayar" adımı dahil).
- İzin kontrolü 3 sinyal: çerçeve cevabı, etkin dinleyici listesi, dinleyici bağlı mı.
- Bitti: ekran görüntüleri `07-izin-yok`, `10-izin-rehberi`.

## M7 Dayanıklılık
- "Dinleme duraklamış" kartı, yeniden başlatma, pil ayarı bağlantısı.
- Bitti: ekran görüntüsü `11-dinleme-duraklamis`.

## M8 Marka
- Tek dosyada marka (`Brand.kt`): renk, köşe, hareket, yazı tipleri. 5 konsept üretilir, biri seçilir.
- Bitti: seçilen konseptle gerçek uygulama ekran görüntüsü.

## M9 Gizlilik ve ödeme
- Gizlilik cümlesi kurulu manifesti okuyarak yazılır.
- Play sürümü: Play Billing destek planları (1/3/6/12 ay), varsayılan hiçbir şey kilitlemez.
- Bitti: sideload manifestinde INTERNET yok (CI denetler).

## M10 Test hattı
- Birim + Robolectric + Roborazzi + emülatör matrisi (API 26/30/34/36) + `cmd notification post` ile
  gerçek sistem uçtan uca testi.
- Bitti: `.github/workflows/android-ci.yml` yeşil.

## M11 Yayın hazırlığı
- Adaptive ikon (+ tek renkli), mağaza metinleri, veri güvenliği formu, 512 ikon, 1024x500 görsel,
  ekran görüntüleri, reklam görselleri.
- İmzalı AAB (gerçek anahtar repoya GİRMEZ).

## M12 Teslim
- İmzalı APK + zip + açıklama, tam yol listesi, doğrulama özeti, açık işler dürüstçe.
