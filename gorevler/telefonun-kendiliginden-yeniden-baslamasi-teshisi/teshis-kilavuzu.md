# Telefon kendi kendine kapanıp açılıyor — donanım mı, yazılım mı?

**Kısa cevap:** Şu an elde telefonun kaydı olmadığı için "kesin şu" demek
yanlış olur. Ama ayırmak zor değil; aşağıdaki 3 adım genelde 1 gün içinde
netleştirir. Kendi yazdığın uygulamanın sebep olması **mümkün** (Android'de
bir uygulama `system_server`'ı çökertirse telefon "yumuşak" yeniden başlar —
F-Droid'de bu yaşanmış gerçek vakalar var), ama sıradan bir uygulama
(özellikle yalnız INTERNET/KAMERA izinli, servis çalıştırmayan bir uygulama)
genelde sadece kendisi çöker, telefonu kapatmaz. Donanımda en sık sebep
yıpranmış/şişmiş pil, gevşek pil bağlantısı ve sıkışan güç tuşudur.

## Adım 1 — Kayıtları al (10 dk, Windows'ta)
1. Platform-Tools'u indir, bir klasöre aç: https://developer.android.com/tools/releases/platform-tools
2. Telefonda *Geliştirici seçenekleri → USB hata ayıklama* açık olsun; bağla, telefonda izni onayla.
3. O klasörde PowerShell aç:
   ```powershell
   .\adb devices              # "device" yazmalı
   python reboot_teshis.py hepsi --cikti kayit1
   ```
   (`reboot_teshis.py` dosyasını platform-tools klasörüne kopyala ya da `adb`'yi PATH'e ekle.)
4. `kayit1\rapor.md`'yi aç. **En iyi zaman:** telefon kendiliğinden yeniden başladıktan hemen sonra (kayıtlar zamanla silinir).

Python yoksa (Yol B):
```powershell
.\adb shell getprop sys.boot.reason
.\adb shell getprop ro.boot.bootreason
.\adb shell getprop sys.boot.reason.last
.\adb shell dumpsys dropbox | findstr /i "system_server SYSTEM_RESTART SYSTEM_BOOT LAST_KMSG TOMBSTONE app_crash"
.\adb shell pm list packages -3 -i
.\adb shell dumpsys battery
.\adb bugreport kayit-bugreport.zip
```

## Adım 2 — Raporu yorumla

| `sys.boot.reason` / iz | Anlamı | Ağırlık |
|---|---|---|
| `reboot,userrequested`, `reboot,adb`, `reboot,ota`, `shell` | Sen/komut/güncelleme yeniden başlattı | Sorun değil |
| dropbox'ta `system_server_crash`, `system_server_watchdog`, `SYSTEM_RESTART` | Android çerçevesi çöktü → **yazılım** | Çökme metninde paket adı geçiyorsa o uygulama baş şüpheli |
| `reboot,rescueparty` | Android art arda çökme gördü, kurtarma devreye girdi → **yazılım** | Son yüklenen uygulamaya bak |
| `kernel_panic`, `watchdog`, `*wdt*`, `hw_reset` | Çekirdek/donanım watchdog | Yazılım (sürücü/ROM) **veya** donanım; Güvenli Mod testi ayırır |
| `shutdown,battery`, `undervoltage`, `brownout`, `ocp`, `pmic` | Güç kesildi/gerilim düştü → **donanım/pil** | Pil sağlığı ≠ "iyi" ise güçlenir |
| `shutdown,thermal…` | Isı koruması | Isınma kaynağını bul (uygulama yükü veya donanım) |
| `cold`, `hard`, `warm`, sadece `reboot` | Genel, ayırt etmez | Adım 3 şart |

Raporun 6. bölümü, her yeniden başlama anından önceki 72 saatte kurulan veya
güncellenen uygulamaları listeler; "Kurucu = null / com.android.shell" olanlar
adb/APK ile yan yüklenmiştir (kendi uygulaman büyük ihtimalle burada).

## Adım 3 — Güvenli testler (kesin ayırt eden kısım)
1. **Güvenli Mod**: Güç tuşuna basılı tut → "Kapat"a uzun bas → Güvenli Mod. (Pixel 6+: Güç+Ses Açma.) Normalde kapandığı kadar süre kullan.
   - Güvenli Mod'da **hiç kapanmıyor** → indirdiğin/yan yüklediğin bir uygulama. Normal moda dön, **önce kendi APK'nı**, sonra son yüklenenleri tek tek kaldır, her birinden sonra gözle.
   - Güvenli Mod'da **da kapanıyor** → sistem yazılımı veya donanım. 4'e geç.
2. Kendi uygulamandan şüpheleniyorsan, kaldırmadan önce telefona bağlıyken çalıştırıp izle:
   `adb logcat -b crash -b system -v time | findstr /i "FATAL system_server Watchdog <paket.adın>"` — telefon kapanırken son satırlar ipucu verir.
3. Depolama: %90'dan fazla doluysa yer aç (Google: %10'dan az boş alan sorun çıkarabilir); sistem güncellemelerini kur.
4. Donanım ayrımı: kılıfı çıkar (güç tuşuna baskı), şarjdayken vs. pilde, soğukken vs. sıcakken davranışı karşılaştır. Arka kapakta kabarma/ekranda kalkma varsa **telefonu şarj etme, servise götür** (şişmiş pil tehlikelidir).
5. Son çare: yedek al → fabrika ayarı → hiçbir uygulama kurmadan 1–2 gün kullan. Yine kapanıyorsa **donanım** neredeyse kesin.

## Karar özeti
- Güvenli Mod temiz + rapor `system_server_crash` içinde paket adı → **o uygulama (yazılım)**.
- Güvenli Mod temiz, paket adı yok → **bir 3. taraf uygulama**, eleme ile bulunur.
- Güvenli Mod'da da var + `shutdown,battery`/kötü pil sağlığı/şarjda düzeliyor → **pil/güç donanımı**.
- Güvenli Mod'da da var + `kernel_panic/watchdog` + güncel yazılım + fabrika ayarında da sürüyor → **donanım (anakart/bellek/pil)**.
