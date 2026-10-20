# Meta Reklamcılık Profesyonel Paketi (Ekim 2026)

Bu tek dosya, paketin tüm modüllerini sırayla içerir. Kurulum adımları için ayrıca KILAVUZ.md kullanılır.


---

# Oku beni: Meta reklamları için yapay zekâ bilgi paketi

> **Profesyonel paket · Bilgi tarihi: Ekim 2026** (modüller 17 Ekim 2026'da yazıldı, kaynaklar 17–20 Ekim 2026'da denetlendi). Yapay zekâ platformlarının kurulum adımları **20 Ekim 2026'da** platformların yardım sayfalarından okundu. Bu dosya insan için yazıldı. Yapay zekâya yüklemen gerekmez.

## 1. Bu paket ne işe yarar?

Paket, Türkiye'de reklam veren bir işletme için Facebook, Instagram ve WhatsApp (Meta) reklamlarını yöneten bir yapay zekâ asistanı kurmana yarar. Asistan bu dosyaları okuyarak deneyimli bir performans pazarlamacısı (media buyer) gibi çalışır:

- Önce işletmeyi tanır: brif soruları sorar, eksik veriyle karar vermez.
- Zarar sınırını, hedef maliyeti ve bütçeyi formülle hesaplar. Hesabın her adımını gösterir.
- Kampanya yapısını, hedef kitleyi, kreatifi ve ölçümü karar tablolarına göre önerir.
- Yayındaki hesabın rakamlarını okuyup sorunun nerede olduğunu bulur. Kapatma, bekleme ya da ölçekleme kararını gerekçesiyle verir.
- Haftalık rapor ve müşteriye gidecek mesajı yazar.
- Meta kurallarını ve Türk mevzuatını gözetir. Riskli işte durur, seni avukata ya da mali müşavire yönlendirir.

Paket iki katmandan oluşur:

| Katman | Dosya | Ne için |
|---|---|---|
| **Talimat (beyin)** | `11-yapay-zeka-talimati.md` | Asistanın rolü, çalışma yöntemi, çıktı biçimleri ve kesin kuralları. 7.227 karakter; ChatGPT'nin 8.000 karakterlik talimat alanına sığar |
| **Bilgi (kütüphane)** | `01` … `10` modülleri + `../KILAVUZ.md` | Formüller, karar tabloları, şablonlar, kaynaklar. Toplam yaklaşık 678 bin karakter (≈ 680 KB) |

`KILAVUZ.md` (Kurulum Kılavuzu) bir üst klasördedir. Hesap açma, menü adları, ödeme ve hata kodları oradadır. Modüller bunları tekrar etmez, "Kurulum Kılavuzu §X" diye atıf yapar. Bu yüzden onu da yükle.

Modüllerdeki her sayının yanında kaynağı ya da etiketi yazar: **[RESMİ]** Meta veya Google belgesi, **[MEVZUAT]** Türk mevzuatı, **[PRATİSYEN]** ajans ya da uzman yazısı, *sezgisel* (ölçülmemiş çalışma kuralı), *varsayım* (örnek için uydurulmuş rakam). Her modülün sonunda "Doğrulanması gerekenler" ve "Kaynaklar" bölümü var.

---

## 2. Bir yapay zekâya nasıl yüklenir?

Hangi platformu seçersen seç mantık aynıdır:

1. **Talimat alanına** `11-yapay-zeka-talimati.md` dosyasının **içeriğini** kopyalayıp yapıştır.
2. **Bilgi ya da dosya alanına** şu 11 dosyayı yükle: `01-…` ile `10-…` arasındaki modüller ve `KILAVUZ.md`.
3. `00-oku-beni.md` dosyasını yükleme. `11` dosyasını da ayrıca bilgi dosyası olarak eklemen gerekmez.
4. Kurulumu §2.6'daki sorularla test et.
5. **İpucu:** Bir müşteri için brif kartını (Modül 01 §1.10) doldurduktan sonra onu da bir dosya olarak projeye ekle. Claude'un yardım sayfasına göre proje içindeki sohbetler birbirinin içeriğini görmez; yalnız proje bilgisine eklenen dosyalar her sohbette kullanılır. Diğer platformlarda da en güvenli yol budur.

> **Ekim 2026'da platformlar değişiyor.** ChatGPT'de özel GPT oluşturma kişisel hesaplarda kapandı. Gemini'da Gem'ler Kasım 2026'da "beceri"ye dönüşüyor. Aşağıda her platform için bugün çalışan yolu ve yakında değişecek olanı ayrı yazdım. Menü adları dile, plana ve sürüme göre farklı görünebilir.

### 2.1 ChatGPT: Projeler (önerilen yol)

Özel GPT yerine **Proje** kullan. Proje kendi talimatını ve dosyalarını tutar, içindeki bütün sohbetlere uygular.

1. chatgpt.com'a gir. Sol kenar çubuğunda **Projeler** bölümünden **Yeni proje**'ye tıkla (İngilizce arayüzde *New project*).
2. Projeye bir ad ver, ör. "Meta Reklam Uzmanı".
3. Projenin ayarlarında **Talimatlar** (*Instructions*) alanını aç. `11-yapay-zeka-talimati.md` içeriğini yapıştır, kaydet.
4. Projenin dosya alanından (**Dosya ekle** / *Add files*) 11 bilgi dosyasını yükle.
5. Projenin içinde yeni bir sohbet aç ve §2.6'daki test sorularını sor.

**Dosya sınırı:** İkincil kaynaklara göre proje başına ücretsiz planda 5, Plus ve Go'da 25, Pro, Business ve Enterprise'da 40 dosya yüklenebiliyor; dosya başına sınır 512 MB. OpenAI yardım sayfası bu çalışmada açılamadı (erişim engeli), sayılar **doğrulanmalı**. Ücretsiz planda 11 dosya sığmaz; §2.5'teki gibi 5 dosyaya birleştir.

### 2.2 ChatGPT: Özel GPT (yalnız kurumsal çalışma alanında)

- İkincil kaynaklara göre Ağustos 2026'dan beri kişisel hesaplarda (Free, Go, Plus, Pro) **yeni GPT oluşturulamıyor**. Mevcut GPT'ler çalışmaya devam ediyor.
- Aynı kaynaklar özel GPT'lerin **11 Aralık 2026'da emekliye ayrılacağını** ve yerlerini "eklenti" (*plugin*) yapısının alacağını yazıyor. Talimat bu yapıda bir "beceri"ye dönüşüyor, bilgi dosyaları kopyalanıyor. Tarihler hesaba göre değişebilir, **doğrulanmalı**.
- Business, Enterprise ya da Edu çalışma alanın varsa ve yöneticin izin veriyorsa: **GPT'leri keşfet → Oluştur → Yapılandır**. Talimatı **Talimatlar** alanına yapıştır (sınır 8.000 karakter; talimat 7.227 karakter). Dosyaları **Bilgi** bölümünden yükle, **Kaydet**.
- **Öneri:** Yeni kurulumu Projeler'de yap. Emekliye ayrılacak bir yapıya emek harcama.

### 2.3 Claude: Proje

Projeler ücretsiz hesaplar dahil herkese açık; ücretsiz planda en fazla 5 proje açılabiliyor.

1. claude.ai/projects adresine git, sağ üstte **+ New Project**'e tıkla.
2. Ad ve açıklama gir. Claude bu iki alanı okumaz; talimatı ayrıca vereceksin.
3. Proje sayfasında **Set project instructions**'a tıkla. `11` dosyasının içeriğini yapıştır, **Save instructions** ile kaydet.
4. Sağdaki proje bilgisi alanında **+** düğmesiyle 11 bilgi dosyasını yükle.
5. Proje içinde yeni sohbet aç ve test et.

**Sınırlar (Claude yardım sayfası):** Proje dosyası başına 30 MB. Dosya sayısı sınırsız, ama toplam içerik bağlam penceresine sığmalı. Ücretli planlarda (Pro, Max, Team, Enterprise) bilgi büyüyünce Claude kendiliğinden RAG moduna geçiyor ve kapasite yaklaşık 10 katına çıkıyor. Ücretsiz planda bu mod yok. Paketin tamamı (≈ 678 bin karakter) ücretsiz planda sığmayabilir. O durumda işine yarayan modülleri yükle (§3.2). Yardım sayfasının dosya türü listesinde `.md` açıkça yazmıyor. Yükleme reddedilirse uzantıyı `.txt` yap.

### 2.4 Gemini: Gem (Kasım 2026'ya kadar) ve Beceri

**Önemli:** Google'a göre kişisel hesaplarda Gem desteği **Kasım 2026'da** kalkıyor ve Gem'ler otomatik olarak **beceriye** (*skill*) taşınıyor. Workspace Business ve Enterprise hesaplarında tarih Mart 2027, Education hesaplarında Haziran 2027. Bugün kuruyorsan doğrudan beceri kurmak daha kalıcı.

**a) Beceri olarak kurulum (önerilen):**

1. gemini.google.com'a gir. Menüden **Özelleştir → Beceriler**'i aç. "Özelleştir" görünmüyorsa önce **Diğer**'e bak; Google'ın geçiş sayfası aynı ekrana **Ayarlar → Beceriler** yolunu da veriyor.
2. **Ekle / Oluştur → Manuel olarak oluştur**'u seç.
3. Ad olarak küçük harf ve tire kullan: `meta-reklam-uzmani`. Açıklamaya "Türkiye'de Meta reklamı planlama, teşhis ve raporlama" yaz. Talimatlar alanına `11` dosyasının içeriğini yapıştır, **Oluştur**'a tıkla.
4. Bilgi dosyalarını eklemek için: Beceriler sayfasında becerinin **Beceri işlemleri → İndir** seçeneği bir `.zip` indirir. Zip'i aç, içindeki `SKILL.md` dosyasını `meta-reklam-uzmani` adlı bir klasöre koy. 11 bilgi dosyasını da aynı klasöre koy.
5. **Beceri işlemleri → Beceriyi değiştir** ile klasörü yükle. Klasör adı beceri adıyla aynı olmalı.
6. Sohbette `/` yazıp beceriyi seç. Gemini, etkin beceriyi konu uyunca kendiliğinden de kullanabilir.

Beceri koşulları (Google yardım sayfası): 18 yaş ve üstü kişisel hesap; "Etkinliği Sakla" ayarı açık; beceriye dosya yükleme şimdilik gemini.google.com web uygulamasında (Google'ın SSS'ine göre Mac uygulamasında da) yapılıyor. `.md`, `.txt` ve `.pdf` destekleniyor, `.docx` desteklenmiyor. Yüklenen dosyaların toplamı 100 MB'ı geçmemeli. Paket bu sınırın çok altında.

**b) Gem olarak kurulum (geçiş tarihine kadar):**

1. gemini.google.com → **Kenar çubuğunu aç → Gem'ler → Yeni Gem**.
2. Ad gir. Talimatlar alanına `11` dosyasının içeriğini yapıştır.
3. **Bilgi** bölümünde **Dosya ekle → Dosya yükle** ile dosyaları ekle, **Kaydet**.

Gem'in Bilgi bölümü en fazla **10 dosya** alıyor (9to5Google, Kasım 2024; Google da bir isteme 10 dosya sınırı veriyor). Paket 11 dosya olduğu için `10` modülünü ve `KILAVUZ.md`'yi tek dosyada birleştir (§2.5).

### 2.5 Dosya sayısı sınırına takılırsan: birleştirme

İki ya da daha çok modülü tek dosyada art arda birleştirmek içeriği bozmaz. Modüller birbirine numarayla atıf yapar ("Modül 05 §10.2"), bu atıflar birleşik dosyada da çalışır.

| Sınır | Birleşik dosyalar |
|---|---|
| **10 dosya** (Gem) | 01–09 ayrı · `E-10-ve-kilavuz.md` = 10 + KILAVUZ |
| **5 dosya** (ChatGPT ücretsiz proje) | `A-01-02.md` = 01 + 02 · `B-03-04.md` = 03 + 04 · `C-05-06-07.md` = 05 + 06 + 07 · `D-08-09.md` = 08 + 09 · `E-10-ve-kilavuz.md` = 10 + KILAVUZ |

**Nasıl birleştirilir:**

- **Bilgisayarda terminal yoksa:** İlk dosyayı Not Defteri ya da TextEdit ile aç. İkinci dosyanın tamamını kopyalayıp sonuna yapıştır. "Farklı kaydet" ile yeni adla, **UTF-8** kodlamasıyla kaydet. Word kullanma: `.docx` bazı platformlarda desteklenmiyor ve biçimi bozuyor.
- **macOS ya da Linux terminali** (`profesyonel` klasörünün içinde):

  ```
  cat 10-raporlama-ve-calisma-rutini.md ../KILAVUZ.md > E-10-ve-kilavuz.md
  cat 01-*.md 02-*.md > A-01-02.md
  ```

- **Windows PowerShell** (`profesyonel` klasörünün içinde):

  ```
  Get-Content 10-raporlama-ve-calisma-rutini.md, ..\KILAVUZ.md -Encoding UTF8 | Set-Content E-10-ve-kilavuz.md -Encoding UTF8
  ```

### 2.6 Kurulumu test et

Yeni sohbette şu dört soruyu sor. Cevap beklenene uymuyorsa talimat yapıştırılmamış ya da dosyalar okunmuyor demektir.

| Soru | Beklenen cevap |
|---|---|
| "Vergi numaram Meta'da girili değil, Piksel siparişleri KDV dahil gönderiyor, katkı marjım %40. Reklam Yöneticisi'nde başa baş ROAS kaç olur?" | P × M = 1,20 × 1,26 = 1,512 → 1,512 ÷ 0,40 ≈ **3,78**. Dayanak olarak Modül 01 §3.2'yi göstermeli |
| "Meyhanem için Instagram reklamı hazırla." | Reddetmeli: ana işi içki sunmak olan yerin reklamı Türkiye'de yasak (Modül 09 §1, kırmızı ve istisnasız). Menüsünde alkol olan bir restoranın reklamı ayrı konudur ve alkol unsuru içermemelidir (Modül 08 §1.1) |
| "Hesabıma bakman için şifremi yazayım mı?" | Reddetmeli. Erişimin portfolyoya kişi ya da ortak ekleyerek verildiğini anlatmalı (Kurulum Kılavuzu §11) |
| "Türkiye'de ortalama CPM kaç TL?" | Güvenilir, yöntemi açık bir Türkiye ortalaması olmadığını söylemeli (Modül 07 §5) ve hesabın kendi tabanını çıkarmayı önermeli |

---

## 3. Modüller ve ne zaman hangisi

### 3.1 Modül listesi

| # | Dosya | İçinde ne var | Ne zaman |
|---|---|---|---|
| 01 | `01-musteri-brif-ve-is-ekonomisi.md` | Brif soruları ([Z] zorunlu), KPI cümlesi, katkı ve başa baş formülleri, Türkiye çarpanları (M = 1,05 / 1,26, P), bütçe ve öğrenme minimumu, test sözleşmesi, beklenti yönetimi | Yeni müşteri, ilk görüşme, "bütçem yeter mi?", "ROAS 3 iyi mi?" |
| 02 | `02-strateji-ve-hesap-yapisi.md` | Amaç → dönüşüm konumu → performans hedefi tablosu, olay merdiveni, Advantage+ kuralları, 4 hesap yapısı şablonu, bütçe türü, teklif stratejisi, reklam alanları, adlandırma standardı | Kampanyayı kurmadan önce |
| 03 | `03-hedef-kitle.md` | Kontrol ve öneri farkı, geniş kitle karar tablosu, özel ve benzer kitleler, müşteri listesi ve KVKK kapısı, yeniden pazarlama tavanı, yerel konum ayarı | Kitle kurarken, liste yüklemeden önce, yerel işletmede |
| 04 | `04-kreatif-ve-metin.md` | Kreatif ilkeleri, ölçüler, 24 Türkçe kanca, metin çerçeveleri, teklif kurgusu ve indirim kuralı, video ve UGC brifi, "kaç kreatif?", yorgunluk, yapay zekâ kontrolü, politika uyumlu dil | Kreatif partisi hazırlarken, yorgunlukta |
| 05 | `05-olcum-ve-takip.md` | Piksel, CAPI, tekilleştirme, altyapı entegrasyonları, ilişkilendirme, Meta–GA4–kasa farkı, haftalık mutabakat (K, P, V, MER), UTM, WhatsApp ve form satışı ölçümü, alarm tablosu, artırımlı etki | Yayından önce ölçüm kurulumu; "Meta ile kasa tutmuyor" |
| 06 | `06-test-optimizasyon-olcekleme.md` | Test hipotezi, test aracı seçimi, 7/14/30/90 gün planı, haftalık karar tablosu, aşamalı kapatma (3× / 5× / 6,3×), ölçekleme kapısı, öğrenmeyi koruma, sezon planı, değişiklik günlüğü | Yayından sonraki her hafta |
| 07 | `07-metrikler-ve-teshis.md` | Metrik tanımları, sütun ön ayarları, metrik zinciri, gürültü kuralı, 35 satırlık teşhis tablosu (T01–T35), kaynaklı kıyas değerleri, kendi tabanını çıkarma | "CPA neden arttı?", rakam yorumlarken |
| 08 | `08-sektor-oyun-kitaplari.md` | Sektör mevzuat kapısı, reklam verilmeyecek işler, ortak bütçe formülleri, WhatsApp merdiveni; 10 sektör kartı: restoran, kuaför, klinik, emlak, eğitim, e-ticaret, ev hizmetleri, otel, B2B, spor salonu | Sektöre özel plan isterken |
| 09 | `09-politika-hukuk-ve-risk.md` | Risk kapısı (kırmızı/sarı/yeşil), Meta yasaklı ve kısıtlı içerik, kişisel özellik kuralı, açılış sayfası şartları, Türk mevzuatı ve 2026 cezaları, hesap güvenliği, ret ve itiraz, uyum kontrol listesi | Her yeni işte ilk adım; ret ya da kısıtlamada |
| 10 | `10-raporlama-ve-calisma-rutini.md` | Günlük, haftalık, aylık rutin; haftalık rapor şablonu ve durum ışığı; müşteri dili; yetki matrisi; bildirim süreleri; Reklam Kütüphanesi ile rakip analizi; devir kontrol listesi | Rapor yazarken, müşteriyle konuşurken, hesap devralırken |
| — | `../KILAVUZ.md` | Kurulum Kılavuzu: hesap, Sayfa, portfolyo, reklam hesabı, ödeme, Reklam Yöneticisi adımları, hata kodları, güvenlik | Hesap henüz yoksa; ekranda bir menü bulunamıyorsa; ödeme ve fatura sorusunda |
| 11 | `11-yapay-zeka-talimati.md` | Sistem talimatı | Yalnız talimat alanına yapıştırılır |

### 3.2 Durum → bakılacak yer

| Durum | Önce | Sonra |
|---|---|---|
| Hiç reklam vermedim, hesabım yok | Kurulum Kılavuzu | Modül 01 |
| Yeni bir işletme için plan istiyorum | Modül 09 §1 (kapı) → Modül 01 §1 (brif) | Modül 02, 03, 04, 05 |
| Sektörüme özel ne yapmalıyım? | Modül 08 (kartın) | Modül 09 §1 |
| Bütçem yeter mi, kaç reklam seti açayım? | Modül 01 §4 | Modül 02 §1 |
| CPA ya da ROAS kötüleşti | Modül 05 §10.2 (önce ölçüm) → Modül 07 §4 | Modül 06 §4.4 |
| Reklamı kapatayım mı, bütçeyi artırayım mı? | Modül 06 §5–§6 | Modül 07 §3.3 (gürültü) |
| Meta ile mağaza panelim tutmuyor | Modül 05 §5.4–§5.5 | Modül 01 §5.3 |
| Reklamım reddedildi ya da hesabım kısıtlandı | Modül 09 §7 | Kurulum Kılavuzu §6.2, §10 |
| 11.11, Kara Cuma, Ramazan hazırlığı | Modül 06 §9 | Modül 09 §5.3 (indirim kuralı), Modül 04 §5 |
| Müşteri listemi yüklemek istiyorum | Modül 03 §4.4 (KVKK kapısı) | Modül 09 §5.6 |
| Haftalık rapor ve müşteri mesajı | Modül 10 §5 | Modül 10 §6 |
| Başka ajanstan hesap devralıyorum | Modül 10 §9 | Modül 09 §6 |

Claude ücretsiz planı gibi alanı dar bir ortamda bu tablodan işine yarayan 3–5 modülü seç. Modül 09'u ve Kurulum Kılavuzu'nu her zaman yükle.

---

## 4. Örnek ilk mesajlar

Asistana ne kadar somut veri verirsen o kadar az soru sorar. Örneklerdeki rakamlar varsayımdır; kendi rakamlarını yaz. Müşteri adı, telefon, e-posta gibi kişisel verileri paylaşmadan önce sil.

**1. Yeni işletme, sıfırdan plan**

> Üsküdar'da kombi bakım ve tamir servisimiz var. İşlerin çoğu WhatsApp'tan geliyor, iki ekibiz. Meta reklamına ayda 15.000 TL ayırabilirim. Bana bir reklam planı çıkar. Önce bilmen gereken ne varsa sor.

**2. E-ticaret: eşik ve bütçe hesabı**

> ikas'ta cilt bakımı seti satıyoruz. Ortalama sipariş 900 TL KDV dahil (%20). Ürün maliyeti 240 TL, kargo ve paket 75 TL, kart komisyonu %3,5, iade yaklaşık %6. Vergi numaramız Meta'da girili, Piksel sipariş değerini KDV dahil gönderiyor. Aylık bütçe 20.000 TL, hedef satışın %15'i kadar kâr. Reklam Yöneticisi'nde bakacağım başa baş ve hedef CPA ile ROAS'ı hesapla, bu bütçe yeter mi söyle.

(Bu örnek Modül 01 §3.3'teki örnekle aynıdır. Asistan RY başa baş CPA ≈ 341 TL ve RY başa baş ROAS ≈ 2,64 bulmalı.)

**3. Teşhis**

> Satış kampanyamda 11–17 Ekim haftasında satış başı maliyet 520 TL'ye çıktı. Önceki 4 haftanın ortalaması 380 TL'ydi. CPM 140 TL'den 165 TL'ye çıktı, bağlantı tıklama oranı %1,3'ten %1,1'e düştü, haftada 31 satış var. Reklam Yöneticisi dışa aktarımını ve kampanya ekranının görüntüsünü ekliyorum. Sorun nerede, bu hafta ne yapmalıyım?

**4. Haftalık rapor**

> Ekteki CSV, geçen haftanın (pazar–cumartesi) Reklam Yöneticisi verisi. Mağaza panelinde aynı hafta 142 sipariş ve KDV hariç 118.000 TL ciro var. Reklam öncesi haftalık ortalamamız 95 siparişti. Reklam Yöneticisi'ndeki hedef CPA'mız 250 TL, başa başımız 340 TL. Müşteriye gidecek haftalık raporu ve kısa WhatsApp özetini yaz.

**5. Ret ve uyum kontrolü**

> Kozmetik ürün reklamım "Sağlık ve sağlıklı yaşam" gerekçesiyle reddedildi. Metin: "Sivilceleri 7 günde yok eden doğal serum! Sadece bugün %50 indirim." Neden reddedilmiş olabilir? Meta'ya ve Türk mevzuatına uygun yeni bir metin yaz. İtiraz etmeli miyim, yoksa reklamı düzeltip yeniden mi göndermeliyim?

**Veriyi nasıl verirsin:** Reklam Yöneticisi'nde tabloyu **Dışa aktar** ile `.csv` ya da `.xlsx` olarak indir ve sohbete ekle. Hangi tarih aralığı, hangi ilişkilendirme ayarı ve hangi düzey (kampanya, reklam seti, reklam) olduğunu yaz. İş sonucunu (sipariş, ciro, satılan iş) kendi kaydından ayrıca ver. Ekran paketin anlattığından farklıysa ekran görüntüsü ekle.

---

## 5. Sınırlar

- **Hesabına giremez.** Asistan reklam açamaz, kapatamaz, para harcayamaz. Ne yapılacağını adım adım yazar, sen uygularsın. Ona şifre, iki adımlı doğrulama kodu ya da kart bilgisi verme. Bir kişiye erişim vereceksen portfolyoya kişi ya da ortak olarak ekle (Kurulum Kılavuzu §11). Modül 10'daki yetki matrisi, hesaba erişimi olan bir kişi ya da ajans için yazıldı. Sohbet asistanında "YZ kendisi yapar" satırlarını "asistan önerir, sen uygularsın" diye oku.
- **Sonuç garantisi yok.** Paket bir çalışma yöntemi verir. Belirli bir ROAS, satış ya da kâr sözü vermez. Meta da hedefli teklif stratejilerinde bile hedefin tutacağını garanti etmiyor (Modül 01 §5.1).
- **Güncellik.** Bilgi Ekim 2026 tarihlidir. Meta özellikleri hesaptan hesaba kademeli açar ve sık değiştirir. Modüllerde "hesaba göre değişebilir" ya da "doğrulanmalı" yazan her şeyi kendi ekranında kontrol et. Özellikle şunlar değişebilir:
  - Türkiye'deki %5 konum ücreti. Dijital Hizmet Vergisi 1 Ocak 2027'de %2,5'e iniyor; Meta'nın ücreti değişir mi bilinmiyor (Modül 01 §3.2). Ocak 2027'den sonra Meta'nın sayfasını yeniden kontrol et.
  - Ceza tutarları (2026 değerleri), sezon tarihleri, set düzeyinde reklam alanı seçimi, yeni performans hedefleri.
  - Yapay zekâ platformlarının kendisi (§2): ChatGPT özel GPT'leri ve Gemini Gem'leri emekliye ayrılıyor.
- **Hukuki ya da mali görüş değildir.** Modül 09 riskli işi fark edip durmak için yazıldı. Sağlık, finans, kripto, kişisel veri, vergi ve stopaj konularında karar avukatın ya da mali müşavirindir.
- **Yapay zekâ yanılabilir.** Uzun bilgi dosyalarında doğru paragrafı bulamayabilir. Bu yüzden talimat her cevapta modül ve bölüm numarası vermesini istiyor. Para harcatan bir karardan önce o bölümü kendin aç ve hesabı kontrol et. Asistan bir sayıyı kaynaksız söylüyorsa "Bu sayı nereden?" diye sor.
- **Kişisel veri.** Müşteri listesi, form yanıtı ya da telefon numarası içeren dosyayı sohbete yükleme. Raporlar için toplam rakamlar yeterli. Meta'ya müşteri listesi yüklemeden önce Modül 03 §4.4'teki KVKK kapısını uygula.

## 6. Paketi güncellerken

- Bir modül değişirse platformdaki eski dosyayı sil, yenisini yükle. Aynı modülün iki sürümü birlikte durmasın.
- Talimatı değiştirirsen karakter sayısını kontrol et: `wc -m 11-yapay-zeka-talimati.md` (ChatGPT özel GPT sınırı 8.000 karakter).
- Platform değişikliğinde (GPT → eklenti, Gem → beceri) talimatın ve dosyaların kopyası zaten bu klasörde. Taşıma sonrası §2.6'daki test sorularını yeniden sor.

## 7. Platform bilgileri için kaynaklar (erişim 20 Ekim 2026)

**Resmî (okundu):**
- Claude, projeler: https://support.claude.com/en/articles/9517075-what-are-projects
- Claude, proje oluşturma ve talimat: https://support.claude.com/en/articles/9519177-how-can-i-create-and-manage-projects
- Claude, dosya türleri ve sınırlar: https://support.claude.com/en/articles/8241126-what-kinds-of-documents-can-i-upload-to-claude-ai
- Gemini, Gem oluşturma: https://support.google.com/gemini/answer/15146780?hl=tr
- Gemini, Gem'lerden becerilere geçiş: https://support.google.com/gemini/answer/18560919?hl=tr
- Gemini, beceri oluşturma ve dosya yükleme: https://support.google.com/gemini/answer/17094296?hl=tr
- Gemini, dosya yükleme sınırları: https://support.google.com/gemini/answer/14903178?hl=tr

**İkincil (OpenAI yardım sayfaları bu çalışmada 403 hatası verdi; ChatGPT bilgileri bunlara dayanıyor, doğrulanmalı):**
- Gem Bilgi bölümünde 10 dosya: https://9to5google.com/2024/11/12/gemini-advanced-gems-files/
- Kişisel hesaplarda GPT oluşturmanın kapanması: https://webiano.digital/openai-is-restricting-custom-gpt-creation-on-personal-accounts-but-some-plus-users-still-have-access/
- Özel GPT'lerin emekliye ayrılması ve eklentiye taşınma: https://mixed-news.com/en/openai-retiring-custom-gpts-what-carries-over-to-plugins/
- Gem ve GPT geçiş tarihleri: https://dev.to/naw103/gemini-gems-become-skills-in-november-and-custom-gpts-retire-on-december-11-what-to-copy-out-first-3kf5
- ChatGPT proje dosya sınırları: https://fast.io/resources/chatgpt-projects-file-limit/
- GPT talimatında 8.000 karakter sınırı (OpenAI topluluk forumu, 2024): https://community.openai.com/t/how-can-i-increase-the-ceiling-for-gpt-instructions-beyond-8000-characters/801867

---

# Meta Reklam Uzmanı: sistem talimatı (bilgi paketi Ekim 2026)

## Rol
Türkiye'de çalışan kıdemli bir Meta (Facebook, Instagram, WhatsApp) performans pazarlamacısısın. Türkçe, kısa ve sayıyla konuşursun. Bilgin yüklenen pakettir: modül 01–10 ve KILAVUZ.md (Kurulum Kılavuzu). Cevap vermeden önce ilgili modülü bilgi dosyalarında ara. Paketle çelişen genel bilgini kullanma. Pakette olmayanı "pakette yok; genel bilgi, doğrulanmalı" diye etiketle.

**Harita:** 01 brif, birim ekonomisi, bütçe, test sözleşmesi · 02 amaç, dönüşüm konumu, hesap yapısı, teklif, adlandırma · 03 hedef kitle, müşteri listesi ve KVKK · 04 kreatif, metin, yorgunluk · 05 Piksel/CAPI, ilişkilendirme, mutabakat · 06 test, kapatma, ölçekleme, sezon · 07 metrikler, teşhis tablosu T01–T35 · 08 sektör kartları · 09 politika, Türk hukuku, hesap riski · 10 rutin, rapor, müşteri dili, yetki · Kurulum Kılavuzu: menüler, ödeme, hata kodları.

## Çalışma yöntemi
1. **Kapı:** İşi Modül 09 §1'e göre kırmızı, sarı ya da yeşil sınıfla. Kırmızıda dur. Sarıda insan onayı iste.
2. **Brif:** Yeni plan isteğinde önce Modül 01 §1'deki [Z] sorularını tek mesajda sor: ne, kime, nerede satılıyor; sipariş tutarı ve KDV; değişken maliyet, kargo, komisyon, iade; vergi numarası Meta'da girili mi; Piksel değeri KDV dahil mi; 90 günlük hedef (sayıyla); kapasite ve mesaj yanıt süresi; aylık bütçe ve taahhüt; ölçüm (Piksel/CAPI, WhatsApp etiketi, CRM); son 90 günün verisi. [Z] eksikken bütçe ya da kurulum önerme. Kullanıcı varsayımla ilerlemeni isterse her varsayımı "varsayım" diye işaretle.
3. **Hesabı göster:** formül → yerine koyma → sonuç → okuma. Temel formüller (Modül 01 §3–4, Modül 02 §1):
   - Katkı = KDV hariç gelir − değişken maliyet − kargo − komisyon − iade karşılığı
   - RY başa baş CPA = Katkı ÷ M · RY başa baş ROAS = P × M ÷ katkı marjı
   - M = 1,05 (vergi no girili, %5 konum ücreti [RESMİ]) ya da 1,26 (girili değil) · P = 1,00 (Piksel KDV hariç) ya da 1 + KDV oranı (%20'de 1,20)
   - Günlük minimum = max(CPA × 50 ÷ 7 [PRATİSYEN]; 10 × CPA [RESMİ]) · N = günlük bütçe × 7 ÷ planlama CPA (Modül 01 §4.2) · set sayısı ≈ N ÷ 50
   - Terim notu: "panel" = RY (Reklam Yöneticisi). Modül 07'deki c = M. Modül 05'teki P (Meta payı) Piksel çarpanı değildir.
4. **Kararı tablodan ver:** amaç ve konum Modül 02 §2.1 · bütçe yetmezse Modül 01 §4.4 · kitle Modül 03 §2.1 · haftalık karar Modül 06 §4.4 · kapatma §5.2 · ölçekleme kapısı §6.1 · teşhis Modül 07 §4 (önce Modül 05 §10.2 ölçüm alarmları) · sektör Modül 08.
5. **Süre ve düzey:** en az 7 tam günlük veri; kampanya bütçesinde kampanya düzeyi [RESMİ]. İlk 72 saatte yalnız operasyonel sebeple kapat [PRATİSYEN]. Değişiklikleri haftada bir, toplu yap.
6. **Doğruluk kaynağı** işletmenin kaydıdır (sipariş paneli, CRM, WhatsApp satış defteri). Meta raporu yön gösterir.
7. İlk mesaj belirsizse dört seçenek sun: kampanya planı, teşhis, haftalık rapor, uyum kontrolü.
8. Her önerinin sonunda dayandığın yeri yaz: "Dayanak: Modül 02 §2.1" ya da "Kurulum Kılavuzu §3.6".

## Çıktı biçimleri
**A) Kampanya planı:** 1 Kapı ve gerekçe · 2 KPI cümlesi (Modül 01 §2.1) · 3 Eşik tablosu: katkı, M, P, RY başa baş ve hedef CPA/ROAS · 4 Bütçe: günlük minimum, N, set sayısı, kapasite tavanı, karar · 5 Yapı ağacı: kampanya, set, reklam; amaç, dönüşüm konumu, performans hedefi, teklif, bütçe türü, adlar (Modül 02 §10) · 6 Kitle ve hariç tutmalar · 7 Kreatif partisi ve metinler · 8 Ölçüm ve test siparişi · 9 Test sözleşmesi: süre, bütçe, başarı, gri bölge, durdurma (Modül 01 §4.6) · 10 7/14/30 gün takvimi · 11 Riskler ve doğrulanacaklar.

**B) Teşhis cevabı:** Tespit (rakam, dönem, veri yaşı) · Ölçüm kontrolü · Zincir: CPM → CTR → YSG → CVR → CPA çarpanları, gürültü payı ±2 ÷ √k (Modül 07 §3) · Muhtemel neden ve T-numarası · Yapılacak: tek ana eylem, tarih, geri dönüş koşulu · Yapılmayacak · Dayanak.

**C) Haftalık rapor:** Modül 10 §5.2 şablonu, §5.1 durum ışığı kuralları. Her sayının yanında hedef, başa baş ve önceki dönem. İstenirse §5.4 kısa mesaj sürümü.

## Kesin kurallar
- Şifre, 2FA kodu, kart numarası, CVV ya da kimlik görseli isteme, kabul etme. Paylaşılırsa şifreyi değiştirmesini söyle. Erişim yalnız portfolyoya kişi ya da ortak eklemeyle verilir (Kurulum Kılavuzu §11).
- Hesaba giremezsin; reklam açamaz, kapatamaz, para harcayamazsın. Adımı sen yazarsın, kullanıcı uygular. Para harcatan ya da geri alınamayan adımda önce onay iste.
- Sayı uydurma. Her sayı ya kullanıcının verisidir ya paketten etiketiyle gelir ([RESMİ], [MEVZUAT], [PRATİSYEN]) ya da "sezgisel/varsayım" diye işaretlenir. Türkiye için yöntemi açık bir CPM/CPC/CPL kıyası yok (Modül 07 §5). "Sektör ortalaması" sorulursa bunu söyle, hesabın kendi tabanını çıkarmayı öner. ROAS, satış ya da kâr garantisi verme.
- Kullanıcı tablo ya da ekran görüntüsü paylaşırken müşteri adı ve telefonu gibi kişisel verileri çıkarmasını iste.
- Kaynak uydurma. Bağlantı vereceksen yalnız paketteki tam bağlantıyı ver.
- Hukuk, vergi, KVKK, stopaj ve fatura konularında paketteki kuralı aktar, "hukuki/mali görüş değildir" de, kararı avukata ya da mali müşavire bırak.
- Politikayı ya da mevzuatı dolanma yolu önerme: cloaking, kapatılan hesabın yerine yeni hesap, yasak ürünü örtük anlatma, sahte yorum ya da sahte aciliyet, kişisel özellik iması, belgesiz "en/tek/garanti", 10 gün kuralını çiğneyen indirim.
- Türkiye'de reklamı yasak işlere (alkol, tütün ve e-sigara, ilaç, bahis, falcılık, eş bulma, silah, Türkiye'deki hastaya yönelik sağlık tesisi ya da hekim reklamı, tesis dışında tıbbi estetik) metin, görsel, hedefleme ya da "gri yol" üretme. Nedenini Modül 09 §1 ve §5.5 ile açıkla. Dar istisnaları (açılış ayı, yetki belgeli sağlık turizmi) yalnız §5.5 koşullarıyla ve hukukçu onayı şartıyla anlat.
- Müşteri listesi ya da form verisi yükleme önerisinden önce KVKK kapısını uygula (Modül 03 §4.4).

## Güncellik
- Paket 17–20 Ekim 2026 tarihli. Meta özellikleri kademeli açar ve sık değiştirir. "Hesaba göre değişebilir" ya da "doğrulanmalı" etiketli bir özelliği kesin bilgi gibi anlatma.
- Kullanıcının ekranı paketteki menüyle uyuşmuyorsa tahmin yürütme. Ekran görüntüsü iste, bu sırada Kurulum Kılavuzu §0.3'ü uygulat.
- Bugünün tarihini bilmiyorsan sor. Ocak 2027 ya da sonrasıysa önce şunları doğrulat: %5 konum ücreti, ceza tutarları, haftada ~50 sonuçluk öğrenme eşiği, sezon tarihleri.
- Web erişimin varsa önemli bir kuralı Meta'nın resmi sayfasıyla doğrula ve okuma tarihini yaz.

## Üslup
Önce sonuç, sonra gerekçe, sonra eylem. Maddeli yaz, jargonu ilk geçtiği yerde aç. Tutarları TL olarak, Türkçe biçimde yaz (1.234,50 TL). Ölçtüğünü ve tahmin ettiğini ayır.

---

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

---

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

---

# Hedef kitle ve yeniden pazarlama

> **Profesyonel paket · Modül 03** · Bilgi tarihi: 17 Ekim 2026 · Hedef kitle ekranının temel adımları, konum iğnesi ve minimum yaş **Kurulum Kılavuzu §5.3**'te (`../KILAVUZ.md`). Piksel kurulumu §3.7'de, öğrenme aşaması §5.2 ve §6.3'te. Huni katmanları ve yeniden hedefleme bütçe payı **Modül 02 §3**'te, hesap yapısı şablonları §5'te, adlandırma §10'da.

**Bu modülde**

- Advantage+ hedef kitlede hangi ayarın **kesin sınır (kontrol)**, hangisinin yalnızca **öneri** olduğu. Geniş hedeflemenin ne zaman açık, ne zaman kapalı olacağı (§1–§2).
- Özel hedef kitle türleri, pencereleri ve oluşturma adımları. Müşteri listesinin Türkiye biçimi ve **KVKK kapısı** (§3–§4).
- Benzer kitlelerin 2026'daki rolü, kalan hariç tutma araçları, yeni ve mevcut müşteri ayrımı (§5–§7).
- Yeniden pazarlama penceresi, kitle büyüklüğü ve **bütçe tavanı formülü**, doldurulmuş kurgular (§8).
- Yerel konum hedeflemesi, örtüşme, sık hatalar ve kontrol listeleri (§9–§11).

**Etiketler:** **[RESMİ]** Meta yardım merkezi, geliştirici belgesi, koşul metni · **[MEVZUAT]** Türk mevzuatı ve KVKK yayınları · **[PRATİSYEN]** ajans, uzman, araç sağlayıcı (çıkar çatışması olabilir) · **[HABER]** sektör haberi · *sezgisel* = ölçülmemiş çalışma kuralı · *türetilmiş* = Meta kuralından yapılan aritmetik · *varsayım* = örnek için uydurulmuş girdi. Meta'nın "%X daha iyi" rakamları kendi deneyleridir, hesabın için garanti değildir.

---

## 0. Çalışma kuralları

1. **Soğuk kitlede varsayılan Advantage+ hedef kitle açık.** Kesin sınırlar yalnızca **kontrollere** yazılır: konum, minimum yaş, özel kitle hariç tutma, dil [RESMİ].
2. **Öneri sınır değildir.** Yaş aralığı, cinsiyet, ilgi alanı ve özel kitle dahil etme yalnız yönlendirir [RESMİ].
3. **Gerçek yeniden pazarlama setinde Advantage+ hedef kitleyi kapat.** Meta yeniden hedeflemede bunu önermiyor [RESMİ].
4. **Kitle dilimi için set açma.** Farkı kırılımla izle [RESMİ]. Set sayısı Modül 02 F5'i geçmez.
5. **Daraltmadan önce kreatifle ayrıştır.** Segment daha değerliyse değer kuralı kullan (§2.4).
6. **Ekrandaki tahmini kitle büyüklüğüne göre karar verme** [RESMİ]. Kararı sonuç verisiyle ver.
7. **Müşteri listesini KVKK kapısından (§4.4) geçmeden yükleme.** Dosyaya yalnız eşleştirme için açık rızası kayıtlı satırlar girer. Aydınlatma metni tek başına yükleme izni vermez.
8. **Kitle adı ve kuralı nötr olsun.** Sağlık ya da finansal durum çağrıştıran kitleler Eylül 2025'ten beri işaretlenip engelleniyor [RESMİ].
9. **Hedeflemeyi seyrek ve toplu değiştir.** Hedefleme değişikliği öğrenmeyi sıfırlar. Müşteri listesini güncellemek sıfırlamaz [RESMİ].
10. **Hedefli reklam bilgilendirmesi (TRHTUY md. 25/A).** Yayından önce işletmenin sitesinde bir "Reklam tercihleri" bölümü olsun (§4.4) (*sezgisel*, hukukçuya teyit).
11. **Özel kitle, benzer kitle ya da detaylı hedefleme kullanan her sette minimum yaş 18** (*sezgisel*, md. 25/A-3'e dayanır).
12. **İstihdam, konut ve kredi reklamında yaş ve cinsiyet daraltılmaz** (§2.1) [MEVZUAT].

---

## 1. Kontrol mü, öneri mi?

### 1.1 Ayar tablosu (Advantage+ hedef kitle açıkken) [RESMİ]

| Ayar | Türü | Yeri | Ajan notu |
|---|---|---|---|
| Konum | **Kontrol** | Hedef kitle kontrolleri | Şehir/bölgede "daha fazla kişiye erişin" kutusu açıksa ülke içinde taşar (§9.2). Şehre varsayılan bir yarıçap da eklenir (§9.3) |
| Minimum yaş | **Kontrol** | Hedef kitle kontrolleri | 18'in altı seçilemez, yaş önerisi bunu ezemez |
| Özel kitle hariç tutma | **Kontrol** | Diğer kontrolleri göster | "Öneri olarak kullan" işaretliyken bile kesin uygulanır |
| Dil | **Kontrol** | Diğer kontrolleri göster > Diller | Yalnız o yerde yaygın olmayan bir dil için (§9.4) |
| Yaş aralığı, maks. yaş | Öneri | Hedef kitle önerileri | Maks. yaş kesin sınır değil [PRATİSYEN] |
| Cinsiyet | Öneri | Hedef kitle önerileri | Kontrol karşılığı yok |
| Detaylı hedefleme | Öneri | Hedef kitle önerileri | §2.3 |
| Özel/benzer kitle dahil etme | Öneri | Hedef kitle önerileri | Yeniden pazarlamada işareti kaldır |

**Advantage+ ne zaman kapanır [RESMİ]:**
- "Reklamlarınızın erişimini daha da sınırlayın" ile bir ayar seçip "Öneri olarak kullan" işaretini kaldırınca.
- Kapatmayanlar: minimum yaşı **25'e kadar** yükseltmek, özel kitle hariç tutmak, hesap düzeyi kontroller.
- Satış ve uygulama kampanyasının "Advantage+" sayılması için en az bir sette Advantage+ hedef kitle gerekir (Modül 02 §4).

### 1.2 Hesap düzeyi kontroller

**Reklam ayarları > Hedef kitle kontrolleri** [RESMİ]. Burada şunlar ayarlanır:
- izinli ülkeler,
- yaş kısıtlı ürün için minimum yaş,
- marka koruma,
- işveren hariç tutma ("kendi çalışanlarıma reklam göstermiyorum"),
- reklam alanı kontrolleri.

Değişikliğin yansıması 48 saati bulabilir. Siyaset, konut, istihdam ve finans reklamlarına uygulanmaz. Özellik kademeli açılıyor.

**Eğer-ise:** Kural her kampanyada geçerli olmalıysa (yalnız Türkiye, 18+) hesap düzeyine yaz. Tek kampanyaya özgüyse set kontrolüne yaz.

### 1.3 İki tuzak

- **WhatsApp'ta yaşı bilinmeyenler:** WhatsApp Durumu reklam alanı açıkken yaşı bilinmeyen kişiler otomatik dahil edilir ("Bilinmeyen yaş: Dahil"). 18+ üründe bu işareti kaldır. Bedeli: WhatsApp yayını azalabilir [RESMİ].
- **Tahmini kitle büyüklüğü:** Gerçek erişim değildir. Advantage+ genişlemesini yansıtmaz. Aynı kişi platformlarda ayrı sayılabilir [RESMİ].

---

## 2. Geniş / Advantage+ hedef kitle ne zaman?

### 2.1 Karar tablosu

| Durum | Kurulum | Advantage+ hedef kitle |
|---|---|---|
| Satış/potansiyel müşteri/uygulama, soğuk kitle, Piksel/CAPI sinyali var | Konum. Gerekiyorsa min. yaş ve mevcut müşteri hariç. Öneri boş ya da 1–2 | **Açık** |
| Yeni hesap ya da yeni Piksel, dönüşüm geçmişi az | Aynısı. Öneri olarak yaş aralığı, birkaç ilgi alanı, varsa müşteri listesi | **Açık**, öneri ver [PRATİSYEN] |
| Ürün çok farklı müşteri gruplarına hitap ediyor | Her gruba ayrı kreatif | **Açık** [PRATİSYEN] |
| Yalnız ziyaretçi, etkileşimci ya da müşteriye harcanacak | Özel kitle dahil, öneri işareti kaldırılmış | **Kapalı** [RESMİ] |
| Mevcut müşteri seti (harcaması sınırlı, §7) | Müşteri kitlesi dahil, işaret kaldırılmış | **Kapalı** (yalnız o sette) |
| Yaş kısıtlı ürün | Min. yaş kontrolde ya da hesap düzeyinde | Açık kalabilir |
| İş gereği kesin cinsiyet (ör. yalnız kadın müşteri kabul eden salon) | Cinsiyeti "daha da sınırla" ile seç ve işareti kaldır. Ya da açık bırak, kreatifle hitap et, kırılımla izle. İstihdam, konut ve kredi için geçerli değil (alttaki satır) | Talep kalitesi bozuksa kapalı |
| İstihdam (iş ilanı), konut (satılık/kiralık), kredi ve finansal ürün | Konum işe ya da mülke göre. Min. yaş 18. Yaş aralığı ve cinsiyet **ne öneri ne kontrol** olarak girilir. Başvuru ve talep kalitesi kırılımla izlenir. Metinde de hitap yok (Modül 09 §3) | **Açık** [MEVZUAT: 6701 s. Kanun] (*sezgisel uygulama, hukukçuya teyit*) |
| Yurt dışına sağlık turizmi | Türkiye hariç, Türkçe dışı dil, otomatik genişletmeler kapalı (hukuk modülü) | **Kapalı** [MEVZUAT] |
| Çok yerel işletme, niş B2B, küçük bütçe | Açık başla, konumu kesin kontrol yap. Şüphe varsa A/B testi (Modül 02 §4) | Açık başla |

**Gerekçe:**
- Meta, Advantage+ hedef kitlenin sonuç başına ücreti ortalama şu oranlarda düşürdüğünü bildiriyor: bilinirlikte %14,8, trafik/etkileşim/potansiyel müşteride %9,7, satış ve uygulamada %7,2 [RESMİ].
- Bağımsız bir çalışma (Confect), Andromeda döneminde ortalama ROAS'ın %7 düştüğünü bildiriyor. Bu bilgi pratisyen aktarımı, birincil kaynak okunmadı [PRATİSYEN].
- Meta küçük kitlenin sonuç başına ücreti **en az iki katına** çıkarabileceğini yazıyor [RESMİ]. Bu yüzden elle daraltmak da risklidir.

**İstihdam, konut ve kredi: Türkiye'de neden ayrı kural?**
- Meta'nın Özel Reklam Kategorisi (ÖRK) hedefleme kısıtları ABD'de, Kanada'da ve listelenen Avrupa ülkelerinde zorunlu. Türkiye bu listede yok [RESMİ]. Bu yüzden yalnız Türkiye'yi hedefleyen bir iş ya da emlak ilanında yaş ve cinsiyet daraltmak teknik olarak mümkün görünüyor (*doğrulanmalı*).
- Teknik olarak mümkün olması yasal olduğu anlamına gelmez. 6701 sayılı Kanun cinsiyet ve yaş dahil sayılan temellere dayalı ayrımcılığı yasaklıyor (m.3/2). Yasak, iş ilanı ve işe dair bilgi edinme aşamasını (m.6/1–2), taşınmazın kiralanması ya da satışı hakkında bilgi almak isteyenleri (m.5/3) de kapsıyor. İş Kanunu m.5 de iş ilişkisinde cinsiyete dayalı ayrımı yasaklıyor [MEVZUAT]. Kredi Kanunda ayrıca sayılmıyor. m.5/1'deki "benzeri hizmetler" içine girip girmediği hukukçuya sorulmalı.
- Basına yansıyan 2021 tarihli bir kararda TİHEK, "bayana ve aileye" uygun diye ilan edilen evi bekâr bir erkeğe kiralamayan ev sahibine ve emlakçıya 6701'e dayanarak para cezası verdi [HABER]. Olay bir reklam hedeflemesi değil. Ama ilan aşamasındaki cinsiyet ayrımının yaptırım konusu olabildiğini gösteriyor.
- Kanun bazı ayrımları ayrımcılık saymıyor: zorunlu mesleki gereklilik, yalnız belli bir cinsiyetin istihdamını zorunlu kılan durumlar, hizmetin gerektirdiği orantılı yaş sınırı (m.7/1-a, b, c) [MEVZUAT]. Bu istisnaya yapay zekâ karar vermez. Hukukçunun yazılı onayı yoksa daraltma.
- ÖRK beyanı Türkiye'de zorunlu olmasa da Meta'nın Ayrımcı Uygulamalar ilkesi geçerli. Bir gruba ayrımcılık yapan reklam reddedilebilir [RESMİ]. Hukuk tarafı: Modül 09.

### 2.2 Öneri verme kuralları

- Meta'nın açıklamasını aktaran bir pratisyene göre sistem önce önerilere uyanlara öncelik verir, sonra genişler [PRATİSYEN].
- Hesap haftada 50+ dönüşüm alıyorsa öneri boş kalabilir (*sezgisel*).
- Öneri sırası (*sezgisel*):
  1. müşteri listesi ya da ziyaretçi kitlesi,
  2. yaş aralığı,
  3. birkaç ilgi alanı.
- Yasal ya da iş zorunluluğu varsa ayarı kontrol olarak gir. Yoksa öneri olarak gir.

### 2.3 Detaylı hedefleme: 2025–2026

- **Hariç tutma kaldırıldı** (21 Ocak 2025). Eski hariç tutmalı set düzenlenince ya da çoğaltılınca hariç tutma silinir. Meta'nın testinde hariç tutmasız kurulumda medyan dönüşüm başına ücret **%22,6 daha düşük** çıktı [RESMİ].
- **Seçenekler birleştirildi.** Reklam Yöneticisi uyarısına göre kaldırılan seçenekleri kullanan setler **15 Ocak 2026'dan** itibaren yayın almıyor. Liste yayımlanmadı [HABER]. **Eğer** eski sette "birleştirildi" uyarısı varsa **o zaman** ilgi alanlarını yeniden seç ya da öneriyi kaldır.
- **Advantage+ detaylı hedefleme** yalnız ilgi ve davranışı genişletir. Yaş, konum, dil ve hariç tutmalar korunur [RESMİ].

### 2.4 Daraltmak yerine değer kuralı

Değer kuralı segmenti dışarıda bırakmaz, teklifini ağırlıklandırır [RESMİ]:
- **Kriterler:** yaş, cinsiyet, konum, işletim sistemi, cihaz, reklam alanı, dönüşüm konumu.
- **Teklif aralığı:** %1000'e kadar artırılabilir, %90'a kadar azaltılabilir.
- Kurallar çakışırsa yalnız ilk kural uygulanır.
- ROAS hedefi ve teklif üst sınırıyla kullanılamaz.
- Toplam CPA artabilir.

**Örnek** (*varsayım*): Kadın giyimde 25–44 kadınların sepeti yüksek. Kitleyi daraltma, şu kuralı yaz: "Kadın + 25–44 → +%30". İki hafta sonra toplam CPA'ya ve segment CPA'sına birlikte bak.

---

## 3. Özel hedef kitleler

### 3.1 Türler ve pencereler

| Kaynak | Azami pencere | En kapsayıcı olay | Tipik kullanım |
|---|---|---|---|
| Site (Piksel/CAPI) | **180 gün** [RESMİ]. Satın alma olayında **730 gün** (18 Mayıs 2026'dan beri). Mevcut 180 günlük satın alma kitleleri, reklamveren itiraz etmediyse o tarihte kendiliğinden 730 güne çevrildi [PRATİSYEN, Meta bildirimine atıfla, *doğrulanmalı*]. Adlandırma: §3.3 | Tüm ziyaretçiler | Gezip almayan, sepet terk, alıcıyı hariç tutma |
| Instagram profesyonel hesap | 365 gün (yardım) / 730 gün (geliştirici) | Profille etkileşim kuran herkes | Ilık kitle |
| Facebook Sayfası | 365 / 730 gün | Sayfayla etkileşim kuran herkes | Ilık kitle, soğukta hariç tutma |
| Video | Resmi sayfada yok (*doğrulanmalı*) | En düşük izleme eşiği | Video izleyenler |
| Potansiyel müşteri formu | 90 gün [RESMİ] | Formu açanlar | Açıp göndermeyenler |
| Müşteri listesi | Pencere yok. 30 günden uzun güncellenmezse "Güncel değil" görünür | — | Hariç tutma, mevcut müşteri seti, benzer kitle kaynağı |
| Sadece hariç tutma listesi | — | — | Rızasını geri çekenler (tanım §4.4). Dahil edilemez, sonradan dönüştürülemez. ÖRK kampanyalarında kullanılamaz [RESMİ]. Kullanım koşulları §6.2 |

- Reklam hesabı başına en fazla **500** özel kitle olabilir [RESMİ].
- Etkileşim kitleleri kendi kendine yenilenir, pencereden çıkan kişi düşer. Pencere değişmiyorsa kitleyi yeniden kurma [RESMİ].
- Silinmiş Sayfaya dayanan kitleler yeni setlerde kullanılamaz [RESMİ].

### 3.2 Oluşturma

**Ortak yol:** Reklam Yöneticisi > **Hedef Kitleler** > **Hedef kitle oluştur** > **Özel hedef kitle** > kaynak > **İleri**. Kitle dolunca **Durum** sütununda "Hazır" yazar [RESMİ].

| Kaynak | Adımlar [RESMİ] | Tuzak |
|---|---|---|
| İnternet sitesi | Piksel aktif mi kontrol et → "Herhangi biri"/"Tümü" → Piksel → olay (tüm ziyaretçiler, URL, Piksel olayı) → tutma (gün) → en fazla 5 kural → ad | Reklam Yöneticisi'nde kitle **kurulduğu andan itibaren dolar**, geriye dönük dolmaz. Kitleyi ihtiyaçtan önce kur |
| Instagram | Instagram hesabı → olaylar → tutma (≤365) → dahil/hariç → ad | Yalnız profesyonel hesap. İki kurala uyan kişide hariç tutma kazanır |
| Facebook Sayfası | Sayfa → olay → tutma (≤365) → en fazla 4 ek kriter → ad | — |
| Video | Video → izleme eşiği → **Video seç** (Sayfa, IG, Kampanya ya da Video kimliği; en fazla 200 video) → tutma → ad | Meta metnindeki örnek eşik "en az 10 saniye". Eski gönderiden yapılan reklamda kitle reklam izlenmesini değil, gönderi izlenmesini yansıtabilir |
| Form | Potansiyel müşteri formu → olay (açan/gönderen/açıp göndermeyen) → Sayfa → form → tutma (≤90) | — |

**Site kitlesi küçükse:**
- Kitlede yalnız Meta kullanıcıları ve site dışı veriyle reklamı reddetmemiş kişiler sayılır [RESMİ].
- Meta'nın önerileri: birkaç yüz kişi birikmesini bekle, kural sayısını azalt, pencereyi uzat [RESMİ].
- Çerez rızası Pikseli geciktiriyorsa kitle ayrıca küçülür (§4.4).

### 3.3 Kitle adlandırma (*sezgisel*, Modül 02 §10 ile uyumlu)

`OK_<KAYNAK>_<OLAY>_<PENCERE>G` özel kitle içindir, `BK_<KAYNAK>_<YÜZDE>` benzer kitle içindir. Yalnız ASCII karakter kullan.

Örnekler:
- `OK_WEB_TUM_30G`
- `OK_WEB_ATC-PURharic_14G`
- `OK_WEB_PUR_180G` ya da `OK_WEB_PUR_730G`
- `OK_IG_TUM_365G`
- `OK_FORM_ACIP-GONDERMEYEN_30G`
- `OK_LISTE_MUS_2610`
- `OK_LISTE_HARICTUT-IZINSIZ_2610`
- `BK_LISTE-MUS-DEG_1-5`

Yanlış: `diyabet-hastalari`, `borclu-musteriler`, `yuksek-gelir`.

**Ad, gerçek pencereyi göstermeli** (*sezgisel*):
- Kitleyi adlandırmadan önce ve aylık bakımda Hedef Kitleler ekranında kitlenin gerçek tutma süresine bak. Ada o süreyi yaz.
- 18 Mayıs 2026'daki otomatik geçiş nedeniyle hesaptaki `OK_WEB_PUR_180G` aslında 730 günlük olabilir (§3.1). Öyleyse adı `OK_WEB_PUR_730G` yap ya da tutma süresini 180 güne indir (arayüz izin veriyorsa).
- 730 günü kullanmadan önce aydınlatma metnindeki saklama süresiyle uyumunu hukukçuya sor. KVKK'ya göre veri, işlendiği amaç için gereken süre kadar saklanır (6698 s. Kanun m.4/2-d) [MEVZUAT].

---

## 4. Müşteri listesi ve KVKK

### 4.1 Dosya kuralları [RESMİ]

- Dosya CSV ya da TXT olur. Liste **en az 1.000 müşteri** içerir.
- En az bir ana tanımlayıcı: e-posta, telefon ya da mobil reklam/uygulama/Sayfa kimliği. E-posta ve telefon için en fazla 3'er sütun.
- **Telefonda ülke kodu zorunlu.** Ülke ayrı sütunda iki harfle yazılır: `TR`.
- İsteğe bağlı sütunlar: ad, soyad, şehir, il, posta kodu, doğum tarihi, cinsiyet. Tanımlayıcı arttıkça eşleşme artar.
- **Müşteri değeri** sütunu para birimi işaretsiz sayıdır. Değer tabanlı benzer kitle için gerekir.
- **Konamaz:**
  - sağlık bilgisi,
  - finansal durum (kredi puanı, borç, gelir, kart),
  - 13 yaş altı çocuk verisi,
  - sosyal güvenlik numarası ya da yerel eşdeğeri,
  - şifre, GPS koordinatı.
- TC Kimlik No büyük olasılıkla "yerel eşdeğer" sayılır (*sezgisel*).

### 4.2 Doldurulmuş örnek (kişiler uydurma, *varsayım*)

```
email,phone,fn,ln,ct,country,value
ayse.ornek@example.com,905321234567,Ayse,Yilmaz,Istanbul,TR,4250
mehmet.ornek@example.com,905441112233,Mehmet,Kaya,Ankara,TR,980
,905059876543,Zeynep,Demir,Izmir,TR,1600
```

- Telefon dönüşümü: `0532 123 45 67` → `905321234567`. Boşluklar ve baştaki 0 silinir, başa 90 eklenir.
- Değer sütunu tek ve tutarlı bir tanıma dayanmalı, ör. son 12 ayın KDV hariç cirosu (*sezgisel*).
- Adres ya da sipariş içeriği gibi gereksiz sütun koyma. KVKK'nın ölçülülük ilkesi bunu gerektirir (6698 s. Kanun m.4/2-ç) [MEVZUAT].

### 4.3 Yükleme ve bakım [RESMİ]

1. Hedef kitle oluştur > Özel hedef kitle > **Müşteri listesi**.
2. Türü seç: "Dahil etme veya hariç tutma" ya da "Sadece hariç tutmalar". **Bu seçim sonradan değişmez.**
3. Dosyayı yükle, değer sorusunu yanıtla, ad ver, koşulları kabul et.
4. **Tanımlayıcı eşleme** ekranında her sütunda onay işareti olmalı. Ünlem işareti olan sütunu düzelt.
5. **İçe aktar ve oluştur.**

**Ön koşullar:** Reklam hesabı işletme portfolyosuna bağlı olmalı. Kullanıcının yönetici ya da reklamveren izni olmalı. Yeni hesaplarda birkaç hafta beklemek gerekebilir.

**Bakım:**
- **Eşleşme puanı** 10 üzerinden gösterilir: eşleşen satır ÷ yüklenen satır. Pratisyenlerin "genelde %20–70" eşleşme iddiası kaynaksız. Bu yüzden hariç tutma her zaman biraz sızdırır (§6.3).
- **En az 30 günde bir güncelle.** Seçenekler: "Müşterileri değiştirin / ekleyin / çıkarın". Güncelleme türeyen benzer kitleleri de günceller ve öğrenmeyi sıfırlamaz.
- **Ajansla çalışırken** listeyi kopyalama, portfolyolar arası "hedef kitle paylaşım ilişkisi" kur. Paylaşılan kitleden benzer kitle kurulamaz.

**Kendi karma işlemini (API) yapıyorsan:**
- Yalnız SHA-256 kabul edilir, sonuç küçük harfli hex olmalı.
- E-posta: boşlukları kırp, küçük harfe çevir.
- Telefon: sembolleri ve baştaki sıfırları sil, ülke kodunu ekle.
- Ülke: `tr` [RESMİ].
- **Türkçe tuzağı:** Python `lower()` "İ" harfini "i" + U+0307 birleşik noktasına çevirir ve karma değişir (yerel testle doğrulandı, 17.10.2026). Önce harfleri Latin karşılığına çevir (İ/ı→i, ş→s, ğ→g, ü→u, ö→o, ç→c), sonra küçült. Meta da ad ve soyadın Latin alfabesine çevrilmesini öneriyor.
- Ham CSV yüklüyorsan normalleştirmeyi Meta'ya bırak.

### 4.4 KVKK kapısı (hukuki görüş değildir)

**Karar kuralı** (*sezgisel*, hukukçuya teyit): Bir satır ancak şu **üç koşul birlikte** sağlanırsa dosyaya girer.
1. **Kişi başına kayıtlı hukuki dayanak.** Varsayılan dayanak açık rızadır (6698 s. Kanun m.5/1). Bu rıza reklam platformunda eşleştirme amacına özgü, aydınlatmadan ayrı ve isteğe bağlı alınmış olmalı (ilke kararı 2026/347; içerik pratisyen özetinden) [MEVZUAT] [PRATİSYEN]. Kayıtta tarih, kanal ve metnin sürümü bulunmalı. Başka bir dayanak (ör. meşru menfaat, m.5/2-f) ancak hukukçunun yazılı onayıyla kullanılır.
2. **Aydınlatma metni (m.10)** bu amacı ve yurt dışına aktarımı içeriyor. Aydınlatma bir bildirim yükümlülüğüdür, hukuki dayanak değildir. Tek başına yükleme izni vermez [MEVZUAT].
3. **m.9 aktarım mekanizması** hukukçuca onaylanmış, tarih ve kişiyle kayıtlı.

**Dosyayı rıza bayrağına göre filtrele.** CRM'de `RIZA_ESLESTIRME = evet` olmayan satır dosyaya girmez. Filtreden sonra 1.000 satırın altında kalan liste Meta'nın biçim kuralını karşılamaz (§4.1). Listeyi başka kişilerle doldurma.

**Tanımlar** (bu modülde):
- **Eşleştirme rızası:** Meta gibi bir reklam platformunda eşleştirme için verilen KVKK açık rızası.
- **İYS onayı:** SMS, e-posta ve arama için ticari elektronik ileti onayı (Modül 09 §5.7). Ayrı bir izindir. İYS onayını geri çekmek tek başına eşleştirme rızasını geri çekmek sayılmaz. Talep belirsizse (ör. "pazarlama istemiyorum") ikisini de geri çekilmiş say (*sezgisel*, ihtiyatlı).
- **"İzinsiz"** (`OK_LISTE_HARICTUT-IZINSIZ`): eşleştirme rızasını geri çeken ya da verisinin silinmesini veya pazarlamada kullanılmamasını isteyen kişi. Hiç rıza vermemiş kişi bu listeye de girmez, çünkü onu yüklemek de veri aktarımıdır.

| Durum | Karar |
|---|---|
| Kendi müşterin, üç koşul sağlanıyor | **Yüklenebilir.** Yalnız rıza bayrağı "evet" olan satırlar |
| Kendi müşterin, aydınlatmada amaç yok ya da rıza kaydı yok | **Yükleme.** Metni güncelle, ayrı rıza adımı kur, hukukçuya sor. **Metni güncellemek geçmişte toplanmış veriye rıza sağlamaz.** Eski müşteri ancak yeni rızası kaydedilince dosyaya girer |
| Referans, "arkadaşını öner", başka işletme, satın alınmış liste, fuar, kartvizit | **Asla yükleme.** Üçüncü kişiden gelmek tek başına hukuki dayanak değil (KVKK, 21.07.2026) [MEVZUAT] |
| Kişi eşleştirme rızasını geri çekti | **Aylık döngüyü bekleme.** Kişiyi o gün "Dahil etme veya hariç tutma" türündeki tüm listelerden "Müşterileri çıkarın" ile çıkar (§4.3). Sadece hariç tutma listesine yalnız §6.2 koşulları sağlanıyorsa al |
| Sağlık/finans sektörü ya da liste hassas bir durumu ima ediyor | **Yükleme.** Meta yasaklıyor ve veri büyük olasılıkla özel nitelikli |

**Ajans ya da dış hizmet sağlayıcı** listeye erişiyorsa ya da listeyi yüklüyorsa, işletme ile arasında imzalı bir veri işleyen sözleşmesi olsun. Veri sorumlusu, kendi adına veri işleyenle birlikte güvenlik tedbirlerinden müştereken sorumludur (m.12/2) [MEVZUAT]. Sözleşme gerekliliği bu hükümden çıkarılmış bir uygulamadır (*sezgisel*).

**Neden temkinli:**
- **Hukuki dayanak reklamverende.** Meta koşullarına göre gereken izin ve hukuki dayanak reklamverene ait. Koşulların veri işleme maddesi yalnız GDPR'ı anıyor [RESMİ].
- **Yurt dışına aktarım (KVKK m.9).** Sıra şöyle: yeterlilik kararı, uygun güvence (standart sözleşme), son olarak yalnız **arızi** aktarımda istisna. Düzenli aktarımda açık rıza istisnasına dayanılamaz [MEVZUAT]. Liste yüklemek büyük olasılıkla yurt dışına aktarımdır. Meta'nın Türkiye için sunduğu güvence bulunamadı (*doğrulanmalı*).
- **Çerez rızası.** KVKK Çerez Rehberi'ne (Temmuz 2025) göre davranışsal reklam çerezleri **açık rıza** ister. Siteye girmek rıza sayılmaz [MEVZUAT]. Piksel rızadan sonra çalışırsa site kitleleri küçülür. Bunu §8.3'teki r_rıza katsayısıyla planla.
- **Hedefli reklamcılık (TRHTUY md. 25/A, 1.8.2026'dan beri).** Madde "satıcı ve sağlayıcılar"la başlıyor. Yani reklamveren işletme doğrudan kapsamda, yükümlülük yalnız platformun değil [MEVZUAT]. Hedefli reklam, hangi kriterle gösterildiği ve bunun nasıl değiştirileceği tüketiciye doğrudan ve kolay erişilebilir biçimde sunulursa yapılabilir. Çocuk olduğu bilinen ya da makul olarak bilinmesi gereken kişiye profillemeyle hedefli reklam yapılamaz [MEVZUAT]. Meta'nın "Bu reklamı neden görüyorum?" özelliğinin bu şartı tek başına karşılayıp karşılamadığı belirsiz. Bu yüzden iki varsayılan kural uygula (*sezgisel*, hukukçuya teyit):
  1. **Sitede "Reklam tercihleri" bölümü.** Alt bilgiden ya da çerez/gizlilik sayfasından tek tıkla açılsın. İçeriği:
     - hangi kriterlerle hedeflendiği: konum, yaş, site ziyareti (Piksel), müşteri listesi, benzer kitle;
     - bunun nasıl değiştirileceği: Meta'daki reklam tercihleri ayarı, sitenin çerez ayarları, listeden çıkma talebi için bir kanal (e-posta ya da WhatsApp).
  2. **Minimum yaş 18:** özel kitle, benzer kitle ya da detaylı hedefleme kullanan her sette. Reşit olmayanlara yönelik ürünlerde veliyi hedefle.

**KVKK kontrol listesi** (*sezgisel*):
- [ ] Liste kendi müşterinden. Her satırın eşleştirme rızası tarih, kanal ve metin sürümüyle kayıtlı. Dosya rıza bayrağına göre filtrelendi.
- [ ] Aydınlatma metni bu amacı ve yurt dışına aktarımı içeriyor. Rıza adımı aydınlatmadan ayrı.
- [ ] Yurt dışı aktarım (m.9) için hukukçu onayı tarih ve kişiyle kayıtlı.
- [ ] Ajans ya da dış hizmet sağlayıcı listeye erişiyorsa veri işleyen sözleşmesi imzalı.
- [ ] Yalnız gerekli sütunlar: e-posta/telefon + ülke (eşleşme için ad, soyad, şehir).
- [ ] Hassas veri yok. Kitle adı nötr.
- [ ] Rızasını geri çekenler o gün tüm listelerden çıkarıldı. Sadece hariç tutma listesi yalnız §6.2 koşullarıyla kullanılıyor.
- [ ] 30 günde bir güncelleme planı var. Amaç bitince kitle silinecek.
- [ ] Sitede "Reklam tercihleri" bölümü var (md. 25/A).

---

## 5. Benzer (lookalike) kitleler: 2026'daki rolü

### 5.1 Güncel durum [RESMİ]

- **Kaynak:** Seçilen ülkede **en az 100 kişi**, önerilen 1.000–5.000. Kaynaktaki kişiler benzer kitleden otomatik çıkarılır. Kaydedilmiş hedef kitle kaynak olamaz.
- **Yüzde:** Küçüldükçe benzerlik artar. API'de %1–20 arası seçilebilir.
- **Konum:** Kurulumda artık seçilmiyor, reklam setinden alınıyor (yeni).
- **Oluşturma süresi:** "Oluşturuluyor" durumu 3 güne kadar sürebilir. Bu sırada reklam yayınlanabilir.
- **Advantage+ benzer kitle:** Benzer kitleyi öneriye çevirir. Site, mobil ya da liste kaynaklı benzer kitle içeren yeni, çoğaltılmış ve taslak kampanyalarda **otomatik açılır**.
- Advantage+ hedef kitle açıkken benzer kitle **hariç tutulamaz**.
- Kaynak hassas bilgi nedeniyle işaretlenirse benzer kitle de engellenir.

### 5.2 Ne zaman kullanılır

| Durum | Karar |
|---|---|
| Advantage+ açık, Piksel verisi zengin | Gerekmez. İstersen öneri olarak ekle |
| Yeni Piksel ama 1.000+ kişilik iyi bir müşteri listesi var | **Öneri** olarak ekle. Değer sütunu varsa değer tabanlı kur |
| Manuel kurulum zorunlu | Ana kitle yap. Kesin sınır gerekiyorsa Advantage+ benzer kitleyi kapat |
| Kaynak 100'den az | Kurulamaz. Pencereyi uzat ya da kaynakları birleştir |
| Kaynak hassas durum ima ediyor | **Kurma** |

### 5.3 Kaynak ve yüzde

- "En iyi müşteriler" kaynağı "tüm müşteriler"den iyi sonuç verebilir [RESMİ].
- Değer tabanlı kaynakta sıfır değerli satırları çıkar [RESMİ].
- Pratisyen önerileri [PRATİSYEN]:
  - Kaynağı son 30–90 günün alıcılarından ya da kohortlardan kur.
  - Değer tabanlı kaynakta en iyi %10–20'lik dilimi kullan.
  - 2026 için önerilen bir yüzde yok. %1/%2/%5 basamakları iOS 14 öncesinden kalma bir alışkanlık.
- **Kural** (*sezgisel*): Benzer kitle öneri olarak kullanılıyorsa yüzde sonucu tek başına belirlemez. Manuel kurulumda dar bir yüzdeyle başla. Set öğrenmeyle sınırlı kalırsa genişlet.
- **Ölçek** (*türetilmiş*): Türkiye'de %1'lik bir benzer kitle bile yüz binlerce kişidir. DataReportal, Instagram'ın Türkiye reklam erişimini 62,3 milyon veriyor [HABER]. Meta'nın yüzdeyi hangi tabana göre hesapladığı okunan sayfalarda yazmıyor.

---

## 6. Hariç tutmalar

### 6.1 Kalan araçlar [RESMİ]

1. Reklam setinde **özel kitle hariç tutma**: Kontroller > "Diğer kontrolleri göster". Kesin uygulanır.
2. **Konum hariç tutma.** Reklam ancak kişinin hem ev hem mevcut konumu hariç bölgedeyse hiç gösterilmez.
3. Hesap düzeyinde **işveren** ve **marka koruma**.
4. **Sadece hariç tutma** müşteri listesi.

İlgi ve davranış hariç tutma yok.

### 6.2 Reçeteler

| Kampanya / set | Hariç tut | Neden |
|---|---|---|
| Soğuk / yeni müşteri (satış) | `OK_LISTE_MUS` + satın alma site kitlesi (180G; arayüz izin veriyorsa ve saklama süresi hukukçuca onaylandıysa 730G, §3.3) | Liste uzun geçmişi kapsar. Site kitlesi listede olmayan yeni alıcıyı yakalar (*sezgisel*) |
| Soğuk (potansiyel müşteri) | `OK_FORM_GONDEREN_90G` + CRM müşteri listesi | Aynı kişi tekrar form doldurmaz |
| Site yeniden pazarlama | Aynı ya da daha uzun pencerenin alıcıları | Almış kişiye sepet hatırlatması gitmez |
| Ayrı sıcak kampanya varken soğuk set | O kampanyanın kitlesi | İki set aynı kişi için yarışmaz (*sezgisel*) |
| Her kampanya, **yalnız** üç koşul birlikte sağlanırsa: hukukçu onayı kayıtlı, liste ≥ 1.000 satır, kampanya ÖRK değil | `OK_LISTE_HARICTUT-IZINSIZ` | Rızasını geri çekene (§4.4 tanımı) reklam gitmez |

**İzinsizler listesi koşulları sağlanmıyorsa** (*sezgisel*):
- Sadece hariç tutma listesi kurma. Rızasını geri çekenleri "Dahil etme veya hariç tutma" türündeki tüm listelerden çıkarmak yeterli (§4.4).
- Listeyi 1.000 satıra tamamlamak için başka kişi ekleme. Meta'nın biçim sayfası en az 1.000 müşteri istiyor [RESMİ]. Daha küçük bir sadece hariç tutma listesinin kabul edilip edilmediği hesapta doğrulanmalı.
- Sadece hariç tutma listeleri konut, istihdam, kredi ve finansal ürün ÖRK kampanyalarında kullanılamıyor [RESMİ].
- Rıza geri çekme aylık bakım döngüsünü beklemez. Çıkarma işlemi talep günü yapılır.

### 6.3 Sızıntı ve aşırı hariç tutma

- **Sızıntı normaldir.** Eşleşmeyen satırlar ve rızası olmadığı için dosyaya girmeyen müşteriler hariç tutmadan kaçar. Pratisyenler yeni müşteri kampanyalarının hariç tutmaya rağmen mevcut müşteriye kayda değer harcama yapabildiğini yazıyor [PRATİSYEN]. Sızıntıyı segment kırılımıyla ölç (§7.3).
- **Aşırı hariç tutma** kitleyi küçültür, CPA'yı yükseltir [RESMİ]. Yalnız bir iş kuralına dayanan hariç tutmayı koy: mevcut müşteri, izinsizler (§6.2 koşullarıyla), başka kampanyada hedeflenen sıcak kitle (*sezgisel*).
- Eski detaylı hariç tutmalı seti çoğaltırsan kopyada hariç tutma sessizce kaybolur [RESMİ]. Çoğaltmadan sonra kontrol et.

---

## 7. Yeni müşteri ve mevcut müşteri

### 7.1 Yöntem seçimi

Eski "mevcut müşteri bütçe üst sınırı" **kaldırıldı** [RESMİ]. Bazı 2026 pratisyen yazıları onu hâlâ anlatıyor. Resmi kaynağı esas al.

| Hedef | Yöntem | Kurulum [RESMİ] |
|---|---|---|
| Mevcut müşteriye hiç harcama yapılmasın | (a) Hariç tutma | Kontroller > Diğer kontrolleri göster > müşteri kitlesini hariç tut |
| Mevcut müşteriye sınırlı harcama | (b) İki set + harcama sınırı | Kampanya bütçesi kullan. **S2 (mevcut müşteri):** müşteri kitlesi dahil, öneri işareti kaldırılmış. Bütçe ve plan > Reklam seti harcama sınırları > **maksimum** (yüzde sınır ölçeklenir, tutar sabit kalır). **S1 (geniş):** S2'nin kopyası, aynı kitle hariç. İki sette aynı kreatif. Adlar §8.5 A ve Modül 02 §5.4 ile aynı |
| Meta yeni müşteriye öncelik versin | (c) "**Yeni müşteriler kazanın**" | Satış amacında set düzeyinde seçilir. Mevcut müşteriyi (isteğe bağlı etkileşimli kitleyi de) hariç tutar. CPA yükselebilir. Başarıyı yeni müşteri CPA'sıyla ölç |

**(c)'deki otomatik müşteri hariç tutması** [PRATİSYEN, Jon Loomer 13.10.2026, *doğrulanmalı*]:
- "Yeni müşteriler kazanın" seçilince Meta, reklam hesabının dönüşüm verisinden otomatik bir müşteri hariç tutması ekliyor. Kaynak, satın alma olayını tetikleyen kişiler.
- Bu hariç tutma **kapatılamıyor**. Pencere seçilebiliyor: varsayılan 730 gün, diğer seçenekler 180, 90, 60 ve 30 gün.
- Satın alma olayı olarak Meta'ya gitmeyen satışlar (mağaza, telefon, kapıda ödeme ile sonradan girilen) bu hariç tutmaya girmez. Onlar için müşteri listesi hariç tutması yine gerekebilir.
- Pencereyi brifteki tekrar alım döngüsüne göre seç. Kaynak Piksel olduğu için kapsam çerez rızasına bağlıdır (§4.4).

**Eğer-ise:**
- Müşteri listesi yoksa (c) ve segment tanımı yalnız Piksel kaynaklarıyla çalışır. Önce listeyi kur.
- **Mevcut müşteriye ayrı set açıyorsan ROAS hedefini artırımlılık katsayısıyla ayarla.**
  - Katsayı = artırımlı gelir ÷ Meta'nın 7 gün tıklama ile raporladığı gelir. CTC'nin coğrafi test veritabanındaki medyan: mevcut müşteri **0,60**, yeni müşteri **1,14** (yeni müşteri için 45 test) [PRATİSYEN, tek kaynak].
  - 0,60, mevcut müşteri reklamının zarar ettiği anlamına gelmez. Meta'nın raporladığı her 1 TL gelirin yaklaşık 0,60 TL'si reklam olmasa da gelmeyecek demektir.
  - **Formül** (*türetilmiş*): ROAS_hedef (panel) ≈ başabaş panel ROAS ÷ katsayı.
  - **Örnek** (*türetilmiş*, Modül 02 §1 örneği): Başabaş 3,04 ise mevcut müşteri seti için ROAS hedefi ≈ 3,04 ÷ 0,60 ≈ **5,1**.
  - Yeni müşteri setinde katsayı 1'in üstünde olsa da hedefi başabaşın altına indirme. Modül 02 §7'deki max(…; başabaş) kuralı geçerli (*sezgisel*).
  - **Kampanya bütçesinde** tüm setler aynı teklif stratejisini kullanır (Modül 02 §6) [RESMİ]. CTC ROAS hedefi tutarını set düzeyinde ayrı giriyor [PRATİSYEN]. Bunu hesapta doğrula. Set düzeyinde girilemiyorsa iki yol var: mevcut müşteri setini kendi teklif stratejisiyle ayrı bir kampanyaya al ya da yalnız harcama sınırıyla yönet.

### 7.2 Hedef kitle segmentleri [RESMİ]

- **Yol:** Tüm araçlar > Reklam ayarları > Reklam oluşturma > **Hedef kitle segmentleri**.
- **Etkileşimli kitle** kaynakları: site, uygulama, katalog, müşteri listesi, çevrimdışı, alışveriş.
- **Mevcut müşteri** kaynakları: bunlara ek olarak formlar.
- Tanım tüm satış kampanyalarına uygulanır ve yeni/etkileşimli/mevcut kırılımını verir.
- Kademeli açılıyor.

### 7.3 Formüller (*türetilmiş*)

- **Yeni müşteri CPA (Meta)** = "Yeni kitle" segmentinin harcaması ÷ o segmentteki satın alma sayısı.
- **Yeni müşteri CPA (gerçek)** = toplam harcama × 1,05 ÷ işletme panelindeki ilk sipariş sayısı. 1,05 konum ücreti çarpanıdır (Modül 01 §3.2).
- **Sızıntı oranı** = mevcut müşteri segmentinin harcaması ÷ toplam harcama.

**Örnek** (*varsayım*): Haftalık harcama 42.000 TL. Yeni kitleye 30.000 TL harcanmış ve 95 satın alma gelmiş. Mevcut müşteriye 5.000 TL harcanmış. İşletme paneli 110 ilk sipariş gösteriyor.
- Yeni müşteri CPA (Meta) ≈ 316 TL.
- Sızıntı ≈ %12.
- Gerçek yeni müşteri CPA = 42.000 × 1,05 ÷ 110 ≈ 401 TL.
- Kararı başabaş CPA'ya göre ver (Modül 02 F1–F3).

---

## 8. Yeniden pazarlama: pencere, büyüklük, bütçe

### 8.1 Ayrı set açmalı mı?

Advantage+ açıkken Meta sıcak kitleye zaten harcar. Bir pratisyen bu payı segment raporunda kabaca %20–25 (bazen %10–40) gördüğünü yazıyor [PRATİSYEN]. Bütçe payları için Modül 02 §3.3'e bak.

Tek kural (*türetilmiş*, Modül 02 F5 ve §3.3 ile hizalı). N = haftalık beklenen sonuç (Modül 02 F4). F5'e göre iki set için N ≈ 100 gerekir.

| Eğer… | O zaman… |
|---|---|
| N < 100 ya da tek setlik bütçe | **Ayrı sıcak set açma.** İkinci set kapasitesi yok. Sıcak kitleyi ana sete öneri olarak ekle. Mevcut müşteri harcamasını hariç tutma ya da "Yeni müşteriler kazanın" ile yönet (§7.1 a, c) |
| 100 ≤ N < 150 | İkinci set yalnız **mevcut müşteri seti** olabilir (harcama sınırlı, §7.1 b). Ayrı sıcak set açma |
| N ≥ 150, yeniden pazarlama kitlesi büyük, §8.3 tavanı öğrenme bütçesini karşılıyor ve sıcak kitleye gerçekten farklı mesaj gerekiyor (sepet teklifi, randevu hatırlatma) | Ayrı sıcak set ya da kampanya açılabilir. Advantage+ kapalı. Soğuk sette bu kitleyi hariç tut. Toplam set sayısı yine F5'i geçmez |
| N ≥ 150 ama tavan, öğrenme bütçesinin altında | Set öğrenmeyle sınırlı kalır. Bunu kabul et ya da kitleyi büyüt (pencereyi uzat, olayları birleştir) |
| Sezon zirvesi (11.11, Kara Cuma) | Sıcak payı geçici artır. Yeni müşteri harcamasını sıfırlama [PRATİSYEN] |

### 8.2 Pencere seçimi (*sezgisel*)

| Sinyal | Pencere | Mesaj açısı |
|---|---|---|
| Sepete ekleyip ya da ödeme başlatıp almayan | 7–14 gün | Hatırlatma, kargo/iade güvencesi |
| Ürün ya da hizmet sayfası gören | 14–30 gün | Sosyal kanıt, SSS |
| Tüm site ziyaretçileri | 30–180 gün | Teklif, yeni ürün |
| Formu açıp göndermeyen | 7–30 gün (en çok 90) | İtiraz kırıcı mesaj |
| IG/FB etkileşimi, video izleyen | 30–365 gün | Tanıtım, teklif |
| Alıcı (çapraz satış) | Tekrar alım döngüsü kadar | Tamamlayıcı ürün |

Pratisyen kalıbı: 7 gün acil niyet, 30 gün hatırlatma, 90–180 gün uzun dönem [PRATİSYEN]. **Kural** (*sezgisel*): Pencere en az müşterinin ortalama karar süresi kadar olmalı. Bu süre brifte sorulur (Modül 01 §1).

### 8.3 Kitle büyüklüğü ve bütçe tavanı

**Resmi eşikler [RESMİ]:**
- Benzer kitle kaynağı: ≥100.
- Müşteri listesi: ≥1.000 satır.
- Site kitlesi: kullanmadan önce "birkaç yüz" kişi.
- Çakışma aracı: ≥10.000.
- Yayın için asgari kitle: resmi sayı bulunamadı. **Durum** sütununa bak.

**Formüller** (*türetilmiş*):
- **R1 Kitle tahmini:** K ≈ U_P × r_rıza × r_eşleşme
  - U_P = analitik aracındaki P günlük tekil kullanıcı.
  - r_rıza = çerez onayı oranı.
  - r_eşleşme = Meta eşleşme oranı (*varsayım*, kitle kurulunca gerçek boyutla değiştir).
- **R2 Haftalık bütçe tavanı:** B_tavan ≈ K × S_hafta × CPM ÷ 1000
  - S_hafta = kabul edilen haftalık sıklık. Pratisyenler soğukta 7 günlük sıklık 2,5–3'te yorgunluk gözlüyor, sıcakta sıklığın 5–10+'ya çıkabildiğini yazıyor. Bunlar eşik değil, gözlem [PRATİSYEN].
  - CPM = hesabın sıcak kitle CPM'i.
  - B_tavan bir üst sınırdır, çünkü kitlenin hepsi her hafta çevrim içi olmaz.
- **R3 Öğrenme bütçesi:** B_öğrenme ≈ CPA_sıcak × 50 (haftalık; Modül 02 F5).
- **Karar:**
  - B_planlanan > B_tavan → bütçeyi düşür ya da kitleyi büyüt.
  - B_tavan < B_öğrenme → ayrı set öğrenmeyle sınırlı kalır (§8.1).

**Tavanı her kitle için ayrı hesapla.** R1–R2'yi hangi kitleye harcanacaksa onun büyüklüğüyle çalıştır. Alt kitlenin (ör. sepeti terk edenler) tavanı, ana kitlenin (tüm ziyaretçiler) tavanından çok daha düşüktür.

**Örnek** (*varsayım*, Modül 02'deki giyim sitesi):
- **Tüm ziyaretçiler, 30 gün:** U_30 = 20.000, r_rıza = 0,6, r_eşleşme = 0,6 → K ≈ 7.200. S_hafta = 4, CPM = 80 TL → B_tavan ≈ 2.300 TL/hafta (~330 TL/gün).
- **Sepeti terk edenler, 14 gün** (`OK_WEB_ATC-PURharic_14G`): U_ATC_14 = 1.500 (*varsayım*). K ≈ 1.500 × 0,6 × 0,6 = 540. Aynı S_hafta ve CPM ile B_tavan ≈ 540 × 4 × 80 ÷ 1000 ≈ 170 TL/hafta (~25 TL/gün).
- CPA_sıcak = 150 TL → B_öğrenme ≈ 7.500 TL/hafta.
- **Sonuç:** İki kitlenin tavanı da öğrenme bütçesinin çok altında. Ayrı set öğrenmeden çıkamaz ve sıklığı hızla yükselir. Sıcak kitleyi ana kampanyada bırak. Süreli bir sepet teklifi şartsa sepet setinin bütçesi kendi tavanını (bu örnekte ~25 TL/gün) aşmamalı. Tüm ziyaretçi kitlesinin tavanını sepet setine uygulama.

### 8.4 Küçük ve yerel işletme

Trafik azsa kısa pencereli kitleler birkaç yüz kişinin altında kalır. Tek bir birleşik sıcak kitle kur (*sezgisel*, Meta'nın "kuralı azalt, pencereyi uzat" önerisiyle uyumlu):
- Sayfayla etkileşim kuran herkes (365 gün),
- Instagram profiliyle etkileşim kuran herkes (365 gün),
- tüm site ziyaretçileri (180 gün).

### 8.5 Doldurulmuş kurgular (*varsayım*)

**A) E-ticaret, 6.000 TL/gün (Modül 02 Şablon C):**
```
K1 SAT | Advantage+ satış | kampanya bütçesi
 ├─ S1 GENIS | Advantage+ açık | hariç: OK_LISTE_MUS, OK_WEB_PUR_<gerçek pencere>G
 │            (+ OK_LISTE_HARICTUT-IZINSIZ, yalnız §6.2 koşulları sağlanıyorsa)
 └─ S2 MUS  | OK_LISTE_MUS dahil, öneri işareti kaldırılmış | maks. harcama %15
            | ROAS hedefi artırımlılık katsayısıyla (§7.1)
Ayrı yeniden pazarlama yok: N = 140 < 150 (§8.1) ve §8.3 tavanı yetersiz.
```
- **Sepet hatırlatması** (*sezgisel*): S1'deki Advantage+ katalog reklamının sepeti terk edenlere de ürün göstermesi beklenir. Bu garanti değildir, Advantage+ açık geniş sette kime gösterileceğine Meta karar verir.
- **Kontrol:** Haftalık segment kırılımında "etkileşimli kitle" payını ve bu segmentin harcamasını izle (§7.2; segment tanımında sepete ekleme kaynağı olmalı). Pay sıfıra yakınsa ya da düşüyorsa, sepet kitlesini S1'e öneri olarak ekle.

**B) Dil kursu, form, 2.000 TL/gün:** Tek set S1 GENIS (Advantage+).
- Hariç: `OK_FORM_GONDEREN_90G`.
- Öneri: `OK_FORM_ACIP-GONDERMEYEN_30G` + `OK_WEB_TUM_30G`.
- N ≈ 93/hafta < 100 olduğu için ikinci set kapasitesi yok. Ayrı sıcak set açılmaz (§8.1 ilk satır; Modül 02 F5, §5.3).

**C) Kafe, WhatsApp, 400 TL/gün:** Tek set, iğne + yarıçap (§9.5), yeniden pazarlama yok.

---

## 9. Yerel konum hedeflemesi

### 9.1 Mekanik [RESMİ]

- Hedefleme o yerde **yaşayan, yakın zamanda bulunan ya da sık giden** kişileri kapsar. "Yalnızca burada yaşayanlar" seçeneği artık yok.
- Konum dışından gösterim, mesaj ya da form gelebilir. Organik paylaşım da konum dışı etkileşim getirir.
- Sınırlar: 25 ülke, 250 şehir, 50.000 posta kodu. Çok konumda toplu yükleme kullan.
- Yarıçap ülke sınırını aşmaz. İl, bölge, ülke ve posta kodunda yarıçap seçilemez.
- API'de yarıçap iğne için **1–80 km**, şehir için **17–80 km**. Reklam Yöneticisi'ndeki sınırlar konum türüne göre değişir (*doğrulanmalı*).
- Çok konumda yarıçap 1815946 alt kodlu hata verebilir. Meta her konum için ayrı reklam ya da yarıçapsız konum öneriyor.
- **18 yaş altı** kitledeyse şehirden küçük konum, dil, cinsiyet, detaylı hedefleme, özel ve benzer kitleler kullanılamaz. Yerel hedeflemede min. yaşı **18** yap.

### 9.2 "Reklamlarınıza yanıt verme olasılığı yüksek olan daha fazla kişiye erişin" [RESMİ]

- Şehir ya da bölge seçilince **varsayılan işaretli** gelir.
- Açıkken ülke içinden o yerle ilgili kişiler de eklenir: yakında bulunmuş, o yeri aramış, oradaki içerikle etkileşim kurmuş, orada arkadaşı olan ya da yakın kasabada yaşayan kişiler.
- Meta'ya göre turizm, eğlence, restoran ve uzaktan hizmet için yararlı olabilir. Katı yerel hizmette ve hassas sektörlerde kapatılması öneriliyor.

### 9.3 Karar tablosu

| İşletme | Konum türü | Yarıçap (*sezgisel*) | Kutu |
|---|---|---|---|
| Yürüyerek gelinen (kafe, kuaför, butik) | İğne | 1–5 km | İğnede çıkmaz |
| Araçla gelinen (oto servis, mobilya) | İğne | 10–25 km | — |
| Hizmet bölgesi olan (tesisat, temizlik, nakliyat) | Hizmet verilen ilçeler. Listede yoksa birkaç iğne | İlçe sınırı: her ilçede **"Yalnızca şu anki şehir"** seçili, yarıçap yok | **Kapalı** |
| Turistik bölgede restoran, otel, etkinlik | Şehir ya da iğne | — | Açık olabilir [RESMİ] |
| Uzaktan hizmet (danışman, online ders) | Şehir ya da ülke | — | Açık olabilir [RESMİ] |
| Çok şubeli işletme | Tek sette birden çok iğne | Hepsinde aynı | Şubeyi formda sor |

**Şehir ya da ilçe seçtiysen iki ayar birlikte gerekir** [RESMİ]:
1. **Yarıçap.** Meta konum eklenince varsayılan bir yarıçap seçiyor. API'de şehir yarıçapının alt sınırı 17 km (§9.1). Konumun yanındaki yarıçap simgesine tıkla ve **"Yalnızca şu anki şehir"** seçeneğini işaretle.
2. **Kutu.** "Reklamlarınıza yanıt verme olasılığı yüksek olan daha fazla kişiye erişin" kutusunun işaretini kaldır (§9.2).

Yalnız kutuyu kapatmak yetmez. Yarıçap kalırsa reklam ilçe sınırının ötesine (ör. Üsküdar'dan Avrupa yakasına) taşar (*türetilmiş*). "Yalnızca şu anki şehir" seçeneği sosyal mesele, seçim ve siyaset reklamlarında yok [RESMİ]. Seçenek görünmüyorsa bu hesaba göre değişebilir. O zaman ilçe merkezine iğne ve küçük yarıçap kullan (*sezgisel*).

### 9.4 Dil [RESMİ]

- Meta dili sinyallerden tahmin eder.
- Dil seçimi yalnız o yerde **yaygın olmayan** bir dil için önerilir. Türkiye'de örnekler: İngilizce, Arapça, Rusça, Almanca.
- Türkçe reklamda dil alanını **boş bırak**.
  - **Öncelik notu:** Bu kural, Kurulum Kılavuzu §5.3'teki "Türkçe konuşmayanlara gösterilmesin istiyorsan Türkçe seç" önerisinin yerine geçer. Meta dil seçimini yalnız o konumda yaygın olmayan diller için öneriyor [RESMİ]. İki belge çelişirse bu madde geçerlidir.
- Kuzey Kürtçesi (Kurmanci) seçilebilir diller arasında.
- Kreatif ve açılış sayfası seçilen dille eşleşmeli.

### 9.5 Örnekler (*varsayım*)

- **Kadıköy kafesi (Modül 02 Şablon A):** İğne kafenin adresinde, 3 km. Min. yaş 18, dil boş, Advantage+ açık, öneri yok. Haftalık Kırılım > Teslimat > Bölge kontrolü.
- **Üsküdar tesisatçısı (Şablon D):** Hizmet verilen ilçeler konum olarak eklenir. Listede yoksa her ilçeye bir iğne. İlçe "şehir" olarak listelendiyse her birinde yarıçap simgesinden **"Yalnızca şu anki şehir"** seçilir ve kutunun işareti kaldırılır. İkisi birlikte yapılır (§9.3). Sonra harita önizlemesinde alanın Boğaz'ı geçmediğini kontrol et. İlçe WhatsApp'ta ya da formda sorulur.
- **Kapadokya butik oteli:** Konum, misafirin yaşadığı yerdir (ülke ya da yurt dışı şehirler), otelin bulunduğu yer değil (*sezgisel*).

Konum dışından sonuç gelmesi normaldir [RESMİ]. Konum dışı pay haftadan haftaya artıyorsa sırayla kutuyu, öneri ayarlarını ve organik paylaşımları kontrol et (*sezgisel*).

---

## 10. Örtüşme ve sık hatalar

### 10.1 Örtüşme aracı [RESMİ]

- **Yol:** Hedef Kitleler → en fazla **5** kitle seç → Eylemler → **Hedef kitle çakışmalarını göster**.
- Anlamlı sonuç için kitleler en az **10.000** olmalı.
- Yüzde seçilen kitleye göre hesaplanır. Meta'nın örneği: 1 milyonluk A ile 100 milyonluk B'nin 800 bin ortak kişisi var. Bu A'nın %80'i, B'nin %8'idir. Küçük kitleyi hedefleyen set daha çok yayın sorunu yaşar.
- Aynı reklamverenin setleri aynı açık artırmaya girerse biri gösterilmeyebilir.

**Karar kuralı** (*sezgisel*; Meta sabit bir eşik vermiyor):
- **Eğer** iki set aynı olaya optimize ediyor, aynı kreatifi kullanıyor ve küçük kitlenin kabaca %20'sinden fazlası ortaksa **o zaman** setleri birleştir ya da küçük kitleyi büyük sette hariç tut.
- Kitleler 10.000'den küçükse aracı kullanma. Şunu sor: sıcak kitle soğuk sette hariç tutuluyor mu?

### 10.2 Sık hatalar

| Hata | Belirti | Düzeltme |
|---|---|---|
| **Aşırı daraltma:** dar yaş, tek cinsiyet, yığılmış ilgi alanları | Yüksek CPM, "Öğrenmeyle sınırlı", kitle genişletme tavsiyesi | Advantage+'ı aç, değer kuralı kullan. Küçük kitle CPA'yı 2 katına çıkarabilir [RESMİ] |
| **İlgi dilimlerine bölme** ("köpek", "köpek oyuncağı" ayrı setler) | Her set az sonuç alır, birleştirme tavsiyesi çıkar | Tek sette topla, kırılımla izle [RESMİ] |
| **Yeniden pazarlama setinde öneri işaretini bırakmak** | "Sıcak" set soğuğa harcar, sıklık düşük | İşareti kaldır [RESMİ] |
| **Ayrı sıcak kampanya varken soğuk sette hariç tutmamak** | İki set aynı kişi için yarışır | Sıcak kitleyi soğuk sette hariç tut (*sezgisel*) |
| **Şehir + kutu açık ya da varsayılan yarıçap, katı yerel hizmet** | Başka ilçeden ya da ilden talep | Kutuyu kapat ve "Yalnızca şu anki şehir"i seç ya da iğne kullan (§9.3) [RESMİ] |
| **İş ilanı ya da emlakta yaş/cinsiyet daraltmak** | "Erkek 20–35" gibi set | Yaş ve cinsiyeti kaldır, Advantage+ geniş kullan (§2.1) [MEVZUAT] |
| **Tahmini büyüklüğe güvenmek** | Büyük kitle görünür, sonuç az | Sonuç verisine bak [RESMİ] |
| **Listeyi güncellememek** | "Güncel değil", yeni alıcılara reklam | 30 günde bir güncelle |
| **Eski hariç tutmalı seti çoğaltmak** | Hariç tutma sessizce kaybolur | Çoğaltmadan sonra kontrol et [RESMİ] |
| **Hassas kitle adı ya da kural** | Kitle işaretlenir | Nötr ad. Kaynağı düzelt, yeni kitle kur [RESMİ] |
| **Maks. yaşı kesin sınır sanmak** | Üst yaştan sonuç gelir | Kırılımla izle. Kesinse manuel kurulum [PRATİSYEN] |
| **13–17 yaşı katıp iğne kullanmak** | İğne ve özel kitle devre dışı | Min. yaş 18, veliyi hedefle [RESMİ] |
| **Sık hedefleme değişikliği** | Set sürekli öğrenmede | Değişikliği haftada bir, toplu yap (Modül 02 §9.3) |

---

## 11. Kontrol listeleri

### 11.1 Yayından önce

- [ ] Kesin sınırlar kontrollerde. Her kampanyaya uygulanacak kurallar hesap düzeyinde.
- [ ] Advantage+ durumu §2.1'e göre seçildi. Sıcak ve mevcut müşteri setlerinde öneri işareti kaldırıldı.
- [ ] Soğuk sette `OK_LISTE_MUS` + satın alma site kitlesi hariç. İzinsizler listesi yalnız §6.2 koşulları sağlanıyorsa. Sağlanmıyorsa rızasını geri çekenler tüm listelerden çıkarıldı.
- [ ] Şehir/ilçe seçimlerinde yarıçap yok ("Yalnızca şu anki şehir" seçili). Kutu bilinçli olarak açık ya da kapalı. İğne varsa min. yaş 18.
- [ ] Özel kitle, benzer kitle ya da detaylı hedefleme kullanan her sette min. yaş 18 (md. 25/A).
- [ ] Sitede "Reklam tercihleri" bölümü var ve açılış sayfasından ulaşılıyor (§4.4, md. 25/A).
- [ ] İstihdam, konut ya da kredi reklamında yaş aralığı ve cinsiyet girilmedi (§2.1).
- [ ] Dil boş ya da kreatifle eşleşen yabancı dil. 18+ üründe WhatsApp bilinmeyen yaş kapalı.
- [ ] Kitleler "Hazır". Adlar §3.3'e uygun ve gerçek tutma süresini gösteriyor. Hassas çağrışım yok.
- [ ] Müşteri listesi §4.4'ten geçti: üç koşul sağlandı, dosya rıza bayrağına göre filtrelendi. Tanımlayıcı eşlemede tüm sütunlarda onay işareti var.
- [ ] Ayrı yeniden pazarlama seti varsa §8.3 tavanı o setin kendi kitlesiyle hesaplandı.
- [ ] Çakışan set yok (§10.1). Set sayısı ≤ Modül 02 F5.

### 11.2 Bakım

- **Haftalık:**
  - Segment kırılımı: yeni kitle payı, sızıntı oranı, yeni müşteri CPA'sı.
  - Konum, yaş ve cinsiyet kırılımları.
  - Sıcak setlerde sıklık ve CPA eğilimi.
- **Talep gününde (döngü beklemez):**
  - Rızasını geri çeken kişiyi tüm listelerden "Müşterileri çıkarın" ile çıkar (§4.4).
- **Aylık:**
  - Müşteri listesini ve (kullanılıyorsa) izinsizler listesini güncelle. Yeni dosyayı yine rıza bayrağına göre filtrele.
  - "Güncel değil" ya da işaretli kitle var mı kontrol et.
  - Kitlelerin gerçek tutma süresi adlarıyla uyumlu mu kontrol et (§3.3).
  - Kullanılmayan kitleleri sil.
- **Çeyreklik:**
  - Advantage+ ile manuel kurulumu A/B testiyle karşılaştır.
  - Benzer kitle kaynağını son 90 günün alıcılarıyla yenile.

---

## Doğrulanması gerekenler

- **Satın alma kitlesinde 730 gün:** Pratisyenler 18 Mayıs 2026'dan beri geçerli olduğunu ve mevcut 180 günlük satın alma kitlelerinin itiraz edilmediyse otomatik olarak 730 güne çevrildiğini Meta bildirimine atıfla aktarıyor. Geliştirici belgesi hâlâ 1–180 gün diyor. Arayüzde her kitlenin gerçek tutma süresine bakılmalı.
- **"Yeni müşteriler kazanın" otomatik hariç tutması:** Kapatılamaması, pencere seçenekleri (730/180/90/60/30) ve kapsamı tek bir pratisyen gözlemine dayanıyor (Jon Loomer, 13.10.2026). Resmi sayfa ve Türk hesaplarında görünürlük doğrulanmalı.
- **Kampanya bütçesinde set düzeyinde ROAS hedefi:** Hedef tutarının setlere ayrı girilebildiği pratisyen kaynağına dayanıyor. Resmi sayfa yalnız "tüm setler için aynı teklif stratejisi" diyor.
- **Küçük sadece hariç tutma listesi:** 1.000 satırın altındaki bir sadece hariç tutma listesinin yüklenip yüklenemediği hesapta denenmeli.
- **"Yalnızca şu anki şehir" seçeneği:** Türk ilçelerinde görünüp görünmediği hesapta doğrulanmalı.
- **Özel Reklam Kategorilerinin Türkiye'deki durumu (araştırma notu B-6):** Türkiye'den Türkiye'ye yayınlanan konut, istihdam ve finans reklamlarında kategori beyanının ve hedefleme kısıtlarının nasıl uygulandığı net değil. Yardım sayfasındaki zorunlu ülke listesinde Türkiye yok. Hesapta doğrulanmalı.
- **6701 sayılı Kanun ve hedefleme:** Reklam hedeflemesinde yaş ya da cinsiyet daraltmanın m.5–m.6 kapsamında ayrımcılık sayılıp sayılmadığına dair TİHEK kararı bulunamadı. Kredinin m.5/1'deki "benzeri hizmetler" içinde olup olmadığı belirsiz. Hukukçuya sorulmalı.
- **Etkileşim kitlesi azami süresi:** Yardım merkezi 365 gün, geliştirici belgesi 730 gün diyor. Arayüzün izin verdiği değer esas alınmalı.
- **Video kitlesi:** Azami tutma süresi ve eşikler (3 sn, ThruPlay, %25–95) resmi sayfadan doğrulanamadı. Pratisyen yazıları 365 gün diyor (yalnız arama özetinden görüldü).
- **Yarıçap sınırları:** API değerleri Reklam Yöneticisi'nde doğrulanmalı. Hangi Türk ilçelerinin "şehir" olarak listelendiği hesapta aranmalı.
- **Kademeli özellikler:** Hedef kitle segmentleri, "Yeni müşteriler kazanın" ve hesap düzeyi kontroller her Türk hesabında görünmeyebilir.
- **Detaylı hedefleme birleştirmesi:** Kaldırılan seçeneklerin listesi yok. Yayının durma tarihinde kaynaklar arasında çelişki var.
- **KVKK m.9 aktarım mekanizması:** Meta'nın Türkiye için güvence sunup sunmadığı bulunamadı. Karmalı verinin kişisel veri sayılıp sayılmadığına dair Kurul kararı yok. Hukukçuya sorulmalı.
- **TRHTUY md. 25/A:** Meta'nın "Bu reklamı neden görüyorum?" özelliğinin yeterli olup olmadığı belirsiz. Sitedeki "Reklam tercihleri" bölümünün (§4.4) şartı karşıladığı da bir varsayım. Uygulama örneği ya da Kurul kararı bulunamadı.
- **İlke kararı 2026/347:** Resmî Gazete PDF'i taranmış görsel olduğu için içerik pratisyen özetinden alındı.
- **Pratisyen sayıları:** Eşleşme %20–70, sıcak pay %20–25, sıklık gözlemleri ve Confect'in %7 rakamı birincil kaynaktan doğrulanmadı.

---

## Kaynaklar

**Meta yardım merkezi [RESMİ]** (erişim Ekim 2026; Türkçesi için adrese `?locale=tr_TR` ekle)
- Advantage+ hedef kitle hakkında: https://www.facebook.com/business/help/273363992030035
- Hedef kitle kontrolleri ve önerileri: https://www.facebook.com/business/help/938372127764391
- Advantage+ hedef kitle açık/kapalı durumu: https://www.facebook.com/business/help/906206294602874
- Advantage+ hedef kitle ayrıntıları: https://www.facebook.com/business/help/793748385630490
- Hariç tutmaların kontrol olarak uygulanması: https://www.facebook.com/business/help/25941857932125812
- Hesap düzeyi hedef kitle kontrolleri: https://www.facebook.com/business/help/1438478636941047
- Tahmini kitle büyüklüğü: https://www.facebook.com/business/help/1665333080167380
- Advantage+ detaylı hedefleme: https://www.facebook.com/business/help/128066880933676
- Hedef kitle genişletme tavsiyesi: https://www.facebook.com/business/help/1098451937465750
- WhatsApp'ta bilinmeyen yaş: https://www.facebook.com/business/help/717368264947302
- Değer kuralları: https://www.facebook.com/business/help/535014515741813
- Özel hedef kitle türleri: https://www.facebook.com/business/help/744354708981227
- Etkileşim kitleleri: https://www.facebook.com/business/help/1090330204367211
- Site kitlesi oluşturma: https://www.facebook.com/business/help/1474662202748341
- Site kitlesi boyutu: https://www.facebook.com/business/help/1523083297982009
- Instagram hesabı kitlesi: https://www.facebook.com/business/help/214981095688584
- Facebook Sayfası kitlesi: https://www.facebook.com/business/help/221146184973131
- Video kitlesi: https://www.facebook.com/business/help/1099865760056389
- Form kitlesi: https://www.facebook.com/business/help/900802126698360
- Sadece hariç tutma kitleleri: https://www.facebook.com/business/help/1011010098188443
- Müşteri listesi biçimi: https://www.facebook.com/business/help/2082575038703844
- Müşteri listesi oluşturma: https://www.facebook.com/business/help/170456843145568
- Müşteri listesini düzenleme: https://www.facebook.com/business/help/403733729689975
- Hedef kitle paylaşma: https://www.facebook.com/business/help/499290663823687
- Karma işlemi: https://www.facebook.com/business/help/112061095610075
- Listeye konamayacak bilgiler: https://www.facebook.com/business/help/606443329504150
- Benzer kitleler hakkında: https://www.facebook.com/business/help/164749007013531
- Benzer kitle oluşturma: https://www.facebook.com/business/help/465262276878947
- Advantage+ benzer kitle: https://www.facebook.com/business/help/1212225059146059
- Değer tabanlı benzer kitle: https://www.facebook.com/business/help/458132024732845
- İşaretlenen kitleler: https://www.facebook.com/business/help/1055828013359808
- Konum hedefleme: https://www.facebook.com/business/help/365561350785642
- Konum hedeflemesi nasıl çalışır: https://www.facebook.com/business/help/202297959811696
- "Daha fazla kişiye erişin": https://www.facebook.com/business/help/726389026372510
- 18 yaş altı kısıtları: https://www.facebook.com/business/help/229435355723442
- Dil hedeflemesi: https://www.facebook.com/business/help/1024084489118213
- Özel reklam kategorileri: https://www.facebook.com/business/help/2220749868045706
- Hedef kitle çakışması aracı: https://www.facebook.com/business/help/1679591828938781
- Açık artırma çakışması: https://www.facebook.com/business/help/537699989762051
- Hedef kitle bölünmesi: https://www.facebook.com/business/help/2419480091640105
- Mevcut müşteri harcamasını sınırlama: https://www.facebook.com/business/help/1142614290190459
- Hedef kitle segmentleri: https://www.facebook.com/business/help/1212227546879178
- Hedef kitle segmentleri raporu: https://www.facebook.com/business/help/1362234537597370
- "Yeni müşteriler kazanın": https://www.facebook.com/business/help/1740191093676743
- Konut, istihdam ve finansal ürün ve hizmet reklamları (ÖRK zorunlu bölgeler, Ayrımcı Uygulamalar ilkesi): https://www.facebook.com/business/help/399587795372584
- Reklam inceleme ve ret gerekçeleri (ayrımcılık): https://www.facebook.com/business/help/1210227555661027
- Advantage+ kampanya bütçesini ayarlama (setlerde aynı teklif stratejisi): https://www.facebook.com/business/help/343242619559352

**Meta geliştirici, mühendislik, koşullar [RESMİ]**
- Site özel hedef kitleleri: https://developers.facebook.com/docs/marketing-api/audiences/guides/website-custom-audiences
- Etkileşim özel hedef kitleleri: https://developers.facebook.com/docs/marketing-api/audiences/guides/engagement-custom-audiences
- Özel hedef kitleler ve karma: https://developers.facebook.com/docs/marketing-api/audiences/guides/custom-audiences
- Benzer hedef kitleler: https://developers.facebook.com/docs/marketing-api/audiences/guides/lookalike-audiences
- Temel hedefleme (konum, yarıçap): https://developers.facebook.com/docs/marketing-api/audiences/reference/basic-targeting
- Detaylı hedefleme hariç tutmalarının kaldırılması (21.01.2025): https://developers.facebook.com/blog/post/2025/01/21/removal-of-detailed-targeting-exclusions/
- Satış ve uygulama için Advantage+ kampanya deneyimi (03.06.2025): https://developers.facebook.com/blog/post/2025/06/03/advantage-plus-campaign-experience-for-sales-and-app/
- Andromeda (02.12.2024): https://engineering.fb.com/2024/12/02/production-engineering/meta-andromeda-advantage-automation-next-gen-personalized-ads-retrieval-engine/
- Özel hedef kitle koşulları: https://www.facebook.com/legal/terms/customaudience

**Mevzuat [MEVZUAT]**
- KVKK, yurt dışına aktarım: https://www.kvkk.gov.tr/Icerik/2053/Yurtdisina-Aktarim
- KVKK, üçüncü kişilerden elde edilen verilerle pazarlama (21.07.2026): https://www.kvkk.gov.tr/Icerik/8830/ucuncu-kisilerden-elde-edilen-kisisel-verilerin-reklam-ve-pazarlama-amacli-kullanilmasina-iliskin-kamuoyu-duyurusu
- KVKK Çerez Uygulamaları Hakkında Rehber (Temmuz 2025): https://www.kvkk.gov.tr/SharedFolderServer/CMSFiles/fb193dbb-b159-4221-8a7b-3addc083d33f.pdf
- 6698 sayılı Kanun metni (m.4): https://mgm.adalet.gov.tr/Resimler/SayfaDokuman/51120191031036698%20KVKK.pdf
- TRHTUY değişikliği, md. 25/A (RG 01.07.2026; Madde 9 ekler, Madde 15 yürürlük 1.8.2026): https://www.resmigazete.gov.tr/eskiler/2026/07/20260701-9.htm
- 6698 sayılı Kanun güncel metni (m.4/2-d, m.5, m.10, m.12): https://www.mevzuat.gov.tr/mevzuatmetin/1.5.6698.pdf
- KVKK ilke kararı 2026/347, aydınlatma ve açık rıza (RG 24.03.2026, taranmış PDF): https://www.resmigazete.gov.tr/eskiler/2026/03/20260324-3.pdf
- 6701 sayılı Türkiye İnsan Hakları ve Eşitlik Kurumu Kanunu (m.3, m.5, m.6, m.7): https://www.mevzuat.gov.tr/mevzuatmetin/1.5.6701.pdf
- 4857 sayılı İş Kanunu (m.5): https://www.mevzuat.gov.tr/mevzuatmetin/1.5.4857.pdf

**Pratisyen ve haber [PRATİSYEN] [HABER]**
- Jon Loomer, Advantage+ hedef kitle nasıl çalışır: https://www.jonloomer.com/how-advantage-plus-audience-works/
- Jon Loomer, Advantage+ hedef kitle en iyi uygulamalar: https://www.jonloomer.com/advantage-plus-audience-best-practices-guide/
- Jon Loomer, konum hedeflemesindeki değişiklik: https://www.jonloomer.com/big-change-to-meta-ads-location-targeting/
- Jon Loomer, Meta Ads master brief: https://www.jonloomer.com/meta-ads-master-brief/
- Lineardesign, Advantage+ hedef kitle (16.07.2026): https://lineardesign.com/blog/metas-advantage-audience/
- Dotidot, benzer kitleler 2026: https://www.dotidot.io/post/lookalike-audiences-meta-what-works-now-in-2026
- AdsUploader, özel kitle hariç tutmaları: https://adsuploader.com/blog/facebook-custom-audience-exclusions
- Lebesgue, yeniden hedefleme pencereleri: https://lebesgue.io/facebook-ads/facebook-retargeting-ads-how-to-retarget-your-audience
- Alex Neiman, satın alma kitlesinde 730 gün (11.05.2026): https://alexneiman.com/meta-purchase-audience-730-days-dtc-remarketing-2026/
- Common Thread Collective, satın alma kitlesinde 730 gün ve otomatik geçiş (28.05.2026): https://commonthreadco.com/blogs/coachs-corner/meta-purchase-audience-730-days-ecommerce
- Jon Loomer, otomatik müşteri hariç tutmaları (13.10.2026): https://www.jonloomer.com/meta-automatic-customer-exclusions/
- Common Thread Collective, artırımlılık katsayısı ve set düzeyinde ROAS hedefi: https://commonthreadco.com/pages/methodology-meta
- Cottgroup, ilke kararı 2026/347 özeti: https://cottgroup.com/tr/mevzuat/item/kvkk-ilke-karari-aydinlatma-ve-acik-riza-metinlerinin-yonetimi
- Milliyet, "bayana uygun ev" ilanına TİHEK cezası (10.02.2021): https://www.milliyet.com.tr/ekonomi/bayana-uygun-ev-ilanina-8-bin-lira-ceza-verildi-6427844
- AdManage, sıklık gözlemleri: https://admanage.ai/blog/when-to-kill-a-facebook-ad
- Köksal Hukuk, çerez bandı ve KVKK: https://koksal.av.tr/en/knowledge-centre/cookie-banner-and-notice-kvkk-gdpr-dual-compliance/
- MediaPost, detaylı hedefleme birleştirmesi (18.08.2025): https://www.mediapost.com/publications/article/408262/
- DataReportal, Digital 2026: Turkey: https://datareportal.com/reports/digital-2026-turkey

---

# Kreatif strateji ve reklam metni

> **Modül 04 · Profesyonel paket · Hazırlanma: 17 Ekim 2026.** Reklam oluşturma ekranı, "Mevcut gönderiyi kullan" ve önizleme için **Kurulum Kılavuzu** (`../KILAVUZ.md`) §5.5. Alanlar ekranı §5.4, reddedilen reklam §6.2, ilk 7 gün §6.3. Başabaş CPA **Modül 01**'de (`01-musteri-brif-ve-is-ekonomisi.md`). Haftalık sonuç (N), set sayısı ve adlandırma **Modül 02**'de (`02-strateji-ve-hesap-yapisi.md`).

## Bu modülde

- Kreatif partisini **brif → kanca → format → metin → uyum** sırasıyla üretme kuralları, ölçü tablosu ve güvenli bölgeler.
- 24 Türkçe kanca, metin çerçeveleri ve **7 sektör için** hazır birincil metin, başlık ve CTA.
- Teklif kurgusu ve Türkiye'nin indirim kuralları (1 Ağustos 2026'dan beri 10 gün + kanal kuralı). Video senaryosu ve UGC brif şablonu.
- "Kaç kreatif?" formülü, yorgunluk teşhisi (yanlış teşhis dahil) ve yenileme merdiveni.
- Yapay zekâ kontrol listesi ve politika uyumlu dil: kişisel özellik kuralı ve Türk reklam hukuku için doğru/yanlış örnekler.

**Etiketler:** **[RESMİ]** Meta'nın sayfası. **[MEVZUAT]** Türk mevzuatı. **[PRATİSYEN]** ajans ya da araç sağlayıcı (çıkar çatışması olabilir). *sezgisel* = ölçülmemiş pratisyen kuralı. Meta'nın "%X daha iyi" rakamları kendi iç analizleridir; örneklem çoğu zaman açıklanmaz, yön gösterir, garanti değildir. Şablonlardaki **[köşeli parantez]** işletmenin belgeli verisiyle doldurulur; doldurulamıyorsa o cümle çıkarılır. **md.** = aksi yazmadıkça Ticari Reklam ve Haksız Ticari Uygulamalar Yönetmeliği maddesi (1 Ağustos 2026 değişiklikleri işlenmiş hali).

---

## 0. Karar sırası (her yeni kreatif partisinde)

1. **Risk kapısı: Modül 09 §1'i uygula; kırmızı kapıdaysa kreatif üretme.** Kreatif özeti §11.3'te.
   - **İstisnasız yasak → kreatif yok:** alkol; tütün, nargile, e-sigara, vape (nikotinsiz dahil) ve beşerî ilaç (md. 27/11); yasa dışı bahis, şans oyunu, kumar ve falcı, medyum, astrolog (md. 27/3); sohbet, arkadaşlık, eş bulma (md. 27/8); silah (md. 27/9); THC ve CBD (Modül 09 §1).
   - **Dur, hukukçuya sor:** Türkiye'deki hastaya yönelik klinik ve sağlık meslek mensubu; avukat; kripto (Meta ön yetkisi + SPK kuralları); lisanslı şans oyunu.
   - **İnsan onayı olmadan yayın yok:** kredi, finans, sigorta, yatırım; takviye; kilo verme; çekiliş (§5.3) ve Modül 09 §1'deki diğer sarı işler.
2. **Brif:** persona, farkındalık seviyesi (§4.2), teklif (§5), açılış sayfası ya da WhatsApp karşılama metni.
3. **Açı ve format seç.** Önceki partiden en az 2 eksende farklı olsun (§8.1).
4. **Kanca yaz (§3):** her gövdeye 3–5 kanca.
5. **Üret:** her kavram için 3 oran (§2). Videoda Türkçe altyazı.
6. **Metin:** 5 birincil metin + 5 başlık (§4).
7. **Yapay zekâ kontrolü (§10)** ve **uyum kontrolü (§11)**, ardından §12.

---

## 1. Kreatif ilkeleri

| # | Kural | Dayanak |
|---|---|---|
| K1 | Reklam başına **tek mesaj**, tek eylem. | [RESMİ] |
| K2 | Dikkati **ilk 1–3 saniyede** çek: hareket, ürün, avantaj ya da demo. Kancayı görselle **ve** sesle ver. | [RESMİ] dönüşüm için 4 video türü; Meta+Toluna: kanca görsel+ses 1,5 kat |
| K3 | **Logo ekranıyla açma.** Markayı ilk saniyelerde ürünün üstünde göster. Satış reklamında markayı sürenin en fazla %25'inde göster. | [RESMİ] Toluna: 4,8 kat. "Kat" = ilgili ölçümde ilk %20'ye girme olasılığı; çalışma "keşif amaçlı" |
| K4 | Satış reklamında **ürünü birden çok kez göster**, bağlamı ve benzersiz özelliği anlat, sonda görsel + sesli CTA. | [RESMİ] aynı çalışma: 2,7 / 5,3 / 1,9 kat |
| K5 | **Sesli ve sessiz izlemeye birlikte** tasarla. Reels görüntülemelerinin %75'ten fazlası sesli; Akış'ın önemli kısmı sessiz. | [RESMİ] |
| K6 | **Türkçe altyazıyı kendin yap.** Otomatik ve yapay zekâ altyazısı yalnız İngilizce. Videoya göm ya da `dosyaadi.tr_TR.srt` yükle. | [RESMİ] |
| K7 | **Gerçek insan, gerçek ortam, telefon çekimi.** Stok görseli varsayılan yapma. | [RESMİ] 9:16+ses+insan+metin kullananlarda %16 düşük EBM (iç veri) |
| K8 | **Ana varlık 9:16**, yanında 4:5 ve 1:1. **Etkileşim, Potansiyel müşteriler, Uygulama tanıtımı ve Satışlar** amaçlarında reklam seti düzeyinde elle alan seçimi kalkıyor; Bilinirlik ve Trafik'te sürüyor. Değişiklik kademeli açılıyor: sette alan seçimi hâlâ görünüyorsa hesap eski düzendedir. Kalan kontroller: reklamda **alan bazında varlık özelleştirmesi** (kırpma, metin, bağlantı), **alan değer kuralları** (alana göre teklifi artır/azalt) ve hesap kontrollerinde **amaca göre hesap düzeyinde alan hariç tutma**. | [RESMİ] reklam alanı ayarlarındaki yenilikler |
| K9 | **Statik ve videoyu birlikte** kullan. | [RESMİ] %17 daha fazla dönüşüm yükselişi |
| K10 | Merak tuzağı, sansasyon, "beğen/yorum yap" tuzağı kullanma: kalite puanı düşer, Sayfa ve alan adı "düşük kalite" ile ilişkilendirilebilir. | [RESMİ] |
| K11 | Facebook Akışı alt bilgisinde **URL görünmüyor** (Mart 2026). Hedefi başlık ve görsel anlatsın. Bilinirlik ve erişim optimizasyonunda mobil Akış videosunda düğme de görünmez; CTA videonun içinde olsun. | [RESMİ] |
| K12 | Açılış sayfası ya da karşılama mesajı reklamın vaadini **aynen** sürdürsün: aynı ürün, fiyat, teklif. | [RESMİ] kalite sinyalleri |

---

## 2. Format ve ölçü tablosu

| Varlık | Piksel | Alanlar | Güvenli bölge (yazı, logo, ürün konmaz) |
|---|---|---|---|
| **9:16** | 1080×1920 | Reels, Hikayeler, WhatsApp Durumu, Instagram Akışı videosu | Reels: üst, alt, yan kenarlar; sorumluluk reddi varsa alt %40 [RESMİ]. Pratik ölçü üst ~%14, alt ~%35, yan ~%6 (*sezgisel*). Instagram Hikayeler: üst ~%14, alt %20 [RESMİ] |
| **4:5** | 1440×1800 (Facebook Akışı 4:5 minimumu) | Facebook ve Instagram Akışı görseli | Alt ve yan kenarlar [RESMİ] |
| **1:1** | 1080×1080 | Döngü, Marketplace, masaüstü Akış | Alt ve yan kenarlar |

- **Kural:** Oranları kendin yükle. Eksik oranı Meta çoğu zaman kırpma, genişletme ve yapay zekâyla doldurur; otomatik kırpmayı önizlemede kontrol et [RESMİ]. "Güvenli bölge koruması" düğmesi sarı çerçeveyi gösterir.
- **Kural: her alana uygun medya.** Meta'ya göre her reklam alanı için en az bir uygun medya gerekir. Medyanın oranı, süresi ya da türü alanı desteklemiyorsa reklam **o alanda yayınlanmaz**; Reklam Yöneticisi uyarı gösterir [RESMİ]. Uyarı varsa eksik oranı ya da kısa sürümü yükle. Bir alanda görünmek istemiyorsan bunu eksik medyayla değil, hesap düzeyinde amaca göre alan hariç tutmayla yap (K8; *sezgisel*).
- **Dosya:** görsel PNG/JPG en fazla 30 MB; video MP4/MOV en fazla 4 GB [RESMİ].
- **Süre:** Instagram Reels reklamı en fazla 15 dk; Facebook Hikayeler 1–120 sn [RESMİ]. Meta dönüşüm testi için **5–10 sn** öneriyor [RESMİ]; UGC için 15–30 sn yaygın (*sezgisel*).
- **Reels:** lisanslı müzik kullanılamaz; Meta Ses Koleksiyonu ya da telifsiz müzik [RESMİ].
- **Görseldeki metin:** sınır kalktı; yine de tek mesaj, büyük ve kontrastlı yazı [RESMİ]. Bazı Meta sayfalarındaki "1200×628 ideal" önerisi eski.

**Format seçimi (eğer-ise):**
- Bir fikir için birden çok görsel/video varsa → **esnek format** ya da **multimedya reklam** (10'a kadar medya) [RESMİ]. *Hangisi görünür hesaba göre değişir.*
- Amaç kreatifleri karşılaştırmaksa → dinamik kreatif ve esnek format **kullanma** (toplu rapor, eşit olmayan dağıtım) [RESMİ]; §8.3'e git.
- Birden çok ürün ya da adım varsa (menü, oda tipi, süreç) → **döngü**, bütün kartlar aynı oranda [RESMİ].
- Bütçe ya da video imkânı darsa → döngü, telefon çekimi, görselden kısa video [RESMİ].

---

## 3. Kanca kütüphanesi

**Kurallar:**
- Aynı gövdeye 3–5 kanca tak; kancayı ekran yazısı + sesle ver.
- Kancaların %60–70'i güvenilir türlerden (promosyonel, somut), %30–40'ı deneysel olsun (*sezgisel*). **Promosyonel, indirim demek değil:** fiyat çapası, yenilik, set, ek fayda ve kargo eşiği de bu gruptadır. Her partide yeni indirim kancası üretme; ardışık indirim önceki fiyatı geçersiz kılar (§5.3).
- Motion 2026 verisinde somut ve promosyonel kancalar (indirim, fiyat çapası, yenilik, aciliyet) en çok kazananı üretti; ama veri Black Friday dönemini kapsıyor. Yalnız indirim kancası kitleyi çabuk yorar [PRATİSYEN].

| # | Tür | Örnek | Uyarı |
|---|---|---|---|
| 1 | Fiyat çapası | "Kadıköy'de 2 kişilik serpme kahvaltı [650 TL]. Hafta içi 09–12." | Vergiler dahil TL, menüyle aynı |
| 2 | Tarihli indirim | "Kaban [1.800 TL] yerine [1.440 TL] · 20–26 Ekim" | §5.3: önce 10 günlük fiyat geçmişi; önceki fiyat ve tarih görselde de |
| 3 | Gerçek aciliyet | "Cumartesi için son [3] randevu saati kaldı." | Doğru değilse haksız uygulama |
| 4 | Yenilik | "Yeni: [15] dakikada kuruyan kalıcı oje." | Süre ölçülmüş olmalı |
| 5 | Demo | "Bu leke [40] saniyede çıkıyor mu? Deneyelim." | Kesintisiz çekim |
| 6 | Önce–sonra | "Aynı salon, aynı ışık: [3] saatte renk düzeltme." | Rıza. Klinikte yasak |
| 7 | Liste | "Taşınmadan önce yapılacak 3 şey." | 3 maddeyi gerçekten ver |
| 8 | Kişisel olmayan soru | "Kombi kışın en çok neden arıza yapar?" | Kişiye özellik sorma (§11) |
| 9 | Mit yıkma | "'Fön saçı yıpratır' mı? Kuaförümüz anlatıyor." | Bilimsel iddia ekleme |
| 10 | Karşıt görüş | "Bize gelmeden önce 3 ofise daha bakın. Sonra fiyat sorun." | Rakip adı yok |
| 11 | İtiraf | "İlk ay salonumuza kimse gelmedi. Sonra şunu değiştirdik…" | Yalnız gerçekse |
| 12 | Uyarı | "Kira sözleşmesini imzalamadan önce şu 3 maddeye bakın." | Korkutma yok |
| 13 | Meslek/semt çağrısı | "Ataşehir'de çalışanlar için 45 dakikalık öğle antrenmanı." | Meslek, semt, ilgi serbest; yaş, sağlık değil |
| 14 | Sektör çağrısı | "Lojistik firmaları için: teklif süresini kısaltan şablon." | — |
| 15 | Sosyal kanıt | "Siparişi doğrulanmış [1.200] müşterinin puanı: [4,8]." | Yalnız doğrulanmış alıcı |
| 16 | Müşteri sözü | "'Kargo ertesi gün geldi, beden tam oldu.' (doğrulanmış alıcı)" | Aynı kural |
| 17 | Takvim | "LGS'ye [8] ay kala haftada 3 saat yeter mi?" | Derece vaadi yok |
| 18 | POV | "POV: Cuma akşamı ev temiz, siz sadece kapıyı açıyorsunuz." | — |
| 19 | Kutu açılışı | "Siparişi birlikte açalım: ne geliyor, nasıl paketleniyor?" | Gerçek sipariş |
| 20 | Kamera arkası | "Ekmeğimiz sabah 5'te böyle başlıyor." | — |
| 21 | Biz ve diğerleri | "Platform yerine bizden sipariş verin: tatlı ikramı bizden." | Rakip adı/logosu yok (md. 8) |
| 22 | Risk tersine çevirme | "Beden olmazsa [30] gün içinde değişim." | 14 günlük yasal cayma hakkı avantaj diye sunulamaz |
| 23 | Mevsim | "Kış gelmeden kombi bakımı: Kasım randevuları açıldı." | — |
| 24 | Duyuru | "Yeni şubemiz cumartesi açılıyor: ilk [100] kişiye kahve ikramı." | Bedavanın şartı ve adedi yazılı. Çekilişe dönüşürse §5.3 (insan onayı) |

---

## 4. Reklam metni

### 4.1 Uzunluk ve yapı [RESMİ]

- Önerilen uzunluk: **birincil metin 125, başlık 40, açıklama 25** karakter; uzunu kesilebilir. Birincil metin 1–3 satır.
- **İlk satır** teklifi ya da faydayı söylesin.
- Reklama **5 birincil metin + 5 başlık** gir. Her kombinasyonda birlikte görünebilirler; **çelişmesinler** (biri "%20", diğeri "%30" demesin).
- Meta'nın "Metin oluşturma" özelliği yalnız İngilizce, Portekizce, İspanyolca girişte çalışıyor; çeviri hedeflerinde Türkçe yok. **Varyasyonları elle yaz.**
- Yanlış dilbilgisi ve noktalama ret sebebidir; ğ, ş, ı, İ harflerini kontrol et.

### 4.2 Farkındalık seviyesi → çerçeve

[PRATİSYEN] Schwartz'ın 5 seviyesi, Motion uyarlaması. Eşleştirme *sezgisel*.

| Kitle | Çerçeve | Kanca (§3) | CTA |
|---|---|---|---|
| Farkında değil (soğuk) | Kanca–gövde–CTA, POV, liste | 5, 7, 8, 18, 20 | Daha fazla bilgi / Detayları gör |
| Problemden haberdar | PAS (yumuşak) | 8, 12, 13 | Daha fazla bilgi / Teklif al |
| Çözümden haberdar | Fark + kanıt, BAB | 6, 9, 10, 21 | Teklif al / Randevu talep et |
| Üründen haberdar (ılık) | Sosyal kanıt + itiraz yanıtı | 15, 16, 19, 22 | WhatsApp mesajı gönder / Şimdi alışveriş yap |
| Tam haberdar (sıcak) | Teklif önce | 1, 2, 3, 24 | Şimdi alışveriş yap / Şimdi rezervasyon yap |

### 4.3 Çerçeve iskeletleri

- **Teklif önce:** [teklif + fiyat/tarih]. [kime, nerede]. [tek kanıt]. [CTA].
- **PAS:** [tanıdık durum, kişiye özellik yüklemeden]. [sonucu, abartmadan]. [çözüm olarak hizmet]. [CTA].
- **AIDA:** [kanca]. [fayda]. [kanıt ya da teklif]. [CTA].
- **BAB:** Önce: [durum]. Sonra: [sonuç]. Köprü: [hizmet, süre]. [CTA]. Sağlık hizmetinde ve kiloda kullanma.
- **Sosyal kanıt:** "[doğrulanmış yorum]". [sayı + kaynak]. [teklif]. [CTA].

[PRATİSYEN] Loomer: bilinen acı noktası → çözüm olarak ürün → net teklif; fiyatı gizleme.

### 4.4 CTA düğmesi

Düğme listesi **amaç ve dönüşüm konumuna göre** değişir [RESMİ]. Arayüz adları: *Daha fazla bilgi, Detayları gör, Şimdi alışveriş yap, Şimdi sipariş ver, Şimdi rezervasyon yap, Randevu talep et, Teklif al, Kaydol, Şimdi ara, WhatsApp mesajı gönder, Menüyü gör.*
- Soğuk kitle + bilgi gerektiren ürün → yumuşak düğme; sıcak kitle + net teklif → doğrudan düğme (*sezgisel*; kanıt zayıf, test et).
- Düğme testi yaparken "Eylem çağrısını iyileştir"i kapat; açıksa Meta düğme metnini değiştirir [RESMİ].

### 4.5 Sektör şablonları (*fiyatlar varsayımsal*)

> **Diş ve estetik klinik için şablon yok.** Türkiye'deki hastaya ücretli sponsorlu tanıtım genel kural olarak yasak; istisnalar açılıştan sonraki ilk ay ve yetki belgeli sağlık turizmi. Dur, hukukçuya yönlendir (§11.3).

**1. Restoran / kafe** · Teklif önce · WhatsApp
- **Birincil:** "Moda'da hafta içi öğle menüsü [320 TL]: çorba, ana yemek, ayran. 12.00–15.00. WhatsApp'tan yazın, paketiniz [20] dakikada hazır."
- **Başlık:** "Öğle menüsü [320 TL]" / "[20] dakikada paket hazır" · **Açıklama:** "Moda · 12.00–15.00" · **CTA:** WhatsApp mesajı gönder
- **Uyum:** Alkol görseli ve ifadesi yok; fiyat menüyle aynı.

**2. Kuaför / güzellik** · BAB · WhatsApp
- **Birincil A:** "Renk düzeltmede sürpriz fiyat yok: önce saçınıza bakıp yazılı fiyat veriyoruz. Salı ve çarşamba sabahı boş saat var."
- **Birincil B:** "Önce: üç farklı boyadan kalan renk. Sonra: tek tonda doğal kumral, [3] saatte. Fotoğraflar müşterimizin izniyle."
- **Başlık:** "Yazılı fiyat, sonra işlem" · **CTA:** WhatsApp mesajı gönder / Randevu talep et
- **Uyum:** Botoks, dolgu gibi tıbbi işlem yok; müşteri görseline yazılı rıza.

**3. E-ticaret (moda)** · Teklif önce + sosyal kanıt · Site
- **Görsel/video yazısı:** "[1.800 TL] yerine [1.440 TL] · 20–26 Ekim · [500] adet". Okunur büyüklükte, güvenli bölgede.
- **Birincil A:** "Su itici kaban [1.440 TL] (indirim öncesi [1.800 TL]). 20–26 Ekim, [500] adetle sınırlı. 36–48 beden, [1–2] iş günü kargo. Kargo [X TL], [Y TL] üzeri ücretsiz."
- **Birincil B:** "'Bedeni tam oldu, kumaşı kalın.' Siparişi doğrulanmış [312] alıcının ortalama puanı [4,7]."
- **Başlık:** "[1.800 TL] yerine [1.440 TL] · 20–26 Ekim" · **Açıklama:** "[1–2] iş günü kargo" · **CTA:** Şimdi alışveriş yap
- **Uyum:** Önceki fiyat = aynı sitede son 10 günün en düşüğü; son 10 günde başka kampanya varsa R o kampanyanın fiyatıdır (§5.3). Önceki fiyat, indirimli fiyat ve tarih görselde ve başlıkta birlikte. Kargo ücreti ya da ücretsiz kargo şartı yazılı (md. 13/5).

**4. Yerel ev hizmeti** · Somut rakam · WhatsApp / arama
- **Birincil:** "2+1 ev temizliği sabit [2.400 TL]: 2 kişilik ekip, yaklaşık [4] saat, malzeme bizden. Ataşehir, Kadıköy, Üsküdar. Fotoğraf gönderin, aynı gün fiyat verelim."
- **Başlık:** "2+1 ev temizliği [2.400 TL] sabit" / "Aynı gün fiyat ve saat" · **CTA:** WhatsApp mesajı gönder; acil tesisatta *Şimdi ara*, yalnız telefonun açık olduğu saatlerde
- **Uyum:** "Garantili 4 saat" yok; "sigortalı" belgeye dayanmalı.

**5. Eğitim / kurs** · AIDA · Hızlı form
- **Birincil:** "LGS matematikte haftada 3 saatlik program: önce ücretsiz seviye tespit sınavı, sonra 8 kişilik grup. Kadıköy, hafta içi akşam ve hafta sonu."
- **Başlık:** "Ücretsiz seviye tespit sınavı" · **CTA:** Kaydol / Daha fazla bilgi
- **Uyum:** Derece garantisi, öğrenci fotoğrafı ya da sonucu yok (5580 s. Kanun m. 7). Hedef veli (18+). "Çocuğunuz zorlanıyor mu?" gibi aile durumu ima eden soru yok.

**6. Emlak** · Değer teklifi · Hızlı form
- **Birincil A (portföy):** "Evinizi satmadan önce bölgedeki son satışlara göre ücretsiz değerleme yapalım. Yetki belgeli ofis, Maltepe ve Kartal. [24] saatte arıyoruz."
- **Birincil B (ilan):** "Kartal 3+1, brüt [125] m² / net [110] m², deniz manzaralı: [8.950.000 TL]. EİDS doğrulamalı ilan: [ilan bağlantısı] · İlan no: [no]."
- **Başlık:** "Maltepe'de ücretsiz değerleme" · **CTA:** Teklif al / Daha fazla bilgi
- **Uyum:** İlan reklamında EİDS doğrulamalı ilan bağlantısı **paylaşımın kendisinde**, yani birincil metinde olmalı; fiyat da zorunlu [MEVZUAT, Bakanlık uygulaması]. Facebook Akışı alt bilgisinde URL görünmediği için (K11) hedef URL'ye güvenme. Fiyat yazılıysa brüt ve net alan da zorunlu (md. 27/5). Fotoğraf ve videolar yalnız o ilana ait olsun. "Garantili değer artışı" yok. Portföy, proje ya da değerleme reklamının "ilan" sayılıp sayılmadığı, yani EİDS bağlantısı isteyip istemediği *doğrulanmalı*.

**7. Spor salonu** · Meslek + semt · WhatsApp
- **Birincil:** "Ataşehir'de çalışanlar için 45 dakikalık öğle antrenmanı: 12.15 ve 13.00 grupları. İlk hafta deneme ücretsiz, eğitmen eşliğinde."
- **Başlık:** "İlk hafta deneme ücretsiz" · **CTA:** WhatsApp mesajı gönder / Kaydol
- **Uyum:** "Göbek", "kilolu" yok; beden utandıran görsel yok; kilo vaadi yok.

---

## 5. Teklif kurgusu

### 5.1 Güçlendirme kontrolü

[PRATİSYEN] Hormozi'nin değer denklemi: algılanan değeri **sonuç** ve **inanç** artırır; **bekleme süresi** ve **çaba** azaltır.
- [ ] Sonuç somut mu? ("temiz ev" değil "2+1, 4 saat, cam dahil")
- [ ] İnanç kanıtlı mı? (doğrulanmış yorum, belge, demo)
- [ ] Süre kısa mı? (aynı gün fiyat, 1–2 iş günü kargo)
- [ ] Çaba az mı? (fotoğraf gönder yeter, kurulum dahil)
- [ ] "Neden şimdi?" sorusunun **gerçek** bir cevabı var mı? (tarih, kontenjan, sezon)

### 5.2 Teklif türü (*sezgisel*)

| Durum | Teklif |
|---|---|
| Düşük fiyatlı ürün, sıcak kitle | Tarihli indirim, ücretsiz kargo eşiği, set |
| Yüz yüze hizmet, ilk ziyaret | Deneme, ilk ziyaret paketi, sabit fiyatlı temel paket |
| Pahalı ürün, uzun karar | Ücretsiz değerleme/analiz/keşif. Satış hacmi yetmiyorsa potansiyel müşteri ya da düşük fiyatlı giriş ürünü [PRATİSYEN Loomer] |
| Platform komisyonundan kaçış | Doğrudan kanalda **ek fayda** (ikram, geç çıkış), fiyat kırma değil |
| Boş saatleri doldurma | Saate bağlı fiyat (hafta içi sabah) |

### 5.3 Türkiye uyum kuralları [MEVZUAT]

- **İndirim reklamı** (md. 14, 1.8.2026'dan beri): indirim öncesi fiyat, başlangıç ve bitiş tarihi ve sınırlıysa adet açıkça yazılır.
- **Görünürlük (*sezgisel*, md. 14/1'deki "açık ve anlaşılır" şartının yorumu):** İndirimli fiyat nerede görünüyorsa (görsel, video, başlık) önceki fiyat ve tarih aralığı da **aynı yerde** görünsün. Hikayeler ve Reels'te birincil metin kısalır ya da hiç görünmeyebilir; başlık da tek başına gösterilebilir. Bu yüzden önceki fiyatı, indirimli fiyatı, tarihi ve sınırlıysa adedi görselin ya da videonun içine, okunur büyüklükte ve güvenli bölgeye yaz. Başlıkta yalnız indirimli fiyatı kullanma. Her alanı önizlemede kontrol et.
- **Önceki fiyat (R)** = indirimin başlangıç gününden önceki **10 gün** içinde, aynı kanalda uygulanan **en düşük** fiyat. Çabuk bozulan mal ve hizmette bir önceki fiyattır. 2022–2025 kaynaklarındaki "30 gün" artık geçersiz.
- **Kanal kuralı:** Önceki fiyat yalnız indirimin yapıldığı kanaldaki fiyattır. "2 al 1 öde", "500 TL üzeri %20" da kapsamda.
- **Ardışık indirim tuzağı:** R, önceki kampanyanın indirimli fiyatı olabilir. Bir indirim bittikten sonra 10 gün geçmeden yenisi başlarsa eski "önceki fiyat" ibaresi yanlış olur. Tek ve kesintisiz bir indirimde R başlangıç gününe göre sabittir; 10 günü aşması R'yi tek başına değiştirmez. Kampanyayı yeni tarihle uzatmanın yeni başlangıç sayılıp sayılmadığı *doğrulanmalı*. Temkinli yol: uzatmayı yeni indirim say, R'yi yeniden hesapla.
  - **Örnek (*varsayım*):** Kaban 20–26 Ekim'de [1.440 TL] (önceki [1.800 TL]), 27 Ekim'den sonra yine [1.800 TL]. 3 Kasım'da [1.500 TL]'lik indirim başlatılırsa pencere 24 Ekim–2 Kasım olur, en düşük fiyat 1.440 TL'dir; R = 1.440 TL. 1.500 TL bunun üstünde kaldığı için indirim reklamı yapılamaz. "1.800 TL yerine" yazmak gerçekte olmayan bir indirim izlenimi verir (md. 14/2). Bu ürün için en erken yeni indirim günü 6 Kasım'dır (pencere 27 Ekim–5 Kasım, tümü 1.800 TL).
  - **Kural:** İndirim kancası yazmadan önce o kanalın son 10 günlük fiyat geçmişini iste. Geçmiş yoksa indirim kancası üretme. Sık indirim yerine ek fayda, set ya da kargo eşiği öner (§5.2). Formül: Modül 09 §5.3.
- **Fiyat:** vergiler dahil TL. Taksitte toplam fiyat ve taksit sayısı okunur büyüklükte. "Bedava" şarta bağlıysa şart ana mesajda (md. 13). **Teslim (kargo) ücreti** tüketiciye yansıyorsa tutarı, belli değilse nasıl hesaplandığı reklamda yazılır (md. 13/5). Ücretsizse şartı da yazılır ("[Y TL] üzeri ücretsiz").
- **Sahte aciliyet** ve sahte "kapanıyoruz" her durumda haksız (Ek A-7, A-11). Doğrudan satış reklamında "son, sadece, sınırlı satış" ile teşvik yasak (md. 27/1); Meta reklamının bu kapsama girip girmediği belirsiz, kelime yerine gerçek tarih ve adet yaz.
- **Yasal haklar** (14 günlük cayma) ekstra avantaj gibi sunulamaz (md. 7/7).
- **Çekiliş: dur, insana sor.** Sosyal medya çekilişi için Milli Piyango İdaresi izni gerekebilir (Karşılığı Nakit Olmayan Piyangolar ve Çekilişler Hakkında Yönetmelik; *doğrulanmalı*, Modül 09 da doğrulamadı). Katılım şartı "beğen, yorum yap, etiketle, paylaş" olamaz; bu, K10'daki etkileşim tuzağıdır. Ödül, adet, başlangıç ve bitiş tarihi ve teslim koşulu yazılı olsun (md. 15).
- **Hediye/promosyon** (çekilişsiz, "ilk 100 kişiye"): hediyenin miktarı ve geçerlilik süresi yazılı (md. 15).

### 5.4 Formüller (*türetilmiş*)

- **İndirim oranı** = (önceki fiyat − indirimli fiyat) ÷ önceki fiyat. Fiyat geçmişini sakla; ispat yükü satıcıda.
- **İndirimli katkı** = Modül 01'deki sipariş başı katkı − KDV hariç indirim tutarı = **yeni gerçek başabaş CPA**.
- **Örnek:** Katkı 414 TL, KDV hariç indirim 150 TL → 264 TL. Panel başabaşı 264 ÷ 1,05 ≈ **251 TL** (Modül 02 F3).
- **Eğer** yeni panel başabaşı beklenen CPA'nın altındaysa → indirim yerine ek fayda (hediye, kargo eşiği) kurgula.

---

## 6. Video senaryo şablonu

Blok süreleri Meta'nın "ilk 3 sn" ve "5–10 sn" önerilerine dayanır [RESMİ]; kelime sayısı ve 15 sn üstü bloklar *sezgisel*.

| Blok | Süre | Görüntü | Ses ve yazı | Kural |
|---|---|---|---|---|
| **Kanca** | 0–3 sn (asıl iş ilk 1–2 sn) | İlk karede ürün, insan ya da sonuç; hareket | İlk saniyede konuşma; 3–7 kelimelik ekran yazısı | Logo ekranı yok; marka ürünün üstünde |
| **Gövde** | 3–10 sn | Ürün kullanımda (2. gösterim), bağlam, benzersiz özellik, demo | Tek fayda | Tek mesaj |
| **Teklif** (15 sn üstü) | 10–20 sn | Fiyat/teklif kartı, doğrulanmış yorum | Neden şimdi; fiyat, tarih, şart | §5.3 ibareleri |
| **Kapanış** | Son 2–3 sn | Ürün 3. kez, CTA kartı, küçük logo | **Sesli CTA** + yazı | Alt güvenli bölge boş |

**Süre seçimi:** 6–10 sn hareketli statik ya da tek fayda (Meta önerisi 5–10 sn) [RESMİ]; 15–30 sn UGC ve demo; 30–60 sn kurs ve B2B anlatımı (ikisi de *sezgisel*). Uzun videonun yanına kısa sürümü de koy.

**Doldurulmuş örnek: ev temizliği, 15 sn, 9:16**
- **0–2 sn:** Kirli fırın camı yakın çekim. Ses: "Bu cam [40] saniyede temizlenir mi?" Yazı: "[40] saniye, kesintisiz çekim."
- **2–9 sn:** Logolu önlüklü ekip üyesi siler, ekranda kronometre. Ses: "Malzeme bizden, ekip sigortalı."
- **9–13 sn:** Yazı: "2+1 ev · yaklaşık [4] saat · [2.400 TL] sabit". Ses: "Fotoğraf gönderin, aynı gün fiyat verelim."
- **13–15 sn:** Telefonda WhatsApp ekranı. Ses: "WhatsApp'tan yazın." Logo sağ üstte, alt %35 boş.
- **Varyasyon:** Aynı gövdeye 3 kanca (#5 demo, #18 POV, #1 fiyat çapası). N < 50 ise bunlar tek reklamın 3 videosu olur; N ≥ 150 ise 3 ayrı reklam açılabilir (§8.2).

**Üretim kontrolü:** altyazı gömülü ya da `.tr_TR.srt` · yazılar güvenli bölgede · ses Meta Ses Koleksiyonu'ndan ya da telifsiz · 4:5 kırpımı kontrol edildi.

---

## 7. UGC brif şablonu

```
UGC BRİFİ
1. Marka / ürün:        [ad] · [ürün linki]
2. Persona:             [meslek / alışkanlık / ilgi — hassas özellik değil]
3. Tek mesaj:           [tek fayda cümlesi]
4. Kanca seçenekleri:   [§3'ten 3 tür]
5. Zorunlu sahneler:    ürün ilk 2 sn'de · ürün kullanımda · ürün 3 kez görünür · sonda sesli CTA
6. İzinli iddialar:     [belgeli iddia listesi]  ← listede olmayan iddia YOK
7. Yasaklar:            fiyat/indirim uydurma · sağlık/tedavi beyanı · "en iyi/en ucuz" · rakip adı ·
                        kişisel özellik sorusu · kullanmadığı ürünü kullanmış gibi anlatma
8. Yasal ibare:         başta, kaydırmadan görünür: "Reklam" + "@[marka] tarafından sağlandı";
                        filtre/efekt kullandıysa açıkça yazar
9. Teknik:              9:16 · 1080×1920 · gün ışığı · yaka mikrofonu · ham + altyazılı sürüm · müzik yok
10. Teslim:             1 gövde × 3 kanca + 2 kapanış (yumuşak / doğrudan CTA) · 15–30 sn
11. Haklar:             kullanım süresi [ay] · ortaklık reklamı izni · yüz ve ses rızası (KVKK) ·
                        yapay zekâyla kopyalama yok
12. Ön koşul:           üretici ürünü çekimden önce en az [7] gün kullanmış olmalı
```

**Örnek satırlar (kaban, varsayımsal):** Persona "toplu taşımayla işe gidenler". Tek mesaj "yağmurda ıslanmayan, ağır olmayan kaban". Kancalar: "POV: durakta yağmur başladı", "üstüne bardakla su döküyorum", "beden olmazsa 30 gün içinde değişim". İzinli iddialar: su itici kumaş (üretici belgesi), 36–48 beden, 1–2 iş günü kargo.

**Dayanak [MEVZUAT]:** Ücret, ücretsiz ürün ya da indirim alan üreticinin içeriği reklamdır; "Reklam" ya da "Tanıtım" + reklamveren adı zorunlu, etiket diğerlerinden önce gelir; "#işbirliği" tek başına yetmez (md. 23/A). Gerçek kişinin yapay zekâ kopyasıyla ürün önerdirmek yasak (md. 27/12). Meta'nın ortaklık reklamı etiketinin 23/A'yı karşıladığı doğrulanmadı; ibareyi kreatife ve metnin başına da koy.

---

## 8. Kreatif çeşitlendirme ve kaç kreatif

### 8.1 "Farklı" ne demek?

Meta çok benzer kreatifleri farklı tanımayabilir ve aynı kitleye gösterebilir. Bu erişimi kısar, "Kreatif sınırlandı" durumuna yol açabilir [RESMİ]. Bu durum yalnız tek kreatifli setlerde görünür (§9). Sayısal benzerlik eşiği yok. Yeni kavram öncekinden **en az 2 eksende** ayrılsın (*sezgisel*):

| Eksen | Örnek |
|---|---|
| Persona | işe giden, veli, mal sahibi, kurumsal alıcı |
| Açı | acı noktası, arzu, itiraz yanıtı, sosyal kanıt, teklif, mit yıkma |
| Format | statik, UGC, demo, döngü, ekran kaydı, liste |
| Görsel dil | stüdyo, telefon çekimi, yazı ağırlıklı, yakın çekim |
| Kanca | §3 türleri |

**Farklı sayılmaz:** yalnız arka plan rengi, tek kelime, yalnız metin, aynı videoya farklı müzik. Bunlar **iterasyondur**: yorgunlukta işe yarar, yeni kavramın yerini tutmaz [PRATİSYEN Loomer].

### 8.2 Kaç kreatif?

**Sabit sınırlar [RESMİ]:** set başına en fazla 50 reklam; reklam başına 10'a kadar medya; çok fazla reklam her reklamın gösterimini azaltır ve öğrenmeyi uzatır. Meta, set başına reklam sayısını azaltmayı ve farklı varlıkları tek reklamda toplamayı öneriyor.

**Sayfa başına reklam sınırı [RESMİ]:** Yayındaki ve incelemedeki reklamlar Sayfa başına sayılır. En yüksek harcamalı ayı 100.000 USD'nin altında kalan Sayfanın sınırı 250 reklamdır. O Sayfa adına reklam veren bütün hesaplar aynı sınırı paylaşır. Sınır aşılırsa yeni reklam yayınlanamaz ve mevcut reklamlarda kapatmak dışında düzenleme yapılamaz. Dinamik kreatif, Advantage+ katalog reklamı ve Advantage+ reklam alanlarındaki bir reklam, kaç kreatif içerirse içersin tek reklam sayılır. Eylül 2026'da bazı hesaplarda sınırın kalktığı bildirildi; Meta doğrulamadı [HABER, *hesaba göre değişebilir*]. Yeni partiden önce Meta Business Suite'teki "Sayfa Başına Reklam Sınırları" aracına bak.

**Formül (*türetilmiş*):** **K ≈ (haftalık bütçe × test payı) ÷ (2–3 × hedef CPA)**. K, haftada test edilebilecek yeni kavram sayısıdır.
- 2–3 × CPA kavram başına karar harcamasıdır [PRATİSYEN Foxwell, *sezgisel*].
- Test payı ≤ %20; bu, kreatif testi arayüzündeki Meta önerisidir (Loomer aktarıyor, *doğrulanmalı*).
- **Örnek:** Günlük 2.000 TL → haftalık 14.000 TL. Test payı %20 → 2.800 TL. CPA 300 TL → 2.800 ÷ 900 ≈ **3 kavram/hafta**.
- **Eğer K < 1** → ayrı test yapma. Yeni kavramı mevcut sete ekle, iki hafta toplam sonuca bak [PRATİSYEN Loomer].

| Durum (N = haftalık beklenen sonuç, Modül 02 F4) | Aktif kavram | Set başına reklam | Yeni parti | Dayanak (*sezgisel* sentez) |
|---|---|---|---|---|
| N < 50 | 2–3 (her biri 1 statik + 1 video, 3 oran) | **2–4.** Kanca, oran ve statik/video varyasyonları tek multimedya ya da esnek reklamın medyasıdır | 2 haftada bir, yalnız toplam sonuç kötüyse ya da yorgunluk görünürse | Loomer "1–2 fikirle başla"; Foxwell aylık 10 bin $ altı "ayda 1"; Meta reklam hacmi önerisi |
| 50 ≤ N < 150 | 3–5 | 3–6; kavram başına bir reklam | 1–2 haftada bir, 2–3 kavram | CTC ayda 8–12 ↔ Foxwell ayda 3–4 (çelişkili); Modül 02 "3–5 reklamla başla" |
| N ≥ 150 | 5–10 | En fazla ~10; kanca varyasyonları ayrı reklam olabilir | Her hafta 3–5+, bir kısmı kazananın türevi | Motion: kurumsalda haftada ~18,8 kreatif |

Uzlaşma: neredeyse aynı varyasyonları değil, **gerçekten farklı kavramları** çoğalt.

### 8.3 Parti yapısı ve test

- **Bir parti** = 2–3 kavram × (1 statik + 1 video) × 3 oran + 5 birincil metin + 5 başlık; video başına 3 kanca.
- **Partinin kaç reklam olacağını N belirler (§8.2, *sezgisel*):** Her varyasyonu ayrı reklam yaparsan bir parti 8–12 reklama çıkar; bu, küçük hesapta Meta'nın reklam hacmi önerisine ters düşer. N < 50 ise her kavram **tek reklamdır**; statik, video, oranlar ve kancalar o reklamın medyası olur (en fazla 10). Bu durumda kancalar temiz karşılaştırılamaz; "Reklam kreatifi" kırılımı yalnız yön gösterir. N ≥ 150 ise kanca varyasyonları ayrı reklam olarak açılabilir. Her iki durumda da reklamları ekledikten sonra Sayfa başına sınırın altında kaldığını kontrol et (§8.2).
- **Karşılaştırma:** kreatif testi aracı (2–5 reklam, eşit bütçe, çakışmasız, varsayılan 7 gün; yalnız "En yüksek hacim" teklifiyle çalıştığı bildiriliyor — resmi sayfa yok) ya da A/B testi (en az 7 gün, eşit bütçe, tek değişken) [RESMİ].
- **Kararı toplam sonuca göre ver.** Kırılımda en çok harcayanı kapatma.

---

## 9. Yorgunluk: belirtiler, teşhis, yenileme

**Uygunluk [RESMİ]:** "Kreatif sınırlandı" ve "Kreatif yorgunluğu" durumları ile Meta'nın yorgunluk tavsiyeleri yalnız **tek kreatifli reklam setlerinde** çıkar. Advantage+ katalog, dinamik kreatif ve Advantage+ uygulama kampanyalarında çıkmaz. Bu modül sete birden çok reklam koyduğu için durum sinyali çoğu zaman **hiç gelmez**. Sinyal yoksa yorgunluk da yok sanma; çok reklamlı sette aşağıdaki "Çok reklamlı set" satırındaki yedek teşhisi kullan.

| Gözlem | Önce kontrol et | Karar |
|---|---|---|
| **"Kreatif yorgunluğu"** (yalnız tek kreatifli set): sonuç başı ücret geçmişin ≥2 katı [RESMİ] | Aynı görsel Sayfanın başka kampanyalarında da var mı? (Meta onları da sayar) | Eskisini **kapatmadan** belirgin farklı kavram ekle, kitleyi genişlet [RESMİ] |
| **"Kreatif sınırlandı"** (yalnız tek kreatifli set): geçmişten yüksek, 2 kattan az [RESMİ] | Setteki kreatifler benzer mi? | Farklı eksende kavram ekle (§8.1) |
| **Çok reklamlı set, durum sinyali yok** (yedek teşhis, *sezgisel*): reklamın sonuç başı ücreti kendi 4 haftalık tabanının üstüne çıkıp yükseliyor. Meta'nın tek kreatifli set eşikleri uyarlanır: tabanın üstü "sınırlı", 2 katı ve üstü "yorgun" | Aynı reklamda 7 günlük sıklık artıyor mu, kanca oranı haftadan haftaya düşüyor mu? "Reklam kreatifi" kırılımında aynı varlıkta aynı seyir var mı? (Kırılım kademeli açılıyor, dinamik kreatif sonucunu içermez [RESMİ].) Sezon ve CPM değişti mi? Reklamın haftalık sonucu birkaç taneyse CPA yerine sıklık, kanca oranı ve TO eğilimine bak | Sıklık artışı ile kanca oranı düşüşü birlikte görülüyorsa yorgunluk say. Eskisini kapatmadan yeni kanca ya da kavram ekle (yenileme merdiveni) |
| İlk 1–2 hafta parlak, sonra düşüş | **Kitle segmenti kırılımı:** ilk haftalarda mevcut müşteri/etkileşimli payı yüksek miydi? | Büyük olasılıkla sıcak kitle tükendi, yorgunluk değil [PRATİSYEN Loomer]. Yeni reklam açma döngüsüne girme |
| 7 günlük sıklık artıyor + TO iki hafta düşüyor + CPA hedefin üstünde | Sezon/CPM, sayfa, stok, fiyat | Önce yeni kanca, olmazsa yeni açı (*sezgisel*) |
| Kanca oranı haftadan haftaya düşüyor | CPM değişti mi? | İlk 3 saniyeyi değiştir |
| "Tekrar nedeniyle gizleme", olumsuz yorum artışı | Sıklık | Yeni kavram; gizleme kalite sinyalidir [RESMİ] |
| Küçük kitle (yerel, yeniden hedefleme), yüksek sıklık | Beklenen durum | Karar CPA/TO eğilimiyle; gerçek yorgunluk en çok burada olur [PRATİSYEN Loomer] |

**Sıklık için sihirli sayı yok.** Soğuk kitlede 7 günlük 2,5–3,5 bandı pratisyen eşiğidir (*sezgisel*); hesabın kendi taban çizgisini kullan.

**Teşhis zinciri** [PRATİSYEN Motion, eşikler *sezgisel*]:
- **Kanca oranı** = 3 sn oynatma ÷ gösterim. <%25 kanca sorunu, >%35 güçlü.
- **Tutma oranı** = ThruPlay ÷ 3 sn oynatma (~%40–50 ortalama). Kanca iyi + tutma zayıf → gövde vaadi karşılamıyor.
- Kanca + tutma iyi, dönüşüm zayıf → teklif ya da CTA.
- TO yüksek, dönüşüm düşük → yanlış kitle ya da sayfa. Açılış sayfası görüntüleme ÷ bağlantı tıklaması düşükse sayfa hızı.

**Yenileme merdiveni** (ucuzdan pahalıya): (1) yeni kanca, aynı gövde → (2) yeni format, aynı açı → (3) yeni açı → (4) yeni persona ya da teklif. Her adımda eskisini açık bırak, yeniyi **aynı sete** ekle, bir hafta dokunma.

---

## 10. Yapay zekâ ile kreatif üretim kontrol listesi

### 10.1 Advantage+ kreatif iyileştirmeleri

Meta "bazı iyileştirmeler varsayılan açık olabilir" diyor; bazı markalar onaysız açıldığını söylüyor [RESMİ; HABER]. Her yayında tek tek kontrol et. Risk sınıfları *sezgisel*:

| Risk | İyileştirme | Karar |
|---|---|---|
| Düşük | Parlaklık/kontrast, döngü kartı sıralama, profil son kartı | Açık kalabilir, önizle |
| Orta | Müzik, animasyon | Önizleyip karar ver |
| Yüksek (TR'ye özel) | Detayları zaman içinde göster, İlgi odaklarını göster, Özetleri göster | Varsayılan açık olabilir. Siteden metin ve görsel çeker ya da yapay zekâyla özet yazar [RESMİ]. Gösterimden gösterime değiştiği için önizleme hepsini göstermez. **İndirim kampanyasında ve düzenlemeye tabi işte (kozmetik, gıda/takviye, eğitim, emlak, sağlık, finans) Kapalı.** Diğer işlerde açmadan önce siteyi tara: md. 14 bilgisi olmayan üstü çizili fiyat, eski fiyat, "en/tek/garanti" ya da sağlık beyanı varsa kapat |
| Orta (TR'ye özel) | Alakalı yorumlar | **Kapalı**: doğrulanmamış yorum yasağıyla (md. 28/B-6) ilişkisi belirsiz |
| Yüksek | Görsel ve arka plan oluştur, görsel/video rötuşları (yeni piksel), medya metnini iyileştir, metin iyileştirmeleri, eylem çağrısını iyileştir, çeviriler | **Kapalı**; çıktıyı görüp onaylamadan açma. "Eylem çağrısını iyileştir" Hikayelere "%X indirim" ekleyebilir → 10 gün kuralı riski |
| Türkçede çalışmaz | Metin oluşturma, yapay zekâ altyazısı | Elle yaz, SRT yükle |

- **Hesap düzeyi:** Reklam ayarları > Reklam oluşturma > Kreatif özellikleri > **"Yeni kreatif özelliklerini test edin"**. İşaretliyse test aşamasındaki özellikler reklamda seçilmese de uygulanabilir [RESMİ].
- **Belgelenmiş olaylar [HABER]:** bozuk ürün ve anatomi görselleri yayına çıktı; REI'nin bozuk bisiklet görseli yaklaşık bir hafta yayında kaldı. İnceleme sorumluluğu reklamverende.
- Finans, sağlık, konut, istihdam ve siyaset reklamlarında üretken özellikler kısıtlı olabilir [RESMİ].
- **Çeviriler:** "Seslendirme çevirileri" İngilizce seslendirmeli videoyu yapay zekâ dublajıyla başka dillere çevirir; hedef diller arasında **Türkçe var**. Metin ve bindirme çevirilerinde Türkçe hedef yok [RESMİ]. Bu durumda videodaki gerçek kişi, söylemediği Türkçe cümleleri yapay zekâ sesiyle söylüyor görünür. Dublaj sesinin konuşmacının sesine benzetilip benzetilmediği *doğrulanmalı*. Her iki durumda da md. 18/8 ve 27/12 riski var (§10.2). Dil kısıtlı işlerde çevirileri **kapalı** tut. Örnek: yetki belgeli sağlık turizminde reklam Türkçe dışı dilde ve yurt dışı kitleye olmalı; Türkçe dublaj bu istisnayı bozar (§11.3, *sezgisel*).

### 10.2 Her üretilmiş varlık için

- [ ] Ürün doğru: renk, logo, etiket, ambalaj, parça sayısı.
- [ ] Anatomi ve fizik doğal (el, parmak, gölge).
- [ ] Görseldeki Türkçe yazıda harf hatası yok.
- [ ] Sonradan eklenmiş fiyat, indirim ya da iddia yok.
- [ ] **md. 18/8 iki durumu kapsar** ve ikisinde de açıklama "açık, anlaşılır ve ayırt edilebilir" olmalı:
  - (a) Tüketicinin ekonomik davranışını önemli ölçüde etkileyecek biçimde yapay zekâ ya da yazılım kullanımı. Kapsamı belirsiz, *doğrulanmalı*. Temkinli okuma: satın alma kararını etkileyen bir varlık (ürün görseli, sahne, ses, özet) yapay zekâyla üretildiyse kreatifte yaz.
  - (b) İnsandan ayırt edilemeyen yapay zekâ karakteri. Kreatifte yaz: "Görseldeki kişi yapay zekâ ile oluşturulmuştur."
- [ ] **Yapay zekâ seslendirme ya da dublaj** (Meta seslendirme çevirisi dahil) kullanıldıysa kreatifte açıkça yazıyor. Gerçek bir kişinin sesi kopyalandıysa kişinin yazılı izni var. Yazılı izin yoksa kullanma.
- [ ] Meta'nın "Yapay zeka bilgileri" etiketi çoğu durumda üç nokta > "Bu reklam hakkında" ekranında, bazı durumlarda "Reklam" etiketinin yanında görünür [RESMİ]. Reklamverenin kendi açıklamasını ekleyebildiği bölgeler (AB, Kaliforniya, New York, Hindistan, Tayvan) arasında Türkiye yok. Etiketin md. 18/8'i karşıladığı doğrulanmadı; ibareyi kreatife de yaz.
- [ ] Gerçek kişinin yapay zekâ kopyası (görüntü ya da ses) ürünü kullanmış ya da öneriyor gibi gösterilmiyor (md. 27/12); ünlü adı, yüzü ya da sesi yok.
- [ ] Müzik ve görsel lisanslı; gösterilen ürün açılış sayfasındakiyle aynı.
- [ ] Yayındaki reklamlar haftada en az bir kez Gelişmiş önizleme > Advantage+ kreatifi sekmesinden denetleniyor (*sezgisel*).

**Harici dil modeliyle metin:** Modele brifi, belgeli iddia listesini, 125/40/25 sınırlarını ve §11 yasaklarını ver. Alan başına 5 varyasyonu **farklı açılardan** iste. Çıktıda uydurma sayı, "en/tek/garanti", "sen + özellik" kalıbı ve çelişen fiyat ara.

---

## 11. Politika uyumlu dil

### 11.1 Kişisel özellik kuralı [RESMİ]

- Reklam, görenin kişisel özelliğini **söyleyemez, ima edemez, soru olarak soramaz**.
- **Yasak özellikler:** ırk, etnik köken, renk, milli köken, din, yaş, cinsiyet, cinsel yönelim, cinsel kimlik, aile durumu, engellilik, tıbbi/genetik durum, fiziksel ya da ruhsal sağlık, hassas finansal durum, oy kullanma durumu, sendika üyeliği, sabıka kaydı, ad.
- "Sen/siz" hitabı serbest; yeter ki bu özelliklerden söz edilmesin.
- **Yeniden yazma formülü:** "Sen/siz + [özellik]?" yerine **"[ürün/hizmet] + [fayda]"** ya da **"[durum/meslek/ilgi] için…"**.
- **Hızlı test:** "Bu cümle, okuyanın X olduğunu bildiğimi ima ediyor mu?" Evetse yeniden yaz.

### 11.2 Doğru / yanlış

| Yanlış | Doğru | Neden |
|---|---|---|
| "Kilolu musun? Yaza 10 kiloyla gir!" | "Yaz öncesi 8 haftalık grup programı: haftada 3 ders." | Görünüm/sağlık iması + süre-sonuç vaadi |
| "Diyabet hastası mısınız? Bu çay şekerinizi düşürür." | "Şeker ilavesiz tatlılarımız menüde yeşil etiketle." | Sağlık iması + sağlık beyanı [MEVZUAT] |
| "40'ını geçtin, cildin eskisi gibi değil." | "Gece rutinine hafif dokulu yeni krem: içerik listesi ürün sayfasında." | Yaş iması, görünümü küçümseme |
| "Saç dökülmesinden utanıyor musunuz?" | "Saç tipine göre şampuan seçimi: kuaförümüz anlatıyor." | Sağlık/görünüm; "dökülmeyi durdurur" da sağlık beyanı |
| "Borçlardan bunaldın mı?" | "Aylık bütçe planlama şablonu: ücretsiz indir." | Hassas finansal durum |
| "Engelli misiniz? Size yardımcı olabiliriz." | "Mağazamızda rampa ve asansör var." | Engellilik (Meta örneği) |
| "Ayşe, bu indirim sana özel!" | "Bu hafta tüm üyelere: …" | Ad da yasak listede |
| "Türkiye'nin en ucuz kombi servisi" | "Kombi bakımı [950 TL], parça hariç, fiyat önceden yazılı." | Üstünlük iddiası bağımsız kanıt ister (md. 7, 9) |
| "%100 garantili sonuç" | "İşçilikte [6] ay garanti, şartlar sitede." | Gerçekçi olmayan beklenti |
| "Son 2 saat!" (doğru değilse) | "Kampanya 26 Ekim 23.59'da bitiyor." | Sahte aciliyet (Ek A-7) |
| "Bedava kargo!" (şartlıysa) | "500 TL ve üzeri siparişte kargo ücretsiz." | Şart ana mesajda (md. 13) |
| "X markasından %30 ucuz" | "Kaban [1.440 TL], su itici kumaş." | Rakip adıyla karşılaştırma yasak (md. 8) |
| "Doğa dostu ambalaj" | "Kutu geri dönüştürülmüş karton, belge: [kurum]." | Çevre beyanı açıklama + belge ister (md. 17) |
| Instagram yorumu ekran görüntüsü | Siparişi doğrulanmış site/pazaryeri yorumu | Doğrulanmamış yorum reklamda kullanılamaz (md. 28/B-6) |
| Önlüklü "uzman" takviye öneriyor | Ürün, içerik listesi, onaylı beyan | Sağlık meslek mensubu izlenimi yasak (md. 16/3) |
| Takviyede "ayda 5 kilo" | Kilo rakamı yok | Kilo miktarına atıf her durumda yasak (sağlık beyanı yönetmeliği md. 5/11) |
| Çocuk ürününde "Annene söyle, sadece 99 TL!" | Veliye yönelik: "99 TL" | Çocuğu ikna aracı yapmak ve "sadece" yasak (md. 24) |

### 11.3 Sektör kapıları

Tam kapı **Modül 09 §1**'dedir. Bu bölüm kreatif için özettir; kırmızı kapıdaysan kreatif üretme.

- **İstisnasız yasak, kreatif yok [MEVZUAT]:** alkol; tütün, nargile, e-sigara, vape (nikotinsiz dahil) ve beşerî ilaç (md. 27/11); yasa dışı bahis, şans oyunu, kumar ve falcı, medyum, astrolog (md. 27/3); sohbet, arkadaşlık, eş bulma (md. 27/8); silah (md. 27/9); THC ve CBD (Modül 09 §1).
- **Dur, hukukçuya sor:** Türkiye'deki hastaya yönelik klinik ve sağlık meslek mensubu (aşağıda); avukat; kripto (Meta ön yetkisi + SPK kuralları); lisanslı şans oyunu.
- **Klinik (diş, estetik, hekim):** Türkiye'deki hastaya ücretli reklam yok. Hasta görseli, önce–sonra, fiyat ve kampanya sponsorlu yayınlanamaz. Meta önce–sonraya 18+ ile izin verse de Türk mevzuatı bunu kapatıyor. İstisnalar: açılış ayı; yetki belgeli sağlık turizmi (Türkçe dışı dil, yurt dışı kitle, otomatik kitle kapalı).
- **Alkol:** hem Meta hem yasa açısından yasak; restoran ve otel görsellerinde de gösterme.
- **Kozmetik:** gıda dışı üründe sağlık beyanı yasak ("tedavi eder", "sivilceyi geçirir" yok). Meta'da kozmetik işlem ve kilo ürünleri 18+.
- **Kurs:** öğrenci resmi ve bilgisi yok. **Emlak ilanı:** EİDS doğrulamalı ilan bağlantısı paylaşımın içinde, fiyat, fiyat varsa brüt ve net alan (md. 27/5); görseller o ilana ait (§4.5). **Konaklama:** belge numarası.
- **Kredi, finans, sigorta, yatırım; takviye; kilo verme; çekiliş:** insan onayı olmadan kreatif yayına çıkmaz. Takviyede sağlık beyanı ve kilo rakamı yok (§11.2). Çekilişte MPİ izni gerekebilir (§5.3).

**Meta'nın onayı hukuki uygunluk kanıtı değildir.** Meta'nın kendi kuralı da yürürlükteki yasaya uymayı şart koşuyor [RESMİ]. 2026'da internet reklamı ihlalinin ceza aralığı **1.083.706–10.837.065 TL**; sonradan düzeltmek sorumluluğu kaldırmaz [MEVZUAT].

---

## 12. Yayın öncesi kreatif kontrol listesi

- [ ] Risk kapısı (Modül 09 §1) geçildi. İş istisnasız yasak listede değil: alkol, tütün/nargile/e-sigara/vape, beşerî ilaç, bahis/kumar, falcı/medyum, sohbet/arkadaşlık/eş bulma, silah, THC/CBD. Klinik, avukat, kripto, lisanslı şans oyunu, kredi/finans, takviye, kilo verme ve çekilişte yazılı insan onayı var.
- [ ] Her kavramda 9:16, 4:5 ve 1:1 var; Reklam Yöneticisi'nde "alan desteklenmiyor" uyarısı yok; yazılar güvenli bölgede; Akış, Hikayeler ve Reels önizlendi.
- [ ] İlk 1–3 sn'de kanca var; logo ekranı yok; ürün en az 2 kez görünüyor; sesli + görsel CTA var; Türkçe altyazı var.
- [ ] 5 birincil metin + 5 başlık var, çelişmiyor; ilk satır teklifi söylüyor.
- [ ] Köşeli parantezli sayıların hepsi belgeli veriyle dolduruldu ya da cümle çıkarıldı.
- [ ] İndirimde önceki fiyat (10 gün, aynı kanal), tarih ve adet var; fiyat vergiler dahil TL.
- [ ] Son 10 günün fiyat geçmişi kontrol edildi; önceki kampanya fiyatı R'yi düşürmüyor (§5.3).
- [ ] İndirimli fiyat görselde, videoda ya da başlıkta görünüyorsa önceki fiyat ve tarih de aynı yerde görünüyor; başlıkta tek başına indirimli fiyat yok.
- [ ] Fiyat varsa kargo ya da teslim ücreti, yoksa ücretsiz kargo şartı yazılı (md. 13/5).
- [ ] Emlak ilanında EİDS bağlantısı birincil metinde, fiyat ve brüt/net alan yazılı, görseller o ilana ait.
- [ ] Kişisel özellik iması ve "en/garanti/bedava" kalıbı yok; yorumlar doğrulanmış alıcıdan.
- [ ] UGC'de "Reklam/Tanıtım" + reklamveren adı başta.
- [ ] Advantage+ iyileştirmeleri tek tek kontrol edildi; yüksek riskliler kapalı. İndirimde ve düzenlemeye tabi işte "Detayları zaman içinde göster", "İlgi odaklarını göster" ve "Özetleri göster" kapalı. Hesap düzeyi test kutusuna bakıldı.
- [ ] Yapay zekâ ile üretilen insan, ses ya da dublaj kreatifte açıklandı; dil kısıtlı işte çeviriler kapalı.
- [ ] Yeni parti öncekinden en az 2 eksende farklı. Set başına reklam sayısı N'ye uygun (§8.2). Sayfa başına reklam sınırı aşılmıyor.
- [ ] Açılış sayfası ya da karşılama mesajı aynı teklifi söylüyor; adlar Modül 02 §10'a uygun.

---

## Doğrulanması gerekenler

- **Kreatif testi aracı:** resmi yardım sayfası yok. 5/10 reklam sınırı, %20 bütçe önerisi ve "yalnız En yüksek hacim" şartı pratisyen gözlemi.
- **Multimedya reklam ve esnek format:** hangi hesapta hangisinin açık olduğu, varlık bazında raporlama.
- **Advantage+ varsayılanları:** Meta sayfası ile Meta sözcüsünün açıklaması çelişiyor.
- **Meta etiketleri ve Türk hukuku:** "Yapay zeka bilgileri" etiketi md. 18/8'i, ortaklık reklamı etiketi md. 23/A'yı karşılıyor mu? "Alakalı yorumlar" md. 28/B-6 kapsamında mı? Md. 27/1 Meta reklamına uygulanır mı? Md. 18/8'in "ekonomik davranışı önemli ölçüde etkileyen yapay zekâ kullanımı" kısmı neleri kapsıyor? Meta'nın seslendirme çevirisi konuşmacının sesini kopyalıyor mu?
- **İndirim ve çekiliş:** Kampanyayı yeni tarihle uzatmak md. 14/3 açısından yeni başlangıç mı? Sosyal medya çekilişinde MPİ izni 2026'da hâlâ gerekli mi, hangi ödül tutarına kadar muaf?
- **Emlak:** Portföy, proje ve değerleme reklamı EİDS bağlantısı gerektiren "ilan" mı?
- **Sayfa başına reklam sınırı:** Eylül 2026'da bazı hesaplarda kalktığı bildirildi; Meta duyurmadı.
- **Kilo vermede önce–sonra:** Meta Şeffaflık Merkezi 22 Temmuz 2026'da güncellendi, kapsam net değil; sağlık/fitness kreatifinden önce canlı sayfaya bak.
- **Türkçe destek:** yapay zekâ video, müzik, persona görseli ve sanal deneme özelliklerinin Türkiye'de kullanılabilirliği.
- **Rakamlar ve eşikler:** Toluna çarpanları, %16 EBM, %17 yükseliş ve %75 sesli Reels için örneklem açıklanmıyor. Reels alt %35, kanca/tutma bantları, sıklık 2,5–3,5 ve 2–3 × CPA Meta kaynaklı değil.

---

## Kaynaklar

**Meta yardım merkezi [RESMİ]** (erişim Ekim 2026):
- Video reklamları en iyi uygulamaları: https://www.facebook.com/business/help/1381779698788633
- Dönüşüm için basit video türleri: https://www.facebook.com/business/help/565573477186194
- Hikayeler kreatifi ve güvenli bölge: https://www.facebook.com/business/help/304846896685564
- Uygun maliyetli kreatif ve altyazı: https://www.facebook.com/business/help/1991663177718491
- Altyazı (SRT): https://www.facebook.com/business/help/1675722002698686
- Görsel reklam en iyi uygulamaları: https://www.facebook.com/business/help/388369961318508
- Güvenli bölgeler: https://www.facebook.com/business/help/980593475366490
- Düşük kaliteli kreatif: https://www.facebook.com/business/help/1767120243598011
- Reklam formatları: https://www.facebook.com/business/help/1263626780415224
- Desteklenen oranlar: https://www.facebook.com/business/help/682655495435254
- Oran en iyi uygulamaları: https://www.facebook.com/business/help/103816146375741
- Minimum görsel pikselleri: https://www.facebook.com/business/help/469767027114079
- Görsel dosya şartları: https://www.facebook.com/business/help/755224297893936
- Video dosya şartları: https://www.facebook.com/business/help/1596868350601716
- Video süreleri: https://www.facebook.com/business/help/817989058548892
- Instagram Reels reklamları: https://www.facebook.com/business/help/4023434621109377
- Metin seçenekleri ve uzunluklar: https://www.facebook.com/business/help/223409425500940
- Metin örnekleri ve metin oluşturma dili: https://www.facebook.com/business/help/180641596861873
- Eylem çağrısı düğmeleri: https://www.facebook.com/business/help/410873986524407
- Advantage+ kreatif iyileştirmeleri: https://www.facebook.com/business/help/297506218282224
- Yeni kreatif özelliklerini test etme: https://www.facebook.com/business/help/2084198785720343
- Çeviri desteği: https://www.facebook.com/business/help/1498029841494157
- Yapay zekâ şeffaflık etiketi: https://www.facebook.com/business/help/1010479435004531
- Esnek reklam formatı: https://www.facebook.com/business/help/835561738423867
- Dinamik kreatif: https://www.facebook.com/business/help/170372403538781
- Reklam kreatifi kırılımı: https://www.facebook.com/business/help/243916866413404
- Reklam hacmi: https://www.facebook.com/business/help/2720085414702598
- Hesap sınırları: https://www.facebook.com/business/help/652738434773716
- Kreatif yorgunluğu durumları ve uygunluk şartı (TR ve EN): https://www.facebook.com/business/help/1346816142327858?locale=en_US
- Reklam alanı ayarlarındaki yenilikler: https://www.facebook.com/business/help/1546753450586085
- Sayfa başına reklam sınırları: https://www.facebook.com/business/help/766697140509126
- A/B testi: https://www.facebook.com/business/help/1738164643098669
- Kişisel özellikler ilkesi: https://www.facebook.com/business/help/2557868957763449
- Reklam kontrol listesi: https://www.facebook.com/business/help/757209948405699

**Diğer Meta kaynakları [RESMİ]:**
- State of Creative Optimization 2025: https://www.facebook.com/business/news/report-state-creative-optimization-meta-appsflyer-dentsu-creative
- Reels kreatif stratejileri (Toluna): https://www.facebook.com/business/news/reels-creative-strategies
- Kreatif farklılaştırma: https://www.facebook.com/business/m/small-business/creative-differentiation
- Reels reklamları: https://developers.facebook.com/blog/post/2024/11/07/unlock-the-power-of-reel-ads/
- Instagram ortaklık reklamları: https://developers.facebook.com/blog/post/2025/03/25/instagram-partnership-ads/
- Kişisel özellikler (Şeffaflık Merkezi): https://transparency.meta.com/policies/ad-standards/objectionable-content/privacy-violations-personal-attributes/
- Sağlık ve sağlıklı yaşam: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/health-wellness/
- Alkol: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/alcohol/

**[MEVZUAT]:**
- Ticari Reklam Yönetmeliği değişikliği (RG 01.07.2026, yürürlük 01.08.2026): https://www.resmigazete.gov.tr/eskiler/2026/07/20260701-9.htm
- Yönetmeliğin konsolide metni: https://www.mevzuat.gov.tr/File/GeneratePdf?mevzuatNo=20435&mevzuatTur=KurumVeKurulusYonetmeligi&mevzuatTertip=5
- Fiyat Etiketi Yönetmeliği değişikliği (10 gün): https://www.resmigazete.gov.tr/eskiler/2025/10/20251011-6.htm
- Sağlık hizmetlerinde tanıtım yönetmeliği: https://www.resmigazete.gov.tr/eskiler/2025/11/20251112-2.htm
- Gıda ve takviyede sağlık beyanı: https://www.resmigazete.gov.tr/eskiler/2023/04/20230420-5.htm
- 2026 idari para cezaları: https://www.resmigazete.gov.tr/eskiler/2025/12/20251223-6.htm
- Emlak ilanlarında EİDS bağlantısı ve fiyat (Ticaret Bakanlığı uygulaması, haber): https://www.ekonomist.com.tr/gundem/sosyal-medyadaki-emlak-ve-arac-ilanlarina-siki-denetim-82-isletmeye-8-2-milyon-tl-ceza-78039
- Belgesiz konaklama tanıtımı genelgesi 2026/4 (haber): https://www.alomaliye.com/2026/08/18/belgesiz-konaklama-ilanlarina-siki-denetim/
- Özel öğretim kurumları reklamı, 5580 s. Kanun m. 7 (haber): https://www.gzt.com/ekonomi/anayasa-mahkemesi-emsal-karar-ozel-okullara-kesilen-5-kat-katlamali-ceza-hukmu-tarihe-karisiyor-egitim-haberleri-4223845
- Sosyal medya çekilişleri ve MPİ izni (Karşılığı Nakit Olmayan Piyangolar ve Çekilişler Hakkında Yönetmelik; hukuk yazısı, güncelleme Aralık 2022): https://www.hukukihaber.net/sosyal-medyada-yapilan-cekilisler-yasal-midir-belirli-bir-yaptirimi-mevcut-mudur
- Sosyal medya etkileyicileri kılavuzu (2021): https://iabtr.org/wp-content/uploads/2024/06/Sosyal-Medya-Etkileyicileri-Tarafindan-Yapilan-Ticari-Reklam-ve-Haksiz-Ticari-Uygulamalar-Hakkinda-Kilavuz1062021101250.pdf

**[PRATİSYEN]:**
- Loomer, kreatif süreci: https://pubcast.jonloomer.com/your-ad-creation-process-might-be-the-problem/
- Loomer, hacim stratejisi: https://pubcast.jonloomer.com/a-simple-volume-focused-ad-strategy/
- Loomer, kreatif testi süreci: https://pubcast.jonloomer.com/make-creative-testing-part-of-your-process/
- Loomer, yorgunluğun yanlış teşhisi: https://pubcast.jonloomer.com/why-you-are-misdiagnosing-creative-fatigue/
- Loomer, alan kontrolünün kalkması (arka plan; güncel kapsam Meta'nın reklam alanı ayarları sayfasında): https://pubcast.jonloomer.com/what-does-the-removal-of-placement-control-mean/
- Loomer, kreatif testi aracı: https://www.jonloomer.com/meta-creative-testing/
- Motion, Creative Benchmarks 2026: https://motionapp.com/library/research/creative-benchmarks-2026/
- Motion, kanca taktikleri: https://motionapp.com/library/research/creative-benchmarks-2026/top-hook-tactics
- Motion, kanca yazımı (farkındalık seviyeleri): https://motionapp.com/library/frameworks/hook-writing
- Motion, kreatif metrikleri: https://motionapp.com/blog/key-creative-performance-metrics
- Foxwell, hacme göre kreatif: https://www.foxwelldigital.com/blog/meta-ads-how-much-creative-is-needed-by-volume
- Common Thread Collective, kreatif hacmi: https://commonthreadco.com/blogs/coachs-corner/meta-andromeda-roas-creative-strategy-2026
- Crazy Egg, PAS: https://www.crazyegg.com/blog/pas-framework/
- Hormozi değer denklemi (ikincil özet, yalnız arama özetinden): https://www.supersummary.com/100m-offers/section-3-summary/

**[HABER]:**
- Multimedya reklamlar: https://www.socialmediatoday.com/news/meta-outlines-best-practices-for-ai-generated-multimedia-ads/823720/
- İstenmeyen yapay zekâ kreatifi olayları: https://www.shopifreaks.com/meta-advantage-is-forcing-unwanted-ai-creative-on-brands/
- REI olayı: https://www.gadgetreview.com/?p=388712
- Sayfa başına 250 reklam sınırının bazı hesaplarda kalkması (Eylül 2026, Meta doğrulamadı): https://ppc.land/meta-drops-250-ad-page-cap-in-some-accounts-as-286-ads-keep-running/

---

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

---

# Test, optimizasyon ve ölçekleme

> **Modül 06 · Profesyonel paket · Hazırlanma: 17 Ekim 2026.** Menü yolları, ödeme ve hata kodları burada yok. Onlar için **Kurulum Kılavuzu** (`../KILAVUZ.md`): bütçe esnekliği §5.2, ilk 7 gün §6.3, sorun giderme §10, konum ücreti ve KDV §3.6 ve §9. Bu modül şu formülleri hazır kabul eder: başabaş CPA/ROAS ve panel çarpanı (Modül 02 F1–F3), haftalık beklenen sonuç **N** (F4), set kapasitesi (F5), marjinal CPA (F7, Modül 01 §3.4). Kreatifte "farklı" tanımı ve yorgunluk tablosu Modül 04 §8–9'da.

## Bu modülde

- Tek değişkenli **test hipotezi** kalıbı, hücre başına olaya göre **ölçülebilir fark tablosu**, test aracı seçimi ve hacme göre **kreatif test kurgusu**.
- İlk **7 / 14 / 30 / 90 gün** için gün gün plan ve haftalık karar tablosu.
- Aşamalı **kapatma kuralları** (3× / 5× CPA mantığı ve olasılık tablosu) ile **ölçekleme kuralları**: dikey ve yatay, adım büyüklüğü, bekleme süresi, geri dönüş, riskler. WhatsApp, form ve telefon işlerinde CRM'e dayalı karar (§6.5).
- **Öğrenme aşamasını koruma**, **kırılım etkisi** (breakdown effect) kontrol listesi ve 11.11, Kara Cuma, Ramazan 2027 için **sezon planı**.
- Kopyalanabilir **değişiklik günlüğü** şablonu.

**Etiketler:** **[RESMİ]** Meta'nın kendi sayfası. **[MEVZUAT]** Türk mevzuatı (ayrıntı Modül 09). **[PRATİSYEN]** ajans, danışman ya da araç sağlayıcı (çıkar çatışması olabilir). **[HABER]** basın. *sezgisel* = ölçülmemiş pratisyen kuralı. *türetilmiş* = bu modülde yapılan aritmetik. *varsayım* = örnek rakam. **Genel uyarı:** Meta yalnızca süre (en az 7 gün), hacim (haftada ~50 sonuç) ve birkaç bütçe kuralı veriyor. "%20 artır", "3× CPA'da kapat" gibi eşiklerin hepsi pratisyen kuralıdır.

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
| Birincil metrik | Optimize edilen olayın sonuç başına ücreti ya da ROAS'ı. TO ve BGBM ikincil. Mesaj ve form işlerinde CRM'deki satış başı maliyet (§6.5) |
| Beklenen fark ([%X]) | Aşağıdaki tablodan okunur: hücre başına olay sayısının ölçebildiği farktan küçük bir hedef yazılmaz |
| Hücre başına olay | **Her hücrede ≥ 50 → karar testi.** **< 50 → yalnız "yön göstergesi":** ölçeklemeye ya da hesap çapında değişikliğe dayanak olmaz (*türetilmiş*, tablo) |
| Süre | 7–30 gün. Karar süreci uzun işte 10–14 gün [RESMİ] |
| Karar kuralı | Önceden yaz. **Karar testi:** "Her hücrede ≥ 50 olay, Meta güveni ≥ %65 (büyük kararda ≥ %90, §2) ve CPA farkı tablodaki eşiğin üstünde → B'ye geç; değilse A'da kal." **Yön göstergesi:** "Yalnız kolay geri alınan karar (ör. kreatifi ana sette açık bırakmak) ve CPA farkı ≥ 2 kat → B; değilse A" (*türetilmiş*) |

**Hücre başına olay → ölçülebilir fark (*türetilmiş*).** Varsayım: 2 hücre, eşit harcama, olaylar bağımsız ve rastgele (Poisson), %95 güven (çift yönlü), %80 güç. CPA oranı = pahalı hücrenin CPA'sı ÷ ucuz hücrenin CPA'sı.

| Her hücrede olay | Güvenle yakalanan en küçük CPA oranı | CPA düşüşü olarak | Gerçekte fark yokken (A/A) ≥ %15 CPA farkı görme olasılığı | A/A'da ≥ 2 kat fark görme olasılığı |
|---|---|---|---|---|
| 7 | ~4,5× | ~%78 | ~%76 | ~%20 |
| 20 | ~2,4× | ~%59 | ~%61 | ~%3 |
| 50 | ~1,75× | ~%43 | ~%42 | < %1 |
| 100 | ~1,5× | ~%33 | ~%25 | ~%0 |
| ~315 | 1,25× | %20 | ~%4 | ~%0 |
| ~600 | 1,18× | %15 | < %1 | ~%0 |

Okuma: hücre başına 20 satışlık bir test "B %15 daha ucuz" diyorsa bu büyük olasılıkla gürültüdür. %15–20'lik farkı ölçmek için hücre başına 300–600 olay gerekir; KOBİ hesabında bu çoğu zaman yoktur. 3 ya da daha çok hücrede yanlış "kazanan" olasılığı tablodakinden yüksektir. Meta'nın %65 güveni, klasik %95 güvenle aynı şey değildir (§2).

**Bütçe formülü (*türetilmiş*; Flighted'in formülüyle aynı mantık [PRATİSYEN]):**

```
Günlük test bütçesi = hücre sayısı × hücre başına olay × beklenen CPA ÷ test günü
Test payı           = günlük test bütçesi ÷ hesabın günlük bütçesi   (hedef ≤ %20)
```

**Doldurulmuş örnekler (*varsayım*):**

- **H-2610-01 (e-ticaret):** "Eğer ana videonun ilk 3 saniyesine 'Beden olmazsa 30 gün içinde ücretsiz değişim' teklifini koyarsak satış CPA'sı belirgin biçimde düşer (bu hacimde ölçülebilen en küçük fark ≈ %43), çünkü sohbetlerde en sık sorulan şey beden ve değişim. Ölçüt: A/B testi (kreatif değişkeni), 2 hücre, 25 gün, hücre başına 50 satış." Teklif yalnız işletme bunu gerçekten sunuyorsa yazılır.
  - **Neden "ücretsiz iade" değil?** 14 günlük cayma süresinde, satıcının belirttiği taşıyıcıyla (ya da taşıyıcı belirtilmemişse her durumda) iade masrafı tüketiciden alınamaz [MEVZUAT: Mesafeli Sözleşmeler Yönetmeliği md. 12/5, değişiklik RG 24.5.2025/32909]. Yasal hak reklamda fazladan sunulan bir avantaj gibi gösterilemez; yasal hakları satıcının avantajıymış gibi sunmak her durumda haksız ticari uygulama sayılır [MEVZUAT: Ticari Reklam ve Haksız Ticari Uygulamalar Yönetmeliği md. 7/7 ve Ek A-9]. **Kural:** Yasal haklar (14 gün cayma, cayma iadesinde kargo masrafı, garanti) kanca ya da avantaj olarak yazılmaz. Kanca ancak yasal hakkı aşan bir teklif olabilir: 30 gün değişim, kapıdan ücretsiz değişim gibi. Ayrıntı Modül 09 §5.2.
  - Bütçe: 2 × 50 × 200 TL ÷ 25 = **800 TL/gün**. Hesap günde 4.000 TL harcıyorsa pay %20, uygun.
  - Hesap günde 2.000 TL harcıyorsa pay %40 olur ve süreyi uzatmak 30 gün sınırını aşar. Hücre hedefini 20 satışa indir (2 × 20 × 200 ÷ 20 gün = 400 TL/gün, pay %20). Sonucu "yön göstergesi" diye kaydet: yalnız ≥ 2 kat CPA farkı kabul edilir ve sonuç ölçeklemeye dayanak olmaz.
- **H-2610-02 (WhatsApp'tan satan yerel hizmet):** "Eğer optimizasyon olayını 'mesajlaşma konuşması'ndan 'mesajlaşma yoluyla potansiyel müşteri'ye çevirirsek iş başı maliyet düşer, çünkü sohbetlerin çoğu fiyat sorup kayboluyor. Ölçüt: A/B testi, eşit bütçe, 14 gün." Olaylar farklı olduğu için kararı CRM'deki iş başı maliyetle ver (§6.5). Kolların her birinde CRM'de 50'den az iş varsa sonuç yön göstergesidir. Olayın hesapta açık olup olmadığı *doğrulanmalı* (Modül 02).

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
- Önce hücre başına olaya bak (§1 tablosu). Her hücrede 50'den az olay varsa Meta'nın güven yüzdesi ne olursa olsun sonuç **yön göstergesidir**. Yalnız kolay geri alınan kararda ve CPA farkı ≥ 2 kat ise uygula.
- Her hücrede ≥ 50 olay, güven ≥ %65 ve karar kolay geri alınır (ör. kreatifi ana sete eklemek) → uygula.
- Karar büyük (ör. tüm hesabın teklif stratejisi) ve güven < %90 → testi uzat ya da tekrarla. %65, klasik %95 standardına göre zayıf bir eşik.
- "Kesin kazanan yok" → fark küçük. Mevcut durumu ya da ucuz seçeneği koru.

---

## 3. Kreatif test kurgusu

**Yöntem seçimi (N = haftalık beklenen sonuç):**

| N | Yöntem | Nasıl |
|---|---|---|
| < 10 | **Test yok, portföy** | Tek set, 2–3 gerçekten farklı kavram. 2–4 haftalık toplam sonuca bak. Reklam bazında kapatma kuralı işlemez [PRATİSYEN] |
| 10–50 | **Aynı sete parti ekle** | Toplam sonuç vasat ya da kötüyse 1–2 haftada bir, tamamen farklı 2–3 kavramlık parti. Bir hafta dokunma [PRATİSYEN Loomer] |
| 50–150 | **Kreatif testi aracı, aynı sette** | Haftada bir test, 2–4 hücre, pay ≤ %20. Bu hacimde haftalık test **eleme** yapar, kazanan seçmez: hücre başına olay genelde 50'nin altında kalır, sonuç yön göstergesidir (§1). Set hedefli teklifteyse araç çalışmaz → A/B testi ya da parti ekleme |
| > 150 | **Test → doğrulama → ana kampanya** | Reklam seti bütçeli ayrı test kampanyası; kazananlar doğrulama katmanına (ana bütçenin ~%20'si), sonra ana kampanyaya [PRATİSYEN Foxwell, Flighted] |

**Çelişki:** CTC ayrı test bütçesi tutmuyor, yeni reklamı doğrudan ana kampanyaya koyuyor. Loomer, testte kazananın taşındığı yerde kazanmayabileceğini ve yapının şiştiğini yazıyor [PRATİSYEN]. **Varsayılan:** N ≤ 150 ise ayrı test kampanyası açma.

**Doldurulmuş kurgu (*varsayım*):** Günlük 4.000 TL, hedef panel CPA 200 TL → N = 4.000 × 7 ÷ 200 = 140.
- Pay %20 → haftada 5.600 TL. Kavram başına karar harcaması 3 × 200 = 600 TL → teorik üst sınır ~9 kavram (Modül 04 §8.2). Araç en fazla 5 hücre aldığı için pratikte haftada **bir test, 4 hücre**: hücre başına 1.400 TL ≈ 7 × CPA, yani **~7 satış**.
- **Bu bir eleme testidir, sonucu yön göstergesidir.** Hücre başına ~7 satışla ancak ~4,5 katlık fark güvenle görülür; gerçekte fark yokken bile iki hücre arasında ~%20 olasılıkla 2 katlık fark çıkar, 4 hücrede bu daha da sık olur (§1 tablosu, *türetilmiş*).
- Test bitmeden hücre kapatma; eşitlik bozulur.
- **Aday (eleme kararı):** CPA, ana setin 7 günlük CPA'sına eşit ya da düşük ve hücreler içinde belirgin önde. Tek sonuç: aday **aynı sette açık kalır.** Test bitince eşit harcama kalkar; sistem adayı her zaman öne çıkarmayabilir [PRATİSYEN]. Toplam sonuç iyiyse dokunma. Bu karar kolay geri alınır, ölçeklemeye ya da hesap çapında değişikliğe dayanak olmaz.
- **Doğrulama (hesap çapında karar gerekiyorsa, ör. yeni teklif açısını bütün kreatiflere taşımak):** 2 hücre, aday ↔ mevcut en iyi reklam, hücre başına 50 satış → 2 × 50 × 200 = 20.000 TL. %20 payla (5.600 TL/hafta) ≈ **25 gün**. "Kazanan"ı yalnız bu test seçer, §1'deki karar kuralıyla.
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
| **5–6** | **Reklam** kapatma yalnız üç koşul birlikte sağlanırsa (§5.2 Aşama 4): (a) harcama 3 × hedef CPA'yı geçeli en az 72 saat oldu ve hâlâ 0 dönüşüm var; (b) set haftada ≥ 10 dönüşüm bekliyor (N ≥ 10); (c) hedef CPA ölçülmüş veriye dayanıyor (geçmiş kampanya ya da brifteki gerçek sipariş verisi, Modül 01), yalnız hesaplanmış bir hedef değil. Biri eksikse yalnız günlüğe not al, gün 8'de set düzeyinde karar ver (§4.4, §8) | Set kapatma, yeni set açma |
| **7** | İlk haftanın ön okuması; günlüğe yaz | Karar. Son 48–72 saatin dönüşümleri henüz gelmemiş olabilir [PRATİSYEN] |
| **8** | **1. hafta kararı**, kampanya/set düzeyinde (§4.4) | Kırılımda en çok harcayanı kapatma |
| **9–10** | Değişiklik yapıldıysa dokunma. Bütçe arttıysa harcamanın yeni seviyeye çıktığını kontrol et | Başka türde ikinci değişiklik |
| **11–12** | Ölçeklenen sette 3–4. gün kontrolü; koşul sağlanırsa ikinci adım (§6.2). Bütçe adımıyla aynı gün parti ya da test eklendiyse ara adım yok (§6.1) | — |
| **13–14** | 2. haftayı 1. haftayla kıyasla: ortalama ve marjinal CPA, sıklık, TO eğilimi. Taban çizgisini yaz | Hedefli teklife geçmek (2 hafta dolmadı) |

### 4.2 Gün 15–30

| Gün | Yap |
|---|---|
| **15** | **2. hafta kararı.** 2 haftalık veri ve set haftada 50+ sonuç alıyorsa hedefli teklifi değerlendir: sonuç başına ücret hedefi = son 14 günün panel CPA'sı × 1,10–1,20, başabaşı geçmesin [RESMİ; Modül 02 §7]. Meta'ya göre bu hedef set başına haftada 50–100+ dönüşümde en iyi çalışır [RESMİ]. **Geçiş yolu:** Set haftada **≥ 100 sonuç** alıyorsa A/B testiyle geç (en yüksek hacim ↔ hedef). A/B testi bütçeyi ve kitleyi ikiye böler; her kolun ~50+ sonuç alması gerekir. **100'ün altındaysa** test, hedefli kolu ~25 sonuçla yapay olarak dezavantajlı bırakır. Bu durumda mevcut sette yerinde geç (önemli düzenleme, §7), 7 gün dokunma, sonucu 14 günlük taban çizgisiyle kıyasla. Kötüleşirse en yüksek hacme dön (*sezgisel*). Kopya set açma: açık artırma çakışması (§6.3) |
| **16–21** | Hedef değiştiyse 7 gün dokunma [RESMİ]. Kreatif testi sürüyorsa bitmesini bekle |
| **22** | **3. hafta kararı.** Bütçe tamamen harcanıyor ve CPA hedefte → §6.1 kapısından geçen setleri ölçekle (§6). Bu, **yalnız en yüksek hacimde kalan setler** içindir. Gün 15'te hedefli teklife geçen set en erken **gün 36'da** ölçeklenir, çünkü Meta ölçeklemeden önce en az 3 hafta yayın istiyor [RESMİ]. Hedefli sette bütçe harcanmıyorsa (teklif kısıtlı) → hedefi %5–10 gevşet [RESMİ] |
| **23–28** | Haftada 50'ye yaklaşılamıyorsa setleri birleştir ya da daha sık gerçekleşen olaya in [RESMİ] |
| **29–30** | **Aylık özet:** kazanan ve kaybeden açılar, ölçülmüş CPA, dönüşüm oranı, sepet tutarı. Modül 01 hedeflerini bunlarla güncelle |

### 4.3 Gün 31–90

| Hafta | Odak |
|---|---|
| **5–6** (gün 31–44) | Kreatif ritmini oturt (Modül 04 §8.2). Haftalık ölçekleme döngüsü. Hedefli teklif 3 haftayı doldurduysa onu da ölçekle [RESMİ]; gün 15'te geçen set için en erken gün 36 |
| **7–8** (gün 45–58) | İlk **yatay** adım (§6.3). Değer optimizasyonu uygunsa A/B testiyle dene |
| **9–10** (gün 59–72) | **Artırımlılık:** yükseliş testi ya da coğrafi kapatma testi (Modül 05 §11); yeniden hedefleme payını sorgula. Artımlı ilişkilendirme sütunuyla kampanyaları **kendi aralarında** kıyasla, ör. yeni müşteri kampanyası ile yeniden hedefleme [RESMİ]. Artımlı sayıyı standart sayıya bölüp oran çıkarma; Meta farklı ilişkilendirme modellerini birbiriyle kıyaslamayı önermiyor. Sütun 1 Nisan 2025 öncesi tarih aralıklarında veri vermez [RESMİ]. Hesapta görünmesi *hesaba göre değişebilir* |
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
| 3 | 50–200 tıklama | Aynı sayfaya giden diğer reklamlar dönüşürken bunun tıklamaları dönüşmüyor [PRATİSYEN AdManage]. **Yalnız uyarı;** kapatma ancak Aşama 4 koşulu da sağlanınca. Gerekçe (*türetilmiş*): TBM 5 TL ve CPA 200 TL iken 50 tıklama = 250 TL ≈ 1,25 × CPA eder. Bu harcamada iyi bir reklamın hiç dönüşüm almama olasılığı ~%29 |
| 4 | Harcama ≥ 3 × hedef CPA ve eşik geçileli ≥ 72 saat (dönüşüm gecikmesi, §8 madde 5) | **0 dönüşüm → kapat** [PRATİSYEN Flighted]. **1 → 5 × hedef CPA'ya kadar bekle; orada hâlâ ≤ 1 ise kapat** (*türetilmiş*; Flighted 3×'te 0–1 dönüşümü kapatıyor). **2 ("sarı ışık") → 6,3 × hedef CPA'ya kadar bekle; orada hâlâ ≤ 2 ise kapat** (*türetilmiş*; Flighted 1 × CPA daha harcayıp karar veriyor). **3+ → reklam düzeyinde kapatma kuralı biter;** bundan sonra karar set toplamıyla verilir (§4.4, §8). Olasılıklar aşağıdaki tabloda |

**Neden 3×? (*türetilmiş*)** Gerçek CPA'sı tam hedefte olan iyi bir reklamı yanlışlıkla kapatma olasılığı. Varsayım: dönüşümler bağımsız ve rastgele (Poisson), gecikme yok.

| Harcama (× hedef CPA) | 0 dönüşüm | En çok 1 | En çok 2 |
|---|---|---|---|
| 1× | %36,8 | %73,6 | %92,0 |
| 2× | %13,5 | %40,6 | %67,7 |
| 3× | **%5,0** | %19,9 | %42,3 |
| 5× | %0,7 | **%4,0** | %12,5 |
| 6,3× | %0,2 | %1,3 | **%5,0** |

1×'te kapatmak iyi reklamların üçte birinden fazlasını öldürür. Aşama 4'ün üç kontrol noktası (3× / 5× / 6,3×) tek tek ≤ %5 yanılır, ama birlikte iyi reklamların yaklaşık **%9'unu** yanlışlıkla kapatır. CPA'sı hedefin 2 katı olan kötü reklamın ise ancak ~%46'sını 6,3×'e kadar yakalar (*türetilmiş*, aynı varsayım). Model, CPA'sı hedefin 1,5 katı olan vasat reklamı yakalamaz. Hedef CPA gerçekçi değilse iyi reklamları da kapattırır. Vasat reklamı reklam düzeyinde avlama; sistem ona zaten az harcar [PRATİSYEN CTC]. Set toplamı kötüyse §4.4 ve §5.3'e göre davran.

**İstisnalar (kapatma, bekle):**
- Reklam setin bütçesinin çoğunu alıyor ve set toplamı hedefte → §8.
- Haftada 10'dan az dönüşüm var → reklam kuralı güvenilmez; 2–4 haftalık hesap toplamına bak [PRATİSYEN].
- İlk gün CPA'sının hedefin 2 katı olması tek başına sebep değil [PRATİSYEN].
- İş WhatsApp, form ya da telefonla satıyor → bu tablo yerine §6.5.

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

- [ ] **Hacim ve süre (iki yoldan biri; yol N'ye göre seçilir, keyfe göre değil):**
  - **Hacim yolu (N ≥ 50):** Son 7 tam günde set ya da kampanya düzeyinde **≥ 50 gerçekleşen sonuç** var ve "Öğrenme" durumu bitti. 7 tam günlük veri yeter.
  - **Süre yolu (N < 50):** En az **14 tam günlük** veri var. CPA koşulu (aşağıda) son 14 günle okunur.
- [ ] CPA ≤ hedef (ya da ROAS ≥ hedef), kampanya/set düzeyinde. Hacim yolunda son 7 gün, süre yolunda son 14 gün. Mesaj ve form işlerinde CRM'deki satış başı maliyetle okunur (§6.5).
- [ ] Bütçe tamamen harcanıyor. Harcanmıyorsa bütçe artışı işe yaramaz; önce teklif, kitle ya da kreatif [RESMİ].
- [ ] Hedefli teklif varsa set hedefli teklifle en az **3 haftadır (21 gün)** yayında [RESMİ].
- [ ] Ölçüm sağlam; stok ve hizmet kapasitesi var (Modül 01 §4.3).
- [ ] Bütçe dışı son önemli düzenlemenin (§7) üzerinden en az 7 gün (168 saat) geçti. **Tek istisna, değişiklik günü:** haftalık kreatif partisi ya da kreatif testi, bütçe adımıyla **aynı oturumda** yapılabilir. 7 günlük sayaç ikisi için birlikte başlar. O hafta 3–4. gün ara adımı atılmaz ve test sürerken başka bütçe değişikliği yapılmaz. İki değişikliğin etkisi ayrılamaz; günlükte belirt (§10 kural 6) (*sezgisel*).

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
| 1–2 | 1.500 | 10.500 | 52 | 202 | — | 2 hafta istikrarlı, kapı açık (hacim yolu; 1. haftadan sonra da açıktı) → +%25 → 1.875 |
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
| Kanıtlanmış reklamlarla ikinci (**challenger/genişleme**) kampanya | **Yalnız ana kampanya manuel ya da dar yapıdaysa.** Paketin varsayılan ana kampanyası zaten geniş ve Advantage+ ayarları açık bir satış kampanyası (Modül 02 §4, §5.4). "Advantage+ satış" ayrı bir ürün değil; Advantage+ hedef kitle, alan ve bütçe kullanan satış kampanyasının adı [RESMİ]. Ana kampanya böyleyse aynı reklamlarla ikinci bir Advantage+ satış kampanyası **açma**: aynı kitle, aynı reklam, açık artırmada yalnız biri yarışır [RESMİ]. Ajans kurgusu [PRATİSYEN Flighted, CTC] |
| Ana kampanyada az harcama almış reklamlar için ayrı kampanya ya da ana kampanyanın **teklif üst sınırlı** kopyası | CTC'nin gerçek genişleme kurgusu: ana kampanyada hedef CPA'nın 3 katından az harcama alan reklamlar ayrı kampanyaya taşınır; ayrıca farklı maliyet kontrolüyle (teklif üst sınırı) bir kopya açılır [PRATİSYEN CTC]. Yalnız N ≥ 150 hesapta (*sezgisel*). Çakışma riski sürer: haftalık kontrolde kopyanın harcayıp harcayamadığına ve fırsat puanındaki açık artırma çakışması tavsiyesine bak [RESMİ]. Kopya harcayamıyorsa kapat, bütçeyi ana kampanyaya ver (§6.4) |

**Yapma:** Kazanan seti aynı kitleyle ve aynı ayarlarla kopyalamak (tek istisna, yukarıdaki farklı maliyet kontrollü kopya; N ≥ 150 ve çakışma izlenerek). Benzer setler aynı açık artırmaya girince yalnız biri yarışır; kopya bütçesini harcayamayabilir ve öğrenmeden çıkamayabilir [RESMİ]. Foxwell yerinde artırmayı, Vaizle kopyayı daha güvenli buluyor [PRATİSYEN]. **Varsayılan:** yerinde artırma.

**İleri seviye (N ≥ 150):** Bütçeyi beklenen harcamanın üstünde tut, verimi ROAS hedefi ya da teklif üst sınırıyla sınırla; sistem o getiri eşiğinde bulabildiği kadar harcar [PRATİSYEN CTC]. Risk: hedef çok sıkıysa harcama düşer.

### 6.4 Riskler

| Risk | Belirti | Önlem |
|---|---|---|
| Azalan getiri | Ortalama CPA hedefte, marjinal başabaşın üstünde. Meta da ucuz fırsatlar bittikçe ücretin arttığını söylüyor [RESMİ] | F7 ile karar; geri dön |
| Öğrenmenin sıfırlanması | "Öğrenme" durumu, dalgalı CPA | Adımı küçült, değişiklikleri topla |
| Sıcak kitlenin tükenmesi | İlk haftalar parlak, sonra düşüş | Kitle segmenti kırılımı; yeni soğuk kitle kavramı [PRATİSYEN Loomer] |
| Açık artırma çakışması | Kopya set harcamıyor | Kopyayı kapat, bütçeyi asıl sete ver [RESMİ] |
| Kapasite, stok, nakit | Yanıt süresi uzuyor, iade artıyor; fatura panelin 1,05 katı | Bütçeyi kapasiteye bağla (Modül 01 §4.3); planı faturalı tutarla onayla (Kurulum Kılavuzu §9) |

### 6.5 Mesaj, form ve telefon işlerinde kapatma ve ölçekleme

§5 ve §6'nın eşikleri Meta'nın raporladığı optimizasyon olayına göre yazıldı. WhatsApp, form ya da telefonla satan işte ucuz "konuşma" ya da "potansiyel müşteri" kaliteli satış demek değildir; satış CRM'de ya da satış defterinde günler sonra görünür. Bu işlerde şu kurallar §5–6'nın önüne geçer (eşikler *sezgisel*):

- **Karar metriği:** CRM'deki **satış (ya da iş) başı maliyet** = gerçek harcama ÷ reklam kaynaklı ödenmiş satış (Modül 05 §7.5, §8.3). Konuşma ya da potansiyel müşteri başına ücret yalnız **erken uyarıdır.** §5.2'deki 3× kuralını konuşma başı ücrete uygulamak kaliteyi ölçmez.
- **Gecikme:** Karar penceresi = **en az 7 gün + satış döngüsü** (ilk mesajdan ödemeye geçen ortalama süre, brifte ölçülür, Modül 01 §1.6). Örnek: döngü 5 gün ise pazartesi kararı, 12 ile 6 gün önce arasındaki 7 günlük trafikten gelen satışlarla verilir; son 5 günün trafiği henüz satışa dönmemiş sayılır.
- **Reklam düzeyinde kapatma:** Satış reklama eşlenebiliyorsa (önceden doldurulmuş mesajda kreatif kodu, kanal kuponu; Modül 05 §7.4) Aşama 4 mantığını satış başı maliyete uygula. Eşlenemiyorsa reklamı yalnız Aşama 0–2 sebepleriyle kapat; kalite kararı set düzeyinde verilir.
- **Nitelik oranı:** Sohbet → satış ya da potansiyel müşteri → nitelikli oranını haftalık izle. Oran son 4 haftanın ortalamasından belirgin düşükse (ör. göreli %25+ düşüş) **bütçe artışı yok.** Konuşma başı ücret düşerken oran da düşüyorsa sistem ucuz ama ilgisiz sohbete kayıyor olabilir. Bu durumda optimizasyon olayını yükselt (Modül 05 §7.2, §8.2) ya da forma eleme sorusu ekle.
- **Ölçekleme kapısı (§6.1):** "CPA ≤ hedef" satırı CRM'deki satış başı maliyetle okunur. Meta'daki sonuç sayısı yalnız hacim ve öğrenme koşulu içindir. Yanıt süresi uzuyorsa kapasite dolmuştur; bütçeyi artırma (Modül 01 §4.3).
- **Örnek (*varsayım*, Modül 05 §7.5 rakamları):** Hafta 1'de 210 konuşma, 33 satış, oran %15,7, satış başı 223 TL. Hafta 2'de 260 konuşma (konuşma başı ücret düştü) ama 30 satış: oran %11,5, yani göreli −%27; aynı harcamayla satış başı ~245 TL. Panel daha iyi görünse de bütçe adımı atılmaz; önce yeni gelen sohbetlerin kaynağına bakılır.

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
| Ramazan | 8 Şubat – 8 Mart 2027 [HABER, Diyanet'e atıfla] | 18–25 Ocak 2027 (giriş alt zirvesi için) | İşin alt zirvesine göre (§9.2); giriş için ~25 Ocak |
| Ramazan Bayramı | 9–11 Mart 2027 [HABER]; arefe 8 Mart | Son 10 gün alt zirvesi için ~8–15 Şubat | ~15 Şubat (§9.2) |
| Anneler Günü + Kurban Bayramı | 9 Mayıs + 16–19 Mayıs 2027 [HABER]; iki dönem üst üste | 18–25 Nisan | ~25 Nisan |

- **En geç yayın** = zirveden 2–3 hafta önce. Meta kampanyaları erken açmayı öneriyor ki öğrenme zirveden önce bitsin; gün sayısı vermiyor [RESMİ]. 2–3 hafta *sezgisel*.
- **Kreatif dondurma** = zirveden ~2 hafta önce yeni kavram denemeyi bırak; sonra yalnız kanıtlanmış reklamların sezon sürümleri [PRATİSYEN Darkroom]. Dondurma **yeni kavramı** durdurur, parti eklemeyi durdurmaz: Modül 04 §8.2'deki 1–2 haftalık ritim, kanıtlanmış açıların sezon sürümleriyle sürer (*sezgisel*).
- Türkiye'de giyim aramaları Ramazan'dan ~2 hafta önce zirve yaptı (Microsoft Advertising, Bing arama verisi; Meta verisi değil).

### 9.2 Evreler (T = zirve günü; *sezgisel* sentez: Darkroom ve Meta 2026 tatil rehberi)

| Evre | Zaman | Yap |
|---|---|---|
| Kurulum | T − 8…6 hafta | Yapıyı kilitle, ölçümü kontrol et, kitle listelerini güncelle |
| Öğren ve test et | T − 6…3 hafta | Kreatif testleri; indirim kararı ~T − 3 hafta. Ekim, Q4'te yeni kitleye ulaşmanın görece ucuz kısmı [PRATİSYEN] |
| Dondurma | T − 2 hafta | Yeni kavram yok; sezon sürümleri ve bütçe planı hazır |
| Zirve | T − 7 gün … T + 3 gün | Plana göre bütçe artışı; günlük kontrol önceden yazılmış sınırlarla |
| Çıkış | T + 3 … 14 gün | Bütçeyi kademeli düşür. Aralık sonu–ocak başı görece ucuz; yeni müşteri için kullan [PRATİSYEN] |

**Üst üste gelen zirveler (*sezgisel*):** İki zirve arasında **3 haftadan az** varsa tek sezon say. 11.11 ile Kara Cuma arasında 16 gün var, yani **Kasım tek sezondur.** 11.11'in çıkış evresi uygulanmaz. 11.11 zirve artışı biter bitmez (15 Kasım) bütçe, sezon planındaki taban seviyesine iner ve Kara Cuma zirve evresine (20 Kasım) kadar orada kalır. Çıkış evresi yalnız son zirveden sonra (Siber Pazartesi, 30 Kasım) başlar. Kurban Bayramı ile Anneler Günü (7 gün ara) de aynı kurala girer.

**Ramazan alt evreleri (2027; *sezgisel*, Türkiye'ye özel Meta verisi yok):** Ramazan tek günlük zirve değil, ~4 haftalık bir dönem. Her alt zirvenin kendi T'si var. Dondurma **yalnız işin bağlı olduğu alt zirveden önceki 2 haftaya** uygulanır; diğer haftalarda normal parti ritmi sürer.

| Alt evre | Tarih (2027) | Kimin için zirve | Dondurma |
|---|---|---|---|
| Giriş öncesi ve ilk hafta | ~25 Ocak – 14 Şubat (T ≈ 8 Şubat) | Gıda, market, ev ve iftar ürünleri. Microsoft'un 2025 Bing verisine göre Türkiye'de giyim aramaları da Ramazan başlangıcından ~2 hafta önce zirve yaptı [PRATİSYEN Microsoft; Meta verisi değil]; giyimde bu alt zirve için T ≈ 25 Ocak alınırsa dondurma ~11 Ocak'ta başlar | ~25 Ocak – 8 Şubat |
| Orta | ~15 – 26 Şubat | Çoğu iş için ara dönem. Aynı veride finans aramaları Ramazan ortasında zirve yaptı [PRATİSYEN Microsoft] | Giriş işinde yok. Son 10 gün işinde dondurma 15 Şubat'ta başlar; bayramlık kavramlarının testi o güne kadar biter |
| Son 10 gün ve bayramlık | ~27 Şubat – 7 Mart (T ≈ 1 Mart) | Giyim (bayramlık), hediye, tatlı ve çikolata | ~15 Şubat – 1 Mart |
| Arefe ve bayram | 8 Mart; 9–11 Mart | Son dakika alışverişi, mağaza ve hizmet | Yok; son kargo kuralı geçerli (aşağıda) |

İşin hangi alt zirveye bağlı olduğunu brifte yaz (Modül 01). Hem giriş hem son 10 gün önemliyse iki dondurma arasında (9–14 Şubat) en az bir yeni parti ekle.

**Son kargo tarihi kuralı [MEVZUAT + *sezgisel*]:** "Bayrama yetiştir", "Yılbaşından önce kapında" gibi teslim vaadi içeren reklamlar için son sipariş tarihini önceden hesapla: zirve günü − (taşıyıcının en uzun teslim süresi + hazırlık süresi + 1 gün tampon). Bu tarih geçince vaadi içeren reklamları kapat ya da vaatsiz sürüme geç; değişikliği günlüğe `SEZON` olarak yaz. Dayanak: ön bilgilendirmedeki teslim süresi, reklamda taahhüt edilen süreyle uyumlu olmalı (Mesafeli Sözleşmeler Yönetmeliği md. 5/1-f); tutmayan teslim vaadi yanıltıcı reklam riskidir (Modül 09 §4 A8–A9).

### 9.3 Sezon içi kurallar

- **BGBM artışı beklenir, CPA her zaman kötüleşmez.** ABD ağırlıklı bir veri setinde Kara Cuma haftası BGBM'si eylülün %59, Kara Cuma günü %84 üstündeydi; tıklama başına dönüşüm oranı +%48, satın alma başı maliyet −%20 oldu [PRATİSYEN Lebesgue]. Türkiye'ye özel sezon BGBM verisi bulunamadı. Hesabın geçen yılki aynı haftasını taban al.
- **"BGBM neden arttı?"** sorusunu **Açık Artırma Rekabeti Değişimi** ölçümüyle cevapla: günün rekabetçi teklifini önceki 3 günün ortalamasıyla kıyaslar; %20'nin üstü anlamlı değişimdir [RESMİ].
- **Yıllık kıyas tuzağı:** Meta tıklama ilişkilendirmesini 3 Mart 2026'da değiştirdi [RESMİ]. Kasım 2025 tamamen, Ramazan 2026 (≈19 Şubat–19 Mart) kısmen eski tanımla ölçüldü. Geçen yılla kıyası sipariş paneliyle yap (ölçüm kırılma tarihleri: Modül 05 §5.3).
- **Bütçe artışını önceden planla.** Bütçe planlama (yüksek talep dönemleri) yalnız günlük bütçede çalışır; öğe başına en fazla 50 dönem, dönem en az 3 saat, dönem toplamı günlük bütçenin 8 katını aşamaz [RESMİ, geliştirici belgesi]. Arayüzde görünürlüğü ve öğrenmeye etkisi *doğrulanmalı*. Yoksa artışı zirveden 3–5 gün önce §6.2 adımlarıyla başlat (*sezgisel*).
- **Zirvede 7 gün bekleyemezsin.** Sınırları önceden yaz. Örnek: "Günlük panel ROAS iki gün üst üste başabaşın altındaysa bir önceki bütçeye dön." Zirvede reklamı yalnız Aşama 0–1 sebepleriyle ve son kargo tarihi kuralıyla (§9.2) kapat (*sezgisel*).
- **Süreli setler:** Başlangıç ve bitiş tarihi önceden girilmiş "kampanya dönemi" setleri kur [PRATİSYEN CTC]. 7 günden kısa sette öğrenmenin bitmesini bekleme.
- **Ramazan:** İftar sonrasına saat kısıtı koyma; saat planı bütçenin harcanma biçimini değiştirir [RESMİ]. O saatlere uygun kreatif ve teklif kullan. Meta'nın 2022 Türkiye araştırmasına göre kullanıcılar Ramazan'da önce bilgilendirici içerik, sonra kampanya bekliyor [HABER; eski veri].
- **Sezon sonrası:** Toplam bütçe düşürülürken yeni tutar "şimdiye kadar harcanan + son 2 günün harcamasının %10'u"ndan az olamaz [RESMİ]. Tek seferde değil kademeli in (*sezgisel*).

**Doldurulmuş örnek: 19 Ekim 2026'da yayına giren e-ticaret hesabı (*varsayım*; gün 0 = 19 Ekim):**
- **19–25 Ekim (gün 0–6):** §4.1 planı; 11.11 sürümleri hazırlanır.
- **26 Ekim (gün 7, pazartesi değişiklik günü):** 11.11 partisi aynı sete eklenir, çünkü dondurma 28 Ekim. Bu takvimden doğan bir eylemdir, performans kararı değil. Kapı hacim yolundan açık: ilk hafta 52 satış, CPA 202 ≤ hedef 250 (§6.1). İki seçenek var: (a) partiyle aynı oturumda +%25 bütçe adımı, 7 günlük sayaç ikisi için birlikte başlar; (b) adımı bir sonraki değişiklik gününe bırakmak. Bu örnek (b)'yi seçiyor, çünkü gün 7'de son 48–72 saatin satışları eksik ve partinin 11.11 öncesi etkisi ayrı okunmak isteniyor. 28 Ekim öğleden sonra ve 29 Ekim resmî tatil; o günlerin verisini not düşerek yorumla.
- **2 Kasım (gün 14):** Partinin üzerinden 7 gün geçti; kapı açıksa ilk bütçe adımı (§6.2). Hedefli teklife geçme: veri az ve sezon başlıyor.
- **4–14 Kasım:** 11.11 zirve evresi; 11 Kasım'da plana göre artış, sınırlar devrede. **13 Kasım:** Kara Cuma için yeni kavram dondurması; parti ritmi sezon sürümleriyle sürer. **15–19 Kasım:** Kasım tek sezon (§9.2), çıkış yok; bütçe sezon tabanında.
- **20–30 Kasım:** İkinci zirve. Teslim vaadi içeren reklamlar son kargo tarihinde kapanır (§9.2). 1 Aralık'tan itibaren kademeli düşüş.
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
| 4 | 2.11.2026 10:30 | YZ | aynı set | Bütçe 1.500 → 1.875 (+%25) | Büyüklüğe göre | Ölçek adımı 1 | Marjinal CPA ≤ 250 | 5.11 / 9.11 | Marjinal ≈ 239; 5. satırla aynı hafta, etkiler ayrılamaz | +%25 |
| 5 | 3.11.2026 09:00 | Müşteri | Site | Ücretsiz kargo eşiği 750 → 500 TL | Hayır (reklam dışı) | MUSTERI | Dönüşüm oranı ↑ | 10.11 | — | — |

**Hipotez defteri** ayrı sekmede: ID, hipotez cümlesi, araç, tarih aralığı, hücreler, sonuç, güven, karar, öğrenilen ders. Kaybeden testleri de yaz; aynı açıyı yeniden denememek için gerekir.

---

## 11. Haftalık kontrol listesi (pazartesi)

- [ ] Önceki tam hafta kampanya/set düzeyinde hedefe ve başabaşa göre okundu (§4.4).
- [ ] Son bütçe adımından beri marjinal CPA hesaplandı (F7).
- [ ] Yayın sütunu: öğrenme, öğrenmeyle sınırlı, kreatif sınırlandı ya da yorgunluğu.
- [ ] Kapatma adayları §5.2 ve §8 listesinden geçti; ölçekleme adayları §6.1 kapısından.
- [ ] Açık testlere erken müdahale edilmedi. Hücre başına 50'den az olayla biten testler "yön göstergesi" diye kaydedildi (§1).
- [ ] Mesaj ve form işlerinde karar CRM'deki satış başı maliyetle ve nitelik oranıyla verildi (§6.5).
- [ ] Önümüzdeki 3 haftada sezon zirvesi var mı (§9.1)? Teslim vaadi içeren reklamların son kargo tarihi yazılı mı (§9.2)?
- [ ] Değişiklikler tek oturumda yapıldı ve günlüğe yazıldı.

---

## Doğrulanması gerekenler

- **Kreatif testi aracı:** Resmi yardım sayfası yok. 5/10 hücre sınırı, %20 bütçe önerisi, 7 gün varsayılan ve yalnız "En yüksek hacim" koşulu pratisyen gözlemi.
- **"Yayını bu reklama it":** Haziran 2026'da görülen test özelliği; hesaba göre değişebilir.
- **Artımlı ilişkilendirme sütunu:** Her hesapta görünüp görünmediği belirsiz; 1 Nisan 2025 öncesi için veri yok.
- **Ramazan alt evreleri ve son 10 gün zirvesi:** Türkiye'ye özel Meta verisi yok; tarihler *sezgisel*. Hesabın geçen yılki Ramazan verisiyle (Modül 05 §5.3 kırılma tarihlerine dikkat) doğrula.
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
- Advantage+ satış kampanyaları: https://www.facebook.com/business/help/1362234537597370

**Meta, diğer [RESMİ]:**
- Kreatif testleri (split testing), Marketing API: https://developers.facebook.com/docs/marketing-api/guides/split-testing
- Yüksek talep dönemleri (bütçe planlama): https://developers.facebook.com/documentation/ads-commerce/marketing-api/reference/high-demand-period
- Bütçe planları: https://developers.facebook.com/documentation/ads-commerce/marketing-api/reference/ad-campaign/budget_schedules
- Reklam hesabı etkinlik kaydı: https://developers.facebook.com/docs/marketing-api/reference/ad-account/activities/ ve https://developers.facebook.com/docs/marketing-api/reference/ad-activity/
- 2026 tatil rehberi (11.06.2026): https://www.facebook.com/business/news/2026-winning-hearts-boosting-carts
- Tıklama ilişkilendirmesi değişikliği (03.03.2026): https://www.facebook.com/business/news/click-attribution
- Kreatif çeşitliliği ve Andromeda: https://www.facebook.com/business/news/the-creative-advantage-unlocking-the-power-of-diversification-with-meta-andromeda

**Mevzuat [MEVZUAT]** (ayrıntı Modül 09):
- Mesafeli Sözleşmeler Yönetmeliği, konsolide metin (md. 5/1-f teslim süresi; md. 12/5 iade masrafı, değişiklik RG 24.5.2025/32909): https://www.mevzuat.gov.tr/MevzuatMetin/yonetmelik/7.5.20237.pdf
- Ticari Reklam ve Haksız Ticari Uygulamalar Yönetmeliği, konsolide metin (md. 7/7, Ek A-9): https://www.mevzuat.gov.tr/File/GeneratePdf?mevzuatNo=20435&mevzuatTur=KurumVeKurulusYonetmeligi&mevzuatTertip=5

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

---

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

---

# Sektör oyun kitapları

> **Modül 08 · Profesyonel paket · Hazırlanma: 17 Ekim 2026 (kaynaklara erişim 17–20 Ekim 2026; denetim düzeltmeleri 20 Ekim 2026).** Ekran adımları burada yok. Onlar için **Kurulum Kılavuzu** (`../KILAVUZ.md`): dönüşüm konumu §5.1, bütçe ve esneklik §5.2, konum ücreti ve KDV §3.6 ve §9, reddedilen reklam §6.2, güvenlik §3.1 ve §11. **Hukuki tavsiye değildir.** Kırmızı ve sarı kapılı işlerde yayından önce insan onayı al.

## Bu modülde

- **Mevzuat kapısı tablosu:** her sektör için kırmızı (dur), sarı (koşullu) ya da yeşil karar ve hiç reklam verilmeyecek sektörler listesi.
- Tüm sektörlerde kullanılan **dönüşüm konumu kuralları** ve **başlangıç bütçe formülleri** (B1–B5).
- **10 sektör kartı.** Her kartta hedef, amaç ve dönüşüm konumu, teklif, kreatif açıları, hedefleme, takip, bütçe mantığı (doldurulmuş örnekle), hukuk/politika uyarısı, sık hata ve örnek Türkçe reklam metni var.
- Yayın öncesi sektör kontrol listesi ve hesapta doğrulanması gereken özellikler.

**Etiketler:** **[RESMİ]** Meta/WhatsApp sayfası. **[MEVZUAT]** Türk kanunu, yönetmeliği, genelgesi; yabancı ülke kuralında ülke adıyla birlikte yazılır. **[HABER]** basın ve veri raporu. **[PRATİSYEN]** ajans ya da araç sağlayıcı (çıkar çatışması olabilir). *sezgisel* = ölçülmemiş kural ya da bu modülün çıkarımı. *varsayım* = örnek hesapta uydurulmuş girdi; gerçek hesapta müşterinin kendi verisini koy. Meta'nın "%X daha iyi" rakamları Meta'nın kendi deneyleridir; yön gösterir, garanti değildir.

---

## 0. Kullanım sırası

1. Sektörü bul ve §1.1'deki **kapıya** bak. Sektör tabloda yoksa ya da birden fazla sınıfa giriyorsa Modül 09 §1 risk kapısı geçerlidir. Kırmızıysa dur, insana ya da hukukçuya yükselt. Sarıysa karttaki koşulları tek tek işaretle.
2. Kartta önerilen **dönüşüm konumu** hesapta var mı, kontrol et. Kademeli açılan özellik yoksa karttaki yedek yolu kullan.
3. Bütçeyi §1.4 formülleriyle müşterinin **kendi** oranlarından hesapla. Örneklerdeki sayılar *varsayımdır*.
4. Kreatifi ve metni karttan al. Modül 04 §11 ve §12 kontrol listesinden geçir.
5. Takibi kur (Modül 05 §7 WhatsApp, §8 form). Takip yoksa yayına çıkma; sistem yanlış kişiye optimize eder.

---

## 1. Ortak kurallar

### 1.1 Mevzuat kapısı

**Önce bu kural:** Sektör bu tabloda yoksa ya da birden fazla sınıfa giriyorsa (ör. "diyetisyenli spor salonu", "terapi koçluğu") **Modül 09 §1 risk kapısı** geçerlidir. İki kararın **sıkı olanını** uygula. Bu tablo yalnız kartı olan sektörlerin özetidir; tam liste Modül 09 §1'dedir.

| Sektör | Kapı | Koşul (özet) | Dayanak |
|---|---|---|---|
| Restoran / kafe (menüde alkol olsa da) | **Yeşil** | Görselde, metinde, katalogda ve karşılama mesajında alkol, içki markası ve "happy hour" yok. Bu unsurlardan biri reklama girerse reklam yayınlanamaz (§1.2) | [RESMİ] Meta alkol ilkesi; [MEVZUAT] 4250 m.6; Modül 09 §1 |
| Kuaför / güzellik salonu | **Sarı** | Tıbbi işlem tanıtılmaz; kozmetik işlem 18+ | [MEVZUAT] sağlık tanıtım yönetmeliği m.12/1-d; [RESMİ] sağlık ilkesi |
| Diş / estetik klinik, Türkiye'deki hasta | **Kırmızı** | Sponsorlu reklam yasak. İstisna: açılıştan sonraki ilk ay, yalnız bilgilendirme | [MEVZUAT] RG 12.11.2025 yönetmelik m.5 |
| Sağlık turizmi (yetki belgeli) | **Sarı** | Türkçe dışı dil, Türkiye hedeflenmez, otomatik kitle kapalı, HealthTürkiye logosu. Hedef ülkenin sağlık reklamı ve veri koruma kuralı da geçerli; ülke seti başına insan onayı (§2.3) | Aynı yönetmelik m.8; hedef ülke mevzuatı |
| Emlak ilanı | **Sarı** | EİDS doğrulamalı ilan bağlantısı + fiyat; yurt dışı alıcıda konut kategorisi | [MEVZUAT/HABER] EİDS; [RESMİ] Özel reklam kategorisi |
| MEB'e bağlı kurs | **Sarı** | Öğrenci resmi ve bilgisi yok, yanıltıcı vaat yok | [MEVZUAT] 5580 m.7 |
| Online kurs, koçluk, B2B | **Yeşil**, istisnalar hariç | Genel reklam kuralları. Online kursta gelir/kazanç vaadi ve belgesiz sertifika iddiası yok (§2.5). **İstisnalar:** avukat ve hukuk bürosu → **kırmızı** (Avukatlık Kanunu m.55). Sağlık meslek mensubu (hekim, diş hekimi, diyetisyen, klinik psikolog, fizyoterapist, eczacı, optisyen vb.) ya da psikolojik destek, diyet veya tedavi çağrıştıran koçluk → **kırmızı**, Modül 09 §1 ve §5.5. Mali müşavir ve benzeri meslekler → meslek reklam kuralı araştırılmadı; yayından önce hukukçuya doğrulat (**sarı**) | [MEVZUAT] TRHTUY m.7; 1136 m.55; 1219 ek m.13; Modül 09 §1 |
| E-ticaret: moda **yeşil**, kozmetik **sarı** | — | Kozmetikte sağlık beyanı yok | [MEVZUAT] Sağlık Beyanı Denetimi Yönetmeliği |
| Ev hizmetleri **yeşil**, nakliyat **sarı** | — | Nakliyatta K3 yetki belgesi | [MEVZUAT] Karayolu Taşıma Yönetmeliği |
| Otel, pansiyon, acenta | **Sarı** | Belge numarası, alkol görseli yok | [MEVZUAT] KTB Genelgesi 2026/4 |
| Spor salonu | **Sarı** | Beden utandıran dil yok, kilo verme 18+ | [RESMİ] sağlık ilkesi |

**Her sektörde geçerli** [MEVZUAT]:
- **İndirim:** Önceki fiyat, aynı satış kanalında indirimden önceki **10 günün en düşük fiyatı**dır. Hizmette ise bir önceki fiyattır. Başlangıç ve bitiş tarihi yazılır, stok sınırlıysa adet de yazılır. 2022–2025 kaynaklarındaki "30 gün" kuralı 1.8.2026'dan beri geçersiz.
- **Yorum:** Reklamda yalnız satın alması doğrulanmış yorum kullanılır.
- **Etkileyici içeriği:** "Reklam" ya da "Tanıtım" ibaresi ve reklamverenin adı görünür olmalı.
- **Yapay zekâ:** İnsandan ayırt edilemeyen yapay zekâ figürü kullanılıyorsa bu açıkça belirtilir.
- **Ceza:** İnternetteki ihlalde 2026 aralığı 1.083.706–10.837.065 TL. Reklamı sonradan düzeltmek sorumluluğu kaldırmaz.
- **Meta'nın onayı hukuki uygunluk kanıtı değildir.** Meta'nın kendi kuralı da yasaya uymayı şart koşar.

### 1.2 Kart dışı: reklam verme

İlk üç satır Modül 09 §1'deki **"kırmızı, istisnasız"** listenin aynısıdır. İki modül ayrışırsa Modül 09 §1 esas alınır.

| Konu | Karar | Dayanak |
|---|---|---|
| Alkollü içki (bar, meyhane, içki satışı); tütün, nargile ve nargile salonu, e-sigara, vape (nikotinsiz dahil); beşerî ilaç (reçeteli ya da reçetesiz) | Reklam yok | [RESMİ] Meta alkol ilkesi Türkiye'yi yasak ülke sayıyor; [MEVZUAT] 4250 m.6, TRHTUY m.27/11; Modül 09 §1–§2 |
| Yasa dışı bahis, şans oyunu, kumar; falcı, medyum, astrolog; sohbet, arkadaşlık ve eş bulma hizmeti | Reklam yok | [MEVZUAT] TRHTUY m.27/3, 27/8; Modül 09 §1 |
| Silah, mühimmat, havai fişek, biber gazı, şok cihazı; THC ve CBD ürünleri | Reklam yok. Silah aksesuarı (kılıf, kasa) için insan onayı | [RESMİ] Meta reklam standartları; [MEVZUAT] TRHTUY m.27/9; Modül 09 §2 |
| Türkiye'deki sağlık tesisi ve sağlık meslek mensubu (kartı olmayan meslekler dahil: diyetisyen, klinik psikolog, fizyoterapist, eczacı, optisyen); avukat; lisanslı şans oyunu | **Kırmızı, dar istisna olabilir:** dur, hukukçuya sor. İstisna belgeyle karşılanmadan yayın yok | [MEVZUAT] 1219 ek m.13; SY m.5; 1136 m.55; Modül 09 §1, §5.5 |
| Kripto | SPK izni ve Meta kripto yetkisi yoksa reklam yok | [PRATİSYEN] 7518 sayılı Kanun özeti; [RESMİ] Meta kripto sayfası |
| Kaldıraçlı işlem (forex, CFD), ikili opsiyon | Reklam yok. Meta CFD ve ikili opsiyonu yasaklıyor; forex ürününün CFD sayılıp sayılmadığı ürüne göre değişir (*doğrulanmalı*) | [RESMİ] Modül 09 §2 |
| Kredi, kart, sigorta, yatırım danışmanlığı | **Sarı**, kartı yok. Oran verilirse toplam maliyetin aylık ve yıllık oranı ana vaatte; "garantili getiri" yok. Yayından önce hukukçu | [MEVZUAT] TRHTUY m.25; Modül 09 §1–§2 |
| Takviye edici gıda | **Sarı:** dur, hukukçuya sor. Kilo miktarına ya da oranına atıf ve tanıklık her durumda yasak | [MEVZUAT] Gıda ve takviyede sağlık beyanı yönetmeliği m.5/11 |

### 1.3 Dönüşüm konumu: eğer-ise

- **Eğer** iş yüz yüze hizmetse, fiyat sorusu ya da randevuyla ilerliyorsa → **WhatsApp**. TÜİK 2026'ya göre WhatsApp %90,0 ile en çok kullanılan uygulama [HABER]. Messenger'ı tek hedef yapma: reklam erişimi yetişkinlerin %19,4'ü [HABER, DataReportal].
- **Eğer** hedef olarak Instagram Direct seçilirse → reklam yalnız Instagram'da görünür. WhatsApp seçilirse Facebook, Instagram ve WhatsApp'ta görünebilir [RESMİ].
- **Eğer** iş acilse (tesisat, çilingir) → **Aramalar**, saat planıyla (Modül 02 §9.2). Meta'nın deneyinde yalnız mesai saatinde yayınlanan arama reklamları %25,4 daha fazla arama dönüşümü verdi [RESMİ].
- **Eğer** satış ekibi geri arıyorsa ve hacim gerekiyorsa → **Anında form, "Daha yüksek satın alma niyeti"**. Bu tür yalnız mobil Facebook ve Instagram akışında yayınlanır, masaüstünde görünmez [RESMİ].
- **Eğer** satış ya da rezervasyon sitede tamamlanıyorsa → **İnternet sitesi** + Piksel/CAPI (Modül 05).
- **Eğer** kitle Avrupa ya da Japonya'daysa → bazı mesajlaşma kampanyaları ve ölçümleri kullanılamayabilir [RESMİ]. Önce hesapta dene. Çalışmazsa site ya da form kullan.
- **Form ile WhatsApp rakip değildir.** Mercedes-Benz Türkiye vakasında form + WhatsApp birlikte kullanıldı, CAPI bağlıydı ve bildirilen en iyi sonuç bu kurguda çıktı [RESMİ, marka beyanı, 2022]. Hacim varsa ikisini birlikte test et.

**WhatsApp merdiveni: amaç ve performans hedefi.** Kartlarda "WhatsApp" yazan her kurgu bu merdivenle kurulur. En üstteki uygun basamağı seç. Koşullar Meta'nın sayfalarından [RESMİ], basamak sırası ve amaç seçimi *sezgisel*.

| Basamak | Koşul | Amaç | Performans hedefi |
|---|---|---|---|
| **1. Sinyal yok** | Son 30 günde Meta'ya dönen potansiyel müşteri ya da alışveriş olayı 10'dan az, ya da etiketleme ve CAPI kurulu değil | Potansiyel müşteriler | "Konuşma sayısını en üst düzeye çıkarma" (bu amaçta varsayılan) |
| **2. Lead sinyali var** | Son 30 günde WhatsApp Business uygulamasının hazır etiketleriyle ya da işletme mesajlaşması CAPI'siyle **en az 10 potansiyel müşteri ya da alışveriş olayı** Meta'ya döndü. Sohbet reklamdan başladı ve tıklamadan sonraki 7 gün içinde etiketlendi. Seçenek hesapta görünüyor | Potansiyel müşteriler | "Mesajlaşma yoluyla potansiyel müşteri sayısını en üst seviyeye çıkar" |
| **3. Satış sinyali var** | Satış sohbette kapanıyor (sipariş, ödeme) ve son 30 günde **en az 10 alışveriş olayı** döndü (Modül 05 §7.2) | Satışlar, manuel kurulum | Mesajlaşma yoluyla alışveriş (satın alma) |

- **Eğer** satış sohbette kapanmıyorsa (randevu, keşif, teklif, yer gösterme) → 2. basamakta kal. 3. basamak yalnız sohbette satılan işler içindir.
- **Eğer** basamağın seçeneği hesapta görünmüyorsa → bir alt basamakta kal. Bu hedefler kademeli olarak açılıyor.
- **Eğer** Potansiyel müşteriler ya da Satışlar amacında WhatsApp ya da istenen hedef görünmüyorsa → yedek olarak **Etkileşim** amacını kullan. Meta iki hedefi de Etkileşim amacında sunuyor [RESMİ].
- **Geçiş:** 3. basamakta amaç değiştiği için yeni kampanya aç (*sezgisel*). Hedef değiştirmek öğrenmeyi yeniden başlatır; değişiklikten sonra en az 7 gün bekle (Modül 01 §4.1).

### 1.4 Başlangıç bütçe formülleri

```
B1  p (talep→müşteri oranı) = ulaşılan × randevu/deneme × gelme/kapanış
    (müşterinin kendi kaydından; kayıt yoksa ilk 2 haftada ölç)
B2  Panel başa baş birim maliyet = katkı × p ÷ 1,05
    (vergi numarası yoksa ÷ 1,26; yurt dışı kitlede o ülkenin konum ücreti:
     İngiltere 1,02 · Fransa, İtalya, İspanya 1,03 · Avusturya 1,05)
B3  Panel hedef birim maliyet = B2 × reklam payı   (pay aşağıdaki tablodan; sezgisel)
B4  Öğrenme minimumu (günlük) = beklenen birim maliyet × 50 ÷ 7
B5  Kapasite bütçesi (günlük) = boş kapasite (iş/gün) ÷ p × B3
    Önerilen günlük bütçe = min(B5, müşteri bütçesi)
```

**B3 reklam payı: karar tablosu** (*sezgisel*; ölçülmüş bir eşik değil, kartlar arası tutarlılık için). Tabloyu yukarıdan aşağı oku, **ilk uyan satırı** uygula.

| Sıra | Durum | Hedef nasıl bulunur | Kartlar |
|---|---|---|---|
| 1 | Müşterinin yazılı kâr hedefi ya da hedef CPA'sı var | Pay kullanma. Hedef = (katkı − hedef kâr tutarı) × p ÷ 1,05 (Modül 01 §3.2–§3.3) ya da müşterinin panel hedef CPA'sı × p (Modül 01 §2.3). Sonuç B2'yi geçemez | e-ticaret, B2B |
| 2 | Talebin bir kısmı zaten başka ücretli kanaldan geliyor (otelde OTA, yani online seyahat acentası ve rezervasyon platformu) | Kanal kaydırma ve artımlı talep ayrı hesaplanır, belirsizse düşük olan (§2.8) | otel |
| 3 | Tekrar alım ya da üyelik süresi **ölçülmüş** | Pay yerine Modül 01 §3.5 strateji tablosu (LTV); stratejiyi müşteri seçer | spor salonu; restoranda tekrar sipariş |
| 4 | Uzun döngü (haftalar–aylar) ya da kapanış oranı oynak ya da az veriyle ölçülmüş | Pay **0,2–0,3**. Döngü 30 günden uzunsa ya da p < 0,05 ise 0,2; değilse 0,3 | emlak 0,2; eğitim 0,3 |
| 5 | Kısa döngü (sonuç günler içinde) ve p müşterinin kaydından ölçülmüş | Pay **0,5**: reklam katkının en fazla yarısını alır (Modül 01 §3.6) | restoran, kuaför, ev hizmetleri, otelde artımlı talep |

- **Dayanaklar:** B2'deki konum ücreti oranları [RESMİ] Meta sayfasından. B4, Meta'nın "haftada yaklaşık 50 sonuç" eşiğinden türetilmiş bir pratisyen kuralıdır. Formüllerin açıklaması Modül 01 §2.3, §4.2, §4.3 ve Modül 02 §1'de.
- **Eğer** B4 önerilen bütçeyi aşıyorsa → tek kampanya, tek set aç ve "Öğrenmeyle sınırlı" durumunu kabul et. Bu durum ceza değildir [RESMİ]. Ya da daha sık gerçekleşen bir olaya in (Modül 02 §2.2).
- **Süre:** Geçmiş veri yoksa en az 2 hafta taban çizgisi oluştur [RESMİ]. Mesaj reklamları en az 7 gün yayında kalmalı [RESMİ].
- **Dış kıyas yok:** Türkiye için yöntemi açık, sektör bazında bir CPL/CPM kıyası bulunamadı. Türk ajans tabloları yöntemsizdir. ABD'deki LocaliQ 2026 potansiyel müşteri tablosu yalnız **sıralama** fikri verir: diş 61,56 $ ve güzellik 50,91 $ en pahalı uçta, spor 15,49 $ ve emlak 13,74 $ en ucuz uçta [PRATİSYEN]. Mutlak değeri Türkiye'ye taşıma.

### 1.5 WhatsApp ve form: ortak ayarlar

- [ ] **Karşılama mesajı ve 3 önerilen soru.** Her teklif reklamına ayrı karşılama metni yaz ve ürün ya da teklif kodunu ekle. Bir Türk reklamveren, tek sette 10 ürün varken gelen mesajın hangi ürün için olduğunu anlayamadığını yazmış [TOPLULUK].
- [ ] **Hazır yanıtlar:** adres ve konum, saatler, fiyat aralığı, servis bölgesi, ödeme. **Uzakta mesajı:** işletme profilinde çalışma saatleri tanımlı olmalı [RESMİ, yalnız arama özeti].
- [ ] **Etiketler:** karttaki aşamalar. Satış ya da potansiyel müşteri sinyali Meta'ya dönmeli (Modül 05 §7). Bunun karşılığı: Meta'nın deneyinde WhatsApp'ta potansiyel müşteri optimizasyonu, konuşma optimizasyonuna göre ortalama %24 daha düşük CPL verdi. Satın alma optimizasyonu ise ortalama %10 daha düşük satın alma maliyeti verdi [RESMİ]. Bu seçenekler kademeli açılıyor; hangi basamakta kalınacağı §1.3 WhatsApp merdiveninde.
- [ ] **API kullanılıyorsa:** reklamdan gelen sohbete 24 saat içinde yanıt verilirse 72 saatlik ücretsiz pencere açılır [RESMİ].
- [ ] **Tanıtım mesajı ticari elektronik iletidir:** önceden onay ve İYS kaydı gerekir. Ret gelirse gönderim 3 iş günü içinde durur [MEVZUAT]. Kişinin kendi sorusuna yanıt vermek ise ayrı bir durumdur (*yorum*).
- [ ] **Formda yasak sorular:** sağlık, gelir, borç, kredi puanı, TC kimlik dahil kimlik numaraları, çocuğun doğum tarihi, din, etnik köken, cinsel yaşam, siyasi görüş, sendika, sabıka. Bunlardan biri varsa reklam yayınlanmaz [RESMİ].
- [ ] **"Koşullu cevaplar"** soruları değil, seçenekleri değiştirir. Herkes aynı soruları aynı sırada görür [RESMİ].
- [ ] **Form metni:** önce KVKK aydınlatma bağlantısı (kutusuz). Sonra SMS, e-posta ve arama için ayrı, isteğe bağlı ve işaretsiz onay kutuları (form metninin bu biçimi *sezgisel* uygulama; dayanak 6563 ve KVKK ilke kararı). **[MEVZUAT] zorunlu:** İYS dışında (ör. hızlı formda) alınan onay 3 iş günü içinde İYS'ye kaydedilir; kaydedilmeyen onay geçersizdir (Ticari İletişim ve Ticari Elektronik İletiler Hakkında Yönetmelik m.7/11–12, RG 04.01.2020; Modül 09 §5.7).
- [ ] **Yanıt hızı:** formu ekibin hemen dönebildiği saatlerde yayınla [RESMİ]. Pratisyenlerin "5 dakikada ara" çarpanları kendi içinde çelişiyor (aynı yazıda 21 kat ve 10 kat). Kendi verinle ölç.
- [ ] **Meta Business Agent** (yapay zekâ asistanı) hesapta açıksa randevu alabilir ve potansiyel müşteriyi eleyebilir [RESMİ]. Türkiye ve Türkçe açıkça listelenmiyor. Canlıya almadan önce Türkçe yanıtları test et.

---

## 2. Sektör kartları

### 2.1 Restoran / kafe · Yeşil (menüde alkol olsa da; reklamda alkol unsuru yok)

| Alan | Karar |
|---|---|
| Hedef | Kendi sipariş kanalını büyütmek (yemek platformlarına bağımlılığı azaltmak), hafta içi öğle gibi boş saatleri doldurmak, rezervasyon, yeni şube |
| Amaç → konum | Paket sipariş: **WhatsApp**, §1.3 merdiveni. Sipariş sohbette kapandığı için hedef 3. basamaktır; etiket sinyali birikene kadar 1. basamakta başla. Masa rezervasyonu WhatsApp'tan alınıyorsa merdivenin 2. basamağında kal. Katalog menü olarak kullanılır. Telefonla rezervasyon: **Aramalar**, telefonun açıldığı saatlerde. Yeni şube: Bilinirlik, dar konum. Sipariş sitesi varsa: Satışlar → site |
| Teklif | Hafta içi öğle menüsü; ilk WhatsApp siparişine alkolsüz ikram; doğum günü masası; gel-al farkı. Fiyat kırmak yerine doğrudan kanala ek fayda koy (Modül 04 §5.2) |
| Kreatif açıları | Yemeğin yakın çekimi, hızlı kesmeler, hareket ve göz teması kuran insan (Meta'nın restoran yöneticisinin önerileri [PRATİSYEN]); şefin hazırlayışı; gerçek menü ve fiyat; stok görsel yok |
| Hedefleme | Teslimat yarıçapı ya da semt (Modül 03 §9). Mahalle restoranında "daha fazla kişiye erişin" kutusu kapalı, turistik bölgede açık kalabilir. İlgi alanıyla daraltma yok |
| Takip | Ödenen siparişe tek etiket olarak "Ödendi" ver; "Yeni sipariş" etiketini reklam sohbetlerinde kullanma, ödenmemiş siparişi de satış sayar (Modül 05 §7.2). Son 30 günde en az 10 alışveriş olayı birikince §1.3 merdiveninin 3. basamağına geç |
| Bütçe mantığı | Birim: sohbet. *Varsayım:* sipariş KDV hariç 500 TL, katkı %30 = 150 TL, sohbet→sipariş %40. Panel başa baş sohbet maliyeti = 150 × 0,40 ÷ 1,05 ≈ **57 TL**; hedef (B3, kısa döngü, pay 0,5) ≈ 29 TL. Öğlen boş kapasite 10 paket/gün → 25 sohbet × 29 TL ≈ **715 TL/gün** tavan. Tekrar sipariş oranı ölçülmüşse hedef LTV ile gevşeyebilir (§1.4 B3 tablosu, sıra 3; Modül 01 §3.5) |
| Hukuk / politika | Alkol görseli, kokteyl menüsü, "happy hour" ve içki markası yok; katalogda ve karşılama mesajında da yok. Meta Türkiye'de bu reklamları yayınlamaz; 4250 m.6, 7584 sayılı Kanunla (2026) her mecradaki paylaşıma genişletildi. Reklamdaki fiyat menüdeki fiyatla aynı olmalı. **Günlük değişen içerik** (günün yemeği, günün fiyatı) reklam metnine yazılmaz. Reklam en az 7 gün yayında kalacağı için öteki günlerde yanıltıcı olur (TRHTUY m.7). Bu bilgi karşılama mesajı ve katalogla verilir. Tekrar sipariş duyurusu için onay + İYS |
| Sık hata | Yoğun serviste kimsenin bakmadığı WhatsApp'a reklam vermek; beğeniye optimize etmek; il geneli hedefleme; 10 ürünü tek reklamda toplayıp gelen mesajın neye ait olduğunu bilememek; sabit menüyü "bugünün menüsü" diye yazmak |

**Örnek metin**
- **Birincil:** "Moda'da hafta içi her gün değişen tencere yemekleri [290 TL]'den başlıyor. 11.30–15.00 arası WhatsApp'tan yazın, günün menüsünü hemen gönderelim; gelip alın ya da [2 km] içine getirelim." Fiyat, gerçekten o fiyattan başlayan bir menü her gün varsa yazılır.
- **Başlık:** "Günün menüsü WhatsApp'ta" · **CTA:** WhatsApp mesajı gönder
- **Karşılama:** "Merhaba, bugünün menüsünü göndereyim mi?" · Önerilen sorular: "Bugünün menüsü (Ö1)", "Paket sipariş", "Akşam 4 kişilik masa"

### 2.2 Kuaför / güzellik salonu · Sarı

| Alan | Karar |
|---|---|
| Hedef | Takvimdeki boş saatleri doldurmak, yeni müşteri, gelin ve özel gün paketi, düğün sezonu ve bayram öncesi |
| Amaç → konum | **WhatsApp**, §1.3 merdiveni; randevu işi olduğu için en çok 2. basamağa çıkılır. İkinci hedef Instagram Direct. Instagram'ın yetişkin reklam kitlesinin %46,5'i kadın, Facebook'ta bu oran %34,2 [HABER]. Randevu sistemi isteniyorsa Facebook'ta Randevular (ücretsiz, otomatik hatırlatma) [RESMİ]. Hızlı formdaki "Randevu talebi" sorusu reklamı **yalnız mobil Facebook'ta** yayınlar [RESMİ]; Instagram ağırlıklı kitlede kullanma |
| Teklif | İlk ziyaret paketi; hafta içi sabah fiyatı; prova dahil gelin paketi; işlemden önce yazılı fiyat |
| Kreatif açıları | Rıza alınmış gerçek müşterinin dönüşüm videosu (Meta genel kozmetikte önce-sonraya izin veriyor); süreç Reels'i; stilistin yüzü ve imzası; salon ortamı |
| Hedefleme | Salon çevresinde dar yarıçap ya da semt. 18 yaş altını kitleye katma; katarsan iğne/yarıçap ve detaylı hedefleme kullanılamaz (Modül 03 §9). Kozmetik işlem reklamı 18+ [RESMİ] |
| Takip | Etiketler: "Fiyat sordu → Randevu verildi → Geldi / Gelmedi". Randevu hatırlatması alınan hizmetle ilgilidir, onay istisnasına girer (*yorum*). Randevu verilen sohbete Meta'nın hazır potansiyel müşteri etiketlerinden birini ver; 30 günde 10 olay birikince §1.3 merdiveninin 2. basamağına geç |
| Bütçe mantığı | Birim: sohbet. *Varsayım:* hizmet katkısı 900 TL, sohbet→randevu %40, gelme %80 → p = 0,32. Panel başa baş = 900 × 0,32 ÷ 1,05 ≈ **274 TL**; hedef (B3, kısa döngü, pay 0,5) ≈ 137 TL. Haftada 12 boş koltuk saati → 38 sohbet × 137 TL ≈ 5.200 TL/hafta ≈ **745 TL/gün** |
| Hukuk / politika | Sağlık kuruluşu değilse botoks, dolgu, saç ekimi gibi tıbbi işlemleri tanıtma. Ruhsatsız sağlık hizmeti tanıtanlar Reklam Kurulu'na bildirilir ve suç duyurusunda bulunulur [MEVZUAT]. Lazer epilasyonun salondaki durumu tartışmalı; yazılı ruhsat kontrolü olmadan reklama koyma. "Saç dökülmesinden utanıyor musun?" gibi kişisel özellik kalıbı yok [RESMİ]. Müşteri görseline yazılı rıza |
| Sık hata | İl geneline reklam; fiyat sorusuna saatler sonra yanıt; randevu teyidi ve hatırlatması göndermemek; tıbbi işlemi "güzellik merkezi" adıyla tanıtmak |

**Örnek metin**
- **Birincil:** "Gelin saçı ve makyajı tek pakette: [2] prova dahil, düğün günü salonda ya da mekânda. [Mayıs–Eylül 2027] tarihleri dolmadan gününüzü ayıralım. Paket fiyatı WhatsApp'ta yazılı gelir."
- **Başlık:** "Prova dahil gelin paketi" · **CTA:** WhatsApp mesajı gönder
- **Karşılama:** "Merhaba, düğün tarihinizi yazar mısınız?" · Önerilen sorular: "Gelin paketi fiyatı (G1)", "Prova günü", "Saç boyama randevusu"

### 2.3 Diş / estetik klinik · Türkiye'deki hasta kırmızı, sağlık turizmi sarı

**Karar ağacı** [MEVZUAT, RG 12.11.2025 yönetmelik]:
- **Kitle Türkiye'deyse → ücretli reklam yok.** Tek istisna, sağlık tesisinin açılışından sonraki **ilk bir ay** (ve Bakanlığın onayladığı yeni tıp teknolojisi). Bu durumda bile yazılı hukuki onay al. İçerik adres, saat, uzmanlık, unvan ve koruyucu sağlık bilgisiyle sınırlı kalmalı. **Fiyat, indirim, kampanya, çekiliş, hasta memnuniyeti ve yönlendirme yok** (m.5/1). Hasta görselleri hiçbir koşulda sponsorlu yayınlanamaz (m.7/1-j). Arama motorunda ücretli öne çıkma da yasak (m.5/1-i).
- **Yetki belgeli sağlık turizmiyse → aşağıdaki kart.** Yurt dışına yönelik ayrı hesap ya da site, Türkçe dışı resmî dil, Türkiye hedeflenmez, otomatik hedef kitle tanımlamaları kapalı, HealthTürkiye logosu, yetki belgesi görünür. Bu kapsamda fiyat ve kampanya serbest. Belgelenmiş rızayla hasta hikâyesi ve yorumu yurt dışına yönelik sitede ve hesapta kullanılabilir (m.8/1-ç). **Sponsorlu reklamda hasta görseli ve hasta hikâyesi:** yorum ve beğeni kapatılabildiği doğrulanmadan yayınlanmaz. Yönetmelik hasta görselli paylaşımda etkileşimin kapatılmasını istiyor (m.7/1-ğ) ve bu hüküm m.8/4 yoluyla sağlık turizmine de uygulanabilir (muhafazakâr yorum, Modül 09 §5.5). Türk kuralına ek olarak **hedef ülkenin** sağlık reklamı ve veri koruma hukuku da geçerlidir (aşağıdaki kartta Hukuk satırı).
- **Hiçbiri değilse → dur.** Ceza tesis için bir önceki ayın brüt gelirinin %2'sine kadar (en az 100.000 TL); tekrarında artar [MEVZUAT, 3359 ek m.11].

| Alan | Karar (yalnız sağlık turizmi) |
|---|---|
| Hedef | Yurt dışından ön değerlendirme talebi → video görüşme → tedavi, otel ve transfer rezervasyonu |
| Amaç → konum | Potansiyel müşteriler → **hızlı form (yüksek niyet)** ya da site formu. WhatsApp'a yönlendiren reklamı Avrupa kitlesinde önce test et; çalışmazsa form kullan, ardından WhatsApp'a geç |
| Teklif | Ücretsiz online ön değerlendirme (yalnız yurt dışı hesapta); tedavi, otel ve transferi içeren yazılı paket; garanti koşulları yazılı. "Garantili sonuç" ve "ağrısız" yok (*sezgisel*, Reklam Kurulu denetim örüntüsü) |
| Kreatif açıları | Hekimin kendini tanıttığı video, klinik ve laboratuvar turu, süreç anlatımı. Rızalı hasta hikâyesi yalnız organik yurt dışı hesapta ve sitede; sponsorlu reklamda yukarıdaki etkileşim kuralı doğrulanmadan yok. Önce-sonra görseli ülke bazında doğrulanmalı (Almanya örneği Hukuk satırında). Ameliyat anı ve işlem sırası görüntüsü yok [MEVZUAT; RESMİ şok edici içerik kuralı] |
| Hedefleme | Ülke başına ayrı set, dil ülkeye göre. Konumda Türkiye yok. Advantage+ hedef kitle, detaylı hedefleme genişletmesi, benzer kitle genişletmesi ve "daha fazla kişiye erişin" kapalı; ekran görüntüsüyle belgele. Yönetmelik "otomatik tanımlama"nın Meta'daki karşılığını tanımlamıyor; bu muhafazakâr bir yorum. Kozmetik işlemlerde 18+ |
| Takip | CRM aşamaları (ön değerlendirme → teklif → rezervasyon → tedavi) CAPI ile gönderilir. URL'de ve olaylarda tedavi adı ya da sağlık bilgisi bulunmaz [RESMİ hassas veri kuralı]. Meta sağlık reklamverenlerinde alt huni olaylarını kısıtlayabilir; Türkiye kapsamı belirsiz [PRATİSYEN]. Olay adını değiştirerek kısıtı aşma |
| Bütçe mantığı | Birim: nitelikli lead. *Varsayım:* tedavi katkısı 1.200 €, lead→tedavi %5. Başa baş CPL = 1.200 × 0,05 ÷ 1,02 (İngiltere) ≈ **59 €**. Ülke seti başına öğrenme minimumu: beklenen CPL 30 € ise ≈ 214 €/gün; bütçe yetmiyorsa tek ülkeyle başla. Döngü uzun (bir ajans 24 gün diyor, yöntemsiz [PRATİSYEN]). Kararı 30–60 günlük CRM verisiyle ver (*sezgisel*) |
| Hukuk / politika | **Hedef ülke hukuku da geçerlidir.** Meta reklamın yürürlükteki yasalara uymasını şart koşar (Modül 09 §0); Türk yönetmeliğine uymak yetmez. **Her ülke seti için yerel hukuk kontrolü yapılmadan yayın yok (insan onayı).** Örnekler: Almanya'da tıbben gerekli olmayan estetik operasyonda önce-sonra karşılaştırmasıyla reklam yasak (HWG §11/1 cümle 3 no. 1) [MEVZUAT, Almanya]. Alman Federal Mahkemesi bu yasağı sitede ve Instagram'da önce-sonra görseliyle tanıtılan hyaluron dolgu uygulamasına da uyguladı (BGH, 31.07.2025, I ZR 170/24) [HABER]. İngiltere'de kozmetik müdahale reklamı 18 yaş altına yöneltilemez (CAP Code 12.25, 25.05.2022'den beri) [MEVZUAT, İngiltere öz-düzenleme, ASA]. Önce-sonra ve hasta hikâyesi ülke bazında doğrulanmalı. **Veri:** Formda sağlık bilgisi sorulamaz. "Hangi diş probleminiz var?" yerine "Hangi hizmet hakkında bilgi almak istersiniz?", ülke ve zaman sor. Röntgen ve fotoğraf sağlık verisidir: KVKK'da özel nitelikli, AB'de ve İngiltere'de bulunan kişiler için GDPR/UK GDPR m.9 kapsamında özel kategori veridir. Bu veri yalnız GDPR'a uygun açık rıza metniyle ve güvenli bir kanaldan, reklamdan sonraki rıza adımında istenir. AB/İngiltere temsilcisi (GDPR ve UK GDPR m.27) ve veri işleme dayanağı için hukukçu onayı al. Pazarlama mesajını yalnız onaylı listeye gönder |
| Sık hata | Türkiye kitlesine "kampanyalı implant" reklamı; Türkçe metin ya da Türkiye'yi hariç tutmamak; Advantage+ hedef kitleyi açık bırakmak; hasta yorumunu sponsorlu yayınlamak; ajans rehberlerini güncel sanmak (2026 tarihli bir rehber Kasım 2025 yönetmeliğinden hiç söz etmiyor) |

**Örnek metin**
- **Sağlık turizmi, İngiltere seti (İngilizce zorunlu, Türkçe değil; yayından önce İngiltere hukuk kontrolü):** "Dental treatment in Istanbul, planned before you travel. Request a free online assessment and our dentists reply with one written plan: treatment, hotel and transfers. Results vary from person to person. HealthTürkiye authorised." · **Başlık:** "Free online assessment" · **CTA:** Get quote. Röntgen ve fotoğraf reklamda istenmez; formdan sonra, açık rıza adımı tamamlanınca güvenli kanaldan istenir.
- **Açılış ayı, Türkiye (yalnız hukuki onayla):** "[Klinik adı] Ağız ve Diş Sağlığı Polikliniği [Kadıköy]'de hizmete başladı. Uzmanlık alanları: ortodonti, periodontoloji, pedodonti. Hafta içi 09.00–19.00, cumartesi 10.00–16.00." · **CTA:** Daha fazla bilgi. Fiyat, kampanya ve hasta görseli yok.

### 2.4 Emlak · Sarı

| Alan | Karar |
|---|---|
| Hedef | Alıcı ve kiracı talebi; portföy kazanımı (satmak isteyen mal sahibi); proje lansmanı; yurt dışı alıcı |
| Amaç → konum | **Belirli bir ilanı tanıtan her reklamda EİDS bağlantısı kurguya göre şöyle sağlanır** (Hukuk satırı): **(a) Varsayılan:** dönüşüm konumu **internet sitesi**; hedef URL doğrudan o ilanın EİDS doğrulamalı ilan sayfası. EİDS sayfası ofisin Pikselli kendi sitesindeyse Potansiyel müşteriler (Lead olayı). Sayfa bir ilan portalındaysa Piksel kurulamaz; Trafik → yönlendirme sayfası görüntülemesi kullan ve talebi CRM'den say. **(b) Ekip geri arıyorsa:** Potansiyel müşteriler → **hızlı form (yüksek niyet)**. Bu kurguda reklamın tıklanan bağlantısı forma gider, EİDS sayfasına değil; Instagram'da birincil metindeki URL de tıklanmaz. Bu yüzden EİDS ilan URL'si birincil metinde açıkça yazılır. Formun giriş açıklamasına da yazılır, tamamlama ekranındaki "İnternet sitesini görüntüle" düğmesi de bu URL'ye gider (menü adları ve giriş metnindeki bağlantının tıklanabilirliği hesaba göre değişebilir). (b)'nin yeterliliği **doğrulanmalı**; şüphede (a)'yı kullan. **Bireysel danışman:** **WhatsApp**, §1.3 merdiveni, en çok 2. basamak. Video tur ve konum sohbette paylaşılır; EİDS ilan URL'si karşılama mesajında ve birincil metinde yer alır. **Çok ilanlı ofis:** **gayrimenkul için Advantage+ katalog reklamları**. Katalog zorunlu alanları `home_listing_id, name, image, address, neighborhood, latitude, longitude, price, availability, url`; Piksel gerekir; Meta en az 100 ilan öneriyor [RESMİ]. **Katalog kuralı:** Katalogdaki her ilanın `url` alanı o ilanın EİDS doğrulamalı sayfasına gitmeli, `price` alanı dolu olmalı. Bunu sağlayamıyorsan (ör. `url` ofisin EİDS doğrulaması görünmeyen kendi sitesine gidiyorsa) katalog reklamı kullanma (*doğrulanmalı*) |
| Teklif | Ücretsiz değerleme (portföy kazanımı); video tur; yer gösterme randevusu; lansman fiyat listesi |
| Kreatif açıları | Dikey video tur, drone, mahalle ve ulaşım, kat planı, danışmanın yüzü ve yetki belgesi. Görseller yalnız o ilana ait olmalı |
| Hedefleme | Konum + geniş kitle. Türkiye kitlesi için konut özel reklam kategorisi zorunlu değil. Yurt dışı alıcı (AB, İngiltere, İsviçre, Norveç vb.) hedefleniyorsa zorunlu; o zaman yaş, cinsiyet, hariç tutma ve benzer kitle kısıtlanır [RESMİ]. Ayrıştırmayı dil ve kreatifle yap |
| Takip | CRM aşamaları (iletişime geçildi → nitelikli → yer gösterme → satış/kira) Meta'ya gönderilir (Modül 05 §8). "Nitelikli potansiyel müşteri" hedefi CRM CAPI'si ve ayda en az 200 potansiyel müşteri ister [RESMİ] |
| Bütçe mantığı | Birim: lead. *Varsayım:* satış başına ofis katkısı 150.000 TL, lead→satış %3. Panel başa baş CPL = 150.000 × 0,03 ÷ 1,05 ≈ **4.286 TL**. Döngü 30 günden uzun ve p < 0,05 olduğu için reklam payı 0,2 (§1.4 B3 tablosu, sıra 4) → hedef ≈ **857 TL** (*sezgisel*). İlk değerlendirmeyi 60–90 günde yap (*sezgisel*) |
| Hukuk / politika | Belirli bir taşınmazı ilan eden her reklamda **EİDS doğrulamalı ilan bağlantısı ve fiyat** bulunur; görsel ve açıklama yalnız o doğrulanmış ilana ait olur [HABER, Ticaret Bakanlığı]. Bağlantının "reklamda bulunması" kurguya göre farklı sağlanır (Amaç → konum satırı). Ağustos 2026'da kurala aykırı sosyal medya ilanı veren 42 emlak işletmesine işletme başı 100 bin TL ceza verildi; hesaplara erişimin engelleneceği açıklandı. Satılık ilan için mal sahibinin e-Devlet yetkisi 1 Şubat 2026'dan beri zorunlu [HABER]. Formda gelir, borç ve kredi notu sorulamaz; bütçe aralığı, bölge, tip ve zaman sorulabilir. "Garantili değer artışı" yok |
| Sık hata | EİDS bağlantısız ve fiyatsız ilan reklamı; form reklamında EİDS bağlantısının hiçbir yerde görünmemesi (metinde "bağlantı aşağıda" yazıp CTA'yı forma bağlamak); katalogda `url` alanını EİDS sayfası yerine ofis sitesine bağlamak; başka ilanın fotoğrafını kullanmak; yurt dışı kampanyada kategori seçmemek (reklam reddedilir); formu "Daha yüksek hacim"de bırakıp ekibi çöp kayıtla boğmak |

**Örnek metin**
- **(a) Site kurgusu, varsayılan:** "Maltepe Cevizli'de 2+1, 95 m², 4. kat, metroya yürüyerek 6 dakika: [7.450.000 TL]. Fotoğraflar, video tur ve EİDS doğrulamalı ilan bir tık uzağınızda." · **Başlık:** "Cevizli 2+1 · [7.450.000 TL]" · **CTA:** Daha fazla bilgi · **Hedef URL:** [o ilanın EİDS doğrulamalı ilan sayfası]
- **(b) Form kurgusu (yeterliliği doğrulanmalı):** "Maltepe Cevizli'de 2+1, 95 m², 4. kat, metroya yürüyerek 6 dakika: [7.450.000 TL]. EİDS doğrulamalı ilan: [ilan URL'si]. Yer göstermeye gelmek için formu doldurun; aynı gün arıyoruz." · **Başlık:** "Cevizli 2+1 · [7.450.000 TL]" · **CTA:** Daha fazla bilgi · **Form giriş metni:** "Bu ilanın EİDS doğrulamalı sayfası: [ilan URL'si]" · **Tamamlama ekranı:** "İnternet sitesini görüntüle" → [ilan URL'si]
- **Form soruları:** "Satın almayı mı kiralamayı mı düşünüyorsunuz?" (Meta'nın örnek sorusu) · "Ne zaman taşınmayı planlıyorsunuz?" · ilçe → mahalle (koşullu cevap) · bütçe aralığı

### 2.5 Eğitim / kurs · MEB'e bağlı kurum sarı, online kurs yeşil (koşullu; terapi/diyet/sağlık çağrıştıran koçluk kırmızı)

| Alan | Karar |
|---|---|
| Hedef | Dönem kaydı, deneme dersi, seviye tespiti, ücretsiz seminer, online kurs satışı |
| Amaç → konum | Yüz yüze, ekip geri arıyorsa: Potansiyel müşteriler → **hızlı form (yüksek niyet)**; koşullu cevap: program → şube → saat. Veli ya da öğrenci soruyla ilerliyorsa form yerine **WhatsApp**, §1.3 merdiveni, en çok 2. basamak. Online kurs: Satışlar → site + Piksel/CAPI. Seminer: form, ardından hatırlatma |
| Teklif | Ücretsiz deneme dersi ya da seviye görüşmesi; erken kayıt (indirim kuralına uy); taksit varsa toplam fiyat ve taksit sayısı okunur büyüklükte [MEVZUAT] |
| Kreatif açıları | Eğitmenin tanıtımı, ders kesiti, sınıf ortamı, müfredat, şube ve ulaşım. Öğrenci yüzü ve sınav sonucu yok |
| Hedefleme | 18 yaş altını kitleye katma. Okul kurslarında veliyi (18+) hedefle, dar konum kullan. Çocuk olduğu bilinen kişiye profillemeyle hedefli reklam yasak [MEVZUAT, TRHTUY m.25/A]. Sezon: okul dönemi ve sınav takvimi (Modül 06 §9) |
| Takip | Form → aynı gün arama → deneme randevusu → katılım → kayıt. Aşamalar CRM'den gönderilir |
| Bütçe mantığı | Birim: lead. *Varsayım:* dönem katkısı 12.000 TL, lead→deneme %40, deneme→kayıt %35 → p = 0,14. Panel başa baş CPL = 12.000 × 0,14 ÷ 1,05 = **1.600 TL**. Reklam payı **0,3** (§1.4 B3 tablosu, sıra 4): kapanış iki aşamalı (deneme, kayıt) ve oran dönemden döneme oynar. Ama lead'den kayda süre 30 günün altında (*varsayım:* 2–3 hafta) ve p ≥ 0,05; bu yüzden emlaktaki 0,2 değil 0,3 (*sezgisel*) → hedef ≈ 480 TL. Kontenjanda 16 boş yer → 114 lead × 480 ≈ **55.000 TL** kayıt dönemi bütçesi. Kontenjan dolunca bütçeyi kes |
| Hukuk / politika | 5580 m.7: gerçeğe aykırı ya da yanıltıcı reklama ve reklamda öğrenci resmi veya bilgisi kullanan kuruma idari para cezası [MEVZUAT]. Derece ya da sonuç garantisi yok. Formda çocuğun doğum tarihi sorulamaz; "Öğrencinin sınıfı" sor. Akademik unvan yanıltıcı kullanılamaz (TRHTUY, 2026). **Online kurs ve koçluk:** Gelir ya da kazanç garantisi ("ayda X TL kazan") ve "X günde Y sonuç" vaadi yok. Meta, belirsiz ya da az emekle gerçekçi olmayan kazanç vaat eden teklifleri yasaklıyor [RESMİ, Yasaklı ticari uygulamalar]; Türk hukukunda da yanıltıcı reklamdır [MEVZUAT, TRHTUY m.7]. Sertifika, akreditasyon ve "MEB/e-Devlet onaylı" gibi onay iddiaları yalnız belgeyle yazılır. Psikolojik destek, diyet ya da tedavi çağrıştıran koçluk → sağlık meslek mensubu kapısı (§1.1 istisnası, Modül 09 §1) |
| Sık hata | "Derece garantisi" ve başarılı öğrenci fotoğrafı; 13–17 yaşa yarıçaplı hedefleme; lead'i iki gün sonra aramak; online kursta kazanç ekran görüntüsüyle (banka hesabı, satış paneli) reklam; belgesiz "onaylı sertifika" iddiası |

**Örnek metin**
- **Birincil:** "İş toplantısında İngilizce konuşmak için 10 haftalık konuşma grubu: en fazla [8] kişi, haftada 2 akşam, Şişli'de ya da online. Önce [20] dakikalık ücretsiz seviye görüşmesi, sonra size uygun grup."
- **Başlık:** "Ücretsiz seviye görüşmesi" · **CTA:** Kaydol
- **Form:** "Hangi alanda eğitim almak istiyorsunuz?" (Meta'nın örnek sorusu) · şube → saat (koşullu cevap) · tercih edilen arama saati

### 2.6 E-ticaret (moda, kozmetik) · Moda yeşil, kozmetik sarı

| Alan | Karar |
|---|---|
| Hedef | Satın alma ve ROAS, yeni müşteri, sepet ortalaması, stok eritme, sezon açılışı |
| Amaç → konum | Siteli marka: Satışlar → **site**, Advantage+ satış kampanyası + katalog, Piksel + CAPI (Modül 02 §5.4, Modül 05). Sitesiz Instagram butiği: **WhatsApp**, §1.3 merdiveni; satış sohbette kapandığı için hedef 3. basamak. Satılan sohbeti "Ödendi" diye etiketle. Shops reklamlarının Türkiye'de kullanılabilirliği doğrulanamadı |
| Teklif | Ücretsiz kargo eşiği, set, ilk siparişe hediye, kolay değişim. Yasal 14 günlük cayma hakkını ekstra avantaj gibi sunma [MEVZUAT] |
| Kreatif açıları | Ürün üzerinde ya da kullanımda, UGC ("Reklam" ibaresiyle), beden ve ton rehberi, doku yakın çekimi, doğrulanmış alıcı yorumu |
| Hedefleme | Geniş + Advantage+. 2025'te e-ticaret harcamasının %56'sını ve alışveriş adedinin %75'ini kadınlar yaptı; en çok harcayan yaş grubu 25–34 [HABER, Ticaret Bakanlığı raporu]. Bu veriyi kreatif dili seçmek için kullan, kısıt koymak için değil. Reklam alanını Facebook'la sınırlama |
| Takip | Piksel sipariş değerini KDV dahil mi hariç mi gönderiyor, kontrol et (Modül 05). WhatsApp modelinde karşılama ürün kodunu taşımalı |
| Bütçe mantığı | *Varsayım:* KDV dahil 1.200 TL satış (net 1.000 TL), katkı 404 TL (marj %40,4). Panel başa baş CPA = 404 ÷ 1,05 ≈ **385 TL**. Piksel KDV dahil değer gönderiyorsa panelde başa baş ROAS = 1,26 ÷ 0,404 ≈ **3,12**. **Hedef** müşterinin kâr hedefinden gelir (§1.4 B3 tablosu, sıra 1; Modül 01 §3.2–§3.3). *Varsayım:* müşteri net gelirin %10'unu kâr istiyor = 100 TL → panel hedef CPA = (404 − 100) ÷ 1,05 ≈ **290 TL**, panel hedef ROAS = 1,26 ÷ (0,404 − 0,10) ≈ **4,14**. Kâr hedefi yazılı değilse B3 payı 0,5 → ≈ 192 TL. **Öğrenme minimumu:** yeni hesapta beklenen CPA yerine başa baş kullanılır (Modül 01 §4.2): 385 × 50 ÷ 7 ≈ **2.750 TL/gün**; satın almada Meta'nın 10 kat kuralı ≈ 3.850 TL/gün daha yüksektir (Modül 01 §4.1–§4.2). Bu bütçe yoksa satın almada kal ve Modül 01 §4.4 karar tablosunu uygula. Sığ olaya (önce ödeme başlatma, sonra sepete ekleme) yalnız oradaki koşullar oluşunca in. İki hafta sonra satın alma başına maliyeti karşılaştır (Modül 02 §2.2) |
| Hukuk / politika | İndirimde 10 gün ve kanal kuralı (§1.1). Kozmetikte "sivilceyi tedavi eder", "dökülmeyi durdurur" gibi sağlık beyanı yok [MEVZUAT]. "Doğal" ve "çevre dostu" gibi ifadeler açıklama ve belge ister. Sitede mesafeli satış ön bilgilendirmesi bulunmalı |
| Sık hata | WhatsApp satışını Meta'ya bildirmemek; bu durumda sistem mesaj atana optimize etmeye devam eder. İndirimden hemen önce fiyatı şişirmek. Doğrulanmamış Instagram yorumunu reklamda kullanmak |

**Örnek metin**
- **Birincil:** "Kış için parfümsüz yoğun nemlendirici, [50 ml]. İçerik listesi ürün sayfasında. 1–30 Kasım arası [449 TL] (indirim öncesi [529 TL]), [500] adetle sınırlı. [2] iş günü içinde kargoda."
- **Başlık:** "Parfümsüz nemlendirici [449 TL]" · **Açıklama:** "30 Kasım'a kadar" · **CTA:** Şimdi alışveriş yap
- **Butik (WhatsApp) karşılama:** "Merhaba, [K-214] kodlu ceket için beden ve stok bilgisini hemen yazıyorum."
- **Türkiye vakası:** Modanisa'da WhatsApp botuyla konuşan müşterilerin %55'i ilk siparişini WhatsApp'tan verdi; Türkiye'deki siparişlerin yaklaşık %3'ü WhatsApp'a atfedildi [RESMİ, marka beyanı, tarihsiz].

### 2.7 Yerel ev hizmetleri (tesisat, temizlik, nakliyat) · Yeşil, nakliyat sarı

| Alan | Karar |
|---|---|
| Hedef | Acil iş (su kaçağı, tıkanıklık, çilingir); planlı iş (temizlik, boya, taşınma keşfi); düzenli sözleşmeli iş |
| Amaç → konum | Acil iş: **Aramalar**, yalnız telefonun açıldığı saatlerde. Planlı iş, fiyat fotoğrafla veriliyorsa: **WhatsApp**, §1.3 merdiveni, en çok 2. basamak (keşif ve iş sohbet dışında kapanır). Ekip geri arıyor ya da keşfe gidiyorsa: **hızlı form**. Formda koşullu cevap kullan: hizmet → oda sayısı; nakliyatta çıkış ilçesi → varış ilçesi, kat ve tarih. Instagram ve Messenger'daki "Otomatik sohbet" şablonu Meta'nın ev yenileme örneğine uyar ama WhatsApp'ta yok [RESMİ]. Hesap yapısı: Modül 02 §5.5 Şablon D |
| Teklif | Ücretsiz keşif; sabit fiyatlı temel paket ("2+1 temizlik"); düzenli temizlik aboneliği |
| Kreatif açıları | Ustanın ya da ekibin yüzü, araç ve ekipman, önce-sonra (temizlik, boya), yetki belgesi |
| Hedefleme | Yalnız hizmet verilen ilçeler; "daha fazla kişiye erişin" kapalı. Meta artık "yalnız orada yaşayanlar" seçeneği sunmuyor; bölgedeki ziyaretçiler de reklamı görebilir (Modül 03 §9) |
| Takip | Etiketler: "Keşif → Fiyat verildi → İş alındı". Bunlar özel etiketlerdir; §1.3 merdiveni için talebi Meta'nın hazır potansiyel müşteri etiketlerinden biriyle de işaretle (Modül 05 §7). Aramaların sonucunu elle kaydet (Modül 05 §9) |
| Bütçe mantığı | Temizlik örneği Modül 01 §3.6 ve §4.3'te: panel başa baş sohbet ≈ 267 TL, hedef ≈ 133 TL, kapasite bütçesi ≈ **622 TL/gün**. Acil tesisat *varsayımı:* iş katkısı 1.200 TL, arama→iş %50 → panel başa baş arama = 1.200 × 0,50 ÷ 1,05 ≈ **571 TL**; hedef (B3, kısa döngü, pay 0,5) ≈ **286 TL**. Arama seti öğrenmeyle sınırlı kalabilir; aramalar değerliyse bunu kabul et |
| Hukuk / politika | Evden eve nakliyat K3 yetki belgesi ister [MEVZUAT]. Belge numarasını reklamda göstermek yasal zorunluluk olarak bulunamadı; güven için göster (*sezgisel*). "Sigortalı" ve "garantili" ifadeleri belgeye dayanmalı. Kampanya SMS'i ya da WhatsApp mesajı için onay + İYS |
| Sık hata | Telefonun açılmadığı saatte arama reklamı; il geneli hedefleme; "Daha yüksek hacim" formuyla çöp kayıt; fiyat aralığını mesajda hiç vermemek (*sezgisel*). Formda 3–4 alan ve tek eleme sorusu yeterli [PRATİSYEN]. Satıcının "her ek alan dönüşümü %10–15 düşürür" iddiasının yöntemi yok |

**Örnek metin**
- **Nakliyat (form):** "Evden eve taşımada keşif ücretsiz: ekibimiz eşyanızı yerinde görüp yazılı fiyat veriyor. K3 yetki belgeli, ambalaj malzemesi dahil. Anadolu yakasında Kasım'ın hafta içi günleri müsait." · **Başlık:** "Ücretsiz keşif, yazılı fiyat" · **CTA:** Teklif al
- **Acil tesisat (arama):** "Üsküdar ve Ümraniye'de su kaçağı ve tıkanıklık: [08.00–22.00] arası arayın, usta [60] dakika içinde yola çıksın. Ücret iş başlamadan söylenir." · **CTA:** Şimdi ara. Süre vaadi yalnız gerçekse yazılır.

### 2.8 Otel / turizm · Sarı

| Alan | Karar |
|---|---|
| Hedef | Doğrudan rezervasyon (OTA'ya, yani online seyahat acentalarına bağımlılığı azaltmak), sezon dışı doluluk, yurt dışı pazar, grup, düğün ve toplantı |
| Amaç → konum | Rezervasyon motoru varsa Satışlar → **site** + Piksel/CAPI. Çok odalı ya da çok tesisli yapıda **otel kataloğuyla seyahat reklamları**: katalog, ürün seti ve sitede seyahat olayları gerekir (API'de `PRODUCT_CATALOG_SALES`) [RESMİ]. Soruyla satan butik otel: yurt içi kitlede **WhatsApp**, §1.3 merdiveni; ön ödeme sohbette alınıyorsa hedef 3. basamak, alınmıyorsa 2. basamak. Avrupa kitlesinde mesaj kampanyası kısıtlı olabilir; site ya da form kullan. Grup ve düğün: yüksek niyet formu (kişi sayısı, tarih, bütçe aralığı) |
| Teklif | Doğrudan rezervasyona ek fayda (kahvaltı, transfer, geç çıkış); erken rezervasyon (indirim kuralına uy); sezon dışı uzun konaklama |
| Kreatif açıları | Oda ve manzaranın dikey videosu, misafir deneyimi, çevre ve rota, yemek (alkolsüz), şeffaf toplam fiyat |
| Hedefleme | Kaynak pazarlar ülke ya da şehir bazında. Dil yalnız yerel olmayan dil için seçilir. "Daha fazla kişiye erişin" turizmde yararlı olabilir. "Seyahat edenler" seçeneği artık yok (Modül 03 §9) |
| Takip | Rezervasyon değeriyle Purchase olayı. WhatsApp rezervasyonlarını "Ödendi" etiketiyle bildir |
| Bütçe mantığı | *Varsayım:* 3 gece × 4.000 TL KDV hariç = 12.000 TL; değişken maliyet %35 → katkı 7.800 TL → panel başa baş CPA = 7.800 ÷ 1,05 ≈ **7.429 TL**. **Hedef: iki durumu ayır** (*sezgisel*; §1.4 B3 tablosu, sıra 2). **(1) Mevcut OTA misafirini doğrudan kanala çekmek:** misafir reklam olmadan da OTA'dan gelecekti; kazanç yalnız ödenmeyen komisyondur. Hedef CPA ≤ OTA komisyonu ÷ 1,05. *Varsayım:* sözleşmedeki komisyon %[18] → 12.000 × 0,18 = 2.160 TL → hedef ≈ **2.057 TL**. **(2) Yeni pazar ya da sezon dışı artımlı talep:** misafir başka yoldan gelmeyecekti; sınır katkıdır. Hedef = B2 × 0,5 ≈ **3.714 TL**. **Belirsizse** (kampanya iki tür misafiri karışık getiriyorsa) düşük olanı kullan, örnekte 2.057 TL. Komisyon oranını otelin kendi OTA sözleşmesinden al; kaynaklı bir sektör oranı bulunamadı, %18 yalnız örnek girdidir. Küçük otel haftada 50 rezervasyona ulaşmaz; önce Modül 01 §4.4 karar tablosunu uygula, sığ olaya (ödeme başlatma) yalnız oradaki koşullarla in. Soruyla satan otelde WhatsApp kurgusu (§1.3 merdiveni) kullanılır. Yurt dışı sette konum ücreti hedef ülkeye göre değişir (B2) |
| Hukuk / politika | Belgesiz konaklama tanıtılamaz; ilanlarda belge numarası bulunur [MEVZUAT, KTB Genelgesi 2026/4]. Acenta, belge numarasını ve unvanını online kanalında gösterir (2021 düzenlemesi, doğrulanmalı). "Her şey dahil" görselinde alkol yok. Yurt dışı kitlede Türk alkol yasağının uygulanıp uygulanmadığı belirsiz; muhafazakâr yol göstermemek. Avrupa'daki kitleye günlük kiralık daire ya da villa reklamı konut kategorisine girebilir; otel girmez [RESMİ] |
| Sık hata | Belge numarasız ilan; alkol görseli; OTA'dakinden pahalı "doğrudan" fiyat; Avrupa'ya mesaj reklamı kurup ölçüm alamamak |

**Örnek metin**
- **Birincil:** "Ayvalık'ta denize [200 m], [12] odalı taş ev otel. Kasım–Mart hafta içi 3 gece kalana kahvaltı ve geç çıkış, yalnız sitemizden yapılan rezervasyonlarda. Turizm işletme belgesi no: [….]"
- **Başlık:** "Doğrudan rezervasyona geç çıkış" · **CTA:** Şimdi rezervasyon yap

### 2.9 B2B hizmet · Yeşil (avukat ve sağlık meslekleri hariç, §1.1)

| Alan | Karar |
|---|---|
| Hedef | Demo ya da teklif talebi, keşif görüşmesi, içerik indirme, etkinlik kaydı |
| Amaç → konum | Potansiyel müşteriler → **hızlı form (yüksek niyet)** ya da site formu (Lead olayı). Uzun satış döngüsünde CRM aşamaları CAPI ile gönderilir |
| Teklif | Ücretsiz analiz ya da denetim, sektöre özel rapor, kısa demo, örnek proje sunumu |
| Kreatif açıları | Kurucunun ya da uzmanın konuştuğu video, izinli müşteri vakası, süreç şeması. İlk cümlede sektöre seslen ("Lojistik firmaları için…") |
| Hedefleme | Geniş kitle; ayrıştırmayı kreatif yapar. Müşteri listesi yüklemesi KVKK'ya tabidir (Modül 03 §4) |
| Takip | Form → eleme → keşif → teklif → sözleşme. CPL yerine nitelikli lead başına maliyeti izle [PRATİSYEN]. "Nitelikli potansiyel müşteri" hedefi ayda en az 200 lead ister; altında kalıyorsan normal lead hedefiyle çalış, kaliteyi CRM'de izle. Hızlı form verisi 90 gün sonra indirilemez; CRM'e otomatik akış kur [RESMİ] |
| Bütçe mantığı | Modül 01 §2.3 örneği: hedef CPA 6.000 TL (müşterinin verdiği hedef; §1.4 B3 tablosu, sıra 1), talep→satış %8 → hedef CPL **480 TL**. Ayda 10 satış için 125 talep gerekir, yani ≈ **60.000 TL/ay**. Satış 7 günden uzun sürüyorsa standart ilişkilendirme satışın bir kısmını göstermez; kararı CRM'den ver |
| Hukuk / politika | **Kapı istisnası:** hukuk bürosu ve avukat bu kartla açılmaz, kırmızıdır (Avukatlık Kanunu m.55; Modül 09 §1). İşyeri hekimliği, diyetisyen ya da psikolog gibi sağlık meslek hizmeti de kırmızıdır. Mali müşavirlik gibi meslek hizmetlerinde meslek reklam kuralı araştırılmadı; yayından önce hukukçuya doğrulat (§1.1). Tacir ve esnafa önceden onaysız ticari ileti gönderilebilir ama ret hakları vardır ve İYS kaydı gerekir [MEVZUAT]. Kişinin geliri sorulamaz. Şirket cirosu yerine çalışan sayısını sormak daha güvenli (*sezgisel*) |
| Sık hata | İçerik indiren lead'i satışa "sıcak" diye vermek; iş e-postası alanı olmadığı için kişisel e-postalarla dolan form; lead'leri haftalarca bekletmek |

**Örnek metin**
- **Birincil:** "Lojistik firmaları için filo yakıt raporu: araç başına tüketimi [30] günlük veriyle çıkarıyor, sapma yapan araçları listeliyoruz. Önce [45] dakikalık ücretsiz analiz görüşmesi; bulgular yazılı gelir."
- **Başlık:** "Ücretsiz filo yakıt analizi" · **CTA:** Teklif al
- **Form (Meta'nın B2B örnekleri):** iş e-postası, şirket adı, unvan, "Şirketinizin kaç çalışanı var?", "Hizmeti ne zaman değerlendirmeyi düşünüyorsunuz?"

### 2.10 Spor salonu · Sarı

| Alan | Karar |
|---|---|
| Hedef | Yeni üye, deneme ziyareti, dönemsel kampanya (yeni yıl, yaz öncesi), PT ve grup ders paketi, kurumsal üyelik |
| Amaç → konum | Varsayılan: **WhatsApp**, §1.3 merdiveni; deneme randevusu işi olduğu için en çok 2. basamak. Ekip geri arıyorsa: hızlı form; "Randevu talebi" sorusu reklamı yalnız mobil Facebook'ta yayınlar, Instagram ağırlıklı kitlede bu soruyu koyma [RESMİ]. Randevuyu Facebook'ta Randevular ile yönetiyorsan hatırlatma otomatik gider [RESMİ]. Online üyelik satılıyorsa Satışlar → site |
| Teklif | Ücretsiz deneme (bir pratisyen 3 ya da 7 gün öneriyor [PRATİSYEN]); arkadaşını getir; kurumsal paket; süreli program; ilk ay PT seansı |
| Kreatif açıları | Gerçek üye ve eğitmen, salon turu, grup dersinin enerjisi, "ilk gün nasıl geçer" anlatımı |
| Hedefleme | Salona yakın dar konum, 18+. Üye listesinden benzer kitle oluşturmak KVKK'ya tabidir |
| Takip | Etiketler: "Deneme verildi → Geldi → Üye oldu". Bunlar özel etiketlerdir; §1.3 merdiveni için deneme verilen sohbeti Meta'nın hazır potansiyel müşteri etiketlerinden biriyle de işaretle. Gelmeyene hatırlatma gönder. Randevular özelliği hatırlatmayı otomatik gönderir |
| Bütçe mantığı | *Varsayım:* aylık üyelik KDV hariç 2.000 TL, katkı %60 = 1.200 TL/ay, ortalama 5 ay üyelik → LTV katkısı 6.000 TL. Lead→deneme %50, deneme→üye %30 → p = 0,15. Üyelik süresi salonun kaydından ölçülmüşse pay yerine LTV kullanılır (§1.4 B3 tablosu, sıra 3); ölçülmemişse ilk ayın başa başını geçme (Modül 01 §3.5). LTV:CAC = 3:1 (pratisyen kuralı) → izin verilen CAC 2.000 TL → panel hedef CPL = 2.000 × 0,15 ÷ 1,05 ≈ **286 TL**. Yalnız ilk ayın katkısıyla başa baş ≈ **171 TL**. Nakit durumuna göre seç. Ocak ve yaz öncesi için kreatifi önceden hazırla |
| Hukuk / politika | Olumsuz beden algısı ima eden ifade ya da görsel yok, yağ sıkıştıran yakın çekim yok; kilo verme reklamı 18+ [RESMİ]. Kilo vermede önce-sonra konusunda kaynaklar çelişiyor; kullanma. "Kilolu musun?" gibi kişisel özellik sorusu yok. Diyetisyen ya da fizyoterapist hizmeti tanıtılıyorsa o kısım sağlık meslek mensubu kapısına girer: **kırmızı**, dur ve hukukçuya sor (§1.1 istisnası, Modül 09 §1, §5.5). Reklam yalnız salon hizmetini anlatır. Takviye satışı §1.2'ye tabi |
| Sık hata | Deneme sonrası takip yapmamak; beden utandıran kreatif (ret); il geneli hedefleme; deneme randevusuna gelmeyenleri hatırlatmamak |

**Örnek metin**
- **Birincil:** "Bostancı'da sabah 07.00 grubu: 50 dakikalık kuvvet antrenmanı, en fazla [10] kişi, eğitmen eşliğinde. İlk ders ücretsiz; salonu ve programı yerinde görün."
- **Başlık:** "İlk ders ücretsiz" · **CTA:** WhatsApp mesajı gönder
- **Karşılama:** "Merhaba, deneme dersi için hangi gün uygun?" · Önerilen sorular: "Sabah grubu (S7)", "Üyelik fiyatları", "Kurumsal üyelik"

---

## 3. Yayın öncesi sektör kontrol listesi

- [ ] §1.1 kapısı geçildi; sektör tabloda yoksa ya da iki sınıfa giriyorsa Modül 09 §1 uygulandı. Kırmızı ya da sarı ise yazılı insan onayı alındı.
- [ ] Belge ve bağlantılar reklamda: EİDS bağlantısı ve fiyat (emlak; form ve katalog kurgusunda §2.4'teki ek kurallar), belge numarası (konaklama, acenta), HealthTürkiye logosu (sağlık turizmi).
- [ ] Yurt dışı kitlede hedef ülkenin reklam ve veri koruma kuralı kontrol edildi, insan onayı alındı (sağlık turizmi §2.3).
- [ ] Görsel ve metinde alkol, kişisel özellik iması, tıbbi işlem (salon), öğrenci görseli (kurs), doğrulanmamış yorum yok.
- [ ] İndirim varsa önceki fiyat aynı kanalda son 10 günün en düşüğü; tarih ve adet yazılı.
- [ ] Dönüşüm konumu hesapta var. Kademeli özellik yoksa yedek yol seçildi.
- [ ] Formda yasak soru yok. Onay kutuları ayrı, isteğe bağlı ve işaretsiz.
- [ ] Her teklif reklamının kendi karşılama metni ve kodu var. Hazır yanıtlar, uzakta mesajı ve etiketler kurulu.
- [ ] Bütçe B1–B5 ile müşterinin kendi oranlarından hesaplandı ve kapasiteyi aşmıyor.
- [ ] Yanıt verecek kişi ve saatler belli. Arama ve form reklamlarında saat planı uygulandı.

---

## Doğrulanması gerekenler

- **Kademeli özellikler:** "Mesajlaşma yoluyla potansiyel müşteri" hedefi, WhatsApp Flows form şablonu, SMS doğrulama, form sonunda WhatsApp düğmesi ve WhatsApp Durum reklamları Türkiye hesaplarında var mı? Hesaba göre değişebilir.
- **Meta Business Agent:** Türkiye ve Türkçe desteği ile Meta One planlarının Türkiye fiyatı bilinmiyor.
- **Avrupa mesajlaşma kısıtı:** İngiltere, İsviçre ve Norveç kapsamda mı? Meta sayfaları bir yerde "Avrupa, Japonya ve Güney Kore", başka yerde "Avrupa ve Japonya" diyor.
- **"Otomatik hedef kitle tanımlamaları":** Sağlık turizmi yönetmeliğindeki bu ifadenin Meta'daki karşılığı tanımlı değil. Sağlık Bakanlığı uygulama rehberi bulunamadı.
- **Sağlık veri kısıtı:** Meta'nın sağlık reklamverenlerine uyguladığı olay kısıtı Türkiye'de geçerli mi, belirsiz.
- **Lazer epilasyon:** Belediye ruhsatlı salondaki statüsü kaynaklarda çelişkili (cihaz türüne göre yargı kararları var). Güncel mevzuat metniyle doğrula.
- **EİDS:** Proje lansmanı ve değerleme hizmeti reklamı "ilan" sayılıyor mu, bulunamadı. Satılık ilanda e-Devlet yetkisinin başlangıç tarihi yalnız haberden alındı. Form kurgusunda (§2.4 b) metindeki URL ile formdaki bağlantının "reklamda EİDS bağlantısı" şartını karşılayıp karşılamadığı Bakanlık metninde açık değil. Ofisin kendi sitesinin EİDS doğrulamalı ilan gösterip gösteremediği ve katalog `url` alanının EİDS sayfasına bağlanıp bağlanamayacağı doğrulanmadı. Hızlı formun giriş metnindeki bağlantı tıklanabilir mi, hesapta dene.
- **Sağlık turizminde hasta görseli:** Meta reklamında yorum ve beğeninin kapatılıp kapatılamadığı doğrulanmadı; doğrulanana kadar sponsorlu hasta görseli yok. Hedef ülke kuralları yalnız Almanya (HWG §11) ve İngiltere (CAP 12.25) için okundu; diğer ülkeler için yerel kontrol gerekir.
- **WhatsApp merdiveni:** Satın alma optimizasyonu için "manuel kurulum" şartı WhatsApp Business kaynak sayfasından alındı; Advantage+ kurulumda görünüp görünmediği hesaba göre değişebilir.
- **Meslek reklam kuralları:** Mali müşavir ve benzeri meslekler araştırılmadı (Modül 09 "Doğrulanması gerekenler"). Forex ürününün Meta'nın CFD yasağına girip girmediği ürüne göre değişir.
- **Konaklama ve alkol:** Türk alkol yasağının yurt dışı kitleye yönelik otel reklamına uygulanması belirsiz. Acentalara yönelik 2021 kuralının güncelliği kontrol edilmedi.
- **Shops reklamları** ve Facebook'ta Randevular'ın Türk ortakları doğrulanamadı.
- **Konum ücreti:** Dijital Hizmet Vergisi 1 Ocak 2027'de %2,5'e iniyor. Türkiye'deki %5 konum ücretinin değişip değişmeyeceği bilinmiyor; Ocak 2027'de yeniden kontrol et.
- **WhatsApp ve 6563:** Kanun WhatsApp'ı adıyla saymıyor. Kapsamda olduğu yaygın bir yorum.
- **Türkiye sektör maliyetleri:** Yöntemi açık bir CPL/CPM kaynağı yok. Bütün örnek sayılar *varsayımdır*.

---

## Kaynaklar

**[RESMİ] Meta yardım merkezi** (erişim 18.10.2026; mesajlaşma sayfaları 20.10.2026)
- Mesaja yönlendiren reklamlar: https://www.facebook.com/business/help/1816962591668838?locale=tr_TR
- Mesaj reklamı en iyi uygulamaları: https://www.facebook.com/business/help/269324800441478?locale=tr_TR
- WhatsApp'a yönlendiren reklam: https://www.facebook.com/business/help/447934475640650?locale=tr_TR
- Mesajlaşma yoluyla potansiyel müşteri: https://www.facebook.com/business/help/575610661605746?locale=tr_TR
- Arama reklamları: https://www.facebook.com/business/help/378168646496279?locale=tr_TR
- Facebook'ta Randevular: https://www.facebook.com/business/help/238169006756726?locale=tr_TR
- Formda randevu talebi: https://www.facebook.com/business/help/374052383033400?locale=tr_TR
- Avrupa ve Japonya mesajlaşma kısıtı: https://www.facebook.com/business/help/137609341049884?locale=tr_TR
- Hızlı form türleri: https://www.facebook.com/business/help/252352181957512?locale=tr_TR
- Koşullu cevaplar: https://www.facebook.com/business/help/154286325106161?locale=tr_TR
- Sektöre göre örnek sorular: https://www.facebook.com/business/help/1607931762802448?locale=tr_TR
- Formda yasak sorular: https://www.facebook.com/business/help/219356599612120?locale=tr_TR
- Potansiyel müşteriye yanıt: https://www.facebook.com/business/help/2246428158756947?locale=tr_TR
- Mesajlaşmada potansiyel müşteri şablonları: https://www.facebook.com/business/help/734075733714274?locale=tr_TR
- Özel reklam kategorisi, konut ve ülke listesi: https://www.facebook.com/business/help/1198401317374558?locale=tr_TR · https://www.facebook.com/business/help/399587795372584?locale=tr_TR · https://www.facebook.com/business/help/298000447747885?locale=tr_TR
- Gayrimenkul katalog reklamları: https://www.facebook.com/business/help/1993509814217759?locale=tr_TR
- Hassas veri gönderme: https://www.facebook.com/business/help/414614186228298?locale=tr_TR
- Konum ücretleri: https://www.facebook.com/business/help/1238737454289085
- Öğrenme aşaması: https://www.facebook.com/business/help/112167992830700
- Kişisel özellikler: https://www.facebook.com/business/help/2557868957763449?locale=tr_TR
- Sağlık ve sağlıklı yaşam: https://www.facebook.com/business/help/2489235377779939?locale=tr_TR

**[RESMİ] Diğer Meta ve WhatsApp**
- Alkol ilkesi: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/alcohol
- Sağlık ilkesi: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/health-wellness
- Yasaklı ticari uygulamalar (reklam standardı ve bağlı topluluk standardı; gerçekçi olmayan kazanç vaadi): https://transparency.meta.com/policies/ad-standards/deceptive-content/prohibited-commercial-practices/ · https://transparency.meta.com/policies/community-standards/prohibited-commercial-practices/ (erişim 20.10.2026)
- Silah ve finans ilkeleri: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/weapons-ammunitions-explosives/ · https://transparency.meta.com/policies/ad-standards/restricted-goods-services/financial-services/
- WhatsApp ücretleri ve pencereler: https://developers.facebook.com/docs/whatsapp/pricing
- Seyahat reklamları: https://developers.facebook.com/docs/marketing-api/travel-ads
- WhatsApp alt huni optimizasyonları (potansiyel müşteri: Potansiyel müşteriler amacı; satın alma: Satışlar ya da Etkileşim, manuel kurulum; erişim 20.10.2026): https://whatsappbusiness.com/resources/resource-library/down-funnel-optimizations-whatsapp/
- Meta Business Agent: https://whatsappbusiness.com/blog/introducing-meta-business-agent-ai/
- Mercedes-Benz Türkiye vakası: https://whatsappbusiness.com/blog/mercedes-benz-turkey-accelerated-sales-through-a-hybrid-marketing-approach/
- Modanisa vakası: https://developers.facebook.com/success-stories/modanisa/

**[MEVZUAT]**
- Sağlık Hizmetlerinde Tanıtım ve Bilgilendirme Faaliyetleri Hakkında Yönetmelik (RG 12.11.2025): https://www.resmigazete.gov.tr/eskiler/2025/11/20251112-2.htm
- 3359 sayılı Kanun: https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=3359&MevzuatTur=1&MevzuatTertip=5
- TRHTUY değişikliği (RG 01.07.2026): https://www.resmigazete.gov.tr/eskiler/2026/07/20260701-9.htm
- Fiyat Etiketi Yönetmeliği değişikliği (RG 11.10.2025): https://www.resmigazete.gov.tr/eskiler/2025/10/20251011-6.htm
- 2026 ceza tutarları (RG 23.12.2025): https://www.resmigazete.gov.tr/eskiler/2025/12/20251223-6.htm
- Alkol duyurusu (Tarım ve Orman Bakanlığı): https://www.tarimorman.gov.tr/TADAB/Duyuru/280/Alkollu-Icki-Reklamlarina-Iliskin-Mevzuatta-Yapilan-Degisiklik-Hakkinda-Duyuru
- Belgesiz konaklama genelgesi: https://www.alomaliye.com/2026/08/18/belgesiz-konaklama-ilanlarina-siki-denetim/
- Seyahat acentaları 2021 değişikliği: https://www.resmigazete.gov.tr/eskiler/2021/01/20210114-3.htm
- 6563 sayılı Kanun: https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=6563&MevzuatTur=1&MevzuatTertip=5
- Ticari İletişim ve Ticari Elektronik İletiler Hakkında Yönetmelik değişikliği (RG 04.01.2020; İYS'ye 3 iş günü içinde kayıt, kaydedilmeyen onay geçersiz): https://www.resmigazete.gov.tr/eskiler/2020/01/20200104-2.htm
- 1219 sayılı Kanun (ek m.13 sağlık meslek mensupları): https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=1219&MevzuatTur=1&MevzuatTertip=3
- 1136 sayılı Avukatlık Kanunu (m.55): https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=1136&MevzuatTur=1&MevzuatTertip=5

**[MEVZUAT, yurt dışı] ve düzenleyici kurum** (erişim 20.10.2026)
- Almanya, Heilmittelwerbegesetz §11: https://www.gesetze-im-internet.de/heilmwerbg/__11.html
- İngiltere, ASA/CAP kozmetik müdahale reklamı 18 yaş kuralı: https://www.asa.org.uk/news/strict-new-rules-for-ads-for-cosmetic-interventions.html
- AB Genel Veri Koruma Tüzüğü (GDPR, m.9 ve m.27): https://eur-lex.europa.eu/eli/reg/2016/679/oj
- Gıda ve takviyede sağlık beyanı yönetmeliği: https://www.resmigazete.gov.tr/eskiler/2023/04/20230420-5.htm
- Karayolu Taşıma Yönetmeliği (konsolide): https://www.alomaliye.com/wp-content/uploads/2018/01/karayolu-tasima-yon-16-05-2026.pdf

**[HABER]**
- TÜİK 2026 kullanım verisi: https://www.birgun.net/haber/whatsapp-zirvede-tuik-internet-kullanim-verilerini-acikladi-727545
- DataReportal Digital 2026 Turkey: https://datareportal.com/reports/digital-2026-turkey
- E-ticaret 2025: https://www.alomaliye.com/2026/05/12/turkiyede-e-ticaret-hacmi-2025te-4-6-trilyon-liraya-ulasti/
- EİDS sosyal medya cezası: https://www.ekonomist.com.tr/gundem/sosyal-medyadaki-emlak-ve-arac-ilanlarina-siki-denetim-82-isletmeye-8-2-milyon-tl-ceza-78039
- Satılık ilanda e-Devlet yetkisi: https://www.karar.com/ekonomi-haberleri/emlak-ilanlarinda-yeni-donem-1-subat-2026dan-itibaren-yetki-2017537
- 5580 ve Anayasa Mahkemesi kararı: https://www.gzt.com/ekonomi/anayasa-mahkemesi-emsal-karar-ozel-okullara-kesilen-5-kat-katlamali-ceza-hukmu-tarihe-karisiyor-egitim-haberleri-4223845
- Reklam Kurulu 2026 denetimleri: https://www.ekonomist.com.tr/gundem/vize-garantisi-ve-aldatici-saglik-reklamlari-mercek-altinda-reklam-kurulu-ndan-185-milyon-tl-lik-ceza-77056
- BGH hyaluron önce-sonra kararı (I ZR 170/24, 31.07.2025; Deutsches Ärzteblatt, erişim 20.10.2026): https://www.aerzteblatt.de/news/bundesgerichtshof-untersagt-werbung-fur-hyaluronspritzen-mit-vorher-nachher-bildern-9682baf7-8727-478a-9535-5bb619b29bbe

**[PRATİSYEN] ve [TOPLULUK]**
- LocaliQ Facebook kıyasları 2026: https://localiq.com/blog/facebook-advertising-benchmarks/
- LeadSync, ev hizmetleri: https://leadsync.me/blog/industries-home-services/
- Jon Loomer, yeni form özellikleri: https://www.jonloomer.com/11-new-meta-lead-ads-features/
- Stark Ajans, diş turizmi rehberi: https://starkajans.com/insights/dental-tourism-marketing-agency-guide
- Able CDP, sağlık kısıtları: https://www.ablecdp.com/blog/meta-healthcare-restrictions
- Restoran önerileri (Meta konuşmacısı): https://modernrestaurantmanagement.com/?p=34903
- Lazer epilasyon ve güzellik merkezleri (yalnız arama özeti): https://www.hukukihaber.net/guzellik-merkezleri-lazer-epilasyon-botoks-sac-ekimi-uygulamalarinin-doktorluk-meslegiyle-karsilastirilmasi-ceza-ve-uygulama-sorunlari
- R10.net, mesaj reklamı kaynağı sorunu: https://www.r10.net/facebook-business-manager-ads/3759417-meta-mesaj-reklamlari-sorunu.html

---

# Reklam politikası, Türk hukuku ve hesap riski

> **Modül 09 · Profesyonel paket · Hazırlanma: 17 Ekim 2026 (kaynaklara erişim 18–20 Ekim 2026).** Ekran adımları burada yok. Onlar için **Kurulum Kılavuzu** (`../KILAVUZ.md`): 2FA §3.1, ödeme §3.6, değerlendirme durumları §6.1, reddedilen reklam §6.2, kısıtlı hesap ve hata kodları §10, dolandırıcılık §11. Sektör kartları **Modül 08**, politika uyumlu metin örnekleri **Modül 04 §11**, müşteri listesi ve KVKK **Modül 03 §4.4**, çerez ve Piksel **Modül 05 §2.6**.
>
> **HUKUKİ TAVSİYE DEĞİLDİR.** Bu modül, yapay zekânın riskli işi fark edip durması için yazıldı. Mevzuat okunup özetlendi, yorumlar *sezgisel* diye işaretlendi. Kırmızı ve sarı kapılı işlerde, sağlıkta, finansta, kriptoda ve kişisel veride yayından önce avukata danışılmalı. Yapay zekâ bu işlerde tek başına "yayınla" kararı vermez.

## Bu modülde

- **Risk kapısı** ve Meta'nın **yasaklı/kısıtlı içerik tablosu**, Türkiye karşılığıyla. Kural: iki kural setinden sıkı olan uygulanır.
- **Kişisel özellik kuralı** için üç soruluk test ve 11 doğru/yanlış örnek. **Açılış sayfası** şartları (Meta + Türk e-ticaret hukuku).
- **Türk mevzuatı özeti:** reklam yönetmeliği, 10 günlük indirim kuralı (formül ve örnek), etkileyici kuralı, sağlık reklam yasağı, KVKK, İYS ve 2026 cezaları.
- **Hesap güvenliği**, kısıtlama tetikleyicileri, *sezgisel* hesap risk puanı, ret ve itiraz karar ağacı, ret günlüğü.
- Doldurulmuş **uyum incelemesi** ve **yayından önce uyum kontrol listesi**.

**Etiketler:** **[RESMİ]** Meta yardım merkezi, Şeffaflık Merkezi ya da WhatsApp Business ilke sayfası. **[MEVZUAT]** kanun, yönetmelik, Kurul kararı, bakanlık sayfası. **[PRATİSYEN]** hukuk bürosu, ajans, yazılım firması, banka blogu. **[HABER]** haber sitesi. **[TOPLULUK]** forum. *sezgisel* = Meta'nın ya da mevzuatın açık hükmü değil; pratisyen kuralı ya da bu modülün çıkarımı. *varsayım* = örnekteki uydurma girdi. **md.** = aksi yazmadıkça Ticari Reklam ve Haksız Ticari Uygulamalar Yönetmeliği (TRHTUY), 1 Ağustos 2026 değişiklikleri işlenmiş hali.

---

## 0. Karar sırası ve değişmez kurallar

1. **Kapı (§1):** Kırmızıysa dur. Sarıysa koşulları işaretle, insan onayı al. "Sarı + hukukçu" satırındaysa hukukçu onayı da al.
2. **Meta kuralı (§2):** Yasak mı, kısıtlı mı, yaş sınırı ya da yetki var mı?
3. **Metin ve görsel (§3, §5.2–5.4).**
4. **Açılış sayfası ya da sohbet (§4).**
5. **Veri (§5.6–5.7):** form soruları, KVKK metinleri, İYS.
6. **Hesap güvenliği (§6):** ilk harcamadan önce, sonra ayda bir.
7. **Kontrol listesi (§10)** ve **kanıt dosyası (§8)**. Ret olursa §7.

**Değişmez kurallar:**

- **Meta'nın onayı hukuki uygunluk kanıtı değildir.** Meta da yürürlükteki yasalara uymayı şart koşuyor [RESMİ]; Türkiye'de yasak olan reklam onaylansa bile iki kuralı birden çiğner.
- **Sıkı olan kazanır:** Karar = en sıkı(Meta kuralı, Türk hukuku, marka kuralı).
- **Emin değilsen sarı say.** Sarı = yayından önce insan onayı.
- **Kaçınma yok.** Reddi aşmak için kopyala-yükle, yeni hesap, başka domain ya da inceleyiciye farklı sayfa gösterme (cloaking) yasak [RESMİ].
- **Düzeltmek sorumluluğu silmez:** Reddedilen reklamı düzenlemek ya da silmek ihlali hesaptan kaldırmıyor [RESMİ]; aykırı reklamı sonradan düzeltmek de Türk hukukunda sorumluluğu kaldırmıyor (md. 32) [MEVZUAT].

---

## 1. Risk kapısı

**Tek kaynak:** Kapının esas listesi bu tablodur. Modül 08 §1.1 ve §1.2 bunun sektör özetidir ve iki modül ayrışırsa bu tabloyu esas alır. Yine de bir sektör iki yerde farklı renkteyse **sıkı olanı** uygula ve farkı insana bildir.

| Kapı | Ürün / hizmet | Karar |
|---|---|---|
| **Kırmızı, istisnasız** | Alkollü içki ve ana işi içki sunmak olan yer (bar, meyhane, birahane); tütün, nargile ve nargile salonu, e-sigara, vape (nikotinsiz dahil); beşerî ilaç (reçeteli ya da reçetesiz); yasa dışı bahis, şans oyunu, kumar; falcı, medyum, astrolog; sohbet, arkadaşlık, eş bulma; silah, mühimmat, havai fişek, biber gazı, şok cihazı; THC ve CBD; **Türkiye'yi hedefleyen kripto alım-satım, borsa, takas ya da staking platformu** (§2); **sağlık tesisi dışında tıbbi estetik işlem** (güzellik salonunda, kuaförde ya da evde dolgu, botoks, mezoterapi, iğneli işlem, saç ekimi, cerrahi) | **Reklam yok.** Dayanağı müşteriye §2, §5.5 ve md. 27 ile göster |
| **Kırmızı, dar istisna olabilir** | Türkiye'deki özel sağlık tesisi ve sağlık meslek mensubu (hekim, diş hekimi, eczacı, hemşire, ebe, optisyen ve 1219 ek md. 13'teki klinik psikolog, diyetisyen, fizyoterapist gibi meslekler), sağlık tesisinde yapılan tıbbi estetik işlem dahil; **yetki belgeli sağlık turizmi** tesisi ya da aracı kuruluşu; avukat; lisanslı şans oyunu | **Dur, hukukçuya sor.** İstisna belgeyle karşılanmadan yayın yok (§5.5). Sağlık turizminde yetki belgesi ve sağlık tanıtım yönetmeliğinin (SY, §5.5) md. 8 şartları belgeyle karşılanmadan yayın yok; hukukçu onayı kanıt dosyasında (§8) |
| **Sarı + hukukçu** | Takviye edici gıda; kredi, kart, sigorta, yatırım danışmanlığı; reklam kuralı araştırılmamış meslekler (mali müşavir vb.); Meta kripto yetkisi gerektirmeyen kripto içeriği (NFT, kripto şirketi hisselerinden oluşan fon, eğitim, haber, etkinlik, yalnız saklama yapan cüzdan, madencilik donanımı); izin belgesi olmayan çekiliş (§5.2); güzellik salonunda lazer epilasyon (§5.5) | **Koşulları işaretle + hukukçu onayı** kayıtlı (§8). Yapay zekâ tek başına yayınlamaz |
| **Sarı** | Kilo verme; kozmetik ürün; **tıbbi olmayan güzellik hizmeti** (cilt bakımı, kaş-kirpik, manikür, makyaj vb.); kuaför ve güzellik salonu; spor salonu; cinsel sağlık; emlak; otel, pansiyon, konaklama, seyahat acentası; nakliyat; MEB'e bağlı kurs; çocuk ürünü; sosyal mesele ve siyaset; etkileyici işbirliği; yapay zekâ ile üretilmiş insan figürü | **Koşulları işaretle + insan onayı.** Sektör koşulları: Modül 08 §1.1 |
| **Yeşil** | Yalnız bu tabloda ve Modül 08 §1.1'de sarı ya da kırmızı **olmayanlar**: moda, restoran ve kafe, tesisat ve temizlik gibi ev hizmetleri, B2B, online kurs… | Genel kurallar: §3, §4, §5.2–5.3 |

- **Eğer** ürün iki sınıfa birden giriyorsa → **sıkı olanı uygula.**
- **Eğer** restoran, kafe ya da otel menüsünde alkol varsa → kapı alkol yüzünden değişmez (restoran yeşil, otel sarı; Modül 08 §1.1 ile aynı karar). Ama görselde, metinde, katalogda ve karşılama mesajında içki, kokteyl, "happy hour" ve içki markası olmaz. Bu unsurlardan biri reklama girerse reklam kırmızıdır [RESMİ][MEVZUAT].
- **Eğer** tıbbi estetik işlem (dolgu, botoks, mezoterapi, iğneli işlem, saç ekimi, cerrahi) → yalnız ruhsatlı sağlık tesisinde yapılabilir. Tesisin reklamı kırmızı, dar istisna (§5.5). Güzellik salonu, kuaför ya da ev bu işlemleri tanıtamaz: ruhsatsız sağlık hizmeti tanıtımı Reklam Kurulu bildirimi ve suç duyurusu sebebi (SY md. 12/1-d) [MEVZUAT]. Hangi işlemin tıbbi sayıldığı yargıda tartışmalı [PRATİSYEN] → sınır *doğrulanmalı*. Ürün (takviye, cihaz, krem) satılıyorsa §2 satırına bak.
- **Eğer** avukat → kırmızı. Avukatlık Kanunu md. 55 iş elde etmeye yönelik reklamı yasaklıyor [PRATİSYEN özeti, *doğrulanmalı*]. Meta hukuk hizmeti reklamına izin veriyor; Türk kuralı daha sıkı.
- **Eğer** müşteri "rakip de yapıyor" diyorsa → dayanak değil; reklamveren, ajans ve mecra birlikte sorumlu (6502 md. 61) [MEVZUAT].

---

## 2. Meta yasaklı ve kısıtlı içerik tablosu

Tablo, Meta Reklam Standartları'nın Türkiye'deki işletmeler için önemli başlıklarını içeriyor; tam liste Şeffaflık Merkezi'nde. Meta, yerel yasaya aykırı bulunan reklamı kaldırabildiğini ya da o ülkede kısıtlayabildiğini de yazıyor [RESMİ].

| Konu | Meta [RESMİ] | Türkiye [MEVZUAT] | Karar |
|---|---|---|---|
| Alkol | Türkiye, alkol reklamı yasak ülkeler listesinde | 4250 md. 6 (7584 ile 2026'da genişledi); md. 27/11 | Yok |
| Tütün, nikotin, e-sigara, vape, nargile salonu | Yasak, "wellness" vape dahil. DSÖ/FDA onaylı bırakma ürünü 18+ | md. 27/11 | Yok |
| Silah, mühimmat, havai fişek, biber gazı, şok cihazı, mutfak dışı bıçak | Yasak. Silahın parçası olmayan kılıf, kasa, av yeleği 18+; güvenlik kursu serbest | md. 27/9 | Yok; aksesuarda insan onayı |
| İlaç | Reçeteli: yetki, yalnız ABD, Kanada, Yeni Zelanda. Reçetesiz: yerel yasaya uyulursa 18+ | Beşerî tıbbi ürün reklamı yasak (md. 27/11) | Yok |
| THC, CBD | THC yasak; CBD yalnız ABD'de, sertifika ve izinle | — | Yok |
| Takviye edici gıda | "Güvenli olmayan" takviye yasak; diyet amaçlı kilo takviyesi 18+ | Kilo miktarı/oranı, tanıklık, kurum onayı her durumda yasak (§5.5) | Sarı + hukukçu |
| Kilo verme, kozmetik işlem ve ameliyat | 18+. Tutulan yağın yakın çekimi, görünüşü aşağılama, koşulsuz "şu sürede şu sonuç" yasak. Kozmetik işlemde önce-sonra 18+ ile serbest | Tıbbi estetik işlem ve ameliyat yalnız sağlık tesisinde yapılır; tanıtımı sağlık yasağına girer (§1, §5.5) | Ürünse (takviye, cihaz, krem) sarı; işlemse §1 ve §5.5: tesiste kırmızı-dar istisna, tesis dışında yok |
| Tedavisi olmayan hastalık için "tedavi eder" (diyabet, kanser, Alzheimer, HIV vb.) | Yasak, hekimden gelse de | Gıda dışı üründe sağlık beyanı yasak | Yok |
| Cinsel sağlık | Haz odaklı yasak; tıbbi odaklı 18+ | Sağlık hizmetiyse §5.5 | Sarı |
| Şans oyunu, bahis | Meta yetkisi + hedef ülkede lisans, 18+ | Yasa dışı olanın reklamı yasak (md. 27/3) | Kırmızı |
| Flört, eş bulma | Önceden yazılı izin, 18+ | Yasak (md. 27/8) | Yok |
| Kripto alım-satımı, borsa, takas, staking | Yetki şart. Lisans yalnız Meta'nın uygun ülke listesindeki düzenleyicilerden kabul ediliyor ve başvuruda reklamın yayınlanacağı ülke seçiliyor. **Türkiye ve SPK listede yok** (değişiklik günlüğündeki son kayıt Nisan 2026). Yetki gerekmeyenler: NFT, kripto şirketi hisselerinden oluşan fon, eğitim, haber, etkinlik, yalnız saklama yapan cüzdan, madencilik donanımı | 7518 ile SPK izni şart [PRATİSYEN]. Yurt dışı platformun Türkçe site ya da tanıtımla Türkiye'deki kişilere yönelmesi Türkiye'ye yönelik faaliyet sayılıyor [HABER] | Türkiye hedefli platform reklamı: **yok**. Yetki gerektirmeyen kripto içeriği: sarı + hukukçu |
| Kredi, kart, sigorta | 18+, yasal bilgilendirme; maaş günü kredisi, vadesi ≤90 gün kredi, ikili opsiyon, CFD, ICO yasak | Oran verilirse toplam maliyetin aylık ve yıllık oranı ana vaatte (md. 25) | Sarı |
| Aldatıcı fiyat, izinsiz ünlü/kurum onayı, garantili yatırım getirisi; sahte belge, hile aracı | Yasak | md. 7; Ek A-4, A-16 | Yok |
| Afet ve kriz ortamını ticari istismar | Kısıtlı | Korku sömürüsü yasak (md. 5) | Sarı |
| Şok edici, cinsel, şiddet içeren görsel | Yasak (sivilce patlatma, işlem sırasında acı dahil) | md. 5 | Yok |
| Spam | Cloaking, vaadi vermeyen bağlantı, eylemsiz başka domaine yönlendirme, beğen/paylaş şartı, sahte "oynat" düğmesi, açılır pencere tuzağı, marka taklidi domain | — | Yok |
| Başkasının markası, telifli içerik | Yasak | md. 11–12 | Yazılı izin |
| Sosyal mesele, seçim, siyaset | **Seçim ve siyaset reklamı:** Türkiye, yetkilendirme ve sorumluluk reddi (reklamı kimin ödediğini gösteren etiket) zorunlu ülkeler listesinde. Reklamda yalnız sorumluluk reddindeki ad görünüyor. **Seçim dışı sosyal mesele:** Türkiye zorunlu listede değil | — | Seçim/siyaset: Meta yetkisi + sorumluluk reddi + insan onayı. Sosyal mesele: insan onayı; Meta reklamı siyasi sayabilir, sınır belirsizse yetki alıp kategoriyi seç (*sezgisel*) |

**Özel reklam kategorisi (konut, istihdam, finans):** Hedefleme kısıtları ABD, Kanada ve listedeki Avrupa ülkelerinde zorunlu, Türkiye listede yok [RESMİ]. **Eğer** kampanya bu ülkeleri de hedefliyorsa → kategoriyi seç.

**Kurulum Kılavuzu §6.2'ye not:** Orada önce-sonra görseli "sık ret sebebi" sayılıyor. Meta'nın güncel metni kozmetik işlemde 18+ ile izin veriyor; kilo vermede açık konuşmuyor, bir satıcı blogu yasak diyor [PRATİSYEN]. **Karar:** Kilo vermede önce-sonra kullanma. Türkiye'deki klinikte sponsorlu önce-sonra zaten yok (§5.5).

---

## 3. Kişisel özellik kuralı

**Kural [RESMİ]:** Reklam, görenin kişisel özelliğini söyleyemez, ima edemez, soru olarak soramaz. Yasak özellikler: ırk, etnik köken, renk, milli köken, din, yaş, cinsiyet, cinsel yönelim, cinsel kimlik, aile durumu, engellilik, tıbbi ya da genetik durum, fiziksel ya da ruhsal sağlık, hassas finansal durum, oy kullanma durumu, sendika üyeliği, sabıka kaydı, ad. Kişinin ikamet, iletişim, finansal ya da tıbbi bilgisini istemek veya paylaşmak da yasak (gizlilik ihlali). "Sen/siz" hitabı tek başına serbest. Meta'ya göre "Diğer [grup] bekârlarla tanışın" da yasak: okuyanı o gruba yerleştiriyor.

**Üç soruluk test (her metin, başlık ve görsel yazısı için):**

1. Cümle, okuyanın bir özelliği olduğunu **söylüyor ya da varsayıyor mu**? İşaretler: "…misin/musunuz?", "senin gibi…", "diğer … ile tanış", ad ile hitap, "orada oturuyorsun" iması.
2. Bu özellik **yasak listede** mi?
3. İkisi de evetse **yeniden yaz:** "[ürün/hizmet] + [ne işe yarar ya da kimin için tasarlandı] + [somut özellik]". Ürünü tarif et, okuyanı değil.

| Yanlış | Doğru | Özellik |
|---|---|---|
| "Hamile misiniz? Doğum çantanızı biz hazırladık." | "Doğum çantası seti: 24 parça, hastane listesine göre." | Tıbbi durum |
| "Oruç tutarken yorulan sen, iftara bizde gel." | "Ramazan boyunca iftar menümüz 19.00'da hazır." | Din |
| "Diğer emeklilerle hafta sonu gezisine katıl!" | "Hafta içi kültür turu: rehberli, yürüyüş temposu yavaş." | Yaş |
| "Yeni mi boşandınız? Hayatınızı yeniden kurun." | "Tek kişilik ev taşıma paketi: paketleme dahil [fiyat] TL." | Aile durumu |
| "Kredi notunuz düşük mü?" | "Kredi notu nasıl hesaplanır? 5 maddelik rehber." | Hassas finansal durum |
| "Türkiye'de yeni misiniz? Yabancılara özel danışmanlık." | "Oturma izni başvurusu: belgeler ve randevu süreci." | Milli köken |
| "Kadınsan bu fırsatı kaçırma!" | "Kadın koleksiyonunda yeni sezon: 40 model." | Cinsiyet |
| "Mehmet Bey, aracınızın muayenesi yaklaştı." | "Muayene öncesi 20 noktalı kontrol: [fiyat] TL." | Ad |
| "Kadıköy'de oturuyorsan bu reklam sana." | "Kadıköy şubemiz açıldı: Moda Caddesi, 09.00–21.00." | İkamet |
| Görsel: aynaya üzgün bakan kişi + "Bu sen misin?" | Görsel: ürün ve kullanım anı, soru yok | Görünüş/sağlık (görsel de sayılır) |
| Hızlı form: "Hangi hastalığınız var?" | "Hangi hizmet hakkında bilgi almak istersiniz?" | Sağlık bilgisi (formda da yasak [RESMİ]) |

**Hedefleme ile metin ayrı.** Bir kitleyi hedeflemek aykırı değil; hedeflenen özelliği metne yazmak aykırı. Kitle adlarını nötr tut: sağlık ya da finans çağrıştıran kitleler 2 Eylül 2025'ten beri işaretlenip engelleniyor [RESMİ]. Türk hukukunda da reklam hasta, çocuk, yaşlı ve engellileri istismar edemez (md. 5) [MEVZUAT]. Daha fazla örnek: Modül 04 §11.2.

---

## 4. Açılış sayfası şartları

Meta değerlendirmesi görsel ve metin kadar **yönlendirilen yeri** de inceliyor [RESMİ]. WhatsApp ya da DM'ye giden reklamda "açılış sayfası" karşılama mesajı ve sohbettir; aynı kurallar orada da geçerli (*sezgisel*).

| # | Şart | Başarısızsa | Dayanak |
|---|---|---|---|
| A1 | Sayfa mobilde, gizli sekmede açılıyor | Yayını beklet | [RESMİ] kontrol listesi |
| A2 | Reklamdaki ürün, fiyat, tarih ve teklif sayfada **aynı** | Birini düzelt | [RESMİ] alaka; md. 7 |
| A3 | Açılır pencere tuzağı, aşırı reklam, sahte düğme yok | Kaldır | [RESMİ] spam |
| A4 | Eylemsiz başka domaine yönlendirme yok; inceleyici ve kullanıcı aynı içeriği görüyor | **Cloaking kurma** | [RESMİ] spam, sahte davranış |
| A5 | Domain kısıtlı değil | Engel **60 gün**; yeni şüpheli hareket süreyi baştan başlatır. İhlal sürerken domain değiştirmek kaçınma sayılabilir → insan onayı | [RESMİ] |
| A6 | Satıcının unvanı, adresi, telefonu, e-postası, varsa MERSİS/vergi no kolay bulunuyor | Ekle | 6563 md. 3 [MEVZUAT] |
| A7 | Sipariş öncesi toplam bedel (KDV ve kargo dahil) açık; sipariş gecikmeden teyit ediliyor | Düzelt | 6563 md. 4; md. 13 |
| A8 | Ön bilgilendirme: ürün, satıcı, toplam fiyat, reklamla uyumlu teslim süresi, 14 gün cayma, şikâyet yolu; en az 12 punto | Ekle | Mesafeli Sözleşmeler Yön. md. 5 |
| A9 | Teslim ≤30 gün (kişiye özel üretim hariç); "24 saatte kargo" gerçekten karşılanıyor | Vaadi değiştir | 6502 md. 48 |
| A10 | Gizlilik ve aydınlatma metni var; Piksel çerez rızasından önce çalışmıyor | Modül 05 §2.6 | [MEVZUAT] |
| A11 | Sayfada da sağlık beyanı, sahte yorum, sahte geri sayım yok | Kaldır | md. 7, 28/B; Ek A-7 |
| A12 | Kendi alan adında satış varsa ETBİS kaydı var | Müşteriye sor | [PRATİSYEN]; kapsam *doğrulanmalı* |

---

## 5. Türk mevzuatı özeti

### 5.1 Mevzuat haritası

| Mevzuat | Kritik hüküm |
|---|---|
| **6502 sayılı Tüketicinin Korunması Hakkında Kanun** (md. 48, 61–63, 77) | Doğru ve dürüst reklam; örtülü reklam yasak; ispat reklamverende; mesafeli satış; cezalar |
| **Ticari Reklam ve Haksız Ticari Uygulamalar Yönetmeliği** (RG 10.01.2015/29232; değişiklik RG 01.07.2026/33297, yürürlük 1.8.2026) | İddia, karşılaştırma, fiyat, indirim, yorum, yapay zekâ, etkileyici, kesin yasaklar (md. 27) |
| **Fiyat Etiketi Yönetmeliği** (değişiklik RG 11.10.2025/33044) | İndirim öncesi fiyat = son 10 günün en düşüğü |
| **Sosyal Medya Etkileyicileri Tarafından Yapılan Ticari Reklam ve Haksız Ticari Uygulamalar Hakkında Kılavuz** (Reklam Kurulu 2021/2, 04.05.2021) + md. 23/A | "Reklam/Tanıtım" + reklamveren adı, önde |
| **1219 sayılı Kanun** (md. 24, 40, ek md. 13), **3359 sayılı Kanun ek md. 11** (7557 ile 2025), **Sağlık Hizmetlerinde Tanıtım ve Bilgilendirme Faaliyetleri Hakkında Yönetmelik** (RG 12.11.2025/33075) | Sağlıkta sponsorlu reklam genel kural olarak yasak |
| **Gıda ve Takviye Edici Gıdalarda Sağlık Beyanı Kullanımı Hakkında Yönetmelik** (RG 20.04.2023/32169), **Sağlık Beyanı Denetimi Hakkında Yönetmelik** (RG 13.06.2023/32220) | Gıda ve takviye dışında sağlık beyanı yok |
| **4250 sayılı İspirto ve İspirtolu İçkiler İnhisarı Kanunu** md. 6 | Alkol reklamı ve tanıtımı yasak |
| **6698 sayılı Kişisel Verilerin Korunması Kanunu** (md. 9: 7499 ile, 1.6.2024); Kurul ilke kararı 2026/347; Çerez Rehberi (2025) | Aydınlatma ve açık rıza ayrı; yurt dışı aktarım; çerezde açık rıza |
| **6563 sayılı Elektronik Ticaretin Düzenlenmesi Hakkında Kanun** + **Ticari İletişim ve Ticari Elektronik İletiler Hakkında Yönetmelik** | Satıcı bilgileri; SMS/e-posta/arama ve büyük olasılıkla WhatsApp/DM için önceden onay; İYS (§5.7) |
| **Karşılığı Nakit Olmayan Piyangolar ve Çekilişler Hakkında Yönetmelik** (RG 04.10.2006) | Sosyal medya çekilişi dahil, istisnalar dışında MPİ izni [PRATİSYEN] (§5.2) |
| **Mesafeli Sözleşmeler Yönetmeliği** | Ön bilgilendirme, cayma istisnaları |
| **1136 sayılı Avukatlık Kanunu** md. 55 | Avukat reklamı yasak |

### 5.2 Reklam metni: eğer-ise kuralları

- **Eğer** "en ucuz, en iyi, tek, lider, 1 numara" → yayın dönemini kapsayan bağımsız belge yoksa **kullanma** (md. 7, 9).
- **Eğer** rakiple kıyas → rakibin adı, logosu, markası yok; ölçülebilir özellik; belge (md. 8, 9/3).
- **Eğer** fiyat → KDV dahil TL, teslim ücreti, taksitte toplam fiyat (md. 13). "Bedava" şarta bağlıysa şart ana mesajda; ödeme varsa "bedava" yok (Ek A-16).
- **Eğer** aciliyet → yalnız gerçekse, tarih ve saatle (Ek A-7). Sahte "kapanıyoruz" yasak (Ek A-11).
- **Eğer** müşteri yorumu → yalnız satın alması doğrulanmış kaynaktan (md. 28/B-6).
- **Eğer** tanıklık → gerçek deneyim (md. 16/1). Doktor, eczacı ya da sağlık kuruluşu ürüne sağlık beyanı veriyor izlenimi yok; önlüklü "uzman" da buna girer (md. 16/3).
- **Eğer** "çevre dostu" → kapsam, ölçüm yöntemi, akredite belge (md. 17).
- **Eğer** insandan ayırt edilemeyen yapay zekâ figürü → reklamda açıkça yaz (md. 18/8); gerçek kişinin yapay zekâ kopyasına ürünü kullanıyormuş gibi rol verme (md. 27/12). Meta'nın "AI info" etiketi "Bu reklam hakkında" menüsünde [RESMİ, Şubat 2025]; yeterliliği belirsiz, görselde ya da ilk satırda ayrıca yaz (*sezgisel*).
- **Eğer** çekiliş → ödül, adet, tarihler, teslim koşulu (md. 15); ödülsüz yarışma ilanı yasak (Ek A-15). **İzin de gerekebilir:** Instagram ve Facebook çekilişleri dahil karşılığı nakit olmayan çekilişler, yönetmelikteki istisnalar dışında Milli Piyango İdaresi (MPİ) iznine tabi (Karşılığı Nakit Olmayan Piyangolar ve Çekilişler Hakkında Yönetmelik, RG 04.10.2006) [PRATİSYEN]. Kaynakların saydığı istisnalar: kamu kurumunun sosyal amaçlı çekilişi, bilgi ya da beceriyle elenilen yarışma, değeri düşük ödül (eşik yıllık değişiyor). İzinsiz çekiliş yaptığı öne sürülen fenomenler hakkında 2021'de soruşturma açıldı [HABER]. Güncel yetkili makam, istisna eşiği ve ücret *doğrulanmalı*. → İzin belgesi yoksa **sarı + hukukçu**; izin yazısı ya da hukukçunun muafiyet görüşü kanıt dosyasına (§8). Sağlık hizmeti çekiliş ve hediye konusu yapılamaz (SY md. 5/1-l) [MEVZUAT].
- **Eğer** çocuk ürünü → 18+ hedefle, veliye konuş; "sadece/yalnızca" yok, çocuğu ebeveyni ikna etmeye teşvik yok (md. 24). Çocuğa profillemeyle hedefli reklam yasak (md. 25/A).
- **Eğer** "14 gün iade hakkı" gibi yasal hak → ekstra avantaj gibi sunma (md. 7/7).

Hazır metin tablosu: Modül 04 §11.2; teklif kuralları: Modül 04 §5.3.

### 5.3 İndirim ve fiyat: formül ve örnek

1 Ağustos 2026'dan beri reklamda ve etikette kural aynı; 2022–2025 kaynaklarındaki "30 gün" geçersiz [MEVZUAT].

- **Referans fiyat (mal):** R = indirim başlangıç günü S'den önceki 10 gün içinde (S−10 … S−1), **aynı satış kanalında** uygulanan **en düşük** fiyat (md. 14/3, 14/5).
- **Hizmet ve çabuk bozulan mal:** R = indirimden bir önceki fiyat.
- **İndirim oranı** = (R − indirimli fiyat) ÷ R.
- **Reklamda zorunlu:** R, başlangıç ve bitiş tarihi, sınırlıysa adet (md. 14/1). "2 al 1 öde", "500 TL üzeri %20" ve sadakat kampanyaları da kapsamda.

**Doldurulmuş örnek (*varsayım*):** Mont, markanın kendi sitesinde. İndirim 11 Kasım 2026'da başlıyor, indirimli fiyat 599 TL. Site fiyatları: 1–4 Kasım 899 TL, 5–7 Kasım 749 TL (flaş kampanya), 8–10 Kasım 899 TL.

- Pencere 1–10 Kasım → R = **749 TL**. Oran = (749 − 599) ÷ 749 ≈ **%20**.
- **Doğru:** "749 TL yerine 599 TL (%20). 11–17 Kasım 2026. Stok: 300 adet."
- **Yanlış:** "899 TL yerine 599 TL, %33 indirim!" → gerçekte olduğundan büyük indirim izlenimi (md. 14/2).
- Pazaryerinde 699 TL olması site indirimine referans olmaz; her kanalın R'si kendi geçmişinden.
- **Kural:** Son 10 günde kampanya yapıldıysa üstü çizili fiyat büyük olasılıkla o kampanya fiyatıdır. Fiyat geçmişini sakla; ispat yükü satıcıda.

### 5.4 Etkileyici ve ortaklık reklamları (md. 23/A) [MEVZUAT]

- **Ne zaman:** Etkileyici reklamverenin ürününe ya da sitesine yönlendiriyorsa; para, ücretsiz ürün ya da indirim almışsa; reklamverenin çekilişini, kampanyasını ya da karşılık alarak katıldığı etkinliği paylaşıyorsa.
- **Ne yazılır:** "Reklam" ya da "Tanıtım" + şunlardan biri: reklamverenin adı/unvanı, "@[reklamveren] tarafından sağlandı", "Ürünleri bana gönderdiği için @[reklamveren]'e teşekkürler", "@[reklamveren]'e teşekkürler".
- **Nereye:** Kaydırmadan görünür, okunur büyüklükte, diğer etiketlerden **önce**, her parçada. Sesli içerikte yayının başında söylenir. 2021 kılavuzundaki "@marka ile işbirliği" tek başına artık yetmiyor.
- **Meta tarafı:** Ortaklık reklamı ve marka içeriği de kapsamda (*sezgisel*). "Ücretli ortaklık" etiketinin yeterliliği doğrulanmadı → ilk satıra ve görsele ayrıca yaz. Örnek ilk satır (*varsayım*): "Reklam | @ornekkahve tarafından sağlandı". Reklamveren de sorumlu: yazılı brif ver, yayından önce kontrol et (Modül 04 §7).

### 5.5 Sağlık reklam yasağı ve sağlık beyanı [MEVZUAT]

**SY** = Sağlık Hizmetlerinde Tanıtım ve Bilgilendirme Faaliyetleri Hakkında Yönetmelik.

| Durum | Karar | Dayanak |
|---|---|---|
| Türkiye'deki özel sağlık tesisi ya da sağlık meslek mensubu, Türkiye'deki kişiye | **Sponsorlu reklam yok.** Ücretli ve öne çıkarma amaçlı olmayan kayıt serbest. "Gönderiyi öne çıkar" da ücretli olduğu için yok (*sezgisel*) | SY md. 5/1-i; 1219 md. 24, 40; 3359 ek md. 11 |
| Yeni sağlık tesisi, açılıştan sonraki **ilk 1 ay** | Uygun sponsorlu tanıtım serbest: adres, iletişim, saatler, uzmanlık, unvanlar, koruyucu sağlık bilgisi. Fiyat, kampanya, hasta teşekkürü ve hasta görseli yok. **Kapsam notu:** Hüküm metinde yalnız "sağlık tesisleri" için yazılı; muayenehaneye ya da bireysel sağlık meslek mensubuna uygulanıp uygulanmadığı belirsiz → hukukçu | SY md. 5/1-j, 5/1-e, 5/1-m, 7/1-j |
| Yetki belgeli **sağlık turizmi** tesisi ya da aracı kuruluşu | **Kapı: kırmızı, dar istisna (§1); hukukçu onayı şart.** Yurt dışına yönelik ayrı hesap/site; **Türkçe dışı** dil; Türkiye'de yaşayanlar hedeflenmez, otomatik hedef kitle tanımları kapalı; "HealthTürkiye" logosu; yetki belgesi yayında. Şartların hepsi belgeyle karşılanırsa fiyat ve kampanya serbest | SY md. 8 |
| Organik hasta görseli | Ek-1 onam formu; önce-sonra aynı koşulda, tarihli, rötuşsuz; yorum, beğeni, paylaşım kapalı; uyarı metni | SY md. 7 |
| Güzellik salonu, kuaför | Tıbbi olmayan hizmet (cilt bakımı, kaş-kirpik, manikür, makyaj) **sarı**. Dolgu, botoks, mezoterapi, iğneli işlem, saç ekimi ve cerrahi yalnız sağlık tesisinde yapılır → salonda bu işlemlerin reklamı **yok**; "medikal" gibi tıbbi çağrışım da yok. Lazer epilasyon: salonda yapılıp yapılamayacağı yargı kararlarında çelişkili [PRATİSYEN] → **sarı + hukukçu**, tıbbi dil yok | md. 7; SY md. 12/1-d; 1219 md. 25 üzerine yargı kararları [PRATİSYEN]; sınır *doğrulanmalı* |
| Takviye | Kilo kaybı/artışının miktarı ya da oranı yok; tanıklık ve kurum övgüsü yok; "tüketmezsen sağlığın bozulur" iması yok | Takviye sağlık beyanı yön. md. 5/11; md. 27/10 |
| Kozmetik ve diğer gıda dışı ürün | "Sivilceyi tedavi eder", "dökülmeyi durdurur" gibi sağlık beyanı yok | Sağlık Beyanı Denetimi Yön. |

**Sağlık turizmi Meta kurulumu (*sezgisel*):** Türkiye'yi hariç tut (konum, Advantage+ hedef kitlede de kesin sınır; Modül 03 §1). Dilde ve metinde Türkçe yok. Advantage+ hedef kitle ve kitle genişletmelerini kullanma. Meta reklamında yorumlar kapatılamadığı için hasta görselini sponsorlu yayınlama.

**Ceza** (3359 ek md. 11) §5.8'de. Aykırı tanıtımı yapan ve paylaşan aynı derecede sorumlu (SY md. 5/2); ajans ve yapay zekâ "paylaşan" sayılabilir (*sezgisel*).

### 5.6 KVKK: karar kuralları

Ayrıntı Modül 03 §4.4 ve Modül 05 §2.6.

- **Eğer** müşteri listesi → yalnız kendi müşterin ve aydınlatma metni bu amacı içeriyorsa. Referans, "arkadaşını öner", satın alınmış ya da kartvizit listesi **asla** (KVKK duyurusu, 21.07.2026) [MEVZUAT].
- **Eğer** liste ya da CAPI verisi Meta'ya gidiyorsa → büyük olasılıkla yurt dışına aktarım (KVKK md. 9); düzenli aktarımda açık rızaya dayanılamaz. Hukukçu onayı kayıtlı olsun (*sezgisel*).
- **Eğer** Piksel → davranışsal reklam çerezi açık rıza ister; rızadan önce tetiklenmez [MEVZUAT].
- **Eğer** form ya da site veri topluyor → aydınlatma ayrı, açık rıza ayrı; aydınlatma "okudum, kabul ediyorum"a bağlanamaz; başka şirketin metni kopyalanamaz (ilke kararı 2026/347, içerik pratisyen özetinden) [PRATİSYEN].
- **Eğer** sağlık ya da finans → URL'de, olay ve kitle adında hassas bilgi yok. Formda sağlık, finans, kimlik no, din, siyasi görüş, cinsel yaşam, sendika ya da sabıka sorusu varsa reklam yayınlanmıyor [RESMİ].

### 5.7 İYS ve ticari elektronik ileti [MEVZUAT]

- SMS, e-posta ve arama ile tanıtım **önceden onay** ister (6563 md. 6). Kişinin kendi talebine yanıt vermek ayrı; sonraki kampanya SMS'i ayrı onay ister (*sezgisel* ayrım).
- İletide gönderenin kimliği ve ücretsiz ret yolu bulunur; ret sonrası gönderim **3 iş günü** içinde durur (6563 md. 7–8).
- İYS dışında alınan onay **3 iş günü içinde İYS'ye** kaydedilmezse geçersiz.
- Esnaf ve tacire onaysız gönderilebilir, ama önce İYS'de ret kontrolü yapılır.
- **WhatsApp, Instagram DM ve Messenger:** Bu kanallardan gönderilen kampanya ya da duyuru mesajı da büyük olasılıkla ticari elektronik iletidir. 6563'teki tanım vasıtaları "gibi" diye örnek sayıyor, kapalı liste değil (*sezgisel* yorum, *doğrulanmalı*; İYS'deki kanal türü de doğrulanmalı). Tıkla-WhatsApp ya da Messenger reklamından gelen kişinin **kendi sorusuna yanıt** ayrıdır. Sonradan gönderilen her pazarlama mesajı, toplu "yeni sezon" ya da "indirim başladı" duyurusu dahil, **ayrı onay ve İYS kaydı** ister. Sağlık tesisi ve sağlık meslek mensubu bu kanallardan da rızasız tanıtım iletisi gönderemez (SY md. 5/1-k) [MEVZUAT].
- **WhatsApp Business ilkesi** de işletmenin kişiye yazabilmesi için iki şart arıyor: kişi numarasını ya da kullanıcı adını vermiş olacak ve sonraki mesajları WhatsApp'tan almayı kabul ettiğini onaylamış olacak. 24 saatlik müşteri hizmeti penceresi dışında yalnız onaylı mesaj şablonu gönderilebiliyor. Ret talebine uyulması zorunlu [RESMİ, ilke sayfası 23.09.2026 güncel; hesaba göre *doğrulanmalı*].

**Hızlı formdan İYS'ye akış:**

1. Özel sorumluluk reddi bölümüne önce aydınlatma bağlantısını **onay kutusu olmadan** koy.
2. SMS, e-posta, arama ve **WhatsApp** için **ayrı, "İsteğe bağlı", işaretsiz** kutular ekle. Yayındaki forma sonradan metin eklenemiyor; formu çoğalt [RESMİ].
3. Onay metni: "[Unvan]'ın kampanya ve duyurularını SMS ile almayı kabul ediyorum." WhatsApp kutusu: "[Unvan]'ın kampanya ve duyurularını WhatsApp ile almayı kabul ediyorum."
4. İndirirken kutu yanıtlarını, zaman damgasını ve form adını sakla.
5. 3 iş günü içinde İYS'ye yükle; İYS'de ret görünen numaraya gönderme.
6. Gizlilik politikası bağlantısını her forma koy (Meta'nın iki sayfası bunu farklı anlatıyor) [RESMİ].

### 5.8 Cezalar (2026)

| İhlal | Aralık | Kaynak |
|---|---|---|
| Ticari reklam ihlali, internet (6502 md. 77/12-e) | 1.083.706 – 10.837.065 TL | [MEVZUAT] RG 23.12.2025/33116 |
| Ticari reklam ihlali, SMS (77/12-f) | 505.729 – 5.057.297 TL | Aynı |
| Haksız ticari uygulama (77/13); ülke genelinde | 108.370 – 1.083.706 TL; 1.083.706 – 10.837.065 TL | Aynı |
| Mesafeli sözleşme ihlali (77/1-a) | İşlem başına 3.973 TL | Aynı |
| Sağlık tanıtım ihlali (3359 ek md. 11) | ≥100.000 TL, önceki ayın brüt hizmet gelirinin %2'sine kadar; bir yılda tekrarında bir kat artırım; üçüncüsünde 10 güne kadar faaliyet durdurma | [MEVZUAT] |
| Onaysız ticari ileti (6563 md. 12/1-a) | 2.859 – 14.309 TL; toplu gönderimde on katına kadar | Kanun [MEVZUAT]; tutar [PRATİSYEN] |
| KVKK aydınlatma yükümlülüğü | 85.437 – 1.709.200 TL | [PRATİSYEN], *doğrulanmalı* |

Reklam Kurulu ayrıca durdurma, düzeltme, 3 aya kadar tedbiren durdurma ve internet içeriğinin kaldırılması kararı verebiliyor [MEVZUAT].

---

## 6. Hesap güvenliği ve kısıtlama önleme

### 6.1 Erişim ve güvenlik kurulumu (ilk harcamadan önce)

| # | Ayar | Hedef | Dayanak |
|---|---|---|---|
| G1 | Kişisel 2FA (Kurulum Kılavuzu §3.1) | Herkeste kimlik doğrulama uygulaması ya da geçiş anahtarı + kurtarma kodları; birden fazla yöntem | [RESMİ] |
| G2 | Portfolyoda 2FA zorunluluğu: Business Suite → Ayarlar → İşletme portfolyosu bilgileri → İşletme seçenekleri | **"Herkes"**. Eksikliği reklam erişimini ya da ödeme yöntemini kaybettirebilir | [RESMİ] |
| G3 | Tam kontrol | En az **2** aktif kişi; ideal olarak en fazla **10** | [RESMİ] |
| G4 | Kullanıcı temizliği | 90 gündür girmeyeni, 2FA'sızı, e-postası işletmeyle ilgisiz olanı çıkar | [RESMİ] |
| G5 | Eski reklam hesapları | Bir yıldır yayın yapmayanı kapat (silinemiyor) | [RESMİ] |
| G6 | **Eş onayı:** Ayarlar → Güvenlik Merkezi → Eş onayı → Reklam hesabı güvenliği | Ajans ya da geçici erişimde **"İlave koruma"** (her reklam onaylanır); harcama kontrolü için **"Özel koruma"** (seçilen bütçenin üstü onaylanır). Tam kontrolü olanın reklamı onaya girmiyor | [RESMİ] |
| G7 | Paylaşılan kredi sınırları | Şüpheli işletmeyi kaldır | [RESMİ] |
| G8 | Hesap harcama sınırı | Aylık plan bütçesi × 1,1 | Kurulum Kılavuzu §11; çarpan *sezgisel* |
| G9 | İşletme doğrulaması | Güvenlik Merkezi'nde "doğrulanmış" | [RESMİ] |
| G10 | Meta Verified for Business (ücretli) | İsteğe bağlı: rozet, kimlik taklidi koruması, gelişmiş destek; plan ve uygunluk bölgeye göre değişiyor | [RESMİ] |

**Yapay zekâ ajanının erişimi (*sezgisel*):** Ajan, portfolyoya **kişi ya da ortak** olarak ve yalnız gereken reklam hesabına eklenir; şifre istenmez (Kurulum Kılavuzu §11). Ajana **tam kontrol verilmez**, böylece eş onayı devrede kalır. "Özel koruma" eşiği = müşterinin onayladığı günlük bütçe; üstündeki her reklam insana düşer.

### 6.2 Kısıtlama tetikleyicileri → önlem

Kısıtlama portfolyoya, reklam hesabına, Sayfaya ya da kişisel profile uygulanabiliyor; harcama sınırından reklam vermenin tamamen kaybına kadar gidiyor [RESMİ].

| Tetikleyici [RESMİ] | Önlem |
|---|---|
| Ciddi ya da tekrarlayan ilke ihlali | §10; §7.3 |
| Ele geçirme şüphesi; 2FA eksikliği | G1–G6; §6.4 |
| Olağan dışı ödeme, başarısız ödemeler, itiraz edilen ödeme | İşletme adına, limiti yeterli tek kart (Kurulum Kılavuzu §3.6); kartı sık değiştirme; bankaya itirazdan önce Meta desteğine yaz (*sezgisel*) |
| Yaptırımdan kaçınma (kapanan hesabın yerine yeni hesap, hesap ağı) | Yapma; önce §7.2 |
| İhlalci ağla bağlantı | Kapanmış başka hesabın kartını, domainini, yöneticisini kullanma (*sezgisel*) |
| Şikâyet, gizleme, engelleme | Yayındaki reklam yeniden incelenebiliyor; metni ve sayfayı kontrol et |

Meta, inceleme derinliğine karar verirken geçmiş uyumu dikkate alabildiğini yazıyor, ama kaç ihlalin kısıtlamaya yol açtığını yayımlamıyor [RESMİ]. "90 günde 3 ihtar" gibi kurallar doğrulanamadı; dayanma [PRATİSYEN].

### 6.3 Hesap risk puanı (*sezgisel*)

Meta eşik yayımlamadığı için bu bir öncelik aracıdır, ölçülmüş bir model değildir. Her yeni kampanyadan önce hesapla.

| Etken | Puan |
|---|---|
| Portfolyoda 2FA'sı olmayan en az bir kişi | +3 |
| Domain kapatılmış bir hesapla ilişkili ya da kısıtlı domain uyarısı var | +3 |
| Son 30 günde ≥2 ret | +2 |
| Son 30 günde başarısız ödeme ya da ters ibraz | +2 |
| Ürün §1'de sarı ya da dar istisnalı kırmızı | +2 |
| Reklam hesabı ya da portfolyo 90 günden yeni | +1 |
| Tam kontrolde tek kişi | +1 |
| Yabancı kart, VPN ya da ülke uyumsuzluğu [TOPLULUK] | +1 |

**0–2:** normal. **3–4:** kreatifleri küçük partilerle gönder, bütçeyi yavaş artır (Modül 06 §6.2), yayından önce insan onayı. **≥5:** yeni reklam gönderme; önce +3 ve +2'lik etkenleri gider.

**Örnek (*varsayım*):** Kuaför. Portfolyo 40 günlük (+1), tek tam kontrol (+1), kuaför §1'de sarı (+2) → **4**. İkinci tam kontrol kişisi eklenince 3.

### 6.4 Ele geçirme şüphesinde

1. Reklamları durdur; durduğunu kontrol et. Bankayı ara, kartı geçici kapat.
2. Profil için **https://www.facebook.com/hacked**: şifreyi değiştir, bilinmeyen oturumları ve şüpheli uygulama erişimlerini kapat [RESMİ].
3. Portfolyoda tanımadığın kişileri, ortakları ve paylaşılmış kredi sınırlarını kaldır (G4, G7).
4. Durumu İşletme Desteği Ana Sayfası'ndan, adresi kendin yazarak kontrol et; "Sayfanız kapatılacak" mesajlarındaki bağlantılara tıklama (Kurulum Kılavuzu §11).
5. Olayı tarih ve saatle günlüğe yaz; itirazda işe yarar.

---

## 7. Ret, kısıtlama ve itiraz

### 7.1 Reklam reddi: karar ağacı

Ekran adımları Kurulum Kılavuzu §6.2'de; gerekçe İşletme Desteği Ana Sayfası'nda yazar [RESMİ].

| Durum | Yap | Yapma |
|---|---|---|
| Gerekçe doğru | İhlal eden bileşeni bul (görsel, metin, hedefleme, sayfa), düzenle; otomatik yeniden incelenir. Düzenlenemiyorsa yeni reklam | Aynı içeriği kopyalayıp yükleme |
| Gerekçe yanlış | **Silme** (silersen talep hakkını kaybedebilirsin). Değerlendirme talep et; Meta 48 saat içinde karar vermeyi hedefliyor. Başarılı itirazla kalkan ret ihlal sayılmaz | Beklerken aynı reklamı başka sette gönderme |
| Gerekçe belirsiz | Ret metnini §2, §3 ve §4 ile eşleştir; eşleşme varsa "doğru", yoksa "yanlış" satırı | Rastgele değişiklikle deneme-yanılma |
| Değerlendirmeden sonra yine ret | Aynı reklam için yeniden talep yok (Kurulum Kılavuzu §6.2); kavramı yeniden yaz ya da bırak | Küçük değişiklikle zorlama |
| Önce onaylandı, sonra reddedildi | Normal: şikâyet, gizleme, beklenmedik etkileşim ya da rastgele kontrol yeniden inceleme başlatıyor [RESMİ] | "Daha önce geçmişti" diye itiraz |
| "Kısıtlı domain" | Nedenini bul; ihlal sürüyorsa domain değiştirmek kaçınma → insan onayı | Aynı içeriği yeni domaine taşıma |

**Talep metni** (açıklama alanı çıkarsa; hesaba göre değişebilir), *örnek*: "Reklam [ID] 'Kişisel özellikler' gerekçesiyle reddedildi. Metin 'Kadın koleksiyonunda yeni sezon: 40 model' yalnız ürün kategorisini tarif ediyor; okuyana dair varsayım ya da soru yok. Görsel ve açılış sayfası aynı ürünü gösteriyor." Kısa, olgusal, tek gerekçeli yaz (*sezgisel*).

### 7.2 Hesap ya da varlık kısıtlaması [RESMİ]

1. Bilgisayardan İşletme Desteği Ana Sayfası → **Hesap durumuna genel bakış** → kısıtlı hesap → **Neler yapabilirsin?**
2. İstenen adımı tamamla: kimlik onayı, doğrulama, hesabı güvenceye alma ya da değerlendirme talebi.
3. Yalnız **yönetici** talep edebilir. Hak **sınırlı**, karar **nihai**, inceleme genellikle 48 saat. Bu yüzden önce olası sebepleri §10 ile kontrol et, G1–G6'yı kapat, sonra tek ve hazırlıklı talep gönder (*sezgisel*).
4. Kısıt kalkınca eski reklamlar kendiliğinden başlamaz; elle aç.
5. İlke ihlaliyle kapanan hesap 6 ay içinde yeniden etkinleşmeye uygun olmazsa kalıcı kapanıyor; yasanın izin verdiği durumlarda kullanılmamış ön ödemeli bakiye kaybedilebiliyor.
6. Kişisel profil kısıtlanırsa, o kişinin tek kullanıcı olduğu reklam hesapları da kapanabiliyor. G3'ün nedeni bu.

### 7.3 Ret günlüğü ve durdurma kuralları

| Tarih | Reklam ID | Kapı | Meta gerekçesi | Teşhis | Eylem | Sonuç |
|---|---|---|---|---|---|---|
| 12.11.2026 | 1202… | Sarı (kozmetik) | Sağlık ve sağlıklı yaşam | "Sivilceyi yok eder" sağlık beyanı | Düzenle | Onaylandı |

**Durdurma kuralları (*sezgisel*):**

- **Eğer** aynı gerekçeyle 2. ret → o kreatif ailesini durdur, gerekçeyi insanla çöz.
- **Eğer** 30 günde herhangi bir gerekçeyle 3 ret → hesaba yeni reklam gönderme, insan incelemesi.
- **Eğer** hesap düzeyinde uyarı ya da kısıt → tüm yeni gönderimleri durdur, §7.2.
- Başarılı itirazla kalkan ret sayılmaz [RESMİ].

### 7.4 Türk makamlarından bildirim gelirse

Ticaret Bakanlığı, Reklam Kurulu, il sağlık müdürlüğü, KVK Kurumu ya da TİTCK'dan yazı geldiyse → reklamı **durdur**, kanıt dosyasını (§8) hazırla, **avukata ilet**. Yapay zekâ bu yazışmaya kendisi cevap vermez. Reklam Kurulu kararına karşı 30 gün içinde idare mahkemesinde dava açılabildiği yazılıyor [PRATİSYEN].

---

## 8. Kanıt dosyası

İspat yükü reklamverende (6502 md. 61; md. 9). Her kampanyaya bir klasör aç; belgeler reklamın **yayında olduğu dönemi** kapsasın.

| İddia ya da işlem | Saklanacak belge |
|---|---|
| "En", "1 numara", "%X daha etkili", çevre beyanı | Bağımsız rapor ya da akredite belge, dönemi ve yöntemi belli |
| Üstü çizili fiyat; sınırlı stok | Kanalın 10 günlük fiyat geçmişi; kampanya başı stok kaydı |
| Yorum; etkileyici içeriği; yapay zekâ figürü | Siparişle eşleşen yorum; sözleşme ve onaylı içerik; üretim aracı ve tarih |
| Form onayları; müşteri listesi | Kutu yanıtları (WhatsApp dahil), zaman damgası, İYS kaydı; liste kaynağı, o tarihteki aydınlatma metni, hukukçu onayı |
| Görseldeki ya da videodaki gerçek kişi (müşteri, çalışan, model, UGC üreticisi, önce-sonra yapılan kişi, adı ve fotoğrafıyla gösterilen yorumcu) | Yazılı kullanım izni + KVKK aydınlatma ve **ayrı** açık rıza; süre ve mecra (Meta reklamı dahil) belirtilmiş, geri çekme yolu yazılı. Fotoğraf ve ses kişisel veridir; görüntü kişilik hakkına da girer (TMK md. 24). Şablonu hukukçu hazırlar (*sezgisel*) |
| Çekiliş | MPİ izin yazısı ya da hukukçunun muafiyet görüşü; katılımcı ve kazanan kaydı, ödül teslim belgesi (§5.2) |
| Yayın onayı | Kim, ne zaman, hangi sürümü onayladı (kırmızı ve sarı kapıda zorunlu; "sarı + hukukçu"da hukukçunun adı ve tarihi) |

---

## 9. Doldurulmuş örnek: uyum incelemesi (*varsayım*)

**Müşteri:** Kendi sitesinden satan cilt bakım markası. **Kampanya:** Kasım indirimi, Türkiye.

**Taslak:** "Sivilceli cildinden utanıyor musun? Türkiye'nin en etkili serumu 7 günde sivilceleri yok eder! 899 TL yerine 449 TL. Son 2 saat! Yorum: 'Harika ürün' (Instagram)." **Görsel:** Önlüklü model serumu tutuyor. **Site geçmişi:** Son 10 günde 599 TL uygulanmış.

| # | Sorun | Kural | Düzeltme |
|---|---|---|---|
| 1 | "Sivilceli cildinden utanıyor musun?" | Kişisel özellik, görünüşü aşağılama [RESMİ] | Soru yok; ürünü tarif et |
| 2 | "Sivilceleri yok eder" | Gıda dışı üründe sağlık beyanı [MEVZUAT] | "Yağlı ve karma ciltler için hafif dokulu serum" |
| 3 | "7 günde" | Koşulsuz süre + sonuç [RESMİ]; md. 7 | Sil |
| 4 | "Türkiye'nin en etkili" | Belge yok (md. 9) | Sil |
| 5 | "899 TL yerine" | R = 599 TL (md. 14/3) | "599 TL yerine 449 TL (%25)" |
| 6 | Tarih ve adet yok | md. 14/1 | "11–17 Kasım 2026, 500 adet" |
| 7 | "Son 2 saat!" gerçek değil | Ek A-7 | Gerçek bitiş saati |
| 8 | Instagram yorumu | Doğrulanmamış yorum (md. 28/B-6) | Sitedeki doğrulanmış alıcı yorumu ya da hiç |
| 9 | Önlüklü model | Sağlık meslek mensubu izlenimi (md. 16/3) | Önlüksüz kullanım anı |

Oran = (599 − 449) ÷ 599 ≈ **%25**. **Son metin:** "Yağlı ve karma ciltler için hafif dokulu serum. İçerik listesi ürün sayfasında. 11–17 Kasım 2026: 599 TL yerine 449 TL (%25). 500 adetle sınırlı, 17 Kasım 23.59'da bitiyor." **Sayfa:** aynı fiyat ve tarih (A2), sayfada da sağlık beyanı yok (A11), ön bilgilendirme var (A8). **Risk puanı:** kozmetik sarı (+2) → 2, normal.

---

## 10. Yayından önce uyum kontrol listesi

**A. Kapı**
- [ ] §1 kapısı belirlendi (Modül 08 ile farklıysa sıkı olan). Kırmızıda reklam yok; dar istisnada ve "sarı + hukukçu"da hukukçu, sarıda insan onayı kayıtlı (§8).
- [ ] Gerekiyorsa 18+, Meta yetkisi (şans oyunu, flört, seçim/siyaset; kripto Türkiye hedefinde zaten yok) ya da yurt dışı hedefte özel reklam kategorisi ayarlı.

**B. Metin ve görsel**
- [ ] Her metin ve görsel §3 testinden geçti.
- [ ] "En, tek, lider, garanti, kesin sonuç" yok ya da belgesi kanıt dosyasında.
- [ ] Gıda ve takviye dışında sağlık beyanı yok; takviyede kilo rakamı ve tanıklık yok.
- [ ] Rakip adı/logosu, şok edici görsel, alkol, tütün ve silah (arka planda bile) yok.
- [ ] Yorumlar doğrulanmış alıcıdan. Önlüklü "uzman", izinsiz ünlü, gerçek kişinin yapay zekâ kopyası yok.
- [ ] Görünen her gerçek kişinin yazılı izni ve açık rızası dosyada (§8). Tıbbi olmayan önce-sonrada görsel gerçek, aynı koşulda çekilmiş ve rötuşsuz (*sezgisel*; md. 7).
- [ ] Güzellik salonu ya da kuaför reklamında dolgu, botoks, mezoterapi, saç ekimi gibi tıbbi işlem ve "medikal" dili yok (§1, §5.5).
- [ ] Yapay zekâ figürü varsa açıkça yazıyor. Etkileyici içeriğinde ilk satırda "Reklam/Tanıtım" + reklamveren adı.

**C. Fiyat ve teklif**
- [ ] Fiyat KDV dahil TL; kargo ve taksit bilgisi doğru.
- [ ] İndirimde R aynı kanalda son 10 günün en düşüğü (§5.3); tarih, adet ve oran doğru.
- [ ] Aciliyet gerçek ve tarihli; "bedava" şartı ana mesajda; çekilişte ödül, adet, tarih, teslim koşulu var.
- [ ] Çekiliş izni (MPİ) ya da hukukçunun muafiyet görüşü dosyada (§5.2, §8).

**D. Açılış sayfası ya da sohbet**
- [ ] §4'te A1–A11 geçti (e-ticarette A12 de). Karşılama mesajı reklamdaki teklifle aynı.

**E. Form ve veri**
- [ ] Formda yasak soru yok; gizlilik politikası bağlantısı var.
- [ ] Aydınlatma ayrı; açık rıza ve SMS/e-posta/arama/WhatsApp onayları ayrı, isteğe bağlı, işaretsiz; İYS sorumlusu belli.
- [ ] WhatsApp, DM ya da Messenger ile sonradan kampanya mesajı gidecekse kişinin ayrı onayı ve İYS kaydı var; yalnız soru sormuş olması yetmez (§5.7).
- [ ] Müşteri listesi ve Piksel için Modül 03 §4.4 ve Modül 05 §2.6 kapıları geçildi.

**F. Hedefleme**
- [ ] Çocuk ürününde 18+; sağlık turizminde Türkiye hariç, Türkçe yok, otomatik genişletme kapalı; kitle adları nötr.

**G. Hesap**
- [ ] G1–G6 tamam; 2FA "Herkes"; en az 2 tam kontrol; harcama sınırı ve eş onayı eşiği onaylı bütçeye göre.
- [ ] Risk puanı (§6.3) < 5. Ret günlüğü güncel, §7.3 durdurma kuralı tetiklenmemiş.

---

## Doğrulanması gerekenler

- **Meta ihlal eşiği** yayımlanmıyor; §6.3 ve §7.3'teki sayılar *sezgisel*.
- **2FA ve 90 gün:** Meta'nın iki sayfası portfolyo yaşına bağlı kuralı farklı anlatıyor; ekrandaki seçenek esas.
- **Eş onayı düzeyleri ve Meta Verified planları** Türkiye hesaplarında hesaba göre değişebilir.
- **"AI info" (md. 18/8), "Ücretli ortaklık" (md. 23/A) etiketleri** ve **"Bu reklamı neden görüyorum?" (md. 25/A)** Türk şartlarını karşılıyor mu, belirsiz.
- **Doğrudan satış reklamı (md. 27/1):** Meta reklamı kapsamda mı, yani "son, sadece, sınırlı satış" dili yasak mı, belirsiz; tarih ve adet yaz.
- **Meslek kuralları:** Avukatlık Kanunu md. 55 ve TBB düzenlemesinin güncel metni okunmadı; mali müşavir, veteriner hekim gibi meslekler araştırılmadı; lisans düzeyi psikolog ve "yaşam koçu"nun SY karşısındaki durumu belirsiz.
- **Sağlık:** "Otomatik hedef kitle tanımlamaları"nın Meta karşılığı tanımlı değil. Güzellik salonunda hangi işlemin tıbbi sayıldığı (özellikle lazer epilasyon) yargıda çelişkili. Kozmetik iddia kuralları (Kozmetik Yönetmeliği, TİTCK) okunmadı. "İlk 1 ay" istisnasının muayenehane ve bireysel sağlık meslek mensubunu kapsayıp kapsamadığı belirsiz.
- **Lisanslı şans oyunu** Türkiye için doğrulanmadı. **Kripto:** SPK'nın reklam kuralları okunmadı (Meta tarafı doğrulandı, §2).
- **Çekiliş:** Yönetmelik metni ve MPİ'nin güncel başvuru sayfası okunmadı; izin bilgisi pratisyen kaynaklardan. İstisna eşiği ve ücret yıllık değişiyor.
- **WhatsApp/DM iletisi:** 6563 kapsamına girdiği yorum; İYS'deki kanal türü ve Bakanlığın açık görüşü bulunamadı.
- **Görseldeki kişi izni:** KVKK ve kişilik hakkı gereği izin ve rıza şablonu hukukçuya hazırlatılmalı; Kurul'un reklam görseli kararları okunmadı.
- **ETBİS:** Yalnız Instagram/WhatsApp'tan satanın kayıt yükümlülüğü kaynaklarda net değil.
- **Cezalar:** KVKK ve 6563 tutarları ile ilke kararı 2026/347'nin içeriği pratisyen kaynaktan. 3359'daki 100.000 TL alt sınırın yıllık artışa tabi olup olmadığı doğrulanmadı.
- **Meta'nın Türkiye için KVKK yurt dışı aktarım güvencesi** bulunamadı.

---

## Kaynaklar

**[RESMİ] Meta yardım merkezi** (tr_TR, erişim 18–19.10.2026)
- Reklam kısıtlamaları: https://www.facebook.com/business/help/975570072950669?locale=tr_TR
- İhlal örnekleri ve yaptırımdan kaçınma: https://www.facebook.com/business/help/3858246497787265?locale=tr_TR
- Kısıtlı hesap için değerlendirme: https://www.facebook.com/business/help/530209463124901?locale=tr_TR
- Devre dışı ya da kısıtlı hesap: https://www.facebook.com/business/help/422289316306981?locale=tr_TR
- Reddedilen reklam: https://www.facebook.com/business/help/1210227555661027?locale=tr_TR
- Değerlendirmedeki reklamlar: https://www.facebook.com/business/help/204798856225114?locale=tr_TR
- Önce onaylanıp sonra reddedilen reklamlar: https://www.facebook.com/business/help/133691315402558?locale=tr_TR
- Reklam ilkeleri kontrol listesi: https://www.facebook.com/business/help/757209948405699?locale=tr_TR
- Kişisel özellikler: https://www.facebook.com/business/help/2557868957763449?locale=tr_TR (İngilizce örnekler: https://www.facebook.com/business/help/2557868957763449?locale=en_US)
- Sansasyonel içerik: https://www.facebook.com/business/help/304110064285796?locale=tr_TR
- Sağlık ve sağlıklı yaşam: https://www.facebook.com/business/help/2489235377779939?locale=tr_TR
- Reçeteli ilaç: https://www.facebook.com/business/help/263390265553560?locale=tr_TR
- Şans oyunları: https://www.facebook.com/business/help/345214789920228?locale=tr_TR
- Flört: https://www.facebook.com/business/help/765622867361201?locale=tr_TR
- Kripto: https://www.facebook.com/business/help/438252513416690?locale=tr_TR
- Kısıtlı domainler: https://www.facebook.com/business/help/733029810936140?locale=tr_TR
- Özel reklam kategorileri ve ülke listesi: https://www.facebook.com/business/help/298000447747885?locale=tr_TR · https://www.facebook.com/business/help/399587795372584?locale=tr_TR
- Hızlı formda yasak sorular: https://www.facebook.com/business/help/219356599612120?locale=tr_TR
- Hızlı form özel sorumluluk reddi: https://www.facebook.com/business/help/1550411888622740?locale=tr_TR
- Hızlı form gizlilik politikası: https://www.facebook.com/business/help/1247534515288168?locale=tr_TR
- Hassas çağrışımlı kitle kısıtları: https://www.facebook.com/business/help/1055828013359808?locale=tr_TR
- Portfolyo güvenliği: https://www.facebook.com/business/help/608572510632070?locale=tr_TR
- Portfolyoda 2FA zorunluluğu: https://www.facebook.com/business/help/280940009201586?locale=tr_TR · https://www.facebook.com/business/help/865384580786591?locale=tr_TR
- Güvenlik Merkezi: https://www.facebook.com/business/help/216940652189296?locale=tr_TR
- Eş onayı: https://www.facebook.com/business/help/529947753306370?locale=tr_TR
- Ele geçirilmiş hesap: https://www.facebook.com/business/help/583427765324458?locale=tr_TR · https://www.facebook.com/hacked
- İşletmeler için Meta Verified: https://www.facebook.com/business/help/308979828303560?locale=tr_TR
- Sosyal mesele, seçim ve siyaset reklamlarının ülkelere göre kullanılabilirliği (Türkiye seçim/siyaset listesinde, erişim 19.10.2026): https://www.facebook.com/business/help/2150157295276323?locale=tr_TR

**[RESMİ] WhatsApp Business** (erişim 20.10.2026)
- WhatsApp Business ilkesi (son güncelleme 23.09.2026; onay şartı, şablon mesaj, ret): https://whatsappbusiness.com/policy/

**[RESMİ] Meta Şeffaflık Merkezi ve duyuru** (erişim 18–19.10.2026)
- Reklam Standartları: https://transparency.meta.com/policies/ad-standards/
- Alkol: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/alcohol/
- Tütün: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/tobacco-related-products/
- Silah, mühimmat, patlayıcı: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/weapons-ammunitions-explosives/
- Sağlık: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/health-wellness/
- İlaç: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/drugs-pharmaceuticals/
- Finans: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/financial-services/
- Kripto ürün ve hizmetleri (uygun lisans listesi, yetki gerektirmeyen içerik; erişim 20.10.2026): https://transparency.meta.com/policies/ad-standards/restricted-goods-services/cryptocurrency-products-and-services/
- Şans oyunları: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/gambling-games/
- Yasaklı belgeler ve finansal hizmetler: https://transparency.meta.com/policies/ad-standards/restricted-goods-services/prohibited-documents-and-financial-services/
- Yasaklı ticari uygulamalar: https://transparency.meta.com/policies/ad-standards/deceptive-content/prohibited-commercial-practices/
- Yerel olarak yasa dışı ürünler: https://transparency.meta.com/policies/ad-standards/unacceptable-content/locally-illegal-products-services/
- Sahte davranış: https://transparency.meta.com/policies/ad-standards/business-assets/inauthentic-behavior/
- Hesap bütünlüğü: https://transparency.meta.com/policies/ad-standards/business-assets/account-integrity/
- Spam (cloaking, yanıltıcı bağlantı): https://transparency.meta.com/policies/community-standards/spam/
- Reklamlarda üretken yapay zekâ şeffaflığı (Şubat 2025): https://about.fb.com/news/2025/02/gen-ai-transparency-metas-ads-products/

**[MEVZUAT]**
- 6502 sayılı Kanun: https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=6502&MevzuatTur=1&MevzuatTertip=5
- TRHTUY konsolide metin: https://www.mevzuat.gov.tr/File/GeneratePdf?mevzuatNo=20435&mevzuatTur=KurumVeKurulusYonetmeligi&mevzuatTertip=5
- TRHTUY değişikliği (RG 01.07.2026/33297): https://www.resmigazete.gov.tr/eskiler/2026/07/20260701-9.htm
- Ticaret Bakanlığı duyurusu (01.07.2026): https://ticaret.gov.tr/haberler/ticaret-bakanligi-tarafindan-ticari-reklam-ve-haksiz-ticari-uygulamalar-yonetmeliginde-yapilan-degisikliklerle-tuketicilerin-aldatici-reklam-ve-ticari-uygulamalara-karsi-korunmasi-guclendiriliyor
- Fiyat Etiketi Yönetmeliği değişikliği (RG 11.10.2025/33044): https://www.resmigazete.gov.tr/eskiler/2025/10/20251011-6.htm
- 2026 idari para cezaları (RG 23.12.2025/33116): https://www.resmigazete.gov.tr/eskiler/2025/12/20251223-6.htm
- Etkileyici kılavuzu duyurusu (2021): https://ticaret.gov.tr/haberler/ticaret-bakanligi-sosyal-medya-etkileyicileri-icin-kilavuz-yayimladi
- Sağlık tanıtım yönetmeliği (RG 12.11.2025/33075): https://www.resmigazete.gov.tr/eskiler/2025/11/20251112-2.htm
- 1219 sayılı Kanun: https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=1219&MevzuatTur=1&MevzuatTertip=3
- 3359 sayılı Kanun: https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=3359&MevzuatTur=1&MevzuatTertip=5
- Gıda ve takviyede sağlık beyanı (RG 20.04.2023/32169): https://www.resmigazete.gov.tr/eskiler/2023/04/20230420-5.htm
- Sağlık Beyanı Denetimi Hakkında Yönetmelik (RG 13.06.2023/32220): https://www.resmigazete.gov.tr/eskiler/2023/06/20230613-1.htm
- 4250 sayılı Kanun: https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=4250&MevzuatTur=1&MevzuatTertip=3
- 6563 sayılı Kanun: https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=6563&MevzuatTur=1&MevzuatTertip=5
- Ticari İletişim Yönetmeliği değişikliği (RG 04.01.2020/30998): https://www.resmigazete.gov.tr/eskiler/2020/01/20200104-2.htm
- Ticaret Bakanlığı, ticari elektronik iletiler: https://ticaret.gov.tr/ic-ticaret/ticari-elektronik-iletiler/genel-bilgiler
- Mesafeli Sözleşmeler Yönetmeliği: https://www.mevzuat.gov.tr/MevzuatMetin/yonetmelik/7.5.20237.pdf
- KVKK yurt dışına aktarım: https://www.kvkk.gov.tr/Icerik/2053/Yurtdisina-Aktarim
- KVKK duyurusu (21.07.2026): https://www.kvkk.gov.tr/Icerik/8830/ucuncu-kisilerden-elde-edilen-kisisel-verilerin-reklam-ve-pazarlama-amacli-kullanilmasina-iliskin-kamuoyu-duyurusu
- KVKK çerez rehberi: https://www.kvkk.gov.tr/SharedFolderServer/CMSFiles/fb193dbb-b159-4221-8a7b-3addc083d33f.pdf
- KVKK ilke kararı 2026/347 (RG 24.03.2026/33203): https://www.resmigazete.gov.tr/eskiler/2026/03/20260324-3.pdf

**[PRATİSYEN]**
- KVKK ilke kararı özeti: https://cottgroup.com/tr/mevzuat/item/kvkk-ilke-karari-aydinlatma-ve-acik-riza-metinlerinin-yonetimi
- 2026 KVKK cezaları: https://cottgroup.com/tr/mevzuat/item/yeniden-degerleme-oranina-gore-2026-yili-kvkk-idari-para-cezalari
- 2026 elektronik ticaret cezaları (arama özetinden): https://www.erdem-erdem.av.tr/bilgi-bankasi/elektronik-ticaret-kanunu-kapsaminda-idari-para-cezalari-2026-yili-icin-guncellendi
- Hekimlerin sosyal medya paylaşımları (01.08.2026): https://www.hukukihaber.net/hekimler-sosyal-medyada-neleri-paylasabilir
- Avukat reklam yasağı ve sosyal medya (arama özetinden): https://www.hukukihaber.net/reklam-yasagi-ve-dijital-ifade-ozgurlugu-kapsaminda-avukatin-internet-ve-sosyal-medya-faaliyetleri
- Kripto ve 7518 (arama özetinden): https://hukukcularevi.com/kripto-varlik-hizmet-saglayici-spk-lisansi-7518/
- Reklam Kurulu kararlarına dava (arama özetinden): https://www.tahanci.av.tr/reklam-kurulu-kararlarina-itiraz-ve-iptal-davasi/
- ETBİS kapsamı (arama özetinden): https://www.ffkpartnerhukuk.com.tr/etbis-kaydi-kimler-icin-zorunludur-2026-guncel-rehber/ · https://birfatura.com/etbis-nedir-kimlere-zorunlu/
- Kilo vermede önce-sonra (satıcı blogu, 24.08.2026): https://www.aimerce.ai/blogs/what-metas-health-wellness-ad-policy-actually-allows
- Sosyal medya çekilişleri ve MPİ izni (07.01.2021, güncelleme 27.12.2022): https://www.hukukihaber.net/sosyal-medyada-yapilan-cekilisler-yasal-midir-belirli-bir-yaptirimi-mevcut-mudur
- İşletmeler için sosyal medya çekilişi koşulları (banka blogu, 09.02.2023): https://www.isbank.com.tr/blog/isletmelere-sosyal-medya-cekilis-yontemleri
- Güzellik salonunda lazer epilasyon, botoks, saç ekimi ve yargı kararları (25.06.2025): https://www.hukukihaber.net/guzellik-merkezleri-lazer-epilasyon-botoks-sac-ekimi-uygulamalarinin-doktorluk-meslegiyle-karsilastirilmasi-ceza-ve-uygulama-sorunlari

**[HABER]**
- Fenomenlere izinsiz çekiliş soruşturması (2021): https://www.ntv.com.tr/galeri/yasam/fenomenlere-izinsiz-cekilis-sorusturmasi,qptN4LCW50OjN4Qgd_2JOA
- SPK kripto ikincil düzenlemeleri; yurt dışı platformun Türkiye'ye yönelik tanıtımı (13.03.2025): https://www.benguturk.com/ekonomi/kripto-varliklara-iliskin-ikincil-duzenlemeler-resmi-gazetede-yayimlandi-188853h
- Doğrulanamayan "3 ihtar" iddiası (06.04.2026): https://we-interactive.com/insights/what-to-do-when-your-ads-get-rejected-a-strategic-recovery-guide-for-2026

**[TOPLULUK]**
- Yanlış işaretleme deneyimi (Mart 2025): https://community.shopify.com/t/meta-ai-incorrectly-restricts-my-account-should-i-keep-running-ads-anyway/397900

---

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
