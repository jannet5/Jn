# 04 — Cemaat, İcmâ ve Şûrâ — Denetim raporu

Denetçi: bağımsız denetçi (2026-10-17). Dosya: `bolumler/04-cemaat-icma-sura.html`. Yöntem: `araclar/ayet_dogrula.py` (6 blok, 0 uyarı; düzeltmelerden sonra yeniden: 0 uyarı), `veri/kuran_tam.json` ile metin içi atıfların elle karşılaştırılması, fawazahmed0 hadith-api (ara-/eng- Buhârî, Müslim, Tirmizî, Ebû Dâvûd, İbn Mâce; `grades` alanı), TDV İslâm Ansiklopedisi "İcmâ" ve "Şûra" maddelerinin tam metni (curl), Gazâlî el-Müstasfâ (islamweb), Şâfiî er-Risâle (Şâmile), İbn Hacer Fethu'l-Bârî 13/37 (Şâmile), Elbânî ses kaydı dökümü (al-albany.com), Dorar kaydı (ikincil). Site derlendi; şemalar mobil açık/koyu temada PNG alındı ve gözle kontrol edildi (`scratchpad/shots/c-04`).

## Ayetler
| Öğe | Durum | Not |
|---|---|---|
| En'âm 6:116, Nisâ 4:115, Âl-i İmrân 3:159, Âl-i İmrân 3:103, Nisâ 4:59 (blok) | doğru | Betikle birebir. |
| Şûrâ 42:38 bloğu | düzeltildi | Arapça yalnız 38. ayetti, Diyanet meali 36-38'i birlikte veriyordu (cite'ta not vardı). `ayet.py "Şûrâ 42:36-38"` ile yeniden üretildi; Arapça ve meal artık aynı aralıkta. |
| Yûnus 10:36, Bakara 2:170, Mâide 5:104, İsrâ 17:36, Âl-i İmrân 3:110, Bakara 2:143, Âl-i İmrân 3:159 ("karar verdiğinde Allah'a güven") (metin içi) | doğru | Tırnaklı parçalar `kuran_tam.json` ile birebir; 3:110'daki "çoğu yoldan çıkmıştır" ve üç nitelik doğru. |

## Hadisler (numara | metin | derece)
| Öğe | Durum | Not |
|---|---|---|
| İbn Mâce 3950 (Enes) "dalâlet üzere birleşmez, sevâd-ı a'zam" | doğru | Elbânî, Abdülbâkî, Arnaut: çok zayıf; Zübeyr Ali Zai: zayıf. Tabloda ve kutuda dürüstçe yazılı; "hasen li-ğayrihî diyen bulunamadı" ifadesi doğru çerçevede. |
| Tirmizî 2166 (İbn Abbas) "Allah'ın eli cemaatle" | doğru | Tirmizî: hasen garîb; Elbânî, Şâkir: sahih; Zübeyr: isnadı sahih. |
| Tirmizî 2165 (Ömer'in Câbiye hutbesi) | doğru | Tirmizî: hasen sahih garîb; Elbânî, Şâkir, Zübeyr: sahih; Bashar Avvâd: hasen sahih. Metin ("şeytan teke yanındadır, iki kişiden uzaktır") doğru. |
| Tirmizî 2167 (İbn Ömer) | düzeltildi | Tablo "zayıf (Elbânî)" diyordu. API'de Elbânî "zayıf", ama Dorar'daki Sahîhu't-Tirmizî kaydı "ومن شذ" ilavesi hariç sahih. Kaynaklar ayrıştığı için hücre bunu açıkça yazacak şekilde değiştirildi (Şâkir sahih, Zübeyr zayıf). İbnü'l-Mübârek hikâyesi ve "cemaatin tefsiri fıkıh, ilim, hadis ehlidir" Tirmizî'nin metninde birebir. |
| Ebû Dâvûd 4253 (Ebû Mâlik el-Eş'arî) | doğru | Elbânî, Muhyiddin Abdülhamid, Zübeyr: zayıf. |
| Buhârî 7084 / Müslim 1847 (Huzeyfe) | düzeltildi | Numaralar ve metin doğru; sahih. Arapçada "ve imâmehum" tekil; "imamlarına" → "imamına" yapıldı. Müslim 1847'de de aynı cümle ("cemaat ve imamları") var. |
| Müslim 1848 (Ebû Hureyre), 1850 (Cündeb) | doğru | İtaatten çıkıp cemaatten ayrılma, kör bayrak, kılıç çekme; sahih. Metin kısaltılmış olarak etiketli. |
| Ebû Dâvûd 4596 (Ebû Hureyre) | doğru | Elbânî, Muhyiddin, Arnaut: hasen sahih; Zübeyr: isnadı hasen. Yalnız sayı bildirir (71/72 Yahudi-Hıristiyan, 73 ümmet). |
| Tirmizî 2641 (Abdullah b. Amr) | doğru | Tirmizî: hasen garîb müfesser; Elbânî, Şâkir: hasen; Zübeyr: zayıf. "Mâ ene aleyhi ve eshâbî" cevabı. |
| İbn Mâce 3992 (Avf b. Mâlik), 3993 (Enes); Ebû Dâvûd 4597 (Muâviye) | düzeltildi | Metin "Hangisi? → el-cemâa" idi; ama 3993'te soru-cevap yok, doğrudan "o da el-cemâadır" der. Cümle buna göre düzeltildi. Dereceler: 3992 Elbânî sahih (Arnaut sahih li-ğayrihî), 3993 sahih, 4597 hasen. |
| "Mâ ene aleyhi…" yalnız Tirmizî'de | düzeltildi | Mutlak ifade fazla genişti (başka kitaplarda da geçebilir). "Burada andığımız kitaplar içinde yalnızca" kaydı eklendi. |
| Müslim 1920 (Sevbân); Buhârî 71, 3640 | doğru | "Benzeri" kaydı yerinde (71 Muâviye, 3640 Mugîre). |
| Müslim 145, Müslim 2623 | doğru | 03. bölüm denetimine bkz. |
| Buhârî 2731-2732 (Hudeybiye, Ümmü Seleme) | doğru | Metinde Ümmü Seleme'nin tavsiyesi ve Hz. Peygamber'in uyması var. |

## Âlim/kitap/ansiklopedi iddiaları
| Öğe | Durum | Not |
|---|---|---|
| TDV "İcmâ": tanım, ehliyet müctehidlerde, ehlü'l-hal ve'l-akd, Şâfiî'nin çoğunluğu kaynak saymaya karşı çıkışı (el-Üm VII, 256), bütün müctehidlerin ittifakı, tek müctehidin muhalefeti, şâz görüş hariç delilsiz icmâ olmaz, konu şer'î-ictihadî, mütevâtir sünnet gücü | doğru | Maddenin tam metninde (curl) karşılaştırıldı; birebir uyumlu. |
| TDV "İcmâ": Âmidî ve avam | düzeltildi | "Âmidî gibi bazı usûlcüler" doğrudan iddia gibi yazılmıştı. TDV bunu "ümmet" lafzının zâhir anlamından hareket eden bazı usûlcüler için der ve örnek olarak Âmidî'yi (el-İhkâm I, 204) gösterir. Cümle TDV'ye atıfla yeniden kuruldu. |
| TDV "İcmâ": Gazâlî ayetler için "kısmen kuvvetli" | doğru | Madde metninde aynen. |
| TDV "Şûra": balın kovandan çıkarılması (Râgıb), Uhud'da çoğunluğa uyma, nasla düzenlenenin şûraya konu olmaması (ittifak), bağlayıcılık görüşleri, "objektif kriterler geliştirilememiştir" | doğru | Madde metninde var. |
| Gazâlî, el-Müstasfâ (el-mesleku's-sâlis, "et-tarîku'l-ma'nevî", "zayıftır") + "Ali'nin cesareti, Hâtem'in cömertliği" | doğru | Islamweb Arapça metninde; sayfa işaretleri 142-143 arasında. Alıntı kısaltılmış/özetlenmiş diye etiketli; Yahudilerin örnek olarak geçtiği kısım atlanmış ama anlam bozulmamış. |
| Şâfiî, er-Risâle: cemaatin anlamı ("gaflet ancak ayrılıkta"), bedenlerin bir arada olmasının önemi yok | doğru | Şâmile'de (§1316-1321) Arapça birebir; Ömer hutbesi için Şâkir dipnotu (mürsel isnad, Tirmizî hasen sahih garîb) doğru. |
| İbn Hacer, Fethu'l-Bârî 13/37: Taberî'den dört yorum (sevâd-ı a'zam / sahâbe / ehl-i ilim / emirliği üzerinde birleşilen imamın cemaati) | doğru | Şâmile metninde; Taberî'nin tercihi doğru aktarılmış. |
| Elbânî: hadis icmâyı ispatlamaz, nefyeder; icmâ "dinden zorunlulukla bilinen"; Nisâ 4:115 | doğru | al-albany.com dökümü okundu; dört beş icmâ tanımı sayması da orada. |
| Tirmizî/İbnü'l-Mübârek: "Ebû Hamza es-Sükkerî bir cemaattir" | doğru | Tirmizî 2167 Arapçasında birebir; Tirmizî'nin "sağlığında söylemiştir" notu da var, anlamı etkilemiyor. |

## Olgusal/ehl-i sünnet, güncel örnek
- Güncel grup, parti, ülke, siyasi örnek yok; metin açıkça "güncel bir gruba, partiye ya da ülkeye bakıp… çıkarılamaz" diyor. Dâru'l-İftâ fetvası ve ekol özdeşleştirmeleri bilinçli dışarıda bırakılmış (doğru karar). 73 fırka kutusu kimseyi etiketlemiyor, "hükmetmek bize düşmez" diyor.
- Şûra bağlayıcılığı: klasik ve çağdaş görüşler TDV'ye atıfla, taraf tutmadan veriliyor; güncel siyasete girmiyor.
- Uçak benzetmesi "delil değil, anlatım aracı" diye etiketli.

## HTML sözleşmesi
- İzinsiz sınıf, `<script>`, dış URL, sabit hex renk, kapanmayan etiket: yok.

## SVG (390px, açık/koyu)
- Şema 1 (sayısal çoğunluk ve ilmî ittifak) ve şema 2 (üç kavram haritası): taşma yok, koyu temada okunur, değişiklik gerekmedi.
- Şema 3 (çözüm akışı): ok başları yukarı bakıyordu, akış aşağı doğru. Yedi `polygon` aşağı bakacak şekilde çevrildi; yeniden PNG alınıp doğrulandı.

## Türkçe
- Küçük düzeltmeler yukarıda. Dil sade; baştan yazma yapılmadı. Build sayacı 2825 kelime (üst sınır 3000).

## Kalan riskler
- Tirmizî 2167 için Elbânî hükmü kaynaklar arasında çelişiyor; hücre bunu açıkça belirtiyor, ama kesin hüküm Sahîhu't-Tirmizî / Da'îfu't-Tirmizî kitaplarından teyit edilmedi.
- "Allah ümmeti dalâlette birleştirmez" yollarının Elbânî tarafından Silsile-i Sahîha'da güçlendirildiği iddiası doğrulanamadı; metin bunu iddia etmiyor ("yollar tek tek tartışmalı").
- Âmidî'nin görüşü TDV'nin aktarımına dayanıyor; el-İhkâm'dan doğrudan teyit edilmedi (metin bunu TDV'ye atfediyor).
- Dereceler büyük ölçüde fawazahmed0 `grades` alanına dayanıyor; Dorar/Elbânî kitaplarına ikincil erişimle çapraz kontrol edildi.
