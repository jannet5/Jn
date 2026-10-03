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

| Hizmet | Plan / fiyat (aylık) | Donanım | Kanıt |
|---|---|---|---|
| Windows 365 Business | $36.00 | 2 vCPU, 8 GB RAM, 128 GB | [Resmi] https://www.microsoft.com/en-us/windows-365/business/compare-plans-pricing |
| | $56.00 | 4 vCPU, 16 GB RAM, 128 GB | aynı |
| | $108.80 | 8 vCPU, 32 GB RAM, 256 GB | aynı (30 gün ücretsiz deneme; en fazla 300 kullanıcı) |
| Shadow PC Lite | $29.99 | ayda 120 saat, oturum başına 4 saat, 256 GB | [Resmi] https://shadow.tech/us/shadowpc/offers/ |
| Shadow PC | $37.99 | ayda 210 saat, oturum başına 6 saat, 512 GB | aynı |
| Shadow PC Pro | $54.99 | ayda 210 saat, Always On, 8 saat | aynı |
| Shadow Power Lite | $43.99 | 28 GB RAM / 20 GB VRAM, ayda 120 saat | aynı |
| Shadow Power Pro | $59.99 | 28 GB RAM / 20 GB VRAM, ayda 210 saat | aynı (taahhüt yok) |
| Amazon WorkSpaces Personal | Standard paketi $44 + RDS SAL lisansı $4.19/kullanıcı | 175 GB kök + 100 GB kullanıcı alanı | [Resmi] https://aws.amazon.com/workspaces-family/workspaces/pricing/ (bölgeye göre değişir; AlwaysOn aylık / AutoStop saatlik) |
| Azure sanal makine (B2s) | ≈ $0.0416/saat Linux (≈ $30/ay sürekli açık); Windows lisansla daha pahalı | 2 vCPU, 4 GB | [Üçüncü taraf] https://www.azurespeed.com/AzureVmPricing/Standard_B2s |
| Kendi diğer PC'n | Ücretsiz | o PC'nin donanımı | [Resmi] Chrome Uzak Masaüstü https://remotedesktop.google.com/ · Windows App "Remote PC" |

**Not:** Üçüncü taraf bir abonelik sitesi Shadow için "Neo Lite / Neo" gibi farklı plan adları listeliyordu. Bu raporda yalnızca Shadow'un kendi teklif sayfasındaki adlar kullanıldı.

[Topluluk/İnceleme] Kullanıcı deneyimleri genelde aynı şeyi söylüyor: ham indirme hızından çok **gecikme ve bağlantı istikrarı** önemli. Kablolu bağlantı öneriliyor. Rekabetçi ve hızlı oyunlarda gecikme hissedilebiliyor. https://www.techradar.com/reviews/shadow-remote-pc-review

### A/B/C seçimi (kullanıcı için)
- **A — Shadow PC Lite (önerilen ilk deneme):** Bireysel kullanıcı için en kolay seçenek; taahhüt yok, her cihazdan bağlanılır. Risk: Türkiye desteklenen bölge değil, gecikmeyi kendi bağlantında ölçmen gerekiyor.
- **B — Windows 365 Business:** Microsoft'un resmi bulut PC'si, 30 gün deneme var. Risk: iş (Entra) hesabı gerektirir, oyun ve GPU için uygun değil.
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

**Sonuç:** G: sürücüsündeki "dolu" kısım, C:'nin dolu kısmıdır. Gerçek Drive kotası https://one.google.com/storage adresinde görünür. Drive bir depolama alanıdır, RAM değildir.

**Kanıt çıktısı:** `araclar/ram-drive-tani.ps1`, G: ile başka bir yerel diskin toplam boyutu %1'den az farklıysa `DRIVE_YANSITMA` bulgusunu üretir ve her durumda `DRIVE_BELLEK_DEGIL` uyarısını yazar. Örnek veriyle çalıştırılmış çıktı `calisma-gunlugu.md` dosyasında.

## Sınırlar
- Windows'ta gerçek çalıştırma, Türkiye'den gecikme ölçümü ve hesap açma yapılmadı.
- Fiyatlar ABD sayfalarından alındı. Türkiye'de KDV ve kur farkı oluşur; TL karşılığı uydurulmadı.
- Microsoft fiyat sayfasına bulut ortamından `curl` ile erişim zaman aşımına uğradı. İçerik aynı gün WebFetch aracıyla okundu.
