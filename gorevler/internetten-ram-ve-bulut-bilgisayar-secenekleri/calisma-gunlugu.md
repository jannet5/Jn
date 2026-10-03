# Çalışma günlüğü

## 1. İstenen
Kullanıcı üç şey sordu: (1) İnternetten fiziksel RAM kiralayıp kendi bilgisayarına bağlamak mümkün mü? (2) Bilgisayara disk olarak bağlanan Google Drive neden C: ile aynı kapasitede ve dolu görünüyor? (3) Sanal PC kiralanırsa kendi bilgisayarından kullanılabilir mi? Ayrıca sonucun kısa bir özet olarak verilmesini istedi.
Kaynak metin ve görev dosyası **özel** olduğu için public depoya konmadı. Yalnızca bulut oturumunun özel scratchpad dizinine (`ozel/kaynak.txt`) kaydedildi.

## 2. Ortam ve araçlar
- Claude Code bulut oturumu, Linux konteyner. İstenen model: Opus 5.5 / medium. (Bu dosya modelin gerçekten seçildiğini kanıtlamaz.)
- Dal: `claude/wizardly-bohr-coy8w8`. Yalnızca `gorevler/internetten-ram-ve-bulut-bilgisayar-secenekleri/` klasörü eklendi, mevcut projelere dokunulmadı.
- Araçlar: WebSearch, WebFetch, curl, PowerShell 7.4.6 (GitHub sürüm arşivinden scratchpad'e indirildi: `powershell-7.4.6-linux-x64.tar.gz`), git, zip, sha256sum.

## 3. Araştırma adımları
1. Google Drive sanal sürücü kapasitesi → Google Yardım 10838124 (alan göstergesi yerel diski gösterir), 13401938 (akış/yansıtma), 17196458, 13470231 (önbellek dizini). Topluluk: Cryptomator/Dokany, elevenforum.
2. RAM kiralama → Microsoft page file belgesi (commit limit = RAM + sayfa dosyaları), arXiv 2305.03943 (bellek ayrıştırma), ram-dl, "download more RAM" şakası, elevenforum (sayfa dosyası NAS'a konamıyor).
3. Bulut PC → Microsoft Windows 365 Business fiyat sayfası ($36 / $56 / $108.80), Windows App belgesi (platformlar, tarayıcı, kişisel MSA ile oturum açılamaz), Shadow teklifler sayfası ve SSS (ülke listesinde Türkiye yok, 15 Mbit/s), AWS WorkSpaces fiyatları, Azure B2s (üçüncü taraf).
4. Çelişki: Üçüncü taraf bir site Shadow planlarını "Neo Lite/Neo" adlarıyla veriyordu. Karar: yalnızca shadow.tech teklif sayfasındaki adlar ve fiyatlar kullanıldı.
5. Shadow `/pricing` sayfası 404 verdi, bunun yerine `/us/shadowpc/offers/` kullanıldı. Shadow destek SSS sayfası 403 verdi, bunun yerine `shadow.tech/faq/` kullanıldı.

## 4. Kararlar
- Teslim biçimi B yolu (harita.md): rapor + salt-okunur PowerShell teşhis betiği. Gerekçe: kabul ölçütleri "somut çıktı/kanıt" istiyor. Windows'ta PowerShell hazır geldiği için kurulum gerekmiyor. GUI ya da sunucu eklemek gereksiz olurdu.
- Betik hiçbir ayarı değiştirmiyor. Test edilebilmesi için `-GirdiJson` modu eklendi.
- Windows PowerShell 5.1 uyumluluğu: `$IsLinux` değişkeni 5.1'de yok ve StrictMode altında hata verir. Bu yüzden platform kontrolü `[Environment]::OSVersion.Platform` ile yapıldı. Dosyalar Türkçe karakterler bozulmasın diye UTF-8 BOM ile kaydedildi.
- Örnek JSON'larda dosya sistemi "(sanal)" olarak yazıldı. Drive'ın Windows'a hangi dosya sistemi adını bildirdiği doğrulanmadığı için bir değer uydurulmadı.

## 5. Sorunlar ve çözümler
- Konteynerde `pwsh` yoktu. Resmi GitHub sürümü indirilip açıldı.
- `kaynak-kontrol.sh` ilk sürümde curl hata verdiğinde kodu iki kez "000000" yazıyordu. `|| true` ile düzeltildi ve `--http1.1` eklendi.
- microsoft.com fiyat sayfası konteynerden curl ile zaman aşımına uğruyor (HTTP/2) ya da 403 dönüyor (HTTP/1.1). Sayfa içeriği aynı gün WebFetch ile okundu. Bu durum raporda belirtildi.

## 6. Doğrulamalar (gerçek çıktı)
### T1 — teşhis testleri (`pwsh -File araclar/testler/tani-test.ps1`)
```
GEÇTİ  G=C kapasite eşleşmesi yansıtma olarak raporlanır
GEÇTİ  Yansıtma metni C: diskini adlandırır
GEÇTİ  Drive bellek değil uyarısı var
GEÇTİ  Önbellek boyutu raporlanır
GEÇTİ  Fiziksel RAM 8.0 GB okunur
GEÇTİ  Sanal bellek = 8 GB + 4864 MB = 12.8 GB
GEÇTİ  Farklı boyutta yansıtma iddia edilmez
GEÇTİ  Farklı boyut DRIVE_FARKLI verir
GEÇTİ  Önbellek bilinmiyorsa uydurulmaz
GEÇTİ  Bol RAM varken düşük RAM uyarısı yok
GEÇTİ  Drive yoksa DRIVE_YOK
GEÇTİ  Boş RAM %6 iken RAM_DUSUK uyarısı
GEÇTİ  -DriveHarfi "c" küçük harf/iki noktasız kabul edilir
SONUÇ: tüm testler GEÇTİ
```
### T2 — sözdizimi: PowerShell ayrıştırıcısı `parse hatası: 0`
### T3 — Windows dışında canlı mod: "Canlı toplama yalnızca Windows'ta çalışır. Test için -GirdiJson kullanın." (çıkış kodu 1)
### Örnek çıktı (`-GirdiJson araclar/testler/ornekler/g-c-ayni.json`)
```
=== RAM ve Google Drive teşhisi (salt okunur) ===
[RAM_FIZIKSEL] Takılı fiziksel RAM: 8.0 GB. Bu sayı yalnızca anakarttaki RAM modülleriyle artar; internet, Google Drive veya bulut hesabı bu sayıyı değiştiremez.
[RAM_BOS] Şu an boş RAM: 1.5 GB (%19).
[SANAL_BELLEK] Windows 'sanal bellek' sınırı ≈ RAM + sayfa dosyası = 12.8 GB. Sayfa dosyası diskte durur ve RAM'den çok daha yavaştır; RAM'in yerini tutmaz.
[DRIVE_YANSITMA] G: (Google Drive) toplamı 237.9 GB, C: ile aynı. Bu sayı bulut kotanız DEĞİL; Drive for desktop önbelleğinin durduğu yerel diskin (C:) boyutunu ve boş alanını gösterir. '200.4 GB dolu' görünen kısım aslında C: sürücüsünün dolu kısmıdır, Drive'ınız boş olsa bile böyle görünür.
[DRIVE_BELLEK_DEGIL] Google Drive bir DEPOLAMA alanıdır (dosya saklar). RAM/bellek değildir; bilgisayarın RAM miktarını veya hızını artırmaz.
[DRIVE_ONBELLEK] Drive for desktop yerel önbelleği (%LOCALAPPDATA%\Google\DriveFS): 700 MB. Bu, C: üzerinde gerçekten yer kaplayan kısımdır.
```
### T4 — kaynak kontrolü (`araclar/kaynak-kontrol.sh`)
```
OK       200 resmi    https://learn.microsoft.com/en-us/troubleshoot/windows-client/performance/introduction-to-the-page-file
OK       200 resmi    https://support.google.com/drive/answer/10838124
OK       200 resmi    https://support.google.com/drive/answer/13401938
OK       200 resmi    https://support.google.com/drive/answer/17196458
OK       200 resmi    https://support.google.com/drive/answer/13470231
KONTROL  000 resmi    https://www.microsoft.com/en-us/windows-365/business/compare-plans-pricing
OK       200 resmi    https://learn.microsoft.com/en-us/windows-app/overview
ENGEL    403 resmi    https://blogs.windows.com/windowsexperience/2026/04/22/windows-365-link-one-year-of-the-simple-secure-purpose-built-cloud-pc-device/
OK       200 resmi    https://shadow.tech/us/shadowpc/offers/
OK       200 resmi    https://shadow.tech/faq/
OK       200 resmi    https://aws.amazon.com/workspaces-family/workspaces/pricing/
OK       200 resmi    https://remotedesktop.google.com/
OK       200 akademik https://arxiv.org/pdf/2305.03943
OK       200 topluluk https://community.cryptomator.org/t/dokany-shows-capacity-free-storage-of-c-drive/5106
ENGEL    403 topluluk https://www.elevenforum.com/t/google-drive-app-shows-incorrect-size.26856/
ENGEL    403 topluluk https://www.elevenforum.com/t/unable-to-set-virtual-memory-pagefile-on-another-disk.36988/
ENGEL    403 topluluk https://github.com/pojntfx/ram-dl
OK       200 inceleme https://www.techradar.com/reviews/shadow-remote-pc-review
OK       200 ucuncu   https://www.azurespeed.com/AzureVmPricing/Standard_B2s
KONTROL gereken: 1
```
ENGEL (403) = sunucu, bot isteklerini engelliyor; sayfa tarayıcıda açılıyor. İçerik WebSearch/WebFetch ile okundu. Tek KONTROL satırı microsoft.com'un zaman aşımı (yukarıdaki açıklamaya bakın).

### T5 — YAPILMADI
Gerçek bir Windows bilgisayarında canlı çalıştırma, Türkiye'den gecikme ölçümü, hesap açma ve ödeme bulut ortamında yapılamaz. Kullanıcının Windows'ta çalıştıracağı komut README'de yazılı.

## 7. Teslim
- Public: bu klasör `claude/wizardly-bohr-coy8w8` dalına push edildi (commit ve geri okuma sonucu aşağıda).
- Özel: scratchpad'de `teslim/` altında ZIP + SHA-256 (kaynak.txt dahil). Public depoya konmadı.
