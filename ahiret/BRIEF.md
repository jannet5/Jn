# ORTAK TALİMAT (tüm bölüm yazarları için)

## Proje
Türkçe, görsellerle desteklenmiş, kitap gibi okunan tek sayfalık bir site: **"İnsanın Yolculuğu"** — Ben kimim, niçin buradayım, ölüm, kabir/berzah, kıyametin kopuşu, diriliş, mahşer, hesap, mizan, sırat, cennet, cehennem ve rahmet.

İstek sahibinin kendi sözleri (özet): *"İnsanlar cennetin nasıl olduğunu, hesap gününün nasıl geçeceğini kafasında canlandıramıyor; hayal gibi geliyor. Görebilselerdi, sindirebilselerdi çok farklı olurdu. 'Ben niye bu dünyadayım, ne anlamı var?' diye soruyorlar, cevaplayamıyorlar. Bunları anlatan, tamamen sindirebileceğimiz bir şey yap. Yüzeysel blog yazıları değil; kitaplardan, büyük âlimlerin eserlerinden, derinlemesine araştır. Kaç sayfa olursa olsun, yeter ki anlayalım."*

Sen bu sitenin **tek bir bölümünü** hem araştırıp hem yazacaksın. Diğer bölümleri aynı anda başka ajanlar yazıyor.

## Zaman
Her adımın başında ve arasında `date -u +%H:%M` ile saati kontrol et. Sana verilen saatlerde (UTC) dur. 10 dakika somut ilerleme yoksa yöntemi değiştir (başka arama motoru, başka kaynak, başka dil). "Bulamadım" deyip bırakma: Google/Bing/DuckDuckGo'yu WebFetch ile dene (`https://www.google.com/search?q=...`, `https://www.bing.com/search?q=...`), İngilizce/Arapça/Türkçe anahtar kelimeleri değiştir, archive.org tam metinlerini ara.

## ADIM 1 — DERİN ARAŞTIRMA
Önce araçları yükle: `ToolSearch` ile `select:WebSearch,WebFetch` (WebSearch `mode` ister: zor/kaynak aramalarında `extended`, basitlerde `standard`).

Yüzeysel SEO blogları yetmez. Birincil ve klasik kaynaklara in:
- **Kur'an (kesin kaynak):** Arapça: `curl -s "https://api.quran.com/api/v4/quran/verses/uthmani?verse_key=51:56"`; Türkçe (Diyanet İşleri meali, id 77): `curl -s "https://api.quran.com/api/v4/quran/translations/77?verse_key=51:56"`. Bütün sureyi `?chapter_number=56` ile alıp python ile süz. Meal metnini API'deki haliyle AYNEN kullan (içindeki `<sup ...>` dipnot etiketlerini temizle). Meal metnini kendi kelimelerinle değiştirme.
- **Hadis:** `https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions.json` mevcut baskıları listeler; `.../editions/ara-bukhari.json`, `eng-bukhari`, `eng-muslim`, `ara-muslim`, `eng-tirmidhi`, `eng-abudawud`, `eng-ibnmajah` gibi dosyalardan numara + metin doğrula. `sunnah.com`, `dorar.net` (derece), `islamqa.info`, `hadithtransmitters`, Diyanet hadis kaynakları için WebFetch dene (sunnah.com curl'e kapalı, WebFetch ile dene). Derece: Elbânî / Arnaut / Dorar / Diyanet — yaz.
- **Klasik eserler (PDF / tam metin):** archive.org (`https://archive.org/advancedsearch.php?q=...&fl[]=identifier&rows=10&output=json` sonra `https://archive.org/download/<id>/<dosya>` ile tam metin / djvu.txt), shamela.ws, islamweb, waqfeya, Vikikaynak, risale.online (Said Nursi: Sözler, Mektubat, Lem'alar, Şuâlar), İslâm Ansiklopedisi (islamansiklopedisi.org.tr), Elmalılı Hamdi Yazır (Hak Dini Kur'an Dili) tefsiri. Eserler: Gazâlî (İhyâ, Kimyâ-yı Saâdet, ed-Durretü'l-Fâhire), İbn Kayyım (Hâdi'l-Ervâh, Kitâbü'r-Rûh, Miftâhu Dâri's-Saâde), Kurtubî (et-Tezkire), İbn Kesîr (en-Nihâye, tefsir), Süyûtî (Şerhu's-Sudûr), Said Nursî, Seyyid Kutub (Zılâl, et-Tasvîru'l-Fennî fi'l-Kur'ân), Râgıb el-İsfahânî, Mevlânâ, Yunus Emre; modern: Yasir Qadhi, Omar Suleiman, Nouman Ali Khan, Hayrettin Karaman, Mustafa İslamoğlu vb. ders/konuşmaları.
- **Forum / YouTube / x.com / Reddit / blog:** sadece insanların *gerçekte ne sorduğunu* ve âlimlerin nasıl cevapladığını görmek için kullan. Otorite olarak ALINTILAMA.
- En az 3 farklı arama açısı kullan (Türkçe, İngilizce, Arapça). Bir eserden alıntı yapacaksan o sayfayı/PDF'i gerçekten AÇ ve oradan oku.

## DÜRÜSTLÜK KURALLARI (sert)
1. Hafızadan ayet, hadis, âlim sözü "uydurma / yeniden kurma" YASAK. Her ayet API'den, her hadis gerçekten açtığın bir koleksiyondan (kitap + numara + derece), her âlim alıntısı gerçekten açtığın bir sayfadan (URL) gelecek. Doğrulayamadıysan koyma ya da "doğrulanamadı" diye açıkça etiketle.
2. Hadis iddiası olarak sadece **sahih/hasan** olanları kullan. Meşhur ama aslı olmayan/zayıf sözleri (ör. "Ben gizli bir hazineydim…", "Dünya ahiretin tarlasıdır", "Ölmeden önce ölünüz") kullanacaksan mutlaka "zayıf / aslı yok" etiketiyle ve gerekçesiyle ver — kullanmadan önce her birini kontrol et.
3. Âlimler ayrılıyorsa kısaca belirt. Ana çizgi Ehl-i Sünnet. Kıyamet için tarih verme. Fetva verme, tekfir yok, mezhep kavgası yok, rahmetsiz korkutma yok.
4. Gayb haberleri sadece vahiyle bilinir. Tasvirleri "vahyin bize anlattığı kadarıyla" ver; gerçeğin hayalimizi aştığını (Secde 32:17; "hiçbir göz görmemiş, hiçbir kulak işitmemiş…" — kaynağını doğrula) belirt. Rüya, kerâmet hikâyesi, uydurma anekdot delil olarak kullanılmaz.
5. Peygamber ve melek resmi/tasviri yok.
6. Hz. Peygamber için "Hz. Muhammed (s.a.v.)", Allah için "Allah (c.c.)" kısaltması yeter; peygamberlerde "(a.s.)".

## ADIM 2 — NOTLARI KAYDET
`/home/user/Jn/ahiret/research/<NN>-<slug>.md` (Türkçe). Bölümler: Özet · Doğrulanmış ayetler (ref | Arapça | Diyanet meali) · Doğrulanmış hadisler (TR anlam | kaynak+no | derece | nasıl doğrulandı) · Âlim/kitap alıntıları (yazar, eser, TR çeviri, URL) · Canlı sahne/benzetme fikirleri · Zayıf/tartışmalı notlar · Açılan kaynak URL'leri.

## ADIM 3 — BÖLÜMÜ YAZ
Çıktı: `/home/user/Jn/ahiret/bolumler/<NN>-<slug>.html` — **HTML parçası** (`<html>/<head>/<body>` YOK), tek bir `<section class="bolum" id="b-<NN>">`.
- Uzunluk: 2000–3500 kelimelik GERÇEK içerik (dolgu yok). `h3` alt başlıklar, kısa paragraflar.
- Dil: sade, sıcak, akıcı Türkçe; ağdalı dil yok. Arapça/İslami terimi ilk geçtiğinde sade Türkçe açıkla. "Biz" dili + sahnelerde "gözünü kapat" tarzı ikinci tekil. Sıkıcı ansiklopedi değil; ama laf kalabalığı da değil. Okuyan, anlatılanı gözünde canlandırabilmeli, sindirebilmeli. Hem korku hem ümit dengesi (havf-recâ).
- Her şey gerçekten *neden/nasıl* sorusuna cevap versin: sadece listelenmiş maddeler değil, bağlantılı bir anlatı.
- Bölümde en az **2 özgün inline SVG şeması** (`figure.sema`) olsun: gerçekten bir şey anlatsın (zaman çizelgesi, ölçek karşılaştırması, sahne, akış, karşılaştırma tablosu şeması…). Kalite çıtası: düzgün hizalı, Türkçe etiketli, 340px genişlikte okunur. `viewBox` genişliği 480–560, metin ≥16 birim, yığılmış/dikey yerleşim tercih et. Renkler sadece CSS değişkenleriyle: `style="fill:var(--accent)"`, `stroke:var(--line)`, `var(--ink)`, `var(--ink-soft)`, `var(--gold)`, `var(--night)`, `var(--sky)`, `var(--surface)`, `var(--danger)`, `var(--bg)`. Metinlerde `style="font-family:inherit"`. Sabit hex renk KULLANMA (koyu/açık tema uyumu için), istisna: gece gökyüzü gibi bilinçli sahne zeminleri için `var(--night)` kullan. SVG'ye `role="img"` ve `aria-label` ekle. Dış görsel/URL KULLANMA (fotoğrafları editör ekleyecek).
- Ayetler Arapça + Diyanet meali + ref ile; hadisler kaynak+no+derece ile. Metin içi kısa atıf: (Buhârî 6416).

### HTML bileşen sözleşmesi (stil dosyası bunları tanır — AYNEN bu sınıfları kullan)
```html
<section class="bolum" id="b-03">
  <header class="bolum-baslik">
    <span class="no">03</span>
    <h2>Başlık</h2>
    <p class="ozet">Tek cümlelik alt başlık</p>
  </header>
  <p class="giris">Giriş paragrafı (büyük punto)</p>
  <h3>Alt başlık</h3>
  <p>Normal paragraf. <strong>vurgu</strong>, <em>italik</em></p>

  <blockquote class="ayet">
    <p class="ar" lang="ar" dir="rtl">…Arapça…</p>
    <p class="tr">…Diyanet meali (API'deki gibi)…</p>
    <cite>Zâriyât 51:56 · Diyanet İşleri meali</cite>
  </blockquote>

  <blockquote class="hadis">
    <p class="tr">…hadisin Türkçe anlamı…</p>
    <cite>Buhârî 6416 · Sahih</cite>
  </blockquote>

  <blockquote class="alim">
    <p>…âlim/kitap alıntısı (açtığın kaynaktan)…</p>
    <cite>İmam Gazâlî — İhyâ, Zikrü'l-Mevt</cite>
  </blockquote>

  <div class="sahne">
    <p class="sahne-etiket">Gözünü kapat…</p>
    <p>…sürükleyici sahne anlatımı (yalnızca vahyin bildirdiği unsurlara dayanarak)…</p>
  </div>

  <aside class="not"><strong>Başlık</strong><p>…</p></aside>
  <!-- varyantlar: class="not dikkat" (uyarı/zayıf rivayet), class="not umut" (rahmet/ümit), class="not dusun" (düşünme sorusu) -->

  <figure class="sema">
    <svg viewBox="0 0 520 420" role="img" aria-label="…">…</svg>
    <figcaption>…</figcaption>
  </figure>

  <details class="detay"><summary>Daha derine in: …</summary><p>…</p></details>

  <div class="kutular">
    <div class="kutu"><h4>Başlık</h4><p>…</p></div>
    <div class="kutu"><h4>Başlık</h4><p>…</p></div>
  </div>

  <ul>…</ul> <ol>…</ol>   <!-- düz listeler serbest -->

  <div class="ozetle"><h3>Aklında kalsın</h3><ul><li>…</li></ul></div>  <!-- bölüm sonu, 3–5 madde -->
</section>
```
Başka sınıf/inline stil icat etme (SVG içi hariç). `<script>` YOK. Tablo gerekiyorsa düz `<table>` kullan (stil dosyası biçimlendirir).

## ADIM 4 — DÖNÜŞ
Sadece kısa rapor (<150 kelime): dosya yolları, kelime sayısı, kullanılan ayet/hadis/alıntı sayısı, açık kalan boşluklar. Başka bir şey yazma.
