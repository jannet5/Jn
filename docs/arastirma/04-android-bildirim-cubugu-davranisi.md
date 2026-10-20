# Android bildirim çubuğu davranışı
Araştırma kolu: android-golge

Tarih: 20 Ekim 2026

Kod okunan sürümler: android-12.0.0_r1 ... android-17.0.0_r1 etiketleri ve
main dalı (AOSP).

## Kısa özet
- Android'de bildirim gövdesine dokunmak UYGULAMAYI açar. Genişletmek için
  ok (caret) kullanılır. Resmi belge bunu açık yazıyor.
  - https://developer.android.com/develop/ui/views/notifications/conversations
  - Kurucunun "dokununca aynı kutuda uzasın" isteği (I-31) bununla
    çelişiyor. Çözüm önerisi "Kurucu isteği ile çakışmalar" bölümünde.
- Otomatik gruplama eşiği Android 12'de 4, Android 13'ten beri 2 bildirim.
  Resmi grup belgesi hâlâ "4 veya daha fazla" yazıyor (eski kalmış).
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-13.0.0_r1/core/res/res/values/config.xml
- Daraltılmış grup en çok 2 satır + "+N" gösterir. Sistemin açtığı grup 5,
  kullanıcı açınca 8 satır gösterir (AOSP sabitleri).
- Konuşma avatarı SOLDA 48dp, uygulama rozeti 20dp. Konuşma olmayan
  bildirimde büyük ikon SAĞDA 48dp. Kurucunun "sağda profil resmi" tarifi
  ikinci türe uyuyor.
- Eylemler en çok 3 düğme. Yanıt = RemoteInput. SystemUI'nin kendi gönderme
  kodu okundu; bizim `sendReply` ondan 4 noktada ayrılıyor (Bölüm 4).
- Android 14+ hedefli uygulama başkasının PendingIntent'ini `send()` ile
  gönderirken ActivityOptions ile izin vermezse açılış sessizce başarısız
  olabilir. Bizim `IntentLauncher` bunu yapmıyor. En önemli teknik bulgu bu.
- Android 15: OTP gibi "hassas" bildirimler güvenilmeyen dinleyiciye
  gizlenir. Telefon numarası içeren sohbet adları da tetikliyor. Müşterisi
  kayıtsız numaralarla yazan esnaf için doğrudan sorun.
- Kendi başına kurulan (sideload) APK'da bildirim erişimi "kısıtlanmış ayar"
  ile kilitli gelir. Play'den kurulumda bu kilit yok.
- Bildirim kalkınca dinleyicinin içerik URI izni de kalkar. Fotoğraf
  bildirim gelir gelmez kopyalanmalı.
- Hareket: Material 3 Expressive "varsayılan uzamsal" yay: sönüm 0.8,
  sertlik 380 (yaklaşık 320 ms'de oturur). Android'in satır animasyonu 360
  ms.
- "Dokununca en üste kayma" hatasının ilacı: sabit anahtar + kaydırma
  sabitleme. SystemUI üstten eklenen satırda kaydırma ofsetini satır
  yüksekliği kadar artırır.
- Doğrulanamayan: WhatsApp, Instagram, TikTok, X, YouTube bildirimlerinin
  gerçek yapısı. Ortamda telefon/emülatör yok. Test yolu aşağıda.

## Nasıl araştırdım
- Android kaynak kodu: android.googlesource.com'dan anonim okundu.
  - SystemUI: ExpandableNotificationRow, NotificationChildrenContainer,
    NotificationClicker, StatusBarNotificationActivityStarter,
    RemoteInputViewController, NotificationRemoteInputManager,
    RemoteInputCoordinator, ConversationCoordinator, RowAppearance...,
    StackStateAnimator, NotificationStackScrollLayout.
  - Çerçeve: Notification, NotificationListenerService,
    NotificationManagerService, GroupHelper, ConversationLayout,
    MessagingLayout, DateTimeView, dimens/config/layout dosyaları.
  - Lisans: AOSP Apache-2.0. Kod kopyalanmadı; sabitler ve akışlar not
    alındı.
- Resmi belgeler: developer.android.com, support.google.com, Android
  Developers Blog.
- Topluluk: Reddit (arşiv API'si; r/androiddev, r/tasker), Stack Overflow
  (API), KDE Connect (MR, e-posta listesi, kaynak kod), AutoResponder blogu,
  XDA / 9to5Google / Android Police haberleri.
  - KDE Connect lisansı GPL-2.0-only OR GPL-3.0-only (başlıkta yazıyor).
    Yalnız okundu, hiçbir şey alınmadı.
- Okuma işaretleri:
  - [resmi] Google belgesi.
  - [AOSP] kaynak kod okundu (satır/sabit doğrulandı).
  - [topluluk] kullanıcı/geliştirici anlatımı.
  - [tek kaynak] tek bağımsız kaynak; dikkatli oku.
  - [çıkarım] kaynaklardan benim çıkardığım sonuç.
  - [doğrulanamadı] kanıt bulunamadı veya okunamadı.
- Main dalı yayınlanmış sürümden ileride olabilir. Fark varsa yazdım.
- Hiç uygulama çalıştırılmadı. Sıfır cihaz testi var.

## 1. Gruplama kuralları
- Uygulama başına gruplama [resmi]
  - Aynı uygulamanın bildirimleri `setGroup()` anahtarıyla toplanır. Her
    grup için bir özet (`setGroupSummary(true)`) şart.
  - Android 7+ cihazda özetin kendi metni gösterilmez; çocuklardan kesitler
    gösterilir.
  - https://developer.android.com/develop/ui/views/notifications/group
- Özetten ne alınır [resmi]
  - Özetin alt metni, contentIntent'i ve deleteIntent'i daraltılmış grubu
    çizmek için kullanılır.
  - https://android-developers.googleblog.com/2016/06/notifications-in-android-n.html
- Otomatik gruplama eşiği [AOSP]
  - `config_autoGroupAtCount`: Android 12 = 4. Android 13, 14, 15, 16 ve 17
    = 2.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-12.0.0_r1/core/res/res/values/config.xml
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/values/config.xml
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-17.0.0_r1/core/res/res/values/config.xml
  - Resmi belge hâlâ "4 veya daha fazla" diyor. [çıkarım] kod daha yeni,
    kodu esas al.
  - https://developer.android.com/develop/ui/views/notifications/group
- Otomatik özetin içeriği [AOSP]
  - Sistemin ürettiği özetin dokunma hedefi uygulamanın BAŞLATMA (launcher)
    intent'idir. Yani otomatik gruba dokunmak o uygulamanın ana ekranını
    açar, sohbeti değil.
  - Kodda: `createAutoGroupSummary` içinde `getLaunchIntentForPackage`.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
  - Kurucunun "ana sayfaya değil, ilgili yere git" isteği (I-39) bu yüzden
    çocuk bildirimin kendi contentIntent'ini ister.
- Zorla gruplama (Android 16 kodu) [AOSP]
  - Bir uygulama 3'ten fazla küçük grup (her biri 3'ten az çocuklu) açarsa
    sistem hepsini tek "Aggregate_" grubuna toplar.
  - Sabitler: `MIN_CHILD_COUNT_TO_AVOID_FORCE_GROUPING = 3`,
    `AUTOGROUP_SPARSE_GROUPS_AT_COUNT = 3`.
  - Bayrak arkasında (`notificationForceGrouping`). Yayınlanmış cihazlarda
    açık mı: [doğrulanamadı].
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/GroupHelper.java
- Konuşma bölümü [resmi] [AOSP]
  - Telefonlarda gölgenin en üstünde yalnız gerçek zamanlı konuşmalar
    (arama, sohbet, grup sohbeti) için ayrı bir bölüm var.
  - https://developer.android.com/develop/ui/views/notifications/conversations
  - Bölüm sırası: öncelikli kişiler, kişiler, sonra uyarılı ve sessiz.
    Önemli konuşma kendi uygulama grubundan çıkarılıp üst düzeye taşınır
    (`shouldPromoteToTopLevel`).
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator.kt
- Bildirim "konuşma" sayılma şartı [resmi]
  - MessagingStyle kullanmalı; Android 11+ hedefte geçerli uzun ömürlü
    kısayola bağlı olmalı; kullanıcı kanalı konuşmadan düşürmemiş olmalı.
  - https://developer.android.com/develop/ui/views/notifications/conversations
  - Dinleyici aynı bilgiyi `Ranking.isConversation()` ile okur.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/service/notification/NotificationListenerService.java
- Sınıflandırma ve "bundle" (Android 16 kodu) [AOSP]
  - Bildirim türü: promosyon, sosyal medya, haber, öneri. Bunlar
    "android.app.promotions / social / news / recs" sistem kanallarına
    taşınır ve her biri kendi bölümünde gruplanır.
  - Varsayılan izinli tür yalnız promosyon
    (`DEFAULT_ALLOWED_ADJUSTMENT_KEY_TYPES`).
  - Bayrak arkasında (`notificationClassification`). [doğrulanamadı] hangi
    cihazda açık.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/service/notification/Adjustment.java
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/app/NotificationChannel.java
  - Haber düzeyinde: "benzer temalı bildirimler susturulup gruplanır". Beta
    dizgisinden; yayınlanmış davranış [doğrulanamadı].
  - https://9to5google.com/2025/03/13/android-16-notification-summaries/
- Grup başlığındaki saat ve sayı [AOSP]
  - Saat: çocukların `when` değerlerinden geçmişteki en yenisi.
  - Sayı: kesilmemiş (görünenden bağımsız) çocuk sayısı.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/collection/coordinator/GroupWhenCoordinator.kt
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/collection/coordinator/GroupCountCoordinator.kt
- Sistem sınırları [AOSP]
  - Bir uygulama en çok 50 aktif bildirim tutabilir
    (`MAX_PACKAGE_NOTIFICATIONS`). MessagingStyle en çok 25 mesaj tutar
    (`MAXIMUM_RETAINED_MESSAGES`).
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/app/Notification.java
- Dinleyici (biz) için sonuç
  - Özet bildirimleri (`FLAG_GROUP_SUMMARY`) mesaj olarak sayma. KDE Connect
    da eler. [topluluk]
  - https://invent.kde.org/network/kdeconnect-android/-/blob/master/src/main/java/org/kde/kdeconnect/plugins/notifications/NotificationsPlugin.kt
  - Özetin üzerinde yanıt eylemi bulunmaz; çocuk bildirimi kullan.
    [topluluk] [tek kaynak, 2017]
  - https://www.reddit.com/r/androiddev/comments/6hxf5z/notificationlistenerservice_reply_in/
  - Sistem otomatik özeti `FLAG_AUTOGROUP_SUMMARY` taşır; onu da ele. [AOSP]
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/GroupHelper.java

## 2. Daraltılmış ve genişletilmiş görünüm
- Grup satır sayıları [AOSP]
  - Daraltılmış grup: en çok 2 çocuk satırı
    (`NUMBER_OF_CHILDREN_WHEN_COLLAPSED = 2`).
  - Satır sistem tarafından genişletilmişse (ilk bildirim) veya heads-up
    ise: 5.
  - Kullanıcı grubu açınca: en çok 8 (`..._CHILDREN_EXPANDED = 8`).
  - Kalan çocuklar daraltılmışta "+N" sayacı olarak görünür.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/stack/NotificationChildrenContainer.java
  - Açık grupta 8'den fazla çocuğun ne olduğu: [doğrulanamadı]. Kod 8'de üst
    sınır koyuyor.
- Grup sayısı ok içinde (yeni) [AOSP] [doğrulanamadı]
  - Main dalında `config_showNotificationGroupCountInExpander = true`: sayı
    ok hapının (pill) içinde görünür. android-17.0.0_r1 etiketinde bu ayar
    yok. Yani çok yeni, yayınlanmış cihazda görülmeyebilir.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/res/res/values/config.xml
- Daraltılmış standart bildirim [AOSP]
  - Başlık ve metin tek satır; sonu "..." (singleLine + ellipsize=end).
  - Genişletilmiş BigText çok satır (singleLine=false).
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/layout/notification_template_material_base.xml
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/layout/notification_template_material_big_text.xml
- Daraltılmış konuşma [AOSP]
  - Yalnız SON mesaj, tek satır (`setMaxDisplayedLines(1)`). Uygulama adı
    gizli. Avatar solda.
  - Genişletilmişte tüm mesajlar (en çok 25) ve uygulama adı görünür.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/com/android/internal/widget/ConversationLayout.java
- Yükseklik sınırları [AOSP]
  - Genişletilmiş bildirim en çok 358dp (`notification_max_height`).
  - Asgari yükseklik 88dp; yeni "2025 tasarımı" şablonunda 72dp.
  - Kart köşesi 28dp, grup içi çocuk köşesi 4dp, satır arası 2dp, yan boşluk
    16dp.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/packages/SystemUI/res/values/dimens.xml
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/values/dimens.xml
  - "2025 tasarımı" şablonları (`notification_2025_*`) bayrak arkasında.
    Hangi cihazda göründüğü [doğrulanamadı].
- Dokununca ne olur [resmi] [AOSP]
  - Konuşmada: bildirime dokunmak konuşmayı UYGULAMADA açar. Oka dokunmak
    yeni mesajları tam uzunlukta genişletir.
  - https://developer.android.com/develop/ui/views/notifications/conversations
  - Kodda satır tıklaması `NotificationClicker`: contentIntent varsa
    uygulamayı başlatır. Grup açıkken çocukların ARASINA dokunmak uygulamayı
    açmaz ("in between the notifications").
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/NotificationClicker.java
  - Grup başlığı (uygulama adı + ok) tıklaması aç/kapa yapar
    (`setGroupHeader(..., mExpandClickListener)`).
  - Grup kapalıyken çocuk satırları tıklanamaz/odaklanamaz
    (`updateClickAndFocus`). Dokunuş özet satırına gider.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/row/ExpandableNotificationRow.java
  - Kapalı grubun gövdesine dokununca özetin contentIntent'i çalışır
    (varsa). Gerçek cihazda sınanmadı: [doğrulanamadı].
- Ok (chevron) [AOSP]
  - Ok, içinde sayı olabilen bir "hap" (pill). Sayı: konuşmada okunmamış
    mesaj sayısı (`EXTRA_CONVERSATION_UNREAD_MESSAGE_COUNT`).
  - Sayı yalnız DARALTILMIŞKEN ve 1'den büyükse görünür; 100 ve üstü "99+"
    yazar (`shouldShowNumber`, `unread_convo_overflow`).
  - Konuşmada ok dokunma alanı 88dp yüksekliğinde
    (`conversation_expand_button_height`). Başlıktaki ok alanı 56dp
    (`notification_header_expand_icon_size`).
  - Genişleyince simge "daralt" simgesine (`ic_collapse_notification`) döner
    ve erişilebilirlik metni değişir.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/com/android/internal/widget/NotificationExpandButton.java
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/com/android/internal/widget/ConversationLayout.java
- Kendiliğinden genişleme [AOSP]
  - Gölgedeki ilk gruplanmamış genişletilebilir bildirim kendiliğinden
    genişler (`config_autoExpandFirstNotification = true`). Hepsini
    genişletme kapalı (`config_alwaysExpandNonGroupedNotifications=false`).
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/packages/SystemUI/res/values/config.xml
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/collection/coordinator/RowAppearanceCoordinator.kt
- Animasyon süreleri [AOSP]
  - Satır yüksekliği/yer değişimi: 360 ms. Köşe yarıçapı: 200 ms.
    Görünme/kaybolma: 464 ms. Kaydırıp atma: 200 ms. Yanıt kutusunu kapatma:
    150 ms. Heads-up görünme: 400 ms.
  - Eğri: `FAST_OUT_SLOW_IN`.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/stack/StackStateAnimator.java
- Genişlerken kaydırma [AOSP]
  - Satır boyu değişince `onChildHeightChanged` listeyi yeniden ölçer,
    kaydırma konumunu "sıkıştırır" (clamp) ve alttaki satır açılıyorsa
    konumu ona göre düzeltir. Liste yeniden SIRALANMAZ.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/stack/NotificationStackScrollLayout.java
  - Ekranın ÜSTÜNDE (görünmeyen alanda) yeni satır eklenirse kaydırma ofseti
    satır yüksekliği kadar artırılır; görünen satırlar yerinde kalır
    (`updateScrollStateForAddedChildren`). Kurucunun "dokununca en üste
    kayma" şikâyetinin Android'deki karşılığı bu kuraldır.

## 3. Mesaj bildirimleri
- Konuşma avatarı [AOSP]
  - Boyut 48dp, SOLDA. İçerik soldan 80dp'de başlar. Köşeye 20dp'lik
    uygulama rozeti biner ve 4dp dışarı taşar.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/layout/notification_template_conversation_icon_container.xml
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/values/dimens.xml
  - Boyutlar Android 13, 14, 15 ve 16'da aynı (48dp avatar, 52dp içerik
    kenar boşluğu, 56dp başlık).
- Konuşma OLMAYAN bildirim [AOSP]
  - Büyük ikon SAĞDA, 48dp (`notification_right_icon_size`).
  - Başlıkta uygulamanın küçük ikonu 24dp'lik daire içinde, solda.
  - Kurucunun "kişinin sağ tarafta profil fotoğrafı" tarifi bu türe uyar.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/values/dimens.xml
- Konuşma üst satırı [AOSP]
  - "Konuşma adı • Uygulama adı • zaman". Daraltılmışta uygulama adı yok.
  - Konuşma adı 16sp.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/layout/notification_template_conversation_header.xml
- Zaman biçimi [AOSP]
  - Göreli kısa metin: 1 dakikadan az "şimdi", 1 saatten az dakika, 1 günden
    az saat, sonra gün.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/java/android/widget/DateTimeView.java
  - Eşikler `DateTimeView` sınıfında. Metin dizgilerinin Türkçe karşılığı:
    [doğrulanamadı].
- Grup sohbeti [AOSP]
  - Başlık `conversationTitle`. Avatar yerine son iki göndericinin "yüz
    yığını" (face pile, 32dp).
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/com/android/internal/widget/ConversationLayout.java
  - Genişletilmişte her mesaj grubunun üstünde gönderen adı görünür; ad
    boşsa gizlenir. Daraltılmışta tek satır olur.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/com/android/internal/widget/MessagingGroup.java
  - Birebir sohbette mesaj grubu için ayrıca küçük avatar çizilmez
    (`setShowingAvatar(!mIsOneToOne && !mIsCollapsed)`).
- Avatar yoksa [AOSP]
  - Sistem baş harfli renkli bir daire üretir (`createAvatarSymbol`). Bizim
    de aynısını yapmamız tutarlı olur.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/com/android/internal/widget/ConversationLayout.java
- Dinleyici hangi alanı okur [AOSP]
  - `EXTRA_MESSAGING_PERSON`, `EXTRA_CONVERSATION_ICON`, `EXTRA_LARGE_ICON`,
    `EXTRA_CONVERSATION_TITLE`, `EXTRA_IS_GROUP_CONVERSATION`,
    `EXTRA_MESSAGES`, `EXTRA_CONVERSATION_UNREAD_MESSAGE_COUNT`.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/app/Notification.java
- Boş sohbet adı tuzağı [topluluk] [tek kaynak]
  - Bazı uygulamalar (örnek: KakaoTalk) grup sohbetinde
    `conversationTitle`'ı boş bırakır; başlıkta son göndericiyi yazar.
  - Çözüm: Android 11+'da `Ranking.getConversationShortcutInfo()` etiketi
    oda adıdır. İzin gerekmez; bağlandıktan hemen sonra null olabilir.
  - https://www.reddit.com/r/androiddev/comments/1x9wetf/notificationlistenerservice_gotchas_from_building/
  - API doğrulaması [AOSP]: "null olabilir, uygulama tam konuşma
    özelliklerine geçmediyse".
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/service/notification/NotificationListenerService.java
  - Not: bu Reddit gönderisini moderatör "yapay zekâ ile yazılmış olabilir"
    diye uyardı. Bu yüzden tek kaynak sayıyorum; teknik ayrıntıları resmi
    belge/AOSP ile çapraz doğruladım.

## 4. Eylemler
- Android'in eylem modeli [AOSP] [resmi]
  - Bildirimde en çok 3 eylem düğmesi (`MAX_ACTION_BUTTONS = 3`).
  - Anlam etiketleri (`SEMANTIC_ACTION_*`): REPLY, MARK_AS_READ,
    MARK_AS_UNREAD, DELETE, ARCHIVE, MUTE, UNMUTE, THUMBS_UP, THUMBS_DOWN,
    CALL, MARK_CONVERSATION_AS_PRIORITY ve CONVERSATION_IS_PHISHING.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/app/Notification.java
  - Düğme başlığı ("Yanıtla", "Beğen", "İlgileniyorum") uygulamanın kendi
    metnidir. Sabit bir liste yok. Biz başlığı olduğu gibi gösteririz. Anlam
    etiketi yalnız simge/sıra için ipucu olur.
  - `actionIntent` boş olabilir; sistem eylemi pasif çizer.
    `isAuthenticationRequired()` doğruysa cihaz kilitliyken önce kilit
    açtırılmalı (API 31).
  - https://developer.android.com/reference/android/app/Notification.Action
- SystemUI yanıtı nasıl gönderir [AOSP]
  - Yanıt eylemi: serbest metin kabul eden `RemoteInput` taşıyan eylem.
    Birden fazla serbest-metin girdisi varsa SystemUI iki yerde farklı
    seçiyor (tıklama yolunda sonuncu, yenileme yolunda ilki). Pratikte tek
    girdi olur.
  - Gönderim sırası:
    1. `Intent().addFlags(FLAG_RECEIVER_FOREGROUND)`
    2. `RemoteInput.addResultsToIntent(eylemin TÜM girdileri, intent,
       sonuç)`
    3. `RemoteInput.setResultsSource(intent, SOURCE_FREE_FORM_INPUT)`
    4. `pendingIntent.send(ctx, 0, intent, null, null, null, options)` ve
       options içinde ActivityOptions
       `MODE_BACKGROUND_ACTIVITY_START_ALLOWED`.
  - Cihaz kilitliyse önce kilit ekranı gösterilir
    (`showBouncerIfNecessary`).
  - Gönderirken düğme gizlenir, ilerleme çarkı döner. Çark, uygulama
    bildirimi güncelleyene kadar sürer; zaman aşımı kodda bulunamadı.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/policy/RemoteInputViewController.kt
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/NotificationRemoteInputManager.java
  - Yanıt gidince uygulama bildirimi iptal ederse SystemUI satırı 500 ms
    tutar (uygulamaya güncelleme şansı), başka eyleme dokunulursa 200 ms.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator.kt
  - Resmi beklenti: uygulama yanıtı işleyince AYNI kimlikle `notify()` ile
    bildirimi güncellemeli; bu yanıt kutusunu kapatır.
  - https://android-developers.googleblog.com/2016/06/notifications-in-android-n.html
- Bizim `LiveActionRegistry.sendReply` ile farklar [AOSP] [çıkarım]
  - (okuduğum sürüm: 20 Ekim 2026; kod sonradan değişmiş olabilir)
  - Biz yalnız seçilen tek `RemoteInput`'a sonuç ekliyoruz; SystemUI eylemin
    tüm girdilerini veriyor. KDE Connect de hepsine aynı metni yazıyor.
  - `setResultsSource` yok (API 28+).
  - `FLAG_RECEIVER_FOREGROUND` yok.
  - `send()` ActivityOptions'sız. Hedef 34+ için opt-in eksik.
  - `isAuthenticationRequired` bakılmıyor.
  - https://invent.kde.org/network/kdeconnect-android/-/blob/master/src/main/java/org/kde/kdeconnect/plugins/notifications/NotificationsPlugin.kt
- Hangi uygulamalar yanıtı kabul eder [topluluk]
  - 2017, KDE Connect: WhatsApp'ta çalıştı; Telegram ve varsayılan SMS
    uygulamasında çalışmadı. Telegram'ın RemoteInput yerine özel arayüz
    kullandığı tahmin edildi. [tek kaynak, eski]
  - https://mail.kde.org/pipermail/kdeconnect/2017-May/001969.html
  - Aynı geliştirici: bazı "Reply" eylemleri yalnız uygulamayı açıyor
    (e-posta uygulaması örneği). Yani her "Yanıtla" satır içi değil.
  - https://mail.kde.org/pipermail/kdeconnect/2017-May/001971.html
  - 2017, Reddit: yanıt `WearableExtender` eylemlerinden alınınca WhatsApp,
    Facebook ve Telegram'da çalıştı. Eylemler ÖZET bildirimde değil çocuk
    bildirimde. [tek kaynak, eski]
  - https://www.reddit.com/r/androiddev/comments/6hxf5z/notificationlistenerservice_reply_in/
  - 2026-06, Reddit: Instagram DM ve bazı WhatsApp sürümleri RemoteInput
    gönderimini "sessizce reddediyor"; Android 13+ sonrası bazı
    bildirimlerde satır içi yanıt eylemi görünmüyor. Cevaplanmamış soru.
    [tek kaynak] [doğrulanamadı]
  - https://www.reddit.com/r/androiddev/comments/1ufrd0m/using_remoteinputapi_to_dispatch_aigenerated/
  - Soru: Stack Overflow'da "WhatsApp bildirimine programla yanıt" sorusu
    cevapsız kaldı.
  - https://stackoverflow.com/questions/71532612/reply-programmatically-to-whatsapp-notification
  - [çıkarım] Yanıt düğmesini YALNIZ canlı bir serbest-metin RemoteInput
    varsa göster. Yoksa "Uygulamada yanıtla" göster (contentIntent).
  - [çıkarım] RemoteInput'ta hazır seçenekler (`getChoices()`) varsa onları
    çip olarak sunabiliriz; SystemUI bu durumda kaynak olarak
    `SOURCE_CHOICE` yazar.
  - https://developer.android.com/reference/android/app/RemoteInput
- Eylemin ömrü, süreç ölümü [resmi] [AOSP]
  - PendingIntent sistemde tutulan bir belirteçtir. Sahibi uygulamanın
    süreci ölse de başka süreçler kullanabilir.
  - https://developer.android.com/reference/android/app/PendingIntent
  - Ama BİZİM sürecimiz ölürse elimizdeki nesne gider. PendingIntent diske
    yazılamaz. İçindeki Intent'i çıkarıp başlatmak "exported değil" güvenlik
    hatası verir. [topluluk]
  - https://stackoverflow.com/questions/40586609/how-can-i-save-pendingintent-to-sqlite-database
  - Çözüm: `onListenerConnected()` sonrasında `getActiveNotifications()` ile
    taze nesneleri yeniden al. Resmi belge: dinleyici bağlanma olayını
    beklemeli; bağlanmadan çağrı hata verebilir.
  - https://developer.android.com/reference/android/service/notification/NotificationListenerService
  - Aynı kalıbı Reddit'te bildiren geliştirici: bağlanır bağlanmaz boşluk
    doldurma; bağlanma bitmeden çağırınca SecurityException. [tek kaynak]
  - https://www.reddit.com/r/androiddev/comments/1x9wetf/notificationlistenerservice_gotchas_from_building/
  - Bildirim kalkmışsa (uygulama iptal etti/kullanıcı sildi) eski nesne
    `CanceledException` verebilir. Yedek: uygulamayı aç. [çıkarım]
- Okundu işaretleme: dikkat, karşı tarafa görünür [resmi] [topluluk]
  - Android'de sistem düzeyi "okundu" yok. Yalnız uygulamanın sunduğu
    `MARK_AS_READ` eylemi var (uygulamaya ait iş).
  - WhatsApp'ın "Mark as read" düğmesi bildirimi kapatır VE mesajı okundu
    yapar (karşı tarafa mavi tik gider, ayar açıksa). [tek kaynak, 2018]
  - https://ubergizmo.com/2018/07/whatsapp-mark-as-read-button-notifications
  - [çıkarım] TekPanel'deki "Okundu" (I-40, I-41) varsayılan olarak YEREL
    olmalı. Kaynak uygulamadaki "okundu" eylemini tetiklemek karşı tarafa
    "görüldü" göndermek demek; ayrı ve açık bir seçenek olsun.
- Bildirimi bizden kaldırmak [resmi] [AOSP]
  - Resmi belge: kullanıcı kendi arayüzünde bildirimi kapatınca
    `cancelNotification(key)` çağır. contentIntent'i çalıştırdıysan ve
    `FLAG_AUTO_CANCEL` varsa yine çağır.
  - https://developer.android.com/reference/android/service/notification/NotificationListenerService
  - Kodda dinleyici iptali uygulamanın `deleteIntent`'ini ÇALIŞTIRIR
    (kullanıcı kaydırmış gibi) ve `FLAG_ONGOING_EVENT` bildirimleri iptal
    edemez.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
  - Medya/arama/navigasyon bildirimini iptal etmek oturumu bozar (YouTube
    Music çalma düğmesi örneği). Filtre: `isOngoing`, kategori
    TRANSPORT/SERVICE/NAVIGATION/CALL. [tek kaynak]
  - https://www.reddit.com/r/androiddev/comments/1v8xbmw/notificationlistenerservice_trap_classifier_was/
- Sessize al ve ertele [resmi]
  - Dinleyici `snoozeNotification(key, ms)` çağırabilir (API 26). Bildirim
    kalkar, süre dolunca tekrar gelir.
  - Kanal ayarı değiştirme (`updateNotificationChannel`) yalnız eşlik eden
    cihaz (companion) veya asistan olan dinleyiciye açık. Biz kullanamayız.
  - https://developer.android.com/reference/android/service/notification/NotificationListenerService
  - Sonuç: "Sessize al" = TekPanel içinde o konuşmayı susturmak. Uygulama
    kendi `MUTE` eylemini sunuyorsa onu ayrıca çalıştırabiliriz.
- Güvenlik [AOSP] [çıkarım]
  - PendingIntent göndermek, o uygulamanın kimliğiyle iş yaptırmaktır.
    Yalnız kullanıcı dokunuşuyla gönder. Otomatik yanıt/otomatik eylem
    yazma.
  - Gizlenmiş (redakte) bildirimde eylem başlıkları BOŞ olur ama
    PendingIntent durur. Başlığı boş eylemi gösterme.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
  - Eylemleri ve yanıt metnini günlüğe (log) yazma. Dışa aktarma
    sözleşmesiyle uyumlu.

## 5. Dokununca ilgili yere git
- SystemUI akışı (kodda) [AOSP]
  1. Gerekirse önce kilit açtırır.
  2. `resumeAppSwitches()` çağırır.
  3. Yarım yazılmış yanıt taslağı varsa fill-in intent'e
     `EXTRA_REMOTE_INPUT_DRAFT` koyar.
  4. `contentIntent.sendAndReturnResult(ctx, 0, fillIn, ..., options)`.
  5. `FLAG_AUTO_CANCEL` varsa satırı kaldırır (neden: REASON_CLICK).
  6. Sistem servisine "tıklandı" bildirir.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/phone/StatusBarNotificationActivityStarter.java
  - [çıkarım] Üçüncü taraf dinleyici için "tıklandı" bildirme API'si yok
    (listede göremedim). Elimizde yalnız `cancelNotification` var (neden:
    LISTENER_CANCEL).
- Android 14 ve sonrası: gönderen izin vermeli [resmi]
  - Hedefi 34+ olan uygulama başkasının PendingIntent'ini gönderirken kendi
    arka-plan-açılış ayrıcalığını ActivityOptions ile açıkça vermeli.
    Vermezse açılış engellenebilir (yaratıcı vermediyse).
  - Önerilen mod: `MODE_BACKGROUND_ACTIVITY_START_ALLOW_IF_VISIBLE` (SDK
    36'da eklendi). Yalnız gönderen uygulama ekrandayken izin verir. Daha
    geniş mod `..._ALLOWED` (SDK 34).
  - https://developer.android.com/guide/components/activities/background-starts
  - https://developer.android.com/about/versions/14/behavior-changes-14
  - Bir geliştirici Android 14'te `send()`'in "hata vermeden hiçbir şey
    yapmadığını" yazdı; ActivityOptions ile çözülmüş. [tek kaynak]
  - https://www.reddit.com/r/androiddev/comments/1x9wetf/notificationlistenerservice_gotchas_from_building/
  - Bizim hedef SDK 36. Etkilenir. Sürüm koruması şart: `ALLOW_IF_VISIBLE`
    API 36, `ALLOWED` API 34, daha eskide seçenek yok.
- Bildirim tuzağı (trampolin) kısıtı, Android 12 [resmi]
  - Hedefi 12+ olan uygulama, bildirime dokunulduktan sonra servis veya
    alıcı üzerinden activity başlatamaz. Bu KAYNAK uygulamanın sorunu;
    contentIntent doğrudan activity ise sorun yok. Bizim gönderimimizde de
    aynı kural işler mi: [doğrulanamadı].
  - https://developer.android.com/about/versions/12/behavior-changes-12
- Derin bağlantı kimde [çıkarım]
  - contentIntent, kaynak uygulamanın kendi derin bağlantısını taşır
    (sohbet, sipariş, canlı yayın, yorum). Çözmek bizim işimiz değil;
    PendingIntent'i göndermek yeter.
  - İçindeki Intent'i alıp biz başlatamayız (güvenlik hatası), o yüzden
    "linke git" = PendingIntent göndermek.
  - Kurucunun TikTok (canlı yayına davet), YouTube (yorum/beğeni) örnekleri
    bu yolla çalışmalı. Gerçek uygulamada: [doğrulanamadı].
- Süreç ölünce veya bildirim kalkınca yedek sırası [çıkarım]
  1. Bellekteki canlı contentIntent.
  2. `getActiveNotifications()` ile aynı anahtarlı taze bildirim.
  3. Kaynak uygulamanın başlatma (launcher) intent'i.
  4. Hata mesajı: "Uygulama açılamadı".
  - İsteğe bağlı 2.5: herkese açık bağlantılar. Telegram
    `tg://resolve?domain=<ad>`; WhatsApp `wa.me/<numara>` (click to chat).
    Bunlar için numara/kullanıcı adı bildirimde yok; yalnız
    çıkarılabiliyorsa.
  - https://core.telegram.org/api/links
  - https://faq.whatsapp.com/5913398998672934
  - (WhatsApp sayfası JS ile çiziliyor; metin okunamadı: [doğrulanamadı].)
  - Reddit'teki geliştirici son 500 kaydı bellekte tutup yedek olarak
    başlatma intent'ine düşüyor. [tek kaynak]
  - https://www.reddit.com/r/androiddev/comments/1x9wetf/notificationlistenerservice_gotchas_from_building/
- Sistemin otomatik grup özeti ana ekranı açar [AOSP]
  - Bu yüzden grup başlığına değil, çocuk satırının kendi contentIntent'ine
    güven (Bölüm 1).
- Balonlar (bubbles) [resmi]
  - Balon yalnız bildirimi yayınlayan uygulamaya aittir ve kısayol ister.
    Üçüncü taraf dinleyici başkası için balon açamaz. Balonlu konuşmayı
    ayrıca kopya saymamaya dikkat.
  - https://developer.android.com/develop/ui/views/notifications/bubbles

## 6. Büyük resim ve medya mesajları
- Büyük resim alanları [AOSP] [resmi]
  - `EXTRA_PICTURE` (bitmap), `EXTRA_PICTURE_ICON` (Icon; içerik URI'si
    olabilir), `EXTRA_PICTURE_CONTENT_DESCRIPTION`. Genişletilmişte küçük
    görsel için `bigLargeIcon`.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/app/Notification.java
  - https://developer.android.com/develop/ui/views/notifications/expanded
- Sistem büyük resmi nasıl çizer [AOSP]
  - En çok 416dp x 284dp'ye küçültür (düşük RAM cihazda 294 x 208dp).
  - Önce yer tutucu (placeholder) çizer, resim gelince değiştirir; ekran
    dışına çıkan resmin belleğini serbest bırakır.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/row/BigPictureIconManager.kt
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/values/dimens.xml
- Mesaj içi resim [AOSP]
  - Satır içinde: en az 44dp, en çok 136dp yükseklik, köşe 4dp.
    Daraltılmışta sağda 40 x 40dp kutuda ayrı gösterilir.
  - Mesaj paketindeki anahtarlar: "text", "time", "sender", "sender_person",
    "type" (MIME), "uri", "extras".
  - https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-16.0.0_r1/core/res/res/layout/notification_template_material_conversation.xml
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/app/Notification.java
  - Yalnız `image/*` türü satır içi resim olur. Ses/video için küçük resim
    yok; "Video" gibi etiket göstermek gerekir. [çıkarım]
- Dinleyici medyayı nasıl, ne zamana kadar okur [AOSP]
  - Sistem, bildirimdeki içerik URI'lerine DİNLEYİCİ PAKETİNE okuma izni
    verir ve bunu dinleyiciye haber vermeden ÖNCE yapar.
  - Bildirim kalkınca veya güncellemede URI düşünce izin geri alınır (iptal
    edilen bildirimin izin sahibi yok edilir).
  - Sonuç: baytları bildirim gelir gelmez al ve kopyala. Sonra erişemezsin.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
  - Dikkat: "Android erişim izni verirse görünür" sınırı budur (MIMARI.md
    ile uyumlu).
- Topluluk tuzakları [topluluk] [tek kaynak]
  - Fotoğraf yoksa bile `containsKey(EXTRA_PICTURE)` ve `EXTRA_LARGE_ICON`
    neredeyse hep doğrudur. Büyük ikon profil resmi veya logodur, ek
    değildir. Değer boş (null) iken `containsKey` yine true döner.
  - Sohbet fotoğrafları `EXTRA_PICTURE`'da gelmez; `MessagingStyle` mesaj
    paketlerinde içerik URI'si olarak gelir.
  - `EXTRA_MESSAGES` birkaç mesaj tutar; hepsini taramak, eski fotoğraflı
    sohbette yeni metni "fotoğraf" diye işaretler. Yalnız SON mesaja bak.
  - Resim çözme/yeniden kodlama yüzlerce ms sürebilir. Bildirim
    geri-çağrısında (binder iş parçacığı) yapma; tek iş parçacıklı
    yürütücüye al. Geliştirici 512 px uzun kenar, JPEG kalite 75 kullandı ve
    resimleri ayrı tabloda tuttu.
  - https://www.reddit.com/r/androiddev/comments/1x9wetf/notificationlistenerservice_gotchas_from_building/
  - Bizim `NotificationExtractor`: `hasBigPicture` değeri
    `containsKey(EXTRA_PICTURE) || containsKey("android.pictureIcon")` ile
    hesaplanıyor. Yukarıdaki tuzağa açık. [çıkarım] değerin boş olmadığına
    bakmalı.
- Saklama [çıkarım]
  - Kopyala, küçült (en çok ~1024 px), içerik adresli dosya olarak sakla.
    Bildirimin ömrüne bağlı kalma. Yedekten hariç (MIMARI ile uyumlu).
  - Kullanıcı "Okundu" veya "Temizle" deyince dosyayı da sil. Müşteri
    fotoğrafı kişisel veridir.
  - Sistemin kendi örneği: yer tutucu + ekran dışında bellek serbest
    bırakma. Biz de aynı kalıbı kullanalım (liste akıcılığı, I-44).

## 7. Android 12-17 değişiklikleri
- Android 12 [resmi] [AOSP]
  - Bildirim tuzağı (trampolin) kısıtı (Bölüm 5).
  - https://developer.android.com/about/versions/12/behavior-changes-12
  - Kullanıcı artık dinleyicinin göreceği bildirim TÜRLERİNİ ve uygulamaları
    kısıtlayabilir: konuşma, uyarılı, sessiz, devam eden
    (`FLAG_FILTER_TYPE_*`, API 31). Sistem her bildirim için hem türü hem
    paketi kontrol eder (`isVisibleToListener`).
  - https://developer.android.com/reference/android/service/notification/NotificationListenerService
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
  - Dinleyici kendi filtresini okuyamaz. "Bazı mesajlar gelmiyor"
    şikâyetinin bir sebebi olabilir. Bu ayarın hangi dinleyiciler için,
    hangi menüde göründüğü cihazdan cihaza değişir: [doğrulanamadı].
- Android 13 [resmi]
  - `POST_NOTIFICATIONS`: bildirim göndermek için çalışma zamanı izni.
    Kullanıcı bir uygulamaya "İzin verme" derse o uygulamanın bildirimi hiç
    yayınlanmaz. Yani bizim listemize de düşmez. Boş ekranda "Bu uygulamanın
    bildirimleri Android ayarlarında kapalı olabilir" ipucu ver. TekPanel
    kendi bildirimini gönderecekse izin ister.
  - https://developer.android.com/develop/ui/views/notifications/notification-permission
  - Kısıtlanmış ayarlar (restricted settings): oturum tabanlı olmayan paket
    yükleyiciyle kurulan (tarayıcı, mesaj, dosya yöneticisi) uygulamada
    bildirim dinleyici anahtarı kapalı gelir. Mağazalar oturum tabanlı
    yükleyici kullandığı için Play kurulumu etkilenmez. [topluluk, XDA]
  - https://www.xda-developers.com/android-13-restricted-setting-notification-listener/
  - Kullanıcı yolu: Ayarlar > Uygulamalar > uygulama > ⋮ > "Kısıtlanmış
    ayarlara izin ver". [resmi]
  - https://support.google.com/android/answer/12623953
  - "Kısıtlanmış ayarlara izin ver" ögesi yalnız bildirim veya
    erişilebilirlik erişimi isteyen uygulamalarda çıkar. Çıkmazsa bazı
    kılavuzlar önce kilitli anahtara dokunmayı öneriyor (bu adımı
    doğrulayamadım). [topluluk, tek kaynak]
  - https://droidwin.com/allow-restricted-settings-missing-from-sideloaded-apps-in-android-13-fix/
  - Tasker kullanıcısı: her güncellemeden sonra tekrar gerekiyor (LineageOS
    21). APK ile dağıtımda güncelleme başına sorun çıkabilir. [tek kaynak]
  - https://www.reddit.com/r/tasker/comments/1bk7432/
- Android 14 [resmi]
  - PendingIntent gönderen uygulama arka-plan açılış izni vermeli (Bölüm 5).
  - https://developer.android.com/about/versions/14/behavior-changes-14
- Android 15 [resmi] [AOSP]
  - OTP kırpma: güvenilmeyen dinleyici, OTP algılanan bildirimin içeriğini
    okuyamaz. Eşlik eden cihaz (companion) uygulamaları muaf.
  - https://developer.android.com/about/versions/15/behavior-changes-all
  - Kısıtlanmış ayarlar listesi genişledi (haber, CDD alıntısı):
    erişilebilirlik, bildirim dinleyici, cihaz yöneticisi, diğer
    uygulamaların üstünde gösterme, kullanım erişimi, arama/SMS rolleri.
  - https://9to5google.com/2024/09/12/android-15-sideloaded-apps-restrictions/
  - Başlangıç sürümü kaynaklarda çelişkili: XDA Android 13 diyor, 9to5Google
    Android 15'te "genişletildi" diyor. [çıkarım] Android 13+ için kısıt var
    varsay.
  - Sistem kilit mekanizması (ECM): Android 15 öncesi haberde izinli liste
    ve güvenilir yükleyicilerden söz ediliyor. Yayınlanmış davranış
    [doğrulanamadı].
  - https://www.androidpolice.com/android-15-enhanced-confirmation-mode-sideloading/
- Android 15: OTP kırpma ayrıntısı (kodda) [AOSP]
  - Güvenilir sayılma: `RECEIVE_SENSITIVE_NOTIFICATIONS` izni (role |
    signature; üçüncü taraf alamaz), platform imzası, aynı adlı appop
    "izinli" veya eşlik eden cihaz (CDM) ilişkisi.
  - Kırpılmış bildirim: başlık = uygulamanın adı; metin = "Sensitive
    notification content hidden" (cihaz diline çevrilir); alt metin, metin
    satırları, büyük ikon silinir; eylem başlıkları BOŞ; MessagingStyle
    yerine adı boş tek sahte mesaj; akıllı yanıt/eylem yok.
  - `getActiveNotifications()` ve her güncelleme de kırpılmış döner.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/res/res/values/strings.xml
  - `Ranking.hasSensitiveContent()` gizli sistem API'si; biz çağıramayız.
    Kırpmayı DİL BAĞIMSIZ anlamak için yapıya bak: tüm eylem başlıkları boş,
    mesaj gönderen adı boş, başlık uygulama adına eşit. [çıkarım]
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/service/notification/NotificationListenerService.java
- Android 15: neyin "hassas" sayıldığı [topluluk] (birden çok kaynak)
  - Sayı dizisi içeren metin (2 adımlı doğrulama kodu).
  - Telefon numarası, e-posta, banka bilgisi içeren bildirimler.
  - Sohbet ADI telefon numarası olunca düz metin bile hassas sayılabiliyor.
  - Hassas işaretlenen sohbetin sonraki mesajları "okundu" işaretlenene
    kadar da gizli kalabiliyor.
  - https://www.autoresponder.ai/post/android-15-allow-sensitive-notifications
  - https://www.reddit.com/r/tasker/comments/1fzkyhs/
  - https://www.reddit.com/r/tasker/comments/1p32nyq/
  - [çıkarım] Türkiye'de kayıtsız numarayla yazan müşteri çok. Bu
    TekPanel'in "mesaj gizlendi" durumunu sık görmesi demek.
- Android 15: çözüm yolları [topluluk]
  - (a) Ayarlar > Bildirimler > "Geliştirilmiş bildirimler" / "Önerilen
    eylemler ve yanıtlar" kapatılır. Ad cihaza göre değişir. Bazı cihazlarda
    işe yarıyor; bir kullanıcı 2025'te kapatmasına rağmen sorun sürdüğünü
    yazdı.
  - (b) ADB ile: `appops set <paket> RECEIVE_SENSITIVE_NOTIFICATIONS allow`
    ve cihazı yeniden başlat. Kullanıcı cihaza kablosuz hata ayıklama kurmak
    zorunda; esnaf için zor.
  - KDE Connect aynı izni manifestte istemeyi denedi; izin yalnız imzalı/rol
    sahibi uygulamalara verildiği için istek kapatıldı.
  - https://invent.kde.org/network/kdeconnect-android/-/merge_requests/492
  - [çıkarım] Çalışma zamanında istenemez. Uygulama içinde "(a)" yolunu
    kısa, görselli anlat; (b)'yi "ileri kullanıcı" sayfasına koy.
- Android 16 [resmi] [AOSP]
  - Canlı Güncellemeler (Live Updates) / `ProgressStyle`: devam eden,
    kullanıcı başlatmalı işler (yolculuk, teslimat, navigasyon). Resmi belge
    sohbet mesajlarını açıkça uygunsuz kullanım sayıyor. Bu bildirimler
    `FLAG_ONGOING_EVENT` taşır; mesaj filtremiz zaten onları elemeli.
  - https://developer.android.com/develop/ui/views/notifications/live-update
  - https://developer.android.com/about/versions/16/features
  - Sınıflandırma/bundle ve özetleme: kodda var, bayrak arkasında; beta
    dizgilerinde "konuşma bildirimlerini özetle" geçiyor. Yayınlanmış
    cihazlarda dinleyiciye ne gittiği: [doğrulanamadı]. Kodda dinleyiciye
    özetleme alanı bulunamadı.
  - https://9to5google.com/2025/03/13/android-16-notification-summaries/
- Android 17 [resmi]
  - Bildirim özel görünümleri için bellek denetimi (hedef 17+) ve Live
    Update için anlamsal renk API'si geldi. Dinleyiciyi doğrudan etkileyen
    madde bulunamadı. Bildirim aktarımı için yeni zorunluluk yok.
  - https://developer.android.com/about/versions/17/summary
  - https://developer.android.com/about/versions/17/features

## 8. Ergonomi ve animasyon
- Android'in kendi süreleri [AOSP]
  - Satır yüksekliği/yer değişimi 360 ms, köşe 200 ms, görünme/kaybolma 464
    ms, kaydırıp atma 200 ms, yanıt kutusu kapanışı 150 ms.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/notification/stack/StackStateAnimator.java
- Material 3 Expressive yay (spring) jetonları [AOSP/androidx]
  - Uzamsal (konum, boyut, şekil): sönüm / sertlik
    - hızlı: 0.6 / 800
    - varsayılan: 0.8 / 380
    - yavaş: 0.8 / 200
  - Efekt (opaklık, renk):
    - hızlı: 1.0 / 3800
    - varsayılan: 1.0 / 1600
    - yavaş: 1.0 / 800
  - https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/ExpressiveMotionTokens.kt
- "Standart" (daha sakin) şema [AOSP/androidx]
  - Uzamsal: hızlı 0.9 / 1400, varsayılan 0.9 / 700, yavaş 0.9 / 300.
  - Efekt değerleri Expressive ile aynı.
  - https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/StandardMotionTokens.kt
  - Şemanın kendisi:
    https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/MotionScheme.kt
- Süre (tween) jetonları [AOSP/androidx]
  - Kısa: 50, 100, 150, 200 ms. Orta: 250, 300, 350, 400 ms. Uzun: 450, 500,
    550, 600 ms. Çok uzun: 700 - 1000 ms.
  - https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/MotionTokens.kt
- Oturma süresi ve taşma (benim hesabım) [çıkarım]
  - Birim kütleli yay, hedefin %1'ine girme süresi, 0.5 ms adımlı benzetim.
    Kaynak değil, hesap.
  - Expressive varsayılan uzamsal 0.8/380: ~320 ms, taşma %1.4.
  - Expressive hızlı uzamsal 0.6/800: ~220 ms, taşma %9.3 (zıplar).
  - Expressive yavaş uzamsal 0.8/200: ~450 ms, taşma %1.5.
  - Standart varsayılan uzamsal 0.9/700: ~195 ms, taşma %0.1.
  - Efekt varsayılan 1.0/1600: ~170 ms, taşma yok.
  - Efekt hızlı 1.0/3800: ~110 ms, taşma yok.
  - Mevcut `Brand.kt`: LowBouncy (0.75) + MediumLow (400): ~330 ms, taşma
    %2.8. Yani Expressive varsayılan uzamsala çok yakın.
  - https://developer.android.com/reference/kotlin/androidx/compose/animation/core/Spring
- Sürüm uyarısı [resmi]
  - `MotionScheme` Compose Material3 1.4.0 çizgisinde geldi. Projenin BOM'u
    2024.09.03 ve bu BOM Material3 1.3.0'a bağlı. Yani şema nesnesi yok;
    sayıları kendi `Brand.kt`'ye yazmak yeter.
  - https://developer.android.com/jetpack/androidx/releases/compose-material3
  - https://dl.google.com/android/maven2/androidx/compose/compose-bom/2024.09.03/compose-bom-2024.09.03.pom
- Animasyonları kapatan kullanıcı [AOSP/androidx]
  - Compose, sistemin animasyon ölçeğini
    (`Settings.Global.ANIMATOR_DURATION_SCALE`) okuyup uygular.
    "Animasyonları kaldır" açıkken Compose animasyonları anında biter. Kendi
    zamanlayıcı animasyonu yazma; Compose API'sini kullan.
  - https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-main/compose/ui/ui/src/androidMain/kotlin/androidx/compose/ui/platform/WindowRecomposer.android.kt
- Dokunma hedefi [resmi]
  - En az 48dp x 48dp. Küçük "Okundu" yazısı görsel olarak küçük kalabilir;
    dokunma alanı 48dp olmalı. Compose Material bileşenleri varsayılan
    olarak 48dp ekler.
  - https://developer.android.com/guide/topics/ui/accessibility/apps
  - https://developer.android.com/develop/ui/compose/accessibility/api-defaults
  - SystemUI kendi satır etkileşim alt sınırı 40dp
    (`notification_min_interaction_height`). 48dp daha güvenli.
- Liste kararlılığı [resmi] [AOSP]
  - Her öğeye kararlı `key` ver. Anahtarsız liste öğeyi konumla eşler; yer
    değişince durum ve kaydırma kaybolur. Yer değişimini
    `Modifier.animateItem()` animasyonlar (anahtar şart).
  - https://developer.android.com/develop/ui/compose/lists
  - Dokunuş sıralamayı değiştirmesin. Sıralama anahtarı "son mesaj zamanı"
    olsun. SystemUI de satıra dokununca listeyi yeniden sıralamaz. [çıkarım]
  - Görünen alanın üstüne öğe eklenince kaydırma ofsetini ekleneni kadar
    artır (SystemUI'nin yaptığı).

## Bizim uygulamada kural kural karşılığı
Biçim: her kuralda önce Android, altında "Biz". (I-xx) kurucunun istek
numarası (docs/ISTEKLER.md).

Gruplama
- Android: bildirimler uygulama başına gruplanır; 2+ bildirimde (13+) sistem
  kendisi gruplar.
  - Biz: iki katman: kanal (uygulama) > konuşma (kişi/sohbet). Kanal
    balonuna dokunmak yalnız o kanalı gösterir (I-25). Konuşma tek kapsayıcı
    kartta durur (I-28, I-29).
- Android: özet ve otomatik özet satırları mesaj değildir.
  - Biz: `FLAG_GROUP_SUMMARY` ve `FLAG_AUTOGROUP_SUMMARY` taşıyanı mesaj
    olarak kaydetme. Sadece sayım/başlık için kullan.
- Android: konuşmalar üst bölümde; önemli konuşma en üstte.
  - Biz: sıralama "seçili/çok mesaj alan kanal, en yeni mesajlı konuşma
    üstte" (I-21). Dokunulan öğe yerinden oynamaz (I-22).
- Android: grup saati = çocukların en yeni geçmiş zamanı; sayı = kesilmemiş
  çocuk sayısı.
  - Biz: kart saati = son mesajın zamanı; sayaç = okunmamış mesaj sayısı;
    100 ve üstü "99+".

Daraltılmış ve genişletilmiş
- Android: daraltılmış = ad + tek satır metin, sonu "...".
  - Biz: ad + tek satır mesaj; tüm kartlar sabit asgari yükseklikte (eşit
    boy, I-31). Paket adı hiç yazmaz (I-24, I-45).
- Android: genişletilmiş = tüm mesajlar (en çok 25) + uygulama adı, 358dp
  üst sınırı.
  - Biz: tam metin, KESME YOK (I-33). "Bildirim" etiketi yok.
- Android: ok hapı genişletir; içinde sayı (daraltılmışken, 1'den büyükse).
  - Biz: ok dokunma alanı 48dp'den küçük olmasın; sayı aynı kuralla.
- Android: gövde dokunuşu uygulamayı açar, ok genişletir.
  - Biz: ÇAKIŞMA. Kurucu gövde dokunuşunun genişletmesini istiyor (I-31).
    Öneri: gövde = genişlet/daralt, genişlemiş kartta görünür "Uygulamada
    aç" eylemi = contentIntent. Detay "Kurucu isteği ile çakışmalar" bölümünde.
- Android: grup kapalıyken çocuklar tıklanamaz. Grup açıkken çocukların
  arası uygulamayı açmaz.
  - Biz: kapsayıcıya tek dokunuş: altlar açılır, ikinci: mesajlar tam uzar,
    üçüncü: toplanır (I-32). Satırlar arası boşluk dokunuşu yutulur, hiçbir
    şey yapmaz.
- Android: ilk gruplanmamış genişletilebilir bildirim kendiliğinden
  genişler.
  - Biz: kendiliğinden genişleme YOK (eşit boy, I-31).
- Android: yükseklik değişimi 360 ms, FAST_OUT_SLOW_IN; liste yeniden
  sıralanmaz; alttaki satır açılırken konum düzeltilir.
  - Biz: yükseklik için Expressive varsayılan uzamsal yay (0.8/380), içerik
    opaklığı için efekt yayı (1.0/1600). Genişleyen kartın altı ekran
    dışında kalıyorsa kaydır.
- Android: yeni satır görünen alanın üstüne eklenince kaydırma ofseti satır
  yüksekliği kadar artar.
  - Biz: sabit `key` + kaydırma sabitleme. "Dokununca en üste kayma" (I-30)
    bu iki kuralla kapanır.

Mesaj bildirimi görünümü
- Android: konuşma avatarı solda 48dp, 20dp uygulama rozeti.
  - Biz: 48dp daire avatar solda; sağ alt köşede telefondaki gerçek uygulama
    simgesi (I-26, I-35). Avatar yoksa baş harfli renkli daire.
- Android: konuşma olmayan bildirimde büyük ikon sağda 48dp.
  - Biz: beğeni, yorum, canlı yayın daveti gibi kartlarda sağda 48dp küçük
    görsel (profil/video kapağı).
- Android: üst satır "ad • uygulama • zaman"; daraltılmışta uygulama adı
  yok.
  - Biz: aynı. Zaman göreli: "şimdi", "5 dk", "2 sa", sonra gün.
- Android: grup sohbeti = son iki göndericinin yüz yığını + gönderen adı
  satırları.
  - Biz: aynı; grup adı boşsa `Ranking` kısayol etiketi, o da yoksa gönderen
    adı.

Eylemler
- Android: en çok 3 eylem; başlıklar uygulamanın.
  - Biz: bildirimin eylemlerini BAŞLIĞIYLA göster (I-36, I-38). Sabit liste
    uydurma. 3'ten fazlaysa "daha fazla".
- Android: Yanıtla = satır içi RemoteInput.
  - Biz: canlı serbest-metin RemoteInput varsa genişlemiş kartta satır içi
    yanıt alanı; yoksa düğme "Uygulamada yanıtla" olur.
- Android: SystemUI'nin gönderim kalıbı (Bölüm 4).
  - Biz: `sendReply`'ı o kalıba çek (tüm girdiler, kaynak, foreground
    bayrağı, ActivityOptions).
- Android: gönderirken çark; uygulama bildirimi güncelleyene kadar sürer.
  - Biz: çark göster; 8 sn içinde güncelleme gelmezse "Gönderildiğinden emin
    olamadım, uygulamada kontrol et" de. (8 sn benim seçimim.)
- Android: `isAuthenticationRequired` eylemi kilitliyken önce kilit açtırır.
  - Biz: cihaz kilitliyse bu eylemleri gönderme; "Önce telefonun kilidini
    aç" de.
- Android: sistemde "okundu" yok; MARK_AS_READ uygulamanın eylemi ve karşı
  tarafa görünebilir.
  - Biz: "Okundu" = YEREL (I-40, I-41). Gövde dokunuşu okundu yapmaz.
    Kaynakta da okundu yapmak ayrı, açık uyarılı ayar.
- Android: dinleyici iptali `deleteIntent`'i tetikler; ongoing iptal
  edilemez.
  - Biz: "Okundu/Temizle" varsayılan olarak bildirim çubuğundaki asıl
    bildirime DOKUNMAZ. İstenirse ayarla "bildirim çubuğundan da kaldır".
    Medya, arama, navigasyon, devam eden bildirimlere asla dokunma.
- Android: sessize alma kanal ayarıdır; dinleyici değiştiremez.
  - Biz: konuşma bazlı yerel susturma (1 sa, 8 sa, her zaman). Kaynak
    uygulamanın ayarına dokunmaz.

Dokununca git
- Android: tıklama = contentIntent gönder; `FLAG_AUTO_CANCEL` varsa
  bildirimi kaldır.
  - Biz: "Uygulamada aç" = contentIntent. Ardından `FLAG_AUTO_CANCEL` ise
    `cancelNotification(key)` (resmi öneri); TekPanel kaydını silme.
- Android 14+: gönderen uygulama ActivityOptions ile izin verir.
  - Biz: tek bir `PendingIntentSender`: API 36'da `ALLOW_IF_VISIBLE`,
    34-35'te `ALLOWED`, altında seçenek yok.
- Android: otomatik özet ana ekranı açar.
  - Biz: her zaman çocuğun (konuşmanın) kendi intent'i.
- Android: PendingIntent yaşar; bizim süreç ölünce nesne gider.
  - Biz: bellekte LRU; bağlanınca `getActiveNotifications()` ile yenile;
    `CanceledException`'da başlatma intent'ine düş. Diske yazma.

Medya
- Android: büyük resim 416 x 284dp'ye sığar; önce yer tutucu.
  - Biz: kartta resim için yer tutucu (kayma yok), en çok 284dp yükseklik,
    dokununca tam ekran görüntüleyici (I-34).
- Android: URI izni bildirimle birlikte kalkar.
  - Biz: bildirim gelir gelmez, arka plan iş parçacığında kopyala.
    Başarısızsa "Fotoğraf alınamadı" yaz, mesajı yine göster.
- Android: fotoğraf mesajı yalnız `image/*`.
  - Biz: video/ses için etiket ("Video", "Ses"). Yalnız SON mesajın ekini
    işle.

Sürüm kuralları
- Android 15: OTP/hassas içerik gizlenir.
  - Biz: yapısal olarak algıla; kartta "Android bu mesajı gizledi" ve kısa
    yardım. Aynı gizli metni tekrar tekrar kaydetme.
- Android 13+: sideload'da kısıtlanmış ayar.
  - Biz: Play kurulumunda sorun yok. APK dağıtımında erişim yoksa kartta
    kısa yönlendirme ve görsel rehber (I-48, I-49). Zorunlu açılış adımı yok
    (I-46).
- Android 12+: kullanıcı dinleyici filtresi.
  - Biz: "Mesaj gelmiyor mu?" yardımı: uygulama bildirimi açık mı, bildirim
    erişimi açık mı.
- Android 13+: `POST_NOTIFICATIONS`.
  - Biz: boş durumda "Bu uygulamanın bildirimleri kapalı olabilir".
- Android 16: Canlı Güncelleme / devam eden.
  - Biz: filtrele; mesaj sayma.

Hareket ve dokunma
- Android: animasyon ölçeği sistemden.
  - Biz: yalnız Compose animasyon API'si; özel zamanlayıcı yok.
- Android: dokunma hedefi 48dp.
  - Biz: küçük "Okundu" yazısı dahil her hedef 48dp (I-41).

## Kurucu isteği ile çakışmalar
Kurucu "birebir Android" dedi, ama bazı istekleri Android'den farklı. Karar
kurucunun. Önerim her maddede yazılı.
- Gövde dokunuşu
  - Android: gövde = uygulamayı aç; ok = genişlet.
  - Kurucu: gövde = aynı kutuda uzasın, tekrar dokununca kısalsın (I-31).
  - Öneri: gövde = genişlet/daralt. Genişlemiş kartta görünür "Uygulamada
    aç" eylemi contentIntent'i gönderir. Ok da genişletir. İki Android
    işlevi de korunur, yalnız tetikleyici yer değiştirir.
  - Seçenek B: Android gibi yap (gövde = aç, ok = genişlet). I-31 ile
    çelişir; önermiyorum.
- Avatar yeri
  - Android: konuşmada sol, konuşma olmayan bildirimde sağ.
  - Kurucu Instagram için "sağ tarafta profil fotoğrafı" dedi.
  - Öneri: Android'in kuralı: kişi sohbetinde sol avatar, beğeni/yayın gibi
    konuşma olmayan kartta sağ küçük görsel.
- "Okundu"
  - Android'de sistem düzeyi okundu yok; kaynakta okundu karşı tarafa mavi
    tik gönderebilir (WhatsApp örneği).
  - Öneri: "Okundu" yerel. Kaynakta okundu ayrı ve uyarılı ayar.
- Kendiliğinden genişleme
  - Android ilk bildirimi genişletir. Kurucu eşit boy istiyor.
  - Öneri: kendiliğinden genişleme yok.
- Metin kesme ve yükseklik sınırı
  - Android daraltılmışta kesiyor, genişletilmişte 358dp'de kesiyor.
  - Kurucu mesajın tamamı görünsün istiyor (I-33).
  - Öneri: genişletilmişte kesme ve yükseklik sınırı yok.
- Temizleme
  - "Ekranı tertemiz yap" TekPanel kaydını temizler (I-42), Android bildirim
    çubuğunu değil.
  - Öneri: varsayılan yalnız kendi ekranı; bildirim çubuğu ayrı ayar.

## Denenen ve çalışmayan yollar
- github.com ve api.github.com
  - Bu oturumun GitHub vekili başka depolar için 403 döndürüyor ("sessions
    are bound to their configured repositories").
  - GitHub issue/PR araması yapılamadı. Aşmaya çalışmadım (kural).
  - Yerine: KDE GitLab'ı (invent.kde.org), Stack Exchange API'si, Reddit
    arşivi (arctic-shift), AOSP kaynak sitesi.
- AOSP Gerrit sorguları
  - "autogroup", "redact" ve benzeri aramalar boş döndü.
  - Gruplama eşiğinin 4'ten 2'ye düşürülme gerekçesi (commit mesajı)
    bulunamadı. Yalnız etiketler arası fark kanıtlandı.
- Medium (Polidea: "her mesajlaşma bildirimine yanıt")
  - 403. web.archive.org kopyası bağlantı sıfırladı. Okunamadı.
- Android Authority makaleleri
  - Cloudflare engeli (403). Aynı konuyu 9to5Google ve Android Police
    haberlerinden aldım.
- reddit.com ve old.reddit.com
  - 403 / yönlendirme. pullpush.io 429 verdi. arctic-shift çalıştı ama alt
    reddit adı zorunlu; yalnız birkaç alt reddit tarandı.
- XDA forum araması
  - 403 (makale sayfası açıldı, forum araması açılmadı).
- WhatsApp Yardım Merkezi "click to chat"
  - Sayfa tarayıcı betikleriyle çiziliyor; metin okunamadı.
- Google belgeleri
  - `EnhancedConfirmationManager` referansı ve source.android.com
    "restricted-settings" sayfası 404. ECM servis kaynağı iki konumda
    bulunamadı.
- Arama aracı
  - Yalnız özet döndürüyor, ABD'ye bağlı. Topluluk kaynaklarını doğrudan API
    ile çektim.
- Playwright / tarayıcı
  - Gerekmedi. Hiçbir ekran görüntüsü indirilmedi, silinecek dosya yok.
- Cihaz / emülatör
  - Yok. Bu yüzden gerçek uygulamaların bildirim yapısı gözlenmedi.

## Doğrulanamayan veya bulunamayanlar
- WhatsApp, Instagram, TikTok, X, YouTube bildirimlerinin gerçek yapısı:
  stil, grup anahtarı, eylem başlıkları, anlam etiketleri, RemoteInput var
  mı, contentIntent türü.
- Kurucunun TikTok "ilgileniyorum / ilgilenmiyorum" eylemlerinin gerçek
  PendingIntent türü ve canlı yayına derin bağlantı.
- Kapalı grubun gövdesine dokunmanın gerçek cihazda ne yaptığı.
- Android 16 zorla gruplama, sınıflandırma ve 2025 şablon tasarımının
  yayınlanmış cihazlarda açık olup olmadığı.
- Android 16 bildirim özetlemesinin dinleyiciye giden veriyi değiştirip
  değiştirmediği (kodda dinleyici alanı bulunamadı).
- Kullanıcının dinleyici filtresini ayarlarda nerede değiştireceği (cihaza
  göre değişir).
- "Geliştirilmiş bildirimler" kapatmanın hassas içerik gizlemeyi her cihazda
  kaldırıp kaldırmadığı (kaynaklar çelişkili).
- Bizim `send()` çağrısında Android 12 trampolin kuralının işleyip
  işlemediği.
- Reddit'teki 2026 gönderisi ("gotchas") moderatörce yapay zekâ şüphesiyle
  işaretlendi. Teknik maddeler başka kaynaklarla doğrulanabildiği yerde
  işaretlendi, diğerleri tek kaynak.
- Sağdaki ok hapında grup sayısı (main dalı) yayınlanmış sürümde yok.

## Cihaz bulununca doğrulama planı
- Gerçek yapıyı oku: `adb shell dumpsys notification --noredact` (kaynakta
  `--noredact` seçeneği var). Her uygulama için: stil, grup, eylem
  başlıkları, anlam etiketi, RemoteInput, contentIntent türü not edilsin.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
- Uçtan uca test için sistem komutu:
  - `cmd notification allow_listener <bileşen>` dinleyiciyi arayüz olmadan
    açar.
  - `cmd notification post -S messaging --conversation <ad> --message
    <kim>:<metin> ... <etiket> <metin>` sahte sohbet atar.
  - `-S bigpicture --picture <iconspec>` resimli bildirim atar.
  - `cmd notification list`, `get <anahtar>`, `snooze` var.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationShellCmd.java
  - Sınırı: shell bildirimi gerçek uygulamanın RemoteInput'unu taşımaz.
    Yanıt/eylem sınaması için ayrı bir test uygulaması gerekir. [çıkarım]
- Hassas gizleme testi: kendi test uygulamasından "123456" içeren ve adı
  telefon numarası olan konuşma gönder; kırpmanın yapısını kaydet.
  Sınıflandırmayı sistem asistanı yapıyor; Android 15+ gerçek cihaz gerekir,
  emülatörde çıkmayabilir. [çıkarım]

## TekPanel için çıkarımlar
Yap
- PendingIntent gönderimini tek sınıfta topla (`PendingIntentSender`).
  - ActivityOptions ile arka-plan-açılış izni ver: API 36'da
    `ALLOW_IF_VISIBLE`, 34-35'te `ALLOWED`.
  - Neden: Android 14+ hedefte aksi halde açılış sessizce başarısız
    olabilir. Bizim hedef SDK 36 ve `IntentLauncher` çıplak `send()`
    kullanıyor.
  - https://developer.android.com/guide/components/activities/background-starts
- `sendReply`'ı SystemUI kalıbına getir.
  - Eylemin tüm `RemoteInput`'larına sonuç ekle, `setResultsSource`
    (SOURCE_FREE_FORM_INPUT) yaz, `FLAG_RECEIVER_FOREGROUND` ekle,
    ActivityOptions ver, `isAuthenticationRequired`ta kilitliyken dur.
  - Neden: SystemUI böyle gönderiyor; uygulamalar bu biçime göre yazılmış.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/packages/SystemUI/src/com/android/systemui/statusbar/policy/RemoteInputViewController.kt
- `WearableExtender` eylemlerini de oku (yedek).
  - Neden: bazı uygulamalar yanıtı yalnız orada sunuyor (2017 kaynakları).
    Eski olduğu için etkisini gerçek cihazda ölç.
  - https://www.reddit.com/r/androiddev/comments/6hxf5z/notificationlistenerservice_reply_in/
- Yanıt düğmesini yalnız canlı RemoteInput varsa göster.
  - Neden: "Yanıtla" eylemi bazı uygulamada yalnız uygulamayı açıyor,
    bazılarında RemoteInput sessizce reddediliyor.
  - https://mail.kde.org/pipermail/kdeconnect/2017-May/001971.html
- "Okundu" yerel kalsın; kaynakta okundu ayrı ve uyarılı olsun.
  - Neden: WhatsApp'ta "okundu" eylemi karşı tarafa mavi tik gönderebilir.
    Pazar araştırmamız (docs/arastirma/01-rakipler-google-play.md) "görüldü
    atmadan oku" talebini öne çıkarıyor.
  - https://ubergizmo.com/2018/07/whatsapp-mark-as-read-button-notifications
- Süreç yeniden başlayınca canlı eylemleri `getActiveNotifications()` ile
  yeniden doldur (bağlanma olayında). `CanceledException`da başlatma
  intent'ine düş.
  - Neden: PendingIntent diske yazılamaz, ama bildirim yaşıyorsa taze
    nesneyi sistemden alabiliriz.
  - https://developer.android.com/reference/android/service/notification/NotificationListenerService
- Hassas gizlemeyi yapısal algıla ve kullanıcıya göster.
  - Eylem başlıkları boş, gönderen adı boş, başlık uygulama adı ise
    "kırpılmış" say. Kartta "Android bu mesajı gizledi" yaz ve kısa yardım
    ver (geliştirilmiş bildirimleri kapat). Gizli metni kaydetme/tekrar
    kaydetme.
  - Neden: Türkiye'de kayıtsız numarayla yazan müşteri çok; telefon numaralı
    sohbet adı gizlemeyi tetikleyebiliyor.
  - https://www.autoresponder.ai/post/android-15-allow-sensitive-notifications
- Fotoğrafı bildirim gelir gelmez, binder iş parçacığı dışında kopyala.
  - Neden: sistem içerik URI iznini bildirim kalkınca geri alıyor.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationManagerService.java
- `hasBigPicture`'ı `containsKey` yerine değerin boş olmadığına göre
  hesapla; yalnız son mesajın ekini işle.
  - Neden: `containsKey` boş değerde de true döner; büyük ikon profil
    resmidir.
  - https://www.reddit.com/r/androiddev/comments/1x9wetf/notificationlistenerservice_gotchas_from_building/
- Listede kararlı `key` + `animateItem()` + kaydırma sabitleme.
  - Neden: "dokununca en üste kayma" (I-30) hatası konum eşleşmesinden veya
    yeniden sıralamadan çıkıyor olabilir [çıkarım]; Android bunu böyle
    çözüyor.
  - https://developer.android.com/develop/ui/compose/lists
- Hareket jetonlarını `Brand.kt`'ye yaz: uzamsal 0.8/380 (genişleme, yer
  değişimi), efekt 1.0/1600 (opaklık), hızlı uzamsal 0.6/800 (basma geri
  bildirimi, küçük öğe). BOM'u yükseltmeden sayıları kopyala.
  - Neden: `MotionScheme` Material3 1.3.0'da yok. Mevcut LowBouncy +
    MediumLow zaten çok yakın (~330 ms).
  - https://developer.android.com/jetpack/androidx/releases/compose-material3
- `Ranking`'i kullan: `isConversation()` ve konuşma kısayol etiketi.
  - Neden: Android'in kendi "konuşma" kararı ve grup adı yedeği.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/service/notification/NotificationListenerService.java
- Ongoing, medya, arama, navigasyon ve Canlı Güncelleme bildirimlerini
  filtrele; asla iptal etme.
  - Neden: iptal oturumu bozabilir; sohbet değiller.
  - https://www.reddit.com/r/androiddev/comments/1v8xbmw/notificationlistenerservice_trap_classifier_was/
- Uçtan uca testi `cmd notification` ile yaz; gerçek uygulama yapısını
  `dumpsys notification --noredact` ile topla.
  - https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/notification/NotificationShellCmd.java

Yapma
- Özet ve otomatik özet bildirimlerini mesaj diye kaydetme.
  - Neden: çift sayım; otomatik özetin hedefi ana ekran.
- PendingIntent'i diske yazma, içindeki Intent'i çıkarıp başlatma.
  - Neden: güvenlik hatası verir; token saklanamaz.
  - https://stackoverflow.com/questions/40586609/how-can-i-save-pendingintent-to-sqlite-database
- Gövde dokunuşunu "okundu" yapma (I-40).
  - Neden: Android'de de dokunmak okundu değil; okundu karşı tarafa
    görünebilir.
- Android'e "birebir" derken her sayıyı kopyalama.
  - Neden: 358dp üst sınırı, kendiliğinden genişleme, kesme kurucunun "tam
    mesaj, eşit boy" isteğiyle çelişiyor.
- Bayraklı Android 16 özelliklerine (zorla gruplama, sınıflandırma, 2025
  şablonu) bağımlı davranış yazma.
  - Neden: yayınlanmış cihazlarda açık mı belirsiz.
- Kullanıcıya ADB'yi zorunlu adım yapma.
  - Neden: esnaf için zor; "Geliştirilmiş bildirimler"i kapatma yolu önce
    gelsin, ADB "ileri kullanıcı" sayfasında kalsın.
- Otomatik yanıt veya otomatik eylem gönderme.
  - Neden: PendingIntent göndermek o uygulamanın kimliğiyle iş yaptırmak;
    yalnız kullanıcı dokunuşuyla.
- Eylem ve yanıt metnini günlüğe yazma.
  - Neden: gizlilik sözleşmesi (MIMARI): teşhis kaydı içerik tutmaz.

Kurucudan karar beklenenler
- Gövde dokunuşu: genişlet mi (öneri), aç mı?
- Avatar yeri: Android kuralı (sol/sağ ayrımı) mı, hep sol mu?
- Bildirim çubuğundan da kaldırma: varsayılan kapalı mı (öneri)?
- Kaynakta "okundu" yapma seçeneği olsun mu (varsayılan kapalı)?
