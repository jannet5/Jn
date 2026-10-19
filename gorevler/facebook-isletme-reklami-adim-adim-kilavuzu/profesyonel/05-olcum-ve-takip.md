# Ölçüm, Piksel, CAPI ve ilişkilendirme

> **Profesyonel paket · Modül 05** · Bilgi tarihi: 17 Ekim 2026 · Piksel oluşturma, "Tanıtım objesi bulunamadı" hatası ve dönüşüm konumu ekranı **Kurulum Kılavuzu §3.7 ve §5.1**'de (`../KILAVUZ.md`), ilk 7 günün izlenmesi §6.3'te. Başabaş CPA/ROAS ve 1,05 konum ücreti çarpanı **Modül 01 §3** ile **Modül 02 §1**'de. Olay merdiveni Modül 02 §2.2'de, adlandırma standardı Modül 02 §10'da.

**Bu modülde**

- İş modeline göre ölçüm kurgusu ve site için kurulum kontrol listesi: Piksel, CAPI, tekilleştirme, olaylar, alan adı, test siparişi (§1–§2).
- Altyapı entegrasyonları (Shopify, ikas, Ticimax, WooCommerce), EMQ hedefi ve teşhis tablosu (§3–§4).
- İlişkilendirme ayarları, 2026 değişiklikleri, Meta–GA4–kasa farkının nedenleri ve haftalık mutabakat formülleri (§5). GA4 kanal kuralına uygun UTM şablonu (§6).
- WhatsApp, form, telefon ve mağaza satışını ölçme yolları: hazır etiket, Google E-Tablolar, CRM, kupon kodu (§7–§9).
- Veri kalite kontrolleri ve artırımlı etkiyi ölçmenin pratik yolları (§10–§11).

**Etiketler:** **[RESMİ]** Meta, Google ya da altyapının kendi belgesi · **[MEVZUAT]** Türk mevzuatı · **[PRATİSYEN]** ajans, araç satıcısı ya da uzman yazısı (çıkar çatışması olabilir) · **[TOPLULUK]** forum · *sezgisel* = ölçülmemiş çalışma kuralı · *türetilmiş* = resmi kuraldan yapılan aritmetik · *varsayım* = örnek için uydurulmuş rakam. Meta'nın "%X daha iyi" rakamları Meta'nın kendi iddiasıdır, yöntemi çoğu zaman yayımlanmaz.

---

## 0. Çekirdek kurallar

1. Sonuç Meta'ya geri dönmüyorsa kampanya o sonuca optimize edilemez. Önce ölçüm kurulur, sonra kampanya.
2. Sitede Piksel ve CAPI birlikte kurulur. Aynı olaylar `event_id` ile tekilleştirilir [RESMİ].
3. Hazır altyapıda yalnız altyapının Meta entegrasyonu kullanılır. Üstüne elle kod ya da kodsuz olay eklenirse olaylar iki kez sayılır [RESMİ, platform].
4. Kararlarda tek doğruluk kaynağı sipariş paneli ya da muhasebedir. Meta raporu optimizasyon ve yön içindir (*sezgisel*).
5. Karşılaştırma her zaman aynı ilişkilendirme ayarı ve modeliyle yapılır [RESMİ].

---

## 1. İş modeli → ölçüm kurgusu

| İş modeli | Meta'ya dönen sinyal | Kurulum | CRM yoksa asgari takip |
|---|---|---|---|
| E-ticaret, hazır altyapı | Purchase (Piksel + CAPI) | Altyapı entegrasyonu (§3) | Sipariş paneli + UTM (§6) |
| E-ticaret, özel kodlu site | Purchase | Meta destekli CAPI, Gateway ya da doğrudan (§2.2) | Aynı |
| WhatsApp'tan satış | Konuşma → alışveriş etiketi ya da mesajlaşma CAPI'si | WhatsApp Business paylaşımı (§7) | Google E-Tablolar satış defteri + kampanya kodu |
| Hızlı form | Lead → CRM aşamaları | Google E-Tablolar ya da CRM bağlantısı (§8) | `lead_status` sütunu |
| Site formu, randevu | Lead / Schedule + CRM aşamaları | Piksel + CAPI + CRM | Satış defteri |
| Telefon, fiziksel mağaza | Arama; çevrimdışı olay | Veri setine CSV ya da CAPI (§9) | Satış defteri + kupon (§7.4) |

**Eğer** satışın Meta'ya dönmesi 2 haftadan uzun sürecekse **ise** kampanyayı daha sığ bir olayla başlat (Modül 02 §2.2), ölçümü paralelde tamamla (*sezgisel*).

---

## 2. Site ölçümü: kurulum kontrol listesi

### 2.1 Piksel

- [ ] Sitede tek piksel var. İkinci piksel yalnız iki ayrı ajans çalışıyorsa gerekir [RESMİ]. Meta Pixel Helper (Chrome eklentisi) ile her sayfada bir kez yüklendiğini gör.
- [ ] Otomatik gelişmiş eşleştirme açık. Altyapı entegrasyonu varsa kodsuz olay kurulumu kapalı ve eski elle eklenmiş olaylar silinmiş [RESMİ, ikas].
- [ ] Olay adları birebir doğru. `viewcontent` yazılırsa standart olay değil, özel olay oluşur [RESMİ].
- [ ] Trafik izinlerinde izin listesi açık, listede yalnız kendi alan adların var. Piksel kimliği herkese açıktır, başka site veriyi kirletebilir [RESMİ].
- [ ] "Yapay zekâ ile sayfa ve ürün bilgisi ekleme" ayarı (2026) gözden geçirilmiş. Hassas sektörde kapat ya da paylaşılan veri türlerini tek tek seç [RESMİ].

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
- Dar hedef için özel dönüşüm kullanılabilir, örneğin "değeri 500 TL üstü Purchase" ya da "URL'de `/tesekkurler` geçen sayfa". Yeterli veri toplanınca performans hedefi olarak seçilebilir [RESMİ].
- Hangi olaya optimize edileceği haftalık hacme göre seçilir (Modül 02 F5, §2.2).

### 2.5 Alan adı ve AEM [RESMİ]

- Olay yapılandırması ve kampanyada "dönüşüm alan adı" seçimi için alan adı doğrulaması artık zorunlu değil.
- Yine de doğrula: bağlantı düzenleme izinleri, ticaret özellikleri ve bazı işletme doğrulamaları için gerekir. Meta etiket, HTML dosyası ya da DNS TXT kaydıyla yapılır, ücretsizdir.
- AEM: Alan adı başına 8 olay önceliklendirmesi kalktı, AEM sekmesi kaldırıldı, reklamverenin yapacağı bir şey yok. Bu kuralı anlatan kaynak eskidir.

### 2.6 KVKK ve çerez kapısı (hukuki görüş değildir)

- KVKK Çerez Rehberi'ne (Temmuz 2025) göre davranışsal reklam çerezleri açık rıza ister [MEVZUAT]. Piksel rızadan önce tetiklenmemeli, "Reddet" seçeneği "Kabul et" kadar kolay olmalı [PRATİSYEN, hukuk bürosu].
- CAPI rıza yükümlülüğünü kaldırmaz. Meta da CAPI'nin gizlilik kurallarını aşmak için yapılmadığını yazıyor [RESMİ].
- Sağlık ve finans gibi alanlarda URL'de, olay parametresinde ya da özel olay adında hassas bilgi bulunmaz. Bu, İşletme Araçları Koşulları'na aykırıdır [RESMİ]. Ayrıntı için hukuk/uyum modülüne bak.
- Yan etki: Rıza reddedildikçe piksel olayı düşer. Meta ve GA4 rakamları kasanın altında kalır (§5.4).

### 2.7 Test protokolü

Her yeni kurulumda ve her tema, eklenti, ödeme ya da önbellek değişikliğinden sonra uygula.

1. Olay Yöneticisi'nde **Olayları Test Et** sekmesini aç. Sunucu olayları için ekrandaki test kodunu kullan.
2. Ürün sayfasını aç, sepete ekle, ödemeyi başlat ve küçük tutarlı bir sipariş ver. Sonra iptal et ya da altyapının test modunu kullan.
3. Kontrol et:
   - Her olay bir kez geldi.
   - Purchase hem "Tarayıcı" hem "Sunucu" satırında var ve tekilleştirildi.
   - `value` panel tutarıyla aynı. KDV dahil mi hariç mi, not et (Modül 01 §1.4, soru 17).
   - `currency` TRY. Katalog varsa `content_ids` katalogla aynı.
4. 24–48 saat sonra Genel bakış, **Tanılar** ve EMQ ekranlarına bak. Tanılar'da kırmızı simge acil sorun, sarı simge uyarıdır [RESMİ].
5. Müşteri dosyasına yaz: tarih, test sipariş no, gönderilen değer, KDV durumu.

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

**Çift sayım vakası [TOPLULUK]:** Bir Shopify mağazasında Meta 7 günde 325 satın alma, Shopify 190 sipariş gösterdi. Önerilen çözüm uygulamayı yeniden bağlamak, tek piksel kaynağı bırakmak ve test siparişi vermekti. **Eğer** Olay Yöneticisi'ndeki Purchase sayısı panel siparişinin belirgin üstündeyse **ise** ikinci piksel kaynağını ara (§10.2).

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

- **Varsayılan:** Standart model, 7 gün tıklama + 1 gün görüntüleme (+ 1 gün etkileşim). Bütün setlerde aynı ayar kullanılır. Farklı modeldeki setler aynı tabloda kıyaslanmaz [RESMİ].
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
| Reklam engelleyici ve çerez reddi pikseli durdurur [RESMİ] | Kasa | CAPI kaybı azaltır |
| Çift piksel ya da tekilleştirme hatası | Meta (hata) | §3, §10.2 |
| Kasada iptal, iade ve kapıda ödeme reddi düşülür. Piksel bunları kendiliğinden düşmez (*sezgisel*, entegrasyona bağlı) | Meta değeri | Net ciroyu kasadan al |
| KDV dahil ya da hariç değer gönderimi | Değişken | §5.5'teki V oranı |
| API dönüşümü gösterim gününe ya da dönüşüm gününe yazabilir (`action_report_time`) [RESMİ] | Günlük farklar | Haftalık karşılaştır |
| UTM eksik ya da medium "social" | GA4'te Organic Social veya Referral | §6 |

**Kural (*sezgisel*):** Araçlar birbirini doğrulamak için kullanılmaz. Her araç kendi zaman serisinde izlenir, gerçek ciro panelden alınır.

### 5.5 Haftalık mutabakat: formüller ve örnek

Formüller *türetilmiş*, eşikler *sezgisel*:
- **Kapsama K** = Olay Yöneticisi'ndeki tüm Purchase olayları (organik dahil) ÷ aynı dönemdeki panel siparişi (iptal öncesi). Beklenen değer 1'in biraz altıdır. K > 1,05 ise çift sayım, K < 0,70 ise olay kaybı şüphesi var.
- **Meta payı P** = Reklam Yöneticisi'ndeki Purchase ÷ panel siparişi. Olay Yöneticisi ≥ Reklam Yöneticisi beklenir [RESMİ]. Haftalık P > 1 ise kesin hata var.
- **Değer oranı V** = Meta'daki ortalama Purchase değeri ÷ panelin KDV dahil ortalama sepeti. V ≈ 1,00 ise KDV dahil, V ≈ 0,83 (1 ÷ 1,20) ise KDV hariç gönderiliyor. Başka bir değer kargo, indirim ya da para birimi hatasıdır. %10 ve %1 KDV'li ürünlerde oran farklıdır.
- **MER** = KDV hariç toplam ciro ÷ gerçek reklam maliyeti (panel × 1,05).

**Doldurulmuş örnek (*varsayım*; Modül 02 §1'deki mağaza, başabaş gerçek CPA 414 TL):**

| Girdi | Değer |
|---|---|
| Panel harcaması / gerçek maliyet | 21.000 / 22.050 TL |
| Panel siparişi / KDV hariç ciro | 150 / 150.000 TL |
| Olay Yöneticisi Purchase | 141 → K = 0,94 ✔ |
| Reklam Yöneticisi Purchase (7 gün tık + 1 gün gör) | 70 → P = 0,47 ✔ |
| Bunun 7 gün tıklama kısmı | 52 → görüntüleme payı %26 |
| GA4 Paid Social satın alma | 38 |
| Meta ort. değer ÷ panel KDV dahil sepet | 1.200 ÷ 1.200 → V = 1,00 |
| MER | 150.000 ÷ 22.050 = 6,8 |

**Okuma:**
- Gerçek CPA Meta'ya göre 22.050 ÷ 70 = **315 TL**, 7 gün tıklamaya göre 424 TL, GA4'e göre 580 TL.
- Başabaş 414 TL bu aralığın içinde kalıyor. Doğru rakam bu tabloyla anlaşılamaz.
- **Karar:** Bütçeyi artırmadan önce artırımlılık testi yap (§11), o zamana kadar bütçeyi koru. Panel ROAS eşiğini V = 1,00'a göre hesapla (Modül 01 §3.2).

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

## 7. WhatsApp satışlarını ölçme

### 7.1 Yöntem merdiveni

| Basamak | Ne zaman | Satış Meta'ya döner mi? |
|---|---|---|
| A. Google E-Tablolar satış defteri + kampanya kodu | Her zaman, ilk gün | Hayır, yalnız senin hesabın için |
| B. WhatsApp Business uygulamasında paylaşım + hazır sipariş etiketleri | Uygulamayı kullanan küçük işletme | Evet |
| C. Mesajlaşma ortağı ya da işletme mesajlaşması CAPI'si (Cloud API) | Çok temsilci, CRM, otomasyon | Evet, olay bazında |

A her zaman tutulur, B ya da C onun üstüne eklenir (*sezgisel*). Satış Meta'ya dönmezse sistem satın alana değil, mesaj atmaya yatkın kişiye optimize etmeye devam eder [PRATİSYEN].

### 7.2 WhatsApp Business uygulamasıyla alışveriş sinyali [RESMİ]

1. Uygulamada **Araçlar > Müşterilerinizin hareketleri** altında Meta ile paylaşımı aç. Menü adı sürüme göre değişebilir.
2. Satış olan sohbete hazır etiketlerden birini ver: "Yeni Sipariş", "Ödeme Bekleniyor", "Ödendi" ya da "Sipariş tamamlandı". Özel etiketli sohbetler ilişkilendirilmeyebilir. "Siparişler" özelliğinde durumu "Ödendi" yapmak da alışveriş sayılır.
3. Sohbet WhatsApp'a tıklama reklamından başlamış ve reklam tıklamasından sonraki 7 gün içinde etiketlenmiş olmalı.
4. Son 30 günde en az 10 alışveriş olayı birikince "Mesajlaşma yoluyla alışveriş sayısını en üst seviyeye çıkarın" hedefi seçilebilir. Birden fazla Sayfa varsa her Sayfa bu koşulu ayrı sağlar.

**Eğer** 30 günde 10 etiketli satış yoksa **ise** konuşma hedefinde kal ve etiketlemeye devam et (Modül 02 §2.1).

**API yolu (C) [RESMİ]:** `action_source=business_messaging`, `messaging_channel=whatsapp` ve mesajın referral nesnesinden gelen `ctwa_clid` gönderilir. Satışa optimizasyon Messenger ve WhatsApp'ta yapılabilir, Instagram'da yapılamaz. On-Premises API desteği 23.10.2025'te bitti.

### 7.3 Google E-Tablolar satış defteri (şablon)

KVKK gereği yalnız telefonun son 4 hanesini tut ve erişimi kısıtla (*sezgisel*, veri minimizasyonu).

| tarih | tel_son4 | kaynak | kod | ürün | durum | tutar_kdv_dahil | meta_etiketi |
|---|---|---|---|---|---|---|---|
| 14.10.2026 | 4567 | meta_reklam | K017 | Elbise-M | odendi | 1.200 | Ödendi ✔ |
| 14.10.2026 | 8812 | organik_ig | — | Etek-S | kayip | 0 | — |
| 15.10.2026 | 3021 | meta_reklam | K021 | Elbise-L | odeme_bekliyor | 1.350 | Ödeme Bekleniyor ✔ |

- `kaynak`: meta_reklam, organik_ig, google, tavsiye, bilinmiyor.
- `durum`: yeni, teklif, odeme_bekliyor, odendi, kayip, iade.
- `meta_etiketi`: satışın WhatsApp'ta da hazır etiketle işaretlendiğini gösterir. Etiket kapsaması bu sütundan hesaplanır.

### 7.4 Kampanya kodu, kupon ve "Bizi nereden duydunuz?"

Bu yöntemler *sezgisel*, forumdaki reklamveren önerilerine dayanır [TOPLULUK].
- **Önceden doldurulmuş mesaj:** Her reklamın karşılama metnine ürün ya da kreatif kodu koy, örneğin "Merhaba, K017 elbise hakkında bilgi almak istiyorum." Kullanıcı metni silebilir, bu yüzden kodlu sohbet sayısı alt sınırdır.
- **Kanal başına benzersiz kupon:** Yalnız Meta reklamında görünen bir kod, örneğin "IG10". Herkes kupon kullanmadığı için bu da alt sınır verir. Kod kupon sitelerine sızarsa sonraki veriyi ayrı oku. Mağaza ve telefon satışında kod, kasada "kaynak" alanına girilir.
- **Kaynağı bilinmeyen sohbet:** İlk yanıtta seçenekli soru sor: "Bizi nereden gördünüz? 1) Instagram reklamı 2) Instagram sayfası 3) Google 4) Tavsiye". Bu soru artırımlılığı ölçmez, yalnız sağlama yapar.

### 7.5 Formüller ve doldurulmuş örnek (*varsayım*)

Bir hafta: panel harcaması 7.000 TL → gerçek 7.350 TL. Meta 210 konuşma gösteriyor. Defterde reklam kaynaklı 33 ödenmiş satış var, 29'u WhatsApp'ta etiketli.
- **Sohbet → satış oranı** = 33 ÷ 210 = **%15,7**
- **Gerçek CPA** = 7.350 ÷ 33 ≈ **223 TL**. Başabaşla karşılaştır (Modül 01 §3.6).
- **Etiket kapsaması** = 29 ÷ 33 = **%88**. %90'ın altı etiketleme disiplini sorunudur (*sezgisel*).
- Bu hızla 30 günde yaklaşık 116 etiketli satış birikir. 10 eşiği aşıldığı için alışveriş hedefi denenebilir.

---

## 8. Form satışlarını ölçme (hızlı form ve site formu)

### 8.1 En basit yol: Google E-Tablolar ile CRM CAPI'si [RESMİ]

1. Meta Business Suite > Tüm araçlar > **Hızlı formlar** > **+ CRM Bağla** yolunu izle. Google E-Tablolar'ı seç ve Google hesabınla izin ver. Microsoft Excel seçeneği de var.
2. Tabloyu ve formu seç, sonra işletme portfolyosunu ve veri setini seç. **Test potansiyel müşterisini gönder** ile bağlantıyı doğrula.
3. Tabloda `lead_status` sütunu oluşur. Değerleri: "Giriş", "Uygun", "Dönüşüm Gerçekleştirdi", "Kayıp", "Uygun değil". Satış ekibi bu sütunu güncel tutar, Meta her güncellemede CAPI olayı üretir.
4. "Gelecekteki formlar için otomatik olarak entegrasyon oluştur" kutusunu işaretle.

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

Bir ay: panel harcaması 30.000 TL → gerçek 31.500 TL. 300 potansiyel müşteri gelmiş, 120'si "Uygun", 24'ü "Dönüşüm Gerçekleştirdi".
- Panel CPL = 30.000 ÷ 300 = 100 TL. **Gerçek nitelikli maliyet** = 31.500 ÷ 120 = 262,5 TL.
- **Gerçek satış başı maliyet** = 31.500 ÷ 24 = **1.312,5 TL**. Karar bu rakamla verilir.
- Satış oranı %8 (%1–40 aralığında) ve hacim ayda 200'ün üstünde. CAPI bağlıysa nitelikli hedef yeni kampanyada denenebilir.

---

## 9. Telefon ve mağaza satışları (çevrimdışı) [RESMİ]

- Olay Yöneticisi'nde veri seti oluştur, reklam hesabına bağla. Satışları CAPI, bir ortak ya da elle dosya yüklemeyle gönder. Eski Offline Conversions API Mayıs 2025'te kapandı.
- "Site ve mağaza" dönüşüm konumunda çevrimdışı olaylar Satış kampanyasında optimizasyona da girer.
- Önerilen gönderim süresi 7 gün, azami 62 gündür. Geç yüklemede geçmiş günlerin sonuçları sonradan değişir.
- Kayıtta e-posta ya da telefon yoksa eşleşme çok düşük kalır. Bu bilgi KVKK'ya uygun aydınlatmayla ve gerekiyorsa rızayla alınır (*sezgisel*).
- Asgari yöntem: satış defteri (§7.3), kanal kuponu (§7.4), kasada "Bizi nereden duydunuz?" alanı.

---

## 10. Veri kalite kontrolleri

### 10.1 Rutin (*sezgisel*)

| Sıklık | Kontrol |
|---|---|
| Günlük | Son 24 saatte Purchase ya da Lead akıyor mu? Tanılar'da kırmızı sorun var mı? |
| Haftalık | K, P, V (§5.5); EMQ; GA4 Paid Social satırı; WhatsApp etiket kapsaması; `lead_status` güncelliği |
| Her site değişikliğinden sonra | §2.7 test protokolü |
| Aylık | Trafik izin listesi; piksele erişimi olan kişi ve ortaklar; form verisinin yedeği; kupon sızıntısı |
| Çeyreklik | Artırımlılık testi ya da katsayı güncellemesi (§11) |

### 10.2 Alarm tablosu (eğer → ise)

| Eğer | İse |
|---|---|
| K > 1,05 | İkinci piksel ya da elle eklenmiş olay ara: tema, GTM, kodsuz olaylar. Tekilleştirmeyi kontrol et |
| Haftalık P > 1 | Kesin ölçüm hatası. Bütçe kararlarını dondur, önce düzelt |
| K < 0,70 | Çerez bandı pikseli engelliyor olabilir, CAPI kopmuş ya da jetonun süresi dolmuş olabilir. Tanılar'a bak, §2.7'yi uygula |
| V ne 1,00 ne 0,83 | Değer alanı yanlış: kargo, indirim ya da para birimi kontrol et |
| Bir olay 24 saattir sıfır ama sipariş sürüyor | Entegrasyon kopmuş. Aynı gün düzelt, bu sürede kampanyayı değiştirme |
| Tanılar'da başka alan adından olay geliyor | İzin listesini güncelle |
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
| Satış il bazında panelden görülüyor | Coğrafi kapatma testi (§11.3) | 2–4 hafta test + 1–2 hafta gözlem [PRATİSYEN, sezgisel] | Gerçek kasa farkı |
| Tek bölge, çok küçük işletme | Zaman bazlı aç-kapa | 2 hafta kapalı ile önceki 2 hafta (*sezgisel*) | Kaba sinyal, mevsime açık |
| Her durumda | MER trendi + "Bizi nereden duydunuz?" | Haftalık | Sağlama |

- Meta işe hesap düzeyinde bir testle başlamayı öneriyor. Minimum harcama kurulum ekranında hesaba göre gösterilir, genel eşik yayımlanmıyor [RESMİ].
- Haftada 50–100'den az dönüşüm alan hesaplarda kısa testlerin istatistiksel gücü yetmeyebilir [PRATİSYEN, geo test satıcısı]. Meta'nın açık kaynak GeoLift paketi aynı mantığı daha titiz uygular, analist gerektirir [RESMİ].
- Testi 11.11, Kasım indirimleri ve bayram haftalarında yapma (*sezgisel*).

### 11.2 Formüller (*türetilmiş*)

- **Artımlı sipariş** = kapatılan bölgede beklenen sipariş − gerçekleşen sipariş.
- **Artımlı CPA** = kapatılan bölgede harcanmayan gerçek maliyet ÷ artımlı sipariş. **iROAS** = artımlı KDV hariç ciro ÷ gerçek maliyet.
- **Artırımlılık katsayısı c** = artımlı sipariş ÷ Meta'nın aynı bölge ve süre için raporladığı sipariş.
- **Düzeltilmiş CPA** = Meta CPA ÷ c. Sonraki haftalarda bunu kullan, c'yi 3–6 ayda bir yeniden ölç (*sezgisel*).

### 11.3 Coğrafi kapatma testi: adımlar ve örnek

**Adımlar (*sezgisel*, GeoLift mantığının basit hâli):**
1. Panelden son 8–12 haftanın siparişlerini teslimat iline göre çıkar.
2. İlleri toplam sipariş ve haftalık seyir bakımından benzer iki gruba ayır: A (reklam açık kalır), B (reklam kapanır). İstanbul, Ankara ve İzmir'i teste katma, oralarda reklam açık kalsın. Bu iller tek başına grupları dengesizleştirir.
3. Ön dönemde her hafta için B ÷ A oranını hesapla, en düşük ve en yüksek oranı not et.
4. Test döneminde B illerini tüm setlerde konum hariç tutmayla kapat. Toplam bütçeyi B'nin harcama payı kadar düşür ki A'ya fazladan bütçe gitmesin.
5. 3 hafta test, 1 hafta gözlem. Bu sürede kreatif, teklif ve yapı değişmez.

**Doldurulmuş örnek (*varsayım*):**
- Ön dönem (4 hafta, iki grupta reklam açık): A 520, B 480 sipariş → r = 480 ÷ 520 = **0,923**. Haftalık oranlar 0,88–0,96 arasında.
- Test (3 hafta, B'de reklam kapalı): A 400, B 300 sipariş.
- B'de beklenen sipariş = 400 × 0,923 = 369 → **artımlı sipariş** = 369 − 300 = **69**. Test oranı 300 ÷ 400 = 0,75, ön dönem aralığının dışında. Yani fark gürültü değil (*sezgisel* kontrol).
- B'de harcanmayan tutar 3 × 15.000 TL panel = 45.000 TL → gerçek 47.250 TL.
- **Artımlı CPA** = 47.250 ÷ 69 ≈ **685 TL**. Meta ön dönemde B için 3 haftada 135 satın alma raporlamıştı → **c** = 69 ÷ 135 ≈ **0,51**.
- **Karar:** Artımlı CPA 685 TL, başabaş 414 TL'nin üstünde. Meta CPA'sı 350 TL görünse de düzeltilmiş CPA 350 ÷ 0,51 ≈ 686 TL'dir. Bütçe artırma. Önce yeniden pazarlama ve görüntüleme ağırlıklı setleri, kreatifi ve teklifi gözden geçir (Modül 02–04).
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
- **Altyapının değer alanı:** KDV dahil mi, iade düşülüyor mu? Her hesapta V oranıyla kontrol et.
- **iOS 26 / Safari:** Normal taramada `fbclid`'in silinip silinmediği. Apple'ın güncel resmi açıklaması bulunamadı.
- **KVKK:** Pikselin çerez rehberi karşısındaki durumu ve CAPI ile yurt dışına aktarım mekanizması hukukçuya sorulmalı.
- **Ad:** Arayüzde "Olay Yöneticisi" ya da "Etkinlik Yöneticisi" görünebilir (Kurulum Kılavuzu §3.7). İkisi aynı araçtır.

---

## Kaynaklar

**Meta yardım merkezi ve duyurular [RESMİ]** (erişim 17–18 Ekim 2026):
- Dönüşümler API'si ve tekilleştirme: https://www.facebook.com/business/help/823677331451951
- Dönüşümler API'si hakkında: https://www.facebook.com/business/help/2041148702652965
- Piksel en iyi uygulamaları: https://www.facebook.com/business/help/2254103654917599
- Piksel ve CAPI güncellemeleri (15.04.2026): https://www.facebook.com/business/news/pixel-conversionsapi-updates
- CAPI kurulum yöntemleri: https://www.facebook.com/business/help/433493041367251
- CAPI Gateway: https://www.facebook.com/business/help/387152639648383
- Yapay zekâ ile sayfa ve ürün bilgisi: https://www.facebook.com/business/help/1567320497668753
- Standart olaylar: https://www.facebook.com/business/help/402791146561655
- Özel dönüşümler: https://www.facebook.com/business/help/780705975381000
- Reklam Yöneticisi ve Olay Yöneticisi farkları: https://www.facebook.com/business/help/337196340694086
- Trafik izinleri: https://www.facebook.com/business/help/572690630080597
- Olay Yöneticisi tanılar: https://www.facebook.com/business/help/667164051342757
- Olay Eşleştirme Kalitesi: https://www.facebook.com/business/help/765081237991954
- Olay gönderme zamanlaması: https://www.facebook.com/business/help/801591810609156
- Toplu Olay Ölçümü ve alan adı: https://www.facebook.com/business/help/721422165168355
- Alan adı doğrulama: https://www.facebook.com/business/help/286768115176155
- İlişkilendirme ayarları: https://www.facebook.com/business/help/460276478298895
- Tıklama ilişkilendirmesi değişikliği (03.03.2026): https://www.facebook.com/business/news/click-attribution
- Artımlı ilişkilendirme: https://www.facebook.com/business/help/2366718460372682 · https://www.facebook.com/business/help/644554008179419 · https://www.facebook.com/business/help/1271619100603114
- Üçüncü taraf araçlarla dönüşüm farkları: https://www.facebook.com/business/help/147965221941551
- İstatistiksel modelleme: https://www.facebook.com/business/help/1329822420714248
- Dinamik URL parametreleri: https://www.facebook.com/business/help/2360940870872492
- URL parametreleri: https://www.facebook.com/business/help/1016122818401732
- Shopify entegrasyonu: https://www.facebook.com/business/help/646757258759189
- Mesajlaşma olayları: https://www.facebook.com/business/help/610611921269482
- Mesajlaşma yoluyla alışveriş hedefi: https://www.facebook.com/business/help/1214599109289826
- Potansiyel müşteri performans hedefleri: https://www.facebook.com/business/help/782657799338685
- CRM'yi Meta'ya bağlama: https://www.facebook.com/business/help/571704773472628
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
- Shopify çift sayım vakası: https://community.shopify.com/t/meta-has-2-3x-times-purchases-shopify-data-in-meta-is-wrong-and-i-need-help-to-fix-it/404737
- WhatsApp satış kaynağını izleme (forum): https://www.r10.net/sorum-var/3924858-whatsapp-donusumlerinizi-nasil-takip-ediyorsunuz-r10.html
- Satışın Meta'ya dönmemesinin etkisi (satıcı): https://eazybe.com/tr/blog/click-to-whatsapp-reklamlari
