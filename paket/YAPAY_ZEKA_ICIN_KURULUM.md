# WinRemote Monitor — yapay zekâ asistanı için kurulum talimatı

Bu klasörü alan yapay zekâ asistanına: kullanıcı bu ürünü kendi Windows
bilgisayarına ve Android telefonuna kurmanı istiyor. Aşağıdaki adımları
sırayla uygula, her adımın sonunda "Kontrol" satırını doğrula. Kullanıcıyla
**Türkçe** ve kısa konuş.

## Ürün nedir

- `winremotemonitor-agent.exe`: Windows'ta arka planda çalışan ajan. Telefona
  CPU/RAM/disk, süreç listesi, dosya olayları ve uyarılar gönderir. TCP 8787,
  TLS + sertifika parmak izi sabitleme + eşleştirme kodu ile korunur.
- `app-release.apk`: Android uygulaması (Android 8.0+).
- Telefon ve bilgisayar **aynı yerel ağda (Wi-Fi)** olmalı; internet
  üzerinden çalışmaz.

## Klasördeki dosyalar

| Dosya | Ne işe yarar |
|---|---|
| `KUR.bat` | Çift tıklanır, `kur.ps1`'i çalıştırır (tam kurulum) |
| `kur.ps1` | Kurulum: kopyalama, config, güvenlik duvarı, otomatik başlatma, ilk eşleştirme kodu |
| `yeni-kod.bat` | Yeni eşleştirme kodu + QR üretir (kod 5 dk geçerli) |
| `KALDIR.bat` / `kaldir.ps1` | Her şeyi kaldırır (`-KeepData` ile eşleşmeleri korur) |
| `allowed_apps.json` | Telefondan başlatılabilecek programlar (örnek: Not Defteri, Hesap Makinesi) |
| `SHA256SUMS.txt` | Dosya bütünlüğü için SHA-256 değerleri |

## Adım 1 — Bütünlük kontrolü (önerilir)

```powershell
Get-FileHash .\winremotemonitor-agent.exe, .\app-release.apk -Algorithm SHA256
```
Kontrol: değerler `SHA256SUMS.txt` ile aynı olmalı.

## Adım 2 — Windows'a kur

Zip'i bir klasöre **çıkar** (zip içinden çalıştırma). Sonra o klasörde:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\kur.ps1
```
(veya kullanıcıya `KUR.bat`'a çift tıklamasını söyle.)

Betiği **yönetici olarak başlatma**; normal kullanıcı olarak çalıştır. Betik
sadece güvenlik duvarı kuralı için tek bir UAC onayı ister — kullanıcıya
"Evet" demesini söyle. (Ajan yönetici olarak ilk kez çalışırsa veri klasörü
yöneticiye ait olur ve sonraki normal açılışlarda ajan veritabanına yazamaz.)

Seçenekler: `-InstallDir D:\Klasor`, `-Port 8787`, `-NoFirewall`,
`-NoAutostart`, `-NoPair`.

Betik şunları yapar: `C:\WinRemoteMonitor`'a kopyalar; yoksa `config.json`
oluşturur (Downloads, Masaüstü, Belgeler izlenir); güvenlik duvarında TCP
8787'yi açar; Başlangıç klasörüne kısayol koyar (oturum açılınca ajan gizli
başlar); ajanı başlatır; eşleştirme kodunu yazdırır ve `pairing-qr.png`'yi açar.

Kontrol:
```powershell
Get-Process winremotemonitor-agent
Get-Content C:\WinRemoteMonitor\agent.log -Tail 5   # "listening on :8787" görünmeli
```
Çıktıdaki `Connect to:` satırı bilgisayarın yerel IP'sidir (örn. 192.168.1.20).
`127.0.0.1` ise ağ bağlantısı yoktur — önce Wi-Fi'ı düzelt.

## Adım 3 — APK'yı telefona kur

- **USB ile (adb varsa):** telefonda Geliştirici seçenekleri → USB hata
  ayıklama açık olmalı. `adb install -r app-release.apk`
- **USB'siz:** `app-release.apk`'yı telefona gönder (kablo, e-posta, bulut),
  telefonda dosyaya dokun → "Bilinmeyen kaynaklara izin ver" → Yükle.

Kontrol: telefonda "WinRemote Monitor" uygulaması görünür.

## Adım 4 — Eşleştir

1. Telefonda uygulamayı aç → **QR Tara** → kamera iznine "İzin ver".
2. Bilgisayarda açılan `pairing-qr.png`'yi telefona **düz karşıdan** göster
   (açılı tutulursa okunmayabilir).
3. Form dolunca **Eşleştir**.
   QR okunmazsa **Elle Gir**: `Connect to` IP'si, port 8787,
   `Cert fingerprint` ve `Pairing code` değerlerini yazdır.
4. Kod 5 dakikada geçer veya bir kez kullanılınca biter. Yeni kod: `yeni-kod.bat`.

Kontrol: telefonda **Panel** açılır ve CPU/RAM/disk değerleri canlı değişir.
Bilgisayarda `C:\WinRemoteMonitor\winremotemonitor-agent.exe devices list`
eşleşen telefonu listeler.

## Sorun giderme

| Belirti | Yapılacak |
|---|---|
| Telefon "bağlanamadı" / zaman aşımı | Aynı Wi-Fi mi? `netsh advfirewall firewall show rule name="WinRemoteMonitor"` kural var mı? Ağ profili "Ortak" ise "Özel" yap. `--pair` çıktısındaki `Other addresses` IP'lerini Elle Gir'de dene. |
| "Eşleştirme kodunun süresi doldu" | `yeni-kod.bat` ile yeni kod al. |
| "Sunucu kimliği doğrulanamadı" | Sertifika değişmiş (veri klasörü silinmiş). Telefonda Ayarlar → eşleşmeyi kaldır, yeniden eşleştir. |
| Ajan başlamıyor | `C:\WinRemoteMonitor\agent.log`'a bak. Port doluysa `config.json`'da `port`'u değiştir (telefonda da aynı port). |
| Telefon "çevrimdışı" | Bilgisayar açık ve ajan çalışıyor mu (`Get-Process winremotemonitor-agent`)? Değilse oturumu kapatıp aç veya `kur.ps1 -NoPair`'i tekrar çalıştır. |
| İzlenen klasörü değiştirmek | `C:\WinRemoteMonitor\config.json` → `watched_roots` (yollar `\\` ile yazılır), sonra ajanı yeniden başlat. |

## Kurallar (uyulması zorunlu)

- `allowed_apps.json`'a kullanıcı **açıkça istemedikçe** program ekleme;
  özellikle `cmd.exe`, `powershell.exe`, betik çalıştırıcıları ekleme — bu
  listeye eklenen her program telefondan başlatılabilir.
- Güvenlik duvarını tamamen kapatma; sadece TCP 8787 kuralı yeterli.
- Eşleştirme kodunu, parmak izini veya `%ProgramData%\WinRemoteMonitor`
  içeriğini (anahtarlar, veritabanı) başka yere gönderme/paylaşma.
- Ajanı internete açmak için port yönlendirme (router) yapma.
- Dosyaları düzenlerken UTF-8 kullan (ajan BOM'lu dosyayı da okur).

## Kaldırma

`KALDIR.bat` (veya `kaldir.ps1`; eşleşmeleri korumak için `-KeepData`).
Telefondaki uygulama ayrıca kaldırılır.
