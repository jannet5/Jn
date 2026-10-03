# Yeniden başlama teşhis raporu

Kayıt klasörü: `emu-kayit3`  
Üretim: 2026-10-03 12:33

## 1. Cihaz

- Model: Google sdk_gphone_x86_64
- Android: 11 (SDK 30), güvenlik yaması 2021-08-05
- Yapı: `google/sdk_gphone_x86_64/generic_x86_64_arm64:11/RSR1.240422.006/12134477:userdebug/dev-keys`

## 2. Son açılış nedeni (AOSP canonical boot reason)

- `sys.boot.reason` = `reboot,shell` → **Kullanıcı/komut/OTA kaynaklı planlı yeniden başlatma**
- `ro.boot.bootreason` (önyükleyici) = `reboot,shell`
- önceki/kalıcı (`sys.boot.reason.last` / `persist.sys.boot.reason`) = `reboot,shell`

## 3. Kanıtlar
- 4 adet system_server çökme/watchdog/SYSTEM_RESTART kaydı var (yazılım tarafı yumuşak yeniden başlama izi).
- Bu çekirdek açılışından beri Android çerçevesi **3 kez** başlatılmış (`boot_progress_start`) → 2 yumuşak yeniden başlama (yazılım tarafı).
- 1 adet system_server Watchdog uyarısı: Blocked in monitor com.android.server.StorageManagerService on foreground thread (android.fg)
- `logcat -b crash` içinde 33 adet 'sistem öldü' (DeadSystemException/FATAL IN SYSTEM PROCESS) satırı.
- Yan yüklenen `com.ornek.benimuygulamam`, 4 yeniden başlamadan (ilki 2026-10-03 12:21:03) önceki 72 saat içinde kurulmuş/güncellenmiş. Zamansal yakınlık nedensellik kanıtı değildir; Güvenli Mod ve kaldırma testiyle doğrulayın.

## 4. Ara karar (kanıt düzeyine göre)

**Yazılım tarafı OLASI** (system_server yeniden başlaması izi var) ama belirli uygulama kaydı yok. Güvenli Mod testi ve yeni yüklenen uygulamaları tek tek kaldırma ile daraltın.

## 5. Olay zaman çizelgesi (dumpsys dropbox)

| Zaman | Etiket |
|---|---|
| 2026-10-03 11:51:34 | system_server_watchdog |
| 2026-10-03 11:57:49 | system_server_watchdog |
| 2026-10-03 12:14:56 | system_server_anr |
| 2026-10-03 12:21:03 | system_server_crash |
| 2026-10-03 12:27:02 | system_server_watchdog |

### Sistem çökmesi metinlerinden kök neden ipuçları

| Etiket: ilk istisna / konu | Sayı |
|---|---|
| `system_server_crash: java.lang.IllegalStateException: Lost network stack` | 1 |
| `system_server_watchdog: Blocked in handler on main thread (main)` | 1 |
| `system_server_watchdog: Blocked in handler on ui thread (android.ui)` | 1 |
| `system_server_watchdog: Blocked in monitor com.android.server.StorageManagerService on foreground thread (android.fg)` | 1 |

### logcat -b crash: çöken süreçler

| Süreç | Sayı |
|---|---|
| `com.android.phone` | 2 |
| `com.android.systemui` | 2 |
| `com.android.bluetooth` | 2 |
| `com.android.networkstack.process` | 1 |
| `com.android.ims.rcsservice` | 1 |
| `com.google.android.deskclock` | 1 |
| `com.google.android.apps.nexuslauncher` | 1 |
| `com.google.android.gms` | 1 |
| `com.google.android.providers.media.module` | 1 |
| `com.google.android.inputmethod.latin` | 1 |
| `com.google.android.googlequicksearchbox:interactor` | 1 |

## 6. Yeniden başlama anları ↔ son 72 saatte kurulan/güncellenen uygulamalar

| Yeniden başlama/çökme anı | Öncesindeki kurulum/güncellemeler |
|---|---|
| 2026-10-03 11:51:34 | — |
| 2026-10-03 11:57:49 | — |
| 2026-10-03 12:21:03 | `com.ornek.benimuygulamam` |
| 2026-10-03 12:21:12 | `com.ornek.benimuygulamam` |
| 2026-10-03 12:27:02 | `com.ornek.benimuygulamam` |
| 2026-10-03 12:27:16 | `com.ornek.benimuygulamam` |

## 7. Üçüncü taraf uygulamalar (en yeni güncellenen önce)

| Paket | Kurucu | Sürüm | İlk kurulum | Son güncelleme | Not |
|---|---|---|---|---|---|
| `com.ornek.benimuygulamam` | null | 0.3-debug | 2026-10-03 12:06:49 | 2026-10-03 12:06:49 | yan yükleme/adb |

## 8. Pil, ısı, depolama

- Sağlık: iyi; seviye %100; sıcaklık 25.0 °C; gerilim 5000 mV; şarj: AC=? USB=false
- /data doluluk: %2
- `logcat -b crash` satır sayısı: 64
- Önceki açılış logu (`logcat -L`, pstore): yok/desteklenmiyor

## 9. Sonraki güvenli test sırası

1. Telefon bir sonraki kendiliğinden kapanıp açıldığında **hemen** bu aracı tekrar çalıştırın (kayıtlar halka tampon; geç kalınca silinir).
2. Güvenli Mod'da (üçüncü taraf uygulamalar kapalı) en az eşit süre kullanın: kapanma **durursa** → uygulama; **sürerse** → sistem/donanım.
3. Güvenli Mod temizse: yan yüklenen/son güncellenen uygulamaları (önce kendi APK'nız) birer birer kaldırıp her birinden sonra gözleyin.
4. Güvenli Mod'da da sürüyorsa: şarjdayken vs. pilde, soğukken vs. sıcakken, kılıfsız ve tuşlara baskısız durumu karşılaştırın; pil sağlığı/şişme kontrolü için servise götürün.
5. Yedek alıp fabrika ayarı sonrası hiçbir uygulama kurmadan da tekrarlıyorsa → donanım neredeyse kesin.
