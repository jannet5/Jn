// Behance proje sayfasındaki Vimeo iframe'lerini oynatıp oynatıcı yapılandırmasını
// (config JSON) ve medya isteklerini yakalar.
import { chromium } from '/opt/node-tools/node_modules/playwright/index.mjs';
import fs from 'node:fs';
const [url, out] = process.argv.slice(2);
const PROXY_CA_SPKI = 'PS48cX347wDVcRynzq+DFqswl2PLNE1sG6uQvxMCOS0=';
const b = await chromium.launch({
  executablePath: '/opt/pw-browsers/chromium', headless: true,
  args: ['--no-sandbox', `--ignore-certificate-errors-spki-list=${PROXY_CA_SPKI}`, '--autoplay-policy=no-user-gesture-required'],
});
const ctx = await b.newContext({
  viewport: { width: 1440, height: 1000 }, locale: 'en-US',
  userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36',
});
const p = await ctx.newPage();
const log = [];
const configs = {};
p.on('response', async r => {
  const u = r.url();
  if (!/vimeo|vimeocdn|akamaized|fastly/.test(u)) return;
  const ct = r.headers()['content-type'] || '';
  log.push({ s: r.status(), ct, u });
  if (/player\.vimeo\.com\/video\/\d+\/config/.test(u) || (/player\.vimeo\.com\/video\/\d+/.test(u) && ct.includes('html'))) {
    try { configs[u] = await r.text(); } catch {}
  }
});
await p.goto(url, { waitUntil: 'domcontentloaded', timeout: 60000 });
await p.waitForTimeout(6000);
const frames = await p.$$('iframe[src*="vimeo"]');
for (const f of frames) { await f.scrollIntoViewIfNeeded(); await p.waitForTimeout(8000); }
fs.writeFileSync(out + '/vimeo-ag.json', JSON.stringify(log, null, 1));
fs.writeFileSync(out + '/vimeo-config.json', JSON.stringify(configs));
for (const fr of p.frames()) if (fr.url().includes('vimeo')) {
  const info = await fr.evaluate(() => { const v = document.querySelector('video'); return v ? { src: v.currentSrc, w: v.videoWidth, h: v.videoHeight, d: v.duration } : document.body.innerText.slice(0, 300); }).catch(e => 'ERR ' + e.message);
  console.log('FRAME', fr.url().slice(0, 90), JSON.stringify(info));
}
await b.close();
console.log('log', log.length, 'configs', Object.keys(configs).length);
