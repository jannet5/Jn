# Bağlı harita — telefonun kendiliğinden yeniden başlaması

Hedef: kapanıp açılmanın **uygulama/yazılım** mı **donanım/güç** mı kaynaklı
olduğunu kanıtla ayırmak; kanıt yokken tek kesin neden ilan etmemek; yeni
uygulama ürünü yapmamak (yalnız salt-okunur teşhis aracı + kılavuz).

```
HEDEF ─► BAĞIMLILIKLAR ─► A/B/C YOLLARI ─► UYGULAMA ─► TEST ─► KALICI TESLİM
  │           │                 │               │           │          │
  │   adb + USB hata ayıklama   │        arac/reboot_teshis.py│    dal push +
  │   telefonun kendisi         │        teshis-kilavuzu.md   │    temiz ürün ZIP+SHA-256
  │   (cloud'da YOK → engel;    │        GİZLİLİK KATMANI:    │    (kaynak/ham kayıt yok)
  │    adb devices -l boş)      │        izin listesi → zaman │
  │                             │        penceresi → maskele →│
  │                             │        takma ad → geri-okuma│
  │                             │        denetimi → paylasim. │
  │                             │        zip + SHA            │
  │                             ├─ A: adb kayıt toplama + otomatik analiz (SEÇİLDİ)
  │                             ├─ B: adb bugreport zip + elle inceleme (yedek yol)
  │                             └─ C: kablosuz/ADB'siz: Güvenli Mod + eleme (her durumda)
  └─ kabul: K1 uygulama↔zaman, K2 log+güvenli test ayrımı, K3 kanıtsız kesin neden yok
```

## 1. Bağımlılıklar
| Bağımlılık | Durum | Not |
|---|---|---|
| Kullanıcının telefonu (USB bağlı) | **Cloud'da yok** | Kaynakta "şu an telefonum bağlı" yazıyor ama bu bulut konteynerine bağlı değil. |
| Android SDK Platform-Tools (`adb`) | Kullanıcı kurar | https://developer.android.com/tools/releases/platform-tools |
| USB hata ayıklama izni | Kullanıcı açar | Geliştirici olduğu için muhtemelen açık. |
| Python 3.8+ | Yol A için | Yoksa Yol B/C. |
| Gerçek reboot logu, uygulama envanteri | **Yok** | Uydurulmadı; araç bunları toplar. |

## 2. Yollar
| Yol | Ne | Artı | Eksi | Karar |
|---|---|---|---|---|
| **A** | `reboot_teshis.py hepsi` → izinli getprop anahtarları, dropbox olay listesi + çökme özetleri, süzülmüş logcat (crash, events, -L), pil/ısı, depolama, 3. taraf paket kurulum zamanları → `rapor-yerel.md` + serbest metinsiz `paylasim.zip` | Tek komut, zaman eşleştirmesi otomatik, root gerekmez, telefonu değiştirmez, ham veri diske yazılmaz | Python 3.10+ ve adb gerekir; üretici bazı kayıtları kısıtlayabilir (raporda "veri tamlığı" olarak görünür) | **Seçildi** |
| **B** | Elle adb komutları / `adb bugreport` (resmî) | Python gerekmez | Maskesiz; bugreport 50–300 MB ve çok kişisel veri içerir → paylaşılmamalı | A çalışmazsa yalnız yerel inceleme için yedek |
| **C** | Güvenli Mod + son uygulamaları eleme (Google/Samsung resmî) | Bilgisayar gerekmez, kesin ayırt edici test | Yavaş (gözlem süresi gerekir), log vermez | Her durumda A/B'den sonra yapılır (K2, K3) |

Neden A: araştırmada hazır karşılaştırılan seçenekler — Android Studio Logcat
(canlı izler, önceki açılışı ve dropbox'ı özetlemez), `adb bugreport`
(kapsamlı ama ham), Battery Historian (pil odaklı, kurulum ağır, reboot nedeni
sınıflamaz), üçüncü taraf “reboot log” uygulamaları (çoğu root ister ya da
yetkisi yok). Hiçbiri “yeni kurulan uygulama ↔ yeniden başlama anı”
eşleştirmesini yapmıyor; bu yüzden ince bir salt-okunur betik yazıldı.

## 3. Gizlilik ve karar ilkeleri (bağımsız statik inceleme sonrası, sürüm 2)
| İlke | Uygulama | Test |
|---|---|---|
| Ham içerik paylaşıma girmez | Tam getprop yok (10 izinli anahtar); logcat/dropbox bellekte süzülür; `--ham-sakla` yoksa ham dosya yazılmaz; ZIP yalnız `paylasim/` | `test_basarili_ve_ham_yazilmaz`, `test_ham_sakla_paylasima_girmez` |
| Yerel çıktı da kişiseldir | `ozet-yerel.json`/`rapor-yerel.md` uygulama adı, kurulum zamanı, maskelenmiş mesaj tutar; belgeler "kişisel veri diske hiç yazılmaz" demez | `test_yerel_ozet_de_ham_icerik_tutmaz` |
| Paylaşımda serbest metin yok (v3) | İstisna mesajı, Subject, sinyal açıklaması, stderr paylaşılmaz; `HATA_SINIFLARI` izin listesi, Watchdog kategori/izinli iş parçacığı, `SIGxxx`; diğerleri `<uygulama-istisnası>`/`<diğer-…>` | `test_paylasimda_serbest_metin_yok_yalniz_yapisal_sinif`, `test_yapisal_siniflar` |
| Atomik paylaşım (v3) | Her çalıştırma önce eski `paylasim/`, `.zip`, `.sha256` ve yarım geçici klasörleri siler; paket geçici klasörde üretilir, disk+ZIP+SHA geri okunur, sonra `os.replace` (SHA en son); hata/bulgu → hiçbir paylaşım artefaktı kalmaz | `EskiPaylasimArtefaktlari` (4 test) |
| Zaman sınırı | Varsayılan son 7 gün (cihazın yerel saatine göre) | `test_zaman_penceresi` |
| Maskeleme (yerel) + takma ad | e-posta, telefon/IMEI, IPv4/IPv6, MAC, URI, Android/Windows/ev yolları, JSON/`alan=değer` sırları, Bearer/Basic, seri ve android_id; 3. taraf paketler `uyg-xxxxxxxx`. Düz kişi metni regex ile yakalanamaz → bu yüzden paylaşıma serbest metin girmez | `test_maskele_*`, `test_paylasimda_pii_ve_gercek_paket_yok` |
| Geri-okuma denetimi | Paylaşım dosyaları diskten ve ZIP'ten geri okunup **tanımlı örüntüler** için taranır (IPv6, Windows/ev yolu, JSON sırrı, Bearer dahil); bulgu → paket yok, çıkış 4. "Bulunmadı" bir garanti değildir | `test_maskeleme_bozulursa_paket_uretilmez`, `test_denetle_komutu` |
| Yapısal adb sonucu | Her komut: çıkış kodu, zaman aşımı, izin, servis yok, süre → `toplama` | `test_izin_hatasi_*`, `test_zaman_asimi_*` |
| Seri güvenliği | `adb devices -l` ayrıştırılır; verilen seri listede/hazır değilse ilerlenmez | `test_cihaz_sec`, `test_cli_seri_listede_yoksa_ilerlemez` |
| Eksik veri ≠ arıza yok | "Veri tamlığı" tablosu; ilgili bölümler "toplanamadı" der | `test_izin_hatasi_*` |
| Paket adı ≠ neden | Hipotez dili; ilk hata/olay zamanı/Güvenli Mod biri eksikse "belirsiz hipotez" | `test_masum_paket_adi_neden_ilan_edilmez` |
| Güvenli Mod ve reset = olasılık | Güvenli Mod sonucu "olası"; reset sonrası firmware/sürücü/sistem ile donanım ayrı | `test_guvenli_mod_olasilik_dili` |

## 4. Uygulama → test → teslim zinciri
1. `arac/reboot_teshis.py` (topla/analiz/hepsi) — salt-okunur adb komutları.
2. `testler/test_reboot_teshis.py` — sentetik fikstür + sahte adb ile birim ve uçtan uca test.
3. Araç doğrulaması (kullanıcı kabulü DEĞİL): yazılım-modu emülatör (API 30) üzerinde sürüm 1 ile alınan ham kayıtlar, sürüm 2 `isle` ile cihaza dokunmadan yeniden işlendi → `kabul/` (yalnız maskelenmiş paylaşım raporları).
4. Kullanıcının telefonunda çalıştırma — **yapılamadı**: bu ortamda `adb devices -l` boş; komut kılavuzda.
5. Dal push + geri okuma; yalnız ürün köklü temiz ZIP (kaynak sohbeti, ham kayıt, `__pycache__` yok) + tam SHA-256 + açıp test.

## 5. Kırılma noktaları ve alternatif
| Kırılma | Alternatif |
|---|---|
| `adb devices` → `unauthorized` | Telefonda RSA iznini onayla; olmazsa “USB hata ayıklama yetkilerini iptal et” → yeniden bağla |
| Python yok | Yol B: `adb bugreport` + kılavuzdaki aramalar |
| `dumpsys dropbox` boş / izin yok / zaman aşımı | Rapor "veri tamlığı"nda gösterir (arıza yok sayılmaz); bir sonraki kapanmadan hemen sonra tekrar topla; Yol C |
| Paylaşım denetimi bulgu verir (çıkış 4) | Paylaşım paketi üretilmez; yalnız yerel rapor kullanılır; bulgu kategorisi bildirilir |
| Verilen seri listede yok/unauthorized | Araç ilerlemez; `adb devices -l` ile doğru seriyi seç, telefonda izni onayla |
| Telefon bilgisayar bağlıyken açık kalmıyor (bootloop) | Yol C: Güvenli Mod / kurtarma modundan önbellek temizleme; servis |

## 6. Referanslar (tam bağlantılar)
- AOSP Canonical boot reason: https://source.android.com/docs/core/architecture/bootloader/boot-reason
- Android logcat aracı: https://developer.android.com/tools/logcat
- Android hata raporu (bugreport): https://developer.android.com/studio/debug/bug-report
- Pixel — yeniden başlayan/çöken telefon: https://support.google.com/pixelphone/answer/4582729?hl=en
- Pixel — Güvenli Mod ile sorunlu uygulama bulma: https://support.google.com/pixelphone/answer/2852139?hl=en
- Topluluk vakası: uygulama başlatılınca system_server çöküp telefon yeniden başlıyor (F-Droid): https://gitlab.com/fdroid/fdroiddata/-/work_items/979 , https://forum.f-droid.org/t/several-apps-caused-bootloop-on-cm-12-1/1509
- İzinsiz uygulamanın system_server'ı çökertebilmesi (CVE-2017-5217): https://nvd.nist.gov/vuln/detail/CVE-2017-5217
- Samsung topluluk/destek tartışmaları (Güvenli Mod, eleme): https://us.community.samsung.com/t5/Galaxy-S26/Phone-restarting-randomly/td-p/3554022 (sayfa bot erişimine 403 verdi; yalnız arama özetinden okundu), https://www.asurion.com/connect/tech-tips/samsung-phone-keeps-restarting
- Root olmadan `last_kmsg` okunamaması (topluluk): https://forum.earlybird.club/threads/random-reboot-on-stock-rom.715058/
