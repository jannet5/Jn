# Test, optimizasyon ve ölçekleme

> **Modül 06 · Profesyonel paket · Hazırlanma: 17 Ekim 2026.** Menü yolları, ödeme ve hata kodları burada yok. Onlar için **Kurulum Kılavuzu** (`../KILAVUZ.md`): bütçe esnekliği §5.2, ilk 7 gün §6.3, sorun giderme §10, konum ücreti ve KDV §3.6 ve §9. Bu modül şu formülleri hazır kabul eder: başabaş CPA/ROAS ve panel çarpanı (Modül 02 F1–F3), haftalık beklenen sonuç **N** (F4), set kapasitesi (F5), marjinal CPA (F7, Modül 01 §3.4). Kreatifte "farklı" tanımı ve yorgunluk tablosu Modül 04 §8–9'da.

## Bu modülde

- Tek değişkenli **test hipotezi** kalıbı, test aracı seçimi ve hacme göre **kreatif test kurgusu**.
- İlk **7 / 14 / 30 / 90 gün** için gün gün plan ve haftalık karar tablosu.
- Aşamalı **kapatma kuralları** (3× / 5× CPA mantığı ve olasılık tablosu) ile **ölçekleme kuralları**: dikey ve yatay, adım büyüklüğü, bekleme süresi, geri dönüş, riskler.
- **Öğrenme aşamasını koruma**, **kırılım etkisi** (breakdown effect) kontrol listesi ve 11.11, Kara Cuma, Ramazan 2027 için **sezon planı**.
- Kopyalanabilir **değişiklik günlüğü** şablonu.

**Etiketler:** **[RESMİ]** Meta'nın kendi sayfası. **[PRATİSYEN]** ajans, danışman ya da araç sağlayıcı (çıkar çatışması olabilir). **[HABER]** basın. *sezgisel* = ölçülmemiş pratisyen kuralı. *türetilmiş* = bu modülde yapılan aritmetik. *varsayım* = örnek rakam. **Genel uyarı:** Meta yalnızca süre (en az 7 gün), hacim (haftada ~50 sonuç) ve birkaç bütçe kuralı veriyor. "%20 artır", "3× CPA'da kapat" gibi eşiklerin hepsi pratisyen kuralıdır.

---

## 0. Değişmez kurallar

1. **Karar düzeyi:** Kampanya bütçesinde kampanya, reklam seti bütçesinde set düzeyinde karar ver. En az **7 tam günlük** veri kullan. Saatlik ya da günlük kırılımla karar verme [RESMİ].
2. **İlk 72 saat:** Yalnız operasyonel sebeple kapat: ret, sıfır yayın, kırık bağlantı ya da izleme, yanlış fiyat [PRATİSYEN].
3. **Toplu değiştir, sonra 7 gün bekle.** Tek istisna, §6.2'deki bütçe adımları. Her değişikliği önce günlüğe yaz (§10).
4. **Ölçekleme sırası:** yeni ve farklı kreatif → kademeli bütçe → yeni kampanya ya da pazar (*sezgisel*).
5. **Kararı marjinal sonuçla ver,** ortalamayla değil (F7).
6. **Bütçenin çoğunu alan öğeyi kapatmadan önce** §8'deki listeyi geç.
7. **Elle aç-kapa ile test yapma.** Meta bunu önermiyor: kitleler çakışır, sonuç güvenilmez olur [RESMİ].

---

## 1. Test hipotezi yazma

**Kalıp:**

> **Eğer** [tek değişiklik] yaparsak, [kampanya/set] için [birincil metrik] en az [%X] [düşer/artar], **çünkü** [kanıt ya da gözlem]. **Ölçüt:** [araç], [süre], [hücre başına en az olay], [karar kuralı].

Meta da ölçülebilir bir hipotez ve tek değişken istiyor [RESMİ].

**Hipotez kartı:**

| Alan | Kural |
|---|---|
| ID | `H-YYAA-NN`. Günlükte bu kimlikle anılır |
| Değişken | **Tek değişken.** Çok değişkenli test yalnız "iki paketten hangisi?" sorusunu cevaplar; kazancın kaynağı bilinmez [RESMİ] |
| Birincil metrik | Optimize edilen olayın sonuç başına ücreti ya da ROAS'ı. TO ve BGBM ikincil |
| En küçük anlamlı fark | %15–20. Küçük bütçe daha küçük farkı ölçemez (*sezgisel*) |
| Hücre başına olay | En az 20, mümkünse 50 (*sezgisel*; Meta'nın haftalık 50 eşiğinden esinli) |
| Süre | 7–30 gün. Karar süreci uzun işte 10–14 gün [RESMİ] |
| Karar kuralı | Önceden yaz: "Güven ≥ %65 ve fark ≥ %15 ise B'ye geç, değilse A'da kal" |

**Bütçe formülü (*türetilmiş*; Flighted'in formülüyle aynı mantık [PRATİSYEN]):**

```
Günlük test bütçesi = hücre sayısı × hücre başına olay × beklenen CPA ÷ test günü
Test payı           = günlük test bütçesi ÷ hesabın günlük bütçesi   (hedef ≤ %20)
```

**Doldurulmuş örnekler (*varsayım*):**

- **H-2610-01 (e-ticaret):** "Eğer ana videonun ilk 3 saniyesine 'ücretsiz iade' itirazını koyarsak satış CPA'sı en az %15 düşer, çünkü sohbetlerde en sık sorulan şey iade. Ölçüt: kreatif testi aracı, 2 hücre, 10 gün, hücre başına 20 satış."
  - Bütçe: 2 × 20 × 200 TL ÷ 10 = **800 TL/gün**. Hesap günde 4.000 TL harcıyorsa pay %20, uygun.
  - Hesap günde 2.000 TL harcıyorsa pay %40 olur. Süreyi 20 güne çıkar (400 TL/gün) ya da hücre hedefini 10 satışa indirip sonucu "yön göstergesi" diye kaydet.
- **H-2610-02 (WhatsApp'tan satan yerel hizmet):** "Eğer optimizasyon olayını 'mesajlaşma konuşması'ndan 'mesajlaşma yoluyla potansiyel müşteri'ye çevirirsek iş başı maliyet düşer, çünkü sohbetlerin çoğu fiyat sorup kayboluyor. Ölçüt: A/B testi, eşit bütçe, 14 gün." Olaylar farklı olduğu için kararı CRM'deki iş başı maliyetle ver. Olayın hesapta açık olup olmadığı *doğrulanmalı* (Modül 02).

**Test sırası (büyük etkiden küçüğe, *sezgisel*):** (1) teklif/paket ve mesaj açısı, (2) format, (3) kanca, (4) optimizasyon olayı ve teklif stratejisi, (5) açılış sayfası. Kitle ve reklam alanı testleri en sona kalır. Meta'ya göre en iyi kaldıraç artık niş hedefleme değil, kreatif çeşitliliği [RESMİ].

---

## 2. Test aracı seçimi

| Soru | Araç | Koşullar |
|---|---|---|
| Hangi strateji? (kitle, alan, optimizasyon olayı, teklif stratejisi, kampanya bütçesi açık/kapalı) | **A/B testi** (araç çubuğundaki düğme ya da Deneyler) | Kitle rastgele ve çakışmasız bölünür, kimse iki sürümü görmez. Eşit bütçe, 7–30 gün. En fazla 5 kampanya. Başka testteki ya da rezervasyon kampanyası alınamaz [RESMİ] |
| Yeni kreatif fikirlerinden hangisi? | **Kreatif testi aracı** (reklam düzenleme ekranında "Creative Testing") | 2–5 yeni reklam (bazı hesaplarda 10), eşit harcama, varsayılan 7 gün. Yalnız "En yüksek hacim" teklifiyle çalışıyor. Teste bütçenin en fazla %20'si öneriliyor. Mevcut reklam teste alınamıyor [PRATİSYEN]. Geliştirici belgesi 2–5 hücreyi doğruluyor [RESMİ]. Resmi yardım sayfası yok, *doğrulanmalı* |
| Bir reklam hiç harcama almıyor | **"Yayını bu reklama it"** (varsa) | Bütçenin seçilen yüzdesini 1–30 gün o reklama ayırır. Test özelliği; alışkanlık yapma [PRATİSYEN] |
| Reklam gerçekten ek satış getiriyor mu? | **Dönüşüm yükselişi** ya da hariç tutmalı A/B testi | Yükseliş sonucu %90+ güvenle güvenilir sayılır [RESMİ]. Uygun değilse coğrafi kapatma testi (Modül 05 §11) |
| Varyasyonları sistem karıştırsın | Esnek format, dinamik kreatif | **Test aracı değil.** Dağıtım eşit değil, sonuçlar toplu [RESMİ] |

**A/B testi kuralları [RESMİ]:**
- Test **Yayında** durumuna geçince test edilen değişken düzenlenemez. Planı değiştirmek ya da iptal etmek sonucun güvenilirliğini bozar.
- Testi iptal etmek kampanyaları durdurmaz. Yayını ayrıca durdur.
- Hariç tutma yoksa kazanan sonuç başına ücrete göre seçilir. Meta sonucu on binlerce kez simüle edip güven yüzdesi verir: **%65+ kazanan** sayılır. Test öncesi tahmini güç **%80+** olsun.
- Test kitlesini aynı anda başka kampanyada kullanma.

**Eğer-ise (*sezgisel*):**
- Güven ≥ %65 ve karar kolay geri alınır (ör. kreatifi ana sete eklemek) → uygula.
- Karar büyük (ör. tüm hesabın teklif stratejisi) ve güven < %90 → testi uzat ya da tekrarla. %65, klasik %95 standardına göre zayıf bir eşik.
- "Kesin kazanan yok" → fark küçük. Mevcut durumu ya da ucuz seçeneği koru.

---

## 3. Kreatif test kurgusu

**Yöntem seçimi (N = haftalık beklenen sonuç):**

| N | Yöntem | Nasıl |
|---|---|---|
| < 10 | **Test yok, portföy** | Tek set, 2–3 gerçekten farklı kavram. 2–4 haftalık toplam sonuca bak. Reklam bazında kapatma kuralı işlemez [PRATİSYEN] |
| 10–50 | **Aynı sete parti ekle** | Toplam sonuç vasat ya da kötüyse 1–2 haftada bir, tamamen farklı 2–3 kavramlık parti. Bir hafta dokunma [PRATİSYEN Loomer] |
| 50–150 | **Kreatif testi aracı, aynı sette** | Haftada bir test, 2–4 hücre, pay ≤ %20. Set hedefli teklifteyse araç çalışmaz → A/B testi ya da parti ekleme |
| > 150 | **Test → doğrulama → ana kampanya** | Reklam seti bütçeli ayrı test kampanyası; kazananlar doğrulama katmanına (ana bütçenin ~%20'si), sonra ana kampanyaya [PRATİSYEN Foxwell, Flighted] |

**Çelişki:** CTC ayrı test bütçesi tutmuyor, yeni reklamı doğrudan ana kampanyaya koyuyor. Loomer, testte kazananın taşındığı yerde kazanmayabileceğini ve yapının şiştiğini yazıyor [PRATİSYEN]. **Varsayılan:** N ≤ 150 ise ayrı test kampanyası açma.

**Doldurulmuş kurgu (*varsayım*):** Günlük 4.000 TL, hedef panel CPA 200 TL → N = 4.000 × 7 ÷ 200 = 140.
- Pay %20 → haftada 5.600 TL. Kavram başına karar harcaması 3 × 200 = 600 TL → teorik üst sınır ~9 kavram (Modül 04 §8.2). Araç en fazla 5 hücre aldığı için pratikte haftada **bir test, 4 hücre**: hücre başına 1.400 TL ≈ 7 × CPA.
- Test bitmeden hücre kapatma; eşitlik bozulur.
- **Kazanan:** güven ≥ %65 ve CPA, ana setin 7 günlük CPA'sına eşit ya da düşük.
- Kazananı **aynı sette açık bırak.** Test bitince eşit harcama kalkar; sistem kazananı her zaman öne çıkarmayabilir [PRATİSYEN]. Toplam sonuç iyiyse dokunma.
- Başka kampanyaya taşıyacaksan çoğaltırken **mevcut etkileşimi gösterme** seçeneğiyle beğeni ve yorumları koru [RESMİ]. Kopya öğrenmeye sıfırdan başlar.

---

## 4. İlk 7 / 14 / 30 / 90 gün planı

Takvim, Meta'nın 7 gün, 2 hafta ve 3 hafta önerilerinden türetilmiş bir sentezdir (*sezgisel*). Müşteriye gösterilecek kısa sürüm Modül 01 §5.2'de.

**Her gün (10 dakika):** Yayın sütunu (ret, "Öğrenmeyle sınırlı", "Kreatif yorgunluğu"), harcama hızı, bağlantı ve stok, sipariş/sohbet panelinin Meta ile kabaca tutması.
**Her pazartesi:** Önceki tam haftanın (Meta haftayı pazar–cumartesi sayar [RESMİ]) kampanya düzeyi raporu, marjinal CPA, günlükteki "sonuç" sütunları (§10) ve §4.4 kararı.

### 4.1 Gün 0–14

| Gün | Yap | Yapma |
|---|---|---|
| **0** (yayın günü) | Olay testi (Piksel/CAPI ya da WhatsApp etiketi); başabaş ve hedef panel CPA; N ve set sayısı (F1–F5); 3–5 farklı kreatif; adlar (Modül 02 §10); günlükte başlangıç satırı; bildirim kuralları (§5.4) | 10+ set; aynı kitleye çakışan iki kampanya |
| **1** | Yayın başladı mı, ret var mı? Bağlantı, fiyat, stok doğru mu? Olaylar Olay Yöneticisi'ne düşüyor mu? | CPA'ya göre karar |
| **2** | 24–48 saattir sıfır yayın varsa sebebi bul: ret, ödeme, düşük hedef, küçük kitle (Kurulum Kılavuzu §10) | Bütçe ya da hedef oynatma |
| **3** | 72 saat doldu. Operasyonel sorun yoksa dokunma | Toplu düzenleme |
| **4** | 1.000+ gösterim almış reklamın bağlantı TO'sunu hesap ortalamasıyla kıyasla; çok düşükse not al | TO'ya bakıp kapatma |
| **5–6** | 0 dönüşümle 3 × hedef CPA'yı geçen **reklamı** kapat (§5.2) | Set kapatma, yeni set açma |
| **7** | İlk haftanın ön okuması; günlüğe yaz | Karar. Son 48–72 saatin dönüşümleri henüz gelmemiş olabilir [PRATİSYEN] |
| **8** | **1. hafta kararı**, kampanya/set düzeyinde (§4.4) | Kırılımda en çok harcayanı kapatma |
| **9–10** | Değişiklik yapıldıysa dokunma. Bütçe arttıysa harcamanın yeni seviyeye çıktığını kontrol et | Başka türde ikinci değişiklik |
| **11–12** | Ölçeklenen sette 3–4. gün kontrolü; koşul sağlanırsa ikinci adım (§6.2) | — |
| **13–14** | 2. haftayı 1. haftayla kıyasla: ortalama ve marjinal CPA, sıklık, TO eğilimi. Taban çizgisini yaz | Hedefli teklife geçmek (2 hafta dolmadı) |

### 4.2 Gün 15–30

| Gün | Yap |
|---|---|
| **15** | **2. hafta kararı.** 2 haftalık veri ve haftada 50+ sonuç varsa hedefli teklifi değerlendir: sonuç başına ücret hedefi = son 14 günün panel CPA'sı × 1,10–1,20, başabaşı geçmesin [RESMİ; Modül 02 §7]. En temiz geçiş A/B testiyle olur (en yüksek hacim ↔ hedef) |
| **16–21** | Hedef değiştiyse 7 gün dokunma [RESMİ]. Kreatif testi sürüyorsa bitmesini bekle |
| **22** | **3. hafta kararı.** Bütçe tamamen harcanıyor ve CPA hedefte → ölçekle (§6). Bütçe harcanmıyor (teklif kısıtlı) → hedefi %5–10 gevşet [RESMİ] |
| **23–28** | Haftada 50'ye yaklaşılamıyorsa setleri birleştir ya da daha sık gerçekleşen olaya in [RESMİ] |
| **29–30** | **Aylık özet:** kazanan ve kaybeden açılar, ölçülmüş CPA, dönüşüm oranı, sepet tutarı. Modül 01 hedeflerini bunlarla güncelle |

### 4.3 Gün 31–90

| Hafta | Odak |
|---|---|
| **5–6** (gün 31–44) | Kreatif ritmini oturt (Modül 04 §8.2). Haftalık ölçekleme döngüsü. Hedefli teklif 3 haftayı doldurduysa onu da ölçekle [RESMİ] |
| **7–8** (gün 45–58) | İlk **yatay** adım (§6.3). Değer optimizasyonu uygunsa A/B testiyle dene |
| **9–10** (gün 59–72) | **Artırımlılık:** yükseliş testi ya da coğrafi kapatma testi (Modül 05 §11); yeniden hedefleme payını sorgula. Artımlı ilişkilendirme sütununu standart sütunla yan yana aç [RESMİ] |
| **11–12** (gün 73–86) | Yapı denetimi: set sayısı F5'i aşıyor mu, çakışma var mı, kurallar hâlâ doğru mu? Sonraki sezonun takvimi (§9) |
| **13** (gün 87–90) | **Çeyrek raporu:** başlangıca göre CPA/ROAS, marjinal verim, kazanan açılar, açık hipotezler. Sonraki 90 günün test planı |

**Plan sezonla çakışıyorsa §9 önce gelir.** 19 Ekim 2026'da başlayan bir hesapta gün 23 = 11.11, gün 39 = Kara Cuma.

### 4.4 Haftalık karar tablosu (gün 8, 15, 22 ve sonraki pazartesiler)

| Kampanya/set, son 7 gün | Karar |
|---|---|
| CPA ≤ hedef, bütçe tamamen harcanıyor | §6.1 kapısı açıksa ölçekle |
| CPA ≤ hedef, bütçe harcanmıyor | Hedefli teklifte hedefi %5–10 gevşet [RESMİ]. En yüksek hacimde kitle ve kreatif darlığına bak |
| Hedef < CPA ≤ başabaş | Bütçeye dokunma. Yeni ve farklı parti ekle |
| CPA > başabaş, ilk hafta | Öğrenme payı. Yapıyı sadeleştir, parti ekle, bekle |
| CPA > başabaş, 2 hafta üst üste | Bütçeyi bir önceki seviyeye ya da %20–30 aşağı çek; zinciri teşhis et (*sezgisel*) |
| CPA > başabaş 3–4 hafta, sorun kreatif dışında (sayfa, teklif, fiyat) | Harcamayı durdur; müşteriyle teklifi ve sayfayı konuş (Modül 01 §6) |

**Teşhis sırası:** yayın → BGBM → bağlantı TO → açılış sayfası görüntüleme ÷ bağlantı tıklaması → dönüşüm oranı → CPA. `CPA = BGBM ÷ (1.000 × TO × dönüşüm oranı)` (*türetilmiş*). Dönüşüm optimizasyonunda yüksek BGBM tek başına kötü işaret değildir [RESMİ]. Kreatif metrikleri Modül 04 §9'da.

---

## 5. Kapatma kuralları

### 5.1 Önce bunu bil

- **Meta kapatma eşiği vermez.** Tavsiyeleri frenleyicidir: kampanya bütçesinde az yayın alan seti durdurma, durdur-aç yapma; yorgunlukta eskisini kapatmadan yeni reklam ekle; düşük fırsat puanı kapatma sebebi değil [RESMİ].
- Meta'ya göre ücret koşuluyla reklam ya da set kapatmak **öğrenmeyi yeniden tetikleyebilir.** Bu yüzden kampanya bütçeli kampanyada reklamı ya da seti açıp kapatan veya bildirim gönderen ücret koşullu otomatik kural kurulamaz [RESMİ].
- Pratisyenler ayrışıyor: Flighted ve AdManage sayısal eşik veriyor, CTC "reklam kapatma" diyor [PRATİSYEN]. **Varsayılan:** reklam düzeyinde dar kurallarla kapat; set ve kampanyada önce bütçeyi geri çek.

### 5.2 Aşamalı kapatma (reklam düzeyi)

| Aşama | Ne zaman | Kapat, eğer… |
|---|---|---|
| 0 | Hemen | Kırık izleme, yanlış URL ya da fiyat, uyum riski, ret [PRATİSYEN] |
| 1 | 24–48 saat | Sıfır yayın ve sebebi çözülemiyor [PRATİSYEN] |
| 2 | ~1.000 gösterim | Bağlantı TO'su hesap ortalamasının çok altında (ör. %1,5'e karşı %0,3). **Yalnız uyarı,** tek başına sebep değil (*sezgisel*) |
| 3 | 50–200 tıklama | Aynı sayfaya giden diğer reklamlar dönüşürken bunun tıklamaları dönüşmüyor [PRATİSYEN] |
| 4 | Harcama ≥ 3 × hedef CPA | **0 dönüşüm → kapat. 1 → 5×'e kadar bekle. 2 → sarı ışık** (Flighted 1× daha harcıyor; %95 güven için ~6× gerekir). **3+ → devam** [PRATİSYEN Flighted; olasılıklar *türetilmiş*] |

**Neden 3×? (*türetilmiş*)** Gerçek CPA'sı tam hedefte olan iyi bir reklamı yanlışlıkla kapatma olasılığı. Varsayım: dönüşümler bağımsız ve rastgele (Poisson), gecikme yok.

| Harcama (× hedef CPA) | 0 dönüşüm | En çok 1 | En çok 2 |
|---|---|---|---|
| 1× | %36,8 | %73,6 | %92,0 |
| 2× | %13,5 | %40,6 | %67,7 |
| 3× | **%5,0** | %19,9 | %42,3 |
| 5× | %0,7 | **%4,0** | %12,5 |
| 6,3× | %0,2 | %1,3 | **%5,0** |

1×'te kapatmak iyi reklamların üçte birinden fazlasını öldürür. Model, CPA'sı hedefin 1,5 katı olan vasat reklamı yakalamaz. Hedef CPA gerçekçi değilse iyi reklamları da kapattırır.

**İstisnalar (kapatma, bekle):**
- Reklam setin bütçesinin çoğunu alıyor ve set toplamı hedefte → §8.
- Haftada 10'dan az dönüşüm var → reklam kuralı güvenilmez; 2–4 haftalık hesap toplamına bak [PRATİSYEN].
- İlk gün CPA'sının hedefin 2 katı olması tek başına sebep değil [PRATİSYEN].

### 5.3 Set ve kampanya: kapatma yerine geri çekme

| Durum | Eylem |
|---|---|
| Ölçek adımından sonra marjinal CPA > başabaş | Bir önceki bütçeye dön [PRATİSYEN Heath] |
| 2 hafta üst üste CPA > başabaş | Bütçeyi küçük adımlarla düşür [PRATİSYEN Bind]; yapıyı sadeleştir, yeni parti |
| Stok ya da kapasite yüzünden geçici durdurma | 7 günden kısa tut: **7 gün ya da daha uzun duran setin öğrenmesi baştan başlar** [RESMİ]. Uzun sürecekse bütçeyi düşür |
| Kampanya bütçesinde az harcama alan set | Durdurma [RESMİ] |

### 5.4 Otomatik kurallar: önce bildirim

Kural eylemi kapatma, açma, bütçe ya da teklif değiştirme veya **yalnız bildirim** olabilir [RESMİ]. Sınırlar [RESMİ]: hesap başına 250 kural; bir kural yalnız aynı düzeydeki öğelere uygulanır; birden çok koşul eklenirse hepsi birlikte sağlanmalı; siyasi reklamda ve rezervasyonda çalışmaz.

Başlangıç kuralları (*sezgisel*; eylem **Sadece bildirim gönder**):
1. **K-3X (reklam):** Sonuçlar < 1 · harcama > 3 × hedef CPA · oluşturmadan sonra geçen saat > 72. "Harcanan tutar" Meta'nın koşul listesinde görünmüyor; arayüzde yoksa bu kontrolü günlük rutinde elle yap (*doğrulanmalı*).
2. **K-HARCAMA:** Toplam bütçenin belirli yüzdesi harcanınca bildirim; Meta'nın hazır kural türü [RESMİ].
3. **K-SIKLIK (set, 7 gün):** Sıklık, hesabın kendi 7 günlük tabanının belirgin üstünde. Sihirli sayı yok (Modül 04 §9).

Otomatik kapatmaya ancak bildirimler 4 hafta boyunca doğru çıkarsa geç (*sezgisel*).

---

## 6. Ölçekleme kuralları

### 6.1 Giriş kapısı (hepsi "evet" olmalı)

- [ ] Son 7 günde CPA ≤ hedef (ya da ROAS ≥ hedef), kampanya/set düzeyinde.
- [ ] Bütçe tamamen harcanıyor. Harcanmıyorsa bütçe artışı işe yaramaz; önce teklif, kitle ya da kreatif [RESMİ].
- [ ] Haftada ≥ 50 sonuç ya da en az 2 tam haftalık veri var.
- [ ] Hedefli teklif varsa kampanya en az **3 haftadır** yayında [RESMİ].
- [ ] Ölçüm sağlam; stok ve hizmet kapasitesi var (Modül 01 §4.3).
- [ ] Son 7 günde başka önemli düzenleme yok.

### 6.2 Dikey ölçekleme (bütçe artışı)

Meta yüzde vermiyor: 100→101 büyük olasılıkla öğrenmeyi sıfırlamaz, 100→1.000 sıfırlayabilir [RESMİ]. "%20 kuralı" bu belirsizlikten doğdu; Loomer ve Heath reddediyor, Vaizle ve AdManage savunuyor [PRATİSYEN].

| Durum | Adım | Bekleme | Dayanak |
|---|---|---|---|
| N ≥ 50, öğrenmeden çıkmış set | %20–30 | en az 3–4 gün | *sezgisel* (pratisyen aralıklarının ortası) |
| N < 50, set zaten öğrenmede ya da sınırlı | %30–50, gerekirse 2× | 7 gün | [PRATİSYEN Heath] + *sezgisel* |
| Hedefli teklif (CPA ya da ROAS hedefi) | %20–30 | 7 gün; teklifi bütçeden 1 hafta sonra değiştir | [RESMİ] kademeli artış + *sezgisel* adım |
| Kampanya bütçesi, çok set | %15–20 | 4–7 gün; kampanya bütçesi değişikliği birden çok seti öğrenmeye sokabilir [RESMİ] | *sezgisel* |
| Meta "öğrenmeye girmeden X'e çıkar" önerisi gösteriyor | Önerilen tutar | 7 gün | [PRATİSYEN Loomer], *hesaba göre değişebilir* |

**Adım sonrası kontrol (*sezgisel*):**
- **3–4. gün:** Harcama yeni seviyeye çıktı ve bu günlerin CPA'sı ≤ hedef × 1,2 → bir adım daha atılabilir.
- **7. gün:** Marjinal CPA ≤ hedef → devam. Hedef < marjinal ≤ başabaş → bütçeyi sabit tut, yeni kreatif ekle. Marjinal > başabaş → bir önceki bütçeye dön.

**Teknik kurallar [RESMİ]:** Yeni bütçe ~15 dakikada geçerli olur. Bütçe ve teklif birlikte değişecekse önce bütçe, 15 dakika sonra teklif. Bütçe değiştirmek için seti durdurup açma. Gün içi tavan, o gün geçerli olan **en yüksek** bütçenin %175'idir; bütçeyi gün içinde artırıp düşürmek o günün tavanını yükseltebilir.

**Doldurulmuş merdiven (*varsayım*):** hedef panel CPA 250 TL, başabaş 340 TL. Marjinal CPA bir önceki bütçe seviyesindeki haftaya göre hesaplanır.

| Hafta | Günlük bütçe | Haftalık harcama | Satış | Ort. CPA | Marjinal CPA | Karar |
|---|---|---|---|---|---|---|
| 1–2 | 1.500 | 10.500 | 52 | 202 | — | 2 hafta istikrarlı, kapı açık → +%25 → 1.875 |
| 3 | 1.875 | 13.125 | 63 | 208 | 2.625 ÷ 11 ≈ 239 | ≤ hedef → +%25 → 2.350 |
| 4 | 2.350 | 16.450 | 72 | 228 | 3.325 ÷ 9 ≈ 369 | > başabaş → 1.875'e dön, yeni parti ekle |
| 5 | 1.875 | 13.125 | 68 | 193 | — | Parti işe yaradı; bir hafta bekle, sonra +%20 |

4. haftada ortalama CPA (228 TL) hâlâ hedefin altında görünüyor, ama son artışın getirdiği her ek satış başabaşın üstüne mal oldu. Sezon ve kreatif değişikliği bu kıyası bozabilir; bunları günlüğe bağlam olarak yaz.

### 6.3 Yatay ölçekleme

| Seçenek | Ne zaman |
|---|---|
| Aynı sete yeni **persona ya da açı** (Modül 04 §8.1) | İlk tercih; yapı değişmez |
| Yeni **ürün grubu** ya da katalog kampanyası | Ayrı iş hedefi ve onu besleyecek bütçe varsa |
| Yeni **bölge ya da ülke** | Önce aynı sette konumu genişlet; fiyat, kargo ya da dil farklıysa ayrı kampanya |
| Yeni **dönüşüm konumu** (WhatsApp, form, arama) | Farklı olay ayrı kampanya ister |
| Kanıtlanmış reklamlarla **Advantage+ satış** kampanyası | Ajansların "challenger/genişleme" adımı [PRATİSYEN CTC, Flighted] |

**Yapma:** Kazanan seti aynı kitleyle kopyalamak. Benzer setler aynı açık artırmaya girince yalnız biri yarışır; kopya bütçesini harcayamayabilir ve öğrenmeden çıkamayabilir [RESMİ]. Foxwell yerinde artırmayı, Vaizle kopyayı daha güvenli buluyor [PRATİSYEN]. **Varsayılan:** yerinde artırma.

**İleri seviye (N ≥ 150):** Bütçeyi beklenen harcamanın üstünde tut, verimi ROAS hedefi ya da teklif üst sınırıyla sınırla; sistem o getiri eşiğinde bulabildiği kadar harcar [PRATİSYEN CTC]. Risk: hedef çok sıkıysa harcama düşer.

### 6.4 Riskler

| Risk | Belirti | Önlem |
|---|---|---|
| Azalan getiri | Ortalama CPA hedefte, marjinal başabaşın üstünde. Meta da ucuz fırsatlar bittikçe ücretin arttığını söylüyor [RESMİ] | F7 ile karar; geri dön |
| Öğrenmenin sıfırlanması | "Öğrenme" durumu, dalgalı CPA | Adımı küçült, değişiklikleri topla |
| Sıcak kitlenin tükenmesi | İlk haftalar parlak, sonra düşüş | Kitle segmenti kırılımı; yeni soğuk kitle kavramı [PRATİSYEN Loomer] |
| Açık artırma çakışması | Kopya set harcamıyor | Kopyayı kapat, bütçeyi asıl sete ver [RESMİ] |
| Kapasite, stok, nakit | Yanıt süresi uzuyor, iade artıyor; fatura panelin 1,05 katı | Bütçeyi kapasiteye bağla (Modül 01 §4.3); planı faturalı tutarla onayla (Kurulum Kılavuzu §9) |

---

## 7. Öğrenme aşamasını koruma

**Önemli düzenleme sayılanlar [RESMİ]:**
- **Her zaman:** hedefleme, kreatif ya da optimizasyon olayı değişikliği; sete yeni reklam eklemek; teklif stratejisi değişikliği; seti 7 gün ya da daha uzun durdurmak.
- **Büyüklüğüne göre:** bütçe tutarı, teklif ya da hedef tutarı, harcama sınırı.
- **Kampanya bütçesinde:** bütçenin setler arasında yer değiştirmesi ve yeni set eklemek diğer setleri sıfırlamaz. Kampanya bütçesini ya da kampanya teklif stratejisini değiştirmek birden çok seti öğrenmeye sokabilir.

**Koruma kuralları:**
1. Haftada bir **değişiklik günü** belirle (ör. pazartesi rapordan sonra). Bütçe adımları dışındaki her şeyi o oturumda yap (*sezgisel*; toplu değişiklik önerisi [RESMİ]).
2. Yeni reklamları tek tek değil **parti halinde** ekle. Loomer 22 reklamlı bir sete tek reklam eklediğinde set öğrenmeye girmedi [PRATİSYEN]; Meta belgesi bunu yine de önemli düzenleme sayıyor [RESMİ].
3. Kırık bağlantı, yanlış fiyat gibi hatalar öğrenmeden önemlidir; düzeltmeyi bekletme.
4. "Son önemli düzenleme" sütununu rapor ön ayarına ekle [RESMİ].
5. Yayındaki reklamın kreatifini düzenleme, yeni reklam ekle. Eskisinin verisi ve etkileşimi korunur (*sezgisel*).
6. **"Öğrenmeyle sınırlı" ceza değildir.** Çözüm sırası: setleri birleştir → kitleyi genişlet → bütçeyi artır → hedefi gevşet → daha sık gerçekleşen olaya in (ör. alışveriş → sepete ekleme) [RESMİ].
7. **Eşik:** Bazı 2026 blogları Advantage+ satışta "haftada 25" diyor; Meta'nın Ekim 2026 sayfaları ~50/hafta diyor. Bu modül 50'yi kullanır.

---

## 8. Kırılım etkisi (breakdown effect) tuzağı

**Mekanizma [RESMİ]:** Sistem kampanyanın **toplam** sonucunu en üst düzeye çıkarmaya çalışır. Bir öğenin ucuz fırsatları bittikçe sıradaki sonucun ücreti hızla artar. Sistem bunu önceden görür ve bütçeyi, ortalaması yüksek görünse de **marjinal** ücreti düşük kalan öğeye kaydırır. Raporda bu "pahalıya çok, ucuza az para" gibi görünür.

**Meta'nın örneği [RESMİ]:** Kampanya bütçeli bir kampanya 12 olayı ortalama 2,50 TL'den alıyor. Ortalaması en yüksek görünen A seti (5 TL/olay) kapatılınca kampanya 11 olaya düşüyor, ortalama 2,73 TL'ye çıkıyor.

**Doldurulmuş örnek (*varsayım*):** Set bütçesi 1.000 TL/gün. Reklam A bütçenin %80'ini alıyor, 7 günlük CPA'sı 260 TL. Reklam B %20 alıyor, CPA'sı 180 TL. Set CPA'sı ≈ 240 TL, hedef 250 TL.
- **Yanlış:** "A pahalı, kapat." B ucuz çünkü az harcıyor; harcaması 5 katına çıkınca CPA'sı aynı kalmaz.
- **Doğru:** Set hedefte, hiçbir şeyi kapatma. B'yi sınamak istiyorsan kreatif testi aracıyla eşit bütçede dene ya da "yayını bu reklama it"i birkaç gün kullan (varsa).

**Kapatma öncesi kontrol listesi** (*sezgisel*, Meta'nın değerlendirme ilkelerine dayanır):
1. Karar düzeyi doğru mu? Toplam (kampanya ya da set) hedefteyse alt öğe kapatılmaz.
2. Veri en az 7 günlük mü? Saatlik ya da günlük kırılımla karar verilmez.
3. Öğe bütçenin çoğunu mu alıyor? Ortalamaya değil, gün gün CPA eğilimine bak.
4. "Ucuz" öğenin envanteri küçük mü (yeniden hedefleme kitlesi, Threads gibi küçük alan)? Küçükse ölçeklenmez [PRATİSYEN Loomer].
5. 48–72 saatlik dönüşüm gecikmesi geçti mi?
6. Toplam kötüyse önce yapıyı sadeleştir ve yeni kreatif ekle. Tek öğe kapatılacaksa §5.2'yi uygula.

**Aynı tuzağın diğer biçimleri:** Hikayeler 4× ROAS, Akış 3× gösteriyor diye yalnız Hikayeler'e geçmek [PRATİSYEN Flighted]; yaş, cinsiyet ya da saat dilimi kapatmak; yeniden hedefleme seti ucuz diye bütçeyi oraya yığmak.

---

## 9. Sezon dönemlerinde strateji

### 9.1 Takvim (Ekim 2026 – Mayıs 2027)

| Olay | Tarih | En geç yayın (*sezgisel*) | Kreatif dondurma |
|---|---|---|---|
| 11.11 | 11 Kasım 2026, çarşamba | 21–28 Ekim | ~28 Ekim |
| Kara Cuma / Efsane Cuma | 27 Kasım 2026; Siber Pazartesi 30 Kasım. Türkiye'de kampanyalar Kasım boyunca sürüyor [HABER] | 6–13 Kasım | ~13 Kasım |
| Ramazan | 8 Şubat – 8 Mart 2027 [HABER, Diyanet'e atıfla] | 18–25 Ocak 2027 | ~25 Ocak |
| Ramazan Bayramı | 9–11 Mart 2027 [HABER] | — | — |
| Anneler Günü + Kurban Bayramı | 9 Mayıs + 16–19 Mayıs 2027 [HABER]; iki dönem üst üste | 18–25 Nisan | ~25 Nisan |

- **En geç yayın** = zirveden 2–3 hafta önce. Meta kampanyaları erken açmayı öneriyor ki öğrenme zirveden önce bitsin; gün sayısı vermiyor [RESMİ]. 2–3 hafta *sezgisel*.
- **Kreatif dondurma** = zirveden ~2 hafta önce yeni kavram denemeyi bırak; sonra yalnız kanıtlanmış reklamların sezon sürümleri [PRATİSYEN Darkroom].
- Türkiye'de giyim aramaları Ramazan'dan ~2 hafta önce zirve yaptı (Microsoft Advertising, Bing arama verisi; Meta verisi değil).

### 9.2 Evreler (T = zirve günü; *sezgisel* sentez: Darkroom ve Meta 2026 tatil rehberi)

| Evre | Zaman | Yap |
|---|---|---|
| Kurulum | T − 8…6 hafta | Yapıyı kilitle, ölçümü kontrol et, kitle listelerini güncelle |
| Öğren ve test et | T − 6…3 hafta | Kreatif testleri; indirim kararı ~T − 3 hafta. Ekim, Q4'te yeni kitleye ulaşmanın görece ucuz kısmı [PRATİSYEN] |
| Dondurma | T − 2 hafta | Yeni kavram yok; sezon sürümleri ve bütçe planı hazır |
| Zirve | T − 7 gün … T + 3 gün | Plana göre bütçe artışı; günlük kontrol önceden yazılmış sınırlarla |
| Çıkış | T + 3 … 14 gün | Bütçeyi kademeli düşür. Aralık sonu–ocak başı görece ucuz; yeni müşteri için kullan [PRATİSYEN] |

### 9.3 Sezon içi kurallar

- **BGBM artışı beklenir, CPA her zaman kötüleşmez.** ABD ağırlıklı bir veri setinde Kara Cuma haftası BGBM'si eylülün %59, Kara Cuma günü %84 üstündeydi; tıklama başına dönüşüm oranı +%48, satın alma başı maliyet −%20 oldu [PRATİSYEN Lebesgue]. Türkiye'ye özel sezon BGBM verisi bulunamadı. Hesabın geçen yılki aynı haftasını taban al.
- **"BGBM neden arttı?"** sorusunu **Açık Artırma Rekabeti Değişimi** ölçümüyle cevapla: günün rekabetçi teklifini önceki 3 günün ortalamasıyla kıyaslar; %20'nin üstü anlamlı değişimdir [RESMİ].
- **Yıllık kıyas tuzağı:** Meta tıklama ilişkilendirmesini 3 Mart 2026'da değiştirdi [RESMİ]. Kasım 2025 tamamen, Ramazan 2026 (≈19 Şubat–19 Mart) kısmen eski tanımla ölçüldü. Geçen yılla kıyası sipariş paneliyle yap (ölçüm kırılma tarihleri: Modül 05 §5.3).
- **Bütçe artışını önceden planla.** Bütçe planlama (yüksek talep dönemleri) yalnız günlük bütçede çalışır; öğe başına en fazla 50 dönem, dönem en az 3 saat, dönem toplamı günlük bütçenin 8 katını aşamaz [RESMİ, geliştirici belgesi]. Arayüzde görünürlüğü ve öğrenmeye etkisi *doğrulanmalı*. Yoksa artışı zirveden 3–5 gün önce §6.2 adımlarıyla başlat (*sezgisel*).
- **Zirvede 7 gün bekleyemezsin.** Sınırları önceden yaz. Örnek: "Günlük panel ROAS iki gün üst üste başabaşın altındaysa bir önceki bütçeye dön." Zirvede reklamı yalnız Aşama 0–1 sebepleriyle kapat (*sezgisel*).
- **Süreli setler:** Başlangıç ve bitiş tarihi önceden girilmiş "kampanya dönemi" setleri kur [PRATİSYEN CTC]. 7 günden kısa sette öğrenmenin bitmesini bekleme.
- **Ramazan:** İftar sonrasına saat kısıtı koyma; saat planı bütçenin harcanma biçimini değiştirir [RESMİ]. O saatlere uygun kreatif ve teklif kullan. Meta'nın 2022 Türkiye araştırmasına göre kullanıcılar Ramazan'da önce bilgilendirici içerik, sonra kampanya bekliyor [HABER; eski veri].
- **Sezon sonrası:** Toplam bütçe düşürülürken yeni tutar "şimdiye kadar harcanan + son 2 günün harcamasının %10'u"ndan az olamaz [RESMİ]. Tek seferde değil kademeli in (*sezgisel*).

**Doldurulmuş örnek: 19 Ekim 2026'da yayına giren e-ticaret hesabı (*varsayım*; gün 0 = 19 Ekim):**
- **19–25 Ekim (gün 0–6):** §4.1 planı; 11.11 sürümleri hazırlanır.
- **26 Ekim (gün 7):** 11.11 partisi aynı sete eklenir, çünkü dondurma 28 Ekim. Bütçeye dokunulmaz: §6.1 en az 2 hafta istiyor. 28 Ekim öğleden sonra ve 29 Ekim resmî tatil; o günlerin verisini not düşerek yorumla.
- **2 Kasım (gün 14):** Kapı açıksa ilk bütçe adımı (§6.2). Hedefli teklife geçme: veri az ve sezon başlıyor.
- **11 Kasım:** Plana göre artış, sınırlar devrede. **13 Kasım:** Kara Cuma için kreatif dondurma.
- **20–30 Kasım:** İkinci zirve. 1 Aralık'tan itibaren kademeli düşüş.
- **Ocak 2027:** Yeni müşteri ve Ramazan hazırlığı; Ramazan reklamları 18–25 Ocak'ta yayında.

---

## 10. Değişiklik günlüğü

**Neden kendi günlüğün?** Reklam Yöneticisi'nde değişiklik geçmişi var, ama değişikliğin sebebini ve beklenen etkisini tutmaz. API'deki etkinlik kaydı kim, ne zaman, hangi öğe ve ne tür değişiklik bilgisini verir; varsayılan olarak bir haftalık veri döndürür [RESMİ]. Arayüzdeki geçmişin saklama süresi resmi kaynakta bulunamadı.

**Kurallar:**
1. Önce satırı yaz, sonra değiştir. Günlüğe girmeyen değişiklik yapılmaz.
2. Her satır bir sebebe bağlanır: `H-…` (hipotez), `HATA`, `SEZON`, `MUSTERI`, `DIS`.
3. "Önemli düzenleme mi?" sütunu §7'ye göre doldurulur.
4. Kontrol tarihi = değişiklik + 7 gün; bütçe adımında ayrıca 3–4. gün.
5. **Dış olaylar da yazılır:** fiyat, stok, site kesintisi, resmî tatil, rakip kampanyası, Meta değişikliği.
6. Aynı hafta iki değişiklik yapıldıysa etkileri ayrılamaz; bunu "Sonuç"ta belirt.
7. Haftalık raporun ilk bölümü günlüğün son 7 günüdür.

**Şablon (doldurulmuş, *varsayım*; adlar Modül 02 §10 standardında):**

| # | Tarih-saat | Kim | Öğe | Değişiklik (önce → sonra) | Önemli düz.? | Sebep / ID | Beklenen | Kontrol | Sonuç | Karar |
|---|---|---|---|---|---|---|---|---|---|---|
| 1 | 19.10.2026 10:00 | YZ | `2610_SAT_WEB_TUM_HAC-SB_Elbise_TR` | Yayın: 1.500 TL/gün, 4 reklam | — | Başlangıç | ~50 satış/hafta | 26.10 | 52 satış, CPA 202 | Devam |
| 2 | 26.10.2026 10:15 | YZ | `GENIS_25plus-MUSharic_PUR_APL_ana` | 3 reklam eklendi (11.11 sürümü) | Evet | SEZON-1111 | 11.11'de CPA ≤ hedef | 2.11 | — | — |
| 3 | 29.10.2026 | — | — | Resmî tatil | — | DIS | Trafik farklı | — | — | — |
| 4 | 2.11.2026 10:00 | YZ | aynı set | Bütçe 1.500 → 1.875 (+%25) | Büyüklüğe göre | Ölçek adımı 1 | Marjinal CPA ≤ 250 | 5.11 / 9.11 | Marjinal ≈ 239; 5. satırla aynı hafta, etkiler ayrılamaz | +%25 |
| 5 | 3.11.2026 09:00 | Müşteri | Site | Ücretsiz kargo eşiği 750 → 500 TL | Hayır (reklam dışı) | MUSTERI | Dönüşüm oranı ↑ | 10.11 | — | — |

**Hipotez defteri** ayrı sekmede: ID, hipotez cümlesi, araç, tarih aralığı, hücreler, sonuç, güven, karar, öğrenilen ders. Kaybeden testleri de yaz; aynı açıyı yeniden denememek için gerekir.

---

## 11. Haftalık kontrol listesi (pazartesi)

- [ ] Önceki tam hafta kampanya/set düzeyinde hedefe ve başabaşa göre okundu (§4.4).
- [ ] Son bütçe adımından beri marjinal CPA hesaplandı (F7).
- [ ] Yayın sütunu: öğrenme, öğrenmeyle sınırlı, kreatif sınırlandı ya da yorgunluğu.
- [ ] Kapatma adayları §5.2 ve §8 listesinden geçti; ölçekleme adayları §6.1 kapısından.
- [ ] Açık testlere erken müdahale edilmedi.
- [ ] Önümüzdeki 3 haftada sezon zirvesi var mı (§9.1)?
- [ ] Değişiklikler tek oturumda yapıldı ve günlüğe yazıldı.

---

## Doğrulanması gerekenler

- **Kreatif testi aracı:** Resmi yardım sayfası yok. 5/10 hücre sınırı, %20 bütçe önerisi, 7 gün varsayılan ve yalnız "En yüksek hacim" koşulu pratisyen gözlemi.
- **"Yayını bu reklama it":** Haziran 2026'da görülen test özelliği; hesaba göre değişebilir.
- **Bütçe planlama (yüksek talep dönemleri):** Yalnız API belgesi okundu. Arayüzde görünürlüğü ve öğrenmeye etkisi bilinmiyor.
- **Otomatik kural koşulu "Harcanan tutar":** Meta'nın koşul listesinde yok; arayüzde kontrol et.
- **Değişiklik geçmişi:** Arayüzdeki menü yolu ve saklama süresi resmi kaynakta bulunamadı.
- **Öğrenme eşiği:** "Haftada 25" iddiası doğrulanmadı; Meta sayfaları ~50 diyor.
- **Meta'ya atfedilen iki rakam** ("öğrenmeden çıkan sette %19 düşük CPA", "öğrenme harcamanın %20–25'i olmalı"): birincil kaynak bulunamadı; bu modülde karar kuralı olarak kullanılmadı.
- **2027 dini bayram tarihleri:** Diyanet'e atıf yapan haberlerden. Bir haber Ramazan başlangıcını 19 Şubat diyor; Diyanet takviminden teyit et.
- **Meta'nın 13 Ağustos 2026 dört evreli tatil rehberi:** yalnız ikincil kaynaktan okundu.
- **Konum ücreti başlangıç tarihi** (1 Temmuz / 1 Ekim 2026 çelişkisi): Kurulum Kılavuzu §3.6.

---

## Kaynaklar

**Meta yardım merkezi [RESMİ]** (erişim Ekim 2026):
- A/B testi hakkında: https://www.facebook.com/business/help/1738164643098669
- A/B testi türleri ve kurulum: https://www.facebook.com/business/help/1159714227408868
- A/B testi en iyi uygulamaları: https://www.facebook.com/business/help/290009911394576
- Kazananın belirlenmesi: https://www.facebook.com/business/help/166313650471318
- Güven ve güç eşikleri: https://www.facebook.com/business/help/239549606692303
- Deneyler sınırları: https://www.facebook.com/business/help/3506622486044209
- Birden fazla değişkenli A/B testi: https://www.facebook.com/business/help/455527338244675
- A/B testini düzenleme ya da iptal: https://www.facebook.com/business/help/286876155019610
- A/B testi sonuçları: https://www.facebook.com/business/help/1376548572415613
- Bütçe optimizasyonu testleri: https://www.facebook.com/business/help/299600627522144
- Dönüşüm testi için kreatif: https://www.facebook.com/business/help/565573477186194
- Öğrenme aşaması: https://www.facebook.com/business/help/112167992830700
- Önemli düzenlemeler: https://www.facebook.com/business/help/316478108955072
- Öğrenmeyle sınırlı: https://www.facebook.com/business/help/269269737396981
- Kampanya performansını yorumlama: https://www.facebook.com/business/help/1364841787225722
- Kırılım etkisi: https://www.facebook.com/business/help/770303663944673
- Advantage+ kampanya bütçesi raporları (sayısal örnek): https://www.facebook.com/business/help/258714594633281
- Advantage+ kampanya bütçesi en iyi uygulamaları: https://www.facebook.com/business/help/2177212182495139
- Sonuç başına ücret hedefi: https://www.facebook.com/business/help/176339196621566
- Minimum bütçe: https://www.facebook.com/business/help/203183363050448
- Bütçe değişikliğinin zamanlaması: https://www.facebook.com/business/help/196976447912140
- Hafta ortası bütçe değişikliği: https://www.facebook.com/business/help/525189242169568
- Açık artırma çakışması: https://www.facebook.com/business/help/537699989762051
- Kampanya çoğaltma: https://www.facebook.com/business/help/209669919072999
- Kreatif yorgunluğu: https://www.facebook.com/business/help/1346816142327858
- Reklam planlama: https://www.facebook.com/business/help/1037425549606837
- Açık Artırma Rekabeti Değişimi (EN): https://www.facebook.com/business/help/2024547657774300
- Otomatik kural oluşturma: https://www.facebook.com/business/help/644860419002064
- Otomatik kural eylemleri: https://www.facebook.com/business/help/192277077825914
- Otomatik kural sınırları: https://www.facebook.com/business/help/222640851458826
- Otomatik kural koşulları: https://www.facebook.com/business/help/975877422460996 ve https://www.facebook.com/business/help/679920066114741
- Ücret koşullu kurallar: https://www.facebook.com/business/help/511107233420528
- Toplam bütçe yüzdesi bildirimi: https://www.facebook.com/business/help/199955874257704
- Artımlı ilişkilendirme sonuçları: https://www.facebook.com/business/help/1271619100603114

**Meta, diğer [RESMİ]:**
- Kreatif testleri (split testing), Marketing API: https://developers.facebook.com/docs/marketing-api/guides/split-testing
- Yüksek talep dönemleri (bütçe planlama): https://developers.facebook.com/documentation/ads-commerce/marketing-api/reference/high-demand-period
- Bütçe planları: https://developers.facebook.com/documentation/ads-commerce/marketing-api/reference/ad-campaign/budget_schedules
- Reklam hesabı etkinlik kaydı: https://developers.facebook.com/docs/marketing-api/reference/ad-account/activities/ ve https://developers.facebook.com/docs/marketing-api/reference/ad-activity/
- 2026 tatil rehberi (11.06.2026): https://www.facebook.com/business/news/2026-winning-hearts-boosting-carts
- Tıklama ilişkilendirmesi değişikliği (03.03.2026): https://www.facebook.com/business/news/click-attribution
- Kreatif çeşitliliği ve Andromeda: https://www.facebook.com/business/news/the-creative-advantage-unlocking-the-power-of-diversification-with-meta-andromeda

**Pratisyen [PRATİSYEN]:**
- Jon Loomer, Meta Ads master brief (02.07.2026): https://www.jonloomer.com/meta-ads-master-brief/
- Jon Loomer, kırılım etkisi (19.05.2025): https://www.jonloomer.com/breakdown-effect/
- Jon Loomer, kreatif testi aracı (güncelleme 03.01.2026): https://www.jonloomer.com/meta-creative-testing/
- Jon Loomer, yayını bu reklama it (09.06.2026): https://www.jonloomer.com/hands-on-push-delivery-to-this-ad/
- Jon Loomer, kreatif testini sürece katmak (06.01.2026): https://pubcast.jonloomer.com/make-creative-testing-part-of-your-process/
- Jon Loomer, bütçe güncellemeleri (güncelleme 01.03.2025): https://www.jonloomer.com/updates-to-meta-ads-budgeting/
- Vaizle, öğrenme aşaması gözlemi (29.09.2025): https://insights.vaizle.com/news/meta-might-have-quietly-changed-how-the-learning-phase-works
- Vaizle, ölçekleme (12.03.2026): https://insights.vaizle.com/how-to-scale-facebook-ads/
- Common Thread Collective, Meta yöntemi: https://commonthreadco.com/pages/methodology-meta
- Foxwell Digital, test-doğrulama-ölçekleme: https://www.foxwelldigital.com/blog/the-performance-marketers-guide-to-testing-validating-and-scaling
- Foxwell Digital, Q3 2026 konuları: https://www.foxwelldigital.com/blog/the-9-most-pressing-topics-for-meta-advertisers-in-q3-2026
- Flighted, reklam ne zaman durdurulur: https://www.flighted.co/blog/how-to-know-when-to-pause-a-meta-ad
- Flighted, hesap yönetimi: https://www.flighted.co/blog/facebook-ads-management-best-practices
- AdManage, reklam kapatma çerçevesi (21.01.2026): https://admanage.ai/blog/when-to-kill-a-facebook-ad
- Heath Media, %20 kuralı (22.01.2021): https://heathmedia.co.uk/?p=544687
- Bind Media, kırılım etkisi (15.03.2024): https://bind.media/insights/every-meta-advertiser-should-know-what-this-is-spoiler-they-dont
- Darkroom, BFCM planı (02.10.2026): https://www.darkroomagency.com/observatory/the-bfcm-paid-media-plan
- Lebesgue, Kara Cuma maliyetleri (16.10.2026): https://lebesgue.io/facebook-ads/are-meta-ads-worth-it-black-friday
- Biddy, Kara Cuma BGBM'si (05.10.2026): https://www.biddyco.com/blog-posts/meta-ads-black-friday-cpm
- Bïrch, Meta reklam maliyetleri 3 yıllık analiz (16.04.2024): https://bir.ch/blog/meta-ad-costs-analysis
- Microsoft Advertising, Ramazan 2026 verisi (25.02.2026): https://about.ads.microsoft.com/en/blog/post/february-2026/decoding-ramadan-2026-data-driven-strategies-for-the-middle-east-turkiye-and-africa

**Haber [HABER]:**
- Relevant Audience, Meta'nın dört evreli Q4 rehberi (17.08.2026): https://www.relevantaudience.com/meta/meta-holiday-playbook-four-phases-q4/
- Evrensel, Diyanet 2027 takvimi (24.08.2026): https://www.evrensel.net/haber/5997565/diyanet-2027-takvimini-duyurdu-ramazan-ne-zaman-basliyor
- Dünya, 2027 bayram tarihleri (05.10.2026): https://www.dunya.com/gundem/2027-bayram-tarihleri-kurban-bayrami-ramazan-bayrami-ne-zaman-haberi-842648
- Marketing Türkiye, Kasım indirim dönemi (02.12.2024): https://www.marketingturkiye.com.tr/?p=168603
- Habertürk, Meta Türkiye Ramazan araştırması (13.04.2022): https://www.haberturk.com/meta-turk-tuketicilerinin-ramazan-ayi-aliskanliklarini-arastirdi-3407505-teknoloji
