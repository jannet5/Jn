# Sende: sistemin mühendislik anlatımı

Bu belge, projeyi hiç görmemiş bir mühendise ya da yapay zekaya verilmek üzere yazıldı. Sırayla:
ne çözüyor, nasıl çalışıyor, hangi kararlar neden verildi, nasıl derlenir/test edilir/yayınlanır.
Kısa harita için `docs/MIMARI.md`, hata günlüğü için `docs/SORUNLAR_COZUMLER.md`.

## 1. Problem ve sınırlar

- Küçük işletme sahibi müşterilerle WhatsApp, Instagram, Telegram, SMS gibi çok yerden yazışıyor.
  Sende, bu uygulamaların BİLDİRİMLERİNİ okuyup tek, sade bir ekranda toplar.
- Sınırlar (pazarlık konusu değil):
  - Veri telefondan çıkmaz. Sideload APK'da INTERNET izni yok; Play sürümünde yalnız Billing'in getirdiği
    ağ izni var ve uygulama kodu mesaj verisini ağa göndermez.
  - Kaynak uygulamalara giriş yapılmaz, API kullanılmaz; yalnız `NotificationListenerService`.
  - Kullanıcının kendi bildirimine dokunulmaz (silme/erteleme kodu yok; CI bunu denetler).
  - Hesap, sunucu, analiz, reklam yok.

## 2. Yığın

- Kotlin 2.0.21, Jetpack Compose (Material 3), Room 2.6.1 + KSP, DataStore, Koin 3.5, kotlinx.serialization.
- AGP 8.11.1, Gradle 8.14.3, JDK 17 hedefi, minSdk 26, targetSdk/compileSdk 36.
- Test: JUnit4, Truth, Robolectric 4 (sdk 34), Roborazzi, Compose UI test, androidTest + emülatör.
- İki ürün çeşidi (flavor): `sideload` ve `play`; üç yapı türü: `debug`, `release` (R8), `preview`
  (R8'li ama elle verilecek biçimde imzalı, paket adı `.preview`).

## 3. Veri akışı (tek yön)

1. Android, seçili uygulamanın bildirimini `SendeNotificationListenerService.onNotificationPosted`'a verir.
2. `NotificationExtractor` çerçeve nesnesini düz veriye çevirir (`RawNotificationPayload`): başlık, metin,
   bigText, textLines, `MessagingStyle` satırları (gönderen, metin, zaman, resim var mı, "bu satırı telefon
   sahibi mi yazdı"), kategori, bayraklar. Resimler tembel: `NotificationMedia` yalnız kayda değer mesaj
   için çözülür.
3. `MessageNormalizer`: en yapısaldan en düze doğru seçer (MessagingStyle > textLines > bigText/text),
   sahibin kendi satırlarını eler, başlıktan sohbeti çıkarır.
4. `MessageFilterEngine`: (a) kendi paketimiz/sistem, (b) kanal seçili mi, (c) boş mu, (d) grup özeti mi,
   (e) süregelen/hizmet/reklam kategorisi mi, (f) küçük sezgisel filtre (marka adıyla gelen etkileşim
   bildirimi, tam cümle "canlı yayında" uyarısı). Sert kurallar metne bakmaz.
5. `CaptureCoordinator` (tek coroutine'li sınıf):
   - Zamanlı satırlar (MessagingStyle): parmak izi = paket + sohbet + gönderen + metin + tam zaman damgası.
   - Zamansız satırlar: `UntimedLineTracker` bildirim anahtarı başına önceki satır listesini tutar; yalnız
     eklenen satırlar yeni sayılır; bildirim kapanınca (`onNotificationRemoved`) unutulur. Yeniden
     başlatmadan sonraki ilk görüşte, çok satırlı bildirimde son 6 saatte kayıtlı aynı satırlar atlanır.
     Parmak izi = ... + tam gönderilme zamanı.
   - Medya: profil resmi ve resim `MediaStore` içine içerik adresli (SHA-256) kaydedilir; hata mesajı
     engellemez. `files/media`, yedekten hariç. 10 dakikadan genç dosya temizlenmez.
   - Kayıt: Room'da `fingerprint` benzersiz indeks; ikinci savunma hattı.
   - Canlı eylemler: yanıt (`RemoteInput`) ve uygulama düğmeleri yalnız bellekte (`LiveActionRegistry`).
6. Room akışı → `InboxRepository` (konuşmalara gruplar, son 3000 okunmamış) → `MainViewModel` → `MainContent`.
7. Dokunuş → ViewModel → depo → Room; ekran akıştan kendiliğinden güncellenir.

Konuşma anahtarı = paket + normalize başlık (küçük harf, NFKC, boşluk sadeleştirilmiş): WhatsApp ile
WhatsApp Business ayrı konuşmadır.

## 4. Arayüz kuralları (kurucunun istekleri, kodda zorlananlar)

- Tek ekran. Menü bir ok işaretinin arkasında; kanal seçici ayrı sayfa; paket adı hiçbir yerde görünmez.
- Kart üç kademeli: kapalı → mesajlar (kısa) → tam; tek mesajlıda kapalı → tam. Kart metnine dokunmak
  da açar; basılı tutmak kopyalar. Dokunma kaydırmayı oynatmaz (`ScrollStabilityTest`).
- "Okundu" ayrı bir yazı düğmesi; mesaja dokunmak okundu yapmaz. Çok mesajlı kapalı kartta düğme
  "Hepsi okundu" der.
- "Ekranı tertemiz yap": onay sorar, yalnız ekranda görüneni gizler, geri alınabilir.
- Geri tuşu: önce menü, sonra açık kartlar, en sonda arka plana (uygulamadan çıkarmaz).
- Her dokunma hedefi ≥ 48 dp (`ErgonomicsTest`), %200 yazı boyutunda da.
- Metin kesilmez (tam kademede); müşteri metni her yazı sisteminde kendi yönünde çizilir
  (`TextDirection.Content`).

## 5. Marka sistemi

- Kaynak gerçek: `docs/marka/konsept-N-belirtecler.json` (renk açık+koyu, yazı tipleri, köşe, hareket).
- `tools/apply_brand.py N` → `BrandTokens.kt`, gömülü yazı tipleri (Latin alt kümesi), uyarlanabilir +
  tek renkli ikon, pencere rengi. `SendeTheme` sistemin açık/koyu ayarını izler.
- `tools/make_store_assets.py N` → Play ve reklam görselleri (markasız kanal adlı ekran görüntüleriyle).

## 6. Para (ödeme) modeli

- Varsayılan: bağış. Hiçbir özellik kilitlenmez. `-Pmonetization=donate|trial_silent|trial_warn`.
- `TrialPolicy` saf işlev: Açık / Bilgi / Paywall. Play'de `PlayPurchaseGateway` (ürün `sende_support`,
  baz planlar 1/3/6/12 ay, bahşişler anında tüketilir). Sideload'da `Dist` boş.

## 7. Derleme, imza, yayın

- `./gradlew :app:assembleSideloadPreview` elden verilecek APK; `:app:bundlePlayRelease -PversionCode=N` AAB.
- Üç imza anahtarı (herkese açık test, özel dağıtım, Play yükleme): `docs/RELEASE.md` §3.
- CI (`.github/workflows/android-ci.yml`): birim/ekran/ergonomi testleri, yasak kod denetimi, INTERNET ve
  izin listesi denetimi, Play R8 derlemesi, 4 sürümlü emülatör matrisi, duman testi; hepsi geçerse
  `son-surum` yayını yenilenir (ClamAV taraması dahil). Doküman değişikliği CI'ı başlatmaz.

## 8. Test sistemi

`docs/TEST_SISTEMI.md`: 9 katman ve tek komut `bash tools/kapilar.sh`. Kural: başarısız test kendini
açıklar; bekleme için uyku değil sinyal beklenir; uçtan uca testler boş gelen kutusuyla başlar.

## 9. Bu tasarımın bilinçli kararları ve nedenleri

- Neden MessagingStyle'ın tüm satırları? Uygulamalar sohbet başına tek bildirim kullanır; yalnız son
  satırı okumak, art arda gelen mesajları sessizce kaybeder (ilk büyük hata, S-22 öncesi).
- Neden bildirim anahtarı tekrar ölçütü değil? Aynı anahtar farklı mesajları taşır.
- Neden `requestRebind` + "Dinleme duraklamış" kartı? Üretici pil yöneticileri bağlantıyı koparır;
  rakiplerin 1 numaralı şikayeti "sessizce durdu".
- Neden iki ürün çeşidi? Billing kütüphanesi ağ izni getirir; "veri çıkmaz" vaadi sideload'da kelimesi
  kelimesine doğru kalsın diye.
- Neden Play uygulama içi güncelleme kütüphanesi yok? O da INTERNET getirir.
- Neden paylaşım girişi kaynak paketi (referrer) kullanmaz? Çağıran istediği değeri koyabilir.

## 10. Bilinen sınırlar

`docs/ROADMAP.md` sonu ve `docs/ISTEKLER.md`: gerçek WhatsApp/Instagram ile gerçek telefonda deneme,
üretici pil kısıtları, Android 8'de çapraz uygulama testi, YZ'li ayrı sürüm (henüz yok).
