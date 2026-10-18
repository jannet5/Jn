# Internetli emülatör testi (TAMAMLANAMADI)

Test edilen APK: dist/Koken.apk, sha256 e1150749b4228a08674133409cd393489d83201eaea24f67b00b7cd143eb5db6 (kurulum başarılı: "Success").

## Yapılanlar
1. Emülatör (k30, API 30, yazılımsal) `-http-proxy http://127.0.0.1:40483 -writable-system` ile açıldı; ~13 dk sürdü (14:09 -> 14:22).
2. Proxy CA'sı (/root/.ccr/agent-proxy-ca.crt, CN=CCR agent-proxy interception CA, hash_old 0266b358) `adb remount` (overlayfs, verity kapatıldı; yeniden başlatma yapılmadı) yerine tmpfs bindirme ile /system/etc/security/cacerts altına, init ve zygote/zygote64 mount ad alanlarına nsenter ile kondu. Dosya hem init hem zygote64 ad alanında görüldü (ls ile doğrulandı).
3. APK kuruldu (pm path app.koken görüldü).

## Başarısız / yapılamayan
- Enjeksiyondan sonra Android çerçevesi (system_server) ölüp yeniden başladı; sonra SystemServer.systemReady içinde NullPointerException (RootWindowContainer.getTopDisplayFocusedStack) ile döngüye girdi (logcat-internet.txt). Neden kesin belirlenemedi (olası: tmpfs/ad alanı müdahalesi, yazılımsal emülatör yükü/watchdog). Doğrulanmadı.
- Sonuç: uygulama AÇILAMADI; "kitap", "Ahmet", "pijama" aramaları YAPILMADI; sonuç ekranı, yol haritası ve kaynak durum şeridi (Nişanyan, TDK, Etymonline) GÖRÜLMEDİ.
- Köprü (200 + base64) yolu internetle DOĞRULANMADI. CA'nın TLS'i gerçekten çözdüğü de test edilemedi.
- Ekran-internet-*.png: yalnızca ekran-internet-son-durum.png (çerçeve çöküşü sırasındaki ekran; sonuç ekranı DEĞİL).

## Kaynak durumu
Hiçbir kaynak test edilemedi.
