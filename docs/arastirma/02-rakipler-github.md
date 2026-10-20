# GitHub rakipleri: açık kaynak bildirim uygulamaları

Araştırma kolu: github-rakipler

Tarih: 2026-10-20. Hazırlayan: araştırma ajanı (github-rakipler kolu).

## Bu dosya nasıl okunur

- Her bölüm kısa maddelerden oluşur. Tablo yok.
- Uzun linkler kendi satırında durur; yana kaydırmayı en aza indirir.
- "[çoklu]" işareti: bulgu birden fazla bağımsız kaynakta çıktı.
- "[tek kaynak]" işareti: bulgu yalnızca bir yerden geldi.
- "[doğrulanamadı]" işareti: kanıt bulamadık ya da kaynağa ulaşamadık.
- Kod linkleri belirli bir commit'e bağlıdır (7 haneli kısa kod).
  Böylece repo değişse bile gördüğümüz satırı bulabilirsin.
- Hiçbir kod kopyalanmadı. Amaç sistemi anlamak ve fikir çıkarmak.

## Özet (önce bunu oku)

- 26 proje bulundu, klonlandı ve incelendi (25 GitHub, 1 Codeberg).
  Ek olarak ~15 proje bulundu ama klonlanmadı (bkz. "Klonlanmayanlar").
- 13'ü derin okundu (dinleyici, depolama, dedup, canlı tutma dosyaları satır satır).
  Geri kalanı kısmen veya yüzeysel okundu. Her kartta "İnceleme" satırı var.
- En önemli bulgu 1: bildirim dinleyicisi en çok üretici pil yönetimi yüzünden
  sessizce duruyor. Bu, en çok tekrar eden şikayet. [çoklu]
- En önemli bulgu 2: Android, bildirim metnini 1024 karaktere kesiyor
  (Android 12 ve sonrası, Android 11'in geç sürümlerinden itibaren).
  Kurucunun "mesaj yarım kalıyor" şikayetinin bir kısmı platform sınırıdır.
  Bunu uygulama düzeltemez; kullanıcıya dürüstçe göstermek gerekir.
- En önemli bulgu 3: açık kaynak rakiplerin hiçbiri küçük işletme iş akışına
  (müşteri, cevap bekliyor, randevu/sipariş) odaklanmıyor. Hepsi "kayıt tutucu",
  "filtre" veya "yönlendirici". Bu bizim boşluğumuz.
- En önemli bulgu 4: 13 proje copyleft lisanslı (GPL/AGPL). Kodlarını ticari,
  kapalı kaynak TekPanel'e alamayız. Sadece fikir alırız.
- TekPanel'in yakalama hattı (parmak izi, çift kayıt koruması, MessagingStyle,
  canlı cevap) rakiplerin çoğundan olgun. Eksik olanlar: dinleyici sağlığı,
  üretici rehberi, kaldırma nedeni (REASON), 1024 sınırı bildirimi.

## Yöntem

- Aday bulma: GitHub konu sayfaları (notification-listener, notification-history,
  notificationlistenerservice, notification-logger), F-Droid "Notification"
  kategorisi, web aramaları.
- Klonlama: anonim `git clone --depth 1`, kimlik/token yok. 26 klon başarılı.
- İnceleme: dinleyici sınıfı, DB şeması, çift kayıt, MessagingStyle, görsel
  saklama, canlı tutma, OEM çözümleri, test, lisans dosyası, README, açık issue'lar.
- Yıldız/issue sayıları GitHub sayfalarından WebFetch ile okundu (2026-10-20).
  Sayfa özetleyici modelden geçtiği için küçük sapma olabilir.
  GitHub sayfasına doğrudan curl proxy tarafından 403 verdi (bkz. "Denenen ve
  çalışmayan yollar").
- Platform davranışı için birincil kaynak olarak AOSP kaynak kodu okundu
  (android.googlesource.com). Resmi doküman sayfaları çok büyük olduğu için
  özetlenemedi.
- Reddit ve Stack Overflow arama aracı tarafından engelli. Onların yerine GitHub
  issue'ları, F-Droid issue'su, XDA, dontkillmyapp.com, OneSignal blogu kullanıldı.

## Platform gerçekleri (birincil kaynak)

Bunlar rakiplerin kodundan değil, Android'in kendi kaynak kodundan çıktı.

- Dinleyiciyi bağlamak sistemin işi. Servis `onListenerConnected()` gelmeden
  işlem yapmamalı. `requestRebind()` bağlantı öncesi ve kopma sonrası çağrılması
  güvenli tek metot.
  https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/service/notification/NotificationListenerService.java
- Düşük RAM'li cihazlarda Android Q ve altında dinleyici bağlanamaz.
  İş profilindeki dinleyiciler yok sayılır. (Aynı dosya, sınıf yorumu.)
  Bu, voicenotify #174 ve notification-vault #26 şikayetlerini açıklıyor. [çoklu]
- Kaldırma nedeni kodları (REASON_CLICK=1, REASON_CANCEL=2, REASON_CANCEL_ALL=3,
  REASON_APP_CANCEL=8, REASON_TIMEOUT=19 ...) `onNotificationRemoved` ile gelir.
  (Aynı dosya.) Bildirimin tıklanarak mı, kaydırılarak mı, uygulama tarafından mı
  kaldırıldığını ayırt etmeye yarar.
- Manifest meta-data ile dinleyicinin görmek istediği bildirim türleri
  seçilebiliyor (conversations, alerting, silent, ongoing).
  Bu, AOSP `main` dalında var; hangi Android sürümünde kullanılabilir
  olduğunu doğrulayamadık. [doğrulanamadı: API seviyesi]
  (Aynı dosya, `META_DATA_DEFAULT_FILTER_TYPES` ve `FLAG_FILTER_TYPE_*`.)
- Metin sınırı: `Notification.Builder` başlık, metin, büyük metin ve
  MessagingStyle mesaj metnini `MAX_CHARSEQUENCE_LENGTH` ile keser.
  - android-8.0.0_r1: 5 * 1024 (5120 karakter)
  - android-10.0.0_r1: 5 * 1024
  - android-11.0.0_r1: 5 * 1024
  - android-11.0.0_r48: 1024
  - android-12.0.0_r1, android-14.0.0_r1, main: 1024
  Android 13 etiketine bakılmadı. Bu sınır framework Builder yolunu kullanan
  uygulamalar için geçerli. Bir uygulama extras'a doğrudan yazarsa farklı
  olabilir; WhatsApp'ın kendi davranışı [doğrulanamadı].
  https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/app/Notification.java
  https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-11.0.0_r1/core/java/android/app/Notification.java
  https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-11.0.0_r48/core/java/android/app/Notification.java
- Android 15: tek kullanımlık şifre (OTP) içeren bildirimlerin içeriği,
  "güvenilir olmayan" dinleyicilerden gizleniyor. Güvenilirler arasında
  yalnızca eşlik cihazı (companion) gibi istisnalar sayılıyor.
  https://developer.android.com/about/versions/15/behavior-changes-all
- Android 13+: mağaza dışından (APK ile) kurulan uygulamalarda bildirim erişimi
  anahtarı gri olabilir. Kullanıcı Uygulama bilgisi > ⋮ > "Kısıtlanmış ayarlara
  izin ver" yolunu izlemeli. Play'den kurulanlar etkilenmiyor (F-Droid
  issue'sunda bildirilen gözlem). [çoklu: F-Droid issue + notisave kodu]
  https://gitlab.com/fdroid/fdroidclient/-/issues/2680
- Pil muafiyeti: Google Play, doğrudan "pil optimizasyonundan muaf tut"
  isteğini yalnızca çekirdek işlevi Doze/Standby yüzünden bozulan uygulamalara
  izin veriyor. Mesajlaşma uygulamaları için FCM kullanılabiliyorsa kabul
  edilmiyor. TekPanel'in bu kategoriye girip girmediği [doğrulanamadı].
  Genel ayar listesine yönlendirme (ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
  özel izin istemiyor.
  https://developer.android.com/training/monitoring-device-state/doze-standby

## Tekrar eden şikayetler (açık/kapalı issue'lardan)

Not: issue gövdeleri yerine çoğunlukla başlıklar okundu. Gövdesi okunanlar
ayrıca belirtildi.

- Dinleyici hiç başlamıyor ya da sessizce duruyor (üretici kısıtı). [çoklu]
  - NotiFilter #124: HyperOS 3'te "Arka planda otomatik başlatma" izni açılmadan
    dinleyici hiç başlamıyor. Gövde okundu.
    https://github.com/BURG3R5/NotiFilter/issues/124
  - NotiFilter #93: Libero 5G II'de sistem dinleyiciyi başlatmıyor, 19 yorum,
    çözümsüz kapandı. Gövde okundu.
    https://github.com/BURG3R5/NotiFilter/issues/93
  - DoNotNotify #8: HyperOS 3.0.8'de kurallar hiç tetiklenmiyor. Çözüm:
    bildirim erişimi + otomatik başlatma + pil "kısıtlama yok" + son uygulamalarda
    kilitleme. Gövde okundu.
    https://github.com/anujja/DoNotNotify/issues/8
  - DoNotNotify #13: MIUI'de bazı bildirimler görülmüyor; güncellemeyle
    periyodik kontrol ve üreticiye özel izin istemi eklendi. Gövde okundu.
    https://github.com/anujja/DoNotNotify/issues/13
  - SmsForwarder #727, #760, #774, #772: OnePlus'ta son uygulamalarda kilitlenmezse
    bağlantı düşüyor (#727); Xiaomi'de gözetimsiz çalışma isteği (#760);
    HyperOS 3'te iletim durdu (#774); HyperOS 4 beta'da kanal durdu (#772).
    Yalnızca başlıklar okundu.
    https://github.com/pppscn/SmsForwarder/issues
  - SmartIsland: 31 açık issue'nun 7'si "oem-specific" etiketli. Başlıklar okundu.
    https://github.com/agupta07505/SmartIsland/issues
  - Dış kaynaklar: dontkillmyapp.com (Xiaomi, Samsung, Huawei, OnePlus 5/5 puan),
    OneSignal blogu, Automate kullanıcı grubu.
    https://dontkillmyapp.com/
    https://onesignal.com/blog/manufacturers-interfere-with-reliable-notifications/
    https://groups.google.com/g/automate-user/c/45jnWpBy4k8
  - Not: dontkillmyapp sayfaları bildirim dinleyicisinden özel olarak söz
    etmiyor; genel arka plan öldürmeyi anlatıyor. [doğrulanamadı: dinleyiciye özel]
- Çift veya tekrar kayıt. [çoklu]
  - WhatsDeleted #11 "Don't write duplicated log entries" (6 yorum).
    https://github.com/4nubhav/WhatsDeleted/issues
  - SpeakThat #92 (gruplanınca tekrar) ve #158 (dedup çözümleri).
    https://github.com/mitchib1440/SpeakThat/issues
  - SmsForwarder #718: kısayol bildirimi mesajı tekrarlıyor.
    https://github.com/pppscn/SmsForwarder/issues
- İş profili ve ikinci kullanıcı. [çoklu]
  - voicenotify #174: iş profili uygulamalarında kara liste çalışmıyor.
    https://github.com/pilot51/voicenotify/issues
  - notification-vault #26: iş profilinde WhatsApp görselleri yalnızca metin
    olarak kaydediliyor.
    https://github.com/snap24/notification-vault/issues
- Kural/filtre çalışmıyor: notification-vault #20, #25; DoNotNotify #17, #20.
- Medya ve depolama (Android 11+): WhatsDeleted #29, #35, #37.
- Toplu işlem sessizce başarısız: notification-vault #28 (çok bildirimde
  "Clear All" hata vermeden çalışmıyor). [tek kaynak]
- Uzun süre askıdayken CPU/IO ve debug log aşınması: voicenotify #166, #168.
  [tek kaynak]
- Mağazada kısıtlı erişim: Alfio010 #25 ("bazı ülkelerde Play'de yok mu?").
  https://github.com/Alfio010/notification-listener-android/issues
- WhatsApp Business isteği: Watomatic #404 (4 yorum). Messenger sorunları
  #446, #392, #367.
  https://github.com/adeekshith/watomatic/issues
- İzin kendiliğinden kapanıyor: SpeakThat #160 (erişilebilirlik izni).
  Service-Keeper'ın varlığı da aynı riski gösteriyor (bazı cihazlar uygulamayı
  `enabled_notification_listeners` listesinden siliyor). [çoklu, dolaylı]

## Proje kartları

Sıralama: TekPanel'e benzerlik ve öğrenme değerine göre. Her kartta:
bağlantı, lisans, yıldız, son commit, test, arayüz, inceleme derinliği,
mimari not, öğrendiğimiz 3 şey, yapmayacağımız 1 şey.

Yıldız sayıları 2026-10-20 tarihli GitHub sayfa okumasıdır.
"Test" sayısı test klasörlerindeki dosya sayısıdır (test fonksiyonu sayısı değil).

### 1. notisave (Echo Inbox)

- Bağlantı:
  https://github.com/abhinavsha077/notisave
- Lisans: Apache-2.0. Yıldız: 0. Son commit: 2026-10-11 (3b1865c). 4 commit.
- Test: 9 dosya + ayrı "fixture" uygulaması + CI. Arayüz: Compose, Material 3.
- İnceleme: derin.
- Dikkat: tek yazar, 0 yıldız. Kendi BUILD_REPORT.md'si "gerçek cihaz
  sertifikasyonu hâlâ yayın şartı" diyor; yani gerçek telefonlarda sınanmamış.
  https://github.com/abhinavsha077/notisave/blob/3b1865c/BUILD_REPORT.md
- Mimari not: dinleyici -> sınırlı tek yazarlı kuyruk (4096) -> işleyici ->
  Room (uygulama, konuşma, bildirim anlık görüntüsü, mesaj olayı, FTS4 arama,
  capture_health). Şifreleme: SQLCipher + Keystore ile sarılmış parola.
  https://github.com/abhinavsha077/notisave/blob/3b1865c/app/src/main/java/com/echo/inbox/data/local/Entities.kt
- Öğrendiğimiz 3 şey:
  - Her güncellemeyi değişmez "anlık görüntü" saklayıp mesajları sıralı LCS
    farkıyla çıkarıyor. Böylece iki ayrı "OK" iki olay kalıyor; kayan pencere
    ve "tarihçe mesajı" geçişi tekrar üretmiyor.
    https://github.com/abhinavsha077/notisave/blob/3b1865c/app/src/main/java/com/echo/inbox/core/util/OrderedMessageDiff.kt
  - Android aynı bildirim anahtarını yeniden kullanır. Yaşam döngüsü sınırı
    olarak "anahtar + ilk postTime" kullanıyor.
    https://github.com/abhinavsha077/notisave/blob/3b1865c/app/src/main/java/com/echo/inbox/core/util/NotificationLifecycle.kt
  - Sağlık ekranı: dinleyici bağlı mı, son geri çağrı, kuyruk, hata sayıları,
    yerel test bildirimi, rebind düğmesi, Android 13 "kısıtlı ayar" tespiti
    (yükleyen mağazayı bir izin listesiyle kontrol ederek) ve ayar sayfasına
    derin bağlantı.
    https://github.com/abhinavsha077/notisave/blob/3b1865c/app/src/main/java/com/echo/inbox/platform/NotificationAccessManager.kt
    https://github.com/abhinavsha077/notisave/blob/3b1865c/docs/OEM_GUIDANCE.md
- Yapmayacağımız 1 şey: gerçek cihazda doğrulanmamış güvenlik/doğruluk
  iddialarını (SQLCipher + HMAC + LCS) olduğu gibi benimsemek. Önce telefonda
  sınarız; ilk sürümde bu kadar mühendislik yapmayız.
  Not: görsel kaydında yeniden boyutlandırma yok (JPEG 85, ham bitmap):
  https://github.com/abhinavsha077/notisave/blob/3b1865c/app/src/main/java/com/echo/inbox/platform/MediaStorageManager.kt

### 2. pingbox

- Bağlantı:
  https://github.com/kushkumarkashyap7280/pingbox
- Lisans: GPL-3.0. Yıldız: 2. Son commit: 2026-10-19 (13f4d80). 17 commit.
- Test: 16 dosya + CI. Arayüz: Compose, Material 3. DB: SQLiteOpenHelper (v6).
- İnceleme: derin. Çok iyi yazılmış mimari belgesi var.
  https://github.com/kushkumarkashyap7280/pingbox/blob/13f4d80/docs/ARCHITECTURE.md
- Mimari not: dinleyici -> NotificationStore (tek tablo) -> ViewModel -> UI.
  Tek yönlü veri akışı. İki ürün çeşidi: GitHub (INTERNET + kendini güncelleme)
  ve offline (izinsiz).
- Öğrendiğimiz 3 şey:
  - Sohbet bildirimlerinde her mesaj ayrı satır; anahtar = bildirim anahtarı +
    mesajın kendi zaman damgası. "Bu mesaj silindi" yer tutucusu gelirse
    "kesin silindi", mesaj bildirimden kaybolursa "muhtemelen silindi".
    Yer tutucu listesi yalnızca İngilizce; kod "çevirileri buraya ekle" diyor.
    https://github.com/kushkumarkashyap7280/pingbox/blob/13f4d80/app/src/main/java/io/github/kushkumarkashyap7280/pingbox/service/MessageCapture.kt
  - `onNotificationRemoved` nedenini kaydediyor: REASON_CLICK = açıldı,
    CANCEL/CANCEL_ALL = okumadan temizlendi. "Okumadan temizlenenler" listesi.
    https://github.com/kushkumarkashyap7280/pingbox/blob/13f4d80/app/src/main/java/io/github/kushkumarkashyap7280/pingbox/service/NotificationCaptureService.kt
  - PendingIntent kalıcı saklanamaz: bellek LRU'da tutulur, yoksa uygulama
    açılır. Hatırlatma alarmları yeniden başlatmada `onListenerConnected`
    içinde tekrar kurulur; RECEIVE_BOOT_COMPLETED gerekmez.
    (Aynı ARCHITECTURE.md.)
- Yapmayacağımız 1 şey: Play sürümüne "GitHub'dan APK indirip kendini
  güncelleyen" güncelleyiciyi (INTERNET + REQUEST_INSTALL_PACKAGES) koymak.
  TekPanel'de Play uygulama içi güncelleme planlı.

### 3. NotifPlus

- Bağlantı:
  https://github.com/Adrian463588/NotifPlus
- Lisans: yok (repoda LICENSE dosyası yok; README lisans belirtmiyor).
  Yıldız: 0. Son commit: 2026-08-17 (fe18f67). 3 commit.
- Test: 7 dosya + CI (devsecops). Arayüz: Compose. Hilt, Room 2.7, WorkManager.
- İnceleme: derin. README Endonezce.
- Mimari not: thread (bildirim) / snapshot (sürüm) / message tabloları;
  "payloadAvailability" ve "captureOrigin" alanları; ek dosyalar ayrı depo.
  https://github.com/Adrian463588/NotifPlus/blob/fe18f67/app/src/main/java/com/notifplus/data/local/NotificationEntity.kt
- Öğrendiğimiz 3 şey:
  - Kopma sonrası rebind gecikmeleri [0, 5 sn, 15 sn, 60 sn]; her adımda
    "yeniden bağlanıyor / kesildi / erişim gerekli" sağlık durumu yazılıyor.
    https://github.com/Adrian463588/NotifPlus/blob/fe18f67/app/src/main/java/com/notifplus/service/NotificationCaptureCoordinator.kt
  - Boot alıcısı yalnız BOOT_COMPLETED değil: MY_PACKAGE_REPLACED ve iki
    QUICKBOOT_POWERON da dinleniyor (güncelleme sonrası dinleyici düşer).
    https://github.com/Adrian463588/NotifPlus/blob/fe18f67/app/src/main/java/com/notifplus/service/BootCompletedReceiver.kt
  - Ek dosya: geçici dosyaya yaz, SHA-256 hesapla, boyut sınırını aşarsa iptal,
    sonra atomik yeniden adlandır. Silerken yolun kök klasör içinde olduğunu
    doğrula. Okunamayanlar "yakalama anında yok" durumuyla işaretleniyor.
    https://github.com/Adrian463588/NotifPlus/blob/fe18f67/app/src/main/java/com/notifplus/data/repository/NotificationAttachmentFileStore.kt
- Yapmayacağımız 1 şey: README'de başka bir ürünün adını ("NotiStar-Style")
  tanımlayıcı olarak kullanmak (marka riski) ve lisansı belirsiz kodu referans
  alıp kod taşımak.

### 4. notification-listener-android (Alfio010)

- Bağlantı:
  https://github.com/Alfio010/notification-listener-android
- Lisans: GPL-3.0. Yıldız: 223. Son commit: 2026-10-20 (9e7ebe8). 278 commit.
- Açık issue: 6. Test: 2 dosya. Arayüz: Views + Compose sohbet ekranı.
  DB: ObjectBox.
- İnceleme: derin.
- Mimari not: tek iş parçacıklı yürütücü; bildirim -> kara/beyaz liste -> dedup
  -> ObjectBox. Uygulama başına "sohbet mi" bayrağı; sohbet uygulamaları ayrı
  sekmede balon görünümüyle.
  https://github.com/Alfio010/notification-listener-android/blob/9e7ebe8/app/src/main/java/com/android/alftendev/services/NotificationListenerServiceImpl.kt
- Öğrendiğimiz 3 şey:
  - Çift kayıt: paket başına 30 girişlik LRU + aynı başlık/metin için 30 sn
    penceresi. Basit ama yeterli bir ilk savunma.
  - Silinmiş bildirim: `onNotificationRemoved` içinde veritabanında olmayan
    bildirimi "silindi" diye kaydedip kullanıcıya haber veriyor.
  - Otomatik kara liste: sürekli (ongoing) ve sistem kategorisi bildirimler,
    kendi paketi, bazı üretici anahtarları. WhatsApp için `key` içinde "null"
    geçen (büyük olasılıkla özet) bildirimleri atlıyor; nedeni kodda yazılı
    değil. [doğrulanamadı]
    https://github.com/Alfio010/notification-listener-android/blob/9e7ebe8/app/src/main/java/com/android/alftendev/services/utils/NotiUtils.kt
- Yapmayacağımız 1 şey: sadece başlık/metin/büyük metne bakıp MessagingStyle'ı
  yok saymak. Sohbetlerde son mesajlar tek satıra çöküyor.

### 5. notification-vault (NotiVault)

- Bağlantı:
  https://github.com/snap24/notification-vault
- Lisans: GPL-3.0. Yıldız: 72. Son commit: 2026-09-03 (175d3b7). 399 commit.
- Açık issue: 13. Test: 0. Dil: Java. Arayüz: Views (Material).
- İnceleme: derin.
- Mimari not: tek iş parçacıklı yürütücü; kural (anahtar sözcük) -> çift kayıt
  -> şifrele -> Room (+ SQLCipher v3). Ayrıca toast kaydı, Drive yedeği,
  hızlı ayar kutucuğu, widget'lar.
  https://github.com/snap24/notification-vault/blob/175d3b7/app/src/main/java/com/zygisk_enc/notivault/service/NotiVaultService.java
- Öğrendiğimiz 3 şey:
  - Ham `android.messages` paketinden son mesajı, göndereni, zamanı ve görsel
    URI'sini okuyor. Görselleri yalnızca "son mesajdan 10 sn içindekiler" için
    alıyor; eski birikmiş fotoğrafları atlıyor.
  - Art arda aynı bildirim `duplicateCount` ile tek satırda toplanıyor
    (sayaç gösterilebilir: "3 kez").
  - Şifreli alanda arama: jeton SHA-256 ilk 8 bayt -> long "kör dizin".
    Alan bazlı AES-GCM şifreleme + kör dizin; v3.0.0'da ayrıca SQLCipher
    eklenmiş (README'ye göre).
    https://github.com/snap24/notification-vault/blob/175d3b7/app/src/main/java/com/zygisk_enc/notivault/util/BlindIndexHelper.java
- Yapmayacağımız 1 şey: "Tümünü temizle"yi tek hamlede yapmak. Issue #28:
  çok sayıda bildirimde sessizce başarısız. Parça parça siler, sonucu raporlarız.

### 6. ActivityTrace

- Bağlantı:
  https://github.com/DavidNeurieder/ActivityTrace
- Lisans: GPL-3.0. Yıldız: 27. Son commit: 2026-09-11 (827d0a7). 189 commit.
- Test: 56 dosya. Arayüz: Compose. DB: Room + SQLCipher + FTS5.
- İnceleme: derin.
- Mimari not: bildirim ve (isteğe bağlı) erişilebilirlik ekran metni ve dosya
  dizinleme tek zaman çizelgesinde. Arama motoru ayrı (sorgu ayrıştırıcı, BM25).
  https://github.com/DavidNeurieder/ActivityTrace/blob/827d0a7/app/src/main/java/com/activitytrace/capture/ActivityTraceNotificationListener.kt
- Öğrendiğimiz 3 şey:
  - FTS5 "external content" tablosu + 3 tetikleyici ile ana tabloyla senkron;
    `in:signal`, `type:notification` gibi sorgu sözdizimi.
    https://github.com/DavidNeurieder/ActivityTrace/blob/827d0a7/app/src/main/java/com/activitytrace/store/ActivityTraceDatabase.kt
  - Çift kayıt iki katmanlı: 2 sn'lik bellek LRU + kalıcı `content_hash`
    UNIQUE dizini (SHA-256 paket|tür|normalize metin).
    https://github.com/DavidNeurieder/ActivityTrace/blob/827d0a7/app/src/main/java/com/activitytrace/capture/CaptureIngestor.kt
  - Görsel: WEBP kalite 80, 500 KB'ı geçerse hiç saklama; şifreli DB içinde
    BLOB. Ayrıca "demo modu" (sahte veri) mağaza ekran görüntüsü için de işe yarar.
- Yapmayacağımız 1 şey: erişilebilirlik servisiyle ekran içeriği toplamak
  (Play incelemesi ve güven riski). Ayrıca başlık+metni " — " ile tek dizgede
  birleştirmek: gönderen/yapı bilgisi kaybolur.

### 7. Notification-History-Android (Viet200103)

- Bağlantı:
  https://github.com/Viet200103/Notification-History-Android
- Lisans: MIT. Yıldız: 14. Son commit: 2024-03-25 (6b47944). Bakımsız görünüyor.
- Test: 23 dosya. Arayüz: Views/XML. DB: Room.
- İnceleme: derin.
- Mimari not: dinleyici -> uygulama meta önbelleği -> işleyici. Büyük ikon ve
  büyük resim, paket-hash klasöründe dosya; DB'de yalnız "var mı" bayrağı.
  Şablon türü (BigText, BigPicture, Inbox, Messaging...) kaydediliyor ama
  MessagingStyle mesajları ayrıştırılmıyor.
  https://github.com/Viet200103/Notification-History-Android/blob/6b47944/app/src/main/java/com/notisaver/main/log/core/NotificationHandler.kt
- Öğrendiğimiz 3 şey:
  - Canlı tutma: `setComponentEnabledSetting` ile bileşeni DISABLED -> ENABLED
    yapıp ardından `requestRebind`; `START_STICKY`; isteğe bağlı "canlı tut"
    bildirimi (foreground).
    https://github.com/Viet200103/Notification-History-Android/blob/6b47944/app/src/main/java/com/notisaver/main/log/core/NotisaveListenerService.kt
  - Pil optimizasyonu için uygulama içi görsel adım rehberi: 6 ekran görüntüsü
    (assets/battery_optimization), EN/ES/VI.
    https://github.com/Viet200103/Notification-History-Android/tree/6b47944/app/src/main/assets/battery_optimization
  - Uygulama başına "temizlenebilir" seçeneği: kaydettikten sonra kaynak
    bildirimi iptal edebiliyor (kullanıcı seçerse).
- Yapmayacağımız 1 şey: kaynak bildirimi sessizce iptal etmek (varsayılan
  açık). Kullanıcı sorulmadan hiçbir kaynak bildirime dokunmayız.

### 8. NotificationLog (Codeberg; GitHub değil)

- Bağlantı:
  https://codeberg.org/jonas-l/notificationlog-android
- Lisans: GPL-3.0 veya üstü (LICENSE.txt başlığı). Son commit: 2025-07-29
  (ced6e84). Test: 2 dosya. Arayüz: Views. DB: Room.
- İnceleme: derin (dinleyici ve kayıt yardımcıları okundu).
- Mimari not: yalnız başlık, metin, ilerleme alanları. Aynı (paket, id, tag)
  bildirimin güncellemelerini "sürüm zinciri" olarak tutuyor.
  https://codeberg.org/jonas-l/notificationlog-android/src/commit/ced6e84/app/src/main/java/de/jl/notificationlog/service/NotificationSaveUtil.kt
- Öğrendiğimiz 3 şey:
  - Sürüm yönetimi ayarı: tüm sürümler / yalnız ilk / yalnız son. Bizde
    "düzenlenen mesaj" gösterimine fikir verir.
  - `contentIntent` bellekte tutulur; dinleyici bağlanınca etkin
    bildirimlerden geri yüklenir ("tıkla, asıl yere git" yeniden çalışır).
  - Erişilebilirlik servisiyle `TYPE_NOTIFICATION_STATE_CHANGED` olayından
    bildirim yakalama yedeği.
- Yapmayacağımız 1 şey: erişilebilirlik yedeği. Güven ve Play politikası riski.

### 9. ReNotify

- Bağlantı:
  https://github.com/Bahoz420/ReNotify-App-OpenSource
- Lisans: GPL-3.0. Yıldız: 2. Son commit: 2026-10-09 (2930863). 4 commit.
- Test: 0. Arayüz: Compose (iOS görünümlü tema). DB: Room.
- Not: INTERNET izni var (kullanıcı onaylı telemetri) ve kodda "Play sürümü Pro
  kilidi" geçiyor. Yani GPL kod + Play'de ücretli sürüm modeli. [tek kaynak]
  https://github.com/Bahoz420/ReNotify-App-OpenSource/blob/2930863/app/src/main/java/com/renotify/app/telemetry/Telemetry.kt
- İnceleme: derin.
- Öğrendiğimiz 3 şey:
  - `ListenerGuard.kick`: yorumuna göre `requestRebind` tek başına sistem
    bağlantıyı bıraktıktan sonra çoğu zaman yok sayılıyor; bu yüzden bileşeni
    kapat/aç da yapıyor, 60 sn'de bir ile sınırlı. (Viet200103 ve NotifPlus de
    aynı hileyi kullanıyor; resmi belgede böyle bir garanti yok.)
    https://github.com/Bahoz420/ReNotify-App-OpenSource/blob/2930863/app/src/main/java/com/renotify/app/service/ListenerGuard.kt
  - `ListenerWatchdogWorker`: 30 dakikada bir WorkManager ile dinleyici sağlığı
    kontrolü; uygulama kapalıyken de çalışır.
    https://github.com/Bahoz420/ReNotify-App-OpenSource/blob/2930863/app/src/main/java/com/renotify/app/service/ListenerWatchdogWorker.kt
  - Pil muafiyeti isteğini yorumda Play politikasıyla gerekçelendirip,
    olmazsa genel liste ekranına düşüyor (fallback).
- Yapmayacağımız 1 şey: sessizleştirmek için orijinal bildirimi iptal edip
  kopyasını yeniden yayınlamak. DoNotNotify kendi belgesinde de bu yolda
  MessagingStyle ve RemoteInput zenginliğinin kaybolduğunu yazıyor.
  https://github.com/anujja/DoNotNotify/blob/8e5a1fa/app/src/main/java/com/donotnotify/donotnotify/StackedNotificationManager.kt

### 10. DoNotNotify

- Bağlantı:
  https://github.com/anujja/DoNotNotify
- Lisans: MIT. Yıldız: 430. Son commit: 2026-10-20 (8e5a1fa). 175 commit.
- Açık issue: 3. Test: 16 dosya. Arayüz: Compose. Depolama: JSON dosya.
- İnceleme: derin.
- Mimari not: dinleyici -> kural eşleştirici (engelle/izin ver/yığınla/kısıtla)
  -> geçmiş. Yığınlama: kaynağı iptal edip kendi paketi altında grup olarak
  yeniden yayınlar. 40+ hazır kural; topluluk paylaşımı.
- Öğrendiğimiz 3 şey:
  - `OemAutostart`: 10 üretici (Xiaomi, Huawei, Oppo, OnePlus, Vivo, Samsung,
    Asus, Letv, Meizu, Nokia) için olası ayar ekranı bileşenleri; `resolveActivity`
    ile yoklayıp ilk çalışanı açıyor, yoksa sessiz geçiyor.
    https://github.com/anujja/DoNotNotify/blob/8e5a1fa/app/src/main/java/com/donotnotify/donotnotify/setup/OemAutostart.kt
  - `HealthCheckWorker`: 6 saatte bir; dinleyici etkin mi ve son bağlantı 24
    saatten yeni mi bakar; değilse kullanıcıya TEK bildirim (24 saatte bir
    sınırlı). Ayrıca 1 saatlik "kalp atışı" ve `onListenerDisconnected` içinde
    `requestRebind`.
    https://github.com/anujja/DoNotNotify/blob/8e5a1fa/app/src/main/java/com/donotnotify/donotnotify/health/HealthCheckWorker.kt
  - Kurulum sihirbazı (SetupWizardScreen) + "yeniden giriş koruması": kendi
    yayınladığı bildirimi tekrar işlememek için ilk satırda paket kontrolü.
- Yapmayacağımız 1 şey: geçmişi tek JSON dosyasında tutup her bildirimde
  dosyayı baştan okuyup baştan yazmak (O(n) yazma). Room kullanırız.
  https://github.com/anujja/DoNotNotify/blob/8e5a1fa/app/src/main/java/com/donotnotify/donotnotify/NotificationHistoryStorage.kt

### 11. NotiFilter

- Bağlantı:
  https://github.com/BURG3R5/NotiFilter
- Lisans: GPL-3.0. Yıldız: 246. Son commit: 2026-10-09 (c7613dc). 219 commit.
- Açık issue: 6 (toplam 53). Test: 14 dosya + 4 CI iş akışı. Arayüz: Compose.
- İnceleme: kısmi (dinleyici başı, issue'lar, wiki bilgisi).
- Mimari not: regex tabanlı filtreler; 12 eylem (kapat, ertele, grupla,
  debounce, sustur, uyar, sesli oku...); zamanlama; ana ekran widget'ı.
  İsteğe bağlı "ön planda çalıştır" tercihi.
  https://github.com/BURG3R5/NotiFilter/blob/c7613dc/app/src/main/java/co/adityarajput/notifilter/services/NotificationListener.kt
- Öğrendiğimiz 3 şey:
  - "Sistemin dinleyiciyi başlatmasını bekliyor" durumunu açıkça gösteriyor;
    böylece kullanıcı "çalışmıyor" yerine "izin eksik" görüyor.
  - Xiaomi/Redmi/POCO için wiki sorun giderme sayfası; issue #124 sonrası
    "Arka planda otomatik başlatma" adımı eklendi. Üreticiye özel rehber
    yaşayan bir belge olmalı.
    https://github.com/BURG3R5/NotiFilter/issues/124
  - Çözülmeyen vaka: issue #93'te sistem dinleyiciyi hiç başlatmadı; sahibi
    "üreticiye özel" deyip "not planned" kapattı. Her cihaz kurtarılamaz;
    bunu kullanıcıya dürüstçe söyleyecek bir "desteklenmeyen cihaz" metni lazım.
    https://github.com/BURG3R5/NotiFilter/issues/93
- Yapmayacağımız 1 şey: kullanıcıya regex yazdırmak ve erişilebilirlik ile
  "dokunma" eylemi yaptırmak. Berber/kuaför regex yazmaz.

### 12. SpeakThat!

- Bağlantı:
  https://github.com/mitchib1440/SpeakThat
- Lisans: GPL-3.0. Yıldız: 138. Son commit: 2026-10-06 (1af2d9a). 1122 commit.
- Açık issue: 14. Test: 1 dosya. Arayüz: Views (Kotlin+Java).
- İnceleme: derin (kurtarma, boot, dedup bölümleri).
- Mimari not: tek `NotificationReaderService.kt` dosyası 8500 satır.
  Merkezî `NotificationListenerRecovery` nesnesi bağlantıyı izliyor.
  Kaynak dosya başlıklarında "SPEAKTHAT, Birleşik Krallık'ta tescilli ticari
  marka" yazıyor.
- Öğrendiğimiz 3 şey:
  - Rebind için üstel geri çekilme: 15 sn'den 15 dk'ya; 30 dakikada en fazla
    6 deneme; "bağlandı / koptu / son kalp atışı" zamanları saklı; erişim yoksa
    hiç denemez.
    https://github.com/mitchib1440/SpeakThat/blob/1af2d9a/app/src/main/java/com/micoyc/speakthat/NotificationListenerRecovery.kt
  - Çok katmanlı çift kayıt: 30 sn içerik penceresi + "grup çocuğu yeniden
    gönderimi" penceresi (Android gruplamayı yeniden kurunca eski çocukları
    yeniden yayınlar) + FLAG_GROUP_SUMMARY atlama + Gmail'e özel yumuşak kural.
    https://github.com/mitchib1440/SpeakThat/blob/1af2d9a/app/src/main/java/com/micoyc/speakthat/NotificationReaderService.kt
  - Kendi kendine test ekranı (SelfTest), uygulama içi günlükçü (InAppLogger),
    boot alıcısı ve `ServiceRestartPolicy` periyodik işi.
    https://github.com/mitchib1440/SpeakThat/blob/1af2d9a/app/src/main/java/com/micoyc/speakthat/BootReceiver.java
- Yapmayacağımız 1 şey: 8.500 satırlık tek servis sınıfı ve tek test. Dinleyici
  ince kalır (Notification-HQ'nun "<40 satır" kuralı gibi).
  Ek ders: ürün adını tescil edilmiş bir markayla çakıştırmayız; TekPanel için
  marka taraması yapılmalı [bu kolun kapsamı dışı].

### 13. Watomatic

- Bağlantı:
  https://github.com/adeekshith/watomatic
- Lisans: GPL-3.0. Yıldız: 555. Son commit: 2026-05-11 (e9a6416). 1800 commit.
- Açık issue: 64. Test: 37 dosya. Arayüz: Views (XML). DB: Room.
- Not: Firebase, faturalama ve ağ (yapay zekâ cevapları) içeriyor.
- İnceleme: derin (hizmet, karar sınıfı, sağlık işi).
- Öğrendiğimiz 3 şey:
  - `NlsHealthCheckWorker`: 6 saatte bir `enabled_notification_listeners`
    içinde paketimiz var mı bakar; yoksa "erişim iptal edildi" bildirimi atar.
    https://github.com/adeekshith/watomatic/blob/e9a6416/app/src/main/java/com/parishod/watomatic/service/NlsHealthCheckWorker.kt
  - Cevap: bildirimin RemoteInput eylemi + PendingIntent. Kararlar ayrı bir
    sınıfta (`NotificationReplyDecider`): 10 sn minimum aralık, grup/özel
    ayrımı (`android.isGroupConversation`), kendi mesajını ayıklama
    (`android.selfDisplayName`). Bu sayede birim test edilebilir.
    https://github.com/adeekshith/watomatic/blob/e9a6416/app/src/main/java/com/parishod/watomatic/service/NotificationReplyDecider.java
  - İsteklerden: WhatsApp Business (#404), kişi filtreleri (#412), eski
    konuşmaları hariç tutma (#419). İşletme sahiplerinin ne istediğine ışık tutar.
    https://github.com/adeekshith/watomatic/issues
- Yapmayacağımız 1 şey: otomatik/yapay zekâ cevap, Firebase, faturalama, ağ.
  "Veriler cihazdan çıkmaz" vaadine ters.

### 14. voicenotify

- Bağlantı:
  https://github.com/pilot51/voicenotify
- Lisans: Apache-2.0. Yıldız: 242. Son commit: 2026-01-28 (3cc5d9a). 355 commit.
- Açık issue: 55. Test: 0. Arayüz: Compose. DB: Room.
- İnceleme: kısmi (hizmet ve issue'lar).
- Öğrendiğimiz 3 şey:
  - Her filtre kararı bir `IgnoreReason` ile kaydediliyor ve kayıt ekranında
    görünüyor. TekPanel'deki `FilterReason` ile aynı fikir; doğrulandı.
    https://github.com/pilot51/voicenotify/blob/3cc5d9a/app/src/main/java/com/pilot51/voicenotify/Service.kt
  - Genel ayar + uygulama başı ayar birleşimi (`getCombinedSettings`).
  - "Aynı bildirimi n saniye içinde tekrar okuma" ayarı (ignoreRepeat).
- Yapmayacağımız 1 şey: kalıcı debug log. Issue #168 flash aşınması, #166
  askıdayken CPU/IO kullanımı. Log varsayılan kapalı, kısa ömürlü olur.
  https://github.com/pilot51/voicenotify/issues

### 15. push-filter

- Bağlantı:
  https://github.com/Rai220/push-filter
- Lisans: MIT. Yıldız: 12. Son commit: 2026-09-08 (3415687). 8 commit.
- Test: 1 dosya. Arayüz: Compose. İnceleme: kısmi (hizmet dosyası okundu).
- Öğrendiğimiz 3 şey:
  - `onListenerDisconnected` içinde `requestRebind`: "paket güncellemesinde
    sistem dinleyiciyi unbind eder".
  - 5 sn'lik `anahtar|engellendi|başlık|metin` penceresi ile günlük şişmesini
    (okundu bilgisi, ilerleme güncellemeleri) önlüyor.
  - Kural motoru (FilterEngine) ve regex önbelleği ayrı sınıflarda.
    https://github.com/Rai220/push-filter/blob/3415687/app/src/main/java/ru/krestnikov/pushfilter/filter/NotificationFilterService.kt
- Yapmayacağımız 1 şey: kuralları son kullanıcıya regex olarak açmak.

### 16. QQ-Notify-Evolution

- Bağlantı:
  https://github.com/ichenhe/QQ-Notify-Evolution
- Lisans: AGPL-3.0. Yıldız: 165. Son commit: 2025-07-03 (f24b935). 117 commit.
- Açık issue: 5 (toplam 29). Test: 4 dosya. Arayüz: Compose.
- İnceleme: kısmi.
- Mimari not: QQ'nun bildirimini Android'in "Konuşmalar" biçimine (MessagingStyle,
  Person, dinamik kısayol) çevirip yeniden yayınlıyor; Nevo eklentisi veya
  bağımsız çalışabiliyor.
  https://github.com/ichenhe/QQ-Notify-Evolution/blob/f24b935/app/src/main/java/cc/chenhe/qqnotifyevo/core/NotificationProcessor.kt
- Öğrendiğimiz 3 şey:
  - Avatar önbelleği (disk + bellek, süreli): grup sohbetinde bildirim avatar
    taşımadığında önceki avatar kullanılıyor.
    https://github.com/ichenhe/QQ-Notify-Evolution/blob/f24b935/app/src/main/java/cc/chenhe/qqnotifyevo/core/AvatarManager.kt
  - `isRunning()` testi: servis zorla öldürülünce nesne boş kalmıyor ama
    `ping()` istisna atıyor; gerçek durumu böyle anlıyor.
    https://github.com/ichenhe/QQ-Notify-Evolution/blob/f24b935/app/src/main/java/cc/chenhe/qqnotifyevo/service/NotificationMonitorService.kt
  - Satıcı push'larıyla gelen bildirimler (MiPush #29, Huawei push #24)
    "wontfix" kapatıldı: dinleyici satıcının kendi bildirim yolundan gelen her
    şeyi yakalayamayabilir. [tek kaynak, başlıklar]
    https://github.com/ichenhe/QQ-Notify-Evolution/issues
- Yapmayacağımız 1 şey: kök/Nevo modu ve satıcı push'larına bağımlı çözümler.

### 17. SmartIsland

- Bağlantı:
  https://github.com/agupta07505/SmartIsland
- Lisans: GPL-3.0. Yıldız: 299. Son commit: 2026-10-19 (149efd1). 387 commit.
- Açık issue: 31 (7'si "oem-specific"). Test: 173 dosya. Arayüz: Compose (145 dosya).
  DB: SQLiteOpenHelper (geçmiş).
- İnceleme: derin (metin okuma, kurtarma, görünüm denetçisi).
- Öğrendiğimiz 3 şey:
  - "Genişletilmiş metin" sırası: BIG_TEXT, yoksa TEXT_LINES, yoksa
    EXTRA_MESSAGES son 6 satır; kendi 1500 karakter sınırı.
    https://github.com/agupta07505/SmartIsland/blob/149efd1/app/src/main/java/com/agupta07505/smartisland/util/NotificationBigText.kt
  - Standart alanlar boşken özel `RemoteViews`'ı şişirip içindeki düğme ve
    etiketleri okuyan `NotificationViewInspector`.
    https://github.com/agupta07505/SmartIsland/blob/149efd1/app/src/main/java/com/agupta07505/smartisland/util/NotificationViewInspector.kt
  - `SystemServiceRecovery`: erişilebilirlik için bileşen durumunu DEFAULT/ENABLED
    arasında çevirip paket değişikliği sinyali üretiyor; bildirim dinleyici için
    resmî `requestRebind`.
    https://github.com/agupta07505/SmartIsland/blob/149efd1/app/src/main/java/com/agupta07505/smartisland/util/SystemServiceRecovery.kt
- Yapmayacağımız 1 şey: ekran üstü katman + erişilebilirlik + Shizuku gerektiren
  özellikler.

### 18. SmsForwarder

- Bağlantı:
  https://github.com/pppscn/SmsForwarder
- Lisans: BSD-2-Clause. Yıldız: 28.5 bin. Son commit: 2026-09-14 (a3d2302).
  874 commit. Açık issue: README 17, liste 20+ (tutarsız).
- Test: 0. Arayüz: Views (XML, xuexiang kütüphaneleri). DB: Room. İş kuyruğu:
  WorkManager.
- İnceleme: kısmi (dinleyici, ayarlar, manifest).
- Öğrendiğimiz 3 şey:
  - İletim işi `SendWorker` ile sıraya konuyor; dinleyici hızlı döner.
    https://github.com/pppscn/SmsForwarder/blob/a3d2302/app/src/main/kotlin/cn/ppps/forwarder/service/NotificationService.kt
  - Kullanıcı ayarları: anahtar sözcük kara listesi (regex), "yalnız ekran
    kilitliyken ilet", belirli uygulamaların bildirimini otomatik kapat.
  - 28 bin yıldızlı bir projede bile en çok şikayet "sessizce durdu" ve
    üreticiye özel sorunlar (bkz. şikayetler bölümü). Sorun ürün büyüklüğüyle
    çözülmüyor. [çoklu]
- Yapmayacağımız 1 şey: "Cactus" canlı tutma hileleri (sessiz müzik çalma,
  1 piksel etkinlik) ve geniş izin listesi (SMS, arama kaydı, rehber, konum,
  kamera, KILL_BACKGROUND_PROCESSES).
  https://github.com/pppscn/SmsForwarder/blob/a3d2302/app/src/main/kotlin/cn/ppps/forwarder/receiver/CactusReceiver.kt

### 19. NotificationForwarder (ItsAzni)

- Bağlantı:
  https://github.com/ItsAzni/NotificationForwarder
- Lisans: MIT. Yıldız: 35. Son commit: 2026-04-26 (b2f7e9e). 6 commit.
- Test: 0. Arayüz: Compose. İnceleme: kısmi (dinleyici okundu).
- Öğrendiğimiz 3 şey:
  - LRU çift kayıt: anahtar + içerik özeti + postTime veya "ani güncelleme"
    penceresi.
    https://github.com/ItsAzni/NotificationForwarder/blob/b2f7e9e/app/src/main/java/com/itsazni/notificationforwarder/service/AppNotificationListenerService.kt
  - Grup özetini atlama (FLAG_GROUP_SUMMARY).
  - Önce Room kuyruğuna yaz, sonra WorkManager ile gönder (veri kaybı yok).
- Yapmayacağımız 1 şey: webhook ile dışarı göndermek; ayrıca içerik
  karşılaştırmasında `hashCode()` (çarpışma). Biz SHA-256 kullanıyoruz.

### 20. android-notification-forwarder (karuhun)

- Bağlantı:
  https://github.com/karuhun-developer/android-notification-forwarder
- Lisans: MIT (yalnız README'de; LICENSE dosyası yok). Yıldız: 15.
  Son commit: 2026-10-14 (e409fe4). 17 commit. Test: 8 dosya. Arayüz: Compose.
- İnceleme: kısmi.
- Öğrendiğimiz 3 şey:
  - Olay kimliği: SHA-256(kurulum kimliği, anahtar, postTime, başlık, metin);
    her alan uzunluk önekiyle birleştiriliyor (çakışma belirsizliğini önler).
    https://github.com/karuhun-developer/android-notification-forwarder/blob/e409fe4/app/src/main/java/com/karuhundeveloper/notification/delivery/EventIdentity.kt
  - Veriyi ağa vermeden önce kalıcılaştır; `onListenerConnected` içinde
    kurtarma ve "etkin bildirimleri yakala" adımı.
  - Room göçü v1/v2 -> v3, yıkıcı geri düşüş (destructive fallback) olmadan.
- Yapmayacağımız 1 şey: ağ teslimat motoru (TekPanel'de internet yok).

### 21. message-mirror

- Bağlantı:
  https://github.com/Dragon-Born/message-mirror
- Lisans: MIT. Yıldız: 20. Son commit: 2025-11-08 (b107f15). 7 commit.
- Test: 11 dosya. Arayüz: Flutter + Kotlin dinleyici. İnceleme: kısmi.
- Öğrendiğimiz 3 şey:
  - Native dinleyici arayüz kapalıyken de olay üretir; olaylar bellekte
    biriktirilip Flutter kanalı bağlanınca boşaltılır.
    https://github.com/Dragon-Born/message-mirror/blob/b107f15/android/app/src/main/kotlin/lol/arian/notifmirror/MsgNotificationListener.kt
  - Uygulama seçicide arama ve ikon.
  - Cihaz içi tanı günlüğü (LogStore).
- Yapmayacağımız 1 şey: QUERY_ALL_PACKAGES ve READ_SMS izinleri; ikon/resmi
  Base64 PNG yapıp olaya gömmek. Biz `<queries>` ile belirli paketleri
  soruyoruz (TekPanel zaten böyle).
  https://github.com/Dragon-Born/message-mirror/blob/b107f15/android/app/src/main/AndroidManifest.xml

### 22. android-notification-aggregator (Tahsin005)

- Bağlantı:
  https://github.com/Tahsin005/android-notification-aggregator
- Lisans: MIT, ama LICENSE dosyası Expo şablonundan kalma ("650 Industries").
  Yıldız: 0. Son commit: 2026-09-29 (7d95e4b). 18 commit. Test: 0.
- Arayüz: React Native / Expo, Kotlin yerel modül. İnceleme: kısmi.
- Öğrendiğimiz 3 şey:
  - Yerel Kotlin servis doğrudan SQLite'a yazıyor; arayüz (RN) kapalıyken de
    kayıt kaybolmuyor.
    https://github.com/Tahsin005/android-notification-aggregator/blob/7d95e4b/modules/android-notification-listener/android/src/main/java/expo/modules/androidnotificationlistener/NotificationAggregatorService.kt
  - "Sessiz kasa": seçili uygulamaların bildirimini durum çubuğundan kaldırıp
    uygulamada tutuyor (DND ile). Kurucunun ihtiyacına benziyor ama kaynak
    bildirimi iptal etmek risklidir (cevap/RemoteInput kaybı).
  - İçgörü ekranı: saatlik histogram, en çok bildirim gelen uygulamalar,
    yakalanan/izin verilen oranı.
- Yapmayacağımız 1 şey: çift yığın (RN köprüsü). Ayrıca şablon artığı lisans
  metni: lisans hijyeni.

### 23. Service-Keeper

- Bağlantı:
  https://github.com/sklndev/Service-Keeper
- Lisans: GPL-3.0. Yıldız: 38. Son commit: 2026-09-22 (356b3d9). 111 commit.
- Test: 2 dosya. Arayüz: Flutter + Kotlin. İnceleme: kısmi.
- Öğrendiğimiz 3 şey:
  - README: "sistem tarafından devre dışı bırakılan dinleyicileri yeniden
    etkinleştirir". Kod, uygulamayı Shizuku ile `enabled_notification_listeners`
    listesine geri ekliyor. İzin sessizce kaybolabilir demektir.
    [çoklu, dolaylı: Watomatic'in sağlık işi aynı riski izliyor]
    https://github.com/sklndev/Service-Keeper/blob/356b3d9/android/app/src/main/kotlin/com/shaunkleyn/service_keeper/KeeperForegroundService.kt
  - Denetim kaydı: durdu / yeniden başlatıldı / sonuç.
  - 15 dakikadan kısa aralıklar için zincirleme tek seferlik işler.
- Yapmayacağımız 1 şey: Shizuku/ADB gerektiren onarım. Son kullanıcı yapamaz.

### 24. WhatsDeleted

- Bağlantı:
  https://github.com/4nubhav/WhatsDeleted
- Lisans: GPL-3.0. Yıldız: 134. Son commit: 2021-11-22 (6f444e8). 28 commit.
- Açık issue: 18. Test: 2 dosya. Arayüz: Views. İnceleme: tam (9 dosya).
- Mimari: yalnızca com.whatsapp ve Signal paketi sabit kodlu; başlık + metni
  düz metin dosyasına ekliyor. Medya için FileObserver.
  https://github.com/4nubhav/WhatsDeleted/blob/6f444e8/app/src/main/java/com/gmail/anubhavdas54/whatsdeleted/NotificationListener.kt
- Öğrendiğimiz 3 şey:
  - Kullanıcı istekleri bir ihtiyaç listesi: konuşma seçici (#15), dışa
    aktarma (#30), log boyutu sınırı (#13), çift kayıt (#11), Signal/Telegram
    (#14, #25).
    https://github.com/4nubhav/WhatsDeleted/issues
  - Android 11+ depolama kısıtları FileObserver tabanlı medya yedeğini bozdu
    (#29, #35, #37). Bildirimden okunan görsel daha sağlam.
  - README'de Türkçe çeviri var; Türkiye'de bu tür uygulamaya talep işareti.
    [tek kaynak]
    https://github.com/4nubhav/WhatsDeleted
- Yapmayacağımız 1 şey: depolama izni + FileObserver ile medya yakalamak;
  düz metin dosyaya eklemek.

### 25. Notification-HQ

- Bağlantı:
  https://github.com/Murali-KrishnaM/Notification-HQ
- Lisans: MIT (yalnız README; LICENSE dosyası yok). Yıldız: 1.
  Son commit: 2026-05-12 (9eaf45a). 45 commit. Test: 2 dosya.
- Arayüz: Views. DB: Room. İnceleme: kısmi.
- Öğrendiğimiz 3 şey:
  - Dinleyici 40 satırdan kısa kalma kuralı; işi Capture -> Denoiser ->
    Router -> DB boru hattına devrediyor.
    https://github.com/Murali-KrishnaM/Notification-HQ/blob/9eaf45a/app/src/main/java/com/bravo/notificationhq/NotificationListener.kt
  - Mesajı konuya/gruba yönlendirme: metin eşleme + gönderen puanı. Bizde
    "bu mesaj hangi müşteri/sipariş" eşlemesine fikir.
  - Çevrimdışı regex yedeği; yapay zekâ yoksa da çalışır.
- Yapmayacağımız 1 şey: bulut yapay zekâ (Gemini) çağrısı.

### 26. al-rawi

- Bağlantı:
  https://github.com/Hany-R-Mahmoud/al-rawi
- Lisans: yok. Yıldız: 0. Son commit: 2026-10-16 (cbdf95e).
- Karar: elendi. Ana dalda NotificationListenerService yok; bu bir RSS okuyucu.
  "Bildirim kutusu" iddiası birleştirilmemiş bir PR'da.
  https://github.com/Hany-R-Mahmoud/al-rawi/pull/1
  Bu PR'ı doğrulamadık. [doğrulanamadı]

## Klonlanmayanlar (bulundu, düşük değer veya kapsam dışı)

- Gadgetbridge (Codeberg) ve KDE Connect (KDE): bildirim yansıtma yapıyor ama
  GitHub'da değil; bu turda klonlanmadı.
  https://f-droid.org/en/packages/com.espruino.gadgetbridge.banglejs/
- AN2Linux, Android 2 Linux Notifications: bildirimleri Linux'a yansıtır.
  https://f-droid.org/en/packages/dev.patri9ck.a2ln/
- Basit/eski örnekler (öğrenme değeri düşük):
  https://github.com/suyashm002/fetchWhatsappdata
  https://github.com/atick-faisal/Android-Notification-Reader
  https://github.com/Suvam-Dawn/Android-Notification-Listener
  https://github.com/TaQuangKhoi/NotificationListenerService-Example
  https://github.com/jaikeerthick/Notifications-Reader
- Ağa iletenler (webhook/ödeme/n8n):
  https://github.com/cobanov/notification-relay-webhook
  https://github.com/suriyadi15/qrishook
  https://github.com/daohoangson/android-notification-listener
- Hedef amaçlı küçük araçlar:
  https://github.com/Rachni/Whatsapp-notification-filter
  https://github.com/NightlyNexus/BatAway
  https://github.com/eret9616/HideOngoing
  https://github.com/anatoliy-kovtun/DroidLog
  https://github.com/SN-Koarashi/NotifyLogger
- Kapalı kaynak, kod yok: Samsung NotiStar (Good Lock modülü), Tenqube Notisave.
  https://play.google.com/store/apps/details?id=com.tenqube.notisave
- Nevolution (bildirim eklenti platformu): GitHub adresi 404 verdi.
  https://github.com/oasisfeng/nevo
  [doğrulanamadı: depo taşındı mı, silindi mi]

## Mimari karşılaştırma

Sayılar bu 26 proje içindir (al-rawi hariç tutulduğunda 25). Kaynaklar: yukarıdaki
kartlar ve her projenin dosyaları.

- Arayüz teknolojisi
  - Compose ağırlıklı 13: notisave, pingbox, NotifPlus, ActivityTrace,
    DoNotNotify, NotiFilter, ReNotify, voicenotify, push-filter, QQ,
    SmartIsland, karuhun, ItsAzni. Alfio010 karışık (Views + Compose sohbet).
  - Views/XML 8: Viet200103, NotificationLog, WhatsDeleted, Watomatic, SpeakThat,
    SmsForwarder, notification-vault (Java), Notification-HQ.
  - Çapraz platform 3: message-mirror ve Service-Keeper (Flutter), aggregator
    (React Native). Her üçünde de yerel Kotlin dinleyici şart.
  - Yeni ve bakımlı projeler Compose'a kayıyor; Views olanların çoğu eski.
- Veri depolama
  - Room en yaygın (14 proje). Ham SQLite: pingbox, push-filter (WAL açık),
    SmartIsland geçmişi. ObjectBox: Alfio010. JSON/düz dosya: DoNotNotify,
    WhatsDeleted.
  - Şifreli DB (SQLCipher) yalnızca 3 projede: notisave, ActivityTrace,
    notification-vault. Hepsi "internet izni yok + yerel" vaadinin parçası.
  - Arama: FTS5 (ActivityTrace), FTS4 (notisave), kör dizin (vault).
- Çift kayıt (dedup) yöntemleri
  - Kalıcı benzersiz dizin: TekPanel (parmak izi), ActivityTrace (content_hash),
    notisave (dedupKey / eventKey).
  - Bellek penceresi: Alfio010 (30 sn, LRU 30), SpeakThat (30 sn + grup çocuğu),
    push-filter (5 sn), ActivityTrace (2 sn), ItsAzni (ani güncelleme),
    voicenotify (n sn ayarı).
  - Sürüm zinciri: NotificationLog. Sayaç: notification-vault (duplicateCount).
  - Mesaj başına anahtar: pingbox (anahtar + mesaj zamanı), notisave (LCS farkı).
  - Zayıf: `hashCode()` kullananlar (ItsAzni). Bizdeki SHA-256 parmak izi
    onlardan sağlam.
- MessagingStyle / EXTRA_MESSAGES okuma
  - Doğru okuyanlar: notisave (compat + tarihçe), pingbox, NotifPlus (compat +
    yerel EXTRA_MESSAGES + EXTRA_HISTORIC_MESSAGES), notification-vault (ham
    paket), SmartIsland (son 6), QQ (yeniden kurar), TekPanel (compat).
  - Yok sayanlar: Alfio010, Viet200103 (yalnız şablon türü), NotificationLog,
    WhatsDeleted, DoNotNotify, NotiFilter, push-filter (arama için başlık/metin),
    ActivityTrace, ItsAzni, karuhun, aggregator, message-mirror (ham alanlar).
  - Kimse, platformun 1024 karakter kesmesini kullanıcıya göstermiyor.
    [bu 26 projede bulunamadı]
- Görsel (büyük ikon / resim) saklama
  - Dosya sistemi: notisave (JPEG 85), Viet200103 (paket klasörü), NotifPlus
    (atomik yazma + SHA-256 + boyut sınırı), TekPanel (WEBP; avatar 192 px, resim
    1280 px).
  - Şifreli BLOB: ActivityTrace (WEBP 80, en çok 500 KB). Şifreli dosya: vault.
  - Olayın içine Base64: message-mirror (ağ için, ağır).
  - Avatar önbelleği: yalnız QQ (süreli, grup sohbeti için yedek).
- Servisi canlı tutma
  - `onListenerDisconnected` içinde `requestRebind`: DoNotNotify, push-filter,
    SmartIsland, karuhun, SmsForwarder, SpeakThat (geri çekilmeli), NotifPlus
    (kademeli), Viet200103, ReNotify.
  - Bileşen aç/kapat hilesi: Viet200103, ReNotify, NotifPlus (README), SmartIsland
    (yalnız erişilebilirlik için). Resmi belgede garanti yok. [çoklu, kod]
  - Boot alıcısı: SpeakThat, NotifPlus (3 eylem), ReNotify, Watomatic,
    message-mirror, ItsAzni, TekPanel (yalnız BOOT_COMPLETED).
  - Periyodik sağlık işi: DoNotNotify (6 sa), Watomatic (6 sa), ReNotify (30 dk),
    SpeakThat (yeniden başlatma politikası). Hiçbiri tek bir ekranda
    "bağlantı sağlığı" göstermiyor; yalnız notisave gösteriyor.
  - Zorunlu foreground: message-mirror, SmsForwarder. Kullanıcı seçimli:
    Viet200103, NotiFilter, SpeakThat. Çoğu proje foreground olmadan çalışıyor.
- OEM pil sorunlarına çözüm
  - Üretici algılayıp doğrudan ayar ekranı açma: DoNotNotify (10 üretici),
    SmartIsland (OemDeviceRules).
  - Görsel adım rehberi: Viet200103 (6 ekran görüntüsü).
  - Metin rehberi: notisave (docs/OEM_GUIDANCE.md), NotiFilter (wiki).
  - Hile: SmsForwarder (Cactus). Ayrıcalıklı onarım: Service-Keeper, SmartIsland
    (Shizuku).
- Test durumu
  - Güçlü: SmartIsland (173 dosya), ActivityTrace (56), Watomatic (37),
    Viet200103 (23), DoNotNotify (16), pingbox (16), NotiFilter (14).
  - Zayıf veya yok: ReNotify, voicenotify, SmsForwarder, NotificationForwarder,
    notification-vault, aggregator (0); SpeakThat (1, 8500 satırlık servis).
  - En ilginç: notisave'in "fixture" uygulaması ve senaryo listesi. Kendi
    test uygulamasıyla bildirim üretip beklenen çıktıyı doğruluyor.
    https://github.com/abhinavsha077/notisave/blob/3b1865c/docs/TEST_MATRIX.md
- Lisans
  - Copyleft 13: GPL-3.0 (Alfio010, WhatsDeleted, vault, ActivityTrace, ReNotify,
    pingbox, NotiFilter, Watomatic, SpeakThat, SmartIsland, Service-Keeper),
    GPL-3.0+ (NotificationLog), AGPL-3.0 (QQ).
  - İzinli 11: MIT (Viet200103, DoNotNotify, ItsAzni, karuhun, push-filter,
    message-mirror, aggregator, Notification-HQ), Apache-2.0 (notisave,
    voicenotify), BSD-2 (SmsForwarder).
  - Lisanssız 2: NotifPlus, al-rawi (kod yok sayılır; "tüm hakları saklıdır").
  - Karar: hiçbirinin kodunu kopyalamıyoruz. Copyleft olanlardan ise yapıyı bile
    birebir taklit etmemeliyiz; yalnız fikir ve genel teknik.
- Bakım durumu
  - Canlı (son commit son 30 gün): Alfio010, notisave, pingbox, SmartIsland,
    DoNotNotify, NotiFilter, SpeakThat, karuhun, aggregator*, vault*, Service-Keeper*.
    (* 30 günü biraz aşıyor.)
  - Ölü: WhatsDeleted (2021), Viet200103 (2024), NotificationLog (2025-07).
  - 0-4 commitlik "yeni" projeler (notisave, NotifPlus, ReNotify, pingbox)
    büyük ihtimalle tek kişi, kısa sürede yazılmış; iddiaları test edilmemiş
    kabul edilmeli.

## Bize entegre edilebilecek fikirler (şu sistemi şu şekilde bağlarız)

Her madde: fikir, hangi TekPanel dosyasına bağlanır, kaynak.

- 1) Dinleyici kendini onarsın (en yüksek öncelik)
  - Bağlanacağı yer: `TekPanelNotificationListenerService.onListenerDisconnected`.
  - Şimdi: yalnız tanı kaydı yazıyor; yeniden bağlanma yalnızca açılışta
    (`BootRestartReceiver`).
  - Ekle: kademeli `requestRebind` (0, 5 sn, 15 sn, 60 sn; NotifPlus) ve
    üstel geri çekilmeli sınır (SpeakThat: en çok 6 deneme / 30 dk).
    Hâlâ kopuksa ve erişim verilmişse, 60 sn'de bir sınırlı bileşen aç/kapat
    (ReNotify, Viet200103). Etkisi garanti değil; yalnız ikinci adım.
  - Kaynaklar: SpeakThat NotificationListenerRecovery, NotifPlus Coordinator,
    ReNotify ListenerGuard (kartlarda bağlantılar var).
- 2) Periyodik sağlık kontrolü + tek kullanıcı bildirimi
  - Bağlanacağı yer: yeni bir WorkManager işi (en az 15 dk, 30 dk makul).
  - Kontrol: bildirim erişimi hâlâ verili mi (`enabled_notification_listeners`
    içinde paket var mı; Watomatic), son geri çağrı ne zaman (DoNotNotify).
    Sorun varsa 24 saatte en çok 1 yerel bildirim: "TekPanel mesajları şu an
    alamıyor. Dokunun, düzeltelim."
  - `DiagnosticsRecorder` şu an bellekte; son bağlantı/kopma/geri çağrı
    zamanlarını DataStore'a yaz (notisave `capture_health` benzeri).
- 3) Ana ekranda bağlantı durumu rozeti ve "Test bildirimi"
  - Bağlanacağı yer: gelen kutusu başlığı + `DiagnosticsScreen`.
  - Test: TekPanel kendi paketinden özel kimlikli bildirim yayınlar, 5 sn
    içinde yakalandı mı kontrol eder (notisave `NotificationTestManager`).
    Şu an kendi paketini filtreliyor; test kimliğine istisna gerekir.
- 4) Üretici rehberi (Türkçe, adım adım) + "Kısıtlanmış ayarlar" rehberi
  - Bağlanacağı yer: ilk kullanım akışı (görev #18) ve "Sorun giderme" ekranı.
  - Üretici algıla (`Build.MANUFACTURER`) -> doğru ayar ekranını `resolveActivity`
    ile yokla, yoksa uygulama bilgisine düş (DoNotNotify OemAutostart).
    Görsel adımlar için kendi ekran görüntülerimizi yaparız (Viet200103 fikri).
  - Kısıtlı ayar: yalnızca APK ile dağıtılan sürümde göster. Yükleyen mağaza
    Play ise gösterme (notisave mantığı + F-Droid issue'su). Bildirim erişim
    ayarına derin bağlantı: `ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS`
    (API 30+; notisave).
  - Hedef üreticiler: Samsung (Uyuyan/Derin uyuyan uygulamalar), Xiaomi/Redmi/
    POCO (Arka planda otomatik başlatma, pil "Kısıtlama yok", son uygulamalarda
    kilit), Oppo/Realme/OnePlus, Vivo. Menü adları sürüme göre değişir.
    https://dontkillmyapp.com/xiaomi
    https://dontkillmyapp.com/samsung
    https://dontkillmyapp.com/oppo
  - Türkiye'deki marka dağılımı bu kolun kapsamı dışında. [doğrulanamadı]
- 5) Pil muafiyeti: yalnız genel liste
  - `Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS` kullan. Doğrudan
    `ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` + izin, Play politikasında
    yalnızca çekirdek işlevi bozulan kategorilere açık; TekPanel'in o kategoriye
    girdiği garanti değil.
    https://developer.android.com/training/monitoring-device-state/doze-standby
- 6) Kaldırma nedeni (REASON) ve "kaynakta okundu" bilgisi
  - Bağlanacağı yer: yeni `onNotificationRemoved(sbn, rankingMap, reason)` ->
    `InboxMessageEntity`'ye `sourceRemovedReason` ve zaman.
  - Kullanım: REASON_CLICK = müşteri mesajı başka yerde açıldı; rozetle göster
    ("WhatsApp'ta açıldı"). Panelden otomatik silme YOK; kurucu "okundu"yu
    yalnız kullanıcı basınca istiyor (ISTEKLER I-xx).
  - Kaynak: pingbox NotificationCaptureService, AOSP REASON_* sabitleri.
- 7) Silinen müşteri mesajı
  - İşletme için değerli ("müşteri mesajı silip sipariş/randevu değiştirdi mi?").
  - pingbox yöntemi: yer tutucu metin ("Bu mesaj silindi" türü) -> kesin silindi;
    mesaj bildirimden kayboldu -> muhtemelen silindi. İngilizce listeyi
    Türkçe/Arapça/Fransızca ile genişletmek şart. WhatsApp'ın Türkçe tam
    metni [doğrulanamadı]; gerçek cihazdan toplanmalı.
  - İkinci sürüm özelliği; önce 1-5.
- 8) 1024 karakter sınırı için dürüst arayüz
  - Bir mesajın uzunluğu 1000 karakteri geçerse "Mesaj kısaltılmış olabilir"
    rozeti + "WhatsApp'ta aç" düğmesi (zaten `contentIntent` var).
  - `EXTRA_BIG_TEXT`, `EXTRA_TEXT_LINES` ve MessagingStyle mesajlarını birlikte
    değerlendirmeye devam; ama 1024'ün ötesini geri getiremeyiz.
  - Kurucunun "tam mesaj görünsün" isteği (I-33) bu sınır yüzünden %100 garanti
    edilemez; satış metninde "tam mesaj" yerine "bildirimde gelen mesaj"
    denmeli. [karar kurucuya ait]
- 9) Test: notisave senaryolarını bizim fixture'a çevir
  - Senaryolar: sekiz birikimli mesaj, iki aynı "OK", tarihçe mesajına geçiş,
    kayan pencere, aynı bildirim anahtarının yeniden kullanımı, yalnız özet
    bildirimi, InboxStyle, uzun Unicode, grup çocukları + özet, 100'lük hızlı
    patlama.
    https://github.com/abhinavsha077/notisave/blob/3b1865c/docs/TEST_MATRIX.md
  - Bağlanacağı yer: `CaptureCoordinatorRobolectricTest` + androidTest
    (görev #21: `cmd notification post` ile uçtan uca).
  - Özellikle sınanacak TekPanel riski: zamansız mesajlarda 60 sn kovası iki
    gerçekten ayrı "Tamam" mesajını birleştirebilir (`Fingerprint.forUntimedMessage`).
- 10) Gürültü kuralları veri olarak
  - `PromotionalHeuristics` şu an kodda ve küçük. DoNotNotify'ın hazır kural
    paketleri gibi uygulama başına JSON kural dosyası (Türkiye: kargo, banka,
    operatör, "canlı yayın" kalıpları); uygulama güncellemesiyle gelir, internet
    gerekmez.
    https://github.com/anujja/DoNotNotify
- 11) Saklama süresi + toplu silme
  - Seçenek: 7 / 30 / 90 gün / süresiz (notisave, NotifPlus, vault). WorkManager
    günlük temizlik; ayarlar yüklenmeden silme yapma (notisave dersi).
  - Toplu silmeyi parça parça yap, bitince sayıyı göster (vault #28 dersi).
- 12) Günlük özet ve "sonra hatırlat"
  - pingbox akşam özeti ve hatırlatıcı: "Bugün 12 müşteri mesajı, 3'ü cevap
    bekliyor." Alarmları `onListenerConnected` içinde yeniden kur (boot izni
    gerekmez; pingbox).
  - Küçük işletme için en doğal "iş akışı" parçası.
- 13) Avatar için kalıcı yedek
  - QQ `AvatarManager` fikri: grup sohbetinde bildirim avatar taşımıyorsa o
    kişinin son bilinen avatarını göster. `AndroidNotificationMedia.avatar`
    şu an yalnız o bildirime bakıyor.
- 14) Arama
  - ActivityTrace FTS5 / notisave FTS4. Room 2.6.1 `@Fts4` ile kolay başlar.
    Konuşma başlığı, gönderen, metin alanları.
- 15) Gizlilik katmanı (ikinci aşama)
  - Biyometrik kilit + ekran görüntüsü koruması (FLAG_SECURE): notisave,
    NotifPlus, vault. SQLCipher: 3 projede var; anahtar kaybı ve yedekleme
    karmaşası getirir. İlk sürüm için gerekli değil; DB zaten yedekten hariç
    (`backup_rules.xml`).
- 16) Android 15 OTP gizleme
  - Güvenilmeyen dinleyiciye OTP içeren bildirimin içeriği gelmez. Müşteri
    mesajlarını etkilemez ama "boş mesaj" olarak görünebilir; `EMPTY_CONTENT`
    kararının tanıda nedenini göster.
    https://developer.android.com/about/versions/15/behavior-changes-all

## Hiçbirinde olmayan, bizim fark yaratacağımız yerler

Bunlar "bu 26 projede bulamadık" bulgularıdır. Başka (kapalı kaynak) uygulamalarda
olabilir; Google Play kolu bunu ayrıca kontrol etmeli.

- Küçük işletme iş akışı: müşteri odaklı konuşma kutusu, "cevap bekliyor /
  cevaplandı", günlük özet. Rakiplerin hepsi kayıt tutucu, filtre veya
  yönlendirici. Hiçbirinde müşteri, sipariş, randevu kavramı yok.
  En yakın: pingbox (özet, hatırlatma, yıldızlama), Notification-HQ (mesajı
  gruba yönlendirme).
- Kanal seçici deneyimi: kurucunun istediği "seçilenler üstte, en çok mesaj
  gelen üstte, WhatsApp / WhatsApp Business ayrı, paket adı görünmez, gerçek
  ikonlar". İncelediğimiz seçiciler alfabetik (SpeakThat A-Z / Z-A + sistem
  uygulamaları anahtarı; NotificationLog alfabetik / yeni / eski).
  "En çok mesaj gelen üstte" sıralaması bulunamadı.
  https://github.com/mitchib1440/SpeakThat/blob/1af2d9a/app/src/main/java/com/micoyc/speakthat/AppPickerActivity.kt
- Panelden doğrudan cevap: incelenenlerde RemoteInput'u yalnız Watomatic
  (otomatik cevap), SmartIsland (ada üzerinde) ve DoNotNotify (yığınlama
  kopyası) kullanıyor. Kayıt tutucuların hiçbiri cevap vermiyor. TekPanel'in
  `LiveActionRegistry` yapısı bu yüzden fark.
- "Yakalama güvenilir mi" sorusuna tek ekranda cevap: parçalı çözümler var
  (notisave sağlık ekranı, DoNotNotify sihirbazı, wiki rehberleri) ama kullanıcı
  dostu, markalı, Türkçe ve tek akışta birleşmiş olanı yok.
- İnternetsiz + ticari paket: açık kaynakların çoğu GPL, hobi ölçeğinde.
  Ticari örnek ReNotify (GPL + Play'de Pro). İnternet iznini hiç istemeyen bir
  ücretli ürün bu grupta yok. [tek kaynak, kod yorumu]
- Platform sınırlarını kullanıcıya açık söylemek (1024 karakter, bildirimde
  görünmeyen içerik, OTP gizleme): hiçbiri anlatmıyor. Güven farkı yaratır.
- Türkçe/yerel dil: yalnız WhatsDeleted README'sinde Türkçe çeviri var;
  hiçbirinde Türkçe silinen-mesaj yer tutucusu veya Türkiye'ye özgü gürültü
  kalıbı yok.

## Denenen ve çalışmayan yollar

- GitHub sayfalarını `curl` ile çekmek (yıldız sayısı için): proxy 403 verdi.
  Çözüm: WebFetch ile sayfa okumak. GitHub API'ye, `gh` komutuna ve MCP
  araçlarına kural gereği dokunulmadı.
- Resmi doküman sayfası `developer.android.com/reference/.../
  NotificationListenerService`: sayfa 335-390 bin karakter; araç yalnız
  ilk 100 bini okuyup menüde kaldı. Çözüm: aynı belgeyi AOSP kaynağından
  (android.googlesource.com, `?format=TEXT`) indirip sınıf yorumlarını okumak.
- Reddit ve Stack Overflow: arama aracı bu alan adlarını engelledi; WebFetch de
  "alınamıyor" dedi. Yerine GitHub issue'ları, F-Droid issue'su, Google grubu,
  XDA, dontkillmyapp.com, OneSignal kullanıldı. Reddit/SO'dan görüş [doğrulanamadı].
- Stackademic makalesi, Samsung Community "Notistar issue" ve AndroidAuthority
  Android 13 makalesi: WebFetch 403 verdi. İçerikleri yalnız arama özetinden
  biliniyor; bulgulara dayanak yapılmadı.
- Google IssueTracker 75414169 ("bilinen Android hatası", Automate grubunda
  anılıyor): giriş gerektiriyor, içeriği okunamadı.
- Sophos yazısı (Android bildirim geçmişi ~100 karakter, birkaç saat, yeniden
  başlatınca silinir iddiası): adres 301 ile 404'e gitti. İddia [doğrulanamadı];
  rapora sayısal olarak alınmadı.
- Nevolution deposu (github.com/oasisfeng/nevo): 404.
- "NotiStar GitHub": NotiStar Samsung'un kapalı kaynak Good Lock modülü; açık
  kaynak karşılığı bulunamadı. Yakın akraba: NotifPlus kendini "NotiStar tarzı
  dayanıklılık" diye tanımlıyor.
- WebSearch özetleri bazı yerlerde eski sayı verdi (Alfio010 için 17 yıldız;
  gerçek sayfa 223). Sayılar yalnızca GitHub sayfa okumasından alındı.
- Hiçbir projeyi derleyip çalıştırmadık (Android SDK/cihaz yok). Davranış
  iddiaları kod okumasına dayanır; çalışma zamanı doğrulaması yok.

## Doğrulanamayanlar ve güvenilirlik notları

- Üretici ayar yolları (menü adları, activity bileşenleri) sürümle değişir;
  DoNotNotify'ın listesi gerçek cihazlarda bu turda sınanmadı.
- Bileşen aç/kapat hilesinin gerçekten bağlantıyı geri getirdiği: üç projede
  uygulanıyor ama resmî dokümanda yok; etkisi [doğrulanamadı].
- Android 13 etiketinde 1024 sınırı: bakılmadı. 8, 10, 11(r1) = 5120; 11(r48),
  12, 14, main = 1024.
- WhatsApp'ın uzun mesajı kendi tarafında bildirimde ne kadar kestiği:
  [doğrulanamadı].
- WhatsApp Türkçe "bu mesaj silindi" tam metni: [doğrulanamadı].
- `FLAG_FILTER_TYPE_CONVERSATIONS` gibi dinleyici filtre türlerinin hangi API
  seviyesinde kullanılabildiği: [doğrulanamadı].
- Yıldız ve issue sayıları özetleyici modelden geçti; küçük sapma olabilir.
  SmsForwarder açık issue sayısı README ve listede tutarsız.
- Çoğu issue için başlıklar okundu, gövdeler değil (gövdesi okunanlar kartlarda
  ve şikayetler bölümünde "Gövde okundu" diye işaretli).
- Tek yazarlı yeni projelerin (notisave, NotifPlus, ReNotify, pingbox) README
  iddiaları kodla kısmen eşleşti ama cihazda sınanmadı.

## TekPanel için çıkarımlar

### Ne yapalım

- Yakalama hattının "kendi kendini onarma" katmanını ekleyelim (kademeli
  rebind, sınırlı bileşen aç/kapat, 30 dk WorkManager kontrolü, tek bildirim).
  Neden: en çok tekrar eden şikayet bu; satış sonrası destek yükünü en çok
  bu azaltır. [çoklu]
- Sağlık durumunu kalıcı yapıp ana ekranda gösterelim; "test bildirimi"
  düğmesi ekleyelim. Neden: kullanıcı "çalışıyor mu" sorusunu kendi görsün;
  destek taleplerini keser (notisave, DoNotNotify).
- Türkçe üretici rehberi + kısıtlı ayarlar rehberi yapalım; menü adlarını gerçek
  Samsung / Xiaomi / Oppo cihazlarında bizim ekran görüntülerimizle doğrulayalım.
  Neden: dinleyici açılamayan kullanıcı ürünü iade eder.
- 1024 karakter sınırını arayüzde dürüstçe gösterelim. Neden: kurucunun
  "yarım mesaj" şikayetinin bir kısmı platform sınırı; söz verip tutamamaktan
  iyidir.
- `onNotificationRemoved` + REASON kaydedelim (yalnız bilgi rozeti). Neden:
  "müşteri mesajı başka yerde okundu" bilgisi iş akışı farkı yaratır
  (pingbox). Panelden otomatik silme yapmayalım.
- Test setini notisave'in fixture senaryolarıyla genişletelim ve gerçek
  cihaz matrisini (Samsung, Xiaomi, Oppo/Realme, Pixel, düşük RAM) çalıştıralım.
  Neden: açık kaynak rakiplerin en zayıf yanı gerçek cihaz doğrulaması.
- Gürültü kurallarını veri dosyasına çıkaralım (Türkiye kalıpları). Neden:
  kod değiştirmeden güncellenir; DoNotNotify 40+ kuralla bunu kanıtlıyor.
- Günlük özet + "sonra hatırlat" ekleyelim (ikinci aşama). Neden: kayıt tutucu
  ile iş aracı arasındaki fark.
- Silinen mesaj tespitini TR/AR/FR yer tutucularıyla ikinci sürümde yapalım.
  Neden: işletme için değerli; yer tutucular önce gerçek cihazdan toplanmalı.

### Ne yapmayalım

- Canlı tutma hileleri: sessiz müzik, 1 piksel etkinlik, Shizuku/ADB onarımı.
  Neden: Play politikası riski ve son kullanıcı yapamaz (SmsForwarder,
  Service-Keeper).
- Kaynak bildirimi iptal edip kopyasını yeniden yayınlamak (sessizleştirme /
  yığınlama). Neden: RemoteInput ve MessagingStyle kaybolur; cevap özelliğimiz
  ölür (ReNotify, DoNotNotify belgesi).
- Erişilebilirlik servisiyle yakalama. Neden: güven ve inceleme riski
  (ActivityTrace, NotificationLog).
- Ağ: otomatik cevap, yapay zekâ, webhook, telemetri, kendini güncelleme.
  Neden: "veriler cihazdan çıkmaz" vaadimiz (Watomatic, Notification-HQ,
  pingbox GitHub sürümü, forwarder'lar).
- Geniş izinler: QUERY_ALL_PACKAGES, SMS, rehber, konum, kamera. Neden: Play
  inceleme ve güven riski (message-mirror, SmsForwarder). TekPanel şimdi
  `<queries>` ile çalışıyor; öyle kalsın.
- Tek devasa servis sınıfı (SpeakThat 8.500 satır). Neden: test edilemez.
- Kalıcı debug log (voicenotify #168, #166). Neden: depolama aşınması ve CPU.
- Copyleft koddan kopya ve başka ürünün adı. Neden: lisans ve marka riski;
  13 projeden 13'ü GPL/AGPL. İzinli lisanslılar bile atıf ister; kopya yapmıyoruz.
- Doğrulanmamış büyük özellikleri (SQLCipher + HMAC + LCS) ilk sürüme
  doldurmak. Neden: notisave bile cihazda sınamadan "yayın şartı" diyor.
- Ana ekranda regex veya "kural yazdırma" arayüzü. Neden: berber/kuaför
  kitlesi kullanmaz (push-filter, NotiFilter karmaşası).

## Temizlik ve lisans notu

- Klonlar yalnızca şu klasörde: 
  /tmp/claude-0/-home-user-Jn/422ffb5b-fa2e-516a-a2cc-64c9eb14ac8a/scratchpad/arastirma/github-rakipler/clones
- Başkalarının ekran görüntüleri ve görsel tasarımları (README görselleri,
  mağaza ekran görüntüleri) not alındıktan sonra silindi; yalnız kod bırakıldı.
- TekPanel deposunda bu dosyadan başka hiçbir dosya değiştirilmedi; commit ve
  push yapılmadı.
- AOSP kaynak kopyaları (Apache-2.0) yalnızca okuma için scratch klasöründe.
