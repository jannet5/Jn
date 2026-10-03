# SONUÇ — İnternetten RAM, Google Drive diski ve bulut bilgisayar

> Kısa özet: hepsini okumak istemiyorsan yalnız bu sayfa yeter.
> Ayrıntı ve kaynaklar için [arastirma-raporu.md](arastirma-raporu.md), yol haritası için [harita.md](harita.md) dosyasına bak.
> Fiyatlar 2026-10-03'te resmi ABD sayfalarından okundu. Hepsi USD ve vergi hariç; zamanla değişebilir. Satın alma yapılmadı.

## 1) İnternetten RAM kiralayıp kendi bilgisayarına takmak mümkün mü?
**Hayır. Ev bilgisayarı için böyle bir ürün yok.**
- RAM anakarta takılı bir çiptir ve işlemci ona nanosaniyeler içinde erişir. İnternet bundan binlerce kat yavaştır, bu yüzden uzaktaki "RAM" gerçek RAM gibi çalışamaz.
- "Download more RAM" siteleri şakadır. GitHub'da ağ üzerinden swap yapan deneme projeleri var (ör. `ram-dl`), ama diskten de yavaştırlar.
- Veri merkezlerinde bellek havuzu teknolojisi (CXL) var. Bu, aynı raftaki sunucuları özel bağlantılarla birbirine bağlar; internet üzerinden evine gelmez.

**Gerçekten işe yarayanlar:**
- Fiziksel RAM modülü takmak. Boş yuva olup olmadığını Görev Yöneticisi → Performans → Bellek → "Kullanılan yuvalar" bölümünden görebilirsin.
- Açık programları ve tarayıcı sekmelerini azaltmak.
- Ağır işleri bulut bilgisayarda çalıştırmak (bkz. madde 3).

## 2) Google Drive neden C: ile aynı boyutta ve dolu görünüyor?
- **Kesin olan (Google'ın resmi açıklaması):** Drive for desktop'taki alan göstergesi *bilgisayarındaki diskte kalan yeri* gösterir. Google hesabındaki depolama kotanı göstermez. Gerçek kotan şurada: https://one.google.com/storage
- **Senin bilgisayarın için olası açıklama:** Sanal Drive sürücüsü, önbelleğin bulunduğu yerel diskin (çoğunlukla C:) toplam ve boş alanını gösteriyor olabilir. Öyleyse "dolu" görünen kısım aslında C:'nin dolu kısmıdır. **Bu senin bilgisayarında ölçülmedi.** Kontrol etmek için aşağıdaki aracı çalıştır, ya da Drive → Ayarlar → Tercihler → Gelişmiş ayarlar → "Yerel önbellek dosyaları dizini" satırına bak.
- Drive bir **depolama** alanıdır, yani dosya saklar. **RAM değildir** ve bilgisayarı hızlandırmaz. Windows sayfa dosyası (sanal bellek) da ağ veya bulut sürücüsüne konamıyor (kullanıcı deneyimleri böyle). Yerel diske konsa bile RAM'den çok yavaştır.
- "Akış" (stream) modu bilgisayarda çok az yer kaplar. "Yansıtma" (mirror) modu ise dosyaların tam kopyasını diske indirir.

**Kendi bilgisayarında kontrol et:** [`araclar/ram-drive-tani.ps1`](araclar/ram-drive-tani.ps1)
```
powershell -ExecutionPolicy Bypass -File .\ram-drive-tani.ps1
```
- **Ne yapar:** Sistem ayarını, kayıt defterini ve Drive ayarını değiştirmez, hiçbir dosyayı silmez, internete bağlanmaz. Yalnızca okuma yapar.
- **Diske yazdığı tek şey:** `-CiktiJson dosya.json` verirsen yalnızca o JSON dosyasını oluşturur. Aynı adda bir dosya zaten varsa, `-UzerineYaz` eklemedikçe üzerine yazmaz.
- **Kararı neye göre verir:** Drive'ın önbellek konumunu Google'ın belgelediği kayıt defteri ayarından (`ContentCachePath`) okur. Bu kanıt bulunur ve kapasiteler de eşleşirse sonuç "DOĞRULANDI" olur. Sadece kapasiteler benziyorsa sonuç "ADAY" olur ve araç ilişkiyi "bilinmiyor" diye yazar. Hiçbir kanıt yoksa sonuç "BİLİNMİYOR" olur.
- **Önbellek boyutu:** Klasör en çok 200.000 öğe veya 20 saniye taranır. Tarama sınıra takılırsa ya da bazı klasörlere erişilemezse sonuç "KISMİ / EN AZ …" olarak yazılır, tam ölçülmüş gibi gösterilmez.

## 3) Sanal PC kiralarsam kendi bilgisayarımdan kullanabilir miyim?
**Evet, o bilgisayara gitmen gerekmez.** Kendi bilgisayarında bir uygulama ya da tarayıcı açarsın; uzaktaki bilgisayarın ekranı oraya gelir. İşlemci, RAM ve disk uzakta çalışır. Bunun için sürekli ve stabil bir internet bağlantısı gerekir (Shadow en az 15 Mbit/s öneriyor; kablolu bağlantı ya da 5 GHz Wi-Fi tavsiye ediliyor).

| Seçenek | Paket ve fiyat (aylık, USD, vergi hariç) | Saat sınırı | Bağlanma | Önemli şartlar |
|---|---|---|---|---|
| **Shadow** (oyun) | Neo Lite $29.99 · Power Lite $43.99 (28 GB RAM) | 120 saat/ay, oturum başına en fazla 4 saat | Windows, Mac, Linux, telefon, tablet, tarayıcı | Taahhüt yok |
| | Neo $37.99 · Power $54.99 (28 GB RAM) | Sınırsız (adil kullanım), oturum başına 6 saat | aynı | Taahhüt yok |
| **Shadow** (profesyonel) | Neo Pro $44.99 · Power Pro $59.99 (28 GB RAM) | Sınırsız (adil kullanım), oturum başına 8 saat | aynı | Taahhüt yok |
| **Windows 365 Business** | $36 (2 vCPU/8 GB) · $56 (4/16 GB) · $108.80 (8/32 GB), kullanıcı başına | Yok | Windows App: Windows, Mac, iOS, Android, tarayıcı | **Otomatik yenilenir.** 30 günlük deneme yalnızca 2 vCPU/8 GB pakette var. **Deneme için kredi kartı isteniyor ve deneme bitince ücret otomatik çekiliyor** (iptal edilmezse). Kişisel Microsoft hesabıyla girilemiyor, iş (Entra) hesabı gerekiyor. |
| **Amazon WorkSpaces** | Standard ≈ $44 + Windows lisansı $4.19/kullanıcı (bölgeye göre değişir) | Saatlik "AutoStop" seçeneği de var | DCV istemcisi / tarayıcı | Kurulum teknik bilgi ister |
| **Kendi diğer bilgisayarın** | Ücretsiz | — | Chrome Uzak Masaüstü / Windows App | Evde güçlü bir ikinci PC varsa en ucuz yol |

**Türkiye ile ilgili iki ayrı konu:**
1. **Ülke uygunluğu:** Türkiye, Shadow'un resmi ülke listesinde yok. Shadow, desteklenmeyen ülkelerden de abone olunabileceğini, ancak deneyim kalitesini garanti edemeyeceğini yazıyor. Türkiye'den hesap açma ve ödemenin gerçekten kabul edilip edilmediği **denenmedi**.
2. **Gecikme:** Bağlantının senin evinden ne kadar hızlı olacağı **ölçülmedi**. Bunu ancak bir deneme aboneliğiyle kendi bağlantında görebilirsin.

**Öneri:**
- Oyun ya da ağır program için: taahhütsüz Shadow Neo Lite'ı bir ay dene ve gecikmeyi kendin ölç.
- Ofis işleri için: Windows 365 denemesi. İstemiyorsan 30 gün dolmadan iptal et, yoksa kart ücretlendirilir.
- Sorun yalnızca RAM yetmezliğiyse: boş yuva varsa RAM modülü takmak en kalıcı ve en ucuz çözüm.

## Yapılmayan / doğrulanamayan
- Teşhis aracı gerçek bir Windows bilgisayarda, gerçek Google Drive kurulumuyla ve senin bilgisayarında **çalıştırılmadı**. Bulut ortamında PowerShell 7.4.6 ile 34 otomatik testin hepsi geçti. Ayrıca root olmayan bir kullanıcıyla yapılan gerçek erişim hatası testi de geçti (bkz. [calisma-gunlugu.md](calisma-gunlugu.md)).
- Shadow ya da Windows 365 hesabı açılmadı, ödeme yapılmadı, gecikme ölçülmedi. Fiyatların TL karşılığı ve Türkiye'deki KDV hesaplanmadı.
