# Play Store yayın gereksinimleri (okunma tarihi 2026-10-02)

Her madde okunan sayfaya bağlı. "OKUNAMADI" yazanlar doğrulanmadı.

## Hedef API
- 31 Ağustos 2026'dan itibaren yeni uygulama ve güncellemeler Android 16 (API 36) hedeflemeli; Wear/Auto/TV istisnaları var. Mevcut uygulamalar yeni kullanıcılar için API 35 hedeflemeli. 1 Kasım 2026'ya uzatma talep edilebilir. Kaynak (arama özeti, resmi sayfa sonucu): https://developer.android.com/google/play/requirements/target-sdk . Resmi sayfanın kendisi WebFetch ile okunmadı, arama özeti okundu; Play Console'da son kontrolü yap.
- Karar: yeni nokta için targetSdk 36.

## AAB, imzalama
- "Starting August 2021, new apps are required to publish with the Android App Bundle on Google Play." https://support.google.com/googleplay/android-developer/answer/9006925
- Play App Signing: yeni uygulamalar otomatik kaydolur; sen "upload key" (RSA 2048+ keystore) üretip AAB'yi onunla imzalarsın, kullanıcıya giden APK'yı Google imzalar. Upload ve app signing key farklı olmalı. https://support.google.com/googleplay/android-developer/answer/9842756 . Keystore yedeğini sakla.

## SYSTEM_ALERT_WINDOW (overlay)
- OKUNAMADI: Google Play politikasında overlay'e özel bir madde/form bulunamadı. Okunan: "Permissions and APIs that Access Sensitive Information" sayfası SYSTEM_ALERT_WINDOW'u yalnızca "özel izinler için kullanıcıyı sistem ayarları sayfasına yönlendir" diye anıyor; ayrı beyan formu detayı yok. Genel kural: hassas izinler çekirdek işlevsellik için gerekli ve mağaza listesinde tanıtılan işleve bağlı olmalı. https://support.google.com/googleplay/android-developer/answer/16558241
- Öneri: mağaza açıklamasında yüzen balonun çekirdek özellik olduğunu açıkça yaz; izin ekranına git yönlendirmesi kullan. Google'ın resmi özel overlay politikasını Play Console Policy status'ta tekrar doğrula.

## Foreground service specialUse
- Android 14+: manifestte tür + `FOREGROUND_SERVICE_SPECIAL_USE` izni + `<property android:name="android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE" android:value="...">` serbest metin açıklama. https://developer.android.com/develop/background-work/services/fgs/service-types
- Play Console: App content sayfasında tür başına beyan: işlev açıklaması, ertelenme/kesinti etkisi, her özelliği gösteren video linki; specialUse için "tüm türler incelemeye tabi". https://support.google.com/googleplay/android-developer/answer/13392821
- Hazırlanacak: kısa video (balonun çalışma süresince açık kalması).

## POST_NOTIFICATIONS
- Android 13 (API 33)+ çalışma zamanı izni; FGS bildirimi de dahil. Reddedilirse FGS çalışır ama bildirim çekmecesinde görünmez (Görev Yöneticisi'nde görünür). Bağlamda iste. https://developer.android.com/develop/ui/views/notifications/notification-permission

## Data safety, gizlilik politikası
- Veri toplamayan uygulama da formu doldurur ve gizlilik politikası linki zorunludur. "Toplama" = cihaz dışına iletilen veri. https://support.google.com/googleplay/android-developer/answer/10787469
- Tamamen yerel/internetsiz nokta için: "veri toplanmıyor, paylaşılmıyor" işaretlenir (kullandığın SDK'lar cihaz dışına veri göndermiyorsa; reklam/analitik SDK ekleme).
- Gizlilik politikası: basit, yayınlanmış bir URL (yerel veri, hesap yok) gerekir. Politika metni yazılmadı.

## Mağaza görselleri ve metin
- İkon: 32-bit PNG, 512x512, en çok 1024 KB. Feature graphic: JPEG/24-bit PNG, 1024x500, alfa yok. Ekran görüntüsü: JPEG/24-bit PNG, alfa yok, kenar 320-3840 px, cihaz başına en çok 8, toplam en az 2; önerilen en az 4 yüksek çözünürlüklü (portre 9:16, en az 1080x1920). Kısa açıklama 80 karakter. Video: YouTube. https://support.google.com/googleplay/android-developer/answer/9866151
- Başlık (30) ve uzun açıklama (4000) sınırı bu sayfa özetinde GELMEDİ, doğrulanmadı.

## İçerik derecelendirme
- Tüm uygulamalar Play Console'da derecelendirme anketini doldurur; yanlış beyan kaldırmaya yol açabilir. https://support.google.com/googleplay/android-developer/answer/9859655

## Ücretli uygulama / IAP
- Ücretli indirme ve uygulama içi satın alma Google Play Billing kullanır (muafiyetler: fiziksel mal vb.). https://support.google.com/googleplay/android-developer/answer/9858738
- Satış için Play Console'da ödeme profili (payments profile / merchant) gerekir: yasal ad, geçerli fiziksel adres (PO box yok), iletişim, destek e-postası, ekstrede görünen ad; kurulum ülkesi sonradan değişmez. https://support.google.com/googleplay/android-developer/answer/7161426 (arama özeti). Türkiye için vergi/ülke ayrıntısı OKUNAMADI.

## Ek bulgu: yeni kişisel hesap testi
- 13 Kasım 2023 sonrası açılan kişisel hesaplar: en az 12 test kullanıcısı, kesintisiz en az 14 gün kapalı test, sonra "Apply for production". Kurumsal hesap muaf. Genelde 7 günde inceleme. https://support.google.com/googleplay/android-developer/answer/14151465
