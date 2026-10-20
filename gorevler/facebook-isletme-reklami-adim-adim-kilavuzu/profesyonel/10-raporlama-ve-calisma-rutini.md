# Raporlama, müşteri iletişimi ve çalışma rutini

> **Modül 10 · Profesyonel paket · Hazırlanma: 17 Ekim 2026.** Menü yolları, ödeme ve hata kodları burada yok. Onlar için **Kurulum Kılavuzu** (`../KILAVUZ.md`): reddedilen reklam §6.2, sorun giderme §10, güvenlik ve ortak erişimi §11, reklam hesabının taşınamaması §3.5, konum ücreti ve fatura §3.6 ile §9. Bu modül şunları hazır kabul eder: başabaş ve panel çarpanı (Modül 02 F1–F3, F7), haftalık mutabakat K, P, V ve MER (Modül 05 §5.5), taban kartı ve gürültü kuralı (Modül 07 §3.3, §6), karar ve kapatma tabloları (Modül 06 §4.4, §5, §6), müşteri cümleleri (Modül 01 §5.3).

## Bu modülde

- **Günlük 5 dakikalık**, **haftalık** (pazartesi) ve **aylık/çeyreklik** kontrol rutini. Her adımda eşik ve "eğer → ise" kuralı var.
- **Haftalık rapor şablonu**: durum ışığı kuralları, doldurulmuş örnek, kısa mesaj sürümü, aylık rapor ekleri ve Meta Reklam Raporları ile otomatikleştirme.
- **Müşteriye açıklama dili**: metrik sözlüğü, durum kalıpları, sık sorulanlara yanıtlar, **yetki matrisi** (ne zaman onay, ne zaman bilgi) ve yanıt süreleri.
- **Reklam Kütüphanesi ile rakip analizi**: Türkiye'de neyin görünüp neyin görünmediği, adımlar, okuma kuralları, doldurulmuş rakip kartı, yasal sınır.
- **Değişiklik günlüğü** çalışma kitabı ve **teslim/devir** kontrol listeleri (devralırken ve devrederken), devir notu şablonu.

**Etiketler:** **[RESMİ]** Meta'nın kendi sayfası. **[MEVZUAT]** Türk mevzuatı. **[PRATİSYEN]** ajans, danışman ya da araç sağlayıcı (çıkar çatışması olabilir). *sezgisel* = ölçülmemiş çalışma kuralı. *türetilmiş* = bu modülde yapılan aritmetik. *varsayım* = örnek rakam, gerçek veri değil.

---

## 0. Değişmez kurallar

1. **İş sonucunun doğruluk kaynağı müşterinin kaydıdır** (sipariş paneli, CRM, kasa, WhatsApp satış defteri). Meta rakamı yön gösterir (Modül 01 §0.2).
2. **Günlük kontrol karar üretmez.** Yalnız operasyonel hatayı düzeltir: ret, ödeme, kırık bağlantı, yanlış fiyat, stok, ölçüm kopması. Performans kararı (bütçe, teklif, kitle, set ve kampanya) haftalıktır, en az 7 tam günlük veriyle verilir [RESMİ] (Modül 06 §0). **Tek istisna:** Modül 06 §5.2 Aşama 4 koşulunu tam sağlayan reklam günlük kontrolde kapatılır (§2.1 satır 5).
3. **Günlüğe girmeyen değişiklik yapılmaz.** Haftalık raporun "yaptıklarımız" bölümü günlükten kopyalanır (§8).
4. **Her sayı bir kıyasla verilir:** hedef, başabaş, önceki dönem ve gürültü payı. Kıyassız rakam rapora girmez (*sezgisel*).
5. **Panel ve gerçek maliyet ayrı yazılır.** Gerçek maliyet = panel harcaması × 1,05; vergi numarası yoksa × 1,26 [RESMİ] (Modül 02 F3).
6. **Kötü haber erken verilir.** Hesap kısıtlaması, harcamanın durması ya da ölçüm kopması için haftalık rapor beklenmez. Bildirim süreleri tek yerde, §6.6'da durur ve tespit anından itibaren işler.
7. **Varlıkların sahibi müşteridir.** Reklam hesabı, Sayfa, Instagram, veri seti ve katalog müşterinin portfolyosunda durur. Ajans ve yapay zekâ "ortak" erişimiyle çalışır, şifre paylaşılmaz [RESMİ] (Kurulum Kılavuzu §11).

---

## 1. Takvim özeti

| Sıklık | Ne zaman | Süre (*sezgisel*) | İçerik | Bölüm |
|---|---|---|---|---|
| Her gün (hafta sonu ve resmî tatil dahil) | Sabah 09:00–10:00 | 5 dk | Uyarı, harcama hızı, olay akışı, stok/bağlantı, mesaj ve yorum | §2 |
| Her hafta | Pazartesi, rapor 12:00'ye kadar | ~60 dk | Mutabakat, teşhis, karar, kreatif hattı, rakip taraması, rapor | §3, §5 |
| Her ay | Ayın ilk pazartesi + fatura kesildikten sonra | ~90 dk | Fatura mutabakatı, erişim denetimi, veri yedeği, taban yenileme, aylık rapor | §4 |
| Her çeyrek | 13. hafta | ~3 saat | Çeyrek raporu, artırımlılık testi, yapı denetimi | §4.5 |
| Olay olunca | Tespitten itibaren §6.6'daki süre | — | Ret, kısıtlama, ölçüm kopması, şüpheli erişim | §6.3, §6.5, §6.6 |

**Rapor dönemi:** Paketin raporlama haftası **pazar–cumartesi**dir. Meta günlük bütçenin haftalık harcama tavanını bu haftaya göre hesaplar [RESMİ]; bu bir bütçe temposu kuralıdır, raporlama kuralı değildir (Modül 02 §9.3, Modül 07 §0.8). Rapor bu haftayı kapsar ve pazartesi hazırlanır; o sırada verinin yaşı 2 gündür. İki ayrı gecikme var:

- **İşleme gecikmesi.** Meta, harcanan tutarın ve sonuçların işlenmesinin 48 saate kadar sürebildiğini yazıyor [RESMİ]. Cumartesi gece yarısından sayılınca 48 saat pazartesi gece yarısı dolar. Pazartesi sabahı cumartesi akşamının rakamları hâlâ tahmini olabilir.
- **İlişkilendirme gecikmesi.** 7 günlük tıklama penceresi yüzünden haftanın son günlerine yazılan dönüşümler 7 güne kadar artmaya devam edebilir. Bu, Reklam Yöneticisi dönüşümü gösterim gününe yazdığında böyledir. Tarih atamasını hesapta doğrula (Modül 07 §0.7, Modül 05 §5.4).

**Bu yüzden (*türetilmiş*, Modül 07 §0.7):**
- Önceki haftanın rakamlarını rapor günü yeniden çek. Rapordaki ilk rakamı "anlık" (veri yaşı 2 gün) diye bırak, güncel rakamı (veri yaşı 9 gün) `RAPOR` sekmesinde ayrı sütuna yaz. Bir sonraki raporda önceki haftayı güncel rakamıyla yeniden ver.
- Az önce biten haftanın son 2–3 gününde sonuç eksik olabilir. CVR, CPA ve ROAS olgunlaştıkça iyileşebilir. Karar bunu hesaba katar: genç veriye bakıp tek başına "kötüleşti" denmez.
- Hafta kıyasını ve marjinal CPA'yı (Modül 02 F7) **aynı veri yaşındaki** rakamlarla yap. Anlık rakam anlık rakamla, güncel rakam güncel rakamla kıyaslanır. Harcama, gösterim, CPM ve CTR bu gecikmeden etkilenmez.
- Tarih aralığını elle seç, saat dilimi reklam hesabınınkiyle aynı olsun.

---

## 2. Günlük kontrol (5 dakika)

### 2.1 Sıra

| # | Süre | Nereye bak | Ne ara | Eğer → ise |
|---|---|---|---|---|
| 1 | 1 dk | Reklam Yöneticisi → **Hesaba Genel Bakış**; İşletme Desteği Ana Sayfası (Hesap Kalitesi artık burada [RESMİ]) | Ret, ödeme sorunu, kısıtlama; son 7 günde öğrenmede harcanan bütçe payı [RESMİ] | Ret → Kurulum Kılavuzu §6.2. Ödeme hatası → Kurulum Kılavuzu §3.6, §10. Kısıtlama → §6.3-D kalıbı. Bildirim süresi her biri için §6.6'da (kısıtlama ve harcamayı durduran ödeme hatası: acil) |
| 2 | 1 dk | Kampanyalar tablosu, "Dün" | Harcama ÷ günlük bütçe. Kampanya bütçesi açıksa oranı **kampanya** düzeyinde oku, değilse **set** düzeyinde. Kampanya bütçesinde setin oranı anlamsızdır | **< 0,5 iki gün üst üste** (*sezgisel*) → önce teklif türüne bak. **En yüksek hacim** (hedefsiz) → teslim sorunu: ret, dar kitle, ödeme, harcama sınırı (Kurulum Kılavuzu §10). **Hedefli teklif** (sonuç başına ücret hedefi, ROAS hedefi, teklif üst sınırı) → büyük olasılıkla teklif kısıtı, teslim sorunu değil. Günlüğe not et; hedefi %5–10 gevşetme kararı haftalık oturumda verilir (Modül 02 §7, Modül 06 §4.4). **Toplam bütçe** → günlük oran kullanılmaz. Meta toplam bütçeyi yayın süresine eşit yaymaya çalışır, iyi günlerde fazla, zayıf günlerde az harcar [RESMİ]. Kalan bütçe ÷ kalan gün ile kıyasla. **Üst sınır:** tek gün 1,75 kata kadar normal, takvim haftasında tavan 7 kat [RESMİ] (Kurulum Kılavuzu §5.2). Reklam seti bütçe paylaşımı açıksa set için günlük tavan 2,10 kat, haftalık tavan 8,4 kattır; kampanya toplamı yine 7 katı geçmez [RESMİ] |
| 3 | 1 dk | Olay Yöneticisi / WhatsApp etiketleri / CRM | Ana olay son 24 saatte akıyor mu? | Olay 24 saattir sıfır, sipariş sürüyor → entegrasyon kopmuş. Aynı gün düzelt, kampanyaya dokunma (Modül 05 §10.2; §6.5 satır "ölçüm kopması"). Bildirim: §6.6 "aynı gün" |
| 4 | 1 dk | Açılış sayfası, ürün, stok | Bağlantı, fiyat ve kampanya reklamla aynı mı? Stok var mı? | Yanlışsa yalnız o reklamı ya da seti durdur (Modül 06 §5.2, Aşama 0), günlüğe yaz, müşteriye bildir (§6.6: yanlış fiyat acil, stok ve bağlantı aynı gün) |
| 5 | 1 dk | Business Suite gelen kutusu, reklam yorumları, kural bildirimleri | Yanıtsız mesaj, şikâyet, spam, K-3X bildirimi. Kural "harcanan tutar" koşulunu desteklemiyorsa K-3X elle kontrol edilir (Modül 06 §5.4) | **K-3X:** Reklam Modül 06 §5.2 Aşama 4 koşulunu **tam** sağlıyorsa kapat. Koşul: kontrol noktasındaki harcama (3×, 5× ya da 6,3× hedef CPA) geçileli ≥ 72 saat ve dönüşüm o noktanın sınırını aşmıyor (3×'te 0, 5×'te ≤ 1, 6,3×'te ≤ 2). Oradaki istisnalardan biri de olmamalı: reklam setin bütçesinin çoğunu alıyor (Modül 06 §8), haftada 10'dan az dönüşüm var ya da iş mesaj ve formla satıyor (Modül 06 §6.5). Kapatınca günlüğe yaz, haftalık raporda bildir. Koşul eksikse ya da istisna varsa yalnız not et, karar haftalık oturumda verilir (§0.2 istisnası). **Mesaj:** Meta okunmamış mesajlara **5 saat** içinde yanıt öneriyor [RESMİ]; gecikme varsa müşteriye bildir. Spam ve hakaret gizlenir, gerçek şikâyet yanıtlanıp müşteriye iletilir (*sezgisel*) |

### 2.2 Günlük kontrolde yapılmayanlar

- Dünkü CPA'ya göre bütçe, teklif ya da kitle değiştirme. Dünün dönüşümleri birkaç gün daha değişebilir (Modül 05 §5.4).
- Saatlik ya da günlük kırılıma bakıp karar verme [RESMİ] (Modül 06 §0).
- Fırsat puanı tavsiyesini kontrolsüz uygulama. Puan performansı ölçmez [RESMİ]. Tavsiyeyi haftalık oturumda değerlendir.

### 2.3 Günlük kayıt satırı

Sorun yoksa da tek satır yaz. Haftalık raporda "sorunsuz gün" sayısı böyle çıkar.

`19.10.2026 | Günlük | Uyarı yok · Harcama/bütçe 0,98 · Purchase akıyor · Stok tamam · 2 mesaj yanıtlandı | Eylem yok`

---

## 3. Haftalık rutin (pazartesi, ~60 dakika)

| Adım | Süre | Yap | Referans |
|---|---|---|---|
| 1 | 5 dk | Ölçüm alarmlarını ve mutabakatı kontrol et: K, P, V, MER. Önceki haftanın rakamlarını yeniden çek (§1). Form kullanılıyorsa CRM aktarımını kontrol et. Aktarım yoksa haftanın formlarını §4.4'teki kurala göre müşterinin sistemine indir. İndirilebilen form verisi yalnız son 90 günü kapsar [RESMİ] | Modül 05 §5.5, §10.2 |
| 2 | 15 dk | Teşhis: yayın sütunu, taban kartı, metrik zinciri, gürültü payı | Modül 07 §7 |
| 3 | 10 dk | Karar: kapatma, ölçekleme, bekleme. Açık testlere dokunma | Modül 06 §4.4, §5, §6, §11 |
| 4 | 5 dk | Kreatif hattı: yorgunluk belirtileri, sonraki parti, müşteriden beklenen dosyalar | Modül 04 §8–9 |
| 5 | 10 dk | Rakip taraması: her hafta listedeki 2–3 rakip, iki haftada tüm liste | §7 |
| 6 | 10 dk | Raporu yaz, 12:00'ye kadar gönder | §5 |
| 7 | 5 dk | Değişiklikleri **tek oturumda** uygula, günlüğe yaz, kontrol tarihlerini koy | §8; Modül 06 §10 |

**Sıra kuralı:** Önce ölçüm, sonra teşhis, sonra karar. Adım 1'de olay kopması varsa ya da Modül 05 §10.2'deki dondurma koşulu oluşmuşsa bütçe kararı verilmez. Dondurma koşulu: P > 1 iki hafta üst üste ya da 28 günlük toplamda **ve** düzeltilmiş K de 1'in üstünde. Rapor "Kırmızı: ölçüm" diye gider. Tek haftalık P > 1 alarm değildir, bir hafta daha izlenir (Modül 05 §10.2).

---

## 4. Aylık ve çeyreklik rutin

### 4.1 Aylık sıra

1. **Fatura mutabakatı** (§4.2).
2. **Erişim ve güvenlik denetimi** (§4.3). Denetimi müşteri yapar; YZ listeyi hazırlar ve sonucu kaydeder.
3. **Veri yedeği** (§4.4).
4. **Taban kartını yenile** (Modül 07 §6.1) ve hedefleri ölçülen oranlarla güncelle (Modül 01 §5.2).
5. **Sezon takvimi:** önümüzdeki 6 haftada zirve var mı? (Modül 06 §9.1)
6. **Derin rakip analizi** ve kendi markanın kütüphane kontrolü (§7.6).
7. **Günlüğü Meta'nın öğe geçmişiyle karşılaştır** [PRATİSYEN; menü adı ve saklama süresi resmi kaynakta yok]. Günlükte olmayan bir değişiklik varsa kimin yaptığını bul: müşteri mi, yetkisiz erişim mi? (*sezgisel*) Yetkisiz erişimden şüpheleniyorsan §6.5'teki "tüm hesabı durdurma" satırını ve §6.6'daki acil süreyi uygula (Modül 09 §6.4). Kişi ya da ortak çıkarma işini müşteri yapar.
8. **Aylık raporu** gönder (§5.5).

### 4.2 Fatura mutabakatı (*türetilmiş*)

```
Beklenen fatura (vergi numarası var) = aylık panel harcaması × 1,05
Beklenen fatura (vergi numarası yok) = aylık panel harcaması × 1,05 × 1,20  (= × 1,26)
Fark oranı = (Faturalar ve ödemeler toplamı − beklenen fatura) ÷ beklenen fatura
```

- Fatura toplamı: **Faturalar ve ödemeler → Ödeme hareketleri** (Kurulum Kılavuzu §9).
- **Fark ≤ %2** → normal (*sezgisel*). Panelde harcanan tutar 48 saate kadar tahmini kalabilir [RESMİ].
- **Fark > %2** → sırayla bak: ödeme tarihi ay sınırını aştı mı, kupon ya da iade var mı, aynı kartı başka hesap kullanıyor mu? Tanımadığın hareket varsa Kurulum Kılavuzu §9 ve §11.
- Makbuzdaki ücretlendirilen tıklama sayısının paneldekinden farklı olması normaldir [RESMİ].

### 4.3 Erişim ve güvenlik denetimi [RESMİ]

**Kim yapar:** Bu denetimi **müşteri**, yani portfolyonun tam kontrolüne sahip kişi yapar. Kişi listesini dışa aktarmak, kişi ya da ortak çıkarmak ve portfolyodaki bir reklam hesabını dondurmak tam kontrol ister [RESMİ]. YZ ortak erişimle çalışır (§0.7), bu işler ona kapalıdır (§6.5). **YZ'nin işi:** Listeyi hazırlar ve müşteriyle birlikte geçer. Sonucu tarihiyle `ERISIM` sekmesine yazar, eksikleri müşteriye tek mesajla iletir. Bir sonraki ay eksiklerin kapanıp kapanmadığına bakar. Müşteri denetimi yapmadıysa aylık raporun "hesap sağlığı" bölümüne risk olarak yazılır (*sezgisel*).

Müşteri Ayarlar → Kişiler'den izin listesini dışa aktarır. Listede kişilerin son etkinlik tarihi ve iki faktörlü doğrulama durumu da var. Daveti henüz kabul etmemiş kişiler bu listede çıkmaz. Tam kontrolü olan kişi Güvenlik Merkezi'ndeki tavsiyelere de bakabilir. Kontrol listesi (müşteri işaretler, YZ kaydeder):

- [ ] Son **90 gün** girmeyen kişi yok; varsa, özellikle tam kontrolü olanı, müşteri çıkarır.
- [ ] İki faktörlü doğrulaması ya da geçiş anahtarı olmayan kişi yok.
- [ ] İşletmeyle ilgisiz, herkese açık e-posta alan adlı kullanıcı yok.
- [ ] Tam kontrolde en az **2** aktif kişi var (Meta'nın tavsiyesi, zorunlu değil); bu sayı ideal olarak **10**'u geçmiyor.
- [ ] Ortaklar listesinde yalnız bugün çalışılan ajans ya da danışman var.
- [ ] İşletme geçmişi dosyası indirildi; kişi, varlık ya da portfolyo bilgisinde beklenmeyen değişiklik yok.
- [ ] **Eş onayı** açık mı? Bu ayar reklam yayına çıkmadan önce güvenilir bir kişinin onayını şart koşar ve izinsiz reklama karşı ikinci bir katmandır. Açılırsa YZ'nin hazırladığı reklamlar da müşteri onayıyla yayına girer, yani §6.5'te "YZ kendisi yapar" denen işler yavaşlar (*sezgisel*). Kararı müşteri verir, karar `ERISIM`'e yazılır. Ayarın yeri ve kapsamı hesaba göre değişebilir.
- [ ] **Paylaşılan kredi sınırı** varsa hangi işletmelerle paylaşıldığı gözden geçirildi. Şüpheli ya da izinsiz hareketi olan işletme müşteri tarafından kaldırıldı.
- [ ] Bir yıldır reklam yayınlamayan reklam hesabı var mı? Reklam hesabı portfolyodan silinemez, yalnız dondurulabilir. Dondurmayı YZ önerir, **müşteri onaylar ve kendisi yapar.** Önce şunu bilsin: hesaptaki tüm kampanyalar durur, borç bakiyesi karttan çekilir, hesaba bağlı ödeme yöntemleri kaldırılır. Kreatif, sonuç ve fatura gibi geçmiş veriler görünür kalır. Meta'ya göre dondurulan hesap bazı durumlarda yeniden etkinleştirilemez [RESMİ].
- [ ] Bizim erişimimiz işin gerektirdiğinden geniş değil (kısmi erişim, yalnız atanan varlıklar).

### 4.4 Veri yedeği

- **Reklam Raporları** en fazla **37 ay** geriye gider [RESMİ]. Her ay sonu kampanya, set ve reklam düzeyinde raporu **.xlsx** olarak müşterinin klasörüne aktar.
- **Form verisi** son 90 günle sınırlı [RESMİ]; CRM aktarımı yoksa haftalık indir. **Kişisel veri kuralı** (*sezgisel*; hukuki görüş değildir, Modül 03 §4.4):
  - Form verisini indirmek kişisel veri işlemektir. Yazılı onay, KVKK dayanağı ve imzalı veri işleyen sözleşmesi yoksa indirme yapılmaz (§6.5).
  - Veri yalnız müşterinin kontrolündeki sisteme alınır: müşterinin CRM'i ya da Drive klasörü. YZ'nin kendi çalışma ortamında kopya kalmaz. Aktarım için geçici dosya gerekiyorsa aktarım bitince silinir.
  - Klasöre yalnız işin gerektirdiği kişiler erişir. Veri, işlendiği amaç için gereken süreden uzun tutulmaz (KVKK m.4/2-d) [MEVZUAT]. Süre aydınlatma metniyle uyumlu olmalı (*sezgisel*). Süresi dolan dosya silinir, silme tarihi `GUNLUK`'e yazılır.
- **Panolar:** 12 Ocak 2026'dan beri 7 ve 28 günlük görüntüleme pencereleri API'den gelmiyor [RESMİ] (Modül 05 §5.3).
- Çalışma kitabının (§8) ay sonu kopyası, müşterinin klasörüne.

### 4.5 Çeyreklik

Modül 06 §4.3'teki çeyrek raporu (başlangıca göre CPA/ROAS, marjinal verim, kazanan açılar), artırımlılık testi ya da katsayı güncellemesi (Modül 05 §11), yapı denetimi (set sayısı F5'i aşıyor mu?) ve sözleşmedeki hedeflerin gözden geçirilmesi.

---

## 5. Haftalık rapor şablonu

### 5.1 Kurallar

- **Uzunluk:** Bir ekran özet, bir tablo ek (*sezgisel*). İlk 5 satırda durum, karar ve müşteriden istenen iş görünür.
- **Kaynak satırı zorunlu:** dönem, saat dilimi, ilişkilendirme ayarı (varsayılan 7 gün tıklama + 1 gün görüntüleme [RESMİ], Modül 05 §5), iş sonucunun kaynağı, veri yaşı (§1).
- **Durum ışığı (*sezgisel*).** Önce tanımlar (*türetilmiş*):
  - **Gerçek CPA** = gerçek maliyet (panel × M; M = 1,05, vergi numarası yoksa 1,26) ÷ Meta'nın saydığı sonuç. Bu, Modül 01'deki "fatura CPA"dır. Maliyet düzeltilmiştir ama sonuç sayısı hâlâ Meta'nındır. Hedef ve başabaş da gerçek (fatura) cinsinden yazılır.
  - **Gerçek ROAS** = KDV hariç Meta değeri ÷ gerçek maliyet = panel ROAS ÷ (P × M). Piksel değeri KDV dahil gönderiyorsa P = 1,20, hariç gönderiyorsa 1,00. Değer modu ve V oranı için Modül 05 §5.5'e, formül için Modül 01 §3.2'ye bak. Kıyas için net başabaş ROAS = 1 ÷ katkı marjı.
  - **İş kapısı:** Meta'nın saydığı sonuçla yetinilmez, müşterinin kaydına da bakılır (§0.1). Ölçü: **kaba ek sipariş başı maliyet** = gerçek maliyet ÷ (sipariş − reklam öncesi haftalık taban). Taban yoksa **toplam MER** kullanılır, eşiği başabaş MER'dir (= 1 ÷ katkı marjı, Modül 01 §3.1). Toplam MER organik satışı da içerdiği için iyimserdir. MER başabaşın altına inmişse durum kesin kötüdür.
  - **k** = ışığın dayandığı haftalık Meta sonucu sayısı.

| Işık | Koşul |
|---|---|
| **Yeşil** | **Hepsi gerekir:** gerçek CPA ≤ hedef (ya da gerçek ROAS ≥ hedef) · iş kapısı geçiyor · açık ölçüm alarmı yok · hesap uyarısı yok · k ≥ 10 |
| **Sarı** | **Biri yeter:** hedef < gerçek CPA ≤ başabaş · tek hafta gerçek CPA > başabaş · iş kapısı geçmiyor (kaba ek sipariş başı maliyet > başabaş gerçek CPA, sipariş tabanın altında ya da tabana eşit, ya da toplam MER < başabaş MER) · Modül 05 §10.2'de açık bir alarm satırı var (ör. K < 0,70, V bant dışı) · müşteri tarafında çözülmemiş sorun (yanıt süresi, stok) · k < 10 |
| **Kırmızı** | **Biri yeter:** gerçek CPA 2 hafta üst üste > başabaş · Modül 05 §10.2'deki dondurma koşulu (§3 sıra kuralı) ya da olay akışı kopuk · hesap kısıtlı ya da harcama durdu · yasal risk (aşağıda) |

**Işık kuralları (*sezgisel*):**
- Birden çok ışığın koşulu tutuyorsa **en kötü ışık geçerlidir.**
- **k < 10** ise ışık en iyi ihtimalle sarıdır ve yanına "veri az" yazılır. CPA satırına son 4 haftanın toplamı da eklenir. Eşik, Modül 06 §5.2'deki "haftada 10'dan az dönüşüm" istisnasıyla aynıdır. Bu hacimde gürültü payı ±2 ÷ √10 ≈ ±%63'tür (Modül 07 §3.3).
- Tek haftalık P > 1 ışığı değiştirmez, rapora not olarak düşülür (Modül 05 §10.2).
- İş kapısı iki hafta üst üste geçmiyor ama Meta CPA'sı hedefteyse rapora "Meta ve iş kaydı ayrışıyor" maddesi yazılır ve artırımlılık testi önerilir (Modül 05 §11).
- **Yasal risk** şu durumlardan biridir: yayındaki reklamın ürünü ya da hizmeti Modül 09 §1 kapısında kırmızı; "sarı + hukukçu" satırında kanıt dosyasında hukukçu onayı yok (Modül 09 §8); metin Modül 09 §5.2 kurallarından birini çiğniyor (belgesiz "en/tek", rakip adı ya da logosu, eksik fiyat bilgisi, sağlık beyanı); KVKK dayanağı olmadan müşteri listesi yüklenmiş ya da veri aktarılmış (Modül 03 §4.4); Türk makamlarından bildirim gelmiş (Modül 09 §7.4). İlgili reklam hemen durdurulur (Modül 06 §5.2 Aşama 0), bildirim §6.6'ya göre yapılır.
- Önemli düzenleme haftasında ışık yine rakama göre verilir. Yanına "öğrenme" notu düşülür.

- **Gürültü notu:** Değişim ±2 ÷ √k içinde kalıyorsa (k = haftalık olay sayısı) rapora "gürültü içinde" yaz (Modül 07 §3.3). "İyileşti/kötüleşti" deme.
- **Kişisel veri yok.** Raporda yalnız toplamlar olur. Müşteri adı, telefon ya da form satırı rapora girmez (*sezgisel*; KVKK için Modül 03 §4.4, hukuki görüş değildir).

### 5.2 Boş şablon

```
HAFTALIK RAPOR — [Müşteri] — [gg.aa]–[gg.aa.yyyy] (pazar–cumartesi, Europe/Istanbul)
Kaynaklar: Meta [ilişkilendirme ayarı] · İş sonucu: [panel/CRM] · Veri yaşı: [2] gün · Hazırlayan: [ ] · Gönderim: [tarih saat]

1. DURUM: [Yeşil/Sarı/Kırmızı] — [tek cümle: ne oldu, neden]
2. KARAR: [bu hafta yapılacak tek ana eylem + tarih]  ·  Kapı: [Modül 06 §6.1 maddeleri ✓/✗]
   SİZDEN: [iş + son tarih]

3. İŞ SONUCU (doğruluk kaynağı)
   Sipariş/talep: [ ] (önceki [ ], reklam öncesi taban [ ])  ·  KDV hariç ciro: [ ]  ·  MER: [ ] (başabaş [ ])
   Kaba ek sipariş başı maliyet: [ ] (başabaş [ ])
4. META SONUCU
   Panel harcama [ ] / gerçek maliyet [ ]  ·  Sonuç [ ]  ·  Gerçek CPA [ ] = gerçek maliyet ÷ sonuç (hedef [ ], başabaş [ ])
   Önceki haftaya göre (aynı veri yaşıyla): CPM [ ] · CTR [ ] · CVR [ ] · CPA [ ]  ·  Gürültü payı ±[ ]
   Ölçüm: K [ ] · P [ ] · V [ ]
5. NEDEN: [en fazla 3 madde; zincirin hangi halkası]
6. YAPTIKLARIMIZ (günlükten): [tarih – değişiklik – sebep]
7. TESTLER: [açık/biten, karar tarihi]
8. GELECEK HAFTA: [eylem – tarih – geri dönüş koşulu]
   Bütçe kontrolü (§6.5): ay sonu fatura tahmini [ ] TL ≤ onaylı [ ] TL
9. RİSKLER: [sezon, stok, ölçüm, politika]
EK: kampanya ve reklam tablosu
```

### 5.3 Doldurulmuş örnek (*varsayım*)

Modül 06 §6.2 merdivenindeki ve §10 günlüğündeki mağaza: kadın elbisesi, `2610_SAT_WEB_TUM_HAC-SB_Elbise_TR`, 1.500 TL/gün, yayın 19.10.2026. Hedef panel CPA 250 TL (gerçek 262,5), panel başabaş 340 TL (gerçek 357). KDV dahil ortalama sepet 936 TL, KDV hariç 780 TL.

```
HAFTALIK RAPOR — Elbise mağazası — 25.10–31.10.2026 (pazar–cumartesi, Europe/Istanbul)
Kaynaklar: Meta 7 gün tıklama + 1 gün görüntüleme · İş sonucu: mağaza paneli · Veri yaşı: 2 gün · Gönderim: 02.11.2026 09:30

1. DURUM: YEŞİL — Satış başı gerçek maliyet 207 TL; hedef 262,5 TL, zarar sınırı 357 TL.
   Mağaza kaydı da aynı yönde: kaba ek sipariş başı maliyet 224 TL < 357 TL.
   26.10'da eklenen 11.11 reklamları öğrenmeyi yeniden başlattı, sonuçlar etkilenmedi.
2. KARAR: Bugün 10:30'da bütçe 1.500 → 1.875 TL/gün (+%25).
   Kapı (Modül 06 §6.1): son 7 tam günde 53 satış, öğrenme bitti ✓ · gerçek CPA ≤ hedef ✓
   · harcama/bütçe 10.460 ÷ 10.500 = 1,00 ✓ · ölçüm normal, stok var ✓
   · son önemli düzenleme 26.10 10:15 → 168 saat 02.11 10:15'te doldu ✓
   SİZDEN: Kara Cuma indirim oranı ve stok listesi, 05.11 Perşembe 17:00'ye kadar
   (kreatifler 13.11'de dondurulacak, Modül 06 §9).

3. İŞ SONUCU (mağaza paneli)
   Sipariş: 128 (önceki hafta 118; reklam öncesi 4 haftalık ortalama 79)
   KDV hariç ciro: 99.840 TL · MER: 99.840 ÷ 10.983 = 9,1
   Reklam öncesine göre fark: +49 sipariş → kaba ek sipariş başı maliyet 10.983 ÷ 49 = 224 TL
4. META SONUCU
   Panel harcama 10.460 TL / gerçek maliyet 10.983 TL · Satış 53 · Gerçek CPA 207 TL (panel 197)
   Önceki haftaya göre (18–24.10, 6 gün; 26.10 anlık rakamı, iki hafta da veri yaşı 2 gün):
   CPM 118,0 → 110,1 (−%7) · CTR %1,64 → %1,60 (−%2)
   CVR %3,52 → %3,49 (−%1) · Panel CPA 204,5 → 197,4 (−%3,5)
   Gürültü payı: satış ±%27, tıklama ±%5 → CTR, CVR ve CPA değişimi gürültü içinde
   Son 2–3 günün satışları eksik olabilir; CPA olgunlaştıkça düşebilir.
   18–24.10'un güncel rakamı (veri yaşı 9 gün) RAPOR sekmesinde.
   CPM için taban henüz yok (geçici taban 4. haftada, Modül 07 §6.5)
   Ölçüm: K 0,95 · P 0,41 · V 1,00 → normal
5. NEDEN: Anlamlı değişim yok. 28.10 öğleden sonra ve 29.10 resmî tatil; etkisi ayrıca izlenmedi.
6. YAPTIKLARIMIZ: 26.10 10:15 — 3 reklam eklendi (11.11 sürümü), önemli düzenleme, SEZON-1111.
7. TESTLER: Açık test yok. 11.11 reklamları gözlemde; her biri 750 TL'ye (3 × hedef CPA) ulaşınca değerlendirilecek.
8. GELECEK HAFTA: 02.11 bütçe adımı → 05.11 ilk kontrol, 09.11 karar.
   09.11'de marjinal CPA aynı veri yaşıyla hesaplanacak: 01–07.11 ile bu raporun 25–31.10 rakamı.
   Ek satışların maliyeti 340 TL'yi (panel) aşarsa 1.500 TL'ye dönülecek.
   Bütçe kontrolü (onaylı tutar faturadır, §6.5): (1.500 + 1.875 × 29) × 1,05 ≈ 58.670 TL ≤ 60.000 TL ✓
   09.11'de ikinci adım (2.350 TL/gün) ay toplamını ≈ 69.640 TL'ye çıkarır → önce yazılı onay.
9. RİSKLER: Kasım'da CPM artışı beklenir. 03.11'de ücretsiz kargo eşiği 750 → 500 TL oluyor.
   Bütçe adımıyla aynı haftaya düştüğü için iki etki birbirinden ayrılamayabilir.
```

**Ek tablo (reklam düzeyi):**

| Reklam | Harcama | Satış | Panel CPA | Not |
|---|---|---|---|---|
| `K017_VID-9x16_sorun-cozum_ofis-calisani_v2` | 4.900 | 27 | 181 | En büyük pay; kapatma adayı değil (Modül 06 §8) |
| `K012_IMG-4x5_urun-fiyat_v1` | 3.100 | 15 | 207 | Hedefte |
| `K019_CAR_kombin_v1` | 1.700 | 8 | 213 | Hedefte |
| `K021–K023` (11.11, 26.10'dan beri) | 760 | 3 | 253 | Erken; reklam başı ~250 TL < 750 TL |
| **Toplam** | **10.460** | **53** | **197** | |

**Hesap notu (*türetilmiş*):** Sipariş başı katkı = başabaş gerçek CPA = 357 TL (Modül 02 F2). Kaba net etki = 49 × 357 − 10.983 ≈ **+6.500 TL**. Ek siparişin tamamının reklamdan geldiği varsayılır; tatil ve sezon ayıklanmadı. Yalnız "kaba" ibaresiyle kullan; artırımlılık için Modül 05 §11.

**Zamanlama notu:** Bütçe adımı 10:30'da atılır. 26.10 10:15'teki önemli düzenlemenin 168 saati 02.11 10:15'te dolar (Modül 06 §6.1; aynı adım Modül 06 §10 günlüğünde 4. satır). 168 saat dolmadan atılan adım, öğrenme sürerken ölçekleme olur. Her ölçek kararında kapı satırını raporda göster.

### 5.4 Kısa mesaj sürümü (WhatsApp ya da e-posta gövdesi)

> Merhaba [ad], 25–31 Ekim özeti: **Durum yeşil.** Reklamdan gelen 53 satışın her biri bize gerçekte 207 TL'ye mal oldu. Hedefimiz 262 TL, zarar sınırımız 357 TL. Mağaza panelinde 128 sipariş var; reklam öncesi haftalık ortalama 79'du. Bugün günlük bütçeyi 1.500'den 1.875 TL'ye çıkarıyoruz. Kasım faturası tahminen 58.700 TL olur, onayladığınız 60.000 TL'nin içinde. 9 Kasım'da ek satışların maliyetine bakıp kararı kesinleştireceğiz. **Sizden ricamız:** Kara Cuma indirim oranı ve stok listesi, 5 Kasım Perşembe 17:00'ye kadar. Ayrıntılı rapor ekte.

### 5.5 Aylık rapora eklenecekler

1. **Ay hedefi ve gerçekleşen** (sipariş, ciro, MER, gerçek CPA) ve sapmanın sebebi.
2. **Birim ekonomisi:** ölçülen CVR, sepet, iade; başabaş değişti mi? (Modül 01 §3)
3. **Kreatif karnesi ve test defteri:** kazanan/kaybeden açılar, öğrenilen ders, gelecek ayın partisi ve hipotezleri (Modül 04 §8, Modül 06 §1).
4. **Rakip notu** (§7) ve **hesap sağlığı** (güvenlik denetimi, fatura mutabakatı, retler).
5. **Gelecek ay bütçesi**, üç senaryo: koru / +%20–30 / geri çek. Her birinde beklenen sonuç aralığı, gerçek maliyet (× 1,05; vergi numarası yoksa × 1,26) ve geri dönüş koşulu. Onay fatura tutarıyla istenir (§6.5 bütçe kontrolü). Senaryo marjinal CPA'ya dayanır (Modül 02 F7), söz değildir.

### 5.6 Meta Reklam Raporları ile otomatikleştirme [RESMİ]

- **Kayıtlı rapor:** Reklam Raporları'nda özel ya da şablondan rapor oluştur. Sütunları Modül 07 §2.3 ön ayar sırasıyla seç, kampanya ve reklam kırılımı ekle, kaydet.
- **E-posta planı:** Raporu aç; rapor adının yanındaki bağlantıdan ya da çark simgesinden e-posta planlamayı aç, abone ekle, kaydet. Menü yeri sürüme göre değişir. **Hesaplar arası rapor e-postayla planlanamaz.**
- **Erişim ve paylaşım:** Raporu açmak için reklam hesabında yönetici, reklamveren ya da analist erişimi gerekir. Erişimi olmayana **.xlsx/.csv** gönder. Bağlantıyla paylaşırken son tarih koy; işletme dışındakiler raporu düzenleyemez.
- **Sınır (*sezgisel*):** Otomatik e-posta yalnız Meta rakamını taşır, müşteri raporunun yerine geçmez.
- **Pano:** Looker Studio'da Google'ın kendi Meta bağlayıcısı yok, ücretli iş ortağı bağlayıcıları var [PRATİSYEN]. Rakamlar Reklam Yöneticisi'ni tutmazsa ilişkilendirme aralığını, saat dilimini ve dönüşüm tarihi seçimini eşitle [RESMİ].

---

## 6. Müşteriye açıklama dili

### 6.1 İlkeler (*sezgisel*)

1. **Önce sonuç, sonra sebep, sonra eylem.** Her mesajda en fazla bir karar ve bir talep olur.
2. **Rakamı kıyasla ver:** "207 TL" değil, "207 TL; hedef 262, zarar sınırı 357".
3. **Ölçtüğünü ve tahmin ettiğini ayır:** "ölçtük", "büyük ihtimalle", "bilmiyoruz, şu tarihte öğreneceğiz".
4. **Yuvarla ve jargonu açıkla.** ROAS 4,37 değil "yaklaşık 4,4". Terimi ilk kullanımda §6.2'deki gibi açıkla.
5. **Söz verme:** "garanti", "kesin kâr" yok (Modül 01 §5.1). Meta hedefe uyulacağını garanti etmiyor [RESMİ].
6. **Kanıtsız suçlama yok:** "Algoritma bozuldu" deme; kanıt varsa ekran görüntüsünü ekle.

### 6.2 Sözlük: metrik → müşteri dili

| Terim | Müşteriye böyle söyle |
|---|---|
| Harcanan tutar / gerçek maliyet | Panelde görünen reklam parası. Faturaya %5 konum ücreti eklenir: panel × 1,05 (vergi numarası yoksa × 1,26) |
| CPA / sonuç başına ücret | Bir satışı ya da talebi kazanmak için ortalama ödediğimiz reklam parası |
| Başabaş CPA | Bir satıştan kalan katkı. CPA bunun üstüne çıkarsa o satış reklam yüzünden zarar ettirir |
| ROAS | Reklama harcanan her 1 TL karşılığında Meta'nın saydığı satış tutarı |
| MER | Toplam cironun toplam reklam maliyetine oranı. Reklamsız satışlar da içindedir |
| CPM | Reklamın 1.000 kez gösterilmesinin fiyatı. Açık artırmada rekabetle değişir |
| CTR | Reklamı görenlerin yüzde kaçının tıkladığı |
| Sıklık | Bir kişinin reklamı ortalama kaç kez gördüğü |
| Öğrenme aşaması | Büyük bir değişiklikten sonra sistemin kime göstereceğini yeniden öğrendiği dönem. Sonuçlar oynak olur |
| İlişkilendirme | Meta'nın bir satışı reklama yazma kuralı: reklama tıkladıktan sonraki 7 gün ya da gördükten sonraki 1 gün |
| Artırımlılık | Reklam olmasaydı gelmeyecek satışlar |

### 6.3 Durum kalıpları

**A. Kötü hafta**
> Bu hafta satış başı maliyet [X] TL oldu. Hedefimiz [H], zarar sınırımız [B] TL. Sebep büyük ihtimalle [halka + kanıt]. [Bu ilk hafta / ikinci hafta] olduğu için [eylem: bekliyoruz, yeni reklam ekliyoruz, bütçeyi [ ] TL'ye çekiyoruz]. [Tarih]'te yeniden bakacağız.

**B. Bütçe artışı önerisi**
> Son [2] haftada satış başı gerçek maliyet [X] TL, hedef [H] TL. Günlük bütçeyi [A]'dan [Y] TL'ye çıkarmayı öneriyoruz. Bu ay faturaya eklenecek tutar yaklaşık [(Y − A) × ayın kalan günü × M] TL (M = 1,05; vergi numarası yoksa 1,26). Ay sonu fatura tahmini [ ] TL, onayladığınız tutar [ ] TL (§6.5 bütçe kontrolü). Meta tek bir günde bütçenin %75'ine kadar fazla harcayabilir, bu yüzden rakam üst sınır değil tahmindir. Ek satışlar daha pahalıya gelebilir. [Tarih]'te ek satışların maliyetine bakacağız; [B] TL'yi aşarsa eski bütçeye döneceğiz. Onayınızı [tarih]'e kadar rica ederiz.

**C. Kaybeden test**
> [Hipotez] testini bitirdik. [Açı/ayar] beklediğimiz sonucu vermedi: [sonuç, güven]. Bütçeyi [kazanan]'a kaydırdık. Bu sonuç bir daha aynı denemeye para harcamamızı önlüyor.

**D. Ret ya da kısıtlama**
> [Tarih saat]'te [reklam/hesap] Meta tarafından [reddedildi/kısıtlandı]. Gerekçe: [Meta'nın yazdığı]. Etkisi: [ ]. Yaptığımız: [düzeltme / inceleme talebi]. Süreyi Meta belirliyor; [tarih]'te bilgi vereceğiz. Bu konuda gelen mesajlardaki bağlantılara tıklamayın (Kurulum Kılavuzu §11).

**E. Bizim hatamız**
> [Tarih]'te [hata] yaptık. Etkisi: [süre, tutar]. [Saat]'te düzelttik. Tekrar etmemesi için [kontrol] ekledik.

Bu kalıp §6.6'daki süreyle (24 saat) ve tutarıyla gönderilir. Hata küçükse de saklanmaz (*sezgisel*).

**F. Müşteriden talep**
> [Ne] gerekiyor. Amacı: [ne için]. Son tarih: [gün saat]. Gecikirse: [sonuç, ör. "Kara Cuma reklamları eski fiyatla açılır"].

### 6.4 Sık müşteri cümleleri (Modül 01 §5.3'e ek)

| Müşteri der ki | Yanıt |
|---|---|
| "Reklamımı hiç görmüyorum." | Meta reklamı işlem yapma olasılığı yüksek kişilere gösterir; siz bu kitlede olmayabilirsiniz. Yayında olduğunu Reklam Kütüphanesi'nde gösterelim (§7). Kendi reklamınızı tıklamayın, sonuçları karıştırır (*sezgisel*) |
| "Beğeni ve takipçi artmıyor." | Kampanya satış için kuruldu. Raporda satış, maliyet ve paneldeki sipariş var |
| "Rakip her yerde, biz yokuz." | Rakibin aktif reklamlarını kütüphaneden inceleyelim. Görünürlük satış demek değil; rakibin sonucunu kütüphane göstermez |
| "Neden her gün değiştirmiyorsunuz?" | Büyük değişiklik öğrenmeyi yeniden başlatır [RESMİ]. Kararlar haftalık; kontrol her sabah |
| "Başka ajans ROAS garanti ediyor." | Meta, hedefli teklifte bile hedefin tutacağını garanti etmiyor [RESMİ]. Biz eşikleri, süreci ve zarar sınırında durmayı taahhüt ediyoruz |
| "Olumsuz yorumu silin." | Spam ve hakareti gizliyoruz. Gerçek bir şikâyeti silmek yerine yanıtlamayı ve size iletmeyi öneriyoruz (*sezgisel*) |

### 6.5 Yetki matrisi (*sezgisel*; sözleşmede aksi yazmıyorsa)

| Eylem | YZ kendisi yapar | Bildirim | Önce onay |
|---|---|---|---|
| Reklam kapatma, Modül 06 §5.2 Aşama 1–4 (Aşama 4 günlük kontrolde de, §2.1 satır 5) | Evet | Haftalık raporda | — |
| Bütçe adımı ya da geri çekme, aşağıdaki bütçe kontrolü tutuyorsa | Evet | Haftalık raporda | — |
| Bütçe kontrolünü tutmayan artış | — | — | **Yazılı onay** (§6.3-B) |
| Onaylı mesaj ve teklif kapsamında yeni kreatif | Evet | Haftalık raporda | — |
| Yeni fiyat, indirim, garanti, "en/tek" iddiası, sağlık ifadesi, müşteri yorumu ya da UGC kullanımı | — | — | **Onay** (Modül 04 §5.3, Modül 08 §1.1) |
| Yeni kampanya, pazar, dil ya da dönüşüm konumu | — | — | **Onay** |
| **(a) Ölçüm kopması** (hesap geneli: olay 24 saattir sıfır, sipariş sürüyor) | Evet: entegrasyonu aynı gün düzeltir. **Kampanyaya dokunmaz** (Modül 05 §10.2) | §6.6, aynı gün | — |
| **(b) Yanlış fiyat, stok bitti, kırık bağlantı** ya da tek reklamın açılış sayfasında izleme yok | Evet: **yalnız ilgili reklamı ya da seti** durdurur (Modül 06 §5.2 Aşama 0). 7 gün ya da daha uzun duran setin öğrenmesi baştan başlar [RESMİ] (Modül 06 §5.3); durma uzayacaksa müşteriyle konuşur | §6.6: yanlış fiyat acil, diğerleri aynı gün | — |
| **(c) Tüm hesabı durdurma** (bütün kampanyaları kapatma): yalnız şüpheli erişim ya da izinsiz harcama | Evet, acil (Modül 09 §6.4, Kurulum Kılavuzu §11). Reklam hesabını dondurmak bu değildir, onu müşteri yapar (§4.3) | §6.6, acil (1 saat) | — |
| Tüm hesabı durdurma: performans ya da başka gerekçe | — | — | **Onay** |
| Reddedilen reklam için düzeltme ya da inceleme talebi | Evet | §6.6, aynı gün | — |
| Kimlik doğrulaması isteyen hesap itirazı | — | — | Müşteri kendisi yapar |
| Müşteri listesi ya da form verisi **yükleme, paylaşma ya da indirme** | — | — | **Yazılı onay**, bir kez ve kapsamıyla (hangi veri, nereye, kim erişir, ne kadar saklanır) + KVKK dayanağı + veri işleyen sözleşmesi (§4.4, Modül 03 §4.4) |
| Ödeme yöntemi, kişi ya da ortak erişimi, portfolyo ayarları, reklam hesabını dondurma, eş onayı, kredi sınırı paylaşımı | **Asla** | — | Yalnız müşteri yapar (§4.3) |

**Bütçe kontrolü (*türetilmiş*).** Onaylı aylık bütçe her zaman **fatura tutarıdır**, yani gerçek maliyettir; panel tutarı değildir. Her bütçe adımından önce şunu hesapla:

```
(Bu ayın düne kadarki panel harcaması + yeni günlük bütçe × ayın kalan günü, bugün dahil) × M ≤ onaylı aylık tutar
M = 1,05 (vergi numarası girili) | 1,26 (girili değil)
```

Eşitsizlik tutmuyorsa adım yazılı onay ister. Hesap tahmindir, üst sınır değildir. Meta tek bir günde bütçenin %75'ine kadar fazla harcayabilir; haftalık 7 kat tavanı pazar–cumartesi haftasına bağlıdır ve ay sınırında bölünür [RESMİ] (Kurulum Kılavuzu §5.2). Tahmin onaylı tutara çok yakınsa pay bırak (*sezgisel*). Örnek: §5.3 madde 8.

### 6.6 Bildirim ve yanıt süreleri (*sezgisel*)

Bildirim sürelerinin tek kaynağı bu tablodur; §0, §2.1, §6.3 ve §6.5 buraya atıf yapar.

- **Süre tespitten itibaren işler.** Tespit, olayın bizim tarafımızdan ilk görüldüğü andır.
- **Tespit kanalları:**
  1. Meta'nın e-posta ve uygulama bildirimleri: ret, ödeme, kısıtlama, hesap uyarısı. Bildirimin kime ve hangi kanaldan gittiği bildirim ayarlarına göre değişir (*hesaba göre değişebilir*). Bildirimdeki bağlantıya tıklanmaz, durum adres elle yazılarak doğrulanır (Kurulum Kılavuzu §11).
  2. Otomatik kural bildirimleri (Modül 06 §5.4).
  3. Hesaba Genel Bakış ve İşletme Desteği Ana Sayfası uyarıları (günlük kontrol, §2.1 satır 1).
  4. Müşteriden gelen haber: sipariş durdu, site çöktü, stok bitti.
  5. Günlük kontrolün kendisi (§2).
- **1 saatlik süre**, olay 1–4 numaralı bir kanaldan geldiğinde ve o kanal izlendiğinde anlamlıdır. Kanal izlenmiyorsa olay en geç bir sonraki günlük kontrolde yakalanır ve süre o anda başlar: bildirim o sabah en geç 11:00'de gider.
- **Hafta sonu ve resmî tatil:** Günlük kontrol bu günlerde de yapılır. "Acil" ve "aynı gün" sınıfları aynen geçerlidir. Müşteriden iş bekleniyorsa (ör. kart güncelleme) ilk iş günü sabahı hatırlatılır.
- **İş günü:** Sözleşmede aksi yazmıyorsa pazartesi–cuma, resmî tatiller hariç.

| Sınıf | Durum | Bildirim süresi (tespitten itibaren) | Hafta sonu ve resmî tatil |
|---|---|---|---|
| Acil | Hesap ya da varlık kısıtlandı; harcama durdu (harcamayı durduran ödeme hatası dahil); yanlış fiyat ya da yanlış kampanya bilgisiyle yayın; şüpheli erişim ya da izinsiz harcama | 1 saat | Aynı süre |
| Aynı gün | Ölçüm kopması (Modül 05 §10.2); harcamayı durdurmayan ödeme uyarısı; reklam reddi; stok bitti ya da kırık bağlantı (reklam durduruldu); yasal risk nedeniyle durdurulan reklam (§5.1) | Tespit edilen takvim günü içinde | Aynı kural |
| 24 saat | Bizim hatamız (§6.3-E) | 24 saat | Aynı süre |
| İş günü | Müşteri sorusu | 1 iş günü | İlk iş günü |
| Rapor | Haftalık rapor | Pazartesi 12:00 | Pazartesi tatilse ilk iş günü 12:00 |
| Rapor | Aylık rapor | Ayın 5. iş günü | — |

---

## 7. Reklam Kütüphanesi ile rakip analizi

### 7.1 Türkiye'de ne görünür? [RESMİ]

Kütüphane, Meta teknolojilerindeki **tüm aktif reklamları** içerir ve herkes arama yapabilir. Ayrıntı düzeyi, reklamın gösterildiği bölgeye ve kategorisine bağlıdır.

| Bilgi | Yalnız Türkiye'de gösterilen ticari reklam | AB ya da Birleşik Krallık'ta da gösterilen reklam |
|---|---|---|
| Kreatif, metin, reklamı veren Sayfa | Evet | Evet |
| Tarih | **Yalnız başlangıç tarihi** | Yayın tarihleri |
| Reklam durduktan sonra | **Kütüphaneden düşer, arşiv yok** | Son gösterimden sonra 1 yıl arşivde |
| Harcama, gösterim aralığı | Hayır | Hayır |
| Toplam erişim, yaş ve cinsiyet kırılımı | Hayır | Evet, yalnız AB/BK kısmı için |
| Reklamverenin yaş, cinsiyet, konum seçimi | Hayır | Evet |
| Sayfa şeffaflığı (oluşturulma tarihi, ad değişiklikleri, yönetildiği ülkeler) | Evet | Evet |

- **Sosyal mesele, seçim ve siyaset reklamları** 7 yıl saklanır. Bu reklamlarda harcama ve gösterim aralığı da görünür.
- **Markalı içerik:** Ücretli ortaklık etiketli aktif içerikler 17 Ağustos 2023'ten itibaren aranabilir. Arama Sayfa ya da Instagram hesabıyla yapılır; platform ve tarih aralığı seçilir. Rakibin hangi içerik üreticileriyle çalıştığını görmek için kullan.
- **Çelişki:** Bazı pratisyen yazılar "aktif olmayan reklam 1 yıl görünür" diyor [PRATİSYEN]. Meta'nın tablosunda bu yalnız AB/BK için geçerli. Türkiye'de durdurulan ticari reklam kaybolur, bu yüzden kendi arşivini tut (§7.2, adım 5).

### 7.2 Adımlar

1. **Giriş yap.** Yaş kısıtlı kreatifler giriş yapmayana gösterilmez; sonuç indirmek de giriş ister [RESMİ].
2. **https://www.facebook.com/ads/library/** → Ülke: **Türkiye** → Kategori: **Tüm reklamlar** → Sayfa adı ya da anahtar kelime. Kelime araması, metninde ya da Sayfa adında o kelime geçen reklamları getirir [RESMİ].
3. **Rakip listesi (5–8, *sezgisel*):** brifteki rakipler (Modül 01 §1.7) ve kelime aramasından çıkanlar: ürün kategorisi, "kampanya", "indirim", ilçe adı, "randevu".
4. **Filtrele:** durum, platform, medya türü, dil, tarih [PRATİSYEN] (*arayüze göre değişebilir*). **Reklam detaylarını gör** ekranında form reklamının ayrıntıları da görünür [RESMİ]; rakibin form sorularını not et.
5. **Rakip kartını doldur** (§7.4) ve **arşivle:** ekran görüntüsü, reklam kimliği, başlangıç tarihi, ilk ve son görüldüğü tarih. TR'de durunca reklam kaybolur; yayın süresini kendi kaydın ölçer.
6. **AB/BK kontrolü:** Rakip AB'de de yayındaysa ülke seçicide bir AB ülkesi seç; erişim, yaş/cinsiyet ve hedefleme seçimleri görünür [RESMİ].
7. **Sayfa şeffaflığı:** Yeni açılmış, sık ad değiştirmiş ya da başka ülkeden yönetilen Sayfa taklit olabilir → §7.6.
8. **Hipoteze çevir:** Ayda en fazla 2 rakip kaynaklı hipotez (*sezgisel*), Modül 06 §1 kalıbıyla.

### 7.3 Okuma kuralları (eğer → ise; *sezgisel*)

| Gördüğün | Anlamı | Yap |
|---|---|---|
| Aynı reklam ≥ 30 gündür aktif | Rakip kapatmamış; kârlılık kanıtı değil | Açıyı ve teklifi aday listesine al [PRATİSYEN Foreplay, bir.ch] |
| Aynı mesajın çok sürümü | Test ya da ölçekleme olabilir (*doğrulanmalı*) | Değişen öğeyi not et: kanca, görsel, CTA |
| Son 7 günde çok yeni reklam | Kampanya dönemi başlıyor | Sezon takvimini kontrol et (Modül 06 §9) |
| Rakip fiyatı belirgin düşük | Sorun teklif olabilir | Modül 01 §6.5, müşteriyle konuş |
| Herkes aynı açıyı kullanıyor | Farklılaşma fırsatı | Farklı kavram partisi (Modül 04 §8.1) |
| Rakip WhatsApp'a, biz siteye yönlendiriyoruz | Dönüşüm konumu farkı | Konum testi adayı (Modül 08 §1.3) |
| Rakipte aktif reklam yok | Pazar boş ya da rakip bırakmış | Tek başına sonuç çıkarma |

**Bilinmeyenler:** Türkiye'de rakibin harcaması, CPA'sı ve hedeflemesi görünmez. Rapora "rakip şu kadar harcıyor" yazma.

### 7.4 Rakip kartı (doldurulmuş, *varsayım*; 17.10.2026, Türkiye, Tüm reklamlar)

| Alan | Rakip A (butik marka) | Rakip B | Rakip C |
|---|---|---|---|
| Aktif reklam | 14 | 3 | 0 |
| En eski aktif reklamın başlangıcı | 12.06.2026 (127 gün) | 02.10.2026 | — |
| Son 7 günde yeni | 5 (11.11 teması) | 0 | — |
| Format | Çoğu 9:16 video, katalog | Tek görsel | — |
| Ana açı / kanca | "Ofisten akşam yemeğine tek elbise" (kullanım) | "%40 indirim" | — |
| Teklif | 30 gün ücretsiz iade | %40 + ücretsiz kargo | — |
| Konum / CTA | Site, "Alışveriş yap" | Instagram profili ve DM | — |
| Okuma | Uzun süren kullanım açısı tutuyor olabilir. 11.11'e erken başladı | Yalnız indirim; çabuk yorulma riski | Pazarda yok ya da durmuş |
| Eylem | **H-05:** "çok kullanım" açısı, ana açıya göre CPA'yı düşürür mü? Kara Cuma sonrası, Aralık'ta test (13.11 kreatif dondurması, Modül 06 §9) | İzle | 2 hafta sonra yeniden bak |

### 7.5 Yasal sınır [MEVZUAT]

- **Kopyalama yok.** Başka bir reklamın metnini, sloganını ya da görsel kurgusunu karışıklık yaratacak biçimde taklit etmek yasak (TRHTUY md. 12). Kütüphaneden **açı ve yapı** öğrenilir, içerik kopyalanmaz.
- **Karşılaştırmalı reklam:** Rakibin adı, markası ya da logosu kullanılmaz. Kıyas nesnel ve kanıtlı olmalı (md. 8). Ayrıntı Modül 04 §5.3 ve Modül 08 §1.1'de.
- Rakibin reklamı Meta Reklam Standartları'na aykırıysa kütüphaneden şikâyet edilebilir [RESMİ]. Bunu rekabet aracı olarak kullanma; yalnız gerçek ihlali bildir (*sezgisel*).

### 7.6 Kendi markanı kontrol et (aylık)

1. Müşterinin Sayfası'nı kütüphanede aç. **Aktif reklam listesi, Reklam Yöneticisi'ndeki aktif reklamlarla aynı mı?** Fazladan reklam varsa yetkisiz erişim şüphesi var. Kurulum Kılavuzu §11'i ve §4.3 denetimini hemen uygula.
2. Marka adını kelime olarak ara. Taklit Sayfa ya da dolandırıcı reklam varsa şikâyet et, ekran görüntüsüyle müşteriye bildir.
3. Kendi reklamlarının kreatif ve metninin kütüphanede doğru göründüğünü kontrol et.

---

## 8. Değişiklik günlüğü şablonu

Standart, Modül 06 §10'daki kurallar ve sütunlardır. Burada aynı günlüğün **çalışma kitabı** düzeni ve raporlama için iki ek sütun var.

**Çalışma kitabı (Google E-Tablolar; tek dosya, müşterinin Drive'ında):**

| Sekme | İçerik | Kaynak |
|---|---|---|
| `GUNLUK` | Değişiklikler ve dış olaylar | Bu bölüm, Modül 06 §10 |
| `HIPOTEZ` | Hipotez defteri | Modül 06 §1, §10 |
| `RAPOR` | Haftada bir satır: harcama, gerçek maliyet, sonuç, CPA, K, P, V, MER, durum ışığı. Sonuç, CPA ve ROAS iki sütunda tutulur: anlık (veri yaşı 2 gün) ve güncel (veri yaşı 9 gün) | §1, §5 |
| `TABAN` | Taban kartı | Modül 07 §6.2 |
| `RAKIP` | Rakip kartları ve reklam arşivi | §7.4 |
| `ERISIM` | Varlık ve erişim envanteri, aylık denetim sonucu. Kişi adı ve e-posta içerir, yani kişisel veridir; devirde §9.2'ye göre teslim edilir | §4.3, §9 |

**`GUNLUK` başlık satırı (kopyala):**

```
No,TarihSaat,Kim,Duzey,Oge,Degisiklik_once_sonra,OnemliDuzenleme,SebepKodu,HipotezID,Beklenen,KontrolTarihi,Sonuc,Karar,MusteriyeBildirim
```

- **Ek sütunlar:** `Duzey` (Hesap / Kampanya / Set / Reklam / İşletme / Dış) ve `MusteriyeBildirim` (hangi rapor ya da mesaj, tarih).
- **Sebep kodları:** Modül 06 §10'dakiler (`H-…`, `HATA`, `SEZON`, `MUSTERI`, `DIS`; Meta değişiklikleri `DIS`), artı `OPS`: stok, kırık bağlantı, ödeme gibi operasyonel düzeltme.
- **Rapora aktarma:** Haftalık raporun 6. bölümü son 7 günün satırlarıdır. Günlük kontrol satırları (§2.3) yalnız "sorunsuz gün" sayısı olarak geçer.

**Doldurulmuş satırlar (*varsayım*; Modül 02 §10'daki tesisat işletmesi, WhatsApp):**

| No | TarihSaat | Kim | Düzey | Öğe | Değişiklik | Önemli düz.? | Sebep | Beklenen | Kontrol | Sonuç | Karar | Bildirim |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 | 02.11.2026 10:00 | YZ | Reklam | `K034`, `K035` ("60 dakikada kapınızda" açısı) | 2 reklam eklendi (haftalık oturum) | Evet | H-07 | Konuşma başına ücret ≤ 95 TL | 09.11 | — | — | 09.11 raporu |
| 2 | 03.11.2026 08:55 | YZ | Hesap | — | "Ödeme başarısız" uyarısı; değişiklik yok | — | OPS | Müşteri kartı günceller | 03.11 14:00 | 11:20'de güncellendi, yayın 2 saat durdu | Kapandı | 03.11 09:00 mesaj |
| 3 | 06.11.2026 | Müşteri | İşletme | — | Bir usta izinli, kapasite −%30 (07–13.11) | Hayır | MUSTERI | Yanıt süresi uzayabilir | 13.11 | — | Bütçe sabit, artış yok (Modül 01 §0.5) | 06.11 mesaj |
| 4 | 08.11.2026 | — | Dış | — | Yoğun yağış, su baskını haberleri | — | DIS | Talep artışı | 09.11 | Konuşma +%40 | Ölçekleme kararında kullanılmaz | 09.11 raporu |

4. satır: dış olaydan gelen sıçrama tekrar etmez, bütçe artışına gerekçe olmaz (*sezgisel*).

---

## 9. Teslim / devir kontrol listesi

### 9.1 Devralırken (yeni müşteri ya da başka ajanstan; ilk 7 gün)

**Sahiplik ve erişim**
- [ ] Müşterinin kendi işletme portfolyosu var. Tam kontrolde en az 2 kişi var [RESMİ tavsiye].
- [ ] Reklam hesabı, Sayfa, Instagram, WhatsApp Business hesabı, veri seti (Piksel) ve katalog müşterinin portfolyosunda. Başka bir portfolyodaysa not et. **Reklam hesabı portfolyolar arasında taşınamaz** (Kurulum Kılavuzu §3.5). Gerekirse müşteri portfolyosunda yeni hesap açılır.
- [ ] Erişim **ortak** olarak verildi, şifre paylaşılmadı. Yalnız gereken varlık ve görev atandı [RESMİ]. Kısa bir denetim için geçici erişim kullanılabilir: yalnız temel erişim verir, 3–75 gün sürer, süre bitince kişi otomatik çıkarılır [RESMİ].
- [ ] Finans erişimi yalnız gerekiyorsa verildi [RESMİ: ayrı bir gelişmiş seçenek].
- [ ] Alan adı doğrulaması hangi portfolyoda? (Modül 05 §2.5)

**Envanter (`ERISIM` sekmesine yaz)**
- [ ] Reklam hesabı kimliği, para birimi, saat dilimi, ödeme yönteminin sahibi, harcama sınırı.
- [ ] Aktif kampanyalar, otomatik kurallar (bildirim kime gidiyor), planlı raporlar.
- [ ] Olaylar, CAPI yolu ve barındıran taraf, EMQ (Modül 05 §2, §4); UTM ve GA4 erişimi (Modül 05 §6).
- [ ] Özel dönüşümler, özel kitleler, müşteri listeleri ve KVKK dayanakları (Modül 03 §4.4).

**Geçmiş ve ilk hafta**
- [ ] En az son 13 ayın aylık raporu dışa aktarıldı (en fazla 37 ay [RESMİ]). Önceki ekipten günlük, test sonuçları, kreatif arşivi ve ret geçmişi istendi.
- [ ] Taban kartı çıkarıldı (Modül 07 §6). Ölçüm testi ve mutabakat yapıldı (Modül 05 §2.7, §5.5).
- [ ] Operasyonel hata dışında yapı değişikliği yok; ilk hafta gözlem (*sezgisel*; değişiklik öğrenmeyi sıfırlar).
- [ ] 7. gün **devralma raporu:** bulgular, riskler, 30 günlük plan, müşteriden istenenler.

### 9.2 Devrederken (sözleşme bitti ya da yeni ekibe geçiş)

**Varlıklar**
- [ ] Bütün varlıklar müşterinin portfolyosunda. Bizim portfolyomuzda oluşturulmuş bir şey varsa (veri seti, katalog, kitle) müşteriye paylaşıldı ya da yeniden kuruldu. Veri seti bir ortağa portfolyo kimliğiyle atanabilir [RESMİ]. Sahiplik devri mümkün mü, *doğrulanmalı*.
- [ ] Ödeme yöntemi müşteriye ait.
- [ ] Kampanyalar müşteri istemedikçe durdurulmadı. Yayında boşluk kalmadı.

**Belgeler (tek klasör)**
- [ ] Çalışma kitabının tamamı (§8), tüm dönemin raporu (.xlsx), hedefler, başabaş hesabı ve brif kartı (Modül 01–02).
- [ ] Adlandırma standardı, UTM şablonu, otomatik kural listesi.
- [ ] Kreatif kaynak dosyaları ve kullanım hakları: UGC sözleşmeleri, müzik ve görsel lisansları (Modül 04 §7).
- [ ] Açık testler, planlanan değişiklikler, sezon takvimi, bilinen riskler.
- [ ] **Kişisel veri içeren her dosya** teslim edilir ya da imha edilir. Kapsam: form verisi, müşteri listesi kaynak dosyaları, CRM dışa aktarımları, `ERISIM` sekmesindeki kişi adları ve e-postaları, müşteri bilgisi görünen ekran görüntüleri. Form verisi son 90 gün indirilebilir [RESMİ], devirden önce §4.4 kuralıyla müşterinin sistemine alınır. Teslim güvenli kanalla yapılır. İmha müşterinin yazılı talimatıyla yapılır ve tarih, kapsam ve yöntemiyle kaydedilir. Bizde kopya kalmaz. Dayanak: işleme sebebi ortadan kalkan veri silinir, yok edilir ya da anonim hâle getirilir (KVKK m.7/1); veri sorumlusu, kendi adına veri işleyenle güvenlik tedbirlerinden birlikte sorumludur (m.12/2) [MEVZUAT]. İş ilişkisi bitince iade ya da imha yükümlülüğünü veri işleyen sözleşmesine yaz (*sezgisel*; Modül 03 §4.4; hukuki görüş değildir).

**Erişim kapanışı**
- [ ] Planlı e-posta raporlarından aboneliklerimiz çıkarıldı. Paylaşılan rapor bağlantılarına son tarih konuldu.
- [ ] Otomatik kural bildirimleri yeni sorumluya yönlendirildi.
- [ ] Bizim oluşturduğumuz entegrasyon ve erişim anahtarları devredildi ya da iptal edildi. CAPI'nin kopmadığı test edildi (Modül 05 §2.7).
- [ ] Müşteri bizi **Ayarlar → Ortaklar → Seçenekler → İşletme portfolyosundan çıkar** yoluyla çıkardı. Kişisel kullanıcılarımız Kişiler'den silindi [RESMİ].
- [ ] Devir notu (§9.3) iki tarafça tarihli olarak onaylandı.

### 9.3 Devir notu şablonu

```
DEVİR NOTU — [Müşteri] — [tarih]
1. Hesap durumu: aktif kampanyalar, günlük bütçe, son 4 hafta gerçek CPA/ROAS, hedef ve başabaş
2. Çalışan: [açı, kitle, yapı]     Çalışmayan: [denenmiş ve kaybetmiş olanlar]
3. Açık işler: [test – bitiş tarihi], [planlı değişiklik – tarih]
4. Riskler: [ölçüm, politika, sezon, stok, kapasite]
5. İlk 14 günde yapılmaması gerekenler: [ör. 11.11 öncesi yapı değişikliği]
6. Dosyalar: [klasör bağlantısı]    Erişim: [kim, hangi yetki, ne zamana kadar]
7. Kişisel veri: [teslim edilen dosyalar – tarih]    [imha edilen dosyalar – tarih, yöntem, talimatı veren]
```

---

## Doğrulanması gerekenler

- **Reklam Kütüphanesi filtreleri:** Meta filtre listesini yalnız siyasi reklamlar için veriyor. Ticari reklamdaki platform, medya türü, dil, durum ve tarih filtreleri pratisyen kaynaktan; arayüze göre değişebilir.
- **"Birden fazla sürüm" etiketi** ve reklam kartındaki kimlik numarası: arayüz gözlemi, resmi tanım bulunamadı.
- **Değişiklik geçmişi:** Reklam Yöneticisi'ndeki menü adı ("Geçmiş") ve saklama süresi resmi kaynakta yok; pratisyen yazı 2024 tarihli.
- **Veri seti (Piksel) sahiplik devri:** Paylaşım belgeli; portfolyolar arası sahiplik devri doğrulanmadı.
- **Planlı rapor e-postası:** Meta başkasına da gönderilebileceğini, ama rapora yalnız izinli kişilerin erişebileceğini yazıyor. İşletme dışı adreste açılıp açılmadığını dene.
- **Hesap Kalitesi'nin yeri** ve **5 saatlik mesaj yanıtı tavsiyesi:** hesaba göre değişebilir.
- **Bildirimlerin YZ'ye ulaşması (§6.6):** Meta'nın e-posta ve uygulama bildirimlerinin ortak erişimli bir kullanıcıya hangi kanaldan geldiği hesaba göre değişebilir. 1 saatlik süre ancak bu kanal izleniyorsa uygulanabilir.
- **Reklam Yöneticisi'nin dönüşüm tarihi ataması (§1):** Dönüşüm gösterim gününe mi, dönüşüm gününe mi yazılıyor? Hesapta doğrula (Modül 05 §5.4, Modül 07 §0.7). Gösterim gününe yazılıyorsa haftanın son günleri 7 güne kadar olgunlaşır.
- **Eş onayı ve paylaşılan kredi sınırı (§4.3):** Ayarların yeri ve kapsamı hesaba göre değişebilir.
- **Looker Studio:** Google'ın kendi Meta bağlayıcısının olmadığı bilgisi pratisyen kaynaklı; kapsam ve fiyat sağlayıcıya göre değişir.
- **%5 konum ücreti 2027'de:** Dijital Hizmet Vergisi 1 Ocak 2027'de %2,5'e iniyor; Meta'nın oranı değişir mi bilinmiyor. Ocak 2027'de §4.2 çarpanını yeniden doğrula (Modül 01).

---

## Kaynaklar

**[RESMİ] Meta yardım merkezi ve Şeffaflık Merkezi (Türkçe metin, erişim Ekim 2026)**
- Meta Reklam Kütüphanesi hakkında (görünen bilgi tablosu, arşiv süreleri, markalı içerik): https://www.facebook.com/business/help/2405092116183307
- Reklam Kütüphanesi: https://www.facebook.com/ads/library/
- Reklam Kütüphanesi araçları: https://transparency.meta.com/researchtools/ad-library-tools/
- Meta Reklam Raporları hakkında: https://www.facebook.com/business/help/487269218011981
- Tekrarlanan rapor ve e-posta planlama: https://www.facebook.com/business/help/596403700379202
- Raporları dışa aktarma ve paylaşma (37 ay sınırı): https://www.facebook.com/business/help/276601619787641
- Üçüncü taraf araçlarla raporlama farkları: https://www.facebook.com/business/help/511923449323749
- Reklam Yöneticisi, Reklam Raporları ve Olay Yöneticisi farkları: https://www.facebook.com/business/help/337196340694086
- Harcanan tutar neden farklı görünüyor (sonuçların işlenmesi 48 saat): https://www.facebook.com/business/help/196476577203529
- Reklam seti bütçesi paylaşımı (günlük tavan (bütçe + paylaşılan) × 1,75, haftalık × 7): https://www.facebook.com/business/help/1388266028979935
- Toplam bütçeler (harcamanın yayın süresine yayılması): https://www.facebook.com/business/help/1844835042445690
- Hesaba Genel Bakış: https://www.facebook.com/business/help/149770628855079
- Fırsat puanı tavsiye kategorileri: https://www.facebook.com/business/help/2086509315182746
- Fırsat puanı: https://www.facebook.com/business/help/804913634782260
- Süresi dolan potansiyel müşteriler (90 gün): https://www.facebook.com/business/help/1526849577619206
- Portfolyo ve varlık izinleri (tam kontrol, kısmi erişim, geçici erişim): https://www.facebook.com/business/help/442345745885606
- İzinleri değiştirme: https://www.facebook.com/business/help/299504287548592
- Ortaklar hakkında: https://www.facebook.com/business/help/695463587559330
- Ortak ekleme: https://www.facebook.com/business/help/708679622611131
- Ortak çıkarma: https://www.facebook.com/business/help/2222659621282858
- Portfolyo güvenliği en iyi uygulamaları (kişi listesini tam kontrolün dışa aktarması, eş onayı, paylaşılan kredi sınırları, Güvenlik Merkezi): https://www.facebook.com/business/help/608572510632070
- Reklam hesabını dondurma (tam kontrol gerekir; ödeme yöntemleri kaldırılır; yeniden etkinleştirme bazı durumlarda mümkün değil): https://www.facebook.com/business/help/190760004667059
- Veri setine ortak ekleme: https://www.facebook.com/business/help/889971929474339
- Veri seti ve Piksel paylaşım kontrolleri: https://www.facebook.com/business/help/352686481592916
- Hesap Kalitesi'nin yeri (not): https://www.facebook.com/business/help/1767120243598011
- Konum ücretleri: https://www.facebook.com/business/help/1238737454289085

**[MEVZUAT]**
- Ticari Reklam ve Haksız Ticari Uygulamalar Yönetmeliği, konsolide metin (md. 8, 12): https://www.mevzuat.gov.tr/File/GeneratePdf?mevzuatNo=20435&mevzuatTur=KurumVeKurulusYonetmeligi&mevzuatTertip=5
- 6698 sayılı Kişisel Verilerin Korunması Kanunu, güncel metin (m.4/2-d, m.7/1, m.12/2): https://www.mevzuat.gov.tr/mevzuatmetin/1.5.6698.pdf

**[PRATİSYEN]**
- Reklam Kütüphanesi filtreleri ve yayın süresi sinyali (15 Aralık 2025): https://bir.ch/blog/meta-ad-library
- Uzun yayın süreli kancalar: https://www.foreplay.co/post/how-to-use-hooks
- Reklam Yöneticisi değişiklik geçmişi (25 Temmuz 2024): https://www.savemyleads.com/blog/other/meta-ads-change-history
- Looker Studio'da yerel Meta bağlayıcısı olmaması (25 Haziran 2026; araç sağlayıcı): https://blog.coupler.io/facebook-ads-to-looker-studio/
- Looker Studio'ya eklenen iş ortağı Meta bağlayıcıları (26 Aralık 2025): https://ppc.land/looker-studio-adds-15-partner-connectors-in-year-end-expansion/
