# Drive depolaması ve AI dosya erişimi rehberi

Google Drive for desktop'ı (Windows'ta genelde `G:`) **depo** olarak kullanmak,
**geliştirme klasörü** olarak kullanmak ve **yapay zekaya dosya vermek** üç ayrı
konu. Bu rehber üçünü kaynaklı olarak ayırır. Araştırma tarihi: 2026-10-03.

- Kaynaklar ve hangi iddianın neyle doğrulandığı: [`kaynaklar.md`](kaynaklar.md)
- Hedef → bağımlılık → A/B/C yolları → kabul haritası: [`harita.md`](harita.md)
- Yerel denetim aracı: [`araclar/drive_denetim.py`](araclar/drive_denetim.py)
- Çalıştırılmış kabul kanıtları: [`kabul/`](kabul/)
- Ne yapıldığının sıralı kaydı: [`calisma-gunlugu.md`](calisma-gunlugu.md)

## Kısa cevap

| Soru | Cevap | Dayanak |
|---|---|---|
| G:'yi depo olarak kullanabilir miyim? | **Evet**, akış (stream) modunda dosyalar bulutta durur, SSD'de yalnız önbellek kalır. | Google: Stream & mirror |
| SSD hiç dolmaz mı? | **Dolar, ama sınırlı**: açılan, "çevrimdışı" işaretlenen ve **henüz yüklenmemiş** dosyalar önbellekte (`%LOCALAPPDATA%\Google\DriveFS`) yer kaplar. 200 GB'ı G:'ye kopyalarsan, yüklenene kadar o veri önce yerelde bekler. | Google Workspace: Advanced config |
| Projeyi (npm/Gradle/git) G:'de çalıştırayım mı? | **Hayır.** Drive for desktop'ta `node_modules` gibi klasörleri hariç tutma yok; Gradle kilit dosyaları ve git iç dosyaları senkronla çakışır. Projeyi `C:\dev`'de tut, Drive'a **bundle/ZIP** koy. | Google forumu, Gradle forumu, kabul/03 |
| Web AI'ya `G:\...` yazarsam açar mı? | **Hayır.** O yol senin PC'nin dosya sistemi. Web AI dosyayı ancak **yükleme** veya **Drive bağlayıcısı (OAuth)** ile görür. | Claude/ChatGPT/Gemini yardım sayfaları, kabul/02, kabul/05 |
| PC'mdeki AI aracı G:'yi açar mı? | **Genelde evet** (Cursor, Claude Code CLI/Desktop, Python, ComfyUI): Drive uygulaması açık + dosya indirilebilir durumdaysa. Büyük video/ZIP'te yavaşlayabilir. | Google: "Files can only be accessed when Drive for desktop is running", Adobe topluluğu |

## 1. G: sürücüsü ve SSD önbelleği farkı (kaynak satır 2, 14, 18)

**Akış (stream, varsayılan):** Dosyalar bulutta; G: bir sanal sürücü. Google:
"Files are primarily stored in the cloud, but will be made available offline when
accessed" ve "Files can only be accessed when Drive for desktop is running."
Yerel alan yalnız "when you work on files on your computer, or for recently and
frequently used files" kullanılır.

**Yansıtma (mirror):** "Mirroring keeps a full copy on your computer" — bu modda
SSD **dolar**. "SSD dolmaz" iddiası yalnız akış modu için doğru.

**Önbellek nerede, ne kadar?**
- Varsayılan: Windows `%LOCALAPPDATA%\Google\DriveFS`, macOS
  `~/Library/Application Support/Google/DriveFS`.
- `ContentCachePath` ile başka NTFS/APFS diske taşınabilir (kayıt defteri
  `HKCU\Software\Google\DriveFS`). macOS File Provider modunda harici diske taşınamaz.
- `ContentCacheMaxKbytes` (yalnız yönetici; boş alanın %20'si ile sınırlı) ve
  `MinFreeDiskSpaceKBytes` (boş alan bu eşiğin altına inince önbelleğe yazmayı durdurur).
- **Uyarı:** "Unsynced changes are stored in a local cache. These changes can be
  lost if the cache is cleared or corrupted." Önbelleği elle silme; yükleme
  bitmeden dosyanın tek kopyası oradadır.
- Sürücü harfi değiştirilebilir (`G:` sabit değil): ayarlar veya `DefaultMountPoint`.

**"5 TB" notu:** Kaynaktaki 5 TB, kullanıcının Google hesap planına bağlı bir
beyan; bu çalışmada hesap kotası doğrulanmadı (erişim yok, uydurulmadı).

**Pratik kural:**
- Arşiv, üretilmiş görsel/video, yedek → G: uygun.
- Üzerinde **çalışılan** büyük video, rastgele okunan büyük ZIP/veri seti →
  önce "Çevrimdışı kullanılabilir" yap veya yerel SSD'ye kopyala, iş bitince G:'ye
  geri at. (Adobe topluluğu: düzenleme sırasında medya yerel diskte olmalı.)

## 2. Gradle / npm / git gibi aktif çalışma dosyaları

Bu dosyalar **binlerce küçük dosya + sık yazma + kilit dosyası** demek:

| Klasör | Ne | G:'de sorun | Öneri |
|---|---|---|---|
| `node_modules` | npm bağımlılıkları (örnek projede 70 paket = **630 dosya**) | Her `npm install` yüzlerce dosyayı senkron kuyruğuna sokar; Drive'da hariç tutma yok | `C:\dev`'de tut; `package-lock.json` commit'le, `npm ci` ile yeniden üret |
| `.gradle` (proje) / `GRADLE_USER_HOME` | Gradle önbelleği; gerçek derlemede **4 `.lock` dosyası** oluştu | Gradle ekibi: paylaşılan/ağ diskteki GRADLE_USER_HOME desteklenmez; kilitler çakışır | Varsayılan `C:\Users\<ad>\.gradle` yerinde kalsın; proje yerelde |
| `build/`, `dist/`, `target/` | Yeniden üretilebilir çıktı | Gereksiz yükleme trafiği | Drive'a yalnız son ürünü (APK/ZIP) koy |
| `.git` | Depo iç verisi | Senkron aracı `.git/refs` içine dosya eklerse depo bozulabilir (topluluk raporu) | Uzak depoya push et veya `git bundle` dosyasını Drive'a koy |
| npm önbelleği | `%LocalAppData%\npm-cache` (örnek: 96 MB) | Zaten C:'de; G:'ye taşıma | Olduğu yerde bırak |

**Kanıtlanmış alternatif (kabul/03b):** 690 dosyalık proje → `git bundle` (12 KB,
tek dosya) + kaynak ZIP (12 KB). `git clone proje.bundle` + `npm ci` + `gradle build`
ile proje sıfırdan geri geldi (70 paket, gradle exit 0, `demo.jar`).
İlk denemede `npm ci` **başarısız** oldu çünkü `package-lock.json` commit'lenmemişti —
lock dosyasını commit'lemek zorunlu.

Drive for desktop'ta hariç tutma isteyenler için üçüncü parti araçlar (Insync
ignore kuralları, odrive, Better-Drive) var; bu rehber bunları **test etmedi**,
yalnız belgelerini kaynak olarak listeler.

## 3. Web AI'nın yerel yol erişimi ≠ bulut yükleme/bağlayıcı izni (kaynak satır 3, 10, 17)

Üç ayrı erişim türü var; karıştırılmamalı:

| Erişim türü | Nasıl çalışır | `G:\x.mp4` yazmak yeter mi? |
|---|---|---|
| **A. Yerel araç** (Cursor, Claude Code CLI/Desktop, Python, ComfyUI, yerel model) | Senin PC'nin dosya sistemini okur | **Evet**, Drive uygulaması açıksa ve dosya indirilebiliyorsa |
| **B. Web sohbete yükleme** (ChatGPT/Claude/Gemini/Grok) | Dosya tarayıcıdan servise kopyalanır; servisin limitleri geçerli (Gemini: istem başına 10 dosya, video ≤2 GB/toplam 5 dk, diğerleri ≤100 MB; kod klasörü ≤5.000 dosya/100 MB) | Hayır — dosyayı seçip yüklemek gerekir |
| **C. Drive bağlayıcısı (OAuth)** | Servis Drive API'ye **senin izinlerinle** bağlanır. Claude: "Claude mirrors your existing permissions"; Docs/Sheets/Slides/PDF/görsel/Office okur, yalnız metin çıkarır. ChatGPT: Google Drive uygulaması, Shared drives Library'de yok. Gemini: "Add from Drive" için Workspace bağlantısı + Keep Activity gerekir | Hayır — G: yolu değil, Drive'daki dosya adı/URL'si kullanılır |

**Paylaşım linki:** Kaynaktaki "paylaşım linki ver" önerisi **koşullu**: web
AI'nın o linki açabilmesi servisin bağlayıcısına/gezinme aracına ve linkin
"herkese açık" olmasına bağlı. Herkese açık link = herkese açık dosya; özel dosya
için B veya C yolunu kullan.

**Kanıt (kabul/02, kabul/05):** Bu çalışma bir bulut AI ortamında (Claude Code
cloud) yapıldı. Aynı ajan `G:\My Drive\isler\gorsel.png`, `G:\klasor\dosya.mp4`
ve bir `C:\Users\...` yolunu açamadı (`ls` çıkış kodu 2, sistemde DriveFS bağlama
noktası yok); ama Google Drive bağlayıcısıyla Drive API sorgusunu hatasız
çalıştırdı. Yani bulut tarafı Drive'a **yol ile değil, izin ile** ulaşır.

## Kendi PC'nde doğrulama (Windows — bu çalışmada YAPILMADI)

Bulut ortamında Windows ve Drive for desktop olmadığı için aşağıdakiler kullanıcı
tarafında çalıştırılmalı (Python 3.8+ yeterli, ek paket yok):

```powershell
cd <bu klasör>\araclar
python drive_denetim.py yol "G:\My Drive\isler\gorsel.png"   # etiket/dosya sistemi dahil sınıflandırma
python drive_denetim.py tara "G:\My Drive"                    # Drive içindeki node_modules/.gradle/.git
python drive_denetim.py onbellek                              # DriveFS önbelleği SSD'de ne kadar yer tutuyor
python drive_denetim.py olc "G:\My Drive\video.mp4"           # 1. ölçüm soğuk (indirme), 2. ölçüm önbellek
python drive_denetim.py olc "C:\temp\video.mp4"               # aynı dosya SSD'de — karşılaştır
python -m unittest -v test_drive_denetim                      # 13 birim testi
```

Araç yalnız okur; dosya silmez/taşımaz, ağa bağlanmaz. `--json` ile makine
okunur çıktı verir.

## Doğrulanmayanlar (açıkça)

- Windows/macOS'ta Drive for desktop üzerinde gerçek G: okuma hızı, önbellek boyutu
  ve birim etiketi/dosya sistemi adı ölçülmedi (ortamda yok).
- Kullanıcının 5 TB kotası, plan türü ve ChatGPT/Gemini hesap bağlantıları test edilmedi.
- OpenAI yardım sayfası WebFetch'e 403 döndü; ChatGPT satırları arama sonucu özetine
  ve sayfa başlığına dayanır (kaynaklar.md'de işaretli).
- Insync/odrive/Better-Drive denenmedi.
