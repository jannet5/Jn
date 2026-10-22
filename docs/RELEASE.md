# Sende — Yayın belgesi

Bu belge "nasıl derlenir, nasıl imzalanır, Play'e ne yazılır, kim ne yapar" sorularının güncel
cevabıdır. Ayrıntılı araştırma: `docs/arastirma/09-google-play-yayin-rehberi-turkiye.md`
(hesap, kapalı test, politika, ret riski). Eski sürümlerin tablosu ve dosya eşlemesi kaldırıldı;
kodun güncel yapısı için `docs/MIMARI.md`.

## 1. İki sürüm, üç komut

- `sideload`: telefona doğrudan kurulan APK. İnternet izni YOK, ödeme kodu yok.
- `play`: Google Play sürümü. Play Billing (destek planları) var. Billing kütüphanesi
  kendiliğinden INTERNET izni getirir (ölçüldü, `docs/SORUNLAR_COZUMLER.md` S-14). Uygulama
  kodu mesaj verisini ağa göndermez; bu fark gizlilik metninde açıkça yazılı (§4).

Komutlar (kökten):
```
./gradlew :app:testSideloadDebugUnitTest                 # birim + ekran görüntüsü testleri
./gradlew :app:assembleSideloadPreview                   # telefona kurulacak küçültülmüş APK
./gradlew :app:bundlePlayRelease -PversionCode=12        # Play'e yüklenecek AAB
```
- APK çıktısı: `app/build/outputs/apk/sideload/preview/app-sideload-preview.apk`
  (paket adı `com.sende.app.preview`).
- AAB çıktısı: `app/build/outputs/bundle/playRelease/app-play-release.aab`.
- `versionCode`: CI'da koşu numarasıdır (her APK öncekinin güncellemesi olur). Play yüklemesinde
  her seferinde artan bir sayıyı elle ver (`-PversionCode=N`). `versionName` şimdilik `0.1.0`.
- Ekranların PNG'leri `app/build/shots/` altına düşer (`-Proborazzi.test.record=true`).

## 2. Otomatik kontroller (her push, `.github/workflows/android-ci.yml`)

- Birim + Robolectric + ekran görüntüsü testleri (130'dan fazla test).
- Sideload manifestinde INTERNET izni olmadığının denetimi; "kullanıcının kendi bildirimini
  silme/erteleme kodu yok" denetimi.
- Play sürümünün R8'li release derlemesi (kural eksikliği burada patlar).
- Gerçek emülatörde (Android 8, 11, 14, 16): dinleyici gerçekten bağlanıyor mu, başka uygulamadan
  gelen bildirim Room'a tam bir kez düşüyor mu, aynı anahtarla gelen yeni mesaj kaybolmuyor mu.
  (Android 8'de çapraz uygulama senaryoları atlanır: `cmd notification post` Android 11'de gelir.)
- Küçültülmüş APK'nın emülatörde kurulup açılması (`.github/scripts/preview-smoke.sh`).
- Hepsi geçerse `son-surum` yayını güncellenir:
  `https://github.com/jannet5/Jn/releases/download/son-surum/Sende.apk`.

## 3. Üç ayrı imza anahtarı (karıştırma)

1. **Herkese açık TEST anahtarı** (`app/sideload-test.jks`, şifre `sende-test`, depoda). Yalnız
   debug ve emülatör testleri içindir. Bununla imzalı APK'yı herkes "güncelleme" diye üretebilir;
   kimseye güven vermez.
2. **Dağıtım anahtarı** (özel). Başkalarına verdiğin APK'yı imzalar. Bir kerelik kurulum:
   `bash tools/make_release_key.sh` çıktısındaki tek satırı GitHub'a `SENDE_SIGNING_B64` adlı
   depo sırrı olarak ekle (Settings > Secrets and variables > Actions). CI bunu görünce APK'yı
   onunla imzalar ve sürüm notuna parmak izini yazar. Anahtar dosyalarını güvenli yerde yedekle:
   kaybedersen kurulu kopyalar güncellenemez. Anahtarı ilk gerçek kullanıcıdan ÖNCE kur;
   sonradan değişirse herkes uygulamayı silip yeniden kurmak zorunda kalır.
3. **Play yükleme anahtarı** (özel, Play App Signing ile). `release/keystore.properties`:
   ```
   storeFile=release/sende-upload.jks
   storePassword=...
   keyAlias=sende
   keyPassword=...
   ```
   (`storeFile` depo köküne göre yazılır; `release/` git'e girmez.) Anahtarı üret:
   ```
   keytool -genkeypair -v -keystore release/sende-upload.jks -alias sende \
     -keyalg RSA -keysize 4096 -validity 10000
   ```
   Dosya yokken `assemblePlayRelease` imzasız çıkar (derlenir ama kurulmaz).

## 4. Gizlilik politikası (taslak; yayından önce hukuki gözden geçirme)

Barındırma: herkese açık bir URL'de, PDF değil (GitHub Pages yeterli). Mağazada VE uygulama
içinde bağlantı olmalı.

**Türkçe**
> Sende, yalnızca senin seçtiğin uygulamaların bildirimlerini, Android'in "Bildirim erişimi" izniyle
> okur; izni ayarlardan istediğin an kapatabilirsin. Bildirimdeki başlığı, mesaj metnini, gönderen
> adını, varsa profil fotoğrafını ve gelen resmi, kaynak uygulamayı ve zamanı YALNIZCA telefonunda,
> yerel bir veritabanında ve uygulamanın kendi dosyalarında saklar. Sende'nin sunucusu yoktur,
> mesaj verini ağa göndermez; reklam veya analiz yazılımı içermez. Bu veriler Android'in bulut
> yedeğine girmez. Kanal listesini göstermek için telefondaki uygulamaların adlarını ve simgelerini
> okur (bu liste de cihazdan çıkmaz). Verini dışa aktarabilir veya uygulama içinden tamamen
> silebilirsin; uygulamayı kaldırınca hepsi silinir. Sende kaynak uygulamalardaki geçmiş mesajlara
> erişmez, hiçbir hesabına giriş yapmaz, bildirimleri silmez ve ertelemez.
> Google Play sürümünde, destek planları için Google Play Faturalandırma kullanılır; bu bağlantıyı
> Google yönetir ve mesaj verin ona verilmez.

**English**
> Sende reads notifications only from apps you choose, through Android's Notification Access
> permission, which you can turn off at any time. It stores the visible title, message text, sender
> name, the sender's profile picture and any picture in the notification, the source app and the
> time ONLY on your phone, in a local database and the app's own files. Sende has no server, does
> not send your message data over the network and contains no advertising or analytics code. This
> data is excluded from Android's cloud backup. To show your channel list it reads the names and
> icons of the apps on your phone (that list never leaves the device either). You can export or
> delete everything inside the app; uninstalling removes it all. Sende does not read message
> history inside other apps, never logs in to your accounts, and never dismisses or snoozes your
> notifications. In the Google Play version, supporter plans use Google Play Billing; Google
> operates that connection and your message data is never passed to it.

## 5. Play "Veri güvenliği" formu (Console'da asıl kaynak Google'ın tanımıdır)

Google'ın tanımında "toplamak" verinin cihazdan dışarı iletilmesidir; yalnız cihazda işlenen
veri beyan edilmez. Bu yüzden önerilen cevaplar:

- Veri toplanıyor mu: HAYIR (mesajlar, kişi adları, fotoğraflar yalnız cihazda).
- Veri paylaşılıyor mu: HAYIR.
- Güvenlik: veri iletilmediği için "şifreli iletim" uygulanamaz; kullanıcı verisini silme yolu
  uygulama içinde (Menü > Tüm yerel veriyi sil).
- Billing kütüphanesinin kendi veri akışı için Console'daki soruları Google'ın yönergesine göre
  işaretle (satın alma bilgisi); emin olmadığında Play desteğine sor. (İlk taslakta "Mesajlar
  toplanıyor: Evet" yazıyordu; yanlıştı ve mağazada gereksiz uyarı çıkarırdı.)
- "İnternet izni yok" cümlesi YALNIZCA sideload APK için doğrudur; Play mağaza metnine yazma.

## 6. Mağaza metni (taslak; ayrıntı ve karakter ölçümleri: araştırma 09 §10)

- Başlık: `Sende: Müşteri Mesajları` (24). Başlıkta WhatsApp/Instagram adı geçmez (marka kuralı).
- Kısa açıklama (≤80): `Müşteri bildirimlerin tek sade ekranda. Verilerin telefonunda kalır.`
- Uzun açıklama: önceki taslaktaki madde işaretleri geçerli (kanal seçimi, tam mesaj, okundu,
  dışa aktar/sil, sunucu yok); en alta "WhatsApp, Instagram ve diğer adlar ilgili sahiplerine
  aittir; Sende bunlarla bağlantılı değildir" notu. "Silinen mesajı gör" gibi vaatler YOK.
- Ekran görüntüleri: uydurma müşteri adlarıyla, uygulama logoları öne çıkmadan
  (`app/build/shots/` altındakilerden üretilir; çerçeveli sürümler marka çalışmasında).

## 7. Senin yapacakların (ajan yapamaz)

Ayrıntılı kontrol listesi: `docs/arastirma/09-google-play-yayin-rehberi-turkiye.md` sonu.
Kritik yol, kısaca:

1. Mali müşavire hesap türünü sor (kişisel mi, şirket/şahıs şirketi mi); para kazanınca kişisel
   hesapta tam adresin mağazada görünür.
2. Yalnız bu iş için yeni bir Google hesabı + Play Console kaydı (25 USD, bir kez).
3. Kimlik ve cihaz doğrulaması.
4. Yeni kişisel hesapta: en az 12 test kullanıcısı, 14 gün kesintisiz kapalı test, sonra
   "üretim erişimi" başvurusu (≈4 hafta). Gerçek esnaflardan 15–20 kişi bul.
5. Gizlilik politikasını bir URL'ye koy; Veri güvenliği ve içerik derecelendirme formlarını §5'e
   göre doldur.
6. §3'teki 2. ve 3. anahtarı üret, yedekle, AAB'yi yükle.
7. Marka/ad taraması: Play'de "Sende" adlı başka bir uygulama var (`docs/MARKA_ADI_KONTROL.md`);
   TÜRKPATENT/WIPO taramasını yaptır.

## 8. Yayın öncesi son kontrol

- [ ] CI'ın tamamı yeşil, `son-surum` APK'sı telefonda kurulup denendi.
- [ ] Gerçek WhatsApp / Instagram bildirimiyle uçtan uca deneme (yalnız gerçek telefonda yapılabilir).
- [ ] Sideload manifestinde INTERNET yok; Play manifestindeki ağ izinleri gizlilik metniyle uyumlu.
- [ ] Birleşmiş manifestlerde eski "tekpanel" kalıntısı yok (`./gradlew clean` sonrası bak).
- [ ] Gizlilik politikası URL'si canlı; uygulamadan bağlantı çalışıyor.
- [ ] Kapalı test: 12+ kişi, 14 gün, geri bildirim kaydı tutuldu.
