# 03 — Ölüm ve Berzah: bağımsız denetim

Denetçi: bağımsız denetim ajanı. Ayetler api.quran.com (uthmani + Diyanet id 77) ile birebir karşılaştırıldı (betikle, 20 `blockquote.ayet`). Hadisler fawazahmed0/hadith-api JSON'larından (eng-bukhari, eng-muslim, eng-nasai, eng-tirmidhi, eng-abudawud, eng-malik) açıldı. Âlim alıntıları alim.org (İbn Kesîr), nurpedia.org, islamqa.info, islamansiklopedisi.org.tr sayfalarından sınandı. sunnah.com ve dorar.net 403 verdi.

## Ayetler

| Öğe | Durum | Not |
|---|---|---|
| 20 ayet bloğu (3:185; 62:8; 6:61-62; 32:11; 67:2; 75:26-30; 56:83-85; 50:19+22; 41:30-32; 16:32; 8:50; 39:42; 23:99-100; 17:85; 14:27; 40:46; 63:10-11; 89:27-30; 2:155-157; 23:101) | doğru | Arapça ve Diyanet metni API ile birebir aynı; sure adı ve numaraları doğru. Bitişik ayetleri tek metin veren Diyanet kaydı aralık olarak yazılmış. |
| Kâf 50:19 ve 50:22 | düzeltildi | Arada 50:20-21 atlandığı hâlde "…" yoktu; hem Arapça hem Türkçe metne "…" eklendi. |
| Kâf 50:22 yorumu | düzeltildi | Metin "ölüm geldiğinde perde kalkıyor" diyordu. İbn Kesîr 50:22'yi diriliş günü için yorumlar (50:20 Sûr, 50:21 sürücü ve şahit sonrası). Paragraf buna göre yeniden yazıldı. |
| Mü'min 40:46 | doğru | Meal metnindeki `sokun"denir` bitişikliği API'de de böyle; "aynen" kuralı gereği korundu. |
| Metin içi atıflar: Rahmân 55:19-20, Furkân 25:53 (berzah = iki deniz arası engel), Bakara 2:154, Âl-i İmrân 3:169 | doğru | Berzah kullanımı TDV "Berzah" maddesinde de geçiyor; şehit "diri" ayetleri doğru. |

## Hadisler

| Öğe | Durum | Not |
|---|---|---|
| Buhârî 1338, 1374 (Enes; kabirde iki melek) | doğru | Metin ve numara tuttu. Buhârî'de "bilmiyorum, insanların dediğini derdim" var. |
| Müslim 2870 (Enes; Katâde'nin yetmiş arşın ilavesi) | doğru | Katâde'ye atfı metinde korunmuş. |
| Buhârî 1379; Müslim 2866 (İbn Ömer; sabah akşam makam) | doğru | |
| Müslim 1887 (Mesrûk, İbn Mes'ûd; şehit ruhları) | doğru | Yalnız şehitler için verilmiş. |
| Nesâî 2073 (Ka'b b. Mâlik; mümin ruhu kuş gibi) | düzeltildi (derece notu) | Elbânî, Ebû Gudde, Zübeyr Alî Zay: sahih; Muvatta 569 (Hilâlî sahih). İngilizce metindeki "(Da'if)" ibaresi çeviri/yayın artığı. Arnaut'un Zâdü'l-Meâd tahrîcinde isnadı sahih bulduğu bir arama sonucuyla teyit edildi; dipnota eklendi. |
| Müslim 1631 (Ebû Hureyre; üç şey) | doğru | |
| Buhârî 6507; Müslim 2684 (Allah'a kavuşmayı sevmek) | doğru | Buhârî'de "Âişe veya hanımlarından biri" geçiyor; Müslim'de Âişe. Metin Buhârî'ye uygun. |
| Tirmidhî 2307 (ölümü çok anın) | doğru | Elbânî ve Ahmed Şâkir: hasen-sahih. |
| Buhârî 5671 (ölümü temenni etmeyin) | doğru | |
| Tirmidhî 1071 (Münker–Nekîr, gelin gibi uyu) | doğru | Elbânî, Şâkir: hasen. Münker–Nekîr adı yalnız bu rivayette. |
| Ebû Dâvûd 4753 (Berâ b. Âzib) | doğru, açıklama eklendi | Elbânî sahih (Zübeyr: hasen). Üç soru (Rabbin, dinin, peygamber) Buhârî/Müslim'de değil, bu rivayette geçiyor. Şema altındaki kaynak satırı ve metne bir cümle eklendi, sahne "Berâ b. Âzib'in rivayetine göre" diye netleştirildi. |
| Müslim 2871 (14:27'nin kabir sorusu hakkında indiği) | doğru | |
| Müslim 2867 (kabir azabından sığınma) | doğru | |
| Ahmed 18534 (Berâ hadisinin uzun metni) | doğrulanamadı (ihtiyatlı ifade korundu) | Doğrudan açılmadı; araştırma notundaki takhrîc kaydına (tohed.com) dayanıyor. Metinde yalnız "isnadı sahih diyenler vardır" deniyor. |
| Buhârî 1303 (İbrahim'in vefatı) | doğru | |
| Müslim 918 (Ümmü Seleme; musibet duası) | doğru | |
| Tirmidhî 2460 (kabir cennet bahçesi/cehennem çukuru) | doğru (kullanılmamış) | Şâkir ve Elbânî "çok zayıf", Zübeyr "zayıf"; metinde doğru biçimde zayıf diye uyarı yapılmış, delil olarak kullanılmamış. |

## Âlim ve kitap alıntıları

| Öğe | Durum | Not |
|---|---|---|
| İbn Kesîr, Fussılet 41:30 ("korkmayın/üzülmeyin") | düzeltildi (künye) | "Korkmayın = âhirette karşılaşacağınızdan" Mücâhid, İkrime, Zeyd b. Eslem'e atfediliyor; "üzülmeyin" kısmı için isim verilmiyor. Künye buna göre düzeltildi. Metin özet olarak işaretli. |
| İbn Kesîr, Mü'minûn 23:100 (Mücâhid; Muhammed b. Ka'b) | doğru | Mücâhid'in sözü birebir; Muhammed b. Ka'b'ın yiyip içme ve karşılık görme ifadesi sayfada var. |
| İbn Kesîr, Vâkıa 56:85 ("Biz ona sizden daha yakınız") | düzeltildi | İbn Kesîr "Biz"i "meleklerimiz" diye açıklıyor. Metin ve özet maddesi "Allah daha yakın" diye kesin konuşuyordu; yorum farkı yazıldı, özet maddesi ayet metnine bağlandı. "Bazı yorumlar Allah'ın ilmi ve kudretine bağlar" ifadesi doğrulanamadı, adı verilmeden genel bırakıldı. |
| Said Nursî, Mektubat ("Mevt idam değil… terhistir, bir tebdil-i mekândır") | düzeltildi (küçük) | nurpedia: 20. Mektup (Yedinci Kelime). Sayfadaki yazım "fena" (metinde "fenâ"), "Belki bir…" virgülsüz; birebir uyumlu hâle getirildi. Künye "Mektubat" olarak bırakıldı. İkincil kaynak, birincil baskı açılamadı. |
| Said Nursî, Sözler 17. Söz ("Kabir ise zulümatlı bir kuyu ağzı değil…") | doğru | nurpedia sayfası 17. Söz'e atfediyor. Birincil baskı açılamadı. |
| İbn Kayyım, Kitâbü'r-Rûh (ruhun beden ilişkisi beş hâl) | doğru | islamqa.info/6090 (Kitâbü'r-Rûh s. 44): rahim, doğum, uyku, berzah, kıyamet. Metinde özet olarak işaretli. |
| TDV İslâm Ansiklopedisi, "Berzah" (tanım; kelâm tartışması; misâlî beden) | doğru | Üç ifade de madde metniyle uyumlu. |

## Olgusal iddialar ve inanç çizgisi

| Öğe | Durum | Not |
|---|---|---|
| Kabir sorusu, nimeti, azabı haktır (Ehl-i sünnet) | doğru | Keyfiyet gayb diye çerçevelenmiş. |
| Teveffî = eksiksiz teslim alma | doğru | |
| Uyku ve ölüm (39:42), "ruhun ayrı gerçekliği" | doğru | Yorum olduğu belirtilmiş. |
| Sûr'a üfürülme sonrası "perde tamamen kalkar" (şema ve metin) | düzeltildi | 23:101 bunu söylemiyor; "berzah sona erer" yapıldı. |
| Uyku paragrafındaki "bırakılır / geri gönderilmişizdir" | düzeltildi | Ayete uygun ("Allah ruhumuzu alır… salıverilmiş") olarak yeniden yazıldı. |
| Tarih verme, uydurma tasvir, rüya/kerâmet | yok | Temiz. |

## HTML sözleşmesi

Tek `section.bolum#b-03`; izinsiz sınıf, `<script>`, dış URL ve sabit hex renk yok; etiketler dengeli (ayrıştırıcıyla sınandı); SVG renkleri yalnız CSS değişkeni; marker kimlikleri benzersiz (b03-ok, ok2, ok3). Bölümde 3 `figure.sema` var.

## SVG görsel denetimi

build.py (düzeltilmiş birleştirme) ile derlendi, qa-bolum.js ile 390px açık ve koyu temada 3 şema PNG olarak alındı ve gözle kontrol edildi (değişiklik sonrası yeniden alındı). Taşma, çakışma, kesilme, koyu temada okunmayan metin yok; QA `over` ve `svgTextTasma` boş. Şema 3'ün kaynak satırı Berâ rivayetini ayıracak biçimde güncellendi, sığıyor.

## Türkçe kalite

Küçük düzeltmeler: "Ölüm bir ayrılık değilse bile bir uzaklıktır" cümlesi sadeleştirildi ("Ölüm yok oluş değil; ama bir ayrılıktır ve ayrılık acıtır"); uyku paragrafı düzeltildi (yukarıda).

## Kalan riskler

- Bölüm ~4000 kelime; BRIEF'in 2000–3500 hedefinin üstünde (içerik dolgu değil, kesilmedi).
- Nursî alıntıları ikincil sitelerden doğrulandı; birincil baskı sayfası açılamadı.
- Ahmed 18534 numarası doğrudan açılmadı.
- sunnah.com ve dorar.net 403 verdi; Nesâî 2073 derecesi JSON'daki üç muhaddis hükmü ve bir arama sonucuyla teyit edildi.
- "Bazı yorumlar yakınlığı Allah'ın ilmine ve kudretine bağlar" genel ifadesi tek kaynakla (İbn Kesîr) sınanmadı.
