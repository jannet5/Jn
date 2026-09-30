# NotiBox – Bildirim Toplayıcı (Android)

Tüm uygulamaların bildirimlerini tek kutuda toplayan, tamamen cihaz içi çalışan Android uygulaması.
Kaynak: `app/`, APK: `dist/notibox-release.apk` (derlenince).

## Özellikler
- Bildirim erişimiyle (NotificationListenerService) tüm bildirimleri yerel Room veritabanına toplar
- Birleşik gelen kutusu: arama, filtre (Tümü/Okunmamış/Yıldızlı/Mesajlar), uygulamaya göre filtre
- Kaydırarak sil, uzun basınca: yıldızla, okundu, **ertele (15 dk/1 sa/12 sa)**, uygulamayı hariç tut
- Dokununca orijinal bildirimi/uygulamayı açar
- Kurallar: anahtar kelime + uygulama bazlı **Engelle** / **Sessize al**
- Uygulama listesi, istatistikler (14 gün, en gürültülü uygulamalar)
- Kopya bildirim engelleme, süregelen/gizli bildirimleri atlama, saklama süresi, CSV dışa aktarma
- Biyometrik/ekran kilidi, Material You + koyu tema, uygulama içi gizlilik politikası
- **İnternet izni yok**, reklam/analiz yok, yedekleme kapalı (Play veri güvenliği formu için kolay)

## Telefona yükleme (USB)
1. Telefonda Ayarlar → Geliştirici seçenekleri → USB hata ayıklama açık
2. `adb install -r dist/notibox-release.apk`
3. Uygulamayı aç → "Bildirim erişimini aç" → NotiBox'ı etkinleştir
   (Android 13+ "kısıtlı ayar": Ayarlar → Uygulamalar → NotiBox → ⋮ → Kısıtlı ayarlara izin ver)

## Derleme
`ANDROID_HOME=<sdk> gradle assembleRelease` (JDK 17+, compileSdk 35). Release, repodaki geliştirme
anahtarıyla (`notibox-release.jks`) imzalanır. **Google Play için kendi anahtarını üret**, bu anahtarı kullanma.

## Play Store notları
- Bildirim erişimi hassas izindir: Play Console'da izin beyanı ve gizlilik politikası URL'si gerekir
  (metin `ui/Screens.kt` → `PRIVACY`). Play'in kısıtlı izin politikasına göre ana işlev bildirim yönetimi olduğundan uygundur, ancak inceleme sonucu garanti değildir.
- Play için `.aab` (`gradle bundleRelease`) ve kendi upload key'in gerekir.
