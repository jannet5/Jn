# Çalışma günlüğü — telefonun kendiliğinden yeniden başlaması teşhisi

Tarih: 2026-10-03 · Ortam: Claude Code Cloud (Linux konteyner) · Dal: `claude/busy-ritchie-3w6er3`

## 1. İstenen
Kullanıcı: telefonu kendi kendine kapanıp açılıyor; donanım mı yazılım mı,
kendi yazıp yüklediği uygulama mı sebep, "net anla". Görev kartı kabulleri:
- K1 Yeni yüklenen uygulamalar ↔ kapanma zamanı karşılaştırması.
- K2 Donanım/yazılım ayrımı: güncel log + güvenli testler.
- K3 Kanıt yokken tek kesin neden ilan etme; uygulanabilir test yolu.
Sınır: yeni uygulama ürünü yapma; kaynak sohbet metnini public depoya koyma.

## 2. Kaynak okuma ve gizlilik
- Kaynak (`kaynak.txt`, `gorev.md`) yalnız bulut scratchpad'e kaydedildi
  (`.../scratchpad/ozel-kaynak/`), depoya eklenmedi. Kaynakta kişisel veri
  yok; gerçek telefon kaydı/uygulama listesi/log **yok**.
- Kaynaktaki "şu an telefonum bağlı" ifadesi eski bir yerel oturuma ait; bu
  bulut konteynerine bağlı cihaz yok (`adb` bile kurulu değildi). Bu yüzden
  kullanıcının telefonu için teşhis sonucu **üretilmedi/uydurulmadı**.
- Depodaki mevcut `android-app/` (WinRemoteMonitor) yalnız okundu: manifestte
  yalnız `INTERNET` ve `CAMERA` izni var, servis/alıcı yok. Kullanıcının
  telefona yüklediği uygulamanın bu olup olmadığı **bilinmiyor**; mevcut
  projelere dokunulmadı.

## 3. Araştırma (WebSearch/WebFetch)
| Kaynak | Ne öğrenildi |
|---|---|
| https://source.android.com/docs/core/architecture/bootloader/boot-reason | `sys.boot.reason`, `ro.boot.bootreason`, `persist.sys.boot.reason`, `sys.boot.reason.last`; değerler: `kernel_panic`, `watchdog`, `shutdown,battery`, `shutdown,thermal`, `reboot,userrequested`, `cold/hard/warm` |
| https://developer.android.com/tools/logcat | `-b crash`, `-b all`, `-L/--last` (önceki açılış), `-d` |
| https://developer.android.com/studio/debug/bug-report | `adb bugreport` içeriği (dumpsys, logcat, anr) |
| https://support.google.com/pixelphone/answer/4582729?hl=en | Resmî sıra: güncelleme, %10 boş alan, Güvenli Mod, son uygulamaları kaldır, aksesuar çıkar, fabrika ayarı |
| https://support.google.com/pixelphone/answer/2852139?hl=en | Güvenli Mod'a giriş |
| https://gitlab.com/fdroid/fdroiddata/-/work_items/979 | Topluluk: uygulama başlatılınca system_server SIGSEGV → telefon yeniden başlıyor (AAPT2 kaynak bozulması) |
| https://nvd.nist.gov/vuln/detail/CVE-2017-5217 | İzinsiz uygulama bile system_server'ı çökertip yeniden başlatabilir (eski Android) |
| Samsung topluluk / Asurion | Güvenli Mod + eleme; pil/çıkarılabilir pil önerileri (Samsung sayfası WebFetch'e 403 verdi, yalnız arama özeti) |
| forum.earlybird.club | Root olmadan `last_kmsg` okunamıyor → araç root gerektirmeyen kaynaklara dayandırıldı |

Arama sonuçlarının bir kısmı spam/kopya siteydi (ör. `?p=` uzantılı alakasız
alan adları); bunlar kaynak olarak kullanılmadı.

## 4. Kararlar
- Yol A seçildi: salt-okunur Python betiği (`arac/reboot_teshis.py`). Neden:
  hazır araçların (Android Studio Logcat, bugreport, Battery Historian)
  hiçbiri "yeniden başlama anı ↔ son kurulan uygulama" eşleştirmesi yapmıyor;
  K1 bunu istiyor. Telefonu değiştiren hiçbir komut yok.
- Python yoksa Yol B (bugreport + elle arama), her durumda Yol C (Güvenli Mod).
- Karar dili kanıt düzeyine bağlı: paket adı system_server çökmesinde geçmedikçe
  "kesin" denmiyor; kanıt yoksa rapor açıkça "Tek kesin neden ilan edilemez" der (K3).

## 5. Uygulama ve testler
- `arac/reboot_teshis.py` yazıldı (topla / analiz / hepsi).
- `testler/test_reboot_teshis.py` + `testler/sahte_adb.py` + `testler/ornek-veri/`
  (**SENTETİK** veri, gerçek telefon değil). Komut:
  `python3 testler/test_reboot_teshis.py` → 5 test, **OK** (ilk çalıştırma).
