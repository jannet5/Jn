# 06 Sorular ve Son Söz — DENETİM RAPORU

Denetçi: bağımsız denetim (2026-10-17). Dosya: `bolumler/06-sss-kapanis.html`. Araç: `araclar/ayet_dogrula.py` → "4 ayet bloğu denetlendi, 0 uyarı". Satır içi ayet alıntıları `veri/kuran_tam.json` (Diyanet) ile tek tek karşılaştırıldı. Hadisler fawazahmed0 API (eng/ara-bukhari, eng-muslim, eng/ara-abudawud). Tefsirler quran.com API ve altafsir.com'dan yeniden çekildi. Gazâlî Şâmile sayfaları (c.4 s.400, 401, 404) ve Nevevî (hadithprophet.com) yeniden açıldı.

## Özet
Denetlenen: 4 ayet bloğu + 19 satır içi ayet alıntısı, 7 hadis atfı, 10 âlim/kitap alıntısı, 14 satırlık kart, 2 SVG, 8 SSS. Düzeltilen: 12 madde (kartta 8 satır dahil). Çıkarılan: 0. Koordinatör notu uygulandı: kartta 2:243, 10:36, 5:103 tam numarayla yazılı; görselde simgelerin örttüğü üç numaraya (Bakara 24…, Yûnus 3…, Mâide 10…) kart altı notta atıf yapıldı, 00 ile tutarlı.

## Ayetler
| Öğe | Durum | Not |
|---|---|---|
| Hucurât 49:6, 49:11, En'âm 6:116, Zümer 39:17-18 (Arapça+Diyanet) | doğru | Betik 0 uyarı. Zümer'de Diyanet kaydı iki ayeti aynı metinle verip Türkçeyi iki kez yazdırıyordu; tekrar çıkarıldı, cite'a not düşüldü |
| Şûrâ 42:38 «Onların işleri aralarında danışma iledir» (42:36-38 birleşik kayıt) | doğru | |
| Âl-i İmrân 3:159, İsrâ 17:36, Sebe 34:13, Sâd 38:24, Hûd 11:40, Bakara 2:249, 2:88, Nisâ 4:46, Asr 103:3, Nahl 16:125 | doğru | Tırnaklı kısımlar Diyanet metniyle birebir |
| Haşr 59:18 gönderme | doğru | |

## Hadisler
| Öğe | Durum | Not |
|---|---|---|
| Müslim 60 (ey kâfir; birine döner) | doğru | hadithnumber 215/216, arabicnumber 60.01/60.02, Sahih (Müslim) |
| Müslim 2623 (insanlar helâk oldu; Ebû İshak tereddüdü) | doğru | arabicnumber 2623.01; tereddüt notu metinde var |
| Müslim 145 (İslâm garip başladı) | doğru | arabicnumber 145. Yorum "lafzı şunu söylemez" yerine "lafzında bu sonuç yok" yapıldı (ihtiyat) |
| Buhârî 5752 (İbn Abbas; ümmetler gösterildi) | doğru | Metin ve "ümmeti ufku kaplar" yorumu uyuyor |
| Ebû Dâvûd 4992 (kefâ bi'l-mer'i ismen...) | düzeltildi | Derece API'de Elbânî/Abdülhamîd/Zübeyr Ali Zai: sahih; metinde "Elbânî'ye göre sahih" diye atıf netleştirildi. Müslim Mukaddime "yalan" lafzı: numarasız bırakılmış, doğru |
| Hucurât 49:6 ile bağ | doğru | |

## Âlim/kitap alıntıları
| Öğe | Durum | Not |
|---|---|---|
| Taberî 6:116 «…çünkü onlar o sırada kâfir ve sapkındılar» | doğru | Arapça birebir `لأنهم كانوا حينئذ كفارًا ضلالا` |
| Kurtubî 6:116 «yani kâfirler» | doğru | `أي الكفار` |
| "Klasik tefsirler 6:116'yı tarihî duruma bağlar" | düzeltildi | İbn Kesîr ve Sa'dî genel okuyor (İbn Kesîr: "ekser ehl el-ard: dalâl", Sâffât 37:71 ve Yûsuf 12:103'e gönderir). Metin "tefsirler aynı okumaz" biçimine getirildi; İbn Kesîr/Sa'dî'nin "zan ve tahmin" vurgusu eklendi (iki kaynaktan da doğrulandı) |
| Q1: "«insanların çoğu» beşi hüküm değil gözlemdir" | düzeltildi | Âlimler bu beşin kapsamında ayrılıyor (02 ile çelişiyordu: Celâleyn/Kurtubî/Râzî Mekkeliler; İbn Kesîr/Sa'dî/İbn Âşûr geniş). Cümle, ayrılığı belirtip "insan eğilimleri, etiket değil" sonucunu koruyacak şekilde yeniden yazıldı |
| İbn Kesîr 42:38 + Hz. Ömer'in altı kişilik şûrası | doğru | Arapça ve İngilizce metinde var (Osman, Ali, Talha, Zübeyr, Sa'd, Abdurrahman b. Avf) |
| İbn Kesîr 34:13 Fudayl rivayeti | doğru | `قال فضيل ... الآن شكرتني حين علمت أن النعمة مني` |
| İbn Kesîr 34:13 «gerçeğin bir yansıması» | düzeltildi | Kaynak İngilizce kısaltma; Arapçası `إخبار عن الواقع` → «olanı haber vermek» |
| Taberî 2:88 (iki okuma; Katâde; "az iman"; dilbilgisi gerekçesiyle ilkini zayıf bulur) | doğru | Metinle birebir uyuyor |
| Nevevî, Şerhu Müslim, Müslim 2623 şerhi | doğru | "ittefeka'l-ulemâ ... ezrâ' ala'n-nâs ... lâ ya'lemü sırra'llâh fî halkıhi"; "hüzünle söyleyenin sakıncası yok" (Mâlik'ten) uyuyor. WebFetch tırnak sınırından dolayı ikinci cümlenin Arapçası birebir kopyalanamadı, anlam doğrulandı |
| Gazâlî, İhyâ c.4 s.404: Ömer «hâsibû enfüseküm...», Haşr 59:18'e işaret | doğru | Şâmile sayfası açıldı, metin birebir |
| Gazâlî c.4 s.400-401: harekete geçmeden önce "lillâh mı, hevâ mı?" yoklaması | doğru | s.400 ve s.401 açıldı |
| Elmalılı (TDV maddesi), Râzî archive.org, Taberî/İbn Kesîr/Kurtubî API ve altafsir bağlantıları | doğru | Hepsi 200 döndü |
| Diyanet Kur'an Portalı | düzeltildi | Bu ortamda 403; "açılıyor" ifadesi "portal da var; içeriğini bu çalışmada okuyup doğrulayamadık" yapıldı |

## Kart (14 satır) — üç yönlü karşılaştırma (bölüm 00 tablosu / bölüm 02 sınıflaması / kuran_tam.json)
| Satır | Durum | Not |
|---|---|---|
| 1 Nahl 16:83 | düzeltildi | "Kime" notu Peygamber'in kavmi (02 A, Taberî) ile eşleştirildi |
| 2 Mâide 5:49 | düzeltildi | Kırmızı not "Atıf 48 değil 49 · «kesîr» · Kime: tefsirler ayrılır" (satırda "kime" notu eksikti; 02 C: Taberî/Kurtubî Yahudiler, İbn Kesîr/Sa'dî genel) |
| 3 Rûm 30:42 | doğru | 02 B |
| 4 İsrâ 17:89 | düzeltildi | "genel ifade" → «insanların çoğu» · kapsamı tefsirler farklı okur (Celâleyn/Kurtubî/Râzî ≠ İbn Âşûr) |
| 5 Yûnus 10:92 | düzeltildi | "âyetlerden" → "ayetlerden"; "genel" → «insanların çoğu» · Firavun kıssasının ardından |
| 6 Şuarâ 26:223 | doğru | 02 A |
| 7 Yûnus 10:36 | doğru | Tam numara (görselde kesik: Yûnus 3…); 02 A |
| 8 Furkân 25:50 | düzeltildi | "genel" → «insanların çoğu» (02 C ile aynı dil) |
| 9 Bakara 2:243 | düzeltildi | Tam numara (görselde kesik: Bakara 24…); "genel" → «insanların çoğu» |
| 10 Bakara 2:100 | doğru | 02 A (İbn Kesîr, ahid bozan) |
| 11 Zuhruf 43:78 | doğru | 02 A (Taberî "yâ ma'şera Kureyş") |
| 12 Yûsuf 12:106 | doğru | 02 A |
| 13 Fussılet 41:4 | doğru | 02 A |
| 14 Mâide 5:103 | düzeltildi | **Çelişki vardı:** kart "Allah'a yalan uyduranlar" diyordu; 02'ye (Taberî) göre "çoğu akletmez" uyduranlar değil takipçilerdir. "Kime: Câhiliye âdetine uyan takipçiler (Taberî)". Tam numara (görselde kesik: Mâide 10…) |
| Sure/ayet numaraları, Diyanet fiilleri (çoğunuz, nankörlükte direndi, habersiz...) | doğru | `kuran_tam.json` ile |

## Ton (tekfir, siyaset, şûrâ)
İhtiyatlı ve fetvasız: "bu ayetler bugünkü ülkeleri/grupları puanlamak için kullanılamaz", "hüküm vermek delile ve ehline bağlı", Q2 şûrâ'yı "hakikat" ile "işlerin yürütülmesi"ni ayırarak ve fıkıh/siyaset ayrıntısına girmeyerek ele alıyor, Q3 "evet de hayır da demiyoruz". Düzeltme gerekmedi. Kapanışta ek olarak kart altı not, kartı hedef göstermek için kullanmamayı söylüyor.

## Bölüm bağlantıları
`#b-00 … #b-05` hepsi var (bolumler/00–05 mevcut, derlenmiş indexte `section#b-0N`).

## SVG
2 şema (üç ilke; düzeltilmiş kart), 390px açık/koyu PNG `scratchpad/shots/c-06`. Düzeltmeler sonrası yeniden üretildi, gözle kontrol: taşma/çakışma/koyu tema sorunu yok; yeni "Kime" satırları 432 birimlik genişliğe sığıyor. `svgTextTasma: []`.

## HTML sözleşmesi
Script yok, SVG dışında inline stil yok, sabit hex yok, sınıflar izinli, etiketler dengeli. Dış URL'ler yalnızca kaynak bağlantısı olan `<a href>` (görsel/script değil), 200 dönenler; JSON uç noktaları okurda ham JSON olarak açılıyor (metinde "JSON olarak açılır" denmiş).

## Kalan riskler
- Müslim Mukaddime hadisinin numarası verilmemiş (doğru tercih).
- Ebû Dâvûd 4992'de Ebû Dâvûd'un kendi senedî notu var (zincir ihtilafı); metinde Elbânî'ye atıf yapıldı, daha fazla ayrıntı verilmedi.
- Diyanet Kur'an Yolu hakkında içerik doğrulanmadı (metinde böyle belirtiliyor).
