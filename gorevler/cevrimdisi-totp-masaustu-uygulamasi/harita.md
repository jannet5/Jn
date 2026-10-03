# Harita — Çevrimdışı TOTP masaüstü uygulaması

Hedef → bağımlılıklar → A/B/C yolları → uygulama → test/build → kalıcı teslim zinciri.
Her düğüm bir sonrakine bağlıdır; bir yol kırılırsa “geri dönüş” sütunundaki düğüme dönülür.

```
[H] Hedef: Windows'ta, internete çıkmadan, anahtarları güvenli kasada saklayan TOTP üretici
  │
  ├─► [B1] TOTP doğruluğu ........ RFC 6238 + pyotp ............ ► [T1] RFC vektörleri + bağımsız HMAC + oathtool
  ├─► [B2] Güvenli depolama ...... keyring → Windows Cred. Mgr. . ► [T2] Wine'da WinVaultKeyring tam döngü
  ├─► [B3] Arayüz ................ customtkinter (koyu, sabit) .. ► [T3] Xvfb + Wine GUI sürücüsü (17 adım)
  ├─► [B4] GitHub uyumu .......... SHA1 / 6 hane / 30 sn ........ ► [T1] + URI/setup-key ayrıştırma testleri
  └─► [B5] Dağıtım ............... PyInstaller --onedir ......... ► [T4] Wine'da .exe derleme + xdotool akışı
                                                                     │
                                         [D] Kalıcı teslim ◄─────────┘  (git dalı push + özel ZIP + SHA-256)
```

## 1. Araştırma bulguları (resmi kaynaklar)

| Konu | Bulgu | Kaynak |
|---|---|---|
| GitHub TOTP parametreleri | Issuer `GitHub`, **SHA1**, **6 hane**, **30 sn**; QR okunamazsa “setup key” = TOTP secret. Kurulumdan sonra kurtarma kodları indirilmeli; 28 günlük “check-up” süresi var. | https://docs.github.com/en/authentication/securing-your-account-with-two-factor-authentication-2fa/configuring-two-factor-authentication |
| TOTP standardı | T = floor(unix / 30), HMAC-SHA1, dinamik kesme; Ek B test vektörleri | https://datatracker.ietf.org/doc/html/rfc6238 |
| pyotp | `TOTP.at/now/verify(valid_window)`, `parse_uri` (otpauth://) | https://github.com/pyauth/pyotp — https://pyauth.github.io/pyotp/ |
| keyring | Windows'ta `WinVaultKeyring` (Windows Credential Manager); kayıtları **listeleyemez** (yalnız get/set/delete) | https://github.com/jaraco/keyring |
| Credential Manager sınırı | `CredentialBlobSize` en fazla **5×512 = 2560 bayt**; generic credential “securely stored” | https://learn.microsoft.com/en-us/windows/win32/api/wincred/ns-wincred-credentiala |
| customtkinter paketleme | Belge `--onedir` önerir, veri dosyaları (.json/.otf) eklenmeli | https://customtkinter.tomschimansky.com/documentation/packaging |
| PyInstaller hook | `pyinstaller-hooks-contrib` 2026.8 içinde `hook-customtkinter.py` → `collect_data_files("customtkinter")`; elle `--add-data` artık gerekmiyor (yerelde dosya okunarak doğrulandı) | https://github.com/pyinstaller/pyinstaller-hooks-contrib |

## 2. Topluluk / kullanıcı deneyimi

| Gözlem | Kaynak |
|---|---|
| Masaüstünde KeePassXC, telefonda Aegis birlikte kullananlar var; yedek kodları birden fazla fiziksel ortamda saklama önerisi | https://www.gog.com/forum/general/authenticator_apps_for_2fa |
| Ente Auth masaüstü istemcisi (açık kaynak, Windows/Mac/Linux) — senkron isteyen için alternatif | https://alternativeto.net/news/2024/4/ente-releases-desktop-version-ente-auth-s-open-source-2fa-authenticator-app |
| KeePassXC TOTP'yi parola veritabanı dosyasında saklar | https://docs.nsiscloud.polsa.gov.pl/en/latest/accountmanagement/Using-KeePassXC-for-Two-Factor-Authentication-on-NSIS-Cloud.html |
| Ortak uyarı: 2FA kodu ile parolanın aynı cihazda olması ikinci faktörün bağımsızlığını zayıflatır | yukarıdaki forum + Reddit özetleri (https://gummysearch.com/tools/best-products/2fa-app/) |

## 3. A / B / C yolları

| Yol | Ne | Artı | Eksi | Karar |
|---|---|---|---|---|
| **A** | Kaynaktaki mimari: Python + customtkinter + pyotp + keyring (Windows Credential Manager), PyInstaller onedir | Kaynakta açıkça istenen ürün; tamamen çevrimdışı; kod küçük ve denetlenebilir | Credential Manager kullanıcı oturumuna bağlı (ayrı ana parola yok); keyring listeleme yapamaz | **SEÇİLDİ** |
| B | Hazır ürün: KeePassXC (TOTP) / Ente Auth / 2FAS | Olgun, yedekleme/senkron, ana parola | Kaynaktaki “kendi uygulamam” isteğini karşılamaz | Belgede alternatif olarak önerildi |
| C | Şifreli yerel dosya (ör. `cryptography` + ana parola) | Ana parola, taşınabilir yedek | Kaynak “düz dosyaya yazma, keyring kullan” diyor; anahtar yönetimi hatası riski | Reddedildi; A kırılırsa (ör. kasa yok) dönülecek yol |

## 4. A yolunun uygulama düğümleri ve kararlar

| # | Karar | Gerekçe | Test bağlantısı |
|---|---|---|---|
| A1 | `keyring.set_password("My2FAApp", hesap_adi, gizli_anahtar)` aynen | Kaynak satır 23 | `test_kasa_ekle_oku_sil_ve_duz_metin_yok` |
| A2 | Hesap **adları** da keyring'de, 900 karakterlik parçalı JSON (`__hesap_listesi_N__`) | keyring listeleyemez; 2560 bayt blob sınırı (UTF-16) | `test_buyuk_hesap_listesi_parcalanir` (80 hesap) |
| A3 | Düz metin / boş kasa arka uçları reddedilir, “Ekle” kapanır | Kaynak satır 10: düz metne asla yazma | `test_guvensiz_backend_reddedilir` |
| A4 | Setup key toleransı (boşluk, tire, küçük harf, `=`) + `otpauth://` URI | GitHub setup key'i gruplu gösterir; URI'den ad önerisi | `test_setup_key_bicim_toleransi`, `test_otpauth_uri_github_bicimi` |
| A5 | Saat her saniye okunur; kod, zaman sayacı değişince (30'un katı) yenilenir; tik saniye başına hizalanır | Kaynak satır 22 | GUI adımı “30 sn sınırında kod otomatik yenilendi” |
| A6 | Pencere 440×560 sabit, koyu tema | Kaynak “örn: 400x300” der; liste + çubuk için büyütüldü, sabitlik korundu | GUI adımı 1 |
| A7 | Kopyala: boşluksuz kod panoya; 30 sn sonra pano hâlâ aynıysa temizlenir | Kaynak satır 18 + güvenlik | GUI adımları “Kopyala”, “Pano otomatik temizleme” |
| A8 | Ağ kütüphanesi yok; NTP de yok (çevrimdışı) | Kaynak satır 5 | `test_tamamen_cevrimdisi_calisir` (soket yasaklı) |
| A9 | PyInstaller `--onedir --windowed --exclude-module PIL` | customtkinter belgesi onedir; PIL gereksiz (32 MB) | Wine'da derleme + exe akışı |

## 5. Kırılma noktaları ve geri dönüşler (gerçekte yaşananlar)

| Kırılma | Çözüm |
|---|---|
| Bulut Python 3.11'de tkinter yok | Sistem Python 3.12 + `python3-tk` ile venv |
| Windows ortamı yok | Wine 9.0 + resmi `python-3.12.10-amd64.exe`; WinVaultKeyring gerçek API üzerinden test edildi |
| Test kasası sınıfı keyring tarafından otomatik keşfedilip zincire girdi | Test sınıfı `priority` → “uygun değil” yapıldı |
| Windows konsolu cp1252 → Türkçe `print` çöktü | `sys.stdout.reconfigure(errors="replace")` |
| `app.resizable()` sorgusu Windows'ta CTk başlık çubuğunu yeniden çizdirip pencereyi gizledi (yalnız test sürücüsünde) | Sorgu doğrudan `wm resizable` ile yapıldı |
| Programatik temizlemede ipucu metinleri kayboldu | Ekle sonrası odak kaldırılıp ipucu yeniden etkinleştirildi |
| Tüm hesaplar silinince boş liste kaydı kasada kalıyordu | Liste boşsa tüm parça kayıtları silinir |

## 6. Doğrulama → teslim

| Kanıt | Sonuç | Dosya |
|---|---|---|
| Birim/kabul testleri, Linux | 28 geçti, 2 Windows'a özel atlandı | `kanit/test-linux.txt` |
| Aynı testler, Windows Python (Wine) + gerçek WinVault | 30/30 | `kanit/test-windows-wine.txt` |
| GUI kullanıcı akışı, Linux | 17/17 | `kanit/gui-linux-cikti.txt`, `kanit/linux-*.png` |
| GUI kullanıcı akışı, Windows Python (Wine) + WinVault | 17/17 | `kanit/gui-wine-windows-cikti.txt`, `kanit/wine-windows-*.png` |
| Bağımsız oathtool çapraz kontrolü (GitHub parametreleri) | 50/50 | `kanit/github-uyum-oathtool.txt` |
| Paketlenmiş `.exe` (Wine) klavye/fare akışı + yeniden açılış | Geçti | `kanit/exe-akis-cikti.txt`, `kanit/exe-*.png` |
| Kalıcı teslim | Dal push + özel ZIP + SHA-256 geri okuma | `calisma-gunlugu.md` |

**Yapılamayan (dürüst sınır):** gerçek Windows 10/11 makinesinde çalıştırma, gerçek DPAPI şifrelemesinin gözlenmesi
(Wine kendi Credential Manager uygulamasını kullanır) ve gerçek bir GitHub hesabında 2FA etkinleştirme. Bunlar
kullanıcının makinesi ve hesabı gerektirir; adımlar `README.md` → “Windows kabul listesi” bölümündedir.
