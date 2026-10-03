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

## 6. Doğrulamalar (ilk sürüm, commit b73c85b — GEÇERSİZ KILINDI)
İlk sürümde 13 test vardı ve hepsi geçmişti. Ancak bağımsız inceleme (bkz. bölüm 8) o sürümün iki diskin kapasitesi benzer diye ilişkiyi kesin saydığını gösterdi; bu bir yanlış pozitifti. O testler ve o örnek çıktı artık kanıt sayılmaz. Güncel doğrulamalar bölüm 8'dedir.

## 7. Teslim
- Public: bu klasör `claude/wizardly-bohr-coy8w8` dalına push edildi (commit ve geri okuma sonucu aşağıda).
- Özel (ilk tur): scratchpad'de ZIP + SHA-256. İlk turun ZIP'i kişisel kaynak dosyasını ve bir git bundle'ı da içeriyordu; ikinci turda ikisi de çıkarıldı (bkz. bölüm 8).

## 8. Bağımsız inceleme düzeltmeleri (2026-10-03, ikinci tur)
**İstenen:** İnceleme ilk sürümde (uzak commit `b73c85b`) şu sorunları buldu:
- Betik, iki diskin kapasitesi %1 içinde yakın diye önbelleğin o diskte olduğunu **kesin** sayıyordu (yanlış pozitif).
- Önbellek klasörü sınırsız ve özyinelemeli taranıyordu. `SilentlyContinue` erişim hatalarını gizliyordu, bu yüzden kısmi ölçüm tam gibi görünüyordu.
- "Salt okunur" ifadesi, betiğin JSON dosyası yazmasıyla çelişiyordu.
- Shadow paket adları, fiyatları ve saat sınırları yanlıştı.
- Windows 365 deneme şartları (kart, otomatik ücret) yazılmamıştı.
- Kişisel teşhis kesin bir dille anlatılıyordu; Türkiye'de abone olabilmek ile gecikme konuları birbirine karışmıştı.

**Yapılanlar:**
1. **Canlı yeniden doğrulama (WebFetch, 2026-10-03):**
   - Shadow: https://shadow.tech/us/shadowpc/offers/ → Neo Lite $29.99 (120 saat/ay, oturum başına 4 saat), Neo $37.99, Power Lite $43.99 (120 saat), Power $54.99, Neo Pro $44.99, Power Pro $59.99. Lite dışındaki paketler sınırsız (adil kullanım), hepsi taahhütsüz. İlk sürümdeki "Shadow PC Lite / Pro $54.99 / 210 saat" bilgisi yanlış özetten gelmişti ve düzeltildi.
   - Windows 365: https://www.microsoft.com/en-us/windows-365/business/compare-plans-pricing → $36 / $56 / $108.80, "Monthly subscription—auto renews". Deneme şartları: "A credit card is required", "After your free trial, you will be charged the applicable subscription fee".
   - DriveFS yapılandırması: https://knowledge.workspace.google.com/admin/drive/advanced-drive-for-desktop-configuration → kayıt defterinde `HKLM/HKCU\Software\Google\DriveFS` ve `HKLM\Software\Policies\Google\DriveFS` anahtarları; `ContentCachePath` (varsayılan `%LOCALAPPDATA%\Google\DriveFS`) ve `DefaultMountPoint` değerleri.
2. **Betik yeniden yazıldı (`araclar/ram-drive-tani.ps1`):**
   - Kayıt defteri yalnızca okunuyor.
   - Drive ile yerel disk ilişkisi dört karardan biriyle raporlanıyor: `DOGRULANDI` (kanıt + kapasite) / `ESLESMIYOR` / `ADAY` (yalnız kapasite benzerliği, "BİLİNMİYOR" diye yazılır) / `BILINMIYOR`. Varsayılan önbellek konumu kanıt sayılmıyor, çünkü ayar uygulama ekranından değiştirilmiş olabilir.
   - `Measure-KlasorSinirli`: tarama öğe sınırı (200000) ve süre sınırı (20 sn) ile yapılıyor; klasörler tembel (lazy) numaralandırılıyor, junction ve symlink'ler izlenmiyor. Her erişim hatası sayılıyor ve örnekleri kaydediliyor. Sonuç "Tam", "Kismi" ya da "Yok" olarak yazılıyor; kısmi sonuçta "EN AZ" ifadesi kullanılıyor.
   - `-CiktiJson` tek yazma işlemi. Aynı adda dosya varsa `-UzerineYaz` verilmedikçe hata veriyor ve dosyayı ezmiyor. Belgede "ayar değiştirmez, dosya silmez; tek yazma -CiktiJson" diye açıkça yazıyor.
3. **Testler (`araclar/testler/tani-test.ps1`, 34 kontrol):** 7 örnek JSON (yanlış pozitif, eşit kapasiteli bağımsız diskler kanıtlı ve kanıtsız, G=C kanıtlı ve kanıtsız, eşleşme yok, Drive yok), enjekte listeleyiciyle erişim hatası / bağlantı atlama / öğe sınırı / süre sınırı / tam sınır, gerçek geçici klasörde sınırda kesilme, çıktı dosyasının ezilmemesi.
   - Root kullanıcı izinleri yok saydığı için gerçek erişim hatası testi `runuser -u nobody` ile, `chmod 000` yapılmış bir klasörde koşuldu. Bunun için pwsh'nin geçici bir kopyası `/home/user/.tani-gecici` dizinine kondu; test bitince silindi.
   - Mutasyon kontrolü: eski davranış ("aday = doğrulandı") betiğe geri konunca 3 test KALDI, erişim hatasını yok sayan mutasyonda 1 test KALDI. Yani testler bu hataları gerçekten yakalıyor.
4. README, rapor ve harita düzeltildi: kişisel teşhis "olasılık" diye yazıldı; fiyat tablosu tam paket adı, saat sınırı, ülke/sayfa, vergi hariç bilgisi, tarih ve kaynakla verildi; Türkiye'de "ülke uygunluğu" ile "gecikme" ayrıldı.

### Güncel çıktılar
T1 (root):
```
--- Drive/önbellek ilişkisi ---
GEÇTİ  Kanıt (ContentCachePath=C:) + G=C → DOGRULANDI
GEÇTİ  Doğrulanan metin kanıt kaynağını (kayıt defteri) yazar
GEÇTİ  Tam ölçüm TAM olarak raporlanır
GEÇTİ  Kanıt yok + G=C → yalnız ADAY (doğrulandı DEĞİL)
GEÇTİ  Aday metni BİLİNMİYOR der
GEÇTİ  Kısmi ölçüm KISMİ + EN AZ + nedenler
GEÇTİ  Kısmi ölçüm TAM gibi gösterilmez
GEÇTİ  YANLIŞ POZİTİF: G=D kapasite ama önbellek kanıtla C → DOGRULANDI yok
GEÇTİ  YANLIŞ POZİTİF: ESLESMIYOR + bilinmiyor
GEÇTİ  EŞİT DİSKLER: Drive harfi kayıt defteri DefaultMountPoint ile bulunur (etiket boş)
GEÇTİ  EŞİT DİSKLER: kanıt D: → D: doğrulanır, C: yalnız not
GEÇTİ  EŞİT DİSKLER: ilişki nesnesi Disk=D:, adaylar C: ve D:
GEÇTİ  EŞİT DİSKLER kanıtsız: iki aday listelenir, hiçbiri seçilmez
GEÇTİ  Ölçüm verisi yoksa önbellek bulgusu uydurulmaz
GEÇTİ  Eşleşme ve kanıt yok → BILINMIYOR
GEÇTİ  Klasör yoksa ONBELLEK_YOK
GEÇTİ  Her Drive bulgusunda kota ve bellek-değil uyarısı
GEÇTİ  Drive yoksa DRIVE_YOK
GEÇTİ  Boş RAM %6 → RAM_DUSUK
GEÇTİ  -DriveHarfi "c" (küçük, iki noktasız) kabul edilir
GEÇTİ  RAM 8.0 GB, sanal bellek 12.8 GB
--- Sınırlı tarama (enjekte listeleyici) ---
GEÇTİ  ERİŞİM HATASI: sayılır, Durum=Kismi, neden erisim_hatasi
GEÇTİ  ERİŞİM HATASI: örnekte klasör ve istisna türü var
GEÇTİ  Erişilebilen kısım yine sayılır (175 bayt, 3 dosya)
GEÇTİ  Bağlantı (reparse) izlenmez, atlanan sayılır
GEÇTİ  SINIRDA KESİLEN: MaxOge=2 → Kismi + oge_siniri, OgeSayisi=2
GEÇTİ  SÜRE SINIRI: MaxSaniye=0 → Kismi + zaman_siniri, hiç öğe sayılmaz
GEÇTİ  Tam olarak MaxOge kadar öğe varsa kesilmez → Tam
GEÇTİ  Olmayan klasör → Durum=Yok
--- Gerçek dosya sistemi ---
GEÇTİ  Gerçek klasör: 30 dosya, 30000 bayt, Tam
GEÇTİ  Gerçek klasör sınırda kesilir: MaxOge=10 → Kismi, alt sınır < 30000
--- Çıktı dosyası güvenliği ---
GEÇTİ  -CiktiJson yeni dosya oluşturur ve geçerli JSON
GEÇTİ  Var olan dosyayı -UzerineYaz olmadan EZMEZ
GEÇTİ  -UzerineYaz ile yazar
SONUÇ: 34/34 test GEÇTİ
```
T1b (nobody, gerçek erişim hatası): `GEÇTİ  Gerçek erişim hatası (root olmayan kullanıcı, chmod 000) → Kismi + erisim_hatasi` · `SONUÇ: 35/35 test GEÇTİ`
T2: `parse hatası: 0`

Örnek: kanıt yok ve G=C (yalnız aday):
```
[DRIVE_SURUCU] Drive sürücüsü: G:, toplam 237.9 GB, boş 37.5 GB. Tespit kaynağı: birim etiketi "Google Drive" (sezgisel).
[DRIVE_YANSITMA_ADAY] G: kapasitesi şu disk(ler)le neredeyse aynı: C:. Bu yalnız bir ADAY ilişkidir; önbelleğin nerede olduğuna dair kayıt defteri kanıtı yok (ayar uygulama içinden değiştirilmiş olabilir). Hangi diski yansıttığı BİLİNMİYOR. Drive → Ayarlar → Tercihler → Gelişmiş ayarlar → 'Yerel önbellek dosyaları dizini' satırına bakarak doğrulayın.
[DRIVE_KOTA] Gerçek Google depolama kotası Windows sürücü kapasitesinden okunamaz: https://one.google.com/storage adresinden bakın.
[DRIVE_BELLEK_DEGIL] Google Drive bir DEPOLAMA alanıdır (dosya saklar). RAM/bellek değildir; bilgisayarın RAM miktarını veya hızını artırmaz.
[ONBELLEK_KISMI] Önbellek (C:\Users\ornek\AppData\Local\Google\DriveFS): EN AZ 500 MB (200000 dosya sayıldı). Tarama KISMİ: öğe sınırı (200000), 3 klasöre erişilemedi. Gerçek boyut daha büyük olabilir.
```
Örnek: yanlış pozitif senaryosu (G kapasitesi D ile aynı, ama önbellek kanıtla C'de):
```
[DRIVE_ESLESMIYOR] Önbellek konumu kanıtla C: üzerinde (HKCU:\Software\Google\DriveFS\ContentCachePath) ama G: kapasitesi C: ile eşleşmiyor. Kapasitenin neyi gösterdiği bu veriyle BİLİNMİYOR. Kapasitesi benzeyen ama kanıtsız aday: D:.
```
T4 kaynak kontrolü:
```
OK       200 resmi    https://learn.microsoft.com/en-us/troubleshoot/windows-client/performance/introduction-to-the-page-file
OK       200 resmi    https://support.google.com/drive/answer/10838124
OK       200 resmi    https://support.google.com/drive/answer/13401938
OK       200 resmi    https://support.google.com/drive/answer/17196458
OK       200 resmi    https://support.google.com/drive/answer/13470231
OK       200 resmi    https://knowledge.workspace.google.com/admin/drive/advanced-drive-for-desktop-configuration
ENGEL    403 resmi    https://www.microsoft.com/en-us/windows-365/business/compare-plans-pricing
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
KONTROL gereken: 0
```
microsoft.com bu turda 403 döndü (bot engeli). Sayfa içeriği aynı gün WebFetch ile okundu.

### Yapılmayanlar (yapılmış sayılmaz)
- Gerçek bir fiziksel Windows bilgisayarda, gerçek Drive for desktop kurulumuyla ve kullanıcının bilgisayarında çalıştırma yapılmadı.
- Google, Shadow ya da Microsoft hesabı açılmadı, ödeme yapılmadı.
- Türkiye'den gecikme ölçülmedi. Fiyatların TL karşılığı ve KDV hesaplanmadı.

### Teslim (ikinci tur)
- **Public depo:** Yalnızca bu klasör, `claude/wizardly-bohr-coy8w8` dalına push edildi.
- **ZIP:** Uzak commit'ten `git archive` ile yalnızca bu klasör alınarak üretildi. İçinde kişisel kaynak dosyası ve `.git` geçmişi yok. Tam SHA-256 değeri ve uzak klonla birebir karşılaştırma sonucu, teslim mesajında ve özel scratchpad'deki `SHA256SUMS.txt` dosyasında.
- **Kişisel kaynak:** `kaynak.txt` yalnızca özel scratchpad'de duruyor; depoya ve ZIP'e girmedi.
