// Kullanım: node uret.mjs [filtre]   (sosyal/sablonlar içinden)
// HTML → Playwright (Chromium /opt/pw-browsers) → PNG; otomatik kontrol: kontrast ≥4.5, taşma, yetim satır.
import { createRequire } from 'node:module';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { sayfa } from './stil.mjs';
import { ICERIK } from './icerik.mjs';

const require = createRequire(import.meta.url);
let pw; try { pw = require('playwright'); } catch { pw = require('/opt/node22/lib/node_modules/playwright'); }
const DIR = path.dirname(fileURLToPath(import.meta.url));
const KOK = path.resolve(DIR, '..');
const filtre = process.argv[2] || '';
const OLCU = { '45': { w: 420, h: 525, dsf: 18 / 7 }, '916': { w: 360, h: 640, dsf: 3 }, '11': { w: 360, h: 360, dsf: 3 } };

const KONTROL = () => {
  const lum = c => { const m = c.match(/[\d.]+/g).map(Number); const f = v => { v /= 255; return v <= .03928 ? v / 12.92 : ((v + .055) / 1.055) ** 2.4; }; return [.2126 * f(m[0]) + .7152 * f(m[1]) + .0722 * f(m[2]), m[3] ?? 1]; };
  const bgOf = el => { for (let e = el; e; e = e.parentElement) { const b = getComputedStyle(e).backgroundColor; const a = b.match(/[\d.]+/g); if (a && (a[3] === undefined || +a[3] > .5)) return b; } return 'rgb(255,255,255)'; };
  const kok = document.querySelector('.s'); const K = kok.getBoundingClientRect(); const hatalar = [];
  // flex .main içeriği sığmıyorsa komşu bloklara biner (çakışma)
  for (const m of kok.querySelectorAll('.main')) { const mr = m.getBoundingClientRect(); for (const c of m.children) { const cr = c.getBoundingClientRect(); if (cr.top < mr.top - 1 || cr.bottom > mr.bottom + 1) hatalar.push(`ÇAKIŞMA: .main içinde <${c.tagName.toLowerCase()} class="${c.className}">`); } }
  for (const el of kok.querySelectorAll('*')) {
    if (el.closest('[data-dek]') || el.closest('svg')) continue;
    const txt = [...el.childNodes].filter(n => n.nodeType === 3 && n.textContent.trim()).map(n => n.textContent.trim()).join(' ');
    const r = el.getBoundingClientRect();
    if (r.width && (r.left < K.left - .5 || r.right > K.right + .5 || r.top < K.top - .5 || r.bottom > K.bottom + .5) && !el.closest('.tel'))
      hatalar.push(`TAŞMA: <${el.tagName.toLowerCase()} class="${el.className}"> ${txt.slice(0, 30)}`);
    if (!txt) continue;
    const [l1] = lum(getComputedStyle(el).color), [l2] = lum(bgOf(el));
    const oran = (Math.max(l1, l2) + .05) / (Math.min(l1, l2) + .05);
    if (oran < 4.5) hatalar.push(`KONTRAST ${oran.toFixed(2)}: "${txt.slice(0, 40)}"`);
    if (el.scrollWidth > el.clientWidth + 1 && getComputedStyle(el).overflow !== 'visible') hatalar.push(`KIRPILMA: "${txt.slice(0, 30)}"`);
    // yetim satır: çok satırlı metinde son satırda tek kelime
    if (/^(P|DIV|SPAN|LI)$/.test(el.tagName) && !el.closest('.tel')) {
      const rg = document.createRange(); rg.selectNodeContents(el);
      const rects = [...rg.getClientRects()].filter(x => x.width > 1);
      const satirlar = {}; rects.forEach(x => { const k = Math.round(x.top / 4); satirlar[k] = (satirlar[k] || 0) + x.width; });
      const ks = Object.keys(satirlar).map(Number).sort((a, b) => a - b);
      if (ks.length > 1 && txt.includes(' ')) { const son = el.innerText.trim().split('\n').pop().trim(); if (son && !son.includes(' ') && son.length < 10) hatalar.push(`YETİM: "${son}" (${txt.slice(0, 30)})`); }
    }
  }
  return hatalar;
};

const tarayici = await pw.chromium.launch({ executablePath: '/opt/pw-browsers/chromium' });
const sayfalar = {};
let toplam = 0, hataToplam = 0;
fs.mkdirSync(path.join(DIR, 'html'), { recursive: true });
for (const o of ICERIK) {
  if (filtre && !o.id.includes(filtre)) continue;
  const { w, h, dsf } = OLCU[o.boyut];
  const pg = sayfalar[o.boyut] ??= await (await tarayici.newContext({ viewport: { width: w, height: h }, deviceScaleFactor: dsf })).newPage();
  const cikti = path.join(KOK, o.klasor); fs.mkdirSync(cikti, { recursive: true });
  for (const [i, html] of o.slaytlar.entries()) {
    const ad = o.slaytlar.length > 1 ? `${o.id}-${String(i + 1).padStart(2, '0')}` : o.id;
    const hp = path.join(DIR, 'html', `${ad}.html`);
    fs.writeFileSync(hp, sayfa(html, ad));
    await pg.goto('file://' + hp); await pg.evaluate(() => document.fonts.ready);
    const hatalar = await pg.evaluate(KONTROL);
    const dosya = o.klasor === 'reklam' || o.klasor === 'profil' ? `${o.id}.png` : (o.slaytlar.length > 1 ? `${String(i + 1).padStart(2, '0')}.png` : 'gorsel.png');
    await pg.screenshot({ path: path.join(cikti, dosya), clip: { x: 0, y: 0, width: w, height: h } });
    toplam++; if (hatalar.length) { hataToplam += hatalar.length; console.log(`✗ ${ad}\n   ` + hatalar.join('\n   ')); }
  }
}
await tarayici.close();
console.log(`${toplam} görsel üretildi, ${hataToplam} uyarı.`);
