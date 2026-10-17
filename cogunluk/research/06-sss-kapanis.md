# 06 — Sorular ve Son Söz (araştırma notları)

## Özet
- Bölüm iki kısım: (A) 8 sık sorulan soru (`details.detay`), (B) kapanış: üç ilke + "Paylaşmak istersen: düzeltilmiş kart" (SVG, 14 satır) + "üç ilke" şeması (SVG).
- Sayılar/atıflar yerel veriden (kuran_tam.json, ekser_ayetleri_tam.json = 81 ayet) ve bölüm 00 notlarından alındı; 14 maddenin hepsini ayrıca ayet.py ile kendim yeniden doğruladım. **Bölüm 00 ile çelişki yok**: tek atıf hatası "Mâide 48" -> 5:49; 5:49 ve 10:92 Arapçada `kesîr` ("birçoğu"), diğer 12'si `ekser`.
- Ek bulgular (bu bölüme özgü): (1) Taberî 6:116'da "çoğunluk" kaydının nedenini açıkça yazar: o gün yeryüzündekilerin çoğu kâfir/dalâlette idi; Kurtubî "yeryüzündekilerin çoğu" ifadesini "yani kâfirler" diye açıklar. (2) Taberî, 2:88'deki "fe-kalîlen mâ yü'minûn"u iki şekilde aktarır: "içlerinden ancak az kişi iman eder" (Katâde) ve "ellerindekinin ancak azına inanırlar"; kendisi ikincisine yakın bir okumayı (az bir iman) tercih eder ve "az kişi" okumasını dilbilgisi gerekçesiyle zayıf bulur. Yani Bakara 2:88 ve Nisâ 4:46'daki "az" bir sayım değil, bir iman derecesi olarak da okunmuştur. (3) Gazâlî, İhyâ'da muhâsebeyi "iş yapılmadan önce durup bakma" ve "iş sonrası hesap" diye iki yere koyar; Hucurât 49:6'daki tebeyyün ile paralel.

## Doğrulanmış ayetler (Diyanet meali; ayet.py / kuran_tam.json)
| Ref | Not |
|---|---|
| 6:116 | "Yeryüzündekilerin çoğunluğuna itaat edersen seni Allah yolundan saptırırlar. Onlar ancak zanna uyarlar, sadece tahminde bulunurlar." |
| 42:38 | Arapça: وَأَمْرُهُمْ شُورَىٰ بَيْنَهُمْ — "Onların işleri aralarında danışma iledir." (Diyanet kaydı 42:36-38'i 38'de birleştirir; bölümde kısmi alıntı + tırnak.) |
| 3:159 | "...iş hakkında onlara danış, fakat karar verdin mi Allah'a güven..." |
| 49:6 | "Ey inananlar! Eğer yoldan çıkmışın biri size bir haber getirirse, onun iç yüzünü araştırın, yoksa bilmeden bir millete fenalık edersiniz de sonra ettiğinize pişman olursunuz." (blok) |
| 49:11 | Bir topluluk bir diğerini alaya almasın, belki de onlar kendilerinden daha iyidirler... (blok) |
| 17:36 | "Bilmediğin şeyin ardına düşme..." |
| 34:13 | "Kullarımdan şükredenler pek azdır." (Diyanet kaydı 34:12-13 birleşik) |
| 11:40 | "Pek az kimse onunla beraber inanmıştı." |
| 38:24 | "...inanıp yararlı iş işleyenler bunun dışındadır ki sayıları da ne kadar azdır!" |
| 2:88 | "Onların pek azı inanırlar." |
| 4:46 | "Onların ancak pek azı inanır." |
| 2:249 | "Nice az topluluk çok topluluğa Allah'ın izniyle üstün gelmiştir, Allah sabredenlerle beraberdir." |
| 59:18 | "...herkes yarına ne hazırladığına baksın..." (blok) |
| 103:1-3 | "...birbirlerine gerçeği tavsiye edenler ve sabırlı olmayı tavsiye edenler bunun dışındadır." |
| 16:125 | "...hikmetle, güzel öğütle çağır; onlarla en güzel şekilde tartış..." |
| 12:103 | "...sen ne kadar yürekten istersen iste, insanların çoğu inanmazlar." (Diyanet kaydı 12:102-103 birleşik) |
| 14 liste ayeti | 16:83, 5:49, 30:42, 17:89, 10:92, 26:221-223, 10:36, 25:50, 2:243, 2:100, 43:78, 12:106, 41:4 (41:2-5 birleşik), 5:103 — kart için yeniden doğrulandı |

## Doğrulanmış hadisler
| TR anlam | Kaynak + no | Derece | Doğrulama |
|---|---|---|---|
| "İnsanlar helâk oldu" diyen, onların en çok helâk olanıdır (Ebû İshak: ahlekühüm / ahlekehüm tereddüdü) | Müslim 2623 | Sahih (Sahîh-i Müslim) | fawazahmed0 ara-muslim/eng-muslim hadithnumber 6683, arabicnumber 2623.01 |
| Bir adam kardeşine "ey kâfir" derse, ikisinden birine döner (bir rivayette: söylediği gibiyse öyle, değilse söz kendi üzerine döner) | Müslim 60 | Sahih | eng-muslim 215/216, arabicnumber 60.01/60.02; tur-muslim aynı kayıt |
| "İslâm garip (yabancı) olarak başladı, başladığı gibi garip olarak dönecek; ne mutlu gariplere" | Müslim 145 (Ebû Hureyre) | Sahih | ara-muslim/eng-muslim hadithnumber 372, arabicnumber 145; tur-muslim |
| Peygamber'e ümmetler gösterildi: bir peygamber yanında bir iki kişi, biri yanında hiç kimse yok... ümmeti büyük kalabalık; 70.000 kişi hesapsız cennete | Buhârî 5752 (İbn Abbâs) | Sahih (Sahîh-i Buhârî) | eng-bukhari/ara-bukhari hadithnumber 5752 (kitap 76, hadis 67); 6541'de de benzer rivayet |
| "Kişiye (günah/yalan olarak) yeter ki duyduğu her şeyi anlatsın" | Ebû Dâvûd 4992 (Ebû Hureyre) lafız: "kefâ bi'l-mer'i ismen..." (günah); Müslim, Mukaddime'de "kezib" lafzıyla | Sahih (Elbânî, Muhyiddin Abdulhamid, Zübeyr Ali Zai — eng/ara-abudawud grades alanı) | eng-abudawud/ara-abudawud 4992; Müslim Mukaddime lafzı tur-muslim giriş kaydı 7'de ("Her işittiğini söylemek, insana yalan olarak yeter") görüldü; Mukaddime numarası ikincil aramada "5" çıktı, metinde yalnızca "Müslim, Mukaddime" yazıldı |

## Âlim/kitap alıntıları (gerçekten açılan)
- **Taberî, Câmiu'l-beyân, 6:116** (quran.com API tefsir id 15, https://api.quran.com/api/v4/tafsirs/15/by_ayah/6:116): "...وإنما قال الله لنبيه: (وإن تطع أكثر من في الأرض) من بني آدم, لأنهم كانوا حينئذ كفارًا ضلالا" — "Allah Peygamberine 'yeryüzündekilerin çoğu' dedi, çünkü o sırada onlar kâfir ve sapkındı." (Taberî, ayrıca bunların 'delilsiz zan' ile hareket ettiğini söyler.)
- **Kurtubî, 6:116** (id 90, https://api.quran.com/api/v4/tafsirs/90/by_ayah/6:116): "وإن تطع أكثر من في الأرض أي الكفار" — "yani kâfirler"; "yehrusûn: tahmin ederler".
- **İbn Kesîr, 6:116** (Arapça id 14 / kısaltılmış İngilizce id 169): "Allah, yeryüzü ahalisinin çoğunun hâlini, dalâlet olarak bildirir"; Sâffât 37:71 ve Yûsuf 12:103'e atıf. İngilizce: "Allah states that most of the people of the earth, are misguided."
- **İbn Kesîr, 42:38** (id 14 ve 169): "lâ yübrimûne emran hattâ yeteşâvarû fîh" — "birbirleriyle danışmadan bir işi kesinleştirmezler"; 3:159'a atıf; Hz. Ömer'in ölürken halifeyi altı kişilik şûraya bırakması (Osman, Ali, Talha, Zübeyr, Sa'd, Abdurrahman b. Avf).
- **İbn Kesîr (kısaltılmış), 34:13**: "But few of My servants are grateful. This is a reflection of reality." ve Fudayl rivayeti (Dâvûd: "Şükrü sen verdin, nasıl şükredeyim?"; cevap: "şimdi şükrettin").
- **Taberî, 2:88**: iki yorum (az kişi / az iman); kendisinin tercihi "az iman" yönünde (gramer gerekçesi: "kalîl" mansub, mastar sıfatı).
- **Taberî, 4:46**: "...ilâ îmânen kalîlen" — "ancak az bir iman"; Katâde: "ancak az kişi iman eder".
- **Nevevî, Şerhu Müslim (Müslim 2623 şerhi)**, https://hadithprophet.com/hadith-52009.html (açıldı): "واتفق العلماء على أن هذا الذم إنما هو فيمن قاله على سبيل الإزراء على الناس واحتقارهم وتفضيل نفسه عليهم وتقبيح أحوالهم لأنه لا يعلم سر الله في خلقه" — "Âlimler, bu yermenin ancak bunu insanları hor görme, küçümseme, kendini onlardan üstün tutma ve hallerini çirkin gösterme yoluyla söyleyen hakkında olduğunda birleştiler; çünkü o, Allah'ın yaratıkları içindeki sırrını bilmez." Devamı: "dinde kendisinde ve insanlarda gördüğü eksikten dolayı üzülerek söyleyenin ise bir sakıncası yoktur."
- **Gazâlî, İhyâ-u ulûmi'd-dîn, Kitâbü'l-murâkabe ve'l-muhâsebe** (Şâmile baskısı, c.4; https://shamela.ws/book/9472/1563 -> c.4 s.404; https://shamela.ws/book/9472/1559 ve /1560 -> s.400-401; https://shamela.ws/book/9472/1557 -> s.398):
  - s.404: "ولذلك قال عمر رضي الله عنه: حاسبوا أنفسكم قبل أن تحاسبوا وزنوها قبل أن توزنوا" — "Bu yüzden Ömer (r.a.) dedi ki: hesaba çekilmeden önce kendinizi hesaba çekin, tartılmadan önce kendinizi tartın." (Gazâlî bunu 59:18'in muhâsebeye işaret ettiğini söyledikten sonra aktarır.)
  - s.404: Hasan-ı Basrî: "المؤمن قوام على نفسه يحاسبها لله، وإنما خف الحساب على قوم حاسبوا أنفسهم في الدنيا" — "Mümin, nefsinin başında duran kimsedir; onu Allah için hesaba çeker. Hesap, dünyada kendilerini hesaba çekmiş bir topluluğa hafif olur."
  - s.400: "فينظر ... أهو لله خاصة أو هو في هوى النفس ومتابعة الشيطان فيتوقف فيه ويتثبت حتى ينكشف له ذلك بنور الحق" ve s.401: "فيتوقف عن الهم وعن السعي حتى ينكشف له بنور العلم أنه لله تعالى فيمضيه أو هو لهوى النفس فيتقيه" — "Önce şuna bakar: Bu, yalnızca Allah için mi, yoksa nefsin hevâsı ve şeytana uymak mı? Durur, iyice sağlama alır (yetesebbet), ta ki hak nuruyla / ilim nuruyla kendisine açık olsun; Allah içinse yürütür, nefsin hevâsı içinse sakınır."
- **TDV İslâm Ansiklopedisi, "Elmalılı Muhammed Hamdi"** (https://islamansiklopedisi.org.tr/elmalili-muhammed-hamdi, açıldı): Hak Dini Kur'an Dili'ni Diyanet İşleri Reisliği teklif etti; 48 yaşında başlayıp 60'ında tamamladı; ilk baskı Diyanet, İstanbul 1935-1938; rivayet ve dirayet metodu; kaynaklar arasında Taberî, Zemahşerî, Râgıb el-İsfahânî, Fahreddin er-Râzî, Ebû Hayyân, Âlûsî; fıkhî konularda genellikle Hanefî kaynaklar.

## Kaynak önerileri için açılan/erişilebilir bağlantılar
- Taberî, İbn Kesîr (Arapça + kısaltılmış İngilizce), Kurtubî: quran.com API tefsir uç noktası — `https://api.quran.com/api/v4/tafsirs/{15|14|169|90}/by_ayah/{sure:ayet}` (hepsi açıldı, metin okundu).
- Taberî ayrıca altafsir.com: https://www.altafsir.com/Tafasir.asp?tMadhNo=1&tTafsirNo=1&tSoraNo=6&tAyahNo=116&tDisplay=yes&Page=1&Size=1&LanguageId=1 (açıldı; sayfada Taberî, vefat 310).
- Râzî, Mefâtîhu'l-gayb: archive.org kayıtları açıldı (cilt cilt tam metin): https://archive.org/details/mftghyb (Seyyid İmrân tahkîki, 16 cilt PDF + djvu.txt), https://archive.org/details/raaazi1. Not: djvu.txt OCR'ı gürültülü; 6:116 pasajı bulunamadı, Râzî'den alıntı yapılmadı.
- Elmalılı: TDV İA maddesi açıldı; çevrim içi tam metin bağlantısı doğrulanamadı -> basılı/yayımcı baskısı önerilir.
- Kur'an Yolu (Diyanet): arama sonuçları DİB yayını olduğunu (I-V cilt, heyet çalışması) gösterdi; Diyanet'in Kur'an-ı Kerim Portalı ana sayfası açıldı (https://kuran.diyanet.gov.tr/ — SPA, ayet ayet tefsir içeriği bu çalışmada okunamadı). "Hangi hocalar" listesi doğrulanamadığı için isim verilmedi.
- Not (arama sonucu, ikincil): Kur'an Yolu hakkında akademik değerlendirmeler klasik Sünnî çizgide olduğunu, ama bazı yeni bakışlar içerdiğini söyler; Diyanet yayını olması ayrıca "resmî akaid" anlamına gelmez (ikincil kaynak, kesin cümle kurulmadı).

## Kart (SVG) için karar notları
- 14 satır; her satır: doğru sure:ayet + Diyanet'in ve Arapçanın söylediği kadarı + "Kime" notu. 5:49 satırında "listede 48 yazıyor" düzeltmesi + Arapça `kesîr`; 10:92 de `kesîr`.
- 30:42'de fiil geçmiş zaman ("müşrik idi"), yani listedeki "müşriktir" kaydı gevşek.
- 17:89 ve 25:50'de Diyanet "nankör"; listede 17:89 "inkârcı", 25:50 "nankör".
- 43:78'de "çoğunuz" (ikinci çoğul şahıs) — dönem muhataplarına hitap.
- Bölüm 02'nin sınıflaması (yazılmamış olabilir) ile çelişirsem kendi doğrulamam esas; çelişki beklenmiyor.

## Üretim notu
- Bölüm HTML: /home/user/Jn/cogunluk/bolumler/06-sss-kapanis.html (2284 kelime; 4 ayet bloğu [49:6, 6:116, 49:11, 39:17-18], 2 hadis bloğu [Buhârî 5752, Müslim 2623], 2 âlim bloğu [Gazâlî, Nevevî], 8 details.detay, 2 SVG: üç ilke şeması + düzeltilmiş kart). Ayet blokları ayet.py ile üretildi. SVG'ler cairosvg ile açık/koyu renkte çizilip kontrol edildi; metin genişlikleri DejaVu Sans ile ölçüldü.
- Bölüm 02 yazılırken henüz yoktu; kart yalnız bölüm 00 (research/00-liste.md ve bolumler/00-liste.html) ve kendi doğrulamamla karşılaştırıldı: çelişki yok.

## Zayıf/tartışmalı
- "İcmâ/cemaat" hadisleri (Tirmizî 2167 / İbn Mâce 3950 vb.) bu bölümde alıntılanmadı; bölüm 04'e atıf yapıldı (derece tartışması orada).
- Müslim Mukaddime hadisinin standart numarası ikincil aramada "5" çıktı; birincil doğrulama yalnızca metin düzeyinde (tur-muslim girişi). Metinde numara verilmedi; Ebû Dâvûd 4992 esas alındı.
- Müslim 145 (garip hadisi): metin doğrulandı; şerhleri bu çalışmada açılmadı, yorum cümleleri "hadisin lafzı şunu söylüyor, şunu söylemiyor" düzeyinde tutuldu.
- Sunnah.com 403 verdi (WebFetch), kullanılmadı.

## Açılan kaynak URL'leri
- /home/user/Jn/cogunluk/veri/kuran_tam.json, ekser_ayetleri_tam.json; /home/user/Jn/cogunluk/research/00-liste.md
- https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/{eng,ara,tur}-muslim.json, {eng,ara,tur}-bukhari.json, {eng,ara}-abudawud.json
- https://api.quran.com/api/v4/tafsirs/{15,14,169,90}/by_ayah/{6:116, 34:13, 5:49, 42:38, 4:46, 2:88}
- https://shamela.ws/book/9472/{1556..1599} (İhyâ); https://hadithprophet.com/hadith-52009.html; https://islamansiklopedisi.org.tr/elmalili-muhammed-hamdi; https://archive.org/details/mftghyb; https://kuran.diyanet.gov.tr/
