# 02 — Dünya: Uzun Yolculukta Kısa Bir Durak — Denetim Raporu

Denetim: bağımsız denetçi. Betik: `araclar/ayet_dogrula.py` (12 ayet bloğu, 0 uyarı; Mü'minûn düzeltmesinden sonra yeniden çalıştırıldı). Derleme ve şema PNG'leri: `python3 build.py` + `qa-bolum.js` (açık/koyu, 390px): taşma yok, hata yok.

## 1. Ayetler

| Öğe | Durum | Not |
|---|---|---|
| Hadîd 57:20 (blok) | doğru | Arapça + Diyanet meali API ile birebir ("hoşnudluğu" Diyanet kaydındaki yazım) |
| Hac 22:47 (blok) | doğru | birebir |
| Meâric 70:4 (blok) | doğru | birebir; İbn Kesîr (quran.com tefsir 169): "elli bin yıllık gün = kıyamet günü" (İbn Abbas, isnadı sahih; İkrime, Dahhâk, İbn Zeyd) — metindeki ifade doğru |
| A'lâ 87:16-17 (blok) | doğru | birebir |
| Mü'minûn 23:112-114 (blok) | düzeltildi | Türkçe kısım 115. ayetin meâlini de içeriyordu ama Arapça 115 yoktu, künye "112-114" idi. Arapça 115 API'den eklendi, künye "23:112-115" yapıldı. API'deki Diyanet kaydı 114 ve 115'i birleşik veriyor ve başta "Allah'" yazım hatası var; sayfada "Allah:" olarak düzeltilmiş (kabul) |
| Nâzi'ât 79:46 (blok) | doğru | birebir |
| Kasas 28:77 (blok) | düzeltildi | Meal API'de 76-77 birleşik; 77'ye ait kısım "…" ile verilmiş, doğru. Künyedeki "(ayetin 77. ayete ait kısmı)" notu çıkarıldı |
| A'râf 7:32 (blok) | doğru | birebir |
| Neml 27:40 (blok) | doğru | birebir. Metindeki "tahtı yanında bulunca" ifadesi Süleyman'ın kendi tahtı gibi okunuyordu; ayette Saba melikesinin tahtıdır — düzeltildi |
| Asr 103:1-3 (blok) | doğru | birebir |
| Tekâsür 102:1-2 (blok) | doğru | API iki ayeti aynı metinle veriyor; Diyanet metni birebir |
| Kehf 18:46 (blok) | doğru | birebir |
| Yûnus 10:45, Rûm 30:55, Ahkâf 46:35 (metin içi) | doğru | tırnak içindeki parçalar Diyanet metninde aynen var |
| Tekâsür 102:3, 102:8; Âl-i İmrân 3:185; Secde 32:17 (metin içi) | doğru | aynen |
| Âl-i İmrân 3:15 (metin içi) | düzeltildi | "daha iyi ve daha bâkî" ifadesi 3:15'te değil A'lâ 87:17'dedir; cümle yeniden kuruldu (3:15 = cennetlikler orada temelli kalacak; "daha iyi ve daha bakidir" = A'lâ 87:17, hemen ardındaki blok) |
| Mü'minûn 23:114-115, Kasas 28:77, A'râf 7:32 (özet/giriş cümleleri) | düzeltildi | Tırnak içindeki bazı ifadeler Diyanet metninden sapıyordu ("ziyneti ... kim haram kıldı", "dünyadaki payını unutma", "bir gün ya da günün bir kısmı", "Pek az kaldınız. Keşke…"); hepsi meal metnine uyduruldu |

## 2. Hadisler (fawazahmed0 hadith-api, ara/eng baskıları; numaralar arabicnumber alanıyla)

| Hadis | Durum | Not |
|---|---|---|
| Tirmizî 3550 (60-70 yaş) | doğru | Tirmizî: hasen garîb; Şâkir, Elbânî, Arnaut, Zübeyr Ali Zai: hasen — künye doğru |
| Buhârî 6416 (garip/yolcu) | doğru | Arapça ve İbn Ömer'in sözü ayrımı doğru (son cümle İbn Ömer'in) |
| Buhârî 6412 (sağlık ve boş vakit) | doğru | İbn Abbas |
| Müslim 2858 (parmak ve deniz) | doğru | arabicnumber 2858 (Kitâbü'l-Cennet); metin ve râvi (el-Müstevrid b. Şeddâd) tutuyor. sunnah.com 403 verdi, API ile doğrulandı |
| Müslim 2956 (dünya mü'minin zindanı) | doğru | arabicnumber 2956. Nevevî ve Sindî şerhi hadeethenc.com/en/browse/hadith/3584 sayfasında açıldı: ifadeler tutuyor |
| Tirmizî 2377 (hasır, ağaç gölgesi) | doğru | Elbânî sahih, Şâkir sahih, Arnaut hasen sahih, Zübeyr Ali Zai hasen; künye doğru |
| Tirmizî 3701 (Hz. Osman, bin dinar) | doğru, ifade düzeltildi | Elbânî/Şâkir/Arnaut hasen. Metin "sahih bir örnek" diyordu; "hasen derecesinde bir rivayet" yapıldı |
| Tirmizî 2325 (dört kişi) | doğru | Tirmizî hasen sahih; Elbânî ve Şâkir sahih; Zübeyr Ali Zai zayıf — ihtilaf künyede zaten belirtilmiş |
| "Beş şeyi beşten önce" (dikkat notu) | düzeltildi | Hâkim 7846, Beyhakî Şuab 10248, İbn Ebi'd-Dünyâ Kasrü'l-emel 111 ve Elbânî'nin sahih demesi (Sahîhu't-Terğîb 3355) arama sonucuyla doğrulandı. Notta geçen "Ebû İshak el-Huveynî illetli dedi" ve "mürsel" iddiaları birincil kaynakla doğrulanamadı; Huveynî adı çıkarıldı, "senedini birincil kaynaktan inceleyemedik, tavsiye olarak aktarıyoruz" yazıldı. Hadis delili olarak kullanılmıyor (zaten öyleydi) |
| "Dünya ahiretin tarlasıdır" (dikkat notu) | doğru | Gazâlî'nin cümlesi İhyâ'da (Kitâbü'l-ilm) aynen var: "فإن الدنيا مزرعة الآخرة ولا يتم الدين إلا بالدنيا"; hadis olarak verilmiyor, not yerinde |

## 3. Âlim/kitap alıntıları

| Alıntı | Durum | Not |
|---|---|---|
| Hz. Ali sözü (Buhârî, Rikak, "fi'l-emel ve tûlihî" babı, muallak) | doğru | ar.wikisource Sahîhu'l-Buhârî Kitâbü'r-Rikak, bab 4: söz bab başlığında "وقال علي بن أبي طالب: ارتحلت الدنيا مدبرة…" diye isnadsız (muallak) geçer; 6417'den önce. Hadis değil, sahabe sözü (mevkuf) diye zaten etiketli. İbn Hacer'in Fethu'l-Bârî'de senedini bağladığı bir arama özetinden okundu; Fethu'l-Bârî sayfası doğrudan açılamadı, bu yüzden metinde sened hakkında hüküm verilmedi (yalnız "muallak" deniyor). Giriş cümlesi ("ile bilinen") sadeleştirildi |
| Hasan-ı Basrî — "Dünya göç yurdudur…" (Ömer b. Abdülaziz'e mektup) | doğru, künye düzeltildi | İhyâ (Irâkî tahkîki, İbn Hazm baskısı, archive.org) metninde aynen: «كتب الحسن البصري إلى عمر بن عبدالعزيز: أما بعد فإن الدنيا دار ظعن ليست بدار إقامة… فإن الزاد منها تركها». Basrî'nin kendi eseri/senedi açılmadı; Gazâlî senedsiz aktarıyor. Künyeye "senedsiz aktarım" eklendi, giriş cümlesi "Gazâlî aktarır" diye yeniden kuruldu |
| Hasan-ı Basrî — "Allah, dünyası kendilerinde emanet olan… kavimlere rahmet etsin" | doğru, künye düzeltildi | İhyâ Zemmü'd-dünyâ'da «وقال الحسن: رحم الله أقواماً كانت الدنيا عندهم وديعة فأدّوها إلى من ائتمنهم عليها ثم راحوا خفافاً» var ("el-Hasan", nisbesiz; aynı bölümde mektup "el-Hasan el-Basrî" adıyla geçiyor). Aynı "senedsiz aktarım" notu eklendi |
| İbn Kayyım — "geçiş yurdu, kalış yurdu değil…" (Miftâhu Dâri's-Saâde) | doğru | archive.org djvu.txt'te aynen: «علموا أن الدنيا دار ممرٍّ لا دار مقر ومنزل عبور لا مقعد حبور… كراكب قال تحت ظل شجرة ثم راح عنها وتركها». Öznesi "Allah adına hakkı ayakta tutan, Peygamber'in ümmetindeki vekiller" (القائمون لله بحجته خلفاء نبيه في أمته); giriş cümlesi buna göre düzeltildi |
| İbn Kayyım — parmak ve deniz benzetmesi | doğru, giriş düzeltildi | Metinde aynen: «لا نسبة لهذا النعيم والعذاب العاجل إليه إلا كما يدخل الرجل أصبعه في اليم ثم ينزعها فالذي تعلق بها منه هو كالدنيا بالنسبة إلى الآخرة». Metinde Müslim 2858'e açık atıf yok (ahirete kesin inanan kişinin hâli bahsinde); "bu hadisi kullanır" ifadesi "aynı görüntüyü kullanır" yapıldı |
| Gazâlî — İhyâ, Zemmü'd-dünyâ ("dünyan ve ahiretin kalbinin iki hâli…") | doğru | «دنياك وآخرتك عبارة عن حالتين من أحوال قلبك…» aynen. Üç kısım kutuları gözden geçirildi: 1. kısım (ilim + halis ibadet) doğru; 2. kısım kutusundaki "israf ve gösteriş" Gazâlî'de yok (günah lezzetleri + ihtiyaçtan fazla refah/lüks) — düzeltildi; 3. kısım kutusundaki "barınak" Gazâlî'de yok (kuvvet kadar yiyecek, kaba bir gömlek, hayat ve sağlık için zorunlu şeyler; niyet ilim/ibadete yardımsa dünyadan sayılmaz, yoksa 2. kısma katılır) — düzeltildi |
| Said Nursî — Sözler, Onyedinci Söz, Birinci Makam sonu (kitap, mezraa, seyrangâh, misafirhane) | doğru | SOZLER-KITAB_djvu.txt (archive.org) metniyle aynen karşılaştırıldı; "s. 203-204" tutuyor |
| Said Nursî — "Dünya ise, bütün şaşaasıyla Âhirete nisbeten bir zindan hükmündedir" | doğru | Aynı yerde dördüncü yüzde, s. 204 |
| Said Nursî — Onuncu Söz (manevra meydanı, misafirhane, "her gün bir kafile gelir…") | düzeltildi | "Sultanın garip sanatlarının meşheri" tırnak içinde bir açıklama olarak verilmişti; aslı "Sanayi-i Garibe-i Sultaniyyenin meşheri": tırnak içine aslı, parantez içine Türkçe açıklaması alındı. Diğer parçalar aynen |

## 4. Olgusal iddialar ve hesaplar (yeniden hesaplandı)

| Öğe | Durum | Not |
|---|---|---|
| 80 yıl ≈ 29.220 gün | doğru | 80 × 365,25 = 29.220 |
| 80 yıl ≈ 4.160 hafta | düzeltildi | 29.220 / 7 = 4.174; 4.160 (80 × 52) yaklaşık ama tutarsız; "≈ 4.170 hafta" yapıldı (metin ve tablo) |
| ≈ 2,5 milyar saniye | doğru | 29.220 × 86.400 = 2,525 milyar |
| 8 saat uyku → ≈ 27 yıl | doğru | 80/3 = 26,7 |
| 1.000 / 80 = 12,5 ömür | doğru | |
| 50.000 / 80 = 625 ömür | doğru | |
| Bin yıllık gün = 24 saat ise 80 yıl ≈ 1 sa 55 dk | doğru | 80/1000 × 24 sa = 1,92 sa = 1 sa 55,2 dk |
| Elli bin yıllık gün = 24 saat ise 80 yıl ≈ 2 dk 18 sn | doğru | 80/50000 × 1440 dk = 2,304 dk = 2 dk 18,2 sn |
| Logaritmik şema ölçeği | doğru | Çentik aralığı 79,2 birim/dekat; çubuk uzunlukları: log10(80)=1,903 → 150,7; log10(1000)=3 → 237,6; log10(50000)=4,699 → 372,2 — şemadaki değerlerle birebir. Başlıktaki "doğrusal çizsek 625'te biri" doğru |
| Küresel yaşam beklentisi 2019 ≈ 73 | doğru, ifade sadeleştirildi | WHO World Health Statistics 2021: 73,3 (2019); sonraki baskılarda 73,1'e revize. "(73,3)" parantezi çıkarıldı, "yaklaşık 73" kaldı |
| "Meâric 70:4 / Hac 22:47 farklı bağlam, dönüşüm formülü değil" uyarısı | doğru | Doğru ve yeterince belirgin |
| Berzah/kıyamet süresi gayb notu | doğru | Şemada berzah çizilmemiş |

## 5. HTML sözleşmesi

Sorun yok: `<script>` yok, dış URL yok, sabit hex renk yok, kapanmayan etiket yok (ayrıştırıcıyla denendi), yalnız izinli sınıflar (alim, ar, ayet, bolum, bolum-baslik, detay, giris, hadis, kutu, kutular, no, not [dikkat/dusun/umut], ozet, ozetle, sahne, sahne-etiket, sema, tr).

## 6. SVG görsel denetimi

| Şema | Durum | Not |
|---|---|---|
| 1 — Seksen nokta | doğru | Açık/koyu temada okunur, çakışma yok |
| 2 — Logaritmik ölçek | düzeltildi | Üçüncü çubuk (Meâric 70:4) `var(--night)` ile dolu idi; koyu temada zeminle (`--surface`) neredeyse aynı renkte, görünmüyordu. `var(--sky)` yapıldı; iki temada da görünür. Yeniden PNG alındı, doğrulandı |
| 3 — Parmak ve deniz | doğru | Koyu temada parmak gövdesi koyu ama dış çizgisi görünür; etiketler okunur |

`qa-bolum.js` sonucu: figs 3, `over` boş, `svgTextTasma` boş, hata yok (açık ve koyu).

## 7. Türkçe kalite

Küçük düzeltmeler: "ile bilinen şu sözde" → "ait şu sözde"; "ebedî olanın hesabıyla yapmak" → "ebedî olanı gözeterek yapmak"; "Sonsuzluğun yanında seksen yıl nokta bile değil" cümlesi alıntı gibi okunduğu için sadeleştirildi; "Samimi bir iyiliğin karşılığı ebedî yurdun kapısıdır" → "Allah'ın lütfuyla … kapısını aralayabilir" (Ehl-i sünnet çizgisi: cennete girmek Allah'ın lütfuyladır). Dil genel olarak sade ve akıcı; baştan yazılmadı.

## Kalan riskler
- Hz. Ali sözünün İbn Hacer tarafından bağlanan senedi birincil kaynaktan açılamadı (yalnız "muallak, sahabe sözü" deniyor; sened hükmü verilmiyor).
- Hasan-ı Basrî sözleri Gazâlî'nin senedsiz aktarımına dayanıyor; Basrî'nin kendi eserinden doğrulanamadı (künyede belirtildi).
- "Beş şeyi beşten önce" için Elbânî'nin derecesi ikincil arama sonucuyla doğrulandı; senedi birincil kaynaktan incelenmedi (hadis delili olarak kullanılmıyor).
- Meâric 70:4'ün "kıyamet günü" yorumu İbn Kesîr'in tercih ettiği yorumdur; metin bunu "anlatmışlardır" diye veriyor, kesin takvim iddiası yok.
