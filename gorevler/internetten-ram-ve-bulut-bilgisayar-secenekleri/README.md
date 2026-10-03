# SONUÇ — İnternetten RAM, Google Drive diski ve bulut bilgisayar

> Kısa özet. Hepsini okumak istemiyorsan yalnız bu sayfa yeter.
> Ayrıntı ve kaynaklar: [arastirma-raporu.md](arastirma-raporu.md) · Yol haritası: [harita.md](harita.md)
> Fiyatlar 2026-10-03'te resmi sayfalardan okundu, ABD doları, vergi hariç; değişebilir.

## 1) İnternetten RAM kiralayıp kendi bilgisayarına takmak mümkün mü?
**Hayır, ev bilgisayarı için böyle bir ürün yok.**
- RAM, işlemcinin nanosaniyeler içinde eriştiği, anakarta takılı bir çiptir. İnternet bağlantısı bunun binlerce kat yavaşıdır; uzaktaki "RAM", RAM gibi davranamaz.
- "Download more RAM" siteleri şakadır. GitHub'da ağ üzerinden swap yapan deneme projeleri var (ör. `ram-dl`), ama bunlar diskten bile yavaş kalır ve günlük kullanıma uygun değildir.
- Veri merkezlerinde "bellek havuzu" (CXL) teknolojisi var, ama bu aynı rafta, özel kablolarla birbirine bağlı sunucular için. İnternetten evine gelmez.

**Gerçekten işe yarayanlar:** (a) fiziksel RAM modülü almak (boş yuva var mı diye Görev Yöneticisi → Performans → Bellek → "Kullanılan yuvalar" kısmına bak), (b) açık programları ve sekmeleri azaltmak, (c) ağır işi **bulut bilgisayarda** yaptırmak (madde 3).

## 2) Google Drive neden C: diskinle aynı boyutta ve dolu görünüyor?
**Bu bir hata değil; o rakam Drive kotan değil, C: diskinin kendisi.**
- Google'ın kendi açıklaması: Drive for desktop'taki alan göstergesi *bilgisayarındaki diskte kalan yeri* gösterir, Google hesabındaki depolama alanını göstermez.
- Sanal **G:** sürücüsü, önbelleğin durduğu diskin (genelde C:) toplam ve boş alanını yansıtır. Yani "dolu" görünen kısım, **C:'nin dolu kısmıdır**. Drive'ın boş olsa bile öyle görünür.
- Asıl Drive kotan: https://one.google.com/storage
- Drive bir **depolama** alanıdır, yani dosya saklar. **RAM değildir** ve bilgisayarı hızlandırmaz. Windows sayfa dosyası (sanal bellek) de ağ ya da bulut sürücüsüne konamıyor (kullanıcı deneyimleri böyle). Yerel diske konsa bile RAM'den çok yavaştır.
- C: üzerinde gerçekten yer kaplayan kısım, Drive önbelleğidir (`%LOCALAPPDATA%\Google\DriveFS`). Önbelleğin yerini değiştirmek için: Drive menüsü → Ayarlar → Tercihler → Gelişmiş ayarlar → "Yerel önbellek dosyaları dizini".
- "Akış" (stream) modu bilgisayarda çok az yer kaplar. "Yansıtma" (mirror) modu ise her dosyanın tam kopyasını diske indirir.

**Kendi bilgisayarında kontrol et:** [`araclar/ram-drive-tani.ps1`](araclar/ram-drive-tani.ps1) sadece okuma yapar, hiçbir şeyi değiştirmez.
```
powershell -ExecutionPolicy Bypass -File .\ram-drive-tani.ps1
```
Çıktı, gerçek RAM miktarını ve G: sürücüsünün C:'yi mi yansıttığını Türkçe olarak yazar.

## 3) Sanal PC kiralarsam kendi bilgisayarımdan kullanabilir miyim?
**Evet. O bilgisayara gitmen gerekmez.** Kendi bilgisayarında bir uygulama ya da tarayıcı penceresi açarsın. Kiraladığın bilgisayarın ekranı oraya gelir; klavye ve fare oradaki makineyi yönetir. İşlemci, RAM ve diskin hepsi uzaktaki makinede çalışır. İhtiyacın olan tek şey **sürekli ve stabil bir internet bağlantısı** (Shadow en az 15 Mbit/s öneriyor; kablolu bağlantı ya da 5 GHz Wi-Fi daha iyi).

| Seçenek | Örnek fiyat (aylık) | RAM | Nasıl bağlanırsın | Not |
|---|---|---|---|---|
| **Shadow PC** (oyun/genel) | Lite $29.99 (ayda 120 saat) · Shadow PC $37.99 · Power Lite $43.99 (28 GB RAM) · Pro $54.99 · Power Pro $59.99 | Power: 28 GB | Windows, Mac, Linux, telefon, tablet, tarayıcı | Taahhüt yok. **Türkiye resmi listede değil**: abone olunabiliyor ama kalite garanti edilmiyor (en yakın veri merkezi Avrupa'da). |
| **Windows 365 Business** (iş) | $36 (2 çekirdek/8 GB) · $56 (4/16 GB) · $108.80 (8/32 GB) | 8–32 GB | Windows App: Windows, Mac, iOS, Android, tarayıcı | 30 gün deneme var. **Kişisel Microsoft hesabıyla girilmez**, iş (Entra) hesabı gerekir. |
| **Amazon WorkSpaces** | Standard ≈ $44 + Windows lisansı $4.19 | pakete göre | DCV istemcisi / tarayıcı | Saatlik "AutoStop" seçeneği de var. Bölgeye göre fiyat değişir. |
| **Bulut sanal makine** (Azure/AWS/Google) | Saat başı ödersin. Ör. Azure B2s Linux ≈ $0.04/saat (üçüncü taraf kaynağa göre; Windows daha pahalı) | istediğin kadar | Uzak Masaüstü | Kapatmayı unutursan fatura büyür. Kurulum teknik bilgi ister. |
| **Kendi diğer bilgisayarın** | Ücretsiz | o PC'deki RAM | Chrome Uzak Masaüstü / Windows Uzak Masaüstü | Evde daha güçlü bir PC varsa en ucuz yol bu. |

**Öneri:** Oyun ya da ağır program için 1 ay **Shadow PC Lite** dene, gecikmeyi kendi bağlantında ölç. İş ve ofis kullanımı için **Windows 365 Business** 30 gün deneme. Asıl sorun RAM yetmezliğiyse ve bilgisayarın yalnızca yavaşsa, en kalıcı ve ucuz çözüm **fiziksel RAM modülü** eklemektir (boş yuva varsa).

## Yapılmayan / doğrulanamayan
- Teşhis betiği gerçek bir **Windows** makinesinde çalıştırılmadı. Bulut ortamında PowerShell 7.4.6 ile örnek verilerle 13/13 test geçti (bkz. [calisma-gunlugu.md](calisma-gunlugu.md)).
- Türkiye'den gerçek gecikme ölçümü, hesap açma ve ödeme yapılmadı. Fiyatların TL karşılığı hesaplanmadı (kur uydurulmadı).
