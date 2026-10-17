# 00 — Paylaşılan Liste: Doğru mu? (araştırma notları)

## Özet
- Görseldeki 14 maddenin 13'ünde sure numarası doğru; **tek atıf hatası: "Mâide 48" -> doğrusu Mâide 5:49** ("İnsanların çoğu gerçekten fasıktırlar"). 5:48'de "çoğu" ifadesi yok (yerel veri, kuran_tam.json).
- Kesik numaralar yerel veriyle (ekser_ayetleri.json, 81 isabet) çözüldü: "Bakara 24?" -> **2:243** (Bakara'da 24x aralığında "ekser" geçen tek ayet; 2:100 de 2:243 de başka); "Yûnus 3?" -> **10:36** (Yûnus'ta 3x aralığında tek isabet); "Mâide 10?" -> **5:103** (Mâide'de 5:59 ve 5:103 var; "akletmez" ifadesi 5:103'te).
- 14 maddenin 12'sinde Arapçada **ekser** (çoğu/çoğunluk) kökü geçer; 2'sinde (5:49 ve 10:92) **kesîr** ("birçoğu/çok sayıda") geçer: "ve inne kesîran mine'n-nâs". Diyanet ikisini de "çoğu" diye çevirmiş. (İbn Kesîr 5:49'u "insanların çoğunluğu" diye okur; bu dil notu onun okumasını reddetmez, sadece kelime farkını gösterir.)
- Üç sorun: (1) atıf hatası (5:49) + kesik numaralar + "inkârcı" gibi gevşek özetler (17:89 ve 25:50'de Diyanet "nankör"); (2) bağlam kaybı: "çoğu"nun döndüğü topluluk düşürülmüş; (3) sonuç mantığı: "çoğunluk batıl => bizim yanımız hak" ayetlerde yok; Müslim 2623 bu tuzağa karşı uyarır.
- Listenin haklı yanı: 6:116 ("Yeryüzündekilerin çoğunluğuna itaat edersen seni Allah yolundan saptırırlar. Onlar ancak zanna uyarlar...") ve 10:36 ilkesi: delilsiz çoğunluğa uymak yerilir. Not: listedeki 14 madde yerine asıl genel ifade 6:116, 11:17, 12:103 gibi ayetlerdedir (listeye girmemişler).

## Doğrulanmış ayetler (Diyanet meali, kuran_tam.json; ayet.py)
| Liste | Gerçek | Arapça anahtar | Diyanet |
|---|---|---|---|
| Nahl 83 çoğu kâfirdir | 16:83 | وَأَكْثَرُهُمُ ٱلْكَـٰفِرُونَ | "Allah'ın nimetini hem bilirler hem de inkar ederler. Zaten çoğu kafir kimselerdir." |
| Mâide 48 çoğu fasıktır | **5:49** (5:48'de yok) | وَإِنَّ كَثِيرًا مِّنَ ٱلنَّاسِ لَفَـٰسِقُونَ | "...İnsanların çoğu gerçekten fasıktırlar." |
| Rûm 42 çoğu müşriktir | 30:42 | كَانَ أَكْثَرُهُم مُّشْرِكِينَ | "De ki: 'Yeryüzünde dolaşın da daha öncekilerden çoğu ortak koşan (müşrik) olanların sonunun nasıl olduğuna bir bakın.'" |
| İsrâ 89 çoğu inkârcıdır | 17:89 | فَأَبَىٰٓ أَكْثَرُ ٱلنَّاسِ إِلَّا كُفُورًا | "...insanların çoğu nankör olmakta direndiler." (Diyanet: nankör) |
| Yûnus 92 çoğu gafildir | 10:92 | وَإِنَّ كَثِيرًا مِّنَ ٱلنَّاسِ عَنْ ءَايَـٰتِنَا لَغَـٰفِلُونَ | "Doğrusu insanların çoğu ayetlerimizden habersizdir." (Firavun'un boğulması sonrası) |
| Şuarâ 223 çoğu yalancıdır | 26:223 (bağlam 26:221-226) | وَأَكْثَرُهُمْ كَـٰذِبُونَ | "Bunlar şeytanlara kulak verirler, çoğu yalancıdırlar." (26:222: günahkâr iftiracıların hepsine iner) |
| Yûnus 36 çoğu zanna uyar | 10:36 | وَمَا يَتَّبِعُ أَكْثَرُهُمْ إِلَّا ظَنًّا | "Onların çoğu zanna uyarlar; gerçekte ise zan, hakikat karşısında bir şey ifade etmez..." (10:31-35: ortak koştukları şeyler hakkında) |
| Furkân 50 çoğu nankördür | 25:50 | فَأَبَىٰٓ أَكْثَرُ ٱلنَّاسِ إِلَّا كُفُورًا | "...Buna rağmen insanların çoğu nankörlükte direnmiştir." (yağmurun dağıtılması) |
| Bakara 243 çoğu şükretmez | 2:243 | وَلَـٰكِنَّ أَكْثَرَ ٱلنَّاسِ لَا يَشْكُرُونَ | "Allah insanlara bol nimet verir, fakat insanların çoğu şükretmezler." |
| Bakara 100 çoğu iman etmez | 2:100 | بَلْ أَكْثَرُهُمْ لَا يُؤْمِنُونَ | "Onlar, her ne zaman bir ahidde bulunmuşlarsa içlerinden bir takımı onu bozmamış mıdır? Zaten onların çoğu inanmazlar." |
| Zuhruf 78 çoğu haktan hoşlanmaz | 43:78 | وَلَـٰكِنَّ أَكْثَرَكُمْ لِلْحَقِّ كَـٰرِهُونَ | "...size gerçeği getirdik; fakat çoğunuz gerçeği sevmiyorsunuz." (2. çoğul şahıs; 43:74-77 cehennemdeki suçlular) |
| Yûsuf 106 çoğu Allah'a ortak koşar | 12:106 | وَمَا يُؤْمِنُ أَكْثَرُهُم بِٱللَّهِ إِلَّا وَهُم مُّشْرِكُونَ | "Onların çoğu, ortak koşmadan Allah'a inanmazlar." |
| Fussılet 4 çoğu Kur'an'dan yüz çevirir | 41:4 | فَأَعْرَضَ أَكْثَرُهُمْ فَهُمْ لَا يَسْمَعُونَ | Diyanet kaydı 41:2-5'i tek metinde birleştirir; "...Ama insanların çoğu yüz çevirmiştir, onlar işitmezler de..." |
| Mâide 103 çoğu akletmez | 5:103 | وَأَكْثَرُهُمْ لَا يَعْقِلُونَ | "...fakat inkar edenler Allah'a karşı yalan uydururlar ve çoğu da akletmezler." (bahîre, sâibe, vasîle, hâm) |

Ek (listede yok, genel ifade): 6:116 "Yeryüzündekilerin çoğunluğuna itaat edersen seni Allah yolundan saptırırlar. Onlar ancak zanna uyarlar, sadece tahminde bulunurlar." 11:17 ve 12:103: "insanların çoğu inanmazlar". Karşı örnekler: 5:59 ("çoğunuzun fasık olması" — Kitap ehline hitap), 3:110 (Kitap ehli için "çoğu yoldan çıkmıştır"), 34:13 ("Kullarımdan şükredenler pek azdır").

## Tefsir kaynakları (gerçekten açılan, quran.com API tefsir uç noktası)
- İbn Kesîr (kısaltılmış, İngilizce, id 169): https://api.quran.com/api/v4/tafsirs/169/by_ayah/<sure:ayet>
  - 12:106: "Ibn `Abbas commented, 'They have a part of faith, for when they are asked, "Who created the heavens? Who created the earth? Who created the mountains?" They say, "Allah did." Yet, they associate others with Him in worship.'" (Mücâhid, Atâ, İkrime, Şa'bî, Katâde, Dahhâk, İbn Zeyd de benzerini söyler.) Ayrıca Hasan-ı Basrî: bu münafıktır (gösteriş); Huzeyfe'nin hasta adamın koluna bağlı ipi koparıp bu ayeti okuması (gizli şirk uyarısı). -> ayet okuyucuya da ayna tutar.
  - 5:49: "Therefore, the majority of humans are disobedient to their Lord, defiant of the truth and deviate away from it." (12:103 ve 6:116'ya atıf). Bağlam: Allah'ın indirdiğiyle hükmetme, heveslerine uymama (5:48-50).
  - 2:100: Hasan-ı Basrî: "Verdikleri her sözü bozarlar..."; Yahudiler ve ahit (Mâlik b. es-Sayf olayı).
  - 26:223: "Shall I inform you upon whom the Shayatin descend? They descend on every lying, sinful person" — kâhinler ve günahkâr yalancılar; Buhârî'de Âişe rivayeti (kâhinler: bir doğru söze yüzden fazla yalan karıştırırlar).
  - 30:42 (30:41-42 bir arada): "Most of them were idolators. means, so see what happened to them when they rejected the Messengers and were ungrateful for the blessings." ("those who came before you")
  - 43:78: "Indeed We have brought the truth to you... but most of you have a hatred for the truth." Başlık: "The Punishment of the Doomed".
  - 41:4: "but most of them turn away, so they hear not. means, most of the Quraysh did not understand anything of it at all..."
  - 16:83: "They recognize the grace of Allah, yet they deny it... by worshipping others besides Him"
  - 5:103: "But those who disbelieve invent lies against Allah, and most of them have no understanding. means, Allah did not legislate these invented rituals... it is the idolators who made them" ; Buhârî: Saîd b. el-Müseyyeb bahîre/sâibe tarifi.
  - 6:116: "Allah states that most of the people of the earth, are misguided."
  - 2:243: İbn Abbâs: Dâverdân köyü, veba, ölüm korkusuyla çıkanlar.
- Taberî (Arapça, id 15): https://api.quran.com/api/v4/tafsirs/15/by_ayah/<sure:ayet>
  - 12:106: İbn Abbâs (Saîd b. Cübeyr yoluyla): "min îmânihim izâ kîle lehum men halaka's-semâ... kâlû: Allah. ve hum müşrikûn." (rivayet no. 19954)
  - 26:223: "ve ekseru men tenezzelu aleyhi'ş-şeyâtîn kâzibûn fî mâ yekûlûne ve yuhbirûn" (Taberî: şeytanların indiği kimselerin çoğu söylediklerinde yalancıdır); Zührî-Urve-Âişe rivayeti.
  - 30:42: "kul yâ Muhammed li-hâulâi'l-müşrikîn min kavmik: sîrû fi'l-bilâd... mesâkine'llezîne keferû min kablikum" (geçmiş toplumların yurtları).
  - 43:78: "lekad erselnâ ileykum yâ ma'şera Kureyş resûlenâ Muhammeden bi'l-hakk" (Süddî) — muhatap Kureyş topluluğu.
  - 10:36: "ve mâ yettebiu ekseru hâulâi'l-müşrikîne illâ zannâ" — çoğu "bu müşrikler".
  - 41:4: Taberî Arapça bu kayıtta sadece i'râb tartışması; İbn Kesîr alıntısı kullanıldı.

## Hadis
- **Müslim 2623** (Ebû Hureyre): "إِذَا قَالَ الرَّجُلُ هَلَكَ النَّاسُ فَهُوَ أَهْلَكُهُمْ" — "Bir adam 'insanlar helak oldu' derse, o onların en çok helak olanıdır" (أَهْلَكُهُمْ merfû okunuşu; nasb okunuşu: onları helak eden — Ebû İshak tereddüdünü belirtir). Derece: Sahih (Sahîh-i Müslim'de). Doğrulama: fawazahmed0 hadith-api ara-muslim hadithnumber 6683, arabicnumber 2623.01 (reference book 45 / hadith 180) ve eng-muslim aynı kayıt: "When a person says that people are ruined he is himself ruined." Ayrıca Mâlik, Muvatta (Kelâm 2) ve Ebû Dâvûd 4983'te (TDV kaynakçası verdiği şekliyle; Ebû Dâvûd'u ben açmadım).
- Şerh: Nevevî, Şerhu Müslim: "وَاتَّفَقَ الْعُلَمَاء عَلَى أَنَّ هَذَا الذَّمّ إِنَّمَا هُوَ فِيمَنْ قَالَهُ عَلَى سَبِيل الْإِزْرَاء ..." — "Âlimler bu yermenin ancak bunu insanları hor görme (izrâ) yoluyla söyleyen hakkında olduğunda birleştiler"; devamı (özet, sayfadan): insanları küçümseyip kendini onlardan üstün görerek söyleyen kınanır; dinin hâlinden üzülerek söyleyen kınanmaz. Kaynak sayfa: https://hadithprophet.com/hadith-52009.html (etiket: شرح النووي على مسلم). Hattâbî'nin açıklaması da aynı sayfada özetlendi (insanların ayıbını sayıp durmak kendini beğenmeye götürür) — doğrudan alıntı yapılmadı.

## Üç soru yöntemi için zemin
1. Ayet doğru mu? (sure:ayet; kelime kökü ekser mi kesîr mi; tam cümle)
2. Kime konuşuyor? ("çoğu"nun zamiri kime döner; önceki ayetler; tefsir)
3. Ne sonuç çıkarıyorum? (ayet "ben/biz haklıyız" demiyor; delil ölçüsü; Müslim 2623; 5:48 "iyiliklere koşuşun")

Köprüler: 01 veri haritası (81 ayet, kök dağılımı), 02 her ayet kime konuşuyor (zamir/muhatap), 03 ölçü sayı mı delil mi (6:116, 10:36...), 04 cemaat/icmâ/şûrâ, 05 nasıl okumamalı/gündelik hayat, 06 sorular.

## İkna psikolojisi (genel gözlem; kaynak: kendi çıkarım, otorite iddiası yok)
Onaylama eğilimi, aidiyet ("biz doğru yoldayız"), tek cümlelik kesinlik, rakam ve sure numarası görünümü (kontrol edilmediği sürece güven verir), kıyas ("çoğunluğa uyma" uyarısı gerçek olduğu için liste de gerçek sanılır).

## Sınıflama (SVG a) — kaynaklara göre
- Belirli topluluk (zamirle): 2:100 (Yahudiler/ahit; İbn Kesîr), 5:103 (inkâr edip yalan uyduranlar), 10:36 (müşrikler; Taberî), 12:106 (müşrikler; İbn Abbâs), 16:83 (nimeti bilip inkâr edenler), 26:223 (kâhin/iftiracılar; Taberî), 41:4 (Kureyş; İbn Kesîr), 43:78 (Kureyş / cehennemdekiler; Taberî) = 8
- Geçmiş toplum: 30:42 = 1
- "İnsanların çoğu" (genel ifade): 5:49, 17:89, 25:50, 2:243, 10:92 = 5. Not: 2:243 ve 10:92 bir olayın (veba kaçkınları; Firavun) ardından; 5:49 ve 10:92 Arapçada kesîr.
(Ayrılan yorumlar olabilir; sınıflama bu bölümün okumasıdır.)

## Zayıf/tartışmalı notlar
- Diyanet kaydında bazı ayetler birleşik (41:2-5 aynı metin; 26:225-226 aynı metin): kısmi alıntı yapıldı.
- 17:89 Diyanet metninde "And olsun ki" (yazım) — ayet.py çıktısı olduğu gibi kullanılır.
- Ebû Dâvûd 4983 numarası ikincil kaynaktan (sorularlaislamiyet arama özeti); doğrudan açılmadı, sayfada kullanılmayacak.
- Nevevî'nin şerhinin yalnızca ilk cümlesi birebir alındı; devamı özet.

## Açılan kaynak URL'leri
- /home/user/Jn/cogunluk/veri/kuran_tam.json, ekser_ayetleri.json
- https://api.quran.com/api/v4/tafsirs/169/by_ayah/{12:106, 26:223, 30:42, 5:49, 2:100, 5:103, 43:78, 16:83, 10:92, 6:116, 41:4, 10:36, 17:89, 25:50, 2:243}
- https://api.quran.com/api/v4/tafsirs/15/by_ayah/{12:106, 26:223, 30:42, 43:78, 10:36, 41:4}
- https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/ara-muslim.json ve eng-muslim.json
- https://hadithprophet.com/hadith-52009.html (Nevevî şerhi)
