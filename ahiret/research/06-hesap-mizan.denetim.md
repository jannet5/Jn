# 06 Hesap ve Mizan: denetim raporu

Denetçi notu: ayetler api.quran.com (Uthmani + Diyanet id 77) ile betikle birebir karşılaştırıldı; hadisler fawazahmed0 baskıları (eng-bukhari, eng-tirmidhi, eng-abudawud, eng-nasai) ve en.tohed.com (Müslim numaraları, Arapça metin) ile; âlim alıntıları açılan kaynaktan (tefsir API, PDF) doğrulandı.

## Ayetler

| Öğe | Durum | Not |
|---|---|---|
| 17 `blockquote.ayet` (40:17; 99:7-8; 17:13-14; 18:49; 84:7-9; 84:10-12; 69:19-20; 36:65; 41:20-21; 99:4-8; 102:8; 17:36; 21:47; 7:8-9; 101:6-9; 4:40; 39:53) | doğru | Arapça ve Türkçe API ile birebir aynı; sure adı ve numaralar doğru |
| Metin içi 41:22 | düzeltildi | Alıntıdaki "şahitlik" Diyanet metnindeki "şahidlik" yapıldı |
| Metin içi 4:48 | düzeltildi | Tırnak içindeki ifade Diyanet metnine uyduruldu ("ortak koşmayı elbette bağışlamaz") |
| Metin içi 69:25-29, 15:92-93, 32:17, 6:164, 18:105, 23:102-103, 31:16, 82:10-12 | doğru | API ile karşılaştırıldı; kısaltmalar "…" ile belirtilmiş, anlam bozulmamış |
| İsrâ 17:13-14 yorum cümlesi | düzeltildi | "Hükmü veren yine kitabın kendisidir" ayette yok; "hesap görücü olarak kişinin kendisi yeterli sayılır" yapıldı |

## Hadisler

| Öğe | Durum | Not |
|---|---|---|
| Buhârî 6536 ve 103; Müslim 2876 (Âişe, arz) | doğru | Metin ve numara tuttu; sahih |
| Buhârî 2441 ve 6070 (necva, örtme) | doğru | İbn Ömer rivayeti; özet anlam |
| Müslim 2969 (Enes, organların şahitliği) | doğru | Arapça ve İngilizce metin tohed'de doğrulandı (Zühd kitabı) |
| Tirmizî 2417 (ömür, ilim, mal, beden) | düzeltildi | Metin doğru; derece ihtilafı eklendi (Tirmizî hasen sahih, Elbânî sahih, Zübeyr Ali Zâî zayıf) |
| Tirmizî 413 / Nesâî 465 (ilk hesap namaz) | doğru | Elbânî sahih; Zübeyr Ali Zâî zayıf; metinde "bazı muhaddisler tartışmıştır" denmiş |
| Buhârî 6533 (kan davaları) | doğru | İbn Mes'ûd; sahih |
| Müslim 2581 (müflis), 2582 (boynuzsuz koyun) | doğru | Numaralar Abdülbâkî sırasıyla tuttu |
| Buhârî 2449 (helallik) | doğru | Metin tuttu |
| Müslim 1886 (şehit, borç hariç) | doğru | Başlık "borç hariç tüm günahlar bağışlanır" |
| Buhârî 6406; Müslim 223; Ebû Dâvûd 4799 (mizan) | doğru | Elbânî Ebû Dâvûd 4799'a sahih der |
| Tirmizî 2639 (bıtaka) | doğru | Elbânî ve Zübeyr Ali Zâî sahih; "Tirmizî hasen garîb" notu İbn Kesîr aktarımına dayanıyor (Tirmizî'nin kendi hükmü baskıda görünmedi) |
| Buhârî 6541; Müslim 220 (hesapsız yetmiş bin, Ukkâşe) | doğru | Özet anlam metne uygun |
| Tirmizî 2437 (her bine yetmiş bin) | doğru | Elbânî ve Şâkir sahih; Beşşâr hasen |
| Buhârî 6491 (iyilik 10-700 kat) | doğru | Metin ve şemadaki üç satır tuttu |
| Buhârî 3244, 7498 (gözün görmediği nimet) | doğru | Metinle uyumlu |
| Tirmizî 2429 (yerin şahitliği) | doğru | Zayıf isnad (Elbânî, Şâkir, Zübeyr); metinde delil yapılmamış, "zayıf" notuyla anılmış |

## Âlim ve kitap alıntıları

| Öğe | Durum | Not |
|---|---|---|
| Said Nursî, Onuncu Söz, Yedinci Suret ve haşiyesi | doğru | Sözler PDF'inde (S:53-54) açıldı; alıntı birebir. "Fotoğraf makineleri" ifadesi Nursî'nin "fotoğraflar kurulmuş" sözünün sade karşılığı |
| Kurtubî, Enbiyâ 21:47 tefsiri | doğru | Arapça metin tefsir API'sinde (id 90) bulundu |
| İbn Kesîr, Enbiyâ 21:47 tefsiri | doğru | Arapça metin tefsir API'sinde (id 14) bulundu |
| Çapak, Gazâlî özeti (YDÜ İlahiyat Dergisi 2/1) | düzeltildi | PDF açıldı; alıntı doğru; "lütfu" yazımı kaynaktaki "lütfü" yapıldı. İkincil aktarım olarak etiketli |
| "Daha derine in" kutusundaki Diyanet Fetva Kurulu / İbn Teymiyye atfı | çıkarıldı | Yalnızca ikincil sitelerde bulundu; birinci el kaynak açılamadı. İsimler kaldırıldı, ifade ihtiyatlı ve fetvasız hâle getirildi |

## Olgusal iddialar, sözleşme, dil

| Öğe | Durum | Not |
|---|---|---|
| Mizan mahiyeti ihtilafı, kâfirler için tartı | doğru | Ana çizgiye uygun, tartışma ihtiyatla aktarılmış |
| Hesap tertibi (defter, şahit, soru, hak, mizan) | doğru | "Nasslar tek sıra vermez" notu hem şemada hem metinde var |
| Mizan cümlesi ("ağırlık bizim hesabımıza göre değişir") | düzeltildi | Anlaşılmazdı; "neyin ağır geleceği dünyadaki hesaplarımıza uymayabilir" yapıldı |
| HTML sözleşmesi | doğru | İzinsiz sınıf, script, dış URL, hex renk, kapanmayan etiket yok |
| Şema 1 (hesap haritası) | düzeltildi | Adımlar arası oklar yukarı bakıyordu; aşağı çevrildi |
| Şema 2 (müflis), Şema 3 (mizan) | doğru | Mobil açık ve koyu temada taşma/çakışma yok; koyu temada okunur |

## Kalan riskler

- Tirmizî 2639 için Tirmizî'nin kendi "hasen garîb" hükmü doğrudan görülmedi (İbn Kesîr aktarımından).
- Gazâlî görüşü yalnızca makaledeki özetten; kendi eseri açılmadı (metinde böyle etiketli).
- Tirmizî 413 ve 2417'de Zübeyr Ali Zâî'nin zayıf hükmü azınlık görüşüdür; metinde kısaca anıldı.
