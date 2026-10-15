# Notivo – Bildirim Geçmişi (Android)

Tüm uygulamaların bildirimlerini tek kutuda toplayan, tamamen cihaz içi çalışan Android uygulaması.
Kaynak: `app/`, APK: `dist/notivo-release.apk` (derlenince).

## Özellikler
- Bildirim erişimiyle (NotificationListenerService) tüm bildirimleri yerel Room veritabanına toplar
- **Uygulamaya göre gruplama (varsayılan):** WhatsApp, Gmail gibi her uygulama kendi başlığı altında toplanır (bildirim/okunmamış sayısı, son 3 bildirim, "N daha göster", başlıktan topluca okundu); istersen Zamana göre veya Konuşmalar görünümüne geç, seçim hatırlanır
- Birleşik gelen kutusu: arama, filtre (Tümü/Okunmamış/Yıldızlı/Mesajlar), uygulamaya göre filtre
- Kaydırarak sil, uzun basınca: yıldızla, okundu, **ertele (15 dk/1 sa/12 sa)**, uygulamayı hariç tut
- Dokununca orijinal bildirimi/uygulamayı açar
- Kurallar: anahtar kelime + uygulama bazlı **Engelle** / **Sessize al**
- Uygulama listesi, istatistikler (14 gün, en gürültülü uygulamalar)
- **Konuşma görünümü** (gönderene göre gruplama) + gün başlıkları (Bugün/Dün/…); konuşmadan **uygulama içi yanıt** (bildirim canlıysa ve uygulama destekliyorsa)
- **Silinen mesaj işareti** ("Bu mesaj silindi" bildirimi gelince son mesajı işaretler; sadece bildirimde görünen metin, sezgiseldir)
- Ertelemeler **yeniden başlatmada kaybolmaz**; isteğe bağlı günlük özet bildirimi; JSON yedekle/geri yükle
- Türkçe harf duyarlı arama/kural eşleme (İ/I/ı)
- Kopya bildirim engelleme, süregelen/gizli bildirimleri atlama, saklama süresi, CSV dışa aktarma
- Biyometrik/ekran kilidi, Material You + koyu tema, uygulama içi gizlilik politikası
- **İnternet izni yok**, reklam/analiz yok, yedekleme kapalı (Play veri güvenliği formu için kolay)

## Telefona yükleme (USB)
1. Telefonda Ayarlar → Geliştirici seçenekleri → USB hata ayıklama açık
2. `adb install -r dist/notivo-release.apk`
3. Uygulamayı aç → "Bildirim erişimini aç" → Notivo'ı etkinleştir
   (Android 13+ "kısıtlı ayar": Ayarlar → Uygulamalar → Notivo → ⋮ → Kısıtlı ayarlara izin ver)

## Derleme
`ANDROID_HOME=<sdk> gradle assembleRelease` (JDK 17+, compileSdk 35). Release, repodaki geliştirme
anahtarıyla (`notivo-release.jks`) imzalanır. **Google Play için kendi anahtarını üret**, bu anahtarı kullanma.

## Play Store notları
- Bildirim erişimi hassas izindir: Play Console'da izin beyanı ve gizlilik politikası URL'si gerekir
  (metin `ui/Screens.kt` → `PRIVACY`). Play'in kısıtlı izin politikasına göre ana işlev bildirim yönetimi olduğundan uygundur, ancak inceleme sonucu garanti değildir.
- Play için `.aab` (`gradle bundleRelease`) ve kendi upload key'in gerekir.

## Test durumu (1.3.0)
- **Derleme:** `assembleRelease` (R8 küçültmeli) ve `lintRelease` tamam; lint 0 hata, 22 bulgu (hepsi uyarı/bilgi) (çoğu kütüphane sürümü bildirimi). APK mevcut `notivo-release.jks` anahtarıyla imzalı.
- **JVM testleri (22, hepsi geçiyor):** `gradle testDebugUnitTest`. Robolectric ile bir **simülasyondur, gerçek cihaz veya emülatör değildir**:
  - `LogicTest`: kural eşleme, silinen-mesaj metni, gün kovaları, Türkçe İ/I/ı normalizasyonu, uygulamaya göre gruplama ve "son 3" kuralı
  - `DaoTest`: Room sorguları (silinen işareti, kopya penceresi, saklama süresi, sayaçlar, kural/ertele kayıtları)
  - `ListenerTest`: gerçek `NotiListener` ile yakalama, kopya engeli, ENGELLE/SESSİZ kuralı, hariç tutma, kalıcı/özet bildirimleri atlama, silinen mesaj işareti
  - `FeaturesTest`: yeniden gösterme, hatırlatıcı + alarm, widget metni, geri yükleme kanalı
  - `VmTest`: yedek/geri yükleme gidiş-dönüş, CSV kaçışı, yanıt/açma yedekleri
  - `UiSmokeTest`: onboarding ve gelen kutusu ekranlarının açılması, arama, sekmeler, uygulama grubu başlıkları ve "N daha göster"
- **Gerçek cihaz:** Henüz denenmedi. Ortamda emülatör/AVD/KVM/USB cihaz yoktu.
- Mağaza metni ve marka notları: `PLAY_LISTING.md`.


## 1.3.0 yenilikleri
Tarih filtreleri (Bugünkü/Dünkü/7/30 gün), sıralama yönü, metni kopyala, sesli oku, bildirim çubuğunda yeniden göster, kendi hatırlatıcın, ana ekran widget'ı. Rakip özellik listesi ve durumları: `docs/FEATURE_CHECKLIST.md`. **Sesli okuma ve widget gerçek cihazda denenmedi.**
