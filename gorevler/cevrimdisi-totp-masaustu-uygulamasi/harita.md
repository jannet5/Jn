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
| A2 | Hesap **adları** da keyring'de, 900 karakterlik parçalı JSON; 1.0.1'de nesil + SHA-256 işaretçi | keyring listeleyemez; 2560 bayt blob sınırı (UTF-16); atomik geçiş | `test_buyuk_hesap_listesi_parcalanir` (80 hesap) |
| A3 | 1.0.1: izin listesi (Windows'ta yalnız WinVault), her işlemde denetim, “Ekle” + Enter kapanır | Kaynak satır 10: düz metne asla yazma | §6 satır 1a–1c |
| A4 | Setup key toleransı (boşluk, tire, küçük harf, `=`) + `otpauth://` URI | GitHub setup key'i gruplu gösterir; URI'den ad önerisi | `test_setup_key_bicim_toleransi`, `test_otpauth_uri_github_bicimi` |
| A5 | Saat her saniye okunur; kod, zaman sayacı değişince (30'un katı) yenilenir; tik saniye başına hizalanır | Kaynak satır 22 | GUI adımı “30 sn sınırında kod otomatik yenilendi” |
| A6 | Pencere 440×560 sabit, koyu tema | Kaynak “örn: 400x300” der; liste + çubuk için büyütüldü, sabitlik korundu | GUI adımı 1 |
| A7 | Kopyala: boşluksuz kod panoya; 30 sn sonra veya normal kapanışta, pano hâlâ aynıysa temizlenir | Kaynak satır 18 + güvenlik | §6 satır 3 |
| A8 | Ağ kütüphanesi yok; NTP de yok (çevrimdışı) | Kaynak satır 5 | `test_tamamen_cevrimdisi_calisir` (soket yasaklı) |
| A9 | PyInstaller `--onedir --windowed --exclude-module PIL`, hash'li kilitli venv'den | customtkinter belgesi onedir; PIL gereksiz (32 MB); tekrar üretilebilirlik | Wine'da derleme + exe akışı |

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

## 6. Bağımsız inceleme düzeltmeleri (1.0.0 → 1.0.1)

İnceleme 77ae732 üzerinde yapıldı. Her bulgu → düzeltme → onu yakalayan test/kanıt:

| # | Bulgu | Düzeltme | Kanıt |
|---|---|---|---|
| 1a | `backend_guvenli_mi` bilinmeyen arka uçları kabul ediyordu | Platforma göre **izin listesi**; Windows'ta yalnız `WinVaultKeyring`; ChainerBackend'deki her arka uç denetlenir | `test_bilinmeyen_backend_*`, `test_windows_yalniz_winvault_ister`, `test_zincirde_tek_yabanci_*`; mutasyon M1 |
| 1b | Kasa okuma/yazmalarında denetim yoktu | `Kasa._kr()` her işlemde politikayı yeniden denetler; açılışta yaz/oku/sil sağlık testi | `test_politikaya_uymayan_kasada_hicbir_okuma_yazma_yapilmaz` (6 işlem), `test_politika_her_islemde_*`, `test_saglik_testi_*`; M10 |
| 1c | Enter, kapalı Ekle düğmesini aşıyordu | `hesap_ekle` başında `kasa_hazir`/düğme durumu denetimi + `Kasa.ekle` içinde `denetle()` | GUI: “Enter ve doğrudan hesap_ekle kapalı Ekle'yi aşamadı” (arka uç çağrısı = 0) |
| 2a | `PasswordDeleteError` yutulup hesap listeden çıkarılıyordu | `_sil` silme sonrası geri okuyarak doğrular; kayıt duruyorsa hata, liste değişmez; arayüz hata gösterir | `test_silinemeyen_kayit_*`, `test_sessizce_basarisiz_silme_*`; GUI “Silinemeyen kayıt”; M2 |
| 2b | Listede olmayan mevcut kaydın üzerine yazılabiliyordu | Ekle öncesi kasada aynı ad varsa reddedilir | `test_indeks_disi_mevcut_kayit_uzerine_yazilmaz` (+ Windows'ta gerçek kasada); M3 |
| 2c | Liste yazma hatasında geri alma yoktu | Nesil + SHA-256 işaretçi (atomik geçiş); ekle → anahtar geri alınır; sil → anahtar geri yüklenir; geri alma da başarısızsa elle temizleme mesajı | `test_ekle_indeks_yazma_hatasinda_geri_alinir` (işaretçi + parça), `test_sil_indeks_yazma_*`, `test_yarim_kalan_*`, `test_bozuk_indeks_fail_closed`; M4, M5, M11 |
| 3 | `destroy` pano zamanlayıcısını iptal edip kodu panoda bırakıyordu | Kapanışta pano **hâlâ kendi koduysa** temizlenir; değilse dokunulmaz | GUI: gerçek zamanlı 31,5 sn ölçüm (28,5. sn'de henüz silinmemiş), başka metne dokunmama, **kontrol deneyi** (temizlik kapalıyken kod Windows'ta kapanıştan sonra kalıyor) + erken kapanış; exe'de WM_CLOSE sonrası pano boş |
| 4a | URI issuer/etiket eşitliği, yinelenen parametre, kontrol karakteri | Hepsi reddedilir; kontrol karakteri `strip()`'ten önce aranır (sondaki `%0A` testte yakalandı) | `test_supheli_uri_reddedilir` (8 durum), `test_issuer_etiket_*`; M6, M7, M8 |
| 4b | Sayaç yalnız 30 sn'ye göreydi | Standart dışı periyotlu satır kendi sayacını gösterir | `test_hesaba_ozel_periyot_sayaci`; GUI “60 sn'lik hesap kendi sayacını gösteriyor” |
| 4c | Sır `repr`'de, ham hata mesajları | `field(repr=False)`, hata mesajlarında karakter/parça yok, `from None`, arka uç hatası yalnız sınıf adıyla | `test_sir_repr_*`, `test_hata_mesajlari_*`, `test_arka_uc_ham_hata_*`; M9 |
| 4d | Bağımlılık kilidi yoktu | `requirements-windows.lock` + `requirements-build-windows.lock` (hash'li, Windows pip'iyle üretildi); `build_windows.bat` yalıtılmış venv + `--require-hashes` | `kanit/kilit-dogrulama.txt` (temiz kurulum + bozuk hash reddi); exe bu venv'den derlendi |

Mutasyon testi (`kanit/mutasyon_testi.py`): 11 güvenlik denetimi tek tek bozuldu, **11/11** test takımınca yakalandı.

## 7. Doğrulama → teslim (1.0.1, son kaynaktan)

| Kanıt | Sonuç | Dosya |
|---|---|---|
| pytest, Linux | 75 geçti, 2 Windows'a özel atlandı | `kanit/test-linux.txt` |
| pytest, Windows Python (Wine) + gerçek WinVault | 77/77 | `kanit/test-windows-wine.txt` |
| GUI akışı, Linux (Xvfb, xclip) | 24/24 | `kanit/gui-linux-cikti.txt` |
| GUI akışı, Windows Python (Wine) + WinVault | 25/25 | `kanit/gui-wine-windows-cikti.txt` |
| oathtool çapraz kontrolü (GitHub parametreleri) | 50/50 | `kanit/github-uyum-oathtool.txt` |
| Mutasyon testi | 11/11 yakalandı | `kanit/mutasyon-sonuclari.txt` |
| Paketlenmiş `.exe` (Wine) akışı | Geçti | `kanit/exe-akis-cikti.txt` |
| Kalıcı teslim | Dal push + özel ZIP + SHA-256 geri okuma | `calisma-gunlugu.md` §10 |

**Wine kabulü ≠ native Windows kabulü.** Gerçek Windows 10/11, gerçek DPAPI, SmartScreen, pano geçmişi (Win+V) ve
gerçek GitHub hesabında 2FA bu ortamda yapılamadı; adımlar `README.md` → “B) Native Windows 10/11 kabulü”.
