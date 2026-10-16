# Bölüm 09 — Cennetin Zirvesi: denetim raporu

Denetçi notu: ayet metinleri `araclar/ayet_dogrula.py` ile (13 blok, düzeltme sonrası 0 uyarı), hadisler fawazahmed0 hadith-api yerel kopyası (ara/eng-bukhari, ara/eng-muslim, eng-tirmidhi, eng-ibnmajah; Müslim için `arabicnumber` = Abdülbâkî numarası) ile, alıntılar kaynak URL'leri yeniden açılarak denetlendi.

## Ayetler

| Öğe | Durum | Not |
|---|---|---|
| 13 `blockquote.ayet` (Secde 32:17, Tevbe 9:72, Yûnus 10:26, Kâf 50:35, Kıyâme 75:22-23, Mutaffifîn 83:22-24, Yâsîn 36:58, Bakara 2:25, Rahmân 55:29, Kehf 18:109, Kehf 18:108, Duhân 44:56-57, Mutaffifîn 83:15) | doğru | Arapça + Diyanet meali betikle birebir eşleşti. |
| Duhân 44:56 bloğu | düzeltildi | Diyanet kaydı 56-57'yi birleştiriyor ("İşte büyük kurtuluş budur" 44:57'dir); Arapça 44:57 eklendi, künye "44:56-57" yapıldı. |
| Metin içi atıflar: Hıcr 15:47-48, Beyyine 98:8, Mâide 5:119, Fecr 89:28, Şûrâ 42:11, En'âm 6:103, A'râf 7:143, Vâkıa 56:25-26, Ra'd 13:23-24, Fâtır 35:35, Rahmân 55:26-27, Yûnus 10:10, Kehf 18:107, Zuhruf 43:71 | doğru | Alıntı metinleri API'deki Diyanet metniyle aynı. Vâkıa'da Arapça numara 25-26 (Diyanet kaydı kaymış, sorun yok). |
| Fussılet "ziyafet" atfı | düzeltildi | "Allah katından bir ziyafet" (nüzül) Arapçada 41:32'dir; "41:30-31" → "41:30-32". |
| Zuhruf alıntısı | düzeltildi | Atıfsızdı; (Zuhruf 43:71) eklendi. |
| Rahmân 55:29 yorumu | düzeltildi | "Her an bir iştedir" tırnak içinde Diyanet metni değildi; "her an kâinata tasarruf etmektedir" olarak Diyanet ifadesine çekildi (gövde + özet maddesi). |

## Hadisler

| Öğe | Durum | Not |
|---|---|---|
| Buhârî 3244, 7498; Müslim 2824 (hiçbir göz görmemiş) | doğru | Sahih, hadis-i kudsî; metin uyuşuyor. |
| Buhârî 6549; Müslim 2829 (rıdvan) | doğru | Numara ve metin uyuşuyor (Abdülbâkî 2829 = Ebû Saîd, "razı oldunuz mu?"). "Hayır hep Senin elindedir" Müslim'deki ek, Buhârî'de yok; birleşik anlatım kabul edilebilir. Sahih (Şeyhayn). |
| Müslim 181 (Suheyb, perde kalkar) | doğru, ifade düzeltildi | 181.01-02: metin uyuşuyor. Ayeti kimin okuduğu rivayetten kesin değil; "(Hammâd: ...okudu)" yerine "Hammâd b. Seleme'nin rivayetinde şu ek vardır" yazıldı ve ayet Diyanet ifadesiyle verildi. |
| Müslim 182 | doğru | 182.01 (Ebû Hureyre, ay/güneş "tudârrûne"), 182.03 (en düşük cennetlik). BRIEF'teki "182 = daha fazla ister misiniz" yanlıştı; o 181'dir. Metin 182'yi yalnızca "aynı anlamda" diye anıyor, doğru. |
| Buhârî 554, 7435, 7436 (ay gibi görmek) | doğru | 554: Cerîr, "lâ tudâmmûne fî ru'yetihî"; 7435: "ıyânen"; 7436: Cerîr, dolunay gecesi. Alıntı 7436 ile uyumlu. Sahih. |
| Buhârî 7439 | doğru | Ebû Saîd, güneş-ay, "bulutsuz (sahv)". |
| Buhârî 6548 | doğru | İbn Ömer, ölüm boğazlanır, sevinç artar. |
| Buhârî 4730; Müslim 2849 | doğru | Ebû Saîd, siyah-beyaz (emlah) koç; Müslim'de "beyaz". |
| Müslim 2837 | doğru | Sağlık, hayat, gençlik, nimet münâdîsi. |
| Müslim 2835 | doğru | Tesbih ve hamdin nefes gibi ilhamı. |
| Buhârî 5673; Müslim 2816 | doğru | "Beni de, Allah rahmetiyle kuşatmadıkça." |
| Buhârî 6571 | düzeltildi | Hadis "cehennemden çıkan son kişi/cennete giren son kişi" için ve "dünya ve on katı" verilir der; cümle buna göre yeniden yazıldı. |
| Tirmizî 2553, 3330 (bin yıllık mesafe, sabah-akşam bakmak) | doğru (zayıf, kullanılmadı) | Elbânî, Şâkir, Zübeyr Ali Zâî: daif. |
| Tirmizî 2549 (cennet pazarı/Cuma ziyareti) | doğru (zayıf) | Elbânî, Şâkir, Zübeyr: daif. |
| İbn Mâce 184 | doğru (zayıf) | Elbânî, Abdülbâkî, Arnaut, Zübeyr: daif (Şâkir bu hadiste yok). Uyarı kutusu "Elbânî, Zübeyr Ali Zâî ve diğerleri" diye düzeltildi. |
| Müslim 2930 (ölmeden Rabbini göremez) | eklendi | Rü'yet bahsine dünyada görülmeme ilkesi için eklendi. Fawaz verisinde bu rivayetin numarası bozuk (169.04 görünüyor); 2929 ile 2930.02 arasındaki sıradan Abdülbâkî 2930.01 olduğu çıkarıldı. Sahih. Numaranın sunnah.com'dan ayrıca teyidi yapılamadı: düşük risk. |

## Ek görev (b): Rü'yetullah çizgisi

- "Benzetilen görmenin netliği ve kolaylığıdır, görülenin kendisi değil" (görmeyi görmeye benzetme, Allah'ı aya benzetmeme) Ehl-i Sünnet çizgisine uygun; şema alt yazısı da Şûrâ 42:11'e bağlıyor. Doğru.
- En'âm 6:103'te "idrak = ihata/kuşatma" yorumu doğru. Mu'tezile'nin reddi doğru aktarılmış.
- Eksik olan "dünyada değil ahirette" vurgusu açık yazılmamıştı (yalnızca A'râf 7:143 notu vardı). Eklendi: rü'yet ahiret nimetidir, dünyada uyanıkken kimse görmez (Müslim 2930); A'râf 7:143 dünya durumuyla ilgili; Miraç'taki rü'yet ayrı ve tartışmalı konu diye bir cümleyle ayrıldı.
- Rıdvan-rü'yet "daha üstün" ifadesi: şema alt yazısı ve özet maddesi "hadislerde ikisi de daha üstün" diyordu. Doğrusu: rıdvan ayette "ekber" (Tevbe 9:72) ve hadiste "daha üstünü" (Buhârî 6549), bakış hadiste "daha sevgili" (Müslim 181). İfade düzeltildi.

## Âlim/kitap alıntıları (ek görev c)

| Öğe | Durum | Not |
|---|---|---|
| İbn Kayyım (Hâdi'l-Ervâh, Fransızca "Le Paradis") "Allah'ı gördükten sonra... müttefiktir" | doğru | archive.org djvu.txt açıldı; Fransızca "Après la vision de Dieu, les personnes du Paradis oublieront toutes les félicités. Son Absence et le voile... plus dur... que le supplice de la Géhenne" ve "Sont d'accord... les Messagers, les Prophètes, l'ensemble des compagnons, les exégètes et les savants de l'Islam" ile uyuşuyor. Çeviri notu metinde var. |
| İbn Kayyım: Ali, Enes (Kâf 50:35, Taberânî), Ebû Bekir, Huzeyfe (Yûnus 10:26) | doğru | Aynı çeviride bu isimler geçiyor (ayrıca İbn Abbas, İbn Mes'ûd). |
| Râbia'ya atfedilen "iki sevgi" beyti (Gazâlî, İhyâ, Ormsby) | korundu, ifade yumuşatıldı | Beytin İngilizce metni Ormsby çevirisinin OCR'ında açıldı ve çeviri uyuşuyor; ardından Gazâlî'nin yorumu ("love of passion", "love because He merits it") da orada. Ancak OCR'da beyti tanıtan satırdaki isim okunamıyor ("She recited a poem"). Râbia atfı genel gelenekle (Taha Abdülbâkî Surûr'un Râbia kitabı, Jagot yazısı vb.) tutarlı ama İhyâ'daki ismi doğrudan göremedik. Bu yüzden "genellikle Râbia'ya atfedilen" ve "Gazâlî aktarır ve yorumlar" diye hafifletildi. Daha sert tercih: çıkarmak. |
| Mevlânâ | metinde yok | Yazar zaten çıkarmış; HTML'de Mevlânâ/Rûmî geçmiyor. |
| Gazâlî bölüm başlığı (4. ve 5. Bölüm) ve "gözü kapalı hayal / gözü açıp bakmak" benzetmesi | doğru (özet) | OCR'da "shutting the eyes... remains mere fantasising" ve "eyelids veil the vision of the eyes" bulundu; metin zaten "Ormsby'den özetlenmiştir" diyor. |
| Said Nursî, 32. Söz 2. Mevkıf 3. Maksat 4. Remiz | korundu (aktarım etiketli) | risalehaber.com sayfasında cümle ve aynı atıf var; yazar hadis diye aktarıyor. risale.online sayfa penceresinde (24751 karakter) cümle bulunamadı; birincil metinle teyit edilemedi. Metindeki "aktarımdan alınmıştır, ayrıca teyit edilmemiştir" kaydı doğru; istenirse çıkarılabilir. |
| Omar Suleiman (Yaqeen, Jannah: Home at Last, Bölüm 27) | doğru | "The scholars agree that seeing Allah ... is the ultimate joy of the Akhirah" 1:00'da; 17 Nisan 2023. |

## Olgusal ve dil düzeltmeleri

| Öğe | Durum | Not |
|---|---|---|
| Özet cümlesi "Sevgili'nin hoşnutluğu" | düzeltildi | Allah için kitabî olmayan bir ad; "Rabbin hoşnutluğu". |
| "Size razı oldum" (başlık, şema, sahne) | düzeltildi | Hadiste yok, Türkçesi de bozuk; hadis ifadesine çekildi ("Bundan sonra size asla kızmayacağım" / "Size hoşnutluğumu indireceğim…"). |
| Şema 2 (ay) aria-label | düzeltildi | "göremeyecekleri bir engel olmadan" anlamsızdı. |

## HTML sözleşmesi ve şemalar

- `<script>`, dış URL, sabit hex renk, izinsiz sınıf yok; etiketler kapalı. Doğru.
- 3 şema, açık ve koyu temada 390px'te PNG alındı; taşan metin yok (`over` ve `svgTextTasma` boş). Şema 2'de başlık metni bir yıldızla çakışıyordu (yıldız taşındı). Şema 1 ve 3 temiz.

## Kalan riskler

- Müslim 2930 numarası fawaz verisindeki bir bozukluktan çıkarıldı (sıradan türetildi).
- Râbia atfının İhyâ'daki adı doğrudan görülemedi; ifade hafifletildi.
- Said Nursî cümlesi ikincil aktarımla doğrulandı.
