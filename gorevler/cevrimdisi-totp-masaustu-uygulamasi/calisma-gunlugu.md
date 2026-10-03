# Çalışma günlüğü — Çevrimdışı TOTP masaüstü uygulaması

Tarih: 2026-10-03 · Ortam: Claude Code bulut kapsayıcısı (Linux) · Dal: `claude/vigilant-ptolemy-9gu0li`
Çalışma alanı: `gorevler/cevrimdisi-totp-masaustu-uygulamasi/` (depodaki diğer projelere dokunulmadı)

## 0. Ne istendi
Kaynak sohbette (kullanıcıdan mesajla gelen `kaynak.txt`, `gorev.md`) tarif edilen ürün: Python + customtkinter +
pyotp + keyring ile, internete bağlanmayan, anahtarları Windows kasasında tutan, 6 haneli kod + 30 sn sayaç +
Kopyala butonu olan masaüstü 2FA uygulaması. Kullanıcının son sorusu: “GitHub 2FA'mı bununla halledebilir miyim?”
İstenen akış: araştırma → bağlı harita + A/B/C → uygulama → gerçek kabul → indirilebilir kalıcı teslim.

Gizlilik kararı: kaynak dosyalar yalnız bulut çalışma alanının özel geçici dizininde tutuldu; public depoya
kaynak metni, sohbet veya özel bağlam **push edilmedi**. Repoya yalnız ürün, test, kanıt ve bu belgeler girdi.

## 1. Araştırma (WebSearch / WebFetch)
- GitHub resmi belgesi okundu: TOTP = SHA1 / 6 hane / 30 sn, setup key = secret, kurtarma kodları, 28 gün check-up.
- RFC 6238, pyotp, jaraco/keyring, Microsoft `CREDENTIAL` yapısı (blob ≤ 2560 bayt), customtkinter paketleme belgesi.
- Topluluk: GOG forumu (KeePassXC + Aegis kullanımı, yedek kodlar), Ente Auth masaüstü haberi, KeePassXC TOTP belgesi.
- Yerelde doğrulama: `pyinstaller-hooks-contrib 2026.8` içinde `hook-customtkinter.py` var → `--add-data` gereksiz.
- Bağlantılar ve A/B/C kararı: `harita.md`.

## 2. Ortam kurulumu (gerçek komutlar)
| Adım | Komut | Sonuç |
|---|---|---|
| Python bağımlılıkları | `pip install pyotp keyring customtkinter pytest pyinstaller` | customtkinter 6.0.0, PyOTP 2.10.0, keyring 25.7.0, PyInstaller 6.22.3 |
| tkinter eksikti (Python 3.11) | `apt-get install python3-tk` → `python3.12 -m venv` | Tk 8.6 |
| Windows çalışma zamanı | `apt-get install wine64 wine` (Wine 9.0) + python.org `python-3.12.10-amd64.exe /quiet` | `C:\Py312\python.exe` |
| Windows tarafı paketler | `wine C:\Py312\python.exe -m pip install pyotp keyring customtkinter pyinstaller pytest` | pywin32-ctypes 0.2.3 ile WinVaultKeyring |
| GUI test araçları | Xvfb, ImageMagick `import`, `xdotool`, `xclip`, `oathtool 2.6.11` | — |

## 3. Uygulama
`uygulama/totp_masaustu.py` (tek dosya, Türkçe yorumlu): `TotpAyari` (pyotp sarmalayıcı), `anahtar_coz`
(setup key / otpauth URI doğrulama), `Kasa` (keyring; hesap adları parçalı JSON olarak yine keyring'de),
`backend_guvenli_mi` (düz metin / fail arka uçlarını reddeder), customtkinter arayüzü (üst: ekleme, orta: liste,
alt: ilerleme çubuğu + sayaç + durum). Kararların gerekçesi `harita.md` §4.

## 4. Sorunlar ve çözümler (sırayla)
1. Python 3.11'de tkinter yok → sistem Python 3.12 venv.
2. Wine'da `xvfb-run` altında pip “Invalid handle” → stdin `/dev/null`, çıktı `| cat` ile alındı.
3. `test_windows_backend_winvault` ilk çalıştırmada ChainerBackend döndü; sonra test kasası sınıfı da zincire
   girdi (keyring alt sınıfları otomatik keşfeder) → test sınıfı “uygun değil” işaretlendi, test zinciri açtı.
4. Windows konsolunda `--kasa-tani` Türkçe karakterde `UnicodeEncodeError` → `reconfigure(errors="replace")`.
5. Wine GUI'de ilk ekran siyah / “Boş liste” adımı kaldı → kök neden: sürücünün `app.resizable()` sorgusu,
   customtkinter'ın Windows'a özgü başlık çubuğu yeniden çizimini tetikliyordu. Sorgu `wm resizable` ile yapıldı
   (uygulamada değişiklik gerekmedi; gerçek `mainloop` akışı etkilenmez — exe ekran görüntüleri bunu gösteriyor).
6. Ekle sonrası ipucu metinleri kayboluyordu → odak kaldırılıp ipucu yeniden etkinleştirildi.
7. Son hesap silinince boş liste kaydı kasada kalıyordu → liste boşsa parça kayıtlar da silinir (test eklendi).
8. Kapanışta bekleyen `after` işleri “invalid command name” uyarısı veriyordu → `destroy()` zamanlayıcıları iptal eder.
   (customtkinter'ın kendi iç `update/check_dpi_scaling` uyarıları yalnız aynı süreçte ikinci pencere açan
   test sürücüsünde görülür.)

## 5. Doğrulamalar
| Doğrulama | Komut | Sonuç |
|---|---|---|
| pytest (Linux) | `python -m pytest testler -v` | 28 geçti, 2 atlandı (Windows'a özel) |
| pytest (Windows Python, Wine, gerçek WinVault) | `wine C:\Py312\python.exe -m pytest testler -v` | **30/30** |
| GUI akışı (Linux, Xvfb) | `xvfb-run python kanit/gui_kabul.py linux` | **17/17** |
| GUI akışı (Windows Python, Wine, WinVault) | `xvfb-run wine C:\Py312\python.exe kanit/gui_kabul.py wine-windows` | **17/17** |
| GitHub parametreleri, bağımsız araç | `python kanit/github_uyum_capraz.py` (oathtool) | **50/50** |
| `.exe` derleme | `wine C:\Py312\python.exe -m PyInstaller --onedir --windowed --exclude-module PIL ...` | 32 MB onedir |
| `.exe` kullanıcı akışı | `kanit/exe_akis.sh` (xdotool: yaz → Ekle → Kopyala → kapat → aç) | ekrandaki kod = pano = beklenen; yeniden açılışta hesap geldi |
| Düz metin taraması | Wine prefix'inde anahtarın ASCII/UTF-16 araması | Kasa kayıtlarında yok (yalnız pip önbelleği/pyotp README'deki herkese açık örnek) |
| Temizlik | testlerden sonra `user.reg` içinde `My2FAApp` sayısı | 0 |

Kullanılan tek anahtar `JBSWY3DPEHPK3PXP`: pyotp belgelerindeki herkese açık örnek; hiçbir hesaba ait değil.
Hiçbir gerçek hesap, parola veya anahtar kullanılmadı/uydurulmadı.

## 6. Yapılamayanlar (somut engel)
- **Gerçek Windows 10/11** — bulutta yok. Wine, Windows API'sini taklit eder ama DPAPI şifrelemesini birebir
  sağlamaz. Gerekli girdi: kullanıcının kendi Windows makinesinde README “Windows kabul listesi” (8 adım).
- **Gerçek GitHub hesabında 2FA açma** — kullanıcının hesabı ve oturumu gerekir; yapılmadı, adımlar README'de.
- **Kod imzalama** — sertifika yok; exe imzasız (SmartScreen uyarısı olabilir).

## 7. Teslim
- Dal push: bkz. git geçmişi (`gorevler/cevrimdisi-totp-masaustu-uygulamasi/`).
- Özel ZIP (kaynak sohbet metni içermez): `CevrimdisiTOTP-1.0.0-teslim.zip` — içinde Windows `.exe` (onedir),
  kaynak kod, testler, kanıtlar, belgeler ve `SHA256SUMS.txt`. SHA-256 değeri ve geri okuma sonucu
  aşağıdaki “Teslim doğrulaması” bölümünde.

## 8. Teslim doğrulaması
| Öğe | Değer |
|---|---|
| ZIP | `CevrimdisiTOTP-1.0.0-teslim.zip` (13 965 296 bayt), sohbete dosya olarak gönderildi; public depoya konmadı |
| ZIP SHA-256 | `cf1ba0b7d1733babb5c09b7badf0f48c6a0ce6f4775d9ccbf76261f9fb79387e` |
| `CevrimdisiTOTP.exe` SHA-256 | `83f13520a803f966e50f540fefae7022c73aea76506b3c8a910a42c639c41966` |
| `totp_masaustu.py` SHA-256 | `59e2bbc235d192bec177196446ef96f8ad96f74ce95098658a01bc293aa47a9b` |
| Geri okuma | `sha256sum -c` ZIP: OK · `unzip -t`: hata yok · içerideki `SHA256SUMS.txt`: 1009/1009 dosya OK |
| Çıkarılan kopya | ZIP'ten çıkarılan `.exe` Wine'da açıldı, `Kasa: WinVaultKeyring · çevrimdışı` gösterdi |
| Gizlilik | Çıkarılan içerikte kaynak sohbet ifadeleri arandı: eşleşme yok |
| Uzak dal | `claude/vigilant-ptolemy-9gu0li` push edildi; `git fetch` sonrası uzak HEAD = yerel HEAD |

## 9. Sonraki adım (kullanıcı)
README → “Windows kabul listesi” 8 adımını gerçek Windows'ta uygulayın; sonra README → “GitHub 2FA'yı bu
uygulamayla kurma” adımlarıyla kendi hesabınızda etkinleştirin (kurtarma kodlarını indirerek).
