// Behance arama sayfasını gerçek Chromium ile açar, ilk proje kartlarını listeler.
import { chromium } from '/opt/node-tools/node_modules/playwright/index.mjs';
const out = process.argv[2];
const PROXY_CA_SPKI = 'PS48cX347wDVcRynzq+DFqswl2PLNE1sG6uQvxMCOS0='; // yalnız ortam proxy CA'sına güven
const b = await chromium.launch({
  executablePath: '/opt/pw-browsers/chromium',
  headless: true,
  args: ['--no-sandbox', `--ignore-certificate-errors-spki-list=${PROXY_CA_SPKI}`],
});
const ctx = await b.newContext({
  viewport: { width: 1440, height: 1000 }, locale: 'en-US',
  userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36',
});
const p = await ctx.newPage();
p.on('response', r => {
  if (r.url().includes('behance.net/search') || r.status() >= 400) console.log('RESP', r.status(), r.url().slice(0, 140));
});
await p.goto('https://www.behance.net/search/projects/mobile%20app', { waitUntil: 'domcontentloaded', timeout: 60000 }).catch(e => console.log('GOTO', e.message.split('\n')[0]));
await p.waitForTimeout(10000);
console.log('title:', await p.title(), 'url:', p.url());
const links = await p.$$eval('a[href*="/gallery/"]', as => as.map(a => {
  const r = a.getBoundingClientRect();
  return { href: a.href, text: (a.innerText || a.getAttribute('aria-label') || a.title || '').trim().slice(0, 80), top: Math.round(r.top), left: Math.round(r.left) };
}));
const seen = new Set();
const uniq = links.filter(l => { const k = l.href.split('?')[0]; if (seen.has(k)) return false; seen.add(k); return true; });
console.log(JSON.stringify(uniq.slice(0, 10), null, 1));
await p.screenshot({ path: out + '/arama.png' });
await ctx.storageState({ path: out + '/state.json' });
await b.close();
