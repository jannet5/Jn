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
