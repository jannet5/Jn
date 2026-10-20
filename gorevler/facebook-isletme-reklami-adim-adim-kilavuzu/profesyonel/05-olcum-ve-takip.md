# Ölçüm, Piksel, CAPI ve ilişkilendirme

> **Profesyonel paket · Modül 05** · Bilgi tarihi: 17 Ekim 2026 · Piksel oluşturma, "Tanıtım objesi bulunamadı" hatası ve dönüşüm konumu ekranı **Kurulum Kılavuzu §3.7 ve §5.1**'de (`../KILAVUZ.md`), ilk 7 günün izlenmesi §6.3'te. Başabaş CPA/ROAS ve 1,05 konum ücreti çarpanı **Modül 01 §3** ile **Modül 02 §1**'de. Olay merdiveni Modül 02 §2.2'de, adlandırma standardı Modül 02 §10'da.

**Bu modülde**

- İş modeline göre ölçüm kurgusu, hukuki ön koşul ve site için kurulum kontrol listesi: Piksel, CAPI, tekilleştirme, olaylar, alan adı, rıza, test siparişi (§1–§2).
- Altyapı entegrasyonları (Shopify, ikas, Ticimax, WooCommerce), EMQ hedefi ve teşhis tablosu (§3–§4).
- İlişkilendirme ayarları, 2026 değişiklikleri, Meta–GA4–kasa farkının nedenleri, haftalık mutabakat formülleri, kapıda ödeme ve havale siparişleri (§5). GA4 kanal kuralına uygun UTM şablonu (§6).
- WhatsApp, Instagram Direct, form, telefon ve mağaza satışını ölçme yolları: hazır etiket, Potansiyel Müşteri Merkezi, Google E-Tablolar, CRM, kupon kodu (§7–§9).
- Veri kalite kontrolleri ve artırımlı etkiyi ölçmenin pratik yolları (§10–§11).

**Etiketler:** **[RESMİ]** Meta, Google ya da altyapının kendi belgesi · **[MEVZUAT]** Türk mevzuatı · **[PRATİSYEN]** ajans, araç satıcısı ya da uzman yazısı (çıkar çatışması olabilir) · **[TOPLULUK]** forum · *sezgisel* = ölçülmemiş çalışma kuralı · *türetilmiş* = resmi kuraldan yapılan aritmetik · *varsayım* = örnek için uydurulmuş rakam. Meta'nın "%X daha iyi" rakamları Meta'nın kendi iddiasıdır, yöntemi çoğu zaman yayımlanmaz.

**Sabit terimler (Modül 01'in başındaki "Sabit terimler" tablosuyla aynı).** Bu modülde tek başına "panel" kelimesi kullanılmaz.
- **RY** = Reklam Yöneticisi. *RY harcaması*, *RY sonucu*: Reklam Yöneticisi'nin gösterdiği sayılar. Harcamada konum ücreti yoktur.
- **Fatura** (gerçek maliyet) = RY harcaması × 1,05. Vergi numarası girili değilse × 1,26 (Modül 01 §3.2, Modül 02 F3).
- **Sipariş sistemi** = işletmenin kendi kaydı: ikas ya da Shopify sipariş ekranı, kasa, muhasebe, CRM, WhatsApp satış defteri. Sipariş sayısı ve ciro buradan alınır.
- **OY** = Olay Yöneticisi. Olayların Meta'ya ulaşıp ulaşmadığını gösterir. Satış sayısı kaynağı değildir.

---

## 0. Çekirdek kurallar

1. Sonuç Meta'ya geri dönmüyorsa kampanya o sonuca optimize edilemez. Önce ölçüm kurulur, sonra kampanya.
2. Sitede Piksel ve CAPI birlikte kurulur. Aynı olaylar `event_id` ile tekilleştirilir [RESMİ].
3. Hazır altyapıda yalnız altyapının Meta entegrasyonu kullanılır. Üstüne elle kod ya da kodsuz olay eklenirse olaylar iki kez sayılır [RESMİ, platform].
4. Kararlarda tek doğruluk kaynağı sipariş sistemidir. Meta raporu optimizasyon ve yön içindir (*sezgisel*).
5. Karşılaştırma her zaman aynı ilişkilendirme ayarı ve modeliyle yapılır [RESMİ].
6. Kişisel veri Meta'ya gitmeden önce hukuki ön koşul sağlanır (§2 başı). Çerezi reddeden ziyaretçinin olayı Pikselden de CAPI'den de gönderilmez (§2.6) (*sezgisel*, hukukçu aksini yazılı onaylamadıkça).

---

## 1. İş modeli → ölçüm kurgusu

| İş modeli | Meta'ya dönen sinyal | Kurulum | CRM yoksa asgari takip |
|---|---|---|---|
| E-ticaret, hazır altyapı | Purchase (Piksel + CAPI) | Altyapı entegrasyonu (§3) | Sipariş sistemi + UTM (§6) |
| E-ticaret, özel kodlu site | Purchase | Meta destekli CAPI, Gateway ya da doğrudan (§2.2) | Aynı |
| Kapıda ödeme ya da havale/EFT ağırlıklı mağaza | Purchase + (geliştirici varsa) onaylı sipariş özel olayı | Yukarıdaki gibi, ek kurallar §5.6 | Ret ve onay oranı takibi (§5.6) |
| WhatsApp'tan satış | Konuşma → alışveriş etiketi ya da mesajlaşma CAPI'si | WhatsApp Business paylaşımı (§7.2) | Google E-Tablolar satış defteri + kampanya kodu |
| Instagram DM'den satış | Konuşma → Profesyonel Gelen Kutusu alışveriş etiketi | Instagram veri paylaşımı (§7.6) | Satış defteri + kampanya kodu |
| Hızlı form | Lead → CRM aşamaları | Potansiyel Müşteri Merkezi, Google E-Tablolar ya da CRM bağlantısı (§8) | Potansiyel Müşteri Merkezi aşaması ya da `lead_status` sütunu |
| Site formu, randevu | Lead / Schedule + CRM aşamaları | Piksel + CAPI + CRM | Satış defteri |
| Telefon, fiziksel mağaza | Arama; çevrimdışı olay | Veri setine CSV ya da CAPI (§9) | Satış defteri + kupon (§7.4) |

**Eğer** satışın Meta'ya dönmesi 2 haftadan uzun sürecekse **ise** kampanyayı daha sığ bir olayla başlat (Modül 02 §2.2), ölçümü paralelde tamamla (*sezgisel*).

---

## 2. Site ölçümü: kurulum kontrol listesi

> **Hukuki ön koşul (hukuki görüş değildir; ayrıntı Modül 09 §5.6 ve Modül 03 §4.4).** Kuruluma geçmeden müşteriden şu üç cevabı yazılı al:
> 1. Gizlilik ve aydınlatma metni, ziyaretçi ve müşteri verisinin reklam ölçümü ve reklam amacıyla Meta'ya aktarıldığını yazıyor mu?
> 2. Meta'ya aktarım büyük olasılıkla yurt dışına aktarımdır (KVKK md. 9). Aktarım mekanizması hukukçuyla belirlenip kayda geçti mi?
> 3. Çerez bandı reklam çerezleri için açık rıza alıyor mu? Altyapının sunucu olayları (CAPI) bu rıza sinyaline uyuyor mu? Bu son sorunun cevabını altyapının desteğinden yazılı al.
>
> **Eğer** 1. ya da 2. sorunun cevabı "hayır" veya "bilinmiyor" **ise** Piksel yalnız rıza kapısının arkasında kurulur. Müşteri bilgisi taşıyan her gönderim hukukçu onayına kadar bekler: gelişmiş eşleştirme, CAPI'deki e-posta, telefon ve ad alanları, CRM aşama gönderimi (§8) ve çevrimdışı yükleme (§9). IP, `fbp` ve `fbc` de kişisel veri sayılabilir. CAPI'nin bunlarla açılıp açılamayacağını da hukukçu söyler (*sezgisel*, ihtiyat). **Eğer** 3. sorudaki teyit alınamıyorsa **ise** §2.7'deki rıza testini yap ve sonucu müşteriye yaz. Test, reddeden ziyaretçinin sunucu olayının gittiğini gösterirse CAPI hukukçu karar verene kadar kapalı kalır (§2.6).

### 2.1 Piksel

- [ ] Sitede tek piksel var. İkinci piksel yalnız iki ayrı ajans çalışıyorsa gerekir [RESMİ]. Meta Pixel Helper (Chrome eklentisi) ile her sayfada bir kez yüklendiğini gör.
- [ ] Otomatik gelişmiş eşleştirme, hukuki ön koşul sağlandıysa açık. Altyapı entegrasyonu varsa kodsuz olay kurulumu kapalı ve eski elle eklenmiş olaylar silinmiş [RESMİ, ikas].
- [ ] Olay adları birebir doğru. `viewcontent` yazılırsa standart olay değil, özel olay oluşur [RESMİ].
- [ ] Trafik izinlerinde izin listesi açık. Piksel kimliği herkese açıktır, başka bir site verini kirletebilir [RESMİ]. Listeye olay gönderen **tüm meşru** alan adlarını ekle: ana alan adı, altyapının alt alan adı (ör. `magaza.myikas.com`, `magaza.myshopify.com`), ödeme ve teşekkür sayfasının açıldığı alan adı. Meta da ortak entegrasyonu, iframe ya da yönlendirme kullanıldığında iki alan adına birden izin verilmesini öneriyor [RESMİ]. Üst alan adını eklemek onun alt alan adlarını da kapsar [RESMİ]. Ancak `myikas.com` altındaki bir adres senin alan adının alt alan adı değildir, ayrıca eklenir.
  - Listeyi kaydetmeden önce Tanılar'da ve trafik izinleri ekranında son 28 günde olay gelen alan adlarını gör. Kaydettikten sonra bir test siparişiyle Purchase'ın hâlâ geldiğini doğrula (§2.7). Yanlış liste Purchase'ı sessizce keser ve §10.2'deki "olay 24 saattir sıfır" durumunu yaratır (*sezgisel*).
- [ ] "Yapay zekâ ile sayfa ve ürün bilgisi ekleme" ayarı (2026) gözden geçirilmiş. Hassas sektörde kapat ya da paylaşılan veri türlerini tek tek seç [RESMİ].
- [ ] Altyapının sunucu olaylarının çerez rızasına uyduğu destekten yazılı teyit edildi ve §2.7'deki rıza testiyle doğrulandı (*sezgisel*).

### 2.2 CAPI yolu seçimi

| Durum | Yol | Not |
|---|---|---|
| Shopify, ikas, WooCommerce, Ticimax, Wix | Altyapı entegrasyonu | Ek ücret yok [RESMİ] |
| Özel site, piksel doğru çalışıyor, geliştirici yok | Meta destekli (tek tık) CAPI, hesapta görünüyorsa | Ücretsiz, yalnız site olayları, tekilleştirmeyi Meta yapar [RESMİ]. Pikselin hatasını da kopyalar [PRATİSYEN] |
| Tek tık yok, site olayına optimize kampanyaya ayda 300 USD'den fazla harcanıyor | CAPI Gateway (AWS/GCP) | Bulut maliyeti aylık 30 USD'den başlar [RESMİ]. Barındırılmış hizmet fiyatını satıcıdan doğrula |
| Geliştirici var, CRM, iade, mağaza ya da uygulama olayları da gidecek | Doğrudan entegrasyon ya da sunucu tarafı GTM | Meta'nın tahmini 2–4 hafta [RESMİ] |
| Piksel hiç ya da yanlış tetikleniyor | Önce pikseli düzelt | CAPI yanlış veriyi düzeltmez (*sezgisel*) |

**Doğrudan entegrasyonun kuralları [RESMİ]:**
- Zorunlu alanlar: `event_name`, `event_time`, `action_source=website`, `event_source_url` ve `user_data` içinde `client_user_agent`. İstemcinin gerçek IP'sini gönder, proxy IP'si gitmesin.
- `event_time` en fazla 7 gün eski olabilir. İstekte tek bir eski olay varsa bütün istek hata verir.
- Hash: E-postada boşlukları sil ve küçük harfe çevir. Telefonda simgeleri ve baştaki sıfırı at, ülke kodunu ekle: `0532 123 45 67` → `905321234567`. Sonra SHA-256 uygula. Ad, soyad, şehir ve ülke de hash'lenir. `fbp`, `fbc`, IP ve user agent **hash'lenmez**.

### 2.3 Tekilleştirme

- Aynı olay iki kanaldan geliyorsa iki koşul gerekir: `event_name` iki tarafta aynı olmalı, pikseldeki `eventID` sunucudaki `event_id` ile aynı olmalı [RESMİ].
- Pencere 48 saattir. `fbp` ya da `external_id` ile yedek eşleşme yalnız tarayıcı olayı önce gelirse çalışır [RESMİ].
- Farklı olaylar gönderiliyorsa (tarayıcıdan AddToCart, sunucudan Purchase) tekilleştirme gerekmez [RESMİ].
- Purchase'ta `event_id` olarak sipariş numarasını kullan (*sezgisel*):
  - Piksel: `fbq('track','Purchase',{value:1000,currency:'TRY'},{eventID:'SIP-104233'})`
  - Sunucu: `"event_name":"Purchase","event_id":"SIP-104233"`
- İşletme mesajlaşması CAPI'sinde Meta tekilleştirme yapmaz, tekrarları sen ayıklarsın [RESMİ].

### 2.4 Olaylar ve parametreler

| İş | Optimize edilecek olay | Destek olayları | Parametreler |
|---|---|---|---|
| E-ticaret | Purchase | ViewContent, AddToCart, InitiateCheckout | `value`, `currency=TRY`, katalogla aynı `content_ids`, `event_id` = sipariş no |
| Site formu | Lead (CRM varsa nitelikli aşama) | CompleteRegistration | İsteğe bağlı `value` |
| Randevu | Schedule | Lead, Contact | — |
| Sitede WhatsApp/ara düğmesi | Contact (yalnız sinyal) | — | Contact satış değildir |

- PageView temel kodla otomatik gelir. Purchase'ta `value` ve `currency` gönderilir [RESMİ].
- Kapıda ödeme ve havale/EFT siparişlerinde Purchase, para alınmadan teşekkür sayfasında tetiklenir. Bu siparişlerin karar kuralları §5.6'da.
- Dar hedef için özel dönüşüm kullanılabilir, örneğin "değeri 500 TL üstü Purchase" ya da "URL'de `/tesekkurler` geçen sayfa". Yeterli veri toplanınca performans hedefi olarak seçilebilir [RESMİ].
- Hangi olaya optimize edileceği haftalık hacme göre seçilir (Modül 02 F5, §2.2).

### 2.5 Alan adı ve AEM [RESMİ]

- Olay yapılandırması ve kampanyada "dönüşüm alan adı" seçimi için alan adı doğrulaması artık zorunlu değil.
- Yine de doğrula: bağlantı düzenleme izinleri, ticaret özellikleri ve bazı işletme doğrulamaları için gerekir. Meta etiket, HTML dosyası ya da DNS TXT kaydıyla yapılır, ücretsizdir.
- AEM: Alan adı başına 8 olay önceliklendirmesi kalktı, AEM sekmesi kaldırıldı, reklamverenin yapacağı bir şey yok. Bu kuralı anlatan kaynak eskidir.

### 2.6 KVKK ve çerez kapısı (hukuki görüş değildir)

- KVKK Çerez Rehberi'ne (Temmuz 2025) göre davranışsal reklam çerezleri açık rıza ister [MEVZUAT]. Piksel rızadan önce tetiklenmemeli, "Reddet" seçeneği "Kabul et" kadar kolay olmalı [PRATİSYEN, hukuk bürosu].
- CAPI rıza yükümlülüğünü kaldırmaz. Meta da CAPI'nin gizlilik kurallarını aşmak için yapılmadığını yazıyor [RESMİ].
- **Kural (*sezgisel*, ihtiyat; hukukçu aksini yazılı onaylamadıkça):** Çerezi reddeden ziyaretçinin site olayları CAPI ile de gönderilmez. CAPI yalnız teknik kaybı azaltır: reklam engelleyici, tarayıcı kısıtı, bağlantı kopması. Rıza reddinden doğan kayıp beklenen bir kayıptır ve telafi edilmez.
- **Çerez bandına dokunma.** Kabul oranını artırmak için bandın tasarımını değiştirme: "Reddet" düğmesini gizlemek, küçültmek, soluk yapmak ya da ikinci katmana taşımak yok. Bant yalnız hukukçunun talebiyle değişir (*sezgisel*).
- Sağlık ve finans gibi alanlarda URL'de, olay parametresinde ya da özel olay adında hassas bilgi bulunmaz. Bu, İşletme Araçları Koşulları'na aykırıdır [RESMİ]. Ayrıntı için Modül 09 §5.6'ya bak.
- Yan etki: Rıza reddedildikçe Piksel ve CAPI olayları düşer. Meta ve GA4 rakamları sipariş sisteminin altında kalır (§5.4). Mutabakatta bu, rıza oranı R ile hesaba katılır (§5.5).

### 2.7 Test protokolü

Her yeni kurulumda ve her tema, eklenti, ödeme ya da önbellek değişikliğinden sonra uygula.

1. Olay Yöneticisi'nde **Olayları Test Et** sekmesini aç. Sunucu olayları için ekrandaki test kodunu kullan.
2. Çerez bandında reklam çerezlerini **kabul et**. Ürün sayfasını aç, sepete ekle, ödemeyi başlat ve küçük tutarlı bir sipariş ver. Siparişte kargo ücreti ve mümkünse küçük bir indirim olsun ki değer alanı ayırt edilebilsin. Sonra iptal et ya da altyapının test modunu kullan.
3. Kontrol et:
   - Her olay bir kez geldi.
   - Purchase hem "Tarayıcı" hem "Sunucu" satırında var ve tekilleştirildi.
   - **Değer modu:** Gönderilen `value` değerini sipariş sistemindeki şu tutarlarla karşılaştır: KDV dahil, KDV hariç, kargolu, kargosuz, indirimden önce, indirimden sonra. Hangisine eşitse onu "değer modu" olarak not et, ör. "KDV dahil, kargosuz, indirim sonrası" (Modül 01 §1.4, soru 17). RY ROAS eşiği bu moda göre hesaplanır (Modül 01 "Sabit terimler" tablosundaki Piksel çarpanı, §3.2).
   - `currency` TRY. Katalog varsa `content_ids` katalogla aynı.
   - Kapıda ödeme ya da havale/EFT seçeneği varsa onunla da bir sipariş ver. Purchase'ın ne zaman tetiklendiğini not et: teşekkür sayfasında mı, ödeme onayında mı (§5.6)?
4. **Rıza testi:** Gizli pencerede aynı akışı tekrarla, bu kez çerez bandında **Reddet** seç. Olayları Test Et'te bu oturumdan ne Tarayıcı ne Sunucu satırında reklam olayı görünmemeli. Sunucu olayı geliyorsa altyapı rıza sinyaline uymuyordur: §2.6 kuralını uygula, altyapıya ve müşterinin hukukçusuna yaz (*sezgisel*). Not: Sunucu test kodu oturuma bağlı değilse olayı sipariş numarasından ayırt et.
5. 24–48 saat sonra Genel bakış, **Tanılar** ve EMQ ekranlarına bak. Tanılar'da kırmızı simge acil sorun, sarı simge uyarıdır [RESMİ].
6. Müşteri dosyasına yaz: tarih, test sipariş no, gönderilen değer, değer modu, rıza testinin sonucu.

### 2.8 Gönderim gecikmesi [RESMİ]

| Kullanım | Önerilen | Azami |
|---|---|---|
| Satış ve potansiyel müşteri optimizasyonu | 1 saat | 7 gün |
| Hedef kitleler | 24 saat | 7 gün |
| Çevrimdışı olaylar (ilişkilendirme) | 7 gün | 62 gün |
| Dönüşüm yükselişi testleri | 7 gün | 90 gün |

**Kural (*sezgisel*):** CRM'den günde bir kereden seyrek yükleme yapılırsa optimizasyon sinyali zayıflar. Hedef, olaydan sonraki 1 saat içinde göndermektir.

---

## 3. Platform entegrasyonları

| Altyapı | Kurulum yolu | Tekilleştirme | Dikkat |
|---|---|---|---|
| **Shopify** | "Facebook & Instagram" uygulaması. Koşullar: Basic ya da üstü plan, parolasız mağaza, portfolyoda yönetici izni [RESMİ] | Uygulama yapar | Veri paylaşımı "Enhanced" ya da "Maximum", veri erişimi "Always on" olmalı [RESMİ]. Temaya ya da GTM'ye ikinci piksel ekleme |
| **ikas** | Satış Kanalları > kanal > Eklentiler > Facebook Piksel > Bağla. CAPI jetonunu Olay Yöneticisi'nde oluştur, "Facebook Conversion API" eklentisinde Bağla > Test Et > Kaydet [RESMİ, ikas] | ikas yapar (Event ID) | Kodsuz olay takibi kapalı olmalı. Olay Yöneticisi'nde tarayıcı ve sunucu ayrı görünür, bu çift görünüm normaldir. Meta her sepete eklemeyi sayar, ikas sepeti bir kez sayar |
| **Ticimax** | Ayarlar > Genel Ayarlar > Dijital Pazarlama [RESMİ, Ticimax] | Belgede yazmıyor | Olayları elle tekrar ekleme. Test siparişiyle tekilleştirmeyi doğrula, doğrulanamazsa destekten teyit al (*sezgisel*) |
| **WooCommerce** | "Meta for WooCommerce" eklentisi [RESMİ] | Eklenti yapar | Önbellek ve JS birleştirme eklentileri olayları bozabilir |
| **Diğer** (T-Soft, İdeasoft vb.) | Altyapının entegrasyon menüsü | Altyapıya sor | CAPI destekliyorsa onu kullan, yoksa §2.2. Meta da önce ortak entegrasyonu öneriyor [RESMİ] |

**Çift sayım vakası [TOPLULUK]:** Bir Shopify mağazasında Meta 7 günde 325 satın alma, Shopify 190 sipariş gösterdi. Önerilen çözüm uygulamayı yeniden bağlamak, tek piksel kaynağı bırakmak ve test siparişi vermekti. **Eğer** tekilleştirilmiş Purchase sayısı (§5.5'teki K) ya da RY'deki alışveriş sayısı sipariş sistemindeki siparişin belirgin üstündeyse **ise** ikinci piksel kaynağını ara (§10.2). OY'nin Veri kaynakları ekranındaki toplam olay sayısını bu karşılaştırmada kullanma. O sayı tekilleştirilmez [RESMİ], tarayıcı ve sunucu satırları toplanınca doğru kurulmuş bir hesapta da siparişin 2 katına yaklaşır.

---

## 4. EMQ (Olay Eşleştirme Kalitesi) hedefi

**Ne ölçer [RESMİ]:** EMQ, CAPI ile gelen site olayları için 0–10 arası bir puandır. Gönderilen müşteri bilgisinin kalitesine ve eşleşen olay oranına bakar. Son 48 saatin verisiyle hesaplanır, bu yüzden düzenli olay akışı gerekir.

**Parametre önceliği [RESMİ]:**
- Yüksek: e-posta, `fbc`.
- Orta: telefon, `external_id`, `fbp`, doğum tarihi, ülke.
- Düşük: ad, soyad, şehir, posta kodu.

**Hedef bantları:** Meta sayısal eşik vermiyor. İlk satır bir CAPI satıcısının bantlarından alındı: 6–7,9 "iyi", 8 ve üstü "çok iyi" [PRATİSYEN]. Diğer satırlar *sezgisel*.

| Olay | Kabul | Hedef |
|---|---|---|
| Purchase, Lead, CompleteRegistration | ≥ 6 | ≥ 8 |
| AddToCart, InitiateCheckout | ≥ 5 | ≥ 6,5 |
| PageView, ViewContent | Düşük çıkması normaldir, çünkü anonim ziyaretçiden e-posta alınamaz (*sezgisel*) | — |

**Teşhis (*sezgisel*):**

| Belirti | Olası neden | Düzeltme |
|---|---|---|
| `fbc` az | Yönlendirme `fbclid`'i siliyor ya da sunucu `_fbc` çerezini okumuyor | Açılış yönlendirmesini kaldır, `_fbc`'yi sunucuya taşı |
| E-posta ve telefon yok | Misafir ödeme, telefon biçimi yanlış | Ödemede e-posta veya telefonu zorunlu yap, telefonu `90…` biçimine çevir |
| `external_id` yok | Müşteri numarası gönderilmiyor | Müşteri numarasını pikselde ve sunucuda aynı biçimde gönder |
| Puan ani düştü | Hash hatası, tema ya da eklenti değişikliği | §2.7'yi tekrarla |

**Rutin:** EMQ'yu haftada bir izle. Alt huni olayında puan 1'den fazla düşerse aynı gün teşhis et (*sezgisel*).

---

## 5. İlişkilendirme

### 5.1 Ayarlar [RESMİ]

- **Modeller (reklam seti düzeyinde):**
  - **Standart.**
  - **Artımlı:** Dönüşümün reklam yüzünden olup olmadığını makine öğrenmesiyle tahmin eder ve buna optimize eder. Satış, Etkileşim ya da Potansiyel müşteriler amacında, dönüşüm konumu site olduğunda seçilir. Teklif kontrolü kullanılamaz, model yayından sonra değiştirilemez.
  - **Özel:** Harici analiz aracının verisini kullanır. Hesaba göre değişebilir.
- **Standart pencereler:** bağlantı tıklamasından sonra 1 ya da 7 gün, gösterimden sonra 1 gün, 1 gün etkileşim. Etkileşim, bağlantı dışı tıklama ya da 5 saniye video izlemedir.
- **Raporlama:** Sütunlar > İlişkilendirme ayarlarını karşılaştır ekranında 1 gün tıklama, 7 gün tıklama, 1 gün görüntüleme ve artımlı sonuç yan yana açılır. Artımlı sütunda 1 Nisan 2025 öncesi veri yoktur.

### 5.2 Karar kuralları

- **Varsayılan:** Standart model. Yeni setlerde hesabın mevcut standart ayarını koru. Hesapta yerleşik bir standart yoksa 7 gün tıklama + 1 gün görüntüleme seç, 1 gün etkileşimi dahil etme (*sezgisel*). Etkileşimin dahil olup olmadığı hesap için bir kez kararlaştırılır, brife yazılır ve bütün setlerde aynı tutulur. Mart 2026'dan beri etkileşim beğeni, paylaşım, kaydetme ve 5 saniye video izlemeyi de kapsadığı için bu seçim rakamı belirgin değiştirebilir (§5.3). Etkileşimi ayrı seçme imkânı hesaba göre değişebilir. Farklı modeldeki setler aynı tabloda kıyaslanmaz [RESMİ].
- **Yayındaki setin ilişkilendirme ayarını rapor için değiştirme.** Standart ilişkilendirme ayarı yalnız raporu belirlemez, yayını da seçilen pencere ve davranışlara göre optimize eder [RESMİ]. Ayarı değiştirmek setin yayınını da değiştirir. Farklı pencereleri yalnız **Sütunlar > İlişkilendirme ayarlarını karşılaştır** ekranında oku. Değişikliğin öğrenmeyi sıfırlayıp sıfırlamadığı doğrulanmalıdır.
- **Görüntüleme payı** = 1 gün görüntüleme dönüşümü ÷ toplam dönüşüm (*türetilmiş*). **Eğer** pay %30'u ya da hesabın kendi ortalamasını belirgin geçiyorsa (*sezgisel eşik*) **ise** seti "kanıtı zayıf" say. Bütçe artırmadan önce artımlı sütuna ve §11'e bak.
- **Eğer** yeniden pazarlama ya da mevcut müşteri ağırlıklı bir set iyi görünüyorsa **ise** artımlı sütunla karşılaştır. Bir pratisyenin 45 coğrafi testinde artımlı ROAS, 7 gün tıklama ROAS'ının mevcut müşteride yaklaşık 0,60 katı, yeni müşteride 1,14 katı çıktı [PRATİSYEN, Common Thread Collective].
- **Artımlı modelle optimizasyon:** Yeni bir kampanyada, mevcut kampanyaya karşı A/B testiyle dene (*sezgisel*). Modeli değiştirmek için yeni kampanya gerekir [RESMİ].

### 5.3 Ölçüm kırılma tarihleri

Bu tarihlerin öncesini ve sonrasını doğrudan karşılaştırma.

| Tarih | Değişiklik | Etki |
|---|---|---|
| 12 Ocak 2026 | Ads Insights API artık 7 ve 28 günlük görüntüleme pencerelerini vermiyor [RESMİ] | Looker Studio ya da Supermetrics'teki eski sütunlar boş gelebilir |
| 3 Mart 2026 | Site ve mağaza dönüşümünde "tıklama" artık yalnız bağlantı tıklaması. Beğeni, paylaşım ve kaydetme etkileşime geçti. Video eşiği 10 saniyeden 5 saniyeye indi [RESMİ] | "Tıklama" dönüşümleri düşebilir. Bu performans düşüşü değil, yeniden sınıflandırmadır. Bazı hesaplar hâlâ eski tanımda olabilir |

### 5.4 Meta, GA4 ve kasa neden tutmuyor?

| Neden | Yüksek görünen | Ne yapmalı |
|---|---|---|
| Görüntüleme sonrası ve cihazlar arası dönüşümü yalnız Meta sayar [RESMİ] | Meta | 7 gün tıklama sütununa ayrıca bak |
| Uygulama içi tarayıcıdan sonra yeni sekmede dönen kullanıcının yönlendiren URL'si kaybolur [RESMİ] | Meta (GA4 düşük) | UTM her zaman eklenir (§6) |
| GA4 son tıklama ya da veriye dayalı model kullanır. Geriye bakış edinmede 30, diğer olaylarda 90 gündür [RESMİ, Google] | Değişken | Araçları eşitlemeye çalışma |
| Meta eksik veriyi istatistiksel modellemeyle tamamlayabilir [RESMİ] | Meta | Modellenmiş sonucu hesaba kat |
| Reklam engelleyici ve tarayıcı kısıtı pikseli durdurur [RESMİ] | Sipariş sistemi | CAPI bu kaybı azaltır |
| Çerez reddi: rıza vermeyen ziyaretçinin olayı gönderilmez (§2.6) | Sipariş sistemi | Beklenen kayıptır, telafi edilmez. CAPI ile kapatılmaz, çerez bandına dokunulmaz. Mutabakatta R oranıyla hesaba kat (§5.5) |
| Çift piksel ya da tekilleştirme hatası | Meta (hata) | §3, §10.2 |
| Sipariş sisteminde iptal, iade ve kapıda ödeme reddi düşülür. Piksel bunları kendiliğinden düşmez (*sezgisel*, entegrasyona bağlı) | Meta değeri | Net ciroyu sipariş sisteminden al. Kapıda ödeme ve havale için §5.6 |
| KDV dahil ya da hariç, kargolu ya da kargosuz değer gönderimi | Değişken | Değer modunu test siparişiyle bul (§2.7), V ile izle (§5.5) |
| API dönüşümü gösterim gününe ya da dönüşüm gününe yazabilir (`action_report_time`) [RESMİ] | Günlük farklar | Haftalık karşılaştır |
| UTM eksik ya da medium "social" | GA4'te Organic Social veya Referral | §6 |

**Kural (*sezgisel*):** Araçlar birbirini doğrulamak için kullanılmaz. Her araç kendi zaman serisinde izlenir, gerçek ciro sipariş sisteminden alınır.

### 5.5 Haftalık mutabakat: formüller ve örnek

Formüller *türetilmiş*, eşikler *sezgisel*. Her oranda payın ve paydanın hangi sistemden geldiği yazılıdır. Sipariş sayısı her yerde aynıdır: sipariş sistemindeki tüm siparişler (bütün kanallar, iptal öncesi, aynı tarih aralığı). Bu modüldeki **P (Meta payı)**, Modül 01'in "Sabit terimler" tablosundaki **P (Piksel çarpanı)** ile aynı şey değildir.

**Önce tekilleştirilmiş sayıyı bul.** OY'nin Veri kaynakları ekranındaki toplam Purchase sayısı K'ya girmez. Meta bu toplamın tekilleştirilmediğini yazıyor [RESMİ]. Piksel + CAPI doğru kurulmuş bir hesapta tarayıcı ve sunucu satırları toplanınca sayı siparişin 2 katına yaklaşır (*türetilmiş*). Bunun yerine:
- **Yol (a), ana yol:** OY > Özel dönüşümler'de Purchase olayına dayanan, filtresiz bir "Tüm Purchase" özel dönüşümü oluştur. Meta özel dönüşüm toplamını tekilleştirir [RESMİ]. Arayüz kural zorunlu tutuyorsa her siparişi kapsayan tek bir kural koy, ör. "URL'de kendi alan adın geçer" (ödeme başka alan adında bitiyorsa onu da ekle). Özel dönüşüm, oluşturulduğu günden önceki olayları göstermeyebilir. Seriyi oluşturma tarihinden başlat (doğrulanmalı).
- **Yol (b), kanal ayrımı:** OY'de Purchase olayının ayrıntısında "Tarayıcı" ve "Sunucu" satırları ayrı görünür. Her biri ayrı ayrı siparişle oranlanır. İkisi toplanmaz.
- Tekilleştirmenin çalıştığını tablo değil, test siparişi kanıtlar. Olayları Test Et'te sunucu olayının tekilleştirildiğini gör (§2.7).

**Rıza oranı R** = reklam çerezini kabul eden ziyaretçi ÷ bandı gören ziyaretçi. Çerez yönetim aracının raporundan alınır. Rapor yoksa "bilinmiyor" yaz. Her iki kanal da rızaya uyuyorsa beklenen K yaklaşık R'dir. Satın alanların rıza oranı ziyaretçilerinkinden farklı olabileceği için bu kaba bir beklentidir (*türetilmiş*).

**Oranlar:**
- **Kapsama K** = "Tüm Purchase" özel dönüşümü (OY, organik dahil) ÷ sipariş sistemindeki sipariş. Beklenen değer R civarıdır ve normalde 1'in altında kalır.
  - K > 1,05 → çift sayım şüphesi.
  - K < 0,70 → önce R'ye bak. K, R'ye yakınsa (fark 0,10'dan az) bu durum normaldir ve çerez bandına dokunulmaz.
  - K, R'nin 0,10'dan fazla altındaysa (K 0,70'in üstünde olsa bile) olay kaybı şüphesi vardır (§10.2).
- **Piksel kapsaması Kt** = OY'de Purchase "Tarayıcı" satırı ÷ sipariş. **Sunucu kapsaması Ks** = OY'de Purchase "Sunucu" satırı ÷ sipariş. Beklenen sıra Kt ≤ Ks ≤ K'dır, çünkü reklam engelleyici yalnız tarayıcıyı keser.
  - Kt ya da Ks tek başına 1,05'i geçiyorsa çift kaynak o kanaldadır: ikinci piksel ya da ikinci sunucu entegrasyonu.
  - Ks, R'nin belirgin üstündeyse sunucu olayları rıza sinyaline uymuyor olabilir. Örneğin R 0,60 iken Ks yaklaşık 1 çıkıyorsa §2.6'yı uygula.
- **Meta payı P** = RY'deki alışveriş sayısı (hesabın standart ilişkilendirme ayarıyla) ÷ sipariş sistemindeki sipariş. RY sonuçları OY sonuçlarından düşüktür [RESMİ], bu yüzden genellikle P < K olur. Tek haftalık P > 1 tek başına hata kanıtı değildir. Meta modellenmiş dönüşüm ekler (§5.4). Dönüşümün gösterim gününe mi dönüşüm gününe mi yazıldığı da doğrulanmamıştır, bu yüzden harcama dalgası haftalar arasında kayma yaratabilir.
  - P > 1 iki hafta üst üste görülürse ya da 28 günlük toplamda P > 1 ise ölçüm hatası şüphesi doğar (*sezgisel*). Ne yapılacağı §10.2'de.
- **Değer modu ve V:**
  - KDV, kargo ve indirim kontrolü oranla değil, **tek bir test siparişiyle** yapılır (§2.7). Sonuç değer modudur, ör. "KDV dahil, kargosuz".
  - **Değer oranı V** = RY'de alışveriş dönüşüm değeri ÷ RY'de alışveriş sayısı (aynı sütun seti ve ilişkilendirme ayarı) ÷ sipariş sistemindeki ortalama sepet. Ortalama sepet, test siparişinde bulunan değer moduyla hesaplanır. Sipariş sistemi UTM kaynağını kaydediyorsa paydayı yalnız `utm_source` fb/ig olan siparişlerden hesapla, böylece iki taraf aynı müşterilere bakar.
  - V yalnız haftalık izleme göstergesidir. Beklenen değer 1 civarıdır, tolerans ±%5'tir (*sezgisel*). Bant dışına çıkarsa test siparişini tekrarla. Test doğruysa fark, Meta'dan gelen müşterinin sepet farkıdır. Bu durumda alarmı kapat, ilk 4 haftanın V ortalamasını o hesabın tabanı yap ve sonraki haftalarda tabandan ±%5 sapmayı izle.
  - %10 ve %1 KDV'li ürünler karışık satılıyorsa KDV hariç ve dahil tutarlar arasındaki oran ürüne göre değişir. Değer modunu yine test siparişi belirler.
- **Toplam MER** (Modül 01 §3.1 ile aynı tanım) = dönemin toplam net geliri (KDV hariç sipariş tutarı, sipariş sisteminden) ÷ dönemin **tüm kanallardaki** toplam reklam faturası (Meta faturası + Google Ads + diğer reklam kanalları). İade ve iptal karşılığı başabaş tarafında katkıdan düşüldüğü için net gelirden ikinci kez düşülmez (Modül 01 §3.1). Gerçekleşen oran brifteki varsayımı belirgin aşıyorsa katkıyı güncelle.
  - **Başabaş MER** = 1 ÷ katkı marjı (Modül 01 §3.1).
  - Toplam MER reklamsız organik satışı da içerir. Bu yüzden yalnız trend göstergesidir ve kârlılık kanıtı değildir. Kârlılık kontrolü artımlı MER ile yapılır (Modül 01 §3.1).
  - **Meta MER** istenirse ayrı adla verilir: aynı net gelir ÷ yalnız Meta faturası. Başka kanal da çalışıyorsa Meta MER sağlık göstergesi olarak kullanılmaz, çünkü diğer kanalların getirdiği ciroyu Meta'ya yazar.

**Doldurulmuş örnek (*varsayım*; Modül 02 §1'deki mağaza, KDV hariç sepet 1.000 TL, sipariş başı katkı 414 TL = başabaş fatura CPA; başka reklam kanalı yok):**

| Girdi | Kaynak | Değer |
|---|---|---|
| RY harcaması / fatura | RY / fatura | 21.000 / 22.050 TL |
| Sipariş / KDV hariç net gelir | Sipariş sistemi | 150 / 150.000 TL |
| Reklam çerezi kabul oranı R | Çerez yönetim aracı | 0,93 |
| Veri kaynakları toplam Purchase (tekilleştirilmemiş) | OY | 266 → **K'ya girmez** (266 ÷ 150 = 1,77 yanlış alarm verirdi) |
| "Tüm Purchase" özel dönüşümü | OY | 141 → K = 0,94, R'ye yakın ✔ |
| Purchase Tarayıcı / Sunucu satırı | OY | 128 / 138 → Kt = 0,85, Ks = 0,92 ✔ |
| Alışveriş (7 gün tık + 1 gün gör) | RY | 70 → P = 0,47 ✔ |
| Bunun 7 gün tıklama kısmı | RY | 52 → görüntüleme payı %26 |
| Paid Social satın alma | GA4 | 38 |
| Değer modu | Test siparişi (§2.7) | KDV dahil, kargosuz |
| RY ort. alışveriş değeri ÷ KDV dahil kargosuz ort. sepet | RY / sipariş sistemi | 1.164 ÷ 1.200 → V = 0,97, ±%5 bandında ✔ |
| Toplam MER | Sipariş sistemi / fatura | 150.000 ÷ 22.050 = 6,8 |

**Okuma:**
- Fatura CPA, RY'nin saydığı sonuca göre 22.050 ÷ 70 = **315 TL**. 7 gün tıklamaya göre 424 TL, GA4'e göre 580 TL.
- Başabaş 414 TL bu aralığın içinde kalıyor. Doğru rakam bu tabloyla anlaşılamaz.
- Toplam MER 6,8, başabaş MER'in (1.000 ÷ 414 ≈ 2,42) belirgin üstünde. Bu iyi bir trend işaretidir ama organik satışı da içerir. Reklamın kârlı olduğunu tek başına göstermez. Kârlılığı artımlı MER (Modül 01 §3.1) ya da §11'deki test gösterir.
- **Karar:** Bütçeyi artırmadan önce artırımlılık testi yap (§11). O zamana kadar bütçeyi koru. Değer KDV dahil gittiği için RY ROAS eşiğini Piksel çarpanı 1,20 ile hesapla: RY'de başabaş ROAS ≈ 3,04 (Modül 01 §3.2, Modül 02 §1).

### 5.6 Kapıda ödeme ve havale/EFT siparişleri

**Sorun:** Bu siparişlerde Purchase, para alınmadan teşekkür sayfasında tetiklenir. Meta ödenmeyen, teslim alınmayan ya da onaylanmayan siparişi de satış sayar. Optimizasyon zamanla bu siparişleri veren kişilere kayabilir (*sezgisel*). Gönderilmiş bir Purchase geri alınamaz. Meta'nın standart olay listesinde iptal ya da iade olayı yoktur [RESMİ, standart olaylar].

**Haftalık takip (*türetilmiş* oranlar, sipariş sisteminden):**
- **Kapıda ödeme ret oranı** = teslim alınmayan ya da kapıda iptal edilen kapıda ödeme siparişi ÷ teslim süreci tamamlanan kapıda ödeme siparişi.
- **Havale onay oranı** = ödemesi gelen havale/EFT siparişi ÷ ödeme için tanınan süresi bitmiş havale/EFT siparişi (ödenmiş ya da ödenmemiş).
- İki oranı ayrıca Meta kaynaklı siparişler için hesapla. Kaynak için UTM kaydını, kanal kuponunu ya da sipariş sisteminin kaynak alanını kullan (§6, §7.4). Oranlar teslim döngüsü bittikten sonra kesinleşir, bu yüzden bir hafta gecikmeli okunur.
- Sonucu brifteki varsayımla karşılaştır (Modül 01 §1.2, soru 9).

**Eşik (*sezgisel*, hesabın kendi geçmişine göre ayarla):** Meta kaynaklı siparişlerde ret oranı %15'i aşıyorsa ya da Meta dışı siparişlerin oranından 5 puan veya daha fazla yüksekse oran "yüksek" sayılır. Havalede onaylanmayan sipariş oranı için de aynı eşik kullanılır.

**Yüksekse seçenekler (sırayla):**
1. **Varsayılan: Purchase'ı koru, kararları net satışla ver.** İşletme CPA'yı ödenen ya da teslim edilen siparişle hesapla: fatura ÷ Meta'ya atanan net sipariş. Katkıdaki ret karşılığını gerçekleşen oranla güncelle ve başabaşı yeniden hesapla (Modül 01 §3.1). Purchase'ı teşekkür sayfasından tamamen kaldırma. Bu, Piksel–CAPI tekilleştirmesini ve gerçek zamanlı sinyali bozar (*sezgisel*).
2. **Geliştirici varsa onaylı sipariş olayı:** Havalede ödeme onayında, kapıda ödemede teslimde CAPI'den ayrı bir özel olay gönder, ör. `OnayliSiparis`. Sipariş anında `fbp`, `fbc`, IP, user agent ve müşteri bilgisini siparişe kaydet ki sonradan gönderilen olay eşleşsin. Bu adım hukuki ön koşula tabidir (§2 başı). Olayı önce raporlama için topla. Haftalık hacim yeterliyse (Modül 02 F5) bu olaya dayanan özel dönüşüme optimize eden yeni bir kampanyayı, mevcut kampanyaya karşı A/B testiyle dene (*sezgisel*).
   - **Zaman sınırları:** Web sitesi olayında `event_time` gönderim anından en fazla 7 gün eski olabilir [RESMİ, §2.2]. Olayı gerçekleştiği anda gönder ve olay zamanı olarak onay ya da teslim anını yaz. Sipariş tarihini yazarsan 7 günü aşan teslimlerde istek reddedilir. Ayrıca tıklamadan 7 günden sonra gerçekleşen teslim, 7 gün tıklama penceresine girmez ve reklama yazılmaz (*türetilmiş*, §5.1).
   - Olayın hangi `action_source` ile gönderileceği ve bu olaya optimizasyonun hesapta açılıp açılmadığı doğrulanmalıdır.

---

## 6. UTM şablonu

**GA4 kanal kuralı [RESMİ, Google]:**
- "Paid Social" için kaynak GA4'ün sosyal site listesinde olmalı **ve** medium `^(.*cp.*|ppc|retargeting|paid.*)$` ile eşleşmeli. Medium `social` olursa trafik Organic Social'a düşer.
- Google'ın kaynak listesinde `fb`, `ig`, `facebook`, `instagram` ve `messenger` sosyal kategoride. `msg`, `an` ve `th` listede yok (18.10.2026'da kontrol edildi). Bu değerlerle gelen paid trafik büyük olasılıkla "Paid Other"a düşer.

**Şablon (reklam düzeyinde Takip > URL parametreleri alanına yapıştır):**

```
utm_source={{site_source_name}}&utm_medium=paid_social&utm_campaign={{campaign.name}}&utm_term={{adset.name}}&utm_content={{ad.name}}&utm_id={{campaign.id}}
```

- `{{site_source_name}}` fb, ig, msg, an ya da th döndürür [RESMİ]. Böylece Facebook ile Instagram GA4'te ayrı görünür. Müşteri tek satır istiyorsa `utm_source=facebook` yaz.
- **Doldurulmuş sonuç** (*varsayım*, kimlik uydurma): `https://ornekmagaza.com/elbise?utm_source=ig&utm_medium=paid_social&utm_campaign=2610_SAT_WEB_TUM_DEG-KB_Kadin-Elbise_TR&utm_term=GENIS_25plus-MUSharic_PUR_APL_ana&utm_content=K017_VID-9x16_sorun-cozum_ofis-calisani_v2&utm_id=120200000000001`

**Kurallar [RESMİ]:**
- Ad tabanlı parametreler reklamın ilk yayındaki adını alır, bu yüzden adları yayından önce koy (Modül 02 §10). Parametreleri yayından sonra değiştirmek öğrenmeyi yeniden başlatabilir.
- Bu alan, site URL'sindeki aynı adlı parametrenin yerine geçer ve yalnız ilk açılan sayfada geçerlidir. Boş değerli parametre bırakma.
- Instagram koleksiyon reklamlarında dinamik parametre çalışmaz.

**Kontrol (*sezgisel*):** İlk hafta GA4'te Trafik edinme raporunda kanalın "Paid Social" göründüğünü doğrula. WhatsApp sohbeti UTM taşımaz, o satışlar §7'deki yöntemlerle ölçülür.

---

## 7. WhatsApp ve Instagram Direct satışlarını ölçme

### 7.1 Yöntem merdiveni

| Basamak | Ne zaman | Satış Meta'ya döner mi? |
|---|---|---|
| A. Google E-Tablolar satış defteri + kampanya kodu | Her zaman, ilk gün | Hayır, yalnız senin hesabın için |
| B. WhatsApp Business uygulamasında paylaşım + hazır sipariş etiketleri | Uygulamayı kullanan küçük işletme | Evet |
| C. Mesajlaşma ortağı ya da işletme mesajlaşması CAPI'si (Cloud API) | Çok temsilci, CRM, otomasyon | Evet, olay bazında |

A her zaman tutulur, B ya da C onun üstüne eklenir (*sezgisel*). Satış Meta'ya dönmezse sistem satın alana değil, mesaj atmaya yatkın kişiye optimize etmeye devam eder [PRATİSYEN].

### 7.2 WhatsApp Business uygulamasıyla alışveriş sinyali [RESMİ]

1. Uygulamada **Araçlar > Müşterilerinizin hareketleri** altında Meta ile paylaşımı aç. Menü adı sürüme göre değişebilir.
2. Meta dört hazır etiketin hepsini alışveriş olayı sayar: "Yeni Sipariş", "Ödeme Bekleniyor", "Ödendi" ve "Sipariş tamamlandı". Özel etiketli sohbetler ilişkilendirilmeyebilir. "Siparişler" özelliğinde sipariş oluşturmak ya da durumu güncellemek (ör. "Ödendi") de alışveriş sayılır [RESMİ].
3. Sohbet WhatsApp'a tıklama reklamından başlamış ve reklam tıklamasından sonraki 7 gün içinde etiketlenmiş olmalı.
4. Son 30 günde en az 10 alışveriş olayı birikince "Mesajlaşma yoluyla alışveriş sayısını en üst seviyeye çıkarın" hedefi seçilebilir. Birden fazla Sayfa varsa her Sayfa bu koşulu ayrı sağlar.

**Etiket kuralı (*sezgisel*):** Meta dört etiketin hepsini satış saysa da her satışa yalnız **bir** etiket verilir:
- Ödeme alındığında **"Ödendi"** ver. Kapıda ödemede bu an teslim anıdır.
- "Yeni Sipariş" ve "Ödeme Bekleniyor" etiketlerini reklam sohbetlerinde kullanma. Bu iki etiket ödenmemiş ya da vazgeçilmiş siparişi de Meta'ya satış olarak gönderir ve optimizasyon sinyalini kirletir.
- Etiket ile "Siparişler" özelliğini aynı satış için birlikte kullanma, ikisinden birini seç. Aynı sohbeti önce bir etiketle sonra başka etiketle işaretleme. Bu durumların iki olay üretip üretmediği doğrulanmamıştır.
- **Tek istisna:** Ödemesi tıklamadan 7 günden sonra gelen hizmet satışında Modül 01 §1.4'teki (#18) kural geçerlidir. Müşteri işi kabul ettiğinde tek etiket olarak "Ödeme Bekleniyor" verilir ve sonra "Ödendi"ye çevrilmez. Vazgeçilen işler satış defterinde (§7.3) izlenir.
- Kapıda ödemeli bir sipariş tıklamadan 7 günden sonra teslim edilirse "Ödendi" etiketi artık alışveriş olarak ilişkilendirilmez. Bu kabul edilen bir kayıptır. Satış defterinde yine yazılır.

**Eğer** 30 günde 10 etiketli satış yoksa **ise** konuşma hedefinde kal ve etiketlemeye devam et (Modül 02 §2.1).

**API yolu (C) [RESMİ]:** `action_source=business_messaging`, `messaging_channel=whatsapp` ve mesajın referral nesnesinden gelen `ctwa_clid` gönderilir. Geliştirici belgesine göre **İşletme mesajlaşması CAPI'si** ile satışa optimizasyon Messenger ve WhatsApp'ta yapılabilir, Instagram'da yapılamaz. Bu sınır yalnız bu API için geçerlidir. Instagram Direct'te Profesyonel Gelen Kutusu etiketleriyle alışveriş optimizasyonu mümkündür (§7.6). On-Premises API desteği 23.10.2025'te bitti.

### 7.3 Google E-Tablolar satış defteri (şablon)

Veri minimizasyonu için yalnız telefonun son 4 hanesini tutman ve tabloya erişimi satış ekibiyle sınırlaman önerilir (*sezgisel*). Bu bir yasal zorunluluk ifadesi değildir. Ne tutulacağına müşterinin aydınlatma metni ve hukukçusu karar verir (Modül 09 §5.6).

| tarih | tel_son4 | kaynak | kod | ürün | durum | tutar_kdv_dahil | meta_etiketi |
|---|---|---|---|---|---|---|---|
| 14.10.2026 | 4567 | meta_reklam | K017 | Elbise-M | odendi | 1.200 | Ödendi ✔ |
| 14.10.2026 | 8812 | organik_ig | — | Etek-S | kayip | 0 | — |
| 15.10.2026 | 3021 | meta_reklam | K021 | Elbise-L | odeme_bekliyor | 1.350 | — |

- `kaynak`: meta_reklam, meta_reklam_ig_dm, organik_ig, google, tavsiye, bilinmiyor.
- `durum`: yeni, teklif, odeme_bekliyor, odendi, kayip, iade.
- `meta_etiketi`: satışın WhatsApp'ta da hazır etiketle işaretlendiğini gösterir. Yalnız `durum` = odendi olduğunda doldurulur, değeri de "Ödendi"dir (§7.2 etiket kuralı). Etiket kapsaması bu sütundan hesaplanır.

### 7.4 Kampanya kodu, kupon ve "Bizi nereden duydunuz?"

Bu yöntemler *sezgisel*, forumdaki reklamveren önerilerine dayanır [TOPLULUK].
- **Önceden doldurulmuş mesaj:** Her reklamın karşılama metnine ürün ya da kreatif kodu koy, örneğin "Merhaba, K017 elbise hakkında bilgi almak istiyorum." Kullanıcı metni silebilir, bu yüzden kodlu sohbet sayısı alt sınırdır.
- **Kanal başına benzersiz kupon:** Yalnız Meta reklamında görünen bir kod, örneğin "IG10". Herkes kupon kullanmadığı için bu da alt sınır verir. Kod kupon sitelerine sızarsa sonraki veriyi ayrı oku. Mağaza ve telefon satışında kod, kasada "kaynak" alanına girilir.
- **Kaynağı bilinmeyen sohbet:** İlk yanıtta seçenekli soru sor: "Bizi nereden gördünüz? 1) Instagram reklamı 2) Instagram sayfası 3) Google 4) Tavsiye". Bu soru artırımlılığı ölçmez, yalnız sağlama yapar.

### 7.5 Formüller ve doldurulmuş örnek (*varsayım*)

Bir hafta: RY harcaması 7.000 TL → fatura 7.350 TL. RY 210 konuşma gösteriyor. Satış defterinde reklam kaynaklı 33 ödenmiş satış var, 29'u WhatsApp'ta "Ödendi" etiketli.
- **Sohbet → satış oranı** = defterdeki 33 ÷ RY'deki 210 = **%15,7**
- **İşletme CPA** = fatura 7.350 ÷ defterdeki 33 ≈ **223 TL**. Başabaşla karşılaştır (Modül 01 §3.6).
- **Etiket kapsaması** = 29 ÷ 33 = **%88**. %90'ın altı etiketleme disiplini sorunudur (*sezgisel*).
- Bu hızla 30 günde yaklaşık 124 etiketli satış birikir (29 × 30 ÷ 7). 10 eşiği aşıldığı için alışveriş hedefi denenebilir.

### 7.6 Instagram Direct satışları [RESMİ]

Instagram DM'den satan butiklerde satış, Instagram'ın Profesyonel Gelen Kutusu üzerinden Meta'ya döndürülebilir. §7.2'deki API sınırı bu yolu kapsamaz.

1. **Paylaşımı aç:** Instagram uygulamasında profil > sağ üst menü > **Ayarlar ve gizlilik > İşletme araçları ve kontrolleri > İşletme hareketleri için veri paylaşımı** yolunu izle. "Meta'nın reklamları iyileştirmek için işletme hareketlerini kullanmasına izin ver" düğmesini aç. Menü adları sürüme göre değişebilir.
2. **Hazır etiketleri kullan:** Meta yalnız uygulamanın önceden tanımlı alışveriş etiketlerini hesaba katar: "Sipariş Verildi", "Ödendi", "Gönderildi" (İngilizce arayüzde Ordered, Paid, Shipped). Başka etiketlerle paylaşılan alışverişler ilişkilendirilmeyebilir.
3. **Uygunluk:** Instagram Direct'e yönlendiren reklamlarda "Mesajlaşma yoluyla alışveriş" hedefi için son 30 günde Profesyonel Gelen Kutusu'ndan en az 10 alışveriş olayı paylaşılmış olmalı. Türkçe sayfa bu cümlede "potansiyel müşteri olayı" diyor, İngilizce sürüm "purchase events" diyor. Bu çeviri hatası sayıldı ve İngilizce sürüm esas alındı.
4. **Messenger ve Instagram Direct aynı kampanyada:** Bu kurgu için Messenger'da alışveriş optimizasyonuna uygun olmak gerekir.

**Etiket kuralı (*sezgisel*, §7.2 ile aynı gerekçe):** Satış başına yalnız "Ödendi" kullan. Kapıda ödemede bu etiket teslimde verilir. "Sipariş Verildi" ödenmemiş siparişi de satış sayar. "Gönderildi"yi aynı satışa ikinci etiket olarak ekleme. Satış defteri (§7.3) aynı şekilde tutulur, `kaynak` sütununa `meta_reklam_ig_dm` yazılır. WhatsApp'taki 7 günlük etiketleme sınırının Instagram'da da geçerli olup olmadığı Meta sayfasında yazmıyor. Yine de satışı aynı gün etiketle.

---

## 8. Form satışlarını ölçme (hızlı form ve site formu)

### 8.1 Hızlı form: kurulum basamakları [RESMİ]

**Basamak 0, en basit yol: Potansiyel Müşteri Merkezi (kurulum yok).**
- Meta Business Suite'te sol menüden **Potansiyel Müşteri Merkezi**'ni aç (ya da Tüm araçlar > Hedef kitleyle etkileşimde bulun).
- Meta'ya göre Potansiyel Müşteri Merkezi kullanılıyorsa hızlı formdan gelen potansiyel müşteriler otomatik gönderilir ve ayrı kurulum gerekmez.
- Satış ekibi her kişinin aşamasını Meta içinde günceller. Varsayılan aşamalar "Giriş", "Uygun", "Dönüşüm Gerçekleştirdi", "Kayıp" ve "Uygun Değil"dir. Özel aşama da eklenebilir. Ardışık görünümde kişiler sütunlar arasında sürüklenir.
- Kişisel veri Meta dışında ikinci bir yere kopyalanmaz. Bu, veri minimizasyonu açısından da en sade yoldur (*sezgisel*).
- **Eğer** ekip zaten tabloyla ya da bir CRM'le çalışıyorsa **ise** aşağıdaki Google E-Tablolar yoluna ya da §8.2'ye geç. Aynı form için iki ayrı aşama kaydı tutma.

**Basamak 1: Google E-Tablolar ile CRM CAPI'si.** Ekip tabloyla çalışıyorsa bu yolu kullan.

1. Meta Business Suite > Tüm araçlar > **Hızlı formlar** > **+ CRM Bağla** yolunu izle. Google E-Tablolar'ı seç ve Google hesabınla izin ver. Microsoft Excel seçeneği de var.
2. Tabloyu ve formu seç, sonra işletme portfolyosunu ve veri setini seç. **Test potansiyel müşterisini gönder** ile bağlantıyı doğrula.
3. Tabloda `lead_status` sütunu oluşur. Değerleri: "Giriş", "Uygun", "Dönüşüm Gerçekleştirdi", "Kayıp", "Uygun değil". Satış ekibi bu sütunu güncel tutar, Meta her güncellemede CAPI olayı üretir.
4. "Gelecekteki formlar için otomatik olarak entegrasyon oluştur" kutusunu işaretle.
5. **Veri koruma (*sezgisel*, Modül 09 §5.6):** Tablo ad, telefon ve e-postanın tamamını içerir. Paylaşımı yalnız adı geçen satış ekibi hesaplarıyla sınırla, "bağlantıya sahip herkes" paylaşımını kapat. Tabloyu indirme ve kopyalama yetkisini kısıtla. Saklama süresini aydınlatma metniyle uyumlu yaz. Süresi dolan satırları sil ya da anonimleştir. Bu yol da hukuki ön koşula (§2 başı) tabidir.

**Neden ilk gün kurulmalı:** Hızlı form verisi gönderimden sonra en fazla 90 gün indirilebilir [RESMİ]. Forma kanal ve kampanya takip parametresi de eklenebilir. Kullanıcı bunları görmez, değerler tabloya gider [RESMİ]. Değerleri Modül 02 §10 standardına göre doldur.

### 8.2 Gerçek CRM ve "nitelikli potansiyel müşteri" hedefi [RESMİ]

- Bağlantı Olay Yöneticisi > Veriyi bağla > CRM yolundan kurulur. HubSpot, Salesforce, Zoho, Zapier, Make gibi ortaklar var. Öğrenme döneminde her durum değişikliği ayrı olay olarak gönderilir: iletişime geçildi, nitelikli, randevu, satış.
- Nisan 2026'dan beri yeni kampanyada "nitelikli potansiyel müşteri" hedefi CAPI entegrasyonu olmadan seçilemiyor. Mevcut kampanyalar Ağustos 2026'dan beri etkileniyor.
- Geliştirici belgesindeki koşullar: ayda en az 200 potansiyel müşteri; optimize edilen aşamanın 28 gün içinde gerçekleşmesi ve oranının %1–%40 arasında olması; en az günde bir yükleme; Meta'nın `lead_id` değerinin CRM'de saklanması. Öğrenme 2–4 hafta sürer.
- Meta'ya göre bu kurgu nitelikli potansiyel müşteri başına maliyeti %21 düşürüyor. Bu Meta'nın kendi iddiasıdır, yöntemi verilmemiş.

**Eğer-ise:**
- Ayda 200'den az potansiyel müşteri varsa aşamaları yine gönder ve raporlamada kullan. Hedef "potansiyel müşteri" kalır.
- Kalitesiz form sorununda şu sırayla ilerle: (1) "Daha yüksek satın alma niyeti" form türü, (2) eleme soruları, (3) CRM aşamaları, (4) hacim yeterliyse nitelikli hedef (*sezgisel*).
- Site formunda aşamalar e-posta ya da telefon hash'iyle CAPI'den gönderilebilir. Nitelikli hedefin site formunda çalışıp çalışmadığı kaynaklarda çelişiyor, hesapta doğrula.

### 8.3 Formüller ve doldurulmuş örnek (*varsayım*)

Bir ay: RY harcaması 30.000 TL → fatura 31.500 TL. 300 potansiyel müşteri gelmiş, 120'si "Uygun", 24'ü "Dönüşüm Gerçekleştirdi".
- RY CPL = 30.000 ÷ 300 = 100 TL. **Fatura ile nitelikli maliyet** = 31.500 ÷ 120 = 262,5 TL.
- **Fatura ile satış başı maliyet** = 31.500 ÷ 24 = **1.312,5 TL**. Karar bu rakamla verilir.
- Satış oranı %8 (%1–40 aralığında) ve hacim ayda 200'ün üstünde. CAPI bağlıysa nitelikli hedef yeni kampanyada denenebilir.

---

## 9. Telefon ve mağaza satışları (çevrimdışı) [RESMİ]

- Olay Yöneticisi'nde veri seti oluştur, reklam hesabına bağla. Satışları CAPI, bir ortak ya da elle dosya yüklemeyle gönder. Eski Offline Conversions API Mayıs 2025'te kapandı.
- "Site ve mağaza" dönüşüm konumunda çevrimdışı olaylar Satış kampanyasında optimizasyona da girer.
- Önerilen gönderim süresi 7 gün, azami 62 gündür. Geç yüklemede geçmiş günlerin sonuçları sonradan değişir.
- Kayıtta e-posta ya da telefon yoksa eşleşme çok düşük kalır.
- **Kural (*sezgisel*, ihtiyat):** Hukuki ön koşul (§2 başı) sağlanmadan ve hukukçunun yazılı onayı olmadan çevrimdışı yükleme yapma. Onay hangi dayanağa izin veriyorsa (ör. kişi başına kayıtlı açık rıza) yalnız o dayanağı olan satırlar yüklenir. Satır seçimi Modül 03 §4.4'teki mantıkla yapılır.
- Asgari yöntem: satış defteri (§7.3), kanal kuponu (§7.4), kasada "Bizi nereden duydunuz?" alanı.

---

## 10. Veri kalite kontrolleri

### 10.1 Rutin (*sezgisel*)

| Sıklık | Kontrol |
|---|---|
| Günlük | Son 24 saatte Purchase ya da Lead akıyor mu? Tanılar'da kırmızı sorun var mı? |
| Haftalık | K, Kt, Ks, P, V ve rıza oranı R (§5.5); kapıda ödeme ret ve havale onay oranı (§5.6); EMQ; GA4 Paid Social satırı; WhatsApp ve Instagram etiket kapsaması; potansiyel müşteri aşamalarının güncelliği |
| Her site değişikliğinden sonra | §2.7 test protokolü |
| Aylık | Trafik izin listesi; piksele erişimi olan kişi ve ortaklar; form verisinin yedeği; kupon sızıntısı |
| Çeyreklik | Artırımlılık testi ya da katsayı güncellemesi (§11) |

### 10.2 Alarm tablosu (eğer → ise)

| Eğer | İse |
|---|---|
| Veri kaynakları ekranındaki toplam Purchase sayısı siparişin yaklaşık 2 katı | **Alarm değil.** Tarayıcı ve sunucu satırlarının toplamı tekilleştirilmez [RESMİ]. K'yı §5.5'teki gibi hesapla |
| K > 1,05 (tekilleştirilmiş, §5.5) | Çift sayım şüphesi. İkinci piksel ya da elle eklenmiş olay ara: tema, GTM, kodsuz olaylar, ikinci sunucu entegrasyonu. Kt ve Ks'ye ayrı bak, hangisi 1,05'i geçiyorsa çift kaynak o kanaldadır. Bir test siparişiyle Olayları Test Et'te tekilleştirmeyi doğrula (§2.7) |
| Tek haftalık P > 1 | Alarm değil. Harcama dalgası, tarih ataması ya da modellenmiş dönüşüm olabilir. Bir hafta daha izle |
| P > 1 iki hafta üst üste ya da 28 günlük toplamda P > 1 (*sezgisel*) | Ölçüm hatası şüphesi. Önce "K > 1,05" satırındaki kontrolü yap. Çift kaynak bulunursa düzelt ve K'yı yeniden hesapla. Bütçe kararlarını **yalnız düzeltilmiş K de 1'in üstündeyse** dondur. K ≤ 1 ise fark büyük olasılıkla ilişkilendirmeden ya da modellemeden gelir. Bu durumda dondurma, kararları işletme CPA ile ver ve RY sayılarını şişik say |
| K < 0,70 ya da K, R'nin 0,10'dan fazla altında (*sezgisel*) | Önce rıza oranı R'ye bak. K ile R arasındaki fark 0,10'dan azsa bu normaldir: rıza reddi açıklıyordur, çerez bandına dokunma (§2.6). K, R'nin 0,10'dan fazla altındaysa ya da R bilinmiyorsa CAPI kopmuş, jetonun süresi dolmuş ya da izin listesi bir alan adını engelliyor olabilir. Tanılar'a bak, §2.7'yi uygula |
| Ks, R'nin belirgin üstünde (ör. R 0,60 iken Ks yaklaşık 1) | Sunucu olayları rıza sinyaline uymuyor olabilir. §2.7'deki rıza testini yap. Doğrulanırsa §2.6 kuralını uygula, altyapıya ve müşterinin hukukçusuna yazılı bildir |
| V, ±%5 bandının ya da hesabın kendi tabanının dışında (§5.5) | Test siparişini tekrarla (§2.7): KDV, kargo, indirim ve para birimini kontrol et. Test doğruysa fark sepet farkıdır, alarmı kapat ve tabanı güncelle |
| Meta kaynaklı kapıda ödeme ret oranı ya da onaylanmayan havale oranı yüksek (§5.6) | §5.6'daki seçenekleri sırayla uygula |
| Bir olay 24 saattir sıfır ama sipariş sürüyor | Entegrasyon kopmuş ya da izin listesi ödeme veya teşekkür sayfasının alan adını engelliyor (§2.1). Aynı gün düzelt, bu sürede kampanyayı değiştirme |
| Tanılar'da başka alan adından olay geliyor | Alan adı senin altyapın, ödeme sağlayıcın ya da teşekkür sayfanınsa izin listesine ekle. Tanımadığın bir siteyse ekleme, izin listesi onu zaten engeller (§2.1) |
| GA4'te Meta trafiği Organic Social ya da Referral'da | UTM eksik ya da medium yanlış (§6) |
| Mart 2026 sonrası "tıklama" dönüşümleri düştü | Önce sınıflandırma değişikliğini kontrol et (§5.3), performans kararı verme |

---

## 11. Artırımlı etki: pratik yöntemler

### 11.1 Yöntem seçimi

| Durum | Yöntem | Süre ve eşik | Ne verir |
|---|---|---|---|
| Deneyler'de Dönüşüm Yükselişi görünüyor, CAPI var | Meta Dönüşüm Yükselişi | Güven ≥ %90 ise güvenilir [RESMİ]. Pratisyene göre en az 7 gün, varsayılan kontrol %10 [PRATİSYEN] | Kişi bazlı artımlı dönüşüm |
| İki strateji kıyaslanacak (ör. Advantage+ ile manuel) | Hariç tutmalı A/B testi | Sonuç strateji başına 100 olaydan sonra görünür, testin bitmesini bekle [RESMİ] | Artımlı CPA farkı |
| Lift testi yok, hızlı bakış lazım | Artımlı ilişkilendirme sütunu | Hemen [RESMİ] | Meta'nın kendi modeli, bağımsız doğrulama değil |
| Satış il bazında sipariş sisteminden görülüyor | Coğrafi kapatma testi (§11.3) | 2–4 hafta test + 1–2 hafta gözlem [PRATİSYEN, sezgisel] | Gerçek kasa farkı |
| Tek bölge, çok küçük işletme | Zaman bazlı aç-kapa | 2 hafta kapalı ile önceki 2 hafta (*sezgisel*) | Kaba sinyal, mevsime açık |
| Her durumda | MER trendi + "Bizi nereden duydunuz?" | Haftalık | Sağlama |

- Meta işe hesap düzeyinde bir testle başlamayı öneriyor. Minimum harcama kurulum ekranında hesaba göre gösterilir, genel eşik yayımlanmıyor [RESMİ].
- Haftada 50–100'den az dönüşüm alan hesaplarda kısa testlerin istatistiksel gücü yetmeyebilir [PRATİSYEN, geo test satıcısı]. Meta'nın açık kaynak GeoLift paketi aynı mantığı daha titiz uygular, analist gerektirir [RESMİ].
- Testi 11.11, Kasım indirimleri ve bayram haftalarında yapma (*sezgisel*).
- **Onay kuralı (*sezgisel*):** Satış kaybını göze alan her test müşterinin yazılı onayıyla başlar. Bunlar coğrafi kapatma, zaman bazlı aç-kapa ve Dönüşüm Yükselişi'nin kontrol grubudur. Onay olmadan testi başlatma. Coğrafi testte beklenen kaybı §11.3'teki 0. adımla hesapla.

### 11.2 Formüller (*türetilmiş*)

- **Artımlı sipariş** = kapatılan bölgede beklenen sipariş − gerçekleşen sipariş.
- **Artımlı CPA** = kapatılan bölgede harcanmayan fatura ÷ artımlı sipariş. **iROAS** = artımlı KDV hariç ciro ÷ harcanmayan fatura.
- **Artırımlılık katsayısı c** = artımlı sipariş ÷ Meta'nın **ön dönemde** B için raporladığı sipariş miktarının test süresine çevrilmiş hâli. Formül: c = artımlı sipariş ÷ (ön dönemde B için RY'nin raporladığı haftalık ortalama alışveriş × test haftası). Test süresince B'de reklam kapalı olduğu için Meta o dönem B'ye 0 yazar, bu yüzden payda ön dönemden alınır. RY sayısı hesabın standart ilişkilendirme ayarıyla okunur, ör. 7 gün tıklama + 1 gün görüntüleme. Ayar c'nin yanına yazılır. B'nin RY sayısı Kırılım > Yayına göre > Bölge ile alınır.
- **Düzeltilmiş CPA** = Meta CPA ÷ c. c yalnız aynı ilişkilendirme ayarıyla okunan Meta CPA'ya uygulanır. Sonraki haftalarda bunu kullan, c'yi 3–6 ayda bir yeniden ölç (*sezgisel*).

### 11.3 Coğrafi kapatma testi: adımlar ve örnek

**Adımlar (*sezgisel*, GeoLift mantığının basit hâli):**
0. **Müşteri onayı ve beklenen kayıp.** Testten önce müşteriye şu iki rakamı yazılı sun ve onay al:
   - Beklenen kayıp satış, üst sınır = B için RY'nin ön dönemde raporladığı haftalık ortalama alışveriş × test haftası (*türetilmiş*). Meta'nın sayısı gerçek artımlı etkinin üstünde olabildiği için bu kötümser bir tahmindir. Kayıp katkı = bu sayı × sipariş başı katkı.
   - Tasarruf = B'nin haftalık ortalama faturası × test haftası.
   - Onay gelmezse testi başlatma. Daha küçük bir B grubu ya da daha kısa bir test öner.
1. Sipariş sisteminden son **8 haftanın** (ön dönem) siparişlerini teslimat iline göre çıkar. Ön dönem her zaman 8 haftadır, bu modülde başka süre kullanılmaz.
2. İlleri toplam sipariş ve haftalık seyir bakımından benzer iki gruba ayır: A (reklam açık kalır), B (reklam kapanır). İstanbul, Ankara ve İzmir'i teste katma, oralarda reklam açık kalsın. Bu iller tek başına grupları dengesizleştirir.
3. Ön dönemin her haftası için B ÷ A oranını hesapla, en düşük ve en yüksek oranı not et. Aynı dönemde B için RY'nin raporladığı haftalık alışverişi de not et (c'nin paydası, §11.2).
4. Test döneminde B illerini tüm setlerde konum hariç tutmayla kapat. Toplam bütçeyi B'nin harcama payı kadar düşür ki A'ya fazladan bütçe gitmesin.
5. 3 hafta test, 1 hafta gözlem. Bu sürede kreatif, teklif ve yapı değişmez.

**Sızıntı ve genelleme uyarıları:**
- Meta'nın konum hedeflemesi bir ilde yaşayanlara ek olarak oraya yakın zamanda gitmiş kişileri de kapsar. Reklam, hedeflenen bölgenin dışındaki kişilere de ulaşabilir [PRATİSYEN, Jon Loomer]. Hariç tutulan B'deki bazı kişiler reklamı yine görebilir. B'li olmayan biri de B'ye teslimat isteyebilir: teslimat ili reklamı gören kişinin bulunduğu ilden farklı olabilir. Bu sızıntı artımlı etkiyi olduğundan küçük gösterir, c'yi büyük olasılıkla aşağı çeker (*sezgisel*). Hediye ve iş yeri teslimatı yüksek kategorilerde sonucu ihtiyatla oku.
- c yalnız teste katılan illere genellenir. İstanbul, Ankara ve İzmir teste katılmadığı için bu illerde c ayrıca doğrulanmalıdır, ör. o illerden birini sonraki testte kendi benzeriyle eşleştir. Hesabın tamamına uygulanan c bir tahmindir. Bunu raporda belirt.

**Doldurulmuş örnek (*varsayım*; hesabın standart ayarı 7 gün tıklama + 1 gün görüntüleme):**
- Ön dönem (8 hafta, iki grupta reklam açık): A 1.040, B 960 sipariş → r = 960 ÷ 1.040 = **0,923**. Haftalık oranlar 0,88–0,96 arasında. RY bu 8 haftada B için 360 alışveriş raporladı, yani haftada ortalama 45.
- 0. adım: Beklenen kayıp üst sınırı = 45 × 3 = 135 sipariş → kayıp katkı 135 × 414 ≈ 55.890 TL. Tasarruf = 3 × 15.000 TL RY harcaması × 1,05 = 47.250 TL fatura. Müşteri en kötü durumda yaklaşık 8.640 TL net kaybı onayladı.
- Test (3 hafta, B'de reklam kapalı): A 400, B 300 sipariş.
- B'de beklenen sipariş = 400 × 0,923 = 369 → **artımlı sipariş** = 369 − 300 = **69**. Test oranı 300 ÷ 400 = 0,75, ön dönem aralığının dışında. Yani fark gürültü değil (*sezgisel* kontrol).
- B'de harcanmayan tutar 3 × 15.000 TL RY harcaması = 45.000 TL → fatura 47.250 TL.
- **Artımlı CPA** = 47.250 ÷ 69 ≈ **685 TL**. c'nin paydası = 45 × 3 = 135 → **c** = 69 ÷ 135 ≈ **0,51** (7 gün tıklama + 1 gün görüntüleme ile).
- **Karar:** Artımlı CPA 685 TL, başabaş 414 TL'nin üstünde. Meta CPA'sı (aynı ayarla) 350 TL görünse de düzeltilmiş CPA 350 ÷ 0,51 ≈ 686 TL'dir. Test edilen illerde bütçe artırma. Önce yeniden pazarlama ve görüntüleme ağırlıklı setleri, kreatifi ve teklifi gözden geçir (Modül 02–04). Büyük üç il için c doğrulanana kadar bu katsayıyı orada kesin kabul etme.
- **Eğer** test oranı ön dönem aralığının içinde kalırsa **ise** sonuç "belirsiz" sayılır. Testi uzat ya da grupları büyüt, bu sonuçla karar verme.

---

## Doğrulanması gerekenler

- **Meta destekli tek tık CAPI:** Türkiye'deki her hesapta görünüp görünmediği (*hesaba göre değişebilir*).
- **WhatsApp Business uygulaması:** "Müşterilerinizin hareketleri" menüsünün ve hazır etiketlerin Türkçe adları. Meta Türkiye için kısıt yazmıyor.
- **Mesajlaşma yoluyla alışveriş hedefi:** Aynı Meta sayfası bir yerde "satış veya etkileşim", başka yerde "potansiyel müşteriler veya etkileşim" amacı diyor.
- **Nitelikli potansiyel müşteri:** Site formunda çalışıp çalışmadığı ve Google E-Tablolar entegrasyonunun uygunluk eşiği.
- **Reklam Yöneticisi'nin tarih ataması:** API'de gösterim günü ve dönüşüm günü seçenekleri var. Arayüzün varsayılanı resmi kaynakta bulunamadı.
- **Mart 2026 tıklama tanımı:** Bütün hesaplara uygulandı mı? Meta bazı hesapların eski tanımda olabileceğini yazıyor.
- **Dönüşüm Yükselişi ve artımlı ilişkilendirme:** Hesapta görünürlük ve minimum harcama. Üçüncü taraf tahminleri çelişiyor.
- **EMQ bantları:** Pratisyen kuralıdır, Meta eşiği değildir.
- **Ticimax:** Tekilleştirme yöntemi destekten teyit edilmeli.
- **Altyapının değer alanı:** KDV dahil mi, kargo dahil mi, iade düşülüyor mu? Her hesapta test siparişiyle kontrol et (§2.7). V yalnız izleme içindir.
- **"Tüm Purchase" özel dönüşümü:** Kuralsız oluşturulabiliyor mu? Oluşturulmadan önceki günleri sayıyor mu? Arayüzde doğrulanmalı (§5.5).
- **Altyapıların rıza uyumu:** ikas, Ticimax, Shopify, WooCommerce eklentisi ve Meta destekli CAPI'nin sunucu olaylarında çerez rızasına uyup uymadığı her müşteride destekten yazılı teyit edilmeli ve rıza testiyle (§2.7) doğrulanmalı.
- **WhatsApp ve Instagram etiketleri:** Etiket değişince (ör. "Yeni Sipariş" → "Ödendi") ya da etiket ve "Siparişler" özelliği birlikte kullanılınca aynı satışın iki alışveriş olayı olarak sayılıp sayılmadığı. Instagram'daki hazır etiketlerin Türkçe arayüzdeki adları ve 7 günlük etiketleme sınırının Instagram'da geçerli olup olmadığı.
- **Instagram Direct uygunluğu:** Türkçe yardım sayfası "potansiyel müşteri olayı", İngilizce sayfa "purchase events" diyor. İngilizce esas alındı (§7.6).
- **Onaylı ya da teslim edilen sipariş olayı:** Ayrı bir özel olaya (ya da ona dayanan özel dönüşüme) optimize edilip edilemeyeceği ve olayın hangi `action_source` ile gönderilmesi gerektiği (§5.6).
- **Potansiyel Müşteri Merkezi:** Aşama güncellemelerinin "nitelikli potansiyel müşteri" hedefi için CAPI entegrasyonu yerine geçip geçmediği (§8.2).
- **Yayındaki setin ilişkilendirme ayarı:** Değiştirmenin öğrenmeyi sıfırlayıp sıfırlamadığı ve etkileşim penceresinin ayrı seçilebildiği hesaplar (§5.2).
- **Bölge kırılımı:** RY'nin Bölge kırılımındaki alışveriş sayısının coğrafi testte payda olarak ne kadar güvenilir olduğu (§11.2).
- **iOS 26 / Safari:** Normal taramada `fbclid`'in silinip silinmediği. Apple'ın güncel resmi açıklaması bulunamadı.
- **KVKK:** Pikselin çerez rehberi karşısındaki durumu ve CAPI, gelişmiş eşleştirme, CRM aşamaları ve çevrimdışı yükleme ile yurt dışına aktarım mekanizması hukukçuya sorulmalı. Bunlar §2'nin başındaki hukuki ön koşul kutusunda kurulum ön koşulu olarak yer alıyor.
- **Ad:** Arayüzde "Olay Yöneticisi" ya da "Etkinlik Yöneticisi" görünebilir (Kurulum Kılavuzu §3.7). İkisi aynı araçtır.

---

## Kaynaklar

**Meta yardım merkezi ve duyurular [RESMİ]** (erişim 17–20 Ekim 2026):
- Dönüşümler API'si ve tekilleştirme: https://www.facebook.com/business/help/823677331451951
- Dönüşümler API'si hakkında: https://www.facebook.com/business/help/2041148702652965
- Piksel en iyi uygulamaları: https://www.facebook.com/business/help/2254103654917599
- Piksel ve CAPI güncellemeleri (15.04.2026): https://www.facebook.com/business/news/pixel-conversionsapi-updates
- CAPI kurulum yöntemleri: https://www.facebook.com/business/help/433493041367251
- CAPI Gateway: https://www.facebook.com/business/help/387152639648383
- Yapay zekâ ile sayfa ve ürün bilgisi: https://www.facebook.com/business/help/1567320497668753
- Standart olaylar (iptal ya da iade olayı yok): https://www.facebook.com/business/help/402791146561655
- Özel dönüşümler: https://www.facebook.com/business/help/780705975381000
- Reklam Yöneticisi ve Olay Yöneticisi farkları (Veri kaynakları toplamı tekilleştirilmez, özel dönüşüm toplamı tekilleştirilir): https://www.facebook.com/business/help/337196340694086
- Trafik izinleri (iki alan adına izin önerisi, alt alan adı kuralı): https://www.facebook.com/business/help/572690630080597
- Olay Yöneticisi tanılar: https://www.facebook.com/business/help/667164051342757
- Olay Eşleştirme Kalitesi: https://www.facebook.com/business/help/765081237991954
- Olay gönderme zamanlaması: https://www.facebook.com/business/help/801591810609156
- Toplu Olay Ölçümü ve alan adı: https://www.facebook.com/business/help/721422165168355
- Alan adı doğrulama: https://www.facebook.com/business/help/286768115176155
- İlişkilendirme ayarları (standart ayar yayını da optimize eder): https://www.facebook.com/business/help/460276478298895
- Tıklama ilişkilendirmesi değişikliği (03.03.2026): https://www.facebook.com/business/news/click-attribution
- Artımlı ilişkilendirme: https://www.facebook.com/business/help/2366718460372682 · https://www.facebook.com/business/help/644554008179419 · https://www.facebook.com/business/help/1271619100603114
- Üçüncü taraf araçlarla dönüşüm farkları: https://www.facebook.com/business/help/147965221941551
- İstatistiksel modelleme: https://www.facebook.com/business/help/1329822420714248
- Dinamik URL parametreleri: https://www.facebook.com/business/help/2360940870872492
- URL parametreleri: https://www.facebook.com/business/help/1016122818401732
- Shopify entegrasyonu: https://www.facebook.com/business/help/646757258759189
- Mesajlaşma olayları: https://www.facebook.com/business/help/610611921269482
- Mesajlaşma yoluyla alışveriş hedefi (WhatsApp ve Instagram Direct etiketleri): https://www.facebook.com/business/help/1214599109289826 · İngilizce sürüm: https://www.facebook.com/business/help/1214599109289826?locale=en_US
- Potansiyel müşteri performans hedefleri: https://www.facebook.com/business/help/782657799338685
- CRM'yi Meta'ya bağlama (Potansiyel Müşteri Merkezi'nde kurulum gerekmediği notu dahil): https://www.facebook.com/business/help/571704773472628
- Potansiyel Müşteri Merkezi: https://www.facebook.com/business/help/681540569280613
- Google E-Tablolar ile CRM CAPI'si: https://www.facebook.com/business/help/1095577538573756
- Dönüşüm potansiyel müşterileri için CRM kurulumu: https://www.facebook.com/business/help/279369167153556
- Hızlı form verisi indirme (90 gün): https://www.facebook.com/business/help/1526849577619206
- Hızlı form takip parametreleri: https://www.facebook.com/business/help/357992214682464
- Çevrimdışı dönüşümler: https://www.facebook.com/business/help/1142103235885551
- Meta Deneyler: https://www.facebook.com/business/help/2420136524735942
- Test güven eşikleri: https://www.facebook.com/business/help/239549606692303
- Hariç tutmalı A/B testi: https://www.facebook.com/business/help/2053923394930992
- Dönüşüm Yükselişi ve CAPI: https://www.facebook.com/business/help/453968412201452

**Meta geliştirici belgeleri [RESMİ]:**
- Piksel ve sunucu olaylarının tekilleştirilmesi: https://developers.facebook.com/docs/marketing-api/conversions-api/deduplicate-pixel-and-server-events/
- Sunucu olayı parametreleri: https://developers.facebook.com/docs/marketing-api/conversions-api/parameters/server-event
- Müşteri bilgisi parametreleri ve hash: https://developers.facebook.com/docs/marketing-api/conversions-api/parameters/customer-information-parameters
- İşletme mesajlaşması CAPI'si: https://developers.facebook.com/docs/marketing-api/conversions-api/business-messaging
- Dönüşüm potansiyel müşterileri entegrasyonu: https://developers.facebook.com/docs/marketing-api/conversions-api/conversion-leads-integration/
- Ads Insights API ilişkilendirme değişikliği (16.10.2025): https://developers.facebook.com/blog/post/2025/10/16/ads-insights-api-metric-availability-updates/
- Insights API (`action_report_time`): https://developers.facebook.com/docs/marketing-api/reference/ad-account/insights/
- GeoLift: https://facebookincubator.github.io/GeoLift/docs/intro/

**Google [RESMİ]:**
- GA4 varsayılan kanal grubu: https://support.google.com/analytics/answer/9756891
- GA4 kaynak ve kategori listesi (PDF): https://storage.googleapis.com/support-kms-prod/qn1xhBu8MVcZPIZ2WZMNdI40FtZXFPGYxj2K
- GA4 ilişkilendirme ve geriye bakış süresi: https://support.google.com/analytics/answer/10597962

**Altyapı belgeleri [RESMİ, platform]:**
- ikas: https://support.ikas.com/tr/facebook-piksel-ve-donusumler-apisi-baglantisi
- Ticimax: https://www.destekalani.com/Icerik/facebook-conversion-api-entegrasyonu-564
- WooCommerce: https://woocommerce.com/document/facebook-for-woocommerce/ · https://wordpress.org/plugins/facebook-for-woocommerce/

**Mevzuat [MEVZUAT]:**
- KVKK Çerez Uygulamaları Hakkında Rehber (Temmuz 2025): https://www.kvkk.gov.tr/SharedFolderServer/CMSFiles/fb193dbb-b159-4221-8a7b-3addc083d33f.pdf

**Pratisyen ve topluluk [PRATİSYEN] / [TOPLULUK]:**
- Çerez bandı ve KVKK (hukuk bürosu): https://koksal.av.tr/en/knowledge-centre/cookie-banner-and-notice-kvkk-gdpr-dual-compliance/
- EMQ hedefleri (CAPI satıcısı): https://customerlabs.com/blog/improve-your-event-match-quality-from-ok-to-great/
- Meta destekli CAPI'nin sınırı (satıcı): https://segwise.ai/blog/meta-pixel-conversions-api-ai-updates-2026.md
- Artırımlılık katsayıları (Common Thread Collective): https://commonthreadco.com/pages/methodology-meta
- Artırımlılık testi süreleri (geo test satıcısı): https://www.haus.io/article/facebook-incrementality-testing
- Konum hedeflemesinin hedef bölge dışına taşması (Jon Loomer, 31.03.2025): https://www.jonloomer.com/meta-ads-reach-people-outside-of-the-targeted-location/
- Shopify çift sayım vakası: https://community.shopify.com/t/meta-has-2-3x-times-purchases-shopify-data-in-meta-is-wrong-and-i-need-help-to-fix-it/404737
- WhatsApp satış kaynağını izleme (forum): https://www.r10.net/sorum-var/3924858-whatsapp-donusumlerinizi-nasil-takip-ediyorsunuz-r10.html
- Satışın Meta'ya dönmemesinin etkisi (satıcı): https://eazybe.com/tr/blog/click-to-whatsapp-reklamlari
