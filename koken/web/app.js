/* ==========================================================
   Köken arayüzü — app.js
   Yalnızca Etym.* (etym.js) çağırır; ağa kendisi çıkmaz.
   Düz betik (modül değil): file:// ve android_asset altında da çalışır.
   ========================================================== */
(function () {
  'use strict';

  /* ---------- Sabitler ---------- */

  // İlişki türlerinin Türkçe karşılıkları (zincirdeki bağlantı etiketleri için).
  var REL_TR = {
    inherited: 'miras',
    borrowed: 'alıntı',
    derived: 'türetme',
    learned: 'bilgince alıntı',
    calque: 'aktarma çeviri',
    unknown: ''
  };

  // Parça türlerinin Türkçe karşılıkları.
  var KIND_TR = {
    prefix: 'ön ek', suffix: 'ek', root: 'kök', compound: 'birleşik', word: 'sözcük'
  };

  // Örnek kelimeler (boş ekranda dokunulabilir öneriler).
  var EXAMPLES = ['Ahmet', 'kitap', 'kitaplar', 'şeker', 'kahve', 'okyanus'];

  var LS_RECENT = 'koken.son';
  var LS_THEME = 'koken.tema';
  var MAX_RECENT = 8;

  /* ---------- Küçük yardımcılar ---------- */

  // Tek elemanı id ile bul.
  function $(id) { return document.getElementById(id); }

  // Güvenli DOM üretici: metinler textContent ile eklenir (XSS riski yok).
  function el(tag, props) {
    var node = document.createElement(tag);
    var i, k, c;
    props = props || {};
    for (k in props) {
      if (!Object.prototype.hasOwnProperty.call(props, k)) continue;
      if (k === 'class') node.className = props[k];
      else if (k === 'text') node.textContent = props[k];
      else if (k === 'on') { for (var ev in props.on) node.addEventListener(ev, props.on[ev]); }
      else if (props[k] === true) node.setAttribute(k, '');
      else if (props[k] !== false && props[k] != null) node.setAttribute(k, props[k]);
    }
    for (i = 2; i < arguments.length; i++) {
      c = arguments[i];
      if (c == null || c === false) continue;
      node.appendChild(typeof c === 'string' ? document.createTextNode(c) : c);
    }
    return node;
  }

  // Satır içi SVG simgesi (index.html'deki <symbol> takımından).
  function icon(name) {
    var ns = 'http://www.w3.org/2000/svg';
    var svg = document.createElementNS(ns, 'svg');
    var use = document.createElementNS(ns, 'use');
    svg.setAttribute('class', 'i');
    svg.setAttribute('aria-hidden', 'true');
    use.setAttribute('href', '#i-' + name);
    svg.appendChild(use);
    return svg;
  }

  // localStorage okuma/yazma: kapalıysa sessizce yoksay.
  function lsGet(key) { try { return localStorage.getItem(key); } catch (e) { return null; } }
  function lsSet(key, val) { try { localStorage.setItem(key, val); } catch (e) { /* yoksay */ } }
  function lsDel(key) { try { localStorage.removeItem(key); } catch (e) { /* yoksay */ } }

  // Boşlukları sadeleştirip kırp.
  function clean(s) { return String(s == null ? '' : s).replace(/\s+/g, ' ').trim(); }

  // Türkçe duyarlı küçük harf (İ/ı için) — yalnız karşılaştırma amaçlı.
  function lower(s) { try { return s.toLocaleLowerCase('tr'); } catch (e) { return s.toLowerCase(); } }

  // Bir terimin yazı sistemini sınıfla: RTL (Arapça/İbranice), Latin dışı ya da Latin.
  function scriptInfo(term) {
    var t = term || '';
    if (/[֐-׿]/.test(t)) return { cls: 'script-rtl', lang: 'he', dir: 'rtl' };
    if (/[؀-ࣿﭐ-﷿ﹰ-﻿]/.test(t)) return { cls: 'script-rtl', lang: 'ar', dir: 'rtl' };
    if (/[^\u0000-ɏɐ-˿̀-ͯḀ-ỿ -⁯]/.test(t)) return { cls: 'script-other', lang: null, dir: 'auto' };
    return { cls: '', lang: null, dir: 'auto' };
  }

  /* ---------- Durum ---------- */

  var view = $('view');
  var input = $('q');
  var state = {
    reqId: 0,            // en son isteğin numarası (eski yanıtları atmak için)
    controller: null,    // iptal denetleyicisi
    current: null,       // ekrandaki sonuç
    word: '',            // son aranan kelime
    entryIndex: 0,       // seçili dil sekmesi
    trCache: {}          // çeviri önbelleği (metin → çeviri)
  };

  /* ---------- Tema ---------- */

  // Şu an geçerli tema: elle seçilmişse o, yoksa sistem tercihi.
  function effectiveTheme() {
    var t = document.documentElement.getAttribute('data-theme');
    if (t === 'dark' || t === 'light') return t;
    return (window.matchMedia && matchMedia('(prefers-color-scheme: dark)').matches) ? 'dark' : 'light';
  }

  // Simgeyi ve tarayıcı çubuğu rengini temaya göre güncelle.
  function paintTheme() {
    var dark = effectiveTheme() === 'dark';
    var use = $('themeUse');
    if (use) use.setAttribute('href', dark ? '#i-sun' : '#i-moon');
    var meta = document.querySelector('meta[name="theme-color"]');
    if (meta) meta.setAttribute('content', dark ? '#1b1612' : '#f5edda');
    var btn = $('themeBtn');
    if (btn) btn.setAttribute('aria-label', dark ? 'Açık temaya geç' : 'Koyu temaya geç');
  }

  // Düğmeye basılınca tersine çevir ve kaydet.
  function toggleTheme() {
    var next = effectiveTheme() === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    lsSet(LS_THEME, next);
    paintTheme();
  }

  /* ---------- Son aramalar ---------- */

  // Kayıtlı son aramaları oku (bozuksa boş liste).
  function getRecents() {
    try {
      var a = JSON.parse(lsGet(LS_RECENT) || '[]');
      return Array.isArray(a) ? a.filter(function (x) { return typeof x === 'string'; }).slice(0, MAX_RECENT) : [];
    } catch (e) { return []; }
  }

  // Yeni aramayı başa ekle (aynısı varsa öne taşı).
  function pushRecent(word) {
    var key = lower(word);
    var list = getRecents().filter(function (w) { return lower(w) !== key; });
    list.unshift(word);
    lsSet(LS_RECENT, JSON.stringify(list.slice(0, MAX_RECENT)));
    paintRecents();
  }

  // Son aramalar çiplerini çiz; liste boşsa bölümü gizle.
  function paintRecents() {
    var list = getRecents();
    var ul = $('recentList');
    ul.textContent = '';
    list.forEach(function (w) {
      var li = el('li');
      li.appendChild(el('button', { type: 'button', class: 'chip', on: { click: function () { goWord(w); } } },
        icon('clock'), w));
      ul.appendChild(li);
    });
    $('recents').hidden = list.length === 0;
  }

  /* ---------- Ağ durumu ---------- */

  // Çevrimdışı şeridini göster/gizle.
  function paintNet() {
    $('netBanner').hidden = navigator.onLine !== false;
  }

  /* ---------- Ekran durumları ---------- */

  // Sonuç alanını boşaltıp yeni içerik koy.
  function show(node) {
    view.textContent = '';
    if (node) view.appendChild(node);
  }

  // Başlangıç / boş durum: kısa tanıtım ve örnek kelimeler.
  function renderHome() {
    document.title = 'Köken';
    state.current = null;
    var chips = el('ul', { class: 'chips' });
    EXAMPLES.forEach(function (w) {
      var li = el('li');
      li.appendChild(el('button', { type: 'button', class: 'chip', on: { click: function () { goWord(w); } } }, w));
      chips.appendChild(li);
    });
    show(el('section', { class: 'state', 'aria-labelledby': 'homeTitle' },
      withClass(icon('leaf'), 'big-ic'),
      el('h2', { id: 'homeTitle', text: 'Henüz bir şey aramadınız' }),
      el('p', { text: 'Bir kelime yazın; hangi dilden geldiğini, hangi parçalardan oluştuğunu ve zaman içindeki yolunu gösterelim. Çekimli yazsanız da (kitaplar) kök sözcüğe gideriz.' }),
      el('p', { class: 'eyebrow', style: 'margin-top:16px;margin-bottom:8px', text: 'Deneyin' }),
      chips));
  }

  // SVG elemanına sınıf ekle (HTML sınıf API'si SVG'de de çalışır).
  function withClass(svg, cls) { svg.setAttribute('class', 'i ' + cls); return svg; }

  // Yükleniyor: iskelet + ilerleme mesajı.
  function renderLoading(word) {
    var nodes = el('div', { 'aria-hidden': 'true' });
    for (var i = 0; i < 3; i++) {
      nodes.appendChild(el('div', { class: 'skel-node' },
        el('div', { class: 'bone dot' }),
        el('div', { class: 'col' }, el('div', { class: 'bone l1' }), el('div', { class: 'bone l2' }))));
    }
    var box = el('section', { class: 'skel', 'aria-busy': 'true' },
      el('p', { class: 'progress', id: 'progress', role: 'status', text: '“' + word + '” aranıyor…' }),
      el('div', { class: 'bone w', 'aria-hidden': 'true' }),
      nodes);
    show(box);
    document.title = word + ' — Köken';
  }

  // Aramayı yürüten motorun ilerleme mesajını yaz.
  function setProgress(msg) {
    var p = $('progress');
    if (p && msg) p.textContent = String(msg);
  }

  // Hata / çevrimdışı kutusu: açıklama + "Tekrar dene".
  function renderError(opts) {
    var retry = el('button', { type: 'button', class: 'btn', on: { click: function () { run(state.word); } } },
      icon('refresh'), 'Tekrar dene');
    var box = el('section', { class: 'state err', role: 'alert' },
      withClass(icon(opts.offline ? 'wifi-off' : 'alert'), 'big-ic'),
      el('h2', { text: opts.title }),
      el('p', { text: opts.body }),
      el('div', { class: 'actions' }, retry));
    if (opts.sources) box.appendChild(el('div', { style: 'margin-top:16px' }, sourceStrip(opts.sources)));
    show(box);
  }

  // Bulunamadı: öneriler varsa çip olarak sun.
  function renderNotFound(res) {
    var box = el('section', { class: 'state', 'aria-labelledby': 'nfTitle' },
      withClass(icon('search'), 'big-ic'),
      el('h2', { id: 'nfTitle', text: '“' + res.query + '” için kayıt bulunamadı' }),
      el('p', { text: 'Yazımı kontrol edin; Türkçe karakterlerle (ç, ğ, ı, ö, ş, ü) deneyin ya da sözcüğün yalın biçimini yazın.' }));
    if (res.suggestions && res.suggestions.length) {
      box.appendChild(el('p', { class: 'eyebrow', style: 'margin-top:16px', text: 'Şunu mu demek istediniz?' }));
      var ul = el('ul', { class: 'chips' });
      res.suggestions.slice(0, 8).forEach(function (w) {
        var li = el('li');
        li.appendChild(el('button', { type: 'button', class: 'chip', on: { click: function () { goWord(w); } } }, w));
        ul.appendChild(li);
      });
      box.appendChild(ul);
    }
    // Bazı kaynaklara ulaşılamadıysa "yok" demek yanıltıcı olur: uyar ve tekrar dene sun.
    var partial = (res.sources || []).some(function (x) { return !x.ok; });
    if (partial) {
      box.appendChild(el('p', { style: 'margin-top:12px', text: 'Bazı kaynaklara ulaşılamadı; sonuç eksik olabilir.' }));
      box.appendChild(el('div', { class: 'actions' }, el('button', { type: 'button', class: 'btn', on: { click: function () { run(state.word); } } },
        icon('refresh'), 'Tekrar dene')));
    }
    if (res.sources && res.sources.length) box.appendChild(el('div', { style: 'margin-top:16px' }, sourceStrip(res.sources)));
    show(box);
    document.title = res.query + ' — Köken';
  }

  /* ---------- Sonuç parçaları ---------- */

  // Kaynak durum şeridi: hangi kaynak çalıştı / çalışmadı (simge + yazı, yalnız renk değil).
  function sourceStrip(sources) {
    var wrap = el('div', { class: 'strip', role: 'list', 'aria-label': 'Kaynak durumu' });
    (sources || []).forEach(function (s) {
      var ms = typeof s.ms === 'number' ? ' ' + Math.round(s.ms) + ' ms' : '';
      var chip = el('span', { class: 'src ' + (s.ok ? 'ok' : 'fail'), role: 'listitem', title: s.note || '' },
        icon(s.ok ? 'check' : 'x'),
        el('span', { text: s.name + (s.ok ? '' : ' (ulaşılamadı)') }),
        ms ? el('small', { text: ms }) : null);
      wrap.appendChild(chip);
    });
    return wrap;
  }

  // Yol haritası: en yakın atadan en eskiye inen dikey zaman çizgisi.
  function timeline(chain) {
    if (!chain || !chain.length) {
      return el('p', { class: 'chain-empty', text: 'Bu madde için yol haritası çıkarılamadı; aşağıdaki ham metne bakın.' });
    }
    var ol = el('ol', { class: 'timeline' });
    chain.forEach(function (n, i) {
      var sc = scriptInfo(n.term);
      var depth = Math.min(i, 5);
      var li = el('li', { class: 'node d' + depth });
      li.appendChild(el('span', { class: 'dot', 'aria-hidden': 'true' }));
      li.appendChild(el('span', { class: 'lang-label', text: n.languageTr || n.language || '' }));
      var termAttrs = { class: 'term ' + sc.cls, dir: sc.dir };
      if (sc.lang) termAttrs.lang = sc.lang;
      li.appendChild(el('div', termAttrs, n.term || '—'));
      if (n.translit) li.appendChild(el('div', { class: 'translit', text: n.translit }));
      if (n.gloss) li.appendChild(el('div', { class: 'gloss', text: n.gloss }));
      // Bu katman ile bir sonraki (daha eski) katman arasındaki ilişki.
      var rel = relationBetween(chain, i);
      if (rel) li.appendChild(el('div', { class: 'rel' }, icon('arrow'), rel));
      ol.appendChild(li);
    });
    return ol;
  }

  // i. katmanın bir sonraki (daha eski) katmana bağlanış biçimini yazıya çevir.
  // Motorda "relation", o düğümün kendisinden önceki (daha yakın) düğüme nasıl ulaştığını anlatır.
  function relationBetween(chain, i) {
    var next = chain[i + 1];
    if (!next) return '';
    var label = REL_TR[next.relation] || '';
    if (!label) return '';
    return label + ' · ' + (next.languageTr || next.language || '');
  }

  // Parçalar: ek / kök / birleşik çipleri.
  function partsList(parts) {
    var wrap = el('ul', { class: 'parts' });
    parts.forEach(function (p) {
      var li = el('li', { class: 'part' },
        el('span', { class: 'k', text: KIND_TR[p.kind] || p.kind || '' }),
        el('span', { class: 't', text: p.term }));
      if (p.language) li.appendChild(el('span', { class: 'g', text: p.language }));
      if (p.gloss) li.appendChild(el('span', { class: 'g', text: '“' + p.gloss + '”' }));
      wrap.appendChild(li);
    });
    return wrap;
  }

  // Bölüm kapsayıcısı: başlık + içerik.
  function section(title, body) {
    return el('section', { class: 'sec' }, el('h3', { text: title }), body);
  }

  // Bir köken (Etymology) bloğunu çiz: yol haritası, parçalar, anlam, akrabalar, ham metin.
  function etymBlock(ety, idx, total, lang) {
    var box = el('div');
    if (total > 1) box.appendChild(el('h3', { class: 'ety-title', text: 'Köken ' + (idx + 1) }));
    box.appendChild(section('Yol haritası', timeline(ety.chain)));
    if (ety.parts && ety.parts.length) box.appendChild(section('Parçalar', partsList(ety.parts)));
    if (ety.gloss) box.appendChild(section('Anlam', el('p', { class: 'meaning', text: ety.gloss })));
    if (ety.cognates && ety.cognates.length) {
      var cog = el('div', { class: 'cog' });
      ety.cognates.slice(0, 14).forEach(function (c) {
        cog.appendChild(el('span', {}, el('b', { text: c.language }), c.term));
      });
      box.appendChild(section('Akraba sözcükler', cog));
    }
    if (ety.text) box.appendChild(rawSection(ety.text));
    return box;
  }

  // Ham metin (İngilizce) + "Türkçeye çevir" düğmesi (kullanıcı basınca çalışır).
  function rawSection(text) {
    var out = el('div', { 'aria-live': 'polite' });
    var btn = el('button', { type: 'button', class: 'btn' }, icon('lang'), 'Türkçeye çevir');
    // Daha önce çevrildiyse önbellekten göster.
    if (state.trCache[text]) paintTranslation(out, state.trCache[text], false);
    btn.addEventListener('click', function () { doTranslate(text, btn, out); });
    return section('Ham metin (İngilizce)',
      el('div', {}, el('blockquote', { class: 'raw', lang: 'en', text: text }), btn, out));
  }

  // Çeviri kutusunu doldur (hata ise kırmızı kenarlıklı).
  function paintTranslation(out, text, bad) {
    out.textContent = '';
    out.appendChild(el('div', { class: 'tr-box' + (bad ? ' bad' : ''), lang: bad ? null : 'tr' },
      el('span', { text: text }),
      bad ? null : el('small', { text: 'Otomatik çeviri; hata içerebilir.' })));
  }

  // "Türkçeye çevir" düğmesinin işi: Etym.translate çağır, sonucu göster.
  function doTranslate(text, btn, out) {
    if (state.trCache[text]) { paintTranslation(out, state.trCache[text], false); return; }
    if (navigator.onLine === false) { paintTranslation(out, 'Çeviri için internet gerekir.', true); return; }
    if (typeof Etym.translate !== 'function') { paintTranslation(out, 'Bu sürümde çeviri yok.', true); return; }
    btn.setAttribute('aria-busy', 'true');
    var label = btn.lastChild;
    var old = label.textContent;
    label.textContent = 'Çevriliyor…';
    var p;
    try { p = Promise.resolve(Etym.translate(text, 'tr', {})); } catch (e) { p = Promise.reject(e); }
    p.then(function (tr) {
      tr = clean(tr);
      if (!tr) throw new Error('boş');
      state.trCache[text] = tr;
      paintTranslation(out, tr, false);
    }).catch(function () {
      paintTranslation(out, 'Çeviri alınamadı. Bağlantınızı kontrol edip tekrar deneyin.', true);
    }).then(function () {
      btn.removeAttribute('aria-busy');
      label.textContent = old;
    });
  }

  // Dış bağlantılar: yinelenenleri ayıkla, yeni sekmede aç.
  function sourceLinks(res) {
    var seen = {};
    var items = [];
    (res.entries || []).forEach(function (e) {
      if (e.url && !seen[e.url]) { seen[e.url] = 1; items.push({ url: e.url, label: 'Vikisözlük · ' + (e.languageTr || e.language) }); }
    });
    (res.sources || []).forEach(function (s) {
      if (s.ok && s.url && !seen[s.url]) { seen[s.url] = 1; items.push({ url: s.url, label: s.name }); }
    });
    if (!items.length) return null;
    var wrap = el('div', { class: 'links' });
    items.forEach(function (it) {
      wrap.appendChild(el('a', { href: it.url, target: '_blank', rel: 'noopener noreferrer' }, icon('ext'), it.label));
    });
    return section('Kaynak bağlantıları', wrap);
  }

  // Paylaşım metnini hazırla: kelime, zincir, anlam.
  function shareText(res) {
    var entry = res.entries[state.entryIndex] || res.entries[0];
    var ety = entry && entry.etymologies && entry.etymologies[0];
    var lines = [res.resolved + ' — köken'];
    if (ety && ety.chain && ety.chain.length) {
      lines.push(ety.chain.map(function (n) { return (n.languageTr || n.language) + ' ' + n.term; }).join(' ← '));
    }
    if (ety && ety.gloss) lines.push('Anlam: ' + ety.gloss);
    lines.push('Köken uygulaması');
    return lines.join('\n');
  }

  // Paylaş: navigator.share varsa onu kullan, yoksa panoya kopyala.
  function doShare(res) {
    var text = shareText(res);
    var data = { title: res.resolved + ' — Köken', text: text };
    if (/^https?:/.test(location.href) && location.hostname !== 'appassets.androidplatform.net') data.url = location.href;
    if (navigator.share) {
      navigator.share(data).catch(function (e) {
        // Kullanıcı vazgeçtiyse sessiz; başka hata olursa kopyalamaya düş.
        if (e && e.name !== 'AbortError') copyText(text);
      });
    } else {
      copyText(text);
    }
  }

  // Panoya kopyala (yeni API yoksa eski yöntem) ve bildirim göster.
  function copyText(text) {
    function ok() { toast('Panoya kopyalandı'); }
    function legacy() {
      var ta = el('textarea', { style: 'position:fixed;opacity:0;left:-999px', 'aria-hidden': 'true' });
      ta.value = text;
      document.body.appendChild(ta);
      ta.select();
      var done = false;
      try { done = document.execCommand('copy'); } catch (e) { done = false; }
      document.body.removeChild(ta);
      toast(done ? 'Panoya kopyalandı' : 'Kopyalanamadı');
    }
    if (navigator.clipboard && navigator.clipboard.writeText) {
      navigator.clipboard.writeText(text).then(ok, legacy);
    } else {
      legacy();
    }
  }

  // Kısa süreli bildirim.
  var toastTimer = null;
  function toast(msg) {
    var t = $('toast');
    t.textContent = msg;
    t.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(function () { t.classList.remove('show'); }, 2200);
  }

  // Sonuç ekranının tamamı.
  function renderResult(res) {
    state.current = res;
    if (state.entryIndex >= res.entries.length) state.entryIndex = 0;
    document.title = res.resolved + ' — Köken';

    var root = el('article', { class: 'result' });
    var head = el('header', { class: 'res-head' });
    if (res.via) head.appendChild(el('p', { class: 'via' }, icon('arrow'), el('span', { text: res.via })));
    head.appendChild(el('h2', { class: 'word', tabindex: '-1', id: 'wordTitle', text: res.resolved }));
    root.appendChild(head);

    // Birden çok dil girdisi varsa sekmeler (Türkçe önce — motor sıralar).
    var panel = el('div', { id: 'panel', role: 'tabpanel' });
    if (res.entries.length > 1) root.appendChild(tabsFor(res, panel));
    root.appendChild(panel);
    paintPanel(res, panel);

    var links = sourceLinks(res);
    if (links) root.appendChild(links);
    root.appendChild(section('Kaynak durumu', sourceStrip(res.sources)));
    root.appendChild(el('div', { class: 'share-row' },
      el('button', { type: 'button', class: 'btn', on: { click: function () { doShare(res); } } },
        icon(navigator.share ? 'share' : 'copy'), navigator.share ? 'Paylaş' : 'Kopyala / paylaş')));
    show(root);

    // Başlığa odaklan ve görünür kıl (ekran okuyucu + kaydırma).
    var w = $('wordTitle');
    if (w) {
      try { w.focus({ preventScroll: true }); } catch (e) { /* eski tarayıcı */ }
      var reduce = window.matchMedia && matchMedia('(prefers-reduced-motion: reduce)').matches;
      try { w.scrollIntoView({ block: 'start', behavior: reduce ? 'auto' : 'smooth' }); } catch (e) { w.scrollIntoView(); }
    }
  }

  // Seçili dil girdisinin içeriğini panele çiz.
  function paintPanel(res, panel) {
    var entry = res.entries[state.entryIndex];
    panel.textContent = '';
    var meta = el('div', { class: 'pos' }, el('span', { class: 'tag lang', text: entry.languageTr || entry.language }));
    (entry.pos || []).forEach(function (p) { meta.appendChild(el('span', { class: 'tag', text: p })); });
    panel.appendChild(meta);
    var list = entry.etymologies || [];
    if (!list.length) {
      panel.appendChild(section('Yol haritası', el('p', { class: 'chain-empty', text: 'Bu dil için köken bilgisi bulunmuyor.' })));
    }
    list.forEach(function (e, i) { panel.appendChild(etymBlock(e, i, list.length, entry.language)); });
  }

  // Dil sekmeleri (ok tuşlarıyla gezilir).
  function tabsFor(res, panel) {
    var bar = el('div', { class: 'tabs', role: 'tablist', 'aria-label': 'Diller' });
    function select(i, focus) {
      state.entryIndex = i;
      Array.prototype.forEach.call(bar.children, function (b, j) {
        b.setAttribute('aria-selected', j === i ? 'true' : 'false');
        b.tabIndex = j === i ? 0 : -1;
      });
      paintPanel(res, panel);
      if (focus) bar.children[i].focus();
    }
    res.entries.forEach(function (e, i) {
      var b = el('button', { type: 'button', class: 'tab', role: 'tab', text: e.languageTr || e.language, on: {
        click: function () { select(i, false); },
        keydown: function (ev) {
          var n = res.entries.length;
          if (ev.key === 'ArrowRight') { ev.preventDefault(); select((i + 1) % n, true); }
          else if (ev.key === 'ArrowLeft') { ev.preventDefault(); select((i - 1 + n) % n, true); }
        }
      } });
      bar.appendChild(b);
    });
    select(state.entryIndex, false);
    return bar;
  }

  /* ---------- Arama akışı ---------- */

  // Sonuç, hiçbir kaynağa ulaşılamadığı için mi boş? (bağlantı sorunu ayrımı)
  function allSourcesFailed(res) {
    var s = res.sources || [];
    return s.length > 0 && s.every(function (x) { return !x.ok; });
  }

  // Aramayı gerçekten yürüt: yükleniyor → Etym.lookup → uygun durum.
  function run(word) {
    word = clean(word);
    state.word = word;
    state.entryIndex = 0;
    // Önceki isteği iptal et; eski yanıt gelirse reqId ile atılır.
    if (state.controller) { try { state.controller.abort(); } catch (e) { /* yoksay */ } }
    var id = ++state.reqId;

    // Çevrimdışıysa ağa hiç çıkma, anlamlı mesaj ver.
    if (navigator.onLine === false) {
      renderError({
        offline: true,
        title: 'Çevrimdışısınız',
        body: 'Köken, kelimeleri internetten arar. Bağlantınız gelince “Tekrar dene”ye dokunun.'
      });
      return;
    }

    state.controller = (typeof AbortController === 'function') ? new AbortController() : null;
    renderLoading(word);
    var p;
    try {
      p = Promise.resolve(Etym.lookup(word, {
        signal: state.controller ? state.controller.signal : undefined,
        onProgress: function (m) { if (id === state.reqId) setProgress(m); }
      }));
    } catch (e) { p = Promise.reject(e); }

    p.then(function (res) {
      if (id !== state.reqId) return; // daha yeni bir arama başladı
      if (!res) throw new Error('boş yanıt');
      res.entries = res.entries || [];
      res.sources = res.sources || [];
      res.query = res.query || word;
      if (res.found && res.entries.length) {
        pushRecent(res.resolved || word);
        renderResult(res);
      } else if (allSourcesFailed(res)) {
        renderError({
          offline: navigator.onLine === false,
          title: 'Kaynaklara ulaşılamadı',
          body: 'İnternet bağlantınızı kontrol edin; kaynak siteler geçici olarak yanıt vermiyor olabilir.',
          sources: res.sources
        });
      } else {
        renderNotFound(res);
      }
    }).catch(function (err) {
      if (id !== state.reqId) return;
      if (err && err.name === 'AbortError') return;
      var off = navigator.onLine === false;
      renderError({
        offline: off,
        title: off ? 'Çevrimdışısınız' : 'Bir şeyler ters gitti',
        body: off ? 'Bağlantınız gelince tekrar deneyin.'
                  : 'Arama tamamlanamadı. Bağlantınızı kontrol edip tekrar deneyin.'
      });
    }).then(function () {
      if (id === state.reqId) $('goBtn').removeAttribute('disabled');
    });
  }

  /* ---------- Yönlendirme (geri/ileri) ---------- */

  // Adres çubuğundaki #q=kelime değerini oku.
  function hashWord() {
    var m = /^#q=(.*)$/.exec(location.hash || '');
    if (!m) return '';
    try { return clean(decodeURIComponent(m[1].replace(/\+/g, ' '))); } catch (e) { return clean(m[1]); }
  }

  // Bir kelimeye git: adresi değiştir (geçmişe girer) → hashchange aramayı başlatır.
  function goWord(word) {
    word = clean(word);
    if (!word) return;
    input.value = word;
    syncClear();
    if (hashWord() === word) { run(word); return; } // aynı adres: yeniden ara
    location.hash = '#q=' + encodeURIComponent(word);
  }

  // Adres değişince (ileri/geri dahil) ekranı ona göre kur.
  function route() {
    var w = hashWord();
    if (!w) {
      // Ana sayfa: bekleyen isteği bırak, boş ekranı göster.
      state.reqId++;
      if (state.controller) { try { state.controller.abort(); } catch (e) { /* yoksay */ } }
      input.value = '';
      syncClear();
      renderHome();
      return;
    }
    input.value = w;
    syncClear();
    run(w);
  }

  /* ---------- Form ---------- */

  // Temizle düğmesini yalnız yazı varken göster.
  function syncClear() { $('clearBtn').hidden = !input.value; }

  // Formu gönder: boşsa nazikçe uyar, doluysa ara.
  function onSubmit(ev) {
    ev.preventDefault();
    var w = clean(input.value);
    if (!w) {
      $('hint').textContent = 'Önce bir kelime yazın.';
      input.focus();
      return;
    }
    $('hint').textContent = '';
    try { input.blur(); } catch (e) { /* yoksay */ }
    goWord(w);
  }

  /* ---------- Servis çalışanı ---------- */

  // Kabuk önbelleği için kaydol; hata uygulamayı asla bozmasın.
  function registerSW() {
    try {
      if (!('serviceWorker' in navigator)) return;
      if (!/^https?:$/.test(location.protocol)) return;   // file:// ve android_asset: kayıt yok
      if (window.KokenNative) return;                     // Android'de dosyalar zaten yerel
      navigator.serviceWorker.register('sw.js').catch(function () { /* kayıt başarısızsa sessiz geç */ });
    } catch (e) { /* yoksay */ }
  }

  /* ---------- Başlat ---------- */

  function init() {
    // Motor yüklenmediyse anlamlı hata göster (uygulama çökmesin).
    if (typeof window.Etym === 'undefined') {
      renderError({ title: 'Motor yüklenemedi', body: 'Sayfayı yenileyip tekrar deneyin.' });
      return;
    }
    paintTheme();
    paintRecents();
    paintNet();
    $('themeBtn').addEventListener('click', toggleTheme);
    $('searchForm').addEventListener('submit', onSubmit);
    $('clearBtn').addEventListener('click', function () { input.value = ''; syncClear(); input.focus(); });
    $('clearRecents').addEventListener('click', function () { lsDel(LS_RECENT); paintRecents(); });
    $('brand').addEventListener('click', function (ev) {
      ev.preventDefault();
      if (location.hash) location.hash = ''; else route();
    });
    input.addEventListener('input', function () { syncClear(); $('hint').textContent = ''; });
    window.addEventListener('hashchange', route);
    window.addEventListener('online', paintNet);
    window.addEventListener('offline', paintNet);
    if (window.matchMedia) {
      try { matchMedia('(prefers-color-scheme: dark)').addEventListener('change', paintTheme); } catch (e) { /* eski tarayıcı */ }
    }
    route();
    registerSW();
  }

  // Betik gövdesi sayfa sonunda yüklendiği için DOM hazır; yine de güvenli tarafta kal.
  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', init);
  else init();
})();
