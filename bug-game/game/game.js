// Böcek Ringi - oyun mantığı (tarayıcı). art (CONCEPTS, BUGS, character, motif, rnd) build sırasında önce eklenir.
'use strict';

// ---------- Böcekler: görünüm + kim kimi yener ----------
// Her böcek kendinden sonraki 9 böceği yener, önceki 9'a yenilir, tam karşısındakiyle berabere.
const KIND = ['rhino', 'lady', 'mantis', 'firefly', 'scorpion', 'ant', 'wasp', 'dragonfly', 'roach', 'hopper',
  'centipede', 'dung', 'stag', 'strider', 'tarantula', 'cicada', 'butterfly', 'mosquito', 'pill', 'giant'];
function outcome(a, b) {
  if (a === b) return 0;
  const d = (b - a + 20) % 20;
  if (d === 10) return 0;
  return d < 10 ? 1 : -1;
}
const beats = a => Array.from({ length: 9 }, (_, k) => (a + 1 + k) % 20);

function bugSVG(i, size = 80, extra = '') {
  const [name, col] = BUGS[i]; const k = KIND[i];
  const L = (x1, y1, x2, y2, w = 3) => `<path d="M${x1} ${y1} L${x2} ${y2}" stroke="#111" stroke-width="${w}" stroke-linecap="round"/>`;
  let s = '';
  const legs = (n = 3, len = 24, y0 = -6, gap = 10) => { let g = ''; for (let j = 0; j < n; j++) { const y = y0 + j * gap; g += L(-8, y, -8 - len, y - 8 + j * 6) + L(8, y, 8 + len, y - 8 + j * 6); } return g; };
  const head = (y = -24, r = 10) => `<circle cx="0" cy="${y}" r="${r}" fill="#222" stroke="#111" stroke-width="3"/><circle cx="-4" cy="${y - 2}" r="3.2" fill="#fff"/><circle cx="4" cy="${y - 2}" r="3.2" fill="#fff"/><circle cx="-4" cy="${y - 2}" r="1.4" fill="#111"/><circle cx="4" cy="${y - 2}" r="1.4" fill="#111"/>`;
  const ant = (y = -32) => `<path d="M-4 ${y + 2} Q-12 ${y - 16} -18 ${y - 14} M4 ${y + 2} Q12 ${y - 16} 18 ${y - 14}" stroke="#111" stroke-width="2.5" fill="none"/>`;
  const shell = (rx = 18, ry = 24) => `<ellipse cx="0" cy="6" rx="${rx}" ry="${ry}" fill="${col}" stroke="#111" stroke-width="3"/><line x1="0" y1="${6 - ry + 4}" x2="0" y2="${6 + ry}" stroke="#111" stroke-width="2" opacity=".5"/><ellipse cx="-6" cy="-4" rx="5" ry="9" fill="#fff" opacity=".25"/>`;
  const wings = (c = '#dff6ff', big = 1) => `<ellipse cx="-20" cy="-2" rx="${18 * big}" ry="${9 * big}" fill="${c}" opacity=".8" stroke="#111" stroke-width="2" transform="rotate(-25 -20 -2)"/><ellipse cx="20" cy="-2" rx="${18 * big}" ry="${9 * big}" fill="${c}" opacity=".8" stroke="#111" stroke-width="2" transform="rotate(25 20 -2)"/>`;
  switch (k) {
    case 'rhino': case 'giant': s = legs() + shell(k === 'giant' ? 22 : 18, k === 'giant' ? 28 : 24) + head(-24) + `<path d="M0 -30 Q4 -52 14 -${k === 'giant' ? 62 : 52}" stroke="#111" stroke-width="7" fill="none" stroke-linecap="round"/>`; break;
    case 'lady': s = legs(3, 18) + `<circle cx="0" cy="6" r="22" fill="${col}" stroke="#111" stroke-width="3"/><line x1="0" y1="-14" x2="0" y2="28" stroke="#111" stroke-width="2.5"/>${[[-10, -2], [10, -2], [-11, 14], [11, 14], [0, 22]].map(([x, y]) => `<circle cx="${x}" cy="${y}" r="4.5" fill="#111"/>`).join('')}` + head(-20, 9) + ant(-28); break;
    case 'mantis': s = L(-6, 18, -22, 40) + L(6, 18, 22, 40) + L(-6, 26, -20, 48) + L(6, 26, 20, 48) + `<ellipse cx="0" cy="20" rx="9" ry="22" fill="${col}" stroke="#111" stroke-width="3"/><rect x="-5" y="-22" width="10" height="26" fill="${col}" stroke="#111" stroke-width="3"/><path d="M-5 -10 L-22 -24 L-14 -2 M5 -10 L22 -24 L14 -2" stroke="${col}" stroke-width="6" fill="none"/><path d="M-12 -36 L12 -36 L0 -22 Z" fill="${col}" stroke="#111" stroke-width="3"/><circle cx="-10" cy="-36" r="4" fill="#111"/><circle cx="10" cy="-36" r="4" fill="#111"/>`; break;
    case 'firefly': s = `<circle cx="0" cy="22" r="22" fill="#fff59d" opacity=".5"/>` + legs(3, 16) + shell(14, 22) + `<ellipse cx="0" cy="20" rx="12" ry="10" fill="#fff176" stroke="#111" stroke-width="2"/>` + head(-20, 8) + ant(-26); break;
    case 'scorpion': s = legs(4, 20, -4, 7) + `<ellipse cx="0" cy="4" rx="13" ry="18" fill="${col}" stroke="#111" stroke-width="3"/><path d="M0 20 Q4 42 -6 50 Q-26 52 -18 34" stroke="${col}" stroke-width="7" fill="none" stroke-linecap="round"/><path d="M-18 34 l-6 -8 l8 2 z" fill="#111"/><path d="M-10 -10 L-22 -26 M10 -10 L22 -26" stroke="${col}" stroke-width="6"/><path d="M-28 -34 q6 -4 10 6 q-8 4 -10 -6 M28 -34 q-6 -4 -10 6 q8 4 10 -6" fill="${col}" stroke="#111" stroke-width="2"/>` + head(-14, 7); break;
    case 'ant': s = legs(3, 22, -2, 8) + `<ellipse cx="0" cy="22" rx="11" ry="15" fill="${col}" stroke="#111" stroke-width="3"/><ellipse cx="0" cy="2" rx="7" ry="8" fill="${col}" stroke="#111" stroke-width="3"/>` + head(-16, 9) + ant(-24) + `<path d="M-5 -8 L-9 -2 M5 -8 L9 -2" stroke="#111" stroke-width="3"/>`; break;
    case 'wasp': s = wings() + legs(3, 16) + `<ellipse cx="0" cy="18" rx="12" ry="18" fill="${col}" stroke="#111" stroke-width="3"/>${[10, 18, 26].map(y => `<rect x="-11" y="${y}" width="22" height="4" fill="#111"/>`).join('')}<path d="M-3 36 L0 44 L3 36" fill="#111"/><ellipse cx="0" cy="-4" rx="8" ry="9" fill="#111"/>` + head(-20, 9) + ant(-28); break;
    case 'dragonfly': s = `<ellipse cx="-22" cy="-6" rx="22" ry="6" fill="#bdf" opacity=".7" stroke="#111" stroke-width="1.5"/><ellipse cx="22" cy="-6" rx="22" ry="6" fill="#bdf" opacity=".7" stroke="#111" stroke-width="1.5"/><ellipse cx="-20" cy="6" rx="20" ry="5" fill="#bdf" opacity=".7" stroke="#111" stroke-width="1.5"/><ellipse cx="20" cy="6" rx="20" ry="5" fill="#bdf" opacity=".7" stroke="#111" stroke-width="1.5"/><rect x="-4" y="-6" width="8" height="52" rx="4" fill="${col}" stroke="#111" stroke-width="2.5"/>` + `<circle cx="-7" cy="-16" r="7" fill="${col}" stroke="#111" stroke-width="2.5"/><circle cx="7" cy="-16" r="7" fill="${col}" stroke="#111" stroke-width="2.5"/>`; break;
    case 'roach': s = legs(3, 26) + `<ellipse cx="0" cy="8" rx="16" ry="27" fill="${col}" stroke="#111" stroke-width="3"/><line x1="0" y1="-12" x2="0" y2="34" stroke="#111" stroke-width="2"/>` + head(-22, 8) + `<path d="M-3 -28 Q-20 -60 -36 -54 M3 -28 Q20 -60 36 -54" stroke="#111" stroke-width="2" fill="none"/>`; break;
    case 'hopper': s = L(-8, 0, -18, -6) + L(8, 0, 18, -6) + `<path d="M-8 16 L-28 -4 L-30 30 M8 16 L28 -4 L30 30" stroke="${col}" stroke-width="6" fill="none" stroke-linejoin="round"/><ellipse cx="0" cy="12" rx="10" ry="26" fill="${col}" stroke="#111" stroke-width="3"/>` + head(-20, 9) + ant(-28); break;
    case 'centipede': for (let j = 0; j < 7; j++) { const y = -18 + j * 9; s += L(-6, y, -22, y + 3, 2.5) + L(6, y, 22, y + 3, 2.5); } for (let j = 6; j >= 0; j--) s += `<circle cx="${Math.sin(j) * 4}" cy="${-18 + j * 9}" r="8" fill="${col}" stroke="#111" stroke-width="2.5"/>`; s += head(-26, 8) + ant(-32); break;
    case 'dung': s = `<circle cx="0" cy="30" r="16" fill="#8d6e4a" stroke="#111" stroke-width="3"/>` + legs(3, 18, -10, 8) + shell(15, 18).replace('cy="6"', 'cy="0"') + head(-20, 8); break;
    case 'stag': s = legs() + shell(17, 23) + head(-22, 9) + `<path d="M-6 -28 Q-20 -46 -6 -56 M-14 -44 l-8 -2 M6 -28 Q20 -46 6 -56 M14 -44 l8 -2" stroke="#3a2412" stroke-width="5" fill="none" stroke-linecap="round"/>`; break;
    case 'strider': s = L(-6, 0, -40, -24, 2) + L(6, 0, 40, -24, 2) + L(-6, 10, -42, 34, 2) + L(6, 10, 42, 34, 2) + L(-6, -6, -18, -20, 2) + L(6, -6, 18, -20, 2) + `<ellipse cx="0" cy="8" rx="6" ry="20" fill="${col}" stroke="#111" stroke-width="2.5"/>` + head(-16, 6); break;
    case 'tarantula': for (let j = 0; j < 4; j++) { const y = -8 + j * 7; s += `<path d="M-8 ${y} Q-26 ${y - 18} -36 ${y + 8} M8 ${y} Q26 ${y - 18} 36 ${y + 8}" stroke="${col}" stroke-width="6" fill="none" stroke-linecap="round"/>`; } s += `<circle cx="0" cy="18" r="17" fill="${col}" stroke="#111" stroke-width="3"/><circle cx="0" cy="-6" r="12" fill="${col}" stroke="#111" stroke-width="3"/>${[-5, -1, 3, 7].map((x, j) => `<circle cx="${x - 1}" cy="${-10 + (j % 2) * 3}" r="2.2" fill="#e33"/>`).join('')}`; break;
    case 'cicada': s = wings('#e6f9ec', 1.15) + legs(3, 14) + shell(14, 20) + head(-18, 10).replace(/r="3.2"/g, 'r="4"'); break;
    case 'butterfly': s = `<path d="M0 0 C-50 -50 -50 20 0 6 Z M0 0 C50 -50 50 20 0 6 Z" fill="${col}" stroke="#111" stroke-width="2.5"/><path d="M0 8 C-36 20 -30 46 0 18 Z M0 8 C36 20 30 46 0 18 Z" fill="#ffd166" stroke="#111" stroke-width="2.5"/><circle cx="-22" cy="-12" r="5" fill="#fff"/><circle cx="22" cy="-12" r="5" fill="#fff"/><rect x="-3" y="-14" width="6" height="40" rx="3" fill="#222"/>` + ant(-16); break;
    case 'mosquito': s = L(-4, 4, -30, 30, 2) + L(4, 4, 30, 30, 2) + L(-4, 0, -32, 6, 2) + L(4, 0, 32, 6, 2) + `<ellipse cx="-14" cy="-6" rx="14" ry="5" fill="#eee" opacity=".8" transform="rotate(-20 -14 -6)"/><ellipse cx="14" cy="-6" rx="14" ry="5" fill="#eee" opacity=".8" transform="rotate(20 14 -6)"/><ellipse cx="0" cy="16" rx="5" ry="18" fill="${col}" stroke="#111" stroke-width="2"/>` + head(-10, 6) + `<path d="M0 -16 L0 -40" stroke="#111" stroke-width="2"/>`; break;
    case 'pill': s = legs(3, 10) + `<ellipse cx="0" cy="6" rx="20" ry="26" fill="${col}" stroke="#111" stroke-width="3"/>${[-8, 0, 8, 16, 24].map(y => `<path d="M-19 ${y} Q0 ${y + 6} 19 ${y}" stroke="#555" stroke-width="2" fill="none"/>`).join('')}` + head(-22, 9) + ant(-30); break;
  }
  return `<svg viewBox="-50 -64 100 116" width="${size}" height="${size * 1.16}" role="img" aria-label="${name}" ${extra}>${s}</svg>`;
}

// ---------- Ses (WebAudio ile sentezlenen tezahürat) ----------
let AC = null, muted = false;
function audio() { if (!AC) { try { AC = new (window.AudioContext || window.webkitAudioContext)(); } catch (e) { AC = null; } } return AC; }
function tone(f, d = .12, type = 'square', v = .06, when = 0) {
  const a = audio(); if (!a || muted) return; const t = a.currentTime + when;
  const o = a.createOscillator(), g = a.createGain(); o.type = type; o.frequency.setValueAtTime(f, t);
  g.gain.setValueAtTime(v, t); g.gain.exponentialRampToValueAtTime(.0001, t + d); o.connect(g).connect(a.destination); o.start(t); o.stop(t + d);
}
function hey(times = 2) {
  const a = audio(); if (!a || muted) return;
  for (let n = 0; n < times; n++) {
    const t = a.currentTime + n * .32;
    for (let v = 0; v < 6; v++) { // kalabalık: birkaç ses + gürültü
      const o = a.createOscillator(), g = a.createGain(), f = a.createBiquadFilter();
      o.type = 'sawtooth'; const base = 180 + Math.random() * 260;
      o.frequency.setValueAtTime(base, t); o.frequency.exponentialRampToValueAtTime(base * 1.35, t + .12);
      f.type = 'bandpass'; f.frequency.value = 900; f.Q.value = 1.4;
      g.gain.setValueAtTime(0, t); g.gain.linearRampToValueAtTime(.035, t + .03); g.gain.exponentialRampToValueAtTime(.0001, t + .22);
      o.connect(f).connect(g).connect(a.destination); o.start(t + Math.random() * .04); o.stop(t + .25);
    }
    const len = a.sampleRate * .2, buf = a.createBuffer(1, len, a.sampleRate), d = buf.getChannelData(0);
    for (let j = 0; j < len; j++) d[j] = (Math.random() * 2 - 1) * (1 - j / len);
    const src = a.createBufferSource(), g2 = a.createGain(); src.buffer = buf; g2.gain.value = .05; src.connect(g2).connect(a.destination); src.start(t);
  }
}
const sfx = {
  tap: () => tone(660, .06, 'triangle', .05),
  place: () => tone(420, .08, 'square', .04),
  leaf: () => { tone(300, .25, 'sine', .05); tone(200, .3, 'sine', .04, .1); },
  win: () => { tone(523, .1); tone(659, .1, 'square', .06, .1); tone(784, .18, 'square', .06, .2); hey(2); },
  draw: () => tone(440, .2, 'triangle', .05),
  lose: () => { tone(300, .15, 'sawtooth', .05); tone(220, .25, 'sawtooth', .05, .12); },
  tick: () => tone(880, .04, 'square', .03),
};

// ---------- Durum ----------
const $ = s => document.querySelector(s);
const save = (k, v) => { try { localStorage.setItem('br_' + k, JSON.stringify(v)); } catch (e) {} };
const load = (k, d) => { try { const v = localStorage.getItem('br_' + k); return v == null ? d : JSON.parse(v); } catch (e) { return d; } };
const S = { world: load('world', 0), names: load('names', ['Oyuncu 1', 'Oyuncu 2']), rounds: 3, round: 1, turn: 0, scores: [0, 0], hidden: [], picks: [], showTime: 5 };
const W0 = () => CONCEPTS[S.world];
const sleep = ms => new Promise(r => setTimeout(r, ms));

// ---------- Sahne (üst bölüm: mekan, maskot, rakip, sisli seyirci) ----------
function sceneSVG(opts = {}) {
  const c = W0(), r = rnd(4242 + S.world * 13);
  let s = `<svg viewBox="0 0 1280 800" preserveAspectRatio="xMidYMid slice" class="scene-svg"><defs><linearGradient id="gs" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="${c.sky[0]}"/><stop offset="1" stop-color="${c.sky[1]}"/></linearGradient><filter id="fb"><feGaussianBlur stdDeviation="16"/></filter></defs>`;
  s += `<rect width="1280" height="800" fill="url(#gs)"/>` + motif(c.motif, r, c) + `<rect y="540" width="1280" height="260" fill="${c.ground}"/>`;
  // seyirci böcekler sisin içinde
  let smoke = ''; for (let k = 0; k < 16; k++) smoke += `<circle cx="${360 + r() * 560}" cy="${420 + r() * 150}" r="${55 + r() * 55}" fill="#eee" opacity="${.22 + r() * .25}"/>`;
  s += `<g class="smoke" filter="url(#fb)">${smoke}</g><g class="crowd">`;
  for (let k = 0; k < 10; k++) s += `<g class="fan" style="animation-delay:${(k % 5) * .12}s"><g transform="translate(${400 + (k % 5) * 110 + r() * 30},${450 + Math.floor(k / 5) * 70})">${bugSVG((S.world + k * 3) % 20, 70).replace('<svg ', '<svg x="-35" y="-40" ')}</g></g>`;
  s += `</g><g class="smoke2" filter="url(#fb)" opacity=".75">${smoke.slice(0, smoke.length / 2)}</g>`;
  s += `<g class="shouts">${[['HEY!', 470, 400], ['HEY HEY!', 640, 380], ['VUR!', 820, 410]].map(([t, x, y]) => `<text x="${x}" y="${y}" font-size="38" font-weight="900" fill="#fff" stroke="#111" stroke-width="6" paint-order="stroke" text-anchor="middle">${t}</text>`).join('')}</g>`;
  s += `<g class="mascot ${opts.talk === 'm' ? 'talk' : ''}">${character(c.mascot, 190, 560, 1.35, false)}</g>`;
  s += `<g class="rival ${opts.talk === 'r' ? 'talk' : ''}">${character(c.rival, 1090, 580, 1.25, true)}</g>`;
  return s + '</svg>';
}

// ---------- Ekranlar ----------
function show(id) { document.querySelectorAll('.screen').forEach(e => e.hidden = e.id !== id); window.scrollTo(0, 0); }

function renderTitle() {
  $('#worlds').innerHTML = CONCEPTS.map((c, i) => `<button class="world ${i === S.world ? 'on' : ''}" data-w="${i}" style="--a:${c.sky[0]};--b:${c.sky[1]}" aria-pressed="${i === S.world}"><span>${String(i + 1).padStart(2, '0')}</span>${c.title}</button>`).join('');
  $('#title-scene').innerHTML = sceneSVG();
  const c = W0();
  $('#world-info').textContent = `${c.mascot.name} (${c.mascot.who}) · Rakip: ${c.rival.name} · ${c.place}`;
  $('#p1').value = S.names[0]; $('#p2').value = S.names[1];
}

const STORY = () => {
  const c = W0(), m = c.mascot.name, r = c.rival.name;
  return [
    ['n', `${c.place}.`],
    ['n', c.hook],
    ['m', `Burası da neresi böyle? Şu sisin içinden bağırışlar geliyor...`],
    ['r', `Hoş geldin ${m}! Ben ${r}. Burada böcekler kapışır, seyirci de coştu bile!`, 'hey'],
    ['r', `Kural basit: yere 5 böcek koyacağım. İyi bak, çünkü sonra üstlerine yaprağı kapatacağım.`],
    ['r', `Sen de her böceğin karşısına onu yenecek böceği koyacaksın. Yenersen 10 puan, berabere 3 puan.`],
    ['r', c.line],
    ['m', `Hazırım! Hadi bakalım, yaprağın altından ne çıkacak?`, 'hey'],
  ];
};
let storyIdx = 0;
function renderStory() {
  const st = STORY(), [who, text, fx] = st[storyIdx], c = W0();
  $('#story-scene').innerHTML = sceneSVG({ talk: who });
  $('#story-who').textContent = who === 'n' ? 'Anlatıcı' : who === 'm' ? c.mascot.name : c.rival.name;
  $('#story-box').className = 'dialog ' + who;
  $('#story-text').textContent = text;
  $('#story-step').textContent = `${storyIdx + 1}/${st.length}`;
  if (fx === 'hey') hey(3);
}

function slotHTML(i, content, cls = '') { return `<div class="slot ${cls}" data-i="${i}"><b>${i + 1}</b><div class="bugbox">${content}</div></div>`; }

function renderBoard() {
  const c = W0();
  $('#hud').innerHTML = `<div class="ps ${S.turn === 0 ? 'on' : ''}"><span>${esc(S.names[0])}</span><strong>${S.scores[0]}</strong></div><div class="rnd">Tur ${S.round}/${S.rounds}</div><div class="ps ${S.turn === 1 ? 'on' : ''}"><span>${esc(S.names[1])}</span><strong>${S.scores[1]}</strong></div>`;
  $('#play-scene').innerHTML = sceneSVG({ talk: 'r' });
  $('#rival-row').innerHTML = S.hidden.map((b, i) => slotHTML(i, b == null ? '' : bugSVG(b, 54) + `<i>${BUGS[b][0]}</i>`)).join('');
  $('#pick-row').innerHTML = S.picks.map((b, i) => slotHTML(i, b == null ? '<em>+</em>' : bugSVG(b, 54) + `<i>${BUGS[b][0]}</i>`, 'pick ' + (b == null ? 'empty' : 'filled'))).join('');
  $('#leaf').className = 'leaf';
}

async function startTurn() {
  S.hidden = Array(5).fill(null); S.picks = Array(5).fill(null);
  S.showTime = Math.max(2, 6 - S.round); // her tur daha az süre
  $('#turn-name').textContent = S.names[S.turn];
  $('#turn-sub').textContent = `Tur ${S.round}/${S.rounds} · ${W0().rival.name} böcekleri diziyor. ${S.showTime} saniye bakabileceksin.`;
  show('turn');
}

async function runShow() {
  show('play'); renderBoard();
  $('#msg').textContent = `${W0().rival.name}: "İyi bak!"`;
  $('#go').hidden = true; $('#pick-row').classList.add('locked');
  const pool = [...Array(20).keys()].sort(() => Math.random() - .5);
  for (let i = 0; i < 5; i++) { await sleep(320); S.hidden[i] = pool[i]; sfx.place(); renderBoard(); }
  for (let t = S.showTime; t > 0; t--) { $('#msg').textContent = `Ezberle! Yaprak ${t} saniye sonra kapanıyor...`; sfx.tick(); await sleep(1000); }
  $('#leaf').className = 'leaf down'; sfx.leaf(); hey(1);
  await sleep(250); $('#rival-row').classList.add('covered');
  await sleep(700);
  $('#msg').textContent = 'Yaprak kapandı! Her slotun altına, o böceği yenecek böceği koy.';
  $('#pick-row').classList.remove('locked');
}

function openPicker(i) {
  sfx.tap();
  $('#picker-title').textContent = `${i + 1}. slotun karşısına hangi böcek?`;
  $('#picker-grid').innerHTML = BUGS.map((b, k) => `<button class="pbug ${S.picks[i] === k ? 'on' : ''}" data-k="${k}">${bugSVG(k, 44)}<span>${b[0]}</span></button>`).join('');
  $('#picker-info').textContent = 'Bir böceğe dokun: kimleri yendiği burada görünür.';
  $('#picker').dataset.slot = i; $('#picker-ok').disabled = true; $('#picker').hidden = false;
}

async function fight() {
  $('#go').hidden = true; $('#pick-row').classList.add('locked');
  $('#rival-row').classList.remove('covered'); $('#leaf').className = 'leaf up'; sfx.leaf(); hey(2);
  await sleep(800);
  let gained = 0;
  for (let i = 0; i < 5; i++) {
    const a = S.picks[i], b = S.hidden[i], o = outcome(a, b);
    const top = $(`#rival-row .slot[data-i="${i}"]`), bot = $(`#pick-row .slot[data-i="${i}"]`);
    top.classList.add('clash'); bot.classList.add('clash');
    $('#msg').textContent = `${BUGS[a][0]} vs ${BUGS[b][0]}...`;
    await sleep(650);
    const pts = o === 1 ? 10 : o === 0 ? 3 : 0; gained += pts; S.scores[S.turn] += pts;
    bot.classList.add(o === 1 ? 'won' : o === 0 ? 'draw' : 'lost'); top.classList.add(o === -1 ? 'won' : o === 0 ? 'draw' : 'lost');
    bot.insertAdjacentHTML('beforeend', `<span class="res">${o === 1 ? 'KAZANDI +10' : o === 0 ? 'BERABERE +3' : 'YENİLDİ'}</span>`);
    (o === 1 ? sfx.win : o === 0 ? sfx.draw : sfx.lose)();
    $('#hud strong').parentElement; // hud yenile
    $('#hud').querySelectorAll('strong')[S.turn].textContent = S.scores[S.turn];
    await sleep(800);
  }
  $('#msg').textContent = `${S.names[S.turn]} bu turda ${gained} puan aldı!`;
  $('#next').hidden = false;
}

function nextTurn() {
  $('#next').hidden = true;
  if (S.turn === 0) S.turn = 1; else { S.turn = 0; S.round++; }
  if (S.round > S.rounds) return finish();
  startTurn();
}

function finish() {
  const [a, b] = S.scores, c = W0();
  const win = a === b ? -1 : a > b ? 0 : 1;
  $('#end-title').textContent = win < 0 ? 'Berabere!' : `${S.names[win]} kazandı!`;
  $('#end-scores').innerHTML = S.names.map((n, i) => `<div class="${i === win ? 'champ' : ''}"><span>${esc(n)}</span><strong>${S.scores[i]}</strong></div>`).join('');
  $('#end-line').textContent = win < 0 ? `${c.rival.name}: "İkiniz de iyi hatırladınız. Bir el daha?"` : `${c.rival.name}: "Tebrikler ${S.names[win]}! Seyirci senin adını bağırıyor!"`;
  $('#end-scene').innerHTML = sceneSVG({ talk: 'r' });
  show('end'); hey(4);
  const best = load('best', 0); const top = Math.max(a, b); if (top > best) save('best', top);
  $('#end-best').textContent = `En yüksek skor: ${Math.max(best, top)}`;
}

function renderGuide() {
  $('#guide-list').innerHTML = BUGS.map((b, k) => `<div class="gitem">${bugSVG(k, 56)}<div><b>${b[0]}</b><small>Yener: ${beats(k).map(x => BUGS[x][0]).join(', ')}</small><small class="eq">Berabere: ${BUGS[(k + 10) % 20][0]}</small></div></div>`).join('');
}

// ---------- Olaylar ----------
document.addEventListener('click', e => {
  const t = e.target.closest('button, .slot'); if (!t) return;
  if (t.matches('.world')) { S.world = +t.dataset.w; save('world', S.world); sfx.tap(); renderTitle(); return; }
  if (t.id === 'start') { audio(); S.names = [$('#p1').value.trim() || 'Oyuncu 1', $('#p2').value.trim() || 'Oyuncu 2']; save('names', S.names);
    S.round = 1; S.turn = 0; S.scores = [0, 0]; storyIdx = 0; show('story'); renderStory(); return; }
  if (t.id === 'story-next') { sfx.tap(); if (++storyIdx >= STORY().length) startTurn(); else renderStory(); return; }
  if (t.id === 'story-skip') { sfx.tap(); startTurn(); return; }
  if (t.id === 'turn-go') { audio(); runShow(); return; }
  if (t.matches('#pick-row .slot') && !$('#pick-row').classList.contains('locked')) { openPicker(+t.dataset.i); return; }
  if (t.matches('.pbug')) { const k = +t.dataset.k; document.querySelectorAll('.pbug').forEach(x => x.classList.toggle('on', x === t)); sfx.tap();
    $('#picker-info').innerHTML = `<b>${BUGS[k][0]}</b> yener: ${beats(k).map(x => BUGS[x][0]).join(', ')}`; $('#picker-ok').disabled = false; $('#picker-ok').dataset.k = k; return; }
  if (t.id === 'picker-ok') { const i = +$('#picker').dataset.slot; S.picks[i] = +t.dataset.k; $('#picker').hidden = true; sfx.place(); renderPicks(); return; }
  if (t.id === 'picker-close') { $('#picker').hidden = true; return; }
  if (t.id === 'go') { fight(); return; }
  if (t.id === 'next') { nextTurn(); return; }
  if (t.id === 'again') { S.round = 1; S.turn = 0; S.scores = [0, 0]; startTurn(); return; }
  if (t.id === 'home') { show('title'); renderTitle(); return; }
  if (t.id === 'guide-btn' || t.id === 'guide-btn2') { renderGuide(); $('#guide').hidden = false; return; }
  if (t.id === 'guide-close') { $('#guide').hidden = true; return; }
  if (t.id === 'mute') { muted = !muted; t.textContent = muted ? 'Ses: kapalı' : 'Ses: açık'; t.setAttribute('aria-pressed', muted); return; }
});
function renderPicks() {
  $('#pick-row').innerHTML = S.picks.map((b, i) => slotHTML(i, b == null ? '<em>+</em>' : bugSVG(b, 54) + `<i>${BUGS[b][0]}</i>`, 'pick ' + (b == null ? 'empty' : 'filled'))).join('');
  const done = S.picks.every(x => x != null);
  $('#go').hidden = !done;
  $('#msg').textContent = done ? 'Hepsi hazır. Kapıştır!' : `${S.picks.filter(x => x == null).length} slot kaldı. Slota dokun, böcek seç.`;
}

renderTitle(); show('title');
