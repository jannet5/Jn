# TekPanel mimarisi: önceki hâl → değişiklik → son hâl → nasıl uygulanır

Bu belge telefonda okunsun diye geniş tablo içermez; her şey madde madde yazıldı.

## 1. Ürün tek cümlede

Seçtiğin uygulamaların bildirimlerinden gelen müşteri mesajlarını, telefondan dışarı hiçbir
şey çıkarmadan, tek bir sade ekranda toplar.

## 2. Önceki hâl (bu turun başında)

- Kanallar kapalı bir katalogdu: 12 sabit uygulama, her birinin elle çizilmiş nokta logosu.
- Bir bildirim = bir mesajdı. Sadece son satır alınıyordu.
- Tekrar koruması bildirim anahtarına (`notificationKey`) dayanıyordu.
- Kartlar düz bir liste; kişiye göre gruplama yoktu.
- Profil resmi, fotoğraf, yanıt, "uygulamada aç" yoktu.
- Arayüz tek bir "Menü" düğmesinin arkasında kanal listesi + dışa aktar + sil karışıktı.

### Bulunan gerçek hata (en önemlisi)

- WhatsApp gibi uygulamalar her sohbet için TEK bildirim kullanır ve her yeni mesajda aynı
  bildirimi günceller; anahtar aynı kalır.
- Eski kod anahtarı "tekrar" sayıyordu. Sonuç: her sohbetten yalnızca İLK mesaj kaydediliyordu.
- Düzeltme: tekrar koruması artık mesaj başına, mesajın kendi zaman damgasıyla
  (parmak izi). Anahtar üzerindeki benzersiz indeks kaldırıldı. Regresyon testi var
  (`CaptureCoordinatorRobolectricTest`).

## 3. Değişenler

- Kanal = telefondaki herhangi bir uygulama.
  - Liste, manifest'teki `<queries><intent>` (MAIN/LAUNCHER) ile okunur.
  - `QUERY_ALL_PACKAGES` yok; Play'de özel izin beyanı gerekmez.
  - İkon ve ad paket yöneticisinden gelir (gerçek ikon, paket adı hiçbir yerde görünmez).
- Yakalama zinciri:
  - `NotificationExtractor` → `MessageNormalizer` (çok mesajlı) → `MessageFilterEngine` →
    parmak izi + `DuplicateGuard` + Room benzersiz indeks → medya → kayıt.
  - MessagingStyle'ın tüm satırları alınır; düz bildirimde bigText/text'in uzun olanı seçilir
    (metin kesik kalmaz).
- Konuşma anahtarı: paket + normalize edilmiş sohbet adı. WhatsApp ile WhatsApp Business
  ayrı konuşmadır.
- Medya: profil resmi ve fotoğraf `files/media` altına içerik adresli (SHA-256) kaydedilir,
  yedekten hariçtir.
- Canlı eylemler (yanıt, tam sohbete git): yalnız bellekte tutulur (PendingIntent
  saklanamaz). Süreç ölürse "uygulamada aç"a düşer; arayüz fazlasını vaat etmez.
- Room v1 → v2 göçü: tablo yeniden kurulur, eski mesajlar korunur.
- Dinleyici sağlığı: bağlantı kopunca kendini yeniden bağlar; bağlı değilse "Dinleme
  duraklamış" uyarısı çıkar (rakiplerin 1 numaralı şikâyeti: yakalama sessizce duruyor).
- Arayüz: tek ekran. Menü ok işaretinin arkasında; "Kanalları ayarla" yalnız seçiciyi açar.
- Marka tek dosyada (`ui/theme/Brand.kt`); renk, köşe, hareket oradan gelir.

## 4. Son hâl (katmanlar)

- `capture/` (Android'e en yakın katman)
  - `TekPanelNotificationListenerService`: sistemin verdiği bildirimi alır, seçilen uygulama
    eklendiğinde kabuktaki mevcut bildirimleri tekrar işler.
  - `NotificationExtractor`, `AndroidNotificationMedia`: çerçeve nesnesi → düz veri.
  - `MessageNormalizer`, `MessageFilterEngine`, `PromotionalHeuristics`, `Fingerprint`,
    `DuplicateGuard`: saf Kotlin, JVM'de test edilir.
  - `CaptureCoordinator`: sırayı yönetir. `LiveActionRegistry`: yanıt/aç eylemleri.
- `data/`
  - `local/` Room (tek tablo, benzersiz parmak izi), `prefs/` DataStore (seçili paketler),
    `apps/` uygulama envanteri + ikon yükleyici, `media/` dosya deposu + çözücü,
    `repository/` Inbox ve Channel depoları.
- `domain/`: modeller (`InboxMessage`, `Conversation`, `Channel`) ve saf sıralama kuralları
  (`ChannelOrdering`).
- `ui/`
  - `main/`: `MainViewModel`, `MainContent` (saf düzen), kart, balon çubuğu, üst çubuk, görüntüleyici.
  - `channels/`: seçici sayfa. `access/`: izin kartı ve görsel rehber. `theme/`: marka.
- `util/`: izin kontrolü (3 sinyal), ayar sayfalarına gitme, dinleyici sağlığı, intent başlatıcı.

Veri akışı (tek yön):
1. Android bildirimi verir → servis → koordinatör → Room.
2. Room akışı → depo → `MainViewModel` → `MainContent` (saf işlev).
3. Dokunuş → ViewModel → depo → Room (döngü kapanır; ekran kendiliğinden güncellenir).

## 5. Gizlilik sözleşmesi (kodda zorlanan)

- Manifest'te INTERNET izni yok; CI birleşmiş manifest'te bunu denetler.
- Mesaj veritabanı ve medya dosyaları bulut yedeğinden ve cihaz aktarımından hariç.
- Teşhis kaydı mesaj içeriği tutmaz (yalnızca sayaçlar).
- Dışa aktarma yalnızca kullanıcı paylaşım sayfasından seçerse dışarı çıkar.

## 6. Nasıl uygulanır (yeni birine tarif)

1. `./gradlew :app:testSideloadDebugUnitTest` → 70+ birim ve Robolectric testi, ekran görüntüleri
   `app/build/shots/` altına çıkar (`-Proborazzi.test.record=true`).
2. `./gradlew :app:assemblePreview` → küçültülmüş, sabit test anahtarıyla imzalı APK.
3. GitHub Actions: emülatör matrisi (API 26/30/34/36) gerçek bildirim → Room uçtan uca testini
   çalıştırır.
4. Marka değiştirmek: yalnız `ui/theme/Brand.kt` ve yazı tipi dosyaları.

## 7. Bilinen sınırlar (dürüst liste)

- Bir uygulamanın fotoğrafı yalnızca o uygulama bildirimde sunarsa ve Android erişim izni
  verirse görünür; verilmezse mesaj fotoğrafsız gösterilir.
- Yanıtlama yalnızca bildirimin yanıt eylemi canlıyken çalışır.
- Gerçek WhatsApp/Instagram ile gerçek telefonda canlı doğrulama kullanıcının elinde.
