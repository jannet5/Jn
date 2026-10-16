// Tarayıcıda 604 sayfanın hepsini çizer; ekrandaki ayet metni data.js ile BİREBİR aynı mı, ayetler sırayla eksiksiz mi kontrol eder.
// Kullanım: node verify_render.mjs http://localhost:8765/index.html
import { createRequire } from 'module';
const require = createRequire('/opt/node-tools/node_modules/');
const { chromium } = require('playwright-core');
const b = await chromium.launch({ executablePath: process.env.CHROME || '/opt/pw-browsers/chromium-1194/chrome-linux/chrome' });
const p = await b.newPage({ viewport: { width: 390, height: 844 } });
await p.goto(process.argv[2]); await p.waitForFunction(() => window.__kuranReady === true);
const r = await p.evaluate(() => {
  const out = { pages: 0, ayetToplam: 0, farkli: [], bosSayfa: [], besmele: 0, stil: null };
  let gi = 0;                                                 // beklenen sonraki global ayet indeksi
  for (let pg = 1; pg <= 604; pg++) {
    window.__kuran.goTo(pg);
    const c = document.getElementById('pgC');
    const spans = Array.from(c.querySelectorAll('.t'));
    const txt = spans.map(e => { const k = e.cloneNode(true); k.querySelectorAll('.e').forEach(x => x.remove()); return k.textContent; }).join(' ');
    const expect = Q.a.slice(Q.p[pg - 1], Q.p[pg]).join(' ').split(' ').join(' ');
    const got = txt.replace(/\s+/g, ' ').trim(), exp = expect.replace(/\s+/g, ' ').trim();
    if (got !== exp) out.farkli.push(pg);
    if (!got) out.bosSayfa.push(pg);
    out.ayetToplam += Q.p[pg] - Q.p[pg - 1];
    if (Q.p[pg - 1] !== gi) out.farkli.push('sira@' + pg);   // ayetler sırayla ve boşluksuz mu
    gi = Q.p[pg];
    out.besmele += c.querySelectorAll('.bsm').length;
    out.pages++;
  }
  const t = getComputedStyle(document.querySelector('#pgC .t'));
  out.stil = { letterSpacing: t.letterSpacing, textTransform: t.textTransform, wordBreak: t.wordBreak, hyphens: t.hyphens, fontFeature: t.fontFeatureSettings, font: t.fontFamily.slice(0, 30) };
  return out;
});
console.log(JSON.stringify(r));
const ok = r.pages === 604 && r.ayetToplam === 6236 && r.farkli.length === 0 && r.bosSayfa.length === 0 && r.besmele === 112;
console.log(ok ? 'OK: 604 sayfa, 6236 ayet, ekrandaki metin veriyle birebir aynı, besmele başlığı 112 (beklenen 112)' : 'FAIL');
await b.close(); process.exit(ok ? 0 : 1);
