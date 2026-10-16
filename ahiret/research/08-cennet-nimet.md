# 08 — Cennet: Gözün Görmediği Yer (mekân, yapı, nimetler, insanın hali)

Araştırma notları. Tüm ayetler api.quran.com (Arapça: `quran/verses/uthmani`; Türkçe: Diyanet İşleri meali, translation id 77) üzerinden alındı; hadisler fawazahmed0/hadith-api (jsdelivr) koleksiyonlarından numara+metin doğrulandı (Müslim numaraları `arabicnumber` = Muhammed Fuad Abdülbâkî numarası ile eşleştirildi). Derece bilgisi Tirmizî için aynı veri setindeki Şâkir/Elbânî/Zübeyr Alî Zâî hükümlerinden, Buhârî–Müslim için "Sahîhayn" olduğundan.

## Özet

- Cennetin tasviri vahiyde **"mesel" (misal)** olarak geçer (Ra'd 13:35, Muhammed 47:15). İbn Abbas: "Dünyadaki şeylerden cennette olanlara isimden başka benzeyen yoktur" (İbn Kesîr, Bakara 2:25 tefsiri). Yani kelimeler dünyadan, gerçek dünyayı aşar. Secde 32:17 ve "hiçbir göz görmemiş…" kudsî hadisi (Buhârî 3244, Müslim 2824) bunun mührüdür.
- Boyut: genişliği "göklerle yer kadar" (Âl-i İmrân 3:133, Hadîd 57:21). En alttaki cennetlik bile "dünya ve on katı" alır (Buhârî 6571, Müslim 186). Katmanlar: yüz derece, iki derece arası gök–yer arası kadar; en üstü Firdevs, üstünde Rahmân'ın Arşı (Buhârî 2790).
- Kapılar: sekiz (Buhârî 3257, Müslim 234); Reyyân oruçlulara (Buhârî 1896, Müslim 1152); namaz, cihad, sadaka kapıları (Buhârî 1897, 3666). Kapının iki kanadı arası Mekke–Hecer kadar (Müslim 194).
- Manzara: dört ırmak (su, süt, şarap, bal; Muhammed 47:15), dikensiz sidre, meyveleri sarkmış, uzun gölge, akan su (Vâkıa 56:27-33); ağacın gölgesinde atlı yüz yıl gider (Buhârî 3252/3253, Müslim 2826); inciden çadır, genişliği altmış mil (Buhârî 4880, Müslim 2838); toprağı misk (Buhârî 349); görünen/içten dışı görünen köşkler (Tirmizî 2527 hasan; Kur'ân: gürfe, Zümer 39:20 vb.).
- İnsan: yeni beden — ihtiyaç gideren/hasta olan beden değil. Yiyip içerler, tükürmez-sümkürmez-ihtiyaç gidermezler; terleri misk (Müslim 2835). Sağlık, genç kalma, ebedî yaşama, nimet (Müslim 2837). Elbiseleri eskimez, gençlikleri tükenmez (Müslim 2836). Yüzleri ay gibi, kalpleri tek kalp gibi, kin yok (Buhârî 3254, Müslim 2834). Âdem'in boyunda/suretinde (Buhârî 3326 + Müslim 2834); 30–33 yaş, tüysüz, sürmeli (Tirmizî 2545 hasan; 2539 hasan).
- Duygu dünyası: kin sökülür, yorgunluk dokunmaz (Hicr 15:47-48), boş söz ve günah yok, ancak selam (Vâkıa 56:25-26), "nimetin parıltısı yüzlerinde" (Mutaffifîn 83:24), ana-baba-eş-çocuk bir arada (Ra'd 13:23, Tûr 52:21), melekler her kapıdan selam verir (Ra'd 13:23-24).
- Cuma pazarı (sûk) ve Rabb'i görmek (Müslim 2833; Müslim 181; Buhârî 7437). "Rıdvân": "Üzerinize hoşnutluğumu indiriyorum, bundan sonra size asla gücenmeyeceğim" (Müslim 2829). Tevbe 9:72: "Allah'ın hoşnut olması en büyük şeydir."
- Said Nursî (Yirmi Sekizinci Söz): "cennet bütün lezâiz-i ma'neviyeye medâr olduğu gibi bütün lezâiz-i cismâniyeye de medârdır" — ruh–beden–kalp–his hepsine hitap eder; bir bahçedeki ziyafette iki dost, kabiliyetleri kadar tadar, yine de omuz omuza.
- İbn Kayyım (Hâdi'l-Ervâh): 70 bâblık dev bir cennet envanteri: yeri, dereceleri, isimleri, toprak ve yapısı, köşkleri, nehirleri, çarşısı, ziyareti, "cennet akla gelenin fevkindedir" (64. bâb).

## Doğrulanmış ayetler (ref | Arapça | Diyanet meali)

Aşağıda API'den aynen çekilmiş listedir (ek A). Not: Diyanet mealinde bazı ayet gruplarının çevirisi tek parça verilmiştir (ör. 13:22-24, 23:10-11); bu durumda API aynı metni grup içindeki her ayete döndürür. HTML'de bunu ayet grubu olarak (22-24) gösterdim.

## Doğrulanmış hadisler (TR anlam | kaynak+no | derece | nasıl doğrulandı)

| TR anlam | Kaynak | Derece | Doğrulama |
|---|---|---|---|
| Cennetin sekiz kapısı vardır; birinin adı Reyyân'dır, ona yalnız oruç tutanlar girer. | Buhârî 3257 (Sehl b. Sa'd) | Sahih (Sahîhayn) | eng-bukhari:3257 |
| Cennette Reyyân denen bir kapı var; kıyamet günü oruçlular girer, onlardan başkası giremez; onlar girince kapanır. | Buhârî 1896; Müslim 1152 | Sahih | eng-bukhari:1896; eng-muslim arabicnumber 1152 |
| Allah yolunda iki çift infak eden "Ey Allah'ın kulu, bu hayırdır" diye kapılardan çağrılır: namaz ehli namaz kapısından, cihad ehli cihad kapısından, oruç ehli Reyyân'dan, sadaka ehli sadaka kapısından. Ebû Bekir: "Hepsinden çağrılan olur mu?" "Evet, umarım sen onlardansın." | Buhârî 1897; Buhârî 3666 | Sahih | eng-bukhari:1897, 3666 |
| Abdest alıp şehadet getirene cennetin sekiz kapısı açılır; dilediğinden girer. | Müslim 234 (Ukbe b. Âmir) | Sahih | eng-muslim 234.01 |
| Kapının iki kanadı arası Mekke ile Hecer arası kadardır. | Müslim 194 (şefaat hadisi içinde) | Sahih | eng-muslim 194.02 |
| Cennette yüz derece vardır; her iki derece arası gök ile yer arası kadardır. Allah'tan isteyince Firdevs'i isteyin; cennetin en ortası ve en yücesidir; üstünde Rahmân'ın Arşı vardır, cennet ırmakları ondan kaynar. (Arş/ırmak kısmı ravinin "sanıyorum" notuyla) | Buhârî 2790 (Ebû Hureyre) | Sahih | eng-bukhari:2790 |
| En son cennete girecek adam: "Cennete gir." Dolu zanneder, döner. Üçüncüde: "Dünyanın bir misli ve on katı sana." "Sen kralken benimle mi alay ediyorsun?" Resûlullah (s.a.v.) azı dişleri görününceye kadar güldü. Bu cennetliklerin en düşük derecesindekidir. | Buhârî 6571; Müslim 186 (İbn Mes'ûd) | Sahih | eng-bukhari:6571; eng-muslim 186.01 |
| Allah: "Salih kullarım için hiçbir gözün görmediği, hiçbir kulağın işitmediği, hiçbir insan kalbine gelmeyen şeyler hazırladım." Dilerseniz Secde 32:17'yi okuyun. | Buhârî 3244; Müslim 2824 (Ebû Hureyre) | Sahih | eng-bukhari:3244; eng-muslim 2824.01-.03 |
| Cennette inciden içi boş bir çadır var; genişliği altmış mil; her köşede ehl var, birbirlerini görmezler; mü'minler dolaşır. Adn cennetindekilerin Rableri'ne bakmasına engel olan, Zât'ı üstündeki azamet perdesinden başka bir şey yoktur. | Buhârî 4880; Müslim 2838 (Abdullah b. Kays/Ebû Mûsâ) | Sahih | eng-bukhari:4880; eng-muslim 2838.01-.03 |
| Cennette bir ağaç var; atlı gölgesinde yüz yıl gider (bitiremez). "Uzatılmış gölge"yi (Vâkıa 56:30) okuyun. Cennette yayınızın boyu kadar yer, güneşin doğup battığı her şeyden hayırlıdır. | Buhârî 3252, 3253; Müslim 2826, 2828 | Sahih | eng-bukhari:3252-3253; eng-muslim 2826.01, 2828 |
| Cennet ehli yer içer ama tükürmez, idrar yapmaz, büyük abdest yapmaz, sümkürmez. "Yemek ne olur?" "Geğirme ve misk gibi ter; nefes alır gibi tesbih ve hamd ilham edilir." | Müslim 2835 (Câbir) | Sahih | eng-muslim 2835.01, ar-muslim 2835.01 |
| Bir münâdî seslenir: "Sizin için sağlık var, bir daha hasta olmayacaksınız; yaşamak var, bir daha ölmeyeceksiniz; gençlik var, bir daha yaşlanmayacaksınız; nimet var, bir daha sıkıntı çekmeyeceksiniz." | Müslim 2837 (Ebû Saîd ve Ebû Hureyre) | Sahih | eng-muslim 2837; ar-muslim 2837 |
| Cennete giren nimet içinde olur, sıkıntı görmez; elbisesi eskimez, gençliği tükenmez. | Müslim 2836 | Sahih | eng-muslim 2836; ar-muslim 2836 |
| Cennetliklerin ilk grubu dolunay gibi, sonrakiler gök yüzündeki en parlak yıldız gibi. Idrar, dışkı, sümük, tükürük yok. Tarakları altın, terleri misk, buhurdanlıkları öd ağacı. Kalpleri tek kalp gibi, aralarında ihtilaf ve kin yok; sabah akşam Allah'ı tesbih ederler. Her biri babaları Âdem'in suretinde, altmış zira boyunda. | Müslim 2834 (.01–.05); Buhârî 3254; Buhârî 3326 | Sahih | eng-muslim 2834.01-.05; eng-bukhari:3254, 3326 |
| Cennetlikler tüysüz, sürmeli, gençlikleri bitmez, elbiseleri eskimez. | Tirmizî 2539 | Hasan (Şâkir, Elbânî; Zübeyr Alî Zâî: isnadı hasan) | eng-tirmidhi:2539 |
| Cennetlikler cennete tüysüz, sürmeli, otuz veya otuz üç yaşında girerler. | Tirmizî 2545 (Muâz b. Cebel) | Hasan (Şâkir, Elbânî, Beşşâr Avvâd Maʿrûf; Zübeyr Alî Zâî zayıf dedi) | eng-tirmidhi:2545 — "tartışmalı hasan" etiketiyle |
| Cennet ehli yüz yirmi saftır; seksen safı bu ümmettendir, kırkı diğer ümmetlerden. | Tirmizî 2546 (Büreyde) | Sahih (Şâkir, Elbânî) / Hasan (Beşşâr, Zübeyr) | eng-tirmidhi:2546 |
| Cennette bir çarşı var; her Cuma oraya gelirler, kuzey rüzgârı esip yüzlerine ve elbiselerine misk saçar, güzellikleri artar; ailelerine dönerler, ailesi: "Vallahi güzelliğiniz artmış" der; onlar da: "Siz de bizden sonra artmışsınız" derler. | Müslim 2833 (Enes) | Sahih | eng-muslim 2833; ar-muslim 2833 (سوقا) |
| Cennetin yüksek köşkleri, dünyadan bakınca ufuktaki parlak yıldız gibi görünür; peygamberlerin menzilleri de inananlara ulaşılabilir ("evet, Allah'a inanıp peygamberleri doğrulayanlar ulaşır"). | Müslim 2831 | Sahih | eng-muslim 2831.01-.02 |
| Cennet ehline: "Hoşnut musunuz?" — "Neden olmayalım, kimseye vermediğini verdin." — "Daha üstününü vereyim mi?" — "Bundan üstünü ne olabilir?" — "Üzerinize hoşnutluğumu indiririm, bundan sonra size asla gücenmem." | Müslim 2829 (Ebû Saîd) | Sahih | eng-muslim 2829 |
| Cennetlikler cennete girince Allah: "Bir şey daha ister misiniz?" "Yüzlerimizi ağartmadın mı, cennete sokup ateşten kurtarmadın mı?" Perde kalkar; Rablerine bakmaktan daha sevimli bir şey verilmemiş olur. | Müslim 181 (Suheyb) | Sahih | eng-muslim 181.01-.02 |
| Cennetliklerden biri Rabbinden ekin ekmek için izin ister; eker, bir anda biter, biçilir, dağlar gibi yığılır. Allah: "Al, ey Âdemoğlu, seni hiçbir şey doyurmaz." Bedevî: "Bu adam ya Kureyşli ya Ensârî'dir." Resûlullah güldü. | Buhârî 7519; Buhârî 2348 | Sahih | eng-bukhari:7519, 2348 |
| Resûlullah (s.a.v.) Sidretü'l-Müntehâ'yı ("tarif edilemeyen renklere bürünmüştü") ve cenneti anlatır: "inciden kubbeler/çadırlar vardı, toprağı miskti." | Buhârî 349 (Mi'râc hadisi, Ebû Zer) | Sahih | eng-bukhari:349 (son kısım) |
| Cennet için çalışan yok mu? Cennetin bir benzeri yoktur; parıldayan nur, kıpırdayan reyhan… (Ebû Ümâme/Üsâme b. Zeyd) | İbn Mâce 4332? | **Doğrulanamadı** (veri setinde metin bulunamadı; İbn Kayyım 64. bâbta anıyor) | KULLANILMADI |

### Tartışmalı / kullanılmayan

- **Tirmizî 2526 (toprak zafran, harcı misk, çakılları inci ve yakut, tuğlası altın-gümüş):** Şâkir ve Elbânî "sahih", Zübeyr Alî Zâî "zayıf". Aynı ana fikir Buhârî 349 (toprağı misk) ve Ahmed'de başka tariklerle var; HTML'de "tartışmalı" notuyla geçti, ana iddia Buhârî 349'a dayandırıldı.
- **Tirmizî 2549 (cennet çarşısı, Rabb'i ziyaret, nur kürsüleri):** Şâkir, Elbânî ve Zübeyr: **zayıf**. KULLANILMADI (İbn Kayyım 60–61. bâblar bu rivayeti işler). Cuma pazarı için sahih Müslim 2833 yeterli.
- **Tirmizî 2527 ("içinden dışı, dışından içi görünen köşkler"):** Şâkir ve Elbânî hasan; Zübeyr zayıf. Kısa, "hasan (tartışmalı)" etiketiyle anıldı.
- Kullanıcı ipucundaki "Müslim 2832 = atlı yüz yıl" **yanlış numara**: Müslim 2832 başka bir hadis (ümmetin sevdiği kimseler). Ağaç hadisi Müslim 2826 ve 2828.
- "Müslim 2838 cennet ehline bir arzunuz var mı" ipucu **yanlış**: 2838 inciden çadır hadisidir. "Arzu" sahnesi Buhârî 7519/2348 (ekin ekme), Zuhruf 43:71 ve Fussılet 41:31'dedir.
- "Müslim 2835 tesbih ve hamd nefes gibi ilham edilir": **doğrulandı** (Müslim 2835 Câbir).
- "Müslim 2836-2837": doğrulandı; ihtiyaç giderme ile ilgili kısım 2835 ve 2834'tedir.
- Sekiz kapının tamamının isimleri sahih nasla sıralanmış değildir. Sahih nasta ismi geçenler: Reyyân (oruç), namaz kapısı, cihad kapısı, sadaka kapısı (Buhârî 1897). Diğer isim listeleri (tevbe, hac, kâzımîn vb.) için sahih bir hadis bulunamadı; HTML'de "dört kapının adı bildirilmiştir, kalanı bildirilmemiştir" diye yazıldı.
- İlliyyûn (Mutaffifîn 83:18-21): Kur'an'da "ebrârın kitabı"nın bulunduğu yüksek yer/defter olarak geçer; cennetin bir ismi sayılmaz. Bu yüzden isimler listesinde cennet adı olarak verilmedi; ayrı bir not düşüldü (İlliyyûn'un tefsiri ihtilaflıdır: defter mi, yer mi).
- Bukhari 2790'daki "Arşın üstünde" ifadesi ravinin şüpheyle ("sanıyorum") eklediği cümledir (hadisin metninde "ravi ekledi" notu); HTML'de "bir rivayette" diyerek verildi.
- Hûrilerin ilik görünmesi (Buhârî 3254/Müslim 2834) metinde geçer; ayrıntıya girilmedi (bölüm 08'in odağı mekân ve insanın hali; ilişkiler kısmında Kur'ân'daki "temiz eşler" (Bakara 2:25) ve "tek kalp" vurgulandı).

## Âlim / kitap alıntıları

1. **İbn Abbas (r.a.), İbn Kesîr tefsiri, Bakara 2:25:** "Cennette olanların hiçbiri dünyadakilere benzemez, sadece isimlerde benzerlik vardır." (A'meş → Ebû Sübyân → İbn Abbas.) Kaynak: https://api.quran.com/api/v4/tafsirs/169/by_ayah/2:25 (İbn Kesîr, Abridged) — açılıp okundu. Aynı kaynakta İbn Kesîr: cennet ırmakları ağaç ve odaların altından akar; ırmaklar vadilerde değil; Kevser'in kıyıları oyuk inciden kubbelerdir; toprağı misk, taşları inci ve mücevher (hadis kaynakları İbn Ebî Hâtim).
2. **Said Nursî, Sözler, Yirmi Sekizinci Söz (Cennet), 171-176. sayfalar** (https://risale.online/oku/sozler/yirmi-sekizinci-soz — açılıp okundu):
   - "Evet, cennet bütün lezâiz-i ma'neviyeye medâr olduğu gibi, bütün lezâiz-i cismâniyeye de medârdır."
   - Bir ziyafet temsili: iki dost aynı sofrada; birinin duyguları az gelişmiş, "o nüzhetgâhın binden ve belki milyondan birisini" tadar; diğerinin "bütün zâhirî ve bâtınî duyguları, akıl ve kalb ve his ve latîfeleri o derece mükemmel ve o mertebe inkişâf etmiştir ki… bütün incelikleri, güzellikleri… ayrı ayrı hissedip zevk eder". "Bulundukları cennetler ayrı ayrı da olsa, beraber bulunmalarına mâni olmaz. Çünkü cennetin sekiz tabakası birbirinden yüksek oldukları hâlde, umûmun damı Arş-ı A'zam'dır."
   - "Sen bir ağaca desen: Filan meyveyi bana getir, getirir…" — bu, Nursî'nin kendi yorumu/tahayyülüdür, hadis değil; HTML'de alıntılanmadı.
   - Dünyada tadın "dil" ile, görmenin "göz" ile, kokunun burunla ayrı ayrı lezzeti olduğu; bedenin "tecelliyât-ı esmâ"nın aynası olduğu mantığı.
3. **İbn Kayyım el-Cevziyye, Hâdi'l-Ervâh ilâ Bilâdi'l-Efrâh (Cennetin sıfatı):** archive.org tam metin (OCR) https://archive.org/details/20200306_20200306_1313 (djvu.txt indirilip okundu). Bâb başlıkları (içindekiler): 1 cennet şimdi var mı; 9 kapıların sayısı; 10 genişlikleri; 12 kapılar arası mesafe; 13 cennetin yeri; 17 dereceleri; 18 en yüksek dereceleri; 21 isimleri; 22 cennetlerin sayısı (iki altından, iki gümüşten…); 34 toprağı, çamuru, çakılı ve yapısı; 35 nuru ve beyazlığı; 36 gurfeleri, köşkleri, çadırları; 37 yerlerini tanımaları; 39 yaratılışlarının sıfatı; 41 girince ilk ikram; 42 kokusu ve kaç günlük yoldan alınacağı; 43 cennet müezzininin ezanı; 44 ağaçları, bahçeleri, gölgeleri; 45 meyveleri; 47 nehirleri, pınarları; 48 yiyecek ve içecekleri; 51 çadır, taht, koltukları; 52 hizmetçileri; 53 eşleri; 59 birbirlerini ziyaret ve sohbet; 60 cennetin çarşısı; 61 Rabb'lerini ziyaret; 62 bulut ve yağmur; 63 mülk — hepsi birer kral; 64 "cennet akla gelenin fevkindedir"; 65 Rabb'lerini görmeleri.
   - 21. bâb girişi (Arapça aynen): "ولها عدة أسماء باعتبار صفاتها ومسماها واحد باعتبار الذات" — "Cennetin sıfatlarına göre birkaç ismi vardır; müsemması (adlandırılan) Zât bakımından birdir." Birinci isim "el-Cenne": ağaçla örtülü bahçe; kökü "setr/örtmek" (cenîn, cin, mücen). 
   - 64. bâb başlığı (Arapça): "في أن الجنة فوق ما يخطر بالبال أو يدور في الخلد وأن موضع سوط منها خير من الدنيا وما فيها" — "Cennetin, akla gelen ve hatıra düşenin üstünde olduğu ve oradan bir kamçı yerinin dünya ve içindekilerden hayırlı olduğu hakkında." İbn Kayyım Secde 32:16-17'yi şöyle okur: "Bak, gece namazında gizli tuttukları amele karşılık, Allah onlara bilinmeyen gizli bir mükâfat vermiş" ("وتأمل كيف قابل ما أخفوه من قيام الليل بالجزاء الذي أخفاه لهم مما لا تعلمه نفس") ve yatağından kalkıp korku-ümitle dua edenlerin kaygısını "gözlerin ferahlığı"yla karşıladığını söyler.
4. **Ebû Hureyre'nin "Kur'an'dan okuyun" ifadesi** (Buhârî 3244): kudsî hadisin Secde 32:17 ile birlikte okunması — delil.

Açılmayan / kullanılmayan: Kurtubî et-Tezkire (cennet bâbları) ve Süyûtî Şerhu's-Sudûr bu tur açılamadı (zaman); Gazâlî ve Yasir Qadhi'ye metin içinde atıf YAPILMADI. Hadi'l-Ervâh için sadece OCR içindekiler ve 21., 64. bâblar okundu.

## Canlı sahne / benzetme fikirleri

- "Neden hayal edemiyoruz": doğuştan görmeyene renkleri anlatmak; ama sorun yalnız bizim dünyamızın dilinde değil, İbn Abbas'ın dediği gibi dünyadaki isimlerle ahiretteki gerçeklerin yalnız ismi ortak. Vahiy bize "çevirisi"ni veriyor, orijinalini değil. (Benzetme olarak; delil değil.)
- Kapı sahnesi: kapılardan çağrılış, Ebû Bekir'in "hepsinden çağrılan olur mu?" sorusu; Reyyân kapısından oruçluların girişi, ardından kapanması.
- Yüz derece: iki derece arası gök–yer kadar; yine de aşağıdakiler yukarıdakileri "ufuktaki yıldız gibi" görür (Müslim 2831): mesafe hasret değil, manzara; ve Nursî'nin "her tabakanın damı Arş" temsili: konutlar ayrı, ama birbirini görür.
- Ölçek: en alttaki cennetlik bile dünya + on katı alır (Buhârî 6571). Dünya = 1 birim; en küçük pay = 11 birim (dünya + on kat; Buhârî'nin "veya dünyanın on misli" ifadesi nedeniyle 10 ya da 11 denebilir — şemada "10+ katı" yazıldı).
- Bir cennetlik gününün sahnesi: kapıdan giriş, ilk selam, ilk yudum, "bu daha önce yediğimiz gibi" (Bakara 2:25), ihtiyaç yok, yorgunluk yok (Hicr 15:48), tek kalp, tesbih nefes gibi, Cuma pazarı, akşam Rabb'ine bakış (Müslim 181). Yalnız vahyin bildirdiği unsurlar.
- Ekin ekme sahnesi (Buhârî 7519): "isteğin bile bir lütuf; ama hiçbir şey seni doyurmaz" — insanın doymayan yanı ancak orada şifa bulur.

## Zayıf / tartışmalı notlar

- Tuba ağacı (Ra'd 13:29 ayet sahih; "Tuba cennette yüz yıllık bir ağaç" hadisi Ahmed'de var; derecesi bu turda doğrulanamadı) — hadis KULLANILMADI, sadece ayet.
- Cennet "şimdi var mı yok mu" tartışması (Hâdi'l-Ervâh 1. bâb): Ehl-i Sünnet cennetin şu an mevcut olduğunu söyler; Mu'tezile/Cehmiyye karşı çıkmıştır. HTML'de değinilmedi (bölüm kapsamı dışı).
- Hûri ayrıntıları, "bir cennetlik yüzlerce hizmetçi" gibi sayılar zayıf rivayetlere dayanabildiğinden HTML'de sayı verilmedi.
- Seküler "ağaç emreder" gibi kerâmet/rüya tarzı anlatılar kullanılmadı.

## Açılan kaynak URL'leri

- Kur'an: https://api.quran.com/api/v4/quran/verses/uthmani?chapter_number=… ; https://api.quran.com/api/v4/quran/translations/77?chapter_number=…
- Hadis: https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/{eng-bukhari,ara-bukhari,eng-muslim,ara-muslim,eng-tirmidhi,ara-tirmidhi,eng-ibnmajah}.json
- İbn Kesîr/İbn Abbas: https://api.quran.com/api/v4/tafsirs/169/by_ayah/2:25
- Said Nursî Yirmi Sekizinci Söz: https://risale.online/oku/sozler/yirmi-sekizinci-soz
- İbn Kayyım Hâdi'l-Ervâh: https://archive.org/details/20200306_20200306_1313 (djvu.txt)
- Arama sonuçları (okunmadı, yalnız yönlendirme): saidnursi.de, sorularlarisale.com, hizmetvakfi.org, nurpedia.org

---

## Ek A — Doğrulanmış ayetler (API'den aynen)

- **2:25** | وَبَشِّرِ ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّـٰلِحَـٰتِ أَنَّ لَهُمْ جَنَّـٰتٍ تَجْرِى مِن تَحْتِهَا ٱلْأَنْهَـٰرُ ۖ كُلَّمَا رُزِقُوا۟ مِنْهَا مِن ثَمَرَةٍ رِّزْقًا ۙ قَالُوا۟ هَـٰذَا ٱلَّذِى رُزِقْنَا مِن قَبْلُ ۖ وَأُتُوا۟ بِهِۦ مُتَشَـٰبِهًا ۖ وَلَهُمْ فِيهَآ أَزْوَٰجٌ مُّطَهَّرَةٌ ۖ وَهُمْ فِيهَا خَـٰلِدُونَ | İnananlar ve yararlı işler yapanlara, kendilerine altlarından ırmaklar akan cennetler olduğunu müjdele. Onlara buranın bir ürünü rızık olarak verildiğinde, "Bu daha önce de rızıklandığımızdır" derler. Bunlar, söylediklerinin benzerleri olarak sunulmuştur. Onlara orada tertemiz eşler vardır ve orada temelli kalırlar.
- **3:133** | ۞ وَسَارِعُوٓا۟ إِلَىٰ مَغْفِرَةٍ مِّن رَّبِّكُمْ وَجَنَّةٍ عَرْضُهَا ٱلسَّمَـٰوَٰتُ وَٱلْأَرْضُ أُعِدَّتْ لِلْمُتَّقِينَ | Rabbinizin mağfiretine ve Allah'a karşı gelmekten sakınanlar için hazırlanmış eni gökler ve yer kadar olan cennete koşuşun.
- **57:21** | سَابِقُوٓا۟ إِلَىٰ مَغْفِرَةٍ مِّن رَّبِّكُمْ وَجَنَّةٍ عَرْضُهَا كَعَرْضِ ٱلسَّمَآءِ وَٱلْأَرْضِ أُعِدَّتْ لِلَّذِينَ ءَامَنُوا۟ بِٱللَّهِ وَرُسُلِهِۦ ۚ ذَٰلِكَ فَضْلُ ٱللَّهِ يُؤْتِيهِ مَن يَشَآءُ ۚ وَٱللَّهُ ذُو ٱلْفَضْلِ ٱلْعَظِيمِ | Ey İnsanlar! Rabbiniz tarafından bağışlanmaya, Allah'a ve Peygamberine inananlar için hazırlanmış, genişliği yerle göğün genişliği kadar olan cennete koşusun; bu Allah'ın dilediğine verdiği lütfudur. Allah, büyük lütuf sahibidir.
- **15:45** | إِنَّ ٱلْمُتَّقِينَ فِى جَنَّـٰتٍ وَعُيُونٍ | Allah'a karşı gelmekten sakınanlar ise, cennetlerde, pınar başlarındadırlar.
- **15:46** | ٱدْخُلُوهَا بِسَلَـٰمٍ ءَامِنِينَ | "Oraya güven içinde, esenlikle girin" denilir.
- **15:47** | وَنَزَعْنَا مَا فِى صُدُورِهِم مِّنْ غِلٍّ إِخْوَٰنًا عَلَىٰ سُرُرٍ مُّتَقَـٰبِلِينَ | Biz onların gönüllerinde olan kini çıkardık, artık onlar sedirler üzerinde karşılıklı oturan kardeşlerdir.
- **15:48** | لَا يَمَسُّهُمْ فِيهَا نَصَبٌ وَمَا هُم مِّنْهَا بِمُخْرَجِينَ | Onlar orada bir yorgunluk hissetmezler. Oradan çıkarılacak da değillerdir.
- **32:17** | فَلَا تَعْلَمُ نَفْسٌ مَّآ أُخْفِىَ لَهُم مِّن قُرَّةِ أَعْيُنٍ جَزَآءًۢ بِمَا كَانُوا۟ يَعْمَلُونَ | Yaptıklarına karşılık onlar için saklanan müjdeyi kimse bilmez.
- **35:33** | جَنَّـٰتُ عَدْنٍ يَدْخُلُونَهَا يُحَلَّوْنَ فِيهَا مِنْ أَسَاوِرَ مِن ذَهَبٍ وَلُؤْلُؤًا ۖ وَلِبَاسُهُمْ فِيهَا حَرِيرٌ | Bunlar, Adn cennetlerine girerler. Orada altın bilezikler ve incilerle süslenirler, oradaki elbiseleri de ipektir.
- **35:34** | وَقَالُوا۟ ٱلْحَمْدُ لِلَّهِ ٱلَّذِىٓ أَذْهَبَ عَنَّا ٱلْحَزَنَ ۖ إِنَّ رَبَّنَا لَغَفُورٌ شَكُورٌ | Derler ki: "Bizden üzüntüyü gideren Allah'a hamdolsun. Doğrusu Rabbimiz bağışlayandır, şükrün karşılığını verendir."
- **35:35** | ٱلَّذِىٓ أَحَلَّنَا دَارَ ٱلْمُقَامَةِ مِن فَضْلِهِۦ لَا يَمَسُّنَا فِيهَا نَصَبٌ وَلَا يَمَسُّنَا فِيهَا لُغُوبٌ | "Bizi lütfuyla, temelli kalınacak cennete O yerleştirdi. Orada bize ne bir yorgunluk gelecek ve ne de usanç gelecektir."
- **40:8** | رَبَّنَا وَأَدْخِلْهُمْ جَنَّـٰتِ عَدْنٍ ٱلَّتِى وَعَدتَّهُمْ وَمَن صَلَحَ مِنْ ءَابَآئِهِمْ وَأَزْوَٰجِهِمْ وَذُرِّيَّـٰتِهِمْ ۚ إِنَّكَ أَنتَ ٱلْعَزِيزُ ٱلْحَكِيمُ | "Rabbimiz! Müminleri ve babalarından, eşlerinden, soylarından iyi olanları, kendilerine söz verdiğin Adn cennetlerine koy; şüphesiz güçlü olan, Hakim olan ancak Sensin"
- **41:31** | نَحْنُ أَوْلِيَآؤُكُمْ فِى ٱلْحَيَوٰةِ ٱلدُّنْيَا وَفِى ٱلْـَٔاخِرَةِ ۖ وَلَكُمْ فِيهَا مَا تَشْتَهِىٓ أَنفُسُكُمْ وَلَكُمْ فِيهَا مَا تَدَّعُونَ | "Rabbimiz Allah'tır" deyip sonra da doğrulukta devam edenler, onları, melekler, ölümleri anında: "Korkmayınız, üzülmeyiniz, size söz verilen cennetle sevinin, biz dünya hayatında da, ahirette de size dostuz. Burada, canlarınızın çektiği, umduğunuz şeyler, bağışlayan ve acıyan Allah katından bir ziyafet olarak size sunulur" diyerek inerler.
- **41:32** | نُزُلًا مِّنْ غَفُورٍ رَّحِيمٍ | "Rabbimiz Allah'tır" deyip sonra da doğrulukta devam edenler, onları, melekler, ölümleri anında: "Korkmayınız, üzülmeyiniz, size söz verilen cennetle sevinin, biz dünya hayatında da, ahirette de size dostuz. Burada, canlarınızın çektiği, umduğunuz şeyler, bağışlayan ve acıyan Allah katından bir ziyafet olarak size sunulur" diyerek inerler.
- **52:17** | إِنَّ ٱلْمُتَّقِينَ فِى جَنَّـٰتٍ وَنَعِيمٍ | Allah'a karşı gelmekten sakınanlar, şüphesiz, cennetlerde ve Rablerinin kendilerine verdikleriyle zevk duyarak nimetler içindedirler. Rableri onları cehennem azabından korumuştur.
- **52:18** | فَـٰكِهِينَ بِمَآ ءَاتَىٰهُمْ رَبُّهُمْ وَوَقَىٰهُمْ رَبُّهُمْ عَذَابَ ٱلْجَحِيمِ | Allah'a karşı gelmekten sakınanlar, şüphesiz, cennetlerde ve Rablerinin kendilerine verdikleriyle zevk duyarak nimetler içindedirler. Rableri onları cehennem azabından korumuştur.
- **52:19** | كُلُوا۟ وَٱشْرَبُوا۟ هَنِيٓـًٔۢا بِمَا كُنتُمْ تَعْمَلُونَ | Onlara şöyle denir: "İşlediklerinizden ötürü, dizi dizi tahtlara yaslanarak afiyetle yiyin için." Onlara, ceylan gözlü eşler veririz.
- **52:20** | مُتَّكِـِٔينَ عَلَىٰ سُرُرٍ مَّصْفُوفَةٍ ۖ وَزَوَّجْنَـٰهُم بِحُورٍ عِينٍ | Onlara şöyle denir: "İşlediklerinizden ötürü, dizi dizi tahtlara yaslanarak afiyetle yiyin için." Onlara, ceylan gözlü eşler veririz.
- **52:21** | وَٱلَّذِينَ ءَامَنُوا۟ وَٱتَّبَعَتْهُمْ ذُرِّيَّتُهُم بِإِيمَـٰنٍ أَلْحَقْنَا بِهِمْ ذُرِّيَّتَهُمْ وَمَآ أَلَتْنَـٰهُم مِّنْ عَمَلِهِم مِّن شَىْءٍ ۚ كُلُّ ٱمْرِئٍۭ بِمَا كَسَبَ رَهِينٌ | İnanan, soyları da inançta kendilerine uyan kimselere soylarını da katarız. Onların işlediklerinden hiçbir şey eksiltmeyiz. Herkes kazancına bağlıdır.
- **52:22** | وَأَمْدَدْنَـٰهُم بِفَـٰكِهَةٍ وَلَحْمٍ مِّمَّا يَشْتَهُونَ | Cennette olanlara diledikleri meyve ve etten bol bol veririz.
- **52:23** | يَتَنَـٰزَعُونَ فِيهَا كَأْسًا لَّا لَغْوٌ فِيهَا وَلَا تَأْثِيمٌ | Orada kadeh tokuştururlar; fakat bunda ne bir saçmalama, ne de bir günaha girme vardır.
- **52:24** | ۞ وَيَطُوفُ عَلَيْهِمْ غِلْمَانٌ لَّهُمْ كَأَنَّهُمْ لُؤْلُؤٌ مَّكْنُونٌ | Sedefteki inciler gibi olan gençler yanlarında dolaşırlar.
- **52:25** | وَأَقْبَلَ بَعْضُهُمْ عَلَىٰ بَعْضٍ يَتَسَآءَلُونَ | Birbirlerine dönüp soruşurlar:
- **52:26** | قَالُوٓا۟ إِنَّا كُنَّا قَبْلُ فِىٓ أَهْلِنَا مُشْفِقِينَ | "Doğrusu bundan önce ailemizin yanında bile korku içindeydik; Allah lütfedip bizi kavurucu azabdan korudu; doğrusu bundan önce de O'na yalvarıyorduk; şüphesiz O, iyilik yapandır, acıyandır" derler.
- **52:27** | فَمَنَّ ٱللَّهُ عَلَيْنَا وَوَقَىٰنَا عَذَابَ ٱلسَّمُومِ | "Doğrusu bundan önce ailemizin yanında bile korku içindeydik; Allah lütfedip bizi kavurucu azabdan korudu; doğrusu bundan önce de O'na yalvarıyorduk; şüphesiz O, iyilik yapandır, acıyandır" derler.
- **52:28** | إِنَّا كُنَّا مِن قَبْلُ نَدْعُوهُ ۖ إِنَّهُۥ هُوَ ٱلْبَرُّ ٱلرَّحِيمُ | "Doğrusu bundan önce ailemizin yanında bile korku içindeydik; Allah lütfedip bizi kavurucu azabdan korudu; doğrusu bundan önce de O'na yalvarıyorduk; şüphesiz O, iyilik yapandır, acıyandır" derler.
- **52:21** | وَٱلَّذِينَ ءَامَنُوا۟ وَٱتَّبَعَتْهُمْ ذُرِّيَّتُهُم بِإِيمَـٰنٍ أَلْحَقْنَا بِهِمْ ذُرِّيَّتَهُمْ وَمَآ أَلَتْنَـٰهُم مِّنْ عَمَلِهِم مِّن شَىْءٍ ۚ كُلُّ ٱمْرِئٍۭ بِمَا كَسَبَ رَهِينٌ | İnanan, soyları da inançta kendilerine uyan kimselere soylarını da katarız. Onların işlediklerinden hiçbir şey eksiltmeyiz. Herkes kazancına bağlıdır.
- **56:10** | وَٱلسَّـٰبِقُونَ ٱلسَّـٰبِقُونَ | İyilik işlemekte önde olanlar, karşılıklarını almakta da önde olanlardır.
- **56:11** | أُو۟لَـٰٓئِكَ ٱلْمُقَرَّبُونَ | Naim cennetlerinde Allah'a en çok yaklaştırılmış olanlar işte bunlardır.
- **56:12** | فِى جَنَّـٰتِ ٱلنَّعِيمِ | Naim cennetlerinde Allah'a en çok yaklaştırılmış olanlar işte bunlardır.
- **56:13** | ثُلَّةٌ مِّنَ ٱلْأَوَّلِينَ | Onların büyük kısmı eski ümmetlerden, bir kısmı da sonrakilerdendir.
- **56:14** | وَقَلِيلٌ مِّنَ ٱلْـَٔاخِرِينَ | Onların büyük kısmı eski ümmetlerden, bir kısmı da sonrakilerdendir.
- **56:15** | عَلَىٰ سُرُرٍ مَّوْضُونَةٍ | Mücevheratla işlenmiş tahtlara karşılıklı olarak yaslanırlar.
- **56:16** | مُّتَّكِـِٔينَ عَلَيْهَا مُتَقَـٰبِلِينَ | Mücevheratla işlenmiş tahtlara karşılıklı olarak yaslanırlar.
- **56:17** | يَطُوفُ عَلَيْهِمْ وِلْدَٰنٌ مُّخَلَّدُونَ | Ölümsüz gençler yanlarında, baş ağrısı ve dönmesi vermeyen bembeyaz bir kaynaktan doldurulmuş kaseler, ibrikler, kadehler; seçecekleri meyveler, arzulayacakları kuş eti ile dolaşırlar.
- **56:18** | بِأَكْوَابٍ وَأَبَارِيقَ وَكَأْسٍ مِّن مَّعِينٍ | Ölümsüz gençler yanlarında, baş ağrısı ve dönmesi vermeyen bembeyaz bir kaynaktan doldurulmuş kaseler, ibrikler, kadehler; seçecekleri meyveler, arzulayacakları kuş eti ile dolaşırlar.
- **56:19** | لَّا يُصَدَّعُونَ عَنْهَا وَلَا يُنزِفُونَ | Ölümsüz gençler yanlarında, baş ağrısı ve dönmesi vermeyen bembeyaz bir kaynaktan doldurulmuş kaseler, ibrikler, kadehler; seçecekleri meyveler, arzulayacakları kuş eti ile dolaşırlar.
- **56:20** | وَفَـٰكِهَةٍ مِّمَّا يَتَخَيَّرُونَ | Ölümsüz gençler yanlarında, baş ağrısı ve dönmesi vermeyen bembeyaz bir kaynaktan doldurulmuş kaseler, ibrikler, kadehler; seçecekleri meyveler, arzulayacakları kuş eti ile dolaşırlar.
- **56:21** | وَلَحْمِ طَيْرٍ مِّمَّا يَشْتَهُونَ | Ölümsüz gençler yanlarında, baş ağrısı ve dönmesi vermeyen bembeyaz bir kaynaktan doldurulmuş kaseler, ibrikler, kadehler; seçecekleri meyveler, arzulayacakları kuş eti ile dolaşırlar.
- **56:22** | وَحُورٌ عِينٌ | İşlediklerine karşılık olarak, sedefteki inciler gibi ceylan gözlüler vardır. Orada boş ve günaha sokacak bir söz duymazlar.
- **56:23** | كَأَمْثَـٰلِ ٱللُّؤْلُؤِ ٱلْمَكْنُونِ | İşlediklerine karşılık olarak, sedefteki inciler gibi ceylan gözlüler vardır. Orada boş ve günaha sokacak bir söz duymazlar.
- **56:24** | جَزَآءًۢ بِمَا كَانُوا۟ يَعْمَلُونَ | İşlediklerine karşılık olarak, sedefteki inciler gibi ceylan gözlüler vardır. Orada boş ve günaha sokacak bir söz duymazlar.
- **56:25** | لَا يَسْمَعُونَ فِيهَا لَغْوًا وَلَا تَأْثِيمًا | Sadece selama karşılık selam sözü işitirler.
- **56:26** | إِلَّا قِيلًا سَلَـٰمًا سَلَـٰمًا | Defterleri sağdan verilenler; ne mutlu o sağcılara!
- **56:27** | وَأَصْحَـٰبُ ٱلْيَمِينِ مَآ أَصْحَـٰبُ ٱلْيَمِينِ | Onlar dikensiz sedir ağaçları, salkımları sarkmış muz ağaçları, uzamış gölge altında, çağlayarak akan sular kenarlarında; bitip tükenmeyen ve yasak da edilmeyen bol meyveler arasında; yüksek döşekler üzerindedirler.
- **56:28** | فِى سِدْرٍ مَّخْضُودٍ | Onlar dikensiz sedir ağaçları, salkımları sarkmış muz ağaçları, uzamış gölge altında, çağlayarak akan sular kenarlarında; bitip tükenmeyen ve yasak da edilmeyen bol meyveler arasında; yüksek döşekler üzerindedirler.
- **56:29** | وَطَلْحٍ مَّنضُودٍ | Onlar dikensiz sedir ağaçları, salkımları sarkmış muz ağaçları, uzamış gölge altında, çağlayarak akan sular kenarlarında; bitip tükenmeyen ve yasak da edilmeyen bol meyveler arasında; yüksek döşekler üzerindedirler.
- **56:30** | وَظِلٍّ مَّمْدُودٍ | Onlar dikensiz sedir ağaçları, salkımları sarkmış muz ağaçları, uzamış gölge altında, çağlayarak akan sular kenarlarında; bitip tükenmeyen ve yasak da edilmeyen bol meyveler arasında; yüksek döşekler üzerindedirler.
- **56:31** | وَمَآءٍ مَّسْكُوبٍ | Onlar dikensiz sedir ağaçları, salkımları sarkmış muz ağaçları, uzamış gölge altında, çağlayarak akan sular kenarlarında; bitip tükenmeyen ve yasak da edilmeyen bol meyveler arasında; yüksek döşekler üzerindedirler.
- **56:32** | وَفَـٰكِهَةٍ كَثِيرَةٍ | Onlar dikensiz sedir ağaçları, salkımları sarkmış muz ağaçları, uzamış gölge altında, çağlayarak akan sular kenarlarında; bitip tükenmeyen ve yasak da edilmeyen bol meyveler arasında; yüksek döşekler üzerindedirler.
- **56:33** | لَّا مَقْطُوعَةٍ وَلَا مَمْنُوعَةٍ | Onlar dikensiz sedir ağaçları, salkımları sarkmış muz ağaçları, uzamış gölge altında, çağlayarak akan sular kenarlarında; bitip tükenmeyen ve yasak da edilmeyen bol meyveler arasında; yüksek döşekler üzerindedirler.
- **56:34** | وَفُرُشٍ مَّرْفُوعَةٍ | Onlar dikensiz sedir ağaçları, salkımları sarkmış muz ağaçları, uzamış gölge altında, çağlayarak akan sular kenarlarında; bitip tükenmeyen ve yasak da edilmeyen bol meyveler arasında; yüksek döşekler üzerindedirler.
- **56:35** | إِنَّآ أَنشَأْنَـٰهُنَّ إِنشَآءً | Biz ceylan gözlüleri, defterleri sağdan verilenler için yeniden yaratmışızdır; onları bakire, eşlerine düşkün ve hepsini bir yaşta kılmışızdır.
- **56:36** | فَجَعَلْنَـٰهُنَّ أَبْكَارًا | Biz ceylan gözlüleri, defterleri sağdan verilenler için yeniden yaratmışızdır; onları bakire, eşlerine düşkün ve hepsini bir yaşta kılmışızdır.
- **56:37** | عُرُبًا أَتْرَابًا | Biz ceylan gözlüleri, defterleri sağdan verilenler için yeniden yaratmışızdır; onları bakire, eşlerine düşkün ve hepsini bir yaşta kılmışızdır.
- **56:38** | لِّأَصْحَـٰبِ ٱلْيَمِينِ | Biz ceylan gözlüleri, defterleri sağdan verilenler için yeniden yaratmışızdır; onları bakire, eşlerine düşkün ve hepsini bir yaşta kılmışızdır.
- **56:39** | ثُلَّةٌ مِّنَ ٱلْأَوَّلِينَ | Bunların bir kısmı eski ümmetlerden, bir kısmı da sonrakilerdendir.
- **56:40** | وَثُلَّةٌ مِّنَ ٱلْـَٔاخِرِينَ | Bunların bir kısmı eski ümmetlerden, bir kısmı da sonrakilerdendir.
- **76:5** | إِنَّ ٱلْأَبْرَارَ يَشْرَبُونَ مِن كَأْسٍ كَانَ مِزَاجُهَا كَافُورًا | Şüphesiz iyiler kafur katılmış bir tastan içerler.
- **76:6** | عَيْنًا يَشْرَبُ بِهَا عِبَادُ ٱللَّهِ يُفَجِّرُونَهَا تَفْجِيرًا | Bu ancak Allah'ın kullarının taşıra taşıra içebileceği bir pınardır.
- **76:7** | يُوفُونَ بِٱلنَّذْرِ وَيَخَافُونَ يَوْمًا كَانَ شَرُّهُۥ مُسْتَطِيرًا | Onlar verdikleri sözleri yerine getirirler, fenalığı yaygın olan bir günden korkarlar.
- **76:8** | وَيُطْعِمُونَ ٱلطَّعَامَ عَلَىٰ حُبِّهِۦ مِسْكِينًا وَيَتِيمًا وَأَسِيرًا | Onlar içleri çektiği halde, yiyeceği yoksula, öksüze ve esire yedirirler.
- **76:9** | إِنَّمَا نُطْعِمُكُمْ لِوَجْهِ ٱللَّهِ لَا نُرِيدُ مِنكُمْ جَزَآءً وَلَا شُكُورًا | "Biz sizi ancak Allah rızası için doyuruyoruz, bir karşılık ve teşekkür beklemiyoruz. Doğrusu biz çok asık suratların bulunacağı bir günde Rabbimizden korkarız" derler.
- **76:10** | إِنَّا نَخَافُ مِن رَّبِّنَا يَوْمًا عَبُوسًا قَمْطَرِيرًا | "Biz sizi ancak Allah rızası için doyuruyoruz, bir karşılık ve teşekkür beklemiyoruz. Doğrusu biz çok asık suratların bulunacağı bir günde Rabbimizden korkarız" derler.
- **76:11** | فَوَقَىٰهُمُ ٱللَّهُ شَرَّ ذَٰلِكَ ٱلْيَوْمِ وَلَقَّىٰهُمْ نَضْرَةً وَسُرُورًا | Allah da onları bu yüzden o günün fenalığından korur; onların yüzüne parlaklık ve neşe verir.
- **76:12** | وَجَزَىٰهُم بِمَا صَبَرُوا۟ جَنَّةً وَحَرِيرًا | Sabırlarının karşılığı, cennet ve oradaki ipeklerdir.
- **76:13** | مُّتَّكِـِٔينَ فِيهَا عَلَى ٱلْأَرَآئِكِ ۖ لَا يَرَوْنَ فِيهَا شَمْسًا وَلَا زَمْهَرِيرًا | Orada tahtlara yaslanırlar; orada yakıcı sıcak ve dondurucu soğuk görmezler.
- **76:14** | وَدَانِيَةً عَلَيْهِمْ ظِلَـٰلُهَا وَذُلِّلَتْ قُطُوفُهَا تَذْلِيلًا | Meyve ağaçlarının gölgeleri üzerlerine sarkmış ve onların koparılması kolaylaştırılmıştır.
- **76:15** | وَيُطَافُ عَلَيْهِم بِـَٔانِيَةٍ مِّن فِضَّةٍ وَأَكْوَابٍ كَانَتْ قَوَارِيرَا۠ | Çevrelerinde gümüş kaplar ve billur kaseler dolaştırılır.
- **76:16** | قَوَارِيرَا۟ مِن فِضَّةٍ قَدَّرُوهَا تَقْدِيرًا | Billurları gümüş gibi parlaktır, onları ölçüp ölçüp dağıtırlar.
- **76:17** | وَيُسْقَوْنَ فِيهَا كَأْسًا كَانَ مِزَاجُهَا زَنجَبِيلًا | Orada, zencefil karışık bir tasla içirilirler.
- **76:18** | عَيْنًا فِيهَا تُسَمَّىٰ سَلْسَبِيلًا | O pınara "Selsebil" denir.
- **76:19** | ۞ وَيَطُوفُ عَلَيْهِمْ وِلْدَٰنٌ مُّخَلَّدُونَ إِذَا رَأَيْتَهُمْ حَسِبْتَهُمْ لُؤْلُؤًا مَّنثُورًا | Yanlarında ölümsüz gençler dolaşır; onları gördüğünde saçılmış birer inci sanırsın.
- **76:20** | وَإِذَا رَأَيْتَ ثَمَّ رَأَيْتَ نَعِيمًا وَمُلْكًا كَبِيرًا | Oranın neresine baksan, nimet ve büyük bir saltanat görürsün.
- **76:21** | عَـٰلِيَهُمْ ثِيَابُ سُندُسٍ خُضْرٌ وَإِسْتَبْرَقٌ ۖ وَحُلُّوٓا۟ أَسَاوِرَ مِن فِضَّةٍ وَسَقَىٰهُمْ رَبُّهُمْ شَرَابًا طَهُورًا | Üzerlerinde ince yeşil ipekli, parlak atlastan elbiseler vardır; gümüş bileziklerle süslenmişlerdir Rableri onlara tertemiz içecekler içirir.
- **76:22** | إِنَّ هَـٰذَا كَانَ لَكُمْ جَزَآءً وَكَانَ سَعْيُكُم مَّشْكُورًا | "İşte bu sizin işlediklerinizin karşılığıdır, çalışmalarınız şükre değer" denir.
- **37:40** | إِلَّا عِبَادَ ٱللَّهِ ٱلْمُخْلَصِينَ | Ancak Allah'a içten bağlı kullar bunun dışındadır.
- **37:41** | أُو۟لَـٰٓئِكَ لَهُمْ رِزْقٌ مَّعْلُومٌ | İşte bildirilen rızık ve meyveler onlaradır. Nimet cennetlerinde, karşılıklı tahtlar üzerinde kendilerine ikram olunur.
- **37:42** | فَوَٰكِهُ ۖ وَهُم مُّكْرَمُونَ | İşte bildirilen rızık ve meyveler onlaradır. Nimet cennetlerinde, karşılıklı tahtlar üzerinde kendilerine ikram olunur.
- **37:43** | فِى جَنَّـٰتِ ٱلنَّعِيمِ | İşte bildirilen rızık ve meyveler onlaradır. Nimet cennetlerinde, karşılıklı tahtlar üzerinde kendilerine ikram olunur.
- **37:44** | عَلَىٰ سُرُرٍ مُّتَقَـٰبِلِينَ | İşte bildirilen rızık ve meyveler onlaradır. Nimet cennetlerinde, karşılıklı tahtlar üzerinde kendilerine ikram olunur.
- **37:45** | يُطَافُ عَلَيْهِم بِكَأْسٍ مِّن مَّعِينٍۭ | Baş ağrısı vermeyen, sarhoş etmeyen, içenlere zevk bahşeden bembeyaz bir kaynaktan doldurulmuş kadehler sunulur.
- **37:46** | بَيْضَآءَ لَذَّةٍ لِّلشَّـٰرِبِينَ | Baş ağrısı vermeyen, sarhoş etmeyen, içenlere zevk bahşeden bembeyaz bir kaynaktan doldurulmuş kadehler sunulur.
- **37:47** | لَا فِيهَا غَوْلٌ وَلَا هُمْ عَنْهَا يُنزَفُونَ | Baş ağrısı vermeyen, sarhoş etmeyen, içenlere zevk bahşeden bembeyaz bir kaynaktan doldurulmuş kadehler sunulur.
- **37:48** | وَعِندَهُمْ قَـٰصِرَٰتُ ٱلطَّرْفِ عِينٌ | Yanlarında, örtülü yumurta gibi (bembeyaz), bakışlarını da yalnız eşlerine çevirmiş güzel gözlüler vardır.
- **37:49** | كَأَنَّهُنَّ بَيْضٌ مَّكْنُونٌ | Yanlarında, örtülü yumurta gibi (bembeyaz), bakışlarını da yalnız eşlerine çevirmiş güzel gözlüler vardır.
- **44:51** | إِنَّ ٱلْمُتَّقِينَ فِى مَقَامٍ أَمِينٍ | Allah'a karşı gelmekten sakınmış olanlar ise, güvenli bir yerde, bahçelerde ve pınar başlarındadırlar.
- **44:52** | فِى جَنَّـٰتٍ وَعُيُونٍ | Allah'a karşı gelmekten sakınmış olanlar ise, güvenli bir yerde, bahçelerde ve pınar başlarındadırlar.
- **44:53** | يَلْبَسُونَ مِن سُندُسٍ وَإِسْتَبْرَقٍ مُّتَقَـٰبِلِينَ | İnce ipekten ve parlak atlastan giyinerek karşılıklı otururlar.
- **44:54** | كَذَٰلِكَ وَزَوَّجْنَـٰهُم بِحُورٍ عِينٍ | Bu böyledir; onları iri siyah gözlü hurilerle eşlendiririz.
- **44:55** | يَدْعُونَ فِيهَا بِكُلِّ فَـٰكِهَةٍ ءَامِنِينَ | Orada, güven içinde olarak her yemişi isteyebilirler.
- **44:56** | لَا يَذُوقُونَ فِيهَا ٱلْمَوْتَ إِلَّا ٱلْمَوْتَةَ ٱلْأُولَىٰ ۖ وَوَقَىٰهُمْ عَذَابَ ٱلْجَحِيمِ | Orada, ilk ölümden başka bir ölüm tatmazlar. Rabbin lütfuyla onları cehennem azabından korumuştur. İşte büyük kurtuluş budur.
- **44:57** | فَضْلًا مِّن رَّبِّكَ ۚ ذَٰلِكَ هُوَ ٱلْفَوْزُ ٱلْعَظِيمُ | Orada, ilk ölümden başka bir ölüm tatmazlar. Rabbin lütfuyla onları cehennem azabından korumuştur. İşte büyük kurtuluş budur.
- **83:22** | إِنَّ ٱلْأَبْرَارَ لَفِى نَعِيمٍ | İyiler, şüphesiz, nimet içinde ve tahtlar üzerinde etrafı seyrederler.
- **83:23** | عَلَى ٱلْأَرَآئِكِ يَنظُرُونَ | İyiler, şüphesiz, nimet içinde ve tahtlar üzerinde etrafı seyrederler.
- **83:24** | تَعْرِفُ فِى وُجُوهِهِمْ نَضْرَةَ ٱلنَّعِيمِ | Onları, yüzlerindeki nimet pırıltısından tanırsın.
- **83:25** | يُسْقَوْنَ مِن رَّحِيقٍ مَّخْتُومٍ | Sonunda misk kokusu bırakan, ağzı kapalı saf bir içecekten içerler. İyi şeyler için yarışanlar, bunun için yarışsınlar.
- **83:26** | خِتَـٰمُهُۥ مِسْكٌ ۚ وَفِى ذَٰلِكَ فَلْيَتَنَافَسِ ٱلْمُتَنَـٰفِسُونَ | Sonunda misk kokusu bırakan, ağzı kapalı saf bir içecekten içerler. İyi şeyler için yarışanlar, bunun için yarışsınlar.
- **83:27** | وَمِزَاجُهُۥ مِن تَسْنِيمٍ | Onun katkısı gözdelerin içtiği yüce kaynaktandır.
- **83:28** | عَيْنًا يَشْرَبُ بِهَا ٱلْمُقَرَّبُونَ | Onun katkısı gözdelerin içtiği yüce kaynaktandır.
- **43:67** | ٱلْأَخِلَّآءُ يَوْمَئِذٍۭ بَعْضُهُمْ لِبَعْضٍ عَدُوٌّ إِلَّا ٱلْمُتَّقِينَ | O gün Allah'a karşı gelmekten sakınanlar dışında, dost olanlar birbirine düşman olurlar.
- **43:68** | يَـٰعِبَادِ لَا خَوْفٌ عَلَيْكُمُ ٱلْيَوْمَ وَلَآ أَنتُمْ تَحْزَنُونَ | Allah: "Ey kullarım! Bugün size korku yoktur, siz üzülmeyeceksiniz" der.
- **43:69** | ٱلَّذِينَ ءَامَنُوا۟ بِـَٔايَـٰتِنَا وَكَانُوا۟ مُسْلِمِينَ | Bunlar, ayetlerimize inanmış ve kendilerini Bize vermişlerdir.
- **43:70** | ٱدْخُلُوا۟ ٱلْجَنَّةَ أَنتُمْ وَأَزْوَٰجُكُمْ تُحْبَرُونَ | Şöyle denir: "Siz ve eşleriniz, ağırlanmış olarak cennete giriniz."
- **43:71** | يُطَافُ عَلَيْهِم بِصِحَافٍ مِّن ذَهَبٍ وَأَكْوَابٍ ۖ وَفِيهَا مَا تَشْتَهِيهِ ٱلْأَنفُسُ وَتَلَذُّ ٱلْأَعْيُنُ ۖ وَأَنتُمْ فِيهَا خَـٰلِدُونَ | Onlar için altın kadeh ve tepsiler dolaştırılır, canlarının istediği ve gözlerinin hoşlandığı her şey oradadır. Siz orada ebedi kalacaksınız.
- **43:72** | وَتِلْكَ ٱلْجَنَّةُ ٱلَّتِىٓ أُورِثْتُمُوهَا بِمَا كُنتُمْ تَعْمَلُونَ | İşlediklerinize karşılık, size miras verilen işte bu cennettir.
- **43:73** | لَكُمْ فِيهَا فَـٰكِهَةٌ كَثِيرَةٌ مِّنْهَا تَأْكُلُونَ | Orada sizin için bol yemiş vardır, onlardan yersiniz.
- **55:46** | وَلِمَنْ خَافَ مَقَامَ رَبِّهِۦ جَنَّتَانِ | Rabbine karşı durmaktan korkan kimseye iki cennet vardır.
- **55:47** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:48** | ذَوَاتَآ أَفْنَانٍ | Bu iki cennet türlü ağaçlarla doludur.
- **55:49** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:50** | فِيهِمَا عَيْنَانِ تَجْرِيَانِ | Bu cennetlerden akan iki kaynak vardır.
- **55:51** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:52** | فِيهِمَا مِن كُلِّ فَـٰكِهَةٍ زَوْجَانِ | Bu cennetlerde türlü meyveden çift çift vardır.
- **55:53** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:54** | مُتَّكِـِٔينَ عَلَىٰ فُرُشٍۭ بَطَآئِنُهَا مِنْ إِسْتَبْرَقٍ ۚ وَجَنَى ٱلْجَنَّتَيْنِ دَانٍ | Orada, örtüleri parlak atlastan yataklara yaslanırlar; iki cennetin meyvelerini de kolayca toplarlar.
- **55:55** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:56** | فِيهِنَّ قَـٰصِرَٰتُ ٱلطَّرْفِ لَمْ يَطْمِثْهُنَّ إِنسٌ قَبْلَهُمْ وَلَا جَآنٌّ | Orada, bakışlarını yalnız eşlerine çevirmiş, daha önce ne insan ve ne de cinlerin dokunmuş olduğu eşler vardır.
- **55:57** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:58** | كَأَنَّهُنَّ ٱلْيَاقُوتُ وَٱلْمَرْجَانُ | Onlar yakut ve mercan gibidirler.
- **55:59** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:60** | هَلْ جَزَآءُ ٱلْإِحْسَـٰنِ إِلَّا ٱلْإِحْسَـٰنُ | İyiliğin karşılığı ancak iyilik değil midir?
- **55:61** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:62** | وَمِن دُونِهِمَا جَنَّتَانِ | Bu iki cennetten başka iki cennet daha vardır.
- **55:63** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:64** | مُدْهَآمَّتَانِ | Renkleri koyu yeşildir.
- **55:65** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:66** | فِيهِمَا عَيْنَانِ نَضَّاخَتَانِ | İkisinde de durmadan fışkıran iki kaynak vardır.
- **55:67** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:68** | فِيهِمَا فَـٰكِهَةٌ وَنَخْلٌ وَرُمَّانٌ | İkisinde de türlü türlü meyveler, hurmalıklar ve nar ağaçları vardır.
- **55:69** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:70** | فِيهِنَّ خَيْرَٰتٌ حِسَانٌ | Oralarda iyi huylu güzel kadınlar vardır.
- **55:71** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:72** | حُورٌ مَّقْصُورَٰتٌ فِى ٱلْخِيَامِ | Çadırlar içinde ceylan gözlüler vardır.
- **55:73** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:74** | لَمْ يَطْمِثْهُنَّ إِنسٌ قَبْلَهُمْ وَلَا جَآنٌّ | Onlara daha önce insan da, cin de dokunmamıştır.
- **55:75** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:76** | مُتَّكِـِٔينَ عَلَىٰ رَفْرَفٍ خُضْرٍ وَعَبْقَرِىٍّ حِسَانٍ | Cennetlikler orada yeşil yastıklara ve harikulade işlemeli döşeklere yaslanırlar.
- **55:77** | فَبِأَىِّ ءَالَآءِ رَبِّكُمَا تُكَذِّبَانِ | Öyleyken, Rabbinizin nimetlerinden hangisini yalanlarsınız?
- **55:78** | تَبَـٰرَكَ ٱسْمُ رَبِّكَ ذِى ٱلْجَلَـٰلِ وَٱلْإِكْرَامِ | Büyük ve pek cömert olan Rabbinin adı ne yücedir!
- **78:31** | إِنَّ لِلْمُتَّقِينَ مَفَازًا | Doğrusu, Allah'a karşı gelmekten sakınanlara kurtuluş, bahçeler, bağlar, yaşıtlar ve dolu kadehler vardır.
- **78:32** | حَدَآئِقَ وَأَعْنَـٰبًا | Doğrusu, Allah'a karşı gelmekten sakınanlara kurtuluş, bahçeler, bağlar, yaşıtlar ve dolu kadehler vardır.
- **78:33** | وَكَوَاعِبَ أَتْرَابًا | Doğrusu, Allah'a karşı gelmekten sakınanlara kurtuluş, bahçeler, bağlar, yaşıtlar ve dolu kadehler vardır.
- **78:34** | وَكَأْسًا دِهَاقًا | Doğrusu, Allah'a karşı gelmekten sakınanlara kurtuluş, bahçeler, bağlar, yaşıtlar ve dolu kadehler vardır.
- **78:35** | لَّا يَسْمَعُونَ فِيهَا لَغْوًا وَلَا كِذَّٰبًا | Orada boş ve yalan söz işitmezler.
- **78:36** | جَزَآءً مِّن رَّبِّكَ عَطَآءً حِسَابًا | Bunlar Rabbinin katından, hesabları karşılığı verilenlerdir.
- **88:8** | وُجُوهٌ يَوْمَئِذٍ نَّاعِمَةٌ | İnanmış olanların yüzleri, o gün, pırıl pırıldır.
- **88:9** | لِّسَعْيِهَا رَاضِيَةٌ | Yaptıklarından hoşnuddurlar.
- **88:10** | فِى جَنَّةٍ عَالِيَةٍ | Yüksek bir cennettedirler.
- **88:11** | لَّا تَسْمَعُ فِيهَا لَـٰغِيَةً | Orada boş söz işitmezler.
- **88:12** | فِيهَا عَيْنٌ جَارِيَةٌ | Orada akan kaynak vardır.
- **88:13** | فِيهَا سُرُرٌ مَّرْفُوعَةٌ | Orada, yükseltilmiş tahtlar vardır.
- **88:14** | وَأَكْوَابٌ مَّوْضُوعَةٌ | Yerleştirilmiş kaseler,
- **88:15** | وَنَمَارِقُ مَصْفُوفَةٌ | Sıra sıra yastıklar,
- **88:16** | وَزَرَابِىُّ مَبْثُوثَةٌ | Serilmiş, yumuşak tüylü halılar vardır.
- **89:27** | يَـٰٓأَيَّتُهَا ٱلنَّفْسُ ٱلْمُطْمَئِنَّةُ | Ey huzur içinde olan can!
- **89:28** | ٱرْجِعِىٓ إِلَىٰ رَبِّكِ رَاضِيَةً مَّرْضِيَّةً | O, senden, sen de O'ndan hoşnut olarak Rabbine dön!
- **89:29** | فَٱدْخُلِى فِى عِبَـٰدِى | Ey can! İyi kullarımın arasına gir.
- **89:30** | وَٱدْخُلِى جَنَّتِى | Cennetime gir.
- **75:22** | وُجُوهٌ يَوْمَئِذٍ نَّاضِرَةٌ | O gün bir takım yüzler Rablerine bakıp parlayacaktır.
- **75:23** | إِلَىٰ رَبِّهَا نَاظِرَةٌ | O gün bir takım yüzler Rablerine bakıp parlayacaktır.
- **10:9** | إِنَّ ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّـٰلِحَـٰتِ يَهْدِيهِمْ رَبُّهُم بِإِيمَـٰنِهِمْ ۖ تَجْرِى مِن تَحْتِهِمُ ٱلْأَنْهَـٰرُ فِى جَنَّـٰتِ ٱلنَّعِيمِ | İnananlar ve yararlı iş yapanları, imanlarına karşılık Rableri doğru yola eriştirir; nimet cennetlerinde onların altlarından ırmaklar akar.
- **10:10** | دَعْوَىٰهُمْ فِيهَا سُبْحَـٰنَكَ ٱللَّهُمَّ وَتَحِيَّتُهُمْ فِيهَا سَلَـٰمٌ ۚ وَءَاخِرُ دَعْوَىٰهُمْ أَنِ ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ | Oradaki duaları: "Münezzehsin ey Allah'ım", dirlik temennileri: "Selam size" ve dualarının sonu da: "Alemlerin Rabbi Allah'a hamd olsun"dur.
- **10:26** | ۞ لِّلَّذِينَ أَحْسَنُوا۟ ٱلْحُسْنَىٰ وَزِيَادَةٌ ۖ وَلَا يَرْهَقُ وُجُوهَهُمْ قَتَرٌ وَلَا ذِلَّةٌ ۚ أُو۟لَـٰٓئِكَ أَصْحَـٰبُ ٱلْجَنَّةِ ۖ هُمْ فِيهَا خَـٰلِدُونَ | İyi davrananlara; daima daha iyisi ve üstünü verilir. Onların yüzlerine ne bir karalık, ne de zillet bulaşır. İşte onlar cennetliklerdir, orada temelli kalırlar.
- **50:35** | لَهُم مَّا يَشَآءُونَ فِيهَا وَلَدَيْنَا مَزِيدٌ | Orada dilediklerini bulurlar. Katımızda fazlası da vardır.
- **19:62** | لَّا يَسْمَعُونَ فِيهَا لَغْوًا إِلَّا سَلَـٰمًا ۖ وَلَهُمْ رِزْقُهُمْ فِيهَا بُكْرَةً وَعَشِيًّا | Orada boş sözler değil sadece esenlik veren sözler işitirler. Orada rızıklarını sabah akşam hazır bulurlar.
- **18:30** | إِنَّ ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّـٰلِحَـٰتِ إِنَّا لَا نُضِيعُ أَجْرَ مَنْ أَحْسَنَ عَمَلًا | İyi hareket edenin ecrini zayi etmeyiz. Doğrusu, inanıp yararlı iş yapanlara, işte onlara, içlerinden ırmaklar akan Adn cennetleri vardır. Orada altın bilezikler takınırlar, ince ve kalın ipekliden yeşil elbiseler giyerek tahtları üzerinde otururlar. Ne güzel bir mükafat ve ne güzel yaslanacak yer!
- **18:31** | أُو۟لَـٰٓئِكَ لَهُمْ جَنَّـٰتُ عَدْنٍ تَجْرِى مِن تَحْتِهِمُ ٱلْأَنْهَـٰرُ يُحَلَّوْنَ فِيهَا مِنْ أَسَاوِرَ مِن ذَهَبٍ وَيَلْبَسُونَ ثِيَابًا خُضْرًا مِّن سُندُسٍ وَإِسْتَبْرَقٍ مُّتَّكِـِٔينَ فِيهَا عَلَى ٱلْأَرَآئِكِ ۚ نِعْمَ ٱلثَّوَابُ وَحَسُنَتْ مُرْتَفَقًا | İyi hareket edenin ecrini zayi etmeyiz. Doğrusu, inanıp yararlı iş yapanlara, işte onlara, içlerinden ırmaklar akan Adn cennetleri vardır. Orada altın bilezikler takınırlar, ince ve kalın ipekliden yeşil elbiseler giyerek tahtları üzerinde otururlar. Ne güzel bir mükafat ve ne güzel yaslanacak yer!
- **22:23** | إِنَّ ٱللَّهَ يُدْخِلُ ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّـٰلِحَـٰتِ جَنَّـٰتٍ تَجْرِى مِن تَحْتِهَا ٱلْأَنْهَـٰرُ يُحَلَّوْنَ فِيهَا مِنْ أَسَاوِرَ مِن ذَهَبٍ وَلُؤْلُؤًا ۖ وَلِبَاسُهُمْ فِيهَا حَرِيرٌ | Doğrusu Allah, inanıp yararlı iş işleyenleri, içlerinden ırmaklar akan cennetlere koyar. Orada altın bilezikler ve inciler takınırlar. Oradaki elbiseleri de ipektendir.
- **57:12** | يَوْمَ تَرَى ٱلْمُؤْمِنِينَ وَٱلْمُؤْمِنَـٰتِ يَسْعَىٰ نُورُهُم بَيْنَ أَيْدِيهِمْ وَبِأَيْمَـٰنِهِم بُشْرَىٰكُمُ ٱلْيَوْمَ جَنَّـٰتٌ تَجْرِى مِن تَحْتِهَا ٱلْأَنْهَـٰرُ خَـٰلِدِينَ فِيهَا ۚ ذَٰلِكَ هُوَ ٱلْفَوْزُ ٱلْعَظِيمُ | İnanmış erkek ve kadınları, defterleri sağdan verilmiş ve ışıkları önlerinde olarak giderken gördüğün gün onlara şöyle denecektir: "Müjde; bugün içlerinden ırmaklar akan, içinde temelli kalacağınız cennetler sizindir." İşte bu büyük kurtuluştur.
- **66:8** | يَـٰٓأَيُّهَا ٱلَّذِينَ ءَامَنُوا۟ تُوبُوٓا۟ إِلَى ٱللَّهِ تَوْبَةً نَّصُوحًا عَسَىٰ رَبُّكُمْ أَن يُكَفِّرَ عَنكُمْ سَيِّـَٔاتِكُمْ وَيُدْخِلَكُمْ جَنَّـٰتٍ تَجْرِى مِن تَحْتِهَا ٱلْأَنْهَـٰرُ يَوْمَ لَا يُخْزِى ٱللَّهُ ٱلنَّبِىَّ وَٱلَّذِينَ ءَامَنُوا۟ مَعَهُۥ ۖ نُورُهُمْ يَسْعَىٰ بَيْنَ أَيْدِيهِمْ وَبِأَيْمَـٰنِهِمْ يَقُولُونَ رَبَّنَآ أَتْمِمْ لَنَا نُورَنَا وَٱغْفِرْ لَنَآ ۖ إِنَّكَ عَلَىٰ كُلِّ شَىْءٍ قَدِيرٌ | Ey inananlar! Yürekten tevbe ederek Allah'a dönün ki, Rabbiniz kötülüklerinizi örtsün, sizi, içlerinden ırmaklar akan cennetlere koysun. Allah'ın Peygamberini ve onunla beraber olan müminleri utandırmayacağı o gün, ışıkları önlerinde ve defterleri sağlarından verilmiş olarak yürürler ve: "Rabbimiz! Işığımızı tamamla, bizi bağışla, doğrusu Sen herşeye Kadir'sin" derler.
- **30:15** | فَأَمَّا ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّـٰلِحَـٰتِ فَهُمْ فِى رَوْضَةٍ يُحْبَرُونَ | Ama inanıp yararlı iş işleyenler, ağırlanacakları bir cennette bulunurlar.
- **25:75** | أُو۟لَـٰٓئِكَ يُجْزَوْنَ ٱلْغُرْفَةَ بِمَا صَبَرُوا۟ وَيُلَقَّوْنَ فِيهَا تَحِيَّةً وَسَلَـٰمًا | İşte onlar, sabrettiklerinden ötürü cennetin en yüksek dereceleriyle mükafatlandırılırlar. Orada esenlik ve dirlik dilekleriyle karşılanırlar.
- **25:76** | خَـٰلِدِينَ فِيهَا ۚ حَسُنَتْ مُسْتَقَرًّا وَمُقَامًا | Orada temellidirler. Orası ne güzel bir yer ve ne güzel duraktır!
- **53:13** | وَلَقَدْ رَءَاهُ نَزْلَةً أُخْرَىٰ | And olsun ki o, Cebrail'i sınırın sonunda başka bir inişinde de görmüştür.
- **53:14** | عِندَ سِدْرَةِ ٱلْمُنتَهَىٰ | And olsun ki o, Cebrail'i sınırın sonunda başka bir inişinde de görmüştür.
- **53:15** | عِندَهَا جَنَّةُ ٱلْمَأْوَىٰٓ | Orada Me'va cenneti vardır.
- **53:16** | إِذْ يَغْشَى ٱلسِّدْرَةَ مَا يَغْشَىٰ | Sidre'yi bürüyen bürüyordu.
- **53:17** | مَا زَاغَ ٱلْبَصَرُ وَمَا طَغَىٰ | Gözü oradan ne kaydı ve ne de onu aştı.
- **53:18** | لَقَدْ رَأَىٰ مِنْ ءَايَـٰتِ رَبِّهِ ٱلْكُبْرَىٰٓ | And olsun ki Rabbinin varlığının büyük delillerini gördü.
- **13:29** | ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّـٰلِحَـٰتِ طُوبَىٰ لَهُمْ وَحُسْنُ مَـَٔابٍ | İnanan ve yararlı iş işleyen kimseler için hoş bir hayat ve dönülecek güzel bir yer vardır.
- **83:18** | كَلَّآ إِنَّ كِتَـٰبَ ٱلْأَبْرَارِ لَفِى عِلِّيِّينَ | Ama iyilerin defteri yüksek katlardadır.
- **83:19** | وَمَآ أَدْرَىٰكَ مَا عِلِّيُّونَ | O yüksek katların ne olduğunu sen bilir misin?
- **83:20** | كِتَـٰبٌ مَّرْقُومٌ | O, gözde meleklerin gördüğü, yazılı bir kitapdır.
- **83:21** | يَشْهَدُهُ ٱلْمُقَرَّبُونَ | O, gözde meleklerin gördüğü, yazılı bir kitapdır.
- **39:20** | لَـٰكِنِ ٱلَّذِينَ ٱتَّقَوْا۟ رَبَّهُمْ لَهُمْ غُرَفٌ مِّن فَوْقِهَا غُرَفٌ مَّبْنِيَّةٌ تَجْرِى مِن تَحْتِهَا ٱلْأَنْهَـٰرُ ۖ وَعْدَ ٱللَّهِ ۖ لَا يُخْلِفُ ٱللَّهُ ٱلْمِيعَادَ | Fakat, Rablerinden sakınanlara, üst üste bina edilmiş köşkler vardır; altlarından ırmaklar akar. Bu, Allah'ın verdiği sözdür, Allah verdiği sözden caymaz.
- **39:73** | وَسِيقَ ٱلَّذِينَ ٱتَّقَوْا۟ رَبَّهُمْ إِلَى ٱلْجَنَّةِ زُمَرًا ۖ حَتَّىٰٓ إِذَا جَآءُوهَا وَفُتِحَتْ أَبْوَٰبُهَا وَقَالَ لَهُمْ خَزَنَتُهَا سَلَـٰمٌ عَلَيْكُمْ طِبْتُمْ فَٱدْخُلُوهَا خَـٰلِدِينَ | Rablerine karşı gelmekten sakınanlar, bölük bölük cennete götürülürler. Oraya varıp da kapıları açıldığında, bekçileri onlara: "Selam size, hoş geldiniz! Temelli olarak buraya girin" derler.
- **39:74** | وَقَالُوا۟ ٱلْحَمْدُ لِلَّهِ ٱلَّذِى صَدَقَنَا وَعْدَهُۥ وَأَوْرَثَنَا ٱلْأَرْضَ نَتَبَوَّأُ مِنَ ٱلْجَنَّةِ حَيْثُ نَشَآءُ ۖ فَنِعْمَ أَجْرُ ٱلْعَـٰمِلِينَ | Onlar: "Bize verdiği sözde duran ve bizi bu yere varis kılan Allah'a hamdolsun. Cennette istediğimiz yerde oturabiliriz. Yararlı iş işleyenlerin ecri ne güzelmiş!" derler.
- **39:75** | وَتَرَى ٱلْمَلَـٰٓئِكَةَ حَآفِّينَ مِنْ حَوْلِ ٱلْعَرْشِ يُسَبِّحُونَ بِحَمْدِ رَبِّهِمْ ۖ وَقُضِىَ بَيْنَهُم بِٱلْحَقِّ وَقِيلَ ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَـٰلَمِينَ | Melekleri, arşın etrafını çevirmiş oldukları halde, Rablerini hamd ile överken görürsün. Artık insanların aralarında adaletle hüküm olunmuştur. "Övgü, Alemlerin Rabbi olan Allah içindir" denir.
- **7:43** | وَنَزَعْنَا مَا فِى صُدُورِهِم مِّنْ غِلٍّ تَجْرِى مِن تَحْتِهِمُ ٱلْأَنْهَـٰرُ ۖ وَقَالُوا۟ ٱلْحَمْدُ لِلَّهِ ٱلَّذِى هَدَىٰنَا لِهَـٰذَا وَمَا كُنَّا لِنَهْتَدِىَ لَوْلَآ أَنْ هَدَىٰنَا ٱللَّهُ ۖ لَقَدْ جَآءَتْ رُسُلُ رَبِّنَا بِٱلْحَقِّ ۖ وَنُودُوٓا۟ أَن تِلْكُمُ ٱلْجَنَّةُ أُورِثْتُمُوهَا بِمَا كُنتُمْ تَعْمَلُونَ | Cennette altlarından ırmaklar akarken gönüllerinden kini çıkarıp atarız. "Bizi buraya eriştiren Allah'a hamdolsun. Eğer Allah bizi doğru yola iletmeseydi, biz doğru yolu bulamazdık. And olsun ki Rabbimizin peygamberleri bize gerçeği getirmiştir" derler. Onlara, "İşlediğinize karşılık işte mirasçısı olduğunuz cennet" diye seslenilir.
- **6:127** | ۞ لَهُمْ دَارُ ٱلسَّلَـٰمِ عِندَ رَبِّهِمْ ۖ وَهُوَ وَلِيُّهُم بِمَا كَانُوا۟ يَعْمَلُونَ | Rablerinin katında selamet yurdu onlarındır. O, işlediklerinden ötürü onların dostudur.
- **10:25** | وَٱللَّهُ يَدْعُوٓا۟ إِلَىٰ دَارِ ٱلسَّلَـٰمِ وَيَهْدِى مَن يَشَآءُ إِلَىٰ صِرَٰطٍ مُّسْتَقِيمٍ | Allah, cennete çağırır ve dilediğini doğru yola eriştirir.
- **40:39** | يَـٰقَوْمِ إِنَّمَا هَـٰذِهِ ٱلْحَيَوٰةُ ٱلدُّنْيَا مَتَـٰعٌ وَإِنَّ ٱلْـَٔاخِرَةَ هِىَ دَارُ ٱلْقَرَارِ | "Ey milletim! Şüphesiz bu dünya hayatı geçicidir, ama ahiret, doğrusu işte o, kalınacak yurttur."
- **25:15** | قُلْ أَذَٰلِكَ خَيْرٌ أَمْ جَنَّةُ ٱلْخُلْدِ ٱلَّتِى وُعِدَ ٱلْمُتَّقُونَ ۚ كَانَتْ لَهُمْ جَزَآءً وَمَصِيرًا | De ki: "Bu mu iyidir, yoksa Allah'a karşı gelmekten sakınanlara mükafat ve gidilecek yer olarak söz verilen ebedi cennet mi daha iyidir?"
- **32:19** | أَمَّا ٱلَّذِينَ ءَامَنُوا۟ وَعَمِلُوا۟ ٱلصَّـٰلِحَـٰتِ فَلَهُمْ جَنَّـٰتُ ٱلْمَأْوَىٰ نُزُلًۢا بِمَا كَانُوا۟ يَعْمَلُونَ | İnanıp yararlı iş işleyenlere gelince, onların yaptıklarına karşılık, varacakları cennet konakları vardır.

## Ek B — Yazım sırasında yapılan ek doğrulamalar

- Müslim 2833'ün Arapçasında rüzgârın "yüzlerine ve elbiselerine" savurduğu şeyin adı geçmez ("fe-tahsû fî vucûhihim ve siyâbihim"); İngilizce çeviri "fragrance" ekler. HTML'de "(koku)" parantezi ile verildi, "misk" denmedi.
- Buhârî 2790 Arapçası açılıp kontrol edildi: "fe-innehû evsatu'l-cenneti ve a'le'l-cenneti, urâhu fevkahû arşu'r-Rahmân ve minhu tefecceru enhâru'l-cenne" — "urâhu" (kanaatimce) ravinin şüphe kaydıdır.
- Said Nursî alıntıları risale.online sayfasından (Latince basılı nüsha, s.171-176) harfi harfine alındı; yalnız yazım işareti (‘ ve â/î) korunarak.
- Sağlam isim listesi (HTML): Firdevs 18:107, 23:11; Adn 9:72; Naîm 56:12; Me'vâ 32:19, 53:15; Dârü's-Selâm 6:127, 10:25; Dârü'l-Karâr 40:39; Cennetü'l-Huld 25:15 — hepsi API'den doğrulandı. "Dârü'l-Huld" ifadesi Kur'an'da cehennem için geçtiği için (Fussılet 41:28) cennet ismi olarak verilmedi (Fussılet 41:28 bu turda çekilmedi, Arapça kök bilgisine dayalı not; sonra doğrulanmalı).
- Boş/istisna: Kurtubî ve Süyûtî'ye bu bölümde atıf yok.
