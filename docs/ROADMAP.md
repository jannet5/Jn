# TekPanel — Yol Haritası (Sıfırdan → Google Play)

Durum lejantı: ✅ bitti ve doğrulandı · 🟡 kod var, gerçek cihazda doğrulama bekliyor ·
⛔ bu ortamda yapılamaz, kullanıcı eylemi gerekiyor.

## Faz 0 — Proje iskeleti
✅ Yeni özel repo, yeni Gradle/AGP/Kotlin/Compose projesi, `com.tekpanel.app`, minSdk 26,
targetSdk 34 (sonradan 36’ya yükseltildi, bkz. Faz 9). `./` kökünde gerçek bir Android projesi (eski kod/worktree yok).

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
✅ 35/35 test yeşil: 27 saf JVM birim testi + 8 Robolectric testi (gerçek
`Notification`/`StatusBarNotification` nesneleri + gerçek bellek-içi Room ile uçtan uca
CAP-04..07 doğrulaması). `assembleDebug` ve `assembleRelease` başarılı, APK'lar üretildi ve
SHA-256'ları alındı (bkz. RELEASE.md).
✅ **Gerçek KVM emülatöründe doğrulandı (bu sandbox'ta değil, GitHub Actions'ta).** Bu
sandbox'ta KVM yok (kontrol edildi: `/dev/kvm` yok, `vmx`/`svm` CPU bayrağı yok), ama
GitHub-hosted runner'larda var: `.github/workflows/android-ci.yml` her push'ta gerçek,
hızlandırılmış bir Android 11 emülatöründe `NotificationListenerRealDeviceTest`'i çalıştırıyor
— bildirim erişimi gerçekten veriliyor, servis gerçek sisteme gerçekten bağlanıyor, gerçek
OS-üretimi bir `StatusBarNotification` doğru ayrıştırılıyor. Son çalıştırma:
https://github.com/jannet5/Jn/actions/runs/36496578477
✅ **WhatsApp/Instagram/Telegram bildirim şekli, bağımsız açık kaynak projelerle
çapraz doğrulandı** (bkz. DECISIONS.md §8): `MessageNormalizer`'ın öncelik sırası üç ayrı
kaynakla örtüşüyor; `androidx.core:core:1.13.1`'in `EXTRA_MESSAGES` ayrıştırmasının bozuk
veriye karşı zaten sertleştirilmiş olduğu bytecode incelemesiyle doğrulandı.
🟡 Hâlâ eksik olan tek şey — dürüstçe: bu, gerçek WhatsApp/Instagram hesabıyla gerçek bir
telefonda ANLIK doğrulama değil, dokümante edilmiş/bağımsız doğrulanmış davranışa karşı kod
incelemesi. Gerçek hesap + gerçek telefon gerektiriyor (aşağıya bakın).

## Faz 9 — Yayın hazırlığı
✅ Uygulama ikonu (özgün, yer tutucu kanal logoları — bkz. DECISIONS.md §5), imzalama
talimatları, mağaza metinleri (EN/TR taslağı), gizlilik politikası taslağı, data safety
eşlemesi — hepsi RELEASE.md'de.
✅ **targetSdk 36.** 31 Ağustos 2026'dan beri Play, Android 16'yı (API 36) hedeflemeyen yeni
uygulamaları ve güncellemeleri reddediyor; uygulama 34'ü hedefliyordu. compileSdk/targetSdk 36,
AGP 8.11.1'e yükseltildi; testler, lintVitalRelease ve CI emülatör testleri geçti.

## Faz 9b — Kurulabilir dağıtım
✅ 2.5 MB'lık küçültülmüş `preview` derlemesi (debug anahtarıyla imzalı, ayrı paket adı),
GitHub raw ve jsDelivr üzerinden indirilebilir, iki adres de sunucu tarafında SHA-256 ile
doğrulandı; CI her push'ta bunu gerçek emülatöre kurup açıyor (`preview-smoke` işi). Ayrıntı:
`dist/README.md`. Drive'a yalnız teslim notu konabildi (APK değil — bağlayıcı ikili dosyayı
araç çağrısının içinde base64 metin olarak istiyor).
🟡 **Açık kabul maddesi: kullanıcının telefonunda indirme/kurulum/kullanım.** 21 MB'lık
`.apk` ve `.zip` bağlantıları telefonda başarısız oldu, hata noktası bilinmiyor. 2.5 MB'lık
preview henüz telefonda denenmedi.

## Faz 10 — Google Play'de gerçek yayın
⛔ Kullanıcı eylemi gerekiyor (Play Console hesabı, 25$ ödeme, kimlik doğrulama, içerik
derecelendirme anketi, AAB yükleme, ülke/fiyat seçimi). Ayrıntılı, adım adım checklist:
RELEASE.md → "Yalnızca kullanıcının yapabileceği adımlar".

---

## Sıradaki somut adımlar (öncelik sırasıyla)

1. **Gerçek hesapla gerçek cihaz testi.** `dist/TekPanel-preview.apk`'yı telefona kur
   (bağlantılar `dist/README.md`'de; olmazsa bilgisayardan `adb install -r`), bildirim erişimini aç, WhatsApp Business + Instagram'dan
   gerçek mesaj gönder, TekPanel'e düşüp düşmediğini doğrula. Kod zaten bağımsız açık kaynak
   projelerle çapraz doğrulanmış davranışa göre yazıldı (DECISIONS.md §8), yani beklenti
   "çalışır ama küçük ayarlar gerekebilir" — büyük bir yeniden yazım değil. Sorun bulunursa
   `MessageNormalizer`/`MessageFilterEngine` içinde hedefli düzeltme yapılır (kodun test
   edilebilir yapısı sayesinde tek dosya değişikliği yeterli olur).
2. **Marka logoları.** DECISIONS.md §5'teki hukuki notu okuyup her marka için karar ver.
3. **İmzalama anahtarı.** RELEASE.md → "İmzalama" bölümünü izleyip gerçek bir release
   keystore'u oluştur (bu, kullanıcının kendi güvenli ortamında yapması gereken bir adımdır —
   anahtar bu depoya asla commit edilmemelidir).
4. **Play Console.** RELEASE.md checklist'ini takip et.
