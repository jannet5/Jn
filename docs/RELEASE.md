# TekPanel — Yayın Belgesi

## 1. Bu oturumun derleme kanıtı

| Artifact | Tarih (UTC) | Boyut | SHA-256 |
|---|---|---|---|
| `TekPanel-debug-20260927.apk` | 2026-09-27 | 21 415 087 bayt | `3ec247332af172dcad1b88ae0ca00d5749de8e8288fe4aefacec17801e5f0115` |
| `TekPanel-release-unsigned-20260927.apk` | 2026-09-27 | 2 552 452 bayt | `0a5fafd249a9281bc1938e20c579962ff1cea18fb5ce12a4a85dc13b503ae0b9` |

Nasıl üretildi (tekrarlanabilir):
```
./gradlew :app:testDebugUnitTest :app:assembleDebug :app:assembleRelease
sha256sum app/build/outputs/apk/debug/app-debug.apk
sha256sum app/build/outputs/apk/release/app-release-unsigned.apk
```
Debug APK doğrudan `adb install -r TekPanel-debug-20260927.apk` ile bir Android telefona
kurulabilir (bildirim erişimi izni ister). Release APK henüz **imzasızdır** (bkz. §5) —
Play Console'a bu haliyle yüklenemez, önce gerçek bir anahtarla imzalanmalıdır.

Testler: **34/34 geçti**. 26'sı saf JVM birim testleri (`MessageNormalizerTest`,
`MessageFilterEngineTest`, `FingerprintTest`, `DuplicateGuardTest`); 8'i Robolectric ile
gerçek `android.app.Notification`/`StatusBarNotification` nesneleri ve gerçek bellek-içi
Room veritabanı kullanan uçtan uca testler (`NotificationExtractorRobolectricTest`,
`CaptureCoordinatorRobolectricTest`) — bu sandbox'ta KVM/donanım sanallaştırma olmadığı
için emulator çalıştırılamıyor (bkz. DECISIONS.md §3), Robolectric bunun yerine gerçek
Android çerçeve sınıflarını JVM üzerinde simüle ederek elden gelen en güçlü kanıtı verir:
gerçek bir WhatsApp bildiriminin şeklini taklit eden bir `Notification` nesnesi uçtan uca
gönderiliyor, Room'a doğru düşüyor, listener yeniden bağlanıp aynı bildirimi tekrar
işlediğinde (CAP-07) mükerrer kayıt oluşmadığı ve kanal kapatılınca yeni kayıt girmediği
doğrulanıyor. `./gradlew test` ile tekrar çalıştırılabilir.

## 2. Şartname → kod eşleme tablosu (CAP-01..17)

| Madde | Karşılayan dosya(lar) |
|---|---|
| CAP-01 Bildirim erişimi | `ui/inbox/EmptyStates.kt` (`NotificationAccessMissingState`), `util/NotificationAccessChecker.kt` |
| CAP-02 Kurulu kanalları keşfetme | `data/catalog/SourceAppCatalog.kt`, `InstalledAppDetector.kt`, manifest `<queries>` |
| CAP-03 Kanal seçimi | `data/prefs/AppPreferences.kt`, `data/repository/ChannelRepository.kt`, `ui/channels/*` |
| CAP-04 Bildirim yakalama | `capture/TekPanelNotificationListenerService.kt`, `NotificationExtractor.kt` |
| CAP-05 Normalizasyon | `capture/MessageNormalizer.kt`, `domain/model/InboxMessage.kt` |
| CAP-06 Filtreleme | `capture/MessageFilterEngine.kt`, `PromotionalHeuristics.kt` |
| CAP-07 Duplicate önleme | `capture/Fingerprint.kt`, `DuplicateGuard.kt`, `data/local/InboxMessageEntity.kt` (unique index) |
| CAP-08 Kalıcı yerel inbox | `data/local/TekPanelDatabase.kt`, `res/xml/data_extraction_rules.xml` |
| CAP-09 Tek ekran | `ui/inbox/InboxScreen.kt` |
| CAP-10 Mesaj kartı | `ui/inbox/MessageCard.kt` |
| CAP-11 Kanal filtreleri | `ui/inbox/ChannelFilterRow.kt` |
| CAP-12 Okundu/kaldırma | `InboxViewModel.markRead`, `MessageCard.kt` |
| CAP-13 Ekranı tertemiz yap | `InboxViewModel.clearActive/undoClear`, `ui/common/ConfirmDialog.kt` |
| CAP-14 Kaynağa gitme | `util/IntentLauncher.kt`, `capture/LiveIntentRegistry.kt` |
| CAP-15 ACTION_SEND yedek | `capture/ShareIntakeActivity.kt` |
| CAP-16 Tanılama | `diagnostics/DiagnosticsRecorder.kt`, `ui/diagnostics/*` |
| CAP-17 Dışa aktar/sil | `export/DataExporter.kt`, `domain/usecase/EraseAllDataUseCase.kt` |

## 3. Gizlilik politikası (taslak — EN, yayından önce hukuki gözden geçirme önerilir)

> **TekPanel Privacy Policy (draft)**
>
> TekPanel reads notifications from messaging apps you explicitly choose, using Android's
> Notification Access permission, which you can revoke at any time in Android Settings.
> TekPanel stores the notification's visible title, message text, sender name, source app,
> and timestamp **only on your device**, in a local database. TekPanel does not have a
> server, does not send any data over the network, and does not use analytics or
> advertising SDKs. Notification content is excluded from Android's automatic cloud backup.
> You can export your local data as a JSON file at any time (you choose where it goes), and
> you can permanently delete all local data from the app's channel settings. Uninstalling
> the app deletes all of its local data. TekPanel does not access message history inside
> the source apps, does not log in to any account, and cannot read messages that never
> produced a visible notification.

**Not:** Play Console, uygulamayı yayınlamadan önce bu politikanın herkese açık bir URL'de
barındırılmasını zorunlu kılar (Play Console → App content → Privacy policy). Bu, kullanıcı
tarafından bir web sayfasına (örn. GitHub Pages, basit bir statik sayfa) konmalıdır.

## 4. Data safety (Play Console formu) eşlemesi

Play Console'un "Data safety" formundaki sorulara verilecek dürüst cevaplar:

- **Veri toplanıyor mu?** Evet — ama cihaz dışına **hiç gönderilmiyor**.
- **Toplanan veri türleri:** Messages (bildirim başlığı/metni/gönderen adı) — yalnızca
  cihazda saklanıyor, sunucuya iletilmiyor.
- **Veri paylaşılıyor mu?** Hayır.
- **Veri şifreli iletiliyor mu?** Uygulanamaz — ağ iletimi yok.
- **Kullanıcı veri silme isteyebilir mi?** Evet, uygulama içinden ("Tüm yerel veriyi sil").
- **Bu veri toplama isteğe bağlı mı?** Kullanıcı hangi kanalların dinleneceğini seçer;
  bildirim erişimi izni olmadan hiçbir veri okunmaz.

## 5. İmzalama (release keystore)

Bu depoya **hiçbir zaman** gerçek bir keystore veya şifre commit edilmemelidir (`.gitignore`
bunu zaten engelliyor: `*.jks`, `*.keystore`, `release/keystore.properties`).

Adımlar (kullanıcının kendi güvenli makinesinde/ortamında):
```
keytool -genkeypair -v -keystore release/tekpanel-release.jks \
  -alias tekpanel -keyalg RSA -keysize 2048 -validity 10000
```
Sonra `release/keystore.properties` dosyasını oluşturun (bu dosya git'e girmez):
```
storeFile=tekpanel-release.jks
storePassword=<gerçek şifre>
keyAlias=tekpanel
keyPassword=<gerçek şifre>
```
`app/build.gradle.kts` bu dosya varsa otomatik olarak `release` build type'ına imzalama
uygular; dosya yoksa `assembleRelease` imzasız üretir (bu oturumdaki gibi). Anahtarı ve
şifreleri kaybetmeyin — Play App Signing kullanılsa bile yükleme anahtarınız (upload key)
kaybolursa Google ile destek süreci gerekir.

AAB üretimi (Play Console `.aab` ister, `.apk` değil):
```
./gradlew :app:bundleRelease
```

## 6. Mağaza listeleme metinleri (taslak)

### English
- **Short description (80 char max):** New customer messages from WhatsApp, Instagram &
  more — in one screen.
- **Full description:**
  TekPanel collects new customer messages from the messaging and social apps you choose —
  WhatsApp, WhatsApp Business, Instagram, TikTok, Messenger, Facebook, Telegram, X, and
  SMS — into one simple screen on your phone.
  
  No more checking five apps to see if a customer wrote to you. TekPanel reads only the
  notifications you already receive, keeps everything on your device, and never logs into
  any of your accounts.
  
  • Choose exactly which apps TekPanel watches
  • See the full message, not a cut-off preview
  • Mark a message done, or open the real app in one tap
  • Export or delete your local data anytime
  • Nothing is ever sent to a server — TekPanel has none

### Türkçe
- **Kısa açıklama (80 karakter):** WhatsApp, Instagram ve daha fazlasından yeni müşteri
  mesajları — tek ekranda.
- **Tam açıklama:**
  TekPanel, seçtiğin mesajlaşma ve sosyal medya uygulamalarından — WhatsApp, WhatsApp
  Business, Instagram, TikTok, Messenger, Facebook, Telegram, X ve SMS — gelen yeni müşteri
  mesajlarını telefonunda tek, sade bir ekranda toplar.

  Müşterin yazdı mı diye beş uygulamayı tek tek açmana gerek yok. TekPanel yalnızca zaten
  aldığın bildirimleri okur, her şeyi cihazında tutar ve hiçbir hesabına giriş yapmaz.

  • TekPanel'in hangi uygulamaları izleyeceğini sen seç
  • Kesilmiş önizleme değil, mesajın tamamını gör
  • Bir dokunuşla okundu işaretle veya gerçek uygulamayı aç
  • Yerel verini istediğin an dışa aktar veya sil
  • Hiçbir şey bir sunucuya gönderilmez — TekPanel'in sunucusu yok

## 7. Yalnızca kullanıcının yapabileceği adımlar (⛔ ajan bunları yapamaz)

Bunların hepsi Google'ın gerçek bir insan kimliği ve ödeme yöntemi istediği adımlardır:

1. [play.google.com/console](https://play.google.com/console) üzerinden geliştirici hesabı
   aç, 25$ tek seferlik kayıt ücretini öde.
2. Geliştirici Dağıtım Sözleşmesi'ni kabul et, kimlik doğrulamasını (D-U-N-S/kimlik belgesi
   vb.) tamamla.
3. Yeni uygulama oluştur, §6'daki metinleri ve bir gizlilik politikası URL'sini gir.
4. İçerik derecelendirme anketini doldur (TekPanel için beklenen sonuç: geniş yaş grubu,
   kullanıcı tarafından üretilen içerik yok).
5. Data safety formunu §4'teki eşlemeye göre doldur.
6. §5'e göre imzalanmış bir `.aab` üret ve yükle.
7. Ülke/fiyatlandırma seç (öneri: ücretsiz, tüm ülkeler veya hedeflenen TR/FR/BAE pazarları).
8. İncelemeye gönder.

Bu adımların her biri tamamlandığında, bu depodaki kod ve belgeler bir sonraki oturumda
yardımcı olmak için hazır: örneğin "Play Console'dan şu hata geldi" denirse hata mesajıyla
birlikte devam edilebilir.
