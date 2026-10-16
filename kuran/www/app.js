/* Kur'an Okuyucu: kütüphanesiz, tek dosya. Veri: data.js (window.Q) */
(function () {
  'use strict';
  var Q = window.Q;                                   // Kur'an verisi (ayetler, sure bilgisi, sayfa başlangıçları)
  var $ = function (id) { return document.getElementById(id); };  // kısayol: id ile eleman bul
  var root = document.documentElement;               // <html> elemanı (CSS değişkenleri buraya yazılır)
  var N = 604;                                        // toplam sayfa sayısı (Medine Mushafı)

  /* ---------- Kalıcı depo: kaldığın yer, yazı boyutu, tema, yer imleri ---------- */
  var store = {
    get: function (k, d) { try { var v = localStorage.getItem('q.' + k); return v === null ? d : JSON.parse(v); } catch (e) { return d; } },  // oku
    set: function (k, v) { try { localStorage.setItem('q.' + k, JSON.stringify(v)); } catch (e) { } }                                           // yaz
  };

  /* ---------- Türetilmiş tablolar (bir kez hesaplanır, çok hafif) ---------- */
  var A2S = new Uint8Array(6236);                     // ayet indeksi -> sure indeksi
  var surahPage = [];                                 // sure indeksi -> başladığı sayfa
  (function () {
    var pi = 1;                                       // sayfa sayacı
    for (var s = 0; s < 114; s++) {                   // her sure için
      var st = Q.s[s][0], cnt = Q.s[s][1];            // ilk ayet indeksi, ayet sayısı
      for (var g = st; g < st + cnt; g++) A2S[g] = s; // sure haritasını doldur
      while (pi < N && Q.p[pi] <= st) pi++;           // sure başlangıcının sayfasını bul
      surahPage[s] = pi;                              // kaydet
    }
  })();
  function juzOf(p) { var c = 1; while (c < 30 && Q.j[c] <= p) c++; return c; }   // sayfanın cüzü
  function arNum(n) { return String(n).replace(/\d/g, function (d) { return String.fromCharCode(0x660 + +d); }); } // Arap-Hint rakamı

  /* ---------- Durum ---------- */
  var page = Math.min(N, Math.max(1, +store.get('page', 1) || 1));   // şu anki sayfa (son kalınan yer)
  var fs = +store.get('fs', 0) || 0;                  // yazı boyutu (px). 0 = ekrana göre otomatik
  var theme = store.get('theme', '');                 // '', 'light', 'sepia', 'dark'
  var bookmarks = store.get('bm', []);                // yer imi sayfaları
  var backTo = 0;                                     // "Git" ile atlamadan önceki sayfa
  var W = 0, minFs = 16, maxFs = 100;                 // alan genişliği, yazı alt/üst sınırı
  var animating = false;                              // sayfa çevirme animasyonu sürüyor mu

  var view = $('view');
  var panes = { p: $('pgP'), c: $('pgC'), n: $('pgN') };   // önceki(sağda) / şimdiki / sonraki(solda)

  /* ---------- Sayfa çizimi: metin olduğu gibi yazılır, hiçbir değişiklik yapılmaz ---------- */
  function renderPage(el, p) {
    if (p < 1 || p > N) { el.innerHTML = ''; el.dataset.page = ''; return; }   // sayfa yoksa boşalt
    var from = Q.p[p - 1], to = Q.p[p], h = '<div class="inner">', open = false;   // sayfadaki ayet aralığı
    for (var g = from; g < to; g++) {                  // sayfadaki her ayet için
      var s = A2S[g], sr = Q.s[s], no = g - sr[0] + 1; // sure, sure içi ayet no
      if (no === 1) {                                  // sure başlıyorsa
        if (open) { h += '</p>'; open = false; }       // açık paragrafı kapat
        h += '<div class="ban" data-s="' + s + '"><div class="tr">' + (s + 1) + '. ' + sr[2] +
             '<small>' + sr[1] + ' ayet · ' + sr[4] + '</small></div><div class="ar">' + sr[3] + '</div></div>';   // sure başlığı
        if (s !== 0 && s !== 8) h += '<p class="bsm">' + Q.b + '</p>';   // besmele (Fâtiha'da ayet, Tevbe'de yok)
      }
      if (!open) { h += '<p class="t">'; open = true; }  // paragraf aç
      h += Q.a[g] + ' <span class="e">۝' + arNum(no) + '</span> ';   // ayet metni + ayet sonu işareti
    }
    if (open) h += '</p>';                             // son paragrafı kapat
    if (p === N) h += '<div class="end">Sadakallahülazîm</div>';
    el.innerHTML = h + '</div>';                       // sayfayı yerleştir
    el.dataset.page = p;                               // hangi sayfa olduğunu işaretle
    el.scrollTop = 0;                                  // en üste al
  }

  /* Roller: #pgP önceki, #pgC şimdiki, #pgN sonraki. Paneller yer değiştirince kimlikler de güncellenir */
  function tagRoles() {
    ['p', 'c', 'n'].forEach(function (k) { panes[k].removeAttribute('id'); });       // önce hepsini boşalt (kimlikler benzersiz kalsın)
    panes.p.id = 'pgP'; panes.c.id = 'pgC'; panes.n.id = 'pgN';                       // sonra rollere göre yaz
    panes.c.setAttribute('aria-hidden', 'false'); panes.p.setAttribute('aria-hidden', 'true'); panes.n.setAttribute('aria-hidden', 'true');
  }

  /* ---------- Yerleşim: üç sayfa yan yana (sonraki solda, önceki sağda) ---------- */
  function place(dx, anim) {
    view.classList.toggle('anim', !!anim);             // animasyon sınıfı
    panes.p.style.transform = 'translate3d(' + (dx + W) + 'px,0,0)';   // önceki sayfa sağda
    panes.c.style.transform = 'translate3d(' + dx + 'px,0,0)';         // şimdiki sayfa
    panes.n.style.transform = 'translate3d(' + (dx - W) + 'px,0,0)';   // sonraki sayfa solda
  }
  function measure() { W = view.clientWidth; place(0, false); }        // genişliği ölç, sıfırla

  /* ---------- Başlık: sure, cüz, sayfa ---------- */
  function updateBar() {
    var from = Q.p[page - 1], to = Q.p[page], names = [], last = -1;   // sayfadaki sureler
    for (var g = from; g < to; g++) { var s = A2S[g]; if (s !== last) { names.push(Q.s[s][2]); last = s; } }
    $('tSure').textContent = names.slice(0, 2).join(' · ');           // sure adı(ları)
    $('tInfo').textContent = juzOf(page) + '. Cüz · Sayfa ' + page;                  // cüz ve sayfa
    document.title = names[0] + ' · Sayfa ' + page + ' · Kur\'an';
  }

  /* ---------- Kaldığın yeri kaydet ---------- */
  var saveT = 0;
  function saveScroll() {
    clearTimeout(saveT);
    saveT = setTimeout(function () {
      var c = panes.c, h = c.scrollHeight - c.clientHeight;           // kaydırma payı
      store.set('page', page); store.set('sc', h > 0 ? +(c.scrollTop / h).toFixed(4) : 0);   // sayfa + yatay oran
    }, 250);
  }
  ['p', 'c', 'n'].forEach(function (k) {
    panes[k].addEventListener('scroll', function () { if (this === panes.c) saveScroll(); }, { passive: true });
  });

  /* ---------- Sayfaya git (animasyonsuz). surahIdx verilirse sure başlığına kaydırır ---------- */
  function goTo(p, opt) {
    opt = opt || {};
    p = Math.min(N, Math.max(1, p | 0));
    if (animating) finishAnim();                       // bekleyen animasyonu bitir
    page = p;
    renderPage(panes.c, p); renderPage(panes.n, p + 1); renderPage(panes.p, p - 1);   // üç sayfayı hazırla
    place(0, false); updateBar(); store.set('page', page);
    var c = panes.c;
    if (opt.surah != null) {                           // sureye gidildiyse başlığa kaydır
      var b = c.querySelector('.ban[data-s="' + opt.surah + '"]');
      if (b) c.scrollTop = Math.max(0, b.offsetTop - 8);
    } else if (opt.ratio) {                            // kaldığın yere dön
      c.scrollTop = opt.ratio * (c.scrollHeight - c.clientHeight);
    }
    saveScroll();
  }

  /* ---------- Sayfa çevirme: dir=+1 sonraki (sağa sürükle), dir=-1 önceki ---------- */
  var pending = null;
  function finishAnim() {
    if (!pending) return;
    var d = pending; pending = null; animating = false; clearTimeout(d.t);
    var P = panes.p, C = panes.c, Nx = panes.n;
    if (d.dir > 0) { panes.p = C; panes.c = Nx; panes.n = P; page += 1; renderPage(panes.n, page + 1); }   // ileri: sonraki sayfa ortaya
    else           { panes.n = C; panes.c = P; panes.p = Nx; page -= 1; renderPage(panes.p, page - 1); }   // geri
    tagRoles(); place(0, false); updateBar(); store.set('page', page); store.set('sc', 0);
  }
  function turn(dir, instant) {
    if (animating) finishAnim();
    var target = page + dir;
    if (target < 1 || target > N) { place(0, true); return; }           // sınır: geri sek
    animating = true;
    pending = { dir: dir, t: 0 };
    var reduce = window.matchMedia && matchMedia('(prefers-reduced-motion: reduce)').matches;
    place(dir > 0 ? W : -W, !instant && !reduce);                       // hedefe kaydır
    pending.t = setTimeout(finishAnim, (instant || reduce) ? 0 : 240);  // bitince panelleri döndür
  }

  /* ---------- Dokunma: sürükleyerek sayfa çevirme + iki parmakla yazıyı büyütme ---------- */
  var ptrs = {}, mode = null, sx = 0, sy = 0, st = 0, lastX = 0, lastT = 0, vx = 0, pinch = null, tapOK = true;
  function count() { return Object.keys(ptrs).length; }
  view.addEventListener('pointerdown', function (e) {
    if (animating) finishAnim();
    ptrs[e.pointerId] = { x: e.clientX, y: e.clientY };
    if (count() === 1) { sx = lastX = e.clientX; sy = e.clientY; st = lastT = e.timeStamp; mode = null; vx = 0; }
    else if (count() === 2) {                          // iki parmak: yazı boyutu (pinch)
      if (mode === 'drag') place(0, true);             // sürüklemeyi iptal et
      mode = 'pinch';
      var k = Object.keys(ptrs), a = ptrs[k[0]], b = ptrs[k[1]];
      pinch = { d: Math.hypot(a.x - b.x, a.y - b.y) || 1, fs: fs };
    }
  });
  view.addEventListener('pointermove', function (e) {
    var q = ptrs[e.pointerId]; if (!q) return;
    q.x = e.clientX; q.y = e.clientY;
    if (mode === 'pinch' && count() === 2) {
      var k = Object.keys(ptrs), a = ptrs[k[0]], b = ptrs[k[1]];
      setFs(pinch.fs * Math.hypot(a.x - b.x, a.y - b.y) / pinch.d, true);   // parmak aralığına göre boyut
      return;
    }
    if (count() !== 1) return;
    var dx = e.clientX - sx, dy = e.clientY - sy;
    if (mode === null && (Math.abs(dx) > 8 || Math.abs(dy) > 8)) {
      mode = (Math.abs(dx) > Math.abs(dy) * 1.2) ? 'drag' : 'scroll';   // yön kararı: yatay = sayfa çevir, dikey = kaydır
      if (mode === 'drag') { try { view.setPointerCapture(e.pointerId); } catch (x) { } }
    }
    if (mode === 'drag') {
      var lim = dx;                                    // sınırlarda lastik etkisi
      if ((dx > 0 && page >= N) || (dx < 0 && page <= 1)) lim = dx / 4;
      place(lim, false);
      var dt = e.timeStamp - lastT; if (dt > 0) vx = (e.clientX - lastX) / dt; lastX = e.clientX; lastT = e.timeStamp;   // hız
    }
  });
  function up(e) {
    var had = ptrs[e.pointerId]; delete ptrs[e.pointerId];
    if (!had) return;
    if (mode === 'pinch') { if (count() < 2) { mode = (count() === 0) ? null : 'scroll'; pinch = null; store.set('fs', fs); } return; }
    // Sürükleme (move olayları geldiyse) ya da hızlı çekiş (yavaş/eski WebView'da move olayı hiç gelmeyebilir: karar bırakma anındaki konumdan verilir)
    if ((mode === 'drag' || mode === null) && count() === 0) {
      var cancel = e.type === 'pointercancel';
      var dx = (cancel ? lastX : e.clientX) - sx, dy = (cancel ? sy : e.clientY) - sy;   // toplam yer değiştirme
      var dt = Math.max(1, e.timeStamp - st), dir = 0;                                    // süre (ms)
      var horizontal = mode === 'drag' || (Math.abs(dx) > 24 && Math.abs(dx) > Math.abs(dy) * 1.5);   // yatay mı
      var speed = Math.max(Math.abs(vx), Math.abs(dx) / dt);                              // px/ms
      if (horizontal && (Math.abs(dx) > W * 0.22 || (speed > 0.45 && Math.abs(dx) > 24))) dir = (dx > 0) ? 1 : -1;   // eşik veya hız
      if (dir !== 0 && page + dir >= 1 && page + dir <= N) turn(dir); else if (mode === 'drag') place(0, true);   // çevir ya da geri dön
    }
    if (count() === 0) mode = null;
  }
  view.addEventListener('pointerup', up);
  view.addEventListener('pointercancel', up);
  view.addEventListener('lostpointercapture', function (e) { if (ptrs[e.pointerId] && mode !== 'drag') delete ptrs[e.pointerId]; });
  view.addEventListener('contextmenu', function (e) { if (mode === 'drag' || mode === 'pinch') e.preventDefault(); });

  /* ---------- Klavye (masaüstü): sol ok = sonraki sayfa (sağdan sola) ---------- */
  document.addEventListener('keydown', function (e) {
    if ($('sheet').classList.contains('open')) { if (e.key === 'Escape') closeSheet(); return; }
    if (e.target && /INPUT|TEXTAREA/.test(e.target.tagName)) return;
    if (e.key === 'ArrowLeft') { e.preventDefault(); turn(1); }
    else if (e.key === 'ArrowRight') { e.preventDefault(); turn(-1); }
    else if (e.key === '+' || e.key === '=') setFs(fs * 1.1);
    else if (e.key === '-') setFs(fs / 1.1);
  });

  /* ---------- Yazı boyutu: büyütünce satırlar yeniden dizilir, yana kaydırmak gerekmez ---------- */
  function setFs(v, live) {
    var c = panes.c, h = c.scrollHeight - c.clientHeight, r = h > 0 ? c.scrollTop / h : 0;   // kaydırma oranını koru
    fs = Math.round(Math.min(maxFs, Math.max(minFs, v)) * 2) / 2;
    root.style.setProperty('--fs', fs + 'px');
    var h2 = c.scrollHeight - c.clientHeight; if (h2 > 0) c.scrollTop = r * h2;
    $('bPlus').disabled = fs >= maxFs; $('bMinus').disabled = fs <= minFs;
    if (!live) store.set('fs', fs);
  }
  function toast(m) {
    var t = $('toast'); t.textContent = m; t.classList.add('show');
    clearTimeout(toast.t); toast.t = setTimeout(function () { t.classList.remove('show'); }, 1200);
  }
  $('bPlus').addEventListener('click', function () { var o = fs; setFs(fs * 1.12); if (fs === o) toast('En büyük boyut'); });
  $('bMinus').addEventListener('click', function () { var o = fs; setFs(fs / 1.12); if (fs === o) toast('En küçük boyut'); });

  // En geniş kelime ekrana sığsın: yazı üst sınırı, gerçek yazı tipiyle ölçülen en uzun kelimelerden hesaplanır
  function calcLimits() {
    W = view.clientWidth;
    var cw = Math.min(W, 760) - 40;                    // metin sütunu genişliği (kenar boşlukları düşülmüş)
    var ctx = document.createElement('canvas').getContext('2d'), mx = 4;
    ctx.font = '100px AmiriQuran'; ctx.direction = 'rtl';
    for (var i = 0; i < Q.w.length; i++) { var w = ctx.measureText(Q.w[i]).width / 100; if (w > mx) mx = w; }   // em cinsinden
    maxFs = Math.max(24, Math.min(120, Math.floor(cw / (mx * 1.06))));
    if (!fs) fs = Math.round(Math.min(maxFs, Math.max(22, Math.min(34, W / 13.5))));    // varsayılan: ekrana göre
    fs = Math.min(maxFs, Math.max(minFs, fs));
    setFs(fs, true);
  }

  /* ---------- Tema ---------- */
  var THEMES = ['light', 'sepia', 'dark'], TNAME = { light: 'Gündüz', sepia: 'Sepya', dark: 'Gece' }, TCOL = { light: '#fbf8f0', sepia: '#f0e4c8', dark: '#101310' };
  function applyTheme() {
    if (theme) root.setAttribute('data-theme', theme); else root.removeAttribute('data-theme');
    var dark = theme === 'dark' || (!theme && window.matchMedia && matchMedia('(prefers-color-scheme: dark)').matches);
    var bg = theme ? TCOL[theme] : (dark ? TCOL.dark : TCOL.light);                  // temanın zemin rengi
    var tc = document.querySelector('meta[name=theme-color]');                       // tarayıcı çubuğu rengi (etiket yoksa atla)
    if (tc) tc.setAttribute('content', bg);
    try { if (window.KuranApp && KuranApp.setBars) KuranApp.setBars(bg, theme === 'dark' || (!theme && dark)); } catch (e) { }   // Android uygulaması: durum çubuğu rengi
  }
  $('bTheme').addEventListener('click', function () {
    var i = THEMES.indexOf(theme); theme = THEMES[(i + 1) % THEMES.length];
    store.set('theme', theme); applyTheme(); toast(TNAME[theme]);
  });

  /* ---------- Git paneli: Sure | Cüz | Sayfa. Aynı anda yalnızca biri geçerli ---------- */
  var sheet = $('sheet'), list = $('list'), active = 'sure';
  var F = { sure: $('fSure'), cuz: $('fCuz'), sayfa: $('fSayfa') }, COL = { sure: $('cSure'), cuz: $('cCuz'), sayfa: $('cSayfa') };

  function norm(s) {                                   // arama için sadeleştir: Mâide -> maide, Mü'minûn -> muminun
    return String(s).normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase().replace(/ı/g, 'i').replace(/[^a-z0-9]/g, '').replace(/(.)\1+/g, '$1');
  }
  var STOP = /^(al|an|ar|as|at|ash|ad|az|adh|ath|ul|ud|us|i)$/;
  var KEYS = Q.s.map(function (sr) {                   // her sure için arama anahtarları
    var ks = [norm(sr[2])];
    (sr[2] + ' ' + sr[5]).split(/[\s\-']+/).forEach(function (t) { var n = norm(t); if (n.length > 1 && !STOP.test(n)) ks.push(n); });
    sr[5].split(/\s+/).forEach(function (t) { var n = norm(t); if (n.length > 2) ks.push(n); });
    return ks;
  });

  function items(kind, q) {                            // listeyi hazırla: [{v, n, name, sub, ar}]
    var out = [], i, qq = String(q || '').trim();
    if (kind === 'sure') {
      var nq = norm(qq), isNum = /^\d+$/.test(qq);
      var scored = [];
      for (i = 0; i < 114; i++) {
        var score = 99;
        if (!qq) score = 0;
        else if (isNum) { var sn = String(i + 1); score = sn === qq ? 0 : (sn.indexOf(qq) === 0 ? 1 : 99); }
        else if (nq) { for (var k = 0; k < KEYS[i].length; k++) { var key = KEYS[i][k]; if (key.indexOf(nq) === 0) { score = Math.min(score, 1); } else if (key.indexOf(nq) > 0) { score = Math.min(score, 2); } } }
        if (score < 99) scored.push([score, i]);
      }
      scored.sort(function (a, b) { return a[0] - b[0] || a[1] - b[1]; });
      scored.forEach(function (x) { var s = x[1], sr = Q.s[s]; out.push({ v: s, n: s + 1, name: sr[2], sub: sr[1] + ' ayet · ' + sr[4] + ' · Sayfa ' + surahPage[s], ar: sr[3].replace(/^سُورَةُ\s*/, '') }); });
    } else if (kind === 'cuz') {
      for (i = 1; i <= 30; i++) if (!qq || String(i).indexOf(qq) === 0) {
        if (qq && !/^\d+$/.test(qq)) break;
        var pg = Q.j[i - 1]; out.push({ v: i, n: i, name: i + '. Cüz', sub: 'Sayfa ' + pg + ' · ' + Q.s[A2S[Q.p[pg - 1]]][2] });
      }
    } else {
      if (qq && !/^\d+$/.test(qq)) return out;
      for (i = 1; i <= N; i++) if (!qq || String(i).indexOf(qq) === 0) out.push({ v: i, n: i, name: 'Sayfa ' + i, sub: juzOf(i) + '. Cüz · ' + Q.s[A2S[Q.p[i - 1]]][2] });
    }
    return out;
  }
  var cur = [];
  function renderList() {
    cur = items(active, F[active].value);
    if (!cur.length) { list.innerHTML = '<li class="empty">Sonuç yok</li>'; return; }
    var h = '';
    for (var i = 0; i < cur.length; i++) {
      var it = cur[i];
      h += '<li role="option" tabindex="0" data-i="' + i + '"><span class="n">' + it.n + '</span><span class="nm">' + it.name + '<small>' + it.sub + '</small></span>' + (it.ar ? '<span class="ar">' + it.ar + '</span>' : '') + '</li>';
    }
    list.innerHTML = h; list.scrollTop = 0;
  }
  function setActive(k, keepValue) {                   // seçili sütun: diğer ikisi temizlenir
    active = k;
    ['sure', 'cuz', 'sayfa'].forEach(function (x) { COL[x].classList.toggle('on', x === k); if (x !== k) F[x].value = ''; });
    if (!keepValue) F[k].value = F[k].value;
    renderList();
  }
  ['sure', 'cuz', 'sayfa'].forEach(function (k) {
    F[k].addEventListener('focus', function () { if (active !== k) setActive(k, true); });
    F[k].addEventListener('input', function () {
      if (k !== 'sure') F[k].value = F[k].value.replace(/\D/g, '').slice(0, 3);   // cüz/sayfa: sadece rakam
      if (active !== k) setActive(k, true); else renderList();
    });
    F[k].addEventListener('keydown', function (e) { if (e.key === 'Enter') { e.preventDefault(); submit(); } });
  });
  function navigate(kind, v) {
    var from = page, target = 1, opt = {};
    if (kind === 'sure') { target = surahPage[v]; opt.surah = v; }
    else if (kind === 'cuz') target = Q.j[v - 1];
    else target = Math.min(N, Math.max(1, v));
    closeSheet();
    if (target !== from) backTo = from;
    goTo(target, opt);
  }
  function submit() {                                  // "Git" düğmesi / Enter: yazılanı uygula
    var v = F[active].value.trim();
    if (active === 'sure') { if (cur.length && cur[0].v != null) navigate('sure', cur[0].v); return; }
    var n = parseInt(v, 10);
    if (!n) { toast(active === 'cuz' ? 'Cüz numarası yaz (1–30)' : 'Sayfa numarası yaz (1–604)'); return; }
    if (active === 'cuz' && (n < 1 || n > 30)) { toast('Cüz 1–30 arası'); return; }
    if (active === 'sayfa' && (n < 1 || n > N)) { toast('Sayfa 1–604 arası'); return; }
    navigate(active, n);
  }
  list.addEventListener('click', function (e) {        // listeden seçince hemen git
    var li = e.target.closest('li[data-i]'); if (!li) return;
    var it = cur[+li.dataset.i]; if (it) navigate(active, it.v);
  });
  list.addEventListener('keydown', function (e) { if (e.key === 'Enter' && e.target.dataset.i != null) e.target.click(); });
  $('bGo').addEventListener('click', submit);

  function renderChips() {                             // alt satır: geri dön + yer imleri
    var h = '', here = bookmarks.indexOf(page) >= 0;
    if (backTo && backTo !== page) h += '<button class="chip" data-a="back" type="button">↩ Sayfa ' + backTo + '\'e dön</button>';
    h += '<button class="chip" data-a="bm" type="button">' + (here ? '★ Yer imini kaldır' : '☆ Bu sayfaya yer imi koy') + ' (' + page + ')</button>';
    bookmarks.slice().sort(function (a, b) { return a - b; }).forEach(function (p) {
      h += '<button class="chip" data-a="go" data-p="' + p + '" type="button">' + p + ' · ' + Q.s[A2S[Q.p[p - 1]]][2] + '</button>';
    });
    $('chips').innerHTML = h;
  }
  $('chips').addEventListener('click', function (e) {
    var b = e.target.closest('.chip'); if (!b) return;
    var a = b.dataset.a;
    if (a === 'back') { var t = backTo; backTo = page; closeSheet(); goTo(t); }
    else if (a === 'go') { var p = +b.dataset.p; closeSheet(); backTo = page; goTo(p); }
    else if (a === 'bm') {
      var i = bookmarks.indexOf(page);
      if (i >= 0) bookmarks.splice(i, 1); else bookmarks.push(page);
      store.set('bm', bookmarks); renderChips(); toast(i >= 0 ? 'Yer imi kaldırıldı' : 'Yer imi eklendi');
    }
  });

  function openSheet() {
    if (animating) finishAnim();
    sheet.classList.add('open'); sheet.setAttribute('aria-hidden', 'false');
    F.sure.value = F.cuz.value = F.sayfa.value = ''; setActive('sure', true); renderChips();
    try { history.pushState({ sheet: 1 }, ''); } catch (e) { }       // Android geri tuşu paneli kapatsın
  }
  function closeSheet(fromPop) {
    if (!sheet.classList.contains('open')) return;
    sheet.classList.remove('open'); sheet.setAttribute('aria-hidden', 'true');
    document.activeElement && document.activeElement.blur && document.activeElement.blur();
    if (!fromPop) { try { if (history.state && history.state.sheet) history.back(); } catch (e) { } }
  }
  window.addEventListener('popstate', function () { closeSheet(true); });
  $('bGit').addEventListener('click', openSheet);
  $('ttl').addEventListener('click', openSheet);
  $('bClose').addEventListener('click', function () { closeSheet(); });
  sheet.addEventListener('click', function (e) { if (e.target === sheet) closeSheet(); });

  /* ---------- Başlat: son kalınan sayfadan, ayar sormadan ---------- */
  function start() {
    applyTheme();
    W = view.clientWidth;
    root.style.setProperty('--fs', (fs || 26) + 'px');
    goTo(page, { ratio: +store.get('sc', 0) || 0 });                // hemen çiz (yazı tipi yüklenince oranı yeniden uygula)
    var after = function () { calcLimits(); measure(); goTo(page, { ratio: +store.get('sc', 0) || 0 }); window.__kuranReady = true; };
    if (document.fonts && document.fonts.load) document.fonts.load('26px AmiriQuran', Q.b).then(after, after); else after();
  }
  var rT = 0;
  window.addEventListener('resize', function () {                  // döndürme / boyut değişimi
    clearTimeout(rT); rT = setTimeout(function () { calcLimits(); measure(); }, 120);
  });
  if ('serviceWorker' in navigator && /^https?:$/.test(location.protocol) && !window.KuranApp) {   // Android uygulamasında gerek yok (dosyalar zaten içinde)
    window.addEventListener('load', function () { navigator.serviceWorker.register('sw.js').catch(function () { }); });   // çevrimdışı çalışsın
  }
  window.__kuran = { goTo: goTo, turn: turn, setFs: setFs, get state() { return { page: page, fs: fs, maxFs: maxFs, minFs: minFs }; } };   // test için
  start();
})();
