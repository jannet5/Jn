// Notivo bildirim çubuğu arayüzü. Tüm kullanıcı metni textContent ile yazılır (innerHTML kullanılmaz).
import { buildSimulator, handleAppEvent, curlSample } from './sim.js';

const $ = (s, r = document) => r.querySelector(s);
const SVG_NS = 'http://www.w3.org/2000/svg';

/** Küçük DOM yardımcısı: h('div', {class:'x', onclick}, ...çocuklar) */
export function h(tag, props = {}, ...kids) {
  const el = document.createElement(tag);
  for (const [k, v] of Object.entries(props ?? {})) {
    if (v === undefined || v === null || v === false) continue;
    if (k === 'class') el.className = v;
    else if (k === 'dataset') Object.assign(el.dataset, v);
    else if (k.startsWith('on')) el.addEventListener(k.slice(2).toLowerCase(), v);
    else if (k === 'text') el.textContent = v;
    else if (k.includes('-') || k === 'role') el.setAttribute(k, v === true ? '' : v);
    else if (k in el) el[k] = v;
    else el.setAttribute(k, v === true ? '' : v);
  }
  for (const kid of kids.flat(Infinity)) {
    if (kid === undefined || kid === null || kid === false) continue;
    el.append(kid.nodeType ? kid : document.createTextNode(String(kid)));
  }
  return el;
}

function icon(d, cls = '') {
  const s = document.createElementNS(SVG_NS, 'svg');
  s.setAttribute('viewBox', '0 0 24 24');
  s.setAttribute('aria-hidden', 'true');
  if (cls) s.setAttribute('class', cls);
  const p = document.createElementNS(SVG_NS, 'path');
  p.setAttribute('d', d);
  s.append(p);
  return s;
}
/** Düğümleri doldurur; null/false/undefined çocukları atar (aksi halde DOM bunları "null"/"false" metni yapar). */
function fill(el, ...kids) {
  el.replaceChildren(...kids.flat(Infinity).filter((k) => k !== null && k !== undefined && k !== false));
}
const PATH = {
  chev: 'M7.4 8.6 12 13.2l4.6-4.6L18 10l-6 6-6-6z',
  more: 'M12 8a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm0 6a2 2 0 1 0 0-4 2 2 0 0 0 0 4z',
  pin: 'M16 3v2l-1 1v5l3 3v2h-5v6l-1 1-1-1v-6H6v-2l3-3V6L8 5V3z',
};

const state = {
  shade: null,
  open: new Set(), // genişletilmiş bildirim anahtarları
  groups: new Set(), // açık gruplar
  dragging: false,
  dirty: false,
  sound: localStorage.getItem('notivoSound') === '1',
};

// ---------------------------------------------------------------- API

const token = () => localStorage.getItem('notivoToken') ?? '';
export async function api(method, path, body) {
  const headers = { 'content-type': 'application/json' };
  if (token()) headers.authorization = `Bearer ${token()}`;
  const res = await fetch(path, { method, headers, body: method === 'GET' ? undefined : JSON.stringify(body ?? {}) });
  if (res.status === 401) {
    askToken();
    throw Object.assign(new Error('Erişim anahtarı gerekli'), { status: 401, code: 'unauthorized' });
  }
  const json = await res.json().catch(() => null);
  if (!res.ok) throw Object.assign(new Error(json?.error?.message ?? res.statusText), { status: res.status, code: json?.error?.code });
  return json;
}

function askToken() {
  const dlg = $('#dlgToken');
  if (dlg.open) return;
  dlg.showModal();
}
$('#tokenForm').addEventListener('submit', () => {
  localStorage.setItem('notivoToken', $('#tokenInput').value.trim());
  location.reload();
});

// ---------------------------------------------------------------- yardımcılar

let toastTimer;
export function toast(msg) {
  const t = $('#toast');
  t.textContent = msg;
  t.hidden = false;
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => (t.hidden = true), 2800);
}

function hue(s) {
  let x = 0;
  for (const c of s) x = (x * 31 + c.charCodeAt(0)) >>> 0;
  return x % 360;
}
function avatar(pkg, name, small = false) {
  const a = h('div', { class: 'avatar' + (small ? ' sm' : ''), 'aria-hidden': 'true' }, [...(name || pkg)][0]?.toUpperCase() ?? '?');
  a.style.setProperty('--h', hue(pkg));
  return a;
}

function rel(ms) {
  const d = Date.now() - ms;
  if (d < 60_000) return 'şimdi';
  if (d < 3_600_000) return `${Math.floor(d / 60_000)} dk`;
  if (d < 86_400_000) return `${Math.floor(d / 3_600_000)} sa`;
  return new Date(ms).toLocaleDateString('tr-TR', { day: 'numeric', month: 'short' });
}

const REASONS = {
  USER_SWIPE: 'Kaydırıldı', GROUP_SWIPE: 'Grup kaydırıldı', CLEAR_ALL: 'Temizlendi', CLICK: 'Açıldı', APP_CANCEL: 'Uygulama kaldırdı',
  TIMEOUT: 'Süresi doldu', CHANNEL_BLOCKED: 'Kanal kapatıldı', APP_BLOCKED: 'Uygulama engellendi', RULE_MUTE: 'Kural: sessiz',
  GROUP_EMPTY: 'Grup boşaldı', RESTORED: 'Yedekten', REPLACED: 'Güncellendi',
};

// ---------------------------------------------------------------- kaydırarak kapatma

/**
 * Yatay kaydırma. canDismiss=false (süregelen) ise yalnızca küçük bir direnç gösterir ve onBlocked çağrılır.
 * Dikey hareket kaydırmayı iptal eder, böylece liste normal kayar.
 */
function attachSwipe(el, { handle = el, canDismiss, onDismiss, onBlocked }) {
  let sx = 0, sy = 0, dx = 0, id = null, active = false, moved = false;
  handle.addEventListener('pointerdown', (e) => {
    if (e.pointerType === 'mouse' && e.button !== 0) return;
    if (e.target.closest('button, input, a, select, textarea')) return;
    active = true; moved = false; sx = e.clientX; sy = e.clientY; dx = 0; id = e.pointerId;
  });
  handle.addEventListener('pointermove', (e) => {
    if (!active || e.pointerId !== id) return;
    const mx = e.clientX - sx, my = e.clientY - sy;
    if (!moved) {
      if (Math.abs(mx) > 8 && Math.abs(mx) > Math.abs(my)) {
        moved = true; state.dragging = true; el.classList.add('drag');
        try { handle.setPointerCapture(id); } catch { /* yok say */ }
      } else {
        if (Math.abs(my) > 10) active = false;
        return;
      }
    }
    dx = mx;
    const w = el.offsetWidth || 300;
    const shown = canDismiss ? dx : Math.max(-28, Math.min(28, dx * 0.25));
    el.style.transform = `translateX(${shown}px)`;
    el.style.opacity = canDismiss ? String(Math.max(0.25, 1 - Math.abs(dx) / (w * 0.9))) : '1';
  });
  const end = (e) => {
    if (!active || e.pointerId !== id) return;
    active = false;
    if (!moved) return;
    // Bırakınca tarayıcı tutucu öğeye bir 'click' gönderir; grup başlığı yanlışlıkla açılıp kapanmasın.
    const swallow = (ev) => { ev.stopImmediatePropagation(); ev.preventDefault(); };
    handle.addEventListener('click', swallow, { capture: true, once: true });
    setTimeout(() => handle.removeEventListener('click', swallow, { capture: true }), 60);
    el.classList.remove('drag');
    const w = el.offsetWidth || 300;
    if (canDismiss && Math.abs(dx) > w * 0.4) {
      el.classList.add('leaving');
      el.style.transform = `translateX(${dx > 0 ? w : -w}px)`;
      setTimeout(() => { state.dragging = false; onDismiss(); }, 190);
    } else {
      if (!canDismiss && Math.abs(dx) > 8) onBlocked?.();
      el.classList.add('bounce');
      el.style.transform = ''; el.style.opacity = '';
      setTimeout(() => el.classList.remove('bounce'), 230);
      state.dragging = false;
      if (state.dirty) { state.dirty = false; scheduleRefresh(); }
    }
  };
  handle.addEventListener('pointerup', end);
  handle.addEventListener('pointercancel', end);
}

// ---------------------------------------------------------------- eylemler

async function guarded(fn) {
  try {
    return await fn();
  } catch (e) {
    if (e.status !== 401) toast(e.message);
    return null;
  } finally {
    scheduleRefresh();
  }
}

const openNotification = (n) => guarded(async () => {
  const r = await api('POST', '/api/shade/click', { key: n.key });
  toast(r.contentIntent ? `Açılıyor: ${r.contentIntent}` : `${n.appName} açılıyor`);
});
const dismissOne = (n) => guarded(() => api('POST', '/api/shade/dismiss', { key: n.key }));
const dismissGroup = (g, section) => guarded(() => api('POST', '/api/shade/dismiss-group', { pkg: g.pkg, group: g.group, section }));
const clearSection = (section) => guarded(() => api('POST', '/api/shade/clear-all', section ? { section } : {}));

function actionsRow(n, { onDone } = {}) {
  const wrap = h('div', { class: 'actions' });
  const draw = () => {
    wrap.replaceChildren(
      ...n.actions.map((a) =>
        h('button', {
          type: 'button', class: 'btn',
          onclick: () => {
            if (a.reply) return drawReply(a);
            guarded(() => api('POST', '/api/shade/action', { key: n.key, actionId: a.id })).then(() => onDone?.());
          },
        }, a.title)),
    );
  };
  const drawReply = (a) => {
    const input = h('input', { type: 'text', placeholder: 'Yanıt yaz…', 'aria-label': a.title, maxLength: 2000 });
    const send = () => {
      const text = input.value.trim();
      if (!text) return input.focus();
      guarded(() => api('POST', '/api/shade/action', { key: n.key, actionId: a.id, text })).then((r) => { if (r) toast('Yanıt gönderildi'); onDone?.(); });
    };
    input.addEventListener('keydown', (e) => { if (e.key === 'Enter') { e.preventDefault(); send(); } if (e.key === 'Escape') draw(); });
    wrap.replaceChildren(h('div', { class: 'reply-row' }, input, h('button', { type: 'button', class: 'btn primary', onclick: send }, 'Gönder')));
    input.focus();
  };
  draw();
  return wrap;
}

// ---------------------------------------------------------------- kartlar

function cardEl(n, { headsUp = false, onClose } = {}) {
  const expanded = state.open.has(n.key);
  const canExpand = !!(n.bigText && n.bigText !== n.text) || n.actions.length > 0;
  const body = h('div', { class: 'card-body' },
    h('div', { class: 'meta' },
      h('span', { class: 'app' }, n.appName),
      h('span', {}, '·'),
      h('time', {}, rel(n.when)),
      n.ongoing && icon(PATH.pin, 'pin'),
      n.ongoing && h('span', { class: 'sr' }, 'süregelen')),
    n.title && h('div', { class: 'title' }, n.title),
    h('div', { class: 'text' }, expanded && n.bigText ? n.bigText : n.text),
    n.deletedBySender && h('div', { class: 'deleted-flag' }, 'Gönderen bu mesajı silmiş olabilir'));
  body.addEventListener('click', () => { openNotification(n); onClose?.(); });

  const side = h('div', { class: 'side' },
    canExpand && !headsUp && h('button', {
      type: 'button', class: 'icon-btn chev', 'aria-label': expanded ? 'Daralt' : 'Genişlet', 'aria-expanded': String(expanded),
      onclick: () => { state.open.has(n.key) ? state.open.delete(n.key) : state.open.add(n.key); render(); },
    }, icon(PATH.chev)),
    !headsUp && h('button', { type: 'button', class: 'icon-btn', 'aria-label': `${n.appName} bildirim seçenekleri`, onclick: () => openMenu(n) }, icon(PATH.more)));
  if (headsUp) side.append(h('button', { type: 'button', class: 'icon-btn', 'aria-label': 'Kapat', onclick: () => onClose?.() }, '✕'));

  const el = h('article', {
    class: `card${n.ongoing ? ' ongoing' : ''}${expanded ? ' expanded' : ''}`,
    dataset: { key: n.key },
    tabindex: 0,
    'aria-label': `${n.appName}: ${n.title} ${n.text}`.trim(),
  }, h('div', { class: 'card-row' }, avatar(n.pkg, n.appName), body, side),
    (expanded || headsUp) && n.actions.length > 0 && actionsRow(n, { onDone: onClose }));

  el.addEventListener('keydown', (e) => {
    if (e.target !== el) return;
    if (e.key === 'Delete' || e.key === 'Backspace') { e.preventDefault(); n.ongoing ? toast('Süregelen bildirimler kapatılamaz') : dismissOne(n); }
    if (e.key === 'Enter') openNotification(n);
  });
  attachSwipe(el, {
    handle: el,
    canDismiss: !n.ongoing || headsUp,
    onDismiss: () => (headsUp ? onClose?.() : dismissOne(n)),
    onBlocked: () => toast('Süregelen bildirimler kaydırılarak kapatılamaz'),
  });
  return el;
}

function groupEl(g, section) {
  const id = `${section}|${g.pkg}|${g.group}`;
  const open = state.groups.has(id);
  const last = g.children[0];
  const head = h('div', {
    class: 'group-head', role: 'button', tabindex: 0, 'aria-expanded': String(open),
    'aria-label': `${g.appName}, ${g.count} bildirim`,
    onclick: () => { open ? state.groups.delete(id) : state.groups.add(id); render(); },
    onkeydown: (e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); e.currentTarget.click(); } },
  },
    avatar(g.pkg, g.appName),
    h('div', { class: 'card-body' },
      h('div', { class: 'group-title' }, g.appName),
      h('div', { class: 'group-sub' }, g.summary ? `${g.summary.title}: ${g.summary.text}`.replace(/^: /, '') : last.title || last.text),
      !open && h('div', { class: 'group-sub' }, g.summary ? last.title : last.text)),
    h('div', { class: 'side' }, h('span', { class: 'count-badge' }, g.count)));
  const wrap = h('article', { class: `group${open ? '' : ' stacked'}`, dataset: { group: id } }, head,
    open && h('div', { class: 'group-children' }, g.children.map((c) => cardEl(c))));
  attachSwipe(wrap, {
    handle: head,
    canDismiss: g.clearable,
    onDismiss: () => dismissGroup(g, section),
    onBlocked: () => toast('Bu grupta kapatılabilir bildirim yok'),
  });
  return wrap;
}

// ---------------------------------------------------------------- çizim

function render() {
  const s = state.shade;
  if (!s) return;
  const icons = $('#statusIcons');
  fill(icons,
    s.statusBar.slice(0, 6).map((i) => h('li', { title: `${i.appName} (${i.count})` }, avatar(i.pkg, i.appName, true))),
    s.statusBar.length > 6 && h('li', {}, `+${s.statusBar.length - 6}`));
  $('#dndBadge').hidden = !s.dnd.enabled;
  $('#tileDnd').setAttribute('aria-pressed', String(s.dnd.enabled));
  $('#tileSound').setAttribute('aria-pressed', String(state.sound));

  const root = $('#sections');
  const parts = [];
  for (const sec of s.sections) {
    if (!sec.entries.length) continue;
    parts.push(h('section', { class: 'section', 'aria-label': sec.title },
      h('div', { class: 'section-head' },
        h('h2', {}, sec.title),
        sec.clearable && h('button', { type: 'button', class: 'link-btn', 'aria-label': `${sec.title} bölümünü temizle`, onclick: () => clearSection(sec.id) }, 'Temizle')),
      h('div', { class: 'entries' }, sec.entries.map((e) => (e.type === 'group' ? groupEl(e, sec.id) : cardEl(e.n))))));
  }
  root.replaceChildren(...parts);
  $('#empty').hidden = s.total > 0;
  $('#clearAll').hidden = !s.sections.some((x) => x.clearable);
  const chip = $('#snoozedChip');
  chip.hidden = s.snoozed === 0;
  chip.textContent = `Ertelenen: ${s.snoozed}`;
}

let refreshTimer;
function scheduleRefresh() {
  if (state.dragging) { state.dirty = true; return; }
  clearTimeout(refreshTimer);
  refreshTimer = setTimeout(refresh, 40);
}
async function refresh() {
  try {
    state.shade = await api('GET', '/api/shade');
    render();
    if ($('#dlgHistory').open) loadHistory();
  } catch (e) {
    if (e.status !== 401) setConn('down');
  }
}

// ---------------------------------------------------------------- heads-up

let hideTimer;
function hideHeadsUp() {
  clearTimeout(hideTimer);
  $('#headsup').hidden = true;
  $('#headsup').replaceChildren();
}
function showHeadsUp(n) {
  const box = $('#headsup');
  box.replaceChildren(cardEl(n, { headsUp: true, onClose: hideHeadsUp }));
  box.hidden = false;
  const arm = () => { clearTimeout(hideTimer); hideTimer = setTimeout(hideHeadsUp, 5000); };
  box.onmouseenter = () => clearTimeout(hideTimer);
  box.onmouseleave = arm;
  box.onfocusin = () => clearTimeout(hideTimer);
  arm();
}

let actx;
function beep() {
  if (!state.sound) return;
  try {
    actx ??= new (window.AudioContext || window.webkitAudioContext)();
    const o = actx.createOscillator(), g = actx.createGain();
    o.frequency.value = 880; g.gain.value = 0.05;
    o.connect(g).connect(actx.destination);
    o.start(); o.stop(actx.currentTime + 0.12);
  } catch { /* ses yok */ }
}

// ---------------------------------------------------------------- menü (bildirim seçenekleri)

const menuItem = (label, fn, hint) => h('button', { type: 'button', class: 'menu-item', onclick: async () => { $('#dlgMenu').close(); await fn(); } }, h('span', {}, label), hint && h('small', {}, hint));

function openMenu(n) {
  $('#menuTitle').textContent = `${n.appName} · ${n.channelName}`;
  const snoozeOpts = [[15, '15 dakika'], [30, '30 dakika'], [60, '1 saat'], [120, '2 saat']];
  fill($('#menuBody'),
    !n.ongoing && h('div', { class: 'menu-sep' }, 'Ertele'),
    ...(!n.ongoing ? snoozeOpts.map(([m, l]) => menuItem(l, () => guarded(async () => { await api('POST', '/api/shade/snooze', { key: n.key, minutes: m }); toast(`${l} sonra tekrar hatırlatılacak`); }))) : []),
    h('div', { class: 'menu-sep' }, 'Bildirim ayarları'),
    n.importance > 2 && menuItem(`"${n.channelName}" kanalını sessize al`, () => guarded(() => api('PATCH', `/api/apps/${encodeURIComponent(n.pkg)}/channels/${encodeURIComponent(n.channel)}`, { importance: 2 })), 'ses ve heads-up kapanır'),
    menuItem(`"${n.channelName}" kanalını kapat`, () => guarded(() => api('PATCH', `/api/apps/${encodeURIComponent(n.pkg)}/channels/${encodeURIComponent(n.channel)}`, { importance: 0 })), 'bildirim gelmez'),
    menuItem(`${n.appName} bildirimlerini kapat`, () => guarded(() => api('PATCH', `/api/apps/${encodeURIComponent(n.pkg)}`, { blocked: true }))),
    menuItem('Bu uygulama için kural ekle…', () => openRules(n.pkg)));
  $('#dlgMenu').showModal();
}

// ---------------------------------------------------------------- geçmiş

let histTimer;
async function loadHistory() {
  const q = $('#histQ').value, st = $('#histState').value;
  try {
    const { items } = await api('GET', `/api/history?q=${encodeURIComponent(q)}&state=${st}&limit=200`);
    const list = $('#histList');
    if (!items.length) {
      list.replaceChildren(h('li', { class: 'hint' }, q ? 'Eşleşen kayıt yok.' : 'Henüz kayıt yok.'));
      return;
    }
    list.replaceChildren(...items.map((it) => {
      const live = it.state === 'active';
      const label = live ? 'Çubukta' : it.state === 'snoozed' ? 'Ertelendi' : REASONS[it.removedReason] ?? it.removedReason ?? '';
      return h('li', { class: 'hist-item' },
        avatar(it.pkg, it.appName),
        h('div', { class: 'b' },
          h('div', { class: 't' }, `${it.appName}${it.title ? ' · ' + it.title : ''}`),
          h('div', { class: 'x' }, it.text),
          it.deletedBySender && h('div', { class: 'deleted-flag' }, 'Gönderen silmiş olabilir'),
          h('div', { class: 'x' }, rel(it.when))),
        h('span', { class: 'state-chip' + (live ? ' live' : '') }, label));
    }));
  } catch (e) {
    if (e.status !== 401) toast(e.message);
  }
}

function openHistory(stateFilter = 'all') {
  $('#histState').value = stateFilter;
  const t = token() ? `?token=${encodeURIComponent(token())}` : '';
  $('#lnkCsv').href = `/api/export.csv${t}`;
  $('#lnkBackup').href = `/api/backup${t}`;
  $('#dlgHistory').showModal();
  loadHistory();
}

// ---------------------------------------------------------------- kurallar

async function loadRules() {
  const [{ rules }, { apps }] = await Promise.all([api('GET', '/api/rules'), api('GET', '/api/apps')]);
  const name = (pkg) => apps.find((a) => a.pkg === pkg)?.name ?? pkg;
  const sel = $('#ruleApp');
  const keep = sel.value;
  sel.replaceChildren(h('option', { value: '' }, 'Tüm uygulamalar'), ...apps.map((a) => h('option', { value: a.pkg }, a.name)));
  sel.value = keep;
  $('#ruleList').replaceChildren(...(rules.length ? rules.map((r) => h('li', { class: 'rule-item' },
    h('div', { class: 'b' },
      h('div', { class: 't' }, r.keyword ? `“${r.keyword}”` : 'Tüm bildirimler'),
      h('div', { class: 'x' }, `${r.action === 'BLOCK' ? 'Engelle' : 'Sessize al'} · ${r.pkg ? name(r.pkg) : 'tüm uygulamalar'}`)),
    h('input', { type: 'checkbox', checked: r.enabled, 'aria-label': `Kuralı aç/kapat: ${r.keyword || r.pkg}`, onchange: (e) => guarded(async () => { await api('PATCH', `/api/rules/${r.id}`, { enabled: e.target.checked }); loadRules(); }) }),
    h('button', { type: 'button', class: 'btn danger', onclick: () => guarded(async () => { await api('DELETE', `/api/rules/${r.id}`); loadRules(); }) }, 'Sil'))) : [h('li', { class: 'hint' }, 'Henüz kural yok.')]));
}
async function openRules(pkg = '') {
  $('#ruleErr').hidden = true;
  await loadRules();
  if (pkg) $('#ruleApp').value = pkg;
  $('#dlgRules').showModal();
}
$('#ruleForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const f = e.target;
  try {
    await api('POST', '/api/rules', { pkg: f.pkg.value, keyword: f.keyword.value, action: f.action.value });
    f.keyword.value = '';
    $('#ruleErr').hidden = true;
    loadRules();
  } catch (err) {
    $('#ruleErr').textContent = err.message;
    $('#ruleErr').hidden = false;
  }
});

// ---------------------------------------------------------------- canlı olaylar

function setConn(kind) {
  const c = $('#conn');
  c.className = `conn ${kind}`;
  c.textContent = kind === 'live' ? 'canlı' : kind === 'down' ? 'bağlantı yok' : 'bağlanıyor…';
}

function onEvent(ev) {
  if (ev.type === 'alert') {
    if (ev.headsUp && ev.notification) showHeadsUp(ev.notification);
    if (ev.sound) beep();
  }
  if (ev.type === 'deleted_notice') toast(`${ev.title || 'Bir sohbet'}: gönderen bir mesajı sildi (işaretlendi)`);
  if (['action', 'click', 'dismissed'].includes(ev.type)) handleAppEvent(ev, { api, toast });
  scheduleRefresh();
}

function connect() {
  const es = new EventSource(`/api/events${token() ? `?token=${encodeURIComponent(token())}` : ''}`);
  es.onopen = () => { setConn('live'); scheduleRefresh(); };
  es.onerror = () => setConn('down');
  es.onmessage = (m) => {
    try { onEvent(JSON.parse(m.data)); } catch { /* bozuk olay */ }
  };
}

// ---------------------------------------------------------------- başlangıç

$('#tileDnd').addEventListener('click', () => guarded(async () => {
  await api('PUT', '/api/dnd', { enabled: !state.shade?.dnd.enabled });
}));
$('#tileSound').addEventListener('click', () => {
  state.sound = !state.sound;
  localStorage.setItem('notivoSound', state.sound ? '1' : '0');
  if (state.sound) beep();
  render();
});
$('#tileHistory').addEventListener('click', () => openHistory());
$('#snoozedChip').addEventListener('click', () => openHistory('snoozed'));
$('#tileRules').addEventListener('click', () => openRules());
$('#tileSim').addEventListener('click', () => $('#dlgSim').showModal());
$('#clearAll').addEventListener('click', () => clearSection());
$('#histQ').addEventListener('input', () => { clearTimeout(histTimer); histTimer = setTimeout(loadHistory, 150); });
$('#histState').addEventListener('change', loadHistory);
$('#fileRestore').addEventListener('change', async (e) => {
  const file = e.target.files[0];
  e.target.value = '';
  if (!file) return;
  try {
    const data = JSON.parse(await file.text());
    const r = await api('POST', '/api/restore', data);
    toast(`${r.added} kayıt geri yüklendi`);
    loadHistory();
  } catch (err) {
    toast(err instanceof SyntaxError ? 'Geçerli bir Notivo yedeği değil' : err.message);
  }
});
for (const dlg of document.querySelectorAll('dialog')) {
  dlg.addEventListener('click', (e) => {
    if (e.target.closest('[data-close]')) return dlg.close();
    const r = dlg.getBoundingClientRect(); // yalnızca panelin dışına (arka plana) tıklanınca kapat
    if (e.target === dlg && (e.clientX < r.left || e.clientX > r.right || e.clientY < r.top || e.clientY > r.bottom)) dlg.close();
  });
}

function tickClock() {
  $('#clock').textContent = new Date().toLocaleTimeString('tr-TR', { hour: '2-digit', minute: '2-digit' });
}
tickClock();
setInterval(tickClock, 10_000);
setInterval(() => { if (!state.dragging) render(); }, 30_000);

buildSimulator($('#simGrid'), { api, toast, guarded });
$('#curlSample').textContent = curlSample(location.origin, !!token());
refresh();
connect();
