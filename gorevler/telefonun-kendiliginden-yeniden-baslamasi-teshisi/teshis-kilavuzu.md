# Telefon kendi kendine kapanıp açılıyor — donanım mı, yazılım mı?

**Kısa cevap:** Telefonun kaydı olmadan "kesin şu" demek yanlış olur. Aşağıdaki
adımlar olasılıkları daraltır; hiçbiri tek başına kesin kanıt değildir.
Kendi yazdığın uygulamanın tetiklemesi **mümkün** (Android'de bir uygulama
`system_server`'ı çökertirse telefon "yumuşak" yeniden başlar — F-Droid'de
gerçek vakalar var), ama sıradan bir uygulama genelde yalnız kendisi çöker.
Donanım tarafında sık nedenler: yıpranmış/şişmiş pil, gevşek pil bağlantısı,
takılan güç tuşu. Bunların arasında sistem yazılımı/firmware/sürücü hataları da
vardır ve fabrika ayarı bunları değiştirmez.

## Adım 1 — Kayıtları al (10 dk, Windows'ta)
1. Platform-Tools'u indir, bir klasöre aç: https://developer.android.com/tools/releases/platform-tools
2. Telefonda *Geliştirici seçenekleri → USB hata ayıklama* açık olsun; bağla, telefonda izni onayla.
3. O klasörde PowerShell aç (Python 3.10+ gerekir):
   ```powershell
   .\adb devices -l          # telefonun satırında "device" yazmalı ("unauthorized" ise telefonda onayla)
   python reboot_teshis.py hepsi --cikti kayit1 --goster <kendi.paket.adın>
   ```
4. Çıktılar:
   - `kayit1\rapor-yerel.md` ve `kayit1\ozet-yerel.json` — **sadece senin için; kişisel veri
     içerir**: uygulama adları, kurulum/güncelleme zamanları, sürümler, maskelenmiş ama yine de
     kişisel metin taşıyabilen hata mesajları. Paylaşma; işin bitince sil.
   - `kayit1\paylasim.zip` (+ `.sha256`) — paylaşmak için. Hata mesajı, Watchdog açıklaması,
     stderr gibi **serbest metin hiç konmaz**; yalnız izin listesindeki hata sınıfları
     (ör. `java.lang.IllegalStateException`) ve kategoriler (ör. `handler-takılması:main`).
     Diğer uygulamalar `uyg-xxxxxxxx` takma adıyla. Paket geçici klasörde üretilip geri
     okunarak tanımlı örüntüler (e-posta, telefon/IMEI, IP/IPv6, MAC, URI, Android/Windows/ev
     yolları, JSON sırları, Bearer, seri, gerçek paket adı…) için denetlenir; bulgu olursa
     paket üretilmez ve önceki çalıştırmadan kalan paylaşım dosyaları da silinmiş olur.
     "Tanımlı örüntüler bulunmadı" sonucu kişisel veri olmadığının **garantisi değildir**;
     göndermeden önce `rapor-paylasim.md`'ye bir göz at.
   - Tam getprop/logcat/dropbox/paket listesi ham hâliyle diske yazılmaz (yukarıdaki yerel
     dosyalar ise yazılır). Ham kopya istersen `--ham-sakla` (yalnız yerel `yerel-ham\`; paylaşma).
5. **En iyi zaman:** telefon kendiliğinden yeniden başladıktan hemen sonra
   (kayıtlar halka tampondadır). Varsayılan pencere son 7 gün (`--gun`).

Araç telefonda hiçbir şeyi değiştirmez: root, yeniden başlatma, uygulama
kaldırma, ayar değiştirme yapmaz. Bir adb komutu izin hatası, zaman aşımı veya
hata koduyla biterse raporun "Veri tamlığı" bölümünde ayrı satır olarak görünür
— **eksik veri, o arızanın olmadığı anlamına gelmez.**

Python yoksa (Yol B, elle ve maskesiz — çıktıları paylaşmadan önce kişisel veri için gözden geçir):
```powershell
.\adb shell getprop sys.boot.reason
.\adb shell getprop ro.boot.bootreason
.\adb shell getprop sys.boot.reason.last
.\adb shell dumpsys dropbox | findstr /i "system_server SYSTEM_RESTART SYSTEM_BOOT LAST_KMSG TOMBSTONE"
.\adb shell dumpsys battery
```

## Adım 2 — Raporu yorumla

| `sys.boot.reason` / iz | Anlamı | Not |
|---|---|---|
| `reboot,userrequested`, `reboot,adb`, `reboot,shell`, `reboot,ota` | Sen/komut/güncelleme yeniden başlattı | Sorun izi değil |
| `system_server_crash/_watchdog`, `SYSTEM_RESTART`, ek `boot_progress_start` | Android çerçevesi yeniden başladı (yazılım katmanı) | Tetikleyici uygulama, sistem bileşeni, bellek/depolama baskısı veya donanım kaynaklı takılma olabilir |
| `reboot,rescueparty` | Android art arda çökme gördü | Son değişikliklere bak |
| `kernel_panic`, `watchdog`, `*wdt*`, `hw_reset` | Çekirdek/donanım watchdog | Çekirdek/sürücü/firmware **veya** donanım |
| `shutdown,battery`, `undervoltage`, `brownout`, `ocp`, `pmic` | Güç kesildi/gerilim düştü | Pil/güç devresi olası; uzun güç tuşu basışı da benzer iz bırakabilir |
| `shutdown,thermal…` | Isı koruması | Isınma kaynağı uygulama yükü de donanım da olabilir |
| `cold`, `hard`, `warm`, sadece `reboot` | Genel | Adım 3 şart |

**Paket adı görmek suçlu bulmak değildir.** Bir uygulamanın adının çökme
metninde geçmesi veya yeniden başlamadan kısa süre önce kurulmuş olması
nedensellik kanıtı değildir: o an ön planda olan ya da çökmeden etkilenen masum
bir uygulama da görünür. Rapor bu yüzden "hipotez" dili kullanır; ilk hata
zamanı, olay zamanı ve Güvenli Mod karşılaştırmasından biri eksikse kanıt
düzeyini "belirsiz hipotez" yazar.

## Adım 3 — Güvenli testler (olasılığı daraltan kısım)
1. **Güvenli Mod**: Güç tuşuna basılı tut → "Kapat"a uzun bas → Güvenli Mod (Pixel 6+: Güç+Ses Açma). Normalde kapandığı süre kadar, benzer kullanımla kullan. Sonucu araca ver: `--guvenli-mod kapanmadi` veya `--guvenli-mod kapandi`.
   - Güvenli Mod'da **kapanmadı** → üçüncü taraf uygulama katmanı **olası** (kesin değil: Güvenli Mod'da yük ve kullanım farklıdır; sorun seyrek ise yeterince uzun gözle). Normal moda dön, önce kendi APK'nı, sonra son yüklenenleri tek tek kaldırıp her birinden sonra gözle.
   - Güvenli Mod'da **da kapandı** → üçüncü taraf dışı neden **olası**: sistem yazılımı, firmware/sürücü veya donanım. 2–4'e geç.
2. Kendi uygulamandan şüpheleniyorsan, telefona bağlıyken izle (çıktıyı paylaşmadan önce gözden geçir):
   `adb logcat -b crash -b system -v time | findstr /i "FATAL system_server Watchdog <paket.adın>"`
3. Depolama %90'ın üzerindeyse yer aç (Google: %10'dan az boş alan sorun çıkarabilir); resmi sistem güncellemelerini kur (firmware/sürücü hatalarını güncellemeler düzeltebilir).
4. Donanım ipuçları: kılıfı çıkar (güç tuşuna baskı), şarjda vs. pilde, soğuk vs. sıcak davranışı karşılaştır. Arka kapakta kabarma/ekranda kalkma varsa **şarj etme, servise götür** (şişmiş pil tehlikelidir).
5. Son çare: yedek al → fabrika ayarı → uygulama kurmadan 1–2 gün kullan. Yine kapanıyorsa üçüncü taraf uygulamalar büyük ölçüde elenir; **geriye sistem yazılımı/firmware/sürücü ile donanım kalır** (fabrika ayarı firmware'i ve sürücüleri değiştirmez). Bunları ayırmak için yetkili servis: resmi firmware'in yeniden yüklenmesi ve donanım testi (pil ölçümü, kart testi).

## Olasılık özeti (kesin hüküm değildir)
- Güvenli Mod temiz + çerçeve yeniden başlama izleri → uygulama katmanı olası; eleme ile daralt.
- Güvenli Mod'da da sürüyor + `shutdown,battery`/kötü pil sağlığı/şarjda düzeliyor → pil/güç donanımı olası.
- Güvenli Mod'da da sürüyor + `kernel_panic/watchdog` → çekirdek/sürücü/firmware veya donanım; resmi güncelleme ve servis testi ayırır.
- Fabrika ayarı sonrası da sürüyor → uygulama dışı; firmware/sürücü/sistem ile donanım ayrımı servis testi ister.
