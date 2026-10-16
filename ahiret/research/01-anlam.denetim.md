# Denetim raporu: Bölüm 01 (Ben Niçin Buradayım?)

Denetçi: bağımsız denetim ajanı. Tarih: 2026-10-16. Dosya: `bolumler/01-anlam.html`.
Yöntem: `araclar/ayet_dogrula.py` (20 blok, 0 uyarı) + kaynaklara elle dönüş (API, fawazahmed0 hadis JSON'ları, Gutenberg, archive.org, ghazali.org, shamela, dersdunyasi, saidnursi.de, islamqa.org, tefsir API'leri).

## 1. Ayetler

| Öğe | Durum | Not |
|---|---|---|
| 20 `blockquote.ayet` (75:36; 23:114-115; 3:190-191; 7:172; 30:30; 67:2; 76:2-3; 51:56; 51:57-58; 6:162-163; 2:30; 95:4-6; 17:70; 33:72; 84:6; 89:27-30; 13:28; 57:20; 57:22-23; 39:29) | doğru | Arapça (Uthmani) ve Diyanet Türkçesi betikle birebir eşleşti; sure adı/numara doğru. |
| Mü'minûn 23:114-115 Türkçe | düzeltildi | API ve alquran.cloud kaydında "Allah' "Pek az…" şeklinde kaynak verisinden gelen bozuk noktalama vardı; "Allah: "Pek az…" yapıldı. Metin dışında oynanmadı. |
| Metin içi atıflar: Enbiyâ 21:16, Kehf 18:7, Ankebût 29:64, Zilzâl 99:7 | doğru | Alıntı parçaları Diyanet metniyle aynı. |
| Tîn 95:6 ("Umut" notunda tırnaklı alıntı) | düzeltildi | Tırnak içi cümle meal metni değildi (özetti); Diyanet metniyle değiştirildi: "Yalnız, inanıp yararlı iş işleyenler bunun dışındadır. Onlara kesintisiz ecir vardır." |

## 2. Hadisler (7 blok, 8 numara + metin içi atıflar)

| Öğe | Durum | Not |
|---|---|---|
| Buhârî 1385 (fıtrat) | doğru | ara/eng-bukhari numara ve metin tuttu. Sahih. |
| Buhârî 2856 (Muâz, Allah'ın hakkı) | doğru | Metin tuttu. Sahih. |
| Buhârî 2886 (dinarın kulu) | doğru | Metin tuttu. Sahih. |
| Buhârî 3244 ve 7498 (hiçbir gözün görmediği) | doğru | Her iki numarada metin var; hadis-i kudsî. Sahih. |
| Buhârî 6416 (garip/yolcu) | doğru | İbn Ömer'in sözü hadisten ayrı verilmiş, doğru. |
| Müslim 223 (Ebû Mâlik el-Eş'arî, "nefsini satar") | doğru | Abdülbâkî numarası 223, Kitâbü't-Tahâre 1. Sahih. |
| İbn Mâce 4105 (Zeyd b. Sâbit) | doğru | Metin tuttu. Derece: Elbânî sahih, Arnaut sahih, Zübeyr Ali Zai isnadı sahih (fawazahmed0 kayıtları). Anlam özet olarak verilmiş ve etiketlenmiş. |
| "Ben gizli hazineydim" notu | doğru | Hadis olmadığı doğru ve etiketli. İbn Teymiyye "isnadını bilmiyorum" (Fetâvâ c.18 s.122), Zerkeşî (Tezkire s.136), İbn Hacer ve Sehâvî (Makâsıd s.521) islamqa.org aktarımıyla; metinde "doğrudan açılmadı" uyarısı zaten var. |

## 3. Âlim/kitap alıntıları

| Öğe | Durum | Not |
|---|---|---|
| Gazâlî, Kimyâ-yı Saâdet I (Field çevirisi) | doğru | ghazali.org/books/alchemy/tah05.htm: "real self-knowledge consists in knowing…" birebir. Field çevirisinin özet olduğu uyarısı metinde var. |
| Augustinus, İtiraflar I.1 | doğru | Gutenberg #3296 (Pusey): "Thou madest us for Thyself, and our heart is restless, until it repose in Thee." |
| C.S. Lewis, Mere Christianity III.10 | düzeltildi | archive.org djvu metninde alıntı birebir var. Ancak Lewis bunu "The Christian says" diye aktarıyor; giriş cümlesi buna göre düzeltildi ("Hıristiyan şöyle der" ve "en olası açıklama"). Çeviri "satisfaction" anlamını koruyacak şekilde düzeltildi. |
| Frankl, Man's Search for Meaning | düzeltildi | İki cümle de Beacon Press metninde var (religiousnaturalism.org). wist.info bot doğrulaması yüzünden açılmadı. Künyedeki "1959" ve "aslından" ifadeleri yanlış izlenim veriyordu: "Beacon Press baskısının İngilizce metninden Türkçeye çevrilmiştir; sayfa numarası doğrulanmadı" yapıldı. |
| Râgıb, Müfredât "ع-ب-د" | doğru (şartlı) | Arapça cümle shamela.ws/book/8322/74 sayfasında "قال الراغب" diye aynen var. Sayfa eser adını vermiyor; Müfredât'ın kendi sayfası açılamadı. Farsça Müfredât aktarımı da aynı anlamı veriyor. Künyedeki "nakledilen hâliyle" kaydı korundu. |
| Nursî, Yirminci Söz, İkinci Makam (misafirhane; "Din bir imtihandır…") | doğru | dersdunyasi.net sayfasında Yirminci Söz İkinci Makam altında birebir. Anlaşılması için "Sadeleştirirsek…" cümlesi eklendi. |
| Nursî, "Ey insan! Yaptığın hizmet…" | doğru (yer şartlı) | saidnursi.de sayfasında "Onuncu Kelime" başlığı altında birebir. Hangi kitapta olduğu doğrulanamadı; künyedeki "kitaptaki tam yer doğrulanamadı" uyarısı korundu. |
| TDV İslâm Ansiklopedisi, İbadet maddesi | doğru | Araştırma notunda açıldığı kayıtlı; ikili anlam özeti doğru. |
| Tefsir aktarımları (Zâriyât 56: İbn Abbas/Taberî tercihi, Mücâhid, İbn Cüreyc, Ali, Zeyd b. Eslem, Sa'lebî "güzel", umûm-hususî; Ahzâb 72: İbn Abbas, Mücâhid, Saîd b. Cübeyr, Dahhâk, Hasan; Kurtubî "cumhur"; Bakara 30: İbn Kesîr "nesiller"; A'râf 172: Kurtubî "müşkil", Kaffâl, İbn Kesîr-Rûm 30:30) | doğru | quran.com tefsir API'sinde (Taberî 15, Kurtubî 90, İbn Kesîr 14/169) tek tek bulundu. |

## 4. Olgusal iddialar

| Öğe | Durum | Not |
|---|---|---|
| Rib'î b. Âmir'in Rüstem'e cevabı (görev b) | düzeltildi (daha dürüst) | Kaynak durumu doğrulandı: İbn Kesîr, el-Bidâye 7/39 (es-Sa'âde). Metin "قالوا" ile, râvi adı vermeden geliyor; bir önceki anlatı "قال سيف عن شيوخه" diye başlıyor. Takrîb'deki hüküm ("ضعيف الحديث عمدة في التاريخ") aynen doğru. Giriş cümlesine, kaynak künyesine ve nota açıkça "senedi zayıf tarihî rivayet" ifadesi eklendi. Hadis gibi sunulmuyor. |
| Brickman & Campbell 1971 (görev b) | düzeltildi (yumuşatıldı) | Künye doğru (Appley, Adaptation-Level Theory, Academic Press, ss. 287-302). "Çok çabuk alışır, çıta sürekli yükselir" ifadesi abartılıydı; "zamanla alıştığını ve kalıcı doyuma ulaşmanın zor olduğunu" yapıldı, "çoğu zaman" eklendi. "Ölçmeye çalıştı" yerine "inceledi". |
| Diener, Lucas & Scollon 2006 (görev b) | düzeltildi (yumuşatıldı) | Özet doğrulandı: ayar noktası nötr değil, kişiden kişiye değişir, birden fazla olabilir, bazı koşullarda kayar. "Gösterdi" yerine "derlemede savundu"; eğrinin "bilimsel yasa olmadığı" açıkça yazıldı. SVG zaten "ölçülmüş veri değildir" diyor; alt yazı da "çoğu zaman" ile yumuşatıldı. |
| "Fıtrat doyumsuzluğun nedenidir" ve "hiçbir dünyevî hedef kalbi doyurmaz" | düzeltildi | Kesin hüküm gibi okunuyordu; "İslâm'ın bakışıyla… ipucudur" ve "İslâm'a göre" diye yumuşatıldı. |
| Ahid ayeti yorum ayrılığı, imtihan, halife/emanet yorumları | doğru | Ehl-i sünnet ana çizgisinde, ihtilaflar belirtilmiş. Tarih verme, fetva, tekfir yok. |

## 5. HTML sözleşmesi

Doğru. `<script>` yok, dış URL yok, sabit hex renk yok, kapanmayan etiket yok (ayrıştırıcıyla denetlendi), yalnız izinli sınıflar var.

## 6. SVG görsel denetimi

3 şema, açık ve koyu tema (390px) için `build.py` sonrası yeniden alındı (`scratchpad/shots/b-01/`). Taşma yok (`over` ve `svgTextTasma` boş). Gözle: çakışan/kesilen metin yok, koyu temada okunur, hizalama düzgün. Düzeltme gerekmedi.

## 7. Türkçe kalite

Küçük düzeltmeler: pusula benzetmesi akıcılaştırıldı; Nursî "çok sade cümle" ifadesi (cümle eski dilde) "kısa bir benzetme" yapıldı ve sadeleştirme cümlesi eklendi; Lewis çevirisi netleştirildi.

## Kalan riskler

- Nursî "Ey insan!…" alıntısının kitaptaki yeri bilinmiyor (metinde belirtildi).
- Râgıb cümlesi Müfredât'tan doğrudan değil, nakil yoluyla doğrulandı (metinde belirtildi).
- Frankl sayfa numarası doğrulanmadı (metinde belirtildi).
- Diyanet API kaydı bazı ayetlerde kaymış olabilir; bu bölümdeki 20 blokta betik başka sorun bulmadı.
