# OEM pil kısıtları ve dinleyici servis güvenilirliği

Araştırma kolu: oem-guvenilirlik

Tarih: 21 Ekim 2026. Bu dosya araştırma çıktısıdır. Cihazda deneme
yapılmadı; her şey doküman, kaynak kodu ve geliştirici/kullanıcı
raporlarının okunmasından çıktı.

## Nasıl okunur

- Etiketler (her maddenin sonunda):
  - [resmi] Android veya üretici belgesi.
  - [AOSP kodu] Android kaynak kodunu bizzat okudum.
  - [çoklu] Birbirinden bağımsız 2+ kaynakta tekrar ediyor.
  - [tek kaynak] Yalnız bir yerden geldi, sağlam sayma.
  - [topluluk] Forum, issue, kullanıcı yorumu, açık kaynak proje.
  - [blog] Blog, üçüncü taraf rehber veya istatistik sitesi.
  - [çıkarım] Kaynaklardan benim çıkardığım sonuç; cihazda sınanmadı.
  - [doğrulanamadı] Bulamadım veya sayfayı okuyamadım.
- AOSP okumaları "main" dalının bugünkü hâli. Bir davranışın hangi
  Android sürümünde girdiğini çoğu yerde doğrulayamadım.
- 02-04 numaralı dosyalarda anlatılanı tekrar etmedim. Orada olanlar:
  - requestRebind, kademeli geri alma, aç/kapat hilesi fikri (02).
  - Sağlık şeridi, test bildirimi fikri (02, 03).
  - dontkillmyapp puanları (03). Burada puanları kısa anıp üstüne
    ekledim.
- Lisans notları ilgili maddenin yanında. Hiçbir kod kopyalanmadı;
  amaç sistemi anlamak.
- Okuduğum Sende dosyaları (bu turdaki hâliyle):
  - capture/SendeNotificationListenerService.kt
  - capture/BootRestartReceiver.kt, capture/MessageFilterEngine.kt
  - util/ListenerHealth.kt, util/OemHints.kt
  - util/NotificationAccessChecker.kt
  - diagnostics/DiagnosticsRecorder.kt, ui/access/AccessBanner.kt

## Kısa sonuç

- Dinleyiciyi uygulama değil sistem bağlar. Uygulama ölürse
  yeniden bağlama işi sistemdedir; biz sadece yardım edebiliriz.
- `requestRebind()` Android kodunda, bileşen daha önce
  `requestUnbind()` ile "uykuya" alınmadıysa hemen geri dönüyor.
  Yani süreç öldükten sonra tek başına çoğu zaman bir şey yapmıyor.
  [AOSP kodu]
- Sistemin kendi kurtarması var: bağlantı ölünce 10 sn sonra yeniden
  bağlanmayı dener. Üretici katmanları bunu bozabilir. [AOSP kodu]
- Aç/kapat (toggle) hilesi bu kurtarmayı tetikliyor gibi görünüyor ve
  geliştiricilerin yaygın yöntemi. Ama belgesiz ve riskleri var. [çoklu]
- Ayarlar garanti değildir. Xiaomi 15 Ultra, HyperOS 3, Android 16'da
  pil "sınırsız" ve tüm izinler yeşilken dinleyici kopuk kaldı
  (Eylül 2026). [tek kaynak]
- "İzin verildi" ile "bağlı" farklı şeylerdir. Üç ayrı projede aynı
  hata görüldü. [çoklu]
- Kaçan bildirimi geri getirmek yalnızca tepsideki bildirimler için
  mümkün (`getActiveNotifications`). Kullanıcı sildiyse yok. [resmi]
- Play'de doğrudan pil muafiyeti diyaloğu yasak/riskli; "pil
  optimizasyonu listesi" ekranına yönlendirmek serbest. [resmi]
- Türkiye'de tanınan Android cihazların yaklaşık dörtte üçü Samsung ve
  Xiaomi/Redmi/POCO. Önce bu ikisinin rehberi mükemmel olmalı. [blog]
- En değerli iş: kalıcı sağlık kaydı + boşluk raporu + tepsi
  uzlaştırması + marka rehberi. Bunların hiçbiri izin istemez.

## 1. Sistem dinleyiciyi nasıl bağlıyor (AOSP)

Kaynak dosyalar Apache-2.0 lisanslı (dosya başlıklarında yazıyor).

### Bağlama biçimi
- Sistem, onaylı dinleyiciyi `bindService` ile bağlar. Bayraklar:
  AUTO_CREATE, FOREGROUND_SERVICE, ALLOW_WHITELIST_MANAGEMENT ve
  NOT_PERCEPTIBLE. [AOSP kodu]
  https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
- Kodda yorum: NOT_PERCEPTIBLE eklendi çünkü "çok fazla üçüncü taraf
  uygulama dinleyici olarak bellekte tutulup aşırı bellek baskısı
  yaratabiliyor". [AOSP kodu]
- NOT_PERCEPTIBLE belgesi: bağlayan görünür/algılanır durumdaysa hedef
  servisin önemini algılanır seviyenin altına indirir. [resmi]
  https://developer.android.com/reference/android/service/notification/NotificationListenerService
- Sonuç: en saf Android'de bile dinleyici süreci bellek baskısında
  önceliksizdir ve öldürülebilir. Üretici katmanı bunu daha da
  sertleştirir. [çıkarım]
- Düşük RAM'li cihazlarda Android Q ve altında dinleyici bağlanamaz.
  İş profilindeki dinleyici yok sayılır. [resmi] (aynı bağlantı)

### Süreç ölünce ne oluyor
- Sistem bir servisi indirdiğinde (ör. bileşen devre dışı kalınca,
  zorla durdurmada) bağlantılara "öldü" bildirimi gidiyor
  (`connected(name, null, dead=true)`). Süreç doğrudan öldürülünce
  de aynı yolun işlediğini ayrıca doğrulamadım. [AOSP kodu]
  https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/am/ActiveServices.java
- Sistemin dinleyici yöneticisi `onBindingDied` alınca bağlantıyı
  bırakır ve 10 000 ms sonra yeniden kaydeder. Zaten bir yeniden
  deneme bekliyorsa ikincisini atlar. [AOSP kodu]
  https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/ManagedServices.java
- `onServiceDisconnected` yalnızca günlüğe yazıyor, başka bir şey
  yapmıyor. [AOSP kodu] (aynı dosya)
- `onBindingDied` belgesi: bağlantı artık geri gelmez, bağla-çöz-bağla
  gerekir; örnek: servisi barındıran uygulama güncellendi. [resmi]
  https://developer.android.com/reference/android/content/ServiceConnection
- Zaten "bağlı" sayılan bir bileşen için ikinci kayıt reddedilir
  ("is already bound"). Sistem bağlı sanıp gerçekte kopuksa yeniden
  bağlama tetiklenmez. Bu, bazı telefonlarda kopukluğun kalıcı
  olmasının makul açıklaması. [AOSP kodu + çıkarım]

### `requestRebind` gerçekte ne yapıyor
- Belge: yalnızca `requestUnbind()` sonrası yeniden bağlamak içindir;
  izni olmayan dinleyicide başarısız olur; `onListenerConnected`
  öncesi ve `onListenerDisconnected` sonrası çağrılabilen tek
  yöntemdir. [resmi]
  https://developer.android.com/reference/android/service/notification/NotificationListenerService
- Kod: `requestBindListener` → `setComponentState(bileşen, kullanıcı,
  true)`. Bileşen "uyku (snooze)" listesinde değilse, durum zaten
  "etkin" olduğundan fonksiyon hemen döner. Listeye yalnız şu iki
  durumda girer: uygulama `requestUnbind` çağırdıysa veya manifestte
  autobind=false (Android 14 meta verisi) varsa. [AOSP kodu]
- Yani süreç öldükten sonra çağrılan `requestRebind` çoğu durumda
  boş işlem. Bir rakip projenin yorumu aynısını söylüyor: "sistem
  bağlantıyı bıraktıktan sonra çoğu zaman yok sayılıyor". [topluluk]
  (ReNotify, GPL-3.0; 02 numaralı dosyada da var)
- Zararsız ve bedava; her kopmada çağırmak doğru, ama tek çare değil.

### Servis içindeki geri çağrılar
- `onDestroy()` içinde AOSP `onListenerDisconnected()` çağırıyor.
  Sende'nin `onDestroy`'unda `serviceScope.cancel()` var; servis
  içinde zamanlanan her deneme bu yüzden ölür. [AOSP kodu]
  https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/service/notification/NotificationListenerService.java
- Tüm bildirim geri çağrıları ana iş parçacığındaki bir Handler'dan
  gelir ve bağlantı bayrağı yoksa sessizce atılır. [AOSP kodu]
  Ana iş parçacığını meşgul eden uygulama olayları geç görür.
- Bağlı değilken `getActiveNotifications()` günlüğe "not yet bound"
  yazıp boş dizi döner. [AOSP kodu]
- Süreç öldürülünce (bellek, üretici, force-stop) `onDestroy` ve
  `onListenerDisconnected` hiç çağrılmaz. Uygulama kopuşu ancak sonradan
  öğrenebilir. [çıkarım]

### Paket değişikliği ve aç/kapat hilesi
- Paket veya bileşen durumu değişince sistem `PACKAGE_CHANGED` yayını
  alır; dinleyici yöneticisi "güncellenmiş olabilir" diye tüm
  dinleyicileri yeniden bağlamayı dener. Kodda yorum: "make sure we're
  still bound to any of our services who may have just upgraded". [AOSP kodu]
- Bir bileşen devre dışı bırakılınca sistem o bileşenin servislerini
  indirir (`bringDownDisabledPackageServicesLocked`); bu da bağlantıya
  "öldü" bildirimi gönderir. 10 sn'lik yeniden kayıt devreye girer. [çıkarım]
  Bu çıkarım bir 2013 Stack Overflow cevabıyla uyuşuyor: aç/kapat
  yöntemi "yaklaşık 10 sn sürer". [tek kaynak]
  https://stackoverflow.com/questions/20198283/notificationlistenerservice-stopping-and-cant-be-restarted-without-a-reboot
- Risk (kodu okuyarak): aynı paket değişikliği işlenirken, onaylı
  listedeki bir bileşenin servis bilgisi bulunamazsa ("bind izni
  yok" sayılıp) kullanıcının verdiği onay listeden silinir. Servis
  bilgisi sorgusu "devre dışı bileşenleri eşleştir" bayrağını
  kullanmıyor. Yani bileşen o anda DISABLED ise onay kaybolabilir.
  Yayın asenkron işlendiği için dar bir yarış penceresi. [AOSP kodu + çıkarım]
  Kullanıcı raporu bulamadım; riski gerçek ama küçük sayıyorum.
  [doğrulanamadı: cihazda]

### Gözlem için bayraklar
- `dumpsys notification` çıktısında şu bölümler var: "Allowed
  notification listeners", "Live notification listeners (N)",
  "Snoozed notification listeners (N)". Bağlı olup olmadığı "Live"
  satırından görülür. [AOSP kodu]
- `cmd notification allow_listener / disallow_listener COMPONENT` ile
  izin adb'den açılıp kapanır. [AOSP kodu]
  https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationShellCmd.java

## 2. Gerçek hayatta neden ölüyor

### dontkillmyapp.com ne diyor
- Puanlar (5 en kötü): Xiaomi 5, Samsung 5, OnePlus 5, Huawei 5,
  Oppo 4, Asus 4, Vivo 3, Tecno 3, realme 3, Motorola 3, Sony 2;
  AOSP/Pixel, Nokia, HTC 0. [topluluk]
  https://dontkillmyapp.com/
- Sitede Honor, Nothing, Infinix sayfası yok (404). HyperOS ve
  Android 15/16 için ayrı içerik yok; Xiaomi sayfası MIUI 14'te
  kalmış. [topluluk]
  https://dontkillmyapp.com/xiaomi
- Site "geliştirici çözümü yok" diyor: Samsung, Oppo, Vivo, realme,
  Tecno, OnePlus, Xiaomi (sadece MIUI autostart kontrolü için bir
  kütüphane). [topluluk]
  https://dontkillmyapp.com/samsung
- Samsung sayfası: Temmuz 2024 güncellemesinde Samsung, One UI 6.0'dan
  beri Android 14 hedefli uygulamaların ön plan servislerinin
  "amaçlandığı gibi çalışacağını" vaat etmiş. Vaat ön plan
  servisleri hakkında; dinleyiciden söz etmiyor. [topluluk]
- Samsung: kullanılmayan uygulama 3 gün sonra arka planda
  başlatılamaz hâle geliyor; ayarı elle kapatsan bile bellenim
  güncellemesinden sonra tekrar listeye ekleyebiliyor. [topluluk]
  https://dontkillmyapp.com/samsung
- OnePlus: pil ayarı rastgele geri dönüyor; uygulamayı son
  uygulamalarda kilitlemek bunu azaltıyor ama %100 değil. [topluluk]
  https://dontkillmyapp.com/oneplus
- Site güncel değil: Oppo sayfası için Haziran 2026'da bir kullanıcı
  "yeni ColorOS'ta bu seçeneklerin hiçbiri yok" yazmış; Tecno sayfası
  için Ekim 2026'da "HiOS 14/15 yolları farklı" bildirilmiş. [topluluk]
  https://github.com/urbandroid-team/dont-kill-my-app/issues/4116
  https://github.com/urbandroid-team/dont-kill-my-app/issues/4394
- Sitenin JSON arayüzü var (`/api/v2/<üretici>.json`) ama kullanmak
  internet gerektirir. Bizde INTERNET izni yok; kullanılamaz.
  https://dontkillmyapp.com/apidoc

### Üreticilerin kendi açıklamaları
- Samsung: "Uyku modundaki uygulamalar aralıklarla arka planda
  çalışabilir; güncellemeler ve bildirimler gecikebilir. Derin uykuya
  alınmış uygulamalar arka planda çalışmaz; bildirimleri yalnızca
  uygulamayı açtığınızda alırsınız." [resmi]
  https://www.samsung.com/tr/support/galaxy-battery/optimization/
- Honor: "Arka planda çalışan uygulamalar pil optimizasyonuyla
  temizlenebilir"; çözüm olarak başlatma ayarı, pil optimizasyonu ve
  son uygulamalarda kilit. Giriş seviyesi cihazlarda kilitli
  uygulamalar yine de temizlenebilir. Ve: "Bu sorun Android'de yaygın;
  Honor iyileştirmeye devam edecek." [resmi]
  https://www.honor.com/za/support/content/en-us00428704/
  https://www.honor.com/global/support/content/en-us00406916/
- vivo (resmi hesap, 2022): Ayarlar > Pil > "Yüksek arka plan güç
  kullanımı" ile uygulamaların arka planda çalışmasına izin verilir.
  [resmi]
  https://bbs.vivo.com/in/thread/11952
- realme Türkiye destek sayfası: bildirim gelmiyorsa bildirim izni,
  son uygulamalarda kilit ve "Telefon Yöneticisi > Gizlilik İzinleri >
  Başlangıç Yöneticisi" açılmalı. [resmi, eski menü]
  https://www.realme.com/tr/support/troubleshooting

### Stack Overflow (2013-2019): hep aynı hikâye
- 2013: dinleyici durur, yalnız yeniden başlatmayla düzelir; günlükte
  Ayarlar'ın "zorla durdur" ekranı açılınca sistemin dinleyiciyi
  kapatıp açtığı görülüyor. Cevap: aç/kapat, "yaklaşık 10 sn". [tek kaynak]
  https://stackoverflow.com/questions/20198283/notificationlistenerservice-stopping-and-cant-be-restarted-without-a-reboot
- 2016: "bilinen hata; güncellemede ve bazen rastgele ölüyor, yeniden
  başlamıyor." Cevaplar: ön plan servisi, START_STICKY, aç/kapat
  (kabul edilen). [topluluk]
  https://stackoverflow.com/questions/35434171/workaround-for-keeping-notificationlistenerservice-alive
- 2017: birkaç saat sonra durur; yeniden başlatma veya erişimi kapat-aç
  düzeltir. Cevap: izleme servisi + aç/kapat. [topluluk]
  https://stackoverflow.com/questions/47294793/onnotificationposted-not-being-called-after-some-time
- 2018, Huawei Mate 10 Pro, Android 8.0: Doze değil; `onDisconnected`
  birkaç dakika sonra geliyor. Android 7.0 tablette seyrek kopuyor
  ve hemen yeniden bağlanıyor. [tek kaynak]
  https://stackoverflow.com/questions/49622813/android-notificationlistenerservice-background-execution-limit-doze
- 2018: kurulumdan 2-3 gün sonra duruyor; erişimi elle kapat-aç
  gerekiyor. [tek kaynak]
  https://stackoverflow.com/questions/53645250/how-to-trigger-bind-notification-listener-service-after-it-stops-automatically
- 2019, Xiaomi: uygulama kapanınca servis ölüyor. Aç/kapat yapınca
  birden fazla istemci (çoklu dinleyici örneği) oluştuğu ve işlemlerin
  çoğaldığı bildirilmiş. [tek kaynak]
  https://stackoverflow.com/questions/58183108/revive-notificationlisternerservice-on-xiaomi-devices
- Not: Stack Overflow'a Stack Exchange API'siyle ulaştım (anonim);
  Reddit'e ulaşılamadı (bkz. "Denenen ve çalışmayan yollar").

### 2026 raporları (GitHub)
- Xiaomi 15 Ultra, HyperOS 3.0, Android 16 (Eylül 2026): Bildirim
  erişimi, kısıtlı ayar, bildirim izni, pil "sınırsız" hepsi yeşil;
  servis durumu "Disconnected". Günlükte kopma satırı yok. [tek kaynak]
  https://github.com/D4vidDf/HyperBridge/issues/330
- Aynı projenin düzeltmesi: sistem dinleyiciyi bırakınca uygulama
  yeniden bağlanmıyordu; yeniden bağlama yalnız açılışta, ekran
  kilidi açılınca ve elle yapılıyordu. Düzeltme: kopunca +3 sn ve
  +15 sn requestRebind, +45 sn aç/kapat + requestRebind, +60 sn karar;
  hâlâ kopuksa tek bildirim. Servisin dışında (süreç düzeyinde)
  çalışıyor, çünkü `onDestroy` servis kapsamını iptal ediyor. Cihazda
  sınanmamış (PR'da yazıyor). Lisans: Apache-2.0. [tek kaynak]
  https://github.com/D4vidDf/HyperBridge/pull/332
- Honor Magic V5, MagicOS (Ekim 2026): güncelleme ve yeniden
  başlatma sonrası dinleme duruyor, erişimi kapat-aç düzeltiyor.
  Yazar: servis "çalışıyor" ama "bağlı" değil; uygulama
  `requestRebind` hiç çağırmıyormuş. Testi: yeniden
  başlatma sonrası uygulamayı açmadan dinleme yalnız "otomatik
  başlat, ikincil başlat, arka planda çalış" açıksa çalıştı; kapalıysa
  sistem BOOT_COMPLETED'i uygulamaya hiç iletmedi. Lisans: GPL-3.0 veya
  üstü (kısmen Apache-2.0). [tek kaynak]
  https://github.com/metabrainz/listenbrainz-android/issues/772
- Bir harcama takip uygulamasının issue'su (tarihi doğrulanamadı):
  "izin verildi" göstergesi bağlı olduğunu göstermez. Zorla durdurma,
  yeniden başlatma, uygulama güncellemesi ve gece bekletme
  senaryolarını gerçek cihazda denemeyi öneriyor;
  `dumpsys notification --noredact` yardımcı. Lisans bilgisi
  bulunamadı. [tek kaynak]
  https://github.com/theprinceraj/qoody/issues/25
- Başka bir proje (Ekim 2026) yeniden bağlamayı açılışta, 22:00'de,
  WorkManager yedeğiyle ve ilk etkinleştirmede yapıyor; yalnız izin ve
  kullanıcı onayı varsa. [tek kaynak]
  https://github.com/dbrckk/Validate_steps/pull/9
- Bu kadar yeni raporun hepsi aynı sonuca varıyor: "bağlı mı"yı kendi
  bayrağınla izle, kopunca süreç düzeyinde kademeli kurtar, kullanıcıya
  haber ver. [çoklu]

## 3. Teknikler: işe yarar mı, riski ne

### 3.1 requestRebind
- Her `onListenerDisconnected`'da ve her uygulama açılışında çağır.
  Bedava. Ama sonucu sayma. (Bölüm 1.)
- Tekrarları süreç düzeyinde zamanla (Handler/uygulama kapsamı); servis
  kapsamı `onDestroy`'da iptal oluyor.

### 3.2 Aç/kapat (toggle) hilesi
- Kim kullanıyor: ReNotify (GPL-3.0; en çok 60 sn'de bir), HyperBridge
  (Apache-2.0), Notification-History-Android (02 dosyası), SO
  cevapları (2013-2019). [çoklu]
  https://github.com/Bahoz420/ReNotify-App-OpenSource/blob/2930863/app/src/main/java/com/renotify/app/service/ListenerGuard.kt
- Nasıl yapılır (özet): `setComponentEnabledSetting` ile DISABLED,
  sonra ENABLED; her ikisinde `DONT_KILL_APP`. [çoklu]
- Neden DONT_KILL_APP şart: bayrak olmadan bileşen durumu değişince
  sistem süreci öldürür; çıkış nedeni `REASON_PACKAGE_STATE_CHANGE`
  olarak kaydedilir. [resmi]
  https://developer.android.com/reference/android/app/ApplicationExitInfo
- Riskler:
  - Belgesiz; resmi bir söz yok. [çoklu]
  - 10 sn kadar sağır pencere. [tek kaynak]
  - Onay kaybı yarışı (Bölüm 1). [AOSP kodu + çıkarım]
  - Çoklu örnek raporu (Xiaomi, 2019). Sende'de çift kayıt, parmak
    izi ve benzersiz indeks sayesinde olmaz; yine de sınanmalı. [tek kaynak]
  - Sık çağrılırsa gereksiz sistem yayını. En az 60 sn ara + günlük
    sınır koy.
  - Kapatıp açma arasında bekleme yok: şimdiki Sende kodu iki çağrıyı
    art arda yapıyor (util/ListenerHealth.kt). Kısa bir ara (1-2 sn)
    yarışı küçültür. [çıkarım]
  - Üretici sürecin kendisini donduruyorsa çare değil.
- Play: kendi bileşenini açıp kapatmak "Cihaz ve Ağ Kötüye
  Kullanımı" örnek listesinde yok (liste başkasının uygulamasına, sisteme
  müdahaleyi sayıyor). [çıkarım]
  https://support.google.com/googleplay/android-developer/answer/9888379
- Karar: yalnız ikinci basamak; önce kademeli requestRebind.

### 3.3 BOOT_COMPLETED ve MY_PACKAGE_REPLACED
- Sistem zaten açılışta (onaylı listeyi okuyunca) ve kullanıcı kilidi
  açılınca dinleyicileri yeniden bağlıyor (`rebindServices`). [AOSP kodu]
- Üretici "otomatik başlat" kapalıysa uygulamaya BOOT_COMPLETED hiç
  gelmeyebilir (Honor testi). [tek kaynak]
  https://github.com/metabrainz/listenbrainz-android/issues/772
- Android 15: kullanıcı uygulamayı "durdurulmuş" durumdan çıkarınca
  uygulamaya BOOT_COMPLETED yayını teslim ediliyor; yani zorla
  durdurma sonrası ilk açılışta alıcı çalışır. [resmi]
  https://developer.android.com/about/versions/15/behavior-changes-all
- Android 15: BOOT_COMPLETED alıcıları dataSync, camera, mediaPlayback,
  phoneCall, mediaProjection, microphone türü ön plan servisi
  başlatamaz. [resmi]
  https://developer.android.com/about/versions/15/behavior-changes-15
- Sonuç: alıcı kalsın; işi kısa tut (kontrol + zamanlama). Güncelleme
  sonrası için MY_PACKAGE_REPLACED de ekle. Sistem güncellemeyi zaten
  yeniden bağlıyor ama üretici katmanı atlayabilir. [çıkarım]

### 3.4 Ön plan servisi (sürekli bildirimli "canlı tut")
- Geliştiricilerin en eski tavsiyesi (2016, 2021 SO cevapları). [topluluk]
  https://stackoverflow.com/questions/35434171/workaround-for-keeping-notificationlistenerservice-alive
- Teknik mantığı var: dinleyici NOT_PERCEPTIBLE ile bağlı, kendi ön
  plan servisimiz süreç önemini yükseltir. [çıkarım]
- Play politikası (Android 14+): ön plan servisi türü bildirilecek;
  yalnızca kullanıcıya fayda, çekirdek işleve ilgili, kullanıcı
  başlatmış veya algılanır, kullanıcı durdurabilir, sistem
  ertelerse işlev bozulur, "yalnız gerektiği kadar" çalışır. Uymayan
  kullanım için `specialUse` türü Play Console'da açıklama + demo
  videosu ister. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9888379
  https://support.google.com/googleplay/android-developer/answer/13392821
- Sürekli, hep açık bir "canlı tut" servisi "yalnız gerektiği kadar"
  ölçütüne takılabilir. Bu yoruma kesin hüküm değil. [çıkarım]
- Karar: Play sürümünde yok. Sideload'da kullanıcı açarsa seçenek
  olabilir (kapalı varsayılan).

### 3.5 Pil muafiyeti (REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
- Android belgesi: çoğu uygulama liste ekranını açan
  `ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS` kullanır; doğrudan
  diyalog yalnız kabul edilebilir kullanım senaryosu olanlara.
  Not: "Google Play, uygulamanın
  çekirdek işlevi olumsuz etkilenmedikçe doğrudan muafiyet istemeyi
  yasaklar". [resmi]
  https://developer.android.com/training/monitoring-device-state/doze-standby
- Belgenin "kabul edilebilir" tablosu: mesajlaşma/arama uygulaması FCM
  kullanabiliyorsa kabul edilmez; FCM kullanamıyorsa veya Doze çekirdek
  işlevi bozuyorsa kabul edilir; güvenlik, görev otomasyonu, çevre
  birim eşlik uygulaması kabul edilir; arada bir eşitleme kabul
  edilmez. Bildirim dinleyici/toplayıcı tabloda yok. [resmi]
- Play politikası: "güç yönetimini atlamaya çalışan ve izin listesine
  uygun olmayan uygulamalar" ihlal örneği; "Uygun değilsen güç
  yönetimini atlama." [resmi]
  https://support.google.com/googleplay/android-developer/answer/9888379
- Android lint `BatteryLife` bu eylemi Play içerik politikası ihlali
  olabilir diye uyarır. [resmi]
  https://googlesamples.github.io/android-custom-lint-rules/checks/BatteryLife.md.html
- Bir rakip (ReNotify) kodda "Play politikası buna izin veriyor,
  çünkü kayıt bu olmadan bozulur" diye yorum yazmış; bu kendi
  gerekçesi, Google'ın onayı değil. DoNotNotify manifestinde izin
  tanımlı. [topluluk]
  https://github.com/anujja/DoNotNotify/blob/8e5a1fa/app/src/main/AndroidManifest.xml
- Neden yine de değerli: kullanıcı uygulamayı muaf listesine alırsa
  Android bu uygulamayı bekleme kovası kısıtlarından da muaf
  tutuyor (EXEMPTED). Kod: kullanıcı listesi "güç tasarrufu izin
  listesi"ne giriyor, bekleme denetleyicisi bu listedekini en üst
  kovaya koyuyor. [AOSP kodu]
  https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/apex/jobscheduler/service/java/com/android/server/usage/AppStandbyController.java
  https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/apex/jobscheduler/service/java/com/android/server/DeviceIdleController.java
- Dinleyici uygulamaları için bekleme kovasında özel muafiyet
  bulamadım (en düşük kova hesabındaki muafiyet listesinde dinleyici
  geçmiyor). [AOSP kodu]
- Durumu okumak izin gerektirmiyor: `PowerManager.isIgnoringBatteryOptimizations`
  ("güç izin listesinde mi; çoğu güç tasarrufu uygulanmaz, aşırı
  durumlarda korumalar sürebilir"). [resmi]
  https://developer.android.com/reference/android/os/PowerManager

### 3.6 Periyodik kontrol (WorkManager / JobScheduler)
- Bekleme kovası sınırları (yaklaşık): Active 20 dk/60 dk; Working set
  10 dk/4 saat; Frequent 10 dk/12 saat; Rare 10 dk/24 saat;
  Restricted günde bir kez en çok 10 dk. Alarm: Rare saatte 1;
  Restricted günde 1. [resmi]
  https://developer.android.com/topic/performance/power/power-details
- Kullanıcının hiç açmadığı bir dinleyici uygulaması zamanla Rare veya
  Restricted'a düşer. Yani bekçi iş güvenilir değil; ikinci sırada. [çıkarım]
- Kullanıcı "Kısıtlı" arka plan seçtiyse işler ve alarmlar hiç
  çalışmaz. `ActivityManager.isBackgroundRestricted()` (API 28)
  bunu okur. [resmi]
  https://developer.android.com/reference/android/app/ActivityManager
- Android 16: iş çalışma kotaları kovaya, ön plan servisine ve görünür
  durumda başlama koşuluna göre ayarlanıyor; WorkManager'ı etkiliyor.
  `WorkInfo.getStopReason()` ile kaydet. [resmi]
  https://developer.android.com/about/versions/16/behavior-changes-all
- Rakiplerde: ReNotify 30 dakikada bir (kontrol etmeden "dürt"),
  DoNotNotify 6 saatte bir (son bağlanma 24 saatten eskiyse ve
  erişim yoksa tek uyarı; 24 saatte en çok 1). [topluluk]
  https://github.com/Bahoz420/ReNotify-App-OpenSource/blob/2930863/app/src/main/java/com/renotify/app/service/ListenerWatchdogWorker.kt
  https://github.com/anujja/DoNotNotify/blob/8e5a1fa/app/src/main/java/com/donotnotify/donotnotify/health/HealthCheckWorker.kt
- Bedeli: WorkManager 2.11.2 birleşik manifestine WAKE_LOCK,
  ACCESS_NETWORK_STATE, RECEIVE_BOOT_COMPLETED, FOREGROUND_SERVICE
  izinlerini ekliyor; INTERNET yok. En son sürüm 2.12.0. Sende'de
  WorkManager şu an yok. [resmi: AndroidX paketi]
  https://dl.google.com/android/maven2/androidx/work/work-runtime/2.11.2/work-runtime-2.11.2.aar
  https://dl.google.com/android/maven2/androidx/work/work-runtime/maven-metadata.xml
- "İnternet izni yok" sözümüz yalnızca INTERNET'e bağlı; ama
  "ağ durumu izni de yok" demek istersek WorkManager buna engel.
  Alternatif: doğrudan JobScheduler (platform API, ek izin yok).
  WorkManager'ın izinlerini `tools:node="remove"` ile atmanın güvenli
  olup olmadığını denemedim. [doğrulanamadı]

### 3.7 Tepsi uzlaştırması (kaçanı geri getirme)
- `getActiveNotifications()` tepsideki bildirimleri verir; bağlanma
  olayından sonra güvenle çağrılır. [resmi]
- Sende zaten bağlanınca seçili paketlerin tepsideki bildirimlerini
  yeniden işliyor (SendeNotificationListenerService). Parmak izi ve
  benzersiz indeks tekrarı engelliyor. İyi. [kod okumam]
- Sınırı dürüstçe söyle: kullanıcı veya uygulama bildirimi kaldırdıysa
  geri gelmez. Kopukken gelip silinen mesaj yok olur. [resmi]
- Yeni fikir ("canlı kaçırma sayacı"): uygulama açıldığında veya
  periyodik işte, bağlıysan tepsiyi tara; veritabanında olmayan ve
  son canlı yakalamadan yeni olan bildirim varsa bu, canlı akışın o
  mesajı kaçırdığının kanıtıdır. Hem içeri al hem sayacı artır hem de
  rebind sürecini başlat. Sıfır izin ister. [çıkarım]

### 3.8 Boşluk (gap) tespiti
- Süreç ölünce sessizdir; ölümü ancak sonradan öğrenirsin.
- `ActivityManager.getHistoricalProcessExitReasons` (API 30) önceki
  süreçlerin çıkış nedenlerini verir: LOW_MEMORY, USER_REQUESTED
  (zorla durdur), PACKAGE_STATE_CHANGE, EXCESSIVE_RESOURCE_USAGE,
  FREEZER, MEMORY_LIMITER, OTHER vb. [resmi]
  https://developer.android.com/reference/android/app/ApplicationExitInfo
- Android 15: `ApplicationStartInfo.wasForceStopped()` ile durdurulmuş
  durumdan çıkış anlaşılır. [resmi]
  https://developer.android.com/about/versions/15/behavior-changes-all
- Android 17: bellek sınırlayıcı süreci öldürebilir; neden `REASON_OTHER`
  ve açıklamada "MemoryLimiter:AnonSwap" yazar (belge ayrıca
  `REASON_MEMORY_LIMITER` sabitini listeler). Dinleyicide büyük
  bitmap tutma. [resmi]
  https://developer.android.com/about/versions/17/behavior-changes-all
- Önerilen kalıcı kayıt (yalnız zaman damgaları ve sayaçlar): son
  bağlanma, son kopma, son canlı bildirim, "yaşıyorum" damgası,
  önceki sürecin çıkış nedeni. Bunlardan "X ile Y arası dinleme
  kapalıydı" hesaplanır. [çıkarım]

### 3.9 Kendi kendine test
- Uygulama kendi kanalından sessiz, düşük öncelikli bir bildirim yayınlar;
  birkaç sn içinde dinleyiciden geri gelmezse "bağlı görünüyor ama
  duymuyor" denir. Notisave benzeri (02 dosyası). [topluluk]
- Gereksinim: Android 13+ bildirim izni (POST_NOTIFICATIONS). İzin
  yoksa test yapılamaz; bu durumu da göster.
- Sende şu an kendi paketini süzüyor; test için özel kimliğe istisna
  gerekir. [kod okumam]
- CI tarafında zaten `cmd notification post` ile uçtan uca test var.

### 3.10 Üretici ayar ekranlarına yönlendirme
- Android belgesi: başka uygulamanın etkinliğini, görünür olsun
  olmasın, `startActivity` ile açabilirsin; bulunamazsa
  `ActivityNotFoundException` yakala. `resolveActivity` ise görünürlük
  filtresine takılır. [resmi]
  https://developer.android.com/training/package-visibility/automatic
  https://developer.android.com/training/package-visibility/use-cases
- Sende'de `QUERY_ALL_PACKAGES` yok. Bu yüzden "var mı" diye sormak
  yerine doğrudan dene + yakala + bir sonraki adaya geç.
- DoNotNotify'ın OemAutostart dosyası (MIT) adayları sırayla
  `resolveActivity` ile yokluyor; manifestinde üretici paketleri için
  `<queries>` yok. Android 11+'ta bu yoklama sessizce başarısız
  olabilir. Cihazda doğrulamadım. [çıkarım]
  https://github.com/anujja/DoNotNotify/blob/8e5a1fa/app/src/main/java/com/donotnotify/donotnotify/setup/OemAutostart.kt
- Aday ekran adları (MIUI 10-12 döneminden; HyperOS'ta çalıştığı
  doğrulanamadı): Xiaomi güvenlik merkezi otomatik başlatma yöneticisi,
  Oppo güvenlik merkezi başlangıç yöneticisi, Vivo izin yöneticisi
  arka plan başlatma yöneticisi, Huawei sistem yöneticisi başlangıç
  yöneticisi, Samsung "lool" pil ekranı, OnePlus zincir başlatma
  listesi. [topluluk]
- AutoStarter kütüphanesi (MIT): Xiaomi, Redmi, Letv, Honor, Oppo,
  Vivo, Huawei, Samsung, Asus, OnePlus; Honor/Oppo/Vivo/OnePlus "test
  edilmedi"; "belgelenmiş API yok, bileşen sürekli değişiyor". [topluluk]
  https://github.com/judemanutd/AutoStarter
- MIUI-autostart (MIT): Xiaomi'de otomatik başlatmanın açık olup
  olmadığını okuyor ama gizli API kullanıyor; yazarı Play incelemesinde
  sorun çıkabileceğini söylüyor. Play sürümünde kullanma. [topluluk]
  https://github.com/XomaDev/MIUI-autostart
- Sonuç: otomatik başlatma durumu okunamaz; "Yaptım" onayı iste.

## 4. Android 14 / 15 / 16 / 17

- Türkiye'de Android sürüm dağılımı (Eylül 2026, StatCounter, mobil):
  16: %31,6; 15: %13,6; 14: %13,1; 13: %14,7; 12: %7,9; 11: %9,0;
  10: %4,1; 9 ve altı yaklaşık %5-6. En yaygın sürüm Android 16.
  [blog]
  https://gs.statcounter.com/android-version-market-share/mobile/turkey
- Android 14: ön plan servisi türü zorunlu (Play'de bildirim formu).
  Bekleme kovasında yeni neden: iş geri çağrıları (onStartJob/
  onStopJob/onBind) tekrar tekrar ANR verirse "Restricted" kovasına
  düşer. Önlem: işlerde ve dinleyicide ana iş parçacığını meşgul
  etme (dinleyiciye uzatmak benim çıkarımım). [resmi]
  https://developer.android.com/about/versions/14/behavior-changes-all
- Android 15 (tüm uygulamalar):
  - "Durdurulmuş" durum sıkılaştı: uygulama durdurulunca bekleyen
    tüm PendingIntent'ler iptal; kullanıcı eylemiyle çıkınca
    BOOT_COMPLETED teslim edilir. [resmi]
  - Güvenilmeyen dinleyicilerde OTP içeren bildirimin içeriği
    gizleniyor ("redacted"). Güvenilen: eşlik cihazı (CDM) ilişkisi
    olanlar. Sende güvenilmeyen sayılır. OTP gibi algılanan bir
    mesajın metni okunamayabilir. [resmi]
    https://developer.android.com/about/versions/15/behavior-changes-all
  - Kodda "güvenilir" = RECEIVE_SENSITIVE_NOTIFICATIONS izni, platform
    imzası, AppOps izni veya CDM ilişkisi. Ana dalda bu davranış bir
    özellik bayrağının arkasında. [AOSP kodu]
    https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
  - Duyarlı içeriğin hangi mesajlarda tetiklendiği (müşteri "kod 123456
    yazdım" derse?) belgelenmemiş. Test et. [doğrulanamadı]
- Android 16: iş kotaları (3.6). Dinleyiciyle ilgili belgelenmiş bir
  davranış değişikliği bulamadım. [resmi]
  https://developer.android.com/about/versions/16/behavior-changes-all
- Android 17: uygulama bellek sınırları (3.8). Dinleyicide
  bellek tasarrufu zorunlu. [resmi]
  https://developer.android.com/about/versions/17/behavior-changes-all
- Üretici sürümleri ayrıca hızla değişiyor: YouTube başlıklarında
  HyperOS 3.1 ve HyperOS 4 görülüyor. Menü adları yine değişecek.
  [tek kaynak: başlıklar]
  https://www.youtube.com/watch?v=dQQz9Q6PAUQ
- Okuduğum belgelerde dinleyiciye özel bir arka plan muafiyeti
  geçmiyor. "Yeni Android daha iyi" varsayımı yapma. [çıkarım]

## 5. Türkiye: hangi markalar

- StatCounter (Türkiye, mobil, Eylül 2025 - Eylül 2026) kayıtlı
  satıcı payları. "Bilinmeyen" oranı çok yüksek (%37-57), Apple
  yaklaşık %24. Veri sayfa görüntülemeye dayanıyor, satış değil. [blog]
  https://gs.statcounter.com/vendor-market-share/mobile/turkey
- Apple ve bilinmeyeni çıkarıp yalnız tanınan Android markalarını
  100'e normalize ettim (13 ay ortalaması / son 3 ay):
  - Samsung %42 / %40.
  - Xiaomi (Redmi, POCO dahil) %34 / %37.
  - Oppo %5 / %6.
  - Huawei %4 / %4.
  - realme %3 / %3.
  - Tecno %3 / %3.
  - Infinix %2 / %2.
  - vivo %2 / %2.
  - Google %1, Reeder, Honor (%0,5-0,8), General Mobile, Lenovo,
    OnePlus, Casper, Nothing: %1'in altı.
- Sonuç: Samsung + Xiaomi ≈ %77. Sonraki kademe Oppo, realme, Tecno,
  Infinix, vivo, Huawei/Honor. [çıkarım]
- Huawei'nin 2019 sonrası modellerinde Google Play yok (Honor 2020
  sonrası ayrıldı, yeni Honor'lar Google hizmetleriyle çıkıyor). Play
  sürümü Huawei'de çoğunlukla kurulamaz, APK (sideload) sürümü
  kurulabilir. 2021 kaynağı, Türkiye'nin güncel durumu doğrulanamadı.
  [tek kaynak]
  https://www.huaweicentral.com/honor-phone-with-google-mobile-services-and-play-store-launching-soon/
- HarmonyOS NEXT Android uygulamalarını çalıştırmıyor; yurt dışı
  Huawei'ler EMUI (AOSP tabanlı) kalacak denmişti (2023); 2026 durumu
  doğrulanamadı. [tek kaynak]
  https://9to5google.com/2024/09/23/huawei-harmony-os-next-apps/
- Türkiye'de ayrıca Casper, General Mobile, Reeder gibi yerli
  markalar var; paylar çok küçük ve bu markaların pil yöneticisi
  hakkında kaynak bulamadım. [doğrulanamadı]

## 6. Marka marka kurulum adımları

Genel sıra (her markada aynı mantık, menü adı değişir):
- 1) Bildirim erişimi: Sende'ye izin (derin bağlantı; zaten var).
- 2) Pil: uygulama için "kısıtsız / sınırsız / optimize etme".
- 3) Arka planda çalışma + otomatik başlatma (marka özel).
- 4) Son uygulamalarda kilitle.
- 5) Markanın "otomatik uyutma / dondurma" özelliklerini kapat.
- 6) Sende'nin "Test et" düğmesi ve 10 dk ekran kapalı deneme.
- Güncelleme sonrası bu ayarlar sıfırlanabilir (Samsung, OnePlus,
  Xiaomi). Sistem sürümü değişince yeniden kontrol öner. [çoklu]

Menü adları sürüme göre değişir; her adımda ayar araması öner
("uyku", "pil", "otomatik başlat").

### Samsung (One UI)
- Türkiye'de ilk sırada (%40+). Resmi Türkçe sayfa kullanıldı. [resmi]
  https://www.samsung.com/tr/support/galaxy-battery/optimization/
- 1) Ayarlar > Uygulamalar > Sende > Pil > "Sınırsız" seç (İngilizce:
  Restricted / Optimised / Unrestricted). Türkçe adı doğrulanamadı.
  Üçüncü taraf kaynaklara göre bu seçim uygulamayı uyku listelerinden
  de muaf tutuyor. [blog, çoğul]
  https://docs.sportstracklive.com/android-battery-saving/samsung
  https://adguard.com/kb/adguard-for-android/solving-problems/background-work/
- 2) Ayarlar > Pil (bazı modellerde: Pil ve cihaz bakımı > Pil) >
  "Arka plan kullanım sınırları". [resmi]
- 3) "Kullanılmayan uygulamaları uykuya al" anahtarını kapat. [blog]
- 4) "Uyku modundaki uygulamalar" ve "Derin uykuya alınmış
  uygulamalar" listelerine bak; Sende varsa çıkar. [resmi]
- 5) "Hiç oto. uykuya geçirilmeyen uygulamalar" > + > Sende > Ekle. [resmi]
  (Unrestricted seçiliyse Sende bu listede görünmeyebilir; normal.) [blog]
- 6) Son uygulamalarda Sende simgesine uzun bas > kilitle. [blog, çoğul]
- 7) Gerekirse "Uyarlanabilir pil"i kapat. [blog]
- Not: Samsung "sık bildirim" nedeniyle de uygulamayı derin uykuya
  alabiliyor (resmi sayfa derin uyku listesinde bunu kontrol
  etmeni söylüyor). Sende kendi bildirimini seyrek tut. [resmi + çıkarım]
- Not: ayarlar bellenim güncellemesinde geri dönebilir. [topluluk]
  https://dontkillmyapp.com/samsung

### Xiaomi, Redmi, POCO (MIUI, HyperOS)
- Türkiye'de ikinci sırada (%34-37). [blog]
- En sık sorun otomatik başlatmanın kapalı olması. [blog]
  https://docs.sportstracklive.com/android-battery-saving/xiaomi
- 1) Ayarlar > Uygulamalar > Uygulamaları yönet > Sende > "Otomatik
  başlatma" aç. MIUI 14/HyperOS'ta alternatif yol: Uygulama izinleri >
  "Arka planda otomatik başlatma". [çoklu]
  https://dontkillmyapp.com/xiaomi
  https://adguard.com/kb/adguard-for-android/solving-problems/background-work/
- 2) Aynı sayfada "Pil tasarrufu" > "Kısıtlama yok". Türkçe blog
  yolu: Ayarlar > Uygulamalar > Uygulamaları Yönet > uygulama > Pil
  Tasarrufu > Kısıtlama Yok. [blog]
  https://teknohappy.com/arka-plan-etkinligi-kisitlandi-xiaomi-cozum/
- 3) "Uygulama etkinliğini duraklat" benzeri (kullanılmıyorsa
  duraklat) seçeneği varsa kapat. İngilizce: "Pause app activity if
  unused". Türkçe adı doğrulanamadı. [tek kaynak]
  https://adguard.com/kb/adguard-for-android/solving-problems/background-work/
- 4) Son uygulamalar ekranında Sende kartını aşağı çek > asma kilit.
  [çoklu]
- 5) Pil menüsünde genel "Pil tasarrufu" ve "Ultra pil tasarrufu"
  kapalı olsun. [blog]
- Uyarı: pil "sınırsız" + izinler tamam iken bile HyperOS 3'te kopma
  görüldü (HyperBridge #330). Yazılım bekçisi şart. [tek kaynak]
- Güncelleme sonrası otomatik başlatma izinleri sıfırlanmış olabilir
  (Türkçe blog). [blog]
  https://teknohappy.com/xiaomi-bildirimler-gec-geliyor-cozumu/
- Çin ROM'u için YouTube'da ayrı düzeltme videoları var (başlıklarda
  "China ROM"). Türkiye'de satılan cihazların Global ROM olduğunu
  varsayıyorum; bu kapsam dışı. [çıkarım]

### Oppo (ColorOS)
- dontkillmyapp sayfası eski (yalnız Oppo F1S). [topluluk]
  https://dontkillmyapp.com/oppo
- 1) Ayarlar > Uygulamalar > Uygulama yönetimi > Sende > Pil kullanımı
  > "Arka plan etkinliğine izin ver" (bazı sürümlerde ön plan
  etkinliği ve otomatik başlatma da burada). [blog, çoğul]
  https://adguard.com/kb/adguard-for-android/solving-problems/background-work/
- 2) Ayarlar > Uygulamalar > "Otomatik başlatma" (veya Güvenlik
  Merkezi > Gizlilik İzinleri > Başlangıç Yöneticisi) > Sende aç. [blog]
- 3) Ayarlar > Pil > "Uygulama pil yönetimi" > Sende > optimize etme /
  arka plana izin. [blog]
- 4) Son uygulamalarda kilitle. [blog]
- 5) Pil modu "Yüksek performans" öneren kaynak var ama pil
  tüketimini artırır; ilk önerme. [tek kaynak]
- Menü adları sürüme göre farklı; AdGuard'ın rehberi de ColorOS için
  tek paragraf. [doğrulanamadı: ColorOS 15 yolu]

### realme (realme UI)
- 1) Uygulama bilgisi > Pil kullanımı > "Arka plan etkinliği",
  "Ön plan etkinliği", "Otomatik başlatma" üçü açık. [blog]
  https://adguard.com/kb/adguard-for-android/solving-problems/background-work/
- 2) Uygulama bilgisi altında "Kullanılmıyorsa uygulama etkinliğini
  duraklat" kapalı. [blog]
- 3) Ayarlar > Pil > Diğer ayarlar > "Pil kullanımını optimize et" >
  Sende > Optimize etme. [blog]
- 4) "Uygulama hızlı dondurma" listesinde Sende olmasın: dondurulan
  uygulama bildirim alamaz ve arka planda çalışamaz. [topluluk]
  https://dontkillmyapp.com/realme
- 5) Eski sürümlerde (resmi Türkiye sayfası): Telefon Yöneticisi >
  Gizlilik İzinleri > Başlangıç Yöneticisi; uygulama sayfasında kilit.
  [resmi, eski]
  https://www.realme.com/tr/support/troubleshooting
- realme topluluğu sayfaları (otomatik başlatma 5 uygulamayla
  sınırlı mı gibi şikâyetler) açılamadı; arama özetinden: "otomatik
  başlatma ve ilişkili başlatma kapalıysa mesajlar gelmeyebilir".
  [doğrulanamadı]
  https://c.realme.com/in/post-details/1950836463042125824

### vivo (Funtouch OS / OriginOS)
- Payı küçük (%2). [blog]
- 1) Ayarlar > Pil > "Yüksek arka plan güç tüketimi" (yeni sürümde
  "Arka plan güç tüketimi yönetimi") > Sende aç. [resmi]
  https://bbs.vivo.com/in/thread/11952
- 2) Otomatik başlatma: Ayarlar > Diğer ayarlar > Uygulamalar >
  Otomatik başlatma (eski: iManager > Uygulama yöneticisi). [topluluk]
  https://dontkillmyapp.com/vivo
- 3) Uygulama bilgisi > Pil > "Optimize edilmemiş". [topluluk]
- 4) Son uygulamalarda kilitle. [topluluk]
- 5) Pil tasarrufu, süper güç tasarrufu, uyku modunu kapat. [blog]

### Huawei / Honor
- Honor (Google hizmetli; Türkiye'de pay %0,5-0,8): resmi adımlar.
  [resmi]
  https://www.honor.com/global/support/content/en-us00406916/
  https://www.honor.com/za/support/content/en-us00428704/
- 1) Optimize edici (veya Ayarlar'da "Başlat" ara) > "Uygulama
  başlatma": Sende için "Otomatik yönet"i kapat; "Otomatik başlat",
  "İkincil başlat", "Arka planda çalış" üçünü aç. En önemlisi
  "Arka planda çalış". [resmi]
- 2) Ayarlar > "Pil optimizasyonu" > Tüm uygulamalar > Sende >
  "İzin verme" (yani optimize etme). [resmi]
- 3) Son uygulamalar ekranında kartı aşağı çek > kilit. Giriş seviyesi
  cihazlarda kilitli uygulama yine temizlenebilir. [resmi]
- Honor testinde yeniden başlatma sonrası mesaj akışı ancak bu
  başlatma ayarları açıkken sürdü. [tek kaynak]
  https://github.com/metabrainz/listenbrainz-android/issues/772
- Huawei (EMUI): aynı "Uygulama başlatma" ekranı + pil optimizasyonu;
  eski modellerde PowerGenie adlı sistem uygulaması kayıtsız
  uygulamaları öldürüyor, tam çözüm ADB. Son kullanıcıya önerme.
  [topluluk]
  https://dontkillmyapp.com/huawei
- Huawei yeni modellerde Google Play yok; bu cihazlarda APK
  (sideload) sürümü gerekir (Bölüm 5). [çıkarım]

### Tecno, Infinix (HiOS, XOS)
- Tecno ve Infinix payı birlikte %5 civarı. [blog]
- dontkillmyapp Tecno sayfası (eski HiOS): Ayarlar > Pil Lab > Pil
  tasarruf ayarları > "Uygulamalar için güç tasarrufu yönetimi"ni
  kapat; Phone Master > Araç kutusu > "Otomatik başlatma yönetimi" >
  arka planda çalışmaya izin ver. [topluluk]
  https://dontkillmyapp.com/tecno
- HiOS 15 / XOS 15 için sahiplerin listesi (Ekim 2026, onaylanmamış;
  paylaşan kişi kendi ayar denetleyici ürününü yapıyor):
  - Ayarlar > Uygulama yönetimi > uygulama > Pil: "Arka plan
    etkinliğine izin ver" açık, "Boştayken uygulamayı durdur" kapalı.
  - Phone Master > Hızlandır > "Otomatik başlatma yönetimi" > aç;
    "Korunan uygulamalar"a ekle.
  - Son uygulamalarda kilitle.
  - Pil tasarrufu bölümünde uyku modu optimizasyonu ve otomatik güç
    tasarrufu kapalı.
  - "Dondurucu" (Freezer): dondurulan uygulama bildirim almaz.
  [tek kaynak]
  https://github.com/urbandroid-team/dont-kill-my-app/issues/4394
- Infinix XOS (AdGuard rehberi): Uygulama yönetimi > Uygulama listesi >
  uygulama > Pil > "Arka plan kullanımına izin ver" > Sınırsız; Otomatik
  başlatma yönetimi > aç; Pil ve Güç Tasarrufu > Güç tasarrufu kapalı;
  Özel uygulama erişimi > Pil optimizasyonu > Optimize edilmemiş. [blog]
  https://adguard.com/kb/adguard-for-android/solving-problems/background-work/

### Diğerleri (Pixel, General Mobile, Casper, Nothing, Motorola)
- Pixel ve saf Android: dontkillmyapp puanı 0. Tek adım pil
  "Sınırsız". [topluluk]
  https://dontkillmyapp.com/google
- Nothing: dontkillmyapp sayfası yok. Bloglar "Sınırsız" ve uyarlanabilir
  pili kapatmayı öneriyor; bir forum yorumu sınırsızın tam çözüm
  olmadığını yazıyor. Sayfaları açmadım, arama özetinden. [blog, zayıf]
  https://www.technobezz.com/nothing-phone-3-notification-problems-fix
  https://droidwin.com/?p=36165
- Motorola: dontkillmyapp puanı 3; AdGuard rehberi dört ayar
  sayıyor (arka plan kullanımı, kısıtlama kaldır, arka plan uygulamalarını
  yönet, uyarlanabilir pil). [blog]
  https://dontkillmyapp.com/motorola
  https://adguard.com/kb/adguard-for-android/solving-problems/background-work/
- General Mobile, Casper, Reeder: kaynak bulunamadı. [doğrulanamadı]

## 7. Play politikası özeti (güvenilirlik için)

- Genel çerçeve: "Cihaz ve Ağ Kötüye Kullanımı". Varsayılan Android
  sistem optimizasyonlarına uyma zorunluluğu ve "güç yönetimini
  atlamaya çalışan, izin listesine uygun olmayan uygulamalar" ihlal
  örneği. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9888379
- Pil muafiyeti diyaloğu: yalnız çekirdek işlevi bozulan kategoriler;
  liste ekranı serbest (Bölüm 3.5). [resmi]
- Ön plan servisi: Android 14+ tür + Play Console beyanı; ölçütler
  Bölüm 3.4. [resmi]
- Erişilebilirlik API'si: yalnızca engelli erişimi için işaretlenen
  araçlar muaf; diğer kullanımlar için beyan formu, uygulama içi
  bilgilendirme ve onay. Dinleyiciyi yedeklemek için "erişilebilirlik
  servisi" açmak uygun değil. [resmi]
  https://support.google.com/googleplay/android-developer/answer/10964491
- Tam zamanlı alarm: `USE_EXACT_ALARM` "kısıtlı kullanım" ve ileride
  Play politikasına tabi; bekçi için gereksiz. İhtiyaç olursa
  `SCHEDULE_EXACT_ALARM` bile istemeden, yaklaşık zamanlı iş
  kullan. [resmi]
  https://developer.android.com/develop/background-work/services/alarms/schedule
- Kullanıcı verisi: güvenilirlik özellikleri yeni veri akışı eklemiyor;
  yalnız zaman damgası ve sayaç tutulacak. Politika ayrıca hassas
  veriye erişimde belirgin uygulama içi bilgilendirme ve onay istiyor
  (zaten notlandı). [resmi]
  https://support.google.com/googleplay/android-developer/answer/9888076
- Sende gibi bildirim toplayıcılar "kabul edilebilir kullanım"
  tablosunda adıyla yok. Sonucu Google'ın inceleme görüşü belirler;
  doğrudan diyalogla bunu riske atmayalım. [çıkarım]

## 8. Sende'de uygulanacak güvenilirlik paketi

### A. Play politikasına uygun (iki sürümde de)
- A1) Süreç düzeyinde "dinleyici bekçisi":
  - Kopunca: +3 sn requestRebind, +15 sn requestRebind, +45 sn aç/kapat
    (1-2 sn aralıklı, DONT_KILL_APP) + requestRebind, +60 sn karar.
  - Bağlanınca bekçi durur; erişim yoksa hiç başlamaz.
  - Aç/kapat: en az 60 sn ara, günlük sınır (ör. 6).
  - Servis kapsamında değil, uygulama kapsamında çalışsın.
- A2) Kalıcı sağlık kaydı (DataStore veya Room, yalnız metaveri):
  son bağlanma, son kopma, son canlı bildirim, yaşıyorum damgası,
  önceki sürecin çıkış nedeni, kopma sayısı (7 gün), toplam boşluk süresi.
  DiagnosticsRecorder bugün bellekte; süreç ölünce kayıp.
- A3) Başlangıç denetimi (Application.onCreate ve ana ekran açılışı):
  - Önceki süreç neden öldü? (ApplicationExitInfo)
  - Boşluk var mı? Varsa kayda yaz.
  - Erişim verili ama bağlı değil mi? Bekçiyi başlat.
- A4) Tepsi uzlaştırması: bağlanınca ve ekran açılınca tepsiyi tara;
  eksik olanı içeri al; "canlı kaçırma" sayacı.
- A5) Okunabilen koruma durumları (izin gerektirmeyen):
  - `isNotificationListenerAccessGranted` (API 27).
  - `PowerManager.isIgnoringBatteryOptimizations`.
  - `ActivityManager.isBackgroundRestricted` (API 28).
  - `UsageStatsManager.getAppStandbyBucket` (API 28, kendi uygulaman).
  [resmi]
  https://developer.android.com/reference/android/app/NotificationManager
  https://developer.android.com/reference/android/app/usage/UsageStatsManager
- A6) Marka sihirbazı (Bölüm 6): `Build.MANUFACTURER`/`BRAND` ile
  Samsung, Xiaomi/Redmi/POCO, Oppo, realme, vivo, Honor/Huawei,
  Tecno/Infinix, diğer. Her adımda "Ayarı aç" düğmesi: marka ekranı
  → olmazsa uygulama bilgisi → olmazsa genel ayar (hep
  try/catch, `resolveActivity` yok). Adım sonunda "Yaptım" onayı.
  Mevcut OemHints tek cümle veriyor ve Tecno/Infinix/Honor ayrımı yok.
- A7) Kullanıcıya pil: `ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS`
  (liste ekranı) + uygulama bilgisi yedeği. ListenerHealth'teki
  `openBatterySettings` bugün böyle; doğru.
- A8) Tek kullanıcı uyarısı: kopma 10 dk'yı aşarsa ve bekçi
  çözemediyse tek yerel bildirim; 24 saatte en çok bir. Dokununca
  doğrudan "Düzelt" ekranı. Bildirim izni yoksa yalnızca uygulama içi
  rozet.
- A9) "Test et" düğmesi (kendi kendine bildirim, 5 sn).
- A10) Sistem güncellemesi tespiti: `Build.FINGERPRINT` değişince
  "Sistem güncellendi; koruma ayarlarını yeniden kontrol edin" notu.
- A11) MY_PACKAGE_REPLACED alıcısı + mevcut BOOT_COMPLETED alıcısı:
  yalnız bekçiyi ve zamanlayıcıyı başlatır (kısa iş; Android 15
  ön plan servisi kısıtı için servis başlatmaz).
- A12) Periyodik kontrol (15-30 dk): JobScheduler tercih (ek izin yok)
  veya WorkManager (3 ek izin getirir, 3.6). İkincil güvence; birincil
  değil.
- A13) Bellek ve ana iş parçacığı disiplini: büyük görseli dinleyici
  çağrısında işleme; kısa iş, ağır iş arka planda. Android 14 ANR
  → Restricted, Android 17 bellek sınırı.
- A14) İlk kullanımda dürüst vaat: "Telefonun pil yöneticisi Sende'yi
  durdurabilir; durursa size haber veririz ve kaçanları tepsiden
  alırız. Tepsiden silinmiş mesajı geri getiremeyiz." (I-47 kısa
  anlat kuralına uyar.)

### B. Yalnız sideload (APK) sürümünde
- B1) `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` + doğrudan diyalog.
  Manifest sürüme özel olmalı; Play sürümünde izin da olmasın
  (lint `BatteryLife` uyarır).
- B2) Kullanıcı açarsa "canlı tut" ön plan bildirimi. Varsayılan
  kapalı, açıklamalı. Play sürümünde yok.
- B3) Android 13+ "kısıtlı ayarlar" rehberi (zaten var).
- B4) Aday: Xiaomi'de MIUI-autostart gibi gizli API ile otomatik
  başlatma durumunu okuma. Önerim: yapma; gizli API kırılgan. Okumak
  yerine "Yaptım" onayı.

### C. Yapmayacaklarımız
- C1) Sessiz ses çalmak, 1 piksel görünmez pencere, sahte medya
  oturumu, kalıcı wakelock. Güç yönetimini atlamak; Play ihlali. [resmi]
  https://support.google.com/googleplay/android-developer/answer/9888379
- C2) Erişilebilirlik servisiyle hayatta kalma veya yedekleme. [resmi]
- C3) Play sürümünde doğrudan pil muafiyeti diyaloğu. [resmi]
- C4) Play sürümünde sürekli "canlı tut" servisi (Bölüm 3.4).
- C5) Kendi kendini tetikleyen sık alarm döngüleri, exact alarm.
- C6) `WRITE_SECURE_SETTINGS` ile sistem ayarı değiştirme (Nokia
  DuraSpeed örneğindeki yöntem). [topluluk]
  https://adguard.com/kb/adguard-for-android/solving-problems/background-work/
- C7) Başka üreticinin sistem uygulamasını ADB ile silme önerisi
  (PowerGenie). Teknik kullanıcıya bile önerme.
- C8) Aç/kapat hilesini her dakika çağırmak.
- C9) Gizli API ile üretici ayarı okumak (Play incelemesi riski).
- C10) Android'in manifest meta verisiyle "autobind=false" yapıp
  isteğe bağlı bağlamak (Android 14): bağlamayı biz yönetmek bize
  daha çok sorumluluk yükler. [resmi]

### D. Mevcut kodda gördüğüm boşluklar
- D1) `onListenerDisconnected` tek requestRebind çağırıyor; kademe yok,
  süreç düzeyi bekçi yok.
- D2) `ListenerHealth.restart` aç/kapatı art arda yapıyor, ara yok,
  sınır yok.
- D3) Durum bellekte; süreç ölünce "kaç saat kapalıydı" bilgisi kayıp.
- D4) "Bağlı ama duymuyor" durumu yakalanmıyor (test yok).
- D5) Periyodik kontrol yok; yalnız ekran açıkken denetim.
- D6) OemHints: Tecno, Infinix, itel, Honor ayrı ele alınmıyor;
  realme "oppo" grubunda; tek cümle.
- D7) MY_PACKAGE_REPLACED yok.
- D8) Sistem sürümü değişiminde yeniden kontrol yok.

### E. Uygulama sırası
- 1. tur (küçük, değerli): A1, A2, A3, A4, A6 (Samsung + Xiaomi önce).
- 2. tur: A5, A8, A9, A10, A11.
- 3. tur: A12, A13, kalan markalar, B maddeleri.
- Her tur sonunda gerçek cihaz testi (Bölüm 10).

## 9. Uygulama içi sağlık göstergesi

Amaç: kullanıcı "çalışıyor mu?" sorusunu bir bakışta, korkmadan
görsün. Ana ekran tek ekran kuralı bozulmaz.

### Görünüm
- Üst çubukta küçük bir nokta. Sağlıklıyken sadece nokta (yeşil).
- Sorun varken noktanın yanında tek satır. Dokununca "Sağlık" sayfası.
- Dört durum:
  - Yeşil: "Dinliyor." Altında "Son mesaj 12 dk önce."
  - Sarı: "Dikkat: telefon Sende'yi uyutabilir." (pil/uyku/kısıtlı
    kova uyarıları)
  - Kırmızı: "Dinleme durdu." + "Düzelt" düğmesi.
  - Gri: "Bildirim erişimi kapalı." + "İzin ver".

### Kullanılan işaretler
- Bağlı mı (onListenerConnected/Disconnected bayrağı).
- Erişim verili mi (`isNotificationListenerAccessGranted`).
- Test sonucu (son başarılı test zamanı).
- Pil muafiyeti (`isIgnoringBatteryOptimizations`).
- Arka plan kısıtı (`isBackgroundRestricted`).
- Bekleme kovası (`getAppStandbyBucket`): RARE, RESTRICTED sarı yapar.
- Önceki sürecin çıkış nedeni (ApplicationExitInfo).
- Canlı kaçırma sayacı (tepsi uzlaştırması).
- Kullanıcı onayları: otomatik başlatma, uyku listesi, kilit
  (okunamaz → "Yaptım").

### "Sağlık" sayfası (üç bölüm)
- Şu an:
  - Bağlantı durumu, son bağlanma zamanı.
  - "Şimdi test et" düğmesi, sonucu 5 sn içinde.
- Koruma ayarları (marka sihirbazı adımları):
  - Her adım: durum (okundu / onaylandı / yapılmadı), "Ayarı aç".
  - Sistem sürümü değişmişse üstte "Yeniden kontrol edin".
- Son 7 gün:
  - Kopma sayısı, en uzun boşluk, geri alınan mesaj sayısı.
  - Boşluk satırı: "14:20-16:05 arası dinleme kapalıydı."
  - Dürüst not: "Bu sırada gelip silinen mesajlar görünmez."

### Metin örnekleri (Türkçe, kısa)
- "Dinliyor."
- "Sende durmuş görünüyor. Düzeltiyoruz."
- "Telefon Sende'yi uyutuyor olabilir. Bir ayar gerekiyor."
- "2 saat 10 dk dinleme kapalıydı. 3 mesaj geri alındı."
- "Test başarılı: bildirim 1 sn'de yakalandı."

### Kurallar
- Sağlıklıyken ekranda ek yazı yok.
- Uyarı bildirimi 24 saatte en çok bir (DoNotNotify örneği).
- Telefon markasına özel yazı yalnız sorun varken.
- Hiçbir mesaj içeriği günlüğe girmez; yalnız sayaç ve zaman.
- Sessize alınmış sohbetlerin bildirim bırakıp bırakmadığı
  doğrulanamadı. WhatsApp SSS'ine göre telefon ayarı uygulama ayarını
  ezer (arama özetinden; sayfayı açmadım). "Sende'ye düşmeyen" ile
  "kaçan" mesajı ayırt etmek için test et. [doğrulanamadı]
  https://faq.whatsapp.com/android/chats/how-to-manage-your-notifications
- "Hiç mesaj gelmiyor" sessizliği ile arıza ayrımı: test + tepsi
  uzlaştırması yoksa mümkün değil; bu yüzden ikisi de pakette.

## 10. Test planı

- Her tur sonunda gerçek cihaz: Samsung, Xiaomi/Redmi, Oppo veya
  realme, vivo veya Honor; her biri 7 gün bırakma. Emülatör bu
  sorunu yakalamaz (üretici katmanı yok). [çıkarım]
- Senaryolar (qoody listesinden uyarlandı) [tek kaynak]:
  - Normal. Ekran kapalı 10 dk, 1 saat, gece boyu.
  - Yeniden başlatma, uygulamayı açmadan mesaj gelsin.
  - Uygulama güncelleme (adb install -r).
  - Son uygulamalardan kaydırarak kapatma.
  - Ayarlar > Uygulama > Zorla durdur.
  - Pil tasarrufu açık.
- adb komutları (dev ortamı):
  - `adb shell am kill com.sende.app` (arka plan sürecini öldürür;
    komut ayrıntısını cihazda doğrula).
  - `adb shell am force-stop com.sende.app`.
  - `adb shell dumpsys notification | grep -A3 "Live notification
    listeners"` bağlı mı; "Snoozed notification listeners" askıda mı.
    [AOSP kodu]
  - `adb shell cmd notification disallow_listener / allow_listener
    com.sende.app/com.sende.app.capture.SendeNotificationListenerService`.
    [AOSP kodu]
  - `adb shell cmd notification post ...` gerçek bildirim (CI'da var).
  - `adb shell dumpsys deviceidle force-idle` (Doze) ve `am set-inactive`;
    `adb shell am set-standby-bucket com.sende.app restricted` (Android
    16 belgesindeki komut). [resmi]
    https://developer.android.com/training/monitoring-device-state/doze-standby
    https://developer.android.com/about/versions/16/behavior-changes-all
  - `adb shell dumpsys notification --noredact` ham metin için. [topluluk]
- Kabul ölçütü: kopma sonrası 2 dk içinde bağlanma ya da kırmızı
  durum + bildirim; boşluk raporu gerçek süreyle ±1 dk uyumlu; tepsideki
  mesajlar kayıpsız geri alınır.
- Mekanizmaları ayrı ayrı sına:
  - requestRebind tek başına işe yarıyor mu? (beklenti: çoğu zaman
    hayır.)
  - Aç/kapat sonrası onay kaybolur mu? Çoklu örnek oluşur mu?
  - Zorla durdurma sonrası ilk açılışta ne oluyor (Android 15+)?

## Denenen ve çalışmayan yollar

- Anonim `git clone` (dontkillmyapp'ın GitHub deposu): "could not
  read Username". `raw.githubusercontent.com` 404, `github.com` 403
  ("bu oturumda GitHub erişimi bu depoya açık değil"). Yerine
  WebFetch ile GitHub sayfaları (issue, PR, dosya) okundu; klon
  yapılmadı. Oturumun GitHub aracına dokunmadım.
- Proxy durum komutunu çalıştırmak (`__agentproxy/status`) otomatik
  izin denetimince reddedildi; geçildi, gerek de kalmadı.
- Reddit: curl 403 ("Blocked"), web arama aracı reddit.com'u
  erişilemez sayıyor (iki yol da). Reddit kaynağı yok.
- X.com: WebFetch 402. YouTube: yalnızca arama sonucu başlıkları ve
  kısa açıklamalar; adım içeriği alınamadı.
- XDA: curl 403, WebFetch 403. Samsung Members TR: 403. Technopat'taki
  Samsung konusu bizim sorunun tersi (arka plan kapatma) çıktı.
- realme topluluğu, Xiaomi topluluğu (c.mi.com), OPPO topluluğu:
  WebFetch yalnız sayfa başlığını döndürdü. Playwright ile realme
  sayfaları da boş (0 bayt) geldi. Denemeyi bıraktım; arama özetleri
  "doğrulanamadı" işaretli.
- Play Console "İzinler" sayfasında REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
  yok; ilgili cümle Android Doze belgesinde ve "Cihaz ve Ağ
  Kötüye Kullanımı" sayfasında.
- dontkillmyapp.com: /honor, /nothing, /infinix 404. API v2'de de yok.
- StatCounter'da marka kırılımında "Bilinmeyen" %37-57; pay hesabı
  yalnız bilinenler arasında ve yaklaşık.
- Gerçek cihaz yok: hiçbir adım telefonda denenmedi.
- Web arama aracıyla Stack Overflow sonuçları zayıf çıktı; Stack
  Exchange API (anonim) ile 40+ soru listelendi, 12'sinin cevapları
  okundu.

## Doğrulanamayanlar

- Aç/kapat sırasında onay kaybı: kod okumasından çıkarım; cihazda ve
  kullanıcı raporunda doğrulanmadı.
- Dinleyici için bekleme kovası muafiyeti: kodda bulamadım;
  gerçek cihazda kovaların nasıl değiştiği ölçülmedi.
- Zorla durdurma sonrası dinleyicinin kendiliğinden bağlanıp bağlanmadığı
  sürüme göre değişiyor olabilir. Android 15 belgesi durumu açıklıyor
  ama dinleyiciden söz etmiyor.
- OTP gizleme: hangi bildirimlerin "duyarlı" sayıldığı belgesiz.
- HyperOS 3/4, ColorOS 15, One UI 8, MagicOS 9 için resmi menü yolları.
  Üretici sayfalarının çoğu İngilizce ve eski; Türkçe adlar çoğunlukla
  blogdan.
- Samsung "Sınırsız" seçeneğinin Türkçe adı.
- Xiaomi "uygulama etkinliğini duraklat" seçeneğinin Türkçe adı.
- realme otomatik başlatmada "5 uygulama sınırı" iddiası.
- Casper, General Mobile, Reeder cihazlarının pil yöneticisi.
- Sessize alınmış WhatsApp sohbetlerinin bildirim bırakıp bırakmadığı.
- WorkManager izinlerini manifestten atmanın güvenli olup olmadığı.
- Üretici bileşen adlarının (autostart ekranları) HyperOS/ColorOS 15'te
  hâlâ çalıştığı.
- AOSP davranışlarının Android sürümü sınırı (NOT_PERCEPTIBLE,
  "snooze" mantığı vb. hangi sürümde girdi).

## Lisans notları

- AOSP dosyaları (ManagedServices, NotificationManagerService,
  NotificationListenerService, ActiveServices, AppStandbyController,
  DeviceIdleController, NotificationShellCmd): Apache-2.0 başlıkları
  gördüm (bir kısmını). Yalnız okundu.
- ReNotify: GPL-3.0 (02 dosyasından). Kod almak yasak sayılsın; fikir.
- DoNotNotify: MIT (02). AutoStarter: MIT. MIUI-autostart: MIT.
- HyperBridge: Apache-2.0 (README'de yazıyor).
- ListenBrainz Android: GPL v3 veya üstü, kısmen Apache-2.0.
- qoody: lisans bilgisi bulunamadı.
- dontkillmyapp.com: site "özgür yazılım" diyor, lisans türünü
  doğrulayamadım; metin kopyalanmadı, kendi cümlelerimle özetlendi.
- Stack Overflow cevapları: CC BY-SA; kod alınmadı.

## Sende için çıkarımlar

Ne yapalım:
- Bekçiyi süreç düzeyinde kur; kademe: +3 sn, +15 sn, +45 sn (aç/kapat),
  +60 sn karar. Neden: requestRebind tek başına çoğu zaman boş işlem,
  servis kapsamı onDestroy'da ölüyor.
- Kalıcı sağlık kaydı + boşluk raporu yaz. Neden: süreç öldüğünde
  uygulama ancak sonradan öğrenir; dürüst bir "şu saatler arası
  kapalıydı" kullanıcı güvenini artırır.
- Tepsi uzlaştırmasını "canlı kaçırma sayacı"na çevir. Neden:
  izinsiz, bedava ve tek gerçek zemin gerçeği tepsi.
- Önce Samsung ve Xiaomi rehberini kusursuz yap (Türkiye'de ≈ %77).
  Neden: veri bunu gösteriyor; geri kalan markalar ikinci tur.
- Rehberde Türkçe adları kullan, ama "adı değişebilir, ayar
  araması kullanın" de. Neden: resmi Türkçe kaynak yalnız Samsung.
- Her ayar adımında "Yaptım" onayı ve sistem sürümü değişince
  yeniden iste. Neden: otomatik başlatma okunamıyor; ayarlar güncelleme
  sonrası sıfırlanabiliyor.
- OEM ekranını `startActivity` + yakala ile aç; `resolveActivity`
  kullanma. Neden: paket görünürlüğü; QUERY_ALL_PACKAGES kullanmıyoruz.
- Pil için liste ekranına yönlendir ve durumu `isIgnoringBatteryOptimizations`
  ile oku. Neden: Play uyumlu; muaf uygulamalar bekleme kovasından da
  muaf, yani periyodik iş kısıtlanmaz.
- Bellek ve ana iş parçacığı disiplini koru. Neden: Android 14
  ANR→Restricted, Android 17 bellek sınırlayıcı.
- Düşük riskli ek: MY_PACKAGE_REPLACED ve sistem güncellemesi
  denetimi. Neden: Honor ve OnePlus güncelleme sonrası bozuluyor.
- Gerçek cihaz testi (4 marka x 7 gün) sürüm kabul şartı olsun.

Ne yapmayalım:
- Play sürümünde doğrudan pil muafiyeti diyaloğu veya sürekli ön plan
  servisi. Neden: politika ve inceleme riski; kazanç belirsiz.
- Gizli API ile üretici ayarı okumak, erişilebilirlik servisi,
  sessiz ses/pencere hileleri. Neden: Play ihlali ve kırılgan.
- Aç/kapat hilesini ilk basamak yapmak. Neden: onay kaybı yarışı ve
  10 sn sağır pencere; ikinci basamak ve sınırlı.
- "Silinen mesajı kurtarırız" demek. Neden: tepsiden silinen mesaj
  geri gelmez.
- Sağlık göstergesini kalabalık yapmak. Neden: tek ekran kuralı;
  sağlıklıyken yalnız küçük nokta.
- WorkManager'ı düşünmeden eklemek. Neden: 3 ek izin getirir; JobScheduler
  yeter, ya da izin sonuçlarını manifestte ölçerek ekle (S-14 kuralı).

Karar sorusu (kurucuya):
- Sideload sürümünde pil muafiyeti diyaloğu ve isteğe bağlı "canlı
  tut" seçeneği olsun mu? Öneri: evet, ikisi de varsayılan kapalı ve
  yalnız APK sürümünde.
