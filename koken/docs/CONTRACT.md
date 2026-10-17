# Köken — Sözleşme (tüm parçaların uyduğu tek kaynak)

Köken: telefonda bir kelime yazılır, uygulama internete kendisi bağlanıp kelimenin
KÖKENİNİ bulur (hangi dilden geldi, anlamı, parçaları, yol haritası). Arkada sunucu YOK,
API anahtarı YOK, kullanıcıdan hesap YOK. İnterneti olan herkes kullanır.

## Klasörler (herkes SADECE kendi klasörüne yazar)
- `koken/web/`      → motor + arayüz (düz HTML/CSS/JS, derleme adımı yok)
    - `etym.js`     → motor (UMD: tarayıcıda `window.Etym`, Node'da `module.exports`)  [MOTOR işçisi]
    - `index.html`, `app.js`, `style.css`, `manifest.webmanifest`, `sw.js`, `icons/*` [ARAYÜZ işçisi]
- `koken/android/`  → Android kabuğu (WebView + yerel HTTP köprüsü)                   [ANDROID işçisi]
- `koken/tests/`    → Node + Playwright testleri (her işçi kendi alt klasörü: tests/engine, tests/ui, tests/android)
- `koken/docs/`     → araştırma notları (ARAŞTIRMA işçileri)
- Git komutu ÇALIŞTIRMAYIN (commit'i ana oturum yapar).

## Ağ katmanı (CORS sorunu için)
Motor ağa SADECE `http` fonksiyonu üzerinden çıkar:

    http(url, {headers?, timeoutMs?}) -> Promise<{status:number, text:string, headers?:object}>

- Tarayıcıda varsayılan: `fetch()` (Vikisözlük `origin=*` ile CORS'a izin verir).
- Android uygulamasında: `window.KokenNative` varsa HERŞEY ona gider (CORS yok, tarayıcı gibi User-Agent):
    - Java tarafı:  `@JavascriptInterface void get(String id, String url, String headersJson, int timeoutMs)`
    - Bitince Java şunu çağırır: `window.__kokenNativeDone(id, status, bodyBase64)`  (gövde UTF-8, base64)
    - JS tarafı `Etym.makeHttp()` bu köprüyü saran fonksiyonu döndürür (yoksa fetch).
- Test için `lookup(word, {http: fakeHttp})` ile ağ taklit edilebilir.

## Motor API
    Etym.lookup(word, opts?) -> Promise<Result>
    Etym.makeHttp() -> http fonksiyonu
    Etym.translate(text, 'tr', opts?) -> Promise<string>   // isteğe bağlı çeviri (anahtarsız uçlar; hata olursa reddet)
    opts = { http?, signal?, onProgress?(msg:string), timeoutMs?:number(varsayılan 12000) }

    Result = {
      query: string,                 // kullanıcının yazdığı
      resolved: string,              // bulunan sayfa başlığı (ör. "Ahmet")
      found: boolean,
      via: string|null,              // ör. "kitaplar → kitap (çoğul biçim, kök sözcüğe gidildi)"
      suggestions: string[],         // bulunamazsa benzer kelimeler
      entries: Entry[],              // dile göre, Türkçe önce, sonra İngilizce, sonra diğerleri
      sources: Source[],             // hangi kaynak denendi, sonuç
      fetchedAt: string              // ISO
    }
    Entry = {
      language: string,              // "Turkish"
      languageTr: string,            // "Türkçe"  (bilinen ~60 dil için sözlük; yoksa language)
      pos: string[],                 // ["noun"]  (varsa)
      etymologies: Etymology[],      // 1+ (Etymology 1/2... ayrı olabilir)
      url: string                    // https://en.wiktionary.org/wiki/Ahmet#Turkish
    }
    Etymology = {
      text: string,                  // düz metin, tek paragraf ("From Arabic أَحْمَد (ʾaḥmad)...")
      chain: ChainNode[],            // EN YAKIN atadan en eskiye: [{Türkçe}, {Arapça}, {Proto-Sami}...]
      parts: Part[],                 // parçalara ayrılış (ek/kök/birleşik) — varsa
      cognates: {language:string, term:string}[],   // akraba sözcükler — varsa
      gloss: string|null             // ana anlam
    }
    ChainNode = { language:string, languageTr:string, term:string, translit:string|null,
                  gloss:string|null, relation:'inherited'|'borrowed'|'derived'|'learned'|'calque'|'unknown' }
    Part = { term:string, kind:'prefix'|'suffix'|'root'|'compound'|'word', gloss:string|null, language:string|null }
    Source = { name:string, url:string, ok:boolean, ms:number, note:string|null }

Kaynak sırası (hepsi anahtarsız):
 1. İngilizce Vikisözlük `en.wiktionary.org/w/api.php` (action=parse, prop=text; HTML'i DOM ile ayrıştır) — GARANTİLİ ana kaynak.
 2. Türkçe Vikisözlük `tr.wiktionary.org` (Köken bölümü) — Türkçe sözcükler için ek.
 3. Yalnız Android köprüsü varsa (tarayıcıda CORS engeller; başarısız olursa sessizce atla, hatayı `sources[]`'a yaz):
    Nişanyan Sözlük, TDK Güncel Sözlük, Etymonline. Erişilemezse uygulama YİNE de çalışmalıdır.
 Çekilen biçim kelimenin çekimli hali ise (kitaplar, geldim) kök sözcüğe git (`via`).

## Arayüz
- Dil: TÜRKÇE (etiketler, hata mesajları). Mobil öncelikli (360–430px), açık/koyu tema, 48px dokunma alanı.
- Ekranlar: arama kutusu + son aramalar; sonuç: "Yol haritası" (chain görsel zaman çizgisi),
  "Parçalar", "Anlam", "Ham metin (İngilizce)" + "Türkçeye çevir" düğmesi, "Kaynak" bağlantıları;
  yükleniyor / boş / hata / çevrimdışı durumları; kaynak durumu şeridi (hangi kaynak çalıştı).
- PWA: manifest + service worker (kabuk önbelleği; sorgular her zaman ağdan).
- Hiç harici CDN yok (tek başına çalışsın, `file://` ve `file:///android_asset/` altında da).
