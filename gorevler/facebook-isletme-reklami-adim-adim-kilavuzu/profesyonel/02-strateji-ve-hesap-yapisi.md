# Strateji, amaç seçimi ve hesap yapısı

> **Modül 02 · Profesyonel paket · Hazırlanma: 17 Ekim 2026 (denetim düzeltmesi 19 Ekim 2026).** Ekran adımları, menü adları, ödeme ve hata kodları burada yok. Onlar için **Kurulum Kılavuzu** (`../KILAVUZ.md`): amaç tablosu §4, dönüşüm konumu §5.1, bütçe esnekliği §5.2, hedef kitle ekranı §5.3, reklam alanları ekranı §5.4, değerlendirme §6.1, ilk 7 gün §6.3, konum ücreti ve KDV §3.6 ve §9.

## Bu modülde

- Kampanya açmadan önce yapılacak **hesap**: başabaş CPA/ROAS, Türkiye maliyet çarpanı (1,05 / 1,26) ve "bu bütçe kaç reklam setini besler?" formülü.
- **İş hedefi → amaç → dönüşüm konumu → performans hedefi** karar tablosu ve sinyal yetmediğinde inilecek olay merdiveni.
- Huni (soğuk/ılık/sıcak/mevcut müşteri) tanımları, bütçe dağılımı kuralları, Advantage+ ile manuel kurulum arasındaki eğer-ise kuralları.
- Dört doldurulmuş hesap yapısı şablonu (küçük bütçe, orta bütçe, e-ticaret, yerel hizmet), bütçe türü ve teklif stratejisi seçimi, reklam alanları, zamanlama.
- Kopyalanabilir bir **adlandırma standardı** (kodlar ve örnek adlar).

**Etiketler:** **[RESMİ]** Meta'nın kendi sayfası. **[PRATİSYEN]** ajans, danışman ya da araç sağlayıcı (çıkar çatışması olabilir). **[HABER]** haber sitesi ya da veri raporu. **[MEVZUAT]** Türk mevzuatı (ayrıntı Modül 09). *sezgisel* = ölçülmemiş pratisyen kuralı. *türetilmiş* = Meta kuralından bu modülde yapılan aritmetik. Meta'nın "%X daha iyi" rakamları Meta'nın kendi deneyleridir; yön gösterir, hesabına garanti değildir.

---

## 0. Karar sırası (her yeni hesapta bu sırayla ilerle)

1. **Mevzuat kapısı:** Ürün/hizmeti **Modül 09 §1 risk kapısı** tablosuna göre sınıflandır. **Kırmızıysa dur. Sarıysa insan onayı al.** Yeşilse genel kurallarla devam et. İki sınıfa giriyorsa sıkı olanı uygula. Aşağıdaki örnekler listeyi tamamlamaz, karar her zaman tablodan verilir:
   - kırmızı: alkol, tütün/e-sigara, beşerî ilaç, bahis ve şans oyunu;
   - kırmızı, dar istisna olabilir: sağlık tesisi ve sağlık meslek mensubu, avukat, kripto;
   - sarı: takviye, kilo verme, kredi/sigorta/yatırım, emlak, konaklama, MEB'e bağlı kurs, çocuk ürünü, siyaset, yapay zekâ ile üretilmiş insan figürü.

   Kapı yalnız ürüne bakmaz. Reklamda indirim ya da fiyat varsa Modül 09 §5.3'ü uygula. Müşteri listesi yüklenecekse Modül 03 §4.4'ü uygula. Sektör kartları Modül 08 §1.1'de.
2. **İş hedefi:** İşletme neyi sayıyor? Satış, randevu, arama, form, mağaza ziyareti?
3. **Ekonomi ve sinyal:** Başabaş CPA/ROAS'ı ve bütçenin kaç seti beslediğini hesapla (§1).
4. **Ölçüm:** Sonuç Meta'ya ulaşmıyorsa (Piksel+CAPI, WhatsApp etiketi, CRM yok) o sonuca optimize edemezsin.
5. Sırayla seç: amaç ve olay (§2), huni (§3), Advantage+ (§4), yapı (§5), bütçe türü (§6), teklif (§7), alanlar (§8), zamanlama (§9).
6. **Adlandır** (§10), kontrol listesini geç (§11), yayınla.

---

## 1. Ekonomi ve sinyal kapasitesi: formüller

| # | Formül | Dayanak |
|---|---|---|
| F1 | **Sipariş başı katkı** = KDV hariç satış − ürün maliyeti − kargo − ödeme komisyonu − beklenen iade kaybı | Tanım |
| F2 | **Başabaş CPA (gerçek)** = F1. **Başabaş ROAS** = KDV hariç satış ÷ F1 (= 1 ÷ katkı marjı) | Tanım |
| F3 | **Panel ↔ gerçek maliyet:** gerçek maliyet = panel harcaması × **1,05** (Türkiye konum ücreti; vergi numarası girilmemişse × **1,26**). Panelde hedef CPA = gerçek hedef ÷ 1,05; panelde başabaş ROAS = F2 ROAS × 1,05 | [RESMİ] konum ücreti; Kurulum Kılavuzu §3.6, §9 |
| F4 | **Haftalık beklenen sonuç** N = günlük bütçe × 7 ÷ beklenen CPA | *türetilmiş* |
| F5 | **Desteklenebilir set sayısı** ≈ N ÷ 50. Bir setin öğrenmeden çıkması için günlük bütçe ≈ CPA × 50 ÷ 7 | Meta'nın "haftada ~50 sonuç" eşiğinden *türetilmiş* |
| F6 | **Bütçe yeterliliği kontrolü:** günlük bütçe ≥ hedef olayın ortalama ücretinin **10 katı**. Meta bunu "genel olarak" diye öneriyor. Sonuç başına ücret hedefi kullanılıyorsa bütçe ayrıca hedefin **en az 5 katı** olmalı. İkisi çelişirse ihtiyatlı olan 10× geçerli. F6 bir **öneridir**; Reklam Yöneticisi'nin uyarı verdiği zorunlu minimum bütçe değildir. Sağlanmazsa aşağıdaki istisna yolunu izle | [RESMİ] performans hedefleri ve minimum bütçe sayfaları |
| F7 | **Marjinal CPA** (ölçeklerken) = (yeni harcama − eski harcama) ÷ (yeni sonuç − eski sonuç) | *türetilmiş* |

**F5 ile F6'nın ilişkisi (*türetilmiş*):** F6'yı sağlayan bir bütçe haftada en az 10 × 7 = **70 sonuç** bekler. F5'in ~50 eşiği (günlük ≈ 7,14 × CPA) bundan gevşektir. Bu yüzden N < 50 olan her durum F6'yı **her zaman** ihlal eder; Şablon A'nın "N < 50" hâli de buna dahildir. Bu, kampanyayı kurmayı yasaklamaz. Kontrolü bütçenin tanımlandığı düzeyde yap: set bütçesinde her set için, kampanya bütçesinde kampanya için (*yorum*).

**F6 sağlanmıyorsa sırayla (Modül 01 §4.4 ile uyumlu):**
1. Bütçeyi F6 düzeyine çıkarmayı müşteriye öner.
2. Bütçe artmıyorsa N'ye göre ilerle:
   - N < 25 → daha sık gerçekleşen olaya in (§2.2).
   - 25 ≤ N < 50 → tek setle kal, 2–3 hafta sonra yeniden değerlendir.
   - 50 ≤ N < 70 → F5 sağlanıyor; tek setle yayınla.
3. N < 50 ile yayınlıyorsan ya da olaya inmek mümkün değilse: Setin öğrenmeyle sınırlı kalabileceğini ve CPA'nın oynak olacağını **müşteriye yazılı** bildir. Gerekçeyi değişiklik günlüğüne yaz (Modül 06 §10).

**Doldurulmuş örnek (e-ticaret, varsayımsal rakamlar):** KDV dahil sepet 1.200 TL → KDV hariç 1.000 TL. Ürün 450, kargo 60, ödeme komisyonu 36, iade kaybı 40 → katkı **414 TL** (F1).
- Başabaş gerçek CPA 414 TL. Panelde başabaş CPA = 414 ÷ 1,05 ≈ **394 TL** (F3).
- Başabaş ROAS (KDV hariç) = 1.000 ÷ 414 ≈ 2,42. Panelde ≈ 2,54.
- **Dikkat:** Piksel sipariş değerini **KDV dahil** gönderiyorsa panel ROAS'ı o değerle hesaplanır. O zaman panelde başabaş ROAS = 1.200 ÷ 414 × 1,05 ≈ **3,04**. Önce pikselin hangi değeri gönderdiğini kontrol et.
- Günlük 2.000 TL, beklenen panel CPA 300 TL → N = 2.000 × 7 ÷ 300 ≈ 47/hafta → **en fazla 1 set** (F5). Bu set "Öğrenmeyle sınırlı" sınırında kalır. Bu bir ceza değil, "bu kurulumla optimize etmekte zorlanıyorum" uyarısıdır [RESMİ].
- **F6:** 2.000 < 10 × 300 = 3.000 → **✗**. İlk iş, günlük 3.000 TL'yi önermek. Bütçe artmazsa N 25–50 arasında olduğu için tek setle yayınla. Öğrenmeyle sınırlı riskini müşteriye yazılı bildir. 2–3 hafta sonra ödeme başlatmaya inmeyi değerlendir (§2.2).

**Eğer-ise:**
- N < 50 ise yeni set açma. Mevcut setleri birleştir. Gerekirse daha sık gerçekleşen olaya in (§2.2).
- F6 sağlanmıyorsa yukarıdaki üç adımı izle. Meta'nın kendi önerisi de bütçeyi artırmak ya da daha kolay gerçekleşen olaya optimize etmektir [RESMİ].
- Tahmin için hesabın kendi son 30–90 günlük ortalamasını kullan. Türkiye için yöntemi açık, güvenilir bir CPM/CPC/CPL kıyası bulunamadı. Dış rakamları yalnızca "makul mü?" kontrolü için kullan.
- **Geçmişi olmayan yeni hesapta beklenen CPA (*sezgisel*):** Sayı uydurma. Sırayla:
  1. Müşterinin önceki kanal verisini kullan: Google Ads, önceki ajans, WhatsApp ya da telefon kaydı (Modül 01 §1.4, soru 15). Yalnız aynı olay ve benzer teklif için geçerlidir.
  2. Bu veri yoksa **başabaş panel CPA**'yı (F3) planlama CPA'sı say. Talep işlerinde bunun yerine Modül 01 §2.3'teki hedef CPL'yi kullan. Bu değer kabul edilebilecek en kötü durumdur. N ve set sayısı bu yüzden ihtiyatlı çıkar.
  3. İlk 7–14 günü taban çizgisi dönemi say (Modül 01 §0 madde 6, §4.5). **7. günde** F4–F6'yı gerçekleşen panel CPA ile yeniden hesapla. Bu hesaptan önce set sayısını artırma.

  Doğrulama testi bütçesini yayından önce müşteriye onaylat (Modül 01 §4.5a).

---

## 2. Amaç + dönüşüm konumu + performans hedefi

### 2.1 Karar tablosu

Amaç adlarının açıklaması Kurulum Kılavuzu §4'te. Bu tablo "hangi kombinasyon" sorusunu cevaplar.

| İş hedefi | Amaç | Dönüşüm konumu | Performans hedefi (ilk tercih) | Ön koşul | Sinyal yetmezse |
|---|---|---|---|---|---|
| Siteden satış (adet) | Satışlar | İnternet sitesi | Dönüşüm sayısı (Purchase) | Piksel + CAPI | Ödeme başlatma → Sepete ekleme → Yönlendirme sayfası görüntülemesi |
| Siteden satış (ciro, ürün fiyatları dağınık) | Satışlar | İnternet sitesi | Dönüşüm değeri | Değer optimizasyonu uygunluğu, değer gönderen Piksel | Önce dönüşüm sayısı ile veri biriktir |
| Instagram butiği, WhatsApp'tan satış | Satışlar veya Etkileşim | Mesajlaşma → WhatsApp | Konuşma; uygun olunca mesajlaşma yoluyla alışveriş | Satış etiketi ya da mesajlaşma CAPI. Uygunluk için 30 günde ~10 alışveriş olayı (ayrıntı ölçüm modülünde) | Konuşmada kal, satışları etiketlemeye başla |
| Randevu, fiyat sorusu (yüz yüze hizmet) | Potansiyel müşteriler veya Etkileşim | WhatsApp (ikinci seçenek Instagram Direct) | Mesajlaşma yoluyla potansiyel müşteri (varsa); yoksa konuşma | Veri paylaşımı ve etiketleme | Konuşma |
| Acil hizmet (tesisat, çilingir) | Potansiyel müşteriler | Aramalar | Arama | Telefonun gerçekten açıldığı saatler | Site + "Şimdi ara" eklentisi |
| Ekibin geri aradığı form, hacim | Potansiyel müşteriler | Anında form ("Daha yüksek satın alma niyeti" türü) | Potansiyel müşteri | Hızlı dönüş kapasitesi | — |
| Aynısı, kalite öncelikli | Potansiyel müşteriler | Anında form | Nitelikli potansiyel müşteri | CRM, CAPI ile bağlı. Geliştirici belgesine göre ayda ≥200 potansiyel müşteri | Potansiyel müşteri + CRM'de kalite takibi |
| Site formu (B2B, kurs, teklif) | Potansiyel müşteriler | İnternet sitesi | Dönüşüm (Lead olayı) | Piksel + CAPI | Yönlendirme sayfası görüntülemesi |
| Yeni açılış, isim değişikliği | Bilinirlik | (yok, ekran görünmez) | Erişim ya da reklam hatırlanırlığı yükselişi | Dar ve doğru konum | — |
| Fiziksel mağaza trafiği | Bilinirlik | Mağaza konumları açık | Erişim | Mağaza konumları tanımlı | — |
| İçerik / sayfa trafiği | Trafik | İnternet sitesi | Yönlendirme sayfası görüntülemesi | Piksel | Bağlantı tıklaması |
| Video izletme, ılık kitle biriktirme | Bilinirlik veya Etkileşim | Reklamında | ThruPlay | — | 2 saniyelik görüntüleme |
| Uygulama | Uygulama tanıtımı | Uygulama | Uygulama olayı / yükleme | SDK | Yükleme |

Kaynak: performans hedefleri ve amaç seçimi sayfaları [RESMİ].

**Türkiye'ye özel kurallar:**
- Yüz yüze hizmette **varsayılan dönüşüm konumu WhatsApp**. TÜİK 2026'ya göre WhatsApp %90 ile en çok kullanılan uygulama [HABER]. Messenger reklam erişimi yetişkinlerin yaklaşık %19'u [HABER]. **Messenger'ı tek hedef yapma.**
- Instagram Direct'i hedef seçersen reklam **yalnızca Instagram'da** görünür. WhatsApp ya da Messenger seçersen Facebook, Instagram ve WhatsApp'ta görünebilir [RESMİ].
- WhatsApp Potansiyel müşteriler amacında varsayılan hedef "Konuşma sayısını en üst düzeye çıkarma" gelir. Meta'nın deneyinde potansiyel müşteri optimizasyonu, konuşma optimizasyonuna göre ortalama %24 daha düşük CPL verdi [RESMİ]. Etiketleme kuruluysa ve seçenek hesapta görünüyorsa ona geç.
- Mesaj reklamları için Meta en az 7 gün yayında kalmayı öneriyor [RESMİ].

### 2.2 Olay merdiveni (sinyal yetmediğinde)

**Kural:** Bütçenin **haftada ~50 sonuç** (F5) üretebildiği **en derin** olayı seç. F6'yı da (≈ 70/hafta) sağlayan bir olay varsa onu tercih et.
- Satın alma: Satın alma → Ödeme başlatma → Sepete ekleme → Yönlendirme sayfası görüntülemesi.
- Potansiyel müşteri: Nitelikli potansiyel müşteri → Potansiyel müşteri.
- WhatsApp: Satın alma/potansiyel müşteri → Konuşma.

Meta'nın kendi örneği de "alışveriş → sepete ekleme" geçişidir [RESMİ].

**Örnek:** Günlük 1.000 TL bütçe.
- Satın alma CPA'sı 400 TL ise haftada ~17,5 satış çıkar. Set öğrenmeyle sınırlı kalır. F6: 1.000 < 4.000 **✗**.
- Sepete ekleme 80 TL ise haftada ~87 sonuç çıkar. Set öğrenmeden çıkabilir. F6: 1.000 ≥ 800 **✓**.
- **Yapılacak (*türetilmiş*):** Bütçe artmıyorsa önce ödeme başlatmanın ücretine bak. Ücret ≤ 100 TL ise F6 sağlanır ve daha derin olduğu için ödeme başlatmayı seç. Değilse sepete eklemeye optimize et.

**Risk (*sezgisel*):** Sığ olaya optimize etmek satış kalitesini düşürebilir. Geçişten 2 hafta sonra iki dönemin **satın alma başı maliyetini** karşılaştır. Kötüleştiyse satın almaya dön ve öğrenmeyle sınırlı durumu kabul et.

---

## 3. Huni ve bütçe dağılımı

### 3.1 Tanımlar

| Katman | Kim | Tipik kaynak (azami saklama) |
|---|---|---|
| **Soğuk** | İşletmeyle hiç etkileşmemiş | Geniş / Advantage+ hedef kitle |
| **Ilık** | Video izleyen, Sayfa/Instagram etkileşimi olan, siteyi gezen | Etkileşim kitlesi (365 gün), site ziyaretçisi (180 gün) |
| **Sıcak** | Sepete ekleyen, ödeme başlatan, fiyat sorup almayan | Site olayı kitlesi; WhatsApp "Fiyat sordu" etiketi |
| **Mevcut müşteri** | Satın almış | Müşteri listesi + satın alma site kitlesi |

Saklama süreleri [RESMİ]. Ayrıntı hedef kitle modülünde.

### 3.2 Temel gerçek

Advantage+ hedef kitle açıkken Meta ılık ve sıcak kişilere **kendiliğinden** harcar. Jon Loomer, hedef kitle segmentleri raporunda bu payı genellikle %20–25 civarı (bazen %10–40) gördüğünü yazıyor [PRATİSYEN]. Yani "soğuk kampanya" zaten huninin tamamına çalışır. Ayrı yeniden hedefleme kampanyası, kendi kampanyanla **açık artırma çakışması** yaratabilir. Çakışan reklamlardan yalnız biri yarışır [RESMİ].

### 3.3 Dağılım kuralları (N = F4'teki haftalık beklenen sonuç)

| Durum | Yapı | Ayrı sıcak/yeniden hedefleme bütçesi |
|---|---|---|
| N < 50 | Tek kampanya, tek set, Advantage+ hedef kitle. Bu durumda F6 sağlanmaz; §1'deki istisna yolunu izle | **Yok (%0)**. Sıcak kitle zaten içeride |
| 50 ≤ N < 150 | Tek ana kampanya; gerekiyorsa mevcut müşteri seti (harcama sınırlı) | Gerekmez. Mevcut müşteriye üst sınır istiyorsan §5.4'teki iki set yöntemi |
| N ≥ 150 ve yeniden hedefleme kitlesi büyük | Ana kampanya + isteğe bağlı sıcak kampanya | Başlangıç %10 (*sezgisel*). Pratisyen aralığı %1–10 (Flighted) ile %20 (yaygın 70/20/10 kalıbı, kaynaksız) arası |
| Sezon zirvesi (11.11, Kara Cuma) | Ana kampanya + dönem kampanyası (K2). K2'yi yalnız ek bütçesi kendi başına F5'i sağlıyorsa aç. Sağlamıyorsa dönem kreatiflerini ana kampanyaya ekle (§5.4 K2 kuralları) | Zirveye doğru sıcağa kayma olur. Yeni müşteri harcamasını sıfırlama [PRATİSYEN] |

N eşikleri F5'ten *türetilmiş*. Flighted, ana kampanya günde ~1.000 USD'ye ulaşmadan ayrı yeniden hedeflemeye ciddi bütçe ayırmamayı öneriyor [PRATİSYEN].

**Eğer-ise:**
- **Bilinirlik bütçesi:** Yalnızca yeni açılışta, isim değişikliğinde ya da etkinlik öncesinde ayır. Süreli kampanya olarak kur (*sezgisel*). Diğer durumlarda soğuk kitleyi de dönüşüm amacıyla hedefle.
- **Ön koşul: hedef kitle segmentleri ve mevcut müşteri kitlesi [RESMİ].** Satış hesabında ilk kampanyadan önce şu ekranı aç: Reklam ayarları > Reklam oluşturma > **Hedef kitle segmentleri**. Orada iki tanım yap:
  - **Mevcut müşteri:** alışveriş site kitlesi (ör. `OK_WEB_PUR_180G`) + müşteri listesi (`OK_LISTE_MUS_…`).
  - **Etkileşimli kitle:** satın almamış site ziyaretçisi ya da sepete ekleyen gibi kaynaklar. Meta'nın kaynak listesi site, uygulama, katalog, müşteri listesi, çevrimdışı ve alışverişten oluşuyor. Instagram ya da Sayfa etkileşim kitlesinin burada seçilip seçilemediğini hesapta doğrula.

  Tanım hesaptaki **tüm satış kampanyalarına** kendiliğinden uygulanır. Tanım yapılmazsa yeni/etkileşimli/mevcut kırılımı gelmez ve yeni müşteri CPA'sı ölçülemez. "Yeni müşteriler kazanın" da mevcut müşterileri temsil eden bir özel hedef kitle ister. Müşteri listesi KVKK kapısından geçmeden kullanılmaz (Modül 03 §4.4). Ayrıntı Modül 03 §7.1–7.2'de.
- **Yeni müşteri önceliği:** Satışlar amacında "Yeni müşteriler kazanın" yaşam döngüsü ayarını ya da mevcut müşteri **hariç tutmasını** kullan [RESMİ]. Başarıyı genel CPA ile değil **yeni müşteri CPA'sı** ile ölç [RESMİ].
- **Mevcut müşteri ROAS'ı yanıltır:** CTC artırımlılık katsayısını şöyle tanımlıyor: artırımlı gelir ÷ Meta'nın 7 gün tıklama ile raporladığı gelir. Coğrafi testlerindeki medyan katsayı mevcut müşteride 0,60, yeni müşteride 1,14 [PRATİSYEN, tek kaynak]. 0,60, reklamın zarar ettiğini göstermez. Meta'nın raporladığı gelirin yaklaşık %60'ının reklam olmadan gelmeyeceğini gösterir. Mevcut müşteriye ayrı set açarsan ROAS_hedef (panel) ≈ başabaş panel ROAS ÷ katsayı kullan (*türetilmiş*). Örnek: başabaş 3,04 ise mevcut müşteri seti için ≈ 5,1. Yeni müşteri setinde hedef başabaşın altına inmez (§7). Kampanya bütçesinde setler aynı teklif stratejisini paylaşır (§6). Hedef tutarının set düzeyinde girilmesi ve diğer ayrıntılar Modül 03 §7.1'de.
- **Ayrı yeniden hedefleme setinde** Advantage+ hedef kitleyi kapat. Meta yeniden hedeflemede Advantage+ hedef kitleyi önermiyor [RESMİ].
- **Haftalık kontrol:** Satış kampanyalarında hedef kitle segmentleri kırılımına bak (yeni / etkileşimli / mevcut). Kırılım yalnız segment tanımları yapılmışsa gelir (yukarıdaki ön koşul). Özellik kademeli açılıyor; hesapta görünmeyebilir [RESMİ].

---

## 4. Advantage+ mı, manuel mi?

**"Advantage+ açık" olması için üç koşul [RESMİ]:**
1. Kampanya bütçesi.
2. En az bir sette Advantage+ hedef kitle.
3. Tüm setlerde Advantage+ reklam alanları. Satışlar, Potansiyel müşteriler, Etkileşim ve Uygulama tanıtımı amaçlarında set düzeyinde manuel alan seçimi kademeli olarak kaldırılıyor. Değişikliğin açıldığı hesapta bu koşul kendiliğinden sağlanır (§8).

Hesap düzeyi kontroller (konum, minimum yaş, alan hariç tutma) bu durumu **bozmaz**. Minimum yaşı 25'e kadar yükseltmek ya da özel hedef kitle hariç tutmak da bozmaz [RESMİ].

| Eğer… | O zaman… |
|---|---|
| Amaç Satışlar, Potansiyel müşteriler ya da Uygulama tanıtımı ve kitle geniş | **Advantage+ açık başla** (varsayılan). Bu kural bütçe, hedef kitle ve alanları kapsar; kreatif iyileştirmeleri kapsamaz (tablonun altındaki not). Meta, Advantage+ satış kampanyalarında dönüşüm başına ücrette ortalama %9 iyileşme bildiriyor [RESMİ] |
| Kesin sınır gerekiyor (konum, 18+, dil, mevcut müşteri hariç) | Advantage+'ı kapatma. Sınırı **kontrol** olarak gir. Herkese uygulanacaksa hesap düzeyinde gir |
| Yalnız ılık/sıcak kitleye harcamak istiyorsun | O sette Advantage+ hedef kitle **kapalı**: "Reklamlarınızın erişimini daha da sınırlayın" → "Öneri olarak kullan" işaretini kaldır |
| Yerel işletme, dar yarıçap | Advantage+ hedef kitle açık kalabilir. Şehir seçtiysen "yanıt verme olasılığı yüksek daha fazla kişiye erişin" kutusunu kapat ya da iğne + yarıçap kullan [RESMİ] |
| Sağlık turizmi (yurt dışı) | Türk yönetmeliği "otomatik hedef kitle tanımlamaları"nı kapatmayı istiyor. Muhafazakâr yol: Advantage+ hedef kitle, detaylı hedefleme genişletmesi ve benzer kitle genişletmesi kapalı; ekran görüntüsüyle belgele (yorum; hukuk modülüne bak) |
| Özel reklam kategorisi (yurt dışı kitleye konut, kredi) | Bazı Advantage+ özellikleri kapalı ya da kısıtlı. Türkiye kitlesi için konut kategorisi zorunlu değil [RESMİ] |
| Setler arasında farklı teklif stratejisi ya da bütçe planı gerekiyor | Set bütçesi kullan. Advantage+ etiketi kapanır. Geniş ayarlarla otomasyondan yine yararlanırsın [RESMİ] |
| Mevcut müşteriye harcamayı sınırlamak istiyorsun | Eski "mevcut müşteri bütçe üst sınırı" kalktı. İki set + harcama sınırı kullan (§5.4). Kampanya bütçesi olduğu için Advantage+ açık kalır |
| Satış Meta'ya ulaşmıyor (Piksel/CAPI yok) | Satış optimizasyonu yapma. Önce ölçümü kur ya da WhatsApp/form yoluna geç |
| Advantage+'ın gerçek yeni müşteri getirdiğinden şüphelisin | Deneyler'de A/B testi (≥7 gün, eşit bütçe) yap. Sonucu sipariş panelindeki yeni müşteri sayısıyla doğrula. Karşı görüş: Wicked Reports 2025'te Advantage+'ta yeni müşteri maliyetinin arttığını iddia ediyor (firma ilişkilendirme aracı satıyor) [PRATİSYEN] |

**Kapsam notu:** Bu bölümdeki "Advantage+" yalnız **bütçe, hedef kitle ve reklam alanlarını** kapsar. **Advantage+ kreatif iyileştirmeleri** bu kurala dahil değildir. Yüksek riskli olanlar **varsayılan olarak kapalı** kalır: metin iyileştirmeleri, çeviriler, "eylem çağrısını iyileştir", görsel ve arka plan oluşturma. Riskin iki örneği:
- "Eylem çağrısını iyileştir" reklama kendiliğinden "%X indirim" ekleyebilir ve 10 günlük referans fiyat kuralını (Modül 09 §5.3) çiğneyebilir.
- Yapay zekâ ile üretilmiş insan figürü Modül 09 §1'de sarı kapıdır.

Bu ayarları her yayında tek tek kontrol et (Modül 04 §10.1).

---

## 5. Hesap yapısı şablonları

### 5.1 Ortak kurallar

- **Az yapı:** İş hedefi, ürün grubu, ülke ya da dönüşüm konumu gerçekten farklı değilse yeni kampanya ya da set açma. Set sayısı = F5 [RESMİ mantık].
- **Kitle dilimi için set açma.** Yaş, ilgi alanı, ilçe gibi dilimleri **kırılım** raporuyla izle. Küçük coğrafyaları birleştir [RESMİ].
- **Kreatif çeşitliliği yeni hedeflemedir.** Bir sette 3–5 **gerçekten farklı** reklam (persona, açı, format) ile başla [PRATİSYEN]. Bir reklama 10'a kadar varlık konabilir. Set başına sınır 50 reklam [RESMİ].
- **Öğrenmeyi koru:** Şunlar öğrenmeyi sıfırlar [RESMİ]:
  - hedeflemede değişiklik,
  - kreatif değişikliği,
  - optimizasyon olayı değişikliği,
  - teklif stratejisi değişikliği,
  - sete yeni reklam eklemek,
  - seti 7 gün ya da daha uzun durdurmak.

  Bütçe ve hedef tutarı değişiklikleri büyüklüğüne göre sıfırlayabilir.

### 5.2 Şablon A — Küçük bütçe (N < 50 ya da tek setlik bütçe)

```
K1  ETK veya PM → WhatsApp | kampanya bütçesi | En yüksek hacim
 └─ S1  Konum (ilçe/yarıçap) + Advantage+ hedef kitle + Advantage+ alanlar
     ├─ R1 Video 9:16, ürün/hizmet yakın çekim
     ├─ R2 Tekli görsel 4:5, fiyatlı teklif
     └─ R3 Döngü, menü/hizmet listesi
```

N < 50 ise bu şablonda F6 sağlanmaz; §1'deki istisna yolunu izle.

**Doldurulmuş örnek:** Kadıköy'de kahvaltı kafesi, günlük 400 TL, WhatsApp konuşması tahmini 25 TL.
- N = 400 × 7 ÷ 25 = 112/hafta → 1 set yeterli.
- F6: 400 ≥ 10 × 25 = 250 ✓.
- Ayrı yeniden hedefleme yok. Bilinirlik kampanyası yok.
- 7 gün dokunma (Kurulum Kılavuzu §6.3).

### 5.3 Şablon B — Orta bütçe (50 ≤ N < 150)

```
K1  Ana dönüşüm (PM/SAT) | kampanya bütçesi | En yüksek hacim → 2+ hafta sonra hedefli teklif
 ├─ S1  Ana kitle (Advantage+), 4–6 farklı reklam
 └─ S2  (isteğe bağlı) gerçekten farklı ürün/hizmet ya da şube; ayrı kitle değil
K2  (isteğe bağlı) farklı iş hedefi ya da dönüşüm konumu (ör. WhatsApp)
```

**Doldurulmuş örnek:** Ankara'da iki şubeli yabancı dil kursu, günlük 2.000 TL, form CPL 150 TL.
- N ≈ 93/hafta. F6: tek sette 2.000 ≥ 1.500 **✓**.
- İki şube için iki set açarsan her biri ~46 sonuç alır ve ikisi de öğrenmeyle sınırlı kalır. İki set bütçesine bölersen her biri 1.000 < 1.500 olur ve F6 **✗**.
- **Doğru kurulum:** Tek sette iki konum. Şube kırılımını formdaki şube sorusuyla izle.
- Şube başına kesin bütçe şartsa set bütçesiyle ayır. Öğrenmeyle sınırlı riskini kabul et.
- Yeni kreatif denemek için kreatif testi aracını kullan. Meta'nın arayüz önerisi teste bütçenin en fazla %20'sini ayırmak (Loomer'ın aktarımı) [PRATİSYEN].

### 5.4 Şablon C — E-ticaret (site, Piksel + CAPI)

```
K1  SAT | Advantage+ satış (sürekli) | kampanya bütçesi | En yüksek hacim/değer → ROAS hedefi
 ├─ S1  Tüm kitle (Advantage+), mevcut müşteri HARİÇ
 │      Reklamlar: 4–8 farklı kreatif + Advantage+ katalog reklamı
 └─ S2  (isteğe bağlı) Mevcut müşteri listesi, öneri işareti kaldırılmış, maksimum harcama sınırı
K2  SAT | Kampanya dönemi (11.11, Kara Cuma) | başlangıç + bitiş tarihli | zirveden 2–3 hafta önce açılır
    | K1'e ek bütçe (K1'den kesilmez) | yalnız aşağıdaki koşul sağlanırsa
```

- S1 ve S2 aynı kreatifleri kullanır. S2'nin harcama sınırı yüzde ya da tutar olabilir [RESMİ].
- Ürün sayısı çoksa ayrı ürün kampanyaları yerine katalog reklamı kullan [RESMİ].
- Katalog reklamını K1'in içinde tutmak parçalanmayı azaltır (*sezgisel*).
- **Liste ön koşulu (S1 ve S2):** Müşteri listesini yüklemeden önce KVKK kapısından geçir. Bu kapı hem S2'nin dahil ettiği listeye hem S1'deki "mevcut müşteri HARİÇ" listesine uygulanır. Üç soru sor: Liste kendi müşterinden mi? Aydınlatma metni reklam platformunda eşleştirmeyi ve yurt dışına aktarımı içeriyor mu? Hukukçu onayı kayıtlı mı? (Modül 03 §4.4, Modül 09 §5.6.) Kapı geçilmezse mevcut müşteri kitlesini yalnız alışveriş site kitlesiyle kur (Piksel çerez rızası şartıyla, Modül 05 §2.6) ya da S2'yi açma.

**K2 (dönem kampanyası) kuralları:**
- **Açma koşulu (*türetilmiş*):** K2'nin ek günlük bütçesi tek başına F5'i sağlamalı (≈ CPA × 50 ÷ 7). Sağlamıyorsa K2'yi açma. Dönem kreatiflerini K1'e ekle ve dönem bitince o reklamları kapat (Modül 06 §9.1 örneği). Sebep: K1 ile K2 aynı kitleye çıkar. Aynı reklamverenin çakışan reklamlarından Meta yalnız toplam değeri en yüksek olanı açık artırmaya sokar. Bu yüzden küçük bir set bütçesini harcayamayabilir ya da öğrenmeden çıkamayabilir [RESMİ].
- **Bütçe (*sezgisel*):** K2 açılırken K1 bütçesine dokunma. K2'ye **ek** bütçe ayır, çünkü K1 bütçesini kesmek K1'i öğrenmeye sokabilir (§5.1).
- **Süre:** K2'yi başlangıç ve bitiş tarihiyle kur; takvim bitince kendiliğinden durur [PRATİSYEN CTC]. Bitiş tarihi = zirve + çıkış penceresi (T + 3…14 gün, Modül 06 §9.2).
- **K1 dönem boyunca sürer.** Zirvede K1'e de bütçe artışı yapılacaksa bunu önceden plana yaz (Modül 06 §9.3). Zirve bitince K2 kendiliğinden durur. K1'in bütçesi artırılmışsa kademeli düşür. Toplam bütçeyi düşürmenin alt sınırı Modül 06 §9.3 "Sezon sonrası" maddesinde [RESMİ].
- **İndirim kontrolü:** İndirim ya da fiyat içeren her reklamda 10 günlük referans fiyat kontrolünü yap (Modül 09 §5.3; reklamlar için 1 Ağustos 2026'dan beri geçerli) [MEVZUAT]. Reklamda referans fiyat, başlangıç ve bitiş tarihi ve stok sınırlıysa adet yazmalı.

**Doldurulmuş örnek:** Kadın giyim sitesi, günlük 6.000 TL, panel CPA 300 TL.
- N = 140/hafta → 2 set kapasitesi var. S1 + S2. F6: kampanya bütçesinde 6.000 ≥ 3.000 **✓**.
- 11.11 için K2'yi ancak ek bütçe ≥ 300 × 50 ÷ 7 ≈ **2.143 TL/gün** ise aç. Ek bütçe daha azsa 11.11 kreatiflerini K1/S1'e ekle.
- Panelde başabaş ROAS 3,04 (Bölüm 1 örneği, KDV dahil değer).
- İlk 2 hafta En yüksek değer. Sonra son 14 günün ROAS'ı 4,0 ise ROAS hedefi = max(4,0 × 0,85; 3,04) = **3,4** (§7).

Büyük hesaplar için CTC'nin 2 × 2 yapısı (sürekli / dönem × yeni / mevcut müşteri) [PRATİSYEN]: yalnız her hücre haftada 50+ sonuç alıyorsa.

### 5.5 Şablon D — Yerel hizmet (tesisat, temizlik, nakliyat)

```
K1  PM | set bütçeleri (iki farklı performans hedefi var)
 ├─ S1  WhatsApp → mesajlaşma yoluyla potansiyel müşteri (yoksa konuşma) | ilçe listesi | 7/24
 └─ S2  Aramalar → arama | aynı ilçeler | YALNIZ telefonun açık olduğu saatler (planlı yayın)
```

- Karma optimizasyon hedefleri set bütçesi gerektirir [RESMİ]. Advantage+ etiketi kapanır; sorun değil.
- Şehir seçtiysen "daha fazla kişiye erişin" kutusunu kapat.
- S1 ile S2 aynı ilçeleri hedefler. Bu bilinçli bir çakışmadır, §11'deki çakışma yasağına girmez. Meta açık artırmaya ikisinden toplam değeri yüksek olanı sokar [RESMİ].

**Doldurulmuş örnek:** Üsküdar'da tesisatçı, günlük 600 TL.
- S1: 400 TL, konuşma tahmini 30 TL → N ≈ 93/hafta. F6: 400 ≥ 300 **✓**.
- S2: 200 TL, arama tahmini 60 TL → N ≈ 23/hafta. S2 öğrenmeyle sınırlı kalır. F6: 200 < 600 **✗**.
- **S2 için yapılacak (*sezgisel*):**
  - Aramalar değerliyse S2'yi 600 TL'ye çıkarmayı öner.
  - Bütçe artmazsa S2 öğrenmeyle sınırlı kalacak; bunu müşteriye yazılı bildirerek yayınla.
  - 2 hafta sonra hâlâ arama gelmiyorsa S2'yi kapat ve bütçesini S1'e ver. WhatsApp karşılama mesajına telefon numarasını yaz.
- Meta'nın deneyinde yalnız mesai saatinde yayınlanan arama reklamları ortalama %25,4 daha fazla arama dönüşümü verdi [RESMİ].

---

## 6. Kampanya bütçesi mi, reklam seti bütçesi mi?

| Eğer… | Seç |
|---|---|
| Tek set var | Fark etmez. Tek setli kampanyada kampanya bütçesi set bütçesi gibi çalışır [RESMİ] |
| 2+ set, hepsi eşit değerde, aynı optimizasyon olayı ve teklif stratejisi | **Kampanya bütçesi** (Advantage+ kampanya bütçesi) |
| Bir sete en az / en fazla harcama garantisi gerekiyor | Kampanya bütçesi + **set harcama sınırları**. Sınırları az kullan; çok kısıt seti yayınsız bırakabilir [RESMİ] |
| Setlerin değeri çok farklı, kitle büyüklükleri çok farklı, ya da karma hedef/teklif var | **Reklam seti bütçesi** [RESMİ] |
| Set bütçesi kullanıyorsun ama esneklik istiyorsun | **Reklam seti bütçesi paylaşımı:** setler günlük bütçelerinin %20'sine kadarını paylaşır [RESMİ] |
| Sabit uzunlukta promosyon, kesin toplam tavan | Toplam bütçe. Bir günden kısa set için toplam bütçe zorunlu [RESMİ] |
| Her gün benzer harcama isteniyor | Günlük bütçe. Esneklik: günde %175'e kadar, haftada 7× (Kurulum Kılavuzu §5.2) |

**Kampanya bütçesiyle çalışma kuralları [RESMİ]:**
- Analizi **kampanya düzeyinde** yap. Bütçenin çoğunu tek sete vermesi normaldir.
- Az yayın alan seti durdurma. Durdur-aç yapma.
- Yeni setleri **toplu** ekle. Her eklemeden sonra yeniden ayarlama yaklaşık 2 saat sürebilir.
- Kampanya başına 70 seti aşma.
- Kitle büyüklüğü farkı bütçeyi büyük kitleye kaydırır.
- Kampanya bütçesini ya da kampanya teklif stratejisini değiştirmek **birden çok seti** öğrenmeye sokabilir. Bütçenin setler arasında yer değiştirmesi ve yeni set eklemek diğer setleri sıfırlamaz.
- Kampanya bütçesinde kampanya başına **tek teklif stratejisi** olur. Farklı strateji gerekiyorsa ayrı kampanya aç.

---

## 7. Teklif stratejisi seçimi

**Karar ağacı:**
1. **Bilinirlik** → En yüksek hacim (erişim/gösterim). Rezervasyon satın alma türünü yeni hesapta kullanma (Kurulum Kılavuzu §4).
2. Geçmiş veri < 2 hafta ya da başabaş bilinmiyor → **En yüksek hacim**. Değer optimizasyonuna uygunsan **En yüksek değer**. En az **2 hafta** taban çizgisi oluştur [RESMİ].
3. Başabaş biliniyor, haftada **50–100+ dönüşüm** var ve CPA/ROAS'ı korumak harcamadan önemli → **Hedefe dayalı** strateji [RESMİ]:
   - **Sonuç başına ücret hedefi** başlangıcı = min(son 14–30 günün panel CPA'sı × 1,10–1,20; başabaş panel CPA).
   - **ROAS hedefi** başlangıcı = max(son 14–30 günün panel ROAS'ı × 0,80–0,90; başabaş panel ROAS). Yalnızca "Dönüşüm değerini en üst düzeye çıkar" ile kullanılır.
   - %10–20 payı Meta'nın önerisi [RESMİ]. min/max ile başabaşa bağlamak Meta'nın "işletmenin kaldırabileceği en yüksek CPA / en düşük ROAS'ı hedef yap" ilkesinden *türetilmiş*.
4. **Teklif üst sınırı** → Yalnızca dönüşüm oranını iyi tahmin edebilen ileri kullanıcı içindir. Teklif düşükse yayın durur ve "Teklif sınırlı" görünür [RESMİ]. Varsayılan olarak kullanma.
5. Kreatif testi aracı kullanılacaksa → En yüksek hacim. Araç diğer stratejilerle çalışmıyor (pratisyen gözlemi; resmi yardım sayfası bulunamadı) [PRATİSYEN].

**Hedefli stratejiyi yönetme [RESMİ]:**
- Değerlendirmeyi **haftalık ortalamayla** yap. Her hedef değişikliğinden sonra **7 gün** bekle. Ölçüm aralığı 50–100 dönüşüm içersin.
- **Teklif kısıtlı** (bütçe harcanmıyor): CPA hedefini **%5–10 yükselt**. ROAS hedefinde aynı mantıkla hedefi düşür (*sezgisel*). Düşük harcama ile birlikte CPA hedefin üstündeyse setleri birleştir.
- **Bütçe kısıtlı** (bütçe tamamen harcanıyor): CPA iyiyse bütçeyi artır. Bütçe harcanıyor ama CPA hedefin belirgin üstündeyse hedefi **%5–10 düşür**.
- Ölçeklemeden önce hedefli kampanyayı en az **3 hafta** çalıştır.
- iOS 14.5+ kitlesinde hedefli stratejiyle set en az **3 tam gün** yayında kalmalı.
- Raporda tek tek günlerde hedefin aşılması normaldir. Saatlik kırılımla hedef karşılaştırması yapma.

**Değer kuralları:** Yaş, cinsiyet, konum, işletim sistemi, cihaz, alan ya da dönüşüm konumu segmentine teklifi %1000'e kadar artırır, %90'a kadar azaltır. ROAS hedefi ve teklif üst sınırıyla **kullanılamaz** [RESMİ]. Kitleyi daraltmak yerine "bu segment daha değerli" demek için kullan.

**Türkiye notları:**
- Hedef tutarı **panel** değeriyle gir (F3: gerçek hedef ÷ 1,05).
- ROAS girişinde ondalık ayırıcı virgüldür. Meta'nın "%110 getiri = 1,100" örneği **1,1×** demektir. Girişten sonra önizlemedeki değeri kontrol et (*doğrulanmalı*).

---

## 8. Reklam alanları

**Varsayılan: Advantage+ reklam alanları açık.** Kapsam: Facebook, Instagram, Messenger, WhatsApp, Audience Network, Threads. Meta'nın bir deneyinde manuel alanlara göre ortalama %11,7 daha düşük CPA çıktı [RESMİ].

> **2026 değişikliği: set düzeyinde alan seçimi kalkıyor [RESMİ, kademeli; hesaba göre doğrulanmalı].**
>
> - **Etkilenen amaçlar:** Satışlar, Potansiyel müşteriler, Etkileşim, Uygulama tanıtımı. Meta'nın "reklam alanı ayarlarındaki yenilikler" sayfasına göre bu amaçlarda reklam alanları artık reklam seti düzeyinde elle seçilmiyor [RESMİ; HABER Social Media Today, 12.10.2026].
> - **Etkilenmeyen amaçlar:** Bilinirlik ve Trafik. Bunlarda manuel alan seçimi şimdilik duruyor.
> - **Platform, cihaz, işletim sistemi:** Ağustos 2026'da bazı hesaplarda görülen arayüz uyarısına göre bunların set düzeyindeki hariç tutmaları da kalkıyor [PRATİSYEN Loomer, 20.08.2026]. Meta'nın sayfası bunu ayrıca yazmıyor.
> - **Geçiş:** Değişiklik açılmamış hesaplarda eski ekran görünebilir.
>
> **Bu dört amaçta kalan araçlar:**
> 1. **Hesap düzeyi alan kontrolü** (Reklam ayarları > Reklam alanı kontrolleri). Yalnız şu alanlar hariç tutulabilir: Audience Network (yerel, reklam bandı ve ara reklam; ödüllü video), Facebook Marketplace, Facebook sağ sütun. Bu hariç tutma artık amaç bazında da ayarlanabiliyor. Kontrol hesaptaki tüm kampanyalara uygulanır ve yansıması 48 saati bulabilir. Siyaset, finans, istihdam ve konut reklamlarında geçerli değildir [RESMİ].
> 2. **Alan değer kuralı:** Belirli alanlarda teklifi artırır ya da azaltır (§7 değer kuralları). Alanı kapatmaz, yalnız ağırlığını değiştirir [RESMİ].
> 3. **Alan bazlı varlık özelleştirmesi ve multimedya reklam:** Her alan için ayrı kırpma, metin ve bağlantı verilebilir. Desteklenen medyası olmayan alanda reklam yayınlanmaz [RESMİ].
> 4. **Dönüşüm konumunun dolaylı etkisi:** Instagram Direct seçilirse reklam yalnız Instagram'da görünür (§2.1).
>
> **Bu dört amaçta kurulamayanlar (*türetilmiş*, hesap düzeyi listeden):** Threads, Reels, WhatsApp Durum ya da Instagram Keşfet'i kapatmak; "yalnız Instagram" ya da "yalnız mobil" kısıtı. Müşteri kesin bir alan kısıtı istiyorsa yayından önce bunun mümkün olmayabileceğini yazılı söyle. Böyle bir kısıt için amacı Bilinirlik ya da Trafik'e çevirmek optimizasyon olayını değiştirir; bu yüzden müşterinin kararıdır.

| Eğer… | O zaman… |
|---|---|
| Bir alanın ortalama CPA'sı yüksek görünüyor | **Kapatma.** Sistem toplamda en çok sonucu hedefler. Meta'nın örneğinde Instagram kapatılınca sonuç 9'dan 8'e düştü, ortalama ücret 3 TL'den 3,25 TL'ye çıktı [RESMİ]. Bilinirlik ve Trafik'te karar düzeyi: kampanya bütçesinde kampanya, set bütçesinde set; en az 7 günlük veri. Diğer amaçlarda setten kapatma yok. Alan gerçekten daha az değerliyse alan değer kuralıyla teklifi düşür |
| Marka güvenliği gerekiyor (ör. Audience Network istemiyorsun) | Hariç tutmayı **hesap düzeyi** reklam alanı kontrollerinden yap. Yalnız Audience Network, Marketplace ve sağ sütun için çalışır (yukarıdaki kutu). Advantage+ durumunu bozmaz; yansıması 48 saati bulabilir [RESMİ] |
| Satışlar / Potansiyel müşteriler amacında **set düzeyinde** alan hariç tuttun (değişikliğin henüz açılmadığı hesap) | Meta, hariç tutulan her alana harcamanın %5'ine kadarını ayırabilir ("sınırlı harcama"). API'de varsayılan kapalı; Reklam Yöneticisi'nde açık geldiği bildiriliyor → ayarı kontrol et (*doğrulanmalı*). Set düzeyinde seçim kalkınca bu satır geçersiz olur. Kuralın hesap düzeyi hariç tutmaya uygulanıp uygulanmadığı doğrulanmadı |
| Farklı alanlara farklı görsel lazım | Ayrı set açma. **Alan bazlı varlık özelleştirmesi** kullan [RESMİ]. 9:16 ve 4:5 (ya da 1:1) varlık hazırla |
| Kitle kadın ağırlıklı (güzellik, moda) | Facebook'a kısıtlama. Türkiye'de Facebook yetişkin reklam kitlesinin yaklaşık %34'ü kadın, Instagram'ınki yaklaşık %47 [HABER] |
| Anında formda "Randevu talebi" sorusu var | Reklam yalnız mobil Facebook uygulamasında yayınlanır. Yüksek niyet formu yalnız mobil Facebook Akışı ve Instagram akışında yayınlanır [RESMİ]. Instagram ağırlıklı kitlede başka yol seç |
| Hedef Instagram Direct | Reklam yalnız Instagram'da görünür (§2.1) |

---

## 9. Zamanlama

### 9.1 Takvim

- **Değerlendirme payı:** Reklam çoğunlukla 24 saat içinde değerlendirilir (Kurulum Kılavuzu §6.1). Başlangıçtan en az 1–2 gün önce kur.
- **Sezon:** Meta'nın 2026 tatil rehberi kampanyaları erken açmayı öneriyor, çünkü öğrenme zirveden önce bitmeli [RESMİ]. Pratik kural (*sezgisel*): zirveden **en az 2–3 hafta önce** yayına al. Darkroom, zirveden ~2 hafta önce yeni kreatif denemeyi bırakmayı öneriyor [PRATİSYEN].
- **2026–2027 tarihleri:**

| Olay | Tarih | En geç yayın (*sezgisel*) |
|---|---|---|
| 11.11 | 11 Kasım 2026, çarşamba | 21–28 Ekim 2026 |
| Kara Cuma | 27 Kasım 2026 | 6–13 Kasım 2026 |
| Ramazan | 8 Şubat 2027 başlangıç (*doğrulanmalı*). Türkiye'de giyim aramaları başlangıçtan ~2 hafta önce zirve yaptı (Microsoft verisi) | Ocak sonu 2027 |
| Ramazan Bayramı | 9–11 Mart 2027 | — |
| Anneler Günü | 9 Mayıs 2027 | — |
| Kurban Bayramı | 16–19 Mayıs 2027 | — |

- Türkiye'ye özel sezonluk CPM verisi bulunamadı. **Taban çizgisini metriğe göre seç:**
  - **İlişkilendirmeden bağımsız metrikler** (BGBM, TBM, harcama, erişim, sıklık): hesabın geçen yılki aynı haftaları taban çizgisi olabilir.
  - **Dönüşüme bağlı metrikler** (CPA, ROAS, dönüşüm sayısı): Mart 2026 öncesiyle doğrudan kıyaslama yapma. Tıklama ilişkilendirmesi 3 Mart 2026'da değişti [HABER], yani Kasım 2025'in tamamı eski tanımla ölçüldü. Bu metriklerde yıllık kıyası işletmenin sipariş paneliyle ya da MER ile yap (Modül 01 §3.1). Kırılma tarihleri Modül 05 §5.3'te.

### 9.2 Gün ve saat planı (reklam seti planı)

- Meta'ya göre planlı yayın günlük ya da toplam bütçeyle kurulabilir ve her plan en az 1 saat sürmelidir. Reklam kitlenin saat diliminde gösterilir [RESMİ]. Bazı üçüncü taraf rehberler saat ızgarasının yalnız toplam bütçede göründüğünü yazıyor; hesapta doğrula.
- Meta plan dışında **yayın yapmaz**, ama az sayıda kişi reklamı plan dışında görebilir [RESMİ].
- **Saat planı kullan, eğer:** sonucu karşılayacak insan kapasitesi saate bağlıysa (arama reklamı, form, WhatsApp'a hızlı dönüş). Meta, form reklamlarının ekibin hemen dönebildiği saatlerde yayınlanmasını öneriyor [RESMİ].
- **Kullanma, eğer:** amaç yalnızca "ucuz saatleri yakalamak" ise. Saat kısıtı bütçenin harcanma biçimini değiştirir. Saatlik kırılım da karar için güvenilmez [RESMİ]. Örneğin iftar sonrasına kısıt koymak yerine o saate uygun kreatif kullan (*sezgisel*).

### 9.3 Değişikliklerin zamanlaması

- Değişiklikleri **toplu** yap. Öğrenme sürerken düzenleme yapma [RESMİ].
- Yeni bütçenin geçerli olması ~15 dakika sürer. Bütçe ve teklif birlikte değişecekse **önce bütçe, 15 dakika sonra teklif** [RESMİ].
- Meta haftayı **pazar–cumartesi** sayar. Hafta ortası değişiklikte kalan günlere orantılı tavan, artı günlük bütçenin %25'i uygulanır [RESMİ].
- Bütçe artışı için Meta yüzde vermez. "%20 kuralı" bir pratisyen kuralıdır. Haftada 50+ sonuç alan sette artış başına %20–30 ve artışlar arasında 3–4 gün iyi bir başlangıçtır (*sezgisel*). Kararı F7 (marjinal CPA) ile ver.

---

## 10. Adlandırma standardı

**Neden önemli:** Dinamik URL parametreleri (`{{campaign.name}}`, `{{adset.name}}`, `{{ad.name}}`) reklamın **ilk yayınlandığındaki adı** alır. Sonradan ad değiştirmek UTM değerini değiştirmez. Parametreleri yayından sonra değiştirmek öğrenmeyi yeniden başlatabilir [RESMİ]. Bu yüzden **adı yayından önce doğru koy.** Ad değişikliği Meta'nın önemli düzenleme listesinde yok, ama rapor sürekliliği için yayındaki adları değiştirme.

**Biçim kuralları (*sezgisel*):**
- Alan ayırıcı `_`, alan içi ayırıcı `-`.
- Yalnız ASCII: ı→i, ş→s, ğ→g, ü→u, ö→o, ç→c. Böylece UTM'de ve dışa aktarmada sorun çıkmaz.
- Kodlar BÜYÜK harf.
- Sağlık durumu ya da finansal durum çağrıştıran kelime kullanma. Bu adlar URL'ye gidebilir ve Meta'ya hassas veri göndermek koşullara aykırıdır [RESMİ]. Ayrıca bu tür çağrışımlı özel kitleler 2025'ten beri işaretlenip engelleniyor [RESMİ].
- Alt katman üst katmanın bilgisini tekrar etmez.

**Şablonlar:**
- **Kampanya:** `YYMM_AMAC_KONUM_HUNI_TEKLIF_URUN-veya-HIZMET_BOLGE`
- **Reklam seti:** `KITLE_YAS-veya-KONTROL_OLAY_ALAN_NOT`
- **Reklam:** `KREATIFID_FORMAT-ORAN_ACI_PERSONA_vN`

**Ada yazılmayanlar:**
- **Hedef tutarı.** §7 hedefi haftada %5–10 oynatır, yayındaki ad ise değişmez. Adda kalan "350" ya da "3,4" kısa sürede eskir ve adı okuyan yanlış hedefi görür. Adda yalnız teklif **türü** olsun; tutarı ve her değişikliğini değişiklik günlüğüne yaz (Modül 06 §10).
- **Karma kampanyada tek konum.** Dönüşüm konumu reklam seti düzeyinde bir ayardır. Setleri farklı konumlarda olan kampanyada KONUM = `MIX` yaz. Bu durumda her set OLAY alanını `KONUM-OLAY` biçiminde yazar (ör. `WA-MPM`, `ARA-CALL`).

**Kodlar:**

| Alan | Kodlar |
|---|---|
| AMAC | BIL, TRF, ETK, PM, UYG, SAT |
| KONUM | WEB, WA, IGD (Instagram Direct), MSN, ARA, FORM, APP, REK (reklamında), MIX (setler farklı konumda) |
| HUNI | TUM (Advantage+ geniş), SOG, ILK, SCK, MUS (mevcut müşteri), DONEM (kampanya dönemi) |
| TEKLIF | HAC (en yüksek hacim), DEG (en yüksek değer), CPA (sonuç başına ücret hedefi), ROAS (ROAS hedefi), TUS (teklif üst sınırı). Bütçe türü eki: KB / SB. **Tutar yazılmaz** |
| KITLE | GENIS (Advantage+ hedef kitle, öneri yok), ONERI (Advantage+ hedef kitle + ilgi, özel ya da benzer kitle önerisi), ILK (ılık özel kitle, ör. `OK_IG_TUM_365G`), SCK (sıcak, ör. `OK_WEB_ATC-PURharic_14G`), MUS (mevcut müşteri, ör. `OK_LISTE_MUS_2610`, `OK_WEB_PUR_180G`). Kitlenin tam adı Modül 03 §3.3 standardındadır. Set adına yalnız kodu yaz; kaynağı gerekirse KONTROL alanında kısalt (ör. `liste`) |
| OLAY | PUR, ATC, IC, LPV, LEAD, QLEAD, KONUS, MPM (mesajlaşma yoluyla PM), CALL, REACH, TP. MIX kampanyada önüne konum gelir: `WA-MPM` |
| ALAN | APL (Advantage+ alanlar), MAN (manuel). MAN yalnız Bilinirlik ve Trafik'te ya da değişikliğin henüz açılmadığı hesaplarda kullanılır. Diğer amaçlarda set düzeyinde manuel alan kalkıyor (§8) |
| FORMAT | IMG, VID, CAR, COL, FLX (esnek), KAT (katalog) |

**Doldurulmuş örnekler:**
- `2610_SAT_WEB_TUM_DEG-KB_Kadin-Elbise_TR`
  - `GENIS_25plus-MUSharic_PUR_APL_ana`
  - `MUS_liste-sinirli15_PUR_APL_mevcut`
    - `K017_VID-9x16_sorun-cozum_ofis-calisani_v2`
- `2610_PM_MIX_TUM_HAC-SB_Tesisat_Uskudar`
  - `GENIS_ilce-6_WA-MPM_APL_7-24`
  - `GENIS_ilce-6_ARA-CALL_APL_08-20`
- `2611_SAT_WEB_DONEM_ROAS-KB_11-11_TR` (hedef tutarı günlükte: "ROAS hedefi 3,4, 2.11.2026")

**Kreatif kaydı:** `K017` gibi her kreatif kimliğini dosya adı, açı, persona ve ilk yayın tarihiyle bir tabloda tut. Aynı kreatif farklı kampanyalarda aynı kimlikle görünsün.

**Ad şablonu:** Reklam Yöneticisi'nde alanları otomatik dolduran ad şablonu bazı hesaplarda var [PRATİSYEN]; varsa bu biçimi oraya kaydet. Kurulum Kılavuzu §4'teki "Ekim 2026 – WhatsApp Sipariş – Kadıköy" bu standartta `2610_ETK_WA_TUM_HAC-KB_Siparis_Kadikoy` olur.

---

## 11. Yayın öncesi strateji kontrol listesi

- [ ] Ürün/hizmet Modül 09 §1 risk kapısında sınıflandırıldı: kırmızı değil. Sarıysa insan onayı yazılı olarak alındı.
- [ ] Başabaş CPA/ROAS panel değerine çevrildi (F1–F3). Pikselin KDV dahil mi hariç mi değer gönderdiği biliniyor.
- [ ] N ve set sayısı hesaplandı (F4–F5). Yeni hesapta beklenen CPA'nın kaynağı yazıldı ve 7. gündeki yeniden hesap takvimde (§1).
- [ ] F6 sağlandı **ya da** istisna gerekçesi ve müşteriye yapılan yazılı bildirim kayıtlı (§1).
- [ ] Amaç, konum ve performans hedefi §2.1'den seçildi; sonuç Meta'ya ulaşıyor.
- [ ] Satış hesabında hedef kitle segmentleri (mevcut müşteri + etkileşimli) tanımlandı (§3.3).
- [ ] Müşteri listesi kullanılıyorsa (dahil etme ya da hariç tutma) KVKK kapısı geçildi. Kontrol edilenler: listenin kaynağı, aydınlatma metni, yurt dışı aktarım için hukukçu onayı (Modül 03 §4.4, Modül 09 §5.6).
- [ ] Kesin sınırlar kontrollerde ya da hesap düzeyinde.
- [ ] Aynı kitleye **plansız** çakışan ikinci kampanya ya da set yok. İki istisna çakışma sayılmaz: §5.4 koşullarıyla açılan dönem kampanyası (K2) ve aynı kampanyada farklı dönüşüm konumlu setler (Şablon D). Bu durumlarda Meta aynı reklamverenin çakışan reklamlarından toplam değeri en yüksek olanı açık artırmaya sokar [RESMİ]. Küçük setin harcamaması bundan olabilir.
- [ ] İndirim ya da fiyat içeren her reklamda 10 günlük referans fiyat kontrolü yapıldı. Referans fiyat, tarihler ve stok sınırlıysa adet reklamda yazıyor (Modül 09 §5.3).
- [ ] Bütçe türü §6'ya, teklif §7'ye göre seçildi (veri yoksa En yüksek hacim/değer).
- [ ] Advantage+ alanlar açık. Müşterinin alan kısıtı isteği §8'deki kutuya göre yanıtlandı.
- [ ] Advantage+ kreatif iyileştirmelerinden yüksek riskli olanlar kapalı (§4 kapsam notu, Modül 04 §10.1).
- [ ] Saat planı yalnız yanıt kapasitesi gerektiriyorsa var.
- [ ] Sezon kampanyası zirveden en az 2–3 hafta önce yayında.
- [ ] Adlar §10 standardında ve yayından **önce** konuldu. Adda hedef tutarı yok.
- [ ] İlk 7 gün izleme planı hazır (Kurulum Kılavuzu §6.3); ilk 72 saatte performansa göre kapatma yok.

---

## Doğrulanması gerekenler

- **Saat planı + günlük bütçe:** Meta'nın TR/EN sayfası mümkün diyor; üçüncü taraf rehberler yalnız toplam bütçede diyor.
- **Mesajlaşma yoluyla potansiyel müşteri, WhatsApp Flows, Advantage+ potansiyel müşteri kampanyası:** kademeli; Türkiye'de görünürlüğü doğrulanmadı.
- **Nitelikli potansiyel müşteri:** CRM şartı (Nisan 2026) ve ayda ≥200 eşiği biliniyor; "Meta kaynağı" alternatifi hesaba göre değişebilir.
- **Hariç tutulan alanlara %5 harcama:** API'de varsayılan kapalı, arayüzde açık geldiği bildiriliyor. Set düzeyinde seçimin kalktığı hesapta geçerli olup olmadığı belirsiz.
- **Set düzeyinde alan seçiminin kalkması (§8):** Meta'nın sayfası kademeli diyor ve tarih vermiyor. Platform, cihaz ve işletim sistemi hariç tutmalarının da kalktığını yalnız pratisyen aktarıyor. Pratisyen bir iddiaya göre hassas sektörler kapsam dışı; bu doğrulanmadı. Amaç bazlı hesap düzeyi hariç tutmanın aynı dört alan grubuyla sınırlı olup olmadığı da belirsiz.
- **Hedef kitle segmentleri (§3.3):** Etkileşimli kitleye Instagram ya da Sayfa etkileşim kitlesinin eklenebildiği doğrulanmadı.
- **Kreatif testi aracı ve ad şablonu:** resmi yardım sayfası bulunamadı; pratisyen gözlemi.
- **Öğrenme eşiği:** Bazı 2026 blogları Advantage+ satışta "haftada 25" diyor; Meta'nın Ekim 2026 sayfaları ~50 diyor. Bu modül 50'yi kullanır.
- **Günlük bütçe esnekliği (%75)** ve **ROAS giriş biçimi** (ondalık virgül).
- **Konum ücreti:** başlangıç tarihi kaynaklarda çelişiyor (1 Temmuz / 1 Ekim 2026); 2027'de değişip değişmeyeceği (Kurulum Kılavuzu §3.6).
- **2027 Ramazan ve bayram tarihleri:** Diyanet takviminden doğrulanmalı.
- **Özel reklam kategorilerinde Advantage+ hedef kitle:** yardım merkezi "erişemez", geliştirici belgesi "kademeli açılıyor" diyor.

---

## Kaynaklar

**Meta yardım merkezi [RESMİ]** (erişim Ekim 2026):
- Reklam verme amacını seçme: https://www.facebook.com/business/help/1438417719786914
- Performans hedefleri: https://www.facebook.com/business/help/355670007911605
- Minimum bütçe en iyi uygulamaları: https://www.facebook.com/business/help/203183363050448
- Öğrenme aşaması: https://www.facebook.com/business/help/112167992830700
- Önemli düzenlemeler: https://www.facebook.com/business/help/316478108955072
- Öğrenmeyle sınırlı: https://www.facebook.com/business/help/269269737396981
- Açık artırma çakışması: https://www.facebook.com/business/help/537699989762051
- Reklam hacmi: https://www.facebook.com/business/help/2720085414702598
- Hedef kitle bölünmesi ve birleştirme: https://www.facebook.com/business/help/2419480091640105
- Teklif stratejileri: https://www.facebook.com/business/help/1619591734742116
- Ücret ve teklif kontrolleri: https://www.facebook.com/business/help/491846184627504
- Sonuç başına ücret hedefi en iyi uygulamaları: https://www.facebook.com/business/help/176339196621566
- ROAS hedefi: https://www.facebook.com/business/help/1113453135474912
- Teklif üst sınırı: https://www.facebook.com/business/help/272503946776144
- Değer kuralları: https://www.facebook.com/business/help/535014515741813
- Advantage+ satış kampanyaları: https://www.facebook.com/business/help/1362234537597370
- Advantage+ açık/kapalı: https://www.facebook.com/business/help/906206294602874
- Advantage+ bütçe stratejisi: https://www.facebook.com/business/help/1602924913861363
- Mevcut müşteri bütçe üst sınırı değişikliği: https://www.facebook.com/business/help/1142614290190459
- Advantage+ hedef kitle: https://www.facebook.com/business/help/273363992030035
- Kontroller ve öneriler: https://www.facebook.com/business/help/938372127764391
- Hesap düzeyi kontroller: https://www.facebook.com/business/help/1438478636941047
- Müşteri yaşam döngüsü stratejisi: https://www.facebook.com/business/help/1740191093676743
- Hedef kitle segmentleri: https://www.facebook.com/business/help/1212227546879178
- Kampanya ve reklam seti bütçeleri: https://www.facebook.com/business/help/458847204894307
- Advantage+ kampanya bütçesi en iyi uygulamaları: https://www.facebook.com/business/help/2177212182495139
- Advantage+ kampanya bütçesi: https://www.facebook.com/business/help/153514848493595
- Reklam seti bütçesi paylaşımı: https://www.facebook.com/business/help/1388266028979935
- Günlük bütçe: https://www.facebook.com/business/help/190490051321426
- Bütçe değişikliğinin zamanlaması: https://www.facebook.com/business/help/196976447912140
- Hafta ortası bütçe değişikliği: https://www.facebook.com/business/help/525189242169568
- Performans dalgalanmaları: https://www.facebook.com/business/help/1364841787225722
- Kırılım etkisi: https://www.facebook.com/business/help/770303663944673
- Advantage+ reklam alanları: https://www.facebook.com/business/help/196554084569964
- Reklam alanları ve yayın: https://www.facebook.com/business/help/965529646866485
- Reklam alanı en iyi uygulamaları: https://www.facebook.com/business/help/23942209872116948
- Reklam alanı ayarlarındaki yenilikler (set düzeyinde alan seçiminin kalkması, tarihsiz; erişim 19 Ekim 2026): https://www.facebook.com/business/help/1546753450586085
- Reklamların planlanması: https://www.facebook.com/business/help/1037425549606837
- Reklam seti planlama: https://www.facebook.com/business/help/1381935425400769
- Mesaja yönlendiren reklamlar: https://www.facebook.com/business/help/1816962591668838
- Mesaj reklamı en iyi uygulamaları: https://www.facebook.com/business/help/269324800441478
- WhatsApp'a yönlendiren reklamlar: https://www.facebook.com/business/help/447934475640650
- Mesajlaşma yoluyla potansiyel müşteri: https://www.facebook.com/business/help/575610661605746
- Arama reklamları: https://www.facebook.com/business/help/378168646496279
- Hızlı form türleri: https://www.facebook.com/business/help/252352181957512
- Randevu talebi sorusu: https://www.facebook.com/business/help/374052383033400
- Potansiyel müşterilere yanıt: https://www.facebook.com/business/help/2246428158756947
- URL parametreleri: https://www.facebook.com/business/help/2360940870872492
- Konum ücreti: https://www.facebook.com/business/help/1238737454289085
- Özel reklam kategorisi (konut): https://www.facebook.com/business/help/1198401317374558
- Hesap sınırları: https://www.facebook.com/business/help/652738434773716
- A/B testi: https://www.facebook.com/business/help/1738164643098669

**Diğer Meta kaynakları [RESMİ]:**
- Advantage+ kampanyaları (geliştirici): https://developers.facebook.com/docs/marketing-api/advantage-campaigns
- Advantage+ satış ve uygulama kampanyası deneyimi (3 Haziran 2025): https://developers.facebook.com/blog/post/2025/06/03/advantage-plus-campaign-experience-for-sales-and-app/
- Hariç tutulan alanlarda sınırlı harcama (8 Ekim 2025): https://developers.facebook.com/blog/post/2025/10/08/introducing-limited-spend-on-excluded-placements-in-marketing-api/
- 2026 tatil rehberi (11 Haziran 2026): https://www.facebook.com/business/news/2026-winning-hearts-boosting-carts
- WhatsApp alt huni optimizasyonları: https://whatsappbusiness.com/resources/resource-library/down-funnel-optimizations-whatsapp/

**Haber ve veri [HABER]:**
- DataReportal, Digital 2026 Turkey: https://datareportal.com/reports/digital-2026-turkey
- TÜİK 2026 bilişim araştırması haberi (5 Ağustos 2026): https://www.birgun.net/haber/whatsapp-zirvede-tuik-internet-kullanim-verilerini-acikladi-727545
- Mart 2026 tıklama ilişkilendirmesi değişikliği: https://ppc.land/meta-rewrites-click-attribution-rules-finally-aligning-with-google-analytics/
- Hariç tutulan alanlara %5 harcama, arayüz varsayılanı: https://ppc.land/meta-pushes-spending-5-on-excluded-placements-for-sales-and-leads-campaigns/
- Social Media Today, Meta'nın güncel alan kontrolü rehberi (12 Ekim 2026): https://www.socialmediatoday.com/news/meta-offers-updated-ad-placement-control-guidance/832791/
- Ramazan 2025 arama verisi (Microsoft Advertising, 25 Şubat 2026): https://preview-about.ads.microsoft.com/en/blog/post/february-2026/decoding-ramadan-2026-data-driven-strategies-for-the-middle-east-turkiye-and-africa

**Pratisyen [PRATİSYEN]:**
- Jon Loomer, Meta Ads master brief (2 Temmuz 2026): https://www.jonloomer.com/meta-ads-master-brief/
- Jon Loomer, bütçe güncellemeleri: https://www.jonloomer.com/updates-to-meta-ads-budgeting/
- Jon Loomer, kreatif testi aracı: https://www.jonloomer.com/meta-creative-testing/
- Jon Loomer, kampanya adlandırma: https://www.jonloomer.com/facebook-campaign-naming-convention/
- Jon Loomer, set düzeyinde alan kontrollerinin kaldırılması (20 Ağustos 2026): https://www.jonloomer.com/meta-removing-placement-controls-ad-sets/
- Common Thread Collective, Meta metodolojisi: https://commonthreadco.com/pages/methodology-meta
- Flighted, hesap yönetimi en iyi uygulamaları: https://www.flighted.co/blog/facebook-ads-management-best-practices
- Darkroom, BFCM planı: https://www.darkroomagency.com/observatory/the-bfcm-paid-media-plan
- Wicked Reports, Advantage+ eleştirisi (26 Kasım 2025): https://www.wickedreports.com/blog/meta-is-pushing-advantage-campaigns-relentlessly
- Adlandırma pratikleri (üçüncü taraf): https://admanage.ai/blog/how-to-organize-facebook-ads ; https://adkit.so/resources/campaign-naming-conventions
- Saat planı ve toplam bütçe iddiası (üçüncü taraf): https://help.hunchads.com/en/articles/15259890-dayparting ; https://madgicx.com/blog/meta-campaign-scheduling
