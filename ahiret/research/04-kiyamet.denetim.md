# 04 — Kıyametin Kopuşu: denetim raporu

Denetlenen dosya: `bolumler/04-kiyamet.html`. Kaynaklar: api.quran.com (Arapça uthmani + Diyanet id 77), fawazahmed0/hadith-api (eng/ara-bukhari, eng/ara-muslim, eng-tirmidhi; Müslim için `arabicnumber`), Quran.com Tafsir Ibn Kathir (id 169), TDV İslâm Ansiklopedisi "Sûr" (WebFetch), IslamQA 49009 (WebFetch), ar.wikisource el-Bidâye (WebFetch), Nursî Haşir Risalesi PDF (pdftotext). Otomatik betik: `araclar/ayet_dogrula.py` → 18 ayet bloğu, 0 uyarı. sunnah.com/dorar.net açılmadı; dereceler fawaz API'deki Şâkir/Elbânî/Zübeyr Alî Zâî alanlarından ve Tirmizî'nin kendi hükmünden alındı.

## Ayet blokları (18) — Arapça + Diyanet meali API ile birebir

| Öğe | Durum | Not |
|---|---|---|
| Yâsîn 36:49-50, 36:51-53 | doğru | Arapça + Türkçe birebir |
| Zümer 39:68; Neml 27:87, 27:88, 27:89 | doğru | birebir |
| Tekvîr 81:1-2, 81:4-6 | doğru | birebir |
| Nebe' 78:19-20; Kâri'a 101:4-5 | doğru | birebir |
| Zilzâl 99:1-5; Tâhâ 20:105-108; Hac 22:1-2 | doğru | birebir |
| Enbiyâ 21:103, 21:104; Fussılet 41:40; Nâzi'ât 79:13-14 | doğru | birebir |
| İbrâhîm 14:48 | düzeltildi | Diyanet mealinde 14:47 ve 14:48 tek cümlede birleşik (API iki ayet için aynı metni veriyor); Arapça yalnız 14:48 idi. 14:47 Arapçası eklendi, atıf "14:47-48" yapıldı |

## Metin içi ayet atıfları

| Öğe | Durum | Not |
|---|---|---|
| Mürselât 77:8 ("ışığı giderildiği") | düzeltildi | tırnak içi ifade Diyanet metnine çekildi ("ışıkları giderilir" uydurma kalıptı) |
| İnşikâk 84:1-2, 84:3-5 | düzeltildi | tırnaklar Diyanet metnine uyduruldu ("boyun eğdiği", "boşaldığı") |
| Tûr 52:10, Tâhâ 20:105, Müzzemmil 73:14, Meâric 70:9 | düzeltildi | "yürüdükçe yürüyeceği", "ufalayıp savuracak", "yumuşak kum yığını haline gelir", "atılmış pamuğa döner" (eski "(yüne)" eki kaldırıldı) |
| Nâzi'ât 79:10-11 | düzeltildi | alıntıya sure:ayet atfı eklendi |
| İnfitâr 82:1-5, Hâkka 69:13-16, Meâric 70:8 ve 70:10, Müzzemmil 73:17, Tekvîr 81:14, İnşikâk 84:6, Kâri'a 101:1-3 | doğru | Diyanet metniyle uyumlu |
| Şema 2 alt yazıları (81:1, 81:2, 82:1-3, 69:16, 78:20, 101:5, 81:6, 99:1-2, 84:3-4, 14:48, 20:106-107) | doğru | sure/ayet numaraları tutuyor |
| "el-fezau'l-ekber" (21:103) açıklaması | düzeltildi | "bunların da ötesindeki korku" iddiası kaynaksızdı, çıkarıldı; ayetin kendi müjdesine indirgendi |
| Hac 22:1-2 sahnesi | düzeltildi | İbn Kesîr ayeti Âdem'e cehennemlik ayrımı hadisiyle anar (mahşer bağlamı); kopuş anıyla sınırlı olmadığı notu eklendi |

## Hadisler

| Öğe | Durum | Not |
|---|---|---|
| Müslim 8 (Ömer) / Buhârî 50 (Ebû Hureyre), Cibril hadisi | doğru | metin Müslim 8'e uyuyor; "Kıyamet Allah'tan başkasının bilmediği beş şeyden biri" Buhârî 50'de var. Sahih (Şeyhayn) |
| Müslim 2901 (Huzeyfe b. Esîd), on alamet | doğru | metin ve numara (arabicnumber 2901.01) tutuyor. Sahih |
| Buhârî 6506, güneşin batıdan doğması + kumaş/süt/havuz/lokma | doğru | metin tutuyor; Buhârî 4635 ve 7121'de benzer metin var. Sahih |
| Sahnedeki "bir çoban ... havuz" | düzeltildi | hadiste "bir adam"; çoban eklenmişti |
| Buhârî 7121'deki küçük alametler listesi | doğru | ilim kalkması, deprem, zaman, fitne, herc, mal çokluğu, yüksek binalarda yarış hepsi metinde |
| Tirmizî 2430 ("Sûr nedir?") | doğru | Tirmizî: hasen-sahih; Şâkir/Elbânî: sahih; Zübeyr Alî Zâî: isnadı sahih |
| Tirmizî 2431 (Sûr sahibi, Hasbünallâh) | düzeltildi | metin doğru; derece satırı netleştirildi: Tirmizî hasen; Elbânî ve Şâkir sahih; Zübeyr Alî Zâî zayıf (muhalefet adıyla yazıldı) |
| Müslim 2955 + Buhârî 4814, iki üfürüş arası kırk | düzeltildi | HTML metni Müslim 2955'e uyuyor (gün-ay-yıl sırası, "su indirir, sebze gibi biter"). Buhârî 4814'te sıra gün-yıl-ay ve su ibaresi yok; künye "Müslim 2955 (kırk bölümü Buhârî 4814'te de geçer)" yapıldı. Sahih |
| Buhârî 3200 | düzeltildi | "(ışıksız bırakılır)" eki çıkarıldı (hadiste yok, çevirmen notu); İbn Kesîr bu hadisi "yükevverân" olarak nakleder |
| Buhârî 2411, 3398, 3408; Müslim 2373 ("ilk ayılan ben olurum") | doğru | numaralar ve metin tutuyor; Müslim 2373'te açıkça "Sûr üfürülünce... istisna" bağlamında. Sahih |
| Müslim 2940 ("çocukları ihtiyarlatan gün") | doğru | ifade hadisin sonunda mevcut. Sahih |

## Âlim / kitap alıntıları

| Öğe | Durum | Not |
|---|---|---|
| İbn Kesîr, Tekvîr 81:1 ("tekvîr = katlamak, sarığın dürülmesi") | doğru | Quran.com Tafsir Ibn Kathir (169) metniyle uyuyor; İngilizceden çeviri olduğu belirtilmiş |
| İbn Kesîr, Neml 27:87 (istisna = şehitler; Sûr'u İsrâfîl üfürür) | doğru | tefsirde var |
| İbn Kesîr, deve/vahşi hayvan açıklaması | düzeltildi | develer için "sahipleri ihmal eder" doğru; vahşiler için "birbirine karışmaları" yalnız Übey b. Ka'b rivayeti, İbn Abbâs'tan "her şey, sineğe kadar toplanır" da var; cümle iki nakli de yansıtacak biçimde yazıldı |
| İbn Kesîr, el-Bidâye: olayların üfürüşlere göre yeri | düzeltildi | "bunların ilk üfürüşten (sa'k) sonra olduğunu söyler" genellemesi yanlıştı. Metinde yalnız gök yarılması/yıldız dağılması "الظاهر والله أعلم" ile sa'k'tan sonraya konuyor; yer sarsıntısı fezâ ile sa'k arasına konuyor. Cümle buna göre yeniden yazıldı |
| İbn Kesîr, el-Bidâye: üç nefha + "mâ beynen-nefhateyn erbaûn" | doğru | wikisource sayfasında var |
| TDV İA "Sûr": İsrâfîl genel kabul; mahiyet belirsiz; çoğunluk iki; Matürîdî tek; üç (uzun Ebû Hureyre hadisi); Kâdî İyâz-İbn Hazm dört | doğru | WebFetch ile aynen doğrulandı |
| "Üç üfürüş diyenler farklı ayetleri birleştirmiş" | çıkarıldı | kaynaklarda doğrulanamadı; yalnız "uzun Ebû Hureyre hadisine dayanır" bırakıldı |
| Uzun "Sûr hadisi" zayıf/muztarib (İbn Hacer, Fethu'l-Bârî 11/369) | doğru | IslamQA 49009'da aynen var; metinde "ikincil kaynak aktarımıyla" diye zaten belirtilmiş |
| Nevevî / Ebû Hureyre "ebeytü" | doğru | IslamQA'da var (metinde ayrıca Nevevî'ye atıf yok, sorun yok) |
| Nursî, Onuncu Söz Zeyl, Dördüncü Mes'ele (seyyare/kuyruklu yıldız, saray) | düzeltildi | alıntı PDF'te harfi harfine var. Künye "Zeylin Üçüncü Parçası, Dördüncü Mes'ele" olarak düzeltildi; "saray yıkılış-inşa" çerçevesi abartılıydı, "misal, tefsir değil" notu korunarak yeniden kuruldu |
| Nursî, Üçüncü Mes'ele ("hikmetten ziyade kudret ve rahmet...") | doğru | PDF'te birebir; künye düzeltildi |
| Nursî, Oniki Suret ("meydan-ı imtihanda binalar muvakkattırlar...") | doğru | PDF'te birebir |

## Olgusal / kelâm / sözleşme / dil

| Öğe | Durum | Not |
|---|---|---|
| Tarih verme, uydurma rivayet, tekfir, resim | doğru | tarih yok; Peygamber/melek tasviri yok; uzun Sûr hadisi ayrıntıları alınmamış; Elmalılı, Kurtubî, Gazâlî doğrudan alıntılanmamış |
| Bilim notu | doğru | "delil değil" çerçevesinde |
| "Pervane... doğru yolu bilmediği için" | düzeltildi | kaynaksız yorum çıkarıldı |
| "Yeniden yaratma ilk yaratmadan daha zor değildir (21:104)" | düzeltildi | ayetin kendi ifadesine ("yaratmaya ilk başladığımız gibi") bağlandı |
| "sûrenin" (ayet kastı) | düzeltildi | "ayetin" |
| "Hâlbuki ... dehşetli idi" | düzeltildi | "Oysa ... dehşetliydi" |
| "Sûr" / Diyanet "Sura" | düzeltildi | okurun karışmaması için bir parantez eklendi |
| HTML sözleşmesi | doğru | izinsiz sınıf, script, dış URL, sabit hex, kapanmayan etiket yok; yalnız izinli sınıflar |
| SVG denetimi (3 şema, 390px, açık + koyu) | düzeltildi | taşma/kesilme yok. Şema 2 ve 3'te koyu sahne zemini üstünde açık temada soluk kalan `var(--gold)`/`var(--ink-soft)`/`var(--sky)` metin ve çizimler `var(--gold-fill)` / `var(--on-night)` yapıldı; PNG'ler yeniden alınıp gözle doğrulandı |

## Kalan riskler

- Tirmizî 2430/2431 dereceleri sunnah.com/dorar.net yerine fawaz API'deki hüküm alanlarından alındı; Tirmizî 2431'de Zübeyr Alî Zâî zayıf diyor (metinde yazılı).
- İbn Kesîr alıntıları Quran.com'daki özet İngilizce tefsirden; Arapça aslıyla karşılaştırılmadı.
- Uzun Sûr hadisinin zayıflığı İbn Hacer'den ikincil kaynakla aktarılıyor (Fethu'l-Bârî sayfası açılmadı).
- Bölüm şu an yaklaşık 3840 kelime (hedefin biraz üstünde), içerik dolgu değil.
