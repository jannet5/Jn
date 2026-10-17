# 02 Her Ayet Kime Konuşuyor? — DENETİM RAPORU

Denetçi: bağımsız denetim (tarih 2026-10-17). Dosya: `bolumler/02-baglam.html`. Araç: `araclar/ayet_dogrula.py` → "16 ayet bloğu denetlendi, 0 uyarı". Tefsir metinleri yeniden çekildi: quran.com API (İbn Kesîr id 14/169, Taberî 15, Kurtubî 90, Sa'dî 91, Tantâvî 93) ve altafsir.com (Celâleyn 8, Râzî 4, Keşşâf 2, İbn Âşûr 54). Hadisler fawazahmed0 API'den (ara/eng-bukhari). TDV maddeleri (kufur, fasik, sirk) yeniden açıldı.

## Özet
Denetlenen: 16 ayet bloğu, 2 hadis, ~45 tefsir/âlim atfı, 3 TDV tanımı, 1 Râgıb atfı, 3 SVG. Düzeltilen: 5 küçük ifade. Çıkarılan: 0. Uydurma sebeb-i nüzul: yok (3 kaynaklı sebeb kaynağında doğrulandı; "bulamadım" denen 11 ayette de kaynakta kayıtlı sebeb çıkmadı; Utbe anlatısı sebeb olarak verilmemiş).

## Ayetler (blok)
| Öğe | Durum | Not |
|---|---|---|
| 16:83, 41:4, 43:78, 12:106, 10:36, 26:223, 2:100, 5:103, 30:42, 2:243, 10:92, 17:89, 25:50, 5:49, 12:103, 6:116 (Arapça + Diyanet) | doğru | Betik 0 uyarı; Arapça `kuran_tam.json` ile birebir. 41:4 ve 12:103 Diyanet kaydı komşu ayetle birleşik; metinde belirtilmiş. 41:4 Arapçası kısmi alıntı (yalnızca 4. ayetin sonu) |
| Sure adı, numara, Mekkî/Medenî | doğru | 10 Mekkî + 4 Medenî sure düzeyinde; şemadaki 8/1/5 ve 10/4 sayıları ve çubuk oranları (357/143; 286/36/178 = 10/14, 8/14, 1/14, 5/14) hesaplandı, doğru |
| Önceki/sonraki ayet özetleri | doğru | `kuran_tam.json` ile örnekleme |

## "kesîr" iddiası (5:49, 10:92)
| Öğe | Durum | Not |
|---|---|---|
| 5:49 Arapça `وَإِنَّ كَثِيرًا مِّنَ ٱلنَّاسِ` | doğru | `ekser` değil `kesîr` |
| 10:92 Arapça `وَإِنَّ كَثِيرًا مِّنَ ٱلنَّاسِ` | doğru | aynı |
| Kalan 12: 8 `ekseruhum`, 1 `ekserakum` (43:78), 3 `ekser en-nâs` (17:89, 25:50, 2:243) | doğru | Arapçadan kelime kelime sayıldı (+12:103 `ekser en-nâs`, 6:116 `ekser men fi'l-ard`) |

## Sınıflama (kime konuşuyor) ve tefsir atıfları
| Öğe | Durum | Not |
|---|---|---|
| A (8): 16:83, 41:4, 43:78, 12:106, 10:36, 26:223, 2:100, 5:103 | doğru | Bölüm 00 tablosuyla uyumlu |
| B (1): 30:42 | doğru | Taberî "kavminden müşriklere: öncekilerin yurtlarına bakın" |
| C (5): 17:89, 10:92, 25:50, 5:49, 2:243 | doğru | + ek 12:103 |
| Taberî 16:83 "nimet=Muhammed, çoğu=kavminin çoğu" | doğru | Süddî rivayetiyle |
| Kurtubî 16:83 sekiz yorum, "çoğu=hepsi" | doğru | "yuhtemil sâbian/sâmenen" + "yani cemîuhum" |
| İbn Kesîr 16:83 Mücâhid bedevî rivayeti | doğru | "fe-enzelallah" lafzı var; mürsel notu zaten metinde |
| Taberî 41:4, İbn Kesîr 41:4 "ekseru Kureyş" | doğru | |
| Taberî 43:78 "yâ ma'şera Kureyş"; Kurtubî Mâlik/Allah, İbn Abbas "kullukum", reisler görüşü; Tantâvî tekid + "az kısmı büyüklerine uyuyordu" | doğru | Metne "cehennemin bekçisi" eklendi (okur Mâlik'i bilmeyebilir) |
| 12:106: Taberî–İbn Abbas/İkrime; Celâleyn telbiye; İbn Kesîr Hasan-ı Basrî riya + Huzeyfe ipi; Kurtubî avam | düzeltildi | Kurtubî "kesîr min avâmmi'l-müslimîn" = "birçoğu"; metin "çoğu" diyordu → "birçok" |
| 10:36: Taberî, Kurtubî (reisler/takipçiler), Celâleyn (babalarını taklit) | doğru | |
| 26:223: Kurtubî (kâhin/şeytan), İbn Kesîr Âişe hadisi (Buhârî) | doğru | |
| 2:100: Taberî (Tevrat ahdi), İbn Kesîr Mâlik b. es-Sayf + Hasan-ı Basrî | doğru | |
| 5:103: Taberî (önderler vs takipçiler; Amr b. Lüheyy), Sa'dî "cehalet ve akılsızlık" | doğru | Taberî'nin tercihi metinde tam bu |
| 30:42: Taberî, Sa'dî | doğru | |
| 2:243: Taberî (başkasına yönelip nankörlük), Sa'dî ("az kısmı şükreder"), Celâleyn ("el-küffâr") | doğru | |
| 10:92: İbn Abbas'tan (İbn Kesîr, Celâleyn) ölümüne şüphe; Celâleyn "ehl Mekke" | doğru | |
| 10:92 Sa'dî "alışıp ibret almama" | düzeltildi | Sa'dî: "tekrar edilir, yönelmedikleri için yararlanmazlar"; "alışıp" Sa'dî'de yok, ifade düzeltildi |
| 17:89: Celâleyn/Kurtubî/Râzî "ehl Mekke"; İbn Âşûr "en zahir umum"; Sa'dî "ancak az kısmı" | doğru | Altafsir metninden: `والأظهر كون التعريف في الناس للعموم` |
| 25:50: İkrime, Kurtubî–Nehhâs, İbn Kesîr→Müslim | doğru | Buhârî 846 aşağıda |
| 5:49: Taberî 12150 (İbn İshak–İbn Abbas), Taberî/Kurtubî "Yahudiler", İbn Kesîr genel (Yûsuf 103, En'âm 116), Sa'dî "tabiatları fısk" | doğru | |
| 12:103: Râzî bağlam (Kureyş ve bir grup Yahudi), Kurtubî, Taberî, Celâleyn, Zemahşerî (umum + İbn Abbas "ehl Mekke"), Tantâvî | doğru | Kesin sebeb-i nüzul diye verilmemiş, doğru |
| 12:103 İbn Âşûr "ikisi de mümkün; umûm-ı örfî" | doğru | Altafsir (cp1256 çözümlemesiyle): `و{الناس} يجوز حمله على جميع جنس الناس، ويجوز أن يراد به ناس معيّنون ... فيكون عموماً عرفياً` |
| 6:116: Taberî, Kurtubî, İbn Kesîr, Sa'dî, Zemahşerî, İbn Âşûr (teselli/azlık-çokluk; hitap Peygamber'e, maksat Müslümanlar) | doğru | İbn Âşûr bağlamı altafsir'den doğrulandı |
| 6:116 Râzî "zan = atalarını taklit" | düzeltildi | Râzî "birçok müfessir bunu taklit diye açar" diyor; ayrıca "kânû dâllîn" cümlesi ayrı. Metin buna göre ayrıldı |
| Tantâvî "hemen kabul eden azınlık" | düzeltildi | Atıf 12:103'e bağlandı (Tantâvî: "tereddütsüz icabet eden bir azınlık") |
| "İnsan" kapsamında tefsirlerin ayrılması (muhatap-merkezli: Celâleyn, Taberî, Kurtubî, Râzî; genel: İbn Kesîr, Sa'dî, İbn Âşûr) | doğru | Gerçek kaynaklarla sınandı; iddia ayakta |
| Sebeb-i nüzul: 5:49, 2:100, 16:83 kaynaklı | doğru | 3'ü de kaynakta mevcut; diğerleri "bulamadım" |

## Hadis / tanım / olgu
| Öğe | Durum | Not |
|---|---|---|
| Buhârî 846 (Zeyd b. Hâlid; "yıldızın doğuşuyla") | doğru | ara/eng-bukhari 846 |
| Buhârî 6045 (Ebû Zer; fısk/küfür ithamı geri döner) | doğru | Arapça lafız birebir tutuyor |
| Buhârî 4623 (Amr b. Âmir el-Huzâî, sâibe) | doğru | |
| TDV "Küfür", "Fâsık", "Şirk" tanımları | doğru | Üçü de yeniden açıldı, tırnaklar tutuyor |
| Râgıb büyük/küçük şirk | doğru (ikincil) | Râgıb'a TDV üzerinden ve Müfredât "şrk" ifadesiyle uyumlu; Hawramani sayfası yazarca açılmış, ben ayrıca yeniden açmadım |

## SVG
3 şema, 390px açık/koyu PNG (`scratchpad/shots/c-02`) gözle kontrol edildi: taşma, çakışma, koyu temada okunmama yok; JSON `svgTextTasma: []`. Sayısal şemada oranlar doğru (yukarıda). Değişiklik gerekmedi.

## HTML sözleşmesi
Script yok, SVG dışında inline stil yok, sabit hex yok, yalnızca izinli sınıflar, etiketler dengeli, dış URL yok, `#b-00/#b-03/#b-05` var.

## Türkçe
Küçük düzeltmeler: "cehennemin bekçisi Mâlik"; Sa'dî ve Râzî cümleleri sadeleştirildi. Baştan yazma yok.

## Kalan riskler
- Koordinatör notu: bölüm 02'de "kesik numara" ile çelişen ya da onu silen bir değişiklik yapılmadı; 02 listedeki atıf düzeltmesini bölüm 00'a bırakıyor (tutarlı).
- Elmalılı'ya bakılmadı (yazar da belirtmiş).
- Mücâhid (16:83) mürsel rivayeti; metinde işaretli.
