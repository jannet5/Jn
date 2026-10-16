# Denetim — 11 Peki Şimdi Ne Yapacağım? (yol haritası + SSS + kapanış)

Denetçi notu: Ayetler api.quran.com (Arapça uthmani + Diyanet id 77) ile betik (`araclar/ayet_dogrula.py`) ve ek olarak blok başına sıkı (birebir) karşılaştırma ile sınandı; hadisler fawazahmed0/hadith-api (eng/ara-bukhari, eng/ara-muslim, eng-tirmidhi, eng-abudawud, eng-ibnmajah) ile metin + numara + derece alanından doğrulandı; Gazâlî metni archive.org djvu.txt'ten, Ormsby alıntısı Matheson Trust sayfasından, Nursî metinleri nurpedia.org'dan, kriz hattı bilgisi resmî sitelerden açılıp kontrol edildi. Şema PNG'leri (390 px, açık/koyu) gözle kontrol edildi.

## 1. Ayet blokları (13)

| Öğe | Durum | Not |
|---|---|---|
| Bakara 2:197, Asr 103:1-3, Kehf 18:110, Haşr 59:18, Bakara 2:186, 2:201, Âl-i İmrân 3:8, Furkân 25:74, İbrâhîm 14:40-41, A'râf 7:23, İsrâ 17:85, Zümer 39:53 | doğru | Arapça ve Diyanet meali birebir |
| Kehf 18:110 | düzeltildi (yazım) | Hem api.quran.com hem alquran.cloud `tr.diyanet` "yararlı iş **işleşin**" veriyor (Diyanet kaydındaki bariz yazım hatası). Okurda bizim hatamız izlenimi vermesin diye "işlesin" yapıldı. Betik bu tek kelime farkı için uyarı veriyor (türkçe=0.852); bilinçli sapma. |
| Yâsîn 36:78-83 | düzeltildi | Diyanet 77. ve 78. ayetleri tek parça çeviriyor; meal 77. ayetin içeriğiyle başlıyordu ama Arapça/atıf 78'den başlıyordu. Atıf 36:77-83 yapıldı, 77. ayetin Arapçası eklendi, cite'a "tek parça çevrilmiş" notu düştü. |

## 2. Metin içi ayet alıntıları

| Öğe | Durum | Not |
|---|---|---|
| Bakara 2:155-157 | düzeltildi | "biraz açlık, mallardan…" → Diyanet: "biraz açlık **ve** mallardan, canlardan, ürünlerden biraz eksiltmekle deneriz" |
| İbrâhîm 14:7 | düzeltildi | "and olsun ki," eklendi (Diyanet: "Şükrederseniz and olsun ki, size karşılığını artıracağım") |
| İsrâ 17:14 | düzeltildi | "bugün," virgülü (Diyanet: "Kitabını oku, bugün, hesap görücü…") |
| İnsan 76:2-3 | düzeltildi | Serbest özet tırnak içindeydi; Diyanet metniyle değiştirildi |
| Nisâ 4:48 | düzeltildi | "elbette" eklendi |
| Nisâ 4:124 | düzeltildi | "mümin olarak," (Diyanet yazımı ve virgülü) |
| İnşirâh 94:5 | düzeltildi | "şüphesiz" eklendi |
| Kıyâme 75:3-4 | düzeltildi | 75:3 serbestçe değiştirilmişti; Diyanet: "…toplayamayız mı sanıyor? Evet, Biz onu, parmak uçlarına…" (alquran.cloud ile çapraz kontrol edildi, aynı) |
| Bakara 2:186 (metin içi) | düzeltildi | "Ben, şüphesiz onlara yakınım" |
| Zâriyât 51:56 (Son söz) | düzeltildi | Diyanet: "…kulluk etmeleri için yaratmışımdır" |
| Muhammed 47:24 | düzeltildi | Diyanet yazımı: "Kuran'ı… kalbleri" |
| İsrâ 17:15 (2 yer) | düzeltildi | Diyanet: "azabetmeyiz" |
| A'râf 7:187 | düzeltildi | "sizlere" küçük harf (cümle ortası) |
| Zilzâl 99:7 (Q11) | değiştirildi | Gayrimüslim iyilikleri bağlamında yanlış anlaşılabilirdi; Nisâ 4:40 ("zerre kadar haksızlık yapmaz") ile değiştirildi |
| Zâriyât 51:56 ("boşuna yaratılmadı" anlamında, Q10) | düzeltildi | Ayet kulluk amacını söyler; "boşuna yaratmadın" Âl-i İmrân 3:191'e bağlandı |
| Ankebût 29:45, 29:2-3, Mülk 67:2, Kehf 18:108, Secde 32:17, Tûr 52:21, Zilzâl 99:7-8 (Q2), Nahl 16:97, Kehf 18:107, Bakara 2:153, 2:286, Tahrîm 66:8, Âl-i İmrân 3:135 ve 3:191, Furkân 25:70, Zümer 39:53 (parça), Ra'd 13:28, Bakara 2:177, Rûm 30:30 | doğru | Tırnak içi parçalar Diyanet metniyle uyuşuyor; kalan atıflar anlam olarak tutuyor |

## 3. Hadisler (28 öğe)

| Öğe | Durum | Not |
|---|---|---|
| Müslim 8 (Cibril hadisi) | doğru | İslâm–iman–ihsân–Saat soruları ve cevaplar metinle uyuşuyor. Giriş cümlesi "üç soru" idi, dördüncü (kıyamet) soru da var; düzeltildi |
| Buhârî 1 | doğru | Sahih |
| Buhârî 6465; Müslim 783 | doğru | Sahih ("gücünüzün yeteceği kadar" Buhârî'de) |
| Buhârî 6407 | doğru | Sahih |
| Buhârî 2449; Müslim 2581 | doğru | Sahih |
| Tirmizî 413 | doğru | Elbânî/Şâkir sahih, Beşşâr hasen, Zübeyr Ali Zay zayıf; metin bu ihtilafı zaten yazıyor. "Hadise göre kıyamette…" diye atıf netleştirildi |
| Müslim 2999 | doğru | Sahih |
| Müslim 2564 | doğru | "yüzlerinize ve mallarınıza değil, kalbinize ve amellerinize bakar" 2564.03'te var |
| Tirmizî 1987 (hasen), 1956 (sahih), Buhârî 1417 | doğru | Dereceler veri tabanıyla uyuşuyor |
| Tirmizî 2459 | doğru (zayıf notu) | Elbânî, Şâkir ve Zübeyr Ali Zay üçü de zayıf diyor; aside'a Zübeyr Ali Zay eklendi. Hz. Ömer'in sözü Tirmizî'de "rüviye" kalıbıyla; Gazâlî'de "Said Umar" diye geçiyor (gaz.txt'te doğrulandı) |
| Buhârî 1385; Müslim 2658 (fıtrat) | doğru | Sahih |
| Buhârî 1383, 1384; Müslim 2658 (tevakkuf) | doğru | Sahih; numaralar Fethu'l-Bârî sırasıyla tutuyor |
| Buhârî 7047 | düzeltildi (ifade) | Rüya hadisi doğru. "Müşriklerin çocukları da mı?" soru biçimi hadis metnine uydurularak "Müşriklerin çocukları ne olacak?" → "Müşriklerin çocukları da" yapıldı; Hz. İbrâhim'e (a.s.) eklendi |
| Buhârî 1248 | düzeltildi (bağlam) | Hadis çocukların değil, bulûğdan önce üç çocuğu ölen müslüman ebeveynin cennete girmesini bildirir. Mü'min çocuklar paragrafında çocukların âkıbeti delili gibi duruyordu; yas tutan ebeveyn paragrafına taşındı ve ebeveyne ait olduğu açık yazıldı |
| Müslim 2752 | doğru | Yüz rahmet; hayvanın ayağını kaldırması 2752.01'de var |
| Buhârî 3244; Müslim 2824 | doğru, ifade düzeltildi | "hayalinden geçmeyen" → Müslim'deki "kalbine doğmayan" |
| Buhârî 3688; Müslim 2639 | doğru | Sahâbenin sevinci Enes'ten aynen |
| Tirmizî 3540 | doğru | Elbânî/Şâkir sahih, Beşşâr hasen |
| Buhârî 3470; Müslim 2766 | doğru, ifade netleştirildi | Buhârî: "Beni İsrâil'den"; cümle "bir rahibi de öldürerek sayıyı yüze çıkaran" diye düzeltildi (önceki cümle "yüzüncüyü" demekle ne kastedildiğini belirsiz bırakıyordu) |
| Müslim 2749 | doğru | Sahih |
| Tirmizî 2038; Ebû Dâvûd 3855; İbn Mâce 3436 | doğru | "ihtiyarlık istisnası" üçünde de var; Elbânî sahih |
| Buhârî 5678 | doğru | Sahih |
| Tirmizî 2517 | doğru | Hasen (Elbânî, Şâkir, Zübeyr Ali Zay) |
| Müslim 2699 | doğru | Sahih |
| Buhârî 6389; Müslim 2690 | doğru | Sahih |

Not: Dereceler fawazahmed0 veri tabanındaki `grades` alanından (Elbânî, Şâkir, Beşşâr Avvâd Ma'rûf, Zübeyr Ali Zay, Arnaut) alındı; dorar.net'e doğrudan bakılmadı.

## 4. Âlim / kitap alıntıları

| Öğe | Durum | Not |
|---|---|---|
| Gazâlî — Hz. Ömer: "Hesaba çekilmeden önce kendinizi hesaba çekin…" | doğru | archive.org (Shaker çevirisi, 1. bölüm) metninde "Said 'Umar [b. al-Khattab]…" ve 3. bölümde "Examine yourselves…" var |
| Gazâlî 59:18'i "geçmiş amelleri gözden geçirmek" diye okuması | doğru | "This signifies the examination of past acts." (3. Sabit Bağlılık, "Merit of Self-examination") |
| Gazâlî — altı basamak (müşârata, murâkabe, muhâsebe, muâkabe, mücâhede, muâtebe) | doğru | Çevirmenin girişinde sıra ve adlar aynen |
| Gazâlî aktarımıyla Hasan-ı Basrî | düzeltildi (ifade) | "Hesap, dünyada nefsini hesaba çeken bir topluluk için hafiftir" cümlesi metne daha yakın yazıldı |
| Gazâlî/Ormsby "mümkünde bundan daha güzeli yoktur" alıntısı | düzeltildi | İngilizce metin Matheson Trust sayfasında birebir kontrol edildi. "sheer justice … pure right" → "tam adalet" yerine "salt adalet; saf hak, içinde yanlış yok" yazıldı; "not deigning to do so" çevirisi düzeltildi; aradaki "…" iman-küfür ve itaat-günah sayımının atlandığını gösteriyor (korundu). Eserdeki (İhyâ) yer sayfada yok, cite'ta öyle denildi |
| "Sonraki yüzyıllarda ayrı risalelerin konusu oldu; bazıları… savunanlar…" | düzeltildi | Süyûtî'nin bu cümle üzerine bir risalesi Princeton katalog kaydıyla doğrulandı (Tashyîdü'l-erkân…); eleştiren/savunan ayrıntısı doğrulanamadığı için genel ve ihtiyatlı ifadeye çekildi |
| Said Nursî, 26. Söz, 1. Mebhas | düzeltildi | Cümle sırası metinde değişmişti ("halk ve icadda…" ile "yağmur" cümleleri ters ve "…" ile bağlanmıştı); nurpedia metnindeki sıraya getirildi, "…" gerekmedi |
| Said Nursî, 10. Söz (Dokuzuncu Hakikat: "Haşr-i baharîde…", "ruh programını…"; Hâtime: "tek insanın halkı ve haşri gibi âsandır") | doğru | Üçü de nurpedia metninde var. "Binlerce çeşit" → metinde "üç yüz binden ziyade" olduğu için "yüz binlerce çeşit" yapıldı |
| Malezya Müftülüğü derlemesi (çocukların âhiret hükmü) | doğru | Sayfa açıldı: Cennet (Nevevî, İbn Hacer), imtihan (İbn Kesîr, Kurtubî, İbn Kayyım "tercih"), tevakkuf. "Nispet edilen" ifadesi "tercih etmiştir (Malezya Müftülüğü'nün derlemesine göre)" yapıldı |

## 5. Olgusal / kelâmî denetim ve hassas SSS

| Öğe | Durum | Not |
|---|---|---|
| "Yılda yaklaşık otuz altı saat" (günde 5 dk) | düzeltildi | 5 x 365 / 60 = 30,4 saat; "otuz saat" |
| Q4 Cehennem ebedî mi | düzeltildi | "Az sayıda âlim farklı yorumlamış" ifadesi 10. bölümle ve TDV İA tartışmasıyla çelişiyordu. "Ehl-i sünnet âlimlerinin çoğunluğu ebedî azap diye anlamıştır; farklı yorum yapanlar da olmuştur" yapıldı; imanlı günahkârın "azap görecekse bile sonunda çıkarılması" eklendi; 10. bölüme bağlantı |
| Q5 Çocuklar | düzeltildi | Müslüman çocukları için "ümit tam" (genel görüş cennet; icma iddiası kurulmadı); müşrik çocukları için üç görüş ihtiyatla; yas tutan ebeveyne şefkatli kapanış; "bilinmeyen için…" ifadesi (Müslüman çocuğu için uygunsuzdu) kaldırıldı |
| Q11 İyi insan olmak yetmez mi | düzeltildi | "Ehl-i sünnete göre ahiret kurtuluşunun temeli imandır, iyilik imanın meyvesidir; iyiliği küçümsemiyoruz ama imanın yerini tutmaz" açıkça eklendi; "iyiliği zayi etmez (99:7)" Nisâ 4:40 ile değiştirildi; kişilere hüküm verilmediği ve davet ulaşmamışlar için 17:15 korunarak ton şefkatli bırakıldı |
| Q10 Depresyon | düzeltildi | "Tedavi edilebilen sağlık durumu"; "anlamsızlık hastalık belirtisidir" cümlesi "depresyonda sık görülen belirti; duygu gerçektir, hafife alınmaz" diye yumuşatıldı; "dua ve ibadet tedavinin yerine geçmez, birlikte yürür" eklendi; intihar düşüncesi için "yalnız kalma, güvendiğin birine söyle" |
| Q8 Günahım çok | düzeltildi (ton) | "Umudu kaybetmek, günahtan daha büyük bir kapıyı kapatır" (suçlayıcı okunabilirdi) → "Kapı açıktır; onu kapatan şey günahın büyüklüğü değil, umudun kesilmesidir" |
| Q3, Q6, Q7, Q9, Q1, Q2 | doğru/ince düzeltme | Kıyamet tarihi verilmiyor; ruh sorusu "Rabbimin emri" ile sınırlı; haşir "ispat/çürütme yok, imkân savunulur" çizgisinde; Gazâlî çerçevesinin "acı çekene söylenmez" uyarısı korunmuş |

## 6. Kriz hattı doğrulaması (Türkiye)

| Öğe | Durum | Kaynak |
|---|---|---|
| 112 | doğru | İçişleri Bakanlığı: polis, jandarma, sağlık, itfaiye, AFAD vb. tek acil numara, ücretsiz, 7/24 (icisleri.gov.tr / 112.gov.tr sayfaları) |
| Alo 182 | doğru (randevu hattı) | mhrs.gov.tr: "Mobil uygulama, Web ve Alo182 Randevu hattından…". Bazı özel sitelerin "182 = kriz/intihar hattı" iddiası resmî kaynakta yok; metinde de kriz hattı olarak sunulmuyor |
| ALO 183 | düzeltildi (tanım) | aile.gov.tr/alo-183: Aile ve Sosyal Hizmetler Bakanlığı, "Şiddetle Mücadele Hattı", 7/24, yurt içinden ücretsiz, rehberlik ve danışmanlık; sayfada psikolojik/ruh sağlığı hizmeti geçmiyor. Metin "aile içi şiddet, istismar, ihmal" bağlamıyla ve "ruh sağlığı kriz hattı değildir" notuyla yeniden yazıldı |
| "Türkiye'de ayrı intihar önleme hattını doğrulayamadım" (birinci tekil) | çıkarıldı/yeniden yazıldı | Resmî ve doğrulanmış bir ulusal intihar önleme hattı bulunamadı (WebSearch + resmî siteler). Metin artık sadece doğrulanmış 112, acil servis, ruh sağlığı uzmanı/aile hekimi, 182 (randevu) ve 183 (şiddet/istismar) bilgilerini veriyor; "güncel bilgi için resmî sitelere bak" notu ekli |

## 7. Bölüm atıfları (Bölüm 00–10)

| Öğe | Durum | Not |
|---|---|---|
| Son söz paragrafı | düzeltildi | "Ben kimim, niçin buradayım? (Bölüm 00)" yanlıştı (00 = Neden görmek istiyoruz, 01 = Ben niçin buradayım); "(Bölüm 01-08)" ve "(Bölüm 09)" gruplaması da yapıya uymuyordu. Paragraf gerçek yapıya göre yeniden yazıldı: 00 Neden görmek istiyoruz · 01 Ben niçin buradayım · 02 Dünya kısa bir durak · 03 Ölüm ve berzah · 04 Kıyametin kopuşu · 05 Diriliş ve mahşer · 06 Hesap ve mizan · 07 Sırat, nur ve havuz · 08 Cennet (mekân ve nimetler) · 09 Cennetin zirvesi · 10 Cehennem, adalet, rahmet. Her biri `href="#b-NN"` bağlantısı; uzun paragraf üçe bölündü |
| SSS içi atıflar | düzeltildi | Q3 "09. bölüm" (Cennette sıkılır mıyız? gerçekten 09'da) ve Q4/Q11 "10. bölüm" (ebedîlik, ehl-i fetret 10'da) bağlantılı yapıldı; Q7 ruh sorusu "berzah ve diriliş bölümleri" yerine 03 ve 05'e bağlandı. Bütün hedef id'ler derlenmiş sayfada var |

## 8. HTML sözleşmesi ve şemalar

| Öğe | Durum | Not |
|---|---|---|
| Sınıf, `<script>`, dış URL, sabit hex, kapanmayan etiket | doğru | İzinsiz sınıf yok; dış URL yok; hex yok; etiketler dengeli |
| Şema 1 (Asr dört direk) | düzeltildi | Açık temada koyu sahne üstünde `var(--gold)` metin düşük kontrastlıydı → `var(--gold-fill)` ve `var(--on-night)` |
| Şema 2 (7 adım merdiveni) | düzeltildi | Açık temada 7. adım dairesi `var(--gold)` + koyu rakam düşük kontrastlıydı → `var(--gold-fill)` |
| Şema 3 (haftalık plan) | düzeltildi | "Cuma" kutusu aynı sorun (`var(--gold-fill)`); alt şerit metni `var(--on-night)` |
| Taşma testi (qa-bolum.js) | doğru | Açık ve koyu temada `over: []`, `svgTextTasma: []` |

## 9. Türkçe kalite (küçük düzeltmeler)

"bir hatırlatma yapma, mesaj at" (anlamsız) → "ertelemeden ara ya da mesaj at"; "Bunun ucuz tarafı" → "Bugün bir telefonla kapanabilecek borç, ertelendikçe ağırlaşır"; "sonra diğerlerine büyü" → "vakitleri birer birer ekle" (namazın "bir vakitten başlama" önerisi fetva izlenimi vermesin diye "hedef elbette beş vakittir" ile); "Bir damla düşmesi gibi" → "Damla damla düşen su taşı oyar"; "tuttum/topladım/çekinmedim" → "tuttuk/topladık/çekinmedik" (site "biz" dilinde); "elindeki sermaye, yani zaman, eriyip" → "elindeki sermaye olan zaman eriyip"; "Namazın ve bağışlanmanın dualarla birleştiği yer…" → "Namaz ve bağışlanma için…"; Cibril girişinin ve "takva soyut kelime" geçişinin sadeleştirilmesi.

## Kalan riskler

- Kehf 18:110'daki "işlesin" düzeltmesi Diyanet'in dijital kaydından bilinçli bir sapmadır (betik uyarı verir); istenirse API'deki haline geri alınabilir.
- Nursî alıntıları basılı baskıdan değil nurpedia.org metninden; sayfa numarası yok.
- Gazâlî/Ormsby pasajı yalnız Matheson Trust'ın verdiği özetten; İhyâ'daki yeri ve tartışmanın ayrıntısı birincil kaynaktan doğrulanmadı.
- Nevevî/İbn Hacer/İbn Kesîr/Kurtubî/İbn Kayyım atıfları Malezya Müftülüğü makalesine dayanır (ikincil); Fethu'l-Bârî, et-Tezkire vb. açılmadı.
- "Müslüman çocukları konusunda genel görüş cennet" ifadesi ihtiyatla yazıldı; icma iddiası kurulmadı ve birincil kaynakta ayrıca doğrulanmadı.
- Hadis dereceleri veri tabanı alanlarından; dorar.net'ten ayrıca doğrulanmadı.
- Türkiye'de resmî, doğrulanmış bir ulusal intihar önleme hattı bulunamadı; metin bunu iddia etmiyor. Hizmetler değişebilir.
