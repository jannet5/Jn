# Notivo – Bildirim Geçmişi (Android)

Tüm uygulamaların bildirimlerini tek kutuda toplayan, tamamen cihaz içi çalışan Android uygulaması.
Kaynak: `app/`, APK: `dist/notivo-release.apk` (derlenince).

## Özellikler
- Bildirim erişimiyle (NotificationListenerService) tüm bildirimleri yerel Room veritabanına toplar
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

## Test durumu
- JVM birim testleri geçiyor (`gradle testDebugUnitTest`): kural eşleme, silinen-mesaj tespiti, gün kovaları, Türkçe normalizasyon.
- Gerçek cihazda/emülatörde **henüz çalıştırılmadı**.
- Mağaza metni ve marka notları: `PLAY_LISTING.md`.
