# 05 — Bu Ayetleri Nasıl Okumamalı, Hayata Nasıl Yansır? — DENETİM RAPORU

Denetçi: bağımsız denetim ajanı · Dosya: `bolumler/05-yanlis-okuma-hayat.html` · Saat: 14:09-14:20 UTC
Yöntem: `araclar/ayet_dogrula.py` (6 blok: 0 uyarı sonda), metin içi ayet alıntıları `veri/kuran_tam.json` ile, hadisler fawazahmed0 API (ara/eng-bukhari, ara/eng-muslim, ara/eng-abudawud), tefsirler quran.com API (İbn Kesîr id 169), âlim alıntıları islamweb/hadithprophet sayfaları, bilimsel rakamlar Asch (1955) ve Vosoughi ve ark. (2018) PDF'lerinden `pdftotext` ile.

## 1. Ayet blokları (blockquote.ayet)
| Öğe | Durum | Not |
|---|---|---|
| Âl-i İmrân 3:113 | doğru | Birebir. (Denetim sırasında kendi düzenleme hatamla Arapça bozuldu; hemen `ayet.py` çıktısıyla geri yüklendi, doğrulayıcı 0 uyarı.) |
| Mâide 5:66 | doğru | Birebir. |
| Fâtır 35:32 | doğru | Birebir. |
| Hucurât 49:6 | doğru | Birebir. |
| Nahl 16:43 | düzeltildi | Blokta Arapça yalnız 43. ayetti, Diyanet Türkçesi 43-44'ü birleştirdiği için Arapça-Türkçe uyuşmuyordu. Arapça 43-44'e tamamlandı, kaynak satırı "Nahl 16:43-44" yapıldı, açıklama cümlesi güncellendi. |
| Hûd 11:112 | doğru | Birebir. |

## 2. Metin içi ayet alıntıları (`kuran_tam.json`)
| Öğe | Durum | Not |
|---|---|---|
| En'âm 6:116 (giriş) | doğru | Birebir. |
| Nisâ 4:94 (kısmi) | doğru | "Size, müslüman olduğunu bildirene… demeyin… iyice araştırıp anlayın": Diyanet'le uyuşuyor, "…" kullanılmış. |
| Mâide 5:100 | doğru | Birebir. |
| Sebe' 34:13, Sâd 38:24, Hûd 11:40 | doğru | "Kullarımdan şükredenler pek azdır", "…sayıları da ne kadar azdır!", "Pek az kimse onunla beraber inanmıştı" aynen mealde. Arapçada "i'melû âle Dâvûde şükran" ve "ve kalîlun min ibâdiye'ş-şekûr" doğrulandı. |
| A'râf 7:159 | düzeltildi | Tırnak içindeki "hakkı gösteren ve onunla hükmeden bir topluluk" Diyanet'in cümlesi değildi; "Musa'nın milletinden bir topluluk hakkı gösterirler ve onunla hükmederlerdi" yapıldı. |
| Âl-i İmrân 3:110 | doğru | "çoğu yoldan çıkmıştır", "içlerinde inananlar olmakla beraber" mealde var. |
| Hadîd 57:20 | düzeltildi | "ekicilerin" Diyanet'te var. "Aynı kök örtmek anlamını taşır, ekici tohumu örter" ifadesi tefsirden doğrulanamadı; İbn Kesîr'in "farmers admire…" cümlesi eklendi ve kök açıklamasının klasik dil bilgisi olduğu, tefsir alıntısı olmadığı açıkça yazıldı. |
| İbrâhîm 14:7, Bakara 2:152 | doğru | Birebir. |
| İsrâ 17:36 | doğru | Birebir. |
| Şûrâ 42:38 | doğru | Cümle Diyanet'in 36-38 birleşik kaydında var; metinde belirtilmiş. |
| Fussılet 41:30 (kısmi) | doğru | "Korkmayınız, üzülmeyiniz, size söz verilen cennetle sevinin…" birebir. |
| Mâide 5:48 (00'da) | doğru | Son cümle birebir. |

## 3. Hadisler
| Öğe | Durum | Not |
|---|---|---|
| Müslim 2623 (Ebû Hüreyre) | doğru | ara/eng-muslim hadithnumber 6683, arabicnumber 2623.01; Arapça birebir; Ebû İshak'ın nasb/ref' tereddüdü aynı kayıtta. Ötre/üstün okunuş ve Humeydî alıntısı Nevevî şerhinden (islamweb + hadithprophet) doğrulandı. Sahih. |
| Buhârî 6104 (İbn Ömer) ve Müslim 60 | doğru | Buhârî 6104: "أَيُّمَا رَجُلٍ قَالَ لِأَخِيهِ يَا كَافِرُ فَقَدْ بَاءَ بِهَا أَحَدُهُمَا" (İbn Ömer); 6103 ise Ebû Hüreyre rivayeti. Müslim 60 (arabicnumber 60.01/60.02): "إِنْ كَانَ كَمَا قَالَ وَإِلَّا رَجَعَتْ عَلَيْهِ" ek cümlesi 60.02'de; metindeki parantez doğru. Sahih. |
| Müslim 91 (İbn Mes'ûd) | doğru | arabicnumber 91.01: "الْكِبْرُ بَطَرُ الْحَقِّ وَغَمْطُ النَّاسِ". Sahih. |
| Müslim 96 (Usâme) | doğru | arabicnumber 96.01: "أَقَالَ لاَ إِلَهَ إِلاَّ اللَّهُ وَقَتَلْتَهُ… إِنَّمَا قَالَهَا خَوْفًا"; "kalbini yarıp baktın mı" ifadesi aynı hadisin devamında. Sahih. |
| Buhârî 2641 (Ömer) | doğru | "إِنَّ أُنَاسًا كَانُوا يُؤْخَذُونَ بِالْوَحْىِ… وَلَيْسَ إِلَيْنَا مِنْ سَرِيرَتِهِ شَىْءٌ، اللَّهُ يُحَاسِبُهُ فِي سَرِيرَتِهِ". Sahih. |
| Müslim, Mukaddime 5 / Ebû Dâvûd 4992 | düzeltildi | Ebû Dâvûd 4992: "كَفَى بِالْمَرْءِ إِثْمًا..." (Elbânî ve diğerleri sahih; Ebû Dâvûd "Hafs Ebû Hüreyre'yi anmadı; müsned yalnız Ali b. Hafs" der). Müslim mukaddime 5 numarası Dorar kaydıyla (Ebû Hüreyre, "mukaddimede zikretti") doğrulandı, İbn Hacer "hasen" demiştir (Hidâyetü'r-Ruvât). Cite satırı bu bilgilerle netleştirildi, "mürsel/merfû' ihtilafı" Ebû Dâvûd'un notuyla bağlandı. |
| Buhârî 29 (İbn Abbâs) | düzeltildi | Metindeki "Hayır;" Arapçada yok ("قِيلَ أَيَكْفُرْنَ بِاللَّهِ قَالَ يَكْفُرْنَ الْعَشِيرَ وَيَكْفُرْنَ الإِحْسَانَ"). Soru "Allah'ı mı inkâr ediyorlar?" olarak, cevap "Kocaya nankörlük eder, iyiliği inkâr ederler" olarak düzeltildi; "küfür kökü burada nankörlük" vurgusu eklendi. Sahih. |
| Buhârî 1 (Ömer) | doğru | "إِنَّمَا الْأَعْمَالُ بِالنِّيَّاتِ". Sahih. |
| Numaralandırma | doğru | Buhârî: Fethu'l-Bârî; Müslim: Abdülbâkî (fawazahmed0 arabicnumber). |
| "Ebû Hureyre" yazımı | düzeltildi | Bölüm 00 ile tutarlı olsun diye "Ebû Hüreyre" yapıldı. |

## 4. Âlim/kitap alıntıları
| Öğe | Durum | Not |
|---|---|---|
| Nevevî, Şerhu Müslim (c. 16) | doğru | islamweb.net/ar/library/content/53/7707 ve hadithprophet.com/hadith-52009 açıldı: "وَاتَّفَقَ الْعُلَمَاء عَلَى أَنَّ هَذَا الذَّمّ إِنَّمَا هُوَ فِيمَنْ قَالَهُ عَلَى سَبِيل الْإِزْرَاء... لِأَنَّهُ لَا يَعْلَم سِرّ اللَّه فِي خَلْقه. قَالُوا: فَأَمَّا مَنْ قَالَ ذَلِكَ تَحَزُّنًا..." birebir. Humeydî: "الرَّفْع أَشْهَر ... أَشَدّهمْ هَلَاكًا" doğru. |
| İbn Abdilberr, et-Temhîd c. 21 s. 242 | doğru | islamweb.net/ar/library/content/78/1393: "أن يقولها الرجل احتقارا للناس وإزراء عليهم وإعجابا بنفسه... تأسفا وتحزنا وخوفا عليهم... ماقتا لنفسه موبخا لها" sayfa 242'de (243 işareti sonrasında). Türkçe aktarım uyuyor. |
| İbn Kesîr 3:113 | doğru | "believers and also criminals among the People of the Book" |
| İbn Kesîr 35:32 | doğru | "The correct view is that they are also part of this Ummah." |
| İbn Kesîr 49:6 | doğru | "taking the lead of the sinners… groups of the scholars of Hadith refuse to accept narrations from narrators whose reliability is unknown". |
| İbn Kesîr 16:43 | doğru | İbn Abbâs/Dahhâk: peygamberlerin insan olup olmadığı bağlamı, "ehl-i zikr = önceki kitap ehli" (Mücâhid). |
| Hucurât 49:6 iniş sebebi | doğru (anlatılmamış) | Metin zaten anlatmıyor; iyi. |

## 5. Bilimsel iddialar (orijinal PDF'ler)
| Öğe | Durum | Not |
|---|---|---|
| Asch 1955, Scientific American 193(5), 31-35 | doğru | Offprint PDF açıldı. "A total of 123 subjects from three institutions of higher learning (not including Swarthmore College)", yedi-dokuz genç erkek öğrenci grubu. |
| Hata %1'in altında | doğru | "individuals matching the lines will make mistakes less than 1 per cent of the time". |
| %36,8 | doğru | "swung to acceptance of the misleading majority's wrong judgments in 36.8 per cent of the selections". |
| ~%75 | düzeltildi (ifade) | PDF'de yalnızca şekil yazısı: "Seventy-five per cent of experimental subjects agree with the majority in varying degrees." Metne bu ifade ("değişen ölçülerde, yani en az bir denemede") eklendi; abartı yok. |
| ~dörtte biri hiç uymadı | doğru | "about one quarter of the subjects were completely independent and never agreed". |
| %63,2 bağımsız | doğru | 100 − 36,8; hesap. |
| Destekçi etkisi | doğru | "subjects answered incorrectly only one fourth as often as under the pressure of a unanimous majority"; ortak sonradan çoğunluğa geçince hatalar hemen arttı. |
| Grup büyüklüğü grafiği | doğru | 1: 3,6; 2: 13,6; 3: 31,8; 4: 35,1; 6: 35,2; 7: 37,1; 9: 35,1; 15: 31,2 — şekil yazısıyla birebir ("seven, 37.1; nine, 35.1; 15, 31.2"). Çubuk uzunlukları 8 birim/puan ile yeniden hesaplandı, uyuşuyor. "Üçten sonra artış duruyor" Asch'ın "group size of three, but further increases added little" ifadesiyle uyumlu. |
| Görüşme bulguları | doğru | "I am wrong, they are right", "not to spoil your results", "sheep" şüphesi, farklılığı eksiklik sayma, "All the yielding subjects underestimated the frequency" PDF'te var. |
| Vosoughi, Roy, Aral 2018, Science 359:1146 | doğru | PDF açıldı: ~126.000 hikâye/söylenti zinciri, ~3 milyon kişi, 4,5 milyondan fazla paylaşım, 2006-2017, altı doğrulama kuruluşu (%95-98 uyum). |
| %70 daha olası retweet | doğru | "falsehoods were 70% more likely to be retweeted than the truth… controlling for account age, activity level, number of followers and followees…verified". |
| Altı kat süre (1500 kişi) | doğru | "It took the truth about six times as long as falsehood to reach 1500 people". |
| Botlar | doğru | "accelerated the spread of true and false news at the same rate". |
| Duygular | doğru | Özette "fear, disgust, and surprise"; ayrıntılı sonuçlarda surprise ve disgust istatistiksel olarak öne çıkıyor. Metindeki "korku, şaşkınlık ve tiksinme" özetle uyumlu. |
| "Yenilik sebep demezler" | doğru | "we cannot claim that novelty causes retweets or that novelty is the only reason". |
| Lorenz ve ark. 2011 (PNAS) | düzeltildi (ifade) | 144 ETH öğrencisi (12 oturum × 12) doğru. "Grup eskisi kadar isabetli değil" abartıydı; makale "çeşitlilik daraldı, doğruluk artmadı, bilgelik etkisi zayıfladı, güven arttı" der. Cümle buna göre yeniden yazıldı. |
| Salganik, Dodds, Watts 2006 | doğru (ikincil) | 14.341 katılımcı ve "başkalarının indirmelerini görenlerde eşitsizlik ve öngörülemezlik" bilinen bulgudur; bölüm zaten "ikincil özetlerden okuduk" diyor, birincil PDF açılmadı. |
| Surowiecki / Galton | doğru (ikincil) | Koşullar (çeşitlilik, bağımsızlık, yerel bilgi, toplulaştırma) standart özet; Galton için rakam verilmemiş. Kitabın kendisi açılmadı. |

## 6. Şemalar ve sözleşme
| Öğe | Durum | Not |
|---|---|---|
| 4 şema, 390 px açık ve koyu tema (`shots/c-05`) | doğru | `over` ve `svgTextTasma` boş; metin taşması, çakışma yok; koyu temada tüm metinler okunur (`var(--ink)`/`--bg` kullanımı yerinde). Grafik çubukları ölçekli. |
| HTML sözleşmesi | doğru | `<script>` yok, dış URL yok, sabit hex renk yok, izinsiz sınıf yok, etiketler kapalı. 2712 kelime (3000 sınırı altında), 4 şema, "Aklında kalsın" 5 madde. |
| Duruş | doğru | Tekfir, siyaset, fetva yok; "kibir/damga" uyarıları sünnî ana çizgide. |

## Kalan riskler
- Salganik ve Surowiecki/Galton birincil kaynaktan açılmadı (bölüm bunu belirtiyor).
- Hadîd 57:20'deki kök açıklaması dil bilgisi düzeyinde; metinde "tefsir alıntısı değil" etiketiyle duruyor.
- Müslim mukaddime 5 için Dorar özeti kullanıldı (fawazahmed0'da mukaddime kayıtları boş); numara 5 tutarlı.
- İbn Kesîr kısaltılmış İngilizce neşirden aktarılıyor.
