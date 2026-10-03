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

## 9. Bağımsız statik inceleme (commit 47d21f1) ve sürüm 2 düzeltmeleri
İncelemenin bulguları ve yapılan değişiklik:
| Bulgu | Düzeltme |
|---|---|
| Tam getprop, tüm logcat tamponları, ham dropbox içeriği, tam `pm list`, exception/Subject metni ham kişisel içerik topluyordu; maskesiz rapor paylaşıma hazır değildi | 10 izinli getprop anahtarı; logcat/dropbox bellekte süzülür, ham hali diske yazılmaz (`--ham-sakla` yalnız yerel, paylaşıma girmez); istisna/Subject/sinyal metinleri maskelenir; varsayılan 7 günlük zaman penceresi; paylaşımda 3. taraf paketler takma ad; yalnız ilgili (pencerede/yan yüklenen) uygulamalar listelenir |
| Redaksiyonun geri-okuma kontrolü yoktu | Paylaşım dosyaları diskten ve ZIP'ten geri okunup e-posta/URI/IP/MAC/telefon-IMEI/kullanıcı yolu/yapı parmak izi/seri/gerçek paket adı/maskelenmemiş noktalı ad için taranır; bulgu → paket silinir, çıkış 4; ayrıca `denetle` komutu |
| "Paket adı görüldü → YÜKSEK tetikleme olasılığı" yanlış nedensellikti | "YÜKSEK" kaldırıldı; hipotez listesi + kanıt düzeyi; ilk hata/olay zamanı/Güvenli Mod karşılaştırmasından biri eksikse "belirsiz hipotez" |
| Güvenli Mod sonucu kesin gibi; "reset sonrası donanım neredeyse kesin" | Güvenli Mod "olası"; reset sonrası "sistem yazılımı/firmware/sürücü ile donanım hâlâ ayrılmamış" (kılavuz + rapor + test) |
| adb exit code/timeout/izin bilgisi kayboluyordu; verilen seri hazır listede değilken ilerleyebiliyordu; eksik veri "kayıt yok" gibi okunuyordu | `AdbSonuc` (çıkış kodu, zaman aşımı, izin, servis yok, süre) → `toplama`; `adb devices -l` ayrıştırma + seri doğrulama; "Veri tamlığı" tablosu ve "eksik veri ≠ arıza yok" |
| Testler anlamlı değildi | 21 test: masum paket adı, PII maskeleme/denetim, sızıntı enjekte edilince paket üretilmemesi, izin (255), zaman aşımı, seri yok/unauthorized/boş/çoklu, zaman penceresi, ham yazılmaması, `--goster`, Güvenli Mod dili |

Kendi gözden geçirmemde ayrıca düzeltilenler: grep boş eşleşmeyi (çıkış 1) hata sanma → süzme Python'da; çökme metnindeki `SecurityException` izin hatası sanılmasın → izin tespiti stderr + ilk 3 satır; cihazın yerel saati pencere için esas; `system_server_wtf/anr` çökme sayılmasın (eski emülatör kaydında 85 → 4).

Komutlar ve sonuçlar:
- `python3 testler/test_reboot_teshis.py` → 21 test OK (Python 3.10, 3.11, 3.13).
- `adb devices -l` (platform-tools 37.0.1) → **boş liste**: fiziksel kullanıcı telefonu bağlı görünmüyor. Emülatör kapalı ve kullanıcının kabulü sayılmadı.
- Eski emülatör ham kayıtları cihaza dokunmadan `reboot_teshis.py isle` ile yeniden işlendi → `kabul/emulator-*-paylasim.md`; üçü de `denetle` → DENETİM TEMİZ. Eski maskesiz `kabul/*-rapor.md` dosyaları depodan kaldırıldı.
- Bu turda root, reset, cihaz yeniden başlatma, uygulama kaldırma yapılmadı.

Kalan gerçek girdi: kullanıcının telefonundan `paylasim.zip` ve Güvenli Mod sonucu.

## 10. Sürüm 2 kalıcı teslim ve geri okuma
- Push: `825eca7` — `git fetch` sonrası uzak = yerel.
- Temiz ürün ZIP'i uzak commit'ten `git archive` ile üretildi (sabit zaman damgalı, tek kök klasör
  `telefonun-kendiliginden-yeniden-baslamasi-teshisi/`, 18 dosya; kaynak sohbeti, `gorev.md`,
  ham/yerel kayıt, `__pycache__`, `.git` yok — yalnız ürünün kendi `.gitignore`'u var).
  `telefon-reboot-teshis-v2.zip` SHA-256 `161e6360f22a04ec84815bb7f7461a83683a172fbd1c81a3721b3a3f786e8de6`
  → `sha256sum -c` OK, `unzip -t` hatasız, kaynak metin araması 0, açılan kopyada 21 test OK,
  `kabul/` raporları `paylasim_denetle` → TEMİZ. (ZIP bu bölüm eklenmeden önceki `825eca7` içeriğidir.)
- ZIP depoya eklenmedi; özel bulut çalışma alanında tutulup kullanıcıya dosya olarak iletildi.

## 11. İkinci bağımsız statik inceleme (661f1e3) ve düzeltmeler
İnceleme uzak HEAD ve 18 dosya hash'ini eşleştirdi; iki yüksek öncelikli açık bildirdi:

| Bulgu | Düzeltme | Test |
|---|---|---|
| `ciktilari_yaz` eski `paylasim.zip`/`.sha256`'yı başta silmiyordu; yeni denetim başarısız olursa eski paket teslim gibi kalıyordu | `paylasim_temizle()` her çalıştırmanın başında `paylasim/`, `paylasim.zip`, `paylasim.zip.sha256` ve yarım `.paylasim-gecici-*` klasörlerini siler. Yeni paket `tempfile.mkdtemp` geçici klasörde üretilir → disk geri okuma denetimi → ZIP `testzip` + ZIP içeriği geri okuma denetimi → SHA dosyası geri okuma → ancak hepsi geçerse `os.replace` (SHA en son). Bulgu → `None` + hiçbir paylaşım artefaktı; istisna → tam temizlik ve yeniden fırlatma | `EskiPaylasimArtefaktlari`: önceki ZIP/SHA dururken sızıntı enjekte; CLI çıkış 4 + eski paket yok; `_zip_yaz` yarıda hata; yeniden çalıştırmada SHA tutarlılığı |
| Serbest Subject/exception metni yalnız regex maskelemesiyle paylaşılıyordu (düz kişi metni, IPv6, Windows yolu, JSON sırrı, Bearer kaçabilir) | Paylaşıma serbest metin HİÇ girmez: `HATA_SINIFLARI` izin listesi (diğerleri `<uygulama-istisnası>`/`<diğer-platform-istisnası>`), Watchdog → `handler-takılması:<izinli iş parçacığı>` / `monitor-takılması:com.android.server.*`, sinyal → `sinyal:SIGxxx`; paylaşım JSON'unda `mesaj/konu/sinyal/hata(stderr)` alanı yok. Yerel maskeleme ve denetim desenlerine IPv6, Windows/ev yolu, JSON/anahtar sırrı, Bearer/Basic eklendi | fixture'a düz kişi adı + IPv6 + Windows/ev yolu + JSON api_key + Bearer + 3. taraf istisna sınıfı; `test_paylasimda_serbest_metin_yok_yalniz_yapisal_sinif`, `test_yapisal_siniflar`, `test_maskele_yeni_bicimler`, `test_denetim_yeni_bicimleri_yakalar` |
| Yerel `ozet-yerel.json`/`rapor-yerel.md` kişisel uygulama ve kurulum metadata tutuyor; "ham dosya yok" ≠ "kişisel veri diske yazılmıyor" | README, kılavuz, harita ve araç açıklaması düzeltildi: yerel dosyalar kişiseldir, paylaşılmamalı, işi bitince silinmeli | `test_yerel_ozet_de_ham_icerik_tutmaz` (ne tuttuğu/tutmadığı) |
| `denetle` "TEMİZ" diyordu | Çıktı: "Tanımlı örüntüler bulunmadı. (Bu, kişisel veri bulunmadığının garantisi değildir …)" | `test_denetle_komutu` ("TEMİZ" yok) |

Bu turdaki önceki iddiaların düzeltilmesi: §9 ve §10'daki "DENETİM TEMİZ" ifadeleri yalnız "tanımlı
örüntüler bulunmadı" anlamındadır; §9'daki "istisna/Subject/sinyal metinleri maskelenir" artık
yalnız YEREL rapor için geçerlidir, paylaşımda bu metinler hiç yoktur.

Doğrulama:
- `python3 testler/test_reboot_teshis.py` → **29 test OK** (Python 3.10, 3.11, 3.13).
- `kabul/emulator-*-paylasim.md` eski emülatör ham kayıtlarından `isle` ile cihaza dokunmadan yeniden üretildi; üçü de `denetle` → "Tanımlı örüntüler bulunmadı".
- Fiziksel telefon yok (`adb devices -l` boş, §9); reset/root/canlı kişisel veri toplama yapılmadı. Cihaz kabulü ayrı ve bulutta tamamlanmış sayılmaz.

## 12. v3 kalıcı teslim ve geri okuma
- Push `01ca743`; `git fetch` sonrası uzak HEAD = yerel.
- `telefon-reboot-teshis-v3.zip` uzak `01ca743`'ten `git archive` ile üretildi (sabit zaman damgası; tek kök
  `telefonun-kendiliginden-yeniden-baslamasi-teshisi/`; 18 dosya; `__pycache__`, `.git`, yerel/ham çıktı,
  paylaşım ZIP'i, kaynak sohbeti, `gorev.md` yok).
  SHA-256 `263d60718b4068dd390e0f2c7c7a7b5d2d44d10656dca4b42c3c5df771f16519` → `sha256sum -c` OK,
  `unzip -t` hatasız, kaynak metin araması 0, açılan kopya uzak commit içeriğiyle `diff -r` aynı,
  açılan kopyada 29 test OK, `denetle kabul` → "Tanımlı örüntüler bulunmadı" (garanti değildir).
  (ZIP bu bölüm eklenmeden önceki `01ca743` içeriğidir.)
- ZIP depoya eklenmedi; özel bulut çalışma alanından kullanıcıya iletildi.
- Açık kalan: fiziksel telefonda canlı çalıştırma, Güvenli Mod ve donanım testleri — ayrı cihaz kabulü; bulutta tamamlanmış sayılmaz.
