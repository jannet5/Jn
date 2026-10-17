# Köken — Uç nokta kopya kâğıdı (SOURCES)

Motor işçisi için. Her şey **anahtarsız**. Sandbox'ta 2026-10-17 tarihinde `curl` ile denendi.
"CORS" sütunu = tarayıcıdan doğrudan `fetch` olur mu. Olmayanlar Android köprüsünden (`KokenNative`) gider.

## Özet tablo

| Kaynak | Sandbox'tan ulaşıldı mı | CORS | Android köprüsü gerekir mi | İş |
|---|---|---|---|---|
| en.wiktionary REST HTML `/api/rest_v1/page/html/{kelime}` | EVET (200, 98 KB, "kitap") | `*` (Wikimedia genel) | Hayır | Ana kaynak. Tüm sayfa HTML |
| en.wiktionary `action=parse` | 429 (sandbox IP'si sınırda; telefonda sorun yok) | `origin=*` ile açık | Hayır | Ana kaynak (tercih edilen) |
| en.wiktionary `opensearch` | EVET (200) | `*` | Hayır | Benzer kelime önerisi |
| tr.wiktionary `action=parse` | 429 (aynı sebep) | `origin=*` | Hayır | Türkçe "Köken" bölümü |
| Wikidata `wbsearchentities` (lexeme) | EVET (200) | `*` | Hayır | Yedek: kelime kimliği (L50461) |
| etimolojiturkce.com API | EVET (200, 1 deneme) | `*` | Hayır | **Türkçe köken, JSON, CORS açık** |
| Nişanyan Sözlük `/kelime/{k}` | EVET (200, 45 KB) | YOK | Evet | En iyi Türkçe köken. HTML'den ayrıştır |
| Nişanyan `/kelime/{k}/__data.json` | EVET (200, 11 KB) | YOK | Evet | Aynı veri, SvelteKit JSON |
| TDK `www.sozluk.gov.tr/gts?ara=` | EVET (200) — **www ile** | `*` | Hayır | Kısa köken: `lisan` alanı ("Arapça kitāb") |
| TDK `sozluk.gov.tr/gts` (www'suz) | HAYIR (bağlantı sıfırlandı) | — | — | Kullanma |
| Etymonline `/word/{k}` | EVET (200, 220 KB) | YOK | Evet | İngilizce kelimeler. Ayrıştırması zor |
| Google gtx çeviri | EVET (200) | `*` | Hayır | Çeviri (resmî olmayan) |
| MyMemory çeviri | EVET (200) | `*` | Hayır | Çeviri yedeği |

## 1. İngilizce Vikisözlük (ANA KAYNAK)

### 1a. `action=parse` (tercih edilen; JSON içinde HTML)

    GET https://en.wiktionary.org/w/api.php?action=parse&page=Ahmet&prop=text|sections&format=json&formatversion=2&origin=*&redirects=1&disablelimitreport=1&disableeditsection=1

- `origin=*` şart (CORS). `formatversion=2` ile `parse.text` düz string gelir.
- Başlık: tarayıcı `User-Agent` ayarlayamaz. Wikimedia kuralı: `Api-User-Agent: Koken/1.0 (iletisim)` ekle
  (bu başlık CORS ön-uçuş ister; Wikimedia izin verir; **sandbox'ta denenemedi**). Android köprüsünde normal `User-Agent: Koken/1.0 (iletisim)` yeter.
- Sadece Türkçe bölümü istersen: önce `prop=sections` al, `line=="Turkish"` olanın `index`'ini bul, sonra `&section=INDEX`.
- Yanıt kesiti: `{"parse":{"title":"Ahmet","pageid":..,"text":"<div class=\"mw-content-ltr mw-parser-output\">...</div>"}}`
- Hata biçimi: `{"error":{"code":"missingtitle","info":"..."}}` (HTTP 200 döner!). `status` değil `error` alanına bak.
- **Hız sınırı**: Sandbox'ta 6 isteği aynı anda atınca `429` + `retry-after: 32` geldi (gövde: "You are making too many requests to the API"). 
  Ortak çıkış IP'si olduğu için sonradan da 429 sürdü. Telefonda kişi başı IP olduğundan sorun beklenmez. Yine de: **eşzamanlı istek sayısı ≤2, 429'da `retry-after` kadar bekle, tek tekrar dene**.
  `opensearch` ve Wikidata aynı anda 200 verdi; sınır `parse` için daha sıkı görünüyor.

### 1b. REST HTML (parse 429 verirse YEDEK; sandbox'ta çalıştı)

    GET https://en.wiktionary.org/api/rest_v1/page/html/kitap     (Parsoid HTML, tüm diller tek sayfada)

- Başlıklar: `Accept: text/html` yeter. CORS: Wikimedia REST `access-control-allow-origin: *` verir.
- Büyük küçük harf duyarlı: `Ahmet` ≠ `ahmet`. Türkçe için küçük harf de dene (`kitap`).
- Yapı (gerçek kesit, "kitap"):

      <section data-mw-section-id="21"><h3>Etymology</h3>
      <div data-etytree-height="3"> ... <span class="etyl">Arabic</span> <i class="Arab mention" lang="ar">كِتَاب</i> ...
      <ul data-lang="tr" data-id="book" data-title="kitap" data-ety-tree-json='{ "id":"book", "children":[ ... ] }'></ul>
      <p><a>Inherited</a> from <a>Ottoman Turkish</a> <i class="Arab mention" lang="ota">كتاب</i>, borrowed from ...</p>

- Dil bölümleri: `<h2 id="Turkish">`. (Parsoid: `section` içinde `h2/h3`; `action=parse`: `<div class="mw-heading mw-heading2"><h2 id="Turkish">` ve kardeş öğeler düz sıralı.) İki biçime de dayanıklı yaz: `h2` bul, sonraki `h2`'ye kadar kardeşleri topla.
- Köken başlığı: `h3#Etymology`, `h3#Etymology_1`, `h3#Etymology_2` (birden çok köken). Yüksek çoğulluk: `h4`'te de olabilir.

### 1c. ÖNEMLİ KEŞİF — `data-ety-tree-json` (hazır zincir!)

Yeni Vikisözlük köken ağacını **JSON olarak HTML'e gömüyor**. Motor bunu ayrıştırırsa `chain` neredeyse bedava:

    ul[data-ety-tree-json]  → JSON.parse(attr)
    kök: {"lang_name":"Turkish","term":"kitap","lang":"tr","children":[
       {"keyword":"inherited","keyword_label":"[[Appendix:Glossary#inherited|Inherited]] from","terms":[
         {"lang_name":"Ottoman Turkish","term":"كتاب","lang":"ota","children":[
           {"keyword":"bor","keyword_label":"[[...|Borrowed]] from","terms":[
             {"lang_name":"Arabic","term":"كِتَاب","lang":"ar","children":[ {"keyword":"root", ... "term":"ك ت ب"} ]}]}]}]}]}

- `keyword`: `inherited`, `bor`, `der`, `lbor`, `cal`, `root` (→ sözleşmedeki `relation`). `is_invisible:"all"` olanı atla.
- Her etimolojide olmayabilir (yalnız `{{etymon}}` kullanan sayfalarda). Yoksa HTML paragrafındaki `span.etyl` + `i.mention[lang]` ile yedek ayrıştırma.
- Paragraf: `<p>` içinde `Inherited from Ottoman Turkish <i class="Arab mention" lang="ota">كتاب</i>, borrowed from Arabic ...`. Düz metin = `p.textContent`.
- Ek bilgi sınıfları: `span.etyl` (dil adı), `i.mention` (terim), `span.mention-tr` (çeviri yazı), `span.mention-gloss` (anlam), `abbr[title=loanword]`.

### 1d. Benzer kelime / öneri

    GET https://en.wiktionary.org/w/api.php?action=opensearch&search=kitab&limit=5&format=json&origin=*
    → ["kitab",["kitab","kitabu","kitabe","kitabevi","kitabça"],["","","","",""],["https://en.wiktionary.org/wiki/kitab",...]]

Gerçek çıktı budur (200, CORS `*`). Çekimli biçim için: sayfada `form of` / `inflection of` / `plural of` metni ara (örn. "kitaplar" → `<span class="form-of-definition">plural of kitap`) ve `a[href]` hedefine git (`via`).

## 2. Türkçe Vikisözlük

    GET https://tr.wiktionary.org/w/api.php?action=parse&page=kitap&prop=text&format=json&formatversion=2&origin=*&disablelimitreport=1

- Aynı hız sınırı ve CORS kuralları (denemede 429 aldı, içerik görülemedi → **ayrıştırma örneği yok, savunmacı yaz**).
- Türkçe sayfada bölüm başlığı genelde `Köken` (`h3`/`h4`, `id="Köken"`); dil bölümü `h2 id="Türkçe"`. Kaynaktan boş gelebilir; boşsa sessizce atla.

## 3. Wikidata Lexeme (yedek / kimlik)

    GET https://www.wikidata.org/w/api.php?action=wbsearchentities&search=kitap&language=tr&uselang=tr&type=lexeme&format=json&origin=*

Gerçek çıktı: `{"search":[{"id":"L50461","display":{"label":{"value":"kitap","language":"tr"},"description":{"value":"Türkçe, isim"}},"concepturi":"http://www.wikidata.org/entity/L50461",...}]}` (200, CORS `*`).
Köken için zayıf: Lexeme'lerde `P5191` (türetildiği) çoğu Türkçe kelimede boş. Detay: `https://www.wikidata.org/wiki/Special:EntityData/L50461.json` (denenmedi). **Yalnızca "bu kelime var mı?" ve öneri için kullan.**

## 4. etimolojiturkce.com API (BONUS — CORS açık Türkçe köken)

    GET https://api.etimolojiturkce.com/word/kitap

Gerçek yanıt (200, `access-control-allow-origin: *`, `content-type: application/json`):

    {"return":"1","word":"kitap","derivedLang":"Ar","explanation":"~ Ar kitāb كتاب  [#ktb msd.]  yazılı şey, belge, kitap &lt; Ar katb/kitāba ... ","firstInHistory":"...", ...}

- `explanation` Nişanyan'ın kısaltma gösterimini kullanıyor (`~` = ödünç, `<` = öncesi, `Ar` = Arapça, `Fa` = Farsça, `[#ktb]` = kök). HTML özel karakter içerir (`&lt;`, `&#8594;`) — çöz.
- Bulunamayınca: `{"return":"0", ...}` olması beklenir (denenmedi).
- Durum: `GET https://api.etimolojiturkce.com/status` → `{"status":"1","message":"Service is UP"}` (README'den).
- Kapsam ~14.000 Türkçe kelime. Kaynağı muhtemelen Nişanyan verisi → **lisans belirsiz; uygulamada "Kaynak: etimolojiturkce.com" yaz, yoğun trafik üretme.** Tarayıcıda da çalıştığı için PWA'da Türkçe için en kolay yol.

## 5. Nişanyan Sözlük (Android köprüsü; sandbox'tan ULAŞILDI)

Eski `/api/words/kitap?session=1` (nisanyan-cli'nin kullandığı) artık **404**. Site SvelteKit'e geçmiş. Çalışan yollar:

### 5a. Sayfa HTML — en kolay: `<meta name="description">`

    GET https://www.nisanyansozluk.com/kelime/pijama        (Türkçe harfler URL-kodla: /kelime/%C5%9Fapka)
    Başlıklar: User-Agent: <mobil Chrome>   (başlıksız da 200 verdi; Referer gerekmedi)

`<meta name="description" content="...">` doğrudan düz metin köken içerir. Gerçek çıktı ("pijama"):

    Fransızca pyjama “Hindistan’a özgü hafif, bol pantolon” sözcüğünden alıntıdır. Bu sözcük İngilizce aynı anlama gelen pyjama sözcüğünden alıntıdır. İngilizce sözcük Modern Hintçe pācāmā पाजामा veya paicāmā पैजामा “ayak giysisi, gevşek pantolon, şalvar” sözcüğünden alıntıdır. Bu sözcük Farsça aynı anlama gelen pācāma پاجامه veya pāy-cāma پای جامه sözcüğünden alıntıdır. Farsça sözcük Farsça pā پا ve Farsça cāma جامه sözcüklerinin bileşiğidir. Daha fazla bilgi için pa ve camadan maddelerine bakınız.

- Dil adlarını sırayla yakalayan regex ("Arapça", "Fransızca", ...) + `“…”` tırnaklı anlamlar → `chain` kolayca çıkar.
- Aynı sayfada `<title>`: `kitap - Nişanyan Sözlük`. Bulunamayan kelime: HTTP **404** (HTML, boş içerik).
- Gelişmiş: `<script>` içinde `__sveltekit_xxx.resolve(2, () => [{data:[{name:"kitap",etymologyNodes:[[{type:"bold",children:[{type:"text",text:"Arapça"}]}, ...]]` — JS nesnesi (JSON değil, `new Date(...)`, `void 0` var). Ayrıştırma pahalı; **meta description yeter**.

### 5b. `__data.json` (daha sağlam)

    GET https://www.nisanyansozluk.com/kelime/kitap/__data.json
    Content-Type: text/sveltekit-data   (satır satır JSON; ilk satırda "seo":{"title":"kitap","description":"Arapça √ktb kökünden gelen kitāb ..."})

İlk satırı `JSON.parse` et → `nodes[1].data` içinde `description` metni var (devalue dizisi: değerler indeks ile referanslı). Pratik: satır 1'de `"description":` sonrası dizgiyi bul.

- CORS: 200 yanıtlarda `access-control-allow-origin` **yok** (sadece 404 sayfasında var) → tarayıcıdan okunamaz. **Android köprüsü şart.**
- Hız sınırı (yanıt başlığı): `ratelimit-policy: "entry-10s";q=30;w=10, "entry-900s";q=600;w=900, "entry-86400s";q=3000;w=86400` → 10 sn'de 30, 15 dk'da 600, günde 3000 istek. Cloudflare önünde; bot gibi görünürse zorlayabilir → **sorgu başına en çok 1 istek, önbellekle.**
- Arama uçları denendi: `/ara?q=` → 404. Kelime bulunamazsa önce **kök sözcük**ü (Vikisözlük'ten) ya da küçük harf dene.

## 6. TDK Güncel Sözlük (kısa köken, CORS açık)

    GET https://www.sozluk.gov.tr/gts?ara=kitap        ← **www ile**. www'suz `sozluk.gov.tr/gts` bağlantıyı sıfırladı.

Gerçek yanıt (200, `Access-Control-Allow-Origin: *`, JSON dizisi):

    [{"madde_id":"27874","madde":"kitap","lisan":"Arapça kitāb","lisan_html":"Ar. kitāb","cesit_say":"6","anlam_say":"3",
      "birlesikler":"kitap açacağı, kitap cebi, ...","anlamlarListe":[{"anlam":"...", ...}], ...}]

- Köken için yalnız `lisan` ("Arapça kitāb") — TDK etimoloji vermez, sadece kaynak dil. Boş string = öz Türkçe veya belirtilmemiş.
- Bulunamayan: `{"error":"Sonuç bulunamadı"}` (HTTP 200, nesne!). Dizi mi nesne mi diye kontrol et.
- Başlık gerekmedi. `tdk-cli` ve `tdk-py` `User-Agent: Mozilla/5.0` kullanıyor (güvenli olsun diye ekle).
- `autocomplete.json`, `/oneri`, `/gts_id` yanıtı SPA HTML döndü (kullanışsız). `gts_id?id=` boş.
- Kelime ailesi: `birlesikler` (birleşik sözcükler) → "Parçalar" için ipucu.

## 7. Etymonline (İngilizce kelimeler; zayıf öncelik)

    GET https://www.etymonline.com/word/pajamas      Başlık: User-Agent: <mobil Chrome>

- 200, 220 KB, Cloudflare. Next.js (app router). `__NEXT_DATA__` **yok**. CORS yok → köprü şart.
- Kolay veri: `<meta name="description">` = kısa ve **kesik** özet ("loose trousers tied at the waist," worn by Muslims in India and adopted by Europeans… ). Tam metin `self.__next_f.push([1,"..."])` (RSC) parçalarında; bu denemede "1800s" metnini bulamadım → **ayrıştırma güvenilmez**.
- Öneri: yalnız yedek; başarısızsa `sources[]`'a `ok:false` yaz. İki aşamalı arama: `https://www.etymonline.com/search?q=pajamas`.
- Sıkça değişen siteyi ana akışa koyma.

## 8. Çeviri uçları (anahtarsız)

### 8a. Google gtx (resmî olmayan, çok hızlı, CORS açık)

    GET https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=tr&dt=t&q=From%20Arabic%20book

Gerçek yanıt (200, `access-control-allow-origin: *`): `[[["Arapça kitaptan","From Arabic book",null,null,3,...]],null,"en",...]`
→ `json[0].map(s=>s[0]).join('')`. Uzun metin için parçala (≈ 1500 karakter), `POST` ya da `q` kodla. Kota belgesiz; çok sorguda 429 olabilir → MyMemory'ye düş.

### 8b. MyMemory

    GET https://api.mymemory.translated.net/get?q=Hello%20world&langpair=en|tr

Gerçek yanıt (200, CORS `*`): `{"responseData":{"translatedText":"Merhaba dünya","match":1},"quotaFinished":false,"responseStatus":200,...}`
- Sınır: `q` en çok **500 bayt**; anahtarsız günde ≈ 5000 karakter (`quotaFinished:true` gelince bırak). `responseStatus` 200 değilse hata.
- Çeviri sırası önerisi: gtx → MyMemory → reddet.

## Savunmacı yazım için hatırlatmalar

1. Her kaynak `try/catch` + zaman aşımı (12 sn); hata `sources[].note`.
2. Wikimedia `429`: `retry-after` oku, en çok bir kez yeniden dene; REST HTML'e düş.
3. Nişanyan/TDK/Etymonline: yalnız `KokenNative` varsa dene (TDK ve etimolojiturkce tarayıcıda da açık; istersen oradan da dene).
4. Çekimli biçim → kök: Vikisözlük `form of` metninden; Nişanyan 404 verirse kök ile tekrar.
5. TDK hata nesnesi (`{"error":...}`) ve Vikisözlük `{"error":...}` HTTP 200 ile gelir.
6. Aynı kelime için sonuçları önbelleğe al (sayfa ömrü boyunca) → hız sınırlarına takılma.
