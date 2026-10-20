# Metrikler, kıyas değerleri ve teşhis tablosu

> **Modül 07 · Profesyonel paket · Hazırlanma: 17 Ekim 2026.** Ekran adımları, yayın durumları, ödeme ve hata kodları burada yok. Onlar için **Kurulum Kılavuzu** (`../KILAVUZ.md`): bütçe esnekliği §5.2, yayın durumları §6.1, ilk 7 gün §6.3, toplam maliyet §9, sorun giderme §10. Bu modül şunları hazır kabul eder: başabaş CPA/ROAS, panel çarpanı, N ve marjinal CPA formülleri (Modül 02 F1–F7), K/P/V mutabakat oranları ve alarm tablosu (Modül 05 §5.5, §10.2), kapatma ve ölçekleme kuralları (Modül 06 §5–6), yorgunluk tablosu (Modül 04 §9), reklamın çözemeyeceği sorunlar (Modül 01 §6), indirim, KVKK ve İYS kapıları (Modül 09 §5.3, §5.6, §5.7).

## Bu modülde

- Reklam Yöneticisi metriklerinin **Meta tanımı ve formülü**, bunlardan türetilen oranlar ve tuzakları.
- Kopyalanabilir **sütun ön ayarları** (satış, potansiyel müşteri, mesaj, kreatif, sezon) ve kurulacak **özel ölçümler**.
- **Metrik zinciri:** CPA'yı CPM, CTR ve dönüşüm oranına ayıran formül, "hangi halka bozdu?" hesabı, gürültü kuralı ve dönüşüm gecikmesi (veri yaşı) kuralı.
- 35 satırlık **"bu rakamı görürsen → muhtemel neden → yapılacak"** tablosu (T01–T35).
- Yalnız kaynaklı, tarihli ve aralık hâlinde **kıyas değerleri** ile doldurulmuş örnekli **"kendi tabanını çıkar"** yöntemi.

**Etiketler:** **[RESMİ]** Meta'nın kendi sayfası. **[PRATİSYEN]** ajans, danışman ya da araç sağlayıcı (çıkar çatışması olabilir). **[KAMU]** TCMB, TÜİK. **[TOPLULUK]** kullanıcı forumu. **[MEVZUAT] atfı** = hukuk kuralı ve kaynağı Modül 09'da. *sezgisel* = ölçülmemiş pratisyen kuralı. *türetilmiş* = bu modülde yapılan aritmetik. *varsayım* = örnek rakam.

---

## 0. Okuma kuralları (her teşhisten önce)

1. **Önce ölçüm.** Modül 05 §10.2 alarm tablosunda herhangi bir satır açıksa teşhis yapma, önce ölçümü düzelt. Asıl liste oradadır, aşağıdaki özet eksiksiz değildir:
   - K > 1,05 (çift sayım şüphesi).
   - K < 0,70 ve K, rıza oranı R'nin belirgin altında ya da R bilinmiyor. Olay kaybı şüphesidir: CAPI kopması, jetonun süresinin dolması, izin listesi.
   - P > 1 iki hafta üst üste ya da 28 günlük toplamda. Tek haftalık P > 1 alarm değildir.
   - Ks, R'nin belirgin üstünde (sunucu olayları rızaya uymuyor olabilir).
   - V, ±%5 bandının ya da hesabın kendi V tabanının dışında.
   - Bir olay 24 saattir sıfır ama sipariş sürüyor.
   
   Olay kaybı CPA'yı sahte biçimde yükseltir. Bunu CPM, CTR ya da CVR sorunu diye teşhis etme.
2. **Pencere ve düzey.** En az 7 tam gün kullan. Kampanya bütçesinde kampanya, reklam seti bütçesinde set düzeyinde oku. Saat ya da alan gibi dar kırılımlar kısa vadede salınır [RESMİ].
3. **Optimize edilen olaya bak.** Dönüşüme optimize eden sette CPM iyi bir performans göstergesi olmayabilir. Sistem daha ucuz dönüşüm için bilerek pahalı gösterim alabilir [RESMİ].
4. **"Yüksek" ve "düşük" hesabın kendi tabanına göredir** (§6). Dış kıyas (§5) yalnız "bu rakam makul mü?" sorusu içindir. Dış kıyasa bakıp kapatma ya da ölçekleme kararı verme.
5. **Panel faturayla aynı değil.** Maliyet çarpanı **c** = 1,05 (Türkiye konum ücreti, vergi numarası girilmiş) ya da **1,26** (vergi numarası girilmemiş) (Modül 02 F3). Gerçek maliyet = panel × c. Konum ücreti Reklam Yöneticisi'nde görünmez [RESMİ].
   - **Panel CPA yalnız panel başabaşıyla** (gerçek başabaş ÷ c), **gerçek CPA (panel × c) yalnız gerçek başabaşla** kıyaslanır. İkisini karıştırma. Panel başabaşını gerçek CPA ile kıyaslarsan düzeltmeyi iki kez uygulamış olursun. ROAS için aynı kural: panel ROAS ↔ panel başabaş ROAS, gerçek ROAS (panel ÷ c) ↔ gerçek başabaş ROAS.
6. **Kırılma tarihini karıştırma.** 3 Mart 2026'dan beri site dönüşümlerinde "tıklama" yalnız bağlantı tıklaması demek. Öncesini ve sonrasını doğrudan karşılaştırma [RESMİ].
7. **Dönüşüm metrikleri geç olgunlaşır (veri yaşı kuralı).** Reklam Yöneticisi dönüşümü, dönüşümün olduğu güne değil reklamın gösterildiği güne yazar [PRATİSYEN Loomer]. Meta da Reklam Raporları'nda gösterim zamanına ya da dönüşüm zamanına göre raporlama seçilebildiğini yazıyor [RESMİ]; seçeneğin yeri hesaba göre değişebilir. Varsayılan ilişkilendirme 7 gün tıklama + 1 gün görüntüleme + 1 gün etkileşimdir [RESMİ]. Bu yüzden bir günün dönüşümleri sonraki 7 güne kadar o güne eklenmeye devam eder. En yeni haftanın CVR'ı ve ROAS'ı düşük, CPA'sı yüksek görünür (*türetilmiş*).
   - **Veri yaşı** = okuma günü − dönemin son günü (gün). Her kıyas satırına yaz.
   - **Gecikmeden etkilenmeyenler.** Harcama, gösterim, erişim, sıklık, CPM, CTR, CPC ve YSG oranı. Bunları az önce biten haftayla oku.
   - **Gecikmeli olanlar.** CVR, CPA, ROAS, konuşma → satış, nitelikli lead oranı. Bunlar için iki yoldan birini seç:
     - (a) Veri yaşı en az 7 gün olan haftayı oku (*türetilmiş*, 7 günlük tıklama penceresinden). Pazartesi rutininde bu, az önce biten haftadan bir önceki haftadır (veri yaşı 9 gün).
     - (b) Az önce biten haftayı, tabanın **aynı yaşta** alınmış anlık görüntüleriyle kıyasla (§6.1 adım 8).
   - Olgun haftayla genç haftayı ya da farklı yaştaki iki değeri asla doğrudan kıyaslama. Çevrimdışı ve CRM olaylarında olgunlaşma daha uzun sürebilir (Modül 05 §2.8).
   - 48–72 saatten genç veriyle performans kararı verme [PRATİSYEN AdManage].
8. **Hafta tanımını sabitle.** Bu pakette raporlama haftası **pazar–cumartesi**dir (Modül 10 §1).
   - Gerekçe bütçe temposuyla hizalamaktır: Meta günlük bütçenin haftalık harcama tavanını pazar–cumartesi haftasına göre hesaplar [RESMİ]. Bu kural bütçe temposu içindir, Meta'nın bir raporlama kuralı değildir.
   - "Geçen hafta" tek bir tanım değildir. API'de pazartesi–pazar ve pazar–cumartesi iki ayrı hazır aralıktır: `last_week_mon_sun` ve `last_week_sun_sat` [RESMİ geliştirici belgesi]. Reklam Yöneticisi'nin tarih seçicisinde hangisinin sunulduğu ve adı hesaba ve dile göre değişebilir (doğrulanmalı). Emin değilsen özel tarih aralığı gir.
   - Taban haftaları ve bu hafta aynı tanımla çekilir.

---

## 1. Metrik sözlüğü

Kısaltmalar: CPM = BGBM, CTR = TO, CPC = TBM. Öteki modüllerde Türkçe kısaltmalar da geçer. Arayüz adları Türkçe Reklam Yöneticisi'ndeki gibidir, hesaba göre küçük farklar olabilir.

### 1.1 Meta'nın tanımladığı metrikler [RESMİ]

| Metrik | Formül / tanım | Karar için not |
|---|---|---|
| Harcanan tutar | Panelde görünen harcama | Konum ücreti ve KDV hariç. Gerçek maliyet için F3 |
| Gösterimler | Reklamın ekranda ilk görünme sayısı. Aynı kişi günün iki ayrı zamanında görürse 2 sayılır. Video için oynatma şartı yok, bot trafiği sayılmaz | İzlenme sayısı değildir |
| Erişim | Reklamı en az bir kez gören Meta Hesabı sayısı. Tahminidir, örneklenmiş veriyle hesaplanır | Set erişimleri toplanmaz: A,B'ye ve B,C'ye erişen iki setin kampanya erişimi 3'tür |
| Sıklık | Gösterim ÷ Erişim (tahmini) | Meta'ya göre sıklık artarken performans düşüyorsa yorgunluk olabilir |
| CPM (1000 gösterim başına ücret) | Harcama ÷ Gösterim × 1.000. Örnek: 50 TL ve 10.000 gösterim → 5 TL | Açık artırma fiyatı, kitle ve kalitenin karışımı |
| Erişilen 1.000 Meta Hesabı başına ücret | Harcama ÷ Erişim × 1.000 | Sıklık arttıkça CPM'den hızlı yükselir |
| Bağlantı tıklamaları | Reklamverenin belirlediği hedefe (site, form, Hızlı Deneyim, uygulama mağazası) götüren bağlantılara tıklama | Hızlı formun açılması da sayılır |
| Tıklamalar (tümü) | Bağlantı tıklaması + beğeni, yorum, paylaşım, profil adına tıklama, görseli büyütme | Her zaman ≥ bağlantı tıklaması. Teşhiste kullanma |
| Dışarı yönlendiren tıklamalar | Meta dışına (siteye) çıkaran tıklamalar | Hızlı Deneyim ve koleksiyonda bunu oku |
| CTR (bağlantı tıklama oranı) | Bağlantı tıklaması ÷ Gösterim | Kreatifin ilgi testi |
| CPC (bağlantı tıklaması başına ücret) | Harcama ÷ Bağlantı tıklaması | = CPM ÷ (1.000 × CTR) (*türetilmiş*) |
| Yönlendirme sayfası görüntülemeleri (YSG) | Tıklamadan sonra sayfanın ya da uygulamanın başarıyla yüklenme sayısı. Piksel ya da uygulama içi tarayıcıyla sayılır, bazen modellenir | Meta bağlantı tıklamasıyla karşılaştırmayı öneriyor |
| Sonuçlar | Amaca ve ayarlara göre sonuç sayısı. Eksik veride modelleme olabilir | Faturalama birimini yansıtmaz |
| Sonuç başına ücret | Harcama ÷ Sonuç | Birincil karar metriği |
| Sonuç oranı | Sonuç ÷ Gösterim | Gösterimden sonuca toplam verim |
| ThruPlay | Video sonuna kadar ya da en az 15 saniye oynatıldı (kısa videoda %97) | Tutma oranının payı |
| Kalite / Etkileşim oranı / Dönüşüm oranı sıralaması | Aynı kitle için yarışan reklamlara göre sıra. 500 gösterim altında çıkmaz, açık artırmada girdi değildir | Zayıf reklamı bulmak içindir |
| Açık Artırma Rekabetindeki Değişim | O günün rekabetçi teklifi ile önceki 3 günün ortalaması arasındaki fark. Meta %20 üstünü önemli sayıyor | Günlük kırılımda oku. Yalnız ani sıçramayı yakalar. Günden güne yavaş artan sezon rekabeti tek günde nadiren %20'yi aşar, bu yüzden metrik onu kaçırır (*türetilmiş*). Sezon kararı §6.4'teki katsayıyla verilir, bu metrik yalnız destekleyici kanıttır (T09) |
| Öğrenmede harcanan bütçe payı | Hesaba Genel Bakış'ta son 7 gün için gösterilir | Sürekli yüksekse yapı çok parçalı (T07) |

**Kaldırılan tekil ölçümler:** Meta 30 Ekim 2024'te tekil sepete ekleme, tekil alışveriş, tekil YSG gibi tekil dönüşüm ölçümlerini ve bunların ücret sütunlarını kaldırdı. Erişim ve tekil bağlantı tıklaması duruyor [RESMİ]. Eski rapor şablonlarında bu sütunlar boş gelir.

### 1.2 Türetilmiş oranlar (*türetilmiş*; hazır sütun yok, §2.2'deki özel ölçümle kurulur)

| Oran | Formül | Ne söyler |
|---|---|---|
| YSG oranı | YSG ÷ Bağlantı tıklaması | Sayfa yükleniyor mu? |
| CVR (zincir CVR'ı) | Sonuç ÷ Bağlantı tıklaması. Bu modülde "CVR" yalnız bu tanımdır ve zincirde (§3.1) yalnız bu kullanılır | Tıklamadan sonuca toplam verim. Pay, ilişkilendirme ayarındaki bütün sonuçlardır. 1 gün görüntüleme ve 1 gün etkileşim dönüşümleri de içindedir [RESMİ Sonuçlar], yani hepsi tıklamadan gelmez. Kreatif karışımı değişince sayfa aynı kalsa da oynar. Gecikmelidir (§0 madde 7) |
| Sayfa CVR'ı | Sonuç ÷ YSG | Sayfanın kendi dönüşüm gücü. **Zincirde kullanma.** Kullanılırsa CPA, YSG oranı kadar (ör. ~%20) düşük hesaplanır |
| Görüntüleme payı | 1 gün görüntüleme sonucu ÷ toplam sonuç ("İlişkilendirme ayarlarını karşılaştır" ekranından) | Modül 05 §5.2. CVR değişimini sayfaya bağlamadan önce bu payın sabit kaldığını gör |
| Sepet oranı | Sepete ekleme ÷ YSG | Ürün sayfası ikna ediyor mu? |
| Ödeme başlatma oranı | Başlatılan alışveriş ÷ Sepete ekleme | Kargo ya da fiyat sürprizi |
| Tamamlama oranı | Alışveriş ÷ Başlatılan alışveriş | Ödeme adımı ve ödeme yöntemleri |
| Alışveriş ROAS | Alışveriş dönüşüm değeri ÷ Harcama | Değer pikselin gönderdiğidir. KDV dahil mi? (Modül 05 V oranı) |
| Gerçek CPA / ROAS | Panel CPA × c · Panel ROAS ÷ c (c = 1,05 ya da 1,26; §0 madde 5) | Yalnız **gerçek** başabaşla karşılaştırılır. Panel CPA ise yalnız panel başabaşıyla |
| Kanca oranı | 3 saniyelik video oynatma ÷ Gösterim | İlk 3 saniye durduruyor mu? [PRATİSYEN Motion] |
| Tutma oranı | ThruPlay ÷ 3 saniyelik oynatma | Gövde vaadi karşılıyor mu? Tanım kaynaklara göre değişiyor [PRATİSYEN Motion] |
| Konuşma → satış | Etiketli ya da tabloya yazılmış satış ÷ Başlatılan mesajlaşma konuşması | Mesaj kampanyasının gerçek verimi (Modül 05 §7) |
| Ulaşılma oranı | Telefonla ulaşılan form ÷ Toplam form (CRM'den) | Form kalitesi ve satış ekibi hızı |
| MER | KDV hariç toplam ciro ÷ gerçek reklam maliyeti | Panelden bağımsız sağlık kontrolü (Modül 05 §5.5) |
| Marjinal CPA | Δ harcama ÷ Δ sonuç | Ölçekleme kararı (Modül 02 F7). Δ sonuç < 2 × √(eski sonuç + yeni sonuç) ise "belirsiz" say ve karar verme (§3.3, T24) |

**Sayfa ve teklif teşhisi (T18, T19; *türetilmiş*):** Sorunun sayfada olduğuna karar vermeden önce CVR'ı "İlişkilendirme ayarlarını karşılaştır" ekranındaki 7 gün tıklama sütunuyla yeniden hesapla ve görüntüleme payına bak. Görüntüleme payı değiştiyse CVR'daki hareket sayfadan değil kreatif ya da alan karışımından gelebilir.

---

## 2. Sütun ön ayarı önerisi

### 2.1 Kaydetme [RESMİ]

Kampanyalar, Reklam setleri ya da Reklamlar sekmesi → **Sütunlar** → **Sütunları özelleştir** → metrikleri seç → **"Sütun ön ayarı olarak kaydet"** kutusunu işaretle → **Uygula**. Kayıtlı ön ayar daha sonra Sütunlar menüsünden seçilir. Metrik kategorileri: Performans, Etkileşim, Dönüşümler, Ayarlar.

### 2.2 Özel ölçümler [PRATİSYEN Loomer, güncelleme 1 Mart 2025; *doğrulanmalı*]

Sütunları özelleştir ekranında, arama kutusunun yanındaki özel ölçüm oluşturma bağlantısı kullanılır. Ad en fazla 100 karakterdir. Biçim sayı, yüzde ya da para birimidir. Formül mevcut metriklerden kurulur. Paylaşım "yalnız ben" ya da "işletmedeki herkes" olabilir. Ölçüm Reklam Raporları'nda da görünür.

| Ad | Formül | Biçim |
|---|---|---|
| `OM_YSG_ORANI` | Yönlendirme sayfası görüntülemeleri ÷ Bağlantı tıklamaları | Yüzde |
| `OM_CVR_TIK` | Alışveriş (ya da Potansiyel müşteri) ÷ Bağlantı tıklamaları | Yüzde. Zincir CVR'ı (§1.2) |
| `OM_SAYFA_CVR` | Alışveriş ÷ Yönlendirme sayfası görüntülemeleri | Yüzde. Yalnız sayfa teşhisi için, zincirde kullanma |
| `OM_SEPET_ODEME` | Başlatılan alışveriş ÷ Sepete ekleme | Yüzde |
| `OM_ODEME_ALIS` | Alışveriş ÷ Başlatılan alışveriş | Yüzde |
| `OM_KANCA` | 3 saniyelik video oynatma ÷ Gösterim | Yüzde |
| `OM_TUTMA` | ThruPlay ÷ 3 saniyelik video oynatma | Yüzde |
| `OM_GERCEK_CPA` | Harcanan tutar × c ÷ Alışveriş. c = 1,05 (vergi numarası var) ya da 1,26 (yok); §0 madde 5 | Para birimi. Hesabın c değerini ölçüm adına yaz (ör. `OM_GERCEK_CPA_105`). Formül sabit sayı kabul etmiyorsa bu hesabı dışa aktarılan tabloda yap. Sonucu yalnız gerçek başabaşla kıyasla |

### 2.3 Ön ayarlar (sütun sırası = metrik zinciri sırası, *sezgisel*)

| Ön ayar | Sütunlar (soldan sağa) |
|---|---|
| `07-SATIS` | Yayın · Son önemli düzenleme · Bütçe · Teklif stratejisi · İlişkilendirme ayarı · Harcanan tutar · Gösterimler · Erişim · Sıklık · CPM · Bağlantı tıklamaları · CTR (bağlantı) · CPC (bağlantı) · YSG · OM_YSG_ORANI · Sepete ekleme · Başlatılan alışveriş · Alışveriş · OM_CVR_TIK · OM_SAYFA_CVR · Alışveriş başına ücret · Alışveriş dönüşüm değeri · Alışveriş ROAS |
| `07-LEAD` | Yayın · Son önemli düzenleme · Bütçe · Harcanan tutar · Gösterimler · Sıklık · CPM · Bağlantı tıklamaları · CTR (bağlantı) · Potansiyel müşteriler · Potansiyel müşteri başına ücret · OM_CVR_TIK · (CRM bağlıysa) nitelikli potansiyel müşteri ve başına ücret |
| `07-MESAJ` | Yayın · Harcanan tutar · Gösterimler · Sıklık · CPM · Bağlantı tıklamaları · CTR (bağlantı) · Başlatılan mesajlaşma konuşmaları · Konuşma başına ücret · (paylaşım açıksa) mesajlaşma yoluyla alışveriş ya da potansiyel müşteri |
| `07-KREATIF` (reklam düzeyi) | Yayın · Harcanan tutar · Gösterimler · CPM · 3 saniyelik oynatma · OM_KANCA · ThruPlay · OM_TUTMA · CTR (bağlantı) · Sonuçlar · Sonuç başına ücret · Kalite, Etkileşim oranı ve Dönüşüm oranı sıralaması |
| `07-SEZON` (Zamana göre → Gün kırılımıyla) | Harcanan tutar · CPM · Açık Artırma Rekabetindeki Değişim · CTR (bağlantı) · Sonuç oranı · Sonuç başına ücret |

Kırılımları (Yayına göre, Zamana göre) yalnız 7 gün ya da daha uzun pencerede aç [RESMİ]. İlişkilendirme kıyası için Sütunlar → "İlişkilendirme modellerini karşılaştır" kullan. Farklı modeldeki setleri aynı tabloda yarıştırma (Modül 05 §5).

---

## 3. Metrik zinciri teşhisi

### 3.1 Zincir denklemleri (*türetilmiş*, tanım gereği)

```
CPC  = CPM ÷ (1.000 × CTR)
CPA  = CPC ÷ CVR = CPM ÷ (1.000 × CTR × CVR)
ROAS = Ortalama sepet × CVR ÷ CPC = Ortalama sepet × 1.000 × CTR × CVR ÷ CPM
CVR  = YSG oranı × Sepet oranı × Ödeme başlatma oranı × Tamamlama oranı   (= Alışveriş ÷ Bağlantı tıklaması)
Sayfa CVR = Sepet oranı × Ödeme başlatma oranı × Tamamlama oranı        (= Alışveriş ÷ YSG; zincire girmez)

CPA değişimi = (yeni CPM ÷ taban CPM) × (taban CTR ÷ yeni CTR) × (taban CVR ÷ yeni CVR)
```

Burada CVR her zaman zincir CVR'ıdır (Sonuç ÷ Bağlantı tıklaması, §1.2). Denklemler, CPA'daki ve CVR'daki sonuç aynı sütundan ve aynı ilişkilendirme ayarından geldiğinde tam tutar. Gecikmeli halkalar (CVR, CPA) için olgun veri ya da aynı yaştaki taban kullanılır (§0 madde 7).

**Çarpan okuma:** Çarpan 1'in üstündeyse o halka CPA'yı kötüleştirmiştir. 1'in altındaysa iyileşmiştir ve başka bir halkanın kaybını kısmen kapatıyordur. Hangi halkadan başlanacağını §3.2'deki tek kural belirler.

### 3.2 Okuma sırası ve başlama kuralı

**Bakılacak halkalar:** Yayın ve harcama → CPM → CTR (bağlantı) → YSG oranı → site hunisi (sepet, ödeme, tamamlama) → CVR → ortalama sepet ve ROAS → arka uç (kasa, iade, ulaşılma). Hepsini oku, ilk bozulanda durma.

**Tek karar kuralı (*türetilmiş*; eşikler *sezgisel*):**
1. Bant dışında **ve** gürültü payının üstünde olan bütün halkaları listele (§3.3, §6.1). CPM ve sıklıkta gürültü payı yerine ek eşik kullanılır (§6.1 adım 6).
2. Her halkanın logaritmik payını hesapla: pay = ln(çarpan) ÷ ln(çarpanların çarpımı). Çarpım 1'e çok yakınsa halkaları |ln(çarpan)| büyüklüğüne göre sırala.
3. En büyük paylı halkadan başla ve §4'teki satırına git.
4. **İstisna:** CPM yüksek ve CTR düşük birlikte listedeyse ve CPM artışı hesap geneli bir sezon ya da rekabet etkisi değilse (T09 değilse), ikisini tek bir kreatif sorunu say (T10). Açık artırmada tahmini eylem oranı toplam değerin parçasıdır [RESMİ açık artırma]. Düşük CTR bu oranı düşürüp aynı gösterimi pahalılaştırabilir (*türetilmiş*).
5. Etki iki yönlüdür: sonraki bir halka öncekini, önceki bir halka sonrakini bozabilir. Bir halkadaki bozulmayı başka bir halkanın "sonucu" diye otomatik olarak eleme.

### 3.3 Gürültü kuralı (*türetilmiş*, Poisson yaklaşımı, kaba)

Bir haftada k olay varsa bu sayı kendi kendine yaklaşık **±2 ÷ √k** oynar (~%95).
- 25 dönüşüm → ±%40. 100 dönüşüm → ±%20. 1.000 tıklama → ±%6.
- Haftada 25 dönüşüm alan sette CPA'nın %30 kötüleşmesi tek başına sinyal değildir. Aynı yön 2 hafta sürerse sinyaldir (2 hafta eşiği *sezgisel*).
- CTR binlerce tıklamaya dayandığı için küçük değişimi de güvenilir gösterir. CVR ise dönüşüm sayısı kadar gürültülüdür.
- Taban çok haftanın toplamından geldiği için onun payı küçüktür. Kuralı bu haftaya uygula.
- **İki dönemin farkı (marjinal CPA):** Eski dönemde a, yeni dönemde b sonuç varsa b − a farkının payı yaklaşık **±2 × √(a + b)** olur. Δ sonuç bu payın altındaysa marjinal CPA "belirsiz"dir, ona bakarak karar verme. Örnek: 35 → 45 satış, Δ = 10, pay ±2 × √80 ≈ ±18 → belirsiz. Çözüm: en az 2 haftalık pencerelerle yeniden hesapla ya da bütçe adımının oturmasını bekle. Belirsizken ortalama CPA kuralı geçerlidir (Modül 06 §5.3).

### 3.4 Doldurulmuş örnek (*varsayım*; taban §6.3'teki mağazadan)

Hafta 9: CPM 153,5 TL (taban 126,6), CTR %1,12 (taban %1,23), CVR %1,80 (taban %2,03), CPA 760 TL. Zincirden taban CPA = 126,6 ÷ (1.000 × 0,0123 × 0,0203) ≈ 507 TL. Örnekteki bütün CVR ve CPA değerleri olgun veridir: Hafta 9 ve taban haftaları bitişlerinden en az 7 gün sonra okunmuştur (§0 madde 7). Pazartesi rutininde Hafta 9'un CPM ve CTR'ı hemen, CVR ve CPA'sı ise bir hafta sonra ya da aynı yaştaki tabanla okunur.
- **Çarpanlar:** CPM 1,21 · CTR 1,10 · CVR 1,12 → çarpım ≈ 1,49, yani CPA +%49. Logaritmik paylar: CPM ≈ %48, CVR ≈ %29, CTR ≈ %23.
- **Gürültü süzgeci (§3.2 adım 1):** CPM bandın (110–144 TL) dışında ve tabandan %21 yüksek, %15 ek eşiğini aşıyor → listede. 1.110 tıklama → ±%6, CTR düşüşü (−%9) bunu aşıyor ve CTR bant (%1,20–1,26) dışında → listede. 20 alışveriş → ±%45. CVR düşüşü (−%11) gürültü içinde ve bant içinde → listede değil. CPA artışı sınırda.
- **Sıralama:** Listedeki halkalar CPM (%48) ve CTR (%23). İkisi birlikte bozulduğu için §3.2'deki istisna kontrol edilir.
- **Karar:**
  - (1) CPM artışı hesap geneli sezon mu? Geçen yıl katsayısına bak (§6.4). Açık Artırma Rekabetindeki Değişim'in günlük serisi yalnız destekleyici kanıttır (T09).
  - (2) Sezonsa CPM'i beklenen say, CTR için sıklık ve kreatif yaşına bak (T13, T22). Sezon değilse CPM ve CTR'ı tek kreatif sorunu say (T10).
  - (3) CVR için bekle.
  - (4) Panel CPA 760 TL, panel başabaş CPA 620 TL'yi (*varsayım*) aştı. Kıyas panel ile panel arasında yapılır (§0 madde 5). Tek hafta olduğu için bütçeyi kesme, farklı kavram ekle. Bu, oturumdaki tek önemli düzenlemedir (§4 eylem kuralı). İkinci olgun hafta da başabaş üstündeyse bütçeyi bir önceki seviyeye çek (Modül 06 §5.3).

---

## 4. Teşhis tablosu: bu rakamı görürsen → muhtemel neden → yapılacak

"Yüksek" ve "düşük" = §6 tabanının bandı dışında ve §3.3 gürültü payının üstünde (CPM ve sıklıkta §6.1'deki ek eşik). Gecikmeli metrikler (CVR, CPA, ROAS) olgun veriyle okunur (§0 madde 7). Satırı değişiklik günlüğünde (Modül 06 §10) numarasıyla an.

**Eylem kuralı:**
1. **Hemen yapılacaklar.** Ölçüm, operasyon ve politika düzeltmeleri aynı gün yapılır, haftalık performans oturumunu beklemez. Örnekler: T01, T17, T19, T21'in kontrol adımı, T33, ret ve hesap kısıtlaması.
2. **Performans değişiklikleri haftada tek oturumda yapılır** (Modül 06 §11, Modül 10 §3 adım 7). Bir reklam setinde tercihen tek önemli düzenleme yap. Birden fazlası gerekiyorsa hepsini aynı oturumda toplu yap, günlere yayma. Her önemli düzenleme öğrenmeyi yeniden başlatır ve Meta öğrenme sürerken düzenleme yapmamayı öneriyor [RESMİ]. Sonra 7 gün dokunma. Tek istisna, Modül 06 §6.2'deki bütçe adımlarıdır.
3. **"Yapılacak (sırayla)" sütunu.** Önce yalnız birinci adımı uygula. 7 gün sonra olgun veride sonuç yoksa sonraki adıma geç. Birinci adım uygulanamıyorsa (ör. birleştirilecek set yok) doğrudan sonrakine geç.

| # | Bu rakamı görürsen | Muhtemel neden | Yapılacak (sırayla) | Dayanak |
|---|---|---|---|---|
| | **Yayın ve harcama** | | | |
| T01 | Harcama 0, "Aktif" ya da hata simgesi | Ödeme, hesap harcama sınırı, ret, başlangıç tarihi ileride, kitle çok dar | Yayın sütunu ve hata simgesi; Kurulum Kılavuzu §6.1, §10 | [RESMİ] |
| T02 | Harcama bütçenin çok altında, teklif hedefli (sonuç başına ücret ya da ROAS hedefi) | Hedef çok sıkı ("teklif kısıtlı") | Hedefi %5–10 gevşet, 7 gün bekle. CPA da hedef üstündeyse setleri birleştir | [RESMİ] |
| T03 | Yayın sütununda "Teklif sınırlı" (teklif üst sınırı) | Tavan açık artırmayı kazanamıyor | Teklifi yükselt, kitleyi genişlet ya da kreatif kalitesini artır | [RESMİ] |
| T04 | Harcama düşük, en yüksek hacim teklifi | Küçük kitle, açık artırma çakışması, çok kısıt | Hesaba Genel Bakış'taki çakışma uyarısı; benzer setleri birleştir, kitleyi genişlet | [RESMİ] |
| T05 | Günlük harcama bütçenin %175'ine kadar çıkıyor | Bütçe esnekliği (haftalık tavan 7×; kademeli sunuluyor) | Bir şey yapma. Fatura fazlası için Kılavuz §5.2, §9 | [RESMİ] |
| T06 | "Öğrenmeyle sınırlı" | Haftada ~50 olay yok: küçük kitle, düşük bütçe, düşük teklif ya da ücret kontrolü, çakışma, seyrek olay, çok reklam | **Önkoşul:** Yalnız CPA/ROAS hedef dışındaysa ya da ölçek gerekiyorsa uygula. Hedefteyse dokunma, çünkü bu durum ceza değildir [RESMİ] (önkoşul *sezgisel*). N'yi hesapla (Modül 02 F4–F5). Sırayla: birleştir → genişlet → bütçe → hedefli teklifte ücret ya da ROAS kontrolünü %5–10 gevşet → daha sık olay (olay merdiveni, Modül 02 §2.2). Her adım önemli düzenlemedir ve öğrenmeyi yeniden başlatır | [RESMİ] + *sezgisel* |
| T07 | Set sık sık "Öğreniyor"a dönüyor; öğrenmede harcanan pay yüksek | Sık önemli düzenleme: kreatif, hedefleme, olay, büyük bütçe değişikliği, 7+ gün durdurma | "Son önemli düzenleme" sütunu; değişiklikleri topla, 7 gün dokunma | [RESMİ] |
| | **CPM** | | | |
| T08 | CPM taban bandının üstünde (§6.1 ek eşikle), CTR ve CVR tabanda, CPA hedefte | Sistem pahalı ama dönüşen gösterim alıyor. Dönüşüm setinde CPM iyi gösterge olmayabilir | Dokunma | [RESMİ] (yorum) + *sezgisel* (eşik) |
| T09 | CPM tüm hesapta birden yükseldi, CPA kötüleşti | Sezon ya da rekabet (11.11; Kara Cuma 27 Kasım 2026; bayramlar) | Sezonu §6.4'teki geçen yıl katsayısıyla doğrula. Bu ana kanıttır. Açık Artırma Rekabetindeki Değişim'i günlük kırılımda oku ve dönem boyunca birikimli etkisine bak. Günlük değerlerin çoğu pozitifse rekabet yavaş artıyordur. Tek gün %20 aşımı yalnız ani sıçramanın kanıtıdır, aşım olmaması sezonu dışlamaz. Soğuk test açma, kanıtlanmış kreatifle devam et (Modül 06 §9) | [RESMİ] + [PRATİSYEN] + *türetilmiş* |
| T10 | CPM yüksek ve CTR düşük | Kreatif ilgi çekmiyor, kalite sıralaması düşük | Tanılara bak (≥500 gösterim); belirgin farklı yeni kavram ekle | [RESMİ] |
| T11 | CPM yalnız bir sette çok yüksek, kitle küçük (yerel, yeniden hedefleme) | Dar kitlede fırsat azaldı | Geniş setle birleştir ya da genişlet; harcama tavanını kitleye göre düşür (Modül 03 §8.3) | *sezgisel* |
| T12 | CPM çok düşük ama sonuç yok ya da kalitesiz | Sığ optimizasyon olayı (bağlantı tıklaması, etkileşim) ya da düşük değerli alan envanteri | Performans hedefini gerçek iş olayına çevir (Modül 02 §2); alan kırılımına 7 günlük bak | [RESMİ] + *sezgisel* |
| | **CTR ve kreatif** | | | |
| T13 | CTR (bağlantı) düşük, kanca oranı düşük | İlk 3 saniye durdurmuyor | Yeni kanca, aynı gövde (Modül 04 §9) | [PRATİSYEN] |
| T14 | Kanca iyi, tutma zayıf, CTR düşük | Gövde vaadi karşılamıyor | Gövdeyi ya da açıyı değiştir | [PRATİSYEN] |
| T15 | CTR (tümü) yüksek, CTR (bağlantı) düşük | Etkileşim var, tıklama sebebi yok; eylem çağrısı zayıf | Teklifi ve eylem çağrısını netleştir; etkileşim tuzağı kurma | [RESMİ] |
| T16 | Kalite ve dönüşüm sıralaması düşük, etkileşim normal | Tıklama tuzağı ya da yanıltıcı vaat | Vaadi sayfayla eşle, abartıyı kaldır | [RESMİ] |
| | **Tıklamadan sonra** | | | |
| T17 | CTR iyi, YSG oranı düşük (taban yoksa < %70) | Sayfa yavaş, yönlendirme hatası, uygulama içi tarayıcı sorunu, piksel geç yükleniyor | Telefonla test, PageSpeed (Modül 01 §6.4). Kreatife dokunma | [RESMİ] + *sezgisel* |
| T18 | CTR yüksek ama dönüşüm yok (YSG iyi) | Vaat ile sayfa uyumsuz, fiyat, stok, yanlış kitle | CVR'ı 7 gün tıklama sütunuyla doğrula, görüntüleme payına bak (§1.2). Aynı sayfaya giden diğer reklamlar dönüşüyor mu? Evet → reklam sorunu, kapatma kuralı (Modül 06 §5.2). Hayır → sayfa ya da teklif testi. Fiyat ya da indirim değişecekse önce Modül 09 §5.3 (referans fiyat, indirim oranı, tarih) | [PRATİSYEN] + [MEVZUAT] atfı |
| T19 | Sepete ekleme var, ödeme başlatma ya da alışveriş çok az | Kargo ücreti sürprizi, üyelik zorunluluğu, kapıda ödeme ya da taksit yok, ödeme hatası, bozuk alışveriş olayı | Telefondan test siparişi; Olayları Test Et (Modül 05 §2.7) | *sezgisel* |
| T20 | CVR iyi, CPA yine yüksek | CPM/CPC yükseldi ya da ortalama sepet düşük | Paket ya da sepet büyütme teklifi; uygunsa değer optimizasyonu. Paket fiyatı, "2 al 1 öde" ya da indirim kurgusu için önce Modül 09 §5.3 | *türetilmiş* + [MEVZUAT] atfı |
| | **CPA ve ROAS** | | | |
| T21 | Ani CPA artışı (1–3 günde %50+; eşik *sezgisel*) | Önce operasyon: olay kopması, site ya da ödeme arızası, stok, fiyat değişikliği, yeni düzenleme. Sonra ilişkilendirme gecikmesi ve rekabet. Son günlerin CPA'sı gecikme yüzünden zaten yüksek görünür (§0 madde 7) | Aynı gün kontrol et: olay akışı, K < 0,70 olay kaybı kontrolü (Modül 05 §10.2), site ve ödeme. Sağlamsa 72 saat bekle, olgun 7 günlük pencereyle oku. Bu eşik yalnız kontrolü tetikler, performans kararını değil. Panikle yeni düzenleme yapma | [RESMİ] + [PRATİSYEN] |
| T22 | CPA haftalarca yavaş yükseliyor, sıklık artıyor, CTR düşüyor | Kreatif yorgunluğu ya da sıcak kitlenin tükenmesi | Hedef kitle segmenti kırılımı; eskiyi kapatmadan farklı kavram ekle (Modül 04 §9) | [RESMİ] + [PRATİSYEN] |
| T23 | "Kreatif sınırlandı" ya da "Kreatif yorgunluğu" | Sonuç başına ücret geçmişin üstünde (2 kattan az / 2 kat ve üstü) | Belirgin farklı görsel ya da videoyla yeni reklam, kitleyi genişlet; eskisini hemen kapatma | [RESMİ] |
| T24 | Bütçe artışından sonra CPA yükseldi | Ucuz fırsatlar bitti, marjinal CPA yüksek | Önce belirsizlik kontrolü (§3.3): Δ sonuç < 2 × √(eski + yeni sonuç) ise marjinal CPA belirsizdir. Karar verme, 2 haftalık pencerelerle ya da bütçe adımı oturduktan sonra yeniden hesapla. Olgun veri kullan. Belirsiz değilse ve marjinal CPA (F7) > başabaş ise bir önceki bütçeye dön (Modül 06 §6) | [RESMİ] + *türetilmiş* |
| T25 | Panel ROAS başabaş üstünde, kasada kâr yok | KDV dahil değer, konum ücreti, iade, çift sayım | V ve K oranı (Modül 05 §5.5); panel başabaşını yeniden hesapla (Modül 02 F3) | *türetilmiş* |
| T26 | CPA iyi ama toplam ciro ve MER yerinde sayıyor | Bütçe zaten alacak olan mevcut ya da sıcak müşteriye gidiyor | Hedef kitle segmentleri raporu, artımlı ilişkilendirme sütunu, coğrafi test (Modül 05 §11) | [RESMİ] + [PRATİSYEN] |
| T27 | Kırılımda bir reklam, alan ya da set pahalı görünüyor ama bütçenin çoğunu alıyor | Kırılım etkisi: sistem marjinal ücreti düşük olanı öne çıkarıyor | Kapatma. Toplam hedefteyse dokunma (Modül 06 §8) | [RESMİ] |
| | **Sıklık** | | | |
| T28 | Soğuk kitlede 7 günlük sıklık tabanın belirgin üstünde, CTR iki hafta düşüyor, CPA hedef üstünde | Gerçek yorgunluk | Yeni kavram ekle, kitleyi genişlet. Pratisyen dikkat bandı 2–3. Kaynaklar 2,0–3,0 arasında: Vaizle 2,5, Shopify TR 2–3, AdManage 2,5–3,0 (küçük kitlede 2,0) (*sezgisel*) | [PRATİSYEN] |
| T29 | Sıklık yüksek ama CPA ve CTR sabit (yeniden hedefleme, yerel küçük kitle) | Beklenen durum | Dokunma, eğilimi izle | [PRATİSYEN] |
| | **Mesaj, form, arka uç** | | | |
| T30 | Mesaj çok, satış yok; konuşma → satış tabanın çok altında | Geç ya da kalitesiz yanıt, fiyatı sohbette saklama, mesai dışı yayın; sistem satın alanı değil mesaj atanı öğreniyor | Gizli müşteri testi (Modül 01 §6.3). **Hukuk kapısı:** Satış verisini Meta'ya göndermeden önce Modül 09 §5.6'yı uygula: KVKK aydınlatması, büyük olasılıkla yurt dışı aktarım (md. 9), kayıtlı hukukçu onayı. Mesajlaşan kişiye sonradan kampanya SMS'i ya da araması için Modül 09 §5.7 (İYS) geçerli. Kapı açıksa satışları "Ödendi" etiketiyle Meta'ya ver. 30 günde 10 alışveriş olunca "mesajlaşma yoluyla alışveriş" hedefine geç (Modül 05 §7) | [RESMİ] + *sezgisel* + [MEVZUAT] atfı |
| T31 | Form doldu ama ulaşılamıyor; yanlış ya da sahte numara | "Daha yüksek hacim" form türü, geç arama, mesai dışı yayın, düşük niyet | "Daha yüksek satın alma niyeti" form türü, eleme sorusu, ekibin dönebildiği saatlerde yayın, ilk aramayı hızlandır, CRM aşamalarını CAPI ile gönder (Modül 05 §8). **Hukuk kapısı:** CAPI'den önce Modül 09 §5.6 (aydınlatma, yurt dışı aktarım, hukukçu onayı). İlk dönüş araması kişinin kendi talebine yanıttır. Sonraki tanıtım araması ve SMS için İYS onayı gerekir (Modül 09 §5.7). **Eleme sorusunda** sağlık, din, siyasi görüş, cinsel yaşam, sendika üyeliği ya da sabıka sorma. Bunlar KVKK md. 6'ya göre özel nitelikli veridir. Finans ve kimlik numarası da sorma. Meta bu soruları içeren formlu reklamı yayınlamıyor (Modül 09 §5.6) | [RESMİ] + *sezgisel* + [MEVZUAT] atfı |
| T32 | CPL düşük, nitelikli oran düşük | Hedef "potansiyel müşteri sayısı", teşvik fazla | CRM bağla. Hacim yetiyorsa (geliştirici belgesi: ayda 200+ potansiyel müşteri) "nitelikli potansiyel müşteri" hedefine geç; CAPI şart. **Hukuk kapısı:** CRM verisi Meta'ya gitmeden önce Modül 09 §5.6 (aydınlatma, yurt dışı aktarım, hukukçu onayı) | [RESMİ] + [MEVZUAT] atfı |
| | **Ölçüm ve raporlama** | | | |
| T33 | Meta'daki alışveriş sayısı mağaza panelinden fazla | Çift piksel ya da tekilleştirme hatası | Bütçe kararlarını dondur; Modül 05 §10.2 | [RESMİ] + [TOPLULUK] |
| T34 | Mart 2026 sonrası "tıklama" dönüşümleri düştü | Yeniden sınıflandırma: beğeni ve paylaşım artık "etkileşim" sayılıyor | Performans kararı verme; aynı tanımlı dönemleri kıyasla | [RESMİ] |
| T35 | Kampanya erişimi set erişimlerinin toplamından küçük; kırılım toplamı özet satırını tutmuyor | Tekil ölçüm ve örnekleme | Normal, hata sanma | [RESMİ] |

---

## 5. Kıyas değerleri (yalnız kaynaklı, aralık, tarihli)

### 5.1 Önce bunu bil

- Meta ülke ya da sektör bazında CPM/CPC tablosu yayımlamıyor. Tahminleri hesaba özel gösteriyor [RESMİ].
- Türkiye için yöntemi açık, güvenilir bir CPM, CPC, CTR ya da CPL kıyası bulunamadı (Ekim 2026). Bulunan kaynaklar birbiriyle 2–4 kat çelişiyor.
- Küresel kıyaslar ABD verisidir. **Mutlak dolar değerlerini Türkiye'ye taşıma.** Yalnız oranları ve yönleri kullan.
- Çeviri kuru 1 USD = 49,4463 TL (TCMB, 16.10.2026) [KAMU]. TL karşılıkları ölçüm değil, aritmetiktir.

### 5.2 Türkiye (yöntemsiz; yalnız makullük kontrolü) [PRATİSYEN]

| Kaynak (tarih) | CPM | CPC | Diğer | Not |
|---|---|---|---|---|
| Adligator (31.03.2026) | FB 2,00–3,50 $ (≈99–173 TL), IG 3,00–5,00 $ (≈148–247 TL) | — | — | "40+ ülke", veri seti açıklanmıyor |
| Stackmatix (06.09.2026) | 2,00–4,40 $ (≈99–218 TL) | — | — | Yöntem yok |
| ZBT Media (30.05.2026) | 15–80 TL | 2–25 TL | Lead/mesaj 50–500 TL | Kaynak yok |
| Yellowstar (01.08.2026) | ≈30–150 TL | ≈2–10 TL | Mesaj ≈15–60 TL | "Kendi kampanyalarımız", örneklem yok |
| Foxs Digital (23.03.2026) | 40–120 TL | 1,5–6 TL | — | ABD verisinden uyarlama |
| BossyDigital (23.06.2026) | 40–90 TL | 1,5–8 TL | — | Kaynak yok |
| Medyografya (16.07.2026) | ≈80–350 TL | 3–20 TL | CPL 50 TL'den birkaç bin TL'ye | Kaynak yok |

**Kullanım kuralı (*sezgisel*):** Bu tablo en fazla 15–350 TL gibi geniş bir CPM makullük bandı verir. Hesabın CPM'i bandın tamamen dışındaysa önce amaç, kitle, alan ve para birimini kontrol et. Bandın içinde olmak "iyi" demek değildir. Dönüşüme optimize eden kampanyada CPM'in aynı hesabın trafik kampanyasından yüksek çıkması normaldir.

### 5.3 Küresel (ABD ağırlıklı; oran ve yön için) [PRATİSYEN]

| Kaynak · dönem · örneklem | Kampanya | CTR | CPC | CVR | Sonuç başı ücret | Diğer |
|---|---|---|---|---|---|---|
| LocaliQ/WordStream 2026 · dönem ve örneklem yazmıyor · ortalama · sayfa 23.09.2026 | Trafik | %1,93 | 0,60 $ | — | — | Sektör CTR %1,46 (finans) – %2,73 (güzellik) |
| aynı | Potansiyel müşteri | %2,70 | 1,80 $ | %8,54 | CPL 27,39 $ | Sektör CTR %1,35–4,18; CVR %4,15–15,87 |
| WordStream/LocaliQ 2025 · Nisan 2024–Haziran 2025 · 554 trafik + 726 lead ABD kampanyası · medyan | Trafik | %1,71 | 0,70 $ | — | — | — |
| aynı | Potansiyel müşteri | %2,59 | 1,92 $ | %7,72 | CPL 27,66 $ | CPL yıllık ≈ +%21 |
| Triple Whale · Ağustos 2025–Temmuz 2026 · 40.000+ e-ticaret markası · medyan (Fibbler aktarımı) | E-ticaret satış | %2,39 | — | %1,53 | CPA 38,99 $ | CPM 15,06 $ (yıllık +%13,24), ROAS 1,88. Dikey aralığı: CTR %1,88–3,02, CVR %1,09–1,89, ROAS 1,13–2,35 |

**Okuma:** (1) Potansiyel müşteri kampanyasında CTR, trafik kampanyasından yüksek. (2) Sektör satırları baskıdan baskıya çok oynuyor: alışveriş ve hediye trafik CTR'ı 2025'te %4,13, 2026'da %1,88. Sektör satırını ±%50 belirsizlikle oku (*sezgisel*). (3) E-ticaret medyan CVR'ı (%1,53) lead CVR'ından (%8,54) çok düşük. Farklı kampanya türlerini aynı eşikle yargılama.

### 5.4 Fiyat ve sezon eğilimi

| Kaynak · dönem | Bulgu |
|---|---|
| Meta 10-Q · 2026 2. çeyrek [RESMİ] | Meta geneli ortalama reklam fiyatı yıllık +%12, gösterim +%14. Türkiye "Avrupa" bölgesinde, ülke fiyatı yok |
| Lebesgue · Eylül–Kasım 2025, 22,6 milyon $ [PRATİSYEN] | Kara Cuma haftası CPM eylülün +%59, Kara Cuma günü +%84 üstünde. Satın alma başı maliyet −%20. İlk belirgin artış 10 Kasım haftası (+%15) |
| Biddy · Ekim 2025–Ocak 2026, büyük ABD hesapları [PRATİSYEN] | Kara Cuma günü CPM ekimin +%57, haftası +%35 üstünde. Aralık 2. hafta CPA ekimin +%39 üstünde. Aralık sonu–ocak başı CPM ve CPA ≈ −%24 |
| Bïrch · 2021–2023 ABD [PRATİSYEN] | Zirve kasım sonu (~%40 yüksek), en ucuz ay ocak |

Türkiye'ye özel 11.11, Efsane Cuma, Ramazan ya da bayram CPM ölçümü bulunamadı. Ajans iddiaları ("%30–50", "%50–200") kaynaksız. **Kural:** Sezon beklentisini kendi geçen yıl verinle kur (§6.4).

### 5.5 Meta'nın resmî eşikleri (kıyas değil, kural) [RESMİ]

Öğrenme için haftada ~50 sonuç · tanılar için 500 gösterim · "Kreatif yorgunluğu" = geçmiş sonuç başına ücretin 2 katı · Açık Artırma Rekabetindeki Değişim %20 üstü önemli · A/B testinde %65 güven kazanan, yükseliş testinde %90 · günlük bütçe ≥ ortalama olay ücretinin 10 katı, sonuç başına ücret hedefinde ≥ hedefin 5 katı · harcama günlük bütçenin en fazla %175'i, haftada 7 katı.

**Pratisyen bantları (*sezgisel*, ölçülmüş kıyas değil):**
- Soğuk kitlede 7 günlük sıklık için dikkat bandı 2–3. Kaynaklar 2,0–3,0 arasında [PRATİSYEN Vaizle, Shopify TR, AdManage].
- Kanca oranı: Motion %30–40'ı hedef gösteriyor, %25 altını uyarı sayıyor. Aynı sayfa %35 üstünü "ilgiyi hızlı yakalıyor" diye niteliyor [PRATİSYEN Motion].
- Tutma oranı ortalaması %40–50 [PRATİSYEN Motion].
- YSG oranı %70'in altındaysa sayfayı incele.

---

## 6. Kendi hesabının tabanını çıkar

### 6.1 Yöntem

1. **Pencere:** Son 8–12 tam hafta (*sezgisel*). Hafta tanımı §0 madde 8: pazar–cumartesi. 4 haftadan az veri varsa "geçici taban" de (*sezgisel*).
2. **Ayır:** Amaç ve optimizasyon olayı başına ayrı taban kur (satış, lead, mesaj). Trafik CPM'ini satış CPM'iyle karıştırma. Aynı ilişkilendirme ayarını kullan. Yeni müşteri ve yeniden hedefleme ayrı tutulur.
3. **Çıkar:** İlk öğrenme haftasını, ölçüm arızalı haftaları ve 3 Mart 2026 öncesini çıkar. Kampanya dönemleri (11.11, Kara Cuma, bayram) ayrı "sezon tabanı" olur. CVR, CPA ve ROAS'ın olgun tabanına veri yaşı 7 günden küçük haftayı alma (§0 madde 7).
4. **Oranları toplamlardan hesapla:** Haftalık CTR = haftalık tıklama ÷ haftalık gösterim. Günlük oranların ortalamasını alma.
5. **Taban = haftalık değerlerin medyanı. Normal bant = medyan ± 1,5 × IQR**, IQR = P75 − P25 (*sezgisel*).
   - P25–P75 bandı kullanılmaz. Bu bant tanım gereği normal haftaların yarısını dışarıda bırakır. "Aynı yönde 2 hafta" kuralıyla birleşince tamamen rastlantısal bir metrik her hafta çiftinde yaklaşık %12,5 olasılıkla (2 × 0,25²) eylem tetikler (*türetilmiş*).
   - Normal dağılımda medyan ± 1,5 × IQR yaklaşık ±2 standart sapmaya denk gelir (*türetilmiş*). Gerçek haftalık veri normal dağılmaz, bu yüzden bu yalnız kaba bir denkliktir.
   - 12 haftadan fazla veri varsa P10–P90 da kullanılabilir. 8 haftada P10–P90 neredeyse en küçük ve en büyük değerdir, o yüzden kullanılmaz.
6. **Alarm (*sezgisel*):**
   - Değer bant dışında **ve** §3.3 gürültü payının üstündeyse sinyaldir.
   - CPM ve sıklıkta sayıma dayalı bir gürültü payı yoktur. Bunlarda sinyal için iki koşul birlikte aranır: bant dışında olmak **ve** tabandan en az %15 sapmak.
   - Aynı yönde 2 hafta → eylem. Tek hafta → not al.
   - Gecikmeli metriklerde bu kıyas olgun veriyle ya da aynı yaştaki anlık görüntüyle yapılır.
7. **Güncelle:** Ayda bir ya da büyük yapı değişikliğinden sonra. Değişikliği günlüğe yaz (Modül 06 §10).
8. **Anlık görüntü (gecikme için §0 madde 7, yol b):**
   - Her pazartesi az önce biten haftanın CVR, CPA ve ROAS değerlerini taban kartına veri yaşıyla birlikte kopyala. Pazartesi okumada veri yaşı 2 gündür.
   - 8 hafta sonra bir "yaş 2 tabanı" oluşur. Bu haftanın yaş 2 değerini yalnız bu tabanla kıyasla.
   - Olgun değerleri (veri yaşı 7 gün ve üstü) ayrı satırda tut.

### 6.2 Taban kartı (şablon)

| Metrik | Veri yaşı (gün) | Taban (medyan) | Bant (medyan ± 1,5 × IQR) | Bu hafta | Gürültü payı / ek eşik | Durum | Dönem |
|---|---|---|---|---|---|---|---|
| CPM | Son hafta (pazartesi: 2) | | | | Bant dışı ve tabandan %15+ (*sezgisel*) | Normal / Sinyal / Eylem | |
| CTR (bağlantı) | Son hafta | | | | ±2 ÷ √tıklama | | |
| YSG oranı | Son hafta | | | | ±2 ÷ √tıklama | | |
| CVR | ≥ 7 (olgun) ya da tabanla aynı yaş | | | | ±2 ÷ √dönüşüm | | |
| CPA (panel ↔ panel başabaş · gerçek ↔ gerçek başabaş) | ≥ 7 ya da tabanla aynı yaş | | | | ±2 ÷ √dönüşüm | | |
| ROAS | ≥ 7 ya da tabanla aynı yaş | | | | ±2 ÷ √dönüşüm (kaba) | | |
| Sıklık (7 gün) | Son hafta | | | | Bant dışı ve tabandan %15+ (*sezgisel*) | | |

Taban satırının veri yaşı ile "Bu hafta" sütununun veri yaşı aynı olmalıdır. Değilse satırı "kıyaslanamaz" diye işaretle.

### 6.3 Doldurulmuş örnek (*varsayım*): site satış kampanyası, 8 hafta

| Hafta | Harcama | Gösterim | Bağlantı tık. | YSG | Alışveriş | CPM | CTR | YSG oranı | CVR | CPA |
|---|---|---|---|---|---|---|---|---|---|---|
| 1 | 14.000 | 112.000 | 1.400 | 1.120 | 28 | 125,0 | %1,25 | %80,0 | %2,00 | 500 |
| 2 | 14.500 | 121.000 | 1.480 | 1.200 | 33 | 119,8 | %1,22 | %81,1 | %2,23 | 439 |
| 3 | 13.800 | 104.000 | 1.290 | 1.010 | 23 | 132,7 | %1,24 | %78,3 | %1,78 | 600 |
| 4 | 14.200 | 118.000 | 1.450 | 1.170 | 30 | 120,3 | %1,23 | %80,7 | %2,07 | 473 |
| 5 | 15.000 | 117.000 | 1.520 | 1.200 | 34 | 128,2 | %1,30 | %78,9 | %2,24 | 441 |
| 6 | 14.700 | 110.000 | 1.340 | 1.060 | 25 | 133,6 | %1,22 | %79,1 | %1,87 | 588 |
| 7 | 14.900 | 123.000 | 1.510 | 1.230 | 31 | 121,1 | %1,23 | %81,5 | %2,05 | 481 |
| 8 | 14.400 | 109.000 | 1.330 | 1.050 | 26 | 132,1 | %1,22 | %78,9 | %1,95 | 554 |
| **Taban (medyan)** | | | | | | **126,6** | **%1,23** | **%79,6** | **%2,03** | **490** |
| P25–P75 (yalnız IQR için) | | | | | | 120,9–132,3 | %1,22–1,24 | %78,9–80,8 | %1,93–2,11 | 465–562 |
| **Bant (medyan ± 1,5 × IQR)** | | | | | | **110–144** | **%1,20–1,26** | **%76,8–82,3** | **%1,76–2,29** | **345–636** |
| **Hafta 9** | 15.200 | 99.000 | 1.110 | 860 | 20 | **153,5** | **%1,12** | %77,5 | %1,80 | **760** |

Bütün değerler olgun veridir: her hafta, bitişinden en az 7 gün sonra okunmuştur.

**Okuma:**
- CPM bant dışında ve tabandan %21 yüksek (ek eşik %15) → sinyal.
- CTR bant dışında ve düşüşü (−%9) gürültü payının (±%6) üstünde → sinyal.
- YSG oranı ve CVR bant içinde → not al.
- CPA bant dışında, artış gürültü sınırında (20 alışveriş, ±%45).
- Çarpan hesabı ve karar §3.4'te.

**Bant seçiminin etkisi:** Eski P25–P75 bandıyla ilk 8 haftanın yarısı bant dışında kalırdı. CPM'de 2., 3., 4. ve 6. haftalar, CPA'da 2., 3., 5. ve 6. haftalar dışarıdaydı. Bu normaldir, çünkü P25–P75 tanım gereği haftaların yarısını dışarıda bırakır. Medyan ± 1,5 × IQR bandında ilk 8 haftadan yalnız 5. haftanın CTR'ı (%1,30) bandın üstünde kalıyor. Yön olumlu ve tek hafta → not al.

### 6.4 Kur, enflasyon ve sezon düzeltmesi

- **Yıllık kıyası TL'de doğrudan yapma.** Örnek (*varsayım*): Ekim 2025'te CPM 80 TL.
  - Kur düzeltmesi: 80 × (49,4463 ÷ 41,8488) ≈ **94,5 TL** (TCMB, 16.10.2026 ve 16.10.2025 [KAMU]).
  - TÜFE düzeltmesi: 80 × 1,2973 ≈ **103,8 TL** (Eylül 2026 yıllık %29,73 [KAMU]).
  - Geçen yılın 80 TL'si bugün ≈ 95–104 TL eder. Bu yıl ekim CPM'i 126 TL ise reel artış var.
- **Sezon katsayısı** = geçen yıl o haftanın CPM'i (ya da CPA'sı) ÷ geçen yıl önceki 4 haftanın medyanı. Bu yılın tabanı × katsayı = sezon beklentisi.
  - Örnek (*varsayım*): Geçen yıl 11.11 haftası CPM 104 TL, önceki 4 hafta medyanı 80 TL → katsayı 1,30. Bu yıl taban 126,6 TL → beklenti ≈ 165 TL.
  - Hafta 9 (153,5 TL) 11.11 haftasına denk geliyorsa CPM beklentinin altında kalır, yani sorun CPM değildir. O zaman CTR satırlarına (T13, T22) git.
- Geçen yıl verisi yoksa sezon kararı zayıf kanıtla verilir:
  - Açık Artırma Rekabetindeki Değişim'in günlük değerlerinde süreklilik ara. Günlerin çoğu pozitifse rekabet yavaş artıyordur (T09).
  - Tek gün %20 aşımının olmaması "rekabet artmadı" demek değildir. Metrik her günü yalnız önceki 3 günle kıyaslar [RESMİ].
  - §5.4'teki küresel aralıkları (+%35 ile +%84) yalnız yön için kullan.

### 6.5 Yeni hesap (veri yok)

1. İlk en az 2 hafta (*sezgisel*) en yüksek hacim ya da en yüksek değer teklifiyle taban oluştur. Sonuç başına ücret hedefine geçilecekse bu tabanın %10–20 üstünden başla [RESMİ].
2. Bu sürede §5'i yalnız makullük için kullan. Kapatma kararını başabaş CPA'ya göre Modül 06 §5.2'nin 3× / 5× kuralına bağla.
3. 4. haftada geçici taban, 8. haftada asıl taban kur (*sezgisel*). CVR ve CPA tabanına yalnız olgun haftalar girer (§0 madde 7).

---

## 7. Haftalık teşhis rutini (pazartesi, ~20 dakika)

1. Ölçüm alarmları kapalı mı? Modül 05 §10.2'deki tablonun tamamına bak (§0 madde 1). Açıksa düzeltmeyi aynı gün yap, performans teşhisine geçme.
2. Yayın sütunu: ret, "Öğrenmeyle sınırlı", "Teklif sınırlı", "Kreatif yorgunluğu" (T01–T07, T23).
3. Amaca uygun ön ayarla (`07-SATIS`, `07-LEAD`, `07-MESAJ`) doğru düzeyde iki pencere aç. Hafta pazar–cumartesi (§0 madde 8):
   - **Az önce biten hafta** (veri yaşı 2): harcama, CPM, CTR, YSG oranı, sıklık.
   - **Ondan önceki hafta** (veri yaşı 9, olgun): CVR, CPA, ROAS. Yol b'yi kullanıyorsan az önce biten haftanın değerlerini yaş 2 tabanıyla kıyasla (§6.1 adım 8).
4. Her metriği taban kartında aynı veri yaşındaki satırla kıyasla. Bant dışı mı? Gürültü payını ya da CPM ve sıklıkta ek eşiği aşıyor mu?
5. Olgun haftanın CPA değişimini üç çarpana ayır (§3.1). Başlanacak halkayı §3.2'deki tek kurala göre seç.
6. Eylemi §4'teki eylem kuralına göre uygula. Ölçüm, operasyon ve politika düzeltmeleri hemen yapılır. Performans değişiklikleri bu oturumda topluca yapılır, sette tercihen tek önemli düzenleme. Günlüğe "T-numarası + eylem + tarih" yaz.
7. Panel CPA ve ROAS'ı **panel** başabaşla karşılaştır. Gerçek CPA (× c) ve gerçek ROAS (÷ c) ise **gerçek** başabaşla karşılaştırılır (§0 madde 5). İkisini karıştırma.
8. Az önce biten haftanın değerlerini veri yaşıyla birlikte taban kartına kopyala (§6.1 adım 8).
9. Ayın ilk pazartesi tabanı yenile (§6.1).

---

## Doğrulanması gerekenler

- Özel ölçüm bağlantısının yeri ve formülün sabit sayı (c = 1,05 ya da 1,26) kabul edip etmediği. Kaynak pratisyen, Mart 2025 güncellemesi.
- Reklam Yöneticisi'nin dönüşümü gösterim gününe yazdığı bilgisi pratisyen kaynaklıdır (Loomer, güncelleme 6 Mart 2025). Meta yalnız Reklam Raporları'nda gösterim zamanı ile dönüşüm zamanı arasında seçim yapılabildiğini yazıyor. Seçeneğin yeri ve Reklam Yöneticisi'ndeki varsayılan hesapta doğrulanmalı. 7 günlük olgunlaşma eşiği bu modülün türetmesidir.
- Tarih seçicideki pazartesi–pazar ve pazar–cumartesi hazır aralıklarının arayüz adları. API adları geliştirici belgesinde var, arayüz adları hesaba ve dile göre değişebilir.
- Medyan ± 1,5 × IQR bandı ve CPM ile sıklıktaki %15 ek eşiği *sezgisel*dir. İlk 8–12 haftada yanlış alarm sıklığını günlükten izle.
- Ön ayar ve sütun adları (ör. "Başlatılan mesajlaşma konuşmaları", "Konuşma başına ücret") hesaba ve dile göre değişebilir. Açık Artırma Rekabetindeki Değişim sütununun her hesapta görünüp görünmediği.
- %175 günlük harcama esnekliği Meta'ya göre bazı hesaplara kademeli sunuluyor.
- Mart 2026 tıklama ilişkilendirmesi tanımına bazı hesaplar henüz geçmemiş olabilir [RESMİ].
- LocaliQ 2026 sayfası veri dönemini ve örneklemi yazmıyor; 2025 baskısı medyan, 2026 sayfası ortalama diyor. WordStream 2025 ve Triple Whale sayfaları doğrudan okunamadı, rakamlar aktaran sayfadan (Fibbler) alındı.
- Türkiye CPM/CPC kaynaklarının hiçbiri yöntem açıklamıyor.
- Konum ücreti %5: Dijital Hizmet Vergisi 1 Ocak 2027'de %2,5'e iniyor. Meta'nın oranı değiştirip değiştirmeyeceği bilinmiyor. Ocak 2027'de c çarpanını (1,05 / 1,26) yeniden doğrula.
- "Mesajlaşma yoluyla alışveriş" hedefinin hangi amaçta göründüğü Meta sayfasında çelişkili (Modül 05 §7).

---

## Kaynaklar

**[RESMİ] Meta yardım merkezi (Türkçe metin, erişim Ekim 2026)**
- CPM: https://www.facebook.com/business/help/753932008002620
- Gösterimler: https://www.facebook.com/business/help/675615482516035
- Erişim: https://www.facebook.com/business/help/710746785663278
- Sıklık: https://www.facebook.com/business/help/1546570362238584
- Erişilen 1.000 Meta Hesabı başına ücret: https://www.facebook.com/business/help/1461718327429941
- Bağlantı tıklamaları: https://www.facebook.com/business/help/659185130844708
- "Tıklamalar (tümü)" ve bağlantı tıklamaları: https://www.facebook.com/business/help/674769555983979
- Bağlantı ve dışarı yönlendiren tıklamalar: https://www.facebook.com/business/help/284415655604125 · https://www.facebook.com/business/help/102928543574299
- CTR (bağlantı tıklama oranı): https://www.facebook.com/business/help/877711998984611
- CPC (bağlantı tıklaması başına ücret): https://www.facebook.com/business/help/683065845109838
- Yönlendirme sayfası görüntülemeleri: https://www.facebook.com/business/help/646884317670110 · fark: https://www.facebook.com/business/help/172641445757289
- Sonuçlar: https://www.facebook.com/business/help/611432918970668
- Sonuç başına ücret: https://www.facebook.com/business/help/762109693832964
- Sonuç oranı: https://www.facebook.com/business/help/856603834384968
- ThruPlay: https://www.facebook.com/business/help/471190536725647
- Sütunları özelleştirme: https://www.facebook.com/business/help/1452248935049010
- Kaldırılan tekil ölçümler: https://www.facebook.com/business/help/1695754927158071
- Ölçüm tutarsızlıkları: https://www.facebook.com/business/help/1098122253564910
- Reklam Yöneticisi ve Hesaba Genel Bakış: https://www.facebook.com/business/help/510910008975690
- Reklam Yöneticisi ve Olay Yöneticisi farkı: https://www.facebook.com/business/help/337196340694086
- Performans dalgalanmaları: https://www.facebook.com/business/help/1364841787225722
- Kırılım etkisi: https://www.facebook.com/business/help/770303663944673
- Açık Artırma Rekabetindeki Değişim: https://www.facebook.com/business/help/2024547657774300
- Öğrenme aşaması: https://www.facebook.com/business/help/112167992830700
- Öğrenmeyle sınırlı: https://www.facebook.com/business/help/269269737396981
- Önemli düzenlemeler: https://www.facebook.com/business/help/316478108955072
- Alaka düzeyi tanıları: https://www.facebook.com/business/help/403110480493160 · https://www.facebook.com/business/help/436113280262012
- Kreatif yorgunluğu: https://www.facebook.com/business/help/1346816142327858
- Sonuç başına ücret hedefi en iyi uygulamalar: https://www.facebook.com/business/help/176339196621566
- Teklif üst sınırı: https://www.facebook.com/business/help/272503946776144
- Açık artırma çakışması: https://www.facebook.com/business/help/537699989762051
- Günlük bütçe: https://www.facebook.com/business/help/190490051321426
- Günlük bütçe değişikliği ve pazar–cumartesi bütçe haftası: https://www.facebook.com/business/help/525189242169568
- Reklam Raporları ile üçüncü taraf araçlar arasındaki farklar (gösterim zamanı ya da dönüşüm zamanı seçimi): https://www.facebook.com/business/help/511923449323749
- Reklam açık artırmaları (toplam değer, tahmini eylem oranı): https://www.facebook.com/business/help/430291176997542
- Minimum bütçe: https://www.facebook.com/business/help/203183363050448
- Performans hedefleri (10× kuralı): https://www.facebook.com/business/help/355670007911605
- Reklam vermenin ücreti: https://www.facebook.com/business/help/201828586525529
- Konum ücretleri: https://www.facebook.com/business/help/1238737454289085
- İlişkilendirme ayarları: https://www.facebook.com/business/help/460276478298895
- Tıklama ilişkilendirmesi (3 Mart 2026): https://www.facebook.com/business/news/click-attribution
- A/B ve yükseliş testi güven eşikleri: https://www.facebook.com/business/help/239549606692303
- Hızlı form türleri: https://www.facebook.com/business/help/252352181957512
- Mesajlaşma yoluyla alışveriş: https://www.facebook.com/business/help/1214599109289826
- Meta 10-Q (30.06.2026 dönemi): https://www.sec.gov/Archives/edgar/data/0001326801/000162828026050705/meta-20260630.htm
- Marketing API Insights, `date_preset` (`last_week_mon_sun`, `last_week_sun_sat`) (geliştirici belgesi): https://developers.facebook.com/docs/marketing-api/reference/ad-account/insights/

**[KAMU]**
- TCMB kurları: https://www.tcmb.gov.tr/kurlar/today.xml · https://www.tcmb.gov.tr/kurlar/202510/16102025.xml
- TÜFE Eylül 2026 (haber): https://gazeteoksijen.com/ekonomi/eylulde-enflasyon-yillik-yuzde-29-73-aylik-yuzde-1-84-oldu-291680

**[PRATİSYEN]**
- LocaliQ 2026: https://localiq.com/blog/facebook-advertising-benchmarks/
- WordStream 2025: https://www.wordstream.com/blog/facebook-ads-benchmarks-2025 · aktaran (Triple Whale dahil): https://www.fibbler.co/blog/facebook-ads-benchmarks
- Adligator: https://adligator.com/blog/meta-ads-cpm-by-country-benchmarks
- Stackmatix: https://www.stackmatix.com/blog/facebook-ads-cpm-by-country-2026
- ZBT Media: https://zbtmedia.com/blog/instagram-reklam-maliyetleri-2026
- Yellowstar Design: https://yellowstardesign.com/instagram-reklam-ucretleri-2026/
- Foxs Digital: https://foxs.digital/blog/instagram-reklam-fiyatlari
- BossyDigital: https://bossydigital.com/turkiyede-meta-reklam-maliyetleri-neden-bu-kadar-artti/
- Medyografya: https://medyografya.com.tr/meta-reklam-maliyetleri-cpm-cpc-cpl-nedir
- Lebesgue (Kara Cuma): https://lebesgue.io/facebook-ads/are-meta-ads-worth-it-black-friday
- Biddy: https://www.biddyco.com/blog-posts/meta-ads-black-friday-cpm
- Bïrch: https://bir.ch/blog/meta-ad-costs-analysis
- Motion (kreatif metrikleri): https://motionapp.com/blog/key-creative-performance-metrics
- AdManage (kapatma, gecikme, sıklık 2,5–3,0): https://admanage.ai/blog/when-to-kill-a-facebook-ad
- Jon Loomer (özel ölçümler): https://www.jonloomer.com/custom-metrics-in-facebook-ads-manager/
- Jon Loomer (dönüşüm gösterim tarihine yazılır; güncelleme 6 Mart 2025): https://www.jonloomer.com/are-ads-manager-results-too-good-to-be-true/
- Jon Loomer (sıklık bağlama bağlı): https://www.jonloomer.com/meta-ads-master-brief/
- Vaizle (sıklık eşiği): https://insights.vaizle.com/how-to-scale-facebook-ads/
- Shopify TR (sıklık 2–3): https://www.shopify.com/tr/blog/facebook-reklamlarinin-maliyeti

**[TOPLULUK]**
- Shopify Community, Meta–Shopify satın alma farkı: https://community.shopify.com/t/meta-has-2-3x-times-purchases-shopify-data-in-meta-is-wrong-and-i-need-help-to-fix-it/404737
