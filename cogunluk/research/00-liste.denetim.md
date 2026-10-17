# 00 — Paylaşılan Liste: Doğru mu? — DENETİM RAPORU

Denetçi: bağımsız denetim ajanı · Dosya: `bolumler/00-liste.html` · Saat: 14:09-14:20 UTC
Yöntem: `araclar/ayet_dogrula.py` (5 blok: 0 uyarı, önce ve sonra), `veri/kuran_tam.json` + `veri/ekser_ayetleri_tam.json` ile satır satır karşılaştırma, quran.com tefsir uç noktası (İbn Kesîr id 169, Taberî id 15), fawazahmed0 hadis API (ara/eng-muslim), islamweb ve hadithprophet sayfaları.

## 1. Ayet blokları (blockquote.ayet)
| Öğe | Durum | Not |
|---|---|---|
| Mâide 5:49 | doğru | Arapça + Diyanet birebir; atıf notu doğru (5:48'de "çoğu" yok, 5:49'da var). |
| Rûm 30:42 | doğru | Birebir. |
| Şuarâ 26:221-223 | doğru | Birebir; Diyanet 26:221, 222, 223 ayrı kayıt. |
| En'âm 6:116 | doğru (biçim düzeltildi) | Metin birebir; `En&#x27;âm` yazımı `En'âm` yapıldı. |
| Yûnus 10:36 | doğru | Birebir. |

## 2. Doğrulama tablosu: 14 satır `kuran_tam.json`'a karşı
Sure:ayet numarası, Arapça "çoğu" sözcüğü, Diyanet cümlesi ve "kimin hakkında" sınıflaması tek tek sınandı. 81 ayetlik listede 12 ayet ekser, 5:49 ve 10:92 kesîr (doğru).

| Liste | Gerçek atıf | Durum | Not |
|---|---|---|---|
| Nahl 83 | 16:83 | doğru | Metin tutuyor. Sınıflama (nimeti bilip inkâr edenler, "başkalarına kulluk ederek") İbn Kesîr'den (16:83) açıldı. |
| Mâide 48 | **5:49** | doğru (atıf hatası tespiti sağlam) | 5:48'de "çoğu" yok; 5:49 "İnsanların çoğu gerçekten fasıktırlar". İbn Kesîr 5:49: "the majority of humans are disobedient" ile "insanların çoğunluğu" okuması doğrulandı. |
| Rûm 42 | 30:42 | doğru | Sınıflama (geçmiş toplumlar): Taberî 30:42 ("mesâkine'llezîne keferû min kablikum", "li-hâulâi'l-müşrikîn min kavmik") birebir destekliyor. |
| İsrâ 89 | 17:89 | düzeltildi | Tablo alıntısı "İnsanların çoğu..." büyük harfle başlıyordu; ayetin gerçek cümlesi "Öyleyken insanların çoğu...". "«…insanların çoğu nankör olmakta direndiler.»" yapıldı. |
| Yûnus 92 | 10:92 | doğru | Metin ve kesîr tespiti doğru. |
| Şuarâ 223 | 26:223 | doğru | Taberî: "ve ekseru men tenezzelu aleyhi'ş-şeyâtîn kâzibûn" (şeytanların indiği kimselerin çoğu) ve Mücâhid rivayeti doğrulandı. |
| Yûnus 36 | 10:36 | doğru | Taberî 10:36: "ekseru hâulâi'l-müşrikîn" doğrulandı (bağlam 10:34-35 ortaklar). |
| Furkân 50 | 25:50 | düzeltildi | Alıntıdaki büyük harf ve eksik başlangıç ("Buna rağmen") nedeniyle "«…insanların çoğu nankörlükte direnmiştir.»" yapıldı. |
| Bakara 243 | 2:243 | doğru | Metin tam. İbn Kesîr 2:243: İbn Abbâs "Dâverdân, veba" rivayeti açıldı; ayetin kendisi yalnızca "ölüm korkusuyla çıkanlar" der. |
| Bakara 100 | 2:100 | düzeltildi | Alıntı "Onlar, her ne zaman..." cümlesinin ortasından başladığı hâlde büyük harfliydi; "«…her ne zaman bir ahidde…»" yapıldı. İbn Kesîr 2:100 "The Jews break their covenants" bölümü açıldı: doğru. |
| Zuhruf 78 | 43:78 | düzeltildi | Alıntı "And olsun ki, size..." kısmını atıyordu; "«…size gerçeği getirdik; fakat çoğunuz gerçeği sevmiyorsunuz.»" yapıldı. Taberî: "yâ ma'şera Kureyş" ve İbn Kesîr bölüm başlığı "The Punishment of the Doomed" doğrulandı. |
| Yûsuf 106 | 12:106 | doğru | İbn Abbâs rivayeti Taberî 19954 numarasıyla açıldı (Saîd b. Cübeyr yoluyla); İbn Kesîr'de Hasan-ı Basrî (münafık) ve Huzeyfe'nin ipi koparması (Urve rivayeti) doğrulandı. |
| Fussılet 4 | 41:4 | doğru | İbn Kesîr: "most of the Quraysh did not understand anything of it at all" doğrulandı. Diyanet 41:2-5'i birleştirir: not doğru. |
| Mâide 103 | 5:103 | doğru | Metin tam; İbn Kesîr: Saîd b. el-Müseyyeb'den Buhârî rivayetiyle bahîre/sâibe/vasîle/hâm açıklaması doğrulandı. |

Ek tablo notu: tefsir anılmayan satırlarda (17:89, 25:50, 2:243 bağlamı, 10:92) sınıflamanın ayetin kendi cümlesinden çıktığı metne yazıldı.

## 3. Şema (SVG) içeriği
| Öğe | Durum | Not |
|---|---|---|
| Küme sayıları (8 + 1 + 5) | doğru | Tablodaki satırlarla örtüşüyor. |
| Bakara 2:243 etiketi | düzeltildi | "Veba kaçkınlarının hikâyesinden sonra" ayetin değil bir rivayetin ifadesiydi; "Ölüm korkusuyla çıkanlardan sonra" yapıldı (genişlik taşmıyor). |
| Görsel denetim | doğru | 390 px açık ve koyu temada incelendi (`shots/c-00`): taşma yok, `svgTextTasma` boş, koyu temada okunur. İki şema da iyi hizalı. |

## 4. Hadis ve âlim alıntıları
| Öğe | Durum | Not |
|---|---|---|
| Müslim 2623 (Ebû Hüreyre) metin ve numara | doğru | fawazahmed0 ara/eng-muslim (hadithnumber 6683, arabicnumber 2623.01, kitap 45 hadis 180); islamweb'de de "2623" numarasıyla. Arapça "إِذَا قَالَ الرَّجُلُ هَلَكَ النَّاسُ فَهُوَ أَهْلَكُهُمْ" birebir. Derece: Sahîh-i Müslim'de, sahih. |
| Nevevî alıntısı (hadithprophet.com/hadith-52009) | doğru (cümle düzeltildi) | Arapça "واتفق العلماء على أن هذا الذم إنما هو فيمن قاله على سبيل الإزراء..." sayfada birebir (islamweb Şerhu Müslim c.16'da da). Türkçe cümle "bu yermenin ancak…" ağdalıydı: "bu yergi ancak … söyleyen kimse için geçerlidir diye görüş birliği ettiler" yapıldı. |
| Nevevî devamı (kınanan/kınanmayan) | doğru | "فأما من قال ذلك تحزنا..." aynı sayfada. |
| Taberî/İbn Abbâs (12:106) alıntısı | doğru | Taberî rivayet 19954: "من إيمانهم إذا قيل لهم: من خلق السماء؟... قالوا: الله. وهم مشركون". |
| İbn Kesîr, Hasan-ı Basrî, Huzeyfe (12:106) | doğru | API metninde mevcut. |

## 5. Olgusal ve sözleşme denetimi
| Öğe | Durum | Not |
|---|---|---|
| "Kesik üç numara" anlatısı | **çıkarıldı** | Metin "görselde sure numaraları kesikti (Bakara 24?, Yûnus 3?, Mâide 10?)" diyordu. Bunun kaynağı yok: BRIEF.md'de aktarılan görsel metni tam numaralıdır (Bakara 243, Yûnus 36, Mâide 103) ve bölümün kendi "Görseldeki yazı" listesi de tam numaraları gösteriyordu (iç çelişki). Gerçek durum: listede *sure numarası* yok, yalnızca ad + ayet numarası ("Nahl 83"). Paragraf buna göre yeniden yazıldı; üç satırdaki "Numara görselde kesikti." cümleleri, "üç kesik numara" ifadeleri ve "Aklında kalsın" maddesi düzeltildi. Doğrulanan olgular korundu: Bakara'da ekser geçen yalnızca iki ayet (2:100, 2:243), Yûnus'ta 30'lu ayetlerde tek isabet 10:36, Mâide'de 5:59 ve 5:103. **Ana ajan dikkatine:** görsel gerçekten kesikse bunu elle geri almak gerekir; ama BRIEF ile çelişir. |
| "Çoğu fasıktır" ayetinin kesîr ayrıntısı, 5:59 ve 3:110 notu | doğru | `ekser_ayetleri_tam.json` ve Diyanet metniyle örtüşüyor. |
| Duruş (tekfir yok, siyaset yok, fetva yok) | doğru | Sorun yok. |
| HTML sözleşmesi | doğru | `<script>` yok, dış URL yok (yalnızca `url(#b00-ok)` iç referansı), sabit hex renk yok, izinsiz sınıf yok, etiketler kapalı; 2008 kelime, 2 şema, "Aklında kalsın" var. |
| Türkçe | düzeltildi | Küçük akıcılık düzeltmeleri (yermenin cümlesi, kesik numara paragrafı). |

## Kalan riskler
- "Kimin hakkında" sınıflaması (8 / 1 / 5) bölümün tefsir okumasıdır; 12:106 ve 2:100 gibi ayetlerde âlimler farklı vurgu yapabilir (figcaption'da belirtilmiş).
- "Kesik numara" kararı yukarıdaki gerekçeyle verildi; görselin aslına bakılamadı.
- İbn Kesîr alıntıları quran.com'daki *kısaltılmış İngilizce* neşirden; bölüm bunu "İbn Kesîr" diye atıf yapıyor.
