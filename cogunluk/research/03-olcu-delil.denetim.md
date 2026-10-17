# 03 — Ölçü Sayı mı, Delil mi? — Denetim raporu

Denetçi: bağımsız denetçi (2026-10-17). Dosya: `bolumler/03-olcu-delil.html`. Yöntem: `araclar/ayet_dogrula.py` (6 blok, 0 uyarı; düzeltmelerden sonra yeniden: 0 uyarı), `veri/kuran_tam.json` ile metin içi atıfların elle karşılaştırılması, fawazahmed0 hadith-api (ara-/eng- Buhârî, Müslim, Tirmizî; `grades` alanı), archive.org tam metinleri, quran.com tefsir uç noktaları, HadeethEnc. Site derlendi; şemalar mobil açık/koyu temada PNG alındı ve gözle kontrol edildi (`scratchpad/shots/c-03`).

## Ayetler
| Öğe | Durum | Not |
|---|---|---|
| En'âm 6:116, Bakara 2:111, Hûd 11:40, İsrâ 17:36, Hucurât 49:6, Nahl 16:43-44 (6 blok) | doğru | Arapça + Diyanet meali betikle birebir; Nahl 16:43-44 Diyanet'in birleşik girdisi, cite doğru. |
| Yûnus 10:36, Necm 53:28, Yûsuf 12:103, En'âm 6:148, Mâide 5:104, Bakara 2:170, Neml 27:64, Ankebût 29:14, Sebe' 34:13, Zümer 39:17-18 (metin içi) | doğru | Tırnaklı parçalar `kuran_tam.json` ile birebir. Yûsuf 12:103 ve Zümer 39:17-18'de Diyanet kaydı bitişik ayetle birleşik; alıntılanan parça doğru ayete ait. |
| Bakara 2:249 ("Nice az topluluk…") | düzeltildi | Metin sanki Allah söylüyormuş gibi sunuyordu; ayette bu söz "Allah'a kavuşacağını bilenler"e ait. "Kur'an ayrıca, Talût'un yanındaki ve Allah'a kavuşacağını bilen kişilerin şu sözünü aktarır" yapıldı. Meal metni doğru. |

## Hadisler
| Öğe | Durum | Not |
|---|---|---|
| Müslim 145 (Ebû Hureyre) "İslâm garip başladı" | doğru | ara/eng-muslim, Abdülbâkî no. 145; sahih. |
| Tirmizî 2629 (İbn Mes'ûd) | düzeltildi (cite) | Aynı lafız doğru; Tirmizî'nin kendi hükmü (hasen sahih garîb) eklendi. Elbânî ve Ahmed Şâkir sahih (API). |
| Tirmizî 2630 ("ıslah edenler" ilavesi) | doğru | Elbânî ve Şâkir: çok zayıf; Zübeyr Ali Zai: zayıf. Metinde "çok zayıf" yazıyor, üzerine bir şey kurulmamış. |
| Müslim 2623 (Ebû Hureyre) "İnsanlar helâk oldu" | düzeltildi | Numara, metin, Ebû İshak notu (ehlekehüm/ehlekühüm) Arapçasıyla doğru. Eklenen "Yani … tuzaktır" çıkarımı kaynaksızdı. Şerhlerde uyarının hedefinin halkı küçümseyip kendini üstün görenler (ikinci okuyuşta halkı Allah'ın rahmetinden ümitsiz bırakanlar) olduğu belirtildi (HadeethEnc şerhi); cümle buna göre yeniden kuruldu. |
| Buhârî 5752 (İbn Abbas) | doğru | Arapça ve İngilizce okundu: bir adam / iki adam / er-raht / hiç kimse; ufku kapatan kalabalık = Mûsâ ve kavmi; iki ufuk kalabalığı = ümmet; yetmiş bin hesapsız. Ellipsis'ler anlamı bozmuyor. |
| Buhârî 6541 | doğru | el-ümme (büyük topluluk) / en-nefer (küçük topluluk) / on / beş / yalnız. Sayılar SVG'de metne uygun. |
| Müslim 220 (Abdülbâkî; API 220.01) | doğru | Husayn b. Abdirrahman – Saîd b. Cübeyr yoluyla İbn Abbas; sahih. sunnah.com'a erişilmedi, API ile doğrulandı. |

## Âlim/kitap alıntıları
| Öğe | Durum | Not |
|---|---|---|
| Gazâlî, Munkız: "hakkı kişilerle tanırlar…" + Arapça | doğru | Arapça metin archive.org tahkik baskısında birebir. "s. 58" sayfa numarası doğrulanamadı, cite'tan çıkarıldı. |
| Ali'ye nispet edilen söz — kaynak durumu kutusu | doğru | Editör dipnotu ("Nehcü'l-Belâğa'da bulamadım; Ali'nin söz cinsinden") aynı baskıda aynen var. Kutu sözün hadis ya da delil olmadığını, Gazâlî'den başka açılmış kaynak bulunmadığını dürüstçe yazıyor. İyi. |
| Gazâlî, taklit bağı çözülmesi + çocukların dini | düzeltildi (küçük) | Arapça birebir doğru. Gazâlî çocuklar için Yahudileri de sayıyor; "Yahudi çocukları Yahudi" eklendi. İngilizce çeviriden aktarılan "Allah'ın rahmetini daraltmak / kalbe atılan nur" özeti Munkız bağlamıyla uyumlu. |
| Şâtıbî, el-İ'tisâm yarım kalan son fasıl | doğru | Fasıl ve editör notu archive.org metninde birebir; "yarım kaldı" iddiası editör dipnotuyla destekli. |
| İbn Kayyım, İ'lâm: "çokluğuna aldanma… icmâ, hüccet, sevâd-ı a'zam hak sahibi âlimdir" | düzeltildi (bağlam) | Arapça birebir doğru. Bağlam eksikti: "bu tür kimseler" delilsiz karşısındakini tekfir/tebdî' eden câhillerdir. Bu cümle eklendi. |
| İbn Mes'ûd sözü (Amr b. Meymûn) | doğru | İ'lâm metninde; bağlam (namazı geciktiren yöneticiler), Nuaym b. Hammâd şerhi doğru. Kaynak durumu kutusu dürüst: sahâbî sözü, senedi doğrulanamadı, ikincil iddialar etiketli. Kutu olduğu gibi bırakıldı. |
| Kurtubî (21:7) ve Sa'dî (21:7) | doğru | quran.com tefsir uç noktasında Arapça birebir; "bu bir ihtilaf yok" ve "sebebi özel ama genel" ifadeleri doğru. |
| Taberî, İbn Kesîr, Kurtubî (16:43) | doğru | Mekke müşriklerine hitap; ehl-i zikr görüşleri; "ve'l-ma'nâ mütekâribdir" Kurtubî'de var. |
| Sagan, Broca's Brain (1979) | doğru | Wikipedia (Galileo gambit): Kolomb, Wright kardeşler, Bozo; s. 64. Metin "özetlemiştir" diyor, kabul. |
| "argumentum ad populum" | doğru | "ad numerum" terimi kullanılmamış. |

## Olgusal/ehl-i sünnet, güncel örnek
- Güncel grup, parti, siyasi örnek yok (taranan terimler: parti, seçim, iktidar, mezhep/fırka adları vb.). "Şehirdeki oyların çoğu" ifadesi köprü hesabı benzetmesi, zararsız.
- Tekfir, fetva yok. Tarih verilmemiş.

## HTML sözleşmesi
- İzinsiz sınıf, `<script>`, dış URL, sabit hex renk, kapanmayan etiket: yok (ayrıştırıcıyla kontrol edildi).
- Şema 1'de çıplak ifade "Burhanını getir; bilgin var mı?" tırnak içindeydi ama ayetin kendi sözü değil, özet. Tırnak kaldırıldı.

## SVG (390px, açık/koyu)
- 3 şema (`b-03-sema1..3`, light/dark): taşma yok (`over` ve `svgTextTasma` boş), koyu temada okunur, hizalı. Değişiklik gerekmedi.

## Türkçe
- Küçük düzeltmeler yukarıda. Akıcı ve sade; baştan yazma yapılmadı.

## Kalan riskler
- İbn Mes'ûd ve Hz. Ali sözlerinin senedi doğrulanamadı; metin bunu açıkça söylüyor.
- Sagan alıntısı için doğrudan kitap sayfası değil Wikipedia kaydı kullanıldı (kitapta Fulton da sayılıyor; metin özet olduğundan sorun yok).
- HadeethEnc şerhi (ikincil) Müslim 2623 yorumu için kullanıldı; Nevevî'nin şerhine doğrudan ulaşılamadı.
