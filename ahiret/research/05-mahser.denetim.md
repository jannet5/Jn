# 05 Diriliş ve Mahşer: denetim raporu

Denetçi notu: Ayetler api.quran.com (Uthmani + Diyanet id 77) ile hem `araclar/ayet_dogrula.py` hem de ayrıca yazılan sıkı betikle (ayet aralığına göre birebir Arapça ve Türkçe karşılaştırma) denetlendi. Hadis metinleri ve numaraları fawazahmed0 baskılarından (ara/eng-bukhari, ara/eng-muslim, eng-tirmidhi, eng-abudawud) doğrulandı; Müslim numaraları `arabicnumber` (Abdülbâkî) alanıyla eşleştirildi. İbn Kesîr alıntıları quran.com tefsir API'sinden (kısaltılmış İngilizce id 169 ve Arapça id 14), Nursî alıntıları erisale.com'dan, TDV alıntısı islamansiklopedisi.org.tr'den, Kurtubî alıntısı read.shamela.ws/book/30031 sayfalarından birebir okundu. Bölüm dosyası kontrol edildi: etiketler dengeli, kapanmayan etiket yok, "Aklında kalsın" kutusu (5 madde) ve `</section>` yerinde, yarım cümle yok.

## Ayetler

| Öğe | Durum | Not |
|---|---|---|
| 21 `blockquote.ayet` (39:68; 36:77-78; 36:79; 30:27; 75:3-4; 41:39; 22:5; 2:259; 2:260; 36:51-53; 54:7-8; 21:104; 23:101; 80:33-37; 70:4; 83:4-6; 20:109; 17:79; 19:85-86; 80:38-39; 32:17) | doğru | Arapça ve Türkçe API ile birebir aynı; sure adı ve numaralar doğru |
| Metin içi 36:82 | düzeltildi | Alıntı doğru ama "Yâsîn de şöyle biter" yanlıştı (sure 36:83 ile biter); "Yâsîn'in sonlarında da şöyle buyurulur" yapıldı |
| Metin içi 46:33; 50:3; 50:4; 50:11; 30:19; 17:52; 18:19; 18:21; 79:13-14; 14:48 | doğru | Tırnak içi ifadeler Diyanet metniyle aynı; kısaltmalar anlamı bozmuyor |
| Metin içi 79:8-9; 78:38; 80:40-41; 22:6-7; 50:41 | doğru | Tırnaksız özet ya da kısa alıntı; anlam tutuyor |
| Metin içi Tâhâ 20:106-107 (yeni atıf) | düzeltildi | Sahnedeki "ne bir ağaç, ne bir bina" ayete dayanmıyordu; Diyanet metnindeki "ne çukur, ne tümsek" ile değiştirildi |
| Yâsîn 36:77 giriş cümlesi | düzeltildi | "Bir insan elinde kemikle gelir" Kur'an metninde yok; iniş sebebi rivayeti olarak ve İbn Kesîr'e atfen yazıldı (Übey b. Halef / Âs b. Vâil, kuru kemik) |

## Hadisler

| Öğe | Durum | Not |
|---|---|---|
| Buhârî 3349 (+6524-6526); Müslim 2860; Tirmizî 2423 (yalın ayak, çıplak, sünnetsiz; İbrâhim ilk giydirilir) | doğru | Metin, numara ve "hutbede" ifadesi tuttu (6525, 6526, Müslim 2860); Tirmizî 2423 "hasen sahih", Elbânî sahih; "yaratıldıkları gibi" ibaresi Tirmizî'de var |
| Buhârî 6527; Müslim 2859 (Âişe: "durum buna dikkat edemeyecekleri kadar ağır") | doğru | Metin tuttu; sahih |
| Tirmizî 2424 (yürüyerek, binekli, yüzü üstü) | doğru | Behz b. Hakîm; Tirmizî "hasen", Elbânî ve Şâkir sahih |
| Buhârî 6523; Müslim 2806 (Enes, kâfir yüzü üstü) | doğru | Metin tuttu |
| Binekli gelenler cümlesi | düzeltildi | "Binekli gelenlerden maksat konuklardır" kesin hüküm gibiydi; "Meryem 19:85'teki konuk ifadesine bakılır" yapıldı (İbn Kesîr vefd'i binekli heyet diye açıklıyor) |
| Buhârî 4712 (+3340); Müslim 194 (şefaat-i uzmâ; meydan, güneş, Âdem'den Îsâ'ya "nefsî nefsî", secde, "ümmetî", sağdaki kapı) | doğru | Metin ve numaralar bağımsız doğrulandı: giriş cümleleri, Âdem'in sözü, Nûh'un dua hakkı, İbrâhim'in üç sözü, Mûsâ'nın öldürdüğü kişi, Îsâ'nın günah anmaması, "geçmiş ve gelecek günahların bağışlandı", Arş'ın altında secde, "başını kaldır, iste verilir, şefaat et kabul edilir", hesapsız girenlerin sağ kapısı hepsi Buhârî 4712 ile birebir. Buhârî 3340 aynı hadisin kısa rivayeti (Âdem, Nûh, sonra Muhammed); doğru. Müslim 194 Ebû Hureyre rivayeti; doğru |
| Nûh / İbrâhim cümlesi ve şema etiketi | düzeltildi | "kullandığı kabul edilecek dua hakkını" ve "üç yalanını" anlaşılmazdı/sertti; hadis metnine uygun açıklandı ("kabul edilmesi kesin dua hakkını kavmi aleyhine kullandığını"; "hadiste 'yalan' diye anılan üç sözünü"); şemadaki "Kavmine ettiği dua hakkı" "Kavmi aleyhine ettiği dua" yapıldı |
| Müslim 2864; Tirmizî 2421 (güneş bir mil; ayak bileği, diz, bel, gem) | doğru | Arapça metin: كعبيه / ركبتيه / حقويه / يلجمه; Süleym b. Âmir'in "mil" notu ve ağzı işaret etme var. Tirmizî 2421 "hasen sahih", Elbânî sahih; "bir veya iki mil" ve "güneş onları eritir" Tirmizî'de |
| Buhârî 4938; Müslim 2862 (ter kulak yarısına kadar) | doğru | İbn Ömer; metin tuttu |
| Buhârî 6532; Müslim 2863 (yetmiş zirâ' ter) | düzeltildi | Buhârî: "yerin içine yetmiş zirâ', gem vurur, kulaklarına kadar"; metindeki "ağızlarına ve kulaklarına" Müslim'le karışmıştı. Müslim 2863'te ise "yetmiş bâ'" ve "ağızlarına veya kulaklarına", râvi Sevr şüphe eder. Metin ve künye buna göre düzeltildi |
| "Rivayetler çelişmez" cümlesi | düzeltildi | Kesin hükümdü; "ters düşmeyecek biçimde, durumlara göre değişen seviyeler olarak anlamak mümkündür" diye yumuşatıldı |
| Müslim 987 (elli bin yıllık gün) | düzeltildi | Numara ve ifade doğru; ama hadis zekât vermeyenlerin azabı bağlamında. Bağlam cümleye eklendi |
| Buhârî 660; Müslim 1031 (gölgedeki yedi kişi) | doğru | Sıralama ve içerik tuttu; Müttefekun aleyh |
| Buhârî 4718 (İbn Ömer, diz üstü, şefaat, makam-ı mahmûd) | düzeltildi | Rivayet İbn Ömer'in kendi sözü (sami'tu İbn Ömer yekûl; Peygamber'e isnad yok). "Anlatıyor" yerine "şöyle dedi"; künyeye "sahâbî sözü olarak rivayet edilir" notu eklendi |
| Tirmizî 3137 (makam-ı mahmûd "şefaattir") | doğru | Ebû Hureyre; Şâkir, Elbânî ve Zübeyr Ali Zâî sahih |
| Ebû Dâvûd 766 (Âişe, duruşun darlığından sığınma) | doğru | Elbânî "hasen sahih", Arnaut "hasen"; metinde "Hasen" yazılı, tutuyor. İbn Kesîr de Ebû Dâvûd'dan aynısını nakleder |
| Buhârî 4779; Müslim 2824 (hiçbir gözün görmediği) | doğru | Kudsî hadis; metin tuttu |
| Kudsî/derece etiketleri | doğru | Sahihayn hadisleri "Sahih (Müttefekun aleyh)"; Tirmizî rivayetleri için Tirmizî ve Elbânî dereceleri tutarlı |

## Âlim ve kitap alıntıları

| Öğe | Durum | Not |
|---|---|---|
| Said Nursî, Onuncu Söz, Dokuzuncu Hakikat: "Evet, en büyük bir ağacın rûh programını…" | doğru | erisale.com sayfasından birebir (s. 125-126 işareti); "Sade söyleyiş" doğru özet |
| Said Nursî: "Elhâsıl: Haşre mâni hiçbir şey yoktur. Muktazî ise, herşeydir." | doğru | Aynı sayfa (127 işaretinden sonra), birebir |
| TDV İslâm Ansiklopedisi, "Şefaat" maddesi alıntısı | doğru | islamansiklopedisi.org.tr/sefaat; cümle birebir. "Âlimler bu noktada ittifak etmiştir" ifadesi maddede var; "ayrıntılarda görüş ayrılığı" da maddede var |
| İbn Kesîr, Mutaffifîn 83:6 alıntısı ("yalın ayak, çıplak, sünnetsiz… güç ve duyuların kaldıramayacağı") | doğru | Kısaltılmış İngilizce baskıdan, çeviri sadık; Arapça baskıda aynı (حفاة عراة غرلا في موقف صعب حرج ضيق ضنك) |
| İbn Kesîr, Meâric 70:4 ("Bu, kıyamet günüdür", İbn Abbas, sağlam isnat) | doğru | Tefsirde "The chain of narration is authentic" |
| İbn Kesîr, Mutaffifîn 83:6: İbn Mes'ûd "kırk yıl", İbn Ömer "yüz yıl" (İbn Cerîr) | doğru | Arapça baskıda: «وعن ابن مسعود يقومون أربعين سنة… وعن ابن عمر يقومون مائة سنة رواهما ابن جرير». Sahâbî sözü olduğu notu doğru |
| İbn Kesîr, Meryem 19:85 (vefd = binekli heyet) | doğru | Tefsirde "A Wafd is a group that arrives while riding" |
| İbn Kesîr, Yâsîn 36:77 (kemik kıssası) | doğru | Mücâhid, İkrime, Urve, Süddî, Katâde: Übey b. Halef; İbn Abbas: Âs b. Vâil |
| Kurtubî, et-Tezkire (Muhâsibî'den nakil: Arş'ın gölgesi / güneşin sıcağı) | doğru | read.shamela.ws/book/30031/265: Muhâsibî, Kitâbü't-Tevehhüm ve'l-Ehvâl'den; "فلا ظل ذلك اليوم إلا ظل عرش الرحمن…". Aynı bâbda (/266) Kurtubî "ولا يصح إسنادهما" der; metindeki not doğru. Bâb 88 başlığı "bu şefaat el-makâmü'l-mahmûddur" doğru |

## Olgusal ve dil düzeltmeleri

| Öğe | Durum | Not |
|---|---|---|
| "milyonlarca kişi" (sahne) | düzeltildi | Rakam verilmişti; "sayısız insan" yapıldı |
| Ahmed rivayeti (müminin beklemesi hafifler) | doğru | Metne delil olarak konmamış; "derecesi tartışmalı" notu var. Kaynak ikinci el (araştırma notu), ayrıntı verilmediği için risk yok |
| Kehf, Bakara kıssaları, "bilimsel bulgu" çerçevesi, "keyfiyetini Allah bilir" ifadeleri | doğru | Ehl-i sünnet çizgisine uygun; tarih verilmemiş, tekfir yok, uydurma tasvir yok |
| Peygamber ve melek resmi | doğru | Şemalarda yalnız isim kutuları ve semboller var |

## HTML sözleşmesi ve SVG

| Öğe | Durum | Not |
|---|---|---|
| Sınıflar, `<script>`, sabit hex renk, dış URL, SVG dışı inline stil | doğru | İzinsiz sınıf yok; script yok; hex renk yok; `http` yok |
| 3 şema (ölü yerin dirilişi, ter seviyeleri, şefaat akışı) | doğru | 390 px açık ve koyu temada PNG alındı ve gözle kontrol edildi; metin taşması/çakışma yok (QA betiği `over: []`, `svgTextTasma: []`); koyu temada okunur. viewBox genişliği 520, metin 16-20 birim |

## Kalan riskler

- Müslim numaralarında fawazahmed0 `arabicnumber` (Abdülbâkî) esas alındı; sunnah.com/dorar.net bu oturumda açılmadı, dereceler API'deki Elbânî/Şâkir/Zübeyr Ali Zâî/Bashar kayıtlarına dayanıyor.
- Ahmed'in "bekleyiş müminlere hafifler" rivayetinin derecesi ikinci elden; metinde delil olarak kullanılmıyor.
- Bölüm uzunluğu (Arapça hariç yaklaşık 3500+ kelime) hedefin üst sınırında; içerik dolgu değil.
