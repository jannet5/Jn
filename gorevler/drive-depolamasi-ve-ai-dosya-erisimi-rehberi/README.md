# Drive depolaması ve AI dosya erişimi rehberi

Google Drive for desktop'ı (Windows'ta genelde `G:`) **depo** olarak kullanmak,
**geliştirme klasörü** olarak kullanmak ve **yapay zekaya dosya vermek** üç ayrı
konu. Bu rehber üçünü kaynaklı olarak ayırır. Araştırma tarihi: 2026-10-03.

- Kaynaklar ve hangi iddianın neyle doğrulandığı: [`kaynaklar.md`](kaynaklar.md)
- Hedef → bağımlılık → A/B/C yolları → kabul haritası: [`harita.md`](harita.md)
- Yerel denetim aracı: [`araclar/drive_denetim.py`](araclar/drive_denetim.py)
- Çalıştırılmış kabul kayıtları ve kapsamları: [`kabul/`](kabul/)
- Ne yapıldığının sıralı kaydı: [`calisma-gunlugu.md`](calisma-gunlugu.md)

## Kısa cevap

| Soru | Cevap | Dayanak |
|---|---|---|
| G:'yi depo olarak kullanabilir miyim? | **Evet.** Akış (stream) modunda dosyalar bulutta tutulur, bilgisayarda çoğunlukla önbellek kalır. | R1, R12 |
| SSD'de yer kaplar mı? | **Evet, kaplayabilir; garanti yok.** Akış modunda da açılan, "çevrimdışı" işaretlenen, son/sık kullanılan ve **henüz yüklenmemiş** dosyalar `%LOCALAPPDATA%\Google\DriveFS` önbelleğinde yer tutar. Büyük bir kopyalama yüklenene kadar yerel diskte bekler. Yansıtma (mirror) modunda tam kopya diskte durur. | R1, R3, R12 |
| Yerel alan = Google kotası mı? | **Hayır.** Google: "Your computer space and Google storage are not the same." Drive for desktop'taki alan göstergesi bilgisayar diskini gösterir, hesap kotasını değil. | R12 |
| Projeyi (npm/Gradle/git) G:'de çalıştırayım mı? | **Önerilmez.** Binlerce küçük dosya ve kilit dosyası senkronla çakışabilir. Resmi ayar sayfasında alt klasör/desen hariç tutma ayarı **bulunamadı** (kesin "yok" hükmü değil, aşağıya bak). Projeyi yerel diskte tut, Drive'a **bundle/ZIP** koy. | R4, T1, T2, kabul/03, 03b |
| Web AI'ya `G:\...` yazarsam açar mı? | **Yalnız yol metniyle hayır.** Yol senin bilgisayarının dosya sisteminde anlamlıdır. Web AI dosyaya yükleme, Drive bağlayıcısı (OAuth) veya PC'nde çalışan bir köprü/ajan üzerinden ulaşır. | R5, R6, R7, R9 |
| PC'mdeki AI aracı G:'yi açar mı? | **Çoğu zaman evet**, koşullarla: Drive for desktop çalışıyor, aracın izin/sandbox ayarı klasörü kapsıyor, dosya indirilebiliyor. Büyük video/ZIP'te yavaşlayabilir. Docs/Sheets/Slides dosyaları tarayıcıda açılır. | R1, R12, T4 |

## 1. G: sürücüsü ve SSD önbelleği farkı (kaynak satır 2, 14, 18)

**Akış (stream, varsayılan):** Google'a göre "Files are primarily stored in the
cloud, but will be made available offline when accessed" ve "Files can only be
accessed when Drive for desktop is running." Yerel alan "when you work on files on
your computer, or for recently and frequently used files" kullanılır. Ayrı bir
sayfada da "Streaming a file uses almost no computer space" deniyor (R12). Ancak
"neredeyse hiç" ≠ "hiç": aşağıdaki durumlar akış modunda da diski doldurabilir.

**Akış modunda yerel disk ne zaman dolar:**
- Çevrimdışı işaretlenen dosya ve klasörler.
- Son açılan ve sık kullanılan dosyalar.
- G:'ye kopyalanıp **henüz yüklenmemiş** dosyalar. Google: "files are moved here
  before they're uploaded" (R3). 200 GB kopyalarsan, yükleme bitene kadar bu veri
  yerelde bekler.
- Senkronize edilen paylaşılan dosyalar: "Shared files can fill up your hard drive
  if you sync them, but they never count against your Google space" (R12).

**Yansıtma (mirror):** "Mirroring keeps a full copy on your computer" (R1).

**Önbellek ayarları (R3):**
- Varsayılan konum: Windows `%LOCALAPPDATA%\Google\DriveFS`, macOS
  `~/Library/Application Support/Google/DriveFS`.
- `ContentCachePath` önbelleği başka bir NTFS/APFS diske taşır. macOS File Provider
  modunda harici diske taşınamaz (R2).
- `ContentCacheMaxKbytes` yalnız yöneticiye açık bir sınırdır (boş alanın %20'si).
  `MinFreeDiskSpaceKBytes`, boş alan bu eşiğin altına inince önbelleğe yazmayı durdurur.
- **Uyarı:** "Unsynced changes are stored in a local cache. These changes can be lost
  if the cache is cleared or corrupted" (R2). Önbelleği elle silme.
- Sürücü harfi sabit değildir (`G:` yalnız varsayılan); `DefaultMountPoint` ile değişir.

**Kota:** Yerel disk alanı ile Google hesap kotası ayrı şeylerdir (R12). Kaynaktaki
"5 TB" kullanıcının plan beyanıdır ve doğrulanmadı.

**Pratik kural:**
- Arşiv, üretilmiş görsel/video ve yedek için G: uygundur.
- Üzerinde **çalışılan** büyük video veya rastgele okunan veri setini önce
  "Çevrimdışı kullanılabilir" yap ya da yerel SSD'ye kopyala, iş bitince geri at
  (Adobe topluluk deneyimi, T4).

## 2. Gradle / npm / git gibi aktif çalışma dosyaları

Bu klasörler **çok sayıda küçük dosya, sık yazma ve kilit dosyası** demektir.
Ölçümler kabul/03'teki gerçek Linux projesinden alındı. Proje G: üzerinde
çalıştırılmadı.

| Klasör | Ne | G:'de olası sorun | Öneri |
|---|---|---|---|
| `node_modules` | npm bağımlılıkları (örnekte 70 paket, **630 dosya**) | Her kurulum yüzlerce dosyayı senkron kuyruğuna sokabilir | Yerel diskte tut; `package-lock.json`'u commit'le, `npm ci` ile yeniden üret |
| `.gradle` / `GRADLE_USER_HOME` | Gradle önbelleği (örnek derlemede **4 `.lock`** dosyası) | Kilit dosyaları senkron/ağ diskinde çakışabilir. Gradle forumundaki uzman yanıtı (T2) bunu önermiyor; bu bir **resmi "unsupported" belgesi değil** | Varsayılan `C:\Users\<ad>\.gradle` yerinde kalsın; proje yerel diskte olsun |
| `build/`, `dist/`, `target/` | Yeniden üretilebilir çıktı | Gereksiz yükleme trafiği | Drive'a yalnız son ürünü (APK/ZIP) koy |
| `.git` | Depo iç verisi (örnekte 38 dosya) | Bir senkron aracı `.git/refs` içine dosya eklerse depo bozulabilir (topluluk raporu, T3) | Uzak depoya push et veya `git bundle` dosyasını Drive'a koy |
| npm önbelleği | `%LocalAppData%\npm-cache` (R11) | Zaten C:'de | Olduğu yerde bırak |

**Hariç tutma durumu: belirsiz.** Google'ın Drive for desktop ayar sayfasında
(R4, 2026-10-03'te okundu) alt klasör, dosya türü veya desen hariç tutma ayarı
geçmiyor. Google geliştirici forumunda bu özellik 2025'ten beri isteniyor ve
resmi yanıt görünmüyor (T1). Bu iki gözlem "özellik yok" hükmü için yeterli
değil: bir yönetici ayarı veya yeni bir sürüm olabilir. Kesin bilgi için kendi
sürümündeki **Ayarlar → Tercihler** ekranını kontrol et.

Üçüncü parti araçlar (Insync, odrive, Better-Drive) hariç tutma sunduğunu
belgeliyor; bu çalışmada test edilmediler.

**Çalıştırılmış alternatif (kabul/03b, Linux):** 690 dosyalık proje 12 KB'lık
bir `git bundle` ve 12 KB'lık bir kaynak ZIP'e indi. `git clone proje.bundle`,
`npm ci` (70 paket) ve `gradle build` (exit 0, `demo.jar`) ile proje sıfırdan geri
geldi. İlk denemede `npm ci` **başarısız** oldu, çünkü `package-lock.json`
commit'lenmemişti. Lock dosyası şarttır.

## 3. Yerel yol erişimi ≠ bulut yükleme / bağlayıcı izni (kaynak satır 3, 10, 17)

Bir AI'nın bir dosyayı görmesi şu **ayrı** koşullara bağlıdır:

| Koşul | Ne demek | Kim için geçerli |
|---|---|---|
| **Mount** | Dosya sistemi, AI'nın çalıştığı makineye bağlı mı? `G:` yalnız senin PC'nde vardır. | Yerel araçlar |
| **İzin / sandbox** | Aracın o klasörü okumasına izin var mı? Yerel ajanlar çoğunlukla çalışma klasörüyle sınırlıdır. | Yerel araçlar, masaüstü uygulamaları |
| **Yükleme** | Dosya sohbete kopyalandı mı? Servis limitleri geçerlidir. Gemini (R7): istem başına 10 dosya, video ≤2 GB ve toplam 5 dk, diğerleri ≤100 MB, kod klasörü ≤5.000 dosya / 100 MB. | Web sohbet |
| **Bağlayıcı (OAuth)** | Servis Drive API'ye **senin izinlerinle** bağlanır ve dosyayı adı/URL'siyle bulur, G: yoluyla değil. Claude (R5): "Claude mirrors your existing permissions"; Docs/Sheets/Slides/PDF/görsel/Office okur, yalnız metin çıkarır. ChatGPT (R6): Library'de Shared drives yok. Gemini (R7): Workspace bağlantısı ve Keep Activity gerekir. | Web sohbet, bulut ajanlar |
| **Köprü** | PC'nde çalışan ve buluta bağlanan bir uygulama veya ajan (ör. masaüstü uygulaması, yerel MCP dosya sunucusu) varsa, bulut tarafı dolaylı erişebilir. | Kurulumuna bağlı |
| **Herkese açık link** | Servis linki açabiliyorsa çalışır; herkese açık link dosyayı herkese açar. | Web sohbet (koşullu) |

**Kabul kayıtlarının kapsamı:**
- **kabul/02:** Bu Linux bulut konteynerinde üç Windows yolu yok ve DriveFS/SMB
  bağlaması görünmüyor. Bu, *bu* ortamın yol metninden dosya üretmediğini gösterir.
  Tüm web AI'lar veya yerel ajanlar için mutlak bir kanıt değildir.
- **kabul/05:** Bağlayıcı aracı boş bir sorguya hatasız `{}` döndü. Hesap, dosya
  okuma, hash ve kota **test edilmedi**.

## Kendi PC'nde doğrulama (Windows — bu çalışmada YAPILMADI)

Python 3.8+ yeterli, ek paket gerekmez. Araç yalnız okur.

`olc` notları:
- Ölçüm **soğuk okuma değildir**. OS ve DriveFS önbellekleri boşaltılmaz; ilk çalıştırma
  yalnız "ilk gözlenen okuma"dır ve veri önbellekte olabilir. Çıktıdaki `onbellek_durumu`
  her zaman "bilinmiyor" der.
- `--rastgele N` değeri 1 ile 100000 arasında bir tam sayı olmalıdır. 0, negatif, ondalık,
  metin veya üst sınırı aşan değerler kullanım mesajıyla reddedilir (çıkış kodu 2, traceback yok;
  bkz. kabul/07).

```powershell
cd <bu klasör>\araclar
python drive_denetim.py yol "G:\My Drive\isler\gorsel.png"   # etiket/dosya sistemi dahil sınıflandırma
python drive_denetim.py tara "G:\My Drive"                    # Drive içindeki node_modules/.gradle/.git
python drive_denetim.py onbellek                              # DriveFS önbelleği diskte ne kadar yer tutuyor
python drive_denetim.py olc "G:\My Drive\video.mp4"           # ilk gözlenen okuma (soğuk garantisi yok)
python drive_denetim.py olc "C:\temp\video.mp4"               # aynı dosya yerel diskte; karşılaştır
python -m unittest -v test_drive_denetim                      # 20 birim testi
```

## Yapılmayan kabuller ve nedenleri

| Kabul | Durum | Neden |
|---|---|---|
| Windows'ta G: birim etiketi, dosya sistemi, okuma hızı | Yapılmadı | Bulut ortamı Linux; Drive for desktop yalnız Windows/macOS'ta çalışır |
| DriveFS önbellek boyutu, akış modunda gerçek disk dolumu | Yapılmadı | Aynı neden; DriveFS yok |
| npm/Gradle projesinin gerçekten G: üzerinde çalıştırılması | Yapılmadı | Aynı neden; yalnız Linux'ta sınıflandırma yapıldı |
| Google hesap kotası (5 TB) | Yapılmadı | Hesap erişimi yok; kişisel hesap verisi okunmadı |
| Bağlayıcıyla gerçek dosya listeleme/okuma/hash, doğru hesap | Yapılmadı | Kişisel dosya okumak/paylaşmak yetki dışında tutuldu; kabul/05 yalnız boş sorgu |
| ChatGPT / Gemini / telefon hesap testleri | Yapılmadı | Hesap erişimi yok |
| Hariç tutma ayarının kesin yokluğu | Belirsiz | Yalnız resmi ayar sayfası ve forum okundu; uygulama arayüzü görülmedi |
| Insync / odrive / Better-Drive | Yapılmadı | Kapsam dışı; yalnız belgeleri listelendi |
| OpenAI yardım sayfası | Kısmi | WebFetch'e 403 döndü; arama özetine dayanıldı |
