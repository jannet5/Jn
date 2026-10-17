# 03 — Ölçü Sayı mı, Delil mi? (araştırma notları)

## Özet
Kur'an "çoğunluğa uy" demez; ama "azınlığa uy" da demez. Ölçü: burhan (açık delil), ilim (bilgi), vahiy. Çoğunluğa itaat ayetinde (En'âm 6:116) sorun sayı değil, çoğunluğun dayanağının *zan ve tahmin* olması. Azınlık için de aynı: Nûh'un yanındaki az kişi (Hûd 11:40) bir *olay tasviri*; "az olan haklıdır" kuralı değil. Buhârî 5752'deki ümmetler hadisi aynı sahnede hem "yanında hiç kimse olmayan peygamber"i hem "ufku kaplayan ümmet"i gösterir: sayı iki yönde de ölçü değil. Uzmana sormak (Nahl 16:43 / Enbiyâ 21:7) sürüye katılmak değil, ilme başvurmaktır; ayetin sebeb-i nüzulü dar (müşrikler Ehl-i Kitap'a "peygamberler beşer miydi" diye sorsun), ama tefsirciler (Kurtubî, Sa'dî) ilkeyi genellemiştir.

## Doğrulanmış ayetler (hepsi veri/kuran_tam.json = api.quran.com uthmani + Diyanet 77; araclar/ayet.py ile)
| ref | not |
|---|---|
| En'âm 6:116 | "Yeryüzündekilerin çoğunluğuna itaat edersen seni Allah yolundan saptırırlar. Onlar ancak zanna uyarlar, sadece tahminde bulunurlar." |
| Yûsuf 12:103 | Diyanet kaydı 12:102+103'ü tek girdide birleştirmiş (iki anahtarda aynı metin). Meal: "...sen ne kadar yürekten istersen iste, insanların çoğu inanmazlar." |
| Hûd 11:40 | "Pek az kimse onunla beraber inanmıştı." |
| Sebe' 34:13 | "...Kullarımdan şükredenler pek azdır." |
| Ankebût 29:14 | Nûh aralarında "bin seneden elli yıl eksik" kaldı. (Diyanet "And olsun ki" ile başlıyor.) |
| Bakara 2:249 | "Nice az topluluk çok topluluğa Allah'ın izniyle üstün gelmiştir" (+ ırmak denemesi: çoğu içti, "pek azı hariç") |
| Bakara 2:111 | "...De ki: Sözünüz doğru ise delillerinizi getirin." (bağlam: "Yahudi veya Hıristiyan olmayan cennete girmez" iddiası) |
| Neml 27:64 | "...açık delilinizi getirin" |
| Nahl 16:43 | Diyanet 16:43+44 tek girdide birleşik; meal "Bilmiyorsanız kitablılara sorun." (ehl-i zikr = "kitablılar") |
| Enbiyâ 21:7 | aynı ifade, kısa meal. |
| İsrâ 17:36 | "Bilmediğin şeyin ardına düşme..." |
| Hucurât 49:6 | "...onun iç yüzünü araştırın..." |
| Necm 53:28 | "...Sanı ise şüphesiz gerçeği ifade etmez." |
| Yûnus 10:36 | "Onların çoğu zanna uyarlar; ... zan, hakikat karşısında bir şey ifade etmez." |
| En'âm 6:148-149 | "Bize karşı çıkarabileceğiniz bir bilginiz var mı?... Üstün delil Allah'ın delilidir." |
| Bakara 2:170, Mâide 5:104 | "ya ataları bir şey bilmeyen ve doğru yolda olmayan kimseler idiyseler?" (atalar/gelenek de delil değil) |
| Zümer 39:17-18 | Diyanet iki ayeti tek girdide birleştirmiş: "Dinleyip de en güzel söze uyan kullarımı müjdele…" |
| Zuhruf 43:22 | "babalarımızı bir din üzerinde bulduk" |
| Yûnus 10:35 | "Gerçeğe eriştiren mi uyulmağa daha layıktır?" |

## Doğrulanmış hadisler
fawazahmed0/hadith-api (eng-/ara-bukhari, eng-/ara-muslim, eng-/ara-tirmidhi) dosyalarından Arapça + İngilizce metin okundu.
| TR anlam | kaynak+no | derece | nasıl doğrulandı |
|---|---|---|---|
| "Bana ümmetler gösterildi. Bir peygamber geçiyordu, yanında bir adam; bir peygamber, yanında iki adam; bir peygamber, yanında bir topluluk (er-raht); bir peygamber, yanında kimse yok. Ufku kapatan büyük bir kalabalık gördüm... 'Bu Mûsâ ve kavmidir.' ... 'Bunlar senin ümmetindir; bunlarla birlikte hesapsız cennete girecek yetmiş bin kişi vardır.'" | Buhârî 5752 (Kitâbü't-Tıbb; İbn Abbas) | Sahih (Buhârî) | ara-bukhari 5752 ve eng-bukhari 5752 (book 76, hadith 67) okundu |
| aynı hadisin Müslim rivayeti: "...bir peygamber ve yanında küçük bir topluluk (er-rüheyt), bir peygamber ve yanında bir adam ve iki adam, bir peygamber ve yanında hiç kimse yok. Derken bana büyük bir kara (kalabalık) gösterildi... 'Bu Mûsâ ve kavmi; ufka bak.' ... diğer ufka bak... 'Bu senin ümmetin; bunlarla birlikte hesapsız ve azapsız cennete girecek yetmiş bin kişi var.'" | Müslim, Îmân 220 (Abdülbâkî numarası; İbn Abbas, Husayn b. Abdirrahman – Saîd b. Cübeyr yoluyla) | Sahih (Müslim) | ara-muslim girdisi (API'de "book 1, hadith 433", hadithnumber 527) Arapça metin okundu; metin brief'teki ifadeyle (bir peygamber ile bir iki kişi, hiç kimse) birebir uyuşuyor. sunnah.com 403 verdiği için sitede ayrıca doğrulanamadı; Abdülbâkî numarası bilgisi hafızadan, metin eşleşmesi ile desteklendi. |
| Buhârî 6541 (Rikâk; İbn Abbas): "...bir peygamber büyük bir topluluk ile, bir peygamber küçük bir topluluk ile, bir peygamber on kişi ile, bir peygamber beş kişi ile, bir peygamber yalnız geçiyordu..." (sayılar: on / beş / yalnız) | Buhârî 6541 | Sahih | eng-bukhari 6541 (book 81, hadith 130) okundu (Arapçası ayrıca açılmadı) |
| Buhârî 6472: yetmiş bin kişi hesapsız cennete girecek (ruk'ya yapmayan, uğursuzluğa inanmayan, Rabbine tevekkül eden) | Buhârî 6472 | Sahih | eng-bukhari 6472 okundu; ümmetler sahnesi bu numarada yok, sahne 5752 ve 6541'de |
| "İslâm garip (yabancı/tek başına) başladı, başladığı gibi garip dönecek; ne mutlu gurebâya." | Müslim 145 (Ebû Hureyre; ayrıca İbn Ömer rivayeti: iki mescid arasına sığınır) | Sahih | ara-muslim/eng-muslim (API hadithnumber 372-373) okundu |
| aynı lafız (İbn Mes'ûd) | Tirmizî 2629 | Sahih (Elbânî, Ahmed Şâkir; Tirmizî: hasen sahih garîb) | eng-tirmidhi grades alanı |
| "...Gurebâ: halkın benden sonra sünnetimden bozduğunu ıslah edenlerdir" ilavesi | Tirmizî 2630 | **ÇOK ZAYIF** (Elbânî, Ahmed Şâkir: çok zayıf; Zübeyr Ali Zaî: zayıf; ravi Kesîr b. Abdillah) | eng-tirmidhi grades; sayfada ilave olarak kullanılmadı, "dikkat" kutusunda zayıf olduğu belirtilecek |
| "İnsanlar helâk oldu" diyen kişi en çok helâk olandır | Müslim 2623 (Ebû Hureyre) | Sahih | ara-muslim API hadithnumber 6683 (book 45, hadith 180) okundu. "ehlekühüm/ehlekuhüm" rivayet farkı Ebû İshak'a ait not da var. |

## Âlim/kitap alıntıları (gerçekten açılanlar)
1. **Gazâlî, el-Munkız mine'd-dalâl** — archive.org: `Heliopolis1957_gmail_2001_201805` (2001 baskısı, `_djvu.txt`), s. 58 civarı:
   «وهذه عادة ضعفاء العقول، يعرفون الحق بالرجال، لا الرجال بالحق. والعاقل يقتدي بقول أمير المؤمنين علي رضي الله عنه حيث قال: لا تعرف الحق بالرجال، بل اعرف الحق تعرف أهله»
   TR (çeviri bana ait): "Bu, akılları zayıf olanların âdetidir: hakkı kişilerle tanırlar, kişileri hakla değil. Akıllı kişi ise Müminlerin Emîri Ali'nin (r.a.) sözüne uyar: 'Hakkı kişilerle tanıma; hakkı bil, ehlini tanırsın.'"
   Ayrıca aynı eserde: «فأبدا يعرفون الحق بالرجال ولا يعرفون الرجال بالحق، وهو غاية الضلال! هذه آفة الرد» ("Hep hakkı kişilerle tanırlar, kişileri hakla tanımazlar; bu sapkınlığın son noktasıdır. Bu, reddetmenin âfetidir.") ve altın/sarraf benzetmesi (sarraf sahte parayı yapana el atar...). Editör dipnotu (aynı baskı): «لم أستطع التعرف على هذا القول في كتاب نهج البلاغة وهو على أي حال من جنس كلام الإمام علي» ("Bu sözü Nehcü'l-Belâğa'da bulamadım; her hâlükârda Ali'nin sözleri cinsindendir.") İngilizce: ghazali.org/books/md/gz101.htm çevirisi (archive.org `AlGhazaliMunkidhMinAlDalalDeliveranceFromError`): "Do not seek for the truth by means of men; find first the truth and then you will recognize those who follow it."
   Taklit bağı: «وقد كان التعطش إلى درك حقائق الأمور دأبي وديدني من أول أمري وريعان عمري، غريزة وفطرة من الله وضعتا في جبلتي، لا باختياري وحيلتي؛ حتى انحلت عني رابطة التقليد، وانكسرت علي العقائد الموروثة» ("Hakikatleri kavramaya susamışlık, gençliğimin başından beri âdetim idi... bir içgüdü ve Allah'ın yaratılışıma koyduğu fıtrattı; öyle ki taklit bağı bende çözüldü, devraldığım inançlar kırıldı.") İngilizce çeviri aynı yerde: "To suppose that certitude can be only based upon formal arguments is to limit the boundless mercy of God." (kesinlik sonunda Allah'ın kalbe attığı nurla gelir.)
2. **İbn Kayyım, İ'lâmü'l-Muvakkıîn** — archive.org `20200116_20200116_1038` (Meşhûr b. Hasen Âl Selmân tahkîki, `_djvu.txt`), "[العالم صاحب الحق]" başlığı altında:
   «فلا تغتر بكثرة هذا الضرب، فإن الآلاف المؤلفة منهم لا يعدلون بشخص واحد من أهل العلم، والواحد من أهل العلم يعدل بملء الأرض منهم. واعلم أن الإجماع والحجة والسواد الأعظم هو العالم صاحب الحق، وإن كان وحده، وإن خالفه أهل الأرض.» (OCR'dan okundu; metin temiz.) TR: "Bu tür kimselerin çokluğuna aldanma; onlardan binlerce kişi, ilim ehlinden tek bir kişiye denk gelmez, ilim ehlinden bir kişi, bunlardan yeryüzünü dolduracak kadar kişiye denk gelir. Bil ki icmâ, hüccet ve en büyük kitle (es-sevâdü'l-a'zam), hak sahibi âlimdir; tek başına olsa da, yeryüzü ahalisi ona muhalefet etse de." DİKKAT: ölçüt "âlim sahibü'l-hak" (delille hak üzere olan bilgin), "az olan" değil; İbn Kayyım'ın icmâ/cemaat yorumu 04. bölümün konusu, burada yalnız ölçüt kısmı kullanıldı.
   Aynı bölümde İbn Mes'ûd rivayeti (aşağıda).
   Ayrıca İbn Kayyım "İğâsetü'l-lehfân" (Bâb 10, ar.wikisource.org/wiki/إغاثة_اللهفان/الباب_العاشر) aynı rivayeti aktarır; Ebû Şâme'nin el-Havâdis ve'l-bida' sözünü aktarır: «حيث جاء الأمر بلزوم الجماعة فالمراد به لزوم الحق واتباعه وإن كان الممتسك به قليلا والمخالف له كثيرا» ve İshak b. Râhaveyh: «فإن الحق إذا لاح وتبين لم يحتج إلى شاهد» ("Hak göründüğünde ve belirginleştiğinde şâhide muhtaç olmaz.").
3. **Şâtıbî, el-İ'tisâm** (archive.org `library4muslim_gmail_201809`, `01_djvu.txt`; editör notu eserin yarım kalan son faslını aktarıyor): «فصل: إذا ثبت أن الحق هو المعتبر دون الرجال، فالحق أيضا لا يُعرف دون وساطتهم، بل بهم يتوصل إليه، وهم الأدلة على طريقه...» TR: "Fasıl: Ölçü olan hak ise, kişiler değilse, hak yine de onların aracılığı olmadan bilinmez; ona onlarla ulaşılır, onlar yolunun delilleridir..." Editör notu: Şâtıbî "hakkın ancak kişilerle bilindiğine dair" bir fasıl başlatmış, tamamlayamamış. (Bu, "ölçü kişi değil hak; ama yol gösterenler ehl-i ilim" dengesi için kullanıldı.)
4. **Tefsir — Nahl 16:43 / Enbiyâ 21:7** (api.quran.com/api/v4/tafsirs/<id>/by_ayah/…):
   - Taberî (id 15) 16:43: ehl-i zikr = Tevrat/İncil'i okumuş olanlar (Mücâhid, A'meş, İbn Abbas'tan); Kureyş müşriklerine hitap.
   - İbn Kesîr (id 14) 16:43: aynı; "Kur'an (ez-Zikr) kastedilmedi, çünkü inkâr eden ona başvurmaz."
   - Kurtubî (id 90) 21:7, "mes'ele": «لم يختلف العلماء أن العامة عليها تقليد علمائها، وأنهم المراد بقول الله عز وجل: فاسألوا أهل الذكر إن كنتم لا تعلمون. وأجمعوا على أن الأعمى لا بد له من تقليد غيره ممن يثق بميزه بالقبلة إذا أشكلت عليه...» ("Âlimler arasında, avamın âlimlerine uyması gerektiği ve ayette kastedilenin bunlar olduğu konusunda ihtilaf yoktur; kıble karışınca görmeyenin güvendiği birine uyması gerektiği konusunda icmâ ettiler..."). Kurtubî 16:43 için: Süfyân: Ehl-i Kitap'tan iman edenler; İbn Abbas: ehl-i Kur'an; "ve'l-ma'nâ mütekâribdir".
   - Sa'dî (id 91) 21:7: «وهذه الآية وإن كان سببها خاصا... فإنها عامة في كل مسألة من مسائل الدين... إذا لم يكن عند الإنسان علم منها، أن يسأل من يعلمها» ("Sebebi özel olsa da ayet dinin bütün meselelerinde geneldir: kişide bilgi yoksa bilene sorar.")
5. **Mantık**: en.wikipedia.org/wiki/Argumentum_ad_populum: "ad populum = bir önerme, yalnızca çoğunluk veya çok kişi öyle düşündüğü için doğru/iyi sayılır" (aynı fallacy: appeal to the majority, bandwagon). en.wikipedia.org/wiki/Galileo_gambit: "reddedilmek haklı olmayı göstermez"; Carl Sagan, *Broca's Brain* (1979) s. 64: Kolomb'la da Wright kardeşlerle de alay ettiler "ama Bozo the Clown ile de alay ettiler". **"argumentum ad numerum" terimi açtığım iki Wikipedia sayfasında geçmiyor; sayfada yalnızca ad populum/bandwagon/appeal to the majority kullanıldı.**

## Zayıf / tartışmalı notlar
- **İbn Mes'ûd sözü**: «الجماعة ما وافق الحق وإن كنت وحدك» (diğer lafız: «...ما وافق طاعة الله»). Bu bir *sahâbî eseridir* (mevkûf), merfû hadis değil. Açtığım kaynak: İbn Kayyım (İ'lâm + İğâse) tam metni Amr b. Meymûn el-Evdî'den aktarır; **bağlamı**: İbn Mes'ûd önce "cemaate sarılın, Allah'ın eli cemaatle beraberdir" der; sonra "size namazı vaktinden geciktiren yöneticiler gelecek" der; Amr bunu çelişkili bulur, İbn Mes'ûd "cemaatin çoğu cemaatten ayrılanlardır; cemaat hakka uyandır, tek başına olsan da" diye açıklar. Nuaym b. Hammâd şerhi: "cemaat bozulduğunda, bozulmadan önceki cemaatin üzerinde olduğun şeye sarıl, tek başına olsan da; o zaman cemaat sensin" (Beyhakî ve başkaları zikretmiş — İbn Kayyım'ın ifadesi). **Senedinin derecesini kendim doğrulayamadım**: ikincil web özetleri Lâlekâî (Şerh usûl i'tikâd ehli's-sünne, no. 160) ve Elbânî'nin Mişkât tahşiyesinde "senedi sahih" dediğini söylüyor; İbn Asâkir'e atıf teyit edilemedi (bir özet, İbn Asâkir'in Tebyîn kizbi'l-müfterî s. 331'de Fudayl b. Iyâd yoluyla Muâz'a ulaşan başka bir eser nakli olduğunu söylüyor). Bu yüzden sayfada: "bir sahâbî sözü olarak âlimlerce yaygın aktarılır, senet derecesini bu çalışmada kendimiz doğrulayamadık; ihtiyatla" etiketi. Bağlam (gelen yöneticiler, namaz) mutlaka verilecek; "tek başına olsan da" kısmı keyfî azınlık övgüsü değil, "hakka uyma" şartlı.
- **Hz. Ali'nin sözü**: «لا تعرف الحق بالرجال، اعرف الحق تعرف أهله». Açabildiğim en eski yazılı kaynak Gazâlî (ö. 505/1111, Munkız), senedsiz "Ali'nin sözü" olarak. Modern tahkik editörü: Nehcü'l-Belâğa'da bulamadı, "Ali'nin söz cinsinden" dedi. Ikincil arama özetleri: Şii kaynaklarda (Tûsî, el-Emâlî; Hâris b. Havt el-Leysî'nin Cemel gününde Ali'ye "Talha, Zübeyr ve Âişe bâtıl üzere mi toplandı" sorusuna cevap bağlamı) benzer lafız geçtiği; bunu kendim açıp senedini doğrulayamadım. Sünni hadis tahrîcinde sahih/hasen senedi bulunamadı. **Hüküm:** "Ali'ye nispet edilen, senedi doğrulanamayan hikmetli söz"; hadis değil, delil de değil; yalnızca Kur'an'daki ilkenin (zan ve delil) özlü ifadesi olarak, nispeti belirtilerek. İslamweb 292684 / 108000 fetvaları senedsiz aktarıyor.
- Alûka makalesi (alukah.net … "لا يعرف الحق بالرجال"): sözün yanlış anlaşılmasına karşı uyarı: "ve 'hakkı bil, kişileri tanırsın' demek her zaman kişileri ölçüsüz bırakmak anlamına gelmez" (ikincil; yalnızca dengenin ruhu için, alıntılanmadı).
- Tirmizî 2630'daki "bozulanı ıslah edenler" ilavesi çok zayıf: kullanılmadı/uyarı notu.
- Hadisteki "er-raht/er-rüheyt" için sözlük anlamı (genelde 3-10 kişi) hafızadan; açık kaynak açmadım, sayfada "küçük topluluk" denip sayı verilmedi.
- "Garîb" kelimesinin gurebâ hadisindeki anlamı (yalnızlık/yadırganma): hadis sayı ölçütü değil, durum tasviri ve teselli.
- Nahl 16:43'ün Diyanet mealinde "ehl-i zikr" "kitablılar" diye geçiyor (dar bağlam); genişletme tefsirci yorumu (Kurtubî, Sa'dî).

## Canlı sahne/benzetme fikirleri
- Şeması 1: üç kapı/terazi — "Çoğunluk ✗ · Azınlık ✗ · Delil ✓".
- Şeması 2: Buhârî 5752/6541'deki peygamberlerin yanındaki sayılar (nokta sayısı = hadis metni; peygamber tasviri yok, soyut altın halka).
- Şeması 3: "Ehline sormak vs sürüye katılmak" — iki akış: (a) kim doğru? → çoğu ne yapıyorsa → gerekçe sorulmadı; (b) bilmiyorum → bilgisi sınanabilir kişiyi bul → gerekçesini/kaynağını sor → öğren.
- Örnekler: doktor-hasta (mahalle çoğunluğu "bu ilaç iyi gelir" vs uzman, ama doktora da "neden" diye sorabilirsin); kıble/pusula (Kurtubî'nin görmeyen örneği); köprü yapımı; sarraf/altın (Gazâlî).

## Açılan kaynak URL'leri
- https://archive.org/download/Heliopolis1957_gmail_2001_201805/ (Munkız ar.)
- https://archive.org/download/AlGhazaliMunkidhMinAlDalalDeliveranceFromError/ (Munkız en.)
- https://archive.org/download/20200116_20200116_1038/ (İ'lâmü'l-Muvakkıîn)
- https://archive.org/download/library4muslim_gmail_201809/01_djvu.txt (el-İ'tisâm)
- https://ar.wikisource.org/wiki/إغاثة_اللهفان/الباب_العاشر
- https://api.quran.com/api/v4/tafsirs/{14,15,90,91}/by_ayah/16:43 ve 21:7
- https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/{eng,ara}-{bukhari,muslim,tirmidhi}.json
- https://en.wikipedia.org/wiki/Argumentum_ad_populum , /Galileo_gambit , /List_of_fallacies
- Açılamayan/başarısız: sunnah.com (403), dorar.net/aqeeda/149 (403), aslein.net (403), islamway/dhofari (içerik alınamadı).
