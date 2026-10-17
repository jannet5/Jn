# Denetim raporu: Bölüm 01 (Kur'an "çoğu" derken: veri haritası)

Denetçi: bağımsız. Saat: 14:11-14:25 UTC. Dosyalar: `bolumler/01-veri-haritasi.html`, `veri/analiz.py`, `veri/analiz.json`, `veri/svg/*.svg`, `research/01-veri-haritasi.md`.

## 1. Sayılar

| Öğe | Durum | Not |
|---|---|---|
| `analiz.py` yeniden çalıştırma | doğru | Çıktı `analiz.json` ile birebir aynı, 5 SVG dosyası aynı (md5). HTML'e gömülü SVG'ler dosyalarla aynıydı. |
| 81 ham eşleşme (ekser) | doğru | BAĞIMSIZ sayım: `kuran_tam.json` Arapçası harekesiz normalleştirildi (hareke, tatvîl, elif varyantları, ى→ي). "اكثر" içeren ayet = 81, kümeler yazarın 81'iyle özdeş (82 geçiş; 38 "أكثرهم", 31 "أكثر" vb.). |
| 71 kesîr ayeti | doğru | Bağımsız sayım: "كثير" içeren ayet = 71 (74 geçiş), küme yazarın 71'iyle özdeş. |
| 71 "az" eşleşmesi (قليل) | doğru | Bağımsız sayım 71; tek yanlış eşleşme 35:18 (مثقلة). 70 ayet sınıflı, grafikteki 20+13+9+20+8 = 70. |
| 68 hüküm / 13 başka anlam | doğru | 13 ayetin tamamı elle okundu (karşılaştırma, fiil, konu çokluğu); hepsi yerinde. |
| Genel insan 26 / belirli 40 / belirsiz 2 | doğru (sınıflama okuma) | Toplamlar tutuyor (A 21 + A* 5; B 40 = 23+11+3+3; ? 2). Lafız ölçütü notu dipnota eklendi. |
| Mekkî 59 / Medenî 9 | doğru, yorumu düzeltildi | Sayı API'ye göre doğru. "Biraz daha yaslanıyor" yorumu zayıftı: sure sayısına göre 26/33 (yüzde 79) Mekkî, tüm Kur'an'da 86/114 (yüzde 75); fark neredeyse yok. Metin buna göre yeniden yazıldı. Ra'd 13:1 Medenî sayıldığı için "Mekkî sayılırsa 60/8" notu eklendi. |
| En sık yüklem "bilmez" 27 | doğru | Yüklem x özne tablosu tutarlı (11+1+2+13 = 27). |
| "kesîr": 71 / 29 / 42 | doğru | Kişi dışı 42 ayetin ayrımları örneklemle kontrol edildi. |
| kesîr nitelik 23 / 4 / 2 | DÜZELTİLDİ → 24 olumsuz / 3 olumlu-nötr / 2 karışık | Tutarsız ölçüt: 2:26 ("birçoğunu saptırır, birçoğunu doğru yola iletir", kesîr iki yönde) olumlu-nötr sayılmış, 57:26 ("kimi doğru yolda, birçoğu yoldan çıkmış", kesîr yalnız olumsuz yanda) karışık sayılmıştı. Ölçüt: kesîr kalıbının kendisi hem olumlu hem olumsuz yüklemle geçiyorsa karışık (2:26, 22:18). 57:26 olumsuz oldu. Kod, JSON, SVG, HTML, araştırma notu güncellendi. |
| Genel 9 / belirli 20 (kesîr), Kitap ehli 9, Mekkî 10 / Medenî 19 | doğru | 29 ayetin hepsi elle yeniden okundu (Diyanet metni). |
| Paylaşılan 14 atıf: 12 ekser, 2 kesîr | doğru | 14 ayetin Arapçası tek tek kontrol edildi (5:49, 10:92 kesîr; kalanı أكثر). |

Elle yeniden sınıflama: 81 ayetten rastgele 24 ayet (seed 20261017: 4:12, 7:102, 7:131, 9:69, 10:36, 11:32, 12:38, 12:40, 18:54, 26:103, 26:158, 27:73, 30:6, 30:9, 34:28, 34:35, 34:41, 36:7, 37:71, 39:29, 39:49, 40:59, 40:61, 43:78) Diyanet metni, bağlam ve gerektiğinde İbn Kesîr ile yeniden sınıflandı: 24/24 uyum (18:54 "insan en çok tartışan varlık" sınırda, kapsam dışı kalması savunulabilir). Ayrıca 13 kapsam dışı ayetin hepsi, A*/?/orta-düşük güvenli bütün ayetler ve 29 kesîr hükmü ayetinin tamamı gözden geçirildi; tek tutarsızlık yukarıdaki kesîr nitelik sorunu.

## 2. SVG grafikleri

| Öğe | Durum | Not |
|---|---|---|
| Şema 1 (sure haritası) | doğru | 114 hücrenin dolgu sınıfı (yok/1/2-3/4+) ve altın çerçeve (28 Medenî sure) veriyle program yoluyla karşılaştırıldı: 0 hata. aria-label'da yalnızca 6 sure sayılıyordu, üç ayetli 5 sureden 2'si anılıp 3'ü atlanıyordu; tamamı yazıldı. |
| Şema 2 (yüklem) | doğru | 17,93 birim/ayet; her segment sayıyla orantılı, A 21 / A* 5 / B 40 / ? 2 toplamları tutuyor. |
| Şema 3 (iki kalıp) | düzeltildi | Panel 1 6,37 birim/ayet, panel 2-4 7,59 birim/ayet ölçeğindeydi (aynı sayı farklı uzunluk). Dört panel tek ölçeğe (6,37) alındı; etiket eşiği 18 birime indirildi; açıklamaya "aynı sayı, aynı uzunluk" eklendi. Etiketler ve toplamlar tutuyor (81 = 68+13, 71 = 29+42, 68 = 26+40+2, 29 = 9+20 = 24+2+3, 68 = 59+9, 29 = 10+19). |
| Şema 4 ("az") | doğru | 24,2 birim/ayet; 20/13/9/20/8. |
| Şema 5 (14 atıf) | doğru | Atıf adları ve gerçek numaralar tutuyor. |
| Mobil (390px), açık ve koyu tema | doğru | PNG'ler açıldı (`scratchpad/shots/c-01`): taşma/çakışma yok, `over` ve `svgTextTasma` boş, koyu temada tüm metin okunuyor. Koyu temada "kalan başka anlamda" çubuğu düşük kontrastlı ama etiketi okunuyor. |

## 3. Sözlük alıntıları (arabiclexicon.hawramani.com/كثر, sayfa açılıp metin arandı)

| Öğe | Durum | Not |
|---|---|---|
| Lisânü'l-Arab: والكُثْرُ معظم الشيء وأَكْثَرُه | doğru | Sayfada aynen var (bir hadis bağlamında "الكثر: الكثير كالقل"). Kaynak etiketi "kesr maddesi" yerine ك ث ر maddesi yapıldı. |
| Sıhâh: الكثرة: نقيض القلة | doğru | Aynen var. |
| Lane: "The greater, or greatest, or main, part, of a thing; the most thereof." | doğru | كُثْرٌ maddesinde var (Lane, kaynak olarak K, yani Kâmûs, gösteriyor). |
| Müfredât: وليست الكثرة إشارة إلى العدد فقط بل إلى الفضل | düzeltildi | Alıntı doğru, ama bağlam atlanmıştı: Râgıb bunu Sâd 38:51'deki cennet meyvesi ("dünya yiyeceklerine göre çok sayılmış") için söylüyor ve aynı yerde kesret/kıllet'in "ayrık nicelikte, sayılarda" kullanıldığını belirtiyor. "الفضل" karşılığı "fazlalık/üstünlük (fazl)" yapıldı, bağlam eklendi; "Râgıb destekler" sonucu "bir ipucu, kesin kanıt değil" diye yumuşatıldı. Sayılan örnek ayetler (23:70, 2:109, 2:249) sayfada var. |

## 4. Sonuç cümleleri / abartı

| Öğe | Durum | Not |
|---|---|---|
| "Tablo daha az sert" | düzeltildi | "iman etmez" (14) ikinci en sık yüklem; sertlik karşılaştırması çıkarıldı, "iman etmez de küçük bir pay değil" denildi. Özet maddesi buna göre değişti. |
| "En çok söylediği şey görmediği, fark etmediği, şükretmediği" | düzeltildi | Sayıyla bilmez+şükretmez+akletmez = 37 ayet; "iman etmez" 14 ayrıca anıldı. |
| "Ekser'de olumlu-nötr sıfır" | düzeltildi | Yalnız 68 hüküm ayeti için doğru; "ekser kalıbı hep olumsuz" anlamına gelmediği, kapsam dışındaki 17:6'nın olumlu olduğu eklendi. SVG aria-label "68 hüküm ayetinin hepsi olumsuz yüklemli". |
| "Hiçbiri zaman dilimi, ülke, parti, mezhep adı vermez" | düzeltildi | 12:21'de "Mısır", 41:4'te "Arap(ça)" geçiyor; "çoğu"yu bir çağa/ülkeye/partiye/mezhebe bağlamaz diye daraltıldı. |
| "Kur'an insanın çoğunlukla..." (Kanıtlar kutusu) | düzeltildi | 68 ayetin 40'ı belirli topluluk; "kimi genel insan, kimi belirli topluluk" eklendi. |
| "az"ın dönüşü çoğu zaman güç dengesi | düzeltildi | 70 "az"ın 50'si bedel/zaman/miktar/derece; ifade buna göre değişti. |
| 2:249 "doğru yanlışla ilgisiz güç dengesi" | düzeltildi | Azlık tarafı müminlerdir; "üstünlüğü sayı değil Allah'ın izni ve sabır belirler" yazıldı. |
| Vâkıa notu (56:13-14, 39-40) | düzeltildi | Arapçada 56:14 "sonrakilerden AZ" (qalîl), 56:40 "sonrakilerden de bir kalabalık"; Diyanet ikisini de "bir kısmı" yapıyor. Not eksik dengeliydi; Arapçası ve Diyanet farkı yazıldı, 56:14'ün "az" sayımında olduğu belirtildi. |
| Mekkî dipnotu | düzeltildi | Sure düzeyi, karışık sureler ve Ra'd 13:1 etkisi yazıldı. |
| "Genel / belirli" ayrımı | düzeltildi | Lafız ölçütüne dayandığı, müfessirlerin bazı "insanlar"ı muhataba bağlayabileceği dipnota eklendi. |
| "İki kez kontrol ettik" | düzeltildi | "Ayrı bir kodla yeniden saydık" (gerçekten yapıldı). |

## 5. Ayetler, alıntılar, sözleşme, Türkçe

| Öğe | Durum | Not |
|---|---|---|
| 5 `blockquote.ayet` (18:34, 12:21, 22:18, 34:13, 11:40) | doğru | `ayet_dogrula.py`: 5 blok, 0 uyarı. Kehf 18:34'teki "arkadaşiyle" yazımı Diyanet kaynağındaki hata; blok birebir olduğu için dokunulmadı. |
| Metin içi Diyanet alıntıları | düzeltildi | Tırnak içindeki birkaç alıntı Diyanet metniyle birebir değildi, düzeltildi: 2:249, 27:15 ("mümin"), 7:17, 34:20, 12:21, 7:86, 26:221-222. Tırnaklı serbest ifadeler tırnaksız yazıldı: 26:54 (Diyanet: "döküntü azınlıklardır"), 8:26, 2:83/4:66/5:13, 13 kapsam dışı örneği. Nakarat alıntısı yalnız 26:103'e atfedildi (26:8 ve 26:174'te Diyanet sözcükleri farklı; Arapça sekizinde aynı, doğrulandı). 26:223, 56:13/56:39, 5:49, 10:92, 38:24, 41:4, 44:39, 49:4, 43:78 metinleri uyuyor. |
| İbn Kesîr atıfları | doğru | api.quran.com tefsir 169: Katâde (2:88 "ancak azı inanır"), 4:46 ("faydalı iman yok" okuması), 49:4 (odaların arkasından seslenen bedevîler), 26:223 ("kâhinler ve diğer günahkâr yalancılar"), 43:78 (cehennem sahnesi). |
| HTML sözleşmesi | doğru | İzinsiz sınıf yok, `<script>` yok, dış URL yok, SVG dışında inline stil yok, sabit hex renk yok, etiketler dengeli. Tek bölüm `section#b-01`, `span.no` aynen, `div.ozetle` 5 madde. Uzunluk 2807 kelime (sınır 3000). |
| Türkçe | düzeltildi | "nedeniyle birlikte duruyor" → "gerekçesiyle birlikte duruyor"; Râgıb ve Mekkî paragrafları sadeleştirildi. |
| Hadis | yok | Bu bölümde hadis geçmiyor. |

## Kalan riskler

- Sınıflama (özellikle genel/belirli, 26'ya 40) yazarın okumasıdır; 5:49 ve 12:106 gibi ayetlerde müfessirler "insanlar"ı farklı daraltabilir. Metin bunu belirtiyor.
- 28:58 ("pek az kimseler oturabilmiştir") "az kişi" mi "az zaman" mı belli değil; ikili okuma olarak işaretli.
- Mekkî/Medenî yalnız sure düzeyinde, api.quran.com'a göre.
- Lane, Sıhâh, Lisân, Müfredât metinleri tek aracı siteden (hawramani) okundu; asıl baskılara karşı kontrol edilmedi.
- Koyu temada "kalan başka anlamda" çubuğunun kontrastı düşük (okunuyor).
