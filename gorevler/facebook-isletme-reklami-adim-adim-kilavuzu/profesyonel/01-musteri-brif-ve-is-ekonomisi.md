# Müşteriyi tanıma, brif ve iş ekonomisi

> **Profesyonel paket · Modül 01** · Bilgi tarihi: 17 Ekim 2026 · Hesap kurulumu, menüler, ödeme ve hata kodları bu modülde yok; onlar için **Kurulum Kılavuzu** (`../KILAVUZ.md`).

**Bu modülde**

- İlk görüşmede sorulacak sorular ve her cevabın hangi kararı değiştirdiği (§1). Doldurulmuş brif kartı ve test sözleşmesi (§1.10).
- Müşterinin "daha çok satış istiyorum" cümlesini ölçülebilir bir KPI'ya, KPI'nın ölçüm yöntemine ve Meta'da optimize edilebilir bir olaya çevirme (§2).
- Başa baş CPA/ROAS, hedef CPA, artımlı MER, LTV ve izin verilen edinme maliyeti formülleri. Türkiye'ye özel %5 konum ücreti, KDV ve Piksel değeri düzeltmesiyle TL cinsinden çözülmüş iki örnek. Kampanya ve indirim dönemi eşikleri (§3).
- Bütçe hesabı: öğrenme minimumu, kapasite tavanı, test bütçesi ve program düzeyinde durdurma eşiği (§4).
- Beklenti yönetimi (§5) ve reklamın çözemeyeceği sorunlar için teşhis kontrol listesi (§6).

**Etiketler:** **[RESMİ]** Meta ya da Google belgesi · **[MEVZUAT]** Türk mevzuatı · **[PRATİSYEN]** ajans, araç sağlayıcı ya da uzman yazısı · **[ÇALIŞMA]** bağımsız ya da ısmarlama araştırma · *türetilmiş* = resmî kural ya da tanımdan yapılmış aritmetik · *sezgisel* = ölçülmemiş çalışma kuralı · *varsayım* = örnek için uydurulmuş girdi, gerçek veri değil.

**Sabit terimler.** Bu modülde her terim tek anlamda kullanılır. Hesap yaparken hangi sayının hangi kaynaktan geldiğini bu tabloya göre yaz.

| Terim | Anlamı |
|---|---|
| **RY** | Reklam Yöneticisi. *RY harcaması, RY CPA, RY ROAS*: Reklam Yöneticisi'nin gösterdiği sayılar. Harcamada konum ücreti ve KDV yoktur. Sonuç ve gelir Meta'nın ilişkilendirmesiyle sayılır. |
| **Fatura** | Meta'nın kestiği toplam tutar = RY harcaması × M. |
| **M (maliyet çarpanı)** | Vergi numarası girili: 1,05. Girili değil: 1,26 (§3.2). |
| **P (Piksel çarpanı)** | Piksel'in gönderdiği sipariş değeri ÷ KDV hariç gelir. KDV hariç gönderiyorsa 1,00. KDV dahil gönderiyorsa 1 + KDV oranı (%20'de 1,20). Yalnız ROAS'a girer. |
| **Fatura CPA / fatura ROAS** | RY CPA × M / RY ROAS ÷ (P × M). Sonuç sayısı hâlâ Meta'nın; yalnız maliyet ve gelir düzeltilmiştir. |
| **Sipariş sistemi** | İşletmenin kendi kaydı: ikas ya da Shopify paneli, CRM, kasa, WhatsApp satış defteri. Doğruluk kaynağı budur (§0.2). |
| **İşletme CPA / işletme ROAS** | Fatura ÷ sipariş sisteminde Meta'ya atanan satış / Meta'ya atanan KDV hariç gelir ÷ fatura. Atama yöntemi brife yazılır (§2.1). |
| **Eşik** | Başa baş ve hedef önce fatura TL'si cinsinden (katkıdan) bulunur, sonra RY'de okunacak sayıya çevrilir (§3.2). |

Diğer modüllerde "panel" kelimesi geçebilir. Orada "panel CPA/ROAS/harcaması" RY demektir, "sipariş paneli" ya da "işletme paneli" sipariş sistemi demektir.

---

## 0. Çalışma kuralları (önce bunları uygula)

1. **Brif tamamlanmadan kampanya kurma.** §1'de **[Z]** (zorunlu) işaretli bir alan boşsa önce onu iste. Özellikle şu dördü olmadan bütçe önerme: katkı marjı, iş hedefi, yasal kapı, mesaj/telefon yanıt kapasitesi.
2. **Tek doğruluk kaynağı seç.** Satış ve gelir sayısı işletmenin sipariş sisteminden gelir: ikas paneli, CRM, kasa ya da WhatsApp satış listesi. Meta raporu yön gösterir. İkisinin birbirini tam tutmaması normaldir. Fark sürekli Meta lehine büyüyorsa önce ölçümü kontrol et (§5.3). *sezgisel*
3. **Her eşiği RY diline çevir.** Reklam Yöneticisi'ndeki harcamaya, Türkiye kitlesi için alınan %5 konum ücreti dahil değildir. Vergi numarası girilmemişse fatura üstüne bir de %20 KDV gelir. Maliyet çarpanı **M**: vergi numarası varsa **1,05**, yoksa **1,26** **[RESMİ]** (Kurulum Kılavuzu §3.6, §9). ROAS eşiğine ayrıca Piksel çarpanı **P** girer. İkisi ayrı bulunur ve çarpılır (§3.2).
4. **Brüt marjla değil, katkı marjıyla hesapla.** Kargo, ödeme komisyonu ve iade düşülmezse başa baş noktası olduğundan iyimser çıkar **[PRATİSYEN]**.
5. **Kapasite tavanı öğrenme kuralından önce gelir.** İşletme yeni işi karşılayamıyorsa "öğrenme için 50 sonuç gerekir" diyerek bütçe artırma.
6. **Türkiye için yöntemi açık bir CPM/CPC/CPL kıyası yok.** Ajans bloglarındaki TL aralıkları birbiriyle 2–4 kat çelişiyor. Hesabın son 30–90 gününü, yeni hesapta ilk 2 haftayı taban çizgisi say.
7. **Sorun işletmedeyse söyle.** Kötü teklif, yavaş yanıt, bozuk site ya da yasal engel varsa reklam harcaması sorunu büyütür. Önce §6'daki kontrolü yap.
8. **Yazılı test sözleşmesi olmadan harcama başlatma.** Test süresi, bütçesi, başarı ve durdurma ölçütü önceden yazılır ve müşteriye onaylatılır (§4.6).

---

## 1. İlk görüşme soru listesi

Soruları gruplar hâlinde, bu sırayla sor. **[Z]** = cevap gelmeden bütçe ve kurulum önerme. Müşteri bir sayıyı bilmiyorsa tahminini al, yanına "tahmin" yaz ve ilk ay içinde ölçülmesini görev olarak ekle.

### 1.1 İşletme ve teklif

| # | Soru | Cevaba göre karar |
|---|---|---|
| 1 [Z] | Ne satıyorsunuz, kime satıyorsunuz? En çok satan 3 ürün ya da hizmet hangisi? | Sektör, yasal kapı (§1.8) ve dönüşüm konumu seçimi. |
| 2 [Z] | Nerede satıyorsunuz: site, WhatsApp/DM, telefon, mağaza, pazaryeri? Hangi şehir ya da bölgeye hizmet veriyorsunuz? | Dönüşüm konumu (Kurulum Kılavuzu §5.1) ve konum hedeflemesi. |
| 3 | Müşteri neden rakip yerine sizi seçiyor? Tek cümleyle söyleyin. | Cevap yoksa ya da yalnız "kaliteli, uygun fiyat" ise teklif zayıf olabilir (§6.5). |
| 4 | Ortalama sipariş ya da iş tutarı nedir (KDV dahil mi, hariç mi)? | Birim ekonomisi (§3). KDV durumunu mutlaka ayır. |
| 5 | Kampanya, indirim, paket ya da garanti var mı? | Kreatif açısı. İndirim varsa 10 gün kuralı (§1.8) ve indirimli fiyatla ayrı eşik (§3.7). |

### 1.2 Birim ekonomisi

| # | Soru | Cevaba göre karar |
|---|---|---|
| 6 [Z] | Bir satışta ürünün ya da hizmetin size değişken maliyeti nedir (alış, malzeme, işçilik)? | Katkı hesabı (§3.1). |
| 7 [Z] | Kargo ve paketleme kimde, sipariş başı kaç TL? | Katkıdan düşülür. |
| 8 | Ödeme komisyonu oranı (sanal POS, taksit, kapıda ödeme ücreti)? | Katkıdan düşülür. |
| 9 | İade, iptal ve kapıda ödemede teslim alınmama oranı? | Katkıdan karşılık olarak düşülür. Bilinmiyorsa *varsayım* yaz, 30 günde ölç. |
| 10 | Müşteri 12 ayda kaç kez tekrar alıyor? | LTV (§3.5). Ölçülmemişse LTV'ye dayanarak yüksek CPA kabul etme. |
| 11 | Reklam işletmede hangi kalemden ödenecek, vergi numarası Meta hesabına girili mi? | Maliyet çarpanı M 1,05 mi, 1,26 mı (§3.2)? Stopaj ve 2 No.lu beyanname konusunu mali müşavire yönlendir. |

### 1.3 Hedef ve zaman

| # | Soru | Cevaba göre karar |
|---|---|---|
| 12 [Z] | 90 gün sonra neyin değişmiş olmasını istiyorsunuz? Sayıyla söyleyin. | KPI cümlesi ve ölçüm yöntemi (§2.1). |
| 13 | Öncelik hangisi: hemen kâr mı, büyüme ve yeni müşteri mi, stok eritme mi? | Hedef CPA'nın başa baş CPA'ya göre konumu (§3.5). |
| 14 | Önümüzdeki 3 ayda önemli tarih var mı (açılış, sezon, 11.11, Efsane Cuma, bayram)? | Takvim. Kara Cuma 2026: 27 Kasım. Kreatifleri zirveden en az 2–3 hafta önce yayına al (*sezgisel*). İndirim dönemi eşiklerini önceden hesapla (§3.7). |

### 1.4 Mevcut veri ve ölçüm

| # | Soru | Cevaba göre karar |
|---|---|---|
| 15 [Z] | Daha önce Meta reklamı verdiniz mi? Son 90 günün harcama, sonuç ve satış verisini paylaşabilir misiniz? Reklamsız son 8–12 haftada haftalık sipariş, net gelir ve yeni müşteri sayısı neydi? | Taban çizgisi. Meta verisi yoksa ilk 2 hafta taban çizgisi dönemidir (§4.5). Reklamsız dönem verisi artımlı ölçümün tabanıdır (§2.1, §3.1). |
| 16 | Piksel ve Dönüşümler API'si kurulu mu? Altyapı hangisi (ikas, Shopify, T-Soft, WooCommerce…)? | Kurulu değilse satış optimizasyonu yapılamaz (Kurulum Kılavuzu §3.7). Altyapının hazır entegrasyonu varken üstüne elle kod eklenirse olaylar çift sayılabilir **[PRATİSYEN]** (altyapı destek sayfaları). |
| 17 | Piksel sipariş değerini KDV dahil mi gönderiyor? | RY ROAS eşiği Piksel çarpanı P ile değişir (§3.2). Bilinmiyorsa bir test siparişiyle kontrol et. |
| 18 | WhatsApp'tan gelen satışlar nerede kayıtlı (etiket, Excel, CRM)? | Kayıt yoksa satış ölçülemez. WhatsApp Business'ta satış etiketleme ilk görev olur. Kurulum tablonun altında. |
| 19 | Müşteri listesi var mı? Pazarlama için açık rıza alınmış mı? | Kitle olarak kullanılabilir mi? KVKK sorusu hukukçuya gider. Reklam ayarlarında "mevcut müşteri" ve "etkileşimli kitle" segmentlerini kurmak brif görevidir (§2.1). Özellik hesaba göre değişebilir. |

**#18 kurulumu: WhatsApp Business uygulamasında satış sinyali [RESMİ]** (ayrıntı: Modül 05 §7.2)

1. **Paylaşımı aç:** Uygulamada Araçlar > Müşterilerinizin hareketleri > "Meta'nın etkinlikler hakkında bilgi almasına izin ver" düğmesini aç. Menü adı sürüme göre değişebilir.
2. **Yalnız hazır etiketleri kullan:** "Yeni Sipariş", "Ödeme Bekleniyor", "Ödendi", "Sipariş tamamlandı". Meta özel etiketli sohbetlerin alışveriş olarak ilişkilendirilmeyebileceğini yazıyor. İşletmeye "Satıldı" gibi kendi etiketini kurdurma. Uygulamanın Siparişler özelliğinde durumu "Ödendi" yapmak da alışveriş sayılır (bazı ülkelerde kapalı).
3. **7 gün sınırı:** Sohbet WhatsApp'a yönlendiren bir reklamdan başlamış olmalı ve reklam tıklamasından sonraki 7 gün içinde etiketlenmeli. Satışı aynı gün ya da en geç ertesi gün etiketle (*sezgisel*).

- **Uygunluk:** Önceki 30 günde 10 ya da daha fazla alışveriş olayı paylaşılınca "Mesajlaşma yoluyla alışveriş" hedefi seçilebilir. Birden fazla Sayfa için reklam veriliyorsa her Sayfa koşulu ayrı sağlar **[RESMİ]**.
- **Satış 7 günden uzun sürüyorsa** (teklif → keşif → iş): (a) Müşteri işi kabul edip ödeme aşamasına geçtiği anda sohbete "Ödeme Bekleniyor" etiketi ver. Kabul yokken etiketleme; sahte alışveriş sinyali sistemi yanlış kişilere yönlendirir (*sezgisel*). (b) Ya da İşletme Mesajlaşması için Dönüşümler API'sini kullanan bir mesajlaşma ortağıyla CRM'den olay gönder **[RESMİ]**. 7 günlük sınırın API yolunda nasıl uygulandığı bu çalışmada doğrulanmadı. Her iki durumda da kararı CRM'deki aşama verisiyle ver (§2.2).

### 1.5 Kapasite ve operasyon

| # | Soru | Cevaba göre karar |
|---|---|---|
| 20 [Z] | Ayda en fazla kaç ek sipariş ya da iş karşılayabilirsiniz? Stok ne durumda? | Kapasite tavanı (§4.3). |
| 21 [Z] | Mesajlara ve aramalara kim, hangi saatlerde bakıyor? Bir mesaja ortalama kaç dakikada dönülüyor? | Reklam yayın saatleri ve teşhis (§6.3). |
| 22 | Teslim süresi kaç gün? Reklamda vaat edilen süre tutuyor mu? | Teslim süresinin reklamdaki vaatle uyumlu olması yasal şart **[MEVZUAT]**. |

### 1.6 Satış süreci

| # | Soru | Cevaba göre karar |
|---|---|---|
| 23 [Z] | İlk temastan satışa adımlar neler (mesaj → fiyat → keşif → teklif → ödeme)? | Huni geri hesabı (§2.3). |
| 24 | 10 talepten kaçı satışa dönüyor? Adım adım biliyor musunuz? | Hedef CPL ya da hedef sohbet maliyeti. |
| 25 | Satışa dönmeyenler en çok neden kayboluyor? | Fiyat, güven, yavaş dönüş, yanlış kitle: hangisi reklam sorunu, hangisi değil (§6.2). |
| 26 | Satış ne kadar sürede kapanıyor (aynı gün mü, 3 hafta mı)? | Uzun döngüde 7 günlük raporlar satışı göstermez. Değerlendirme penceresini uzat. WhatsApp'ta etiketleme sınırı için §1.4 #18. |

### 1.7 Rakipler ve pazar

| # | Soru | Cevaba göre karar |
|---|---|---|
| 27 | En güçlü 3 rakip kim? Fiyatları sizden yüksek mi, düşük mü? | Teklif kontrolü (§6.5). Meta Reklam Kitaplığı'nda rakiplerin yayındaki reklamlarına bak. |
| 28 | Aynı ürün pazaryerlerinde daha ucuza satılıyor mu? | Öyleyse site satışı zorlaşır. Paket, hediye ya da hizmetle fark yarat. |

### 1.8 Yasal kısıt ve politika

| # | Soru | Cevaba göre karar |
|---|---|---|
| 29 [Z] | Şu alanlardan birinde misiniz: sağlık hizmeti (hekim, diş hekimi, klinik, hastane, estetik), ilaç, takviye edici gıda, kilo verme ürünü, alkol, tütün ya da e-sigara, bahis ya da şans oyunu, flört ya da arkadaşlık, kredi/yatırım/forex, kripto, silah, falcılık/medyumluk/astroloji? Sağlık tesisi olmayan bir salonda botoks, dolgu, lazer gibi tıbbi işlem yapıyor musunuz? | **Kırmızı kapı:** Yayından önce dur, insan ya da hukukçu onayı iste. **Türkiye'de reklamı hiç yapılamayanlar** (Meta izin verse bile): alkollü içki, tütün ve e-sigara, beşerî tıbbi ürün (ilaç), yasa dışı bahis, yasa dışı şans oyunu ve kumar, falcı/medyum/astrolog hizmeti, sohbet/arkadaşlık/eş bulma hizmeti, dar istisnalar dışında silah **[MEVZUAT]**. Meta da Türkiye'yi alkol reklamının yasak olduğu ülkeler arasında sayıyor **[RESMİ]**. **Sağlık:** Türkiye'deki hekim ve sağlık kuruluşlarına ücretli tanıtım genel kural olarak yasak. İki dar istisna var: yeni tesiste açılıştan sonraki ilk ay; yetki belgeli sağlık turizminde yalnız yurt dışı konum, Türkçe dışı dil ve otomatik kitle genişletmesi (Advantage+ kitle gibi) kapalı reklam **[MEVZUAT]**. Salonda tıbbi işlem tanıtımı ruhsatsız sağlık hizmeti sayılabilir; kapsam doğrulanmadı. Tam risk kapısı: Modül 09 §1. |
| 30 | Emlak, konaklama, seyahat acentası ya da MEB'e bağlı kurs mu? Reklamda seçim, siyaset ya da sosyal mesele konusu var mı? | **Sarı kapı:** Emlak ilanlarında EİDS doğrulamalı bağlantı ve fiyat zorunlu **[MEVZUAT]**. Konaklamada geçerli belge ve belge numarası gerekir **[MEVZUAT]**. Özel öğretim kurumu reklamında öğrenci resmi ya da bilgisi kullanmak cezaya konu **[MEVZUAT]**. **Seçim ve siyaset:** Türkiye, Meta'nın bu reklamlar için yetkilendirme ve "Anlaşmalı olan:" sorumluluk reddi istediği ülkeler listesinde **[RESMİ]**. Bu kapsamdaki reklam yetkisiz yayınlanırsa onaylanmaz **[RESMİ]**. Yalnız sosyal mesele içeren reklam için Türkiye'de zorunluluk Meta'nın listesinde görünmüyor. İçerik ve hesap bazında doğrula. |
| 30a | Ürün sayfasında ve reklamda hangi iddialar var: doğal, organik, kimyasalsız, doğa dostu, akne, leke, kırışıklık, "uzman önerisi"? Her birinin belgesi var mı? (Kozmetik, temizlik, takviye ve gıda ürünlerinde **[Z]**) | **Sarı kapı: iddia kontrolü.** Reklamdaki doğrulanabilir her iddia, yayın dönemini kapsayan bilimsel ya da akredite belgeyle kanıtlanmalı (TRHTUY md. 7, 9) **[MEVZUAT]**. "Doğa dostu", "çevre dostu" gibi genel çevre beyanı açıklamasız kullanılamaz. Beyanın kapsamı, ölçüm yöntemi ve akredite belgesi gerekir (md. 17, 1 Ağustos 2026'dan beri) **[MEVZUAT]**. Gıda ve takviye dışı ürünlerde sağlık beyanı (tedavi eder, iyileştirir, hastalığa karşı korur) kullanılamaz **[MEVZUAT]**. Hekim, eczacı ya da sağlık kuruluşunun ürüne sağlık beyanı veriyormuş izlenimi yasak (md. 16/3) **[MEVZUAT]**. Önlüklü "uzman" görünümünün de buna girdiği *sezgisel* bir yorumdur. Kozmetiğe özel iddia mevzuatı bu pakette okunmadı, doğrulanmalı. Belgesiz iddiayı yayından önce reklamdan ve ürün sayfasından çıkar: Modül 04 §11.3, Modül 09 §5.2. |
| 31 | İndirim duyurusu yapacak mısınız? | Üstü çizili önceki fiyat, aynı kanalda indirimden önceki 10 günün en düşük fiyatı olmalı (1 Ağustos 2026'dan beri) **[MEVZUAT]**. Eşikleri indirimli fiyatla yeniden hesapla (§3.7). |
| 32 | Yurt dışına satış ya da hizmet var mı? | Konut ve kredi reklamlarında Özel Reklam Kategorisi ABD, Kanada ve Avrupa listesindeki ülkelerde zorunlu. Türkiye kitlesinde zorunlu değil **[RESMİ]**. |

### 1.9 Bütçe ve erişimler

| # | Soru | Cevaba göre karar |
|---|---|---|
| 33 [Z] | Aylık reklam bütçesi ne kadar? Bu para 3 ay boyunca ayrılabilir mi? İlk ayın sonunda hangi sonuçta durmayı kabul edersiniz? | Bütçe öğrenme minimumuna yetiyor mu (§4.2)? 1 aydan kısa taahhüt varsa beklentiyi düşür. Durdurma ölçütü test sözleşmesine yazılır (§4.6). |
| 34 | Sayfa, Instagram, reklam hesabı ve işletme portfolyosu kimin adına? Bize hangi yetki verilecek? | Erişim planı (Kurulum Kılavuzu §3.3–3.5). Tek Instagram hesabına bağımlılık riskli; WhatsApp, site ve onaylı müşteri listesi yedek kanal olmalı. |
| 35 | Fotoğraf, video, müşteri yorumu ya da içerik üreticisi var mı? | Kreatif üretim planı. Reklamda yalnız satın alması doğrulanmış yorum kullanılabilir **[MEVZUAT]**. |

### 1.10 Brif kartı (doldurulmuş örnek, *varsayım*)

Görüşmeden sonra bu kartı doldur ve müşteriye onaylat. Sonraki bütün hesaplar bu karttan beslenir.

```
İŞLETME        : "Örnek Kozmetik" – cilt bakımı, sitede ve Instagram'da "doğal" diye tanıtılıyor;
                 kendi sitesi (ikas), Türkiye geneli kargo
TEKLİF         : 3'lü bakım seti; 2 kutu üstü ücretsiz kargo; 14 gün iade
SİPARİŞ TUTARI : 900 TL KDV dahil (KDV %20) → 750 TL KDV hariç
DEĞİŞKEN GİDER : ürün 240 TL | kargo+paket 75 TL | ödeme komisyonu %3,5 | iade karşılığı %6
VERGİ NO       : Meta hesabında girili → M = 1,05
PİKSEL DEĞERİ  : KDV dahil gönderiyor (test siparişiyle görüldü) → P = 1,20
TEKRAR ALIM    : 12 ayda müşteri başı 1,8 sipariş (son 12 ayın sipariş dökümünden)
HEDEF (90 gün) : tabanın üstüne ayda 200 ek yeni müşteri; reklam maliyeti düşüldükten sonra
                 net gelirin ≥ %15'i kâr
KPI ÖLÇÜMÜ     : yeni müşteri = ikas'ta ilk siparişi bu dönemde olan müşteri
                 Meta payı = artımlı taban yöntemi (birincil); UTM son tıklama yan gösterge (§2.1)
                 yeni müşteri CAC = fatura ÷ (yeni müşteri − taban yeni müşteri)
TABAN          : reklamsız son 12 hafta: ayda ~120 sipariş ≈ 90.000 TL net; bunların ~70'i yeni müşteri
KAPASİTE       : günde 40 siparişe kadar paketleme; stok 3 ay yeterli
YANIT          : DM/WhatsApp hafta içi 09–19, ortalama ilk yanıt ~20 dk (müşteri beyanı, ölçülecek)
VERİ           : son 90 günde Meta harcaması yok (RY taban çizgisi ilk 2 haftada oluşacak)
YASAL KAPI     : SARI – iddia kontrolü (§1.8 #30a). "Doğal" iddiasının belgesi istendi; yoksa reklamdan
                 ve ürün sayfasından çıkacak. Akne/leke gibi tedavi çağrıştıran ifade ve önlüklü "uzman"
                 görseli yok. "14 gün iade" yasal cayma hakkıysa avantaj gibi sunulmayacak (Modül 04 §5.3).
                 İndirimde 10 gün kuralı. Yayın: kontrol tamamlanınca
BÜTÇE          : ayda 20.000 TL RY ≈ 21.000 TL fatura, 3 ay taahhüt
TEST SÖZLEŞMESİ: 1. ay (30 gün) test | test bütçesi 20.000 TL RY | başarı: son 14 günde işletme CPA
                 ≤ 358 TL | durdurma: bütçe bitince işletme CPA > 466 TL → dur, §6 teşhisi (§4.6)
                 | sonuç yön göstergesidir (doğrulama bütçesinin altında) | müşteri yazılı onayı: __/__
GÖREVLER       : hedef kitle segmentlerini kur (mevcut müşteri = ikas müşteri listesi + satın alanlar;
                 etkileşimli = site ziyaretçisi, sepete ekleyen; hesaba göre değişebilir, Modül 03 §7.2)
                 | iade oranını ve ilk yanıt süresini ölç | UTM şablonunu uygula (Modül 05 §6)
DOĞRULUK KAYN. : ikas sipariş sistemi (KDV hariç ciro, yeni/eski müşteri ayrımı)
```

---

## 2. Hedefi ölçülebilir KPI'ya çevirme

### 2.1 KPI cümle kalıbı ve ölçüm tanımları

Her hedefi şu kalıpla yaz:

> **[Kime ait sonuç]** + **[sayı]** + **[süre]** + **[ölçüm kaynağı ve atama yöntemi]** + **[koruma sınırı]**

- Kötü: "Satışları artıralım."
- Yine kötü: "Meta kaynaklı yeni müşteri ayda 200, RY CPA ≤ 234 TL." "Meta kaynaklı" tanımsız. RY satın alma CPA'sı mevcut müşterilerin alımlarını ve görüntülemeden sonraki 1 gün içinde gelen organik alımları da sayar.
- İyi: "1 Aralık–28 Şubat arasında, ikas'ta ilk siparişini veren **yeni müşteri** sayısını reklamsız tabanın (ayda ≈ 70) üstüne ayda **200 ek** müşteri çıkar. Sayı **ikas sipariş sisteminden** alınır, Meta payı **artımlı taban yöntemiyle** hesaplanır. Koruma sınırı: 14 günlük **yeni müşteri CAC'ı 246 TL'yi** (hedef, fatura TL) geçmez. Günlük işletim sinyali: 7 günlük **RY CPA ≤ 234 TL**."

**Ölçüm tanımları (seçilen yöntem brif kartına yazılır):**

- **Yeni müşteri** = sipariş sisteminde ilk siparişi bu dönemde olan müşteri. Aynı kişiyi telefon ya da e-postayla tekilleştir.
- **Meta payı** iki yöntemden biriyle ölçülür:
  - **(a) UTM son tıklama:** Sipariş sisteminde kaynağı Meta UTM'li olan siparişler (UTM şablonu: Modül 05 §6). Kolaydır, kampanya ve reklam kırılımı verir. Görüntüleme sonrası, cihaz değiştiren ve uygulama içi tarayıcıdan kopan alıcıyı kaçırdığı için **alt sınır** gibi okunur (Modül 05 §5.4).
  - **(b) Artımlı taban:** Dönemdeki sipariş ya da yeni müşteri sayısı − taban. **Taban** = reklamsız son 8–12 haftanın haftalık ortalaması × dönemdeki hafta sayısı (*sezgisel*). Ölçüm dönemi taban döneminden farklı bir sezondaysa (Kasım indirimleri, bayram, yılbaşı) tabanı geçen yılın aynı dönem oranıyla düzelt ya da kararı sezon sonrasına bırak (*sezgisel*). Organik satışı sahiplenmez, ama küçük hacimde gürültülüdür. Daha titiz yöntemler: Modül 05 §11.
- **İşletme CPA** = fatura ÷ Meta'ya atanan sipariş (yöntem a ya da b).
- **Yeni müşteri CAC (işletme)** = fatura ÷ (yeni müşteri − taban yeni müşteri). Yöntem (a) seçildiyse payda UTM'li yeni müşteridir.
- **RY satın alma CPA'sı** günlük işletim sinyalidir. KPI'nın koruma sınırı yapılmaz.
- **Meta'nın kendi kırılımı:** Reklam ayarlarında "Hedef kitle segmentleri" altında mevcut müşteri ve etkileşimli kitle tanımlanırsa satış kampanyası raporları yeni, etkileşimli ve mevcut müşteri kırılımı verir. Bu tanımlar hesaptaki Advantage+ satış kampanyalarına da otomatik uygulanır **[RESMİ]**. Meta bu özelliğin herkesin kullanımına açılmamış olabileceğini yazıyor; hesaba göre değişebilir. Kurulum ve formüller: Modül 03 §7.2–7.3. Bu kırılım da Meta ilişkilendirmesidir, sipariş sisteminin yerine geçmez.

### 2.2 Hedef → KPI → Meta olayı tablosu

| Müşterinin hedefi | Birincil KPI (doğruluk kaynağı) | Meta'da optimize edilecek olay | İzlenecek ara metrikler | Koruma sınırı |
|---|---|---|---|---|
| Site satışı (e-ticaret) | Yeni müşteri sayısı ve katkı (sipariş sistemi, §2.1'deki atama yöntemiyle) | Satın alma (Piksel + CAPI) | BGBM (CPM), bağlantı TO (CTR), sepete ekleme, ödeme başlatma, RY CPA | İşletme CPA ve yeni müşteri CAC ≤ hedef; artımlı MER ≥ başa baş (§3.1) |
| WhatsApp/DM'den satış (yerel hizmet, butik) | Satılan iş sayısı (WhatsApp etiketi, Excel) | Önce konuşma ya da potansiyel müşteri. 30 günde 10+ alışveriş etiketi birikince "mesajlaşma yoluyla alışveriş" **[RESMİ]**. Etiket ve 7 gün koşulları: §1.4 #18 | Sohbet maliyeti, sohbet → teklif, teklif → satış | Sohbet maliyeti ≤ hedef CPA × sohbet→satış oranı |
| Form ile talep (B2B, eğitim, emlak) | Nitelikli talep ve satış (CRM) | Potansiyel müşteri. CRM CAPI ile bağlanınca ve ayda 200+ talep olunca "nitelikli potansiyel müşteri" **[RESMİ]** | CPL, ulaşılabilme oranı, nitelikli oran | Nitelikli talep başı maliyet |
| Telefon araması (acil hizmet) | Gerçekleşen iş (çağrı kaydı) | Aramalar, yalnız telefona bakılan saatlerde **[RESMİ]** | Arama başı maliyet, cevaplanan arama oranı | Cevapsız arama oranı |

**Eğer-ise kuralları:**

- Müşteri yalnız "bilinirlik" istiyorsa → ölçülebilir bir satış ya da talep KPI'sı öner. Erişim kampanyasını ancak gerçekten satış hedefi yoksa kur (*sezgisel*).
- Hedef satışsa ve ölçüm kuruluysa → satış olayına optimize et. Konuşma ya da tıklama sayısını başarı sayma.
- Haftalık beklenen satış 50'nin çok altındaysa → §4.4'teki karar tablosuna git. Varsayılan, satın almada kalmaktır. Olayı değiştirmeden önce bütçe, set sayısı ve sipariş tutarı seçeneklerini değerlendir.
- WhatsApp satışı Meta'ya bildirilmiyorsa → sistem satın alana değil, mesaj atmaya yatkın kişiye optimize etmeye devam eder **[PRATİSYEN]**. İlk görev satış etiketlemesi (§1.4 #18).
- Satış döngüsü 7 günden uzunsa → Meta'nın standart ilişkilendirmesi (en fazla 7 gün tıklama + 1 gün görüntüleme) **[RESMİ]** satışın bir kısmını göstermez. Kararı CRM'deki aşama verisiyle ver.

### 2.3 Huni geri hesabı (talep ve sohbet işleri için)

```
Talep→satış oranı  = (talep→ulaşılan) × (ulaşılan→nitelikli) × (nitelikli→satış)
Hedef CPL          = Hedef CPA × talep→satış oranı
Gerekli talep      = Hedef satış ÷ talep→satış oranı
Gerekli bütçe      = Gerekli talep × Hedef CPL        (RY tutarı; fatura = RY × M)
Koşul              : Hedef satış ≤ boş kapasite (§4.3)
```

**Örnek (*varsayım*, B2B yazılım hizmeti):** RY hedef CPA 6.000 TL. Ulaşılan %80, nitelikli %40, kapanış %25 → talep→satış = 0,80 × 0,40 × 0,25 = **%8**. Hedef CPL = 6.000 × 0,08 = **480 TL**. Ayda 10 satış için 10 ÷ 0,08 = **125 talep** gerekir. Bütçe ≈ 125 × 480 = **60.000 TL RY**.

Oranlar müşterinin kendi kaydından gelmeli. Bir araç satıcısının ev hizmetleri için verdiği "talep→randevu %20–40" aralığı yöntemsiz **[PRATİSYEN]**, hedef olarak kullanma.

---

## 3. Birim ekonomisi: formüller ve çözülmüş örnekler

### 3.1 Tanımlar

- **Net gelir** = KDV hariç sipariş tutarı. Tüketici fiyatı KDV dahildir. Genel oran %20, bazı ürünlerde %10 ya da %1.
- **Katkı (sipariş başı)** = Net gelir − ürün/hizmet değişken maliyeti − kargo ve paket − ödeme komisyonu − iade/iptal karşılığı. Kira ve maaş gibi sabit giderler girmez.
- **Katkı marjı** = Katkı ÷ Net gelir.
- **Başa baş CPA (fatura TL)** = Katkı. Fatura CPA ya da işletme CPA bundan yüksekse satış zarar yazar.
- **Net başa baş ROAS** = 1 ÷ katkı marjı (KDV hariç gelir ÷ fatura cinsinden). Kaynakta brüt marjla hesap yapmanın sonucu ciddi biçimde iyimser çıkardığı gösteriliyor **[PRATİSYEN]**.
- **Hedef ROAS (kâr hedefli)** = 1 ÷ (katkı marjı − hedef kâr marjı) **[PRATİSYEN]**.
- **Toplam MER** (pazarlama verimlilik oranı) = Dönemin toplam net geliri ÷ dönemin toplam reklam faturası. Yalnız **trend göstergesi** olarak kullan. Reklamsız organik satışı da içerdiği için reklam hiç satış getirmese bile yüksek çıkabilir.
- **Artımlı MER** = (Dönemin net geliri − taban net gelir) ÷ dönemin reklam faturası (*türetilmiş*). Taban, §2.1'deki gibi reklamsız son 8–12 haftanın haftalık ortalamasıdır (*sezgisel*); sezon farkı varsa aynı düzeltmeyi uygula. Meta'nın ilişkilendirmesinden bağımsız bir kontroldür. **Koruma sınırı: artımlı MER ≥ 1 ÷ katkı marjı.**

**Örnek A ile (*varsayım*):** Taban ≈ 120 sipariş × 750 TL = 90.000 TL net/ay. Reklam bütçesi 20.000 TL RY = 21.000 TL fatura. Reklam hiç satış getirmese bile toplam MER = 90.000 ÷ 21.000 ≈ **4,3** çıkar ve başa baş 2,09'u rahatça geçer. Artımlı MER ise 0'dır. Reklamlı ayda net gelir 135.000 TL olduysa artımlı MER = 45.000 ÷ 21.000 ≈ **2,14**. Bu, başa başın (2,09) yalnızca biraz üstüdür.

### 3.2 Türkiye düzeltmeleri

| Düzeltme | Neden | Çarpan |
|---|---|---|
| Konum ücreti | Türkiye kitlesine gösterilen reklamda harcamanın %5'i faturaya ayrı satır olarak eklenir, RY'de görünmez **[RESMİ]** | M = 1,05 |
| Vergi numarası yok | Meta, harcama + konum ücreti toplamı üzerine %20 KDV ekler **[RESMİ]**. İndirim hakkı yoksa bu doğrudan maliyettir | M = 1,05 × 1,20 = 1,26 |
| Piksel KDV dahil değer gönderiyor | RY'deki gelir, KDV hariç gelirden KDV oranı kadar yüksek görünür | P = 1,20 (%10'luk üründe 1,10, %1'likte 1,01). Yalnız ROAS'a girer |
| Stopaj | İşletmeye göre değişiyor, kaynaklar ayrışıyor | Varsayılan olarak ekleme. Mali müşavire sor |
| 1 Ocak 2027 | Dijital Hizmet Vergisi %2,5'e iniyor. Meta'nın %5'lik ücreti değişir mi, belli değil | Ocak 2027'de Meta sayfasını yeniden oku |

**RY eşikleri (kullanacağın genel formül, *türetilmiş*):**

```
RY başa baş CPA   = Katkı ÷ M
RY hedef CPA      = (Katkı − hedef kâr tutarı) ÷ M
RY başa baş ROAS  = P × M ÷ katkı marjı
RY hedef ROAS     = P × M ÷ (katkı marjı − hedef kâr marjı)

M = 1,05 (vergi no girili)   | 1,26 (vergi no girili değil)
P = 1,00 (Piksel KDV hariç)  | 1 + KDV oranı (Piksel KDV dahil; %20'de 1,20)
```

**Ters çeviri (RY'de gördüğün sayıyı fatura cinsine çevir, *türetilmiş*):**

```
Fatura CPA  = RY CPA × M            (CPA'da gelir olmadığı için P girmez)
Fatura ROAS = RY ROAS ÷ (P × M)     (net başa baş ROAS = 1 ÷ katkı marjı ile kıyaslanır)
```

Örnek A'da (P = 1,20, M = 1,05) RY ROAS 2,64 → 2,64 ÷ 1,26 = **2,09**, yani kampanya tam başa baştadır. Yalnız ÷ 1,05 yapılırsa 2,51 çıkar. Bu sayı başa baş 2,09'un üstünde göründüğü için kâr etmeyen kampanya kârlı sanılır.

**Dört durum (Örnek A: katkı 358,50 TL, katkı marjı %47,8, hedef kâr %15):**

| Vergi no | Piksel değeri | P × M | RY başa baş ROAS | RY hedef ROAS | RY başa baş CPA | RY hedef CPA |
|---|---|---|---|---|---|---|
| Girili | KDV hariç | 1,00 × 1,05 = 1,05 | 2,20 | 3,20 | 341 TL | 234 TL |
| Girili | KDV dahil | 1,20 × 1,05 = 1,26 | **2,64** (Örnek A) | **3,84** | **341 TL** | **234 TL** |
| Girili değil | KDV hariç | 1,00 × 1,26 = 1,26 | 2,64 | 3,84 | 285 TL | 195 TL |
| Girili değil | KDV dahil | 1,20 × 1,26 = 1,512 | 3,16 | 4,61 | 285 TL | 195 TL |

**Dikkat:** 2. ve 3. satır aynı ROAS eşiğine düşer (P × M = 1,26). Ama sebepleri farklıdır ve CPA eşikleri farklıdır. 1,26'yı gördüğünde "vergi no yok" çarpanı sanıp tek başına uygulama. Önce M'yi, sonra P'yi ayrı ayrı bul, ikisini çarp. Vergi numarası yokken Piksel de KDV dahil gönderiyorsa çarpan 1,512'dir.

**P'yi bulmak:** Test siparişi yap ya da Modül 05 §5.5'teki V oranını kullan. Genel oranlı (%20) üründe P ≈ V × 1,20.

### 3.3 Örnek A: e-ticaret (§1.10 brif kartı, *varsayım*)

| Kalem | Hesap | Sonuç |
|---|---|---|
| Net gelir | 900 ÷ 1,20 | **750 TL** |
| Ürün maliyeti | — | 240 TL |
| Kargo + paket | — | 75 TL |
| Ödeme komisyonu | %3,5 × 900 | 31,50 TL |
| İade karşılığı | %6 × 750 | 45 TL |
| **Katkı** | 750 − 240 − 75 − 31,50 − 45 | **358,50 TL** |
| Katkı marjı | 358,50 ÷ 750 | **%47,8** |
| Başa baş CPA (fatura TL) | = katkı | 358,50 TL |
| **RY başa baş CPA** | 358,50 ÷ 1,05 | **≈ 341 TL** (vergi no olmasaydı ≈ 285 TL) |
| Net başa baş ROAS | 1 ÷ 0,478 | 2,09 |
| **RY başa baş ROAS** (P 1,20 × M 1,05) | 1,26 ÷ 0,478 | **≈ 2,64** (diğer üç durum §3.2 tablosunda) |
| Hedef kâr tutarı (%15) | 0,15 × 750 | 112,50 TL |
| Hedef CPA (fatura TL) | 358,50 − 112,50 | 246 TL |
| **RY hedef CPA** | 246 ÷ 1,05 | **≈ 234 TL** |
| **RY hedef ROAS** (P 1,20 × M 1,05) | 1,26 ÷ (0,478 − 0,15) | **≈ 3,84** |

**Okuma:** RY ROAS 2,64'ün altındaysa bu kampanya zarar ediyor. 2,64 ile 3,84 arasındaysa kâr hedefinin altında ama zararda değil. "ROAS 3 iyi mi?" diye soran müşteriye bu örnekte doğru cevap şudur: "Zarar etmiyorsunuz, ama %15 kâr hedefinizin altındasınız." Aynı RY ROAS 3, vergi numarası girili olmasaydı (eşik 3,16) zarar demekti. RY ROAS Meta'nın ilişkilendirmesiyle sayılır. Aylık kararı işletme ROAS'ı ve artımlı MER ile teyit et (§3.1).

**"ROAS iyi mi?" protokolü:** Tek bir sayı söyleme. Sırayla şunları sor: katkı marjı ne, Piksel KDV dahil mi gönderiyor (P), vergi numarası girili mi (M)? Sonra §3.2 formülüyle eşiği hesapla ve RY'deki sayıyla karşılaştır.

### 3.4 Marjinal CPA (ölçeklerken)

Bütçe arttıkça ucuz fırsatlar biter ve sonuç başına maliyet yükselir **[RESMİ]**. Kararı ortalamaya göre değil, ek harcamanın getirdiği ek sonuca göre ver. İki bütçe seviyesini (A: eski, B: yeni) karşılaştır (*türetilmiş*):

```
Ek harcama    = B'deki toplam RY harcaması − (A'nın haftalık ort. harcaması × B'deki hafta sayısı)
Ek sonuç      = B'deki toplam sonuç        − (A'nın haftalık ort. sonucu   × B'deki hafta sayısı)
Marjinal CPA  = Ek harcama ÷ Ek sonuç      (RY eşikleriyle kıyaslanır)
```

**Okuma koşulları (*sezgisel*):**

- **Her seviye en az 2 tam hafta.** Tek haftalık kıyasla karar verme.
- **Artıştan sonraki ilk haftayı kıyasa katma.** Meta, bütçe değişikliğinin büyüklüğüne göre reklam setini öğrenme aşamasına yeniden sokabileceğini söylüyor, ama bir yüzde eşiği vermiyor **[RESMİ]**. Öğrenme aşamasında CPA genellikle daha yüksek ve oynaktır **[RESMİ]**. Bu hafta geçici olarak şişmiş olabilir.
- **Artışı kademeli yap.** Adım büyüklüğü ve bekleme süresi: Modül 06 §6.2.
- **Ek sonuç en az ~30 olmalı.** Daha azsa fark gürültüden ayrılamaz. Örneğin 36 ve 46 satışlık iki haftanın her biri Poisson gürültüsüyle kabaca ±6–7 satış oynar. Aradaki 10 satışlık fark bu gürültüyle aynı büyüklüktedir (gürültü kuralı: Modül 07 §3.3).

**Karar:**

| Durum | Eylem |
|---|---|
| Ek sonuç < ~30 | Karar verme. Seviyeyi sabit tut, 2 hafta daha topla ve yeniden hesapla |
| Marjinal CPA ≤ RY hedef CPA | Bir adım daha artırılabilir |
| RY hedef < marjinal CPA ≤ RY başa baş | Bütçeyi sabit tut, yeni kreatif partisi ekle |
| Marjinal CPA > RY başa baş | Bir önceki bütçe seviyesine dön |

**Örnek (Örnek A eşikleriyle, *farklı bütçeli varsayım*):** Brif kartındaki 20.000 TL/ay (≈ 4.700 TL/hafta) bütçeyle ölçekleme kararı yoktur. Aşağıdaki tutarlar, müşterinin bütçeyi artırmayı yazılı onayladığı ayrı bir varsayımdır. RY hedef CPA 234 TL, RY başa baş CPA 341 TL.

| Dönem | Günlük bütçe (RY) | Haftalık harcama | Satış | Not |
|---|---|---|---|---|
| Hafta 1–2 (A) | 1.500 | 10.500 | 52 + 50 = 102 | Ort. CPA ≈ 206 TL |
| Hafta 3 | 1.800 (+%20) | 12.600 | — | Uyum haftası, kıyasa girmez |
| Hafta 4 | 2.150 (+%20) | 15.050 | — | Uyum haftası, kıyasa girmez |
| Hafta 5–6 (B) | 2.150 | 15.050 | 68 + 66 = 134 | Ort. CPA ≈ 225 TL |

Ek harcama = 30.100 − 2 × 10.500 = 9.100 TL. Ek sonuç = 134 − 2 × 51 = 32 (≥ 30, okunabilir). Marjinal CPA = 9.100 ÷ 32 ≈ **284 TL**. Ortalama CPA (225 TL) hâlâ hedefin altında görünüyor. Ama son artışla gelen satışların her biri hedefin (234 TL) üstüne, başa başın (341 TL) altına mal oluyor. **Karar:** bütçeyi 2.150 TL'de sabit tut, yeni kreatif partisi ekle, artırmaya devam etme. B haftaları 58 + 56 = 114 satış getirseydi ek sonuç yalnız 12 olurdu. O zaman karar verilmez, iki hafta daha beklenirdi.

### 3.5 LTV ve izin verilen edinme maliyeti (CAC)

```
LTV (katkı bazlı) = Ortalama katkı/sipariş × Müşteri başı sipariş sayısı (seçilen ufukta)
Abonelikte        = Aylık müşteri başı katkı ÷ aylık kayıp (churn) oranı
Geri ödeme süresi = CAC ÷ aylık müşteri başı katkı
```
**[PRATİSYEN]** (LTV hesaplayıcı kaynakları).

- **Ufuk:** 6–12 ay kullan. TÜFE Eylül 2026'da yıllık %29,73 oldu **[HABER]**. Uzun ufukta nominal TL hesap iyimser sonuç verir (*sezgisel*).
- **LTV:CAC ≈ 3:1** yaygın bir pratisyen kuralıdır, yasa değildir **[PRATİSYEN]**. Katkı bazlı LTV ile kullanıldığında daha sıkı bir testtir.
- Tekrar oranı **ölçülmemişse** LTV'ye dayanarak ilk sipariş başa baş CPA'sının üstüne çıkma.
- CAC'ı yeni müşteri CAC'ı olarak ölç (§2.1). RY satın alma CPA'sı mevcut müşterileri de saydığı için CAC'ı olduğundan düşük gösterir.

**Örnek A devam (*varsayım*):** 12 ayda 1,8 sipariş × 358,50 TL = **645 TL** LTV katkısı.

| Strateji (müşteriye seçtir) | İzin verilen CAC (fatura TL) | RY CPA tavanı | Ne zaman |
|---|---|---|---|
| Kârlı büyüme (LTV ÷ 3) | 215 TL | **≈ 205 TL** | Nakit sıkışık, risk istemiyor |
| İlk siparişte %15 kâr | 246 TL | **≈ 234 TL** | Brif kartındaki hedef |
| İlk siparişte başa baş | 358,50 TL | **≈ 341 TL** | Tekrar oranı ölçülmüş, nakit 3–6 ay bekleyebiliyor |
| 12 ayda başa baş (LTV × 1) | 645 TL | ≈ 614 TL | Yalnız güçlü nakit ve kanıtlanmış tekrar alımla. Agresif; önerme (*sezgisel*) |

### 3.6 Örnek B: WhatsApp'tan satan yerel hizmet (*varsayım*)

Ev temizliği şirketi. İş başı 4.000 TL KDV hariç. Değişken maliyet (personel, malzeme, ulaşım) 2.600 TL. Vergi numarası girili (M = 1,05). Son 3 ayın WhatsApp kayıtlarına göre 100 reklam sohbetinin 20'si işe dönüşüyor.

| Kalem | Hesap | Sonuç |
|---|---|---|
| Katkı | 4.000 − 2.600 | 1.400 TL (marj %35) |
| RY başa baş CPA | 1.400 ÷ 1,05 | ≈ 1.333 TL |
| **RY başa baş sohbet maliyeti** | 1.333 × 0,20 | **≈ 267 TL** |
| Hedef: reklam en fazla katkının yarısını alsın (*sezgisel*) | 700 ÷ 1,05 | RY hedef CPA ≈ 667 TL |
| **RY hedef sohbet maliyeti** | 667 × 0,20 | **≈ 133 TL** |
| LTV (12 ayda 3 iş) | 3 × 1.400 | 4.200 TL. LTV ÷ 3 = 1.400 TL, yani ilk iş başa başı tekrar alımla destekleniyor |

**Karar kuralı:** RY sohbet maliyeti 133 TL'nin altındaysa ve sohbet→iş oranı %20 civarında kalıyorsa devam et. Sohbet maliyeti düşük ama oran düşüyorsa sorun büyük olasılıkla yanıt hızında ya da teklifte. §6'ya git.

### 3.7 Kampanya ve indirim dönemi

İndirim, paket fiyatı, hediye ya da ücretsiz kargo eşiğindeki değişiklik katkıyı değiştirir. Tam fiyat eşikleriyle kampanya dönemi yönetilirse yapay zekâ zarar eden satışı kârlı sanar.

**Kurallar (*türetilmiş*; kampanya süresince ayrı eşik kullanımı *sezgisel*):**

1. Kampanya başlamadan katkıyı kampanya fiyatıyla yeniden hesapla:
   `İndirimli katkı = İndirimli net gelir − ürün maliyeti − kargo ve paket (ücretsiz kargo eşiği değiştiyse yeni ortalamayla) − hediye maliyeti − ödeme komisyonu (indirimli KDV dahil fiyattan) − iade karşılığı (indirimli net gelirden; kampanyada iade oranı farklıysa onunla)`
2. Kampanya süresince RY eşiklerini (§3.2) bu katkıyla kullan. Kampanya bitince tam fiyat eşiklerine dön. İki dönemi aynı tabloda kıyaslama.
3. Kataloğun yalnız bir kısmı indirimdeyse beklenen satış karışımına göre ağırlıklı ortalama katkı kullan (*sezgisel*).
4. Piksel indirimli tutarı gönderir. RY ROAS bu yüzden indirimli gelirle okunur ve kıyaslanacak eşik yükselir.
5. Kâr hedefini müşteriyle yeniden konuş. İndirim döneminde %15 kâr hedefi çoğu zaman tutmaz. "İlk siparişte başa baş + yeni müşteri" hedefi seçilebilir; LTV tablosuna bak (§3.5) (*sezgisel*).

**Örnek A, %20 indirim (*varsayım*: hediye yok, kargo değişmiyor, iade oranı aynı):**

| Kalem | Tam fiyat | %20 indirim |
|---|---|---|
| KDV dahil fiyat | 900 TL | 720 TL |
| Net gelir | 750 TL | 600 TL |
| Ürün maliyeti | 240 TL | 240 TL |
| Kargo + paket | 75 TL | 75 TL |
| Ödeme komisyonu (%3,5 × KDV dahil) | 31,50 TL | 25,20 TL |
| İade karşılığı (%6 × net) | 45 TL | 36 TL |
| **Katkı** | **358,50 TL** | **223,80 TL** |
| Katkı marjı | %47,8 | %37,3 |
| RY başa baş CPA (M 1,05) | ≈ 341 TL | **≈ 213 TL** |
| RY başa baş ROAS (P 1,20 × M 1,05) | ≈ 2,64 | **≈ 3,38** |
| RY hedef CPA (%15 kâr) | ≈ 234 TL | (223,80 − 90) ÷ 1,05 ≈ **127 TL** |
| RY hedef ROAS (%15 kâr) | ≈ 3,84 | 1,26 ÷ (0,373 − 0,15) ≈ **5,65** |

**Okuma:** Başa baş CPA %38 düşüyor (358,50 → 223,80 TL). Kampanyada dönüşüm oranı genelde artar, ama CPM de yükselir (§5.3). Kampanya boyunca tam fiyat eşiğiyle (341 TL) karar verilirse, RY CPA'sı 213–341 TL arasında kalan her satış zarar ettirir. Modül 04 §5.4'teki "katkı − KDV hariç indirim" kısa yolu komisyon ve iade düşüşünü saymaz. Bu örnekte 208,50 TL verir, yani biraz ihtiyatlı kalır.

---

## 4. Bütçe hesaplama

### 4.1 Meta'nın resmî eşikleri

| Kural | Değer | Kaynak |
|---|---|---|
| Genel bütçe kontrolü | Günlük bütçe ≥ seçilen performans hedefinin (optimizasyon olayının) **ortalama ücretinin 10 katı** | **[RESMİ]** |
| Öğrenmeden çıkış | Son önemli düzenlemeden sonraki bir haftada **yaklaşık 50 sonuç** | **[RESMİ]** |
| Öğrenme aşamasında | Yayın daha istikrarsız, CPA genellikle daha yüksek | **[RESMİ]** |
| Hedefe dayalı teklif (sonuç başına ücret ya da ROAS hedefi) | En iyi çalışma **haftada 50–100+ dönüşüm**. Ücret hedefi, hedefe uymak için bütçenin tamamını harcamayabilir | **[RESMİ]** |
| Sonuç başına ücret hedefi kullanılıyorsa | Günlük bütçe ≥ **hedefin 5 katı** | **[RESMİ]** |
| Geçmiş veri yoksa | Önce en az **2 hafta** en yüksek hacim stratejisiyle taban çizgisi | **[RESMİ]** |
| Hedef değişikliği sonrası | En az **7 gün** bekle. Ölçeklemeden önce en az **3 hafta** yayın | **[RESMİ]** |
| Bütçe değişikliği | Büyüklüğüne göre öğrenmeyi yeniden başlatabilir; yüzde eşiği verilmiyor | **[RESMİ]** |
| Çok yüksek bütçe | Sonuç başına maliyet yükselebilir | **[RESMİ]** |
| Günlük esneklik | Günde %175'e kadar, haftada 7 kata kadar harcama | Kurulum Kılavuzu §5.2 |

### 4.2 Öğrenme minimumu

Bütün bütçe formüllerinde aynı CPA'yı kullan: **planlama CPA'sı**.

```
Planlama CPA (RY)   = max(beklenen CPA, RY hedef CPA)
   Beklenen CPA     : hesabın son 30–90 günlük RY CPA'sı (taban çizgisi)
   Yeni hesapta     : taban yok → beklenen CPA yerine RY başa baş CPA'yı kullan (sezgisel)

Günlük minimum (RY) = max( Planlama CPA × 50 ÷ 7 ;  10 × Planlama CPA ;  ücret hedefi varsa 5 × hedef )
Haftalık beklenen sonuç N          = Günlük bütçe × 7 ÷ Planlama CPA
Desteklenebilir reklam seti sayısı ≈ N ÷ 50
```

- **Neden hedef CPA tek başına değil?** Hedef CPA, hedefin tutturulacağını varsayar. Yeni hesapta ve öğrenme aşamasında gerçekleşen CPA genellikle daha yüksek ve oynaktır **[RESMİ]**. Hedefle hesaplanan bütçe bu yüzden sistematik olarak düşük çıkar.
- **Neden yeni hesapta başa baş?** Başa baştan yüksek bir CPA zaten programın zarar ettiği anlamına gelir. Plan yapmaya değer en kötü CPA budur (*sezgisel*). İlk 2 haftada gerçekleşen RY CPA'sı gelince beklenen CPA'yı onunla değiştir.
- **Üç kural ne işe yarar?** 50 ÷ 7 formülü Meta'nın haftalık 50 sonuç eşiğinden türetilmiş bir pratisyen kuralıdır **[PRATİSYEN]**. 50 **haftalık** bir sayıdır, günlük değil. 50 ÷ 7 ≈ 7,1 olduğu için aynı CPA ile Meta'nın 10× kuralı **[RESMİ]** her zaman daha büyük çıkar. Satın alma optimizasyonunda belirleyici olan odur. 10× bütçe, planlama CPA'sında haftada ≈ 70 sonuç demektir. CPA dalgalansa da 50 eşiğinin üstünde kalmak için pay bırakır (*türetilmiş*). 50 ÷ 7 formülü daha gevşektir, ama N'yi ve set sayısını o verir. 5× kuralı yalnız ücret hedefi varken ek bir alt sınırdır. Hedef tutarla hesaplandığı için çoğu zaman en küçük çıkar.

**Örnek A (*varsayım*, yeni hesap):** Planlama CPA = max(RY başa baş 341, RY hedef 234) = **341 TL**.
- 50 ÷ 7 kuralı: 341 × 50 ÷ 7 ≈ 2.439 TL/gün.
- 10× kuralı: 10 × 341 ≈ **3.414 TL/gün** → ayda ≈ **102.400 TL RY**, ≈ 107.600 TL fatura.
- Ücret hedefi 234 TL girilirse 5× kuralı: 1.171 TL/gün.
- Esas: **3.414 TL/gün.** İyimser alt sınır (ilk 2 haftanın CPA'sı hedefte ya da altındaysa): 10 × 234 ≈ 2.343 TL/gün ≈ 70.300 TL/ay RY.

Müşterinin bütçesi ayda 20.000 TL → günlük ≈ 667 TL → N = 667 × 7 ÷ 341 ≈ **14 satış/hafta** (CPA hedefte tutarsa ≈ 20). Set büyük olasılıkla "Öğrenmeyle sınırlı" kalır. Bu bir ceza değildir **[RESMİ]**, ama CPA daha oynak olur. Karar için §4.4'e git.

**Hedef–bütçe tutarlılık kontrolü:** Brif kartındaki "ayda 200 ek yeni müşteri" hedefi için 200 × 341 ≈ **68.300 TL/ay RY** gerekir (CPA hedefte tutarsa 200 × 234 ≈ 46.900 TL). 20.000 TL ile ayda ≈ 59–85 satış beklenir. Bu hesap RY satın alma CPA'sıyla yapıldı. Mevcut müşteri ve organik payı düşüldüğünde yeni müşteri CAC'ı daha yüksek çıkar, yani açık daha da büyük olabilir. Ya hedef inecek ya bütçe artacak. Bu çelişkiyi yayından önce müşteriye yazılı olarak göster (§5).

### 4.3 Kapasite tavanı

```
Boş kapasite (iş/ay) = Aylık azami kapasite − mevcut iş
Gerekli sohbet/talep = Boş kapasite ÷ sohbet→satış oranı
Kapasite bütçesi     = Gerekli sohbet × RY hedef sohbet maliyeti
Önerilen bütçe       = min(Kapasite bütçesi, müşterinin bütçesi)
```

**Örnek B (*varsayım*):** 3 ekip × günde 1 iş × 26 gün = 78 iş/ay. Mevcut 50 iş → boş kapasite **28 iş**. Gerekli sohbet 28 ÷ 0,20 = **140/ay**. Bütçe 140 × 133 ≈ **18.700 TL/ay RY** (≈ 622 TL/gün, faturada ≈ 19.600 TL).

Bu bütçe haftada ≈ 33 sohbet getirir, yani 50'nin altında kalır. 10× kuralı da sağlanmıyor (10 × 133 ≈ 1.330 TL/gün). Haftada 50 sohbet için ≈ 952 TL/gün gerekir. Bu da ayda ≈ 43 iş demektir, yani kapasiteyi aşar. **Karar:** Kapasite tavanı öncelikli olduğu için (§0.5) 622 TL/gün ile kal ve "öğrenmeyle sınırlı" uyarısını kabul et. Ekip eklemek ya da bölge genişletmek işletmenin kararıdır. Karşılanamayan talep şikâyete döner (*sezgisel*).

### 4.4 Bütçe yetmezse: karar tablosu

N = haftalık beklenen satın alma (§4.2, planlama CPA'sıyla). Aşağıdaki bantlar *sezgisel*dir. Meta yalnız "haftada yaklaşık 50" eşiğini veriyor **[RESMİ]**.

| Durum (N, *sezgisel* bant) | Önce dene | Sonra | Yapma |
|---|---|---|---|
| Haftada 25–50 satın alma | Tek kampanya, tek reklam seti, Advantage+ kitle. Satın almada kal | 2–3 hafta sonra yeniden değerlendir | Birden çok sete bölmek. Ücret hedefi koymak (koşulu §5.2'de) |
| Haftada 10–25 | **Satın almada kal** (varsayılan). "Öğrenmeyle sınırlı" bir ceza değildir **[RESMİ]**. Sipariş tutarını artır (paket, ücretsiz kargo eşiği). Bütçe artışını görüş | Kararları 2 tam haftalık toplamlarla ver (§3.4) | Sığ olaya erken geçmek. Tıklama ya da trafiğe optimize edip satış beklemek |
| Haftada 10'dan az | Satın almada başla. Teklifi güçlendir, düşük fiyatlı giriş ürünü dene **[PRATİSYEN]**. Site dışı satışta talep ya da mesaj odaklı kurgu (§2.2). Sonuçları aylarla değerlendir | Aşağıdaki koşullar oluşursa sığ olaya geç. Bütçe ya da kapsam (ürün, bölge) değişikliğini müşteriyle konuş | Reklam bazında kapatma kuralı uygulamak; bu hacimde güvenilir değil **[PRATİSYEN]** |
| Kapasite dolu | Bütçeyi kapasiteye göre düşür | Fiyatı ya da kârlılık hedefini yükselt | "Öğrenme için" bütçe artırmak |

**Sığ olaya geçiş kuralı (*sezgisel*):**

- **Ne zaman:** Hepsi birlikte geçerliyse: en az 2 tam haftadır haftada 10'dan az satın alma geliyor; set bu sürede "Öğrenmeyle sınırlı" kaldı; haftalık CPA karar verilemeyecek kadar oynuyor; bütçe artışı mümkün değil. Meta, bütçe artırılamıyorsa daha kolay gerçekleşen bir olaya optimize etmeyi öneriyor **[RESMİ]**. Ne zaman geçileceğini ise söylemiyor.
- **Sıra:** Önce ödeme başlatma, yetmezse sepete ekleme (Modül 02 §2.2). Yönlendirme sayfası görüntülemesine ya da tıklamaya inme. Sığ olay ucuz ama satmayan trafik getirebilir.
- **İzleme:** Geçişten sonra her hafta sipariş sisteminden **satın alma başına işletme CPA'sını** hesapla. Sığ olayın ucuzlaması başarı sayılmaz.
- **Geri dönüş:** Satın alma başına işletme CPA'sı 2 tam hafta üst üste başa başın (fatura TL) üstündeyse ya da geçiş öncesindeki 2 haftadan kötüyse satın alma optimizasyonuna dön. Öğrenmeyle sınırlı durumu kabul et.

### 4.5 Test bütçesi

**a) Doğrulama testi (ilk kez reklam veren hesap):**

```
Doğrulama bütçesi ≈ 14 gün × günlük öğrenme minimumu (§4.2)
```

Dayanak: geçmiş veri yokken en az 2 hafta taban çizgisi oluşturma önerisi **[RESMİ]**. Örnek A: 14 × 3.414 ≈ **47.800 TL RY** (≈ 50.200 TL fatura). Müşteri bu kadarını ayıramıyorsa en az 7 tam gün yayınla ve sonucun "yön göstergesi" olduğunu test sözleşmesine yaz (§4.6) (*sezgisel*).

**b) Süren hesapta kreatif testi:**

- Meta'nın kreatif testi arayüzü, mevcut reklamları korumak için teste bütçenin **en fazla %20'sini** ayırmayı öneriyor. Araç yalnız "en yüksek hacim" stratejisiyle çalışıyor, varsayılan süre 7 gün **[PRATİSYEN]** (arayüzü deneyen uzmanın aktarımı).
- Bir reklam hakkında karar vermeden önce en az **3 × RY hedef CPA** harca. 0 satış varsa kapat. 1 satış varsa yaklaşık 5 kata kadar bekle **[PRATİSYEN]**. Uzun vadeli CPA'sı tam hedefte olan bir reklamın 3 kat harcamada hiç satış getirmeme olasılığı yaklaşık %5'tir (Poisson hesabı, *türetilmiş*).

```
Ay içinde adil test edilebilecek kreatif sayısı ≈ (aylık bütçe × 0,20) ÷ (3 × RY hedef CPA)
```

Örnek A: 102.400 TL bütçeyle ≈ 20.480 ÷ 703 ≈ **29 kreatif**. 20.000 TL bütçeyle ≈ 4.000 ÷ 703 ≈ **5 kreatif**. Daha fazlasını açarsan hiçbiri anlamlı veri toplamaz.

**c) İlk 72 saat kuralı:** Bu sürede yalnız operasyonel sebeple kapat: ret, sıfır yayın, kırık bağlantı ya da izleme, yanlış fiyat. Yüksek ilk gün CPA'sı kapatma sebebi değildir **[PRATİSYEN]** (Kurulum Kılavuzu §6.3).

### 4.6 Test sözleşmesi: program düzeyinde başarı ve durdurma

§4.5 b'deki 3× kuralı tek bir reklamı kapatır. Test sözleşmesi ise bütün programın ne zaman sürdürüleceğini, ne zaman durdurulacağını baştan yazar. Böylece müşteriye neye söz verildiği somutlaşır (§5.1). Bütün eşikler *sezgisel*dir. Sözleşmeyi yayından önce müşteriye yazılı onaylat ve brif kartına ekle (§1.10).

| Alan | Kural (*sezgisel*) | Örnek A |
|---|---|---|
| Süre | En az 4 tam hafta. İlk 72 saat yalnız operasyonel kontrol (§4.5 c) | 1. ay (30 gün) |
| Test bütçesi | RY tutarı olarak yazılır; fatura = RY × M | 20.000 TL RY ≈ 21.000 TL fatura |
| Ölçüt | Son 14 günün işletme CPA'sı (§2.1'deki atama yöntemiyle) | Artımlı taban yöntemi |
| Başarı | İşletme CPA ≤ başa baş CPA (fatura TL) → sürdür. Hedefin de altındaysa ölçeklemeyi konuş (§3.4) | ≤ 358 TL sürdür; ≤ 246 TL ise ölçekleme konuşulur |
| Gri bölge | Başa baş < işletme CPA ≤ başa baş × 1,3 → bir tur daha: yeni kreatif partisi, teklif ya da açılış sayfası değişikliği. Bütçe artışı yok | 358–466 TL |
| Durdurma | Test bütçesinin tamamı harcandığında işletme CPA > başa baş × 1,3 → harcamayı durdur ve §6 teşhisine dön. Teklif, fiyat ya da maliyet değişmeden yeniden başlatma | > 466 TL |
| Erken durdurma | Test bütçesinin yarısı harcandığında sipariş sisteminde Meta'ya atanabilen hiç satış yoksa dur. Ölçüm bozuksa (§6.2) harcamayı durdur, ölçümü düzelt, sonra devam et | 10.000 TL'de 0 satış |
| Küçük örnek | Son 14 günde Meta'ya atanan satış 20'den azsa kararı bütün test dönemine göre ver | — |

Örnek A'da test bütçesi doğrulama bütçesinin (≈ 47.800 TL, §4.5 a) altında. Bu yüzden sözleşmeye "sonuç yön göstergesidir" cümlesi yazılır.

---

## 5. Beklenti yönetimi

### 5.1 Söz ver / söz verme

| Söz verilebilir | Söz verilemez |
|---|---|
| Sürecin kendisi: haftalık rapor, test takvimi, kapatma ve ölçekleme kuralları | Belirli bir ROAS, CPA ya da satış sayısı. Meta hedef stratejilerinde bile hedefe uyulacağını garanti etmiyor **[RESMİ]** |
| Eşiklerin şeffaf hesabı (§3) | "İlk haftadan kâr" |
| Test sözleşmesindeki durdurma ölçütü aşılınca harcamayı durdurma (§4.6). Ölçeklemede marjinal CPA başa başı aşınca bütçeyi geri çekme (§3.4) | Türkiye ya da sektör "ortalamasını" tutturmak; güvenilir bir ortalama yok |
| Ölçüm kurulumu ve satışın doğruluk kaynağından raporlanması | Meta raporunun sipariş sistemiyle birebir tutması |

Hedefli teklifi (ücret ya da ROAS hedefi) müşteriye "maliyeti kilitleme" aracı olarak sunma. Bu teklif yalnız haftada yaklaşık 50 ve üstü dönüşümde iyi çalışır ve hedefe uyulacağı garanti edilmez **[RESMİ]**. Geçiş koşulu §5.2'de, düşük hacimde ne yapılacağı §4.4'te.

### 5.2 Zaman çizelgesi (müşteriye bu tabloyu göster)

| Dönem | Müşteri ne görür | Biz ne yaparız |
|---|---|---|
| Gün 0 | — | Ölçüm kontrolü, eşikler, test sözleşmesi onayı, 3–5 farklı kreatif |
| Gün 1–3 | Dalgalı ve pahalı sonuçlar | Yalnız operasyonel kontrol |
| Gün 4–7 | İlk eğilim | Açıkça kötü reklamı 3× kuralıyla kapatma |
| Gün 8–14 | İlk tam hafta değerlendirmesi | Kampanya düzeyinde karar, yeni kreatif partisi |
| Gün 15–30 | Taban çizgisi oturur | Ölçekleme yalnız §3.4 koşullarıyla ve kademeli. **Hedefli teklife geçiş yalnız şu durumda:** son 2 haftada haftada ≥ 50 dönüşüm var (Meta'nın önerisi 50–100+) ve ücret kontrolü iş gereği. O zaman hedefi son dönemin CPA'sının %10–20 üstünden başlat **[RESMİ]**. Koşul yoksa en yüksek hacimde kal. Düşük hacimde ücret hedefi yayını daraltır ve bütçenin tamamı harcanmayabilir **[RESMİ]** (§4.4) |
| Gün 30 | Test sonucu | Test sözleşmesine göre sürdür, bir tur daha ya da durdur (§4.6) |
| Ay 2–3 | İşletme CPA'sı ve LTV verisi | Hedeflerin ölçülmüş oranlarla güncellenmesi |

Takvim, Meta'nın 7 gün, 2 hafta ve 3 hafta önerilerinden türetilmiş bir sentezdir (*sezgisel*).

### 5.3 Müşteri cümlesi → doğru yanıt

| Müşteri der ki | Yanıt |
|---|---|
| "Dün çok pahalıydı, kapatalım." | Meta sonuçların en az bir tam hafta üzerinden değerlendirilmesini öneriyor **[RESMİ]**. Günlük dalgalanma normal. Reklam kapatma kuralımız 3× hedef CPA, programı durdurma kuralımız test sözleşmesinde (§4.6). |
| "CPM arttı, reklam kötü." | Dönüşüm optimizasyonunda BGBM (CPM) tek başına iyi bir performans göstergesi değil. Sistem daha ucuz dönüşüm için daha pahalı gösterim alabilir **[RESMİ]**. Bakılacak sayı CPA. |
| "Meta 40 satış diyor, sipariş sisteminde Meta UTM'li 31 sipariş var." | **Önce kontrol, sonra açıklama.** Fark sürekli Meta lehine ve belirgin büyükse (ör. birkaç haftadır %20'den fazla, *sezgisel*; bu örnekte %29) önce ölçümü kontrol et: Olay Yöneticisi'nde tekilleştirme; çift Piksel ya da hem altyapı entegrasyonu hem elle kod (§1.4 #16, Modül 05 §2.3); Modül 05 §5.5'teki kapsama (K) ve Meta payı (P) oranları; aynı dönemi "7 gün tıklama" sütunuyla karşılaştırıp görüntüleme payına bak (Modül 05 §5.2). Meta'nın saydığı satış sipariş sistemindeki **toplam** siparişten fazlaysa kesin ölçüm hatası vardır. Kontroller temizse müşteriye şunu söyle: "Bu fark beklenir. Meta görüntüleme sonrası ve cihazlar arası satışı da sayar, UTM bunları kaçırır. Kararlarımızı sipariş sisteminden veriyoruz." Kontrol hata gösterirse §6.2'deki ölçüm satırına geç: kararları dondur, ölçümü düzelt. |
| "Fatura bütçeden fazla geldi." | Türkiye kitlesinde %5 konum ücreti bütçenin dışında, faturaya ayrı eklenir **[RESMİ]**. Vergi numarası yoksa KDV de eklenir (Kurulum Kılavuzu §9). |
| "Kasımda neden pahalandı?" | Küresel veride medyan markanın CPM'i Kara Cuma haftasında eylül seviyesinin %59 üstüne çıktı **[PRATİSYEN]** (ABD ağırlıklı veri). Türkiye'ye özel ölçülmüş veri yok. Kendi hesabımızın geçen yılki aynı dönemiyle ve Meta'nın "Açık Artırma Rekabeti Değişimi" ölçümüyle kıyaslıyoruz **[RESMİ]**. İndirim dönemindeki eşiklerimiz ayrı (§3.7). |
| "Bütçeyi ikiye katlayalım, satış da ikiye katlansın." | Ölçekte maliyet yükselir **[RESMİ]**. Kademeli artırıp her seviyeyi en az 2 tam hafta izliyor ve marjinal CPA'ya bakıyoruz (§3.4). |

---

## 6. Reklamın çözemeyeceği sorunlar: teşhis kontrol listesi

### 6.1 Yayın öncesi hazırlık kontrolü

Her maddeyi **Evet / Hayır / Bilinmiyor** diye işaretle. Kırmızı madde "Hayır" ise yayın açma. Sarı maddede yayını aç, sorunu müşteriye yazılı bildir.

| # | Kontrol | Seviye |
|---|---|---|
| 1 | Yasal kapı yeşil ya da gerekli onay alındı. Sarı kapıda iddia kontrolü tamamlandı (§1.8) | Kırmızı |
| 2 | Katkı pozitif ve RY başa baş CPA, hesabın gerçekçi CPA'sına yakın (bilinmiyorsa test bütçesi ve test sözleşmesi kabul edildi, §4.6) | Kırmızı |
| 3 | Stok ya da kapasite, hedef satışı karşılıyor | Kırmızı |
| 4 | Satış ölçülüyor: Piksel/CAPI test siparişini görüyor ya da WhatsApp satış etiketi/Excel kaydı kuruldu (§1.4 #18) | Kırmızı |
| 5 | Mesai içinde ilk yanıt hedefi tutuyor (§6.3) | Sarı |
| 6 | Site mobilde hızlı ve ödeme adımı çalışıyor (§6.4) | Sarı |
| 7 | Teklif rakiplere göre tek cümleyle ayrışıyor (§6.5) | Sarı |
| 8 | Son 3 ayda çözülmemiş müşteri şikâyeti yığılması yok (Google Haritalar yorumları, Şikayetvar, pazaryeri mağaza puanı) | Sarı |

### 6.2 Teşhis matrisi (yayındaki hesap, 7 günlük veriyle)

| Belirti | Olası sebep | Sorun kimde | Kanıt nasıl toplanır | Eylem |
|---|---|---|---|---|
| TO normal, açılış sayfası görüntüleme ÷ bağlantı tıklaması düşük | Sayfa yavaş, yönlendirme hatası | Site | PageSpeed Insights; mobil hücresel ağda deneme | Siteyi düzelt. Kreatif değiştirme **[PRATİSYEN]** |
| TO iyi, tüm reklamlarda dönüşüm oranı düşük | Fiyat, teklif, güven, ödeme adımı | Teklif ya da site | Test siparişi, rakip fiyat karşılaştırması | Teklif ya da sayfa testi |
| Sohbet çok, satış az | Yavaş ya da kalitesiz yanıt, fiyatı sohbette sakladığı için kopma | Satış ekibi | Gizli müşteri testi (§6.3) | Yanıt süreci, hazır yanıtlar, mesai planı |
| Form talebi çok, ulaşılamayan çok | Geç arama, düşük niyetli form | İkisi | CRM'de "ilk arama süresi" | Hızlı arama düzeni. "Daha yüksek satın alma niyeti" form türü |
| Meta satış diyor, sipariş sisteminde yok ya da sürekli çok daha az | Çift sayım, tekilleştirme hatası, yanlış olay, KDV dahil değer, görüntüleme sonrası organik alımı sahiplenme | Ölçüm | Olay Yöneticisi, test siparişi, 7 gün tıklama sütunu, Modül 05 §5.5 oranları (kontrol sırası §5.3) | Ölçümü düzelt, kararları dondur |
| CPA iyi, toplam ciro artmıyor | Reklam zaten gelecek müşteriyi satın alıyor | Belirsiz | Artımlı MER (§3.1), yeni/eski müşteri ayrımı (§2.1) | Yeni müşteri hedefi, artırımlılık testi (Modül 05 §11) |
| Satış geliyor, iade ya da iptal yüksek | Vaat ile ürün uyuşmuyor, teslim gecikiyor | Ürün ya da operasyon | İade nedenleri | Reklam vaadini daralt. Operasyonu düzelt |

### 6.3 Gizli müşteri testi (WhatsApp ve telefon)

1. Farklı bir numaradan **5 mesaj** at: 2'si mesai içinde, 1'i öğle arasında, 1'i akşam, 1'i hafta sonu. Bir aramayı da cevapsız bırakıp geri dönüş olup olmadığına bak.
2. Her biri için kaydet: ilk yanıt süresi (dakika), fiyat verildi mi, sonraki adım önerildi mi, 24 saat içinde takip mesajı geldi mi?
3. Karar kuralları:
   - Mesai içinde ilk insan yanıtı **15 dakikayı** geçiyorsa yanıt sorununu müşteriye bildir (*sezgisel* hedef). Reklamı yalnız yanıt verilebilen saatlerde yayınla. Meta form reklamları için bunu açıkça öneriyor **[RESMİ]**. Saat planlama ayarının hangi bütçe türünde açık olduğu hesaba göre değişebilir, doğrula. Arama reklamlarında yalnız mesai saatinde yayın, bir Meta deneyinde %25,4 daha fazla arama dönüşümü getirdi **[RESMİ]** (yöntem açıklanmamış).
   - Mesai dışı için uzakta mesajı ve karşılama mesajı yoksa kur.
   - WhatsApp Business Platformu (API) kullanılıyorsa: reklamdan gelen kişiye **24 saat içinde** yanıt verilirse 72 saatlik ücretsiz mesajlaşma penceresi açılır **[RESMİ]**. Geç yanıt maliyeti de artırır.
4. Dış kanıt: 2011'de yayımlanan bir ABD çalışmasına göre web talebine ilk bir saat içinde dönmeye çalışan firmaların talebi nitelendirme olasılığı, daha geç dönenlerin yaklaşık 7 katıydı **[ÇALIŞMA]** (Harvard Business Review). Veri eski; yalnız "hız önemlidir" ilkesinin desteği olarak kullan. "5 dakikada 21 kat" gibi pratisyen çarpanları birbiriyle çelişiyor, kullanma.

### 6.4 Site kontrolü

1. **Hız:** pagespeed.web.dev üzerinden mobil ölçüm al. Google'ın "iyi" eşikleri 75. persentilde şöyle: LCP ≤ 2,5 sn, INP ≤ 200 ms, CLS ≤ 0,1 **[RESMİ]**. Google'ın ısmarladığı bir Deloitte çalışmasında (37 marka), mobil hızdaki 0,1 saniyelik iyileşme perakendede %8,4, seyahatte %10,1 daha yüksek dönüşümle birlikte görüldü **[ÇALIŞMA]**. Gözlemsel bir çalışmadır, deney değildir.
2. **Açılış sayfası görüntüleme oranı:** Hesabın diğer kampanyalarına göre belirgin düşükse ya da %70'in altındaysa sayfayı incele (*sezgisel*, hesap içi kıyas daha güvenilir).
3. **Test siparişi:** Telefondan reklam bağlantısıyla gir. Ürünü bul, sepete ekle, öde, iade et. Her adımda takılma noktasını not et. Kapıda ödeme, taksit ve havale seçenekleri görünüyor mu?
4. **Yasal zorunluluklar:** Satıcı unvanı ve iletişim bilgisi, vergiler dahil toplam fiyat, teslim süresi ve cayma hakkı bilgisi (14 gün) ön bilgilendirmede yer almalı **[MEVZUAT]**. Eksikse hem dönüşüm hem ceza riski var.
5. **Reklam-sayfa uyumu:** Reklamdaki ürün, fiyat, kampanya ve iddialar açılış sayfasında aynen görünüyor mu? Sayfadaki iddialar da §1.8 #30a kontrolünden geçti mi?

### 6.5 Teklif kontrolü

- Ürün ya da hizmet pazaryerlerinde ve fiyat karşılaştırma sitelerinde daha ucuz mu?
- "Neden siz?" sorusunun cevabı somut mu (süre, garanti, paket, hizmet), yoksa genel mi ("kaliteli")?
- Kargo süresi ve ücreti rakiplerden kötü mü?
- Organik ya da tavsiyeyle hiç satış oldu mu? Hiç olmadıysa ürün-pazar uyumu bilinmiyor demektir. Yalnız doğrulama testi bütçesiyle başla (*sezgisel*).
- Teklifi güçlendirme yönleri: sonucu somutlaştır, kanıt ve garanti ekle, bekleme süresini ve müşterinin çabasını azalt **[PRATİSYEN]**. "Garanti" ya da "kesin sonuç" vaadinde yasal sınırlara uy.

### 6.6 Müşteriye söyleme kalıbı

> "Reklam tarafında [X] yapacağız. Ancak [Y kontrolünde] şunu gördük: [kanıt, sayı]. Bu düzelmeden reklam bütçesini artırmak, [sonuç: pahalı sohbet, kaçan satış] demek olur. Önerimiz: [işletme eylemi] ve [tarih]. O tarihe kadar bütçeyi [tutar] seviyesinde tutuyoruz."

Kanıtı (ekran görüntüsü, süre ölçümü, PageSpeed sonucu) ekle ve ölçüme dayalı konuş.

---

## Doğrulanması gerekenler

- **%5 konum ücreti 2027'de:** Dijital Hizmet Vergisi 1 Ocak 2027'de %2,5'e iniyor. Meta'nın Türkiye ücretinin değişip değişmeyeceği bilinmiyor. Ocak 2027'de Meta konum ücreti sayfası yeniden okunmalı; §3.2 çarpanları buna göre güncellenmeli.
- **Piksel değeri KDV dahil mi:** Altyapıya göre değişir. Her hesapta test siparişiyle kontrol edilmeli.
- **Stopaj ve KDV uygulaması:** İşletme türüne göre değişir. Mali müşavir teyidi gerekir.
- **"Mesajlaşma yoluyla alışveriş" ve "nitelikli potansiyel müşteri" hedefleri:** Kademeli açılıyor, koşullar hesaba göre değişebilir. Reklam Yöneticisi'nde görünürlüğü doğrulanmalı. WhatsApp'ta 7 günlük etiketleme sınırının İşletme Mesajlaşması için Dönüşümler API'si yolunda nasıl uygulandığı doğrulanmadı.
- **Hedef kitle segmentleri:** Meta özelliğin herkesin kullanımına açılmamış olabileceğini yazıyor. Reklam ayarlarında görünürlüğü doğrulanmalı.
- **10× ve 5× bütçe kuralları:** İki resmî sayfa farklı bağlamda farklı çarpan veriyor (genel performans hedefi 10×, ücret hedefi 5×). Bu modül ihtiyatlı olanı (büyük olanı) esas alıyor.
- **Saat planlama (dayparting):** Meta reklam setinde tarih ve saat planı kurulabildiğini yazıyor. Hangi bütçe türüyle kullanılabildiği bu araştırmada doğrulanmadı.
- **Kreatif testi aracındaki %20 önerisi:** Bir uzmanın arayüz gözlemi. Arayüz hesaba göre değişebilir.
- **HBR 2011 bulgusu:** Makalenin tam metni okunamadı (abonelik duvarı). "Yaklaşık 7 kat" ifadesi ikincil kaynaklardan doğrulandı.
- **Haftalık 50 sonuç eşiği:** Bazı 2026 blog yazıları Advantage+ satış kampanyalarında eşiğin düştüğünü iddia ediyor. Meta'nın Ekim 2026 Türkçe ve İngilizce sayfaları hâlâ yaklaşık 50 diyor.
- **Kozmetik iddiaları:** Kozmetik ürünlere özel iddia mevzuatı (Kozmetik Yönetmeliği, TİTCK kılavuzları) bu pakette okunmadı. "Doğal", "organik", "akne" gibi ifadelerin sınırı hukukçuyla doğrulanmalı.
- **Sosyal mesele reklamları:** Türkiye, Meta'nın seçim ve siyaset reklamlarında yetkilendirme istediği ülkeler arasında. Siyaset dışı sosyal mesele reklamlarında Türkiye için zorunluluk görünmüyor; içerik bazında doğrulanmalı.
- **Salonlarda tıbbi işlem tanıtımı:** Sağlık tesisi olmayan güzellik salonlarının botoks, dolgu gibi işlemleri tanıtmasının ruhsatsız sağlık hizmeti hükmüne girip girmediği doğrulanmadı.
- **Türkiye'ye özel CPM/CPL kıyası:** Yöntemi açık kaynak yok. Bu modüldeki örnek TL tutarları *varsayım*dır.

---

## Kaynaklar

**Meta [RESMİ]**
- Öğrenme aşaması (haftada ~50 sonuç; öğrenmede CPA daha yüksek ve oynak): https://www.facebook.com/business/help/112167992830700
- Önemli düzenlemeler ve öğrenme aşaması (bütçe değişikliğinin büyüklüğü): https://www.facebook.com/business/help/316478108955072
- Öğrenmeyle sınırlı ve çözümleri: https://www.facebook.com/business/help/269269737396981
- Performans hedefleri (günlük bütçe ≥ ortalama ücretin 10 katı): https://www.facebook.com/business/help/355670007911605
- Minimum bütçeler (5× kuralı): https://www.facebook.com/business/help/203183363050448
- Teklif stratejileri (haftada 50–100 dönüşüm): https://www.facebook.com/business/help/1619591734742116
- Sonuç başına ücret hedefi en iyi uygulamaları (2 hafta, %10–20, 3 hafta, bütçenin tamamını harcamayabilir): https://www.facebook.com/business/help/176339196621566
- Sonuçları yorumlama (tam hafta, CPM uyarısı, ölçekte maliyet artışı): https://www.facebook.com/business/help/1364841787225722
- Sonuç başına ücreti azaltma (yüksek bütçe uyarısı): https://www.facebook.com/business/help/321695409726523
- Konum ücretleri: https://www.facebook.com/business/help/1238737454289085
- Türkiye KDV: https://www.facebook.com/business/help/1020947424780313
- Açık Artırma Rekabeti Değişimi: https://www.facebook.com/business/help/2024547657774300
- İlişkilendirme ayarları: https://www.facebook.com/business/help/460276478298895?locale=tr_TR
- Hedef kitle segmentleri (yeni, etkileşimli, mevcut müşteri kırılımı): https://www.facebook.com/business/help/1212227546879178
- Potansiyel müşterilere yanıt ve çalışma saatleri: https://www.facebook.com/business/help/2246428158756947?locale=tr_TR
- Arama reklamları: https://www.facebook.com/business/help/378168646496279?locale=tr_TR
- Reklamların planlanması: https://www.facebook.com/business/help/1037425549606837
- Mesajlaşma yoluyla alışveriş koşulları (hazır etiketler, 7 gün, paylaşım ayarı): https://www.facebook.com/business/help/1214599109289826?locale=tr_TR
- Potansiyel müşteri performans hedefleri (nitelikli talep, CAPI şartı): https://www.facebook.com/business/help/782657799338685?locale=tr_TR
- CRM için Dönüşümler API'si (ayda 200 talep): https://developers.facebook.com/docs/marketing-api/conversions-api/conversion-leads-integration/
- WhatsApp Business Platformu ücretlendirme (24 saat / 72 saat): https://developers.facebook.com/docs/whatsapp/pricing
- Özel Reklam Kategorileri ülke kapsamı: https://www.facebook.com/business/help/1198401317374558?locale=tr_TR
- Sosyal mesele, seçim ve siyaset reklamlarının kullanılabilirliği (Türkiye: seçim ve siyaset reklamında yetkilendirme): https://www.facebook.com/business/help/2150157295276323?locale=tr_TR
- Alkol ilkesi: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/alcohol

**Google [RESMİ] ve araştırma [ÇALIŞMA]**
- Core Web Vitals eşikleri: https://web.dev/articles/defining-core-web-vitals-thresholds
- PageSpeed Insights: https://pagespeed.web.dev/
- Milliseconds Make Millions (Deloitte/Google): https://web.dev/case-studies/milliseconds-make-millions
- The Short Life of Online Sales Leads (HBR, Mart 2011): https://hbr.org/2011/03/the-short-life-of-online-sales-leads

**Mevzuat ve haber [MEVZUAT] [HABER]**
- Sağlık hizmetlerinde tanıtım yönetmeliği (RG 12.11.2025; ilk ay ve sağlık turizmi istisnaları, otomatik kitle kapalı şartı): https://www.resmigazete.gov.tr/eskiler/2025/11/20251112-2.htm
- Ticari Reklam ve Haksız Ticari Uygulamalar Yönetmeliği değişikliği: indirim için 10 gün kuralı, doğrulanmış yorum şartı, çevre beyanı (md. 17), kesin reklam yasakları (md. 27) (RG 01.07.2026): https://www.resmigazete.gov.tr/eskiler/2026/07/20260701-9.htm
- Ticari Reklam ve Haksız Ticari Uygulamalar Yönetmeliği, güncel konsolide metin (md. 7, 9, 16/3): https://www.mevzuat.gov.tr/File/GeneratePdf?mevzuatNo=20435&mevzuatTur=KurumVeKurulusYonetmeligi&mevzuatTertip=5
- Sağlık Beyanı Denetimi Hakkında Yönetmelik (RG 13.06.2023; gıda ve takviye dışı üründe sağlık beyanı yasağı): https://www.resmigazete.gov.tr/eskiler/2023/06/20230613-1.htm
- Alkol reklamı yasağı duyurusu: https://www.tarimorman.gov.tr/TADAB/Duyuru/280/Alkollu-Icki-Reklamlarina-Iliskin-Mevzuatta-Yapilan-Degisiklik-Hakkinda-Duyuru
- Emlak ilanlarında EİDS denetimi: https://www.ekonomist.com.tr/gundem/sosyal-medyadaki-emlak-ve-arac-ilanlarina-siki-denetim-82-isletmeye-8-2-milyon-tl-ceza-78039
- Belgesiz konaklama genelgesi: https://www.alomaliye.com/2026/08/18/belgesiz-konaklama-ilanlarina-siki-denetim/
- Özel öğretim kurumları reklam cezası (5580 m.7, haber): https://www.gzt.com/ekonomi/anayasa-mahkemesi-emsal-karar-ozel-okullara-kesilen-5-kat-katlamali-ceza-hukmu-tarihe-karisiyor-egitim-haberleri-4223845
- 6502 sayılı Kanun (mesafeli sözleşme, cayma): https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=6502&MevzuatTur=1&MevzuatTertip=5
- Mesafeli Sözleşmeler Yönetmeliği: https://www.mevzuat.gov.tr/MevzuatMetin/yonetmelik/7.5.20237.pdf
- Dijital Hizmet Vergisi oranı: https://aa.com.tr/tr/ekonomi/dijital-hizmet-vergisi-orani-yeniden-belirlendi/3780348
- TÜFE Eylül 2026: https://gazeteoksijen.com/ekonomi/eylulde-enflasyon-yillik-yuzde-29-73-aylik-yuzde-1-84-oldu-291680

**Pratisyen [PRATİSYEN]**
- Başa baş ROAS ve katkı marjı: https://adadvisor.ai/blog/break-even-roas-contribution-margin
- ikas Piksel ve Dönüşümler API'si bağlantısı (çift sayım uyarısı, tekilleştirme): https://support.ikas.com/tr/facebook-piksel-ve-donusumler-apisi-baglantisi
- Konum ücreti ve ROAS düzeltmesi: https://gokhantuz.com/meta-reklamlarinda-konum-ucreti/
- Minimum bütçe formülü: https://www.growthmentor.com/blog/how-much-budget-to-start-ads
- Reklam seti kapasitesi formülü: https://insights.vaizle.com/how-to-scale-facebook-ads/
- LTV hesaplayıcıları: https://goodmetrics.io/tools/cltv-calculator ve https://fullmetrix.com/en/tools/ltv-calculator
- 3× CPA kapatma kuralı: https://www.flighted.co/blog/how-to-know-when-to-pause-a-meta-ad
- Aşamalı kapatma ve açılış sayfası görüntüleme oranı: https://admanage.ai/blog/when-to-kill-a-facebook-ad
- Kreatif testi aracı (%20 önerisi): https://www.jonloomer.com/meta-creative-testing/
- Düşük hacimde teklif stratejisi: https://pubcast.jonloomer.com/a-simple-volume-focused-ad-strategy/
- Kara Cuma CPM verisi: https://lebesgue.io/facebook-ads/are-meta-ads-worth-it-black-friday
- WhatsApp satışını Meta'ya bildirmeme uyarısı: https://eazybe.com/tr/blog/click-to-whatsapp-reklamlari
- Ev hizmetleri talep analizi (talep→randevu iddiası): https://leadsync.me/blog/industries-home-services/
- Teklif değer denklemi (özet): https://www.supersummary.com/100m-offers/section-3-summary/
