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

## 6. Gerçek Android kabul turu (emülatör)
Komutlar (özet):
```
sdkmanager "platform-tools" "emulator" "system-images;android-30;google_apis;x86_64" "build-tools;34.0.0" "platforms;android-30"
avdmanager create avd -n teshis -k "system-images;android-30;google_apis;x86_64" -d pixel
emulator -avd teshis -no-accel -gpu swiftshader_indirect -no-window -no-audio -no-snapshot -memory 3072 -cores 4
python3 arac/reboot_teshis.py hepsi --cikti emu-kayit1      # tur 1 (3 dk 19 sn)
aapt2 link / zipalign / apksigner → kodsuz test.apk; adb install -r test.apk
adb root; adb shell 'kill -9 $(pidof system_server)'        # gerçek çerçeve yeniden başlaması
python3 arac/reboot_teshis.py hepsi --cikti emu-kayit2      # tur 2
adb reboot   (430 sn'de açıldı; sys.boot.reason=reboot,shell)
python3 arac/reboot_teshis.py hepsi --cikti emu-kayit3      # tur 3
```
Sorun → çözüm:
1. Depoda hazır APK yok (`android-app/dist` yalnız SHA256SUMS içeriyor) → yalnız manifestli, kodsuz test APK'sı derlendi (depoya eklenmedi).
2. `logcat -e` süzgeci events tamponunda boş döndü → cihazda `grep -E` ile süzme.
3. Yeniden başlama sırasında `Can't find service: dropbox` → `service check dropbox` ile 2 dk'ya kadar bekleme.
4. `kill -9` ile olan yeniden başlama dropbox'a yazılmadı → `boot_progress_start` zamanları yeniden başlama anı olarak eklendi; eşleşme sonra doğru çıktı.
5. `*** SERVICE 'dropbox' DUMP TIMEOUT (10000ms) ***` → `dumpsys -t 60` ve raporda zaman aşımı uyarısı.
6. Kullanıcıya en faydalı bilgi çökme metninin ilk istisnası → "kök neden ipuçları" tablosu eklendi (gerçek örnek: `Lost network stack`).
Her düzeltmeden sonra `python3 testler/test_reboot_teshis.py` → 6 test OK.
Ayrıntı: `kabul/README.md` ve üç rapor.

## 7. Kabul durumu
| Kabul | Durum | Kanıt |
|---|---|---|
| K1 uygulama ↔ kapanma zamanı | Araç gerçek Android'de doğruladı; kullanıcının telefonunda **yapılmadı** | `kabul/2-…`, `kabul/3-…` bölüm 6 |
| K2 log + güvenli testlerle ayrım | Log tarafı gerçek Android'de doğrulandı; Güvenli Mod/donanım testleri **kullanıcıya bağlı** | rapor bölüm 2–5, 9; `teshis-kilavuzu.md` Adım 3 |
| K3 kanıtsız kesin neden yok | Gerçek veriyle gösterildi + birim test | `kabul/README.md`, `test_kanitsiz_kesin_neden_yok` |

Kullanıcıdan gereken gerçek girdi: telefon bağlıyken `python reboot_teshis.py hepsi --cikti kayit1` → `kayit1/rapor.md` (+ isterse tüm `kayit1` klasörü) ve Güvenli Mod sonucu.

## 8. Kalıcı teslim ve geri okuma
- Dal `claude/busy-ritchie-3w6er3` push edildi; `git fetch` sonrası uzak = yerel `86e3d94`; uzaktan `git archive` ile çıkarılan kopyada testler yeniden çalıştırıldı → OK.
- Özel ZIP (yalnız bulut scratchpad'de, depoya eklenmedi): `telefon-reboot-teshis-teslim.zip`, 21 dosya, kaynak sohbet metni içermez (metin araması 0 eşleşme).
  SHA-256 `676ae807def84d032bcbd7c8bf5d18fb64a27585d1d648e43210bb4d1ff2ce74` — `sha256sum -c` OK; ZIP açılıp testler çalıştırıldı → OK.
  (ZIP bu bölüm eklenmeden önceki `86e3d94` içeriğinden üretildi.)
- Emülatör iş bitince kapatıldı.
