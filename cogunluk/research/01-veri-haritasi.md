# 01 — Kur'an "Çoğu" Derken: Veri Haritası (araştırma notları)

Hazırlayan: bölüm yazarı · Hesaplar: `veri/analiz.py` → `veri/analiz.json` (tekrarlanabilir; `python3 -I veri/analiz.py`). Sınıflama (yüklem, özne türü, kim) bu çalışmanın yazarına aittir; kod yalnızca sayar, doğrular (assert) ve SVG çizer.

## Özet

- Arapçasında **أكثر** ("daha çok / çoğu") kalıbı geçen ham eşleşme: **81** ayet. Elle okunup ayıklandı: **68** ayette gerçekten "çoğu şöyledir" türünden bir çoğunluk hükmü var; **13** ayette kalıp başka anlamda (karşılaştırma: "daha çok mal"; fiil: "çok tartıştın"; konu çokluğu).
- Bu 68 ayet **33** surede dağılır; en yoğun: Şuarâ 9, Yûsuf 6, A'râf 4, Nahl 4, En'âm 3.
- **En sık yüklem "bilmez"**: 27/68 (%40); ardından "iman etmez" 14, "şükretmez" 6. Bilmez+iman etmez+şükretmez+akletmez = 51/68: ağırlık **bilgi/idrak/iman/şükür** eksenindedir; "fasık" 4, "kâfir/nankör/yalancı" 4, "ortak koşar" 3, "haktan hoşlanmaz" 2, "zanna uyar" 2, "yüz çevirir" 1.
- **Kim için?** Lafzen "insanların çoğu" (A): **21**; zamirle ama öncülü genel insan (A*): **5**; belirli topluluk (B): **40**; öncülü belirsiz (?): **2**. Genel insan toplamı 26/68, belirli topluluk 40/68. B içinde: Mekkeli/Arap müşrikler ve inkârcılar 23, Kitap ehli 3, geçmiş kavim/ümmet 11, diğer belirli grup 3.
- **Olumlu-nötr "çoğu" (ekser) ayeti: 0.** Vâkıa 56:13-14 ve 39-40 ekser/kesîr değil, `ثلة` (sülle, topluluk) kullanır; Mâide 5:66 ise kesîr kalıbıdır. Olumlu-nötr örnekler ancak kesîr kalıbında (4 ayet + 2 karışık) ve "az" taramasında çıkar.
- **İki kalıp ayrı sayıldı:** ekser 81 ham → 68 hüküm (hepsi olumsuz; 26 genel, 40 belirli); kesîr 71 ayet → 29 kişilere ilişkin hüküm (23 olumsuz, 4 olumlu-nötr, 2 karışık; 9 genel, 20 belirli; Mekkî 10 / Medenî 19). Paylaşılan 14 atfın 12'si ekser, 2'si (5:49, 10:92) kesîr.
- **Mekkî/Medenî** (api.quran.com surelerin `revelation_place` alanı): hüküm ayetleri Mekkî 59, Medenî 9 (Mekkî payı %87). Kur'an'ın kendi ayet dağılımı: Mekkî 4613 / Medenî 1623 (Mekkî payı %74); sure sayısı Mekkî 86 / Medenî 28. Yani çoğunluk ifadeleri Mekkî surelere biraz daha yaslanır (Mekkî sureler tevhid/inkâr/tebliğ tartışmalarını işler). Ra'd gibi bazı surelerde Mekkî/Medenî ayrılığı âlimler arasında tartışmalıdır.
- **"Az" kalıbı** (قليل/قلة): 71 eşleşme, 1 yanlış eşleşme (35:18). Türler: az kişi (azınlık) 20, az davranış (az şükür/iman/öğüt) 13, az bedel (ثمنا قليلا) 9, az zaman/geçici zevk 20, az şey/miktar/algı 8. "Azınlık" anlamında olan yalnızca "az kişi" grubu (20 ayet); bunların çoğunda azınlık **iman eden/şükreden/doğru yolda kalan** taraftır (Sebe' 34:13, Hûd 11:40, Sâd 38:24 …).

## Yöntem

1. `ekser_ayetleri_tam.json` (81 ham eşleşme) tek tek okundu; Arapçada أكثر geçen her ayet için meal, bağlam ve (gerekince) İbn Kesîr tefsiri (quran.com tafsir id 169) kontrol edildi.
2. Her ayete: **yüklem** (birincil + varsa ikinci), **özne türü** (A / A* / B / ?), **kim** (B için), **güven** (yüksek/orta/düşük) yazıldı. Belirsiz olan `?` bırakıldı.
3. A ölçütü kodla doğrulanır: Arapçada lafzen `أكثر الناس` ya da `أكثر من في الأرض` geçen ayetler tam olarak A olarak etiketlenenlerdir (assert).
4. Kod: 81 = 68 + 13 (assert), özne ve yüklem toplamları 68 (assert), her ham ayetin Arapçasında كثر var (assert).
5. Mekkî/Medenî: `https://api.quran.com/api/v4/chapters?language=tr` → `revelation_place` (makkah/madinah). Sure düzeyindedir; "Medenî surede Mekkî ayet" ayrımı yapılmadı.
6. Karşılaştırma tabanı: bütün Kur'an 6236 ayet (kod doğrular).

## Sözlük doğrulaması: "ekser" ne demek?

Kök ك-ث-ر (kesret = çokluk). `أكثر` (ekser) bu kökün üstünlük/karşılaştırma kalıbıdır: "daha çok / en çok / çoğu". Kişiler için `أكثرهم` "onların çoğu" demektir. Açılan sayfa: https://arabiclexicon.hawramani.com/%D9%83%D8%AB%D8%B1/ (WebFetch + curl ile metin doğrulandı).

- **Cevherî, es-Sıhâh (Tâcü'l-Lüga), "كثر"**: `الكثرة: نقيض القلة ... وقد كثر الشيء فهو كثير` — kesret, kıllet'in (azlığın) zıddıdır; كثير bu köktendir (=çok)
- **İbn Manzûr, Lisânü'l-Arab, "كثر"**: `والكُثْرُ معظم الشيء وأَكْثَرُه` — "kuthr": bir şeyin büyük kısmı, çoğu; "ekser" bu anlamda çoğunluğu gösterir
- **E. W. Lane, Arabic-English Lexicon, "كُثْرٌ"**: `The greater, or greatest, or main, part, of a thing; the most thereof.` — aynı
- **Râgıb el-İsfahânî, el-Müfredât fî Garîbi'l-Kur'ân, "كثر"**: `الْكِثْرَةَ والقلّة يستعملان في الكمّيّة المنفصلة كالأعداد ... وليست الْكَثْرَةُ إشارة إلى العدد فقط بل إلى الفضل` — kesret/kıllet ayrık nicelikte (sayılarda) kullanılır; kesret yalnızca sayıya değil, bolluk ve fazlalığa da işaret edebilir. Râgıb örnek olarak 5:64, 23:70 (أكثرهم للحق كارهون), 21:24, 2:249, 4:1 ve 2:109 (كثير من أهل الكتاب) ayetlerini anar.
- Kaynak sayfası: Sıhâh, Lisân, Lane ve Müfredât metinleri hawramani.com'daki 'كثر' maddesinden okundu (WebFetch + curl ile Arapça metin doğrulandı).

**Ek dilbilgisi notu (önemli):** Kur'an'da "çoğu" iki ayrı kalıpla gelir: **أكثر** (ekser, "çoğu/çoğunluk" — ana konu) ve **كثير** (kesîr, "çok, birçok"; `كثير من` = "…lerden birçoğu"). Diyanet meali ikisini de bazen "çoğu" diye çevirir (ör. 5:49 `كثيرا من الناس` = "insanların çoğu"; 22:18'de aynı kalıp "birçoğu"). أكثر (ekser) = üstünlük/karşılaştırma kalıbı: "daha çok / çoğu / en çoğu"; ayrıca كُثْر (kuthr) "bir şeyin büyük kısmı". كثير (kesîr) = "çok, bol, birçok" sıfatı; كثير من = "…lerden birçoğu". Kesîr "çoğunluk (%50'den fazla)" demeyi tek başına gerektirmez; ekser "çoğunu/en çoğunu" göstermeye daha yakındır. Diyanet meali ikisini de "çoğu" diye çevirebilir. Paylaşılan 14'lü listede 12 ayet ekser, 2 ayet (5:49 ve 10:92) kesîr kalıbıdır. Cevherî'nin Sıhâh'ı kesret'i "kıllet'in zıddı" der; Râgıb el-İsfahânî kesret ile kıllet'in "ayrık nicelikte, sayılarda" kullanıldığını, kesret'in "yalnızca sayıya değil, fazilete/bolluğa da işaret edebileceğini" söyler (örnekleri arasında 2:109 `كثير من أهل الكتاب`, 2:249 `فئة كثيرة`, 23:70 `أكثرهم للحق كارهون` vardır).

## 1. Ayet ayet sınıflama (81 ham eşleşme)

Özne: A = lafzen "insanların çoğu"; A* = zamirle, öncül genel insan; B = belirli topluluk; ? = belirsiz. Güven: y=yüksek, o=orta, d=düşük.

| # | Ayet | Sure (yer) | Arapça (kalıp çevresi) | Diyanet (kısaltılmış) | Yüklem | Özne | Kim / not | G |
|---|---|---|---|---|---|---|---|---|
| 1 | 2:100 | Bakara (Medenî) | ۚ بَلْ أَكْثَرُهُمْ لَا يُؤْمِنُونَ | Onlar, her ne zaman bir ahidde bulunmuşlarsa içlerinden bir takımı onu bozmamış mıdır? Zaten onların çoğu inanmazlar. | iman etmez | B | Kitap ehli — ahdi bozan Yahudi topluluğu (Medine, 2:97-101) | y |
| 2 | 2:243 | Bakara (Medenî) | ٱلنَّاسِ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَشْكُرُونَ | Binlerce kişinin memleketlerinden ölüm korkusuyla çıktıklarını görmedin mi? Allah onlara "Ölün" dedi. Sonra onları diriltti. Allah… | şükretmez | A | insanlar (genel) — لا يشكرون | y |
| 3 | 3:110 | Âl-i İmrân (Medenî) | مِّنْهُمُ ٱلْمُؤْمِنُونَ وَأَكْثَرُهُمُ ٱلْفَـٰسِقُونَ | Siz, insanlar için ortaya çıkarılan, doğruluğu emreden, fenalıktan alıkoyan, Allah'a inanan hayırlı bir ümmetsiniz. Kitap ehli ina… | fasık | B | Kitap ehli — "Kitap ehli... içlerinde inananlar var, çoğu yoldan çıkmıştır" [aynı cümlede "minhumu'l-mu'minûn" (içlerinden inananlar da var)] | y |
| 4 | 4:12 | Nisâ (Medenî) | فَإِن كَانُوٓا۟ أَكْثَرَ مِن ذَٰلِكَ فَهُمْ | Kadınlarınızın çocukları yoksa bıraktıklarının yarısı sizindir, çocukları varsa, bıraktıklarının ettikleri vasiyetten veya borçtan… | — (kapsam dışı) | — | karşılaştırma: "bundan fazla" (miras payı) | — |
| 5 | 5:59 | Mâide (Medenî) | قَبْلُ وَأَنَّ أَكْثَرَكُمْ فَـٰسِقُونَ | De ki, "Ey kitap ehli! Allah'a, bize indirilene ve daha önce indirilene inanmamızdan ve çoğunuzun fasık olmasından ötürü mü bizden… | fasık | B | Kitap ehli — "Ey Kitap ehli! ... çoğunuzun fasık olması" | y |
| 6 | 5:103 | Mâide (Medenî) | ٱلْكَذِبَ ۖ وَأَكْثَرُهُمْ لَا يَعْقِلُونَ | Allah, kulağı çentilen, salıverilen, erkek dişi ikizler doğuran, on defa yavrulamasından ötürü yük vurulmayan hayvanların adanması… | akletmez | B | Mekkeli/Arap müşrikler ve inkârcılar — bahîra, sâibe, vasîle, hâm adakları: cahiliye uygulaması | o |
| 7 | 6:37 | En'âm (Mekkî) | ءَايَةً وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَعْلَمُونَ | "Rabbinden ona (Muhammed'e) bir belge indirilseydi ya" dediler. De ki: "Doğrusu Allah bir belge indirmeye Kadir'dir, fakat çoğu bi… | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — "Ona Rabbinden bir mucize indirilseydi" diyenler | y |
| 8 | 6:111 | En'âm (Mekkî) | ٱللَّهُ وَلَـٰكِنَّ أَكْثَرَهُمْ يَجْهَلُونَ | Eğer biz onlara melekleri indirsek, ölüler onlarla konuşsa ve her şeyi karşılarına toplasaydık, Allah dilemedikçe, yine de inanmaz… | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — بل... يجهلون: melekler inse de inanmazlardı | y |
| 9 | 6:116 | En'âm (Mekkî) | وَإِن تُطِعْ أَكْثَرَ مَن فِى ٱلْأَرْضِ | Yeryüzündekilerin çoğunluğuna itaat edersen seni Allah yolundan saptırırlar. Onlar ancak zanna uyarlar, sadece tahminde bulunurlar… | zanna uyar (+saptırır) | A | yeryüzündekiler (genel) — إن يتبعون إلا الظن [ayetin asıl hükmü bir uyarıdır: çoğunluğa itaat edersen seni saptırırlar] | y |
| 10 | 7:17 | A'râf (Mekkî) | وَلَا تَجِدُ أَكْثَرَهُمْ شَـٰكِرِينَ | "Beni azdırdığın için, and olsun ki, Senin doğru yolun üzerinde onlara karşı duracağım; sonra önlerinden, ardlarından, sağ ve soll… | şükretmez | A* | Âdemoğulları (İblis'in iddiası) — لا تجد أكثرهم شاكرين [söyleyen İblis'tir; Kur'an bunu 34:20'de "iman edenlerden bir topluluk hariç" diyerek aktarır] | y |
| 11 | 7:102 | A'râf (Mekkî) | وَمَا وَجَدْنَا لِأَكْثَرِهِم مِّنْ عَهْدٍ ۖ | Onların çoğunda ahde bağlılık görmedik, çoğunu fasık kimseler olarak bulduk. | fasık (+ahde vefasız) | B | geçmiş kavim/ümmet — önceki şehir/ülke halkları (7:94-102); "çoğunda ahde bağlılık görmedik" | y |
| 12 | 7:131 | A'râf (Mekkî) | ٱللَّهِ وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَعْلَمُونَ | Onlara bir iyilik geldiği zaman; "Bu bizden ötürüdür" derler, bir fenalığa uğrarlarsa da, Musa ve onunla beraber olanların uğursuz… | bilmez | B | geçmiş kavim/ümmet — Firavun'un kavmi | y |
| 13 | 7:187 | A'râf (Mekkî) | ٱللَّهِ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | Sana, kıyamet saatinin ne zaman gelip çatacağını soruyorlar, de ki: "Onu ancak Rabbim bilir, onun vaktini, O'ndan başka belirtecek… | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 14 | 8:34 | Enfâl (Medenî) | ٱلْمُتَّقُونَ وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَعْلَمُونَ | Yoksa Mescidi Haram'a girmekten menederlerken Allah onlara niçin azab etmesin? Hem de O'nun dostu değiller; O'nun dostları ancak k… | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — Mescid-i Harâm'dan engelleyenler; "Allah'ın dostları değiller" | y |
| 15 | 9:8 | Tevbe (Medenî) | وَتَأْبَىٰ قُلُوبُهُمْ وَأَكْثَرُهُمْ فَـٰسِقُونَ | Nasıl olabilir ki, size üstün gelselerdi ne bir yakınlık, ne de bir ahd gözetirlerdi. Kalpleriyle istemezlerken sizi ağızlarıyla h… | fasık | B | Mekkeli/Arap müşrikler ve inkârcılar — ahit yapılan müşrikler (9:1-8) [Tevbe 9:7-8: ahdi gözetmeyen müşrikler; "çoğu fasık" (azı ahde bağlı: 9:7)] | o |
| 16 | 9:69 | Tevbe (Medenî) | مِنكُمْ قُوَّةً وَأَكْثَرَ أَمْوَٰلًا وَأَوْلَـٰدًا فَٱسْتَمْتَعُوا۟ | Ey ikiyüzlüler! Siz, sizden önce daha kuvvetli, malları ve çocukları daha çok olup, hisselerince bunlardan faydalanan kimseler gib… | — (kapsam dışı) | — | karşılaştırma: "malları ve çocukları daha çok" | — |
| 17 | 10:36 | Yûnus (Mekkî) | وَمَا يَتَّبِعُ أَكْثَرُهُمْ إِلَّا ظَنًّا ۚ | Onların çoğu zanna uyarlar; gerçekte ise zan, hakikat karşısında bir şey ifade etmez. Allah, yaptıklarını şüphesiz bilir. | zanna uyar | B | Mekkeli/Arap müşrikler ve inkârcılar — "ortaklarınızdan hakka ileten var mı" (10:35) bağlamı | o |
| 18 | 10:55 | Yûnus (Mekkî) | حَقٌّ وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَعْلَمُونَ | İyi bilin ki, Allah'ın verdiği söz gerçektir, ama çoğu bunu bilmez. | bilmez | ? | öncül belirsiz (çevrede inkârcılar/zalimler) — لا يعلمون [10:54 zalimlerden söz eder; zamirin öncülü açık değil] | d |
| 19 | 10:60 | Yûnus (Mekkî) | ٱلنَّاسِ وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَشْكُرُونَ | Allah'a karşı yalan uyduranlar kıyamet gününü ne zannederler? Doğrusu Allah'ın insanlara olan nimeti boldur, fakat çoğu şükretmezl… | şükretmez | A* | insanlar (öncül: "insanlara nimeti boldur") — لا يشكرون [aynı bağlamda "Allah'a yalan uyduranlar" (10:59-60) geçer] | o |
| 20 | 11:17 | Hûd (Mekkî) | رَّبِّكَ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يُؤْمِنُونَ | Rabbinin katından bir belgesi ve onun arkasından da bir şahidi olanlar, önlerinde de Musa'nın Kitap'ı önder ve rahmet olarak bulun… | iman etmez | A | insanlar (genel) — لا يؤمنون | y |
| 21 | 11:32 | Hûd (Mekkî) | قَدْ جَـٰدَلْتَنَا فَأَكْثَرْتَ جِدَٰلَنَا فَأْتِنَا بِمَا | "Ey Nuh! Bizimle cidden tartıştın; hem de çok tartıştın. Doğru sözlülerden isen tehdit ettiğin azabı başımıza getir" dediler. | — (kapsam dışı) | — | fiil: "çok tartıştın" (أكثرت) | — |
| 22 | 12:21 | Yûsuf (Mekkî) | أَمْرِهِۦ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | Mısır'da onu satın alan kimse karısına: "Ona güzel bak, belki bize faydası olur yahut ta onu evlat ediniriz" dedi. Biz işte böylec… | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 23 | 12:38 | Yûsuf (Mekkî) | ٱلنَّاسِ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَشْكُرُونَ | Atalarım İbrahim, İshak ve Yakub'un dinine uydum. Allah'a herhangi bir ortak koşmak bize yaraşmaz; bu, Allah'ın bize ve insanlara … | şükretmez | A | insanlar (genel) — لا يشكرون | y |
| 24 | 12:40 | Yûsuf (Mekkî) | ٱلْقَيِّمُ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | "Allah'ı bırakıp taptığınız, sizin ve babalarınızın adlandırdığı putlardan başka bir şey değildir. Allah onların doğru olduğuna da… | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 25 | 12:68 | Yûsuf (Mekkî) | عَلَّمْنَـٰهُ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | Babalarının emrettiği gibi girdiler. Esasen bu, Allah katında onlara bir fayda sağlamazdı, ancak Yakub içindeki arzuyu ortaya koym… | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 26 | 12:103 | Yûsuf (Mekkî) | وَمَآ أَكْثَرُ ٱلنَّاسِ وَلَوْ حَرَصْتَ | Sana böylece vahyettiklerimiz, gaybe ait haberlerdir. Onlar elbirliği edip düzen kurdukları zaman yanlarında değildin; sen ne kada… | iman etmez | A | insanlar (genel) — بمؤمنين | y |
| 27 | 12:106 | Yûsuf (Mekkî) | وَمَا يُؤْمِنُ أَكْثَرُهُم بِٱللَّهِ إِلَّا وَهُم | Onların çoğu, ortak koşmadan Allah'a inanmazlar. | ortak koşar (+iman eder ama ortak koşar) | A* | insanlar (öncül 12:103 "en-nâs") — وما يؤمن أكثرهم بالله إلا وهم مشركون [İbn Abbâs: "Allah yarattı" derler, yine de ortak koşarlar (İbn Kesîr)] | o |
| 28 | 13:1 | Ra'd (Medenî) | ٱلْحَقُّ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يُؤْمِنُونَ | Elif, Lam, Mim, Ra. Bunlar Kitap'ın ayetleridir. Sana Rabbinden indirilen Kitap haktır; fakat insanların çoğu inanmazlar. | iman etmez | A | insanlar (genel) — لا يؤمنون | y |
| 29 | 16:38 | Nahl (Mekkî) | حَقًّا وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | Ölen kimseyi Allah'ın diriltmeyeceği üzerine bütün güçleriyle Allah'a yemin ederler. Hayır; öyle değil, ayrılığa düştükleri şeyi o… | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 30 | 16:75 | Nahl (Mekkî) | ۚ بَلْ أَكْثَرُهُمْ لَا يَعْلَمُونَ | Allah, hiçbir şeye gücü yetmeyen ve başkasının malı olan bir köle ile, kendisine verdiğimiz güzel nimetlerden gizlice ve açıkça sa… | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — köle/hür misali: Allah'tan başkasına tapanlara | y |
| 31 | 16:83 | Nahl (Mekkî) | ثُمَّ يُنكِرُونَهَا وَأَكْثَرُهُمُ ٱلْكَـٰفِرُونَ | Allah'ın nimetini hem bilirler hem de inkar ederler. Zaten çoğu kafir kimselerdir. | kâfir/nankör/yalancı | B | Mekkeli/Arap müşrikler ve inkârcılar — "nimeti bilirler, sonra inkâr ederler" [kâfir (küfür)] | o |
| 32 | 16:101 | Nahl (Mekkî) | ۚ بَلْ أَكْثَرُهُمْ لَا يَعْلَمُونَ | Bir ayetin yerini başka bir ayetle değiştirdiğimizde, ki Allah ne indirdiğini gayet iyi bilir onlar, "Sen sadece uyduruyorsun" der… | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — "Sen uyduruyorsun" diyenler | y |
| 33 | 17:6 | İsrâ (Mekkî) | وَبَنِينَ وَجَعَلْنَـٰكُمْ أَكْثَرَ نَفِيرًا | "Bunun ardından sizi onlara galip getireceğiz; mallar ve oğullarla size yardım edecek ve sizin sayınızı artıracağız." | — (kapsam dışı) | — | karşılaştırma, olumlu: "sayıca daha çok kıldık" (أكثر نفيرا) | — |
| 34 | 17:89 | İsrâ (Mekkî) | مَثَلٍ فَأَبَىٰٓ أَكْثَرُ ٱلنَّاسِ إِلَّا كُفُورًا | And olsun ki, biz Kuran'da insanlara türlü türlü misal gösterip açıkladık. Öyleyken insanların çoğu nankör olmakta direndiler. | kâfir/nankör/yalancı | A | insanlar (genel) — أبى ... إلا كفورا (nankörlükte direnmek) [nankör (küfûr)] | y |
| 35 | 18:34 | Kehf (Mekkî) | يُحَاوِرُهُۥٓ أَنَا۠ أَكْثَرُ مِنكَ مَالًا وَأَعَزُّ | Onun gelirleri de vardı. Bu yüzden, arkadaşiyle konuşurken: "Ben malca senden zengin, nüfusça da senden daha itibarlıyım" dedi. | — (kapsam dışı) | — | karşılaştırma: "malca senden zenginim" | — |
| 36 | 18:54 | Kehf (Mekkî) | وَكَانَ ٱلْإِنسَـٰنُ أَكْثَرَ شَىْءٍ جَدَلًا | And olsun ki, Biz bu Kuran'da insanlara türlü türlü misali gösterip açıkladık. İnsanın en çok yaptığı iş tartışmadır. | — (kapsam dışı) | — | üstünlük: "insan en çok tartışan varlıktır" (çoğunluk hükmü değil, tür niteliği) | — |
| 37 | 21:24 | Enbiyâ (Mekkî) | ۗ بَلْ أَكْثَرُهُمْ لَا يَعْلَمُونَ ٱلْحَقَّ | O'nu bırakıp tanrılar mı edindiler? De ki: "Kesin delilinizi getirin. İşte benim ve ümmetimin Kitap'ı ve senden öncekilerin kitapl… | bilmez (+yüz çevirir) | B | Mekkeli/Arap müşrikler ve inkârcılar — tanrılar edinenler | y |
| 38 | 23:70 | Mü'minûn (Mekkî) | جَآءَهُم بِٱلْحَقِّ وَأَكْثَرُهُمْ لِلْحَقِّ كَـٰرِهُونَ | Ya da: "Onda delilik var" diyorlar öyle mi? Hayır; onlara gerçeği getirmiştir, ama çoğu ondan hoşlanmamaktadır. | haktan hoşlanmaz | B | Mekkeli/Arap müşrikler ve inkârcılar — "Onda delilik var" diyenler | y |
| 39 | 25:44 | Furkân (Mekkî) | تَحْسَبُ أَنَّ أَكْثَرَهُمْ يَسْمَعُونَ أَوْ يَعْقِلُونَ | Yoksa çoklarının söz dinlediklerini veya aklettiklerini mi sanırsın? Onlar şüphesiz davarlar gibidir, belki daha da sapık yolludur… | akletmez (+dinlemez) | B | Mekkeli/Arap müşrikler ve inkârcılar — 25:41-44: Peygamber'le alay edenler | y |
| 40 | 25:50 | Furkân (Mekkî) | لِيَذَّكَّرُوا۟ فَأَبَىٰٓ أَكْثَرُ ٱلنَّاسِ إِلَّا كُفُورًا | And olsun ki öğüt almaları için ülkeler arasında yer yer türlü türlü yağmur yağdırmışızdır. Buna rağmen insanların çoğu nankörlükt… | kâfir/nankör/yalancı | A | insanlar (genel) — أبى ... إلا كفورا (nankörlükte direnmek) [nankör (küfûr)] | y |
| 41 | 26:8 | Şuarâ (Mekkî) | وَمَا كَانَ أَكْثَرُهُم مُّؤْمِنِينَ | Şüphesiz bunlarda Allah'ın kudretine işaret vardır, ama çoğu inanmazlar. | iman etmez | B | Mekkeli/Arap müşrikler ve inkârcılar — 26:1-8 bağlamı: Kur'an'dan yüz çevirenler | o |
| 42 | 26:67 | Şuarâ (Mekkî) | وَمَا كَانَ أَكْثَرُهُم مُّؤْمِنِينَ | Bunda şüphesiz ders vardır, ama çoğu inanmamıştır. | iman etmez | B | geçmiş kavim/ümmet — Mûsâ kıssası sonu (26:10-68); Firavun ve çevresi [refren; Mûsâ kıssasında kimin kastedildiği bağlamdan kesin okunmaz] | o |
| 43 | 26:103 | Şuarâ (Mekkî) | وَمَا كَانَ أَكْثَرُهُم مُّؤْمِنِينَ | Bunda şüphesiz bir ders vardır ama çoğu inanmamıştır. | iman etmez | B | geçmiş kavim/ümmet — İbrâhîm kıssası sonu (26:69-104) | o |
| 44 | 26:121 | Şuarâ (Mekkî) | وَمَا كَانَ أَكْثَرُهُم مُّؤْمِنِينَ | Doğrusu bunda bir ders vardır, ama çoğu inanmamıştır. | iman etmez | B | geçmiş kavim/ümmet — Nûh kıssası sonu (26:105-122) | o |
| 45 | 26:139 | Şuarâ (Mekkî) | وَمَا كَانَ أَكْثَرُهُم مُّؤْمِنِينَ | Böylece onu yalanladılar; Biz de kendilerini yok ettik. Bunda şüphesiz ki ders vardır; ama çoğu inanmamıştır. | iman etmez | B | geçmiş kavim/ümmet — Âd/Hûd kıssası sonu (26:123-140) | o |
| 46 | 26:158 | Şuarâ (Mekkî) | وَمَا كَانَ أَكْثَرُهُم مُّؤْمِنِينَ | Bunun üzerine onları azap yakaladı. Doğrusu bunda bir ders vardır, fakat çoğu inanmamıştır. | iman etmez | B | geçmiş kavim/ümmet — Semûd/Sâlih kıssası sonu (26:141-159) | o |
| 47 | 26:174 | Şuarâ (Mekkî) | وَمَا كَانَ أَكْثَرُهُم مُّؤْمِنِينَ | Şüphesiz bunda bir ders vardır, ama çoğu inanmamıştır. | iman etmez | B | geçmiş kavim/ümmet — Lût kıssası sonu (26:160-175) | o |
| 48 | 26:190 | Şuarâ (Mekkî) | وَمَا كَانَ أَكْثَرُهُم مُّؤْمِنِينَ | Doğrusu bunda bir ders vardır. Fakat çoğu inanmamıştır. | iman etmez | B | geçmiş kavim/ümmet — Eyke halkı/Şuayb kıssası sonu (26:176-191) | o |
| 49 | 26:223 | Şuarâ (Mekkî) | يُلْقُونَ ٱلسَّمْعَ وَأَكْثَرُهُمْ كَـٰذِبُونَ | Bunlar şeytanlara kulak verirler, çoğu yalancıdırlar. | kâfir/nankör/yalancı | B | şeytanlara kulak veren günahkâr iftiracılar (kâhin tipi) — 26:221-223: "Şeytanlar kimlere iner?" "Her günahkâr iftiracıya" [yalancı; İbn Kesîr: "kâhinler ve diğer günahkâr yalancılar"] | y |
| 50 | 27:61 | Neml (Mekkî) | ۚ بَلْ أَكْثَرُهُمْ لَا يَعْلَمُونَ | Yoksa yeri, yaratıklarının oturmasına elverişli kılan ve aralarında ırmaklar meydana getiren, yeryüzüne sabit dağlar yerleştiren, … | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — "Allah'la beraber başka ilah mı?" | y |
| 51 | 27:73 | Neml (Mekkî) | ٱلنَّاسِ وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَشْكُرُونَ | Doğrusu Rabbin, insanlara karşı lütuf sahibidir. Fakat onların çoğu şükretmezler. | şükretmez | A* | insanlar (öncül: "insanlara lütuf sahibidir") — لا يشكرون | y |
| 52 | 27:76 | Neml (Mekkî) | بَنِىٓ إِسْرَٰٓءِيلَ أَكْثَرَ ٱلَّذِى هُمْ فِيهِ | Doğrusu bu Kuran, İsrailoğullarına, ayrılığa düştükleri şeyin çoğunu anlatmaktadır. | — (kapsam dışı) | — | şeylerin çokluğu: "ayrılığa düştükleri şeyin çoğunu" (kişi değil, konu) | — |
| 53 | 28:13 | Kasas (Mekkî) | حَقٌّ وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَعْلَمُونَ | Böylece onu, annesinin gözü aydın olsun, üzülmesin, Allah'ın verdiği sözün gerçek olduğunu bilsin diye, ona geri çevirdik. Fakat ç… | bilmez | ? | öncül belirsiz — لا يعلمون [Mûsâ'nın annesine verilen sözün gerçekliği; "çoğu" kimdir ayetten kesin okunmuyor] | d |
| 54 | 28:57 | Kasas (Mekkî) | لَّدُنَّا وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَعْلَمُونَ | "Seninle beraber doğru yolda gidersek, yurdumuzdan ediliriz" dediler. Onları katımızdan bir rızık olarak her şeyin ürününün toplan… | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — "Seninle doğru yola girersek yurdumuzdan edilirik" diyenler | y |
| 55 | 28:78 | Kasas (Mekkî) | مِنْهُ قُوَّةً وَأَكْثَرُ جَمْعًا ۚ وَلَا | Karun: "Bu servet ancak, bende mevcut bir ilimden ötürü bana verilmiştir" demişti. Allah'ın, önceleri, ondan daha güçlü ve topladı… | — (kapsam dışı) | — | karşılaştırma: "daha çok topladı" (Kârûn kıssası) | — |
| 56 | 29:63 | Ankebût (Mekkî) | ۚ بَلْ أَكْثَرُهُمْ لَا يَعْقِلُونَ | And olsun ki onlara: "Gökten su indirip onunla, ölümünden sonra yeri dirilten kimdir?" diye sorarsan, şüphesiz, "Allah'tır" derler… | akletmez | B | Mekkeli/Arap müşrikler ve inkârcılar — "Gökten suyu kim indirir?" "Allah" derler | y |
| 57 | 30:6 | Rûm (Mekkî) | وَعْدَهُۥ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | Bu, Allah'ın vaadidir; Allah verdiği sözden caymaz, fakat insanların çoğu bilmezler. | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 58 | 30:9 | Rûm (Mekkî) | ٱلْأَرْضَ وَعَمَرُوهَآ أَكْثَرَ مِمَّا عَمَرُوهَا وَجَآءَتْهُمْ | Yeryüzünde dolaşıp, kendilerinden önce geçmiş kimselerin sonlarının nasıl olduğuna bakmazlar mı? Ki onlar kendilerinden daha kuvve… | — (kapsam dışı) | — | karşılaştırma: "onlardan çok imar etmişlerdi" | — |
| 59 | 30:30 | Rûm (Mekkî) | ٱلْقَيِّمُ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | Hakka yönelerek kendini Allah'ın insanlara yaratılışta verdiği dine ver. Zira Allah'ın yaratışında değişme yoktur; işte dosdoğru d… | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 60 | 30:42 | Rûm (Mekkî) | ۚ كَانَ أَكْثَرُهُم مُّشْرِكِينَ | De ki: "Yeryüzünde dolaşın da daha öncekilerden çoğu ortak koşan (müşrik) olanların sonunun nasıl olduğuna bir bakın." | ortak koşar | B | geçmiş kavim/ümmet — "daha öncekilerden çoğu ortak koşanlardı" | y |
| 61 | 31:25 | Lokmân (Mekkî) | ۚ بَلْ أَكْثَرُهُمْ لَا يَعْلَمُونَ | And olsun ki onlara: "Gökleri ve yeri yaratan kimdir?" diye sorsan, "Allah'tır" derler. De ki: "Hamd Allah'a mahsustur", ama çoğu … | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — "Gökleri kim yarattı?" "Allah" derler | y |
| 62 | 34:28 | Sebe' (Mekkî) | وَنَذِيرًا وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | Biz seni bütün insanlara ancak müjdeci ve uyarıcı olarak göndermişizdir; fakat insanların çoğu bilmez. | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 63 | 34:35 | Sebe' (Mekkî) | وَقَالُوا۟ نَحْنُ أَكْثَرُ أَمْوَٰلًا وَأَوْلَـٰدًا وَمَا | Ve dediler ki: "Malları ve çocukları en çok olan bizleriz, azaba uğratılacak da değiliz" | — (kapsam dışı) | — | karşılaştırma: "malları ve çocukları en çok olan biziz" (müşriklerin iddiası) | — |
| 64 | 34:36 | Sebe' (Mekkî) | وَيَقْدِرُ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | De ki: "Şüphesiz Rabbim rızkı dilediğine genişletir ve bir ölçüye göre verir, fakat insanların çoğu bilmezler." | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 65 | 34:41 | Sebe' (Mekkî) | ٱلْجِنَّ ۖ أَكْثَرُهُم بِهِم مُّؤْمِنُونَ | Melekler: "Haşa, bizim dostumuz onlar değil, Sensin. Hayır; onlar bize değil cinlere tapıyorlardı, çoğu onlara inanıyorlardı" derl… | ortak koşar | B | Mekkeli/Arap müşrikler ve inkârcılar — "Çoğu cinlere inanıyordu" (melekler konuşuyor) [yüklem olumlu görünür (مؤمنون) ama nesnesi cinlerdir] | y |
| 66 | 36:7 | Yâsîn (Mekkî) | ٱلْقَوْلُ عَلَىٰٓ أَكْثَرِهِمْ فَهُمْ لَا يُؤْمِنُونَ | And olsun ki, hüküm çoğunun aleyhine gerçekleşmiştir, bunun için artık inanmazlar. | iman etmez | B | Mekkeli/Arap müşrikler ve inkârcılar — "hüküm çoğunun aleyhine gerçekleşti"; 36:6 babaları uyarılmamış kavim | y |
| 67 | 37:71 | Sâffât (Mekkî) | ضَلَّ قَبْلَهُمْ أَكْثَرُ ٱلْأَوَّلِينَ | Onlardan önce, evvelki ümmetlerin çoğu, and olsun ki sapıtmıştı. | diğer (sapmak) | B | geçmiş kavim/ümmet — "evvelki ümmetlerin çoğu sapmıştı" | y |
| 68 | 39:29 | Zümer (Mekkî) | ۚ بَلْ أَكْثَرُهُمْ لَا يَعْلَمُونَ | Allah, geçimsiz efendileri olan bir adamla, yalnız bir kişiye bağlı olan bir adamı misal olarak verir. Bu ikisi eşit midir? Övülme… | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — ortaklı köle/tek efendili adam misali | y |
| 69 | 39:49 | Zümer (Mekkî) | فِتْنَةٌ وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَعْلَمُونَ | İnsanın başına bir sıkıntı gelince Bize yalvarır. Sonra katımızdan ona bir nimet verdiğimiz zaman: "Bu bana bilgimden dolayı veril… | bilmez | A* | insan (el-insân, tür) — لا يعلمون [nimeti "bilgimle kazandım" diyen insan tipi; sıkıntıda yalvarıp nimette unutan] | y |
| 70 | 40:57 | Mü'min (Mekkî) | ٱلنَّاسِ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | Göklerin ve yerin yaratılması, insanların yaratılmasından daha büyük bir şeydir. Fakat insanların çoğu bilmezler. | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 71 | 40:59 | Mü'min (Mekkî) | فِيهَا وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يُؤْمِنُونَ | Kıyamet günü mutlaka gelecektir. Bunda şüphe yoktur, fakat, insanların çoğu inanmıyor. | iman etmez | A | insanlar (genel) — لا يؤمنون | y |
| 72 | 40:61 | Mü'min (Mekkî) | ٱلنَّاسِ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَشْكُرُونَ | Size, geceyi dinlenesiniz diye karanlık ve gündüzü aydınlık olarak yaratan Allah'tır. Doğrusu Allah insanlara karşı lütufkardır, a… | şükretmez | A | insanlar (genel) — لا يشكرون | y |
| 73 | 40:82 | Mü'min (Mekkî) | ۚ كَانُوٓا۟ أَكْثَرَ مِنْهُمْ وَأَشَدَّ قُوَّةً | Yeryüzünde dolaşıp, kendilerinden daha çok, daha kuvvetli, yeryüzünde bıraktıkları eserler daha sağlam olan öncekilerin sonuçların… | — (kapsam dışı) | — | karşılaştırma: "kendilerinden daha çok ve daha güçlü" | — |
| 74 | 41:4 | Fussilet (Mekkî) | وَنَذِيرًا فَأَعْرَضَ أَكْثَرُهُمْ فَهُمْ لَا يَسْمَعُونَ | Bu Kitap, merhametli olan Allah katından indirilmedir; bilen bir millet için müjdeci ve uyarıcı olmak üzere Arapça okunarak, ayetl… | yüz çevirir (+işitmez) | B | Mekkeli/Arap müşrikler ve inkârcılar — أعرض أكثرهم [Arapçada "أكثرهم" (onların çoğu); "insanların" kelimesini Diyanet mealine yerleştirmiş] | y |
| 75 | 43:78 | Zuhruf (Mekkî) | بِٱلْحَقِّ وَلَـٰكِنَّ أَكْثَرَكُمْ لِلْحَقِّ كَـٰرِهُونَ | And olsun ki, size gerçeği getirdik; fakat çoğunuz gerçeği sevmiyorsunuz. | haktan hoşlanmaz | B | cehennemdeki suçlular (ahiret sahnesinde hitap) — 43:74-78: nöbetçiyle konuşan cehennemlikler [dünyada değil, ahiret sahnesinde söylenen bir tespit] | y |
| 76 | 44:39 | Duhân (Mekkî) | بِٱلْحَقِّ وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَعْلَمُونَ | Biz onları, ancak ve ancak gerektiği gibi yarattık, ama insanların çoğu bilmezler. | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — 44:34-35: yeniden dirilişi inkâr edenler [Arapçada "أكثرهم"; "insanların" kelimesi Diyanet mealinde eklenmiş] | o |
| 77 | 45:26 | Câsiye (Mekkî) | فِيهِ وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَعْلَمُونَ | De ki: "Sizi Allah diriltir, sonra öldürür, sonra sizi şüphe götürmeyen kıyamet gününde toplar. Ama insanların çoğu bilmezler." | bilmez | A | insanlar (genel) — لا يعلمون | y |
| 78 | 49:4 | Hucurât (Medenî) | وَرَآءِ ٱلْحُجُرَٰتِ أَكْثَرُهُمْ لَا يَعْقِلُونَ | Sana odaların ötesinden seslenenlerin çoğu akletmeyen kimselerdir. | akletmez | B | odaların ardından Peygamber'e seslenenler (bedevî) — İbn Kesîr: "odaların ardından seslenen bedevîler" [sebeb-i nüzûl rivayetlerinde Temîm'den bir kişi anılır (İbn Kesîr/Ahmed)] | y |
| 79 | 52:47 | Tûr (Mekkî) | ذَٰلِكَ وَلَـٰكِنَّ أَكْثَرَهُمْ لَا يَعْلَمُونَ | Zulmedenlere, şüphesiz, bundan başka da azap vardır; fakat onların çoğu bilmezler. | bilmez | B | Mekkeli/Arap müşrikler ve inkârcılar — "zulmedenlere bundan başka da azap vardır" | y |
| 80 | 58:7 | Mücâdele (Medenî) | ذَٰلِكَ وَلَآ أَكْثَرَ إِلَّا هُوَ مَعَهُمْ | Göklerde olanları da, yerde olanları da Allah'ın bildiğini bilmez misin? Üç kişinin gizli bulunduğu yerde dördüncü mutlaka O'dur; … | — (kapsam dışı) | — | karşılaştırma: "az veya çok" (Allah'ın ilmi/ma'iyyeti) | — |
| 81 | 89:12 | Fecr (Mekkî) | فَأَكْثَرُوا۟ فِيهَا ٱلْفَسَادَ | Vadide kayaları kesip yontan Semud milletine, memleketlerde aşırı giden, oralarda bozgunculuğu artıran, sarsılmaz bir saltanat sah… | — (kapsam dışı) | — | fiil: "bozgunculuğu artırdılar" (فأكثروا) | — |

## 2. Sayılar

Yüklem (birincil / anılan): bilmez 27/27; iman etmez 14/14; şükretmez 6/6; akletmez 4/4; fasık 4/4; kâfir/nankör/yalancı 4/4; ortak koşar 3/3; haktan hoşlanmaz 2/2; zanna uyar 2/2; yüz çevirir 1/2; diğer (sapmak) 1/1.

Yüklem × özne: bilmez: A=11, A*=1, ?=2, B=13; iman etmez: A=4, B=10; şükretmez: A=3, A*=3; akletmez: B=4; fasık: B=4; kâfir/nankör/yalancı: A=2, B=2; ortak koşar: A*=1, B=2; haktan hoşlanmaz: B=2; zanna uyar: A=1, B=1; yüz çevirir: B=1; diğer (sapmak): B=1.

"İnsanların çoğu" (A, 21 ayet) içinde yüklemler: bilmez 11, iman etmez 4, şükretmez 3, kâfir/nankör/yalancı 2, zanna uyar 1. Yani genel formül ağırlıkla **"çoğu bilmez"**dir; "çoğu kâfirdir/fasıktır/müşriktir" biçiminde lafzen genel insan için gelen ayet yoktur (17:89 ve 25:50'de "nankörlükte direndiler" denir).

Mekkî/Medenî (hüküm ayetleri): {"makkah": 59, "madinah": 9}; özneye göre: {"A": {"makkah": 19, "madinah": 2}, "A*": {"makkah": 5, "madinah": 0}, "B": {"makkah": 33, "madinah": 7}, "?": {"makkah": 2, "madinah": 0}}; B içinde kime göre: {"Mekkeli/Arap müşrikler ve inkârcılar": {"makkah": 20, "madinah": 3}, "Kitap ehli": {"makkah": 0, "madinah": 3}, "geçmiş kavim/ümmet": {"makkah": 11, "madinah": 0}, "diğer belirli grup": {"makkah": 2, "madinah": 1}}.

Medenî hüküm ayetleri: 2:100, 2:243, 3:110, 5:59, 5:103, 8:34, 9:8, 13:1, 49:4 (not: 5:103 Medenî sure içinde Arap cahiliye adaklarını anar; 8:34 ve 9:8 Mekkeli müşrikleri anan Medenî ayetler).

## 3. Paylaşılan 14'lü listenin çapraz kontrolü

| Listedeki atıf | Gerçek ayet | Kalıp | Özne türü | Kim |
|---|---|---|---|---|
| Nahl 83 | 16:83 | ekser (أكثر) | B | Mekkeli/Arap müşrikler ve inkârcılar |
| Mâide 48 | 5:49 | kesîr (كثير) | — (ekser listesinde yok) | — |
| Rûm 42 | 30:42 | ekser (أكثر) | B | geçmiş kavim/ümmet |
| İsrâ 89 | 17:89 | ekser (أكثر) | A | insanlar (genel) |
| Yûnus 92 | 10:92 | kesîr (كثير) | — (ekser listesinde yok) | — |
| Şuarâ 223 | 26:223 | ekser (أكثر) | B | şeytanlara kulak veren günahkâr iftiracılar (kâhin tipi) |
| Yûnus 36 | 10:36 | ekser (أكثر) | B | Mekkeli/Arap müşrikler ve inkârcılar |
| Furkân 50 | 25:50 | ekser (أكثر) | A | insanlar (genel) |
| Bakara 243 | 2:243 | ekser (أكثر) | A | insanlar (genel) |
| Bakara 100 | 2:100 | ekser (أكثر) | B | Kitap ehli |
| Zuhruf 78 | 43:78 | ekser (أكثر) | B | cehennemdeki suçlular (ahiret sahnesinde hitap) |
| Yûsuf 106 | 12:106 | ekser (أكثر) | A* | insanlar (öncül 12:103 "en-nâs") |
| Fussılet 4 | 41:4 | ekser (أكثر) | B | Mekkeli/Arap müşrikler ve inkârcılar |
| Mâide 103 | 5:103 | ekser (أكثر) | B | Mekkeli/Arap müşrikler ve inkârcılar |

Sonuç: 14 atfın 12'si ekser kalıbı, 2'si (Mâide "48" → gerçekte 5:49; Yûnus 92) kesîr kalıbı. "Mâide 48" atfı hatalıdır: ilgili cümle 5:49'dadır (5:48 başka bir ayettir). Ayrıca 26:223 (Şuarâ) kâhin/iftiracı tipi içindir; 43:78 ahiret sahnesinde cehennemliklere söylenir; 41:4 Arapçada "insanların" değil "onların çoğu" der (Diyanet mealinde "insanların" eklenmiş).

## 4. İkinci kalıp: كثير (kesîr = çok/birçok) — ayrı sayıldı

Kur'an'da كثير geçen **71** ayetin tamamı koddan bulundu ve elle etiketlendi: **29** ayet kişilere ilişkin "çoğu/birçoğu" hükmü taşıyor, **42** ayet kişi dışı (çok anmak, çok nimet, çok şey). Kişilere ilişkin 29: nitelik olumlu-nötr 4, olumsuz 23, karışık 2; özne: genel 9, belirli 20 (Kitap ehli 9); Mekkî 10 / Medenî 19. **Ekser'in tersine kesîr ağırlıkla Medenî surelerde ve Kitap ehli hakkındadır; ayrıca olumlu-nötr örnekleri de vardır.**

| Ayet | Nitelik | Özne türü | Özne | Ne deniyor | Diyanet kelimesi |
|---|---|---|---|---|---|
| 2:26 | olumlu-nötr | genel | insanlar | Allah misalle birçoğunu saptırır, birçoğunu doğru yola iletir | birçoğu |
| 2:109 | olumsuz | belirli | Kitap ehli | çoğu kıskançlıkla sizi küfre döndürmek ister | çoğu |
| 2:249 | olumlu-nötr | belirli | Tâlût'un askerleri | "Nice az topluluk çok topluluğa Allah'ın izniyle galip gelmiştir" | çok |
| 3:146 | olumlu-nötr | belirli | peygamberlerin yanında savaşanlar | nice peygamberle birlikte pek çok Rabbânî savaştı | pek çok |
| 5:32 | olumsuz | belirli | İsrailoğulları | bunca ayetten sonra çoğu yeryüzünde aşırı gider | çoğu |
| 5:49 | olumsuz | genel | insanlar | çoğu fasıktır | çoğu |
| 5:62 | olumsuz | belirli | Kitap ehli | çoğu günaha ve haksızlığa koşuşur | çoğu |
| 5:64 | olumsuz | belirli | Kitap ehli | indirilenler çoğunun azgınlığını ve küfrünü artırır | çoğu |
| 5:66 | olumsuz | belirli | Kitap ehli | bir zümre orta yolda; çoğunun işledikleri kötü | çoğu |
| 5:68 | olumsuz | belirli | Kitap ehli | indirilenler çoğunun azgınlık ve küfrünü artırır | çoğu |
| 5:71 | olumsuz | belirli | Kitap ehli | tevbe kabulünden sonra çoğu yine körleşti, sağırlaştı | çoğu |
| 5:77 | olumsuz | belirli | Kitap ehli'nden önce sapan millet | öncekiler çoğunu saptırdı | çoğunu |
| 5:80 | olumsuz | belirli | Kitap ehli | çoğunun inkâr edenleri dost edindiğini görürsün | çoğu |
| 5:81 | olumsuz | belirli | Kitap ehli | çoğu fasıktır | çoğu |
| 6:119 | olumsuz | genel | insanlar | çoğunluk heva ile bilmeden sapıtır | çoğunluk |
| 6:137 | olumsuz | belirli | müşrikler | ortaklar müşriklerin çoğuna çocuk öldürmeyi güzel gösterdi | çoğu |
| 7:179 | olumsuz | genel | cin ve insan | cehennem için birçok cin ve insan yaratıldı (kalpleri var anlamazlar) | birçok |
| 9:34 | olumsuz | belirli | hahamlar ve rahipler | çoğu insanların mallarını haksızlıkla yer | çoğu |
| 10:92 | olumsuz | genel | insanlar | çoğu ayetlerimizden habersizdir | çoğu |
| 14:36 | olumsuz | genel | insanlar | putlar çok insanı saptırdı (İbrâhim'in duası) | çok |
| 22:18 | karışık | genel | insanlar | insanların birçoğu Allah'a secde eder; birçoğu da azabı hak etmiştir | birçoğu |
| 27:15 | olumlu-nötr | belirli | mü'min kullar | Dâvûd ve Süleymân: "mü'min kullarının çoğundan üstün kıldı" | çoğu |
| 30:8 | olumsuz | genel | insanlar | çoğu Rablerine kavuşmayı inkâr eder | çoğu |
| 36:62 | olumsuz | genel | insanlar | şeytan sizden nice nesilleri saptırdı | nice |
| 38:24 | olumsuz | belirli | ortaklar | ortakçıların çoğu birbirine haksızlık eder; inanıp salih amel işleyenler hariç, ne kadar azdır | çoğu |
| 57:16 | olumsuz | belirli | Kitap ehli | çoğunun kalbi katılaştı, fasıklaştı | çoğu |
| 57:26 | karışık | belirli | Nûh ve İbrâhim soyu | kimi doğru yolda, birçoğu yoldan çıkmış | birçoğu |
| 57:27 | olumsuz | belirli | İsâ'ya uyanlar | çoğu fasık (rahbâniyet icadı) | çoğu |
| 71:24 | olumsuz | belirli | Nûh kavminin önderleri | birçoğunu saptırdılar | birçoğu |

Kişi dışı 42 ayet (özet): kişi sayısı, hüküm değil: 4:1; çok anmak / çok tesbih (zikir): 3:41, 8:45, 20:33, 20:34, 26:227, 33:21, 33:35, 33:41, 62:10, 22:40; çok nimet, rızık, ganimet, mal: 2:245, 2:269, 4:19, 4:94, 4:100, 9:25, 23:19, 23:21, 25:49, 38:51, 43:73, 48:19, 48:20, 56:32; çok şey: azap, çelişki, günah, zan, misal: 3:186, 4:82, 4:114, 4:160, 5:15, 6:91, 8:43, 9:82, 11:91, 25:14, 25:38, 41:22, 42:30, 42:34, 49:7, 49:12, 17:70.

**Olumlu-nötr 'çok/çoğu' örnekleri (kesîr):** 2:249 ("nice az topluluk çok topluluğa galip gelmiştir" — burada azlık ve çokluk ahlâkî ölçü değildir), 3:146 ("nice peygamberle birlikte pek çok Rabbânî savaştı"), 22:18 ("insanların birçoğu Allah'a secde eder; birçoğu da azabı hak etmiştir"), 27:15 (Dâvûd ve Süleymân "mü'min kullarının çoğundan üstün kıldı"). Aynı kalıbın hem övgüde hem yergide kullanılması, kesîr'in kendi başına bir ahlâkî etiket olmadığını gösterir.

**Vâkıa düzeltmesi (doğrulandı):** 56:13-14 ve 56:39-40'ta kalıp kesîr değildir: Arapçası `ثُلَّةٌ مِّنَ ٱلْأَوَّلِينَ وَقَلِيلٌ مِّنَ ٱلْـَٔاخِرِينَ` (56:13-14) ve `ثُلَّةٌ مِّنَ ٱلْأَوَّلِينَ وَثُلَّةٌ مِّنَ ٱلْـَٔاخِرِينَ` (56:39-40). Sülle = topluluk/küme (Diyanet meali 56:13'te 'büyük kısmı', 56:39'da 'bir kısmı' diye çevirir; sözlükten ayrıca doğrulanmadı); 56:14'te 'qalîl' (az) vardır. Yani bu ayetler ne ekser ne kesîr sayımındadır; 'az' taramasında 56:14 sayılmıştır.

## 5. "Az" kalıpları (قليل / قلة)

قليل/قلة geçen ayetler; yanlış eşleşme çıkarıldı. "Az" birçok farklı şeyi niteler; yalnızca "az kişi" grubu azınlık ifadesidir.

| Ayet | Tür | Not |
|---|---|---|
| 2:83 | az kişi (azınlık) | azınlık dönmeyenler (İsrailoğulları) |
| 2:88 | az kişi (azınlık) | azınlık inananlar (Medine Yahudileri) (ikili okuma: "pek azı" / "pek az iman") |
| 2:246 | az kişi (azınlık) | azınlık savaşa devam edenler |
| 2:249 | az kişi (azınlık) | azınlık: sudan içmeyenler; "az topluluk çok topluluğa galip" |
| 4:46 | az kişi (azınlık) | azınlık inananlar (ikili okuma: "pek azı" / "pek az iman") |
| 4:66 | az kişi (azınlık) | azınlık itaat edenler |
| 4:83 | az kişi (azınlık) | azınlık şeytana uymayanlar |
| 4:155 | az kişi (azınlık) | azınlık inananlar (ikili okuma: "pek azı" / "pek az iman") |
| 5:13 | az kişi (azınlık) | azınlık hainlik etmeyenler |
| 11:40 | az kişi (azınlık) | Nûh'la birlikte inananlar az |
| 11:116 | az kişi (azınlık) | kurtulanlar az |
| 17:62 | az kişi (azınlık) | İblis: "azı hariç" soyunu saptıracağım |
| 18:22 | az kişi (azınlık) | mağara ehlinin sayısını az kişi bilir (nötr) |
| 34:13 | az kişi (azınlık) | şükreden kullar az |
| 38:24 | az kişi (azınlık) | iman edip salih amel işleyenler az |
| 56:14 | az kişi (azınlık) | sonrakilerden az (cennetin öndekileri) |
| 26:54 | az kişi (azınlık) | Firavun: "bunlar az bir topluluk" (Mûsâ'ya uyanlar) |
| 7:86 | az kişi (azınlık) | "az idiniz, sizi çoğalttı" (nimet olarak) |
| 8:26 | az kişi (azınlık) | "yeryüzünde az ve zayıf idiniz" (nimet hatırlatması) |
| 28:58 | az kişi (azınlık) | Diyanet: "pek az kimseler oturabilmiştir" (zaman/kişi okuması belirsiz) (ikili okuma: "pek azı" / "pek az iman") |
| 7:3 | az davranış (az şükür/iman/öğüt) |  |
| 7:10 | az davranış (az şükür/iman/öğüt) |  |
| 23:78 | az davranış (az şükür/iman/öğüt) |  |
| 27:62 | az davranış (az şükür/iman/öğüt) |  |
| 32:9 | az davranış (az şükür/iman/öğüt) |  |
| 40:58 | az davranış (az şükür/iman/öğüt) |  |
| 67:23 | az davranış (az şükür/iman/öğüt) |  |
| 69:41 | az davranış (az şükür/iman/öğüt) |  |
| 69:42 | az davranış (az şükür/iman/öğüt) |  |
| 4:142 | az davranış (az şükür/iman/öğüt) |  |
| 48:15 | az davranış (az şükür/iman/öğüt) |  |
| 33:18 | az davranış (az şükür/iman/öğüt) |  |
| 33:20 | az davranış (az şükür/iman/öğüt) |  |
| 2:41 | az bedel (ثمنا قليلا) |  |
| 2:79 | az bedel (ثمنا قليلا) |  |
| 2:174 | az bedel (ثمنا قليلا) |  |
| 3:77 | az bedel (ثمنا قليلا) |  |
| 3:187 | az bedel (ثمنا قليلا) |  |
| 3:199 | az bedel (ثمنا قليلا) |  |
| 5:44 | az bedel (ثمنا قليلا) |  |
| 9:9 | az bedel (ثمنا قليلا) |  |
| 16:95 | az bedel (ثمنا قليلا) |  |
| 2:126 | az zaman/geçici zevk |  |
| 3:197 | az zaman/geçici zevk |  |
| 4:77 | az zaman/geçici zevk |  |
| 9:38 | az zaman/geçici zevk |  |
| 9:82 | az zaman/geçici zevk |  |
| 16:117 | az zaman/geçici zevk |  |
| 17:52 | az zaman/geçici zevk |  |
| 17:76 | az zaman/geçici zevk |  |
| 23:40 | az zaman/geçici zevk |  |
| 23:114 | az zaman/geçici zevk |  |
| 31:24 | az zaman/geçici zevk |  |
| 33:16 | az zaman/geçici zevk |  |
| 33:60 | az zaman/geçici zevk |  |
| 39:8 | az zaman/geçici zevk |  |
| 44:15 | az zaman/geçici zevk |  |
| 73:2 | az zaman/geçici zevk |  |
| 73:3 | az zaman/geçici zevk |  |
| 73:11 | az zaman/geçici zevk |  |
| 77:46 | az zaman/geçici zevk |  |
| 51:17 | az zaman/geçici zevk |  |
| 8:43 | az şey/miktar/algı |  |
| 8:44 | az şey/miktar/algı |  |
| 12:47 | az şey/miktar/algı |  |
| 12:48 | az şey/miktar/algı |  |
| 17:74 | az şey/miktar/algı |  |
| 17:85 | az şey/miktar/algı |  |
| 34:16 | az şey/miktar/algı |  |
| 53:34 | az şey/miktar/algı |  |

Doğrulanan örnekler (Diyanet): Sebe' 34:13 "Kullarımdan şükredenler pek azdır."; Hûd 11:40 "Pek az kimse onunla beraber inanmıştı."; Bakara 2:88 "Onların pek azı inanırlar."; Nisâ 4:46 "…artık pek azı dışında inanmazlar" (Diyanet'te: bkz. kuran_tam); A'râf 7:10 "Öyleyken pek az şükrediyorsunuz."; Sâd 38:24 "…sayıları da ne kadar azdır!". Hepsi veri/kuran_tam.json'dan satır satır doğrulandı (`ayet.py --metin`).

## 6. Tefsir kontrolleri (gerçekten açılan)

Kaynak: quran.com API tefsir id 169 = İbn Kesîr (kısaltılmış İngilizce) — `https://api.quran.com/api/v4/tafsirs/169/by_ayah/<ayet>`:

- **12:106** ("çoğu ortak koşmadan Allah'a inanmazlar"): İbn Kesîr, İbn Abbâs'tan: "Onlarda bir parça iman vardır; 'Gökleri kim yarattı?' diye sorulunca 'Allah' derler, yine de O'na ortak koşarlar." Mücâhid, Atâ, İkrime, Şa'bî, Katâde, Dahhâk, İbn Zeyd de benzer demiştir. Aynı yerde: hac mevsiminde müşriklerin telbiyesi "Lebbeyk… senin ortağın yok, yalnız bir ortak hariç; onun sahibi sensin" (Sahih'te geçtiği belirtilir). Bağlam: ayetin başında "Allah çoğu insanın O'nun delillerini düşünmediğini bildirir".
- **26:223**: İbn Kesîr: şeytanlar "yalancı ve günahkâr herkese" iner = "kâhinler ve diğer günahkâr yalancılar"; "şeytanlara kulak verirler" = gökten haber çalmaya çalışırlar, bir doğruya yüz yalan katarlar (Buhârî, Âişe rivayetiyle anılır). Yani "çoğu yalancıdır" kâhin tipine aittir.
- **2:88**: İbn Kesîr: Katâde, "fe-kalîlen mâ yu'minûn" için "onlardan ancak azı inanır" der. (Aynı ayette dil bakımından "çok az iman" okuması da mümkündür; bu yüzden tabloda 'ikili okuma' işaretlendi.)
- **49:4**: İbn Kesîr: "odaların arkasından seslenen bedevîler"; nüzûl sebebi olarak Akra' b. Hâbis et-Temîmî'nin anıldığı belirtilir (Ahmed rivayeti).

## Âlim/kitap alıntıları
- (Lugat) Lisânü'l-Arab: `والكُثْرُ معظم الشيء وأَكْثَرُه` — bir şeyin büyük kısmı ve çoğu. URL yukarıda. Lane: "The greater, or greatest, or main, part, of a thing; the most thereof."
- (Tefsir) İbn Kesîr, 12:106 ve 26:223 (bkz. §6).

## Canlı sahne/benzetme fikirleri
- Sure haritası: 114 kutu, yanan kutular. Şuarâ'daki sekiz "refren" (ve mâ kâne ekseruhum mü'minîn) sekiz farklı kıssanın dönüş cümlesidir; sayıyı şişirir.
- "Kalabalık bir otobüste ses": sayı, kimin konuştuğunu söylemez.

## Zayıf/tartışmalı notlar
- Özne sınıflaması yazara aittir; güven alanı 'orta' veya 'düşük' olanlar tartışmaya açıktır (26:67 ve Şuarâ refrenleri, 10:55, 28:13, 12:106, 10:60).
- Mekkî/Medenî sure düzeyindedir, quran.com verisine göredir; Ra'd (13), Hac gibi surelerde âlimler ayrılır.
- 2:88, 4:46, 4:155'teki `قليلا ما` / `إلا قليلا` kalıbı hem "pek azı" hem "pek az iman" diye okunabilir.
- Yan tarama (kesîr) örneklemdir, tam sayım değildir; "az" taraması tam (71 eşleşme) fakat türleme yazara aittir.
- Olumlu-nötr çoğunluk (ekser) ayeti bulunmaması "Kur'an çoğunluğu hep kötüler" demek değildir; çünkü aynı Kitap'ta 22:18 (birçok insan secde eder), 2:249 (az topluluk çok topluluğa galip), 3:146 gibi kesîr/çoğul ifadeler vardır.

## Açılan kaynak URL'leri
- https://api.quran.com/api/v4/chapters?language=tr (indirildi → veri/chapters_quran_com.json)
- https://api.quran.com/api/v4/tafsirs/169/by_ayah/12:106 · 26:223 · 2:88 · 49:4 · 28:13 · 16:83 (İbn Kesîr)
- https://arabiclexicon.hawramani.com/%D9%83%D8%AB%D8%B1/ (Sıhâh, Lisânü'l-Arab, Lane, Müfredât bölümleri)
- https://api.quran.com/api/v4/resources/tafsirs (tefsir listesi)
- Yerel: veri/kuran_tam.json, veri/ekser_ayetleri_tam.json, araclar/ayet.py
