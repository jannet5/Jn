// Behance proje sayfasını gerçek Chromium ile açar; sayfa durumu (JSON), DOM medya
// referansları, ağ medya yanıtları ve tam sayfa ekran görüntüsünü kaydeder.
import { chromium } from '/opt/node-tools/node_modules/playwright/index.mjs';
import fs from 'node:fs';
const [url, out] = process.argv.slice(2);
const PROXY_CA_SPKI = 'PS48cX347wDVcRynzq+DFqswl2PLNE1sG6uQvxMCOS0=';
const b = await chromium.launch({
  executablePath: '/opt/pw-browsers/chromium', headless: true,
  args: ['--no-sandbox', `--ignore-certificate-errors-spki-list=${PROXY_CA_SPKI}`],
});
const ctx = await b.newContext({
  viewport: { width: 1440, height: 1000 }, locale: 'en-US',
  userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36',
});
const p = await ctx.newPage();
const net = [];
p.on('response', r => {
  const ct = r.headers()['content-type'] || '';
  if (/image|video|mpegurl|octet/.test(ct)) net.push({ status: r.status(), ct, url: r.url() });
});
await p.goto(url, { waitUntil: 'domcontentloaded', timeout: 60000 }).catch(e => console.log('GOTO', e.message.split('\n')[0]));
await p.waitForTimeout(8000);
// Tembel yüklenen modüller için sayfayı sonuna kadar kaydır
let last = 0;
for (let i = 0; i < 80; i++) {
  await p.mouse.wheel(0, 900);
  await p.waitForTimeout(350);
  const h = await p.evaluate(() => document.body.scrollHeight);
  const y = await p.evaluate(() => window.scrollY + window.innerHeight);
  if (y >= h - 5 && h === last) break;
  last = h;
}
await p.waitForTimeout(3000);
console.log('title:', await p.title(), 'url:', p.url());
const html = await p.content();
fs.writeFileSync(out + '/proje.html', html);
const dom = await p.evaluate(() => {
  const r = [];
  document.querySelectorAll('img').forEach(i => r.push({ tag: 'img', src: i.currentSrc || i.src, srcset: i.srcset || '', alt: i.alt || '' }));
  document.querySelectorAll('video, video source').forEach(v => r.push({ tag: v.tagName.toLowerCase(), src: v.currentSrc || v.src || '', poster: v.poster || '' }));
  document.querySelectorAll('iframe').forEach(f => r.push({ tag: 'iframe', src: f.src }));
  document.querySelectorAll('picture source').forEach(s => r.push({ tag: 'source', srcset: s.srcset }));
  return r;
});
fs.writeFileSync(out + '/dom-medya.json', JSON.stringify(dom, null, 1));
fs.writeFileSync(out + '/ag-medya.json', JSON.stringify(net, null, 1));
await p.screenshot({ path: out + '/proje-tam.png', fullPage: true }).catch(e => console.log('SHOT', e.message));
await ctx.storageState({ path: out + '/state-proje.json' });
await b.close();
console.log('dom', dom.length, 'net', net.length, 'html', html.length);
