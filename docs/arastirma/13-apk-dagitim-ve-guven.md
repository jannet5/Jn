# APK dağıtımı ve güven: telefona nasıl ulaşır, kullanıcı neden korkmaz

Araştırma kolu: dagitim-guven (kendim, 22 Ekim 2026). Ayrıca bkz. `09-google-play-yayin-rehberi-turkiye.md`
§8.3 ve §12 (Play Protect, geliştirici doğrulaması). Etiketler: [resmi], [çoklu] birden çok bağımsız
kaynak, [tek kaynak], [ölçtüm] bu oturumda ölçtüm, [doğrulanamadı].

## İndirme neden takılır, ne ölçtüm

- "Yüzde 100'de takılma" için kaynak bulamadım. [doğrulanamadı] Yaygın nedenler (hepsi çıkarım):
  depolama doluluğu, Chrome'un indirme sonrası kendi taraması, indirme yöneticisi, operatör/ISS.
- Chrome APK'yı "dosya zararlı olabilir" diye uyarabilir; Play Protect açıksa uyarı çıkmaması
  üzerinde çalışılıyor, yayılma durumu belirsiz. [çoklu] https://www.androidheadlines.com/2024/07/android-fewer-apk-download-warnings-chrome.html
- Ölçtüğüm: GitHub sürüm bağlantısı `son-surum/Sende.apk` doğru dosya türüyle
  (`application/vnd.android.package-archive`), 3,1 MB, sunucu tarafında 0,5 sn'de iniyor ve SHA-256 sürüm
  notundakiyle aynı. [ölçtüm, 22 Ekim 2026] Telefon tarafı denenmedi.
- Dosya adı ve içerik türü doğru olunca Chrome ve indirme yöneticisi için sorun beklenmiyor; yine de
  takılırsa: farklı tarayıcı, QR (kamera uygulaması farklı tarayıcı açabilir), veya Wi-Fi ↔ mobil veri.

## Kurulum engelleri (kullanıcının göreceği sırayla)

1. **"Bu kaynaktan kuruluma izin ver."** Tarayıcıya/dosya yöneticisine bir kereliğine izin. [resmi, Android]
2. **Play Protect uyarısı.** Bildirim dinleyiciler "yüksek riskli izin" sayılır; bazı pazarlarda internetten
   inen bu tür uygulamaların kurulumu engellenir. Türkiye'nin kapsamda olup olmadığı [doğrulanamadı].
   "Ayrıntılar → Yine de yükle" çıkmayabilir; çıkmazsa geçici olarak Play Protect taramasını kapatmak
   tek yol olur ve bu gerçek bir güvenlik ödünü. [çoklu]
   https://developers.google.com/android/play-protect/warning-dev-guidance
3. **Kısıtlanmış ayar (Android 13+).** Bildirim erişimi anahtarı gri olur. Adımlar: önce anahtara dokun
   (iletişim kutusu çıksın) → Ayarlar > Uygulamalar > Tüm uygulamalar > Sende (uygulamanın kendi
   ayar menüsünden DEĞİL) → sağ üst üç nokta > "Kısıtlanmış ayarlara izin ver" → PIN/parmak izi → anahtarı
   aç. Bazı telefonlarda (Samsung, Infinix) üç nokta menüsü hiç çıkmıyor; çözüm APK'yı dosya
   yöneticisiyle yeniden kurmak. [çoklu]
   https://cleanbrowsing.org/support/mobile/disable-restricted-settings-android
   Sende'nin "Nasıl yapılır?" rehberi bu sırayı anlatıyor.

## Dağıtım seçenekleri (hesap, hız, güven, güncelleme)

- **Google Play (iç/kapalı/açık test, sonra üretim).** En güvenilir, otomatik güncelleme, Play Protect
  uyarısı yok. Hesap ve 12 kişilik 14 günlük kapalı test (kişisel hesapta) gerektirir. Ana yol. [resmi]
- **GitHub sürüm bağlantısı (şimdiki).** Hesapsız, hızlı, ücretsiz, sabit adres; otomatik güncelleme yok
  (kullanıcı yeniden indirir; sürüm kodu artıyor, üstüne kurulur). İmza: şu an herkese açık test anahtarı;
  özel anahtar yolu hazır (`tools/make_release_key.sh`). [ölçtüm]
- **Firebase App Distribution / Obtainium / F-Droid özel depo / Accrescent.** Hepsi ya hesap ya da
  ek uygulama ister; "hesap yok, tek dokunuş" isteğiyle çakışıyor. [çıkarım] Obtainium, kullanıcı
  tarafında GitHub sürümlerini izleyip güncelleyen bir uygulama: ileri kullanıcı için seçenek, esnaf için değil.
- **Telegram/WhatsApp ile dosya.** Hızlı ama güven zincirsiz; mesajlaşma uygulaması APK'yı kendisi de
  engelleyebilir. [çıkarım]
- **Google Drive.** Bu ortamdan ikili dosya yüklenemiyor (bağlayıcı yalnız metin taşıyor); paylaşım
  bağlantısı da indirmeden önce "virüs taraması yapılamadı" ekranı gösterir. [çıkarım]

## Güven artırma (kodda ve belgede yapılanlar)

- İzin listesi çok kısa (yalnız açılışta yeniden bağlan), INTERNET yok; her yayında CI APK'nın izinlerini
  denetler ve ClamAV ile tarar. [ölçtüm]
- Sürüm notunda commit, yapı numarası, imza parmak izi ve SHA-256 yazar; `dist/README.md` doğrulama adımını anlatır.
- Açık kaynak şeffaflığı: kod ve CI herkese açık depoda.
- Özel imza anahtarı: şimdilik KURULU DEĞİL (GitHub gizli kaydı eklenmedi). Anahtar kurulunca sürüm notu
  "özel anahtar" der. Anahtarı değiştirmek, kurulu kopyaların güncellenmesini kırar: ilk gerçek
  kullanıcıdan önce yapılmalı.
- VirusTotal: ücretsiz kotalı API anahtarı gerekir; anahtar olmadığı için yapılmadı. [doğrulanamadı]

## Android geliştirici doğrulaması (özet, ayrıntı 09 §12)

30 Eylül 2026'dan beri Brezilya, Endonezya, Singapur, Tayland'da; Türkiye ilk dalgada yok; dünya geneli
2027. Play'e yayın yapan zaten doğrulanmış sayılıyor. [resmi] https://developer.android.com/developer-verification

## Kuruluma güvenen kullanıcı için akış (ekran metinleri)

1. Bağlantı: "Sende'yi indir (3 MB, hesap gerekmez)".
2. "Android 'bilinmeyen kaynak' diyebilir: bu normal, yalnız bu kurulum için izin ver."
3. "İlk ekranda zorunlu adım yok. Üstteki karta dokun, Sende'yi bul, anahtarı aç."
4. "Anahtar gri mi? 'Nasıl yapılır?' dokun: 3 adım gösterir."
5. "Sende internete bağlanamaz (izin listesinde yok). Verilerin telefonunda kalır."

## Sende için çıkarımlar

- Play ana yol olmalı; GitHub bağlantısı geliştirme/deneme yolu.
- Müşteriye APK verilecekse önce özel imza anahtarı kurulmalı.
- Rehberdeki "Allow restricted settings" adımı, uygulamanın kendi ayar menüsünden DEĞİL, Ayarlar >
  Uygulamalar yolundan anlatılmalı (kaynaklarda bu yüzden menü görünmüyor).
