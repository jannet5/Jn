# 04 — Cemaat, İcmâ ve Şûrâ: Çelişki Var mı? (araştırma notları)

Hazırlayan: bölüm 04 yazarı. Tarih: 2026-10-17. Doğrulama yöntemi her kalemde belirtildi. "(teyit edilemedi)" = kaynak sayfası açılamadı, bilgi ikincil özetten geliyor, sayfada KULLANILMAYACAK ya da açıkça etiketlenecek.

## Özet (tez)
1. Kur'an'ın "çoğunluğa uyma" uyarısı (En'âm 6:116, Yûnus 10:36) **sayıyı değil, delilsiz zanna/hevâya/ata mirasına dayanmayı** yerer. Ölçüt: ilim, vahiy, hak.
2. Cemaat/sevâd-ı a'zam/icmâ hadisleri ve usûl, **sayıyı değil, nassa ve ehl-i ilme dayanan birliği** konu alır. Usûl-i fıkıhta icmâ = müctehidlerin ittifakıdır; avamın veya halkın çoğunluğu değildir (TDV İcmâ; Şâfiî el-Üm VII, 256 atfıyla).
3. "Cemaat" hadislerinin bir kısmı **siyasî fitne/isyan/kan dökme bağlamındadır** (Buhârî 7084, Müslim 1847-1850): bunlar "inanç meselesinde çoğunluğa uy" demez.
4. Şûrâ **hakikati oylamaz**; nassla bildirilen konular şûraya konu olmaz (TDV Şûra, "ittifakla kabul edilmiştir"); amaç isabetli karar için görüşleri açığa çıkarmaktır (kök anlam: balı kovandan çıkarmak; Râgıb).
5. Dürüst not: "ümmetim dalâlet üzere birleşmez" lafzının hadisleri **tek tek zayıf/tartışmalı**; mana ise çok tarikten geldiği için usûlcüler "mütevâtir sünnet gücünde" (TDV) sayar. Gazâlî ise bu yolun kendisini "bizce zayıf" bulur ve ayetlerin hiçbirinin icmâyı açıkça ifade etmediğini söyler. Elbânî de bu hadisin icmâyı ispatlamadığını, yalnızca "ümmetin tamamının dalâlette birleşmeyeceğini" söylediğini belirtir. Yani âlimler arasında gerçek bir tartışma var; sayfa bunu saklamayacak.

## Doğrulanmış ayetler (tamamı `araclar/ayet.py` ile, kuran_tam.json — Diyanet İşleri meali)
| ref | not |
|---|---|
| En'âm 6:116 | "Yeryüzündekilerin çoğunluğuna itaat edersen seni Allah yolundan saptırırlar. Onlar ancak zanna uyarlar, sadece tahminde bulunurlar." |
| Yûnus 10:36 | "Onların çoğu zanna uyarlar; gerçekte ise zan, hakikat karşısında bir şey ifade etmez…" |
| Nisâ 4:115 | "…Peygamberden ayrılıp, inananların yolundan başkasına uyan kimseyi…" (icmâ delili olarak ileri sürülür; Gazâlî "kısmen kuvvetli ama icmâ için sevk edilmemiş" der — TDV İcmâ) |
| Âl-i İmrân 3:103 | "Toptan Allah'ın ipine sarılın, ayrılmayın…" (bağ: **Allah'ın ipi**, "çoğunluk" değil) |
| Âl-i İmrân 3:110 | "…hayırlı bir ümmetsiniz [niteliği: doğruluğu emreden, fenalıktan alıkoyan, Allah'a inanan]… içlerinde inananlar olmakla beraber, çoğu yoldan çıkmıştır." (aynı ayette hem "hayırlı ümmet" hem "çoğu fasık" — sayının değil niteliğin ölçü olduğunu gösteriyor) |
| Bakara 2:143 | "…tam ortada bulunan bir ümmet kıldık… şahid ve örnek olmanız için" |
| Şûrâ 42:38 | "Onların işleri aralarında danışma iledir." (Diyanet kaydı 36-38'i birleşik verir; Arapça 38'dir) |
| Âl-i İmrân 3:159 | "…iş hakkında onlara danış, fakat karar verdin mi Allah'a güven…" |
| Nisâ 4:59 | "…bir şeyde çekişirseniz… onun halini Allah'a ve Peygambere bırakın." |
| Nisâ 4:83 | "…sonuç çıkarmaya kadir olanlar onu bilirdi" (istinbât ehli → ehl-i ilim) |
| Nahl 16:43 | "Bilmiyorsanız … sorun" (Diyanet kaydı 43-44 birleşik; "ehl-i zikre sorun") |
| Bakara 2:170, Mâide 5:104 | "atalarımızı yaparken bulduğumuz şeye uyarız" (çoğunluk/gelenek taklidi) |
| İsrâ 17:36 | "Bilmediğin şeyin ardına düşme" |

## Doğrulanmış hadisler
Yöntem: fawazahmed0/hadith-api (cdn.jsdelivr.net, `eng-*` ve `ara-*` baskıları) açıldı, metin+numara okundu; `grades` alanı (Elbânî, Arnaut, Ahmed Şâkir, Zübeyr Ali Zai vb.) oradan alındı. **Müslim numaraları Abdülbâkî numaralamasıdır** (API'de `arabicnumber`; API'nin `hadithnumber` alanı farklıdır, onu KULLANMA).

| TR anlam | Kaynak+no | Derece | Not |
|---|---|---|---|
| "Ümmetim dalâlet üzere birleşmez; ihtilaf görürseniz sevâd-ı a'zama uyun." (Enes) | İbn Mâce 3950 (Fiten, bâb es-sevâdü'l-a'zam) | **Elbânî: çok zayıf (da'îf cidden); Arnaut: çok zayıf; Abdülbâkî: çok zayıf (API) ; Zübeyr Ali Zai: zayıf** | İsnadda Ebû Halef el-A'mâ (Hâzim b. Atâ), zayıf; Mu'ân b. Rifâ'a. Ayrıca bkz. aşağıda Busayrî/Abdülbâkî. |
| "Allah'ın eli cemaatle beraberdir." (İbn Abbas) | Tirmizî 2166 | Tirmizî: "hasen garîb"; **Elbânî: sahih; Ahmed Şâkir: sahih; Zübeyr: isnadı sahih** (API) | |
| Hz. Ömer'in Câbiye hutbesi: "Cemaate sarılın, ayrılıktan sakının; şeytan teke yanındadır, iki kişiden uzaktır; cennetin ortasını isteyen cemaate sarılsın…" | Tirmizî 2165 | Tirmizî: "hasen sahîh garîb"; **Elbânî sahih; Ahmed Şâkir sahih; Beşşâr Avvâd: hasen sahih** | Şâfiî er-Risâle'de (Şâkir nşr. no 1315, s. 474) aynı hutbeyi (mürsel isnadla) delil getirir; Şâkir dipnotu: "Ahmed, Tayâlisî, İbn Mâce (bir parça), Tirmizî, Hâkim rivayet etti". |
| "Allah ümmetimi dalâlet üzere toplamaz; Allah'ın eli cemaat üzerindedir; kim ayrılırsa ateşe ayrılır." (İbn Ömer) | Tirmizî 2167 | Tirmizî: "garîb"; **Elbânî: zayıf; Zübeyr: zayıf; Ahmed Şâkir: sahih** (API) | Tirmizî'nin kendi notu: "Cemaatin tefsiri ehl-i ilim nezdinde fıkıh, ilim ve hadis ehlidir" + İbnü'l-Mübârek: "Cemaat kimdir?" → "Ebû Bekir ve Ömer"; "öldüler"; "falan-filan"; "öldüler" → "Ebû Hamza es-Sükkerî bir cemaattir." (metin ara-tirmidhi 2167'den okundu) |
| "Allah sizi üç şeyden korudu: …hak ehline batıl ehlinin galip gelmemesi, **ve dalâlet üzere birleşmemeniz**." (Ebû Mâlik el-Eş'arî) | Ebû Dâvûd 4253 | **Elbânî: zayıf; Abdülbâkî(Muhyiddin): zayıf; Zübeyr: zayıf** (API). İkincil kaynak (alukah/dorar özeti): Elbânî "üçüncü cümlenin sahih olduğunu" belirtip Sahîha'ya atıf yapmış — (teyit edilemedi, dorar Cloudflare engeli) | |
| Huzeyfe: "Müslümanların cemaatine ve imamına sarıl." "Cemaat de imam da yoksa?" "O fırkaların hepsinden ayrıl, ölüm seni o hâlde yakalayana kadar bir ağacın kökünü ısırmak zorunda kalsan bile." | Buhârî 7084 (Fiten; = bâb "kayfe'l-emr izâ lem tekün cemâa") ; Müslim 1847 (İmâre) | Sahîh (Buhârî/Müslim) | Bağlam: fitne dönemi, "cehennem kapılarında çağıranlar" (bizim dilimizi konuşan topluluklar). Hadis çoğunluk ölçmez; fitnede saf tutma, kan dökmeme, dağılmama konusudur. |
| "İtaatten çıkıp cemaatten ayrılan ve bu hâlde ölen cahiliye ölümüyle ölmüştür; kör bir bayrak altında asabiyet için savaşan… cahiliye ölümü; ümmetime kılıç çekip iyisini kötüsünü vuran, ahdi olana vefa göstermeyen benden değildir." | Müslim 1848 (Ebû Hüreyre); Müslim 1849 (İbn Abbas: "emîrinden hoşlanmadığı bir şey görürse sabretsin; cemaatten bir karış ayrılıp ölen cahiliye ölümüyle ölür"); Müslim 1850 (Cündeb b. Abdillah el-Becelî: kör bayrak/asabiyet) | Sahîh (Müslim) | Bağlam: siyasî otoriteden ayrılma, silahlı fitne, kabile taassubu. |
| "İsrailoğulları 72 millete ayrıldı; ümmetim 73'e ayrılacak; biri hariç hepsi ateştedir. — Hangisi? — **Benim ve ashabımın üzerinde olduğum (yol)**." | Tirmizî 2641 (Abdullah b. Amr) | Tirmizî: "hasen garîb (müfesser)"; **Elbânî: hasen; Ahmed Şâkir: hasen; Zübeyr Ali Zai: zayıf** (API). Zayıf halka tartışması: isnadda Abdurrahman b. Ziyâd el-İfrîkî (ikincil aktarım: ibnamin.com, ibn-jebreen; kitaptan teyit edilemedi); İbnü'l-Arabî ve Irâkî'nin hasen dediği ikincil kaynaktan (islamqa özeti) — teyit edilemedi | "Mâ ene aleyhi ve eshâbî" kaydı **yalnız bu rivayettedir**; Ebû Dâvûd/İbn Mâce rivayetlerinde "el-cemâa" geçer. |
| "Yahudiler 71 veya 72, Hıristiyanlar 71 veya 72 fırkaya ayrıldı; ümmetim 73 fırkaya ayrılacak." (kalan kısım Ebû Dâvûd'da cümle olarak yok; "ateşte" kaydı yok) | Ebû Dâvûd 4596 (Ebû Hüreyre) | **Elbânî: hasen sahih; Arnaut: hasen sahih; Zübeyr: isnadı hasen** | |
| Muâviye: "72'si ateşte, biri cennette; o da **el-cemâa**'dır." + "ümmetimden hevâlar (kuduz gibi) işleyecek kavimler çıkacak" | Ebû Dâvûd 4597 | Elbânî: hasen; Muhyiddin: hasen; Zübeyr: isnadı hasen | |
| Avf b. Mâlik: "73 fırka… biri cennette. — Kim onlar? — **el-Cemâa**." | İbn Mâce 3992 | Elbânî sahih; Arnaut sahih li-gayrihî; Zübeyr isnadı hasen | |
| Enes: "…hepsi ateşte, biri hariç; o da el-cemâa." | İbn Mâce 3993 | Elbânî sahih; Zübeyr sahih | (API'nin İngilizce çevirisi "main body" yazar; Arapçası "el-cemâa") |
| "Ümmetimden bir tâife hak üzere zâhir olmaya devam eder; onları yalnız bırakanlar zarar veremez." | Müslim 1920 (Sevbân), 1923 (Câbir); Buhârî 71, 3640, 7311 | Sahîh | Dengeleyici: hak taifesinin sayısı belirtilmez; "çoğunluk haktır" kuralı çıkmaz. |
| "İslâm garip başladı, tekrar garip dönecek; ne mutlu garipler (gurebâ) için." | Müslim 145 (Ebû Hüreyre) | Sahîh | Dengeleyici; "azınlık haktır" kuralı da çıkmaz. Kullanılırsa dikkatle. |
| "İnsanlar helâk oldu diyen kişi kendisi helâk olmuştur." | Müslim 2623 | Sahîh | (b-05'e atıf) |
| Hudeybiye'de Hz. Peygamber'in ashabı kurban kesmeye kalkmayınca Ümmü Seleme'nin tavsiyesine uyması | Buhârî 2731-2732 | Sahîh | Şûra/istişarenin "oylama" olmadığını gösteren örnek (TDV Şûra da aynı örneği verir: Buhârî, Şürût 15). |

### İbn Mâce 3950'nin derecesi: kesin cümle
- Metin (Arapça, ara-ibnmajah): "إِنَّ أُمَّتِي لَنْ تَجْتَمِعَ عَلَى ضَلاَلَةٍ فَإِذَا رَأَيْتُمْ اخْتِلاَفًا فَعَلَيْكُمْ بِالسَّوَادِ الأَعْظَمِ" (Enes b. Mâlik).
- API'ye göre: Elbânî "çok zayıf", Arnaut "çok zayıf", Abdülbâkî "çok zayıf", Zübeyr "zayıf". **Hasen li-ğayrihî diyen bir muhaddis bulunamadı/teyit edilemedi.** İnternette dolaşan "şevâhidle hasen" iddiası İbn Hacer ve Sehâvî'ye atfedilerek ikincil sitelerde geçiyor (teyit edilemedi; sayfada KULLANMA).
- Abdülbâkî tâlîki (mshmsdin.com sayfasından aktarma, teyit edilen ifade): "في إسناده أبو خلف الأعمى واسمه حازم بن عطاء وهو ضعيف". Es-Sehsevânî (Sıyânetü'l-İnsân): "هذا الحديث ضعيف جداً…" (aynı site; birincil kitaptan teyit edilemedi).
- Busayrî (Misbâhu'z-Zücâce): arama motoru özeti "isnadı zayıf, Ebû Halef el-A'mâ zayıf" diyor (kitap sayfası açılamadı → "ikincil aktarım" olarak etiketle ya da yazma).
- Çözüm tezine etkisi: Bu lafzın **tek başına** delil olarak kullanılamayacağı açık. Ama "ümmetin tamamı dalâlette birleşmez" **anlamı** başka sahâbîlerden birçok tarikle gelir; usûlcüler bunu manen mütevâtir sayar (aşağı).

## Âlim/kitap alıntıları (gerçekten açılan sayfalardan)

### 1) TDV İslâm Ansiklopedisi, "İCMÂ" (İbrahim Kâfi Dönmez) — https://islamansiklopedisi.org.tr/icma (curl ile tam metin indirildi, grep'lendi)
- Tanım: "İslâm âlimlerinin dinî bir meselenin hükmü üzerinde fikir birliği etmelerini … ifade eden şer‘î delil, İslâm fıkhının Kur’an ve Sünnet’ten sonra üçüncü kaynağı." (madde başı özeti)
- "İcmâa katılma ehliyeti sadece müctehidlere aittir… ‘fukaha’, ‘ulemâü’l-ümme’, ‘ehlü’l-hal ve’l-akd’, ‘ehlü’r-re’y ve’l-ictihâd’ … dinî hükümleri kaynaklarından elde edebilme melekesine sahip … kişilerdir… müctehid olmayan ve hadis, kelâm vb. ilimlerde uzman olan kişilerin şer‘î bir konudaki icmâa etkisinin bulunmaması gerekir (Serahsî, I, 312; Gazzâlî, I, 183)."
- "Bazı yazarlarca Şâfiî’nin icmâı ‘müslümanların çoğunluğunun kanaati’ olarak tarif ettiği … ileri sürülmekteyse de gerçekte Şâfiî’nin, çoğunluğun ittifakının kaynak sayılması fikrine açıkça karşı çıktığı (el-Üm, VII, 256), icmâa katılacaklarda âlim olma şartı aradığı ve birçok yerde ‘nâs’ kelimesiyle müctehid kişileri kastettiği görülür (el-Üm, V, 35, 198; VII, 248, 257-258; er-Risâle, s. 534-535)."
- "Âmidî … icmâın bağlayıcı kaynak gücünü kazanabilmesi için avamın da dikkate alınması gerektiğini ileri sürmüşlerdir (el-İhkâm, I, 204)." → ihtilaf var.
- "İcmâın gerçekleşebilmesi için usulcülerin çoğunluğuna göre icmâa katılma ehliyetini haiz bütün müctehidlerin ittifakı şarttır. Azınlığın, hatta bir tek müctehidin muhalefeti icmâın oluşmasına engel teşkil eder. Bununla beraber … çoğunluğun fikir birliği ile icmâın gerçekleşeceğini savunanlar ya da böyle fikir birliğini icmâ saymamakla beraber hüccet kabul edenler de vardır. Çoğunluğun görüşü konusundaki tartışmaların hareket noktasını daha çok topluluktan ayrılmamayı öğütleyen hadislerin yorumu teşkil etmektedir."
- Delil: "İcmâın Kur’an’daki delili olarak başta Nisâ sûresinin 115. âyeti olmak üzere birçok âyet gösterilmektedir (Bakara 2/143, 257; Âl-i İmrân 3/103, 110; Nisâ 4/59, 83 …). … Gazzâlî ise bunların hiçbirinin maksadı (icmâı) açıkça ifade etmediğini, Nisâ sûresinin 115. âyeti kısmen kuvvetli bir delâlete sahip sayılabilirse de onun da icmâ anlamı için sevkedildiği kanaatinde olmadığını ifade etmektedir." "Şâfiî, er-Risâle’sinde … herhangi bir Kur’an âyetini delil göstermediği halde … Nisâ 115 ile istidlâl ettiğine dair bir menkıbe yaygınlık kazanmıştır."
- Sünnet delili: "Bu hadislerin birleştiği iki nokta, İslâm ümmeti içinde daima doğru üzerinde bulunacak bir grubun var olacağı, dolayısıyla bu ümmetin hata ve dalâlet üzerine birleşmeyeceği ve cemaatten (topluluk) ayrılmamanın gerekliliği esaslarıdır… Söz konusu hadisler … tek tek mütevâtir olmamakla birlikte aynı anlamı taşıyan bu kadar çok sayıdaki hadisin mütevâtir sünnet gücünde ve bir kaynağın ispatı için yeterli sayılacağı kabul edilmiştir (Cessâs, III, 264-267; Debûsî). Hadislerde geçen ‘cemaat’ kelimesinin nasıl anlaşılması gerekeceği hususunda ise değişik görüşler ileri sürülmüştür (Şâtıbî, el-İʿtiṣâm, II, 258-265)."
- İtiraz: "Aklî istidlâl yoluyla yapılan itirazların özünü ise ümmetten her bir kişinin hata etmesi mümkün olduğuna göre bütün ümmetin hata edebileceği ve kesin delile dayanıyorsa icmâa ihtiyaç bulunmadığı, zannî delile dayanıyorsa esasen tam bir fikir birliği sağlanamayacağı tezi teşkil eder."
- Konu: "Usulcülerin çoğunluğu icmâın konusunun şer‘î bir hüküm olması kaydına yer verir … dinî olmayan (meselâ kimyevî veya tıbbî) bir konuda ya da dinî olmakla birlikte şer‘î-ictihadî olmayan (meselâ sahâbeye ait bilgiler, âhiret, kıyamet alâmetleri gibi) hususlar üzerindeki görüş birliğinin icmâ sayılmayacağını ifade ederler."
- Şûra bağı: "İcmâın fikrî temelleri arasında, İslâm’ın şûrayı emretmesi ve Resûlullah’ın bizzat bu prensibi titizlikle uygulamasının yanında sahâbeye şûra ve istişare fikri kazandırmış olması ağırlıklı bir yer tutar."
- Hâl ve akd siyasî rol notu: "icmâa katılma ehliyetini haiz olanlardan ‘ehlü’l-hal ve’l-akd’ olarak söz edilmesi… icmâın politik rolüne" dikkat çekmiştir (aynı madde). — Sayfada siyasete girilmeyecek; sadece terim açıklanacak.

### 2) TDV İslâm Ansiklopedisi, "ŞÛRA" (Talip Türcan) — https://islamansiklopedisi.org.tr/sura (tam metin indirildi)
- Kök anlam: "Sözlükte ‘danışma, görüş alışverişinde bulunma…’… Şûra ile aynı kökten (şevr) türeyen birçok kelimenin ‘bir şeyi bulunduğu yerden alma ve açığa çıkarma’ mânasında birleştiği, özellikle balın kovandan çıkarılması işini anlatmak için bu kökten gelen kelimelerin kullanıldığı ve danışma işinin de bir meselede isabetli karara varabilmek amacıyla kişilerin fikirlerinin açığa çıkmasını sağlamaktan ibaret olduğu dikkate alındığında (Râgıb el-İsfahânî, el-Müfredât…)".
- Hz. Peygamber uygulaması: "Hicretin 3. yılında … Hz. Peygamber, Medine’de kalınıp savunma yapılması kanaatinde olmasına rağmen müşriklerin şehir dışında karşılanmasını daha yerinde bulan çoğunluğun görüşüne uymuş ve savaş Uhud’da gerçekleşmiştir (Vâkıdî, I, 209-214; İbn Hişâm, III, 67-68). Hudeybiye’de … Ümmü Seleme ile konuşup tavsiyesine uyması da … (Buhârî, “Şürûṭ”, 15)."
- Konu: "nasla düzenlenen meselelerin şûra konusu yapılamayacağı ittifakla kabul edilmiştir. Hz. Peygamber’in vahiyle bildirilen şer‘î hükümlere dair ashabı ile istişare etmemesi bu anlayışın temel dayanaklarındandır."
- Bağlayıcılık: "Klasik fıkıh doktrinindeki genel yaklaşıma göre şûra sonucunda ortaya çıkan karar çoğunlukla, hatta ittifakla alınmış olsa bile devlet başkanı için bağlayıcı değildir. Zira … ‘Kesin karar verdiğinde artık Allah’a tevekkül et!’ (Âl-i İmrân 3/159)… (Taberî)… Çağdaş hukukçu ve araştırmacıların büyük çoğunluğu ise şûrada ortaya çıkan umumun görüşünü devlet başkanını hukuken bağlayıcı bir karar diye telakki etmektedir." → âlimler ayrılıyor; sayfada sadece "ayrılıyorlar" denecek, taraf tutulmayacak.
- Ehlü'l-hal ve'l-akd: "Müellifler ehlü’l-hal ve’l-akdi müslümanların erdemlileri, ictihad ve adalet ehli, icmâ ehli, ilim ve din ehli, toplumun önde gelenleri… gibi tabirlerle niteleme yoluna gitmişlerdir"; "objektif kriterler geliştirilememiştir."
- Hükmü: azınlık görüş (klasik) vâcip; çağdaş büyük çoğunluk vâcip.
- Madde başlıklarından "CEMAAT" ve "SEVÂD-ı A'ZAM": islamansiklopedisi.org.tr/cemaat ve /sevad-i-azam adresleri **arama sonuç sayfası** döndürdü; bu adlarla ayrı bir TDV maddesi bulunamadı. Sevâd-ı a'zam için TDV'de "EHL-i SÜNNET" maddesi geçişi şu cümle: "…büyük ekseriyeti (sevâd-ı a‘zam) ve müctehid âlimler gibi…" (arama sonucu kesiti; tam madde okunmadı). **Sayfada "TDV Cemaat/Sevâd-ı a'zam maddesi" diye atıf YAPMA.**

### 3) Şâfiî, er-Risâle (Ahmed Muhammed Şâkir nşr.), "Bâbü'l-İcmâ" — Şâmile: https://shamela.ws/book/8180/556 ve `ajax/pageContent/8180/557-562` (açıldı; Arapça okundu; çeviri bana ait)
- §1312 (s. 472): "…Sünnetler onların âmmesinden (genelinden) uzak kalmaz, bazılarından uzak kalabilir; ve biz biliriz ki onların âmmesi Resûlullah'ın sünnetine aykırı bir şey üzerinde birleşmez, yanlışta da (birleşmez) inşallah."
- §1316-1320 (s. 475-476), cemaate sarılmanın anlamı: "…Cemaatleri ülkelere dağılmış ise kimse bedenleri dağınık bir topluluğa 'sarılamaz'. Bedenler Müslimlerle kâfirlerin, takvâ sahipleriyle facirlerin bir arada olduğu şekilde de bir araya gelebilir; bu yüzden bedenlere sarılmanın bir anlamı yoktur — çünkü mümkün değildir, bedenlerin bir arada olması da bir şey ifade etmez. O halde 'cemaate sarılmak'ın tek anlamı, cemaatin helâl-haram konusunda ve bunlara itaatte söylediğine sarılmaktır. Müslümanların cemaatinin söylediğini söyleyen cemaatlerine sarılmıştır; onların söylediğine aykırı olan, emredildiği cemaate aykırı davranmıştır. Gaflet ancak ayrılıkta olur; cemaatin tamamında ise Kitab'ın, sünnetin veya kıyasın anlamına dair (kollektif) bir gaflet olamaz, inşallah." (§1319-1320; s. 476 başı)
- Şâkir dipnotu (s. 474, n.5): Hz. Ömer'in Câbiye hutbesi için "ahadis-i sahih ve maruf… Ahmed, Tayâlisî, İbn Mâce'nin bir kısmı, Tirmizî (Fiten, Lüzûmü'l-cemâa; 'hasen sahih garîb'), Hâkim (sahih; Zehebî muvafık)".

### 4) Gazâlî, el-Müstasfâ min ilmi'l-usûl, "el-Aslü's-sâlis: el-İcmâ" — https://www.islamweb.net/ar/library/content/33/130/ (sayfa tam indirildi; s. 139-143 işaretleri metinde; Arapça okundu, çeviri bana ait)
- Rivayetlerin toplu delaleti: "Hz. Peygamber'den, anlamda birleşen farklı lafızlarla, bu ümmetin hatadan korunduğu (ismet) konusunda rivayetler yaygınlaşmıştır… ‘Ümmetim dalâlette birleşmez’, ‘Allah ümmetimi dalâlet üzere toplayacak değildir’, ‘Cennetin ortasında yer almak isteyen cemaate sarılsın’, ‘Şeytan teke birlikte, iki kişiden uzaktır’, ‘Allah'ın eli cemaatle beraberdir’, ‘Ümmetimden bir tâife hak üzere zâhir olur…’, ‘Cemaatten bir karış ayrılıp ölen cahiliye ölümüyle ölür’." (s. 139)
- Delilleme: "Bu haberlerin tek tek tevâtürü mümkün değil, âhâd nakli ilim ifade etmez denirse… iki yol: (1) Biz zarurî bilgi iddia ederiz: Hz. Peygamber bu ümmetin şanını yücelttiğini ve hatadan korunduğunu bu dağınık haberlerin **toplamıyla** bildirmiştir, tek tek tevâtür olmasa da — tıpkı Ali'nin cesaretini, Hâtem'in cömertliğini, Şâfiî'nin fıkhını bildiğimiz gibi." (s. 139)
- **Sayıyla ilgili kritik pasaj (s. 142-143)**: "Üçüncü yol: manevî yola dayanmak (et-tarîku'l-ma'nevî)… **Bu yol bizce zayıftır.** Çünkü hatanın kaynağı ya kasıtlı yalan ya da kesin olmayan şeyi kesin sanmaktır. Birincisi tevatür sayısına ulaşan topluluğa yakışmaz; ikincisi ise caizdir: Yahudiler Îsâ ve Muhammed (a.s.)'ın peygamberliğinin batıl olduğuna kesin hükmettiler ve sayıları tevatür sayısından fazlaydı; bu, kesin olmayan şeyde kesin hükümdü… Âlemin hudûsunu, peygamberliği inkâr edenler ve her türlü bid'at ve dalâlete düşenlerin sayısı da tevatür sayısına ulaşır… ama kesin olmayan yerde kesin hükmetmekle yanıldılar. Bu görüşü savunan kimse Yahudilerle Hıristiyanların icmâını da delil saymak zorunda kalır; oysa bu ümmete has bir durum olmuştur…" → Gazâlî'nin kendi gerekçesi: **sayı hakkı garanti etmez; icmâ'nın bağlayıcılığı sayıdan değil, Hz. Peygamber'in bu ümmete verdiği ismet haberinden gelir.**
- "Ayetler": TDV'ye göre Gazâlî bu ayetlerin hiçbirinin icmâ için açık delil olmadığını söyler (el-Müstasfâ, I, 173-217 aralığı).

### 5) İbn Hacer, Fethu'l-Bârî (Selefiyye baskısı) 13/37, Fiten, "bâb kayfe'l-emr izâ lem tekün cemâa" — Şâmile https://shamela.ws/book/1673/7468 (ajax/pageContent/1673/7468, s. 37) — okundu
- İbn Battâl: "(Bu hadiste) fakihler topluluğu için müslümanların cemaatine sarılmanın ve cevr ehli imamlara karşı çıkmayı terk etmenin vâcip olduğuna delil vardır…"
- Taberî'den aktarım: "Bu emirde ve cemaat konusunda ihtilaf edilmiştir: Bir kısım 'emir vâciptir, **cemaat = sevâd-ı a'zam**' dedi. (Ebû Mes'ûd'un, Hz. Osman öldürülünce kendisine soran kişiye 'Cemaate sarıl; Allah Muhammed ümmetini dalâlet üzere toplamaz' dediği rivayeti zikredilir.) Bir kısım 'kastedilen yalnız sahâbedir' dedi. Bir kısım 'kastedilen **ehl-i ilimdir**; çünkü Allah onları halk üzerine hüccet kıldı, insanlar dinde onlara tâbidir' dedi. **Taberî dedi ki: Doğrusu, haberden kastedilenin, emirliği üzerinde birleştikleri kimsenin itaatindeki cemaate sarılmak olduğudur; biatını bozan cemaatten çıkmış olur.**" → Aynı sayfada devam: "hadiste, insanların imamı olmayıp fırkalara ayrıldığı zaman, fırkalardan hiçbirine uymayıp mümkünse hepsinden uzak durulacağı…"
- Bu pasaj "cemaat" kelimesinin klasik literatürde en az dört yoruma sahip olduğunu belgeliyor. Sayfada Taberî'nin tercihini **İbn Hacer'in aktarımıyla** ver.

### 6) Elbânî (ses kaydı dökümü) — https://www.al-albany.com/audios/content/11168/ (indirildi, okundu)
- "Hadis icmâ ile ilgili değildir; icmâ ispat eden (sübûtî) bir şeydir, hadis ise bir şeyi nefyeder… ‘Ümmet dalâlette birleşmez’ dokuz kişi hakta bir kişi batılda ya da tersi olsa da ümmet dalâlette birleşmiş olmaz… Hadisi icmânın sübûtuna delil yapmak büyük bir hatadır." İcmâyı Nisâ 4:115'in "mü'minlerin yolu"na bağlar ve esas olarak "dinden zorunlulukla bilinen" şeylerde görür. (Alıntıda "birebir Türkçe çeviri" yerine özetle ver; Arapça birebir kullanılmayacak.)
- Aynı kaynağa göre Elbânî, icmâ tanımı konusunda dört beş görüş sayar (ümmet / ulema / sahâbe / Medine / Kûfe).

### 7) Tirmizî'nin cemaat tefsiri — Tirmizî 2167 sonunda (ara-tirmidhi API'sinden okundu): "وَتَفْسِيرُ الْجَمَاعَةِ عِنْدَ أَهْلِ الْعِلْمِ هُمْ أَهْلُ الْفِقْهِ وَالْعِلْمِ وَالْحَدِيثِ" ("Ehl-i ilim nezdinde cemaatin tefsiri, fıkıh, ilim ve hadis ehlidir.") + İbnü'l-Mübârek anekdotu.

### 8) Dâru'l-İftâ el-Mısriyye "sevâd-ı a'zam" fetvası (https://www.dar-alifta.org/ar/fatwa/details/16611) — açıldı; **sayfada KULLANILMAYACAK**: terimi belirli kelâmî ekollerle özdeşleştiriyor; bu sayfanın "grup işaretlememe" ilkesiyle uyumlu olmaması için bilinçli olarak dışarıda bırakıldı. (Bu da bir "âlimler farklı yorumluyor" kanıtı.)

## Zayıf/tartışmalı notlar
- "Kim ayrılırsa ateşe ayrılır" (ve men şedde şedde ile'n-nâr) → Tirmizî 2167'de; Elbânî'ye göre bu hadis zayıf (API). İkincil kaynakta (alukah özeti) Elbânî'nin bu kısmı ayrıca zayıf saydığı yazıyor — teyit edilemedi.
- "Ebû Şâme: sevâd-ı a'zam = hakka sarılmak, azlık/çokluk fark etmez" (mshmsdin.com ve çeşitli forumlar aktarıyor; birincil kitap `el-Bâ'is` sayfası açılamadı → "teyit edilemedi", sayfaya koyma).
- "Cemaat = Ahl-i sünnet" tarzı ekol özdeşleştirmeleri (Dâru'l-İftâ, İbn Sebkî'den Zebîdî aktarımı, Abdülkâhir el-Bağdâdî) — güncel grup işaretlememek için dışarıda.
- Mâ ene aleyhi hadisinde "ateşte" ifadesi, fırkaları tekfir etme hakkı vermez; ehl-i sünnet ana çizgisinde "ehl-i kıble tekfir edilmez". Bu iddia için kaynak açılmadı → sayfada sadece "kimin hangi fırkada olduğuna hükmetmek bu sayfanın işi değil" demekle yetin.
- Busayrî, İbn Hacer, Sehâvî'nin 3950 hakkında sözleri: ikincil özetlerden (arama motoru) geliyor; birincil sayfa açılamadı. Sayfada yalnız API'deki Elbânî/Arnaut/Abdülbâkî/Zübeyr derecelerini ver; Busayrî için "zayıf dediği aktarılır (kitap sayfası doğrulanamadı)" de ya da hiç yazma.

## Canlı sahne/benzetme fikirleri (sayfada kullanılan)
- "Uçuş kontrol kulesi": 300 yolcunun oyu ile pilotun ilmi; yolcular uçağın nereye ineceğini oylamaz; ama hava durumu bilgisini kule + pilotlar + radar birlikte teyit edince güvenilir. (benzetme; sayfada kısa, delil olarak değil anlatım aracı)
- Balı kovandan çıkarmak (şûra'nın kök anlamı; Râgıb/TDV).
- İbnü'l-Mübârek anekdotu: "cemaat" sayı değil, kimlik: "Ebû Hamza es-Sükkerî bir cemaattir."
- Şema (a): üç kavram haritası (çoğunluk/kanaat – icmâ – şûrâ): kim karar verir? neye dayanır? neyi kapsar? sayı ölçü mü?
- Şema (b): "sayısal çoğunluk" ile "ilmî ittifak" karşılaştırma şeması.
- Şema (c): "Ayet ile hadis çelişki mi?" çözüm akışı.

## Açılan kaynak URL'leri
- https://islamansiklopedisi.org.tr/icma (tam metin)
- https://islamansiklopedisi.org.tr/sura (tam metin)
- https://islamansiklopedisi.org.tr/cemaat ve /sevad-i-azam (arama sayfası; madde yok)
- https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/{eng,ara}-{bukhari,muslim,tirmidhi,abudawud,ibnmajah}.json
- https://shamela.ws/book/8180/556 + https://shamela.ws/ajax/pageContent/8180/557..562 (Şâfiî, er-Risâle)
- https://shamela.ws/ajax/pageContent/1673/7468 (Fethu'l-Bârî 13/37)
- https://www.islamweb.net/ar/library/content/33/130/… (Gazâlî, el-Müstasfâ, s. 139-143)
- https://www.al-albany.com/audios/content/11168/… (Elbânî dökümü)
- https://www.dar-alifta.org/ar/fatwa/details/16611/… (açıldı, kullanılmadı)
- Engelli/açılamayan: dorar.net (Cloudflare), mshmsdin.com/majles.alukah (ikincil; yalnız ipucu).
- WebSearch özetleri (ikincil, KULLANMA): hadithanswers.com, islamqa.info, ibn-jebreen.com, ibnamin.com.
