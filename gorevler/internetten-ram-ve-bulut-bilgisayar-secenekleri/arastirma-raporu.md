# Araştırma raporu — Fiziksel RAM, Drive depolaması ve bulut bilgisayar

Erişim tarihi: 2026-10-03. Kanıt düzeyi etiketleri: **[Resmi]** üretici belgesi · **[Topluluk]** forum/kullanıcı deneyimi · **[Akademik]** makale · **[Üçüncü taraf]** bağımsız fiyat/inceleme sitesi. Fiyatlar USD, vergi hariç.

## Kabul ölçütü 1 — "İnternetten fiziksel RAM kiralama" beklentisi

**Bulgu:** Kişisel bir bilgisayara internet üzerinden RAM ekleyen ticari bir ürün yok. Bunun nedeni fiziksel.

| İddia | Kanıt |
|---|---|
| RAM'in değeri düşük gecikmesinden gelir. Ağ üzerinden erişim bu avantajı yok eder. | [Akademik] Bellek ayrıştırma araştırması; uzak bellek ancak veri merkezi içinde, RDMA/CXL gibi özel bağlantılarla ve ek gecikme pahasına kullanılır: https://arxiv.org/pdf/2305.03943 |
| Ağ üzerinden "RAM" deneyleri swap (disk benzeri) olarak çalışır, gerçek RAM gibi değil. | [Topluluk] `ram-dl` projesi ağ üzerinden bellek blok aygıtı/swap sağlar: https://github.com/pojntfx/ram-dl |
| Windows'ta "sanal bellek" = RAM + sayfa dosyası. Sayfa dosyası diskte durur ve RAM'in yerini tutmaz. | [Resmi] Microsoft: "The system commit memory limit is the sum of physical memory and all page files combined." https://learn.microsoft.com/en-us/troubleshoot/windows-client/performance/introduction-to-the-page-file |
| Sayfa dosyası ağ (NAS) veya çıkarılabilir sürücüye konamıyor. | [Topluluk] https://www.elevenforum.com/t/unable-to-set-virtual-memory-pagefile-on-another-disk.36988/ |

**Gerçek alternatifler (yakından uzağa):**
1. Fiziksel RAM modülü takmak. Kalıcı ve en ucuz çözüm, ama boş yuva ve anakart desteği gerekir.
2. Bellek tüketimini azaltmak (tarayıcı sekmeleri, açılışta başlayan uygulamalar).
3. Ağır işi bulut bilgisayarda çalıştırmak. Bu durumda RAM uzak makinede kalır, sana yalnızca ekran görüntüsü gelir (Kabul ölçütü 2).

**Kanıt çıktısı:** `araclar/ram-drive-tani.ps1` gerçek RAM miktarını ve RAM + sayfa dosyası toplamını raporlar. İnternet ya da Drive bu sayıyı değiştirmez.

## Kabul ölçütü 2 — Bulut bilgisayar: bağlantı ve ücret

**Kullanıcının sorusu:** "Kendi bilgisayarımdan kullanabilir miyim?" **Evet.** Bulut bilgisayarlar uzak masaüstü olarak çalışır. Kendi bilgisayarındaki istemci uygulaması ya da tarayıcı, uzaktaki makinenin ekranını gösterir.

- [Resmi] Windows App ile Windows 365'e Windows, macOS, iOS/iPadOS, Android/ChromeOS ve **web tarayıcısından** bağlanılır. Tarayıcıdan bağlanırken kurulum gerekmez. Oturum açmak için yönetici tarafından verilmiş bir iş/okul hesabı gerekir; **kişisel Microsoft hesabı (MSA) ile oturum açılamaz**. https://learn.microsoft.com/en-us/windows-app/overview
- [Resmi] Shadow: "Windows, Linux or Apple computers, smartphones, tablet, Android TV or any Browser". Kendi sayfasında. Desteklenen ülkeler: ABD, Kanada, Fransa, Almanya, Birleşik Krallık, İspanya, İtalya, İsviçre, Hollanda, Belçika, Lüksemburg, Avusturya, İsveç, Danimarka. **Türkiye listede yok.** Desteklenmeyen bölgelerden abone olunabiliyor, ancak "we cannot guarantee a perfect quality of experience outside our supported countries". En az 15 Mbit/s öneriliyor; kablolu bağlantı ya da 5 GHz Wi-Fi tavsiye ediliyor. https://shadow.tech/faq/

### Ücret karşılaştırması

Ortak koşullar: Fiyatlar 2026-10-03 tarihinde resmi ABD sayfalarından okundu. Hepsi USD ve vergi hariç; sayfalarda KDV ya da vergi oranı yazmıyor. Satın alma yapılmadı.

| Hizmet / paket | Aylık | Saat sınırı / oturum | Donanım | Ülke/sayfa | Kaynak |
|---|---|---|---|---|---|
| Shadow **Neo Lite** | $29.99 | 120 saat/ay, oturum başına 4 saat | 256 GB, 1440p | ABD sayfası | [Resmi] https://shadow.tech/us/shadowpc/offers/ |
| Shadow **Power Lite** | $43.99 | 120 saat/ay, oturum başına 4 saat | 28 GB RAM / 20 GB VRAM, 256 GB | aynı | aynı |
| Shadow **Neo** | $37.99 | sınırsız (adil kullanım), oturum başına 6 saat | 512 GB | aynı | aynı |
| Shadow **Power** | $54.99 | sınırsız (adil kullanım), oturum başına 6 saat | 28 GB RAM / 20 GB VRAM, 512 GB | aynı | aynı |
| Shadow **Neo Pro** (profesyonel) | $44.99 | sınırsız (adil kullanım), oturum başına 8 saat | 28 GB RAM / 20 GB VRAM*, 256 + 512 GB | aynı | aynı |
| Shadow **Power Pro** (profesyonel) | $59.99 | sınırsız (adil kullanım), oturum başına 8 saat | 28 GB RAM / 20 GB VRAM, 256 + 512 GB | aynı | aynı (tüm Shadow paketleri taahhütsüz) |
| Windows 365 Business 2 vCPU/8 GB/128 GB | $36.00 /kullanıcı | — | | ABD sayfası | [Resmi] https://www.microsoft.com/en-us/windows-365/business/compare-plans-pricing |
| Windows 365 Business 4 vCPU/16 GB/128 GB | $56.00 /kullanıcı | — | | aynı | aynı |
| Windows 365 Business 8 vCPU/32 GB/256 GB | $108.80 /kullanıcı | — | | aynı | aynı |
| Amazon WorkSpaces Personal, Standard | $44 + RDS SAL $4.19/kullanıcı | AlwaysOn aylık; AutoStop saatlik | 175 GB kök + 100 GB kullanıcı alanı | bölgeye göre | [Resmi] https://aws.amazon.com/workspaces-family/workspaces/pricing/ |
| Azure sanal makine B2s | ≈ $0.0416/saat (Linux) | saat başı | 2 vCPU, 4 GB | bölgeye göre | [Üçüncü taraf] https://www.azurespeed.com/AzureVmPricing/Standard_B2s |
| Kendi diğer PC'n | ücretsiz | — | o PC'nin donanımı | — | [Resmi] https://remotedesktop.google.com/ |

\* Neo Pro için 28 GB RAM değeri, sayfanın aynı gün WebFetch ile okunan özetinden alındı.

**Windows 365 deneme ve fatura şartları** (aynı resmi sayfadan, alıntı): Paketler "Monthly subscription—auto renews", yani aylık ve otomatik yenileniyor. Deneme yalnızca 2vCPU/8GB/128GB paketi için ve "free for up to 30 days". Şartlar: **"A credit card is required"**, **"After your free trial, you will be charged the applicable subscription fee"**, "Cancel any time to stop future charges", "Only one Trial Period per version". Kısacası kart bilgisi veriliyor, iptal edilmezse deneme bitince ücret otomatik çekiliyor.

**Düzeltme kaydı:** Bu raporun ilk sürümünde (commit b73c85b) Shadow paketleri "Shadow PC Lite / Shadow PC / Shadow PC Pro" adlarıyla, 210 saat sınırıyla ve "Pro $54.99" olarak yazılmıştı. Bu bilgi, sayfanın hatalı özetlenmesinden geldi. 2026-10-03'te resmi teklif sayfası canlı olarak yeniden okundu. Doğru adlar ve fiyatlar şöyle: Neo Lite $29.99, Neo $37.99, Power Lite $43.99, Power $54.99, Neo Pro $44.99, Power Pro $59.99. Neo, Power ve Pro paketlerinde saat sınırı yok, adil kullanım politikası uygulanıyor. Üçüncü taraf bir sitenin verdiği Neo/Power adları da böylece doğrulanmış oldu.

[Topluluk/İnceleme] Kullanıcı deneyimleri genelde aynı şeyi söylüyor: ham indirme hızından çok **gecikme ve bağlantı istikrarı** önemli. Kablolu bağlantı öneriliyor. Rekabetçi ve hızlı oyunlarda gecikme hissedilebiliyor. https://www.techradar.com/reviews/shadow-remote-pc-review

### A/B/C seçimi (kullanıcı için)
- **A — Shadow Neo Lite (önerilen ilk deneme):** Taahhüt yok, her cihazdan bağlanılır. İki ayrı risk var: (1) **Ülke uygunluğu.** Türkiye resmi listede yok. Abone olmak "teknik olarak mümkün" ama kalite garanti edilmiyor, ve Türkiye'den hesap açma ya da ödeme kabulü denenmedi. (2) **Gecikme.** Ölçülmedi; ancak bir deneme aboneliğiyle görülebilir.
- **B — Windows 365 Business:** Microsoft'un resmi bulut PC'si. 30 günlük deneme kredi kartı istiyor ve sonunda otomatik ücretlendiriyor. Ayrıca iş (Entra) hesabı gerekiyor. Oyun ve GPU işleri için uygun değil.
- **C — Fiziksel RAM yükseltmesi ya da kendi diğer PC'ye uzak masaüstü:** Aylık ücret yok, gecikme sorunu yok. Risk: boş yuva veya ikinci bir PC gerekir.

## Kabul ölçütü 3 — Drive depolaması bellek değildir; "C: ile aynı ve dolu" görünmesi

| İddia | Kanıt |
|---|---|
| Drive for desktop'taki alan göstergesi Google kotasını değil, bilgisayardaki diskte kalan yeri gösterir. | [Resmi] "The space tracker in Drive for desktop shows the room left on your computer's hard drive, not your Google Account storage." https://support.google.com/drive/answer/10838124 |
| Akış (stream) modunda dosyalar sanal sürücüde durur ve az yer kaplar. Yansıtma (mirror) modunda tam kopya diske iner. | [Resmi] https://support.google.com/drive/answer/13401938 |
| Bilgisayar ve Google hesabı depolamayı farklı ölçer. Gmail, Fotoğraflar ve Çöp Kutusu kotayı kullanır ama yerel klasörde görünmez. | [Resmi] https://support.google.com/drive/answer/17196458 |
| Önbellek dizini değiştirilebilir: Ayarlar → Tercihler → Gelişmiş ayarlar → "Local cache files directory". | [Resmi] https://support.google.com/drive/answer/13470231 |
| Sanal dosya sistemi sürücüleri, verinin durduğu yerel diskin kapasitesini gösterir. | [Topluluk] Cryptomator/Dokany: "If your vault is located on drive X:, then you can only use the amount of free storage of X:" https://community.cryptomator.org/t/dokany-shows-capacity-free-storage-of-c-drive/5106 |
| Kullanıcılar G: sürücüsünün C: ile aynı ya da "yanlış" boyutta göründüğünü bildiriyor. | [Topluluk] https://www.elevenforum.com/t/google-drive-app-shows-incorrect-size.26856/ |

**Sonuç (genel, kaynaklı):** Drive for desktop'un gösterdiği yerel alan değeri Google hesabının kotası değildir; gerçek kota https://one.google.com/storage adresinden görülür. Drive bir depolama alanıdır, RAM değildir.

**Sonuç (kişisel teşhis için):** Kullanıcının Drive sürücüsünün C: ile ilişkisi **ölçülmedi**. "G:'deki dolu kısım C:'nin dolu kısmıdır" ifadesi kullanıcının bilgisayarı için yalnızca bir **olasılıktır**. Bu ilişki iki koşul birlikte sağlanırsa doğrulanmış sayılır: Drive'ın önbellek konumu kanıtla bilinmeli (Google'ın belgelediği `ContentCachePath` kayıt defteri değeri ya da uygulamanın Gelişmiş ayarlar ekranı) ve Drive sürücüsünün kapasitesi o diskin kapasitesiyle eşleşmeli.

[Resmi] Drive for desktop yapılandırmayı şu kayıt defteri anahtarlarından okur: `HKLM\Software\Google\DriveFS`, `HKCU\Software\Google\DriveFS`, ve öncelikli olarak `HKLM\Software\Policies\Google\DriveFS`. `ContentCachePath` için varsayılan `%LOCALAPPDATA%\Google\DriveFS`, `DefaultMountPoint` ise sürücü harfini ya da yolu belirtir. https://knowledge.workspace.google.com/admin/drive/advanced-drive-for-desktop-configuration . Kullanıcı ayarı uygulama ekranından değiştirdiyse kayıt defterinde değer bulunmayabilir. Araç bu durumda "bilinmiyor/aday" der ve varsayılan konumu kanıt saymaz.

**Kanıt çıktısı:** `araclar/ram-drive-tani.ps1` dört karardan birini verir:
- `DOGRULANDI`: kayıt defteri kanıtı var ve kapasite eşleşiyor.
- `ESLESMIYOR`: kanıt var ama kapasite eşleşmiyor.
- `ADAY`: kanıt yok, yalnız kapasite benzerliği var; araç ilişkiyi "bilinmiyor" diye yazar.
- `BILINMIYOR`: ne kanıt ne de benzer kapasite var.

Bu kararlar yanlış pozitif ve eşit kapasiteli bağımsız disk senaryolarıyla test edildi (`calisma-gunlugu.md`).

## Sınırlar
- Gerçek bir Windows / Drive for desktop kurulumunda çalıştırma, hesap açma, ödeme ve Türkiye'den gecikme ölçümü **yapılmadı**. Bunlar yapılmış sayılmamalıdır.
- Fiyatlar ABD sayfalarından alındı. Türkiye'de KDV ve kur farkı oluşur; TL karşılığı uydurulmadı.
- Microsoft fiyat sayfasına bulut ortamından `curl` ile erişim zaman aşımına uğradı. İçerik aynı gün WebFetch aracıyla okundu.
