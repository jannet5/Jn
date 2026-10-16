// Google Play mağaza görselleri: simge 512, öne çıkan görsel 1024x500, telefon ekran görüntüleri 1080x1920
// Kullanım: node make_store_assets.mjs http://localhost:8765/index.html play/assets
import { createRequire } from 'module';
const require = createRequire('/opt/node-tools/node_modules/');
const { chromium } = require('playwright-core');
import fs from 'fs';
const [url, out] = process.argv.slice(2);
fs.mkdirSync(out, { recursive: true });
const b = await chromium.launch({ executablePath: process.env.CHROME || '/opt/pw-browsers/chromium-1194/chrome-linux/chrome' });
const ctx = await b.newContext({ viewport: { width: 360, height: 640 }, deviceScaleFactor: 3, hasTouch: true, isMobile: true, locale: 'tr-TR' });
const p = await ctx.newPage();
await p.goto(url); await p.waitForFunction(() => window.__kuranReady === true);
const set = async (theme, fs) => p.evaluate(([t, f]) => { localStorage.setItem('q.theme', JSON.stringify(t)); window.__kuran.setFs(f); }, [theme, fs]);
async function shot(name, setup) { await setup(); await p.waitForTimeout(350); await p.screenshot({ path: `${out}/${name}.png` }); console.log(name); }
const theme = async (t) => { await p.evaluate((x) => { const r = document.documentElement; if (x) r.setAttribute('data-theme', x); else r.removeAttribute('data-theme'); }, t); };
await theme('light');
await shot('ekran-1-sayfa', async () => { await p.evaluate(() => { window.__kuran.setFs(30); window.__kuran.goTo(2); }); });
await shot('ekran-2-buyuk-yazi', async () => { await p.evaluate(() => { window.__kuran.setFs(46); window.__kuran.goTo(1); }); });
await shot('ekran-3-git-sure', async () => { await p.evaluate(() => { window.__kuran.setFs(30); window.__kuran.goTo(106, { surah: 4 }); }); await p.click('#bGit'); await p.fill('#fSure', 'ma'); await p.evaluate(() => document.activeElement.blur()); });
await p.keyboard.press('Escape'); await p.waitForTimeout(200);
await shot('ekran-4-git-cuz', async () => { await p.click('#bGit'); await p.fill('#fCuz', '1'); await p.evaluate(() => document.activeElement.blur()); });
await p.keyboard.press('Escape'); await p.waitForTimeout(200);
await theme('dark');
await shot('ekran-5-gece', async () => { await p.evaluate(() => { window.__kuran.setFs(32); window.__kuran.goTo(440); }); });
await theme('sepia');
await shot('ekran-6-sepya', async () => { await p.evaluate(() => { window.__kuran.setFs(32); window.__kuran.goTo(50); }); });
await theme('light');
// Öne çıkan görsel 1024x500: sol metin, sağda iki telefon
const f = await b.newPage({ viewport: { width: 1024, height: 500 }, deviceScaleFactor: 1 });
const img = (n) => 'data:image/png;base64,' + fs.readFileSync(`${out}/${n}.png`).toString('base64');
const icon = 'data:image/svg+xml;base64,' + fs.readFileSync(new URL('../www/icon.svg', import.meta.url)).toString('base64');
await f.setContent(`<style>
 *{box-sizing:border-box} body{margin:0;width:1024px;height:500px;background:#1f6f5c;font-family:system-ui,Roboto,sans-serif;position:relative;overflow:hidden;color:#fff}
 .t{position:absolute;left:56px;top:120px;width:470px} .t img{width:84px;height:84px;border-radius:20px;display:block;margin-bottom:22px}
 h1{font-size:54px;margin:0 0 12px;line-height:1.05;letter-spacing:-.5px} p{font-size:24px;margin:0;opacity:.92;line-height:1.35}
 .ph{position:absolute;width:236px;height:420px;border-radius:26px;border:6px solid #0d2e26;overflow:hidden;box-shadow:0 14px 34px rgba(0,0,0,.4);background:#000}
 .ph img{width:100%;height:100%;object-fit:cover;display:block} .a{left:560px;top:62px;transform:rotate(-4deg)} .c{left:790px;top:100px;transform:rotate(4deg)}
</style><div class="t"><img src="${icon}"><h1>Kur'an Okuyucu</h1><p>Hafif, hızlı, çevrimdışı.<br>Yazıyı büyüt, satırlar yeniden dizilsin.</p></div>
<div class="ph a"><img src="${img('ekran-1-sayfa')}"></div><div class="ph c"><img src="${img('ekran-5-gece')}"></div>`);
await f.waitForTimeout(500);
await f.screenshot({ path: `${out}/one-cikan-gorsel-1024x500.png` });
// Simge 512
const i = await b.newPage({ viewport: { width: 512, height: 512 } });
await i.goto(new URL('../www/icon.svg', import.meta.url).href.replace('file://', 'file://'));
await i.screenshot({ path: `${out}/simge-512.png` });
await b.close();
console.log('tamam');
