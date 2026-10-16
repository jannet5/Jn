# 00 Giriş (Neden Görmek İstiyoruz?): denetim raporu

Denetçi notu: ayetler api.quran.com (Uthmani + Diyanet id 77) ile hem kendi betiğimle hem `araclar/ayet_dogrula.py` ile birebir karşılaştırıldı (10 blok, 0 uyarı). Hadisler fawazahmed0 baskıları (eng/ara-muslim, eng/ara-bukhari, eng/ara-tirmidhi, eng-nasai; Müslim için `arabicnumber` = Abdülbâkî numarası) ile, metin ve dereceler yerinde okunarak doğrulandı. Âlim alıntıları açılan kaynaklardan (PDF, djvu.txt, tefsir sayfası) tek tek bulundu.

## Ayetler

| Öğe | Durum | Not |
|---|---|---|
| Kâf 50:20-22 | doğru | Arapça + Türkçe API ile aynı |
| Mülk 67:2 | doğru | Aynı |
| Mü'minûn 23:115 ("ilgili cümle") | doğru | Arapça birebir; Türkçe, API'deki birleşik 114-115 metninden yalnız 115'e ait tırnak içi cümle, "…" ile kısaltılmış; anlam bozulmamış |
| Tekvîr 81:10-14 | doğru | Aynı |
| İnfitâr 82:1-5 | doğru | Aynı |
| İnşikâk 84:1-6 | doğru | API'nin tekrar eden (birleşik) kayıtları ayıklanmış hâliyle aynı |
| Secde 32:17 | doğru | API'deki Diyanet metni ("müjdeyi") aynen |
| Bakara 2:25 | doğru | Aynı |
| Tekâsür 102:1-8 | doğru | API'nin tekrarı ayıklanmış hâliyle aynı |
| Haşr 59:18 | doğru | Aynı |
| Metin içi Bakara 2:3 ("gaybe inanırlar") | doğru | Diyanet metninde aynen "gaybe inanırlar" |
| Metin içi Tekvîr 81:1, 81:2, 81:10-13 | doğru | Diyanet ifadeleriyle uyumlu |
| Perde şemasındaki Kâf 50:22 alıntısı | doğru | "işte senden gaflet perdesini kaldırdık" Diyanet metninde var |

## Hadisler

| Öğe | Durum | Not |
|---|---|---|
| Müslim 2750 (Hanzala) | düzeltildi | Numara tuttu (arabicnumber 2750.01), metin ve "kâtiplerinden" Arapçada doğrulandı; "vahiy kâtiplerinden" ifadesi hadiste yok, "kâtiplerinden" yapıldı. Müslim'de sahih |
| Müslim 867 (hutbe, gözleri kızarma) | düzeltildi | Numara ve metin tuttu (867.01, Cuma kitabı). "ahireti anlatırken" ve "cuma hutbeleri" nitelemesi hadiste yok, "uyarırken / hutbelerini" yapıldı; iki parmak yorumu "âlimler açıklar" diye atfedildi |
| Tirmizî 3333 (Kim kıyameti gözüyle görmek isterse) | düzeltildi | Metin, numara tuttu; Tirmizî "hasen garîb", Elbânî ve Ahmed Şâkir sahih, Zübeyr Ali Zay isnadı hasen (fawaz + Arapça metin). Sure adlarının okunuşundaki hatalı yazım düzeltildi (İzes-semâü'nfetarat) |
| Buhârî 3244; Müslim 2824 (hiçbir göz görmemiş) | doğru | Buhârî 3244 (Bed'ü'l-halk) ve Müslim 2824 (arabicnumber 2824.01) metinleri tuttu, ikisinde de Secde 32:17 okunur; sahih |
| Tirmizî 2307; Nesâî 1824 (lezzetleri yok eden) | düzeltildi | Numaralar tuttu. Derece: Tirmizî "hasen garîb", Elbânî ikisi için "hasen sahih"; Tirmizî'nin kendi hükmü etikete eklendi |
| Tirmizî 2459 (Şeddâd) ve Hz. Ömer'e nisbet edilen söz | doğru | Hadis zayıf (Elbânî, Şâkir, Zübeyr Ali Zay: daif); Ömer sözü mevkuf, Tirmizî senedsiz anar; İbn Kesîr'e göre "meşhur ama munkatı" (Sâbit b. el-Haccâc Ömer'e yetişmemiş; hadithanswers.com sayfasında Arapça alıntıyla görüldü). "Zayıf/delil değil" etiketi yerinde |

## Âlim ve kitap alıntıları

| Öğe | Durum | Not |
|---|---|---|
| Seyyid Kutub, "Kur'an'da Âhiret Âlemi" (1. alıntı) | doğru | Archive.org PDF'te (Kur'an'da Kıyamet Sahneleri, 1. bölüm sonrası) aynı cümle bulundu; OCR bozuklukları düzeltilmiş |
| Seyyid Kutub (2. alıntı, "bugün karşımızda hazır vaziyete getirilmiştir…") | doğru | Aynı PDF'te bulundu |
| Kutub: Edebî Tasvir'e atıf ("tamamen uyar") | doğru | PDF'te "bütün tasvir özellikleri, bilhassa bu sahnelere tamamen uyar" |
| Kutub yorum cümlesi ("edebî güzellikten çok pedagojik tercih") | düzeltildi | Yazarın kendi yorumuydu, Kutub'a ait gibi okunabilirdi; daha yumuşak ve yazarın yorumu olduğu belli ifadeyle değiştirildi |
| İbn Abbas: "isimden başka benzemez" | düzeltildi | Taberî ve İbn Kesîr (quran-tafsir.net) sayfalarında Arapça lafızlar ve isnad (Sevrî ← A'meş ← Ebû Zabyân) bulundu; Arapça metin Türkçe çeviriyle aynı lafza (Ebû Kureyb'in lafzı) getirildi. Ayetin ayrılması ve "şunu söyler" cümlesi düzeltildi. Elbânî'nin sahih demesi İslamweb fetvasında soru-cevapta aktarılıyor (ikincil), metinde "aktarılır" diye bırakıldı. "Renk ve görünüşte benzer" yorumunun İbn Abbas'tan (Süddî yoluyla) geldiği ve Taberî'nin tercihi olduğu İbn Kesîr'de teyit edildi |
| Gazâlî, İhyâ, Zikrü'l-mevt, 1. bölüm (Winter çevirisi) | düzeltildi | djvu.txt'te "Know that death is a terrible and most perilous thing…" ve "contentment and pleasure in the world will wane" bulundu, çeviri doğru. Üç grup (dünyaya dalmış, tövbe eden, ârif) aynı bölümde; "aynı yerde" "aynı bölümde" yapıldı |
| Elmalılı, Kâf 50:22 ("herkeste ahiretten az çok bir gaflet") | doğru (aktarım) | dinirehberim.com sayfasında geçtiği ve Elmalılı'ya atfedildiği görüldü; basılı eserle karşılaştırılmadığı metinde belirtiliyor; notun dili sadeleştirildi |

## Olgusal, kelâmî ve içerik düzeltmeleri

| Öğe | Durum | Not |
|---|---|---|
| Perde şeması: "Bildiğimiz her şey bu kapıdan girer" | düzeltildi | Vahyi (haber-i sadık) bilgi kaynağı sayan Ehl-i Sünnet çizgisiyle çelişiyordu; "Gözlemle öğrendiğimiz her şey…" yapıldı |
| "Yolun iki kanadı" notu: "Hanzala hadisindeki melekler… hem hatırlayıp hem yaşayanlara musafaha eder" | düzeltildi | Hadis bunu söylemiyor (hep zikir hâlinde kalınsaydı musafaha edeceklerini söylüyor); hadisin gerçek mesajı olan "bir saat şunun, bir saat bunun" dengesine çevrildi. Havf "korku" olarak düzeltildi |
| Hanzala yorumundaki tırnaklı "hayat yaşamak için de var" | düzeltildi | Hadis metni değildi; tırnaksız paraphrase yapıldı |
| Sahne ("Önce ışık gidiyor…") | düzeltildi | Üç sureden derlenen sıra, olay sırası iddiası gibi okunuyordu; "Önce" çıkarıldı ve sıranın surelerin okunuş akışı olduğu, zaman sırası iddiası taşımadığı eklendi |
| Tablo: "Cehennemin yeri" | düzeltildi | Yer/mekân tahminine yol açabilirdi; "Cehennemin tasviri, Allah'ın adaleti ve rahmeti" |
| Yolculuk haritası: bölüm numaraları ve başlıklar | doğru | 01-11 dosya başlıklarıyla uyumlu; ahid için "yorum farkı 01. bölümde" notu mevcut |
| Kıyamet tarihi / zayıf rivayet / rüya-kerâmet | doğru | Tarih verilmiyor; zayıf rivayet açıkça etiketli; delil olarak kullanılmıyor |

## HTML sözleşmesi

Sınıflar sözleşmedeki kümeyle sınırlı (alim, ar, ayet, bolum, bolum-baslik, detay, giris, hadis, kutu, kutular, no, not [dikkat/dusun/umut], ozet, ozetle, sahne, sahne-etiket, sema, tr). `<script>`, dış URL, sabit hex renk yok; etiketler dengeli (ayrıştırıcıyla denendi). `figure.sema` 2 adet, role/aria-label var, renkler yalnız CSS değişkeni.

## Şema görsel denetimi (390px, açık + koyu)

`build.py` + `qa-bolum.js` çalıştırıldı: taşan metin yok (over: [], svgTextTasma: []), hata yok; 4 PNG (şema 1 ve 2, açık/koyu) gözle incelendi. Her iki şemada çakışma, kesilme, okunmayan metin yok; koyu temada metinler okunuyor. Düzeltme gerekmedi.

## Türkçe

"Doymadan bırakma, bunalmadan devam etme" çelişkili okunuyordu, "Acele etme" yapıldı; "Hz. Peygamber / Hz. Muhammed (s.a.v.)" kullanımı tutarlı; hadis dereceleri açıklamasına "hafızası sağlam" eklendi; Bakara 2:25 ve İbn Abbas cümlelerinin akışı düzeltildi.

## Kalan riskler

- Elmalılı alıntısı yalnız çevrimiçi aktarımdan doğrulandı (metinde belirtildi).
- İbn Abbas eserinin Elbânî tarafından sahihlenmesi ikincil kaynaktan (İslamweb fetvası) aktarılıyor; Elbânî'nin kendi metni açılmadı.
- Ahmed b. Hanbel Müsned'inde Tirmizî 3333 hadisinin bulunduğu ikincil aktarım; numara verilmedi.
- Mü'minûn 23:115 ayeti, API'deki birleşik Diyanet kaydı yüzünden "ilgili cümle" olarak kısmen alındı.
