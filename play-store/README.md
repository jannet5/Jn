# Google Play'de yayınlama — adım adım

Bu klasörde Play'e yüklemek için gereken her şey hazır. Sadece Google'ın
**sizin kimliğinizle** yapılması gereken adımlar kaldı.

| Dosya | Ne için |
|---|---|
| `app-release.aab` (paket zip'inde) | Play'e yüklenecek uygulama paketi |
| `upload-keystore.jks` + `upload.properties` (paket zip'inde, GİZLİ) | Yükleme anahtarı. **Yedekleyin, kimseyle paylaşmayın.** |
| `icon-512.png` | Uygulama simgesi (512×512) |
| `feature-graphic-1024x500.png` | Öne çıkan görsel |
| `screenshots/*.png` | Telefon ekran görüntüleri (720×1440, Android 16'dan) |
| `listing-tr.md`, `listing-en.md` | Mağaza adı, kısa ve tam açıklama |
| `privacy-policy.md` | Gizlilik politikası (URL'si hazır) |
| `play-console-cevaplari.md` | Play Console formlarının cevapları |

## 1. Geliştirici hesabı (bir kere)
1. https://play.google.com/console → Google hesabınızla girin.
2. Hesap türü: **Kişisel** (veya şirketiniz varsa Kuruluş). Tek seferlik
   kayıt ücreti 25 $.
3. Kimlik doğrulamasını tamamlayın (kimlik belgesi + telefon). Birkaç gün sürebilir.

## 2. Uygulamayı oluşturun
**Uygulama oluştur** → Ad: `WinRemote Monitor` · Varsayılan dil: Türkçe ·
Uygulama · Ücretsiz → beyanları onaylayın.

## 3. İçerik formları
`play-console-cevaplari.md` dosyasındaki cevapları **Uygulama içeriği**
bölümüne girin (gizlilik politikası, reklam, uygulama erişimi, içerik
derecelendirmesi, hedef kitle, veri güvenliği).

## 4. Mağaza kaydı
**Ana mağaza kaydı**: `listing-tr.md` metinleri, `icon-512.png`,
`feature-graphic-1024x500.png` ve `screenshots/` klasöründeki görselleri yükleyin.
İngilizce çeviri için `listing-en.md`.

## 5. Önce kapalı test (yeni kişisel hesaplarda zorunlu)
Google, 2023 sonrası açılan kişisel hesaplarda üretime çıkmadan önce
**en az 12 test kullanıcısıyla en az 14 gün kapalı test** ister
(kuralı Play Console'daki "Üretim erişimi" sayfasında kontrol edin).
1. **Test → Kapalı test → Kanal oluştur** → test kullanıcılarının
   Gmail adreslerini ekleyin.
2. **Yeni sürüm oluştur** → **Play Uygulama İmzalama**'yı kabul edin
   (Google'ın yönettiği anahtar) → `app-release.aab` dosyasını yükleyin.
3. Testçilere katılım bağlantısını gönderin; 14 gün kullanmalarını isteyin.
4. Süre dolunca **Üretim erişimi için başvur**.

## 6. Üretim
**Üretim → Yeni sürüm** → aynı AAB'yi (veya yenisini) yükleyin → Ülkeler:
Türkiye (ve istediğiniz diğerleri) → **İncelemeye gönder**. İnceleme genelde
birkaç gün sürer.

## Güncelleme yayınlarken
1. `android-app/app/build.gradle.kts` içinde `versionCode`'u 1 artırın.
2. `./gradlew bundleRelease -PsigningProps=/yol/upload.properties`
3. Play Console'da yeni sürüm olarak yükleyin.

## Windows programı için önemli not
Müşteriler Windows programını
`https://github.com/jannet5/Jn/raw/HEAD/downloads/WinRemoteMonitor-Kurulum.exe`
adresinden indirir (uygulamadaki "Bağlantıyı gönder" düğmesi bunu gönderir).
Program kod imzalı olmadığı için Windows ilk çalıştırmada **"Windows
bilgisayarınızı korudu"** uyarısı gösterir (Ek bilgi → Yine de çalıştır).
Çok sayıda müşteri için bu uyarıyı kaldırmak üzere bir kod imzalama
sertifikası alın (ör. Microsoft **Azure Trusted Signing**, aylık ~10 $; veya
bir OV/EV kod imzalama sertifikası) ve `WinRemoteMonitor-Kurulum.exe`'yi
imzalayın. Bu, sizin adınıza ödeme ve kimlik doğrulaması gerektirir.
