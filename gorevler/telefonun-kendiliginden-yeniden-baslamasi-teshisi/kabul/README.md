# Gerçek Android üzerinde kabul doğrulaması

**Ne olduğu:** Android SDK emülatörü, Android 11 (API 30, `google_apis`,
x86_64), KVM olmadan yazılım modunda (`-no-accel`) bu bulut konteynerinde
çalıştırıldı. Gerçek `adb` (platform-tools r37.0.1) ve gerçek Android
servisleri kullanıldı; çıktılar sahte değil. **Ama bu kullanıcının telefonu
değildir** — kullanıcının cihazı için teşhis sonucu bu klasörde yoktur.

KVM yokluğunda emülatör çok yavaş çalıştığı için `system_server` gerçekten
Watchdog'a takılıp birkaç kez kendiliğinden yeniden başladı. Bu, aranan olay
sınıfının (yazılım tarafı "yumuşak" yeniden başlama) gerçek bir örneği oldu.

| # | Senaryo (gerçekten yapılan) | Rapor | Sonuç |
|---|---|---|---|
| 1 | İlk açılış sonrası toplama (aracın ilk sürümü) | `1-ilk-acilis-rapor.md` | `reboot,factory_reset` okundu; dropbox'ta gerçek `system_server_watchdog`. **Açık bulundu:** events süzgeci boş döndü, dropbox servisi yeniden başlarken yoktu → araç düzeltildi (servis bekleme, events'i cihazda grep). Bu rapor sonradan yeni analizle yeniden üretildi; events dosyası eski sürümle toplandığı için boştur. |
| 2 | `adb install` ile kodsuz test APK'sı (`com.ornek.benimuygulamam`, kurucu=null) yan yüklendi 12:06:49; `kill -9 system_server` ile gerçek çerçeve yeniden başlaması 12:07:22 | `2-yan-yukleme-ve-system-server-yeniden-baslama-rapor.md` | Yan yükleme tespit edildi; 4 çerçeve başlangıcı / 3 yumuşak yeniden başlama sayıldı; **12:07:39 yeniden başlaması ↔ 12:06:49 kurulum eşleşti (K1)**. **Açık bulundu:** `kill -9` dropbox kaydı bırakmıyor → `boot_progress_start` zamanları da yeniden başlama anı sayıldı. |
| 3 | `adb reboot` → 430 sn'de açıldı; ardından emülatör kendi kendine yine yumuşak yeniden başladı | `3-adb-reboot-sonrasi-rapor.md` | `sys.boot.reason = reboot,shell` doğru okundu ve "kullanıcı/komut" sınıflandı. Gerçek `system_server_crash: IllegalStateException: Lost network stack` ve 3 Watchdog konusu özetlendi. **Açık bulundu:** yoğun sistemde `dumpsys` 10 sn'de zaman aşımına uğradı → `dumpsys -t 60` + rapor uyarısı eklendi. |

**K3 kanıtı (gerçek veriyle):** Senaryo 3'te test uygulaması her yeniden
başlamadan önce kurulu görünüyor, ama uygulamanın **hiç kodu yok** ve çökme
metni ağ yığını zaman aşımını gösteriyor. Araç uygulamayı "kesin neden"
ilan etmedi; yalnız "zamansal yakınlık nedensellik değildir" uyarısıyla
listeledi ve kök neden ipucunu çökme metninden verdi.

**Yapılamayanlar:** kullanıcının gerçek telefonunda çalıştırma, Güvenli Mod
testi, pil/şarj donanım testleri (fiziksel cihaz gerekir). Emülatör pili
sanaldır (sağlık "iyi", 25 °C) — donanım dalı yalnız sentetik testle sınandı.
