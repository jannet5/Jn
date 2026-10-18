/*
 * Köken motoru (etym.js) — UMD: tarayıcıda window.Etym, Node'da module.exports.
 * Sözleşme: docs/CONTRACT.md
 *
 * Notlar:
 *  - ChainNode.relation: "bu düğümün, bir önceki (daha yeni) düğüme nasıl geçtiği".
 *    chain[0] her zaman sorgulanan sözcüğün kendisidir (relation:'unknown'); chain[1].relation
 *    ise chain[0]'ın chain[1]'den nasıl geldiğini söyler (borrowed = ödünç, inherited = miras...).
 *  - Etymology.source: sözleşmeye eklenen isteğe bağlı alan (hangi kaynaktan geldi).
 *  - Ağa yalnızca http(url,{headers,timeoutMs}) fonksiyonu üzerinden çıkılır.
 */
(function (root, factory) {
  // UMD: Node'da module.exports, tarayıcıda window.Etym
  if (typeof module === 'object' && module.exports) module.exports = factory();
  else root.Etym = factory();
})(typeof self !== 'undefined' ? self : this, function () {
  'use strict';

  // ------------------------------------------------------------------ sabitler
  var WIKT_EN = 'https://en.wiktionary.org';
  var WIKT_TR = 'https://tr.wiktionary.org';
  var UA = 'Koken/1.0 (kelime koken uygulamasi; https://github.com/jn/koken)';
  var DEFAULT_TIMEOUT = 12000;

  // İngilizce dil adı -> Türkçe dil adı sözlüğü
  var LANG_TR = {
    'Turkish': 'Türkçe', 'English': 'İngilizce', 'Arabic': 'Arapça', 'Persian': 'Farsça',
    'French': 'Fransızca', 'German': 'Almanca', 'Italian': 'İtalyanca', 'Spanish': 'İspanyolca',
    'Portuguese': 'Portekizce', 'Russian': 'Rusça', 'Greek': 'Yunanca', 'Ancient Greek': 'Eski Yunanca',
    'Koine Greek': 'Koine Yunancası', 'Medieval Greek': 'Orta Çağ Yunancası', 'Byzantine Greek': 'Bizans Yunancası',
    'Modern Greek': 'Modern Yunanca', 'Latin': 'Latince', 'Late Latin': 'Geç Latince',
    'Medieval Latin': 'Orta Çağ Latincesi', 'Vulgar Latin': 'Halk Latincesi', 'New Latin': 'Yeni Latince',
    'Old French': 'Eski Fransızca', 'Middle French': 'Orta Fransızca', 'Anglo-Norman': 'Anglo-Norman',
    'Old English': 'Eski İngilizce', 'Middle English': 'Orta İngilizce', 'Old Norse': 'Eski Nordik',
    'Old High German': 'Eski Yüksek Almanca', 'Middle High German': 'Orta Yüksek Almanca',
    'Old Saxon': 'Eski Sakson', 'Dutch': 'Felemenkçe', 'Middle Dutch': 'Orta Felemenkçe',
    'Swedish': 'İsveççe', 'Danish': 'Danca', 'Norwegian': 'Norveççe', 'Icelandic': 'İzlandaca',
    'Hebrew': 'İbranice', 'Aramaic': 'Aramice', 'Syriac': 'Süryanice', 'Akkadian': 'Akadca',
    'Sumerian': 'Sümerce', 'Sanskrit': 'Sanskritçe', 'Hindi': 'Hintçe', 'Urdu': 'Urduca',
    'Bengali': 'Bengalce', 'Chinese': 'Çince', 'Mandarin': 'Mandarin Çincesi', 'Cantonese': 'Kantonca',
    'Old Chinese': 'Eski Çince', 'Middle Chinese': 'Orta Çince', 'Japanese': 'Japonca', 'Korean': 'Korece',
    'Mongolian': 'Moğolca', 'Ottoman Turkish': 'Osmanlı Türkçesi', 'Old Turkic': 'Eski Türkçe',
    'Old Anatolian Turkish': 'Eski Anadolu Türkçesi', 'Old Uyghur': 'Eski Uygurca',
    'Proto-Turkic': 'Proto-Türkçe', 'Proto-Indo-European': 'Proto-Hint-Avrupa', 'Proto-Germanic': 'Proto-Cermen',
    'Proto-West Germanic': 'Proto-Batı Cermen', 'Proto-West-Germanic': 'Proto-Batı Cermen',
    'Proto-Semitic': 'Proto-Sami', 'Proto-Italic': 'Proto-İtalik', 'Proto-Slavic': 'Proto-Slav',
    'Proto-Celtic': 'Proto-Kelt', 'Proto-Uralic': 'Proto-Ural', 'Proto-Mongolic': 'Proto-Moğol',
    'Proto-Hellenic': 'Proto-Helen', 'Proto-Indo-Iranian': 'Proto-Hint-İran', 'Proto-Iranian': 'Proto-İran',
    'Proto-Indo-Aryan': 'Proto-Hint-Aryan', 'Proto-Balto-Slavic': 'Proto-Balto-Slav',
    'Proto-Sino-Tibetan': 'Proto-Çin-Tibet', 'Proto-Afroasiatic': 'Proto-Afro-Asya',
    'Azerbaijani': 'Azerbaycan Türkçesi', 'Kazakh': 'Kazakça', 'Uzbek': 'Özbekçe', 'Tatar': 'Tatarca',
    'Crimean Tatar': 'Kırım Tatarcası', 'Kyrgyz': 'Kırgızca', 'Turkmen': 'Türkmence', 'Uyghur': 'Uygurca',
    'Chagatai': 'Çağatayca', 'Bashkir': 'Başkurtça', 'Gagauz': 'Gagavuzca', 'Karakalpak': 'Karakalpakça',
    'Armenian': 'Ermenice', 'Georgian': 'Gürcüce', 'Albanian': 'Arnavutça', 'Bulgarian': 'Bulgarca',
    'Serbo-Croatian': 'Sırp-Hırvatça', 'Macedonian': 'Makedonca', 'Romanian': 'Rumence', 'Polish': 'Lehçe',
    'Czech': 'Çekçe', 'Slovak': 'Slovakça', 'Ukrainian': 'Ukraynaca', 'Hungarian': 'Macarca',
    'Finnish': 'Fince', 'Estonian': 'Estonca', 'Catalan': 'Katalanca', 'Galician': 'Galiçyaca',
    'Irish': 'İrlandaca', 'Welsh': 'Galce', 'Gothic': 'Gotça', 'Coptic': 'Kıptice',
    'Egyptian': 'Eski Mısırca', 'Malay': 'Malayca', 'Indonesian': 'Endonezce', 'Swahili': 'Svahili',
    'Vietnamese': 'Vietnamca', 'Thai': 'Tayca', 'Tamil': 'Tamilce', 'Telugu': 'Teluguca',
    'Kurdish': 'Kürtçe', 'Pashto': 'Peştuca', 'Tagalog': 'Tagalogca', 'Hawaiian': 'Hawaiice',
    'Nahuatl': 'Nahuatl', 'Classical Nahuatl': 'Klasik Nahuatl', 'Quechua': 'Keçuaca', 'Guarani': 'Guaranice',
    'Translingual': 'Çok dilli', 'Phoenician': 'Fenikece', 'Ge\'ez': 'Geezce', 'Amharic': 'Amharca',
    'Old Persian': 'Eski Farsça', 'Middle Persian': 'Orta Farsça', 'Avestan': 'Avestaca',
    'Old Irish': 'Eski İrlandaca', 'Old Frisian': 'Eski Frizce', 'West Frisian': 'Batı Frizcesi',
    'Yiddish': 'Yidiş', 'Afrikaans': 'Afrikaanca', 'Esperanto': 'Esperanto', 'Lithuanian': 'Litvanca',
    'Latvian': 'Letonca', 'Slovene': 'Slovence', 'Belarusian': 'Belarusça', 'Hittite': 'Hititçe',
    'Tocharian A': 'Toharca A', 'Tocharian B': 'Toharca B', 'Old Church Slavonic': 'Eski Kilise Slavcası'
  };

  // ISO/Wiktionary dil kodu -> İngilizce dil adı (bağlantıdan ad çıkmazsa yedek)
  var CODE_NAME = {
    tr: 'Turkish', en: 'English', ar: 'Arabic', fa: 'Persian', fr: 'French', de: 'German', it: 'Italian',
    es: 'Spanish', pt: 'Portuguese', ru: 'Russian', el: 'Greek', grc: 'Ancient Greek', la: 'Latin',
    ota: 'Ottoman Turkish', 'otk': 'Old Turkic', 'trk-pro': 'Proto-Turkic', 'ine-pro': 'Proto-Indo-European',
    'gem-pro': 'Proto-Germanic', 'sem-pro': 'Proto-Semitic', 'itc-pro': 'Proto-Italic', 'sla-pro': 'Proto-Slavic',
    'gmw-pro': 'Proto-West Germanic', 'cel-pro': 'Proto-Celtic', 'ira-pro': 'Proto-Iranian', 'iir-pro': 'Proto-Indo-Iranian',
    he: 'Hebrew', arc: 'Aramaic', akk: 'Akkadian', sux: 'Sumerian', sa: 'Sanskrit', hi: 'Hindi', ur: 'Urdu',
    bn: 'Bengali', zh: 'Chinese', cmn: 'Mandarin', yue: 'Cantonese', ja: 'Japanese', ko: 'Korean',
    mn: 'Mongolian', nl: 'Dutch', sv: 'Swedish', da: 'Danish', no: 'Norwegian', nb: 'Norwegian', is: 'Icelandic',
    fro: 'Old French', frm: 'Middle French', enm: 'Middle English', ang: 'Old English', non: 'Old Norse',
    goh: 'Old High German', gmh: 'Middle High German', ML: 'Medieval Latin', 'LL.': 'Late Latin',
    'VL.': 'Vulgar Latin', 'NL.': 'New Latin', 'ML.': 'Medieval Latin', LL: 'Late Latin', VL: 'Vulgar Latin', NL: 'New Latin',
    xno: 'Anglo-Norman', az: 'Azerbaijani', kk: 'Kazakh', uz: 'Uzbek', tt: 'Tatar', crh: 'Crimean Tatar',
    ky: 'Kyrgyz', tk: 'Turkmen', ug: 'Uyghur', chg: 'Chagatai', hy: 'Armenian', ka: 'Georgian', sq: 'Albanian',
    bg: 'Bulgarian', sh: 'Serbo-Croatian', mk: 'Macedonian', ro: 'Romanian', pl: 'Polish', cs: 'Czech',
    uk: 'Ukrainian', hu: 'Hungarian', fi: 'Finnish', ca: 'Catalan', ga: 'Irish', cy: 'Welsh', got: 'Gothic',
    cop: 'Coptic', ms: 'Malay', id: 'Indonesian', sw: 'Swahili', vi: 'Vietnamese', th: 'Thai', ku: 'Kurdish',
    myn: 'Mayan', nah: 'Nahuatl', nci: 'Classical Nahuatl', qu: 'Quechua', gn: 'Guarani', und: 'Unknown'
  };

  // ------------------------------------------------------------------ yardımcılar
  // İngilizce dil adını Türkçeye çevirir; bilinmiyorsa aynen döndürür
  function langTr(name) {
    if (!name) return '';
    return LANG_TR[name] || name;
  }

  // Zaman ölçümü için şimdiki an (ms)
  function now() { return Date.now(); }

  // Belirli süre bekler
  function sleep(ms) { return new Promise(function (r) { setTimeout(r, ms); }); }

  // Boşlukları sadeleştirir
  function norm(s) { return String(s == null ? '' : s).replace(/[\u00a0\s]+/g, ' ').trim(); }

  // URL için güvenli kodlama
  function enc(s) { return encodeURIComponent(s); }

  // Sorgu için aday yazımlar üretir (Türkçe İ/ı dahil büyük/küçük harf varyantları)
  function variants(q) {
    var out = [];
    function add(v) { if (v && out.indexOf(v) < 0) out.push(v); }
    var trLow = q.toLocaleLowerCase('tr');
    var enLow = q.toLowerCase();
    // İlk harfi büyüten yardımcı (verilen yerel ayara göre)
    function cap(s, loc) { return s.charAt(0).toLocaleUpperCase(loc) + s.slice(1); }
    add(q);
    add(trLow);
    add(enLow);
    add(cap(trLow, 'tr'));
    add(cap(enLow, 'en'));
    add(cap(q, 'tr'));
    return out;
  }

  // Dize başındaki/sonundaki noktalama temizliği için küçük yardımcı
  function stripPunct(s) { return norm(s).replace(/^[\s,;:.()\-–—]+|[\s,;:.()]+$/g, ''); }

  // ------------------------------------------------------------------ DOM
  var nodeDomParser = null;

  // HTML metnini DOM belgesine çevirir (tarayıcıda DOMParser, Node'da linkedom)
  function parseHtml(html) {
    if (typeof DOMParser !== 'undefined') {
      return new DOMParser().parseFromString('<!doctype html><html><body>' + html + '</body></html>', 'text/html');
    }
    // Node: linkedom'u tembel yükle (önce normal, sonra tests/node_modules)
    if (!nodeDomParser) {
      var req = (typeof module !== 'undefined' && module.require) ? module.require.bind(module) : require;
      var mod = null;
      var tries = ['linkedom'];
      try {
        var path = req('path');
        tries.push(path.join(__dirname, 'node_modules', 'linkedom'));
        tries.push(path.join(__dirname, '..', 'tests', 'node_modules', 'linkedom'));
        tries.push(path.join(__dirname, '..', 'node_modules', 'linkedom'));
      } catch (e) { /* yol modülü yoksa sadece 'linkedom' denenir */ }
      for (var i = 0; i < tries.length && !mod; i++) {
        try { mod = req(tries[i]); } catch (e) { mod = null; }
      }
      if (!mod) throw new Error('linkedom bulunamadı (Node için gerekli)');
      nodeDomParser = mod.parseHTML;
    }
    return nodeDomParser('<!doctype html><html><body>' + html + '</body></html>').document;
  }

  // Elemanın sınıf listesini küçük harfli dize olarak verir
  function cls(el) { return (el && el.getAttribute && el.getAttribute('class')) || ''; }

  // Elemanın etiket adı (küçük harf)
  function tag(el) { return (el.localName || el.tagName || '').toLowerCase(); }

  // Sınıf dizesinde belirli bir sınıf var mı
  function hasCls(el, name) { return (' ' + cls(el) + ' ').indexOf(' ' + name + ' ') >= 0; }

  // ------------------------------------------------------------------ AĞ
  var nativePending = {};
  var nativeSeq = 0;
  var nativeHooked = false;

  // Android köprüsünden dönen base64 UTF-8 gövdeyi metne çevirir
  function b64ToText(b64) {
    if (!b64) return '';
    var bin = atob(b64);
    var bytes = new Uint8Array(bin.length);
    for (var i = 0; i < bin.length; i++) bytes[i] = bin.charCodeAt(i);
    return new TextDecoder('utf-8').decode(bytes);
  }

  // http fonksiyonu üretir: KokenNative varsa köprü, yoksa fetch
  function makeHttp() {
    var g = (typeof window !== 'undefined') ? window : (typeof globalThis !== 'undefined' ? globalThis : {});
    var nat = g.KokenNative;
    if (nat && typeof nat.get === 'function') {
      // Java'nın çağıracağı geri dönüş kancasını bir kez kur
      if (!nativeHooked) {
        nativeHooked = true;
        var prev = g.__kokenNativeDone;
        g.__kokenNativeDone = function (id, status, bodyBase64) {
          var p = nativePending[id];
          if (p) {
            delete nativePending[id];
            var text = '';
            try { text = b64ToText(bodyBase64); } catch (e) { text = ''; }
            p.resolve({ status: Number(status) || 0, text: text, headers: {} });
          } else if (typeof prev === 'function') { prev(id, status, bodyBase64); }
        };
      }
      return function nativeHttp(url, o) {
        o = o || {};
        return new Promise(function (resolve) {
          var id = 'k' + (++nativeSeq) + '_' + now();
          var tmo = o.timeoutMs || DEFAULT_TIMEOUT;
          // JS tarafında da emniyet zaman aşımı: köprü cevap vermezse status 0 dön
          var timer = setTimeout(function () {
            if (nativePending[id]) { delete nativePending[id]; resolve({ status: 0, text: '', headers: {} }); }
          }, tmo + 1500);
          nativePending[id] = { resolve: function (r) { clearTimeout(timer); resolve(r); } };
          try {
            nat.get(id, url, JSON.stringify(o.headers || {}), tmo);
          } catch (e) {
            clearTimeout(timer); delete nativePending[id];
            resolve({ status: 0, text: '', headers: {} });
          }
        });
      };
    }
    // Köprü yok: fetch + AbortController zaman aşımı
    return function fetchHttp(url, o) {
      o = o || {};
      var ctrl = (typeof AbortController !== 'undefined') ? new AbortController() : null;
      var tmo = o.timeoutMs || DEFAULT_TIMEOUT;
      var timer = ctrl ? setTimeout(function () { ctrl.abort(); }, tmo) : null;
      var init = { method: 'GET', headers: o.headers || {} };
      if (ctrl) init.signal = ctrl.signal;
      return fetch(url, init).then(function (res) {
        return res.text().then(function (text) {
          var h = {};
          try { res.headers.forEach(function (v, k) { h[k.toLowerCase()] = v; }); } catch (e) { /* başlıklar okunamadı */ }
          return { status: res.status, text: text, headers: h };
        });
      }).then(function (r) { if (timer) clearTimeout(timer); return r; },
        function (e) { if (timer) clearTimeout(timer); throw e; });
    };
  }

  // Yeniden denemeli istek: 429/503 ve ağ hatalarında kısa bekleyip tekrar dener
  function getRetry(http, url, o, signal) {
    o = o || {};
    var maxTry = o.tries || 3;
    var maxWait = o.maxWaitMs == null ? 2500 : o.maxWaitMs;
    var attempt = 0;
    // Tek deneme + gerekirse özyinelemeli tekrar
    function once() {
      attempt++;
      if (signal && signal.aborted) return Promise.reject(new Error('iptal'));
      return Promise.resolve().then(function () { return http(url, o); }).then(function (r) {
        if ((r.status === 429 || r.status === 503) && attempt < maxTry) {
          var ra = 0;
          try { ra = Number((r.headers || {})['retry-after']) * 1000; } catch (e) { ra = 0; }
          var wait = ra > 0 ? ra : 600 * attempt;
          // Çok uzun beklemeye değmez; kullanıcıya hızlı hata dön
          if (wait > maxWait) return r;
          return sleep(wait).then(once);
        }
        return r;
      }, function (err) {
        if (attempt < maxTry && !(signal && signal.aborted)) return sleep(400 * attempt).then(once);
        throw err;
      });
    }
    return once();
  }

  // ------------------------------------------------------------------ Wiktionary ayrıştırma
  // Başlık elemanından (div.mw-heading ya da h2-h6) {level, text, id} çıkarır
  function headingInfo(el) {
    var h = null;
    if (/^h[1-6]$/.test(tag(el))) h = el;
    else if (el.querySelector) h = el.querySelector('h1,h2,h3,h4,h5,h6');
    if (!h) return null;
    return { level: Number(tag(h).charAt(1)), text: norm(h.textContent), id: h.getAttribute('id') || '' };
  }

  // Düğümü kopyalayıp gürültüyü (dipnot, stil, düzenle bağlantısı) atar ve düz metin verir
  function cleanText(el) {
    var c = el.cloneNode(true);
    var junk = c.querySelectorAll ? c.querySelectorAll('sup.reference, style, script, .mw-editsection, .HQToggle') : [];
    for (var i = 0; i < junk.length; i++) { if (junk[i].parentNode) junk[i].parentNode.removeChild(junk[i]); }
    return norm(c.textContent).replace(/\s+([,.;:)])/g, '$1').replace(/\(\s+/g, '(');
  }

  // Bir elemanın dil etiketli (sözcük/terim) düğüm olup olmadığına bakar
  function isTermEl(el) {
    if (!el.hasAttribute || !el.hasAttribute('lang')) return false;
    var c = ' ' + cls(el) + ' ';
    if (/ (tr|mention-tr|mention-gloss) /.test(c)) return false;
    if (/ (mention|term|Latn|Arab|Cyrl|Deva|Grek|Hebr|Hani|Hira|Kana|Hang|Armn|Geor|Thai|Mong|Copt|Ethi|Syrc|Tfng|Xsux|Egyp|Goth|Runr|Ital|Orkh|Phnx|Sinh|Beng|Taml|Telu|Knda|Mlym|Gujr|Guru|Mymr|Khmr|Laoo|Tibt|Hans|Hant|Jpan|Kore|Phag|Brah) /.test(c)) return true;
    return tag(el) === 'i';
  }

  // Terim elemanındaki bağlantıdan dil bölümü adını (#Turkish gibi) çıkarır
  function fragmentLang(el) {
    var a = el.querySelector ? el.querySelector('a[href]') : null;
    if (!a) return null;
    var href = a.getAttribute('href') || '';
    var i = href.indexOf('#');
    if (i < 0) return null;
    // Wikipedia vb. dış bağlantılarda parça yok sayılır
    if (/^https?:/.test(href) && href.indexOf('wiktionary.org') < 0) return null;
    var frag = href.slice(i + 1);
    try { frag = decodeURIComponent(frag); } catch (e) { /* kodlama bozuksa ham bırak */ }
    frag = frag.replace(/_/g, ' ').trim();
    return frag || null;
  }

  // "w:English language" gibi başlıktan dil adını çıkarır
  function langFromTitle(t) {
    return norm(String(t || '').replace(/^w:/, '').replace(/ language$/i, ''));
  }

  // Paragrafı {t:'text'|'lang'|'term'} belirteç dizisine çevirir
  function tokenize(p) {
    var tokens = [];
    var skip = [];
    // Bu düğüm daha önce bir terimin çeviri/anlam parçası olarak tüketildi mi
    function consumed(n) { return skip.indexOf(n) >= 0; }

    // Terimin hemen arkasındaki (okunuş)/“anlam” süslerini toplar
    function takeAnnotations(termEl, tok) {
      var cands = [];
      var last = -1;
      var n = termEl.nextSibling;
      var guard = 0;
      while (n && guard++ < 40) {
        if (n.nodeType === 3) {
          // Yalnızca boşluk/virgül/parantez içeren metin köprü sayılır
          if (/^[\s,;:()]*$/.test(n.nodeValue)) { cands.push(n); n = n.nextSibling; continue; }
          break;
        }
        if (n.nodeType === 1) {
          var c = cls(n);
          if (/(^|\s)mention-/.test(c)) {
            cands.push(n); last = cands.length - 1;
            if (/(^|\s)mention-tr(\s|$)/.test(c) || hasCls(n, 'tr')) tok.translit = norm(n.textContent);
            else if (/(^|\s)mention-gloss(\s|$)/.test(c)) tok.gloss = (tok.gloss ? tok.gloss + '; ' : '') + norm(n.textContent);
            n = n.nextSibling; continue;
          }
          if (hasCls(n, 'tr') && n.hasAttribute('lang')) {
            cands.push(n); last = cands.length - 1; tok.translit = norm(n.textContent);
            n = n.nextSibling; continue;
          }
        }
        break;
      }
      // Son anlam/okunuş elemanına kadar olanları tüketilmiş say
      for (var i = 0; i <= last; i++) skip.push(cands[i]);
    }

    // Çocukları sırayla gezen özyinelemeli yürüyüş
    function walk(node) {
      var kids = node.childNodes;
      for (var i = 0; i < kids.length; i++) {
        var ch = kids[i];
        if (consumed(ch)) continue;
        if (ch.nodeType === 3) { tokens.push({ t: 'text', s: ch.nodeValue.replace(/[\u200b-\u200f\u2060\u202a-\u202e]/g, '') }); continue; }
        if (ch.nodeType !== 1) continue;
        var tg = tag(ch);
        var c = cls(ch);
        if (tg === 'style' || tg === 'script' || (tg === 'sup' && /reference/.test(c)) || /mw-editsection/.test(c)) continue;
        if (isTermEl(ch)) {
          var tok = {
            t: 'term', lang: ch.getAttribute('lang') || '', term: norm(ch.textContent),
            frag: fragmentLang(ch), translit: null, gloss: null
          };
          takeAnnotations(ch, tok);
          tokens.push(tok);
          continue;
        }
        if (tg === 'a' && /Appendix:Glossary/.test(ch.getAttribute('href') || '')) { tokens.push({ t: 'text', s: ch.textContent }); continue; }
        if (hasCls(ch, 'etyl') || (tg === 'a' && hasCls(ch, 'extiw'))) {
          var a = (tg === 'a') ? ch : (ch.querySelector('a') || ch);
          tokens.push({ t: 'lang', s: norm(a.textContent), title: langFromTitle(a.getAttribute && a.getAttribute('title')) });
          continue;
        }
        walk(ch);
      }
    }
    walk(p);
    return tokens;
  }

  var ABBR = /(?:^|[\s(])(?:cf|ca|c|vs|lit|ult|esp|st|dr|no|vol|ed|fl|sp|spp|sc|viz|orig|poss|prob|dim)\.$/i;

  // Belirteçleri cümlelere böler; her cümle terim öğelerinden oluşur
  function sentencesOf(tokens) {
    var sents = [];
    var cur = { items: [], tail: '' };
    var conn = '';
    var pendingLang = null;
    // Öğe eklemeden önce bekleyen dil adını kontrol eden yardımcı
    function flushSentence() {
      cur.tail = conn;
      sents.push(cur);
      cur = { items: [], tail: '' };
      conn = '';
      pendingLang = null;
    }
    for (var i = 0; i < tokens.length; i++) {
      var tk = tokens[i];
      if (tk.t === 'lang') {
        // Dil adı bağlantısı: hemen ardından terim gelirse dil adı sayılır, yoksa metin
        var j = i + 1;
        while (j < tokens.length && tokens[j].t === 'text' && /^\s*$/.test(tokens[j].s)) j++;
        if (j < tokens.length && tokens[j].t === 'term') pendingLang = tk;
        else conn += ' ' + tk.s + ' ';
        continue;
      }
      if (tk.t === 'term') {
        cur.items.push({ conn: norm(conn), langTok: pendingLang, tok: tk });
        conn = ''; pendingLang = null;
        continue;
      }
      // Düz metin: cümle sonu noktası/noktalı virgül ara
      var s = tk.s;
      var re = /[.;](?=\s|$)/g;
      var m, last = 0;
      while ((m = re.exec(s))) {
        var before = conn + s.slice(last, m.index + 1);
        if (m[0] === '.' && ABBR.test(before)) continue;
        conn += s.slice(last, m.index);
        last = m.index + 1;
        flushSentence();
      }
      conn += s.slice(last);
    }
    cur.tail = conn;
    sents.push(cur);
    return sents;
  }

  var RX_COGNATE = /cognate|compare|\bcf\b|akin to|related to|doublet|see also|corresponds|parallel|displaced|replaced|unrelated|not related|\bwhence\b|same root|same origin|distinct from|sometimes (?:erroneous|confused)|influenced by|contrast|\bvs\b|\bsee\b/i;
  var RX_CHAIN = /(from|borrow|loan|inherit|descend|derive|via|calque|after|alteration|variant|short|clip|abbreviat|back-formation|origin|ultimately|coined|blend|diminutive|compound|developed|evolved|reborrow|adapt|transcri|translat|replac|form of|use of|feminine|masculine|plural|extended|contraction|metathe|corrupt|eponym|named)/i;
  var RX_PARTS_HEAD = /(equivalent to|compound of|composed of|combination of|consist|blend of|formed? from|derivation of|analy[sz]able as|surface analysis|\banalysis\b|\bsuffixed\b|\bprefixed\b)/i;

  // Bağlaç metninden ilişki türünü tahmin eder
  function relationOf(conn) {
    var c = (conn || '').toLowerCase();
    if (/calque|loan translation|semantic loan|loan-translation/.test(c)) return 'calque';
    if (/learned|scholar|reborrow/.test(c)) return 'learned';
    if (/borrow|loan|adapt|transcri/.test(c)) return 'borrowed';
    if (/inherit|descend|continu/.test(c)) return 'inherited';
    if (RX_CHAIN.test(c)) return 'derived';
    return 'unknown';
  }

  // Terim belirtecinden dil adını çözer (öncelik: bitişik dil bağlantısı, parça, kod)
  function langNameOf(item) {
    var tk = item.tok;
    var lt = item.langTok;
    var name = null;
    if (lt && lt.s) name = lt.s;
    if (!name && tk.frag && !/^(Etymology|Translingual)/.test(tk.frag)) name = tk.frag;
    if (!name && lt && lt.title) name = lt.title;
    if (!name && tk.lang) name = CODE_NAME[tk.lang] || CODE_NAME[tk.lang.split('-')[0]] || null;
    return name || tk.lang || '';
  }

  // Bir ek/kök kalıbına göre Part.kind belirler
  function partKind(term, nonAffixCount) {
    var t = term.replace(/^[*]/, '');
    var starts = /^[-‐‑–]/.test(t);
    var ends = /[-‐‑–]$/.test(t);
    if (starts && !ends) return 'suffix';
    if (ends && !starts) return 'prefix';
    if (nonAffixCount >= 2) return 'compound';
    return 'root';
  }

  // Tek bir etimoloji paragraf kümesinden chain/parts/cognates çıkarır
  function analyzeEtymology(paras, headword) {
    var chain = [{
      language: headword.language, languageTr: langTr(headword.language), term: headword.term,
      translit: null, gloss: headword.gloss || null, relation: 'unknown'
    }];
    var parts = [];
    var cognates = [];
    var partsDone = false;
    var seenCog = {};

    for (var pi = 0; pi < paras.length; pi++) {
      var sents = sentencesOf(tokenize(paras[pi]));
      for (var si = 0; si < sents.length; si++) {
        var items = sents[si].items;
        var mode = 'none';
        var group = null;
        for (var k = 0; k < items.length; k++) {
          var it = items[k];
          var conn = it.conn.toLowerCase();
          var isPlus = /(^|[\s,])\+\s*$/.test(conn) || conn === '+';
          var lname = langNameOf(it);

          // Artı ile bağlı terim: bir önceki terimle aynı gruba girer
          if (isPlus) {
            if (!group) {
              group = { items: [], head: true };
              // Önceki terim zinciren geri alınır (ayrı atadan çok parça sayılır)
              var prev = items[k - 1];
              if (prev) {
                var lastNode = chain[chain.length - 1];
                if (chain.length > 1 && lastNode.term === prev.tok.term) {
                  chain.pop();
                }
                group.items.push(prev);
              }
              // Grup, hiç ata yoksa ya da "equivalent to" gibi ifadeden sonra gelmişse sözcüğün kendisine aittir
              group.head = chain.length === 1 || (prev && RX_PARTS_HEAD.test(prev.conn));
            }
            group.items.push(it);
            continue;
          }
          // Artısız terim gelince varsa önceki grubu kapat
          if (group) { closeGroup(); }

          // Kip belirle: akraba mı, zincir mi?
          if (RX_COGNATE.test(conn)) mode = 'cognate';
          else if (RX_PARTS_HEAD.test(conn)) mode = 'parts';
          else if (RX_CHAIN.test(conn)) mode = 'chain';

          if (mode === 'cognate') {
            var key = lname + '|' + it.tok.term;
            if (!seenCog[key] && cognates.length < 40) { seenCog[key] = 1; cognates.push({ language: lname, term: it.tok.term }); }
          } else if (mode === 'chain') {
            // Yalnızca virgül/"or"/"/" ile eklenen alternatif yazımları zincire katma
            if (!RX_CHAIN.test(conn)) continue;
            // Ardışık yinelenen düğümleri ele
            var lastN = chain[chain.length - 1];
            if (lastN.term === it.tok.term && lastN.language === lname) continue;
            chain.push({
              language: lname, languageTr: langTr(lname), term: it.tok.term,
              translit: it.tok.translit || null, gloss: it.tok.gloss || null, relation: relationOf(it.conn)
            });
          } else if (mode === 'parts') {
            // Tek başına "equivalent to X" -> parça grubunun ilk öğesi (artı bekleyen)
            var nxt = items[k + 1];
            if (nxt && /(^|[\s,])\+\s*$/.test(nxt.conn.toLowerCase())) {
              // sonraki turda artı bulunca prev olarak buraya bağlanacak
              group = { items: [it], head: true };
            }
          }
        }
        if (group) closeGroup();

        // Grubu kapatıp parça listesine yazan iç yardımcı
        function closeGroup() {
          var g = group; group = null;
          if (!g || !g.head || partsDone) return;
          var nonAff = 0, ii;
          for (ii = 0; ii < g.items.length; ii++) { if (partKind(g.items[ii].tok.term, 0) === 'root') nonAff++; }
          for (ii = 0; ii < g.items.length; ii++) {
            var t = g.items[ii].tok;
            // Sonu tireli terim (say-, sev-) ardından düz sözcük gelmiyorsa ön ek değil fiil kökü sayılır
            var kind = partKind(t.term, nonAff);
            if (kind === 'prefix') {
              var hasWordAfter = false, jj;
              for (jj = ii + 1; jj < g.items.length; jj++) { if (partKind(g.items[jj].tok.term, 0) === 'root') hasWordAfter = true; }
              if (!hasWordAfter) kind = 'root';
            }
            parts.push({
              term: t.term, kind: kind, gloss: t.gloss || null,
              language: langNameOf(g.items[ii]) || null
            });
          }
          if (parts.length) partsDone = true;
        }
      }
    }
    // Zincirdeki her düğümün anlamı varsa kısalt
    return { chain: chain, parts: parts, cognates: cognates };
  }

  // Tanım satırından (li) iç içe listeleri atarak düz anlam metni çıkarır
  function defGloss(li) {
    var c = li.cloneNode(true);
    var junk = c.querySelectorAll('ol, ul, dl, .HQToggle, sup.reference, style, .maintenance-line, .mw-collapsible');
    for (var i = 0; i < junk.length; i++) { if (junk[i].parentNode) junk[i].parentNode.removeChild(junk[i]); }
    var t = norm(c.textContent);
    return t.length > 220 ? t.slice(0, 217) + '…' : t;
  }

  // Tanım satırının "biçim" (çoğul/çekim vb.) olup olmadığını bulur
  function formOfIn(li) {
    var fo = li.querySelector('.form-of-definition');
    if (!fo && !/\b(?:plural|inflection|participle|tense|form|genitive|dative|accusative|locative|ablative|comparative|superlative|diminutive|alternative|spelling|variant|singular|person)\b[^.]{0,40}\bof\b/i.test(norm(li.textContent).slice(0, 80))) return null;
    // Önce asıl hedef bağlantıları, sonra genel bağlantıları dene; sözlük/ek sayfası bağlantılarını ele
    var target = null;
    var sels = ['.form-of-definition-link a', '.form-of-definition a[title]', 'span[class*="form-of"] a[href]'];
    for (var si = 0; si < sels.length && !target; si++) {
      var as = li.querySelectorAll(sels[si]);
      for (var ai = 0; ai < as.length && !target; ai++) {
        var cand = as[ai].getAttribute('title') || norm(as[ai].textContent);
        if (cand && !/^(Appendix|Category|Wiktionary|w:)/.test(cand)) target = cand;
      }
    }
    if (!target) return null;
    var desc = norm((li.querySelector('.form-of-definition') || li).textContent).toLowerCase();
    return { target: target.replace(/ \(page does not exist\)$/, ''), desc: desc.slice(0, 80) };
  }

  var POS_NAMES = /^(noun|proper noun|verb|adjective|adverb|pronoun|preposition|postposition|conjunction|interjection|numeral|particle|determiner|article|prefix|suffix|infix|circumfix|affix|phrase|proverb|idiom|contraction|symbol|letter|character|abbreviation|acronym|initialism|participle|romanization|classifier|combining form|root|name|prepositional phrase|punctuation mark|diacritical mark|han character|hanzi|kanji|hanja|syllable|counter|clitic|ligature|number|cardinal numeral|ordinal numeral|interfix|postposition|adnominal)$/i;

  // en.wiktionary parse çıktısını dil bölümlerine ayrıştırır
  function parseEnPage(html, title) {
    var doc = parseHtml(html);
    var rootEl = doc.querySelector('.mw-parser-output') || doc.body;
    var sections = [];
    var sec = null;
    var ety = null;
    var inEty = false;
    var kids = rootEl.children;

    // Yeni etimoloji kaydı açar
    function newEty(label) {
      ety = { label: label || '', paras: [], pos: [], glosses: [], formOf: null };
      sec.etys.push(ety);
      return ety;
    }

    for (var i = 0; i < kids.length; i++) {
      var el = kids[i];
      var hi = (hasCls(el, 'mw-heading') || /^h[1-6]$/.test(tag(el))) ? headingInfo(el) : null;
      if (hi) {
        if (hi.level === 2) {
          sec = { language: hi.text, id: hi.id, etys: [], pos: [] };
          sections.push(sec);
          ety = null; inEty = false;
          continue;
        }
        if (!sec) continue;
        if (/^Etymology/i.test(hi.text)) {
          // Boş bir kayıt zaten açıksa yeniden kullan (başlıksız POS kaydı değilse)
          if (ety && !ety.paras.length && !ety.pos.length) { ety.label = hi.text; }
          else newEty(hi.text);
          inEty = true;
          continue;
        }
        inEty = false;
        var base = hi.text.replace(/\s+\d+$/, '');
        if (POS_NAMES.test(base)) {
          if (!ety) newEty('');
          var posLow = base.toLowerCase();
          if (ety.pos.indexOf(posLow) < 0) ety.pos.push(posLow);
          if (sec.pos.indexOf(posLow) < 0) sec.pos.push(posLow);
        }
        continue;
      }
      if (!sec) continue;
      var tg = tag(el);
      if (inEty && tg === 'p') {
        if (cleanText(el)) ety.paras.push(el);
        continue;
      }
      // Tanım listeleri: anlam ve biçim-of bilgisi
      if (tg === 'ol' && ety && !inEty) {
        var lis = el.children;
        for (var j = 0; j < lis.length; j++) {
          if (tag(lis[j]) !== 'li') continue;
          var fo = formOfIn(lis[j]);
          if (fo) { if (!ety.formOf) ety.formOf = fo; }
          else {
            var g = defGloss(lis[j]);
            if (g && ety.glosses.length < 3) ety.glosses.push(g);
          }
        }
      }
    }
    return sections;
  }

  // "plural of" gibi İngilizce açıklamadan Türkçe biçim adı çıkarır
  function formDescTr(desc) {
    var d = (desc || '').toLowerCase();
    if (/plural/.test(d)) return 'çoğul biçim';
    if (/past tense|past participle|preterite/.test(d)) return 'geçmiş zaman biçimi';
    if (/participle/.test(d)) return 'ortaç biçimi';
    if (/inflection|genitive|dative|accusative|locative|ablative|person|tense|possessive/.test(d)) return 'çekimli biçim';
    if (/comparative|superlative/.test(d)) return 'karşılaştırma biçimi';
    if (/alternative|spelling|variant/.test(d)) return 'yazım değişkesi';
    if (/diminutive/.test(d)) return 'küçültme biçimi';
    return 'başka bir sözcüğün biçimi';
  }

  // Dil adı sıralaması: Türkçe, İngilizce, sonra sayfadaki sıra
  function sortEntries(entries) {
    function rank(e) { return e.language === 'Turkish' ? 0 : (e.language === 'English' ? 1 : 2); }
    return entries.map(function (e, i) { return [e, i]; }).sort(function (a, b) {
      return (rank(a[0]) - rank(b[0])) || (a[1] - b[1]);
    }).map(function (x) { return x[0]; });
  }

  // Ayrıştırılmış bölümlerden sözleşmedeki Entry listesini kurar
  function buildEntries(sections, pageTitle, wiktBase) {
    var entries = [];
    var formTargets = [];
    sections.forEach(function (sec) {
      var etyList = [];
      sec.etys.forEach(function (e) {
        if (!e.paras.length) return;
        var headword = { language: sec.language, term: pageTitle, gloss: e.glosses[0] || null };
        var a = analyzeEtymology(e.paras, headword);
        etyList.push({
          text: e.paras.map(cleanText).join(' '),
          chain: a.chain, parts: a.parts, cognates: a.cognates,
          gloss: e.glosses[0] || null, source: 'en.wiktionary'
        });
      });
      // Biçim sayfası ipucu: etimolojisi olmayıp biçim-of içeren bölüm
      sec.etys.forEach(function (e) {
        if (!e.paras.length && e.formOf) formTargets.push({ language: sec.language, target: e.formOf.target, desc: e.formOf.desc });
      });
      if (etyList.length) {
        entries.push({
          language: sec.language, languageTr: langTr(sec.language), pos: sec.pos.slice(),
          etymologies: etyList, url: wiktBase + '/wiki/' + enc(pageTitle.replace(/ /g, '_')) + '#' + enc(sec.language.replace(/ /g, '_'))
        });
      }
    });
    return { entries: sortEntries(entries), formTargets: formTargets };
  }

  // ------------------------------------------------------------------ tr.wiktionary
  // Türkçe Vikisözlük sayfasından "Köken" bölümünü düz metin olarak çıkarır
  function parseTrPage(html) {
    var doc = parseHtml(html);
    var rootEl = doc.querySelector('.mw-parser-output') || doc.body;
    var inTurkish = false, inEty = false, paras = [];
    var kids = rootEl.children;
    for (var i = 0; i < kids.length; i++) {
      var el = kids[i];
      var hi = (hasCls(el, 'mw-heading') || /^h[1-6]$/.test(tag(el))) ? headingInfo(el) : null;
      if (hi) {
        if (hi.level === 2) { inTurkish = /^T[üu]rk[çc]e$/i.test(hi.text); inEty = false; continue; }
        inEty = inTurkish && /^(K[öo]ken|Etimoloji|Kökeni)/i.test(hi.text);
        continue;
      }
      if (inEty && /^(p|ul|ol|dl)$/.test(tag(el))) { if (cleanText(el)) paras.push(el); }
    }
    return paras;
  }

  // Metindeki Türkçe dil adlarından ("Arapça X kelimesinden") basit zincir üretir
  function trTextChain(text, headTerm) {
    var chain = [{ language: 'Turkish', languageTr: 'Türkçe', term: headTerm, translit: null, gloss: null, relation: 'unknown' }];
    // Türkçe dil adı -> İngilizce ad (uzun adlar önce denensin diye uzunluğa göre sıralanır)
    var names = { 'Osmanlı Türkçesi': 'Ottoman Turkish', 'Osmanlıca': 'Ottoman Turkish', 'Eski Türkçe': 'Old Turkic', 'Orta Farsça': 'Middle Persian', 'Arapça': 'Arabic', 'Farsça': 'Persian', 'Fransızca': 'French', 'İngilizce': 'English', 'Almanca': 'German', 'İtalyanca': 'Italian', 'Eski Yunanca': 'Ancient Greek', 'Yunanca': 'Greek', 'Latince': 'Latin', 'Rusça': 'Russian', 'İspanyolca': 'Spanish', 'Moğolca': 'Mongolian', 'Ermenice': 'Armenian', 'Süryanice': 'Syriac', 'İbranice': 'Hebrew', 'Çince': 'Chinese', 'Sanskritçe': 'Sanskrit', 'Çağatayca': 'Chagatai', 'Hintçe': 'Hindi', 'Aramice': 'Aramaic', 'Sümerce': 'Sumerian', 'Akadca': 'Akkadian' };
    var keys = Object.keys(names).sort(function (x, y) { return y.length - x.length; });
    var rx = new RegExp('(' + keys.join('|') + ')\\s+([^\\s,;()]+)', 'g');
    var m;
    // Görünmez yön işaretlerini temizle
    var clean = String(text || '').replace(/[\u200e\u200f\u202a-\u202e]/g, '');
    while ((m = rx.exec(clean))) {
      var term = m[2].replace(/^["“”'‘’]+|["“”'‘’.]+$/g, '');
      // "kelimesinden/sözcüğünden" gibi sözcükleri terim sanma
      if (/^(kelime|sözcük|dil|dilin|sözcüğ|kökenli|kökünden|ile|ve|o|da)/i.test(term)) continue;
      var en = names[m[1]];
      var last = chain[chain.length - 1];
      if (last.term === term && last.language === en) continue;
      chain.push({ language: en, languageTr: m[1], term: term, translit: null, gloss: null, relation: 'derived' });
    }
    return chain;
  }

  // ------------------------------------------------------------------ Yerel köprü kaynakları
  // Nişanyan Sözlük (JSON; biçim değişebilir, savunmacı yazıldı)
  function parseNisanyan(text) {
    // Yeni site HTML veriyor: köken düz metin olarak <meta name="description"> içinde
    if (/^\s*</.test(text)) {
      var hd = parseHtml(text);
      var mm = hd.querySelector('meta[name="description"]');
      var ct = mm ? norm(mm.getAttribute('content') || '') : '';
      return ct ? { name: '', text: ct, loan: false } : null;
    }
    var j = JSON.parse(text);
    var words = j && (j.words || j.data || (Array.isArray(j) ? j : null));
    if (!words || !words.length) return null;
    var w = words[0];
    var parts = [];
    // Etimoloji alanı dize, dizi ya da HTML olabilir
    function strip(s) { return norm(String(s || '').replace(/<[^>]*>/g, ' ')); }
    if (Array.isArray(w.etymologies)) {
      w.etymologies.forEach(function (e) {
        // languages[0].name, romanizedText, originalText, definition, relation.text alanları kullanılır
        var lang0 = (e.languages && e.languages[0]) ? (e.languages[0].name || e.languages[0]) : '';
        var rel = e.relation && (e.relation.text || e.relation.abbr || '');
        var orig = e.romanizedText || e.originalText || e.text || '';
        var piece = [rel, lang0, orig, e.definition ? '“' + e.definition + '”' : ''].filter(Boolean).join(' ');
        if (piece) parts.push(strip(piece));
      });
    }
    var etxt = parts.join(' ← ') || strip(w.etymologyHtml || w.etymology || w.k || '');
    if (!etxt) return null;
    return { name: w.name || w.n || '', text: etxt, loan: !!(w.isLoanword) };
  }

  // Metinde ilk geçen dil adını (Arapça, Fransızca...) döndürür: sözcüğü veren dil
  function firstLangOf(text) {
    var names = Object.keys(LANG_TR).map(function (k) { return LANG_TR[k]; }).filter(function (n) { return n && n !== 'Türkçe'; });
    names.sort(function (a, b) { return b.length - a.length; });
    var best = null, bestAt = Infinity;
    names.forEach(function (n) {
      var re = new RegExp('(^|[^\\p{L}])' + n.replace(/[.*+?^${}()|[\]\\]/g, '\\$&') + '(?![\\p{L}])', 'u');
      var m = re.exec(text);
      if (m && m.index < bestAt) { bestAt = m.index; best = n; }
    });
    return best;
  }

  // Metindeki ilk dil adı ve ondan sonra gelen sözcük: {lang, term}. "Arapça ktb kökünden gelen kitāb ..." -> kitāb
  function firstDonor(text) {
    var lang = firstLangOf(text);
    if (!lang) return null;
    var at = text.indexOf(lang);
    var rest = text.slice(at + lang.length).replace(/^\s+/, '');
    var m = /^\S+\s+kökünden gelen\s+(\S+)/.exec(rest);
    var tok = m ? m[1] : (rest.split(/\s+/)[0] || '');
    var term = tok.replace(/^["“”'‘’(]+|["“”'‘’).,;:]+$/g, '');
    if (/^(sözcü|kelime|dil|ile|ve|bir|veya)/i.test(term)) term = '';
    return { lang: lang, term: term };
  }

  // etimolojiturkce.com API yanıtı: {"return":"1","word":..,"root":"düz metin köken","explanation":".."}
  function parseEtimTr(text) {
    var j = JSON.parse(text);
    if (!j || String(j['return']) !== '1') return null;
    var raw = j.root || j.explanation || '';
    // HTML özel karakterlerini (&lt; &#8594; &thinsp;) metne çevir
    var d = parseHtml(String(raw));
    var t = norm((d.body || d.documentElement).textContent).replace(/[\u200e\u200f\u202a-\u202e]/g, '');
    return t ? { name: j.word || '', text: t, lang: firstDonor(t) } : null;
  }

  // TDK Güncel Sözlük (gts JSON dizisi)
  function parseTdk(text) {
    var j = JSON.parse(text);
    if (!Array.isArray(j) || !j.length) return null;
    var m = j[0];
    var anlamlar = (m.anlamlarListe || []).slice(0, 4).map(function (a) { return norm(a.anlam); }).filter(Boolean);
    return { name: m.madde || '', lisan: norm(m.lisan || ''), gloss: anlamlar.join('; ') };
  }

  // Etymonline sözcük sayfası: <section data-word> içindeki metin
  function parseEtymonlineWord(html) {
    var doc = parseHtml(html);
    var secs = doc.querySelectorAll('section[data-word]');
    if (!secs || !secs.length) return null;
    var sec = secs[0];
    var txt = norm(sec.textContent);
    if (!txt) return null;
    return { name: sec.getAttribute('data-word') || '', text: txt.length > 1500 ? txt.slice(0, 1497) + '…' : txt };
  }

  // Etymonline arama sayfası (HTML ya da __NEXT_DATA__)
  function parseEtymonline(html) {
    var m = html.match(/<script id="__NEXT_DATA__"[^>]*>([\s\S]*?)<\/script>/);
    if (m) {
      try {
        var nd = JSON.parse(m[1]);
        var pp = (nd.props && nd.props.pageProps) || {};
        var list = pp.searchResults || pp.data || (pp.initialData && pp.initialData.results) || [];
        if (list && list.length) {
          var r = list[0];
          var txt = r.text || r.definition || r.content || r.html || '';
          return { name: r.title || r.word || r.name || '', text: norm(String(txt).replace(/<[^>]*>/g, ' ')) };
        }
      } catch (e) { /* JSON bozuksa DOM yoluna düş */ }
    }
    var doc = parseHtml(html);
    var blk = doc.querySelector('[class*="word__defination"], [class*="word__definition"], [class*="word_"] p, section p');
    if (!blk) return null;
    var nm = doc.querySelector('[class*="word__name"], [class*="word-name"], h1, h2');
    return { name: nm ? norm(nm.textContent) : '', text: norm(blk.textContent) };
  }

  // ------------------------------------------------------------------ Ana arama
  // Wiktionary parse API URL'si
  function parseUrl(base, title) {
    return base + '/w/api.php?action=parse&page=' + enc(title) + '&prop=text&redirects=1&format=json&formatversion=2&origin=*';
  }

  // Sayfayı çeker; {status:'ok'|'missing'|'error', html, title, note}
  async function fetchWikt(http, base, title, o, signal) {
    var r;
    try {
      r = await getRetry(http, parseUrl(base, title), { headers: { 'Api-User-Agent': UA, 'Accept': 'application/json' }, timeoutMs: o.timeoutMs }, signal);
    } catch (e) {
      return { status: 'error', note: 'ağ hatası: ' + (e && e.message ? e.message : e) };
    }
    if (r.status === 429) return { status: 'error', note: 'istek sınırı (429)' };
    if (r.status < 200 || r.status >= 300) return { status: 'error', note: 'HTTP ' + r.status };
    var j;
    try { j = JSON.parse(r.text); } catch (e) { return { status: 'error', note: 'JSON çözülemedi' }; }
    if (j.error) {
      if (/missingtitle|invalidtitle/.test(j.error.code || '')) return { status: 'missing' };
      return { status: 'error', note: j.error.code || 'API hatası' };
    }
    if (!j.parse || !j.parse.text) return { status: 'missing' };
    return { status: 'ok', html: j.parse.text, title: j.parse.title || title };
  }

  // Bulunamayan kelime için benzer başlıklar önerir
  async function suggest(http, query, o, signal) {
    var out = [];
    var tries = [query];
    var low = query.toLocaleLowerCase('tr');
    if (low !== query) tries.push(low);
    for (var i = 0; i < tries.length; i++) {
      try {
        var url = WIKT_EN + '/w/api.php?action=opensearch&search=' + enc(tries[i]) + '&limit=8&namespace=0&format=json&origin=*';
        var r = await getRetry(http, url, { headers: { 'Api-User-Agent': UA }, timeoutMs: o.timeoutMs }, signal);
        if (r.status === 200) {
          var j = JSON.parse(r.text);
          (j[1] || []).forEach(function (t) { if (out.indexOf(t) < 0) out.push(t); });
        }
      } catch (e) { /* öneri alınamazsa sessiz geç */ }
      if (out.length >= 6) break;
    }
    return out.slice(0, 8);
  }

  // Ana arama fonksiyonu
  async function lookup(word, opts) {
    opts = opts || {};
    var http = opts.http || makeHttp();
    var o = { timeoutMs: opts.timeoutMs || DEFAULT_TIMEOUT };
    var signal = opts.signal;
    var progress = typeof opts.onProgress === 'function' ? opts.onProgress : function () { };
    var query = norm(word);
    var result = {
      query: query, resolved: '', found: false, via: null, suggestions: [],
      entries: [], sources: [], fetchedAt: new Date().toISOString()
    };
    if (!query) return result;

    // Kaynak kaydı ekleyen yardımcı
    function addSource(name, url, ok, t0, note) {
      result.sources.push({ name: name, url: url, ok: !!ok, ms: now() - t0, note: note || null });
    }

    // 1) İngilizce Vikisözlük: yazım varyantlarını sırayla dene
    progress('İngilizce Vikisözlük aranıyor…');
    var page = null, usedTitle = null, lastErr = null;
    var vs = variants(query);
    for (var vi = 0; vi < vs.length && !page; vi++) {
      if (signal && signal.aborted) throw new Error('iptal');
      var t0 = now();
      var pg = await fetchWikt(http, WIKT_EN, vs[vi], o, signal);
      var url = WIKT_EN + '/wiki/' + enc(vs[vi].replace(/ /g, '_'));
      if (pg.status === 'ok') {
        page = pg; usedTitle = pg.title;
        addSource('en.wiktionary.org', url, true, t0, vi > 0 ? 'yazım varyantı: ' + vs[vi] : null);
      } else if (pg.status === 'error') {
        lastErr = pg.note;
        addSource('en.wiktionary.org', url, false, t0, pg.note);
        // Ağ/sınır hatasında aynı hatayı tekrar tekrar denemenin anlamı yok
        break;
      } else if (vi === vs.length - 1) {
        addSource('en.wiktionary.org', url, false, t0, 'sayfa yok');
      }
    }

    var built = { entries: [], formTargets: [] };
    if (page) {
      progress('Köken bölümleri ayrıştırılıyor…');
      var sections;
      try {
        sections = parseEnPage(page.html, usedTitle);
        built = buildEntries(sections, usedTitle, WIKT_EN);
        result.found = true;
        result.resolved = usedTitle;
        result.entries = built.entries;
      } catch (e) {
        addSource('en.wiktionary.org (ayrıştırma)', WIKT_EN, false, now(), String(e && e.message || e));
      }
    }

    // Biçim sayfası: etimolojisi olmayıp biçim-of olan bölüm varsa kök sözcüğe git
    if (page && result.found) {
      var needLemma = built.formTargets.filter(function (f) {
        // Aynı dilde zaten etimoloji bulunduysa kök sözcüğe gitmeye gerek yok
        return !result.entries.some(function (e) { return e.language === f.language; });
      });
      // Türkçe/İngilizce biçimler öncelikli
      needLemma.sort(function (a, b) {
        function r(x) { return x.language === 'Turkish' ? 0 : (x.language === 'English' ? 1 : 2); }
        return r(a) - r(b);
      });
      if (needLemma.length && (!result.entries.length || needLemma[0].language === 'Turkish' || needLemma[0].language === 'English')) {
        var f = needLemma[0];
        if (f.target && f.target !== usedTitle) {
          progress('Kök sözcüğe gidiliyor: ' + f.target);
          var tl = now();
          var lp = await fetchWikt(http, WIKT_EN, f.target, o, signal);
          var lurl = WIKT_EN + '/wiki/' + enc(f.target.replace(/ /g, '_'));
          if (lp.status === 'ok') {
            addSource('en.wiktionary.org (kök sözcük)', lurl, true, tl, null);
            var lsec = parseEnPage(lp.html, lp.title);
            var lb = buildEntries(lsec, lp.title, WIKT_EN);
            if (lb.entries.length) {
              // Kök sözcüğün Türkçe/İngilizce girdisini öne al
              var pref = lb.entries.filter(function (e) { return e.language === f.language; });
              var rest = lb.entries.filter(function (e) { return e.language !== f.language; });
              var merged = pref.concat(rest);
              // Özgün sayfadaki, kökte olmayan dillerin girdilerini sona ekle
              result.entries.forEach(function (e) {
                if (!merged.some(function (m) { return m.language === e.language; })) merged.push(e);
              });
              result.entries = sortEntries(merged);
              result.via = usedTitle + ' → ' + lp.title + ' (' + formDescTr(f.desc) + ', kök sözcüğe gidildi)';
              result.resolved = lp.title;
            }
          } else {
            addSource('en.wiktionary.org (kök sözcük)', lurl, false, tl, lp.status === 'missing' ? 'sayfa yok' : lp.note);
          }
        }
      }
    }

    // Yazım varyantıyla bulunduysa bunu via'ya yaz
    if (result.found && !result.via && usedTitle && usedTitle !== query) {
      result.via = query + ' → ' + usedTitle + ' (yazım düzeltildi)';
    }

    // Bulunamadıysa öneriler
    if (!result.found) {
      progress('Benzer sözcükler aranıyor…');
      var ts = now();
      result.suggestions = await suggest(http, query, o, signal);
      addSource('en.wiktionary.org (öneri)', WIKT_EN + '/w/api.php?action=opensearch&search=' + enc(query), result.suggestions.length > 0, ts, result.suggestions.length ? null : 'öneri yok');
      if (lastErr && !result.suggestions.length) result.error = lastErr;
    }

    // 2) Türkçe Vikisözlük (Türkçe sözcükler için ek)
    var looksTurkish = result.entries.some(function (e) { return e.language === 'Turkish'; }) ||
      /[çğıöşüÇĞİÖŞÜ]/.test(query) || (!result.found);
    if (looksTurkish && !(signal && signal.aborted)) {
      progress('Türkçe Vikisözlük (Köken) deneniyor…');
      var tt = now();
      var trTitle = result.found && result.resolved ? result.resolved : query.toLocaleLowerCase('tr');
      var trUrl = WIKT_TR + '/wiki/' + enc(trTitle.replace(/ /g, '_'));
      try {
        var tp = await fetchWikt(http, WIKT_TR, trTitle, { timeoutMs: Math.min(o.timeoutMs, 8000) }, signal);
        if (tp.status === 'missing' && trTitle !== query.toLocaleLowerCase('tr')) tp = await fetchWikt(http, WIKT_TR, query.toLocaleLowerCase('tr'), { timeoutMs: Math.min(o.timeoutMs, 8000) }, signal);
        if (tp.status === 'ok') {
          var paras = parseTrPage(tp.html);
          if (paras.length) {
            var ttext = paras.map(cleanText).join(' ');
            var tetym = {
              text: ttext, chain: trTextChain(ttext, tp.title), parts: [], cognates: [], gloss: null, source: 'tr.wiktionary'
            };
            var trEntry = result.entries.filter(function (e) { return e.language === 'Turkish'; })[0];
            if (trEntry) trEntry.etymologies.push(tetym);
            else {
              result.entries.unshift({
                language: 'Turkish', languageTr: 'Türkçe', pos: [], etymologies: [tetym],
                url: WIKT_TR + '/wiki/' + enc(tp.title.replace(/ /g, '_')) + '#Türkçe'
              });
              if (!result.found) { result.found = true; result.resolved = tp.title; }
            }
            addSource('tr.wiktionary.org', trUrl, true, tt, null);
          } else addSource('tr.wiktionary.org', trUrl, false, tt, 'Köken bölümü yok');
        } else addSource('tr.wiktionary.org', trUrl, false, tt, tp.status === 'missing' ? 'sayfa yok' : tp.note);
      } catch (e) {
        addSource('tr.wiktionary.org', trUrl, false, tt, String(e && e.message || e));
      }
    }

    // 3) Ek kaynaklar. TDK ve etimolojiturkce tarayıcıdan da açık (CORS *); Nişanyan ve Etymonline yalnız Android köprüsüyle.
    var g = (typeof window !== 'undefined') ? window : {};
    var bridge = !!(g.KokenNative || opts.extended);
    var qn = (result.resolved || query).toLocaleLowerCase('tr');
    // Türkçe kaynaklar yalnız Türkçe sözcük gibi görünüyorsa (Türkçe girdi var, bulunamadı ya da Türkçe harf) sorulur
    var turkishish = result.entries.some(function (e) { return e.language === 'Turkish'; }) || !result.found || /[çğıöşüÇĞİÖŞÜ]/.test(query);
    if (!(signal && signal.aborted) && (turkishish || bridge)) {
      progress('Türkçe sözlükler deneniyor…');
      var nt = { timeoutMs: Math.min(o.timeoutMs, 4500), headers: { 'Accept': 'application/json, text/html' } };
      var jobs = [];
      if (turkishish) {
        // TDK Güncel Sözlük (www'li adres şart; www'suz bağlantıyı sıfırlıyor)
        jobs.push((async function () {
          var s = now(), u = 'https://www.sozluk.gov.tr/gts?ara=' + enc(qn);
          try {
            var r = await http(u, nt);
            if (r.status !== 200) { addSource('TDK Güncel Sözlük', u, false, s, 'HTTP ' + r.status); return; }
            var p = parseTdk(r.text);
            if (!p) { addSource('TDK Güncel Sözlük', u, false, s, 'kayıt yok'); return; }
            var txt = (p.lisan ? 'Köken dili: ' + p.lisan + '. ' : 'Türkçe kökenli (TDK\'da köken dili belirtilmemiş). ') + (p.gloss ? 'Anlam: ' + p.gloss : '');
            attachExtra('Turkish', 'tdk', txt, qn, 'https://sozluk.gov.tr/', firstDonor(p.lisan || ''), p.gloss);
            addSource('TDK Güncel Sözlük', u, true, s, null);
          } catch (e) { addSource('TDK Güncel Sözlük', u, false, s, String(e && e.message || e)); }
        })());
        // etimolojiturkce.com (Nişanyan tarzı düz metin köken)
        jobs.push((async function () {
          var s = now(), u = 'https://api.etimolojiturkce.com/word/' + enc(qn);
          try {
            var r = await http(u, nt);
            if (r.status !== 200) { addSource('Etimoloji Türkçe', u, false, s, 'HTTP ' + r.status); return; }
            var p = parseEtimTr(r.text);
            if (!p) { addSource('Etimoloji Türkçe', u, false, s, 'kayıt yok'); return; }
            attachExtra('Turkish', 'etimolojiturkce', p.text, p.name || qn, 'https://www.etimolojiturkce.com/', p.lang, null);
            addSource('Etimoloji Türkçe', u, true, s, null);
          } catch (e) { addSource('Etimoloji Türkçe', u, false, s, String(e && e.message || e)); }
        })());
      }
      if (bridge && turkishish) {
        // Nişanyan Sözlük: sayfa HTML'i (eski /api/words 404 veriyor); CORS yok, yalnız köprü
        jobs.push((async function () {
          var s = now(), u = 'https://www.nisanyansozluk.com/kelime/' + enc(qn);
          try {
            var r = await http(u, { timeoutMs: nt.timeoutMs, headers: { 'Accept': 'text/html' } });
            if (r.status === 404) { addSource('Nişanyan Sözlük', u, false, s, 'kayıt yok'); return; }
            if (r.status !== 200) { addSource('Nişanyan Sözlük', u, false, s, 'HTTP ' + r.status); return; }
            var p = parseNisanyan(r.text);
            if (!p) { addSource('Nişanyan Sözlük', u, false, s, 'kayıt yok'); return; }
            attachExtra('Turkish', 'nisanyan', p.text, qn, u, firstDonor(p.text), null);
            addSource('Nişanyan Sözlük', u, true, s, null);
          } catch (e) { addSource('Nişanyan Sözlük', u, false, s, String(e && e.message || e)); }
        })());
      }
      if (bridge) {
        jobs.push((async function () {
          var ew = (result.resolved || query).toLocaleLowerCase('en');
          var u = 'https://www.etymonline.com/word/' + enc(ew);
          var s = now();
          try {
            // Önce doğrudan sözcük sayfası (<section data-word>), olmazsa arama sayfasına düş
            var r = await http(u, nt);
            var p = (r.status === 200) ? parseEtymonlineWord(r.text) : null;
            if (!p) {
              u = 'https://www.etymonline.com/search?q=' + enc(result.resolved || query);
              r = await http(u, nt);
              if (r.status !== 200) { addSource('Etymonline', u, false, s, 'HTTP ' + r.status); return; }
              p = parseEtymonline(r.text);
            }
            if (!p || !p.text) { addSource('Etymonline', u, false, s, 'kayıt yok'); return; }
            attachExtra('English', 'etymonline', p.text, p.name || query, u);
            addSource('Etymonline', u, true, s, null);
          } catch (e) { addSource('Etymonline', u, false, s, String(e && e.message || e)); }
        })());
      }
      await Promise.all(jobs);
    }

    // Ek kaynağın metnini ilgili dil girdisine etimoloji olarak ekler
    function attachExtra(language, source, text, term, url, srcLang, gloss) {
      var entry = result.entries.filter(function (e) { return e.language === language; })[0];
      var chain = [{ language: language, languageTr: langTr(language), term: term, translit: null, gloss: gloss || null, relation: 'unknown' }];
      if (srcLang) {
        var sl = (typeof srcLang === 'object') ? srcLang.lang : srcLang;
        var st = (typeof srcLang === 'object') ? (srcLang.term || '') : '';
        var en = null;
        Object.keys(LANG_TR).forEach(function (k) { if (LANG_TR[k] === sl) en = k; });
        chain.push({ language: en || sl, languageTr: sl, term: st, translit: null, gloss: null, relation: 'derived' });
      }
      var ety = { text: text, chain: chain, parts: [], cognates: [], gloss: gloss || null, source: source };
      if (entry) entry.etymologies.push(ety);
      else {
        result.entries.push({ language: language, languageTr: langTr(language), pos: [], etymologies: [ety], url: url });
        result.entries = sortEntries(result.entries);
      }
      if (!result.found) { result.found = true; result.resolved = term; }
    }

    result.fetchedAt = new Date().toISOString();
    return result;
  }

  // ------------------------------------------------------------------ Çeviri
  // Metni en fazla max karakterlik parçalara böler (cümle/boşluk sınırından)
  function chunkText(text, max) {
    var out = [];
    var rest = String(text || '').trim();
    while (rest.length > max) {
      var cut = Math.max(rest.lastIndexOf('. ', max), rest.lastIndexOf('; ', max), rest.lastIndexOf(', ', max));
      if (cut < max * 0.4) cut = rest.lastIndexOf(' ', max);
      if (cut <= 0) cut = max;
      out.push(rest.slice(0, cut + 1).trim());
      rest = rest.slice(cut + 1).trim();
    }
    if (rest) out.push(rest);
    return out;
  }

  // Metni Türkçeye çevirir (önce Google gtx, olmazsa MyMemory); hata olursa reddeder
  async function translate(text, target, opts) {
    opts = opts || {};
    target = target || 'tr';
    var http = opts.http || makeHttp();
    var tmo = opts.timeoutMs || DEFAULT_TIMEOUT;
    var chunks = chunkText(text, 450);
    var outs = [];
    for (var i = 0; i < chunks.length; i++) {
      var piece = chunks[i], res = null, err = null;
      // 1. yol: Google gtx
      try {
        var r = await getRetry(http, 'https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=' + enc(target) + '&dt=t&q=' + enc(piece), { timeoutMs: tmo, tries: 2 }, opts.signal);
        if (r.status === 200) {
          var j = JSON.parse(r.text);
          if (Array.isArray(j) && Array.isArray(j[0])) res = j[0].map(function (x) { return x && x[0] ? x[0] : ''; }).join('');
        } else err = 'google HTTP ' + r.status;
      } catch (e) { err = 'google: ' + (e && e.message ? e.message : e); }
      // 2. yol: MyMemory
      if (!res) {
        try {
          var r2 = await getRetry(http, 'https://api.mymemory.translated.net/get?q=' + enc(piece) + '&langpair=en|' + enc(target), { timeoutMs: tmo, tries: 2 }, opts.signal);
          if (r2.status === 200) {
            var j2 = JSON.parse(r2.text);
            var tt = j2 && j2.responseData && j2.responseData.translatedText;
            if (tt && Number(j2.responseStatus) === 200 && !/MYMEMORY WARNING|QUERY LENGTH LIMIT/i.test(tt)) res = tt;
            else err = (err ? err + '; ' : '') + 'mymemory: ' + (j2.responseDetails || 'boş yanıt');
          } else err = (err ? err + '; ' : '') + 'mymemory HTTP ' + r2.status;
        } catch (e) { err = (err ? err + '; ' : '') + 'mymemory: ' + (e && e.message ? e.message : e); }
      }
      if (!res) throw new Error('Çeviri alınamadı (' + (err || 'bilinmeyen hata') + ')');
      outs.push(res.trim());
    }
    return outs.join(' ');
  }

  return {
    lookup: lookup, makeHttp: makeHttp, translate: translate,
    // Testlerin kullandığı iç yardımcılar
    _internal: { firstDonor: firstDonor, firstLangOf: firstLangOf, parseEtimTr: parseEtimTr, parseEnPage: parseEnPage, buildEntries: buildEntries, analyzeEtymology: analyzeEtymology, variants: variants, chunkText: chunkText, parseTdk: parseTdk, parseNisanyan: parseNisanyan, parseEtymonline: parseEtymonline, parseEtymonlineWord: parseEtymonlineWord, parseTrPage: parseTrPage, trTextChain: trTextChain, tokenize: tokenize, sentencesOf: sentencesOf, langTr: langTr }
  };
});
