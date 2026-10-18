# WinRemote Monitor — yapay zekâ asistanı için kurulum ve yayın talimatı

Bu klasörü alan yapay zekâ asistanına: kullanıcı bu ürünü (1) kendi Windows
bilgisayarına ve Android telefonuna kurmanı, (2) istenirse Google Play'de
yayınlamasına yardım etmeni istiyor. Kullanıcıyla **Türkçe, kısa** konuş;
her adımın sonunda "Kontrol" satırını doğrula.

## Ürün
- `WinRemoteMonitor-Kurulum.exe`: Windows kurulum programı (Windows 10/11, 64-bit).
  Arka planda çalışan bir ajan kurar; telefon ona yerel ağda TLS + QR eşleştirme
  ile bağlanır. İnternete/sunucuya veri gitmez.
- `app-release.apk`: Android uygulaması (Android 8.0+).
- `telefona-kur.bat`: APK'yı USB ile telefona kurar.
- `google-play/`: Play'de yayın için her şey (AAB, mağaza metinleri, görseller,
  gizlilik politikası, form cevapları, **GİZLİ** yükleme anahtarı).
- Telefon ile bilgisayar **aynı Wi-Fi ağında** olmalı.

## 1) Windows'a kur
Zip'i bir klasöre çıkar. Kullanıcı ile:
```powershell
.\WinRemoteMonitor-Kurulum.exe            # sihirbaz: İleri, İleri, Bitti
# veya sessiz kurulum (yönetici onayı ister):
.\WinRemoteMonitor-Kurulum.exe /VERYSILENT /SUPPRESSMSGBOXES
```
"Windows bilgisayarınızı korudu" çıkarsa: **Ek bilgi → Yine de çalıştır**
(program kod imzalı değil). Kurulum: `C:\Program Files\WinRemote Monitor`,
veriler `C:\ProgramData\WinRemoteMonitor`, güvenlik duvarı izni ve oturum
açılışında otomatik başlatma otomatik yapılır. Sonunda tarayıcıda QR sayfası açılır.

Kontrol:
```powershell
Get-Process WinRemoteMonitor                                  # çalışıyor olmalı
Get-Content C:\ProgramData\WinRemoteMonitor\agent.log -Tail 5  # "listening on :8787"
```

## 2) Telefona kur
- **USB ile (sen yapabilirsin):** Kullanıcıya telefonda *Ayarlar → Telefon
  hakkında → Yapım numarasına 7 kez dokun → Geliştirici seçenekleri → USB hata
  ayıklama: Açık* yaptır, telefonu USB ile bağlat, sonra `telefona-kur.bat`'ı
  çalıştır. Telefonda çıkan "USB hata ayıklamaya izin verilsin mi?" sorusuna
  **İzin ver** dedirt. (Script Google'ın resmi platform-tools paketini
  indirir ve `adb install -r app-release.apk` çalıştırır.)
- **USB'siz:** APK'yı telefona gönder (e-posta, WhatsApp, bulut), telefonda aç →
  "Bilinmeyen kaynaklara izin ver" → Yükle.
- Uygulama Play'de yayınlandıktan sonra müşteriler doğrudan Play'den indirir.

Kontrol: `adb shell pm list packages com.jn.winremote` → `package:com.jn.winremote`

## 3) Eşleştir
1. Bilgisayarda QR sayfası açık değilse: **Başlat → Telefonu Eşleştir**
   (veya masaüstündeki kısayol; ya da
   `& "C:\Program Files\WinRemote Monitor\WinRemoteMonitor.exe" --pair --open`).
2. Telefonda uygulamayı aç → **QR Tara** → kamera iznine *Uygulamayı
   kullanırken* → telefonu QR'a **düz karşıdan** tut → **Eşleştir**.
3. QR okunmazsa **Elle Gir**: sayfadaki "QR okunmuyorsa elle girin"
   bölümündeki adres, port (8787), kod ve parmak izini yazdır. Bu değerler
   `C:\ProgramData\WinRemoteMonitor\pairing.html` dosyasında da var.
4. Kod 5 dakika geçerli ve tek kullanımlık; süresi dolarsa 1. adımı tekrarla.

Kontrol: telefonda **Panel** açılır, değerler canlı değişir. Bilgisayarda:
`& "C:\Program Files\WinRemote Monitor\winremotemonitor-agent.exe" devices list`
eşleşen telefonu listeler.

## Sorun giderme
| Belirti | Yapılacak |
|---|---|
| "Bilgisayara bağlanılamadı" | Aynı Wi-Fi mi? Wi-Fi'ın "Misafir ağı" olmadığından emin ol (misafir ağları cihazları birbirinden ayırır). Bilgisayarda `Get-Process WinRemoteMonitor`. Windows ağ profili "Ortak" ise "Özel" yap. QR sayfasındaki diğer adresleri Elle Gir'de dene. |
| "Kodun süresi doldu" | Başlat → Telefonu Eşleştir ile yeni kod. |
| "Sunucu kimliği doğrulanamadı" | Program kaldırılıp yeniden kurulduysa sertifika değişir: telefonda Ayarlar → eşleşmeyi kaldır, yeniden eşleştir. |
| Program çalışmıyor | Oturumu kapatıp aç (otomatik başlar) veya Başlat menüsünden yeniden kur. Günlük: `C:\ProgramData\WinRemoteMonitor\agent.log`. |
| İzlenen klasörleri değiştirmek | `C:\Program Files\WinRemote Monitor\config.json` (yönetici olarak düzenle) → `watched_roots`; `%USERPROFILE%` kullanılabilir. Sonra programı yeniden başlat. |
| Telefondan açılabilecek programlar | Aynı klasörde `allowed_apps.json`. |

## 4) Google Play'de yayınlama (kullanıcının hesabıyla)
`google-play/README.md` adım adım anlatıyor. Özet: Play Console hesabı (25 $,
kimlik doğrulama) → uygulama oluştur → `play-console-cevaplari.md`'deki
cevapları gir → mağaza kaydına metin ve görselleri yükle → **kapalı test**
(yeni kişisel hesaplarda 12 testçi / 14 gün) → `app-release.aab`'yi yükle
(Play Uygulama İmzalama'yı kabul et) → üretime başvur.
Gizlilik politikasındaki `[iletişim e-postanız]` alanını kullanıcıya doldurt.

## Kurallar (uyulması zorunlu)
- `google-play/GIZLI/` içindeki yükleme anahtarını ve parolasını **hiçbir
  yere yükleme, paylaşma, sohbete yapıştırma**; kullanıcıya güvenli bir yere
  (ör. şifreli USB + bulut yedek) yedeklemesini söyle. Kaybolursa Play
  Console'dan "yükleme anahtarını sıfırla" ile yenisi istenebilir.
- `allowed_apps.json`'a kullanıcı açıkça istemedikçe program ekleme; özellikle
  `cmd.exe`, `powershell.exe` ekleme.
- Güvenlik duvarını kapatma; router'da port yönlendirme yapma (program
  internete açılmak için tasarlanmadı).
- Eşleştirme kodunu, parmak izini, `C:\ProgramData\WinRemoteMonitor`
  içeriğini paylaşma.

## Kaldırma
Windows: *Ayarlar → Uygulamalar → WinRemote Monitor → Kaldır* (veya Başlat
menüsündeki kısayol). Tüm veriler, güvenlik duvarı kuralı ve otomatik
başlatma silinir. Telefon: uygulamayı normal şekilde kaldır.
