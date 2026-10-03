# Yeniden başlama teşhis raporu — PAYLAŞIM SÜRÜMÜ (maskelenmiş)

Bu sürümde serbest metin maskelenmiş, üçüncü taraf paket adları takma adla (`uyg-xxxxxxxx`) gösterilmiştir. Ham kayıt içermez.

Cihaz saati: 2026-10-03 11:56:17 · Zaman penceresi: son 7 gün

## 1. Cihaz

- Model: Google sdk_gphone_x86_64
- Android: 11 (SDK 30), güvenlik yaması 2021-08-05

## 2. Son açılış nedeni (AOSP canonical boot reason)

- `sys.boot.reason` = `reboot,factory_reset` → **Kullanıcı/komut/OTA kaynaklı planlı yeniden başlatma**
- `ro.boot.bootreason` = `reboot,factory_reset`
- önceki/kalıcı = `reboot`

## 3. Veri tamlığı

Toplama durumu bilinmiyor (ham klasörden yeniden işlendi).

- Olay zamanı: var · İlk hata zamanı: 2026-10-03 11:51:36 · Güvenli Mod karşılaştırması: YOK

## 4. Hipotezler — kanıt düzeyi: **belirsiz hipotez (ilk hata, olay zamanı veya Güvenli Mod karşılaştırmasından en az biri eksik)**

**H1. Android sistem yazılımı tarafında yumuşak yeniden başlama**
- destek: crash tamponunda 4 'sistem öldü' satırı
- not: Tetikleyici bir uygulama, sistem bileşeni, düşük bellek/depolama veya donanım kaynaklı takılma olabilir; çökme metninin ilk istisnası ipucudur, kanıt değildir.

Güvenli Mod: Güvenli Mod karşılaştırması henüz yapılmadı/bildirilmedi.

## 5. Olay zaman çizelgesi

| Zaman | Kaynak | Olay |
|---|---|---|
| 2026-10-03 11:51:34 | dropbox | system_server_watchdog |

### crash tamponunda çöken süreçler

| Süreç | Sayı |
|---|---|
| `com.android.systemui` | 1 |
| `com.google.android.setupwizard` | 1 |
| `com.google.android.gms.persistent` | 1 |
| `com.android.phone` | 1 |

## 6. Yeniden başlama anları ↔ önceki 72 saatte kurulan/güncellenen uygulamalar

| An | Öncesindeki kurulum/güncellemeler |
|---|---|
| 2026-10-03 11:51:34 | — |

Zamansal yakınlık nedensellik değildir.

## 7. İlgili üçüncü taraf uygulamalar (pencerede kurulan/güncellenen veya yan yüklenen; 0 diğer uygulama listelenmedi)

—

## 8. Pil, ısı, depolama

- Sağlık: iyi; seviye %100; sıcaklık 25.0 °C; gerilim 5000 mV (Android'in bildirdiği değer; kapasite/şişme ölçümü değildir)
- Termal durum kodu: 0
- /data doluluk: %1
- Önceki açılış logu (`logcat -L`): yok/desteklenmiyor

## 9. Sonraki güvenli test sırası

1. Telefon bir sonraki kendiliğinden kapanıp açıldığında **hemen** aracı tekrar çalıştırın (kayıtlar halka tampondadır).
2. Güvenli Mod'da en az normalde kapandığı kadar kullanın; sonucu `--guvenli-mod kapanmadi|kapandi` ile verin. Sonuç **olasılık** bildirir, kesinlik değil.
3. Güvenli Mod'da kapanmıyorsa: yan yüklenen/son güncellenen uygulamaları birer birer kaldırıp her birinden sonra gözleyin.
4. Güvenli Mod'da da kapanıyorsa: resmi sistem güncellemelerini kurun; şarjda/pilde, soğuk/sıcak, kılıfsız durumu karşılaştırın; kabarma varsa şarj etmeyin, servise götürün.
5. Yedek alıp fabrika ayarından sonra uygulama kurmadan da sürerse: üçüncü taraf uygulamalar büyük ölçüde elenir; **sistem yazılımı/firmware/sürücü ile donanım hâlâ ayrılmamıştır** (fabrika ayarı firmware'i değiştirmez). Ayrım için servis: resmi firmware'in yeniden yüklenmesi ve donanım testi (pil ölçümü vb.).
