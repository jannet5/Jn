# Bağlı harita — telefonun kendiliğinden yeniden başlaması

Hedef: kapanıp açılmanın **uygulama/yazılım** mı **donanım/güç** mı kaynaklı
olduğunu kanıtla ayırmak; kanıt yokken tek kesin neden ilan etmemek; yeni
uygulama ürünü yapmamak (yalnız salt-okunur teşhis aracı + kılavuz).

```
HEDEF ─► BAĞIMLILIKLAR ─► A/B/C YOLLARI ─► UYGULAMA ─► TEST ─► KALICI TESLİM
  │           │                 │               │           │          │
  │   adb + USB hata ayıklama   │        arac/reboot_teshis.py│    dal push +
  │   telefonun kendisi         │        teshis-kilavuzu.md   │    özel ZIP+SHA-256
  │   (cloud'da YOK → engel)    │                             │
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
| **A** | `reboot_teshis.py hepsi` → getprop boot reason, dumpsys dropbox, logcat (crash, -L önceki açılış), pil/ısı, depolama, 3. taraf paket kurulum/güncelleme zamanları → `rapor.md` | Tek komut, zaman eşleştirmesi otomatik, root gerekmez, telefonu değiştirmez | Python + adb gerekir; üretici bazı kayıtları kısıtlayabilir | **Seçildi** (kabul K1+K2'yi doğrudan üretir) |
| **B** | `adb bugreport` (resmî) | Her şeyi içerir, Python gerekmez | 50–300 MB, elle okumak zor | A çalışmazsa yedek; kılavuzda grep komutları var |
| **C** | Güvenli Mod + son uygulamaları eleme (Google/Samsung resmî) | Bilgisayar gerekmez, kesin ayırt edici test | Yavaş (gözlem süresi gerekir), log vermez | Her durumda A/B'den sonra yapılır (K2, K3) |

Neden A: araştırmada hazır karşılaştırılan seçenekler — Android Studio Logcat
(canlı izler, önceki açılışı ve dropbox'ı özetlemez), `adb bugreport`
(kapsamlı ama ham), Battery Historian (pil odaklı, kurulum ağır, reboot nedeni
sınıflamaz), üçüncü taraf “reboot log” uygulamaları (çoğu root ister ya da
yetkisi yok). Hiçbiri “yeni kurulan uygulama ↔ yeniden başlama anı”
eşleştirmesini yapmıyor; bu yüzden ince bir salt-okunur betik yazıldı.

## 3. Uygulama → test → teslim zinciri
1. `arac/reboot_teshis.py` (topla/analiz/hepsi) — salt-okunur adb komutları.
2. `testler/test_reboot_teshis.py` — sentetik fikstür + sahte adb ile birim ve uçtan uca test.
3. Gerçek Android (yazılım-modu emülatör, API 30) üzerinde aracı gerçek `adb` ile çalıştırma; gerçek `reboot,adb` ve gerçek `system_server` yeniden başlaması üretip raporun bunları doğru sınıfladığını görme → `kabul/`.
4. Kullanıcının telefonunda çalıştırma — **yapılamadı (cihaz yok)**; komut kılavuzda.
5. Dal push + geri okuma; kaynak metni içermeyen özel ZIP + SHA-256 geri okuma.

## 4. Kırılma noktaları ve alternatif
| Kırılma | Alternatif |
|---|---|
| `adb devices` → `unauthorized` | Telefonda RSA iznini onayla; olmazsa “USB hata ayıklama yetkilerini iptal et” → yeniden bağla |
| Python yok | Yol B: `adb bugreport` + kılavuzdaki aramalar |
| `dumpsys dropbox` boş | Üretici kısıtı/temizlenmiş; bir sonraki kapanmadan hemen sonra tekrar topla; Yol C |
| Telefon bilgisayar bağlıyken açık kalmıyor (bootloop) | Yol C: Güvenli Mod / kurtarma modundan önbellek temizleme; servis |

## 5. Referanslar (tam bağlantılar)
- AOSP Canonical boot reason: https://source.android.com/docs/core/architecture/bootloader/boot-reason
- Android logcat aracı: https://developer.android.com/tools/logcat
- Android hata raporu (bugreport): https://developer.android.com/studio/debug/bug-report
- Pixel — yeniden başlayan/çöken telefon: https://support.google.com/pixelphone/answer/4582729?hl=en
- Pixel — Güvenli Mod ile sorunlu uygulama bulma: https://support.google.com/pixelphone/answer/2852139?hl=en
- Topluluk vakası: uygulama başlatılınca system_server çöküp telefon yeniden başlıyor (F-Droid): https://gitlab.com/fdroid/fdroiddata/-/work_items/979 , https://forum.f-droid.org/t/several-apps-caused-bootloop-on-cm-12-1/1509
- İzinsiz uygulamanın system_server'ı çökertebilmesi (CVE-2017-5217): https://nvd.nist.gov/vuln/detail/CVE-2017-5217
- Samsung topluluk/destek tartışmaları (Güvenli Mod, eleme): https://us.community.samsung.com/t5/Galaxy-S26/Phone-restarting-randomly/td-p/3554022 (sayfa bot erişimine 403 verdi; yalnız arama özetinden okundu), https://www.asurion.com/connect/tech-tips/samsung-phone-keeps-restarting
- Root olmadan `last_kmsg` okunamaması (topluluk): https://forum.earlybird.club/threads/random-reboot-on-stock-rom.715058/
