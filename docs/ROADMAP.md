# TekPanel — Yol Haritası (Sıfırdan → Google Play)

Durum lejantı: ✅ bitti ve doğrulandı · 🟡 kod var, gerçek cihazda doğrulama bekliyor ·
⛔ bu ortamda yapılamaz, kullanıcı eylemi gerekiyor.

## Faz 0 — Proje iskeleti
✅ Yeni özel repo, yeni Gradle/AGP/Kotlin/Compose projesi, `com.tekpanel.app`, minSdk 26,
targetSdk 34. `./` kökünde gerçek bir Android projesi (eski kod/worktree yok).

## Faz 1 — Veri katmanı
✅ Room (`InboxMessageEntity` + unique index'ler), DataStore (`AppPreferences`), kapalı
kanal kataloğu (`SourceAppCatalog`) + kurulu uygulama tespiti (`InstalledAppDetector`,
manifest `<queries>` ile sınırlı, `QUERY_ALL_PACKAGES` yok).

## Faz 2 — Bildirim yakalama motoru (CAP-04..07)
✅ `NotificationExtractor` → `MessageNormalizer` → `MessageFilterEngine` →
`DuplicateGuard`/Room unique index → `CaptureCoordinator`. 26 birim testiyle doğrulandı.
🟡 Gerçek WhatsApp/Instagram/TikTok bildirimlerine karşı gerçek cihazda son ince ayar.

## Faz 3 — Tek ekran arayüz (CAP-09..13)
✅ `InboxScreen`, `MessageCard`, `ChannelFilterRow`, boş/hata/yükleniyor durumları, geri
alınabilir "Ekranı tertemiz yap".

## Faz 4 — Kurulum ve kanal ayarları (CAP-01..03)
✅ Bildirim erişimi isteği ana ekranın boş durumu olarak; kanal ayarları `ModalBottomSheet`,
"Tüm kanalları aç"/"Tümünü kapat".

## Faz 5 — Kaynağa gitme ve paylaşım (CAP-14, CAP-15)
✅ `IntentLauncher` (canlı intent → uygulama açma → hata), `ShareIntakeActivity`
(`Activity.getReferrer()` ile kaynak tespiti).

## Faz 6 — Tanılama, dışa aktarma, silme (CAP-16, CAP-17)
✅ Gizli `DiagnosticsActivity` (7 dokunuşla açılır), JSON dışa aktarma (paylaşım sayfası
üzerinden, dosya yazma/izin gerektirmez), güçlü onaylı "Tüm yerel veriyi sil".

## Faz 7 — Yerelleştirme, erişilebilirlik, yedekleme kuralları
✅ `values` (EN, varsayılan), `values-tr`, `values-fr`, `values-ar` (RTL). 48dp dokunma
hedefleri, `maxLines` clamp yok, `data_extraction_rules.xml` ile mesaj veritabanı
bulut yedeklemesinden hariç tutuldu.

## Faz 8 — Test ve build doğrulama
✅ 34/34 test yeşil: 26 saf JVM birim testi + 8 Robolectric testi (gerçek
`Notification`/`StatusBarNotification` nesneleri + gerçek bellek-içi Room ile uçtan uca
CAP-04..07 doğrulaması). `assembleDebug` ve `assembleRelease` başarılı, APK'lar üretildi ve
SHA-256'ları alındı (bkz. RELEASE.md).
🟡 Gerçek fiziksel cihaz testi henüz yok — bu sandbox'ta KVM yok (kontrol edildi:
`/dev/kvm` yok, `vmx`/`svm` CPU bayrağı yok), bu yüzden hızlandırılmış emulator da
çalıştırılamıyor. Robolectric ile gidilebilecek en yakın nokta yapıldı.

## Faz 9 — Yayın hazırlığı
✅ Uygulama ikonu (özgün, yer tutucu kanal logoları — bkz. DECISIONS.md §5), imzalama
talimatları, mağaza metinleri (EN/TR taslağı), gizlilik politikası taslağı, data safety
eşlemesi — hepsi RELEASE.md'de.

## Faz 10 — Google Play'de gerçek yayın
⛔ Kullanıcı eylemi gerekiyor (Play Console hesabı, 25$ ödeme, kimlik doğrulama, içerik
derecelendirme anketi, AAB yükleme, ülke/fiyat seçimi). Ayrıntılı, adım adım checklist:
RELEASE.md → "Yalnızca kullanıcının yapabileceği adımlar".

---

## Sıradaki somut adımlar (öncelik sırasıyla)

1. **Gerçek cihaz testi.** `app-debug.apk`'yı bir Android telefona `adb install -r
   app-debug.apk` ile kur, bildirim erişimini aç, WhatsApp Business + Instagram'dan gerçek
   mesaj gönder, TekPanel'e düşüp düşmediğini doğrula. Sorun bulunursa
   `MessageNormalizer`/`MessageFilterEngine` içinde hedefli düzeltme yapılır (kodun test
   edilebilir yapısı sayesinde tek dosya değişikliği yeterli olur).
2. **Marka logoları.** DECISIONS.md §5'teki hukuki notu okuyup her marka için karar ver.
3. **İmzalama anahtarı.** RELEASE.md → "İmzalama" bölümünü izleyip gerçek bir release
   keystore'u oluştur (bu, kullanıcının kendi güvenli ortamında yapması gereken bir adımdır —
   anahtar bu depoya asla commit edilmemelidir).
4. **Play Console.** RELEASE.md checklist'ini takip et.
