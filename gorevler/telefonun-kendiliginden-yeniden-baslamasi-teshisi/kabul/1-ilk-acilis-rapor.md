# Yeniden başlama teşhis raporu

Kayıt klasörü: `emu-kayit1`  
Üretim: 2026-10-03 12:33

## 1. Cihaz

- Model: Google sdk_gphone_x86_64
- Android: 11 (SDK 30), güvenlik yaması 2021-08-05
- Yapı: `google/sdk_gphone_x86_64/generic_x86_64_arm64:11/RSR1.240422.006/12134477:userdebug/dev-keys`

## 2. Son açılış nedeni (AOSP canonical boot reason)

- `sys.boot.reason` = `reboot,factory_reset` → **Kullanıcı/komut/OTA kaynaklı planlı yeniden başlatma**
- `ro.boot.bootreason` (önyükleyici) = `reboot,factory_reset`
- önceki/kalıcı (`sys.boot.reason.last` / `persist.sys.boot.reason`) = `reboot`

## 3. Kanıtlar
- 1 adet system_server çökme/watchdog/SYSTEM_RESTART kaydı var (yazılım tarafı yumuşak yeniden başlama izi).
- `logcat -b crash` içinde 8 adet 'sistem öldü' (DeadSystemException/FATAL IN SYSTEM PROCESS) satırı.

## 4. Ara karar (kanıt düzeyine göre)

**Yazılım tarafı OLASI** (system_server yeniden başlaması izi var) ama belirli uygulama kaydı yok. Güvenli Mod testi ve yeni yüklenen uygulamaları tek tek kaldırma ile daraltın.

## 5. Olay zaman çizelgesi (dumpsys dropbox)

| Zaman | Etiket |
|---|---|
| 2026-10-03 11:51:34 | system_server_watchdog |

### logcat -b crash: çöken süreçler

| Süreç | Sayı |
|---|---|
| `com.android.systemui` | 1 |
| `com.google.android.setupwizard` | 1 |
| `com.google.android.gms.persistent` | 1 |
| `com.android.phone` | 1 |

## 6. Yeniden başlama anları ↔ son 72 saatte kurulan/güncellenen uygulamalar

| Yeniden başlama/çökme anı | Öncesindeki kurulum/güncellemeler |
|---|---|
| 2026-10-03 11:51:34 | — |

## 7. Üçüncü taraf uygulamalar (en yeni güncellenen önce)

| Paket | Kurucu | Sürüm | İlk kurulum | Son güncelleme | Not |
|---|---|---|---|---|---|

## 8. Pil, ısı, depolama

- Sağlık: iyi; seviye %100; sıcaklık 25.0 °C; gerilim 5000 mV; şarj: AC=? USB=false
- /data doluluk: %1
- `logcat -b crash` satır sayısı: 13
- Önceki açılış logu (`logcat -L`, pstore): yok/desteklenmiyor

## 9. Sonraki güvenli test sırası

1. Telefon bir sonraki kendiliğinden kapanıp açıldığında **hemen** bu aracı tekrar çalıştırın (kayıtlar halka tampon; geç kalınca silinir).
2. Güvenli Mod'da (üçüncü taraf uygulamalar kapalı) en az eşit süre kullanın: kapanma **durursa** → uygulama; **sürerse** → sistem/donanım.
3. Güvenli Mod temizse: yan yüklenen/son güncellenen uygulamaları (önce kendi APK'nız) birer birer kaldırıp her birinden sonra gözleyin.
4. Güvenli Mod'da da sürüyorsa: şarjdayken vs. pilde, soğukken vs. sıcakken, kılıfsız ve tuşlara baskısız durumu karşılaştırın; pil sağlığı/şişme kontrolü için servise götürün.
5. Yedek alıp fabrika ayarı sonrası hiçbir uygulama kurmadan da tekrarlıyorsa → donanım neredeyse kesin.
