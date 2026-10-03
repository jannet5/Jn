# Araç doğrulaması — emülatör (KULLANICININ TELEFONU İÇİN KABUL DEĞİLDİR)

**Önemli:** Buradaki raporlar Android SDK emülatöründen (Android 11, API 30,
`google_apis`, x86_64, KVM'siz yazılım modu) alınmıştır. Bunlar yalnız
**aracın gerçek Android çıktı biçimlerini doğru işlediğini** gösterir.
Kullanıcının fiziksel telefonu için hiçbir teşhis sonucu veya kabul kanıtı
değildir. Bu bulut ortamında resmi `adb devices -l` çıktısı (platform-tools
37.0.1) **boştur**; fiziksel telefon bağlı görünmemektedir.

## Nasıl üretildi
1. 2026-10-03'te emülatörde araç sürüm 1 ile ham kayıtlar alındı (o sürüm ham
   dosyaları diske yazıyordu). Senaryolar:
   - 1: ilk açılış sonrası (`reboot,factory_reset`).
   - 2: kodsuz test APK'sı adb ile yan yüklendi (12:06:49), ardından
     `kill -9 system_server` ile çerçeve yeniden başlatıldı (12:07:22).
   - 3: `adb reboot` → açılış nedeni `reboot,shell`; ardından yavaş emülatör
     kendi kendine yine Watchdog/`Lost network stack` ile yumuşak yeniden başladı.
2. İki bağımsız statik incelemeden sonra araç güncellendi (sürüm 2, sonra
   serbest metinsiz paylaşım + atomik paket). Eski ham kayıtlar **cihaza
   dokunmadan** `reboot_teshis.py isle` ile yeniden işlendi; burada yalnız
   **paylaşım** raporları duruyor (ham kayıtlar ve yerel raporlar depoya
   konmadı). Paylaşım raporlarında hata mesajı metni yok, yalnız hata sınıfı
   ve kategori var. Her biri `reboot_teshis.py denetle` ile "Tanımlı örüntüler
   bulunmadı" (bu bir garanti değil, yalnız örüntü listesinin sonucu).

| Dosya | Gösterdiği |
|---|---|
| `emulator-1-ilk-acilis-paylasim.md` | Açılış nedeni okuma; dropbox Watchdog. Sürüm 1'in events süzgeci boş döndüğü için çerçeve başlangıcı bilgisi yok (o sürümün hatası). |
| `emulator-2-yan-yukleme-ve-system-server-paylasim.md` | Yan yükleme tespiti (takma adla), `boot_progress_start` ile yumuşak yeniden başlama sayımı, kurulum ↔ yeniden başlama zaman eşleşmesi |
| `emulator-3-adb-reboot-sonrasi-paylasim.md` | `reboot,shell` sınıflaması; `java.lang.IllegalStateException` (mesajı "Lost network stack" yalnız yerel raporda) ve Watchdog kategorileri; kodsuz test uygulaması zaman olarak yakın olsa da **neden ilan edilmiyor** (belirsiz hipotez) |

## Bu doğrulamanın sınırları
- Emülatör pili sanaldır; pil/ısı/güç dalı yalnız sentetik testlerle sınandı.
- Güvenli Mod, fabrika ayarı, donanım testleri yapılmadı (fiziksel cihaz gerekir).
- Sürüm 2'nin canlı adb toplama yolu sahte adb ile (izin/zaman aşımı/seri
  senaryoları dahil) test edildi; sürüm 2 canlı bir cihazda henüz çalıştırılmadı.
