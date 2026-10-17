/* Sahte Etym: gerçek motor (web/etym.js) yokken arayüzü geliştirmek için.
   Sözleşmedeki Result biçimini üretir. window.Etym olarak yüklenir. */
(function () {
  'use strict';

  // Belirli bir süre bekle (iskeleti görmek için); iptal edilirse reddet.
  function wait(ms, signal) {
    return new Promise(function (res, rej) {
      var t = setTimeout(res, ms);
      if (signal) signal.addEventListener('abort', function () {
        clearTimeout(t); var e = new Error('aborted'); e.name = 'AbortError'; rej(e);
      });
    });
  }

  // Kaynak durum satırı üretici.
  function src(name, ok, note) {
    return { name: name, url: 'https://en.wiktionary.org/', ok: ok, ms: 120, note: note || null };
  }

  // Ahmet örneği (Türkçe + Arapça girdisi).
  var AHMET = {
    query: 'Ahmet', resolved: 'Ahmet', found: true, via: null, suggestions: [],
    entries: [{
      language: 'Turkish', languageTr: 'Türkçe', pos: ['proper noun'],
      url: 'https://en.wiktionary.org/wiki/Ahmet#Turkish',
      etymologies: [{
        text: 'From Ottoman Turkish احمد (aḥmed), from Arabic أَحْمَد (ʾaḥmad, "most praiseworthy"), from Proto-Semitic *ḥamad-.',
        chain: [
          { language: 'Turkish', languageTr: 'Türkçe', term: 'Ahmet', translit: null, gloss: null, relation: 'unknown' },
          { language: 'Ottoman Turkish', languageTr: 'Osmanlı Türkçesi', term: 'احمد', translit: 'aḥmed', gloss: null, relation: 'inherited' },
          { language: 'Arabic', languageTr: 'Arapça', term: 'أَحْمَد', translit: 'ʾaḥmad', gloss: 'en çok övülen', relation: 'borrowed' },
          { language: 'Proto-Semitic', languageTr: 'Proto-Sami', term: '*ḥamad-', translit: null, gloss: 'övmek', relation: 'inherited' }
        ],
        parts: [], cognates: [{ language: 'Hebrew', term: 'חמד' }], gloss: 'en çok övülen, çok övgüye değer'
      }]
    }, {
      language: 'English', languageTr: 'İngilizce', pos: ['proper noun'], url: 'https://en.wiktionary.org/wiki/Ahmet#English',
      etymologies: [{ text: 'From Turkish Ahmet.', chain: [
        { language: 'English', languageTr: 'İngilizce', term: 'Ahmet', translit: null, gloss: null, relation: 'unknown' },
        { language: 'Turkish', languageTr: 'Türkçe', term: 'Ahmet', translit: null, gloss: null, relation: 'borrowed' }
      ], parts: [], cognates: [], gloss: null }]
    }],
    sources: [src('Vikisözlük (İng.)', true), src('Vikisözlük (Tr.)', false, 'madde yok'), src('Nişanyan', false, 'yalnız Android')],
    fetchedAt: new Date().toISOString()
  };

  // kitap örneği (çok parçalı + iki köken).
  var KITAP = {
    query: 'kitap', resolved: 'kitap', found: true, via: null, suggestions: [],
    entries: [{
      language: 'Turkish', languageTr: 'Türkçe', pos: ['noun'], url: 'https://en.wiktionary.org/wiki/kitap#Turkish',
      etymologies: [{
        text: 'From Ottoman Turkish كتاب (kitab), from Arabic كِتَاب (kitāb, "book"), from Proto-Semitic *katab- ("to write").',
        chain: [
          { language: 'Turkish', languageTr: 'Türkçe', term: 'kitap', translit: null, gloss: null, relation: 'unknown' },
          { language: 'Ottoman Turkish', languageTr: 'Osmanlı Türkçesi', term: 'كتاب', translit: 'kitab', gloss: null, relation: 'inherited' },
          { language: 'Arabic', languageTr: 'Arapça', term: 'كِتَاب', translit: 'kitāb', gloss: 'kitap', relation: 'borrowed' },
          { language: 'Proto-Semitic', languageTr: 'Proto-Sami', term: '*katab-', translit: null, gloss: 'yazmak', relation: 'inherited' }
        ],
        parts: [{ term: 'ktb', kind: 'root', gloss: 'yazmak', language: 'Arapça' }, { term: 'kitāb', kind: 'word', gloss: 'kitap', language: 'Arapça' }],
        cognates: [{ language: 'Persian', term: 'کتاب' }, { language: 'Azerbaijani', term: 'kitab' }],
        gloss: 'yazılı sayfaların ciltlenmiş bütünü'
      }, {
        text: 'Second etymology: unrelated test entry for stacked sections.',
        chain: [{ language: 'Turkish', languageTr: 'Türkçe', term: 'kitap', translit: null, gloss: null, relation: 'unknown' }],
        parts: [], cognates: [], gloss: null
      }]
    }],
    sources: [src('Vikisözlük (İng.)', true), src('Vikisözlük (Tr.)', true)],
    fetchedAt: new Date().toISOString()
  };

  window.Etym = {
    // Arama: ağ gecikmesini taklit eder; özel kelimeler farklı durumları tetikler.
    lookup: function (word, opts) {
      opts = opts || {};
      var w = String(word).trim().toLocaleLowerCase('tr');
      if (opts.onProgress) opts.onProgress('Vikisözlük (İng.) okunuyor…');
      return wait(900, opts.signal).then(function () {
        if (w === 'ahmet') return AHMET;
        if (w === 'kitap') return KITAP;
        if (w === 'kitaplar') {
          var r = JSON.parse(JSON.stringify(KITAP));
          r.query = 'kitaplar'; r.via = 'kitaplar → kitap (çoğul biçim, kök sözcüğe gidildi)'; return r;
        }
        if (w === 'hata') throw new Error('sahte ağ hatası');
        if (w === 'agyok') return { query: word, resolved: word, found: false, via: null, suggestions: [], entries: [],
          sources: [src('Vikisözlük (İng.)', false, 'zaman aşımı'), src('Vikisözlük (Tr.)', false, 'zaman aşımı')], fetchedAt: '' };
        return { query: word, resolved: word, found: false, via: null, suggestions: ['kitap', 'kitle'],
          entries: [], sources: [src('Vikisözlük (İng.)', true, 'madde yok'), src('Vikisözlük (Tr.)', true, 'madde yok')], fetchedAt: '' };
      });
    },
    makeHttp: function () { return function () { return Promise.reject(new Error('sahte')); }; },
    translate: function (text) { return wait(500).then(function () { return '(çeviri) ' + text.slice(0, 60) + '…'; }); }
  };
})();
